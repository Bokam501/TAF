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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "environment_combination")
public class EnvironmentCombination {
private Integer	environment_combination_id;
private String environmentCombinationName;
private ProductMaster productMaster;
private Set<Environment> environmentSet=new HashSet<Environment>(0);
private Set<TestCaseConfiguration> testCaseConfiguration=new HashSet<TestCaseConfiguration>(0);
private Set<WorkPackage> workpackageSet=new HashSet<WorkPackage>(0);
private Set<RunConfiguration> runConfigSet=new HashSet<RunConfiguration>(0);
private Set<Activity> activitySet = new HashSet<Activity>(0);
private Integer envionmentCombinationStatus;

public EnvironmentCombination() {
}
@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "envionmentCombinationId", unique = true, nullable = false)
public Integer getEnvironment_combination_id() {
	return environment_combination_id;
}
public void setEnvironment_combination_id(Integer environment_combination_id) {
	this.environment_combination_id = environment_combination_id;
}
@Column(name = "environmentCombinationName")
public String getEnvironmentCombinationName() {
	return environmentCombinationName;
}
public void setEnvironmentCombinationName(String environmentCombinationName) {
	this.environmentCombinationName = environmentCombinationName;
}
@OneToMany(fetch = FetchType.LAZY, mappedBy = "environmentCombination")
public Set<TestCaseConfiguration> getTestCaseConfiguration() {
	return testCaseConfiguration;
}
public void setTestCaseConfiguration(
		Set<TestCaseConfiguration> testCaseConfiguration) {
	this.testCaseConfiguration = testCaseConfiguration;
}
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name = "environment_combination_has_environment", joinColumns = { @JoinColumn(name = "environmentCombinationId") }, inverseJoinColumns = { @JoinColumn(name = "environmentId") })
public Set<Environment> getEnvironmentSet() {
	return environmentSet;
}
public void setEnvironmentSet(Set<Environment> environmentSet) {
	this.environmentSet = environmentSet;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "productId")
public ProductMaster getProductMaster() {
	return this.productMaster;
}

public void setProductMaster(ProductMaster productMaster) {
	this.productMaster = productMaster;
}

@ManyToMany(fetch = FetchType.LAZY, mappedBy = "environmentCombinationList",cascade=CascadeType.ALL)
public Set<WorkPackage> getWorkpackageSet() {
	return workpackageSet;
}
public void setWorkpackageSet(Set<WorkPackage> workpackageSet) {
	this.workpackageSet = workpackageSet;
}

@OneToMany(fetch = FetchType.LAZY, mappedBy = "environmentcombination")
public Set<RunConfiguration> getRunConfigSet() {
	return runConfigSet;
}
public void setRunConfigSet(Set<RunConfiguration> runConfigSet) {
	this.runConfigSet = runConfigSet;
}

@Column(name = "envionmentCombinationStatus")
public Integer getEnvionmentCombinationStatus() {
	return envionmentCombinationStatus;
}
public void setEnvionmentCombinationStatus(Integer envionmentCombinationStatus) {
	this.envionmentCombinationStatus = envionmentCombinationStatus;
}

@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
@JoinTable(name = "activity_has_environment_combination", joinColumns = { @JoinColumn(name = "environmentCombinationId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "activityId", nullable = false, updatable = false) })
public Set<Activity> getActivitySet() {
	return activitySet;
}
public void setActivitySet(Set<Activity> activitySet) {
	this.activitySet = activitySet;
}

}
