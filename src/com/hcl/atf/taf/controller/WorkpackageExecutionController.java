package com.hcl.atf.taf.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.json.JsonLogFileContent;
import com.hcl.atf.taf.model.json.JsonWorkPackage;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.WorkPackageExecutionService;

@Controller
public class WorkpackageExecutionController {

	private static final Log log = LogFactory.getLog(WorkpackageExecutionController.class);
	
	@Autowired
	WorkPackageExecutionService workPackageExecutionService;
	
	@RequestMapping(value="workpackage.job.automated.abort",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse abortAutomatedTestRunJob(@RequestParam int testRunJobId) {

		log.debug("workpackage.automated.abort");
		JTableResponse jTableResponse = null;

		if (testRunJobId < 1) {
			jTableResponse = new JTableResponse("ERROR","Unable to Abort. Job id is not valid : " + testRunJobId);	
		    return jTableResponse;
		}
		try {
			boolean success = workPackageExecutionService.abortAutomatedTestRunJob(testRunJobId);
			if (success)
				jTableResponse = new JTableResponse("OK","Abort process for Job : " + testRunJobId + " initiated. This may take upto a couple of minutes to complete.");
			else 
				jTableResponse = new JTableResponse("ERROR","Unable to Abort Job. Job could have already been completed : " + testRunJobId);
		} catch (Exception e) {

			log.error("Unable to abort the job :" + testRunJobId);
			jTableResponse = new JTableResponse("ERROR","Unable to abort Job due to unknown reason : " + testRunJobId);	
		}
	    return jTableResponse;
	}
	
	@RequestMapping(value="workpackage.execute.singletestcase",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse executeSingleAutomatedTestCase(HttpServletRequest req , @RequestParam Map<String, String>  mapData) {
		Integer testCaseId = Integer.parseInt(mapData.get("testCaseId") );
		Integer testRunPlanId = Integer.parseInt(mapData.get("testRunPlanId") );
		String runconfigIdStr= mapData.get("runconfigId");
		JTableResponse jTableResponse = null;
		UserList user=(UserList)req.getSession().getAttribute("USER");
		if (testCaseId == null || testCaseId < 0) {
			jTableResponse = new JTableResponse("ERROR","Testcase Id is not valid : " + testCaseId);	
		    return jTableResponse;
		}
		try {
			boolean success = workPackageExecutionService.executeSingleAutomatedTestCase(testCaseId, testRunPlanId , user ,runconfigIdStr);
			String jobIds = ScriptLessExecutionDTO.getJobIDs() ;
			
			if (success){
				if(jobIds != null && !jobIds.trim().equalsIgnoreCase("")){
					jTableResponse = new JTableResponse("OK", "Test case execution initiated. Workpackage " + ScriptLessExecutionDTO.getWorkPackageId()+"["+ ScriptLessExecutionDTO.getWorkPackageName()  + "]" + " created.  Jobs are "+jobIds.substring(0, jobIds.length()-1));
				} else {
					jTableResponse = new JTableResponse("OK", "Test case execution initiated. Workpackage " + ScriptLessExecutionDTO.getWorkPackageId()+"["+ ScriptLessExecutionDTO.getWorkPackageName()  + "]" + " created.");
				}
			}else {
				jTableResponse = new JTableResponse("ERROR","Unable to execute test case, may be Test Case is not associated with TestRunPlan");
				return jTableResponse;
			}
		} catch (Exception e) {

			log.error("Unable to execute test case :" + testCaseId + " on test run plan : " + testRunPlanId);
			jTableResponse = new JTableResponse("ERROR","Unable to execute test case :" + testCaseId + " on test run plan : " + testRunPlanId);	
		}
	    return jTableResponse;
	}
	
	@RequestMapping(value = "testjob.livelog.status", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse getJobLogUpdatedContent(@RequestParam Integer testJobId, @RequestParam Integer lastLine) {	
		
		JTableSingleResponse jTableSingleResponse = null;
		try {	
			
			if (testJobId == null)
				return new JTableSingleResponse("ERROR","Invalid Job ID");
			
			JsonLogFileContent jsonLogFileContent = workPackageExecutionService.getJobLogUpdatedContent(testJobId, lastLine);
			if(jsonLogFileContent != null) {
				log.info("===================================>" + jsonLogFileContent.getLogFileContent());
			} else if (jsonLogFileContent == null) 
				return new JTableSingleResponse("ERROR","Invalid details for fetching log content");
			
			jTableSingleResponse = new JTableSingleResponse("OK",jsonLogFileContent);	

		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value="workpackage.automated.abort",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse abortAutomatedWorkPackage(@RequestParam int workPackageId) {

		log.debug("workpackage.automated.abort");
		JTableResponse jTableResponse = null;

		if (workPackageId < 1) {
			jTableResponse = new JTableResponse("ERROR","Unable to Abort. WorkPackage id is not valid : " + workPackageId);	
		    return jTableResponse;
		}
		try {
			boolean success = workPackageExecutionService.abortAutomatedWorkPackage(workPackageId);
			if (success)
				jTableResponse = new JTableResponse("OK","Abort process for WorkPackage : " + workPackageId + " initiated. This may take upto a couple of minutes to complete.");
			else 
				jTableResponse = new JTableResponse("ERROR","Unable to Abort WorkPackage. Job could have already been completed : " + workPackageId);
		} catch (Exception e) {

			log.error("Unable to abort the WorkPackage :" + workPackageId);
			jTableResponse = new JTableResponse("ERROR","Unable to abort WorkPackage due to unknown reason : " + workPackageId);	
		}
	    return jTableResponse;
	}

}
