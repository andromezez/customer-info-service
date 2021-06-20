package com.apigate.customer_info_service.dto.httprequestbody.client;

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
 * @date 14/6/2021 9:40 AM
 */
@Data
@JsonInclude
public class UpdateClientEntryReqDto {
    @JsonProperty
    @NotBlank(message = "partnerId must have value",  groups = Step1.class)
    @Size(max = 50, message = "partnerId invalid length", groups = Step2.class)
    private String partnerId;

    @JsonProperty
    @NotNull(message = "cacheActive must have value",  groups = Step1.class)
    private boolean cacheActive;
}
