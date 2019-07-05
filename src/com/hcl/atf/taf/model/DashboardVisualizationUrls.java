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
@Table(name = "dashboard_visualization_urls")

public class DashboardVisualizationUrls implements java.io.Serializable{
	
	private Integer visualizationId;
	private String urlName;
	private String url;
	private Integer status;
	private String description;
	private UserList createdBy;
	private Date createdDate;
	private Date modifiedDate;
	

	public DashboardVisualizationUrls() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "visualizationId", unique = true, nullable = false)
	public Integer getVisualizationId() {
		return visualizationId;
	}


	public void setVisualizationId(Integer visualizationId) {
		this.visualizationId = visualizationId;
	}


	public String getUrlName() {
		return urlName;
	}


	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	
}
