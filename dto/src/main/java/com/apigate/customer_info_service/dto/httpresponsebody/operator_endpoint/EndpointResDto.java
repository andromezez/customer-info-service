package com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:38 PM
 */
@Data
@JsonInclude
public class EndpointResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private MnoApiEndpointEntryDto endpoint;

    public EndpointResDto(){
        super();
    }
}
