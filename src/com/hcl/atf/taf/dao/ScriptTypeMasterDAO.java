package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCategoryMaster;

public interface ScriptTypeMasterDAO  {	 
	List<ScriptTypeMaster> list();
	ScriptTypeMaster getScriptTypeMasterByscriptType(String scriptTypeName);
}
