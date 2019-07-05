/**
 * 
 */
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

import com.hcl.atf.taf.dao.OnboardUserRequestAccessDAO;
import com.hcl.atf.taf.model.OnboardUserRequestAccess;

/**
 * @author silambarasur
 * 
 */
@Repository
public class OnboardUserRequestAccessDAOImpl implements
		OnboardUserRequestAccessDAO {

	private static final Log log = LogFactory
			.getLog(OnboardUserRequestAccessDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void addOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(
					onboardUserRequestAccess);
		} catch (RuntimeException re) {
			log.error("Error in addOnboardUserRequestAccess", re);
		}
	}

	@Override
	@Transactional
	public void updateOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		try {
			sessionFactory.getCurrentSession().update(onboardUserRequestAccess);
		} catch (RuntimeException re) {
			log.error("Error in updateOnboardUserRequestAccess", re);
		}

	}

	@Override
	@Transactional
	public void deleteOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		try {
			sessionFactory.getCurrentSession().delete(onboardUserRequestAccess);
		} catch (RuntimeException re) {
			log.error("Error in updateOnboardUserRequestAccess", re);
		}
	}

	@Override
	@Transactional
	public List<OnboardUserRequestAccess> listOnboardUserRequestAccess() {
		try {

		} catch (RuntimeException re) {

		}
		return null;
	}

	@Override
	@Transactional
	public OnboardUserRequestAccess getByOnboardUserRequestAccessUserName(String onboardUserName) {
		OnboardUserRequestAccess onboardUserRequestAccess = null;
		List list = null;
		try {
			 list =  sessionFactory.getCurrentSession().createQuery("from OnboardUserRequestAccess where onboardUserName=:onboardUserName")
					.setParameter("onboardUserName", onboardUserName)
					.list();				
				onboardUserRequestAccess = (list!=null && list.size()!=0) ? (OnboardUserRequestAccess)list.get(0) : null;
				log.debug("list all OnboardUserRequestAccess get successful");
		} catch (RuntimeException re) {
			log.error("getting OnboardUserRequestAccess failed",re);
		}
		return onboardUserRequestAccess;
	}

	@Override
	@Transactional
	public List<OnboardUserRequestAccess> listOnboardUserRequestAccess(int status,
			int startIndex, int pageSize) {
			List<OnboardUserRequestAccess> listOnBoardUserRequestAccessList = new ArrayList<OnboardUserRequestAccess>();
			try{
				listOnBoardUserRequestAccessList =  (List<OnboardUserRequestAccess>) sessionFactory.getCurrentSession().createQuery("from OnboardUserRequestAccess where status=:status")
						.setParameter("status", status).list();
						
				
				log.debug("list all OnboardUserRequestAccess list successful");
				
				}catch(RuntimeException re){
					log.error("list all OnboardUserRequestAccess failed", re);
				}
			
			return listOnBoardUserRequestAccessList;
	}
	@Override
	@Transactional
	public OnboardUserRequestAccess getOnboardUserRequestAccessById(
			Integer onboardUserId) {
		OnboardUserRequestAccess onboardUserRequestAccess = null;		
		try{
		List list =  (List) sessionFactory.getCurrentSession().createQuery("from OnboardUserRequestAccess our where=:onboardUserId").setParameter("onboardUserId", onboardUserId);
		onboardUserRequestAccess = (list!=null && list.size()!=0) ? (OnboardUserRequestAccess)list.get(0) : null;
		log.debug("list all OnboardUserRequestAccess get successful");
		}catch(Exception ex){
			
			log.error("getting OnboardUserRequestAccess failed");
		}
	return 	onboardUserRequestAccess;
		
	}

	@Override
	@Transactional
	public OnboardUserRequestAccess getByOnboardUserRequestAccessByProductIdandUserName(
			Integer productId, String onboardUserName) {
		OnboardUserRequestAccess onboardUserRequestAccess = null;
		List<OnboardUserRequestAccess> onboardUserRequestAccessList = null;
		try {
				Criteria c= sessionFactory.getCurrentSession().createCriteria(OnboardUserRequestAccess.class,"onboardAccess");
				c.createAlias("onboardAccess.product", "product");
				c.add(Restrictions.eq("product.productId", productId));
				c.add(Restrictions.eq("onboardAccess.onboardUserName", onboardUserName));
				onboardUserRequestAccessList=c.list();
				onboardUserRequestAccess = (onboardUserRequestAccessList!=null && onboardUserRequestAccessList.size()!=0) ? (OnboardUserRequestAccess)onboardUserRequestAccessList.get(0) : null;
				log.debug("list all OnboardUserRequestAccess get successful");
		} catch (RuntimeException re) {
			log.error("getting OnboardUserRequestAccess failed",re);
		}
		return onboardUserRequestAccess;
	}

}