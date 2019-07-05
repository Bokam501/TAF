package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EntityRelationship;

public class JsonEntityRelationship {

	private static final Log log = LogFactory.getLog(JsonEntityRelationship.class);
	
	@JsonProperty
	private Integer entityRelationshipId;
	@JsonProperty
	private Integer entityTypeId1;
	@JsonProperty
	private Integer entityTypeId2;
	@JsonProperty
	private Integer entityInstanceId1;
	@JsonProperty
	private Integer entityInstanceId2;
	@JsonProperty
	private Integer isActive;
	@JsonProperty
	private String entityStatus;
	@JsonProperty
	private String entityTypeName1;
	@JsonProperty
	private String entityTypeName2;
	@JsonProperty
	private String entityInstanceName1;
	@JsonProperty
	private String entityInstanceName2;
	@JsonProperty
	private String relationshipType;
	
	public JsonEntityRelationship() {
		
	}
	
	public JsonEntityRelationship(EntityRelationship entityRelationship) {
		this.entityRelationshipId = entityRelationship.getId();
		this.entityTypeId1 = entityRelationship.getEntityTypeId1();
		this.entityTypeId2 = entityRelationship.getEntityTypeId2();
		this.entityInstanceId1 = entityRelationship.getEntityInstanceId1();
		this.entityInstanceId2 = entityRelationship.getEntityInstanceId2();
		this.isActive = entityRelationship.getIsActive();
		this.relationshipType = entityRelationship.getRelationshipType();
	}
	
	@JsonIgnore
	public EntityRelationship getEntityRelationship() {
		EntityRelationship entityRelationship = new EntityRelationship();
		entityRelationship.setId(this.entityRelationshipId);
		entityRelationship.setEntityTypeId1(this.entityTypeId1);
		entityRelationship.setEntityTypeId2(this.entityTypeId2);
		entityRelationship.setEntityInstanceId1(this.entityInstanceId1);
		entityRelationship.setEntityInstanceId2(this.entityInstanceId2);
		entityRelationship.setRelationshipType(this.relationshipType);
		return entityRelationship;
	}
	
	public Integer getEntityRelationshipId() {
		return entityRelationshipId;
	}
	public void setEntityRelationshipId(Integer entityRelationshipId) {
		this.entityRelationshipId = entityRelationshipId;
	}
	
	public Integer getEntityTypeId1() {
		return entityTypeId1;
	}
	public void setEntityTypeId1(Integer entityTypeId1) {
		this.entityTypeId1 = entityTypeId1;
	}
	
	public Integer getEntityTypeId2() {
		return entityTypeId2;
	}
	public void setEntityTypeId2(Integer entityTypeId2) {
		this.entityTypeId2 = entityTypeId2;
	}
	
	public Integer getEntityInstanceId1() {
		return entityInstanceId1;
	}
	public void setEntityInstanceId1(Integer entityInstanceId1) {
		this.entityInstanceId1 = entityInstanceId1;
	}
	
	public Integer getEntityInstanceId2() {
		return entityInstanceId2;
	}
	public void setEntityInstanceId2(Integer entityInstanceId2) {
		this.entityInstanceId2 = entityInstanceId2;
	}
	
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getEntityStatus() {
		return entityStatus;
	}
	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public String getEntityTypeName1() {
		return entityTypeName1;
	}
	public void setEntityTypeName1(String entityTypeName1) {
		this.entityTypeName1 = entityTypeName1;
	}

	public String getEntityTypeName2() {
		return entityTypeName2;
	}
	public void setEntityTypeName2(String entityTypeName2) {
		this.entityTypeName2 = entityTypeName2;
	}

	public String getEntityInstanceName1() {
		return entityInstanceName1;
	}
	public void setEntityInstanceName1(String entityInstanceName1) {
		this.entityInstanceName1 = entityInstanceName1;
	}

	public String getEntityInstanceName2() {
		return entityInstanceName2;
	}
	public void setEntityInstanceName2(String entityInstanceName2) {
		this.entityInstanceName2 = entityInstanceName2;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}	
	
}
