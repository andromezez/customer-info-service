package com.apigate.customer_info_service.dto.httprequestbody.routing;

import com.apigate.customer_info_service.dto.validator.Step1;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 8:43 AM
 */
@Data
@JsonInclude
public class UpdateRoutingEntryReqDto {
    @JsonProperty
    @NotNull(message = "cachePeriod must have value",  groups = Step1.class)
    private int cachePeriod;

    @JsonProperty
    @NotNull(message = "cacheActive must have value",  groups = Step1.class)
    private boolean cacheActive;
}
