package com.hcl.atf.taf.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.dto.DefectReportDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.json.JsonDefectManagementSystem;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.DefectUploadService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class DefectUploadController {

	private static final Log log = LogFactory
			.getLog(DefectUploadController.class);

	@Autowired
	private DefectUploadService defectUploadService;
	
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private DefectManagementService defectManagementService;
	
	
	@RequestMapping(value="export.to.generate.auth.key", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse exportToGetAuthKey() {
		log.debug("export.to.generate.auth.key");
		JTableResponse jTableResponse;
		try {
			String isexportComplete = defectUploadService.exportToGetAuthKey();
			jTableResponse = new JTableResponse("OK",isexportComplete);
		} catch (Exception e) {
			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Export completed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="export.to.google.drive", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse exportToGoogleDrive(@RequestParam Integer productId, @RequestParam Integer productVersionId, @RequestParam Integer productBuildId,
    		@RequestParam Integer workPackageId, @RequestParam Integer approveStatus, @RequestParam  Date startDate, @RequestParam  Date endDate, @RequestParam String code) {
		log.debug("test.export.to.google");
		JTableResponse jTableResponse;
		try {
			
			Integer isexportComplete = defectUploadService.exportToGoogleDriveDefects(code, productId, productVersionId, productBuildId, workPackageId, approveStatus, startDate, endDate);
			if(isexportComplete.equals(1)){
				jTableResponse = new JTableResponse("OK","Defect(s) are exported to Google Drive");
			}else if(isexportComplete.equals(2)){
				jTableResponse = new JTableResponse("OK","Defect(s) are already exported to Google Drive");
			}else if(isexportComplete.equals(3)){
				jTableResponse = new JTableResponse("OK","No Defect(s) are exported to Google Drive");
			}else{
				jTableResponse = new JTableResponse("ERROR","Defect(s) are not Exported to Google Drive");
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Export completed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="export.to.google.drive.test.results", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse exportToGoogleDriveTestResults(@RequestParam Integer workPackageId, @RequestParam String code) {
		log.debug("test.export.to.google.reports");
		JTableResponse jTableResponse;
		try {
			
			boolean isexportComplete = defectUploadService.exportToGoogleDriveTestResults(workPackageId, code);
			if(isexportComplete){
				jTableResponse = new JTableResponse("OK","Test Results has been Exported to Google Drive");
			}else{
				jTableResponse = new JTableResponse("ERROR","Test Results is not Exported to Google Drive");
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Export completed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="defect.bugnizer.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse defectDataimport(HttpServletRequest request) {
		log.debug("defect.bugnizer");
		JTableResponse jTableResponse;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			boolean isImportComplete = excelTestDataIntegrator.importDefectfromBugnizer(fileName, is);
			
			if(isImportComplete){
				log.info("Import is Completed.");
				jTableResponse = new JTableResponse("Ok","Import is Completed.");
			} else{
				log.info("Import is not completed");
				jTableResponse = new JTableResponse("Ok","Import is not completed");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="list.defects.for.analyse",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDefectsForAnalyse(@RequestParam Integer productId,@RequestParam Integer productVersionId,@RequestParam Integer productBuildId,
    		@RequestParam Integer workPackageId,@RequestParam Integer approveStatus, @RequestParam  Date startDate, @RequestParam  Date endDate,@RequestParam Integer issueStatus, @RequestParam Integer analyseStatus, 
    		HttpServletRequest req, Integer jtStartIndex, Integer jtPageSize) {
			log.debug("list.defects.for.analyse");
			log.info("Product Id====>"+productId);
			log.info("product BuildId====>"+productBuildId);
			log.info("workPackageId--->"+workPackageId);
			List<TestExecutionResultBugList> defectList=null;
			JTableResponse jTableResponse = null;
			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
				try {
					defectList	= defectUploadService.listDefectsByTestcaseExecutionPlanIdAnalyse(productId, productVersionId,productBuildId,workPackageId,approveStatus,startDate, endDate,issueStatus, analyseStatus, jtStartIndex, jtPageSize);
					List<TestExecutionResultBugList> defectListforPagination=defectUploadService.listDefectsByTestcaseExecutionPlanIdAnalyse(productId, productVersionId,productBuildId,workPackageId,approveStatus,startDate, endDate, issueStatus, analyseStatus, null, null);
					for(TestExecutionResultBugList defect: defectList){
						jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defect));
					}
					if(jsonTestExecutionResultBugList.size()!=0) {
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,defectListforPagination.size() );
					}else{    
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);  
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
	
	
	@RequestMapping(value="defects.analyse.from.data.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDefectsAnalyseFromData(@RequestParam Integer executionResultBugId, @RequestParam Integer action, HttpServletRequest req, Integer jtStartIndex, Integer jtPageSize) {
			log.debug("defects.analyse.from.data.list");
			List<TestExecutionResultBugList> defectList=null;
			List<TestExecutionResultBugList> defectListforPagination = null;
			JTableResponse jTableResponse = null;
			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
				try {
					TestExecutionResultBugList  testExecution=defectUploadService.listDefectsBySpecificTestcaseExecutionBugId(executionResultBugId);
					String stageName = null;
					if (testExecution != null){
						if (testExecution.getDefectFoundStage().getStageId() != 1){
							stageName = IDPAConstants.STAGE_NAME_LIVE;
						}else {
							stageName = IDPAConstants.STAGE_NAME_WEB;
						}
						String featureName = null;
						if (testExecution.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature() != null){
							featureName=testExecution.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature().getProductFeatureName();
						}
						
						defectList	= defectUploadService.listDefectsAnalyseFromData(stageName,featureName, action, jtStartIndex, jtPageSize);
						defectListforPagination=defectUploadService.listDefectsAnalyseFromData(stageName,featureName, action ,null, null);
						for(TestExecutionResultBugList defect: defectList){
							jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defect));
						}
					}else{
						defectList	= defectUploadService.listDefectsAnalyseFromData(stageName, null, action, jtStartIndex, jtPageSize);
						defectListforPagination=defectUploadService.listDefectsAnalyseFromData(stageName, null, action ,null, null);
						for(TestExecutionResultBugList defect: defectList){
							jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defect));
						}
					}
					
					if(jsonTestExecutionResultBugList.size()!= 0 ) {
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,defectListforPagination.size() );
					}else{    
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);  
					}
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
	
	
	@RequestMapping(value="defects.analyse.from.data.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse defectAnalyseUpdate(HttpServletRequest request, @RequestParam Integer action,  @RequestParam Integer testBugId) {
		JTableSingleResponse jTableSingleResponse;
		try {
				boolean isUpdateComplete = defectUploadService.updateDefectAnalyseUpdate(testBugId, action);
				if (isUpdateComplete){
					jTableSingleResponse = new JTableSingleResponse("OK","Defects are analysed!");
				}else{
					jTableSingleResponse = new JTableSingleResponse("ERROR","Defects are not analysed!");
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error Updating Records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="list.defects.for.analyse.using.testCaseExecutionResultId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDefectsForAnalyseUsingSpecificTestCaseExecutionResultId(@RequestParam Integer bugId) {
		
		JTableResponse jTableResponse = null;
		List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
			try {
				log.info("Bug id==>"+bugId);
				TestExecutionResultBugList defectListUsingId=defectUploadService.listDefectsBySpecificTestcaseExecutionBugId(bugId);
				if(defectListUsingId!=null){
					jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defectListUsingId));
				}
					
				if(jsonTestExecutionResultBugList.size()!=0) {
					jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,jsonTestExecutionResultBugList.size() );
				}else{    
					jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);  
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
		
	    }
	
	@RequestMapping(value="defects.for.analyse.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDefectbyIssueId(@ModelAttribute JsonTestExecutionResultBugList jsonTestExecutionResultBugList, BindingResult result) {
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		TestExecutionResultBugList testExecutionResultBugListUI = null;
		try {
			testExecutionResultBugListUI= jsonTestExecutionResultBugList.getTestExecutionResultBugList();
			defectUploadService.updateDefectbyIssueId(testExecutionResultBugListUI);		
			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Customer!");
	            log.error("JSON ERROR", e);
	        }	        
	        
        return jTableResponse;

    }	

	@RequestMapping(value = "environment.combination.based.report.testcase", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse listAllWorkPackageEnvironmenCombTestCase(@RequestParam Integer workPackageId) {
		log.debug("inside environment.combination.based.report.testcase");
		JTableResponse jTableResponse;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> testCaseDTOList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try {

			HashMap<String,JsonWorkPackageTestCaseExecutionPlanForTester> mapOfmapOfJsonWPTester = null;
			mapOfmapOfJsonWPTester = defectUploadService.getworkPackageEnvironmentSummary(workPackageId);
			log.debug("mapOfmapOfJsonWPTester---size---"+mapOfmapOfJsonWPTester.size());
				if(mapOfmapOfJsonWPTester != null && mapOfmapOfJsonWPTester.size()>0){
					Set<String> keySet = mapOfmapOfJsonWPTester.keySet();
					if(keySet != null && keySet.size()>0){
						for (String userEnvCombination : keySet) {
							Integer totNotExecuted = 0;
							Integer totExecuted = 0;
							JsonWorkPackageTestCaseExecutionPlanForTester jsonWorkPackagePlan = mapOfmapOfJsonWPTester.get(userEnvCombination);
							if (jsonWorkPackagePlan.getNotExecuted() != null)
							{
								totNotExecuted = jsonWorkPackagePlan.getNotExecuted() ;
							}
							if (jsonWorkPackagePlan.getTotalExecutedTesCases() != null)
							{
								totExecuted = jsonWorkPackagePlan.getTotalExecutedTesCases() ;
							}
							jsonWorkPackagePlan.setTotalExecutionTCs(totNotExecuted+totExecuted);
							testCaseDTOList.add(jsonWorkPackagePlan);
						}
					}
				}
				jTableResponse = new JTableResponse("OK", testCaseDTOList, testCaseDTOList.size());	
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "environment.combination.based.report.tester", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String listAllWorkPackageEnvironmenCombTester(@RequestParam Integer workPackageId) {			
		String finalResult="";
		try {
			JSONObject finalObj = new JSONObject();
			JSONObject slNoTitle= new JSONObject();
			JSONObject testerNameTitle= new JSONObject();
			JSONObject rcTitle= null;
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();

			Integer counter=1;

			slNoTitle.put("title", "Sl No");
			list.add(slNoTitle);
			testerNameTitle.put("title", "Tester Name");
			list.add(testerNameTitle);
			List<String> wpTCExeutionPlan=defectUploadService.listRunConfigurationNameBywpId(workPackageId);
			int i = 1;
			for(String wptep: wpTCExeutionPlan){
				rcTitle= new JSONObject();
				rcTitle.put("title", "Device "+ i++);
				list.add(rcTitle);
			}
			finalObj.put("COLUMNS", list);
			columnData = new JSONArray();
			List<TestCaseDTO> wptcplanListforTester = defectUploadService.listWorkPackageTestCasesExecutionPlanBywpId(workPackageId);
			
			Map <String, List<String>> userEnvironmentMap = new HashMap<String, List<String>>();
			
			for (TestCaseDTO testCaseDTO : wptcplanListforTester) {
				List<String> envCombinationNames = null;
				String mapKey = testCaseDTO.getTesterId()+"~"+testCaseDTO.getTesterName();
				if(userEnvironmentMap.containsKey(mapKey)){
					envCombinationNames = userEnvironmentMap.get(mapKey);
					envCombinationNames.add(testCaseDTO.getEnvCombName());
					userEnvironmentMap.put(mapKey, envCombinationNames);
				}else{
					envCombinationNames = new ArrayList<String>();
					envCombinationNames.add(testCaseDTO.getEnvCombName());
					userEnvironmentMap.put(mapKey, envCombinationNames);
				}
			}
			
			Set<String> userKeys = userEnvironmentMap.keySet();
			for (String userKey : userKeys) {
				columnData = new JSONArray();
				columnData.add(counter++);
				columnData.add(userKey.substring(userKey.indexOf("~") + 1));
				List<String> userEnvCombinations = userEnvironmentMap.get(userKey);
				if(userEnvCombinations.size() == wpTCExeutionPlan.size()){
					for (String userEnvComb : userEnvCombinations) {
						columnData.add(userEnvComb);
					}
				}else if(userEnvCombinations.size() < wpTCExeutionPlan.size()){
					for (String userEnvComb : userEnvCombinations) {
						columnData.add(userEnvComb);
					}
					Integer difference = wpTCExeutionPlan.size()-userEnvCombinations.size();
					for(int diffCounter=0; diffCounter<difference; diffCounter++){
						columnData.add("");
					}
				}
				
				columnData1.add(columnData);
			}
			finalObj.put("DATA", columnData1);

			finalResult=finalObj.toString();
			return "["+finalResult+"]";

		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}		        
		return "["+finalResult+"]";
	}
	
	@RequestMapping(value = "environment.combination.based.report.timesheet", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String listAllWorkPackageEnvironmenCombTimeSheet(@RequestParam Integer workPackageId) {			
		String finalResult="";
		try {
			JSONObject finalObj = new JSONObject();
			JSONObject slNoTitle= new JSONObject();
			JSONObject testerNameTitle= new JSONObject();
			JSONObject rcTitle= new JSONObject();
			JSONObject startTitle= new JSONObject();
			JSONObject endTitle= new JSONObject();
			JSONObject totalTitle= new JSONObject();
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			
			JSONArray totalData = new JSONArray();

			Integer counter=1;

			slNoTitle.put("title", "Sl No");
			list.add(slNoTitle);
			rcTitle.put("title", "Device Name");
			list.add(rcTitle);
			testerNameTitle.put("title", "Tester Name");
			list.add(testerNameTitle);
			startTitle.put("title", "Start Time");
			list.add(startTitle);
			endTitle.put("title", "End Time");
			list.add(endTitle);
			totalTitle.put("title", "Time(S)");
			list.add(totalTitle);
			finalObj.put("COLUMNS", list);
			List<TestCaseDTO> wptcplanListforTester = defectUploadService.listWorkPackageTimeSheetBywpId(workPackageId);
			long totalHours = 0;
			for (TestCaseDTO testCaseDTO : wptcplanListforTester) {
				columnData = new JSONArray();
				columnData.add(counter++);
				columnData.add(testCaseDTO.getEnvCombName());
				if (testCaseDTO.getStartTime() != null && testCaseDTO.getEndTime() != null)
				{	columnData.add(testCaseDTO.getTesterName());
					columnData.add(DateUtility.getTimeStampinHHmmss(testCaseDTO.getStartTime()));
					columnData.add(DateUtility.getTimeStampinHHmmss(testCaseDTO.getEndTime()));
					long timeinHours = DateUtility.DateDifferenceInMinutes(testCaseDTO.getStartTime(), testCaseDTO.getEndTime());
					totalHours = totalHours + timeinHours;
					String timeDuration = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)timeinHours));
					columnData.add(timeDuration);
				}else{
					columnData.add("");
					columnData.add("");
					columnData.add("");
					columnData.add("");
				}
				columnData1.add(columnData);
			}
			finalObj.put("DATA", columnData1);
			
			totalData.add("");
			totalData.add("");
			totalData.add("");
			totalData.add("");
			totalData.add("");
			String totalTimeDuration = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)totalHours));
			totalData.add(totalTimeDuration);
			finalObj.put("TOTAL", totalData);
			finalResult=finalObj.toString();
			return "["+finalResult+"]";

		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}		        
		return "["+finalResult+"]";
	}
	
	@RequestMapping(value = "defect.system.name.code.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse listDefectSystemNameAndCode(@RequestParam Integer testExecutionResultsBugId) {
		log.debug("inside defect.system.name.code.list");
		JTableResponse jTableResponse = null;
		
		try {
			List<DefectReportDTO> defectReportdto = new ArrayList<DefectReportDTO>();
			defectReportdto=defectManagementService.listDefectNameAndCode(testExecutionResultsBugId);

			List<JsonDefectManagementSystem> defecJsonList = new ArrayList<JsonDefectManagementSystem>();
			if (defectReportdto == null || defectReportdto.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", defecJsonList, 0);
			}else{
				
				for (DefectReportDTO defectReportDto : defectReportdto) {
					defecJsonList.add(new JsonDefectManagementSystem(defectReportDto));
				}				
				jTableResponse = new JTableResponse("OK", defectReportdto,defectReportdto.size() );
				defectReportdto = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);	            
		}
		return jTableResponse;
	}
	
}
