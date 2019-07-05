package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EngagementTypeMaster;

public class JsonEngagementTypeMaster implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonEngagementTypeMaster.class);

	@JsonProperty
	private Integer engagementTypeId;	
	@JsonProperty
	private String engagementTypeName;
	@JsonProperty
	private String description;

	public JsonEngagementTypeMaster() {
		
	}

	public JsonEngagementTypeMaster(EngagementTypeMaster engagementTypeMaster) {
		this.engagementTypeId = engagementTypeMaster.getEngagementTypeId();
		this.engagementTypeName = engagementTypeMaster.getEngagementTypeName();
		this.description = engagementTypeMaster.getDescription();
	}
	
	public Integer getEngagementTypeId() {
		return engagementTypeId;
	}

	public void setEngagementTypeId(Integer engagementTypeId) {
		this.engagementTypeId = engagementTypeId;
	}

	public String getEngagementTypeName() {
		return engagementTypeName;
	}

	public void setEngagementTypeName(String engagementTypeName) {
		this.engagementTypeName = engagementTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
