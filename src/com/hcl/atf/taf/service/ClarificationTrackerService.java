package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.TransactionClarification;
import com.hcl.atf.taf.model.json.JsonClarificationTracker;

public interface ClarificationTrackerService {

	ClarificationTracker getClarificationTrackersById(int clarificationTrackerId,Integer initializationLevel);
	Integer addClarificationTracker(ClarificationTracker clarificationTracker);
	void updateClarificationTracker(ClarificationTracker clarificationTracker);
	void deleteClarificationTracker(ClarificationTracker clarificationTracker);
	List<JsonClarificationTracker> listClarificationTrackerByActivityId(
			Integer entityTypeId, Integer entityInstanceId, Integer initializationLevel);
    List<JsonClarificationTracker> listJsonClarificationTrackers();
    
    List<ClarificationTracker> listClarificationTrackersByProductId(Integer productId);   
    List<ClarificationTypeMaster> listClarificationType();    
    List<EntityStatus>  listClarificationStatus(Integer entityTypeId);
    
    List<EntityStatus>   listClarificationResolutionStatus(Integer parentStatusId);
    
    List<ClarificationScope> getClarificationScope();
    
    Integer countAllClarification(Date startDate, Date endDate);
	List<ClarificationTracker> listAllClarificationByLastSyncDate(int startIndex,int pageSize, Date startDate, Date endDate);
	List<JsonClarificationTracker> listClarificationsByEngagementAndProductIds(int testFactoryId, int productId);
	void addTransactionClarification(TransactionClarification transactionClarification);
	List<TransactionClarification> listTransactionClarification(Integer clarificationTrackerId);
	void updateTransactionClarification(TransactionClarification transactionClarificationUI);	
	String deleteClarificationTracker(Integer clarificationTrackerId);
	List<JsonClarificationTracker> listClarificationsByAWPId(Integer entityTypeId, Integer entityInstanceId,
			Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel);
	Integer getcountOfClarificationByEntityTypeAndInstanceIds(Integer entityTypeId, Integer entityInstanceId);
	List<JsonClarificationTracker> listClarificationsByProductId(Integer entityTypeId, Integer entityInstanceId,
			Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel);
	Integer getClarificationTrackerCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize);
}
