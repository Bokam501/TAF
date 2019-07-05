package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.ChangeRequestDAO;
import com.hcl.atf.taf.dao.ClarificationTrackerDAO;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.json.JsonChangeRequest;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EntityRelationshipService;
@Service
public class ChangeRequestServiceImpl implements ChangeRequestService {
	private static final Log log = LogFactory.getLog(ChangeRequestServiceImpl.class);
	@Autowired
	ChangeRequestDAO changeRequestDAO;
	@Autowired
	EntityRelationshipService entityRelationshipService;
	@Autowired
	CommonService commonService;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ProductMasterDAO productMasterDAO;
	@Autowired
	ActivityDAO activityDAO;
	@Autowired
	ClarificationTrackerDAO clarificationTrackerDAO;
	@Autowired
	TestFactoryDao testFactoryDao;
	@Autowired
	AttachmentDAO attachmentDAO;
	@Autowired
	ActivityWorkPackageDAO activityWorkPackageDAO; 
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
    private String maxBatchCount;
	
	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequests() {
		List<ChangeRequest> listChangeRequest = null;
		List<JsonChangeRequest> listJsonChangeRequest = new ArrayList<JsonChangeRequest>();
		try {
			listChangeRequest = changeRequestDAO.listChangeRequests();
			for(ChangeRequest wr: listChangeRequest){
				listJsonChangeRequest.add(new JsonChangeRequest(wr));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return listChangeRequest;
	}

	@Override
	@Transactional
	public ChangeRequest getChangeRequestById(int ChangeRequestId) {
		return changeRequestDAO.getChangeRequestById(ChangeRequestId);
		
	}

	
	@Override
	@Transactional
	public Integer addChangeRequest(ChangeRequest changeRequest) {
		return changeRequestDAO.addChangeRequest(changeRequest);
		
	}
	
	

	@Override
	@Transactional
	public void updateChangeRequest(ChangeRequest changeRequest) {
		changeRequestDAO.updateChangeRequest(changeRequest);
		
	}

	@Override
	public void deleteChangeRequest(ChangeRequest changeRequest) {
		changeRequestDAO.deleteChangeRequest(changeRequest);
		
	}

	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestsByActivityId(
			Integer activityId, Integer initializationLevel) {
		List<JsonChangeRequest> listOfJsonChangeRequests = new ArrayList<JsonChangeRequest>();
		List<ChangeRequest> listOfChangeRequests = changeRequestDAO.listChangeRequestsByActivityId(activityId, initializationLevel);
		if(listOfChangeRequests != null && listOfChangeRequests.size()>0){
			for (ChangeRequest changeRequest : listOfChangeRequests) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
				listOfJsonChangeRequests.add(jsonChangeRequest);
			}
		}
		return listOfJsonChangeRequests;
	}

	
	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1) {	
		return changeRequestDAO.listChangeRequestByEntityTypeAndInstanceIds(entityType1, entityInstance1);
	}
	
	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestByProductId(Integer entityType1, Integer entityInstance1,
			Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		List<JsonChangeRequest> jsonChangeRequestList = new ArrayList<JsonChangeRequest>();
		List<ChangeRequest> changeRequestList = new ArrayList<ChangeRequest>();
		changeRequestList = changeRequestDAO.listChangeRequestByProductId(entityType1, entityInstance1, jtStartIndex, jtPageSize, initializationLevel);
		if(changeRequestList != null && !changeRequestList.isEmpty()){
			for (ChangeRequest changeRequest : changeRequestList) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
				if(changeRequest.getEntityType() != null){	
					 if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						 TestFactory testFactory = testFactoryDao.getTestFactoryById(changeRequest.getEntityInstanceId());
						 jsonChangeRequest.setEntityInstanceName(testFactory.getTestFactoryName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(productMaster.getProductName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						 ActivityWorkPackage awp = activityWorkPackageDAO.getActivityWorkPackageById(changeRequest.getEntityInstanceId(), 3);
							jsonChangeRequest.setEntityInstanceName(awp.getActivityWorkPackageName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDAO.getByActivityId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(activity.getActivityName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID){
						 ClarificationTracker clarificationTracker = clarificationTrackerDAO.getlistClarificationTrackersById(changeRequest.getEntityInstanceId(), 1);
						 jsonChangeRequest.setEntityInstanceName(clarificationTracker.getClarificationTitle());
					 }
				}
				int attachCount = attachmentDAO.getAttachmentsCountByentityMasterId(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID, jsonChangeRequest.getChangeRequestId(), -1,-1);
				jsonChangeRequest.setAttachmentCount(attachCount);
				
				jsonChangeRequestList.add(jsonChangeRequest);
			}
		}
		return jsonChangeRequestList;
	}		

	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestByActivityWorkPackageId(Integer entityType1, Integer entityInstance1,
			Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		List<JsonChangeRequest> jsonChangeRequestList = new ArrayList<JsonChangeRequest>();
		List<ChangeRequest> changeRequestList = new ArrayList<ChangeRequest>();
		changeRequestList = changeRequestDAO.listChangeRequestByActivityWorkPackageId(entityType1, entityInstance1, jtStartIndex, jtPageSize, initializationLevel);
		if(changeRequestList != null && !changeRequestList.isEmpty()){
			for (ChangeRequest changeRequest : changeRequestList) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
				if(changeRequest.getEntityType() != null){	
					 if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						 TestFactory testFactory = testFactoryDao.getTestFactoryById(changeRequest.getEntityInstanceId());
						 jsonChangeRequest.setEntityInstanceName(testFactory.getTestFactoryName());
					 }
					 else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(productMaster.getProductName());
					 }
					 else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
							ActivityWorkPackage activityWorkPackage = activityWorkPackageDAO.getActivityWorkPackageById(changeRequest.getEntityInstanceId(), 1);
							jsonChangeRequest.setEntityInstanceName(activityWorkPackage.getActivityWorkPackageName());
					 }
					 else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDAO.getByActivityId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(activity.getActivityName());
					 }
					 else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID){
						 ClarificationTracker clarificationTracker = clarificationTrackerDAO.getlistClarificationTrackersById(changeRequest.getEntityInstanceId(), 1);
						 jsonChangeRequest.setEntityInstanceName(clarificationTracker.getClarificationTitle());
					 }
				}
				int attachCount = attachmentDAO.getAttachmentsCountByentityMasterId(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID, jsonChangeRequest.getChangeRequestId(), -1,-1);
				jsonChangeRequest.setAttachmentCount(attachCount);
				
				jsonChangeRequestList.add(jsonChangeRequest);
			}
		}
		return jsonChangeRequestList;
	}		
		
	@Override
	public int addChangeRequestBulk(List<ChangeRequest> listOfChangeRequestToAdd) {
		return changeRequestDAO.addChangeRequestBulk(listOfChangeRequestToAdd, Integer.parseInt(maxBatchCount));
	}

	@Override
	@Transactional
	public List<String> getExistingChangeRequestNames(ProductMaster product) {
		return changeRequestDAO.getExistingChangeRequestNames(product);
	}

	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestsByProductId(Integer productId,Integer status,
			Integer initializationLevel, Integer jtStartIndex,
			Integer jtPageSize) {
		List<JsonChangeRequest> listOfJsonChangeRequests = new ArrayList<JsonChangeRequest>();
		List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
		List<ChangeRequest> listOfChangeRequests = changeRequestDAO.listChangeRequestsByProductId(productId,status, initializationLevel,jtStartIndex,jtPageSize);
		if(listOfChangeRequests != null && listOfChangeRequests.size()>0){
			for (ChangeRequest changeRequest : listOfChangeRequests) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
				if(changeRequest.getEntityType() != null){
					if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(productMaster.getProductName());
					}else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDAO.getByActivityId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(activity.getActivityName());
					}else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID){
						ClarificationTracker clarificationTracker = clarificationTrackerDAO.getlistClarificationTrackersById(changeRequest.getEntityInstanceId(), 1);
						jsonChangeRequest.setEntityInstanceName(clarificationTracker.getClarificationTitle());
					}
				}
				jsonChangeRequest.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonChangeRequest.getChangeRequestId()));
				listOfJsonChangeRequests.add(jsonChangeRequest); 
			}
		}
		return listOfJsonChangeRequests;
	}
	@Override
	@Transactional
	public void updateChangeRequesttoActivity(ChangeRequest changeRequest) {
		changeRequestDAO.updateChangeRequesttoActivity(changeRequest);
		
	}
	
	@Override
	@Transactional
	public ChangeRequest getChangeRequestByName(String changeRequestName) {
		return changeRequestDAO.getChangeRequestByName(changeRequestName);
	}
	
	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestsByentityInstanceId(Integer entityType1,Integer entityType2,Integer entityInstanceId1){
		List<ChangeRequest> listChangeRequest = null;
		List<JsonChangeRequest> listJsonChangeRequest = new ArrayList<JsonChangeRequest>();
		try{
			List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
			List<Integer> changeRequestIds = entityRelationshipService.listEntityInstance2ByEntityInstance1(entityType1, entityType2, entityInstanceId1);
			if(changeRequestIds != null && changeRequestIds.size() >0){
			listChangeRequest = changeRequestDAO.listChangeRequestsByIds(changeRequestIds);
				for(ChangeRequest changeRequest : listChangeRequest){
					JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
					jsonChangeRequest.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonChangeRequest.getChangeRequestId()));
					listJsonChangeRequest.add(jsonChangeRequest);
				}
			}
		}catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return listJsonChangeRequest;
	}

	@Override
	@Transactional
	public List<ChangeRequestType> getChangeRequestType() {
		
		return changeRequestDAO.getChangeRequestType();
	}
	
	@Override
	public List<Object[]> getRcnByProductId(int productId, int jtStartIndex, int jtPageSize, int i) {
		return changeRequestDAO.getRcnByProductId(productId, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public Integer countAllChangeRequest(Date startDate, Date endDate) {
		return changeRequestDAO.countAllChangeRequest(startDate,endDate);
	}
	
	@Override
	@Transactional
	public List<ChangeRequest> listAllChangeRequestByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate) {
		return changeRequestDAO.listAllChangeRequestByLastSyncDate(startIndex,pageSize,startDate,endDate);
	}
	
	@Override
	@Transactional
	public List<JsonChangeRequest> listChangeRequestsByEngagementAndProductIds(int testFactoryId, int productId){
		List<JsonChangeRequest> listOfJsonChangeRequests = new ArrayList<JsonChangeRequest>();
		List<ChangeRequest> listOfChangeRequests = new ArrayList<ChangeRequest>();
		if(testFactoryId !=0 && productId == 0){
			List<ProductMaster> listOfProductMasters = productMasterDAO.getProductsByTestFactoryId(testFactoryId);
			if(listOfProductMasters != null && listOfProductMasters.size() > 0){
				for(ProductMaster productMaster : listOfProductMasters){
					List<ChangeRequest> listOfChangeRequestsByProduct = changeRequestDAO.listChangeRequestsByProductId(productMaster.getProductId(), null, 1, null, null);
					for(ChangeRequest changeRequest : listOfChangeRequestsByProduct){
						listOfChangeRequests.add(changeRequest);
					}
				}
			}
		}
		else if(productId != 0){
			listOfChangeRequests = changeRequestDAO.listChangeRequestsByProductId(productId, null, 1, null, null);
		}
		if(listOfChangeRequests != null && listOfChangeRequests.size()>0){
			List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID);
			for (ChangeRequest changeRequest : listOfChangeRequests) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
				if(changeRequest.getEntityType() != null){	
					 if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						 TestFactory testFactory = testFactoryDao.getTestFactoryById(changeRequest.getEntityInstanceId());
						 jsonChangeRequest.setEntityInstanceName(testFactory.getTestFactoryName());
					 }
					 else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(productMaster.getProductName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDAO.getByActivityId(changeRequest.getEntityInstanceId());
						jsonChangeRequest.setEntityInstanceName(activity.getActivityName());
					 }else if(changeRequest.getEntityType().getEntitymasterid() == IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID){
						 ClarificationTracker clarificationTracker = clarificationTrackerDAO.getlistClarificationTrackersById(changeRequest.getEntityInstanceId(), 1);
						 jsonChangeRequest.setEntityInstanceName(clarificationTracker.getClarificationTitle());
					 }
				}
				jsonChangeRequest.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonChangeRequest.getChangeRequestId()));
				listOfJsonChangeRequests.add(jsonChangeRequest);
			}
		}
		return listOfJsonChangeRequests;
	}
	
	@Override
	@Transactional
	public Integer getcountOfChangeRequestsByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1) {
		return changeRequestDAO.getcountOfChangeRequestsByEntityTypeAndInstanceIds(entityType1, entityInstance1);
	}

	@Override
	@Transactional
	public Integer getChangeRequestCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize) {
		return changeRequestDAO.getChangeRequestCountFromProductHierarchy(productId, isHierarchy, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<ChangeRequest> listCRByEntityTypeAndInstanceIds(
			Integer entityType1, Integer entityType2, Integer entityInstance1) {
		return changeRequestDAO.listCRByEntityTypeAndInstanceIds(entityType1, entityType2, entityInstance1);
	}
	
	
}
