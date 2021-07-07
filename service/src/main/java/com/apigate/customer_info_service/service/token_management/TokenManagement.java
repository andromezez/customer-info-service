package com.apigate.customer_info_service.service.token_management;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.token_management.CreateTokenResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.token_management.RefreshTokenResDto;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.repository.MnoRepository;
import com.apigate.customer_info_service.service.CacheService;
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
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Bayu Utomo
 * @date 6/7/2021 11:29 AM
 */
@Component
public class TokenManagement {

    @Autowired
    private MnoRepository mnoRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TaskScheduler taskScheduler;


    private String getTokenAccessRedisKey(Mno mno) {
        return "token:" + mno.getId() + ":access";
    }

    private String getTokenRefreshRedisKey(Mno mno) {
        return "token:" + mno.getId() + ":refresh";
    }

    private Duration getTokenExpirePeriod(Mno mno){
        return cacheService.getRemainingTTL(getTokenAccessRedisKey(mno));
    }

    @EventListener
    public void checkTokenAfterStartup(ApplicationReadyEvent event) {
        var mnoList = mnoRepository.findAll();
        for(var mno : mnoList){
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i=1;i<=10;i++){
                        try {
                            ServicesLog.getInstance().logInfo("testing event listener execute background task");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            ServicesLog.getInstance().logError(e);
                        }
                    }
                    //renewToken(mno);
                }
            });
        }
    }

    public void renewToken(Mno mno){
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

            String newAccessToken = null, newRefreshToken = null;
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
                }

            }else{//If error or false, then call token create
                var createTokenHttpRequest= new CreateTokenHttpRequest(mno.getTokenUrl(),mno.getUsername(),mno.getPassword(),authKey);
                HttpClientUtils.HttpResponse createResponse = null;
                try{
                    createResponse = HttpClientUtils.executeRequest(createTokenHttpRequest.getRequest());
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
                        }
                    }
                }catch (Exception e){
                    ServicesLog.getInstance().logError(e);
                }
            }

            if(StringUtils.isNoneBlank(newAccessToken,newRefreshToken) && newExpireIn > 0){ //once success getting new valid token from refresh process or create process, then set scheduler to keep track refresh token based on new expiry
                cacheService.createCache(getTokenAccessRedisKey(mno),newAccessToken,newExpireIn);
                cacheService.createCache(getTokenRefreshRedisKey(mno),newRefreshToken,newExpireIn);

                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, Instant.now().plusSeconds(newExpireIn-Config.getTtlLeftBeforeRefreshToken().getSeconds()));

                cacheHasBeenRenewed = true;
            }
        }

        if(!cacheHasBeenRenewed){
            if(expireInFromCache.getSeconds()>0){ //no renew (process failed), but there is still existing cache, then set scheduler to keep track refresh token based on expiry of the existing token

            }else{ //no renew (process failed) the token from refresh process and from create process, and no existing cache as well

            }
        }
    }
}
