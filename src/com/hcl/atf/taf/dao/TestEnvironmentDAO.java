package com.hcl.atf.taf.dao;

import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;

public interface TestEnvironmentDAO  {	 
	//Parent Table CURD operations
	void add (TestEnvironmentDevices testEnvironmentDevices);
	void update (TestEnvironmentDevices testEnvironmentDevices);
	void delete(TestEnvironmentDevices testEnvironmentDevices);
	List<TestEnvironmentDevices> listAll();
	List<TestEnvironmentDevices> listAllTestEnvironments(int startIndex,int pageSize);
	int getTotalTestEnviornmentRecords();
	TestEnvironmentDevices getTestEnviornmentByName(String name);
	boolean isTestEnviromentdevicesExistingByName(TestEnvironmentDevices testEnvironmentDevices);
	List<TestEnvironmentDevices> listTestEnviornmnetsByName(String[] parameters);
	TestEnvironmentDevices getByTestEnviornmentId(int testEnvironmentDevicesId);
	List<TestEnvironmentDevices> listTestEnviornmnetsByDescription(int testEnviornmentDeviceId, String[] parameters);
	void delete(int testEnvironmentDevicesId);
	List<DeviceList> addDeviceToTestEnvironment(int testEnvironmentDevicesId, int deviceListId);
	List<DeviceList> deleteDeviceFromTestEnvironment(Integer testEnvironmentDevicesId, Integer deviceListId);
	List<TestEnvironmentDevices> listTestRunConfigurationChildhasTestEnviornments(int testRunConfigurationChildId);
	int getTotalRecords(Integer testEnvironmentDevicesId);
	void reactivate(TestEnvironmentDevices testEnvironmentDevices);
	
	
	
	
	
	
	
}
