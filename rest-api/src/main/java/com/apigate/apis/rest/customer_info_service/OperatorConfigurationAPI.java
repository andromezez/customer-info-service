package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httpresponsebody.operator.MnoEntryDto;
import com.apigate.customer_info_service.dto.httprequestbody.operator.MnoEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.ListOfOperatorsResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.OperatorResDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.OperatorService;
import com.apigate.exceptions.HTTPResponseBody.OperationResult;
import com.apigate.exceptions.business.InputValidationException;
import com.apigate.logging.HTTPRequestLog;
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
 * @date 10/6/2021 9:18 AM
 */
@RestController
@RequestMapping("/customer-info-service/v1/config/operators")
@Validated
public class OperatorConfigurationAPI extends AbstractController{

    @Autowired
    private OperatorService operatorService;

    @GetMapping
    public ResponseEntity<Object> retrieveList(@RequestParam(name = "name", required = false) String name, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest("",request,HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        ListOfOperatorsResDto responseDto = new ListOfOperatorsResDto();
        responseDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS,"","Data successfully retrieved");

        try{
            if(name == null || name.isBlank()){
                responseDto.getOperators().addAll(operatorService.getOperatorsDetails());
            }else{
                responseDto.getOperators().addAll(operatorService.getOperatorsDetails(name));
            }
            responseEntity = ResponseEntity.ok(responseDto);
        }catch (Exception e){
            ExceptionResponse exceptionResponse = processException(request, e);
            responseEntity = exceptionResponse.responseEntity;
            throw exceptionResponse.ex;
        }finally {
            logResponse(requestLog, responseEntity);
        }
        return responseEntity;
    }

    @PutMapping("{id}")
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> updateSingle(@PathVariable("id") String id, @RequestBody(required = false) @Valid MnoEntryReqDto requestBody, BindingResult bindingResult, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request,HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(requestBody == null){
                throw new InputValidationException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new InputValidationException(bindingResult.getFieldError().getDefaultMessage());
            }

            MnoEntryDto mnoEntryDto = operatorService.update(id, requestBody);
            OperatorResDto operatorResDto = new OperatorResDto();
            operatorResDto.setOperator(mnoEntryDto);
            operatorResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Operator Successfully Updated");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(operatorResDto);

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