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

import com.hcl.atf.taf.dao.TestCategoryMasterDAO;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestToolMaster;

@Repository
public class TestToolMasterDAOImpl implements TestCategoryMasterDAO {
	private static final Log log = LogFactory.getLog(TestToolMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<TestCategoryMaster> list() {
		log.debug("listing all TestCategoryMaster instance");
		List<TestCategoryMaster> testCategoryMaster=null;
		try {
			testCategoryMaster=sessionFactory.getCurrentSession().createQuery("from TestCategoryMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testCategoryMaster;
	}

	@Override
	@Transactional
	public TestToolMaster getTestToolById(Integer testToolId) {
		
		TestToolMaster testTool = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestToolMaster.class, "tt");
			c.add(Restrictions.eq("tt.testToolId", testToolId));

			List<Object> list = c.list();
			testTool = (list!=null && list.size()!=0)?(TestToolMaster)list.get(0):null;

		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testTool;
	}
	
	@Override
	@Transactional
	public TestToolMaster getTestToolByName(String testToolName) {
		
		TestToolMaster testTool = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestToolMaster.class, "tt");
			c.add(Restrictions.eq("tt.testToolName", testToolName));

			List<Object> list = c.list();
			testTool = (list!=null && list.size()!=0)?(TestToolMaster)list.get(0):null;

		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testTool;
	}

}
