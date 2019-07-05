package com.hcl.atf.taf.service;

import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;

public interface TestExecutionResultAutomatedService {

	TestExecutionResult addTestExecutionResult(TestExecutionResult testExecutionResult);

	TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult,JsonTestExecutionResult jsonTestExecutionResult);

}
