package com.hcl.atf.taf.model;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clarification_type")
public class ClarificationTypeMaster {
	
	private static final long serialVersionUID = 1L;
	private Integer clarificationId;
	private String clarificationType;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "clarification_id", unique = true, nullable = false)
	public Integer getClarificationId() {
		return clarificationId;
	}
	public void setClarificationId(Integer clarificationId) {
		this.clarificationId = clarificationId;
	}
	@Column(name="clarification_type")
	public String getClarificationType() {
		return clarificationType;
	}
	public void setClarificationType(String clarificationType) {
		this.clarificationType = clarificationType;
	}
}