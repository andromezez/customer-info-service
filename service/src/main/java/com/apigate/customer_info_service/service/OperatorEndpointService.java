package com.apigate.customer_info_service.service;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint.MnoApiEndpointEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.customer_info_service.repository.MnoRepository;
import com.apigate.exceptions.db.RecordNotFoundException;
import com.apigate.exceptions.internal.ErrorException;
import com.apigate.logging.ServicesLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 12/6/2021 7:34 PM
 */
@Service
public class OperatorEndpointService {
    @Autowired
    private MnoApiEndpointRepository mnoApiEndpointRepository;

    @Autowired
    private RoutingService routingService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MnoRepository mnoRepository;

    public MnoApiEndpointEntryDto update(String id, MnoApiEndpointEntryReqDto mnoApiEndpointEntryReqDto) throws RecordNotFoundException {
        var endpointDB = mnoApiEndpointRepository.findById(id);

        if(endpointDB.isPresent()){
            var cachePeriodBefore = endpointDB.get().getCachePeriod();

            endpointDB.get().setName(mnoApiEndpointEntryReqDto.getName());
            endpointDB.get().setUrl(mnoApiEndpointEntryReqDto.getUrl());
            endpointDB.get().setUpdatedAt(ZonedDateTime.now());
            endpointDB.get().setCachePeriod(mnoApiEndpointEntryReqDto.getCachePeriod());

            var resultAfterUpdate = mnoApiEndpointRepository.save(endpointDB.get());

            var mnoApiEndpointEntryDto = new MnoApiEndpointEntryDto();
            mnoApiEndpointEntryDto.parseFrom(resultAfterUpdate);

            if (cachePeriodBefore != resultAfterUpdate.getCachePeriod()) {
                removeAPIResponseCache(resultAfterUpdate);
            }

            return mnoApiEndpointEntryDto;
        }else{
            throw new RecordNotFoundException(id);
        }
    }

    public MnoApiEndpointEntryDto create(String mnoId, MnoApiEndpointEntryReqDto mnoApiEndpointEntryReqDto) throws ErrorException {
        var mnoEntity = mnoRepository.findById(mnoId);

        if(mnoEntity.isEmpty()){
            throw new RecordNotFoundException(mnoId);
        }

        var createdAt = ZonedDateTime.now();
        var updatedAt = createdAt;

        var idBuilder = new StringBuilder(mnoId);
        idBuilder.append("api");
        String prefix = idBuilder.toString();

        var recordWithIdPrefix = mnoApiEndpointRepository.findByIdStartingWith(prefix);

        int counter = -1;
        for(var record : recordWithIdPrefix){
            var numberString = StringUtils.stripStart(StringUtils.strip(record.getId()),prefix);
            try{
                var number = Integer.valueOf(numberString);
                if(number.intValue() > counter){
                    counter = number.intValue();
                }
            } catch (NumberFormatException e) {
                //ignore purposely
            }
        }

        if(counter > -1){
            counter++;
        }else{
            counter = 1;
        }

        var id = idBuilder.append(counter).toString();

        var mnoApiEndpointEntity = new MnoApiEndpoint(id,
                mnoApiEndpointEntryReqDto.getUrl(),
                createdAt,
                updatedAt,
                mnoApiEndpointEntryReqDto.getName(),
                mnoApiEndpointEntryReqDto.getCachePeriod(),
                mnoId+":"+id);
        mnoApiEndpointEntity.setMnoId(mnoEntity.get());

        mnoApiEndpointEntity = mnoApiEndpointRepository.save(mnoApiEndpointEntity);
        mnoApiEndpointRepository.refresh(mnoApiEndpointEntity);

        var result = new MnoApiEndpointEntryDto();
        result.parseFrom(mnoApiEndpointEntity);

        return result;
    }

    public String getMsisdn(MnoApiEndpoint endpoint, HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        try {
            var incomingRequestURI = request.getRequestURI();
            URL url = new URL(endpoint.getUrl());
            String dbPath = url.getPath();
            ServicesLog.getInstance().logInfo("Extracting path variable " + Config.getApigateCustInfoOperatorEndpointPathvariablePatternMsisdn()
                    + " from incoming URI " + incomingRequestURI + " with pattern from db "+ dbPath);
            var stringMap = antPathMatcher.extractUriTemplateVariables(dbPath, incomingRequestURI);
            if (stringMap.size() > 0) {
                String msisdn = stringMap.get(Config.getApigateCustInfoOperatorEndpointPathvariablePatternMsisdn());
                return msisdn == null ? "" : msisdn;
            }
        } catch (Exception e) {
            ServicesLog.getInstance().logError(e);
        }
        return "";
    }

    public void clearCache(String id){
        var endpointDB = mnoApiEndpointRepository.findById(id);
        if(endpointDB.isPresent()){
            clearCache(endpointDB.get());
        }
    }

    public void clearCache(MnoApiEndpoint endpointDB) {
        removeAPIResponseCache(endpointDB);
    }

    public void createAPIResponseCache(MnoApiEndpoint apiEndpoint, String msisdn, String responseBody) throws IllegalArgumentException{
        cacheService.createCache(getAPIResponseCacheRedisKey(apiEndpoint, msisdn), responseBody, apiEndpoint.getCachePeriod());
    }

    public String getAPIResponseCache(MnoApiEndpoint apiEndpoint, String msisdn) throws IllegalArgumentException{
        return cacheService.getFromCache(getAPIResponseCacheRedisKey(apiEndpoint, msisdn));
    }

    private String getAPIResponseCacheRedisKey(MnoApiEndpoint apiEndpoint, String msisdn) throws IllegalArgumentException{
        if(StringUtils.isBlank(msisdn)){
            throw new IllegalArgumentException(Thread.currentThread().getStackTrace()[1].getMethodName() + " doesn't allow empty msisdn");
        }
        return apiEndpoint.getRedisKey()+":"+msisdn;
    }

    public void removeAPIResponseCache(MnoApiEndpoint apiEndpoint){
        String pattern = apiEndpoint.getRedisKey()+":*";
        ServicesLog.getInstance().logInfo("Looking for caches with key pattern " + pattern);
        var keys = cacheService.getKeys(pattern);
        ServicesLog.getInstance().logInfo("Found " + keys.size() + " caches to be removed");
        for (var key : keys){
            cacheService.removeCache(key);
        }
    }
}
