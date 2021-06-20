package com.apigate.customer_info_service.dto.httprequestbody.masking;

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
 * @date 16/6/2021 9:58 AM
 */
@Data
@JsonInclude
public class UpdateMaskingEntryReqDto {

    @JsonProperty
    @NotBlank(message = "jsonPath must have value",  groups = Step1.class)
    @Size(max = 2000, message = "jsonPath invalid length", groups = Step2.class)
    private String jsonPath;

    @JsonProperty
    @NotBlank(message = "mask must have value",  groups = Step1.class)
    @Size(max = 30, message = "mask invalid length", groups = Step2.class)
    private String mask;

    @JsonProperty
    @NotNull(message = "active must have value",  groups = Step1.class)
    private boolean active;
}
