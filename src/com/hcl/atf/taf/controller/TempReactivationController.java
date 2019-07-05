package com.hcl.atf.taf.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestEnvironmentDeviceService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
@Controller
public class TempReactivationController {
	private static final Log log = LogFactory.getLog(TempReactivationController.class);
	
	@Autowired
	private ProductListService productListService;	
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestEnvironmentDeviceService testEnvironmentDeviceService;
	
	@RequestMapping(value="reactivateEntities",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions reactivateEntity(String entityType, int entityId) {
		log.debug("Reactivating entities");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			if(entityType.equals(TAFConstants.ENTITY_PRODUCT)){
				productListService.reactivateProduct(entityId);
			} else if(entityType.equals(TAFConstants.ENTITY_PRODUCT_VERSION)){
				productListService.reactivateProductVersion(entityId);
			} else if(entityType.equals(TAFConstants.ENTITY_TEST_RUN_CONFIGURATION_CHILD)){
				testRunConfigurationService.reactivateTestRunConfigurationChild(entityId);
			} else if(entityType.equals(TAFConstants.ENTITY_TEST_SUITE)){
				testSuiteConfigurationService.reactivateTestSuite(entityId);
			}
			else if(entityType.equals(TAFConstants.ENTITY_TEST_ENVIRONMENT)){
				testEnvironmentDeviceService.reactivateTestEnvironment(entityId);
			}
			jTableResponseOptions = new JTableResponseOptions("OK");
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Reactivating Entities!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}

}
