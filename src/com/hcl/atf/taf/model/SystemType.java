package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "systemtype")
public class SystemType {

	private Integer systemTypeId;
	private String name;
	private String description;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "systemTypeId", unique = true, nullable = false)
	public Integer getSystemTypeId() {
		return systemTypeId;
	}
	public void setSystemTypeId(Integer systemTypeId) {
		this.systemTypeId = systemTypeId;
	}
	@Column(name = "name", length = 45)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
