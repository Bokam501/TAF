package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "activity_entity_master")
public class ActivityEntityMaster {

private Integer entityId;
private String entityName;
private String description;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "activityentityId", unique = true, nullable = false)
public Integer getEntityId() {
	return entityId;
}
public void setEntityId(Integer entityId) {
	this.entityId = entityId;
}
@Column(name = "entityName")
public String getEntityName() {
	return entityName;
}
public void setEntityName(String entityName) {
	this.entityName = entityName;
}
@Column(name = "description")
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}



}
