package com.apigate.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class HTTPResponseLog extends AbstractLogger{
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPResponseLog.class);
    private HTTPRequestLog requestLog;
    private String responseBody, httpStatus;

    private HTTPResponseLog() {
    }

    public HTTPResponseLog(HTTPRequestLog requestLog, String httpStatus) {
        this.requestLog = requestLog;
        this.httpStatus = httpStatus;
    }

    private StringBuilder buildLogMsg(){
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(" [RESPONSE] ");
        logMsg.append(" [REQUEST_ID: ").append(requestLog.getRequestId()).append("] ");
        logMsg.append(" [USERNAME: ").append(requestLog.getUsername()).append("] ");
        logMsg.append(" [HTTP_STATUS: ").append(httpStatus).append("] ");
        logMsg.append(" [BODY: ").append(responseBody).append("] ");
        return logMsg;
    }

    public void logInfo(String httpStatus, String responseBody) {
        this.httpStatus = httpStatus;
        this.responseBody = responseBody;
        LOGGER.info(buildLogMsg().toString());
    }


    @Override
    public void logError(Exception e) {
        LOGGER.error(parseError(e));
    }

    @Override
    public void logInfo(String responseBody) {
        this.responseBody = responseBody;
        LOGGER.info(buildLogMsg().toString());
    }

    @Override
    public void logDebug(String msg) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(msg);
        }
    }

    @Override
    public void logTrace(String msg) {
        LOGGER.trace(msg);
    }

    @Override
    public void logWarn(String msg) {
        LOGGER.warn(msg);
    }
}
