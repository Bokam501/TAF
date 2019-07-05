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
@Table(name = "test_factory_resource_reservation")
public class TestFactoryResourceReservation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer resourceReservationId;
	private WorkPackage workPackage;
	private Date reservationDate;
	private WorkShiftMaster shift;
	private UserList blockedUser;
	private UserList reservationActionUser;
	private Date reservationActionDate;
	private UserRoleMaster userRole;
	private Skill skill;
	private Integer reservationPercentage;
	private Integer reservationWeek;
	private Integer reservationYear;
	private String reservationMode;
	private TestfactoryResourcePool resourcePool;
	
	private UserTypeMasterNew userType;
	
	private Long groupReservationId;
	

	
	public TestFactoryResourceReservation() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "resourceReservationId", unique = true, nullable = false)
	public Integer getResourceReservationId() {
		return resourceReservationId;
	}
	public void setResourceReservationId(Integer resourceReservationId) {
		this.resourceReservationId = resourceReservationId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId") 
	public WorkPackage getWorkPackage() {
		return workPackage;
	}
	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	
	@Column(name = "reservationDate", length = 45)
	public Date getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId") 
	public WorkShiftMaster getShift() {
		return shift;
	}
	public void setShift(WorkShiftMaster shift) {
		this.shift = shift;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getBlockedUser() {
		return blockedUser;
	}
	public void setBlockedUser(UserList blockedUser) {
		this.blockedUser = blockedUser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservationActionUserId") 
	public UserList getReservationActionUser() {
		return reservationActionUser;
	}
	public void setReservationActionUser(UserList reservationActionUser) {
		this.reservationActionUser = reservationActionUser;
	}
	
	@Column(name = "reservationActionDate", length = 45)
	public Date getReservationActionDate() {
		return reservationActionDate;
	}
	public void setReservationActionDate(Date reservationActionDate) {
		this.reservationActionDate = reservationActionDate;
	}

	public Integer getReservationWeek() {
		return reservationWeek;
	}

	public void setReservationWeek(Integer reservationWeek) {
		this.reservationWeek = reservationWeek;
	}

	@Column(name = "reservationYear")
	public Integer getReservationYear() {
		return reservationYear;
	}

	public void setReservationYear(Integer reservationYear) {
		this.reservationYear = reservationYear;
	}

	@Column(name = "reservationMode")
	public String getReservationMode() {
		return reservationMode;
	}

	public void setReservationMode(String reservationMode) {
		this.reservationMode = reservationMode;
	}

	@Column(name = "groupReservationId")
	public Long getGroupReservationId() {
		return groupReservationId;
	}

	public void setGroupReservationId(Long groupReservationId) {
		this.groupReservationId = groupReservationId;
	}

	@Column(name = "reservationPercentage")
	public Integer getReservationPercentage() {
		return reservationPercentage;
	}

	public void setReservationPercentage(Integer reservationPercentage) {
		this.reservationPercentage = reservationPercentage;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId")
	public UserRoleMaster getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoleMaster userRole) {
		this.userRole = userRole;
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
	@JoinColumn(name = "userTypeId")
	public UserTypeMasterNew getUserType() {
		return userType;
	}

	public void setUserType(UserTypeMasterNew userType) {
		this.userType = userType;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resourcePoolId")
	public TestfactoryResourcePool getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(TestfactoryResourcePool resourcePool) {
		this.resourcePool = resourcePool;
	}
	
}
