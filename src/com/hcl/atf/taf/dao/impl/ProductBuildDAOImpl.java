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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;


@Repository
public class ProductBuildDAOImpl implements ProductBuildDAO {
	private static final Log log = LogFactory.getLog(ProductBuildDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<ProductBuild> list() {
		log.debug("listing all Product Builds instance");
		List<ProductBuild> productBuild=null;
		try {
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild where Status='1' ").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return productBuild;
	}

	@Override
	@Transactional
	public List<ProductBuild> list(int productVersionMasterListId) {
		log.debug("listing specific ProductBuild instance");
		List<ProductBuild> productBuild=null;
		try {
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild pb where pb.status=1 and pb.productVersion.productVersionListId=:productVersionMasterListId")
														.setParameter("productVersionMasterListId", productVersionMasterListId).list();
			if (!(productBuild == null || productBuild.isEmpty())){
				for (ProductBuild pbuild : productBuild) {
					Hibernate.initialize(pbuild.getProductVersion());
					Hibernate.initialize(pbuild.getBuildType());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productBuild;
	}


	@Override
	@Transactional
	public List<ProductBuild> listProductBuilds(int productVersionMasterListId, int status) {
		log.debug("listing specific ProductBuild instance");
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return list(productVersionMasterListId);
		}
		List<ProductBuild> productBuild=null;
		try {
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild pb where (pb.productVersion.productVersionListId=:productVersionListId) and Status='1' ")
														.setParameter("productVersionListId", productVersionMasterListId)
														.list();
			if (!(productBuild == null || productBuild.isEmpty())){
				for (ProductBuild pbuild : productBuild) {
					Hibernate.initialize(pbuild.getProductVersion());	
					Hibernate.initialize(pbuild.getBuildType());
					Hibernate.initialize(pbuild.getClonedBuild());	
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productBuild;
	}


	@Override
	@Transactional
	public Integer add(ProductBuild productBuild) {
		log.debug("adding Product Build instance");
		try {
			productBuild.setStatus(1);
			sessionFactory.getCurrentSession().save(productBuild);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return productBuild.getProductBuildId();
	}



	@Override
	@Transactional
	public void update(ProductBuild productBuild) {
		log.debug("updating ProductBuild instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(productBuild);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}



	@Override
	@Transactional
	public void delete(ProductBuild productBuild) {
		log.info("PBDAOImpl"+productBuild);
		log.debug("reactivate ProductBuild instance");
		try {
			
			sessionFactory.getCurrentSession().delete(productBuild);
			log.debug("reactivate successful");
		} catch (RuntimeException re) {
			log.error("reactivate failed", re);
		}		
		
		
	}
	
	@Override
	@Transactional
	public void reactivate(int productBuildId) {
		log.debug("reactivate ProductVersionListMaster instance");
		try {
			ProductBuild productBuild = getByProductBuildId(productBuildId, 0);
			update(productBuild);
			log.debug("reactivate successful");
		} catch (RuntimeException re) {
			log.error("reactivate failed", re);
		}		
	}

	@Override
	@Transactional
	public List<ProductBuild> list(int startIndex, int pageSize) {
		log.debug("listing all ProductBuild instance");
		List<ProductBuild> productBuild=null;
		try {
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild")
					.setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productBuild;
	}

	@Override
	@Transactional
	public List<ProductBuild> list(int productVersionMasterListId, int startIndex,
			int pageSize) {
		log.debug("listing all ProductBuild instance");
		List<ProductBuild> productBuild=null;
		try {
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild where ProductVersion=:ProductVersion")
					.setParameter("ProductVersion", productVersionMasterListId).setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productBuild;
	}

	@Override
	@Transactional
	public ProductBuild getByProductBuildId(int productBuildId, int listOrObjInitialize) {
		log.debug("getting ProductVersionListMaster instance by id");
		ProductBuild productBuild=null;
		try {
			List<ProductBuild> productBuildList=null;
			if(listOrObjInitialize ==1){
				productBuildList = sessionFactory.getCurrentSession().createQuery("from ProductBuild p where productBuildId=:productBuildId").setParameter("productBuildId", productBuildId).list();
				if (!(productBuildList == null || productBuildList.isEmpty())){
					for (ProductBuild pbuild : productBuildList) {						
						Hibernate.initialize(pbuild.getProductVersion());
						Hibernate.initialize(pbuild.getBuildType());
						Hibernate.initialize(pbuild.getProductVersion().getProductMaster());
						if(pbuild.getWorkPackage() != null){
							Hibernate.initialize(pbuild.getWorkPackage());
						}					
					}
				}
				productBuild = (productBuildList!=null && productBuildList.size()!=0)?(ProductBuild)productBuildList.get(0):null;
			}else{
				List list = sessionFactory.getCurrentSession().createQuery("from ProductBuild p where productBuildId=:productBuildId").setParameter("productBuildId", productBuildId).list();
				productBuild = (list!=null && list.size()!=0)?(ProductBuild)list.get(0):null;
				if (productBuild != null) {
					Hibernate.initialize(productBuild.getProductVersion());
					Hibernate.initialize(productBuild.getBuildType());
					Hibernate.initialize(productBuild.getProductVersion().getProductMaster());
					if(productBuild.getWorkPackage() != null){
						Hibernate.initialize(productBuild.getWorkPackage());
					}
				}
			}		
			
			
			log.debug("getProductBuild successful");
		} catch (RuntimeException re) {
			log.error("getProductBuild failed", re);
		}
		return productBuild;
        
	}
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting ProductBuild total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from product_build").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<ProductBuild> list(int productVersionListId, String[] parameters) {
		log.debug("listing parameterized ProductBuild instance");
		List<ProductBuild> productBuild=null;
		try {
			StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("p."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
			
			productBuild=sessionFactory.getCurrentSession().createQuery("from ProductBuild p where ProductVersion=:ProductVersion")
			.setParameter("ProductVersion",productVersionListId)
	        .list();
			if (!(productBuild == null || productBuild.isEmpty())){
				for (ProductBuild pbuild : productBuild) {
					Hibernate.initialize(pbuild.getProductVersion());
				}
			}
			log.debug("list successful");
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productBuild;    
	}
	
	public boolean isProductBuildExistingByName(ProductBuild productBuild) {
		
		String hql = "from ProductBuild c where c.buildname = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", productBuild.getBuildname().trim()).list();
		if (instances == null || instances.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public ProductBuild productBuildByName(String productBuildName) {
		log.debug("listing specific ProductBuild instance");
		ProductBuild productBuild=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from ProductBuild where buildname=:buildname")
														.setParameter("buildname", productBuildName).list();
			productBuild=(list!=null && list.size()!=0)?(ProductBuild)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productBuild;
	}

	@Override
	@Transactional
	public List<ProductBuild> getUserAssociatedProductBuilds(int userRoleId,int userId) {
		List<WorkPackage> listOfWorkPackages = null;
		List<ProductBuild> listOfProductBuilds = new ArrayList<ProductBuild>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wp.productBuild", "pb");
			c.add(Restrictions.eq("pb.status", 1));
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
				if(!listOfProductBuilds.contains(workPackage.getProductBuild())){
					listOfProductBuilds.add(workPackage.getProductBuild());
				}
			}
			log.info("Result Set Size of Product Builds : " + listOfProductBuilds.size());
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfProductBuilds;
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfigurationById(Integer runconfigId) {
		log.info("getting getRunConfigurationById instance by id");
		RunConfiguration runConfiguration=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where runconfigId=:runconfigId").setParameter("runconfigId", runconfigId).list();
			
			runConfiguration = (list!=null && list.size()!=0)?(RunConfiguration)list.get(0):null;
			if (runConfiguration != null) {
				if(runConfiguration.getEnvironmentcombination() != null){
					EnvironmentCombination ecomb = runConfiguration.getEnvironmentcombination();
					Hibernate.initialize(ecomb);
					if(ecomb.getProductMaster() != null){
						ProductMaster prod = ecomb.getProductMaster();
						Hibernate.initialize(prod);
						if(prod.getProductType() != null){
							Hibernate.initialize(prod.getProductType());
						}
					}
				}
				
				Hibernate.initialize(runConfiguration.getGenericDevice());
				if (runConfiguration.getHostList() != null) {
					HostList hl = runConfiguration.getHostList();
					Hibernate.initialize(hl);					
					if(hl.getCommonActiveStatusMaster() != null){
						CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
						Hibernate.initialize(casm);
					}
					if(hl.getHostTypeMaster() != null){
						HostTypeMaster htm = hl.getHostTypeMaster();
						Hibernate.initialize(htm);
					}
					if(hl.getHostPlatformMaster() != null){
						HostPlatformMaster hpfm = hl.getHostPlatformMaster();
						Hibernate.initialize(hpfm);
					}					
				}
				if(runConfiguration.getGenericDevice() != null){
					GenericDevices gd = runConfiguration.getGenericDevice();
					
					if(gd.getHostList() != null){
						HostList hl = gd.getHostList(); 
						Hibernate.initialize(hl);
						if(hl.getCommonActiveStatusMaster() != null){
							CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}						
				}
				Hibernate.initialize(runConfiguration.getProduct());
				
				if(runConfiguration.getTestRunPlan() != null){
					TestRunPlan trunplan = runConfiguration.getTestRunPlan();
					Hibernate.initialize(trunplan);					
					if(trunplan.getProductVersionListMaster() != null){
						ProductVersionListMaster version = trunplan.getProductVersionListMaster();
						Hibernate.initialize(version);						
						if(version.getProductMaster() != null){
							ProductMaster prod = version.getProductMaster();
							Hibernate.initialize(prod);
							if(prod.getProductType() != null){
								ProductType ptype = prod.getProductType();
								Hibernate.initialize(ptype);								
							}
						}
					}
				}
				Set<WorkpackageRunConfiguration> wpRunConfig=runConfiguration.getWorkPackageRunConfigSet();
				if(wpRunConfig.size()!=0){
					Hibernate.initialize(runConfiguration.getWorkPackageRunConfigSet());
				for(WorkpackageRunConfiguration wpRunConf:wpRunConfig){
					Hibernate.initialize(wpRunConf.getRunconfiguration());
					Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=wpRunConf.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
						if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
							if(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage()!=null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
							}
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
						}
						Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
						Hibernate.initialize(environments);
						for(Environment environment:environments){
							Hibernate.initialize(environment.getEnvironmentCategory());
							Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
						}
					}
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
						Set<TestCaseConfiguration> testCaseConfigurationSet =workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
						for(TestCaseConfiguration testCaseConfiguration:testCaseConfigurationSet){
							Hibernate.initialize(testCaseConfiguration.getWorkpackage_run_list());
							Hibernate.initialize(testCaseConfiguration.getEnvironmentCombination());
						}
					}
						
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
						TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
						if(trcRes != null && trcRes.getTestStepExecutionResultSet() != null) {
							Set<TestStepExecutionResult> tcstepexecRes=  trcRes.getTestStepExecutionResultSet();
							if(tcstepexecRes != null && tcstepexecRes.size() != 0){
								for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
									if(testStepExecRes.getTestCaseExecutionResult() != null)
										Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
								}
							}
						}
					}
				
				}
				}
				if(runConfiguration.getTestSuiteLists() != null){
					Hibernate.initialize(runConfiguration.getTestSuiteLists());
				}
			}
			log.info("getRunConfigurationById successful");
		} catch (RuntimeException re) {
			log.info("getRunConfigurationById failed", re);
		}
		return runConfiguration;
        
	}

	@Override
	@Transactional
	public ProductBuild getLatestProductBuild(Integer productId) {
		log.info("listing specific ProductBuild instance");
		ProductBuild productBuild=null;
		List<ProductBuild> productBuilds= new ArrayList<ProductBuild>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBuild.class, "pb");
			c.createAlias("pb.productVersion", "pv");
			c.createAlias("pv.productMaster", "pm");
			c.add(Restrictions.eq("pm.productId", productId));
			c.add(Restrictions.eq("pb.status", 1));
			c.addOrder(Order.desc("pb.buildDate"));
			
			productBuilds=c.list();
			productBuild=(productBuilds!=null && productBuilds.size()!=0)?(ProductBuild)productBuilds.get(0):null;
			log.info("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productBuild;
	}

	@Override
	@Transactional
	public ProductBuild getProductBuildIdWithCompleteInitialization(
			int productBuildId) {
		log.debug("getting ProductVersionListMaster instance by id");
		ProductBuild productBuild=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ProductBuild p where productBuildId=:productBuildId").setParameter("productBuildId", productBuildId).list();
			productBuild = (list!=null && list.size()!=0)?(ProductBuild)list.get(0):null;
			if (productBuild != null) {
				Hibernate.initialize(productBuild.getProductVersion().getProductMaster());
			}
			log.debug("getProductBuild successful");
		} catch (RuntimeException re) {
			log.error("getProductBuild failed", re);
		}
		return productBuild;
	}

	@Override
	@Transactional
	public List<ProductBuild> listBuildsByProductId(int productId) {
		List<ProductBuild> listOfProductBuild = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBuild.class,"pb");
			c.createAlias("pb.productVersion", "pv");
			c.createAlias("pv.productMaster", "product");
			c.add(Restrictions.eq("pv.status",1));
			c.add(Restrictions.eq("product.productId",new Integer(productId)));
			c.add(Restrictions.eq("pb.status",1));
			listOfProductBuild = c.list();
			if (!(listOfProductBuild == null || listOfProductBuild.isEmpty())){
				for (ProductBuild productBuild : listOfProductBuild) {
					Hibernate.initialize(productBuild.getBuildType());
				}
			}
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfProductBuild;
	}
	
	@Override
	@Transactional
	public Integer countAllProductBuilds(Date startDate,Date endDate) {
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBuild.class,"productBuild");
			
			
			if (startDate != null) {
				c.add(Restrictions.ge("productBuild.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productBuild.createdDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all ProductBuild", e);
			return -1;
		}
	}
	
	
	@Override
	@Transactional
	public Integer countAllProductTestCases(Date startDate,Date endDate) {
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testCaseList");
			if (startDate != null) {
				c.add(Restrictions.ge("testCaseList.testCaseCreatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("testCaseList.testCaseCreatedDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all TestCaseList", e);
			return -1;
		}
	}
	
	
	
	
	@Override
	@Transactional
	public List<ProductBuild> listAllProductBuildByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		log.debug("listing all ProductBuild");
		List<ProductBuild> productBuilds=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBuild.class, "productBuild");
			
			if (startDate != null) {
				c.add(Restrictions.ge("productBuild.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("productBuild.createdDate", endDate));
			}
			
			c.addOrder(Order.asc("productBuildId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            productBuilds = c.list();		
			
			if (!(productBuilds == null || productBuilds.isEmpty())){
				for (ProductBuild productBuild : productBuilds) {
					Hibernate.initialize(productBuild.getProductVersion());	
					Hibernate.initialize(productBuild.getBuildType());
					Hibernate.initialize(productBuild.getClonedBuild());	
					Hibernate.initialize(productBuild.getProductBuildId());	
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productBuilds;
	}
	

	
	@Override
	@Transactional
	public List<ProductBuild> getProductBuildDetailsByVersionIdAndDayORWeek(int productVersionMasterListId,String filter) {
		log.debug("listing specific getProductBuildDetailsByVersionIdAndDayORWeek instance");
		List<ProductBuild> productBuildList=new ArrayList<ProductBuild>();
		try {
			
			String sql="";
			if(filter.equalsIgnoreCase("Day")) {
				
				sql="SELECT * FROM product_build WHERE productVersionId="+productVersionMasterListId+" AND buildDate BETWEEN (CURRENT_DATE - INTERVAL '1 DAY') AND (CURRENT_DATE+INTERVAL '1 DAY')  and status=1 GROUP BY productBuildId,productId order by buildDate desc ";
				
			} else if(filter.equalsIgnoreCase("Week")) {
				sql="SELECT * FROM product_build WHERE productVersionId="+productVersionMasterListId+" AND buildDate BETWEEN (CURRENT_DATE - INTERVAL '1 WEEK') AND (CURRENT_DATE+INTERVAL '1 DAY')  and status=1 GROUP BY productBuildId,productId order by buildDate desc";
				
			}else if(filter.equalsIgnoreCase("Month")) {
				sql="SELECT * FROM product_build WHERE productVersionId="+productVersionMasterListId+" AND buildDate BETWEEN (CURRENT_DATE - INTERVAL '1 MONTH') AND (CURRENT_DATE+INTERVAL '1 DAY')  and status=1 GROUP BY productBuildId,productId order by buildDate desc";
				
			}else{
				return productBuildList;
			}
			
			 List<Object[]> productBuilds=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			 
			 if(productBuilds != null && productBuilds.size() >0) {
				 for(Object[] build:productBuilds) {
					 Integer buildId =(Integer)build[0];
					 productBuildList.add(getByProductBuildId(buildId, 0));
				 }
			 }
		} catch (RuntimeException re) {
			log.error("list getProductBuildDetailsByVersionIdAndDayORWeek failed", re);
		}
		return productBuildList;
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfigurationByIdWithoutInitialization(Integer runconfigId) {
		log.info("getting getRunConfigurationByIdWithoutInitialization instance by id");
		RunConfiguration runConfiguration=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where runconfigId=:runconfigId").setParameter("runconfigId", runconfigId).list();
			
			runConfiguration = (list!=null && list.size()!=0)?(RunConfiguration)list.get(0):null;
			if (runConfiguration != null) {
				if(runConfiguration.getEnvironmentcombination() != null){
					EnvironmentCombination ecomb = runConfiguration.getEnvironmentcombination();
					Hibernate.initialize(ecomb);
					if(ecomb.getProductMaster() != null){
						ProductMaster prod = ecomb.getProductMaster();
						Hibernate.initialize(prod);
						if(prod.getProductType() != null){
							Hibernate.initialize(prod.getProductType());
						}
					}
				}
				
				Hibernate.initialize(runConfiguration.getGenericDevice());
				if (runConfiguration.getHostList() != null) {
					HostList hl = runConfiguration.getHostList();
					Hibernate.initialize(hl);					
					if(hl.getCommonActiveStatusMaster() != null){
						CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
						Hibernate.initialize(casm);
					}
					if(hl.getHostTypeMaster() != null){
						HostTypeMaster htm = hl.getHostTypeMaster();
						Hibernate.initialize(htm);
					}
					if(hl.getHostPlatformMaster() != null){
						HostPlatformMaster hpfm = hl.getHostPlatformMaster();
						Hibernate.initialize(hpfm);
					}					
				}
				if(runConfiguration.getGenericDevice() != null){
					GenericDevices gd = runConfiguration.getGenericDevice();
					
					if(gd.getHostList() != null){
						HostList hl = gd.getHostList(); 
						Hibernate.initialize(hl);
						if(hl.getCommonActiveStatusMaster() != null){
							CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}						
				}
				Hibernate.initialize(runConfiguration.getProduct());
				
				if(runConfiguration.getTestRunPlan() != null){
					TestRunPlan trunplan = runConfiguration.getTestRunPlan();
					Hibernate.initialize(trunplan);					
					if(trunplan.getProductVersionListMaster() != null){
						ProductVersionListMaster version = trunplan.getProductVersionListMaster();
						Hibernate.initialize(version);						
						if(version.getProductMaster() != null){
							ProductMaster prod = version.getProductMaster();
							Hibernate.initialize(prod);
							if(prod.getProductType() != null){
								ProductType ptype = prod.getProductType();
								Hibernate.initialize(ptype);								
							}
						}
					}
				}
				if(runConfiguration.getTestSuiteLists() != null){
					Hibernate.initialize(runConfiguration.getTestSuiteLists());
				}
			}
			log.info("getRunConfigurationByIdWithoutInitialization successful");
		} catch (RuntimeException re) {
			log.info("getRunConfigurationByIdWithoutInitialization failed", re);
		}
		return runConfiguration;
        
	}
	
	@Override
	@Transactional
	public ProductBuild getproductBuildByProductIdAndBuildName(Integer productVersionId,String productBuildName) {
		log.debug("listing specific ProductBuild instance");
		ProductBuild productBuild=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from ProductBuild pb where pb.productVersion.productVersionListId=:productVersionListId and buildname=:buildname")
														.setParameter("productVersionListId", productVersionId)
														.setParameter("buildname", productBuildName).list();
			productBuild=(list!=null && list.size()!=0)?(ProductBuild)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return productBuild;
	}
}
