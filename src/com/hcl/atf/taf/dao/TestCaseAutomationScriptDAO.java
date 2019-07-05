package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.AmdocsPageMethods;
import com.hcl.atf.taf.model.AmdocsPageObjects;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.ScriptGenerationDetails;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseScriptVersion;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.UIObjectItems;

public interface TestCaseAutomationScriptDAO {
	void updateTestCaseAutomationScript(TestCaseAutomationStory testCaseAutomationStory);
	List<TestCaseAutomationStory> listTestAutomationScripts(Integer testCaseId);
	TestCaseAutomationStory getTestAutomationScript(Integer testCaseId, String scriptType, String testEngine,Integer maxVersionId);
	void addTestCaseAutomationScript(TestCaseScript testcasescript);
	List<TestCaseScript> getTestCaseAutoscripts(Integer testcaseId,Integer jtStartIndex,Integer jtPageSize);
	void addTestCaseAutomationScriptVersionDAO(TestCaseScriptVersion testcasescriptversion);
	List<TestCaseScriptVersion> getTestAutomationScriptversion(Integer scriptId, Integer jtStartIndex, Integer jtPageSize);
	List<TestCaseScriptVersion> getTestCaseScriptVersionIsSelected(Integer testCaseId);
	List<BDDKeywordsPhrases> getBDDKeywordsPhrases(String productType, String testTool, Integer jtStartIndex,Integer jtPageSize);
	void updateBDDKeyWordsPhrase(BDDKeywordsPhrases bddKeyWordsPhrasesFromUI);
	public List<String> getKeywordPhrases(String productType,String testTool );
	List<TestCaseScriptVersion> getTestCaseScriptVersionStatus(Integer scriptId , Integer status);
	Integer getBDDKeywordsPhrasesListSize(String productType, String testTool);
	List<KeywordLibrary> listKeywordLibraryByKeywordId(Integer keywordId);
	Integer saveKeywordLibrary(KeywordLibrary keywordLibrary);
	void updateKeywordLibrary(KeywordLibrary keywordLibrary);
	List<String> getKeywordLibrariesbyName(String testEngine);
	List<KeywordLibrary> getkeywordLibById(Integer keywrdLibId);
	List<BDDKeywordsPhrases> getMyKeywords(Integer userId,
			Integer jtStartIndex, Integer jtPageSize);
	Integer getMyKeywordsSize(Integer userId);
	KeywordLibrary getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(
			Integer testToolId, Integer languagID, String type, Integer id);
	int totalKeywordLibraryByKeywordId(Integer keywordId);
	BDDKeywordsPhrases getBDDKeyWordsPhraseByKeywordPharse(String keywordPhrase);
	Integer saveBDDKeyWordsPhrase(BDDKeywordsPhrases bddKeyWordsPhrasesFromUI);
	List<TestCaseScript> getTestCaseScripts(Integer productId,Integer jtStartIndex,Integer jtPageSize);
	void addTestcaseScript(TestCaseScript testCaseScript);
	void updateTestcaseScript(TestCaseScript testCaseScript);
	void deleteTestcaseScript(TestCaseScript testCaseScript);
	List<Object[]> getUnMappedTestCasesByProductId(int productId, int scriptId, int jtStartIndex, int jtPageSize);
	List<Object[]> getMappedTestCasesByScriptId(int scriptId);
	int getUnMappedTestCasesCountByProductId(int productId, int scriptId);
	TestCaseScript getTestCaseScriptById(int testCaseScriptId);
	TestCaseScript getTestCaseScriptByName(String testCaseScriptName);
	void addTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase);
	void deleteTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase);
	TestCaseScriptHasTestCase getTestCaseScriptAssociationByIds(Integer scriptId, Integer testCaseId);
	List<TestCaseScriptHasTestCase> getTestCaseScriptAssociationList();
	List<TestCaseScriptHasTestCase> getTestcaseAndScriptByScriptId(Integer testScriptId);
	List<TestCaseScript> getTestcaseScriptsList();
	
	Integer getTestCaseScriptCount(Integer productId);
	List<TestCaseScript> getTestcaseScriptByScriptId(Integer scriptId);
	List<TestCaseScript> getTestCaseScripsByTestcaseId(Integer testcaseId);
	Integer getMappedTestCasesCountByProductId(int productId);
	Integer getMappedTestscriptCountByTestCaseId(Integer testCaseId);
	public TestDataItems getTestDataItemByItemName(String testDataItemName,Integer productId,Integer userId);
	public int addTestDataItems(TestDataItems testDataItems);
	public int addTestDataItemValues(TestDataItemValues testDataItemVal);
	TestDataPlan getTestDataPlanByTestDataPlanName(String testPlan,
			Integer projectId);
	Integer addTestDataPlan(TestDataPlan testDataPlan);
	void updateTestDataItems(TestDataItems testDataItems);
	UIObjectItems getUIObjectItemByElementName(String elementName,
			Integer projectId, Integer userId);
	void updateUIObjects(UIObjectItems uiObjects);
	int addUIObjects(UIObjectItems uiObjects);
	public List<String> getKeywordPhraseRegularExpressions(String productType,
			String testEngine);
	public List<String> listUIObjectItemElementNamesByProjectId(Integer productId,
			Integer userId, String filter);
	public List<String> listTestDataNamesByProductId(Integer productId,
			Integer userId, String filter);
	public List<AmdocsPageObjects> listAmdocsPageObjectsByProjectIdAndTestCaseId(
			Integer projectId, Integer userId, Integer testCaseId);
	public List<AmdocsPageMethods> listAmdocsPageMethodsByProjectId(Integer projectId,
			Integer userId);
	public List<BDDKeywordsPhrases> getBDDKeywordsPhrases(String productType,String testTool,Integer status,Integer jtStartIndex, Integer jtPageSize,
			Map<String, String> searchStrings);
	public Integer getMaxVersionIdByTestCaseId(Integer testCaseId);
	public TestCaseAutomationStory getTestAutomationScript(Integer testCaseId,
			String scriptType, Integer versionId, String testEngine);
	public Integer addTestCaseAutomationScript(
			TestCaseAutomationStory testCaseAutomationScript);
	public TestCaseAutomationStory getByTestCaseAutomationScriptId(Integer testCaseId,
			Integer versionId);
	public String getTestCaseStoryEditingUser(Integer testcaseId);
	public void updateTestStoryEditingStatus(Integer testcaseId, String userName,
			Date editingStartTime, String editingStatus, Integer userId);
	public List<Attachment> listDeviceObjectsNamesByProductId(Integer projectId);
	public List<TestDataItems> listTestDataItemsByProductId(Integer productId,
			Integer testDataFilterId, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);
	public int totalTestDataItemsByProductId(Integer productId,
			Integer testDataFilterId, Integer userId);
	public List<TestDataPlan> listTestDataPlan(Integer productId);
	public List<TestDataItemValues> listTestDataItemValuesByTestDataItemId(
			Integer testDataItemId, Integer jtStartIndex, Integer jtPageSize);
	public int totalTestDataItemValuesByTestDataItemId(Integer testDataItemId);
	public void updateTestDataItemValues(TestDataItemValues testDataItemVal);
	public TestDataItemValues getTestDataItemValuesByProductAndTestItemValName(
			Integer productId, Integer userId, String testDataItemValue,
			Integer testDataItemId, Integer testDataPlanId);
	public List<UIObjectItems> listUIObjectItemsByProductId(Integer productId,
			Integer objRepoFilterId, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);
	public int totalUIObjectItemsByProductId(Integer productId,
			Integer objRepoFilterId, Integer userId);
	public void addScriptGenerationDetails(ScriptGenerationDetails scriptDetails);
	public List<String> listUIObjectItemHandleNamesByProductId(Integer productId,
			Integer userId);
	public List<UIObjectItems> listUIObjectItemsByHandleName(Integer productId,
			Integer userId, String handle);
	public List<String> listTesDataItemHandleNamesByProductId(Integer productId,
			Integer userId);
	public Integer getTestDataItemValuesCountByProductIdAndUserId(Integer productId,
			Integer userId);
	public List<TestDataItems> listTestDataItemValuesByProductAndTestDataPlanAndHandle(
			Integer productId, String handle, Integer testDataPlanId,
			Integer userId);
	public List<TestCaseAutomationStory> listTestAutomationStories(Integer productId);
	
	public TestCaseAutomationStory getByTestCaseStoryByAutomationStoryId(
			Integer generatedScrpitId);
	public void updateTestDataPlan(TestDataPlan testDataPlan);
	public TestDataItems getTestDataItemById(Integer testDataItemId);
	public String deleteTestdata(TestDataItems tesDataItem);
	public UIObjectItems getUiObjectItemById(Integer uiObjectItemId);
	public String deleteUiObjectRepository(UIObjectItems uiObjectItem);
	boolean deleteKeywordLibrary(Integer keywordLibId);
	KeywordLibrary getKeywordLibraryByClassNameAndBinary(String className,String binaryLoaderName);
}
