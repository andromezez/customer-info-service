package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint.MnoApiEndpointEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.EndpointResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.OperatorEndpointService;
import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.apigate.exceptions.HTTPResponseBody.OperationResult;
import com.apigate.exceptions.internal.ErrorException;
import com.apigate.logging.HTTPRequestLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URL;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:32 PM
 */
@RestController
@RequestMapping("/customer-info-service/v2/config/endpoints")
@Validated
public class OperatorEndpointConfigurationAPI extends AbstractController{

    @Autowired
    private OperatorEndpointService operatorEndpointService;



    @PutMapping("{id}")
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> updateSingle(@PathVariable("id") String id, @RequestBody(required = false) @Valid MnoApiEndpointEntryReqDto requestBody, BindingResult bindingResult, HttpServletRequest request){

        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            URL url = new URL(requestBody.getUrl());

            if(StringUtils.isNotBlank(url.getQuery())){
                throw new ErrorException("Endpoint url can't have query parameters");
            }

            MnoApiEndpointEntryDto mnoEntryDto = operatorEndpointService.update(id, requestBody);
            var endpointResDto = new EndpointResDto();
            endpointResDto.setEndpoint(mnoEntryDto);
            endpointResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Endpoint Successfully Updated");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(endpointResDto);

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }finally {
            logResponse(requestLog, responseEntity);
        }
        return responseEntity;
    }

    @PostMapping("{id}/clear-cache")
    public ResponseEntity<Object> clearCache(@PathVariable("id") String id, HttpServletRequest request){

        InitiatedData initiatedData = initiateDataAndLogRequest("",request, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(StringUtils.isBlank(id)){
                throw new ErrorException("id can't be empty");
            }

            operatorEndpointService.clearCache(id);
            var genericResponseMessageDto = new GenericResponseMessageDto();
            genericResponseMessageDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Caches have been cleared successfully");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(genericResponseMessageDto);

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }finally {
            logResponse(requestLog, responseEntity);
        }
        return responseEntity;
    }

    @PostMapping
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> createSingle(@RequestHeader(name = "Operator-Id", required = false) String mnoId,
                                               @RequestBody(required = false) @Valid MnoApiEndpointEntryReqDto requestBody,
                                               BindingResult bindingResult,
                                               HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(StringUtils.isAnyBlank(mnoId)){
                throw new ErrorException("Header parameters Operator-Id must have value");
            }

            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            URL url = new URL(requestBody.getUrl());

            if(StringUtils.isNotBlank(url.getQuery())){
                throw new ErrorException("Endpoint url can't have query parameters");
            }

            var mnoEntryDto = operatorEndpointService.create(mnoId, requestBody);

            var endpointResDto = new EndpointResDto();
            endpointResDto.setEndpoint(mnoEntryDto);
            endpointResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Endpoint Entry Created");

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(endpointResDto);

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }finally {
            logResponse(requestLog, responseEntity);
        }
        return responseEntity;
    }

}
