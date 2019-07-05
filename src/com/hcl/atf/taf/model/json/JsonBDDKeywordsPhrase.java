package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.Tag;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonBDDKeywordsPhrase {

	@JsonProperty
	private Integer id;
	@JsonProperty
	private String keywordPhrase;
	@JsonProperty
	private String description;
	@JsonProperty
	private String objects;
	@JsonProperty
	private String parameters;
	@JsonProperty
	private Integer isCommon;
	@JsonProperty
	private Integer isMobile;
	@JsonProperty
	private Integer isWeb;
	@JsonProperty
	private Integer isDesktop;
	@JsonProperty
	private Integer isSoftwareCommon;
	@JsonProperty
	private Integer isEmbedded;
	@JsonProperty
	private Integer isSeeTest;
	@JsonProperty
	private Integer isAppium;
	@JsonProperty
	private Integer isCodedui;
	@JsonProperty
	private Integer isTestComplete;
	@JsonProperty
	private Integer isSelenium;
	@JsonProperty
	private Integer isAutoIt;
	@JsonProperty
	private Integer isRobotium;
	@JsonProperty
	private Integer isProtractor;
	@JsonProperty
	private Integer isRestAssured;
	@JsonProperty
	private Integer isEDAT;
	@JsonProperty
	private String tags;
	private String type;
	@JsonProperty
	private Integer languagID;
	@JsonProperty
	private String languageName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer testToolId;
	@JsonProperty
	private String testToolName;
	@JsonProperty
	private String dateAdded;
	@JsonProperty
	private String isSeleniumScriptless;
	@JsonProperty
	private String isSeleniumScripGeneration;
	@JsonProperty
	private String isSeleniumScripGenerationTAF;
	
	@JsonProperty
	private String isAppiumScriptless;
	@JsonProperty
	private String isAppiumScripGeneration;
	@JsonProperty
	private String isAppiumScripGenerationTAF;
	@JsonProperty
	private String isCodeduiScriptless;
	@JsonProperty
	private String isCodeduiScripGeneration;
	@JsonProperty
	private String isCodeduiScripGenerationTAF;
	@JsonProperty
	private String isTestCompleteScriptless;
	@JsonProperty
	private String isTestCompleteScripGeneration;
	@JsonProperty
	private String isTestCompleteScripGenerationTAF;
	@JsonProperty
	private String isSeetestScriptless;
	@JsonProperty
	private String isSeetestScripGeneration;
	@JsonProperty
	private String isSeetestScripGenerationTAF;
	@JsonProperty
	private String isProtractorScriptless;
	@JsonProperty
	private String isProtractorScripGeneration;
	@JsonProperty
	private String isProtractorScripGenerationTAF;
	@JsonProperty
	private String isEDATScriptless;
	@JsonProperty
	private String isEDATScripGeneration;
	@JsonProperty
	private String isEDATScripGenerationTAF;
	@JsonProperty
	private String isRestAssuredScriptless;
	@JsonProperty
	private String isRestAssuredScripGeneration;
	@JsonProperty
	private String isRestAssuredScripGenerationTAF;
	@JsonProperty
	private String keywordRegularExpression;
	@JsonProperty
	private Integer tagId;
	@JsonProperty
	private String tagName;
	@JsonProperty
	private String isTestCompleteWebScriptless;
	@JsonProperty
	private String isTestCompleteWebScripGeneration;
	@JsonProperty
	private String isTestCompleteWebScripGenerationTAF;
	@JsonProperty
	private Integer isDevice;
	private Integer isCustomCisco;
	private String isCustomCiscoScriptless;
	private String isCustomCiscoScripGeneration;
	private String isCustomCiscoScripGenerationTAF;
	
	public JsonBDDKeywordsPhrase() {}

	public JsonBDDKeywordsPhrase(BDDKeywordsPhrases bDDKeyWordsPhrases) {
		
		this.id = bDDKeyWordsPhrases.getId();
		this.keywordPhrase = bDDKeyWordsPhrases.getKeywordPhrase();
		this.description = bDDKeyWordsPhrases.getDescription();
		this.objects = bDDKeyWordsPhrases.getObjects();
		this.parameters = bDDKeyWordsPhrases.getParameters();
		this.isCommon = bDDKeyWordsPhrases.getIsCommon();
		this.isMobile = bDDKeyWordsPhrases.getIsMobile();
		this.isWeb = bDDKeyWordsPhrases.getIsWeb();
		this.isDesktop = bDDKeyWordsPhrases.getIsDesktop();
		this.isSoftwareCommon = bDDKeyWordsPhrases.getIsSoftwareCommon();
		this.isEmbedded = bDDKeyWordsPhrases.getIsEmbedded();
		this.isSeeTest = bDDKeyWordsPhrases.getIsSeeTest();
		this.isAppium = bDDKeyWordsPhrases.getIsAppium();
		this.isCodedui = bDDKeyWordsPhrases.getIsCodedui();
		this.isTestComplete = bDDKeyWordsPhrases.getIsTestComplete();
		this.isSelenium = bDDKeyWordsPhrases.getIsSelenium();
		this.isAutoIt = bDDKeyWordsPhrases.getIsRobotium();
		this.isRobotium = bDDKeyWordsPhrases.getIsRobotium();
		this.isProtractor = bDDKeyWordsPhrases.getIsProtractor();
		this.isEDAT = bDDKeyWordsPhrases.getIsEdat();
		this.isRestAssured = bDDKeyWordsPhrases.getIsRestAssured();
		this.status = bDDKeyWordsPhrases.getStatus();	
		this.isDevice = bDDKeyWordsPhrases.getIsDevice();
		this.isCustomCisco = bDDKeyWordsPhrases.getIsCustomCisco();
		int count = 0 ;
		if(bDDKeyWordsPhrases.getIsAppiumScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsAppiumScripGeneration() == 1){
				isAppiumScripGeneration ="Yes";
			}else{
				isAppiumScripGeneration ="No";
			}			
		}else{
			isAppiumScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsCodeduiScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsCodeduiScripGeneration() == 1){
				isCodeduiScripGeneration ="Yes";
			}else{
				isCodeduiScripGeneration ="No";
			}			
		}else{
			isCodeduiScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteScripGeneration() == 1){
				isTestCompleteScripGeneration ="Yes";
			}else{
				isTestCompleteScripGeneration ="No";
			}			
		}else{
			isTestCompleteScripGeneration ="No";
		}
		if(bDDKeyWordsPhrases.getIsAppiumScriptless() != null){
			if(bDDKeyWordsPhrases.getIsAppiumScriptless() == 1){
				isAppiumScriptless ="Yes";
			}else{
				isAppiumScriptless ="No";
			}			
		}else{
			isAppiumScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsCodeduiScriptless() != null){
			if(bDDKeyWordsPhrases.getIsCodeduiScriptless() == 1){
				isCodeduiScriptless ="Yes";
			}else{
				isCodeduiScriptless ="No";
			}			
		}else{
			isCodeduiScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteScriptless() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteScriptless() == 1){
				isTestCompleteScriptless ="Yes";
			}else{
				isTestCompleteScriptless ="No";
			}			
		}else{
			isTestCompleteScriptless ="No";
		}
		if(bDDKeyWordsPhrases.getIsSeleniumScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsSeleniumScripGeneration() == 1){
				isSeleniumScripGeneration ="Yes";
			}else{
				isSeleniumScripGeneration ="No";
			}			
		}else{
			isSeleniumScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsSeleniumScriptless() != null){
			if(bDDKeyWordsPhrases.getIsSeleniumScriptless() == 1){
				isSeleniumScriptless ="Yes";
			}else{
				isSeleniumScriptless ="No";
			}			
		}else{
			isSeleniumScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsSeetestScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsSeetestScripGeneration() == 1){
				isSeetestScripGeneration ="Yes";
			}else{
				isSeetestScripGeneration ="No";
			}		
		}else{
			isSeetestScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsSeetestScriptless() != null){
			if(bDDKeyWordsPhrases.getIsSeetestScriptless() == 1){
				isSeetestScriptless ="Yes";
			}else{
				isSeetestScriptless ="No";
			}			
		}else{
			isSeetestScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsProtractorScriptless() != null){
			if(bDDKeyWordsPhrases.getIsProtractorScriptless() == 1){
				isProtractorScriptless ="Yes";
			}else{
				isProtractorScriptless ="No";
			}			
		}else{
			isProtractorScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsProtractorScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsProtractorScripGeneration() == 1){
				isProtractorScripGeneration ="Yes";
			}else{
				isProtractorScripGeneration ="No";
			}			
		}else{
			isProtractorScripGeneration ="No";
		}		
		
		if(bDDKeyWordsPhrases.getIsEDATScriptless() != null){
			if(bDDKeyWordsPhrases.getIsEDATScriptless() == 1){
				isEDATScriptless ="Yes";
			}else{
				isEDATScriptless ="No";
			}			
		}else{
			isEDATScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsEDATScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsEDATScripGeneration() == 1){
				isEDATScripGeneration ="Yes";
			}else{
				isEDATScripGeneration ="No";
			}			
		}else{
			isEDATScripGeneration ="No";
		}	
		
		if(bDDKeyWordsPhrases.getIsSeleniumScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsSeleniumScripGenerationTAF() == 1){
				isSeleniumScripGenerationTAF ="Yes";
			}else{
				isSeleniumScripGenerationTAF ="No";
			}			
		}else{
			isSeleniumScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsAppiumScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsAppiumScripGenerationTAF() == 1){
				isAppiumScripGenerationTAF ="Yes";
			}else{
				isAppiumScripGenerationTAF ="No";
			}			
		}else{
			isAppiumScripGenerationTAF ="No";
		}
		
		
		if(bDDKeyWordsPhrases.getIsSeetestScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsSeetestScripGenerationTAF() == 1){
				isSeetestScripGenerationTAF ="Yes";
			}else{
				isSeetestScripGenerationTAF ="No";
			}			
		}else{
			isSeetestScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsProtractorScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsProtractorScripGenerationTAF() == 1){
				isProtractorScripGenerationTAF ="Yes";
			}else{
				isProtractorScripGenerationTAF ="No";
			}			
		}else{
			isProtractorScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsEDATScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsEDATScripGenerationTAF() == 1){
				isEDATScripGenerationTAF ="Yes";
			}else{
				isEDATScripGenerationTAF ="No";
			}			
		}else{
			isEDATScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsCodeduiScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsCodeduiScripGenerationTAF() == 1){
				isCodeduiScripGenerationTAF ="Yes";
			}else{
				isCodeduiScripGenerationTAF ="No";
			}			
		}else{
			isCodeduiScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteScripGenerationTAF() == 1){
				isTestCompleteScripGenerationTAF ="Yes";
			}else{
				isTestCompleteScripGenerationTAF ="No";
			}			
		}else{
			isTestCompleteScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsRestAssuredScriptless() != null){
			if(bDDKeyWordsPhrases.getIsRestAssuredScriptless() == 1){
				isRestAssuredScriptless ="Yes";
			}else{
				isRestAssuredScriptless ="No";
			}			
		}else{
			isRestAssuredScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsRestAssuredScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsRestAssuredScripGeneration() == 1){
				isRestAssuredScripGeneration ="Yes";
			}else{
				isRestAssuredScripGeneration ="No";
			}		
		}else{
			isRestAssuredScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsRestAssuredScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsRestAssuredScripGenerationTAF() == 1){
				isRestAssuredScripGenerationTAF ="Yes";
			}else{
				isRestAssuredScripGenerationTAF ="No";
			}			
		}else{
			isRestAssuredScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteWebScriptless() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteWebScriptless() == 1){
				isTestCompleteWebScriptless ="Yes";
			}else{
				isTestCompleteWebScriptless ="No";
			}			
		}else{
			isTestCompleteWebScriptless ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteWebScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteWebScripGeneration() == 1){
				isTestCompleteWebScripGeneration ="Yes";
			}else{
				isTestCompleteWebScripGeneration ="No";
			}			
		}else{
			isTestCompleteWebScripGeneration ="No";
		}
		
		if(bDDKeyWordsPhrases.getIsTestCompleteWebScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsTestCompleteWebScripGenerationTAF() == 1){
				isTestCompleteWebScripGenerationTAF ="Yes";
			}else{
				isTestCompleteWebScripGenerationTAF ="No";
			}			
		}else{
			isTestCompleteWebScripGenerationTAF ="No";
		}
		
		if(bDDKeyWordsPhrases.getKeywordRegularExpression() != null && bDDKeyWordsPhrases.getKeywordRegularExpression() !=""){
			keywordRegularExpression = bDDKeyWordsPhrases.getKeywordRegularExpression();
		}	
		
		if(bDDKeyWordsPhrases.getIsCustomCiscoScripGeneration() != null){
			if(bDDKeyWordsPhrases.getIsCustomCiscoScripGeneration() == 1){
				isCustomCiscoScripGeneration ="Yes";
			}else{
				isCustomCiscoScripGeneration ="No";
			}			
		}else{
			isCustomCiscoScripGeneration ="No";
		}
		if(bDDKeyWordsPhrases.getIsCustomCiscoScripGenerationTAF() != null){
			if(bDDKeyWordsPhrases.getIsCustomCiscoScripGenerationTAF() == 1){
				isCustomCiscoScripGenerationTAF ="Yes";
			}else{
				isCustomCiscoScripGenerationTAF ="No";
			}			
		}else{
			isCustomCiscoScripGenerationTAF ="No";
		}
		if(bDDKeyWordsPhrases.getIsCustomCiscoScriptless() != null){
			if(bDDKeyWordsPhrases.getIsCustomCiscoScriptless() == 1){
				isCustomCiscoScriptless ="Yes";
			}else{
				isCustomCiscoScriptless ="No";
			}			
		}else{
			isCustomCiscoScriptless ="No";
		}
	}
	
	public JsonBDDKeywordsPhrase(Tag tag) {
		tagId = tag.getTagId();
		tagName = tag.getTagName();
	}
	
	@JsonIgnore
	public BDDKeywordsPhrases getJsonBDDKeywordsPhrase(){
		BDDKeywordsPhrases bddKeywrd=new BDDKeywordsPhrases();
		if(id != null ){
			bddKeywrd.setId(id);
		}		
		bddKeywrd.setKeywordPhrase(keywordPhrase);
		bddKeywrd.setDescription(description);
		bddKeywrd.setObjects(objects);
		bddKeywrd.setParameters(parameters);
		bddKeywrd.setIsCommon(0);
		bddKeywrd.setIsMobile(0);
		bddKeywrd.setIsWeb(0);
		bddKeywrd.setIsDesktop(0);
		bddKeywrd.setIsSoftwareCommon(0);
		bddKeywrd.setIsEmbedded(0);
		bddKeywrd.setIsSeeTest(0);
		bddKeywrd.setIsAppium(0);
		bddKeywrd.setIsCodedui(0);
		bddKeywrd.setIsSelenium(0);
		bddKeywrd.setIsAutoIt(0);
		bddKeywrd.setIsRobotium(0);
		bddKeywrd.setIsProtractor(0);
		bddKeywrd.setIsEmbedded(0);
		bddKeywrd.setIsRestAssured(0);
		bddKeywrd.setIsTestComplete(0);
		bddKeywrd.setIsEdat(0);
		bddKeywrd.setStatus(0);
		bddKeywrd.setIsDevice(0);
		if(keywordRegularExpression != null && keywordRegularExpression != ""){
			bddKeywrd.setKeywordRegularExpression(keywordRegularExpression);	
		}else{
			bddKeywrd.setKeywordRegularExpression(null);
		}	
		return bddKeywrd;
	}
	
	@JsonIgnore
	public BDDKeywordsPhrases getUpdateJsonBDDKeywordsPhrase(){
		BDDKeywordsPhrases bddKeywrd=new BDDKeywordsPhrases();
		if(id != null ){
			bddKeywrd.setId(id);
		}		
		bddKeywrd.setKeywordPhrase(keywordPhrase);
		bddKeywrd.setDescription(description);
		bddKeywrd.setObjects(objects);
		bddKeywrd.setParameters(parameters);
		bddKeywrd.setIsCommon(isCommon);
		bddKeywrd.setIsMobile(isMobile);
		bddKeywrd.setIsWeb(isWeb);
		bddKeywrd.setIsDesktop(isDesktop);
		bddKeywrd.setIsSoftwareCommon(isSoftwareCommon);
		bddKeywrd.setIsEmbedded(isEmbedded);
		bddKeywrd.setIsSeeTest(isSeeTest);
		bddKeywrd.setIsAppium(isAppium);
		bddKeywrd.setIsCodedui(isCodedui);
		bddKeywrd.setIsSelenium(isSelenium);
		bddKeywrd.setIsAutoIt(isAutoIt);
		bddKeywrd.setIsRobotium(isRobotium);
		bddKeywrd.setIsProtractor(isProtractor);
		bddKeywrd.setIsEmbedded(isEmbedded);
		bddKeywrd.setIsRestAssured(isRestAssured);
		bddKeywrd.setStatus(status);
		bddKeywrd.setIsDevice(isDevice);
		bddKeywrd.setIsCustomCisco(isCustomCisco);
		if(keywordRegularExpression != null && keywordRegularExpression != ""){
			bddKeywrd.setKeywordRegularExpression(keywordRegularExpression);	
		}else{
			bddKeywrd.setKeywordRegularExpression(null);
		}	
		return bddKeywrd;
	}
	
	@JsonIgnore
	public KeywordLibrary getKeywordLibrary(){		
		KeywordLibrary keyWordLibrary=new KeywordLibrary();		
		if (this.testToolId != null) {
			TestToolMaster testToolMaster=new TestToolMaster();
			testToolMaster.setTestToolId(this.testToolId);
			keyWordLibrary.setTestToolMaster(testToolMaster);			
		}
		
		if(this.languagID!=null){
			Languages languages=new Languages();
			languages.setId(this.languagID);
			keyWordLibrary.setLanguage(languages);
		}
		
		if(type != null){
			keyWordLibrary.setType(type);
		}
		
		if(this.status == null){
			
			keyWordLibrary.setStatus("new");
		}
		
		if(id != null){
			BDDKeywordsPhrases keyword = new BDDKeywordsPhrases();
			keyword.setId(id);
			keyWordLibrary.setKeywords(keyword);
		}
		
		keyWordLibrary.setDateAdded(new Date());		
		return keyWordLibrary;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLanguagID() {
		return languagID;
	}

	public void setLanguagID(Integer languagID) {
		this.languagID = languagID;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public Integer getTestToolId() {
		return testToolId;
	}

	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}

	public String getTestToolName() {
		return testToolName;
	}

	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeywordPhrase() {
		return keywordPhrase;
	}

	public void setKeywordPhrase(String keywordPhrase) {
		this.keywordPhrase = keywordPhrase;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjects() {
		return objects;
	}

	public void setObjects(String objects) {
		this.objects = objects;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Integer getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Integer isCommon) {
		this.isCommon = isCommon;
	}

	public Integer getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}

	public Integer getIsWeb() {
		return isWeb;
	}

	public void setIsWeb(Integer isWeb) {
		this.isWeb = isWeb;
	}

	public Integer getIsDesktop() {
		return isDesktop;
	}

	public void setIsDesktop(Integer isDesktop) {
		this.isDesktop = isDesktop;
	}

	public Integer getIsSoftwareCommon() {
		return isSoftwareCommon;
	}

	public void setIsSoftwareCommon(Integer isSoftwareCommon) {
		this.isSoftwareCommon = isSoftwareCommon;
	}

	public Integer getIsEmbedded() {
		return isEmbedded;
	}

	public void setIsEmbedded(Integer isEmbedded) {
		this.isEmbedded = isEmbedded;
	}

	public Integer getIsSeeTest() {
		return isSeeTest;
	}

	public void setIsSeeTest(Integer isSeeTest) {
		this.isSeeTest = isSeeTest;
	}

	public Integer getIsAppium() {
		return isAppium;
	}

	public void setIsAppium(Integer isAppium) {
		this.isAppium = isAppium;
	}

	public Integer getIsSelenium() {
		return isSelenium;
	}

	public void setIsSelenium(Integer isSelenium) {
		this.isSelenium = isSelenium;
	}

	public Integer getIsAutoIt() {
		return isAutoIt;
	}

	public void setIsAutoIt(Integer isAutoIt) {
		this.isAutoIt = isAutoIt;
	}

	public Integer getIsRobotium() {
		return isRobotium;
	}

	public void setIsRobotium(Integer isRobotium) {
		this.isRobotium = isRobotium;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getIsSeleniumScriptless() {
		return isSeleniumScriptless;
	}

	public void setIsSeleniumScriptless(String isSeleniumScriptless) {
		this.isSeleniumScriptless = isSeleniumScriptless;
	}

	public String getIsSeleniumScripGeneration() {
		return isSeleniumScripGeneration;
	}

	public void setIsSeleniumScripGeneration(String isSeleniumScripGeneration) {
		this.isSeleniumScripGeneration = isSeleniumScripGeneration;
	}

	public String getIsAppiumScriptless() {
		return isAppiumScriptless;
	}

	public void setIsAppiumScriptless(String isAppiumScriptless) {
		this.isAppiumScriptless = isAppiumScriptless;
	}

	public String getIsAppiumScripGeneration() {
		return isAppiumScripGeneration;
	}

	public void setIsAppiumScripGeneration(String isAppiumScripGeneration) {
		this.isAppiumScripGeneration = isAppiumScripGeneration;
	}

	public String getIsSeetestScriptless() {
		return isSeetestScriptless;
	}

	public void setIsSeetestScriptless(String isSeetestScriptless) {
		this.isSeetestScriptless = isSeetestScriptless;
	}

	public String getIsSeetestScripGeneration() {
		return isSeetestScripGeneration;
	}

	public void setIsSeetestScripGeneration(String isSeetestScripGeneration) {
		this.isSeetestScripGeneration = isSeetestScripGeneration;
	}

	public String getKeywordRegularExpression() {
		return keywordRegularExpression;
	}

	public void setKeywordRegularExpression(String keywordRegularExpression) {
		this.keywordRegularExpression = keywordRegularExpression;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getIsCodedui() {
		return isCodedui;
	}

	public String getIsCodeduiScriptless() {
		return isCodeduiScriptless;
	}

	public String getIsCodeduiScripGeneration() {
		return isCodeduiScripGeneration;
	}

	public void setIsCodedui(Integer isCodedui) {
		this.isCodedui = isCodedui;
	}

	public void setIsCodeduiScriptless(String isCodeduiScriptless) {
		this.isCodeduiScriptless = isCodeduiScriptless;
	}

	public void setIsCodeduiScripGeneration(String isCodeduiScripGeneration) {
		this.isCodeduiScripGeneration = isCodeduiScripGeneration;
	}

	public Integer getIsRestAssured() {
		return isRestAssured;
	}

	public void setIsRestAssured(Integer isRestAssured) {
		this.isRestAssured = isRestAssured;
	}

	public String getIsRestAssuredScriptless() {
		return isRestAssuredScriptless;
	}

	public void setIsRestAssuredScriptless(String isRestAssuredScriptless) {
		this.isRestAssuredScriptless = isRestAssuredScriptless;
	}

	public String getIsRestAssuredScripGeneration() {
		return isRestAssuredScripGeneration;
	}

	public void setIsRestAssuredScripGeneration(String isRestAssuredScripGeneration) {
		this.isRestAssuredScripGeneration = isRestAssuredScripGeneration;
	}

	public String getIsRestAssuredScripGenerationTAF() {
		return isRestAssuredScripGenerationTAF;
	}

	public void setIsRestAssuredScripGenerationTAF(
			String isRestAssuredScripGenerationTAF) {
		this.isRestAssuredScripGenerationTAF = isRestAssuredScripGenerationTAF;
	}

	public String getIsTestCompleteWebScriptless() {
		return isTestCompleteWebScriptless;
	}

	public void setIsTestCompleteWebScriptless(String isTestCompleteWebScriptless) {
		this.isTestCompleteWebScriptless = isTestCompleteWebScriptless;
	}

	public String getIsTestCompleteWebScripGeneration() {
		return isTestCompleteWebScripGeneration;
	}

	public void setIsTestCompleteWebScripGeneration(
			String isTestCompleteWebScripGeneration) {
		this.isTestCompleteWebScripGeneration = isTestCompleteWebScripGeneration;
	}

	public String getIsTestCompleteWebScripGenerationTAF() {
		return isTestCompleteWebScripGenerationTAF;
	}

	public void setIsTestCompleteWebScripGenerationTAF(
			String isTestCompleteWebScripGenerationTAF) {
		this.isTestCompleteWebScripGenerationTAF = isTestCompleteWebScripGenerationTAF;
	}

	public Integer getIsDevice() {
		return isDevice;
	}

	public void setIsDevice(Integer isDevice) {
		this.isDevice = isDevice;
	}

	public String getIsCustomCiscoScriptless() {
		return isCustomCiscoScriptless;
	}

	public String getIsCustomCiscoScripGeneration() {
		return isCustomCiscoScripGeneration;
	}

	public String getIsCustomCiscoScripGenerationTAF() {
		return isCustomCiscoScripGenerationTAF;
	}

	public void setIsCustomCiscoScriptless(String isCustomCiscoScriptless) {
		this.isCustomCiscoScriptless = isCustomCiscoScriptless;
	}

	public void setIsCustomCiscoScripGeneration(String isCustomCiscoScripGeneration) {
		this.isCustomCiscoScripGeneration = isCustomCiscoScripGeneration;
	}

	public void setIsCustomCiscoScripGenerationTAF(String isCustomCiscoScripGenerationTAF) {
		this.isCustomCiscoScripGenerationTAF = isCustomCiscoScripGenerationTAF;
	}

	public Integer getIsCustomCisco() {
		return isCustomCisco;
	}

	public void setIsCustomCisco(Integer isCustomCisco) {
		this.isCustomCisco = isCustomCisco;
	}

}