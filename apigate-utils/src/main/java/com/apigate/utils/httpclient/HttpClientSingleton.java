package com.apigate.utils.httpclient;

import com.apigate.config.Config;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

/**
 * @author Bayu Utomo
 * @date 24/6/2021 11:22 PM
 */
public class HttpClientSingleton {
    private static final ConnectionKeepAliveStrategy CONNECTION_KEEP_ALIVE_STRATEGY = (response, context) -> TimeValue.ofSeconds(Config.getApigateHttpClientTimeout());
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            .setConnectionRequestTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            .setResponseTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            //.setConnectionKeepAlive(TimeValue.ofSeconds(Config.getApigateHttpClientTimeout())) Default: 3 minutes
            .build();
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.custom()
            .setKeepAliveStrategy(CONNECTION_KEEP_ALIVE_STRATEGY)
            .setConnectionManager(HttpClientConnectionManagerPool.getPoolingConnManager())
            //.setConnectionManagerShared(true)
            .setDefaultRequestConfig(REQUEST_CONFIG)
            .evictExpiredConnections()
            .evictIdleConnections(TimeValue.ofMinutes(3))
            .build();

    private HttpClientSingleton(){}

    static CloseableHttpClient getInstance(){
        return HTTP_CLIENT;
    }
}
