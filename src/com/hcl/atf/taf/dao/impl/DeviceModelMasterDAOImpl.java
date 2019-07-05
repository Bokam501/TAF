package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceModelMasterDAO;
import com.hcl.atf.taf.model.DeviceModelMaster;


@Repository
public class DeviceModelMasterDAOImpl implements DeviceModelMasterDAO {
	private static final Log log = LogFactory.getLog(DeviceModelMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<DeviceModelMaster> list() {
		log.debug("listing all DeviceModels instance");
		List<DeviceModelMaster> deviceModels=null;
		try {
			deviceModels=sessionFactory.getCurrentSession().createQuery("from DeviceModelMaster").list();
			if (deviceModels == null || deviceModels.size() <= 0) {
				log.trace("No device Models in database");
				return deviceModels;
			} else {
				for (DeviceModelMaster deviceModel : deviceModels) {
					Hibernate.initialize(deviceModel.getDeviceMakeMaster());
				}
			}
				
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return deviceModels;
	}



	@Override
	@Transactional
	public List<DeviceModelMaster> list(String deviceMake) {
		log.debug("listing  specific DeviceModels instance");
		List<DeviceModelMaster> deviceModels=null;
		try {
			deviceModels=sessionFactory.getCurrentSession().createQuery("from DeviceModelMaster where deviceMake=:deviceMake")
										   .setParameter("deviceMake", deviceMake).list();
			if (deviceModels == null || deviceModels.size() <= 0) {
				log.trace("No device Models of specified make in database : " + deviceMake);
				return deviceModels;
			} else {
				for (DeviceModelMaster deviceModel : deviceModels) {
					Hibernate.initialize(deviceModel.getDeviceMakeMaster());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return deviceModels;
	}

	
	@Override
	@Transactional
	public DeviceModelMaster getDeviceModelByName(String model) {
		log.debug("getDeviceModelByName");
		List<DeviceModelMaster> deviceModels = null;
		DeviceModelMaster deviceModelMaster=null;
		try {
			deviceModels=sessionFactory.getCurrentSession().createQuery("from DeviceModelMaster d where deviceModel=:deviceModel").setParameter("deviceModel", model).list();
			if (deviceModels == null || deviceModels.size() <= 0) {
				log.debug("Device Model is not available. getDeviceModelByName failed");
				return deviceModelMaster;
			} else {
				deviceModelMaster = deviceModels.get(0);
				Hibernate.initialize(deviceModelMaster.getDeviceMakeMaster());
			}
			log.debug("getDeviceModelByName successful");
		} catch (RuntimeException re) {
			log.error("getDeviceModelByName failed", re);
		}
		return deviceModelMaster;
	}
	@Override
	@Transactional
	public void add(DeviceModelMaster deviceModelMaster) {
		log.debug("adding DeviceModelMaster instance");
		try {
			sessionFactory.getCurrentSession().save(deviceModelMaster);
			log.debug("add DeviceModelMaster successful");
		} catch (RuntimeException re) {
			log.error("add DeviceModelMaster failed", re);
		}
				
	}

}
