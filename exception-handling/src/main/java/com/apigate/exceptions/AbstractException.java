package com.apigate.exceptions;

import com.apigate.exceptions.HTTPResponseBody.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public abstract class AbstractException extends ResponseStatusException implements com.apigate.exceptions.ExceptionLogger {

    public AbstractException(HttpStatus status) {
        super(status);
    }

    public AbstractException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public AbstractException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    @Override
    public ErrorInfo getErrorInfo(HttpServletRequest request) {
        return new ErrorInfo(request, this);
    }

}
