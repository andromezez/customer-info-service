/*package com.apigate.utils.httpclient;

import com.apigate.config.Config;
import com.apigate.exceptions.internal.ExhaustedResourceException;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;*/

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
/*public class HttpClientPool {
    private static final int MAX_POOL_SIZE = Config.getApigateServerThreadsMax();
    private static final ObjectPool<CloseableHttpClient> HTTP_CLIENT_POOL = new GenericObjectPool<>(new HttpClientPoolFactory(),
            new HttpClientPoolConfig(getMaxPoolSize()),
            new AbandonedPoolConfig());

    private HttpClientPool(){}

    public static CloseableHttpClient getHttpClientInstance() throws ExhaustedResourceException {
        try {
            return HTTP_CLIENT_POOL.borrowObject();
        } catch (Exception e) {
            throw new ExhaustedResourceException(e);
        }
    }

    public static void returnToPool(CloseableHttpClient httpClient){
        try {
            if(httpClient != null){
                HTTP_CLIENT_POOL.returnObject(httpClient);
            }
        } catch (Exception e) {
            // ignored
        }
    }


    public static int getNumIdle(){
        return HTTP_CLIENT_POOL.getNumIdle();
    }

    public static int getNumActive(){
        return HTTP_CLIENT_POOL.getNumActive();
    }

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }
}*/
