package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestToolMasterDAO;
import com.hcl.atf.taf.model.TestToolMaster;

@Repository
public class TestCategoryMasterDAOImpl implements TestToolMasterDAO {
	private static final Log log = LogFactory.getLog(TestCategoryMasterDAOImpl.class);

	@Autowired(required=true)
	private SessionFactory sessionFactory;



	@Override
	@Transactional
	public List<TestToolMaster> list() {
		log.debug("listing all TestToolMaster instance");
		List<TestToolMaster> testToolMaster=null;
		try {
			testToolMaster=sessionFactory.getCurrentSession().createQuery("from TestToolMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return testToolMaster;
	}



	@Override
	@Transactional
	public TestToolMaster getTestToolMaster(int parseInt) {
		TestToolMaster testToolMaster=null;
		List<TestToolMaster> testToolMasterList=null;
		try {
			testToolMasterList=sessionFactory.getCurrentSession().createQuery("from TestToolMaster where testToolId="+parseInt).list();
			testToolMaster = (testToolMasterList != null && testToolMasterList.size() != 0) ? (TestToolMaster) testToolMasterList
					.get(0) : null;
					log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testToolMaster;
	}



	@Override
	@Transactional
	public TestToolMaster getTestToolIdByName(String testEngine) {
		TestToolMaster testToolMaster=null;
		List<TestToolMaster> testToolMasterList=null;
		try {
			testToolMasterList = sessionFactory.getCurrentSession().createQuery("from TestToolMaster where testToolName=:testEngine").setParameter("testEngine", testEngine).list();
			for(Object tm : testToolMasterList){
				testToolMaster = (TestToolMaster)tm;
				break;
			}
		
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testToolMaster;
	}	
}
