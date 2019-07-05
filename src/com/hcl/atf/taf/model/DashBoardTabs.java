package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "dash_board_tabs")
public class DashBoardTabs implements java.io.Serializable{
	
	private Integer tabId;
	private String tabName;
	private String tabType;
	private String deployment;
	private Integer orderNo;
	private Integer status;
	private TestFactory engagement;
	

	public DashBoardTabs() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "tabId", unique = true, nullable = false)
	public Integer getTabId() {
		return tabId;
	}


	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	@Column(name = "tabName")
	public String getTabName() {
		return tabName;
	}


	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	@Column(name = "tabType")
	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	@Column(name = "deployment")
	public String getDeployment() {
		return deployment;
	}

	public void setDeployment(String deployment) {
		this.deployment = deployment;
	}

	@Column(name = "orderNo")
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="engagementId")
	public TestFactory getEngagement() {
		return engagement;
	}

	public void setEngagement(TestFactory engagement) {
		this.engagement = engagement;
	}
}
