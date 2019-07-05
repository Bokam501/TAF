package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.CompetencyDAO;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;

@Service
public class CompetencyDAOImpl implements CompetencyDAO {
	private static final Log log = LogFactory.getLog(CompetencyDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public CompetencyMember getMemberMappedStatusByUserId(Integer userId, Integer status, Integer referenceId, Date startDate, Date endDate) {
		log.info("get member mapped with competency or not by user id name");
		CompetencyMember competencyMember = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CompetencyMember.class, "competencyMember");
			criteria.createAlias("competencyMember.userId", "user");	
			criteria.add(Restrictions.eq("user.userId", userId));	
			criteria.add(Restrictions.eq("competencyMember.status", status));
			if(referenceId != null && referenceId != 0){
				criteria.add(Restrictions.ne("competencyMember.competencyMemberId", referenceId));
			}
			criteria.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("competencyMember.startDate", startDate ),Restrictions.le("competencyMember.startDate", endDate ))));
			criteria.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("competencyMember.endDate", endDate ), Restrictions.ge("competencyMember.endDate", startDate ))));
			List<CompetencyMember> competencyMembers = criteria.list();	
			
			competencyMember = (competencyMembers != null && competencyMembers.size() > 0) ? competencyMembers.get(0) : null;
			log.info("isUserMapped process succuess");
		} catch (RuntimeException re) {
			log.error("isUserMapped process failed", re);
		}
		return competencyMember;
	}
	
	@Override
	@Transactional
	public void addCompetencyMember(CompetencyMember competencyMember) {

		log.info("adding UserList instance");
		try {
			sessionFactory.getCurrentSession().save(competencyMember);
			
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	
	}

	@Override
	@Transactional
	public List<CompetencyMember> getCompetencyMembers(Integer dimensionId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all DimensionMaster instance");
		List<CompetencyMember> competencyMembers=null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CompetencyMember.class, "member");
			criteria.createAlias("member.dimensionId", "dimension");	
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));	
			if(status != 2){
				criteria.add(Restrictions.eq("member.status", status));
			}
			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			competencyMembers = criteria.list();
			
			for (CompetencyMember competencyMember : competencyMembers) {
				Hibernate.initialize(competencyMember.getDimensionId());
				Hibernate.initialize(competencyMember.getUserId());
				Hibernate.initialize(competencyMember.getMappedBy());
			}
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return competencyMembers;
	}

	@Override
	@Transactional
	public List<UserList> getMembersToAddWithCompetency() {
		log.info("listing all DimensionMaster instance");
		List<UserList> userList=null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "userList")
		    .add(Restrictions.eq("userList.status", 1));
			
			userList = criteria.list();
			if(userList!=null && !userList.isEmpty()){
				for (UserList user : userList) {
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
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return userList;
	}

	@Override
	@Transactional
	public void updateCompetencyMember(CompetencyMember competencyMember) {
		log.info("updating Competency member instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(competencyMember);
		} catch (RuntimeException re) {
			log.error("updating CompetencyMember failed", re);
		}
	}

	@Override
	@Transactional
	public List<ProductTeamResources> getCompetencyProductTeamResourcesList(Integer productId, Integer dimensionId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing Product Team resource");
		List<ProductTeamResources> productTeamResList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
			c.createAlias("productTeam.productMaster", "prodMaster");
			c.createAlias("productTeam.dimensionMaster", "dimension");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("dimension.dimensionId", dimensionId));
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
	public Integer getTotalRecordsForComptencyProductTeam(Integer productId, Integer dimensionId) {
		log.debug("listing Product Team resource");
		int totalRecordCount = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
			c.createAlias("productTeam.productMaster", "prodMaster");
			c.createAlias("productTeam.userList", "user");
			c.createAlias("productTeam.dimensionMaster", "dimension");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			c.add(Restrictions.eq("productTeam.status", 1));
			
			c.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) c.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public List<UserList> getMembersToMapWithCompetencyProduct(Integer dimensionId) {

		log.info("listing all DimensionMaster instance");
		List<UserList> userList=null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserList.class, "userList")
			.add(Subqueries.propertyIn("userList.userId", DetachedCriteria.forClass(CompetencyMember.class, "competencyMember")
			    .createAlias("competencyMember.dimensionId", "dimension")
			    .add(Restrictions.eq("dimension.dimensionId", dimensionId))
			    .setProjection(Property.forName("competencyMember.userId"))
			));
			
			userList = criteria.list();
			if(userList!=null && !userList.isEmpty()){
				for (UserList user : userList) {
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
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return userList;
	}

	@Override
	@Transactional
	public Integer getNumberOfAllocatedMembersForCompetency(Integer dimensionId, Integer status, Class<ProductTeamResources> className) {
		log.info("getting records count of allocated members For Competency");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "productTeamResorce");
			criteria.createAlias("productTeamResorce.dimensionMaster", "dimension");	
			criteria.createAlias("productTeamResorce.userList", "user");
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.countDistinct("user.userId"));
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
		} catch (RuntimeException re) {
			log.error("list getNumberOfAllocatedMembersForCompetency failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public Integer getNumberOfProductsWithMembersInCompetency(Integer dimensionId, Integer status, Class<ProductTeamResources> className) {
		log.info("Getting records count for products with members");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "productTeamResorce");
			criteria.createAlias("productTeamResorce.dimensionMaster", "dimension");	
			criteria.createAlias("productTeamResorce.productMaster", "product");	
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.countDistinct("product.productId"));
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
		} catch (RuntimeException re) {
			log.error("list getNumberOfProductsWithMembersInCompetency failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public List<CompetencyMember> getCompetencyMemberMappedByUserId(Integer userId, Integer status, Integer referenceId) {
		List<CompetencyMember> competencyMembers = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CompetencyMember.class, "competencyMember");
			c.createAlias("competencyMember.userId", "user");	
			c.add(Restrictions.eq("user.userId", userId));
			if(referenceId != null && referenceId != 0){
				c.add(Restrictions.ne("competencyMember.competencyMemberId", referenceId));
			}
			
			competencyMembers = c.list();
			
			for (CompetencyMember competencyMember : competencyMembers) {
				Hibernate.initialize(competencyMember.getDimensionId());
				Hibernate.initialize(competencyMember.getUserId());
				Hibernate.initialize(competencyMember.getMappedBy());
				Hibernate.initialize(competencyMember.getModifiedBy());		
			}
			
			log.info("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return competencyMembers;
	}
	
	@Override
	@Transactional
	public List<Integer> getCompetencyIdByProductId(Integer productId){
		
		log.info("getting dimensionId records");
		List<Integer> competencyList=new ArrayList<Integer>();
		try {
			 competencyList = sessionFactory.getCurrentSession().createSQLQuery("SELECT competencyId FROM competency_product WHERE productId=:productId")
					.setParameter("productId", productId).list();
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return competencyList; 
	}

	@Override
	@Transactional
	public Integer getTotalRecordsForCompetencyMemberPagination(Integer dimensionId, Integer status, Class className) {
		log.info("getting TotalRecords For CompetencyMember Pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "competencyMember");
			criteria.createAlias("competencyMember.dimensionId", "dimension");	
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
		} catch (RuntimeException re) {
			log.error("list TotalRecordsForCompetencyMemberPagination failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public int getTotalRecordsForDimensionProductPagination(Integer competencyId, Integer status, Class<DimensionProduct> className) {log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "dimensionProduct");
			criteria.createAlias("dimensionProduct.dimensionId", "dimension");	
			criteria.add(Restrictions.eq("dimension.dimensionId", competencyId));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;			
		} catch (RuntimeException re) {
			log.error("list getTotalRecordsForDimensionProductPagination failed", re);		
		}
		return totalRecordCount;
	}

}
