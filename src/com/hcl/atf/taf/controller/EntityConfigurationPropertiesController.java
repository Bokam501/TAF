package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.model.json.JsonEntityConfigurationProperties;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
import com.hcl.atf.taf.service.ProductListService;

@Controller
public class EntityConfigurationPropertiesController {
	private static final Log log = LogFactory.getLog(EntityConfigurationPropertiesController.class);
	
	@Autowired
	private EntityConfigurationPropertiesService entityConfigurationPropertiesService;
	@Autowired
	private ProductListService productListService;
	
	@RequestMapping(value="entityConfigureProperties.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addEntityConfigureProperties(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {
		JTableSingleResponse jTableSingleResponse=null;
		try { 
			List<EntityConfigurationProperties> entityConfigurationPropertiesList=null;
			EntityConfigurationProperties  entityConfigurationProperties=new EntityConfigurationProperties();
			String propertyTrpParameter = mapData.get("propertyTrp");
			Integer entityConfigProprsEntityId=null;
		    Integer	entityMasterId=Integer.parseInt(mapData.get("entityMasterId"));
		    List<EntityConfigurationPropertiesMaster> entityConfigurationPropertiesMasterList=entityConfigurationPropertiesService.listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")), -1);
			EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster=entityConfigurationPropertiesMasterList.get(0);
			if(entityMasterId.equals(IDPAConstants.ENTITY_TEST_RUN_PLAN_ID)){
				entityConfigProprsEntityId=Integer.parseInt(mapData.get("testRunPlanId"));
				entityConfigurationPropertiesList=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(entityConfigProprsEntityId,entityMasterId, -1);
			}
			if(entityMasterId.equals(IDPAConstants.ENTITY_RUN_CONFIGURATION_ID)){
				entityConfigProprsEntityId=Integer.parseInt(mapData.get("runConfigurationId"));
				entityConfigurationPropertiesList=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(entityConfigProprsEntityId,entityMasterId,-1);
			}
			if(entityConfigurationPropertiesList.size()!=0){
				if(Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) != 5 && Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) != 6){
					for(int i=0;i<entityConfigurationPropertiesList.size();i++){
						EntityConfigurationPropertiesMaster ecmp = entityConfigurationPropertiesList.get(i).getEntityConfigurationPropertiesMaster();
						if(ecmp.getEntityConfigPropertiesMasterId() == Integer.parseInt(mapData.get("entityConfigPropertiesMasterId"))){
							entityConfigurationProperties = entityConfigurationPropertiesList.get(i);
							entityConfigurationProperties.setEntityId(entityConfigProprsEntityId);		
							entityConfigurationProperties.setCreatedDate(new Date());
						}
					}
				} else if(Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) == 5 || Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) != 6){
					for(int i=0;i<entityConfigurationPropertiesList.size();i++){
						EntityConfigurationPropertiesMaster ecmp = entityConfigurationPropertiesList.get(i).getEntityConfigurationPropertiesMaster();
						if(ecmp.getEntityConfigPropertiesMasterId() == Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) && entityConfigurationPropertiesList.get(i).getProperty() != null && entityConfigurationPropertiesList.get(i).getProperty().equalsIgnoreCase(mapData.get("propertyTrp"))){
							entityConfigurationProperties = entityConfigurationPropertiesList.get(i);
							entityConfigurationProperties.setValue(mapData.get("propertyValue"));
							entityConfigurationProperties.setEntityId(entityConfigProprsEntityId);	
						}
					}
				}				
			}	
			
			if(Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) ==5 || Integer.parseInt(mapData.get("entityConfigPropertiesMasterId")) == 6){
				entityConfigurationProperties.setValue(mapData.get("propertyValue"));
				entityConfigurationProperties.setProperty(mapData.get("propertyTrp"));
			}				
			if(entityConfigurationProperties.getCreatedDate() == null)
				entityConfigurationProperties.setCreatedDate(new Date());
			entityConfigurationProperties.setEntityId(entityConfigProprsEntityId);
			entityConfigurationProperties.setValue(mapData.get("propertyValue"));		
			entityConfigurationProperties.setEntityConfigurationPropertiesMaster(entityConfigurationPropertiesMaster);			
			entityConfigurationProperties.setStatus(1);
			entityConfigurationProperties.setModifiedDate(new Date());
			entityConfigurationPropertiesService.addEntityConfigureProperties(entityConfigurationProperties);
		
			jTableSingleResponse = new JTableSingleResponse("OK","Record is saved");
	           
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Entity Configuration Property!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
		
    }
	@RequestMapping(value="entityConfigureProperties.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listEntityConfigurePropertiesByEntityID(HttpServletRequest request, @RequestParam Integer entityId,@RequestParam Integer entityMasterId) {
		log.info("entityConfigureProperties.list");
		JTableResponse jTableResponse;
		
		try {
			List<EntityConfigurationProperties> entityConfigurationPropertiesList=null;
			List<JsonEntityConfigurationProperties> jsonEntityConfigurationPropertiesList=new ArrayList<JsonEntityConfigurationProperties>();
			
				entityConfigurationPropertiesList=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(entityId,entityMasterId,-1);
				 for(EntityConfigurationProperties entityConfigurationProperties:entityConfigurationPropertiesList){
						jsonEntityConfigurationPropertiesList.add(new JsonEntityConfigurationProperties(entityConfigurationProperties));
					}
				 
			  jTableResponse = new JTableResponse("OK", jsonEntityConfigurationPropertiesList,jsonEntityConfigurationPropertiesList.size());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }	
	
	@RequestMapping(value="entityconfigproperty.remove",method=RequestMethod.POST ,produces="application/json")
	 public @ResponseBody JTableResponse deleteEntityConfigurePropertiesByEntityID(@RequestParam Integer entityConfigId,@RequestParam Integer entityMasterId, @RequestParam Integer entityId) {
		log.info("entityConfigureProperties.remove");
		JTableResponse jTableResponse;		
		try {
			EntityConfigurationProperties entityConfigurationProperties = null;
			  List<EntityConfigurationProperties> entityConfigurationPropertiesList=null;
			  entityConfigurationPropertiesList=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(entityId,entityMasterId,-1);
			  for(EntityConfigurationProperties e : entityConfigurationPropertiesList){
				  if(e != null && e.getEntityConfigPropertyId() != null && e.getEntityConfigPropertyId().intValue() ==entityConfigId.intValue()){
					  entityConfigurationProperties = e;
				  }
			  }
			  if(entityConfigurationProperties != null){
				  entityConfigurationPropertiesService.deleteEntityConfigureProperties(entityConfigurationProperties);
				  jTableResponse = new JTableResponse("OK", "Property Deleted!");
			  } else {
				  jTableResponse = new JTableResponse("ERROR","Property deletion failed!");
			  }
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="entityconfigproperty.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateEntityConfigureProperties(@ModelAttribute JsonEntityConfigurationProperties jsonEntityConfigurationProperties, BindingResult result) {
		JTableResponse jTableResponse;
		EntityConfigurationProperties entityConfPropFromUI = null;
		EntityConfigurationProperties entityConfPropFromDB = null;
		List<JsonEntityConfigurationProperties> jsonEntityConfigurationPropertiesList =new ArrayList<JsonEntityConfigurationProperties>();
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {	//convert JsonHostList to HostList for persist operation
			entityConfPropFromUI = jsonEntityConfigurationProperties.getEntityConfigurationProperties();
			if(jsonEntityConfigurationProperties.getValue() != null){
				entityConfPropFromUI.setValue(jsonEntityConfigurationProperties.getValue());
			}
			if(jsonEntityConfigurationProperties.getProperty() != null){
				entityConfPropFromUI.setProperty(jsonEntityConfigurationProperties.getProperty());;
			}
			entityConfPropFromDB = entityConfigurationPropertiesService.getEntityConfigureProperties(entityConfPropFromUI.getEntityConfigPropertyId());
			if(entityConfPropFromDB.getEntityConfigPropertyId() == entityConfPropFromUI.getEntityConfigPropertyId()){
				entityConfigurationPropertiesService.updateEntityConfigureProperties(entityConfPropFromUI);
				jsonEntityConfigurationPropertiesList.add(new JsonEntityConfigurationProperties(entityConfPropFromUI));
				jTableResponse = new JTableResponse("OK", jsonEntityConfigurationPropertiesList,jsonEntityConfigurationPropertiesList.size());
			} else {
				jTableResponse = new JTableResponse("ERROR","Error updating record!");
			}
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error updating record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;			
    }
}
