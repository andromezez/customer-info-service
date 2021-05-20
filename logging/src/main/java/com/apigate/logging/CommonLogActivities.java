package com.apigate.logging;

import com.apigate.config.CommonHeaders;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Component
@WebFilter("/*")
public class CommonLogActivities extends HttpFilter {
    private static final org.slf4j.Logger httpRequestLogger = LoggerFactory.getLogger(com.apigate.logging.HTTPRequestLog.class);

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Instant start = Instant.now();
        String reqId = RequestIDGenerator.generateId();
        com.apigate.logging.Logger.registerReqId(reqId);
        com.apigate.logging.ServicesLog.getInstance().logInfo("START process the request [METHOD: " + request.getMethod() + "] [ENDPOINT: " + request.getRequestURL().toString() + "]");
        try {
            String clientCorrelationId = request.getHeader(CommonHeaders.CORRELATION_ID.getHeaderName());
            if(clientCorrelationId != null){
                com.apigate.logging.ServicesLog.getInstance().logInfo("clientCorrelationId " + CommonHeaders.CORRELATION_ID.getHeaderName() + " : " + clientCorrelationId);
                response.addHeader(CommonHeaders.CORRELATION_ID.getHeaderName(),clientCorrelationId);
            }
            if(httpRequestLogger.isDebugEnabled()){
                request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                    request.getHeaders(headerName).asIterator().forEachRemaining(value -> {
                        httpRequestLogger.debug("Request Header '"+headerName+"' = " + value);
                    });
                });
            }

            super.doFilter(request, response, chain);
        }finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            StringBuilder str = new StringBuilder();
            str.append("END");
            str.append(" [METHOD: " + request.getMethod() +"]");
            str.append(" [ENDPOINT: " + request.getRequestURL().toString() + "]");

            String durationStr = DurationFormatUtils.formatDurationHMS(duration.toMillis());
            //String[] timeData = durationStr.split(":");
            str.append(" [TIME_TAKEN: ");
            str.append(durationStr);
            /*str.append(timeData[0]+" hours ");
            str.append(timeData[1]+" minutes ");
            str.append(timeData[2]+" seconds ");*/
            str.append("]");
            com.apigate.logging.ServicesLog.getInstance().logInfo(str.toString());
            com.apigate.logging.Logger.removeDianosticContext();
        }

    }
}
