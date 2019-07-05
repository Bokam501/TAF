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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "workpackage_testsuite_execution_plan")
public class WorkPackageTestSuiteExecutionPlan  implements java.io.Serializable{
	
	private Integer id;
	@Column(name="plannedExecutionDate")
	private Date plannedExecutionDate;
	@Column(name="actualExecutionDate")
	private Date actualExecutionDate;
	@Column(name="createdDate")
	private Date createdDate;
	@Column(name="modifiedDate")
	private Date modifiedDate;
	private WorkPackage workPackage;
	private UserList testLead;
	private UserList tester;
	private WorkShiftMaster plannedWorkShiftMaster;
	private WorkShiftMaster actualWorkShiftMaster;
	private Set<TestCaseConfiguration>  testCaseConfigurationSet=new HashSet<TestCaseConfiguration>(0);
	private WorkpackageRunConfiguration runConfiguration;
	private HostList hostList;
	private TestSuiteList testsuite;
	private ExecutionPriority executionPriority;
	private Integer status;
	private TestRunJob testRunJob;
	
public WorkPackageTestSuiteExecutionPlan  () {
}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "wptspId", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId") 
	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

		
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="testLeadId")
	public UserList getTestLead() {
		return testLead;
	}

	public void setTestLead(UserList testLead) {
		this.testLead = testLead;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="testerId")
	public UserList getTester() {
		return tester;
	}

	public void setTester(UserList tester) {
		this.tester = tester;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(Date plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}

	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "plannedShiftId") 
	public WorkShiftMaster getPlannedWorkShiftMaster() {
		return plannedWorkShiftMaster;
	}

	public void setPlannedWorkShiftMaster(WorkShiftMaster plannedWorkShiftMaster) {
		this.plannedWorkShiftMaster = plannedWorkShiftMaster;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "actualShiftId") 
	public WorkShiftMaster getActualWorkShiftMaster() {
		return actualWorkShiftMaster;
	}

	public void setActualWorkShiftMaster(WorkShiftMaster actualWorkShiftMaster) {
		this.actualWorkShiftMaster = actualWorkShiftMaster;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workpackage_run_list",cascade=CascadeType.ALL)
	public Set<TestCaseConfiguration> getTestCaseConfigurationSet() {
		return testCaseConfigurationSet;
	}

	public void setTestCaseConfigurationSet(
			Set<TestCaseConfiguration> testCaseConfigurationSet) {
		this.testCaseConfigurationSet = testCaseConfigurationSet;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "testRunConfigurationId") 
	public WorkpackageRunConfiguration getRunConfiguration() {
		return runConfiguration;
	}

	public void setRunConfiguration(WorkpackageRunConfiguration runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

		

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "hostId") 
	public HostList getHostList() {
		return hostList;
	}

	public void setHostList(HostList hostList) {
		this.hostList = hostList;
	}

	

	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testSuiteId") 
	public TestSuiteList getTestsuite() {
		return testsuite;
	}

	public void setTestsuite(TestSuiteList testsuite) {
		this.testsuite = testsuite;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "executionPriorityId", nullable=true) 
	@NotFound(action=NotFoundAction.IGNORE)
	public ExecutionPriority getExecutionPriority() {
		return executionPriority;
	}

	public void setExecutionPriority(ExecutionPriority executionPriority) {
		this.executionPriority = executionPriority;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunJobId") 
	public TestRunJob getTestRunJob() {
		return testRunJob;
	}

	public void setTestRunJob(TestRunJob testRunJob) {
		this.testRunJob = testRunJob;
	}
	
}
