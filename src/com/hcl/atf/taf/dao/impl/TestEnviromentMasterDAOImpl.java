package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestEnvironmentMasterDAO;
import com.hcl.atf.taf.model.TestEnviromentMaster;


@Repository
public class TestEnviromentMasterDAOImpl implements TestEnvironmentMasterDAO {
	private static final Log log = LogFactory.getLog(TestEnviromentMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	

	@Override
	@Transactional
	public List<TestEnviromentMaster> list() {
		log.debug("listing all TestEnviromentMaster instance");
		List<TestEnviromentMaster> testEnvironmentMaster=null;
		try {
			testEnvironmentMaster=sessionFactory.getCurrentSession().createQuery("from TestEnviromentMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testEnvironmentMaster;
	}



	@Override
	@Transactional
	public List<TestEnviromentMaster> list(String devicePlatform) {
		log.debug("listing  specific TestEnviroments instance");
		List<TestEnviromentMaster> testEnviromentMaster=null;
		try {
			testEnviromentMaster=sessionFactory.getCurrentSession().createQuery("from TestEnviromentMaster where devicePlatformName=:devicePlatform")
										   .setParameter("devicePlatform", devicePlatform).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testEnviromentMaster;
	}


}
