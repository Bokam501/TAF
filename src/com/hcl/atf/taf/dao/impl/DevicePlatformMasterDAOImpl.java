package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DevicePlatformMasterDAO;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;

@Repository
public class DevicePlatformMasterDAOImpl implements DevicePlatformMasterDAO {
	private static final Log log = LogFactory.getLog(DevicePlatformMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<DevicePlatformMaster> list() {
		log.debug("listing all DevicePlatformMaster instance");
		List<DevicePlatformMaster> devicePlatformMaster=null;
		try {
			devicePlatformMaster=sessionFactory.getCurrentSession().createQuery("from DevicePlatformMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return devicePlatformMaster;
	}

	
	public List<DevicePlatformMaster> list(int productId) {
		log.debug("listing all DevicePlatformMaster instance");		
		List<DevicePlatformMaster> devicePlatformMaster=new  ArrayList<DevicePlatformMaster>();		
		List<ProductVersionListMaster> productVersionListMaster=null;
		try {			
			if(productId!=-1){
					StringBuffer qry= new StringBuffer("from ProductVersionListMaster");
				
					qry.append(" a where ");
					qry.append(" a.productMaster.productId= :productId");				
									
					Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());				
				
					query.setParameter("productId", productId);
				
					int pageSize=500;								
					
					productVersionListMaster=query.setFirstResult(0).setMaxResults(pageSize).list();
			}else{				
				devicePlatformMaster=sessionFactory.getCurrentSession().createQuery("from DevicePlatformMaster").list();
				log.debug("list all successful");
			}
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return devicePlatformMaster;
	}	

}
