package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;


@Repository
public class ProductVersionListMasterDAOImpl implements ProductVersionListMasterDAO {
	private static final Log log = LogFactory.getLog(ProductVersionListMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> list() {
		log.debug("listing all DeviceModels instance");
		List<ProductVersionListMaster> devicePlatformVersionListMaster=null;
		try {
			devicePlatformVersionListMaster=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return devicePlatformVersionListMaster;
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> list(int productId) {
		log.debug("listing specific ProductVersionListMaster instance");
		List<ProductVersionListMaster> productVersionListMaster=null;
		try {
			productVersionListMaster=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster v where status=1 and productId=:productId order by v.productVersionListId asc")
														.setParameter("productId", productId).list();
			if (!(productVersionListMaster == null || productVersionListMaster.isEmpty())){
				for (ProductVersionListMaster pvlm : productVersionListMaster) {
					//Commented the following statements when remove the platform column from ProductVersionListMaster. By: Logeswari, On :  11-Feb-2015
					Hibernate.initialize(pvlm.getProductMaster());
					Hibernate.initialize(pvlm.getTestCaseLists());
					Hibernate.initialize(pvlm.getTestSuiteLists());
						
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productVersionListMaster;
	}

	@Override
	@Transactional
	public List<Integer> listVersionId(int productId) {
		log.debug("listing specific ProductVersionListId by productId");
		List<ProductVersionListMaster> productVersionListMaster=null;
		List<Integer> versionIdList = null;
		try {
			versionIdList = sessionFactory.getCurrentSession().createSQLQuery("select productVersionListId from product_version_list_master pv where pv.productId=:prodId")
					.setParameter("prodId", productId).list();			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return versionIdList;
	}
	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersions(int productId, int status) {
		log.debug("listing specific ProductVersionListMaster instance");
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return list(productId);
		}
		List<ProductVersionListMaster> productVersionListMaster=null;
		try {
			productVersionListMaster=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster where productId=:productId and status=:status")
														.setParameter("productId", productId)
														.setParameter("status", status)
														.list();
			if (!(productVersionListMaster == null || productVersionListMaster.isEmpty())){
				for (ProductVersionListMaster pvlm : productVersionListMaster) {
					//Commented the following statements when remove the platform column from ProductVersionListMaster. By: Logeswari, On :  11-Feb-2015
					Hibernate.initialize(pvlm.getProductMaster());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return productVersionListMaster;
	}


	@Override
	@Transactional
	public void add(ProductVersionListMaster productVersionListMaster) {
		log.debug("adding ProductVersionListMaster instance");
		try {
			productVersionListMaster.setStatus(1);
			productVersionListMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(productVersionListMaster);
			
			
			productVersionListMaster.getProductVersionName();
			productVersionListMaster.getProductVersionDescription();
			productVersionListMaster.getWebAppURL();
			productVersionListMaster.getTargetSourceLocation();
			productVersionListMaster.getTargetBinaryLocation();
			productVersionListMaster.getReleaseDate();
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
				
		
	}



	@Override
	@Transactional
	public void update(ProductVersionListMaster productVersionListMaster) {
		log.debug("updating ProductVersionListMaster instance");
		try {
			productVersionListMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(productVersionListMaster);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}



	@Override
	@Transactional
	public void delete(ProductVersionListMaster productVersionListMaster) {
		log.debug("reactivate ProductVersionListMaster instance");
		try {
			productVersionListMaster.setStatus(0);
			productVersionListMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(productVersionListMaster);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			//throw re;
		}		
		
		
	}
	
	@Override
	@Transactional
	public void reactivate(int productVersionId) {
		log.debug("reactivate ProductVersionListMaster instance");
		try {
			ProductVersionListMaster productVersionListMaster = getByProductListId(productVersionId);
			productVersionListMaster.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
			productVersionListMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(productVersionListMaster);
			log.debug("reactivate successful");
		} catch (RuntimeException re) {
			log.error("reactivate failed", re);
		}		
		
		
	}



	@Override
	@Transactional
	public List<ProductVersionListMaster> list(int startIndex, int pageSize) {
		log.debug("listing all DeviceModels instance");
		List<ProductVersionListMaster> devicePlatformVersionListMaster=null;
		try {
			devicePlatformVersionListMaster=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster")
					.setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return devicePlatformVersionListMaster;
	}

	
	@Override
	@Transactional
	public Integer countAllProductVersions(Date startDate,Date endDate) {
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class,"productVersion");
			if (startDate != null) {
				c.add(Restrictions.ge("productVersion.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productVersion.createdDate", endDate));
			}
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all ProductVersions", e);
			return -1;
		}
	}
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listAllProductVersionByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		log.debug("listing all ProductVersion");
		List<ProductVersionListMaster> productVersions=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class, "productVersion");
			if (startDate != null) {
				c.add(Restrictions.ge("productVersion.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productVersion.createdDate", endDate));
			}
			c.addOrder(Order.asc("productVersionListId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            productVersions = c.list();		
			
			if (!(productVersions == null || productVersions.isEmpty())){
				for (ProductVersionListMaster productVersion : productVersions) {
					Hibernate.initialize(productVersion.getProductMaster());
					Hibernate.initialize(productVersion.getTestCaseLists());
					Hibernate.initialize(productVersion.getTestSuiteLists());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productVersions;
	}
	
	@Override
	@Transactional
	public ProductVersionListMaster getLatestProductVersionListMaster(Integer productId) {
		
		log.debug("Getting the latest product version");
		ProductVersionListMaster productVersion=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class,"productVersion")
			.createAlias("productVersion.productMaster", "product")
		    .add(Restrictions.eq("product.productId", productId));
			c.addOrder(Order.desc("createdDate"));
			//c.setProjection(Projections.rowCount());
/*			Integer count = Integer.parseInt(c.uniqueResult().toString());
			if (count <= 0) {
				log.debug("No product versions available for product Id : " + productId);
				return null;
			}	

			DetachedCriteria latestVersionDate = DetachedCriteria.forClass(ProductVersionListMaster.class, "productVersion")
					.createAlias("productVersion.productMaster", "product")
				    .add(Restrictions.eq("product.productId", productId))
				    .setProjection(Projections.max("releaseDate"));
			
			Criteria cl = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class, "productVersion");
			cl.add(Restrictions.ge("productVersion.releaseDate", latestVersionDate));*/
            productVersion = (ProductVersionListMaster) c.list().get(0);		
			
			if (!(productVersion == null)){
				Hibernate.initialize(productVersion.getProductMaster());
				Hibernate.initialize(productVersion.getTestCaseLists());
				Hibernate.initialize(productVersion.getTestSuiteLists());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return productVersion;
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> list(int productId, int startIndex,
			int pageSize) {
		log.debug("listing all DeviceModels instance");
		List<ProductVersionListMaster> devicePlatformVersionListMaster=null;
		try {
			devicePlatformVersionListMaster=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster where productId=:productId")
					.setParameter("productId", productId).setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return devicePlatformVersionListMaster;
	}

	@Override
	@Transactional
	public ProductVersionListMaster getByProductListId(int productVersionListId) {
		log.debug("getting ProductMaster instance by id");
		ProductVersionListMaster productVersionListMaster=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster p where productVersionListId=:productVersionListId").setParameter("productVersionListId", productVersionListId).list();
			productVersionListMaster = (list!=null && list.size()!=0)?(ProductVersionListMaster)list.get(0):null;
			if (productVersionListMaster != null) {
				//Commented the following statements when remove the platform column from ProductVersionListMaster. By: Logeswari, On :  11-Feb-2015
				Hibernate.initialize(productVersionListMaster.getProductMaster());
				Hibernate.initialize(productVersionListMaster.getProductMaster().getGenericeDevices());
				Set<GenericDevices> genericDevices =productVersionListMaster.getProductMaster().getGenericeDevices();
				for(GenericDevices gd:genericDevices){
					Hibernate.initialize(gd.getDeviceLab());
					Hibernate.initialize(gd.getDeviceModelMaster());
					Hibernate.initialize(gd.getHostList());
					Hibernate.initialize(gd.getPlatformType());
					if((gd instanceof MobileType) && ((MobileType) gd).getDeviceMakeMaster() != null){
						Hibernate.initialize(((MobileType) gd).getDeviceMakeMaster());
					}
					if((genericDevices instanceof ServerType) ){
						if(((ServerType) gd).getProcessor() != null){
							Hibernate.initialize(((ServerType) gd).getProcessor());	
						}
						if(((ServerType) gd).getSystemType() != null){
							Hibernate.initialize(((ServerType) gd).getSystemType());	
						}						
					}
				}
				if(productVersionListMaster.getTestSuiteLists() != null){
					Hibernate.initialize(productVersionListMaster.getTestSuiteLists());
					Set<TestSuiteList> testSuiteListset = productVersionListMaster.getTestSuiteLists();
					for (TestSuiteList testSuiteList : testSuiteListset) {
						Hibernate.initialize(testSuiteList.getProductMaster());
						Hibernate.initialize(testSuiteList.getScriptTypeMaster());
						if(testSuiteList.getTestCaseLists()!= null){
							Hibernate.initialize(testSuiteList.getTestCaseLists());	
						}						
					}
				}
				if(null != productVersionListMaster.getWebAppURL()){
					Hibernate.initialize(productVersionListMaster.getWebAppURL());
				}
			}
			log.debug("getByProductVersionListId successful");
		} catch (RuntimeException re) {
			log.error("getByProductVersionListId failed", re);
		}
		return productVersionListMaster;
        
	}
	
	@Override
	@Transactional
	public int getProductTypeByVersionId(int productVersionListId, int productId, int workPackageId) {
		log.debug("getting ProductTypeByVersionId");
		int prodType =0;
		try {
			if(workPackageId != -1){
				prodType=((Number) sessionFactory.getCurrentSession().createSQLQuery("select productTypeId from product_master pm,"
						+ " product_version_list_master pvm, product_build pb, workpackage wp where pm.productId=pvm.productId "
						+ "and pvm.productVersionListId=pb.productVersionId and pb.productBuildId=wp.productBuildId "
						+ "and wp.workPackageId=:wpId")
						.setParameter("wpId", workPackageId)
						.uniqueResult()).intValue();
			}else if(productVersionListId != -1){
				prodType=((Number) sessionFactory.getCurrentSession().createSQLQuery("select productTypeId from product_master pm, product_version_list_master pvm"
						+ " where  pm.productId=pvm.productId and pvm.productVersionListId=:versionId")
						.setParameter("versionId", productVersionListId)
						.uniqueResult()).intValue();
			}else{
				prodType=((Number) sessionFactory.getCurrentSession().createSQLQuery("select pm.productTypeId from product_master pm where pm.productId=:pid")
						.setParameter("pid", productId)
						.uniqueResult()).intValue();
			}
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return prodType;	
	}
	
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting ProductVersionListMaster total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from product_version_list_master").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> list(int productId, String[] parameters) {
		log.debug("listing parameterized ProductVersionListMaster instance");
		List<ProductVersionListMaster> productVersionListMaster=null;
		try {
			
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class, "prodVer");
			crit.createAlias("prodVer.productMaster", "productMaster");
			if(productId !=-1){
				crit.add(Restrictions.eq("productMaster.productId", productId));
				crit.add(Restrictions.eq("prodVer.status", 1));
			}else{
				crit.add(Restrictions.eq("prodVer.status", 1));
			}
			
			productVersionListMaster = crit.list();
			if (!(productVersionListMaster == null || productVersionListMaster.isEmpty())){
				for (ProductVersionListMaster pvlm : productVersionListMaster) {
					//Commented the following statements when remove the platform column from ProductVersionListMaster. By: Logeswari, On :  11-Feb-2015
					Hibernate.initialize(pvlm.getProductMaster());
				}
			}
			log.debug("list successful");
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		
			//throw re;
		}
		return productVersionListMaster;    
	}
	
	@Override
	@Transactional
	public boolean isProductVersionExistingByName(ProductVersionListMaster productVersionListMaster) {

		String hql = "from ProductVersionListMaster c where c.productVersionName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", productVersionListMaster.getProductVersionName().trim()).list();
		if (instances == null || instances.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public boolean isProductVersionExistingByNameForUpdate(ProductVersionListMaster productVersionListMaster, int productVersionListId) {
		ProductVersionListMaster productVersion = new ProductVersionListMaster();
		log.debug("Verifying if ProductVersion name already exist");
		List<ProductVersionListMaster> productVersionList=null;
		try {			
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class, "prodVer");			
			if(productVersionListId !=-1){
				crit.add(Restrictions.ne("prodVer.productVersionListId", productVersionListId));
			}
			crit.add(Restrictions.eq("prodVer.productVersionName", productVersionListMaster.getProductVersionName().trim()));
			productVersionList = crit.list();
			productVersion = (productVersionList!=null && productVersionList.size()!=0)?(ProductVersionListMaster)productVersionList.get(0):null;
			if(productVersion != null){
				if(productVersion.getProductVersionListId() != -1){//Version with same name already exists
					return true;
				}else{
					return false;
				}
			}
			if (!(productVersionList == null || productVersionList.isEmpty())){				
				for (ProductVersionListMaster pvlm : productVersionList) {
					//Commented the following statements when remove the platform column from ProductVersionListMaster. By: Logeswari, On :  11-Feb-2015
					Hibernate.initialize(pvlm.getProductMaster());
				}
			}
			log.debug("Verifying if ProductVersion name already exist");
			
		} catch (RuntimeException re) {
			log.error("Version with same name does not exists", re);
		}
		return false;    
	
	}
	
	@Override
	@Transactional
	public ProductVersionListMaster productVersionListByName(String productVersionName) {
		log.debug("listing specific ProductVersionListMaster instance");
		ProductVersionListMaster productVersionListMaster=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster where productVersionName=:productVersionName")
														.setParameter("productVersionName", productVersionName).list();
			productVersionListMaster=(list!=null && list.size()!=0)?(ProductVersionListMaster)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productVersionListMaster;
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> getUserAssociatedProductVariants(int userRoleId, int userId) {
		List<WorkPackage> listOfWorkPackages = null;
		List<ProductVersionListMaster> listOfProductsVersions = new ArrayList<ProductVersionListMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wp.productBuild", "pb");
			c.createAlias("pb.productVersion", "pv");
			c.add(Restrictions.eq("pv.status", 1));
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
				c.createAlias("wptcep.testLead", "testLead");
				c.add(Restrictions.eq("testLead.userId", userId));
			}else if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.eq("tester.userId", userId));
			}
			
			listOfWorkPackages = c.list();
			log.info("Result Set Size of WorkPackages : " + listOfWorkPackages.size());
			
			for (WorkPackage workPackage : listOfWorkPackages) {
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
				if(!listOfProductsVersions.contains(workPackage.getProductBuild().getProductVersion())){
					listOfProductsVersions.add(workPackage.getProductBuild().getProductVersion());
				}
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProductsVersions;
	}
	
	@Override
	@Transactional
	public int getProductVersionIdBybuildId(int productBuildId) {
		log.debug("get ProductVersionId By buildId");
		int productVersionMasterListId = 0;
		String sql = "";
		try {
				sql="SELECT pb.productVersionId  FROM product_build pb "
						+ " WHERE pb.productBuildId=:prodBulId";
				productVersionMasterListId = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
												.setParameter("prodBulId", productBuildId)
												.uniqueResult()).intValue(); 
			 
		} catch (RuntimeException re) {
			log.error("get ProductVersionId By buildId", re);
		}
		return productVersionMasterListId;
	}
	
	@Override
	@Transactional
	public ProductVersionListMaster getProductVersionListByProductIdAndVersionName(Integer productId,String productVersionName) {
		log.debug("listing specific ProductVersionListMaster instance");
		ProductVersionListMaster productVersionListMaster=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from ProductVersionListMaster pvm where pvm.productMaster.productId=:productId and pvm.productVersionName=:productVersionName")
								.setParameter("productId", productId)
								.setParameter("productVersionName", productVersionName).list();
			productVersionListMaster=(list!=null && list.size()!=0)?(ProductVersionListMaster)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productVersionListMaster;
	}

}
