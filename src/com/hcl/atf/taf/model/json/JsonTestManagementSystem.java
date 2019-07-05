package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.SystemType;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;


public class JsonTestManagementSystem implements java.io.Serializable {

	@JsonProperty
	private Integer testManagementSystemId;
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private String title;
	@JsonProperty
	private String description;
	@JsonProperty
	private String connectionUri;
	
	@JsonProperty
	private String connectionUserName;
	@JsonProperty
	private String connectionPassword;
	
	@JsonProperty
	private String connectionProperty1;
	
	@JsonProperty
	private String connectionProperty2;
	
	@JsonProperty
	private String connectionProperty3;
	
	@JsonProperty
	private String connectionProperty4;
	@JsonProperty
	private String connectionProperty5;//Changes for Bug 792 - Adding TestSetFilter by name 
	/*@JsonProperty
	private String testSystemName;*/
	@JsonProperty
	private String testSystemVersion;
	@JsonProperty
	private Integer testSystemTypeId;
	@JsonProperty
	private Integer isPrimary;
	@JsonProperty
	private String ResultCode;
	//@JsonProperty
	//private String TestSystemName;
	@JsonProperty
	private String exportedDate;	
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;	
	@JsonProperty	
	private String modifiedFieldValue;
	
	@JsonProperty	
	private Integer toolIntagrationId;
	
	@JsonProperty	
	private String toolIntagrationName;
	
	
	public JsonTestManagementSystem() {
	}


	public JsonTestManagementSystem(TestManagementSystem testManagementSystem) {
		
		this.testManagementSystemId =testManagementSystem.getTestManagementSystemId();
		this.productId = testManagementSystem.getProductMaster().getProductId();

	if(testManagementSystem.getSystemType()!=null)
	{
		this.testSystemTypeId=testManagementSystem.getSystemType().getSystemTypeId();
	}

		this.isPrimary=testManagementSystem.getIsPrimary();
		this.title=testManagementSystem.getTitle();
		this.description=testManagementSystem.getDescription();
		this.connectionUri=testManagementSystem.getConnectionUri();
		this.connectionUserName=testManagementSystem.getConnectionUserName();
		this.connectionPassword=testManagementSystem.getConnectionPassword();
		this.connectionProperty1=testManagementSystem.getConnectionProperty1();
		this.connectionProperty2=testManagementSystem.getConnectionProperty2();
		this.connectionProperty3=testManagementSystem.getConnectionProperty3();
		this.connectionProperty4=testManagementSystem.getConnectionProperty4();
		this.connectionProperty4=testManagementSystem.getConnectionProperty5();//Changes for Bug 792 - Adding TestSetFilter by name
		if(testManagementSystem.getToolIntagration() != null) {
			this.toolIntagrationId = testManagementSystem.getToolIntagration().getId();
			this.toolIntagrationName = testManagementSystem.getToolIntagration().getName();
		}
		//this.testSystemName=testManagementSystem.getTestSystemName();
		this.testSystemVersion=testManagementSystem.getTestSystemVersion();	
	}

	public Integer getTestManagementSystemId() {
		return testManagementSystemId;
	}


	public void setTestManagementSystemId(Integer testManagementSystemId) {
		this.testManagementSystemId = testManagementSystemId;
	}


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
	}




	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getConnectionUri() {
		return connectionUri;
	}


	public void setConnectionUri(String connectionUri) {
		this.connectionUri = connectionUri;
	}


	public String getConnectionUserName() {
		return connectionUserName;
	}


	public void setConnectionUserName(String connectionUserName) {
		this.connectionUserName = connectionUserName;
	}


	public String getConnectionPassword() {
		return connectionPassword;
	}


	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}


	public String getConnectionProperty1() {
		return connectionProperty1;
	}


	public void setConnectionProperty1(String connectionProperty1) {
		this.connectionProperty1 = connectionProperty1;
	}


	public String getConnectionProperty2() {
		return connectionProperty2;
	}


	public void setConnectionProperty2(String connectionProperty2) {
		this.connectionProperty2 = connectionProperty2;
	}


	public String getConnectionProperty3() {
		return connectionProperty3;
	}


	public void setConnectionProperty3(String connectionProperty3) {
		this.connectionProperty3 = connectionProperty3;
	}


	public String getConnectionProperty4() {
		return connectionProperty4;
	}


	public void setConnectionProperty4(String connectionProperty4) {
		this.connectionProperty4 = connectionProperty4;
	}
	
	//Changes for Bug 792 - Adding TestSetFilter by name - starts
	public String getConnectionProperty5() {
		return connectionProperty5;
	}


	public void setConnectionProperty5(String connectionProperty5) {
		this.connectionProperty5 = connectionProperty5;
	}
	//Changes for Bug 792 - Adding TestSetFilter by name - ends


	public String getTestSystemVersion() {
		return testSystemVersion;
	}


	public void setTestSystemVersion(String testSystemVersion) {
		this.testSystemVersion = testSystemVersion;
	}
	
	



	public Integer getTestSystemTypeId() {
		return testSystemTypeId;
	}


	public void setTestSystemTypeId(Integer testSystemTypeId) {
		this.testSystemTypeId = testSystemTypeId;
	}


	public Integer getIsPrimary() {
		return isPrimary;
	}


	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}


	@JsonIgnore
	public TestManagementSystem getTestManagementSystem(){
		TestManagementSystem testManagementSystem=new TestManagementSystem();
		testManagementSystem.setTestManagementSystemId(testManagementSystemId);
		
		ProductMaster productMaster=new ProductMaster();
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		
		testManagementSystem.setProductMaster(productMaster);
	
		
		
		if(testSystemTypeId != null){
			SystemType systemType = new SystemType();
			systemType.setSystemTypeId(testSystemTypeId);
			testManagementSystem.setSystemType(systemType);
		}
		
		if(isPrimary == null){
			isPrimary = 0;	
		}
		testManagementSystem.setIsPrimary(isPrimary);
		testManagementSystem.setTitle(title);
		testManagementSystem.setDescription(description);
		testManagementSystem.setConnectionUri(connectionUri);
		testManagementSystem.setConnectionUserName(connectionUserName);
		testManagementSystem.setConnectionPassword(connectionPassword);
		testManagementSystem.setConnectionProperty1(connectionProperty1);
		testManagementSystem.setConnectionProperty2(connectionProperty2);
		testManagementSystem.setConnectionProperty3(connectionProperty3);
		testManagementSystem.setConnectionProperty4(connectionProperty4);
		testManagementSystem.setConnectionProperty4(connectionProperty5);//Changes for Bug 792 - Adding TestSetFilter by name 
		ToolIntagrationMaster toolIntagration = new ToolIntagrationMaster();
		if(toolIntagrationId != null) {
			toolIntagration.setId(toolIntagrationId);
			testManagementSystem.setToolIntagration(toolIntagration);
		}
		//testManagementSystem.setTestSystemName(testSystemName);
		testManagementSystem.setTestSystemVersion(testSystemVersion);
		
		return testManagementSystem;		
	}
	
	
	public JsonTestManagementSystem(WorkPackageTCEPSummaryDTO testMgmtSys){
		this.ResultCode = testMgmtSys.getResultCode();
		//this.testSystemName = testMgmtSys.getTestSystemName();
		
			this.toolIntagrationId = testMgmtSys.getToolIntagrationId();
			this.toolIntagrationName = testMgmtSys.getToolIntagrationName();
		this.exportedDate = testMgmtSys.getExportedDate();
		}
	
	@JsonIgnore
	public WorkPackageTCEPSummaryDTO getWorkPackageTCEPSummaryDTO(){
		WorkPackageTCEPSummaryDTO wpTcepSumDto = new  WorkPackageTCEPSummaryDTO();
		//wpTcepSumDto.setTestSystemName(TestSystemName);
		wpTcepSumDto.setToolIntagrationId(toolIntagrationId);
		wpTcepSumDto.setToolIntagrationName(toolIntagrationName);
		wpTcepSumDto.setResultCode(ResultCode);
		wpTcepSumDto.setExportedDate(exportedDate);
		return wpTcepSumDto;
	}


	public String getExportedDate() {
		return exportedDate;
	}


	public void setExportedDate(String exportedDate) {
		this.exportedDate = exportedDate;
	}


	public String getResultCode() {
		return ResultCode;
	}


	public void setResultCode(String ResultCode) {
		this.ResultCode = ResultCode;
	}
	
	public String getModifiedField() {
		return modifiedField;
	}


	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}


	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}


	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}


	public String getOldFieldValue() {
		return oldFieldValue;
	}


	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}


	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}


	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}


	public Integer getToolIntagrationId() {
		return toolIntagrationId;
	}


	public void setToolIntagrationId(Integer toolIntagrationId) {
		this.toolIntagrationId = toolIntagrationId;
	}


	public String getToolIntagrationName() {
		return toolIntagrationName;
	}


	public void setToolIntagrationName(String toolIntagrationName) {
		this.toolIntagrationName = toolIntagrationName;
	}
	
	
}
