package com.apigate.config;

import java.time.format.DateTimeFormatter;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class Config {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss Z";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
    private static String INSTANCE_ID;
    private static int APIGATE_SERVER_THREADS_MAX;
    private static int SERVER_MONITORING_PORT = 0;
    private static int SERVER_MONITORING_MAX_THREAD = 0;

    public static void setInstanceId(String instanceId) {
        INSTANCE_ID = instanceId;
    }

    public static int getApigateServerThreadsMax() {
        return APIGATE_SERVER_THREADS_MAX;
    }

    public static String getInstanceId() {
        return INSTANCE_ID;
    }

    public static void setApigateServerThreadsMax(int apigateServerThreadsMax) {
        APIGATE_SERVER_THREADS_MAX = apigateServerThreadsMax;
    }

    private Config(){}

    public static int getServerMonitoringPort() {
        return SERVER_MONITORING_PORT;
    }

    public static void setServerMonitoringPort(int serverMonitoringPort) {
        SERVER_MONITORING_PORT = serverMonitoringPort;
    }

    public static int getServerMonitoringMaxThread() {
        return SERVER_MONITORING_MAX_THREAD;
    }

    public static void setServerMonitoringMaxThread(int serverMonitoringMaxThread) {
        SERVER_MONITORING_MAX_THREAD = serverMonitoringMaxThread;
    }
}