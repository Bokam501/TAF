package com.hcl.atf.taf.service;

import java.util.ArrayList;
import java.util.List;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;
import com.hcl.atf.taf.model.TestToolMaster;

public interface TestRunConfigurationService {
	//TestConfigurationParent
	void addTestRunConfigurationParent (TestRunConfigurationParent testRunConfigurationParent);
	void updateTestRunConfigurationParent (TestRunConfigurationParent testRunConfigurationParent);
	void deleteTestRunConfigurationParent (int testRunConfigurationParentId);
	List<TestRunConfigurationParent> listAllTestRunConfigurationParent();
	List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId);
	List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId,int productId);
	List<TestRunConfigurationParent> listAllTestRunConfigurationParent(int startIndex, int pageSize);
	List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId,int startIndex, int pageSize);
	List<TestRunConfigurationParent> listTestRunConfigurationParent(int userId,int productId,int startIndex, int pageSize);
	TestRunConfigurationParent getByTestRunConfigurationParentId(int testRunConfigurationParentId);
	
	int getTotalRecordsOfTestRunConfigurationParent();
	int getTotalRecordsOfTestRunConfigurationParent(int userId);
	int getTotalRecordsOfTestRunConfigurationParent(int userId,int productId);
	int getMaxRunNo();		
	List<TestRunList> getRunNo(int productId, String pltfm_id);
	boolean isTestRunParentExistsByName(String testRunconfigurationName);
	//TestConfigurateionParent END
	
	
	
	//TestConfigurationChild
	void addTestRunConfigurationChild (TestRunConfigurationChild testRunConfigurationChild);
	void updateTestRunConfigurationChild (TestRunConfigurationChild testRunConfigurationChild);
	void deleteTestRunConfigurationChild (int testRunConfigurationChildId);
	List<TestRunConfigurationChild> listAllTestRunConfigurationChild();
	List<TestRunConfigurationChild> listTestRunConfigurationChild(int testRunConfigurationParentId);	
	List<TestRunConfigurationChild> listAllTestRunConfigurationChild(int startIndex, int pageSize);	
	List<TestRunConfigurationChild> listTestRunConfigurationChild(int testRunConfigurationParentId,int startIndex, int pageSize);
	TestRunConfigurationChild getByTestRunConfigurationChildId(int testRunConfigurationChildId);
	TestRunConfigurationChild getByTestRunConfigurationChildName(String testRunConfigurationChildName);
	int getTotalRecordsOfTestRunConfigurationChild();
	int getTotalRecordsOfTestRunConfigurationChild(int testRunConfigurationParentId);
	//TestConfigurateionChild END
	
	
	List<TestEnviromentMaster> testEnviromentsList(String devicePlatform);
	List<TestEnviromentMaster> testEnviromentsList(int productVersionListId);
	
	List<TestCategoryMaster> testCategoriesList();
	
	
	//Child device
	void addTestRunConfigurationChildDevice (TestRunSelectedDeviceList testRunSelectedDeviceList);	
	void deleteTestRunConfigurationChildDevice (int selectedDeviceListId);
	List<TestRunSelectedDeviceList> listTestRunConfigurationChildDevice(int testRunConfigurationChildId);
	TestRunSelectedDeviceList getBySelectedDeviceListId(int selectedDeviceListId);
	int getTotalRecords(int testRunConfigurationChildId);
	//Child device end
	
	List<DeviceList> listAttachedDevices(int devicePlatformVersionlistId);
	List<DeviceList> listAvailableDevices(String devicePlatformVersion);
	
	boolean isTestConfigurationChildExistingByName(String testRunConfigurationName);
	List<TestRunConfigurationChild> listTestRunConfigurationChild(int testEnvironmentDevicesId, int status);
	void reactivateTestRunConfigurationChild(int testRunConfigurationChildId);
	List<TestEnvironmentDevices> listAllTestEnviornments(int testRunConfigurationChildId);
	ArrayList listTestConfigurationChildDevices(int testRunConfigurationChildId);
	void addTestRunConfigurationChildTestEnviornment(int testRunConfigurationChildId, int testEnvironmentDevicesId);
	void deleteTestEnviornmentDevicesFromTestRunConfigChild(int testRunConfigurationChildId, int testEnvironmentDevicesId);
	
	DeviceList deviceslistById(String deviceId);
	List<TestToolMaster> listTestToolMaster();
	TestToolMaster getTestToolMaster(int parseInt);
	
	
}
