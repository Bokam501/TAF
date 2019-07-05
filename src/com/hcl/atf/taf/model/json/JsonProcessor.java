package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Processor;

public class JsonProcessor {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	private Integer processorId;
	@JsonProperty
	private String processorName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer systemTypeId;

public JsonProcessor(Processor processor) {
	this.processorId=processor.getProcessorId();
	this.processorName=processor.getProcessorName();
	this.description=processor.getDescription();
	if(processor.getSystemType() != null){
		this.systemTypeId = processor.getSystemType().getSystemTypeId();	
	}else{
		this.systemTypeId = 0;
	}
	
}
@JsonIgnore
public Processor getProcessor() {
	Processor processor = new Processor();
	return processor;	
}

public Integer getProcessorId() {
	return processorId;
}
public void setProcessorId(Integer processorId) {
	this.processorId = processorId;
}
public String getProcessorName() {
	return processorName;
}
public void setProcessorName(String processorName) {
	this.processorName = processorName;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public Integer getSystemTypeId() {
	return systemTypeId;
}
public void setSystemTypeId(Integer systemTypeId) {
	this.systemTypeId = systemTypeId;
}

}
