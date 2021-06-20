package com.apigate.apis.rest.customer_info_service;

import com.apigate.apis.rest.util.HTTPUtils;
import com.apigate.exceptions.internal.SystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Controller
@RequestMapping("/")
public class DefaultController extends AbstractController{

    /*@RequestMapping(value="**")
    public ResponseEntity getAnythingelse(HttpServletRequest request) {
        ResponseEntity responseEntity = null;
        InitiatedData initiatedData = null;

        try{
            initiatedData = initiateDataAndLogRequest(HTTPUtils.extractRequestBody(request), request, HttpStatus.OK, Thread.currentThread().getStackTrace()[1].getMethodName());
            responseEntity = initiatedData.responseEntity;

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }
        finally {
            logResponse(initiatedData.requestLog, responseEntity);
        }
        return responseEntity;
    }*/

    @RequestMapping(value="**")
    public ResponseEntity getAnythingelse(HttpServletRequest request) {
        ResponseEntity responseEntity = null;
        InitiatedData initiatedData = null;

        try{
            initiatedData = initiateDataAndLogRequest(HTTPUtils.extractRequestBody(request), request, HttpStatus.OK, Thread.currentThread().getStackTrace()[1].getMethodName());
            responseEntity = initiatedData.responseEntity;

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
