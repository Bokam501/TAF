/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestCaseToVersionMappingDAO;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;
import com.hcl.atf.taf.service.TestCaseToVersionMappingService;

/**
 * @author silambarasur
 *
 */
@Service
public class TestCaseToVersionMappingServiceImpl implements TestCaseToVersionMappingService{
	
	private static final Log log = LogFactory.getLog(TestCaseToVersionMappingServiceImpl.class);

	@Autowired
	private TestCaseToVersionMappingDAO testCaseToVersionMappingDAO;
	
	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestCaseListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId) {
		try {
			return testCaseToVersionMappingDAO.getTestCaseListByProductIdAndVersionIdAndBuild(productId, versionId);
		}catch(Exception e) {
			log.error("Error in getTestCaseListByProductIdAndVersionIdAndBuild",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId,Integer jtStartIndex, Integer jtPageSize) {
		try {
			return testCaseToVersionMappingDAO.getTestCaseByProductIdAndVersionIdAndBuild(productId, versionId,jtStartIndex, jtPageSize);
		}catch(Exception e) {
			log.error("Error in getTestCaseByProductIdAndVersionIdAndBuild",e);
		}
		return null;
	}

	@Override
	@Transactional
	public void unMappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping) {
		try {
			testCaseToVersionMappingDAO.unMappingTestCaseToProductVersion(testCaseProductVersionMapping);
		}catch(Exception e) {
			log.error("Error in unMappingTestCaseToProductVersion",e);
		}
		
	}

	@Override
	@Transactional
	public void mappingTestCaseToProductVersion(TestCaseProductVersionMapping testCaseProductVersionMapping) {
		try {
			testCaseToVersionMappingDAO.mappingTestCaseToProductVersion(testCaseProductVersionMapping);
		}catch(Exception e) {
			log.error("Error in mappingTestCaseToProductVersion",e);
		}
		
	}

	@Override
	@Transactional
	public List<TestCaseProductVersionMapping> getTestcaseToProductVersionMappingList(Integer productId) {
		try {
			return testCaseToVersionMappingDAO.getTestcaseToProductVersionMappingList(productId);
		}catch(Exception e) {
			log.error("Error in getTestcaseToProductVersionMappingList",e);
		}
		return null;
	}

}
