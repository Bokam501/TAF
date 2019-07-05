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
@Table(name = "workflowevent")
public class WorkFlowEvent implements java.io.Serializable{

	private Integer workfloweventId;
	private Date eventDate;
	private String remarks;
	private UserList user;
	private WorkFlow workFlow;
	private Integer  entityTypeRefId;
	public WorkFlowEvent () {
}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workfloweventId", unique = true, nullable = false)
	public Integer getWorkfloweventId() {
		return workfloweventId;
	}
	public void setWorkfloweventId(Integer workfloweventId) {
		this.workfloweventId = workfloweventId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventDate")
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	@Column(name = "remarks", length = 45)
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") 
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowId") 
	public WorkFlow getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}
	
	@Column(name = "entityTypeId")
	public Integer getEntityTypeRefId() {
		return entityTypeRefId;
	}
	public void setEntityTypeRefId(Integer entityTypeRefId) {
		this.entityTypeRefId = entityTypeRefId;
	}
	
}
