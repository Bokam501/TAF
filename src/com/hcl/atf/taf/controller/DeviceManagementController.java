package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.json.JsonDeviceList;
import com.hcl.atf.taf.model.json.JsonDeviceMakeMaster;
import com.hcl.atf.taf.model.json.JsonDeviceModelMaster;
import com.hcl.atf.taf.model.json.JsonDevicePlatformMaster;
import com.hcl.atf.taf.model.json.JsonDevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.HostListService;
@Controller
public class DeviceManagementController {

	private static final Log log = LogFactory.getLog(DeviceManagementController.class);
	
	@Autowired
	private DeviceListService deviceListService;
	
	@Autowired
	private HostListService hostListService;
	
	
	
	@RequestMapping(value="administration.device.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDevices(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside administration.device.list");
		JTableResponse jTableResponse;
			try {
			
			List<DeviceList> deviceList=deviceListService.list(jtStartIndex,jtPageSize);
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(DeviceList dl: deviceList){
				jsonDeviceList.add(new JsonDeviceList(dl));
			}
	        jTableResponse = new JTableResponse("OK", jsonDeviceList,deviceListService.totalRecordsCount());
	        deviceList = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.device.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDevice(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
				
				DeviceList deviceList=jsonDeviceList.getDeviceList();
				if(!(isDeviceExists(deviceList.getDeviceId().trim()))){
					deviceListService.add(deviceList);				
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonDeviceList(deviceList));
				}
				else{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","Device alredy Exists");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	@RequestMapping(value="administration.device.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteDevice(@RequestParam int deviceListId) {
		JTableResponse jTableResponse;
		try {
	            deviceListService.delete(deviceListId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="administration.device.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDevice(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		jTableResponse = new JTableResponse("ERROR","Updating Device details is temporarily disabled"); 
        return jTableResponse;
    }
	

	@RequestMapping(value="administration.device.list.platform",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDevicePlatforms() {
		log.debug("inside administration.device.list.platform");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DevicePlatformMaster> devicePlatformMaster=deviceListService.platformsList();
			List<JsonDevicePlatformMaster> jsonDevicePlatformMaster=new ArrayList<JsonDevicePlatformMaster>();
			for(DevicePlatformMaster dvm: devicePlatformMaster){
				jsonDevicePlatformMaster.add(new JsonDevicePlatformMaster(dvm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDevicePlatformMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="administration.device.list.platformversion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDevicePlatformVersions(@RequestParam String devicePlatform) {
		log.debug("inside administration.device.list.platformversion");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DevicePlatformVersionListMaster> devicePlatformVersionListMaster=deviceListService.platformVersionsList(devicePlatform);
			List<JsonDevicePlatformVersionListMaster> jsonDevicePlatformVersionListMaster=new ArrayList<JsonDevicePlatformVersionListMaster>();
			for(DevicePlatformVersionListMaster dvm: devicePlatformVersionListMaster){
				jsonDevicePlatformVersionListMaster.add(new JsonDevicePlatformVersionListMaster(dvm));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDevicePlatformVersionListMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.device.list.make",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceMake() {
		log.debug("inside administration.device.list.make");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DeviceMakeMaster> deviceMakeMaster=deviceListService.makeList();
			List<JsonDeviceMakeMaster> jsonDeviceMakeMaster=new ArrayList<JsonDeviceMakeMaster>();			 
			for(DeviceMakeMaster dmm: deviceMakeMaster){
				jsonDeviceMakeMaster.add(new JsonDeviceMakeMaster(dmm));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceMakeMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.device.list.host",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listHosts() {
		log.debug("inside administration.device.list.host");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<HostList> hostList=hostListService.list();
			List<JsonHostList> jsonHostList=new ArrayList<JsonHostList>();			 
			for(HostList hl: hostList){
				jsonHostList.add(new JsonHostList(hl));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonHostList);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.device.list.model",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceModel(@RequestParam String deviceMake) {
		log.debug("inside administration.device.list.model");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DeviceModelMaster> deviceModelMaster=deviceListService.modelList(deviceMake);
			List<JsonDeviceModelMaster> jsonDeviceModelMaster=new ArrayList<JsonDeviceModelMaster>();			 
			for(DeviceModelMaster dmm: deviceModelMaster){
				jsonDeviceModelMaster.add(new JsonDeviceModelMaster(dmm));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceModelMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	/*
	 * 'Device should be unique.'
	 */
	public boolean isDeviceExists(String deviceId){
		boolean bResult=false;	
		try{
			bResult= deviceListService.isDeviceExistById(deviceId);
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}
}
