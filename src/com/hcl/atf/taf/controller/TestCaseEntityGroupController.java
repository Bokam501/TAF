/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.hcl.atf.taf.dao.TestCaseEntityGroupsDAO;
import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseEntityGroupHasTestCase;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonTestCaseEntityGroup;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.TestCaseEntityGroupService;

/**
 * @author silambarasur
 *
 */
@Controller
public class TestCaseEntityGroupController {
	
	private static final Log log = LogFactory.getLog(TestCaseEntityGroupController.class);
	
	@Autowired
	private TestCaseEntityGroupService testCaseEntityGroupService;
	
	@Autowired
	private TestCaseEntityGroupsDAO testCaseEntityGroupsDAO;
	
	
	@RequestMapping(value = "testcase.entity.group.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse getTestCaseEntityGroupList(HttpServletRequest req,@RequestParam Integer productId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize) {
		JTableResponse jTableResponse = null;
		List<JsonTestCaseEntityGroup> jsonTestCaseEntityGroups = new ArrayList<JsonTestCaseEntityGroup>();
		List<TestCaseEntityGroup> testCaseEntityGroupList = null;
		try {
			testCaseEntityGroupList = testCaseEntityGroupService.getAllTestCaseEntityGroups(productId);
			if (testCaseEntityGroupList != null && testCaseEntityGroupList.size() > 0) {
				for (TestCaseEntityGroup testCaseEntityGroup : testCaseEntityGroupList) {
					jsonTestCaseEntityGroups.add(new JsonTestCaseEntityGroup(testCaseEntityGroup));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonTestCaseEntityGroups,
					jsonTestCaseEntityGroups.size());
			log.info("process fetching testcase.entity.group.list completed");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "testCase.entity.group.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse addTestCaseEntityGroup(HttpServletRequest request,
			@ModelAttribute JsonTestCaseEntityGroup jsonTestCaseEntityGroup,
			BindingResult result) {
		JTableResponse jTableResponse = null;
		List<JsonTestCaseEntityGroup> jsonTestCaseEntityGroupList=new ArrayList<JsonTestCaseEntityGroup>();
		TestCaseEntityGroup testCaseEntityGroupFromDB = null;
		String testCaseEntityGroupName = "";
		UserList userList = (UserList)request.getSession().getAttribute("USER");
		try {

			TestCaseEntityGroup testCaseEntityGroup = jsonTestCaseEntityGroup.getTestCaseEntityGroup();
			testCaseEntityGroupName = testCaseEntityGroup.getTestCaseEntityGroupName().trim();	
			if(testCaseEntityGroupName != "" && testCaseEntityGroupName != null){
				testCaseEntityGroupFromDB = testCaseEntityGroupService.getTestCaseEntityGroupByName(testCaseEntityGroupName);
				if(testCaseEntityGroupFromDB != null){
					return jTableResponse = new JTableResponse("ERROR","Test Scenario name already exists!");
				}
			}else{
				return jTableResponse = new JTableResponse("ERROR","Test Scenario name should not be empty!");
			}
			testCaseEntityGroup.setCreatedBy(userList);
			testCaseEntityGroup.setModifiedBy(userList);
			testCaseEntityGroup.setCreatedDate(new Date());
			testCaseEntityGroup.setModifiedDate(new Date());
			testCaseEntityGroupService.addTestCaseEntityGroup(testCaseEntityGroup);
			jsonTestCaseEntityGroupList.add(new JsonTestCaseEntityGroup(testCaseEntityGroup));
			jTableResponse = new JTableResponse("OK",jsonTestCaseEntityGroupList);
			log.info("testCase.entity.group.add Success");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error in adding TestScenario!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "testCase.entity.group.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateTestCaseEntityGroup(HttpServletRequest request,
			@ModelAttribute JsonTestCaseEntityGroup jsonTestCaseEntityGroup,
			BindingResult result) {
		JTableResponse jTableResponse = null;
		TestCaseEntityGroup testCaseEntityGroupFromDB = null;
		String testCaseEntityGroupName = "";
		List<JsonTestCaseEntityGroup> jsonTestCaseEntityGroupList=new ArrayList<JsonTestCaseEntityGroup>();
		try {
			List<TestCaseEntityGroupHasTestCase> testscenarioAssociationList= testCaseEntityGroupService.getMappedTestCaseAssociationByTestcaseScenarioId(jsonTestCaseEntityGroup.getTestCaseEntityGroupId());
			TestCaseEntityGroup testCaseEntityGroup = jsonTestCaseEntityGroup.getTestCaseEntityGroup();
			if(jsonTestCaseEntityGroup.getModifiedFieldTitle().equalsIgnoreCase("Name")){
				testCaseEntityGroupName = testCaseEntityGroup.getTestCaseEntityGroupName().trim();	
				if(testCaseEntityGroupName != "" && testCaseEntityGroupName != null){
					testCaseEntityGroupFromDB = testCaseEntityGroupService.getTestCaseEntityGroupByName(testCaseEntityGroupName);
					if(testCaseEntityGroupFromDB != null){
						return jTableResponse = new JTableResponse("ERROR","Test Scenario name already exists!");
					}
				}else{
					return jTableResponse = new JTableResponse("ERROR","Test Scenario name should not be empty!");
				}
			}			
			if(jsonTestCaseEntityGroup.getModifiedFieldTitle().equalsIgnoreCase("Parent Scenario")){
				TestCaseEntityGroup parentTestCaseEntityGroups = testCaseEntityGroup.getParentEntityGroupId();
				if(parentTestCaseEntityGroups != null){
					if(parentTestCaseEntityGroups.getTestCaseEntityGroupId() == 0){
						TestCaseEntityGroup tcg = testCaseEntityGroupService.getTestCaseEntityGroupByName("---");
						testCaseEntityGroup.setParentEntityGroupId(tcg);
					}
					else if(parentTestCaseEntityGroups.getTestCaseEntityGroupId() != null){
						parentTestCaseEntityGroups = testCaseEntityGroupService.getTestCaseEntityGrouById(parentTestCaseEntityGroups.getTestCaseEntityGroupId());
						testCaseEntityGroup.setParentEntityGroupId(parentTestCaseEntityGroups);
					}
				}
			}			
			testCaseEntityGroupService.updateTestCaseEntityGroup(testCaseEntityGroup);
			
			if(testscenarioAssociationList != null && testscenarioAssociationList.size() >0) {
				for(TestCaseEntityGroupHasTestCase testScenarioAssocition :testscenarioAssociationList) {
					 testCaseEntityGroupsDAO.updateTestCaseToTestScenario(testScenarioAssocition, "map");
				}
			}
			jsonTestCaseEntityGroupList.add(new JsonTestCaseEntityGroup(testCaseEntityGroup));
			jTableResponse = new JTableResponse("OK",jsonTestCaseEntityGroupList);
			log.info("testCase.entity.group.update Success");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error in updating TestScenario!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="unmapped.testCases.list.for.testScenario",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnMappedTestScriptsByProductId(@RequestParam int productId,@RequestParam int testScenarioId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
    	log.debug("unmapped.testCases.list.for.testScenario");
    	JTableResponse jTableResponse=null;
    		try {	
    			List<Object[]> unMappedTestCasesListObj = testCaseEntityGroupService.getUnMappedTestCasesByProductId(productId, testScenarioId, jtStartIndex, jtPageSize);
    		
    			JSONArray unMappedJsonArray = new JSONArray();
    			for (Object[] row : unMappedTestCasesListObj) {
    				JSONObject jsonObj =new JSONObject();
    				jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
    				jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
    				unMappedJsonArray.add(jsonObj);					
    			}				
    			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
    			unMappedTestCasesListObj = null;					 
    		
    		} catch (Exception e) {
                jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedTestCaseList for TestScenario!");
                log.error("JSON ERROR", e);
            }
        return jTableResponse;
    }
    
    @RequestMapping(value="testCases.mapped.to.testScenario.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listMappedTestScriptsByTestCaseId(@RequestParam int testScenarioId) {
    	log.debug("testCases.mapped.to.testScenario.list");
    	JTableResponse jTableResponse;		
    		try {	
    				List<Object[]> mappedTestCasesObj = testCaseEntityGroupService.getMappedTestCasesByTestScenarioId(testScenarioId);
    				JSONArray mappedJsonArray = new JSONArray();
    				for (Object[] row : mappedTestCasesObj) {
    					JSONObject jsonObj =new JSONObject();
    					jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
    					jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
    					mappedJsonArray.add(jsonObj);					
    				}				
    				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
    				mappedTestCasesObj = null;
            } catch (Exception e) {
                jTableResponse = new JTableResponse("ERROR","Error fetching mapped TestCases for TestScenario!");
                log.error("JSON ERROR", e);
            }	        
        return jTableResponse;
    }
    
    @RequestMapping(value="unmapped.testCases.count.for.testScenario",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse getUnMappedTestScriptsCountByProductId(@RequestParam int productId, @RequestParam int testScenarioId) {
    	log.debug("unmapped.testCases.count.for.testScenario");
    	JTableSingleResponse jTableSingleResponse;
    	int unMappedTestCasesCount = 0;
    	JSONObject unMappedTestCasesCountObj =new JSONObject();
    	try {	
    		unMappedTestCasesCount = testCaseEntityGroupService.getUnMappedTestCasesCountByProductId(productId, testScenarioId);		    	
    		unMappedTestCasesCountObj.put("unMappedTCCount", unMappedTestCasesCount);						
    		jTableSingleResponse = new JTableSingleResponse("OK",unMappedTestCasesCountObj);
            } catch (Exception e) {
                jTableSingleResponse = new JTableSingleResponse("ERROR","Error fetching unMappedTestCasesCount!");
                log.error("JSON ERROR", e);	 
            }
            
        return jTableSingleResponse;
    }
    
    @RequestMapping(value="testCase.map.or.unmap.to.testScenario",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse mapOrUnmapTestScripts(HttpServletRequest request, @RequestParam Integer testScenarioId,@RequestParam Integer testCaseId, @RequestParam String maporunmap) {
    	log.debug("testCase.map.or.unmap.to.testScenario");
    	JTableSingleResponse jTableSingleResponse = null;
    		try {
    		
    		UserList userList = (UserList)request.getSession().getAttribute("USER");
    		testCaseEntityGroupService.updateTestCaseToTestScenario(testScenarioId, testCaseId, userList, maporunmap);    		
    					    			
    		} catch (Exception e) {
                jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the TestCase  & TestScenario association!");
                log.error("JSON ERROR", e);	 
            }
    	
        return jTableSingleResponse;
    }
	
}
