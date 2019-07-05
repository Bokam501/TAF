package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "defect_type_master")
public class DefectTypeMaster implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer defectTypeId;
	private String defectTypeName;
	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "defectTypeId", unique = true, nullable = false)
	public Integer getDefectTypeId() {
		return defectTypeId;
	}
	public void setDefectTypeId(Integer defectTypeId) {
		this.defectTypeId = defectTypeId;
	}
	
	@Column(name="defectTypeName")
	public String getDefectTypeName() {
		return defectTypeName;
	}
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}
	
}
