package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;

public interface EntityConfigurationPropertiesService {
	List<EntityConfigurationPropertiesMaster> listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(Integer entityConfigPropertiesMasterId,Integer entityMasterId);
	void addEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties);
	List<EntityConfigurationProperties> getEntityConfigurePropertiesByEntityId(Integer entityId,Integer entityMasterId,Integer entityConfigPropertiesMasterId);
	void deleteEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties);
	void updateEntityConfigureProperties(EntityConfigurationProperties entityConfigurationProperties);
	EntityConfigurationProperties getEntityConfigureProperties(Integer entityConfPropId);
}
