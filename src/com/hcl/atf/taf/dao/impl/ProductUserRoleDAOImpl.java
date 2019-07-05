package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.UserList;

@Repository
public class ProductUserRoleDAOImpl implements ProductUserRoleDAO {
	private static final Log log = LogFactory.getLog(ProductUserRoleDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public ProductUserRole getByproductUserRoleId(int productUserRoleId) {
		log.debug("getting ProductUserRole instance by id");
		ProductUserRole productUserRole=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductUserRole ur where ur.productUserRoleId=:puserRoleId").setParameter("puserRoleId", productUserRoleId)
					.list();
			productUserRole=(list!=null && list.size()!=0)?(ProductUserRole)list.get(0):null;
			if(productUserRole != null){
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getRole());
				Hibernate.initialize(productUserRole.getUser());
			}
			log.debug("getBy ProductUserRoleId successful");
		} catch (RuntimeException re) {
			log.error("getBy ProductUserRoleId failed", re);			
		}	
		return productUserRole;
	}

	@Override
	@Transactional
	public void update(ProductUserRole productUserRole) {
		log.debug("updating ProductUserRole instance");
		try {
			log.info("update is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(productUserRole);
			
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	public void add(ProductUserRole productUserRole) {
		log.debug("adding ProductUserRole instance");
		try {
			sessionFactory.getCurrentSession().save(productUserRole);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public ProductUserRole getProductUserRoleByUserId(int userId) {
		log.debug("getting getProductUserRoleByUserId");
		ProductUserRole productUserRole=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductUserRole ur where ur.user.userId=:userId").setParameter("userId", userId)
					.list();
			productUserRole=(list!=null && list.size()!=0)?(ProductUserRole)list.get(0):null;
			if(productUserRole != null){
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getRole());
				Hibernate.initialize(productUserRole.getUser());
			}
			log.debug("getBy getProductUserRoleByUserId successful");
		} catch (RuntimeException re) {
			log.error("getBy getProductUserRoleByUserId failed", re);			
		}	
		return productUserRole;
	}
	
	@Override
	@Transactional
	public ProductUserRole getProductUserRoleByUserIdAndProductId(int userId, int productId) {
		log.debug("getting getProductUserRoleByUserIdAndProductId");
		ProductUserRole productUserRole=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery(
					"from ProductUserRole ur "
					+ "where "
					+ "ur.user.userId=:userId and "
					+ "ur.product.productId=:productId")
					.setParameter("userId", userId)
					.setParameter("productId", productId)
					.list();
			productUserRole=(list!=null && list.size()!=0)?(ProductUserRole)list.get(0):null;
			if(productUserRole != null){
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getRole());
				Hibernate.initialize(productUserRole.getUser());
			}
			log.debug("getBy getProductUserRoleByUserId successful");
		} catch (RuntimeException re) {
			log.error("getBy getProductUserRoleByUserId failed", re);			
		}	
		return productUserRole;
	}
	
	@Override
	@Transactional
	public List<UserList> getProductResources(int productId) {
		log.info("inside getListOfResourceBlockedForWorkpackage() of ResourceAvailability DAO Impl");
		List<ProductUserRole> listOfProductUserRole = new ArrayList<ProductUserRole>();
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class, "prodUser");
			c.add(Restrictions.eq("prodUser.product.productId", productId));
			
			listOfProductUserRole = c.list();
			log.info("Result Set Size : " + listOfProductUserRole.size());
			for (ProductUserRole productUserRole : listOfProductUserRole) {
				Hibernate.initialize(productUserRole.getUser());
				listOfBlockedUsers.add(productUserRole.getUser());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfBlockedUsers;
	}

	@Override
	@Transactional
	public Integer countAllProductUserRole(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class,"productUser");
			if (startDate != null) {
				c.add(Restrictions.ge("productUser.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productUser.createdDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all productUserList", e);
			return -1;
		}
	}

	@Override
	public List<ProductUserRole> listAllPaginate(int startIndex,int pageSize, Date startDate, Date endDate) {
		log.debug("listing all ProductUserRole");
		List<ProductUserRole> productUserRoleList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class, "productUserRoleList");
			if (startDate != null) {
				c.add(Restrictions.ge("productUserRoleList.modifiedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productUserRoleList.modifiedDate", endDate));
			}
			c.addOrder(Order.asc("productUserRoleId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            productUserRoleList = c.list();		
			
			if (!(productUserRoleList == null || productUserRoleList.isEmpty())){
				for (ProductUserRole productUserRole : productUserRoleList) {
					Hibernate.initialize(productUserRole.getProduct());
					Hibernate.initialize(productUserRole.getRole());
					Hibernate.initialize(productUserRole.getUser());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productUserRoleList;
	}

}
