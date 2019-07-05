package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.ClarificationTrackerDAO;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TransactionClarification;
import com.hcl.atf.taf.model.json.JsonClarificationTracker;
import com.hcl.atf.taf.service.ClarificationTrackerService;
import com.hcl.atf.taf.service.CommonService;

@Service
public class ClarificationTrackerServiceImpl implements ClarificationTrackerService {
	private static final Log log = LogFactory.getLog(ClarificationTrackerServiceImpl.class);
	@Autowired
	ClarificationTrackerDAO clarificationTrackerDAO;
	@Autowired
	ActivityDAO activityDao; 
	@Autowired
	ProductMasterDAO productMasterDAO;
	@Autowired
	TestFactoryDao testFactoryDao;
	@Autowired
	CommonService commonService;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	AttachmentDAO attachmentDAO;
	@Autowired
	ActivityWorkPackageDAO activityWorkPackageDAO; 
	
	@Override
	@Transactional
	public ClarificationTracker getClarificationTrackersById(
			int clarificationTrackerId,Integer initializelevel) {
		return clarificationTrackerDAO.getlistClarificationTrackersById(clarificationTrackerId, initializelevel);
	}

	@Override
	@Transactional
	public Integer addClarificationTracker(
			ClarificationTracker clarificationTracker) {
		return clarificationTrackerDAO.addClarificationTracker(clarificationTracker);
		
	}

	@Override
	@Transactional
	public void updateClarificationTracker(
			ClarificationTracker clarificationTracker) {
		clarificationTrackerDAO.updateClarificationTracker(clarificationTracker);
		
	}

	@Override
	@Transactional
	public void deleteClarificationTracker(
			ClarificationTracker clarificationTracker) {
		clarificationTrackerDAO.deleteClarificationTracker(clarificationTracker);
		
	}
	@Override
	@Transactional
	public List<JsonClarificationTracker> listClarificationTrackerByActivityId(Integer entityTypeId, Integer entityInstanceId, Integer initializationLevel) {
		List<JsonClarificationTracker> listOfJsonClarificationTrackers = new ArrayList<JsonClarificationTracker>();
		List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID);
		List<ClarificationTracker> listOfClarificationTrackers = clarificationTrackerDAO.listClarificationTrackersByActivityId(entityTypeId, entityInstanceId, initializationLevel);
		if(listOfClarificationTrackers != null && listOfClarificationTrackers.size()>0){
			for (ClarificationTracker clarificationTracker : listOfClarificationTrackers) {
				Activity activity = null;
				JsonClarificationTracker jsonClarificationTracker = new JsonClarificationTracker(clarificationTracker);
				if(clarificationTracker.getEntityInstanceId() != null){
						if(clarificationTracker.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						activity = activityDao.getActivityById(clarificationTracker.getEntityInstanceId(),0);
						jsonClarificationTracker.setEntityInstanceName(activity.getActivityName());
						}
				}
				jsonClarificationTracker.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonClarificationTracker.getClarificationTrackerId()));
				listOfJsonClarificationTrackers.add(jsonClarificationTracker);
			}
		}
		return listOfJsonClarificationTrackers;
	}


	@Override
	@Transactional
	public List<JsonClarificationTracker> listClarificationsByAWPId(Integer entityTypeId, Integer entityInstanceId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		List<JsonClarificationTracker> jsonClarificationTrackersList = new ArrayList<JsonClarificationTracker>();
		List<ClarificationTracker> listOfClarificationTrackers = clarificationTrackerDAO.listClarificationsByActivityWorkPackageId(entityTypeId, entityInstanceId, jtStartIndex, jtPageSize, initializationLevel);
		if(listOfClarificationTrackers != null && listOfClarificationTrackers.size()>0){
			for (ClarificationTracker clartObj : listOfClarificationTrackers) {
				
				JsonClarificationTracker jsonClarificationTracker = new JsonClarificationTracker(clartObj);
				if(clartObj.getEntityType() != null){
					if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						TestFactory testFactory = testFactoryDao.getTestFactoryById(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(testFactory.getTestFactoryName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(productMaster.getProductName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						ActivityWorkPackage awp = activityWorkPackageDAO.getActivityWorkPackageById(clartObj.getEntityInstanceId(), 3);
						jsonClarificationTracker.setEntityInstanceName(awp.getActivityWorkPackageName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDao.getByActivityId(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(activity.getActivityName());
					}
				}
				int attachCount = attachmentDAO.getAttachmentsCountByentityMasterId(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID, jsonClarificationTracker.getClarificationTrackerId(), -1,-1);
				jsonClarificationTracker.setAttachmentCount(attachCount);
				
				jsonClarificationTrackersList.add(jsonClarificationTracker);
			}
		}
		return jsonClarificationTrackersList;
	}
	

	@Override
	@Transactional
	public List<JsonClarificationTracker> listClarificationsByProductId(Integer entityTypeId, Integer entityInstanceId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		List<JsonClarificationTracker> jsonClarificationTrackersList = new ArrayList<JsonClarificationTracker>();
		List<ClarificationTracker> listOfClarificationTrackers = clarificationTrackerDAO.listClarificationsByProductId(entityTypeId, entityInstanceId, jtStartIndex, jtPageSize, initializationLevel);
		if(listOfClarificationTrackers != null && listOfClarificationTrackers.size()>0){
			for (ClarificationTracker clartObj : listOfClarificationTrackers) {				
				JsonClarificationTracker jsonClarificationTracker = new JsonClarificationTracker(clartObj);
				if(clartObj.getEntityType() != null){
					if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						TestFactory testFactory = testFactoryDao.getTestFactoryById(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(testFactory.getTestFactoryName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						ProductMaster productMaster = productMasterDAO.getByProductId(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(productMaster.getProductName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						ActivityWorkPackage awp = activityWorkPackageDAO.getActivityWorkPackageById(clartObj.getEntityInstanceId(), 3);
						jsonClarificationTracker.setEntityInstanceName(awp.getActivityWorkPackageName());
					}else if(clartObj.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						Activity activity = activityDao.getByActivityId(clartObj.getEntityInstanceId());
						jsonClarificationTracker.setEntityInstanceName(activity.getActivityName());
					}
				}
				int attachCount = attachmentDAO.getAttachmentsCountByentityMasterId(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID, jsonClarificationTracker.getClarificationTrackerId(), -1,-1);
				jsonClarificationTracker.setAttachmentCount(attachCount);
				
				jsonClarificationTrackersList.add(jsonClarificationTracker);
			}
		}
		return jsonClarificationTrackersList;
	}
    @Override
	@Transactional
	public List<JsonClarificationTracker>  listJsonClarificationTrackers() {
		List<ClarificationTracker> clarificationTrackerlist = null;
		List<JsonClarificationTracker> jsonClarificationTrackerlist = new ArrayList<JsonClarificationTracker>();
		try {
			clarificationTrackerlist =clarificationTrackerDAO.listJsonClarificationTrackers();
			if(clarificationTrackerlist != null &&clarificationTrackerlist.size()>0){
				for(ClarificationTracker wr: clarificationTrackerlist){
					jsonClarificationTrackerlist.add(new JsonClarificationTracker(wr));	
				}
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonClarificationTrackerlist;
	}

	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationTrackersByProductId(Integer productId) {
		return clarificationTrackerDAO.listClarificationTrackersByProductId(productId);
	}

	@Override
	@Transactional
	public List<ClarificationTypeMaster> listClarificationType() {		
		return clarificationTrackerDAO.listClarificationType();
	}

	@Override
	@Transactional
	public List<EntityStatus> listClarificationStatus(Integer entityTypeId) {
		
		return clarificationTrackerDAO.listClarificationStatus(entityTypeId);
	}

	@Override
	@Transactional
	public List<EntityStatus> listClarificationResolutionStatus(Integer parentStatusId) {
		return clarificationTrackerDAO.listClarificationResolutionStatus(parentStatusId);
	}

	@Override
	@Transactional
	public List<ClarificationScope> getClarificationScope() {		
		return clarificationTrackerDAO.getClarificationScope();
			
	}

	
	@Override
	@Transactional
	public Integer countAllClarification(Date startDate, Date endDate) {
		return clarificationTrackerDAO.countAllClarification(startDate,endDate);
	}

	@Override
	@Transactional
	public List<ClarificationTracker> listAllClarificationByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate) {
		return clarificationTrackerDAO.listAllClarificationByLastSyncDate(startIndex,pageSize,startDate,endDate);
	}
	
	@Override
	@Transactional
	public List<JsonClarificationTracker> listClarificationsByEngagementAndProductIds(int testFactoryId, int productId){
		List<JsonClarificationTracker> listOfJsonClarificationTrackers = new ArrayList<JsonClarificationTracker>();
		List<ClarificationTracker> listOfClarificationTrackers = clarificationTrackerDAO.listClarificationsByEngagementAndProductIds(testFactoryId, productId);
		if(listOfClarificationTrackers != null && listOfClarificationTrackers.size()>0){
			List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID);
			for (ClarificationTracker clarificationTracker : listOfClarificationTrackers) {
				JsonClarificationTracker jsonClarificationTracker = new JsonClarificationTracker(clarificationTracker);
				if(clarificationTracker.getEntityType() != null){
					if(clarificationTracker.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
						if(clarificationTracker.getEntityInstanceId() != null){
						TestFactory testFactory = testFactoryDao.getTestFactoryById(clarificationTracker.getEntityInstanceId());
						  if(testFactory != null){
							  jsonClarificationTracker.setEntityInstanceName(testFactory.getTestFactoryName());
						  }
						}
					}else if(clarificationTracker.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
						if(clarificationTracker.getEntityInstanceId() != null){
							ProductMaster productMaster = productMasterDAO.getByProductId(clarificationTracker.getEntityInstanceId());
							if(productMaster != null){
								jsonClarificationTracker.setEntityInstanceName(productMaster.getProductName());
									}
						}
					}else if(clarificationTracker.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){						
						if(clarificationTracker.getEntityInstanceId() != null){
						Activity activity = activityDao.getByActivityId(clarificationTracker.getEntityInstanceId());
							if(activity != null){
									jsonClarificationTracker.setEntityInstanceName(activity.getActivityName());
							}
						}
						
					}
				}
				jsonClarificationTracker.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonClarificationTracker.getClarificationTrackerId()));
				listOfJsonClarificationTrackers.add(jsonClarificationTracker);
			}
		}
	
		return listOfJsonClarificationTrackers;
	}

	@Override
	@Transactional
	public void addTransactionClarification(TransactionClarification transactionClarification) {
		clarificationTrackerDAO.addTransactionClarification(transactionClarification);
		
	}

	@Override
	@Transactional
	public List<TransactionClarification> listTransactionClarification(Integer clarificationTrackerId) {
		return clarificationTrackerDAO.listTransactionClarification(clarificationTrackerId);
	}

	@Override
	@Transactional
	public void updateTransactionClarification(TransactionClarification transactionClarificationUI) {
		clarificationTrackerDAO.updateTransactionClarification(transactionClarificationUI);	
	}
	
	@Override
	@Transactional
	public String deleteClarificationTracker(Integer clarificationTrackerId){		
		return clarificationTrackerDAO.deleteClarificationTracker(clarificationTrackerId);		
	}
	
	@Override
	@Transactional
	public Integer getcountOfClarificationByEntityTypeAndInstanceIds(Integer entityTypeId, Integer entityInstanceId) {
		return clarificationTrackerDAO.getcountOfClarificationByEntityTypeAndInstanceIds(entityTypeId, entityInstanceId);
	}

	@Override
	@Transactional
	public Integer getClarificationTrackerCountFromProductHierarchy(
			Integer productId, Boolean isHierarchy, Integer jtStartIndex,
			Integer jtPageSize) {
		return clarificationTrackerDAO.getClarificationTrackerCountFromProductHierarchy(productId, isHierarchy, jtStartIndex, jtPageSize);
	}
	
}
