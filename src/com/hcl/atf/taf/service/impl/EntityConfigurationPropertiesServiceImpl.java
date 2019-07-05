package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.EntityConfigurationPropertiesDAO;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
@Service
public class EntityConfigurationPropertiesServiceImpl implements EntityConfigurationPropertiesService{
	
	@Autowired
	private EntityConfigurationPropertiesDAO entityConfigurationPropertiesDAO;
	
	@Override
	@Transactional
	public List<EntityConfigurationPropertiesMaster> listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(
			Integer entityConfigPropertiesMasterId,Integer entityMasterId) {
		return entityConfigurationPropertiesDAO.listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(entityConfigPropertiesMasterId,entityMasterId);
	}
	@Override
	@Transactional
	public void addEntityConfigureProperties(
			EntityConfigurationProperties entityConfigurationProperties) {
		entityConfigurationPropertiesDAO.addEntityConfigureProperties(entityConfigurationProperties);
	}
	@Override
	@Transactional
	public List<EntityConfigurationProperties> getEntityConfigurePropertiesByEntityId(Integer entityId,Integer entityMasterId,Integer entityConfigPropertiesMasterId) {
		return entityConfigurationPropertiesDAO.getEntityConfigurePropertiesByEntityId(entityId,entityMasterId,entityConfigPropertiesMasterId);
	}
	
	@Override
	@Transactional
	public void deleteEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties) {
		entityConfigurationPropertiesDAO.deleteEntityConfigureProperties(entityConfigurationProperties);		
	}
	@Override
	public void updateEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties) {
		entityConfigurationPropertiesDAO.updateEntityConfigureProperties(entityConfigurationProperties);
	}
	@Override
	@Transactional
	public EntityConfigurationProperties getEntityConfigureProperties(Integer entityConfPropId) {
		return entityConfigurationPropertiesDAO.getEntityConfigureProperties(entityConfPropId);
	}

}
