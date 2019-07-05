package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceMakeMasterDAO;
import com.hcl.atf.taf.model.DeviceMakeMaster;


@Repository
public class DeviceMakeMasterDAOImpl implements DeviceMakeMasterDAO {
	private static final Log log = LogFactory.getLog(DeviceMakeMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<DeviceMakeMaster> list() {
		log.debug("listing all DeviceMakeMaster instance");
		List<DeviceMakeMaster> deviceMakeMaster=null;
		try {
			deviceMakeMaster=sessionFactory.getCurrentSession().createQuery("from DeviceMakeMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return deviceMakeMaster;
	}

	
	@Override
	@Transactional
	public void add(DeviceMakeMaster deviceMakeMaster) {
		log.debug("adding deviceMakeMaster instance : " + deviceMakeMaster.getDeviceMake());
		try {
			if (deviceMakeMaster.getDeviceMake() == null) {
				log.info("Not able to add Device Make : " + deviceMakeMaster.getDeviceMake());
				return;
			}
			deviceMakeMaster.setDeviceMake(deviceMakeMaster.getDeviceMake().toUpperCase());
			sessionFactory.getCurrentSession().save(deviceMakeMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
				
	}


	@Override
	public DeviceMakeMaster getDeviceMakeByName(String make) {
		log.debug("getting DeviceMake instance by name : " + make);
		List<DeviceMakeMaster> deviceManufacturers = null;
		DeviceMakeMaster deviceMakeMaster=null;
		try {
			deviceManufacturers= sessionFactory.getCurrentSession().createQuery("from DeviceMakeMaster d where deviceMake=:deviceMake").setParameter("deviceMake",make.toUpperCase()).list();
			if (!(deviceManufacturers == null || deviceManufacturers.size() == 0))
				deviceMakeMaster = deviceManufacturers.get(0);
			log.debug("getDeviceMakeByName successful");
		} catch (RuntimeException re) {
			log.error("getDeviceMakeByName failed", re);
		}
		return deviceMakeMaster;
	}

}
