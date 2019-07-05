package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.json.JsonChangeRequest;


public interface ChangeRequestService {
	
	List<ChangeRequest> listChangeRequests();
	ChangeRequest getChangeRequestById(int ChangeRequestId);	
	Integer addChangeRequest(ChangeRequest changeRequest);
	void updateChangeRequest(ChangeRequest changeRequest);
	void deleteChangeRequest(ChangeRequest changeRequest);
	List<JsonChangeRequest> listChangeRequestsByActivityId(Integer activityId, Integer initializationLevel);
	List<ChangeRequest> listChangeRequestByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1);
	int addChangeRequestBulk(List<ChangeRequest> listOfChangeRequestToAdd);
	List<String> getExistingChangeRequestNames(ProductMaster product);
	List<JsonChangeRequest> listChangeRequestsByProductId(Integer productId,Integer status, Integer initializationLevel, Integer jtStartIndex, Integer jtPageSize);
	void updateChangeRequesttoActivity(ChangeRequest changeRequest);
	ChangeRequest getChangeRequestByName(String changeRequestName);
	List<JsonChangeRequest> listChangeRequestsByentityInstanceId(Integer entityType1,Integer entityType2,Integer entityInstanceId1);
	List<ChangeRequestType> getChangeRequestType();
	List<Object[]> getRcnByProductId(int productId, int jtStartIndex,
			int jtPageSize, int i);
	Integer countAllChangeRequest(Date startDate, Date endDate);
	List<ChangeRequest> listAllChangeRequestByLastSyncDate(int startIndex,int pageSize, Date startDate, Date endDate);
	List<JsonChangeRequest> listChangeRequestsByEngagementAndProductIds(int testFactoryId, int productId);
	List<JsonChangeRequest> listChangeRequestByActivityWorkPackageId(Integer entityType1, Integer entityInstance1, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel);
	Integer getcountOfChangeRequestsByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1);
	List<JsonChangeRequest> listChangeRequestByProductId(Integer entityType1, Integer entityInstance1, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel);
	Integer getChangeRequestCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize);
	List<ChangeRequest> listCRByEntityTypeAndInstanceIds(Integer entityType1, Integer entityType2, Integer entityInstance1);
}
