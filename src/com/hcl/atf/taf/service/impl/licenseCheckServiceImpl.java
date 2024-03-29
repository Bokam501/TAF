package com.hcl.atf.taf.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.Charset; 
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.service.LicenseCheckService;
@Service
public class licenseCheckServiceImpl implements LicenseCheckService {

	private static String fName    = "";
	private static String copyRight = "Copyright © 2015, HCL Technologies Limited\n" 
									+ "Please contact authorized HCL for a new license file\n"
									+ "if you are not an authorized person\n";
	
	
	private static String message = "";
	
	@Override
	@Transactional
	public String licenseAgrementValidation(String file)  {
		String validateMsg = "";
		try{
		licenseCheckServiceImpl objLicenseChk = new licenseCheckServiceImpl();
		fName = file;
		System.out.println("properties file path fName------"+fName);
		Properties p = objLicenseChk.getValidLicense();
	
		
		if( p == null ) {
			System.out.println("Invalid license file .. Tampered !");
				validateMsg= "{\"status\":\"Failure\",\"message\":"+message+"}";
		} else {
			Properties properties = getLicencePropertiesData(fName);
			if(properties != null )
			{
				System.out.println("Valid license file, generated by " + p.getProperty("LIC_updatedBy"));
				Boolean userDateAvial  = objLicenseChk.chkWithDate(properties);
				System.out.println("date valuation status-------"+userDateAvial);
				//Boolean MacStatus = objLicenseChk.macAdressCheck(properties);//true;
				//System.out.println("MAC valuation status-------"+MacStatus);
				if(userDateAvial){
					System.out.println("you are validated user");
					validateMsg= "{\"status\":\"success\"}";
				}
				else{
					System.out.println("You are not validate user. Please contact system administrator");
					validateMsg= "{\"status\":\"Failure\",\"message\":"+message+"}";
				}
				
			}
			else
			{
				
				message = "Error in reading license file"	;
				validateMsg= "{\"status\":\"Failure\",\"message\":"+message+"}";
				
			}
			
				
		}
		}catch(Exception e){
			message = "Error in reading license file"	;
			validateMsg= "{\"status\":\"Failure\",\"message\":"+message+"}";
		}
		return validateMsg;	
		
	}
	
	public Properties getLicencePropertiesData(String fName)
	{
		try
		{
		FileInputStream in = new FileInputStream(fName);
		Properties props = new Properties();
		props.load(in);
		in.close();
		return props;
		}
		catch(Exception e)
		{
			message = "Error in reading properties file";
			e.printStackTrace();
			
		}
		return null;
		
	}
	
	
	private Boolean chkWithDate(Properties props){
		try {
		
			String userExpireDate = props.getProperty("Expire");
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = formatter.format(new Date());
			
			Date date1 = formatter.parse(userExpireDate); 
			Date date2 = formatter.parse(currentDate);
			if(date1.after(date2)){
				System.out.println("Date1 is after Date2");
				return true;
			}
			else
			{
				message = "Expired license";
			}
		
        	
		}catch (Exception e) {
			message =  "An Exception occured in expiry date check";
			e.printStackTrace();
		}
		return false;
	}
	
	
	private Boolean macAdressCheck(Properties props){
		try {
			licenseCheckServiceImpl objlicenseCheck = new licenseCheckServiceImpl();
		
			String propMACAdr= props.getProperty("MAC");
			List macList = objlicenseCheck.getCurrentSysMACAdr();
			System.out.println("properties file MAC Adr----"+propMACAdr);
			System.out.println("properties file MAC Adr----"+macList.toString());
			
				if(macList.contains(propMACAdr)){
					System.out.println("MAC address matched");
					return true;
				}else{
					System.out.println("MAC address not matched");
					message = "MAC address not matched" ;
				}
				
			
		} catch (Exception e) {
			message = "An Exception occured in mac address check";
			e.printStackTrace();
		} 
		return false;
	}
	
	private List getCurrentSysMACAdr(){

		Enumeration<NetworkInterface> networkInterfaces;
		List macList = new ArrayList();
        try {
             networkInterfaces = NetworkInterface.getNetworkInterfaces();
             List li = Collections.list(networkInterfaces);
             for(int in=0; in<li.size();in++ )
             {
            	 StringBuilder sb = new StringBuilder();
                   try {
                	   
                        if (((NetworkInterface) li.get(in)).getHardwareAddress() != null) {
                             byte[] mac = (((NetworkInterface) li.get(in)).getHardwareAddress());
                             for (int i = 0; i < mac.length; i++) {
                            	 System.out.println(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                                   sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                                  
                             }
                             
                             if(sb !=null && sb.length() >0)
                             {
                             macList.add(sb.toString());
                             }
                           
                       }
                   } catch (SocketException e) {
                        e.printStackTrace();
                   }
             }
             System.out.println("============="+macList.toString());
        } catch (SocketException e1) {
             e1.printStackTrace();
        }	
		return macList;
	}
	
	
	private String checkSum(String inp) {
		   try {
				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
				//byte[] array = md.digest(inp.getBytes());
				byte[] array = md.digest(inp.getBytes("UTF-8")); 
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < array.length; ++i) {
				  sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			   }
				return sb.toString();
			} catch (Exception e) {
			}
			return null;
	}
	
	private String getStr(Properties props) {
		String s =  ":" + copyRight + ":";
		Enumeration e = props.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if(key.startsWith("LIC_")) continue;
			s += props.getProperty(key);
		}
		
		return s;
	}
	
	
	private Properties getValidLicense () throws Exception {
		FileInputStream in = new FileInputStream(fName);
		Properties props = new Properties();
		//props.load(in);
		props.load(new InputStreamReader(in, Charset.forName("UTF-8"))); 
		in.close();

	    String sval = getStr(props);
		String sig = checkSum(sval);
		if( sig.equals(props.getProperty("LIC_Signature")))
		{ 	
		
			return props;
	    }
		message = "LIC_Signature failed";	
		return null;
	}

	@Override
	@Transactional
	public String getLicenseExpiryData(String filePath) {
		String userExpireDate = null;
		Properties properties = getLicencePropertiesData(fName);
		if(properties != null)
			userExpireDate = properties.getProperty("Expire");
		
		return userExpireDate;
	}
	
  
}