package com.apigate.customer_info_service.service;

import com.apigate.config.Config;
import com.apigate.customer_info_service.dto.httprequestbody.operator_endpoint.MnoApiEndpointEntryReqDto;
import com.apigate.customer_info_service.dto.httpresponsebody.operator_endpoint.MnoApiEndpointEntryDto;
import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import com.apigate.customer_info_service.repository.MnoApiEndpointRepository;
import com.apigate.exceptions.db.RecordNotFoundException;
import com.apigate.logging.ServicesLog;
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

}
