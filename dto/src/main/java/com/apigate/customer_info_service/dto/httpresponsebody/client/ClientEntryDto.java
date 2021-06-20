package com.apigate.customer_info_service.dto.httpresponsebody.client;

import com.apigate.config.Config;
import com.apigate.customer_info_service.entities.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 13/6/2021 7:17 PM
 */
@Data
@JsonInclude
public class ClientEntryDto {

    @JsonProperty
    private String id;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime createdAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime updatedAt;

    @JsonProperty
    private boolean cacheActive;

    @JsonProperty
    private String partnerId;

    public void parseFrom(Client client){
        this.id = client.getId();
        this.createdAt = client.getCreatedAt();
        this.updatedAt = client.getUpdatedAt();
        this.cacheActive = client.isCacheActive();
        this.partnerId = client.getPartnerId();
    }
}
