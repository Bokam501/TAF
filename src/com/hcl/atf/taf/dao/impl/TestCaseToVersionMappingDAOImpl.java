/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestCaseToVersionMappingDAO;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;

/**
 * @author silambarasur
 *
 */
@Repository
public class TestCaseToVersionMappingDAOImpl implements TestCaseToVersionMappingDAO{
	
private static final Log log = LogFactory.getLog(TestCaseToVersionMappingDAOImpl.class);
	
	 
	@Autowired(required=true)
    private SessionFactory sessionFactory;	

	
	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingList(Integer productId) {
		List<TestCaseProductVersionMapping> testcaseToProductVersionMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseProductVersionMapping.class, "testCaseProductVersionMapping");
			c.createAlias("testCaseProductVersionMapping.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			testcaseToProductVersionMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getTestcaseToProductVersionMappingList",re);
		}
		return testcaseToProductVersionMappingList;
	}
	
	@Override
	@Transactional
	public void mappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseProductVersionMapping);
		}catch(RuntimeException re) {
			log.error("Error in mappingTestCaseToProductVersion",re);
		}
	}
	
	@Override
	@Transactional
	public void unMappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping) {
		try {
			String sql= "delete from test_case_list_has_product_version_list where productId="+testCaseProductVersionMapping.getProduct().getProductId()+" and productVersionListId="+testCaseProductVersionMapping.getVersionId()+" and testcaseId="+testCaseProductVersionMapping.getTestCaseId();
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			log.error("Error in unMappingTestCaseToProductVersion",re);
		}
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId,Integer jtStartIndex, Integer jtPageSize) {
		List<Object[]> mappingTestCaseIds=null;
		List<TestCaseList> testCaseList= null;
		try {
			
			String sql="SELECT DISTINCT testCaseId FROM test_case_list_has_product_version_list mappingTestCaseAndVersion "+
					" LEFT JOIN product_version_list_master versions ON (versions.productVersionListId = mappingTestCaseAndVersion.productVersionListId) "+
					" LEFT JOIN product_master product ON (product.productId = versions.productId) "+
					" WHERE product.productId="+productId;
			if( versionId !=null && versionId >0 ) {
				sql+=" AND mappingTestCaseAndVersion.productVersionListId ="+versionId;
			}
			
			
			 sql+=" ORDER BY testCaseId ASC";
			 mappingTestCaseIds=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			 
			 List<Integer> testCaseIds = new ArrayList<Integer>();
			 if(mappingTestCaseIds != null && mappingTestCaseIds.size() >0) {
				for(Object feature:mappingTestCaseIds) {
					testCaseIds.add(Integer.parseInt(feature.toString()));
				}
			 }
			 
			 Criteria testCaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCase");
			 testCaseCriteria.add(Restrictions.in("testCase.testCaseId", testCaseIds));
			 testCaseList = testCaseCriteria.list();
		}catch(RuntimeException re) {
			log.error("Error in getTestCaseByProductIdAndVersionIdAndBuild",re);
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestCaseListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId) {
		List<TestCaseProductVersionMapping> mappingList = null;
		List<Object[]> mappingIdList= null;
		try {
			
		
			 Criteria mappingCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseProductVersionMapping.class, "mappingTestCaseToVersion");
			 mappingCriteria.createAlias("mappingTestCaseToVersion.product", "product");
			 mappingCriteria.add(Restrictions.eq("product.productId", productId));
			 mappingCriteria.add(Restrictions.eq("mappingTestCaseToVersion.versionId", versionId));
			 
			 mappingList = mappingCriteria.list();
			
		}catch(RuntimeException re) {
			log.error("Error in getTestCaseListByProductIdAndVersionIdAndBuild",re);
		}
		return mappingList;
	}
	
	
	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingId(Integer mappingId) {
		List<TestCaseProductVersionMapping> testcaseToProductVersionMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseProductVersionMapping.class, "testCaseProductVersionMapping");
			c.add(Restrictions.eq("testCaseProductVersionMapping.id", mappingId));
			testcaseToProductVersionMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getTestcaseToProductVersionMappingList",re);
		}
		return testcaseToProductVersionMappingList;
	}
	
	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingList() {
		List<TestCaseProductVersionMapping> testcaseToProductVersionMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseProductVersionMapping.class, "testCaseProductVersionMapping");
			testcaseToProductVersionMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getTestcaseToProductVersionMappingList",re);
		}
		return testcaseToProductVersionMappingList;
	}
}
