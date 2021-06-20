package com.apigate.customer_info_service.dto.httpresponsebody.routing;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 14/6/2021 5:21 PM
 */
@Data
@JsonInclude
public class ListOfRoutingResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Setter(AccessLevel.NONE)
    private List<RoutingEntryDto> routingConfigs = new ArrayList<>(1);

    public ListOfRoutingResDto(){
        super();
    }
}
