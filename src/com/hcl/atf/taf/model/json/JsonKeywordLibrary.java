package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonKeywordLibrary implements Serializable{
	
	@JsonProperty
	private String keywordPhrase;
	@JsonProperty
	private Integer keywordLibId;
	@JsonProperty
	private String type;
	@JsonProperty
	private Integer languagID;
	@JsonProperty
	private String languageName;
	@JsonProperty
	private String className;
	@JsonProperty
	private String binary;
	@JsonProperty
	private String status;
	@JsonProperty
	private Integer testToolId;
	@JsonProperty
	private String testToolName;
	@JsonProperty
	private String dateAdded;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private Integer id;
	@JsonProperty
	private Integer isApprove;
	@JsonProperty
	private Integer isReferBack;
	@JsonProperty
	private Integer isReject;
	@JsonProperty
	private String comments;
	@JsonProperty
	private String approvedBy;
	@JsonProperty
	private String rejectedBy;
	@JsonProperty
	private String referBackBy;
	@JsonProperty
	private Integer approvedById;
	@JsonProperty
	private Integer rejectedById;
	@JsonProperty
	private Integer referBackById;
	@JsonProperty
	private Integer tcAttachmtsCount;
	@JsonProperty
	private Integer productTypeId;
	@JsonProperty
	private String productTypeName;
	public JsonKeywordLibrary() {
		
	}

	
	
	public JsonKeywordLibrary(KeywordLibrary keywordLibrary){
		if(keywordLibrary!=null){
			BDDKeywordsPhrases keyword = keywordLibrary.getKeywords();
			if(keyword != null){
				keywordPhrase = keyword.getKeywordPhrase();	
			}
			
			this.keywordLibId=keywordLibrary.getId();
			this.type=keywordLibrary.getType();
			this.className=keywordLibrary.getClassName();
			this.binary=keywordLibrary.getBinary();
			this.status=keywordLibrary.getStatus();
			this.userId=keywordLibrary.getUser().getUserId();
			this.userName=keywordLibrary.getUser().getLoginId();
			this.languagID=keywordLibrary.getLanguage().getId();
			this.languageName=keywordLibrary.getLanguage().getName();
			this.testToolId=keywordLibrary.getTestToolMaster().getTestToolId();
			this.testToolName=keywordLibrary.getTestToolMaster().getTestToolName();
			if(keywordLibrary.getProductType() != null){
				this.productTypeId = keywordLibrary.getProductType().getProductTypeId();
				this.productTypeName = keywordLibrary.getProductType().getTypeName();
			}
		}
	}
	
	 public JsonKeywordLibrary(KeywordLibrary keywordLibrary, String myTestCase) {
	        
	        if(keywordLibrary!=null){
				this.keywordLibId=keywordLibrary.getId();
				BDDKeywordsPhrases keyword = keywordLibrary.getKeywords();
				if(keyword != null){
					keywordPhrase = keyword.getKeywordPhrase();	
				}
				this.type=keywordLibrary.getType();
				this.className=keywordLibrary.getClassName();
				this.binary=keywordLibrary.getBinary();
				this.status=keywordLibrary.getStatus();
				this.userId=keywordLibrary.getUser().getUserId();
				this.userName=keywordLibrary.getUser().getLoginId();
				this.languagID=keywordLibrary.getLanguage().getId();
				this.languageName=keywordLibrary.getLanguage().getName();
				this.testToolId=keywordLibrary.getTestToolMaster().getTestToolId();
				this.testToolName=keywordLibrary.getTestToolMaster().getTestToolName();
				if(keywordLibrary.getProductType() != null){
					this.productTypeId = keywordLibrary.getProductType().getProductTypeId();
					this.productTypeName = keywordLibrary.getProductType().getTypeName();
				}
			}
	        
	        int count=0;
	        isApprove=0;
	        isReferBack=0;
	        isReject=0;
	    }

	
	@JsonIgnore
	public KeywordLibrary getKeywordLibrary(){
		
		KeywordLibrary keyWordLibrary=new KeywordLibrary();
		
		if (keywordLibId != null) {
			keyWordLibrary.setId(keywordLibId);
		}
		if (this.testToolId != null) {
			TestToolMaster testToolMaster=new TestToolMaster();
			testToolMaster.setTestToolId(this.testToolId);
			keyWordLibrary.setTestToolMaster(testToolMaster);
			
		}
		
		if(this.languagID!=null){
			Languages languages=new Languages();
			languages.setId(this.languagID);
			keyWordLibrary.setLanguage(languages);
		}
		
		if(type != null){
			keyWordLibrary.setType(type);
		}
		if(className != null){
			keyWordLibrary.setClassName(className);
		}
		if(binary != null){
			keyWordLibrary.setBinary(binary);;
		}
		
		if(this.status == null){
			
			keyWordLibrary.setStatus("new");
		}
		if(id != null){
			BDDKeywordsPhrases keyword = new BDDKeywordsPhrases();
			keyword.setId(id);
			keyWordLibrary.setKeywords(keyword);
		}
		keyWordLibrary.setDateAdded(new Date());
		if(this.productTypeId != null){
			ProductType prodType = new ProductType();
			prodType.setProductTypeId(productTypeId);
			keyWordLibrary.setProductType(prodType);
		}
		return keyWordLibrary;
	}
	
	
	


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getKeywordLibId() {
		return keywordLibId;
	}



	public void setKeywordLibId(Integer keywordLibId) {
		this.keywordLibId = keywordLibId;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLanguagID() {
		return languagID;
	}

	public void setLanguagID(Integer languagID) {
		this.languagID = languagID;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTestToolId() {
		return testToolId;
	}

	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}

	public String getTestToolName() {
		return testToolName;
	}

	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



	public Integer getIsApprove() {
		return isApprove;
	}



	public void setIsApprove(Integer isApprove) {
		this.isApprove = isApprove;
	}



	public Integer getIsReferBack() {
		return isReferBack;
	}



	public void setIsReferBack(Integer isReferBack) {
		this.isReferBack = isReferBack;
	}



	public Integer getIsReject() {
		return isReject;
	}



	public void setIsReject(Integer isReject) {
		this.isReject = isReject;
	}



	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}



	public String getApprovedBy() {
		return approvedBy;
	}



	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}



	public String getRejectedBy() {
		return rejectedBy;
	}



	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}



	public String getReferBackBy() {
		return referBackBy;
	}



	public void setReferBackBy(String referBackBy) {
		this.referBackBy = referBackBy;
	}



	public Integer getApprovedById() {
		return approvedById;
	}



	public void setApprovedById(Integer approvedById) {
		this.approvedById = approvedById;
	}



	public Integer getRejectedById() {
		return rejectedById;
	}



	public void setRejectedById(Integer rejectedById) {
		this.rejectedById = rejectedById;
	}



	public Integer getReferBackById() {
		return referBackById;
	}



	public void setReferBackById(Integer referBackById) {
		this.referBackById = referBackById;
	}



	public Integer getTcAttachmtsCount() {
		return tcAttachmtsCount;
	}



	public void setTcAttachmtsCount(Integer tcAttachmtsCount) {
		this.tcAttachmtsCount = tcAttachmtsCount;
	}



	public String getKeywordPhrase() {
		return keywordPhrase;
	}



	public void setKeywordPhrase(String keywordPhrase) {
		this.keywordPhrase = keywordPhrase;
	}



	public Integer getProductTypeId() {
		return productTypeId;
	}



	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}



	public String getProductTypeName() {
		return productTypeName;
	}



	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	
	
	
	
	
}
