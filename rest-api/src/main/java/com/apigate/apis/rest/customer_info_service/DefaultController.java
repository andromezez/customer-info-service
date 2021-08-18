package com.apigate.apis.rest.customer_info_service;

import com.apigate.apis.rest.util.HTTPUtils;
import com.apigate.customer_info_service.service.MaskingService;
import com.apigate.customer_info_service.service.OperatorEndpointService;
import com.apigate.customer_info_service.service.OperatorService;
import com.apigate.customer_info_service.service.RoutingService;
import com.apigate.exceptions.internal.ErrorException;
import com.apigate.logging.ServicesLog;
import com.apigate.utils.httpclient.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

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
    private OperatorEndpointService operatorEndpointService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private MaskingService maskingService;

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

                    String incomingRequestMsisdn = operatorEndpointService.getMsisdn(routing.get().getMnoApiEndpoint(), request);

                    try{
                        if(routing.get().getClient().isCacheActive() && routing.get().isCacheActive()){
                            ServicesLog.getInstance().logInfo("Cache is on");
                            cacheResponse = routingService.getRoutingResponseCache(routing.get(), incomingRequestMsisdn);
                        }else{
                            ServicesLog.getInstance().logInfo("Cache is off");
                        }
                    }catch (Exception e){
                        ServicesLog.getInstance().logError(e);
                    }

                    if(StringUtils.isNotBlank(cacheResponse)){
                        ServicesLog.getInstance().logInfo("Cache is found");
                        String responseBodyAfterMasking = maskingService.maskTheResponse(routing.get(), cacheResponse);
                        responseEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBodyAfterMasking);
                    }else{
                        ServicesLog.getInstance().logInfo("Cache not found. Cache is " + cacheResponse);
                        String endpoint = HttpClientUtils.subtitutePath(new URL(routing.get().getMnoApiEndpoint().getUrl()), request);
                        HttpGet httpGet = new HttpGet(endpoint);
                        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + operatorService.getAccessToken(routing.get().getMnoApiEndpoint().getMnoId()));
                        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                        httpGet.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
                        var httpResponse = HttpClientUtils.executeRequest(httpGet);
                        if(httpResponse.isResponseComplete()){
                            String responseBody = httpResponse.getBody();
                            try{
                                if (routing.get().getClient().isCacheActive() && routing.get().isCacheActive() && (httpResponse.getCode() == HttpStatus.OK.value())) {
                                    routingService.createRoutingResponseCache(routing.get(),incomingRequestMsisdn,responseBody);
                                    responseBody = maskingService.maskTheResponse(routing.get(), responseBody);
                                }
                            }catch (Exception e){
                                ServicesLog.getInstance().logError(e);
                            }
                            responseEntity = ResponseEntity.status(HttpStatus.valueOf(httpResponse.getCode())).contentType(MediaType.APPLICATION_JSON).body(responseBody);
                        }else{
                            String errorMessage = "Operator endpoint " + routing.get().getMnoApiEndpoint().getUrl()
                                    + " doesn't return proper response.";
                            String errorDetails = errorMessage
                                    + "\n    response code : " + httpResponse.getCode()
                                    + "\n    reason phrase : " + httpResponse.getReasonPhrase()
                                    + "\n    response body : " + httpResponse.getBody()
                                    + "\n    response headers : " + HttpClientUtils.HttpResponse.toString(httpResponse.getHeaders());
                            var errorForLog = new ErrorException(errorDetails);
                            ServicesLog.getInstance().logError(errorForLog);

                            var errorForClient = new ErrorException(errorMessage);
                            throw errorForClient;
                        }
                    }
                }else{
                    throw new ErrorException("Can't find routing for partnerId " + partnerId
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

            throw new ErrorException(new Exception("Intentionally throw exception to test GCP stackdriver output on printing exception's stack traces"));

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }
        finally {
            logResponse(initiatedData.requestLog, responseEntity);
        }
    }
}
