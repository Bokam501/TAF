package com.hcl.atf.taf.model;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "skill_levels")
public class SkillLevels implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer skillLevelId;
	

	@Column(name = "levelName")
	private String levelName;	
	@Column(name = "description")
	private String description;
	@Column(name = "levelValue")
	private Integer levelValue;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "skillLevelId", unique = true, nullable = false)
	public Integer getSkillLevelId() {
		return skillLevelId;
	}

	public void setSkillLevelId(Integer skillLevelId) {
		this.skillLevelId = skillLevelId;
	}
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(Integer levelValue) {
		this.levelValue = levelValue;
	}

	public SkillLevels() {
	}

	@Override
	public boolean equals(Object o) {		
		SkillLevels dc = (SkillLevels) o;
		if (this.skillLevelId == dc.getSkillLevelId())
			return true;
		else
			return false;
	}	
	
	@Override
	public int hashCode(){
	    return (int) skillLevelId;
	  }

	
}
