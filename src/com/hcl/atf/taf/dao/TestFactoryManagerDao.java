package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.UserList;

public interface TestFactoryManagerDao {

public	List<TestFactoryManager> listTestFactoryManager(int testFactoryId,
			int entityStatusActive);

public	void addTestFactoryManager(TestFactoryManager tfManager);

public	void delete(TestFactoryManager manager);

public TestFactoryManager getTestFactoryManagerByTestCatoryIdUserId(int userId,int testFactoryId);

public void deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(Integer testFactoryId, List<Integer> userIds);

}
