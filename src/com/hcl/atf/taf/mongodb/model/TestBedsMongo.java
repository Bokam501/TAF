package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testbeds")
public class TestBedsMongo {
	@Id
	private String env_name;
	private String description;
	private String distribution;
	private String env_type;
	private String created_date;
	private Integer genericDeviceId;
	private Integer productId;
	private Integer productVersionId;
	private Integer	runconfigNameId;
	private Integer workPackageId;
	private String testRunPlanSet;
	private String hostListId;	
	
	public TestBedsMongo(){
		
	}
	
	public TestBedsMongo(String env_name, String description, String distribution, String env_type, String created_date, 
			Integer genericDeviceId, Integer productId, Integer productVersionId, Integer runconfigNameId, 
			Integer workPackageId, String testRunPlanSet, String hostListId){
		this.env_name = env_name;
		this.description = description;
		this.distribution = distribution;
		this.env_type = env_type;
		this.created_date = created_date;
		this.genericDeviceId = genericDeviceId;
		this.productId= productId;
		this.productVersionId = productVersionId;
		this.runconfigNameId = runconfigNameId;
		this.workPackageId = workPackageId;
		this.testRunPlanSet = testRunPlanSet;
		this.hostListId = hostListId;
	}

	public String getEnv_name() {
		return env_name;
	}

	public void setEnv_name(String env_name) {
		this.env_name = env_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getEnv_type() {
		return env_type;
	}

	public void setEnv_type(String env_type) {
		this.env_type = env_type;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public Integer getGenericDeviceId() {
		return genericDeviceId;
	}

	public void setGenericDeviceId(Integer genericDeviceId) {
		this.genericDeviceId = genericDeviceId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}

	public Integer getRunconfigNameId() {
		return runconfigNameId;
	}

	public void setRunconfigNameId(Integer runconfigNameId) {
		this.runconfigNameId = runconfigNameId;
	}

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getTestRunPlanSet() {
		return testRunPlanSet;
	}

	public void setTestRunPlanSet(String testRunPlanSet) {
		this.testRunPlanSet = testRunPlanSet;
	}

	public String getHostListId() {
		return hostListId;
	}

	public void setHostListId(String hostListId) {
		this.hostListId = hostListId;
	}

	@Override
    public String toString(){
	return env_name+":"+description+":"+distribution+":"+env_type+":"+created_date+":"+genericDeviceId+":"+ productId+":"+
			productVersionId+":"+runconfigNameId+":"+workPackageId+":"+testRunPlanSet+":"+hostListId;
    }	
	
	
}
