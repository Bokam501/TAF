package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

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
@Table(name = "workpackage_has_runconfiguration")
public class WorkpackageRunConfiguration {


	private WorkPackage workpackage;
	private RunConfiguration runconfiguration;
	private Integer workpackageRunConfigurationId;
	private String type;
	private Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = new HashSet<WorkPackageTestCaseExecutionPlan>(0);
	private Set<TestCaseConfiguration> testCaseConfigSet=new HashSet<TestCaseConfiguration>(0);
	public WorkpackageRunConfiguration() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "WorkpackageRunConfigurationId", unique = true, nullable = false)
	public Integer getWorkpackageRunConfigurationId() {
		return workpackageRunConfigurationId;
	}
	public void setWorkpackageRunConfigurationId(
			Integer workpackageRunConfigurationId) {
		this.workpackageRunConfigurationId = workpackageRunConfigurationId;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workpackageId", nullable = false)
	public WorkPackage getWorkpackage() {
		return workpackage;
	}
	public void setWorkpackage(WorkPackage workpackage) {
		this.workpackage = workpackage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "runconfigurationId", nullable = false)
	public RunConfiguration getRunconfiguration() {
		return runconfiguration;
	}
	public void setRunconfiguration(RunConfiguration runconfiguration) {
		this.runconfiguration = runconfiguration;
	}
	@Column(name = "type", length = 200)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "runConfiguration",cascade=CascadeType.ALL)
	public Set<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlan() {
		return workPackageTestCaseExecutionPlan;
	}

	public void setWorkPackageTestCaseExecutionPlan(
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan) {
		this.workPackageTestCaseExecutionPlan = workPackageTestCaseExecutionPlan;
	}

	@Override
	public boolean equals(Object o) {
		WorkpackageRunConfiguration wpRunconfig = (WorkpackageRunConfiguration) o;
		if (this.workpackageRunConfigurationId == wpRunconfig.getWorkpackageRunConfigurationId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) workpackageRunConfigurationId;
	  }

}
