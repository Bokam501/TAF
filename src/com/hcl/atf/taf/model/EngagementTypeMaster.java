package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "engagement_type_master")
public class EngagementTypeMaster implements java.io.Serializable {
	private Integer engagementTypeId;	
	private String engagementTypeName;
	private String description;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "engagementTypeId", unique = true, nullable = false)
	public Integer getEngagementTypeId() {
		return engagementTypeId;
	}
	public void setEngagementTypeId(Integer engagementTypeId) {
		this.engagementTypeId = engagementTypeId;
	}
	
	@Column(name = "engagementTypeName", length = 1000)
	public String getEngagementTypeName() {
		return engagementTypeName;
	}
	public void setEngagementTypeName(String engagementTypeName) {
		this.engagementTypeName = engagementTypeName;
	}
	
	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
