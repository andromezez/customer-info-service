package com.apigate.customer_info_service.dto.httpresponsebody.operator;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Bayu Utomo
 * @date 11/6/2021 8:17 PM
 */
@Data
@JsonInclude
public class OperatorResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private MnoEntryDto operator;

    public OperatorResDto(){
        super();
    }
}
