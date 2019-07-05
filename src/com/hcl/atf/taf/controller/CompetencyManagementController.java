package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.dto.CompetencySummaryDTO;
import com.hcl.atf.taf.model.json.JsonCompetencyMember;
import com.hcl.atf.taf.model.json.JsonCompetencySummary;
import com.hcl.atf.taf.model.json.JsonProductTeamResources;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CompetencyService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;

@Controller
public class CompetencyManagementController {

	private static final Log log = LogFactory.getLog(CompetencyManagementController.class);
	
	@Autowired
	private CompetencyService competencyService;
	
	@Autowired
	private DimensionService dimensionService;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private ProductListService productListService;
	
	@RequestMapping(value="competency.summary",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getProductSummary(@RequestParam Integer competencyId){	
		log.debug("competency.summary");
		JTableResponse jTableResponse = null;
			try {
				CompetencySummaryDTO competencySummaryDTO = competencyService.getCompetencySummary(competencyId);
				List<JsonCompetencySummary> jsonCompetencySummaryList = new ArrayList<JsonCompetencySummary>();
				if (competencySummaryDTO == null ) {
					jTableResponse = new JTableResponse("OK", jsonCompetencySummaryList, 0);
				} else {
					jsonCompetencySummaryList.add(new JsonCompetencySummary(competencySummaryDTO));
					jTableResponse = new JTableResponse("OK", jsonCompetencySummaryList, jsonCompetencySummaryList.size());
					competencySummaryDTO = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Competency Summary!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="competency.user.by.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCompetenciesMembersList(@RequestParam Integer competencyId, @RequestParam Integer status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside competency.user.by.status");
		JTableResponse jTableResponse;
		 
		try {
			List<CompetencyMember> competencyMembers = competencyService.getCompetencyMembers(competencyId, status, jtStartIndex, jtPageSize);
			List<JsonCompetencyMember> jsonCompetencyMembers=new ArrayList<JsonCompetencyMember>();
			int totalRecordsAvailable = competencyService.getTotalRecordsForCompetencyMemberPagination(competencyId, status, CompetencyMember.class);
			
			for(CompetencyMember competencyMember: competencyMembers){
				jsonCompetencyMembers.add(new JsonCompetencyMember(competencyMember));
				
			}
	        jTableResponse = new JTableResponse("OK", jsonCompetencyMembers, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="competency.user.list.to.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMebersToAddWithCompetency() {  
		log.debug("  ------------competency.user.list.to.add-----------");
		JTableResponseOptions jTableResponseOptions = null;
		
		try {
			List<UserList> userLists = competencyService.getMembersToAddWithCompetency();
			List<JsonUserList> jsonUserLists = new ArrayList<JsonUserList>();
			
			for(UserList userList : userLists){
				jsonUserLists.add(new JsonUserList(userList));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserLists,true);     
	        } catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error adding new member for competency!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableResponseOptions;		
    }
	
	@RequestMapping(value="competency.user.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addMemberToCompetency(@ModelAttribute JsonCompetencyMember jsonCompetencyMember, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------competency.user.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			CompetencyMember competencyMember = jsonCompetencyMember.getCompetencyMember();
			String errorMessage = ValidationUtility.validateForCompetencyMemberAdditionOrUpdation(jsonCompetencyMember.getUserId(), competencyService, 1, jsonCompetencyMember.getCompetencyMemberId(), competencyMember.getStartDate(), competencyMember.getEndDate());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			if(competencyMember.getStartDate().after(competencyMember.getEndDate())){
				errorMessage = "End date should greaterthan start date";
			}
			
			if (errorMessage != null && !errorMessage.isEmpty()) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			competencyMember.setStatus(1);
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			competencyMember.setMappedBy(userList);
			competencyMember.setModifiedBy(userList);
						
			competencyService.addCompetencyMember(competencyMember);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonCompetencyMember(competencyMember));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new member for competency!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="competency.user.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateMemberWithCompetency(@ModelAttribute JsonCompetencyMember jsonCompetencyMember, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------competency.user.update-----------");
		JTableSingleResponse jTableSingleResponse;
		
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			
			CompetencyMember competencyMember = jsonCompetencyMember.getCompetencyMember();
			String errorMessage = ValidationUtility.validateForCompetencyMemberAdditionOrUpdation(jsonCompetencyMember.getUserId(), competencyService, 1, jsonCompetencyMember.getCompetencyMemberId(), competencyMember.getStartDate(), competencyMember.getEndDate());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			if(competencyMember.getStartDate().after(competencyMember.getEndDate())){
				errorMessage = "End date should greaterthan start date";
			}
			
			if (errorMessage != null && !errorMessage.isEmpty()) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			competencyMember.setModifiedBy(userList);
			competencyMember.setModifiedDate(new Date());
						
			competencyService.updateCompetencyMember(competencyMember);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonCompetencyMember(competencyMember));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error update member for competency!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="competency.product.team.resouces.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductTeamResources(@RequestParam Integer productId, @RequestParam Integer dimensionId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {			
		JTableResponse jTableResponse;			 
		try {
			List<ProductTeamResources> productTeamResList = competencyService.getCompetencyProductTeamResourcesList(productId, dimensionId, jtStartIndex,jtPageSize);
			Integer totalRecordsAvailable = competencyService.getTotalRecordsForComptencyProductTeam(productId, dimensionId);
			
			List<JsonProductTeamResources> jsonProductTeamResList=new ArrayList<JsonProductTeamResources>();
			for(ProductTeamResources productCoreRes: productTeamResList){
				jsonProductTeamResList.add(new JsonProductTeamResources(productCoreRes));	
			}				
			jTableResponse = new JTableResponse("OK", jsonProductTeamResList, totalRecordsAvailable);     
			productTeamResList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="competency.user.for.product.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMebersToAddWithProduct(@RequestParam Integer competencyId) {  
		log.debug("  ------------competency.user.for.product.options-----------");
		JTableResponseOptions jTableResponseOptions = null;
		
		try {
			List<UserList> userLists = competencyService.getMembersToMapWithCompetencyProduct(competencyId);
			List<JsonUserList> jsonUserLists = new ArrayList<JsonUserList>();
			
			for(UserList userList : userLists){
				jsonUserLists.add(new JsonUserList(userList));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserLists,true);     
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching user for product Competency!");
        	log.error("JSON ERROR", e);
	    }	        
        return jTableResponseOptions;		
    }
	
	@RequestMapping(value="competency.product.team.user.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addProductTeamUser(@ModelAttribute JsonProductTeamResources jsonProductTeamResource, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {   
			DimensionProduct dimensionProduct = dimensionService.getDimensionProductMappedByProductId(jsonProductTeamResource.getProductId(), jsonProductTeamResource.getDimensionId(), null);
			List<CompetencyMember> competencyMembers = competencyService.getCompetencyMemberMappedByUserId(jsonProductTeamResource.getUserId(), 2, null);
			ProductTeamResources pTeamResource = jsonProductTeamResource.getProductTeamResources();
			boolean isUserAvailableForCompetency = true;
			if(competencyMembers != null){
				for(CompetencyMember competencyMember : competencyMembers){
					if(competencyMember.getStartDate().after(pTeamResource.getFromDate()) || competencyMember.getEndDate().before(pTeamResource.getToDate())){
						isUserAvailableForCompetency = false;
					}else{
						isUserAvailableForCompetency = true;
						break;
					}
				}
			}
			if(!isUserAvailableForCompetency){
				String msg="User may not available for the competency in the specified duration.";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
			if(dimensionProduct.getStartDate().after(pTeamResource.getFromDate()) || dimensionProduct.getEndDate().before(pTeamResource.getToDate())){
				String msg="Product may not associated with competency in the specified duration.";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
			if(pTeamResource.getFromDate().after(pTeamResource.getToDate())){
				String msg="From date should be less than or equal to To date";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
			
			UserList user=userListService.getUserListById(jsonProductTeamResource.getUserId());
			UserRoleMaster productSpecificRole = userListService.getRoleById(jsonProductTeamResource.getProductSpecificUserRoleId());
			if(productSpecificRole != null){
				pTeamResource.setProductSpecificUserRole(productSpecificRole);
			}
			Boolean errorMessage=	productListService.isUserAlreadyProductTeamResource(jsonProductTeamResource.getProductId(),jsonProductTeamResource.getUserId(),jsonProductTeamResource.getFromDate(),jsonProductTeamResource.getToDate(),null);
			if (errorMessage) {
				String msg="User "+ user.getLoginId() +" is already a Product Team resource for this or other product in specified period";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
			productListService.addProductTeamResource(pTeamResource);
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductTeamResources(pTeamResource));		
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="competency.product.team.user.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateProductTeamResourceMapping(@ModelAttribute JsonProductTeamResources jsonProductTeamResource, BindingResult result) {
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
		
			DimensionProduct competencyProduct = dimensionService.getDimensionProductMappedByProductId(jsonProductTeamResource.getProductId(), jsonProductTeamResource.getDimensionId(), null);
			List<CompetencyMember> competencyMembers = competencyService.getCompetencyMemberMappedByUserId(jsonProductTeamResource.getUserId(), 2, null);
			ProductTeamResources pTeamResource = jsonProductTeamResource.getProductTeamResources();
			boolean isUserAvailableForCompetency = true;
			if(competencyMembers != null){
				for(CompetencyMember competencyMember : competencyMembers){
					if(competencyMember.getStartDate().after(pTeamResource.getFromDate()) || competencyMember.getEndDate().before(pTeamResource.getToDate())){
						isUserAvailableForCompetency = false;
					}else{
						isUserAvailableForCompetency = true;
						break;
					}
				}
			}
			if(!isUserAvailableForCompetency){
				String msg = "User may not available for the competency in the specified duration.";
				jTableResponse = new JTableResponse("ERROR",msg);
				return jTableResponse;
			}
			if(competencyProduct.getStartDate().after(pTeamResource.getFromDate()) || competencyProduct.getEndDate().before(pTeamResource.getToDate())){
				String msg="Product may not associated with competency in the specified duration.";
				jTableResponse = new JTableResponse("ERROR",msg);
				return jTableResponse;
			}
			Date updatetodate=DateUtility.dateformatWithOutTime(jsonProductTeamResource.getToDate());
			Date updatefromdate=DateUtility.dateformatWithOutTime(jsonProductTeamResource.getFromDate());
			if(updatefromdate.compareTo(updatetodate)>0){
				jTableResponse = new JTableResponse("ERROR","From date should be less than or equal to To date");
				return jTableResponse;
			}
			ProductTeamResources productTeamResourcesFromUI = jsonProductTeamResource.getProductTeamResources();
			ProductTeamResources productTeamResourcesFromDB = productListService.getProductTeamResourceById(jsonProductTeamResource.getProductTeamResourceId());	
			log.info(" Cretaed date FromDB :"+productTeamResourcesFromDB.getCreatedDate());
			if(jsonProductTeamResource.getStatus() == 0){
				ProductTeamResources prdTeamResource = productListService.getProductTeamResourceById(jsonProductTeamResource.getProductTeamResourceId());
				 if(prdTeamResource != null){
					 productListService.removeProductTeamResourceMapping(prdTeamResource);
					 log.info("Product Team User association with product is removed.");
					 jTableResponse = new JTableResponse("OK");  
				 }
			}else{
				if((productTeamResourcesFromUI.getCreatedDate()!=null) ){					
					productTeamResourcesFromUI.setCreatedDate(productTeamResourcesFromDB.getCreatedDate());
				}
				UserList user=userListService.getUserListById(jsonProductTeamResource.getUserId());
				UserRoleMaster userRoleMaster = userListService.getRoleById(jsonProductTeamResource.getProductSpecificUserRoleId());
				productTeamResourcesFromUI.setProductSpecificUserRole(userRoleMaster);
				Boolean errorMessage = productListService.isUserAlreadyProductTeamResource(jsonProductTeamResource.getProductId(),jsonProductTeamResource.getUserId(),jsonProductTeamResource.getFromDate(),jsonProductTeamResource.getToDate(),productTeamResourcesFromDB);
				if (errorMessage) {
					String msg="User "+ user.getLoginId() +" is already a Product Team resource for this or other product in specified period";
					jTableResponse = new JTableResponse("ERROR",msg);
					return jTableResponse;
				}else{
					productListService.updateProductTeamResource(productTeamResourcesFromUI);	
					 jTableResponse = new JTableResponse("OK");  
				}
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error updating Product Team Resource!");
            log.error("JSON ERROR", e);
            e.printStackTrace();
        }
        return jTableResponse;
    }
}
