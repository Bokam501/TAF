/**
 * 
 */
package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseEntityGroupHasTestCase;
import com.hcl.atf.taf.model.UserList;

/**
 * @author silambarasur
 *
 */
public interface TestCaseEntityGroupService {

	void addTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups);
	void updateTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups);
	List<TestCaseEntityGroup> getAllTestCaseEntityGroups(Integer productId);
	List<TestCaseEntityGroup> getTestCaseEntityGroupsByParentEntityId(Integer parentEntityId);
	TestCaseEntityGroup getTestCaseEntityGrouById(Integer testCaseEntityGroupId);
	List<TestCaseEntityGroup> getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(Integer productId,Integer testCaseEntityGroupId);
	TestCaseEntityGroup getTestCaseEntityGroupByName(String testCaseEntityGroupName);
	List<Object[]> getUnMappedTestCasesByProductId(int productId, int testScenarioId, int jtStartIndex, int jtPageSize);
	List<Object[]> getMappedTestCasesByTestScenarioId(int testScenarioId);
	int getUnMappedTestCasesCountByProductId(int productId, int testScenarioId);
	void updateTestCaseToTestScenario(Integer testScenarioId, Integer testCaseId, UserList userList, String maporunmap);
	public TestCaseEntityGroupHasTestCase getTestScenarioAssociationByIds(Integer testScenarioId, Integer testCaseId);
	List<TestCaseEntityGroupHasTestCase> getMappedTestCaseAssociationByTestcaseScenarioId(Integer testCaseEntityGroupId);
}
