package com.apigate.exceptions;

import com.apigate.exceptions.HTTPResponseBody.ErrorInfo;
import com.apigate.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public interface ExceptionLogger {
    Logger getLogger();

    void log();

    ErrorInfo getErrorInfo(HttpServletRequest request);
}
