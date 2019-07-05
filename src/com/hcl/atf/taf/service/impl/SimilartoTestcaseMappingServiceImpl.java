/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.SimilartoTestcaseMappingDAO;
import com.hcl.atf.taf.model.SimilartoTestcaseMapping;
import com.hcl.atf.taf.service.SimilartoTestcaseMappingService;

/**
 * @author silambarasur
 *
 */
@Service
public class SimilartoTestcaseMappingServiceImpl implements SimilartoTestcaseMappingService{

	@Autowired
	private SimilartoTestcaseMappingDAO similartoTestcaseMappingDAO;
	
	@Override
	@Transactional
	public List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByProductId(Integer productId) {
		try {
			return similartoTestcaseMappingDAO.getSimilarToTestcaseMappingListByProductId(productId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public void unMappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping) {
		try {
			similartoTestcaseMappingDAO.unMappingSimilarToTestcase(similartoTestcaseMapping);
		}catch(Exception e) {
			
		}
		
	}

	@Override
	@Transactional
	public void mappingSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping) {
		try {
			similartoTestcaseMappingDAO.mappingSimilarToTestcase(similartoTestcaseMapping);
		}catch(Exception e) {
			
		}
		
	}

	@Override
	@Transactional
	public List<SimilartoTestcaseMapping> getSimilarToTestcaseMappingListByTestcaseId(Integer testCaseId) {
		try {
			return similartoTestcaseMappingDAO.getSimilarToTestcaseMappingListByTestcaseId(testCaseId);
		}catch(Exception e) {
			
		}
		return null;
	}

}
