package com.hcl.atf.taf.report.xml.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.hcl.atf.taf.model.TestCaseList;


@XmlRootElement(name="WorkPackage")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"workPackageId", "productId","productName", "description", "executionType","result","status","productVersion", "productBuild", "testPlanName", "plannedStartDate", "plannedEndDate","passedTestSteps", "totalTestSteps", "passedTestCases", "totalTestCases", "jobsCount","createdBy","productType","duration","executedTCCount","notExecutedTCCount","job"})
public class WorkPackageBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	//Workpackage attributes & details
	private Integer workPackageId;
	@XmlTransient
	private String name;
	private String description;
	private String executionType;
	@XmlTransient
	private String sourceType; // What is this?
	@XmlTransient
	private String isActive;
	@XmlTransient
	private Integer iterationNumber;
	@XmlTransient
	private String lifeCyclePhase;

	private String status;
	private String result;
	@XmlTransient
	private Integer totalJobs;
	private Integer totalTestCases;
	private Integer passedTestCases;
	private Integer totalTestSteps;
	private Integer passedTestSteps;
	private Date plannedStartDate;
	private Date plannedEndDate;
	@XmlTransient
	private Date actualStartDate;
	@XmlTransient
	private Date actualEndDate;
	@XmlTransient
	private Long executionDuration;
	@XmlTransient
	private Date createDate;
	@XmlTransient
	private Date modifiedDate;
	@XmlTransient
	private String runCode; //What is this ?
	@XmlTransient
	private Set<TestCaseList> testcaseList = new HashSet<TestCaseList>(0);
	/*private TestRunPlanBean testRunPlanBean;
	private ProductBean productBean;
	private ProductBuildBean productBuildBean;*/
	@XmlElement(name="job")
	private Set<TestRunJobBean> job  = new HashSet<TestRunJobBean>(0);
	private Integer jobsCount;
	private Integer productId;
    private String productName;
    @XmlTransient
    private Integer prodcutVersionId;
    private String productVersion;
    @XmlTransient
    private Integer productBuildId;
    private String productBuild;
    @XmlTransient
    private Integer testPlanId;
    private String testPlanName;
    @XmlTransient
    private String testTool;
    @XmlTransient
    private String scriptType;
    private String createdBy;
    private String productType;
    private String duration;
    private Integer executedTCCount;
    private Integer notExecutedTCCount;
	public WorkPackageBean() {
	}

	public Integer getWorkPackageId() {
		return this.workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}


	public String getName() {
		return name;
	}


	public void setName(final String paramName) {
		name = paramName;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(final String paramDescription) {
		description = paramDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String paramStatus) {
		status = paramStatus;
	}
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(final Date paramPlannedStartDate) {
		plannedStartDate = paramPlannedStartDate;
	}
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(final Date paramPlannedEndDate) {
		plannedEndDate = paramPlannedEndDate;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(final Date paramActualStartDate) {
		actualStartDate = paramActualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(final Date paramActualEndDate) {
		actualEndDate = paramActualEndDate;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productBuildId") 
	public ProductBuild getProductBuild() {
		return productBuild;
	}

	public void setProductBuild(ProductBuild productBuild) {
		this.productBuild = productBuild;
	}
	 */
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productVersionId") 
	public ProductVersionListMaster getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersionListMaster productVersion) {
		this.productVersion = productVersion;
	}*/


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;	
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;		
	}

	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getIterationNumber() {
		return iterationNumber;
	}

	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	public String getLifeCyclePhase() {
		return lifeCyclePhase;
	}

	public void setLifeCyclePhase(String lifeCyclePhase) {
		this.lifeCyclePhase = lifeCyclePhase;
	}
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getTotalJobs() {
		return totalJobs;
	}

	public void setTotalJobs(Integer totalJobs) {
		this.totalJobs = totalJobs;
	}
	public Integer getTotalTestCases() {
		return totalTestCases;
	}

	public void setTotalTestCases(Integer totalTestCases) {
		this.totalTestCases = totalTestCases;
	}
	public Integer getPassedTestCases() {
		return passedTestCases;
	}

	public void setPassedTestCases(Integer passedTestCases) {
		this.passedTestCases = passedTestCases;
	}
	public Integer getTotalTestSteps() {
		return totalTestSteps;
	}

	public void setTotalTestSteps(Integer totalTestSteps) {
		this.totalTestSteps = totalTestSteps;
	}
	public Integer getPassedTestSteps() {
		return passedTestSteps;
	}

	public void setPassedTestSteps(Integer passedTestSteps) {
		this.passedTestSteps = passedTestSteps;
	}

	public Long getExecutionDuration() {
		return executionDuration;
	}

	public void setExecutionDuration(Long executionDuration) {
		this.executionDuration = executionDuration;
	}
	public Set<TestRunJobBean> getJob() {
		return job;
	}

	public void setJob(Set<TestRunJobBean> job) {
		this.job = job;
	}
	public Set<TestCaseList> getTestcaseList() {
		return testcaseList;
	}


	public void setTestcaseList(Set<TestCaseList> testcaseList) {
		this.testcaseList = testcaseList;
	}


	public Integer getJobsCount() {
		return jobsCount;
	}


	public void setJobsCount(Integer jobsCount) {
		this.jobsCount = jobsCount;
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


	public Integer getProdcutVersionId() {
		return prodcutVersionId;
	}


	public void setProdcutVersionId(Integer prodcutVersionId) {
		this.prodcutVersionId = prodcutVersionId;
	}


	public String getProductVersion() {
		return productVersion;
	}


	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}


	public Integer getProductBuildId() {
		return productBuildId;
	}


	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}


	public String getProductBuild() {
		return productBuild;
	}


	public void setProductBuild(String productBuild) {
		this.productBuild = productBuild;
	}


	public Integer getTestPlanId() {
		return testPlanId;
	}


	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}


	public String getTestPlanName() {
		return testPlanName;
	}


	public void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}


	public String getTestTool() {
		return testTool;
	}


	public void setTestTool(String testTool) {
		this.testTool = testTool;
	}


	public String getScriptType() {
		return scriptType;
	}


	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getExecutedTCCount() {
		return executedTCCount;
	}

	public void setExecutedTCCount(Integer executedTCCount) {
		this.executedTCCount = executedTCCount;
	}

	public Integer getNotExecutedTCCount() {
		return notExecutedTCCount;
	}

	public void setNotExecutedTCCount(Integer notExecutedTCCount) {
		this.notExecutedTCCount = notExecutedTCCount;
	}


}
