package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "processortype")
public class Processor {
	private Integer processorId;
	private String processorName;
	private String description;
	private SystemType systemType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "processorTypeId", unique = true, nullable = false)
	public Integer getProcessorId() {
		return processorId;
	}
	public void setProcessorId(Integer processorId) {
		this.processorId = processorId;
	}
	@Column(name = "processorName", length = 45)
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	 @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "systemTypeId")
	public SystemType getSystemType() {
		return systemType;
	}
	public void setSystemType(SystemType systemType) {
		this.systemType = systemType;
	}
	
	
}
