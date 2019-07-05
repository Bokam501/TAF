package com.hcl.atf.taf.service;

public interface PasswordEncryptionService {
	public String encrypt(String value);
	public String decrypt(String value);
}
