/*
package com.apigate.customer_info_service.monitoring;

import com.apigate.utils.ObjectMapperUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

*/
/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 *//*

@Component
public class JsonMapperPoolHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        var status = Health.up();
        status.withDetail("maxPoolSize", ObjectMapperUtils.getMaxPoolSize());
        status.withDetail("idleObjects", ObjectMapperUtils.getNumIdle());
        status.withDetail("activeObjects", ObjectMapperUtils.getNumActive());
        status.withDetail("totalManagedObjects", ObjectMapperUtils.getNumIdle() + ObjectMapperUtils.getNumActive());
        return status.build();
    }

}
*/
