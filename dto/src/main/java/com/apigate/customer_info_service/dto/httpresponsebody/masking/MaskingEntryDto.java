package com.apigate.customer_info_service.dto.httpresponsebody.masking;

import com.apigate.config.Config;
import com.apigate.customer_info_service.entities.Masking;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 4:58 PM
 */
@Data
@JsonInclude
public class MaskingEntryDto {
    @JsonProperty
    private String clientId;

    @JsonProperty
    private String endpointId;

    @JsonProperty
    private String jsonPath;

    @JsonProperty
    private boolean atLog;

    @JsonProperty
    private boolean atResponse;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime createdAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.TIMESTAMP_PATTERN)
    private ZonedDateTime updatedAt;

    public void parseFrom(Masking masking){
        clientId = masking.getMaskingPK().getClientId();
        endpointId = masking.getMaskingPK().getMnoApiEndpointId();
        jsonPath = masking.getMaskingPK().getJsonPath();
        atLog = masking.isAtLog();
        atResponse = masking.isAtResponse();
        createdAt = masking.getCreatedAt();
        updatedAt = masking.getUpdatedAt();
    }
}
