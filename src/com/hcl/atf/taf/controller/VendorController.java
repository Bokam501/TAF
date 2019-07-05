package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.json.JsonVendorMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.VendorListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class VendorController {
	
	@Autowired
	private VendorListService vendorListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private EventsService eventsService;

	private static final Log log = LogFactory.getLog(VendorController.class);
	
	
	@RequestMapping(value="administration.vendor.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listVendor(@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {			
		JTableResponse jTableResponse;			 
		try { 
			List<VendorMaster> vendorList=vendorListService.getVendorMasterList(jtStartIndex, jtPageSize);
			List<VendorMaster> vendorListForpagination=vendorListService.getVendorMasterList(null, null);
			List<JsonVendorMaster> jsonVendorList=new ArrayList<JsonVendorMaster>();
			for(VendorMaster vendorMaster: vendorList){
				jsonVendorList.add(new JsonVendorMaster(vendorMaster));	
				}				
			jTableResponse = new JTableResponse("OK", jsonVendorList,vendorListForpagination.size());     
			vendorList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Vendors!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.vendor.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addVendor(HttpServletRequest req, @ModelAttribute JsonVendorMaster jsonVendor, BindingResult result) {  
		JTableSingleResponse jTableSingleResponse;
		
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			VendorMaster vendor = jsonVendor.getVendorMaster();
			
			List<VendorMaster> vendorList=vendorListService.getVendorMasterList();
			List<JsonVendorMaster> jsonVendorList=new ArrayList<JsonVendorMaster>();
			
			String errorMessage = ValidationUtility.validateForNewVendorAddition(vendor, vendorListService, "add");			
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			vendorListService.addVendorMaster(vendor);
			UserList user=(UserList)req.getSession().getAttribute("USER");
			eventsService.addNewEntityEvent(IDPAConstants.ENTITY_VENDOR, vendor.getVendorId(), vendor.getRegisteredCompanyName(), user);
            jTableSingleResponse = new JTableSingleResponse("OK",new JsonVendorMaster(vendor));				
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Vendor!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableSingleResponse;			
    }
	

	@RequestMapping(value="administration.vendor.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateVendor(HttpServletRequest req, @ModelAttribute JsonVendorMaster jsonVendor, BindingResult result) {
		JTableResponse jTableResponse;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {
		    VendorMaster vendorFromUI = jsonVendor.getVendorMaster();				
		    VendorMaster vendorFromDB = vendorListService.getVendorById(jsonVendor.getVendorId());						
			vendorFromUI.setCreatedDate(vendorFromDB.getCreatedDate());
							
				String errorMessage = ValidationUtility.validateForNewVendorAddition(vendorFromUI, vendorListService, "update");			
				if (errorMessage != null) {					
					jTableResponse = new JTableResponse("ERROR",errorMessage);
					return jTableResponse;
				}
				vendorListService.updateVendorMaster(vendorFromUI);	
				UserList user=(UserList)req.getSession().getAttribute("USER");				
				
				remarks = "Vendor :"+vendorFromUI.getRegisteredCompanyName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_VENDOR, vendorFromUI.getVendorId(), vendorFromUI.getRegisteredCompanyName(),
						jsonVendor.getModifiedField(), jsonVendor.getModifiedFieldTitle(),
						jsonVendor.getOldFieldValue(), jsonVendor.getModifiedFieldValue(), user, remarks);
				
				 jTableResponse = new JTableResponse("OK");  
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Vendor!");
	            log.error("JSON ERROR  updating Vendor", e);
	        }
        return jTableResponse;
    }
}
