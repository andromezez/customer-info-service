package com.apigate.customer_info_service.dto.httpresponsebody.client;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Bayu Utomo
 * @date 14/6/2021 8:47 AM
 */
@Data
@JsonInclude
public class ClientResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private ClientEntryDto client;

    public ClientResDto(){
        super();
    }
}
