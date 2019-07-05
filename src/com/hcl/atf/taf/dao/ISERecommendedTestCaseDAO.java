/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;

/**
 * @author silambarasur
 *
 */
public interface ISERecommendedTestCaseDAO {
	
	//List<ISERecommandedTestcases> getISERecommendatedTestCaseListByBuildId(Integer buildId);
	List<Object[]> getISERecommendatedTestCaseListByBuildId(Integer buildId);
	ISERecommandedTestcases getISERecommendatedTestCaseListByBuildIdAndTestCaseId(Integer buildId,Integer testCaseId); 
	void addISERecommendedTestCase(ISERecommandedTestcases iseRecommandedTestcases);
	void updateISERecommendedTestCase(ISERecommandedTestcases iseRecommandedTestcases);
	void removeISERecommendationTestcases(Integer buildId);
	List<Object[]> getISERecommendatedTestCaseCategoryCountByBuildId(Integer buildId);
	Integer getISERecommendatedTestCaseCountByBuildId(Integer buildId);

}
