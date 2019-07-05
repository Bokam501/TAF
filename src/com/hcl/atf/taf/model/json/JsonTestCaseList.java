package com.hcl.atf.taf.model.json;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

public class JsonTestCaseList implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonTestCaseList.class);
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionListName;
	@JsonProperty
	private Integer devicePlatformVersionListId;
	@JsonProperty
	private String devicePlatformVersion;
	@JsonProperty
	private String devicePlatformName;
	@JsonProperty
	private String testSuiteScriptFileLocation;
	@JsonProperty
	private String testSuiteName;
	@JsonProperty
	private String testScriptType;
	@JsonProperty
	private String testCaseNameOptions;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private String testCaseDescription;
	@JsonProperty
	private String testCaseCode;
	@JsonProperty
	private Integer status;
	// Changes for Integration with testManagement tools
	@JsonProperty
	private Integer productFeatureId;
	@JsonProperty
	private Integer decouplingCategoryId;
	@JsonProperty
	private String testCaseCreatedDate;
	@JsonProperty
	private String testCaseType;
	@JsonProperty
	private String testCasePriority;
	@JsonProperty
	private String testCaseLastUpdatedDate;
	@JsonProperty
	private String testCaseSource;
	@JsonProperty
	private Integer testcaseExecutionType;
	@JsonProperty
	private String testcaseinput;
	@JsonProperty
	private String testcaseexpectedoutput;
	@JsonProperty
	private Integer executionTypeId;
	@JsonProperty
	private Integer testcasePriorityId;
	@JsonProperty
	private Integer testcaseTypeId;
	@JsonProperty
	private Integer isSelected;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	@JsonProperty
	private boolean isBeingEdited = false;
	@JsonProperty
	private String editingUser;

	/*
	 * @JsonProperty private Integer executionPriorityId;
	 */

	// Changes for Integration with testManagement tools - ends

	private String testCaseScriptQualifiedName;
	private String testCaseScriptFileName;
	
	/*
	 * Added for Test Case Execution Order Fix
	 */
	@JsonProperty
	private Integer testCaseExecutionOrder;
	
	/*
	 * @JsonProperty private String isSelected;
	 */
	/*
	 * @JsonProperty private String allVersions;
	 * 
	 * @JsonProperty private String clearVersions;
	 */

	

	// The below variables are use to store the mapping status of the test case
	// to the various versions of the product
	// 1 : Is mapped to the specific version
	// 0 : Not mapped to the specific version
	// A maximum of 20 versions are supported for this mapping
	@JsonProperty
	private String ver1;
	@JsonProperty
	private String ver2;
	@JsonProperty
	private String ver3;
	@JsonProperty
	private String ver4;
	@JsonProperty
	private String ver5;
	@JsonProperty
	private String ver6;
	@JsonProperty
	private String ver7;
	@JsonProperty
	private String ver8;
	@JsonProperty
	private String ver9;
	@JsonProperty
	private String ver10;
	@JsonProperty
	private String ver11;
	@JsonProperty
	private String ver12;
	@JsonProperty
	private String ver13;
	@JsonProperty
	private String ver14;
	@JsonProperty
	private String ver15;
	@JsonProperty
	private String ver16;
	@JsonProperty
	private String ver17;
	@JsonProperty
	private String ver18;
	@JsonProperty
	private String ver19;
	@JsonProperty
	private String ver20;
	@JsonProperty
	private Integer totalUnMappedTCList;
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private String workflowStatusDisplayName;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private Integer totalEffort;

	@JsonProperty
	private Integer productTypeId;
	@JsonProperty
	private String productTypeName;
	@JsonProperty
	private Integer workflowId;

	@JsonProperty
	private String actors;

	@JsonProperty
	private String completedBy;

	@JsonProperty
	private Integer remainingHours;

	@JsonProperty
	private String workflowIndicator;
	
	@JsonProperty
	private Integer attachmentCount;
	
	@JsonProperty
	private String remainingHrsMins;
	
	@JsonProperty
	private String iseRecommended;
	
	@JsonProperty
	private String recommendedCategory;
	
	@JsonProperty
	private String probability;	
	
	@JsonProperty
    private Integer recommendedTestCaseCount;
	
	@JsonProperty
    private Integer totalTestCaseCount;
	
	@JsonProperty
	private String testCaePredecessors;
	
	@JsonProperty
	private String productFeatureName;

	
	@JsonProperty
	private String productType;
	
	
	public JsonTestCaseList() {
		this.productId = new Integer(0);
	}

	public JsonTestCaseList(TestCaseList testCaseList) {

		
		if (testCaseList.getTestCaseId() != null)
			this.testCaseId = testCaseList.getTestCaseId();
		if (testCaseList.getTestCaseName() != null)
			this.testCaseName = testCaseList.getTestCaseName();
		if (testCaseList.getTestCaseDescription() != null)
			this.testCaseDescription = testCaseList.getTestCaseDescription();
		if (testCaseList.getTestCaseCode() != null)
			this.testCaseCode = testCaseList.getTestCaseCode();
		if (testCaseList.getTestCaseType() != null)
			this.testCaseType = testCaseList.getTestCaseType();

		if (testCaseList.getTestCaseCreatedDate() != null) {
			this.testCaseCreatedDate = DateUtility
					.dateToStringWithSeconds1(testCaseList
							.getTestCaseCreatedDate());
		}
		if (testCaseList.getTestCaseLastUpdatedDate() != null) {
			this.testCaseLastUpdatedDate = DateUtility
					.dateformatWithOutTime(testCaseList
							.getTestCaseLastUpdatedDate());
		}

		
		if (testCaseList.getTestCaseSource() != null)
			this.testCaseSource = testCaseList.getTestCaseSource();
		if (testCaseList.getProductMaster() != null
				&& testCaseList.getProductMaster().getProductId() != null) {
			this.productId = testCaseList.getProductMaster().getProductId();

		}

		if (testCaseList.getExecutionTypeMaster() != null) {
			this.executionTypeId = testCaseList.getExecutionTypeMaster()
					.getExecutionTypeId();
		}
		if (testCaseList.getTestCasePriority() != null) {
			this.testcasePriorityId = testCaseList.getTestCasePriority()
					.getTestcasePriorityId();
		} else {
			this.testcasePriorityId = null;
		}
		if (testCaseList.getTestcaseTypeMaster() != null) {
			this.testcaseTypeId = testCaseList.getTestcaseTypeMaster().getTestcaseTypeId();
		} else {
			this.testcaseTypeId = null;
		}
		if (testCaseList.getStatus() != null) {
			this.status = testCaseList.getStatus();
		}
		
		if (testCaseList.getProductFeature() != null) {
			if (testCaseList.getProductFeature().iterator().hasNext()) {
				this.productFeatureId = testCaseList.getProductFeature()
						.iterator().next().getProductFeatureId();
			}
		}
		if (testCaseList.getDecouplingCategory() != null) {
			if (testCaseList.getDecouplingCategory().iterator().hasNext()) {
				this.decouplingCategoryId = testCaseList
						.getDecouplingCategory().iterator().next()
						.getDecouplingCategoryId();
			}
		}
		this.testCaseScriptFileName = testCaseList.getTestCaseScriptFileName();
		this.testCaseScriptQualifiedName = testCaseList
				.getTestCaseScriptQualifiedName();
		this.testCaseNameOptions = testCaseNameOptions;
		this.testcaseExecutionType = testCaseList.getTestcaseExecutionType();
		this.testcaseinput = testCaseList.getTestcaseinput();
		this.testcaseexpectedoutput = testCaseList.getTestcaseexpectedoutput();
		if(testCaseList.getTestCaseExecutionOrder() == null || testCaseList.getTestCaseExecutionOrder() == 0){
			this.testCaseExecutionOrder = testCaseList.getTestCaseId();
		} else {
			this.testCaseExecutionOrder = testCaseList.getTestCaseExecutionOrder();
		}
		if(testCaseList.getTestSuiteLists() != null && testCaseList.getTestSuiteLists().size() != 0){
			this.testSuiteId = testCaseList.getTestSuiteLists().iterator().next().getTestSuiteId();
			this.testSuiteName = testCaseList.getTestSuiteLists().iterator().next().getTestSuiteName(); 
		}
		 

		this.ver1 = "0";
		this.ver2 = "0";
		this.ver3 = "0";
		this.ver4 = "0";
		this.ver5 = "0";
		this.ver6 = "0";
		this.ver7 = "0";
		this.ver8 = "0";
		this.ver9 = "0";
		this.ver10 = "0";
		this.ver11 = "0";
		this.ver12 = "0";
		this.ver13 = "0";
		this.ver14 = "0";
		this.ver15 = "0";
		this.ver16 = "0";
		this.ver17 = "0";
		this.ver18 = "0";
		this.ver19 = "0";
		this.ver20 = "0";
		if (testCaseList.getWorkflowStatus() != null && testCaseList.getWorkflowStatus()
				.getWorkflowStatusId() != -1) {
			this.workflowStatusId = testCaseList.getWorkflowStatus()
					.getWorkflowStatusId();
			this.workflowStatusName = testCaseList.getWorkflowStatus()
					.getWorkflowStatusName();
			this.workflowStatusDisplayName = testCaseList.getWorkflowStatus()
					.getWorkflowStatusDisplayName();
			if(testCaseList.getWorkflowStatus().getWorkflowStatusCategory() != null){
				this.workflowStatusCategoryName = "["+testCaseList.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName()+"]";
			}else{
				this.workflowStatusCategoryName = "";	
			}
		}else{
			this.workflowStatusName = "--";
			this.workflowStatusDisplayName = "--";
			this.workflowStatusCategoryName = "";
		}
		if(testCaseList.getTotalEffort() != null){
			this.totalEffort = testCaseList.getTotalEffort();
		}else{
			this.totalEffort = 0;
		}
		
		this.testCaePredecessors = testCaseList.getTestCaePredecessors();
		
		if (testCaseList.getProductType() != null) {
            this.productTypeId = testCaseList.getProductType().getProductTypeId();
            this.productTypeName=testCaseList.getProductType().getTypeName();
        }
	}

	public JsonTestCaseList(TestCaseList testCaseList, int prodFeatureId) {
		if (testCaseList.getTestCaseId() != null)
			this.testCaseId = testCaseList.getTestCaseId();
		if (testCaseList.getTestCaseName() != null)
			this.testCaseName = testCaseList.getTestCaseName();
		if (testCaseList.getTestCaseDescription() != null)
			this.testCaseDescription = testCaseList.getTestCaseDescription();
		if (testCaseList.getTestCaseCode() != null)
			this.testCaseCode = testCaseList.getTestCaseCode();
		if (testCaseList.getTestCaseType() != null)
			this.testCaseType = testCaseList.getTestCaseType();

		if (testCaseList.getTestCaseCreatedDate() != null) {
			this.testCaseCreatedDate = DateUtility
					.dateToStringWithSeconds1(testCaseList
							.getTestCaseCreatedDate());
		}
		if (testCaseList.getTestCaseLastUpdatedDate() != null) {
			this.testCaseLastUpdatedDate = DateUtility
					.dateformatWithOutTime(testCaseList
							.getTestCaseLastUpdatedDate());
		}

		
		if (testCaseList.getTestCaseSource() != null)
			this.testCaseSource = testCaseList.getTestCaseSource();
		if (testCaseList.getProductMaster() != null
				&& testCaseList.getProductMaster().getProductId() != null) {
			this.productId = testCaseList.getProductMaster().getProductId();
		}
		if (testCaseList.getExecutionTypeMaster() != null) {
			this.executionTypeId = testCaseList.getExecutionTypeMaster()
					.getExecutionTypeId();
		}
		if (testCaseList.getTestCasePriority() != null) {
			this.testcasePriorityId = testCaseList.getTestCasePriority()
					.getTestcasePriorityId();
			this.testCasePriority = testCaseList.getTestCasePriority().getPriorityName();
		} else {
			this.testcasePriorityId = null;
			this.testCasePriority = "";
		}
		if (testCaseList.getTestcaseTypeMaster() != null) {
			this.testcaseTypeId = testCaseList.getTestcaseTypeMaster()
					.getTestcaseTypeId();
			this.testCaseType = testCaseList.getTestcaseTypeMaster().getName();
		} else {
			this.testcaseTypeId = null;
			this.testCaseType ="";
		}
		if (testCaseList.getStatus() != null) {
			this.status = testCaseList.getStatus();
		}
		
		if (testCaseList.getProductFeature() != null) {
			if (testCaseList.getProductFeature().iterator().hasNext()) {
				this.productFeatureId = prodFeatureId;
			}
		}
		if (testCaseList.getDecouplingCategory() != null) {
			if (testCaseList.getDecouplingCategory().iterator().hasNext()) {
				this.decouplingCategoryId = testCaseList
						.getDecouplingCategory().iterator().next()
						.getDecouplingCategoryId();
			}
		}
		this.testCaseScriptFileName = testCaseList.getTestCaseScriptFileName();
		this.testCaseScriptQualifiedName = testCaseList
				.getTestCaseScriptQualifiedName();
		this.testCaseNameOptions = testCaseNameOptions;
		this.testcaseExecutionType = testCaseList.getTestcaseExecutionType();
		this.testcaseinput = testCaseList.getTestcaseinput();
		this.testcaseexpectedoutput = testCaseList.getTestcaseexpectedoutput();
		if (testCaseList.getWorkflowStatus() != null) {
			this.workflowStatusId = testCaseList.getWorkflowStatus()
					.getWorkflowStatusId();
			this.workflowStatusName = testCaseList.getWorkflowStatus()
					.getWorkflowStatusDisplayName();
		}

		this.totalEffort = testCaseList.getTotalEffort();
		this.actors = actors;
		this.completedBy=completedBy;
		this.remainingHours=remainingHours;
		this.workflowIndicator = workflowIndicator;
		this.remainingHrsMins = remainingHrsMins;

		this.testCaePredecessors = testCaseList.getTestCaePredecessors();
		if (testCaseList.getProductType() != null) {
            this.productTypeId = testCaseList.getProductType().getProductTypeId();
        }
	}

	public String getProductFeatureName() {
		return productFeatureName;
	}

	public void setProductFeatureName(String productFeatureName) {
		this.productFeatureName = productFeatureName;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

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

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public String getProductVersionListName() {
		return productVersionListName;
	}

	public void setProductVersionListName(String productVersionListName) {
		this.productVersionListName = productVersionListName;
	}

	public Integer getDevicePlatformVersionListId() {
		return devicePlatformVersionListId;
	}

	public void setDevicePlatformVersionListId(
			Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
	}

	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}

	public void setTestSuiteScriptFileLocation(
			String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getTestCaseNameOptions() {
		return testCaseNameOptions;
	}

	public void setTestCaseNameOptions(String testCaseNameOptions) {
		this.testCaseNameOptions = testCaseNameOptions;
	}

	public String getTestScriptType() {
		return testScriptType;
	}

	public void setTestScriptType(String testScriptType) {
		this.testScriptType = testScriptType;
	}

	@JsonIgnore
	public TestCaseList getTestCaseList() {

		TestCaseList testCaseList = new TestCaseList();
		testCaseList.setTestCaseId(testCaseId);
		testCaseList.setTestCaseName(testCaseName);
		testCaseList.setTestCaseDescription(testCaseDescription);
		testCaseList.setTestCaseCode(testCaseCode);
        testCaseList.setTestCaseExecutionOrder(testCaseExecutionOrder);
		if (this.status != null) {
			testCaseList.setStatus(status);
		} else {
			testCaseList.setStatus(0);
		}
		TestSuiteList testSuiteList = new TestSuiteList();
		if (testSuiteId != null) {
			testSuiteList.setTestSuiteId(testSuiteId);
			log.info("testSuiteId::  " + testSuiteId);
			testCaseList.getTestSuiteLists().add(testSuiteList);
		}
		if (productId != null) {
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			testCaseList.setProductMaster(productMaster);
		}
		if (testSuiteList.getProductMaster() == null) {
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			if(productVersionListId != null){
				ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
				productVersionListMaster
						.setProductVersionListId(productVersionListId);
				productVersionListMaster.setProductMaster(productMaster);
				testSuiteList.setProductVersionListMaster(productVersionListMaster);
			}
			testSuiteList.setProductMaster(productMaster);
		}

		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
		devicePlatformVersionListMaster
				.setDevicePlatformVersionListId(devicePlatformVersionListId);
		devicePlatformVersionListMaster
				.setDevicePlatformMaster(devicePlatformMaster);

		testSuiteList.setTestSuiteName(testSuiteName);
		testSuiteList
				.setTestSuiteScriptFileLocation(testSuiteScriptFileLocation);

		if (testSuiteList.getScriptTypeMaster() != null) {
			ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
			scriptTypeMaster.setScriptType(testScriptType);
			testSuiteList.setScriptTypeMaster(scriptTypeMaster);

		}
		testCaseList.setTestCaseType(testCaseType);
		testCaseList.setTestCaseSource(testCaseSource);
		testCaseList.setTestcaseExecutionType(testcaseExecutionType);

		if (executionTypeId != null) {
			ExecutionTypeMaster etm = new ExecutionTypeMaster();
			etm.setExecutionTypeId(executionTypeId);
			testCaseList.setExecutionTypeMaster(etm);
		}
		if (testcasePriorityId != null) {
			TestCasePriority testcasePriority = new TestCasePriority();
			testcasePriority.setTestcasePriorityId(testcasePriorityId);
			testCaseList.setTestCasePriority(testcasePriority);
		}
		if (testcaseTypeId != null) {
			TestcaseTypeMaster testcaseTypeMaster = new TestcaseTypeMaster();
			testcaseTypeMaster.setTestcaseTypeId(testcaseTypeId);
			testCaseList.setTestcaseTypeMaster(testcaseTypeMaster);
		}

		if (this.testCaseCreatedDate == null
				|| this.testCaseCreatedDate.trim().isEmpty()) {
			testCaseList.setTestCaseCreatedDate(DateUtility.getCurrentTime());
		} else {

			testCaseList.setTestCaseCreatedDate(DateUtility
					.toFormatDate(this.testCaseCreatedDate));
		}
		testCaseList.setTestCaseLastUpdatedDate(DateUtility.getCurrentTime());

		testCaseList.setTestCaseScriptFileName(testCaseScriptFileName);
		testCaseList
				.setTestCaseScriptQualifiedName(testCaseScriptQualifiedName);
	
		if ((productFeatureId != null) && (productFeatureId != 0)) {
			ProductFeature productFeature = new ProductFeature();
			productFeature.setProductFeatureId(productFeatureId);
			Set<ProductFeature> featureSet = new HashSet<ProductFeature>();
			featureSet.add(productFeature);
			testCaseList.setProductFeature(featureSet);
		}
		if (decouplingCategoryId != null) {
			DecouplingCategory decouplingCategory = new DecouplingCategory();
			decouplingCategory.setDecouplingCategoryId(decouplingCategoryId);
			Set<DecouplingCategory> decouplingSet = new HashSet<DecouplingCategory>();
			decouplingSet.add(decouplingCategory);
			testCaseList.setDecouplingCategory(decouplingSet);
		}
		if (this.workflowStatusId != null) {
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.workflowStatusId);
			testCaseList.setWorkflowStatus(workflowStatus);
		}
		if ((productTypeId != null) && (productTypeId != 0)) {
			ProductType productType = new ProductType();
			productType.setProductTypeId(productTypeId);
			testCaseList.setProductType(productType);
		}
		testCaseList.setTestCaePredecessors(this.testCaePredecessors);
		
		return testCaseList;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getTestCaseDescription() {
		return testCaseDescription;
	}

	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}

	public String getTestCaseCode() {
		return testCaseCode;
	}

	public void setTestCaseCode(String testCaseCode) {
		this.testCaseCode = testCaseCode;
	}

	// Changes for integration with testmanagement tools
	public Integer getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}

	public Integer getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(Integer decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getTestCaseCreatedDate() {
		return testCaseCreatedDate;
	}

	public void setTestCaseCreatedDate(String testCaseCreatedDate) {
		this.testCaseCreatedDate = testCaseCreatedDate;
	}

	public String getTestCaseAutomationType() {
		return testCaseType;
	}

	public void setTestCaseAutomationType(String testCaseAutomationType) {
		this.testCaseType = testCaseAutomationType;
	}

	public String getTestCasePriority() {
		return testCasePriority;
	}

	public void setTestCasePriority(String testCasePriority) {
		this.testCasePriority = testCasePriority;
	}

	public String getTestCaseSource() {
		return testCaseSource;
	}

	public void setTestCaseSource(String testCaseSource) {
		this.testCaseSource = testCaseSource;
	}

	// Changes for integration with testmanagement tools - ends

	public String getTestCaseType() {
		return testCaseType;
	}

	public void setTestCaseType(String testCaseType) {
		this.testCaseType = testCaseType;
	}

	public Integer getTestcaseExecutionType() {
		return testcaseExecutionType;
	}

	public void setTestcaseExecutionType(Integer testcaseExecutionType) {
		this.testcaseExecutionType = testcaseExecutionType;
	}

	public String getTestcaseinput() {
		return testcaseinput;
	}

	public void setTestcaseinput(String testcaseinput) {
		this.testcaseinput = testcaseinput;
	}

	public String getTestcaseexpectedoutput() {
		return testcaseexpectedoutput;
	}

	public void setTestcaseexpectedoutput(String testcaseexpectedoutput) {
		this.testcaseexpectedoutput = testcaseexpectedoutput;
	}

	public Integer getExecutionTypeId() {
		return executionTypeId;
	}

	public void setExecutionTypeId(Integer executionTypeId) {
		this.executionTypeId = executionTypeId;
	}

	public Integer getTestcasePriorityId() {
		return testcasePriorityId;
	}

	public void setTestcasePriorityId(Integer testcasePriorityId) {
		this.testcasePriorityId = testcasePriorityId;
	}

	public Integer getTestcaseTypeId() {
		return testcaseTypeId;
	}

	public void setTestcaseTypeId(Integer testcaseTypeId) {
		this.testcaseTypeId = testcaseTypeId;
	}

	public String getTestCaseScriptQualifiedName() {
		return testCaseScriptQualifiedName;
	}

	public void setTestCaseScriptQualifiedName(
			String testCaseScriptQualifiedName) {
		this.testCaseScriptQualifiedName = testCaseScriptQualifiedName;
	}

	public String getTestCaseScriptFileName() {
		return testCaseScriptFileName;
	}

	public void setTestCaseScriptFileName(String testCaseScriptFileName) {
		this.testCaseScriptFileName = testCaseScriptFileName;
	}

	
	public String getVer1() {
		return ver1;
	}

	public void setVer1(String ver1) {
		this.ver1 = ver1;
	}

	public String getVer2() {
		return ver2;
	}

	public void setVer2(String ver2) {
		this.ver2 = ver2;
	}

	public String getVer3() {
		return ver3;
	}

	public void setVer3(String ver3) {
		this.ver3 = ver3;
	}

	public String getVer4() {
		return ver4;
	}

	public void setVer4(String ver4) {
		this.ver4 = ver4;
	}

	public String getVer5() {
		return ver5;
	}

	public void setVer5(String ver5) {
		this.ver5 = ver5;
	}

	public String getVer6() {
		return ver6;
	}

	public void setVer6(String ver6) {
		this.ver6 = ver6;
	}

	public String getVer7() {
		return ver7;
	}

	public void setVer7(String ver7) {
		this.ver7 = ver7;
	}

	public String getVer8() {
		return ver8;
	}

	public void setVer8(String ver8) {
		this.ver8 = ver8;
	}

	public String getVer9() {
		return ver9;
	}

	public void setVer9(String ver9) {
		this.ver9 = ver9;
	}

	public String getVer10() {
		return ver10;
	}

	public void setVer10(String ver10) {
		this.ver10 = ver10;
	}

	public String getVer11() {
		return ver11;
	}

	public void setVer11(String ver11) {
		this.ver11 = ver11;
	}

	public String getVer12() {
		return ver12;
	}

	public void setVer12(String ver12) {
		this.ver12 = ver12;
	}

	public String getVer13() {
		return ver13;
	}

	public void setVer13(String ver13) {
		this.ver13 = ver13;
	}

	public String getVer14() {
		return ver14;
	}

	public void setVer14(String ver14) {
		this.ver14 = ver14;
	}

	public String getVer15() {
		return ver15;
	}

	public void setVer15(String ver15) {
		this.ver15 = ver15;
	}

	public String getVer16() {
		return ver16;
	}

	public void setVer16(String ver16) {
		this.ver16 = ver16;
	}

	public String getVer17() {
		return ver17;
	}

	public void setVer17(String ver17) {
		this.ver17 = ver17;
	}

	public String getVer18() {
		return ver18;
	}

	public void setVer18(String ver18) {
		this.ver18 = ver18;
	}

	public String getVer19() {
		return ver19;
	}

	public void setVer19(String ver19) {
		this.ver19 = ver19;
	}

	public String getVer20() {
		return ver20;
	}

	public void setVer20(String ver20) {
		this.ver20 = ver20;
	}


	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}

	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}

	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}

	public void setAllVersionMappingsStatus(String mappingStatus) {

		if (mappingStatus.equals("1") || mappingStatus.equals("0")) {
			this.ver1 = mappingStatus;
			this.ver2 = mappingStatus;
			this.ver3 = mappingStatus;
			this.ver4 = mappingStatus;
			this.ver5 = mappingStatus;
			this.ver6 = mappingStatus;
			this.ver7 = mappingStatus;
			this.ver8 = mappingStatus;
			this.ver9 = mappingStatus;
			this.ver10 = mappingStatus;
			this.ver11 = mappingStatus;
			this.ver12 = mappingStatus;
			this.ver13 = mappingStatus;
			this.ver14 = mappingStatus;
			this.ver15 = mappingStatus;
			this.ver16 = mappingStatus;
			this.ver17 = mappingStatus;
			this.ver18 = mappingStatus;
			this.ver19 = mappingStatus;
			this.ver20 = mappingStatus;
		}
	}

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getTotalUnMappedTCList() {
		return totalUnMappedTCList;
	}

	public void setTotalUnMappedTCList(Integer totalUnMappedTCList) {
		this.totalUnMappedTCList = totalUnMappedTCList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

	public Integer getRemainingHours() {
		return remainingHours;
	}

	public void setRemainingHours(Integer remainingHours) {
		this.remainingHours = remainingHours;
	}

	public String getWorkflowIndicator() {
		return workflowIndicator;
	}

	public void setWorkflowIndicator(String workflowIndicator) {
		this.workflowIndicator = workflowIndicator;
	}
	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public String getRemainingHrsMins() {
		return remainingHrsMins;
	}

	public void setRemainingHrsMins(String remainingHrsMins) {
		this.remainingHrsMins = remainingHrsMins;
	}

	public String getTestCaseLastUpdatedDate() {
		return testCaseLastUpdatedDate;
	}

	public void setTestCaseLastUpdatedDate(String testCaseLastUpdatedDate) {
		this.testCaseLastUpdatedDate = testCaseLastUpdatedDate;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}
	
	public Integer getTestCaseExecutionOrder() {
		return testCaseExecutionOrder;
	}

	public void setTestCaseExecutionOrder(Integer testCaseExecutionOrder) {
		this.testCaseExecutionOrder = testCaseExecutionOrder;
	}

	public String getIseRecommended() {
		return iseRecommended;
	}

	public void setIseRecommended(String iseRecommended) {
		this.iseRecommended = iseRecommended;
	}

	public String getRecommendedCategory() {
		return recommendedCategory;
	}

	public void setRecommendedCategory(String recommendedCategory) {
		this.recommendedCategory = recommendedCategory;
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public Integer getRecommendedTestCaseCount() {
		return recommendedTestCaseCount;
	}

	public void setRecommendedTestCaseCount(Integer recommendedTestCaseCount) {
		this.recommendedTestCaseCount = recommendedTestCaseCount;
	}

	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}

	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}

	public String getTestCaePredecessors() {
		return testCaePredecessors;
	}

	public void setTestCaePredecessors(String testCaePredecessors) {
		this.testCaePredecessors = testCaePredecessors;
	}

	public boolean isBeingEdited() {
		return isBeingEdited;
	}

	public void setBeingEdited(boolean isBeingEdited) {
		this.isBeingEdited = isBeingEdited;
	}

	public String getEditingUser() {
		return editingUser;
	}

	public void setEditingUser(String editingUser) {
		this.editingUser = editingUser;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	@JsonIgnore
	public JSONObject getCleanJson() {
		JSONObject jsonTestCaseObject= new JSONObject();
		try {
			jsonTestCaseObject.put("testCaseCode", testCaseCode);
			jsonTestCaseObject.put("testCaseName", testCaseName);
			jsonTestCaseObject.put("testCaseDesc", testCaseDescription);
			jsonTestCaseObject.put("productId", productId);
			jsonTestCaseObject.put("productTypeId", productTypeId);
			jsonTestCaseObject.put("testCaseCreatedDate", testCaseCreatedDate);
			jsonTestCaseObject.put("testCaseLastUpdatedDate", testCaseLastUpdatedDate);
			jsonTestCaseObject.put("testcasePriorityId", testcasePriorityId);
			jsonTestCaseObject.put("testcaseTypeId", testcaseTypeId);
			jsonTestCaseObject.put("testScriptType", testScriptType);
			jsonTestCaseObject.put("testcaseExecutionType", testcaseExecutionType);
			jsonTestCaseObject.put("testCaseExecutionOrder", testCaseExecutionOrder);
			jsonTestCaseObject.put("testCaseScriptQualifiedName", testCaseScriptQualifiedName);
			jsonTestCaseObject.put("testCaseScriptFileName", testCaseScriptFileName);
			jsonTestCaseObject.put("testCaePredecessors", testCaePredecessors);
			if(status == 1) {
				jsonTestCaseObject.put("status", "ACTIVE");
			} else {
				jsonTestCaseObject.put("status", "INACTIVE");
			}
			
		}catch(Exception e) {
			log.error("Error in obtaining Json Testcase List"+e);
		}
		return  jsonTestCaseObject;
	}
	
}
