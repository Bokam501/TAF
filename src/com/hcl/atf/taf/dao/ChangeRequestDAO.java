package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.ProductMaster;


public interface ChangeRequestDAO {
	List<ChangeRequest> listChangeRequests();
	ChangeRequest getChangeRequestById(int ChangeRequestId);
	Integer addChangeRequest(ChangeRequest changeRequest);
	void updateChangeRequest(ChangeRequest changeRequest);
	void deleteChangeRequest(ChangeRequest changeRequest);
	List<ChangeRequest> listChangeRequestsByActivityId(Integer activityId, Integer initializationLevel);
	List<ChangeRequest> listChangeRequestByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1);
	int addChangeRequestBulk(List<ChangeRequest> listOfChangeRequestToAdd,int maxBatchCount);
	List<String> getExistingChangeRequestNames(ProductMaster product);
	List<ChangeRequest> listChangeRequestsByProductId(Integer productId,Integer status,Integer initializationLevel, Integer jtStartIndex, Integer jtPageSize);
	void updateChangeRequesttoActivity(ChangeRequest changeRequest);
	ChangeRequest getChangeRequestByName(String changeRequestName);
	List<ChangeRequest> listChangeRequestsByIds(List<Integer> changeRequestIds);	
	List<ChangeRequestType> getChangeRequestType();
	List<Object[]> getRcnByProductId(int productId, int jtStartIndex, int jtPageSize);
	Integer countAllChangeRequest(Date startDate, Date endDate);
	List<ChangeRequest> listAllChangeRequestByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate);
	List<ChangeRequest> listChangeRequestByActivityWorkPackageId(Integer entityType1, Integer entityInstance1, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel);
	Integer getcountOfChangeRequestsByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1);
	List<ChangeRequest> listChangeRequestByProductId(Integer entityType1,
			Integer entityInstance1, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel);
	Integer getChangeRequestCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize);
	List<ChangeRequest> listCRByEntityTypeAndInstanceIds(Integer entityType1, Integer entityType2, Integer entityInstance1);
}
