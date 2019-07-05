package com.hcl.atf.taf.service.impl;


import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.hcl.atf.taf.service.PasswordEncryptionService;

@Service
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService
{
	private static final Log log = LogFactory.getLog(PasswordEncryptionServiceImpl.class);
    @Value("#{ilcmProps['ALGORITHM']}")
    private String algorithm;
    
    @Value("#{ilcmProps['KEY']}")
    private String encryptionkey;
    
    @Override
    @Transactional
    public String encrypt(String value){
    	String encryptedValue64 = null;
        try {
			Key key = generateKey();
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
			encryptedValue64 = new BASE64Encoder().encode(encryptedByteValue);
		}catch (Exception e) {
			log.error("ERROR  ",e);
			e.printStackTrace();
		}
        return encryptedValue64;  
    }
    
    @Override
    @Transactional
    public String decrypt(String value)
    {
        String decryptedValue = null;
		try {
			Key key = generateKey();
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(value);
			byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
			decryptedValue = new String(decryptedByteValue,"utf-8");
		}catch (Exception e) {
			log.error("ERROR  ",e);
			e.printStackTrace();
		}
        return decryptedValue;
                
    }
    
    private Key generateKey() throws Exception 
    {
        Key key = new SecretKeySpec(encryptionkey.getBytes(),algorithm);
        return key;
    }
    
}
