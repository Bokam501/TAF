package com.hcl.atf.taf.controller.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepEvidenceGridList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;

public class EvidenceGridData {

	public static List<TestRunReportsDeviceCaseStepEvidenceGridList> transposeTestStepScreenShotData(List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList)
	{
		
		List<TestRunReportsDeviceCaseStepEvidenceGridList> testRunReportsDeviceCaseStepEvidenceGridList= new ArrayList<TestRunReportsDeviceCaseStepEvidenceGridList>();
		Integer CurrestTestStepIDNumber = 0;
		
		TestRunReportsDeviceCaseStepEvidenceGridList t = new TestRunReportsDeviceCaseStepEvidenceGridList();
		HashMap<Integer,String> screenShotPathMap =  new HashMap<Integer,String>();

		for(TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepListdet: testRunReportsDeviceCaseStepList){
			//The following if condition is run for the first record alone. Initially the counter is set as 0, update the structure with the first record 
			if (CurrestTestStepIDNumber == 0)
			{
				t.setTestRunNo(testRunReportsDeviceCaseStepListdet.getTestrunno());
				t.setTestRunConfigurationChildId(testRunReportsDeviceCaseStepListdet.getTestrunconfigurationchildid());
				t.setTestCaseId(testRunReportsDeviceCaseStepListdet.getTestcaseid());
				t.setTestStepId(testRunReportsDeviceCaseStepListdet.getTeststepid());
				t.setTestCaseName(testRunReportsDeviceCaseStepListdet.getTestcasename());
				t.setTestStepName(testRunReportsDeviceCaseStepListdet.getTeststepname());
				t.setTestStepDescription(testRunReportsDeviceCaseStepListdet.getTeststepdescription());
				t.setScreenShotLabel(testRunReportsDeviceCaseStepListdet.getScreenshotlabel());
				
				
				if (!screenShotPathMap.containsKey(testRunReportsDeviceCaseStepListdet.getTestrunlistid()))
					screenShotPathMap.put(testRunReportsDeviceCaseStepListdet.getTestrunlistid(), testRunReportsDeviceCaseStepListdet.getScreenshotpath());
				
				if (testRunReportsDeviceCaseStepList.size() > 1)
					CurrestTestStepIDNumber = testRunReportsDeviceCaseStepListdet.getTeststepid();
				else //If the record count is 1 populate screenpaths and come out of the loop
				{	
					t.setScreenShotPathMap(screenShotPathMap);
					testRunReportsDeviceCaseStepEvidenceGridList.add(t);
				}
			}
			else
			{
				// The below logic is for 2nd record until end of the records, if the same test step is found update the screenshot paths
				//with the existing collection, otherwise create new record for the output and update the records
				if (CurrestTestStepIDNumber.intValue() != testRunReportsDeviceCaseStepListdet.getTeststepid().intValue())
				{	
					t.setScreenShotPathMap(screenShotPathMap);
					testRunReportsDeviceCaseStepEvidenceGridList.add(t);
					
					CurrestTestStepIDNumber = testRunReportsDeviceCaseStepListdet.getTeststepid();
					
					t = new TestRunReportsDeviceCaseStepEvidenceGridList();
					screenShotPathMap =  new HashMap<Integer,String>();
					
					t.setTestRunNo(testRunReportsDeviceCaseStepListdet.getTestrunno());
					t.setTestRunConfigurationChildId(testRunReportsDeviceCaseStepListdet.getTestrunconfigurationchildid());
					t.setTestCaseId(testRunReportsDeviceCaseStepListdet.getTestcaseid());
					t.setTestStepId(testRunReportsDeviceCaseStepListdet.getTeststepid());
					t.setTestCaseName(testRunReportsDeviceCaseStepListdet.getTestcasename());
					t.setTestStepName(testRunReportsDeviceCaseStepListdet.getTeststepname());
					t.setTestStepDescription(testRunReportsDeviceCaseStepListdet.getTeststepdescription());
					t.setScreenShotLabel(testRunReportsDeviceCaseStepListdet.getScreenshotlabel());
				}
				if (!screenShotPathMap.containsKey(testRunReportsDeviceCaseStepListdet.getTestrunlistid()))
					screenShotPathMap.put(testRunReportsDeviceCaseStepListdet.getTestrunlistid(), testRunReportsDeviceCaseStepListdet.getScreenshotpath());
			}
		}
		//If the number of records is 0, above for loop comes out immediately. Hence should not create transpose data if the record count is 0
		if (CurrestTestStepIDNumber != 0)
		{
			t.setScreenShotPathMap(screenShotPathMap);
			testRunReportsDeviceCaseStepEvidenceGridList.add(t);
		}
		return testRunReportsDeviceCaseStepEvidenceGridList;
	}
}


