package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestEngineLanguageMode;

public interface TestEngineLanguageModeDAO  {	 
	public TestEngineLanguageMode getTestEngineLanguageModeId(int testEngineId, int languageId, int modeId);

	public TestEngineLanguageMode delete(TestEngineLanguageMode testEngineLanguageMode);

	
}