package com.apigate.customer_info_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * @author Bayu Utomo
 * @date 20/5/2021 10:05 AM
 */
@Controller("application_config_reader")
public class Config {
    @Value("${apigate.instance.id}")
    public void setInstanceId(String id){
        if (com.apigate.config.Config.getInstanceId() == null || com.apigate.config.Config.getInstanceId().isEmpty()) {
            com.apigate.config.Config.setInstanceId(id);
        }
    }

    @Value("${apigate.server.threads.max}")
    public void setApigateServerThreadsMax(int max){
        com.apigate.config.Config.setApigateServerThreadsMax(max);
    }

    @Value("${apigate.server.monitoring.threads.max}")
    public void setHealthCheckMaxThread(int value) {
        if (com.apigate.config.Config.getServerMonitoringMaxThread() == 0) {
            com.apigate.config.Config.setServerMonitoringMaxThread(value);
        }
    }

    @Value("${apigate.server.monitoring.port}")
    public void setHealthCheckPort(int value) {
        if (com.apigate.config.Config.getServerMonitoringPort() == 0) {
            com.apigate.config.Config.setServerMonitoringPort(value);
        }
    }
}
