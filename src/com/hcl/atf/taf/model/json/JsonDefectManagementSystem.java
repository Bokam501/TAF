package com.hcl.atf.taf.model.json;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.dto.DefectReportDTO;


public class JsonDefectManagementSystem implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonDefectManagementSystem.class);
	@JsonProperty
	private Integer defectManagementSystemId;
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
	/*@JsonProperty
	private String defectSystemName;*/
	@JsonProperty
	private String defectSystemVersion;
	
	@JsonProperty
	private Integer isPrimary;
	
	@JsonProperty
	private String defectSystemCode;
	
	@JsonProperty
	private String defectExportDate;
	@JsonProperty
	private String defectSystemURI;
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
	
	public JsonDefectManagementSystem() {
	}


	public JsonDefectManagementSystem(DefectManagementSystem defectManagementSystem) {
		
		this.defectManagementSystemId = defectManagementSystem.getDefectManagementSystemId();
		this.productId = defectManagementSystem.getProductMaster().getProductId();
		this.title=defectManagementSystem.getTitle();
		this.description=defectManagementSystem.getDescription();
		this.connectionUri=defectManagementSystem.getConnectionUri();
		this.connectionUserName=defectManagementSystem.getConnectionUserName();
		this.connectionPassword=defectManagementSystem.getConnectionPassword();
		this.connectionProperty1=defectManagementSystem.getConnectionProperty1();
		this.connectionProperty2=defectManagementSystem.getConnectionProperty2();
		this.connectionProperty3=defectManagementSystem.getConnectionProperty3();
		this.connectionProperty4=defectManagementSystem.getConnectionProperty4();
		//this.defectSystemName=defectManagementSystem.getDefectSystemName();
		if(defectManagementSystem.getToolIntagration() != null ) {
			this.toolIntagrationId = defectManagementSystem.getToolIntagration().getId();
			this.toolIntagrationName = defectManagementSystem.getToolIntagration().getName();
		}
		this.defectSystemVersion=defectManagementSystem.getDefectSystemVersion();	
		this.isPrimary=defectManagementSystem.getIsPrimary();		
	}

	public Integer getDefectManagementSystemId() {
		return defectManagementSystemId;
	}


	public void setDefectManagementSystemId(Integer defectManagementSystemId) {
		this.defectManagementSystemId = defectManagementSystemId;
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


	public String getDefectSystemVersion() {
		return defectSystemVersion;
	}


	public void setDefectSystemVersion(String defectSystemVersion) {
		this.defectSystemVersion = defectSystemVersion;
	}
	
	

	public Integer getIsPrimary() {
		return isPrimary;
	}


	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}


	@JsonIgnore
	public DefectManagementSystem getDefectManagementSystem(){
		DefectManagementSystem defectManagementSystem=new DefectManagementSystem();
		defectManagementSystem.setDefectManagementSystemId(defectManagementSystemId);
		
		ProductMaster productMaster=new ProductMaster();
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		
		defectManagementSystem.setProductMaster(productMaster);
		
		defectManagementSystem.setTitle(title);
		defectManagementSystem.setDescription(description);
		defectManagementSystem.setConnectionUri(connectionUri);
		defectManagementSystem.setConnectionUserName(connectionUserName);
		defectManagementSystem.setConnectionPassword(connectionPassword);
		defectManagementSystem.setConnectionProperty1(connectionProperty1);
		defectManagementSystem.setConnectionProperty2(connectionProperty2);
		defectManagementSystem.setConnectionProperty3(connectionProperty3);
		defectManagementSystem.setConnectionProperty4(connectionProperty4);
		//defectManagementSystem.setDefectSystemName(defectSystemName);
		if(toolIntagrationId != null) {
			ToolIntagrationMaster toolIntagration = new ToolIntagrationMaster();
			toolIntagration.setId(toolIntagrationId);
			defectManagementSystem.setToolIntagration(toolIntagration);
		}
		defectManagementSystem.setDefectSystemVersion(defectSystemVersion);
		defectManagementSystem.setIsPrimary(isPrimary);
		
		return defectManagementSystem;		
	}

	public JsonDefectManagementSystem(DefectReportDTO defectRepDto) {
		this.defectSystemCode = defectRepDto.getDefectSystemCode();
		//this.defectSystemName = defectRepDto.getDefectSystemName();
		this.toolIntagrationName = defectRepDto.getDefectSystemName();
		this.defectExportDate = defectRepDto.getDefectExportDate();
		if(defectRepDto.getDefectSystemURI() != null)
			this.defectSystemURI = defectRepDto.getDefectSystemURI();
		
		if(defectExportDate != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date defDate;
			try {
				defDate = sdf.parse(defectExportDate);
				String xx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defDate);
				this.defectExportDate = xx;
			} catch (ParseException e) {
				log.error("ERROR  ",e);
			}
			
		}		

	}
	
	@JsonIgnore
	public DefectReportDTO getDefectReportDTO(){
		DefectReportDTO defectReportDto=new DefectReportDTO();	
		defectReportDto.setDefectSystemCode(this.defectSystemCode);
		//defectReportDto.setDefectSystemName(this.defectSystemName);
		defectReportDto.setDefectSystemName(this.toolIntagrationName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String defDate = sdf.format(defectReportDto.getDefectExportDate());
		this.defectExportDate = defDate;
		return defectReportDto;		
	}


	public String getDefectSystemCode() {
		return defectSystemCode;
	}


	public void setDefectSystemCode(String defectSystemCode) {
		this.defectSystemCode = defectSystemCode;
	}


	public String getDefectExportDate() {
		return defectExportDate;
	}


	public void setDefectExportDate(String defectExportDate) {
		this.defectExportDate = defectExportDate;
	}


	public String getDefectSystemURI() {
		return defectSystemURI;
	}


	public void setDefectSystemURI(String defectSystemURI) {
		this.defectSystemURI = defectSystemURI;
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
