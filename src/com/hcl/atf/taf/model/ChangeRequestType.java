package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "change_request_type")
public class ChangeRequestType {
private Integer changeRequestTypeId;
private String changeRequestTypeName;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "crtypeId",unique = true, nullable = false)
public Integer getChangeRequestTypeId() {
	return changeRequestTypeId;
}
public void setChangeRequestTypeId(Integer changeRequestTypeId) {
	this.changeRequestTypeId = changeRequestTypeId;
}

@Column(name = "crtypeName")
public String getChangeRequestTypeName() {
	return changeRequestTypeName;
}
public void setChangeRequestTypeName(String changeRequestTypeName) {
	this.changeRequestTypeName = changeRequestTypeName;
}
}
