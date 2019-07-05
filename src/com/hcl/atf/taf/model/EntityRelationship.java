package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "entity_relationship")
public class EntityRelationship {

private Integer Id;
private Integer entityTypeId1;
private Integer entityTypeId2;
private Integer entityInstanceId1;
private Integer entityInstanceId2;
private Integer isActive;
private String relationshipType;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public Integer getId() {
	return Id;
}
public void setId(Integer id) {
	Id = id;
}

@Column(name = "entityTypeId1")
public Integer getEntityTypeId1() {
	return entityTypeId1;
}
public void setEntityTypeId1(Integer entityTypeId1) {
	this.entityTypeId1 = entityTypeId1;
}

@Column(name = "entityTypeId2")
public Integer getEntityTypeId2() {
	return entityTypeId2;
}
public void setEntityTypeId2(Integer entityTypeId2) {
	this.entityTypeId2 = entityTypeId2;
}

@Column(name = "entityInstanceId1")
public Integer getEntityInstanceId1() {
	return entityInstanceId1;
}
public void setEntityInstanceId1(Integer entityInstanceId1) {
	this.entityInstanceId1 = entityInstanceId1;
}

@Column(name = "entityInstanceId2")
public Integer getEntityInstanceId2() {
	return entityInstanceId2;
}
public void setEntityInstanceId2(Integer entityInstanceId2) {
	this.entityInstanceId2 = entityInstanceId2;
}

@Column(name = "isActive")
public Integer getIsActive() {
	return isActive;
}
public void setIsActive(Integer isActive) {
	this.isActive = isActive;
}

@Column(name = "relationshipType")
public String getRelationshipType() {
	return relationshipType;
}
public void setRelationshipType(String relationshipType) {
	this.relationshipType = relationshipType;
}

}
