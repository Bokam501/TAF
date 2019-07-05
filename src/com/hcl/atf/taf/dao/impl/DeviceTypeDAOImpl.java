package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceTypeDAO;
import com.hcl.atf.taf.model.DeviceType;


@Repository
public class DeviceTypeDAOImpl implements DeviceTypeDAO {
	private static final Log log = LogFactory.getLog(DeviceTypeDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<DeviceType> list() {
		List<DeviceType> deviceTypeList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DeviceType.class, "devType");			
			deviceTypeList = c.list();			
			log.debug("list DeviceType successful");
		}catch (RuntimeException re) {
			log.error("list DeviceType failed", re);
		}
		return deviceTypeList;	
	}

	@Override
	@Transactional
	public void add(DeviceType deviceType) {
		log.debug("adding DeviceType instance");
		try {
			sessionFactory.getCurrentSession().save(deviceType);
			log.debug("DeviceType - add successful");
		} catch (RuntimeException re) {
			log.error("DeviceType - add failed", re);
		}				
	}

	@Override
	@Transactional
	public DeviceType getDeviceTypeByName(String deviceTypeName) {
		DeviceType deviceType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DeviceType.class, "devType");
			c.add(Restrictions.eq("devType.deviceTypeName", deviceTypeName));
			List list = c.list();		
			deviceType=(list!=null && list.size()!=0)?(DeviceType)list.get(0):null;
			
			log.debug("DeviceType by Name successful");
		} catch (RuntimeException re) {
			log.error("DeviceType by Name failed", re);
		}
		return deviceType;
	}

	@Override
	@Transactional
	public DeviceType getDeviceTypeByTypeId(Integer deviceTypeId) {
		DeviceType deviceType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DeviceType.class, "devType");
			c.add(Restrictions.eq("devType.deviceTypeId", deviceTypeId));
			List list = c.list();		
			deviceType=(list!=null && list.size()!=0)?(DeviceType)list.get(0):null;
			
			log.debug("DeviceType by TypeId successful");
		} catch (RuntimeException re) {
			log.error("DeviceType by TypeId failed", re);
		}
		return deviceType;
	}

}
