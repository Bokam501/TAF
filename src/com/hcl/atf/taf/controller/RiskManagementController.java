package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.Risk;
import com.hcl.atf.taf.model.RiskAssessment;
import com.hcl.atf.taf.model.RiskLikeHoodMaster;
import com.hcl.atf.taf.model.RiskMatrix;
import com.hcl.atf.taf.model.RiskMitigationMaster;
import com.hcl.atf.taf.model.RiskRating;
import com.hcl.atf.taf.model.RiskSeverityMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.RiskHazardTraceabilityMatrixDTO;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonRisk;
import com.hcl.atf.taf.model.json.JsonRiskAssessment;
import com.hcl.atf.taf.model.json.JsonRiskHazardTraceabilityMatrix;
import com.hcl.atf.taf.model.json.JsonRiskLikeHood;
import com.hcl.atf.taf.model.json.JsonRiskMitigation;
import com.hcl.atf.taf.model.json.JsonRiskRating;
import com.hcl.atf.taf.model.json.JsonRiskSeverity;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.RiskListService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class RiskManagementController {
	private static final Log log = LogFactory.getLog(RiskManagementController.class);

	@Autowired
	private RiskListService riskListService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService; 
	@Autowired
	private UserListService userListService;	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@RequestMapping(value="list.risks.by.product",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRisks(@RequestParam Integer productId, @RequestParam Integer riskStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside list.risks.by.product");
		JTableResponse jTableResponse;

		try {			
			List<Risk> riskList=riskListService.listRisks(productId, riskStatus, jtStartIndex, jtPageSize);
			List<Risk> riskListforPagination=riskListService.listRisks(productId, riskStatus, null, null);

			List<JsonRisk> jsonRisk=new ArrayList<JsonRisk>();
			for(Risk risks: riskList){
				jsonRisk.add(new JsonRisk(risks));
			}
			jTableResponse = new JTableResponse("OK", jsonRisk,riskListforPagination.size());     
			riskList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risks!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risks.by.product.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskList(HttpServletRequest request, @ModelAttribute JsonRisk jsonRisk, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			Risk risks  = jsonRisk.getRiskList();

			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonRisk.getProductId());
			risks.setProductMaster(productMasterFromUI);

			risks.setUserList(user);
			riskListService.addRisk(risks);			
			if(risks != null && risks.getRiskId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_RISK, risks.getRiskId(), risks.getRiskName(), user);
			}

			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRisk(risks));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risks.by.product.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskList(HttpServletRequest request, @ModelAttribute JsonRisk jsonRisk, BindingResult result) {			
		JTableResponse jTableResponse;
		TestFactory testFactory = null;		
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			Risk riskFromDB = riskListService.getByRiskId(jsonRisk.getRiskId());
			Risk riskFromUI = jsonRisk.getRiskList();
			Set<TestCaseList> tcSet=riskFromDB.getTestCaseList();
			Set<ProductFeature> pfSet=riskFromDB.getFeatureList();
			Set<RiskMitigationMaster> rmSet=riskFromDB.getMitigationList();
			Set<RiskAssessment> riskSet=riskFromDB.getAssessmentList();
			UserList user=(UserList)request.getSession().getAttribute("USER");
			productMaster = riskFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", Risk :"+riskFromDB.getRiskName();
			if (riskFromDB.getStatus() == riskFromUI.getStatus()){
				riskListService.updateRisk(riskFromUI);		
				if(riskFromUI != null && riskFromUI.getRiskId() != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK, riskFromUI.getRiskId(), riskFromUI.getRiskName(),
							jsonRisk.getModifiedField(), jsonRisk.getModifiedFieldTitle(),
							jsonRisk.getOldFieldValue(), jsonRisk.getModifiedFieldValue(), user, remarks);
				}								
			}else{
				if(tcSet.size() != 0 || pfSet.size() != 0 ||  rmSet.size() != 0 || riskSet.size() != 0){
					jTableResponse = new JTableResponse("ERROR","Testcase or Feature or Mitigation or Assessment mapped Risk cannot be inactive!");
					return jTableResponse;
				}else{
					riskListService.updateRisk(riskFromUI);
					if(riskFromUI != null && riskFromUI.getRiskId() != null){
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK, riskFromUI.getRiskId(), riskFromUI.getRiskName(),
								jsonRisk.getModifiedField(), jsonRisk.getModifiedFieldTitle(),
								jsonRisk.getOldFieldValue(), jsonRisk.getModifiedFieldValue(), user, remarks);
					}						
				}
			}
			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risks!");
			log.error("JSON ERROR updating Risks", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="test.case.feature.unmappedto.risk.count",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getUnMappedTestCaseFeatureListCountOfRisksByHazaradId(@RequestParam Integer productId, @RequestParam Integer productRiskId, @RequestParam Integer filter) {
		JTableSingleResponse jTableSingleResponse;
		log.debug("inside the test.case.feature.unmappedto.risk.list");	
		int unMappedTCorFeatureOfRisk = 0;
		JSONObject unMappedTCorFeatureCountObj =new JSONObject();
		try {	
			unMappedTCorFeatureOfRisk = riskListService.getUnMappedTestCaseorFeaturesListCountOfRisksByRiskId(productId, productRiskId, filter);			
			unMappedTCorFeatureCountObj.put("unMappedTCCount", unMappedTCorFeatureOfRisk);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedTCorFeatureCountObj);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmappedTestcase count to risk!!");
			log.error("JSON ERROR fetching unmappedTestcase count to risk", e);	 
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value="risks.unmappedtestcaseorfeature.byProduct.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUnMappedTestCaseFeatureWithHazaradsOfProductId(@RequestParam Integer productId, @RequestParam Integer productRiskId, @RequestParam Integer filter, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("risks.unmappedtestcase.byProduct.list");
		JTableResponse jTableResponse;
		try {	
			List<Object[]> unMappedTCListObj = riskListService.getUnMappedTestCaseorFeaturesListByProductRiskId(productId, productRiskId, filter, jtStartIndex, jtPageSize);

			JSONArray unMappedJsonArray = new JSONArray();
			for (Object[] row : unMappedTCListObj) {
				JSONObject jsobj =new JSONObject();
				jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				unMappedJsonArray.add(jsobj);					
			}				
			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
			unMappedTCListObj = null;					 

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedTestCaseList for Product!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="risks.test.case.feature.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskTestCasesFeatureswithcode(@RequestParam Integer productRiskId, @RequestParam Integer productId, @RequestParam Integer filter) {
		log.debug("risks.test.case.list.withcode");
		JTableResponse jTableResponse;		
		try {	
			List<Object[]> mappedTCListObj = riskListService.getMappedTestCaseorFeaturesListByProductRiskId(productId, productRiskId, filter, -1, -1);
			JSONArray mappedJsonArray = new JSONArray();
			for (Object[] row : mappedTCListObj) {
				JSONObject jsobj =new JSONObject();
				jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
				mappedJsonArray.add(jsobj);					
			}				
			jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
			mappedTCListObj = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching TestCases mapped to Risk!");
			log.error("JSON ERROR", e);
		}	        
		return jTableResponse;
	}

	@RequestMapping(value="test.case.risks.mapping",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskToTestCaseFeature(@RequestParam Integer productRiskId,@RequestParam Integer testcaseId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.case.risks.mapping");		
		try {

			Risk risk = riskListService.getByRiskId(productRiskId);				
			TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);

			TestCaseList testCase = riskListService.updateProductRiskTestCasesOneToMany(testCaseList.getTestCaseId(), risk.getRiskId(), maporunmap);

			List<JsonTestCaseList> jsonTestCaseListToUI=new ArrayList<JsonTestCaseList>();
			if(testCase != null){
				jsonTestCaseListToUI.add(new JsonTestCaseList(testCase));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseListToUI);			



		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase Product Risk  & Testcase association!");
			log.error("JSON ERROR updating the testcase Product Risk", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="feature.risks.mapping",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskToFeature(@RequestParam Integer productRiskId,@RequestParam Integer productFeatureId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.case.risks.mapping");		
		try {

			Risk risk = riskListService.getByRiskId(productRiskId);				
			ProductFeature  productFeature = productListService.getByProductFeatureId(productFeatureId);

			ProductFeature productFeatureList = riskListService.updateProductRiskFeaturesOneToMany(productFeature.getProductFeatureId(), risk.getRiskId(), maporunmap);

			List<JsonProductFeature> jsonFeatureToUI=new ArrayList<JsonProductFeature>();
			if(productFeatureList != null){
				jsonFeatureToUI.add(new JsonProductFeature(productFeatureList));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonFeatureToUI);			

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase Product Risk  & Testcase association!");
			log.error("JSON ERROR", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="risk.severity.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskSeverity(@RequestParam Integer productId, @RequestParam Integer riskSevStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside risk.severity.list");
		JTableResponse jTableResponse;

		try {			
			List<RiskSeverityMaster> riskSeverityList=riskListService.listRiskSeverity(productId, riskSevStatus, jtStartIndex, jtPageSize);
			List<RiskSeverityMaster> riskSeverityListforPagination=riskListService.listRiskSeverity(productId, riskSevStatus, null, null);

			List<JsonRiskSeverity> jsonRiskSeverity=new ArrayList<JsonRiskSeverity>();
			for(RiskSeverityMaster risks: riskSeverityList){
				jsonRiskSeverity.add(new JsonRiskSeverity(risks));
			}
			jTableResponse = new JTableResponse("OK", jsonRiskSeverity, riskSeverityListforPagination.size());     
			riskSeverityList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risk Severity!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risk.severity.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskSeverityList(HttpServletRequest request, @ModelAttribute JsonRiskSeverity jsonRiskSeverity, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskSeverityMaster riskSeverityMaster  = jsonRiskSeverity.getRiskSeverityList();
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonRiskSeverity.getProductId());
			riskSeverityMaster.setProductMaster(productMasterFromUI);
			riskListService.addRiskSeverity(riskSeverityMaster);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			UserList userObj = userListService.getUserListById(user.getUserId());
			if(riskSeverityMaster != null && riskSeverityMaster.getRiskSeverityId() != null && userObj != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_RISK_SEVERITY, riskSeverityMaster.getRiskSeverityId(), riskSeverityMaster.getSeverityName(), userObj);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRiskSeverity(riskSeverityMaster));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risk.severity.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskSeverityList(HttpServletRequest request, @ModelAttribute JsonRiskSeverity jsonRiskSeverity, BindingResult result) {				
		JTableResponse jTableResponse;
		TestFactory testFactory = null;		
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskSeverityMaster riskSevFromUI = jsonRiskSeverity.getRiskSeverityList();
			RiskSeverityMaster riskSevFromDB =  riskListService.getRiskSeverityById(jsonRiskSeverity.getRiskSeverityId());
			Set<RiskAssessment> rsSet=riskSevFromDB.getRiskAssessment();
			UserList user=(UserList)request.getSession().getAttribute("USER");
			UserList userObj = userListService.getUserListById(user.getUserId());
			productMaster = riskSevFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", RiskSeverityName :"+riskSevFromDB.getSeverityName();
			if (riskSevFromDB.getStatus() == riskSevFromUI.getStatus()){
				riskListService.updateRiskSeverity(riskSevFromUI);				
				if(riskSevFromUI != null && riskSevFromUI.getRiskSeverityId() != null && userObj != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_SEVERITY, riskSevFromUI.getRiskSeverityId(), riskSevFromUI.getSeverityName(),
							jsonRiskSeverity.getModifiedField(), jsonRiskSeverity.getModifiedFieldTitle(),
							jsonRiskSeverity.getOldFieldValue(), jsonRiskSeverity.getModifiedFieldValue(), userObj, remarks);
				}
			}else{
				if (rsSet.size() != 0 ){
					jTableResponse = new JTableResponse("ERROR","Mapped Risk Severity with Risk Assessment, cannot be inactive!");
					return jTableResponse;
				}else{
					riskListService.updateRiskSeverity(riskSevFromUI);
					if(riskSevFromUI != null && riskSevFromUI.getRiskSeverityId() != null){
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_SEVERITY,  riskSevFromUI.getRiskSeverityId(), riskSevFromUI.getSeverityName(),
								jsonRiskSeverity.getModifiedField(), jsonRiskSeverity.getModifiedFieldTitle(),
								jsonRiskSeverity.getOldFieldValue(), jsonRiskSeverity.getModifiedFieldValue(), user, remarks);
					}
				}
			}
			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Severity!");
			log.error("JSON ERROR updating Risk Severity", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="risk.likehood.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskLikeHood(@RequestParam Integer productId, @RequestParam Integer riskLikeStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside risk.likehood.list");
		JTableResponse jTableResponse;

		try {			
			List<RiskLikeHoodMaster> riskLikeHoodList=riskListService.listRiskLikeHood(productId, riskLikeStatus, jtStartIndex, jtPageSize);
			List<RiskLikeHoodMaster> riskLikeHoodListforPagination=riskListService.listRiskLikeHood(productId, riskLikeStatus, null, null);

			List<JsonRiskLikeHood> jsonRiskLikeHood=new ArrayList<JsonRiskLikeHood>();
			for(RiskLikeHoodMaster riskLike: riskLikeHoodList){
				jsonRiskLikeHood.add(new JsonRiskLikeHood(riskLike));
			}
			jTableResponse = new JTableResponse("OK", jsonRiskLikeHood, riskLikeHoodListforPagination.size());     
			riskLikeHoodList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risk LikeHood!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risk.likehood.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskLikeHoodList(HttpServletRequest request, @ModelAttribute JsonRiskLikeHood jsonRiskLikeHood, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskLikeHoodMaster riskLikeHoodMaster  = jsonRiskLikeHood.getRiskLikeHoodList();
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonRiskLikeHood.getProductId());
			riskLikeHoodMaster.setProductMaster(productMasterFromUI);
			riskListService.addRiskLikeHood(riskLikeHoodMaster);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(riskLikeHoodMaster != null && riskLikeHoodMaster.getRiskLikeHoodId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_RISK_LIKEHOOD, riskLikeHoodMaster.getRiskLikeHoodId(), riskLikeHoodMaster.getLikeHoodName(), user);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRiskLikeHood(riskLikeHoodMaster));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risk.likehood.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskLikeHoodList(HttpServletRequest request, @ModelAttribute JsonRiskLikeHood jsonRiskLikeHood, BindingResult result) {				
		JTableResponse jTableResponse;
		TestFactory testFactory = null;		
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskLikeHoodMaster riskLikeFromUI = jsonRiskLikeHood.getRiskLikeHoodList();
			RiskLikeHoodMaster riskLikeFromDB = riskListService.getRiskLikeHoodById(jsonRiskLikeHood.getRiskLikeHoodId());
			Set<RiskAssessment> raSet=riskLikeFromDB.getRiskAssessment();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			productMaster = riskLikeFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", RiskLikeHood :"+riskLikeFromDB.getLikeHoodName();
			if (riskLikeFromDB.getStatus() == riskLikeFromUI.getStatus()){
				riskListService.updateRiskLikeHood(riskLikeFromUI);
				if(riskLikeFromUI != null && riskLikeFromUI.getRiskLikeHoodId() != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_LIKEHOOD, riskLikeFromUI.getRiskLikeHoodId(), riskLikeFromUI.getLikeHoodName(),
							jsonRiskLikeHood.getModifiedField(), jsonRiskLikeHood.getModifiedFieldTitle(),
							jsonRiskLikeHood.getOldFieldValue(), jsonRiskLikeHood.getModifiedFieldValue(), user, remarks);
				}								
			}else{
				if (raSet.size() != 0 ){
					jTableResponse = new JTableResponse("ERROR","Mapped Risk LikeHood with Risk Assessment, cannot be inactive!");
					return jTableResponse;
				}else{
					riskListService.updateRiskLikeHood(riskLikeFromUI);
				}
			}
			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Like Hood!");
			log.error("JSON ERROR updating Risk Like Hood", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="risk.mitigation.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskMitigation(@RequestParam Integer productId,  @RequestParam Integer riskMitigationStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside risk.mitigation.list");
		JTableResponse jTableResponse;

		try {			
			List<RiskMitigationMaster> riskMitigationList=riskListService.listRiskMitigation(productId, riskMitigationStatus, jtStartIndex, jtPageSize);
			List<RiskMitigationMaster> riskMitigationListforPagination=riskListService.listRiskMitigation(productId, riskMitigationStatus, null, null);

			List<JsonRiskMitigation> jsonRiskMitigation=new ArrayList<JsonRiskMitigation>();
			for(RiskMitigationMaster riskLike: riskMitigationList){
				jsonRiskMitigation.add(new JsonRiskMitigation(riskLike));
			}
			jTableResponse = new JTableResponse("OK", jsonRiskMitigation, riskMitigationListforPagination.size());     
			riskMitigationList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risk Mitigation!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risk.mitigation.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskMitigationList(HttpServletRequest request, @ModelAttribute JsonRiskMitigation jsonRiskMitigation, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskMitigationMaster riskMitigationMaster  = jsonRiskMitigation.getRiskMitigationList();
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonRiskMitigation.getProductId());
			riskMitigationMaster.setProductMaster(productMasterFromUI);
			riskMitigationMaster.setIsAvailable(1);
			riskListService.addRiskMitigation(riskMitigationMaster);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(riskMitigationMaster != null && riskMitigationMaster.getRiskMitigationId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_RISK_MITIGATION, riskMitigationMaster.getRiskMitigationId(), riskMitigationMaster.getRmCode(), user);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRiskMitigation(riskMitigationMaster));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risk.mitigation.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskMitigationList(HttpServletRequest request, @ModelAttribute JsonRiskMitigation jsonRiskMitigation, BindingResult result) {				
		JTableResponse jTableResponse;
		TestFactory testFactory = null;		
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskMitigationMaster riskMitigationFromUI = jsonRiskMitigation.getRiskMitigationList();
			RiskMitigationMaster riskMitigationFromDB = riskListService.getRiskMitigationById(jsonRiskMitigation.getRiskMitigationId());
			Set<Risk> riskSet=riskMitigationFromDB.getRiskList();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			productMaster = riskMitigationFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", RiskMitigation :"+riskMitigationFromDB.getRmCode();
			if (riskMitigationFromDB.getStatus() == riskMitigationFromUI.getStatus()){
				riskListService.updateRiskMitigation(riskMitigationFromUI);
				if(riskMitigationFromUI != null && riskMitigationFromUI.getRiskMitigationId() != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_MITIGATION, riskMitigationFromUI.getRiskMitigationId(), riskMitigationFromUI.getRmCode(),
							jsonRiskMitigation.getModifiedField(), jsonRiskMitigation.getModifiedFieldTitle(),
							jsonRiskMitigation.getOldFieldValue(), jsonRiskMitigation.getModifiedFieldValue(), user, remarks);
				}	
			}else{
				if (riskSet.size() != 0 ){
					jTableResponse = new JTableResponse("ERROR","Mapped Risk Mitigation with Risks, cannot be inactive!");
					return jTableResponse;
				}else{
					riskListService.updateRiskMitigation(riskMitigationFromUI);
					if(riskMitigationFromUI != null && riskMitigationFromUI.getRiskMitigationId() != null){
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_MITIGATION, riskMitigationFromUI.getRiskMitigationId(), riskMitigationFromUI.getRmCode(),
								jsonRiskMitigation.getModifiedField(), jsonRiskMitigation.getModifiedFieldTitle(),
								jsonRiskMitigation.getOldFieldValue(), jsonRiskMitigation.getModifiedFieldValue(), user, remarks);
					}	
				}
			}
			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Mitigation!");
			log.error("JSON ERROR updating Risk Mitigation", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="mitigation.risks.mapping",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskToMitigation(@RequestParam Integer productRiskId,@RequestParam Integer riskMitigationId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the mitigation.risks.mapping");		
		try {

			Risk risk = riskListService.getByRiskId(productRiskId);				
			RiskMitigationMaster riskMitigationMaster = riskListService.getByRiskMitigationId(riskMitigationId);

			RiskMitigationMaster riskMitigationList = riskListService.updateProductRiskMitigationsOneToMany(riskMitigationMaster.getRiskMitigationId(), risk.getRiskId(), maporunmap);

			List<JsonRiskMitigation> jsonRiskMitigationToUI=new ArrayList<JsonRiskMitigation>();
			if(riskMitigationList != null){
				jsonRiskMitigationToUI.add(new JsonRiskMitigation(riskMitigationList));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonRiskMitigationToUI);			

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the Product Risk  & Risk Mitigation association!");
			log.error("JSON ERROR updating the Product Risk", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="risk.severity.list.for.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listRiskSeverityforOptions(@RequestParam Integer productId) {
		log.debug("inside risk.severity.list");
		JTableResponseOptions jTableResponseOptions=null;

		try {			
			List<RiskSeverityMaster> riskSeverityList=riskListService.listRiskSeverity(productId, 1, null, null);

			List<JsonRiskSeverity> jsonRiskSeverity=new ArrayList<JsonRiskSeverity>();
			if (riskSeverityList !=  null){
				for(RiskSeverityMaster risks: riskSeverityList){
					jsonRiskSeverity.add(new JsonRiskSeverity(risks));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonRiskSeverity);     
				riskSeverityList = null;
			}
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Risk Severity!");
			log.error("JSON ERROR", e);
		}
		return jTableResponseOptions;
	}

	@RequestMapping(value="risk.likehood.list.for.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listRiskLikeHoodforOptions(@RequestParam Integer productId) {
		log.debug("inside risk.likehood.list.for.options");
		JTableResponseOptions jTableResponseOptions= null;

		try {			
			List<RiskLikeHoodMaster> riskLikeHoodList=riskListService.listRiskLikeHood(productId, 1, null, null);

			List<JsonRiskLikeHood> jsonRiskLikeHood=new ArrayList<JsonRiskLikeHood>();
			if (riskLikeHoodList !=  null){
				for(RiskLikeHoodMaster riskLike: riskLikeHoodList){
					jsonRiskLikeHood.add(new JsonRiskLikeHood(riskLike));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonRiskLikeHood);     
				riskLikeHoodList = null;
			}
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Risk LikeHood!");
			log.error("JSON ERROR", e);
		}
		return jTableResponseOptions;
	}


	@RequestMapping(value="risk.rating.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskRating(@RequestParam Integer productId,  @RequestParam Integer riskRatingStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside risk.rating.list");
		JTableResponse jTableResponse;

		try {			
			List<RiskRating> riskRatingList=riskListService.listRiskRating(productId, riskRatingStatus, jtStartIndex, jtPageSize);
			List<RiskRating> riskRatingListforPagination=riskListService.listRiskRating(productId, riskRatingStatus, null, null);

			List<JsonRiskRating> jsonRiskRating=new ArrayList<JsonRiskRating>();
			for(RiskRating riskRating: riskRatingList){
				jsonRiskRating.add(new JsonRiskRating(riskRating));
			}
			jTableResponse = new JTableResponse("OK", jsonRiskRating, riskRatingListforPagination.size());     
			riskRatingList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risk Rating!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risk.rating.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskRatingList(HttpServletRequest request, @ModelAttribute JsonRiskRating jsonRiskRating, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskRating riskRating  = jsonRiskRating.getRiskRatingList();
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonRiskRating.getProductId());
			riskRating.setProductMaster(productMasterFromUI);

			riskListService.addRiskRating(riskRating);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(riskRating != null && riskRating.getRiskRatingId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_RISK_RATING, riskRating.getRiskRatingId(), riskRating.getRatingName(), user);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRiskRating(riskRating));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risk.rating.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskRatingList(HttpServletRequest request, @ModelAttribute JsonRiskRating jsonRiskRating, BindingResult result) {				
		JTableResponse jTableResponse;
		TestFactory testFactory = null;		
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskRating riskRatingFromUI = jsonRiskRating.getRiskRatingList();
			RiskRating riskRatingFromDB = riskListService.getRiskRatingById(jsonRiskRating.getRiskRatingId());
			Set<RiskAssessment> rsSet=riskRatingFromDB.getRiskAssessment();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			productMaster = riskRatingFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", RiskRating :"+riskRatingFromDB.getRatingName();
			if (riskRatingFromDB.getStatus() == riskRatingFromUI.getStatus()){
				riskListService.updateRiskRating(riskRatingFromUI);
				if(riskRatingFromUI != null && riskRatingFromUI.getRiskRatingId() != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_RATING, riskRatingFromUI.getRiskRatingId(), riskRatingFromUI.getRatingName(),
							jsonRiskRating.getModifiedField(), jsonRiskRating.getModifiedFieldTitle(),
							jsonRiskRating.getOldFieldValue(), jsonRiskRating.getModifiedFieldValue(), user, remarks);
				}	
			}else{
				if (rsSet.size() != 0 ){
					jTableResponse = new JTableResponse("ERROR","Mapped Risk Rating with Risk Assessment, cannot be inactive!");
					return jTableResponse;
				}else{
					riskListService.updateRiskRating(riskRatingFromUI);
					if(riskRatingFromUI != null && riskRatingFromUI.getRiskRatingId() != null){
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_RISK_RATING, riskRatingFromUI.getRiskRatingId(), riskRatingFromUI.getRatingName(),
								jsonRiskRating.getModifiedField(), jsonRiskRating.getModifiedFieldTitle(),
								jsonRiskRating.getOldFieldValue(), jsonRiskRating.getModifiedFieldValue(), user, remarks);
					}
				}
			}
			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Rating!");
			log.error("JSON ERROR updating Risk Rating", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="risk.assessment.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRiskAssessment(@RequestParam Integer riskId, @RequestParam Integer filter, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside risk.assessment.list");
		JTableResponse jTableResponse;

		try {			
			List<RiskAssessment> riskAssessmentList=riskListService.listRiskAssessment(riskId, filter, jtStartIndex, jtPageSize);
			List<RiskAssessment> riskAssessmentListforPagination=riskListService.listRiskAssessment(riskId, filter, null, null);

			List<JsonRiskAssessment> jsonRiskAssessment=new ArrayList<JsonRiskAssessment>();
			for(RiskAssessment riskAssess: riskAssessmentList){
				jsonRiskAssessment.add(new JsonRiskAssessment(riskAssess));
			}
			jTableResponse = new JTableResponse("OK", jsonRiskAssessment, riskAssessmentListforPagination.size());     
			riskAssessmentList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Risk Mitigation!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	@RequestMapping(value="risk.assessment.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addRiskAssessmentList(HttpServletRequest request, @ModelAttribute JsonRiskAssessment jsonRiskAssessment, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			RiskAssessment riskAssessment  = jsonRiskAssessment.getRiskAssessmentList();
			riskAssessment.setUserList(user);
			boolean isPreMitigationavailable = riskListService.getPreMitigationavailable(jsonRiskAssessment.getRiskId());
			if(jsonRiskAssessment.getMitigationType().equals(1) ){
				if (isPreMitigationavailable){
					jTableSingleResponse = new JTableSingleResponse("ERROR","Already Pre-Mitigation is available for the Risk!");
					return jTableSingleResponse;
				}else{
					riskListService.addRiskAssessment(riskAssessment);
				}
			}else{
				riskListService.addRiskAssessment(riskAssessment);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonRiskAssessment(riskAssessment));
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
			log.error("JSON ERROR", e);
		}		        
		return jTableSingleResponse;			
	}

	@RequestMapping(value="risk.assessment.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskAssessmentList(HttpServletRequest request, @ModelAttribute JsonRiskAssessment jsonRiskAssessment, BindingResult result) {				
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
		}			
		try {
			RiskAssessment riskAssessmentFromUI =  jsonRiskAssessment.getRiskAssessmentList();
			riskListService.updateRiskAssessment(riskAssessmentFromUI);

			jTableResponse = new JTableResponse("OK");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Assessment!");
			log.error("JSON ERROR updating Risk Assessment", e);
		}
		return jTableResponse;			
	}

	@RequestMapping(value="risk.rating.list.for.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listRiskRatingforOptions(@RequestParam Integer productId) {
		log.debug("inside risk.rating.list.for.options");
		JTableResponseOptions jTableResponseOptions= null;

		try {			
			List<RiskRating> riskRatingList=riskListService.listRiskRating(productId, 1, null, null);

			List<JsonRiskRating> jsonRiskRating=new ArrayList<JsonRiskRating>();
			if (riskRatingList !=  null){
				for(RiskRating riskRat: riskRatingList){
					jsonRiskRating.add(new JsonRiskRating(riskRat));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonRiskRating);     
				riskRatingList = null;
			}
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Risk Rating!");
			log.error("JSON ERROR", e);
		}
		return jTableResponseOptions;
	}

	@RequestMapping(value="lifecycle.list.for.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listLifeCycleforOptions() {
		log.debug("inside risk.rating.list.for.options");
		JTableResponseOptions jTableResponseOptions= null;

		try {			
			List<LifeCyclePhase> lifeCyclePhaseList=riskListService.listLifeCycle();

			List<JsonRiskAssessment> jsonRiskAssessmentList=new ArrayList<JsonRiskAssessment>();
			if (lifeCyclePhaseList !=  null){
				for(LifeCyclePhase life: lifeCyclePhaseList){
					JsonRiskAssessment jsonRiskAssessment=new JsonRiskAssessment();
					jsonRiskAssessment.setLifeCyclePhaseId(life.getLifeCyclePhaseId());
					jsonRiskAssessment.setLifeCyclePhaseName(life.getName());
					jsonRiskAssessmentList.add(jsonRiskAssessment);
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonRiskAssessmentList);     
				lifeCyclePhaseList = null;
			}
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Risk Rating!");
			log.error("JSON ERROR", e);
		}
		return jTableResponseOptions;
	}

	@RequestMapping(value="risk.matrix.for.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String listRiskRatingMatrix(@RequestParam Integer productId, @RequestParam Integer riskMatStatus) {
		log.debug("inside risk.matrix. for.list");
		JTableResponseOptions jTableResponseOptions= null;
		String finalResult="";
		Map<Integer,RiskSeverityMaster> riskSeverityMasterMap=new HashMap<Integer,RiskSeverityMaster>();
		RiskSeverityMaster riskSeverity=new RiskSeverityMaster();
		Integer riskSeverityId;
		Integer riskLikeHoodId;
		Integer riskLikeHoodListSize;
		Integer indexOfriskLikeHoodList;
		int index=-1;
		JSONArray columnData1 = new JSONArray();
		JSONObject colm = new JSONObject();
		JSONArray list = new JSONArray();
		int headerIndex=3;
		try {			
			JSONObject finalObj = new JSONObject();
			JSONObject sevrtiyNameTitle= new JSONObject();
			JSONObject sevrtiyRatingTitle= new JSONObject();
			JSONObject sevrtiyId= new JSONObject();
			JSONObject likeHoodIdTitle= new JSONObject();
			JSONObject likeHoodTitle= new JSONObject();
			JSONObject likeHoodNameTitle= new JSONObject();


			JSONArray columnData = new JSONArray();
			JSONArray dynamicLikeHoodHeaders = new JSONArray();
			List<String>  finallist=new ArrayList<String>();

			List<RiskLikeHoodMaster> riskLikeHoodList=riskListService.listRiskLikeHood(productId, riskMatStatus, null, null);
			riskLikeHoodListSize=riskLikeHoodList.size();
		
			String likeHoddName="";
			for (RiskLikeHoodMaster riskLike : riskLikeHoodList) {
				likeHoodTitle= new JSONObject();
				likeHoddName=riskLike.getLikeHoodName() + " ( "+ riskLike.getLikeHoodRating() + " ) ";
				likeHoodTitle.put("Id", "riskLikeHood"+riskLike.getRiskLikeHoodId());
				likeHoodTitle.put("title",likeHoddName);

				list.add(likeHoodTitle);
				likeHoddName="";
			}
			finalObj.put("COLUMNS", list);
			List<Object[]> riskSeverityList=riskListService.listRiskRatingMatrix(productId,-1);
			Integer riskServerityCount=riskListService.gerRiskSeverityMasterSizeByProductId(productId);

			List<String> ratingCol = null;


			for (Object[] objSev : riskSeverityList) {
				RiskLikeHoodMaster riskLikeHoodMaster=new RiskLikeHoodMaster();

				riskSeverityId=(Integer)objSev[0];
				riskLikeHoodId=(Integer)objSev[3];
				riskLikeHoodMaster.setRiskLikeHoodId(riskLikeHoodId);

				if(!riskSeverityMasterMap.containsKey(riskSeverityId)){
					index++;
					colm = new JSONObject();
					for (RiskLikeHoodMaster riskLike : riskLikeHoodList) {
						colm.put("riskLikeHood"+riskLike.getRiskLikeHoodId(),"");
					}

				
					colm.put("riskSeverityId",riskSeverityId);
					colm.put("severityName",(String) objSev[1]);
					colm.put("severityRating",(String) objSev[2]);
					columnData1.add(index,colm);
					riskSeverity.setRiskSeverityId((Integer)objSev[0]);
					riskSeverityMasterMap.put(riskSeverityId,riskSeverity);
					riskSeverity=new RiskSeverityMaster();
				}
			

				if(riskLikeHoodList.size()!=0){
					
					colm.put("riskLikeHood"+riskLikeHoodId,(Integer) objSev[5]);
				}
				columnData1.set(index,colm);

			}

			finalObj.put("DATA", columnData1);
			finalObj.put("RiskSeveritiesCount",riskServerityCount);
			finalResult=finalObj.toString();
			return "["+finalResult+"]";

		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}		        
		return "["+finalResult+"]";
	}


	@RequestMapping(value="risk.matrix.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRiskAssessmentList(@RequestParam Integer productId,HttpServletRequest req) {				
		JTableResponse jTableResponse;

		try {
			log.info("req.getParameter"+req.getParameter("riskSeverityId"));
			int riskSeverityId=Integer.parseInt(req.getParameter("riskSeverityId"));
			Integer riskLikeHoodIdFromDb=0;
			int riskLikeHoodRatingFromUI=0;
			Integer ratingFromDb;
			boolean updateFlag=false;
			boolean saveFlag=false;
			Integer ristMatrixFromDBArr[];
			RiskMatrix riskMatrix=new RiskMatrix();
			int arrIndex=0;
			List<RiskMatrix> listRiskMatrix=riskListService.listRiskMatrixByRiskSeverityId(riskSeverityId);
			if(listRiskMatrix!=null){
				ristMatrixFromDBArr=new Integer[listRiskMatrix.size()];
			}else{
				ristMatrixFromDBArr=new Integer[0];
			}
			
			if(listRiskMatrix!=null){
				for(RiskMatrix riskMatrixFromDB:listRiskMatrix){
					if(riskMatrixFromDB.getRiskLikeHood()!=null){
						
						riskLikeHoodIdFromDb=riskMatrixFromDB.getRiskLikeHood().getRiskLikeHoodId();
						ristMatrixFromDBArr[arrIndex]=riskLikeHoodIdFromDb;
						arrIndex++;
						 riskLikeHoodRatingFromUI=Integer.parseInt(req.getParameter("riskLikeHood"+riskLikeHoodIdFromDb));
							if(riskLikeHoodRatingFromUI!=riskMatrixFromDB.getRiskRating().getRiskRatingId()){
								riskMatrix=riskMatrixFromDB;
								updateFlag=true;
								break;
					}
				}
			}
			}
				if(!updateFlag){
					List<RiskLikeHoodMaster> riskLikeHoodMasterList=	riskListService.listRiskLikeHoodByNotInRiskMatrix(productId,ristMatrixFromDBArr);
					for(RiskLikeHoodMaster riskLikeHoodMaster:riskLikeHoodMasterList){
						riskLikeHoodIdFromDb=riskLikeHoodMaster.getRiskLikeHoodId();
						if(req.getParameter("riskLikeHood"+riskLikeHoodIdFromDb)!=""){
							 riskLikeHoodRatingFromUI=Integer.parseInt(req.getParameter("riskLikeHood"+riskLikeHoodIdFromDb));
							 saveFlag=true;
									 break;
						}
							
						}
				}
				
			if(updateFlag){
				RiskRating riskRating = new RiskRating();
				riskRating.setRiskRatingId(riskLikeHoodRatingFromUI);
				riskMatrix.setRiskRating(riskRating);
			}	
			
			if(saveFlag){
				
				RiskRating riskRating = new RiskRating();
				RiskSeverityMaster riskSeverityMaster = new RiskSeverityMaster();
				riskSeverityMaster.setRiskSeverityId(riskSeverityId);
				RiskLikeHoodMaster riskLikeHoodMaster=new RiskLikeHoodMaster();
				riskLikeHoodMaster.setRiskLikeHoodId(riskLikeHoodIdFromDb);
				riskRating.setRiskRatingId(riskLikeHoodRatingFromUI);
				
				riskMatrix.setRiskRating(riskRating);
				riskMatrix.setRiskLikeHood(riskLikeHoodMaster);
				riskMatrix.setRiskSeverity(riskSeverityMaster);
				
			}
			riskListService.updateRiskMatrix(riskMatrix);
			
			jTableResponse = new JTableResponse("OK", "Rating Matrix is been Updated");   

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Risk Matrix!");
			log.error("JSON ERROR updating Risk Matrix", e);
		}
		return jTableResponse;			
	}
	
	@RequestMapping(value = "risk.hazard.traceability.matrix", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse riskHazardTraceabilityMatrix(@RequestParam Integer productId) {
		log.debug("inside risk.hazard.traceability.matrix");
		JTableResponse jTableResponse = null;
		
		try {
			List<RiskHazardTraceabilityMatrixDTO> riskHazardTraceMatrixdto = new ArrayList<RiskHazardTraceabilityMatrixDTO>();
			riskHazardTraceMatrixdto=riskListService.riskHazardTraceService(productId);

			List<JsonRiskHazardTraceabilityMatrix> jsonriskTraceMatrixList = new ArrayList<JsonRiskHazardTraceabilityMatrix>();
			if (riskHazardTraceMatrixdto == null || riskHazardTraceMatrixdto.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList, 0);
			}else{
				
				for (RiskHazardTraceabilityMatrixDTO riskHazardTraceDto : riskHazardTraceMatrixdto) {
					jsonriskTraceMatrixList.add(new JsonRiskHazardTraceabilityMatrix(riskHazardTraceDto));
				}				
				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList,jsonriskTraceMatrixList.size() );
				riskHazardTraceMatrixdto = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);	            
		}
		return jTableResponse;
	}
}
