package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MnoApiEndpointRepository extends CustomRepository<MnoApiEndpoint, String>{

}