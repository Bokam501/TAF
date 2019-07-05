package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TestExecutionResultsExportData  - Details of Test Result, Test Management System Id, TAF test execution Result Id ,
 * test management System  result id are maintained.
 */
@Entity
@Table(name = "test_execution_results_export_data")
public class TestExecutionResultsExportData implements java.io.Serializable {

	
	private Integer testExecutionResultExportId;
	private Integer testExecutionsResultId;
	private Integer testManagementSystemId;
	private String resultCode;
	private Date exportedDate;

	public TestExecutionResultsExportData(){
		
	}

	public TestExecutionResultsExportData(Integer testExecutionResultExportId, Integer testExecutionsResultId, Integer testManagementSystemId,String resultCode,Date exportedDate){
		this.testExecutionResultExportId = testExecutionResultExportId;
		this.testExecutionsResultId = testExecutionsResultId;
		this.testManagementSystemId = testManagementSystemId;
		this.resultCode = resultCode;
		this.exportedDate = exportedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testExecutionResultExportId", unique = true, nullable = false)
	public Integer getTestExecutionResultExportId() {
		return this.testExecutionResultExportId;
	}

	public void setTestExecutionResultExportId(Integer testExecutionResultExportId) {
		this.testExecutionResultExportId = testExecutionResultExportId;
	}

	@Column(name = "testExecutionsResultId")
	public Integer getTestExecutionsResultId() {
		return testExecutionsResultId;
	}

	public void setTestExecutionsResultId(Integer testExecutionsResultId) {
		this.testExecutionsResultId = testExecutionsResultId;
	}

	@Column(name = "testManagementSystemId")
	public Integer getTestManagementSystemId() {
		return testManagementSystemId;
	}

	public void setTestManagementSystemId(Integer testManagementSystemId) {
		this.testManagementSystemId = testManagementSystemId;
	}

	@Column(name = "resultCode")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Column(name = "exportedDate")
	public Date getExportedDate() {
		return exportedDate;
	}

	public void setExportedDate(Date exportedDate) {
		this.exportedDate = exportedDate;
	}

}
