package com.hcl.atf.taf.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.TestEnvironmentDAO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestSuiteList;

@Repository
public class TestEnvironmentDAOImpl implements TestEnvironmentDAO {
	private static final Log log = LogFactory.getLog(TestEnvironmentDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void add(TestEnvironmentDevices testEnvironmentDevices) 
	{
		log.debug("adding TestEnvironment instance");
		
		try {
			testEnvironmentDevices.setStatus(1);
			testEnvironmentDevices.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(testEnvironmentDevices);
			log.debug("add TestEnvironment successful");
		} catch (RuntimeException re) {
			log.error("add TestEnvironment failed", re);
		}		
		
	}
	@Override
    @Transactional
    public void update(TestEnvironmentDevices testEnvironmentDevices) {
           log.debug("updating Test Environment instance");
           try {
                  
        	   TestEnvironmentDevices existingTestEnvironment = null;  
                  List list = sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices t where testEnvironmentDevicesId=:testEnvironmentDevicesId").setParameter("testEnvironmentDevicesId",testEnvironmentDevices.getTestEnvironmentDevicesId())
                               .list();
                  existingTestEnvironment = (list!=null && list.size()!=0)?(TestEnvironmentDevices) list.get(0):null;
                  if (existingTestEnvironment != null) {
                	  	 existingTestEnvironment.setDescription(testEnvironmentDevices.getDescription());
                	  	 existingTestEnvironment.setName(testEnvironmentDevices.getName());
                         sessionFactory.getCurrentSession().saveOrUpdate(existingTestEnvironment);
                         log.debug("update successful");
                  } else {
                        log.error("Update of testEnvironment failed");
                  }
           } catch (RuntimeException re) {
                  log.error("update failed", re);
           }
	}
	
	@Override
	@Transactional
	public void delete(TestEnvironmentDevices testenvironmentdevices) {
		log.debug("deleting TestEnvironmentDevices instance");
		try {
	
			sessionFactory.getCurrentSession().delete(testenvironmentdevices);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}
		
	}

	@Override
	@Transactional
	public void delete(int  testEnvironmentDevicesId) {
		log.debug("deleting TestEnvironmentDevices instance");
		try {
			//Do a soft delete
			TestEnvironmentDevices testEnvironment = (TestEnvironmentDevices)sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentDevicesId);
			testEnvironment.setStatus(TAFConstants.ENTITY_STATUS_INACTIVE);
			testEnvironment.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().update(testEnvironment);
			log.debug("Soft delete successful");
		} catch (RuntimeException re) {
			log.error("Soft delete failed", re);
		}
		
	}
	

	@Override
	@Transactional
	public TestEnvironmentDevices getTestEnviornmentByName(String name) {
		log.debug("listing specific Test Envronment instance");
		TestEnvironmentDevices testEnvironmentDevices=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices where name=:name")
														.setParameter("name", name).list();
			testEnvironmentDevices=(list!=null && list.size()!=0)?(TestEnvironmentDevices)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testEnvironmentDevices;
	}
	
	@Override
	@Transactional
	public void reactivate(TestEnvironmentDevices testEnvironmentDevices) {
		log.debug("soft-deleting TestEnvironmentDevices instance");
		try {		
			testEnvironmentDevices.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
			testEnvironmentDevices.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(testEnvironmentDevices);			
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	@Override
	@Transactional
	public List<TestEnvironmentDevices> listAll() {
		log.debug("listing TestRunConfigurationParent instance");
		
		List<TestEnvironmentDevices> testenviornmentdevices=null;
		try {
			testenviornmentdevices=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices t where status=1")
										.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testenviornmentdevices;     
	}
		

	@Override
	@Transactional
	public List<TestEnvironmentDevices> listAllTestEnvironments(int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationParent instance");
		
		List<TestEnvironmentDevices> testenvironmentDevices=null;
		try {
			testenvironmentDevices=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices where status=1")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			
			log.debug("list successful");
			
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return testenvironmentDevices;     
	}
	
	@Override
	@Transactional
	public int getTotalTestEnviornmentRecords() {
		log.debug("getting TestEnviornment Devicess total records");
		
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_environment_devices where status=1").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	
	}

	@Override
	@Transactional
	public boolean isTestEnviromentdevicesExistingByName(
			TestEnvironmentDevices testEnviornmentDevices) {
		String hql = "from TestEnvironmentDevices te where name = :name";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", testEnviornmentDevices.getName().trim()).list();
		if (list != null  && !list.isEmpty()) 
		    return true;
		else 
			return false;
	}
	
	@Override
	@Transactional
	public List<TestEnvironmentDevices> listTestEnviornmnetsByName(
			String[] parameters) {
		log.debug("listing parameterized TestEnvironmentDevices instance");
		List<TestEnvironmentDevices> testenviornmentdevices=null;
		try {
			StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("t."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
			testenviornmentdevices=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices t where status=1")
	                .list();
			
			log.debug("list successful");
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return testenviornmentdevices;       
    }

	@Override
	@Transactional
	public List<TestEnvironmentDevices> listTestEnviornmnetsByDescription(int testEnviornmentDeviceId,String[] parameters) {
		log.debug("listing parameterized ProductVersionListMaster instance");
		List<TestEnvironmentDevices> testenvironmentdevices=null;
		try {
			StringBuffer qry=new StringBuffer("select ");
			for(int i=0;i<parameters.length;i++){
				qry.append("te."+parameters[i]+",");				
					
			}
			String query=qry.substring(0, qry.lastIndexOf(","));
		
			testenvironmentdevices=sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices te where testEnvironmentDevicesId=:testEnvironmentDevicesId ")
			.setParameter("testEnvironmentDevicesId",testEnviornmentDeviceId)
	        .list();
			
		
			log.debug("list successful");
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		
			
		}
		return testenvironmentdevices;    
	}

	@Override
	@Transactional
	public TestEnvironmentDevices getByTestEnviornmentId(int testEnvironmentDevicesId) {
		log.debug("getting TestEnvironmentDevices instance by id");
		TestEnvironmentDevices testenvironmentdevices=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from TestEnvironmentDevices ted where testEnvironmentDevicesId=:testEnvironmentDevicesId").setParameter("testEnvironmentDevicesId", testEnvironmentDevicesId)
					.list();
			testenvironmentdevices=(list!=null && list.size()!=0)?(TestEnvironmentDevices)list.get(0):null;
			log.debug("getByTestEnviornmentId successful");
		} catch (RuntimeException re) {
			log.error("getByTestEnviornmentId failed", re);
			
		}
		return testenvironmentdevices;
        
	}
	
	@Override
	@Transactional
	public List<DeviceList> deleteDeviceFromTestEnvironment(Integer testEnvironmentDevicesId,Integer deviceListId) {
		log.debug("Remove device from TestEnvironment");
		TestEnvironmentDevices testEnvironment = null;
		DeviceList device =  null;
		List<DeviceList> deviceList=null;
		try {
			
			testEnvironment = (TestEnvironmentDevices) sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentDevicesId);
			if (testEnvironment == null) {
				log.debug("Test Environment with specified id not found : " + testEnvironmentDevicesId);
				return null;
			}
			device = (DeviceList) sessionFactory.getCurrentSession().get(DeviceList.class, deviceListId);
			if (device == null) {
				log.debug("Device could not found in the database : " + deviceListId);
				
				return null;
			}
			//Get the devices of the environment
			Set<DeviceList> deviceSet = testEnvironment.getDeviceList();
			//Remove the specified device from the test environment devices
			
			deviceSet.remove(device);
			
			
			//Set the modified devices list back to the test environment
			testEnvironment.setDeviceList(deviceSet);
			//Save the test environment with the modified devices
			
			sessionFactory.getCurrentSession().save(testEnvironment);
			
			log.debug("Removed device from test environment successfully");
		} catch (RuntimeException re) {
			log.error("Failed to remove device from test environment", re);
			
		}
		return deviceList;
	}
	@Override
	@Transactional
	public List<DeviceList> addDeviceToTestEnvironment(int testEnvironmentDevicesId, int deviceListId) {
		log.debug("Add device from TestEnvironment");
		TestEnvironmentDevices testEnvironment = null;
		DeviceList device =  null;
		List<DeviceList> deviceList=null;
		try {
			
			testEnvironment = (TestEnvironmentDevices) sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentDevicesId);
			if (testEnvironment == null) {
				log.debug("Test Environment with specified id not found : " + testEnvironmentDevicesId);
				return null;
			}
			
			device = (DeviceList) sessionFactory.getCurrentSession().get(DeviceList.class, deviceListId);
			if (device == null) {
				log.debug("Device could not found in the database : " + deviceListId);
				return null;
			}
			
			//Get the devices of the environment
			Set<DeviceList> devices = testEnvironment.getDeviceList();
			//Remove the specified device from the test environment devices
			devices.add(device);
			//Set the modified devices list back to the test environment
			testEnvironment.setDeviceList(devices);
			//Save the test environment with the modified devices
			
			sessionFactory.getCurrentSession().save(testEnvironment);
			
			log.debug("Added device to test environment successfully");
		} catch (RuntimeException re) {
			log.error("Failed to add device to test environment", re);
			//throw re;
		}
		return deviceList;
	}
	@Override
	@Transactional
	public int getTotalRecords(Integer testEnvironmentDevicesId) {
		log.debug("getting TestEnvironmentDevicesHasDeviceList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from  test_environment_devices_has_device_list where testEnvironmentDevicesId=:testEnvironmentDevicesId").setParameter("testEnvironmentDevicesId", testEnvironmentDevicesId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}
	@Override
	@Transactional
	public List<TestEnvironmentDevices> listTestRunConfigurationChildhasTestEnviornments(
			int testRunConfigurationChildId) {
		// TODO Auto-generated method stub
		
		return null;
	}

}