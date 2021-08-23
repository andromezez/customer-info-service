package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httprequestbody.masking.UpdateMaskingEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.masking.ListOfMaskingResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.masking.MaskingEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.masking.MaskingResDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.MaskingService;
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
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 4:31 PM
 */
@RestController
@RequestMapping("/customer-info-service/v2/config/masks")
@Validated
public class MaskingConfigurationAPI extends AbstractController{

    @Autowired
    MaskingService maskingService;

    @GetMapping
    public ResponseEntity<Object> retrieveList(@RequestHeader(name = "Client-Id", required = false) String clientId,
                                               @RequestHeader(name = "Endpoint-Id", required = false) String endpointId,
                                               HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest("",request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            List<MaskingEntryDto> maskingEntryDtoList;
            if(StringUtils.isNoneBlank(clientId,endpointId)){
                maskingEntryDtoList  = maskingService.getMaskingBy(clientId, endpointId);
            }else if(StringUtils.isNotBlank(clientId)){
                maskingEntryDtoList = maskingService.getMaskingByClientId(clientId);
            }else if(StringUtils.isNotBlank(endpointId)){
                maskingEntryDtoList = maskingService.getMaskingByEndpointId(endpointId);
            }else{
                maskingEntryDtoList = maskingService.getAllMasking();
            }

            ListOfMaskingResDto listOfMaskingResDto = new ListOfMaskingResDto();
            listOfMaskingResDto.getMasks().addAll(maskingEntryDtoList);
            listOfMaskingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Data successfully retrieved");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(listOfMaskingResDto);

        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }finally {
            logResponse(requestLog, responseEntity);
        }
        return responseEntity;
    }

    @PutMapping
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> updateSingle(@RequestHeader(name = "Client-Id", required = false) String clientId,
                                               @RequestHeader(name = "Endpoint-Id", required = false) String endpointId,
                                               @RequestHeader(name = "Json-Path", required = false) String jsonPath,
                                               @RequestBody(required = false) @Valid UpdateMaskingEntryReqDto requestBody,
                                               BindingResult bindingResult, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request,HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(StringUtils.isAnyBlank(clientId, endpointId,jsonPath)){
                throw new ErrorException("Header parameters Client-Id and Endpoint-Id and Json-Path must have value");
            }

            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            var maskingEntryDto = maskingService.update(clientId,endpointId,jsonPath,requestBody);

            MaskingResDto maskingResDto = new MaskingResDto();
            maskingResDto.setMask(maskingEntryDto);
            maskingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Masking Successfully Updated");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(maskingResDto);

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
    public ResponseEntity<Object> createSingle(@RequestHeader(name = "Client-Id", required = false) String clientId,
                                               @RequestHeader(name = "Endpoint-Id", required = false) String endpointId,
                                               @RequestBody(required = false) @Valid UpdateMaskingEntryReqDto requestBody,
                                               BindingResult bindingResult, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request,HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(StringUtils.isAnyBlank(clientId, endpointId)){
                throw new ErrorException("Header parameters Client-Id and Endpoint-Id must have value");
            }

            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            var maskingEntryDto = maskingService.create(clientId, endpointId, requestBody);

            MaskingResDto maskingResDto = new MaskingResDto();
            maskingResDto.setMask(maskingEntryDto);
            maskingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Masking Entry Created");

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(maskingResDto);
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
