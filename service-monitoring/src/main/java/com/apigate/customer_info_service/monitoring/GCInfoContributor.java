package com.apigate.customer_info_service.monitoring;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

/**
 * @author Bayu Utomo
 * @date 17/3/2022 11:34 AM
 */
@Component
public class GCInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        var infos = new ArrayList<String>(ManagementFactory.getGarbageCollectorMXBeans().size());
        for(var bean : ManagementFactory.getGarbageCollectorMXBeans()){
            infos.add(bean.getName() + " | " + bean.getObjectName());
        }
        builder.withDetail("GarbageCollector",infos);
    }
}
