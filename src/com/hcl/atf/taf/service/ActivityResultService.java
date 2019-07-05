package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityResult;

public interface ActivityResultService {
	ActivityResult getResultById(Integer resultId);
	ActivityResult getResultByName(String resultName);
	List<ActivityResult> listResultByActivityId(Integer activityId);
	List<ActivityResult> listActivityResult();
}


