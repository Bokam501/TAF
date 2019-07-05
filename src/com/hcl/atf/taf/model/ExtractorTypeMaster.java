package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "data_extractor_type")
public class ExtractorTypeMaster implements Serializable {

	private static final long serialVersionUID = 378236606538876680L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "extractorName")
	private String extarctorName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExtarctorName() {
		return extarctorName;
	}

	public void setExtarctorName(String extarctorName) {
		this.extarctorName = extarctorName;
	}
	
}
