package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.EntityMaster;


public interface CustomFieldDAO {

	List<EntityMaster> getWorkflowCapableEntityTypeList();
	List<CustomFieldGroupMaster> getFieldGroups();
	CustomFieldConfigMaster getCustomFieldById(Integer customFieldId, Integer isActive);
	List<CustomFieldConfigMaster> getCustomFieldExistForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
	List<CustomFieldValues> getCustomFieldValuesExistForInstance(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive);
	List<CustomFieldValues> getCustomFieldValuesExistForAllInstanceOfEntity(Integer entityId, List<Integer> instanceIds, String frequencyType, Integer isActive);
	Boolean isCustomFieldAreadyExistWithProperties(Integer fieldId, String fieldName, Integer entityId, Integer entityTypeId, Integer parentEntityId, Integer parentEntityInstanceId, Integer isActive);
	void saveOrUpdateCustomField(CustomFieldConfigMaster customFieldConfigMaster);
	Boolean isCustomFieldHasValueForInstance(Integer customFieldId);
	void deleteCustomField(CustomFieldConfigMaster customFieldConfigMaster);
	void saveOrUpdateCustomFieldValue(CustomFieldValues customFieldValues);
	CustomFieldValues getCustomFieldValuesById(Integer customFieldValueId);
	List<CustomFieldConfigMaster> getCustomFieldsWithDefaultValueForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded);
	List<Integer> getInstanceIdsOfCustomFieldValuesModifiedAfter(Integer entityId, Integer engagementId, Integer productId, Date modifiedAfterDate);
	List<CustomFieldValues> getValuesOfCustomFieldForInstance(Integer customFieldId, Integer entityInstanceId);
	List<CustomFieldValues> getCustomFieldValuesExistForInstanceByFieldNames(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, List<String> fieldNames);
	List<CustomFieldConfigMaster> getCustomFieldExistForEngagementIdAndProductId(Integer entityId,Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
	
	List<CustomFieldConfigMaster> getCustomFieldExistForEntityWithoutEntityTypeId(Integer entityId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration);
}
