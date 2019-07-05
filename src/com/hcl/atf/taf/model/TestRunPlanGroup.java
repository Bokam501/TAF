package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "testrunplangroup")
public class TestRunPlanGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
private Integer testRunPlanGroupId;

private String name;

private String description;

private Date createdDate;

private Date modifiedDate;
private ExecutionTypeMaster executionTypeMaster;
private Set<TestRunPlangroupHasTestRunPlan> testRunPlangroupHasTestRunPlans = new HashSet<TestRunPlangroupHasTestRunPlan>(0);

private ProductVersionListMaster productVersionListMaster;

private ProductMaster product;

public TestRunPlanGroup () {
}

@Id
@Column(name = "testRunPlanGroupId")
@GeneratedValue(strategy = IDENTITY)
public Integer getTestRunPlanGroupId() {
	return testRunPlanGroupId;
}

public void setTestRunPlanGroupId(Integer testRunPlanGroupId) {
	this.testRunPlanGroupId = testRunPlanGroupId;
}

@Column(name = "name")
public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

@Column(name = "description")
public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
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
@Column(name = "modifiedDate")
public Date getModifiedDate() {
	return modifiedDate;
}

public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "executionType")
public ExecutionTypeMaster getExecutionTypeMaster() {
	return executionTypeMaster;
}

public void setExecutionTypeMaster(ExecutionTypeMaster executionTypeMaster) {
	this.executionTypeMaster = executionTypeMaster;
}

@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunPlanGroup",cascade=CascadeType.ALL)
public Set<TestRunPlangroupHasTestRunPlan> getTestRunPlangroupHasTestRunPlans() {
	return testRunPlangroupHasTestRunPlans;
}

public void setTestRunPlangroupHasTestRunPlans(
		Set<TestRunPlangroupHasTestRunPlan> testRunPlangroupHasTestRunPlans) {
	this.testRunPlangroupHasTestRunPlans = testRunPlangroupHasTestRunPlans;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "productVersionId")
public  ProductVersionListMaster getProductVersionListMaster() {
	return productVersionListMaster;
}
public  void setProductVersionListMaster(
		ProductVersionListMaster productVersionListMaster) {
	this.productVersionListMaster = productVersionListMaster;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "productId")
public ProductMaster getProduct() {
	return product;
}

public void setProduct(ProductMaster product) {
	this.product = product;
}

}

