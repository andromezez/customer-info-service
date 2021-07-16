package com.apigate.customer_info_service.dto.httpresponsebody.operator;

import com.apigate.config.Config;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 10/6/2021 4:12 PM
 */
@Data
@JsonInclude
public class MnoApiEndpointEntryDto {
    @JsonProperty
    private String id;

    @JsonProperty
    private String url;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime createdAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime updatedAt;

    @JsonProperty
    private String name;

    @JsonProperty
    private String operatorId;
}
