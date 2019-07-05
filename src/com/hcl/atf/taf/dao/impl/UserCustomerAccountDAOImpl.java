package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserCustomerAccountDAO;
import com.hcl.atf.taf.model.UserCustomerAccount;

@Repository
public class UserCustomerAccountDAOImpl implements UserCustomerAccountDAO {
	private static final Log log = LogFactory.getLog(UserCustomerAccountDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<UserCustomerAccount> list(Integer userId) {
    log.info("Listing User Customer Account");
    	List<UserCustomerAccount> userCustomerAccountList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserCustomerAccount.class, "userCustomerAccount");
			c.createAlias("userCustomerAccount.userList", "userList");
			c.add(Restrictions.eq("userList.userId", userId));
			userCustomerAccountList = c.list();
			if(userCustomerAccountList.size()!=0){
			for (UserCustomerAccount userCustomerAccount : userCustomerAccountList) {
				Hibernate.initialize(userCustomerAccount.getCustomer());
				Hibernate.initialize(userCustomerAccount.getUserList());
			}
			}
		} catch (HibernateException e) {
			log.error("Error Listing User Customer Account ",e);
		}
		return userCustomerAccountList;
	}

	@Override
	@Transactional
	public void add(UserCustomerAccount userCustomerAccount) {
		log.debug("adding User Customer Account instance");
		try {
					
			sessionFactory.getCurrentSession().save(userCustomerAccount);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public UserCustomerAccount listUserCustomerAccountByUserCustomerAccountId(
			Integer userCustomerAccountId) {
        log.info("User Customer Account listing");
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserCustomerAccount.class, "userCustomerAccount");
	
		c.add(Restrictions.eq("userCustomerAccount.userCustomerAccountId", userCustomerAccountId));
	List<UserCustomerAccount>	 userCustomerAccountList =  c.list();
	UserCustomerAccount userCustomerAccount=userCustomerAccountList.get(0);
		if(userCustomerAccount!=null){
		
			Hibernate.initialize(userCustomerAccount.getCustomer());
			Hibernate.initialize(userCustomerAccount.getUserList());
		}
	
		return userCustomerAccount;
	}

	@Override
	@Transactional
	public void updateUserCustomerAccountInline(
			UserCustomerAccount userCustomerAccount) {
		log.debug("updating updateUserCustomerAccountInline instance");
		
		try {
			
			sessionFactory.getCurrentSession().update(userCustomerAccount);
			
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
		
	}

	@Override
	@Transactional
	public UserCustomerAccount getUserCustomerAccountByUserIdCustomerId(Integer userId, Integer customerId) {
		 log.info("User Customer Account listing");
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserCustomerAccount.class, "userCustomerAccount");
		c.createAlias("userCustomerAccount.userList", "userList");
		c.createAlias("userCustomerAccount.customer", "customer");
		c.add(Restrictions.eq("userList.userId", userId));
		c.add(Restrictions.eq("customer.customerId", customerId));
		List<UserCustomerAccount>	 userCustomerAccountList =  c.list();
		
		UserCustomerAccount userCustomerAccount = (userCustomerAccountList != null && userCustomerAccountList.size() != 0) ? (UserCustomerAccount) userCustomerAccountList.get(0) : null;
		
		if(userCustomerAccount!=null){
			Hibernate.initialize(userCustomerAccount.getCustomer());
			Hibernate.initialize(userCustomerAccount.getUserList());
		}
	
		return userCustomerAccount;
	}
}
