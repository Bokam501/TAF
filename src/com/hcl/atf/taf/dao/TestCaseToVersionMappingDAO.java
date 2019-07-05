/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;

/**
 * @author silambarasur
 *
 */
public interface TestCaseToVersionMappingDAO {
	List<TestCaseProductVersionMapping> getTestCaseListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId);
	 List<TestCaseList> getTestCaseByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId,Integer jtStartIndex, Integer jtPageSize);
	void unMappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping);
	void mappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping);
	List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingList(Integer productId);
	List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingList();
	List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingId(Integer mappingId);

}
