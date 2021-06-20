package com.apigate.apis.rest.util;

import org.apache.commons.io.IOUtils;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Bayu Utomo
 * @date 10/6/2021 11:04 AM
 */
public class HTTPUtils {

    public static String extractRequestBody(HttpServletRequest request) throws IOException {
        HttpServletRequest requestCacheWrapperObject
                = new ContentCachingRequestWrapper(request);
        requestCacheWrapperObject.getParameterMap();
        ServletServerHttpRequest springRequestWrapper = new ServletServerHttpRequest(requestCacheWrapperObject);

        String requestBody = "Request body is empty.";
        InputStream streamBody = null;

        streamBody = springRequestWrapper.getBody();
        if(streamBody!=null){
            requestBody = IOUtils.toString(streamBody, StandardCharsets.UTF_8 );
        }

        return requestBody;
    }
}
