package com.apigate.customer_info_service.dto.httpresponsebody.masking;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Bayu Utomo
 * @date 16/6/2021 2:26 PM
 */
@Data
@JsonInclude
public class MaskingResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private MaskingEntryDto mask;

    public MaskingResDto(){
        super();
    }
}