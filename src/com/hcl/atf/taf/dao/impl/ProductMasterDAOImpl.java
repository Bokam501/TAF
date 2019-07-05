package com.hcl.atf.taf.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.HostListDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.MetricsMaster;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.RunConfigurationTSHasTC;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlanTSHasTC;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceExperienceSummaryDTO;
import com.hcl.atf.taf.model.dto.RiskHazardTraceabilityMatrixDTO;
import com.hcl.atf.taf.model.json.JsonRiskHazardTraceabilityMatrix;
import com.hcl.atf.taf.model.json.JsonUserRoles;

@Repository
public class ProductMasterDAOImpl implements ProductMasterDAO {
	private static final Log log = LogFactory.getLog(ProductMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired(required=true)
    private EnvironmentDAO environmentDAO;
	@Autowired(required=true)
    private TestSuiteListDAO testSuiteListDAO;
	
	@Autowired(required=true)
    private UserListDAO userListDAO;
	@Autowired(required=true)
    private WorkPackageDAO workPackageDAO;

	@Autowired(required=true)
    private HostListDAO hostListDAO;
	@Autowired(required=true)
    private TestCaseListDAO testCaseListDAO;
	@Autowired(required=true)
    private ProductFeatureDAO featureDAO;
	@Autowired(required=true)
    private ProductBuildDAO productBuildDAO;
		
	@Override
	@Transactional
	public List<ProductMaster> list(boolean initialize) {
		log.debug("listing all ProductMaster instance");
		List<ProductMaster> productMaster=null;
		try {
			//Changes on 'Soft delete : Delete should only make the status of the Product inactive and NOT delete from database'
			productMaster=sessionFactory.getCurrentSession().createQuery("from ProductMaster p where Status='1'").list();
			if(initialize){
				for (ProductMaster productMaster2 : productMaster) {
					Hibernate.initialize(productMaster2.getTestFactory());
					Hibernate.initialize(productMaster2.getProductMode());
					Hibernate.initialize(productMaster2.getCustomer());
					Hibernate.initialize(productMaster2.getProductType());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return productMaster;
	}
	
		
	@Override
	@Transactional
	public int getTotalRecordCount() {
		log.debug("getting Product List total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from product_master").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
			//throw re;
		}
		return count;
	}
	@Override
	@Transactional
	public List<ProductMaster> listByNames() {
		log.debug("listing all ProductMaster instance");
		log.debug("inside the productMaster dao impl");
		List<ProductMaster> productMaster=null;
		try {
			//Changes on 'Soft delete : Delete should only make the status of the Product inactive and NOT delete from database'
			productMaster=sessionFactory.getCurrentSession().createQuery("select productName from ProductMaster where status=1").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return productMaster;
	}



	@Override
	@Transactional
	public ProductMaster getByProductId(int productId) {
		log.debug("getting ProductMaster instance by id");
		ProductMaster productMaster=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductMaster p where productId=:productId").setParameter("productId", productId)
					.list();
			productMaster=(list!=null && list.size()!=0)?(ProductMaster)list.get(0):null;
			if (productMaster != null) {
				Hibernate.initialize(productMaster.getProductVersionListMasters());
				Hibernate.initialize(productMaster.getProductMode());
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getDefectManagementSystems());
				Hibernate.initialize(productMaster.getDefectManagementSystemMappings());
				Hibernate.initialize(productMaster.getProductFeatures());
				Hibernate.initialize(productMaster.getTestCaseLists());
				Hibernate.initialize(productMaster.getDecouplingCategory());

				Set<TestCaseList> testCaseLists=productMaster.getTestCaseLists();
				for (TestCaseList testCaseList : testCaseLists) {
					Hibernate.initialize(testCaseList.getProductFeature());
				}
				Hibernate.initialize(productMaster.getTestSuiteLists());
				Hibernate.initialize(productMaster.getTestRunConfigurationParents());
				Hibernate.initialize(productMaster.getTestManagementSystems());
				Set<TestManagementSystem> testSystems  = productMaster.getTestManagementSystems();
				if(testSystems != null){
					for(TestManagementSystem testManagementSystem : testSystems){
						Hibernate.initialize((testManagementSystem.getTestManagementSystemMappings()));
					}
				}
				Hibernate.initialize(productMaster.getDefectManagementSystems());
				Set<DefectManagementSystem> defectSystem  = productMaster.getDefectManagementSystems();
				if(testSystems != null){
					for(DefectManagementSystem defectManagementSystem : defectSystem){
						Hibernate.initialize((defectManagementSystem.getDefectManagementSystemMappings()));
					}
				}
				Hibernate.initialize(productMaster.getGenericeDevices());
				if(productMaster.getGenericeDevices()!=null && !productMaster.getGenericeDevices().isEmpty()){
					Set<GenericDevices> genericDevices=productMaster.getGenericeDevices();
					for (GenericDevices gd : genericDevices) {
						Hibernate.initialize(gd.getDeviceLab());
						Hibernate.initialize(gd.getPlatformType());
						if(gd.getDeviceModelMaster() != null){
						Hibernate.initialize(gd.getDeviceModelMaster());
						}
						Hibernate.initialize(gd.getHostList());
						
						if((gd instanceof MobileType) && ((MobileType) gd).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) gd).getDeviceMakeMaster());
						}
						if((gd instanceof ServerType) ){
							if(((ServerType) gd).getProcessor() != null){
								Hibernate.initialize(((ServerType) gd).getProcessor());	
							}
							if(((ServerType) gd).getSystemType() != null){
								Hibernate.initialize(((ServerType) gd).getSystemType());	
							}						
						}
					}
				}
				Hibernate.initialize(productMaster.getHostLists());
				Hibernate.initialize(productMaster.getProductTeamResources());
				Hibernate.initialize(productMaster.getProductCoreResources());
				Hibernate.initialize(productMaster.getProductType());
				if(productMaster.getProductType().getTypeName() != null)
					Hibernate.initialize(productMaster.getProductType().getTypeName());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return productMaster;
        
	}
	
	@Override
	@Transactional
	public ProductMaster getProductDetailsById(Integer productId) {
		log.debug("Inside getProductDetailsById");
		ProductMaster productMaster = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductMaster p where productId=:productId").setParameter("productId", productId).list();
			productMaster = (list != null && list.size() != 0) ? (ProductMaster)list.get(0) : null;
			log.debug("getProductDetailsById successful");
		} catch (RuntimeException re) {
			log.error("getProductDetailsById failed", re);
		}
		return productMaster;
	}
	
	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithDevicesHostList(Integer productId) {
		log.debug("Inside getProductDetailsByIdWithDevicesList");
		ProductMaster productMaster = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductMaster p where productId=:productId").setParameter("productId", productId).list();
			productMaster = (list != null && list.size() != 0) ? (ProductMaster)list.get(0) : null;
			if(productMaster != null){
				Hibernate.initialize(productMaster.getGenericeDevices());
				if(productMaster.getGenericeDevices()!=null && !productMaster.getGenericeDevices().isEmpty()){
					Set<GenericDevices> genericDevices=productMaster.getGenericeDevices();
					for (GenericDevices gd : genericDevices) {
						Hibernate.initialize(gd.getDeviceLab());
						Hibernate.initialize(gd.getPlatformType());
						Hibernate.initialize(gd.getDeviceModelMaster());
						Hibernate.initialize(gd.getHostList());
						
						if((gd instanceof MobileType) && ((MobileType) gd).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) gd).getDeviceMakeMaster());
						}
						if((gd instanceof ServerType) ){
							if(((ServerType) gd).getProcessor() != null){
								Hibernate.initialize(((ServerType) gd).getProcessor());	
							}
							if(((ServerType) gd).getSystemType() != null){
								Hibernate.initialize(((ServerType) gd).getSystemType());	
							}						
						}
					}
				}
				Hibernate.initialize(productMaster.getHostLists());
			}
			log.debug("getProductDetailsByIdWithDevicesList successful");
		} catch (RuntimeException re) {
			log.error("getProductDetailsByIdWithDevicesList failed", re);
		}
		return productMaster;
	}
	
	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithDevicesList(Integer productId) {
		log.debug("Inside getProductDetailsByIdWithDevicesList");
		ProductMaster productMaster = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductMaster p where productId=:productId").setParameter("productId", productId).list();
			productMaster = (list != null && list.size() != 0) ? (ProductMaster)list.get(0) : null;
			if(productMaster != null){
				Hibernate.initialize(productMaster.getGenericeDevices());
				if(productMaster.getGenericeDevices()!=null && !productMaster.getGenericeDevices().isEmpty()){
					Set<GenericDevices> genericDevices=productMaster.getGenericeDevices();
					for (GenericDevices gd : genericDevices) {
						Hibernate.initialize(gd.getDeviceLab());
						Hibernate.initialize(gd.getPlatformType());
						Hibernate.initialize(gd.getDeviceModelMaster());
						Hibernate.initialize(gd.getHostList());
						
						if((gd instanceof MobileType) && ((MobileType) gd).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) gd).getDeviceMakeMaster());
						}
						if((gd instanceof ServerType) ){
							if(((ServerType) gd).getProcessor() != null){
								Hibernate.initialize(((ServerType) gd).getProcessor());	
							}
							if(((ServerType) gd).getSystemType() != null){
								Hibernate.initialize(((ServerType) gd).getSystemType());	
							}						
						}
					}
				}
			}
			log.debug("getProductDetailsByIdWithDevicesList successful");
		} catch (RuntimeException re) {
			log.error("getProductDetailsByIdWithDevicesList failed", re);
		}
		return productMaster;
	}
	
	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithHostList(Integer productId) {
		log.debug("Inside getProductDetailsByIdWithHostList");
		ProductMaster productMaster = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductMaster p where productId=:productId").setParameter("productId", productId).list();
			productMaster = (list != null && list.size() != 0) ? (ProductMaster)list.get(0) : null;
			if(productMaster != null){
				Hibernate.initialize(productMaster.getHostLists());
			}
			log.debug("getProductDetailsByIdWithHostList successful");
		} catch (RuntimeException re) {
			log.error("getProductDetailsByIdWithHostList failed", re);
		}
		return productMaster;
	}
	
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting ProductMaster total records");
		int count =0;
		try {
			//Changes on 'Soft delete : Delete should only make the status of the Product inactive and NOT delete from database'
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from product_master where status=1").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}



	@Override
	@Transactional
	public Integer add(ProductMaster productMaster) {
		log.debug("adding ProductMaster instance");
		try {
			productMaster.setStatus(1);
			productMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(productMaster);
			log.debug("add successful");
			return productMaster.getProductId();
		} catch (RuntimeException re) {
			log.error("add failed", re);
			return null;
		}
		
	}



	@Override
	public void delete(ProductMaster productMaster) {
		log.debug("deleting ProductMaster instance");
		try {
			productMaster.setStatus(TAFConstants.ENTITY_STATUS_INACTIVE);
			productMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(productMaster);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}	
		
	}

	@Override
	public void reactivate(ProductMaster productMaster) {
		log.debug("reactivating ProductMaster instance");
		try {
			productMaster.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
			productMaster.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(productMaster);
			log.debug("reactivating successful");
		} catch (RuntimeException re) {
			log.error("reactivating failed", re);
		}	
		
	}


	@Override
	@Transactional
	public List<ProductMaster> list(int startIndex, int pageSize) {
		log.debug("listing all ProductMaster instance");
		List<ProductMaster> productMaster=null;
		try {
			//Changes on 'Soft delete : Delete should only make the status of the Product inactive and NOT delete from database'			
			productMaster=sessionFactory.getCurrentSession().createQuery("from ProductMaster where status='1'")
			.setFirstResult(startIndex).setMaxResults(pageSize).list();
			if (!(productMaster == null || productMaster.isEmpty())){
				for (ProductMaster product : productMaster) {
					Hibernate.initialize(product.getTestFactory());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productMaster;
	}

	@Override
	@Transactional
	public List<ProductMaster> listbyCustomerId(int customerId, Integer startIndex, Integer pageSize) {
		log.debug("listing all ProductMaster instance");
		List<ProductMaster> productMaster=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");			
			c.createAlias("product.customer", "cust");			
			c.add(Restrictions.eq("cust.customerId", customerId));
			c.add(Restrictions.eq("product.status", 1));			
			if(startIndex != null && pageSize != null){
				c.setFirstResult(startIndex);
				c.setMaxResults(pageSize);
			}
			
			productMaster= c.list();
			
			if (!(productMaster == null || productMaster.isEmpty())){
				for (ProductMaster product : productMaster) {					
					Hibernate.initialize(product.getTestFactory());
					Hibernate.initialize(product.getCustomer());				
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productMaster;
	}

	@Override
	@Transactional
	public void update(ProductMaster productMaster) {
		log.debug("updating ProductMaster instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(productMaster);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}
	@Override
	@Transactional
    public List<ProductMaster> list(String[] parameters) {
		log.debug("listing parameterized ProductMaster instance");
		List<ProductMaster> productMaster=null;
		try {
			StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("p."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
			//Changes on 'Soft delete : Delete should only make the status of the Product inactive and NOT delete from database'
			productMaster=sessionFactory.getCurrentSession().createQuery("from ProductMaster p where status='1'")
	                .list();
			if(productMaster != null && productMaster.size()>0){
				for (ProductMaster productMaster2 : productMaster) {
					Hibernate.initialize(productMaster2.getTestFactory());
					Hibernate.initialize(productMaster2.getCustomer());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return productMaster;       
    }
	
	
	@Override
	@Transactional
	public boolean isProductExistingByName(String productName) {

		String hql = "from ProductMaster c where productName =:productName";
		List products = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productName", productName).list();
		if (products == null || products.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public boolean isProductExistingByName(ProductMaster productMaster) {

		String hql = "from ProductMaster c where c.productName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", productMaster.getProductName().trim()).list();
		if (instances == null || instances.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public ProductMaster getProductByName(String productName) {
		log.debug("listing specific ProductMaster instance");
		ProductMaster productMaster=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from ProductMaster where productName=:productName")
														.setParameter("productName", productName).list();
			productMaster=(list!=null && list.size()!=0)?(ProductMaster)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return productMaster;
	}

	@Override
	@Transactional
	public void addProductUserRole(ProductUserRole productUserRole) {
		log.debug("adding addProductUserRole instance");
		try {
			
			
			productUserRole.setDescription("test");
			productUserRole.setName("test");
			productUserRole.setStatus(1);
			productUserRole.setCreatedDate(new Date(System.currentTimeMillis()));
			productUserRole.setModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(productUserRole);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public List<ProductUserRole> listProductUserRole(int productId,
			int startIndex, int pageSize) {
		log.debug("listing listProductUserRole instance");
		List<ProductUserRole> productUserRole = null;
		try {
			productUserRole = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ProductUserRole pur where pur.product.productId = "
									+ productId	+" and status=1")
					.list();
			if (!(productUserRole == null || productUserRole
					.isEmpty())) {
				for (ProductUserRole dl : productUserRole) {
					Hibernate.initialize(dl.getProduct());
					Hibernate.initialize(dl.getRole());
					Hibernate.initialize(dl.getUser());
					Hibernate.initialize(dl.getUser().getUserTypeMasterNew());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productUserRole;
	}

	@Override
	public List<UserRoleMaster> getAllRoles() {
				log.debug("listing getAllRoles instance");
				List<UserRoleMaster> userRoleMaster = null;
				try {
					userRoleMaster = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from UserRoleMaster urm ")
							.list();
					
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
					// throw re;
				}
				return userRoleMaster;
	}

	@Override
	@Transactional
	public boolean isProductUserRoleExits(int productId, int userId, int roleId) {
		String hql = "from ProductUserRole pur where pur.product.productId = :productId and pur.user.userId=:userId  and status=1 ";
		List productUserRole = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productId", productId).setParameter("userId", userId).list();
		if (productUserRole == null || productUserRole.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public ProductUserRole getProductUserRole(int productId, int userId) {
		try {
		/*String hql = "from ProductUserRole pur where pur.product.productId = :productId and pur.user.userId=:userId";
		List<ProductUserRole> productUserRoles = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productId", productId).setParameter("userId", userId).list();*/
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class,"productUserRole");
		c.createAlias("productUserRole.product", "product");
		c.createAlias("productUserRole.user", "user");
		c.add(Restrictions.eq("product.productId", productId));
		c.add(Restrictions.eq("user.userId", userId));
		List<ProductUserRole> productUserRoles =c.list();
		if(productUserRoles != null && productUserRoles.size() >0) {
			for(ProductUserRole productUserRole:productUserRoles){
				Hibernate.initialize(productUserRole.getRole());
			}
		}
		if (productUserRoles == null || productUserRoles.isEmpty()) 
		    return null;
		else 
			return (ProductUserRole)productUserRoles.get(0);
		}catch(RuntimeException re) {
			log.error("Error in getProductUserRole",re);
		}
		return null;
	}
	

	@Override
	@Transactional
	public Environment getEnvironmentByName(String environmentName) {
		Environment environment=null;
		String hql = "from Environment e where e.environmentName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", environmentName.trim()).list();
		environment=(instances!=null && instances.size()!=0)?(Environment)instances.get(0):null;
		if(environment!=null){
			Hibernate.initialize(environment.getEnvironmentCategory());
			Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
			Hibernate.initialize(environment.getWorkPackageList());
			Hibernate.initialize(environment.getWorkPackageTestCaseExecutionPlanList());
		}
		return environment;
	}
	
	@Override
	@Transactional
	public Environment getEnvironmentByNameByProduct(String environmentName,String productId) {
		// TODO Auto-generated method stub
		Environment environment=null;
		String hql = "from Environment e where e.environmentName = :name and e.productMaster.productId = :productMasterId) and status=1";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", environmentName.trim()).setParameter("productMasterId",Integer.parseInt(productId)).list();
		environment=(instances!=null && instances.size()!=0)?(Environment)instances.get(0):null;
		return environment;
	}
	
	@Override
	@Transactional
	public List<ProductUserRole> listProductUserRole(int productId) {
		log.debug("listing listProductUserRole instance");
		List<ProductUserRole> productUserRole = null;
		try {
			productUserRole = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ProductUserRole pur where pur.product.productId = "
									+ productId	+" and status=1")
					.list();
			if (!(productUserRole == null || productUserRole
					.isEmpty())) {
				for (ProductUserRole dl : productUserRole) {
					Hibernate.initialize(dl.getProduct());
					Hibernate.initialize(dl.getRole());
					Hibernate.initialize(dl.getUser());
					Hibernate.initialize(dl.getUser().getUserTypeMasterNew());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return productUserRole;
	}

	@Override
	@Transactional
	public List<ProductMaster> listProductsByTestFactoryId(int testFactoryId) {
		log.debug("listing all ProductMaster instance");
		List<ProductMaster> listOfProductMaster=null;
		try {
			listOfProductMaster = sessionFactory.getCurrentSession().createQuery("from ProductMaster p where p.status=1 and p.testFactory.testFactoryId=:testFactoryId").setParameter("testFactoryId",testFactoryId)
					.list();
			for (ProductMaster productMaster : listOfProductMaster) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getTestSuiteLists());
			}
			log.debug("list products by test factory Id successful");
		} catch (RuntimeException re) {
			log.error("list products by test factory Id  failed", re);		
		}
		return listOfProductMaster;
	}
	
	@Override
	@Transactional
	public Integer getProductsCountByTestFactoryId(int testFactoryId, int jtStartIndex, int jtPageSize) {
		log.debug("getProductsCount By TestFactoryId instance");
		int testFactoryProductCount = 0;
		String sql= "";
		try {
			sql="SELECT count(*) FROM product_master pm "
					+ "inner join test_factory tf on pm.testFactoryId = tf.testFactoryId "
					+ "where tf.testFactoryId=:tfId and pm.status=1";
			
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				testFactoryProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();	
			}else{
				testFactoryProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.uniqueResult()).intValue();	
			}
		} catch (RuntimeException re) {
			log.error("getProductsCount By TestFactoryId instance failed", re);		
		}
		return testFactoryProductCount;
	}
	
	@Override
	@Transactional
	public List<ProductMaster> getProductsByWorkPackageForUserId(int userRoleId,int userId, int filter) {
		List<WorkPackage> listOfWorkPackages = null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wp.productBuild", "pb");
			c.createAlias("pb.productVersion", "pv");
			c.createAlias("pv.productMaster", "product");
			c.add(Restrictions.eq("product.status", 1));
			if(filter == 0){
				if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					c.createAlias("wptcep.testLead", "testLead");
					c.add(Restrictions.eq("testLead.userId", userId));
				}else if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
					c.createAlias("wptcep.tester", "tester");
					c.add(Restrictions.eq("tester.userId", userId));
				}
			}else{
				c.createAlias("wptcep.testLead", "testLead");
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.disjunction().add(
				        Restrictions.or(Restrictions.eq("tester.userId", userId),
				                		Restrictions.eq("testLead.userId", userId))));
			}
			
			listOfWorkPackages = c.list();
			log.debug("Result Set Size of WorkPackages : " + listOfWorkPackages.size());
			
			for (WorkPackage workPackage : listOfWorkPackages) {
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getCustomer());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
				if(!listOfProducts.contains(workPackage.getProductBuild().getProductVersion().getProductMaster())){
					listOfProducts.add(workPackage.getProductBuild().getProductVersion().getProductMaster());
				}
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductMode());
			}
			log.debug("Result Set Size of Products : " + listOfProducts.size());
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}

	@Override
	@Transactional
	/**
	 * Get products associated with user 
	 * @param userRoleId
	 * @param userId
	 * @param filter - 0 - both
	 * 				   1- Test Factory
	 * 				   2- Test Engagement
	 * @return List<ProductMaster>
	 */
	public List<ProductMaster> getProductsByProductUserRoleForUserId(
			int userRoleId, int userId,int filter) {
		List<ProductUserRole> listOfProductUserRoles = null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class, "pur");
			c.createAlias("pur.role", "productUserRole");
			c.createAlias("pur.user", "productUser");
			c.createAlias("pur.product", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.engagementTypeMaster", "engagementType");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("productUserRole.userRoleId", userRoleId));
			c.add(Restrictions.eq("productUser.userId", userId));
			c.add(Restrictions.eq("tf.status", 1));
			c.add(Restrictions.eq("pur.status", 1));
			if(filter != 0){
				c.add(Restrictions.eq("engagementType.engagementTypeId", filter));
			}
			listOfProductUserRoles = c.list();
			log.debug("Getting list of Products where user >>> "+ userId +" is the Test Manager" + listOfProductUserRoles.size());
			
			for (ProductUserRole productUserRole : listOfProductUserRoles) {
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getProduct().getTestFactory());
				Hibernate.initialize(productUserRole.getProduct().getCustomer());
				Hibernate.initialize(productUserRole.getProduct().getProductType());
				Hibernate.initialize(productUserRole.getProduct().getProductMode());
				listOfProducts.add(productUserRole.getProduct());
			}
		} catch (Exception e) {
			log.error("Getting Products where user >>> "+ userId +" is the Test Manager", e);
		}
		return listOfProducts;
	}
	
	@Override
	@Transactional
	/**
	 * Get products associated with user 
	 * @param userRoleId
	 * @param userId
	 * @param filter - 0 - both
	 * 				   1- Test Factory
	 * 				   2- Test Engagement
	 * @return List<ProductMaster>
	 */
	public List<ProductMaster> getProductsByProductUserRoleForUserIdOfTestFactory(
			int userRoleId, int userId, int testFactoryId, int filter) {
		List<ProductUserRole> listOfProductUserRoles = null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class, "pur");
			c.createAlias("pur.role", "productUserRole");
			c.createAlias("pur.user", "productUser");
			c.createAlias("pur.product", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.engagementTypeMaster", "engagementType");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("productUserRole.userRoleId", userRoleId));
			c.add(Restrictions.eq("productUser.userId", userId));
			c.add(Restrictions.eq("tf.status", 1));
			c.add(Restrictions.eq("pur.status", 1));
			c.add(Restrictions.eq("tf.testFactoryId", testFactoryId));			
			if(filter != 0){
				c.add(Restrictions.eq("engagementType.engagementTypeId", filter));
			}
			listOfProductUserRoles = c.list();
			log.debug("Getting list of Products where user >>> "+ userId +" is the Test Manager" + listOfProductUserRoles.size());
			
			for (ProductUserRole productUserRole : listOfProductUserRoles) {
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getProduct().getTestFactory());
				Hibernate.initialize(productUserRole.getProduct().getCustomer());
				Hibernate.initialize(productUserRole.getProduct().getProductType());
				Hibernate.initialize(productUserRole.getProduct().getProductMode());
				listOfProducts.add(productUserRole.getProduct());
			}
		} catch (Exception e) {
			log.error("Getting Products where user >>> "+ userId +" is the Test Manager", e);
		}
		return listOfProducts;
	}
	@Override
	@Transactional
	/**
	 * Get products associated with user 
	 * @param userRoleId
	 * @param userId
	 * @param filter - 0 - both
	 * 				   1- Test Factory
	 * 				   2- Test Engagement
	 * @return List<ProductMaster>
	 */
	public List<ProductMaster> getProductsByProductUserRoleForUserIdNotByRole(
			int userRoleId, int userId,int filter) {
		List<ProductUserRole> listOfProductUserRoles = null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductUserRole.class, "pur");
			c.createAlias("pur.role", "productUserRole");
			c.createAlias("pur.user", "productUser");
			c.createAlias("pur.product", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.engagementTypeMaster", "engagementType");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("productUser.userId", userId));
			c.add(Restrictions.eq("tf.status", 1));
			c.add(Restrictions.eq("pur.status", 1));
			if(filter != 0){
				c.add(Restrictions.eq("engagementType.engagementTypeId", filter));
			}
			listOfProductUserRoles = c.list();
			log.debug("Getting list of Products where user >>> "+ userId +" is the Test Manager" + listOfProductUserRoles.size());
			
			for (ProductUserRole productUserRole : listOfProductUserRoles) {
				Hibernate.initialize(productUserRole.getProduct());
				Hibernate.initialize(productUserRole.getProduct().getTestFactory());
				Hibernate.initialize(productUserRole.getProduct().getCustomer());
				Hibernate.initialize(productUserRole.getProduct().getProductType());
				Hibernate.initialize(productUserRole.getProduct().getProductMode());
				listOfProducts.add(productUserRole.getProduct());
			}
		} catch (Exception e) {
			log.error("Getting Products where user >>> "+ userId +" is the Test Manager", e);
		}
		return listOfProducts;
	}

	@Override
	@Transactional
	public List<ProductMaster> getProductsByTestFactoryId(int testFactoryId) {
		List<ProductMaster> listOfProducts = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.testFactory", "testfactory");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("testfactory.testFactoryId", testFactoryId));
			listOfProducts = c.list();
			for (ProductMaster productMaster : listOfProducts) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}
	
	@Override
	public List<UserRoleMaster> getRolesBasedResource() {
				log.debug("listing getRolesBasedResource instance");
				List<UserRoleMaster> userRoleMaster = null;
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoleMaster.class, "userRoleMaster");
					c.add(Restrictions.in("userRoleMaster.userRoleId",  Arrays.asList(3, 4, 5)));
					userRoleMaster = c.list();
					
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return userRoleMaster;
	}
	
	@Override
	@Transactional
	public UserRoleMaster getRolesByUserRoleId(int userRoleId) {
				log.debug("listing UserRole by userRoleId");
				List<UserRoleMaster> userRoleMaster = null;
				UserRoleMaster userRole = null;
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoleMaster.class, "userRoleMaster");
					c.add(Restrictions.eq("userRoleMaster.userRoleId",  userRoleId));
					userRoleMaster = c.list();	
					
					userRole=(userRoleMaster!=null && userRoleMaster.size()!=0)?(UserRoleMaster)userRoleMaster.get(0):null;
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return userRole;
	}
	
	@Override
	@Transactional
	public UserRoles mapUserWithRoles(JsonUserRoles jsonUserRoles) {
		log.debug("mapping Role to User");
		UserList user = null;
		UserRoleMaster userRole = null;
		UserRoles userRoles = new UserRoles();
		UserRoles userRolesUI = jsonUserRoles.getUserRoles();
		try {		
			userRole=userListDAO.getRoleByLabel(jsonUserRoles.getRoleName());
			
			if (userRole == null) {
				log.debug("userRoleList with specified id not found : " +userRolesUI.getRole().getUserRoleId());
			}
			log.debug("user>>>>>"+jsonUserRoles.getUserId());
			
			user=userListDAO.getByUserId(jsonUserRoles.getUserId());
			if (user == null) {
				log.debug("userList with specified id not found : " + userRolesUI.getUserList().getUserId());
			}			
			userRoles.setUserList(user);
			userRoles.setRole(userRole);
			userRoles.setCreatedDate(DateUtility.getCurrentTime());
			userRoles.setFromDate(userRolesUI.getFromDate());
			userRoles.setToDate(userRolesUI.getToDate());
			userRoles.setStatus(1);
			userRoles.setCreatedBy(userListDAO.getUserByLoginId(jsonUserRoles.getCreatedBy()));
			sessionFactory.getCurrentSession().save(userRoles);
			log.debug("mapped Role to User at UserRoles");
		} catch (RuntimeException re) {
			log.error("Failed to map Role to User at UserRoles", re);
		}
		return userRoles;
	}
	
	@Override
    @Transactional
    public void updateUserRoles(UserRoles userRoles) {
		log.debug("updating UserRoles Mapping");
		try {			
			sessionFactory.getCurrentSession().saveOrUpdate(userRoles);
			log.debug("Updated UserRoles  from update : " + userRoles.getUserList().getFirstName() + " : " + userRoles.getRole().getRoleName());
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}		
	}
	
	@Override
	@Transactional
	public UserRoles getUserRolewithuserRoleIdUserId(int userId, int userRoleId) {
		log.debug("Get UserRoles Mapping by UserId and UserRoleId");		
		UserRoles userRoles = new UserRoles();
		List<UserRoles> userRolesList = new ArrayList<UserRoles>();
		try {
		
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoles.class, "userRoles");
			c.createAlias("userRoles.userList", "user");
			c.createAlias("userRoles.role", "roles");
			
			c.add(Restrictions.eq("user.userId",  userId));
			c.add(Restrictions.eq("roles.userRoleId",  userRoleId));
			userRolesList = c.list();	
			
			if (userRolesList == null) {
				log.debug("userRolesList with specified ids  : " + userId+"-- "+userRoleId+" not found");
			}			
			userRoles=(userRolesList!=null && userRolesList.size()!=0)?(UserRoles)userRolesList.get(0):null;			
			
			log.debug("Got  UserRoles Mapping by UserId and UserRoleId");
		} catch (RuntimeException re) {
			log.error("Failed to get UserRoles Mapping by UserId and UserRoleId", re);
		}
		return userRoles;
	}
	
	@Override
	@Transactional
	public List<UserRoles> listUserRoles(int startIndex, int pageSize,Integer userId) {
		log.debug("listing all UserRoles instance");
		List<UserRoles> userRolesList=null;
		List<UserRoles> userRolesListFinal=new ArrayList<UserRoles>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoles.class, "userRoles");			
			c.add(Restrictions.eq("userRoles.status",  1));
			c.add(Restrictions.eq("userRoles.userList.userId",  userId));
			
			if(startIndex!=-1 && pageSize!=-1){
				c.setFirstResult(startIndex);
				c.setMaxResults(pageSize);
			}
			userRolesList = c.list();					
			
			if (!(userRolesList == null || userRolesList.isEmpty())){
				for (UserRoles userRoles : userRolesList) {
					if(userRoles.getToDate()!=null){
						long dateDiff=DateUtility.DateDifference(DateUtility.dateToStringInSecond(userRoles.getToDate()), DateUtility.dateToStringInSecond(DateUtility.getCurrentTime()));
						
						if(dateDiff==0){
							userRoles.setStatus(0);
							updateUserRoles(userRoles);
						}
					}
					userRolesListFinal.add(userRoles);
					Hibernate.initialize(userRoles.getUserList());
					Hibernate.initialize(userRoles.getRole());
					Hibernate.initialize(userRoles.getCreatedBy());
					
					
				}				
			}
			log.debug("UserRoles listing was successful");
		} catch (RuntimeException re) {
			log.error("UserRoles listing failed", re);
		}
		return userRolesListFinal;
	}	
	
	@Override
	@Transactional
	public List<ProductMaster> listbyCustomerIdTestFactoryId(int testFactoryId, int customerId, int startIndex, int pageSize) {
		List<ProductMaster> productMaster=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");			
			c.createAlias("product.customer", "cust");
			c.createAlias("product.testFactory", "testFac");
			if(customerId!=0){
			c.add(Restrictions.eq("cust.customerId", customerId));
			}
			c.add(Restrictions.eq("testFac.testFactoryId", testFactoryId));
			c.add(Restrictions.eq("product.status", 1));			
			c.setFirstResult(startIndex);
			c.setMaxResults(pageSize);
			
			productMaster= c.list();
			log.info("productMaster:: "+productMaster.size());
			if (!(productMaster == null || productMaster.isEmpty())){
				for (ProductMaster product : productMaster) {					
					Hibernate.initialize(product.getTestFactory());
					Hibernate.initialize(product.getCustomer());				
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productMaster;
	}



	@Override
	@Transactional
	public List<ProductMaster> getCustmersbyProductId(Integer testFactoryId,Integer productId) {
		List<ProductMaster> listOfProducts = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.testFactory", "testfactory");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("testfactory.testFactoryId", testFactoryId));
			if(productId != 0)
			{
				c.add(Restrictions.eq("product.productId", productId));
			}
			listOfProducts = c.list();
			log.debug("Result Set Size of products *** : " + listOfProducts.size());
			for (ProductMaster productMaster : listOfProducts) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getProductMode());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}

	@Override
	@Transactional
	public List<ProductMaster> getCustmersbyProductIdStatus(int testFactoryId,
			int productId, int status, int jtStartIndex, int jtPageSize) {
		List<ProductMaster> listOfProducts = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.testFactory", "testfactory");
			if(status != 	2){
				c.add(Restrictions.eq("product.status", status));	
			}			
			c.add(Restrictions.eq("testfactory.testFactoryId", testFactoryId));
			if(productId != 0)
			{
				c.add(Restrictions.eq("product.productId", productId));
			}
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				c.setFirstResult(jtStartIndex);
				c.setMaxResults(jtPageSize);
			}
			
			listOfProducts = c.list();
			log.debug("Result Set Size of products *** : " + listOfProducts.size());
			for (ProductMaster productMaster : listOfProducts) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getProductMode());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}


	@Override
	@Transactional
	public List<ProductMaster> getUserRoleBasedProductByTestFactoryId(int testFactoryId,
			int productId, int userId, int status, int jtStartIndex, int jtPageSize) {
		List<ProductMaster> listOfProducts = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.testFactory", "testfactory");
			c.createAlias("product.productUserRoles", "prodUserRoles");
			c.createAlias("prodUserRoles.user", "userList");
			if(productId != 0)
			{
				c.add(Restrictions.eq("product.productId", productId));
			}
			if(status != 2){
				c.add(Restrictions.eq("product.status", status));	
			}			
			c.add(Restrictions.eq("testfactory.testFactoryId", testFactoryId));
			c.add(Restrictions.eq("prodUserRoles.status", 1));
			if(userId != -1){
				c.add(Restrictions.eq("userList.userId", userId));	
			}			
			c.addOrder(Order.desc("product.productId"));
			
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				c.setFirstResult(jtStartIndex);
				c.setMaxResults(jtPageSize);
			}
			
			listOfProducts = c.list();
			log.debug("Result Set Size of products *** : " + listOfProducts.size());
			for (ProductMaster productMaster : listOfProducts) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getProductMode());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}
	
	@Override
	@Transactional
	public Integer getUsersProductCountByTestFactoryId(int testFactoryId,
			int productId, int userId, int status, int jtStartIndex, int jtPageSize, int userRoleId) {
		log.debug("listing getUsersProductCountByTestFactoryIds instance");
		int userProductCount = 0;
		String sql= "";

		try {
		if(userRoleId== IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
			sql="SELECT count(pm.productId) as ProductIdCount FROM product_master pm "
					+ "inner join test_factory tf on pm.testFactoryId= tf.testFactoryId "
					+ "inner join product_user_roles puroles on pm.productId = puroles.productId "
					+ "inner join user_list ul on puroles.userId = ul.userId "
					+ "where tf.testFactoryId=:tfId and puroles.status=1 and pm.status=1 ";
			
		}else{//For Admin and other User roles.
			sql="SELECT count(pm.productId) as ProductIdCount FROM product_master pm "
					+ "inner join test_factory tf on pm.testFactoryId= tf.testFactoryId "					
					+ "where tf.testFactoryId=:tfId and pm.status=1 ";
		}

		if(userId != -1){
			sql = sql + "and ul.userId=:usrId  order by ProductIdCount desc ";
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				userProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.setParameter("usrId", userId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				userProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.setParameter("usrId", userId)
						.uniqueResult()).intValue();					
			}
		}else{//For Admin and other User roles.
			sql = sql + "order by ProductIdCount desc ";
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				userProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				userProductCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("tfId", testFactoryId)
						.uniqueResult()).intValue();					
			}
		}
		
		} catch (RuntimeException re) {
			log.error("list getUsersProductCountByTestFactoryIds", re);
			// throw re;
		}
		return userProductCount;
	}
	
	
	@Override
	@Transactional
	public List<ResourceExperienceSummaryDTO> listResourceExperienceOfSelectedProduct(
			Integer productId, Integer productVersionId, Integer userId) {
		
		log.debug("listing ResourceExperienceSummaryDTO: ");
		List<ResourceExperienceSummaryDTO> listOfResourceExperienceSummaryDTO = new ArrayList<ResourceExperienceSummaryDTO>();
		log.debug("Getting Resource ExperienceSummary for Product Id : " + productId);
		if(userId == null){
			return null;
		}
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.tester", "testerUser");
			c.createAlias("wptcep.testLead", "testLeadUser");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			if(productId == 0){
				return null;
			}else if(productId == -1){
				// Get all the Work package TC Plans where user is associated with irrespective of Product
			}
			else if(productId != 0 && (productVersionId == 0 || productVersionId == -1)){
				c.add(Restrictions.eq("product.productId", productId));
			}else if(productVersionId != 0){
				c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			}
			c.add(Restrictions.eq("wptcep.isExecuted", 1));
			if(userId != 0){
			c.add(Restrictions.disjunction().add(
			        Restrictions.or(Restrictions.eq("tester.userId", userId),
			                		Restrictions.eq("testLead.userId", userId))));
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("testerUser.userId"));
			projectionList.add(Property.forName("testerUser.loginId"));
			projectionList.add(Property.forName("product.productId"));
			projectionList.add(Property.forName("product.productName"));
			projectionList.add(Property.forName("productVersion.productVersionListId"));
			projectionList.add(Property.forName("productVersion.productVersionName"));
			projectionList.add(Projections.countDistinct("workPackage.workPackageId"));
			projectionList.add(Projections.count("wptcep.id"));
			projectionList.add(Projections.groupProperty("productVersion.productVersionListId"));
			if(userId == 0){
				projectionList.add(Projections.groupProperty("testerUser.userId"));
			}
			c.setProjection(projectionList).addOrder(Order.asc("product.productName"));
			
			List<Object[]> list = c.list();
			log.debug("Result Set Size : " + list.size());
			ResourceExperienceSummaryDTO resourceExperienceSummaryDTO = null;
			for (Object[] row : list) {
				resourceExperienceSummaryDTO = new ResourceExperienceSummaryDTO();
				resourceExperienceSummaryDTO.setUserId((Integer)row[0]);
				resourceExperienceSummaryDTO.setUserLoginId((String)row[1]);
				resourceExperienceSummaryDTO.setProductId((Integer)row[2]);
				resourceExperienceSummaryDTO.setProductName((String)row[3]);
				resourceExperienceSummaryDTO.setProductVersionId((Integer)row[4]);
				resourceExperienceSummaryDTO.setProductVersionName((String)row[5]);
				resourceExperienceSummaryDTO.setWpCount(((Long)row[6]).intValue());
				resourceExperienceSummaryDTO.setExecutedTestCaseCount(((Long)row[7]).intValue());
				listOfResourceExperienceSummaryDTO.add(resourceExperienceSummaryDTO);
				log.debug("Status Summary for User Id : "  +(Integer)row[0] + "  User Name : "+(String)row[1]+"   Product Id : " + (Integer)row[2]+ " Product Name : " + (String)row[3] + "  WP Count : " + ((Long)row[6]).intValue()+ "  ExecutedTestCaseCount : " + ((Long)row[7]).intValue());
			}
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return listOfResourceExperienceSummaryDTO;
	}



	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWpTCExecutionOfUserforSelectedProduct(Integer productId, Integer productVersionId, Integer userId) {
		log.debug("listing ResourceExperienceSummaryDTO: ");
		if(userId == null){
			return null;
		}
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		log.info("Getting Resource ExperienceSummary for Product Id : " + productId);
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.tester", "testerUser");
			c.createAlias("wptcep.testLead", "testLeadUser");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			if(productId == 0){
				return null;
			}else if(productId == -1){
				// Get all the Work package TC Plans where user is associated with irrespective of Product
			}
			else if(productId != 0 && (productVersionId == 0 || productVersionId == -1)){
				c.add(Restrictions.eq("product.productId", productId));
			}else if(productVersionId != 0){
				c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			}
			c.add(Restrictions.eq("wptcep.isExecuted", 1));
			if(userId != 0){
				c.add(Restrictions.disjunction().add(
				        Restrictions.or(Restrictions.eq("tester.userId", userId),
				                		Restrictions.eq("testLead.userId", userId))));
			}
			listOfWorkPackageTestCaseExecutionPlan = c.list();
			log.info("Result Set Size : " + listOfWorkPackageTestCaseExecutionPlan.size());
			for (WorkPackageTestCaseExecutionPlan wpTcExecPlan : listOfWorkPackageTestCaseExecutionPlan) {
				Hibernate.initialize(wpTcExecPlan.getWorkPackage());
				Hibernate.initialize(wpTcExecPlan.getRunConfiguration());
				Hibernate.initialize(wpTcExecPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
				Hibernate.initialize(wpTcExecPlan.getTestCase());
				Hibernate.initialize(wpTcExecPlan.getTestCaseExecutionResult());
				Hibernate.initialize(wpTcExecPlan.getRunConfiguration());
				Hibernate.initialize(wpTcExecPlan.getTestSuiteList());
				Hibernate.initialize(wpTcExecPlan.getTester());
				Hibernate.initialize(wpTcExecPlan.getTestLead());
				Hibernate.initialize(wpTcExecPlan.getWorkPackage().getWorkPackageRunConfigSet());
			}
			
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return listOfWorkPackageTestCaseExecutionPlan;
	}



	@Override
	@Transactional
	public List<ProductType> listProductTyper() {
		List<ProductType> productTypes = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductType.class, "productType");
			productTypes = c.list();		
			for(ProductType productType : productTypes){
				if(productType.getTestToolMaster() != null)
					Hibernate.initialize(productType.getTestToolMaster());
			}
		} catch (Exception e) {
			log.error("Getting Products Type ", e);
		}
		return productTypes;
	}

	@Override
	@Transactional
	public ProductType getProductTypeById(Integer productTypeId) {
		log.debug("getting getProductType instance by id");
		ProductType productType=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductType p where productTypeId=:productTypeId").setParameter("productTypeId", productTypeId)
					.list();
			productType=(list!=null && list.size()!=0)?(ProductType)list.get(0):null;
				
			log.debug("getProductTypeById successful");
		} catch (RuntimeException re) {
			log.error("getProductTypeById failed", re);
		}
		return productType;
        
	}


	@Override
	@Transactional
	public List<DeviceType> getDeviceType() {
		log.debug("getting getProductType instance by id");
		List<DeviceType> deviceType = null;
		try {
			deviceType =  sessionFactory.getCurrentSession().createQuery("from DeviceType").list();
			log.debug("getProductTypeById successful");
		} catch (RuntimeException re) {
			log.error("getProductTypeById failed", re);
		}
		return deviceType;
	}



	@Override
	@Transactional
	public List<DeviceLab> listDeviceLab() {
		log.debug("getting getProductType instance by id");
		List<DeviceLab> deviceLab = null;
		try {
			deviceLab =  sessionFactory.getCurrentSession().createQuery("from DeviceLab").list();
			log.debug("getProductTypeById successful");
		} catch (RuntimeException re) {
			log.error("getProductTypeById failed", re);
		}
		return deviceLab;
	}



	@Override
	@Transactional
	public Integer addTestRunplan(TestRunPlan testRunPlan) {
		log.debug("adding TestRunPlan instance");
		try {
			testRunPlan.setStatus(1);
			testRunPlan.setCreatedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(testRunPlan);
			log.debug("add successful"+testRunPlan.getTestRunPlanId());
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return testRunPlan.getTestRunPlanId();
	}
	
	
	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductVersionId(Integer productVersionId) {
		log.debug("listing all TestRunPlan instance");
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.createAlias("trplan.productVersionListMaster", "productVersion");
			
			c.add(Restrictions.eq("trplan.status", 1));
			if(productVersionId!=null){
			c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			}
			testRunPlanList = c.list();
			for (TestRunPlan testRunPlan : testRunPlanList) {
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				if(testRunPlan.getProductVersionListMaster().getProductMaster() != null){
					Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());	
				}
				Hibernate.initialize(testRunPlan.getTestToolMaster());
				if(testRunPlan.getObjectRepository() != null)
					Hibernate.initialize(testRunPlan.getObjectRepository());
				if(testRunPlan.getTestData() != null)
				Hibernate.initialize(testRunPlan.getTestData());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlanList;
	}
	
	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanBytestFactorProductorVersion(Integer productVersionId, Integer productId,Integer testFactoryId) {
		log.debug("listing all TestRunPlan instance of testFactorProductorVersion");
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.createAlias("trplan.productVersionListMaster", "productVersion");
			if(productVersionId!=-1){
				c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			}
			if(productId != -1){
				c.createAlias("productVersion.productMaster", "prod");
				c.add(Restrictions.eq("prod.productId", productId));
			}
			if(testFactoryId != -1){
				c.createAlias("productVersion.productMaster", "prod");
				c.createAlias("prod.testFactory", "tfact");
				c.add(Restrictions.eq("tfact.testFactoryId", testFactoryId));
			}
			
			c.add(Restrictions.eq("trplan.status", 1));
			
			testRunPlanList = c.list();
			for (TestRunPlan testRunPlan : testRunPlanList) {
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				if(testRunPlan.getProductVersionListMaster().getProductMaster() != null){
					Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());
				}				
				Hibernate.initialize(testRunPlan.getTestToolMaster());
				
				if(testRunPlan.getTestSuiteLists().size()>0){
					for(TestSuiteList testSuite : testRunPlan.getTestSuiteLists()){
						Hibernate.initialize(testSuite);
						if(testSuite.getTestCaseLists()!=null){
							Hibernate.initialize(testSuite.getTestCaseLists());
						}
					}
				}
				
				Set<RunConfiguration> runConfigurationSet= testRunPlan.getRunConfigurationList();
				Hibernate.initialize(runConfigurationSet);
				for(RunConfiguration runConfig:runConfigurationSet){
					Hibernate.initialize(runConfig);
					if(runConfig.getTestSuiteLists().size()>0){
						for(TestSuiteList testSuite : runConfig.getTestSuiteLists()){
							Hibernate.initialize(testSuite);
							if(testSuite.getTestCaseLists()!=null){
								Hibernate.initialize(testSuite.getTestCaseLists());
							}
						}
					}
				}
				
				Hibernate.initialize(testRunPlan.getProductBuild());
			}
			log.debug("list all successful of testFactorProductorVersion");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlanList;
	}
	
	@Override
	@Transactional
	public TestRunPlan getTestRunPlanById(Integer testRunPlanId) {
		log.debug("getting TestRunPlan instance by id");
		TestRunPlan testRunPlan=null;
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.add(Restrictions.eq("trplan.testRunPlanId", testRunPlanId));
			
			c.add(Restrictions.eq("trplan.status", 1));
			testRunPlanList = c.list();
			testRunPlan=(testRunPlanList!=null && testRunPlanList.size()!=0)?(TestRunPlan)testRunPlanList.get(0):null;
			if(testRunPlan!=null){
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				if(testRunPlan.getProductVersionListMaster().getProductMaster() != null){
					Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());
				}	
				if(testRunPlan.getTestSuiteLists() != null && !testRunPlan.getTestSuiteLists().isEmpty()){
					Hibernate.initialize(testRunPlan.getTestSuiteLists());
					for(TestSuiteList testSuite : testRunPlan.getTestSuiteLists()){
						Hibernate.initialize(testSuite);
						if(testSuite.getTestCaseLists()!=null){
							Hibernate.initialize(testSuite.getTestCaseLists());
						}
					}
				}			
				
				Set<RunConfiguration> runConfigurationSet= testRunPlan.getRunConfigurationList();
				Hibernate.initialize(runConfigurationSet);
				for(RunConfiguration runConfig:runConfigurationSet){
					Hibernate.initialize(runConfig);
					if(runConfig.getTestSuiteLists() != null ){
						Hibernate.initialize(runConfig.getTestSuiteLists());
						for(TestSuiteList tsl : runConfig.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
				}
				
				Hibernate.initialize(testRunPlan.getAttachments());
				if( testRunPlan.getAttachments() != null &&  testRunPlan.getAttachments().size()>0){
					Set<Attachment> attachmentSet= testRunPlan.getAttachments();
					Hibernate.initialize(attachmentSet);
					for(Attachment attach: attachmentSet){
						Hibernate.initialize(attach);
					}
				}				
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlan;
        
	}
	
	@Override
	@Transactional
	public TestRunPlan getTestRunPlanBytestRunPlanId(Integer testRunPlanId) {
		log.info("getting TestRunPlan instance by id");
		TestRunPlan testRunPlan=null;
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.createAlias("trplan.productVersionListMaster", "productVersion");
			c.add(Restrictions.eq("trplan.testRunPlanId", testRunPlanId));
			c.add(Restrictions.eq("trplan.status", 1));
			testRunPlanList = c.list();
			testRunPlan=(testRunPlanList!=null && testRunPlanList.size()!=0)?(TestRunPlan)testRunPlanList.get(0):null;
			if(testRunPlan!=null){				
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getTestToolMaster());
				Hibernate.initialize(testRunPlan.getScriptTypeMaster());

				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getProductMode());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getHostLists());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getGenericeDevices());
				if(testRunPlan.getProductBuild() != null){
					Hibernate.initialize(testRunPlan.getProductBuild());
					if(testRunPlan.getProductBuild().getProductBuildId() != null)
						Hibernate.initialize(testRunPlan.getProductBuild().getProductBuildId());
				}
				if(testRunPlan.getRunConfigurationList() != null && !testRunPlan.getRunConfigurationList().isEmpty())
					Hibernate.initialize(testRunPlan.getRunConfigurationList());
				Set<RunConfiguration> runconfigSet = testRunPlan.getRunConfigurationList();
				Hibernate.initialize(runconfigSet);
					
				for (RunConfiguration runConfiguration : runconfigSet) {
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getHostList());
					if( runConfiguration.getGenericDevice()!=null){
						Hibernate.initialize(runConfiguration.getGenericDevice().getHostList());
						GenericDevices devices = runConfiguration.getGenericDevice();
						Hibernate.initialize(devices);
						Hibernate.initialize(devices.getDeviceLab());
						Hibernate.initialize(devices.getDeviceModelMaster());
						Hibernate.initialize(devices.getPlatformType());
						Hibernate.initialize(devices.getProductMaster());
						Hibernate.initialize(devices.getTestFactory());
						
						if((devices instanceof MobileType) && ((MobileType) devices).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) devices).getDeviceMakeMaster());
						}
						if((devices instanceof ServerType) ){
							if(((ServerType) devices).getProcessor() != null){
								Hibernate.initialize(((ServerType) devices).getProcessor());	
							}
							if(((ServerType) devices).getSystemType() != null){
								Hibernate.initialize(((ServerType) devices).getSystemType());	
							}						
						}
					}
					Hibernate.initialize(runConfiguration.getProduct());
					Hibernate.initialize(runConfiguration.getProductVersion());
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					if(runConfiguration.getTestSuiteLists() != null ){
						Hibernate.initialize(runConfiguration.getTestSuiteLists());
						for(TestSuiteList tsl : runConfiguration.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
					Hibernate.initialize(runConfiguration.getTestScriptFileLocation());
					Hibernate.initialize(runConfiguration.getScriptTypeMaster());
					Hibernate.initialize(runConfiguration.getTestTool());
					if(runConfiguration.getEnvironmentcombination() != null)
						Hibernate.initialize(runConfiguration.getEnvironmentcombination());					
				}
				Hibernate.initialize(testRunPlan.getAttachments());		
				if( testRunPlan.getAttachments() != null &&  testRunPlan.getAttachments().size()>0){
					Set<Attachment> attachmentSet= testRunPlan.getAttachments();
					Hibernate.initialize(attachmentSet);
					for(Attachment attach: attachmentSet){
						Hibernate.initialize(attach);
					}
				}
				Hibernate.initialize(testRunPlan.getTestSuiteLists());		
				Hibernate.initialize(testRunPlan.getFeatureList());	
				Set<ProductFeature> productfeatureList = testRunPlan.getFeatureList();
				if(productfeatureList.size()!=0){
					for(ProductFeature pf : productfeatureList){
						if(pf!=null){
							Hibernate.initialize(pf.getProductFeatureName());
							Hibernate.initialize(pf.getProductFeatureCode());
							Hibernate.initialize(pf.getProductFeatureDescription());
							Hibernate.initialize(pf.getDisplayName());
							Hibernate.initialize(pf.getParentFeature());
						}
					}
				}
				Hibernate.initialize(testRunPlan.getTestCaseList());		
				Set<TestCaseList> testcaseList1=testRunPlan.getTestCaseList();
				if(testcaseList1.size()!=0){
					for (TestCaseList tcl : testcaseList1) {
						if(tcl!=null){
							Hibernate.initialize(tcl);
							if(tcl.getProductFeature()!=null){
								Hibernate.initialize(tcl.getProductFeature());
							}
							if(tcl.getDecouplingCategory()!=null){
								Hibernate.initialize(tcl.getDecouplingCategory());
							}
							
							
						}
						
						
					}
				}
				
				Set<TestSuiteList> testSuiteSet = testRunPlan.getTestSuiteLists();
				Hibernate.initialize(testSuiteSet);		
				for (TestSuiteList testSuiteList : testSuiteSet) {
					Hibernate.initialize(testSuiteList.getExecutionTypeMaster());
					Hibernate.initialize(testSuiteList.getProductMaster());
					Hibernate.initialize(testSuiteList.getProductVersionListMaster());
					Hibernate.initialize(testSuiteList.getScriptTypeMaster());
					Set<TestCaseList> testcaseList = testSuiteList.getTestCaseLists();
					Hibernate.initialize(testcaseList);
					for (TestCaseList testCaseListObj : testcaseList) {
						Hibernate.initialize(testCaseListObj.getTestCaseStepsLists());
						if(testCaseListObj.getProductFeature()!=null){
							Hibernate.initialize(testCaseListObj.getProductFeature());
						}
						
					}
					
					
					Hibernate.initialize(testSuiteList.getTestExecutionResults());
					Hibernate.initialize(testSuiteList.getTestRunPlanList());
					Hibernate.initialize(testRunPlan.getTestCaseList());
					Set<TestCaseList> testcaseSet=testRunPlan.getTestCaseList();
					for (TestCaseList tcl : testcaseSet) {
						Hibernate.initialize(tcl.getProductFeature());
						Hibernate.initialize(tcl.getDecouplingCategory());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
					Set<ProductFeature> featureSet=testRunPlan.getFeatureList();
					for (ProductFeature productFeature : featureSet) {
						Hibernate.initialize(productFeature.getTestCaseList());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
				}
						
			}
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlan;
        
	}
	
	@Override
	@Transactional
	public void mapTestRunPlanWithTestRunconfiguration(Integer testRunPlanId,Integer runConfigurationId,String action) {
		log.debug("mapWorkpackageWithTestCase");
				try {
					
					TestRunPlan tsetRunPlan=getTestRunPlanById(testRunPlanId);
					RunConfiguration runConfiguration=environmentDAO.getRunConfiguration(runConfigurationId);
					if (tsetRunPlan != null && tsetRunPlan != null) {
						boolean needToUpdateOrAdd = false;

						Set<RunConfiguration> runcfgSet = tsetRunPlan.getRunConfigurationList();
						if (action.equalsIgnoreCase("Add")) {
							if (runcfgSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								log.info("size > 0");
								RunConfiguration runcfgForProcessing = runcfgSet
										.iterator().next();
								if (runcfgForProcessing != null) {
									log.info("envForProcessing not null");
									int alreadyAvailableRuncfgId = runcfgForProcessing.getRunconfigId().intValue();

									if (alreadyAvailableRuncfgId != runConfigurationId) {
										RunConfiguration	runCfgAvailable=environmentDAO.getRunConfiguration(alreadyAvailableRuncfgId);
										for (TestRunPlan testRunAvailable : runCfgAvailable.getTestRunPlanSet()) {
											log.debug("testRunAvailable.getTestRunPlanId().intValue()"
													+testRunAvailable.getTestRunPlanId().intValue());

											if (testRunAvailable.getTestRunPlanId().intValue() == testRunPlanId) {
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																runCfgAvailable);
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								tsetRunPlan.getRunConfigurationList().add(runConfiguration);
								runConfiguration.getTestRunPlanSet().add(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(runConfiguration);
							}
						}
						else if(action.equalsIgnoreCase("Remove")){

							log.debug("Remove tsetRunPlan from runConfigurationSet ");

							try {

								Set<RunConfiguration> runConfigurationSet = tsetRunPlan.getRunConfigurationList();
								runConfigurationSet.remove(runConfiguration);
								
								tsetRunPlan.setRunConfigurationList(runConfigurationSet);
								
								sessionFactory.getCurrentSession().save(tsetRunPlan);
								
								log.debug("Removed tsetRunPlan from  runConfigurationSet successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove tsetRunPlan from  runConfigurationSet", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	}
	
	
	@Override
	@Transactional
	public void mapTestRunPlanWithTestSuite(Integer testRunPlanId,Integer testSuiteId,String action) {
				log.info("mapWorkpackageWithTestCase");
				try {
					
					log.debug("testRunPlanId--->"+testRunPlanId);
					log.debug("testSuiteId--->"+testSuiteId);
					TestRunPlan tsetRunPlan=getTestRunPlanById(testRunPlanId);
					TestSuiteList testSuiteList=testSuiteListDAO.getByTestSuiteId(testSuiteId);
					if (tsetRunPlan != null && tsetRunPlan != null) {
						boolean needToUpdateOrAdd = false;

						Set<TestSuiteList> tcSuiteSet = tsetRunPlan.getTestSuiteLists();
						log.debug("inside action>>"+action);		
						if (action.equalsIgnoreCase("Add")) {
							log.debug("inside add");
							if (tcSuiteSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								log.info("size > 0");
								TestSuiteList tcSuiteForProcessing = tcSuiteSet
										.iterator().next();
								if (tcSuiteForProcessing != null) {
									log.debug("tcSuiteForProcessing not null");
									int alreadyAvailabletcSuiteId = tcSuiteForProcessing.getTestSuiteId().intValue();

									if (alreadyAvailabletcSuiteId != testSuiteId) {
										log.debug("alreadyAvailabletcSuiteId---------->"+ alreadyAvailabletcSuiteId);
										log.debug("testSuiteId---------->"+ testSuiteId);
										log.info("values or different");
										log.debug("envcombination.getEnvironmentSet().size()="
												+ tsetRunPlan.getRunConfigurationList().size());
										TestSuiteList testSuiteListAvailable=testSuiteListDAO.getByTestSuiteId(testSuiteId);
										for (TestRunPlan testRunAvailable : testSuiteListAvailable.getTestRunPlanList()) {
											log.debug("testRunAvailable.getTestRunPlanId().intValue()"
													+testRunAvailable.getTestRunPlanId().intValue());

											if (testRunAvailable.getTestRunPlanId().intValue() == testRunPlanId) {
												log.debug("testRunAvailable found ");
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																testSuiteListAvailable);
												log.debug("testSuiteListAvailable.getTestRunPlanList().size()="+testSuiteListAvailable.getTestRunPlanList().size());
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								tsetRunPlan.getTestSuiteLists().add(testSuiteList);
								testSuiteList.getTestRunPlanList().add(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(testSuiteList);
							}
						}
						else if(action.equalsIgnoreCase("Remove")){

							log.debug("Remove tsetRunPlan from runConfigurationSet ");

							try {

								tsetRunPlan.getTestSuiteLists().remove(testSuiteList);
								testSuiteList.getTestRunPlanList().remove(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(tsetRunPlan);
								sessionFactory.getCurrentSession().saveOrUpdate(testSuiteList);
								
								log.debug("Removed tsetRunPlan from  runConfigurationSet successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove tsetRunPlan from  runConfigurationSet", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	}

	@Override
	@Transactional
	public ProductMaster productShowHideTab(Integer testFactoryId, Integer productId) {
		log.debug("Getting Product Core resource Tab to show by product id");
		ProductMaster getproductShowHideTab=null;
		List<ProductMaster> getproductShowHideTabList = new ArrayList<ProductMaster>();
		try {
		
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "prod");
			c.createAlias("prod.testFactory", "test");
			if (testFactoryId != 0){
				c.add(Restrictions.eq("test.testFactoryId",  testFactoryId));
			}
			
			c.add(Restrictions.eq("prod.productId",  productId));
			c.add(Restrictions.eq("prod.productMode.modeId",  1));
			
			getproductShowHideTabList = c.list();	
			
			if (getproductShowHideTabList == null) {
				log.debug("Product Core resource Tab with testfactory ID  : " + testFactoryId +" and Product Id -- "+productId+" not found");
			}			
			getproductShowHideTab=(getproductShowHideTabList!=null && getproductShowHideTabList.size()!=0)?(ProductMaster)getproductShowHideTabList.get(0):null;			
			
			log.info("Got Product Core resource Tab");
		} catch (RuntimeException re) {
			log.error("Failed to get Product Core resource Tab", re);
		}
		
		return getproductShowHideTab;
	}



	@Override
	@Transactional
	public boolean validateUserRole(UserRoles ur, JsonUserRoles jsonUserRoles) {
		List<UserRoles> userRoles= new ArrayList<UserRoles>();
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoles.class,"ur");
		c.add(Restrictions.between("ur.fromDate", DateUtility.dateformatWithOutTime(jsonUserRoles.getFromDate()),  DateUtility.dateformatWithOutTime(jsonUserRoles.getToDate())));
		c.add(Restrictions.between("ur.toDate", DateUtility.dateformatWithOutTime(jsonUserRoles.getFromDate()),  DateUtility.dateformatWithOutTime(jsonUserRoles.getToDate())));
		
		c.add(Restrictions.eq("ur.mutliRoleId", ur.getMutliRoleId()));
		
		userRoles=c.list();
		
		if(userRoles!=null && !userRoles.isEmpty()){
			return true;	
		}
			
		return false;
	}



	@Override
	@Transactional
	public HostList getHostListById(Integer hostListId) {
		log.debug("getting HostList instance by id");
		HostList hostList=null;
		List<HostList> hostLists=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(HostList.class, "hl");
			c.add(Restrictions.eq("hl.hostId", hostListId));
			
			hostLists = c.list();
			hostList=(hostLists!=null && hostLists.size()!=0)?(HostList)hostLists.get(0):null;
			if(hostList!=null){
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return hostList;
        
	}



	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId) {
		log.debug("listing all RunConfiguration instance");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();


		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.testRunPlan", "tp");
			c.createAlias("rc.environmentcombination", "ec");

			c.add(Restrictions.eq("ec.environment_combination_id", environmentCombinationId));
			
			c.add(Restrictions.eq("tp.testRunPlanId", testRunPlanId));
			c.add(Restrictions.eq("rc.runConfigStatus",1));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
					
					if(rc.getTestSuiteLists() != null){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
					
					if(type==2){
						if(rc.getGenericDevice()!=null && rc.getGenericDevice().getGenericsDevicesId()!=null){
							runConfigurationListFinal.add(rc);
						}else{
							runConfigurationListFinal.add(rc);
						}
					}else{
						
						runConfigurationListFinal.add(rc);
					}
				}
				
				runConfigurations.addAll(runConfigurationListFinal);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurations;

	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type) {
		log.debug("listing all RunConfiguration instance");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.testRunPlan", "tp");
						
			c.add(Restrictions.eq("tp.testRunPlanId", testRunPlanId));
			c.add(Restrictions.eq("rc.runConfigStatus", 1));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
					
					if(rc.getTestSuiteLists() != null){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
					
					if(type==2){
						if(rc.getGenericDevice()!=null && rc.getGenericDevice().getGenericsDevicesId()!=null){
							runConfigurationListFinal.add(rc);

						}else{
							runConfigurationListFinal.add(rc);
						}
					}else{
						
						runConfigurationListFinal.add(rc);
					}
					
					
				}
				
				runConfigurations.addAll(runConfigurationListFinal);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurations;

	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListByStatus(Integer testRunPlanId,Integer type,Integer status) {
		log.debug("listing all RunConfiguration instance");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.testRunPlan", "tp");
						
			c.add(Restrictions.eq("tp.testRunPlanId", testRunPlanId));
			c.add(Restrictions.eq("rc.runConfigStatus", 1));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
					
					if(rc.getTestSuiteLists() != null){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
					
					if(type==2){
						if(rc.getGenericDevice()!=null && rc.getGenericDevice().getGenericsDevicesId()!=null){
							runConfigurationListFinal.add(rc);

						}else{
							runConfigurationListFinal.add(rc);
						}
					}else{
						
						runConfigurationListFinal.add(rc);
					}
				}
				
				runConfigurations.addAll(runConfigurationListFinal);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurations;

	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListByWP(
			Integer workpackageId, Integer type,Integer environmentCombinationId) {
		// TODO Auto-generated method stub
		log.debug("listing all RunConfiguration instance");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();


		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.workPackage", "wp");
			c.createAlias("rc.environmentcombination", "ec");

			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			c.add(Restrictions.eq("ec.environment_combination_id", environmentCombinationId));
			c.add(Restrictions.eq("rc.runConfigStatus", 1));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				log.info("runConfigurationList>>"+runConfigurationList.size());
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					if(rc.getTestRunPlan()!=null){
						if(rc.getTestRunPlan().getProductVersionListMaster()!=null){
							Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
							if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
								Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
								if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
									Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
								}
							}
						}
					}
					
					if(type==2){
						if(rc.getGenericDevice()!=null && rc.getGenericDevice().getGenericsDevicesId()!=null){
							runConfigurationListFinal.add(rc);
						}else{
							runConfigurationListFinal.add(rc);
						}
					}else{
						
						runConfigurationListFinal.add(rc);
					}
				}
				
				runConfigurations.addAll(runConfigurationListFinal);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurations;
	}





	@Override
	public List<UserRoleMaster> getProductUserRoles(Integer typeFilter) {
		List<UserRoleMaster> userRoleMaster=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoleMaster.class, "userRole");
			if (typeFilter == 1){
				c.add(Restrictions.in("userRole.userRoleId", Arrays.asList(2, 3, 4, 5, 6, 8,9)));
			}
			if (typeFilter == 2){
				c.add(Restrictions.in("userRole.userRoleId", Arrays.asList(2, 3, 4, 5)));
			}
			userRoleMaster = c.list();
			
			log.debug("getuserRoleMaster successful");
		} catch (RuntimeException re) {
			log.error("getuserRoleMaster failed", re);
		}
		return userRoleMaster;
	}



	@Override
	@Transactional
	public ProductMode getProductModeById(Integer productModeId) {
				log.debug("getting getProductModeById instance by id");
				ProductMode productMode = null;
				try {
					List list =  sessionFactory.getCurrentSession().createQuery("from ProductMode pm where modeId=:productModeId").setParameter("productModeId", productModeId)
							.list();
					productMode=(list!=null && list.size()!=0)?(ProductMode)list.get(0):null;
						
					log.debug("getProductTypeById successful");
				} catch (RuntimeException re) {
					log.error("getProductTypeById failed", re);
					//throw re;
				}
				return productMode;
	}



	@Override
	@Transactional
	public List<ProductMaster> listProductsByTestFactoryIdAndProductMode(int testFactoryId, int productModeId) {
		log.debug("listing all ProductMaster instance");
		List<ProductMaster> listOfProductMaster=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "pm");
			c.createAlias("pm.productMode", "pMode");
			c.createAlias("pm.testFactory", "tf");
			c.add(Restrictions.eq("pm.status", 1));
			if (testFactoryId != 0){
				c.add(Restrictions.eq("pm.status", 1));
			}
			if(productModeId == 1){
				c.add(Restrictions.eq("pMode.modeId", 1));
			}
			if(productModeId == 2){
				c.add(Restrictions.eq("pMode.modeId", 2));
			}
			listOfProductMaster = c.list();
			for (ProductMaster productMaster : listOfProductMaster) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getTestSuiteLists());
				Hibernate.initialize(productMaster.getProductMode());
			}
			log.debug("getuserRoleMaster successful");
		} catch (RuntimeException re) {
			log.error("getuserRoleMaster failed", re);
		}
		return listOfProductMaster;
	}



	@Override
	@Transactional
	public void mapMobileWithProduct(Integer productId, Integer deviceId,
			String action) {
		log.debug("mapMobileWithProduct");
		ProductMaster product = null;
		GenericDevices genericDevice = null;

		try {
			product = getByProductId(productId);
			genericDevice = environmentDAO.getGenericDevices(deviceId);

			if (product != null && genericDevice != null) {
				boolean needToUpdateOrAdd = false;

				Set<GenericDevices> genericDeviceSet = product.getGenericeDevices();
				if (action.equalsIgnoreCase("map")) {

					if (genericDeviceSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						log.info("size > 0");
						GenericDevices deviceForProcessing = genericDeviceSet
								.iterator().next();
						if (deviceForProcessing != null) {
							log.info("deviceForProcessing not null");
							int alreadyAvailableDeviceId = deviceForProcessing
									.getGenericsDevicesId().intValue();

							if (alreadyAvailableDeviceId != deviceId) {
								log.debug("alreadyAvailableDeviceId---------->"
												+ alreadyAvailableDeviceId);
								log.debug("deviceId---------->"
										+ deviceId);
								log.info("values or different");
								log.debug("workPackage.getGenericeDevices().size()="
										+ product.getGenericeDevices()
												.size());

								GenericDevices deviceAvailable = environmentDAO.getGenericDevices(alreadyAvailableDeviceId) ;
								for (ProductMaster p : deviceAvailable.getProductMasterSet()) {
									log.debug("p.getProductId().intValue()"
											+ p.getProductId().intValue());

									if (p.getProductId().intValue() == productId) {
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														deviceAvailable);
										log.debug("deviceAvailable.getProductMaster().size()="
												+ deviceAvailable
														.getProductMasterSet().size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						product.getGenericeDevices().add(genericDevice);
						genericDevice.getProductMasterSet().add(product);
						sessionFactory.getCurrentSession().saveOrUpdate(
								product);
						sessionFactory.getCurrentSession().saveOrUpdate(
								genericDevice);
					}
				}
				else if(action.equalsIgnoreCase("unmap")){
					try {
						log.debug("deviceId input: "  +deviceId);
						product = (ProductMaster) sessionFactory.getCurrentSession().get(ProductMaster.class, productId);
						if (product == null) {
							log.debug("product with specified id not found : " + productId);
						}
						genericDevice = (GenericDevices) sessionFactory.getCurrentSession().get(GenericDevices.class, deviceId);
						if (genericDevice == null) {
							log.debug("device could not found in the database : " + deviceId);
						}
						Set<GenericDevices> genericDevices = product.getGenericeDevices();
						log.debug("locale set size before removing :"+ genericDevices.size());
						genericDevices.remove(genericDevice);
						log.debug("locale set size  after removing ::"+ genericDevices.size());
						
						product.setGenericeDevices(genericDevices);
						
						sessionFactory.getCurrentSession().save(product);
						
						log.debug("Removed locale from workpackage successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove locale from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			// throw re;
		}
	
	}

	
	@Override
	@Transactional
	public ProductMaster getCustomerByProductId(int productId) {

		
		ProductMaster customer=null;
		List<ProductMaster> customerList = new ArrayList<ProductMaster>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.add(Restrictions.eq("product.productId", productId));
			
			customerList = c.list();
			
			log.debug("customerList-==="+customerList);
//			log.info("customerList  size-==="+customerList.size());
			customer=(customerList!=null && customerList.size()!=0)?customerList.get(0):null;
			log.debug("customer-==="+customer);
			
			log.debug("getByCustomer successful");
		} catch (RuntimeException re) {
			log.error("getByCustomer failed", re);
		}
		return customer;
	}


	@Override
	@Transactional
	public void mapServerWithProduct(Integer productId, Integer hostId,
			String action) {

		log.info("mapServerWithProduct");

		ProductMaster product = null;
		HostList host = null;

		try {
			product = getByProductId(productId);
			host = hostListDAO.getByHostId(hostId);

			if (product != null && host != null) {
				boolean needToUpdateOrAdd = false;

				Set<HostList> hostListSet = product.getHostLists();
				if (action.equalsIgnoreCase("map")) {

					if (hostListSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						log.info("size > 0");
						HostList hostForProcessing = hostListSet
								.iterator().next();
						if (hostForProcessing != null) {
							log.info("deviceForProcessing not null");
							int alreadyAvailableHostId = hostForProcessing
									.getHostId().intValue();

							if (alreadyAvailableHostId != hostId) {
								log.debug("alreadyAvailableHostId---------->"
												+ alreadyAvailableHostId);
								log.debug("hostId---------->"
										+ hostId);
								log.info("values or different");
								log.debug("workPackage.getHostLists().size()="
										+ product.getHostLists()
												.size());

								HostList hostAvailable = hostListDAO.getByHostId(alreadyAvailableHostId);
								for (ProductMaster p : hostAvailable.getProductMasterSet()) {
									log.debug("p.getProductId().intValue()"
											+ p.getProductId().intValue());

									if (p.getProductId().intValue() == productId) {
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														hostAvailable);
										log.debug("deviceAvailable.getProductMaster().size()="
												+ hostAvailable
														.getProductMasterSet().size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						product.getHostLists().add(host);
						host.getProductMasterSet().add(product);
						sessionFactory.getCurrentSession().saveOrUpdate(
								product);
						sessionFactory.getCurrentSession().saveOrUpdate(
								host);
					}
				}
				else if(action.equalsIgnoreCase("unmap")){
					try {
						log.debug("deviceId input: "  +hostId);
						product = (ProductMaster) sessionFactory.getCurrentSession().get(ProductMaster.class, productId);
						if (product == null) {
							log.debug("product with specified id not found : " + productId);
						}
						host = (HostList) sessionFactory.getCurrentSession().get(HostList.class, hostId);
						if (host == null) {
							log.debug("device could not found in the database : " + hostId);
						}
						Set<HostList> hostList = product.getHostLists();
						log.debug("locale set size before removing :"+ hostList.size());
						hostList.remove(host);
						log.debug("locale set size  after removing ::"+ hostList.size());
						
						product.setHostLists(hostList);
						
						sessionFactory.getCurrentSession().save(product);
						
						log.debug("Removed locale from workpackage successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove locale from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
	
	
	}



	@Override
	@Transactional
	public void mapTestRunPlanWithTestCase(Integer testRunPlanId,
			Integer testCaseId, String action) {
		log.info("mapTestRunPlanWithTestCase");
		try {
			
			log.debug("testRunPlanId--->"+testRunPlanId);
			log.debug("testCaseId--->"+testCaseId);
			TestRunPlan tsetRunPlan=getTestRunPlanById(testRunPlanId);
			TestCaseList testCaseList=testCaseListDAO.getByTestCaseId(testCaseId);
			if (tsetRunPlan != null && tsetRunPlan != null) {
				boolean needToUpdateOrAdd = false;

				Set<TestCaseList> testCaseSet = tsetRunPlan.getTestCaseList();
				log.debug("inside action>>"+action);		
				if (action.equalsIgnoreCase("Add")) {
						log.info("inside add");
					if (testCaseSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						log.info("size > 0");
						TestCaseList tcForProcessing = testCaseSet
								.iterator().next();
						if (tcForProcessing != null) {
							log.info("tcSuiteForProcessing not null");
							int alreadyAvailabletcId = tcForProcessing.getTestCaseId().intValue();

							if (alreadyAvailabletcId != testCaseId) {
								log.debug("alreadyAvailabletcSuiteId---------->"+ alreadyAvailabletcId);
								log.debug("testCaseId---------->"+ testCaseId);
								log.info("values or different");
								
								TestCaseList testCaseListAvailable=testCaseListDAO.getByTestCaseId(testCaseId);
								for (TestRunPlan testRunAvailable : testCaseListAvailable.getTestRunPlanList()) {
									log.debug("testRunAvailable.getTestRunPlanId().intValue()"
											+testRunAvailable.getTestRunPlanId().intValue());

									if (testRunAvailable.getTestRunPlanId().intValue() == testRunPlanId) {
										log.debug("testRunAvailable found ");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														testCaseListAvailable);
										log.debug("testSuiteListAvailable.getTestRunPlanList().size()="+testCaseListAvailable.getTestRunPlanList().size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						tsetRunPlan.getTestCaseList().add(testCaseList);
						testCaseList.getTestRunPlanList().add(tsetRunPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(tsetRunPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){

					log.debug("Remove testcase from testrunplan ");

					try {

						Set<TestCaseList> testCaseListSet = tsetRunPlan.getTestCaseList();
						testCaseListSet.remove(testCaseList);
						
						tsetRunPlan.setTestCaseList(testCaseListSet);
						
						sessionFactory.getCurrentSession().save(tsetRunPlan);
						
						log.debug("Removed testcase from testrunplan successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove testcase from testrunplan", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
}



	@Override
	@Transactional
	public void mapTestRunPlanWithFeature(Integer testRunPlanId,
			Integer featureId, String action) {
		log.info("mapTestRunPlanWithFeature");
		try {
			
			log.info("testRunPlanId--->"+testRunPlanId);
			log.info("featureId--->"+featureId);
			TestRunPlan tsetRunPlan=getTestRunPlanById(testRunPlanId);
			ProductFeature feature=featureDAO.getByProductFeatureId(featureId);
			if (tsetRunPlan != null && tsetRunPlan != null) {
				boolean needToUpdateOrAdd = false;

				Set<ProductFeature> featureSet = tsetRunPlan.getFeatureList();
				log.info("inside action>>"+action);		
				if (action.equalsIgnoreCase("Add")) {
						log.info("inside add");
					if (featureSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						log.info("size > 0");
						ProductFeature featureForProcessing = featureSet
								.iterator().next();
						if (featureForProcessing != null) {
							log.info("tcSuiteForProcessing not null");
							int alreadyAvailablefeatureId = featureForProcessing.getProductFeatureId().intValue();

							if (alreadyAvailablefeatureId != featureId) {
								log.debug("alreadyAvailablefeatureId---------->"+ alreadyAvailablefeatureId);
								log.debug("featureId---------->"+ featureId);
								log.info("values or different");
								
								ProductFeature featureListAvailable=featureDAO.getByProductFeatureId(featureId);
								for (TestRunPlan testRunAvailable : featureListAvailable.getTestRunPlanList()) {
									log.info("testRunAvailable.getTestRunPlanId().intValue()"
											+testRunAvailable.getTestRunPlanId().intValue());

									if (testRunAvailable.getTestRunPlanId().intValue() == testRunPlanId) {
										log.info("testRunAvailable found ");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														featureListAvailable);
										log.info("featureListAvailable.getTestRunPlanList().size()="+featureListAvailable.getTestRunPlanList().size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						tsetRunPlan.getFeatureList().add(feature);
						feature.getTestRunPlanList().add(tsetRunPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(tsetRunPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(feature);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){

					log.debug("Remove testcase from testrunplan ");

					try {

						Set<ProductFeature> featureListSet = tsetRunPlan.getFeatureList();
						featureListSet.remove(feature);
						
						tsetRunPlan.setFeatureList(featureListSet);
						
						sessionFactory.getCurrentSession().save(tsetRunPlan);
						
						log.debug("Removed testcase from testrunplan successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove testcase from testrunplan", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
}


	@Override
	@Transactional
	public ProductSummaryDTO getProductSummary(Integer productId) {
		log.info("listing listWorkPackageTestCaseExecutionSummary");
		
		ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO();
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "prod");
		
		c.add(Restrictions.eq("prod.productId", productId));
		
		ProjectionList projectionList = Projections.projectionList();			

		Object [] row=null;
		List<ProductMaster> productList = c.list();
		ProductMaster productMaster = (productList != null && productList.size() != 0) ? (ProductMaster) productList.get(0) : null;
		
		if(productMaster!=null){
			productSummaryDTO.setProductId(productMaster.getProductId());
			productSummaryDTO.setProductName(productMaster.getProductName());
			productSummaryDTO.setDescription(productMaster.getProductDescription());
			productSummaryDTO.setProductMode(productMaster.getProductMode().getModeName());
			if(productMaster.getTestFactory() != null){
				Hibernate.initialize(productMaster.getTestFactory());
				productSummaryDTO.setEngagementId(productMaster.getTestFactory().getTestFactoryId());
				productSummaryDTO.setEngagementName(productMaster.getTestFactory().getTestFactoryName());
			}
			if(productMaster.getCustomer() != null){
				Hibernate.initialize(productMaster.getCustomer());
				productSummaryDTO.setCustomerId(productMaster.getCustomer().getCustomerId());
				productSummaryDTO.setCustomerName(productMaster.getCustomer().getCustomerName());
			}
			if(productMaster.getProductVersionListMasters() != null){
				Hibernate.initialize(productMaster.getProductVersionListMasters());
				Set<ProductVersionListMaster> setOfProductVersions = productMaster.getProductVersionListMasters();
				if(setOfProductVersions != null && !(setOfProductVersions.isEmpty())){
					productSummaryDTO.setpVersionCount(setOfProductVersions.size());
					int productBuilds = 0;
					for (ProductVersionListMaster productVersionListMaster : setOfProductVersions) {
						List<ProductBuild> listOfProductBuilds = productBuildDAO.listProductBuilds(productVersionListMaster.getProductVersionListId(), 1);
						if(listOfProductBuilds != null && !listOfProductBuilds.isEmpty()){
							if(productBuilds == 0){
								productBuilds = listOfProductBuilds.size();
							}else{
								productBuilds = productBuilds+listOfProductBuilds.size();
							}
						}
					}
					productSummaryDTO.setpBuildCount(productBuilds);
				}
			}
		}
		
			Criteria tCaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tCase");
			tCaseCriteria.createAlias("tCase.productMaster", "product");
			tCaseCriteria.add(Restrictions.eq("product.productId", productId));
			
			List<TestCaseList> listOfTestCase = tCaseCriteria.list();
			if(listOfTestCase != null && !(listOfTestCase.isEmpty())){
				productSummaryDTO.setTestCaseCount(listOfTestCase.size());
			}else{
				productSummaryDTO.setTestCaseCount(0);
			}
			
			
			Criteria tSuiteCriteria = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "tSuite");
			tSuiteCriteria.createAlias("tSuite.productMaster", "TSproduct");
			tSuiteCriteria.add(Restrictions.eq("TSproduct.productId", productId));
			
			List<TestSuiteList> listOfTestSuite = tSuiteCriteria.list();
			if(listOfTestSuite != null && !(listOfTestSuite.isEmpty())){
				productSummaryDTO.setTestSuiteCount(listOfTestSuite.size());
			}else{
				productSummaryDTO.setTestSuiteCount(0);
			}
			
			Criteria wpCriteria = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "workPackage");
			wpCriteria.createAlias("workPackage.productBuild", "pbuild");
			wpCriteria.createAlias("pbuild.productVersion", "pVersion");
			wpCriteria.createAlias("pVersion.productMaster", "WPproduct");
			wpCriteria.add(Restrictions.eq("workPackage.status", "1"));
			wpCriteria.add(Restrictions.eq("WPproduct.productId", productId));
			
			List<WorkPackage> listOfWorkPackages = wpCriteria.list();
			log.info("****listOfWorkPackages *** "+listOfWorkPackages.size());
			if(listOfWorkPackages != null && !listOfWorkPackages.isEmpty()){
				productSummaryDTO.setWpCount(listOfWorkPackages.size());
			}else{
				productSummaryDTO.setWpCount(0);
			}
		
		}
		catch(Exception e){
			log.error("Error getting ProductSummary",e);
		}
		return productSummaryDTO;
	}
	
	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductId(Integer productId) {
		log.debug("listing all TestRunPlan instance");
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.createAlias("trplan.productVersionListMaster", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			
			c.add(Restrictions.eq("trplan.status", 1));
			if(productId!=null){
				c.add(Restrictions.eqProperty("trplan.productVersionListMaster.productVersionListId", "productVersion.productVersionListId"));
				c.add(Restrictions.eq("productVersion.productMaster.productId", productId));
			}
			testRunPlanList = c.list();
			for(TestRunPlan tp : testRunPlanList){
				if(tp.getTestSuiteLists() != null){
					Hibernate.initialize(tp.getTestSuiteLists());
					for(TestSuiteList tsl : tp.getTestSuiteLists()){
						if(tsl.getTestCaseLists() != null){
							Hibernate.initialize(tsl.getTestCaseLists());
						}
					}					
				}
				
				if(tp.getRunConfigurationList() != null){
					Hibernate.initialize(tp.getRunConfigurationList());
					for(RunConfiguration rc : tp.getRunConfigurationList()){
						if(rc.getTestSuiteLists() != null){
							Hibernate.initialize(rc.getTestSuiteLists());
							for(TestSuiteList tsl : rc.getTestSuiteLists()){
								if(tsl.getTestCaseLists() != null){
									Hibernate.initialize(tsl.getTestCaseLists());
								}
							}	
						}
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlanList;
	}



	@Override
	@Transactional
	public void addtestRunPlanGroup(TestRunPlanGroup testRunPlanGroup) {
		try {
		sessionFactory.getCurrentSession().save(testRunPlanGroup);
		log.debug("add successful");
		}catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}



	@Override
	public List<TestRunPlanGroup> listTestRunPlanGroup(int productVersionId, int productId) {
		log.info("listing all TestRunPlanGroup");
		List<TestRunPlanGroup> testRunPlanGroup=null;
		
		try {
			if(productVersionId == 0){
				testRunPlanGroup= sessionFactory.getCurrentSession().createQuery("from TestRunPlanGroup trpg"
						+ " where productId="+productId).list();	
			}else{
				testRunPlanGroup= sessionFactory.getCurrentSession().createQuery("from TestRunPlanGroup trpg "
						+ "where productVersionId="+productVersionId).list();
			}			
			if(testRunPlanGroup!=null && !testRunPlanGroup.isEmpty()){
				for (TestRunPlanGroup trpg : testRunPlanGroup) {
					Hibernate.initialize(trpg.getProductVersionListMaster());
					if(trpg.getProductVersionListMaster()!= null && trpg.getProductVersionListMaster().getProductMaster() != null){
						ProductMaster prod = trpg.getProductVersionListMaster().getProductMaster();
						Hibernate.initialize(prod);
						if(prod.getProductType() != null){
							ProductType  prodType = prod.getProductType();
							Hibernate.initialize(prodType);
						}
					}
				}
			}
		} catch (HibernateException e) {
			log.error("Error Listing TestRunPlanGroup", e);
		}
		return testRunPlanGroup;
	}

	@Override
	@Transactional
	public void update(TestRunPlanGroup testRunPlanGroupFromUI) {
		log.info("updating TestRunPlanGroup instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testRunPlanGroupFromUI);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}
	}



	@Override
	public List<TestRunPlangroupHasTestRunPlan> listTestRunPlanGroupMap(Integer testRunPlanGroupId) {
		List<TestRunPlangroupHasTestRunPlan> testRunPlanGroupMap=null;
		try{
			log.info("listing all TestRunPlanGroup Mapp");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlangroupHasTestRunPlan.class, "trplan");
		c.add(Restrictions.eq("trplan.testRunPlanGroup.testRunPlanGroupId", testRunPlanGroupId));
		c.addOrder(Order.asc("trplan.executionOrder"));
		testRunPlanGroupMap=c.list();
		if(testRunPlanGroupMap.size()!=0){
			Hibernate.initialize(testRunPlanGroupMap);
			for(TestRunPlangroupHasTestRunPlan testRunPlangroupHasTestRunPlan:testRunPlanGroupMap){
				Hibernate.initialize(testRunPlangroupHasTestRunPlan);
				if(testRunPlangroupHasTestRunPlan.getTestrunplan()!=null){
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan());
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getTestRunPlanId());
					if(testRunPlangroupHasTestRunPlan.getTestrunplan().getProductVersionListMaster() != null){
						Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getProductVersionListMaster());
						if(testRunPlangroupHasTestRunPlan.getTestrunplan().getProductVersionListMaster().getProductMaster() != null){
							Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getProductVersionListMaster().getProductMaster());	
						}
					}
				}
				if(testRunPlangroupHasTestRunPlan.getTestRunPlanGroup()!=null){
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestRunPlanGroup());
				}
				Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestRunPlanGroup().getExecutionTypeMaster().getName());
				Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestRunPlanGroup().getExecutionTypeMaster().getExecutionTypeId());
				Hibernate.initialize(testRunPlangroupHasTestRunPlan.getExecutionOrder());
				Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestRunGroupId());
				if(testRunPlangroupHasTestRunPlan.getTestrunplan()!=null){
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getTestToolMaster().getTestToolName());
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getTestToolMaster().getTestToolId());
					Hibernate.initialize(testRunPlangroupHasTestRunPlan.getTestrunplan().getRunConfigurationList());
				}
				
				
			
			}
		}
		
		}catch(RuntimeException re){
			log.error("list failed", re);
		}
		return testRunPlanGroupMap;
		
	}

		
		@Override
		@Transactional
		public TestRunPlan addtestrunpalngroupMapping(int testRunPlanId, int testRunPlanGroupId,String maporunmap) {
			log.info("updateProductFeatureTestCases method updating ProductFeature - TestCase Association");
			TestRunPlan testRunPlan = null;
			TestRunPlanGroup testRunPlanGroup = null;
			Set<TestRunPlan> testRunPlanSet = null;
			try{
				log.info("testRunPlanId==>"+testRunPlanId+"testRunPlanGroupId==>"+testRunPlanGroupId);
				testRunPlan = getTestRunPlanById(testRunPlanId);
				 testRunPlanGroup=getTestRunPlanGroupById(testRunPlanGroupId);
				 log.info("testRunPlan===>"+testRunPlan.getTestRunPlanId()+"testRunPlanGroup=="+testRunPlanGroup.getTestRunPlanGroupId());
				 TestRunPlangroupHasTestRunPlan testRunPlanHasRunconfig=new TestRunPlangroupHasTestRunPlan();
				 testRunPlanHasRunconfig.setExecutionOrder(0);
				 testRunPlanHasRunconfig.setTestrunplan(testRunPlan);
				 testRunPlanHasRunconfig.setTestRunPlanGroup(testRunPlanGroup);
				 if(maporunmap.equalsIgnoreCase("map")){
					 
					 sessionFactory.getCurrentSession().save(testRunPlanHasRunconfig);
				 }
				 if(maporunmap.equalsIgnoreCase("unmap")){
					// sessionFactory.getCurrentSession().save(testRunPlanHasRunconfig);
					 sessionFactory.getCurrentSession().createQuery("delete from TestRunPlangroupHasTestRunPlan tstrunPlanHasConfig where tstrunPlanHasConfig.testrunplan.testRunPlanId="+testRunPlanId+"and tstrunPlanHasConfig.testRunPlanGroup.testRunPlanGroupId="+testRunPlanGroupId).executeUpdate();
				 }
				
			} catch (RuntimeException re) {
				log.error("list specific failed", re);
			}
			return null;		
	}
	
	@Override
	@Transactional
	public TestRunPlanGroup getTestRunPlanGroupById(int testRunPlanGroupId){
		log.debug("getting TestRunPlan instance by id");
		TestRunPlanGroup testRunPlanGroup=null;
		List<TestRunPlanGroup> testRunGroupList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlanGroup.class, "trplanGroup");
			c.add(Restrictions.eq("trplanGroup.testRunPlanGroupId", testRunPlanGroupId));
			
			testRunGroupList = c.list();
			testRunPlanGroup=(testRunGroupList!=null && testRunGroupList.size()!=0)?(TestRunPlanGroup)testRunGroupList.get(0):null;
			Hibernate.initialize(testRunPlanGroup.getTestRunPlangroupHasTestRunPlans());
			if(testRunPlanGroup.getTestRunPlangroupHasTestRunPlans() != null){
				for(TestRunPlangroupHasTestRunPlan tpg : testRunPlanGroup.getTestRunPlangroupHasTestRunPlans()){
					Hibernate.initialize(tpg.getTestrunplan());
					if(tpg.getTestrunplan() != null){
						Hibernate.initialize(tpg.getTestrunplan().getTestRunPlanId());
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlanGroup;
        
	}

	@Override
	@Transactional
	public List<ProductMaster> getProductsByAllocation(int userId) {
		log.info("listing all ProductMaster getProductsByAllocation");
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		Set<ProductMaster> setOfProducts = new HashSet<ProductMaster>();
			try {
				if(userId == 0){
					return null;
				}
				List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = null;
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
				c.createAlias("wptcep.testLead", "testLeadUser");
				c.createAlias("wptcep.workPackage", "workPackage");
				c.createAlias("workPackage.productBuild", "productbuild");
				c.createAlias("productbuild.productVersion", "productVersion");
				c.createAlias("productVersion.productMaster", "product");
				if(userId != 0){
					c.add(Restrictions.eq("testLead.userId", userId));
				}
				c.add(Restrictions.eq("product.status",1));
				listOfWorkPackageTestCaseExecutionPlan = c.list();
				log.info("Result Set Size : " + listOfWorkPackageTestCaseExecutionPlan.size());
				for (WorkPackageTestCaseExecutionPlan wpTcExecPlan : listOfWorkPackageTestCaseExecutionPlan) {
					if(wpTcExecPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
						ProductMaster product = wpTcExecPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
						Hibernate.initialize(product);
						setOfProducts.add(product);
					}
				}
				if(setOfProducts != null && setOfProducts.size() >0){
					listOfProducts.addAll(setOfProducts);
				}
			} catch (Exception re) {
				log.error("list failed", re);
			}
		return listOfProducts;
	}



	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductVersionId(
			int productversionId, int testRunPlanGroupId) {
		log.debug("listing all TestRunPlan instance");
		TestRunPlanGroup testRunPlanGroup =getTestRunPlanGroupById(testRunPlanGroupId);
		
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			c.createAlias("trplan.productVersionListMaster", "productVersion");
			c.createAlias("trplan.executionType", "et");

			c.add(Restrictions.eq("trplan.status", 1));
			c.add(Restrictions.eq("et.executionTypeId", testRunPlanGroup.getExecutionTypeMaster().getExecutionTypeId()));

			if(productversionId!=-1){
				c.add(Restrictions.eq("productVersion.productVersionListId", productversionId));
			}
			testRunPlanList = c.list();
			for (TestRunPlan testRunPlan : testRunPlanList) {
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				if(testRunPlan.getProductVersionListMaster().getProductMaster() != null){
					Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());	
				}
				Hibernate.initialize(testRunPlan.getTestToolMaster());
				Hibernate.initialize(testRunPlan.getRunConfigurationList());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlanList;
		}

	
	
	@Override
	@Transactional
	public void update(TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromUI) {
		
			log.info("updating TestRunPlangroupHasTestRunPlan instance");
			try {
				sessionFactory.getCurrentSession().saveOrUpdate(testRunPlanGroupHasFromUI);
				log.info("update successful");
			} catch (RuntimeException re) {
				log.error("update failed", re);
				//throw re;
			}
		}

	@Override
	@Transactional
	public TestRunPlan getTestRunPlanByName(String name) {
		log.debug("listing all TestRunPlan instance");
		List<TestRunPlan> testRunPlanList=null;

		TestRunPlan testRunPlan=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			
			c.add(Restrictions.eq("trplan.testRunPlanName", name));
			
			testRunPlanList = c.list();
			testRunPlan=(testRunPlanList!=null && testRunPlanList.size()!=0)?(TestRunPlan)testRunPlanList.get(0):null;
			if(testRunPlan!=null){
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getTestToolMaster());

				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getProductMode());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getHostLists());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getGenericeDevices());

				Set<RunConfiguration> runconfigSet = testRunPlan.getRunConfigurationList();
				Hibernate.initialize(runconfigSet);
					
				for (RunConfiguration runConfiguration : runconfigSet) {
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getHostList());
					if( runConfiguration.getGenericDevice()!=null){
						Hibernate.initialize(runConfiguration.getGenericDevice().getHostList());
						GenericDevices devices = runConfiguration.getGenericDevice();
						Hibernate.initialize(devices);
						Hibernate.initialize(devices.getDeviceLab());
						Hibernate.initialize(devices.getDeviceModelMaster());
						Hibernate.initialize(devices.getPlatformType());
						Hibernate.initialize(devices.getProductMaster());
						Hibernate.initialize(devices.getTestFactory());
						if((devices instanceof MobileType) && ((MobileType) devices).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) devices).getDeviceMakeMaster());
						}
						if((devices instanceof ServerType) ){
							if(((ServerType) devices).getProcessor() != null){
								Hibernate.initialize(((ServerType) devices).getProcessor());	
							}
							if(((ServerType) devices).getSystemType() != null){
								Hibernate.initialize(((ServerType) devices).getSystemType());	
							}						
						}
					}
					Hibernate.initialize(runConfiguration.getProduct());
					Hibernate.initialize(runConfiguration.getProductVersion());
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration.getTestSuiteLists());
				}
				Hibernate.initialize(testRunPlan.getTestSuiteLists());		
				Hibernate.initialize(testRunPlan.getFeatureList());		
				Hibernate.initialize(testRunPlan.getTestCaseList());		
				Set<TestCaseList> testcaseList1=testRunPlan.getTestCaseList();
				if(testcaseList1.size()!=0){
					for (TestCaseList tcl : testcaseList1) {
						if(tcl!=null){
							Hibernate.initialize(tcl);
							if(tcl.getProductFeature()!=null){
								Hibernate.initialize(tcl.getProductFeature());
							}
							if(tcl.getDecouplingCategory()!=null){
								Hibernate.initialize(tcl.getDecouplingCategory());
							}
							
							
						}
						
						
					}
				}
				
				Set<TestSuiteList> testSuiteSet = testRunPlan.getTestSuiteLists();
				Hibernate.initialize(testSuiteSet);		
				for (TestSuiteList testSuiteList : testSuiteSet) {
					Hibernate.initialize(testSuiteList.getExecutionTypeMaster());
					Hibernate.initialize(testSuiteList.getProductMaster());
					Hibernate.initialize(testSuiteList.getProductVersionListMaster());
					Hibernate.initialize(testSuiteList.getScriptTypeMaster());
					Set<TestCaseList> testcaseList = testSuiteList.getTestCaseLists();
					Hibernate.initialize(testcaseList);
					for (TestCaseList testCaseListObj : testcaseList) {
						Hibernate.initialize(testCaseListObj.getTestCaseStepsLists());
						if(testCaseListObj.getProductFeature()!=null){
							Hibernate.initialize(testCaseListObj.getProductFeature());
						}
						
					}
					
					
					Hibernate.initialize(testSuiteList.getTestExecutionResults());
					Hibernate.initialize(testSuiteList.getTestRunPlanList());
					Hibernate.initialize(testRunPlan.getTestCaseList());
					Set<TestCaseList> testcaseSet=testRunPlan.getTestCaseList();
					for (TestCaseList tcl : testcaseSet) {
						Hibernate.initialize(tcl.getProductFeature());
						Hibernate.initialize(tcl.getDecouplingCategory());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
					Set<ProductFeature> featureSet=testRunPlan.getFeatureList();
					for (ProductFeature productFeature : featureSet) {
						Hibernate.initialize(productFeature.getTestCaseList());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
				}
			}						
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlan;
		}



	@Override
	@Transactional
	public boolean isProductExitsInsameTestFactory(Integer testFactoryId,String productName) {
			String hql = "from ProductMaster pur where pur.testFactory.testFactoryId = :testFactoryId and pur.productName=:productName";
			List product = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryId", testFactoryId).setParameter("productName", productName).list();
			
			if (product == null || product.isEmpty()) 
			    return false;
			else 
				return true;
	}

	@Override
	@Transactional
	public void updateTestRunplan(TestRunPlan testRunPlan) {

		log.info("updating TestRunPlan instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testRunPlan);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}



	@Override
	@Transactional
	public List<ProductMaster> listProductByEngagementType(int engagementTypeId) {
		log.debug("listing all ProductMaster by engagementTypeId");
		List<ProductMaster> productMaster=null;
		try {
			productMaster=sessionFactory.getCurrentSession().createQuery("from ProductMaster p where p.status='1' and p.testFactory.engagementTypeMaster.engagementTypeId=:engagementTypeId").setParameter("engagementTypeId", engagementTypeId).list();
			for (ProductMaster productMaster2 : productMaster) {
				Hibernate.initialize(productMaster2.getTestFactory());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return productMaster;
	}



	@Override
	@Transactional
	public void mapTestSuiteTestCasesTestRunPlan(int testRunPlanId,
			int testSuiteId, int testCaseId, String type) {
		log.info("testCaseList:"+testCaseId);
		log.info("testSuiteList:"+testSuiteId);
	
		if(type.equals("Add")){
			TestRunPlanTSHasTC testRunPlanTSHasTC = new TestRunPlanTSHasTC();
			testRunPlanTSHasTC.setTestrunplan(getTestRunPlanById(testRunPlanId));
			TestSuiteList testSuiteList =testSuiteListDAO.getByTestSuiteId(testSuiteId);
			TestCaseList testCaseList =testCaseListDAO.getByTestCaseId(testCaseId);
			log.info("testCaseList:"+testCaseList.getTestCaseId());
			log.info("testSuiteList:"+testSuiteList.getTestSuiteId());
			testRunPlanTSHasTC.setTestSuiteList(testSuiteList);
			testRunPlanTSHasTC.setTestCaseList(testCaseList);
			sessionFactory.getCurrentSession().save(testRunPlanTSHasTC);
		}else if(type.equals("Remove")){
			List<TestRunPlanTSHasTC> testRunPlanTSHasTCList=null;
			TestRunPlanTSHasTC testRunPlanTSHasTC=null;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlanTSHasTC.class, "trptstc");
			c.createAlias("trptstc.testrunplan", "trp");
			c.createAlias("trptstc.testSuiteList", "tsl");
			c.createAlias("trptstc.testCaseList", "tcl");

			c.add(Restrictions.eq("trp.testRunPlanId",testRunPlanId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			c.add(Restrictions.eq("tcl.testCaseId",testCaseId));
			
			testRunPlanTSHasTCList = c.list();
			if(testRunPlanTSHasTCList != null && testRunPlanTSHasTCList.size() > 0){
				for(TestRunPlanTSHasTC tc : testRunPlanTSHasTCList){
					sessionFactory.getCurrentSession().delete(tc);
				}
			}
		}
		
		
	}



	@Override
	@Transactional
	public List<TestCaseList> getTestSuiteTestCaseMapped(int testRunPlanId,
			int testSuiteId) {
		log.debug("listing all TestCaseList instance");
		
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
		List<TestRunPlanTSHasTC> testRunPlanTSHasTCList=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlanTSHasTC.class, "trptstc");
			c.createAlias("trptstc.testrunplan", "trp");
			c.createAlias("trptstc.testSuiteList", "tsl");

			c.add(Restrictions.eq("trp.testRunPlanId",testRunPlanId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));

			
			testRunPlanTSHasTCList = c.list();
			for (TestRunPlanTSHasTC testRunPlanTSHasTC : testRunPlanTSHasTCList) {
				Hibernate.initialize(testRunPlanTSHasTC.getTestCaseList());
				Hibernate.initialize(testRunPlanTSHasTC.getTestSuiteList());
				Hibernate.initialize(testRunPlanTSHasTC.getTestrunplan());
				testCaseList.add(testRunPlanTSHasTC.getTestCaseList());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testCaseList;
	}
	
	@Override
	@Transactional
	public List<RunConfiguration> listRunConfigurationByTestRunPlanId(Integer testRunPlanId,Integer jtStartIndex,Integer jtPageSize) {
		List<RunConfiguration> runConfigurationList = new LinkedList<RunConfiguration>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "runConfig");
		c.createAlias("runConfig.testRunPlan", "testRunPlan");
		c.add(Restrictions.eq("testRunPlan.testRunPlanId",testRunPlanId));
		c.add(Restrictions.eq("runConfig.runConfigStatus",1));
		c.setFirstResult(jtStartIndex);
		c.setMaxResults(jtPageSize);
		runConfigurationList = c.list();
		if (runConfigurationList.size()!=0) {
			for (RunConfiguration runConfig : runConfigurationList) {
				Hibernate.initialize(runConfig);
				if(runConfig.getEnvironmentcombination()!=null){
					Hibernate.initialize(runConfig.getEnvironmentcombination());
				}
				if(runConfig.getTestRunPlan()!=null){
					Hibernate.initialize(runConfig.getTestRunPlan());
					if(runConfig.getTestRunPlan().getProductVersionListMaster()!=null){
						Hibernate.initialize(runConfig.getTestRunPlan().getProductVersionListMaster());
						if(runConfig.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
							Hibernate.initialize(runConfig.getTestRunPlan().getProductVersionListMaster().getProductMaster());
							if(runConfig.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
								Hibernate.initialize(runConfig.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(runConfig.getGenericDevice()!=null){
					Hibernate.initialize(runConfig.getGenericDevice());
					if(runConfig.getGenericDevice().getHostList()!=null){
						Hibernate.initialize(runConfig.getGenericDevice().getHostList());
						if(runConfig.getGenericDevice().getHostList().getCommonActiveStatusMaster()!=null){
							Hibernate.initialize(runConfig.getGenericDevice().getHostList().getCommonActiveStatusMaster());
						}
					}
				}
				
				if(runConfig.getHostList()!=null){
					if(runConfig.getHostList() != null){
						Hibernate.initialize(runConfig.getHostList());
						Hibernate.initialize(runConfig.getHostList().getHostIpAddress());
						Hibernate.initialize(runConfig.getHostList().getHostName());
						Hibernate.initialize(runConfig.getHostList().getHostId());
					}
				}	
				
				if(runConfig.getTestSuiteLists() != null){
					Hibernate.initialize(runConfig.getTestSuiteLists());
					for(TestSuiteList tsl : runConfig.getTestSuiteLists()){
						Hibernate.initialize(tsl.getTestCaseLists());
						if(tsl.getTestCaseLists() != null){
							for(TestCaseList tcts : tsl.getTestCaseLists()){
								Hibernate.initialize(tcts.getTestCaseStepsLists());
							}
						}
					}
				}
				if(runConfig.getTestTool() != null){
					Hibernate.initialize(runConfig.getTestTool().getTestToolId());
					Hibernate.initialize(runConfig.getTestTool().getTestToolName());
				}
				if(runConfig.getScriptTypeMaster() != null){
					Hibernate.initialize(runConfig.getScriptTypeMaster().getScriptType());
				}
				if(runConfig.getProduct() != null){
					Hibernate.initialize(runConfig.getProduct().getProductId());
					Hibernate.initialize(runConfig.getProduct().getProductName());
				}
				if(runConfig.getProductType() != null){
					if(runConfig.getProductType().getProductTypeId() != null)
						Hibernate.initialize(runConfig.getProductType().getProductTypeId());
				}
				
		}
	}
		return runConfigurationList;
	
}



	@Override
	@Transactional
	public List<MetricsMaster> listMetrics() {
		List<MetricsMaster> metricsMaster = new ArrayList<MetricsMaster>();
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(MetricsMaster.class, "MetricsMaster");
		metricsMaster=c.list();
		log.info("Total metrics-----"+metricsMaster.size());
	}
		catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return metricsMaster;
		
		
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfWPrcStatus(Integer workpackageId, Integer runConfigStatus) {
		log.debug("listing all RunConfiguration instance of WP Id with rcStatus 1");
		Set<RunConfiguration> runConfigurationsSet=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;		
		try {
			runConfigurationList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.workPackage.workPackageId=:wpid and rc.runConfigStatus=:rcStatus")
					.setParameter("wpid", workpackageId)
					.setParameter("rcStatus", runConfigStatus).list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				log.info("runConfigurationList>>"+runConfigurationList.size());
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					if(rc.getTestRunPlan()!=null){
						if(rc.getTestRunPlan().getProductVersionListMaster()!=null){
							Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
							if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
								Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
								if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
									Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
								}
							}
						}
					}
				}
				runConfigurationsSet.addAll(runConfigurationList);
			}
			log.debug("listing all RunConfiguration instance of WP Id with rcStatus 1 successful");
		} catch (RuntimeException re) {
			log.error("listing all RunConfiguration instance of WP Id with rcStatus 1 failed", re);		
		}
		return runConfigurationsSet;
	}
	
	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatus(Integer testRunPlanId, Integer runConfigStatus) {
		log.debug("listing all RunConfiguration instance of TestRunPlan Id with rcStatus 1");
		Set<RunConfiguration> runConfigurationsSet=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		try {
			runConfigurationList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.testRunPlan.testRunPlanId=:trplanid and rc.runConfigStatus=:rcStatus")
					.setParameter("trplanid", testRunPlanId)
					.setParameter("rcStatus", runConfigStatus).list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){				
				log.info("runConfigurationList>>"+runConfigurationList.size());
				for (RunConfiguration rc : runConfigurationList) {					
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					if(rc.getTestRunPlan()!=null){
						if(rc.getTestRunPlan().getProductVersionListMaster()!=null){
							Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
							if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
								Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
								if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
									Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
								}
							}
						}
					}
				}
				runConfigurationsSet.addAll(runConfigurationList);
			}
			log.debug("listing all RunConfiguration instance of TestRunPlan Id with rcStatus 1 successful");
		} catch (RuntimeException re) {
			log.error("listing all RunConfiguration instance of TestRunPlan Id with rcStatus 1 failed", re);		
		}
		return runConfigurationsSet;
	}
	
	
	
	@Override
	@Transactional
	public Integer countAllProduct(Date startDate,Date endDate) {

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class,"product");
			if (startDate != null) {
				c.add(Restrictions.ge("product.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("product.createdDate", endDate));
			}
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all Product", e);
			return -1;
		}
	}
	
	
	@Override
	@Transactional
	public List<ProductMaster> listAllProductsByLastSyncDate(int startIndex, int pageSize,  Date startDate,Date endDate) {
		log.debug("listing all Product");
		List<ProductMaster> products=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			if (startDate != null) {
				c.add(Restrictions.ge("product.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("product.createdDate", endDate));
			}
			c.addOrder(Order.asc("productId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);

            products = c.list();		
			
			if (!(products == null || products.isEmpty())){
				for (ProductMaster product : products) {
					Hibernate.initialize(product.getTestFactory());
					Hibernate.initialize(product.getProductMode());
					Hibernate.initialize(product.getCustomer());
					Hibernate.initialize(product.getProductType());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return products;
	}
	
	
	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(Integer testRunPlanId, Integer runConfigStatus, Integer prodType ) {
		log.debug("listing all RunConfiguration(DeviceorHost) instance of TestRunPlan Id with rcStatus 1");
		Set<RunConfiguration> runConfigurationsSet=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;	
		try {
			if(prodType == 1 || prodType == 5){
				runConfigurationList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.testRunPlan.testRunPlanId=:trplanid and rc.runConfigStatus=:rcStatus and rc.genericDevice.genericsDevicesId is not null")
						.setParameter("trplanid", testRunPlanId)
						.setParameter("rcStatus", runConfigStatus).list();
			}else if(prodType == 6){
				String query="select * from runconfiguration where testRunPlanId="+testRunPlanId+" and runConfigStatus="+runConfigStatus
						+" and (hostId or deviceId) is not null";
				runConfigurationList = sessionFactory.getCurrentSession().createSQLQuery(query).addEntity(RunConfiguration.class).list();
			}else{
				runConfigurationList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.testRunPlan.testRunPlanId=:trplanid and rc.runConfigStatus=:rcStatus and rc.hostList.hostId is not null")
						.setParameter("trplanid", testRunPlanId)
						.setParameter("rcStatus", runConfigStatus).list();
			}
			
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){				
				log.info("runConfigurationList>>"+runConfigurationList.size());
				for (RunConfiguration rc : runConfigurationList) {					
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					if(rc.getTestRunPlan()!=null){
						if(rc.getTestRunPlan().getProductVersionListMaster()!=null){
							Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
							if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
								Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
								if(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
									Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
								}
							}
						}
					}
					
					if(rc.getTestSuiteLists() != null ){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
				}
				runConfigurationsSet.addAll(runConfigurationList);
			}
			log.debug("listing all RunConfiguration(DeviceorHost) instance of TestRunPlan Id with rcStatus 1 successful");
		} catch (RuntimeException re) {
			log.error("listing all RunConfiguration(DeviceorHost) instance of TestRunPlan Id with rcStatus 1 failed", re);		
		}
		return runConfigurationsSet;
	}
	/**
	 filter - 0 (Get data by assignee)
	 		- 1 (Get data by reviewer)
	 **/
	@Override
	@Transactional
	public List<ProductMaster> getProductsByAssigneeOrReviwerForUserId(int userRoleId,int userId, int filter) {
		List<WorkPackage> listOfWorkPackages = null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wp.productBuild", "pb");
			c.createAlias("pb.productVersion", "pv");
			c.createAlias("pv.productMaster", "product");
			c.add(Restrictions.eq("product.status", 1));
			if(filter == 0){
				if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					c.createAlias("wptcep.testLead", "testLead");
					c.add(Restrictions.eq("testLead.userId", userId));
				}else if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
					c.createAlias("wptcep.tester", "tester");
					c.add(Restrictions.eq("tester.userId", userId));
				}
			}else{
				c.createAlias("wptcep.testLead", "testLead");
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.disjunction().add(
				        Restrictions.or(Restrictions.eq("tester.userId", userId),
				                		Restrictions.eq("testLead.userId", userId))));
			}
			
			listOfWorkPackages = c.list();
			log.debug("Result Set Size of WorkPackages : " + listOfWorkPackages.size());
			
			for (WorkPackage workPackage : listOfWorkPackages) {
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getCustomer());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
				if(!listOfProducts.contains(workPackage.getProductBuild().getProductVersion().getProductMaster())){
					listOfProducts.add(workPackage.getProductBuild().getProductVersion().getProductMaster());
				}
			}
			log.debug("Result Set Size of Products : " + listOfProducts.size());
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}
	@Override
	@Transactional
	public ProductSummaryDTO getProductSummaryByProductId(Integer productId){
	
		List<ProductSummaryDTO> productSummaryDTOList = null;
		ProductSummaryDTO productSummaryDTO = null;
		String sql="SELECT  product.productName AS productName,product.productDescription AS description,"
		+"customer.customerName AS customerName,prodMode.modeId AS productModeId,prodMode.modeName AS productMode,tf. testFactoryId as engagementId,tf.testFactoryName as engagementName, "


		+"( SELECT  COUNT(pversion.productVersionListId)   FROM product_version_list_master AS pversion WHERE pversion.productId ="+ productId+" and pversion.status=1 ) AS pVersionCount," 

		+"( SELECT  COUNT(pbuild.productBuildId)   FROM product_build AS pbuild "
		+" INNER JOIN product_version_list_master AS pversion ON pbuild.productVersionId=pversion.productVersionListId WHERE pversion.productId ="+ productId+" and pversion.status=1  and pbuild.status=1) AS pBuildCount," 

		+"( SELECT  COUNT(wp.workPackageId)   FROM workpackage AS wp "
		 
		+" INNER JOIN product_build AS pbuild ON pbuild.productBuildId=wp.productBuildId"
		+" INNER JOIN product_version_list_master AS pversion ON pbuild.productVersionId=pversion.productVersionListId WHERE pversion.productId ="+ productId+" AND wp.status='1') AS wpCount ,"

		+"( SELECT  COUNT(pFeature.productFeatureId)   FROM product_feature AS pFeature WHERE pFeature.productId ="+ productId+" and pFeature.productFeatureStatus=1) AS featuresCount," 

		+"( SELECT  COUNT(tclist.testCaseId)   FROM test_case_list AS tclist WHERE tclist.productId ="+ productId+" and status = 1 ) AS testCaseCount,"

		+"( SELECT  COUNT(tcStepsList.testStepId)   FROM test_case_steps_list AS tcStepsList "
		+" INNER JOIN test_case_list AS tclist ON tcStepsList.testCaseId=tclist.testCaseId WHERE tclist.productId ="+ productId+"  ) AS testCaseStepsCount,"

		+"( SELECT  COUNT(tslist.testSuiteId)   FROM test_suite_list AS tslist WHERE tslist.productId ="+ productId+" and tslist.status=1) AS testSuiteCount,"

		+"( SELECT  COUNT(decouplingCat.decouplingCategoryId)   FROM decoupling_category AS decouplingCat WHERE decouplingCat.productId ="+ productId+" and decouplingCat.status=1 ) AS dcCount,"

		+"( SELECT  COUNT(testRunPlan.testRunPlanId)   FROM test_run_plan AS testRunPlan "
		+" INNER JOIN product_version_list_master AS pversion ON testRunPlan.productVersionId=pversion.productVersionListId  WHERE pversion.productId ="+ productId+" and pversion.status=1 and  testRunPlan.status=1) AS testRunPlanCount,"
+" ( SELECT  COUNT(*)   FROM environment_combination AS envComb WHERE envComb.productId ="+ productId+" AND envComb.envionmentCombinationStatus=1) AS productEnviCombinationCount"
         +" FROM    product_master  AS product "

		+" INNER JOIN customer AS customer ON  customer.customerId=product.customerId"
		+" INNER JOIN test_factory AS tf ON tf.testFactoryId=product.testFactoryId"
		+" LEFT JOIN product_mode AS prodMode ON prodMode.modeId=product.modeId WHERE product.productId="+ productId;

		productSummaryDTOList = ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql)).addScalar("productName",StandardBasicTypes.STRING).addScalar("description",StandardBasicTypes.STRING)
				.addScalar("customerName",StandardBasicTypes.STRING).addScalar("productModeId",StandardBasicTypes.INTEGER)
				.addScalar("productMode",StandardBasicTypes.STRING).addScalar("engagementId",StandardBasicTypes.INTEGER).addScalar("engagementName",StandardBasicTypes.STRING)
				.addScalar("pVersionCount",StandardBasicTypes.INTEGER).addScalar("pBuildCount",StandardBasicTypes.INTEGER)
				.addScalar("wpCount",StandardBasicTypes.INTEGER).addScalar("featuresCount",StandardBasicTypes.INTEGER)
				.addScalar("testCaseCount",StandardBasicTypes.INTEGER).addScalar("testCaseStepsCount",StandardBasicTypes.INTEGER)
				.addScalar("testSuiteCount",StandardBasicTypes.INTEGER).addScalar("dcCount",StandardBasicTypes.INTEGER)
				.addScalar("testRunPlanCount",StandardBasicTypes.INTEGER).addScalar("productEnviCombinationCount",StandardBasicTypes.INTEGER)
				//.addScalar("wpCount",StandardBasicTypes.INTEGER).addScalar("featuresCount",StandardBasicTypes.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(ProductSummaryDTO.class)).list();

		productSummaryDTO=productSummaryDTOList.get(0);

		return productSummaryDTO;
		
	}


	
	@Override
	@Transactional
	public List<Integer> listTestSuites(int testRunPlanId) {
		log.debug("listing all listTestSuites instance");
		List<Integer> listTestSuites=null;
		try {			
			listTestSuites = sessionFactory.getCurrentSession().createSQLQuery("select tc.testSuiteId from testrunplan_has_testsuite tc where tc.testRunPlanId=:trplanid").setParameter("trplanid", testRunPlanId).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return listTestSuites;
	}
	
	@Override
	@Transactional
	public List<ProductMaster> getProductsByProductUserRoleForTestingTeamUserId(
			int userRoleId, int userId,int filter) {
		List<ProductTeamResources> listOfProductteam= null;
		List<ProductMaster> listOfProducts = new ArrayList<ProductMaster>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductTeamResources.class, "productTeam");
			c.createAlias("productTeam.productSpecificUserRole", "productUserRole");
			c.createAlias("productTeam.userList", "productUser");
			c.createAlias("productTeam.productMaster", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.engagementTypeMaster", "engagementType");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.eq("productUser.userId", userId));
			c.add(Restrictions.eq("tf.status", 1));
			if(filter != 0){
				c.add(Restrictions.eq("engagementType.engagementTypeId", filter));
			}
			listOfProductteam = c.list();
			log.debug("Getting list of Products where user >>> "+ userId +" is the Test Manager" + listOfProductteam.size());
			
			for (ProductTeamResources productUserRole : listOfProductteam) {
				Hibernate.initialize(productUserRole.getProductMaster());
				Hibernate.initialize(productUserRole.getProductMaster().getTestFactory());
				Hibernate.initialize(productUserRole.getProductMaster().getCustomer());
				Hibernate.initialize(productUserRole.getProductMaster().getProductType());
				Hibernate.initialize(productUserRole.getProductMaster().getProductMode());
				listOfProducts.add(productUserRole.getProductMaster());
			}
		} catch (Exception e) {
			log.error("Getting Products where user >>> "+ userId +" is the Test Manager", e);
		}
		return listOfProducts;
	}


	@Override
	@Transactional
	public List<Object[]> fixFailReportDAO(Integer productId) {
		List<RiskHazardTraceabilityMatrixDTO>  fixreportdto = new ArrayList<RiskHazardTraceabilityMatrixDTO>();
		List<TestRunJob> fixreportlist = new ArrayList<TestRunJob>();
		List<Object[]> fixObjList = new ArrayList<Object[]>();
		log.info("Get the fix fail report with respect to product id");	
		try{
			Criteria cfix = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "testRunJob");
			cfix.createAlias("testRunJob.workPackageTestCaseExecutionPlans", "wptcep");
			cfix.createAlias("wptcep.testCaseExecutionResult", "tcer");
			cfix.createAlias("wptcep.testCase", "tcl");
			cfix.createAlias("tcl.productMaster", "pm");
			cfix.createAlias("tcer.testExecutionResultBugListSet", "terbl");
			
			cfix.createAlias("testRunJob.workPackage", "wp");
			cfix.createAlias("wp.lifeCyclePhase", "lcp");
			cfix.add(Restrictions.eq("pm.productId",productId));
			ProjectionList projectFixList = Projections.projectionList();
			projectFixList.add(Property.forName("terbl.testExecutionResultBugId").as("bugid"));
			projectFixList.add(Property.forName("terbl.bugCreationTime").as("bugCreationTime"));
			projectFixList.add(Property.forName("tcer.result").as("result"));
			projectFixList.add(Property.forName("tcl.testCaseId").as("testCaseId"));
			projectFixList.add(Property.forName("wp.iterationNumber").as("iterationNumber"));
			projectFixList.add(Property.forName("lcp.name").as("phase"));
			projectFixList.add(Property.forName("tcer.endTime").as("date"));
			projectFixList.add(Property.forName("tcer.testCaseExecutionResultId").as("testCaseExecutionResultId"));
			cfix.setProjection(projectFixList);
			fixObjList = cfix.list();
			RiskHazardTraceabilityMatrixDTO fixdto = null;
		}catch(Exception e){
			log.info("Error in Product Manage Plan DAO IMPL - Fix Fail Report");
		}
		return fixObjList;
	}
	@Override
	@Transactional
	public String getDefectSeverityName(int bugId) {
		List<Object[]> fixObjList = new ArrayList<Object[]>();
		String sevName = "";
		try {
			Criteria cFixFail = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "buglist");
			cFixFail.createAlias("buglist.defectSeverity", "sev");
			cFixFail.add(Restrictions.eq("buglist.testExecutionResultBugId",bugId ));
			ProjectionList projectFixList = Projections.projectionList();
			projectFixList.add(Property.forName("sev.severityName").as("severityName"));
			projectFixList.add(Property.forName("sev.severityId").as("severityId"));
			cFixFail.setProjection(projectFixList);
			fixObjList = cFixFail.list();
			for (Object[] objects : fixObjList) {
				if(objects[0] != null){
					sevName = (String)objects[0];	
				}				
			}
			return sevName;
		} catch (Exception e) {
			log.error("Unable to get count of all Product", e);
			return sevName;
		}
	}


	@Override
	@Transactional
	public List<Object[]> testFixFailReportDAO(Integer productId) {

		List<RiskHazardTraceabilityMatrixDTO>  testreportdto = new ArrayList<RiskHazardTraceabilityMatrixDTO>();
		List<TestRunJob> testfixreportlist = new ArrayList<TestRunJob>();
		List<Object[]> testfixObjList = new ArrayList<Object[]>();
		log.info("Get the fix fail report with respect to product id");	
		try{
			Criteria ctestfix = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "prodFeature");
			ctestfix.createAlias("prodFeature.testCaseList", "tcl");
			ctestfix.createAlias("tcl.workPackageTestCaseExecutionPlan", "wptcep");
			ctestfix.createAlias("wptcep.testCaseExecutionResult", "tcer");
			ctestfix.createAlias("prodFeature.productMaster", "pm");
			ctestfix.createAlias("wptcep.workPackage", "wp");		
			ctestfix.createAlias("wp.lifeCyclePhase", "lcp");	
			ctestfix.add(Restrictions.eq("pm.productId",productId));
			ctestfix.addOrder(Order.desc("tcer.endTime"));
			ProjectionList projectFixList = Projections.projectionList();
			projectFixList.add(Property.forName("tcer.result").as("result"));
			projectFixList.add(Property.forName("tcl.testCaseId").as("testCaseId"));
			projectFixList.add(Property.forName("tcl.testCaseName").as("testCaseName"));
			projectFixList.add(Property.forName("wp.iterationNumber").as("iterationNumber"));
			projectFixList.add(Property.forName("lcp.name").as("phase"));
			projectFixList.add(Property.forName("tcer.endTime").as("date"));
			projectFixList.add(Property.forName("prodFeature.productFeatureId").as("productFeatureId"));
			projectFixList.add(Property.forName("tcer.testCaseExecutionResultId").as("testCaseExecutionResultId"));
			projectFixList.add(Projections.max("tcer.endTime"));
			projectFixList.add(Projections.groupProperty("prodFeature.productFeatureId"));
			projectFixList.add(Projections.groupProperty("tcer.result"));
			projectFixList.add(Projections.groupProperty("tcl.testCaseId"));
			projectFixList.add(Projections.groupProperty("tcl.testCaseName"));
			projectFixList.add(Projections.groupProperty("wp.iterationNumber"));
			projectFixList.add(Projections.groupProperty("lcp.name"));
			projectFixList.add(Projections.groupProperty("tcer.endTime"));
			projectFixList.add(Projections.groupProperty("tcer.testCaseExecutionResultId"));
			ctestfix.setProjection(projectFixList);
			testfixObjList = ctestfix.list();
			RiskHazardTraceabilityMatrixDTO testfixdto = null;
		}catch(Exception e){
			log.info("Error in Product Manage Plan DAO IMPL - Fix Fail Report");
		}
		return testfixObjList;
	
	}
	
	@Override
	@Transactional
	public HashMap<String, String> getBugDetails(int testCaseExecutionResultId) {
		List<Object[]> fixObjList = new ArrayList<Object[]>();
		JsonRiskHazardTraceabilityMatrix jsonTraceTest = null;
		HashMap<String, String> bugDetailsSet = new HashMap<String, String>();
		try {
			Criteria cFixFail = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
			cFixFail.createAlias("tcer.testExecutionResultBugListSet", "terbl");
			cFixFail.createAlias("terbl.bugFilingStatus", "wf");
			cFixFail.add(Restrictions.eq("tcer.testCaseExecutionResultId",testCaseExecutionResultId));
			ProjectionList projectFixList = Projections.projectionList();
			projectFixList.add(Property.forName("terbl.testExecutionResultBugId").as("testExecutionResultBugId"));
			projectFixList.add(Property.forName("terbl.bugCreationTime").as("bugCreationTime"));	
			projectFixList.add(Property.forName("wf.stageName").as("stageName"));
			cFixFail.setProjection(projectFixList);
			fixObjList = cFixFail.list();	
			String bugid = null;
			String stagename = null;
			String bugcreationTime =  null;
			for (Object[] objects : fixObjList) {
				
				if(objects[0] != null){
					bugid = ((Integer)objects[0]).toString();	
					bugDetailsSet.put("bugid", bugid);
				}	
				if(objects[1] != null){					
					bugcreationTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objects[1]);
					bugDetailsSet.put("creationTime",bugcreationTime );
				}
				if(objects[2] != null){
					stagename = ((String)objects[2]);	
					bugDetailsSet.put("stagename",stagename );
				}
			}
			return bugDetailsSet;
		} catch (Exception e) {
			log.error("Unable to get count of all Product", e);
			return bugDetailsSet;
		}
	}


	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanBytestCaseId(int testCaseId) {
		List<TestRunPlan> testRunPlanList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "testRunPlan");
			c.createAlias("testRunPlan.runConfigurationList", "runConfigurationList");
			c.createAlias("runConfigurationList.testSuiteLists", "testSuiteLists");
			c.createAlias("testSuiteLists.testCaseLists", "testCaseLists");
		
			c.add(Restrictions.eq("testCaseLists.status", 1));
			c.add(Restrictions.eq("testCaseLists.testCaseId", testCaseId));
			testRunPlanList=c.list();
			for(TestRunPlan tp : testRunPlanList){
				if(tp.getTestSuiteLists() != null){
					Hibernate.initialize(tp.getTestSuiteLists());
					for(TestSuiteList tsl : tp.getTestSuiteLists()){
						if(tsl.getTestCaseLists() != null){
							Hibernate.initialize(tsl.getTestCaseLists());
						}
					}					
				}
				
				if(tp.getRunConfigurationList() != null){
					Hibernate.initialize(tp.getRunConfigurationList());
					for(RunConfiguration rc : tp.getRunConfigurationList()){
						if(rc.getTestSuiteLists() != null){
							Hibernate.initialize(rc.getTestSuiteLists());
							for(TestSuiteList tsl : rc.getTestSuiteLists()){
								if(tsl.getTestCaseLists() != null){
									Hibernate.initialize(tsl.getTestCaseLists());
								}
							}	
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Unable to get testrunplan list", e);			
		}
		return testRunPlanList;
	}


	@Override
	@Transactional
	public List<ProductMaster> getProductByUserCustomerAndEngagement(Integer userId, Integer userRoleId, Integer customerId, Integer engagementId, Integer activeStatus) {
		List<ProductMaster> productMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			if(userRoleId == 3 || userRoleId == 4 || userRoleId == 5){
				criteria.add(Subqueries.propertyIn("customer.customerId", DetachedCriteria.forClass(ProductUserRole.class, "productUserRole")
						.createAlias("productUserRole.user", "user")
						.createAlias("productUserRole.product", "product")
						.add(Restrictions.eq("user.userId", userId))
						.setProjection(Property.forName("product.productId"))
					));
			}
			if(customerId != null){
				criteria.add(Restrictions.eq("product.customer.customerId", customerId));
			}
			if(engagementId != null){
				criteria.add(Restrictions.eq("product.testFactory.testFactoryId", engagementId));
			}
			
			productMasters = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getProductByUserCustomerAndEngagement - ", ex);
		}
		return productMasters;
	}
	
	@Override
	@Transactional
	public List<ProductMaster> getProductsOfEngagementByUserId(Integer engagementId, Integer userId) {
		List<ProductMaster> productMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			criteria.createAlias("product.testFactory", "productTestFactory");
			criteria.add(Restrictions.eq("productTestFactory.testFactoryId", engagementId));
			if(userId != null){
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductUserRole.class, "productUserRole");
				detachedCriteria.createAlias("productUserRole.user", "user");
				detachedCriteria.createAlias("productUserRole.product", "assignedProduct");
				detachedCriteria.add(Restrictions.eq("user.userId", userId));
				detachedCriteria.setProjection(Property.forName("assignedProduct.productId"));
				criteria.add(Subqueries.propertyIn("product.productId", detachedCriteria));
			}			
			productMasters = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getProductsOfEngagementByUserId - ", ex);
		}
		return productMasters;
	}
	
	@Override
	@Transactional
	public List<ProductMaster> getProductsByEngagementId(List<Integer> testFactoryId) {
		List<ProductMaster> listOfProducts = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.testFactory", "testfactory");
			c.add(Restrictions.eq("product.status", 1));
			c.add(Restrictions.in("testfactory.testFactoryId", testFactoryId));
			
			listOfProducts = c.list();
			log.debug("Result Set Size of products *** : " + listOfProducts.size());
			for (ProductMaster productMaster : listOfProducts) {
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getProductType());
				Hibernate.initialize(productMaster.getProductMode());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProducts;
	}


	@Override
	@Transactional
	public ProductMaster getProductByNameAndTestfactoryId(String productName,Integer testFactoryId) {
		List<ProductMaster> productMaster =null;
		ProductMaster product = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "prod");
			c.createAlias("prod.testFactory", "tf");
			c.add(Restrictions.eq("tf.testFactoryId", testFactoryId));
			c.add(Restrictions.eq("prod.productName", productName));
			
			productMaster = c.list();
			product=(productMaster!=null && productMaster.size()!=0)?(ProductMaster)productMaster.get(0):null;
		}catch(Exception ex){
			log.error("Unable to get product ", ex);
		}
		
		return product;
	}


	@Override
	@Transactional
	public List<RunConfiguration> listRunConfiguration(Integer productId) {
		List<RunConfiguration> runconfigList =null;
		RunConfiguration runconfig = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			c.createAlias("rc.product", "prod");	
			c.add(Restrictions.eq("prod.productId", productId));
			runconfigList = c.list();			
		}catch(Exception ex){
			log.error("Unable to get product ", ex);
		}		
		return runconfigList;
	}

	@Override
	@Transactional
	public void mapTestSuiteWithRunConfiguration(Integer runConfigId,Integer testSuiteId, String action) {
		log.info("mapTestSuiteWithRunConfiguration");
		try {					
			log.debug("runConfigId--->"+runConfigId);
			log.debug("testSuiteId--->"+testSuiteId);
			RunConfiguration runConfig=environmentDAO.getRunConfiguration(runConfigId);
			TestSuiteList testSuiteList=testSuiteListDAO.getByTestSuiteId(testSuiteId);
			if (runConfig != null && runConfig != null) {
				boolean needToUpdateOrAdd = false;
				Set<TestSuiteList> tcSuiteSet = runConfig.getTestSuiteLists();
				log.debug("inside action>>"+action);		
				if (action.equalsIgnoreCase("Add")) {
					log.debug("inside add");
					if (tcSuiteSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						log.info("size > 0");
						TestSuiteList tcSuiteForProcessing = tcSuiteSet.iterator().next();
						if (tcSuiteForProcessing != null) {
							log.debug("tcSuiteForProcessing not null");
							int alreadyAvailabletcSuiteId = tcSuiteForProcessing.getTestSuiteId().intValue();
							if (alreadyAvailabletcSuiteId != testSuiteId) {
								log.debug("alreadyAvailabletcSuiteId---------->"+ alreadyAvailabletcSuiteId);
								log.debug("testSuiteId---------->"+ testSuiteId);
								log.info("values or different");								
								TestSuiteList testSuiteListAvailable=testSuiteListDAO.getByTestSuiteId(testSuiteId);
								for (RunConfiguration runConfigAvailable : testSuiteListAvailable.getRunConfigList()) {
									if (runConfigAvailable.getRunconfigId().intValue() == runConfigId) {
										log.debug("runConfigAvailable found ");
										sessionFactory.getCurrentSession().saveOrUpdate(testSuiteListAvailable);
										log.debug("testSuiteListAvailable.getTestRunPlanList().size()="+testSuiteListAvailable.getTestRunPlanList().size());
										break;
									}
								}
								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						runConfig.getTestSuiteLists().add(testSuiteList);
						testSuiteList.getRunConfigList().add(runConfig);
						sessionFactory.getCurrentSession().saveOrUpdate(runConfig);
						sessionFactory.getCurrentSession().saveOrUpdate(testSuiteList);
					}
				} else if(action.equalsIgnoreCase("Remove")){
					log.debug("Remove runConfig from runConfigurationSet ");
					try {					
						runConfig.getTestSuiteLists().remove(testSuiteList);
						testSuiteList.getRunConfigList().remove(runConfig);
						sessionFactory.getCurrentSession().saveOrUpdate(runConfig);
						sessionFactory.getCurrentSession().saveOrUpdate(testSuiteList);						
						log.debug("Removed runConfig from  runConfigurationSet successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove runConfig from  runConfigurationSet", re);						
					}				
				}
			}
		} catch (RuntimeException re) {
			log.error("Runconfiguration list specific failed", re);
		}
	}

	@Override
	@Transactional
	public void mapTestSuiteTestCasesRunConfiguration(int runConfigId,int testSuiteId, int testCaseId, String type) {
		log.info("testCaseList:"+testCaseId);
		log.info("testSuiteList:"+testSuiteId);	
		if(type.equals("Add")){
			RunConfigurationTSHasTC runConfigTSHasTC = new RunConfigurationTSHasTC();
			runConfigTSHasTC.setRunConfiguration(environmentDAO.getRunConfiguration(runConfigId));
			TestSuiteList testSuiteList =testSuiteListDAO.getByTestSuiteId(testSuiteId);
			TestCaseList testCaseList =testCaseListDAO.getByTestCaseId(testCaseId);
			log.info("testCaseList:"+testCaseList.getTestCaseId());
			log.info("testSuiteList:"+testSuiteList.getTestSuiteId());
			runConfigTSHasTC.setTestSuiteList(testSuiteList);
			runConfigTSHasTC.setTestCaseList(testCaseList);
			runConfigTSHasTC.setExecutionOrder(testCaseList.getTestCaseExecutionOrder());
			sessionFactory.getCurrentSession().save(runConfigTSHasTC);
		} else if(type.equals("Remove")){
			List<RunConfigurationTSHasTC> runConfigurationTSHasTCList = null;
			RunConfigurationTSHasTC runConfigurationTSHasTC = null;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfigurationTSHasTC.class, "rctstc");
			c.createAlias("rctstc.runConfiguration", "rc");
			c.createAlias("rctstc.testSuiteList", "tsl");
			c.createAlias("rctstc.testCaseList", "tcl");
			c.add(Restrictions.eq("rc.runconfigId",runConfigId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			c.add(Restrictions.eq("tcl.testCaseId",testCaseId));			
			runConfigurationTSHasTCList = c.list();
			runConfigurationTSHasTC=(runConfigurationTSHasTCList!=null && runConfigurationTSHasTCList.size()!=0)?(RunConfigurationTSHasTC)runConfigurationTSHasTCList.get(0):null;
			if(runConfigurationTSHasTC != null) {
				sessionFactory.getCurrentSession().delete(runConfigurationTSHasTC);
			}
		}		
	}

	@Override
	@Transactional
	public List<TestCaseList> getRunConfigTestSuiteTestCaseMapped(int runConfigId, int testSuiteId) {
		log.info("listing all TestCaseList instance");		
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
		List<RunConfigurationTSHasTC> runConfigTSHasTCList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfigurationTSHasTC.class, "rctstc");
			c.createAlias("rctstc.runConfiguration", "rc");
			c.createAlias("rctstc.testSuiteList", "tsl");
			c.createAlias("rctstc.testCaseList", "tc");
			c.add(Restrictions.eq("rc.id",runConfigId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			c.addOrder(Order.asc("rctstc.executionOrder"));
			c.addOrder(Order.asc("tsl.testSuiteId"));
			c.addOrder(Order.asc("tc.testCaseId"));
			runConfigTSHasTCList = c.list();
			for (RunConfigurationTSHasTC runConfigTSHasTC : runConfigTSHasTCList) {
				Hibernate.initialize(runConfigTSHasTC.getTestCaseList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList().getTestCaseLists());
				Hibernate.initialize(runConfigTSHasTC.getRunConfiguration());
				testCaseList.add(runConfigTSHasTC.getTestCaseList());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public Integer getTotalTestCaseCountForATestRunPlan(Integer testRunPlanId) {
		log.debug("listing getTotalTestCaseCountForATestRunPlan");
		int totalTestCaseCountForATestRunPlan = 0;
		String sql= "";
		try {
			sql="SELECT count(*) FROM test_case_list WHERE testCaseId IN (SELECT DISTINCT testCaseId FROM test_suite_list_has_test_case_list WHERE testSuiteId IN" 
				+ "(SELECT DISTINCT testSuiteId FROM testrunplan_has_testsuite WHERE testRunPlanId=:testRunPlanId))";
			
			totalTestCaseCountForATestRunPlan=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("testRunPlanId", testRunPlanId)
				.uniqueResult()).intValue();		
		} catch (RuntimeException re) {
			log.error("list getUsersProductCountByTestFactoryIds", re);
		}
		return totalTestCaseCountForATestRunPlan;
	}
	
	@Override
	@Transactional
	public boolean isTestCaseAlreadyMapped(Integer testRunPlanId, Integer testSuiteId,Integer testCaseId){
		boolean flag = false;
		int totalTestCaseCountForATestRunPlan = 0;
		String sql= "";
		try {
			List<TestRunPlanTSHasTC> testRunPlanTSHasTCList=null;
			TestRunPlanTSHasTC testRunPlanTSHasTC=null;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlanTSHasTC.class, "trptstc");
			c.createAlias("trptstc.testrunplan", "trp");
			c.createAlias("trptstc.testSuiteList", "tsl");
			c.createAlias("trptstc.testCaseList", "tcl");

			c.add(Restrictions.eq("trp.testRunPlanId",testRunPlanId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			c.add(Restrictions.eq("tcl.testCaseId",testCaseId));
			
			testRunPlanTSHasTCList = c.list();
			
			if(testRunPlanTSHasTCList.size() > 0){
				flag = true;
			}
		}catch(Exception e){
			log.error("Error in obtaining status " ,e);
		}
		return flag;
	}


	@Override
	@Transactional
	public void unMapRunConfigurationTestRunPlan(int testrunplanid,	int runconfigid) {
		try{
			 sessionFactory.getCurrentSession().createSQLQuery("delete from testrunplan_has_runconfig where testRunPlanId="+testrunplanid+" and runConfigurationId="+runconfigid).executeUpdate();
		} catch(Exception e){
			log.error("Unable to unamp runconfiguration for test run plan", e);
		}		
	}
	
	@Override
	@Transactional
	public boolean isTestConfigurationTestCaseAlreadyMapped(Integer testConfigurationId, Integer testSuiteId,Integer testCaseId){
		boolean flag = false;
		int totalTestCaseCountForATestConfiguration = 0;
		String sql= "";
		try {
			List<RunConfigurationTSHasTC> testConfigurationTSHasTCList=null;
			RunConfigurationTSHasTC testConfigurationTSHasTC=null;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfigurationTSHasTC.class, "tctstc");
			c.createAlias("tctstc.runConfiguration", "trp");
			c.createAlias("tctstc.testSuiteList", "tsl");
			c.createAlias("tctstc.testCaseList", "tcl");

			c.add(Restrictions.eq("trp.runconfigId",testConfigurationId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			c.add(Restrictions.eq("tcl.testCaseId",testCaseId));
			
			testConfigurationTSHasTCList = c.list();
			
			if(testConfigurationTSHasTCList.size() > 0){
				flag = true;
			}
		}catch(Exception e){
			log.error("Error in obtaining Test Configuration Test Suite Test Cases " ,e);
		}
		return flag;
	}
	
	public RunConfiguration getRunConfigurationById(Integer runConfigId) {
		RunConfiguration runconfig = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			c.add(Restrictions.eq("rc.runconfigId",runConfigId));
			runconfig = (RunConfiguration)c.uniqueResult();			
		}catch(Exception ex){
			log.error("Unable to get product ", ex);
		}		
		return runconfig;
	}
	
	@Override
	@Transactional
	public void mapTestConfigurationTestSuiteTestCase(int testConfigId, int testSuiteId, List<TestCaseList> testCaseLists, String type) {
		try {
			if (testCaseLists == null)
				return;
			
			if(type.equals("Add")){
				for (TestCaseList testCaseList : testCaseLists) {
					RunConfigurationTSHasTC testConfigurationTSHasTC = new RunConfigurationTSHasTC();	
					testConfigurationTSHasTC.setRunConfiguration(getRunConfigurationById(testConfigId));
					TestSuiteList testSuiteList = testSuiteListDAO.getByTestSuiteId(testSuiteId);
					testConfigurationTSHasTC.setTestSuiteList(testSuiteList);
					testConfigurationTSHasTC.setTestCaseList(testCaseList);
					sessionFactory.getCurrentSession().save(testConfigurationTSHasTC);
				}
			}		
		} catch(Exception e) {
			log.error("Error in adding test plan test suite testcase list to a test configuration" , e);
		}
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId, Integer hostId, Integer deviceId) {
		log.debug("listing all RunConfiguration instance");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();


		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.testRunPlan", "tp");
			c.createAlias("rc.environmentcombination", "ec");
			c.createAlias("rc.genericDevice", "gd");
			c.createAlias("rc.hostList", "hl");

			c.add(Restrictions.eq("ec.environment_combination_id", environmentCombinationId));
			
			c.add(Restrictions.eq("tp.testRunPlanId", testRunPlanId));
			c.add(Restrictions.eq("rc.runConfigStatus",1));
			c.add(Restrictions.eq("gd.genericsDevicesId", deviceId));
			c.add(Restrictions.eq("hl.hostId", hostId));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
					
					if(rc.getTestSuiteLists() != null){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
					
					if(type==2){
						if(rc.getGenericDevice()!=null && rc.getGenericDevice().getGenericsDevicesId()!=null){
							runConfigurationListFinal.add(rc);

						}else{
							runConfigurationListFinal.add(rc);
						}
					}else{
						
						runConfigurationListFinal.add(rc);
					}
				}
				
				runConfigurations.addAll(runConfigurationListFinal);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurations;

	}


	@Override
	@Transactional
	public boolean hostHasTestConfigurations(int productId, int hostId) {
		log.debug("Checking hostHasTestConfigurations");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();


		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.product", "pm");
			c.createAlias("rc.hostList", "hl");

			
			c.add(Restrictions.eq("pm.productId", productId));
			c.add(Restrictions.eq("hl.hostId", hostId));

			runConfigurationList = c.list();
			if(runConfigurationList != null && !runConfigurationList.isEmpty() && runConfigurationList.size() > 0)
				return true;
			else
				return false;
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return false;
	}


	@Override
	@Transactional
	public boolean deviceHasTestConfigurations(int productId, int deviceId) {
		log.debug("Checking deviceHasTestConfigurations");
		Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;
		List<RunConfiguration> runConfigurationListFinal=new ArrayList<RunConfiguration>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.product", "pm");
			c.createAlias("rc.genericDevice", "gd");
			c.add(Restrictions.eq("pm.productId", productId));
			c.add(Restrictions.eq("gd.genericsDevicesId", deviceId));

			runConfigurationList = c.list();
			
			if(runConfigurationList != null && !runConfigurationList.isEmpty() && runConfigurationList.size() > 0)
				return true;
			else
				return false;
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return false;
	}


	@Override
	@Transactional
	public List<TestRunPlan> getTestPlansByProductBuildIds(String productBuildIds) {
		log.debug("Retrieving Test Plans for the Product Builds : " +productBuildIds);
		List<TestRunPlan> testPlans = new ArrayList<TestRunPlan>();
		List<Integer> pbIds = new ArrayList<Integer>();
		try{
			for(String id : productBuildIds.split(",")){
				pbIds.add(Integer.valueOf(id));
			}
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "tp");	
			c.createAlias("tp.productBuild","productBuild");
			c.add(Restrictions.in("productBuild.productBuildId", pbIds));
			testPlans = c.list();
			for(TestRunPlan testPlan : testPlans) {
				Hibernate.initialize(testPlan.getTestSuiteLists());
				for(TestSuiteList ts : testPlan.getTestSuiteLists()) {
					Hibernate.initialize(ts.getTestCaseLists());
				}
				
				if(testPlan.getRunConfigurationList() != null) {
					Hibernate.initialize(testPlan.getRunConfigurationList());		
					for(RunConfiguration testConfig : testPlan.getRunConfigurationList()) {
						Hibernate.initialize(testConfig.getTestSuiteLists());
						for(TestSuiteList ts : testConfig.getTestSuiteLists()) {
							Hibernate.initialize(ts.getTestCaseLists());
						}
					}
				}
			}
			log.info("Size of test plans : "+testPlans.size());
			
		} catch(Exception e) {
			log.error("Exception : ",e);
			return testPlans;
		}
		return testPlans;
	}
	@Override
	@Transactional
	public ProductType getProductTypeByName(String productTypeName) {
		log.debug("getting getProductType instance by Name");
		ProductType productType=null;
		List<ProductType> productTypeList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductType.class, "pt");
			c.add(Restrictions.eq("pt.typeName", productTypeName).ignoreCase());
			productTypeList = c.list();
			if(productTypeList.size() > 0 && !productTypeList.isEmpty())
				productType = productTypeList.get(0);
			log.debug("getProductTypeById successful");
		} catch (RuntimeException re) {
			log.error("getProductTypeById failed", re);
		}
		return productType;
        
	}


	@Override
	@Transactional
	public List<RunConfiguration> getRunConfigurationListByEnvironmentCombination(Integer environmentCombinationId) {
		log.debug("listing all RunConfiguration instance");
		//List<RunConfiguration> runConfigurations=new ArrayList<RunConfiguration>();
		List<RunConfiguration> runConfigurationList=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.createAlias("rc.environmentcombination", "ec");
			c.add(Restrictions.eq("ec.environment_combination_id", environmentCombinationId));
			c.add(Restrictions.eq("rc.runConfigStatus",1));
			runConfigurationList = c.list();
			if(runConfigurationList!=null && !runConfigurationList.isEmpty()){
				for (RunConfiguration rc : runConfigurationList) {
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					if(rc.getGenericDevice()!=null){
						Hibernate.initialize(rc.getGenericDevice().getHostList());
						if(rc.getGenericDevice().getHostList()!=null)
							Hibernate.initialize(rc.getGenericDevice().getHostList());
					}
					Hibernate.initialize(rc.getTestRunPlan());
					Hibernate.initialize(rc.getHostList());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(rc.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
					
					if(rc.getTestSuiteLists() != null){
						Hibernate.initialize(rc.getTestSuiteLists());
						for(TestSuiteList tsl : rc.getTestSuiteLists()){
							if(tsl.getTestCaseLists()!=null){
								Hibernate.initialize(tsl.getTestCaseLists());
							}
						}
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigurationList;
	}


	@Override
	public ProductMaster getProductExitsInsameTestFactory(
			Integer testFactoryId, String productName) {
		ProductMaster productMaster=null;
		try {
			String hql = "from ProductMaster pur where pur.testFactory.testFactoryId = :testFactoryId and pur.productName=:productName";
			List product = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryId", testFactoryId).setParameter("productName", productName).list();
			if(product != null && product.size() >0) {
				productMaster=(ProductMaster)product.get(0);
			}
			
		}catch(RuntimeException re) {
			log.error("Error in getProductExitsInsameTestFactory",re);
		}
		return productMaster;
	}
	
	@Override
	@Transactional
	public List<ProductMaster> getProductDetailsByUserId(int userId) {
		List<ProductUserRole> productUserRoles= null;
		List<ProductMaster> products= new ArrayList<>();
		try {
			String hql = "from ProductUserRole pur where pur.user.userId=:userId and status=1 ";
			productUserRoles = sessionFactory.getCurrentSession().createQuery(hql).setParameter("userId", userId).list();
			if(productUserRoles != null && productUserRoles.size() >0) {
				for(ProductUserRole userRole:productUserRoles) {
					products.add(userRole.getProduct());
				}
			}
			
		}catch(RuntimeException re) {
			log.error("Error in ");
		}
		return products;
	}
	@Override
	@Transactional
	public List<TestCaseList> getRunConfigTestCasesByTestSuite(Integer testcaseId) {
		log.info("listing all TestCaseList instance");		
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
		List<RunConfigurationTSHasTC> runConfigTSHasTCList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfigurationTSHasTC.class, "rctstc");
			c.createAlias("rctstc.testCaseList", "tc");
			c.add(Restrictions.eq("tc.testCaseId",testcaseId));
			c.addOrder(Order.asc("tc.testCaseId"));
			runConfigTSHasTCList = c.list();
			for (RunConfigurationTSHasTC runConfigTSHasTC : runConfigTSHasTCList) {
				Hibernate.initialize(runConfigTSHasTC.getTestCaseList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList().getTestCaseLists());
				Hibernate.initialize(runConfigTSHasTC.getRunConfiguration());
				testCaseList.add(runConfigTSHasTC.getTestCaseList());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testCaseList;
	}
	@Override
	@Transactional
	public List<RunConfigurationTSHasTC> getRunConfigTestCaseObjectByTestSuite(Integer testcaseId) {
		log.info("listing all TestCaseList instance");		
		List<RunConfigurationTSHasTC> runConfigTSHasTCList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfigurationTSHasTC.class, "rctstc");
			c.createAlias("rctstc.testCaseList", "tc");
			c.add(Restrictions.eq("tc.testCaseId",testcaseId));
			c.addOrder(Order.asc("tc.testCaseId"));
			runConfigTSHasTCList = c.list();
			for (RunConfigurationTSHasTC runConfigTSHasTC : runConfigTSHasTCList) {
				Hibernate.initialize(runConfigTSHasTC.getTestCaseList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList());
				Hibernate.initialize(runConfigTSHasTC.getTestSuiteList().getTestCaseLists());
				Hibernate.initialize(runConfigTSHasTC.getRunConfiguration());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return runConfigTSHasTCList;
	}


	
	@Override
	@Transactional
	public boolean isUserPermissionByProductIdandUserId(Integer productId,
			Integer userId, Integer roleId) {
		try{
			String hql = "from ProductUserRole pur where pur.product.productId = :productId and pur.user.userId=:userId  and status=1 ";
			List productUserRole = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productId", productId).setParameter("userId", userId).list();
			if (productUserRole == null || productUserRole.isEmpty()) { 
			    return false;
			}else{ 
				return true;
			}	
		
		}catch(RuntimeException re) {
			log.error("Error in isUserPermissionByProductIdandUserId",re);
		}
	return false;
	}


	@Override
	@Transactional
	public TestRunPlan getTestRunPlanBytestRunPlanNameAndProductBuild(String testRunPlanName, Integer productBuildId) {

		log.debug("listing all TestRunPlan instance");
		List<TestRunPlan> testRunPlanList=null;

		TestRunPlan testRunPlan=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlan.class, "trplan");
			
			c.add(Restrictions.like("trplan.testRunPlanName", testRunPlanName, MatchMode.ANYWHERE));
			if(productBuildId != -1){
				c.createAlias("trplan.productBuild","productBuild");
				c.add(Restrictions.eq("productBuild.productBuildId", productBuildId));
			}
			
			testRunPlanList = c.list();
			testRunPlan=(testRunPlanList!=null && testRunPlanList.size()!=0)?(TestRunPlan)testRunPlanList.get(0):null;
			if(testRunPlan!=null){
				Hibernate.initialize(testRunPlan.getExecutionType());
				Hibernate.initialize(testRunPlan.getTestToolMaster());

				Hibernate.initialize(testRunPlan.getProductVersionListMaster());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getProductMode());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getHostLists());
				Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getGenericeDevices());

				Set<RunConfiguration> runconfigSet = testRunPlan.getRunConfigurationList();
				Hibernate.initialize(runconfigSet);
					
				for (RunConfiguration runConfiguration : runconfigSet) {
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getHostList());
					if( runConfiguration.getGenericDevice()!=null){
						Hibernate.initialize(runConfiguration.getGenericDevice().getHostList());
						GenericDevices devices = runConfiguration.getGenericDevice();
						Hibernate.initialize(devices);
						Hibernate.initialize(devices.getDeviceLab());
						Hibernate.initialize(devices.getDeviceModelMaster());
						Hibernate.initialize(devices.getPlatformType());
						Hibernate.initialize(devices.getProductMaster());
						Hibernate.initialize(devices.getTestFactory());
						if((devices instanceof MobileType) && ((MobileType) devices).getDeviceMakeMaster() != null){
							Hibernate.initialize(((MobileType) devices).getDeviceMakeMaster());
						}
						if((devices instanceof ServerType) ){
							if(((ServerType) devices).getProcessor() != null){
								Hibernate.initialize(((ServerType) devices).getProcessor());	
							}
							if(((ServerType) devices).getSystemType() != null){
								Hibernate.initialize(((ServerType) devices).getSystemType());	
							}						
						}
					}
					Hibernate.initialize(runConfiguration.getProduct());
					Hibernate.initialize(runConfiguration.getProductVersion());
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration.getTestSuiteLists());
				}
				Hibernate.initialize(testRunPlan.getTestSuiteLists());		
				Hibernate.initialize(testRunPlan.getFeatureList());		
				Hibernate.initialize(testRunPlan.getTestCaseList());		
				Set<TestCaseList> testcaseList1=testRunPlan.getTestCaseList();
				if(testcaseList1.size()!=0){
					for (TestCaseList tcl : testcaseList1) {
						if(tcl!=null){
							Hibernate.initialize(tcl);
							if(tcl.getProductFeature()!=null){
								Hibernate.initialize(tcl.getProductFeature());
							}
							if(tcl.getDecouplingCategory()!=null){
								Hibernate.initialize(tcl.getDecouplingCategory());
							}
							
							
						}
						
						
					}
				}
				
				Set<TestSuiteList> testSuiteSet = testRunPlan.getTestSuiteLists();
				Hibernate.initialize(testSuiteSet);		
				for (TestSuiteList testSuiteList : testSuiteSet) {
					Hibernate.initialize(testSuiteList.getExecutionTypeMaster());
					Hibernate.initialize(testSuiteList.getProductMaster());
					Hibernate.initialize(testSuiteList.getProductVersionListMaster());
					Hibernate.initialize(testSuiteList.getScriptTypeMaster());
					Set<TestCaseList> testcaseList = testSuiteList.getTestCaseLists();
					Hibernate.initialize(testcaseList);
					for (TestCaseList testCaseListObj : testcaseList) {
						Hibernate.initialize(testCaseListObj.getTestCaseStepsLists());
						if(testCaseListObj.getProductFeature()!=null){
							Hibernate.initialize(testCaseListObj.getProductFeature());
						}
						
					}
					
					
					Hibernate.initialize(testSuiteList.getTestExecutionResults());
					Hibernate.initialize(testSuiteList.getTestRunPlanList());
					Hibernate.initialize(testRunPlan.getTestCaseList());
					Set<TestCaseList> testcaseSet=testRunPlan.getTestCaseList();
					for (TestCaseList tcl : testcaseSet) {
						Hibernate.initialize(tcl.getProductFeature());
						Hibernate.initialize(tcl.getDecouplingCategory());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
					Set<ProductFeature> featureSet=testRunPlan.getFeatureList();
					for (ProductFeature productFeature : featureSet) {
						Hibernate.initialize(productFeature.getTestCaseList());
					}
					Hibernate.initialize(testRunPlan.getFeatureList());
				}
			}						
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testRunPlan;
		
	}


	@Override
	@Transactional
	public TestRunPlan getFirstTestRunPlanByTestPlanGroupId(Integer testPlanGroupId) {
		List<TestRunPlangroupHasTestRunPlan> trpgmList = new ArrayList<TestRunPlangroupHasTestRunPlan>();
		TestRunPlangroupHasTestRunPlan trpgm = null;
		TestRunPlan testPlan = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlangroupHasTestRunPlan.class, "trpgm");
		c.createAlias("trpgm.testRunPlanGroup", "trpg");
		c.createAlias("trpgm.testrunplan", "trp");

		c.add(Restrictions.eq("trpg.testRunPlanGroupId", testPlanGroupId));
		//c.add(Restrictions.eq("trpgm.executionOrder", executionOrder+1));
		c.addOrder(Order.asc("trpgm.executionOrder"));
		trpgmList=c.list();
		trpgm = (trpgmList != null && trpgmList.size() != 0) ? (TestRunPlangroupHasTestRunPlan) trpgmList.get(0) : null;
		if(trpgm!=null && trpgm.getTestrunplan() != null){
			testPlan = getTestRunPlanById(trpgm.getTestrunplan().getTestRunPlanId());
		}
		return testPlan;
	}


	@Override
	@Transactional
	public TestRunPlangroupHasTestRunPlan getTestRunPlanGroupHasTestPlanByTestPlanId(Integer testRunPlanId) {
		List<TestRunPlangroupHasTestRunPlan> trpgmList = new ArrayList<TestRunPlangroupHasTestRunPlan>();
		TestRunPlangroupHasTestRunPlan trpgm = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlangroupHasTestRunPlan.class, "trpgm");
		c.createAlias("trpgm.testrunplan", "trp");
		c.add(Restrictions.eq("trp.testRunPlanId", testRunPlanId));
		trpgmList=c.list();
		trpgm = (trpgmList != null && trpgmList.size() != 0) ? (TestRunPlangroupHasTestRunPlan) trpgmList.get(0) : null;
		
		return trpgm;
	}


	@Override
	@Transactional
	public TestRunPlanTSHasTC getTestPlanTestSuiteTestCase(Integer testRunPlanId, Integer testSuiteId, Integer testCaseId) {
		List<TestRunPlanTSHasTC> tpTsTcList = new ArrayList<TestRunPlanTSHasTC>();
		TestRunPlanTSHasTC  tpTsTc = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlanTSHasTC.class, "tptstc");
		c.createAlias("tptstc.testrunplan", "trp");
		c.add(Restrictions.eq("trp.testRunPlanId", testRunPlanId));
		c.createAlias("tptstc.testSuiteList", "tsl");
		c.add(Restrictions.eq("tsl.testSuiteId", testSuiteId));
		c.createAlias("tptstc.testCaseList", "tcl");
		c.add(Restrictions.eq("tcl.testCaseId", testCaseId));
		tpTsTcList=c.list();
		tpTsTc = (tpTsTcList != null && tpTsTcList.size() != 0) ? (TestRunPlanTSHasTC) tpTsTcList.get(0) : null;
		
		return tpTsTc;
	}
}