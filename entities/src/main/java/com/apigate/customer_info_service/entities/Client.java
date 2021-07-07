package com.apigate.customer_info_service.entities;

import java.io.Serializable;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BayuUtomo
 */
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(columnNames = "partner_id"))
@NamedQueries({
        @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "id", table = "client", nullable = false)
    private String id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at", table = "client", nullable = false)
    private ZonedDateTime createdAt;

    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at", table = "client", nullable = false)
    private ZonedDateTime updatedAt;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cache_active", table = "client", nullable = false)
    private boolean cacheActive;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "partner_id", table = "client", nullable = false, length = 50)
    private String partnerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Routing> routingCollection;

    public Client() {
    }

    public Client(String id) {
        this.id = id;
    }

    public Client(String id, ZonedDateTime createdAt, ZonedDateTime updatedAt, boolean cacheActive, String partnerId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cacheActive = cacheActive;
        this.partnerId = partnerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isCacheActive() {
        return cacheActive;
    }

    public void setCacheActive(boolean cacheActive) {
        this.cacheActive = cacheActive;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public List<Routing> getRoutingCollection() {
        return routingCollection;
    }

    public void setRoutingCollection(List<Routing> routingCollection) {
        this.routingCollection = routingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.Client[ id=" + id + " ]";
    }

}