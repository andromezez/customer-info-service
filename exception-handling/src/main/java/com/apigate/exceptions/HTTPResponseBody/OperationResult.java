package com.apigate.exceptions.HTTPResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Data
public class OperationResult {

    public enum Status{
        SUCCESS,WARN,ERROR;
    }
    @JsonProperty(value = "method")
    private String httpRequestMethod;

    @JsonProperty(value = "path")
    private String url;

    @JsonProperty(value = "status")
    private Status status;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "reason")
    private String reason;

    public void setRequestInfo(HttpServletRequest request){
        httpRequestMethod = request.getMethod();
        url = request.getRequestURL().toString();
    }

    public void setOperationResult(HttpServletRequest request, Status status, String code, String reason){
        setRequestInfo(request);
        this.status = status;
        this.code = code;
        this.reason = reason;
    }
}
