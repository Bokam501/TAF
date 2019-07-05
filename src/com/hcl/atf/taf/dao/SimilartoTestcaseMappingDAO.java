/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.SimilartoTestcaseMapping;

/**
 * @author silambarasur
 *
 */
public interface SimilartoTestcaseMappingDAO {
	
	List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByProductId(Integer productId);
	void unMappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping);
	void mappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping);
	List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByTestcaseId(Integer testCaseId);
	

}
