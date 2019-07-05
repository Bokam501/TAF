package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.AttachmentDTO;

public class JsonComments implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonComments.class);
	@JsonProperty
	private Integer commentId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private Integer entityMasterId;	
	@JsonProperty
	private String entityMasterName;

	@JsonProperty
	private Integer entityPrimaryId;
	@JsonProperty
	private String entityName;
	@JsonProperty
	private String commentsText;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer createrId;	
	@JsonProperty	
	private String createrName;	
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String result;
	
	public JsonComments() {
		this.commentId =0 ;
		this.productId=0 ;
		this.productName = "";
		this.entityMasterId=0 ;
		this.entityMasterName = "";
		this.entityPrimaryId=0 ;
		this.entityName = "";
		this.description = "";
		this.createrId=0 ;
		this.createrName = "";
		this.createdDate = "";
		this.result = "";
	}

	public JsonComments(Comments commentsObj) {		
		if(commentsObj.getCommentId() != null){
			this.commentId = commentsObj.getCommentId() ;	
		}
		if(commentsObj.getProduct() != null){
			this.productId = commentsObj.getProduct().getProductId();
			this.productName = commentsObj.getProduct().getProductName();
		}

		if(commentsObj.getComments() != null){
			this.commentsText = commentsObj.getComments();
		}

		if(commentsObj.getEntityMaster() != null){
			this.entityMasterId = commentsObj.getEntityMaster().getEntitymasterid();
			this.entityMasterName = commentsObj.getEntityMaster().getEntitymastername();
		}
		if(commentsObj.getEntityId() != null){
			this.entityPrimaryId = commentsObj.getEntityId();
		}	

		if(commentsObj.getDescription() != null){
			this.description = commentsObj.getDescription();
		}

		if(commentsObj.getCreatedBy() != null){
			this.createrId = commentsObj.getCreatedBy().getUserId();
			this.createrName = commentsObj.getCreatedBy().getLoginId();
		}
		
		if(commentsObj.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(commentsObj.getCreatedDate());
		}
		
		if(commentsObj.getResult() != null){
			this.result = commentsObj.getResult();
		}
		
		}
		
	public JsonComments(AttachmentDTO attachmentDTO) {}

	@JsonIgnore
	public Comments getComments() {
		Comments commentsObj = new Comments();
		if(this.commentId != null){
			commentsObj.setCommentId(commentId);
		}		

		if(this.productId != null && this.productId > 0){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			commentsObj.setProduct(productMaster);
		}

		if(this.entityMasterId != null){//this.entityMasterName
			EntityMaster etm = new EntityMaster();
			etm.setEntitymasterid(entityMasterId);			
			commentsObj.setEntityMaster(etm);
		}

		if(this.entityPrimaryId != null)
			commentsObj.setEntityId(entityPrimaryId);
		if(this.commentsText != null)
			commentsObj.setComments(commentsText);
		
		if(this.description != null){
			commentsObj.setDescription(description);
		}
		
		if(this.createrId != null){
			UserList creater = new UserList();
			creater.setUserId(createrId);		//this.createrName 				
			commentsObj.setCreatedBy(creater);
		}
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			commentsObj.setCreatedDate(DateUtility.getCurrentTime());
		}else{		
			commentsObj.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		
		if(this.result != null){
			commentsObj.setResult(result);
		}
		
		return commentsObj;		
	}

	public Integer getCommentId() {
		return commentId;
	}

	public Integer getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Integer getEntityMasterId() {
		return entityMasterId;
	}

	public String getEntityMasterName() {
		return entityMasterName;
	}

	public Integer getEntityPrimaryId() {
		return entityPrimaryId;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getDescription() {
		return description;
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public String getCreaterName() {
		return createrName;
	}

	public String getCreatedDate() {
		return createdDate;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setEntityMasterId(Integer entityMasterId) {
		this.entityMasterId = entityMasterId;
	}

	public void setEntityMasterName(String entityMasterName) {
		this.entityMasterName = entityMasterName;
	}

	public void setEntityPrimaryId(Integer entityPrimaryId) {
		this.entityPrimaryId = entityPrimaryId;
	}

	public String getCommentsText() {
		return commentsText;
	}

	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
