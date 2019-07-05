package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.HostPlatformMasterDAO;
import com.hcl.atf.taf.model.HostPlatformMaster;

@Repository
public class HostPlatformMasterDAOImpl implements HostPlatformMasterDAO {
	private static final Log log = LogFactory.getLog(HostPlatformMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<HostPlatformMaster> list() {
		log.debug("listing all HostPlatformMaster instance");
		List<HostPlatformMaster> hostPlatformMaster=null;
		try {
			hostPlatformMaster=sessionFactory.getCurrentSession().createQuery("from HostPlatformMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return hostPlatformMaster;
	}

	
	

}
