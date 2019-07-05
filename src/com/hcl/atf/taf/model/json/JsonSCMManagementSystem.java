package com.hcl.atf.taf.model.json;



import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.UserList;

public class JsonSCMManagementSystem implements java.io.Serializable {
	
	private static final Log log = LogFactory.getLog(JsonSCMManagementSystem.class);
	@JsonProperty
	private Integer id;
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
	private String scmSystemName;
	@JsonProperty
	private String scmSystemVersion;
	@JsonProperty
	private Integer isPrimary;
	@JsonProperty
	private String scmSystemCode;
	@JsonProperty
	private String sourceCodeExportDate;
	@JsonProperty
	private String scmSystemURI;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private Integer createdBy;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer modifiedBy;
	
	@JsonProperty
	private Integer toolIntagrationId;
	
	@JsonProperty
	private String toolIntagrationName;
	
	public JsonSCMManagementSystem() {
	}

	public JsonSCMManagementSystem(SCMSystem scmSystem) {
		
		this.id = scmSystem.getId();
		this.productId = scmSystem.getProductMaster().getProductId();
		this.title=scmSystem.getTitle();
		this.description=scmSystem.getDescription();
		this.connectionUri=scmSystem.getConnectionUri();
		this.connectionUserName=scmSystem.getConnectionUserName();
		this.connectionPassword=scmSystem.getConnectionPassword();
		this.connectionProperty1=scmSystem.getConnectionProperty1();
		this.connectionProperty2=scmSystem.getConnectionProperty2();
		this.connectionProperty3=scmSystem.getConnectionProperty3();
		this.connectionProperty4=scmSystem.getConnectionProperty4();
		//this.scmSystemName=scmSystem.getScmSystemName();
		if(scmSystem.getToolIntagration() != null) {
			this.toolIntagrationId = scmSystem.getToolIntagration().getId();
			this.toolIntagrationName = scmSystem.getToolIntagration().getName();
		}
		this.scmSystemVersion=scmSystem.getScmSystemVersion();	
		this.isPrimary=scmSystem.getIsPrimary();		
		this.status = scmSystem.getStatus();
		if(scmSystem.getCreatedBy() != null)
			this.createdBy = scmSystem.getCreatedBy().getUserId();
		if(scmSystem.getModifiedBy() != null)
			this.modifiedBy = scmSystem.getModifiedBy().getUserId();
		if (scmSystem.getCreatedDate() != null) {
			this.createdDate = DateUtility.sdfDateformatWithOutTime(scmSystem.getCreatedDate());

		}
		if (scmSystem.getModifiedDate() != null) {
			this.modifiedDate = DateUtility.sdfDateformatWithOutTime(scmSystem.getModifiedDate());
		}
	}

	@JsonIgnore
	public SCMSystem getSCMSystem(){
		SCMSystem scmSystem=new SCMSystem();
		scmSystem.setId(id);
		
		ProductMaster productMaster=new ProductMaster();
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		
		scmSystem.setProductMaster(productMaster);
		
		scmSystem.setTitle(title);
		scmSystem.setDescription(description);
		scmSystem.setConnectionUri(connectionUri);
		scmSystem.setConnectionUserName(connectionUserName);
		scmSystem.setConnectionPassword(connectionPassword);
		scmSystem.setConnectionProperty1(connectionProperty1);
		scmSystem.setConnectionProperty2(connectionProperty2);
		scmSystem.setConnectionProperty3(connectionProperty3);
		scmSystem.setConnectionProperty4(connectionProperty4);
		if(toolIntagrationId != null) {
			ToolIntagrationMaster toolIntagration = new ToolIntagrationMaster();
			toolIntagration.setId(toolIntagrationId);
			scmSystem.setToolIntagration(toolIntagration);
		}
		//scmSystem.setScmSystemName(scmSystemName);
		scmSystem.setScmSystemVersion(scmSystemVersion);
		scmSystem.setIsPrimary(isPrimary);
		scmSystem.setStatus(status);
		
		if(createdDate != null ){
			scmSystem.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			scmSystem.setCreatedDate(new Date());
		}
		if(modifiedDate != null && modifiedDate != "" ){
			scmSystem.setModifiedDate(DateUtility.dateFormatWithOutSeconds(modifiedDate));	
		}else{
			scmSystem.setModifiedDate(new Date());
		}
		if(this.createdBy != null){
			UserList user = new UserList();
			user.setUserId(createdBy);	
			scmSystem.setCreatedBy(user);
		}
		if(this.modifiedBy != null){
			UserList user = new UserList();
			user.setUserId(modifiedBy);			
			scmSystem.setCreatedBy(user);
		}
				
		return scmSystem;		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getScmSystemName() {
		return scmSystemName;
	}

	public void setScmSystemName(String scmSystemName) {
		this.scmSystemName = scmSystemName;
	}

	public String getScmSystemVersion() {
		return scmSystemVersion;
	}

	public void setScmSystemVersion(String scmSystemVersion) {
		this.scmSystemVersion = scmSystemVersion;
	}
	
	public Integer getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getSourceCodeExportDate() {
		return sourceCodeExportDate;
	}


	public void setSourceCodeExportDate(String sourceCodeExportDate) {
		this.sourceCodeExportDate = sourceCodeExportDate;
	}

	public String getScmSystemURI() {
		return scmSystemURI;
	}

	public void setScmSystemURI(String scmSystemURI) {
		this.scmSystemURI = scmSystemURI;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
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
