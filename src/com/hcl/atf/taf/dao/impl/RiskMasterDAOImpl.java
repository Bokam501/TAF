package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.RiskMasterDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
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
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.dto.RiskHazardTraceabilityMatrixDTO;

@Repository
public class RiskMasterDAOImpl implements RiskMasterDAO {
	private static final Log log = LogFactory.getLog(RiskMasterDAOImpl.class);

	@Autowired(required=true)
	private SessionFactory sessionFactory;
	@Autowired(required=true)
	private TestCaseListDAO testCaseListDAO;
	@Autowired(required=true)
	private ProductFeatureDAO featureDAO;

	@Override
	@Transactional
	public List<Risk> listRisks(Integer productId, Integer riskStatus, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("Risk listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(Risk.class, "risk");
		c.createAlias("risk.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(riskStatus != 2){
			c.add(Restrictions.eq("risk.status", riskStatus));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);

		List<Risk> risks = c.list();
		for (Risk risk : risks) {
			if(risk.getUserList() != null){
				Hibernate.initialize(risk.getUserList());
			}
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}

		log.debug("Total risks-----"+risks.size());
		return risks;
	}

	@Override
	@Transactional
	public Risk getByRiskId(Integer riskId) {
		log.debug("getting Risk instance by id");
		Risk risk=null;
		List<Risk> riskList = new ArrayList<Risk>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Risk.class, "risk");
			c.add(Restrictions.eq("risk.riskId", riskId));
			riskList = c.list();
			risk=(riskList!=null && riskList.size()!=0)?(Risk)riskList.get(0):null;
			if(risk != null){
				Hibernate.initialize(risk.getTestCaseList());
				Hibernate.initialize(risk.getFeatureList());
				Hibernate.initialize(risk.getMitigationList());
				Hibernate.initialize(risk.getUserList());
				if(risk.getProductMaster() != null){
					Hibernate.initialize(risk.getProductMaster());
				}
				if(risk.getAssessmentList() != null){
					Hibernate.initialize(risk.getAssessmentList());
				}
			}
			log.debug("getByRiskId successful");
		} catch (RuntimeException re) {
			log.error("getByRiskId failed", re);
		}
		return risk;
	}

	@Override
	@Transactional
	public void addRisk(Risk risks) {
		log.debug("adding Risk instance");
		try {
			risks.setStatus(1);
			sessionFactory.getCurrentSession().save(risks);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRisk(Risk riskFromUI) {
		log.debug("updating Risk instance");
		try {
			sessionFactory.getCurrentSession().update(riskFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public Integer getUnMappedTestCaseorFeaturesListCountOfRisksByRiskId(Integer productId, Integer productRiskId, Integer filter) {
		log.debug("Get Testcaselist unmapped to Risk");
		int totUnMappedTCFeatureListCount = 0;
		try {
			if (filter == 1){
				totUnMappedTCFeatureListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from test_case_list t where  t.productId=:productMasterId and t.testCaseId not in(select phte.testCaseId from product_risk_has_test_case_list phte where phte.productRiskId=:riskId)").
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).uniqueResult()).intValue();
			}else if(filter == 2){
				totUnMappedTCFeatureListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from product_feature pf where  pf.productId=:productMasterId and pf.productFeatureId not in(select phfe.featureId from product_risk_has_feature_list phfe where phfe.productRiskId=:riskId)").
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).uniqueResult()).intValue();
			}else if(filter == 3){
				totUnMappedTCFeatureListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from risk_mitigation_master rm where  rm.productId=:productMasterId and rm.riskMitigationId not in(select rhml.riskMitigationId from product_risk_has_mitigation_list rhml where rhml.productRiskId=:riskId)").
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).uniqueResult()).intValue();
			}
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totUnMappedTCFeatureListCount;
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("Get UnMappedTCOfHazarad with ProductHazaradId");
		List<Object[]> unMappedTCFeatureListObj = null;
		String sql=null;
		try {
			if (filter == 1){
				sql = "select distinct testCaseId, testCaseName from test_case_list t where  productId=:productMasterId and t.testCaseId not in(select phte.testCaseId from product_risk_has_test_case_list phte where phte.productRiskId=:riskId) limit ";
				sql = sql+jtStartIndex+" ,"+jtPageSize+"";
				unMappedTCFeatureListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("unmapped testcase list :"+unMappedTCFeatureListObj.size());
			}else if(filter == 2){
				sql="select distinct productFeatureId, productFeatureName from product_feature pf where  productId=:productMasterId and pf.productFeatureId not in(select phfe.featureId from product_risk_has_feature_list phfe where phfe.productRiskId=:riskId) limit ";
				sql = sql+jtStartIndex+" ,"+jtPageSize+"";			
				unMappedTCFeatureListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("unmapped feature list :"+unMappedTCFeatureListObj.size());
			}else if(filter == 3){
				sql="select distinct riskMitigationId, rmCode from risk_mitigation_master rm where  productId=:productMasterId and rm.riskMitigationId not in(select rhml.riskMitigationId from product_risk_has_mitigation_list rhml where rhml.productRiskId=:riskId) limit ";
				sql = sql+jtStartIndex+" ,"+jtPageSize+"";			
				unMappedTCFeatureListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("unmapped mitigation list :"+unMappedTCFeatureListObj.size());
			}

		} catch (RuntimeException re) {
			log.error("Get testcaseorfeatureslist Unmapped to ProductHazaradId", re);	
		}
		return unMappedTCFeatureListObj;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseorFeaturesListByProductRiskId(Integer productId, Integer productRiskId, Integer filter, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("Get MappedTCOfRiskwith ProductRiskId");
		List<Object[]> mappedTCFeaturesListObj = null;
		String sql= null;
		try {
			if (filter == 1){
				sql="select distinct testCaseId, testCaseName from test_case_list t where  productId=:productMasterId and t.testCaseId in(select phte.testCaseId from product_risk_has_test_case_list phte where phte.productRiskId=:riskId)";
				mappedTCFeaturesListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("mapped testcase list :"+mappedTCFeaturesListObj.size());
			}else if(filter == 2){
				sql="select distinct productFeatureId, productFeatureName from product_feature pf where  productId=:productMasterId and pf.productFeatureId in(select phfe.featureId from product_risk_has_feature_list phfe where phfe.productRiskId=:riskId)";
				mappedTCFeaturesListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("mapped feature list :"+mappedTCFeaturesListObj.size());
			}else if(filter == 3){
				sql="select distinct riskMitigationId, rmCode from risk_mitigation_master rm where  productId=:productMasterId and rm.riskMitigationId in(select rhml.riskMitigationId from product_risk_has_mitigation_list rhml where rhml.productRiskId=:riskId)";
				mappedTCFeaturesListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productMasterId", productId).
						setParameter("riskId", productRiskId).
						list();
				log.info("mapped mitigation list :"+mappedTCFeaturesListObj.size());
			}

		} catch (RuntimeException re) {
			log.error("Get testcaseorfeaturesormitigations list Mapped to ProductRiskId", re);	
		}
		return mappedTCFeaturesListObj;
	}

	@Override
	@Transactional
	public TestCaseList updateProductRiskTestCasesOneToMany(Integer testCaseId, Integer productRiskId, String maporunmap) {
		log.debug("updateProductRiskTestCases method updating ProductRisk - TestCase Association");
		TestCaseList testCaseList = null;
		Risk productRisk = null;
		Set<TestCaseList> testCaseSet = null;
		try{
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			productRisk = getByRiskId(productRiskId);		
			if (testCaseList != null && productRisk != null){
				if(maporunmap.equalsIgnoreCase("map")){				
					Risk ph = productRisk;
					ph.getTestCaseList().add(testCaseList);
					sessionFactory.getCurrentSession().saveOrUpdate(ph);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					testCaseSet = productRisk.getTestCaseList();
					if(testCaseSet.size() != 0){
						if(testCaseSet.contains(testCaseList)){
							Risk ph1 = productRisk;
							ph1.getTestCaseList().remove(testCaseList);
							sessionFactory.getCurrentSession().saveOrUpdate(ph1);
						}					
					}					
				}				
			}				
		} catch (RuntimeException re) {
			log.error("list specific testcase failed", re);
		}
		return testCaseList;		
	}

	@Override
	@Transactional
	public ProductFeature updateProductRiskFeaturesOneToMany(Integer productFeatureId, Integer productRiskId, String maporunmap) {
		log.debug("updateProductRiskTestCases method updating ProductRisk - TestCase Association");
		ProductFeature featureList = null;
		Risk productRisk = null;
		Set<ProductFeature> featureSet = null;
		try{
			featureList = featureDAO.getByProductFeatureId(productFeatureId);
			productRisk = getByRiskId(productRiskId);		
			if (featureList != null && productRisk != null){
				if(maporunmap.equalsIgnoreCase("map")){				
					Risk ph = productRisk;
					ph.getFeatureList().add(featureList);
					sessionFactory.getCurrentSession().saveOrUpdate(ph);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					featureSet = productRisk.getFeatureList();
					if(featureSet.size() != 0){
						if(featureSet.contains(featureList)){
							Risk ph1 = productRisk;
							ph1.getFeatureList().remove(featureList);
							sessionFactory.getCurrentSession().saveOrUpdate(ph1);
						}					
					}					
				}				
			}				
		} catch (RuntimeException re) {
			log.error("list specific feature failed", re);
		}
		return featureList;		
	}

	@Override
	@Transactional
	public List<RiskSeverityMaster> listRiskSeverity(Integer productId, Integer riskSevStatus, Integer jtStartIndex,	Integer jtPageSize) {
		log.debug("RiskSeverityMaster listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskSeverityMaster.class, "riskSev");
		c.createAlias("riskSev.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(riskSevStatus != 2){
			c.add(Restrictions.eq("riskSev.status", riskSevStatus));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);

		List<RiskSeverityMaster> risks = c.list();
		for (RiskSeverityMaster risk : risks) {
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}
		log.debug("Total risks Severity-----"+risks.size());
		return risks;
	}


	@Override
	@Transactional
	public void addRiskSeverity(RiskSeverityMaster riskSeverityMaster) {
		log.debug("adding RiskSeverityMaster instance");
		try {
			riskSeverityMaster.setStatus(1);
			sessionFactory.getCurrentSession().save(riskSeverityMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRiskSeverity(RiskSeverityMaster riskSeverityMasterFromUI) {
		log.debug("updating RiskSeverityMaster instance");
		try {
			sessionFactory.getCurrentSession().update(riskSeverityMasterFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}


	@Override
	@Transactional
	public List<RiskLikeHoodMaster> listRiskLikeHood(Integer productId, Integer riskLikeStatus, Integer jtStartIndex,	Integer jtPageSize) {
		log.debug("RiskLikeHoodMaster listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskLikeHoodMaster.class, "riskLike");
		c.createAlias("riskLike.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(riskLikeStatus != 2){
			c.add(Restrictions.eq("riskLike.status", riskLikeStatus));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);

		List<RiskLikeHoodMaster> risks = c.list();
		for (RiskLikeHoodMaster risk : risks) {
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}
		log.debug("Total risks LikeHood-----"+risks.size());
		return risks;
	}

	@Override
	@Transactional
	public void addRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMaster) {
		log.debug("adding RiskLikeHoodMaster instance");
		try {
			riskLikeHoodMaster.setStatus(1);
			sessionFactory.getCurrentSession().save(riskLikeHoodMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMasterFromUI) {
		log.debug("updating RiskLikeHoodMaster instance");
		try {
			sessionFactory.getCurrentSession().update(riskLikeHoodMasterFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}


	@Override
	@Transactional
	public List<RiskMitigationMaster> listRiskMitigation(Integer productId, Integer riskMitigationStatus, Integer jtStartIndex,	Integer jtPageSize) {
		log.debug("RiskMitigationMaster listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskMitigationMaster.class, "riskMit");
		c.createAlias("riskMit.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(riskMitigationStatus != 2){
			c.add(Restrictions.eq("riskMit.status", riskMitigationStatus));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);

		List<RiskMitigationMaster> risks = c.list();
		for (RiskMitigationMaster risk : risks) {
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}
		log.debug("Total risks Mitigation -----"+risks.size());
		return risks;
	}

	@Override
	@Transactional
	public void addRiskMitigation(RiskMitigationMaster riskMitigationMaster) {
		log.debug("adding RiskMitigationMaster instance");
		try {
			riskMitigationMaster.setStatus(1);
			sessionFactory.getCurrentSession().save(riskMitigationMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRiskMitigation(RiskMitigationMaster riskMitigationFromUI) {
		log.debug("updating RiskMitigationMaster instance");
		try {
			sessionFactory.getCurrentSession().update(riskMitigationFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public RiskMitigationMaster getByRiskMitigationId(Integer riskMitigationId) {
		log.debug("getting Risk Mitigation instance by id");
		RiskMitigationMaster riskMitigation=null;
		List<RiskMitigationMaster> riskMitigationList = new ArrayList<RiskMitigationMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskMitigationMaster.class, "riskMit");
			c.add(Restrictions.eq("riskMit.riskMitigationId", riskMitigationId));
			riskMitigationList = c.list();
			riskMitigation=(riskMitigationList!=null && riskMitigationList.size()!=0)?(RiskMitigationMaster)riskMitigationList.get(0):null;
			if(riskMitigation != null){
				if(riskMitigation.getProductMaster() != null){
					Hibernate.initialize(riskMitigation.getProductMaster());
				}
			}
			log.debug("getByRiskMitigationId successful");
		} catch (RuntimeException re) {
			log.error("getByRiskMitigationId failed", re);
		}
		return riskMitigation;
	}

	@Override
	@Transactional
	public RiskMitigationMaster updateProductRiskMitigationsOneToMany(Integer riskMitigationId, Integer productRiskId, String maporunmap) {
		log.debug("updateProductRiskMItigations method updating ProductRisk - Mitigation Association");
		RiskMitigationMaster mitigationList = null;
		Risk productRisk = null;
		Set<RiskMitigationMaster> mitigationSet = null;
		try{
			mitigationList = getByRiskMitigationId(riskMitigationId);
			productRisk = getByRiskId(productRiskId);		
			if (mitigationList != null && productRisk != null){
				if(maporunmap.equalsIgnoreCase("map")){				
					Risk ph = productRisk;
					ph.getMitigationList().add(mitigationList);
					sessionFactory.getCurrentSession().saveOrUpdate(ph);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					mitigationSet = productRisk.getMitigationList();
					if(mitigationSet.size() != 0){
						if(mitigationSet.contains(mitigationList)){
							Risk ph1 = productRisk;
							ph1.getMitigationList().remove(mitigationList);
							sessionFactory.getCurrentSession().saveOrUpdate(ph1);
						}					
					}					
				}				
			}				
		} catch (RuntimeException re) {
			log.error("list specific mitigation failed", re);
		}
		return mitigationList;		
	}

	@Override
	public List<RiskRating> listRiskRating(Integer productId, Integer riskRatingStatus, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("RiskRating listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskRating.class, "riskRating");
		c.createAlias("riskRating.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(riskRatingStatus != 2){
			c.add(Restrictions.eq("riskRating.status", riskRatingStatus));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);

		List<RiskRating> risks = c.list();
		for (RiskRating risk : risks) {
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}
		log.debug("Total risks Rating -----"+risks.size());
		return risks;
	}

	@Override
	@Transactional
	public void addRiskRating(RiskRating riskRating) {
		log.debug("adding RiskRating instance");
		try {
			riskRating.setStatus(1);
			sessionFactory.getCurrentSession().save(riskRating);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRiskRating(RiskRating riskRatingFromUI) {
		log.debug("updating RiskRating instance");
		try {
			sessionFactory.getCurrentSession().update(riskRatingFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public List<RiskAssessment> listRiskAssessment(Integer riskId, Integer filter, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("RiskAssessment listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskAssessment.class, "riskAssess");
		c.createAlias("riskAssess.risk", "risk");
		if (filter == 1){
			DetachedCriteria userSubquery1 = DetachedCriteria.forClass(RiskAssessment.class, "risk1")
					.add(Restrictions.eq("risk1.mitigationType", 1 ))
					.add(Restrictions.eq("risk1.risk.riskId", riskId))
					.setProjection(Projections.min("risk1.modifiedDate"));

			DetachedCriteria userSubquery2 = DetachedCriteria.forClass(RiskAssessment.class, "risk2")
					.add(Restrictions.eq("risk2.mitigationType", 2 ))
					.add(Restrictions.eq("risk2.risk.riskId", riskId))
					.setProjection(Projections.max("risk2.modifiedDate"));

			c.add(Restrictions.or(Subqueries.propertyIn("riskAssess.modifiedDate", userSubquery1), Subqueries.propertyIn("riskAssess.modifiedDate", userSubquery2)));
		}else{
			c.add(Restrictions.eq("risk.riskId", riskId));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		}

		List<RiskAssessment> riskAss = c.list();
		for (RiskAssessment riskAs : riskAss) {
			if(riskAs.getRiskRating() != null){
				Hibernate.initialize(riskAs.getRiskRating());
			}
			if(riskAs.getRiskSeverity() != null){
				Hibernate.initialize(riskAs.getRiskSeverity());
			}
			if(riskAs.getRiskLikeHood() != null){
				Hibernate.initialize(riskAs.getRiskLikeHood());
			}
			if(riskAs.getRisk() != null){
				Hibernate.initialize(riskAs.getRisk());
			}
			if(riskAs.getUserList() != null){
				Hibernate.initialize(riskAs.getUserList());
			}
			if(riskAs.getLifeCyclePhase() != null){
				Hibernate.initialize(riskAs.getLifeCyclePhase());
			}
		}
		log.debug("Total risks Assessment-----"+riskAss.size());
		return riskAss;
	}


	@Override
	@Transactional
	public void addRiskAssessment(RiskAssessment riskAssessment) {
		log.debug("adding RiskAssessment instance");
		try {
			riskAssessment.setStatus(1);
			sessionFactory.getCurrentSession().save(riskAssessment);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateRiskAssessment(RiskAssessment riskAssessmentFromUI) {
		log.debug("updating RiskAssessment instance");
		try {
			sessionFactory.getCurrentSession().update(riskAssessmentFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public List<LifeCyclePhase> listLifeCycle() {
		log.debug("LifeCyclePhase listing");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(LifeCyclePhase.class, "life");
		List<LifeCyclePhase> life = c.list();
		log.debug("Total Life Cycle Phase-----"+life.size());
		return life;
	}


	@Override
	@Transactional
	public LifeCyclePhase getLifeCyclePhaseBylifeCyclePhaseId(Integer lifeCyclePhaseId) {
		log.debug("getLifeCyclePhaseBylifeCyclePhaseId instance");
		LifeCyclePhase lifeCyclePhase = null;
		try {
			List<LifeCyclePhase> lifeCycleList = new ArrayList<LifeCyclePhase>();		
			Criteria c = sessionFactory.getCurrentSession().createCriteria(LifeCyclePhase.class, "life");
			c.add(Restrictions.eq("life.lifeCyclePhaseId", lifeCyclePhaseId));
			lifeCycleList = c.list();
			lifeCyclePhase=(lifeCycleList!=null && lifeCycleList.size()!=0)?(LifeCyclePhase)lifeCycleList.get(0):null;
		} catch (HibernateException e) {
			log.error("getLifeCyclePhaseBylifeCyclePhaseId failed", e);
		}
		log.debug("Life Cycle Phase-----"+lifeCyclePhase.getLifeCyclePhaseId());
		return lifeCyclePhase;
	}
	@Override
	@Transactional
	public RiskSeverityMaster getRiskSeverityById(Integer riskSeverityId) {
		log.debug("getting RiskSeverityMaster instance by id");
		RiskSeverityMaster risk=null;
		List<RiskSeverityMaster> riskList = new ArrayList<RiskSeverityMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskSeverityMaster.class, "riskSev");
			c.add(Restrictions.eq("riskSev.riskSeverityId", riskSeverityId));
			riskList = c.list();
			risk=(riskList!=null && riskList.size()!=0)?(RiskSeverityMaster)riskList.get(0):null;
			if(risk != null){
				if(risk.getProductMaster() != null){
					Hibernate.initialize(risk.getProductMaster());
				}
				if(risk.getRiskAssessment() != null){
					Hibernate.initialize(risk.getRiskAssessment());
				}
			}
			log.debug("getRiskSeverityById successful");
		} catch (RuntimeException re) {
			log.error("getRiskSeverityById failed", re);
		}
		return risk;
	}

	@Override
	@Transactional
	public RiskLikeHoodMaster getRiskLikeHoodById(Integer riskLikeHoodId) {
		log.debug("getting RiskLikeHoodMaster instance by id");
		RiskLikeHoodMaster risk=null;
		List<RiskLikeHoodMaster> riskList = new ArrayList<RiskLikeHoodMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskLikeHoodMaster.class, "riskLike");
			c.add(Restrictions.eq("riskLike.riskLikeHoodId", riskLikeHoodId));
			riskList = c.list();
			risk=(riskList!=null && riskList.size()!=0)?(RiskLikeHoodMaster)riskList.get(0):null;
			if(risk != null){
				if(risk.getProductMaster() != null){
					Hibernate.initialize(risk.getProductMaster());
				}
				if(risk.getRiskAssessment() != null){
					Hibernate.initialize(risk.getRiskAssessment());
				}
			}
			log.debug("getRiskLikeHoodById successful");
		} catch (RuntimeException re) {
			log.error("getRiskLikeHoodById failed", re);
		}
		return risk;
	}

	@Override
	@Transactional
	public RiskMitigationMaster getRiskMitigationById(Integer riskMitigationId) {
		log.debug("getting RiskMitigationMaster instance by id");
		RiskMitigationMaster risk=null;
		List<RiskMitigationMaster> riskList = new ArrayList<RiskMitigationMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskMitigationMaster.class, "riskMit");
			c.add(Restrictions.eq("riskMit.riskMitigationId", riskMitigationId));
			riskList = c.list();
			risk=(riskList!=null && riskList.size()!=0)?(RiskMitigationMaster)riskList.get(0):null;
			if(risk != null){
				if(risk.getProductMaster() != null){
					Hibernate.initialize(risk.getProductMaster());
				}
				if(risk.getRiskList() != null){
					Hibernate.initialize(risk.getRiskList());
				}
			}
			log.debug("getRiskMitigationById successful");
		} catch (RuntimeException re) {
			log.error("getRiskMitigationById failed", re);
		}
		return risk;
	}


	@Override
	@Transactional
	public RiskRating getRiskRatingById(Integer riskRatingId) {
		log.debug("getting RiskLikeHoodMaster instance by id");
		RiskRating risk=null;
		List<RiskRating> riskList = new ArrayList<RiskRating>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskRating.class, "riskRating");
			c.add(Restrictions.eq("riskRating.riskRatingId", riskRatingId));
			riskList = c.list();
			risk=(riskList!=null && riskList.size()!=0)?(RiskRating)riskList.get(0):null;
			if(risk != null){
				if(risk.getProductMaster() != null){
					Hibernate.initialize(risk.getProductMaster());
				}
				if(risk.getRiskAssessment() != null){
					Hibernate.initialize(risk.getRiskAssessment());
				}
			}
			log.debug("getRiskLikeHoodById successful");
		} catch (RuntimeException re) {
			log.error("getRiskLikeHoodById failed", re);
		}
		return risk;
	}

	@Override
	@Transactional
	public boolean getPreMitigationavailable(Integer riskId) {
		log.debug("getPreMitigationavailable listing");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskAssessment.class, "riskAssess");
		c.createAlias("riskAssess.risk", "risk");
		c.add(Restrictions.eq("risk.riskId", riskId));
		c.add(Restrictions.eq("riskAssess.mitigationType", 1));
		List<RiskAssessment> riskAss = c.list();
		if (riskAss.size()>0){
			log.info("getPreMitigationavailable is available");
			return true;
		}
		return false;
	}

	@Override
	public List<Object[]> listRiskRatingMatrix(Integer productId,int riskSeverityId) {
		List<Object[]> listObj = new ArrayList<Object[]>();
		
		String sql="";
		try {
			if(riskSeverityId==-1){
				sql = "SELECT rs.riskSeverityId,rs.severityName, rs.severityRating, rl.riskLikeHoodId, rl.likeHoodName, rm.riskRatingId, rr.ratingName FROM risk_severity_master rs LEFT JOIN risk_matrix  rm ON rm.riskSeverityId=rs.riskSeverityId LEFT JOIN risk_likehood_master rl ON rm.riskLikeHoodId=rl.riskLikeHoodId LEFT JOIN risk_rating rr ON rm.riskRatingId = rr.riskRatingId WHERE rs.productId="+productId;
			}
			
			listObj = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			log.debug("Result Set Size : " + listObj.size());
			} catch (Exception e) {
				log.error("List failed", e);
				return null;
			}
			log.debug("list all sucessfull: "+listObj.size());
		return listObj;
	}


	@Override
	@Transactional
	public void updateRiskMatrix(RiskMatrix riskMatrix) {
		log.debug("updating RiskMatrix instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(riskMatrix);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}

	}

	@Override
	@Transactional
	public List<RiskMatrix> listRiskMatrixByRiskSeverityId(int riskSeverityId) {
		log.debug("getPreMitigationavailable listing");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskMatrix.class, "riskMatrix");
		c.createAlias("riskMatrix.riskSeverity", "riskSeverity");
		c.add(Restrictions.eq("riskSeverity.riskSeverityId", riskSeverityId));
		
		List<RiskMatrix> riskMatrixList = c.list();
		if (riskMatrixList != null){
			for(RiskMatrix riskMatrix:riskMatrixList){
				Hibernate.initialize(riskMatrix.getRiskSeverity());
				Hibernate.initialize(riskMatrix.getRiskRating());
				Hibernate.initialize(riskMatrix.getRiskLikeHood());
			}
			log.debug("getPreMitigationavailable is available");
			
		}
		return riskMatrixList;
	}

	@Override
	@Transactional
	public List<RiskLikeHoodMaster> listRiskLikeHoodByNotInRiskMatrix(Integer productId, Integer[] ristMatrixFromDBArr) {
		String sql=  "from RiskLikeHoodMaster as riskLike join  where id not in (select jobId from Production)";
		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskLikeHoodMaster.class, "riskLike");
		c.createAlias("riskLike.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		 if(ristMatrixFromDBArr.length!=0){
				c.add(Restrictions.not(
					    Restrictions.in("riskLike.riskLikeHoodId", ristMatrixFromDBArr)
					  )) ;
		 }
	
		List<RiskLikeHoodMaster> risks = c.list();
		for (RiskLikeHoodMaster risk : risks) {
			if(risk.getProductMaster() != null){
				Hibernate.initialize(risk.getProductMaster());
			}
		}
		return risks;
	}

	@Override
	@Transactional
	public Integer gerRiskSeverityMasterSizeByProductId(Integer productId) {
		log.debug("RiskSeverityMaster listing");

		Criteria c = sessionFactory.getCurrentSession().createCriteria(RiskSeverityMaster.class, "riskSev");
		c.createAlias("riskSev.productMaster", "product");
		if(productId != null){
			c.add(Restrictions.eq("product.productId", productId));
		}
		
		Integer riskSeverityCount = ((Number)c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		log.debug("Total risks Severity-----"+riskSeverityCount);
		return riskSeverityCount;
	}
	
	@Override
	@Transactional
	public List<RiskHazardTraceabilityMatrixDTO> getRiskHazardTraceabilityMatrix(Integer productId) {
		List<RiskHazardTraceabilityMatrixDTO>  rhtmdto = new ArrayList<RiskHazardTraceabilityMatrixDTO>();
		List<Risk> riskList = new ArrayList<Risk>();
		List<Object[]> riskObjArray = new ArrayList<Object[]>();
		List<Object[]> riskObjArray1 = new ArrayList<Object[]>();
		
		log.info("Get the Risk Hazard Traceability matrix with respect to product id");		
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Risk.class, "risk");
			c.createAlias("risk.productMaster", "prod");
			c.createAlias("risk.featureList", "pfl");
			c.createAlias("risk.testCaseList", "tcl");
			c.createAlias("risk.assessmentList", "rassess");
			c.createAlias("risk.mitigationList", "miti");
			c.add((Restrictions.eq("prod.productId", productId)));
			c.addOrder(Order.asc("risk.riskId"));
			c.addOrder(Order.asc("tcl.testCaseId"));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("risk.riskId").as("riskId"));
			projectionList.add(Property.forName("risk.riskName").as("riskName"));
			projectionList.add(Property.forName("pfl.productFeatureId").as("featureId"));
			projectionList.add(Property.forName("miti.rmCode").as("rmCode"));
			projectionList.add(Property.forName("tcl.testCaseId").as("testCaseId"));			
			projectionList.add(Projections.groupProperty("tcl.testCaseId"));
			projectionList.add(Projections.groupProperty("risk.riskId"));
			c.setProjection(projectionList);			
			riskObjArray = c.list();
			RiskHazardTraceabilityMatrixDTO rhazarddto = null;
			for (Object[] objects : riskObjArray) {
			 rhazarddto = new RiskHazardTraceabilityMatrixDTO();
			 	if(objects[0] != null){
			 		rhazarddto.setRiskId((Integer)objects[0]);
			 	}
			 	if(objects[1] != null){
			 		rhazarddto.setRiskName((String)objects[1]);
			 	}
			 	if(objects[2] != null){
			 		rhazarddto.setFeatureId((Integer)objects[2]);
			 	}
			 	if(objects[3] != null){
			 		rhazarddto.setRiskMitigationCode((String)objects[3]);
			 	}
			 	if(objects[4] != null){
			 		rhazarddto.setTestCaseId((Integer)objects[4]);
			 	}
			 	//Severity
			 	DetachedCriteria detachedPostMitigation = DetachedCriteria.forClass(RiskSeverityMaster.class,"rsm");
				detachedPostMitigation.createAlias("rsm.riskAssessment", "rassess");
				detachedPostMitigation.createAlias("rassess.risk", "risk");
				detachedPostMitigation.createAlias("rsm.productMaster", "prod");				
				detachedPostMitigation.add(Restrictions.eq("rassess.mitigationType", 2));
				detachedPostMitigation.add(Restrictions.eq("prod.productId", productId));
				if((Integer)objects[0] != null)
				detachedPostMitigation.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
				detachedPostMitigation.setProjection(Projections.max("rassess.modifiedDate") );
				
				
				
				Criteria totallist = sessionFactory.getCurrentSession().createCriteria(RiskSeverityMaster.class,"rsm");
				totallist.createAlias("rsm.riskAssessment", "rassess");		
				totallist.add(Property.forName("rassess.modifiedDate").eq(detachedPostMitigation));
				totallist.setProjection(Projections.projectionList().add(Property.forName("rsm.severityName")));
						
				riskObjArray = null;
				riskObjArray = totallist.list();
				for (Object object : riskObjArray) {
					String postmitigationSeverityname=(String)object;
					rhazarddto.setSeverityName2(postmitigationSeverityname);
				}
				
				DetachedCriteria detachedPrePostMitigation = DetachedCriteria.forClass(RiskSeverityMaster.class,"rsm");
				detachedPrePostMitigation.createAlias("rsm.riskAssessment", "rassess");
				detachedPrePostMitigation.createAlias("rassess.risk", "risk");
				detachedPrePostMitigation.createAlias("rsm.productMaster", "prod");			
				detachedPrePostMitigation.add(Restrictions.eq("rassess.mitigationType", 1));
				detachedPrePostMitigation.add(Restrictions.eq("prod.productId", productId));
				if((Integer)objects[0] != null)
				detachedPrePostMitigation.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
				detachedPrePostMitigation.setProjection(Projections.min("rassess.modifiedDate"));
				
				totallist = null;
				totallist = sessionFactory.getCurrentSession().createCriteria(RiskSeverityMaster.class,"rsm");
				totallist.createAlias("rsm.riskAssessment", "rassess");		
				totallist.add(Property.forName("rassess.modifiedDate").eq(detachedPrePostMitigation));
				totallist.setProjection(Projections.projectionList().add(Property.forName("rsm.severityName")));
				
				riskObjArray = null;
				riskObjArray = totallist.list();	
				for (Object object : riskObjArray) {
					String premitigationSeverityname=(String)object;
					rhazarddto.setSeverityName1(premitigationSeverityname);
				}
				
				//Likelihood
			 	DetachedCriteria detachedPostMitigationLikeli = DetachedCriteria.forClass(RiskLikeHoodMaster.class,"rlm");
			 	detachedPostMitigationLikeli.createAlias("rlm.riskAssessment", "rassess");
			 	detachedPostMitigationLikeli.createAlias("rassess.risk", "risk");
			 	detachedPostMitigationLikeli.createAlias("rlm.productMaster", "prod");			
			 	detachedPostMitigationLikeli.add(Restrictions.eq("rassess.mitigationType", 2));
			 	detachedPostMitigationLikeli.add(Restrictions.eq("prod.productId", productId));
			 	if((Integer)objects[0] != null)
			 	detachedPostMitigationLikeli.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
			 	detachedPostMitigationLikeli.setProjection(Projections.max("rassess.modifiedDate"));
				
				Criteria totallistLikeli = sessionFactory.getCurrentSession().createCriteria(RiskLikeHoodMaster.class,"rlm");
				totallistLikeli.createAlias("rlm.riskAssessment", "rassess");		
				totallistLikeli.add(Property.forName("rassess.modifiedDate").eq(detachedPostMitigationLikeli));
				totallistLikeli.setProjection(Projections.projectionList().add(Property.forName("rlm.likeHoodName")));
						
				riskObjArray = null;
				riskObjArray = totallistLikeli.list();
				for (Object object : riskObjArray) {
					String postmitigationLikelihoodName=(String)object;
					rhazarddto.setLikeliHoodName2(postmitigationLikelihoodName);
				}
				
				DetachedCriteria detachedPreMitigationLikeli = DetachedCriteria.forClass(RiskLikeHoodMaster.class,"rlm");
				detachedPreMitigationLikeli.createAlias("rlm.riskAssessment", "rassess");
				detachedPreMitigationLikeli.createAlias("rassess.risk", "risk");
				detachedPreMitigationLikeli.createAlias("rlm.productMaster", "prod");			
				detachedPreMitigationLikeli.add(Restrictions.eq("rassess.mitigationType", 1));
				detachedPreMitigationLikeli.add(Restrictions.eq("prod.productId", productId));
				if((Integer)objects[0] != null)
				detachedPreMitigationLikeli.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
				detachedPreMitigationLikeli.setProjection(Projections.min("rassess.modifiedDate"));
				
				totallist = null;
				totallist = sessionFactory.getCurrentSession().createCriteria(RiskLikeHoodMaster.class,"rlm");
				totallist.createAlias("rlm.riskAssessment", "rassess");		
				totallist.add(Property.forName("rassess.modifiedDate").eq(detachedPreMitigationLikeli));
				totallist.setProjection(Projections.projectionList().add(Property.forName("rlm.likeHoodName")));
				
				riskObjArray = null;
				riskObjArray = totallist.list();	
				for (Object object : riskObjArray) {
					String premitigationLikelihoodName=(String)object;
					rhazarddto.setLikeliHoodName1(premitigationLikelihoodName);
				}
				
				//RiskPriority
			 	DetachedCriteria detachedPostMitigationPriority = DetachedCriteria.forClass(RiskRating.class,"rr");
			 	detachedPostMitigationPriority.createAlias("rr.riskAssessment", "rassess");
			 	detachedPostMitigationPriority.createAlias("rassess.risk", "risk");
			 	detachedPostMitigationPriority.createAlias("rr.productMaster", "prod");			
			 	detachedPostMitigationPriority.add(Restrictions.eq("rassess.mitigationType", 2));
			 	detachedPostMitigationPriority.add(Restrictions.eq("prod.productId", productId));
			 	if((Integer)objects[0] != null)
			 	detachedPostMitigationPriority.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
			 	detachedPostMitigationPriority.setProjection(Projections.max("rassess.modifiedDate"));
				
				Criteria totallistPriority = sessionFactory.getCurrentSession().createCriteria(RiskRating.class,"rr");
				totallistPriority.createAlias("rr.riskAssessment", "rassess");		
				totallistPriority.add(Property.forName("rassess.modifiedDate").eq(detachedPostMitigationPriority));
				totallistPriority.setProjection(Projections.projectionList().add(Property.forName("rr.ratingName")));
						
				riskObjArray = null;
				riskObjArray = totallistPriority.list();
				for (Object object : riskObjArray) {
					String postmitigationPriority=(String)object;
					rhazarddto.setRiskPriority2(postmitigationPriority);
				}
				
				DetachedCriteria detachedPreMitigationPriority = DetachedCriteria.forClass(RiskRating.class,"rr");
				detachedPreMitigationPriority.createAlias("rr.riskAssessment", "rassess");
				detachedPreMitigationPriority.createAlias("rassess.risk", "risk");
				detachedPreMitigationPriority.createAlias("rr.productMaster", "prod");			
				detachedPreMitigationPriority.add(Restrictions.eq("rassess.mitigationType", 1));
				detachedPreMitigationPriority.add(Restrictions.eq("prod.productId", productId));
				if((Integer)objects[0] != null)
				detachedPreMitigationPriority.add(Restrictions.eq("risk.riskId", (Integer)objects[0]));
				detachedPreMitigationPriority.setProjection(Projections.min("rassess.modifiedDate"));
				
				totallist = null;
				totallist = sessionFactory.getCurrentSession().createCriteria(RiskRating.class,"rr");
				totallist.createAlias("rr.riskAssessment", "rassess");		
				totallist.add(Property.forName("rassess.modifiedDate").eq(detachedPreMitigationPriority));
				totallist.setProjection(Projections.projectionList().add(Property.forName("rr.ratingName")));
				
				riskObjArray = null;
				riskObjArray = totallist.list();	
				for (Object object : riskObjArray) {
					String premitigationPriorityname=(String)object;
					rhazarddto.setRiskPriority1(premitigationPriorityname);
				}
				
				Criteria cRisk = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "testRunJob");
				cRisk.createAlias("testRunJob.workPackageTestCaseExecutionPlans", "wptcep");
				cRisk.createAlias("wptcep.testCaseExecutionResult", "tcer");
				cRisk.createAlias("wptcep.testCase", "tcl");
				cRisk.createAlias("tcl.risk", "risktc");
				cRisk.createAlias("testRunJob.workPackage", "wp");
				cRisk.createAlias("wp.lifeCyclePhase", "lcp");			
				cRisk.add((Restrictions.eq("tcl.testCaseId", (Integer)objects[4])));
				cRisk.add((Restrictions.eq("risktc.riskId", (Integer)objects[0])));
				ProjectionList projectList = Projections.projectionList();
				projectList.add(Property.forName("tcer.result").as("result"));
				projectList.add(Property.forName("wp.iterationNumber").as("iterationNumber"));
				projectList.add(Property.forName("lcp.name").as("phase"));
				projectList.add(Property.forName("tcer.endTime").as("date"));
				projectList.add(Property.forName("tcer.testCaseExecutionResultId").as("tcerid"));
				projectList.add(Property.forName("tcl.testCaseName").as("testcasename"));
				cRisk.setProjection(projectList);	
				riskObjArray1 = cRisk.list();
				RiskHazardTraceabilityMatrixDTO rhazardto = null;
				for (Object[] riskobjects : riskObjArray1) {
					rhazardto = new RiskHazardTraceabilityMatrixDTO();
				 	if(riskobjects[0] != null){
				 		rhazardto.setResult((String)riskobjects[0]);
				 	}
				 	if(riskobjects[1] != null){
				 		rhazardto.setIterationNumber((Integer)riskobjects[1]);
				 	}
				 	if(riskobjects[2] != null){
				 		rhazardto.setLifecyclephase((String)riskobjects[2]);
				 	}
				 	if(riskobjects[3] != null){
				 		rhazardto.setDate((Date)riskobjects[3]);
				 	}	
				 	if(riskobjects[4] != null){
				 		rhazardto.setTestcaseexeid((Integer)riskobjects[4]);
				 	}
				 	if(riskobjects[5] != null){
				 		rhazardto.setTestcasename((String)riskobjects[5]);
				 	}
				}
				
				rhazarddto.setIterationNumber(rhazardto.getIterationNumber());
				rhazarddto.setLifecyclephase(rhazardto.getLifecyclephase());
				rhazarddto.setResult(rhazardto.getResult());
				rhazarddto.setDate(rhazardto.getDate());
				rhazarddto.setTestcaseexeid(rhazardto.getTestcaseexeid());
				rhazarddto.setTestcasename(rhazardto.getTestcasename());
			 	rhtmdto.add(rhazarddto);
			 	
			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in Risk Traceability Matrix DAO IMPL");
		}
		return rhtmdto;
	}

}
