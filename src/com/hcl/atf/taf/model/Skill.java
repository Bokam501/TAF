package com.hcl.atf.taf.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "skill")
public class Skill implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer skillId;
	

	@Column(name = "skillName")
	private String skillName;
	@Column(name = "displayName")
	private String displayName;
	@Column(name = "skillDescription")
	private String skillDescription;
	@Column(name = "status")
	private Integer status;
	private Skill parentSkill;	

	@Column(name = "leftIndex")
	private Integer leftIndex;
	@Column(name = "rightIndex")
	private Integer rightIndex;
	@Column(name = "isSpecific")
	private Integer isSpecific;
	@Column(name = "entityType")
	private Integer entityType;
	@Column(name = "entityId")
	private Integer entityId;
	/*@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;*/
	
	//private Set<UserList> userList = new HashSet<UserList>(0);
	private Set<Skill> childCategories = new HashSet<Skill>(0);
	public Skill() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "skillId", unique = true, nullable = false)
	public Integer getSkillId() {
		return skillId;
	}

	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	
	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	

	public String getSkillDescription() {
		return skillDescription;
	}

	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentSkillId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public Skill getParentSkill() {
		return parentSkill;
	}

	public void setParentSkill(Skill parentSkill) {
		this.parentSkill = parentSkill;
	}
	
	public Integer getLeftIndex() {
		return leftIndex;
	}

	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	public Integer getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}	

	public Integer getIsSpecific() {
		return isSpecific;
	}

	public void setIsSpecific(Integer isSpecific) {
		this.isSpecific = isSpecific;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
	public Set<UserList> getUserList() {
		return this.userList;
	}
	
	public void setUserList(Set<UserList> userList) {
		this.userList = userList;
	}*/
	
	/*@Temporal(TemporalType.TIMESTAMP)
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
	}*/

	
	/*private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>(0);
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "decouplingcategory_has_test_case_list", joinColumns = { @JoinColumn(name = "decouplingCategoryId") }, inverseJoinColumns = { @JoinColumn(name = "testCaseId") })
	public Set<TestCaseList> getTestCaseList() {
		return this.testCaseList;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}	*/

	@Override
	public boolean equals(Object o) {		
		Skill dc = (Skill) o;
		if (this.skillId == dc.getSkillId())
			return true;
		else
			return false;
	}	
	
	@Override
	public int hashCode(){
	    return (int) skillId;
	  }

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentSkill", cascade=CascadeType.ALL)
	public Set<Skill> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(Set<Skill> childCategories) {
		this.childCategories = childCategories;
	}
}
