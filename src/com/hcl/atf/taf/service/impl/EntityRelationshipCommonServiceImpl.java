package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.dao.EntityRelationshipCommonDAO;
import com.hcl.atf.taf.model.EntityRelationshipCommon;
import com.hcl.atf.taf.service.EntityRelationshipCommonService;
@Service
public class EntityRelationshipCommonServiceImpl implements EntityRelationshipCommonService {

	@Autowired
	private EntityRelationshipCommonDAO entityRelationshipCommonDAO;
	
	@Override
	public List<EntityRelationshipCommon> getEntityRelationships(
			Integer sourceEntityTypeId, Integer sourceEntityInstanceId,
			String relationshipType, String relationshipSubtype) {
		try {
			return entityRelationshipCommonDAO.getEntityRelationships(sourceEntityTypeId, sourceEntityInstanceId, relationshipType, relationshipSubtype);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public void addEntityRelationship(Integer sourceEntityTypeId,Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype, Integer targetEntityTypeId,	Integer targetEntityInstanceId) {
		try {
			entityRelationshipCommonDAO.addEntityRelationship(sourceEntityTypeId, sourceEntityInstanceId, relationshipType, relationshipSubtype, targetEntityTypeId, targetEntityInstanceId);
		}catch(Exception e) {
			
		}
		
	}

	@Override
	public void deleteEntityRelationship(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype, Integer targetEntityTypeId,
			Integer targetEntityInstanceId) {
		
		try {
			entityRelationshipCommonDAO.deleteEntityRelationship(sourceEntityTypeId, sourceEntityInstanceId, relationshipType, relationshipSubtype, targetEntityTypeId, targetEntityInstanceId);
		}catch(Exception e) {
			
		}
			
	}

	@Override
	public void deleteEntityRelationships(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype) {
		try {
			entityRelationshipCommonDAO.deleteEntityRelationships(sourceEntityTypeId, sourceEntityInstanceId, relationshipType, relationshipSubtype);
		}catch(Exception e) {
			
		}
		
	}
	
	
}
