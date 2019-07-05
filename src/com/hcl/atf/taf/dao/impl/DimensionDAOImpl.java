package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
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

import com.hcl.atf.taf.dao.DimensionDAO;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.DimensionType;

@Service
public class DimensionDAOImpl implements DimensionDAO {
	private static final Log log = LogFactory.getLog(DimensionDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	

	@Override
	@Transactional
	public void addDimension(DimensionMaster dimensionMaster) {
		log.info("adding DimensionMaster instance");
		try {
			sessionFactory.getCurrentSession().save(dimensionMaster);			
		} catch (RuntimeException re) {
			log.error("Adding Dimension failed", re);
		}
	
	}
	
	@Override
	@Transactional
	public List<DimensionMaster> getDimensionList(Integer status, Integer jtStartIndex, Integer jtPageSize, boolean allowRootDimension, Integer dimensionTypeId) {
		List<DimensionMaster> dimensionMasters=null;
		try {
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension");
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			if(!allowRootDimension){
				criteria.add(Restrictions.ne("dimension.id", 1));
			}
			
			if(dimensionTypeId != null && dimensionTypeId != 0){
				
				if(!allowRootDimension){
					criteria.add(Subqueries.propertyIn("dimension.id", DetachedCriteria.forClass(DimensionMaster.class, "dimensionMaster")
							.createAlias("dimensionMaster.type", "dimensionType")
							.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId))
							.setProjection(Property.forName("dimensionMaster.id"))
							));
				}else{
					criteria.add(Restrictions.disjunction().add(Restrictions.or(Subqueries.propertyIn("dimension.id", DetachedCriteria.forClass(DimensionMaster.class, "dimensionMaster")
							.createAlias("dimensionMaster.type", "dimensionType")
							.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId))
							.setProjection(Property.forName("dimensionMaster.id"))
							), Restrictions.eq("dimension.dimensionId", 1))));
				}
			}

			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			dimensionMasters = criteria.list();
			
			for (DimensionMaster dimensionMaster : dimensionMasters) {
				Hibernate.initialize(dimensionMaster.getParent());
				Hibernate.initialize(dimensionMaster.getType());
				Hibernate.initialize(dimensionMaster.getOwner());
				Hibernate.initialize(dimensionMaster.getModifiedBy());
			}
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return dimensionMasters;
	}
	
	@Override
	@Transactional
	public int getTotalRecordsForDimensionPagination(Integer status, Class<DimensionMaster> className, boolean allowRootDimension, Integer dimensionTypeId) {
		log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "dimension");
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			if(!allowRootDimension){
				criteria.add(Restrictions.ne("dimension.dimensionId", 1));
			}
			if(dimensionTypeId != null && dimensionTypeId != 0){
				
				if(!allowRootDimension){
					criteria.add(Subqueries.propertyIn("dimension.id", DetachedCriteria.forClass(DimensionMaster.class, "dimensionMaster")
							.createAlias("dimensionMaster.type", "dimensionType")
							.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId))
							.setProjection(Property.forName("dimensionMaster.id"))
							));
				}else{
					criteria.add(Restrictions.disjunction().add(Restrictions.or(Subqueries.propertyIn("dimension.id", DetachedCriteria.forClass(DimensionMaster.class, "dimensionMaster")
							.createAlias("dimensionMaster.type", "dimensionType")
							.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId))
							.setProjection(Property.forName("dimensionMaster.id"))
							), Restrictions.eq("dimension.dimensionId", 1))));
				}
			}

			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
		} catch (RuntimeException re) {
			log.error("list TotalRecords For DimensionPagination failed", re);		
		}
		return totalRecordCount;
	}
	
	@Override
	@Transactional
	public void updateDimension(DimensionMaster dimensionMaster) {
		log.info("updating UserList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(dimensionMaster);
		} catch (RuntimeException re) {
			log.error("update Dimension failed", re);
		}
	}

	@Override
	@Transactional
	public DimensionMaster getDimensionById(Integer dimensionId) {
		log.info("get dimension details by dimension id");
		DimensionMaster dimensionMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension");
			c.add(Restrictions.eq("dimension.dimensionId", dimensionId));
						
			List<DimensionMaster> dimensionMasters = c.list();	
			for (DimensionMaster dimensionMasterLoop : dimensionMasters) {
				Hibernate.initialize(dimensionMasterLoop.getParent());		
				Hibernate.initialize(dimensionMasterLoop.getOwner());
				Hibernate.initialize(dimensionMasterLoop.getModifiedBy());
			}
			dimensionMaster = (dimensionMasters!=null && dimensionMasters.size()!=0)?(DimensionMaster)dimensionMasters.get(0):null;
		} catch (RuntimeException re) {
			log.error("list Dimension By Id failed", re);
		}
		return dimensionMaster;
	}
	
	@Override
	@Transactional
	public DimensionMaster getDimensionByNameAndType(String dimensionName, Integer referenceId, Integer dimensionTypeId) {
		log.info("get dimension details by dimension name and type");
		DimensionMaster dimensionMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension");
			c.add(Restrictions.eq("dimension.name", dimensionName));
			if(referenceId != null && referenceId != 0){
				c.add(Restrictions.ne("dimension.dimensionId", referenceId));
			}
			if(dimensionTypeId != null && dimensionTypeId != 0){
				c.createAlias("dimension.type", "dimensionType");
				c.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId));
			}
			List<DimensionMaster> dimensionMasters = c.list();	
			for (DimensionMaster dimensionMasterLoop : dimensionMasters) {
				Hibernate.initialize(dimensionMasterLoop.getParent());			
			}
			dimensionMaster = (dimensionMasters!=null && dimensionMasters.size()!=0)?(DimensionMaster)dimensionMasters.get(0):null;
			
			log.info("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return dimensionMaster;
	}
	
	@Override
	@Transactional
	public List<Integer> getDimensionIdByProductId(Integer productId,Integer dimensionTypeId){
		List<Integer> dimensionIds = new ArrayList<Integer>();
		try {
			dimensionIds = sessionFactory.getCurrentSession().createSQLQuery("SELECT id FROM dimension_master where type=:type and id in (SELECT dimensionId FROM dimension_product WHERE productId=:productId)")
					.setParameter("type", dimensionTypeId).setParameter("productId", productId).list();
		} catch (RuntimeException re) {
			log.error("listing DimensionId By ProductId failed", re);		
		}
		return dimensionIds; 
	}

	@Override
	@Transactional
	public List<DimensionType> getDimensionTypeList() {
		log.info("listing all DimensionType instance");
		List<DimensionType> dimensionTypes = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionType.class);
			dimensionTypes = criteria.list();
		} catch (RuntimeException re) {
			log.error("listing DimensionType failed", re);		
		}
		return dimensionTypes;
	}

	@Override
	@Transactional
	public List<DimensionMaster> getDimensionListByUserId(Integer status, Integer userId, Integer dimensionTypeId) {
		log.info("listing all DimensionMaster instances for user Id --- "+userId);
		List<DimensionMaster> competencyMasters = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension")
			.add(Subqueries.propertyIn("dimensionId", DetachedCriteria.forClass(CompetencyMember.class, "competencyMember")
					.createAlias("competencyMember.userId", "user")
					.add(Restrictions.eq("user.userId", userId))
					.setProjection(Property.forName("competencyMember.dimensionId"))
		    ));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			if(dimensionTypeId != null && dimensionTypeId != 0){
				criteria.createAlias("dimension.type", "dimensionType");
				criteria.add(Restrictions.eq("dimensionType.dimensionTypeId", dimensionTypeId));
			}
			competencyMasters = criteria.list();
			for (DimensionMaster competencyMaster : competencyMasters) {
				Hibernate.initialize(competencyMaster.getParent());
				Hibernate.initialize(competencyMaster.getOwner());
				Hibernate.initialize(competencyMaster.getModifiedBy());
			}
		} catch (RuntimeException re) {
			log.error("listing DimensionList By UserId failed", re);		
		}
		return competencyMasters;
	}

	@Override
	@Transactional
	public List<DimensionProduct> getDimensionProducts(Integer dimensionId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all DimensionMaster instance");
		List<DimensionProduct> dimensionProducts = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionProduct.class, "product");
			criteria.createAlias("product.dimensionId", "dimension");	
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));	
			if(status != 2){
				criteria.add(Restrictions.eq("product.status", status));
			}
			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			dimensionProducts = criteria.list();
			
			for (DimensionProduct dimensionProduct : dimensionProducts) {
				Hibernate.initialize(dimensionProduct.getDimensionId());
				Hibernate.initialize(dimensionProduct.getProductId());
				Hibernate.initialize(dimensionProduct.getMappedBy());
			}
		} catch (RuntimeException re) {
			log.error("listing DimensionProducts failed", re);		
		}
		return dimensionProducts;
	
	}

	@Override
	@Transactional
	public int getTotalRecordsForDimensionProductPagination(Integer dimensionId, Integer status, Class<DimensionProduct> className) {
		log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "dimensionProduct");
			criteria.createAlias("dimensionProduct.dimensionId", "dimension");	
			criteria.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public void updateDimensionProduct(DimensionProduct dimensionProduct) {
		log.info("updating Dimension product instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(dimensionProduct);
			
		} catch (RuntimeException re) {
			log.error("updating DimensionProduct failed", re);
		}
	}

	@Override
	@Transactional
	public List<Object[]> getDimensionsToAddWithProduct(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all competency that not mapped with product id - "+productId);
		List<Object[]> dimensionMasters = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension")
			.add(Restrictions.eq("dimension.status", 1))
		    .add(Subqueries.propertyNotIn("dimension.dimensionId", DetachedCriteria.forClass(DimensionProduct.class, "dimensionProduct")
		    		.createAlias("dimensionProduct.productId", "product")
		    		.add(Restrictions.eq("product.productId", productId))
		            .setProjection(Property.forName("dimensionProduct.dimensionId"))		
		    ))
		    .createAlias("dimension.type", "dimensionType")
		    .add(Restrictions.eq("dimensionType.id", dimensionTypeId))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("dimensionId"))
		    	.add(Projections.property("name")
		    ));
			
			if(!allowRootDimension){
				criteria.add(Restrictions.ne("dimension.dimensionId", 1));
			}
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			if(jtStartIndex != null && jtPageSize != null){
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			
			dimensionMasters = criteria.list();
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return dimensionMasters;
	}

	@Override
	@Transactional
	public Integer getDimensionsToAddWithProductCount(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status) {
		log.info("count all dimension that not mapped with product id - "+productId);
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension")
			.add(Restrictions.eq("dimension.status", 1))
		    .add(Subqueries.propertyNotIn("dimension.dimensionId", DetachedCriteria.forClass(DimensionProduct.class, "dimensionProduct")
		    		.createAlias("dimensionProduct.productId", "product")
		    		.add(Restrictions.eq("product.productId", productId))
		            .setProjection(Property.forName("dimensionProduct.dimensionId"))		
		    ))
		    .createAlias("dimension.type", "dimensionType")
		    .add(Restrictions.eq("dimensionType.id", dimensionTypeId))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("dimensionId"))
		    	.add(Projections.property("name")
		    ));
			
			if(!allowRootDimension){
				criteria.add(Restrictions.ne("dimension.dimensionId", 1));
			}
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public List<Object[]> getDimensionsOfProduct(Integer productId, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize) {

		log.info("listing all dimensions for product id - "+productId);
		List<Object[]> dimensionMasters=null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension")
		    .add(Subqueries.propertyIn("dimension.dimensionId", DetachedCriteria.forClass(DimensionProduct.class, "dimensionProduct")
		    		.createAlias("dimensionProduct.productId", "product")
		    		.add(Restrictions.eq("product.productId", productId))
		            .setProjection(Property.forName("dimensionProduct.dimensionId"))		
		    ))
		    .createAlias("dimension.type", "dimensionType")
		    .add(Restrictions.eq("dimensionType.id", dimensionTypeId))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("dimensionId"))
		    	.add(Projections.property("name")
		    ));
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			dimensionMasters = criteria.list();
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return dimensionMasters;
	
	}

	@Override
	@Transactional
	public int getTotalRecordsForDimensionOfProductPagination(Integer productId, Integer dimensionTypeId, Integer status, Class<DimensionProduct> className) {
		log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DimensionMaster.class, "dimension")
		    .add(Subqueries.propertyIn("dimension.dimensionId", DetachedCriteria.forClass(DimensionProduct.class, "dimensionProduct")
		    		.createAlias("dimensionProduct.productId", "product")
		    		.add(Restrictions.eq("product.productId", productId))
		            .setProjection(Property.forName("dimensionProduct.dimensionId"))		
		    ))
		    .createAlias("dimension.type", "dimensionType")
		    .add(Restrictions.eq("dimensionType.id", dimensionTypeId))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("dimensionId"))
		    	.add(Projections.property("name")
		    ));	
			if(status != 2){
				criteria.add(Restrictions.eq("dimension.status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public DimensionProduct getDimensionProductMappedByProductId(Integer productId, Integer dimensionId, Integer dimensionProductId) {
		log.info("get dimension details by competency name");
		DimensionProduct dimensionProduct = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DimensionProduct.class, "dimensionProduct");
			c.createAlias("dimensionProduct.productId", "product");	
			c.add(Restrictions.eq("product.productId", productId));
			c.createAlias("dimensionProduct.dimensionId", "dimension");	
			c.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			if(dimensionProductId != null && dimensionProductId != 0){
				c.add(Restrictions.ne("competencyMember.competencyProductId", dimensionProductId));
			}
			
			List<DimensionProduct> dimensionProducts = c.list();	
			for (DimensionProduct dimensionProductLoop : dimensionProducts) {
				Hibernate.initialize(dimensionProductLoop.getDimensionId());
				Hibernate.initialize(dimensionProductLoop.getProductId());
				Hibernate.initialize(dimensionProductLoop.getMappedBy());
				Hibernate.initialize(dimensionProductLoop.getModifiedBy());		
			}
			
			dimensionProduct = (dimensionProducts!=null && dimensionProducts.size()!=0)?(DimensionProduct)dimensionProducts.get(0):null;
			
			log.info("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return dimensionProduct;
	}

	@Override
	@Transactional
	public void addDimensionForProduct(DimensionProduct dimensionProduct, String mapOrUnmap) {

		log.info(mapOrUnmap+" dimension for product");
		try {
			log.info("update is executed");
			if(mapOrUnmap == null || mapOrUnmap.isEmpty() || mapOrUnmap.equalsIgnoreCase("map")){
				sessionFactory.getCurrentSession().save(dimensionProduct);
			}else{
				dimensionProduct = (DimensionProduct) sessionFactory.getCurrentSession().createCriteria(DimensionProduct.class, "dimensionProduct")
						.createAlias("dimensionProduct.productId", "product")
						.createAlias("dimensionProduct.dimensionId", "dimension")
	                    .add(Restrictions.eq("dimension.dimensionId", dimensionProduct.getDimensionId().getDimensionId()))
	                    .add(Restrictions.eq("product.productId", dimensionProduct.getProductId().getProductId()))
	                    .uniqueResult();
				sessionFactory.getCurrentSession().delete(dimensionProduct);
			}			
			log.debug(mapOrUnmap+" successful");
		} catch (RuntimeException re) {
			log.error(mapOrUnmap+" DimensionForProduct failed", re);
		}
	}

}
