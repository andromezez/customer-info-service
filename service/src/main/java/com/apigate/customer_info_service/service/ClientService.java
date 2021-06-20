package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.dto.httprequestbody.client.CreateClientEntryReqDto;
import com.apigate.customer_info_service.dto.httprequestbody.client.UpdateClientEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.client.ClientEntryDto;
import com.apigate.customer_info_service.entities.Client;
import com.apigate.customer_info_service.repository.ClientRepository;
import com.apigate.exceptions.db.DuplicateRecordException;
import com.apigate.exceptions.db.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 13/6/2021 7:07 PM
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private List<ClientEntryDto> fillClientsDto(List<Client> clientList){
        var result = new ArrayList<ClientEntryDto>(1);
        for(Client client : clientList) {
            ClientEntryDto dto = new ClientEntryDto();
            dto.parseFrom(client);
            result.add(dto);
        }
        return result;
    }

    public List<ClientEntryDto> getClientsDetails(String partnerId){
        var clientList = clientRepository.findByPartnerIdContainingIgnoreCase(partnerId);
        return fillClientsDto(clientList);
    }

    public List<ClientEntryDto> getClientsDetails(){
        var clientList = clientRepository.findAll();
        return fillClientsDto(clientList);
    }

    public ClientEntryDto create(CreateClientEntryReqDto createClientEntryReqDto) throws DuplicateRecordException{
        if(clientRepository.findByIdOrPartnerIdContainingIgnoreCase(createClientEntryReqDto.getId(), createClientEntryReqDto.getPartnerId()).isEmpty()){
            var clientEntity = new Client();
            clientEntity.setId(createClientEntryReqDto.getId());
            clientEntity.setCacheActive(createClientEntryReqDto.isCacheActive());
            clientEntity.setPartnerId(createClientEntryReqDto.getPartnerId());
            clientEntity.setCreatedAt(ZonedDateTime.now());
            clientEntity.setUpdatedAt(ZonedDateTime.now());

            clientEntity = clientRepository.save(clientEntity);

            clientRepository.refresh(clientEntity);

            var result = new ClientEntryDto();
            result.parseFrom(clientEntity);

            return result;
        }else{
            throw new DuplicateRecordException("id or partnerId already exist");
        }
    }

    public ClientEntryDto update(String id, UpdateClientEntryReqDto updateClientEntryReqDto) throws DuplicateRecordException, RecordNotFoundException{
        if(clientRepository.findByIdNotAndPartnerIdContainingIgnoreCase(id, updateClientEntryReqDto.getPartnerId()).isEmpty()){
            var existingClientDB = clientRepository.findById(id);
            if(existingClientDB.isPresent()){
                existingClientDB.get().setPartnerId(updateClientEntryReqDto.getPartnerId());
                existingClientDB.get().setCacheActive(updateClientEntryReqDto.isCacheActive());
                existingClientDB.get().setUpdatedAt(ZonedDateTime.now());

                var updatedClient = clientRepository.save(existingClientDB.get());

                ClientEntryDto result = new ClientEntryDto();
                result.parseFrom(updatedClient);

                return result;
            }else{
                throw new RecordNotFoundException(id);
            }
        }else{
            throw new DuplicateRecordException("partnerId owned by other resource");
        }
    }
}
