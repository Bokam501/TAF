package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.EntityRelationshipDao;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.json.JsonEntityRelationship;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.EntityRelationshipService;
@Service
public class EntityRelationshipServiceImpl implements EntityRelationshipService {

	private static final Log log = LogFactory.getLog(EntityRelationshipServiceImpl.class);
	
	@Autowired
	private EntityRelationshipDao entityRelationshipDao;
	@Autowired
	private ActivityService activityService;
	
	@Override
	@Transactional
	public Integer addEntityRelationship(EntityRelationship entityRelationship) {
		return entityRelationshipDao.addEntityRelationship(entityRelationship);
	}
	
	@Override
	@Transactional
	public List<Integer> listEntityInstance2ByEntityInstance1(Integer entityType1,Integer entityType2,Integer entityInstance1) {
		return entityRelationshipDao.listEntityInstance2ByEntityInstance1(entityType1, entityType2, entityInstance1);
	}
	
	@Override
	@Transactional
	public void deleteEntityRelationship(Integer entityInstanceId1,Integer entityInstanceId2,Integer entityTypeId1,Integer entityTypeId2) {
		 entityRelationshipDao.deleteEntityRelationship(entityInstanceId1, entityInstanceId2, entityTypeId1, entityTypeId2);
	}

	@Override
	@Transactional
	public List<JsonEntityRelationship> listSourceEntitiesByTypeAndInstanceIds(Integer entityType1,Integer entityInstance1) {
		log.info("listSourceEntitiesByTypeAndInstanceIds execution started");
		List<JsonEntityRelationship> jsonEntityRelationshipList = new ArrayList<JsonEntityRelationship>();
		List<EntityRelationship> entityRelationshipList = null;
		try{
			 entityRelationshipList = entityRelationshipDao.listSourceEntitiesByTypeAndInstanceIds(entityType1, entityInstance1);
			 if(entityRelationshipList != null && entityRelationshipList.size() > 0){
				 for(EntityRelationship entityRelationship : entityRelationshipList){
					 JsonEntityRelationship jsonEntityRelationship = new JsonEntityRelationship(entityRelationship);				 
					 if(entityRelationship.getEntityTypeId2() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
						 Activity activity = activityService.getActivityById(jsonEntityRelationship.getEntityInstanceId2(), 1);
						 jsonEntityRelationship.setEntityTypeName2(IDPAConstants.ENTITY_ACTIVITY);
						 jsonEntityRelationship.setEntityInstanceName2(activity.getActivityName());
						 if(activity.getWorkflowStatus() != null){
						 jsonEntityRelationship.setEntityStatus(activity.getWorkflowStatus().getWorkflowStatusDisplayName());
						 }						 
					 }
					 jsonEntityRelationshipList.add(jsonEntityRelationship);
				 }
			 }
		}catch(Exception e){
			log.error("listSourceEntitiesByTypeAndInstanceIds failed", e);
		}
		 return jsonEntityRelationshipList;
	}

	@Override
	@Transactional
	public List<EntityRelationship> getEntityRelationShipByEntityInstanceId(Integer entityInstanceId1) {
		return	 entityRelationshipDao.getEntityRelationShipByEntityInstanceId(entityInstanceId1);
	}

	@Override
	@Transactional
	public EntityRelationship getEntityRelationshipByTypeAndInstanceIds(
			Integer entityType1, Integer entityType2, Integer entityInstance1, Integer entityInstance2) {
		return entityRelationshipDao.getEntityRelationshipByTypeAndInstanceIds(entityType1, entityType2, entityInstance1, entityInstance2);
	}

	@Override
	@Transactional
	public List<EntityRelationship> getEntityRelationShipByTypes(Integer entityType1, Integer entityType2) {
		return entityRelationshipDao.getEntityRelationShipByTypes(entityType1, entityType2);
	}
	
}
