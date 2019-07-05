/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ToolTypeMaster;


/**
 * @author silambarasur
 *
 */
public class JsonToolTypeMaster {

	@JsonProperty
	private Integer typeId;
	@JsonProperty
	private String typeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	
	public JsonToolTypeMaster(){
		
	}
	
	public JsonToolTypeMaster(ToolTypeMaster toolTypeMaster) {
		this.typeId=toolTypeMaster.getId();
		this.typeName = toolTypeMaster.getName();
		this.description = toolTypeMaster.getDescription();
		
		this.status = toolTypeMaster.getStatus();
		
	}
	
	public ToolTypeMaster getToolTypeMaster() {
		ToolTypeMaster toolTypeMasterk = new ToolTypeMaster();
		toolTypeMasterk.setId(typeId);
		toolTypeMasterk.setName(typeName);
		toolTypeMasterk.setDescription(description);
		toolTypeMasterk.setStatus(status);
		return toolTypeMasterk;
	}
	
	
	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	
}
