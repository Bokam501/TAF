package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "platformtype")
public class PlatformType {

	private Integer platformId;
	private String name;
	private String version;
	private EntityMaster entityMaster;
	
public PlatformType() {
}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "platformId", unique = true, nullable = false)
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	@Column(name = "name", length = 45)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "version", length = 45)
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
