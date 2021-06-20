package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.dto.httprequestbody.routing.UpdateRoutingEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.RoutingEntryDto;
import com.apigate.customer_info_service.entities.Routing;
import com.apigate.customer_info_service.entities.RoutingPK;
import com.apigate.customer_info_service.repository.ClientRepository;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.customer_info_service.repository.RoutingRepository;
import com.apigate.exceptions.db.DuplicateRecordException;
import com.apigate.exceptions.db.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 14/6/2021 10:51 AM
 */
@Service
public class RoutingService {
    @Autowired
    private RoutingRepository routingRepository;

    @Autowired
    private MnoApiEndpointRepository mnoApiEndpointRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private List<RoutingEntryDto> transformToDto(List<Routing> routingListDB){
        var routingsDto = new ArrayList<RoutingEntryDto>(1);
        for(Routing routing : routingListDB){
            var routingEntryDto = new RoutingEntryDto();
            routingEntryDto.parseFrom(routing);

            routingsDto.add(routingEntryDto);
        }
        return routingsDto;
    }

    public List<RoutingEntryDto> getAllRouting(){
        var routingListDB = routingRepository.findAll();
        return transformToDto(routingListDB);
    }

    public List<RoutingEntryDto> getRoutingByClientId(String clientId){
        var routingListDB = routingRepository.findByClientId(clientId);
        return transformToDto(routingListDB);
    }

    public List<RoutingEntryDto> getRoutingByOperatorId(String operatorId){
        var routingListDB = routingRepository.findByMnoId(operatorId);
        return transformToDto(routingListDB);
    }

    public List<RoutingEntryDto> getRoutingBy(String clientId, String operatorId){
        var routingListDB = routingRepository.findBy(clientId,operatorId);
        return transformToDto(routingListDB);
    }

    public RoutingEntryDto update(String clientId, String mnoApiEndpointId, UpdateRoutingEntryReqDto updateRoutingEntryReqDto) throws RecordNotFoundException{
        RoutingPK routingPK = new RoutingPK(clientId,mnoApiEndpointId);

        var routingDB = routingRepository.findById(routingPK);

        if(routingDB.isPresent()){
            routingDB.get().setCachePeriod(updateRoutingEntryReqDto.getCachePeriod());
            routingDB.get().setCacheActive(updateRoutingEntryReqDto.isCacheActive());
            routingDB.get().setUpdatedAt(ZonedDateTime.now());

            Routing routingDBAfterUpdate = routingRepository.save(routingDB.get());

            RoutingEntryDto result = new RoutingEntryDto();
            result.parseFrom(routingDBAfterUpdate);

            return result;
        }else{
            throw new RecordNotFoundException(routingPK.toString());
        }
    }

    public RoutingEntryDto create(String clientId, String mnoApiEndpointId, UpdateRoutingEntryReqDto updateRoutingEntryReqDto) throws DuplicateRecordException, RecordNotFoundException {
        var clientEntity = clientRepository.findById(clientId);
        var endpointEntity = mnoApiEndpointRepository.findById(mnoApiEndpointId);

        if(clientEntity.isEmpty() || endpointEntity.isEmpty()){
            throw new RecordNotFoundException("Either clientId or endpointId doesn't exist in DB");
        }

        var routingPK = new RoutingPK(clientId,mnoApiEndpointId);

        if(!routingRepository.existsById(routingPK)){
            String redisKey = clientId+":"+endpointEntity.get().getMnoId().getId()+":"+endpointEntity.get().getId();
            var routingEntity = new Routing(routingPK, updateRoutingEntryReqDto.isCacheActive(), updateRoutingEntryReqDto.getCachePeriod(), ZonedDateTime.now(), redisKey, ZonedDateTime.now());
            routingEntity = routingRepository.save(routingEntity);

            routingRepository.refresh(routingEntity); //refresh need to be called to reload all the object graph

            var result = new RoutingEntryDto();
            result.parseFrom(routingEntity);

            return result;
        }else{
            throw new DuplicateRecordException("Routing clientId and endpointId already exist");
        }
    }



    public void createCache(String key, String value, int secondsExpire){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, Duration.ofSeconds(secondsExpire));
    }


}
