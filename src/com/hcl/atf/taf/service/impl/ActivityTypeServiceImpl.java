package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.JsonActivityMaster;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
@Repository
public class ActivityTypeServiceImpl implements ActivityTypeService{
	private static final Log log = LogFactory.getLog(ActivityTypeServiceImpl.class);
	@Autowired
	ActivityTypeDAO activityTypeDAO;


	@Override
	@Transactional
	public void addActivityMaster(ActivityMaster activityMaster) {
		activityTypeDAO.addActivityMaster(activityMaster);
	}

	@Override
	@Transactional
	public void updateActivityMaster(ActivityMaster activityMaster) {
		activityTypeDAO.updateActivityMaster(activityMaster);
	}

	@Override
	@Transactional
	public Boolean isActivityMasterAvailable(String activityTypeName, Integer referenceActivityMasterId, Integer testFactoryId, Integer productId){
		return activityTypeDAO.isActivityMasterAvailable(activityTypeName, referenceActivityMasterId, testFactoryId, productId);
	}

	@Override
	@Transactional
	public List<JsonActivityMaster> listActivityTypes(Integer testFactoryId, Integer productId, Integer jtStartIndex, Integer jtPageSize, int initializationLevel, Boolean isConsildated) {
		List<ActivityMaster> activityTypeList = null;
		List<JsonActivityMaster> activityMasters = new ArrayList<JsonActivityMaster>();
		try {
			activityTypeList = activityTypeDAO.listActivityTypes(testFactoryId, productId, jtStartIndex, jtPageSize, initializationLevel, isConsildated);
			for(ActivityMaster activityMaster: activityTypeList){
				activityMasters.add(new JsonActivityMaster(activityMaster));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return activityMasters;
	}

	@Override
	@Transactional
	public List<ActivityType> listActivityTypes(int entityStatusActive, Integer jtStartIndex, Integer jtPageSize,Integer initializationLevel) {
		return activityTypeDAO.listActivityTypes(entityStatusActive, jtStartIndex, jtPageSize,initializationLevel);
	}


	@Override
	@Transactional
	public ActivityMaster getActivityMasterById(Integer activityMasterId) {
		return activityTypeDAO.getActivityMasterById(activityMasterId);
	}


	@Override
	@Transactional
	public List<ActivityType> listActivityTypesByActivityGroupId(
			int entityStatusActive, int activityGroupId, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel) {
		return activityTypeDAO.listActivityTypesByActivityGroupId(entityStatusActive,activityGroupId, jtStartIndex, jtPageSize,initializationLevel);
	}


	@Override
	@Transactional
	public ActivityMaster getActivityMasterByName(String activityMasterName) {
		return activityTypeDAO.getActivityMasterByName(activityMasterName);
	}


	@Override
	@Transactional
	public ExecutionTypeMaster getCategoryByName(String categoryName) {
		return activityTypeDAO.getCategoryByName(categoryName);
	}

	@Override
	@Transactional
	public ActivityMaster getActivityMasterByNameAndProductId(String activityMasterName,Integer productId){
		return activityTypeDAO.getActivityMasterByNameAndProductId(activityMasterName, productId);
	}
	
	@Override
	@Transactional
	public List<JsonActivityMaster> listActivityTypesByEnagementIdAndProductId(Integer testFactoryId, Integer productId, Boolean isConsildated){
		List<ActivityMaster> activityTypeList = null;
		List<ActivityMaster> activitiesWithWF = new ArrayList<ActivityMaster>();
		List<ActivityMaster> activitiesWithOutWF = new ArrayList<ActivityMaster>();
		List<WorkflowMasterEntityMapping> workflowMappedTypeList = new ArrayList<WorkflowMasterEntityMapping>();
		List<JsonActivityMaster> activityMasters = new ArrayList<JsonActivityMaster>();
		try {
			activityTypeList = activityTypeDAO.listActivityTypesByEnagementIdAndProductId(testFactoryId, productId, isConsildated);
			if(activityTypeList != null && activityTypeList.size() >0) {
				for(ActivityMaster activityMaster: activityTypeList){
					workflowMappedTypeList = activityTypeDAO.listActivityTypeByProductAndEntityAndMappingLevel(productId, 
							IDPAConstants.ENTITY_ACTIVITY_TYPE, activityMaster.getActivityMasterId(), IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_ENTITY);
					if(workflowMappedTypeList != null && workflowMappedTypeList.size() > 0){												
						activitiesWithWF.add(activityMaster);
					}else{
						activitiesWithOutWF.add(activityMaster);
					}
				}
				
				if(activitiesWithOutWF != null & activitiesWithOutWF.size() > 0){
					activitiesWithWF.addAll(activitiesWithOutWF);
				}
				if(activitiesWithWF != null && activitiesWithWF.size() > 0){
					for(ActivityMaster activityMaster : activitiesWithWF){
					activityMasters.add(new JsonActivityMaster(activityMaster));
					}
				}
			}
			
		}catch(Exception e) {
			log.error("Error service in listActivityTypesByEnagementIdAndProductId",e);
		}
		Collections.sort(activityMasters, new Comparator<JsonActivityMaster>() {
			@Override
			public int compare(JsonActivityMaster jsonActivityMasterOne, JsonActivityMaster jsonActivityMasterTwo) {
				return jsonActivityMasterOne.getActivityMasterId() - jsonActivityMasterTwo.getActivityMasterId();
			}
		});
		return activityMasters;
	}

	@Override
	@Transactional
	public ActivityMaster getActivityMasterByNameInProductAndTestFactory(String activityTypeName, Integer productId, Integer testFactoryId) {
		return activityTypeDAO.getActivityMasterByNameInProductAndTestFactory(activityTypeName, productId, testFactoryId);
	}
}
