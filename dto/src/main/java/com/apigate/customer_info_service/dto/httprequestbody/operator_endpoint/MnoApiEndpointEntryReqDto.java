package com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint;

import com.apigate.customer_info_service.dto.validator.Step1;
import com.apigate.customer_info_service.dto.validator.Step2;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 8:15 PM
 */
@Data
@JsonInclude
public class MnoApiEndpointEntryReqDto {

    @JsonProperty
    @NotBlank(message = "name must have value",  groups = Step1.class)
    @Size(max = 50, message = "name invalid length", groups = Step2.class)
    private String name;

    @JsonProperty
    @NotBlank(message = "url must have value",  groups = Step1.class)
    @Size(max = 2000, message = "url invalid length", groups = Step2.class)
    private String url;

    @JsonProperty
    @NotNull(message = "cachePeriod must have value",  groups = Step1.class)
    private int cachePeriod;

}
