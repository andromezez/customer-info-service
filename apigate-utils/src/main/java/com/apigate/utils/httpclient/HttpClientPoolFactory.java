/*package com.apigate.utils.httpclient;

import com.apigate.config.Config;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;*/

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
/*public class HttpClientPoolFactory extends BasePooledObjectFactory<CloseableHttpClient> {

    private static final ConnectionKeepAliveStrategy CONNECTION_KEEP_ALIVE_STRATEGY = (response, context) -> TimeValue.ofSeconds(Config.getApigateHttpClientTimeout());
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            .setConnectionRequestTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            .setResponseTimeout(Timeout.ofSeconds(Config.getApigateHttpClientTimeout()))
            .setConnectionKeepAlive(TimeValue.ofSeconds(Config.getApigateHttpClientTimeout()))
            .build();

    @Override
    public CloseableHttpClient create() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setKeepAliveStrategy(CONNECTION_KEEP_ALIVE_STRATEGY)
                .setConnectionManager(HttpClientConnectionManagerPool.getPoolingConnManager())
                .setConnectionManagerShared(true)
                .setDefaultRequestConfig(REQUEST_CONFIG)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(Config.getApigateHttpClientTimeout() * 5L))
                .build();

        return httpClient;
    }

    @Override
    public PooledObject<CloseableHttpClient> wrap(CloseableHttpClient obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<CloseableHttpClient> p) throws Exception {
        p.getObject().close();
        super.destroyObject(p);
    }
}*/
