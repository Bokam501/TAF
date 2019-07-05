package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.MetricsMaster;
import com.hcl.atf.taf.model.json.JsonMetricsMaster;
import com.hcl.atf.taf.model.json.JsonMetricsMasterProgramDefects;
import com.hcl.atf.taf.model.json.JsonMetricsProgramExecutionProcess;
import com.hcl.atf.taf.model.json.JsonMetricsTestCaseResult;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
@Controller
public class MetricsMasterController {
	private static final Log log = LogFactory.getLog(MetricsMasterController.class);
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@RequestMapping(value="metrics.master.summary.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsSummaryList(HttpServletRequest req) {
		log.info("metrics.master.summary.list");
		JTableResponse jTableResponse;		 
		try {			
			List<MetricsMaster> metricsList=productListService.listMetrics();
			List<JsonMetricsMaster> jsonMetricsMaster=new ArrayList<JsonMetricsMaster>();
			
			
			Integer week= DateUtility.getWeekOfYear();
			log.info("week number ===="+week);
			
			Date startDate = DateUtility.getDateForDayOfWeek(week,0);
			log.info("startDate="+startDate);
			Date endDate = DateUtility.getDateForDayOfWeek(week,6);
			log.info("endDate="+endDate);
			Date currentDate= DateUtility.getCurrentDate();
			log.info("currentDate="+currentDate);
			
			HashMap<Integer,JsonMetricsProgramExecutionProcess> mapOfTotalWptep = null;
			mapOfTotalWptep = workPackageService.getWorkpackageTestcaseExecutionPlanByDateFilter(1,startDate,currentDate);
			log.info("mapOfTotalWptep---size---"+mapOfTotalWptep.size());
			Integer totalWptepCount = 0;
			float percentageForExecutionProgress = 0;
			Integer testCaseCompletedCountForAllWp = 0;
			Integer totalWptepCountForAllWp = 0;
			Integer poorProgamExecutionIndexCount = 0;
				if(mapOfTotalWptep != null && mapOfTotalWptep.size()>0){
					Set<Integer> keySet = mapOfTotalWptep.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer workPackageIdKey : keySet) {
							JsonMetricsProgramExecutionProcess jsonProgramExecutionMetricsMaster = mapOfTotalWptep.get(workPackageIdKey);
							Integer notStartedCount = 0;
							Integer allocatedCount = 0;
							Integer testCaseCompletedCount = 0;
							Integer poorProgamExecutionPercentageForIndividual = 0;
							jsonProgramExecutionMetricsMaster.getWorkPackageId();
							if(jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount() != null){
								notStartedCount = jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount() != null){
								allocatedCount = jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount() != null){
								testCaseCompletedCount = jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount();
								testCaseCompletedCountForAllWp = testCaseCompletedCountForAllWp + testCaseCompletedCount;
							}
							
							totalWptepCount = notStartedCount+allocatedCount+testCaseCompletedCount;
							jsonProgramExecutionMetricsMaster.setTotalPlannedTestcaseCount(totalWptepCount);
							totalWptepCountForAllWp = totalWptepCountForAllWp+totalWptepCount;
							
							poorProgamExecutionPercentageForIndividual = (Math.round((float)(totalWptepCount-testCaseCompletedCount)/totalWptepCount*100));
							if(poorProgamExecutionPercentageForIndividual >4){
								poorProgamExecutionIndexCount++;
							}
							log.info("Wp Id: "+jsonProgramExecutionMetricsMaster.getWorkPackageId()+" Name: "+jsonProgramExecutionMetricsMaster.getWorkPackageName());
							log.info("notStartedCount "+notStartedCount+"  allocatedCount : "+allocatedCount+"  testCaseCompletedCount:  "+testCaseCompletedCount);
							
							log.info(" testCaseCompletedCountForAllWp>>>>>>>> "+testCaseCompletedCountForAllWp);
							log.info(" totalWptepCountForAllWp>>>>>>>> "+totalWptepCountForAllWp);
						}
					}
				}
			
			
			log.info(" testCaseCompletedCountForAllWp *************** "+testCaseCompletedCountForAllWp);
			log.info(" totalWptepCountForAllWp ************** "+totalWptepCountForAllWp);
			if(totalWptepCountForAllWp != 0){
				percentageForExecutionProgress=(Math.round((float)(totalWptepCountForAllWp-testCaseCompletedCountForAllWp)/totalWptepCountForAllWp*100));
			}else{
				percentageForExecutionProgress=0;
			}
			log.info("Percentage For Execution Progress *****==="+percentageForExecutionProgress);
			
			
			HashMap<Integer, JsonMetricsMasterProgramDefects> mapOfTotalpDefects = null;
			mapOfTotalpDefects = workPackageService.getTotalBugListByStatus(0);
			log.info("mapOfTotalpDefects---size---"+mapOfTotalpDefects.size());
			Integer totalBuglistCount = 0;
			float percentageForQuality = 0;
			Integer defectsClosedCountForAllp = 0;
			Integer defectsCountForAllp = 0;
			Integer poorProgamQualityIndexCount = 0;
			Integer	poorDefectQualityIndexCountForIndividual=0;
			
			Integer defectsDuplicateCountForDefectIndex = 0;
			Integer defectsNotReproducibleCountForDefectIndex = 0;
			Integer defectsReferBackCountForDefectIndex = 0;
			
				if(mapOfTotalpDefects != null && mapOfTotalpDefects.size()>0){
					Set<Integer> keySet = mapOfTotalpDefects.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer productIdKey : keySet) {
							JsonMetricsMasterProgramDefects jsonMetricsMasterProgramDefects = mapOfTotalpDefects.get(productIdKey);
							Integer newDefects = 0;
							Integer referBack = 0;
							Integer reviewed = 0;
							Integer approved = 0;
							Integer closed = 0;
							Integer duplicate = 0;
							Integer fixed = 0;
							Integer verified = 0;
							Integer intendedBehaviourCount = 0;
							Integer notReproducible = 0;
							
							Integer defectsClosedCount = 0;
							Integer defectsCountTotal = 0;
							Integer poorProgamQualityCountForIndividual = 0;
							float  poorDefectQualityCountForIndividual = 0;
							
							jsonMetricsMasterProgramDefects.getProductId();
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount() != null){
								newDefects =jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount() != null){
								referBack = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount();
								defectsReferBackCountForDefectIndex=defectsReferBackCountForDefectIndex+referBack;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount() != null){
								reviewed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount() != null){
								approved=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
								closed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount() != null){
								duplicate=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount();
								defectsDuplicateCountForDefectIndex=defectsDuplicateCountForDefectIndex+duplicate;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount() != null){
								fixed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() != null){
								verified=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() ;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() != null){
								intendedBehaviourCount=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() ;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() != null){
								notReproducible=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() ;
								defectsNotReproducibleCountForDefectIndex=defectsNotReproducibleCountForDefectIndex+notReproducible;
								
							}
							
							
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
								defectsClosedCount = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
								defectsClosedCountForAllp = defectsClosedCountForAllp + defectsClosedCount;
							}
							
							
							defectsCountTotal =newDefects+referBack+reviewed+approved+closed+duplicate+fixed+verified+intendedBehaviourCount+notReproducible;
							defectsCountForAllp = defectsCountForAllp+defectsCountTotal;
							if(defectsCountTotal!=0){
							poorProgamQualityCountForIndividual = (Math.round((defectsClosedCount * 100) / defectsCountTotal));
							}
							if(poorProgamQualityCountForIndividual < 90){
								poorProgamQualityIndexCount++;
							}
							
							Integer defectIndex=duplicate+notReproducible+referBack;
							float percentNumber=(float)defectIndex/defectsCountTotal;
							 poorDefectQualityCountForIndividual=Math.round((1-percentNumber)*100);
							if(poorDefectQualityCountForIndividual<90)
							{
								poorDefectQualityIndexCountForIndividual++;
							}
							
							log.info("poorProgamQualityCountForIndividual defects=="+poorProgamQualityIndexCount);
							
						}
					}
				}
			
			
			log.info(" defectsClosedCountForAllp *************** "+defectsClosedCountForAllp);
			log.info(" defectsCountForAllp ************** "+defectsCountForAllp);
			if(defectsCountForAllp != 0){
				percentageForQuality=(Math.round((defectsClosedCountForAllp * 100) / defectsCountForAllp));
			}else{
				percentageForQuality=0;
			}
			log.info("Percentage For Quality==="+percentageForQuality);
			
			
			Integer totalBugForQualityIndexCount=defectsDuplicateCountForDefectIndex+defectsNotReproducibleCountForDefectIndex+defectsReferBackCountForDefectIndex;
			float percentNumber=0;
			if(defectsCountForAllp!=0){
				percentNumber=(float)totalBugForQualityIndexCount/defectsCountForAllp;
			}
			float  percentageForQualityIndex=Math.round((1-percentNumber)*100);
			log.info("defectsCountForAllp==="+defectsCountForAllp);
			log.info("totalBugForQualityIndexCount==="+totalBugForQualityIndexCount);
			log.info("Percentage  QualityIndex==="+percentageForQualityIndex);
			
			

			
			HashMap<Integer,JsonMetricsTestCaseResult> mapOfTotalTcResult = null;
			mapOfTotalTcResult = workPackageService.getTestcaseExecutionResultList();
			log.info("mapOfTotalWptep---size---"+mapOfTotalTcResult.size());
			
			float percentageForProductConfidence = 0;
			Integer testCasePassedCountForAll = 0;
			Integer testCaseFailedCountForAll = 0;
			Integer testCaseBlockedCountForAll = 0;
			Integer testCaseNorunCountForAll = 0;
			Integer totalTcResultCountForAllWp = 0;
			Integer poorTcResultIndexCount = 0;
				if(mapOfTotalTcResult != null && mapOfTotalTcResult.size()>0){
					Set<Integer> keySet = mapOfTotalTcResult.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer productIdKey : keySet) {
							JsonMetricsTestCaseResult jsonMetricsTestCaseResult = mapOfTotalTcResult.get(productIdKey);
							Integer passedCount = 0;
							Integer failedCount = 0;
							Integer blockedCount = 0;
							Integer noRunCount = 0;
							Integer poorProgamExecutionPercentageForIndividual = 0;
							Integer totalTcResult=0;
							jsonMetricsTestCaseResult.getWorkPackageId();
							
							if(jsonMetricsTestCaseResult.getTestCasePassedCount() != null){
								passedCount = jsonMetricsTestCaseResult.getTestCasePassedCount();
								testCasePassedCountForAll=testCasePassedCountForAll+passedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseFailedCount() != null){
								failedCount = jsonMetricsTestCaseResult.getTestCaseFailedCount();
								testCaseFailedCountForAll=testCaseFailedCountForAll+failedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseBlockedCount() != null){
								blockedCount=jsonMetricsTestCaseResult.getTestCaseBlockedCount();
								testCaseBlockedCountForAll=testCaseBlockedCountForAll+blockedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseNorunCount() != null){
								noRunCount=jsonMetricsTestCaseResult.getTestCaseNorunCount();
								testCaseNorunCountForAll=testCaseNorunCountForAll+noRunCount;
								
							}
							
							
							totalTcResult = passedCount+failedCount+blockedCount+noRunCount;
							jsonMetricsTestCaseResult.setTotalTestCasePassedCount(totalTcResult);
							totalTcResultCountForAllWp = totalTcResultCountForAllWp+totalTcResult;
							
							poorProgamExecutionPercentageForIndividual = (Math.round((float)passedCount/totalTcResult*100));
							if(poorProgamExecutionPercentageForIndividual <90){
								poorTcResultIndexCount++;
							}
							
							log.info("passedCount "+passedCount+"  failedCount : "+failedCount+"  blockedCount:  "+blockedCount +" noRunCount "+noRunCount);
							
						}
					}
				}
				
			log.info("testCasePassedCountForAll "+testCasePassedCountForAll);
			log.info("testCaseFailedCountForAll "+testCaseFailedCountForAll);
			log.info("testCaseBlockedCountForAll "+testCaseBlockedCountForAll);
			log.info("testCaseNorunCountForAll "+testCaseNorunCountForAll);
			
			log.info(" totalTcResultCountForAllWp *************** "+totalTcResultCountForAllWp);
			log.info(" testCasePassedCountForAll ************** "+testCasePassedCountForAll);
			if(totalTcResultCountForAllWp != 0){
				percentageForProductConfidence=(Math.round((float)(testCasePassedCountForAll)/totalTcResultCountForAllWp*100));
			}else{
				percentageForProductConfidence=0;
			}
			log.info("Percentage For percentageForProductConfidence*****==="+percentageForProductConfidence);
		
			
			for(MetricsMaster metrics: metricsList){
				jsonMetricsMaster.add(new JsonMetricsMaster(metrics));
			}
			for(JsonMetricsMaster jsonmetrics: jsonMetricsMaster){
				if(jsonmetrics.getMetricsName().equals("Program Execution Progress Variance")){
					jsonmetrics.setActualValue(percentageForExecutionProgress+"%");
					jsonmetrics.setPoorProgramExecutionIndexCount(poorProgamExecutionIndexCount);
				}
				if(jsonmetrics.getMetricsName().equals("Program Quality Progress")){
					jsonmetrics.setActualValue(percentageForQuality+"%");
					jsonmetrics.setPoorProgramExecutionIndexCount(poorProgamQualityIndexCount);
				}
				if(jsonmetrics.getMetricsName().equals("Defect Quality Index")){
					jsonmetrics.setActualValue(percentageForQualityIndex+"%");
					jsonmetrics.setPoorProgramExecutionIndexCount(poorDefectQualityIndexCountForIndividual);
					
				}
				if(jsonmetrics.getMetricsName().equals("Product Confidence")){
					jsonmetrics.setActualValue(percentageForProductConfidence+"%");
					jsonmetrics.setPoorProgramExecutionIndexCount(poorTcResultIndexCount);
				}
			}

			jTableResponse = new JTableResponse("OK", jsonMetricsMaster,jsonMetricsMaster.size());     
          
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	
	@RequestMapping(value="metrics.master.varience.programExecutionProcess.popup",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsSummaryListVarience(HttpServletRequest req) {
		log.info("metrics.master.varience.programExecutionProcess.popup");
		JTableResponse jTableResponse;
		List<MetricsMaster> metricsList=productListService.listMetrics();
		List<JsonMetricsProgramExecutionProcess> listOfJsonProgramExecutionMetricsMasters=new ArrayList<JsonMetricsProgramExecutionProcess>();
		
		try {
			Integer week= DateUtility.getWeekOfYear();
			log.info("week number ===="+week);
			
			Date startDate = DateUtility.getDateForDayOfWeek(week,0);
			log.info("startDate="+startDate);
			Date endDate = DateUtility.getDateForDayOfWeek(week,6);
			log.info("endDate="+endDate);
			Date currentDate= DateUtility.getCurrentDate();
			log.info("currentDate="+currentDate);
			
			HashMap<Integer,JsonMetricsProgramExecutionProcess> mapOfTotalWptep = null;
			mapOfTotalWptep = workPackageService.getWorkpackageTestcaseExecutionPlanByDateFilter(1,startDate,currentDate);
			log.info("mapOfTotalWptep---size---"+mapOfTotalWptep.size());
			
			Integer totalWptepCount = 0;
			float percentageForExecutionProgress = 0;
			Integer testCaseCompletedCountForAllWp = 0;
			Integer totalWptepCountForAllWp = 0;
			Integer poorProgamExecutionIndexCount = 0;
			
				if(mapOfTotalWptep != null && mapOfTotalWptep.size()>0){
					Set<Integer> keySet = mapOfTotalWptep.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer workPackageIdKey : keySet) {
							JsonMetricsProgramExecutionProcess jsonProgramExecutionMetricsMaster = mapOfTotalWptep.get(workPackageIdKey);
							
							Integer notStartedCount = 0;
							Integer allocatedCount = 0;
							Integer testCaseCompletedCount = 0;
							Integer poorProgamExecutionPercentageForIndividual = 0;
							jsonProgramExecutionMetricsMaster.getWorkPackageId();
							if(jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount() != null){
								notStartedCount = jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount() != null){
								allocatedCount = jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount() != null){
								testCaseCompletedCount = jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount();
								testCaseCompletedCountForAllWp = testCaseCompletedCountForAllWp + testCaseCompletedCount;
							}
							
							totalWptepCount = notStartedCount+allocatedCount+testCaseCompletedCount;
							jsonProgramExecutionMetricsMaster.setTotalPlannedTestcaseCount(totalWptepCount);
							totalWptepCountForAllWp = totalWptepCountForAllWp+totalWptepCount;
							
							poorProgamExecutionPercentageForIndividual = (Math.round((float)(totalWptepCount-testCaseCompletedCount)/totalWptepCount*100));
							if(poorProgamExecutionPercentageForIndividual > 4){
								poorProgamExecutionIndexCount++;
							}
							
							Float completedTestCasePercentage=(float) (Math.round((float)testCaseCompletedCount/totalWptepCount*100));
							
							jsonProgramExecutionMetricsMaster.setWorkPackageName(jsonProgramExecutionMetricsMaster.getWorkPackageName());
							jsonProgramExecutionMetricsMaster.setTestCaseCompletedCount(testCaseCompletedCount);
							jsonProgramExecutionMetricsMaster.setTestCaseAllocatedCount(allocatedCount);
							jsonProgramExecutionMetricsMaster.setVariancePercentage(poorProgamExecutionPercentageForIndividual);
							jsonProgramExecutionMetricsMaster.setProductName(jsonProgramExecutionMetricsMaster.getProductName());
							if(completedTestCasePercentage!=0){
							jsonProgramExecutionMetricsMaster.setCompletedTestCasePercentage(completedTestCasePercentage);
							}else{
								jsonProgramExecutionMetricsMaster.setCompletedTestCasePercentage((float) 0);
							}
							listOfJsonProgramExecutionMetricsMasters.add(jsonProgramExecutionMetricsMaster);
							log.info("program Id: "+jsonProgramExecutionMetricsMaster.getProductId() +"  program Name: "+jsonProgramExecutionMetricsMaster.getProductName()+" Name: "+jsonProgramExecutionMetricsMaster.getWorkPackageName());
							log.info("notStartedCount "+notStartedCount+"  allocatedCount : "+allocatedCount+"  testCaseCompletedCount:  "+testCaseCompletedCount+"  completedTestCasePercentage:  "+completedTestCasePercentage);
							
							log.info(" testCaseCompletedCountForAllWp>>>>>>>> "+testCaseCompletedCountForAllWp);
							log.info(" totalWptepCountForAllWp>>>>>>>> "+totalWptepCountForAllWp);
						}
					}
				}

				jTableResponse = new JTableResponse("OK", listOfJsonProgramExecutionMetricsMasters,listOfJsonProgramExecutionMetricsMasters.size());     
			
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="metrics.master.program.defect.quality.popup",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsSummaryListProductQualitypopup(HttpServletRequest req) {
		log.info("metrics.master.program.defect.quality.popup");
		JTableResponse jTableResponse;
		List<MetricsMaster> metricsList=productListService.listMetrics();
		List<JsonMetricsMasterProgramDefects> listOfJsonMetricsMasterProgramDefects=new ArrayList<JsonMetricsMasterProgramDefects>();
		
		try {
			HashMap<Integer, JsonMetricsMasterProgramDefects> mapOfTotalpDefects = null;
			mapOfTotalpDefects = workPackageService.getTotalBugListByStatus(0);
			log.info("mapOfTotalpDefects---size---"+mapOfTotalpDefects.size());
			
			
			Integer totalBuglistCount = 0;
			float percentageForQuality = 0;
			Integer defectsClosedCountForAllp = 0;
			Integer defectsCountForAllp = 0;
			Integer poorProgamQualityIndexCount = 0;
				if(mapOfTotalpDefects != null && mapOfTotalpDefects.size()>0){
					Set<Integer> keySet = mapOfTotalpDefects.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer productIdKey : keySet) {
							JsonMetricsMasterProgramDefects jsonMetricsMasterProgramDefects = mapOfTotalpDefects.get(productIdKey);
							
							Integer newDefects = 0;
							Integer referBack = 0;
							Integer reviewed = 0;
							Integer approved = 0;
							Integer closed = 0;
							Integer duplicate = 0;
							Integer fixed = 0;
							Integer verified = 0;
							Integer intendedBehaviourCount = 0;
							Integer notReproducible = 0;
							
							Integer defectsClosedCount = 0;
							Integer defectsCountTotal = 0;
							Integer poorProgamQualityCountForIndividual = 0;
							Integer blockerCount=0;
							Integer normalCount=0;
							Integer closedPercentage=0;
							jsonMetricsMasterProgramDefects.getProductId();
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount() != null){
								newDefects =jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount() != null){
								referBack = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount() != null){
								reviewed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount() != null){
								approved=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
								closed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount() != null){
								duplicate=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount() != null){
								fixed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount();
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() != null){
								verified=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() ;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() != null){
								intendedBehaviourCount=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() ;
							}
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() != null){
								notReproducible=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() ;
							}						
							
							if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
								defectsClosedCount = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
								defectsClosedCountForAllp = defectsClosedCountForAllp + defectsClosedCount;
							}
							
							
							if(jsonMetricsMasterProgramDefects.getBlockerDefectsCount()!=null){
								blockerCount=jsonMetricsMasterProgramDefects.getBlockerDefectsCount();
							}
							if(jsonMetricsMasterProgramDefects.getNormalDefectsCount()!=null){
								normalCount=jsonMetricsMasterProgramDefects.getNormalDefectsCount();
							}
							
							defectsCountTotal =newDefects+referBack+reviewed+approved+closed+duplicate+fixed+verified+intendedBehaviourCount+notReproducible;
							defectsCountForAllp = defectsCountForAllp+defectsCountTotal;
							if(defectsCountTotal!=0){
							poorProgamQualityCountForIndividual = (Math.round((defectsClosedCount * 100) / defectsCountTotal));
							}
							if(poorProgamQualityCountForIndividual < 90){
								 poorProgamQualityIndexCount++;
							}
							if(defectsCountTotal!=0){
								closedPercentage=defectsClosedCount/defectsCountTotal*100;
							}
							jsonMetricsMasterProgramDefects.setProductName(jsonMetricsMasterProgramDefects.getProductName());
							jsonMetricsMasterProgramDefects.setVariance(poorProgamQualityCountForIndividual);
							jsonMetricsMasterProgramDefects.setTotalTestCaseDefectsCount(defectsCountTotal);
							jsonMetricsMasterProgramDefects.setBlockerDefectsCount(blockerCount);
							jsonMetricsMasterProgramDefects.setNormalDefectsCount(normalCount);
							if(closedPercentage!=0){
								jsonMetricsMasterProgramDefects.setClosedPercentage(closedPercentage);
							}
							listOfJsonMetricsMasterProgramDefects.add(jsonMetricsMasterProgramDefects);
							log.info("program Id: "+jsonMetricsMasterProgramDefects.getProductId() +"  program Name: "+jsonMetricsMasterProgramDefects.getProductName());
							log.info("newDefects "+newDefects+"  referBack : "+referBack+"  reviewed:  "+reviewed+"  closed:  "+closed +"  closedPercentage:  "+closedPercentage +"  poorProgamQualityCountForIndividual:  "+poorProgamQualityCountForIndividual);
							log.info("blockerCount "+blockerCount+"  normalCount : "+normalCount);
							
						}
					}
				}

				jTableResponse = new JTableResponse("OK", listOfJsonMetricsMasterProgramDefects,listOfJsonMetricsMasterProgramDefects.size());     
			
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="metrics.master.program.defect.quality.Index.popup",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsSummaryListDefectQualitypopup(HttpServletRequest req) {
		log.info("metrics.master.program.defect.quality.Index.popup");
		JTableResponse jTableResponse;
		List<MetricsMaster> metricsList=productListService.listMetrics();
		List<JsonMetricsMasterProgramDefects> listOfJsonMetricsMasterProgramDefects=new ArrayList<JsonMetricsMasterProgramDefects>();
		
		try {HashMap<Integer, JsonMetricsMasterProgramDefects> mapOfTotalpDefects = null;
		mapOfTotalpDefects = workPackageService.getTotalBugListByStatus(0);
		log.info("mapOfTotalpDefects---size---"+mapOfTotalpDefects.size());
		Integer totalBuglistCount = 0;
		float percentageForQuality = 0;
		Integer defectsClosedCountForAllp = 0;
		Integer defectsCountForAllp = 0;
		Integer poorProgamQualityIndexCount = 0;
		
		Integer defectsDuplicateCountForDefectIndex = 0;
		Integer defectsNotReproducibleCountForDefectIndex = 0;
		Integer defectsReferBackCountForDefectIndex = 0;
		
			if(mapOfTotalpDefects != null && mapOfTotalpDefects.size()>0){
				Set<Integer> keySet = mapOfTotalpDefects.keySet();
				if(keySet != null && keySet.size()>0){
					for (Integer productIdKey : keySet) {
						JsonMetricsMasterProgramDefects jsonMetricsMasterProgramDefects = mapOfTotalpDefects.get(productIdKey);
						Integer newDefects = 0;
						Integer referBack = 0;
						Integer reviewed = 0;
						Integer approved = 0;
						Integer closed = 0;
						Integer duplicate = 0;
						Integer fixed = 0;
						Integer verified = 0;
						Integer intendedBehaviourCount = 0;
						Integer notReproducible = 0;
						
						Integer defectsClosedCount = 0;
						Integer defectsCountTotal = 0;
						Integer poorProgamQualityCountForIndividual = 0;
						
						jsonMetricsMasterProgramDefects.getProductId();
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount() != null){
							newDefects =jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNewCount();
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount() != null){
							referBack = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreferbackCount();
							defectsReferBackCountForDefectIndex=defectsReferBackCountForDefectIndex+referBack;
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount() != null){
							reviewed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusreviewedCount();
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount() != null){
							approved=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusapprovedCount();
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
							closed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount() != null){
							duplicate=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusDuplicateCount();
							defectsDuplicateCountForDefectIndex=defectsDuplicateCountForDefectIndex+duplicate;
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount() != null){
							fixed=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusFixedCount();
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() != null){
							verified=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusVerifiedCount() ;
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() != null){
							intendedBehaviourCount=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusIntendedBehaviourCount() ;
						}
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() != null){
							notReproducible=jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusNotReproducibleCount() ;
							defectsNotReproducibleCountForDefectIndex=defectsNotReproducibleCountForDefectIndex+notReproducible;
							
						}
						
						
						if(jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount() != null){
							defectsClosedCount = jsonMetricsMasterProgramDefects.getDefectsBugfilingStatusClosedCount();
							defectsClosedCountForAllp = defectsClosedCountForAllp + defectsClosedCount;
						}
						
						log.info("newDefects: "+newDefects+ "  ---> referBack: "+referBack+"  ---> reviewed: "+reviewed+ "  ---> approved: "+approved+"  ---> closed: "+closed+"  ---> duplicate: "+duplicate);
						log.info("  ---> fixed: "+fixed+ "  ---> verified: "+verified+"  ---> intendedBehaviourCount: "+intendedBehaviourCount+"  ---> notReproducible: "+notReproducible);
						defectsCountTotal =newDefects+referBack+reviewed+approved+closed+duplicate+fixed+verified+intendedBehaviourCount+notReproducible;
						defectsCountForAllp = defectsCountForAllp+defectsCountTotal;
						if(defectsCountTotal!=0){
						poorProgamQualityCountForIndividual = (Math.round((defectsClosedCount * 100) / defectsCountTotal));
						}
						if(poorProgamQualityCountForIndividual < 90){
							poorProgamQualityIndexCount++;
						}
						
						Integer totalBugForQualityIndexCount=duplicate+notReproducible+referBack;
						float percentNumber=(float)totalBugForQualityIndexCount/defectsCountTotal;
						float  percentageForQualityIndex=Math.round((1-percentNumber)*100);
						if(defectsCountTotal == 0){
							percentageForQualityIndex = 100;
						}
						
						
						jsonMetricsMasterProgramDefects.setProductName(jsonMetricsMasterProgramDefects.getProductName());
						jsonMetricsMasterProgramDefects.setDefectsQualityPercentageSpecific(percentageForQualityIndex);
						jsonMetricsMasterProgramDefects.setTotalTestCaseDefectsCount(defectsCountTotal);
						jsonMetricsMasterProgramDefects.setLowQualityDefetcsPercentage(100-percentageForQualityIndex);
						
						listOfJsonMetricsMasterProgramDefects.add(jsonMetricsMasterProgramDefects);
						log.info("poorProgamQualityCountForIndividual defects=="+poorProgamQualityIndexCount);
						
					}
				}
			}
		
		
		log.info(" defectsClosedCountForAllp *************** "+defectsClosedCountForAllp);
		log.info(" defectsCountForAllp ************** "+defectsCountForAllp);
		if(defectsCountForAllp != 0){
			percentageForQuality=(Math.round((defectsClosedCountForAllp * 100) / defectsCountForAllp));
		}else{
			percentageForQuality=0;
		}
		log.info("Percentage For Quality==="+percentageForQuality);
		
				jTableResponse = new JTableResponse("OK", listOfJsonMetricsMasterProgramDefects,listOfJsonMetricsMasterProgramDefects.size());     
			
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="metrics.master.product.Confidence.popup",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsSummaryproductConfidencePopUp(HttpServletRequest req) {
		log.info("metrics.master.Product.Confidence.popup");
		JTableResponse jTableResponse;
		List<JsonMetricsTestCaseResult> listOfJsonMetricsTestCaseResult=new ArrayList<JsonMetricsTestCaseResult>();
		
		try {
			HashMap<Integer,JsonMetricsTestCaseResult> mapOfTotalTcResult = null;
			mapOfTotalTcResult = workPackageService.getTestcaseExecutionResultList();
			log.info("mapOfTotalWptep---size---"+mapOfTotalTcResult.size());
			
			float percentageForProductConfidence = 0;
			Integer testCasePassedCountForAll = 0;
			Integer testCaseFailedCountForAll = 0;
			Integer testCaseBlockedCountForAll = 0;
			Integer testCaseNorunCountForAll = 0;
			Integer totalTcResultCountForAllWp = 0;
			Integer poorTcResultIndexCount = 0;
				if(mapOfTotalTcResult != null && mapOfTotalTcResult.size()>0){
					Set<Integer> keySet = mapOfTotalTcResult.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer productIdKey : keySet) {
							JsonMetricsTestCaseResult jsonMetricsTestCaseResult = mapOfTotalTcResult.get(productIdKey);
							Integer passedCount = 0;
							Integer failedCount = 0;
							Integer blockedCount = 0;
							Integer noRunCount = 0;
							Float productConfidenceForIndividual = (float) 0;
							Integer totalTcResult=0;
							jsonMetricsTestCaseResult.getWorkPackageId();
							
							if(jsonMetricsTestCaseResult.getTestCasePassedCount() != null){
								passedCount = jsonMetricsTestCaseResult.getTestCasePassedCount();
								testCasePassedCountForAll=testCasePassedCountForAll+passedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseFailedCount() != null){
								failedCount = jsonMetricsTestCaseResult.getTestCaseFailedCount();
								testCaseFailedCountForAll=testCaseFailedCountForAll+failedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseBlockedCount() != null){
								blockedCount=jsonMetricsTestCaseResult.getTestCaseBlockedCount();
								testCaseBlockedCountForAll=testCaseBlockedCountForAll+blockedCount;
							}
							if(jsonMetricsTestCaseResult.getTestCaseNorunCount() != null){
								noRunCount=jsonMetricsTestCaseResult.getTestCaseNorunCount();
								testCaseNorunCountForAll=testCaseNorunCountForAll+noRunCount;
								
							}
							
							
							totalTcResult = passedCount+failedCount+blockedCount+noRunCount;
							jsonMetricsTestCaseResult.setTotalTestCasePassedCount(totalTcResult);
							totalTcResultCountForAllWp = totalTcResultCountForAllWp+totalTcResult;
							
							productConfidenceForIndividual = (float) (Math.round((float)passedCount/totalTcResult*100));
							if(productConfidenceForIndividual <90){
								poorTcResultIndexCount++;
							}
							
							jsonMetricsTestCaseResult.setTestCasePassedCount(passedCount);
							jsonMetricsTestCaseResult.setTestCaseFailedCount(failedCount);
							jsonMetricsTestCaseResult.setTestCaseBlockedCount(blockedCount);
							jsonMetricsTestCaseResult.setTestCaseNorunCount(noRunCount);
							jsonMetricsTestCaseResult.setProductConfidenceQuality(productConfidenceForIndividual);
							listOfJsonMetricsTestCaseResult.add(jsonMetricsTestCaseResult);
							log.info("Percentage For percentageForProductConfidence*****==="+productConfidenceForIndividual);
							
							log.info("passedCount "+passedCount+"  failedCount : "+failedCount+"  blockedCount:  "+blockedCount +" noRunCount "+noRunCount);
							
						}
					}
				}
			
			
			jTableResponse = new JTableResponse("OK", listOfJsonMetricsTestCaseResult,listOfJsonMetricsTestCaseResult.size());     
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
            log.error("JSON ERROR", e);
	    }
        return jTableResponse;
    }
	
	@RequestMapping(value="metrics.master.varience.resource.performance.popup",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse metricsResourcePerformanceSummary(HttpServletRequest req) {
		log.info("metrics.master.summary.list");
		JTableResponse jTableResponse;
		List<JsonMetricsProgramExecutionProcess> listOfJsonResourcePerformanceMetrics =new ArrayList<JsonMetricsProgramExecutionProcess>();
		try {
			Integer week= DateUtility.getWeekOfYear();
			log.info("week number ===="+week);
			
			Date startDate = DateUtility.getDateForDayOfWeek(week,0);
			log.info("startDate="+startDate);
			Date endDate = DateUtility.getDateForDayOfWeek(week,6);
			log.info("endDate="+endDate);
			Date currentDate= DateUtility.getCurrentDate();
			log.info("currentDate="+currentDate);
			
			HashMap<Integer,JsonMetricsProgramExecutionProcess> mapOfTotalWptep = null;
			mapOfTotalWptep = workPackageService.getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(0,startDate,currentDate);
			log.info("mapOfTotalWptep---size---"+mapOfTotalWptep.size());
			
			Integer totalWptepCountForResource = 0;
			Integer testCaseCompletedCountForAllWp = 0;
			Integer totalWptepCountForAllWp = 0;
			Integer goodResourcePerformanceIndexCount = 0;
			
				if(mapOfTotalWptep != null && mapOfTotalWptep.size()>0){
					Set<Integer> keySet = mapOfTotalWptep.keySet();
					if(keySet != null && keySet.size()>0){
						for (Integer userIdKey : keySet) {
							JsonMetricsProgramExecutionProcess jsonProgramExecutionMetricsMaster = mapOfTotalWptep.get(userIdKey);
							
							Integer notStartedCount = 0;
							Integer allocatedCount = 0;
							Integer testCaseCompletedCount = 0;
							Integer goodResourcePerformancePercentageForIndividual = 0;
							jsonProgramExecutionMetricsMaster.getUserId();
							if(jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount() != null){
								notStartedCount = jsonProgramExecutionMetricsMaster.getTestCaseNotStartedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount() != null){
								allocatedCount = jsonProgramExecutionMetricsMaster.getTestCaseAllocatedCount();
							}
							if(jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount() != null){
								testCaseCompletedCount = jsonProgramExecutionMetricsMaster.getTestCaseCompletedCount();
								testCaseCompletedCountForAllWp = testCaseCompletedCountForAllWp + testCaseCompletedCount;
							}
							
							totalWptepCountForResource = jsonProgramExecutionMetricsMaster.getTotalPlannedTestcaseCount();
							jsonProgramExecutionMetricsMaster.setTotalPlannedTestcaseCount(jsonProgramExecutionMetricsMaster.getTotalPlannedTestcaseCount());
							
							goodResourcePerformancePercentageForIndividual = (Math.round((float)(testCaseCompletedCount-totalWptepCountForResource)/totalWptepCountForResource*100));
							
							log.info("USerId : "+jsonProgramExecutionMetricsMaster.getUserId()+ "  ---> notStartedCount: "+notStartedCount);
							log.info("  ---> allocatedCount: "+allocatedCount+ "  ---> testCaseCompletedCount: "+testCaseCompletedCount);
							
							Float completedTestCasePercentage=(float) (Math.round((float)testCaseCompletedCount/totalWptepCountForResource*100));
							jsonProgramExecutionMetricsMaster.setUserId(jsonProgramExecutionMetricsMaster.getUserId());
							jsonProgramExecutionMetricsMaster.setUserName(jsonProgramExecutionMetricsMaster.getUserName());
							jsonProgramExecutionMetricsMaster.setTestCaseCompletedCount(testCaseCompletedCount);
							jsonProgramExecutionMetricsMaster.setTestCaseAllocatedCount(allocatedCount);
							jsonProgramExecutionMetricsMaster.setVariancePercentage(goodResourcePerformancePercentageForIndividual);
							jsonProgramExecutionMetricsMaster.setProductName(jsonProgramExecutionMetricsMaster.getProductName());
							if(completedTestCasePercentage!=0){
							jsonProgramExecutionMetricsMaster.setCompletedTestCasePercentage(completedTestCasePercentage);
							}else{
								jsonProgramExecutionMetricsMaster.setCompletedTestCasePercentage((float) 0);
							}
							listOfJsonResourcePerformanceMetrics.add(jsonProgramExecutionMetricsMaster);
							log.info("program Id: "+jsonProgramExecutionMetricsMaster.getProductId() +"  program Name: "+jsonProgramExecutionMetricsMaster.getProductName()+" Name: "+jsonProgramExecutionMetricsMaster.getWorkPackageName());
							log.info("notStartedCount "+notStartedCount+"  allocatedCount : "+allocatedCount+"  testCaseCompletedCount:  "+testCaseCompletedCount+"  completedTestCasePercentage:  "+completedTestCasePercentage);
							
							log.info(" testCaseCompletedCountForAllWp>>>>>>>> "+testCaseCompletedCountForAllWp);
							log.info(" totalWptepCountForAllWp>>>>>>>> "+totalWptepCountForAllWp);
						}
					}
				}
				jTableResponse = new JTableResponse("OK", listOfJsonResourcePerformanceMetrics,listOfJsonResourcePerformanceMetrics.size());     
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show MetricsList!");
	            log.error("JSON ERROR", e);
	   }
        return jTableResponse;
    }
	
	
	
	
}
