package com.hcl.atf.taf.model;

// Generated Aug 18, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TestExecutionResultBugList generated by hbm2java
 */
@Entity
@Table(name = "defect_feature_mapping")
public class DefectFeatureMapping implements java.io.Serializable {

	private TestExecutionResultBugList testExecutionResultBugList;	
	private ProductFeature feature;
	private Integer defectFeatureMappingId;	
	private Integer explicitMapping;
	private Integer analyticsMappingConfidence;
	private Integer analyticsMappingConfirmation;	
	private UserList user;	
	private Date dateOfConfrimation;
	
	public DefectFeatureMapping() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "defectFeatureMappingId", unique = true, nullable = false)
	public Integer getDefectFeatureMappingId() {
		return defectFeatureMappingId;
	}

	public void setDefectFeatureMappingId(Integer defectFeatureMappingId) {
		this.defectFeatureMappingId = defectFeatureMappingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testExecutionResultBugId", nullable = false)
	public TestExecutionResultBugList getTestExecutionResultBugList() {
		return testExecutionResultBugList;
	}

	public void setTestExecutionResultBugList(
			TestExecutionResultBugList testExecutionResultBugList) {
		this.testExecutionResultBugList = testExecutionResultBugList;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productFeatureId", nullable = false)
	public ProductFeature getFeature() {
		return feature;
	}

	public void setFeature(ProductFeature feature) {
		this.feature = feature;
	}	
	
	@Column(name="explicitMapping")
	public Integer getExplicitMapping() {
		return explicitMapping;
	}

	public void setExplicitMapping(Integer explicitMapping) {
		this.explicitMapping = explicitMapping;
	}	

	@Column(name="analyticsMappingConfidence")
	public Integer getAnalyticsMappingConfidence() {
		return analyticsMappingConfidence;
	}

	public void setAnalyticsMappingConfidence(Integer analyticsMappingConfidence) {
		this.analyticsMappingConfidence = analyticsMappingConfidence;
	}

	@Column(name="analyticsMappingConfirmation")
	public Integer getAnalyticsMappingConfirmation() {
		return analyticsMappingConfirmation;
	}

	public void setAnalyticsMappingConfirmation(Integer analyticsMappingConfirmation) {
		this.analyticsMappingConfirmation = analyticsMappingConfirmation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public UserList getUser() {
		return user;
	}

	public void setUser(UserList user) {
		this.user = user;
	}

	@Column(name="dateOfConfrimation")
	public Date getDateOfConfrimation() {
		return dateOfConfrimation;
	}

	public void setDateOfConfrimation(Date dateOfConfrimation) {
		this.dateOfConfrimation = dateOfConfrimation;
	}
	
	@Override
	public boolean equals(Object o) {
		
		DefectFeatureMapping defectFeatureMapping = (DefectFeatureMapping) o;
		if (this.defectFeatureMappingId == defectFeatureMapping.getDefectFeatureMappingId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) defectFeatureMappingId;
	  }

}
