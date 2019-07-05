package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.JsonHostPlatformMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.WorkPackageService;


@Controller
public class HostManagementController{
private static final Log log = LogFactory.getLog(HostManagementController.class);
	
	@Autowired
	private HostListService hostListService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkPackageService workPackageService;

	@RequestMapping(value="administration.host.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listHosts(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer workpackageId,@RequestParam Integer ecId,@RequestParam Integer testRunPlanId,@RequestParam Integer filter ) {
		log.debug("inside administration.host.list");
		JTableResponse jTableResponse;
		 
		try {
			List<HostList> hostLists=hostListService.list(jtStartIndex,jtPageSize);
			List<JsonHostList> jsonHostLists=new ArrayList<JsonHostList>();
			Set<RunConfiguration> runConfigurations =null;
			if(filter==-1){
				
				 TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				 runConfigurations=productListService.getRunConfigurationList(testRunPlanId,2);
				 
				List<HostList> hostListAlreadyToTestPlan =new ArrayList<HostList>();
				if(runConfigurations != null && runConfigurations.size()>0) {
					 for(RunConfiguration rc:runConfigurations){
						 if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(ecId))
							 hostListAlreadyToTestPlan.add(rc.getHostList());
					 }
				}
				 if(hostLists != null && hostLists.size() >0) {
					for(HostList hl: hostLists){
						if(!hostListAlreadyToTestPlan.contains(hl)){
							
							JsonHostList jsonhHostList=	new JsonHostList(hl);
							jsonHostLists.add(jsonhHostList);	
						}
										
					}
				 }
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
				
				jsonHostLists = null;
		        return jTableResponse;
			}else if(filter==1){//1 for list from workpackage
				WorkPackage workPackage=null;
				if(workpackageId!=-1){
					workPackage= workPackageService.getWorkPackageById(workpackageId);
				}
				runConfigurations=productListService.getRunConfigurationListOfWPrcStatus(workpackageId, 1);
				
				List<HostList> hostListAlreadyAddedList =new ArrayList<HostList>();
				
				for(RunConfiguration rc:runConfigurations){
					if(rc.getHostList()!=null){
						if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(ecId))
							hostListAlreadyAddedList.add(rc.getHostList());
					}
				}
				
				if(hostListAlreadyAddedList==null || hostListAlreadyAddedList.isEmpty() || hostListAlreadyAddedList.size()==0){
					for(HostList hl:hostLists){
						JsonHostList jsonHostList=	new JsonHostList(hl);
						jsonHostLists.add(jsonHostList);	
					}
				}else{
					for(HostList hl:hostLists){
						if(!hostListAlreadyAddedList.contains(hl)){
							JsonHostList jsonHostList=	new JsonHostList(hl);
							jsonHostLists.add(jsonHostList);					
						}
					}
				}
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
				jsonHostLists = null;
		        return jTableResponse;
			
			}else if(filter==-2){
				for(HostList hl:hostLists){
						JsonHostList jsonHostList=	new JsonHostList(hl);
						jsonHostLists.add(jsonHostList);					
					}
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
				jsonHostLists = null;
		        return jTableResponse;
			}else{
				
				for(HostList hl: hostLists){
						jsonHostLists.add(new JsonHostList(hl));
				}
			}
		 	jTableResponse = new JTableResponse("OK", jsonHostLists,hostListService.getTotalRecords());
	   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
	        
        return jTableResponse;
    }
	

	@RequestMapping(value="administration.host.list.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listHostsMappedWithProduct(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer workpackageId,@RequestParam Integer ecId,@RequestParam Integer testRunPlanId,@RequestParam Integer filter ) {
		log.debug("inside administration.host.list");
		JTableResponse jTableResponse;
		 
		try {
			List<HostList> hostListAlreadyMapped = new ArrayList<HostList>();
			WorkPackage workPackage=null;
			ProductMaster productMaster=null;
			TestRunPlan testRunPlan=null;
			
			if(workpackageId!=-1){
				workPackage= workPackageService.getWorkPackageById(workpackageId);
				productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
			}else if(testRunPlanId!=-1){
				 testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				productMaster=testRunPlan.getProductVersionListMaster().getProductMaster();
			}
			Set<HostList> hostLists=productMaster.getHostLists();
			List<JsonHostList> jsonHostLists=new ArrayList<JsonHostList>();
			if(filter==-1){
		       List<JsonHostList> unMappedhostListsOfTestRunPlan = hostListService.getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(productMaster.getProductId(), ecId, 1, testRunPlanId); 
		       jTableResponse = new JTableResponse("OK", unMappedhostListsOfTestRunPlan,unMappedhostListsOfTestRunPlan.size());
		       log.info("Unmapped Host List obtained from SQL query");
		       unMappedhostListsOfTestRunPlan = null;
			}else if(filter==1){//1 for list from workpackage
				
				List<JsonHostList> unMappedhostListsOfWorkPackageLevel = hostListService.getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(productMaster.getProductId(), ecId, 1, workpackageId);
				jTableResponse = new JTableResponse("OK", unMappedhostListsOfWorkPackageLevel,unMappedhostListsOfWorkPackageLevel.size());
				log.info("Unmapped Host List obtained from SQL query");
				unMappedhostListsOfWorkPackageLevel = null;
		        return jTableResponse;
			
			}else if(filter==-2){
				for(HostList hl:hostLists){
						JsonHostList jsonHostList=	new JsonHostList(hl);
						jsonHostLists.add(jsonHostList);					
					}
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
				jsonHostLists = null;
		        return jTableResponse;
			}else{
				
				for(HostList hl: hostLists){
						jsonHostLists.add(new JsonHostList(hl));
				}
				jTableResponse = new JTableResponse("OK", jsonHostLists,hostListService.getTotalRecords());
			}
		 	
	   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
	        
        return jTableResponse;
    }
	

	
	@RequestMapping(value="administration.host.list.platform",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listHostPlatforms() {
		log.debug("inside administration.host.platform");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<HostPlatformMaster> hostPlatformMaster=hostListService.platformsList();
			List<JsonHostPlatformMaster> jsonHostPlatformMaster=new ArrayList<JsonHostPlatformMaster>();
			for(HostPlatformMaster hpm: hostPlatformMaster){
				jsonHostPlatformMaster.add(new JsonHostPlatformMaster(hpm));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonHostPlatformMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="administration.host.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addHost(@ModelAttribute JsonHostList jsonHostList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
				HostList hostList = jsonHostList.getHostList();
				if (!(isHostExists(hostList.getHostName().trim()))){
					hostListService.add(hostList);
		            eventsService.raiseNewTerminalRegisteredEvent(hostList, "New Terminal Added");
					jsonHostList.setHostId(hostList.getHostId());
		            jTableSingleResponse = new JTableSingleResponse("OK",jsonHostList);
				}
				else
				{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","Hostname Already Exists!");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	@RequestMapping(value="administration.host.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteHost(@RequestParam int hostId) {
		JTableResponse jTableResponse;
		try {
				eventsService.raiseTerminalDeregisteredEvent(hostListService.getHostById(hostId), "Terminal Removed");
	            hostListService.delete(hostId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="administration.host.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateHost(@ModelAttribute JsonHostList jsonHostList, BindingResult result) {
		JTableResponse jTableResponse;
		HostList hostListFromUI = null;
		HostList hostListFromDB = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {	//convert JsonHostList to HostList for persist operation
				hostListFromUI = jsonHostList.getHostList();
				hostListFromDB = hostListService.getHostById(hostListFromUI.getHostId());
				if(hostListFromUI.getHostName().trim().equals(hostListFromDB.getHostName())){
					List<HostList> hostList = updateHostList(hostListFromUI);
					jTableResponse = new JTableResponse("OK",hostList,1);
				}
				else {
					if (!isHostExists(hostListFromUI.getHostName().trim())){
						List<HostList> hostList = updateHostList(hostListFromUI);
						jTableResponse = new JTableResponse("OK",hostList,1);
					}
					else
					{
						jTableResponse = new JTableResponse("INFORMATION","Host Name Already Exists!");
					}
				}
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }

	

	/*
	 * 'Host names should be unique in Edit Screen.'
	 */
	public List<HostList> updateHostList(HostList hostListFromUI){
		hostListService.update(hostListFromUI);
		List<HostList> hostList =new ArrayList<HostList>();
		hostList.add(hostListFromUI);
		return hostList;
	}
	
	/*
	 * 'Host should be unique.'
	 */
	public boolean isHostExists(String hostName){
		boolean bResult=false;	
		try{
			log.info("inside the isHostExists:");
			bResult= hostListService.isHostExistingByName(hostName);
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