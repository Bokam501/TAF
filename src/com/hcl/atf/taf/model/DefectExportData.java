package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DefectExportData  - Details of defect, Defect System Id, TAF Bug Id , Defect System Bug id are maintained.
 */
@Entity
@Table(name = "defect_export_data")
public class DefectExportData implements java.io.Serializable {

	
	private Integer defectExportDataId;
	private Integer defectManagementSystemId;
	private Integer testExecutionResultsBugId;
	private String defectSystemCode;
	private Date defectExportDate;

	public DefectExportData(){
		
	}

	public DefectExportData(Integer defectExportDataId, Integer defectManagementSystemId, Integer testExecutionResultsBugId,String defectSystemCode,Date defectExportDate){
		this.defectExportDataId = defectExportDataId;
		this.defectManagementSystemId = defectManagementSystemId;
		this.testExecutionResultsBugId = testExecutionResultsBugId;
		this.defectSystemCode = defectSystemCode;
		this.defectExportDate = defectExportDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "defectExportDataId", unique = true, nullable = false)
	public Integer getDefectExportDataId() {
		return defectExportDataId;
	}

	public void setDefectExportDataId(Integer defectExportDataId) {
		this.defectExportDataId = defectExportDataId;
	}

	@Column(name = "defectManagementSystemId")
	public Integer getDefectManagementSystemId() {
		return defectManagementSystemId;
	}

	public void setDefectManagementSystemId(Integer defectManagementSystemId) {
		this.defectManagementSystemId = defectManagementSystemId;
	}

	@Column(name = "testExecutionResultsBugId")
	public Integer getTestExecutionResultsBugId() {
		return testExecutionResultsBugId;
	}

	public void setTestExecutionResultsBugId(Integer testExecutionResultsBugId) {
		this.testExecutionResultsBugId = testExecutionResultsBugId;
	}

	@Column(name = "defectSystemCode")
	public String getDefectSystemCode() {
		return defectSystemCode;
	}

	public void setDefectSystemCode(String defectSystemCode) {
		this.defectSystemCode = defectSystemCode;
	}

	@Column(name = "defectExportDate")
	public Date getDefectExportDate() {
		return defectExportDate;
	}

	public void setDefectExportDate(Date defectExportDate) {
		this.defectExportDate = defectExportDate;
	}
	
}
