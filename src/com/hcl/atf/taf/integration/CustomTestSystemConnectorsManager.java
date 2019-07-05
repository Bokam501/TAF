package com.hcl.atf.taf.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.service.CommonTestManagementService;

@Service
public class CustomTestSystemConnectorsManager {
	
	private static final Log log = LogFactory.getLog(CustomTestSystemConnectorsManager.class);
	
	private String testSystemName;
	private String testSystemVersion;
	private String testSystemType;
	private String customConnectorClassName;
	private ITestSystemConnector connector;

	@Autowired
	private CommonTestManagementService commonTestManagementService;
	
	public CustomTestSystemConnectorsManager(){
		
	}
	
	public boolean initializeTestSystemConnectorsManager(String testSystemName, String testSystemVersion, String testSystemType,String customConnectorClassName) {
		this.testSystemName = testSystemName;
		this.testSystemVersion = testSystemVersion;
		this.testSystemType = testSystemType;
		this.customConnectorClassName=customConnectorClassName;
		if(getConnector() == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isConnectorAvailable(String testSystemName, String testSystemVersion, String testSystemType) {
		
		this.testSystemName = testSystemName;
		this.testSystemVersion = testSystemVersion;
		this.testSystemType = testSystemType;
		
		
		getConnector();
		if (connector == null) {
			log.info("Custom connector is not available for : " + testSystemName + " : " + testSystemVersion + " : " + testSystemType);
			return false;
		} else {
			log.info("Custom connector is available for : " + testSystemName + " : " + testSystemVersion + " : " + testSystemType + " : " + connector.getClass().getName());
			return true;
		}
		
	}

	public ITestSystemConnector getConnector() {

		if (connector != null)
			return connector;

		connector = TAFTestSystemConnectorsFactory.getTestSystemConnector(testSystemName, testSystemVersion, testSystemType,customConnectorClassName);
		return connector;

	}

	/**
	 * Process the JSON and add the features to the product in TAF
	 * 
	 * @param systemConnectionDetailsJson
	 * @param queryJson
	 * @param productId
	 * @return
	 * @throws JSONException
	 */
	public String syncFeaturesIntoTAF(String systemConnectionDetailsJson,String queryJson, Integer productId) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JSONObject responseJson= new JSONObject();
				
		if (getConnector() == null) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Connector is not available for the specified System");
			responseJson.put("Failure_Details", "Connector is not available for the specified System");
			return responseJson.toString();
		}

		try {
			String featuresJson = connector.importFeaturesIntoTAF(systemConnectionDetailsJson, queryJson);
			responseJson = commonTestManagementService.addFeatures(featuresJson);
		} catch (Exception e) {
			log.error(e);
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error while importing features to TAF");
			responseJson.put("Failure_Details", e.getMessage());
		}
		return responseJson.toString();
	}

	public String syncTestcasesIntoTAF(String systemConnectionDetailsJson,String queryJson, Integer productId) throws JSONException {
		JSONObject responseJson= new JSONObject();
		//connector = new CustomConnectorForTesting();
		try {
			if (getConnector() == null){
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Connector is not available for the specified System");
				responseJson.put("Failure_Details", "Connector is not available for the specified System");
				return responseJson.toString();
			}
			String testcasesJson = connector.importTestcasesIntoTAF(systemConnectionDetailsJson, queryJson);
			responseJson=commonTestManagementService.addTestcases(testcasesJson);
		} catch (Exception e) {
			log.error(e);
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error while importing testcases to TAF");
			responseJson.put("Failure_Details", e.getMessage());
		}
		return responseJson.toString();
	}

	public String syncTestsuitesIntoTAF(String systemConnectionDetailsJson,String queryJson, Integer productId) throws JSONException {
		JSONObject responseJson= new JSONObject();
		try {
			if (getConnector() == null){
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Connector is not available for the specified System");
				responseJson.put("Failure_Details", "Connector is not available for the specified System");
				return responseJson.toString();
			}
			String testSuitesJson = connector.importTestSuitesIntoTAF(systemConnectionDetailsJson, queryJson);
			responseJson = commonTestManagementService.addTestSuites(testSuitesJson);
		} catch (Exception e) {
			log.error(e);
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error while importing testsuites to TAF");
			responseJson.put("Failure_Details", e.getMessage());
		}
		return responseJson.toString();
	}
	
	public String syncTestcaseToTestSuiteMappings(String systemConnectionDetailsJson,String queryJson, Integer productId) throws JSONException {
		JSONObject responseJson= new JSONObject();
		try {
			if (getConnector() == null){
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Connector is not available for the specified System");
				responseJson.put("Failure_Details", "Connector is not available for the specified System");
				return responseJson.toString();
			}
			String mapJsonObject = connector.importTestcaseToTestSuiteMappings(systemConnectionDetailsJson, queryJson);
			responseJson = commonTestManagementService.mapTestcasesToTestsuite(mapJsonObject);
		} catch (Exception e) {
			log.error(e);
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error while syncTestcaseToTestSuiteMappings to TAF");
			responseJson.put("Failure_Details", e.getMessage());
		}
		return responseJson.toString();
	}
	
	public String syncFeaturesToTestcasesMappings(String systemConnectionDetailsJson,String queryJson, Integer productId) throws JSONException {
		JSONObject responseJson= new JSONObject();
		try {
			if (getConnector() == null){
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Connector is not available for the specified System");
				responseJson.put("Failure_Details", "Connector is not available for the specified System");
				return responseJson.toString();
			}
			String mapJsonObject = connector.importFeatureToTestcaseMappings(systemConnectionDetailsJson, queryJson);
			responseJson = commonTestManagementService.mapFeatureToTestcases(mapJsonObject);
		} catch (Exception e) {
			log.error(e);
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error while syncFeaturesToTestcasesMappings to TAF");
			responseJson.put("Failure_Details", e.getMessage());
		}
		return responseJson.toString();
	}
	
	public String reportDefectsToSystem(String systemConnectionDetailsJson, String defectsJson) {
		String defectJson="";
		try {
			defectJson=connector.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
		}catch(Exception e) {
			
		}
		return defectJson;
	}	
	
	
	public String reportTestExecutionResultsToSystem(String systemConnectionDetailsJson, String workpackageExecutionsJson){
		String workpackageDetailsJson="";
		try {
			workpackageDetailsJson=connector.reportTestExecutionResultsToSystem(systemConnectionDetailsJson, workpackageExecutionsJson);
		}catch(Exception e) {
			
		}
		return workpackageDetailsJson;
	}
	
	
}
