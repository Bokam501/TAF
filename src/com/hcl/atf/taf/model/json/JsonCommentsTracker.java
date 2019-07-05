package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.model.CommentsTracker;
import com.hcl.atf.taf.model.CommentsTypeMaster;
import com.hcl.atf.taf.model.UserList;


public class JsonCommentsTracker {
	
	private static final Log log = LogFactory.getLog(JsonActivity.class);
	@JsonProperty
	private Integer commentsTrackerId;
	@JsonProperty	
	private Integer entityId;
	@JsonProperty	
	private String entityName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer raisedBy;
	@JsonProperty
	private String raisedByName;
	@JsonProperty
	private Date raisedDate;
	@JsonProperty
	private String comment;
	@JsonProperty
	private Integer commentTypeId;
	@JsonProperty
	private String commentTypeName;
	@JsonProperty
	private String commentStatus;	
	@JsonProperty
	private String lastUpdatedDate;	
	
	
	public JsonCommentsTracker(){
	}
	
	public JsonCommentsTracker(CommentsTracker commentsTracker) {
		this.commentsTrackerId = commentsTracker.getCommentsTrackerId();	
	
		this.entityId = commentsTracker.getEntityId().getActivityTaskId();
		this.entityName = commentsTracker.getEntityId().getActivityTaskName();
		this.raisedBy = commentsTracker.getRaisedBy().getUserId();	
		this.raisedByName = commentsTracker.getRaisedBy().getLoginId();	
		this.raisedDate = commentsTracker.getRaisedDate();	
		this.comment = commentsTracker.getComment();	
		this.lastUpdatedDate = DateUtility.dateformatWithOutTime(commentsTracker.getLastUpdatedDate());;	
		this.commentStatus = commentsTracker.getCommentStatus();	

		
		if (commentsTracker.getEntityType() != null) {
			this.entityTypeId = commentsTracker.getEntityType().getEntityId();
			this.entityTypeName = commentsTracker.getEntityType().getEntityName();
			
			
		}
		if (commentsTracker.getEntityType() != null) {			
			this.commentTypeId = commentsTracker.getCommentType().getCommentsTypeId();
			this.commentTypeName = commentsTracker.getCommentType().getCommentsTypeName();
			
		}
		
	}
	
	@JsonIgnore
	public CommentsTracker getCommentsTracker() {
		CommentsTracker commentsTracker = new CommentsTracker();
		commentsTracker.setCommentsTrackerId(commentsTrackerId);		
		
		if (this.commentTypeId != null) {
			CommentsTypeMaster commentsTypeMaster = new CommentsTypeMaster();			
			commentsTypeMaster.setCommentsTypeId(commentTypeId);
			commentsTypeMaster.setCommentsTypeName(commentTypeName);
			commentsTracker.setCommentType(commentsTypeMaster);
		}
		if (this.entityTypeId != null) {
			ActivityEntityMaster activityEntityMaster = new ActivityEntityMaster();
			activityEntityMaster.setEntityId(entityId);	
			commentsTracker.setEntityType(activityEntityMaster);
			
		}
		if (this.commentTypeId != null) {
			UserList userList = new UserList();
			userList.setUserId(entityId);
			commentsTracker.setRaisedBy(userList);
			
		}
		
		commentsTracker.setRaisedDate(raisedDate);
		commentsTracker.setLastUpdatedDate(DateUtility.getCurrentTime());
		commentsTracker.setComment(comment);	
		commentsTracker.setCommentStatus(commentStatus);
				
		return commentsTracker;
		
	}

}

