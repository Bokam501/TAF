package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.DecouplingCategoryDAO;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.SkillDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestFactoryLabDao;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.service.DataMapTreeService;

@Service
public class DataMapTreeServiceImpl implements DataMapTreeService{

	private static final Log log = LogFactory.getLog(DataMapTreeServiceImpl.class);
	
	@Autowired
	private ProductFeatureDAO productFeatureDAO;

	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Autowired
	private DecouplingCategoryDAO decouplingCategoryDAO;
	
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	
	@Autowired
	private EnvironmentDAO environmentDAO;
	
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
	private TestFactoryDao testFactoryDao;
	
	@Autowired
	private SkillDAO skillDao;
	
	@Value("#{ilcmProps['ROOT_PRODUCT_FEATURE']}")
    private String rootProductFeatureDescription;
	
	@Value("#{ilcmProps['ROOT_PRODUCT_DECOUPLING_CATEGORY']}")
    private String rootDecouplingCategoryDescription;
	
	@Value("#{ilcmProps['ROOT_USER_SKILLS']}")
	private String rootUserSkillsDescription;
	
	@Override
	public JSONArray getFeatureTestCaseMappingTree(int productId) {
		
		log.info("In getFeatureTestCaseMappingTree() method");
		HashMap<String, Object> tcLabelMap = null;
		HashMap<String, Object> testcaseScriptLabelMap = null;
		List<HashMap<String, Object>> tcList = null;
		List<HashMap<String, Object>> tscriptList = null;
		HashMap<String, Object> parentFeatureMap = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> tsGroupList = null;
		List<HashMap<String, Object>> featureGroupList = null;
		JSONArray jsonArray = null;
		List<ProductFeature> listOfFeatures = null;
		List<TestCaseList> listOfFeatureTestCases = null;
		List<TestCaseScript> listOfTestcaseScripts= null;
		HashMap<String, Object> tcMap = null;
		HashMap<String, Object> tcScriptMap = null;
		Integer rootFeatureId = 0;
		rootFeatureId = productFeatureDAO.geRootProductFeatureId(rootProductFeatureDescription);
		log.debug("rootFeatureId : "+rootFeatureId); 
		Integer jtStartIndex = -1;
		Integer jtPageSize = -1; 
		listOfFeatures = productFeatureDAO.getRootFeatureListByProductId(productId, jtStartIndex, jtPageSize,rootFeatureId,true);
		boolean isDataCountMatchPageSize = false;
		if(listOfFeatures != null && listOfFeatures.size()>=jtPageSize){
			isDataCountMatchPageSize = true;
		}else{
			listOfFeatures = null;
			listOfFeatures = productFeatureDAO.getFeatureListByProductId(productId, null, jtStartIndex, jtPageSize);
			isDataCountMatchPageSize = true;
		}
		if(isDataCountMatchPageSize){
			featureGroupList = new ArrayList<HashMap<String, Object>>();
			for (ProductFeature productFeature : listOfFeatures) {
				if(productFeature != null){
					if(productFeature.getParentFeature() != null && productFeature.getParentFeature().getProductFeatureId().equals(rootFeatureId)){
						groupList = new ArrayList<HashMap<String, Object>>();
						tcLabelMap = new HashMap<String, Object>();
						tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
						if(isContainingChildFeatures(productFeature)){
							parentFeatureMap = new HashMap<String, Object>();
							parentFeatureMap = putDataInParentTreeMapCheck(parentFeatureMap, "F"+productFeature.getProductFeatureId(),productFeature.getProductFeatureName(), IDPAConstants.ENTITY_PRODUCT_FEATURE,true,0,true);
							featureGroupList.add(parentFeatureMap);
						}else{
							parentFeatureMap = new HashMap<String, Object>();
							parentFeatureMap = putDataInParentTreeMapCheck(parentFeatureMap, "F"+productFeature.getProductFeatureId(), productFeature.getProductFeatureName() , IDPAConstants.ENTITY_PRODUCT_FEATURE,true,0,false);
							featureGroupList.add(parentFeatureMap);
							tcLabelMap = new HashMap<String, Object>();
							tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
							listOfFeatureTestCases = testCaseListDAO.getTestCasesMappedToFeature(productFeature.getProductFeatureId());
							groupList = new ArrayList<HashMap<String,Object>>();
							tcList = new ArrayList<HashMap<String, Object>>();
							if(listOfFeatureTestCases != null && listOfFeatureTestCases.size()>0){
								
								for (TestCaseList testCase : listOfFeatureTestCases) {
									
									tcMap = new HashMap<String, Object>();
									tcMap = putDataInParentTreeMapCheck(tcMap, "C"+testCase.getTestCaseId(), testCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,true,0,true);
									
									tcList.add(tcMap);
									testcaseScriptLabelMap = new HashMap<String, Object>();
									testcaseScriptLabelMap = putDataInLabelMap(testcaseScriptLabelMap, "Test Scripts",IDPAConstants.ENTITY_TEST_CASE_SCRIPT,true);
									listOfTestcaseScripts=testCaseListDAO.getMappedTestScriptsByProductIdAndTestcaseId(testCase.getProductMaster().getProductId(), testCase.getTestCaseId());
									tsGroupList = new ArrayList<HashMap<String,Object>>();
									tscriptList = new ArrayList<HashMap<String, Object>>();
									if(listOfTestcaseScripts != null && listOfTestcaseScripts.size()>0) {
										for(TestCaseScript script:listOfTestcaseScripts) {
											tcScriptMap = new HashMap<String, Object>();
											tcScriptMap = putDataInParentTreeMapCheck(tcScriptMap, "S"+script.getScriptId(),script.getScriptName(), IDPAConstants.ENTITY_TEST_CASE_SCRIPT,true,0,false);
											tscriptList.add(tcScriptMap);
										}
										testcaseScriptLabelMap.put("children", tscriptList);
										tsGroupList.add(testcaseScriptLabelMap);
										tcLabelMap.put("children", tsGroupList);
									}
									
									
								}
								tcLabelMap.put("children", tcList);
								groupList.add(tcLabelMap);
								parentFeatureMap.put("children",groupList);
							}
						}
					}else{
						continue;
					}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(featureGroupList);
		}
		return jsonArray;
	}
	
	@Override
	public JSONArray getChildFeaturesOfParentFeature(int parentNodeId, String node) {
		log.info("In getChildFeaturesOfParentFeature() method");
		HashMap<String, Object> tcLabelMap = null;
		HashMap<String, Object> tcChildrenLabelMap = null;
		HashMap<String, Object> tcScriptMap=null;
		HashMap<String, Object> testcaseScriptLabelMap=null;
		List<HashMap<String, Object>> tcList = null;
		List<HashMap<String, Object>> tcChildList = null;
		List<HashMap<String, Object>> tscriptList=null;
		HashMap<String, Object> tcMap = null;
		HashMap<String, Object> tcChildMap = null;
		HashMap<String, Object> childFeatureMap = null;
		List<HashMap<String, Object>> childFeatureList = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> featureGroupList = null;
		JSONArray jsonArray = null;
		JSONArray childJsonArray = null;
		List<ProductFeature> listOfChildFeatures = null;
		List<TestCaseList> listOfFeatureTestCases = null;
		List<TestCaseScript> listOfTestcaseScripts = null;
		listOfChildFeatures = productFeatureDAO.getChildFeatureListByParentfeatureId(parentNodeId);
		featureGroupList = new ArrayList<HashMap<String, Object>>();
		listOfFeatureTestCases = testCaseListDAO.getTestCasesMappedToFeature(parentNodeId);
		if(listOfFeatureTestCases != null && listOfFeatureTestCases.size()>0 && node.equalsIgnoreCase("Feature")){
			tcLabelMap = new HashMap<String, Object>();
			tcLabelMap = putDataInLabelMap(tcLabelMap, "Test case",IDPAConstants.ENTITY_TEST_CASE,true);
			tcList = new ArrayList<HashMap<String, Object>>();
			
			for (TestCaseList tc : listOfFeatureTestCases) {
				
				tcMap = new HashMap<String, Object>();
				tcMap = putDataInParentTreeMap(tcMap, tc.getTestCaseId(), tc.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,true,0,true);
				tcList.add(tcMap);
			}
			tcLabelMap.put("children", tcList);
			featureGroupList.add(tcLabelMap);
		}
		if(listOfChildFeatures != null && node.equalsIgnoreCase("Feature")){
			for (ProductFeature childFeature : listOfChildFeatures) {
				childFeatureList = new ArrayList<HashMap<String,Object>>();
				if(childFeature != null){
						if(isContainingChildFeatures(childFeature)){
							childFeatureMap = new HashMap<String, Object>();
							childFeatureMap = putDataInParentTreeMap(childFeatureMap, childFeature.getProductFeatureId(),childFeature.getProductFeatureName() , IDPAConstants.ENTITY_PRODUCT_FEATURE,true,0,true);
							featureGroupList.add(childFeatureMap);
						}else{
							List<TestCaseList> listOfChildFeatureTestCases = new ArrayList<TestCaseList>();
							listOfChildFeatureTestCases = testCaseListDAO.getTestCasesMappedToFeature(childFeature.getProductFeatureId());
							if(listOfChildFeatureTestCases != null && listOfChildFeatureTestCases.size()>0){
								childFeatureMap = new HashMap<String, Object>();
								childFeatureMap = putDataInParentTreeMap(childFeatureMap, childFeature.getProductFeatureId(),childFeature.getProductFeatureName(), IDPAConstants.ENTITY_PRODUCT_FEATURE,true,0,true);
								featureGroupList.add(childFeatureMap);
								tcChildrenLabelMap = new HashMap<String, Object>();
								tcChildrenLabelMap = putDataInLabelMap(tcChildrenLabelMap, "Test case",IDPAConstants.ENTITY_TEST_CASE,true);
								groupList = new ArrayList<HashMap<String,Object>>();
								tcChildList = new ArrayList<HashMap<String, Object>>();
								for (TestCaseList testCase : listOfChildFeatureTestCases) {
									testcaseScriptLabelMap = new HashMap<String, Object>();
									testcaseScriptLabelMap = putDataInLabelMap(testcaseScriptLabelMap, "Test case",IDPAConstants.ENTITY_TEST_CASE,true);
									tcChildMap = new HashMap<String, Object>();
									tcChildMap = putDataInParentTreeMap(tcChildMap, testCase.getTestCaseId(), testCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,true,0,true);
									tcChildList.add(tcChildMap);
								}
								tcChildrenLabelMap.put("children", tcChildList);
								groupList.add(tcChildrenLabelMap);
								childFeatureMap.put("children",groupList);
							}else{
								childFeatureMap = new HashMap<String, Object>();
								childFeatureMap = putDataInParentTreeMap(childFeatureMap, childFeature.getProductFeatureId(),childFeature.getProductFeatureName(), IDPAConstants.ENTITY_PRODUCT_FEATURE,true,0,false);
								featureGroupList.add(childFeatureMap);
							}
						}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(featureGroupList);
		} else if(node.equalsIgnoreCase("TestCase")){
			childJsonArray = new JSONArray();
			childJsonArray=getChildTestCaseOfParentFeature(parentNodeId,  node); 
			return childJsonArray;
		}
		return jsonArray;
	}
	
	
	private JSONArray getChildTestCaseOfParentFeature(int parentNodeId, String node) {
		log.info("In getChildTestCaseOfParentFeature() method");
		HashMap<String, Object> tcLabelMap = null;
		HashMap<String, Object> tcChildrenLabelMap = null;
		HashMap<String, Object> tcScriptMap=null;
		HashMap<String, Object> testcaseScriptLabelMap=null;
		List<HashMap<String, Object>> tcList = null;
		List<HashMap<String, Object>> tcChildList = null;
		List<HashMap<String, Object>> tscriptList=null;
		HashMap<String, Object> tcMap = null;
		HashMap<String, Object> tcChildMap = null;
		HashMap<String, Object> childFeatureMap = null;
		List<HashMap<String, Object>> childFeatureList = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> featureGroupList = null;
		JSONArray jsonArray = null;
		List<ProductFeature> listOfChildFeatures = null;
		List<TestCaseList> listOfFeatureTestCases = null;
		List<TestCaseScript> listOfTestcaseScripts = null;
		if(node.equalsIgnoreCase("Feature")) {
			listOfChildFeatures = productFeatureDAO.getChildFeatureListByParentfeatureId(parentNodeId);
		}
		featureGroupList = new ArrayList<HashMap<String, Object>>();
		if(node.equalsIgnoreCase("TestCase")) {
			listOfFeatureTestCases = new ArrayList<TestCaseList>();
			listOfFeatureTestCases.add(testCaseListDAO.getByTestCaseId(parentNodeId));
			listOfChildFeatures = productFeatureDAO.getProductFeatureHasTestCaseId(parentNodeId);
		}else if(node.equalsIgnoreCase("TestCaseScript")){
			
		} else {
			listOfFeatureTestCases = testCaseListDAO.getTestCasesMappedToFeature(parentNodeId);
		}
		if(listOfFeatureTestCases != null && listOfFeatureTestCases.size()>0){
			tcLabelMap = new HashMap<String, Object>();
			
			tcList = new ArrayList<HashMap<String, Object>>();
			
			for (TestCaseList tc : listOfFeatureTestCases) {
				
				testcaseScriptLabelMap = new HashMap<String, Object>();
				testcaseScriptLabelMap = putDataInLabelMap(testcaseScriptLabelMap, "Test Scripts",IDPAConstants.ENTITY_TEST_CASE_SCRIPT,false);
				listOfTestcaseScripts=testCaseListDAO.getMappedTestScriptsByProductIdAndTestcaseId(tc.getProductMaster().getProductId(), tc.getTestCaseId());
				tscriptList = new ArrayList<HashMap<String, Object>>();
				if(listOfTestcaseScripts != null && listOfTestcaseScripts.size()>0) {
					for(TestCaseScript script:listOfTestcaseScripts) {
						
						tcScriptMap = new HashMap<String, Object>();
						tcScriptMap = putDataInParentTreeMap(tcScriptMap, script.getScriptId(),script.getScriptName(), IDPAConstants.ENTITY_TEST_CASE_SCRIPT,true,0,false);
						tscriptList.add(tcScriptMap);
					}
					testcaseScriptLabelMap.put("children", tscriptList);
					tcList.add(testcaseScriptLabelMap);
					
				}
			}
			featureGroupList.add(testcaseScriptLabelMap);
			jsonArray = new JSONArray();
			jsonArray.addAll(featureGroupList);
			
		}else {
			jsonArray = new JSONArray();
			jsonArray.addAll(featureGroupList);
		}
		return jsonArray;
	}
	
	private boolean isContainingChildFeatures(ProductFeature productFeature){
		boolean hasChildCategories = false;
		Set<ProductFeature> childCategories = productFeature.getChildFeatures();
		if(childCategories != null && childCategories.size()>0){
			hasChildCategories = true;
		}
		return hasChildCategories;
		
	}
	
	private boolean isContainingChildCategories(DecouplingCategory dc){
		boolean hasChildCategories = false;
		Set<DecouplingCategory> childCategories = dc.getChildCategories();
		if(childCategories != null && childCategories.size()>0){
			hasChildCategories = true;
		}
		return hasChildCategories;
		
	}
	
	public HashMap<String, Object> putDataInParentTreeMap(HashMap<String, Object> mapObject, long keyId, String entityName, String entityType, boolean isParent, Integer parentId, boolean hasChildern){
		if(mapObject != null){
			mapObject.put("id", String.valueOf(keyId));
			mapObject.put("text", entityName);
			mapObject.put("children", hasChildern);
			mapObject.put("data", entityType);
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_FEATURE)){
				mapObject.put("icon","fa fa-crosshairs");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_CASE)){
				mapObject.put("icon","fa fa-crosshairs");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_CASE_SCRIPT)){
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
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_USER_SKILLS)){
				mapObject.put("icon","fa fa-chain");
			}
		}
		return mapObject;
	}
	
	public HashMap<String, Object> putDataInLabelMap(HashMap<String, Object> mapObject, String entityTitle,String entityType,boolean isParent){
		if(mapObject != null){
			Random rand = new Random();
			int randomNum = rand.nextInt(10000) + 1;
			mapObject.put("id", String.valueOf(randomNum));
			mapObject.put("text", entityTitle);
			mapObject.put("children", true);
			mapObject.put("data", entityType+"Type");
		}
		return mapObject;
	}

	@Override
	public JSONArray getDecouplingTestCaseMappingTree(int productId) {
		log.info("In getDecouplingTestCaseMappingTree() method productId: "+productId);
		HashMap<String, Object> tcLabelMap = null;
		List<HashMap<String, Object>> tcList = null;
		HashMap<String, Object> parentDCMap = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> dcGroupList = null;
		JSONArray jsonArray = null;
		List<DecouplingCategory> listOfDecouplingCategories = null;
		List<TestCaseList> listOfDCTestCases = null;
		HashMap<String, Object> tcMap = null;
		Integer rootDecouplingCategoryId = 0;
		rootDecouplingCategoryId = decouplingCategoryDAO.geRootDecouplingCategoryId(rootDecouplingCategoryDescription);
		log.info("rootDecouplingCategoryId : "+rootDecouplingCategoryId);
		listOfDecouplingCategories = decouplingCategoryDAO.getDecouplingCategoryListByProductId(productId);
		if(listOfDecouplingCategories != null){
			dcGroupList = new ArrayList<HashMap<String, Object>>();
			for (DecouplingCategory dc : listOfDecouplingCategories) {
				if(dc != null){
					if(dc.getParentCategory() != null && dc.getParentCategory().getDecouplingCategoryId().equals(rootDecouplingCategoryId)){
						groupList = new ArrayList<HashMap<String, Object>>();
						tcLabelMap = new HashMap<String, Object>();
						tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
						if(isContainingChildCategories(dc)){
							parentDCMap = new HashMap<String, Object>();
							parentDCMap = putDataInParentTreeMap(parentDCMap, dc.getDecouplingCategoryId(), dc.getLeftIndex()+" : "+dc.getDecouplingCategoryName()+" : "+dc.getRightIndex(), IDPAConstants.ENTITY_DECOUPLING_CATEGORY,true,0,true);
							dcGroupList.add(parentDCMap);
						}else{
							parentDCMap = new HashMap<String, Object>();
							parentDCMap = putDataInParentTreeMap(parentDCMap, dc.getDecouplingCategoryId(), dc.getLeftIndex()+" : "+dc.getDecouplingCategoryName()+" : "+dc.getRightIndex(), IDPAConstants.ENTITY_DECOUPLING_CATEGORY,true,0,false);
							dcGroupList.add(parentDCMap);
							tcLabelMap = new HashMap<String, Object>();
							tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
							Set<TestCaseList> setOfTestCase = dc.getTestCaseList();
							if(setOfTestCase != null && setOfTestCase.size()>0){
								listOfDCTestCases = new ArrayList<TestCaseList>();
								listOfDCTestCases.addAll(setOfTestCase);
								groupList = new ArrayList<HashMap<String,Object>>();
								tcList = new ArrayList<HashMap<String, Object>>();
								if(listOfDCTestCases != null && listOfDCTestCases.size()>0){
									for (TestCaseList dcTtestCase : listOfDCTestCases) {
										tcMap = new HashMap<String, Object>();
										tcMap = putDataInParentTreeMap(tcMap, dcTtestCase.getTestCaseId(), dcTtestCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
										tcList.add(tcMap);
									}
									tcLabelMap.put("children", tcList);
									groupList.add(tcLabelMap);
									parentDCMap.put("children",groupList);
								}
								listOfDCTestCases = null;
							}
						}
					}else{
						continue;
					}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(dcGroupList);
		}
		return jsonArray;
	}

	@Override
	@Transactional
	public JSONArray getChildDecouplingCategoriesOfParentCatregory(int dcId, String node) {
		log.info("In getChildDecouplingCategoriesOfParentCatregory() method");
		HashMap<String, Object> tcLabelMap = null;
		HashMap<String, Object> tcChildrenLabelMap = null;
		List<HashMap<String, Object>> tcList = null;
		List<HashMap<String, Object>> tcChildList = null;
		HashMap<String, Object> tcMap = null;
		HashMap<String, Object> tcChildMap = null;
		HashMap<String, Object> childDCMap = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> dcGroupList = null;
		JSONArray jsonArray = null;
		List<DecouplingCategory> listOfChildDecoplingCategories = null;
		List<TestCaseList> listOfDCTestCases = null;
		listOfChildDecoplingCategories = decouplingCategoryDAO.getChildCategoriesListByParentCategoryId(dcId);
		dcGroupList = new ArrayList<HashMap<String, Object>>();
		DecouplingCategory dcObj = decouplingCategoryDAO.getDecouplingCategoryById(dcId);
		if(dcObj != null){
			 Set<TestCaseList> setOfTestCases = dcObj.getTestCaseList();
			 if(setOfTestCases != null){
				 listOfDCTestCases = new ArrayList<TestCaseList>();
				 listOfDCTestCases.addAll(setOfTestCases);
			 }
		}
		if(listOfDCTestCases != null && listOfDCTestCases.size()>0){
			tcLabelMap = new HashMap<String, Object>();
			tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
			tcList = new ArrayList<HashMap<String, Object>>();
			for (TestCaseList testCase : listOfDCTestCases) {
				tcMap = new HashMap<String, Object>();
				tcMap = putDataInParentTreeMap(tcMap, testCase.getTestCaseId(), testCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
				tcList.add(tcMap);
			}
			tcLabelMap.put("children", tcList);
			dcGroupList.add(tcLabelMap);
		}
		if(listOfChildDecoplingCategories != null){
			for (DecouplingCategory childDecopleCategory : listOfChildDecoplingCategories) {
				if(childDecopleCategory != null){
						if(isContainingChildCategories(childDecopleCategory)){
							childDCMap = new HashMap<String, Object>();
							childDCMap = putDataInParentTreeMap(childDCMap, childDecopleCategory.getDecouplingCategoryId(), childDecopleCategory.getLeftIndex()+" : "+childDecopleCategory.getDecouplingCategoryName()+" : "+childDecopleCategory.getRightIndex(), IDPAConstants.ENTITY_DECOUPLING_CATEGORY,true,0,true);
							dcGroupList.add(childDCMap);
						}else{
							List<TestCaseList> listOfChildDCTestCases = new ArrayList<TestCaseList>();	
							Set<TestCaseList> setOfTestCase = childDecopleCategory.getTestCaseList();
							if(setOfTestCase != null && setOfTestCase.size()>0){
								listOfChildDCTestCases = new ArrayList<TestCaseList>();
								listOfChildDCTestCases.addAll(setOfTestCase);
							}
							if(listOfChildDCTestCases != null && listOfChildDCTestCases.size()>0){
								childDCMap = new HashMap<String, Object>();
								childDCMap = putDataInParentTreeMap(childDCMap, childDecopleCategory.getDecouplingCategoryId(), childDecopleCategory.getLeftIndex()+" : "+childDecopleCategory.getDecouplingCategoryName()+" : "+childDecopleCategory.getRightIndex(), IDPAConstants.ENTITY_DECOUPLING_CATEGORY,true,0,true);
								dcGroupList.add(childDCMap);
								tcChildrenLabelMap = new HashMap<String, Object>();
								tcChildrenLabelMap = putDataInLabelMap(tcChildrenLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
								groupList = new ArrayList<HashMap<String,Object>>();
								tcChildList = new ArrayList<HashMap<String, Object>>();
								for (TestCaseList testCase : listOfChildDCTestCases) {
									tcChildMap = new HashMap<String, Object>();
									tcChildMap = putDataInParentTreeMap(tcChildMap, testCase.getTestCaseId(), testCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
									tcChildList.add(tcChildMap);
								}
								tcChildrenLabelMap.put("children", tcChildList);
								groupList.add(tcChildrenLabelMap);
								childDCMap.put("children",groupList);
							}else{
								childDCMap = new HashMap<String, Object>();
								childDCMap = putDataInParentTreeMap(childDCMap, childDecopleCategory.getDecouplingCategoryId(), childDecopleCategory.getLeftIndex()+" : "+childDecopleCategory.getDecouplingCategoryName()+" : "+childDecopleCategory.getRightIndex(), IDPAConstants.ENTITY_DECOUPLING_CATEGORY,true,0,false);
								dcGroupList.add(childDCMap);
							}
						}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(dcGroupList);
		}
		return jsonArray;
	}

	@Override
	@Transactional
	public JSONArray getTestSuiteTestCaseMappingTree(int productId) {
		log.info("In getDecouplingTestCaseMappingTree() method");
		HashMap<String, Object> tcLabelMap = null;
		List<HashMap<String, Object>> tcList = null;
		HashMap<String, Object> parentTSMap = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> tsGroupList = null;
		JSONArray jsonArray = null;
		Set<TestSuiteList> listOfTestSuites = null;
		Set<TestCaseList> listOfTSTestCases = null;
		HashMap<String, Object> tcMap = null;
		listOfTestSuites = testSuiteListDAO.getTestSuiteByProductId(productId);
		if(listOfTestSuites != null){
			tsGroupList = new ArrayList<HashMap<String, Object>>();
			for (TestSuiteList testSuite : listOfTestSuites) {
				if(testSuite != null){
					Set<TestSuiteList> subTestSuites = testSuite.getTestSuiteList();
					if(subTestSuites != null && subTestSuites.size()>0){
						parentTSMap = new HashMap<String, Object>();
						parentTSMap = putDataInParentTreeMap(parentTSMap, testSuite.getTestSuiteId(), testSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,true);
						tsGroupList.add(parentTSMap);
						tcLabelMap = new HashMap<String, Object>();
						tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
						listOfTSTestCases = testSuite.getTestCaseLists();
						groupList = new ArrayList<HashMap<String,Object>>();
						tcList = new ArrayList<HashMap<String, Object>>();
						if(listOfTSTestCases != null && listOfTSTestCases.size()>0){
							for (TestCaseList tsTtestCase : listOfTSTestCases) {
								tcMap = new HashMap<String, Object>();
								tcMap = putDataInParentTreeMap(tcMap, tsTtestCase.getTestCaseId(), tsTtestCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
								tcList.add(tcMap);
							}
							tcLabelMap.put("children", tcList);
							groupList.add(tcLabelMap);
							parentTSMap.put("children",groupList);
							parentTSMap = new HashMap<String, Object>();
							parentTSMap = putDataInParentTreeMap(parentTSMap, testSuite.getTestSuiteId(), testSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,false);
							tsGroupList.add(parentTSMap);
						}
					}else{
						tcLabelMap = new HashMap<String, Object>();
						tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
						listOfTSTestCases = testSuite.getTestCaseLists();
						groupList = new ArrayList<HashMap<String,Object>>();
						tcList = new ArrayList<HashMap<String, Object>>();
						if(listOfTSTestCases != null && listOfTSTestCases.size()>0){
							for (TestCaseList tsTtestCase : listOfTSTestCases) {
								tcMap = new HashMap<String, Object>();
								tcMap = putDataInParentTreeMapCheck(tcMap, "C"+tsTtestCase.getTestCaseId(), tsTtestCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
								tcList.add(tcMap);
							}
							tcLabelMap.put("children", tcList);
							groupList.add(tcLabelMap);
							parentTSMap = new HashMap<String, Object>();
							parentTSMap = putDataInParentTreeMapCheck(parentTSMap, "P"+testSuite.getTestSuiteId(), testSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,false);
							parentTSMap.put("children",groupList);
							tsGroupList.add(parentTSMap);
						}else{
							parentTSMap = new HashMap<String, Object>();
							parentTSMap = putDataInParentTreeMapCheck(parentTSMap, "P"+testSuite.getTestSuiteId(), testSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,false);
							tsGroupList.add(parentTSMap);
						}
					}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(tsGroupList);
		}
		return jsonArray;
	}

	@Override
	@Transactional
	public JSONArray getChildTestSuiteTestCaseMappingTree(int parentId, String node) {
		log.info("In getChildTestSuiteTestCaseMappingTree() method");
		HashMap<String, Object> tcLabelMap = null;
		HashMap<String, Object> tcChildrenLabelMap = null;
		List<HashMap<String, Object>> tcList = null;
		List<HashMap<String, Object>> tcChildList = null;
		HashMap<String, Object> tcMap = null;
		HashMap<String, Object> tcChildMap = null;
		HashMap<String, Object> childTSMap = null;
		List<HashMap<String, Object>> groupList = null;
		List<HashMap<String, Object>> tsGroupList = null;
		JSONArray jsonArray = null;
		Set<TestSuiteList> listOfSubTestSuites = null;
		Set<TestCaseList> listOfTsTestCases = null;
		TestSuiteList tsuiteObj = testSuiteListDAO.getByTestSuiteId(parentId);
		if(tsuiteObj != null){
			listOfSubTestSuites = tsuiteObj.getTestSuiteList();
			log.info("listOfSubTestSuites: "+listOfSubTestSuites.size()+ " of Test Suite: "+tsuiteObj.getTestSuiteName());
		}
		tsGroupList = new ArrayList<HashMap<String, Object>>();
		listOfTsTestCases = tsuiteObj.getTestCaseLists();
		if(listOfTsTestCases != null && listOfTsTestCases.size()>0){
			tcLabelMap = new HashMap<String, Object>();
			tcLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
			tcList = new ArrayList<HashMap<String, Object>>();
			for (TestCaseList testCase : listOfTsTestCases) {
				tcMap = new HashMap<String, Object>();
				tcMap = putDataInParentTreeMap(tcMap, testCase.getTestCaseId(), testCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
				tcList.add(tcMap);
			}
			tcLabelMap.put("children", tcList);
			tsGroupList.add(tcLabelMap);
		}
		if(listOfSubTestSuites != null && listOfSubTestSuites.size()>0){
			for (TestSuiteList tSuite : listOfSubTestSuites) {
				if(tSuite != null){
						Set<TestCaseList> listOfChildTSTestCases = new HashSet<TestCaseList>();
						Set<TestSuiteList> subTestSuites = tSuite.getTestSuiteList();
						if(subTestSuites != null && subTestSuites.size()>0){
							childTSMap = new HashMap<String, Object>();
							childTSMap = putDataInParentTreeMap(childTSMap, tSuite.getTestSuiteId(), tSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,true);
							tsGroupList.add(childTSMap);
						}else{
							childTSMap = new HashMap<String, Object>();
							childTSMap = putDataInParentTreeMap(childTSMap, tSuite.getTestSuiteId(), tSuite.getTestSuiteName(), IDPAConstants.ENTITY_TEST_SUITE,true,0,false);
							tsGroupList.add(childTSMap);
							tcChildrenLabelMap = new HashMap<String, Object>();
							tcChildrenLabelMap = putDataInLabelMap(tcLabelMap, "Test Cases",IDPAConstants.ENTITY_TEST_CASE,true);
							listOfChildTSTestCases = tSuite.getTestCaseLists();
							groupList = new ArrayList<HashMap<String,Object>>();
							tcChildList = new ArrayList<HashMap<String, Object>>();
							if(listOfChildTSTestCases != null && listOfChildTSTestCases.size()>0){
								for (TestCaseList tsTtestCase : listOfChildTSTestCases) {
									tcChildMap = new HashMap<String, Object>();
									tcChildMap = putDataInParentTreeMap(tcChildMap, tsTtestCase.getTestCaseId(), tsTtestCase.getTestCaseName(), IDPAConstants.ENTITY_TEST_CASE,false,0,false);
									tcChildList.add(tcChildMap);
								}
								tcChildrenLabelMap.put("children", tcChildList);
								groupList.add(tcChildrenLabelMap);
								childTSMap.put("children",groupList);
							}
						}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(tsGroupList);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getEnvironmentGroupTree() {
		log.info("Fetching EnvironmentGroupTree");
		HashMap<String, Object> envGroupMap = null;
		List<HashMap<String, Object>> envGroupList = null;
		JSONArray jsonArray = null;
		List<EnvironmentGroup> listOfEnvironmentGroup = null;
		listOfEnvironmentGroup = environmentDAO.getEnvironmentGroupList();
		if(listOfEnvironmentGroup != null){
			envGroupList = new ArrayList<HashMap<String, Object>>();
			for (EnvironmentGroup eGroup : listOfEnvironmentGroup) {
				if(eGroup != null){
					List<EnvironmentCategory> listOfEnvCategory = environmentDAO.getEnvironmentCategoryListByGroup(eGroup.getEnvironmentGroupId());
					if(listOfEnvCategory != null && listOfEnvCategory.size()>0){
						envGroupMap = new HashMap<String, Object>();
						envGroupMap = putDataInParentTreeMap(envGroupMap, eGroup.getEnvironmentGroupId(), eGroup.getEnvironmentGroupName(), IDPAConstants.ENTITY_ENVIRONMENT_GROUP,true,0,true);
					}else{
						envGroupMap = new HashMap<String, Object>();
						envGroupMap = putDataInParentTreeMap(envGroupMap, eGroup.getEnvironmentGroupId(), eGroup.getEnvironmentGroupName(), IDPAConstants.ENTITY_ENVIRONMENT_GROUP,true,0,false);
					}
					envGroupList.add(envGroupMap);
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(envGroupList);
		}	
		return jsonArray;
	}

	@Override
	@Transactional
	public JSONArray getEnvironmentCategoryOfParentNodeTree(int parentId, String node) {
		log.debug("Fetching ChildDecouplingCategories Of ParentCatregory");
		HashMap<String, Object> childECMap = null;
		List<HashMap<String, Object>> ecGroupList = null;
		JSONArray jsonArray = null;
		List<EnvironmentCategory> listOfenvCategories = null;
		ecGroupList = new ArrayList<HashMap<String, Object>>();
		if(node.equalsIgnoreCase(IDPAConstants.ENTITY_ENVIRONMENT_GROUP)){
			listOfenvCategories = environmentDAO.getEnvironmentCategoryListByGroup(parentId);
		}else{
			listOfenvCategories = environmentDAO.getChildEnvCategoriesByParentCategoryId(parentId);
		}
		
		if(listOfenvCategories != null){
			for (EnvironmentCategory ec : listOfenvCategories) {
				if(ec != null){
					List<EnvironmentCategory> listOfSubEnvCategories = environmentDAO.getChildEnvCategoriesByParentCategoryId(ec.getEnvironmentCategoryId());
					
					if(listOfSubEnvCategories != null && listOfSubEnvCategories.size()>0){
						childECMap = new HashMap<String, Object>();
						childECMap = putDataInParentTreeMapCheck(childECMap,"P"+ec.getEnvironmentCategoryId(), ec.getEnvironmentCategoryName(), IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY,true,0,true);
					}else{
						childECMap = new HashMap<String, Object>();
						childECMap = putDataInParentTreeMapCheck(childECMap,"C"+ec.getEnvironmentCategoryId(),ec.getEnvironmentCategoryName(), IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY,true,0,false);
					}
					ecGroupList.add(childECMap);
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(ecGroupList);
		}
		return jsonArray;
	}
	
	public List<ProductMaster> getUserAssociatedProducts(int userRoleId, int userId, int filter){
		List<ProductMaster> listOfProducts = null;
		if(userRoleId == 1){
			listOfProducts = productMasterDAO.list(true);
		}
		if(filter == 0){
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
				listOfProducts = productMasterDAO.getProductsByWorkPackageForUserId(userRoleId, userId,filter);
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
				listOfProducts = productMasterDAO.getProductsByProductUserRoleForUserId(userRoleId, userId,0);
			}
		}else if(filter == 1){
			if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
				listOfProducts = productMasterDAO.getProductsByWorkPackageForUserId(userRoleId, userId,filter);
			}
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER || userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
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

	@Override
	public JSONArray getProductPlanTree(UserList user) {
		JSONArray jsonArray = null;
		HashMap<String, Object> productMap = null;
		List<HashMap<String, Object>> productList = null;
		int userRoleId = user.getUserRoleMaster().getUserRoleId();
		int userId = user.getUserId();
		List<ProductMaster> listOfProducts = null;
		
		List<ProductBuild> listOfUserAssociatedProductBuild = null;
		List<WorkPackage> listOfUserAssociatedWorkPackages = null;
		List<TestFactory> listOfTestFactories = null;
		if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD || userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProducts = getUserAssociatedProducts(userRoleId,userId,1);
		}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
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
		else{
			listOfProducts = productMasterDAO.list(false);
		}
		
		if(listOfProducts != null && listOfProducts.size()>0){
			productList = new ArrayList<HashMap<String, Object>>();
			for (ProductMaster product : listOfProducts) {
				if(product != null){
					productMap = new HashMap<String, Object>();
					productMap = putDataInParentTreeMap(productMap, product.getProductId(), product.getProductName(), IDPAConstants.ENTITY_PRODUCT,true,0,true);
					productList.add(productMap);
				}
			}
		}
		jsonArray = new JSONArray();
		jsonArray.addAll(productList);
		return jsonArray;
	}

	@Override
	public JSONArray loadProductVersions(UserList user, int entityId) {
		JSONArray jsonArray = null;
		HashMap<String, Object> productVersionMap = null;
		List<ProductVersionListMaster> listOfUserAssociatedProductVariants = null;
		List<ProductVersionListMaster> listOfProductVariants = null;
		List<ProductVersionListMaster> listOfProductVersionsOfProduct = null;
		List<HashMap<String, Object>> productVersionList = null;
		int userRoleId = user.getUserRoleMaster().getUserRoleId();
		int userId = user.getUserId();
		if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProductVersionsOfProduct = productVersionListMasterDAO.list(entityId);
			listOfUserAssociatedProductVariants = productVersionListMasterDAO.getUserAssociatedProductVariants(userRoleId,userId);
			listOfProductVariants = getProductVersionList(listOfUserAssociatedProductVariants, listOfProductVersionsOfProduct);
		}else{
			listOfProductVariants =  productVersionListMasterDAO.list(entityId);
		}
		if(listOfProductVariants != null && listOfProductVariants.size()>0){
			productVersionList = new ArrayList<HashMap<String, Object>>();
			for (ProductVersionListMaster version : listOfProductVariants) {
				if(version != null){
					productVersionMap = new HashMap<String, Object>();
					productVersionMap = putDataInParentTreeMap(productVersionMap, version.getProductVersionListId(), version.getProductVersionName(), IDPAConstants.ENTITY_PRODUCT_VERSION,true,entityId,true);
					productVersionList.add(productVersionMap);
				}
			}
		}
		jsonArray = new JSONArray();
		jsonArray.addAll(productVersionList);
		return jsonArray;
	}

	@Override
	public JSONArray loadProductBuilds(UserList user,int entityId) {
		JSONArray jsonArray = null;
		HashMap<String, Object> productBuildMap = null;
		List<ProductBuild> listOfUserAssociatedProductBuilds = null;
		List<ProductBuild> listOfProductBuilds = null;
		List<ProductBuild> listOfProductBuildsOfProduct = null;
		List<HashMap<String, Object>> productBuildList = null;
		int userRoleId = user.getUserRoleMaster().getUserRoleId();
		int userId = user.getUserId();
		if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfProductBuildsOfProduct = productBuildDAO.list(entityId);
			listOfUserAssociatedProductBuilds = productBuildDAO.getUserAssociatedProductBuilds(userRoleId,userId);
			listOfProductBuilds = getProductBuilds(listOfUserAssociatedProductBuilds, listOfProductBuildsOfProduct);
		}else{
			listOfProductBuilds = productBuildDAO.list(entityId);
		}
		productBuildList = new ArrayList<HashMap<String, Object>>();
		if(listOfProductBuilds != null && listOfProductBuilds.size()>0){
			for (ProductBuild productBuild : listOfProductBuilds) {
				if(productBuild != null){
					productBuildMap = new HashMap<String, Object>();
					productBuildMap = putDataInParentTreeMap(productBuildMap, productBuild.getProductBuildId(), productBuild.getBuildname(), IDPAConstants.ENTITY_PRODUCT_BUILD,true,entityId,true);
					productBuildList.add(productBuildMap);
				}
			}
		}
		jsonArray = new JSONArray();
		jsonArray.addAll(productBuildList);
		return jsonArray;
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
	

	@Override
	public JSONArray loadProductWorkPackages(UserList user,int entityId) {
		JSONArray jsonArray = null;
		HashMap<String, Object> productWorkpackagesMap = null;
		List<WorkPackage> listOfUserAssociatedWorkPackages = null;
		List<WorkPackage> listOfWorkPackages = null;
		List<WorkPackage> listOfWorkPackagesOfProductBuild = null;
		List<HashMap<String, Object>> workPackageList = null;
		int userRoleId = user.getUserRoleMaster().getUserRoleId();
		int userId = user.getUserId();
		if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
			listOfUserAssociatedWorkPackages = workPackageDAO.getUserAssociatedWorkPackages(userRoleId,userId);
			listOfWorkPackagesOfProductBuild = workPackageDAO.listWorkPackagesByProductBuild(entityId);
			listOfWorkPackages = getWorkPackages(listOfUserAssociatedWorkPackages,listOfWorkPackagesOfProductBuild);
		}else{
			listOfWorkPackages = workPackageDAO.listWorkPackagesByProductBuild(entityId);
		}
		workPackageList = new ArrayList<HashMap<String, Object>>();
		if(listOfWorkPackages != null && listOfWorkPackages.size()>0){
			for (WorkPackage workPackage : listOfWorkPackages) {
				if(workPackage != null){
					productWorkpackagesMap = new HashMap<String, Object>();
					productWorkpackagesMap = putDataInParentTreeMap(productWorkpackagesMap, workPackage.getWorkPackageId(), workPackage.getName(), IDPAConstants.ENTITY_WORK_PACKAGE,true,entityId,true);
					workPackageList.add(productWorkpackagesMap);
				}
			}
		}
		jsonArray = new JSONArray();
		jsonArray.addAll(workPackageList);
		return jsonArray;
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
	
	private boolean isContainingSkillChildCategories(Skill skill){
		boolean hasChildCategories = false;
		Set<Skill> childCategories = skill.getChildCategories();
		if(childCategories != null && childCategories.size()>0){
			hasChildCategories = true;
		}
		return hasChildCategories;
		
	}
	
	@Override
	public JSONArray getUserSkillsMappingTree() {
		log.info("In getUserSkillsMappingTree() method");
		HashMap<String, Object> parentSkillMap = null;
		List<HashMap<String, Object>> skillGroupList = null;
		JSONArray jsonArray = null;
		List<Skill> skillList = null;
		Integer rootSkillId = 0;
		Integer jtStartIndex = -1;
		Integer jtPageSize = -1;
		rootSkillId = skillDao.getRootUserSkillId(rootUserSkillsDescription);
		log.info("rootSkillId : "+rootSkillId);
		skillList = skillDao.getSkillListByRootId(jtStartIndex, jtPageSize, rootSkillId, true);
		
			skillGroupList = new ArrayList<HashMap<String, Object>>();
			for(Skill skill : skillList){
				if(skill != null){
					if(skill.getParentSkill() != null && skill.getParentSkill().getSkillId().equals(rootSkillId)){
					
						if(isContainingSkillChildCategories(skill)){
							parentSkillMap = new HashMap<String, Object>();
							parentSkillMap = putDataInParentTreeMap(parentSkillMap, skill.getSkillId(), skill.getLeftIndex()+" : "+skill.getSkillName()+" : "+skill.getRightIndex(), IDPAConstants.ENTITY_USER_SKILLS,true,0,true);
							skillGroupList.add(parentSkillMap);
						}else{
							parentSkillMap = new HashMap<String, Object>();
							parentSkillMap = putDataInParentTreeMap(parentSkillMap, skill.getSkillId(), skill.getLeftIndex()+" : "+skill.getSkillName()+" : "+skill.getRightIndex(), IDPAConstants.ENTITY_USER_SKILLS,true,0,false);
							skillGroupList.add(parentSkillMap);																																					
						}
					}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(skillGroupList);
			
		return jsonArray;
	}
	
	@Override
	public JSONArray getChildSkillsOfParentSkill(int skillId, String node) {

		log.info("In getChildSkillsOfParentSkill() method");
		HashMap<String, Object> childSkillMap = null;
		List<HashMap<String, Object>> skillGroupList = null;
		JSONArray jsonArray = null;
		List<Skill> listOfChildSkills = null;
		listOfChildSkills = skillDao.getChildSkillListByParentSkillId(skillId);
		skillGroupList = new ArrayList<HashMap<String, Object>>();
		
		if(listOfChildSkills != null){
			for (Skill childSkill : listOfChildSkills) {
				if(childSkill != null){					
						if(isContainingSkillChildCategories(childSkill)){
							childSkillMap = new HashMap<String, Object>();
							childSkillMap = putDataInParentTreeMap(childSkillMap, childSkill.getSkillId(), childSkill.getLeftIndex() + " : " +  childSkill.getSkillName() +  " : " +childSkill.getRightIndex(), IDPAConstants.ENTITY_USER_SKILLS,true,0,true);
							skillGroupList.add(childSkillMap);
						}else{
							childSkillMap = new HashMap<String, Object>();
							childSkillMap = putDataInParentTreeMap(childSkillMap, childSkill.getSkillId(), childSkill.getLeftIndex() + " : " +  childSkill.getSkillName() +  " : " +childSkill.getRightIndex(), IDPAConstants.ENTITY_USER_SKILLS,true,0,false);
							skillGroupList.add(childSkillMap);
						}
				}
			}
			jsonArray = new JSONArray();
			jsonArray.addAll(skillGroupList);
		}
		return jsonArray;	
	}
	public HashMap<String, Object> putDataInParentTreeMapCheck(HashMap<String, Object> mapObject, String keyId, String entityName, String entityType, boolean isParent, Integer parentId, boolean hasChildern){
		if(mapObject != null){
			mapObject.put("id", String.valueOf(keyId));
			mapObject.put("text", entityName);
			mapObject.put("children", hasChildern);
			mapObject.put("data", entityType);
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_FEATURE)){
				mapObject.put("icon","fa fa-crosshairs");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_CASE)){
				mapObject.put("icon","fa fa-crosshairs");
			}
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_CASE_SCRIPT)){
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
			if(entityType.equalsIgnoreCase(IDPAConstants.ENTITY_USER_SKILLS)){
				mapObject.put("icon","fa fa-chain");
			}
		}
		return mapObject;
	}
	
}