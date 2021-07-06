package com.apigate.customer_info_service.service.token_management;

import com.apigate.config.Config;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.repository.MnoRepository;
import com.apigate.customer_info_service.service.CacheService;
import com.apigate.logging.ServicesLog;
import com.apigate.utils.httpclient.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;

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
                    var expireIn = getTokenExpirePeriod(mno);
                    if(expireIn.getSeconds() <= Config.getTtlLeftBeforeRefreshToken().getSeconds()){

                        //first call token refresh.
                        String refreshToken = cacheService.getFromCache(getTokenRefreshRedisKey(mno));
                        String authKey = mno.getAuthKey();
                        var refreshTokenHttpRequest= new RefreshTokenHttpRequest(mno.getTokenUrl(),refreshToken,authKey);
                        HttpClientUtils.HttpResponse refreshResponse = null;
                        try{
                            refreshResponse = HttpClientUtils.executeRequest(refreshTokenHttpRequest.getRequest());
                        }catch (Exception e){
                            ServicesLog.getInstance().logError(e);
                        }

                        if ((refreshResponse != null)
                                && refreshResponse.isResponseComplete()
                                && (refreshResponse.getCode() == HttpStatus.OK.value() || refreshResponse.getCode() == HttpStatus.CREATED.value())) {
                            //TODO: extract token from response body
                        }else{//If error or false, then call token create
                            var createTokenHttpRequest= new CreateTokenHttpRequest(mno.getTokenUrl(),mno.getUsername(),mno.getPassword(),authKey);
                            HttpClientUtils.HttpResponse createResponse = null;
                            try{
                                createResponse = HttpClientUtils.executeRequest(createTokenHttpRequest.getRequest());
                                if((createResponse != null)
                                        && createResponse.isResponseComplete()
                                        && (createResponse.getCode() == HttpStatus.OK.value() || createResponse.getCode() == HttpStatus.CREATED.value())){
                                    //TODO: extract token from response body
                                }
                            }catch (Exception e){
                                ServicesLog.getInstance().logError(e);
                            }
                        }



                        //TODO: once success getting valid token, then set scheduler to keep track refresh token based on expiry
                    }
                }
            });
        }
    }
}
