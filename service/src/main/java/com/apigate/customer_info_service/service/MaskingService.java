package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.dto.httprequestbody.masking.UpdateMaskingEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.masking.MaskingEntryDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.RoutingEntryDto;
import com.apigate.customer_info_service.entities.Masking;
import com.apigate.customer_info_service.entities.MaskingPK;
import com.apigate.customer_info_service.entities.Routing;
import com.apigate.customer_info_service.entities.RoutingPK;
import com.apigate.customer_info_service.repository.MaskingRepository;
import com.apigate.customer_info_service.repository.RoutingRepository;
import com.apigate.exceptions.db.DuplicateRecordException;
import com.apigate.exceptions.db.RecordNotFoundException;
import com.apigate.exceptions.internal.SystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 4:32 PM
 */
@Service
public class MaskingService {

    @Autowired
    private MaskingRepository maskingRepository;

    @Autowired
    private RoutingRepository routingRepository;

    private List<MaskingEntryDto> transformToDto(List<Masking> maskingListDB){
        var maskingListDto = new ArrayList<MaskingEntryDto>(1);
        for(Masking masking : maskingListDB){
            var maskingEntryDto = new MaskingEntryDto();
            maskingEntryDto.parseFrom(masking);

            maskingListDto.add(maskingEntryDto);
        }
        return maskingListDto;
    }

    public List<MaskingEntryDto> getAllMasking(){
        return transformToDto(maskingRepository.findAll());
    }

    public List<MaskingEntryDto> getMaskingBy(String clientId, String endpointId){
        return transformToDto(maskingRepository.findByPartialId(clientId, endpointId));
    }

    public List<MaskingEntryDto> getMaskingByClientId(String clientId){
        return transformToDto(maskingRepository.findByClientIdOnly(clientId));
    }

    public List<MaskingEntryDto> getMaskingByEndpointId(String endpointId){
        return transformToDto(maskingRepository.findByEndpointIdOnly(endpointId));
    }

    @Transactional
    public MaskingEntryDto update(String clientId, String endpointId, String jsonPath, UpdateMaskingEntryReqDto updateMaskingEntryReqDto) throws RecordNotFoundException {
        MaskingPK maskingPK = new MaskingPK(clientId, endpointId, jsonPath);

        var existingMaskingDB = maskingRepository.findById(maskingPK);

        if(existingMaskingDB.isPresent()){
            MaskingEntryDto result = new MaskingEntryDto();;
            if(existingMaskingDB.get().getMaskingPK().getJsonPath().equals(updateMaskingEntryReqDto.getJsonPath())){
                existingMaskingDB.get().setMask(updateMaskingEntryReqDto.getMask());
                existingMaskingDB.get().setActive(updateMaskingEntryReqDto.isActive());
                existingMaskingDB.get().setUpdatedAt(ZonedDateTime.now());

                var maskingDBAfterUpdate = maskingRepository.save(existingMaskingDB.get());

                result.parseFrom(maskingDBAfterUpdate);
            }else{
                Masking newMasking = new Masking(new MaskingPK(clientId, endpointId, updateMaskingEntryReqDto.getJsonPath()),
                                            updateMaskingEntryReqDto.getMask(),
                                            updateMaskingEntryReqDto.isActive(),
                                            ZonedDateTime.now(),
                                            ZonedDateTime.now());

                maskingRepository.delete(existingMaskingDB.get());
                newMasking = maskingRepository.save(newMasking);

                result.parseFrom(newMasking);
            }

            return result;
        }else{
            throw new RecordNotFoundException(maskingPK.toString());
        }
    }

    public MaskingEntryDto create(String clientId, String endpointId,UpdateMaskingEntryReqDto updateMaskingEntryReqDto){
        RoutingPK routingPK = new RoutingPK(clientId, endpointId);

        if(!routingRepository.existsById(routingPK)){
            throw new RecordNotFoundException("Routing with provided clientId and endpointId doesn't exist in DB");
        }

        MaskingPK maskingPK = new MaskingPK(clientId, endpointId, updateMaskingEntryReqDto.getJsonPath());

        if(!maskingRepository.existsById(maskingPK)){
            Masking newMasking = new Masking(maskingPK,
                    updateMaskingEntryReqDto.getMask(),
                    updateMaskingEntryReqDto.isActive(),
                    ZonedDateTime.now(),
                    ZonedDateTime.now());

            newMasking = maskingRepository.save(newMasking);

            maskingRepository.refresh(newMasking);

            var result = new MaskingEntryDto();
            result.parseFrom(newMasking);

            return result;
        }else{
            throw new DuplicateRecordException("Masking with clientId and endpointId and jsonPath already exist");
        }
    }

}
