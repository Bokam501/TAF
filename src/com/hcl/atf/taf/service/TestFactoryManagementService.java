package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjectionList;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;

public interface TestFactoryManagementService {

	 List<TestFactoryLab> getTestFactoryLabsList();

	 List<TestFactory> getTestFactoryList(int testFactoryLabId, int entityStatusActive, int filter);
	 List<TestFactory> getTestFactoryListByLabAndUser(int testFactoryLabId, int status, int userId, int testFactoryId,int filterstatus);
	TestFactoryLab getTestFactoryLabById(int testFactoryLabId);
	 List<TestFactory> listByTestFactoryId(int testFactoryId);
	 List<TestFactory> listByTestFactoryIdAndLabId(int testFactoryId, int testFactoryLabId);
	 List<TestFactoryCoreResource> getCoreResourcesList(int testFactoryId, Integer jtStartIndex, Integer jtPageSize);
	 List<TestFactoryCoreResource> getCoreResourcesList();

	 void addCoreResource(TestFactoryCoreResource coreResouce);

	 TestFactoryCoreResource getCoreResourceById(
			Integer testFactoryCoreResourceId);

	 void updateCoreResource(TestFactoryCoreResource coreResourceFromUI);

	

	 Boolean isUserExisted(Integer testFactoryId, Integer userId);

	 TestFactory getTestFactoryById(Integer testFactoryId);

	 List<TestFactory> getTestFactoryList();
	 List<TestfactoryResourcePool> getResourcePoolListbyTestFactoryId(int testFactoryId);
	
	int addTestFactory (TestFactory testFactory);	
	void updateTestFactory (TestFactory testFactory);
	 TestFactory updateTestFactoryInline(Integer testFactoryId, String modifiedField, String modifiedFieldValue); 
	boolean isTestFactoryExistingByName(TestFactory testFactory);
	boolean isTestFactoryExistingByNameForUpdate(TestFactory testFactory, int testFactoryId);
	TestFactory getTestFactoryByName(String testFactoryName);
	
	 List<TestFactoryManager> listTestFactoryManager(int testFactoryId,
			int entityStatusActive);

	 void addTestFactoryManager(TestFactoryManager tfManager);

	 void deleteTestFactoryManager(TestFactoryManager manager);
	
	 List<TestFactory> getTestFactoriesByTestFactoryManagerId(int testFactoryManagerId);
	
	List<WorkShiftMaster> listWorkShiftsByTestFactoryId(int testFacrtoryId);
	
	 List<WorkShiftMaster> getTestFactoryWorkShiftsList(int testFactoryId,int testFactoryLabId);

	 List<ActualShift> shiftManageList(Integer testFactoryId, Integer shiftId, Date workDate);

	 void shiftManageAdd(Integer shiftId, Date workDate, String shiftTime, String shiftRemarks, String shiftRemarksValue, Integer startByUserId);
	
	 ActualShift shiftManageUpdate(ActualShift actualShift, String shiftTime, String shiftRemarks, String shiftRemarksValue, Integer userId);

	 void addTestFactoryShits(WorkShiftMaster workShiftMaster);

	 WorkShiftMaster updateTestFactoryShitsInline(Integer shiftId, String modifiedField, String modifiedFieldValue);
	 WorkShiftMaster getWorkShiftsByshiftId(Integer shiftId);
	
	 ShiftTypeMaster getShiftTypeByShiftId(int shiftId);

	 List<ActualShift> getActualShiftByStartByUserId(Integer userId);

	 List<ActualShift> listActualShift(Integer shiftId, Date date);
	
	 List<TestfactoryResourcePool> listResourcePoolByTestFactoryLabId(int testFactoryLabId);
	
	 List<JsonWorkPackageDemandProjectionList> listDemandDetailsOfTestFactoryLab(Integer testFactoryLabId, Integer shiftTypeId,Date date);

	 TestFactoryManager getTestFactoryManagerByTestCatoryIdUserId(int userId,
			int testFactoryId);
	 List<EngagementTypeMaster> listEngagementTypes();
	EngagementTypeMaster getEngagementTypeById(int engagementTypeId);

	 int getEngagementTypeIdBytestfactoryId(int testFactoryId);

	 List<ProductMode> getmodelist();
	
	 TestFactory getResourcePoolShowHideTab(Integer testFactoryId);
	List<TestFactoryLab> getTestFactoryLabsList(Integer testFactoryLabId);

	List<JsonWorkShiftMaster> listJsonWorkShiftsByTestFactoryId(int testFactoryId);

	Integer countAllTestFactory(Date startDate, Date endDate);

	List<TestFactory> listAllTestFactoryByLastSyncDate(int startIndex, int pageSize,Date startDate, Date endDate);

	List<TestFactory> getEngagementByUserAndCustomerProduct(Integer userId, Integer userRoleId, Integer customerId, Integer activeStatus);

	void deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(Integer testFactoryId,List<Integer> userIds);
	
	int addEngagement(EngagementTypeMaster engagementTypeMaster);
}
