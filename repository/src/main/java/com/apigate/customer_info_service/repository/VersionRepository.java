package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.Version;
import org.springframework.stereotype.Repository;

/**
 * @author Bayu Utomo
 * @date 17/6/2021 9:14 AM
 */
@Repository
public interface VersionRepository extends CustomRepository<Version, String>{

}
