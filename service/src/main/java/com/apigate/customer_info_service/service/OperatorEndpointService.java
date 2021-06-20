package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.dto.httprequestbody.operator.MnoEntryReqDto;
import com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint.MnoApiEndpointEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.MnoEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.exceptions.db.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:34 PM
 */
@Service
public class OperatorEndpointService {
    @Autowired
    private MnoApiEndpointRepository mnoApiEndpointRepository;

    public MnoApiEndpointEntryDto update(String id, MnoApiEndpointEntryReqDto mnoApiEndpointEntryReqDto) throws RecordNotFoundException {
        var endpointDB = mnoApiEndpointRepository.findById(id);

        if(endpointDB.isPresent()){
            endpointDB.get().setName(mnoApiEndpointEntryReqDto.getName());
            endpointDB.get().setUrl(mnoApiEndpointEntryReqDto.getUrl());
            endpointDB.get().setUpdatedAt(ZonedDateTime.now());

            var result = mnoApiEndpointRepository.save(endpointDB.get());

            var mnoApiEndpointEntryDto = new MnoApiEndpointEntryDto();
            mnoApiEndpointEntryDto.parseFrom(result);

            return mnoApiEndpointEntryDto;
        }else{
            throw new RecordNotFoundException(id);
        }
    }
}
