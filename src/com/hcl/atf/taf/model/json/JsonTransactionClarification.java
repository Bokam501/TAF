package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.TransactionClarification;
import com.hcl.atf.taf.model.UserList;

public class JsonTransactionClarification {

	private static final Log log = LogFactory.getLog(JsonTransactionClarification.class);

	@JsonProperty
	private Integer transactionId;
	@JsonProperty
	private String transactionName;
	@JsonProperty
	private Integer clarificationTrackerId;
	@JsonProperty
	private String comment;
	@JsonProperty
	private String commentType;
	@JsonProperty
	private String commentedDate;
	@JsonProperty
	private Integer commentWeightage;
    @JsonProperty
	private Integer replyTo;
	
	@JsonProperty
	private Integer commentedBy;
	
	
	public JsonTransactionClarification() {
	}

	public JsonTransactionClarification(TransactionClarification transactionClarification) {
		this.transactionId = transactionClarification.getTransactionId();
		this.transactionName=transactionClarification.getTransactionName();
		this.comment=transactionClarification.getComment();
		this.commentType=transactionClarification.getCommentType();
		this.commentWeightage=transactionClarification.getCommentWeightage();
		this.replyTo=transactionClarification.getReplyTo();
		
		if (transactionClarification.getClarificationTrackerId() != null) {
			this.clarificationTrackerId = transactionClarification.getClarificationTrackerId().getClarificationTrackerId();
		}
		if(transactionClarification.getCommentedDate() != null){
			this.commentedDate = DateUtility.dateToStringWithSeconds1(transactionClarification.getCommentedDate());
		}
		
		if (transactionClarification.getCommentedBy() != null) {
			this.commentedBy = transactionClarification.getCommentedBy().getUserId();
		}
		
		
	}

	@JsonIgnore
	public TransactionClarification getTransactionClarification() {
		TransactionClarification transactionClarification=new TransactionClarification();
		transactionClarification.setTransactionId(transactionId);
		transactionClarification.setTransactionName(transactionName);
		transactionClarification.setComment(comment);
		transactionClarification.setCommentType(commentType);
		transactionClarification.setCommentWeightage(commentWeightage);
		transactionClarification.setReplyTo(replyTo);
		
		if(this.clarificationTrackerId!=null){
			ClarificationTracker clarificationTracker=new ClarificationTracker();
			clarificationTracker.setClarificationTrackerId(clarificationTrackerId);
			transactionClarification.setClarificationTrackerId(clarificationTracker);
		}
		
		if(this.commentedDate == null || this.commentedDate.trim().isEmpty()) {
			transactionClarification.setCommentedDate(DateUtility.getCurrentTime());
		} else {
			transactionClarification.setCommentedDate(DateUtility.toFormatDate(this.commentedDate));
		}
		
		if (this.commentedBy!=null) {
			UserList user=new UserList();
			user.setUserId(commentedBy);
			transactionClarification.setCommentedBy(user);
		}
		
		return transactionClarification;
		
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}

	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getCommentedDate() {
		return commentedDate;
	}

	public void setCommentedDate(String commentedDate) {
		this.commentedDate = commentedDate;
	}

	public Integer getCommentWeightage() {
		return commentWeightage;
	}

	public void setCommentWeightage(Integer commentWeightage) {
		this.commentWeightage = commentWeightage;
	}

	public Integer getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
	}

	public Integer getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(Integer commentedBy) {
		this.commentedBy = commentedBy;
	}

	
	
	
	
	
}
