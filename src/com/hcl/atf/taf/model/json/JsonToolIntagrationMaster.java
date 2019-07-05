/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.ToolTypeMaster;


/**
 * @author silambarasur
 *
 */
public class JsonToolIntagrationMaster {

	@JsonProperty
	private Integer toolIntagrationId;
	@JsonProperty
	private String name;
	@JsonProperty
	private Integer toolTypeId;
	@JsonProperty
	private String toolTypeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String version;
	@JsonProperty
	private Integer status;
	
	public JsonToolIntagrationMaster(){
		
	}
	
	public JsonToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster) {
		this.toolIntagrationId=toolIntagrationMaster.getId();
		this.name = toolIntagrationMaster.getName();
		this.description = toolIntagrationMaster.getDescription();
		this.version = toolIntagrationMaster.getVersion();
		if(toolIntagrationMaster.getToolType() != null) {
			this.toolTypeId=toolIntagrationMaster.getToolType().getId();
			this.toolTypeName=toolIntagrationMaster.getToolType().getName();
		}
		this.status = toolIntagrationMaster.getStatus();
		
	}
	
	@JsonIgnore
	public ToolIntagrationMaster getToolIntagrationMaster() {
		ToolIntagrationMaster toolIntagrationMaster = new ToolIntagrationMaster();
		toolIntagrationMaster.setId(toolIntagrationId);
		toolIntagrationMaster.setName(name);
		toolIntagrationMaster.setDescription(description);
		toolIntagrationMaster.setStatus(status);
		toolIntagrationMaster.setVersion(version);
			if(toolTypeId != null) {
				ToolTypeMaster toolType = new ToolTypeMaster();
				toolType.setId(toolTypeId);
				toolIntagrationMaster.setToolType(toolType);
			}
		return toolIntagrationMaster;
	}
	
	
	public Integer getToolIntagrationId() {
		return toolIntagrationId;
	}

	public void setToolIntagrationId(Integer toolIntagrationId) {
		this.toolIntagrationId = toolIntagrationId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getToolTypeId() {
		return toolTypeId;
	}
	public void setToolTypeId(Integer toolTypeId) {
		this.toolTypeId = toolTypeId;
	}
	public String getToolTypeName() {
		return toolTypeName;
	}
	public void setToolTypeName(String toolTypeName) {
		this.toolTypeName = toolTypeName;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
