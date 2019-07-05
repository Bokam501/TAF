package com.hcl.atf.taf.service;


public interface HierarchicalEntitiesService {

	void updateHierarchyIndexForNew(String tableName, int parentRightIndex);
	void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex);
}
