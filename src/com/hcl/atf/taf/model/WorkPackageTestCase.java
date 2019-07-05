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
@Table(name = "work_package_testcases")
public class WorkPackageTestCase  implements java.io.Serializable{
	
	private Integer id;
	
	private UserList user;
	@Column(name="createdDate")
	private Date createdDate;
	@Column(name="editedDate")
	private Date editedDate;
	@Column(name="status")
	private String status;
	@Column(name="isSelected")
	private Integer isSelected;

	private WorkPackage workPackage;
	private TestCaseList testCase;
	public WorkPackageTestCase  () {
}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId") 
	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testcaseId") 
	public TestCaseList getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCaseList testCase) {
		this.testCase = testCase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") 
	public UserList getUser() {
		return user;
	}

	public void setUser(UserList user) {
		this.user = user;
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
	@Column(name = "editedDate")
	public Date getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
}
