package com.apigate.apis.rest.customer_info_service;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.HTTPResponseBody.ErrorInfo;
import com.apigate.exceptions.internal.ErrorException;
import com.apigate.logging.HTTPRequestLog;
import com.apigate.logging.HTTPResponseLog;
import com.apigate.logging.Logger;
import com.apigate.utils.json_processor.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bayu Utomo
 * @date 10/6/2021 9:27 AM
 */
public class AbstractController {
    protected static final String RESPONSE_ENTITY_INITIAL_VALUE = "The initial value should have been overridden";

    protected class ExceptionResponse {
        ResponseEntity<Object> responseEntity;
        AbstractException ex;
    }

    protected ExceptionResponse processException(HttpServletRequest request, Exception e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        AbstractException abstractException;
        if(e instanceof AbstractException){
            abstractException = ((AbstractException) e);
        }else{
            abstractException = new ErrorException(e);
        }
        var mapper = ObjectMapperUtils.getMapperInstance();
        ResponseEntity<Object> responseEntity = ResponseEntity
                .status(abstractException.getStatus())
                .body(abstractException.getErrorInfo(request).toJson(mapper));
        ObjectMapperUtils.returnToPool(mapper);
        exceptionResponse.responseEntity = responseEntity;
        exceptionResponse.ex = abstractException;
        return exceptionResponse;
    }

    private HTTPRequestLog logRequest(String reqId, Object requestBody, HttpServletRequest request){
        ServletServerHttpRequest springRequestWrapper = new ServletServerHttpRequest(request);
        HTTPRequestLog requestLog = new HTTPRequestLog(reqId, springRequestWrapper.getURI().toString(), "not implemented", springRequestWrapper.getMethod().toString());

        if (requestBody != null) {
			try {
				requestLog.logInfo(ObjectMapperUtils.parseObjectToJsonString(requestBody));
			} catch (Exception e) {
				requestLog.logWarn("unable to parse object to json : " + e.getMessage() + ". " + requestBody.toString());
			}
		} else {
			requestLog.logInfo("");
		}

        return requestLog;
    }

    protected void logResponse(HTTPRequestLog requestLog, ResponseEntity responseEntity){
        HTTPResponseLog responseLog = new HTTPResponseLog(requestLog, responseEntity.getStatusCode().toString());
        String responseBody = "";
        if(responseEntity.getBody() != null){
            try {
                responseBody = ObjectMapperUtils.parseObjectToJsonString(responseEntity.getBody());
            } catch (JsonProcessingException e) {
                responseBody = responseEntity.getBody().toString();
            }
        }
        responseLog.logInfo(responseBody);
    }

    protected class InitiatedData{
        ResponseEntity responseEntity;
        HTTPRequestLog requestLog;
    }
    protected InitiatedData initiateDataAndLogRequest(Object requestBody, HttpServletRequest request, HttpStatus httpStatus, String apiMethodEndpoint){
        InitiatedData initiatedData = new InitiatedData();
        if(HttpStatus.OK == httpStatus){
            initiatedData.responseEntity = ResponseEntity.ok().build();
        }else{
            initiatedData.responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInfo(request,new IllegalStateException(RESPONSE_ENTITY_INITIAL_VALUE)));
        }
        Logger.registerApiMethodEndpoint(apiMethodEndpoint);
        initiatedData.requestLog = logRequest(Logger.getReqIdFromContext(),requestBody, request);
        return initiatedData;
    }
}
