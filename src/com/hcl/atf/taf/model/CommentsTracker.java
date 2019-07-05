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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "comments_tracker")
public class CommentsTracker {
	
		private Integer commentsTrackerId;
		private ActivityTask entityId;
		private ActivityEntityMaster entityType;
		private UserList raisedBy;
		private Date raisedDate;
		private String comment;
		private CommentsTypeMaster commentType;
		private String commentStatus;	
		private Date lastUpdatedDate;
		
		@Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "commentsTrackerId",unique = true, nullable = false)
		public Integer getCommentsTrackerId() {
			return commentsTrackerId;
		}
		public void setCommentsTrackerId(Integer commentsTrackerId) {
			this.commentsTrackerId = commentsTrackerId;
		}	
		@ManyToOne (fetch = FetchType.LAZY)
		@JoinColumn (name = "entityId")
		public ActivityTask getEntityId() {
			return entityId;
		}
		public void setEntityId(ActivityTask entityId) {
			this.entityId = entityId;
		}	
		@ManyToOne (fetch = FetchType.LAZY)
		@JoinColumn (name = "raisedById")
		public UserList getRaisedBy() {
			return raisedBy;
		}
		public void setRaisedBy(UserList raisedBy) {
			this.raisedBy = raisedBy;
		}
		@Column(name = "raisedDate")
		public Date getRaisedDate() {
			return raisedDate;
		}
		public void setRaisedDate(Date raisedDate) {
			this.raisedDate = raisedDate;
		}
		@Column(name = "comment")
		public String getComment() {
			return comment;
		}		
		public void setComment(String comment) {
			this.comment = comment;
		}			
		@Column(name = "commentStatus")
		public String getCommentStatus() {
			return commentStatus;
		}
		public void setCommentStatus(String commentStatus) {
			this.commentStatus = commentStatus;
		}
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "lastUpdatedDate")
		public Date getLastUpdatedDate() {
			return lastUpdatedDate;
		}
		public void setLastUpdatedDate(Date lastUpdatedDate) {
			this.lastUpdatedDate = lastUpdatedDate;
		}
		@ManyToOne (fetch = FetchType.LAZY)
		@JoinColumn (name = "entityTypeId")
		public ActivityEntityMaster getEntityType() {
			return entityType;
		}
		public void setEntityType(ActivityEntityMaster entityType) {
			this.entityType = entityType;
		}
		@ManyToOne (fetch = FetchType.LAZY)
		@JoinColumn (name = "commentTypeId")
		public CommentsTypeMaster getCommentType() {
			return commentType;
		}
		public void setCommentType(CommentsTypeMaster commentType) {
			this.commentType = commentType;
		}	

}

