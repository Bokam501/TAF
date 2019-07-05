package com.hcl.atf.taf.schedule.data.extractor;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DPAWorkbookCollection;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UtilizationCollection;

@Service
public class CollectionValuesScheduleMapper {

	public void setActivityCollectionValues(ActivityCollection activityCollection, LinkedHashMap<String, String> excelDataTemplate, String headerCellValue, String cellValue, Cell contentCell){
		if(excelDataTemplate.containsKey("programName") && excelDataTemplate.get("programName").equalsIgnoreCase(headerCellValue)){
			activityCollection.setProductName(cellValue);
		}else if(excelDataTemplate.containsKey("programManager") && excelDataTemplate.get("programManager").equalsIgnoreCase(headerCellValue)){
			activityCollection.setProductManager(cellValue);
		}else if(excelDataTemplate.containsKey("project") && excelDataTemplate.get("project").equalsIgnoreCase(headerCellValue)){
			activityCollection.setProject(cellValue);
		}else if(excelDataTemplate.containsKey("projectManager") && excelDataTemplate.get("projectManager").equalsIgnoreCase(headerCellValue)){
			activityCollection.setProjectManager(cellValue);
		}else if(excelDataTemplate.containsKey("competency") && excelDataTemplate.get("competency").equalsIgnoreCase(headerCellValue)){
			activityCollection.setCompetency(cellValue);
		}else if(excelDataTemplate.containsKey("competencyManager") && excelDataTemplate.get("competencyManager").equalsIgnoreCase(headerCellValue)){
			activityCollection.setCompetencyManager(cellValue);
		}else if(excelDataTemplate.containsKey("versionName") && excelDataTemplate.get("versionName").equalsIgnoreCase(headerCellValue)){
			activityCollection.setVersionName(cellValue);
		}else if(excelDataTemplate.containsKey("buildName") && excelDataTemplate.get("buildName").equalsIgnoreCase(headerCellValue)){
			activityCollection.setBuildName(cellValue);
		}else if(excelDataTemplate.containsKey("weekDate") && excelDataTemplate.get("weekDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setWeekDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("createdDate") && excelDataTemplate.get("createdDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setCreatedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("updateDate") && excelDataTemplate.get("updateDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setUpdateDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("phase") && excelDataTemplate.get("phase").equalsIgnoreCase(headerCellValue)){
			activityCollection.setPhase(cellValue);
		}else if(excelDataTemplate.containsKey("workpackageName") && excelDataTemplate.get("workpackageName").equalsIgnoreCase(headerCellValue)){
			activityCollection.setWorkpackageName(cellValue);
		}else if(excelDataTemplate.containsKey("activityBatchNo") && excelDataTemplate.get("activityBatchNo").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityBatchNo(cellValue);
		}else if(excelDataTemplate.containsKey("activityType") && excelDataTemplate.get("activityType").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityType(cellValue);
		}else if(excelDataTemplate.containsKey("activityCategory") && excelDataTemplate.get("activityCategory").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityCategory(cellValue);
		}else if(excelDataTemplate.containsKey("activityName") && excelDataTemplate.get("activityName").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityName(cellValue);
		}else if(excelDataTemplate.containsKey("activitySizeActual") && excelDataTemplate.get("activitySizeActual").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivitySizeActual(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activitySizePlanned") && excelDataTemplate.get("activitySizePlanned").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivitySizePlanned(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReference") && excelDataTemplate.get("activityReference").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReference(cellValue);
		}else if(excelDataTemplate.containsKey("activityTracker") && excelDataTemplate.get("activityTracker").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityTracker(cellValue);
		}else if(excelDataTemplate.containsKey("activityEnvironmentPrimary") && excelDataTemplate.get("activityEnvironmentPrimary").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityEnvironmentPrimary(cellValue);
		}else if(excelDataTemplate.containsKey("activityEnvironmentSecondary") && excelDataTemplate.get("activityEnvironmentSecondary").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityEnvironmentSecondary(cellValue);
		}else if(excelDataTemplate.containsKey("activityEnvironmentTertiary") && excelDataTemplate.get("activityEnvironmentTertiary").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityEnvironmentTertiary(cellValue);
		}else if(excelDataTemplate.containsKey("weightageType") && excelDataTemplate.get("weightageType").equalsIgnoreCase(headerCellValue)){
			activityCollection.setWeightageType(cellValue);
		}else if(excelDataTemplate.containsKey("weightageUnit") && excelDataTemplate.get("weightageUnit").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setWeightageUnit(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("workUnitPlanned") && excelDataTemplate.get("workUnitPlanned").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setWorkUnitPlanned(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("workUnitActual") && excelDataTemplate.get("workUnitActual").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setWorkUnitActual(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activitySeverity") && excelDataTemplate.get("activitySeverity").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivitySeverity(cellValue);
		}else if(excelDataTemplate.containsKey("activityResolution") && excelDataTemplate.get("activityResolution").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityResolution(cellValue);
		}else if(excelDataTemplate.containsKey("activityStatus") && excelDataTemplate.get("activityStatus").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityStatus(cellValue);
		}else if(excelDataTemplate.containsKey("activitySource") && excelDataTemplate.get("activitySource").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivitySource(cellValue);
		}else if(excelDataTemplate.containsKey("activityComponent") && excelDataTemplate.get("activityComponent").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityComponent(cellValue);
		}else if(excelDataTemplate.containsKey("activityStage") && excelDataTemplate.get("activityStage").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityStage(cellValue);
		}else if(excelDataTemplate.containsKey("actualActivityStartDate") && excelDataTemplate.get("actualActivityStartDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActualActivityStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("actualActivityEndDate") && excelDataTemplate.get("actualActivityEndDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActualActivityEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("plannedActivityStartDate") && excelDataTemplate.get("plannedActivityStartDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setPlannedActivityStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("plannedActivityEndDate") && excelDataTemplate.get("plannedActivityEndDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setPlannedActivityEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("revisedActivityStartDate") && excelDataTemplate.get("revisedActivityStartDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setRevisedActivityStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("revisedActivityEndDate") && excelDataTemplate.get("revisedActivityEndDate").equalsIgnoreCase(headerCellValue)){
			activityCollection.setRevisedActivityEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("actualActivityEffort") && excelDataTemplate.get("actualActivityEffort").equalsIgnoreCase(headerCellValue)){
			cellValue = cellValue.replaceAll(",", "");
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActualActivityEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("plannedActivityEffort") && excelDataTemplate.get("plannedActivityEffort").equalsIgnoreCase(headerCellValue)){
			cellValue = cellValue.replaceAll(",", "");
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setPlannedActivityEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("revisedActivityEffort") && excelDataTemplate.get("revisedActivityEffort").equalsIgnoreCase(headerCellValue)){
			cellValue = cellValue.replaceAll(",", "");
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setRevisedActivityEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityTag") && excelDataTemplate.get("activityTag").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityTag(cellValue);
		}else if(excelDataTemplate.containsKey("cumulativeActivityActual") && excelDataTemplate.get("cumulativeActivityActual").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setCumulativeActivityActual(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("cumulativeActivityPlanned") && excelDataTemplate.get("cumulativeActivityPlanned").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setCumulativeActivityPlanned(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityRaisedBy") && excelDataTemplate.get("activityRaisedBy").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityRaisedBy(cellValue);
		}else if(excelDataTemplate.containsKey("activityOwner") && excelDataTemplate.get("activityOwner").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityOwner(cellValue);
		}else if(excelDataTemplate.containsKey("activityAssignedTo") && excelDataTemplate.get("activityAssignedTo").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityAssignedTo(cellValue);
		}else if(excelDataTemplate.containsKey("activityReviewer1") && excelDataTemplate.get("activityReviewer1").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReviewer1(cellValue);				
		}else if(excelDataTemplate.containsKey("activityReviewer2") && excelDataTemplate.get("activityReviewer2").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReviewer2(cellValue);
		}else if(excelDataTemplate.containsKey("activityReviewer3") && excelDataTemplate.get("activityReviewer3").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReviewer3(cellValue);
		}else if(excelDataTemplate.containsKey("activityReviewer4") && excelDataTemplate.get("activityReviewer4").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReviewer4(cellValue);
		}else if(excelDataTemplate.containsKey("activityReviewer5") && excelDataTemplate.get("activityReviewer6").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityReviewer5(cellValue);
		}else if(excelDataTemplate.containsKey("activityParent") && excelDataTemplate.get("activityParent").equalsIgnoreCase(headerCellValue)){
			activityCollection.setActivityParent(cellValue);
		}else if(excelDataTemplate.containsKey("activityExecutionEffort") && excelDataTemplate.get("activityExecutionEffort").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityExecutionEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReviewEffort1") && excelDataTemplate.get("activityReviewEffort1").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReviewEffort1(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReviewEffort2") && excelDataTemplate.get("activityReviewEffort2").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReviewEffort2(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReviewEffort3") && excelDataTemplate.get("activityReviewEffort3").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReviewEffort3(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReviewEffort4") && excelDataTemplate.get("activityReviewEffort4").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReviewEffort4(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReviewEffort5") && excelDataTemplate.get("activityReviewEffort5").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReviewEffort5(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("activityReworkEffort") && excelDataTemplate.get("activityReworkEffort").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setActivityReworkEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l0ResourceCount") && excelDataTemplate.get("l0ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL0ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l1ResourceCount") && excelDataTemplate.get("l1ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL1ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l2ResourceCount") && excelDataTemplate.get("l2ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL2ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l3ResourceCount") && excelDataTemplate.get("l3ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL3ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l4ResourceCount") && excelDataTemplate.get("l4ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL4ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l5ResourceCount") && excelDataTemplate.get("l5ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL5ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l6ResourceCount") && excelDataTemplate.get("l6ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL6ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("l7ResourceCount") && excelDataTemplate.get("l7ResourceCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setL7ResourceCount(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("remarks") && excelDataTemplate.get("remarks").equalsIgnoreCase(headerCellValue)){
			activityCollection.setRemarks(cellValue);
		}else if(excelDataTemplate.containsKey("eVa") && excelDataTemplate.get("eVa").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.seteVa(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("eVb") && excelDataTemplate.get("eVb").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.seteVb(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("sVa") && excelDataTemplate.get("sVa").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setsVa(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("sVb") && excelDataTemplate.get("sVb").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				activityCollection.setsVb(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("type") && excelDataTemplate.get("type").equalsIgnoreCase(headerCellValue)){
			activityCollection.setType(cellValue);
		}
	}

	
	public void setDefectCollectionValues(DefectCollection defectCollection, LinkedHashMap<String, String> excelDataTemplate, String headerCellValue, String cellValue, Cell contentCell){
		if(excelDataTemplate.containsKey("defectId") && excelDataTemplate.get("defectId").equalsIgnoreCase(headerCellValue)){
			defectCollection.setDefectId(cellValue);
		}else if(excelDataTemplate.containsKey("title") && excelDataTemplate.get("title").equalsIgnoreCase(headerCellValue)){
			defectCollection.setTitle(cellValue);
		}else if(excelDataTemplate.containsKey("programName") && excelDataTemplate.get("programName").equalsIgnoreCase(headerCellValue)){
			defectCollection.setProductName(cellValue);
		}else if(excelDataTemplate.containsKey("programManager") && excelDataTemplate.get("programManager").equalsIgnoreCase(headerCellValue)){
			defectCollection.setProductManager(cellValue);
		}else if(excelDataTemplate.containsKey("project") && excelDataTemplate.get("project").equalsIgnoreCase(headerCellValue)){
			defectCollection.setProject(cellValue);
		}else if(excelDataTemplate.containsKey("projectManager") && excelDataTemplate.get("projectManager").equalsIgnoreCase(headerCellValue)){
			defectCollection.setProjectManager(cellValue);
		}else if(excelDataTemplate.containsKey("competency") && excelDataTemplate.get("competency").equalsIgnoreCase(headerCellValue)){
			defectCollection.setCompetency(cellValue);
		}else if(excelDataTemplate.containsKey("competencyManager") && excelDataTemplate.get("competencyManager").equalsIgnoreCase(headerCellValue)){
			defectCollection.setCompetencyManager(cellValue);
		}else if(excelDataTemplate.containsKey("versionName") && excelDataTemplate.get("versionName").equalsIgnoreCase(headerCellValue)){
			defectCollection.setVersionName(cellValue);
		}else if(excelDataTemplate.containsKey("buildName") && excelDataTemplate.get("buildName").equalsIgnoreCase(headerCellValue)){
			defectCollection.setBuildName(cellValue);
		}else if(excelDataTemplate.containsKey("workpackageName") && excelDataTemplate.get("workpackageName").equalsIgnoreCase(headerCellValue)){
			defectCollection.setWorkpackageName(cellValue);
		}else if(excelDataTemplate.containsKey("description") && excelDataTemplate.get("description").equalsIgnoreCase(headerCellValue)){
			defectCollection.setDescription(cellValue);
		}else if(excelDataTemplate.containsKey("release") && excelDataTemplate.get("release").equalsIgnoreCase(headerCellValue)){
			defectCollection.setRelease(cellValue);
		}else if(excelDataTemplate.containsKey("requestType") && excelDataTemplate.get("requestType").equalsIgnoreCase(headerCellValue)){
			defectCollection.setRequestType(cellValue);
		}else if(excelDataTemplate.containsKey("priority") && excelDataTemplate.get("priority").equalsIgnoreCase(headerCellValue)){
			defectCollection.setPriority(cellValue);
		}else if(excelDataTemplate.containsKey("resolution") && excelDataTemplate.get("resolution").equalsIgnoreCase(headerCellValue)){
			defectCollection.setResolution(cellValue);
		}else if(excelDataTemplate.containsKey("category") && excelDataTemplate.get("category").equalsIgnoreCase(headerCellValue)){
			defectCollection.setCategory(cellValue);
		}else if(excelDataTemplate.containsKey("severity") && excelDataTemplate.get("severity").equalsIgnoreCase(headerCellValue)){
			defectCollection.setSeverity(cellValue);
		}else if(excelDataTemplate.containsKey("detection") && excelDataTemplate.get("detection").equalsIgnoreCase(headerCellValue)){
			defectCollection.setDetection(cellValue);
		}else if(excelDataTemplate.containsKey("injection") && excelDataTemplate.get("injection").equalsIgnoreCase(headerCellValue)){
			defectCollection.setInjection(cellValue);
		}else if(excelDataTemplate.containsKey("internalDefect") && excelDataTemplate.get("internalDefect").equalsIgnoreCase(headerCellValue)){
			defectCollection.setInternalDefect(cellValue);
		}else if(excelDataTemplate.containsKey("feature") && excelDataTemplate.get("feature").equalsIgnoreCase(headerCellValue)){
			defectCollection.setFeature(cellValue);
		}else if(excelDataTemplate.containsKey("raisedDate") && excelDataTemplate.get("raisedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setRaisedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("assignedDate") && excelDataTemplate.get("assignedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setAssignedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("kbid") && excelDataTemplate.get("kbid").equalsIgnoreCase(headerCellValue)){
			defectCollection.setKbid(cellValue);
		}else if(excelDataTemplate.containsKey("status") && excelDataTemplate.get("status").equalsIgnoreCase(headerCellValue)){
			defectCollection.setStatus(cellValue);
		}else if(excelDataTemplate.containsKey("createdDate") && excelDataTemplate.get("createdDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setCreatedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("updatedDate") && excelDataTemplate.get("updatedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setUpdatedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("expectedEffort") && excelDataTemplate.get("expectedEffort").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				defectCollection.setExpectedEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("plannedClosureDate") && excelDataTemplate.get("plannedClosureDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setPlannedClosureDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("closureDate") && excelDataTemplate.get("closureDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setClosedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("updatedBy") && excelDataTemplate.get("updatedBy").equalsIgnoreCase(headerCellValue)){
			defectCollection.setUpdatedBy(cellValue);
		}else if(excelDataTemplate.containsKey("raisedBy") && excelDataTemplate.get("raisedBy").equalsIgnoreCase(headerCellValue)){
			defectCollection.setRaisedBy(cellValue);
		}else if(excelDataTemplate.containsKey("owner") && excelDataTemplate.get("owner").equalsIgnoreCase(headerCellValue)){
			defectCollection.setOwner(cellValue);
		}else if(excelDataTemplate.containsKey("assignedTo") && excelDataTemplate.get("assignedTo").equalsIgnoreCase(headerCellValue)){
			defectCollection.setAssignedTo(cellValue);
		}else if(excelDataTemplate.containsKey("lastUpdatedDate") && excelDataTemplate.get("lastUpdatedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setLastUpdatedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("file") && excelDataTemplate.get("file").equalsIgnoreCase(headerCellValue)){
			defectCollection.setFile(cellValue);
		}else if(excelDataTemplate.containsKey("primaryFeature") && excelDataTemplate.get("primaryFeature").equalsIgnoreCase(headerCellValue)){
			defectCollection.setPrimaryFeature(cellValue);
		}else if(excelDataTemplate.containsKey("primaryFeatureParent") && excelDataTemplate.get("primaryFeatureParent").equalsIgnoreCase(headerCellValue)){
			defectCollection.setPrimaryFeatureParent(cellValue);
		}else if(excelDataTemplate.containsKey("secondaryFeature") && excelDataTemplate.get("secondaryFeature").equalsIgnoreCase(headerCellValue)){
			defectCollection.setSecondaryFeature(cellValue);
		}else if(excelDataTemplate.containsKey("mapped") && excelDataTemplate.get("mapped").equalsIgnoreCase(headerCellValue)){
			defectCollection.setMapped(cellValue);
		}else if(excelDataTemplate.containsKey("testCases") && excelDataTemplate.get("testCases").equalsIgnoreCase(headerCellValue)){
			defectCollection.setTestCases(cellValue);
		}else if(excelDataTemplate.containsKey("confirmed") && excelDataTemplate.get("confirmed").equalsIgnoreCase(headerCellValue)){
			defectCollection.setConfirmed(cellValue);
		}else if(excelDataTemplate.containsKey("testExecutionResultBugId") && excelDataTemplate.get("testExecutionResultBugId").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				defectCollection.setTestExecutionResultBugId(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("testCaseExecutionResult") && excelDataTemplate.get("testCaseExecutionResult").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				defectCollection.setTestCaseExecutionResult(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("environment") && excelDataTemplate.get("environment").equalsIgnoreCase(headerCellValue)){
			defectCollection.setEnvironment(cellValue);
		}else if(excelDataTemplate.containsKey("closedDate") && excelDataTemplate.get("closedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setClosedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("resolvedDate") && excelDataTemplate.get("resolvedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setResolvedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("verifiedDate") && excelDataTemplate.get("verifiedDate").equalsIgnoreCase(headerCellValue)){
			defectCollection.setVerifiedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("parentState") && excelDataTemplate.get("parentState").equalsIgnoreCase(headerCellValue)){
			defectCollection.setParentState(cellValue);
		}else if(excelDataTemplate.containsKey("driverState") && excelDataTemplate.get("driverState").equalsIgnoreCase(headerCellValue)){
			defectCollection.setDriverState(cellValue);
		}else if(excelDataTemplate.containsKey("specificationState") && excelDataTemplate.get("specificationState").equalsIgnoreCase(headerCellValue)){
			defectCollection.setSpecificationState(cellValue);
		}else if(excelDataTemplate.containsKey("closedStatus") && excelDataTemplate.get("closedStatus").equalsIgnoreCase(headerCellValue)){
			defectCollection.setClosedStatus(cellValue);
		}else if(excelDataTemplate.containsKey("resolvedInfo") && excelDataTemplate.get("resolvedInfo").equalsIgnoreCase(headerCellValue)){
			defectCollection.setResolvedInfo(cellValue);
		}else if(excelDataTemplate.containsKey("remarks") && excelDataTemplate.get("remarks").equalsIgnoreCase(headerCellValue)){
			defectCollection.setRemarks(cellValue);
		}else if(excelDataTemplate.containsKey("type") && excelDataTemplate.get("type").equalsIgnoreCase(headerCellValue)){
			defectCollection.setType(cellValue);
		}else if(excelDataTemplate.containsKey("activityName") && excelDataTemplate.get("activityName").equalsIgnoreCase(headerCellValue)){
			defectCollection.setActivityName(cellValue);
		}else if(excelDataTemplate.containsKey("activityComponent") && excelDataTemplate.get("activityComponent").equalsIgnoreCase(headerCellValue)){
			defectCollection.setActivityComponent(cellValue);
		}
	}
	
	public void setReviewRecordCollectionValues(ReviewRecordCollection reviewRecordCollection, LinkedHashMap<String, String> excelDataTemplate, String headerCellValue, String cellValue, Cell contentCell){
		if(excelDataTemplate.containsKey("programName") && excelDataTemplate.get("programName").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setProductName(cellValue);
		}else if(excelDataTemplate.containsKey("programManager") && excelDataTemplate.get("programManager").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setProductManager(cellValue);
		}else if(excelDataTemplate.containsKey("project") && excelDataTemplate.get("project").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setProject(cellValue);
		}else if(excelDataTemplate.containsKey("projectManager") && excelDataTemplate.get("projectManager").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setProjectManager(cellValue);
		}else if(excelDataTemplate.containsKey("competency") && excelDataTemplate.get("competency").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setCompetency(cellValue);
		}else if(excelDataTemplate.containsKey("competencyManager") && excelDataTemplate.get("competencyManager").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setCompetencyManager(cellValue);
		}else if(excelDataTemplate.containsKey("versionName") && excelDataTemplate.get("versionName").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setVersionName(cellValue);
		}else if(excelDataTemplate.containsKey("buildName") && excelDataTemplate.get("buildName").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setBuildName(cellValue);
		}else if(excelDataTemplate.containsKey("reviewRecordType") && excelDataTemplate.get("reviewRecordType").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setReviewRecordType(cellValue);
		}else if(excelDataTemplate.containsKey("workProductName") && excelDataTemplate.get("workProductName").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setWorkProductName(cellValue);
		}else if(excelDataTemplate.containsKey("revisionNo") && excelDataTemplate.get("revisionNo").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setRevisionNo(cellValue);
		}else if(excelDataTemplate.containsKey("reviewStartDate") && excelDataTemplate.get("reviewStartDate").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setReviewStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("reviewEndDate") && excelDataTemplate.get("reviewEndDate").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setReviewEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("major") && excelDataTemplate.get("major").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setMajor(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("minor") && excelDataTemplate.get("minor").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setMinor(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("trivial") && excelDataTemplate.get("trivial").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setTrivial(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("totalDefects") && excelDataTemplate.get("totalDefects").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setTotalDefects(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("totalAuthorEffort") && excelDataTemplate.get("totalAuthorEffort").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setTotalAuthorEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("totalReviewEffort") && excelDataTemplate.get("totalReviewEffort").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setTotalReviewEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("totalReworkEffort") && excelDataTemplate.get("totalReworkEffort").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setTotalReworkEffort(Float.parseFloat(cellValue));
			}
		}else if(excelDataTemplate.containsKey("workProductEffort") && excelDataTemplate.get("workProductEffort").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setWorkProductEffort(cellValue);
		}else if(excelDataTemplate.containsKey("reviewNumber") && excelDataTemplate.get("reviewNumber").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setReviewNumber(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("commentNumber") && excelDataTemplate.get("commentNumber").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				reviewRecordCollection.setCommentNumber(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("typeOfDefect") && excelDataTemplate.get("typeOfDefect").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setTypeOfDefect(cellValue);
		}else if(excelDataTemplate.containsKey("severity") && excelDataTemplate.get("severity").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setSeverity(cellValue);
		}else if(excelDataTemplate.containsKey("reviewerName") && excelDataTemplate.get("reviewerName").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setReviewerName(cellValue);
		}else if(excelDataTemplate.containsKey("issueLocation") && excelDataTemplate.get("issueLocation").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setIssueLocation(cellValue);
		}else if(excelDataTemplate.containsKey("defectDescription") && excelDataTemplate.get("defectDescription").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setDefectDescription(cellValue);
		}else if(excelDataTemplate.containsKey("actionTaken") && excelDataTemplate.get("actionTaken").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setActionTaken(cellValue);
		}else if(excelDataTemplate.containsKey("status") && excelDataTemplate.get("status").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setStatus(cellValue);
		}else if(excelDataTemplate.containsKey("causeOfDefect") && excelDataTemplate.get("causeOfDefect").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setCauseOfDefect(cellValue);
		}else if(excelDataTemplate.containsKey("remarks") && excelDataTemplate.get("remarks").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setRemarks(cellValue);
		}else if(excelDataTemplate.containsKey("correctiveAction") && excelDataTemplate.get("correctiveAction").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setCorrectiveAction(cellValue);
		}else if(excelDataTemplate.containsKey("preventiveAction") && excelDataTemplate.get("preventiveAction").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setPreventiveAction(cellValue);
		}else if(excelDataTemplate.containsKey("owner") && excelDataTemplate.get("owner").equalsIgnoreCase(headerCellValue)) {
			reviewRecordCollection.setOwner(cellValue);
		}else if(excelDataTemplate.containsKey("activityName") && excelDataTemplate.get("activityName").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setActivityName(cellValue);
		}else if(excelDataTemplate.containsKey("activityComponent") && excelDataTemplate.get("activityComponent").equalsIgnoreCase(headerCellValue)){
			reviewRecordCollection.setActivityComponent(cellValue);
		}
	}
	
	public void setDPAWorkbookCollectionValues(DPAWorkbookCollection dpaWorkbookCollection, LinkedHashMap<String, String> excelDataTemplate, String headerCellValue, String cellValue, Cell contentCell, String groupingType){
		
		if(excelDataTemplate.containsKey("programName") && excelDataTemplate.get("programName").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setProductName(cellValue);
		}else if(excelDataTemplate.containsKey("programManager") && excelDataTemplate.get("programManager").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setProductManager(cellValue);
		}else if(excelDataTemplate.containsKey("project") && excelDataTemplate.get("project").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setProject(cellValue);
		}else if(excelDataTemplate.containsKey("projectManager") && excelDataTemplate.get("projectManager").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setProjectManager(cellValue);
		}else if(excelDataTemplate.containsKey("competency") && excelDataTemplate.get("competency").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setCompetency(cellValue);
		}else if(excelDataTemplate.containsKey("competencyManager") && excelDataTemplate.get("competencyManager").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setCompetencyManager(cellValue);
		}else if(excelDataTemplate.containsKey("versionName") && excelDataTemplate.get("versionName").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setVersionName(cellValue);
		}else if(excelDataTemplate.containsKey("buildName") && excelDataTemplate.get("buildName").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setBuildName(cellValue);
		}else if(excelDataTemplate.containsKey("dpaId") && excelDataTemplate.get("dpaId").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setDpaId(cellValue);
		}else if(excelDataTemplate.containsKey("meetingReferenceDate") && excelDataTemplate.get("meetingReferenceDate").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setMeetingReferenceDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("defectType") && excelDataTemplate.get("defectType").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setDefectType(cellValue);
		}else if(excelDataTemplate.containsKey("defectCount") && excelDataTemplate.get("defectCount").equalsIgnoreCase(headerCellValue)){
			if(NumberUtils.isNumber(cellValue)){
				dpaWorkbookCollection.setDefectCount(Integer.parseInt(cellValue));
			}
		}else if(excelDataTemplate.containsKey("analysis") && excelDataTemplate.get("analysis").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setAnalysis(cellValue);
		}else if(excelDataTemplate.containsKey("primaryCause") && excelDataTemplate.get("primaryCause").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setPrimaryCause(cellValue);
		}else if(excelDataTemplate.containsKey("correctiveDescription") && excelDataTemplate.get("correctiveDescription").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveDescription(cellValue);
		}else if(excelDataTemplate.containsKey("correctivePlannedStartDate") && excelDataTemplate.get("correctivePlannedStartDate").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectivePlannedStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("correctivePlannedEndDate") && excelDataTemplate.get("correctivePlannedEndDate").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectivePlannedEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("correctiveActualStartDate") && excelDataTemplate.get("correctiveActualStartDate").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveActualStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("correctiveActualEndDate") && excelDataTemplate.get("correctiveActualEndDate").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveActualEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("correctiveClosureResponsibility") && excelDataTemplate.get("correctiveClosureResponsibility").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveClosureResponsibility(cellValue);
		}else if(excelDataTemplate.containsKey("correctiveActionStatus") && excelDataTemplate.get("correctiveActionStatus").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveActionStatus(cellValue);
		}else if(excelDataTemplate.containsKey("correctiveEvaluatingEffectiveness") && excelDataTemplate.get("correctiveEvaluatingEffectiveness").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveEvaluatingEffectiveness(cellValue);
		}else if(excelDataTemplate.containsKey("correctiveEvaluatingDate") && excelDataTemplate.get("correctiveEvaluatingDate").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveEvaluatingDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("correctiveEffectiveness") && excelDataTemplate.get("correctiveEffectiveness").equalsIgnoreCase(headerCellValue) && "Corrective".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setCorrectiveEffectiveness(cellValue);
		}else if(excelDataTemplate.containsKey("preventiveDescription") && excelDataTemplate.get("preventiveDescription").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveDescription(cellValue);
		}else if(excelDataTemplate.containsKey("preventivePlannedStartDate") && excelDataTemplate.get("preventivePlannedStartDate").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventivePlannedStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("preventivePlannedEndDate") && excelDataTemplate.get("preventivePlannedEndDate").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventivePlannedEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("preventiveActualStartDate") && excelDataTemplate.get("preventiveActualStartDate").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveActualStartDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("preventiveActualEndDate") && excelDataTemplate.get("preventiveActualEndDate").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveActualEndDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("preventiveClosureResponsibility") && excelDataTemplate.get("preventiveClosureResponsibility").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveClosureResponsibility(cellValue);
		}else if(excelDataTemplate.containsKey("preventiveActionStatus") && excelDataTemplate.get("preventiveActionStatus").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveActionStatus(cellValue);
		}else if(excelDataTemplate.containsKey("preventiveEvaluatingEffectiveness") && excelDataTemplate.get("preventiveEvaluatingEffectiveness").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveEvaluatingEffectiveness(cellValue);
		}else if(excelDataTemplate.containsKey("preventiveEvaluatingDate") && excelDataTemplate.get("preventiveEvaluatingDate").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveEvaluatingDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("preventiveEffectiveness") && excelDataTemplate.get("preventiveEffectiveness").equalsIgnoreCase(headerCellValue) && "Preventive".equalsIgnoreCase(groupingType)){
			dpaWorkbookCollection.setPreventiveEffectiveness(cellValue);
		}else if(excelDataTemplate.containsKey("remarks") && excelDataTemplate.get("remarks").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setRemarks(cellValue);
		}else if(excelDataTemplate.containsKey("createdDate") && excelDataTemplate.get("createdDate").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setCreatedDate(contentCell.getDateCellValue());
		}else if(excelDataTemplate.containsKey("updateDate") && excelDataTemplate.get("updateDate").equalsIgnoreCase(headerCellValue)){
			dpaWorkbookCollection.setUpdateDate(contentCell.getDateCellValue());
		}
	}
	
	public void setUtilizationCollectionValues(UtilizationCollection utilizationCollection, LinkedHashMap<String, String> excelDataTemplate, String headerCellValue, String cellValue, Cell contentCell){
		if(utilizationCollection == null){
			return;
		}
		if(excelDataTemplate.containsKey("programName") && excelDataTemplate.get("programName").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setProductName(cellValue);
		}else if(excelDataTemplate.containsKey("programManager") && excelDataTemplate.get("programManager").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setProductManager(cellValue);
		}else if(excelDataTemplate.containsKey("project") && excelDataTemplate.get("project").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setProject(cellValue);
		}else if(excelDataTemplate.containsKey("projectManager") && excelDataTemplate.get("projectManager").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setProjectManager(cellValue);
		}else if(excelDataTemplate.containsKey("competency") && excelDataTemplate.get("competency").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setCompetency(cellValue);
		}else if(excelDataTemplate.containsKey("competencyManager") && excelDataTemplate.get("competencyManager").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setCompetencyManager(cellValue);
		}else if(excelDataTemplate.containsKey("versionName") && excelDataTemplate.get("versionName").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setVersionName(cellValue);
		}else if(excelDataTemplate.containsKey("buildName") && excelDataTemplate.get("buildName").equalsIgnoreCase(headerCellValue)){
			utilizationCollection.setBuildName(cellValue);
		}else if(excelDataTemplate.containsKey("startDate") && excelDataTemplate.get("startDate").equalsIgnoreCase(headerCellValue)) {
			if(contentCell != null){
				utilizationCollection.setStartDate(contentCell.getDateCellValue());
			}
		}else if(excelDataTemplate.containsKey("endDate") && excelDataTemplate.get("endDate").equalsIgnoreCase(headerCellValue)) {
			if(contentCell != null){
				utilizationCollection.setEndDate(contentCell.getDateCellValue());
			}
		}else if(excelDataTemplate.containsKey("resourceName") && excelDataTemplate.get("resourceName").equalsIgnoreCase(headerCellValue)) {
			utilizationCollection.setResourceName(cellValue);
		}else if(excelDataTemplate.containsKey("moduleName") && excelDataTemplate.get("moduleName").equalsIgnoreCase(headerCellValue)) {
			utilizationCollection.setModuleName(cellValue);
		}else if(excelDataTemplate.containsKey("activityName") && excelDataTemplate.get("activityName").equalsIgnoreCase(headerCellValue)) {
			utilizationCollection.setActivityName(cellValue);
		}else if(excelDataTemplate.containsKey("activityType") && excelDataTemplate.get("activityType").equalsIgnoreCase(headerCellValue)) {
			utilizationCollection.setActivityType(cellValue);
		}else if(excelDataTemplate.containsKey("activityEffort") && excelDataTemplate.get("activityEffort").equalsIgnoreCase(headerCellValue)) {
			if(NumberUtils.isNumber(cellValue)){
				utilizationCollection.setActivityEffort(Float.parseFloat(cellValue));
			}
		}
	}
	
	public Date setDateForMongoDB(Date dateToMongoDB){
		
		Calendar dateToMongoDBCalendar = Calendar.getInstance();
		dateToMongoDBCalendar.setTime(dateToMongoDB);
		dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
		
		return dateToMongoDBCalendar.getTime();
	}

}
