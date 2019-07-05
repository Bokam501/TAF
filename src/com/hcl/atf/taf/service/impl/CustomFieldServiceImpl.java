package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.CustomFieldDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonCustomFieldConfigMaster;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.EventsService;

@Service
public class CustomFieldServiceImpl implements CustomFieldService {

	private static final Log log = LogFactory.getLog(CustomFieldServiceImpl.class);
	
	@Autowired
	CustomFieldDAO customFieldDAO;
	
	@Autowired
	TestFactoryDao testFactoryDao;
	
	@Autowired
	ProductMasterDAO productMasterDAO;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	MongoDBService mongoDBService;
	
	@Autowired
	EventsService eventsService;
	
	@Override
	@Transactional
	public List<JsonEntityMaster> getCustomFieldEligibleEntityList() {
		List<EntityMaster> entityMasters = customFieldDAO.getWorkflowCapableEntityTypeList();
		List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
		if(entityMasters != null && entityMasters.size() > 0){
			for(EntityMaster entityMaster : entityMasters){
				JsonEntityMaster jesonEntityMaster = new JsonEntityMaster(entityMaster);
				jsonEntityMasters.add(jesonEntityMaster);
			}
		}
		return jsonEntityMasters;
	}

	@Override
	@Transactional
	public List<JsonTestFactory> getCustomFieldEngagementList(Integer userId) {
		List<TestFactory> testFactories = testFactoryDao.getEngagementListByUserId(userId);
		List<JsonTestFactory> jsonTestFactories = new ArrayList<JsonTestFactory>();
		if(testFactories != null && testFactories.size() > 0){
			if(testFactories == null || testFactories.size() == 0){
				JsonTestFactory jsonTestFactory = new JsonTestFactory();
				jsonTestFactory.setTestFactoryId(0);
				jsonTestFactory.setTestFactoryName("--");
				jsonTestFactories.add(jsonTestFactory);
			}else{
				for(TestFactory testFactory : testFactories){
					jsonTestFactories.add(new JsonTestFactory(testFactory));
				}	
			}
		}
		return jsonTestFactories;
	}

	@Override
	@Transactional
	public List<JsonProductMaster> getCustomFieldProductOfEngagementList(Integer engagementId, Integer userId) {
		List<ProductMaster> productMasters = productMasterDAO.getProductsOfEngagementByUserId(engagementId, userId);
		List<JsonProductMaster> jsonProductMasters = new ArrayList<JsonProductMaster>();
		if(productMasters != null && productMasters.size() > 0){
			if(productMasters == null || productMasters.size() == 0){
				JsonProductMaster jsonProductMaster = new JsonProductMaster();
				jsonProductMaster.setProductId(0);
				jsonProductMaster.setProductName("--");
				jsonProductMasters.add(jsonProductMaster);
			}else{
				for(ProductMaster productMaster : productMasters){
					jsonProductMasters.add(new JsonProductMaster(productMaster));
				}	
			}
		}
		return jsonProductMasters;
	}

	@Override
	@Transactional
	public List<Map<String, String>> getCustomFieldConfigurationDropDownOptions(String fieldName) {
		List<Map<String, String>> customFieldConfigurationDropDownOptions = new ArrayList<Map<String, String>>();
		if("dataType".equalsIgnoreCase(fieldName) || "controlType".equalsIgnoreCase(fieldName) || "mandatory".equalsIgnoreCase(fieldName) || "frequencyType".equalsIgnoreCase(fieldName) || "frequency".equalsIgnoreCase(fieldName)){
			String[] customFieldDropDownOptions = null;
			if("dataType".equalsIgnoreCase(fieldName)){
				customFieldDropDownOptions = IDPAConstants.CUSTOM_FIELDS_DATA_TYPE;
			}else if("controlType".equalsIgnoreCase(fieldName)){
				customFieldDropDownOptions = IDPAConstants.CUSTOM_FIELDS_CONTROL_TYPE;
			}else if("mandatory".equalsIgnoreCase(fieldName)){
				customFieldDropDownOptions = IDPAConstants.CUSTOM_FIELDS_MANDATORY;
			}else if("frequencyType".equalsIgnoreCase(fieldName)){
				customFieldDropDownOptions = IDPAConstants.CUSTOM_FIELDS_FREQUENCY_TYPE;
			}else if("frequency".equalsIgnoreCase(fieldName)){
				customFieldDropDownOptions = IDPAConstants.CUSTOM_FIELDS_FREQUENCY;
			}
			
			if(customFieldDropDownOptions != null && customFieldDropDownOptions.length > 0){
				for(String customFieldOption : customFieldDropDownOptions){
					HashMap<String, String> customFieldDropDownOption = new HashMap<String, String>();
					customFieldDropDownOption.put("id", customFieldOption);
					customFieldDropDownOption.put("Value", customFieldOption);
					customFieldDropDownOption.put("value", customFieldOption);
					customFieldDropDownOption.put("label", customFieldOption);
					customFieldDropDownOption.put("DisplayText", customFieldOption);
					customFieldDropDownOption.put("Number", "");
					customFieldConfigurationDropDownOptions.add(customFieldDropDownOption);
				}
			}
		}else if("fieldGroup".equalsIgnoreCase(fieldName)){
			List<CustomFieldGroupMaster> customFieldGroupMasters = getCustomFieldGroups();
			if(customFieldGroupMasters != null && customFieldGroupMasters.size() > 0){
				for(CustomFieldGroupMaster customFieldGroupMaster : customFieldGroupMasters){
					HashMap<String, String> customFieldDropDownOption = new HashMap<String, String>();
					customFieldDropDownOption.put("id", customFieldGroupMaster.getId()+"");
					customFieldDropDownOption.put("Value", customFieldGroupMaster.getId()+"");
					customFieldDropDownOption.put("value", customFieldGroupMaster.getId()+"");
					customFieldDropDownOption.put("label", customFieldGroupMaster.getGroupName());
					customFieldDropDownOption.put("DisplayText", customFieldGroupMaster.getGroupName());
					customFieldDropDownOption.put("Number", "");
					customFieldConfigurationDropDownOptions.add(customFieldDropDownOption);
				}
			}
		}
		
		return customFieldConfigurationDropDownOptions;
	}
	
	@Override
	@Transactional
	public List<CustomFieldGroupMaster> getCustomFieldGroups() {
		return customFieldDAO.getFieldGroups();
	}

	@Override
	@Transactional
	public CustomFieldConfigMaster getCustomFieldById(Integer customFieldId, Integer isActive) {
		return customFieldDAO.getCustomFieldById(customFieldId, isActive);
	}
	
	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldExistForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		return customFieldDAO.getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, isCustomFieldConfiguration);
	}

	@Override
	@Transactional
	public List<HashMap<String, Object>> getCustomFieldValuesExistForInstance(Integer entityId, Integer entityTypeId, Integer entityInstanceId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, Date modifiedAfterDate) {
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		try{
			Boolean isTypeCheckNeeded = false;
			List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, false);
			List<CustomFieldValues> customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForInstance(entityId, entityInstanceId, frequencyType, frequency, frequencyMonth, frequencyYear, isActive);
			if("Single".equalsIgnoreCase(frequencyType)){
				getCustomValuesForSingleFrequency(customFieldConfigMasters, customFieldValuesList, customFields, modifiedAfterDate);
			}else{
				getCustomValueForSeriesFrequency(customFieldConfigMasters, frequency, frequencyMonth, frequencyYear, customFieldValuesList, customFields, modifiedAfterDate);
			}
        	
		}catch(Exception ex){
			log.error("Error in getCustomFieldsExistForInstance - ", ex);
		}
		return customFields;
	}
	
	private void getCustomValueForSeriesFrequency(List<CustomFieldConfigMaster> customFieldConfigMasters, String frequency, Integer frequencyMonth, Integer frequencyYear, List<CustomFieldValues> customFieldValuesList, List<HashMap<String, Object>> customFields, Date modifiedAfterDate){
		Integer fieldCount = 0;
		if("Daily".equalsIgnoreCase(frequency) || "Weekly".equalsIgnoreCase(frequency)){
			fieldCount = commonService.getColumnByFrequency(frequency, frequencyMonth, frequencyYear).size();
		}else if("Monthly".equalsIgnoreCase(frequency)){
			fieldCount = 12;
		}else if("Quaterly".equalsIgnoreCase(frequency)){
			fieldCount = 4;
		}else if("Half-yearly".equalsIgnoreCase(frequency)){
			fieldCount = 4;
		}else if("Annual".equalsIgnoreCase(frequency)){
			fieldCount = 5;
		}
		
		HashMap<Integer, HashMap<String, Object>> customFieldsMap = new HashMap<Integer, HashMap<String, Object>>();
		if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
			for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
				HashMap<String, Object> customField = new HashMap<String, Object>();
				customField.put("fieldName", customFieldConfigMaster.getFieldName());
				customField.put("fieldId", customFieldConfigMaster.getId());
				customField.put("fieldDescription", customFieldConfigMaster.getDescription());
				customField.put("fieldType", customFieldConfigMaster.getFrequencyType());
				customField.put("fieldFrequency", customFieldConfigMaster.getFrequency());
				customField.put("fieldDisplayOrder", customFieldConfigMaster.getDisplayOrder());
				customField.put("frequency", frequency);
				customField.put("frequencyMonth", frequencyMonth);
				customField.put("frequencyYear", frequencyYear);
				customField.put("defaultValue", customFieldConfigMaster.getDefaultValue());
				customField.put("entityTypeId", customFieldConfigMaster.getEntityType());
				for(int i = 1; i <= fieldCount; i++){
					customField.put(frequency+i, "--");
					customField.put(frequency+i+"Id", 0);
					customField.put("frequencyOrder", i);
				}
				customField.put("fieldDataType", customFieldConfigMaster.getDataType());
				customField.put("fieldControlType", customFieldConfigMaster.getControlType());
				if(customFieldConfigMaster.getFieldOptions() != null && !customFieldConfigMaster.getFieldOptions().trim().isEmpty()){
					List<HashMap<String, String>> fieldOptions = new ArrayList<HashMap<String, String>>();
					String[] options = customFieldConfigMaster.getFieldOptions().split(";");
					for(String option : options){
						HashMap<String, String> optionDetail = new HashMap<String, String>();
						optionDetail.put("Value", option.trim());
						optionDetail.put("value", option.trim());
						optionDetail.put("DisplayText", option.trim());
						if(option.contains("~~")){
							optionDetail.put("Value", option.split("~~")[0].trim());
							optionDetail.put("value", option.split("~~")[0].trim());
							optionDetail.put("DisplayText", option.split("~~")[1].trim());
						}
						fieldOptions.add(optionDetail);
					}
					customField.put("fieldOptions", fieldOptions);
				}else{
					customField.put("fieldOptions", "");
				}
				customField.put("fieldOptionsURL", customFieldConfigMaster.getFieldOptionUrl());
				customField.put("dependsOn", customFieldConfigMaster.getDependsOn());
				customField.put("isModified", false);
				customFieldsMap.put(customFieldConfigMaster.getId(), customField);
				customFields.add(customField);
			}
		}
		
		if(customFieldValuesList != null && customFieldValuesList.size() > 0){
			for(CustomFieldValues customFieldValues : customFieldValuesList){
				if(customFieldsMap.containsKey(customFieldValues.getCustomFieldId().getId())){
					HashMap<String, Object> customField = customFieldsMap.get(customFieldValues.getCustomFieldId().getId());
					customField.put(frequency+customFieldValues.getFrequencyOrder(), customFieldValues.getFieldValue());
					customField.put(frequency+customFieldValues.getFrequencyOrder()+"Id", customFieldValues.getId());
					customField.put("entityInstanceId", customFieldValues.getEntityInstanceId());
					if(commonService.isModifiedAfterDate(customFieldValues.getModifiedOn(), modifiedAfterDate)){
						customField.put("isModified", true);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getCustomValuesForSingleFrequency(List<CustomFieldConfigMaster> customFieldConfigMasters, List<CustomFieldValues> customFieldValuesList, List<HashMap<String, Object>> customFields, Date modifiedAfterDate){
		HashMap<String, Object> customFieldsObject = new HashMap<String, Object>();
		List<HashMap<String, Object>> fieldGroup = new ArrayList<HashMap<String, Object>>();
		HashMap<Integer, HashMap<String, Object>> fieldGroupCollection = new HashMap<Integer, HashMap<String, Object>>();
		
		if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
			for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
				Integer fieldGroupId = customFieldConfigMaster.getFieldGroup().getId();
				HashMap<String, Object> fieldGroupObject = null;
				if(fieldGroupCollection.containsKey(fieldGroupId)){
					fieldGroupObject = fieldGroupCollection.get(fieldGroupId);
				}else{
					fieldGroupObject = new HashMap<String, Object>();
					fieldGroupObject.put("groupName", customFieldConfigMaster.getFieldGroup().getGroupName());
					fieldGroupObject.put("groupId", customFieldConfigMaster.getFieldGroup().getId());
					fieldGroupObject.put("groupOrder", customFieldConfigMaster.getFieldGroup().getDisplayOrder());
					fieldGroupObject.put("groupDescription", customFieldConfigMaster.getFieldGroup().getDescription());
					fieldGroupObject.put("groupFields", new ArrayList<HashMap<String, Object>>());
					fieldGroupCollection.put(fieldGroupId, fieldGroupObject);
					fieldGroup.add(fieldGroupObject);
				}
				
				HashMap<String, Object> groupFieldObject = new HashMap<String, Object>();
				groupFieldObject.put("customFieldName", customFieldConfigMaster.getFieldName());
				groupFieldObject.put("customFieldId", customFieldConfigMaster.getId());
				groupFieldObject.put("customFieldDescription", customFieldConfigMaster.getDescription());
				groupFieldObject.put("fieldType", customFieldConfigMaster.getFrequencyType());
				groupFieldObject.put("fieldFrequency", customFieldConfigMaster.getFrequency());
				groupFieldObject.put("fieldDisplayOrder", customFieldConfigMaster.getDisplayOrder());
				groupFieldObject.put("fieldValue", "");
				groupFieldObject.put("fieldValueId", "0");
				groupFieldObject.put("fieldDataType", customFieldConfigMaster.getDataType());
				groupFieldObject.put("fieldControlType", customFieldConfigMaster.getControlType());
				groupFieldObject.put("defaultValue", customFieldConfigMaster.getDefaultValue());
				groupFieldObject.put("entityTypeId", customFieldConfigMaster.getEntityType());
				if(customFieldConfigMaster.getFieldOptions() != null && !customFieldConfigMaster.getFieldOptions().trim().isEmpty()){
					List<HashMap<String, String>> fieldOptions = new ArrayList<HashMap<String, String>>();
					String[] options = customFieldConfigMaster.getFieldOptions().split(";");
					for(String option : options){
						HashMap<String, String> optionDetail = new HashMap<String, String>();
						optionDetail.put("Value", option.trim());
						optionDetail.put("value", option.trim());
						optionDetail.put("DisplayText", option.trim());
						if(option.contains("~~")){
							optionDetail.put("Value", option.split("~~")[0].trim());
							optionDetail.put("value", option.split("~~")[0].trim());
							optionDetail.put("DisplayText", option.split("~~")[1].trim());
						}
						fieldOptions.add(optionDetail);
					}
					groupFieldObject.put("fieldOptions", fieldOptions);
				}else{
					groupFieldObject.put("fieldOptions", "");
				}
				if(customFieldConfigMaster.getFieldOptionUrl() != null){
					groupFieldObject.put("fieldOptionsURL", customFieldConfigMaster.getFieldOptionUrl());
				}else{
					groupFieldObject.put("fieldOptionsURL", "");
				}
				groupFieldObject.put("dependsOn", customFieldConfigMaster.getDependsOn());
				groupFieldObject.put("isModified", false);
				((ArrayList<HashMap<String, Object>>)fieldGroupObject.get("groupFields")).add(groupFieldObject);
			}
		}
		
		if(customFieldValuesList != null && customFieldValuesList.size() > 0){
			for(CustomFieldValues customFieldValues : customFieldValuesList){
				
				CustomFieldConfigMaster customFieldConfigMaster = customFieldValues.getCustomFieldId();
				Integer fieldGroupId = customFieldConfigMaster.getFieldGroup().getId();
				HashMap<String, Object> fieldGroupObject = fieldGroupCollection.get(fieldGroupId);
				if(fieldGroupObject != null && !fieldGroupObject.isEmpty() && fieldGroupObject.containsKey("groupFields")){
					ArrayList<HashMap<String, Object>> fieldGroups = (ArrayList<HashMap<String, Object>>) fieldGroupObject.get("groupFields");
					if(fieldGroups != null && fieldGroups.size() > 0){
						for(HashMap<String, Object> groupFieldObject : fieldGroups){
							if(groupFieldObject.containsKey("customFieldId") && customFieldValues.getCustomFieldId().getId() == (Integer)groupFieldObject.get("customFieldId")){
								groupFieldObject.put("fieldValue", customFieldValues.getFieldValue());
								groupFieldObject.put("id", customFieldValues.getId());
								groupFieldObject.put("fieldValueId", customFieldValues.getId());
								groupFieldObject.put("entityInstanceId", customFieldValues.getEntityInstanceId());
								if(commonService.isModifiedAfterDate(customFieldValues.getModifiedOn(), modifiedAfterDate)){
									groupFieldObject.put("isModified", true);
								}
								if(customFieldValues.getCreatedBy() != null){
									groupFieldObject.put("createdById", customFieldValues.getCreatedBy().getUserId());
								}
								if(customFieldValues.getCreatedOn() != null){
									groupFieldObject.put("createdOn", DateUtility.dateToStringWithSeconds1(customFieldValues.getCreatedOn()));
								}
								
								break;
							}
						}
					}
				}
			}
		}
		customFieldsObject.put("fieldGroup", fieldGroup);
		customFields.add(customFieldsObject);
	}
	
	@Override
	@Transactional
	public List<HashMap<String, Object>> getCustomFieldValuesExistForAllInstanceOfEntity(Integer entityId, Integer entityTypeId, Integer entityParentInstanceId, Integer engagementId, Integer productId, Integer isActive, Date modifiedAfterDate) {
		Long startTime = System.currentTimeMillis();
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		try{
			List<Integer> instanceIds = new ArrayList<Integer>();
			HashMap<Integer, HashMap<String, Object>> instanceDetailsOfEntity = getInstanceDetailsOfEntity(entityId, entityTypeId, entityParentInstanceId, engagementId, productId, instanceIds, customFields, modifiedAfterDate);
			List<CustomFieldValues> customFieldValuesList = new ArrayList<CustomFieldValues>(); 
			if(instanceIds != null && instanceIds.size() > 0){
				customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForAllInstanceOfEntity(entityId, instanceIds, "Single", isActive);
				if(customFieldValuesList != null && customFieldValuesList.size() > 0){
					for(CustomFieldValues customFieldValues : customFieldValuesList){
						CustomFieldConfigMaster customFieldConfigMaster = customFieldValues.getCustomFieldId();
						Integer entityInstanceId = customFieldValues.getEntityInstanceId();
						if(instanceDetailsOfEntity.containsKey(entityInstanceId)){
							instanceDetailsOfEntity.get(entityInstanceId).put(customFieldConfigMaster.getFieldName(), customFieldValues.getFieldValue());
							instanceDetailsOfEntity.get(entityInstanceId).put("idsOf"+customFieldConfigMaster.getFieldName(), customFieldConfigMaster.getId()+"-"+customFieldValues.getId());
							if(commonService.isModifiedAfterDate(customFieldValues.getModifiedOn(), modifiedAfterDate)){
								instanceDetailsOfEntity.get(entityInstanceId).put("isModifiedOf"+customFieldConfigMaster.getFieldName(), true);
								instanceDetailsOfEntity.get(entityInstanceId).put("isModified", true);
							}
						}
					}
				}
			}
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldsExistForAllInstanceOfEntity - ", ex);
		}
		log.debug("Method exewcution time - "+(System.currentTimeMillis() - startTime));
		return customFields;
	}
	
	private HashMap<Integer, HashMap<String, Object>> getInstanceDetailsOfEntity(Integer entityId, Integer entityTypeId, Integer entityParentInstanceId, Integer engagementId, Integer productId, List<Integer> instanceIds, List<HashMap<String, Object>> customFields, Date modifiedAfterDate){
		HashMap<Integer, HashMap<String, Object>> instanceDetails = new HashMap<Integer, HashMap<String, Object>>();
		if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
			String parentName = IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE;
			if(entityParentInstanceId == null || entityParentInstanceId == 0){
				if(productId != null && productId > 0){
					parentName = IDPAConstants.ENTITY_PRODUCT;
					entityParentInstanceId = productId;
				}else if(engagementId != null && engagementId > 0){
					parentName = IDPAConstants.ENTITY_TEST_FACTORY_ENGAGEMENT;
					entityParentInstanceId = engagementId;
				}
			}
			List<Activity> activities = activityService.getActivityByParents(entityParentInstanceId, parentName);
			if(activities != null && activities.size() > 0){
				for(Activity activity : activities){
					if(activity.getActivityMaster() != null && activity.getActivityMaster().getActivityMasterId().equals(entityTypeId)){
						instanceIds.add(activity.getActivityId());
						HashMap<String, Object> instanceDetail = new HashMap<String, Object>();
						instanceDetail.put("Id", activity.getActivityId());
						instanceDetail.put("Name", activity.getActivityName());
						instanceDetail.put("Activity Type", "--");
						instanceDetail.put("Status", "--");
						instanceDetail.put("Assignee", "--");
						instanceDetail.put("Reviewer", "--");
						instanceDetail.put("isModified", false);
						if(commonService.isModifiedAfterDate(activity.getModifiedDate(), modifiedAfterDate)){
							instanceDetail.put("isModified", true);
						}
						if(activity.getActivityMaster() != null){
							instanceDetail.put("Activity Type", activity.getActivityMaster().getActivityMasterName());
						}
						if(activity.getWorkflowStatus() != null){
							instanceDetail.put("Status", activity.getWorkflowStatus().getWorkflowStatusName());
						}
						if(activity.getAssignee() != null){
							instanceDetail.put("Assignee", activity.getAssignee().getLoginId());
						}
						if(activity.getReviewer() != null){
							instanceDetail.put("Reviewer", activity.getReviewer().getLoginId());
						}
						instanceDetails.put(activity.getActivityId(), instanceDetail);
						customFields.add(instanceDetail);
					}
				}
			}
		}
		if(instanceDetails != null && instanceDetails.size() > 0){
			Boolean isTypeCheckNeeded = true;
			List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, "Single", null, 1, isTypeCheckNeeded, false);
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					for(Map.Entry<Integer, HashMap<String, Object>> instanceDetail : instanceDetails.entrySet()){
						HashMap<String, Object> instanceFieldDetail = instanceDetail.getValue();
						instanceFieldDetail.put(customFieldConfigMaster.getFieldName(), "--");
						instanceFieldDetail.put("idsOf"+customFieldConfigMaster.getFieldName(), customFieldConfigMaster.getId()+"-0");
						instanceFieldDetail.put("isModifiedOf"+customFieldConfigMaster.getFieldName(), false);
					}
				}
			}
		}
		return instanceDetails;
	}

	@Override
	@Transactional
	public String validatePropertiesOfCustomField(JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, CustomFieldService customFieldService) {
		String errorMessage = "";
		try{
			
			if(jsonCustomFieldConfigMaster.getEntityId() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
				if(jsonCustomFieldConfigMaster.getEntityTypeId() == null || jsonCustomFieldConfigMaster.getEntityTypeId() <= 0){
					errorMessage = "Please select Entity Type for this Entity to create custom field";
					return errorMessage;
				}
			}else{
				jsonCustomFieldConfigMaster.setEntityTypeId(null);
			}

			if(jsonCustomFieldConfigMaster.getFieldName() == null || jsonCustomFieldConfigMaster.getFieldName().trim().isEmpty()){
				errorMessage = "Please provide valid custom field name";
				return errorMessage;
			}
			
			if("Yes".equalsIgnoreCase(jsonCustomFieldConfigMaster.getIsMandatory())){
				if(jsonCustomFieldConfigMaster.getDefaultValue() == null || jsonCustomFieldConfigMaster.getDefaultValue().trim().isEmpty()){
					errorMessage = "Please provide default value";
					return errorMessage;
				}
				
			}
			
			if(jsonCustomFieldConfigMaster.getDefaultValue() != null && !jsonCustomFieldConfigMaster.getDefaultValue().trim().isEmpty()){
				errorMessage = validateCustomFieldValueForDataType(jsonCustomFieldConfigMaster.getDataType(), jsonCustomFieldConfigMaster.getDefaultValue());
				if(errorMessage != null && !errorMessage.trim().isEmpty()){
					return "Default Value : " + errorMessage;
				}
			}
			
			String dataType = jsonCustomFieldConfigMaster.getDataType();
			if(dataType != null && ("Integer".equalsIgnoreCase(dataType) || "Decimal".equalsIgnoreCase(dataType) || "Date".equalsIgnoreCase(dataType))){
				if(jsonCustomFieldConfigMaster.getUpperLimit() != null && !jsonCustomFieldConfigMaster.getUpperLimit().trim().isEmpty()){
					errorMessage = validateCustomFieldValueForDataType(jsonCustomFieldConfigMaster.getDataType(), jsonCustomFieldConfigMaster.getUpperLimit());
					if(errorMessage != null && !errorMessage.trim().isEmpty()){
						return "Upper Limit : " + errorMessage;
					}
				}
				if(jsonCustomFieldConfigMaster.getLowerLimit() != null && !jsonCustomFieldConfigMaster.getLowerLimit().trim().isEmpty()){
					errorMessage = validateCustomFieldValueForDataType(jsonCustomFieldConfigMaster.getDataType(), jsonCustomFieldConfigMaster.getLowerLimit());
					if(errorMessage != null && !errorMessage.trim().isEmpty()){
						return "Lower Limit : " + errorMessage;
					}
				}
				
				if(jsonCustomFieldConfigMaster.getUpperLimit() != null && !jsonCustomFieldConfigMaster.getUpperLimit().trim().isEmpty() && jsonCustomFieldConfigMaster.getLowerLimit() != null && !jsonCustomFieldConfigMaster.getLowerLimit().trim().isEmpty()){
					if(dataType != null && ("Integer".equalsIgnoreCase(dataType))){
						if(Integer.parseInt(jsonCustomFieldConfigMaster.getUpperLimit()) < Integer.parseInt(jsonCustomFieldConfigMaster.getLowerLimit())){
							errorMessage = "Upper Limit should be greater than or equal to Lower Limit";
							return errorMessage;
						}
					}else if("Decimal".equalsIgnoreCase(dataType)){
						if(Double.parseDouble(jsonCustomFieldConfigMaster.getUpperLimit()) < Double.parseDouble(jsonCustomFieldConfigMaster.getLowerLimit())){
							errorMessage = "Upper Limit should be greater than or equal to Lower Limit";
							return errorMessage;
						}
					}else if("Date".equalsIgnoreCase(dataType)){
						Date UpperLimitDate = DateUtility.getDateFromString(jsonCustomFieldConfigMaster.getUpperLimit());
						Date lowerLimitDate = DateUtility.getDateFromString(jsonCustomFieldConfigMaster.getLowerLimit());
						if(UpperLimitDate.before(lowerLimitDate)){
							errorMessage = "Upper Limit should be greater than or equal to Lower Limit";
							return errorMessage;
						}
					}
				}
				
				errorMessage = validateCustomFieldValueForDataRange(dataType, jsonCustomFieldConfigMaster.getDefaultValue(), jsonCustomFieldConfigMaster.getUpperLimit(), jsonCustomFieldConfigMaster.getLowerLimit());
				if(errorMessage != null && !errorMessage.trim().isEmpty()){
					return "Default Value : " + errorMessage;
				}
			}
			
			String controlType = jsonCustomFieldConfigMaster.getControlType();
			if(("Select".equalsIgnoreCase(controlType) || "Radio Button".equalsIgnoreCase(controlType) || "Check Box".equalsIgnoreCase(controlType))){
				String fieldOptions = jsonCustomFieldConfigMaster.getFieldOptions();
				String fieldOptionUrl = jsonCustomFieldConfigMaster.getFieldOptionUrl();
				if((fieldOptions == null || fieldOptions.trim().isEmpty()) && (fieldOptionUrl == null || fieldOptionUrl.trim().isEmpty())){
					errorMessage = "Please provide values for options";
					return errorMessage;
				}
			}
			
			errorMessage = ValidationUtility.validateForCustomFieldAdditionOrUpdation(jsonCustomFieldConfigMaster, customFieldService, 1);
			return errorMessage;
		}catch(Exception ex){
			log.error("Error in validatePropertiesOfCustomField - ", ex);
		}
		return errorMessage;
	}
	
	@Override
	@Transactional
	public Boolean isCustomFieldAreadyExistWithProperties(Integer fieldId, String fieldName, Integer entityId, Integer entityTypeId, Integer parentEntityId, Integer parentEntityInstanceId, Integer isActive) {
		return customFieldDAO.isCustomFieldAreadyExistWithProperties(fieldId, fieldName, entityId, entityTypeId, parentEntityId, parentEntityInstanceId, isActive);
	}
	

	@Override
	@Transactional
	public Object customFieldsSaveOrUpdateForEntity(JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, CustomFieldService customFieldService, UserList user) {
		CustomFieldConfigMaster customFieldConfigMaster = jsonCustomFieldConfigMaster.getCustomFieldConfigMaster();
		try{
			String errorMessage = customFieldService.validatePropertiesOfCustomField(jsonCustomFieldConfigMaster, customFieldService);
			if (errorMessage != null && !errorMessage.trim().isEmpty()) {
				return errorMessage;
			}
			
			if(customFieldConfigMaster.getId() == null || customFieldConfigMaster.getId() <= 0){
				customFieldConfigMaster.setCreatedBy(user);
				customFieldConfigMaster.setCreatedOn(new Date());
			}
			customFieldConfigMaster.setModifiedBy(user);
			customFieldConfigMaster.setModifiedOn(new Date());
			
			customFieldService.saveOrUpdateCustomField(customFieldConfigMaster);
			if(customFieldConfigMaster != null && customFieldConfigMaster.getId() != null){				
				if(jsonCustomFieldConfigMaster.getId() != null && jsonCustomFieldConfigMaster.getId() > 0){
					String remarks = "";
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CUSTOM_FIELD_CONFIG, customFieldConfigMaster.getId(), customFieldConfigMaster.getFieldName(),
							jsonCustomFieldConfigMaster.getModifiedField(), jsonCustomFieldConfigMaster.getModifiedFieldTitle(),
							jsonCustomFieldConfigMaster.getOldFieldValue(), jsonCustomFieldConfigMaster.getModifiedFieldValue(), user, remarks);
				}else{
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_CUSTOM_FIELD_CONFIG, customFieldConfigMaster.getId(), customFieldConfigMaster.getFieldName(), user);
				}
				
			}
		}catch(Exception ex){
			log.error("Error occured in customFieldsSaveOrUpdateForEntity - ", ex);
		}
		return customFieldConfigMaster;
	}
	
	@Override
	@Transactional
	public void saveOrUpdateCustomField(CustomFieldConfigMaster customFieldConfigMaster) {
		customFieldDAO.saveOrUpdateCustomField(customFieldConfigMaster);
	}
	

	@Override
	@Transactional
	public Boolean isCustomFieldHasValueForInstance(Integer customFieldId) {
		return customFieldDAO.isCustomFieldHasValueForInstance(customFieldId);
	}

	@Override
	@Transactional
	public void deleteCustomField(CustomFieldConfigMaster customFieldConfigMaster) {
		customFieldDAO.deleteCustomField(customFieldConfigMaster);
	}
	
	@Override
	@Transactional
	public String validateCustomFieldValueForDataType(String dataType, String fieldValue) {
		String errorMessage = "";
		try{
			if("Integer".equalsIgnoreCase(dataType)){
				try{
					Integer.parseInt(fieldValue);
				}catch(Exception ex){
					errorMessage = "Invalid value for Integer datatype";
					return errorMessage;
				}
				
			}else if("Decimal".equalsIgnoreCase(dataType)){
				try{
					Double.parseDouble(fieldValue);
				}catch(Exception ex){
					errorMessage = "Invalid value for Decimal datatype";
					return errorMessage;
				}
				
			}else if("Boolean".equalsIgnoreCase(dataType)){
				try{
					Boolean.parseBoolean(fieldValue);
				}catch(Exception ex){
					errorMessage = "Invalid value for Boolean datatype";
					return errorMessage;
				}
			}else if("Date".equalsIgnoreCase(dataType)){
				Date convertedDate = DateUtility.getDateFromString(fieldValue);
				if(convertedDate == null){
					errorMessage = "Invalid value for Date datatype";
					return errorMessage;
				}
			}
		}catch(Exception ex){
			log.error("Error in validateCustomFieldValue - ", ex);
			errorMessage = "Error while processing the value, Value may out of range for the type";
		}
		return errorMessage;
	}

	
	@Override
	@Transactional
	public String validateCustomFieldValueForDataRange(String dataType, String value, String upperLimit, String lowerLimit){
		String errorMessage = "";
		try{
			if(dataType != null && ("Integer".equalsIgnoreCase(dataType) || "Decimal".equalsIgnoreCase(dataType) || "Date".equalsIgnoreCase(dataType))){
				if(value != null && !value.trim().isEmpty()){
					if(dataType != null && ("Integer".equalsIgnoreCase(dataType))){
						if(upperLimit != null && !upperLimit.trim().isEmpty() && Integer.parseInt(upperLimit) < Integer.parseInt(value)){
							errorMessage = "Value is higher than Upper Limit";
							return errorMessage;
						}
						if(lowerLimit != null && !lowerLimit.trim().isEmpty() && Integer.parseInt(lowerLimit) > Integer.parseInt(value)){
							errorMessage = "Value is lesser than Lower Limit";
							return errorMessage;
						}
					}else if("Decimal".equalsIgnoreCase(dataType)){
						if(upperLimit != null && !upperLimit.trim().isEmpty() && Double.parseDouble(upperLimit) < Double.parseDouble(value)){
							errorMessage = "Value is higher than Upper Limit";
							return errorMessage;
						}
						if(lowerLimit != null && !lowerLimit.trim().isEmpty() && Double.parseDouble(lowerLimit) > Double.parseDouble(value)){
							errorMessage = "Value is lesser than Lower Limit";
							return errorMessage;
						}
					}else if("Date".equalsIgnoreCase(dataType)){
						Date fieldValueDate = DateUtility.getDateFromString(value);
						Date upperLimitDate = DateUtility.getDateFromString(upperLimit);
						Date lowerLimitDate = DateUtility.getDateFromString(lowerLimit);
						if(upperLimitDate != null && upperLimitDate.before(fieldValueDate)){
							errorMessage = "Date is higher than Upper Limit Date";
							return errorMessage;
						}
						if(lowerLimitDate != null && lowerLimitDate.after(fieldValueDate)){
							errorMessage = "Date is lesser than Lower Limit Date";
							return errorMessage;
						}
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in validateCustomFieldValueForDataRange - ", ex);
		}
		return errorMessage;
	}
	
	@Override
	@Transactional
	public Object convertCustomFieldDataType(String customFieldValue, String dataType){
		try{
			if(customFieldValue != null && !customFieldValue.trim().isEmpty() && dataType != null && !dataType.trim().isEmpty()){
				if("Integer".equalsIgnoreCase(dataType)){
					try{
						return Integer.parseInt(customFieldValue);
					}catch(Exception ex){
						log.error("Error while converting value "+customFieldValue+" to Integer datatype - "+ex.getMessage());
					}
					
				}else if("Decimal".equalsIgnoreCase(dataType)){
					try{
						return Double.parseDouble(customFieldValue);
					}catch(Exception ex){
						log.error("Error while converting value "+customFieldValue+" to Decimal datatype - "+ex.getMessage());
					}
					
				}else if("Boolean".equalsIgnoreCase(dataType)){
					try{
						return Boolean.parseBoolean(customFieldValue);
					}catch(Exception ex){
						log.error("Error while converting value "+customFieldValue+" to Boolean datatype - "+ex.getMessage());
					}
				}else if("Date".equalsIgnoreCase(dataType)){
					Date convertedDate = DateUtility.getDateFromString(customFieldValue);
					if(convertedDate == null){
						log.error("Error while converting value "+customFieldValue+" to Date datatype");
					}else{
						return convertedDate;
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in convertCustomFieldDataType - ", ex);
		}
		
		return customFieldValue;
	}
	
	@Override
	@Transactional
	public void saveOrUpdateCustomFieldValue(CustomFieldValues customFieldValues) {
		customFieldDAO.saveOrUpdateCustomFieldValue(customFieldValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<HashMap<String, Object>> getCustomFieldsIndividualDetail(Integer customFieldValueId, Integer customFieldId, Integer frequencyOrder, Integer frequencyMonth, Integer frequencyYear, Integer isActive) {
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> customFieldsObject = new HashMap<String, Object>();
		List<HashMap<String, Object>> fieldGroup = new ArrayList<HashMap<String, Object>>();
		try{
			CustomFieldConfigMaster customFieldConfigMaster = null;
			CustomFieldValues customFieldValues = null;
			
			if(customFieldValueId != null && customFieldValueId > 0){
				customFieldValues = customFieldDAO.getCustomFieldValuesById(customFieldValueId);
				if(customFieldValues != null){
					customFieldConfigMaster = customFieldValues.getCustomFieldId();
				}
			}else if(customFieldId != null && customFieldId > 0){
				customFieldConfigMaster = customFieldDAO.getCustomFieldById(customFieldId, isActive);
			}
			
			if(customFieldConfigMaster != null){
				HashMap<String, Object> fieldGroupObject = new HashMap<String, Object>();
				fieldGroupObject.put("groupName", customFieldConfigMaster.getFieldGroup().getGroupName());
				fieldGroupObject.put("groupId", customFieldConfigMaster.getFieldGroup().getId());
				fieldGroupObject.put("groupOrder", customFieldConfigMaster.getFieldGroup().getDisplayOrder());
				fieldGroupObject.put("groupDescription", customFieldConfigMaster.getFieldGroup().getDescription());
				fieldGroupObject.put("groupFields", new ArrayList<HashMap<String, Object>>());
				fieldGroup.add(fieldGroupObject);
				
				HashMap<String, Object> groupFieldObject = new HashMap<String, Object>();
				groupFieldObject.put("customFieldName", customFieldConfigMaster.getFieldName());
				groupFieldObject.put("customFieldId", customFieldConfigMaster.getId());
				groupFieldObject.put("customFieldDescription", customFieldConfigMaster.getDescription());
				groupFieldObject.put("fieldType", customFieldConfigMaster.getFrequencyType());
				groupFieldObject.put("fieldFrequency", customFieldConfigMaster.getFrequency());
				groupFieldObject.put("fieldDisplayOrder", customFieldConfigMaster.getDisplayOrder());
				if(customFieldValues != null){
					groupFieldObject.put("fieldValue", customFieldValues.getFieldValue());
					groupFieldObject.put("fieldValueId", customFieldValues.getId());
					groupFieldObject.put("entityInstanceId", customFieldValues.getEntityInstanceId());
					if(customFieldValues.getCreatedBy() != null){
						groupFieldObject.put("createdById", customFieldValues.getCreatedBy().getUserId());
					}
					if(customFieldValues.getCreatedOn() != null){
						groupFieldObject.put("createdOn", DateUtility.dateToStringWithSeconds1(customFieldValues.getCreatedOn()));
					}
				}else{
					groupFieldObject.put("fieldValue", "");
					groupFieldObject.put("fieldValueId", "0");
				}
				
				groupFieldObject.put("frequencyOrder", frequencyOrder);
				groupFieldObject.put("frequencyMonth", frequencyMonth);
				groupFieldObject.put("frequencyYear", frequencyYear);
				
				groupFieldObject.put("fieldDataType", customFieldConfigMaster.getDataType());
				groupFieldObject.put("fieldControlType", customFieldConfigMaster.getControlType());
				if(customFieldConfigMaster.getFieldOptions() != null && !customFieldConfigMaster.getFieldOptions().trim().isEmpty()){
					List<HashMap<String, String>> fieldOptions = new ArrayList<HashMap<String, String>>();
					String[] options = customFieldConfigMaster.getFieldOptions().split(";");
					for(String option : options){
						HashMap<String, String> optionDetail = new HashMap<String, String>();
						optionDetail.put("Value", option.trim());
						optionDetail.put("value", option.trim());
						optionDetail.put("DisplayText", option.trim());
						if(option.contains("~~")){
							optionDetail.put("Value", option.split("~~")[0].trim());
							optionDetail.put("value", option.split("~~")[0].trim());
							optionDetail.put("DisplayText", option.split("~~")[1].trim());
						}
						fieldOptions.add(optionDetail);
					}
					groupFieldObject.put("fieldOptions", fieldOptions);
				}else{
					groupFieldObject.put("fieldOptions", "");
				}
				if(customFieldConfigMaster.getFieldOptionUrl() != null){
					groupFieldObject.put("fieldOptionsURL", customFieldConfigMaster.getFieldOptionUrl());
				}else{
					groupFieldObject.put("fieldOptionsURL", "");
				}
				groupFieldObject.put("dependsOn", customFieldConfigMaster.getDependsOn());
				
				((ArrayList<HashMap<String, Object>>)fieldGroupObject.get("groupFields")).add(groupFieldObject);
			}
			customFieldsObject.put("fieldGroup", fieldGroup);
			customFields.add(customFieldsObject);
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldsIndividualDetail - ", ex);
		}
		return customFields;
	}


	@Override
	@Transactional
	public void setCustomFieldsDefaultValuesForInstance(Integer entityId, Integer entityTypeId, Integer entityInstanceId, String entityInstanceName, Integer engagementId, Integer productId, UserList user, String collectionName, Boolean isTypeCheckNeeded){
		try{
			String remarks = "";
			List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldsWithDefaultValueForEntity(entityId, entityTypeId, engagementId, productId, "Single", null, 1, isTypeCheckNeeded);
			HashMap<String, Object> customFields = new HashMap<String, Object>();
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				String entityTypeName = "";
				if(entityId == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID || entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
					entityTypeName = IDPAConstants.ENTITY_ACTIVITY;
				}
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					CustomFieldValues customFieldValues = new CustomFieldValues();
					customFieldValues.setCustomFieldId(customFieldConfigMaster);
					customFieldValues.setFieldValue(customFieldConfigMaster.getDefaultValue());
					customFieldValues.setEntityInstanceId(entityInstanceId);
					customFieldValues.setCreatedBy(user);
					customFieldValues.setCreatedOn(new Date());
					customFieldValues.setModifiedBy(user);
					customFieldValues.setModifiedOn(new Date());
					
					saveOrUpdateCustomFieldValue(customFieldValues);
					
					remarks = "CustomField :"+customFieldConfigMaster.getFieldName();
					eventsService.addEntityChangedEvent(entityTypeName, entityInstanceId, "CF-"+customFieldConfigMaster.getFieldName(), entityInstanceName, "CF-"+customFieldConfigMaster.getFieldName(), "", customFieldConfigMaster.getDefaultValue(), user, remarks);
					
					customFields.put(customFieldConfigMaster.getFieldName(), convertCustomFieldDataType(customFieldConfigMaster.getDefaultValue(), customFieldConfigMaster.getDataType()));
				}
			}
			
			if(customFields != null && customFields.size() > 0){
				mongoDBService.addOrUpdateCustomField(entityInstanceId, customFields, collectionName);
			}
		}catch(Exception ex){
			log.error("Error in setCustomFieldsDefaultValuesForInstance - ", ex);
		}
	}
	

	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldsWithDefaultValueForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded) {
		return customFieldDAO.getCustomFieldsWithDefaultValueForEntity(entityId, entityTypeId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded);
	}

	@Override
	@Transactional
	public List<Integer> getInstanceIdsOfCustomFieldValuesModifiedAfter(Integer entityId, Integer engagementId, Integer productId, Date modifiedAfterDate) {
		return customFieldDAO.getInstanceIdsOfCustomFieldValuesModifiedAfter(entityId, engagementId, productId, modifiedAfterDate);
	}
	
	@Override
	@Transactional
	public void cloneCustomFieldValuesOfInstance(Integer entityId, Integer originalInstanceId, Integer clonedInstanceId){
		try{
			List<CustomFieldValues> customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForInstance(entityId, originalInstanceId, null, null, null, null, 1);
			if(customFieldValuesList != null && customFieldValuesList.size() > 0){
				for(CustomFieldValues customFieldValues : customFieldValuesList){
					CustomFieldValues clonedCustomFieldValues = (CustomFieldValues) customFieldValues.clone();
					clonedCustomFieldValues.setId(null);
					clonedCustomFieldValues.setEntityInstanceId(clonedInstanceId);
					customFieldDAO.saveOrUpdateCustomFieldValue(clonedCustomFieldValues);
				}
			}
		}catch(Exception ex){
			log.error("Error in cloneCustomFieldValuesOfInstance - ", ex);
		}
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getValuesOfCustomFieldForInstance(Integer customFieldId, Integer entityInstanceId) {
		return customFieldDAO.getValuesOfCustomFieldForInstance(customFieldId, entityInstanceId);
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getCustomFieldValuesExistForInstanceByFieldNames(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, List<String> fieldNames) {
		return customFieldDAO.getCustomFieldValuesExistForInstanceByFieldNames(entityId, entityInstanceId, frequencyType, frequency, frequencyMonth, frequencyYear, isActive, fieldNames);
	}

	@Override
	@Transactional
	public void saveCustomFieldsListForInstance(List<CustomFieldValues> customFieldValuesList, Integer instanceId) {
		try{
			if(customFieldValuesList != null && customFieldValuesList.size() > 0){
				for(CustomFieldValues customFieldValues : customFieldValuesList){
					customFieldValues.setEntityInstanceId(instanceId);
					saveOrUpdateCustomFieldValue(customFieldValues);
					
					if(customFieldValues != null && customFieldValues.getId() != null){				
						CustomFieldConfigMaster customFieldConfigMaster = customFieldValues.getCustomFieldId();
						String collectionName = "";
						String entityTypeName = "";
						if(customFieldConfigMaster.getEntity().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID || customFieldConfigMaster.getEntity().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
							collectionName = MongodbConstants.ACTIVITY;
							entityTypeName = IDPAConstants.ENTITY_ACTIVITY;
						}
						if(collectionName != null && !collectionName.trim().isEmpty()){
							mongoDBService.addOrUpdateCustomField(customFieldValues.getEntityInstanceId(), customFieldConfigMaster.getFieldName(), customFieldValues.getFieldValue(), collectionName);
						}
						if(entityTypeName != null && !entityTypeName.trim().isEmpty()){
							eventsService.addEntityChangedEvent(entityTypeName, customFieldValues.getEntityInstanceId(), "CF-"+customFieldConfigMaster.getFieldName(),
									customFieldConfigMaster.getFieldName(), "CF-"+customFieldConfigMaster.getFieldName(),
									"", customFieldValues.getFieldValue(), customFieldValues.getCreatedBy(), "Custom filed created");
						}
						
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in saveCustomFieldsListForInstance - ", ex);
		}
	}

	@Override
	public List<CustomFieldConfigMaster>  getCustomFieldExistForEngagementIdAndProductId(
			Integer entityId, Integer engagementId, Integer productId,
			String frequencyType, String frequency, Integer isActive,
			Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		try {
			
			
			 List<CustomFieldConfigMaster> customFieldConfigMasters= customFieldDAO.getCustomFieldExistForEngagementIdAndProductId(entityId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, isCustomFieldConfiguration);
			return  customFieldConfigMasters;
		}catch(Exception e) {
			log.error("Error in getCustomFieldExistForEngagementIdAndProductId",e);
		}
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getCustomFieldValuesExistForEntityTypeBased(
			Integer entityId, Integer entityTypeId,Integer entityInstanceId, Integer engagementId,
			Integer productId, String frequencyType, String frequency,
			Integer frequencyMonth, Integer frequencyYear, int isActive,
			Date modifiedAfterDate) {
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		try{
			Boolean isTypeCheckNeeded = false;
			List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, false);
			List<CustomFieldValues> customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForInstance(entityId, entityInstanceId, frequencyType, frequency, frequencyMonth, frequencyYear, isActive);
			if("Single".equalsIgnoreCase(frequencyType)){
				getCustomValuesForSingleFrequency(customFieldConfigMasters, customFieldValuesList, customFields, modifiedAfterDate);
			}else{
				getCustomValueForSeriesFrequency(customFieldConfigMasters, frequency, frequencyMonth, frequencyYear, customFieldValuesList, customFields, modifiedAfterDate);
			}
        	
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesExistForEntityTypeBased - ", ex);
		}
		return customFields;
	}
	
	
	@Override
	@Transactional
	public List<HashMap<String, Object>> getAllCustomFieldsExistInstance(Integer entityId,Integer entityParentInstanceId, Integer engagementId, Integer productId, Integer isActive, Date modifiedAfterDate) {
		Long startTime = System.currentTimeMillis();
		List<HashMap<String, Object>> customFields = new ArrayList<HashMap<String, Object>>();
		try{
			List<Integer> instanceIds = new ArrayList<Integer>();
			HashMap<Integer, HashMap<String, Object>> instanceDetailsOfEntity = getInstanceDetailsOfEntityWithoutEntityTypeId(entityId, entityParentInstanceId, engagementId, productId, instanceIds, customFields, modifiedAfterDate);
			List<CustomFieldValues> customFieldValuesList = new ArrayList<CustomFieldValues>(); 
			if(instanceIds != null && instanceIds.size() > 0){
				customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForAllInstanceOfEntity(entityId, instanceIds, "Single", isActive);
				if(customFieldValuesList != null && customFieldValuesList.size() > 0){
					for(CustomFieldValues customFieldValues : customFieldValuesList){
						CustomFieldConfigMaster customFieldConfigMaster = customFieldValues.getCustomFieldId();
						Integer entityInstanceId = customFieldValues.getEntityInstanceId();
						if(instanceDetailsOfEntity.containsKey(entityInstanceId)){
							instanceDetailsOfEntity.get(entityInstanceId).put(customFieldConfigMaster.getFieldName(), customFieldValues.getFieldValue());
							instanceDetailsOfEntity.get(entityInstanceId).put("idsOf"+customFieldConfigMaster.getFieldName(), customFieldConfigMaster.getId()+"-"+customFieldValues.getId());
							if(commonService.isModifiedAfterDate(customFieldValues.getModifiedOn(), modifiedAfterDate)){
								instanceDetailsOfEntity.get(entityInstanceId).put("isModifiedOf"+customFieldConfigMaster.getFieldName(), true);
								instanceDetailsOfEntity.get(entityInstanceId).put("isModified", true);
							}
						}
					}
				}
			}
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldsExistForAllInstanceOfEntity - ", ex);
		}
		log.debug("Method exewcution time - "+(System.currentTimeMillis() - startTime));
		return customFields;
	}
	
	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldExistForEntityWithoutEntityTypeId(Integer entityId,Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		return customFieldDAO.getCustomFieldExistForEntityWithoutEntityTypeId(entityId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, isCustomFieldConfiguration);
	}
	
	
	private HashMap<Integer, HashMap<String, Object>> getInstanceDetailsOfEntityWithoutEntityTypeId(Integer entityId,Integer entityParentInstanceId, Integer engagementId, Integer productId, List<Integer> instanceIds, List<HashMap<String, Object>> customFields, Date modifiedAfterDate){
		HashMap<Integer, HashMap<String, Object>> instanceDetails = new HashMap<Integer, HashMap<String, Object>>();
		if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
			String parentName = IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE;
			if(entityParentInstanceId == null || entityParentInstanceId == 0){
				if(productId != null && productId > 0){
					parentName = IDPAConstants.ENTITY_PRODUCT;
					entityParentInstanceId = productId;
				}else if(engagementId != null && engagementId > 0){
					parentName = IDPAConstants.ENTITY_TEST_FACTORY_ENGAGEMENT;
					entityParentInstanceId = engagementId;
				}
			}
			List<Activity> activities = activityService.getActivityByParents(entityParentInstanceId, parentName);
			if(activities != null && activities.size() > 0){
				for(Activity activity : activities){
						instanceIds.add(activity.getActivityId());
						HashMap<String, Object> instanceDetail = new HashMap<String, Object>();
						instanceDetail.put("Id", activity.getActivityId());
						instanceDetail.put("Name", activity.getActivityName());
						instanceDetail.put("Activity Type", "--");
						instanceDetail.put("Status", "--");
						instanceDetail.put("Assignee", "--");
						instanceDetail.put("Reviewer", "--");
						instanceDetail.put("isModified", false);
						if(commonService.isModifiedAfterDate(activity.getModifiedDate(), modifiedAfterDate)){
							instanceDetail.put("isModified", true);
						}
						if(activity.getActivityMaster() != null){
							instanceDetail.put("Activity Type", activity.getActivityMaster().getActivityMasterName());
						}
						if(activity.getWorkflowStatus() != null){
							instanceDetail.put("Status", activity.getWorkflowStatus().getWorkflowStatusName());
						}
						if(activity.getAssignee() != null){
							instanceDetail.put("Assignee", activity.getAssignee().getLoginId());
						}
						if(activity.getReviewer() != null){
							instanceDetail.put("Reviewer", activity.getReviewer().getLoginId());
						}
						instanceDetails.put(activity.getActivityId(), instanceDetail);
						customFields.add(instanceDetail);
				}
			}
		}
		if(instanceDetails != null && instanceDetails.size() > 0){
			Boolean isTypeCheckNeeded = true;
			List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldExistForEntityWithoutEntityTypeId(entityId,engagementId, productId, "Single", null, 1, isTypeCheckNeeded, false);
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				for(CustomFieldConfigMaster customFieldConfigMaster : customFieldConfigMasters){
					for(Map.Entry<Integer, HashMap<String, Object>> instanceDetail : instanceDetails.entrySet()){
						HashMap<String, Object> instanceFieldDetail = instanceDetail.getValue();
						instanceFieldDetail.put(customFieldConfigMaster.getFieldName(), "--");
						instanceFieldDetail.put("idsOf"+customFieldConfigMaster.getFieldName(), customFieldConfigMaster.getId()+"-0");
						instanceFieldDetail.put("isModifiedOf"+customFieldConfigMaster.getFieldName(), false);
					}
				}
			}
		}
		return instanceDetails;
	}
	
	@Override
	public HashMap<String, Integer> getSeriesCustomFieldValuesCount(Integer entityId, Integer entityTypeId,Integer entityInstanceId, Integer engagementId,
			Integer productId, String frequencyType,Integer frequencyMonth, Integer frequencyYear, int isActive,
			Date modifiedAfterDate) {
		HashMap<String, Integer> customFields = new HashMap<String, Integer>();
		try{
			Boolean isTypeCheckNeeded = false;
			String frequencyList[] = {"Daily","Weekly","Monthly","Quaterly","Half-yearly","Annual"};
			for(String frequency:frequencyList) {
				List<CustomFieldConfigMaster> customFieldConfigMasters = getCustomFieldExistForEntity(entityId, entityTypeId, engagementId, productId, frequencyType, frequency, isActive, isTypeCheckNeeded, false);
				if(customFieldConfigMasters !=null && customFieldConfigMasters.size() >0) {
					customFields.put(frequency, customFieldConfigMasters.size());
				} else {
					customFields.put(frequency, 0);
				}
			}
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesExistForEntityTypeBased - ", ex);
		}
		return customFields;
	}

}

