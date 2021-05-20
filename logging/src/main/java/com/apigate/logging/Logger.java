package com.apigate.logging;

import org.slf4j.MDC;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public interface Logger {
    String MDC_ENDPOINT="apiMethodEndpoint", MDC_REQ_ID="reqId";
    void logError(Exception e);
    void logInfo(String msg);
    void logDebug(String msg);
    void logTrace(String msg);
    void logWarn(String msg);

    static void registerApiMethodEndpoint(String apiMethodEndpoint) {
        MDC.put(MDC_ENDPOINT, apiMethodEndpoint);
    }

    static void registerReqId(String reqId) {
        MDC.put(MDC_REQ_ID, reqId);
    }

    static void removeDianosticContext() {
        MDC.clear();
    }

    static String getReqIdFromContext(){
        return MDC.get(MDC_REQ_ID);
    }

    static String getApiMethodEndpointFromContext(){
        return MDC.get(MDC_ENDPOINT);
    }
}
