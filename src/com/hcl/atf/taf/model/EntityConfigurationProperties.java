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
@Table(name = "entity_configuration_properties")
public class EntityConfigurationProperties {

	private Integer entityConfigPropertyId;
	private String value;
	private String property;
	private Integer status;
	private Date createdDate;
	private Date modifiedDate;
	private Integer entityId;
	private EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entityConfigPropertyId", unique = true, nullable = false)
	public Integer getEntityConfigPropertyId() {
		return entityConfigPropertyId;
	}

	public void setEntityConfigPropertyId(Integer entityConfigPropertyId) {
		this.entityConfigPropertyId = entityConfigPropertyId;
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	@Column(name = "entityId")
	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Column(name = "property")
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityConfigPropertiesMasterId")
	public EntityConfigurationPropertiesMaster getEntityConfigurationPropertiesMaster() {
		return entityConfigurationPropertiesMaster;
	}

	public void setEntityConfigurationPropertiesMaster(
			EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster) {
		this.entityConfigurationPropertiesMaster = entityConfigurationPropertiesMaster;
	}

}
