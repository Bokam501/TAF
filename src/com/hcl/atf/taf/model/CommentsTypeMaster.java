package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "comments_type_master")
public class CommentsTypeMaster {
	
	private Integer commentsTypeId;
	private String commentsTypeName;
	private String description;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "commentsTypeId", unique = true, nullable = false)
	public Integer getCommentsTypeId() {
		return commentsTypeId;
	}
	public void setCommentsTypeId(Integer commentsTypeId) {
		this.commentsTypeId = commentsTypeId;
	}
	@Column(name = "commentsTypeName")
	public String getCommentsTypeName() {
		return commentsTypeName;
	}
	public void setCommentsTypeName(String commentsTypeName) {
		this.commentsTypeName = commentsTypeName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
