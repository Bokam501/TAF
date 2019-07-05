/**
 * 
 */
package com.hcl.atf.taf.service;

import org.json.JSONObject;

/**
 * @author silambarasur
 *
 */
public interface CommonTestManagementService {

	public JSONObject addTestcases(String testcasesJson);
	public JSONObject addFeatures(String featuresJson);
	public JSONObject addTestSuites(String testSuitesJson);
	public JSONObject mapTestcasesToTestsuite(String mapJsonObject);
	public JSONObject mapFeatureToTestcases(String mapJsonObject);
}
