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
@Table(name = "user_skills")
public class UserSkills implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer userSkillId;		
	private Skill skill;	
	private UserList user;
	private SkillLevels selfSkillLevel;	
	private UserList approvingManager;	
	private SkillLevels managerSkillLevel;
	
	private Date fromDate;	
	private Date toDate;
	
	private String userName;
	
	private String userCode;
	
	@Column(name = "selfIsPrimary")
	private Integer selfIsPrimary;
	@Column(name = "status")
	private Integer status;	
	@Column(name = "managerIsPrimary")
	private Integer managerIsPrimary;
	@Column(name = "isApproved")
	private Integer isApproved;
	public UserSkills() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userSkillId", unique = true, nullable = false)
	public Integer getUserSkillId() {
		return userSkillId;
	}

	public void setUserSkillId(Integer userSkillId) {
		this.userSkillId = userSkillId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selfSkillId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId" )
	@NotFound(action=NotFoundAction.IGNORE)	
	public UserList getUser() {
		return user;
	}

	public void setUser(UserList user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selfSkillLevelId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public SkillLevels getSelfSkillLevel() {
		return selfSkillLevel;
	}

	public void setSelfSkillLevel(SkillLevels selfSkillLevel) {
		this.selfSkillLevel = selfSkillLevel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvingManagerId" )
	@NotFound(action=NotFoundAction.IGNORE)	
	public UserList getApprovingManager() {
		return approvingManager;
	}

	public void setApprovingManager(UserList approvingManager) {
		this.approvingManager = approvingManager;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managerSkillLevelId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public SkillLevels getManagerSkillLevel() {
		return managerSkillLevel;
	}

	public void setManagerSkillLevel(SkillLevels managerSkillLevel) {
		this.managerSkillLevel = managerSkillLevel;
	}
	
	public Integer getSelfIsPrimary() {
		return selfIsPrimary;
	}

	public void setSelfIsPrimary(Integer selfIsPrimary) {
		this.selfIsPrimary = selfIsPrimary;
	}	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fromDate")
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "toDate")
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Column(name = "managerIsPrimary")
	public Integer getManagerIsPrimary() {
		return managerIsPrimary;
	}

	public void setManagerIsPrimary(Integer managerIsPrimary) {
		this.managerIsPrimary = managerIsPrimary;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}	

	@Override
	public boolean equals(Object o) {		
		UserSkills usk = (UserSkills) o;
		if (this.userSkillId == usk.getUserSkillId())
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode(){
	    return (int) userSkillId;
	  }

	@Column(name="userName")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="userCode")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	
	
}
