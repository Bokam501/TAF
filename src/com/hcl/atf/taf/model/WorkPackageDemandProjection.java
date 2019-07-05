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
@Table(name = "workpackage_demand_projection")
public class WorkPackageDemandProjection  implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer wpDemandProjectionId;
	
	private Float resourceCount;
	
	private Date workDate;
	
	private WorkPackage workPackage;
	
	private WorkShiftMaster workShiftMaster;
	
	private Skill skill;
	
	private UserRoleMaster userRole;
	
	private Date demandRaisedOn;
	private UserTypeMasterNew userTypeMasterNew;
	
	private UserList demandRaisedByUser;
	private Integer workWeek;
	private Integer workYear;
	private String demandMode;
	
	private Long groupDemandId;
	
	public WorkPackageDemandProjection  () {
}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "wpDemandProjectionId", unique = true, nullable = false)
	public Integer getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}

	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}
	
	@Column(name="resourceCount")
	public Float getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "workDate")
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId") 
	public WorkShiftMaster getWorkShiftMaster() {
		return workShiftMaster;
	}

	public void setWorkShiftMaster(WorkShiftMaster workShiftMaster) {
		this.workShiftMaster = workShiftMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId") 
	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skillId") 
	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId") 
	public UserRoleMaster getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoleMaster userRole) {
		this.userRole = userRole;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "demandRaisedOn")
	public Date getDemandRaisedOn() {
		return demandRaisedOn;
	}

	public void setDemandRaisedOn(Date demandRaisedOn) {
		this.demandRaisedOn = demandRaisedOn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "demandRaisedByUserId")
	public UserList getDemandRaisedByUser() {
		return demandRaisedByUser;
	}

	public void setDemandRaisedByUser(UserList demandRaisedByUser) {
		this.demandRaisedByUser = demandRaisedByUser;
	}
	
	@Column(name = "workWeek")
	public Integer getWorkWeek() {
		return workWeek;
	}
	public void setWorkWeek(Integer workWeek) {
		this.workWeek = workWeek;
	}
	
	@Column(name = "workYear")
	public Integer getWorkYear() {
		return workYear;
	}
	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}
	
	@Column(name = "demandMode")
	public String getDemandMode() {
		return demandMode;
	}
	public void setDemandMode(String demandMode) {
		this.demandMode = demandMode;
	}
	
	@Column(name = "groupDemandId")
	public Long getGroupDemandId() {
		return groupDemandId;
	}
	public void setGroupDemandId(Long groupDemandId) {
		this.groupDemandId = groupDemandId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userTypeId", nullable = true)
	public UserTypeMasterNew getUserTypeMasterNew() {
		return userTypeMasterNew;
	}

	public void setUserTypeMasterNew(UserTypeMasterNew userTypeMasterNew) {
		this.userTypeMasterNew = userTypeMasterNew;
	}

	
}
