package com.apigate.exceptions.HTTPResponseBody;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.OperationResultErrorLevel;
import com.apigate.exceptions.OperationResultWarnLevel;
import com.apigate.exceptions.ResponseCodes;
import com.apigate.exceptions.internal.ErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Data
public class ErrorInfo extends GenericResponseMessageDto{

    public ErrorInfo(String httpRequestMethod, String path, OperationResult.Status status, String code, String reason) {
        super();
        super.operationResult.setHttpRequestMethod(httpRequestMethod);
        super.operationResult.setUrl(path);
        super.operationResult.setStatus(status);
        super.operationResult.setCode(code);
        super.operationResult.setReason(reason);
    }

    public ErrorInfo(HttpServletRequest request, ResponseStatusException ex) {
        this(request.getMethod(),
                request.getRequestURL().toString(),
                OperationResult.Status.ERROR,
                "",
                "");
        resolveFromException(ex);
    }

    public ErrorInfo(HttpServletRequest request, Exception ex) {
        this(request.getMethod(),
                request.getRequestURL().toString(),
                OperationResult.Status.ERROR,
                "",
                "");
        resolveFromException(ex);
    }

    private ErrorInfo(){}

    private void resolveFromException(Exception ex){
        OperationResult.Status status = OperationResult.Status.ERROR;
        String responseCode = "";
        String responseReason = "";
        if(ex instanceof OperationResultErrorLevel){
            responseCode = ((OperationResultErrorLevel)ex).getErrorResponseCode().getCode();
            if((ex instanceof ErrorException) && (!((ErrorException)ex).isReasonProvided())){
                boolean nestedCauseFound = ((ErrorException)ex).getEx().getCause() != null;
                Throwable exBefore = ((ErrorException)ex).getEx();
                Throwable exFurther = ((ErrorException)ex).getEx();
                while (nestedCauseFound){
                    if(exFurther==null){
                        nestedCauseFound = false;
                    }else{
                        exBefore = exFurther;
                        exFurther = exFurther.getCause();
                    }
                }
                responseReason = ((AbstractException)ex).getReason() + " | " + exBefore.getMessage();
            }else{
                responseReason = ((AbstractException)ex).getReason();
            }
        }else if(ex instanceof OperationResultWarnLevel){
            status = OperationResult.Status.WARN;
            responseCode = ((OperationResultWarnLevel)ex).getWarnResponseCode().getCode();
            responseReason = ((AbstractException)ex).getReason();
        }else if(ex instanceof ResponseStatusException){
            responseCode = ResponseCodes.Errors.CIS9999.getCode();
            responseReason = ResponseCodes.Errors.CIS9999.getMessage()+ " | " + ((ResponseStatusException) ex).getReason();
        }else {
            responseCode = ResponseCodes.Errors.CIS9999.getCode();
            responseReason = ResponseCodes.Errors.CIS9999.getMessage();
        }
        super.operationResult.setStatus(status);
        super.operationResult.setCode(responseCode);
        super.operationResult.setReason(responseReason);
    }

    public String toJson(ObjectMapper objectMapper){
        String json = "";
        try{
            json = objectMapper.writeValueAsString(this);
        }catch (JsonProcessingException e){
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append(" \"operationResult\" : \"Something wrong in serialize the ErrorInfo. Check with developer.\"");
            jsonBuilder.append("}");
            json = jsonBuilder.toString();
        }
        return json;
    }


}
