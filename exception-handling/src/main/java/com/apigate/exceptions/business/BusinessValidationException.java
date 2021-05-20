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
public class BusinessValidationException extends AbstractException implements OperationResultErrorLevel {

    private static final long serialVersionUID = 1L;

    private static final ResponseCodes.Errors error = ResponseCodes.Errors.CIS9996;

    private BusinessValidationException(){
        super(HttpStatus.BAD_REQUEST);
    }

    public BusinessValidationException(String reason) {
        super(HttpStatus.BAD_REQUEST,error.toString() + " | " + reason);
    }

    @Override
    public Logger getLogger() {
        return ServicesLog.getInstance();
    }

    @Override
    public void log() {
        this.getLogger().logWarn(this.getMessage());
    }

    @Override
    public ResponseCodes.Errors getErrorResponseCode() {
        return error;
    }
}
