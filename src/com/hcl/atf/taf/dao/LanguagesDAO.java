package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;








import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestEngineLanguageMode;
import com.hcl.atf.taf.model.WorkShiftMaster;


public interface LanguagesDAO  {

	List<Languages> listLanguages(int status);

	Languages getLanguageForId(int languageId);

	Languages getLanguageByName(String languageName);

	public void updateTestEngineLanguageMode(
			TestEngineLanguageMode testEngineLanguageMode);
}
