package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.SkillLevels;

public class JsonSkillLevels implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonSkillLevels.class);
	@JsonProperty
	private Integer skillLevelId;
	@JsonProperty
	private String levelName;	
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer levelValue;	
	

	public JsonSkillLevels() {
	}

	public JsonSkillLevels(SkillLevels skillLevels) {
		this.skillLevelId = skillLevels.getSkillLevelId();
		this.levelName = skillLevels.getLevelName();
		this.description = skillLevels.getDescription();
		this.levelValue = skillLevels.getLevelValue();
	}

	@JsonIgnore
	public SkillLevels getSkillLevels() {
		
		SkillLevels skillLevels = new SkillLevels();
		skillLevels.setSkillLevelId(skillLevelId);
		skillLevels.setLevelName(levelName);
		skillLevels.setDescription(description);
		skillLevels.setLevelName(levelName);
		return skillLevels;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

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

}
