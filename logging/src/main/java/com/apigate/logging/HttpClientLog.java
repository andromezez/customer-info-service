package com.apigate.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bayu Utomo
 * @date 17/8/2021 07:30 PM
 */
public class HttpClientLog extends AbstractLogger{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientLog.class);
    private static final HttpClientLog INSTANCE = new HttpClientLog();

    private HttpClientLog(){}

    public static HttpClientLog getInstance() {
        return INSTANCE;
    }

    @Override
    public void logError(Exception e) {
        LOGGER.error(parseError(e));
    }
    @Override
    public void logInfo(String msg){
        LOGGER.info(msg);
    }

    public void logSentRequest(String msg){
        var logStr = new StringBuilder("[SENT] ");
        logStr.append(msg);
        logInfo(logStr.toString());
    }

    public void logReceivedResponse(String msg){
        var logStr = new StringBuilder("[RECEIVED] ");
        logStr.append(msg);
        logInfo(logStr.toString());
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
