package com.hcl.atf.taf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="amdocs_page_methods")
public class AmdocsPageMethods implements Serializable{
	
	private Integer amdocsPageMethodId;
	private String methodName;
	private String params;
	private String paramTypes;
	private String returnType;
	private AmdocsPageObjects amdocsPageObjects;
	
	public AmdocsPageMethods(){
		
	}

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="amdocsPageMethodId", unique=true, nullable=false)
	public Integer getAmdocsPageMethodId() {
		return amdocsPageMethodId;
	}

	public void setAmdocsPageMethodId(Integer amdocsPageMethodId) {
		this.amdocsPageMethodId = amdocsPageMethodId;
	}

	@Column(name="methodName")
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name="params")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name="paramTypes")
	public String getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(String paramTypes) {
		this.paramTypes = paramTypes;
	}

	@Column(name="returnType")
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="amdocsPageObjectId")
	public AmdocsPageObjects getAmdocsPageObjects() {
		return amdocsPageObjects;
	}

	public void setAmdocsPageObjects(AmdocsPageObjects amdocsPageObjects) {
		this.amdocsPageObjects = amdocsPageObjects;
	}

	
}
