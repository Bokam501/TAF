package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.DeviceStatus;
import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;

@Repository
public class DeviceListDAOImpl implements DeviceListDAO {
	private static final Log log = LogFactory.getLog(DeviceListDAOImpl.class);
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void delete(DeviceList deviceList) {
		log.debug("deleting DeviceList instance");
		try {
			sessionFactory.getCurrentSession().delete(deviceList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public void add(DeviceList deviceList) {
		log.debug("adding DeviceList instance");
		try {
			sessionFactory.getCurrentSession().save(deviceList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
				
	}

	@Override
	@Transactional
	public void update(DeviceList deviceList) {
		log.debug("updating DeviceList instance");
		try {
			
			deviceList.setDeviceModelMaster(getDeviceModelByName(deviceList.getDeviceModelMaster().getDeviceModel()));
			sessionFactory.getCurrentSession().saveOrUpdate(deviceList);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
	
	@Override
	@Transactional
	public void genericupdate(GenericDevices deviceList) {
		log.debug("updating DeviceList instance");
		try {
			sessionFactory.getCurrentSession().update(deviceList);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
    
	private DeviceModelMaster getDeviceModelByName(String model) {
		log.debug("getDeviceModelByName");
		DeviceModelMaster deviceModelMaster=null;
		try {
			deviceModelMaster=(DeviceModelMaster) sessionFactory.getCurrentSession().createQuery("from DeviceModelMaster d where deviceModel=:deviceModel")
			.setParameter("deviceModel", model)
	                .list().get(0);
			log.debug("getDeviceModelByName successful");
		} catch (RuntimeException re) {
			log.error("getDeviceModelByName failed", re);
		}
		return deviceModelMaster;
	}

	
	@Override
	@Transactional
	public List<DeviceList> list() {
		log.debug("listing all DeviceList instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList").list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return deviceList;
	}
	
	@Override
	@Transactional
    public List<DeviceList> list(int startIndex, int pageSize) {
		log.debug("listing DeviceList instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d order by d.commonActiveStatusMaster.status asc")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getDeviceLab());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return deviceList;       
    }
	
	@Override
	@Transactional
	public DeviceList getByDeviceListId(int deviceListId){
		log.debug("getByDeviceListId");
		DeviceList deviceList=null;
		try {
			List list =sessionFactory.getCurrentSession().createQuery("from DeviceList d where deviceListId=:deviceListId").setParameter("deviceListId", deviceListId).list();
			deviceList=(list!=null && list.size()!=0)?(DeviceList) list.get(0):null;
			if (deviceList != null) {
				Hibernate.initialize(deviceList.getDeviceModelMaster().getDeviceMakeMaster());
				Hibernate.initialize(deviceList.getCommonActiveStatusMaster());
				Hibernate.initialize(deviceList.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
				Hibernate.initialize(deviceList.getHostList());
			}
			log.debug("getByDeviceListId successful");
		} catch (RuntimeException re) {
			log.error("getByDeviceListId failed", re);
		}
		return deviceList;
        
	}
	
	@Override
	@Transactional
	public DeviceList getByDeviceId(String deviceId){
		log.debug("getByDeviceId");
		DeviceList deviceList=null;
		try {
			List list =sessionFactory.getCurrentSession().createQuery("from DeviceList d where deviceId=:deviceId")
					.setParameter("deviceId", deviceId).list();
			deviceList=(list!=null && list.size()!=0)?(DeviceList)list.get(0):null;
			if (deviceList != null) {
				Hibernate.initialize(deviceList.getHostList());
				Hibernate.initialize(deviceList.getDeviceModelMaster().getDeviceMakeMaster());
				Hibernate.initialize(deviceList.getCommonActiveStatusMaster());
				Hibernate.initialize(deviceList.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
			}
			log.debug("getByDeviceId successful");
		} catch(IndexOutOfBoundsException ie){
		} catch (RuntimeException re) {
			log.error("getByDeviceId failed", re);
		}
		return deviceList;
        
	}
	@Override
	@Transactional
	public int totalRecordsCount() {
		log.debug("getting Device List total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from device_list").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}
	
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting DeviceList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from device_list").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<DeviceList> list(int devicePlatformVersionlistId, String[] parameters) {
		log.debug("listing parameterized DeviceList instance");
		List<DeviceList> deviceList=null;
		try {
			StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("d."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d where devicePlatformVersionlistId=:devicePlatformVersionlistId")
			.setParameter("devicePlatformVersionlistId", devicePlatformVersionlistId)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return deviceList;      
	}

	@Override
	@Transactional
	public List<DeviceList> listDevicesForPlatformVersion(String devicePlatformVersion, String[] parameters) {
		log.debug("listing parameterized DeviceList instance");
		
		List<DeviceList> deviceList=null;
		try {
		StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("d."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d where devicePlatformVersionlistId=:devicePlatformVersion")
					.setParameter("devicePlatformVersion", devicePlatformVersion)
			                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceListId());
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		
		return deviceList;      
	}
	@Override
	@Transactional
	public List<DeviceList> listDevicesForPlatformVersion(String platformVersion) {
		log.debug("listing parameterized DeviceList instance");
		
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d where d.DevicePlatformVersionListMaster.devicePlatformVersion=:platformVersion")
					.setParameter("platformVersion", platformVersion)
					.list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		
		return deviceList;      
	}

	@Override
	@Transactional
	public List<DeviceList> listByHostId(int hostId) {
		log.debug("listing Devices by host id");
		List<DeviceList> deviceList=null;
		try {
			
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d where hostId=:hostId ORDER BY d.commonActiveStatusMaster.status,d.deviceId asc")
			.setParameter("hostId", hostId)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			log.debug("listing Devices by host id successful");
		} catch (RuntimeException re) {
			log.error("listing Devices by host id failed", re);
		}
		return deviceList;    
	}

	@Override
	@Transactional
	public void resetDevicesStatus() {
		log.debug("reset all Devices by host id status to INACTIVE");		
		try {
			sessionFactory.getCurrentSession().createQuery("update DeviceList set deviceStatus=:inactive where deviceStatus=:active")
					.setParameter("inactive", DeviceStatus.INACTIVE.toString()).setParameter("active",DeviceStatus.ACTIVE.toString()).executeUpdate();
			log.debug("reset all Devices by host id status to INACTIVE successful");
		} catch (RuntimeException re) {
			log.error("reset all Devices by host id status to INACTIVE failed", re);
		}
		
	}
	
	@Override
	@Transactional
	public void resetDevicesStatus(String deviceId) {
		log.debug("reset all Devices by host id status to ACTIVE");		
		try {
			sessionFactory.getCurrentSession().createQuery("update DeviceList set deviceStatus=:active where deviceId=:deviceId")
					.setParameter("active", DeviceStatus.ACTIVE.toString()).setParameter("deviceId",deviceId).executeUpdate();
			log.debug("reset all Devices by host id status to ACTIVE successful");
		} catch (RuntimeException re) {
			log.error("reset all Devices by host id status to ACTIVE failed", re);
		}
		
	}
	
	@Override
	@Transactional
	public void resetDevicesStatus(int hostId) {
		log.debug("reset all Devices status to INACTIVE");		
		try {
			sessionFactory.getCurrentSession().createQuery("update DeviceList set deviceStatus=:inactive where deviceStatus=:active and hostId=:hostId")
					.setParameter("inactive", DeviceStatus.INACTIVE.toString()).setParameter("active",DeviceStatus.ACTIVE.toString())
					.setParameter("hostId",hostId)
					.executeUpdate();
			log.debug("reset all Devices status to INACTIVE successful");
		} catch (RuntimeException re) {
			log.error("reset all Devices status to INACTIVE failed", re);
		}
		
	}
	
	@Override
	@Transactional
    public List<DeviceList> list(String deviceStatus) {
		log.debug("listing DeviceList instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList where deviceStatus=:deviceStatus")
					.setParameter("deviceStatus", deviceStatus)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			throw re;
		}
		return deviceList;       
    }
	
	@Override
	@Transactional
    public List<DeviceList> list(String deviceStatus, int startIndex, int pageSize) {
		log.debug("listing DeviceList instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList where deviceStatus=:deviceStatus")
					.setParameter("deviceStatus", deviceStatus)
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			throw re;
		}
		return deviceList;       
    }

	@Override
	@Transactional
	public DeviceList getByTestEnvironmentDevicesId(int deviceListId) {
		log.debug("getting getByTestEnvironmentDevices instance by id");
		
		DeviceList deviceList=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from DeviceList d where deviceListId=:deviceListId").setParameter("deviceListId", deviceListId)
					.list();
			deviceList=(list!=null && list.size()!=0)?(DeviceList)list.get(0):null;
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return deviceList;
        
	}

	@Override
	@Transactional
	public List<DeviceList> listTestEnvironmentDevices(
			int testEnvironmentDevicesId, int startIndex, int pageSize) {
		log.debug("listing specific listTestEnvironmentDevices instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices where testEnvironmentDevicesId=:testEnvironmentDevicesId")
														.setParameter("testEnvironmentDevicesId", testEnvironmentDevicesId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return deviceList;
	}

	@Override
	@Transactional
	public List<DeviceList> listTestEnvironmentDevices(int startIndex,
			int pageSize) {
		log.debug("listing listTestEnvironmentDevices instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return deviceList;  
	}

	@Override
	@Transactional
	public List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId) {
		log.debug("listing specific listTestEnvironmentDevices instance");
		
		List<DeviceList> deviceList=null;
		try {
			String sql="select * from device_list d where d.deviceListId in(select te.deviceListId from test_environment_devices_has_device_list te where te.testEnvironmentDevicesId=:testEnvironmentDevicesId)";
			deviceList=sessionFactory.getCurrentSession().createSQLQuery("sql").list();
			log.debug("list specific successful");
			
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return deviceList;
	}

	@Override
	@Transactional
	public List<DeviceList> listTestEnvironmentDevices() {
		log.debug("listing all TestCaseList instance");
		List<DeviceList> deviceList=null;
		try {
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return deviceList;
	}
	
	@Override
	@Transactional
	public void deleteDevice(int deviceListId) {
		log.debug("deleting DeviceList instance");
		try {
			sessionFactory.getCurrentSession().delete(deviceListId);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public List<DeviceList> getDevicesOfTestEnvironment(int testEnvironmentDevicesId) {
		log.debug("getting devices for test environment");
		TestEnvironmentDevices testEnvironment = null;
		List<DeviceList> deviceList=null;
		try {
			
			testEnvironment = (TestEnvironmentDevices) sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentDevicesId);
			if (testEnvironment == null) {
				log.debug("Test Environment with specified id not found : " + testEnvironmentDevicesId);
				return null;
			}
			
			Set<DeviceList> devices = testEnvironment.getDeviceList();
			for (DeviceList dl : devices) {
				Hibernate.initialize(dl);
				Hibernate.initialize(dl.getDeviceId());
				Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
				Hibernate.initialize(dl.getCommonActiveStatusMaster());
				Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
				Hibernate.initialize(dl.getHostList());
			}
			deviceList = new ArrayList<DeviceList>();
			deviceList.addAll(devices);
			log.debug("Found Test Environment by ID successfully");
		} catch (RuntimeException re) {
			log.error("Failed to find Test Environment by ID", re);
		}
		return deviceList;	
		}

	@Override
	@Transactional
	public boolean isDeviceExistById(String deviceId) {
		String hql = "from DeviceList dl where deviceId =:deviceId";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("deviceId", deviceId)
					.list();
		if (list != null  && !list.isEmpty()) 
		    return true;
		else 
			return false;
	}
	
	
	@Override
	@Transactional
	public List<DeviceList> listHostIdByDevice(String deviceId) {
		log.debug("listing host by device id");
		List<DeviceList> deviceList=null;
		try {
			
			deviceList=sessionFactory.getCurrentSession().createQuery("from DeviceList d where deviceId=:deviceId")
			.setParameter("deviceId", deviceId)
	                .list();
			if (!(deviceList == null || deviceList.isEmpty())){
				for(DeviceList dl : deviceList){
					Hibernate.initialize(dl.getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(dl.getCommonActiveStatusMaster());
					Hibernate.initialize(dl.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(dl.getHostList());
				}
			}
			log.debug("listing Devices by host id successful");
		} catch (RuntimeException re) {
			log.error("listing Devices by host id failed", re);
		}
		return deviceList;    
	}

	@Override
	@Transactional
	public DeviceLab getDeviceLabByDeviceLabId(Integer deviceLabId) {
		log.debug("getting ProductMaster instance by id");
		DeviceLab deviceLab=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from DeviceLab deviceLab where device_lab_Id=:deviceLabId").setParameter("deviceLabId", deviceLabId)
					.list();
			deviceLab=(list!=null && list.size()!=0)?(DeviceLab)list.get(0):null;
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return deviceLab;
	}

	@Override
	@Transactional
	public DeviceModelMaster getDeviceModelMasterById(
			Integer deviceModelMasterId) {
		log.debug("getting ProductMaster instance by id");
		DeviceModelMaster deviceModelMaster=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from DeviceModelMaster deviceModelMaster where deviceModelListId=:deviceModelMasterId").setParameter("deviceModelMasterId", deviceModelMasterId)
					.list();
			deviceModelMaster=(list!=null && list.size()!=0)?(DeviceModelMaster)list.get(0):null;
			if(deviceModelMaster != null){
				if(deviceModelMaster.getDeviceMakeMaster() != null){
					Hibernate.initialize(deviceModelMaster.getDeviceMakeMaster());
				}
				
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return deviceModelMaster;
	}

	@Override
	@Transactional
	public DeviceMakeMaster getDeviceMakeMasterById(Integer deviceMakeId) {
		log.debug("getDeviceMakeMasterById instance by id");
		DeviceMakeMaster deviceMakeMaster  = null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from DeviceMakeMaster deviceMakeMaster where deviceMakeId=:devMakeId").setParameter("devMakeId", deviceMakeId)
					.list();
			deviceMakeMaster=(list!=null && list.size()!=0)?(DeviceMakeMaster)list.get(0):null;
			if(deviceMakeMaster != null){
				if(deviceMakeMaster.getDeviceModelMasters() != null && deviceMakeMaster.getDeviceModelMasters().size() >0){
					Hibernate.initialize(deviceMakeMaster.getDeviceModelMasters());
				}				
			}			
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return deviceMakeMaster;
	}
	
	@Override
	@Transactional
	public PlatformType getPlatFormType(Integer platformTypeId) {
		log.debug("getting ProductMaster instance by id");
		PlatformType platformType=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from PlatformType pformType where platformId=:platformTypeId").setParameter("platformTypeId", platformTypeId)
					.list();
			platformType=(list!=null && list.size()!=0)?(PlatformType)list.get(0):null;
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return platformType;
	}

	@Override
	@Transactional
	public List<PlatformType> listPlatformType() {
		List<PlatformType> platformTypeList=null;
		try {
			platformTypeList=sessionFactory.getCurrentSession().createQuery("from PlatformType").list();
			if (!(platformTypeList == null || platformTypeList.isEmpty())){
				for(PlatformType platformType : platformTypeList){
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return platformTypeList;
	}

	
	@Override
	@Transactional
	public String getPlatformTypeVersionByplatformId(int platformId) {
		log.debug("getting PlatformTypeVersionByplatformId");
		String platformTypeVersion ="";
		try {
			platformTypeVersion=sessionFactory.getCurrentSession().createSQLQuery("select version from platformtype where platformId=:platformversionId")
					.setParameter("platformversionId", platformId)
					.uniqueResult().toString();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return platformTypeVersion;	
	}
	@Override
	@Transactional
	public void updateTestRunJob(TestRunJob testRunJob) {
			log.debug("updateTestRunJob");
			try {
				sessionFactory.getCurrentSession().saveOrUpdate(testRunJob);
				log.debug("update successful");
			} catch (RuntimeException re) {
				log.error("update failed", re);
			}
	}

	@Override
	@Transactional
	public int addDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		log.info("addDeviceModelMaster instance by name");
		try {
			sessionFactory.getCurrentSession().save(deviceModelMaster);
			
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return deviceModelMaster.getDeviceModelListId();
	}
	
	@Override
	@Transactional
	public void updateDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		log.info("updateDeviceModelMaster instance by name");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(deviceModelMaster);
			log.debug("updated successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}
	
	@Override
	@Transactional
	public int addPlatformType(PlatformType platformType) {
		log.info("addPlatformType ");
		try {
			sessionFactory.getCurrentSession().save(platformType);			
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return platformType.getPlatformId();
	}
	
	@Override
	@Transactional
	public int addDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		log.info("addDeviceMakeMaster instance by name");
		try {
			sessionFactory.getCurrentSession().save(deviceMakeMaster);
			
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return deviceMakeMaster.getDeviceMakeId();
	}

	@Override
	@Transactional
	public DeviceModelMaster getDeviceModelMaster(String deviceModel) {
		log.debug("getting getDeviceModelMaster instance by name");
		
		List<DeviceModelMaster> deviceModelMasters=null;
		DeviceModelMaster deviceModelMaster=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DeviceModelMaster.class, "dmm");
			c.add(Restrictions.eq("dmm.deviceModel",deviceModel));
		
			deviceModelMasters=c.list();
			deviceModelMaster = (deviceModelMasters != null && deviceModelMasters.size() != 0) ? (DeviceModelMaster)(deviceModelMasters.get(0)): null;				
			if(deviceModelMaster != null){
				if(deviceModelMaster.getDeviceMakeMaster() != null){
					Hibernate.initialize(deviceModelMaster.getDeviceMakeMaster());	
				}				
			}
			log.debug("deviceModelMaster successful");
		} catch (Exception re) {
			log.error("deviceModelMaster failed", re);
		}
		return deviceModelMaster;
	}
	
	@Override
	@Transactional
	public PlatformType getPlatFormType(String devicePlatformName,
			String devicePlatformVersion) {
		log.debug("getting getDeviceModelMaster instance by name");
		
		List<PlatformType> platformTypes=null;
		PlatformType platformType=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(PlatformType.class, "pt");
			if(devicePlatformName!=null)
			c.add(Restrictions.eq("pt.name",devicePlatformName));
			if(devicePlatformVersion!=null)
			c.add(Restrictions.eq("pt.version",devicePlatformVersion));

			platformTypes=c.list();
			platformType = (platformTypes != null && platformTypes.size() != 0) ? (PlatformType)(platformTypes.get(0)): null;				
			
			log.debug("PlatformType successful");
		} catch (Exception re) {
			log.error("PlatformType failed", re);
		}
		return platformType;
	}

	@Override
	@Transactional
	public PlatformType getPlatFormTypeById(Integer platformId) {
		
		log.debug("getting getPlatFormTypeById instance by Id");
		
		List<PlatformType> platformTypes=null;
		PlatformType platformType=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(PlatformType.class, "pt");
			c.add(Restrictions.eq("pt.platformId",platformId));
			platformTypes=c.list();
			platformType = (platformTypes != null && platformTypes.size() != 0) ? (PlatformType)(platformTypes.get(0)): null;				
			
			log.debug("PlatformType successful");
		} catch (Exception re) {
			log.error("PlatformType failed", re);
		}
		return platformType;
	}
	@Override
	@Transactional
	public DeviceMakeMaster getDeviceMakeMaster(String deviceMake) {
		log.debug("getting DeviceMakeMaster instance by name");
		
		List<DeviceMakeMaster> deviceMakeMasters=null;
		DeviceMakeMaster deviceMakeMaster=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DeviceMakeMaster.class, "dmm");
			c.add(Restrictions.eq("dmm.deviceMake",deviceMake));

			deviceMakeMasters=c.list();
			deviceMakeMaster = (deviceMakeMasters != null && deviceMakeMasters.size() != 0) ? (DeviceMakeMaster)(deviceMakeMasters.get(0)): null;				
			
			log.debug("DeviceMakeMaster successful");
		} catch (Exception re) {
			log.error("DeviceMakeMaster failed", re);
		}
		return deviceMakeMaster;
	}
	
	@Override
	@Transactional
	public List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId) {
		log.info("Get UnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel");
		List<Object[]> genDeviceObj = new ArrayList<Object[]>();
		List<JsonGenericDevice> jsonGenericDeviceList= new ArrayList<JsonGenericDevice>();
		List<Object[]> runconfigObjList = new ArrayList<Object[]>();
		Integer testRunPlanId = 0;
		String sql = "";
		try {
			//Check if WP was created through TestRunPlan
			sql = "select wp.testRunPlanId from workpackage wp where workPackageId=:wpid";
			Object tRPlanIdObject = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workpackageId).uniqueResult();
			if(tRPlanIdObject != null){
				testRunPlanId = ((Number)tRPlanIdObject).intValue();
			}
			//if created through TRPlan, then fetch Mapped Host from WPRunconfig(WorkpackageRunConfiguration) table
			if(testRunPlanId != null && testRunPlanId != 0){
				//Check if Devices were already Mapped To WorkPackage(At WorkpackageRunConfiguration).				
				runconfigObjList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc join rc.workPackageRunConfigSet whr "+
						"  where whr.workpackage.workPackageId=:wpid and rc.runconfigId=whr.runconfiguration.runconfigId and "+
						 "rc.runConfigStatus=:rconfigStat and rc.genericDevice.genericsDevicesId is not null and "+
						"rc.environmentcombination.environment_combination_id =:envcombid group by whr.runconfiguration.runconfigId order by whr.runconfiguration.runconfigId")
						.setParameter("wpid", workpackageId)
						.setParameter("envcombid", ecId)						
						.setParameter("rconfigStat",runConfigStatus )
						.list();
				if(runconfigObjList != null && !runconfigObjList.isEmpty() && runconfigObjList.size() != 0 ){
					//Few Devices were already mapped to WP through TRPlan, then (Product has Device - Mapped Device)
					genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
							"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
							+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid where  phd.deviceId not in"							
							+ " (select rf.deviceId from runconfiguration rf  join workpackage_has_runconfiguration whr "
							+ "where whr.workPackageId=:wpid and  rf.runconfigId=whr.runconfigurationId and "
							+ "rf.runConfigStatus=:rconfigStat and rf.deviceId is not null and "
							+ "rf.environmentcombinationId=:envcombid group by whr.runconfigurationId order by whr.runconfigurationId)")
							.setParameter("pid", productId)
							.setParameter("envcombid", ecId)
							.setParameter("wpid", workpackageId)
							.setParameter("rconfigStat", runConfigStatus)
							.list();
				}else if(runconfigObjList == null || runconfigObjList.isEmpty() || runconfigObjList.size() == 0){
					//No Devices were mapped to WorkPackage so far// So fetch Devices from Product has Device			
					genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
							"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
							+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid")
							.setParameter("pid", productId)							
							.list();
				}
			}
			else{//if WP was created manually and not through TestRunPlan
				//Check if Devices were already Mapped To WorkPackage(At RunConfiguration).
				/*genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select rf.deviceId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.deviceId !=::nullobject")
						.setParameter("envcombId", ecId)
						.setParameter("wpId", workpackageId)
						.setParameter("rcStatus", runConfigStatus)
						.setParameter("nullobject", "NULL")
						.list();*/
				genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select rf.deviceId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.deviceId is not null")			
						.setParameter("envcombId", ecId)
						.setParameter("wpId", workpackageId)
						.setParameter("rcStatus", runConfigStatus)
						.list();
				if(genDeviceObj != null && !genDeviceObj.isEmpty() && genDeviceObj.size() != 0 ){
					//Few Devices were already mapped to WorkPackage
					genDeviceObj = null;
					genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
							"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
							+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid where  phd.deviceId not in"
						//	+ " (select rf.deviceId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.deviceId!=:nullobject)")
							+ " (select rf.deviceId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.deviceId is not null)")
							.setParameter("pid", productId)
							.setParameter("envcombId", ecId)
							.setParameter("wpId", workpackageId)
							.setParameter("rcStatus", runConfigStatus)
							//.setParameter("nullobject", "NULL")
							.list();
				}else if(genDeviceObj == null || genDeviceObj.isEmpty() || genDeviceObj.size() == 0){
					//No Devices were mapped to WorkPackage so far					
					genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
							"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
							+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid")
							.setParameter("pid", productId)							
							.list();
				}				
			}			
			for (Object genDeviceobj[] : genDeviceObj) {
				JsonGenericDevice jsonGD = new JsonGenericDevice();
				if(genDeviceobj[0] != null){
					Integer gdtId = (Integer)genDeviceobj[0];
					jsonGD.setGenericsDevicesId(gdtId);
				}
				if(genDeviceobj[1] != null){
					String gdName = (String)genDeviceobj[1];
					jsonGD.setName(gdName);
				}
				if(genDeviceobj[2] != null){
					String gdDescription = (String)genDeviceobj[2];
					jsonGD.setDescription(gdDescription);
				}
				if(genDeviceobj[3] != null){
					String gdStatus = (String)genDeviceobj[3];
					jsonGD.setStatus(Integer.parseInt(gdStatus));
				}
				if(genDeviceobj[4] != null){
					Integer gdDevLabId = (Integer)genDeviceobj[4];
					jsonGD.setDeviceLabId(gdDevLabId);
				}
				if(genDeviceobj[5] != null){
					Integer gddeviceModelId = (Integer)genDeviceobj[5];
					jsonGD.setDeviceModelMasterId(gddeviceModelId);
				}
				if(genDeviceobj[6] != null){
					Integer gdprodId = (Integer)genDeviceobj[6];
					jsonGD.setProductId(gdprodId);				
				}			
				if(genDeviceobj[7] != null){					
					String gdUDID = (String)genDeviceobj[7];
					jsonGD.setUDID(gdUDID);		
				}	
				if(genDeviceobj[6] != null){					
					Integer gdavailableStatus = (Integer)genDeviceobj[6];
					jsonGD.setAvailableStatus(gdavailableStatus);		
				}			
				jsonGenericDeviceList.add(jsonGD);
			}			
			return jsonGenericDeviceList;
		} catch (HibernateException e1) {
			log.error("Error fetching UnMappedDevice Of Product from RunConfigurationWorkPackageLevel",e1);
		}
		return jsonGenericDeviceList;
	}
	
	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfGenDeviceOfWPExisting(Integer environmentCombinationId, Integer workpackageId, Integer deviceId) {
		log.debug("isRunConfigurationOfGenDeviceOfWPExisting");
		RunConfiguration runconfig = null;
		List<RunConfiguration> runconfigList = null;
		try {
			runconfigList=sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
					+ " rc.workPackage.workPackageId=:wpId and rc.genericDevice.genericsDevicesId=:genDeviceId")
					.setParameter("ecid",environmentCombinationId )
					.setParameter("wpId", workpackageId)
					.setParameter("genDeviceId",deviceId ).list();					
			runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList.get(0) : null;
			if (runconfig != null) {
				Hibernate.initialize(runconfig.getTestRunPlanSet());
				Hibernate.initialize(runconfig.getEnvironmentcombination());	
				if(runconfig.getGenericDevice() != null){
					GenericDevices gd = runconfig.getGenericDevice();
					Hibernate.initialize(gd);
					if(gd.getDeviceLab() != null){
						Hibernate.initialize(gd.getDeviceLab());
					}
					if(gd.getDeviceModelMaster() != null){						
						Hibernate.initialize(gd.getDeviceModelMaster());
					}
					if(gd.getPlatformType() != null){						
						Hibernate.initialize(gd.getPlatformType());
					}
					if(gd.getProductMaster() != null){						
						Hibernate.initialize(gd.getProductMaster());						
					}
				}
			}
			return runconfig;
		} catch (RuntimeException re) {
			log.error("isRunConfigurationOfGenDeviceOfWPExisting Error ", re);
		}		
		return runconfig;
	}
	@Override
	@Transactional
	public List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId) {
		log.info("Get getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel");
		List<Object[]> genDeviceObj = new ArrayList<Object[]>();
		List<JsonGenericDevice> jsonGenericDeviceList= new ArrayList<JsonGenericDevice>();
		try {
			
			
			//Check if Devices were already Mapped To TestRunPlan(At RunConfiguration).
			genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select * from runconfiguration rf where environmentcombinationId=:envcombId and testRunPlanId=:trplanId and runConfigStatus =:rcStatus and rf.deviceId !=:nullobject")
					.setParameter("envcombId", ecId)
					.setParameter("trplanId", testRunPlanId)
					.setParameter("rcStatus", runConfigStatus)
					.setParameter("nullobject", "NULL")
					.list();
			
			if(genDeviceObj != null || !genDeviceObj.isEmpty() || genDeviceObj.size() != 0 ){
				//Few Devices were already mapped to TestRunPlan
				genDeviceObj = null;
				genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
						"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
						+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid where  phd.deviceId not in"
						+ " (select rf.deviceId from runconfiguration rf where environmentcombinationId=:envcombId and testRunPlanId=:trplanId and runConfigStatus =:rcStatus and rf.deviceId != 'NULL')")
						.setParameter("pid", productId)
						.setParameter("envcombId", ecId)
						.setParameter("trplanId", testRunPlanId)
						.setParameter("rcStatus", runConfigStatus)
						.list();
			}else if(genDeviceObj == null || genDeviceObj.isEmpty() || genDeviceObj.size() == 0){
				//No Device were mapped to TestRunPlan so far
				genDeviceObj = sessionFactory.getCurrentSession().createSQLQuery("select gd.genericsDevicesId, gd.name, gd.description, gd.status, gd.deviceLabId, gd.deviceModelId,"+
						"gd.productId, gd.UDID, gd.availableStatus from generic_devices gd inner join product_has_device phd "
						+ "on gd.genericsDevicesId=phd.deviceId and phd.productId=:pid")
						.setParameter("pid", productId)
						.list();
			}
			if(genDeviceObj != null || !genDeviceObj.isEmpty() || genDeviceObj.size() != 0){
				for (Object genDeviceobj[] : genDeviceObj) {
					JsonGenericDevice jsonGD = new JsonGenericDevice();
				
					if(genDeviceobj[0] != null){
						Integer gdtId = (Integer)genDeviceobj[0];
						jsonGD.setGenericsDevicesId(gdtId);
					}
					if(genDeviceobj[1] != null){
						String gdName = (String)genDeviceobj[1];
						jsonGD.setName(gdName);
					}
					if(genDeviceobj[2] != null){
						String gdDescription = (String)genDeviceobj[2];
						jsonGD.setDescription(gdDescription);
					}
					if(genDeviceobj[3] != null){
						String gdStatus = (String)genDeviceobj[3];
						jsonGD.setStatus(Integer.parseInt(gdStatus));
					}
					if(genDeviceobj[4] != null){
						Integer gdDevLabId = (Integer)genDeviceobj[4];
						jsonGD.setDeviceLabId(gdDevLabId);
					}
					if(genDeviceobj[5] != null){
						Integer gddeviceModelId = (Integer)genDeviceobj[5];
						jsonGD.setDeviceModelMasterId(gddeviceModelId);
					}
					if(genDeviceobj[6] != null){
						Integer gdprodId = (Integer)genDeviceobj[6];
						jsonGD.setProductId(gdprodId);				
					}			
					if(genDeviceobj[7] != null){					
						String gdUDID = (String)genDeviceobj[7];
						jsonGD.setUDID(gdUDID);		
					}	
					if(genDeviceobj[8] != null){					
						Integer gdavailableStatus = (Integer)genDeviceobj[8];
						jsonGD.setAvailableStatus(gdavailableStatus);		
					}			
					jsonGenericDeviceList.add(jsonGD);
				}	
			}
			return jsonGenericDeviceList;
		} catch (HibernateException e1) {
			log.error("Error fetching UnMappedDevice Of Product from RunConfigurationTestRunPlanLevel", e1);
		}
		return jsonGenericDeviceList;
	}
	
	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId, Integer runConfigStatus) {
		log.debug("getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel");
		
		List<RunConfiguration> runconfigList = null;
		List<JsonRunConfiguration> jsonRunconfigList = new ArrayList<JsonRunConfiguration>();
		
		try {
			runconfigList=sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
					+ " rc.testRunPlan.testRunPlanId=:trplanId and rc.runConfigStatus=:rconfigStat and rc.genericDevice.genericsDevicesId is not null")
					.setParameter("ecid",environmentCombinationId )
					.setParameter("trplanId",testRunPlanId )
					.setParameter("rconfigStat",runConfigStatus )
					.list();			
			for (RunConfiguration runConfiguration : runconfigList) {
				if (runConfiguration != null) {
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					
					if(runConfiguration.getHostList() != null){
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
							if(hl.getCommonActiveStatusMaster() != null){
								CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
								Hibernate.initialize(casm);
							}
							if(hl.getHostTypeMaster() != null){
								HostTypeMaster htm = hl.getHostTypeMaster();
								Hibernate.initialize(htm);
							}
							if(hl.getHostPlatformMaster() != null){
								HostPlatformMaster hpfm = hl.getHostPlatformMaster();
								Hibernate.initialize(hpfm);
							}
					}					
					if(runConfiguration.getTestRunPlan() != null){
						if(runConfiguration.getTestRunPlan() != null){
							TestRunPlan trunplan = runConfiguration.getTestRunPlan();
							Hibernate.initialize(trunplan);					
							if(trunplan.getProductVersionListMaster() != null){
								ProductVersionListMaster version = trunplan.getProductVersionListMaster();
								Hibernate.initialize(version);						
								if(version.getProductMaster() != null){
									ProductMaster prod = version.getProductMaster();
									Hibernate.initialize(prod);
									if(prod.getProductType() != null){
										ProductType ptype = prod.getProductType();
										Hibernate.initialize(ptype);								
									}
								}
							}
						}					
					}
					if(runConfiguration.getHostList() != null){
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
						if(hl.getCommonActiveStatusMaster() != null){
							CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}
					jsonRunconfigList.add(new JsonRunConfiguration(runConfiguration));
				}
			}			
			return jsonRunconfigList;
		} catch (RuntimeException re) {
			log.error("getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel Error ", re);
		}		
		return jsonRunconfigList;
	}
	
	
	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId, Integer runConfigStatus) {
		log.debug("getMappedHostListFromRunconfigurationWorkPackageLevel");		
		List<RunConfiguration> runconfigList = new ArrayList<RunConfiguration>();
		List<JsonRunConfiguration> jsonRunconfigList = new ArrayList<JsonRunConfiguration>();
		List<Object[]> runconfigObjList = new ArrayList<Object[]>();
		Integer testRunPlanId = 0;
		String sql = "";		
		try {
			//Check if WP was created through TestRunPlan
			sql = "select wp.testRunPlanId from workpackage wp where workPackageId=:wpid";
			//testRunPlanId = ((Number)(sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId).uniqueResult())).intValue();
			Object tRPlanIdObject = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId).uniqueResult();
			if(tRPlanIdObject != null){
				testRunPlanId = ((Number)tRPlanIdObject).intValue();
			}
			//if created through TRPlan, then fetch Mapped Devices from WPRunconfig(WorkpackageRunConfiguration) table
			if(testRunPlanId != null && testRunPlanId != 0){
				runconfigObjList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc join rc.workPackageRunConfigSet whr "+
						"  where whr.workpackage.workPackageId=:wpid and rc.runconfigId=whr.runconfiguration.runconfigId and "+
						 "rc.runConfigStatus=:rconfigStat and rc.genericDevice.genericsDevicesId is not null and "+
						"rc.environmentcombination.environment_combination_id =:envcombid group by whr.runconfiguration.runconfigId order by whr.runconfiguration.runconfigId")
						.setParameter("wpid", workPackageId)
						.setParameter("envcombid", environmentCombinationId)						
						.setParameter("rconfigStat",runConfigStatus )
						.list();
				//jsonRunconfigList=	getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(environmentCombinationId, testRunPlanId, runConfigStatus);				
			}else{//else if WP was created manually,
				//Fetch Mapped Devices of WP
				runconfigList=sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
						+ " rc.workPackage.workPackageId=:wpId and rc.runConfigStatus=:rconfigStat and rc.genericDevice.genericsDevicesId is not null")
						.setParameter("ecid",environmentCombinationId )
						.setParameter("wpId",workPackageId )
						.setParameter("rconfigStat",runConfigStatus )
						.list();
			}								
			if(runconfigObjList != null && !runconfigObjList.isEmpty()){
				for (Object[] runconfigwprunconfigobj : runconfigObjList) {	
					runconfigList.add((RunConfiguration)runconfigwprunconfigobj[0]);
				}
			}
			if(runconfigList != null && !runconfigList.isEmpty()){
				for (RunConfiguration runConfiguration : runconfigList) {
					if (runConfiguration != null) {
						Hibernate.initialize(runConfiguration.getTestRunPlanSet());
						Hibernate.initialize(runConfiguration.getEnvironmentcombination());
						if(runConfiguration.getGenericDevice() != null){
							GenericDevices gd = runConfiguration.getGenericDevice();
							Hibernate.initialize(gd);	
							if(gd.getHostList() != null){
								HostList hl = gd.getHostList();
								Hibernate.initialize(hl);
								if(hl.getCommonActiveStatusMaster() != null){
									CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
									Hibernate.initialize(casm);
								}
							}
						}						
						if(runConfiguration.getTestRunPlan() != null){
							if(runConfiguration.getTestRunPlan() != null){
								TestRunPlan trunplan = runConfiguration.getTestRunPlan();
								Hibernate.initialize(trunplan);					
								if(trunplan.getProductVersionListMaster() != null){
									ProductVersionListMaster version = trunplan.getProductVersionListMaster();
									Hibernate.initialize(version);						
									if(version.getProductMaster() != null){
										ProductMaster prod = version.getProductMaster();
										Hibernate.initialize(prod);
										if(prod.getProductType() != null){
											ProductType ptype = prod.getProductType();
											Hibernate.initialize(ptype);								
										}
									}
								}
							}					
						}
						if(runConfiguration.getHostList() != null){
							HostList hl = runConfiguration.getHostList();
							Hibernate.initialize(hl);
							if(hl.getCommonActiveStatusMaster() != null){
								CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
								Hibernate.initialize(casm);
							}
						}
						jsonRunconfigList.add(new JsonRunConfiguration(runConfiguration));
					}
				}
			}
						
			return jsonRunconfigList;
		} catch (RuntimeException re) {
			log.error("getMappedHostListFromRunconfigurationWorkPackageLevel Error ", re);
		}		
		return jsonRunconfigList;
	}
}
