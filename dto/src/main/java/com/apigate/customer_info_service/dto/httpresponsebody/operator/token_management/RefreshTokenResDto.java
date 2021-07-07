package com.apigate.customer_info_service.dto.httpresponsebody.operator.token_management;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Bayu Utomo
 * @date 7/7/2021 9:23 AM
 */
@Data
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenResDto {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private int expiresIn;

}
