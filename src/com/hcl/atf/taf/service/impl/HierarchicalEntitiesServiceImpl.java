package com.hcl.atf.taf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.HierarchicalEntitiesDAO;
import com.hcl.atf.taf.service.HierarchicalEntitiesService;


@Service
public class HierarchicalEntitiesServiceImpl implements HierarchicalEntitiesService {
	
	@Autowired
	HierarchicalEntitiesDAO hierarchicalEntitiesDAO;

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(String tableName, int parentRightIndex) {
		
		hierarchicalEntitiesDAO.updateHierarchyIndexForNew(tableName, parentRightIndex);
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex) {
		
		hierarchicalEntitiesDAO.updateHierarchyIndexForDelete(tableName, leftIndex, rightIndex);
	}
}
