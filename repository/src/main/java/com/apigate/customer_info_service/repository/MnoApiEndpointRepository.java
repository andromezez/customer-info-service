package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.MnoApiEndpoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MnoApiEndpointRepository extends CustomRepository<MnoApiEndpoint, String>{

    @Query("Select mae from MnoApiEndpoint mae where mae.mnoId.id = :mnoId")
    List<MnoApiEndpoint> findByMnoId(@Param("mnoId") String mnoId);

    List<MnoApiEndpoint> findByIdStartingWith(String idPrefix);
}