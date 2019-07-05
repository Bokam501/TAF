package com.hcl.atf.taf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataExtractorNotification {

	private String productName;
	private String filePath;
	private String extractorType;
	private List<String> messageList;
	private boolean isFileFailed;
	private boolean isConfigurationFailed;
	private boolean isExtractionSuccess;
	private boolean isDataProcessingFailed;
	private Integer totalNumberOfRecords;
	private Integer numberOfValidRecords;
	private Integer numberOfInvalidRecords;
	private Date createdOrModifiedDate;
	private Date extarctionDate;
	
	public DataExtractorNotification(){
		productName = "--";
		filePath = "--";
		extractorType = "--";
		messageList = new ArrayList<String>();
		isFileFailed = false;
		isConfigurationFailed = false;
		isExtractionSuccess = false;
		isDataProcessingFailed = false;
		totalNumberOfRecords = 0;
		numberOfValidRecords = 0;
		numberOfInvalidRecords = 0;
		createdOrModifiedDate = null;
		extarctionDate = null;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getExtractorType() {
		return extractorType;
	}
	public void setExtractorType(String extractorType) {
		this.extractorType = extractorType;
	}
	public List<String> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}
	public boolean isFileFailed() {
		return isFileFailed;
	}
	public void setFileFailed(boolean isFileFailed) {
		this.isFileFailed = isFileFailed;
	}
	public boolean isConfigurationFailed() {
		return isConfigurationFailed;
	}
	public void setConfigurationFailed(boolean isConfigurationFailed) {
		this.isConfigurationFailed = isConfigurationFailed;
	}
	public boolean isExtractionSuccess() {
		return isExtractionSuccess;
	}
	public void setExtractionSuccess(boolean isExtractionSuccess) {
		this.isExtractionSuccess = isExtractionSuccess;
	}

	public boolean isDataProcessingFailed() {
		return isDataProcessingFailed;
	}

	public void setDataProcessingFailed(boolean isDataProcessingFailed) {
		this.isDataProcessingFailed = isDataProcessingFailed;
	}

	public Integer getTotalNumberOfRecords() {
		return totalNumberOfRecords;
	}

	public void setTotalNumberOfRecords(Integer totalNumberOfRecords) {
		this.totalNumberOfRecords = totalNumberOfRecords;
	}

	public Integer getNumberOfValidRecords() {
		return numberOfValidRecords;
	}

	public void setNumberOfValidRecords(Integer numberOfValidRecords) {
		this.numberOfValidRecords = numberOfValidRecords;
	}

	public Integer getNumberOfInvalidRecords() {
		return numberOfInvalidRecords;
	}

	public void setNumberOfInvalidRecords(Integer numberOfInvalidRecords) {
		this.numberOfInvalidRecords = numberOfInvalidRecords;
	}

	public Date getCreatedOrModifiedDate() {
		return createdOrModifiedDate;
	}

	public void setCreatedOrModifiedDate(Date createdOrModifiedDate) {
		this.createdOrModifiedDate = createdOrModifiedDate;
	}

	public Date getExtarctionDate() {
		return extarctionDate;
	}

	public void setExtarctionDate(Date extarctionDate) {
		this.extarctionDate = extarctionDate;
	}
	
}
