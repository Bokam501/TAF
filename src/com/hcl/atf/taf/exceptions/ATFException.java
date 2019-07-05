package com.hcl.atf.taf.exceptions;

public class ATFException extends Exception {

	static final long serialVersionUID = 00000000001;
	
	public String errorCode;
	public String errorDescription;
	
	public Exception rootException;
	
	public ATFException(Exception e) {
	
		rootException = e;
	}

	public ATFException(String errorCode, String errorDescription, Exception e) {
		
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		rootException = e;
	}
	
	public ATFException(String errorCode, String errorDescription) {
		
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		//rootException = e;
	}
}
