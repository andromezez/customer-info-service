package com.apigate.exceptions.handler;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.HTTPResponseBody.ErrorInfo;
import com.apigate.exceptions.business.EndpointDoesntExistException;
import com.apigate.exceptions.business.HeaderValidationException;
import com.apigate.exceptions.business.InputValidationException;
import com.apigate.exceptions.internal.SystemErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, AbstractException ex) {
        ex.log();
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, MethodArgumentNotValidException e) {
        InputValidationException ex = new InputValidationException(e.getBindingResult().getFieldError().getDefaultMessage());
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, ConstraintViolationException e) {
        InputValidationException ex = new InputValidationException(e.getConstraintViolations().stream().findFirst().get().getMessage());
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, MissingServletRequestParameterException e) {
        InputValidationException ex = new InputValidationException(e.getMessage());
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        EndpointDoesntExistException ex = new EndpointDoesntExistException();
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, HttpMediaTypeException e) {
        HeaderValidationException ex = new HeaderValidationException(e.getMessage());
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorInfo> handle(HttpServletRequest request, Exception e) {
        SystemErrorException ex = new SystemErrorException(e);
        ex.log();
        ResponseEntity<ErrorInfo> response = ResponseEntity.status(ex.getStatus()).body(ex.getErrorInfo(request));
        return response;
    }

}
