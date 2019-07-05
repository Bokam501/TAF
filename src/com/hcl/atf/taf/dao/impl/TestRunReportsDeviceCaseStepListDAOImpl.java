package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceCaseStepListDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;

@Repository
public class TestRunReportsDeviceCaseStepListDAOImpl implements TestRunReportsDeviceCaseStepListDAO {
	private static final Log log = LogFactory.getLog(TestRunReportsDeviceCaseStepListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	

	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAll() {
		log.debug("listing all TestRunReportsDeviceCaseStepList instance(listAll method)");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList=null;
		try {
			testRunReportsDeviceCaseStepList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testRunReportsDeviceCaseStepList;
	}
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseStepList> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunReportsDeviceCaseStepList instance");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList=null;
		try {
			testRunReportsDeviceCaseStepList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseStepList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseStepList> list(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId){
		log.debug("listing TestRunReportsDeviceCaseStepList instance");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList=null;
		try {
			testRunReportsDeviceCaseStepList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList where testRunListId=:testRunListId and testRunConfigurationChildId=:testRunConfigurationChildId and testCaseId=:testCaseId").setParameter("testRunListId", testRunListId).setParameter("testRunConfigurationChildId", testRunConfigurationChildId).setParameter("testCaseId", testCaseId)
					.setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseStepList;       
    }
	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listForEvidenceGrid(Integer testrunno, Integer testRunConfigurationChildId) {
		log.debug("listForEvidenceGrid method - listing TestRunReportsDeviceCaseStepList instance");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList=null;
		try {
			String sql="select * from testrunreports_teststeps_devicedetails_view where testRunNo=:testRunNo order by testRunListId,testCaseId asc";
			testRunReportsDeviceCaseStepList = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("testRunNo", testrunno)
					.setResultTransformer(Transformers.aliasToBean(TestRunReportsDeviceCaseStepList.class))
					.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseStepList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceCaseStepList> list(Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId){
		log.debug("listing TestRunReportsDeviceCaseStepList instance");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList=null;
		try {
			testRunReportsDeviceCaseStepList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList where testRunListId=:testRunListId and testRunConfigurationChildId=:testRunConfigurationChildId and testCaseId=:testCaseId").setParameter("testRunListId", testRunListId).setParameter("testRunConfigurationChildId", testRunConfigurationChildId).setParameter("testCaseId", testCaseId)
											.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceCaseStepList;       
    }
	@Override
	@Transactional
	
	public TestRunReportsDeviceCaseStepList getByTestRunReportsDeviceCaseStepListId(int testRunListId){
		log.debug("getting TestRunReportsDeviceCaseStepList instance by Job id");
		TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList t where testRunListId=:testRunListId").setParameter("testRunListId", testRunListId)
					.list();
			testRunReportsDeviceCaseStepList=(list!=null && list.size()!=0)?(TestRunReportsDeviceCaseStepList)list.get(0):null;
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunReportsDeviceCaseStepList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunReportsDeviceCaseStepList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from testrunreports_teststeps_devicedetails_view").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfEvidence(Integer testRunNo, Integer testRunConfigurationChildId) {
		log.debug("getting TestRunReportsDeviceCaseStepList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from testrunreports_teststeps_devicedetails_view")
					.setParameter("testRunNo", testRunNo)
					.setParameter("testRunConfigurationChildId", testRunConfigurationChildId)
					.uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public int getTotalRecordsFilteredTestRunReportsDeviceCaseStepList(Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId) {
		log.debug("getting TestRunReportsDeviceList Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from testrunreports_teststeps_devicedetails_view");
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
			if(testCaseId!=-1){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testCaseId=:testCaseId");
				isANDrequired=true;
			}
			
			
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(qry.toString());
			
			if(testRunListId!=-1){
				query.setParameter("testRunListId", testRunListId);
			}
			if(testRunConfigurationChildId!=null){
				query.setParameter("testRunConfigurationChildId", testRunConfigurationChildId);
			}
			if(testCaseId!=-1){
				query.setParameter("testCaseId", testCaseId);
			}
			
			count=((Number) query.uniqueResult()).intValue();
			
			
			
			log.debug("total Filtered records fetch successful");
		} catch (RuntimeException re) {
			log.error("total Filtered records fetch failed", re);			
		}
		return count;
	
	}
	
	
	//Added for getting the List of testStepReports for the job id
	@Override
	@Transactional
	
	public List<TestRunReportsDeviceCaseStepList> getByJobId(int testRunJobId){
		log.debug("getting TestRunReportsDeviceCaseStepList instance by Job id");
		 List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepsList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceCaseStepList t where testRunListId=:testRunJobId").setParameter("testRunJobId", testRunJobId)
					.list();
			if(list!=null && list.size()!=0){
				testRunReportsDeviceCaseStepsList = list;
					log.info("getByTestJobId successful and Result data obtained");
			} else {
				log.info("No Result Data in Test ");
			}
		} catch (Exception exe) {
			log.error("Getting TestResults by getByJobId failed", exe);			
		}
		return testRunReportsDeviceCaseStepsList;
	}
}