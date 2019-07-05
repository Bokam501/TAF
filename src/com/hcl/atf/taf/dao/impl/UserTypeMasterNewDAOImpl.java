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

import com.hcl.atf.taf.dao.UserTypeMasterNewDAO;
import com.hcl.atf.taf.model.UserTypeMasterNew;

@Repository
public class UserTypeMasterNewDAOImpl implements UserTypeMasterNewDAO {
	private static final Log log = LogFactory.getLog(UserTypeMasterNewDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;


	@Override
	@Transactional
	public UserTypeMasterNew getByuserTypeId(int userTypeId) {
		log.debug("getting UserTypeMasterNew instance by id");
		UserTypeMasterNew userTypeMasterNew=null;
		try {
			
			List list = sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew utm where utm.userTypeId=:userId").setParameter("userId", userTypeId)
					.list();
			userTypeMasterNew=(list!=null && list.size()!=0)?(UserTypeMasterNew) list.get(0):null;
			log.debug("getByuserTypeId successful");
		} catch (RuntimeException re) {
			log.error("getByuserTypeId failed", re);
		}
		return userTypeMasterNew;
	}

	@Override
	@Transactional
	public UserTypeMasterNew isUserTypeMasterNewById(Integer userTypeId) {

		log.debug("Is getting UserTypeMasterNew instance by id");
		UserTypeMasterNew userTypeMasterNew=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew utm where userTypeId=:userId").setParameter("userId", userTypeId)
					.list();
			userTypeMasterNew=(list!=null && list.size()!=0)?(UserTypeMasterNew)list.get(0):null;
			log.debug("getBy userTypeId successful");
		} catch (RuntimeException re) {
			log.error("getBy userTypeId failed", re);
			
		}
		return userTypeMasterNew;
	}

	@Override
	@Transactional
	public List<UserTypeMasterNew> list() {
		log.debug("listing all UserTypeMasterNew instance");
		List<UserTypeMasterNew> UserTypeMasterNewList=null;
		try {
			UserTypeMasterNewList=sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew").list();
			log.debug("list all successful--------------------------");
			
			if (!(UserTypeMasterNewList == null || UserTypeMasterNewList.isEmpty())) {
				for (UserTypeMasterNew utm : UserTypeMasterNewList) {
					if(utm.getUserList()!=null)					
						Hibernate.initialize(utm.getUserList());					
				}
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}		
		return UserTypeMasterNewList;
	}

	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeMasterNewByuserTypeLabel(
			String userTypeLabel) {

		log.debug("getUserTypeMasterNewByuserTypeLabel");
		UserTypeMasterNew userTypeMasterNew = null;
		try{
			List list = sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew utm where utm.userTypeLabel=:userlabel")
					.setParameter("userlabel", userTypeLabel).list();
			userTypeMasterNew=(list!=null && list.size()!=0)?(UserTypeMasterNew) list.get(0):null;
			if(userTypeMasterNew != null){
				Hibernate.initialize(userTypeMasterNew.getUserList());				
			}
			log.debug("getUserTypeMasterNewByuserTypeLabel successful");
		}catch(IndexOutOfBoundsException ae){
			
		}catch(RuntimeException re){
			log.error("getUserTypeMasterNewByuserTypeLabel",re);
		}
		
		return userTypeMasterNew;
	}

	@Override
	@Transactional
	public List<UserTypeMasterNew> listProductUser(Integer typeFilter) {
		List<UserTypeMasterNew> userTypeMasterNew=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserTypeMasterNew.class, "userType");
			if (typeFilter == 1){
				c.add(Restrictions.in("userType.userTypeId", Arrays.asList(1, 2)));
			}else if(typeFilter == 2){
					c.add(Restrictions.in("userType.userTypeId", Arrays.asList(4)));
			}
			userTypeMasterNew = c.list();
			
			log.debug("getlistProductUser successful");
		} catch (RuntimeException re) {
			log.error("getlistProductUser failed", re);
		}
		return userTypeMasterNew;
	}

}
