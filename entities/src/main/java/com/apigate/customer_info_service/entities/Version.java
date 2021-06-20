package com.apigate.customer_info_service.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Data
@Entity
@Table(name = "version")
public class Version implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="version")
	private String versionId;
	@Column(name ="deployed_at")
	private ZonedDateTime deployedAT;
	
	
}
