package com.hcl.atf.taf.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.ScriptGenerationDetails;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseScriptVersion;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestStoryGeneratedScripts;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonAutomationScriptsVersion;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScript;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScriptFileContent;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScripts;
import com.hcl.atf.taf.model.json.JsonTestCaseScript;
import com.hcl.atf.taf.model.json.JsonTestStoryGeneratedScripts;

public interface TestCaseScriptGenerationService {

	JsonTestCaseScript getTestCaseScriptForViewing(Integer testCaseId, String scriptsFor, String packageName,
			String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine,
			boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption);

	String generateProductTestCaseScripts(Integer id, String scriptsFor, String packageName, String className,
			String destinationDirectory, int nameSource, String scriptType, String executionEngine,
			boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption);

	JsonTestCaseScript getTestSuiteRefScriptForViewing(Integer id, String scriptsFor, String packageName,
			String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine,
			boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption);

	String addTestCaseAutomationScript(JsonTestCaseAutomationScript jsonTestCaseAutomationScript);
	List<JsonTestCaseAutomationScript> listTestCaseAutomationScripts(Integer testCaseId);
	String generateBDDTestCaseAutomationScriptFiles(Integer id, String scriptsFor, String scriptType, String testEngine);
	String generateBDDTestCaseAutomationScriptFiles(Integer id, String scriptsFor, String scriptType, String testEngine, String destinationDirectory);
	String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, String testEngine, String destinationDirectory);
	String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, String testEngine, String destinationDirectory, TestRunPlan testRunPlan);

	void addTestcaseautoscripts(JsonTestCaseAutomationScripts jsonTestAutomationScripts);

	List<TestCaseScript> getTestcaseAutoScripts(Integer testScriptId,Integer jtStartIndex,Integer jtPageSize);
	
	List<TestCaseScriptVersion> addTestCaseAutoScriptsVersion(JsonAutomationScriptsVersion jsontcautomationscriptsversion, Integer testCaseId);

	List<TestCaseScriptVersion> getTestCaseAutoScriptsVersionList(Integer scriptId, Integer jtStartIndex, Integer jtPageSize);
	
	List<BDDKeywordsPhrases> getBDDKeywordsPhrasesList(String productType, String testTool, Integer jtStartIndex,Integer jtPageSize);

	void updateBDDKeyWordsPhrase(BDDKeywordsPhrases bddKeyWordsPhrasesFromUI);

	void addTestCaseAutoScriptsVersionStatus(JsonAutomationScriptsVersion jsontcautomationscriptsversion,Integer scriptVersionId, Integer status);

	Integer getBDDKeywordsPhrasesListSize(String productType, String testTool);

	List<JsonTestCaseAutomationScriptFileContent> readContentFromObjectRepositoryFile(String destinationDirectory);

	List<JsonTestCaseAutomationScriptFileContent> readContentFromTestDataFile(String filePath);

	List<String> getKeywordLibrariesbyName(String testEngine);

	List<JsonTestCaseAutomationScriptFileContent> loadAttachmentKeywords(
			Integer attachmentId);

	KeywordLibrary getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(
			Integer testToolId, Integer languagID, String type, Integer id);

	void updateKeywordLibrary(KeywordLibrary keywordLibrary);

	Integer saveKeywordLibrary(KeywordLibrary keywordLibrary);

	List<KeywordLibrary> listKeywordLibraryByKeywordId(Integer keywordId);

	Integer totalKeywordLibraryByKeywordId(Integer keywordId);

	List<KeywordLibrary> getkeywordLibById(Integer keywrdLibId);

	List<BDDKeywordsPhrases> getMyKeywords(Integer userId,
			Integer jtStartIndex, Integer jtPageSize);

	Integer getMyKeywordsSize(Integer userId, Integer jtStartIndex,
			Integer jtPageSize);

	BDDKeywordsPhrases getBDDKeyWordsPhraseByKeywordPharse(String keywordPhrase);

	Integer saveBDDKeyWordsPhrase(BDDKeywordsPhrases bddKeyWordsPhrasesFromUI);

	List<TestCaseScript> getTestcaseScripts(Integer productId,Integer jtStartIndex,Integer jtPageSize);

	TestCaseScript getTestCaseScriptById(int testCaseScriptId);
	
	TestCaseScript getTestCaseScriptByName(String testCaseScriptName);
	
	void addTestcaseScript(TestCaseScript testCaseScript);

	void updateTestcaseScript(TestCaseScript testCaseScript);

	void deleteTestcaseScript(TestCaseScript testCaseScript);

	List<Object[]> getUnMappedTestCasesByProductId(int productId, int scriptId, int jtStartIndex, int jtPageSize);

	List<Object[]> getMappedTestCasesByScriptId(int scriptId);
	
	int getUnMappedTestCasesCountByProductId(int productId, int scriptId);

	void updateTestCaseToTestCaseScript(Integer scriptId, Integer testCaseId, UserList userList, String mapOrUnmap);

	void addTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase);
	
	void deleteTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase);
	
	TestCaseScriptHasTestCase getTestCaseScriptAssociationByIds(Integer scriptId, Integer testCaseId);
	
	List<TestCaseScriptHasTestCase> getTestcaseAndScriptByScriptId(Integer testScriptId);
	
	List<TestCaseScript> getTestcaseScriptsList();
	
	Integer getTestCaseScriptCount(Integer productId);

	List<TestCaseScript> getTestcaseScriptByScriptId(Integer scriptId);

	List<TestCaseScript> getTestCaseScripsByTestcaseId(Integer testcaseId);
	
	void addTestDataItems(Attachment testDataObj);
	void addUIObjects(Attachment objectRepAttach);

	public List<JsonTestCaseScript> getTestAutomationStoryWithVersions(
			Integer testCaseId, String scriptType, String testEngine,
			Integer projectId, HttpSession session);

	public List<BDDKeywordsPhrases> getBDDKeywordsPhrasesList(String productType,String testTool,Integer status, Integer jtStartIndex, Integer jtPageSize,
			Map<String, String> searchStrings);

	public JsonTestCaseScript getTestCaseAutomationScript(Integer testCaseId,
			String scriptType, String testEngine, String productTypeName,
			String objectRepAttach, String testDataAttach, String amdocsMode,
			Integer productId, HttpSession session);

	public String getTestCaseStoryEditingUser(Integer testCaseId);

	public void updateTestStoryEditingStatus(Integer testCaseId, String userName,
			String editingStatus, Integer userId);

	public List<String> loadAttachmentKeywords(HttpSession session, Integer productId,
			Integer attachmentId, String type, String filter);

	public List<Attachment> listDeviceObjectsEDAT(Integer productId);

	public String saveTestCaseAutomationStory(Integer testCaseId, String testEngine,
			String automationScript, String selectedConfigFile);

	public TestStoryGeneratedScripts saveGeneratedTestScript(Integer testCaseId,
			Integer testStoryId, String script, String testEngine, String languageName,
			Integer userId, String downloadPath, String codeGenerationMode);

	public JsonTestStoryGeneratedScripts getAutomationScript(Integer testCaseStoryId,
			String languageName, String testEngine);

	public JsonTestStoryGeneratedScripts updatedGeneartedScript(
			Integer generatedScriptId, String updatedScript,
			String languageName, String modifiedBy, String testToolName, String codeGenerationMode);

	

	public List<TestDataItems> listTestDataItemsByProductId(Integer productId,
			Integer testDataItemFilterId, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);

	public int totalTestDataItemsByProductId(Integer productId,
			Integer testDataFilterId, Integer userId);

	public List<TestDataPlan> listTestDataPlan(Integer productId);

	public int addTestDataItems(TestDataItems testDataItems);

	public void updateTestDataItems(TestDataItems testDataItems);

	public TestDataItems getTestDataItemByItemName(String testDataItemName,
			Integer productId, Integer userId);

	public List<TestDataItemValues> listTestDataItemValuesByTestDataItemId(
			Integer testDataItemId, Integer jtStartIndex, Integer jtPageSize);

	public int totalTestDataItemValuesByTestDataItemId(Integer testDataItemId);

	public int addTestDataItemValues(TestDataItemValues testDataItemVal);

	public void updateTestDataItemValues(TestDataItemValues testDataItemVal);

	public TestDataItemValues getTestDataItemValuesByProductAndTestItemValName(
			Integer projectId, Integer userId, String testDataItemValue,
			Integer testDataItemId, Integer testDataPlanId);

	public List<UIObjectItems> listUIObjectItemsByProductId(Integer productId,
			Integer objRepoFilterId, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);

	public int totalUIObjectItemsByProductId(Integer productId,
			Integer objRepoFilterId, Integer userId);

	public int addUIObjects(UIObjectItems uiObj);

	public void updateUIObjects(UIObjectItems uIObjectItems);

	public UIObjectItems getUIObjectItemByElementName(String elementName,
			Integer productId, Integer userId);

	public String saveTestCaseAutomationScript(Integer testCaseId, Integer versionId,
			String automationScript, String selectedConfigFile);

	public String generateBDDTestCaseAutomationScriptFiles(Integer id,
			String scriptsFor, String scriptType, Integer versionId,
			String testEngine, String destinationDirectory);

	public String generateBDDTestCaseAutomationScriptFiles(
			Set<TestCaseList> testCases, String scriptType, Integer versionId,
			String testEngine, String destinationDirectory);

	public String generateBDDTestCaseAutomationScriptFiles(
			Set<TestCaseList> testCases, String scriptType, Integer versionId,
			String testEngine, String destinationDirectory,
			TestRunPlan testRunPlan);

	public TestCaseAutomationStory getTestEngineConfigFile(Integer testCaseId,
			String scriptType, Integer versionId, String testEngine);

	public void addScriptGenerationDetails(ScriptGenerationDetails scriptDetails);

	public List<String> listUIObjectItemHandleNamesByProductId(Integer productId,
			Integer userId);

	public List<UIObjectItems> listUIObjectItemsByHandleName(Integer productId,
			Integer userId, String handle);

	public List<String> listTesDataItemHandleNamesByProductId(Integer productId,
			Integer userId);

	public Integer listTestDataItemValuesCountByProductIdAndUserId(Integer productId,
			Integer userId);

	public List<TestDataItems> listTestDataItemValuesByProductAndTestDataPlanAndHandle(
			Integer productId, String handle, Integer testDataPlanId,
			Integer userId);

	public String downloadStoryScriptBundle(Map<String, String> queryMap);

	public String generateStoryScriptBundles(Integer productId,
			List<TestCaseAutomationStory> stories,
			List<TestCaseList> testCases, String scriptType, String testEngine,
			String destinationDirectory);

	public JsonTestStoryGeneratedScripts getGeneratedScriptById(Integer testCaseStoryId);

	public String saveTestCaseAutomationStory(Integer testCaseId, Integer versionId,
			String automationScript, String selectedConfigFile, String testEngine);

	public JsonTestStoryGeneratedScripts getAutomationScript(Integer testCaseStoryId,
			String languageName, String testEngine, String codeGenerationMode);

	public JsonTestStoryGeneratedScripts getGeneratedScriptByIdAndTAFMode(
			Integer testCaseAutomationStoryId, String codeGenerationMode);

	public List<String> addTestStepFilesByModeAndTestEngine(String codeGenerationMode,
			String testEngine, String scriptLanguage);

	public String checkInStoryToSVN(Integer testCaseId, String tcName, String data, String commitComment);

	public String checkInScriptToSVN(Integer storyId, String commitComment);

	public String generateScriptTdandObjForDownload(
			JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts);

	public Integer addTestDataPlan(TestDataPlan testDataPlan);

	public void updateTestDataPlan(TestDataPlan testDataPlan);

	public TestDataPlan getTestDataPlanByTestDataPlanName(String testDataPlanName,
			Integer projectId);

	public TestDataItems getTestDataItemByItemId(Integer testDataItemId);

	public String deleteTestData(TestDataItems tesDataItem);

	public UIObjectItems getUIObjectItemById(Integer uiObjectItemId);

	public String deleteObjectRepo(UIObjectItems uiObjectItem);
	
	public boolean deleteKeywordLibrary(Integer keywordLibId);

	KeywordLibrary getKeywordLibraryByClassNameAndBinary(String className, String binaryLoaderName);

	String checkInRepositoryToSVN(Integer storyId, String commitComment, boolean isObjectRepo, boolean isTestData);
	
}
