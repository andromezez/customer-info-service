package com.apigate.apis.rest.customer_info_service;

import com.apigate.apis.rest.util.HTTPUtils;
import com.apigate.customer_info_service.service.CacheService;
import com.apigate.customer_info_service.service.RoutingService;
import com.apigate.exceptions.business.BusinessValidationException;
import com.apigate.exceptions.internal.SystemErrorException;
import com.apigate.logging.ServicesLog;
import com.apigate.utils.httpclient.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.time.Duration;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Controller
@RequestMapping("/")
public class DefaultController extends AbstractController{

    @Autowired
    private RoutingService routingService;

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value="**")
    public ResponseEntity getAnythingElse(@RequestHeader(name = "application_id", required = false) String partnerId, HttpServletRequest request) {
        ResponseEntity responseEntity = null;
        InitiatedData initiatedData = null;

        try{
            initiatedData = initiateDataAndLogRequest(HTTPUtils.extractRequestBody(request), request, HttpStatus.OK, Thread.currentThread().getStackTrace()[1].getMethodName());
            responseEntity = initiatedData.responseEntity;

            if(StringUtils.isNotBlank(partnerId)){
                var routing = routingService.findRouting(request, partnerId);
                if(routing.isPresent()){
                    String cacheResponse = null;
                    try{
                        cacheResponse = cacheService.getFromCache(routing.get().getRedisKey());
                    }catch (Exception e){
                        ServicesLog.getInstance().logError(e);
                    }

                    if(StringUtils.isNotBlank(cacheResponse)){
                        ServicesLog.getInstance().logInfo("Cache is found");
                        responseEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cacheResponse);
                    }else{
                        ServicesLog.getInstance().logInfo("Cache not found. Cache is " + cacheResponse);
                        var httpResponse = HttpClientUtils.executeGetRequest(HttpClientUtils.subtitutePath(new URL(routing.get().getMnoApiEndpoint().getUrl()), request));
                        if(httpResponse.isResponseComplete()){
                            try{
                                cacheService.createCache(routing.get(),httpResponse.getBody());
                            }catch (Exception e){
                                ServicesLog.getInstance().logError(e);
                            }
                            responseEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(httpResponse.getBody());
                        }else{
                            String errorMessage = "Operator endpoint " + routing.get().getMnoApiEndpoint().getUrl()
                                    + " doesn't return proper response.";
                            String errorDetails = errorMessage
                                    + "\n    response code : " + httpResponse.getCode()
                                    + "\n    reason phrase : " + httpResponse.getReasonPhrase()
                                    + "\n    response body : " + httpResponse.getBody()
                                    + "\n    response headers : " + HttpClientUtils.HttpResponse.toString(httpResponse.getHeaders());
                            var errorForLog = new BusinessValidationException(errorDetails);
                            ServicesLog.getInstance().logError(errorForLog);

                            var errorForClient = new BusinessValidationException(errorMessage);
                            throw errorForClient;
                        }
                    }
                }else{
                    throw new BusinessValidationException("Can't find routing for partnerId " + partnerId
                            + " and URI " + request.getRequestURI() + " , under " + RoutingService.LOCK_ON_OPERATOR + " operator.");
                }
            }
        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }
        finally {
            logResponse(initiatedData.requestLog, responseEntity);
        }
        return responseEntity;
    }

    @GetMapping("test-error")
    public ResponseEntity getTestError(HttpServletRequest request) {
        ResponseEntity responseEntity = null;
        InitiatedData initiatedData = null;
        try{
            initiatedData = initiateDataAndLogRequest(HTTPUtils.extractRequestBody(request), request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
            responseEntity = initiatedData.responseEntity;

            throw new SystemErrorException(new Exception("Intentionally throw exception to test GCP stackdriver output on printing exception's stack traces"));

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }
        finally {
            logResponse(initiatedData.requestLog, responseEntity);
        }
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("test-redis")
    public ResponseEntity testRedis(@RequestHeader(name = "key", required = false) String key, @RequestHeader(name = "value", required = false) String value, HttpServletRequest request) {
        ResponseEntity responseEntity = null;
        InitiatedData initiatedData = null;
        try{
            initiatedData = initiateDataAndLogRequest(HTTPUtils.extractRequestBody(request), request, HttpStatus.OK, Thread.currentThread().getStackTrace()[1].getMethodName());
            responseEntity = initiatedData.responseEntity;

            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, Duration.ofSeconds(5));

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }
        finally {
            logResponse(initiatedData.requestLog, responseEntity);
        }
        return responseEntity;
    }
}
