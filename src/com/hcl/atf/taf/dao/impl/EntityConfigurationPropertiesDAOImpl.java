package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.EntityConfigurationPropertiesDAO;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
@Repository
public class EntityConfigurationPropertiesDAOImpl implements EntityConfigurationPropertiesDAO {
	private static final Log log = LogFactory.getLog(EntityConfigurationPropertiesDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<EntityConfigurationPropertiesMaster> listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(
			Integer entityConfigPropertiesMasterId,Integer entityMasterId) {
		List<EntityConfigurationPropertiesMaster> entityConfigurationPropertiesMasterList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityConfigurationPropertiesMaster.class, "entityConfigurePropsMaster");
			c.createAlias("entityConfigurePropsMaster.entityMaster", "entityMaster");
			if(entityConfigPropertiesMasterId!=-1){
				c.add(Restrictions.eq("entityConfigurePropsMaster.entityConfigPropertiesMasterId",entityConfigPropertiesMasterId));
			}
			if(entityMasterId!=-1){
				c.add(Restrictions.eq("entityMaster.entitymasterid",entityMasterId));
			}
			entityConfigurationPropertiesMasterList=c.list();
			if(entityConfigurationPropertiesMasterList.size()!=0){
				for(EntityConfigurationPropertiesMaster entityConfigPropMastr:entityConfigurationPropertiesMasterList){
					Hibernate.initialize(entityConfigPropMastr);
					if(entityConfigPropMastr.getEntityMaster()!=null){
						Hibernate.initialize(entityConfigPropMastr.getEntityMaster());
					}
				}
			}
			
		} catch (Exception re) {			
			log.error("Entity Configure Props Master failed", re);
		}
		return entityConfigurationPropertiesMasterList;
	}
	@Override
	@Transactional
	public void addEntityConfigureProperties(
			EntityConfigurationProperties entityConfigurationProperties) {
		sessionFactory.getCurrentSession().saveOrUpdate(entityConfigurationProperties);
	}
	@Override
	@Transactional
	public List<EntityConfigurationProperties> getEntityConfigurePropertiesByEntityId(Integer entityId,Integer entityMasterId,Integer entityConfigPropertiesMasterId) {

		List<EntityConfigurationProperties> entityConfigurationProperties=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityConfigurationProperties.class, "entityConfigurationProperties");
			c.createAlias("entityConfigurationProperties.entityConfigurationPropertiesMaster", "entityConfigPropsMaster");
			c.createAlias("entityConfigPropsMaster.entityMaster", "entityMaster");
			if(entityId!=-1){
				c.add(Restrictions.eq("entityConfigurationProperties.entityId",entityId));
			}
			if(entityConfigPropertiesMasterId!=-1){
				c.add(Restrictions.eq("entityConfigPropsMaster.entityConfigPropertiesMasterId",entityConfigPropertiesMasterId));
			}
			if(entityMasterId!=-1){
				c.add(Restrictions.eq("entityMaster.entitymasterid",entityMasterId));
			}
			
			entityConfigurationProperties=c.list();
			if(entityConfigurationProperties.size()!=0){
				for(EntityConfigurationProperties entityConfigurationPro:entityConfigurationProperties){
					Hibernate.initialize(entityConfigurationPro);
					if(entityConfigurationPro.getEntityConfigurationPropertiesMaster()!=null){
						Hibernate.initialize(entityConfigurationPro.getEntityConfigurationPropertiesMaster());
						if(entityConfigurationPro.getEntityConfigurationPropertiesMaster().getEntityMaster()!=null){
							Hibernate.initialize(entityConfigurationPro.getEntityConfigurationPropertiesMaster().getEntityMaster());
						}
					}
				}
			}
			
			
		} catch (Exception re) {
			log.error("Entity Configure Props  failed", re);
		}
		return entityConfigurationProperties;
	}
	
	@Override
	@Transactional
	public void deleteEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties) {
		try {
			sessionFactory.getCurrentSession().delete(entityConfigurationProperties);
			log.info("Entity Configuration Properties deletion done");
		} catch(Exception e){
			log.error("Entity Configure Props deletion failed", e);
		}
	}
	@Override
	@Transactional
	public void updateEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties) {
		try {
			sessionFactory.getCurrentSession().update(entityConfigurationProperties);
			log.info("Entity Configuration Properties updation done");
		} catch(Exception e){
			log.error("Entity Configure Props updation failed", e);
		}
	}
	
	@Override
	@Transactional
	public EntityConfigurationProperties getEntityConfigureProperties(Integer entityConfPropId) {
		EntityConfigurationProperties entityConfigureProperty=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from EntityConfigurationProperties where entityConfigPropertyId=:entityConfPropId")
					.setParameter("entityConfPropId", entityConfPropId)
					.list();
			entityConfigureProperty = (list!=null && list.size()!=0) ? (EntityConfigurationProperties)list.get(0) : null;
		} catch(Exception e){
			log.error("Entity Configuration Property retrieval failed", e);
		}
		return entityConfigureProperty;
	}
}
