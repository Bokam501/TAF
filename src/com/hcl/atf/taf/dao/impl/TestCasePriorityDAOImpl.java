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

import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.model.TestCasePriority;


@Repository
public class TestCasePriorityDAOImpl implements TestCasePriorityDAO {
	private static final Log log = LogFactory.getLog(TestCasePriorityDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<TestCasePriority> list() {		
		List<TestCasePriority> testCasePriorityList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCasePriority.class, "tcasePriority");
			testCasePriorityList = c.list();			
		} catch (HibernateException e) {
			log.error("Unable to fetch TestCasePriorities", e);
		}		
		return testCasePriorityList;
	}

	@Override
	@Transactional
	public TestCasePriority getTestCasePriorityBytestcasePriorityId(
			int testcasePriorityId) {
		List<TestCasePriority> testCasePriorityList = null;		
		TestCasePriority testCasePriority = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCasePriority.class, "tcp");		
			c.add(Restrictions.eq("tcp.testcasePriorityId", testcasePriorityId));
			testCasePriorityList = c.list();
			
			testCasePriority = (testCasePriorityList!=null && testCasePriorityList.size()!=0)?(TestCasePriority)testCasePriorityList.get(0):null;
		} catch (HibernateException e) {
			log.error("Unable to fetch TestCasePriority By Id", e);
		}		
		return testCasePriority;
	}

	@Override
	@Transactional
	public TestCasePriority getPrioirtyByName(String priroirtyName) {
		List<TestCasePriority> testCasePriorityList = null;		
		TestCasePriority testCasePriority = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCasePriority.class, "tcp");		
			c.add(Restrictions.eq("tcp.priorityName", priroirtyName));
			testCasePriorityList = c.list();
			
			testCasePriority = (testCasePriorityList!=null && testCasePriorityList.size()!=0)?(TestCasePriority)testCasePriorityList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return testCasePriority;
	}

}
