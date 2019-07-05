package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "testcase_priority")
public class TestCasePriority implements java.io.Serializable{

	private Integer testcasePriorityId;	
	private String priorityName;
	private Integer priorityLevelValue;
	private String description;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testcasePriorityId", unique = true, nullable = false)
	public Integer getTestcasePriorityId() {
		return testcasePriorityId;
	}
	public void setTestcasePriorityId(Integer testcasePriorityId) {
		this.testcasePriorityId = testcasePriorityId;
	}
	
	@Column(name = "priorityName", length = 1000)
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	
	@Column(name = "priorityLevelValue")
	public Integer getPriorityLevelValue() {
		return priorityLevelValue;
	}
	public void setPriorityLevelValue(Integer priorityLevelValue) {
		this.priorityLevelValue = priorityLevelValue;
	}
	
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
