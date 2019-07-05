
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseEntityGroupHasTestCase;

public interface TestCaseEntityGroupsDAO {
	
	void addTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups);
	void updateTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups);
	List<TestCaseEntityGroup> getAllTestCaseEntityGroups(Integer productId);
	List<TestCaseEntityGroup> getTestCaseEntityGroupsByParentEntityId(Integer parentEntityId);
	TestCaseEntityGroup getTestCaseEntityGrouById(Integer testCaseEntityGroupId);
	TestCaseEntityGroup getTestCaseEntityGroupByName(String testCaseEntityGroupName);
	List<TestCaseEntityGroup> getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(Integer productId,Integer testCaseEntityGroupId);
	TestCaseEntityGroup getRootTestCaseEntityGroup();
	List<Object[]> getUnMappedTestCasesByProductId(int productId, int testScenarioId, int jtStartIndex, int jtPageSize);
	List<Object[]> getMappedTestCasesByTestScenarioId(int testScenarioId);
	int getUnMappedTestCasesCountByProductId(int productId, int testScenarioId);
	void updateTestCaseToTestScenario(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase, String maporunmap);
	TestCaseEntityGroupHasTestCase getTestCaseScriptAssociationByIds(Integer testScenarioId, Integer testCaseId);
	Integer addTestScenarioAssociation(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase);
	void deleteTestScenarioAssociation(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase);
	List<TestCaseEntityGroupHasTestCase> getMappedTestCaseAssociationByTestcaseScenarioId(Integer testcaseSenarioId);
}
