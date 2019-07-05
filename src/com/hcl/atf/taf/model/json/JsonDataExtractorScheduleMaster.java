package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.ExtractorTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;

public class JsonDataExtractorScheduleMaster implements Serializable {

	private static final long serialVersionUID = 378236606538876680L;
	
	@JsonProperty
	private Integer id;
	
	@JsonProperty
	private String jobName;
	
	@JsonProperty
	private String groupName;
	
	@JsonProperty
	private Integer customerId;
	
	@JsonProperty
	private String customerName;
	
	@JsonProperty
	private Integer engagementId;
	
	@JsonProperty
	private String engagementName;
	
	@JsonProperty
	private Integer competencyId;
	
	@JsonProperty
	private String competencyName;
	
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private Integer extractorTypeId;
	
	@JsonProperty
	private String extractorTypeName;
	
	@JsonProperty
	private String extractorName;
	
	@JsonProperty
	private String fileLocation;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String cronExpression;
	
	@JsonProperty
	private String startDate;
	
	@JsonProperty
	private String endDate;
	
	@JsonProperty
	private String lastExecuted;
	
	@JsonProperty
	private String nextExecution;
	
	@JsonProperty
	private String createdDate;
	
	@JsonProperty
	private String updatedDate;
	
	@JsonProperty
	private Integer status;

	@JsonProperty
	private Integer attachmentCount;
	
	public JsonDataExtractorScheduleMaster(){
		
	}
	
	public JsonDataExtractorScheduleMaster(DataExtractorScheduleMaster dataExtractorScheduleMaster){
		
		this.id = dataExtractorScheduleMaster.getId();
		this.jobName = dataExtractorScheduleMaster.getJobName();
		this.groupName = dataExtractorScheduleMaster.getGroupName();
		this.customerId = dataExtractorScheduleMaster.getCustomer().getCustomerId();
		this.customerName = dataExtractorScheduleMaster.getCustomer().getCustomerName();
		if(dataExtractorScheduleMaster.getEngagement() != null){
			this.engagementId = dataExtractorScheduleMaster.getEngagement().getTestFactoryId();
			this.engagementName = dataExtractorScheduleMaster.getEngagement().getTestFactoryName();
		}else{
			this.engagementId = 0;
			this.engagementName = " -- ";
		}
		this.competencyId = dataExtractorScheduleMaster.getCompetency().getDimensionId();
		this.competencyName = dataExtractorScheduleMaster.getCompetency().getName();
		if(dataExtractorScheduleMaster.getProduct() != null){
			this.productId = dataExtractorScheduleMaster.getProduct().getProductId();
			this.productName = dataExtractorScheduleMaster.getProduct().getProductName();
		}else{
			this.productId = 0;
			this.productName = " -- ";
		}
		if(dataExtractorScheduleMaster.getExtractorType() != null) {
			this.extractorTypeId = dataExtractorScheduleMaster.getExtractorType().getId();
			this.extractorTypeName = dataExtractorScheduleMaster.getExtractorType().getExtarctorName();
		}
		this.extractorName = dataExtractorScheduleMaster.getExtractorName();
		this.fileLocation = dataExtractorScheduleMaster.getFileLocation();
		this.description = dataExtractorScheduleMaster.getDescription();
		this.cronExpression = dataExtractorScheduleMaster.getCronExpression();
		if(dataExtractorScheduleMaster.getStartDate() != null){
			this.startDate = DateUtility.dateformatWithOutTime(dataExtractorScheduleMaster.getStartDate());
		}
		if(dataExtractorScheduleMaster.getEndDate() != null){
			this.endDate = DateUtility.dateformatWithOutTime(dataExtractorScheduleMaster.getEndDate());
		}
		if(dataExtractorScheduleMaster.getLastExecuted() != null){
			this.lastExecuted = DateUtility.dateToStringWithSeconds1(dataExtractorScheduleMaster.getLastExecuted());
		}
		if(dataExtractorScheduleMaster.getNextExecution() != null){
			this.nextExecution = DateUtility.dateToStringWithSeconds1(dataExtractorScheduleMaster.getNextExecution());
		}
		if(dataExtractorScheduleMaster.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(dataExtractorScheduleMaster.getCreatedDate());
		}
		if(dataExtractorScheduleMaster.getUpdatedDate() != null){
			this.updatedDate = DateUtility.dateToStringWithSeconds1(dataExtractorScheduleMaster.getUpdatedDate());
		}
		this.status = dataExtractorScheduleMaster.getStatus();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(Integer engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(Integer competencyId) {
		this.competencyId = competencyId;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
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

	public Integer getExtractorTypeId() {
		return extractorTypeId;
	}

	public void setExtractorTypeId(Integer extractorTypeId) {
		this.extractorTypeId = extractorTypeId;
	}

	public String getExtractorTypeName() {
		return extractorTypeName;
	}

	public void setExtractorTypeName(String extractorTypeName) {
		this.extractorTypeName = extractorTypeName;
	}

	public String getExtractorName() {
		return extractorName;
	}

	public void setExtractorName(String extractorName) {
		this.extractorName = extractorName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(String lastExecuted) {
		this.lastExecuted = lastExecuted;
	}

	public String getNextExecution() {
		return nextExecution;
	}

	public void setNextExecution(String nextExecution) {
		this.nextExecution = nextExecution;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	@JsonIgnore
	public DataExtractorScheduleMaster getDataExtractorScheduleMaster() {
		
		DataExtractorScheduleMaster dataExtractorScheduleMaster = new DataExtractorScheduleMaster();
		dataExtractorScheduleMaster.setId(this.id);
		dataExtractorScheduleMaster.setJobName(this.jobName);
		dataExtractorScheduleMaster.setGroupName(this.groupName);
		
		Customer customer = new Customer();
		customer.setCustomerId(this.customerId);
		dataExtractorScheduleMaster.setCustomer(customer);
		
		TestFactory testFactory = new TestFactory();
		testFactory.setTestFactoryId(engagementId);
		dataExtractorScheduleMaster.setEngagement(testFactory);
		
		DimensionMaster competency = new DimensionMaster();
		if(this.competencyId != null && this.competencyId > 1){
			competency.setDimensionId(this.competencyId);
		}else{
			competency.setDimensionId(1);
		}
		dataExtractorScheduleMaster.setCompetency(competency);
		
		if(this.productId != null && this.productId > 0){
			ProductMaster product = new ProductMaster();
			product.setProductId(this.productId);
			dataExtractorScheduleMaster.setProduct(product);
		}
		
		ExtractorTypeMaster extractorTypeMaster = new ExtractorTypeMaster();
		extractorTypeMaster.setId(this.extractorTypeId);
		dataExtractorScheduleMaster.setExtractorType(extractorTypeMaster);
				
		dataExtractorScheduleMaster.setExtractorName(this.extractorName);
		dataExtractorScheduleMaster.setFileLocation(this.fileLocation);
		dataExtractorScheduleMaster.setDescription(this.description);
		dataExtractorScheduleMaster.setCronExpression(this.cronExpression);
		if(this.startDate != null && !this.startDate.trim().isEmpty()){
			dataExtractorScheduleMaster.setStartDate(DateUtility.dateformatWithOutTime(this.startDate));
		}
		if(this.endDate != null && !this.endDate.trim().isEmpty()){
			dataExtractorScheduleMaster.setEndDate(DateUtility.dateformatWithOutTime(this.endDate));
		}
		if(this.lastExecuted != null && !this.lastExecuted.trim().isEmpty()){
			dataExtractorScheduleMaster.setLastExecuted(DateUtility.toFormatDate(this.lastExecuted));
		}
		if(this.nextExecution != null && !this.nextExecution.trim().isEmpty()){
			dataExtractorScheduleMaster.setNextExecution(DateUtility.toFormatDate(this.nextExecution));
		}
		if(this.createdDate != null && !this.createdDate.trim().isEmpty()){
			dataExtractorScheduleMaster.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}else{
			dataExtractorScheduleMaster.setCreatedDate(new Date());
		}
		dataExtractorScheduleMaster.setUpdatedDate(new Date());
		dataExtractorScheduleMaster.setStatus(this.status);
		
		return dataExtractorScheduleMaster;
	}
}
