package com.apigate.customer_info_service.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BayuUtomo
 */
@Embeddable
public class RoutingPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "client_id", table = "routing", nullable = false)
    private String clientId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "mno_api_endpoint_id", table = "routing", nullable = false)
    private String mnoApiEndpointId;

    public RoutingPK() {
    }

    public RoutingPK(String clientId, String mnoApiEndpointId) {
        this.clientId = clientId;
        this.mnoApiEndpointId = mnoApiEndpointId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMnoApiEndpointId() {
        return mnoApiEndpointId;
    }

    public void setMnoApiEndpointId(String mnoApiEndpointId) {
        this.mnoApiEndpointId = mnoApiEndpointId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += Objects.hashCode(clientId);
        hash += Objects.hashCode(mnoApiEndpointId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return false;
        }

        if(!(obj instanceof RoutingPK other)){
            return false;
        }

        if (!Objects.equals(this.getClientId(), other.getClientId())) {
            return false;
        }
        if (!Objects.equals(this.getMnoApiEndpointId(), other.getMnoApiEndpointId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.RoutingPK[ clientId=" + clientId + ", mnoApiEndpointId=" + mnoApiEndpointId + " ]";
    }

}