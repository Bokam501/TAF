package com.hcl.atf.taf.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hcl.ilcm.workflow.model.WorkflowStatus;

/**
 * TestCaseList generated by hbm2java
 */
@Entity
@Table(name = "product_feature")
public class ProductFeature implements java.io.Serializable {

	private Integer productFeatureId;
	private ProductMaster productMaster;
	private String productFeatureName;
	private String displayName;
	private String productFeatureDescription;
	private String productFeatureCode;
	private Integer productFeaturestatus;
	@Column(name = "leftIndex")
	private Integer leftIndex;
	@Column(name = "rightIndex")
	private Integer rightIndex;

	private ProductFeature parentFeature;
	private Set<ProductFeature> childFeatures = new HashSet<ProductFeature>(0);
	private Set<WorkPackage> workPackageList = new HashSet<WorkPackage>(0);

	private Set<TestRunJob>  testRunJobSet=new HashSet<TestRunJob>(0);
	private TestCasePriority executionPriority;
	private Set<TestRunPlan> testRunPlanList = new HashSet<TestRunPlan>(0);
	
	private Date createdDate;
	private Date modifiedDate;
	private String sourceType;
	private Set<Risk> risk;
	
	private Integer mappedTestcaseCount;
	
	private String abbr;
	
	private WorkflowStatus workflowStatus;
	
	private UserList assignee;
	private UserList reviewer;
	
	private Integer totalEffort;

	public ProductFeature() {
	}

	public ProductFeature(ProductMaster productMaster, int id, String name,
			String description, String featureCode, int status,Date createdDate,Date modifiedDate,String sourceType,
			Set<TestCaseList> testCaseLists) {
		this.productMaster = productMaster;
		this.productFeatureId = id;
		this.productFeatureName = name;
		this.productFeatureDescription = description;
		this.productFeatureCode = featureCode;
		this.productFeaturestatus = status;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.sourceType=sourceType;
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "productFeatureId", unique = true, nullable = false)
	public Integer getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(Integer id) {
		this.productFeatureId = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentFeatureId")
	@NotFound(action = NotFoundAction.IGNORE)
	public ProductFeature getParentFeature() {
		return parentFeature;
	}

	public void setParentFeature(ProductFeature parentFeature) {
		this.parentFeature = parentFeature;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	@NotFound(action=NotFoundAction.IGNORE)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	// Changes for integration with TestManagement tools

	@Column(name = "productFeatureName", length = 250)
	public String getProductFeatureName() {
		return productFeatureName;
	}

	public void setProductFeatureName(String name) {
		this.productFeatureName = name;
	}

	@Column(name = "displayName", length = 250)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "productFeatureDescription", length = 2000)
	public String getProductFeatureDescription() {
		return productFeatureDescription;
	}

	public void setProductFeatureDescription(String description) {
		this.productFeatureDescription = description;
	}

	@Column(name = "productFeatureCode", length = 50)
	public String getProductFeatureCode() {
		return productFeatureCode;
	}

	public void setProductFeatureCode(String featureCode) {
		this.productFeatureCode = featureCode;
	}

	@Column(name = "productFeatureStatus", length = 50)
	public Integer getProductFeaturestatus() {
		return productFeaturestatus;
	}

	public void setProductFeaturestatus(Integer productFeaturestatus) {
		this.productFeaturestatus = productFeaturestatus;
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
	
	private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>(0);
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_feature_has_test_case_list", joinColumns = { @JoinColumn(name = "productFeatureId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}
	
	public Integer getLeftIndex() {
		return leftIndex;
	}

	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	public Integer getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}
	
	@Column(name = "sourceType", length = 25)
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Override
	public boolean equals(Object o) {
		ProductFeature productFeature = (ProductFeature) o;
		if (this.productFeatureId .equals( productFeature.getProductFeatureId())) {
			return true;
		}else{
			return false;
		}
	}	
	
	@Override
	public int hashCode(){
	    return (int) productFeatureId;
	  }
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentFeature", cascade=CascadeType.ALL)
	public Set<ProductFeature> getChildFeatures() {
		return childFeatures;
	}

	public void setChildFeatures(Set<ProductFeature> childFeatures) {
		this.childFeatures = childFeatures;
	}
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "productFeature",cascade=CascadeType.ALL)
	public Set<WorkPackage> getWorkPackageList() {
		return workPackageList;
	}

	public void setWorkPackageList(Set<WorkPackage> workPackageList) {
		this.workPackageList = workPackageList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "featureSet",cascade=CascadeType.ALL)
	public Set<TestRunJob> getTestRunJobSet() {
		return testRunJobSet;
	}

	public void setTestRunJobSet(Set<TestRunJob> testRunJobSet) {
		this.testRunJobSet = testRunJobSet;
	}

	

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "executionPriority")
	public TestCasePriority getExecutionPriority() {
		return executionPriority;
	}

	public void setExecutionPriority(TestCasePriority executionPriority) {
		this.executionPriority = executionPriority;
	}
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "featureList",cascade=CascadeType.ALL)
	public Set<TestRunPlan> getTestRunPlanList() {
		return testRunPlanList;
	}

	public void setTestRunPlanList(Set<TestRunPlan> testRunPlanList) {
		this.testRunPlanList = testRunPlanList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "featureList",cascade=CascadeType.ALL)
	public Set<Risk> getRisk() {
		return risk;
	}

	public void setRisk(Set<Risk> risk) {
		this.risk = risk;
	}

	@Transient
	public Integer getMappedTestcaseCount() {
		return mappedTestcaseCount;
	}

	public void setMappedTestcaseCount(Integer mappedTestcaseCount) {
		this.mappedTestcaseCount = mappedTestcaseCount;
	}

	@Column(name="abbr")
	public String getAbbr() {
		return abbr;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusId")
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigneeId")
	public UserList getAssignee() {
		return assignee;
	}

	public void setAssignee(UserList assignee) {
		this.assignee = assignee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewerId")
	public UserList getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserList reviewer) {
		this.reviewer = reviewer;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	@Column(name="totalEffort")
	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}
	
	
	
	
}