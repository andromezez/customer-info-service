package com.apigate.apis.rest;

import com.apigate.exceptions.business.EndpointDoesntExistException;
import com.apigate.exceptions.internal.SystemErrorException;
import com.apigate.logging.HTTPRequestLog;
import com.apigate.logging.HTTPResponseLog;
import com.apigate.logging.Logger;
import com.apigate.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    private HTTPRequestLog logRequest(String reqId, /*CreateRequestDto TODO enable this later*/ String requestBody, HttpServletRequest request){
        ServletServerHttpRequest springRequestWrapper = new ServletServerHttpRequest(request);
        HTTPRequestLog requestLog = new HTTPRequestLog(reqId, springRequestWrapper.getURI().toString(), "not implemented", springRequestWrapper.getMethod().toString());

        if (requestBody != null) {
            try {
                //requestLog.logInfo(ObjectMapperUtils.parseObjectToJsonString(requestBody)); TODO enable this later
                requestLog.logInfo(requestBody);
            } catch (Exception e) {
                requestLog.logWarn("unable to parse object to json : " + e.getMessage() + ". " + requestBody.toString());
            }
        } else {
            requestLog.logInfo("");
        }

        return requestLog;
    }

    private void logResponse(HTTPRequestLog requestLog, ResponseEntity responseEntity){
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

    @RequestMapping(value="**")
    public ResponseEntity getAnythingelse(HttpServletRequest request) {
        HttpServletRequest requestCacheWrapperObject
                = new ContentCachingRequestWrapper(request);
        requestCacheWrapperObject.getParameterMap();
        ServletServerHttpRequest springRequestWrapper = new ServletServerHttpRequest(requestCacheWrapperObject);

        String requestBody = "";
        InputStream streamBody = null;
        try {
            streamBody = springRequestWrapper.getBody();
            if(streamBody!=null){
                requestBody = IOUtils.toString(streamBody, StandardCharsets.UTF_8 );
            }
        } catch (IOException e) {
            throw new SystemErrorException(e);
        }

        HTTPRequestLog requestLog = logRequest(Logger.getReqIdFromContext(),requestBody, request);

        throw new SystemErrorException(new Exception("Intentionally throw exception to test GCP stackdriver output on printing exception's stack traces"));
    }
}
