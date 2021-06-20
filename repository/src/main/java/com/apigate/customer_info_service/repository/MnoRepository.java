package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.Mno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MnoRepository extends CustomRepository<Mno, String> {
    List<Mno> findByNameContainingIgnoreCase(String name);
}