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
public class MaskingPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "client_id", table = "masking", nullable = false)
    private String clientId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "mno_api_endpoint_id", table = "masking", nullable = false)
    private String mnoApiEndpointId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "json_path", nullable = false, length = 2000)
    private String jsonPath;

    public MaskingPK() {
    }

    public MaskingPK(String clientId, String mnoApiEndpointId, String jsonPath) {
        this.clientId = clientId;
        this.mnoApiEndpointId = mnoApiEndpointId;
        this.jsonPath = jsonPath;
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

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += Objects.hashCode(clientId);
        hash += Objects.hashCode(mnoApiEndpointId);
        hash += Objects.hashCode(jsonPath);
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

        if(!(obj instanceof MaskingPK other)){
            return false;
        }

        if (!Objects.equals(this.getClientId(), other.getClientId())) {
            return false;
        }
        if (!Objects.equals(this.getMnoApiEndpointId(), other.getMnoApiEndpointId())) {
            return false;
        }
        if (!Objects.equals(this.getJsonPath(), other.getJsonPath())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.MaskingPK[ clientId=" + clientId + ", mnoApiEndpointId=" + mnoApiEndpointId + ", jsonPath=" + jsonPath + " ]";
    }

}