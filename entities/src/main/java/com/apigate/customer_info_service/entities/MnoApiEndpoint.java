package com.apigate.customer_info_service.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 * @author BayuUtomo
 */
@Entity
@Table(name = "mno_api_endpoint", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries({
    @NamedQuery(name = "MnoApiEndpoint.findAll", query = "SELECT m FROM MnoApiEndpoint m")})
public class MnoApiEndpoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "id", table = "mno_api_endpoint", nullable = false)
    private String id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "url", table = "mno_api_endpoint", nullable = false, length = 2000)
    private String url;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at", table = "mno_api_endpoint", nullable = false)
    private ZonedDateTime createdAt;

    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at", table = "mno_api_endpoint", nullable = false)
    private ZonedDateTime updatedAt;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name", table = "mno_api_endpoint", nullable = false, length = 50)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mno_id", referencedColumnName = "id")
    private Mno mnoId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mnoApiEndpoint")
    private List<Routing> routingCollection;

    public MnoApiEndpoint() {
    }

    public MnoApiEndpoint(String id) {
        this.id = id;
    }

    public MnoApiEndpoint(String id, String url, ZonedDateTime createdAt, ZonedDateTime updatedAt, String name) {
        this.id = id;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Routing> getRoutingCollection() {
        return routingCollection;
    }

    public void setRoutingCollection(List<Routing> routingCollection) {
        this.routingCollection = routingCollection;
    }

    public Mno getMnoId() {
        return mnoId;
    }

    public void setMnoId(Mno mnoId) {
        this.mnoId = mnoId;
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
        if (!(object instanceof MnoApiEndpoint other)) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.MnoApiEndpoint[ id=" + id + " ]";
    }

}