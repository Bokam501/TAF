package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DevicePlatformVersionListMasterDAO;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;


@Repository
public class DevicePlatformVersionListMasterDAOImpl implements DevicePlatformVersionListMasterDAO {
	private static final Log log = LogFactory.getLog(DevicePlatformVersionListMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<DevicePlatformVersionListMaster> list() {
		log.debug("listing all DeviceModels instance");
		List<DevicePlatformVersionListMaster> devicePlatformVersionListMaster=null;
		try {
			devicePlatformVersionListMaster=sessionFactory.getCurrentSession().createQuery("from DevicePlatformVersionListMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return devicePlatformVersionListMaster;
	}



	@Override
	@Transactional
	public List<DevicePlatformVersionListMaster> list(String devicePlatform) {
		log.debug("listing  specific DeviceModels instance");
		List<DevicePlatformVersionListMaster> devicePlatformVersionList=null;
		try {
			devicePlatformVersionList=sessionFactory.getCurrentSession().createQuery("from DevicePlatformVersionListMaster where devicePlatformName=:devicePlatform")
										   .setParameter("devicePlatform", devicePlatform).list();
			if (!(devicePlatformVersionList == null || devicePlatformVersionList.isEmpty())){
				for(DevicePlatformVersionListMaster dpl : devicePlatformVersionList){
					Hibernate.initialize(dpl.getDevicePlatformMaster());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return devicePlatformVersionList;
	}

	
	@Override
	@Transactional
	public void add(DevicePlatformVersionListMaster devicePlatformVersionListMaster) {
		log.debug("adding DevicePlatformVersionListMaster instance");
		try {
			sessionFactory.getCurrentSession().save(devicePlatformVersionListMaster);
			log.debug("add DevicePlatformVersionListMaster successful");
		} catch (RuntimeException re) {
			log.error("add DevicePlatformVersionListMaster failed", re);
			
		}
				
	}



	@Override
	@Transactional
	public DevicePlatformVersionListMaster getDevicePlatformVersionByVersionCode(String platform,String code) {
		log.debug("getDevicePlatformVersionByVersionCode");
		DevicePlatformVersionListMaster devicePlatformVersionListMaster=null;
		try {
			log.info("Device Platform name and Version: " + platform + " : " + code );
			
			devicePlatformVersionListMaster=(DevicePlatformVersionListMaster) sessionFactory.getCurrentSession().createQuery("from DevicePlatformVersionListMaster d where devicePlatformName=:platform AND devicePlatformVersion=:code")
			.setParameter("code", code).setParameter("platform",platform)
	                .list().get(0);
		
			log.debug("getDevicePlatformVersionByVersionCode successful");
		}catch(IndexOutOfBoundsException ie){
		}catch (RuntimeException re) {
		
			log.error("getDevicePlatformVersionByVersionCode failed", re);
		}
		return devicePlatformVersionListMaster;
	}



	@Override
	public DevicePlatformVersionListMaster getDevicePlatformVersionById(
			Integer devicePlatformVersionListId) {
		return null;
	}



	
	

}
