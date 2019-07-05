package com.hcl.atf.taf.dao;

public interface HierarchicalEntitiesDAO {

	void updateHierarchyIndexForNew(String tableName, int parentRightIndex);
	void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex);
}
