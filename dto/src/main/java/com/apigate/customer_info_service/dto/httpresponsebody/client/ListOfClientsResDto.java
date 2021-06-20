package com.apigate.customer_info_service.dto.httpresponsebody.client;

import com.apigate.exceptions.HTTPResponseBody.GenericResponseMessageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 13/6/2021 7:28 PM
 */
@Data
@JsonInclude
public class ListOfClientsResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Setter(AccessLevel.NONE)
    private List<ClientEntryDto> clients = new ArrayList<>(1);

    public ListOfClientsResDto(){
        super();
    }
}
