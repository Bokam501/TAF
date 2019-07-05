package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.CustomFieldDAO;
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;

@Service
public class CustomFieldDAOImpl implements CustomFieldDAO {
	private static final Log log = LogFactory.getLog(CustomFieldDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<EntityMaster> getWorkflowCapableEntityTypeList() {
		List<EntityMaster> entityMasters = new ArrayList<EntityMaster>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntityMaster.class, "entityMaster");
			criteria.add(Restrictions.eq("entityMaster.isCustomFieldCapable", 1));
			criteria.addOrder(Order.asc("entityMaster.entityDisplayName"));
			entityMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error while obtaining entity type list in getWorkflowCapableEntityTypeList", ex);
		}
		return entityMasters;
	}

	@Override
	@Transactional
	public List<CustomFieldGroupMaster> getFieldGroups() {
		List<CustomFieldGroupMaster> customFieldGroupMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldGroupMaster.class, "customFieldGroup");
			criteria.addOrder(Order.asc("customFieldGroup.displayOrder"));
			customFieldGroupMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error in getFieldGroups - ", ex);
		}
		return customFieldGroupMasters;
	}
	
	@Override
	@Transactional
	public CustomFieldConfigMaster getCustomFieldById(Integer customFieldId, Integer isActive) {
		log.debug("Inside getCustomFieldById with parameters - customFieldId : "+customFieldId);
		CustomFieldConfigMaster customFieldConfigMaster = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			criteria.add(Restrictions.eq("customFields.id", customFieldId));
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			List<CustomFieldConfigMaster> customFieldConfigMasters = criteria.list();
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				customFieldConfigMaster = customFieldConfigMasters.get(0);
			}
		}catch(Exception ex){
			log.error("Error in getCustomFieldById - ", ex);
		}
		return customFieldConfigMaster;
	}
	
	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldExistForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		log.debug("Inside getCustomFieldExistForEntity with parameters - entityId : "+entityId+", entityTypeId : "+entityTypeId+", engagementId : "+engagementId+", productId : "+productId);
		List<CustomFieldConfigMaster> customFieldConfigMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
				if(entityTypeId >0) {
						criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entityType", entityTypeId), Restrictions.isNull("customFields.entityType"))));
				}
			}else{
				criteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(engagementId != null && engagementId > 0 && productId != null && productId > 0){
				criteria.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)), 
							Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", productId))
				)));
			}else if(engagementId != null && engagementId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.testFactory.testFactoryId", engagementId));
					detachedCriteria.setProjection(Property.forName("product.productId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else if(productId != null && productId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", productId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.productId", productId));
					detachedCriteria.setProjection(Property.forName("product.testFactory.testFactoryId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", productId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else {
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntity.entitymasterid", 0), Restrictions.isNull("customFields.parentEntity.entitymasterid")));
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntityInstanceId", 0), Restrictions.isNull("customFields.parentEntityInstanceId")));
			}
			
			if(isTypeCheckNeeded){
				if(entityTypeId != null && entityTypeId > 0){
					criteria.add(Restrictions.eq("customFields.entityType", entityTypeId));
				}else{
					criteria.add(Restrictions.isNull("customFields.entityType"));
				}				
			}
			
			if(frequencyType != null){
				criteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType) && frequency != null){
					criteria.add(Restrictions.eq("customFields.frequency", frequency));
				}
			}
			criteria.createAlias("customFields.fieldGroup", "fieldGroup");
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			criteria.addOrder(Order.asc("fieldGroup.displayOrder"));
			criteria.addOrder(Order.asc("customFields.displayOrder"));
			
			customFieldConfigMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error in getCustomFieldExistForEntity - ", ex);
		}
		return customFieldConfigMasters;
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getCustomFieldValuesExistForInstance(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive) {
		log.debug("Inside getCustomFieldValuesExistForInstance with parameters - entityId : "+entityId+", entityInstanceId : "+entityInstanceId+", frequencyType : "+frequencyType+", frequency : "+frequency+", frequencyMonth : "+frequencyMonth+", frequencyYear : "+frequencyYear);
		List<CustomFieldValues> customFieldValuesList = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			if(entityInstanceId != null && entityInstanceId >0) {
				criteria.add(Restrictions.eq("customFieldValues.entityInstanceId", entityInstanceId));
			}
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				detachedCriteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				detachedCriteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(frequencyType != null){
				detachedCriteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType)){
					detachedCriteria.add(Restrictions.eq("customFields.frequency", frequency));
					if(!"Annual".equalsIgnoreCase(frequency)){
						criteria.add(Restrictions.eq("customFieldValues.frequencyYear", frequencyYear));
						if("Daily".equalsIgnoreCase(frequency)){
							criteria.add(Restrictions.eq("customFieldValues.frequencyMonth", frequencyMonth));
						}
					}else if("Annual".equalsIgnoreCase(frequency)){
						List<Integer> frequencyYears = new ArrayList<Integer>();
						for(int i = frequencyYear - 2; i <= frequencyYear + 2; i++){
							frequencyYears.add(i);
						}
						criteria.add(Restrictions.in("customFieldValues.frequencyYear", frequencyYears));
					}
				}
			}
			
			if(isActive != null){
				detachedCriteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			detachedCriteria.addOrder(Order.asc("customFields.displayOrder"));
			detachedCriteria.setProjection(Property.forName("customFields.id"));
			
			criteria.add(Subqueries.propertyIn("customFieldValues.customFieldId.id", detachedCriteria));
			criteria.addOrder(Order.asc("customFieldValues.frequencyOrder"));
			
			customFieldValuesList = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesExistForInstance - ", ex);
		}
		return customFieldValuesList;
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getCustomFieldValuesExistForAllInstanceOfEntity(Integer entityId, List<Integer> instanceIds, String frequencyType, Integer isActive) {
		log.debug("Inside getCustomFieldValuesExistForAllInstanceOfEntity with parameters - entityId : "+entityId+", instanceIds : "+instanceIds+", frequencyType : "+frequencyType);
		List<CustomFieldValues> customFieldValuesList = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			criteria.add(Restrictions.in("customFieldValues.entityInstanceId", instanceIds));
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				detachedCriteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				detachedCriteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(frequencyType != null && !frequencyType.trim().isEmpty()){
				detachedCriteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
			}
			if(isActive != null){
				detachedCriteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			detachedCriteria.addOrder(Order.asc("customFields.displayOrder"));
			detachedCriteria.setProjection(Property.forName("customFields.id"));
			
			criteria.add(Subqueries.propertyIn("customFieldValues.customFieldId.id", detachedCriteria));
			criteria.addOrder(Order.asc("customFieldValues.frequencyOrder"));
			
			customFieldValuesList = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesExistForAllInstanceOfEntity - ", ex);
		}
		return customFieldValuesList;
	}

	@Override
	@Transactional
	public Boolean isCustomFieldAreadyExistWithProperties(Integer fieldId, String fieldName, Integer entityId, Integer entityTypeId, Integer parentEntityId, Integer parentEntityInstanceId, Integer isActive) {
		log.debug("Inside isCustomFieldAreadyExistWithProperties with parameters - fieldId : "+fieldId+", fieldName : "+fieldName+", entityId : "+entityId+", entityTypeId : "+entityTypeId+", parentEntityId : "+parentEntityId+", parentEntityInstanceId : "+parentEntityInstanceId);
		Boolean isCustomFieldAreadyExist = false;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			criteria.add(Restrictions.eq("customFields.fieldName", fieldName));
			criteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			if(entityTypeId != null && entityTypeId > 0){
				criteria.add(Restrictions.eq("customFields.entityType", entityTypeId));
			}else{
				criteria.add(Restrictions.or(Restrictions.eq("customFields.entityType", 0), Restrictions.isNull("customFields.entityType")));
			}
			if(parentEntityId != null && parentEntityId > 0){
				criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", parentEntityId));
			}else{
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntity.entitymasterid", 0), Restrictions.isNull("customFields.parentEntity.entitymasterid")));
			}
			if(parentEntityInstanceId != null && parentEntityInstanceId > 0){
				criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", parentEntityInstanceId));
			}else{
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntityInstanceId", 0), Restrictions.isNull("customFields.parentEntityInstanceId")));
			}
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			if(fieldId != null && fieldId > 0){
				criteria.add(Restrictions.ne("customFields.id", fieldId));
			}
			
			List<CustomFieldConfigMaster> customFieldConfigMasters = criteria.list();
			if(customFieldConfigMasters != null && customFieldConfigMasters.size() > 0){
				isCustomFieldAreadyExist = true;
			}
		}catch(Exception ex){
			log.error("Error in isCustomFieldAreadyExistWithProperties - ", ex);
		}
		return isCustomFieldAreadyExist;
	}
	
	@Override
	@Transactional
	public void saveOrUpdateCustomField(CustomFieldConfigMaster customFieldConfigMaster) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(customFieldConfigMaster);
		}catch(Exception ex){
			log.error("Error in saveOrUpdateCustomField - ", ex);
		}
	}

	@Override
	@Transactional
	public Boolean isCustomFieldHasValueForInstance(Integer customFieldId) {
		log.debug("Inside isCustomFieldHasValueForInstance with parameters - customFieldId : "+customFieldId);
		Boolean isCustomFieldHasValue = false;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			criteria.add(Restrictions.eq("customFieldValues.customFieldId.id", customFieldId));
			
			List<CustomFieldValues> customFieldValuesList = criteria.list();
			if(customFieldValuesList != null && customFieldValuesList.size() > 0){
				isCustomFieldHasValue = true;
			}
		}catch(Exception ex){
			log.error("Error in isCustomFieldHasValueForInstance - ", ex);
		}
		return isCustomFieldHasValue;
	}

	@Override
	@Transactional
	public void deleteCustomField(CustomFieldConfigMaster customFieldConfigMaster) {
		try{
			sessionFactory.getCurrentSession().delete(customFieldConfigMaster);
		}catch(Exception ex){
			log.error("Error in deleteCustomField - ", ex);
		}
	}

	
	@Override
	@Transactional
	public void saveOrUpdateCustomFieldValue(CustomFieldValues customFieldValues) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(customFieldValues);
		}catch(Exception ex){
			log.error("Error in saveOrUpdateCustomFieldValue - ", ex);
		}
	}

	@Override
	@Transactional
	public CustomFieldValues getCustomFieldValuesById(Integer customFieldValueId) {
		CustomFieldValues customFieldValues = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			criteria.add(Restrictions.eq("customFieldValues.id", customFieldValueId));
			List<CustomFieldValues> customFieldValuesList = criteria.list();
			if(customFieldValuesList != null && customFieldValuesList.size() > 0){
				customFieldValues = customFieldValuesList.get(0);
			}
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesById - ", ex);
		}
		return customFieldValues;
	}

	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldsWithDefaultValueForEntity(Integer entityId, Integer entityTypeId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded) {
		log.debug("Inside getCustomFieldsWithDefaultValueForEntity with parameters - entityId : "+entityId+", entityTypeId : "+entityTypeId+", engagementId : "+engagementId+", productId : "+productId);
		List<CustomFieldConfigMaster> customFieldConfigMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
				criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entityType", entityTypeId), Restrictions.isNull("customFields.entityType"))));
			}else{
				criteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(engagementId != null && engagementId > 0 && productId != null && productId > 0){
				criteria.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)), 
							Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", productId))
				)));
			}else if(engagementId != null && engagementId > 0){
				criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID));
				criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId));
				
			}else if(productId != null && productId > 0){
				criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID));
				criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", productId));
			}else {
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntity.entitymasterid", 0), Restrictions.isNull("customFields.parentEntity.entitymasterid")));
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntityInstanceId", 0), Restrictions.isNull("customFields.parentEntityInstanceId")));
			}
			
			if(isTypeCheckNeeded){
				if(entityTypeId != null && entityTypeId > 0){
					criteria.add(Restrictions.eq("customFields.entityType", entityTypeId));
				}else{
					criteria.add(Restrictions.isNull("customFields.entityType"));
				}				
			}
			
			if(frequencyType != null){
				criteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType) && frequency != null){
					criteria.add(Restrictions.eq("customFields.frequency", frequency));
				}
			}
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			
			criteria.add(Restrictions.isNotNull("customFields.defaultValue"));
			criteria.add(Restrictions.ne("customFields.defaultValue", ""));
			
			criteria.createAlias("customFields.fieldGroup", "fieldGroup");
			criteria.addOrder(Order.asc("fieldGroup.displayOrder"));
			criteria.addOrder(Order.asc("customFields.displayOrder"));
			
			customFieldConfigMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error in getCustomFieldsWithDefaultValueForEntity - ", ex);
		}
		return customFieldConfigMasters;
	}

	@Override
	@Transactional
	public List<Integer> getInstanceIdsOfCustomFieldValuesModifiedAfter(Integer entityId, Integer engagementId, Integer productId, Date modifiedAfterDate) {
		log.debug("Inside getInstanceIdsOfCustomFieldValuesModifiedAfter with parameters - entityId : "+entityId+", engagementId : "+engagementId+", productId : "+productId+", modifiedAfterDate : "+modifiedAfterDate);
		List<Integer> instanceIds = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				detachedCriteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				detachedCriteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			detachedCriteria.setProjection(Property.forName("customFields.id"));
			
			criteria.add(Subqueries.propertyIn("customFieldValues.customFieldId.id", detachedCriteria));
			criteria.add(Restrictions.ge("customFieldValues.modifiedOn", modifiedAfterDate));
			criteria.setProjection(Projections.distinct(Property.forName("customFieldValues.entityInstanceId")));
			
			instanceIds = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getInstanceIdsOfCustomFieldValuesModifiedAfter - ", ex);
		}
		return instanceIds;
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getValuesOfCustomFieldForInstance(Integer customFieldId, Integer entityInstanceId) {
		List<CustomFieldValues> customFieldValues = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "values");
			criteria.add(Restrictions.eq("values.entityInstanceId", entityInstanceId));
			criteria.add(Restrictions.eq("values.customFieldId.id", customFieldId));
			customFieldValues = criteria.list();
		}catch(Exception ex){
			log.error("Error in getValuesOfCustomFieldForInstance - ", ex);
		}
		return customFieldValues;
	}

	@Override
	@Transactional
	public List<CustomFieldValues> getCustomFieldValuesExistForInstanceByFieldNames(Integer entityId, Integer entityInstanceId, String frequencyType, String frequency, Integer frequencyMonth, Integer frequencyYear, Integer isActive, List<String> fieldNames) {
		log.debug("Inside getCustomFieldValuesExistForInstanceByFieldNames with parameters - entityId : "+entityId+", entityInstanceId : "+entityInstanceId+", frequencyType : "+frequencyType+", frequency : "+frequency+", frequencyMonth : "+frequencyMonth+", frequencyYear : "+frequencyYear+", fieldName : "+fieldNames);
		List<CustomFieldValues> customFieldValuesList = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldValues.class, "customFieldValues");
			if(entityInstanceId != null && entityInstanceId > 0){
				criteria.add(Restrictions.eq("customFieldValues.entityInstanceId", entityInstanceId));
			}
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				detachedCriteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				detachedCriteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(fieldNames != null && fieldNames.size() > 0){
				detachedCriteria.add(Restrictions.in("customFields.fieldName", fieldNames));
			}
			if(frequencyType != null){
				detachedCriteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType)){
					detachedCriteria.add(Restrictions.eq("customFields.frequency", frequency));
					if(!"Annual".equalsIgnoreCase(frequency)){
						criteria.add(Restrictions.eq("customFieldValues.frequencyYear", frequencyYear));
						if("Daily".equalsIgnoreCase(frequency)){
							criteria.add(Restrictions.eq("customFieldValues.frequencyMonth", frequencyMonth));
						}
					}else if("Annual".equalsIgnoreCase(frequency)){
						List<Integer> frequencyYears = new ArrayList<Integer>();
						for(int i = frequencyYear - 2; i <= frequencyYear + 2; i++){
							frequencyYears.add(i);
						}
						criteria.add(Restrictions.in("customFieldValues.frequencyYear", frequencyYears));
					}
				}
			}
			
			if(isActive != null){
				detachedCriteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			detachedCriteria.addOrder(Order.asc("customFields.displayOrder"));
			detachedCriteria.setProjection(Property.forName("customFields.id"));
			
			criteria.add(Subqueries.propertyIn("customFieldValues.customFieldId.id", detachedCriteria));
			criteria.addOrder(Order.asc("customFieldValues.frequencyOrder"));
			
			customFieldValuesList = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldValuesExistForInstanceByFieldNames - ", ex);
		}
		return customFieldValuesList;
	}
	
	
	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldExistForEngagementIdAndProductId(Integer entityId,Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		log.debug("Inside getCustomFieldExistForEntity with parameters - entityId : "+entityId+", engagementId : "+engagementId+", productId : "+productId);
		List<CustomFieldConfigMaster> customFieldConfigMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				criteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(engagementId != null && engagementId > 0 && productId != null && productId > 0){
				criteria.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)), 
							Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", productId))
				)));
			}else if(engagementId != null && engagementId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.testFactory.testFactoryId", engagementId));
					detachedCriteria.setProjection(Property.forName("product.productId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else if(productId != null && productId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", productId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.productId", productId));
					detachedCriteria.setProjection(Property.forName("product.testFactory.testFactoryId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", productId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else {
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntity.entitymasterid", 0), Restrictions.isNull("customFields.parentEntity.entitymasterid")));
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntityInstanceId", 0), Restrictions.isNull("customFields.parentEntityInstanceId")));
			}
			if(frequencyType != null){
				criteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType) && frequency != null){
					criteria.add(Restrictions.eq("customFields.frequency", frequency));
				}
			}
			criteria.createAlias("customFields.fieldGroup", "fieldGroup");
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			criteria.addOrder(Order.asc("fieldGroup.displayOrder"));
			criteria.addOrder(Order.asc("customFields.displayOrder"));
			
			customFieldConfigMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error in getCustomFieldExistForEntity - ", ex);
		}
		return customFieldConfigMasters;
	}
	
	
	@Override
	@Transactional
	public List<CustomFieldConfigMaster> getCustomFieldExistForEntityWithoutEntityTypeId(Integer entityId, Integer engagementId, Integer productId, String frequencyType, String frequency, Integer isActive, Boolean isTypeCheckNeeded, Boolean isCustomFieldConfiguration) {
		log.debug("Inside getCustomFieldExistForEntity with parameters - entityId : "+entityId+", engagementId : "+engagementId+", productId : "+productId);
		List<CustomFieldConfigMaster> customFieldConfigMasters = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CustomFieldConfigMaster.class, "customFields");
			if(entityId == IDPAConstants.ENTITY_ACTIVITY_ID){
				criteria.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("customFields.entity.entitymasterid", entityId), Restrictions.eq("customFields.entity.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_TYPE))));
			}else{
				criteria.add(Restrictions.eq("customFields.entity.entitymasterid", entityId));
			}
			if(engagementId != null && engagementId > 0 && productId != null && productId > 0){
				criteria.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)), 
							Restrictions.conjunction()
							.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
							.add(Restrictions.eq("customFields.parentEntityInstanceId", productId))
				)));
			}else if(engagementId != null && engagementId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.testFactory.testFactoryId", engagementId));
					detachedCriteria.setProjection(Property.forName("product.productId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", engagementId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else if(productId != null && productId > 0){
				if(isCustomFieldConfiguration){
					criteria.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID));
					criteria.add(Restrictions.eq("customFields.parentEntityInstanceId", productId));
				}else{
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
					detachedCriteria.add(Restrictions.eq("product.productId", productId));
					detachedCriteria.setProjection(Property.forName("product.testFactory.testFactoryId"));
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.or(Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.PRODUCT_ENTITY_MASTER_ID))
								.add(Restrictions.eq("customFields.parentEntityInstanceId", productId)),
								Restrictions.conjunction()
								.add(Restrictions.eq("customFields.parentEntity.entitymasterid", IDPAConstants.ENGAGEMENT_ENTITY_MASTER_ID))
								.add(Subqueries.propertyIn("customFields.parentEntityInstanceId", detachedCriteria))
					)));
				}
				
			}else {
				
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntity.entitymasterid", 0), Restrictions.isNull("customFields.parentEntity.entitymasterid")));
				criteria.add(Restrictions.or(Restrictions.eq("customFields.parentEntityInstanceId", 0), Restrictions.isNull("customFields.parentEntityInstanceId")));
			}
			
			if(isTypeCheckNeeded){
				criteria.add(Restrictions.isNull("customFields.entityType"));
			}
			
			if(frequencyType != null){
				criteria.add(Restrictions.eq("customFields.frequencyType", frequencyType));
				if("Series".equalsIgnoreCase(frequencyType) && frequency != null){
					criteria.add(Restrictions.eq("customFields.frequency", frequency));
				}
			}
			criteria.createAlias("customFields.fieldGroup", "fieldGroup");
			if(isActive != null){
				criteria.add(Restrictions.eq("customFields.isActive", isActive));
			}
			criteria.addOrder(Order.asc("fieldGroup.displayOrder"));
			criteria.addOrder(Order.asc("customFields.displayOrder"));
			
			customFieldConfigMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error in getCustomFieldExistForEntity - ", ex);
		}
		return customFieldConfigMasters;
	}
}
