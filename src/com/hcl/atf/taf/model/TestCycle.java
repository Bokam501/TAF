package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "test_cycle")
public class TestCycle implements Serializable{

	private Integer testCycleId;
	
	private TestRunPlanGroup testRunPlanGroup;
	
	private Integer status;
	
	private String result;
	
	private Date startTime;
	
	private Date endTime;
	
	private UserList userList;
	
	private String testCycleStatus;
	
	private Set<WorkPackage> workPackageList = new HashSet<WorkPackage>(0);
	public TestCycle(){
		
	}
	
	@Id
	@Column(name = "test_cycle_id")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getTestCycleId() {
		return testCycleId;
	}
	public void setTestCycleId(Integer testCycleId) {
		this.testCycleId = testCycleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_run_plan_group_id")
	public TestRunPlanGroup getTestRunPlanGroup() {
		return testRunPlanGroup;
	}
	public void setTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup) {
		this.testRunPlanGroup = testRunPlanGroup;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "result")
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	
	@Column(name = "start_date")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_date")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by") 
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testCycle",cascade=CascadeType.ALL)
	public Set<WorkPackage> getWorkPackageList() {
		return workPackageList;
	}
	public void setWorkPackageList(Set<WorkPackage> workPackageList) {
		this.workPackageList = workPackageList;
	}

	@Column(name = "test_cycle_status")
	public String getTestCycleStatus() {
		return testCycleStatus;
	}
	public void setTestCycleStatus(String testCycleStatus) {
		this.testCycleStatus = testCycleStatus;
	}
}