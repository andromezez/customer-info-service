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
/*@NamedEntityGraph(
        name = "get-all-graph-for-single-routing",
        attributeNodes = {
                @NamedAttributeNode("client"),
                @NamedAttributeNode("mnoApiEndpoint"),
                @NamedAttributeNode("maskingCollection")
        }
)*/
@Entity
@Table(name = "routing")
@NamedQueries({
    @NamedQuery(name = "Routing.findAll", query = "SELECT r FROM Routing r")})
public class Routing implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected RoutingPK routingPK;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cache_active", table = "routing", nullable = false)
    private boolean cacheActive;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cache_period", table = "routing", nullable = false)
    private int cachePeriod;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at", table = "routing", nullable = false)
    private ZonedDateTime createdAt;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "redis_key", table = "routing", nullable = false)
    private String redisKey;

    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at", table = "routing", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mno_api_endpoint_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MnoApiEndpoint mnoApiEndpoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "routing")
    private List<Masking> maskingCollection;

    public Routing() {
    }

    public Routing(RoutingPK routingPK) {
        this.routingPK = routingPK;
    }

    public Routing(RoutingPK routingPK, boolean cacheActive, int cachePeriod, ZonedDateTime createdAt, String redisKey, ZonedDateTime updatedAt) {
        this.routingPK = routingPK;
        this.cacheActive = cacheActive;
        this.cachePeriod = cachePeriod;
        this.createdAt = createdAt;
        this.redisKey = redisKey;
        this.updatedAt = updatedAt;
    }

    public Routing(String clientId, String mnoApiEndpointId) {
        this.routingPK = new RoutingPK(clientId, mnoApiEndpointId);
    }

    public RoutingPK getRoutingPK() {
        return routingPK;
    }

    public void setRoutingPK(RoutingPK routingPK) {
        this.routingPK = routingPK;
    }

    public boolean isCacheActive() {
        return cacheActive;
    }

    public void setCacheActive(boolean cacheActive) {
        this.cacheActive = cacheActive;
    }

    public int getCachePeriod() {
        return cachePeriod;
    }

    public void setCachePeriod(int cachePeriod) {
        this.cachePeriod = cachePeriod;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public MnoApiEndpoint getMnoApiEndpoint() {
        return mnoApiEndpoint;
    }

    public void setMnoApiEndpoint(MnoApiEndpoint mnoApiEndpoint) {
        this.mnoApiEndpoint = mnoApiEndpoint;
    }

    public List<Masking> getMaskingCollection() {
        return maskingCollection;
    }

    public void setMaskingCollection(List<Masking> maskingCollection) {
        this.maskingCollection = maskingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routingPK != null ? routingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Routing other)) {
            return false;
        }
        if ((this.routingPK == null && other.routingPK != null) || (this.routingPK != null && !this.routingPK.equals(other.routingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.Routing[ routingPK=" + routingPK + " ]";
    }

}