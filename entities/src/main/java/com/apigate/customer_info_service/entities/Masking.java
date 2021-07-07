package com.apigate.customer_info_service.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BayuUtomo
 */
@Entity
@Table(name = "masking")
@NamedQueries({
        @NamedQuery(name = "Masking.findAll", query = "SELECT m FROM Masking m")})
public class Masking implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected MaskingPK maskingPK;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "mask")
    private String mask;

    @Basic(optional = false)
    @NotNull
    @Column(name = "active", table = "masking", nullable = false)
    private boolean active;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at", table = "masking", nullable = false)
    private ZonedDateTime createdAt;

    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at", table = "masking", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "client_id", referencedColumnName = "client_id", insertable = false, updatable = false),
        @JoinColumn(name = "mno_api_endpoint_id", referencedColumnName = "mno_api_endpoint_id", insertable = false, updatable = false)})
    private Routing routing;

    public Masking() {
    }

    public Masking(MaskingPK maskingPK) {
        this.maskingPK = maskingPK;
    }

    public Masking(MaskingPK maskingPK, String mask, boolean active, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.maskingPK = maskingPK;
        this.mask = mask;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Masking(String clientId, String mnoApiEndpointId, String jsonPath) {
        this.maskingPK = new MaskingPK(clientId, mnoApiEndpointId, jsonPath);
    }

    public MaskingPK getMaskingPK() {
        return maskingPK;
    }

    public void setMaskingPK(MaskingPK maskingPK) {
        this.maskingPK = maskingPK;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maskingPK != null ? maskingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Masking)) {
            return false;
        }
        Masking other = (Masking) object;
        if ((this.maskingPK == null && other.maskingPK != null) || (this.maskingPK != null && !this.maskingPK.equals(other.maskingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.Masking[ maskingPK=" + maskingPK + " ]";
    }

}