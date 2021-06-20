package com.apigate.customer_info_service.monitoring;

import com.apigate.config.Config;
import com.apigate.customer_info_service.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */

@Component
public class DbInfoContributor implements InfoContributor {
    @Autowired
    private VersionRepository versionRepository;

    @Override
    public void contribute(Info.Builder builder) {
        var versions = versionRepository.findAll(Sort.by(Sort.Direction.DESC, "deployedAT"));
        var dbInfo = new HashMap<String,String>();
        dbInfo.put("schemaVersion", versions.get(0).getVersionId());
        dbInfo.put("deployedAt", versions.get(0).getDeployedAT().format(Config.DATE_TIME_FORMATTER));
        builder.withDetail("database",dbInfo);
    }
}
