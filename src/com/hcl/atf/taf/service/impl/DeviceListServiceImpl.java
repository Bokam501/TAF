package com.hcl.atf.taf.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.DeviceMakeMasterDAO;
import com.hcl.atf.taf.dao.DeviceModelMasterDAO;
import com.hcl.atf.taf.dao.DevicePlatformMasterDAO;
import com.hcl.atf.taf.dao.DevicePlatformVersionListMasterDAO;
import com.hcl.atf.taf.dao.DeviceTypeDAO;
import com.hcl.atf.taf.dao.ProcessorDAO;
import com.hcl.atf.taf.dao.SystemTypeDAO;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.Processor;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SystemType;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class DeviceListServiceImpl implements DeviceListService {
	private static final Log log = LogFactory.getLog(DeviceListServiceImpl.class);
	
	@Autowired
    private DeviceListDAO deviceListDAO;
	@Autowired
	private DevicePlatformMasterDAO devicePlatformMasterDAO;
	@Autowired
	private DevicePlatformVersionListMasterDAO devicePlatformVersionListMasterDAO;
	@Autowired
	private DeviceMakeMasterDAO deviceMakeMasterDAO;
	@Autowired
	private DeviceModelMasterDAO deviceModelMasterDAO;
	@Autowired
	private SystemTypeDAO systemTypeDAO;
	@Autowired
	private ProcessorDAO processorDAO;
	@Autowired
	private DeviceTypeDAO deviceTypeDAO;
	@Autowired
	private WorkPackageService workPackageService;
	
	@Override
	@Transactional
	public void add(DeviceList deviceList) {
		deviceListDAO.add(deviceList);
	}
	
	@Override
	@Transactional
	public List<DeviceList> list() {	 
		return deviceListDAO.list();
	}

	@Override
	@Transactional
	public void delete(int deviceListId) {
		deviceListDAO.delete(deviceListDAO.getByDeviceListId(deviceListId));			
	}

	@Override
	@Transactional
	public void update(DeviceList deviceList) {
		deviceListDAO.update(deviceList);	
		
	}
	
	@Override
	@Transactional
	public void genericupdate(GenericDevices deviceList) {
		deviceListDAO.genericupdate(deviceList);	
		
	}


	@Override
	@Transactional
	public List<DeviceList> list(int startIndex, int pageSize) {
		
		return deviceListDAO.list(startIndex,pageSize);
	}

	@Override
	@Transactional
	public int totalRecordsCount() {
		return deviceListDAO.totalRecordsCount();
	}

	@Override
	@Transactional
	public List<DevicePlatformMaster> platformsList(int productId) {
		
		return devicePlatformMasterDAO.list(productId);
	}
	@Override
	@Transactional
	public List<DevicePlatformMaster> platformsList() {
		
		return devicePlatformMasterDAO.list();
	}

	@Override
	@Transactional
	public List<DevicePlatformVersionListMaster> platformVersionsList(String devicePlatform) {
		
		return devicePlatformVersionListMasterDAO.list(devicePlatform);
	}


	@Override
	@Transactional
	public List<DeviceMakeMaster> makeList() {
 
		return deviceMakeMasterDAO.list();
	}




	@Override
	@Transactional
	public List<DeviceModelMaster> modelList() {
 
		return deviceModelMasterDAO.list();
	}

	


	@Override
	@Transactional
	public List<DeviceModelMaster> modelList(String deviceMake) {
 
		return deviceModelMasterDAO.list(deviceMake);
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		return deviceListDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public List<DeviceList> listByHostId(int hostId) {
		
		return deviceListDAO.listByHostId(hostId);
	}

	/*
	 * 
	 * Reset device status to INAVTIVE during server restart or first time launch
	 */
	@Override
	@PostConstruct
	public void resetDevicesStatus() {
		deviceListDAO.resetDevicesStatus();
		
	}

	@Override
	@Transactional
	public void resetDevicesStatus(String deviceId) {
		deviceListDAO.resetDevicesStatus(deviceId);
		
	}
	
	@Override
	@Transactional
	public void resetDevicesStatus(int hostId) {
		deviceListDAO.resetDevicesStatus(hostId);
		
	}

	public List<DeviceList> listHostIdByDevice(String deviceId){
		return deviceListDAO.listHostIdByDevice(deviceId);
	}
	
	@Override
	@Transactional
	public void add(DevicePlatformVersionListMaster devicePlatformVersionListMaster) {
		devicePlatformVersionListMasterDAO.add(devicePlatformVersionListMaster);
				
	}
	
	@Override
	@Transactional
	public DevicePlatformVersionListMaster getDevicePlatformVersionList(DevicePlatformVersionListMaster devicePlatformVersionListMaster) {
		//create a master record in device platform version if not found
		DevicePlatformVersionListMaster tmp=devicePlatformVersionListMasterDAO.getDevicePlatformVersionByVersionCode(devicePlatformVersionListMaster.getDevicePlatformMaster().getDevicePlatformName(),devicePlatformVersionListMaster.getDevicePlatformVersion());
				if(tmp==null){
					devicePlatformVersionListMasterDAO.add(devicePlatformVersionListMaster);
				} else {
					devicePlatformVersionListMaster=tmp;
				}
		return devicePlatformVersionListMaster;
	}
	
	@Override
	@Transactional
	public DeviceModelMaster getDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		//create a master record in device model master if not found
		DeviceModelMaster tmp=deviceModelMasterDAO.getDeviceModelByName(deviceModelMaster.getDeviceModel());
		if(tmp==null){
			deviceModelMasterDAO.add(deviceModelMaster);
		} else {
			deviceModelMaster = tmp;
		}
		//deviceModelMaster=tmp;
		return deviceModelMaster;
	}
	
	@Override
	@Transactional
	public DeviceMakeMaster getDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		//create a master record in device model master if not found
		DeviceMakeMaster tmp=deviceMakeMasterDAO.getDeviceMakeByName(deviceMakeMaster.getDeviceMake());
		if(tmp==null){
			deviceMakeMasterDAO.add(deviceMakeMaster);
		} else {
			deviceMakeMaster = tmp;
		}
		//deviceModelMaster=tmp;
		return deviceMakeMaster;
	}
	
	@Override
	@Transactional
	public DeviceList getDeviceByListId(int  deviceListId) {
		return deviceListDAO.getByDeviceListId(deviceListId);
	}
	
	@Override
	@Transactional
	public DeviceList getDeviceById(String  deviceId) {
		return deviceListDAO.getByDeviceId(deviceId);
	}
	
	@Override
	@Transactional
	public List<DeviceList> list(String deviceStatus) {
		
		return deviceListDAO.list(deviceStatus);
	}
	
	@Override
	@Transactional
	public List<DeviceList> list(String deviceStatus,int startIndex, int pageSize) {
		
		return deviceListDAO.list(deviceStatus,startIndex,pageSize);
	}

	@Override
	@Transactional
	public boolean isDeviceExistById(String deviceId) {
		return deviceListDAO.isDeviceExistById(deviceId);
	}

	@Override
	public DeviceLab getDeviceLabByDeviceLabId(Integer deviceLabId) {
		return deviceListDAO.getDeviceLabByDeviceLabId(deviceLabId);
	}

	@Override
	public DeviceModelMaster getDeviceModelMasterById(Integer deviceModelMasterId) {
		return deviceListDAO.getDeviceModelMasterById(deviceModelMasterId);
	}

	@Override
	@Transactional
	public DeviceMakeMaster getDeviceMakeMasterById(Integer deviceMakeId) {		
		return deviceListDAO.getDeviceMakeMasterById(deviceMakeId);
	}
	
	@Override
	public PlatformType getPlatFormType(Integer platformTypeId) {
		return deviceListDAO.getPlatFormType(platformTypeId);
	}

	@Override
	public List<PlatformType> listPlatformType() {
		return deviceListDAO.listPlatformType();
	}

	@Override
	@Transactional
	public void updateTestRunJob(TestRunJob testRunJob) {
		deviceListDAO.updateTestRunJob(testRunJob);
	}

	@Override
	@Transactional
	public List<SystemType> listSystemType() {
		return systemTypeDAO.list();
	}

	@Override
	@Transactional
	public void addSystemType(SystemType systemType) {
		systemTypeDAO.add(systemType);
	}

	@Override
	@Transactional
	public SystemType getSystemTypeByName(String name) {
		return systemTypeDAO.getSystemTypeByName(name);
	}

	@Override
	@Transactional
	public List<Processor> listProcessor() {
		return processorDAO.list();
	}

	@Override
	@Transactional
	public void addProcessor(Processor processor) {
		processorDAO.add(processor);
	}

	@Override
	@Transactional
	public Processor getProcessorByProcessorId(Integer processorId) {
		return processorDAO.getProcessorByProcessorId(processorId);
	}

	@Override
	@Transactional
	public Processor getProcessorByProcessorName(String processorName) {
		return processorDAO.getProcessorByProcessorName(processorName);
	}

	@Override
	@Transactional
	public List<DeviceType> listDeviceType() {
		return deviceTypeDAO.list();
	}

	@Override
	@Transactional
	public void addDeviceType(DeviceType deviceType) {
		deviceTypeDAO.add(deviceType);
	}

	@Override
	@Transactional
	public DeviceType getDeviceTypeByName(String deviceTypeName) {
		return deviceTypeDAO.getDeviceTypeByName(deviceTypeName);
	}

	@Override
	@Transactional
	public DeviceType getDeviceTypeByTypeId(Integer deviceTypeId) {
		return deviceTypeDAO.getDeviceTypeByTypeId(deviceTypeId);
	}

	@Override
	@Transactional
	public DeviceModelMaster getDeviceModelMaster(String deviceModel) {
		return deviceListDAO.getDeviceModelMaster(deviceModel);
	}

	@Override
	@Transactional
	public int addDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		return deviceListDAO.addDeviceModelMaster(deviceModelMaster);
	}
	
	@Override
	@Transactional
	public void updateDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		deviceListDAO.updateDeviceModelMaster(deviceModelMaster);
	}
	
	@Override
	@Transactional
	public int addDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		return deviceListDAO.addDeviceMakeMaster(deviceMakeMaster);
	}
	
	@Override
	@Transactional
	public int addPlatformType(PlatformType platformType) {		
		return deviceListDAO.addPlatformType(platformType);
	}
	
	@Override
	@Transactional
	public PlatformType getPlatFormType(String devicePlatformName,
			String devicePlatformVersion) {
		return deviceListDAO.getPlatFormType(devicePlatformName, devicePlatformVersion);
	}
	
	@Override
	@Transactional
	public PlatformType validatePlatFormOrCreate(String devicePlatformName,
			String devicePlatformVersion) {
		PlatformType platformType = getPlatFormType(devicePlatformName,devicePlatformVersion);
			if(platformType != null) {
				if(!platformType.getVersion().equalsIgnoreCase(devicePlatformVersion) && devicePlatformVersion !=null ){
					//Adding A Platform with new version
					PlatformType platformnew = new PlatformType();
					platformnew.setName(platformType.getName());
					platformnew.setEntityMaster(platformType.getEntityMaster());
					log.info("Adding platform with new Version "+devicePlatformVersion);
					platformnew.setVersion(devicePlatformVersion);
					int platFormId =addPlatformType(platformnew);
					platformnew = getPlatFormType(platFormId);		
					return platformnew;
				}else{
					
				}
			}else if(devicePlatformVersion!=null ){//Adding A New Platform
				PlatformType platformnew = new PlatformType();
				platformnew.setName(devicePlatformName);
				EntityMaster entMas = new EntityMaster();
				entMas = workPackageService.getEntityMasterById(7);							
				platformnew.setEntityMaster(entMas);
				platformnew.setVersion(devicePlatformVersion);
				int platFormId =addPlatformType(platformnew);
				platformnew = getPlatFormType(platFormId);
				return platformnew;
			}
			return platformType;
	}
	
	@Override
	@Transactional
	public PlatformType getPlatFormTypeById(Integer platformId) {
		return deviceListDAO.getPlatFormTypeById(platformId);
	}

	@Override
	@Transactional
	public DeviceMakeMaster getDeviceMakeMaster(String deviceMake) {
		return deviceListDAO.getDeviceMakeMaster(deviceMake);
	}

	@Override
	@Transactional
	public List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId) {
		return deviceListDAO.getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(productId, ecId, runConfigStatus, workpackageId);
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfGenDeviceOfWPExisting(
			Integer environmentCombinationId, Integer workpackageId,
			Integer deviceId) {
		return deviceListDAO.isRunConfigurationOfGenDeviceOfWPExisting(environmentCombinationId, workpackageId, deviceId);
	}

	@Override
	@Transactional	
	public List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId) {
		return deviceListDAO.getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(productId, ecId, runConfigStatus, testRunPlanId);
	}

	@Override
	@Transactional	
	public List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus) {		
		return deviceListDAO.getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(environmentCombinationId,testRunPlanId, runConfigStatus);
	}

	@Override
	@Transactional	
	public List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId, Integer runConfigStatus) {
		return deviceListDAO.getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(environmentCombinationId, workPackageId, runConfigStatus);
	}

	@Override
	@Transactional
	public String getPlatformTypeVersionByplatformId(int platformId) {
		return deviceListDAO.getPlatformTypeVersionByplatformId(platformId);
	}
}
