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
public class HeaderValidationException extends AbstractException implements OperationResultErrorLevel {
    private static final long serialVersionUID = 1L;
    private static final ResponseCodes.Errors error = ResponseCodes.Errors.CIS9992;

    private HeaderValidationException(){
        super(HttpStatus.BAD_REQUEST);
    }

    public HeaderValidationException(String message) {
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
