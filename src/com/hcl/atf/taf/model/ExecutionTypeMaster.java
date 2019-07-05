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
@Table(name = "execution_type_master")
public class ExecutionTypeMaster implements java.io.Serializable{

	private Integer executionTypeId;	
	private String name;
	private String description ;
	private EntityMaster entitymaster;
	public ExecutionTypeMaster () {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "executionTypeId", unique = true)
	public Integer getExecutionTypeId() {
		return executionTypeId;
	}
	public void setExecutionTypeId(Integer executionTypeId) {
		this.executionTypeId = executionTypeId;
	}
	
	@Column(name = "name", length = 1000)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entitymasterId")
	public EntityMaster getEntitymaster() {
		return entitymaster;
	}
	public void setEntitymaster(EntityMaster entitymaster) {
		this.entitymaster = entitymaster;
	}
	
}
