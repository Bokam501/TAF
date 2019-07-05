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
@Table(name = "runconfiguration")
public class RunConfiguration {
	private Integer	runconfigId;
	private EnvironmentCombination environmentcombination;
	private GenericDevices genericDevice;
	private ProductMaster product;
	private ProductVersionListMaster productVersion;
	private String	runconfigName;
	private WorkPackage workPackage;
	private TestRunPlan testRunPlan;

	private Set<TestRunPlan> testRunPlanSet=new HashSet<TestRunPlan>(0);
	private Set<WorkpackageRunConfiguration> workPackageRunConfigSet = new HashSet<WorkpackageRunConfiguration>(0);
	private HostList hostList;
	private Set<TestRunJob> testRunJobSet=new HashSet<TestRunJob>();
	private Integer runConfigStatus;
	
	private TestToolMaster testTool;
	private Set<TestSuiteList> testSuiteLists;
	private ScriptTypeMaster scriptTypeMaster;
	private String testScriptFileLocation;
	private ProductType productType;
	private String copyTestPlanTestSuite;
	
	public RunConfiguration () {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "runconfigId", unique = true, nullable = false)
	public Integer getRunconfigId() {
		return runconfigId;
	}

	public void setRunconfigId(Integer runconfigId) {
		this.runconfigId = runconfigId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "environmentcombinationId")
	public EnvironmentCombination getEnvironmentcombination() {
		return environmentcombination;
	}

	public void setEnvironmentcombination(
			EnvironmentCombination environmentcombination) {
		this.environmentcombination = environmentcombination;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "deviceId")
	public GenericDevices getGenericDevice() {
		return genericDevice;
	}
	public void setGenericDevice(GenericDevices genericDevice) {
		this.genericDevice = genericDevice;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	@Column(name = "runconfigName")
	public String getRunconfigName() {
		return runconfigName;
	}
	public void setRunconfigName(String runconfigName) {
		this.runconfigName = runconfigName;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "runConfigurationList",cascade=CascadeType.ALL)
	public  Set<TestRunPlan> getTestRunPlanSet() {
		return testRunPlanSet;
	}
	public  void setTestRunPlanSet(Set<TestRunPlan> testRunPlanSet) {
		this.testRunPlanSet = testRunPlanSet;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productVersionId")
	public  ProductVersionListMaster getProductVersion() {
		return productVersion;
	}
	public  void setProductVersion(ProductVersionListMaster productVersion) {
		this.productVersion = productVersion;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "runconfiguration",cascade=CascadeType.ALL)
	public Set<WorkpackageRunConfiguration> getWorkPackageRunConfigSet() {
		return workPackageRunConfigSet;
	}

	public void setWorkPackageRunConfigSet(
			Set<WorkpackageRunConfiguration> workPackageRunConfigSet) {
		this.workPackageRunConfigSet = workPackageRunConfigSet;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workpackageId")
	public WorkPackage getWorkPackage() {
		return workPackage;
	}
	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunPlanId")
	public TestRunPlan getTestRunPlan() {
		return testRunPlan;
	}
	public void setTestRunPlan(TestRunPlan testRunPlan) {
		this.testRunPlan = testRunPlan;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hostId")
	public HostList getHostList() {
		return hostList;
	}
	public void setHostList(HostList hostList) {
		this.hostList = hostList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "runConfiguration",cascade=CascadeType.ALL)
	public Set<TestRunJob> getTestRunJobSet() {
		return testRunJobSet;
	}
	public void setTestRunJobSet(Set<TestRunJob> testRunJobSet) {
		this.testRunJobSet = testRunJobSet;
	}
	@Column(name = "runConfigStatus")
	public Integer getRunConfigStatus() {
		return runConfigStatus;
	}
	public void setRunConfigStatus(Integer runConfigStatus) {
		this.runConfigStatus = runConfigStatus;
	}

	@Override
	public boolean equals(Object o) {
		RunConfiguration runConfiguration = (RunConfiguration) o;
		if (this.runconfigId.equals(runConfiguration.getRunconfigId())) {
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int hashCode(){
		return (int) runconfigId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testToolId")
	public TestToolMaster getTestTool() {
		return testTool;
	}
	public void setTestTool(TestToolMaster testTool) {
		this.testTool = testTool;
	}

	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "runconfiguration_has_testsuite", joinColumns = { @JoinColumn(name = "runConfigurationId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testSuiteId", nullable = false, updatable = false) })
	public  Set<TestSuiteList> getTestSuiteLists() {
		return testSuiteLists;
	}
	public  void setTestSuiteLists(Set<TestSuiteList> testSuiteLists) {
		this.testSuiteLists = testSuiteLists;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scriptType")
	public ScriptTypeMaster getScriptTypeMaster() {
		return scriptTypeMaster;
	}

	public void setScriptTypeMaster(ScriptTypeMaster scriptTypeMaster) {
		this.scriptTypeMaster = scriptTypeMaster;
	}
	
	@Column(name = "testScriptFileLocation")
	public String getTestScriptFileLocation() {
		return testScriptFileLocation;
	}
	public void setTestScriptFileLocation(String testScriptFileLocation) {
		this.testScriptFileLocation = testScriptFileLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productTypeId")
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@Column(name="copyTestPlanTestSuite")
	public String getCopyTestPlanTestSuite() {
		return copyTestPlanTestSuite;
	}

	public void setCopyTestPlanTestSuite(String copyTestPlanTestSuite) {
		this.copyTestPlanTestSuite = copyTestPlanTestSuite;
	}

}
