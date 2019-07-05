package com.hcl.atf.taf.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityEntityMasterDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.CustomFieldDAO;
import com.hcl.atf.taf.dao.DatabaseVersionDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.AttachmentType;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonAttachmentType;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonSimpleOption;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.LicenseCheckService;

@Service
public class CommonServiceImpl implements CommonService {	 

	private static final Log log = LogFactory.getLog(CommonServiceImpl.class);

	@Autowired
	private CommonDAO commonDAO;
	@Autowired
	private DatabaseVersionDAO databaseVersionDAO;
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Autowired
	private ActivityEntityMasterDAO activityEntityMasterDAO;
	
	@Autowired
	private ActivityTaskDAO activityTaskDAO;
	@Autowired
	private ActivityDAO activityDAO;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private EventsService eventsService;

	@Autowired
	private CustomFieldDAO customFieldDAO;
	@Autowired
	private LicenseCheckService licenseCheckService;
	
	@Override
	@Transactional
	public List<String> getPalmBuildDetails(HttpServletRequest request) {
		List<String> results=null;
		String expiryDate = null;
		try {
			 results = readFile(request);
			//Get the License expirt date
			String filePath = request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"license.properties";
			expiryDate = licenseCheckService.getLicenseExpiryData(filePath);
			results.add("ExpiryDate="+expiryDate);
		} catch (IOException e) {
			log.error("ERROR  ",e);
		}
		return results;
	}
	
	public List<String> readFile(HttpServletRequest request) throws IOException {
		List<String> tcList = new ArrayList<String>();
		InputStream fis = new FileInputStream (request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"version.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			tcList.add(line);
		}
		br.close();
		String currentDBVersion = "1.0";
		currentDBVersion = databaseVersionDAO.getCurrentDBVersion();
		String currentDBVersionText = "DatabaseVersion="+currentDBVersion;
		tcList.add(currentDBVersionText);
		return tcList;
	}

	@Override
	@Transactional
	public String duplicateName(String name, String tableName, String columnName,String entityName,String filter) {
		return commonDAO.duplicateName(name, tableName, columnName,entityName,filter);
	}
	
	@Override
	@Transactional
	public String duplicateNameWithOutFilter(String name, String tableName, String columnName,String entityName) {
		return commonDAO.duplicateNameWithOutFilter(name, tableName, columnName,entityName);
	}
	
	@Override
	@Transactional
	public List<JsonEntityMaster> getEntityTypeList() {
		List<EntityMaster> entityMasters = commonDAO.getEntityTypeList();
		List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
		for(EntityMaster entityMaster : entityMasters){
			JsonEntityMaster jesonEntityMaster = new JsonEntityMaster(entityMaster);
			jsonEntityMasters.add(jesonEntityMaster);
		}
		return jsonEntityMasters;
	}

	@Override
	@Transactional
	public JTableResponseOptions getEntityForTypeList(Integer engagementId, Integer productId, Integer entityTypeId) {
		JTableResponseOptions jTableResponseOptions = null;
		try {
			List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();

			if(IDPAConstants.ENTITY_TASK_TYPE.equals(entityTypeId)){
				List<ActivityTaskType> activityTaskTypes = activityTaskDAO.listActivityTaskTypes(engagementId, productId, null, null, 1, true);
				if(activityTaskTypes != null && activityTaskTypes.size() > 0){
					for(ActivityTaskType activityTaskType : activityTaskTypes){
						JsonEntityMaster jsonEntityMaster = new JsonEntityMaster();
						jsonEntityMaster.setEntityMasterId(activityTaskType.getActivityTaskTypeId());
						jsonEntityMaster.setEntityMasterName(activityTaskType.getActivityTaskTypeName());
						jsonEntityMasters.add(jsonEntityMaster);
					}
				}
			}else if(IDPAConstants.ENTITY_ACTIVITY_TYPE.equals(entityTypeId)){
				List<ActivityMaster> activityMasters = activityEntityMasterDAO.getActivityTypesByEnagementAndProductId(engagementId, productId);
				if(activityMasters != null && activityMasters.size() > 0){
					for(ActivityMaster activityMaster : activityMasters){
						JsonEntityMaster jsonEntityMaster = new JsonEntityMaster();
						jsonEntityMaster.setEntityMasterId(activityMaster.getActivityMasterId());
						jsonEntityMaster.setEntityMasterName(activityMaster.getActivityMasterName());
						jsonEntityMasters.add(jsonEntityMaster);
					}
				}
			}
			
			if (jsonEntityMasters == null || jsonEntityMasters.isEmpty()) {
				JsonEntityMaster jsonEntityMaster = new JsonEntityMaster();
				jsonEntityMaster.setEntityMasterId(0);
				jsonEntityMaster.setEntityMasterName("--");
				jsonEntityMasters.add(jsonEntityMaster);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityMasters, true);
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity list for entity type - "+entityTypeId);
			log.error("Unable to get entity list for entity type - "+entityTypeId, e);
		}
		return jTableResponseOptions;
	}

	@Override
	@Transactional
	public List<JsonEntityMaster> getWorkflowCapableEntityTypeList() {
		List<EntityMaster> entityMasters = commonDAO.getWorkflowCapableEntityTypeList();
		List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
		for(EntityMaster entityMaster : entityMasters){
			JsonEntityMaster jesonEntityMaster = new JsonEntityMaster(entityMaster);
			jsonEntityMasters.add(jesonEntityMaster);
		}
		return jsonEntityMasters;
	}

	@Override
	@Transactional
	public void addAttachment(Attachment attachment) {
		commonDAO.addAttachment(attachment);
	}

	@Override
	@Transactional
	public void updateAttachment(Attachment attachment) {
		commonDAO.updateAttachment(attachment);
	}
	
	@Override
	@Transactional
	public List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize) {
		return commonDAO.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceId, fileName, userId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Attachment getAttachmentById(Integer attachmentId) {
		return commonDAO.getAttachmentById(attachmentId);
	}

	@Override
	@Transactional
	public void deleteAttachment(Attachment attachment) {
		commonDAO.deleteAttachment(attachment);
	}

	@Override
	@Transactional
	public List<JsonAttachmentType> getAttachmentTypeForEntity(Integer entityTypeId) {
		List<AttachmentType> attachmentTypes = commonDAO.getAttachmentTypeForEntity(entityTypeId);
		List<JsonAttachmentType> jsonAttachmentTypes = new ArrayList<JsonAttachmentType>();
		for(AttachmentType attachmentType : attachmentTypes){
			JsonAttachmentType jsonAttachmentType = new JsonAttachmentType(attachmentType);
			jsonAttachmentTypes.add(jsonAttachmentType);
		}
		if(jsonAttachmentTypes.size() == 0){
			JsonAttachmentType jsonAttachmentType = new JsonAttachmentType();
			jsonAttachmentType.setAttachmentTypeId(0);
			jsonAttachmentType.setAttachmentTypeName(" -- ");
			jsonAttachmentTypes.add(jsonAttachmentType);
		}
		return jsonAttachmentTypes;
	}

	
	@Override
	@Transactional
	public Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId) {
		return commonDAO.getAttachmentOfEntityOrInstancePagination(entityTypeId, entityInstanceId, fileName, userId);
	}

	@Override
	@Transactional
	public List<Integer> getEntityInstanceIdsOfEntityByProductId(Integer productId, Integer entityTypeId) {
		List<Integer> entityInstanceIds = new ArrayList<Integer>();
		try {
			
			if(IDPAConstants.ENTITY_PRODUCT_VERSION_ID == entityTypeId){
				List<ProductVersionListMaster> productVersionListMasters = productVersionListMasterDAO.list(productId);
				if(productVersionListMasters != null && productVersionListMasters.size() > 0){
					for(ProductVersionListMaster productVersionListMaster : productVersionListMasters){
						entityInstanceIds.add(productVersionListMaster.getProductVersionListId());
					}
				}
			}
			
		} catch (Exception e) {
        	log.error("Error in getInstanceIdsOfEntityByProductId - "+entityTypeId, e);
		}
		return entityInstanceIds;
	}

	@Override
	@Transactional
	public List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, List<Integer> entityInstanceIds, Integer jtStartIndex, Integer jtPageSize) {
		return commonDAO.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, List<Integer> entityInstanceIds) {
		return commonDAO.getAttachmentOfEntityOrInstancePagination(entityTypeId, entityInstanceIds);
	}

	@Override
	public String getModifiedFieldOldValue(String modifiedFieldName, String entityTypeName, String tableName,
			String primaryKeyColumnName, String ColumnValue, String filter) {
		return commonDAO.getModifiedFieldOldValue(modifiedFieldName, entityTypeName, tableName, primaryKeyColumnName, ColumnValue, filter);
	}

	@Override
	@Transactional
	public List<Attachment> getAttachmentsConsolidatedForProduct(Integer productId, Integer jtStartIndex, Integer jtPageSize) {
		return commonDAO.getAttachmentsConsolidatedForProduct(productId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getAttachmentsConsolidatedForProductPagination(Integer productId) {
		return commonDAO.getAttachmentsConsolidatedForProductPagination(productId);
	}

	@Override
	public Integer getCountFromAttachmentCountDetails(List<Object[]> attachmentCountDetails, Integer entityInstanceId) {
		Integer attachmentCount = 0;
		try{
			if(attachmentCountDetails != null && attachmentCountDetails.size() > 0){
				for(Object[] attachmentCountDetail : attachmentCountDetails){
					Integer attachemnetDetailsInstanceId = (Integer)attachmentCountDetail[0];
					if(attachemnetDetailsInstanceId != null && entityInstanceId.equals(attachemnetDetailsInstanceId)){
						attachmentCount = ((Long)attachmentCountDetail[1]).intValue();
					}
				}
			}
		}catch(Exception ex){
			log.error("Exception in getCountFromAttachmentCountDetails - ", ex);
		}
		return attachmentCount;
	}

	@Override
	public List<Object[]> getAttachmentCountOfEntity(Integer entityTypeId) {
		return commonDAO.getAttachmentCountOfEntity(entityTypeId);
	}

	@Override
	@Transactional
	public List<Comments> getCommentsOfEntityIDorEntityIDList(Integer entityTypeId,	Integer entityInstanceId, 
			List<Integer> entityIdList,String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize){
		return commonDAO.getCommentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, 
				entityIdList, fileName, userId, jtStartIndex, jtPageSize);
		
	}
	@Override
	@Transactional
	public Integer getCommentsCountOfEntityOrInstancePagination(Integer entityTypeId,
			Integer entityInstanceId, String fileName, Integer userId){
		return commonDAO.getCommentsCountOfEntityOrInstancePagination(entityTypeId, entityInstanceId, fileName, userId);
	}



	@Override
	public List<JsonSimpleOption> getColumnByFrequency(String frequencyType, Integer frequencyMonth, Integer frequencyYear) {
		List<JsonSimpleOption> columnNames = new ArrayList<JsonSimpleOption>();
		try{
			Calendar calendar = Calendar.getInstance();
			if("Daily".equalsIgnoreCase(frequencyType)){
				Integer dayOfMonth = 1;
				calendar.setMinimalDaysInFirstWeek(7);
				calendar.set(Calendar.YEAR, frequencyYear);
				calendar.set(Calendar.MONTH, frequencyMonth);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				String[] dayOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
				while(calendar.get(Calendar.MONTH) == frequencyMonth){
					columnNames.add(new JsonSimpleOption("Day&nbsp;"+calendar.get(Calendar.DAY_OF_MONTH)+" "+dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) - 1)]));
					dayOfMonth++;
					calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				}
			}else if("Weekly".equalsIgnoreCase(frequencyType)){
				
		        calendar.clear();
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				int week = 1;
				int i = 0;
				String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
				
				Boolean isValidYear = true;
				while(isValidYear){
					i++;
					calendar.set(Calendar.WEEK_OF_YEAR, i);
					calendar.set(Calendar.YEAR, frequencyYear);
					if(i == 1 && calendar.get(Calendar.MONTH) != Calendar.JANUARY){
						continue;
					}
					if(calendar.get(Calendar.YEAR) == frequencyYear){
						columnNames.add(new JsonSimpleOption("W&nbsp;"+week+" "+months[calendar.get(Calendar.MONTH)]+"&nbsp;"+calendar.get(Calendar.DAY_OF_MONTH)));	
						week++;
					}else{
						isValidYear = false;
					}
				}
				
			}
		}catch(Exception ex){
			log.error("Error in getColumnByFrequency - ", ex);
		}
		return columnNames;
	}

	@Override
	@Transactional
	public void addComments(Comments comments) {
		commonDAO.addComments(comments);
		
	}

	@Override
	public Date getModifiedAfterDate(Date loginDate, Date logoutDate) {
		Date modifiedAfterDate = null;
		try{
			if(logoutDate != null){
				modifiedAfterDate = logoutDate;
			}else if(loginDate != null){
				modifiedAfterDate = loginDate;
			}
		}catch(Exception ex){
			log.error("Error in getModifiedAfterDate - ", ex);
		}
		return modifiedAfterDate;
	}

	@Override
	public Boolean isModifiedAfterDate(Date instanceModifiedDate, Date modifiedAfterDate){
		Boolean isModified = false;
		try{
			if(instanceModifiedDate != null && modifiedAfterDate != null && (instanceModifiedDate.after(modifiedAfterDate) || instanceModifiedDate.equals(modifiedAfterDate))){
				isModified =  true;
			}
		}catch(Exception ex){
			log.error("Error in isModifiedAfterDate - ", ex);
		}
		return isModified;
	}
	

	@Override
	@Transactional
	public Object autoAllocationOfResource(Object instanceObject){
		try{
			String tableName = "";
			String resourceTypeName = "";
			String workloadField = "";
			if(instanceObject instanceof Activity){
				tableName = "activity";
				workloadField = "plannedActivitySize";
				Integer userId  = null;
				Activity activity = (Activity) instanceObject;
				if(activity.getAssignee() != null && activity.getAssignee().getUserId() != null && activity.getAssignee().getUserId() == -4){
					resourceTypeName = "assigneeId";
					if(activity.getAutoAllocateReferenceId() != null && activity.getAutoAllocateReferenceId() > 0){
					//	userId = getUserByAutoAllocationPolicy(activity.getAutoAllocateReferenceId(), tableName, resourceTypeName, activity.getProductMaster().getProductId(), DateUtility.dateToStringInSecond(activity.getPlannedStartDate()), DateUtility.dateToStringInSecond(activity.getPlannedEndDate()), workloadField);
					}else{
						log.info("No auto allocation bot chosen ");
					}
					
					if(userId != null){
						UserList user = new UserList();
						user.setUserId(userId);
						activity.setAssignee(user);
					}
				}
				
				userId  = null;
				if(activity.getReviewer() != null && activity.getReviewer().getUserId() != null && activity.getReviewer().getUserId() == -4){
					resourceTypeName = "reviewerId";
					if(activity.getAutoAllocateReferenceId() != null && activity.getAutoAllocateReferenceId() > 0){
						//userId = getUserByAutoAllocationPolicy(activity.getAutoAllocateReferenceId(), tableName, resourceTypeName, activity.getProductMaster().getProductId(), DateUtility.dateToStringInSecond(activity.getPlannedStartDate()), DateUtility.dateToStringInSecond(activity.getPlannedEndDate()), workloadField);
					}else{
						log.info("No auto allocation bot chosen ");
					}
					
					if(userId != null){
						UserList user = new UserList();
						user.setUserId(userId);
						activity.setReviewer(user);
					}
				}
				
				instanceObject = activity;
			}
		}catch(Exception ex){
			log.error("Error occured in autoAllocationOfResource - ", ex);
		}
		return instanceObject;
	}
	
	
	@Override
	@Transactional
	public String getInstanceAndAutoAllocationOfResource(Integer entityId, Integer entityInstanceId, Integer parentEntityId, Integer parentEntityInstanceId, UserList user){
		String message = "Auto allocation completed";
		TestFactory testFactory = null;
		ProductMaster productMaster = null;
		ActivityWorkPackage activityWorkPackage = null;
		String remarks = "";
		try{
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				List<Activity> activities = new ArrayList<Activity>();
				if(entityInstanceId != null && entityInstanceId != 0){
					Activity activity = activityDAO.getByActivityId(entityInstanceId);
					if(activity != null){
						activities.add(activity);
					}
				}else if(parentEntityId != null && parentEntityId != 0 && parentEntityInstanceId != null && parentEntityInstanceId != 0){
					activities = activityDAO.getActivitiesForAutoAllocation(parentEntityId, parentEntityInstanceId);
				}
				
				if(activities != null && activities.size() > 0){
					Integer assigneeId = 0;
					Integer reviewerId = 0;
					for(Activity activity : activities){
						if((activity.getAssignee() != null && activity.getAssignee().getUserId() == -4) || (activity.getReviewer() != null && activity.getReviewer().getUserId() == -4)){
							assigneeId = 0;
							reviewerId = 0;
														
							if(activity.getAssignee() != null){
								assigneeId = activity.getAssignee().getUserId();
							}
							if(activity.getReviewer() != null){
								reviewerId = activity.getReviewer().getUserId();
							}
							autoAllocationOfResource(activity);							
							if((activity.getAssignee() != null && assigneeId != activity.getAssignee().getUserId()) || (activity.getReviewer() != null && reviewerId != activity.getReviewer().getUserId())){
								activity.setModifiedDate(new Date());
								activity.setModifiedBy(user);
								activityDAO.updateActivity(activity);
								mongoDBService.addActivitytoMongoDB(activity.getActivityId());
								
								activityWorkPackage = activity.getActivityWorkPackage();
								productMaster = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();
								testFactory = productMaster.getTestFactory();
								remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ActivityWorkpackage :"+activityWorkPackage.getActivityWorkPackageName()+", Activity :"+activity.getActivityName();
								if(activity.getAssignee() != null && assigneeId != activity.getAssignee().getUserId()){
									eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY, activity.getActivityId(), activity.getActivityName(),
											"assigneeId", "Assignee",
											assigneeId.toString(), activity.getAssignee().getUserId().toString(), user, remarks);
								}
								if(activity.getReviewer() != null && reviewerId != activity.getReviewer().getUserId()){
									eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY, activity.getActivityId(), activity.getActivityName(),
											"reviewerId", "Reviewer",
											reviewerId.toString(), activity.getReviewer().getUserId().toString(), user, remarks);
								}
							}
						}
					}
				}
			}
		}catch(Exception ex){
			log.error("Error occured in getInstanceAndAutoAllocationOfResource - ", ex);
		}
		return message;
	}

	@Override
	@Transactional
	public List<Comments> getCommentsListBasedOnDateFillter(Integer testFactoryId,Integer productId,Integer wpId,Integer entityTypeId,Date fromDate,Date toDate) {
		List<Activity> activities =new ArrayList<Activity>();
		List<Integer>ids = new ArrayList<Integer>();
		if(entityTypeId.equals(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID)){
			activities=	activityDAO.getActivitiesBasedOnTestFactoryIdProductIdandAwpId(testFactoryId, productId, wpId);
			if(activities != null & activities.size() > 0){
				for(Activity activity :activities ){
					ids.add(activity.getActivityId());
				}
			}
		}
		
		return commonDAO.getCommentsListBasedOnDateFillter(ids,entityTypeId,fromDate,toDate);
	}
	
	public void writeUploadReportFile(Workbook extractionReportWorkbook, String reportLocation, String reportName, ProductMaster product){
		try{
			File filePath = new File(reportLocation);
			if(filePath == null || !filePath.exists() || !filePath.isDirectory()){
				filePath.mkdirs();
			}
			filePath = new File(reportLocation+"\\"+reportName);
			FileOutputStream fos = new FileOutputStream(filePath);
			extractionReportWorkbook.write(fos);
		}catch(Exception ex){
			log.error("Exception occured in writeUploadReportFile - ", ex);
		}
	}
	
	@Override
	public void createAttachment(ProductMaster product, String reportName, String reportLocation){
		Attachment attachment = new Attachment();
		attachment.setProduct(product);
		attachment.setEntityId(product.getProductId());
		EntityMaster entityMaster = new EntityMaster();
		entityMaster.setEntitymasterid(IDPAConstants.PRODUCT_ENTITY_MASTER_ID);
		attachment.setEntityMaster(entityMaster);
		attachment.setAttachmentType("UploadReport");
		attachment.setDescription("DataUpload");
		String ext = reportName.substring(reportName.lastIndexOf(".")).toLowerCase();
		attachment.setAttributeFileExtension(ext);
		String fileName = reportName.substring(0, reportName.lastIndexOf("."));
		attachment.setAttachmentName(fileName);
		attachment.setAttributeFileName(fileName);
		attachment.setAttributeFileURI(reportLocation+"\\"+reportName);
		UserList userList = new UserList();
		userList.setUserId(1);
		attachment.setCreatedBy(userList);
		attachment.setModifiedBy(userList);
		attachment.setUploadedDate(new Date());
		attachment.setModifiedDate(new Date());
		attachment.setLastModifiedDate(new Date());
		attachment.setStatus(1);
		attachment.setAttachmentPrefixName(reportName.substring(0, reportName.lastIndexOf(".")));
		File file = new File(reportLocation+"\\"+reportName);
		if(file != null && file.exists() && file.isFile()){
			Long size = file.length();
			String fileSize = "0 MB";
			if(size > 0){
				fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
			}
			attachment.setAttributeFileSize(fileSize);
		}
		List<Attachment> existingAttachments = getAttachmentOfEntityOrInstance(IDPAConstants.PRODUCT_ENTITY_MASTER_ID, product.getProductId(), fileName, userList.getUserId(), null, null);
		if(existingAttachments != null && existingAttachments.size() > 0){
			return;
		}
		addAttachment(attachment);
	}

	@Override
	@Transactional
	public Comments getLatestCommentOfByEntityTypeAndInstanceId(Integer entityTypeId,Integer instanceId) {
		return commonDAO.getLatestCommentOfByEntityTypeAndInstanceId(entityTypeId,instanceId);
	}
	
	@Override
	@Transactional
	public Comments getLatestCommentOfByEntityType(Integer entityTypeId) {		
		return commonDAO.getLatestCommentOfByEntityType(entityTypeId);
	}

	@Override
	public List<Comments> getCommentsListBasedOnDateFillterAndComment(Integer productId,
			Integer entityTypeId, Date fromDate, Date toDate, String comment) {
		try {
			return commonDAO.getCommentsListBasedOnDateFillterAndComment(productId,entityTypeId, fromDate, toDate, comment);
		}catch(Exception e) {
			
		}
		return null;
	}
}
