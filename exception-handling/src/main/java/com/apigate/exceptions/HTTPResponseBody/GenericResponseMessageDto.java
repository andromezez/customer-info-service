package com.apigate.exceptions.HTTPResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Data
public class GenericResponseMessageDto {

    @JsonProperty(value = "operationResult")
    protected OperationResult operationResult;

    public GenericResponseMessageDto(){
        operationResult = new OperationResult();
    }
}
