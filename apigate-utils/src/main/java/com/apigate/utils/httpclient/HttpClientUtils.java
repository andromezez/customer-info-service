package com.apigate.utils.httpclient;

import com.apigate.logging.ServicesLog;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Bayu Utomo
 * @date 23/6/2021 4:05 PM
 */
public class HttpClientUtils {

    @Data
    public static class HttpResponse {
        private int code = 0;
        private String body, reasonPhrase;
        private Header[] headers;

        public boolean isResponseComplete() {
            if ((code > 0) && StringUtils.isNotBlank(body) && (headers != null)) {
                return true;
            } else {
                return false;
            }
        }

        public static String toString(Header[] headers){
            TextStringBuilder result = new TextStringBuilder();
            return result.append("[ ")
                    .appendWithSeparators(headers," , ")
                    .append(" ]")
                    .toString();
        }
    }

    private HttpClientUtils(){}

    public static HttpResponse executeGetRequest(String endpoint) throws IOException {
        HttpGet httpGet = new HttpGet(endpoint);
        HttpResponse result = new HttpResponse();

        //HttpClientConnectionManagerPool.getPoolingConnManager().closeExpired();
        //HttpClientConnectionManagerPool.getPoolingConnManager().closeIdle(TimeValue.ofMinutes(3));

        //var httpClient = HttpClientPool.getHttpClientInstance();

        ServicesLog.getInstance().logInfo("Sending http request to " + endpoint);
        try (CloseableHttpResponse httpResponse = HttpClientSingleton.getInstance().execute(httpGet)) {
            ServicesLog.getInstance().logInfo("Get http response " + httpResponse.getCode() + " " + httpResponse.getReasonPhrase());

            HttpEntity entity = httpResponse.getEntity();
            result.setBody(IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8.name()));

            result.setCode(httpResponse.getCode());
            result.setReasonPhrase(httpResponse.getReasonPhrase());
            result.setHeaders(httpResponse.getHeaders());

            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        }

        //HttpClientPool.returnToPool(httpClient);

        return result;
    }

    public static String subtitutePath(URL originalUrl, HttpServletRequest replaceWith){
        StringBuilder result = new StringBuilder();
        return result.append(originalUrl.getProtocol())
                .append("://")
                .append(originalUrl.getHost())
                .append(":")
                .append(originalUrl.getPort())
                .append(replaceWith.getRequestURI())
                .toString();
    }
}
