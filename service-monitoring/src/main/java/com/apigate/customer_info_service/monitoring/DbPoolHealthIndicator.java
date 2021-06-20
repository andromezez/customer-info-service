package com.apigate.customer_info_service.monitoring;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */

@Component
public class DbPoolHealthIndicator implements HealthIndicator {

    @Autowired
    private HikariDataSource dataSource;

    @Override
    public Health health() {
        Health.Builder status = Health.up();
        status.withDetail("maxPoolSize", dataSource.getMaximumPoolSize());
        status.withDetail("idleConnections", dataSource.getHikariPoolMXBean().getIdleConnections());
        status.withDetail("activeConnections", dataSource.getHikariPoolMXBean().getActiveConnections());
        status.withDetail("totalConnections", dataSource.getHikariPoolMXBean().getTotalConnections());
        status.withDetail("threadsAwaitingConnection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        return status.build();
    }

}
