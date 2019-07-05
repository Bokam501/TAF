package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestStoryGeneratedScripts;
import com.hcl.atf.taf.model.TestCaseAutomationStory;

public interface TestCaseStoryDAO {



	public List<TestCaseAutomationStory> getTestCaseStoryWithVersions(Integer testCaseId);


	public TestStoryGeneratedScripts saveGeneratedScripts(
			TestStoryGeneratedScripts storyGeneratedScripts);



	public TestStoryGeneratedScripts getGeneratedScripts(
			Integer testCaseStoryId, Integer languageId, Integer testToolId);



	public TestStoryGeneratedScripts getGeneratedScript(
			Integer generatedScriptId, Integer languageId);



	public String updateGeneratedScript(
			TestStoryGeneratedScripts testStoryGeneratedScript);


	public TestStoryGeneratedScripts getGeneratedScripts(Integer testCaseStoryId);

	public TestStoryGeneratedScripts getGeneratedScripts(
			Integer testCaseStoryId, Integer id, Integer testToolId,
			String codeGenerationMode);

	
}
