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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "environment_category")
public class EnvironmentCategory implements java.io.Serializable{

	private Integer environmentCategoryId;
	private String environmentCategoryName;
	private String description;
	private Date createdDate;
	private Date modifiedDate;
	private Integer status;
	private String displayName;
	private EnvironmentCategory parentEnvironmentCategory;
	private Integer leftIndex;
	private Integer rightIndex;
	private EnvironmentGroup environmentGroup;
	
	
	public EnvironmentCategory(){
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "environment_category_id", unique = true, nullable = false)
	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}


	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	@Column(name = "environment_category_name", length = 100)
	public String getEnvironmentCategoryName() {
		return environmentCategoryName;
	}


	public void setEnvironmentCategoryName(String environmentCategoryName) {
		this.environmentCategoryName = environmentCategoryName;
	}

	@Column(name = "environment_category_description", length = 100)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "environment_category_displayname", length = 100)
	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Column(name = "left_index")
	public Integer getLeftIndex() {
		return leftIndex;
	}


	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	@Column(name = "right_index")
	public Integer getRightIndex() {
		return rightIndex;
	}


	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "environment_group_id" )
	public EnvironmentGroup getEnvironmentGroup() {
		return environmentGroup;
	}


	public void setEnvironmentGroup(EnvironmentGroup environmentGroup) {
		this.environmentGroup = environmentGroup;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_environment_category_id" )
	@NotFound(action=NotFoundAction.IGNORE)
	public EnvironmentCategory getParentEnvironmentCategory() {
		return parentEnvironmentCategory;
	}

	public void setParentEnvironmentCategory(
			EnvironmentCategory parentEnvironmentCategory) {
		this.parentEnvironmentCategory = parentEnvironmentCategory;
	}
	
	
	
}
