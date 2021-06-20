package com.apigate.customer_info_service.dto.httpresponsebody.routing;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 9:31 AM
 */
@Data
@JsonInclude
public class RoutingResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private RoutingEntryDto routingConfig;

    public RoutingResDto(){
        super();
    }
}
