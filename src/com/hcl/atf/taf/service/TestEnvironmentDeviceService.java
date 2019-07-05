package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;

public interface TestEnvironmentDeviceService {
	
	//Test Enviorment starts
	List<TestEnvironmentDevices> listAllTestEnviornments();
	List<TestEnvironmentDevices> listAllTestEnvironments(int startIndex,int pageSize);
	int getTotalTestEnviornmentRecords();
	void addTestEnviroments(TestEnvironmentDevices testEnvironmentDevices);
	void updateTestEnvironments(TestEnvironmentDevices testEnvironmentDevices);
	void delete(int testEnvironmentDevicesId);
	TestEnvironmentDevices getTestEnvironmentByName(String name);
	//Test Enviormment ends
	
	
	//Test Enviornment Devices starts
	List<DeviceList> listTestEnvironmentDevices();
	List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId);
	List<DeviceList> listTestEnvironmentDevices(int startIndex, int pageSize);
	List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId,int startIndex, int pageSize);
	DeviceList getByTestEnvironmentDevicesId(int deviceListId);
	TestEnvironmentDevices getByTestEnvironmentId(int testEnviornmnetId);
	int getTotalRecordsDevices(int testEnvironmentDevicesId);
	//Test Enviornment Devices ends
	void deleteDevice(Integer testEnvironmentDevicesId,Integer deviceListId);
	boolean isTestEnviromentdevicesExistingByName(TestEnvironmentDevices testEnvironmentDevices);
	List<TestEnvironmentDevices> listTestEnvironmentDevicesByName();
	List<TestEnvironmentDevices> listTestEnvironmentDevicesBydescription(int testEnvironmentDevicesId);
	void addTestEnvironmentDevicesHasList(TestEnvironmentDevices testEnvironmentDevices);
	void addDeviceToTestEnvironment(Integer testEnvironmentDevicesId,Integer deviceListId);
	void reactivateTestEnvironment(int testEnvironmentDevicesId);	
}
