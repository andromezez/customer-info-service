package com.apigate.customer_info_service.service;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httpresponsebody.operator.MnoEntryDto;
import com.apigate.customer_info_service.dto.httprequestbody.operator.MnoEntryReqDto;
import com.apigate.customer_info_service.entities.Mno;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.customer_info_service.repository.MnoRepository;
import com.apigate.customer_info_service.service.token_management.TokenManagement;
import com.apigate.exceptions.db.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 10/6/2021 11:28 AM
 */
@Service
public class OperatorService {
    @Autowired
    private MnoRepository mnoRepository;

    @Autowired
    private MnoApiEndpointRepository mnoApiEndpointRepository;

    @Autowired
    private RoutingService routingService;

    @Autowired
    private OperatorEndpointService endpointService;

    @Autowired
    private TokenManagement tokenManagement;

    @Autowired
    private CacheService cacheService;

    private List<MnoEntryDto> fillOperatorsDto(List<Mno> mnoList){
        List<MnoEntryDto> result = new ArrayList<>(1);
        for(Mno mno : mnoList){
            result.add(fillOperatorDto(mno));
        }
        return result;
    }

    private MnoEntryDto fillOperatorDto(Mno mno){
        MnoEntryDto mnoEntryDto = new MnoEntryDto();
        mnoEntryDto.parseFrom(mno);
        return mnoEntryDto;
    }

    public List<MnoEntryDto> getOperatorsDetails(String name){
        var mnoList = mnoRepository.findByNameContainingIgnoreCase(name);
        return fillOperatorsDto(mnoList);
    }

    public List<MnoEntryDto> getOperatorsDetails(){
        var mnoList = mnoRepository.findAll();
        return fillOperatorsDto(mnoList);
    }

    public MnoEntryDto update(String id, MnoEntryReqDto mnoEntryReqDto) throws RecordNotFoundException{
        var mnoDB = mnoRepository.findById(id);

        if(mnoDB.isPresent()){
            mnoDB.get().setName(mnoEntryReqDto.getName());
            mnoDB.get().setUsername(mnoEntryReqDto.getUsername());
            mnoDB.get().setPassword(mnoEntryReqDto.getPassword());
            mnoDB.get().setAuthKey(mnoEntryReqDto.getAuthKey());
            mnoDB.get().setTokenUrl(mnoEntryReqDto.getTokenUrl());
            mnoDB.get().setUpdatedAt(ZonedDateTime.now());
            Mno result = mnoRepository.save(mnoDB.get());
            return fillOperatorDto(result);
        }else{
            throw new RecordNotFoundException(id);
        }
    }

    public void clearCache(String id){
        var endpointList = mnoApiEndpointRepository.findByMnoId(id);
        for(var endpoint : endpointList){
            endpointService.clearCache(endpoint);
        }
    }

    public String getAccessToken(Mno mno){
        for(int i=1;i<=Config.MAX_RETRY_GET_ACCESS_TOKEN_DELAY;i++){
            if(cacheService.isLocked(tokenManagement.getLockRedisKey(mno))){
                try {
                    Thread.sleep(Config.GET_ACCESS_TOKEN_DELAY_WHEN_LOCKED.toMillis());
                } catch (InterruptedException e) {
                    //ignore
                }
            }else{
                break;
            }
        }
        String redisKey = tokenManagement.getTokenAccessRedisKey(mno);
        return cacheService.getFromCache(redisKey);
    }
}
