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
@Table(name = "mno", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries({
        @NamedQuery(name = "Mno.findAll", query = "SELECT m FROM Mno m")})
public class Mno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "id", table = "mno", nullable = false)
    private String id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name", table = "mno", nullable = false, length = 50)
    private String name;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at", table = "mno", nullable = false)
    private ZonedDateTime createdAt;

    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at", table = "mno", nullable = false)
    private ZonedDateTime updatedAt;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username", table = "mno", nullable = false, length = 50)
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "password", table = "mno", nullable = false, length = 50)
    private String password;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "auth_key", table = "mno", nullable = false, length = 2000)
    private String authKey;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "token_url", table = "mno", nullable = false, length = 2000)
    private String tokenUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mnoId")
    private List<MnoApiEndpoint> mnoApiEndpointCollection;

    public Mno() {
    }

    public Mno(String id) {
        this.id = id;
    }

    public Mno(String id, String name, ZonedDateTime createdAt, ZonedDateTime updatedAt, String username, String password, String authKey, String tokenUrl) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.password = password;
        this.authKey = authKey;
        this.tokenUrl = tokenUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public List<MnoApiEndpoint> getMnoApiEndpointCollection() {
        return mnoApiEndpointCollection;
    }

    public void setMnoApiEndpointCollection(List<MnoApiEndpoint> mnoApiEndpointCollection) {
        this.mnoApiEndpointCollection = mnoApiEndpointCollection;
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
        if (!(object instanceof Mno)) {
            return false;
        }
        Mno other = (Mno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apigate.customer_info_service.entities.Mno[ id=" + id + " ]";
    }

}