package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.RiskMasterDAO;
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
import com.hcl.atf.taf.service.RiskListService;

@Service
public class RiskListServiceImpl implements RiskListService {

	@Autowired
	private RiskMasterDAO riskMasterDAO;

	@Override
	@Transactional
	public List<Risk> listRisks(Integer productId, Integer riskStatus, Integer jtStartIndex, Integer jtPageSize) {
		return riskMasterDAO.listRisks(productId, riskStatus, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Risk getByRiskId(Integer riskId) {
		return riskMasterDAO.getByRiskId(riskId);
	}

	@Override
	@Transactional
	public void addRisk(Risk risks) {
		riskMasterDAO.addRisk(risks);
	}

	@Override
	@Transactional
	public void updateRisk(Risk riskFromUI) {
		riskMasterDAO.updateRisk(riskFromUI);
	}

	@Override
	@Transactional
	public Integer getUnMappedTestCaseorFeaturesListCountOfRisksByRiskId(Integer productId, Integer productRiskId, Integer filter) {
		return riskMasterDAO.getUnMappedTestCaseorFeaturesListCountOfRisksByRiskId(productId, productRiskId, filter);
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize) {
		return riskMasterDAO.getUnMappedTestCaseorFeaturesListByProductRiskId(productId, productRiskId, filter, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize){
		return riskMasterDAO.getMappedTestCaseorFeaturesListByProductRiskId(productId, productRiskId, filter, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public TestCaseList updateProductRiskTestCasesOneToMany(Integer testCaseId, Integer productRiskId, String maporunmap) {
		return riskMasterDAO.updateProductRiskTestCasesOneToMany(testCaseId, productRiskId, maporunmap);
	}

	@Override
	@Transactional
	public ProductFeature updateProductRiskFeaturesOneToMany(Integer productFeatureId, Integer productRiskId, String maporunmap) {
		return riskMasterDAO.updateProductRiskFeaturesOneToMany(productFeatureId, productRiskId, maporunmap);
	}

	@Override
	@Transactional
	public List<RiskSeverityMaster> listRiskSeverity(Integer productId, Integer riskSevStatus, Integer jtStartIndex,	Integer jtPageSize) {
		return riskMasterDAO.listRiskSeverity(productId, riskSevStatus, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addRiskSeverity(RiskSeverityMaster riskSeverityMaster) {
		riskMasterDAO.addRiskSeverity(riskSeverityMaster);
	}

	@Override
	@Transactional
	public void updateRiskSeverity(RiskSeverityMaster riskSeverityMasterFromUI) {
		riskMasterDAO.updateRiskSeverity(riskSeverityMasterFromUI);
	}

	@Override
	@Transactional
	public List<RiskLikeHoodMaster> listRiskLikeHood(Integer productId, Integer riskLikeStatus, Integer jtStartIndex,	Integer jtPageSize) {
		return riskMasterDAO.listRiskLikeHood(productId, riskLikeStatus, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMaster) {
		riskMasterDAO.addRiskLikeHood(riskLikeHoodMaster);
	}

	@Override
	@Transactional
	public void updateRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMasterFromUI) {
		riskMasterDAO.updateRiskLikeHood(riskLikeHoodMasterFromUI);
	}

	@Override
	@Transactional
	public List<RiskMitigationMaster> listRiskMitigation(Integer productId, Integer riskMitigationStatus, Integer jtStartIndex,	Integer jtPageSize) {
		return riskMasterDAO.listRiskMitigation(productId, riskMitigationStatus, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addRiskMitigation(RiskMitigationMaster riskMitigationMaster) {
		riskMasterDAO.addRiskMitigation(riskMitigationMaster);
	}

	@Override
	@Transactional
	public void updateRiskMitigation(RiskMitigationMaster riskMitigationFromUI) {
		riskMasterDAO.updateRiskMitigation(riskMitigationFromUI);
	}

	@Override
	@Transactional
	public RiskMitigationMaster getByRiskMitigationId(Integer riskMitigationId) {
		return riskMasterDAO.getByRiskMitigationId(riskMitigationId);
	}

	@Override
	@Transactional
	public RiskMitigationMaster updateProductRiskMitigationsOneToMany(Integer riskMitigationId, Integer productRiskId, String maporunmap) {
		return riskMasterDAO.updateProductRiskMitigationsOneToMany(riskMitigationId, productRiskId, maporunmap);
	}

	@Override
	@Transactional
	public List<RiskRating> listRiskRating(Integer productId, Integer riskRatingStatus, Integer jtStartIndex, Integer jtPageSize) {
		return riskMasterDAO.listRiskRating(productId, riskRatingStatus, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addRiskRating(RiskRating riskRating) {
		riskMasterDAO.addRiskRating(riskRating);
	}

	@Override
	@Transactional
	public void updateRiskRating(RiskRating riskRatingFromUI) {
		riskMasterDAO.updateRiskRating(riskRatingFromUI);
	}

	@Override
	@Transactional
	public List<RiskAssessment> listRiskAssessment(Integer riskId, Integer filter, Integer jtStartIndex, Integer jtPageSize) {
		return riskMasterDAO.listRiskAssessment(riskId, filter,  jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addRiskAssessment(RiskAssessment riskAssessment) {
		riskMasterDAO.addRiskAssessment(riskAssessment);
	}

	@Override
	@Transactional
	public void updateRiskAssessment(RiskAssessment riskAssessmentFromUI) {
		riskMasterDAO.updateRiskAssessment(riskAssessmentFromUI);
	}

	@Override
	@Transactional
	public List<LifeCyclePhase> listLifeCycle() {
		return riskMasterDAO.listLifeCycle();
	}

	@Override
	@Transactional
	public LifeCyclePhase getLifeCyclePhaseBylifeCyclePhaseId(
			Integer lifeCyclePhaseId) {
		return riskMasterDAO.getLifeCyclePhaseBylifeCyclePhaseId(lifeCyclePhaseId);
	}

	@Override
	@Transactional
	public RiskSeverityMaster getRiskSeverityById(Integer riskSeverityId) {
		return riskMasterDAO.getRiskSeverityById(riskSeverityId);
	}

	@Override
	@Transactional
	public RiskLikeHoodMaster getRiskLikeHoodById(Integer riskLikeHoodId) {
		return riskMasterDAO.getRiskLikeHoodById(riskLikeHoodId);
	}

	@Override
	@Transactional
	public RiskMitigationMaster getRiskMitigationById(Integer riskMitigationId) {
		return riskMasterDAO.getRiskMitigationById(riskMitigationId);
	}

	@Override
	@Transactional
	public RiskRating getRiskRatingById(Integer riskRatingId) {
		return riskMasterDAO.getRiskRatingById(riskRatingId);
	}

	@Override
	@Transactional
	public boolean getPreMitigationavailable(Integer riskId) {
		return riskMasterDAO.getPreMitigationavailable(riskId);
	}

	@Override
	@Transactional
	public void updateRiskMatrix(RiskMatrix riskMatrix) {
		riskMasterDAO.updateRiskMatrix(riskMatrix);
	}
	
	@Override
	@Transactional
	public List<Object[]> listRiskRatingMatrix(Integer productId,int riskSeverityId) {
		return riskMasterDAO.listRiskRatingMatrix(productId,riskSeverityId);
	}

	@Override
	@Transactional
	public List<RiskMatrix> listRiskMatrixByRiskSeverityId(int riskSeverityId) {
		return riskMasterDAO.listRiskMatrixByRiskSeverityId(riskSeverityId);
	}

	@Override
	@Transactional
	public List<RiskLikeHoodMaster> listRiskLikeHoodByNotInRiskMatrix(
			Integer productId, Integer[] ristMatrixFromDBArr) {
		return riskMasterDAO.listRiskLikeHoodByNotInRiskMatrix(productId,ristMatrixFromDBArr);
	}

	@Override
	public Integer gerRiskSeverityMasterSizeByProductId(Integer productId) {
		return riskMasterDAO.gerRiskSeverityMasterSizeByProductId(productId);
	}


	@Override
	public List<RiskHazardTraceabilityMatrixDTO> riskHazardTraceService(Integer productId) {
		return riskMasterDAO.getRiskHazardTraceabilityMatrix(productId);
	}

}