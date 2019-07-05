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

@Entity
@Table(name = "entity_relationship_common")
public class EntityRelationshipCommon {

	private Integer Id;
	private Integer sourceEntityTypeId;
	private Integer sourceEntityInstanceId;
	private Integer targetEntityTypeId;
	private Integer targetEntityInstanceId;
	private String relationshipType;
	private String relationshipSubType;

	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isActive;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}

	@Column(name = "sourceEntityTypeId")
	public Integer getSourceEntityTypeId() {
		return sourceEntityTypeId;
	}

	public void setSourceEntityTypeId(Integer sourceEntityTypeId) {
		this.sourceEntityTypeId = sourceEntityTypeId;
	}

	@Column(name = "sourceEntityInstanceId")
	public Integer getSourceEntityInstanceId() {
		return sourceEntityInstanceId;
	}

	public void setSourceEntityInstanceId(Integer sourceEntityInstanceId) {
		this.sourceEntityInstanceId = sourceEntityInstanceId;
	}

	@Column(name = "targetEntityTypeId")
	public Integer getTargetEntityTypeId() {
		return targetEntityTypeId;
	}

	public void setTargetEntityTypeId(Integer targetEntityTypeId) {
		this.targetEntityTypeId = targetEntityTypeId;
	}

	@Column(name = "targetEntityInstanceId")
	public Integer getTargetEntityInstanceId() {
		return targetEntityInstanceId;
	}

	public void setTargetEntityInstanceId(Integer targetEntityInstanceId) {
		this.targetEntityInstanceId = targetEntityInstanceId;
	}

	@Column(name = "relationshipType")
	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	@Column(name = "relationshipSubType")
	public String getRelationshipSubType() {
		return relationshipSubType;
	}

	public void setRelationshipSubType(String relationshipSubType) {
		this.relationshipSubType = relationshipSubType;
	}

	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
}
