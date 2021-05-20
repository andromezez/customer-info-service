package com.apigate.config;

/**
 * @author Bayu Utomo
 * @date 9/12/2020 10:29 AM
 */
public enum CommonHeaders {
    CORRELATION_ID("X-Apigate-CorrId");

    private String headerName;

    CommonHeaders(String headerName){
        this.headerName=headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
