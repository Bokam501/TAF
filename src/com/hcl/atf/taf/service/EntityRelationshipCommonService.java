package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.EntityRelationshipCommon;

public interface EntityRelationshipCommonService {

	List<EntityRelationshipCommon> getEntityRelationships(Integer sourceEntityTypeId,Integer sourceEntityInstanceId, String relationshipType, String relationshipSubtype);
	void addEntityRelationship(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String relationshipType, String relationshipSubtype, Integer targetEntityTypeId, Integer targetEntityInstanceId);
	void deleteEntityRelationship(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String relationshipType, String relationshipSubtype, Integer targetEntityTypeId, Integer targetEntityInstanceId);
	void deleteEntityRelationships(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String relationshipType, String relationshipSubtype);
}
