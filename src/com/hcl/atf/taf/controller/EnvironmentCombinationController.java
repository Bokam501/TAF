package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityService;


@Controller
public class EnvironmentCombinationController {
	private static final Log log = LogFactory.getLog(ChangeManagementController.class);

    @Autowired
	ActivityService activityService;
	

@RequestMapping(value="envicombi.unmappedto.activity.count",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableSingleResponse getUnMappedTestCaseListCountOfFeatureByProductFeatureId(@RequestParam int productId, @RequestParam int activityId) {
	JTableSingleResponse jTableSingleResponse;
	log.info("inside the envicombi.unmappedto.activity.count");	
	int unMappedEnviCombiOfActivity = 0;
	JSONObject unMappedEnviCombiCountObj =new JSONObject();
	try {	
		unMappedEnviCombiOfActivity = activityService.getUnMappedEnvironmentCombiListCountOfActivityByActivityId(productId,activityId,0);			
	
		unMappedEnviCombiCountObj.put("unMappedTCCount", unMappedEnviCombiOfActivity);						
		jTableSingleResponse = new JTableSingleResponse("OK",unMappedEnviCombiCountObj);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the EnvironmentCombination  & Activty association!");
            log.error("JSON ERROR getting UnMappedTestCases Of Feature By FeatureId", e);	 
        }
        
    return jTableSingleResponse;
}	

@RequestMapping(value="activity.unmappedenvironmentcombi.byProduct.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listUnMappedTestCaseWithCodeOfProductId(@RequestParam int productId, @RequestParam int activityId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
	log.info("activity.unmappedenvironmentcombi.byProduct.list");
	JTableResponse jTableResponse=null;
		try {	
			List<Object[]> unMappedenvironmentListObj = activityService.getUnMappedEnvironmentCombiListByActivityId(productId, activityId, jtStartIndex, jtPageSize,0);
		
			JSONArray unMappedJsonArray = new JSONArray();
			for (Object[] row : unMappedenvironmentListObj) {
				JSONObject jsobj =new JSONObject();
				jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				unMappedJsonArray.add(jsobj);					
			}				
			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
			unMappedenvironmentListObj = null;					 
		
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedEnvCombinationList for Product!");
            log.error("JSON ERROR", e);
        }
    return jTableResponse;
}




@RequestMapping(value="activity.envcombination.mapping",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableSingleResponse addEnvironmentCombinationToActivity(@ModelAttribute JsonEnvironmentCombination jsonEnvironmentCombination,@RequestParam Integer activityId,@RequestParam Integer enviCombiId, @RequestParam String maporunmap) {
	JTableSingleResponse jTableSingleResponse;
	log.info("inside the activity.envcombination.mapping ");		
	try {
		
			EnvironmentCombination envCombination = activityService.updateEnvironmentCombiToActivityOneToMany(enviCombiId, activityId, maporunmap);
			
			List<JsonEnvironmentCombination> jsonEnvironmentCombiToUI=new ArrayList<JsonEnvironmentCombination>();
			if(envCombination != null){
				jsonEnvironmentCombiToUI.add(new JsonEnvironmentCombination(envCombination));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonEnvironmentCombiToUI);		
			
		} catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the Activity  & Environment association!");
            log.error("JSON ERROR updating the Activity  & Environment association", e);	 
        }
    return jTableSingleResponse;
}

@RequestMapping(value="activity.Environmentcombination.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listEnvironmentCombinationsOfProduct(@RequestParam String activityId, @RequestParam int productId) {
	log.debug("activity.Environmentcombination.list");
	int intactivityId = Integer.parseInt(activityId);
	JTableResponse jTableResponse;		
		try {	
				List<Object[]> mappedECListObj = activityService.getMappedEnvCombiByActivityId(productId, intactivityId, -1, -1,0);
				JSONArray mappedJsonArray = new JSONArray();
				for (Object[] row : mappedECListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
					mappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
				mappedECListObj = null;
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching TestCases mapped to Feature!");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponse;
}
}




	