/**
 * 
 */
package com.hcl.atf.taf.controller.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author silambarasur
 *
 */
public class ActiveDirectoryUtility {

	
	public static String fetchActiveDirectoryUserInformation(String adLocation,String userName,String requiredInfo) {
			try 
	        { 
				String execcmd = "cmd /c "+adLocation+" -sc u:"+userName+requiredInfo;
				Process p=Runtime.getRuntime().exec(execcmd);
	            BufferedReader reader=new BufferedReader(
	                new InputStreamReader(p.getInputStream())
	            ); 
	            String line;
	            while((line = reader.readLine()) != null) 
	            { 
	            	
	            	requiredInfo=requiredInfo.replace(" ", ">");
	            	if(line.contains(requiredInfo.trim())) {
	            		String response[]=line.split(":");
	            		if(response != null && response.length >0) {
	            			return response[1];
	            		}
	            	}
	            	
	            } 
	        }
	        catch(IOException e1) {
	        	e1.printStackTrace();
	        	
	       	} 
			return "";
			
	}	
}
