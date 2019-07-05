package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dash_board_tabs_roleBased")
public class DashBoardTabsRoleBasedURL implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer roleBasedId;
	private DashBoardTabs dashBoardTabs;
	private UserRoleMaster productSpecificUserRole;
	private DashboardVisualizationUrls url;
	private Integer status;
	
	
	
	
	public DashBoardTabsRoleBasedURL() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "roleBasedId", unique = true, nullable = false)
	public Integer getRoleBasedId() {
		return roleBasedId;
	}




	public void setRoleBasedId(Integer roleBasedId) {
		this.roleBasedId = roleBasedId;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tabId")
	public DashBoardTabs getDashBoardTabs() {
		return dashBoardTabs;
	}




	public void setDashBoardTabs(DashBoardTabs dashBoardTabs) {
		this.dashBoardTabs = dashBoardTabs;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId")
	public UserRoleMaster getProductSpecificUserRole() {
		return productSpecificUserRole;
	}




	public void setProductSpecificUserRole(UserRoleMaster productSpecificUserRole) {
		this.productSpecificUserRole = productSpecificUserRole;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "url")
	public DashboardVisualizationUrls getUrl() {
		return url;
	}




	public void setUrl(DashboardVisualizationUrls url) {
		this.url = url;
	}



	@JoinColumn(name = "status")
	public Integer getStatus() {
		return status;
	}




	public void setStatus(Integer status) {
		this.status = status;
	}

	

}
