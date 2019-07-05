
package com.hcl.atf.taf.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Tag;
import com.hcl.atf.taf.model.UserList;

@Entity
@Table(name="bdd_keyword_phrases")
public class BDDKeywordsPhrases {
	private Integer id;
	private String keywordPhrase;
	private String description;
	private String objects;
	private String parameters;
	private Integer isCommon;
	private Integer isMobile;
	private Integer isWeb;
	private Integer isDesktop;
	private Integer isSoftwareCommon;
	private Integer isEmbedded;
	private Integer isSeeTest;
	private Integer isAppium;
	private Integer isSelenium;
	private Integer isAutoIt;
	private Integer isRobotium;
	private Integer isEdat;
	private Integer status;
	private String tags;
	private Integer isSeleniumScriptless;
	private Integer isSeleniumScripGeneration;
	private Integer isAppiumScriptless;
	private Integer isAppiumScripGeneration;
	private Integer isSeetestScriptless;
	private Integer isSeetestScripGeneration;
	//aotc changes
	private Integer isCodedui;
	private Integer isProtractor;
	private Integer isTestComplete;
	private Integer isRestAssured;


	private Integer isSeleniumScripGenerationTAF;
	private Integer isAppiumScripGenerationTAF;
	private Integer isSeetestScripGenerationTAF;

	private Integer isProtractorScriptless;
	private Integer isProtractorScripGeneration;
	private Integer isProtractorScripGenerationTAF;
	private Integer isEDATScriptless;
	private Integer isEDATScripGeneration;	
	private Integer isEDATScripGenerationTAF;
	private Integer isCodeduiScriptless;
	private Integer isCodeduiScripGeneration;
	private Integer isCodeduiScripGenerationTAF;
	private Integer isTestCompleteScriptless;
	private Integer isTestCompleteScripGeneration;
	private Integer isTestCompleteScripGenerationTAF;
	private Integer isRestAssuredScriptless;
	private Integer isRestAssuredScripGeneration;
	private Integer isRestAssuredScripGenerationTAF;
	private Integer isTestCompleteWebScriptless;
	private Integer isTestCompleteWebScripGeneration;
	private Integer isTestCompleteWebScripGenerationTAF;
	private String keywordRegularExpression;
	private Integer isDevice;	
	private UserList user;
	private Integer isCustomCisco;
	private Integer isCustomCiscoScriptless;
	private Integer isCustomCiscoScripGeneration;
	private Integer isCustomCiscoScripGenerationTAF;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="keywordPhrase")
	public String getKeywordPhrase() {
		return this.keywordPhrase;
	}

	public void setKeywordPhrase(String keywordPhrase) {
		this.keywordPhrase = keywordPhrase;
	}

	@Column(name="description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="objects")
	public String getObjects() {
		return this.objects;
	}

	public void setObjects(String objects) {
		this.objects = objects;
	}

	@Column(name="parameters")
	public String getParameters() {
		return this.parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name="isCommon")
	public Integer getIsCommon() {
		return this.isCommon;
	}

	public void setIsCommon(Integer isCommon) {
		this.isCommon = isCommon;
	}

	@Column(name="isMobile")
	public Integer getIsMobile() {
		return this.isMobile;
	}

	public void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}

	@Column(name="isWeb")
	public Integer getIsWeb() {
		return this.isWeb;
	}

	public void setIsWeb(Integer isWeb) {
		this.isWeb = isWeb;
	}

	@Column(name="isDesktop")
	public Integer getIsDesktop() {
		return this.isDesktop;
	}

	public void setIsDesktop(Integer isDesktop) {
		this.isDesktop = isDesktop;
	}

	@Column(name="isSoftwareCommon")
	public Integer getIsSoftwareCommon() {
		return this.isSoftwareCommon;
	}

	public void setIsSoftwareCommon(Integer isSoftwareCommon) {
		this.isSoftwareCommon = isSoftwareCommon;
	}

	@Column(name="isEmbedded")
	public Integer getIsEmbedded() {
		return this.isEmbedded;
	}

	public void setIsEmbedded(Integer isEmbedded) {
		this.isEmbedded = isEmbedded;
	}

	@Column(name="isSeeTest")  
	public Integer getIsSeeTest() {
		return this.isSeeTest;
	}

	public void setIsSeeTest(Integer isSeeTest) {
		this.isSeeTest = isSeeTest;
	}

	@Column(name="isAppium")
	public Integer getIsAppium() {
		return this.isAppium;
	}

	public void setIsAppium(Integer isAppium) {
		this.isAppium = isAppium;
	}

	@Column(name="isSelenium")
	public Integer getIsSelenium() {
		return this.isSelenium;
	}

	public void setIsSelenium(Integer isSelenium) {
		this.isSelenium = isSelenium;
	}

	@Column(name="isAutoIt")
	public Integer getIsAutoIt() {
		return this.isAutoIt;
	}

	public void setIsAutoIt(Integer isAutoIt) {
		this.isAutoIt = isAutoIt;
	}

	@Column(name="isRobotium")
	public Integer getIsRobotium() {
		return this.isRobotium;
	}

	public void setIsRobotium(Integer isRobotium) {
		this.isRobotium = isRobotium;
	}

	@Column(name="status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="isSeleniumScriptless")
	public Integer getIsSeleniumScriptless() {
		return isSeleniumScriptless;
	}

	public void setIsSeleniumScriptless(Integer isSeleniumScriptless) {
		this.isSeleniumScriptless = isSeleniumScriptless;
	}
	@Column(name="isSeleniumScripGeneration")
	public Integer getIsSeleniumScripGeneration() {
		return isSeleniumScripGeneration;
	}

	public void setIsSeleniumScripGeneration(Integer isSeleniumScripGeneration) {
		this.isSeleniumScripGeneration = isSeleniumScripGeneration;
	}
	@Column(name="isAppiumScriptless")
	public Integer getIsAppiumScriptless() {
		return isAppiumScriptless;
	}

	public void setIsAppiumScriptless(Integer isAppiumScriptless) {
		this.isAppiumScriptless = isAppiumScriptless;
	}
	@Column(name="isAppiumScripGeneration")
	public Integer getIsAppiumScripGeneration() {
		return isAppiumScripGeneration;
	}

	public void setIsAppiumScripGeneration(Integer isAppiumScripGeneration) {
		this.isAppiumScripGeneration = isAppiumScripGeneration;
	}
	@Column(name="isSeetestScriptless")
	public Integer getIsSeetestScriptless() {
		return isSeetestScriptless;
	}

	public void setIsSeetestScriptless(Integer isSeetestScriptless) {
		this.isSeetestScriptless = isSeetestScriptless;
	}
	@Column(name="isSeetestScripGeneration")
	public Integer getIsSeetestScripGeneration() {
		return isSeetestScripGeneration;
	}

	public void setIsSeetestScripGeneration(Integer isSeetestScripGeneration) {
		this.isSeetestScripGeneration = isSeetestScripGeneration;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}




	@Column(name="isCodedui")
	public Integer getIsCodedui() {
		return isCodedui;
	}

	public void setIsCodedui(Integer isCodedui) {
		this.isCodedui = isCodedui;
	}
	@Column(name="isProtractor")
	public Integer getIsProtractor() {
		return isProtractor;
	}

	public void setIsProtractor(Integer isProtractor) {
		this.isProtractor = isProtractor;
	}
	@Column(name="isTestComplete")
	public Integer getIsTestComplete() {
		return isTestComplete;
	}

	public void setIsTestComplete(Integer isTestComplete) {
		this.isTestComplete = isTestComplete;
	}
	@Column(name="isRestAssured")
	public Integer getIsRestAssured() {
		return isRestAssured;
	}

	public void setIsRestAssured(Integer isRestAssured) {
		this.isRestAssured = isRestAssured;
	}
	@Column(name="isSeleniumScripGenerationTAF")
	public Integer getIsSeleniumScripGenerationTAF() {
		return isSeleniumScripGenerationTAF;
	}

	public void setIsSeleniumScripGenerationTAF(Integer isSeleniumScripGenerationTAF) {
		this.isSeleniumScripGenerationTAF = isSeleniumScripGenerationTAF;
	}
	@Column(name="isAppiumScripGenerationTAF")
	public Integer getIsAppiumScripGenerationTAF() {
		return isAppiumScripGenerationTAF;
	}

	public void setIsAppiumScripGenerationTAF(Integer isAppiumScripGenerationTAF) {
		this.isAppiumScripGenerationTAF = isAppiumScripGenerationTAF;
	}
	@Column(name="isSeetestScripGenerationTAF")
	public Integer getIsSeetestScripGenerationTAF() {
		return isSeetestScripGenerationTAF;
	}

	public void setIsSeetestScripGenerationTAF(Integer isSeetestScripGenerationTAF) {
		this.isSeetestScripGenerationTAF = isSeetestScripGenerationTAF;
	}
	@Column(name="isProtractorScriptless")
	public Integer getIsProtractorScriptless() {
		return isProtractorScriptless;
	}

	public void setIsProtractorScriptless(Integer isProtractorScriptless) {
		this.isProtractorScriptless = isProtractorScriptless;
	}
	@Column(name="isProtractorScripGeneration")
	public Integer getIsProtractorScripGeneration() {
		return isProtractorScripGeneration;
	}

	public void setIsProtractorScripGeneration(Integer isProtractorScripGeneration) {
		this.isProtractorScripGeneration = isProtractorScripGeneration;
	}
	@Column(name="isProtractorScripGenerationTAF")
	public Integer getIsProtractorScripGenerationTAF() {
		return isProtractorScripGenerationTAF;
	}

	public void setIsProtractorScripGenerationTAF(
			Integer isProtractorScripGenerationTAF) {
		this.isProtractorScripGenerationTAF = isProtractorScripGenerationTAF;
	}
	@Column(name="isEDATScriptless")
	public Integer getIsEDATScriptless() {
		return isEDATScriptless;
	}

	public void setIsEDATScriptless(Integer isEDATScriptless) {
		this.isEDATScriptless = isEDATScriptless;
	}
	@Column(name="isEDATScripGeneration")
	public Integer getIsEDATScripGeneration() {
		return isEDATScripGeneration;
	}

	public void setIsEDATScripGeneration(Integer isEDATScripGeneration) {
		this.isEDATScripGeneration = isEDATScripGeneration;
	}
	@Column(name="isEDATScripGenerationTAF")
	public Integer getIsEDATScripGenerationTAF() {
		return isEDATScripGenerationTAF;
	}

	public void setIsEDATScripGenerationTAF(Integer isEDATScripGenerationTAF) {
		this.isEDATScripGenerationTAF = isEDATScripGenerationTAF;
	}
	@Column(name="isCodeduiScriptless")	
	public Integer getIsCodeduiScriptless() {
		return isCodeduiScriptless;
	}

	public void setIsCodeduiScriptless(Integer isCodeduiScriptless) {
		this.isCodeduiScriptless = isCodeduiScriptless;
	}
	@Column(name="isCodeduiScripGeneration")
	public Integer getIsCodeduiScripGeneration() {
		return isCodeduiScripGeneration;
	}

	public void setIsCodeduiScripGeneration(Integer isCodeduiScripGeneration) {
		this.isCodeduiScripGeneration = isCodeduiScripGeneration;
	}
	@Column(name="isCodeduiScripGenerationTAF")
	public Integer getIsCodeduiScripGenerationTAF() {
		return isCodeduiScripGenerationTAF;
	}

	public void setIsCodeduiScripGenerationTAF(Integer isCodeduiScripGenerationTAF) {
		this.isCodeduiScripGenerationTAF = isCodeduiScripGenerationTAF;
	}
	@Column(name="isTestCompleteScriptless")
	public Integer getIsTestCompleteScriptless() {
		return isTestCompleteScriptless;
	}

	public void setIsTestCompleteScriptless(Integer isTestCompleteScriptless) {
		this.isTestCompleteScriptless = isTestCompleteScriptless;
	}
	@Column(name="isTestCompleteScripGeneration")
	public Integer getIsTestCompleteScripGeneration() {
		return isTestCompleteScripGeneration;
	}

	public void setIsTestCompleteScripGeneration(
			Integer isTestCompleteScripGeneration) {
		this.isTestCompleteScripGeneration = isTestCompleteScripGeneration;
	}
	@Column(name="isTestCompleteScripGenerationTAF")
	public Integer getIsTestCompleteScripGenerationTAF() {
		return isTestCompleteScripGenerationTAF;
	}

	public void setIsTestCompleteScripGenerationTAF(
			Integer isTestCompleteScripGenerationTAF) {
		this.isTestCompleteScripGenerationTAF = isTestCompleteScripGenerationTAF;
	}
	@Column(name="isRestAssuredScriptless")
	public Integer getIsRestAssuredScriptless() {
		return isRestAssuredScriptless;
	}

	public void setIsRestAssuredScriptless(Integer isRestAssuredScriptless) {
		this.isRestAssuredScriptless = isRestAssuredScriptless;
	}
	@Column(name="isRestAssuredScripGeneration")
	public Integer getIsRestAssuredScripGeneration() {
		return isRestAssuredScripGeneration;
	}

	public void setIsRestAssuredScripGeneration(Integer isRestAssuredScripGeneration) {
		this.isRestAssuredScripGeneration = isRestAssuredScripGeneration;
	}
	@Column(name="isRestAssuredScripGenerationTAF")
	public Integer getIsRestAssuredScripGenerationTAF() {
		return isRestAssuredScripGenerationTAF;
	}

	public void setIsRestAssuredScripGenerationTAF(
			Integer isRestAssuredScripGenerationTAF) {
		this.isRestAssuredScripGenerationTAF = isRestAssuredScripGenerationTAF;
	}
	@Column(name="isTestCompleteWebScriptless")
	public Integer getIsTestCompleteWebScriptless() {
		return isTestCompleteWebScriptless;
	}

	public void setIsTestCompleteWebScriptless(Integer isTestCompleteWebScriptless) {
		this.isTestCompleteWebScriptless = isTestCompleteWebScriptless;
	}
	@Column(name="isTestCompleteWebScripGeneration")
	public Integer getIsTestCompleteWebScripGeneration() {
		return isTestCompleteWebScripGeneration;
	}

	public void setIsTestCompleteWebScripGeneration(
			Integer isTestCompleteWebScripGeneration) {
		this.isTestCompleteWebScripGeneration = isTestCompleteWebScripGeneration;
	}
	@Column(name="isTestCompleteWebScripGenerationTAF")
	public Integer getIsTestCompleteWebScripGenerationTAF() {
		return isTestCompleteWebScripGenerationTAF;
	}

	public void setIsTestCompleteWebScripGenerationTAF(
			Integer isTestCompleteWebScripGenerationTAF) {
		this.isTestCompleteWebScripGenerationTAF = isTestCompleteWebScripGenerationTAF;
	}


	public String getKeywordRegularExpression() {
		return keywordRegularExpression;
	}

	public void setKeywordRegularExpression(String keywordRegularExpression) {
		this.keywordRegularExpression = keywordRegularExpression;
	}

	@Column(name="tags")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	
	@Column(name="isDevice")
	public Integer getIsDevice() {
		return isDevice;
	}

	public void setIsDevice(Integer isDevice) {
		this.isDevice = isDevice;
	}
	@Column(name="isEdat")
	public Integer getIsEdat() {
		return isEdat;
	}

	public void setIsEdat(Integer isEdat) {
		this.isEdat = isEdat;
	}
	
	@Column(name="isCustomCiscoScriptless")
	public Integer getIsCustomCiscoScriptless() {
		return isCustomCiscoScriptless;
	}

	public void setIsCustomCiscoScriptless(Integer isCustomCiscoScriptless) {
		this.isCustomCiscoScriptless = isCustomCiscoScriptless;
	}
	
	@Column(name="isCustomCiscoScripGeneration")
	public Integer getIsCustomCiscoScripGeneration() {
		return isCustomCiscoScripGeneration;
	}

	public void setIsCustomCiscoScripGeneration(Integer isCustomCiscoScripGeneration) {
		this.isCustomCiscoScripGeneration = isCustomCiscoScripGeneration;
	}

	@Column(name="isCustomCiscoScripGenerationTAF")
	public Integer getIsCustomCiscoScripGenerationTAF() {
		return isCustomCiscoScripGenerationTAF;
	}

	public void setIsCustomCiscoScripGenerationTAF(
			Integer isCustomCiscoScripGenerationTAF) {
		this.isCustomCiscoScripGenerationTAF = isCustomCiscoScripGenerationTAF;
	}

	@Column(name="isCustomCisco")
	public Integer getIsCustomCisco() {
		return isCustomCisco;
	}

	public void setIsCustomCisco(Integer isCustomCisco) {
		this.isCustomCisco = isCustomCisco;
	}
}
