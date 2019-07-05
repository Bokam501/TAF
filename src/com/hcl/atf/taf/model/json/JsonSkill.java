package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Skill;

public class JsonSkill implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonSkill.class);

	@JsonProperty
	private Integer skillId;
	@JsonProperty
	private String skillName;
	@JsonProperty
	private String displayName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer parentSkillId;
	@JsonProperty
	private String parentSkillName;
	@JsonProperty
	private Integer leftIndex;
	@JsonProperty
	private Integer rightIndex;
	@JsonProperty
	private Integer isSpecific;
	@JsonProperty
	private Integer entityType;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;

	public JsonSkill() {
	}

	public JsonSkill(Skill skill) {
		this.skillId = skill
				.getSkillId();
		this.skillName = skill.getSkillName();
		this.displayName = skill.getDisplayName();
		this.description = skill.getSkillDescription();
		this.leftIndex = skill.getLeftIndex();
		this.rightIndex = skill.getRightIndex();
		this.isSpecific = skill.getIsSpecific();
		this.entityType = skill.getEntityType();
		this.entityId = skill.getEntityId();
		if(skill.getStatus() != null){
			this.status = skill.getStatus();
		}		
		Skill parentSkill = skill.getParentSkill();
		if(parentSkill != null){
			this.parentSkillId = parentSkill.getSkillId();
			if(parentSkill.getSkillName() != null){
				this.parentSkillName = parentSkill.getSkillName();
			}else{
				this.parentSkillName = null;
			}
		}else{
			this.parentSkillId = 0;
			this.parentSkillName = null;
		}		
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getParentSkillId() {
		return parentSkillId;
	}

	public void setParentSkillId(Integer parentSkillId) {
		this.parentSkillId = parentSkillId;
	}

	public String getParentSkillName() {
		return parentSkillName;
	}

	public void setParentSkillName(String parentSkillName) {
		this.parentSkillName = parentSkillName;
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
	
	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	@JsonIgnore
	public Skill getSkill() {
		Skill skill = new Skill();
		skill.setSkillId(skillId);
		skill.setSkillName(skillName);
		skill.setDisplayName(displayName);
		skill.setSkillDescription(description);
		skill.setIsSpecific(isSpecific);
		skill.setEntityType(entityType);
		skill.setEntityId(entityId);
		skill.setLeftIndex(leftIndex);
		skill.setRightIndex(rightIndex);
		if(this.status != null ){			
			skill.setStatus(status);			
		}else{
			skill.setStatus(0);	
		}		
		Skill parentSkill = new Skill();
		parentSkill.setSkillId(this.parentSkillId);
		skill.setParentSkill(parentSkill);	
		return skill;
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
}
