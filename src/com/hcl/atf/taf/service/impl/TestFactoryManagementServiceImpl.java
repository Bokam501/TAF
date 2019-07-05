package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ResourceShiftCheckinDAO;
import com.hcl.atf.taf.dao.TestFactoryCoreResoucesDao;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestFactoryLabDao;
import com.hcl.atf.taf.dao.TestFactoryManagerDao;
import com.hcl.atf.taf.dao.TestfactoryResourcePoolDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjectionList;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;
import com.hcl.atf.taf.mongodb.dao.TestFactoryMongoDAO;


import com.hcl.atf.taf.service.TestFactoryManagementService;

@Service
public class TestFactoryManagementServiceImpl implements TestFactoryManagementService{
	
	@Autowired
	private TestFactoryLabDao testFactoryLabDao;
	@Autowired
	private TestFactoryDao testFactoryDao;
	@Autowired
	private TestFactoryCoreResoucesDao tfCoreResDao; 
	@Autowired
	private TestFactoryManagerDao testFactoryManagerDao;
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	@Autowired
	private TestfactoryResourcePoolDAO testfactoryResourcePoolDAO;
	@Autowired
	private ResourceShiftCheckinDAO resourceShiftCheckinDAO;
	@Autowired
	private TestFactoryMongoDAO testFactoryMongoDAO;
	
	@Override
	@Transactional
	public List<TestFactoryLab> getTestFactoryLabsList() {
		return testFactoryLabDao.getTestFactoryLabsList();
	}

	@Override
	@Transactional
	public List<TestFactory> getTestFactoryList(int testFactoryLabId,
			int status, int filter) {
		return testFactoryDao.getTestFactoryList(testFactoryLabId,status,filter);
	}
	
	@Override
	@Transactional
	public List<TestFactory> getTestFactoryListByLabAndUser(int testFactoryLabId, int status, int userId, int testFactoryId,int filterstatus){
		return testFactoryDao.getTestFactoryListByLabAndUser(testFactoryLabId, status, userId, testFactoryId,filterstatus);
	}
	
	@Override
	@Transactional
	public TestFactoryLab getTestFactoryLabById(int testFactoryLabId) {
		return testFactoryLabDao.getTestFactoryLabBytestFactoryLabId(testFactoryLabId);
	}

	@Override
	@Transactional
	public List<TestFactoryCoreResource> getCoreResourcesList(
			int testFactoryId, Integer jtStartIndex,Integer jtPageSize) {
	
		return tfCoreResDao.getCoreResourcesList(testFactoryId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public void addCoreResource(TestFactoryCoreResource coreResouce) {
		tfCoreResDao.addCoreResource(coreResouce);
	}


	@Override
	@Transactional
	public TestFactoryCoreResource getCoreResourceById(
			Integer testFactoryCoreResourceId) {
		return tfCoreResDao.getCoreResourceById(testFactoryCoreResourceId);
	}

	@Override
	@Transactional
	public void updateCoreResource(TestFactoryCoreResource coreResourceFromUI) {
		tfCoreResDao.updateCoreRes(coreResourceFromUI);
	}

	@Override
	@Transactional
	public Boolean isUserExisted(Integer testFactoryId, Integer userId) {
		return tfCoreResDao.isUserExisted(testFactoryId,userId);
	}

	@Override
	public TestFactory getTestFactoryById(Integer testFactoryId) {
		return testFactoryDao.getTestFactoryById(testFactoryId);
	}

	@Override
	@Transactional
	public List<TestFactoryCoreResource> getCoreResourcesList() {
		return tfCoreResDao.list();
	}

	@Override
	@Transactional
	public List<TestFactory> getTestFactoryList() {
		return testFactoryDao.getTestFactoryList();
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> getResourcePoolListbyTestFactoryId(
			int testFactoryId) {
		return testFactoryDao.getResourcePoolListbyTestFactoryId(testFactoryId);
	}
	
	@Override
	@Transactional
	public List<TestFactoryManager> listTestFactoryManager(int testFactoryId,
			int entityStatusActive) {
		return testFactoryManagerDao.listTestFactoryManager(testFactoryId,
				entityStatusActive);
	}

	@Override
	@Transactional
	public void addTestFactoryManager(TestFactoryManager tfManager) {
		testFactoryManagerDao.addTestFactoryManager(tfManager);
	}

	@Override
	@Transactional
	public void deleteTestFactoryManager(TestFactoryManager manager) {
		testFactoryManagerDao.delete(manager);
	}

	@Override
	@Transactional
	public List<TestFactory> getTestFactoriesByTestFactoryManagerId(
			int testFactoryManagerId) {
		return testFactoryDao.getTestFactoriesByTestFactoryManagerId(testFactoryManagerId);
	}

	@Override
	public List<WorkShiftMaster> listWorkShiftsByTestFactoryId(int testFacrtoryId) {
		return workShiftMasterDAO.listWorkShiftsByTestFactoryId(testFacrtoryId);
	}
	
	@Override
	@Transactional
	public List<JsonWorkShiftMaster> listJsonWorkShiftsByTestFactoryId(int testFactoryId) {
		return workShiftMasterDAO.listJsonWorkShiftsByTestFactoryId(testFactoryId);
	}
	@Override
	@Transactional
	public List<WorkShiftMaster> getTestFactoryWorkShiftsList(int testFacrtoryId,int testFactoryLabId) {
		List<WorkShiftMaster> workShiftMasterList=null;
		List<TestFactory> testFcatoryList=	testFactoryDao.list(testFactoryLabId);
		for(TestFactory testFcatory:testFcatoryList){
			if(testFcatory.getTestFactoryId()==testFacrtoryId){
				workShiftMasterList= workShiftMasterDAO.listWorkShiftsByTestFactoryId(testFacrtoryId);
			}
		}
		return workShiftMasterList;
	}
	
	@Override
	@Transactional
	public List<ActualShift> shiftManageList(Integer testFactoryId, Integer shiftId, Date workDate) {
		return workShiftMasterDAO.listActualShift(shiftId,workDate);
	}
	
	
	@Override
	@Transactional
	public void shiftManageAdd(Integer shiftId, Date workDate, String shiftTime, String shiftRemarks, String shiftRemarksValue,Integer startByUserId) {
		UserList startByUser=new UserList();
		startByUser.setUserId(startByUserId);
		WorkShiftMaster workShift = new WorkShiftMaster();
		workShift.setShiftId(shiftId);
		ActualShift actualShifts=null;
		Date startTime=DateUtility.toDateInSec(shiftTime);
		if(workDate!=null && shiftId!=null ){
			 actualShifts=workShiftMasterDAO.listActualShiftbyshiftId(shiftId, workDate);
			if(actualShifts!=null){
				if(actualShifts.getEndTime()==null){
				actualShifts.setStartByUserList(startByUser);
				if (shiftRemarks.equalsIgnoreCase("startRemarks")) {
					actualShifts.setStartTime(startTime);
					actualShifts.setStartTimeRemarks(shiftRemarksValue);
				}
				workShiftMasterDAO.updateShiftManage(actualShifts);
				List<ResourceShiftCheckIn> resourceShiftCheckInList= resourceShiftCheckinDAO.getResourceShiftCheckInByDateAndShift(workDate,actualShifts.getActualShiftId());
				if(resourceShiftCheckInList.size()!=0){
				for(ResourceShiftCheckIn resShiftCheckIn:resourceShiftCheckInList){
					if(resShiftCheckIn.getCheckIn()!=null){
					if(resShiftCheckIn.getCheckIn().compareTo(startTime)<0){
						resShiftCheckIn.setCheckIn(startTime);
						resourceShiftCheckinDAO.updateResourceShiftCkeckIn(resShiftCheckIn);
					}
				}
				}
				}
				}
			}else{
				ActualShift actualShift = new ActualShift();
				actualShift.setWorkdate(workDate);
				actualShift.setStartByUserList(startByUser);
				actualShift.setShift(workShift);
				if (shiftRemarks.equalsIgnoreCase("startRemarks")) {
					actualShift.setStartTime(startTime);
					actualShift.setStartTimeRemarks(shiftRemarksValue);
				}
				 workShiftMasterDAO.addShiftManage(actualShift);
			}
			
			
		}	
	}
	
	@Override
	@Transactional
	public ActualShift shiftManageUpdate(ActualShift actualShifts, String shiftTime, String shiftRemarks, String shiftRemarksValue,Integer endByuserId) {
		UserList endByUser=new UserList();
		endByUser.setUserId(endByuserId);
		Date endTime=DateUtility.toDateInSec(shiftTime);
		actualShifts.setEndByUserList(endByUser);
			if (shiftRemarks.equalsIgnoreCase("endRemarks")) {
				actualShifts.setEndTime(endTime);
				actualShifts.setEndTimeRemarks(shiftRemarksValue);
			}
		workShiftMasterDAO.updateShiftManage(actualShifts);
		return actualShifts;
	}

	@Override
	@Transactional
	public void addTestFactoryShits(WorkShiftMaster workShiftMaster) {
		workShiftMaster.setStatus(1);
		workShiftMasterDAO.addTestFactoryShits(workShiftMaster);
		
	}

	@Override
	@Transactional
	public WorkShiftMaster updateTestFactoryShitsInline(Integer shiftId,String modifiedField, String modifiedFieldValue) {
		
		WorkShiftMaster workShiftMaster = workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
		if (modifiedField.equalsIgnoreCase("startTime")) {
			Date modifiedstartTime = DateUtility.getStringconvertedTime(workShiftMaster.getStartTime(),modifiedFieldValue);
			workShiftMaster.setStartTime(modifiedstartTime);
		}else if (modifiedField.equalsIgnoreCase("endTime")) {
			Date modifiedEndTime = DateUtility.getStringconvertedTime(workShiftMaster.getEndTime(),modifiedFieldValue);
			workShiftMaster.setEndTime(modifiedEndTime);
		}else if (modifiedField.equalsIgnoreCase("shiftName")) {
			workShiftMaster.setShiftName(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("displayLabel")) {
			workShiftMaster.setDisplayLabel(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("description")) {
			workShiftMaster.setDescription(modifiedFieldValue);
		}
		workShiftMasterDAO.updateTestFactoryShitsInline(workShiftMaster);
		return workShiftMaster;
	}

	@Override
	public WorkShiftMaster getWorkShiftsByshiftId(Integer shiftId) {
		return workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
	}

	@Override
	public ShiftTypeMaster getShiftTypeByShiftId(int shiftId) {
		return workShiftMasterDAO.getShiftTypeByShiftId(shiftId);
	}

	@Override
	public List<ActualShift> getActualShiftByStartByUserId(Integer userId) {
		return workShiftMasterDAO.getActualShiftByStartByUserId(userId);
		
	}

	@Override
	public List<ActualShift> listActualShift(Integer shiftId, Date date) {
		return workShiftMasterDAO.listActualShift(shiftId, date);
	}
	
	@Override
	public List<TestfactoryResourcePool> listResourcePoolByTestFactoryLabId(
			int testFactoryLabId) {
		return testfactoryResourcePoolDAO.listResourcePoolByTestFactoryLabId(testFactoryLabId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageDemandProjectionList> listDemandDetailsOfTestFactoryLab(
			Integer testFactoryLabId, Integer shiftTypeId,
			Date date) {
		List<JsonWorkPackageDemandProjectionList> jsonWorkPackageDemandProjectionList = new ArrayList<JsonWorkPackageDemandProjectionList>();
		List<WorkPackageDemandProjection>  listWorkPackageDemandProjection =  testFactoryLabDao.listDemandDetailsOfTestFactoryLab(testFactoryLabId, shiftTypeId,date);
		if (listWorkPackageDemandProjection == null || listWorkPackageDemandProjection.isEmpty()) {
			jsonWorkPackageDemandProjectionList = null;
		} else {
			for(WorkPackageDemandProjection workPackageDemandProjection:listWorkPackageDemandProjection){
				jsonWorkPackageDemandProjectionList.add(new JsonWorkPackageDemandProjectionList(workPackageDemandProjection));
			}
			listWorkPackageDemandProjection = null;
		}
		return jsonWorkPackageDemandProjectionList;
	}

	@Override
	@Transactional
	public int addTestFactory(TestFactory testFactory) {
		int result=	testFactoryDao.add(testFactory);
		return result;
	}

	@Override
	@Transactional
	public void updateTestFactory(TestFactory testFactory) {
		testFactoryDao.update(testFactory);		
	}

	@Override
	public TestFactoryManager getTestFactoryManagerByTestCatoryIdUserId(int userId,int testFactoryId) {
		return testFactoryManagerDao.getTestFactoryManagerByTestCatoryIdUserId(userId,testFactoryId);
	}

	@Override
	@Transactional
	public TestFactory updateTestFactoryInline(Integer testFactoryId,
			String modifiedField, String modifiedFieldValue) {
		TestFactory testFactory = testFactoryDao.getTestFactoryById(testFactoryId);	
		if (modifiedField.equalsIgnoreCase("testFactoryName")) {
			testFactory.setTestFactoryName(modifiedFieldValue);			
		}else if (modifiedField.equalsIgnoreCase("displayName")) {
			testFactory.setDisplayName(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("city")) {
			testFactory.setCity(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("state")) {
			testFactory.setState(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("country")) {
			testFactory.setCountry(modifiedFieldValue);
		}
		testFactoryDao.update(testFactory);
		return testFactory;
		}

	@Override
	@Transactional
	public boolean isTestFactoryExistingByName(TestFactory testFactory) {
		return testFactoryDao.isTestFactoryExistingByName(testFactory);
	}

	@Override
	@Transactional
	public boolean isTestFactoryExistingByNameForUpdate(
			TestFactory testFactory, int testFactoryId) {
		return testFactoryDao.isTestFactoryExistingByNameForUpdate(testFactory, testFactoryId);
	}

	@Override
	@Transactional
	public TestFactory getTestFactoryByName(String testFactoryName) {
		return testFactoryDao.getTestFactoryByName(testFactoryName);
	}

	@Override
	@Transactional
	public List<TestFactory> listByTestFactoryId(int testFactoryId) {		
		return testFactoryDao.listByTestFactoryId(testFactoryId);
	}

	@Override
	@Transactional
	public List<TestFactory> listByTestFactoryIdAndLabId(int testFactoryId,
			int testFactoryLabId) {
		return testFactoryDao.listByTestFactoryIdAndLabId(testFactoryId, testFactoryLabId);
	}

	@Override
	@Transactional
	public List<EngagementTypeMaster> listEngagementTypes() {
		return testFactoryDao.listEngagementTypes();
	}

	@Override
	@Transactional
	public EngagementTypeMaster getEngagementTypeById(int engagementTypeId) {
		return testFactoryDao.getEngagementTypeById(engagementTypeId);
	}

	@Override
	public int getEngagementTypeIdBytestfactoryId(int testFactoryId) {
		return testFactoryDao.getEngagementTypeIdBytestfactoryId(testFactoryId);
	}

	@Override
	public List<ProductMode> getmodelist() {
		return testFactoryDao.getmodelist();
	}
	
	@Override
	@Transactional
	public TestFactory getResourcePoolShowHideTab(Integer testFactoryId) {
		return testFactoryDao.getResourcePoolShowHideTab(testFactoryId);
	}

	@Override
	@Transactional
	public List<TestFactoryLab> getTestFactoryLabsList(Integer testFactoryLabId) {
		return testFactoryLabDao.getTestFactoryLabsList(testFactoryLabId);
	}

	@Override
	public Integer countAllTestFactory(Date startDate,Date endDate) {
		return testFactoryDao.countAllTestFactory(startDate,endDate);
	}

	@Override
	public List<TestFactory> listAllTestFactoryByLastSyncDate(int startIndex,int pageSize, Date startDate,Date endDate) {
		return testFactoryDao.listAllTestFactoryByLastSyncDate(startIndex, pageSize,startDate,endDate);
	}

	@Override
	@Transactional
	public List<TestFactory> getEngagementByUserAndCustomerProduct(Integer userId, Integer userRoleId, Integer customerId, Integer activeStatus) {
		return testFactoryDao.getEngagementByUserAndCustomerProduct(userId, userRoleId, customerId, activeStatus);
	}

	@Override
	@Transactional
	public void deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(Integer testFactoryId,List<Integer> userIds) {
		testFactoryManagerDao.deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(testFactoryId,userIds);
	}

	@Override
	@Transactional
	public int addEngagement(EngagementTypeMaster engagementTypeMaster) {
		int result=	testFactoryDao.addEngagement(engagementTypeMaster);
		return result;
	}

}
