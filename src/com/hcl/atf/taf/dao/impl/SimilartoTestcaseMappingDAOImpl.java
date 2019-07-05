/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.SimilartoTestcaseMappingDAO;
import com.hcl.atf.taf.model.SimilartoTestcaseMapping;

/**
 * @author silambarasur
 *
 */
@Repository
public class SimilartoTestcaseMappingDAOImpl implements SimilartoTestcaseMappingDAO{
	
	private static final Log log = LogFactory.getLog(SimilartoTestcaseMappingDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByProductId(Integer productId) {
		List<SimilartoTestcaseMapping> similarTestcaseMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SimilartoTestcaseMapping.class, "similarTestCaseMapping");
			c.createAlias("similarTestCaseMapping.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			similarTestcaseMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getSimilarToTestcaseMappingListByProductId",re);
		}
		return similarTestcaseMappingList;
	}
	
	
	@Override
	@Transactional
	public List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByTestcaseId(Integer testCaseId) {
		List<SimilartoTestcaseMapping> similarTestcaseMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SimilartoTestcaseMapping.class, "similarTestCaseMapping");
			c.add(Restrictions.eq("similarTestCaseMapping.testCaseId", testCaseId));
			similarTestcaseMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getSimilarToTestcaseMappingListByTestcaseId",re);
		}
		return similarTestcaseMappingList;
	}
	
	
	@Override
	@Transactional
	public void mappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping) {
		try {
			sessionFactory.getCurrentSession().save(similartoTestcaseMapping);
		}catch(RuntimeException re) {
			log.error("Error in mappingSimilarToTestcase",re);
		}
	}
	
	@Override
	@Transactional
	public void unMappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping) {
		try {
			String sql= "delete from similar_to_testcase_mapping where productId="+similartoTestcaseMapping.getProduct().getProductId()+" and testcaseId="+similartoTestcaseMapping.getTestCaseId();
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			log.error("Error in mappingProductFeatureToProductBuild",re);
		}
	}
	
	
	

}
