package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonEnvironmentCategory;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonEnvironmentGroup;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.json.JsonCommonOption;

@Controller
public class EnvironmentController {

	private static final Log log = LogFactory.getLog(EnvironmentController.class);
	
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	
	
	@RequestMapping(value="environment.group.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getEnvironmentGroupList(HttpServletRequest req) {
		log.debug("inside environment.group.list");
		UserList user=(UserList)req.getSession().getAttribute("USER");
		
		JTableResponse jTableResponse = null;
			try {
				List<EnvironmentGroup> environmentGroupList=environmentService.getEnvironmentGroupList();
				
				List<JsonEnvironmentGroup> jsonEnvironmentGroup=new ArrayList<JsonEnvironmentGroup>();
				
				if (environmentGroupList == null || environmentGroupList.isEmpty()) {
					
					jTableResponse = new JTableResponse("OK", jsonEnvironmentGroup, 0);
				} else {
					for(EnvironmentGroup environmentGroup: environmentGroupList){
						jsonEnvironmentGroup.add(new JsonEnvironmentGroup(environmentGroup));
					}
					jTableResponse = new JTableResponse("OK", jsonEnvironmentGroup,jsonEnvironmentGroup.size() );
					environmentGroupList = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="environment.group.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addEnvironmentGroup(HttpServletRequest req, @ModelAttribute JsonEnvironmentGroup jsonEnvironmentGroup, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			EnvironmentGroup environmentGroup  =jsonEnvironmentGroup.getEnvironmentGroup();
			String errorMessage=commonService.duplicateNameWithOutFilter(environmentGroup.getEnvironmentGroupName(), "EnvironmentGroup", "environmentGroupName", "EnvironmentGroup");
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			environmentService.addEnvironmentGroup(environmentGroup);
			if(environmentGroup != null && environmentGroup.getEnvironmentGroupId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ENVIRONMENT_GROUP, environmentGroup.getEnvironmentGroupId(), environmentGroup.getEnvironmentGroupName(), user);
			}			
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonEnvironmentGroup(environmentGroup));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableSingleResponse;			
    }
	
	@RequestMapping(value="environment.group.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateEnvironmentGroup(HttpServletRequest req, @ModelAttribute JsonEnvironmentGroup jsonEnvironmentGroup, BindingResult result) {
		JTableResponse jTableResponse;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {
			EnvironmentGroup environmentGroupfromUI  =jsonEnvironmentGroup.getEnvironmentGroup();
				environmentService.updateEnvironmentGroup(environmentGroupfromUI);
				if(environmentGroupfromUI != null && environmentGroupfromUI.getEnvironmentGroupId() != null){
					UserList user=(UserList)req.getSession().getAttribute("USER");
					remarks = "EnvironmentGroup :"+environmentGroupfromUI.getEnvironmentGroupName();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ENVIRONMENT_GROUP, environmentGroupfromUI.getEnvironmentGroupId(), environmentGroupfromUI.getEnvironmentGroupName(),
							jsonEnvironmentGroup.getModifiedField(), jsonEnvironmentGroup.getModifiedFieldTitle(),
							jsonEnvironmentGroup.getOldFieldValue(), jsonEnvironmentGroup.getModifiedFieldValue(), user, remarks);
				}	
				List<JsonEnvironmentGroup> tmpList = new ArrayList();
				tmpList.add(new JsonEnvironmentGroup(environmentGroupfromUI));
				jTableResponse = new JTableResponse("OK",tmpList ,1);					
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating EnvironmentGroup!");
	            log.error("JSON ERROR updating EnvironmentGroup", e);
	        }       
        return jTableResponse;
    }
	
	@RequestMapping(value="environment.category.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getEnvironmentCategoryList(HttpServletRequest req,@RequestParam Integer environmentGroupId) {
		log.debug("inside environment.category.list");
		UserList user=(UserList)req.getSession().getAttribute("USER");
		
		JTableResponse jTableResponse = null;
			try {
				List<EnvironmentCategory> environmentCategoryList=environmentService.getEnvironmentCategoryListByGroup(environmentGroupId);
				
				List<JsonEnvironmentCategory> jsonEnvironmentCategory=new ArrayList<JsonEnvironmentCategory>();
				
				if (environmentCategoryList == null || environmentCategoryList.isEmpty()) {
					
					jTableResponse = new JTableResponse("OK", jsonEnvironmentCategory, 0);
				} else {
					for(EnvironmentCategory environmentCategory: environmentCategoryList){
						jsonEnvironmentCategory.add(new JsonEnvironmentCategory(environmentCategory));
					}
					jTableResponse = new JTableResponse("OK", jsonEnvironmentCategory,jsonEnvironmentCategory.size() );
					environmentCategoryList = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="environment.category.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addEnvironmentCategory(HttpServletRequest req, @ModelAttribute JsonEnvironmentCategory jsonEnvironmentCategory, BindingResult result) {			
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			EnvironmentCategory environmentCategory  =jsonEnvironmentCategory.getEnvironmentCategory();
			EnvironmentCategory parentEnvironmentCategory=null;
			EnvironmentGroup environmentGroup=null;
			if(jsonEnvironmentCategory.getEnvironmentGroupId()!=null){
				environmentGroup=environmentService.getEnvironmentGroupById(jsonEnvironmentCategory.getEnvironmentGroupId());
				environmentCategory.setEnvironmentGroup(environmentGroup);
			}
			if(jsonEnvironmentCategory.getParentEnvironmentCategoryId()!=null && jsonEnvironmentCategory.getParentEnvironmentCategoryId()!=0){
				parentEnvironmentCategory=environmentService.getEnvironmentCategoryById(jsonEnvironmentCategory.getParentEnvironmentCategoryId());
				environmentCategory.setParentEnvironmentCategory(parentEnvironmentCategory);
					
					//Create display name from parent 
					StringBuffer displayName = new StringBuffer();
					displayName.append(environmentCategory.getEnvironmentCategoryName());
					boolean hasParent = true;
					while (hasParent) {
						displayName.insert(0, parentEnvironmentCategory.getEnvironmentCategoryName() + " | ");
						parentEnvironmentCategory = parentEnvironmentCategory.getParentEnvironmentCategory();
						if (parentEnvironmentCategory == null)
							hasParent = false;
					}
					environmentCategory.setDisplayName(displayName.toString());
				}else{
					environmentCategory.setParentEnvironmentCategory(null);
					environmentCategory.setDisplayName(environmentCategory.getEnvironmentCategoryName());			
				}
				
			String errorMessage=commonService.duplicateName(environmentCategory.getEnvironmentCategoryName(), "EnvironmentCategory", "environmentCategoryName", "Environment Category Name", "environmentGroup.environmentGroupId="+environmentGroup.getEnvironmentGroupId());
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			environmentService.addEnvironmentCategory(environmentCategory);
			if(environmentCategory != null && environmentCategory.getEnvironmentCategoryId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY,  environmentGroup.getEnvironmentGroupId(), environmentGroup.getEnvironmentGroupName(), user);
			}
						
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonEnvironmentCategory(environmentCategory));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableSingleResponse;			
    }
	
	
	@RequestMapping(value="environment.category.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateEnvironmentCategory(HttpServletRequest req, @ModelAttribute JsonEnvironmentCategory jsonEnvironmentCategory, BindingResult result) {
		JTableResponse jTableResponse;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {
			EnvironmentCategory environmentCategoryfromUI  =jsonEnvironmentCategory.getEnvironmentCategory();
			
			if(environmentCategoryfromUI != null && environmentCategoryfromUI.getParentEnvironmentCategory().getEnvironmentCategoryId() != null 
					&& !environmentCategoryfromUI.getParentEnvironmentCategory().getEnvironmentCategoryId().equals("0")){
				environmentCategoryfromUI.setParentEnvironmentCategory(environmentService.getEnvironmentCategoryById(environmentCategoryfromUI.getParentEnvironmentCategory().getEnvironmentCategoryId()));
			}
			
			if (jsonEnvironmentCategory.getModifiedField().equalsIgnoreCase("parentEnvironmentCategoryId")) {
				if (jsonEnvironmentCategory.getOldFieldID() == null || jsonEnvironmentCategory.getOldFieldID().equals("-1") || jsonEnvironmentCategory.getOldFieldID().isEmpty()) {
					EnvironmentCategory rootEnvCategory = environmentService.getRootEnvironmentCategory();
					jsonEnvironmentCategory.setOldFieldID(rootEnvCategory.getEnvironmentCategoryId()+"");
					jsonEnvironmentCategory.setOldFieldValue(rootEnvCategory.getEnvironmentCategoryName());
				}
				if (jsonEnvironmentCategory.getModifiedFieldID() == null || jsonEnvironmentCategory.getModifiedFieldID().equals("-1") || jsonEnvironmentCategory.getModifiedFieldID().isEmpty()) {
					EnvironmentCategory rootEnvCategory = environmentService.getRootEnvironmentCategory();
					jsonEnvironmentCategory.setModifiedFieldID(rootEnvCategory.getEnvironmentCategoryId()+"");
					jsonEnvironmentCategory.setModifiedFieldValue(rootEnvCategory.getEnvironmentCategoryName());
				}
				updateEnvironmentCategory(environmentCategoryfromUI, new Integer(jsonEnvironmentCategory.getOldFieldID()), new Integer(jsonEnvironmentCategory.getModifiedFieldID()));
			}else {
				environmentService.updateEnvironmentCategory(environmentCategoryfromUI);
			}
			
				if(environmentCategoryfromUI != null && environmentCategoryfromUI.getEnvironmentCategoryId() != null){
					UserList user=(UserList)req.getSession().getAttribute("USER");
					remarks = "EnvironmentCategory :"+environmentCategoryfromUI.getEnvironmentCategoryName();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ENVIRONMENT_CATEGORY,  
							environmentCategoryfromUI.getEnvironmentCategoryId(), environmentCategoryfromUI.getEnvironmentCategoryName(),
							jsonEnvironmentCategory.getModifiedField(), jsonEnvironmentCategory.getModifiedFieldTitle(),
							jsonEnvironmentCategory.getOldFieldValue(), jsonEnvironmentCategory.getModifiedFieldValue(), user, remarks);
				}
				List<JsonEnvironmentCategory> tmpList = new ArrayList();
				tmpList.add(new JsonEnvironmentCategory(environmentCategoryfromUI));
				jTableResponse = new JTableResponse("OK",tmpList ,1);					
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating EnvironmentCategory!");
	            log.error("JSON ERROR updating EnvironmentCategory", e);
	        }       
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.environment.combination.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateEnvironmentCombination(@ModelAttribute JsonEnvironmentCombination jsonEnvironmentCombination, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
			List<JsonEnvironmentCombination> tmpList = new ArrayList();    
			EnvironmentCombination environmentCombinationfromUI  = jsonEnvironmentCombination.getEnvironment();
			 log.info("environmentCombinationfromUI status: "+environmentCombinationfromUI.getEnvionmentCombinationStatus());
			    if(environmentCombinationfromUI != null){
			    	EnvironmentCombination environmentCombinationfromDB =  environmentService.getEnvironmentCombinationById(environmentCombinationfromUI.getEnvironment_combination_id());
			    	if(environmentCombinationfromDB != null){
			    		if(environmentCombinationfromUI.getEnvionmentCombinationStatus() == 0){
			    			environmentCombinationfromDB.setEnvionmentCombinationStatus(0);
						}else if(environmentCombinationfromUI.getEnvionmentCombinationStatus() == 1){
							environmentCombinationfromDB.setEnvionmentCombinationStatus(1);
						}
			    		environmentService.updateEnvironmentCombination(environmentCombinationfromDB);
						tmpList.add(new JsonEnvironmentCombination(environmentCombinationfromDB));
			    	}
			    }
				
			    jTableResponse = new JTableResponse("OK",tmpList ,1);	
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating EnvironmentCombination!");
	            log.error("JSON ERROR updating EnvironmentCombination", e);
	        }       
        return jTableResponse;
    }
	
	public void updateEnvironmentCategory(EnvironmentCategory environmentCategory, Integer oldParentEnvCategoryId, Integer newParentEnvCategoryId) {
			
			EnvironmentCategory oldParentEnvCategory = null;
			if (oldParentEnvCategoryId == null || oldParentEnvCategoryId < 0 ) {
				oldParentEnvCategory = environmentService.getRootEnvironmentCategory();
			} else {
				oldParentEnvCategory = environmentService.getEnvironmentCategoryById(oldParentEnvCategoryId);
			}
			EnvironmentCategory newParentEnvCategory = null;
			if (newParentEnvCategoryId == null || newParentEnvCategoryId < 0) {
				newParentEnvCategory = environmentService.getRootEnvironmentCategory();
			} else {
				newParentEnvCategory = environmentService.getEnvironmentCategoryById(newParentEnvCategoryId);
			}
			updateEnvCategoryParent(environmentCategory, oldParentEnvCategory, newParentEnvCategory);
		}

	public void updateEnvCategoryParent(EnvironmentCategory environmentCategory, EnvironmentCategory oldEnvCategory, EnvironmentCategory newEnvCategory) {
		
		log.debug("updating Environment Category parent");
		String ENTITY_TABLE_NAME = "environment_category";
	
		try {
			
			log.info("Change EnvironmentCategory parent for " + environmentCategory.getEnvironmentCategoryName() + " from " + oldEnvCategory.getEnvironmentCategoryName() + " to " + newEnvCategory.getEnvironmentCategoryName());
			environmentCategory = environmentService.getEnvironmentCategoryById(environmentCategory.getEnvironmentCategoryId());
			log.info("Updated Environment_Category Indexes : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
			List<EnvironmentCategory> childEnvCategoriesHierarchy = environmentService.listChildEnvCategoryNodesInHierarchyinLayers(environmentCategory);
			
			log.info("Updated Node : " + environmentCategory.getEnvironmentCategoryName());
			log.info("Child Nodes Count : " + childEnvCategoriesHierarchy.size());
			if (childEnvCategoriesHierarchy == null || childEnvCategoriesHierarchy.isEmpty()) {
	
				log.info("Updated EnvironmentCategory Indexes Before Delete : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
				log.info("Parent EnvironmentCategory Indexes Before Delete : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getRightIndex());
				environmentService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, environmentCategory.getLeftIndex(), environmentCategory.getRightIndex());
				
				//Refresh the Parent so that its indices are refreshed
				newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
				log.info("Parent EnvironmentCategory Indexes After Delete : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getRightIndex());
				log.info("Updated EnvironmentCategory Indexes After Delete : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
				//Add the updated EnvironmentCategory to its new parent hierarchy
				environmentService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newEnvCategory.getRightIndex());
				newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
	
				log.info("Parent EnvironmentCategory Indexes After Add : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getRightIndex());
				log.info("Updated EnvironmentCategory Indexes After Add : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
				
				//Update the EnvironmentCategory's new parent in database
				environmentCategory.setParentEnvironmentCategory(newEnvCategory);
				environmentCategory.setLeftIndex(newEnvCategory.getRightIndex() - 2);
				environmentCategory.setRightIndex(newEnvCategory.getRightIndex() - 1);
				environmentService.updateEnvironmentCategory(environmentCategory);
				environmentCategory = environmentService.getEnvironmentCategoryById(environmentCategory.getEnvironmentCategoryId());
				log.info("Updated EnvironmentCategory Indexes After Add : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
				return;
			}
			
			
			for (EnvironmentCategory envCategoryObj : childEnvCategoriesHierarchy) {
				log.info("EnvironmentCategory Hierarchy Layered sorted : " + envCategoryObj.getLeftIndex() + " : " + envCategoryObj.getEnvironmentCategoryName() + " : " + envCategoryObj.getRightIndex());
			}
			
			//Remove all the child nodes, starting from leaf nodes and then progress upwards
			int childNodesCount = childEnvCategoriesHierarchy.size();
			for (int i = childNodesCount-1; i >= 0; i--) {
				
				EnvironmentCategory childEnvCategory = childEnvCategoriesHierarchy.get(i);
				childEnvCategory = environmentService.getEnvironmentCategoryById(childEnvCategory.getEnvironmentCategoryId());
				oldEnvCategory = environmentService.getEnvironmentCategoryById(oldEnvCategory.getEnvironmentCategoryId());
				log.info("Old Parent before Removing child Node : " + oldEnvCategory.getLeftIndex() + " : " + oldEnvCategory.getEnvironmentCategoryName() + " : " + oldEnvCategory.getRightIndex());
				environmentService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, childEnvCategory.getLeftIndex(), childEnvCategory.getRightIndex());
				log.info("Removed child Node : " + childEnvCategory.getLeftIndex() + " : " + childEnvCategory.getEnvironmentCategoryName() + " : " + childEnvCategory.getRightIndex());
				oldEnvCategory = environmentService.getEnvironmentCategoryById(oldEnvCategory.getEnvironmentCategoryId());
				log.info("Old Parent after Removing child Node : " + oldEnvCategory.getLeftIndex() + " : " + oldEnvCategory.getEnvironmentCategoryName() + " : " + oldEnvCategory.getRightIndex());
			}
			//Remove the updated node. With this, the hierarchy now has no indexes for the updated EnvironmentCategory and its n level child nodes 
			environmentCategory = environmentService.getEnvironmentCategoryById(environmentCategory.getEnvironmentCategoryId());
			environmentService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, environmentCategory.getLeftIndex(), environmentCategory.getRightIndex());
			log.info("Removed updated Node : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getEnvironmentCategoryName() + " : " + environmentCategory.getRightIndex());
			oldEnvCategory = environmentService.getEnvironmentCategoryById(oldEnvCategory.getEnvironmentCategoryId());
			log.info("Old Parent after updated Node : " + oldEnvCategory.getLeftIndex() + " : " + oldEnvCategory.getEnvironmentCategoryName() + " : " + oldEnvCategory.getRightIndex());
			log.info("Finished removing Hierarchy from old parent");
			
			//Update the EnvironmentCategory's new parent in database
	
			newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
			log.info("New Parent before adding updated Node : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getEnvironmentCategoryName() + " : " + newEnvCategory.getRightIndex());
			environmentService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newEnvCategory.getRightIndex());
			newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
			environmentCategory.setParentEnvironmentCategory(newEnvCategory);
			environmentCategory.setLeftIndex(newEnvCategory.getRightIndex() - 2);
			environmentCategory.setRightIndex(newEnvCategory.getRightIndex() - 1);
			environmentService.updateEnvironmentCategory(environmentCategory);
			environmentCategory = environmentService.getEnvironmentCategoryById(environmentCategory.getEnvironmentCategoryId());
			log.info("Updated EnvironmentCategory Indexes After Add : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getRightIndex());
			log.info("Added updated Node : " + environmentCategory.getLeftIndex() + " : " + environmentCategory.getEnvironmentCategoryName() + " : " + environmentCategory.getRightIndex());
			newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
			log.info("New Parent after adding updated Node : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getEnvironmentCategoryName() + " : " + newEnvCategory.getRightIndex());
			oldEnvCategory = environmentService.getEnvironmentCategoryById(oldEnvCategory.getEnvironmentCategoryId());
			log.info("Old Parent after adding updated Node : " + oldEnvCategory.getLeftIndex() + " : " + oldEnvCategory.getEnvironmentCategoryName() + " : " + oldEnvCategory.getRightIndex());
			
			//Add the child nodes back to the hierarchy. Their parents have not changed
			for (int i = 0; i < childNodesCount; i++) {
				
				EnvironmentCategory childEnvCategory = childEnvCategoriesHierarchy.get(i);
				//Reload parent from DB, so that it contains the updated indices
				EnvironmentCategory tempParentEnv = environmentService.getEnvironmentCategoryById(childEnvCategory.getParentEnvironmentCategory().getEnvironmentCategoryId());
				log.info("Child node parent : " + tempParentEnv.getLeftIndex() + " : " + tempParentEnv.getEnvironmentCategoryName() + " : " + tempParentEnv.getRightIndex());
				environmentService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, tempParentEnv.getRightIndex());
				tempParentEnv = environmentService.getEnvironmentCategoryById(tempParentEnv.getEnvironmentCategoryId());
				childEnvCategory.setLeftIndex(tempParentEnv.getRightIndex() - 2);
				childEnvCategory.setRightIndex(tempParentEnv.getRightIndex() - 1);
				environmentService.updateEnvironmentCategory(childEnvCategory);
				log.info("Added child Node : " + childEnvCategory.getLeftIndex() + " : " + childEnvCategory.getEnvironmentCategoryName() + " : " + childEnvCategory.getRightIndex());
				newEnvCategory = environmentService.getEnvironmentCategoryById(newEnvCategory.getEnvironmentCategoryId());
				log.info("New Parent after adding child Node : " + newEnvCategory.getLeftIndex() + " : " + newEnvCategory.getEnvironmentCategoryName() + " : " + newEnvCategory.getRightIndex());
				oldEnvCategory = environmentService.getEnvironmentCategoryById(oldEnvCategory.getEnvironmentCategoryId());
				log.info("Old Parent after adding child Node : " + oldEnvCategory.getLeftIndex() + " : " + oldEnvCategory.getEnvironmentCategoryName() + " : " + oldEnvCategory.getRightIndex());
			}
			log.debug("Parent EnvironmentCategory Updated Successfully");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}
	
	@RequestMapping(value="environment.category.update.parent",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateFeatureParentInHierarchy(HttpServletRequest request,@RequestParam Integer environmentCategoryId, @RequestParam Integer oldParentCategoryId, @RequestParam Integer newParentCategoryId) {

		log.debug("Updating EnvironmentCategory");
		JTableResponse jTableResponse;
		try {
			EnvironmentCategory environmentCategory = environmentService.getEnvironmentCategoryById(environmentCategoryId);
			updateEnvironmentCategory(environmentCategory, oldParentCategoryId, newParentCategoryId);
			jTableResponse = new JTableResponse("OK", "Updated EnvironmentCategory parent");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating EnvironmentCategory parent!");
			log.error("JSON ERROR updating EnvironmentCategory parent", e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="common.standaloane.environment.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getEnvironmentStandalonList() {
		log.info("inside common.standaloane.environment.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonStandaloneOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption typeOption=new JsonCommonOption();
			
		
			typeOption.setId(0);
			typeOption.setValue("No");
			jsonStandaloneOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setId(1);
			typeOption.setValue("Yes");
			jsonStandaloneOptions.add(typeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonStandaloneOptions, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in environment Standalone list");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value = "get.environment.details.by.environmentCombinationId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listEnvironmentDetailsByEnvironmentCombinationId(HttpServletRequest request, @RequestParam Integer environmentCombinationId) {
		JTableResponse jTableResponse=null;
		try{
			if(environmentCombinationId != null && environmentCombinationId == 0){
				return jTableResponse = new JTableResponse("INFORMATION","Enviroment CombinationId should not empty!");
			}
			List<Object[]> envirionmentDetails = environmentService.getEnvironmentDetailsByEnvironmentCombinationId(environmentCombinationId);
			ArrayList<HashMap<String, Object>> envirionmentList = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : envirionmentDetails) {
				HashMap<String, Object> environment =new HashMap<String, Object>();
				environment.put("environmentId", (Integer)row[0]);
				environment.put("environmentName", (String)row[1]);
				environment.put("description", (String)row[2]);
				environment.put("environmentCategoryName", (String)row[3]);
				environment.put("environmentGroupName", (String)row[4]);
				envirionmentList.add(environment);					
			}				
			jTableResponse = new JTableResponse("OK", envirionmentList, envirionmentList.size());
		}catch(Exception e) {
			log.error("Error in listEnvironmentDetailsByEnvironmentCombinationId",e);
		}
		return jTableResponse;
	}
	
}
