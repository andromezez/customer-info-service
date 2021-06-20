package com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint;

import com.apigate.config.Config;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:51 PM
 */
@Data
@JsonInclude
public class MnoEntryDto {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime createdAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime updatedAt;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String authKey;

    @JsonProperty
    private String tokenUrl;
}
