package com.hcl.atf.taf.model.dto;


public class ImportFileValidationDTO {
	private String fileType;
	private Integer numberOfCols;
	private Integer finalTotalRows;
	private Integer validCount;	
	private Integer invalidCount;
	private float completnessPercentage;
	private Integer mandatoryExcelColCount;
	private Integer validElementColSize;
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Integer getNumberOfCols() {
		return numberOfCols;
	}
	public void setNumberOfCols(Integer numberOfCols) {
		this.numberOfCols = numberOfCols;
	}
	public Integer getFinalTotalRows() {
		return finalTotalRows;
	}
	public void setFinalTotalRows(Integer finalTotalRows) {
		this.finalTotalRows = finalTotalRows;
	}
	public Integer getValidCount() {
		return validCount;
	}
	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}
	public Integer getInvalidCount() {
		return invalidCount;
	}
	public void setInvalidCount(Integer invalidCount) {
		this.invalidCount = invalidCount;
	}
	public float getCompletnessPercentage() {
		return completnessPercentage;
	}
	public void setCompletnessPercentage(float completnessPercentage) {
		this.completnessPercentage = completnessPercentage;
	}
	public Integer getMandatoryExcelColCount() {
		return mandatoryExcelColCount;
	}
	public void setMandatoryExcelColCount(Integer mandatoryExcelColCount) {
		this.mandatoryExcelColCount = mandatoryExcelColCount;
	}
	public Integer getValidElementColSize() {
		return validElementColSize;
	}
	public void setValidElementColSize(Integer validElementColSize) {
		this.validElementColSize = validElementColSize;
	}
	
}
