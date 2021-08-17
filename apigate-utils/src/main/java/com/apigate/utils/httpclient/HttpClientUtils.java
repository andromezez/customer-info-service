package com.apigate.utils.httpclient;

import com.apigate.logging.HttpClientLog;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.TextStringBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
            if ((code > 0) /*&& StringUtils.isNotBlank(body)*/ && (headers != null)) { //response from request method HEAD won't have body in the response
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

        public HttpHeaders getHeadersForSpring(){
            HttpHeaders httpHeaders = new HttpHeaders();
            for(var header : headers){
                httpHeaders.add(header.getName(),header.getValue());
            }
            return httpHeaders;
        }
    }

    private HttpClientUtils(){}

    private static HttpResponse executeRequest(ClassicHttpRequest httpRequest) throws IOException, URISyntaxException {
        HttpResponse result = new HttpResponse();

        URI uri = httpRequest.getUri();

        var headerStr = new StringBuilder();
        headerStr.append('{');
        for(var header : httpRequest.getHeaders()){
            headerStr.append('[').append(header.getName()).append('=').append(header.getValue()).append(']');
        }
        headerStr.append('}');

        var requestLog = new StringBuilder();
        requestLog.append("Sending http ").append( httpRequest.getMethod()).append(" request to ") .append(uri.toString()).append(" with headers ").append(headerStr);

        if(httpRequest.getEntity() != null){
            var requestBody = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8.name());
            requestLog.append(" and request body ").append(requestBody);
        }

        HttpClientLog.getInstance().logSentRequest(requestLog.toString() );
        try (CloseableHttpResponse httpResponse = HttpClientSingleton.getInstance().execute(httpRequest)) {

            HttpEntity entity = httpResponse.getEntity();
            result.setBody(IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8.name()));

            result.setCode(httpResponse.getCode());
            result.setReasonPhrase(httpResponse.getReasonPhrase());
            result.setHeaders(httpResponse.getHeaders());

            HttpClientLog.getInstance().logReceivedResponse("Receive http response " + httpResponse.getCode() + " " + httpResponse.getReasonPhrase() + " with response body " + result.getBody());
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        }

        return result;
    }

    public static HttpResponse executeRequest(HttpPost httpPost) throws IOException, URISyntaxException {
        return executeRequest((ClassicHttpRequest) httpPost);
    }

    public static HttpResponse executeRequest(HttpGet httpGet) throws IOException, URISyntaxException {
        return executeRequest((ClassicHttpRequest) httpGet);
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
