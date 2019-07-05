package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestcaseTypeMasterDAO;
import com.hcl.atf.taf.model.TestcaseTypeMaster;


@Repository
public class TestcaseTypeMasterDAOImpl implements TestcaseTypeMasterDAO {
	private static final Log log = LogFactory.getLog(TestcaseTypeMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<TestcaseTypeMaster> list() {		
		List<TestcaseTypeMaster> testcaseTypeMasterList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestcaseTypeMaster.class, "tcm");
			testcaseTypeMasterList = c.list();			
		} catch (HibernateException e) {
			log.error("Unable to fetch TestcaseTypeMaster", e);
		}		
		return testcaseTypeMasterList;
	}

	@Override
	@Transactional
	public TestcaseTypeMaster getTestcaseTypeMasterBytestcaseTypeId(
			int testcaseTypeId) {
		List<TestcaseTypeMaster> testcaseTypeMasterList = null;		
		TestcaseTypeMaster testcaseTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestcaseTypeMaster.class, "ttm");		
			c.add(Restrictions.eq("ttm.testcaseTypeId", testcaseTypeId));
			testcaseTypeMasterList = c.list();			
			testcaseTypeMaster = (testcaseTypeMasterList!=null && testcaseTypeMasterList.size()!=0)?(TestcaseTypeMaster)testcaseTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return testcaseTypeMaster;
		}
	
	@Override
	public TestcaseTypeMaster getTestcaseTypeMasterByName(String name) {
		List<TestcaseTypeMaster> testcaseTypeMasterList = null;		
		TestcaseTypeMaster testcaseTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestcaseTypeMaster.class, "ttm");		
			c.add(Restrictions.eq("ttm.name", name));
			testcaseTypeMasterList = c.list();
			
			testcaseTypeMaster = (testcaseTypeMasterList!=null && testcaseTypeMasterList.size()!=0)?(TestcaseTypeMaster)testcaseTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR", e);
		}		
		log.info("TestcaseTypeMaster By ID -----"+testcaseTypeMasterList.size());
		return testcaseTypeMaster;
	}
		
}
