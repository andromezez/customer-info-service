package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.Client;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CustomRepository<Client, String> {

    List<Client> findByPartnerIdContainingIgnoreCase(String partnerId);

    List<Client> findByIdOrPartnerIdContainingIgnoreCase(String id, String partnerId);

    List<Client> findByIdNotAndPartnerIdContainingIgnoreCase(String id, String partnerId);
}