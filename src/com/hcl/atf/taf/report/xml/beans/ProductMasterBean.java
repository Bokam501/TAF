package com.hcl.atf.taf.report.xml.beans;


// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


public class ProductMasterBean implements java.io.Serializable {

	private Integer productId;
	private String productName;
	private String projectCode;
	private String projectName;
	private String productDescription;
	private Set<TestSuiteListBean> testSuiteLists = new HashSet<TestSuiteListBean>(0);
	//private Set<TestCaseListBean> testCaseLists = new HashSet<TestCaseListBean>(0);
	private Integer status;
	private Date createdDate;
	private Date statusChangeDate;
	

	//private TestFactory testFactory;
	/*private CustomerBean customer;
	private ProductModeBean productMode;
	private Set<ProductVersionListMasterBean> productVersionListMasters = new HashSet<ProductVersionListMasterBean>(0);
	private Set<TestRunConfigurationParentBean> testRunConfigurationParents = new HashSet<TestRunConfigurationParentBean>(0);*/
	//private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>(0);
	//private Set<DecouplingCategory> decouplingCategory = new HashSet<DecouplingCategory>(0);

	//Changes for integration with DefectManagementSystems
	//private Set<DefectManagementSystem> defectManagementSystems = new HashSet<DefectManagementSystem>(0);
	//private Set<DefectManagementSystemMapping> defectManagementSystemMappings = new HashSet<DefectManagementSystemMapping>(0);
	//Changes for integration with TestManagementSystems
	//private Set<TestManagementSystem> testManagementSystems = new HashSet<TestManagementSystem>(0);
	//private Set<TestManagementSystemMapping> testManagementSystemMappings = new HashSet<TestManagementSystemMapping>(0);
	//private Set<ResourceShiftCheckIn> resourceShiftCheckInSet = new HashSet<ResourceShiftCheckIn>(0);
	
	
	
	
	//private ProductTypeBean productType;
	
	private Integer shiftAttendanceGraceTime;
	private Integer weeklyOverTimeLimit;
	private Integer shiftLunchAuthorisedTime;
	private Integer shiftBreaksAuthorisedTime;
	/*private Set<GenericDevicesBean> genericeDevices = new HashSet<GenericDevicesBean>(0);
	private Set<HostListBean> hostLists = new HashSet<HostListBean>(0);*/
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Set<TestSuiteListBean> getTestSuiteLists() {
		return testSuiteLists;
	}
	public void setTestSuiteLists(Set<TestSuiteListBean> testSuiteLists) {
		this.testSuiteLists = testSuiteLists;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getStatusChangeDate() {
		return statusChangeDate;
	}
	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}
	public Integer getShiftAttendanceGraceTime() {
		return shiftAttendanceGraceTime;
	}
	public void setShiftAttendanceGraceTime(Integer shiftAttendanceGraceTime) {
		this.shiftAttendanceGraceTime = shiftAttendanceGraceTime;
	}
	public Integer getWeeklyOverTimeLimit() {
		return weeklyOverTimeLimit;
	}
	public void setWeeklyOverTimeLimit(Integer weeklyOverTimeLimit) {
		this.weeklyOverTimeLimit = weeklyOverTimeLimit;
	}
	public Integer getShiftLunchAuthorisedTime() {
		return shiftLunchAuthorisedTime;
	}
	public void setShiftLunchAuthorisedTime(Integer shiftLunchAuthorisedTime) {
		this.shiftLunchAuthorisedTime = shiftLunchAuthorisedTime;
	}
	public Integer getShiftBreaksAuthorisedTime() {
		return shiftBreaksAuthorisedTime;
	}
	public void setShiftBreaksAuthorisedTime(Integer shiftBreaksAuthorisedTime) {
		this.shiftBreaksAuthorisedTime = shiftBreaksAuthorisedTime;
	}

	//private Set<ProductTeamResources> productTeamResources = new HashSet<ProductTeamResources>(0);
	//private Set<TestFactoryProductCoreResource> productCoreResources = new HashSet<TestFactoryProductCoreResource>(0);
	
	//private Set<ProductUserRoleBean> productUserRoles = new HashSet<ProductUserRoleBean>(0);
	

	/*public ProductMaster(String productName, String projectCode,String projectName,
			String productDescription, Set<TestSuiteList> testSuiteLists,
			Set<TestCaseList> testCaseLists,
			Set<ProductVersionListMaster> productVersionListMasters,
			Set<TestRunConfigurationParent> testRunConfigurationParents, 
			Set<ProductFeature> productFeatures,
			Set<DefectManagementSystem> defectManagementSystems,
			Set<DefectManagementSystemMapping> defectManagementSystemMappings,
			Set<TestManagementSystem> testManagementSystems,
			Set<TestManagementSystemMapping> testManagementSystemMappings) {
		this.productName = productName;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.productDescription = productDescription;
		this.testSuiteLists = testSuiteLists;
		this.testCaseLists = testCaseLists;
		this.productVersionListMasters = productVersionListMasters;
		this.testRunConfigurationParents = testRunConfigurationParents;
		this.productFeatures = productFeatures;
		this.defectManagementSystems = defectManagementSystems;
		this.defectManagementSystemMappings=defectManagementSystemMappings;
		this.testManagementSystems = testManagementSystems;
		this.testManagementSystemMappings=testManagementSystemMappings;
		
	}*/

	
	
}
