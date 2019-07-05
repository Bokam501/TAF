package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DPAWorkbookCollection;
import com.hcl.atf.taf.model.DataExtractionReportSummary;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ExtractorTypeMaster;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UtilizationCollection;

public interface DataSourceExtractorService {

	void saveOrUpdateActivityCollection(ActivityCollection activityCollection);
	ActivityCollection getActivityCollectionOfWeekForProgramAndType(String testFactoryName, String programName, Date weekDate, String recordType1, String recordType2);
	
	Boolean getStatusOfFileUpdate(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate);
	Date getLastUpdateDateOfFileLocation(String fileLocation);
	void saveOrUpdateFileUpdatedStatus(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate);
	
	void saveOrUpdateDefectCollection(DefectCollection defectCollection);
	DefectCollection getDefectCollectionByProgramNameDateAndID(String testFactoryName, String programName, Date raisedDate, String defectId, String versionName);
	Float getCumulativeActualActivityCollection(String testFactoryName, String programName, Date weekDate, String recordType1, String recordType2);
	
	void saveOrUpdateReviewRecordCollection(ReviewRecordCollection reviewRecordCollection);
	
	void saveOrUpdateUtilizationCollection(UtilizationCollection utilizationCollection);
	
	ActivityCollection getTaskActivityCollectionByName(ActivityCollection activityCollectionTask);
	DefectCollection getQueryLogByNameNoRaisedDate(DefectCollection defectCollection);
	
	List<DataExtractorScheduleMaster> getDataExtractorScheduleMastersList(Integer status, Integer jtStartIndex, Integer jtPageSize);
	int getTotalRecordsForDataExtractorSchedulePagination(Integer status, Class<DataExtractorScheduleMaster> className);
	void addDataExtractorScheduleMaster(DataExtractorScheduleMaster dataExtractorScheduleMaster);
	void updateDataExtractorScheduleMasster(DataExtractorScheduleMaster dataExtractorScheduleMaster);
	DataExtractorScheduleMaster getDataExtractorScheduleMasterById(int entityId);
	DataExtractorScheduleMaster getDataExtractorByJobName(String dataExtractorJobName, Integer dataExtractorId);
	
	List<ExtractorTypeMaster> getExtarctorTypeList();
	ExtractorTypeMaster getExtarctorTypeById(Integer id);
	void addDataExtractionReportSummary(DataExtractionReportSummary dataExtractionReportSummary);
	
	DPAWorkbookCollection getDPAWorkbookCollection(String testFactoryName, String productName, String dpaId);
	void saveOrUpdateDPAWorkbookCollection(DPAWorkbookCollection dpaWorkbookCollection);
}
