/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestCaseEntityGroupsDAO;
import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseEntityGroupHasTestCase;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.service.TestCaseEntityGroupService;

/**
 * @author silambarasur
 *
 */
@Service
public class TestCaseEntityGroupServiceImpl implements TestCaseEntityGroupService{

	@Autowired
	private TestCaseEntityGroupsDAO testCaseEntityGroupsDAO;
	
	@Override
	@Transactional
	public void addTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups) {
		try{
			testCaseEntityGroupsDAO.addTestCaseEntityGroup(testCaseEntityGroups);
		}catch(Exception e) {
			
		}
		
	}
	
	@Override
	@Transactional
	public void updateTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups) {
		try {
			testCaseEntityGroupsDAO.updateTestCaseEntityGroup(testCaseEntityGroups);
		}catch(Exception e) {
			
		}
	}
	@Override
	@Transactional
	public List<TestCaseEntityGroup> getAllTestCaseEntityGroups(Integer productId) {
		try {
			return testCaseEntityGroupsDAO.getAllTestCaseEntityGroups(productId);
		}catch(Exception e) {
			
		}
		return null;
	}
	public List<TestCaseEntityGroup> getTestCaseEntityGroupsByParentEntityId(Integer parentEntityId) {
		try {
			return testCaseEntityGroupsDAO.getTestCaseEntityGroupsByParentEntityId(parentEntityId);
		}catch(Exception e) {
			
		}
		return null;
	}
	@Override
	@Transactional
	public TestCaseEntityGroup getTestCaseEntityGrouById(Integer testCaseEntityGroupId) {
		try {
			return testCaseEntityGroupsDAO.getTestCaseEntityGrouById(testCaseEntityGroupId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public List<TestCaseEntityGroup> getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(Integer productId,Integer testCaseEntityGroupId) {
		try {
			return testCaseEntityGroupsDAO.getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(productId,testCaseEntityGroupId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public TestCaseEntityGroup getTestCaseEntityGroupByName(String testCaseEntityGroupName) {
		return testCaseEntityGroupsDAO.getTestCaseEntityGroupByName(testCaseEntityGroupName);
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCasesByProductId(int productId, int testScenarioId, int jtStartIndex, int jtPageSize) {
		return testCaseEntityGroupsDAO.getUnMappedTestCasesByProductId(productId, testScenarioId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCasesByTestScenarioId(int testScenarioId) {
		return testCaseEntityGroupsDAO.getMappedTestCasesByTestScenarioId(testScenarioId);
	}

	@Override
	@Transactional
	public int getUnMappedTestCasesCountByProductId(int productId, int testScenarioId) {
		return testCaseEntityGroupsDAO.getUnMappedTestCasesCountByProductId(productId, testScenarioId);
	}

	@Override
	@Transactional
	public void updateTestCaseToTestScenario(Integer testScenarioId, Integer testCaseId, UserList userList, String maporunmap) {
		TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase = null;
		if (testScenarioId!= null && testCaseId!= null){
			if(maporunmap.equalsIgnoreCase("map")){										
				testCaseEntityGroupHasTestCase = new TestCaseEntityGroupHasTestCase();
				testCaseEntityGroupHasTestCase.setTestCaseEntityGroupId(testScenarioId);
				testCaseEntityGroupHasTestCase.setTestCaseId(testCaseId);
				testCaseEntityGroupHasTestCase.setCreatedBy(userList);
				testCaseEntityGroupHasTestCase.setModifiedBy(userList);
				testCaseEntityGroupHasTestCase.setCreatedDate(new Date());
				testCaseEntityGroupHasTestCase.setModifiedDate(new Date());
				
			}else if(maporunmap.equalsIgnoreCase("unmap")){	
				testCaseEntityGroupHasTestCase = getTestScenarioAssociationByIds(testScenarioId, testCaseId);
			}
		 testCaseEntityGroupsDAO.updateTestCaseToTestScenario(testCaseEntityGroupHasTestCase, maporunmap);
		}
	}
	
	@Override
	@Transactional
	public TestCaseEntityGroupHasTestCase getTestScenarioAssociationByIds(Integer testScenarioId, Integer testCaseId) {
		return testCaseEntityGroupsDAO.getTestCaseScriptAssociationByIds(testScenarioId,testCaseId);
	}

	@Override
	@Transactional
	public List<TestCaseEntityGroupHasTestCase> getMappedTestCaseAssociationByTestcaseScenarioId(Integer testcaseSenarioId) {
		try {
			return testCaseEntityGroupsDAO.getMappedTestCaseAssociationByTestcaseScenarioId(testcaseSenarioId);
		}catch(Exception e) {
			
		}
		return null;
	}
	
}
