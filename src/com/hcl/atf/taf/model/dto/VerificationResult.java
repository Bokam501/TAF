/**
 * 
 */
package com.hcl.atf.taf.model.dto;

/**
 * @author HCL
 *
 */
public class VerificationResult {
	
	private String isReady;

	private String sb;
	
	public VerificationResult() {
		
	}
	
	public void addMessage(String message) {
		sb = message;
	}
	
	public String getVerificationMessage() { 
		return sb;
	}

	public String getIsReady() {
		return isReady;
	}

	public void setIsReady(String isReady) {
		this.isReady = isReady;
	}	
}