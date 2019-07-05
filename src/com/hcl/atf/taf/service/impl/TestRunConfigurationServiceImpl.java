package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.TestCategoryMasterDAO;
import com.hcl.atf.taf.dao.TestEnvironmentDAO;
import com.hcl.atf.taf.dao.TestEnvironmentMasterDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationParentDAO;
import com.hcl.atf.taf.dao.TestRunSelectedDeviceListDAO;
import com.hcl.atf.taf.dao.TestToolMasterDAO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.service.TestRunConfigurationService;

@Service
public class TestRunConfigurationServiceImpl  implements TestRunConfigurationService{	 
	@Autowired
	private TestRunConfigurationParentDAO testRunConfigurationParentDAO;
	@Autowired
	private TestRunConfigurationChildDAO testRunConfigurationChildDAO;
	@Autowired
	private TestEnvironmentMasterDAO testEnvironmentMasterDAO;
	@Autowired 
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	@Autowired
	private TestCategoryMasterDAO testCategoryMasterDAO;
	@Autowired
	private TestRunSelectedDeviceListDAO testRunSelectedDeviceListDAO;
	@Autowired
	private DeviceListDAO deviceListDAO;
	@Autowired
	private TestEnvironmentDAO testEnvironmentDAO; 
	@Autowired
	private TestToolMasterDAO testToolMasterDAO;
	@Override
	@Transactional
	public void addTestRunConfigurationParent(TestRunConfigurationParent testRunConfigurationParent) {
		
		testRunConfigurationParentDAO.add(testRunConfigurationParent);
	}

	@Override
	@Transactional
	public void deleteTestRunConfigurationParent(int testRunConfigurationParentId) {
		testRunConfigurationParentDAO.delete(getByTestRunConfigurationParentId(testRunConfigurationParentId));
		
	}

	@Override
	@Transactional
	public TestRunConfigurationParent getByTestRunConfigurationParentId(int testRunConfigurationParentId) {
		return testRunConfigurationParentDAO.getByTestRunConfigurationParentId(testRunConfigurationParentId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunConfigurationParent() {
		
		return testRunConfigurationParentDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunConfigurationParent(int userId) {
		
		return testRunConfigurationParentDAO.getTotalRecords(userId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunConfigurationParent(int userId,int productId) {
		
		return testRunConfigurationParentDAO.getTotalRecords(userId, productId);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listAllTestRunConfigurationParent() {
		
		return testRunConfigurationParentDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listAllTestRunConfigurationParent(int startIndex, int pageSize) {
		
		return testRunConfigurationParentDAO.listAll(startIndex, pageSize);
	}

	
	@Override
	@Transactional
	public List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId) {
		
		return testRunConfigurationParentDAO.list(userId);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId, int productId) {

		return testRunConfigurationParentDAO.list(userId, productId);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId, int startIndex, int pageSize) {
		
		return testRunConfigurationParentDAO.list(userId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId, int productId, int startIndex, int pageSize) {
		
		return testRunConfigurationParentDAO.list(userId, productId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public void updateTestRunConfigurationParent(TestRunConfigurationParent testRunConfigurationParent) {
		testRunConfigurationParentDAO.update(testRunConfigurationParent);
		
	}
	
	@Override
	@Transactional
	public boolean isTestRunParentExistsByName(String testRunconfigurationName) {
		return testRunConfigurationParentDAO.getTestRunConfigurationParentByName(testRunconfigurationName);
	}
	
	
	@Override
	@Transactional
	public void addTestRunConfigurationChild(TestRunConfigurationChild testRunConfigurationChild) {
		testRunConfigurationChildDAO.add(testRunConfigurationChild);
	}
	

	@Override
	@Transactional
	public void deleteTestRunConfigurationChild(int testRunConfigurationChildId) {
		testRunConfigurationChildDAO.delete(getByTestRunConfigurationChildId(testRunConfigurationChildId));
	}

	@Override
	@Transactional
	public void reactivateTestRunConfigurationChild(int testRunConfigurationChildId) {
		testRunConfigurationChildDAO.reactivate(getByTestRunConfigurationChildId(testRunConfigurationChildId));
	}
	
	@Override
	@Transactional
	public TestRunConfigurationChild getByTestRunConfigurationChildId(int testRunConfigurationChildId) {
		return testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public TestRunConfigurationChild getByTestRunConfigurationChildName(String testRunConfigurationChildName) {
		
		return testRunConfigurationChildDAO.getByTestRunConfigurationChildName(testRunConfigurationChildName);
	}
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunConfigurationChild() {
		
		return testRunConfigurationChildDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunConfigurationChild(
			int testRunConfigurationParentId) {
		
		return testRunConfigurationChildDAO.getTotalRecords(testRunConfigurationParentId);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationChild> listAllTestRunConfigurationChild() {
		
		return testRunConfigurationChildDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunConfigurationChild> listAllTestRunConfigurationChild(
			int startIndex, int pageSize) {
		
		return testRunConfigurationChildDAO.listAll(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationChild> listTestRunConfigurationChild(
			int testRunConfigurationParentId, int status) {
		
		return testRunConfigurationChildDAO.list(testRunConfigurationParentId, status);
	}
	
	@Override
	@Transactional
	public List<TestRunConfigurationChild> listTestRunConfigurationChild(
			int testRunConfigurationParentId) {
		
		return testRunConfigurationChildDAO.list(testRunConfigurationParentId);
	}

	@Override
	@Transactional
	public List<TestRunConfigurationChild> listTestRunConfigurationChild(
			int testRunConfigurationParentId, int startIndex, int pageSize) {
		
		return testRunConfigurationChildDAO.list(testRunConfigurationParentId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public void updateTestRunConfigurationChild(
			TestRunConfigurationChild testRunConfigurationChild) {
		
		testRunConfigurationChildDAO.update(testRunConfigurationChild);
	}

	@Override
	@Transactional
	public List<TestEnviromentMaster> testEnviromentsList(String devicePlatform) {
		
		return testEnvironmentMasterDAO.list(devicePlatform);
	}

	@Override
	@Transactional
	public List<TestEnviromentMaster> testEnviromentsList(
			int productVersionListId) {
		return null;
	}

	@Override
	@Transactional
	public List<TestCategoryMaster> testCategoriesList() {
		
		return testCategoryMasterDAO.list();
	}
//*******************
	@Override
	@Transactional
	public void addTestRunConfigurationChildDevice(
			TestRunSelectedDeviceList testRunSelectedDeviceList) {
		
		testRunSelectedDeviceListDAO.add(testRunSelectedDeviceList);
	}

	@Override
	@Transactional
	public void deleteTestRunConfigurationChildDevice(int selectedDeviceListId) {
		testRunSelectedDeviceListDAO.delete(getBySelectedDeviceListId(selectedDeviceListId));
		
	}
	@Override
	@Transactional
	public List<TestRunSelectedDeviceList> listTestRunConfigurationChildDevice(int testRunConfigurationChildId) {
		
		return testRunSelectedDeviceListDAO.list(testRunConfigurationChildId);
	}
	@Override
	@Transactional
	public TestRunSelectedDeviceList getBySelectedDeviceListId(
			int selectedDeviceListId) {
		
		return testRunSelectedDeviceListDAO.getBySelectedDeviceListId(selectedDeviceListId);
	}

	@Override
	@Transactional
	public int getTotalRecords(int testRunConfigurationChildId) {
		
		return testRunSelectedDeviceListDAO.getTotalRecords(testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public List<DeviceList> listAttachedDevices(int devicePlatformVersionlistId) {
		
		return deviceListDAO.list(devicePlatformVersionlistId, new String[]{"deviceListId","deviceId"});
	}
	@Override
	@Transactional
	public List<DeviceList> listAvailableDevices(String devicePlatformVersion) {
		
		
		return deviceListDAO.listDevicesForPlatformVersion(devicePlatformVersion,new String[]{"deviceListId","deviceId"});//, new String[]{"deviceListId","deviceId"});
	}
	@Override
	@Transactional
	public int getMaxRunNo() {
		return testRunConfigurationChildDAO.getMaxRunNo();
	}	
	
	@Override
	@Transactional
	public List<TestRunList> getRunNo(int productId, String pltfm_id) {
		List<TestRunList> ww = testRunConfigurationChildDAO.getRunNo(productId, pltfm_id);
		return ww;
	}
	
	@Override
	@Transactional
	public boolean isTestConfigurationChildExistingByName(String testRunConfigurationName) {
		
		return testRunConfigurationChildDAO.isTestConfigurationChildExistingByName(testRunConfigurationName);
	}
	@Override
	@Transactional
	public void addTestRunConfigurationChildTestEnviornment(int testRunConfigurationChildId,int testEnvironmentDeviceId) {
		testRunConfigurationChildDAO.addTestEnvironmentToTestRunConfigurationChild(testRunConfigurationChildId,testEnvironmentDeviceId);
	}
	@Override
	@Transactional
	public List<TestEnvironmentDevices> listAllTestEnviornments(int testRunConfigurationChildId) {
		return testRunConfigurationChildDAO.listTestEnvironmentsOfTestRunConfigurationChild(testRunConfigurationChildId);
	}
	@Override
	@Transactional
	public ArrayList listTestConfigurationChildDevices(
			int testRunConfigurationChildId) {
		return testRunSelectedDeviceListDAO.listDeviceList(testRunConfigurationChildId);
	}
	
	@Override
	@Transactional
	public void deleteTestEnviornmentDevicesFromTestRunConfigChild(
			int testRunConfigurationChildId, int testEnvironmentDevicesId) {
		testRunConfigurationChildDAO.removeTestEnvironmentFromTestRunConfigurationChild(testRunConfigurationChildId,testEnvironmentDevicesId);
	}
	
	
	@Override
	@Transactional
	public DeviceList deviceslistById(String deviceId) {		
		return deviceListDAO.getByDeviceId(deviceId);
	}

	@Override
	public List<TestToolMaster> listTestToolMaster() {
		return testToolMasterDAO.list();
	}

	@Override
	public TestToolMaster getTestToolMaster(int parseInt) {
		return testToolMasterDAO.getTestToolMaster(parseInt);
	}
}
