package com.apigate.apis.rest.customer_info_service;

import com.apigate.customer_info_service.dto.httprequestbody.client.CreateClientEntryReqDto;
import com.apigate.customer_info_service.dto.httprequestbody.client.UpdateClientEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.client.ClientResDto;
import com.apigate.customer_info_service.dto.httpresponsebody.client.ListOfClientsResDto;
import com.apigate.customer_info_service.dto.validator.ValidationSequence;
import com.apigate.customer_info_service.service.ClientService;
import com.apigate.exceptions.HTTPResponseBody.OperationResult;
import com.apigate.exceptions.internal.ErrorException;
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
 * @date 13/6/2021 2:17 PM
 */
@RestController
@RequestMapping("/customer-info-service/v2/config/clients")
@Validated
public class ClientConfigurationAPI extends AbstractController{

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Object> retrieveList(@RequestHeader(name = "Partner-Id", required = false) String partnerId, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest("",request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        ListOfClientsResDto responseDto = new ListOfClientsResDto();
        responseDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS,"","Data successfully retrieved");

        try{
            if(partnerId == null || partnerId.isBlank()){
                responseDto.getClients().addAll(clientService.getClientsDetails());
            }else{
                responseDto.getClients().addAll(clientService.getClientsDetails(partnerId));
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

    @PostMapping
    @Validated(ValidationSequence.class)
    public ResponseEntity<Object> createSingle(@RequestBody(required = false) @Valid CreateClientEntryReqDto requestBody, BindingResult bindingResult, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request, HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try{
            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            var clientEntryDto = clientService.create(requestBody);
            ClientResDto clientResDto = new ClientResDto();
            clientResDto.setClient(clientEntryDto);
            clientResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Client Entry Created");

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(clientResDto);
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
    public ResponseEntity<Object> updateSingle(@PathVariable("id") String id, @RequestBody(required = false) @Valid UpdateClientEntryReqDto requestBody, BindingResult bindingResult, HttpServletRequest request){
        InitiatedData initiatedData = initiateDataAndLogRequest(requestBody,request,HttpStatus.INTERNAL_SERVER_ERROR, Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEntity responseEntity = initiatedData.responseEntity;
        HTTPRequestLog requestLog = initiatedData.requestLog;

        try {
            if(requestBody == null){
                throw new ErrorException("Request Body can't be empty");
            } else if(bindingResult.hasErrors()){
                throw new ErrorException(bindingResult.getFieldError().getDefaultMessage());
            }

            var clientEntryDto = clientService.update(id, requestBody);

            ClientResDto clientResDto = new ClientResDto();
            clientResDto.setClient(clientEntryDto);
            clientResDto.getOperationResult().setOperationResult(request, OperationResult.Status.SUCCESS, "", "Client Successfully Updated");

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(clientResDto);
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
