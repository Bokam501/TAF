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
@Table(name = "test_factory_manager")
public class TestFactoryManager {

	
	private Integer testFactoryManagerId;
	@Column(name = "status")
	private Integer status;
	
    private Date createdDate;
	private UserList userList;
	private TestFactory testFactory;
	
	@Id
	@Column(name = "testFactoryManagerId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getTestFactoryManagerId() {
		return testFactoryManagerId;
	}
	public void setTestFactoryManagerId(Integer testFactoryManagerId) {
		this.testFactoryManagerId = testFactoryManagerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") 
	public UserList getUserList() {
		return userList;
	}
	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId") 
	public TestFactory getTestFactory() {
		return testFactory;
	}
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	
	
}
