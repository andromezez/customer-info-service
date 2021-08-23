package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httprequestbody.routing.UpdateRoutingEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.ListOfRoutingResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.RoutingEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.RoutingResDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.RoutingService;
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
 * @date 14/6/2021 10:50 AM
 */
@RestController
@RequestMapping("/customer-info-service/v2/config/routing")
@Validated
public class RoutingConfigurationAPI extends AbstractController{

    @Autowired
    private RoutingService routingService;

    @GetMapping
    public ResponseEntity<Object> retrieveList(@RequestHeader(name = "Client-Id", required = false) String clientId,
                                                  @RequestHeader(name = "Operator-Id", required = false) String operatorId,
                                                  HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest("",request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            List<RoutingEntryDto> routingEntryDtoList;
            if(StringUtils.isNoneBlank(clientId,operatorId)){
                routingEntryDtoList  = routingService.getRoutingBy(clientId, operatorId);
            }else if(StringUtils.isNotBlank(clientId)){
                routingEntryDtoList = routingService.getRoutingByClientId(clientId);
            }else if(StringUtils.isNotBlank(operatorId)){
                routingEntryDtoList = routingService.getRoutingByOperatorId(operatorId);
            }else{
                routingEntryDtoList = routingService.getAllRouting();
            }

            ListOfRoutingResDto listOfRoutingResDto = new ListOfRoutingResDto();
            listOfRoutingResDto.getRoutingConfigs().addAll(routingEntryDtoList);
            listOfRoutingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Data successfully retrieved");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(listOfRoutingResDto);

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
                                                @RequestBody(required = false) @Valid UpdateRoutingEntryReqDto requestBody,
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

            var routingEntryDto = routingService.update(clientId,endpointId,requestBody);

            RoutingResDto routingResDto = new RoutingResDto();
            routingResDto.setRoutingConfig(routingEntryDto);
            routingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Routing Successfully Updated");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(routingResDto);
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
                                                   @RequestBody(required = false) @Valid UpdateRoutingEntryReqDto requestBody,
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

            var routingEntryDto = routingService.create(clientId,endpointId,requestBody);

            RoutingResDto routingResDto = new RoutingResDto();
            routingResDto.setRoutingConfig(routingEntryDto);
            routingResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Routing Entry Created");

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(routingResDto);
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
