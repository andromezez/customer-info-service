package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.dto.httprequestbody.routing.UpdateRoutingEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.routing.RoutingEntryDto;
import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import com.apigate.customer_info_service.entities.Routing;
import com.apigate.customer_info_service.entities.RoutingPK;
import com.apigate.customer_info_service.repository.ClientRepository;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.customer_info_service.repository.RoutingRepository;
import com.apigate.exceptions.db.RecordNotFoundException;
import com.apigate.exceptions.internal.ErrorException;
import com.apigate.logging.CommonLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static final String LOCK_ON_OPERATOR = "celcom";

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
        var routingListDB = routingRepository.findByClientIdAndMnoId(clientId,operatorId);
        return transformToDto(routingListDB);
    }

    public List<Routing> getRoutingByMnoApiEndpointId(String mnoApiEndpointId){
        var routingListDB = routingRepository.findByMnoApiEndpointId(mnoApiEndpointId);
        return routingListDB;
    }

    public RoutingEntryDto update(String clientId, String mnoApiEndpointId, UpdateRoutingEntryReqDto updateRoutingEntryReqDto) throws RecordNotFoundException{
        RoutingPK routingPK = new RoutingPK(clientId,mnoApiEndpointId);

        var routingDB = routingRepository.findById(routingPK);

        if(routingDB.isPresent()){

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

    public RoutingEntryDto create(String clientId, String mnoApiEndpointId, UpdateRoutingEntryReqDto updateRoutingEntryReqDto) throws ErrorException, RecordNotFoundException {
        var clientEntity = clientRepository.findById(clientId);
        var endpointEntity = mnoApiEndpointRepository.findById(mnoApiEndpointId);

        if(clientEntity.isEmpty() || endpointEntity.isEmpty()){
            throw new RecordNotFoundException("Either clientId or endpointId doesn't exist in DB");
        }

        var routingPK = new RoutingPK(clientId,mnoApiEndpointId);

        if(!routingRepository.existsById(routingPK)){
            var routingEntity = new Routing(routingPK, updateRoutingEntryReqDto.isCacheActive(), ZonedDateTime.now(), ZonedDateTime.now());
            routingEntity = routingRepository.save(routingEntity);

            routingRepository.refresh(routingEntity); //refresh need to be called to reload all the object graph

            var result = new RoutingEntryDto();
            result.parseFrom(routingEntity);

            return result;
        }else{
            throw new ErrorException("Routing clientId and endpointId already exist");
        }
    }

    private Optional<MnoApiEndpoint> findURI(HttpServletRequest request) throws MalformedURLException {

        MnoApiEndpoint foundEndpoint = null;

        var requestURI = request.getRequestURI();
        CommonLog.getInstance().logInfo("incoming request URI : " + requestURI);

        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for(var endpoint : mnoApiEndpointRepository.findByMnoId(LOCK_ON_OPERATOR)){
            CommonLog.getInstance().logInfo("operator URL from DB : " + endpoint.getUrl());
            URL url = new URL(endpoint.getUrl());
            String path = url.getPath();
            CommonLog.getInstance().logInfo("operator URI path from DB : " + path);

            if(antPathMatcher.match(path,requestURI)){
                CommonLog.getInstance().logInfo("found match url from db : " + endpoint.getUrl());
                foundEndpoint = endpoint;
                break;
            }
        }

        return Optional.ofNullable(foundEndpoint);
    }

    public Optional<Routing> findRouting(HttpServletRequest request, String partnerId) throws MalformedURLException {
        var endpoint = findURI(request);
        if(endpoint.isPresent()){
            var routingList = routingRepository.findByEndpointIdAndClientPartnerId(endpoint.get().getId(), partnerId);
            if(routingList.isEmpty()){
                return Optional.empty();
            }else{
                if(routingList.size() > 1){
                    throw new ErrorException("Internal data error. Found multiple routing for URI " + request.getRequestURI() +
                            "and partnerId " + partnerId);
                }else{
                    return Optional.of(routingList.get(0));
                }
            }
        }else{
            return Optional.empty();
        }
    }

}
