package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import com.hcl.atf.taf.model.ExecutionMode;
import com.hcl.atf.taf.model.Languages;


public interface ExecutionModeDAO  {

	List<ExecutionMode> listExecutionModes(int modeId);

	ExecutionMode getExecutionModeId(int languageId);

	ExecutionMode getModeIdByName(String modeName);

}
