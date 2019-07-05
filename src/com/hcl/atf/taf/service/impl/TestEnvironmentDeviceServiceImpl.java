package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import sun.util.logging.resources.logging;
import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.TestCategoryMasterDAO;
import com.hcl.atf.taf.dao.TestEnvironmentDAO;
import com.hcl.atf.taf.dao.TestEnvironmentMasterDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationParentDAO;
import com.hcl.atf.taf.dao.TestRunSelectedDeviceListDAO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.service.TestEnvironmentDeviceService;
@Service
public class TestEnvironmentDeviceServiceImpl  implements TestEnvironmentDeviceService{	 
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

		@Override
		@Transactional
		public List<TestEnvironmentDevices> listAllTestEnvironments(int startIndex, int pageSize) {
			return testEnvironmentDAO.listAllTestEnvironments(startIndex, pageSize);
		}
		@Override
		@Transactional
		public List<TestEnvironmentDevices> listAllTestEnviornments() {
			return testEnvironmentDAO.listAll();
		}
		@Override
		@Transactional
		public int getTotalTestEnviornmentRecords() {
			return testEnvironmentDAO.getTotalTestEnviornmentRecords();
		}

		@Override
		@Transactional
		public void addTestEnviroments(TestEnvironmentDevices testEnvironmentDevice) {	
			testEnvironmentDAO.add(testEnvironmentDevice);
		}
		@Override
		@Transactional
		public void updateTestEnvironments(TestEnvironmentDevices testEnvironmentDevices) {
			 testEnvironmentDAO.update(testEnvironmentDevices);	
		}
		@Override
		@Transactional
		public void delete(int testEnvironmentDevicesId) {
			
			testEnvironmentDAO.delete(testEnvironmentDevicesId);
		}
		
		@Override
		@Transactional
		public TestEnvironmentDevices getTestEnvironmentByName(String name) {
			return testEnvironmentDAO.getTestEnviornmentByName(name);
		}
		@Override
		@Transactional
		public void reactivateTestEnvironment(int testEnvironmentDevicesId) {
			testEnvironmentDAO.reactivate(getByTestEnvironmentId(testEnvironmentDevicesId));
		}
		@Override
		@Transactional
		public void deleteDevice(Integer testEnvironmentDevicesId,Integer deviceListId) 
		{
			testEnvironmentDAO.deleteDeviceFromTestEnvironment(testEnvironmentDevicesId,deviceListId);
		}
		@Override
		@Transactional
		public List<DeviceList> listTestEnvironmentDevices() {
			return deviceListDAO.listTestEnvironmentDevices();
		}
		@Override
		@Transactional
		public List<DeviceList> listTestEnvironmentDevices(
				int testEnvironmentDevicesId) {
			return deviceListDAO.getDevicesOfTestEnvironment(testEnvironmentDevicesId);
		}
		@Override
		@Transactional
		public List<DeviceList> listTestEnvironmentDevices(int startIndex,
				int pageSize) {
			return deviceListDAO.listTestEnvironmentDevices(startIndex, pageSize);
		}
		@Override
		@Transactional
		public List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId,
				int startIndex, int pageSize) {
			return deviceListDAO.listTestEnvironmentDevices(testEnvironmentDevicesId, startIndex, pageSize);
		}
		@Override
		@Transactional
		public DeviceList getByTestEnvironmentDevicesId(int deviceListId) {
			return deviceListDAO.getByTestEnvironmentDevicesId(deviceListId);
		}
		@Override
		@Transactional
		public int getTotalRecordsDevices(int testEnvironmentDevicesId) {
			return testEnvironmentDAO.getTotalRecords(testEnvironmentDevicesId);
		}
		
		@Override
		@Transactional
		public void addTestEnvironmentDevicesHasList(TestEnvironmentDevices testEnvironmentDevices) {
			testEnvironmentDAO.add(testEnvironmentDevices);
			
		}
		@Override
		@Transactional
		public boolean isTestEnviromentdevicesExistingByName(TestEnvironmentDevices testenviornmentdevices) 
		{
			return testEnvironmentDAO.isTestEnviromentdevicesExistingByName(testenviornmentdevices);
		}
		@Override
		@Transactional
		public List<TestEnvironmentDevices> listTestEnvironmentDevicesByName() {
			return testEnvironmentDAO.listTestEnviornmnetsByName(new String[]{"testEnvironmentDevicesId","name"});
		}
		
		@Override
		@Transactional
		public List<TestEnvironmentDevices> listTestEnvironmentDevicesBydescription(
				int testEnvironmentDevicesId) {
			return testEnvironmentDAO.listTestEnviornmnetsByDescription(testEnvironmentDevicesId,new String[]{"description","DescriptionName"});	
		}
		
		@Override
		@Transactional
		public void addDeviceToTestEnvironment(Integer testEnvironmentDevicesId, Integer deviceListId) {
			testEnvironmentDAO.addDeviceToTestEnvironment(testEnvironmentDevicesId, deviceListId);
		}
		@Override
		public TestEnvironmentDevices getByTestEnvironmentId(
				int testEnviornmnetId) {
			return testEnvironmentDAO.getByTestEnviornmentId(testEnviornmnetId);
		}
				
}
