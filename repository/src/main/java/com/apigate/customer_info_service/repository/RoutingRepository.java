package com.apigate.customer_info_service.repository;

import com.apigate.customer_info_service.entities.Routing;
import com.apigate.customer_info_service.entities.RoutingPK;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutingRepository extends CustomRepository<Routing, RoutingPK> {

    @Query("Select r from Routing r where r.routingPK.clientId = :clientId")
    List<Routing> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT r from Mno m " +
            "inner join m.mnoApiEndpointCollection mae " +
            "inner join mae.routingCollection r " +
            "where m.id = :mnoId")
    List<Routing> findByMnoId(@Param("mnoId") String mnoId);

    @Query("SELECT r from Mno m " +
            "inner join m.mnoApiEndpointCollection mae " +
            "inner join mae.routingCollection r " +
            "where m.id = :mnoId and r.routingPK.clientId = :clientId")
    List<Routing> findByClientIdAndMnoId(@Param("clientId") String clientId, @Param("mnoId") String mnoId);

    @Query("SELECT r from Routing r " +
            "inner join r.client c " +
            "where r.routingPK.mnoApiEndpointId = :endpointId and c.partnerId = :partnerId")
    List<Routing> findByEndpointIdAndClientPartnerId(@Param("endpointId") String endpointId, @Param("partnerId") String partnerId);

    @Query("Select r from Routing r where r.routingPK.mnoApiEndpointId = :mnoApiEndpointId")
    List<Routing> findByMnoApiEndpointId(@Param("mnoApiEndpointId") String mnoApiEndpointId);

    /*@Query("SELECT r from Mno m " +
            "inner join m.mnoApiEndpointCollection mae " +
            "inner join mae.routingCollection r " +
            "inner join r.client c " +
            "inner join r.maskingCollection mask " +
            "where r.routingPK.clientId = :clientId and r.routingPK.mnoApiEndpointId = :mnoApiEndpointId")
    Optional<Routing> findByIdForceLoadAllObjectGraph(String clientId, String mnoApiEndpointId);*/

    /*@Override
    @EntityGraph(value = "get-all-graph-for-single-routing", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Routing> findById(RoutingPK routingPK);*/
    //List<Routing> findByIdForceLoadAllObjectGraph(String clientId, String mnoApiEndpointId);
}