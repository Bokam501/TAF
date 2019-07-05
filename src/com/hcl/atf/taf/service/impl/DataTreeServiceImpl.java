package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.json.JSONValue;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.CustomerDAO;
import com.hcl.atf.taf.dao.DecouplingCategoryDAO;
import com.hcl.atf.taf.dao.DimensionDAO;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.ReportDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestFactoryLabDao;
import com.hcl.atf.taf.dao.TestfactoryResourcePoolDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.UserTypeMasterNewDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;

@Service
public class DataTreeServiceImpl implements DataTreeService{

	private static final Log log = LogFactory.getLog(DataTreeServiceImpl.class);

	@Autowired
    private WorkPackageDAO workPackageDAO;
	
	@Autowired
    private  ProductMasterDAO productMasterDAO;
	
	@Autowired
    private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private TestFactoryLabDao testFactoryLabDao;
	
	@Autowired
	private TestfactoryResourcePoolDAO testfactoryResourcePoolDAO;
	
	@Autowired
	private UserTypeMasterNewDAO userTypeMasterNewDAO;
	
	@Autowired
	private TestFactoryDao testFactoryDao;
	
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	
	@Autowired
	private DecouplingCategoryDAO decouplingCategoryDAO;
	
	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	
	@Autowired
	private EnvironmentDAO environmentDAO;
	
	@Autowired
	private UserListDAO userDAO;
	
	@Autowired
	private ActivityWorkPackageDAO activityWorkPackageDAO;

	@Autowired
	private ActivityDAO activityDAO;

	@Autowired
	private ActivityTaskDAO activityTaskDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private DimensionDAO dimensionDAO;
	
	@Autowired
	private WorkflowMasterDAO workflowMasterDAO;
	

	@Autowired
	private ReportDAO reportDAO;
	
	@Value("#{ilcmProps['DATA_TREE_WORKPACKAGE_DISPLAY_NAME']}")
    private String workPackages;
	
	
	
	@Override
	public String getWorkPackageTree(int userRoleId,int userId) {
		log.debug("In workPackageTreeData() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> workPackageMap = null;
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> workPackageList = null;
		
		productList = new ArrayList<HashMap<String,Object>>();
		List<TestFactory> listOfTestFactories = null;
		List<ProductMaster> listOfProducts = null;
		// Temporarily added  Resource manager and  Program manager with Admin. Need to bring in different tree for these roles.
		//This tree comes for all role in fill my availability.
		if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER){
			listOfProducts = productMasterDAO.list(false);
		}
		else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			 if(listOfTestFactories != null && listOfTestFactories.size()>0){
				 listOfProducts = new ArrayList<ProductMaster>();
				 for (TestFactory testFactory : listOfTestFactories) {
					List<ProductMaster> subListOfProducts = null;
					subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(listOfProducts.size() == 0){
						listOfProducts = subListOfProducts;
					}else{
						listOfProducts.addAll(subListOfProducts);
					}
				}
			 }
		}
		String returnvalue = "";
		if(listOfProducts != null){
			for(ProductMaster product : listOfProducts) {
				List<ProductVersionListMaster> listOfProductVariants = null;
				productBuildList = new ArrayList<HashMap<String,Object>>();
				if(product != null){
					productMap = new HashMap<String, Object>();
					productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
					listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
					for (ProductVersionListMaster productVersion : listOfProductVariants) {
						if(productVersion != null){
							List<ProductBuild> listOfProductBuilds = null;
							listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
							for (ProductBuild productBuild : listOfProductBuilds) {
								List<WorkPackage> listOfWorkPackage = null;
								if(productBuild != null){
									productBuildMap = new HashMap<String, Object>();
									productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,false,product.getProductId());
									workPackageList = new ArrayList<HashMap<String,Object>>();
									listOfWorkPackage = workPackageDAO.listWorkPackagesByProductBuild(productBuild.getProductBuildId());
									for (WorkPackage workPackageBean : listOfWorkPackage) {
										if(workPackageBean != null){
											WorkFlow workFlow=null;
										if(workPackageBean.getWorkFlowEvent()!=null){
											workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
												if(workFlow!=null){
													if(workFlow.getEntityMaster()!=null){
														if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
															int stageId=workFlow.getStageId();
															workPackageMap = new HashMap<String, Object>();
															workPackageMap = putDataInJSStateTreeMap(workPackageMap, workPackageBean.getWorkPackageId(), workPackageBean.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,false,productBuild.getProductBuildId(),stageId);
														}
													}
												}
											}
										}
										workPackageList.add(workPackageMap);
									}
								}
								productBuildMap.put("children", workPackageList);
								productBuildList.add(productBuildMap);
							}
						}
					}
					productMap.put("children", productBuildList);
					productList.add(productMap);
				}
			}
			returnvalue = JSONValue.toJSONString(productList);
		}else{
			returnvalue = null;
		}
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}
	
	
	public HashMap<String, Object> putDataInJSTreeMap(HashMap<String, Object> mapObject, long keyId, String entityName, String entityType, boolean isParent, Integer parentId){
		if(mapObject != null){
			mapObject.put("data", String.valueOf(keyId)+"~"+entityType+"~"+entityName);
			mapObject.put("text",entityName);
			HashMap<String,Object> attrMap = new HashMap<String,Object>();
			attrMap.put("key", String.valueOf(keyId));
			attrMap.put("type", entityType);
			mapObject.put("attr", attrMap);
			if(isParent){
				mapObject.put("parent","#");
			}else{
				mapObject.put("parent", Integer.toString(parentId));
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_RESOURCE_POOL)){
				mapObject.put("icon","fa fa-users");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY_LAB)){
				mapObject.put("icon","fa fa-university");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY)){
				mapObject.put("icon","fa fa-home");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				mapObject.put("icon","fa fa-cube");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_VERSION)){
				mapObject.put("icon","fa fa-cubes");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_BUILD)){
				mapObject.put("icon","fa fa-building-o");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_SHIFT)){
				mapObject.put("icon","fa fa-university");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_CUSTOMER)){
				mapObject.put("icon","fa fa-user");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY)){
				mapObject.put("icon","fa fa-list-alt");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE)){
				mapObject.put("icon","fa fa-briefcase");
			}
		}
		return mapObject;
	}
	
	public HashMap<String, Object> putDataInJSStateTreeMap(HashMap<String, Object> mapObject, long keyId, String entityName, String entityType, boolean isParent, Integer parentId, int modeOrStageOrTypeId){
		if(mapObject != null){
			mapObject.put("data", String.valueOf(keyId)+"~"+entityType+"~"+entityName);
			mapObject.put("text",entityName);
			HashMap<String,Object> attrMap = new HashMap<String,Object>();
			attrMap.put("key", String.valueOf(keyId));
			attrMap.put("type", entityType);
			mapObject.put("tooltip", entityName);
			if(isParent){
				mapObject.put("parent","#");
			}else{
				mapObject.put("parent", Integer.toString(parentId));
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_RESOURCE_POOL)){
				mapObject.put("icon","fa fa-users");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY_LAB)){
				mapObject.put("icon","fa fa-university");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_CUSTOMER)){
				mapObject.put("icon","fa fa-user");
			}
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_PACKAGE)){
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_NEW){
					mapObject.put("icon","fa fa-file-text-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_PLANNING){
					mapObject.put("icon","fa fa-pencil-square-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION){
					mapObject.put("icon","fa fa-play-circle-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION){
					mapObject.put("icon","fa fa-play-circle-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED){
					mapObject.put("icon","fa fa-check-square-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_CLOSED){
					mapObject.put("icon","fa fa-lock");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_ABORT || modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_ABORTED){
					mapObject.put("icon","fa fa-lock");
				}
			}
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_SHIFT)){
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_MORNING){
					mapObject.put("icon","fa fa-sun-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_NIGHT){
					mapObject.put("icon","fa fa-moon-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_GRAVEYARD){
					mapObject.put("icon","fa fa-star-o");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY)){
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_FACTORY){
					mapObject.put("icon","fa fa-home");
				}
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_ENGAGEMENT){
					mapObject.put("icon","fa fa-gears");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY)){
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_FACTORY){
					mapObject.put("icon","fa fa-home");
				}
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_ENGAGEMENT){
					mapObject.put("icon","fa fa-gears");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				if(modeOrStageOrTypeId == IDPAConstants.TEST_FACTORY_MODE_PRODUCT){
					mapObject.put("icon","fa fa-cube");
				}
				if(modeOrStageOrTypeId == IDPAConstants.PROJECT_MODE_PRODUCT){
					mapObject.put("icon","fa fa-square");
				}
			}
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_COMPETENCY)){
				if(isParent){
					mapObject.put("icon","fa fa-cubes");
				}else{
					mapObject.put("icon","fa fa-cube");
				}
			}
			
		}
		return mapObject;
	}
	
	
	public HashMap<String, Object> putDataInMapToolTip(HashMap<String, Object> mapObject, long keyId, String entityName, String entityType,String tootltip){
		if(mapObject != null){
		mapObject.put("key", String.valueOf(keyId));
			mapObject.put("title",entityName);
			mapObject.put("type", entityType);
			mapObject.put("tooltip", tootltip);
		}
		return mapObject;
	}
	
	public HashMap<String, Object> putDataInLabelMap(HashMap<String, Object> mapObject, String entityTitle,boolean isParent){
		if(mapObject != null){
			mapObject.put("data", "listWorkPackages");
			mapObject.put("text", entityTitle);
			if(isParent){
				mapObject.put("parent","#");
			}
			mapObject.put("folder", true);
			mapObject.put("tooltip", entityTitle);
		}
		return mapObject;
	}
	public HashMap<String, Object> putDataInLabelMap(HashMap<String, Object> mapObject, String entityTitle,boolean isParent,int parentId){
		if(mapObject != null){
			mapObject.put("data", "WorkPackages");
			mapObject.put("text", entityTitle);
			mapObject.put("id", parentId);
			
			if(isParent){
				mapObject.put("parent","#");
			}
			mapObject.put("folder", true);
			mapObject.put("tooltip", entityTitle);
		}
		return mapObject;
	}


	@Override
	public String getProductTree(int userRoleId, int userId) {
		log.debug("In getProductTree() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		HashMap<String, Object> productVersionMap = null;
		List<HashMap<String, Object>> productVersionList = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		List<Integer> engagementManagerTestFactoryIds = new ArrayList<Integer>();
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			if(listOfTestFactories != null && listOfTestFactories.size() > 0){
				for(TestFactory testFactory : listOfTestFactories){
					engagementManagerTestFactoryIds.add(testFactory.getTestFactoryId());
				}
			}
			if(listOfTestFactories == null){
				listOfTestFactories = new ArrayList<TestFactory>();
			}
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size() > 0){
				for(ProductMaster productMaster : listOfProducts){
					if(!engagementManagerTestFactoryIds.contains(productMaster.getTestFactory().getTestFactoryId())){
						listOfTestFactories.add(productMaster.getTestFactory());
						engagementManagerTestFactoryIds.add(productMaster.getTestFactory().getTestFactoryId());
					}
				}
			}
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();
					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						if(engagementManagerTestFactoryIds.contains(testFactory.getTestFactoryId())){
							listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					for (ProductMaster product : listOfProductsToConsider) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						if(product != null){
							productVersionList = new ArrayList<HashMap<String,Object>>();
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION, false,product.getProductId());									
								}
								productVersionList.add(productVersionMap);
							}
						}
						productMap.put("children", productVersionList);

						productList.add(productMap);
					}
					
				}
				testFactoryMap.put("children", productList);
				testFactoryList.add(testFactoryMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryList);
		}
		log.debug("returnvalue*****===" + returnvalue);	
		return returnvalue;
	}
	
	@Override
	public String getProductBotTree(int userRoleId, int userId) {
		log.debug("In getProductBotTreeTree() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		List<Integer> engagementManagerTestFactoryIds = new ArrayList<Integer>();
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			if(listOfTestFactories != null && listOfTestFactories.size() > 0){
				for(TestFactory testFactory : listOfTestFactories){
					engagementManagerTestFactoryIds.add(testFactory.getTestFactoryId());
				}
			}
			if(listOfTestFactories == null){
				listOfTestFactories = new ArrayList<TestFactory>();
			}
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size() > 0){
				for(ProductMaster productMaster : listOfProducts){
					if(!engagementManagerTestFactoryIds.contains(productMaster.getTestFactory().getTestFactoryId())){
						listOfTestFactories.add(productMaster.getTestFactory());
						engagementManagerTestFactoryIds.add(productMaster.getTestFactory().getTestFactoryId());
					}
				}
			}
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();
					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						if(engagementManagerTestFactoryIds.contains(testFactory.getTestFactoryId())){
							listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					for (ProductMaster product : listOfProductsToConsider) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
						}

						productList.add(productMap);
					}
					
				}
				testFactoryMap.put("children", productList);
				testFactoryList.add(testFactoryMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryList);
		}
		log.debug("returnvalue*****===" + returnvalue);	
		return returnvalue;
	}
	
	@Override
	public String getProductTreeWithFolder(int userRoleId, int userId) {
		log.debug("In workPackageTreeData() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productLabelMap = null;
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productLabelList = null;
		productLabelList = new ArrayList<HashMap<String, Object>>();
		productLabelMap = new HashMap<String, Object>();
		productLabelMap = putDataInLabelMap(productLabelMap, "Products", true);
		productList = new ArrayList<HashMap<String,Object>>();
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			 if(listOfTestFactories != null && listOfTestFactories.size()>0){
				 listOfProducts = new ArrayList<ProductMaster>();
				 for (TestFactory testFactory : listOfTestFactories) {
					List<ProductMaster> subListOfProducts = null;
					subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(listOfProducts.size() == 0){
						listOfProducts = subListOfProducts;
					}else{
						listOfProducts.addAll(subListOfProducts);
					}
				}
			 }
		}else{
			listOfProducts = productMasterDAO.list(false);
		}
		String returnvalue = null;
		if(listOfProducts != null && listOfProducts.size()>0){
			for (ProductMaster product : listOfProducts) {
				if(product != null){
					productMap = new HashMap<String, Object>();
					productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(),IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
				}
				productList.add(productMap);
			}
			productLabelMap.put("children", productList);
			productLabelList.add(productLabelMap);
			returnvalue = JSONValue.toJSONString(productLabelList);
		}
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}



	@Override
	public String getProductHierarchyTree(int userRoleId, int userId) {
		log.debug("In getProductHierarchyTree() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		String returnvalue = null;
				productList = new ArrayList<HashMap<String,Object>>();
				List<TestFactory> listOfTestFactories = null;
				List<ProductMaster> listOfProducts = null;
				if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
					listOfProducts = productMasterDAO.list(false);
				}
				else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER ){
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
					listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
					 if(listOfTestFactories != null && listOfTestFactories.size()>0){
						 listOfProducts = new ArrayList<ProductMaster>();
						 for (TestFactory testFactory : listOfTestFactories) {
							List<ProductMaster> subListOfProducts = null;
							subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
							if(listOfProducts.size() == 0){
								listOfProducts = subListOfProducts;
							}else{
								listOfProducts.addAll(subListOfProducts);
							}
						}
					 }
				}
				if(listOfProducts != null){
					for (ProductMaster product : listOfProducts) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						productVersionList = new ArrayList<HashMap<String,Object>>();
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									List<ProductBuild> listOfProductBuilds = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
										for (ProductBuild productBuild : listOfProductBuilds) {
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(productBuild.getBuildDate()));
												productBuildMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(productBuild.getBuildDate()));
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
											}
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
							}
						productMap.put("children", productVersionList);
						productList.add(productMap);
					}
					returnvalue = JSONValue.toJSONString(productList);
				}
				
				log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}


	@Override
	@Transactional
	public String getUserTypeTree(int userRoleId, int userId) {
		log.debug("In User Type tree() method");
		
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> resourcePoolMap = null;
		HashMap<String, Object> userTypeMap = null;
		
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> resourcePoolList = null;
		List<HashMap<String, Object>> userTypeList = null;
		
			    testFactoryLabList = new ArrayList<HashMap<String,Object>>();
				List<TestFactoryLab> listOfTestFactoryLabs = null;
				List<TestfactoryResourcePool> listOfResourcePoolsOfResourceManager = null;
				
				if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
					listOfResourcePoolsOfResourceManager = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId,userId);
					for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePoolsOfResourceManager) {
						listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
					}
				}else{
					listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
				}
				
				String returnvalue = null;
				if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size()>0){
					for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
						List<TestfactoryResourcePool> listOfResourcePools = null;
						List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab = null;
						resourcePoolList = new ArrayList<HashMap<String,Object>>();
						if(testFactoryLab != null){
							testFactoryLabMap = new HashMap<String, Object>();
							testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), IDPAConstants.ENTITY_TEST_FACTORY_LAB,true,0);
							resourcePoolOfSpecificTestFactoryLab = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
							if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
								listOfResourcePools = getResourcePools(listOfResourcePoolsOfResourceManager, resourcePoolOfSpecificTestFactoryLab);
							}else{
								listOfResourcePools = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
							}
							for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
								List<UserTypeMasterNew> listOfUserTypes = null;
								userTypeList = new ArrayList<HashMap<String,Object>>();
								if(testfactoryResourcePool != null){
									resourcePoolMap = new HashMap<String, Object>();
									resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, testfactoryResourcePool.getResourcePoolId(), testfactoryResourcePool.getDisplayName(),IDPAConstants.ENTITY_RESOURCE_POOL,false,testFactoryLab.getTestFactoryLabId());
									listOfUserTypes = userTypeMasterNewDAO.list();
									for (UserTypeMasterNew userType : listOfUserTypes) {
										userTypeMap = new HashMap<String, Object>();
										userTypeMap = putDataInJSTreeMap(userTypeMap, userType.getUserTypeId(), userType.getUserTypeLabel(), IDPAConstants.ENTITY_USER_TYPE,false,testfactoryResourcePool.getResourcePoolId());
										userTypeList.add(userTypeMap);
									}
								}
								
								resourcePoolMap.put("children", userTypeList);
								resourcePoolList.add(resourcePoolMap);
							}
							/*  Adding Un allocate Resourcepool for who are all not allocate with any resourcepool for today's date we are showing here  */
							resourcePoolMap = new HashMap<String, Object>();
							resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, IDPAConstants.UN_ALLOCATED_RESOURCEPOOL_ID, IDPAConstants.UN_ALLOCATED_RESOURCEPOOL_NAME,IDPAConstants.ENTITY_RESOURCE_POOL,false,testFactoryLab.getTestFactoryLabId());
							resourcePoolList.add(resourcePoolMap);
						}
						testFactoryLabMap.put("children", resourcePoolList);
						testFactoryLabList.add(testFactoryLabMap);
					}
					returnvalue = JSONValue.toJSONString(testFactoryLabList);
				}
				log.debug("returnvalue===" + returnvalue);
				return returnvalue;
	}

	
	@Override
	@Transactional
	public JSONArray getWorkPackagePlanTreeInJSON(int userRoleId, int userId, int treeType,String parentType,int parentId) {
		log.debug("GetWorkPackagePlanTree() for User Role Id: ..."+userRoleId);
		JSONArray	jsonArray = new JSONArray();
		String nodeType=parentType;
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> workPackageMap = null;
		HashMap<String, Object> productVersionMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> workPackageList = null;
		List<HashMap<String, Object>> productVersionList = null;
		HashMap<String, Object> workPackageLabelMap = null;
		List<HashMap<String, Object>> workPackageLabelList = null;
		List<ProductVersionListMaster> listOfUserAssociatedProductVariants = null;
		List<ProductBuild> listOfUserAssociatedProductBuild = null;
		List<WorkPackage> listOfUserAssociatedWorkPackages = null;
		
		List<ProductVersionListMaster> listOfProductVariants = null;
		Set<ProductVersionListMaster> setOfProductVariants = null;
		List<ProductVersionListMaster> listOfProductVersionsOfProduct = null;
		Set<ProductVersionListMaster> setOfProductVersionsOfProduct = null;
		boolean flag=false;
		
		
				
				List<ProductMaster> listOfProducts = null;
				List<TestFactory> listOfTestFactories = null;
				if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
					if(nodeType.equalsIgnoreCase("Product")){
						listOfUserAssociatedProductVariants = productVersionListMasterDAO.getUserAssociatedProductVariants(userRoleId,userId);
					}					
				}else if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
					if(!nodeType.equalsIgnoreCase("Product") && !nodeType.equalsIgnoreCase("ProductBuild") && !nodeType.equalsIgnoreCase("WorkPackage")){
						listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
					}
					
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
					listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
					 if(listOfTestFactories != null && listOfTestFactories.size()>0){
						 listOfProducts = new ArrayList<ProductMaster>();
						 for (TestFactory testFactory : listOfTestFactories) {
							List<ProductMaster> subListOfProducts = null;
							subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
							if(listOfProducts.size() == 0){
								listOfProducts = subListOfProducts;
							}else{
								listOfProducts.addAll(subListOfProducts);
							}
						}
					 }
				}
				else{
					flag=true;  // admin
					
				}
				String returnValue = "";
				
		if (!nodeType.equalsIgnoreCase("TestFactory")&&!nodeType.equalsIgnoreCase("Product") && !nodeType.equalsIgnoreCase("ProductBuild") && !nodeType.equalsIgnoreCase("WorkPackage")) {

			listOfTestFactories = testFactoryDao.getTestFactoryList(0, 1, 0);

			List<HashMap<String, Object>> testFactoryList = new ArrayList<HashMap<String, Object>>();
			if (listOfTestFactories != null && listOfTestFactories.size() > 0) {
				HashMap<String, Object> testFactoryMap = null;
				for (TestFactory testFactory : listOfTestFactories) {
					if (testFactory != null) {
						testFactoryMap = new HashMap<String, Object>();
						if (userRoleId == IDPAConstants.ROLE_ID_TESTER) {
							if (treeType == 2) {
								if (testFactory.getProductMaster().size() > 0) {
									testFactoryMap = putDataInParentTreeMap(testFactoryMap,"TestFactory"+"~"+ testFactory.getTestFactoryId(),testFactory.getTestFactoryName(),IDPAConstants.ENTITY_TEST_FACTORY,false, testFactory.getTestFactoryLab().getTestFactoryLabId(),true, -1);
								} else {
									testFactoryMap = putDataInParentTreeMap(testFactoryMap,"TestFactory"+"~"+ testFactory.getTestFactoryId(),testFactory.getTestFactoryName(),IDPAConstants.ENTITY_TEST_FACTORY,false, testFactory.getTestFactoryLab().getTestFactoryLabId(),false, -1);
								}
							}
						} else {
							
							listOfProducts=productMasterDAO.listProductsByTestFactoryId(testFactory.getTestFactoryId());
							int productCount = 0;
							productCount = productMasterDAO.getProductsCountByTestFactoryId(testFactory.getTestFactoryId(), -1, -1);
							if (productCount > 0) {
								testFactoryMap = putDataInParentTreeMap(testFactoryMap,"TestFactory"+"~"+testFactory.getTestFactoryId(),testFactory.getTestFactoryName(),IDPAConstants.ENTITY_TEST_FACTORY,true, testFactory.getTestFactoryLab().getTestFactoryLabId(), true,-1);
							} else {
								testFactoryMap = putDataInParentTreeMap(testFactoryMap,"TestFactory"+ "~"+ testFactory.getTestFactoryId(),testFactory.getTestFactoryName(),IDPAConstants.ENTITY_TEST_FACTORY,false, testFactory.getTestFactoryLab().getTestFactoryLabId(), false,-1);
							}
						}
					}
					testFactoryList.add(testFactoryMap);
				}
			}
			jsonArray.addAll(testFactoryList);
			return jsonArray;
		}
				
				if(!nodeType.equalsIgnoreCase("Product") && !nodeType.equalsIgnoreCase("ProductBuild") && !nodeType.equalsIgnoreCase("WorkPackage") && nodeType.equalsIgnoreCase("TestFactory")){
								if(flag){
										listOfProducts=productMasterDAO.listProductsByTestFactoryId(parentId);//Admin
								}
								productList = new ArrayList<HashMap<String,Object>>();
								if(listOfProducts != null && listOfProducts.size()>0){ 
								for (ProductMaster product : listOfProducts) {
															
									if(product != null){
										productMap = new HashMap<String, Object>();
										if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
											if(treeType == 2){
											if(product.getProductVersionListMasters().size()>0){
												productMap = putDataInParentTreeMap(productMap,"Product"+"~"+product.getProductId(), product.getProductName(),IDPAConstants.ENTITY_PRODUCT,false,product.getProductMode().getModeId(),true,-1);
											}else{
												productMap = putDataInParentTreeMap(productMap,"Product"+"~"+product.getProductId(), product.getProductName(),IDPAConstants.ENTITY_PRODUCT,false,product.getProductMode().getModeId(),false,-1);
											}
											}
										}else{
											List<ProductBuild>  pbs =  productBuildDAO.listBuildsByProductId(product.getProductId());  //product Id
											if(pbs.size()>0){
												productMap = putDataInParentTreeMap(productMap,"Product"+"~"+product.getProductId(), product.getProductName(),IDPAConstants.ENTITY_PRODUCT,true,product.getProductMode().getModeId(),true,-1);
											}else{
												productMap = putDataInParentTreeMap(productMap,"Product"+"~"+product.getProductId(), product.getProductName(),IDPAConstants.ENTITY_PRODUCT,false,product.getProductMode().getModeId(),false,-1);
			
											}
											
										}
										
										}
										
							     	productList.add(productMap);
									
								}
							}
					 jsonArray.addAll(productList);
					 return jsonArray;
				}
				else if(nodeType.equalsIgnoreCase("Product")){
				productVersionList = new ArrayList<HashMap<String,Object>>();
					if(treeType == 2){
						productVersionList = new ArrayList<HashMap<String,Object>>();
					}
				if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
					listOfUserAssociatedProductVariants = productVersionListMasterDAO.getUserAssociatedProductVariants(userRoleId,userId);
					listOfProductVersionsOfProduct = productVersionListMasterDAO.list(parentId); //product Id
					//setOfProductVersionsOfProduct=product.getProductVersionListMasters();
					listOfProductVariants = getProductVersionList(listOfUserAssociatedProductVariants, listOfProductVersionsOfProduct);
					for (ProductVersionListMaster productVersion : listOfProductVariants){
						if(productVersion != null){
							if(treeType == 2){
								productVersionMap = new HashMap<String, Object>();
								if(productVersion.getProductBuild().size()!=0){
									productVersionMap = putDataInParentTreeMap(productVersionMap, "ProductVersion~"+productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,parentId,true,-1);
								}else{
									productVersionMap = putDataInParentTreeMap(productVersionMap, "ProductVersion~"+productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,parentId,false,-1);
								}
							}
							
						}
					}
					productVersionList.add(productBuildMap);
					 jsonArray.addAll(productVersionList);
					 return jsonArray;
					
				}else{
					// except tester
					List<ProductBuild> listOfProductBuilds = null;
					productBuildList = new ArrayList<HashMap<String,Object>>();
					listOfProductBuilds =  productBuildDAO.listBuildsByProductId(parentId);  //product Id
					
					
					for (ProductBuild productBuild : listOfProductBuilds) {
						
						if(productBuild != null){
							productBuildMap = new HashMap<String, Object>();
							if(treeType != 2){
								productBuildMap = putDataInParentTreeMap(productBuildMap, "ProductBuild~"+productBuild.getProductBuildId(),productBuild.getBuildname(),IDPAConstants.ENTITY_PRODUCT_BUILD,false,parentId,true,-1);
							}else{
								productBuildMap = putDataInParentTreeMap(productBuildMap, "ProductBuild~"+productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,false,productBuild.getProductVersion().getProductVersionListId(),true,-1); // product version id
							}
						
						}
						productBuildList.add(productBuildMap);
					}
					
					
					 jsonArray.addAll(productBuildList);
					 return jsonArray;
					
					
					
				}
					
				}else if(nodeType.equalsIgnoreCase("ProductVersion")){     // Tester and Tree type==2
					List<ProductBuild> listOfProductBuilds = null;
					productBuildList = new ArrayList<HashMap<String,Object>>();
					listOfUserAssociatedProductBuild = productBuildDAO.getUserAssociatedProductBuilds(userRoleId,userId);
					listOfProductBuilds =  productBuildDAO.listBuildsByProductId(parentId);  //product Id
					listOfProductBuilds = getProductBuilds(listOfUserAssociatedProductBuild,listOfProductBuilds);
					
					for (ProductBuild productBuild : listOfProductBuilds) {
						
						if(productBuild != null){
							productBuildMap = new HashMap<String, Object>();
							if(treeType != 2){
								productBuildMap = putDataInParentTreeMap(productBuildMap, "ProductBuild~"+productBuild.getProductBuildId(),productBuild.getBuildname(),IDPAConstants.ENTITY_PRODUCT_BUILD,false,parentId,true,-1);
							}else{
								productBuildMap = putDataInParentTreeMap(productBuildMap, "ProductBuild~"+productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,false,productBuild.getProductVersion().getProductVersionListId(),true,-1); // product version id
							}
						
						}
						productBuildList.add(productBuildMap);
					}
					
					
					 jsonArray.addAll(productBuildList);
					 return jsonArray;
				}
				
				else if(nodeType.equalsIgnoreCase("ProductBuild") ){
					workPackageLabelMap = new HashMap<String, Object>();
					workPackageLabelList = new ArrayList<HashMap<String, Object>>();
					workPackageLabelMap = putDataInParentTreeMap(workPackageLabelMap,"pb~"+parentId,"Work Packages", IDPAConstants.ENTITY_WORK_PACKAGE,true,parentId,false,-1); // product version id
					workPackageLabelList.add(workPackageLabelMap);
					
					jsonArray.addAll(workPackageLabelList);
					 return jsonArray;
					
				}
	              else if(nodeType.equalsIgnoreCase("WorkPackage") ){  //        ProductBuild  WorkPackage
					workPackageLabelList = new ArrayList<HashMap<String, Object>>();
					List<WorkPackage> listOfWorkPackage = null;
					List<WorkPackage> listOfWorkPackagesOfProductBuild = null;
					workPackageList = new ArrayList<HashMap<String,Object>>();
					if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
						listOfUserAssociatedWorkPackages = workPackageDAO.getUserAssociatedWorkPackages(userRoleId,userId);
						listOfWorkPackagesOfProductBuild = workPackageDAO.listWorkPackagesByProductBuild(parentId);   //build id
						listOfWorkPackage = getWorkPackages(listOfUserAssociatedWorkPackages,listOfWorkPackagesOfProductBuild);
						
					}else{
						listOfWorkPackage =  workPackageDAO.listWorkPackagesByProductBuild(parentId);  //build id
					}
					
					for (WorkPackage workPackageBean : listOfWorkPackage) {
												
						List<DecouplingCategory> listOfDecouplingCategories = null;
						List<ProductFeature> listOfFeatures = null;
						if(workPackageBean != null){
							WorkFlow workFlow=null;
								if(workPackageBean.getWorkFlowEvent()!=null){
									workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
									if(workFlow!=null){
										if(workFlow.getEntityMaster()!=null){
											if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
												int stageId=workFlow.getStageId();
												workPackageMap = new HashMap<String, Object>();
												workPackageMap = putDataInParentTreeMap(workPackageMap,"WorkPackage~"+workPackageBean.getWorkPackageId(),workPackageBean.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,false,parentId,false,stageId); // product version id
											}
										}
									}
								}
							
						}
						if(workPackageMap!=null){
							workPackageList.add(workPackageMap);
						}
					
					}
					jsonArray.addAll(workPackageList);
					 return jsonArray;
				}
				else{
					returnValue = null;
				}
				return jsonArray;
	}

	
	@Override
	@Transactional//mamtha change
	public JSONArray getActivityWorkPackageTaskPlanTreeInJSON(int userRoleId, int userId, int treeType,String parentType,int parentId) 
	{
	log.debug("getActivityWorkPackageTaskPlanTreeInJSON() for User Role Id: ..."+userRoleId);
	JSONArray	jsonArray = new JSONArray();
	String nodeType=parentType;
	HashMap<String, Object> testFactoryMap = null;
	HashMap<String, Object> productMap = null;
	HashMap<String, Object> activityWpMap = null;
	HashMap<String, Object> activityMap = null;
	HashMap<String, Object> activityTaskMap = null;
	
	List<HashMap<String, Object>> testFactoryList = null;
	List<HashMap<String, Object>> productList = null;
	List<HashMap<String, Object>> activityWpList = null;
	List<HashMap<String, Object>> activityList = null;
	List<HashMap<String, Object>> activityTaskList = null;
		
	List<ProductMaster> listOfUserAssociatedProducts = null;	
	List<ActivityWorkPackage> listOfUserAssociatedActivityWP = null;
	List<Activity> listOfUserAssociatedActivity = null;	
	List<ActivityTask> listOfUserAssociatedActivityTask = null;
			
	List<ProductMaster> listOfProducts = null;
	List<TestFactory> listOfTestFactories = null;
			
	List<HashMap<String, Object>> activityWorkPackageLabelList = null;
	List<HashMap<String, Object>> activityLabelList = null;
	List<HashMap<String, Object>> activityTaskLabelList = null;
	
	UserList user = userDAO.getByUserId(userId);
	
	if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
		listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
		if(listOfProducts != null && listOfProducts.size()>0){
			listOfTestFactories = new ArrayList<TestFactory>();
			for (ProductMaster productMaster : listOfProducts) {
				List<TestFactory> subListOfTestFactory = null;
				subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
				if(listOfTestFactories.size() == 0){
					listOfTestFactories = subListOfTestFactory;
				}else{
					if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
						for (TestFactory testFactory : subListOfTestFactory) {
							if(!listOfTestFactories.contains(testFactory)){
								listOfTestFactories.add(testFactory);
							}
						}
					}
				}
			}
		}
	}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
		listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
	}else{
		listOfTestFactories = testFactoryDao.getTestFactoryList();
		listOfProducts = productMasterDAO.list(false);
		
	}
	String returnValue = "";
	if(!nodeType.equalsIgnoreCase("TestFactory") && !nodeType.equalsIgnoreCase("Product") && !nodeType.equalsIgnoreCase("ActivityWorkPackage") 
			&& !nodeType.equalsIgnoreCase("Activity") && !nodeType.equalsIgnoreCase("ActivityWorkPackageIcon")
			&& !nodeType.equalsIgnoreCase("ActivityIcon") && !nodeType.equalsIgnoreCase("ActivityTaskIcon"))
	{
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size() >0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					testFactoryMap = new HashMap<String, Object>();
					if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
						if(treeType == 2){
							if(testFactory.getProductMaster().size() >0){
								testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), true, -1);
							}else{
								testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), false, -1);
							}
						}
					}else{
						int productCount = 0;
						productCount = productMasterDAO.getProductsCountByTestFactoryId(testFactory.getTestFactoryId(), -1, -1);
						if(productCount >0){
							testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true, testFactory.getTestFactoryLab().getTestFactoryLabId(), true, -1);
						}else{
							testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), false, -1);
						}					
					}
				}
				testFactoryList.add(testFactoryMap);
			}
		}		
		jsonArray.addAll(testFactoryList);
		return jsonArray;
	}
	else if(nodeType.equalsIgnoreCase("TestFactory"))
	{
		productList = new ArrayList<HashMap<String,Object>>();
		
		if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
			listOfUserAssociatedProducts = productMasterDAO.getProductsByTestFactoryId(parentId);
		}
		else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfUserAssociatedProducts = productMasterDAO.getProductsByProductUserRoleForUserIdOfTestFactory(userRoleId, userId, parentId, 0);
		}
		if(listOfUserAssociatedProducts != null && listOfUserAssociatedProducts.size() >0){
			for (ProductMaster productMaster : listOfUserAssociatedProducts) {
					productMap = new HashMap<String, Object>();
					if(productMaster.getProductVersionListMasters().size() != 0){
						productMap = putDataInParentTreeMap(productMap, "Product~"+productMaster.getProductId(), productMaster.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,parentId,true,-1);
					}else{
						productMap = putDataInParentTreeMap(productMap, "Product~"+productMaster.getProductId(), productMaster.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,parentId,false,-1);
					}					
				productList.add(productMap);
			}
		}
		
		jsonArray.addAll(productList);
		return jsonArray;
	}else if(nodeType.equalsIgnoreCase("Product"))
	{
		activityWpMap = new HashMap<String, Object>();
		boolean hasChildren = false;
		listOfUserAssociatedActivityWP = activityWorkPackageDAO.listActivityWorkPackagesByProductId(parentId, 0, 0, 1,user, null);
		if(listOfUserAssociatedActivityWP != null && !listOfUserAssociatedActivityWP.isEmpty()){
			hasChildren = true;
		}
		activityWorkPackageLabelList   = new ArrayList<HashMap<String, Object>>();
		activityWpMap =  putDataInParentTreeMap(activityWpMap,"product~"+parentId,"WorkPackages", IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_FOLDER_ICON,true,parentId,hasChildren,-1); // product version id
		activityWorkPackageLabelList.add(activityWpMap);			 
		jsonArray.addAll(activityWorkPackageLabelList);
		return jsonArray;
	}else if(nodeType.equalsIgnoreCase("ActivityWorkPackageIcon"))
	{
		activityWpList = new ArrayList<HashMap<String,Object>>();
		listOfUserAssociatedActivityWP = activityWorkPackageDAO.listActivityWorkPackagesByProductId(parentId, 0, 0, 1,user, null);
		
		if(listOfUserAssociatedActivityWP != null && !listOfUserAssociatedActivityWP.isEmpty()){
			for (ActivityWorkPackage activityWorkPackage : listOfUserAssociatedActivityWP) {
				activityWpMap = new HashMap<String, Object>();
				activityWpMap.put("testFactId", activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
				activityWpMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedStartDate()));
				activityWpMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedEndDate()));
				if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0 && activityWorkPackage.getWorkflowStatus().getIsLifeCycleStage() == 1){
					activityWpMap.put("lifeCycleStage", activityWorkPackage.getWorkflowStatus().getWorkflowStatusId());
				}
				if(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId() != null){
					activityWpMap.put("productId", activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
				}
				activityWpMap = putDataInParentTreeMap(activityWpMap, "WorkPackage~"+activityWorkPackage.getActivityWorkPackageId(),activityWorkPackage.getActivityWorkPackageName(),IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE,false,parentId,false,-1);
				activityWpList.add(activityWpMap);
			}			 
		}	
		jsonArray.addAll(activityWpList);
		return jsonArray;
	}
	else if(nodeType.equalsIgnoreCase("ActivityWorkPackage") )
	{
		activityMap = new HashMap<String, Object>();
		listOfUserAssociatedActivity = activityDAO.listActivitiesByActivityWorkPackageId(parentId, 0,1);
		boolean hasChildren = false;
		ActivityWorkPackage awp = null;
		if(listOfUserAssociatedActivity != null && !listOfUserAssociatedActivity.isEmpty()){
			awp = listOfUserAssociatedActivity.get(0).getActivityWorkPackage();
			activityMap = setActivityWpInitialInformation(awp);
		}else{
			awp = activityWorkPackageDAO.getActivityWorkPackageById(parentId, 0);
			if(awp != null){
			activityMap = setActivityWpInitialInformation(awp);
			}
		}
		activityLabelList   = new ArrayList<HashMap<String, Object>>();
				
		activityMap =  putDataInParentTreeMap(activityMap,"activityWorkPackage~"+parentId,"Activities", IDPAConstants.ENTITY_ACTIVITY_FOLDER_ICON,true,parentId,hasChildren,-1); // product version id
		activityLabelList.add(activityMap);			 
		jsonArray.addAll(activityLabelList);
		return jsonArray;
	}
	else if(nodeType.equalsIgnoreCase("ActivityIcon"))
	{
		activityList = new ArrayList<HashMap<String, Object>>();
		listOfUserAssociatedActivity = activityDAO.listActivitiesByActivityWorkPackageId(parentId, 0,1);
		if(listOfUserAssociatedActivity != null && !listOfUserAssociatedActivity.isEmpty()){
			for (Activity activity : listOfUserAssociatedActivity) {
				activityMap = new HashMap<String, Object>();					
				activityMap = setActivityInitialInformation(activity);
				/*----Activities addnl fields ---*/
				activityMap = putDataInParentTreeMap(activityMap, "Activity~"+activity.getActivityId(),activity.getActivityName(),IDPAConstants.ENTITY_ACTIVITY,false,parentId,true,-1);
				activityList.add(activityMap);
			}
		}
		jsonArray.addAll(activityList);
		 return jsonArray;
	}
	else if(nodeType.equalsIgnoreCase("Activity") )
	{
		activityTaskMap = new HashMap<String, Object>();
		activityTaskLabelList   = new ArrayList<HashMap<String, Object>>();
		listOfUserAssociatedActivityTask = activityTaskDAO.getActivityTaskByActivityId(parentId);
		boolean hasChildren = false;
		if(listOfUserAssociatedActivityTask != null && !listOfUserAssociatedActivityTask.isEmpty()){
			hasChildren = true;
			Activity act = listOfUserAssociatedActivityTask.get(0).getActivity();
			activityTaskMap=setActivityInitialInformation(act);
		} else {
			Activity activity=activityDAO.getActivityById(parentId, 0);
			if(activity != null) {
				activityTaskMap=setActivityInitialInformation(activity);
			}
		}
		activityTaskMap =  putDataInParentTreeMap(activityTaskMap,"activity~"+parentId,"Tasks", IDPAConstants.ENTITY_ACTIVITY_TASK_FOLDER_ICON,true,parentId,hasChildren,-1); // product version id
		activityTaskLabelList.add(activityTaskMap);			 
		jsonArray.addAll(activityTaskLabelList);
		return jsonArray;
	}
	else if(nodeType.equalsIgnoreCase("ActivityTaskIcon") )
	{
		activityTaskList = new ArrayList<HashMap<String, Object>>();
		listOfUserAssociatedActivityTask = activityTaskDAO.getActivityTaskByActivityId(parentId);
		
		if(listOfUserAssociatedActivityTask != null && !listOfUserAssociatedActivityTask.isEmpty()){
			for (ActivityTask activityTask : listOfUserAssociatedActivityTask) {
				activityTaskMap = new HashMap<String, Object>();
				if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId() != null){
					activityTaskMap.put("productId", activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
				}
				if(activityTask.getActivityTaskType() != null && activityTask.getActivityTaskType().getActivityTaskTypeId() != null){
					activityTaskMap.put("taskTypeId", activityTask.getActivityTaskType().getActivityTaskTypeId());
				}
				if(activityTask.getActivityTaskId() != null){
					activityTaskMap.put("taskId", activityTask.getActivityTaskId());
				}
				if(activityTask.getActivity() != null){
					activityTaskMap.put("activityId", activityTask.getActivity().getActivityId());
				}
				activityTaskMap = putDataInParentTreeMap(activityTaskMap, "ActivityTask~"+activityTask.getActivityTaskId(),activityTask.getActivityTaskName(),IDPAConstants.ENTITY_ACTIVITY_TASK,false,parentId,false,-1);
				activityTaskList.add(activityTaskMap);
			}	
		}		
		jsonArray.addAll(activityTaskList);
		 return jsonArray;
	}
	else{
		returnValue = null;
	}
	log.info("returnValue===" + jsonArray);
	return jsonArray;
	}
	
	@Override
	@Transactional
	public JSONArray getProductAndWPListForDemandAndReservationInJson(int userRoleId, int userId, int treeType,String parentType,int parentId) 
	{
	log.debug("getActivityWorkPackageTaskPlanTreeInJSON() for User Role Id: ..."+userRoleId);
	JSONArray	jsonArray = new JSONArray();
	String nodeType=parentType;
	HashMap<String, Object> testFactoryLabMap = null;
	
	HashMap<String, Object> testFactoryMap = null;
	HashMap<String, Object> productMap = null;
	HashMap<String, Object> testCaseWpMap = null;
	
	List<HashMap<String, Object>> testFactoryLabList = null;
	
	List<HashMap<String, Object>> testFactoryList = null;
	List<HashMap<String, Object>> productList = null;
	List<HashMap<String, Object>> testCaseWpList = null;
	List<ProductMaster> listOfUserAssociatedProducts = null;	
	List<WorkPackage> listOfUserAssociatedTestCaseWP = null;
	List<ProductMaster> listOfProducts = null;
	List<TestFactory> listOfTestFactories = null;
			
	List<HashMap<String, Object>> tsetCaseWorkPackageLabelList = null;
	 testFactoryLabList = new ArrayList<HashMap<String,Object>>();
	 List<TestFactoryLab> listOfTestFactoryLabs = null;
	if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
		listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
		if(listOfProducts != null && listOfProducts.size()>0){
			listOfTestFactories = new ArrayList<TestFactory>();
			for (ProductMaster productMaster : listOfProducts) {
				List<TestFactory> subListOfTestFactory = null;
				subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
				if(listOfTestFactories.size() == 0){
					listOfTestFactories = subListOfTestFactory;
				}else{
					if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
						for (TestFactory testFactory : subListOfTestFactory) {
							if(!listOfTestFactories.contains(testFactory)){
								listOfTestFactories.add(testFactory);
							}
						}
					}
				}
			}
		}
	}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
		listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
	}else{
		listOfTestFactories = testFactoryDao.getTestFactoryList();
		listOfProducts = productMasterDAO.list(false);
		
	}
	
	if(listOfTestFactories != null && listOfTestFactories.size() > 0){
		listOfTestFactoryLabs = new ArrayList<TestFactoryLab>();
		for(TestFactory testFactory : listOfTestFactories){
			if(!listOfTestFactoryLabs.contains(testFactory.getTestFactoryLab())){
				listOfTestFactoryLabs.add(testFactory.getTestFactoryLab());
			}
		}
	}
	
	String returnValue = "";
	if(!nodeType.equalsIgnoreCase("TestFactoryLab") && !nodeType.equalsIgnoreCase("TestFactory") && !nodeType.equalsIgnoreCase("Product") && !nodeType.equalsIgnoreCase("WorkpackageIcon") && !nodeType.equalsIgnoreCase("Workpackage"))
	{
		testFactoryLabList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size() >0){
			for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
				if(testFactoryLab != null){
					testFactoryLabMap = new HashMap<String, Object>();
					testFactoryLabMap = putDataInParentTreeMap(testFactoryLabMap, "TestFactoryLab"+"~"+testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), IDPAConstants.ENTITY_TEST_FACTORY_LAB, true, null, true, 1);
				}
				testFactoryLabList.add(testFactoryLabMap);
			}
		}		
		jsonArray.addAll(testFactoryLabList);
		return jsonArray;
	
	}
	else if(nodeType.equalsIgnoreCase("TestFactoryLab")){

		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size() >0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					testFactoryMap = new HashMap<String, Object>();
					if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
						if(treeType == 2){
							if(testFactory.getProductMaster().size() >0){
								testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), true, 1);
							}else{
								testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), false, 1);
							}
						}
					}else{
						List<ProductMaster> pm = productMasterDAO.listProductsByTestFactoryId(testFactory.getTestFactoryId());
						if(pm.size() >0){
							testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true, testFactory.getTestFactoryLab().getTestFactoryLabId(), true,testFactory.getEngagementTypeMaster().getEngagementTypeId());
						}else{
							testFactoryMap = putDataInParentTreeMap(testFactoryMap, "TestFactory"+"~"+testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, false, testFactory.getTestFactoryLab().getTestFactoryLabId(), false, testFactory.getEngagementTypeMaster().getEngagementTypeId());
						}					
					}
				}
				testFactoryList.add(testFactoryMap);
			}
		}		
		jsonArray.addAll(testFactoryList);
		return jsonArray;
	
	}
	else if(nodeType.equalsIgnoreCase("TestFactory"))
	{
		productList = new ArrayList<HashMap<String,Object>>();
		
		if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
			listOfUserAssociatedProducts = productMasterDAO.getProductsByTestFactoryId(parentId);
		}
		else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfUserAssociatedProducts = productMasterDAO.getProductsByProductUserRoleForUserIdOfTestFactory(userRoleId, userId, parentId, 0);
		}
		if(listOfUserAssociatedProducts != null && listOfUserAssociatedProducts.size() >0){
			for (ProductMaster productMaster : listOfUserAssociatedProducts) {
					productMap = new HashMap<String, Object>();
					if(productMaster.getProductVersionListMasters().size() != 0){
						productMap = putDataInParentTreeMap(productMap, "Product~"+productMaster.getProductId(), productMaster.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,parentId,true,-1);
					}else{
						productMap = putDataInParentTreeMap(productMap, "Product~"+productMaster.getProductId(), productMaster.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,parentId,false,-1);
					}					
				productList.add(productMap);
			}
		}
		
		jsonArray.addAll(productList);
		return jsonArray;
	}else if(nodeType.equalsIgnoreCase("Product"))
	{
		testCaseWpMap = new HashMap<String, Object>();
		boolean hasChildren = false;
		listOfUserAssociatedTestCaseWP = workPackageDAO.getWorkPackagesByProductId(parentId);
		if(listOfUserAssociatedTestCaseWP != null && !listOfUserAssociatedTestCaseWP.isEmpty()){
			hasChildren = true;
		}
		tsetCaseWorkPackageLabelList   = new ArrayList<HashMap<String, Object>>();
		testCaseWpMap =  putDataInParentTreeMap(testCaseWpMap,"product~"+parentId,workPackages, IDPAConstants.ENTITY_WORK_PACKAGE_ICON,false,parentId,hasChildren,1); // product version id
		tsetCaseWorkPackageLabelList.add(testCaseWpMap);			 
		jsonArray.addAll(tsetCaseWorkPackageLabelList);
		return jsonArray;
	}else if(nodeType.equalsIgnoreCase("WorkPackageIcon"))
	{
		testCaseWpList = new ArrayList<HashMap<String,Object>>();
		listOfUserAssociatedTestCaseWP =  workPackageDAO.getWorkPackagesByProductId(parentId);
		
		if(listOfUserAssociatedTestCaseWP != null && !listOfUserAssociatedTestCaseWP.isEmpty()){
			for (WorkPackage workPackage : listOfUserAssociatedTestCaseWP) {
				testCaseWpMap = new HashMap<String, Object>();
				
				testCaseWpMap = putDataInParentTreeMap(testCaseWpMap, "WorkPackage~"+workPackage.getWorkPackageId(),workPackage.getName(),IDPAConstants.ENTITY_WORK_PACKAGE,false,parentId,false,1);
				testCaseWpList.add(testCaseWpMap);
			}			 
		}	
		jsonArray.addAll(testCaseWpList);
		return jsonArray;
	}else if(nodeType.equalsIgnoreCase("Workpackage"))
	{
		testCaseWpList = new ArrayList<HashMap<String,Object>>();
		listOfUserAssociatedTestCaseWP = workPackageDAO.getWorkPackagesByProductId(parentId);
		
		if(listOfUserAssociatedTestCaseWP != null && !listOfUserAssociatedTestCaseWP.isEmpty()){
			for (WorkPackage workPackage : listOfUserAssociatedTestCaseWP) {
				testCaseWpMap = new HashMap<String, Object>();
				/*For AWP additional Fields*/
				testCaseWpMap.put("testFactId", workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
				

				/*For AWP additional Fields*/
				testCaseWpMap = putDataInParentTreeMap(testCaseWpMap, "WorkPackage~"+workPackage.getWorkPackageId(),workPackage.getName(),IDPAConstants.ENTITY_WORK_PACKAGE,false,parentId,false,-1);
				testCaseWpList.add(testCaseWpMap);
			}			 
		}	
		jsonArray.addAll(testCaseWpList);
		return jsonArray;
	}
	
	else{
		returnValue = null;
	}
	log.info("returnValue===" + jsonArray);
	return jsonArray;
	}


	public HashMap<String, Object> putDataInParentTreeMap(HashMap<String, Object> mapObject, String keyId, String entityName, String entityType, boolean isParent, Integer parentId, boolean hasChildern,Integer modeOrStageOrTypeId){
		if(mapObject != null){
			String keyIDArr[]=keyId.split("~");
			mapObject.put("id", String.valueOf(keyId));
			mapObject.put("text", entityName);
			mapObject.put("children", hasChildern);
			mapObject.put("data", String.valueOf(keyIDArr[1])+"~"+entityType+"~"+entityName);
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY_LAB)){
				mapObject.put("icon","fa fa-university");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				if(parentId == IDPAConstants.TEST_FACTORY_MODE_PRODUCT){
					mapObject.put("icon","fa fa-cube");
				}
				if(parentId == IDPAConstants.PROJECT_MODE_PRODUCT){
					mapObject.put("icon","fa fa-square");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_FEATURE)){
				mapObject.put("icon","fa fa-crosshairs");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_CASE)){
				mapObject.put("icon","fa fa-eyedropper");
			}
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_SUITE)){
				mapObject.put("icon","fa fa-cog");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_DECOUPLING_CATEGORY)){
				mapObject.put("icon","fa fa-chain");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ENVIRONMENT_GROUP)){
				mapObject.put("icon","fa fa-sitemap");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY)){
				mapObject.put("icon","fa fa-chain");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_VERSION)){
				mapObject.put("icon","fa fa-cubes");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_BUILD)){
				mapObject.put("icon","fa fa-building-o");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_FOLDER_ICON)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
					mapObject.put("data", String.valueOf(keyIDArr[1])+"~"+IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE+"~"+entityName);
				}				
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_PACKAGE_ICON)){
				if(!isParent){
					mapObject.put("icon","fa fa-cubes");
				}
			}
			
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_FOLDER_ICON)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
					mapObject.put("data", String.valueOf(keyIDArr[1])+"~"+IDPAConstants.ENTITY_ACTIVITY+"~"+entityName);
				}				
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
				}				
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_TASK_FOLDER_ICON)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
					mapObject.put("data", String.valueOf(keyIDArr[1])+"~"+IDPAConstants.ENTITY_ACTIVITY_TASK+"~"+entityName);
				}				
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_TASK)){
				if(!isParent){
					mapObject.put("icon","fa fa-cube");
				}				
			}
			if(modeOrStageOrTypeId!=-1){
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_PACKAGE)){
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_NEW){
					mapObject.put("icon","fa fa-file-text-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_PLANNING){
					mapObject.put("icon","fa fa-pencil-square-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION){
					mapObject.put("icon","fa fa-play-circle-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION){
					mapObject.put("icon","fa fa-play-circle-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED){
					mapObject.put("icon","fa fa-check-square-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_CLOSED){
					mapObject.put("icon","fa fa-lock");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_ABORT || modeOrStageOrTypeId == IDPAConstants.WORKFLOW_STAGE_ID_ABORTED){
					mapObject.put("icon","fa fa-lock");
				}
			}
			
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_WORK_SHIFT)){
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_MORNING){
					mapObject.put("icon","fa fa-sun-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_NIGHT){
					mapObject.put("icon","fa fa-moon-o");
				}
				if(modeOrStageOrTypeId == IDPAConstants.WORKSHIFT_TYPE_ID_GRAVEYARD){
					mapObject.put("icon","fa fa-star-o");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY)){
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_FACTORY){
					mapObject.put("icon","fa fa-home");
				}
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_ENGAGEMENT){
					mapObject.put("icon","fa fa-gears");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_FACTORY)){
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_FACTORY){
					mapObject.put("icon","fa fa-home");
				}
				if(modeOrStageOrTypeId == IDPAConstants.ENGAGEMENT_TYPE_TEST_ENGAGEMENT){
					mapObject.put("icon","fa fa-gears");
				}
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				if(modeOrStageOrTypeId == IDPAConstants.TEST_FACTORY_MODE_PRODUCT){
					mapObject.put("icon","fa fa-cube");
				}
				if(modeOrStageOrTypeId == IDPAConstants.PROJECT_MODE_PRODUCT){
					mapObject.put("icon","fa fa-square");
				}
			}
		}
		if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_MONGO_COLLECTION)){
			mapObject.put("icon","fa fa-cubes");
		}
		if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PIVOT_REPORT_COLLECTION)){
			mapObject.put("icon","fa fa-pencil-square-o");
		}
		
		if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_COMPETENCY)){
				if(isParent){
					mapObject.put("icon","fa fa-cubes");
				}else{
					mapObject.put("icon","fa fa-cube");
				}
		}
	}
		return mapObject;
	}
	
	public List<ProductMaster> getUserAssociatedProducts(int userRoleId, int userId, int filter){
		List<ProductMaster> listOfProducts = null;
		if(userRoleId == 1){
			listOfProducts = productMasterDAO.list(true);
		}
		if(filter == 0){
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
				listOfProducts = productMasterDAO.getProductsByWorkPackageForUserId(userRoleId, userId,filter);
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfProducts = productMasterDAO.getProductsByProductUserRoleForUserId(userRoleId, userId,0);
			}
		}else if(filter == 1){
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER || userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfProducts = productMasterDAO.getProductsByProductUserRoleForUserId(userRoleId, userId,0);
			}
		}
		return listOfProducts;
	}
	
	public List<ProductMaster> getUserAssociatedProductsActivity(int userRoleId, int userId, int filter){
		List<ProductMaster> listOfProducts = null;
		if(userRoleId == 1){
			listOfProducts = productMasterDAO.list(true);
		}
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER || userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
			listOfProducts = productMasterDAO.getProductsByProductUserRoleForTestingTeamUserId(userRoleId, userId,filter);
		}
		return listOfProducts;
	}
	
	
	public List<ProductMaster> getProductsByActivityForUserId(int userRoleId, int userId, String filter){
		List<ProductMaster> listOfProducts = null;
		listOfProducts = activityDAO.getUserAssociatedProducts(userId, filter);
		return listOfProducts;
	}
	
	public List<ProductMaster> getProducts(List<ProductMaster> listUserAssociatedProducts, List<ProductMaster> productsOfSpecificTestFactory){
		if(listUserAssociatedProducts == null || productsOfSpecificTestFactory == null){
			return null;
		}
		List<ProductMaster> listOfProductsToConsider = new ArrayList<ProductMaster>();
		for (ProductMaster product : listUserAssociatedProducts) {
			if(productsOfSpecificTestFactory.contains(product)){
				listOfProductsToConsider.add(product);
			}
		}
		return listOfProductsToConsider;
	}
	
	public List<ProductVersionListMaster> getProductVersionList(List<ProductVersionListMaster> listUserAssociatedProductVersions, List<ProductVersionListMaster> listOfProductVersions){
		if(listUserAssociatedProductVersions == null || listOfProductVersions == null){
			return null;
		}
		List<ProductVersionListMaster> listOfProductVersionToConsider = new ArrayList<ProductVersionListMaster>();
		for (ProductVersionListMaster productVersionListMaster : listUserAssociatedProductVersions) {
			if(listOfProductVersions.contains(productVersionListMaster)){
				listOfProductVersionToConsider.add(productVersionListMaster);
			}
		}
		return listOfProductVersionToConsider;
	}
	
	public List<ProductVersionListMaster> getProductVersionList(List<ProductVersionListMaster> listUserAssociatedProductVersions, Set<ProductVersionListMaster> listOfProductVersions){
		if(listUserAssociatedProductVersions == null || listOfProductVersions == null){
			return null;
		}
		List<ProductVersionListMaster> listOfProductVersionToConsider = new ArrayList<ProductVersionListMaster>();
		for (ProductVersionListMaster productVersionListMaster : listUserAssociatedProductVersions) {
			if(listOfProductVersions.contains(productVersionListMaster)){
				listOfProductVersionToConsider.add(productVersionListMaster);
			}
		}
		return listOfProductVersionToConsider;
	}
	
	
	public List<ProductBuild> getProductBuilds(List<ProductBuild> listUserAssociatedProductBuilds, List<ProductBuild> productBuildsOfSpecificProductVersion){
		if(listUserAssociatedProductBuilds == null || productBuildsOfSpecificProductVersion == null){
			return null;
		}
		List<ProductBuild> listOfProductBuildsToConsider = new ArrayList<ProductBuild>();
		for (ProductBuild productBuild : listUserAssociatedProductBuilds) {
			if(productBuildsOfSpecificProductVersion.contains(productBuild)){
				listOfProductBuildsToConsider.add(productBuild);
			}
		}
		return listOfProductBuildsToConsider;
	}
	
	
	public List<ActivityWorkPackage> getActivityWorkPackage(List<ActivityWorkPackage> listUserAssociatedActivityWorkPackages, List<ActivityWorkPackage> activityWorkPackagesOfSpecificBuild){
		if(listUserAssociatedActivityWorkPackages == null || activityWorkPackagesOfSpecificBuild == null){
			return null;
		}
		List<ActivityWorkPackage> listOfActivityWorkPackagesToConsider = new ArrayList<ActivityWorkPackage>();
		for (ActivityWorkPackage awp : listUserAssociatedActivityWorkPackages) {
			if(activityWorkPackagesOfSpecificBuild.contains(awp)){
				listOfActivityWorkPackagesToConsider.add(awp);
			}
		}
		return listOfActivityWorkPackagesToConsider;
	}

	public List<Activity> getActivities(List<Activity> listUserAssociatedActivities, List<Activity> actvitiesOfSpecificActivityWorkPackage){
		if(listUserAssociatedActivities == null || actvitiesOfSpecificActivityWorkPackage == null){
			return null;
		}
		List<Activity> listOfActivitiesToConsider = new ArrayList<Activity>();
		for (Activity activity : listUserAssociatedActivities) {
			if(actvitiesOfSpecificActivityWorkPackage.contains(activity)){
				listOfActivitiesToConsider.add(activity);
			}
		}
		return listOfActivitiesToConsider;
	}
	
	public List<ActivityTask> getActivityTasks(List<ActivityTask> listUserAssociatedActivityTasks, List<ActivityTask> activityTasksOfSpecificActivity){
		if(listUserAssociatedActivityTasks == null || activityTasksOfSpecificActivity == null){
			return null;
		}
		List<ActivityTask> listOfActivityTasksToConsider = new ArrayList<ActivityTask>();
		for (ActivityTask activityTask : listUserAssociatedActivityTasks) {
			if(activityTasksOfSpecificActivity.contains(activityTask)){
				listOfActivityTasksToConsider.add(activityTask);
			}
		}
		return listOfActivityTasksToConsider;
	}
	
	public List<TestfactoryResourcePool> getResourcePools(List<TestfactoryResourcePool> listUserAssociatedTestfactoryResourcePool, List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab){
		if(listUserAssociatedTestfactoryResourcePool == null || resourcePoolOfSpecificTestFactoryLab == null){
			return null;
		}
		List<TestfactoryResourcePool> listOfResourcePoolsToConsider = new ArrayList<TestfactoryResourcePool>();
		for (TestfactoryResourcePool testfactoryResourcePool : listUserAssociatedTestfactoryResourcePool) {
			if(resourcePoolOfSpecificTestFactoryLab.contains(testfactoryResourcePool)){
				listOfResourcePoolsToConsider.add(testfactoryResourcePool);
			}
		}
		return listOfResourcePoolsToConsider;
	}

	
	public List<WorkPackage> getWorkPackages(List<WorkPackage> listOfUserAssociatedWorkPackages, List<WorkPackage> workPacakgesOfSpecificProductBuild){
		if(listOfUserAssociatedWorkPackages == null || workPacakgesOfSpecificProductBuild == null){
			return null;
		}
		List<WorkPackage> listOfWorkPackagesToConsider = new ArrayList<WorkPackage>();
		for (WorkPackage workPackage : listOfUserAssociatedWorkPackages) {
			if(workPacakgesOfSpecificProductBuild.contains(workPackage)){
				listOfWorkPackagesToConsider.add(workPackage);
			}
		}
		return listOfWorkPackagesToConsider;
	}
	public List<WorkPackage> getWorkPackages(List<WorkPackage> listOfUserAssociatedWorkPackages, Set<WorkPackage> workPacakgesOfSpecificProductBuild){
		if(listOfUserAssociatedWorkPackages == null || workPacakgesOfSpecificProductBuild == null){
			return null;
		}
		List<WorkPackage> listOfWorkPackagesToConsider = new ArrayList<WorkPackage>();
		for (WorkPackage workPackage : listOfUserAssociatedWorkPackages) {
			if(workPacakgesOfSpecificProductBuild.contains(workPackage)){
				listOfWorkPackagesToConsider.add(workPackage);
			}
		}
		return listOfWorkPackagesToConsider;
	}
	
	@Override
	public String getEodShiftsReportTree(int userRoleId, int userId) {
		log.debug("In getEodReportsTree() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> workPackageMap = null;
		HashMap<String, Object> shiftsMap = null;
		
		HashMap<String, Object> workPackageLabelMap = null;
		HashMap<String, Object> shiftsLabelMap = null;
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> workPackageList = null;
		List<HashMap<String, Object>> shiftsList = null;
		
		List<HashMap<String, Object>> workPackageLabelList = null;
		List<HashMap<String, Object>> shiftsLabelList = null;
		
		List<ProductVersionListMaster> listOfUserAssociatedProductVariants = null;
		List<ProductBuild> listOfUserAssociatedProductBuild = null;
		List<WorkPackage> listOfUserAssociatedWorkPackages = null;
		
				List<ProductMaster> listOfProducts = null;
				List<TestFactory> listOfTestFactories = null;
				if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
					listOfUserAssociatedProductVariants = productVersionListMasterDAO.getUserAssociatedProductVariants(userRoleId,userId);
					listOfUserAssociatedProductBuild = productBuildDAO.getUserAssociatedProductBuilds(userRoleId,userId);
					listOfUserAssociatedWorkPackages = workPackageDAO.getUserAssociatedWorkPackages(userRoleId,userId);
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
					//log.info("Get Product of test Manager......");
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
					listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
					 if(listOfTestFactories != null && listOfTestFactories.size()>0){
						 listOfProducts = new ArrayList<ProductMaster>();
						 for (TestFactory testFactory : listOfTestFactories) {
							List<ProductMaster> subListOfProducts = null;
							subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
							if(listOfProducts.size() == 0){
								listOfProducts = subListOfProducts;
							}else{
								listOfProducts.addAll(subListOfProducts);
							}
						}
					 }
				}else{
					listOfProducts = productMasterDAO.list(false);
				}
				String returnvalue = "";
			if(listOfProducts != null && listOfProducts.size()>0){
					productList = new ArrayList<HashMap<String,Object>>();
				for (ProductMaster product : listOfProducts) {
					List<ProductVersionListMaster> listOfProductVariants = null;
					List<ProductVersionListMaster> listOfProductVersionsOfProduct = null;
					productVersionList = new ArrayList<HashMap<String,Object>>();
					if(product != null){
						productMap = new HashMap<String, Object>();
						productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
						if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
							listOfProductVersionsOfProduct = productVersionListMasterDAO.list(product.getProductId());
							listOfProductVariants = getProductVersionList(listOfUserAssociatedProductVariants, listOfProductVersionsOfProduct);
						}else{
							listOfProductVariants =  productVersionListMasterDAO.list(product.getProductId());
						}
						for (ProductVersionListMaster productVersion : listOfProductVariants) {
							if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									List<ProductBuild> listOfProductBuilds = null;
									List<ProductBuild> listOfProductBuildsOfProductVersion = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
											listOfProductBuildsOfProductVersion = productBuildDAO.list(productVersion.getProductVersionListId());
											listOfProductBuilds = getProductBuilds(listOfUserAssociatedProductBuild,listOfProductBuildsOfProductVersion);
										}else{
											listOfProductBuilds =  productBuildDAO.list(productVersion.getProductVersionListId());
										}
										for (ProductBuild productBuild : listOfProductBuilds) {
											workPackageLabelList = new ArrayList<HashMap<String, Object>>();
											workPackageLabelMap = new HashMap<String, Object>();
											workPackageLabelMap = putDataInLabelMap(workPackageLabelMap, "Work Packages",true);
											List<WorkPackage> listOfWorkPackage = null;
											List<WorkPackage> listOfWorkPackagesOfProductBuild = null;
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,false, productVersion.getProductVersionListId());
												workPackageList = new ArrayList<HashMap<String,Object>>();
												if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
													listOfWorkPackagesOfProductBuild = workPackageDAO.listWorkPackagesByProductBuild(productBuild.getProductBuildId());
													listOfWorkPackage = getWorkPackages(listOfUserAssociatedWorkPackages,listOfWorkPackagesOfProductBuild);
												}else{
													listOfWorkPackage =  workPackageDAO.listWorkPackagesByProductBuild(productBuild.getProductBuildId());
												}
												for (WorkPackage workPackageBean : listOfWorkPackage) {
													shiftsLabelList = new ArrayList<HashMap<String, Object>>();
													shiftsLabelMap = new HashMap<String, Object>();
													shiftsLabelMap = putDataInLabelMap(shiftsLabelMap, "Shifts",true);
													List<WorkShiftMaster> listOfworkShifts = null;
													if(workPackageBean != null){
														WorkFlow workFlow=null;
															if(workPackageBean.getWorkFlowEvent()!=null){
																workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
																if(workFlow!=null){
																	if(workFlow.getEntityMaster()!=null){
																		if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
																			int stageId=workFlow.getStageId();
																			workPackageMap = new HashMap<String, Object>();
																			workPackageMap = putDataInJSStateTreeMap(workPackageMap, workPackageBean.getWorkPackageId(), workPackageBean.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,false,productBuild.getProductBuildId(),stageId);
																		}
																	}
																}
															}
														shiftsList = new ArrayList<HashMap<String,Object>>();
														listOfworkShifts = workShiftMasterDAO.listWorkShiftsByTestFactoryId(product.getTestFactory().getTestFactoryId());
														for (WorkShiftMaster workShiftMaster : listOfworkShifts) {
															if(workShiftMaster != null){
																shiftsMap = new HashMap<String, Object>();
																shiftsMap = putDataInJSStateTreeMap(shiftsMap, workShiftMaster.getShiftId(), workShiftMaster.getDisplayLabel(), IDPAConstants.ENTITY_WORK_SHIFT,false,workPackageBean.getWorkPackageId(),workShiftMaster.getShiftType().getShiftTypeId());
															}
															shiftsList.add(shiftsMap);															
														}
														shiftsLabelMap.put("children", shiftsList);
													}
													shiftsLabelList.add(shiftsLabelMap);
													workPackageMap.put("children", shiftsLabelList);
													workPackageList.add(workPackageMap);
												}
												workPackageLabelMap.put("children", workPackageList);
											}
											workPackageLabelList.add(workPackageLabelMap);
											productBuildMap.put("children", workPackageLabelList);
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
						}
					productMap.put("children", productVersionList);
					productList.add(productMap);
				}
				returnvalue = JSONValue.toJSONString(productList);
			}
			log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}
	
	@Override
	public String getResourcePoolTree(int userRoleId, int userId) {
		String testFactoryLabType = "TestFactoryLab";
		String resourcePoolType = "ResourcePool";
		
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> resourcePoolMap = null;
		
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> resourcePoolList = null;
		List<HashMap<String, Object>> userTypeList = null;
		
			testFactoryLabList = new ArrayList<HashMap<String,Object>>();
			List<TestFactoryLab> listOfTestFactoryLabs = null;
			List<TestfactoryResourcePool> listOfResourcePoolsOfResourceManager = null;
			
			if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
				listOfResourcePoolsOfResourceManager = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId,userId);
				for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePoolsOfResourceManager) {
					//log.info("testfactoryResourcePool: "+testfactoryResourcePool.getResourcePoolName());
					listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				}
			}else{
				listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
			}
			String returnvalue = null;
			if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size()>0){
				for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
					List<TestfactoryResourcePool> listOfResourcePools = null;
					List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab = null;
					resourcePoolList = new ArrayList<HashMap<String,Object>>();
					if(testFactoryLab != null){
						testFactoryLabMap = new HashMap<String, Object>();
						testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), testFactoryLabType,true,0);
						resourcePoolOfSpecificTestFactoryLab = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
						if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
							listOfResourcePools = getResourcePools(listOfResourcePoolsOfResourceManager, resourcePoolOfSpecificTestFactoryLab);
						}else{
							listOfResourcePools = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
						}
						for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
							if(testfactoryResourcePool != null){
								resourcePoolMap = new HashMap<String, Object>();
								resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, testfactoryResourcePool.getResourcePoolId(), testfactoryResourcePool.getDisplayName(), resourcePoolType,false,testFactoryLab.getTestFactoryLabId());
						}
						resourcePoolMap.put("children", userTypeList);
						resourcePoolList.add(resourcePoolMap);
					}
				}
				testFactoryLabMap.put("children", resourcePoolList);
				testFactoryLabList.add(testFactoryLabMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryLabList);
			log.debug("returnvalue===" + returnvalue);
			}
			
		return returnvalue;
	}


	@Override
	public String getResourcePoolTreeFromLab(UserList userList) {
		String testFactoryLabType = "TestFactoryLab";
		String resourcePoolType = "ResourcePool";
		
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> resourcePoolMap = null;
		
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> resourcePoolList = null;
		List<HashMap<String, Object>> userTypeList = null;
		
			testFactoryLabList = new ArrayList<HashMap<String,Object>>();
			List<TestFactoryLab> listOfTestFactoryLabs = null;
			List<TestfactoryResourcePool> listOfResourcePoolsOfResourceManager = null;
			Integer userRoleId = userList.getUserRoleMaster().getUserRoleId();
			if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || (userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER && userList.getResourcePool().getResourcePoolId() != -10)){
				listOfResourcePoolsOfResourceManager = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId, userList.getUserId());
				for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePoolsOfResourceManager) {
					listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				}
			}else{
				listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
			}
			String returnvalue = null;
			if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size()>0){
				for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
					List<TestfactoryResourcePool> listOfResourcePools = null;
					List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab = null;
					resourcePoolList = new ArrayList<HashMap<String,Object>>();
					if(testFactoryLab != null){
						testFactoryLabMap = new HashMap<String, Object>();
						testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), testFactoryLabType,true,0);
						resourcePoolOfSpecificTestFactoryLab = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
						if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || (userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER && userList.getResourcePool().getResourcePoolId() != -10)){
							listOfResourcePools = getResourcePools(listOfResourcePoolsOfResourceManager, resourcePoolOfSpecificTestFactoryLab);
						}else{
							listOfResourcePools = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
						}
						for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
							if(testfactoryResourcePool != null){
								resourcePoolMap = new HashMap<String, Object>();
								if(testfactoryResourcePool.getResourcePoolId() == userList.getResourcePool().getResourcePoolId()){
									resourcePoolMap.put("isOwnResourcePool", true);
								}else{
									resourcePoolMap.put("isOwnResourcePool", false);
								}
								resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, testfactoryResourcePool.getResourcePoolId(), testfactoryResourcePool.getDisplayName(), resourcePoolType,false,testFactoryLab.getTestFactoryLabId());
						}
						resourcePoolMap.put("children", userTypeList);
						resourcePoolList.add(resourcePoolMap);
					}
				}
				testFactoryLabMap.put("children", resourcePoolList);
				testFactoryLabList.add(testFactoryLabMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryLabList);
			log.debug("returnvalue===" + returnvalue);
			}
			
		return returnvalue;
	}
	

	@Override
	public String getTestFactoryProductTree(int userRoleId, int userId) {
		log.debug("In getTestFactoryProductTree() method");
		
		HashMap<String, Object> testFactoryMap = null;
		HashMap<String, Object> productMap = null;
		
		List<HashMap<String, Object>> testFactoryList = null;
		List<HashMap<String, Object>> productList = null;
		List<TestFactoryLab> listOfTestFactoryLab = null;
		List<TestFactory> listOfTestFactories = null;
		List<TestfactoryResourcePool> listOfTestfactoryResourcePool = null;
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		
		if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
			listOfTestfactoryResourcePool = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId,userId);
			for (TestfactoryResourcePool testfactoryResourcePool : listOfTestfactoryResourcePool) {
				log.debug("testfactoryResourcePool: "+testfactoryResourcePool.getResourcePoolName());
				listOfTestFactoryLab = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				for (TestFactoryLab testFactoryLab : listOfTestFactoryLab) {
					listOfTestFactories = testFactoryDao.list(testFactoryLab.getTestFactoryLabId());
				}
			}
		}else{
			// Get only the list of Test Factories
			listOfTestFactories = testFactoryDao.getTestFactoryList(0,1,1);
		}
		for (TestFactory testFactory : listOfTestFactories) {
			productList = new ArrayList<HashMap<String,Object>>();
			List<ProductMaster> listOfProducts = null;
			if(testFactory != null && testFactory.getEngagementTypeMaster().getEngagementTypeId() == 1){
				testFactoryMap = new HashMap<String, Object>();
				testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true, 0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
				listOfProducts = productMasterDAO.listProductsByTestFactoryId(testFactory.getTestFactoryId());
				for (ProductMaster product : listOfProducts) {
					if(product != null){
						productMap = new HashMap<String, Object>();
						productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
					}
					productList.add(productMap);
				}
				testFactoryMap.put("children", productList);
			}
			testFactoryList.add(testFactoryMap);
		}
		String returnvalue = JSONValue.toJSONString(testFactoryList);
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}

	
	@Override
	public String getTestFactoryLabTestFactoryProductTree(int userRoleId, int userId) {
		log.debug("In getTestFactoryLabTestFactoryProductTree() method userRoleId: "+userRoleId+"  userId: "+userId);
		
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> testFactoryMap = null;
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> workPackageMap = null;
		HashMap<String, Object> resourcePoolMap = null;
		
		HashMap<String, Object> testFactoryLabelMap = null;
		HashMap<String, Object> resourcePoolLabelMap = null;
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> workPackageList = null;
		List<HashMap<String, Object>> resourcePoolList = null;
		
		
		List<HashMap<String, Object>> testFactoryLabelList = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> resourcePoolLabelList = null;
		
		List<TestFactoryLab> listOfTestFactoryLabs = null;
		List<TestFactory> listOfTestFactories = null;
		List<TestfactoryResourcePool> listOfResourcePools = null;
		
		List<TestfactoryResourcePool> listOfTestfactoryResourcePool = null;
		List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab = null;
		testFactoryLabList = new ArrayList<HashMap<String,Object>>();
		
		if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
			listOfTestfactoryResourcePool = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId,userId);
			for (TestfactoryResourcePool testfactoryResourcePool : listOfTestfactoryResourcePool) {
				listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
					if(testFactoryLab != null){
						listOfTestFactories = testFactoryDao.list(testFactoryLab.getTestFactoryLabId());
					}
				}
			}
		}else{
			listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
		}
		String returnvalue = null;
		if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size()>0){
			for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
				testFactoryLabelList = new ArrayList<HashMap<String, Object>>();
				groupList = new ArrayList<HashMap<String, Object>>();
				testFactoryLabelMap = new HashMap<String, Object>();
				testFactoryLabelMap = putDataInLabelMap(testFactoryLabelMap, "Test Factories",true);
				resourcePoolLabelList = new ArrayList<HashMap<String, Object>>();
				resourcePoolLabelMap = new HashMap<String, Object>();
				resourcePoolLabelMap = putDataInLabelMap(resourcePoolLabelMap, "Resource Pools",true);
				testFactoryList = new ArrayList<HashMap<String,Object>>();
				if(testFactoryLab != null){
					testFactoryLabMap = new HashMap<String, Object>();
					testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(),IDPAConstants.ENTITY_TEST_FACTORY_LAB, true, 0);
					listOfTestFactories = testFactoryDao.getTestFactoryList(testFactoryLab.getTestFactoryLabId(),1,1);
					for (TestFactory testFactory : listOfTestFactories) {
						productList = new ArrayList<HashMap<String,Object>>();
						List<ProductMaster> listOfProducts = null;
						if(testFactory != null){
							testFactoryMap = new HashMap<String, Object>();
							testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY,false,testFactoryLab.getTestFactoryLabId(),testFactory.getEngagementTypeMaster().getEngagementTypeId());
							listOfProducts = productMasterDAO.listProductsByTestFactoryId(testFactory.getTestFactoryId());
							for (ProductMaster product : listOfProducts) {
								List<ProductVersionListMaster> listOfProductVariants = null;
								productVersionList = new ArrayList<HashMap<String,Object>>();
								if(product != null){
									productMap = new HashMap<String, Object>();
									productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
									listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
									for (ProductVersionListMaster productVersion : listOfProductVariants) {
										if(productVersion != null){
												productVersionMap = new HashMap<String, Object>();
												productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION, false, product.getProductId());
												List<ProductBuild> listOfProductBuilds = null;
													productBuildList = new ArrayList<HashMap<String,Object>>();
													listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
													for (ProductBuild productBuild : listOfProductBuilds) {
														List<WorkPackage> listOfWorkPackage = null;
														if(productBuild != null){
															productBuildMap = new HashMap<String, Object>();
															productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
															workPackageList = new ArrayList<HashMap<String,Object>>();
															listOfWorkPackage = workPackageDAO.listWorkPackagesByProductBuild(productBuild.getProductBuildId());
															for (WorkPackage workPackageBean : listOfWorkPackage) {
																if(workPackageBean != null){
																	WorkFlow workFlow=null;
																		if(workPackageBean.getWorkFlowEvent()!=null){
																			workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
																			if(workFlow!=null){
																				if(workFlow.getEntityMaster()!=null){
																					if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
																						int stageId=workFlow.getStageId();
																						workPackageMap = new HashMap<String, Object>();
																						workPackageMap = putDataInJSStateTreeMap(workPackageMap, workPackageBean.getWorkPackageId(), workPackageBean.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,false,productBuild.getProductBuildId(),stageId);
																					}
																				}
																			}
																		}
																}
																workPackageList.add(workPackageMap);
															}
														}
													}
												}
											}
									}
								productMap.put("children", workPackageList);
								productList.add(productMap);
							}
							testFactoryMap.put("children", productList);
						}
						testFactoryList.add(testFactoryMap);
					}
					testFactoryLabelMap.put("children", testFactoryList);
					groupList.add(testFactoryLabelMap);
					
					resourcePoolOfSpecificTestFactoryLab = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
					if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
						listOfResourcePools = getResourcePools(listOfTestfactoryResourcePool, resourcePoolOfSpecificTestFactoryLab);
					}else{
						listOfResourcePools = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
					}
					resourcePoolList = new ArrayList<HashMap<String,Object>>();
					for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
						if(testfactoryResourcePool != null){
							resourcePoolMap = new HashMap<String, Object>();
							resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, testfactoryResourcePool.getResourcePoolId(), testfactoryResourcePool.getResourcePoolName(), IDPAConstants.ENTITY_RESOURCE_POOL,false, testFactoryLab.getTestFactoryLabId());
						}
						resourcePoolList.add(resourcePoolMap);
					}
					resourcePoolLabelMap.put("children", resourcePoolList);
					groupList.add(resourcePoolLabelMap);
					testFactoryLabMap.put("children", groupList);
				}
				testFactoryLabList.add(testFactoryLabMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryLabList);
			log.debug("returnvalue===" + returnvalue);
		}
		return returnvalue;
	}
	
	@Override
	public String getTestFactoryShiftTree(int userRoleId, int userId, int treeType) {
		log.debug("In getTestFactoryShiftTree() method");
			HashMap<String, Object> testFactoryLabMap = null;
			HashMap<String, Object> testFactoryMap = null;
			HashMap<String, Object> testFactoryShiftMap = null;
			
			List<HashMap<String, Object>> testFactoryLabList = null;
			List<HashMap<String, Object>> testFactoryLabListFinal = null;
			List<HashMap<String, Object>> testFactoryList = null;
			List<HashMap<String, Object>> testFactoryShiftList = null;
			
			List<ProductMaster> listOfProducts = null;
			List<TestFactoryLab> listOfTestFactoryLab = null;
			List<TestFactory> listOfTestFactoriesForTM = new ArrayList<TestFactory>();
			List<TestFactory> listOfTestFactoriesForTL = new ArrayList<TestFactory>();
			List<TestFactory> listOfTestFactories = null;
			
			
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
				listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
					if(listOfProducts != null && listOfProducts.size()>0){
						listOfTestFactories = new ArrayList<TestFactory>();
						listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
					for (ProductMaster productMaster : listOfProducts) {
						List<TestFactory> subListOfTestFactory = null;
						subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
						if(listOfTestFactories.size() == 0){
							listOfTestFactories = subListOfTestFactory;
						}else{
							if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
								for (TestFactory testFactory : subListOfTestFactory) {
									if(!listOfTestFactories.contains(testFactory)){
										listOfTestFactories.add(testFactory);
									}
								}
							}
						}
					}
					for (TestFactory testFactory : listOfTestFactories) {
						TestFactoryLab tFactoryLab = testFactory.getTestFactoryLab();
						if(tFactoryLab != null){
							if(!listOfTestFactoryLab.contains(tFactoryLab)){
								listOfTestFactoryLab.add(tFactoryLab);
							}
						}
					}
					listOfTestFactoriesForTM = listOfTestFactories;
					
				}
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
				listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
				if(listOfProducts != null && listOfProducts.size()>0){
					listOfTestFactories = new ArrayList<TestFactory>();
					listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
					for (ProductMaster productMaster : listOfProducts) {
						List<TestFactory> subListOfTestFactory = null;
						subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
						if(listOfTestFactories.size() == 0){
							listOfTestFactories = subListOfTestFactory;
						}else{
							if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
								for (TestFactory testFactory : subListOfTestFactory) {
									if(!listOfTestFactories.contains(testFactory)){
										listOfTestFactories.add(testFactory);
									}
								}
							}
						}
					}
					for (TestFactory testFactory : listOfTestFactories) {
						TestFactoryLab tFactoryLab = testFactory.getTestFactoryLab();
						if(tFactoryLab != null){
							if(!listOfTestFactoryLab.contains(tFactoryLab)){
								listOfTestFactoryLab.add(tFactoryLab);
							}
						}
					}
					listOfTestFactoriesForTL = listOfTestFactories;
				
				}
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
				UserList user = userDAO.getByUserId(userId);
				if(user != null){
					if(user.getResourcePool().getResourcePoolId() == -10){
						if(listOfTestFactories.size() > 0){
							listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
							for (TestFactory testFactory : listOfTestFactories) {
								TestFactoryLab tfLab = testFactoryLabDao.getTestFactoryLabBytestFactoryLabId(testFactory.getTestFactoryLab().getTestFactoryLabId());
								listOfTestFactoryLab.add(tfLab);
							}
						}
					}else{
						if(listOfTestFactories.size() == 0){
							TestFactoryLab tfLab = testfactoryResourcePoolDAO.getTestFactoryLabIdByUserResourcePool(userId);
							listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
							listOfTestFactoryLab.add(tfLab);
						}else{
							TestFactoryLab tfLab = testfactoryResourcePoolDAO.getTestFactoryLabIdByUserResourcePool(userId);
							listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
							listOfTestFactoryLab.add(tfLab);
						}
					}
				}
			}else{
				listOfTestFactoryLab = testFactoryLabDao.getTestFactoryLabsList();
			}
			
			testFactoryLabList = new ArrayList<HashMap<String,Object>>();
			testFactoryLabListFinal = new ArrayList<HashMap<String,Object>>();
			String returnvalue = null;
			if(listOfTestFactoryLab != null){
				for (TestFactoryLab testFactoryLab : listOfTestFactoryLab) {
					testFactoryList = new ArrayList<HashMap<String,Object>>();
					List<TestFactory> listOfTestFactoriesOfLab = null;
					if(testFactoryLab != null){
						testFactoryLabMap = new HashMap<String, Object>();
						testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), IDPAConstants.ENTITY_TEST_FACTORY_LAB,true,0);
						if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
							listOfTestFactoriesOfLab = testFactoryDao.getTestFactoryListByLabAndUser(testFactoryLab.getTestFactoryLabId(), 1,userId, -1,IDPAConstants.ENTITY_STATUS_ACTIVE);
						}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
							listOfTestFactoriesOfLab = listOfTestFactoriesForTM;
						}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
							listOfTestFactoriesOfLab = listOfTestFactoriesForTL;
						}
						else{
							listOfTestFactoriesOfLab = testFactoryDao.getTestFactoryList(testFactoryLab.getTestFactoryLabId(), 1,1);
						}
						for (TestFactory testFactory : listOfTestFactoriesOfLab) {
							testFactoryShiftList = new ArrayList<HashMap<String,Object>>();
							List<WorkShiftMaster> listOfShiftsOfTestFactory = null;
							if(testFactory != null){
								testFactoryMap = new HashMap<String, Object>();
								testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY,false,testFactoryLab.getTestFactoryLabId(),testFactory.getEngagementTypeMaster().getEngagementTypeId());
								if(treeType == 1){
									listOfShiftsOfTestFactory = workShiftMasterDAO.listWorkShiftsByTestFactoryId(testFactory.getTestFactoryId());
									for (WorkShiftMaster workShift : listOfShiftsOfTestFactory) {
										if(workShift != null){
											testFactoryShiftMap = new HashMap<String, Object>();
											testFactoryShiftMap = putDataInJSStateTreeMap(testFactoryShiftMap, workShift.getShiftId(), workShift.getShiftName(), IDPAConstants.ENTITY_WORK_SHIFT,false,testFactoryLab.getTestFactoryLabId(),workShift.getShiftType().getShiftTypeId());
										}
										testFactoryShiftList.add(testFactoryShiftMap);
									}
								}
							}
							if(treeType == 1){
								testFactoryMap.put("children", testFactoryShiftList);
							}
							testFactoryList.add(testFactoryMap);
						}
					}
					testFactoryLabMap.put("children", testFactoryList);
					testFactoryLabList.add(testFactoryLabMap);
					
					for(HashMap<String, Object> factory:testFactoryLabList){
						if(!testFactoryLabListFinal.contains(factory)){
							testFactoryLabListFinal.add(factory);
						}
						
						
					}
					
					
				}
				returnvalue = JSONValue.toJSONString(testFactoryLabListFinal);
			}	
			log.debug("returnvalue===" + returnvalue);
			return returnvalue;
		}



	@Override
	public String getEnvironmentCategoryGroupTree(int userRoleId, int userId) {
		log.debug("In getEnvironmentCategoryGroupTree() method");
		HashMap<String, Object> enviGroupMap = null;
		HashMap<String, Object> enviCategoryMap = null;
		HashMap<String, Object> enviGroupLabelMap = null;
		HashMap<String, Object> enviCategoryLabelMap = null;
		
		List<HashMap<String, Object>> enviGroupList = null;
		List<HashMap<String, Object>> enviCategoryList = null;
		List<HashMap<String, Object>> enviGroupLabelList = null;
		List<HashMap<String, Object>> enviCategoryLabelList = null;
		
		List<EnvironmentGroup> listOfEnviGroups = null;
		
		enviGroupList = new ArrayList<HashMap<String,Object>>();
		listOfEnviGroups = environmentDAO.getEnvironmentGroupList();
		String returnvalue = null;
		if(listOfEnviGroups != null){
			for (EnvironmentGroup enviGroup : listOfEnviGroups) {
				enviCategoryLabelList = new ArrayList<HashMap<String, Object>>();
				enviCategoryLabelMap = new HashMap<String, Object>();
				enviCategoryLabelMap = putDataInLabelMap(enviCategoryLabelMap, "Environment Category",true);
				enviCategoryList = new ArrayList<HashMap<String,Object>>();
				List<EnvironmentCategory> listOfEnviCategories = null;
				if(enviGroup != null){
					enviGroupMap = new HashMap<String, Object>();
					enviGroupMap = putDataInJSTreeMap(enviGroupMap, enviGroup.getEnvironmentGroupId(),enviGroup.getEnvironmentGroupName(), IDPAConstants.ENTITY_ENVIRONMENT_GROUP,true,0);
					listOfEnviCategories = environmentDAO.getEnvironmentCategoryListByGroup(enviGroup.getEnvironmentGroupId());
					for (EnvironmentCategory category : listOfEnviCategories) {
						if(category != null){
							enviCategoryMap = new HashMap<String, Object>();
							enviCategoryMap = putDataInJSTreeMap(enviCategoryMap, category.getEnvironmentCategoryId(),category.getEnvironmentCategoryName(), IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY,false,enviGroup.getEnvironmentGroupId());
						}
						enviCategoryList.add(enviCategoryMap);
					}
					enviCategoryLabelMap.put("children", enviCategoryList);
					enviCategoryLabelList.add(enviCategoryLabelMap);
				}
				enviGroupMap.put("children", enviCategoryLabelList);
				enviGroupList.add(enviGroupMap);
			}
			returnvalue = JSONValue.toJSONString(enviGroupList);
		}	
		log.debug("returnvalue===" + returnvalue);
		return returnvalue; 
	}



	@Override
	public String getTestFactoryLabTestFactoryProductsCompleteTree(Integer userRoleId, Integer userId) {
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> testFactoryMap = null;
		HashMap<String, Object> productMap = null;
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		List<HashMap<String, Object>> productList = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactoryLab> listOfTestFactoryLabs = null;
		List<TestFactory> listOfTestFactories = null;
		
		
		listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
		
		testFactoryLabList = new ArrayList<HashMap<String,Object>>();
		String returnvalue = null;
		if(listOfTestFactoryLabs != null){
			for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
				testFactoryList = new ArrayList<HashMap<String,Object>>();
				if(testFactoryLab != null){
					testFactoryLabMap = new HashMap<String, Object>();
					testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), IDPAConstants.ENTITY_TEST_FACTORY_LAB,true,0);
					listOfTestFactories = testFactoryDao.getTestFactoryList(testFactoryLab.getTestFactoryLabId(),1,1);
					for (TestFactory testFactory : listOfTestFactories) {
						productList = new ArrayList<HashMap<String,Object>>();
						if(testFactory != null){
							testFactoryMap = new HashMap<String, Object>();
							testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY,false,testFactoryLab.getTestFactoryLabId(),testFactory.getEngagementTypeMaster().getEngagementTypeId());
								listOfProducts = productMasterDAO.listProductsByTestFactoryId(testFactory.getTestFactoryId());
								for (ProductMaster product : listOfProducts) {
									if(product != null){
										productMap = new HashMap<String, Object>();
										productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
									}
									productList.add(productMap);
								}
						}
						testFactoryMap.put("children", productList);
						testFactoryList.add(testFactoryMap);
					}
				}
				testFactoryLabMap.put("children", testFactoryList);
				testFactoryLabList.add(testFactoryLabMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryLabList);
		}	
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}



	@Override
	public String getUserVendorTree(Integer userRoleId, Integer userId) {
		log.debug("In User Type tree() method");
		
		HashMap<String, Object> testFactoryLabMap = null;
		HashMap<String, Object> resourcePoolMap = null;
		HashMap<String, Object> vendorMap = null;
		
		List<HashMap<String, Object>> testFactoryLabList = null;
		List<HashMap<String, Object>> resourcePoolList = null;
		List<HashMap<String, Object>> vendorList = null;
		
			    testFactoryLabList = new ArrayList<HashMap<String,Object>>();
				List<TestFactoryLab> listOfTestFactoryLabs = null;
				List<TestfactoryResourcePool> listOfResourcePoolsOfResourceManager = null;
				
				if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
					listOfResourcePoolsOfResourceManager = testfactoryResourcePoolDAO.listResourcePoolByResourceManagerId(userRoleId,userId);
					for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePoolsOfResourceManager) {
						listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabByResourcePoolId(testfactoryResourcePool.getResourcePoolId());
					}
				}else{
					listOfTestFactoryLabs = testFactoryLabDao.getTestFactoryLabsList();
				}
				
				String returnvalue = null;
				if(listOfTestFactoryLabs != null && listOfTestFactoryLabs.size()>0){
					for (TestFactoryLab testFactoryLab : listOfTestFactoryLabs) {
						List<TestfactoryResourcePool> listOfResourcePools = null;
						List<TestfactoryResourcePool> resourcePoolOfSpecificTestFactoryLab = null;
						resourcePoolList = new ArrayList<HashMap<String,Object>>();
						if(testFactoryLab != null){
							testFactoryLabMap = new HashMap<String, Object>();
							testFactoryLabMap = putDataInJSTreeMap(testFactoryLabMap, testFactoryLab.getTestFactoryLabId(), testFactoryLab.getTestFactoryLabName(), IDPAConstants.ENTITY_TEST_FACTORY_LAB,true,0);
							resourcePoolOfSpecificTestFactoryLab = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
							if(userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER){
								listOfResourcePools = getResourcePools(listOfResourcePoolsOfResourceManager, resourcePoolOfSpecificTestFactoryLab);
							}else{
								listOfResourcePools = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLab.getTestFactoryLabId());
							}
							for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
								List<UserTypeMasterNew> listOfUserTypes = null;
								List<VendorMaster> listOfVendors = null;
								vendorList = new ArrayList<HashMap<String,Object>>();
								if(testfactoryResourcePool != null){
									resourcePoolMap = new HashMap<String, Object>();
									resourcePoolMap = putDataInJSTreeMap(resourcePoolMap, testfactoryResourcePool.getResourcePoolId(), testfactoryResourcePool.getDisplayName(),IDPAConstants.ENTITY_RESOURCE_POOL,false,testFactoryLab.getTestFactoryLabId());
									listOfVendors = null;
									for (VendorMaster vendor : listOfVendors) {
										vendorMap = new HashMap<String, Object>();
										vendorMap = putDataInJSTreeMap(vendorMap,  vendor.getVendorId(),  vendor.getRegisteredCompanyName(), IDPAConstants.ENTITY_VENDOR,false,testfactoryResourcePool.getResourcePoolId());
										vendorList.add(vendorMap);
									}
								}
								resourcePoolMap.put("children", vendorList);
								resourcePoolList.add(resourcePoolMap);
							}
						}
						testFactoryLabMap.put("children", resourcePoolList);
						testFactoryLabList.add(testFactoryLabMap);
					}
					returnvalue = JSONValue.toJSONString(testFactoryLabList);
				}
				log.debug("returnvalue===" + returnvalue);
				return returnvalue;
	}
	
	
	
	
	@Override
	public String getProductVersionTree(int userRoleId, int userId) {
		log.debug("In getProductHierarchyTree() method");
		
		HashMap<String, Object> testFactoryMap = null;
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;

		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
				productList = new ArrayList<HashMap<String,Object>>();
				List<TestFactory> listOfTestFactories = null;
				List<ProductMaster> listOfProducts = null;
				if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
					listOfProducts = productMasterDAO.list(false);
				}
				else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
					listOfProducts = getUserAssociatedProducts(userRoleId,userId,0);
				}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
					listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
					 if(listOfTestFactories != null && listOfTestFactories.size()>0){
						 listOfProducts = new ArrayList<ProductMaster>();
						 for (TestFactory testFactory : listOfTestFactories) {
							List<ProductMaster> subListOfProducts = null;
							subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
							if(listOfProducts.size() == 0){
								listOfProducts = subListOfProducts;
							}else{
								listOfProducts.addAll(subListOfProducts);
							}
						}
					 }
				}
				if(listOfProducts != null){
					for (ProductMaster product : listOfProducts) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						productVersionList = new ArrayList<HashMap<String,Object>>();
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
										productVersionMap = new HashMap<String, Object>();
										productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									}
									productVersionList.add(productVersionMap);
								}
							}
						productMap.put("children", productVersionList);
						productList.add(productMap);
					}
					returnvalue = JSONValue.toJSONString(productList);
				}
				
				log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}

	
	@Override
	@Transactional
	public String getAllocateTestcaseFilter(UserList user,
			int workpackageId,String param) {
		log.debug("In getAllocateTestcaseFilter() method "+param);
		
		HashMap<String, Object> testerMap = null;
		HashMap<String, Object> testLeadMap = null;
		HashMap<String, Object> environmentMap = null;
		HashMap<String, Object> deviceMap = null;
		
		HashMap<String, Object> dcMap = null;
		HashMap<String, Object> workPackageMap = null;
		HashMap<String, Object> executionPriorityMap = null;
		HashMap<String, Object> resultMap = null;

		
		HashMap<String, Object> testerLabelMap = null;
		HashMap<String, Object> testLeadLabelMap = null;
		HashMap<String, Object> environmentLabelMap = null;
		HashMap<String, Object> deviceLabelMap = null;
		
		HashMap<String, Object> dcLabelMap = null;
		HashMap<String, Object> executionPriorityLabelMap = null;
		HashMap<String, Object> resultLabelMap = null;

		List<HashMap<String, Object>> testerList = null;
		List<HashMap<String, Object>> testLeadList = null;
		List<HashMap<String, Object>> environmentList = null;
		List<HashMap<String, Object>> deviceList = null;
		List<HashMap<String, Object>> dcList = null;
		List<HashMap<String, Object>> workPackageList = null;
		List<HashMap<String, Object>> executionPriorityList = null;
		List<HashMap<String, Object>> resultList = null;

		
		List<HashMap<String, Object>> testerLabelList = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> testLeadLabelList = null;
		List<HashMap<String, Object>> environmentLabelList = null;
		List<HashMap<String, Object>> deviceLabelList = null;
		
		List<HashMap<String, Object>> dcLabelList = null;
		List<HashMap<String, Object>> executionPriorityLabelList = null;
		List<HashMap<String, Object>> resultLabelList = null;

		
		List<UserList> listOfTester = null;
		List<UserList> listOfTestLead = null;
		List<EnvironmentCombination> listOfEnvironments = null;
		List<GenericDevices> listOfDevices = null;
		
		List<DecouplingCategory> listOfDC = null;
		List<WorkPackage> listOfWorkpPackage = null;
		List<ExecutionPriority> listOfExecutionPriority = null;
		List<String> listOfResult = null;


		String returnvalue = null;
		if(workpackageId!=0){
			WorkPackage workpackage = workPackageDAO.getWorkPackageById(workpackageId);
			testerList = new ArrayList<HashMap<String,Object>>();
			testLeadList = new ArrayList<HashMap<String,Object>>();
			environmentList = new ArrayList<HashMap<String,Object>>();
			deviceList = new ArrayList<HashMap<String,Object>>();
			
			dcList = new ArrayList<HashMap<String,Object>>();
			executionPriorityList = new ArrayList<HashMap<String,Object>>();
			resultList = new ArrayList<HashMap<String,Object>>();

			if(workpackage != null){
				if(param!=null && param.equalsIgnoreCase("dc")){
					dcLabelMap = new HashMap<String, Object>();
					dcLabelList = new ArrayList<HashMap<String, Object>>();
					Integer dcLabelId = new Integer(0);
					dcLabelMap = putDataInLabelMap(dcLabelMap, "Decoupling Category",true);
					listOfDC= new ArrayList<DecouplingCategory>();
					listOfDC=  decouplingCategoryDAO.getDCByWorkpackage(workPackageDAO.getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workpackageId),workpackageId);
					log.debug(">>>>+listOfDC>>"+listOfDC);
					for (DecouplingCategory dc : listOfDC) {
						if(dc != null){
							dcMap = new HashMap<String, Object>();
							dcMap=putDataInJSTreeMap(dcMap, dc.getDecouplingCategoryId(), dc.getDecouplingCategoryName(), "Decoupling Category",false,dcLabelId);
						}
						dcList.add(dcMap);
					}
					dcLabelMap.put("children", dcList);
					dcLabelList.add(dcLabelMap);
					returnvalue = JSONValue.toJSONString(dcLabelList);
					log.debug("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("env")){
					environmentLabelMap = new HashMap<String, Object>();
					environmentLabelList = new ArrayList<HashMap<String, Object>>();
					environmentLabelMap = new HashMap<String, Object>();
					Integer environmentLabelId = new Integer(0);
					environmentLabelMap = putDataInLabelMap(environmentLabelMap, "Environment Combination",true);

					listOfEnvironments= new ArrayList<EnvironmentCombination>();
					Set<EnvironmentCombination> environments = workPackageDAO.getEnvironmentCombinationMappedToWorkpackage(workpackageId);
					log.debug(">>>>+environments>>"+environments);

					if(environments!=null && !environments.isEmpty()){
						log.debug("environments>>>>>"+environments.size());
						listOfEnvironments=new ArrayList<EnvironmentCombination>(environments);
					}
					
					for (EnvironmentCombination environment : listOfEnvironments) {
						if(environment != null){
							environmentMap = new HashMap<String, Object>();
							environmentMap=putDataInJSTreeMap(environmentMap, environment.getEnvironment_combination_id(), environment.getEnvironmentCombinationName(), "Environment Combination",false,environmentLabelId);
						}
						environmentList.add(environmentMap);
					}
					environmentLabelMap.put("children", environmentList);
					environmentLabelList.add(environmentLabelMap);
					returnvalue = JSONValue.toJSONString(environmentLabelList);
					log.debug("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("device")){
					deviceLabelMap = new HashMap<String, Object>();
					deviceLabelList = new ArrayList<HashMap<String, Object>>();
					deviceLabelMap = new HashMap<String, Object>();
					Integer deviceLabelId = new Integer(0);
					deviceLabelMap = putDataInLabelMap(deviceLabelMap, "Device",true);

					listOfDevices= new ArrayList<GenericDevices>();
					Set<GenericDevices> devices = workPackageDAO.getDevicesMappedToWorkpackage(workpackageId);
					log.debug(">>>>+environments>>"+devices);

					if(devices!=null && !devices.isEmpty()){
						log.debug("devices>>>>>"+devices.size());
						listOfDevices=new ArrayList<GenericDevices>(devices);
					}
					
					for (GenericDevices dev : listOfDevices) {
						if(dev != null){
							deviceMap = new HashMap<String, Object>();
							deviceMap=putDataInJSTreeMap(deviceMap, dev.getGenericsDevicesId(), dev.getName(), "Device",false,deviceLabelId);
						}
						deviceList.add(deviceMap);
					}
					deviceLabelMap.put("children", deviceList);
					deviceLabelList.add(deviceLabelMap);
					returnvalue = JSONValue.toJSONString(deviceLabelList);
					log.debug("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("ep")){
					executionPriorityLabelMap = new HashMap<String, Object>();
					executionPriorityLabelList = new ArrayList<HashMap<String, Object>>();
					executionPriorityLabelMap = new HashMap<String, Object>();
					executionPriorityLabelMap = putDataInLabelMap(executionPriorityLabelMap, "Execution Priority",true);
					listOfExecutionPriority = workPackageDAO.getExecutionPriority();
					log.debug(">>>>+listOfExecutionPriority>>"+listOfExecutionPriority);

					for (ExecutionPriority ep : listOfExecutionPriority) {
						if(ep != null){
							executionPriorityMap = new HashMap<String, Object>();
							executionPriorityMap=putDataInJSTreeMap(executionPriorityMap, ep.getExecutionPriorityId(), ep.getDisplayName(), "Execution Priority", true, 0);
						}
						executionPriorityList.add(executionPriorityMap);
					}
					executionPriorityLabelMap.put("children", executionPriorityList);
					executionPriorityLabelList.add(executionPriorityLabelMap);
					returnvalue = JSONValue.toJSONString(executionPriorityLabelList);
					log.debug("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("tester")){
					testerLabelMap = new HashMap<String, Object>();
					testerLabelList = new ArrayList<HashMap<String, Object>>();
					Integer testerLabelId = new Integer(0);
					testerLabelMap = putDataInLabelMap(testerLabelMap, "Testers",true);
					listOfTester = workPackageDAO.getAllocatedUserListByRole(workpackageId,IDPAConstants.ROLE_ID_TESTER);
					for (UserList tester : listOfTester) {
						if(tester != null){
							testerMap = new HashMap<String, Object>();
							testerMap=putDataInJSTreeMap(testerMap,tester.getUserId(),tester.getLoginId(),"Tester",false,testerLabelId);
						}
						testerList.add(testerMap);
					}
					testerLabelMap.put("children",testerList);
					testerLabelList.add(testerLabelMap);
					returnvalue = JSONValue.toJSONString(testerLabelList);
					log.info("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("testlead")){
					testLeadLabelMap = new HashMap<String, Object>();
					testLeadLabelList = new ArrayList<HashMap<String, Object>>();
					testLeadLabelMap = new HashMap<String, Object>();
					Integer testerLeadLabelId = new Integer(0);
					testLeadLabelMap = putDataInLabelMap(testLeadLabelMap, "Test Lead",true);
					listOfTestLead = new ArrayList<UserList>();
					listOfTestLead = workPackageDAO.getAllocatedUserListByRole(workpackageId,IDPAConstants.ROLE_ID_TEST_LEAD);
					log.debug(">>>>+listOfTestLead>>"+listOfTestLead);

					for (UserList testLead : listOfTestLead) {
						if(testLead != null){
							testLeadMap = new HashMap<String, Object>();
							testLeadMap=putDataInJSTreeMap(testLeadMap, testLead.getUserId(), testLead.getLoginId(), "Test Lead",false,testerLeadLabelId);
						}
						testLeadList.add(testLeadMap);
					}
					testLeadLabelMap.put("children", testLeadList);
					testLeadLabelList.add(testLeadLabelMap);
					returnvalue = JSONValue.toJSONString(testLeadLabelList);
					log.info("returnvalue===" + returnvalue);
				}else if(param!=null && param.equalsIgnoreCase("result")){
					resultMap = new HashMap<String, Object>();
					resultLabelList = new ArrayList<HashMap<String, Object>>();
					resultLabelMap = new HashMap<String, Object>();
					resultLabelMap = putDataInLabelMap(resultLabelMap, "Test Case Result",true);
					listOfResult = new ArrayList<String>();
					listOfResult.add("Pass");
					listOfResult.add("Fail");
					listOfResult.add("NoRun");
					listOfResult.add("Blocked");
					//log.info("listOfResult===" + listOfResult);
					for (String result : listOfResult) {
						log.info("result===" + result);
						if(result != null && result.equals("Pass")){
							resultMap = new HashMap<String, Object>();
							resultMap=putDataInJSTreeMap(resultMap, 1, "Pass", "Test Case Result",true,0);
						}else if(result != null && result.equals("Fail")){
							resultMap = new HashMap<String, Object>();
							resultMap=putDataInJSTreeMap(resultMap, 2, "Fail", "Test Case Result",true,0);
						}else if(result != null && result.equals("NoRun")){
							resultMap = new HashMap<String, Object>();
							resultMap=putDataInJSTreeMap(resultMap, 3, "NoRun", "Test Case Result",true,0);
						}else if(result != null && result.equals("Blocked")){
							resultMap = new HashMap<String, Object>();
							resultMap=putDataInJSTreeMap(resultMap, 4, "Blocked", "Test Case Result",true,0);
						}
						resultList.add(resultMap);
					}
					resultLabelMap.put("children", resultList);
					resultLabelList.add(resultLabelMap);
					returnvalue = JSONValue.toJSONString(resultLabelList);
					log.info("returnvalue===" + returnvalue);
				}
			}
			
			
		}
		return returnvalue;
	}



	@Override
	public String getProductWithTFTree(int userRoleId, int userId) {
		log.debug("In getProductTree() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		try {
			List<ProductMaster> listOfProducts = null;
			List<TestFactory> listOfTestFactories = null;
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
				listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
				if(listOfProducts != null && listOfProducts.size()>0){
					listOfTestFactories = new ArrayList<TestFactory>();
					for (ProductMaster productMaster : listOfProducts) {
						List<TestFactory> subListOfTestFactory = null;
						subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
						if(listOfTestFactories.size() == 0){
							listOfTestFactories = subListOfTestFactory;
						}else{
							if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
								for (TestFactory testFactory : subListOfTestFactory) {
									if(!listOfTestFactories.contains(testFactory)){
										listOfTestFactories.add(testFactory);
									}
								}
							}
						}
					}
				}
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			}else{
				listOfTestFactories = testFactoryDao.getTestFactoryList();
				listOfProducts = productMasterDAO.list(false);
			}
			testFactoryList = new ArrayList<HashMap<String,Object>>();
			if(listOfTestFactories != null && listOfTestFactories.size()>0){
				for (TestFactory testFactory : listOfTestFactories) {
					if(testFactory != null){
						List<ProductMaster> productListforTestFactoryId = null;
						List<ProductMaster> listOfProductsToConsider = null;
						testFactoryMap = new HashMap<String, Object>();
						testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
						productList = new ArrayList<HashMap<String,Object>>();
						productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
						if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
							 listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
						for (ProductMaster product : listOfProductsToConsider) {
							if(product != null){
								productMap = new HashMap<String, Object>();
								productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
							}
							productList.add(productMap);
						}
						
					}
					testFactoryMap.put("children", productList);
					testFactoryList.add(testFactoryMap);
				}
				returnvalue = JSONValue.toJSONString(testFactoryList);
			}
		} catch (Exception e) {
			log.error("ERROR getTING Product With TF Tree",e);
		}
		return returnvalue;	
	}

	@Override
	@Transactional
	public String getDeviceLabTree(int userRoleId, int userId) {
		log.debug("In getDeviceLabTree() method");
		HashMap<String, Object> deviceLabMap = null;
		HashMap<String, Object> deviceLabLabelMap = null;
		List<HashMap<String, Object>> deviceLabList = null;
		List<HashMap<String, Object>> deviceLabLabelList = null;
		String returnvalue = null;
		
		List<DeviceLab> listOfDeviceLab = null;
		listOfDeviceLab= new ArrayList<DeviceLab>();

		
		deviceLabList = new ArrayList<HashMap<String,Object>>();
		deviceLabMap = new HashMap<String, Object>();
		deviceLabLabelList = new ArrayList<HashMap<String, Object>>();
		deviceLabLabelMap = new HashMap<String, Object>();

		deviceLabLabelMap = putDataInLabelMap(deviceLabLabelMap, "Device Lab",true);
		listOfDeviceLab = productMasterDAO.listDeviceLab();

		if(listOfDeviceLab != null && listOfDeviceLab.size()>0){
			for (DeviceLab deviceLab : listOfDeviceLab) {
				if(deviceLab != null){
					deviceLabMap = new HashMap<String, Object>();
					deviceLabMap = putDataInJSTreeMap(deviceLabMap, deviceLab.getDevice_lab_Id(), deviceLab.getDevice_lab_name(), IDPAConstants.ENTITY_DEVICE_LAB, true,0);
					log.debug("Test Factory Name: "+deviceLab.getDevice_lab_name());
					deviceLabList.add(deviceLabMap);
				}
				deviceLabLabelMap.put("children", deviceLabList);
				
			}
			deviceLabLabelList.add(deviceLabLabelMap);
			returnvalue = JSONValue.toJSONString(deviceLabLabelList);
		}
		return returnvalue;
	}

		@Override
	public String getWorkPackageTestCaseReviewTree(Integer userRoleId, Integer userId) {
			log.debug("getWorkPackageTestCaseReviewTree() for User Role Id: ..."+userRoleId);
		
		HashMap<String, Object> productMap = null;
		List<HashMap<String, Object>> productList = null;
		List<ProductMaster> listOfProducts = null;
		List<ProductMaster> listOfUserAssociatedProducts = null;
		
		HashMap<String, Object> productVersionMap = null;
		List<HashMap<String, Object>> productVersionList = null;
		
		HashMap<String, Object> productBuildMap = null;
		List<HashMap<String, Object>> productBuildList = null;
		
		
		HashMap<String, Object> workPackageMap = null;
		List<HashMap<String, Object>> workPackageList = null;
				
				String returnValue = "";
				try {
					productList = new ArrayList<HashMap<String,Object>>();
					listOfProducts = productMasterDAO.getProductsByAllocation(userId);
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
						listOfUserAssociatedProducts = getUserAssociatedProducts(userRoleId,userId,1);
						if(listOfUserAssociatedProducts != null && listOfUserAssociatedProducts.size()>0){
							if(listOfProducts == null || listOfProducts.size() ==0){
								listOfProducts.addAll(listOfUserAssociatedProducts);
							}else{
								for(ProductMaster product1 : listOfUserAssociatedProducts){
									if(!listOfProducts.contains(product1)){
										listOfProducts.add(product1);
									}
								}
							}
						}
					}
					if(listOfProducts != null && listOfProducts.size()>0){
						for (ProductMaster product : listOfProducts) {
							List<ProductVersionListMaster> listOfProductVariants = null;
							if(product != null){
								productMap = new HashMap<String, Object>();
								productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
								productVersionList = new ArrayList<HashMap<String,Object>>();
								listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
								for (ProductVersionListMaster productVersion : listOfProductVariants) {
									if(productVersion != null){
										productVersionMap = new HashMap<String, Object>();
										productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION, false,product.getProductId());
										List<ProductBuild> listOfProductBuilds = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
										for (ProductBuild productBuild : listOfProductBuilds) {
											List<WorkPackage> listOfWorkPackage = null;
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,false,product.getProductId());
												workPackageList = new ArrayList<HashMap<String,Object>>();
												listOfWorkPackage = workPackageDAO.listWorkPackagesByProductBuild(productBuild.getProductBuildId());
												for (WorkPackage workPackageBean : listOfWorkPackage) {
													if(workPackageBean != null){
														WorkFlow workFlow=null;
													if(workPackageBean.getWorkFlowEvent()!=null){
														workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
															if(workFlow!=null){
																if(workFlow.getEntityMaster()!=null){
																	if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
																		int stageId=workFlow.getStageId();
																		workPackageMap = new HashMap<String, Object>();
																		workPackageMap = putDataInJSStateTreeMap(workPackageMap, workPackageBean.getWorkPackageId(), workPackageBean.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,false,productBuild.getProductBuildId(),stageId);
																	}
																}
															}
														}
													}
													workPackageList.add(workPackageMap);
												}
											}
											productBuildMap.put("children", workPackageList);
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
							}
							productMap.put("children", productVersionList);
							productList.add(productMap);
						}
						returnValue = JSONValue.toJSONString(productList);
					}else{
						returnValue = null;
					}
				} catch (Exception e) {
					log.error("ERROR  ",e);
				}
				return returnValue;
		}
		@Override
		public String getProductActivityTree(int userRoleId, int userId,String filter) {
			log.debug("In getProductHierarchyTree() method");
			
			HashMap<String, Object> productMap = null;
			HashMap<String, Object> productVersionMap = null;
			HashMap<String, Object> productBuildMap = null;
			HashMap<String, Object> activityWpMap = null;
			HashMap<String, Object> activityMap = null;
			
			List<HashMap<String, Object>> productList = null;
			List<HashMap<String, Object>> productVersionList = null;
			List<HashMap<String, Object>> productBuildList = null;
			List<HashMap<String, Object>> activityWpList = null;
			List<HashMap<String, Object>> activityList = null;
			
			String returnvalue = null;
					productList = new ArrayList<HashMap<String,Object>>();
					List<TestFactory> listOfTestFactories = null;
					List<ProductMaster> listOfProducts = null;
					if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
						listOfProducts = productMasterDAO.list(false);
					}
					else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
						listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
					}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
						 if(listOfTestFactories != null && listOfTestFactories.size()>0){
							 listOfProducts = new ArrayList<ProductMaster>();
							 for (TestFactory testFactory : listOfTestFactories) {
								List<ProductMaster> subListOfProducts = null;
								subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
								if(listOfProducts.size() == 0){
									listOfProducts = subListOfProducts;
								}else{
									listOfProducts.addAll(subListOfProducts);
								}
							}
						 }
					}
					if(listOfProducts != null){
						for (ProductMaster product : listOfProducts) {
							List<ProductVersionListMaster> listOfProductVariants = null;
							productVersionList = new ArrayList<HashMap<String,Object>>();
							if(product != null){
								productMap = new HashMap<String, Object>();
								productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
								listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
								for (ProductVersionListMaster productVersion : listOfProductVariants) {
									if(productVersion != null){
										productVersionMap = new HashMap<String, Object>();
										productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
										List<ProductBuild> listOfProductBuilds = null;
											productBuildList = new ArrayList<HashMap<String,Object>>();
											listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
											List<ActivityWorkPackage> listOfActivityWorkPackages = null;
											for (ProductBuild productBuild : listOfProductBuilds) {
												if(productBuild != null){
													productBuildMap = new HashMap<String, Object>();
													productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
													activityWpList = new ArrayList<HashMap<String,Object>>();
													listOfActivityWorkPackages = activityWorkPackageDAO.listActivityWorkPackagesByBuildId(productBuild.getProductBuildId(),1);
													for (ActivityWorkPackage activityWorkPackage : listOfActivityWorkPackages) {
														if(activityWorkPackage != null){
															activityWpMap = new HashMap<String, Object>();
															activityWpMap = putDataInJSTreeMap(activityWpMap, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, false, productBuild.getProductBuildId());
															List<Activity> listOfActivities = null;
															if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER ){
																listOfActivities = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackage.getActivityWorkPackageId(), 0,1);
															}else {
																listOfActivities = activityDAO.listActivitiesByActivityWorkPackageIdAndUserId(activityWorkPackage.getActivityWorkPackageId(), 0,1,userId,filter);
															}
															activityList= new ArrayList<HashMap<String,Object>>();
															for (Activity activity : listOfActivities) {
																if(activity != null){
																	activityMap = new HashMap<String, Object>();
																	activityMap = setActivityInitialInformation(activity);
																	activityMap = putDataInJSTreeMap(activityMap, activity.getActivityId(), activity.getActivityName(), IDPAConstants.ENTITY_ACTIVITY, false, activityWorkPackage.getActivityWorkPackageId());
																}
																activityList.add(activityMap);
															}
															}      
														       activityWpMap.put("children",activityList);
														       activityWpList.add(activityWpMap);
													}
												}
												productBuildMap.put("children",activityWpList);
												productBuildList.add(productBuildMap);
											}
										}
										productVersionMap.put("children", productBuildList);
										productVersionList.add(productVersionMap);
									}
								}
							productMap.put("children", productVersionList);
							productList.add(productMap);
						}
						returnvalue = JSONValue.toJSONString(productList);
					}
					
					log.debug("returnvalue===" + returnvalue);
			return returnvalue;
		}
		@Override
		public String getProductActivityWorkPackageTree(int userRoleId, int userId) {
			log.debug("In getProductHierarchyTree() method");
			
			HashMap<String, Object> productMap = null;
			HashMap<String, Object> productVersionMap = null;
			HashMap<String, Object> productBuildMap = null;
			HashMap<String, Object> activityWpMap = null;
			
			
			List<HashMap<String, Object>> productList = null;
			List<HashMap<String, Object>> productVersionList = null;
			List<HashMap<String, Object>> productBuildList = null;
			List<HashMap<String, Object>> activityWpList = null;
			
			String returnvalue = null;
					productList = new ArrayList<HashMap<String,Object>>();
					List<TestFactory> listOfTestFactories = null;
					List<ProductMaster> listOfProducts = null;
					if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
						listOfProducts = productMasterDAO.list(false);
					}
					else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER || userRoleId == IDPAConstants.ROLE_ID_PQA_REVIEWER){
						listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
					}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
						 if(listOfTestFactories != null && listOfTestFactories.size()>0){
							 listOfProducts = new ArrayList<ProductMaster>();
							 for (TestFactory testFactory : listOfTestFactories) {
								List<ProductMaster> subListOfProducts = null;
								subListOfProducts = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
								if(listOfProducts.size() == 0){
									listOfProducts = subListOfProducts;
								}else{
									listOfProducts.addAll(subListOfProducts);
								}
							}
						 }
					}
					if(listOfProducts != null){
						for (ProductMaster product : listOfProducts) {
							List<ProductVersionListMaster> listOfProductVariants = null;
							productVersionList = new ArrayList<HashMap<String,Object>>();
							if(product != null){
								productMap = new HashMap<String, Object>();
								productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
								listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
								for (ProductVersionListMaster productVersion : listOfProductVariants) {
									if(productVersion != null){
										productVersionMap = new HashMap<String, Object>();
										productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
										List<ProductBuild> listOfProductBuilds = null;
											productBuildList = new ArrayList<HashMap<String,Object>>();
											listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
											List<ActivityWorkPackage> listOfActivityWorkPackages = null;
											for (ProductBuild productBuild : listOfProductBuilds) {
												if(productBuild != null){
													productBuildMap = new HashMap<String, Object>();
													productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
													activityWpList = new ArrayList<HashMap<String,Object>>();
													listOfActivityWorkPackages = activityWorkPackageDAO.listActivityWorkPackagesByBuildId(productBuild.getProductBuildId(),1);
													for (ActivityWorkPackage activityWorkPackage : listOfActivityWorkPackages) {
														if(activityWorkPackage != null){
															activityWpMap = new HashMap<String, Object>();
															activityWpMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedStartDate()));
															activityWpMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedEndDate()));
															if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0 && (activityWorkPackage.getWorkflowStatus().getIsLifeCycleStage() != null && activityWorkPackage.getWorkflowStatus().getIsLifeCycleStage() == 1)){
																activityWpMap.put("lifeCycleStage", activityWorkPackage.getWorkflowStatus().getWorkflowStatusId());
															}
															activityWpMap = putDataInJSTreeMap(activityWpMap, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, false, productBuild.getProductBuildId());
														}
														 activityWpList.add(activityWpMap);  
													}
												}
												productBuildMap.put("children",activityWpList);
												productBuildList.add(productBuildMap);
											}
										}
										productVersionMap.put("children", productBuildList);
										productVersionList.add(productVersionMap);
									}
								}
							productMap.put("children", productVersionList);
							productList.add(productMap);
						}
						returnvalue = JSONValue.toJSONString(productList);
					}
					
					log.debug("returnvalue===" + returnvalue);
			return returnvalue;
		}
		
		
	@Override
	public String getCustomersTreeData(int userRoleId, int userId) {
		log.debug("In getCustomersTreeData() method");
		
		HashMap<String, Object> customerMap = null;
		List<HashMap<String, Object>> customerList = null;
		customerList = new ArrayList<HashMap<String,Object>>();
		List<Customer> listOfCustomers = null;
		listOfCustomers = customerDAO.list(1, null, null);
		String returnvalue = null;
		if(listOfCustomers != null && listOfCustomers.size()>0){
			for (Customer customer : listOfCustomers) {
				if(customer != null){
					customerMap = new HashMap<String, Object>();
					customerMap = putDataInJSTreeMap(customerMap, customer.getCustomerId(), customer.getCustomerName(),IDPAConstants.ENTITY_CUSTOMER,true,0);
				}
				customerList.add(customerMap);
			}
			returnvalue = JSONValue.toJSONString(customerList);
		}
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}
	
	@Override
	public String getActivityTree(int userRoleId, int userId, String filter) {
		log.debug("In getActivityTree() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> activityWpMap = null;
		HashMap<String, Object> activityMap = null;
		
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> activityWpList = null;
		List<HashMap<String, Object>> activityList = null;
		
		String returnvalue = null;
				productList = new ArrayList<HashMap<String,Object>>();
				List<TestFactory> listOfTestFactories = null;
				List<ProductMaster> listOfProducts = null;
				List<ProductVersionListMaster> listOfUserAssociatedProductVariants = null;
				List<ProductBuild> listOfUserAssociatedProductBuild = null;
				List<ActivityWorkPackage> listOfUserAssociatedActivityWorkPackages = null;
				List<Activity> listOfUserAssociatedActivities = null;
				
					listOfProducts = getProductsByActivityForUserId(userRoleId,userId,filter);
					listOfUserAssociatedProductVariants = activityDAO.getUserAssociatedProductVariants(userId,filter);
					listOfUserAssociatedProductBuild = activityDAO.getUserAssociatedProductBuilds(userId,filter);
					listOfUserAssociatedActivityWorkPackages = activityDAO.getUserAssociatedActivityWorkPackages(userId,filter);
					listOfUserAssociatedActivities = activityDAO.getUserAssociatedActivities(userId,filter);
				if(listOfProducts != null){
					for (ProductMaster product : listOfProducts) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						List<ProductVersionListMaster> listOfProductVariantsOfProduct = null;
						productVersionList = new ArrayList<HashMap<String,Object>>();
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
							listOfProductVariantsOfProduct = productVersionListMasterDAO.list(product.getProductId());
							listOfProductVariants = getProductVersionList(listOfUserAssociatedProductVariants, listOfProductVariantsOfProduct);
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									List<ProductBuild> listOfProductBuilds = null;
									List<ProductBuild> listOfProductBuildsOfProductVersion = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										listOfProductBuildsOfProductVersion = productBuildDAO.list(productVersion.getProductVersionListId());
										listOfProductBuilds = getProductBuilds(listOfUserAssociatedProductBuild,listOfProductBuildsOfProductVersion);
										List<ActivityWorkPackage> listOfActivityWorkPackages = null;
										List<ActivityWorkPackage> listOfActivityWorkPackagesOfBuild = null;
										for (ProductBuild productBuild : listOfProductBuilds) {
											List<Activity> listOfActivities = null;
											List<Activity> listOfActivitiesOfActivityWp = null;
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
												activityWpList = new ArrayList<HashMap<String,Object>>();
												listOfActivityWorkPackagesOfBuild = activityWorkPackageDAO.listActivityWorkPackagesByBuildId(productBuild.getProductBuildId(),1);
												listOfActivityWorkPackages = getActivityWorkPackage(listOfUserAssociatedActivityWorkPackages,listOfActivityWorkPackagesOfBuild);
												for (ActivityWorkPackage activityWorkPackage : listOfActivityWorkPackages) {
													if(activityWorkPackage != null){
														activityWpMap = new HashMap<String, Object>();
														activityWpMap = putDataInJSTreeMap(activityWpMap, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, false, productBuild.getProductBuildId());
														activityList = new ArrayList<HashMap<String,Object>>();
														listOfActivitiesOfActivityWp = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackage.getActivityWorkPackageId(),0,1);
														listOfActivities = getActivities(listOfUserAssociatedActivities, listOfActivitiesOfActivityWp);
														for (Activity activity : listOfActivities) {
															if(activity != null){
																activityMap = new HashMap<String, Object>();
																activityMap = putDataInJSTreeMap(activityMap, activity.getActivityId(), activity.getActivityName(), IDPAConstants.ENTITY_ACTIVITY, false, activityWorkPackage.getActivityWorkPackageId());
															}
															activityList.add(activityMap);
														}
														
													}
													activityWpMap.put("children", activityList);
													activityWpList.add(activityWpMap);  
												}
											}
											productBuildMap.put("children",activityWpList);
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
							}
						productMap.put("children", productVersionList);
						productList.add(productMap);
					}
					returnvalue = JSONValue.toJSONString(productList);
				}
				
				log.info("returnvalue===" + returnvalue);
		return returnvalue;
	}



	@Override
	public String getActivityTreeForPqaReviewer(Integer userRoleId,
			Integer userId, String roleType) {
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> activityWpMap = null;
		HashMap<String, Object> activityMap = null;
		
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> activityWpList = null;
		List<HashMap<String, Object>> activityList = null;
		String returnvalue = null;
		List<ProductMaster> listOfProducts = null;
		productList = new ArrayList<HashMap<String,Object>>();
		listOfProducts = productMasterDAO.list(false);
		if(listOfProducts != null){
			for (ProductMaster product : listOfProducts) {
				List<ProductVersionListMaster> listOfProductVariants = null;
				productVersionList = new ArrayList<HashMap<String,Object>>();
				if(product != null){
					productMap = new HashMap<String, Object>();
					productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
					listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
					for (ProductVersionListMaster productVersion : listOfProductVariants) {
						if(productVersion != null){
							productVersionMap = new HashMap<String, Object>();
							productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
							List<ProductBuild> listOfProductBuilds = null;
								productBuildList = new ArrayList<HashMap<String,Object>>();
								listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
								List<ActivityWorkPackage> listOfActivityWorkPackages = null;
								for (ProductBuild productBuild : listOfProductBuilds) {
									if(productBuild != null){
										productBuildMap = new HashMap<String, Object>();
										productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
										activityWpList = new ArrayList<HashMap<String,Object>>();
										listOfActivityWorkPackages = activityWorkPackageDAO.listActivityWorkPackagesByBuildId(productBuild.getProductBuildId(),1);
										for (ActivityWorkPackage activityWorkPackage : listOfActivityWorkPackages) {
											if(activityWorkPackage != null){
												activityWpMap = new HashMap<String, Object>();
												activityWpMap = putDataInJSTreeMap(activityWpMap, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, false, productBuild.getProductBuildId());
												List<Activity> listOfActivities = null;
												listOfActivities = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackage.getActivityWorkPackageId(), -1,1);
												activityList= new ArrayList<HashMap<String,Object>>();
													for (Activity activity : listOfActivities) {
														if(activity != null){
															activityMap = new HashMap<String, Object>();
															activityMap = putDataInJSTreeMap(activityMap, activity.getActivityId(), activity.getActivityName(), IDPAConstants.ENTITY_ACTIVITY, false, activityWorkPackage.getActivityWorkPackageId());
														}
														activityList.add(activityMap);
													}
												}      
										       activityWpMap.put("children",activityList);
										       activityWpList.add(activityWpMap);
										}
									}
									productBuildMap.put("children",activityWpList);
									productBuildList.add(productBuildMap);
								}
							}
							productVersionMap.put("children", productBuildList);
							productVersionList.add(productVersionMap);
						}
					}
				productMap.put("children", productVersionList);
				productList.add(productMap);
			}
			returnvalue = JSONValue.toJSONString(productList);
		}
		
		log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}
	
	@Override
	@Transactional
	public String getDimensionTreeByType(int userRoleId, int userId, String filer, Integer dimensionTypeId) {
		log.debug("In workPackageTreeData() method");
		HashMap<String, Object> competencyParentMap = null;
		HashMap<String, Object> competencyMap = null;
		List<HashMap<String, Object>> competencyList = null;
		List<HashMap<String, Object>> competencyParentList = null;
		
		competencyParentList = new ArrayList<HashMap<String,Object>>();
		competencyList = new ArrayList<HashMap<String,Object>>();
		
		List<DimensionMaster> listOfCompetencies = null;
		// Temporarily added  Resource manager and  Program manager with Admin. Need to bring in different tree for these roles.
		//This tree comes for all role in fill my availability.
		if(userRoleId == IDPAConstants.ROLE_ID_ADMIN || userRoleId == IDPAConstants.ROLE_ID_RESOURCE_MANAGER || userRoleId == IDPAConstants.ROLE_ID_PROGRAM_MANAGER){
			listOfCompetencies = dimensionDAO.getDimensionList(1, null, null, false, dimensionTypeId);
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
			listOfCompetencies = dimensionDAO.getDimensionListByUserId(1, userId, dimensionTypeId);
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfCompetencies = dimensionDAO.getDimensionList(1, null, null, false, dimensionTypeId);
		}
		
		String returnvalue = "";
		if(listOfCompetencies != null){
			for(DimensionMaster competency : listOfCompetencies) {
				if(competency != null){
					competencyMap = new HashMap<String, Object>();
					competencyMap = putDataInJSStateTreeMap(competencyMap, competency.getDimensionId(), competency.getName(), IDPAConstants.ENTITY_COMPETENCY,false,0,0);
					competencyList.add(competencyMap);
				}
			}
		}
		
		if(competencyList == null || competencyList.size() == 0){
			competencyParentMap = new HashMap<String, Object>();
			if(dimensionTypeId == 1){
				competencyParentMap = putDataInJSStateTreeMap(competencyParentMap, 0, "No competency available", IDPAConstants.ENTITY_COMPETENCY,false,0,0);
			}else if(dimensionTypeId == 2){
				competencyParentMap = putDataInJSStateTreeMap(competencyParentMap, 0, "No status available", IDPAConstants.ENTITY_COMPETENCY,false,0,0);
			}
			competencyParentList.add(competencyParentMap);
		}else{
			competencyParentList.addAll(competencyList);
		}
		
		returnvalue = JSONValue.toJSONString(competencyParentList);
		log.debug("returnvalue===" + returnvalue);
		
		return returnvalue;
	}



	@Override
	public String getWorkPackagePlanTree(int userRoleId, int userId,
			int treeType) {
		return null;
	}
	
	
	@Override
	public JSONArray getProductTestingTeamActivityTreeJSON(int userRoleId, int userId,int treeType,String parentType,int parentId,int isActive) {
		
		JSONArray	jsonArray = new JSONArray();
		
	    
		log.debug("In getProductHierarchyTree() method");
		List<HashMap<String, Object>> activityLabelList = null;
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> activityWpMap = null;
		HashMap<String, Object> activityMap = null;
		
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> activityWpList = null;
		List<HashMap<String, Object>> activityList = null;
		HashMap<String, Object> activityLabelMap = null;
		
		String returnvalue = null;
		String nodeType=parentType;   
		parentId=0;
		
				List<Activity> listOfActivities = null;
				List<Activity> listOfActivitiesOfWorkPackage = null;
				activityList = new ArrayList<HashMap<String,Object>>();
				if(nodeType==null || nodeType=="")
				{
					activityLabelMap = new HashMap<String, Object>();
					
					activityLabelList = new ArrayList<HashMap<String, Object>>();
					activityLabelMap = putDataInParentTreeMap(activityLabelMap,"activity~"+parentId,"Activities", IDPAConstants.ENTITY_ACTIVITIES,true,parentId,true,-1); // product version id
					activityLabelList.add(activityLabelMap);
					
					jsonArray.addAll(activityLabelList);
					 return jsonArray;
				}
				else if(nodeType=="Activity"|| parentId==0)
				{
					if(userRoleId==IDPAConstants.ROLE_ID_PQA_REVIEWER){
						listOfActivities=activityDAO.listActiveActivities();
						
					}else{
						listOfActivities = activityDAO.listActivitiesByActivityWorkPackageIdAndLoginId(parentId,1,isActive,userId, true);  //build id
					}
					
				/*}*/
				List<Integer> activityIds = new ArrayList<Integer>();
				for (Activity activity : listOfActivities) {			
					if(activity != null && !activityIds.contains(activity.getActivityId())){
						//groupList = new ArrayList<HashMap<String, Object>>();
						activityIds.add(activity.getActivityId());
						if(activity.getIsActive()==1){					
							activityMap = new HashMap<String, Object>();
							activityMap = setActivityInitialInformation(activity);							
							activityMap = putDataInJSTreeMap(activityMap, activity.getActivityId(), activity.getActivityName(), IDPAConstants.ENTITY_ACTIVITY,false,0);
						}
						if(activityMap!=null){
							activityList.add(activityMap);
						}
					}
				}
				jsonArray.addAll(activityList);
				return jsonArray;
				}
			return jsonArray;
	}

	@Override
	public String getProductTreeForMyWorkflowActions(int userRoleId, int userId) {
		log.debug("In getProductTreeForMyWorkflowActions() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProductsActivity(userRoleId, userId, 0);//getUserAssociatedProducts(userRoleId,userId,1);
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();

					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						 listOfProductsToConsider = productListforTestFactoryId;
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					List<Integer> productIds = new ArrayList<Integer>();
					for (ProductMaster product : listOfProductsToConsider) {
						if(product != null){
							if(productIds.contains(product.getProductId())){
								continue;
							}else{
								productIds.add(product.getProductId());
							}
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
						}
						productList.add(productMap);
					}
				}
				testFactoryMap.put("children", productList);
				testFactoryList.add(testFactoryMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryList);
		}
		log.debug("returnvalue*****===" + returnvalue);	
		return returnvalue;
	}
	
	@Override
	public JSONArray getPivotReportViewAdvanceTree(String nodeType, Integer parentId) {
		log.debug("In getPivotReportViewAdvanceTree() method");
		JSONArray	jsonArray = new JSONArray();
		
		HashMap<String, Object> collectionMap = null;
		List<HashMap<String, Object>> collectionList = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> reportMap = null;
		List<HashMap<String, Object>> reportList = new ArrayList<HashMap<String,Object>>();
		List<PivotRestTemplate> reportCollections = new ArrayList<PivotRestTemplate>();
		if(!nodeType.equalsIgnoreCase("MongoCollection")){
			List<MongoCollection> listOfCollections = reportDAO.getMongoCollectionList();
			if(listOfCollections != null && listOfCollections.size() > 0){
				for (MongoCollection mongoCollection : listOfCollections) {
					collectionMap = new HashMap<String, Object>();
					reportCollections = null;
					reportCollections = reportDAO.getPivotRestTemplateReportByParams(null,null,mongoCollection.getCollectionId());
					collectionMap = putDataInParentTreeMap(collectionMap, "MongoCollection~"+mongoCollection.getCollectionId(), mongoCollection.getDisplayName()+"("+reportCollections.size()+")", IDPAConstants.ENTITY_MONGO_COLLECTION,true,null,true,-1);
					collectionList.add(collectionMap);
				}
			}
			jsonArray.addAll(collectionList);
			return jsonArray;
		}else if(nodeType.equalsIgnoreCase("MongoCollection")){
			reportList = new ArrayList<HashMap<String,Object>>();
			List<PivotRestTemplate> reportCollection = reportDAO.getPivotRestTemplateReportByParams(null,null,parentId);
			if(reportCollection != null && reportCollection.size() > 0){
				for (PivotRestTemplate reportTemplate : reportCollection) {
					reportMap = new HashMap<String, Object>();
					reportMap = putDataInParentTreeMap(reportMap, "ReportNames~"+reportTemplate.getTemplateId(), reportTemplate.getTemplateName(), IDPAConstants.ENTITY_PIVOT_REPORT_COLLECTION,false,parentId,false,-1);
					reportList.add(reportMap);
				}
			}
			jsonArray.addAll(reportList);
			return jsonArray;
		}
		log.debug("returnvalue*****===" + jsonArray);	
		return jsonArray;
	}
	
	private HashMap<String, Object> setActivityWpInitialInformation(ActivityWorkPackage awp){
		HashMap<String, Object> activityMap = new HashMap<String, Object>();		
		activityMap.put("testFactId", awp.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
		activityMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(awp.getPlannedStartDate()));
		activityMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(awp.getPlannedEndDate()));
		if(awp.getWorkflowStatus() != null && awp.getWorkflowStatus().getWorkflowStatusId() > 0 && awp.getWorkflowStatus().getIsLifeCycleStage() == 1){
			activityMap.put("lifeCycleStage", awp.getWorkflowStatus().getWorkflowStatusId());
		}
		if(awp.getProductBuild().getProductVersion().getProductMaster().getProductId() != null){
			activityMap.put("productId", awp.getProductBuild().getProductVersion().getProductMaster().getProductId());
		}
		return activityMap;
	}
	
	private HashMap<String, Object> setActivityInitialInformation(Activity act) {
		HashMap<String, Object> activityTaskMap =new HashMap<String, Object>();
		if(act.getAssignee()!=null && act.getAssignee().getUserId()!=null){
			activityTaskMap.put("activityAssigneeId", act.getAssignee().getUserId());
		}
		if(act.getReviewer()!=null && act.getReviewer().getUserId()!=null){
			activityTaskMap.put("activityReviewerId", act.getReviewer().getUserId());
		}
		if(act.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId() != null){
			activityTaskMap.put("productId", act.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
		}
		if(act.getActivityWorkPackage() != null){
			activityTaskMap.put("activityWorkpackageId", act.getActivityWorkPackage().getActivityWorkPackageId());
		}
		activityTaskMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(act.getPlannedStartDate()));
		activityTaskMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(act.getPlannedEndDate()));
		if(act.getLifeCycleStage() != null && act.getLifeCycleStage().getWorkflowStatusId() > 0){
			activityTaskMap.put("lifeCycleStage", act.getLifeCycleStage().getWorkflowStatusId());
		}
		if(act.getActivityMaster() != null){
			activityTaskMap.put("activityTypeId", act.getActivityMaster().getActivityMasterId());
		}
		return activityTaskMap;
	}
	
	
	@Override
	public String getProductActivityWorkPackageTreeByProductId(Integer productId) {
		log.debug("In getProductActivityWorkPackageTreeByProductId() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		HashMap<String, Object> activityWpMap = null;
		
		
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		List<HashMap<String, Object>> activityWpList = null;
		
		String returnvalue = null;
					ProductMaster product=productMasterDAO.getProductDetailsById(productId);
						List<ProductVersionListMaster> listOfProductVariants = null;
						productVersionList = new ArrayList<HashMap<String,Object>>();
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									List<ProductBuild> listOfProductBuilds = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
										List<ActivityWorkPackage> listOfActivityWorkPackages = null;
										for (ProductBuild productBuild : listOfProductBuilds) {
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
												activityWpList = new ArrayList<HashMap<String,Object>>();
												listOfActivityWorkPackages = activityWorkPackageDAO.listActivityWorkPackagesByBuildId(productBuild.getProductBuildId(),1);
												for (ActivityWorkPackage activityWorkPackage : listOfActivityWorkPackages) {
													if(activityWorkPackage != null){
														activityWpMap = new HashMap<String, Object>();
														activityWpMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedStartDate()));
														activityWpMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(activityWorkPackage.getPlannedEndDate()));
														if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0 && activityWorkPackage.getWorkflowStatus().getIsLifeCycleStage() == 1){
															activityWpMap.put("lifeCycleStage", activityWorkPackage.getWorkflowStatus().getWorkflowStatusId());
														}
														activityWpMap = putDataInJSTreeMap(activityWpMap, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, false, productBuild.getProductBuildId());
													}
													 activityWpList.add(activityWpMap);  
												}
											}
											productBuildMap.put("children",activityWpList);
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
							}
						productMap.put("children", productVersionList);
						productList.add(productMap);
					returnvalue = JSONValue.toJSONString(productList);
				
				log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	}



	@Override
	public String getProductHierarchyTreeByProductId(Integer productId) {

		log.debug("In getProductHierarchyTreeByProduct() method");
		
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		String returnvalue = null;
						List<ProductVersionListMaster> listOfProductVariants = null;
						productVersionList = new ArrayList<HashMap<String,Object>>();
						ProductMaster product=productMasterDAO.getProductDetailsById(productId);
						if(product != null){
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,false,product.getProductId());
									List<ProductBuild> listOfProductBuilds = null;
										productBuildList = new ArrayList<HashMap<String,Object>>();
										listOfProductBuilds = productBuildDAO.list(productVersion.getProductVersionListId());
										for (ProductBuild productBuild : listOfProductBuilds) {
											if(productBuild != null){
												productBuildMap = new HashMap<String, Object>();
												productBuildMap.put("plannedStartDate", DateUtility.dateformatWithSlashWithOutTime(productBuild.getBuildDate()));
												productBuildMap.put("plannedEndDate", DateUtility.dateformatWithSlashWithOutTime(productBuild.getBuildDate()));
												productBuildMap = putDataInJSTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false, productVersion.getProductVersionListId());
											}
											productBuildList.add(productBuildMap);
										}
									}
									productVersionMap.put("children", productBuildList);
									productVersionList.add(productVersionMap);
								}
							}
						productMap.put("children", productVersionList);
						productList.add(productMap);
					returnvalue = JSONValue.toJSONString(productList);
				log.debug("returnvalue===" + returnvalue);
		return returnvalue;
	
	}
	
	
	@Override
	public String getProductBuildNodeTree(int userRoleId, int userId) {
		log.debug("In getProductBuildNodeTree() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		List<Integer> engagementManagerTestFactoryIds = new ArrayList<Integer>();
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			if(listOfTestFactories != null && listOfTestFactories.size() > 0){
				for(TestFactory testFactory : listOfTestFactories){
					engagementManagerTestFactoryIds.add(testFactory.getTestFactoryId());
				}
			}
			if(listOfTestFactories == null){
				listOfTestFactories = new ArrayList<TestFactory>();
			}
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size() > 0){
				for(ProductMaster productMaster : listOfProducts){
					if(!engagementManagerTestFactoryIds.contains(productMaster.getTestFactory().getTestFactoryId())){
						listOfTestFactories.add(productMaster.getTestFactory());
						engagementManagerTestFactoryIds.add(productMaster.getTestFactory().getTestFactoryId());
					}
				}
			}
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();
					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						if(engagementManagerTestFactoryIds.contains(testFactory.getTestFactoryId())){
							listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					for (ProductMaster product : listOfProductsToConsider) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						if(product != null){
							productVersionList = new ArrayList<HashMap<String,Object>>();
							productMap = new HashMap<String, Object>();
							productMap = putDataInJSStateTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT, false,testFactory.getTestFactoryId(),product.getProductMode().getModeId());
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								List<ProductBuild> dbProductBuildList=null;
								productBuildList =  new ArrayList<HashMap<String,Object>>();
								if(productVersion != null){
									productVersionMap = new HashMap<String, Object>();
									productVersionMap = putDataInJSTreeMap(productVersionMap, productVersion.getProductVersionListId(), productVersion.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION, false,product.getProductId());
									
									dbProductBuildList=productBuildDAO.list(productVersion.getProductVersionListId());
									if(dbProductBuildList != null && dbProductBuildList.size() >0) {
										for(ProductBuild build:dbProductBuildList) {
											productBuildMap = new HashMap<String, Object>();
											productBuildMap = putDataInJSTreeMap(productBuildMap, build.getProductBuildId(), build.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD, false,productVersion.getProductVersionListId());
											productBuildList.add(productBuildMap);
										}
									}								
								}
								productVersionMap.put("children", productBuildList);
								productVersionList.add(productVersionMap);
							}
						}
						productMap.put("children", productVersionList);

						productList.add(productMap);
					}
					
				}
				testFactoryMap.put("children", productList);
				testFactoryList.add(testFactoryMap);
			}
			returnvalue = JSONValue.toJSONString(testFactoryList);
		}
		log.debug("returnvalue*****===" + returnvalue);	
		return returnvalue;
	}
	
	
	@Override
	@Transactional
	public List<JsonProductBuild> getProductBuildDetails(int userRoleId, int userId,String filter) {
		log.debug("In getProductBuildDetails() method");
		HashMap<String, Object> productMap = null;
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		String returnvalue = null;
		HashMap<String, Object> productVersionMap = null;
		HashMap<String, Object> productBuildMap = null;
		List<HashMap<String, Object>> productVersionList = null;
		List<HashMap<String, Object>> productBuildList = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		List<Integer> engagementManagerTestFactoryIds = new ArrayList<Integer>();
		
		List<JsonProductBuild> jsonProductBuilds= new ArrayList<JsonProductBuild>();
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Associated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			if(listOfTestFactories != null && listOfTestFactories.size() > 0){
				for(TestFactory testFactory : listOfTestFactories){
					engagementManagerTestFactoryIds.add(testFactory.getTestFactoryId());
				}
			}
			if(listOfTestFactories == null){
				listOfTestFactories = new ArrayList<TestFactory>();
			}
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size() > 0){
				for(ProductMaster productMaster : listOfProducts){
					if(!engagementManagerTestFactoryIds.contains(productMaster.getTestFactory().getTestFactoryId())){
						listOfTestFactories.add(productMaster.getTestFactory());
						engagementManagerTestFactoryIds.add(productMaster.getTestFactory().getTestFactoryId());
					}
				}
			}
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();
					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						if(engagementManagerTestFactoryIds.contains(testFactory.getTestFactoryId())){
							listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					for (ProductMaster product : listOfProductsToConsider) {
						List<ProductVersionListMaster> listOfProductVariants = null;
						if(product != null){
							productVersionList = new ArrayList<HashMap<String,Object>>();
							listOfProductVariants = productVersionListMasterDAO.list(product.getProductId());
							for (ProductVersionListMaster productVersion : listOfProductVariants) {
								List<ProductBuild> dbProductBuildList=null;
								if(productVersion != null){
									dbProductBuildList=productBuildDAO.getProductBuildDetailsByVersionIdAndDayORWeek(productVersion.getProductVersionListId(),filter);
									if(dbProductBuildList != null && dbProductBuildList.size() >0) {
										productBuildList =  new ArrayList<HashMap<String,Object>>();
										for(ProductBuild build:dbProductBuildList) {
											productBuildList.add(productBuildMap);
											
											jsonProductBuilds.add(new JsonProductBuild(build));
										}
									}
								
								}
							}
						}
					}
					
				}
			}
		}
		return jsonProductBuilds;
	}
	
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlanForTester> getUserRolebasedCompletedAndAbortedWorkpackges(int userRoleId, int userId) {
		log.debug("In getUserRolebasedCompletedAndAbortedWorkpackges() method");
		HashMap<String, Object> testFactoryMap = null;
		List<HashMap<String, Object>> productList = null;
		List<HashMap<String, Object>> testFactoryList = null;
		
		List<ProductMaster> listOfProducts = null;
		List<TestFactory> listOfTestFactories = null;
		List<Integer> engagementManagerTestFactoryIds = new ArrayList<Integer>();
		
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlanForTesterList= new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size()>0){
				listOfTestFactories = new ArrayList<TestFactory>();
				for (ProductMaster productMaster : listOfProducts) {
					List<TestFactory> subListOfTestFactory = null;
					subListOfTestFactory = testFactoryDao.getTestFactoriesByProductId(productMaster.getProductId());
					if(listOfTestFactories.size() == 0){
						listOfTestFactories = subListOfTestFactory;
					}else{
						if(subListOfTestFactory != null && subListOfTestFactory.size()>0){
							for (TestFactory testFactory : subListOfTestFactory) {
								if(!listOfTestFactories.contains(testFactory)){
									listOfTestFactories.add(testFactory);
								}
							}
						}
					}
				}
			}
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
			listOfTestFactories = testFactoryDao.getTestFactoriesByTestFactoryManagerId(userId);
			if(listOfTestFactories != null && listOfTestFactories.size() > 0){
				for(TestFactory testFactory : listOfTestFactories){
					engagementManagerTestFactoryIds.add(testFactory.getTestFactoryId());
				}
			}
			if(listOfTestFactories == null){
				listOfTestFactories = new ArrayList<TestFactory>();
			}
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);//Fetching all Products Assocated to User(User Id), not by Role(Role Id).
			if(listOfProducts != null && listOfProducts.size() > 0){
				for(ProductMaster productMaster : listOfProducts){
					if(!engagementManagerTestFactoryIds.contains(productMaster.getTestFactory().getTestFactoryId())){
						listOfTestFactories.add(productMaster.getTestFactory());
						engagementManagerTestFactoryIds.add(productMaster.getTestFactory().getTestFactoryId());
					}
				}
			}
		}else{
			listOfTestFactories = testFactoryDao.getTestFactoryList();
			listOfProducts = productMasterDAO.list(false);
		}
		testFactoryList = new ArrayList<HashMap<String,Object>>();
		if(listOfTestFactories != null && listOfTestFactories.size()>0){
			for (TestFactory testFactory : listOfTestFactories) {
				if(testFactory != null){
					List<ProductMaster> productListforTestFactoryId = null;
					List<ProductMaster> listOfProductsToConsider = null;
					testFactoryMap = new HashMap<String, Object>();
					testFactoryMap = putDataInJSStateTreeMap(testFactoryMap, testFactory.getTestFactoryId(), testFactory.getTestFactoryName(), IDPAConstants.ENTITY_TEST_FACTORY, true,0,testFactory.getEngagementTypeMaster().getEngagementTypeId());
					productList = new ArrayList<HashMap<String,Object>>();
					productListforTestFactoryId = productMasterDAO.getProductsByTestFactoryId(testFactory.getTestFactoryId());
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
						if(engagementManagerTestFactoryIds.contains(testFactory.getTestFactoryId())){
							listOfProductsToConsider = productListforTestFactoryId;
						}else{
							listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
						}
					}else{
						listOfProductsToConsider = getProducts(listOfProducts, productListforTestFactoryId);
					}
					for (ProductMaster product : listOfProductsToConsider) {
						

						List<WorkPackageTCEPSummaryDTO> wtpdto = workPackageDAO
								.getWPTCExecutionSummaryByProdIdBuildId(testFactory.getTestFactoryId(),product.getProductId(),-1, 0, 100000,null);
						if (wtpdto != null && wtpdto.size() > 0) {
							for (WorkPackageTCEPSummaryDTO workPackageTCEPSummaryDTO : wtpdto) {
								if(workPackageTCEPSummaryDTO.getWorkFlowStageName() != null && (workPackageTCEPSummaryDTO.getWorkFlowStageName().equalsIgnoreCase("Completed") || workPackageTCEPSummaryDTO.getWorkFlowStageName().equalsIgnoreCase("Aborted"))){
								jsonWorkPackageTestCaseExecutionPlanForTesterList
										.add(new JsonWorkPackageTestCaseExecutionPlanForTester(
												workPackageTCEPSummaryDTO));
								}
							}
						}
					
						
					}
					
				}
			}
		}
		return jsonWorkPackageTestCaseExecutionPlanForTesterList;
	}
	
}
