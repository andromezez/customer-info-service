package com.apigate.utils.httpclient;

import com.apigate.config.Config;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Bayu Utomo
 * @date 23/6/2021 4:05 PM
 */
public class HttpClientUtils {

    private static final PoolingHttpClientConnectionManager POOLING_CONN_MANAGER;

    private static final ConnectionKeepAliveStrategy CONNECTION_KEEP_ALIVE_STRATEGY = (response, context) -> TimeValue.ofSeconds(Config.getApigateHttpClientTimeout());

    static {
        /*POOLING_CONN_MANAGER = new PoolingHttpClientConnectionManager();
        POOLING_CONN_MANAGER.setMaxTotal(Config.getApigateServerThreadsMax());
        POOLING_CONN_MANAGER.setDefaultMaxPerRoute(4);
        POOLING_CONN_MANAGER.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(Config.getApigateHttpClientTimeout(), TimeUnit.SECONDS).build());
        Use builder. It has more access to properties.
        */

        POOLING_CONN_MANAGER = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(Config.getApigateServerThreadsMax())
                .setMaxConnPerRoute(4)
                .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(Config.getApigateHttpClientTimeout(), TimeUnit.SECONDS).build())
                .setConnectionTimeToLive(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
                .build();
    }

    private HttpClientUtils(){}

    public static PoolingHttpClientConnectionManager getPoolingConnManager() {
        return POOLING_CONN_MANAGER;
    }

    public static HttpEntity executeGetRequest(String endpoint) throws IOException {
        HttpGet httpGet = new HttpGet(endpoint);
        HttpEntity response;

        POOLING_CONN_MANAGER.closeExpired();
        POOLING_CONN_MANAGER.closeIdle(TimeValue.ofSeconds(Config.getApigateHttpClientTimeout()* 5L));

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
                .setConnectionRequestTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
                .setResponseTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
                .setConnectionKeepAlive(TimeValue.ofSeconds(Config.getApigateHttpClientTimeout()))
                .build();

        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setKeepAliveStrategy(CONNECTION_KEEP_ALIVE_STRATEGY)
                .setConnectionManager(POOLING_CONN_MANAGER)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
                response = response1.getEntity();
                EntityUtils.consume(response);
            }
        }

        return response;
    }
}
