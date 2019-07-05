/**
 * 
 */
package com.hcl.atf.taf.model;

/**
 * @author silambarasur
 *
 */
public class WorkFlowDTO {
	
	private String action;
	
	private String result;
	
	private String actor;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}
	
	

}
