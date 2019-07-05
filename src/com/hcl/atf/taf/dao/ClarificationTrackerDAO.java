package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.TransactionClarification;

public interface ClarificationTrackerDAO {
	
	ClarificationTracker getlistClarificationTrackersById(int clarificationTracker,Integer initializationlevel);
	Integer addClarificationTracker(ClarificationTracker clarificationTracker);
	void updateClarificationTracker(ClarificationTracker clarificationTracker);
	void deleteClarificationTracker(ClarificationTracker clarificationTracker);
	List<ClarificationTracker> listClarificationTrackersByActivityId(
			Integer entityTypeId, Integer entityInstanceId, Integer initializationLevel);
	List<ClarificationTracker> listJsonClarificationTrackers();
	List<ClarificationTracker> listClarificationTrackersByProductId(Integer productId);
	List<ClarificationTypeMaster> listClarificationType();	
	List<EntityStatus> listClarificationStatus(Integer entityTypeId);
	List<EntityStatus>listClarificationResolutionStatus(Integer parentStatusId);	
	List<ClarificationScope> getClarificationScope();
	Integer countAllClarification(Date startDate, Date endDate);
	List<ClarificationTracker> listAllClarificationByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate);
	List<ClarificationTracker> listClarificationsByEngagementAndProductIds(int testFactoryId, int productId);
	void addTransactionClarification(TransactionClarification transactionClarification);
	List<TransactionClarification> listTransactionClarification(Integer clarificationTrackerId);
	void updateTransactionClarification(TransactionClarification transactionClarificationUI);
	String deleteClarificationTracker(Integer clarificationTrackerId);
	List<ClarificationTracker> listClarificationsByActivityWorkPackageId(Integer entityTypeId, Integer entityInstanceId,
			Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel);
	Integer getcountOfClarificationByEntityTypeAndInstanceIds(Integer entityTypeId, Integer entityInstanceId);
	List<ClarificationTracker> listClarificationsByProductId(Integer entityTypeId, Integer entityInstanceId,
			Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel);
	Integer getClarificationTrackerCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize);
}
