package com.hcl.atf.taf.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductTestEnvironmentDAO;
import com.hcl.atf.taf.model.Environment;

@Repository
public class ProductTestEnvironmentDAOImpl implements ProductTestEnvironmentDAO {
	private static final Log log = LogFactory.getLog(ProductTestEnvironmentDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<Environment> list() {
		log.debug("listing all Environments");
		List<Environment> environments=null;
		try {
			environments=sessionFactory.getCurrentSession().createQuery("from Environment where status='1'").list();
			log.debug("list all successful");
			if(environments!=null && !environments.isEmpty()){
				for(Environment environment:environments){
					Hibernate.initialize(environment.getEnvironmentCategory());
					Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
				}
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return environments;
	}

	@Override
	@Transactional
	public List<Environment> getEnvironmentListByProductId(Integer productId){
		log.debug("listing all Environments for product");		
		List<Environment> environments=null;

		try {			
			environments=sessionFactory.getCurrentSession().createQuery("from Environment e where e.productMaster.productId=:productMasterId and status=1 ")
			.setParameter("productMasterId", productId).list();		

			if(environments!=null && !environments.isEmpty()){
				for(Environment environment:environments){
					Hibernate.initialize(environment.getEnvironmentCategory());
					Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return environments;
	}

	
	@Override
	@Transactional
	public List<Environment> getEnvironmentListByProductIdAndStatus(Integer productId,Integer status){
		log.debug("listing all Environments for product");		
		List<Environment> environments=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Environment.class, "ec");
			c.createAlias("ec.productMaster", "pm");
			if(productId!=0){
			c.add(Restrictions.eq("pm.productId", productId));
			}
			if(status ==2){
				c.add(Restrictions.in("ec.status", Arrays.asList(0,1)));
			}else {
				c.add(Restrictions.eq("ec.status", status));
			}
			environments = c.list();
			
			if(environments!=null && !environments.isEmpty()){
				for(Environment environment:environments){
					Hibernate.initialize(environment.getEnvironmentCategory());
					Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return environments;
	}

	
	@Override
	@Transactional
	public Environment getByEnvironmentId(int environmentId) {
		log.debug("getting Environment instance by id");
		Environment environment=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from Environment p where environmentId=:environmentId")
					.setParameter("environmentId", environmentId)
					.list();
			environment=(list!=null && list.size()!=0)?(Environment)list.get(0):null;
			if(environment!=null){
				Hibernate.initialize(environment.getEnvironmentCategory());
				Hibernate.initialize(environment.getEnvironmentCombinationSet());
				Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
		
		}
			log.debug("getByProductFeatureId successful");
		} catch (RuntimeException re) {
			log.error("getByProductFeatureId failed", re);
		}
		return environment;
        
	}
	
	@Override
	@Transactional
	public boolean isProductEnvironmentExistingByName(Environment environment) {	
		String hql = "from Environment e where e.environmentName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", environment.getEnvironmentName().trim()).list();
		if (instances == null || instances.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public void add(Environment environment) {
		log.debug("adding Environment instance");
		Environment env=null;
		try {
			environment.setStatus(1);
			sessionFactory.getCurrentSession().save(environment);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}
	@Override
	@Transactional
	public void update(Environment environment){
		log.debug("updating Environment instance");
		try {
			log.info("Environment Id in Dao--->"+environment.getEnvironmentId());
			sessionFactory.getCurrentSession().update(environment);
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
		
	}
	
	public void delete(Environment environment){
		log.debug("deleting Environment instance");
		try {
			sessionFactory.getCurrentSession().delete(environment);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}
	
}
