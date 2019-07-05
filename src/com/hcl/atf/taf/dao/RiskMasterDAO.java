package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.Risk;
import com.hcl.atf.taf.model.RiskAssessment;
import com.hcl.atf.taf.model.RiskLikeHoodMaster;
import com.hcl.atf.taf.model.RiskMatrix;
import com.hcl.atf.taf.model.RiskMitigationMaster;
import com.hcl.atf.taf.model.RiskRating;
import com.hcl.atf.taf.model.RiskSeverityMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.dto.RiskHazardTraceabilityMatrixDTO;

public interface RiskMasterDAO  {	 

	List<Risk> listRisks(Integer productId, Integer riskStatus, Integer jtStartIndex, Integer jtPageSize);
	Risk getByRiskId(Integer riskId);
	void addRisk(Risk risks);
	void updateRisk(Risk riskFromUI);
	Integer getUnMappedTestCaseorFeaturesListCountOfRisksByRiskId(Integer productId, Integer productRiskId, Integer filter);
	List<Object[]> getUnMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize);
	TestCaseList updateProductRiskTestCasesOneToMany(Integer testCaseId, Integer productRiskId, String maporunmap);
	ProductFeature updateProductRiskFeaturesOneToMany(Integer productFeatureId, Integer productRiskId, String maporunmap);
	List<RiskSeverityMaster> listRiskSeverity(Integer productId, Integer riskSevStatus, Integer jtStartIndex,	Integer jtPageSize);
	void addRiskSeverity(RiskSeverityMaster riskSeverityMaster);
	void updateRiskSeverity(RiskSeverityMaster riskSeverityMasterFromUI);
	List<RiskLikeHoodMaster> listRiskLikeHood(Integer productId, Integer riskLikeStatus, Integer jtStartIndex,	Integer jtPageSize);
	void addRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMaster);
	void updateRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMasterFromUI);
	List<RiskMitigationMaster> listRiskMitigation(Integer productId, Integer riskMitigationStatus, Integer jtStartIndex, Integer jtPageSize);
	void addRiskMitigation(RiskMitigationMaster riskMitigationMaster);
	void updateRiskMitigation(RiskMitigationMaster riskMitigationFromUI);
	RiskMitigationMaster getByRiskMitigationId(Integer riskMitigationId);
	RiskMitigationMaster updateProductRiskMitigationsOneToMany(Integer riskMitigationId, Integer productRiskId, String maporunmap);
	List<RiskRating> listRiskRating(Integer productId, Integer riskRatingStatus, Integer jtStartIndex, Integer jtPageSize);
	void addRiskRating(RiskRating riskRating);
	void updateRiskRating(RiskRating riskRatingFromUI);
	List<RiskAssessment> listRiskAssessment(Integer riskId, Integer filter, Integer jtStartIndex, Integer jtPageSize);
	void addRiskAssessment(RiskAssessment riskAssessment);
	void updateRiskAssessment(RiskAssessment riskAssessmentFromUI);
	List<LifeCyclePhase> listLifeCycle();
	LifeCyclePhase getLifeCyclePhaseBylifeCyclePhaseId(Integer lifeCyclePhaseId);
	RiskSeverityMaster getRiskSeverityById(Integer riskSeverityId);
	RiskLikeHoodMaster getRiskLikeHoodById(Integer riskLikeHoodId);
	RiskMitigationMaster getRiskMitigationById(Integer riskMitigationId);
	RiskRating getRiskRatingById(Integer riskRatingId);
	boolean getPreMitigationavailable(Integer riskId);
	List<Object[]> listRiskRatingMatrix(Integer productId, int riskSeverityId);
	void updateRiskMatrix(RiskMatrix riskMatrix);
	List<RiskMatrix> listRiskMatrixByRiskSeverityId(int riskSeverityId);
	List<RiskLikeHoodMaster> listRiskLikeHoodByNotInRiskMatrix(
			Integer productId, Integer[] ristMatrixFromDBArr);
	Integer gerRiskSeverityMasterSizeByProductId(Integer productId);
	List<RiskHazardTraceabilityMatrixDTO> getRiskHazardTraceabilityMatrix(Integer productId);
	
}
