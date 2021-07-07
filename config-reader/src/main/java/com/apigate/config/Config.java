package com.apigate.config;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class Config {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss Z";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
    private static String INSTANCE_ID;
    private static int APIGATE_SERVER_THREADS_MAX=100;
    private static int SERVER_MONITORING_PORT = 0;
    private static int SERVER_MONITORING_MAX_THREAD = 0;
    private static int APIGATE_HTTP_CLIENT_TIMEOUT = 5; //in seconds

    private static Duration TTL_LEFT_BEFORE_REFRESH_TOKEN = Duration.ofSeconds(10);
    private static Duration REFRESH_TOKEN_SCHEDULER_PERIOD = Duration.ofSeconds(10);

    /**
     * Gets apigate http client timeout.
     *
     * @return the apigate http client timeout. In seconds
     */
    public static int getApigateHttpClientTimeout() {
        return APIGATE_HTTP_CLIENT_TIMEOUT;
    }

    /**
     * Sets apigate http client timeout.
     *
     * @param apigateHttpClientTimeout the apigate http client timeout. In seconds
     */
    public static void setApigateHttpClientTimeout(int apigateHttpClientTimeout) {
        APIGATE_HTTP_CLIENT_TIMEOUT = apigateHttpClientTimeout;
    }

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

    public static Duration getTtlLeftBeforeRefreshToken() {
        return TTL_LEFT_BEFORE_REFRESH_TOKEN;
    }

    public static void setTtlLeftBeforeRefreshToken(Duration ttlLeftBeforeRefreshToken) {
        TTL_LEFT_BEFORE_REFRESH_TOKEN = ttlLeftBeforeRefreshToken;
    }

    public static Duration getRefreshTokenSchedulerPeriod() {
        return REFRESH_TOKEN_SCHEDULER_PERIOD;
    }

    public static void setRefreshTokenSchedulerPeriod(Duration refreshTokenSchedulerPeriod) {
        REFRESH_TOKEN_SCHEDULER_PERIOD = refreshTokenSchedulerPeriod;
    }
}
