package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CommentsTypeMaster;

public class JsonCommentsTypeMaster {
	@JsonProperty
	private Integer commentsTypeId;
	@JsonProperty
	private String commentsTypeName;
	@JsonProperty
	private String description;
	
	public JsonCommentsTypeMaster() {
	}

	public JsonCommentsTypeMaster(CommentsTypeMaster commentsTypeMaster) {
		this.commentsTypeId = commentsTypeMaster.getCommentsTypeId();
		this.commentsTypeName = commentsTypeMaster.getCommentsTypeName();
	}

	public Integer getCommentsTypeId() {
		return commentsTypeId;
	}

	public void setCommentsTypeId(Integer commentsTypeId) {
		this.commentsTypeId = commentsTypeId;
	}

	public String getCommentsTypeName() {
		return commentsTypeName;
	}

	public void setCommentsTypeName(String commentsTypeName) {
		this.commentsTypeName = commentsTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
