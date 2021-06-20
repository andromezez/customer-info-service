package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.Masking;
import com.apigate.customer_info_service.entities.MaskingPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaskingRepository extends CustomRepository<Masking, MaskingPK> {
    @Query("SELECT m from Masking m " +
            "where m.maskingPK.clientId = :clientId and m.maskingPK.mnoApiEndpointId = :mnoApiEndpointId")
    List<Masking> findByPartialId(String clientId, String mnoApiEndpointId);

    @Query("SELECT m from Masking m " +
            "where m.maskingPK.clientId = :clientId")
    List<Masking> findByClientIdOnly(String clientId);

    @Query("SELECT m from Masking m " +
            "where m.maskingPK.mnoApiEndpointId = :mnoApiEndpointId")
    List<Masking> findByEndpointIdOnly(String mnoApiEndpointId);
}