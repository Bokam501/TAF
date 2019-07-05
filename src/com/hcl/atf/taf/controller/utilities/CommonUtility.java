package com.hcl.atf.taf.controller.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;

public class CommonUtility {

	private static Pattern pattern;
	private static Matcher matcher;

	public static String priorityId="";
	
	public static String crticalPriority=IDPAConstants.EXECUTION_PRIORITY_CRITICAL+IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN
			+IDPAConstants.PRIORITY_CRITICAL+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.PRIORITY_URGENT+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.EXECUTION_PRIORITY_CRITICAL;
	public static String highPriority=IDPAConstants.EXECUTION_PRIORITY_HIGH+IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN
			+IDPAConstants.PRIORITY_HIGH+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.PRIORITY_URGENT+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.EXECUTION_PRIORITY_HIGH;
	public static String majorPriority=IDPAConstants.EXECUTION_PRIORITY_MAJOR+IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN
			+IDPAConstants.PRIORITY_MAJOR+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.PRIORITY_URGENT+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.EXECUTION_PRIORITY_MAJOR;
	public static String mediumPriority=IDPAConstants.EXECUTION_PRIORITY_MEDIUM+IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN
			+IDPAConstants.PRIORITY_MEDIUM+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.PRIORITY_URGENT+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.EXECUTION_PRIORITY_MEDIUM;
	public static String lowPriority=IDPAConstants.EXECUTION_PRIORITY_TRIVIAL+IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN
			+IDPAConstants.PRIORITY_TRIVIAL+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.PRIORITY_URGENT+IDPAConstants.PRIORITY_PARSE_TOKEN+IDPAConstants.EXECUTION_PRIORITY_TRIVIAL;
	public static String [] crticalPriorityValue=null;
	public static String [] highPriorityValue=null;
	public static String [] mediumPriorityValue=null;
	public static String [] majorPriorityValue=null;
	public static String [] lowPriorityValue=null;
	public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	

	public static String getExecutionPriority(String priorityValue){
		String [] crticalPriorityArray = crticalPriority.split(IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN);
		
		String [] highPriorityArray = highPriority.split(IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN);
		String [] majorPriorityArray = majorPriority.split(IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN);
		String [] mediumPriorityArray = mediumPriority.split(IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN);
		String [] lowPriorityArray = lowPriority.split(IDPAConstants.EXECUTION_PRIORITY_PARSE_TOAKEN);
		
		if(crticalPriorityArray[1]!=null ){
			crticalPriorityValue=crticalPriorityArray[1].split(IDPAConstants.PRIORITY_PARSE_TOKEN);
			
		}
		 if(highPriorityArray[1]!=null ){
			highPriorityValue=highPriorityArray[1].split(IDPAConstants.PRIORITY_PARSE_TOKEN);
		}
		if(mediumPriorityArray[1]!=null ){
			mediumPriorityValue=mediumPriorityArray[1].split(IDPAConstants.PRIORITY_PARSE_TOKEN);
		}
		if(majorPriorityArray[1]!=null ){
			majorPriorityValue=majorPriorityArray[1].split(IDPAConstants.PRIORITY_PARSE_TOKEN);
		}
		if(lowPriorityArray[1]!=null ){
			lowPriorityValue=lowPriorityArray[1].split(IDPAConstants.PRIORITY_PARSE_TOKEN);
		}
		
		if((crticalPriorityValue[0].equalsIgnoreCase(priorityValue))||(crticalPriorityValue[1].equalsIgnoreCase(priorityValue))||(crticalPriorityValue[2].equalsIgnoreCase(priorityValue))){
			priorityId=crticalPriorityArray[0];
		}
		else if((highPriorityValue[0].equalsIgnoreCase(priorityValue))||(highPriorityValue[1].equalsIgnoreCase(priorityValue))||(highPriorityValue[2].equalsIgnoreCase(priorityValue))){
			priorityId=highPriorityArray[0];
		}
		
		else if((mediumPriorityValue[0].equalsIgnoreCase(priorityValue))||(mediumPriorityValue[1].equalsIgnoreCase(priorityValue))||(mediumPriorityValue[2].equalsIgnoreCase(priorityValue))){
			priorityId=mediumPriorityArray[0];
		}
		else if((majorPriorityValue[0].equalsIgnoreCase(priorityValue))||(majorPriorityValue[1].equalsIgnoreCase(priorityValue))||(majorPriorityValue[2].equalsIgnoreCase(priorityValue))){
			priorityId=majorPriorityArray[0];
		}
		else if((lowPriorityValue[0].equalsIgnoreCase(priorityValue))||(lowPriorityValue[1].equalsIgnoreCase(priorityValue))||(lowPriorityValue[2].equalsIgnoreCase(priorityValue))){
			priorityId=lowPriorityArray[0];
		}
		return priorityId;
	}
	
	public static boolean copyInputStreamToFile(InputStream in, File file ) {
		boolean res=false;
		OutputStream out = null;
		try {
	    	int cnt=0;
	    	out = new FileOutputStream(file);
	    	byte[] buffer = new byte[1024];
	    	int len = in.read(buffer);
	    	while (len != -1) {
	    		cnt++;
	        	if(cnt==1)
	        		res=true;
	    	    out.write(buffer, 0, len);
	    	    len = in.read(buffer);
	    	    if (Thread.interrupted()) {
	    	        throw new InterruptedException();
	    	    }
	    	}
	    } catch (Exception e) {
	        e.printStackTrace();
	        res=false;
	    }finally{
	    	if(out != null){
	    		try{
	    			out.flush();
	    			out.close();
	    		}catch(Exception ex){
	    			ex.printStackTrace();
	    		}
	    	}
	    }
	    return res;
	}
	
	
	public static String getEvidenceFileName(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan,String extn){
		String fileName="";
	
		fileName=workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId()+"_"+
				workPackageTestCaseExecutionPlan.getTestCase().getTestCaseName()+"_"+System.currentTimeMillis()+"_"+extn;
		return fileName;
	}
	
	public static String getCatalinaPath(){
		return System.getProperty("catalina.home");
	}
	

	public static Long getSeconds(String executionTime){
	
		Long seconds=null;
		String temp="";
		String[] tempArray=null;
		if(executionTime.contains("sec")){
			temp=executionTime.substring(0,executionTime.indexOf(" sec"));
			seconds=Long.parseLong(temp);
		}else if(executionTime.contains("min")){
			temp=executionTime.substring(0,executionTime.indexOf(" min"));
			tempArray=temp.split(":");
			Long sec=Long.parseLong(tempArray[1]);
			Long minute=Long.parseLong(tempArray[0]);
			seconds=sec+(60*minute);
		}
		else{
			tempArray=executionTime.split(":");
			Long sec=Long.parseLong(tempArray[2]);
			Long minute=Long.parseLong(tempArray[1]);
			Long hour=Long.parseLong(tempArray[0]);
			seconds=sec+(60*minute)+(3600*hour);
		}
		//
		return seconds;
	}
	
	public static void main(String args[]){
		String executionTime="1:00:03";

		getSeconds(executionTime);
	}
	
	public static List<Integer> getListFromString(String value){
		List<Integer> arrayListOfValue=new ArrayList<Integer>();
		if (value !=null && value!="" &&!value.equals("0") && !value.equals("null")) {
			
			if(value.contains(",")){
				String[] valueArray=value.split(",");
				for(String e:valueArray){
					arrayListOfValue.add(Integer.parseInt(e));
				}
			}else{
				arrayListOfValue.add(Integer.parseInt(value));
			}
		}
		return arrayListOfValue;
	}
	
	public static List<String> getListFromStringAsString(String value){
		List<String> arrayListOfValue=new ArrayList<String>();
		if (value !=null && value!="" &&!value.equals("0") && !value.equals("null")) {
			
			if(value.contains(",")){
				String[] valueArray=value.split(",");
				for(String e:valueArray){
					arrayListOfValue.add(e);
				}
			}else{
				arrayListOfValue.add(value);
			}
		}
		return arrayListOfValue;
	}
	
	public static String getDirectoryForBDDStoriesZipForJob(Integer testRunJobId, String baseDirectory) {
		
		if (testRunJobId == null || baseDirectory == null || baseDirectory.trim().isEmpty())
			return null;
		return baseDirectory + File.separator + "BDD" + File.separator + testRunJobId;		
	}

	public static String getDirectoryForBDDStoriesZipForJobForURL(Integer testRunJobId, String baseDirectory) {
		
		if (testRunJobId == null || baseDirectory == null || baseDirectory.trim().isEmpty())
			return null;
		return baseDirectory + "/" + "BDD" + "/" + testRunJobId;		
	}
	
	public static String roleConversion(String role) {
		String ilcmRole="";
		switch(role.trim()) {
		
		case "Tester" :
			ilcmRole="Tester";
			break;
		case "Test Lead" :
			ilcmRole="Test Lead";
			break;
		case "Test Manager" :
			ilcmRole="Test Manager";
			break;
		case "Engagement Manager":
			ilcmRole="Engagement Manager";
			break;
		/*case "SOFTWARE ENGINEER" :
			ilcmRole="Tester ";
			break;
		case "LEAD ENGINEER" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR SOFTWARE ENGINEER" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR TEST ENGINEER" :
			ilcmRole="Test Lead";
			break;	
		case "TEST LEAD" :
			ilcmRole="Test Lead";
			break;
		case "TECHNICAL LEAD" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR TECHNICAL LEAD" :
			ilcmRole="Test Lead";
			break;
		case "PROJECT MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "TECHNICAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "SENIOR TECHNICAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "ASSOCIATE GENERAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "GENERAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "DEPUTY GENERAL MANAGER":
			ilcmRole="Test Manager";
			break;
		case "MANAGER" :
			ilcmRole="Test Manager";
		break;*/
		default :
			ilcmRole="Test Manager";
			break;
		}
		
		return ilcmRole;
	}

	public static void EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validateEmail(final String hex) {
		EmailValidator();
		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
	
	public static boolean validatePhoneNumber(String phoneNumber) {

	      Pattern pattern = Pattern.compile("\\d{3}\\d{7}");
	      Matcher matcher = pattern.matcher(phoneNumber);
	      
	      if (matcher.matches()) {
	    	  return true;
	      }
	      else
	      {
	    	  return false;
	      }
	 }

	public static String encrypt(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        
        return generatedPassword;
    }
	
	public static boolean numericValidation(String testDataValue) {
		try {
			Double.parseDouble(testDataValue);
		}catch(NumberFormatException e) {
			return true;
		}catch(Exception e) {
			return true;
		}
		return false;
	}
}
