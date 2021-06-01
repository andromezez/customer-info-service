package com.apigate.logging;

import org.slf4j.LoggerFactory;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class CommonLog extends AbstractLogger{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CommonLog.class);
    private static final com.apigate.logging.Logger INSTANCE = new CommonLog();

    private CommonLog(){}

    public static com.apigate.logging.Logger getInstance() {
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
