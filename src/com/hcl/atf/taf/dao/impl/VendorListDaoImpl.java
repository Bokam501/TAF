package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.VendorListDao;
import com.hcl.atf.taf.model.VendorMaster;
@Repository
public class VendorListDaoImpl implements VendorListDao {
	
	private static final Log log = LogFactory.getLog(ProductLocaleDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<VendorMaster> getVendorMasterList() {
		log.debug("listing all vendors");
		List<VendorMaster> vendorList=null;
		List<VendorMaster> vendors = new ArrayList<VendorMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(VendorMaster.class, "vendor");
			c.add(Restrictions.eq("vendor.status", 1));
			vendorList = c.list();
			if( vendorList != null && vendorList.size()>0){
				for (VendorMaster vendorMaster : vendorList) {
					if(vendorMaster.getVendorId() != -10){
						vendors.add(vendorMaster);
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return vendors;
	}

	

	@Override
	@Transactional
	public void addVendorMaster(VendorMaster vendor) {
		log.debug("adding VendorMaster instance");
		try {
			vendor.setStatus(1);
			sessionFactory.getCurrentSession().save(vendor);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
	}



	@Override
	@Transactional
	public VendorMaster getVendorById(Integer vendorId) {
		VendorMaster vendor=null;
		String hql = "from VendorMaster vl where vl.vendorId = :vendorId";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("vendorId", vendorId).list();
		vendor=(instances!=null && instances.size()!=0)?(VendorMaster)instances.get(0):null;
		
		return vendor;
	}



	@Override
	public void updateVendorMaster(VendorMaster vendorFromUI) {
		log.debug("updating Locale instance");
	
		try {
			sessionFactory.getCurrentSession().update(vendorFromUI);
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
		
	}



	@Override
	public List<VendorMaster> getVendorListByResourcePoolId(
			Integer resourcePoolId) {
		return null;
	}
	
	@Override
	@Transactional
	public List<VendorMaster> getVendorMasterList(Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all vendors");
		List<VendorMaster> vendorList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(VendorMaster.class, "vendor");
			c.add(Restrictions.ne("vendor.vendorId", -10));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			vendorList = c.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return vendorList;
	}

	@Override
	@Transactional
	public boolean isVendorExistingByregisteredCompanyName(
			String registeredCompanyName) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(VendorMaster.class, "vendor");
		c.add(Restrictions.eq("vendor.registeredCompanyName", registeredCompanyName));
		
		List<VendorMaster> vendors = c.list();		
		
		if (vendors == null || vendors.isEmpty()) 
		    return false;
		else 
			return true;
	}

		
	@Override
	@Transactional
	public VendorMaster getVendorByregisteredCompanyName(
			String registeredCompanyName) {
		log.debug("listing specific Vendor instance by Name");
		VendorMaster vendorMaster=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(VendorMaster.class, "vendor");
			c.add(Restrictions.eq("vendor.registeredCompanyName", registeredCompanyName));
			
			List<VendorMaster> vendorMasterList = c.list();	
			
			vendorMaster=(vendorMasterList!=null && vendorMasterList.size()!=0)?(VendorMaster)vendorMasterList.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return vendorMaster;
	}

	@Override
	@Transactional
	public boolean isVendorExistingByregisteredCompanyNameForUpdate(
			String registeredCompanyName, int vendorId) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(VendorMaster.class, "vendor");
		c.add(Restrictions.eq("vendor.registeredCompanyName", registeredCompanyName));
		
		if(vendorId !=-10){
			c.add(Restrictions.ne("vendor.vendorId", vendorId));			
		}		
		List<VendorMaster> vendors = c.list();		
		if (vendors == null || vendors.isEmpty()) 
		    return false;
		else 
			return true;
	}
}
