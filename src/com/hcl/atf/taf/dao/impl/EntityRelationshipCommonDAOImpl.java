/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.EntityRelationshipCommonDAO;
import com.hcl.atf.taf.model.EntityRelationshipCommon;

/**
 * @author silambarasur
 *
 */
@Repository
public class EntityRelationshipCommonDAOImpl implements EntityRelationshipCommonDAO{
	private static final Log log = LogFactory
			.getLog(EntityRelationshipCommonDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<EntityRelationshipCommon> getEntityRelationships(
			Integer sourceEntityTypeId, Integer sourceEntityInstanceId,
			String relationshipType, String relationshipSubtype) {
		List<EntityRelationshipCommon> entityRelationShipList=null; 
		try {
			
			String sql="from EntityRelationshipCommon where sourceEntityTypeId="+sourceEntityTypeId+" and sourceEntityInstanceId="+sourceEntityInstanceId+" and relationshipType='"+relationshipType+"' and relationshipSubtype='"+relationshipSubtype+"'"; 
			entityRelationShipList=sessionFactory.getCurrentSession().createQuery(sql).list();
			
		}catch(RuntimeException e) {
			
		}
		return entityRelationShipList;
	}

	@Override
	@Transactional
	public void addEntityRelationship(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype, Integer targetEntityTypeId,
			Integer targetEntityInstanceId) {
		try {
			EntityRelationshipCommon entityRelationshipCommon = new EntityRelationshipCommon();
			entityRelationshipCommon.setSourceEntityTypeId(sourceEntityTypeId);
			entityRelationshipCommon.setSourceEntityInstanceId(sourceEntityInstanceId);
			entityRelationshipCommon.setRelationshipType(relationshipType);
			entityRelationshipCommon.setRelationshipSubType(relationshipSubtype);
			entityRelationshipCommon.setTargetEntityTypeId(targetEntityTypeId);
			entityRelationshipCommon.setTargetEntityInstanceId(targetEntityInstanceId);
			entityRelationshipCommon.setIsActive(1);
			sessionFactory.getCurrentSession().save(entityRelationshipCommon);
			
		}catch(RuntimeException re) {
			
		}
		
	}

	@Override
	@Transactional
	public void deleteEntityRelationship(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype, Integer targetEntityTypeId,
			Integer targetEntityInstanceId) {
		try {
			String sql="delete from entity_relationship_common where sourceEntityTypeId="+sourceEntityTypeId+" and sourceEntityInstanceId="+sourceEntityInstanceId+" and relationshipType='"+relationshipType+"' and relationshipSubtype='"+relationshipSubtype+"' and targetEntityTypeId="+targetEntityTypeId+" and targetEntityInstanceId="+targetEntityInstanceId;
		sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			
		}
		
	}

	

	@Override
	public void deleteEntityRelationships(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String relationshipType,
			String relationshipSubtype) {
		
		try {
			String sql="delete from entity_relationship_common where sourceEntityTypeId="+sourceEntityTypeId+" and sourceEntityInstanceId="+sourceEntityInstanceId+" and relationshipType='"+relationshipType+"' and relationshipSubtype='"+relationshipSubtype+"'"; 
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			
		}
		
	}
	
	

}
