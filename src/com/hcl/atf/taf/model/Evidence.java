package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TestEnviromentMaster generated by hbm2java
 */
@Entity
@Table(name = "evidence")
public class Evidence implements java.io.Serializable {

	private Integer evidenceid;
	private EntityMaster entityMaster;
	private String fileuri;
	private String filetype;
	private Long size;
	private String evidencename;
	private String description;
	private Integer entityvalue;
	private EvidenceType evidenceType;

	public Evidence() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "evidenceid", unique = true, nullable = false)
	public Integer getEvidenceid() {
		return evidenceid;
	}



	public void setEvidenceid(Integer evidenceid) {
		this.evidenceid = evidenceid;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entitymasterid") 
	public EntityMaster getEntityMaster() {
		return entityMaster;
	}


	public void setEntityMaster(EntityMaster entityMaster) {
		this.entityMaster = entityMaster;
	}


	@Column(name = "fileuri")
	public String getFileuri() {
		return fileuri;
	}



	


	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}


	@Column(name = "filetype", length = 100)
	public String getFiletype() {
		return filetype;
	}



	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}


	@Column(name = "size", length = 100)
	public Long getSize() {
		return size;
	}



	public void setSize(Long size) {
		this.size = size;
	}

	@Column(name = "evidencename")
	public String getEvidencename() {
		return evidencename;
	}


	public void setEvidencename(String evidencename) {
		this.evidencename = evidencename;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "entityvalue")
	public Integer getEntityvalue() {
		return entityvalue;
	}


	public void setEntityvalue(Integer entityvalue) {
		this.entityvalue = entityvalue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evidenceTypeId") 
	public EvidenceType getEvidenceType() {
		return evidenceType;
	}


	public void setEvidenceType(EvidenceType evidenceType) {
		this.evidenceType = evidenceType;
	}

	
	
	
	
}