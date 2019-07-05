package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "data_extractor_schedule")
public class DataExtractorScheduleMaster implements Serializable {

	private static final long serialVersionUID = 378236606538876680L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "jobName")
	private String jobName;
	
	@Column(name = "groupName")
	private String groupName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "engagement")
	private TestFactory engagement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competency", nullable = true)
	private DimensionMaster competency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product", nullable = true)
	private ProductMaster product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "extractorType")
	private ExtractorTypeMaster extractorType;
	
	@Column(name = "extractorName")
	private String extractorName;
	
	@Column(name = "fileLocation")
	private String fileLocation;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "cronExpression")
	private String cronExpression;
	
	@Column(name = "startDate")
	private Date startDate;
	
	@Column(name = "endDate")
	private Date endDate;
	
	@Column(name = "lastExecuted")
	private Date lastExecuted;
	
	@Column(name = "nextExecution")
	private Date nextExecution;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updatedDate")
	private Date updatedDate;
	
	@Column(name = "status")
	private Integer status;

	@Transient
	private Boolean isStopProcess;

	@Transient
	private Boolean isExctractorTypeFound;
	
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public TestFactory getEngagement() {
		return engagement;
	}

	public void setEngagement(TestFactory engagement) {
		this.engagement = engagement;
	}

	public DimensionMaster getCompetency() {
		return competency;
	}

	public void setCompetency(DimensionMaster competency) {
		this.competency = competency;
	}

	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	public ExtractorTypeMaster getExtractorType() {
		return extractorType;
	}

	public void setExtractorType(ExtractorTypeMaster extractorType) {
		this.extractorType = extractorType;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(Date lastExecuted) {
		this.lastExecuted = lastExecuted;
	}

	public Date getNextExecution() {
		return nextExecution;
	}

	public void setNextExecution(Date nextExecution) {
		this.nextExecution = nextExecution;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsStopProcess() {
		return isStopProcess;
	}

	public void setIsStopProcess(Boolean isStopProcess) {
		this.isStopProcess = isStopProcess;
	}

	public Boolean getIsExctractorTypeFound() {
		return isExctractorTypeFound;
	}

	public void setIsExctractorTypeFound(Boolean isExctractorTypeFound) {
		this.isExctractorTypeFound = isExctractorTypeFound;
	}

}
