package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserGroupMapping;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.dto.ResourcePoolSummaryDTO;

@Repository
public class UserListDAOImpl implements UserListDAO {
	private static final Log log = LogFactory.getLog(UserListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void delete(UserList userList) {
		log.debug("deleting UserList instance");
		try {
			sessionFactory.getCurrentSession().delete(userList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public int add(UserList userList) {
		log.debug("adding UserList instance");
		try {
			sessionFactory.getCurrentSession().save(userList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return userList.getUserId().intValue();		
	}

	@Override
	@Transactional
	public void update(UserList userList) {
		log.debug("updating UserList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(userList);			
		} catch (RuntimeException re) {
			log.error("UserList update failed", re);
		}
	}
	
	@Override
	@Transactional
	public List<UserList> list() {
		log.debug("listing all UserList instance");
		List<UserList> userList=null;
		try {
			userList=sessionFactory.getCurrentSession().createQuery("from UserList u where status=1").list();
			if(userList!=null && !userList.isEmpty()){
				for (UserList tester : userList) {
					Hibernate.initialize(tester.getResourcePool());
					Hibernate.initialize(tester.getUserRoleMaster());
					Hibernate.initialize(tester.getUserTypeMasterNew());
					if(tester.getCommonActiveStatusMaster() != null && tester.getCommonActiveStatusMaster().getStatus() !=null) {
						Hibernate.initialize(tester.getCommonActiveStatusMaster());
					}
					Hibernate.initialize(tester.getVendor());
					Hibernate.initialize(tester.getUserSkills());
					Hibernate.initialize(tester.getAuthenticationType());
					Hibernate.initialize(tester.getAuthenticationMode());
					if(tester.getCustomer() != null){
						Hibernate.initialize(tester.getCustomer());
					}
					}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return userList;
	}
	
	@Override
	@Transactional
    public List<UserList> list(int startIndex, int pageSize) {
		log.debug("listing UserList instance");
		List<UserList> userList=null;
		try {
			userList=sessionFactory.getCurrentSession().createQuery("from UserList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			for(UserList ul : userList){
				Hibernate.initialize(ul.getUserTypeMasterNew());
				Hibernate.initialize(ul.getUserRoleMaster());
				Hibernate.initialize(ul.getResourcePool());
				Hibernate.initialize(ul.getVendor());
				Hibernate.initialize(ul.getCommonActiveStatusMaster());
				Hibernate.initialize(ul.getUserSkills());
				Hibernate.initialize(ul.getAuthenticationType());
				Hibernate.initialize(ul.getAuthenticationMode());
				if(ul.getCustomer() != null){
					Hibernate.initialize(ul.getCustomer());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userList;       
    }
	
	@Override
	@Transactional
	public UserList getByUserId(int userId){
		log.debug("getting UserList instance by id");
		UserList userList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from UserList u where userId=:userId").setParameter("userId", userId)
					.list();
			userList=(list!=null && list.size()!=0)?(UserList) list.get(0):null;		
			
			if(userList != null){
				Hibernate.initialize(userList.getUserTypeMasterNew());
				Hibernate.initialize(userList.getUserRoleMaster());
				Hibernate.initialize(userList.getResourcePool());
				Hibernate.initialize(userList.getVendor());
				Hibernate.initialize(userList.getCommonActiveStatusMaster());
				Hibernate.initialize(userList.getUserSkills());
				Hibernate.initialize(userList.getAuthenticationType());
				Hibernate.initialize(userList.getAuthenticationMode());
				if(userList.getCustomer() != null){
					Hibernate.initialize(userList.getCustomer());
				}
			}
			log.debug("getByUserId successful");
		} catch (RuntimeException re) {
			log.error("getByUserId failed", re);
		}
		return userList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting UserList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from user_list").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public UserList getByUserName(String userName) {
		log.debug("getByUserName");
		UserList userList=null;
		try {
			
			List list = sessionFactory.getCurrentSession().createQuery("from UserList u where loginId=:userName and status=1")
			.setParameter("userName", userName)
	                .list();
			userList=(list!=null && list.size()!=0)?(UserList) list.get(0):null;
			//
			if(userList != null){
				Hibernate.initialize(userList.getUserTypeMasterNew());
				Hibernate.initialize(userList.getUserRoleMaster());
				Hibernate.initialize(userList.getResourcePool());
				Hibernate.initialize(userList.getVendor());
				Hibernate.initialize(userList.getCommonActiveStatusMaster());
				Hibernate.initialize(userList.getResourcePool().getTestFactoryList());
				if(userList.getResourcePool().getTestFactoryLab()!=null){
				Hibernate.initialize(userList.getResourcePool().getTestFactoryLab().getTestFactoryLabName());
				}
				Hibernate.initialize(userList.getUserSkills());
				Hibernate.initialize(userList.getAuthenticationType());
				Hibernate.initialize(userList.getAuthenticationMode());
			}
			log.debug("getByUserName successful");
		}catch(IndexOutOfBoundsException ae){
		} catch (RuntimeException re) {
			log.error("getByUserName failed", re);
		}
		return userList;    
	}

	@Override
	@Transactional
	public boolean isUserExitsByName(UserList userList) {
		String hql = "from UserList ul where loginId = :userName";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("userName", userList.getLoginId().trim()).list();
		if (list != null  && !list.isEmpty()) 
		    return true;
		else 
			return false;
	}
	

	@Override
	@Transactional
	public UserList isUserListById(Integer userId) {
		log.debug("getting UserList instance by id");
		UserList userList=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from UserList ul where userId=:userId").setParameter("userId", userId)
					.list();		
			userList=(list!=null && list.size()!=0)?(UserList)list.get(0):null;
			if (!(userList == null)) {
					Hibernate.initialize(userList.getUserTypeMasterNew());
					Hibernate.initialize(userList.getUserRoleMaster());
					Hibernate.initialize(userList.getResourcePool());
					Hibernate.initialize(userList.getVendor());
					Hibernate.initialize(userList.getCommonActiveStatusMaster());
					Hibernate.initialize(userList.getUserSkills());
					Hibernate.initialize(userList.getLanguages());
					Hibernate.initialize(userList.getAuthenticationType());
					Hibernate.initialize(userList.getAuthenticationMode());
					if(userList.getCustomer() != null){
						Hibernate.initialize(userList.getCustomer());
						Set<Customer> cust = userList.getCustomer();
						for (Customer customer : cust) {
							if(customer.getCustomerUser() != null){
								Hibernate.initialize(customer.getCustomerUser());	
							}
						}
					}
					Set<ProductUserRole> productUserRoleSet=userList.getProductUserRoleSet();
					Hibernate.initialize(userList.getProductUserRoleSet());
					}
			
			log.debug("getBy UserId successful");
		} catch (RuntimeException re) {
			log.error("getBy UserId failed", re);			
		}
		return userList;
	}

	@Override
	@Transactional
	public UserRoleMaster getRoleById(int roleId) {
		log.debug("getting userRoleMaster instance by id");
		UserRoleMaster userRoleMaster=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from UserRoleMaster u where userRoleId=:userRoleId").setParameter("userRoleId", roleId)
					.list();
			userRoleMaster=(list!=null && list.size()!=0)?(UserRoleMaster)list.get(0):null;
			log.debug("getBy UserId successful");
		} catch (RuntimeException re) {
			log.error("getBy UserId failed", re);
			
		}
		return userRoleMaster;
	}
	

	@Override
	@Transactional
	public List<UserRoleMaster> listRole() {
				log.debug("getting userRoleMasterList instance");
				List<UserRoleMaster> userRoleMasterList=null;
				try {					
					userRoleMasterList =  sessionFactory.getCurrentSession().createQuery("from UserRoleMaster").list();
					log.debug("userRoleMasterList  successful");
				} catch (RuntimeException re) {
					log.error("userRoleMasterList  failed", re);					
				}
				return userRoleMasterList;
	}	
	
	@Override
	@Transactional
    public List<UserList> listUserByType(int userTypeId) {
		log.debug("listing UserList instance");
		List<UserList> userList=null;
		try {
			userList=  sessionFactory.getCurrentSession().createQuery("from UserList ul where userTypeId=:userTypeId").setParameter("userTypeId", userTypeId)
		                .list();
			if (!(userList == null || userList.isEmpty())) {
				for (UserList user : userList) {
					Hibernate.initialize(user.getUserTypeMasterNew());
					Hibernate.initialize(user.getUserRoleMaster());
					Hibernate.initialize(user.getResourcePool());
					Hibernate.initialize(user.getVendor());	
					Hibernate.initialize(user.getCommonActiveStatusMaster());
					Hibernate.initialize(user.getUserSkills());
					Hibernate.initialize(user.getAuthenticationType());
					Hibernate.initialize(user.getAuthenticationMode());
					if(user.getCustomer() != null){
						Hibernate.initialize(user.getCustomer());
					}
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userList;       
    }

	@Override
	@Transactional
	public List<UserList> listUserByTypeResourcePoolTestfactoryLabByStatus(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize) {
		List<UserList> userList = new ArrayList<UserList>();
		try{
			log.debug("userTypeId=="+userTypeId+", resourcePoolId="+resourcePoolId+", testFactoryLabId="+testFactoryLabId);			
			if(userTypeId != 0){				
				//all 3 available
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "userType");
				c.add(Restrictions.eq("userType.resourcePool.resourcePoolId", resourcePoolId));
				c.add(Restrictions.eq("userType.userTypeMasterNew.userTypeId", userTypeId));
				if(status != 2){
					c.add(Restrictions.eq("userType.status", status));
				}else if(status == 2){
					c.add(Restrictions.in("userType.status", Arrays.asList(0, 1, -1)));
				}
				if(jtStartIndex != null && jtPageSize != null)
					c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
				if( userList.size() ==0){
					userList = c.list();
				}else if(userList != null && userList.size()>0){
					userList = null;
					userList = c.list();
				}
				
			}else if(resourcePoolId != 0){
				log.debug("DAO Impl -- By ResourcePoolId"+resourcePoolId+", LabID "+testFactoryLabId);
				//RP and FL available
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class,"userRes");
				c.add(Restrictions.eq("userRes.resourcePool.resourcePoolId", resourcePoolId));
				if(status != 2){
					c.add(Restrictions.eq("userRes.status", status));
				}else if(status == 2){
					c.add(Restrictions.in("userRes.status", Arrays.asList(0, 1, -1)));
				}
				if(jtStartIndex != null && jtPageSize != null)
					c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
				if(userList.size() ==0){
					userList = c.list();
				}else{
					userList = null;
					userList = c.list();
				}				
			}else if(testFactoryLabId !=0){
				//Only TFLabID available
				Criteria c_rp = sessionFactory.getCurrentSession().createCriteria(UserList.class,"userList");
				c_rp.createAlias("userList.resourcePool","rp" );
				c_rp.createAlias("rp.testFactoryLab","tfl" );
				if(status != 2){
					c_rp.add(Restrictions.eq("userList.status", status));
				}else if(status == 2){
					c_rp.add(Restrictions.in("userList.status", Arrays.asList(0, 1, -1)));
				}
				c_rp.add(Restrictions.eq("tfl.testFactoryLabId",testFactoryLabId ));
				
				if(jtStartIndex != null && jtPageSize != null)
					c_rp.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
				
				if(userList == null || userList.size()==0){
					userList = c_rp.list();
					log.info("TestfactoryLab "+userList.size());
				}else if(userList != null || userList.size()>0){
					userList= null;
					userList = c_rp.list();
					log.info("Refresshed TestfactoryLab "+userList.size());
				}
			}			
			if (!(userList == null || userList.isEmpty())) {
				for (UserList user : userList) {
					Hibernate.initialize(user.getUserTypeMasterNew());
					Hibernate.initialize(user.getUserRoleMaster());
					Hibernate.initialize(user.getResourcePool());
					Hibernate.initialize(user.getVendor());	
					Hibernate.initialize(user.getCommonActiveStatusMaster());
					Hibernate.initialize(user.getUserSkills());
					Hibernate.initialize(user.getLanguages());
					Hibernate.initialize(user.getAuthenticationType());
					Hibernate.initialize(user.getAuthenticationMode());
					if(user.getCustomer() != null){
						Hibernate.initialize(user.getCustomer());	
						Set<Customer> userCust = user.getCustomer();
						for (Customer customer : userCust) {
							if(customer != null){
								Hibernate.initialize(customer);	
								if(customer.getCustomerUser() != null){
									Hibernate.initialize(customer.getCustomerUser());
									}								
							}
						}
					}
					
					
				}
			}
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}			
		return userList;
	}	
	
	@Override
	@Transactional
	public List<UserList> listUserByTypeResourcePoolMapped(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize) {
		List<UserList> userList = new ArrayList<UserList>();
		try{
			log.debug("userTypeId=="+userTypeId+", resourcePoolId="+resourcePoolId+", testFactoryLabId="+testFactoryLabId);	
			
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userTypeMasterNew", "userType");
			
			if(resourcePoolId > 0){
				c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(UserResourcePoolMapping.class, "urpm")
				  .add(Restrictions.eq("urpm.resourcepoolId.resourcePoolId", resourcePoolId))
				  .add(Restrictions.le("urpm.fromDate",new Date()))
				  .add(Restrictions.ge("urpm.toDate",new Date()))
				   .setProjection(Projections.property("urpm.userId.userId"))		
				)));
			}else if(resourcePoolId < 0){
				c.add(Restrictions.disjunction().add(Subqueries.propertyNotIn("user.userId", DetachedCriteria.forClass(UserResourcePoolMapping.class, "urpm")
						.createAlias("urpm.resourcepoolId", "resourcepool")
						.createAlias("resourcepool.testFactoryLab", "testFactoryLab")
						.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId))  
						.add(Restrictions.le("urpm.fromDate",new Date()))
						.add(Restrictions.ge("urpm.toDate",new Date()))
						.setProjection(Projections.property("urpm.userId.userId"))		
					)));
				c.add(Restrictions.ne("user.resourcePool.resourcePoolId",-10))
				.createAlias("user.resourcePool", "userResourcepool")
				.createAlias("userResourcepool.testFactoryLab", "userTestFactoryLab")
				.add(Restrictions.eq("userTestFactoryLab.testFactoryLabId", testFactoryLabId));  
			}else if(testFactoryLabId !=0){
				c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(UserResourcePoolMapping.class, "um")
					.createAlias("um.resourcepoolId", "resourcepool")
					.createAlias("resourcepool.testFactoryLab", "testFactoryLab")
					.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId))
					.add(Restrictions.le("um.fromDate",new Date()))
				    .add(Restrictions.ge("um.toDate",new Date()))
     			    .setProjection(Property.forName("um.userId.userId"))		
				)));
				
			}
			
			if(userTypeId!= 0){
				c.add(Restrictions.eq("userType.userTypeId",userTypeId));
			}
			if(status != 2){
				c.add(Restrictions.eq("user.status", status));
			}else if(status == 2){
				c.add(Restrictions.in("user.status", Arrays.asList(0, 1, -1)));
			}
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			userList = c.list();
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}			
		return userList;
	}
	

	@Override
	@Transactional
	public List<UserList> listUserByRoleResourcePool(int roleId, int resourcePoolId) {
		
		if (roleId < 0) {
			return null;
		}
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.createAlias("user.userRoleMaster", "role");
		c.add(Restrictions.eq("role.userRoleId", roleId));
		
		if(resourcePoolId >= 0) {
			c.createAlias("user.resourcePool", "pool");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
		}
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		}
		return users;
	}
	
	@Override
	@Transactional
	public List<UserList> listTestFactoryManagerUserByResourcePool(
		List<TestfactoryResourcePool> resourcePoolList, int testFactoryId) {
		List<UserList> usersLoop = new ArrayList<UserList>();
		int roleId=2;//Test Factory Manager
		if (roleId < 0) {
			return null;
		}
		for(TestfactoryResourcePool rpool: resourcePoolList){
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "role");
			c.add(Restrictions.eq("role.userRoleId", roleId));
			c.createAlias("user.resourcePool", "pool");
			c.add(Restrictions.eq("pool.resourcePoolId", rpool.getResourcePoolId()));
			c.add(Restrictions.eq("user.status", 1));
			usersLoop.addAll(c.list());
			
			
		}
		log.info("Total usersLoop ---"+usersLoop.size());
		List<UserList> users = new ArrayList<UserList>();
		users.addAll(usersLoop);
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
			
		}
		return users;
	}
	
	@Override
	
	public List<ResourcePoolSummaryDTO> listUserByRoleResourcePoolSummary(
			int roleId, int resourcePoolId) {
		return null;
	}
	
	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeNewByLabel(String userTypeLabel) {
		log.debug("getting userRoleMaster instance by id");
		UserTypeMasterNew userType=null;
		try {
			log.info("");
			List list =  sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew u where userTypeLabel=:userTypeLabel").setParameter("userTypeLabel", userTypeLabel)
				.list();
			userType=(list!=null && list.size()!=0)?(UserTypeMasterNew)list.get(0):null;
			if(userType!=null){
				Hibernate.initialize(userType.getUserTypeId());
			}
			log.debug("getBy UserId successful");
		} catch (RuntimeException re) {
			log.error("getBy UserId failed", re);
		}
		return userType;
	}

	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeNewById(int userTypeId) {
		log.debug("getting UserTypeNewById instance by id");
		UserTypeMasterNew userType=null;
		try {
			log.info("");
			List list =  sessionFactory.getCurrentSession().createQuery("from UserTypeMasterNew u where u.userTypeId=:typeId").setParameter("typeId", userTypeId)
				.list();
			userType=(list!=null && list.size()!=0)?(UserTypeMasterNew)list.get(0):null;
			Hibernate.initialize(userType.getUserTypeId());
			log.debug("getBy UserTypeId successful");
		} catch (RuntimeException re) {
			log.error("getBy UserTypeId failed", re);
		}
		return userType;
	}
	
	
	@Override
	@Transactional
	public UserRoleMaster getRoleByLabel(String userRoleLabel) {
		log.debug("getting userRoleMaster instance by id");
		UserRoleMaster userRoleMaster=null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from UserRoleMaster u where roleLabel=:roleLabel").setParameter("roleLabel", userRoleLabel)
					.list();
			userRoleMaster=(list!=null && list.size()!=0)?(UserRoleMaster)list.get(0):null;
			log.debug("getBy UserId successful");
		} catch (RuntimeException re) {
			log.error("getBy UserId failed", re);			
		}
		return userRoleMaster;
	}

	@Override
	@Transactional
	public UserList getUserByLoginId(String loginId) {
		log.debug("getting UserList instance by LoginId");
		UserList userList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from UserList u where u.loginId=:loginId").setParameter("loginId", loginId).list();
			userList=(list!=null && list.size()!=0)?(UserList) list.get(0):null;		
			log.debug("getByUserId successful");
		} catch (RuntimeException re) {
			log.error("getByUserId failed", re);
		}
		return userList;
	}

	@Override
	@Transactional
	public List<UserList> getUserListByResourcePoolId(int resourcePoolId) {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		
			if(resourcePoolId != 0){
				c.createAlias("user.resourcePool", "pool");
				c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			}
			c.add(Restrictions.eq("user.status", 1));
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
			}
		return users;
	}	

	@Override
	@Transactional
	public List<UserList> getUserListByLabIdOrResourcePoolId(Integer labId, Integer resourcePoolId) {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.createAlias("user.resourcePool", "pool");
		
		if(resourcePoolId != null && resourcePoolId != 0){
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
		}
		if(labId != null && labId != 0){
			c.createAlias("pool.testFactoryLab", "testFactoryLab");
			c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", labId));
		}
		c.add(Restrictions.in("user.userRoleMaster.userRoleId",  Arrays.asList(3, 4, 5)));
		c.add(Restrictions.eq("user.status", 1));
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		}
		return users;
	}
	
	@Override
	@Transactional
	public List<UserList> getUserListByResourcePoolIdByRole(int resourcePoolId) {
		
		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(3);//TM
		roleIds.add(4);// Test Lead
		roleIds.add(5);// Tester
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		
			c.createAlias("user.resourcePool", "pool");
			c.createAlias("user.userRoleMaster", "role");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("role.userRoleId", roleIds));
			
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
			}
		return users;
	}	
	
	@Override
	@Transactional
	public List<UserList> getUserListBasedRoleId(int resourcePoolId) {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		
			c.createAlias("user.resourcePool", "pool");
			c.createAlias("user.userTypeMasterNew", "userType");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("user.userRoleMaster.userRoleId",  Arrays.asList(3, 4, 5)));
			c.add(Restrictions.eq("user.status",1));
			c.add(Restrictions.ne("userType.userTypeId",4));
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		}
		return users;
	}

	@Override
	@Transactional
	public List<UserList> getUserListByRoleFromResourcePoolId(int resourcePoolId) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.createAlias("user.resourcePool", "pool");
		c.createAlias("user.userRoleMaster", "userRole");
		c.add(Restrictions.in("pool.resourcePoolId", Arrays.asList(resourcePoolId,-10)));
		c.add(Restrictions.in("userRole.userRoleId",  Arrays.asList(3, 4, 5, 2)));
		c.add(Restrictions.eq("user.status",1));

	List<UserList> users = c.list();
	for (UserList user : users) {
		Hibernate.initialize(user.getResourcePool());
		Hibernate.initialize(user.getUserRoleMaster());
		Hibernate.initialize(user.getVendor());
		Hibernate.initialize(user.getUserTypeMasterNew());
		Hibernate.initialize(user.getUserSkills());
		Hibernate.initialize(user.getAuthenticationType());
		Hibernate.initialize(user.getAuthenticationMode());
		if(user.getCustomer() != null){
			Hibernate.initialize(user.getCustomer());
		}
	}
	return users;
	}
	
	@Override
	@Transactional
	public List<UserList> getUserListByRoleFromResourcePoolIdByRoleAndSkill(Integer resourcePoolId, Integer roleId, Integer skillId,Integer userTypeId,Date weekDate,Skill skill) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		
		c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(UserResourcePoolMapping.class, "urpm")
			  .add(Restrictions.eq("urpm.resourcepoolId.resourcePoolId", resourcePoolId))
			  .add(Restrictions.le("urpm.fromDate",weekDate))
			  .add(Restrictions.ge("urpm.toDate",weekDate))
			   .setProjection(Projections.property("urpm.userId.userId"))		
			)));
		if(skillId != null){
			if(skillId!=10){
				c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(UserSkills.class, "userSkills")
					  	.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("skill.skillId", skillId), 
					  			Restrictions.conjunction().add(Restrictions.isNotNull("skill.leftIndex")).add(Restrictions.isNotNull("skill.rightIndex")).add(Restrictions.ge("skill.leftIndex", skill.getLeftIndex())).add(Restrictions.le("skill.rightIndex", skill.getRightIndex()))
					  	)))
					    .setProjection(Projections.property("userSkills.user.userId"))		
					)));
			}
			
		}
		if(roleId != null){
			c.createAlias("user.userRoleMaster", "userRole");
			c.add(Restrictions.eq("userRole.userRoleId",  roleId));
		}
		if(userTypeId != null){
			c.createAlias("user.userTypeMasterNew", "userType");
			c.add(Restrictions.eq("userType.userTypeId",  userTypeId));
		}
		
		c.add(Restrictions.eq("user.status",1));

		List<UserList> users = c.list();
		
		
		return users;
	}
	
	
	
	@Override
	@Transactional
	public UserRoleMaster listUserRoleByUserId(int userId) {
		UserRoleMaster userRole = null;
		if (userId < 0) {
			return null;
		}
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.add(Restrictions.eq("user.userId", userId));
		
		List<UserList> users = c.list();
		UserList user = (users!=null && users.size()!=0)?(UserList) users.get(0):null;	
		if(user != null ){
			Hibernate.initialize(user.getUserRoleMaster());
			userRole = user.getUserRoleMaster();
		}
		return userRole;
	}
	@Override
	@Transactional
	public boolean isActiveUserExitsByLoginId(String userLoginId) {
		log.debug("getting UserList instance by LoginId");
		boolean isUserExitsByName = false;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.add(Restrictions.eq("user.loginId",userLoginId));
			c.add(Restrictions.in("user.status",Arrays.asList(0, 1)));
			List<UserList> users = c.list();
			
			if (users != null  && !users.isEmpty()) 
				{isUserExitsByName = true;}
			else 
				{isUserExitsByName = false;}
			log.debug("getByUserId successful");
		} catch (RuntimeException re) {
			log.error("getByUserId failed", re);
		}
		return isUserExitsByName;
		
	}

	@Override
	@Transactional
	public List<UserRoleMaster> getRolesByUser(Integer userid) {
		List<UserRoleMaster> userRoleMasters = new ArrayList<UserRoleMaster>();
		if (userid < 0) {
			return null;
		}
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoles.class, "userroles");
		c.add(Restrictions.eq("userroles.userList.userId", userid));
		
		List<UserRoles> userRoles = c.list();
		if(userRoles != null &&!userRoles.isEmpty()){
			for(UserRoles uRole:userRoles){
				Hibernate.initialize(uRole.getRole());
				userRoleMasters.add(uRole.getRole());
			}
		}
		return userRoleMasters;
	}
	
	@Override
	@Transactional
	public List<UserList> getCustomerUserList(int resourcePoolId, Integer customerId) {
//		log.info("inside  getCustomerUserList function");
		List<UserList> customerUserList = new ArrayList<UserList>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		
			c.createAlias("user.resourcePool", "pool");
			c.createAlias("user.userTypeMasterNew", "userType");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("user.userRoleMaster.userRoleId",  Arrays.asList(3, 4, 5)));
			c.add(Restrictions.eq("user.status",1));
		List<UserList> users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
				Set<Customer> custUserSet = user.getCustomer();
//				log.info("custUserSet size==>"+custUserSet.size());
				for (Customer customer : custUserSet) {
					if(customer.getCustomerId().equals(customerId)){
						customerUserList.add(user);
//						log.info("final customerUserList"+user);
					}
				}
			}
		}
		return customerUserList;
	}

	@Override
	@Transactional
	public List<AuthenticationType> listAuthenticationTypes() {
		List<AuthenticationType> listOfAuthenticationType = new ArrayList<AuthenticationType>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AuthenticationType.class, "at");
			List<AuthenticationType> authenticationTypes = c.list();
			if(authenticationTypes != null && !authenticationTypes.isEmpty()){
				for(AuthenticationType authType:authenticationTypes){
					listOfAuthenticationType.add(authType);
				}
			}
		} catch (Exception e) {
			log.error("ERROR listing AuthenticationType ",e);
		}
		return listOfAuthenticationType;
	}

	@Override
	@Transactional
	public AuthenticationType getAuthenticationTypeById(int authenticationTypeId) {
		AuthenticationType authenticationType = null;
		try {
			if(authenticationTypeId != 0){
				Criteria c = sessionFactory.getCurrentSession().createCriteria(AuthenticationType.class, "at");
				c.add(Restrictions.eq("at.authenticationTypeId", authenticationTypeId));
				List<AuthenticationType> authenticationTypes = c.list();
				if(authenticationTypes != null && !authenticationTypes.isEmpty()){
					authenticationType = authenticationTypes.get(0) != null ? authenticationTypes.get(0) : null;
				}
			}
		} catch (Exception e) {
			log.error("ERROR listing AuthenticationType ",e);
		}
		return authenticationType;
	}

	@Override
	@Transactional
	public List<AuthenticationMode> listAuthenticationModes() {
		List<AuthenticationMode> listOfAuthenticationMode = new ArrayList<AuthenticationMode>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AuthenticationMode.class, "authMode");
			List<AuthenticationMode> authenticationModes = c.list();
			if(authenticationModes != null && !authenticationModes.isEmpty()){
				for(AuthenticationMode authMode : authenticationModes){
					listOfAuthenticationMode.add(authMode);
				}
			}
		} catch (Exception e) {
			log.error("ERROR listing AuthenticationMode ",e);
		}
		return listOfAuthenticationMode;
	}

	@Override
	@Transactional
	public AuthenticationMode getAuthenticationModeById(int authenticationModeId) {
		AuthenticationMode authenticationMode = null;
		try {
			if(authenticationModeId != 0){
				Criteria c = sessionFactory.getCurrentSession().createCriteria(AuthenticationMode.class, "authMode");
				c.add(Restrictions.eq("authMode.authenticationModeId", authenticationModeId));
				List<AuthenticationMode> authenticationModes = c.list();
				if(authenticationModes != null && !authenticationModes.isEmpty()){
					authenticationMode = authenticationModes.get(0) != null ? authenticationModes.get(0) : null;
				}
			}
		} catch (Exception e) {
			log.error("ERROR fetching AuthenticationMode ",e);
		}
		return authenticationMode;
	}

	@Override
	@Transactional
	public UserCustomerAccount getCustomerUserByName(String customerUserName) {
		log.info("getCustomerUserByName Login Id: "+customerUserName);
		UserCustomerAccount customerUser = null;
		try {
			
			List list = sessionFactory.getCurrentSession().createQuery("from UserCustomerAccount uca where userCustomerName=:customerUserName")
			.setParameter("customerUserName", customerUserName).list();
			customerUser=(list!=null && list.size()!=0)?(UserCustomerAccount) list.get(0):null;
			if(customerUser != null){
				Hibernate.initialize(customerUser.getCustomer());
				Hibernate.initialize(customerUser.getUserList());
			}
			log.debug("getCustomerUserByName successful");
		}catch(IndexOutOfBoundsException ae){
		} catch (RuntimeException re) {
			log.error("getCustomerUserByName failed", re);
		}
		return customerUser;  
	}

	@Override
	@Transactional
	public boolean isCustomerUserExitsByLoginId(String userLoginId) {
		log.debug("Check if a Customer User already exists with the same name");
		boolean isCustomerUserExitsByName = false;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserCustomerAccount.class, "userCustAcc");
			c.add(Restrictions.eq("userCustAcc.userCustomerName",userLoginId));
			List<UserList> users = c.list();
			
			if (users != null  && !users.isEmpty()) 
				{isCustomerUserExitsByName = true;}
			else 
				{isCustomerUserExitsByName = false;}
			log.debug("isCustomerUserExitsByLoginId successful");
		} catch (RuntimeException re) {
			log.error("isCustomerUserExitsByLoginId failed", re);
		}
		return isCustomerUserExitsByName;
	}

	
	
	
	
	@Override
	@Transactional
	public List<UserList> getActivityUserListBasedRoleId(int resourcePoolId) {
		List<UserList> users=null;
		try	{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.createAlias("user.resourcePool", "pool");
			c.createAlias("user.userTypeMasterNew", "userType");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("user.userRoleMaster.userRoleId",  Arrays.asList(3, 4, 5)));
			c.add(Restrictions.eq("user.status",1));		
		users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		
		    } 
	      }
		catch (RuntimeException re) {
		log.error("isCustomerUserExitsByLoginId failed", re);
		}
		return users;
	}
	
	@Override
	@Transactional
	public List<UserList> getActivityUserListBasedRoleIdAndProductId(int resourcePoolId,int productId) {
		List<UserList> users=null;
		try {
		users  = sessionFactory.getCurrentSession().createQuery("from UserList ul where ul.userId IN (SELECT pur.userList.userId FROM ProductTeamResources pur WHERE ((pur.productSpecificUserRole.userRoleId='3' OR pur.productSpecificUserRole.userRoleId='4' OR pur.productSpecificUserRole.userRoleId='5') and pur.status = 1 and productId=:productId and ul.status ='1'))")
		
				.setParameter("productId", productId)
				
				.list();
		for (UserList user : users) {		
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		}
		}
		catch (RuntimeException re) {
			log.error("isCustomerUserExitsByLoginId failed", re);
			}
		return users;
	}
	
	
	public List<String> getProductManagerEmailList(Integer productId){
		List<String> productManagerList = sessionFactory.getCurrentSession().createSQLQuery("SELECT emailId FROM user_list WHERE userId IN (SELECT userId FROM product_user_roles WHERE roleId='3' and productId=:productId)")
				.setParameter("productId", productId).list();
		return productManagerList;
	}

	@Override
	@Transactional
	public AuthenticationType getAuthenticationTypeByName(String authenticationTypeName) {
		AuthenticationType authenticationType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AuthenticationType.class, "at");
			c.add(Restrictions.eq("at.authenticationTypeName", authenticationTypeName));
			List<AuthenticationType> authenticationTypes = c.list();
			if(authenticationTypes != null && !authenticationTypes.isEmpty()){
				authenticationType = authenticationTypes.get(0) != null ? authenticationTypes.get(0) : null;
			}
		} catch (Exception e) {
			log.error("ERROR fetching AuthenticationType  ",e);
		}
		return authenticationType;
	}

	@Override
	@Transactional
	public List<String> getUserLoginIds() {
		log.debug("listing all UserList instance");
		List<String> userList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "users");
			c.setProjection(Property.forName("users.loginId"));
			userList = c.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return userList;
	}

	@Override
	@Transactional
	public Integer countAllUserList(Date startDate, Date endDate) {

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class,"user");
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all User", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public List<UserList> listAllUsers(Integer startIndex, Integer pageSize,Date startDate, Date endDate) {
		log.debug("listing all Users");
		List<UserList> users=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.addOrder(Order.asc("userId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);

            users = c.list();		
			
			if (!(users == null || users.isEmpty())){
				for (UserList user : users) {
					Hibernate.initialize(user.getResourcePool());
					Hibernate.initialize(user.getUserRoleMaster());
					Hibernate.initialize(user.getUserTypeMasterNew());
					Hibernate.initialize(user.getCommonActiveStatusMaster());
					Hibernate.initialize(user.getVendor());
					Hibernate.initialize(user.getUserSkills());
					Hibernate.initialize(user.getAuthenticationType());
					Hibernate.initialize(user.getAuthenticationMode());
					if(user.getCustomer() != null){
						Hibernate.initialize(user.getCustomer());
					}
					}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return users;
	}

	@Override
	@Transactional
	public List<UserList> getAllUserListBasedRoleId(int resourcePoolId) {	
		List<UserList> users=null;
		try	{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
		c.createAlias("user.resourcePool", "pool");
			c.createAlias("user.userTypeMasterNew", "userType");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.eq("user.status",1));		
		users = c.list();
		for (UserList user : users) {
			Hibernate.initialize(user.getResourcePool());
			Hibernate.initialize(user.getUserRoleMaster());
			Hibernate.initialize(user.getVendor());
			Hibernate.initialize(user.getUserTypeMasterNew());
			Hibernate.initialize(user.getUserSkills());
			Hibernate.initialize(user.getAuthenticationType());
			Hibernate.initialize(user.getAuthenticationMode());
			if(user.getCustomer() != null){
				Hibernate.initialize(user.getCustomer());
			}
		
		    } 
	      }
		catch (RuntimeException re) {
		log.error("isCustomerUserExitsByLoginId failed", re);
		}
		return users;
		
		
		
	}

	@Override
	@Transactional
	public List<UserResourcePoolMapping> listUserResourcePoolMapping(int userId, int resourcePoolId, Integer jtStartIndex,Integer jtPageSize) {
		
		List<UserResourcePoolMapping>listMapping = new ArrayList<UserResourcePoolMapping>();
		try{
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserResourcePoolMapping.class, "resourcepoolMapping");
				c.add(Restrictions.eq("resourcepoolMapping.userId.userId", userId));
				listMapping = c.list();
			
		}catch(Exception ex){
			log.error("Unable to get list ", ex);
		}
		return listMapping;
	}

	@Override
	@Transactional
	public void addResourcepoolmapping(UserResourcePoolMapping userResourcePoolMapping) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(userResourcePoolMapping);
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
				
		
	}

	@Override
	@Transactional
	public void updateResourceMapping(UserResourcePoolMapping userResourcePoolMappingFromUI) {
		log.debug("updating UserResourcePoolMapping instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(userResourcePoolMappingFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}

	@Override
	@Transactional
	public UserResourcePoolMapping getUserResourcePoolMappingById(int resourcePoolMappingId) {
		log.debug("getting getUserResourcePoolMappingById instance by id");
		UserResourcePoolMapping userResourcePoolMapping=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from UserResourcePoolMapping urpm where resourcePoolMappingId=:resourcePoolMappingId").setParameter("resourcePoolMappingId", resourcePoolMappingId)
					.list();
			userResourcePoolMapping=(list!=null && list.size()!=0)?(UserResourcePoolMapping)list.get(0):null;
				
			log.debug("userResourcePoolMapping successful");
		} catch (RuntimeException re) {
			log.error("userResourcePoolMapping failed", re);
		}
		return userResourcePoolMapping;
        
	}

	@Override
	@Transactional
	public void deleteResourcepoolMapping(UserResourcePoolMapping resourcepoolMapping) {
		sessionFactory.getCurrentSession().delete(resourcepoolMapping);
		}

	@Override
	@Transactional
	public boolean getUserResourcePoolMappingByFilter(Integer userId,Integer rpId, Date fromDate, Date toDate,UserResourcePoolMapping umrp) {
		
		List<UserResourcePoolMapping>listMapping = new ArrayList<UserResourcePoolMapping>();
		boolean result = false;
		try{
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserResourcePoolMapping.class, "resourcepoolMapping");
				c.add(Restrictions.eq("resourcepoolMapping.userId.userId", userId));
				
				if(umrp != null){
					c.add(Restrictions.ne("resourcepoolMapping.resourcePoolMappingId", umrp.getResourcePoolMappingId()));
				}
				c.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("resourcepoolMapping.fromDate", fromDate ),Restrictions.le("resourcepoolMapping.fromDate", toDate ), Restrictions.eq("resourcepoolMapping.fromDate", fromDate),Restrictions.eq("resourcepoolMapping.fromDate", toDate))));
				c.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("resourcepoolMapping.toDate", toDate ), Restrictions.ge("resourcepoolMapping.toDate", fromDate ) ,Restrictions.eq("resourcepoolMapping.toDate", fromDate),Restrictions.eq("resourcepoolMapping.toDate", toDate))));

				listMapping = c.list();
				log.info("listMapping size " +listMapping.size());
				if(listMapping!=null && listMapping.size()!=0){
					result = true;
				}
			
		}catch(Exception ex){
			log.error("Unable to get list ", ex);
		}
		return result;
	}

	@Override
	@Transactional
	public List<UserList> getUserListMappedToResourcePool(Integer loopResourcePoolId, Date startDate, Date endDate) {
		List<UserList> users = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(UserResourcePoolMapping.class, "urpm")
				  .add(Restrictions.eq("urpm.resourcepoolId.resourcePoolId", loopResourcePoolId))
				  .add(Restrictions.ge("urpm.fromDate",startDate))
				  .add(Restrictions.lt("urpm.fromDate",endDate))
				   .setProjection(Projections.property("urpm.userId.userId"))		
				)));
			
			c.add(Restrictions.eq("user.status",1));
			users = c.list();
			
		}catch(Exception ex){
			log.error("Error in getUserListMappedToResourcePool - ", ex);
		}
		return users;
	}
	
	
	@Override
	@Transactional
	public List<UserResourcePoolMapping> getUserResourcePoolMappingByFilter(Integer userId,Integer rpId,Date fromDate, Date toDate) {
		
		List<UserResourcePoolMapping>listMapping = new ArrayList<UserResourcePoolMapping>();
		try{
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserResourcePoolMapping.class, "resourcepoolMapping");
				c.add(Restrictions.eq("resourcepoolMapping.userId.userId", userId));
				
				if(rpId != null){
					c.add(Restrictions.eq("resourcepoolMapping.resourcepoolId.resourcePoolId", rpId));
				}
				
				c.add(Restrictions.ge("resourcepoolMapping.fromDate",fromDate));
				c.add(Restrictions.lt("resourcepoolMapping.fromDate",toDate));
				listMapping = c.list();
				
			
		}catch(Exception ex){
			log.error("Unable to get list ", ex);
		}
		return listMapping;
	}

	@Override
	@Transactional
	public List<UserList> getUserListBasedRoleAndProductId(int productId,String roleName) {
		List<UserList> users=null;
		try {
		users  = sessionFactory.getCurrentSession().createQuery("from UserList ul where ul.userId IN (SELECT pur.userList.userId FROM ProductTeamResources pur WHERE ((pur.productSpecificUserRole.roleName='"+roleName+"') and pur.status = 1 and productId=:productId and ul.status ='1'))")
				.setParameter("productId", productId).list();
		}
		catch (RuntimeException re) {
			log.error("isCustomerUserExitsByLoginId failed", re);
			}
		return users;
	}
	
	@Override
	@Transactional
	public List<UserGroup> getUserGroups(Integer testFactoryId, Integer productId, Boolean isConsolidated) {
		List<UserGroup> userGroups = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserGroup.class, "userGroup");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsolidated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("userGroup.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("userGroup.product.productId")), 
									Restrictions.eq("userGroup.product.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("userGroup.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("userGroup.product.productId", productId));
				}
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("userGroup.testFactory.testFactoryId", testFactoryId));
				c.add(Restrictions.isNull("userGroup.product.productId"));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("userGroup.product.productId", productId));
			}else{
				c.add(Restrictions.isNull("userGroup.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("userGroup.product.productId"));
			}
			userGroups = c.list();
			
		}catch(Exception ex){
			log.error("Error occured in getUserGroups - ", ex);
		}
		return userGroups;
	}
	
	@Override
	@Transactional
	public Boolean isUserGroupAvailable(String userGroupName, Integer referenceUserGroupId, Integer testFactoryId, Integer productId) {
		Boolean isAlreadyExsist = false;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserGroup.class, "userGroup");		
			c.add(Restrictions.eq("userGroup.userGroupName", userGroupName));
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				c.add(Restrictions.disjunction().add(
						Restrictions.or(
								Restrictions.conjunction()
									.add(Restrictions.eq("userGroup.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("userGroup.product.productId")), 
								Restrictions.eq("userGroup.product.productId", productId)
				)));
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("userGroup.testFactory.testFactoryId", testFactoryId));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("userGroup.product.productId", productId));
			}else{
				c.add(Restrictions.isNull("userGroup.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("userGroup.product.productId"));
			}
			
			if(referenceUserGroupId != null){
				c.add(Restrictions.ne("am.activityMasterId", referenceUserGroupId));
			}
			List<UserGroup> userGroups = c.list();
			if(userGroups != null && userGroups.size() > 0){
				isAlreadyExsist = true;
			}
		}catch(Exception ex){
			log.error("Error in isUserGroupAvailable - ", ex);
		}
		return isAlreadyExsist;
	}

	@Override
	@Transactional
	public void addUserGroup(UserGroup userGroup) {
		try{
			sessionFactory.getCurrentSession().save(userGroup);
		}catch(Exception ex){
			log.error("Error occured in addUserGroup - ", ex);
		}
	}
	
	@Override
	@Transactional
	public void updateUserGroup(UserGroup userGroup) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(userGroup);
		}catch(Exception ex){
			log.error("Error occured in updateUserGroup - ", ex);
		}
	}

	@Override
	@Transactional
	public Integer getUsersAvailbleToMapWithGroupCount(Integer productId, Integer userGroupId) {
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user")
			.add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(ProductTeamResources.class, "team")
	    		.createAlias("team.userList", "teamUser")
	    		.createAlias("team.productMaster", "teamProduct")
				.add(Subqueries.propertyNotIn("teamUser.userId", DetachedCriteria.forClass(UserGroupMapping.class, "mapping")
    				.createAlias("mapping.product", "mappingProduct")
    				.createAlias("mapping.userGroup", "mappingUserGroup")
    				.createAlias("mapping.user", "mappingUser")
	    			.add(Restrictions.eq("mappingProduct.productId", productId))
	    			.add(Restrictions.eq("mappingUserGroup.userGroupId", userGroupId))
	    			.setProjection(Property.forName("mappingUser.userId"))
	    		))
	    		.add(Restrictions.eq("teamProduct.productId", productId))
	            .setProjection(Property.forName("teamUser.userId"))		
		    ));
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
		} catch (Exception ex) {
			log.error("Error in getUsersAvailbleToMapWithGroupCount - ", ex);		
		}
		return totalRecordCount;
	}
	
	@Override
	@Transactional
	public List<Object[]> getUsersAvailbleToMapWithGroup(Integer productId, Integer userGroupId) {
		List<Object[]> userAvailableToMapWithGroup = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user")
			.add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(ProductTeamResources.class, "team")
	    		.createAlias("team.userList", "teamUser")
	    		.createAlias("team.productMaster", "teamProduct")
				.add(Subqueries.propertyNotIn("teamUser.userId", DetachedCriteria.forClass(UserGroupMapping.class, "mapping")
    				.createAlias("mapping.product", "mappingProduct")
    				.createAlias("mapping.userGroup", "mappingUserGroup")
    				.createAlias("mapping.user", "mappingUser")
	    			.add(Restrictions.eq("mappingProduct.productId", productId))
	    			.add(Restrictions.eq("mappingUserGroup.userGroupId", userGroupId))
	    			.setProjection(Property.forName("mappingUser.userId"))
	    		))
	    		.add(Restrictions.eq("teamProduct.productId", productId))
	            .setProjection(Property.forName("teamUser.userId"))		
		    ));
			criteria.setProjection(Projections.projectionList()
		    	.add(Projections.property("user.userId"))
		    	.add(Projections.property("user.loginId"))
		    );
			userAvailableToMapWithGroup = criteria.list();
		}catch(Exception ex){
			log.error("Error occured in getUsersAvailbleToMapWithGroup - ", ex);
		}
		return userAvailableToMapWithGroup;
	}
	
	@Override
	@Transactional
	public List<Object[]> getUsersMappedWithGroup(Integer productId, Integer userGroupId) {
		List<Object[]> userMappedWithGroup = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user")
			.add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(ProductTeamResources.class, "team")
	    		.createAlias("team.userList", "teamUser")
	    		.createAlias("team.productMaster", "teamProduct")
				.add(Subqueries.propertyIn("teamUser.userId", DetachedCriteria.forClass(UserGroupMapping.class, "mapping")
    				.createAlias("mapping.product", "mappingProduct")
    				.createAlias("mapping.userGroup", "mappingUserGroup")
    				.createAlias("mapping.user", "mappingUser")
	    			.add(Restrictions.eq("mappingProduct.productId", productId))
	    			.add(Restrictions.eq("mappingUserGroup.userGroupId", userGroupId))
	    			.setProjection(Property.forName("mappingUser.userId"))
	    		))
	    		.add(Restrictions.eq("teamProduct.productId", productId))
	            .setProjection(Property.forName("teamUser.userId"))		
		    ));
			criteria.setProjection(Projections.projectionList()
		    	.add(Projections.property("user.userId"))
		    	.add(Projections.property("user.loginId"))
		    );
			userMappedWithGroup = criteria.list();
		}catch(Exception ex){
			log.error("Error occured in getUsersMappedWithGroup - ", ex);
		}
		return userMappedWithGroup;
	}

	@Override
	@Transactional
	public void mapOrUnmapUserWithUserGroup(UserGroupMapping userGroupMapping, String mapOrUnmap) {
		try {
			if(mapOrUnmap == null || mapOrUnmap.isEmpty() || mapOrUnmap.equalsIgnoreCase("map")){
				sessionFactory.getCurrentSession().save(userGroupMapping);
			}else{
				userGroupMapping = (UserGroupMapping) sessionFactory.getCurrentSession().createCriteria(UserGroupMapping.class, "mapping")
					.createAlias("mapping.product", "product")
					.createAlias("mapping.userGroup", "userGroup")
					.createAlias("mapping.user", "user")
                    .add(Restrictions.eq("product.productId", userGroupMapping.getProduct().getProductId()))
                    .add(Restrictions.eq("userGroup.userGroupId", userGroupMapping.getUserGroup().getUserGroupId()))
                    .add(Restrictions.eq("user.userId", userGroupMapping.getUser().getUserId()))
                    .uniqueResult();
				sessionFactory.getCurrentSession().delete(userGroupMapping);
			}			
		} catch (Exception ex) {
			log.error("Error occured in mapOrUnmapUserWithUserGroup - ", ex);
		}
	}

	@Override
	@Transactional
	public List<UserList> getListOfEngManagerByResourcePoolId(Integer resourcePoolId) {
		List<UserList>listofUsers = new ArrayList<UserList>();
		try{
			Integer roleId= 2;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "role");
			c.add(Restrictions.eq("role.userRoleId", roleId));
			c.createAlias("user.resourcePool", "pool");
			c.add(Restrictions.eq("pool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.eq("user.status", 1));
			listofUsers = c.list();
		
		}catch(Exception ex){
			log.error("Unable to get users ", ex);
			
		}
		return listofUsers;
	}
	
	
	@Override
	@Transactional
	public List<UserResourcePoolMapping> getUserResourcePoolMappingByUserIdAndResourcePoolId(Integer userId,Integer rpId,Date fromDate,Date toDate) {
		
		List<UserResourcePoolMapping>listMapping = new ArrayList<UserResourcePoolMapping>();
		try{
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserResourcePoolMapping.class, "resourcepoolMapping");
				c.add(Restrictions.eq("resourcepoolMapping.userId.userId", userId));
				c.add(Restrictions.ge("resourcepoolMapping.fromDate", fromDate));
				c.add(Restrictions.le("resourcepoolMapping.toDate", toDate));
				if(rpId != 0){
					c.add(Restrictions.eq("resourcepoolMapping.resourcepoolId.resourcePoolId", rpId));
				}
				
				
				listMapping = c.list();
				
			
		}catch(Exception ex){
			log.error("Unable to get list ", ex);
		}
		return listMapping;
	}

	@Override
	@Transactional
	public void mapOrUnmapEntityUserGroup(EntityUserGroup entityUserGroup,String mapOrUnmap) {
		try{
			if(mapOrUnmap == null || mapOrUnmap.isEmpty() || mapOrUnmap.equalsIgnoreCase("map")){
				sessionFactory.getCurrentSession().saveOrUpdate(entityUserGroup);
			}else{
				sessionFactory.getCurrentSession().delete(entityUserGroup);
			}
			
		}catch(Exception ex){
			log.error("Error occured in Map or Unmap entityUserGroup - ", ex);
		}
	}

	@Override
	@Transactional
	public EntityUserGroup getEntityUserGroupByEntityAndEntityInstanceId(Integer entityId, Integer instanceId, Integer userId) {
		EntityUserGroup entityUserGroup =null;
		try{
			List<EntityUserGroup>listEntityUserGroups = new ArrayList<EntityUserGroup>();
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityUserGroup.class, "entityUserGroup");
			c.createAlias("entityUserGroup.user", "user");
			c.createAlias("entityUserGroup.entityTypeId", "entityMaster");
			
			c.add(Restrictions.eq("user.userId", userId));
			c.add(Restrictions.eq("entityMaster.entitymasterid", entityId));
			c.add(Restrictions.eq("entityUserGroup.entityInstanceId", instanceId));
			listEntityUserGroups = c.list();
			entityUserGroup = listEntityUserGroups.get(0);
		}catch(Exception ex){
			log.error("Unable to fetch Record",ex);
			
		}
		return entityUserGroup;
	}

	
	@Override
	@Transactional
	public List<EntityUserGroup> getEntityUserGroupByEntityId(Integer entityId,Integer instanceId) {
		List<EntityUserGroup>listEntityUserGroups = new ArrayList<EntityUserGroup>();
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityUserGroup.class, "entityUserGroup");
			c.add(Restrictions.eq("entityUserGroup.entityTypeId.entitymasterid", entityId));
			c.add(Restrictions.eq("entityUserGroup.entityInstanceId", instanceId));
			listEntityUserGroups = c.list();
		}catch(Exception ex){
			log.error("Unable to fetch Record",ex);
			
		}
		return listEntityUserGroups;
	}

	@Override
	@Transactional
	public List<Object[]> getEntityUserGroupReadyForMapping(Integer productId,Integer entityTypeId, Integer entityInstanceId) {
		List<Object[]> userAvailableToMapWithGroup = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user")
			.add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(ProductUserRole.class, "productUserRole")
	    		.createAlias("productUserRole.user", "productUser")
	    		.createAlias("productUserRole.product", "product")
	    		.add(Restrictions.eq("productUserRole.status", 1))
				.add(Subqueries.propertyNotIn("productUser.userId", DetachedCriteria.forClass(EntityUserGroup.class, "mapping")
    				.createAlias("mapping.entityTypeId", "mappingEntityTypeId")
    				.createAlias("mapping.user", "mappingUser")
	    			.add(Restrictions.eq("mappingEntityTypeId.entitymasterid", entityTypeId))
	    			.add(Restrictions.eq("mapping.entityInstanceId", entityInstanceId))
	    			.setProjection(Property.forName("mappingUser.userId"))
	    		))
	    		.add(Restrictions.eq("product.productId", productId))
	            .setProjection(Property.forName("productUser.userId"))		
		    ));
			criteria.setProjection(Projections.projectionList()
		    	.add(Projections.property("user.userId"))
		    	.add(Projections.property("user.loginId"))
		    );
			userAvailableToMapWithGroup = criteria.list();
		}catch(Exception ex){
			log.error("Error occured in getEntityUserGroupReadyForMapping - ", ex);
		}
		return userAvailableToMapWithGroup;
	}

	@Override
	@Transactional
	public EntityUserGroup getEntityUserGroupByEntityTypeIdAndUserId(Integer entityTypeId, Integer entityInstanceId, Integer userId) {
		EntityUserGroup entityUserGroup =null;
		try{
			List<EntityUserGroup> listEntityUserGroups = new ArrayList<EntityUserGroup>();
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityUserGroup.class, "entityUserGroup");
			c.add(Restrictions.eq("entityUserGroup.entityTypeId.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("entityUserGroup.entityInstanceId", entityInstanceId));
			c.add(Restrictions.eq("entityUserGroup.user.userId", userId));
			listEntityUserGroups = c.list();
			entityUserGroup = listEntityUserGroups.get(0);
			
		}catch(Exception ex){
			log.error("Unable to get records",ex);
		}
		return entityUserGroup;
		
	}

	@Override
	@Transactional
	public void deleteEntityAndUserMappingByProductIdandUserId(Integer productId, Integer userId) {
		try{
			List<EntityUserGroup>entityUserGroups = new ArrayList<EntityUserGroup>();
			Criteria c =sessionFactory.getCurrentSession().createCriteria(EntityUserGroup.class, "entityUserGroup");
			c.createAlias("entityUserGroup.product", "product");
			c.createAlias("entityUserGroup.user", "user");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("user.userId", userId));
			entityUserGroups = c.list();
			
			for(EntityUserGroup entityUserGroup : entityUserGroups){
				sessionFactory.getCurrentSession().delete(entityUserGroup);
			}
			
		}catch(Exception ex){
			log.error("Unable to delete ",ex);
		}
		
	}

}
