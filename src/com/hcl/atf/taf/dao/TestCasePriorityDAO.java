package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCasePriority;

public interface TestCasePriorityDAO  {	
	List<TestCasePriority> list();	
	TestCasePriority getTestCasePriorityBytestcasePriorityId(int testcasePriorityId);
	TestCasePriority getPrioirtyByName(String priroirtyName);
}
