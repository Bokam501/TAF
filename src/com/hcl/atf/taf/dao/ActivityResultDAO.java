package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityResult;

public interface ActivityResultDAO {
	List<ActivityResult> listActivityResults();
	ActivityResult getResultsByName(String resultName);
	ActivityResult getResultsById(Integer resultId);
}
