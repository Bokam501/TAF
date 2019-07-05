package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="data_extraction_report_summary")
public class DataExtractionReportSummary {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "dataExtractorId", unique= true, nullable = false)
	private int dataExtractorId;
	
	@Column(name = "fileName")
	private String fileName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "productId")
	private ProductMaster productId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "extractorTypeId")
	private ExtractorTypeMaster extractorTypeId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "extractorScheduleId")
	private DataExtractorScheduleMaster extractorScheduleId;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "extractionDate")
	private Date extractionDate;
	
	@Column(name = "extractedBy")
	private String extractedBy;
	
	@Column(name = "totalRecords")
	private int totalRecords;
	
	@Column(name = "successfulRecords")
	private int successfulRecords;
	
	@Column(name = "failedRecords")
	private int failedRecords;
	
	@Column(name = "extractionStatus")
	private String extractionStatus;

	public int getDataExtractorId() {
		return dataExtractorId;
	}

	public void setDataExtractorId(int dataExtractorId) {
		this.dataExtractorId = dataExtractorId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ProductMaster getProductId() {
		return productId;
	}

	public void setProductId(ProductMaster productId) {
		this.productId = productId;
	}

	public ExtractorTypeMaster getExtractorTypeId() {
		return extractorTypeId;
	}

	public void setExtractorTypeId(ExtractorTypeMaster extractorTypeId) {
		this.extractorTypeId = extractorTypeId;
	}

	public DataExtractorScheduleMaster getExtractorScheduleId() {
		return extractorScheduleId;
	}

	public void setExtractorScheduleId(
			DataExtractorScheduleMaster extractorScheduleId) {
		this.extractorScheduleId = extractorScheduleId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExtractionDate() {
		return extractionDate;
	}

	public void setExtractionDate(Date extractionDate) {
		this.extractionDate = extractionDate;
	}

	public String getExtractedBy() {
		return extractedBy;
	}

	public void setExtractedBy(String extractedBy) {
		this.extractedBy = extractedBy;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getSuccessfulRecords() {
		return successfulRecords;
	}

	public void setSuccessfulRecords(int successfulRecords) {
		this.successfulRecords = successfulRecords;
	}

	public int getFailedRecords() {
		return failedRecords;
	}

	public void setFailedRecords(int failedRecords) {
		this.failedRecords = failedRecords;
	}

	public String getExtractionStatus() {
		return extractionStatus;
	}

	public void setExtractionStatus(String extractionStatus) {
		this.extractionStatus = extractionStatus;
	}
	
}
