/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ISERecommendedTestCaseDAO;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;

/**
 * @author silambarasur
 *
 */
@Repository
public class ISERecommendedTestCaseDAOImpl implements ISERecommendedTestCaseDAO{
	
	private static final Log log = LogFactory.getLog(ISERecommendedTestCaseDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<Object[]> getISERecommendatedTestCaseListByBuildId(Integer buildId) {
		List<Object[]> recommendedTestcaseList= null;
		try {
			String sql="SELECT *, string_agg(DISTINCT testbed, ', ') AS testBeds FROM ise_recommended_testcases "+
					" WHERE buildId = "+buildId;
			sql+=" GROUP BY title,ise_recommended_testcases.id ORDER BY rag_value DESC";
			recommendedTestcaseList =sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
		}catch(RuntimeException re) {
			log.error("Error in getISERecommendatedTestCaseList",re);
		}
		
		return recommendedTestcaseList;
	}
	
	@Override
	@Transactional
	public ISERecommandedTestcases getISERecommendatedTestCaseListByBuildIdAndTestCaseId(Integer buildId,Integer testCaseId) {
		List<ISERecommandedTestcases> iseRecommandedTestcases = null;
		ISERecommandedTestcases iseRecommandedTestcase= null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ISERecommandedTestcases.class, "recommendationTestcase");
			c.createAlias("recommendationTestcase.build", "build");
			c.createAlias("recommendationTestcase.testcase", "testcase");
			c.add(Restrictions.eq("build.productBuildId", buildId));
			c.add(Restrictions.eq("testcase.testCaseId", buildId));
			iseRecommandedTestcases = c.list();
			if(iseRecommandedTestcases != null && iseRecommandedTestcases.size() >0) {
				iseRecommandedTestcase = iseRecommandedTestcases.get(0);
			}
		}catch(RuntimeException re) {
			log.error("Error in getISERecommendatedTestCaseList",re);
		}
		return iseRecommandedTestcase;
	}

	@Override
	@Transactional
	public void addISERecommendedTestCase(ISERecommandedTestcases iseRecommandedTestcases) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(iseRecommandedTestcases);
		}catch(RuntimeException re) {
			log.error("Error in Adding ISE recommended Testcase",re);
		}
		
	}

	@Override
	@Transactional
	public void updateISERecommendedTestCase(ISERecommandedTestcases iseRecommandedTestcases) {
		try {
			sessionFactory.getCurrentSession().update(iseRecommandedTestcases);
		}catch(RuntimeException re) {
			log.error("Error in updation ISE recommended Testcase",re);
		}
		
	}

	@Override
	@Transactional
	public void removeISERecommendationTestcases(Integer buildId) {
		try {
			String sql="delete from ise_recommended_testcases where buildId="+buildId;
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			log.error("Error while removing ise Recommendation testCases",re);
		}
		
	}
	
	@Override
	@Transactional
	public List<Object[]> getISERecommendatedTestCaseCategoryCountByBuildId(Integer buildId) {
		List<Object[]> recommendedTestcaseCategoryCountList= null;
		try {
			String sql="SELECT recommendationCategory, COUNT(DISTINCT title) AS testCaseCount FROM ise_recommended_testcases "+
					" WHERE buildId = "+buildId;
			sql+=" GROUP BY recommendationCategory";
			recommendedTestcaseCategoryCountList =sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
		}catch(RuntimeException re) {
			log.error("Error in getISERecommendatedTestCaseCategoryCountByBuildId",re);
		}
		
		return recommendedTestcaseCategoryCountList;
	}

	@Override
	@Transactional
	public Integer getISERecommendatedTestCaseCountByBuildId(Integer buildId) {
		
		List<Object[]> recommendedTestcaseList= null;
		Integer recommendedTestCaseCount=0;
		try {
			String sql="SELECT COUNT(DISTINCT title) AS testCaseCount,buildId FROM ise_recommended_testcases "+
					" WHERE buildId = "+buildId;
			sql+=" GROUP BY buildId";
			recommendedTestcaseList =sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(recommendedTestcaseList != null && recommendedTestcaseList.size() >0 ) {
				for(Object[] recommendedTescase:recommendedTestcaseList) {
					BigInteger count=(BigInteger)recommendedTescase[0];
					recommendedTestCaseCount =count.intValue();
					
				}
			}
		}catch(RuntimeException re) {
			log.error("Error in getISERecommendatedTestCaseCategoryCountByBuildId",re);
		}
		
		return recommendedTestCaseCount;
	}
	

}
