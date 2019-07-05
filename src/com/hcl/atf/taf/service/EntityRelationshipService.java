package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.json.JsonEntityRelationship;

public interface EntityRelationshipService {

	Integer addEntityRelationship(EntityRelationship entityRelationship);
	List<Integer> listEntityInstance2ByEntityInstance1(Integer entityType1,Integer entityType2,Integer entityInstance1);
	void deleteEntityRelationship(Integer entityInstanceId1,Integer entityInstanceId2,Integer entityTypeId1,Integer entityTypeId2);
	List<JsonEntityRelationship> listSourceEntitiesByTypeAndInstanceIds(Integer entityType1,Integer entityInstance1);
	List<EntityRelationship> getEntityRelationShipByEntityInstanceId(Integer entityInstanceId1);
	EntityRelationship getEntityRelationshipByTypeAndInstanceIds(Integer entityType1,Integer entityType2,Integer entityInstance1,Integer entityInstance2);
	List<EntityRelationship> getEntityRelationShipByTypes(Integer entityType1, Integer entityType2);
}
