package com.apigate.exceptions.business;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.OperationResultErrorLevel;
import com.apigate.exceptions.ResponseCodes;
import com.apigate.logging.CommonLog;
import com.apigate.logging.Logger;
import org.springframework.http.HttpStatus;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class EndpointDoesntExistException extends AbstractException implements OperationResultErrorLevel {
    private static final long serialVersionUID = 1L;
    private static final ResponseCodes.Errors error = ResponseCodes.Errors.CIS9991;

    public EndpointDoesntExistException(){
        super(HttpStatus.NOT_FOUND, error.toString());
    }

    @Override
    public Logger getLogger() {
        return CommonLog.getInstance();
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
