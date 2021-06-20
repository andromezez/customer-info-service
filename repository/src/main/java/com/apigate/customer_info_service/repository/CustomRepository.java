package com.apigate.customer_info_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Bayu Utomo
 * @date 15/6/2021 3:00 PM
 */
@NoRepositoryBean
public interface CustomRepository<T, ID>
        extends JpaRepository<T, ID> {
    void refresh(T t);
}
