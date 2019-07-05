package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonCustomFieldConfigMaster;
import com.hcl.atf.taf.model.json.JsonCustomFieldValues;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonSimpleOption;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.EventsService;

@Controller
public class CustomFieldManagementController {

	@Autowired
	CustomFieldService customFieldService;
	
	@Autowired
	EventsService eventsService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	MongoDBService mongoDBService;
	
	private static final Log log = LogFactory.getLog(CustomFieldManagementController.class);
	
	@RequestMapping(value="custom.field.configuration.eligible.entity.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getCustomFieldEligibleEntityList() {
		
		log.debug("Inside entity.type.list");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
			jsonEntityMasters = customFieldService.getCustomFieldEligibleEntityList();
			if (jsonEntityMasters == null || jsonEntityMasters.isEmpty()) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","There are no custom field eligible entity available");
	        	log.info("There are no custom field eligible entity available");
			} else {
				jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityMasters, true);
			}
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity type list");
			log.error("Unable to get entity type list", e);
		}
		return jTableResponseOptions;
	}
	
	@RequestMapping(value="custom.field.configuration.engagement.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getCustomFieldEngagementList(HttpServletRequest request) {
		log.debug("inside custom.field.configuration.engagement.list");
		JTableResponseOptions jTableResponseOptions;
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<JsonTestFactory> jsonTestFactories = new ArrayList<JsonTestFactory>();
			if(user.getUserRoleMaster().getUserRoleId() == IDPAConstants.ROLE_ID_ADMIN){
				jsonTestFactories = customFieldService.getCustomFieldEngagementList(null);
			}else{
				jsonTestFactories = customFieldService.getCustomFieldEngagementList(user.getUserId());
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestFactories, true);  
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show engagement!");
            log.error("Error in getCustomFieldEngagementList - ", e);
        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="custom.field.configuration.drop.down.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldConfigurationDropDownOptions(@RequestParam String fieldName) {
		log.debug("inside custom.field.configuration.drop.down.options");
		JTableResponse jTableResponse;
		try {
			List<Map<String, String>> customFieldConfigurationOptions = customFieldService.getCustomFieldConfigurationDropDownOptions(fieldName);
			jTableResponse = new JTableResponse("OK", customFieldConfigurationOptions);  
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Unable to show drop down options!");
            log.error("Error in getCustomFieldConfigurationDropDownOptions - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.configuration.product.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getCustomFieldProductList(@RequestParam Integer engagementId, HttpServletRequest request) {
		log.debug("inside custom.field.configuration.product.list");
		JTableResponseOptions jTableResponseOptions;
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<JsonProductMaster> jsonProductMasters = new ArrayList<JsonProductMaster>();
			if(user.getUserRoleMaster().getUserRoleId() == IDPAConstants.ROLE_ID_ADMIN){
				jsonProductMasters = customFieldService.getCustomFieldProductOfEngagementList(engagementId, null);
			}else{
				jsonProductMasters = customFieldService.getCustomFieldProductOfEngagementList(engagementId, user.getUserId());
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMasters, true);  
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show product!");
            log.error("Error in getCustomFieldProductList - ", e);
        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="custom.field.exist.for.entity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldsExistForEntity(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer parentEntityId, @RequestParam Integer parentEntityInstanceId) {
		log.debug("inside custom.field.exist.for.entity");
		JTableResponse jTableResponse;
		try {
			Integer engagementId = 0;
			Integer productId = 0;
			if(parentEntityId == IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID){
				engagementId = parentEntityInstanceId;
			}else if(parentEntityId == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
				productId = parentEntityInstanceId;
			}
			List<JsonCustomFieldConfigMaster> jsonCustomFieldConfigMasters = new ArrayList<JsonCustomFieldConfigMaster>();
			List<CustomFieldConfigMaster> customFieldConfigMasters = customFieldService.getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, null, null, null, false, false);
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					jsonCustomFieldConfigMasters.add(new JsonCustomFieldConfigMaster(customFieldConfigMaster));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonCustomFieldConfigMasters, jsonCustomFieldConfigMasters.size());
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Unable to show custom field list!");
            log.error("Error in getCustomFieldsExistForEntity - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.save.or.update.for.entity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse customFieldsSaveOrUpdateForEntity(@ModelAttribute JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, BindingResult result, HttpServletRequest request) {
		log.debug("inside custom.field.save.or.update.for.entity");
		JTableResponse jTableResponse = null;
		try {
			if(result.hasErrors()){
				return jTableResponse = new JTableResponse("ERROR", "Please check, Some field or field data type is not valid");
			}
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			Object customField = customFieldService.customFieldsSaveOrUpdateForEntity(jsonCustomFieldConfigMaster, customFieldService, user);
			
			if(customField != null && customField instanceof String){
				jTableResponse = new JTableResponse("ERROR", (String)customField);
				return jTableResponse;
			}else if(customField != null && customField instanceof CustomFieldConfigMaster){
				CustomFieldConfigMaster customFieldConfigMaster = (CustomFieldConfigMaster) customField;
				List<JsonCustomFieldConfigMaster> jsonCustomFieldConfigMasters = new ArrayList<JsonCustomFieldConfigMaster>();
				jsonCustomFieldConfigMasters.add(new JsonCustomFieldConfigMaster(customFieldConfigMaster));
				jTableResponse = new JTableResponse("OK", jsonCustomFieldConfigMasters);
			}
			
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error while save or update custom field");
            log.error("Error in customFieldsSaveOrUpdateForEntity - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse deleteCustomField(@RequestParam Integer customFieldId) {
		log.debug("inside custom.field.delete");
		JTableSingleResponse jTableSingleResponse = null;
		try {
			Boolean isCustomFiledHasValue = customFieldService.isCustomFieldHasValueForInstance(customFieldId);
			if(isCustomFiledHasValue){
				jTableSingleResponse = new JTableSingleResponse("OK", "Custom field has value for some instance, please delete value before deleting field");
			}else{
				CustomFieldConfigMaster customFieldConfigMaster = new CustomFieldConfigMaster();
				customFieldConfigMaster.setId(customFieldId);
				customFieldService.deleteCustomField(customFieldConfigMaster);
				jTableSingleResponse = new JTableSingleResponse("OK", "Custom field deleted successfully");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to delete custom filed");
            log.error("Error in deleteCustomField - ", e);
        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="custom.field.exist.for.instance",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldsExistForInstance(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, @RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam String frequencyType, @RequestParam String frequency, @RequestParam Integer frequencyMonth, @RequestParam Integer frequencyYear, HttpServletRequest request) {
		log.debug("inside custom.field.exist.for.instance");
		JTableResponse jTableResponse = null;
		try {
			Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
			List<HashMap<String, Object>> customFields = customFieldService.getCustomFieldValuesExistForInstance(entityId, entityTypeId, entityInstanceId, engagementId, productId, frequencyType, frequency, frequencyMonth, frequencyYear, 1, modifiedAfterDate);
			jTableResponse = new JTableResponse("OK", customFields, customFields.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields of instance");
            log.error("Error in getCustomFieldsExistForInstance - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.columns.exist.for.all.instance.of.entity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldsColumnsExistForAllInstanceOfEntity(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer engagementId, @RequestParam Integer productId) {
		log.debug("inside custom.field.columns.exist.for.all.instance.of.entity");
		JTableResponse jTableResponse = null;
		try {
			List<CustomFieldConfigMaster> customFieldConfigMasters = customFieldService.getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, "Single", null, 1, false, false);
			List<JsonSimpleOption> columnNames = new ArrayList<JsonSimpleOption>();	
			columnNames.add(new JsonSimpleOption("Id"));
			columnNames.add(new JsonSimpleOption("Name"));
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				columnNames.add(new JsonSimpleOption("Activity Type"));
				columnNames.add(new JsonSimpleOption("Status"));
				columnNames.add(new JsonSimpleOption("Assignee"));
				columnNames.add(new JsonSimpleOption("Reviewer"));
			}
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					columnNames.add(new JsonSimpleOption(customFieldConfigMaster.getFieldName()));
				}
			}
			jTableResponse = new JTableResponse("OK", columnNames, columnNames.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields columns for all instance of entity");
            log.error("Error in getCustomFieldsColumnsExistForAllInstanceOfEntity - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.exist.for.all.instance.of.entity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldsExistForAllInstanceOfEntity(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId, @RequestParam Integer engagementId, @RequestParam Integer productId, HttpServletRequest request) {
		log.debug("inside custom.field.exist.for.all.instance.of.entity");
		JTableResponse jTableResponse = null;
		try {
			Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
			List<HashMap<String, Object>> customFields = customFieldService.getCustomFieldValuesExistForAllInstanceOfEntity(entityId, entityTypeId, entityParentInstanceId, engagementId, productId, 1, modifiedAfterDate);
			jTableResponse = new JTableResponse("OK", customFields, customFields.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields for all instance of entity");
            log.error("Error in getCustomFieldsExistForAllInstanceOfEntity - ", e);
        }
        return jTableResponse;
    }

	@RequestMapping(value="custom.field.individual.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse customFieldsIndividualDetail(@RequestParam Integer customFieldValueId, @RequestParam Integer customFieldId, @RequestParam Integer frequencyOrder, @RequestParam Integer frequencyMonth, @RequestParam Integer frequencyYear) {
		log.debug("inside custom.field.individual.details");
		JTableResponse jTableResponse;
		try {
			List<HashMap<String, Object>> customFields = customFieldService.getCustomFieldsIndividualDetail(customFieldValueId, customFieldId, frequencyOrder, frequencyMonth, frequencyYear, 1);
			jTableResponse = new JTableResponse("OK", customFields);
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error while getting custom field");
            log.error("Error in customFieldsIndividualDetail - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="custom.field.value.save.or.update.for.instance",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse customFieldsValueSaveOrUpdateForInstance(@ModelAttribute JsonCustomFieldValues jsonCustomFieldValues, HttpServletRequest request) {
		log.debug("inside custom.field.value.save.or.update.for.instance");
		JTableSingleResponse jTableSingleResponse;
		String remarks = "";
		try {
			if(jsonCustomFieldValues.getFieldValue() == null || jsonCustomFieldValues.getFieldValue().trim().isEmpty()){
			}
			jsonCustomFieldValues.setFieldValue(jsonCustomFieldValues.getFieldValue().trim());
			CustomFieldConfigMaster customFieldConfigMaster = customFieldService.getCustomFieldById(jsonCustomFieldValues.getCustomFieldId(), 1);
			String errorMessage = customFieldService.validateCustomFieldValueForDataType(customFieldConfigMaster.getDataType(), jsonCustomFieldValues.getFieldValue());
			if(errorMessage == null || errorMessage.trim().isEmpty()){
				errorMessage = customFieldService.validateCustomFieldValueForDataRange(customFieldConfigMaster.getDataType(), jsonCustomFieldValues.getFieldValue(), customFieldConfigMaster.getUpperLimit(), customFieldConfigMaster.getLowerLimit());
			}
			if(errorMessage == null || errorMessage.trim().isEmpty()){
				CustomFieldValues customFieldValues = jsonCustomFieldValues.getCustomFieldValues();
				UserList user = (UserList)request.getSession().getAttribute("USER");
				if(customFieldValues.getId() == null || customFieldValues.getId() <= 0){
					customFieldValues.setCreatedBy(user);
					customFieldValues.setCreatedOn(new Date());
				}
				customFieldValues.setModifiedBy(user);
				customFieldValues.setModifiedOn(new Date());
				customFieldService.saveOrUpdateCustomFieldValue(customFieldValues);
				if(customFieldValues != null && customFieldValues.getId() != null){				
					String collectionName = "";
					String entityTypeName = "";
					if(customFieldConfigMaster.getEntity().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID || customFieldConfigMaster.getEntity().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
						collectionName = MongodbConstants.ACTIVITY;
						entityTypeName = IDPAConstants.ENTITY_ACTIVITY;
					}
					Object fieldValue = customFieldService.convertCustomFieldDataType(customFieldValues.getFieldValue(), customFieldConfigMaster.getDataType());
					if(collectionName != null && !collectionName.trim().isEmpty()){
						mongoDBService.addOrUpdateCustomField(customFieldValues.getEntityInstanceId(), customFieldConfigMaster.getFieldName(), fieldValue, collectionName);
					}
					if(entityTypeName != null && !entityTypeName.trim().isEmpty()){
						eventsService.addEntityChangedEvent(entityTypeName, jsonCustomFieldValues.getEntityInstanceId(), "CF-"+jsonCustomFieldValues.getCustomFieldName(),
								jsonCustomFieldValues.getModifiedField(), "CF-"+jsonCustomFieldValues.getModifiedFieldTitle(),
								jsonCustomFieldValues.getOldFieldValue(), jsonCustomFieldValues.getModifiedFieldValue(), user, remarks);
					}
					
				}
				if(customFieldValues.getId() == null || customFieldValues.getId() <= 0){
					jTableSingleResponse = new JTableSingleResponse("OK", jsonCustomFieldValues, "Value added successfully");
				}else{
					jTableSingleResponse = new JTableSingleResponse("OK", jsonCustomFieldValues, "Value updated successfully");
				}
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", errorMessage);
			}
        } catch (Exception e) {
        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error while adding custom field");
            log.error("Error in customFieldsValueSaveOrUpdateForInstance - ", e);
        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="custom.field.exist.for.entity.engagementId.and.productId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldsExistForEntityByEnagementIdAndProductId(@RequestParam Integer entityId, @RequestParam Integer parentEntityId, @RequestParam Integer parentEntityInstanceId) {
		log.debug("inside custom.field.exist.for.entity.engagementId.and.productId");
		JTableResponse jTableResponse;
		try {
			Integer engagementId = 0;
			Integer productId = 0;
			if(parentEntityId == IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID){
				engagementId = parentEntityInstanceId;
			}else if(parentEntityId == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
				productId = parentEntityInstanceId;
			}
			List<JsonCustomFieldConfigMaster> jsonCustomFieldConfigMasters = new ArrayList<JsonCustomFieldConfigMaster>();
			 List<CustomFieldConfigMaster> customFieldConfigMasters = customFieldService.getCustomFieldExistForEngagementIdAndProductId(entityId,engagementId, productId, "Single", null, null, false, false);
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					jsonCustomFieldConfigMasters.add(new JsonCustomFieldConfigMaster(customFieldConfigMaster));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonCustomFieldConfigMasters, jsonCustomFieldConfigMasters.size());
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Unable to show custom field list!");
            log.error("Error in  getCustomFieldsExistForEntityByEnagementIdAndProductId - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="get.all.custom.field.exist.for.entityType",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getCustomFieldValuesExistForEntityTypeBased(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId,  @RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam String frequencyType, @RequestParam String frequency, @RequestParam Integer frequencyMonth, @RequestParam Integer frequencyYear, HttpServletRequest request) {
		log.debug("inside custom.field.exist.for.instance");
		JTableResponse jTableResponse = null;
		try {
			Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
			List<HashMap<String, Object>> customFields = customFieldService.getCustomFieldValuesExistForEntityTypeBased(entityId, entityTypeId,entityInstanceId,engagementId, productId, frequencyType, frequency, frequencyMonth, frequencyYear, 1, modifiedAfterDate);
			jTableResponse = new JTableResponse("OK", customFields, customFields.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields of instance");
            log.error("Error in getCustomFieldsExistForInstance - ", e);
        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.custom.field.exist.for.all.instance",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getAllCustomFieldsExistInstance(@RequestParam Integer entityId, @RequestParam Integer entityParentInstanceId, @RequestParam Integer engagementId, @RequestParam Integer productId, HttpServletRequest request) {
		log.debug("inside get.custom.field.exist.for.all.instance");
		JTableResponse jTableResponse = null;
		try {
			Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
			List<HashMap<String, Object>> customFields = customFieldService.getAllCustomFieldsExistInstance(entityId,entityParentInstanceId, engagementId, productId, 1, modifiedAfterDate);
			jTableResponse = new JTableResponse("OK", customFields, customFields.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields for all instance of entity");
            log.error("Error in getAllCustomFieldsExistInstance - ", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="get.all.series.custom.field.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getSeriesCustomFieldValuesCount(@RequestParam Integer entityId, @RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId,  @RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam String frequencyType,HttpServletRequest request) {
		log.debug("inside custom.field.exist.for.instance");
		JTableResponse jTableResponse = null;
		ArrayList<HashMap<String, Integer>> seriesCustomFieldCountList = new ArrayList<HashMap<String, Integer>>();
		try {
			Integer frequencyMonth=-1;
			Integer frequencyYear = new Date().getYear();
			Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
			HashMap<String, Integer> customFields = customFieldService.getSeriesCustomFieldValuesCount(entityId, entityTypeId,entityInstanceId,engagementId, productId, frequencyType,frequencyMonth, frequencyYear, 1, modifiedAfterDate);
			seriesCustomFieldCountList.add(customFields);
			jTableResponse = new JTableResponse("OK", seriesCustomFieldCountList, seriesCustomFieldCountList.size());
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR", "Unable to get custom fields of instance");
            log.error("Error in getCustomFieldsExistForInstance - ", e);
        }
        return jTableResponse;
    }
	
}
