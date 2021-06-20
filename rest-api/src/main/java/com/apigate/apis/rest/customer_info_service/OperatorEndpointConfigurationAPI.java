package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httprequestbody.operator.MnoEntryReqDto;
import com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint.MnoApiEndpointEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.MnoEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.OperatorResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.EndpointResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.OperatorEndpointService;
import com.apigate.exceptions.HTTPResponseBody.OperationResult;
import com.apigate.exceptions.business.InputValidationException;
import com.apigate.exceptions.internal.ExhaustedResourceException;
import com.apigate.logging.HTTPRequestLog;
import com.apigate.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:32 PM
 */
@RestController
@RequestMapping("/customer-info-service/v1/config/apis")
@Validated
public class OperatorEndpointConfigurationAPI extends AbstractController{

    @Autowired
    private OperatorEndpointService operatorEndpointService;



    @PutMapping("{apiId}")
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> updateSingle(@PathVariable("apiId") String apiId, @RequestBody(required = false) @Valid MnoApiEndpointEntryReqDto requestBody, BindingResult bindingResult, HttpServletRequest request){

        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(requestBody == null){
                throw new InputValidationException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new InputValidationException(bindingResult.getFieldError().getDefaultMessage());
            }

            MnoApiEndpointEntryDto mnoEntryDto = operatorEndpointService.update(apiId, requestBody);
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

}