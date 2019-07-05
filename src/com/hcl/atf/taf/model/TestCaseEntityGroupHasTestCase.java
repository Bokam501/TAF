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
@Table(name="testcase_entitygroup_has_test_case_list")
public class TestCaseEntityGroupHasTestCase {
	
	private Integer id;
	private Integer testCaseEntityGroupId;
	private Integer testCaseId;
	private UserList createdBy;
	private UserList modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	
	
	@Column(name="testCaseEntityGroupId")
	public Integer getTestCaseEntityGroupId() {
		return testCaseEntityGroupId;
	}
	
	public void setTestCaseEntityGroupId(Integer testCaseEntityGroupId) {
		this.testCaseEntityGroupId = testCaseEntityGroupId;
	}
	
	
	@Column(name="testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}
	
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
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
	
	
	@Column(name="createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
