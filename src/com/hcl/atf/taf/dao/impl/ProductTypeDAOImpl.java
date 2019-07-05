package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductTypeDAO;
import com.hcl.atf.taf.model.ProductType;

@Repository
public class ProductTypeDAOImpl implements ProductTypeDAO {
	private static final Log log = LogFactory.getLog(ProductTypeDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	

	@Override
	@Transactional
	public List<ProductType> getProductList() {
		log.debug("listing all TestToolMaster instance");
		List<ProductType> producttype=null;
		try {
			producttype=sessionFactory.getCurrentSession().createQuery("from ProductType").list();
			for(ProductType pt : producttype){
				if(null != pt.getTypeName())
					Hibernate.initialize(pt.getTypeName());

				if(pt.getTestToolMaster() != null)
					Hibernate.initialize(pt.getTestToolMaster());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return producttype;
	}



	@Override
	@Transactional
	public ProductType getProductTypeById(Integer productId) {
		log.debug("Inside getProductDetailsById");
		ProductType productType = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductType p where productTypeId=:productId").setParameter("productId", productId).list();
			productType = (list != null && list.size() != 0) ? (ProductType)list.get(0) : null;
			log.debug("getProductDetailsById successful");
			
			if(productType.getTypeName() != null){
				Hibernate.initialize(productType.getTypeName());
			}
				
		} catch (RuntimeException re) {
			log.error("getProductDetailsById failed", re);
		}
		return productType;
	}
}
