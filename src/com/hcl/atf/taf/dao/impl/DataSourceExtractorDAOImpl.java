package com.hcl.atf.taf.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
import com.hcl.atf.taf.model.FileUpdateMaster;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UtilizationCollection;

@Service
public class DataSourceExtractorDAOImpl implements DataSourceExtractorDAO {

	private static final Log log = LogFactory.getLog(DataSourceExtractorDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	

	@Override
	@Transactional
	public void saveOrUpdateActivityCollection(ActivityCollection activityCollection) {
		log.debug("Adding document reference for MongoDB");
		try {
			log.debug("add is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(activityCollection);
			
			log.debug("add successful");
		} catch (Exception ex) {
			log.error("add failed", ex);
		}
	}

	@Override
	@Transactional
	public ActivityCollection getActivityCollectionOfWeekForProgramAndType(String testFactoryName, String programName, Date weekDate, String recordType1, String recordType2) {
		log.debug("Get activity collection for week");
		ActivityCollection activityCollection = null;
		try {
			log.debug("add is executed");
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			criteria.add(Restrictions.eq("activity.testFactoryName", testFactoryName));
			criteria.add(Restrictions.eq("activity.productName", programName));
			criteria.add(Restrictions.eq("activity.weekDate", weekDate));
			
			if(recordType1 != null && !recordType1.trim().isEmpty() && recordType2 != null && !recordType2.trim().isEmpty()){
				criteria.add(Restrictions.or(Restrictions.eq("activity.type", recordType1), Restrictions.eq("activity.type", recordType2)));
			}else if(recordType1 != null && !recordType1.trim().isEmpty()){
				criteria.add(Restrictions.eq("activity.type", recordType1));
			}else if(recordType2 != null && !recordType2.trim().isEmpty()){
				criteria.add(Restrictions.eq("activity.type", recordType2));
			}
			
			List<ActivityCollection> activityCollections = criteria.list();
			
			if(activityCollections != null && activityCollections.size() > 0){
				activityCollection = activityCollections.get(0);
			}
			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing failed", ex);
		}
		return activityCollection;
	}
	
	
	@Override
	@Transactional
	public Boolean getStatusOfFileUpdate(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate) {
		log.debug("Get file update status");
		boolean isFileUpdated = true;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FileUpdateMaster.class, "fileUpdate");
			criteria.add(Restrictions.eq("fileUpdate.testFactoryName", testFactoryName));
			criteria.add(Restrictions.eq("fileUpdate.projectId", projectId));
			criteria.add(Restrictions.eq("fileUpdate.fileName", fileName));
			
			List<FileUpdateMaster> fileUpdateMasters = criteria.list();
			
			if(fileUpdateMasters != null && fileUpdateMasters.size() > 0){
				FileUpdateMaster fileUpdateMaster = fileUpdateMasters.get(0);
				
				SimpleDateFormat commonDateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm.sss");
				fileUpdateMaster.setLastUpdatedDate(commonDateFormatter.parse(commonDateFormatter.format(fileUpdateMaster.getLastUpdatedDate())));
				lastUpdatedDate = commonDateFormatter.parse(commonDateFormatter.format(lastUpdatedDate));
				
				if(fileUpdateMaster.getLastUpdatedDate().equals(lastUpdatedDate) || fileUpdateMaster.getLastUpdatedDate().after(lastUpdatedDate)){
					isFileUpdated = false;
				}
			}
			
			
		} catch (Exception ex) {
			log.error("Status identification failed", ex);
		}
		
		return isFileUpdated;
	}

	@Override
	@Transactional
	public Date getLastUpdateDateOfFileLocation(String fileLocation) {
		log.debug("Get file update status");
		Date lastUpdatedDate = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FileUpdateMaster.class, "fileUpdate");
			criteria.add(Restrictions.eq("fileUpdate.fileName", fileLocation));
			
			List<FileUpdateMaster> fileUpdateMasters = criteria.list();
			
			if(fileUpdateMasters != null && fileUpdateMasters.size() > 0){
				FileUpdateMaster fileUpdateMaster = fileUpdateMasters.get(0);
				
				SimpleDateFormat commonDateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm.sss");
				fileUpdateMaster.setLastUpdatedDate(commonDateFormatter.parse(commonDateFormatter.format(fileUpdateMaster.getLastUpdatedDate())));
				lastUpdatedDate = fileUpdateMaster.getLastUpdatedDate();
				
			}
			
			
		} catch (Exception ex) {
			log.error("Status identification failed", ex);
		}
		
		return lastUpdatedDate;
	}

	@Override
	@Transactional
	public void saveOrUpdateFileUpdatedStatus(String testFactoryName, String projectId, String fileName, Date lastUpdatedDate) {
		
		try {
			log.debug("saveOrUpdateFileUpdatedStatus");
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FileUpdateMaster.class, "fileUpdate");
			criteria.add(Restrictions.eq("fileUpdate.fileName", fileName));
			
			List<FileUpdateMaster> fileUpdateMasters = criteria.list();
			
			FileUpdateMaster fileUpdateMaster = null;
			if(fileUpdateMasters != null && fileUpdateMasters.size() > 0){
				fileUpdateMaster = fileUpdateMasters.get(0);
				fileUpdateMaster.setLastUpdatedDate(lastUpdatedDate);
			}else{
				fileUpdateMaster = new FileUpdateMaster();
				fileUpdateMaster.setTestFactoryName(testFactoryName);
				fileUpdateMaster.setProjectId(projectId);
				fileUpdateMaster.setFileName(fileName);
				fileUpdateMaster.setLastUpdatedDate(lastUpdatedDate);
			}
			if(fileUpdateMaster != null){
				sessionFactory.getCurrentSession().saveOrUpdate(fileUpdateMaster);
				log.debug("Saved or Update successfully");
			}
		} catch (Exception ex) {
			log.error("Error while save or update file update details", ex);
		}
		
	}


	@Override
	@Transactional
	public void saveOrUpdateDefectCollection(DefectCollection defectCollection) {
		log.debug("Adding document reference for MongoDB");
		try {
			log.debug("add is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(defectCollection);
			
			log.debug("add successful");
		} catch (Exception ex) {
			log.error("adding document reference failed", ex);
		}
		
	}

	@Override
	@Transactional
	public DefectCollection getDefectCollectionByProgramNameDateAndID(String testFactoryName, String programName, Date raisedDate, String defectId, String versionName) {

		log.debug("Get defect collection for credentials");
		DefectCollection defectCollection = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DefectCollection.class, "defect");
			criteria.add(Restrictions.eq("defect.testFactoryName", testFactoryName));
			criteria.add(Restrictions.eq("defect.productName", programName));
			criteria.add(Restrictions.eq("defect.raisedDate", raisedDate));
			criteria.add(Restrictions.eq("defect.defectId", defectId));
			criteria.add(Restrictions.eq("defect.versionName", versionName));
			
			List<DefectCollection> defectCollections = criteria.list();
			
			if(defectCollections != null && defectCollections.size() > 0){
				defectCollection = defectCollections.get(0);
			}
			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing defect collection failed", ex);
		}
		return defectCollection;
	
	}

	@Override
	@Transactional
	public Float getCumulativeActualActivityCollection(String testFactoryName, String productName, Date weekDate, String recordType1, String recordType2) {
		Float cumulativeActual = 0.0F;
		try{
			
			log.debug("getCumulativeActualActivityCollection");
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			criteria.add(Restrictions.eq("activity.testFactoryName", testFactoryName));
			criteria.add(Restrictions.eq("activity.productName", productName));
			criteria.add(Restrictions.lt("activity.weekDate", weekDate));
			
			if(recordType1 != null && !recordType1.trim().isEmpty() && recordType2 != null && !recordType2.trim().isEmpty()){
				criteria.add(Restrictions.or(Restrictions.eq("activity.type", recordType1), Restrictions.eq("activity.type", recordType2)));
			}else if(recordType1 != null && !recordType1.trim().isEmpty()){
				criteria.add(Restrictions.eq("activity.type", recordType1));
			}else if(recordType2 != null && !recordType2.trim().isEmpty()){
				criteria.add(Restrictions.eq("activity.type", recordType2));
			}
			
			criteria.setProjection(Projections.sum("activity.activitySizeActual"));
			
			if(criteria.uniqueResult() != null){
				cumulativeActual = ((Double) criteria.uniqueResult()).floatValue();
			}
			
			
		}catch(Exception ex){
			log.error("retriving CumulativeActualActivityCollection  failed", ex);
		}
		return cumulativeActual;
	}

	@Override
	@Transactional
	public void saveOrUpdateReviewRecordCollection(ReviewRecordCollection reviewRecordCollection) {
		log.debug("Adding document reference for MongoDB");
		try {
			log.debug("add is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(reviewRecordCollection);
			
			log.debug("add successful");
		} catch (Exception ex) {
			log.error("Adding document reference failed", ex);
		}
	}
	
	@Override
	@Transactional
	public void saveOrUpdateUtilizationCollection(UtilizationCollection utilizationCollection) {
		log.debug("Adding document reference for MongoDB");
		try {
			log.debug("add is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(utilizationCollection);
			
			log.debug("add successful");
		} catch (Exception ex) {
			log.error("Adding document reference failed", ex);
		}
	}
	
	@Override
	@Transactional
	public ActivityCollection getTaskActivityCollectionByName(ActivityCollection activityCollectionTask) {
		log.debug("Get activity collection for task");
		ActivityCollection activityCollection = null;
		try {
			log.debug("add is executed");
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			criteria.add(Restrictions.eq("activity.productName", activityCollectionTask.getProductName()));
			criteria.add(Restrictions.eq("activity.activityName", activityCollectionTask.getActivityName()));
			criteria.add(Restrictions.eq("activity.testFactoryName", activityCollectionTask.getTestFactoryName()));
			
			List<ActivityCollection> activityCollections = criteria.list();
			
			if(activityCollections != null && activityCollections.size() > 0){
				activityCollection = activityCollections.get(0);
			}
			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing TaskActivityCollectionByName failed", ex);
		}
		return activityCollection;
	}

	@Override
	@Transactional
	public DefectCollection getQueryLogByNameNoRaisedDate(DefectCollection defectCollection) {
		log.debug("Get defect collection for query log");
		DefectCollection availableDefectCollection = null;
		try {
			log.debug("add is executed");
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DefectCollection.class, "defect");
			criteria.add(Restrictions.eq("defect.title", defectCollection.getTitle()));
			criteria.add(Restrictions.eq("defect.testFactoryName", defectCollection.getTestFactoryName()));
			criteria.add(Restrictions.eq("defect.productName", defectCollection.getProductName()));
			criteria.add(Restrictions.eq("defect.raisedDate", defectCollection.getRaisedDate()));
			
			List<DefectCollection> defectCollections = criteria.list();
			
			if(defectCollections != null && defectCollections.size() > 0){
				availableDefectCollection = defectCollections.get(0);
			}			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing QueryLog By NameNoRaisedDate failed", ex);
		}
		return availableDefectCollection;
	}
	
	@Override
	@Transactional
	public List<DataExtractorScheduleMaster> getDataExtractorScheduleMastersList(Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all DataExtractorScheduleMaster instance");
		List<DataExtractorScheduleMaster> dataExtractorScheduleMasters = new ArrayList<DataExtractorScheduleMaster>();
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataExtractorScheduleMaster.class, "dataExtractor");
			if(status != 2){
				criteria.add(Restrictions.eq("dataExtractor.status", status));
			}
			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			dataExtractorScheduleMasters = criteria.list();
			
			for (DataExtractorScheduleMaster dataExtractorScheduleMasterLoop : dataExtractorScheduleMasters) {
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCustomer());		
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getProduct());
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCompetency());
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getExtractorType());
			}
			log.debug("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return dataExtractorScheduleMasters;
	}


	@Override
	@Transactional
	public int getTotalRecordsForDataExtractorSchedulePagination(Integer status, Class<DataExtractorScheduleMaster> className) {
		log.debug("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName());
			if(status != 2){
				criteria.add(Restrictions.eq("status", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = 0; 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}


	@Override
	@Transactional
	public void addDataExtractorScheduleMaster(DataExtractorScheduleMaster dataExtractorScheduleMaster) {

		log.debug("adding DataExtractorScheduleMaster ");
		try {
			log.debug("add is executed");
			sessionFactory.getCurrentSession().save(dataExtractorScheduleMaster);
			
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("Adding DataExtractorScheduleMaster failed", re);
		}
	}

	@Override
	@Transactional
	public void updateDataExtractorScheduleMaster(DataExtractorScheduleMaster dataExtractorScheduleMaster) {
		log.debug("updating DataExtractorScheduleMaster");
		try {
			log.debug("update is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(dataExtractorScheduleMaster);
			
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("Updating DataExtractorScheduleMaster failed", re);
		}
	}


	@Override
	@Transactional
	public DataExtractorScheduleMaster getDataExtractorScheduleMasterById(int dataExtractorScheduleMasterId) {
		log.debug("get Data extratcor schedule master for id - "+dataExtractorScheduleMasterId );
		DataExtractorScheduleMaster dataExtractorScheduleMaster = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataExtractorScheduleMaster.class, "dataExtractor");
			criteria.add(Restrictions.eq("dataExtractor.id", dataExtractorScheduleMasterId));
						
			List<DataExtractorScheduleMaster> dataExtractorScheduleMasters = criteria.list();	
			for (DataExtractorScheduleMaster dataExtractorScheduleMasterLoop : dataExtractorScheduleMasters) {
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCustomer());		
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getProduct());
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCompetency());
			}
			dataExtractorScheduleMaster = (dataExtractorScheduleMasters!=null && dataExtractorScheduleMasters.size()!=0)?(DataExtractorScheduleMaster)dataExtractorScheduleMasters.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return dataExtractorScheduleMaster;
	}


	@Override
	@Transactional
	public DataExtractorScheduleMaster getDataExtractorByJobName(String dataExtractorJobName, Integer dataExtractorId) {
		log.debug("get data extractor details by data extractor job name");
		DataExtractorScheduleMaster dataExtractorScheduleMaster = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataExtractorScheduleMaster.class, "dataExtractor");
			criteria.add(Restrictions.eq("dataExtractor.jobName", dataExtractorJobName));
			if(dataExtractorId != null && dataExtractorId != 0){
				criteria.add(Restrictions.ne("dataExtractor.id", dataExtractorId));
			}
			
			List<DataExtractorScheduleMaster> dataExtractorScheduleMasters = criteria.list();	
			for (DataExtractorScheduleMaster dataExtractorScheduleMasterLoop : dataExtractorScheduleMasters) {
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCustomer());		
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getProduct());
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getCompetency());
				Hibernate.initialize(dataExtractorScheduleMasterLoop.getExtractorType());
			}
			dataExtractorScheduleMaster = (dataExtractorScheduleMasters!=null && dataExtractorScheduleMasters.size()!=0)?(DataExtractorScheduleMaster)dataExtractorScheduleMasters.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return dataExtractorScheduleMaster;
	}


	@Override
	@Transactional
	public List<ExtractorTypeMaster> getExtarctorTypeList() {
		log.debug("Get extarctor types");
		List<ExtractorTypeMaster> extractorTypeMasters = new ArrayList<ExtractorTypeMaster>();
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtractorTypeMaster.class);
			
			extractorTypeMasters = criteria.list();
			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing ExtractorTypeMaster failed", ex);
		}
		return extractorTypeMasters;
	}

	@Override
	@Transactional
	public ExtractorTypeMaster getExtarctorTypeById(Integer id) {
		log.debug("Get extarctor type by Id");
		ExtractorTypeMaster extractorTypeMaster = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExtractorTypeMaster.class, "extractorType");
			criteria.add(Restrictions.eq("extractorType.id", id));
			
			List<ExtractorTypeMaster> extractorTypeMasters = criteria.list();
			
			if(extractorTypeMasters != null && extractorTypeMasters.size() > 0){
				extractorTypeMaster = extractorTypeMasters.get(0);
			}
			
			log.debug("listing successful");
			
		} catch (Exception ex) {
			log.error("listing ExtractorTypeMaster failed", ex);
		}
		return extractorTypeMaster;
	}
	
	@Override
	@Transactional
	public void addDataExtractionReportSummary(DataExtractionReportSummary dataExtractionReportSummary) {
		log.debug("adding DataExtractionReportSummary ");
		try {			
			sessionFactory.getCurrentSession().save(dataExtractionReportSummary);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("adding DataExtractionReportSummary failed", re);
		}
	 }

	@Override
	@Transactional
	public DPAWorkbookCollection getDPAWorkbookCollection(String testFactoryName, String productName, String dpaId) {
		DPAWorkbookCollection dpaWorkbookCollection = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DPAWorkbookCollection.class, "dpaWorkbook");
			criteria.add(Restrictions.eq("dpaWorkbook.testFactoryName", testFactoryName));
			criteria.add(Restrictions.eq("dpaWorkbook.productName", productName));
			criteria.add(Restrictions.eq("dpaWorkbook.dpaId", dpaId));
			
			List<DPAWorkbookCollection> dpaWorkbookCollections = criteria.list();
			
			if(dpaWorkbookCollections != null && dpaWorkbookCollections.size() > 0){
				dpaWorkbookCollection = dpaWorkbookCollections.get(0);
			}
			
		}catch(Exception ex){
			log.error("Error in getDPAWorkbookCollection - ", ex);
		}
		return dpaWorkbookCollection;
	}

	@Override
	@Transactional
	public void saveOrUpdateDPAWorkbookCollection(DPAWorkbookCollection dpaWorkbookCollection) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(dpaWorkbookCollection);
		}catch(Exception ex){
			log.error("Error in saveOrUpdateDPAWorkbookCollection - ", ex);
		}
	}

}
