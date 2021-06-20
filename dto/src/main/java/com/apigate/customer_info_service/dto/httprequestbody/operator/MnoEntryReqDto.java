package com.apigate.customer_info_service.dto.httprequestbody.operator;

import com.apigate.customer_info_service.dto.validator.Step1;
import com.apigate.customer_info_service.dto.validator.Step2;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Bayu Utomo
 * @date 11/6/2021 10:42 AM
 */
@Data
@JsonInclude
public class MnoEntryReqDto {

    @JsonProperty
    @NotBlank(message = "name must have value",  groups = Step1.class)
    @Size(max = 50, message = "name invalid length", groups = Step2.class)
    private String name;

    @JsonProperty
    @NotBlank(message = "username must have value",  groups = Step1.class)
    @Size(max = 50, message = "username invalid length", groups = Step2.class)
    private String username;

    @JsonProperty
    @NotBlank(message = "password must have value",  groups = Step1.class)
    @Size(max = 50, message = "password invalid length", groups = Step2.class)
    private String password;

    @JsonProperty
    @NotBlank(message = "authKey must have value",  groups = Step1.class)
    @Size(max = 2000, message = "authKey invalid length", groups = Step2.class)
    private String authKey;

    @JsonProperty
    @NotBlank(message = "tokenUrl must have value",  groups = Step1.class)
    @Size(max = 2000, message = "tokenUrl invalid length", groups = Step2.class)
    private String tokenUrl;
}
