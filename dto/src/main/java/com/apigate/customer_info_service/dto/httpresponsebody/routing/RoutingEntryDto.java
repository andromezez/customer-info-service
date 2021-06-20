package com.apigate.customer_info_service.dto.httpresponsebody.routing;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httpresponsebody.client.ClientEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.entities.Routing;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 14/6/2021 1:15 PM
 */
@Data
@JsonInclude
public class RoutingEntryDto {
    @JsonProperty
    private int cachePeriod;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime createdAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime updatedAt;

    @JsonProperty
    private String redisKey;

    @JsonProperty
    private boolean cacheActive;

    @JsonProperty
    private ClientEntryDto client;

    @JsonProperty
    private MnoApiEndpointEntryDto endpoint;

    public void parseFrom(Routing routing){
        cachePeriod = routing.getCachePeriod();
        createdAt= routing.getCreatedAt();
        updatedAt = routing.getUpdatedAt();
        redisKey = routing.getRedisKey();
        cacheActive = routing.isCacheActive();

        client = new ClientEntryDto();
        client.parseFrom(routing.getClient());

        endpoint = new MnoApiEndpointEntryDto();
        endpoint.parseFrom(routing.getMnoApiEndpoint());
    }
}
