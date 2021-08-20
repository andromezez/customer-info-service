package com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint;

import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:45 PM
 */
@Data
@JsonInclude
public class MnoApiEndpointEntryDto extends com.apigate.customer_info_service.dto.httpresponsebody.operator.MnoApiEndpointEntryDto {

    @JsonProperty
    private MnoEntryDto operator;

    public void parseFrom(MnoApiEndpoint endpoint){
        this.setId(endpoint.getId());
        this.setUrl(endpoint.getUrl());
        this.setCreatedAt(endpoint.getCreatedAt());
        this.setUpdatedAt(endpoint.getUpdatedAt());
        this.setName(endpoint.getName());
        this.setOperatorId(endpoint.getMnoId().getId());
        this.setCachePeriod(endpoint.getCachePeriod());
        this.setRedisKey(endpoint.getRedisKey());
        operator = new MnoEntryDto();
        operator.setId(endpoint.getMnoId().getId());
        operator.setName(endpoint.getMnoId().getName());
        operator.setCreatedAt(endpoint.getMnoId().getCreatedAt());
        operator.setUpdatedAt(endpoint.getMnoId().getUpdatedAt());
        operator.setUsername(endpoint.getMnoId().getUsername());
        operator.setPassword(endpoint.getMnoId().getPassword());
        operator.setAuthKey(endpoint.getMnoId().getAuthKey());
        operator.setTokenUrl(endpoint.getMnoId().getTokenUrl());
    }
}
