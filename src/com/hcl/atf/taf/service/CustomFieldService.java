package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonCustomFieldConfigMaster;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonTestFactory;


public interface CustomFieldService {

	List<JsonEntityMaster> getCustomFieldEligibleEntityList();
	List<JsonTestFactory> getCustomFieldEngagementList(Integer userId);
	List<JsonProductMaster> getCustomFieldProductOfEngagementList(Integer engagementId, Integer userId);
	List<Map<String, String>> getCustomFieldConfigurationDropDownOptions(String fieldName);
	List<CustomFieldGroupMaster> getCustomFieldGroups();
	CustomFieldConfigMaster getCustomFieldById(Integer customFieldId, Integer isActive);
	List<CustomFieldConfigMaster> getCustomFieldExistForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
	List<HashMap<String, Object>>  getCustomFieldValuesExistForInstance(Integer entityId, Integer entityTypeId, Integer entityInstanceId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, Date modifiedAfterDate);
	List<HashMap<String, Object>> getCustomFieldValuesExistForAllInstanceOfEntity(Integer entityId, Integer entityTypeId, Integer entityParentInstanceId, Integer engagementId, Integer productId, Integer isActive, Date modifiedAfterDate);
	Boolean isCustomFieldAreadyExistWithProperties(Integer fieldId, String fieldName, Integer entityId, Integer entityTypeId, Integer parentEntityId, Integer parentEntityInstanceId, Integer isActive);
	Object customFieldsSaveOrUpdateForEntity(JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, CustomFieldService customFieldService, UserList user);
	void saveOrUpdateCustomField(CustomFieldConfigMaster customFieldConfigMaster);
	Boolean isCustomFieldHasValueForInstance(Integer customFieldId);
	void deleteCustomField(CustomFieldConfigMaster customFieldConfigMaster);
	String validateCustomFieldValueForDataType(String dataType, String fieldValue);
	void saveOrUpdateCustomFieldValue(CustomFieldValues customFieldValues);
	List<HashMap<String, Object>> getCustomFieldsIndividualDetail(Integer customFieldValueId, Integer customFieldId, Integer frequencyOrder, Integer frequencyMonth, Integer frequencyYear, Integer isActive);
	void setCustomFieldsDefaultValuesForInstance(Integer entityId, Integer entityTypeId, Integer entityInstanceId, String entityInstanceName, Integer engagementId, Integer productId, UserList user, String collectionName, Boolean isTypeCheckNeeded);
	List<CustomFieldConfigMaster> getCustomFieldsWithDefaultValueForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded);
	String validatePropertiesOfCustomField(JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, CustomFieldService customFieldService);
	String validateCustomFieldValueForDataRange(String dataType, String value, String upperLimit, String lowerLimit);
	Object convertCustomFieldDataType(String customFieldValue, String dataType);
	List<Integer> getInstanceIdsOfCustomFieldValuesModifiedAfter(Integer entityId, Integer engagementId, Integer productId, Date modifiedAfterDate);
	void cloneCustomFieldValuesOfInstance(Integer entityId, Integer originalInstanceId, Integer clonedInstanceId);
	List<CustomFieldValues> getValuesOfCustomFieldForInstance(Integer customFieldId, Integer entityInstanceId);
	List<CustomFieldValues> getCustomFieldValuesExistForInstanceByFieldNames(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, List<String> fieldNames);
	void saveCustomFieldsListForInstance(List<CustomFieldValues> customFieldValuesList, Integer instanceId);
	
	 List<CustomFieldConfigMaster> getCustomFieldExistForEngagementIdAndProductId(Integer entityId,Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
	
	List<HashMap<String, Object>> getCustomFieldValuesExistForEntityTypeBased(Integer entityId, Integer entityTypeId,Integer entityInstanceId, Integer engagementId,Integer productId, String frequencyType, String frequency,Integer frequencyMonth, Integer frequencyYear, int i,Date modifiedAfterDate);

	List<HashMap<String, Object>> getAllCustomFieldsExistInstance(Integer entityId, Integer entityParentInstanceId,	Integer engagementId, Integer productId, Integer i,Date modifiedAfterDate);
	List<CustomFieldConfigMaster> getCustomFieldExistForEntityWithoutEntityTypeId(Integer entityId,Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
	
	HashMap<String, Integer> getSeriesCustomFieldValuesCount(Integer entityId, Integer entityTypeId,Integer entityInstanceId, Integer engagementId,
			Integer productId, String frequencyType,Integer frequencyMonth, Integer frequencyYear, int isActive,Date modifiedAfterDate);
	
}
