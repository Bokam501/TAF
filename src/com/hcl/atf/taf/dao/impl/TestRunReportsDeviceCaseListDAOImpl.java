package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceCaseListDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;


@Repository
public class TestRunReportsDeviceCaseListDAOImpl implements TestRunReportsDeviceCaseListDAO {
	private static final Log log = LogFactory.getLog(TestRunReportsDeviceCaseListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	

	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> listAll() {
		log.debug("listing all TestRunReportsDeviceCaseList instance");
		List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseList=null;
		try {
			testRunReportsDeviceCaseList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return testRunReportsDeviceCaseList;
	}
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseList> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunReportsDeviceCaseList instance");
		List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseList=null;
		try {
			testRunReportsDeviceCaseList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseList> list(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId){
		log.debug("listing TestRunReportsDeviceCaseList instance");
		List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseList=null;
		try {
			testRunReportsDeviceCaseList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList where testRunListId=:testRunListId and testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunListId", testRunListId).setParameter("testRunConfigurationChildId", testRunConfigurationChildId)
					.setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseList> list(Integer testRunListId,Integer testRunConfigurationChildId){
		log.debug("listing TestRunReportsDeviceCaseList instance");
		List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseList=null;
		try {
			testRunReportsDeviceCaseList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList where testRunListId=:testRunListId and testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunListId", testRunListId).setParameter("testRunConfigurationChildId", testRunConfigurationChildId).list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		
		return testRunReportsDeviceCaseList;       
    }
	@Override
	@Transactional
	public TestRunReportsDeviceCaseList getByTestRunReportsDeviceCaseListId(int testRunListId){
		log.debug("getting TestRunReportsDeviceList instance by id");
		TestRunReportsDeviceCaseList testRunReportsDeviceCaseList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList t where testRunListId=:testRunListId").setParameter("testRunListId", testRunListId)
					.list();
			testRunReportsDeviceCaseList=(list!=null && list.size()!=0)?(TestRunReportsDeviceCaseList)list.get(0):null;
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunReportsDeviceCaseList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunReportsDeviceCaseList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from testrunreports_testcases_devicedetails_view").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}


	

	@Override
	@Transactional
	public int getTotalRecordsFilteredTestRunReportsDeviceCaseList(Integer testRunListId,Integer testRunConfigurationChildId) {
		log.debug("getting TestRunReportsDeviceList Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from testrunreports_testcases_devicedetails_view");
			boolean isANDrequired=false;
			if(testRunListId!=-1){
				qry.append(" e where");
				qry.append(" e.testRunListId=:testRunListId");
				isANDrequired=true;
			}
					
			if(testRunConfigurationChildId!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunConfigurationChildId=:testRunConfigurationChildId");
				isANDrequired=true;
			}
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(qry.toString());
			
			if(testRunListId!=-1){
				query.setParameter("testRunListId", testRunListId);
			}
			if(testRunConfigurationChildId!=null){
				query.setParameter("testRunConfigurationChildId", testRunConfigurationChildId);
			}
			
			
			count=((Number) query.uniqueResult()).intValue();
			
			
			
			log.debug("total Filtered records fetch successful");
		} catch (RuntimeException re) {
			log.error("total Filtered records fetch failed", re);			
		}
		return count;
	
	}

	//Added for iLCM TAF integration - Generating Excel reports  - Bugzilla Id 717
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> getTestRunReportsByTestRunJobId(int testRunJobId) {

		log.debug("getting List of TestRunReportsDeviceList instance for testRunJob id");
		List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseLists=null;
		try {
			testRunReportsDeviceCaseLists = sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseList t where testRunListId=:testRunListId").setParameter("testRunListId", testRunJobId)
					.list();
			
			if(testRunReportsDeviceCaseLists !=  null && !testRunReportsDeviceCaseLists.isEmpty()){
				log.debug("getByTesRunId successful.Data obtained for given TestRunJob");
			} else {
				log.debug("No Data obtained for given TestRunJob");
			}			
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunReportsDeviceCaseLists;       
	}
}