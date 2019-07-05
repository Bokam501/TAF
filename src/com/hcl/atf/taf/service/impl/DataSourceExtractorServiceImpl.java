package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DataSourceExtractorDAO;
import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DPAWorkbookCollection;
import com.hcl.atf.taf.model.DataExtractionReportSummary;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ExtractorTypeMaster;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UtilizationCollection;
import com.hcl.atf.taf.service.DataSourceExtractorService;

@Service
public class DataSourceExtractorServiceImpl implements DataSourceExtractorService {
	
	@Autowired
	private DataSourceExtractorDAO dataSourceExtractorDAO;
	
	@Override
	public void saveOrUpdateActivityCollection(ActivityCollection activityCollection) {
		dataSourceExtractorDAO.saveOrUpdateActivityCollection(activityCollection);
	}

	@Override
	public ActivityCollection getActivityCollectionOfWeekForProgramAndType(String testFactoryName, String programName, Date weekDate, String recordType1, String recordType2) {
		return dataSourceExtractorDAO.getActivityCollectionOfWeekForProgramAndType(testFactoryName, programName, weekDate, recordType1, recordType2);
	}
	
	@Override
	public Boolean getStatusOfFileUpdate(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate) {
		return dataSourceExtractorDAO.getStatusOfFileUpdate(testFactoryName, projectId, fileName, lastUpdatedDate);
	}
	
	@Override
	public Date getLastUpdateDateOfFileLocation(String fileLocation) {
		return dataSourceExtractorDAO.getLastUpdateDateOfFileLocation(fileLocation);
	}

	@Override
	public void saveOrUpdateFileUpdatedStatus(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate) {
		dataSourceExtractorDAO.saveOrUpdateFileUpdatedStatus(testFactoryName, projectId, fileName, lastUpdatedDate);
	}

	@Override
	public void saveOrUpdateDefectCollection(DefectCollection defectCollection) {
		dataSourceExtractorDAO.saveOrUpdateDefectCollection(defectCollection);		
	}

	@Override
	public DefectCollection getDefectCollectionByProgramNameDateAndID(String testFactoryName, String programName, Date raisedDate, String defectId, String versionName) {
		return dataSourceExtractorDAO.getDefectCollectionByProgramNameDateAndID(testFactoryName, programName, raisedDate, defectId, versionName);	
	}
	
	@Override
	public Float getCumulativeActualActivityCollection(String testFactoryName, String programName, Date weekDate, String recordType1, String recordType2) {
		return dataSourceExtractorDAO.getCumulativeActualActivityCollection(testFactoryName, programName, weekDate, recordType1, recordType2);
	}

	@Override
	public void saveOrUpdateReviewRecordCollection(ReviewRecordCollection reviewRecordCollection) {
		dataSourceExtractorDAO.saveOrUpdateReviewRecordCollection(reviewRecordCollection);		
	}
	
	@Override
	public void saveOrUpdateUtilizationCollection(UtilizationCollection utilizationCollection) {
		dataSourceExtractorDAO.saveOrUpdateUtilizationCollection(utilizationCollection);
	}

	@Override
	public ActivityCollection getTaskActivityCollectionByName(ActivityCollection activityCollectionTask) {
		return dataSourceExtractorDAO.getTaskActivityCollectionByName(activityCollectionTask);
	}

	@Override
	public DefectCollection getQueryLogByNameNoRaisedDate(DefectCollection defectCollection) {
		return dataSourceExtractorDAO.getQueryLogByNameNoRaisedDate(defectCollection);
	}
	
	@Override
	public List<DataExtractorScheduleMaster> getDataExtractorScheduleMastersList(Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return dataSourceExtractorDAO.getDataExtractorScheduleMastersList(status, jtStartIndex, jtPageSize);
	}

	@Override
	public int getTotalRecordsForDataExtractorSchedulePagination(Integer status, Class<DataExtractorScheduleMaster> className) {
		return dataSourceExtractorDAO.getTotalRecordsForDataExtractorSchedulePagination(status, className);
	}

	@Override
	public void addDataExtractorScheduleMaster(DataExtractorScheduleMaster dataExtractorScheduleMaster) {
		dataSourceExtractorDAO.addDataExtractorScheduleMaster(dataExtractorScheduleMaster);
	}

	@Override
	public void updateDataExtractorScheduleMasster(DataExtractorScheduleMaster dataExtractorScheduleMaster) {
		dataSourceExtractorDAO.updateDataExtractorScheduleMaster(dataExtractorScheduleMaster);
	}
	
	@Override
	public DataExtractorScheduleMaster getDataExtractorScheduleMasterById(int dataExtractorScheduleMasterId) {
		return dataSourceExtractorDAO.getDataExtractorScheduleMasterById(dataExtractorScheduleMasterId);
	}

	@Override
	public DataExtractorScheduleMaster getDataExtractorByJobName(String dataExtractorJobName, Integer dataExtractorId) {
		return dataSourceExtractorDAO.getDataExtractorByJobName(dataExtractorJobName, dataExtractorId);
	}

	@Override
	public List<ExtractorTypeMaster> getExtarctorTypeList() {
		return dataSourceExtractorDAO.getExtarctorTypeList();
	}

	@Override
	public ExtractorTypeMaster getExtarctorTypeById(Integer id) {
		return dataSourceExtractorDAO.getExtarctorTypeById(id);
	}

	@Override
	public void addDataExtractionReportSummary(DataExtractionReportSummary dataExtractionReportSummary){
		dataSourceExtractorDAO.addDataExtractionReportSummary(dataExtractionReportSummary);
	}

	@Override
	@Transactional
	public DPAWorkbookCollection getDPAWorkbookCollection(String testFactoryName, String productName, String dpaId) {
		return dataSourceExtractorDAO.getDPAWorkbookCollection(testFactoryName, productName, dpaId);
	}

	@Override
	@Transactional
	public void saveOrUpdateDPAWorkbookCollection(DPAWorkbookCollection dpaWorkbookCollection) {
		dataSourceExtractorDAO.saveOrUpdateDPAWorkbookCollection(dpaWorkbookCollection);
	}
}
