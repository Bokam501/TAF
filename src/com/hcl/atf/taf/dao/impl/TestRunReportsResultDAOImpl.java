package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsResultDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsList;

@Repository
public class TestRunReportsResultDAOImpl implements TestRunReportsResultDAO {

	private static final Log log = LogFactory.getLog(TestRunReportsResultDAOImpl.class);
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	    
	@Override
	@Transactional
	public List<TestRunReportsList> listAll() {
		log.debug("listing all TestRunReportList instance");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			testRunReportsList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testRunReportsList;
	}
	
	@Override
	@Transactional
    public List<TestRunReportsList> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunReportsList instance");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			testRunReportsList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsList> listAll(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName){
		log.debug("listing TestRunReportsList instance");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			
			testRunReportsList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsList")
					.setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsList;       
    }
	@Override
	@Transactional
	public TestRunReportsList getByTestRunReportListId(int testRunNo){
		log.debug("getting TestExecutionResult instance by id");
		TestRunReportsList testRunReportsList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunReportsList t where testRunNo=:testRunNo").setParameter("testRunNo",testRunNo)
					.list();
			testRunReportsList=(list!=null && list.size()!=0)?(TestRunReportsList) list.get(0):null;
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunReportsList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestExecutionResult total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_reports_list_view").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestRunReportsList> list(int testRunNo) {
		log.debug("listing specific TestExecutionResult");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			testRunReportsList=sessionFactory.getCurrentSession().createQuery("from testRunReportsList where testRunNo=:testRunNo")
														.setParameter("testRunNo", testRunNo).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsList;
	}

	@Override
	@Transactional
	public List<TestRunReportsList> list(int testRunNo, int startIndex, int pageSize) {
		log.debug("listing specific TestExecutionResult instance");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			testRunReportsList=sessionFactory.getCurrentSession().createQuery("from testRunReportsList where testRunNo=:testRunNo")
														.setParameter("testRunNo", testRunNo).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsList;
	}

	@Override
	@Transactional
	public int getTotalRecordsFilteredtestRunReportsList(int testRunNo, String productName, String productVersionListName) {
		log.debug("getting TestExecutionResult Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from test_run_reports_list_view");
			boolean isANDrequired=false;
			if(testRunNo!=-1){
				qry.append(" e where");
				qry.append(" e.testRunNo=:testRunNo");
				isANDrequired=true;
			}
					
			if(productName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productName=:productName");
				isANDrequired=true;
			}
			if(productVersionListName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productVersionListName=:productVersionListName");
				isANDrequired=true;
			}
			
			
			
			
			Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());
			
			if(testRunNo!=-1){
				query.setParameter("testRunNo", testRunNo);
			}
			if(productName!=null){
				query.setParameter("productName", productName);
			}
			if(productVersionListName!=null){
				query.setParameter("productVersionListName", productVersionListName);
			}
			
			
			count=((Number) query.uniqueResult()).intValue();
			
			
			
			log.debug("total Filtered records fetch successful");
		} catch (RuntimeException re) {
			log.error("total Filtered records fetch failed", re);			
		}
		return count;
	
	}
	
	@Override
	@Transactional
	public List<TestRunReportsList> listFilteredTestRunReportsList(
			int testRunNo, String productName, String productVersionListName) {
		log.debug("listing filtered TestExecutionResult instance");
		List<TestRunReportsList> testRunReportsList=null;
		try {
			
			StringBuffer qry= new StringBuffer(" from TestRunReportsList");
			
			boolean isANDrequired=false;
			if(testRunNo!=-1){
				qry.append(" e where");
				qry.append(" e.testRunNo=:testRunNo");
				isANDrequired=true;
			}
					
			if(productName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productName=:productName");
				isANDrequired=true;
			}
			if(productVersionListName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productVersionListName=:productVersionListName");
				isANDrequired=true;
			}
			
			
			
			
			Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());
			
			if(testRunNo!=-1){
				query.setParameter("testRunNo", testRunNo);
			}
			if(productName!=null){
				query.setParameter("productName", productName);
			}
			if(productVersionListName!=null){
				query.setParameter("productVersionListName", productVersionListName);
			}
					
			
			testRunReportsList = query.list();
			log.debug("list filtered successful");
		} catch (RuntimeException re) {
			log.error("list filtered failed", re);
		}
		return testRunReportsList;
	}
}