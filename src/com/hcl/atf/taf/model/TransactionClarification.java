package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "transaction_clarification")
public class TransactionClarification {
	
	private Integer transactionId;
	private String transactionName;	
	private ClarificationTracker clarificationTrackerId;
	private String comment;
	private String commentType;
	private Date commentedDate;
    private Integer commentWeightage;
    private Integer replyTo;
    private UserList  commentedBy;
    
    
    
    @Id
  	@GeneratedValue(strategy = IDENTITY)
  	@Column(name = "transactionId", unique = true, nullable = false)
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	
	
	@Column(name = "transactionName")
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clarificationTrackerId")
	public ClarificationTracker getClarificationTrackerId() {
		return clarificationTrackerId;
	}
	public void setClarificationTrackerId(
			ClarificationTracker clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}
	
	@Column(name = "comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "commentType")
	public String getCommentType() {
		return commentType;
	}
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	
	@Column(name = "commentedDate")
	public Date getCommentedDate() {
		return commentedDate;
	}
	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}
	
	@Column(name = "commentWeightage")
	public Integer getCommentWeightage() {
		return commentWeightage;
	}
	public void setCommentWeightage(Integer commentWeightage) {
		this.commentWeightage = commentWeightage;
	}
	
	@Column(name = "replyTo")
	public Integer getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commentedBy")
	public UserList getCommentedBy() {
		return commentedBy;
	}
	public void setCommentedBy(UserList commentedBy) {
		this.commentedBy = commentedBy;
	}
	
	

}
