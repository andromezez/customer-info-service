package com.apigate.exceptions.business;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.OperationResultErrorLevel;
import com.apigate.exceptions.ResponseCodes;
import com.apigate.logging.Logger;
import com.apigate.logging.ServicesLog;
import org.springframework.http.HttpStatus;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class InputValidationException extends AbstractException implements OperationResultErrorLevel {

    private static final long serialVersionUID = 1L;
    private static final ResponseCodes.Errors error = ResponseCodes.Errors.CIS9998;

    private InputValidationException(){
        super(HttpStatus.BAD_REQUEST);
    }

    public InputValidationException(String message) {
        super(HttpStatus.BAD_REQUEST,error.toString() + " | " + message);
    }

    @Override
    public Logger getLogger() {
        return ServicesLog.getInstance();
    }

    @Override
    public void log() {
        this.getLogger().logError(this);
    }

    @Override
    public ResponseCodes.Errors getErrorResponseCode() {
        return error;
    }
}
