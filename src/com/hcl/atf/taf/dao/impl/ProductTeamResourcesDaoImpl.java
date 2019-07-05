package com.hcl.atf.taf.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonUserList;
@Repository
public class ProductTeamResourcesDaoImpl implements ProductTeamResourcesDao{
	private static final Log log = LogFactory.getLog(ProductTeamResourcesDaoImpl.class);
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ProductTeamResources> getProductTeamResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing Product Team resource");
		List<ProductTeamResources> productTeamResList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
			c.createAlias("productTeam.productMaster", "prodMaster");
			c.createAlias("productTeam.userList", "user");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("productTeam.status", 1));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			productTeamResList = c.list();
			
			if (!(productTeamResList == null || productTeamResList.isEmpty())) {
				for (ProductTeamResources productTeamResource : productTeamResList) {
					Hibernate.initialize(productTeamResource.getProductMaster());
					Hibernate.initialize(productTeamResource.getProductSpecificUserRole());
					Hibernate.initialize(productTeamResource.getUserList());
					Hibernate.initialize(productTeamResource.getUserList().getUserRoleMaster());
					Hibernate.initialize(productTeamResource.getDimensionMaster());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productTeamResList;
	}

	@Override
	@Transactional
	public void addProductTeamResource(ProductTeamResources pTeamResource) {
		log.debug("adding Product Team Resource");
		try {
			pTeamResource.setStatus(1);
			sessionFactory.getCurrentSession().save(pTeamResource);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public Boolean isUserAlreadyProductTeamResource(Integer productId, Integer userId,
			Date fromDate, Date toDate, ProductTeamResources productTeamResourcesFromDB) {
		log.debug("isUserAlreadyProductTeamResource");
		log.info("From Date -> "+fromDate+" and ToDate --> "+toDate);
		List<ProductTeamResources> productTeamResourceList =null;	
		try {			
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeamResource");	
			crit.createAlias("productTeamResource.productMaster", "productMaster");
			crit.createAlias("productTeamResource.userList", "userList");
			if(productId != 0){
				if(productTeamResourcesFromDB != null){
					crit.add(Restrictions.ne("productTeamResource.productTeamResourceId", productTeamResourcesFromDB.getProductTeamResourceId()));
				}
				crit.add(Restrictions.eq("userList.userId", userId));
				crit.add(Restrictions.eq("productTeamResource.status", 1));
				crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("productTeamResource.fromDate", fromDate ),Restrictions.le("productTeamResource.fromDate", toDate ), Restrictions.eq("productTeamResource.fromDate", fromDate),Restrictions.eq("productTeamResource.fromDate", toDate))));
				crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("productTeamResource.toDate", toDate ), Restrictions.ge("productTeamResource.toDate", fromDate ) ,Restrictions.eq("productTeamResource.toDate", fromDate),Restrictions.eq("productTeamResource.fromDate", toDate))));
			}
			productTeamResourceList = crit.list();
			log.info("Overlapping Product Team Resource Assignments -->"+productTeamResourceList.size());
			for (ProductTeamResources ptResources : productTeamResourceList) {
				log.info("Overlapping Product Team Resource Assignments --> "+ptResources.getProductMaster().getProductName()+" === " +ptResources.getFromDate() +"  -  " + ptResources.getToDate() );
			}
			if (productTeamResourceList == null || productTeamResourceList.isEmpty()) 
			    return false;
			else 
				return true;
		} catch(Exception e) {
			log.error("Problem finding if the resource is already a core resource", e);
			return true;
		}
	}

	@Override
	@Transactional
	public ProductTeamResources getProductTeamResourceById(
			Integer productTeamResourceId) {
		List<ProductTeamResources> productTeamResourceList = null;
		ProductTeamResources productTeamResourceobj = new ProductTeamResources();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "ptr");
			c.add(Restrictions.eq("ptr.productTeamResourceId", productTeamResourceId));
			c.add(Restrictions.eq("ptr.status", 1));
			productTeamResourceList=c.list();
			log.info("productTeamResourceList size-->"+productTeamResourceList.size());
			if (!(productTeamResourceList == null || productTeamResourceList.isEmpty())) {
				for (ProductTeamResources ptResource : productTeamResourceList) {
					Hibernate.initialize(ptResource.getProductMaster());
					Hibernate.initialize(ptResource.getUserList());
					Hibernate.initialize(ptResource.getProductSpecificUserRole());
					Hibernate.initialize(ptResource.getDimensionMaster());
				}
			}			
			productTeamResourceobj = (productTeamResourceList != null && productTeamResourceList.size() != 0) ? (ProductTeamResources) productTeamResourceList.get(0) : null;
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return productTeamResourceobj;
	}

	@Override
	@Transactional
	public void update(ProductTeamResources productTeamResourcesFromUI) {
		log.debug("updating Product Team Resources instance");
		try {
			sessionFactory.getCurrentSession().update(productTeamResourcesFromUI);
			
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		} 
		
	}

	@Override
	@Transactional
	public List<UserList> getProductTeamResourcesOfProduct(Integer productId,
			String plannedExecutionDate,int roleId) {
		List<UserList> productTeamUsers = new ArrayList<UserList>();
		List<ProductTeamResources> productTeamResourceList = null;
		ProductTeamResources productTeamResourceobj = new ProductTeamResources();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "ptr");
			c.createAlias("ptr.productMaster", "product");
			c.createAlias("ptr.userList", "ptUser");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("ptUser.status", 1));
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && plannedExecutionDate.contains("/")){
				c.add(Restrictions.le("ptr.fromDate",DateUtility.dateformatWithOutTime(plannedExecutionDate)));
				c.add(Restrictions.ge("ptr.toDate",DateUtility.dateformatWithOutTime(plannedExecutionDate)));
			}else{
				if(plannedExecutionDate!=null && !plannedExecutionDate.equals("")){
					c.add(Restrictions.le("ptr.fromDate",DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
					c.add(Restrictions.ge("ptr.toDate",DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
				}
			}
			if(roleId!=0 && roleId!=10){
				if(roleId==4){
					c.add(Restrictions.in("ptr.productSpecificUserRole.userRoleId",  Arrays.asList(4,3)));
				}else if(roleId==5){
					c.add(Restrictions.in("ptr.productSpecificUserRole.userRoleId",  Arrays.asList(5,4)));
				}
			}
			
			productTeamResourceList=c.list();
			log.info("productTeamResourceList size-->"+productTeamResourceList.size());
			if (!(productTeamResourceList == null || productTeamResourceList.isEmpty())) {
				for (ProductTeamResources ptResource : productTeamResourceList) {
					Hibernate.initialize(ptResource.getProductMaster());
					Hibernate.initialize(ptResource.getUserList());
					Hibernate.initialize(ptResource.getUserList().getResourcePool());
					Hibernate.initialize(ptResource.getProductSpecificUserRole());
					productTeamUsers.add(ptResource.getUserList());
					Hibernate.initialize(ptResource.getDimensionMaster());
				}
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return productTeamUsers;
	}

	@Override
	@Transactional
	public List<JsonUserList> getProductTeamResourcesJsonListOfProduct(Integer productId,
			String plannedExecutionDate,int roleId) {
		List<JsonUserList> productTeamJsonUsers = new ArrayList<JsonUserList>();
		List<ProductTeamResources> productTeamResourceList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "ptr");
			c.createAlias("ptr.productMaster", "product");
			c.createAlias("ptr.userList", "ptUser");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("ptUser.status", 1));
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && plannedExecutionDate.contains("/")){
				c.add(Restrictions.le("ptr.fromDate",DateUtility.dateformatWithOutTime(plannedExecutionDate)));
				c.add(Restrictions.ge("ptr.toDate",DateUtility.dateformatWithOutTime(plannedExecutionDate)));
			}else{
				if(plannedExecutionDate!=null && !plannedExecutionDate.equals("")){
					c.add(Restrictions.le("ptr.fromDate",DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
					c.add(Restrictions.ge("ptr.toDate",DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
				}
			}
			if(roleId!=0 && roleId!=10){
				if(roleId==4){
					c.add(Restrictions.in("ptr.productSpecificUserRole.userRoleId",  Arrays.asList(4,3)));
				}else if(roleId==5){
					c.add(Restrictions.in("ptr.productSpecificUserRole.userRoleId",  Arrays.asList(5,4)));
				}
			}			
			productTeamResourceList=c.list();
			log.info("productTeamResourceList size-->"+productTeamResourceList.size());
			if (!(productTeamResourceList == null || productTeamResourceList.isEmpty())) {
				for (ProductTeamResources ptResource : productTeamResourceList) {
				
					Hibernate.initialize(ptResource.getUserList());
					productTeamJsonUsers.add(new JsonUserList(ptResource.getUserList()));
					Hibernate.initialize(ptResource.getDimensionMaster());
				}
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productTeamJsonUsers;
	}
	@Override
	@Transactional
	public void delete(ProductTeamResources productTeamResource) {
		log.debug("remove Product Team Resources mapping instance");
		try {
			sessionFactory.getCurrentSession().delete(productTeamResource);
			
			log.debug("Delete successful");  
		} catch (RuntimeException re) {
			log.error("Delete failed", re);
		} 
	}

	@Override
	@Transactional
	public List<ProductTeamResources> getProductTeamResourcesByRole(
			int productId, Integer roleId) {
		log.debug("listing Product Team resource");
		List<ProductTeamResources> productTeamResList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
			c.createAlias("productTeam.productMaster", "prodMaster");
			c.createAlias("productTeam.userList", "user");
			c.createAlias("productTeam.productSpecificUserRole", "userRole");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("productTeam.status", 1));
			c.add(Restrictions.eq("userRole.userRoleId", roleId));
		
			
			productTeamResList = c.list();
			
			if (!(productTeamResList == null || productTeamResList.isEmpty())) {
				for (ProductTeamResources productTeamResource : productTeamResList) {
					Hibernate.initialize(productTeamResource.getProductMaster());
					Hibernate.initialize(productTeamResource.getProductSpecificUserRole());
					Hibernate.initialize(productTeamResource.getUserList());
					Hibernate.initialize(productTeamResource.getDimensionMaster());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productTeamResList;
	}
	
	@Override
	@Transactional
	public Map<Integer, Integer> getProductTeamResourcesCountByRole(
			int productId) {
		log.debug("listing Product Team resourcesCountByRole");		
		Map<Integer, Integer> productTeamResourceRoleCount = new HashMap<Integer, Integer>();
		List<Object[]> listFromDb = new ArrayList<Object[]>();
		String sql="";
		Query query = null;
		try {
			sql ="select count(ptr.userRoleId) as userroleidcount, ptr.userRoleId, ptr.productId from product_team_resources ptr"+
			" inner join user_list ul on ptr.userId=ul.userId where ptr.productId=:pid and "+
			"ul.status =1 and ptr.status = 1 GROUP BY ptr.userRoleId,ptr.productId";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("pid",productId );
			listFromDb = query.list();
			for(Object[] row:listFromDb){
				Integer rolecount = ((BigInteger)row[0]).intValue();
				Integer roleid = (Integer)row[1];
				productTeamResourceRoleCount.put(roleid, rolecount);
			}						
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productTeamResourceRoleCount;
	}



@Override
@Transactional
public UserList getProductTeamResourcesByUserName(
		int productId, String userName,Date plannedExecutionStartDate, Date plannedExecutionEndDate) {
	log.debug("listing Product Team resource");
	UserList userlist=null;
	List<ProductTeamResources> productTeamResList = null;
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
		c.createAlias("productTeam.productMaster", "prodMaster");
		c.createAlias("productTeam.userList", "user");
		c.add(Restrictions.eq("prodMaster.productId", productId));
		c.add(Restrictions.eq("user.loginId", userName));
		c.add(Restrictions.eq("productTeam.status", 1));
		
		if(plannedExecutionStartDate!=null && plannedExecutionEndDate!=null){
			c.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("productTeam.fromDate", plannedExecutionStartDate ),Restrictions.le("productTeam.fromDate", plannedExecutionEndDate ))));
			c.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("productTeam.toDate", plannedExecutionEndDate ), Restrictions.ge("productTeam.toDate", plannedExecutionStartDate ))));
		}
		productTeamResList = c.list();
		if (!(productTeamResList == null || productTeamResList.isEmpty())) {
			for (ProductTeamResources productTeamResource : productTeamResList) {
				Hibernate.initialize(productTeamResource.getUserList());
				userlist=productTeamResource.getUserList();
			}
		}
		
		log.debug("list successful");
	} catch (RuntimeException re) {
		log.error("list failed", re);
	}
	return userlist;
}

@Override
@Transactional
public Integer countAlladdAllProductTeamResources(Date startDate, Date endDate) {
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class,"productTeam");
		if (startDate != null) {
			c.add(Restrictions.ge("productTeam.createdDate", startDate));
		}
		if (endDate != null) {
			c.add(Restrictions.le("productTeam.createdDate", endDate));
		}
		
		c.setProjection(Projections.rowCount());
		Integer count = Integer.parseInt(c.uniqueResult().toString());
		return count;
	} catch (Exception e) {
		log.error("Unable to get count of all ProductTeamResources", e);
		return -1;
	}
}

@Override
public List<ProductTeamResources> listAllProductTeam(int startIndex,int pageSize, Date startDate, Date endDate) {
	log.debug("listing all ProductTeamResources");
	List<ProductTeamResources> productTeamResourceList=null;
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeamResources");
		if (startDate != null) {
			c.add(Restrictions.ge("productTeamResources.createdDate", startDate));
		}
		if (endDate != null) {
			c.add(Restrictions.le("productTeamResources.createdDate", endDate));
		}
		c.addOrder(Order.asc("productTeamResourceId"));
        c.setFirstResult(startIndex);
        c.setMaxResults(pageSize);
        productTeamResourceList = c.list();		
		
		if (!(productTeamResourceList == null || productTeamResourceList.isEmpty())){
			for (ProductTeamResources productResource : productTeamResourceList) {
				Hibernate.initialize(productResource.getDimensionMaster());
				Hibernate.initialize(productResource.getProductMaster());
				Hibernate.initialize(productResource.getUserList());
				Hibernate.initialize(productResource.getProductSpecificUserRole());
			}
		}
		log.debug("list all successful");
	} catch (RuntimeException re) {
		log.error("list all failed", re);
	}
	return productTeamResourceList;
}



@Override
@Transactional
public Boolean getTeamResourceByUserIdandProductIdandDate(Integer productId, Integer userId,Date fromDate, Date toDate) {
	log.debug("isUserAlreadyProductTeamResource");
	log.info("From Date -> "+fromDate+" and ToDate --> "+toDate);
	List<ProductTeamResources> productTeamResourceList =null;	
	try {			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeamResource");	
		crit.createAlias("productTeamResource.productMaster", "productMaster");
		crit.createAlias("productTeamResource.userList", "userList");
		if(productId != 0){
			
			crit.add(Restrictions.eq("userList.userId", userId));
			crit.add(Restrictions.eq("productTeamResource.status", 1));
			crit.add(Restrictions.eq("productTeamResource.productMaster.productId", productId));

			crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("productTeamResource.fromDate", fromDate ),Restrictions.le("productTeamResource.fromDate", toDate ), Restrictions.eq("productTeamResource.fromDate", fromDate),Restrictions.eq("productTeamResource.fromDate", toDate))));
			crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("productTeamResource.toDate", toDate ), Restrictions.ge("productTeamResource.toDate", fromDate ) ,Restrictions.eq("productTeamResource.toDate", fromDate),Restrictions.eq("productTeamResource.fromDate", toDate))));
		}
		productTeamResourceList = crit.list();
		log.info("Overlapping Product Team Resource Assignments -->"+productTeamResourceList.size());
		for (ProductTeamResources ptResources : productTeamResourceList) {
			log.info("Overlapping Product Team Resource Assignments --> "+ptResources.getProductMaster().getProductName()+" === " +ptResources.getFromDate() +"  -  " + ptResources.getToDate() );
		}
		if (productTeamResourceList == null || productTeamResourceList.isEmpty()) 
		    return false;
		else 
			return true;
	} catch(Exception e) {
		log.error("Problem finding if the resource is already a core resource", e);
		return true;
	}
}


@Override
@Transactional
public Boolean isExistsTeamResourceByUserIdandProductIdandUserId(Integer productId, Integer userId) {
	log.debug("isUserAlreadyProductTeamResource");
	List<ProductTeamResources> productTeamResourceList =null;	
	try {			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeamResource");	
		crit.createAlias("productTeamResource.productMaster", "productMaster");
		crit.createAlias("productTeamResource.userList", "userList");
		if(productId != 0){
			
			crit.add(Restrictions.eq("userList.userId", userId));
			crit.add(Restrictions.eq("productTeamResource.status", 1));
			crit.add(Restrictions.eq("productTeamResource.productMaster.productId", productId));

		}
		productTeamResourceList = crit.list();
		log.info("Overlapping Product Team Resource Assignments -->"+productTeamResourceList.size());
		for (ProductTeamResources ptResources : productTeamResourceList) {
			log.info("Overlapping Product Team Resource Assignments --> "+ptResources.getProductMaster().getProductName()+" === " +ptResources.getFromDate() +"  -  " + ptResources.getToDate() );
		}
		if (productTeamResourceList == null || productTeamResourceList.isEmpty()) 
		    return false;
		else 
			return true;
	} catch(Exception e) {
		log.error("Problem finding if the resource is already a core resource", e);
		return true;
	}
}

@Override
@Transactional
public List<ProductMaster> getProductDetailsByTeamResourceUserId(Integer userId) {
	log.debug("getProductDetailsByUserId");
	List<ProductTeamResources> productTeamResourceList =null;	
	List<ProductMaster> products= new ArrayList<ProductMaster>();
	try {			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeamResource");	
		crit.createAlias("productTeamResource.userList", "userList");
		if(userId != 0){
			crit.add(Restrictions.eq("userList.userId", userId));
			crit.add(Restrictions.eq("productTeamResource.status", 1));
		}
		productTeamResourceList = crit.list();
		log.info("Overlapping Product Team Resource Assignments -->"+productTeamResourceList.size());
		for (ProductTeamResources ptResources : productTeamResourceList) {
			products.add(ptResources.getProductMaster());
			log.info("Overlapping Product Team Resource Assignments --> "+ptResources.getProductMaster().getProductName()+" === " +ptResources.getFromDate() +"  -  " + ptResources.getToDate() );
		}
		
	} catch(Exception e) {
		log.error("Problem finding if the resource is already a core resource", e);
	}
	return products;
}
}