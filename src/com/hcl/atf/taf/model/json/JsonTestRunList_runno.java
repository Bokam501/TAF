package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestRunList;


public class JsonTestRunList_runno implements java.io.Serializable {
	
	@JsonProperty
	private int runNo;	
	
	public JsonTestRunList_runno() {
	}
	public JsonTestRunList_runno(TestRunList testRunList) {
	
		runNo=testRunList.getRunNo();
	}		
	public int getRunNo() {
		return runNo;
	}
	public void setRunNo(int runNo) {
		this.runNo = runNo;
	}

	@JsonIgnore
	public TestRunList getTestRunList(){
		TestRunList testRunList = new TestRunList();
		
		testRunList.setRunNo(runNo);
		
		return testRunList;
	}	
}
