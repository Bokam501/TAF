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

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.json.JsonDeviceList;
import com.hcl.atf.taf.model.json.JsonTestEnvironmentDevices;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.TestEnvironmentDeviceService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
@Controller
public class TestEnvironmentController {

	private static final Log log = LogFactory.getLog(TestEnvironmentController.class);
	
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestEnvironmentDeviceService testEnvironmentDeviceService;
	
	@Autowired
	private TestExecutionService testExecutionService;
	
	
	//Parent table CRUD operation
	
	@RequestMapping(value="test.env.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestEnviornments(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside test.env.list");
		JTableResponse jTableResponse;
			try {
			List<TestEnvironmentDevices> testEnvironmentDevices=testEnvironmentDeviceService.listAllTestEnvironments(jtStartIndex, jtPageSize);
			List<JsonTestEnvironmentDevices> jsonTestEnvironmentDevices=new ArrayList<JsonTestEnvironmentDevices>();
			for(TestEnvironmentDevices ted:testEnvironmentDevices){
				jsonTestEnvironmentDevices.add(new JsonTestEnvironmentDevices(ted));	
			}
	        jTableResponse = new JTableResponse("OK", jsonTestEnvironmentDevices,testEnvironmentDeviceService.getTotalTestEnviornmentRecords());  
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR listing TestEnviornments : ", e);
	        }
	    return jTableResponse;
    }
	@RequestMapping(value="test.env.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestEnvironment(@ModelAttribute JsonTestEnvironmentDevices jsonTestEnvironmentDevices, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		log.debug("inside the test.env.add:");
		TestEnvironmentDevices testEnvironmentDeviceFromUI = null;
		TestEnvironmentDevices testEnvironmentDeviceFromDB = null;
		String Status="";
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
				testEnvironmentDeviceFromUI = jsonTestEnvironmentDevices.getTestEnvironmentDevices();
				testEnvironmentDeviceFromDB = testEnvironmentDeviceService.getTestEnvironmentByName(testEnvironmentDeviceFromUI.getName().trim());
				if(testEnvironmentDeviceFromDB==null){
					log.info("No TestEnvironment Name found with the given Name:");
				}
				else{
					int status= testEnvironmentDeviceFromDB.getStatus();
					if(status==0)
						Status = "InActive";
					else
						Status = "Active";
				}
				if (!(isTestEnviornmentExists(testEnvironmentDeviceFromUI))){
					testEnvironmentDeviceService.addTestEnviroments(testEnvironmentDeviceFromUI);				
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestEnvironmentDevices(testEnvironmentDeviceFromUI));
				}
				else
				{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION",Status+"-Test Enviornment Name Already Exists.Please use another unique name.");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	            
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.env.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestEnvironment(@ModelAttribute JsonTestEnvironmentDevices jsontestEnvironmentDevices, BindingResult result) {
		JTableResponse jTableResponse = null;
		TestEnvironmentDevices testEnvironmentDeviceFromUI = null;
		TestEnvironmentDevices testEnvironmentDeviceFromDB = null;
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}
		try {	
			testEnvironmentDeviceFromUI= jsontestEnvironmentDevices.getTestEnvironmentDevices();
			testEnvironmentDeviceFromDB = testEnvironmentDeviceService.getByTestEnvironmentId(testEnvironmentDeviceFromUI.getTestEnvironmentDevicesId());
				if(testEnvironmentDeviceFromUI.getName().trim().equals(testEnvironmentDeviceFromDB.getName())){
					List<TestEnvironmentDevices> testEnviromentLists = updateTestEnvironment(testEnvironmentDeviceFromUI);
					jTableResponse = new JTableResponse("OK",testEnviromentLists,1);
				}
				else {
					if (!(isTestEnviornmentExists(testEnvironmentDeviceFromUI))){
						List<TestEnvironmentDevices> testEnviromentLists = updateTestEnvironment(testEnvironmentDeviceFromUI);
						jTableResponse = new JTableResponse("OK",testEnviromentLists,1);
					}
					else
					{
						jTableResponse = new JTableResponse("INFORMATION","Test Enviornment Name Already Exists!");
					}
				}
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR updating TestEnvironment : ", e);
	        }
        return jTableResponse;
    }
	@RequestMapping(value="test.env.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestEnvironment(@RequestParam int testEnvironmentDevicesId) {
		JTableResponse jTableResponse;
		try {
				testEnvironmentDeviceService.delete(testEnvironmentDevicesId);
				jTableResponse = new JTableResponse("OK");
				
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	/*
	 * 'Test Enviornment names should be unique in Edit Screen.'
	 */
	public List<TestEnvironmentDevices> updateTestEnvironment(TestEnvironmentDevices testEnvironmentDeviceFromUI){
		JTableResponse jTableResponse = null;
		if (testEnvironmentDeviceFromUI.getStatus() == null)
			testEnvironmentDeviceFromUI.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
		testEnvironmentDeviceService.updateTestEnvironments(testEnvironmentDeviceFromUI);
		List<TestEnvironmentDevices> testEnviromentLists =new ArrayList<TestEnvironmentDevices>();
		testEnviromentLists.add(testEnvironmentDeviceFromUI);
		return testEnviromentLists;
	}
	
	/*
	 * 'Test Enviornment names should be unique.'
	 */
	public boolean isTestEnviornmentExists(TestEnvironmentDevices testEnvironmentDevice){
		boolean bResult=false;	
		try{
			bResult= testEnvironmentDeviceService.isTestEnviromentdevicesExistingByName(testEnvironmentDevice);
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	
	//Child table CRUD operation
	
	@RequestMapping(value="test.env.dev.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestEnvironmentDevices(@RequestParam Integer testEnvironmentDevicesId) {
		log.debug("inside test.env.dev.list");
		JTableResponse jTableResponse;
		int totalRecords = 0;
			try {
				List<DeviceList> listdevice=testEnvironmentDeviceService.listTestEnvironmentDevices(testEnvironmentDevicesId);
				if (listdevice != null) {
					List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
					for(DeviceList testEnviornmentDeviceList: listdevice){
						jsonDeviceList.add(new JsonDeviceList(testEnviornmentDeviceList));
					}
					
					totalRecords = testEnvironmentDeviceService.getTotalRecordsDevices(testEnvironmentDevicesId);
		            jTableResponse = new JTableResponse("OK", jsonDeviceList,totalRecords);
		            listdevice = null;   
				} else {
		            jTableResponse = new JTableResponse("OK","No Devices in environment");
				}
	 
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR listing TestEnvironmentDevices : ", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.env.device.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDeviceToTestEnvironment(@ModelAttribute JsonTestEnvironmentDevices jsonTestEnviournmentDevices, JsonDeviceList
    		jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		HostList hostList = null;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {	DeviceList deviceList = jsonDeviceList.getDeviceList();
				int deviceListId=Integer.parseInt(jsonDeviceList.getDeviceId());
				if(isdevicesExists(jsonTestEnviournmentDevices,deviceListId)==false)
				{
					
					testEnvironmentDeviceService.addDeviceToTestEnvironment(jsonTestEnviournmentDevices.getTestEnvironmentDevicesId(), deviceListId);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonDeviceList(deviceList));
				}
				else
				{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","Device Already Exists!");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR adding Device To TestEnvironment", e);	 
	        }
	        
        return jTableSingleResponse;
    }
	@RequestMapping(value="test.env.device.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteDeviceFromTestEnviornment(@RequestParam Integer testEnvironmentDevicesId,@RequestParam String deviceListId) {
		JTableResponse jTableResponse;
		try {
				testEnvironmentDeviceService.deleteDevice(testEnvironmentDevicesId,new Integer(deviceListId));
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	/*
	 * Changes for 'Device should be unique.'
	 */
	public boolean isdevicesExists(JsonTestEnvironmentDevices jsonTestEnviournmentDevices, int deviceListId){
		boolean bResult=false;
		List<DeviceList> deviceList=null;
		String devListId=Integer.toString(deviceListId);
		try{
			deviceList=(ArrayList) testEnvironmentDeviceService.listTestEnvironmentDevices(jsonTestEnviournmentDevices.getTestEnvironmentDevicesId());
			for(DeviceList DL: deviceList){
				if (devListId.equals(DL.getDeviceListId().toString())){
					bResult=true;
				}
			}
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
