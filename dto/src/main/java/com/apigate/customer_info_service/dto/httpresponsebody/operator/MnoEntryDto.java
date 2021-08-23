package com.apigate.customer_info_service.dto.httpresponsebody.operator;

import com.apigate.config.Config;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 10/6/2021 4:01 PM
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

    @JsonProperty
    @Setter(AccessLevel.NONE)
    private List<MnoApiEndpointEntryDto> endpoints = new ArrayList<>(1);

    public void parseFrom(Mno mno){
        this.setId(mno.getId());
        this.setName(mno.getName());
        this.setCreatedAt(mno.getCreatedAt());
        this.setUpdatedAt(mno.getUpdatedAt());
        this.setUsername(mno.getUsername());
        this.setPassword(mno.getPassword());
        this.setAuthKey(mno.getAuthKey());
        this.setTokenUrl(mno.getTokenUrl());

        for(MnoApiEndpoint endpoint : mno.getMnoApiEndpointCollection()){
            MnoApiEndpointEntryDto mnoApiEndpointEntryDto = new MnoApiEndpointEntryDto();
            mnoApiEndpointEntryDto.setId(endpoint.getId());
            mnoApiEndpointEntryDto.setUrl(endpoint.getUrl());
            mnoApiEndpointEntryDto.setCreatedAt(endpoint.getCreatedAt());
            mnoApiEndpointEntryDto.setUpdatedAt(endpoint.getUpdatedAt());
            mnoApiEndpointEntryDto.setName(endpoint.getName());
            mnoApiEndpointEntryDto.setOperatorId(endpoint.getMnoId().getId());
            mnoApiEndpointEntryDto.setCachePeriod(endpoint.getCachePeriod());
            mnoApiEndpointEntryDto.setRedisKey(endpoint.getRedisKey());

            this.getEndpoints().add(mnoApiEndpointEntryDto);
        }
    }
}
