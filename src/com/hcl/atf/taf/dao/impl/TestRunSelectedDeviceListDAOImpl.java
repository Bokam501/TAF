package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunSelectedDeviceListDAO;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;

@Repository
public class TestRunSelectedDeviceListDAOImpl implements TestRunSelectedDeviceListDAO {
	private static final Log log = LogFactory.getLog(TestRunSelectedDeviceListDAOImpl.class);
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public void add(TestRunSelectedDeviceList testRunSelectedDeviceList) {
		log.debug("adding TestRunSelectedDeviceList instance");
		try {
			sessionFactory.getCurrentSession().save(testRunSelectedDeviceList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}
	@Override
	@Transactional
	public void delete(TestRunSelectedDeviceList testRunSelectedDeviceList) {
		log.debug("deleting TestRunSelectedDeviceList instance");
		try {
			sessionFactory.getCurrentSession().delete(testRunSelectedDeviceList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	@Override
	@Transactional
	public TestRunSelectedDeviceList getBySelectedDeviceListId(
			int selectedDeviceListId) {
		log.debug("getting TestRunSelectedDeviceList instance by id");
		TestRunSelectedDeviceList testRunSelectedDeviceList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunSelectedDeviceList t where selectedDeviceListId=:selectedDeviceListId").setParameter("selectedDeviceListId",selectedDeviceListId)
					.list();
			testRunSelectedDeviceList=(list!=null && list.size()!=0)?(TestRunSelectedDeviceList) list.get(0):null;
			log.debug("getByTestRunSelectedDeviceListId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunSelectedDeviceListId failed", re);
		}
		return testRunSelectedDeviceList;
	}
	@Override
	@Transactional
	public int getTotalRecords(int testRunSelectedDeviceListId) {
		log.debug("getting TestRunSelectedDeviceList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_selected_device_list where testRunSelectedDeviceListId=:testRunSelectedDeviceListId").setParameter("testRunSelectedDeviceListId", testRunSelectedDeviceListId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}
	@Override
	@Transactional
	public List<TestRunSelectedDeviceList> list(int testRunConfigurationChildId) {
		log.debug("listing TestRunSelectedDeviceList instance");
		List<TestRunSelectedDeviceList> testRunSelectedDeviceList=null;
		try {
			testRunSelectedDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunSelectedDeviceList t where testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunConfigurationChildId",testRunConfigurationChildId)	               
	                .list();
			log.debug("list successful");
			if(!(testRunSelectedDeviceList == null || testRunSelectedDeviceList.isEmpty())){
				for (TestRunSelectedDeviceList tdl : testRunSelectedDeviceList) {
					Hibernate.initialize(tdl.getDeviceList().getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(tdl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(tdl.getDeviceList().getCommonActiveStatusMaster());
					Hibernate.initialize(tdl.getDeviceList().getHostList());
					Hibernate.initialize(tdl.getTestRunConfigurationChild());
					Hibernate.initialize(tdl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunSelectedDeviceList;
	}
	@Override
	@Transactional
	public ArrayList listDeviceList(int testRunConfigurationChildId) {
		log.debug("listing TestRunConfigurationChild instance");
		
		List<TestRunSelectedDeviceList> testRunSelectedDeviceList=null;
		try {
			testRunSelectedDeviceList=sessionFactory.getCurrentSession().createQuery("from TestRunSelectedDeviceList t where testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunConfigurationChildId",testRunConfigurationChildId)
										.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return (ArrayList) testRunSelectedDeviceList; 
	}
	@Override
	@Transactional
	public TestRunConfigurationChild listTestRunConfigurationChild(
			int testRunConfigurationChildId) {
		return null;
	}
}