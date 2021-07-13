package com.apigate.customer_info_service.service.token_management;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.token_management.CreateTokenResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.token_management.RefreshTokenResDto;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.repository.MnoRepository;
import com.apigate.customer_info_service.service.CacheService;
import com.apigate.logging.RequestIDGenerator;
import com.apigate.logging.ServicesLog;
import com.apigate.utils.httpclient.HttpClientUtils;
import com.apigate.utils.json_processor.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bayu Utomo
 * @date 6/7/2021 11:29 AM
 */
@Service
public class TokenManagement {

    @Autowired
    private MnoRepository mnoRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TaskScheduler taskScheduler;


    public String getTokenAccessRedisKey(Mno mno) {
        return "token:" + mno.getId() + ":access";
    }

    private String getTokenRefreshRedisKey(Mno mno) {
        return "token:" + mno.getId() + ":refresh";
    }

    private String getTokenTypeRedisKey(Mno mno) {
        return "token:" + mno.getId() + ":token-type";
    }

    private Duration getTokenExpirePeriod(Mno mno){
        return cacheService.getRemainingTTL(getTokenAccessRedisKey(mno));
    }

    @EventListener
    public void initTokenAfterStartup(ApplicationReadyEvent event) {
        taskScheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                com.apigate.logging.Logger.registerReqId(RequestIDGenerator.generateId());
                ServicesLog.getInstance().logInfo("Start Token management scheduler");
                var mnoList = mnoRepository.findAll();
                AtomicInteger completedTask = new AtomicInteger(0);
                for(var mno : mnoList){
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                com.apigate.logging.Logger.registerReqId(RequestIDGenerator.generateId());
                                renewToken(mno);
                            }catch (Exception e){
                                ServicesLog.getInstance().logError(e);
                            }finally {
                                completedTask.incrementAndGet();
                                com.apigate.logging.Logger.removeDianosticContext();
                            }
                        }
                    });
                }
                while (mnoList.size() > completedTask.get() ){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
                ServicesLog.getInstance().logInfo("Finish Token management scheduler");
                com.apigate.logging.Logger.removeDianosticContext();
            }
        },Config.getRefreshTokenSchedulerPeriod());
    }

    private void renewToken(Mno mno){
        ServicesLog.getInstance().logInfo("Token management trying to refresh token for operator " + mno.getName());
        var expireInFromCache = getTokenExpirePeriod(mno);
        boolean cacheHasBeenRenewed = false;
        if(expireInFromCache.getSeconds() <= Config.getTtlLeftBeforeRefreshToken().getSeconds()){ // try to refresh or create even if the token cache doesn't exist

            //first call token refresh.
            String refreshTokenFromCache = cacheService.getFromCache(getTokenRefreshRedisKey(mno));
            String authKey = mno.getAuthKey();
            var refreshTokenHttpRequest= new RefreshTokenHttpRequest(mno.getTokenUrl(),refreshTokenFromCache,authKey);
            HttpClientUtils.HttpResponse refreshResponse = null;

            try{
                refreshResponse = HttpClientUtils.executeRequest(refreshTokenHttpRequest.getRequest());
            }catch (Exception e){
                ServicesLog.getInstance().logError(e);
            }

            String newAccessToken = null, newRefreshToken = null, newTokenType=null;
            int newExpireIn = 0;

            if ((refreshResponse != null)
                    && refreshResponse.isResponseComplete()
                    && (refreshResponse.getCode() == HttpStatus.OK.value() || refreshResponse.getCode() == HttpStatus.CREATED.value())) {

                ObjectMapper mapper = ObjectMapperUtils.getMapperInstance();
                RefreshTokenResDto refreshResponseBody = null;
                try {
                    refreshResponseBody = mapper.readValue(refreshResponse.getBody(), RefreshTokenResDto.class);
                } catch (JsonProcessingException e) {
                    ServicesLog.getInstance().logError(e);
                }finally {
                    ObjectMapperUtils.returnToPool(mapper);
                }

                if(refreshResponseBody != null){
                    newAccessToken = refreshResponseBody.getAccessToken();
                    newRefreshToken = refreshResponseBody.getRefreshToken();
                    newExpireIn = refreshResponseBody.getExpiresIn();
                    newTokenType = refreshResponseBody.getTokenType();
                }

            }else{//If error or false, then call token create
                var createTokenHttpRequest= new CreateTokenHttpRequest(mno.getTokenUrl(),mno.getUsername(),mno.getPassword(),authKey);
                HttpClientUtils.HttpResponse createResponse = null;

                try{
                    createResponse = HttpClientUtils.executeRequest(createTokenHttpRequest.getRequest());
                }catch (Exception e){
                    ServicesLog.getInstance().logError(e);
                }

                if((createResponse != null)
                        && createResponse.isResponseComplete()
                        && (createResponse.getCode() == HttpStatus.OK.value() || createResponse.getCode() == HttpStatus.CREATED.value())){

                    ObjectMapper mapper = ObjectMapperUtils.getMapperInstance();
                    CreateTokenResDto createResponseBody = null;
                    try {
                        createResponseBody = mapper.readValue(refreshResponse.getBody(), CreateTokenResDto.class);
                    } catch (JsonProcessingException e) {
                        ServicesLog.getInstance().logError(e);
                    }finally {
                        ObjectMapperUtils.returnToPool(mapper);
                    }

                    if(createResponseBody != null){
                        newAccessToken = createResponseBody.getAccessToken();
                        newRefreshToken = createResponseBody.getRefreshToken();
                        newExpireIn = createResponseBody.getExpiresIn();
                        newTokenType = createResponseBody.getTokenType();
                    }
                }
            }

            if(StringUtils.isNoneBlank(newAccessToken,newRefreshToken,newTokenType) && newExpireIn > 0){ //success getting new valid token from refresh process or create process
                cacheService.createCache(getTokenAccessRedisKey(mno), newAccessToken, newExpireIn);
                cacheService.createCache(getTokenRefreshRedisKey(mno), newRefreshToken, newExpireIn);
                cacheService.createCache(getTokenTypeRedisKey(mno), newRefreshToken, newExpireIn);

                cacheHasBeenRenewed = true;
            }
        }

        if (!cacheHasBeenRenewed) {
            if (expireInFromCache.getSeconds() > Config.getTtlLeftBeforeRefreshToken().getSeconds()) { //no renew, since token cache still valid for more than the expiry limit
                ServicesLog.getInstance().logInfo("No refresh token. Token still valid for " + expireInFromCache.getSeconds() + " seconds. Not reaching the expiry limit.");
            } else if (expireInFromCache.getSeconds() > 0) { //no renew (process failed), but there is still existing cache
                ServicesLog.getInstance().logError(new Exception("Failure on the process. Token is not refreshed. Token will expire in approx " + expireInFromCache.getSeconds() + " seconds."));
            } else {
                ServicesLog.getInstance().logError(new Exception("Process failed to get the token from refresh process and from create process, and no existing cache as well."));
            }
        } else {
            ServicesLog.getInstance().logInfo("Token is refreshed");
        }
    }
}
