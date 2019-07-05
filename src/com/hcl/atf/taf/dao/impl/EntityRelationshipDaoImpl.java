package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.EntityRelationshipDao;
import com.hcl.atf.taf.model.EntityRelationship;
@Repository
public class EntityRelationshipDaoImpl implements EntityRelationshipDao {
	private static final Log log = LogFactory.getLog(ChangeRequestDAOImpl.class);
	@Autowired
	public SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Integer addEntityRelationship(EntityRelationship entityRelationship) {
		log.info("adding EntityRelationship instance");
		try {	
			sessionFactory.getCurrentSession().save(entityRelationship);
			log.info("add EntityRelationship successful");
		} catch (RuntimeException re) {
			log.error("add EntityRelationship failed", re);
		}
		return entityRelationship.getId();
	}
	
	@Override
	@Transactional
	public List<Integer> listEntityInstance2ByEntityInstance1(Integer entityType1,Integer entityType2,Integer entityInstance1) {
		log.info("inside listing entityInstance2");
		List<Integer> entityInstance2List=new ArrayList<Integer>();
		int entityInstance2 = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityRelationship.class, "entityRelationship");
			c.add(Restrictions.eq("entityRelationship.entityTypeId1", entityType1));
			c.add(Restrictions.eq("entityRelationship.entityTypeId2", entityType2));
			c.add(Restrictions.eq("entityRelationship.entityInstanceId1", entityInstance1));			
			List list = c.setProjection(Projections.property("entityRelationship.entityInstanceId2")).list();
			if(list != null && list.size() >0){
				for(Object obj:list){
					entityInstance2 = (int)obj;
					entityInstance2List.add(entityInstance2);
				}
			}
		}catch(RuntimeException re){
			log.error("listing entityInstance2 failed");
		}
		return entityInstance2List;
	}

	@Override
	@Transactional
	public void deleteEntityRelationship(Integer entityInstanceId1,Integer entityInstanceId2,Integer entityTypeId1,Integer entityTypeId2) {
		log.info("inside deleteEntityRelationship");
		try{
			String hql = "delete from EntityRelationship where entityInstanceId1=:entityInstanceId1 and entityInstanceId2=:entityInstanceId2 and entityTypeId1=:entityTypeId1 and entityTypeId2=:entityTypeId2";
				Query query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setInteger("entityInstanceId1", entityInstanceId1);
				query.setInteger("entityInstanceId2", entityInstanceId2);
				query.setInteger("entityTypeId1", entityTypeId1);
				query.setInteger("entityTypeId2", entityTypeId2);
				query.executeUpdate();
				log.debug("deleteEntityRelationship successful");
		}catch(RuntimeException re){
			log.error("deleteEntityRelationship failed", re);
		}
		
	}
	
	@Override
	@Transactional
	public List<EntityRelationship> listSourceEntitiesByTypeAndInstanceIds(Integer entityType1,Integer entityInstance1) {
		log.info("inside listSourceEntitiesByTypeAndInstanceIds");
		List<EntityRelationship> entityRelationshipList=new ArrayList<EntityRelationship>();		
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityRelationship.class, "entityRelationship");
			c.add(Restrictions.eq("entityRelationship.entityTypeId1", entityType1));
			c.add(Restrictions.eq("entityRelationship.entityInstanceId1", entityInstance1));			
			entityRelationshipList = c.list();
			
		}catch(RuntimeException re){
			log.error("listSourceEntitiesByTypeAndInstanceIds failed", re);
		}
		return entityRelationshipList;
	}

	@Override
	public List<EntityRelationship> getEntityRelationShipByEntityInstanceId(Integer entityInstanceId1) {
		log.info("inside getEntityRelationShipByEntityInstanceId1");
		List<EntityRelationship> entityRelationShipList = new ArrayList<EntityRelationship>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityRelationship.class,"entityRelationShip");
			c.add(Restrictions.eq("entityRelationShip.entityTypeId1",IDPAConstants.ENTITY_ACTIVITY_ID));
			c.add(Restrictions.eq("entityRelationShip.entityInstanceId1",entityInstanceId1));			
			entityRelationShipList = c.list(); 
		}catch(RuntimeException re){
			log.error("getEntityRelationShipByEntityInstanceId1 failed", re);
			
		}
		return entityRelationShipList;
	}

	@Override
	public EntityRelationship getEntityRelationshipByTypeAndInstanceIds(Integer entityType1, Integer entityType2, Integer entityInstance1, Integer entityInstance2) {
		log.info("inside getEntityRelationshipByTypeAndInstanceIds");
		EntityRelationship entityRelationShip = null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityRelationship.class,"entityRelationship");
			c.add(Restrictions.eq("entityRelationship.entityTypeId1", entityType1));
			c.add(Restrictions.eq("entityRelationship.entityTypeId2", entityType2));
			c.add(Restrictions.eq("entityRelationship.entityInstanceId1", entityInstance1));
			c.add(Restrictions.eq("entityRelationship.entityInstanceId2", entityInstance2));
			List entityRelationshipList = c.list();
			entityRelationShip = (entityRelationshipList != null && entityRelationshipList.size() !=0)?(EntityRelationship)entityRelationshipList.get(0):null;
		} catch (RuntimeException re) {
			log.error("getEntityRelationshipByTypeAndInstanceIds failed", re);
		}
		return entityRelationShip;
	}

	@Override
	public List<EntityRelationship> getEntityRelationShipByTypes(Integer entityType1, Integer entityType2) {
		log.info("inside getEntityRelationShipByTypes");
		List<EntityRelationship> entityRelationShip = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityRelationship.class,"entityRelationship");
			c.add(Restrictions.eq("entityRelationship.entityTypeId1", entityType1));
			c.add(Restrictions.eq("entityRelationship.entityTypeId2", entityType2));
			entityRelationShip = c.list();			
		}catch(RuntimeException re){
			log.error("getEntityRelationShipByTypes failed", re);
		}
		return entityRelationShip;
	}
	
}
