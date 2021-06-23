/*
package com.apigate.customer_info_service.monitoring;

import com.apigate.blacklist_service.logging.CommonLog;
import com.apigate.blacklist_service.service.PartnerTypeHTTPResponse;
import com.apigate.config.CommonHeaders;
import com.apigate.config.Config;
import com.apigate.utils.json_processor.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

*/
/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 *//*

@Component
public class ExternalPartnerServiceHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        Health.Builder status = Health.up();
        short goodCheckLevelPassed=0;
        var restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        var descriptionParam = "description";
        var upProblematicReason = "connected to external partner service with response ";
        var problemToConnectReason = "Problem to connect";
        var goodConnectionReason = new StringBuilder();
        try {
            var headers = new HttpHeaders();
            headers.add(CommonHeaders.CORRELATION_ID.getHeaderName(), "health-check");
            var httpEntity = new HttpEntity<String>( headers);
            response = restTemplate.exchange(Config.getPartnerTypeUrl(), HttpMethod.GET, httpEntity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                goodConnectionReason.append("Response code 200 OK. ");
                goodCheckLevelPassed++;
            }else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                status = Health.status(AdditionalStatus.WARNING.name());
                status.withDetail(descriptionParam, upProblematicReason + HttpStatus.BAD_REQUEST);
            }else if(response.getStatusCode() == HttpStatus.NOT_FOUND){
                status = Health.status(AdditionalStatus.WARNING.name());
                status.withDetail(descriptionParam, upProblematicReason + HttpStatus.NOT_FOUND);
            }else {
                status = Health.unknown();
                status.withDetail(descriptionParam, "connected to external partner service with unknown reason");
            }
        }catch (RestClientException e){
            if(e instanceof HttpClientErrorException   ){
                if((((HttpClientErrorException)e).getStatusCode() == HttpStatus.BAD_REQUEST)){
                    status = Health.status(AdditionalStatus.WARNING.name());
                    status.withDetail(descriptionParam, upProblematicReason + HttpStatus.BAD_REQUEST);
                }else if((((HttpClientErrorException)e).getStatusCode() == HttpStatus.NOT_FOUND) ){
                    status = Health.status(AdditionalStatus.WARNING.name());
                    status.withDetail(descriptionParam, upProblematicReason + HttpStatus.NOT_FOUND);
                }else{
                    CommonLog.getInstance().logError(e);
                    status = Health.down();
                    status.withDetail(descriptionParam, problemToConnectReason);
                }
            }else{
                CommonLog.getInstance().logError(e);
                status = Health.down();
                status.withDetail(descriptionParam, problemToConnectReason);
            }
        }catch (Exception e){
            CommonLog.getInstance().logError(e);
            status = Health.down();
            status.withDetail(descriptionParam, problemToConnectReason);
        }
        if (goodCheckLevelPassed > 0){
            var mapper = ObjectMapperUtils.getMapperInstance();
            try {
                var externalPartnerType = mapper.readValue(response.getBody(), PartnerTypeHTTPResponse.class);
                if(externalPartnerType.getType() != null){
                    goodConnectionReason.append("Data structure is valid. ");
                    goodCheckLevelPassed++;
                }else{
                    goodConnectionReason.append("Data structure is invalid. ");
                }
            } catch (JsonProcessingException e) {
                goodConnectionReason.append("Data structure is invalid. ");
            }finally {
                ObjectMapperUtils.returnToPool(mapper);
            }

            if(goodCheckLevelPassed>1){
                goodConnectionReason.append("connection to external partner service is good.");
            }else{
                goodConnectionReason.append("connection to external partner service is half good.");
                status = Health.status(AdditionalStatus.WARNING.name());
            }
            status.withDetail(descriptionParam, goodConnectionReason);
        }
        status.withDetail("apigate.partnertype.url",Config.getPartnerTypeUrl());
        return status.build();
    }
}
*/
