package com.hcl.atf.taf.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceResultDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;

@Repository
public class TestRunReportsDeviceResultDAOImpl implements TestRunReportsDeviceResultDAO {
	private static final Log log = LogFactory.getLog(TestRunReportsDeviceResultDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	

	
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAll() {
		log.debug("listing all TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testRunReportsDeviceList;
	}
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceList> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceList;       
    }
	
	@Override
	@Transactional
    public List<TestRunReportsDeviceList> listAll(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName){
		log.debug("listing TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList")
					.setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunReportsDeviceList;       
    }
	@Override
	@Transactional
	public TestRunReportsDeviceList getByTestRunReportsDeviceListId(int testRunNo){
		log.debug("getting TestRunReportsDeviceList instance by id");
		TestRunReportsDeviceList testRunReportsDeviceList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList t where testRunNo=:testRunNo").setParameter("testRunNo", testRunNo)
					.list();
			testRunReportsDeviceList=(list!=null && list.size()!=0)?(TestRunReportsDeviceList)list.get(0):null;
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunReportsDeviceList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunReportsDeviceList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_reports_device_list_view").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(int testRunNo) {
		log.debug("listing specific TestRunReportsDeviceList");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo")
														.setParameter("testRunNo", testRunNo).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsDeviceList;
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(int testRunNo, int startIndex, int pageSize) {
		log.debug("listing specific TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo")
														.setParameter("testRunNo", testRunNo).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsDeviceList;
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(int testRunNo,Integer testRunConfigurationChildId){
		log.debug("listing specific TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList = new LinkedList<TestRunReportsDeviceList>();
		try {
			List<Object[]> testRunReportsDeviceListObj =sessionFactory.getCurrentSession().createSQLQuery("select distinct testrunlistid,testenvironmentname,hostname, hostplatform,hostipaddress,"
					+ "deviceid, deviceplatformname,deviceplatformversion,devicemodel "
					+ "from test_run_reports_device_list_view where testRunNo=:testRunNo")
					.setParameter("testRunNo", testRunNo)
					.list();			
			log.debug("Total Test Configuration records are  : " +testRunReportsDeviceListObj != null ? testRunReportsDeviceListObj.size() : 0 + " fetched successful");
			for(Object[] obj : testRunReportsDeviceListObj) {
				TestRunReportsDeviceList testRunReportsDevice = new TestRunReportsDeviceList();
				testRunReportsDevice.setTestRunListId((Integer) obj[0]);				
				testRunReportsDevice.setTestEnvironmentName((String) obj[1]);				
				testRunReportsDevice.setHostName((String) obj[2]);
				testRunReportsDevice.setHostPlatform((String) obj[3]);
				testRunReportsDevice.setHostIpAddress((String) obj[4]);
				testRunReportsDevice.setDeviceId((String) obj[5]);
				testRunReportsDevice.setDevicePlatformName((String) obj[6]);
				testRunReportsDevice.setDevicePlatformVersion((String) obj[7]);
				testRunReportsDevice.setDeviceModel((String) obj[8]);
				testRunReportsDeviceList.add(testRunReportsDevice);
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsDeviceList;
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime){
		log.debug("listing specific TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo and productName=:productName and productVersionName=:productVersionName and testRunTriggeredTime=:testRunTriggeredTime")
														.setParameter("testRunNo", testRunNo)
														.setParameter("productName",productName)
														.setParameter("productVersionName",productVersionName)
														.setParameter("testRunTriggeredTime",testRunTriggeredTime)
														.list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsDeviceList;
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(Integer testRunNo,String productName,String productVersionName){
		log.debug("listing specific TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo and productName=:productName and productVersionName=:productVersionName")
														.setParameter("testRunNo", testRunNo)
														.setParameter("productName",productName)
														.setParameter("productVersionName",productVersionName)
														.list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testRunReportsDeviceList;
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(Integer testRunNo, String deviceId){
		log.debug("listing specific TestRunReportsDeviceList instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		
		try {
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo and deviceId=:deviceId")
														.setParameter("testRunListId", testRunNo)
														.setParameter("deviceId",deviceId) 
														.list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return testRunReportsDeviceList;
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> list(Integer testRunNo){
		log.debug("listing specific TestRunReportsDeviceList instance");
		
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
		
			testRunReportsDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunReportsDeviceList where testRunNo=:testRunNo")
														.setParameter("testRunNo", testRunNo)
														//.setParameter("testRunTriggeredTime",datetestRunTriggeredTime)
														.list();
			log.debug("list specific successful");
		}catch (RuntimeException re) {
			log.error("list specific failed", re);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		
		return testRunReportsDeviceList;
	}

	@Override
	@Transactional
	public int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo, String productName, String productVersionName) {
		log.debug("getting TestRunReportsDeviceList Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from test_run_reports_device_list_view");
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
			if(productVersionName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productVersionName=:productVersionName");
				isANDrequired=true;
			}
			
			
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(qry.toString());
			
			if(testRunNo!=-1){
				query.setParameter("testRunNo", testRunNo);
			}
			if(productName!=null){
				query.setParameter("productName", productName);
			}
			if(productVersionName!=null){
				query.setParameter("productVersionName", productVersionName);
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
	public int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo, Integer testRunConfigurationChildId) {
		log.debug("getting TestRunReportsDeviceList Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from test_run_reports_device_list_view");
			boolean isANDrequired=false;
			if(testRunNo!=-1){
				qry.append(" e where");
				qry.append(" e.testRunNo=:testRunNo");
				isANDrequired=true;
			}
					
			if(testRunConfigurationChildId!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunConfigurationChildId=:testRunConfigurationChildId");
				isANDrequired=true;
			}
			
			
			
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(qry.toString());
			
			if(testRunNo!=-1){
				query.setParameter("testRunNo", testRunNo);
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
	@Override
	@Transactional
	public int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo, String productName, String productVersionName,String testRunTriggeredTime) {
		log.debug("getting TestRunReportsDeviceList Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from test_run_reports_device_list_view");
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
			if(productVersionName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.productVersionName=:productVersionName");
				isANDrequired=true;
			}
			if(testRunTriggeredTime!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunTriggeredTime=:testRunTriggeredTime");
				isANDrequired=true;
			}
			
			
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(qry.toString());
			
			if(testRunNo!=-1){
				query.setParameter("testRunNo", testRunNo);
			}
			if(productName!=null){
				query.setParameter("productName", productName);
			}
			if(productVersionName!=null){
				query.setParameter("productVersionName", productVersionName);
			}
			if(testRunTriggeredTime!=null){
				query.setParameter("testRunTriggeredTime", testRunTriggeredTime);
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
	public List<TestRunReportsDeviceList> listFilteredTestRunReportsDeviceList(
			int testRunNo, String productName, String productVersionListName,String testRunTriggeredTime) {
		log.debug("listing filtered TestExecutionResult instance");
		List<TestRunReportsDeviceList> testRunReportsDeviceList=null;
		try {
			
			StringBuffer qry= new StringBuffer(" from testRunReportsList");
			
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
			if(testRunTriggeredTime!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunTriggeredTime=:testRunTriggeredTime");
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
			if(testRunTriggeredTime!=null){
				query.setParameter("testRunTriggeredTime", testRunTriggeredTime);
			}		
			testRunReportsDeviceList = query.list();
			log.debug("list filtered successful");
		} catch (RuntimeException re) {
			log.error("list filtered failed", re);
		}
		return testRunReportsDeviceList;
	}
}
