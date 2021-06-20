package com.apigate.customer_info_service.dto.httpresponsebody.operator;

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
 * @date 10/6/2021 4:20 PM
 */
@Data
@JsonInclude
public class ListOfOperatorsResDto extends GenericResponseMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Setter(AccessLevel.NONE)
    private List<MnoEntryDto> operators = new ArrayList<>(1);

    public ListOfOperatorsResDto(){
        super();
    }
}
