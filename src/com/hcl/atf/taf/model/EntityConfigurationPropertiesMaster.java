package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "entity_configuration_properties_master")
public class EntityConfigurationPropertiesMaster {
	
	private Integer entityConfigPropertiesMasterId;	
	private String name;
	private String description;
	private String type;
	private String options;
	private Integer status;
	private Date createdDate;
	private Date modifiedDate;
	private EntityMaster entityMaster;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entityConfigPropertiesMasterId", unique = true, nullable = false)
	public Integer getEntityConfigPropertiesMasterId() {
		return entityConfigPropertiesMasterId;
	}
	public void setEntityConfigPropertiesMasterId(
			Integer entityConfigPropertiesMasterId) {
		this.entityConfigPropertiesMasterId = entityConfigPropertiesMasterId;
	}
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "options")
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityMasterId")
	public EntityMaster getEntityMaster() {
		return entityMaster;
	}
	public void setEntityMaster(EntityMaster entityMaster) {
		this.entityMaster = entityMaster;
	}
	
	
	
}
