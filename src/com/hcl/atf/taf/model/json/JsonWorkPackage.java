package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCycle;
import com.hcl.atf.taf.model.WorkPackage;


public class JsonWorkPackage implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackage.class);
	
	@JsonProperty	
	private Integer id;
	@JsonProperty
	private String name;	
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer isActive;
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String plannedEndDate;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;
	@JsonProperty
	private Integer productBuildId;	
	@JsonProperty
	private String productBuildName;
	@JsonProperty
	private String runCode;
	@JsonProperty
	private Integer productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private int productVersionId;
	@JsonProperty	
	private String productVersionName;
	
	@JsonProperty	
	private String workPackageType;
	
	@JsonProperty
	private String createDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer combinedResultsReportingJobId;
	@JsonProperty
	private String resultsReportingMode;
	@JsonProperty
	private Integer testCycleId;
	public JsonWorkPackage(){
		
	}
	
	public JsonWorkPackage(WorkPackage workPackage){
		this.id= workPackage.getWorkPackageId();
		this.name= workPackage.getName();
		this.description= workPackage.getDescription();
		if(workPackage.getIsActive() != null){
			this.isActive = workPackage.getIsActive();
		}		
		if(workPackage.getStatus() != null){
			this.status = workPackage.getStatus();
		}
	
		this.productBuildId = workPackage.getProductBuild().getProductBuildId();
		if (workPackage.getProductBuild() != null) {
			productBuildId = workPackage.getProductBuild().getProductBuildId(); 
		}
		if (workPackage.getProductBuild().getProductVersion() != null) {
			this.productVersionId = workPackage.getProductBuild().getProductVersion().getProductVersionListId();
			this.productVersionName = workPackage.getProductBuild().getProductVersion().getProductVersionName();
		}
		
		if (workPackage != null) {
			this.workPackageType = workPackage.getWorkPackageType().getName();
		}
		
		
		if (workPackage.getProductBuild().getProductVersion().getProductMaster() != null) {
			this.productId = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if (workPackage.getProductBuild() != null) {
			productBuildId = workPackage.getProductBuild().getProductBuildId(); 
		}
			if(workPackage.getPlannedStartDate()!=null){
				this.plannedStartDate=DateUtility.dateToStringInSecond(workPackage.getPlannedStartDate());
			}
			
			if(workPackage.getPlannedEndDate()!=null){
				this.plannedEndDate=DateUtility.dateToStringInSecond(workPackage.getPlannedEndDate());
			}
			if(workPackage.getActualEndDate()!=null){
				this.actualEndDate=DateUtility.dateToStringInSecond(workPackage.getActualEndDate());
			}
			if(workPackage.getActualStartDate()!=null){
				this.actualStartDate=DateUtility.dateToStringInSecond(workPackage.getActualStartDate());
			}
			
			if(workPackage.getCreateDate()!=null){
				this.createDate = DateUtility.dateToStringWithSeconds1(workPackage.getCreateDate());
			}
			if(workPackage.getModifiedDate()!=null){
				this.modifiedDate = DateUtility.dateformatWithOutTime(workPackage.getModifiedDate());
			}
			if(workPackage.getResultsReportingMode() != null){
				this.resultsReportingMode = workPackage.getResultsReportingMode();
			}
			if(workPackage.getTestCycle() != null ){
				this.testCycleId = workPackage.getTestCycle().getTestCycleId();
			}
			
			
	}
	
	@JsonIgnore
	public WorkPackage getWorkPackage() {

		WorkPackage workPackage = new WorkPackage();
		if (id != null) {
			workPackage.setWorkPackageId(id);
		}
		workPackage.setName(name);
		workPackage.setDescription(description);
		workPackage.setIsActive(isActive);
		if(this.isActive !=null){
			workPackage.setIsActive(isActive);
		}else{
			workPackage.setIsActive(1);
		}
		if(this.status != null ){			
			workPackage.setStatus(status);			
		}else{
			workPackage.setStatus(0);	
		}
		
		
		if(this.productId != null){
			
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			productMaster.setProductName(productName);
			workPackage.setProductMaster(productMaster);
		}

		
		
		if(this.createDate == null || this.createDate.trim().isEmpty()) {
			workPackage.setCreateDate(DateUtility.getCurrentTime());
		} else {
			workPackage.setCreateDate(DateUtility.toFormatDate(this.createDate));
		}
		workPackage.setModifiedDate(DateUtility.getCurrentTime());
		
			
		ProductBuild productBuild = new ProductBuild();
		productBuild.setProductBuildId(productBuildId);
		workPackage.setProductBuild(productBuild);
		
		
		
		if(this.actualStartDate == null || this.actualStartDate.trim().isEmpty()) {
			workPackage.setActualStartDate(DateUtility.getCurrentTime());
		} else {
		
			workPackage.setActualStartDate(DateUtility.dateFormatWithOutSeconds(this.actualStartDate));
		}
		if(this.actualEndDate == null || this.actualEndDate.trim().isEmpty()) {
			workPackage.setActualEndDate(DateUtility.getCurrentTime());
		} else {
		
			workPackage.setActualEndDate(DateUtility.dateFormatWithOutSeconds(this.actualEndDate));
		}
		if(this.plannedStartDate == null || this.plannedStartDate.trim().isEmpty()) {
			workPackage.setPlannedStartDate(DateUtility.getCurrentTime());
		} else {
		
			workPackage.setPlannedStartDate(DateUtility.dateFormatWithOutSeconds(this.plannedStartDate));
		}
		if(this.plannedEndDate == null || this.plannedEndDate.trim().isEmpty()) {
			workPackage.setPlannedEndDate(DateUtility.getCurrentTime());
		} else {
		
			workPackage.setPlannedEndDate(DateUtility.dateFormatWithOutSeconds(this.plannedEndDate));
		}
		
		workPackage.setResultsReportingMode(this.resultsReportingMode);
		
		TestCycle tc = new TestCycle();
		tc.setTestCycleId(this.testCycleId);
		workPackage.setTestCycle(tc);
		return workPackage;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public String getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public String getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public String getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}
	
	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(int productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public boolean equals(Object o) {
		WorkPackage workPackage = (WorkPackage) o;
		if (this.id == workPackage.getWorkPackageId()) {
			return true;
		}else{
			return false;
		}
	}

	public String getWorkPackageType() {
		return workPackageType;
	}

	public void setWorkPackageType(String workPackageType) {
		this.workPackageType = workPackageType;
	}
	public Integer getCombinedResultsReportingJobId() {
		return combinedResultsReportingJobId;
	}
	public void setCombinedResultsReportingJobId(Integer combinedResultsReportingJobId) {
		this.combinedResultsReportingJobId = combinedResultsReportingJobId;
	}

	public String getResultsReportingMode() {
		return resultsReportingMode;
	}

	public void setResultsReportingMode(String resultsReportingMode) {
		this.resultsReportingMode = resultsReportingMode;
	}

	public Integer getTestCycleId() {
		return testCycleId;
	}

	public void setTestCycleId(Integer testCycleId) {
		this.testCycleId = testCycleId;
	}

	
}
