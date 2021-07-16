package com.apigate.logging;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Data
public class HTTPRequestLog  extends AbstractLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestLog.class);

    //private RequestEntity requestEntity;
    private String requestId, urlEndpoint, requestBody, username, http_method;

    private HTTPRequestLog() {
    }

    public HTTPRequestLog(String requestId, String urlEndpoint, String username, String http_method) {
        this.requestId = requestId;
        this.urlEndpoint = urlEndpoint;
        this.username = username;
        this.http_method = http_method;
    }

    private StringBuilder buildLogMsg(){
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(" [REQUEST] ");
        logMsg.append(" [HTTP_METHOD: ").append(http_method).append("] ");
        logMsg.append(" [REQUEST_ID: ").append(requestId).append("] ");
        logMsg.append(" [USERNAME: ").append(username).append("] ");
        logMsg.append(" [URL: ").append(urlEndpoint).append("] ");
        logMsg.append(" [BODY: ").append(requestBody).append("] ");
        return logMsg;
    }


    @Override
    public void logError(Exception e) {
        LOGGER.error(parseError(e));
    }

    @Override
    public void logInfo(String requestBody) {
        this.requestBody = requestBody;
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
