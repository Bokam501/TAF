package com.hcl.atf.taf.model.json;

import java.lang.reflect.Field;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ExecutionMode;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonTestEngineLanguageMode {

	private static final Log log = LogFactory.getLog(JsonTestEngineLanguageMode.class);
	@JsonProperty
	private String languageName;
	@JsonProperty
	private String appiumScriptless;
	@JsonProperty
	private String appiumScriptGeneration;
	@JsonProperty
	private String appiumScriptGenTAF;
	@JsonProperty
	private String appiumTestExecution;
	@JsonProperty
	private String seetestScriptless;
	@JsonProperty
	private String seetestScriptGeneration;
	@JsonProperty
	private String seetestScriptGenTAF;
	@JsonProperty
	private String seetestTestExecution;
	@JsonProperty
	private String seleniumScriptless;
	@JsonProperty
	private String seleniumScriptGeneration;
	@JsonProperty
	private String seleniumScriptGenTAF;
	@JsonProperty
	private String seleniumTestExecution;
	@JsonProperty
	private String edatScriptless;
	@JsonProperty
	private String edatScriptGeneration;
	@JsonProperty
	private String edatScriptGenTAF;
	@JsonProperty
	private String edatTestExecution;
	@JsonProperty
	private String codeduiScriptless;
	@JsonProperty
	private String codeduiScriptGeneration;
	@JsonProperty
	private String codeduiScriptGenTAF;
	@JsonProperty
	private String codeduiTestExecution;
	@JsonProperty
	private String protractorScriptless;
	@JsonProperty
	private String protractorScriptGeneration;
	@JsonProperty
	private String protractorScriptGenTAF;
	@JsonProperty
	private String protractorTestExecution;
	@JsonProperty
	private String testcompleteScriptless;
	@JsonProperty
	private String testcompleteScriptGeneration;
	@JsonProperty
	private String testcompleteScriptGenTAF;
	@JsonProperty
	private String testcompleteTestExecution;
	@JsonProperty
	private String restAssuredScriptless;
	@JsonProperty
	private String restAssuredScriptGeneration;
	@JsonProperty
	private String restAssuredScriptGenTAF;
	@JsonProperty
	private String restAssuredTestExecution;
	@JsonProperty
	private String modifiedFieldTitle;
	@JsonProperty
	private String oldFieldValue;
	@JsonProperty
	private String modifiedFieldValue;

	//Always add variables before this line.variable name should start with engine name in lower case

	public JsonTestEngineLanguageMode getJsonTestEngineLanguageMode(Languages language){	
		JsonTestEngineLanguageMode js = new JsonTestEngineLanguageMode();
		try{
			for(TestToolMaster toolMaster : language.getTestEngines()){	
				String engineName = toolMaster.getTestToolName();
				for(ExecutionMode eMode : toolMaster.getExecutionModes()){
					for (Field f : js.getClass().getDeclaredFields()) {
						if(f.getName().startsWith("languageName"))
							f.set(js,language.getName());						
						if("SCRIPTLESS".equalsIgnoreCase(eMode.getName()) && f.getName().toLowerCase().startsWith(engineName.toLowerCase()) && f.getName().contains("Scriptless")){
							f.set(js, "Yes");
						}

						if("GENERIC".equalsIgnoreCase(eMode.getName()) && f.getName().toLowerCase().startsWith(engineName.toLowerCase()) && f.getName().contains("ScriptGeneration")){
							f.set(js, "yes");
						}
						if("Test Execution".equalsIgnoreCase(eMode.getName()) && f.getName().toLowerCase().startsWith(engineName.toLowerCase()) && f.getName().contains("TestExecution")){
							f.set(js, "yes");
						}
						if("TAF-MODE".equalsIgnoreCase(eMode.getName()) && f.getName().toLowerCase().startsWith(engineName.toLowerCase()) && f.getName().contains("ScriptGenTAF")){
							f.set(js, "yes");
						}
					}				
				}	
			}
			for (Field f : js.getClass().getDeclaredFields()) {
				if(f.get(js) == null){
					f.set(js, "No");
				}
			}
		}catch(Exception e){
			log.error("Unknown error "+e.getMessage());
		}
		return js;
	}

	public JsonTestEngineLanguageMode() {
		// TODO Auto-generated constructor stub
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getSeleniumScriptless() {
		return seleniumScriptless;
	}

	public void setSeleniumScriptless(String seleniumScriptless) {
		this.seleniumScriptless = seleniumScriptless;
	}

	public String getSeleniumScriptGeneration() {
		return seleniumScriptGeneration;
	}

	public void setSeleniumScriptGeneration(String seleniumScriptGeneration) {
		this.seleniumScriptGeneration = seleniumScriptGeneration;
	}

	public String getAppiumScriptless() {
		return appiumScriptless;
	}

	public void setAppiumScriptless(String appiumScriptless) {
		this.appiumScriptless = appiumScriptless;
	}

	public String getAppiumScriptGeneration() {
		return appiumScriptGeneration;
	}

	public void setAppiumScriptGeneration(String appiumScriptGeneration) {
		this.appiumScriptGeneration = appiumScriptGeneration;
	}

	public String getSeetestScriptless() {
		return seetestScriptless;
	}

	public void setSeetestScriptless(String seetestScriptless) {
		this.seetestScriptless = seetestScriptless;
	}

	public String getSeetestScriptGeneration() {
		return seetestScriptGeneration;
	}

	public void setSeetestScriptGeneration(String seetestScriptGeneration) {
		this.seetestScriptGeneration = seetestScriptGeneration;
	}

	public String getProtractorScriptless() {
		return protractorScriptless;
	}

	public void setProtractorScriptless(String protractorScriptless) {
		this.protractorScriptless = protractorScriptless;
	}

	public String getProtractorScriptGeneration() {
		return protractorScriptGeneration;
	}

	public void setProtractorScriptGeneration(String protractorScriptGeneration) {
		this.protractorScriptGeneration = protractorScriptGeneration;
	}

	public String getEdatScriptless() {
		return edatScriptless;
	}

	public void setEdatScriptless(String edatScriptless) {
		this.edatScriptless = edatScriptless;
	}

	public String getEdatScriptGeneration() {
		return edatScriptGeneration;
	}

	public void setEdatScriptGeneration(String edatScriptGeneration) {
		this.edatScriptGeneration = edatScriptGeneration;
	}

	public String getCodeduiScriptless() {
		return codeduiScriptless;
	}

	public void setCodeduiScriptless(String codeduiScriptless) {
		this.codeduiScriptless = codeduiScriptless;
	}

	public String getCodeduiScriptGeneration() {
		return codeduiScriptGeneration;
	}

	public void setCodeduiScriptGeneration(String codeduiScriptGeneration) {
		this.codeduiScriptGeneration = codeduiScriptGeneration;
	}

	public String getSeleniumTestExecution() {
		return seleniumTestExecution;
	}

	public void setSeleniumTestExecution(String seleniumTestExecution) {
		this.seleniumTestExecution = seleniumTestExecution;
	}

	public String getSeleniumScriptGenTAF() {
		return seleniumScriptGenTAF;
	}

	public void setSeleniumScriptGenTAF(String seleniumScriptGenTAF) {
		this.seleniumScriptGenTAF = seleniumScriptGenTAF;
	}

	public String getAppiumTestExecution() {
		return appiumTestExecution;
	}

	public void setAppiumTestExecution(String appiumTestExecution) {
		this.appiumTestExecution = appiumTestExecution;
	}

	public String getAppiumScriptGenTAF() {
		return appiumScriptGenTAF;
	}

	public void setAppiumScriptGenTAF(String appiumScriptGenTAF) {
		this.appiumScriptGenTAF = appiumScriptGenTAF;
	}

	public String getSeetestTestExecution() {
		return seetestTestExecution;
	}

	public void setSeetestTestExecution(String seetestTestExecution) {
		this.seetestTestExecution = seetestTestExecution;
	}

	public String getSeetestScriptGenTAF() {
		return seetestScriptGenTAF;
	}

	public void setSeetestScriptGenTAF(String seetestScriptGenTAF) {
		this.seetestScriptGenTAF = seetestScriptGenTAF;
	}

	public String getProtractorTestExecution() {
		return protractorTestExecution;
	}

	public void setProtractorTestExecution(String protractorTestExecution) {
		this.protractorTestExecution = protractorTestExecution;
	}

	public String getProtractorScriptGenTAF() {
		return protractorScriptGenTAF;
	}

	public void setProtractorScriptGenTAF(String protractorScriptGenTAF) {
		this.protractorScriptGenTAF = protractorScriptGenTAF;
	}

	public String getEdatTestExecution() {
		return edatTestExecution;
	}

	public void setEdatTestExecution(String edatTestExecution) {
		this.edatTestExecution = edatTestExecution;
	}

	public String getEdatScriptGenTAF() {
		return edatScriptGenTAF;
	}

	public void setEdatScriptGenTAF(String edatScriptGenTAF) {
		this.edatScriptGenTAF = edatScriptGenTAF;
	}

	public String getCodeduiTestExecution() {
		return codeduiTestExecution;
	}

	public void setCodeduiTestExecution(String codeduiTestExecution) {
		this.codeduiTestExecution = codeduiTestExecution;
	}

	public String getCodeduiScriptGenTAF() {
		return codeduiScriptGenTAF;
	}

	public void setCodeduiScriptGenTAF(String codeduiScriptGenTAF) {
		this.codeduiScriptGenTAF = codeduiScriptGenTAF;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public String getTestcompleteScriptless() {
		return testcompleteScriptless;
	}

	public void setTestcompleteScriptless(String testcompleteScriptless) {
		this.testcompleteScriptless = testcompleteScriptless;
	}

	public String getTestcompleteScriptGeneration() {
		return testcompleteScriptGeneration;
	}

	public void setTestcompleteScriptGeneration(String testcompleteScriptGeneration) {
		this.testcompleteScriptGeneration = testcompleteScriptGeneration;
	}

	public String getTestcompleteScriptGenTAF() {
		return testcompleteScriptGenTAF;
	}

	public void setTestcompleteScriptGenTAF(String testcompleteScriptGenTAF) {
		this.testcompleteScriptGenTAF = testcompleteScriptGenTAF;
	}

	public String getTestcompleteTestExecution() {
		return testcompleteTestExecution;
	}

	public void setTestcompleteTestExecution(String testcompleteTestExecution) {
		this.testcompleteTestExecution = testcompleteTestExecution;
	}
	public String getRestAssuredScriptless() {
		return restAssuredScriptless;
	}

	public void setRestAssuredScriptless(String restAssuredScriptless) {
		this.restAssuredScriptless = restAssuredScriptless;
	}

	public String getRestAssuredScriptGeneration() {
		return restAssuredScriptGeneration;
	}

	public void setRestAssuredScriptGeneration(String restAssuredScriptGeneration) {
		this.restAssuredScriptGeneration = restAssuredScriptGeneration;
	}

	public String getRestAssuredScriptGenTAF() {
		return restAssuredScriptGenTAF;
	}

	public void setRestAssuredScriptGenTAF(String restAssuredScriptGenTAF) {
		this.restAssuredScriptGenTAF = restAssuredScriptGenTAF;
	}

	public String getRestAssuredTestExecution() {
		return restAssuredTestExecution;
	}

	public void setRestAssuredTestExecution(String restAssuredTestExecution) {
		this.restAssuredTestExecution = restAssuredTestExecution;
	}

}
