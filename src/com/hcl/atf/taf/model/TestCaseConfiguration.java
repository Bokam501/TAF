package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "testcase_configuration")
public class TestCaseConfiguration implements java.io.Serializable {
	
	private Integer testCaseConfigurationId;
	private Integer device_combination_id;
    private EnvironmentCombination environmentCombination;
	private WorkPackageTestCaseExecutionPlan workpackage_run_list;
	private WorkpackageRunConfiguration workpackageRunConfiguration;
	public TestCaseConfiguration () {
}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testCaseConfigurationId", unique = true, nullable = false)
	public Integer getTestCaseConfigurationId() {
		return testCaseConfigurationId;
	}
	public void setTestCaseConfigurationId(Integer testCaseConfigurationId) {
		this.testCaseConfigurationId = testCaseConfigurationId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "environment_combination_id")
	public EnvironmentCombination getEnvironmentCombination() {
		return environmentCombination;
	}
	public void setEnvironmentCombination(EnvironmentCombination environmentCombination) {
		this.environmentCombination = environmentCombination;
	}
	@Column(name = "device_combination_id")
	public Integer getDevice_combination_id() {
		return device_combination_id;
	}
	public void setDevice_combination_id(Integer device_combination_id) {
		this.device_combination_id = device_combination_id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wptcepId")
	public WorkPackageTestCaseExecutionPlan getWorkpackage_run_list() {
		return workpackage_run_list;
	}
	public void setWorkpackage_run_list(
			WorkPackageTestCaseExecutionPlan workpackage_run_list) {
		this.workpackage_run_list = workpackage_run_list;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workpackageRunConfigurationId")
	public  WorkpackageRunConfiguration getWorkpackageRunConfiguration() {
		return workpackageRunConfiguration;
	}
	public  void setWorkpackageRunConfiguration(
			WorkpackageRunConfiguration workpackageRunConfiguration) {
		this.workpackageRunConfiguration = workpackageRunConfiguration;
	}
	
	
}
