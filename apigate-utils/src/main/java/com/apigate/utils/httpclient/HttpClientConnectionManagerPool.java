package com.apigate.utils.httpclient;

import com.apigate.config.Config;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * @author Bayu Utomo
 * @date 24/6/2021 6:28 PM
 */
public class HttpClientConnectionManagerPool {
    private static final PoolingHttpClientConnectionManager POOLING_CONN_MANAGER = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(Config.getApigateServerThreadsMax())
            .setMaxConnPerRoute(Config.getApigateServerThreadsMax())
            .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(Config.getApigateHttpClientTimeout(), TimeUnit.SECONDS).build())
            .setConnectionTimeToLive(TimeValue.ofMinutes(3))
            //.setValidateAfterInactivity(TimeValue.ofSeconds(5)) default 2 seconds
            .build();

    static PoolingHttpClientConnectionManager getPoolingConnManager() {
        return POOLING_CONN_MANAGER;
    }

    private HttpClientConnectionManagerPool(){}
}
