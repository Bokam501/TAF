package com.hcl.atf.taf.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductLocaleDao;
import com.hcl.atf.taf.model.ProductLocale;

@Repository
public class ProductLocaleDaoImpl implements ProductLocaleDao  {
private static final Log log = LogFactory.getLog(ProductLocaleDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ProductLocale> getProductLocaleListByProductId(Integer productId){
		log.debug("listing all Locale for product");		
		List<ProductLocale> localeList=null;

		try {			
			localeList=sessionFactory.getCurrentSession().createQuery("from ProductLocale e where (e.productMaster.productId=:productMasterId) and (status=1 )")
			.setParameter("productMasterId", productId).list();		

			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return localeList;
	}

	@Override
	public List<ProductLocale> list() {
		log.debug("listing all Locale");
		List<ProductLocale> localeList=null;
		try {
			
			localeList=sessionFactory.getCurrentSession().createQuery("from ProductLocale").list();
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return localeList;
	}

	@Override
	@Transactional
	public void addLocale(ProductLocale locale) {
		log.debug("adding Product Locale instance");
		try {
			locale.setStatus(1);
			
			locale.setCreatedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(locale);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateProductLocale(ProductLocale locale) {
		log.debug("updating Locale instance");
		try {
			
			log.info("Environment Id in Dao--->"+locale.getProductLocaleId());
		
			locale.setModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().update(locale);
			
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
	}

	@Override
	@Transactional
	public ProductLocale getLocaleByName(String localeName) {
		ProductLocale locale=null;
		String hql = "from ProductLocale pl where pl.localeName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", localeName.trim()).list();
		locale=(instances!=null && instances.size()!=0)?(ProductLocale)instances.get(0):null;
		return locale;
	}  
	
	@Override
	@Transactional
	public ProductLocale getProductLocaleById(int localeId) {
		ProductLocale locale=null;
		String hql = "from ProductLocale pl where pl.productLocaleId = :productLocaleId";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productLocaleId", localeId).list();
		locale=(instances!=null && instances.size()!=0)?(ProductLocale)instances.get(0):null;
		return locale;
	}  
	
	@Override
	@Transactional
	public ProductLocale getLocaleByNameByProduct(String localeName,String productId) {
		ProductLocale locale=null;
		String hql = "from ProductLocale pl where (pl.localeName = :name) and (pl.productMaster.productId = :productMasterId) and status=1";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", localeName.trim()).setParameter("productMasterId", Integer.parseInt(productId)).list();
		locale=(instances!=null && instances.size()!=0)?(ProductLocale)instances.get(0):null;
		return locale;
	}  
}
