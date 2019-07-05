package com.hcl.atf.taf.scriptgeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ScriptGeneratorUtilities {	 

	private static final Log log = LogFactory.getLog(ScriptGeneratorUtilities.class);

	public static String getTestCaseClassName(String testCaseName, int testCaseId, String testCaseCode, int nameSource) {
		
		String testCaseClassName = null;
		
		//Name based on Testcase name
		if (nameSource == 1) {
			testCaseClassName = removeSpecialCharactersForClassMethodNames(testCaseName.trim());
			if (testCaseClassName.length() > 100) {
				testCaseClassName = testCaseClassName.substring(0, 100);
			}
		} else if (nameSource == 2) {
			//Name based on Testcase code
			if ((testCaseCode == null || testCaseCode.trim().length() < 1) && testCaseId > 0)
				testCaseClassName = "TC_" + testCaseId;
			else
				testCaseClassName = removeSpecialCharactersForClassMethodNames(testCaseName.trim());
				if (testCaseClassName.length() > 100) {
					testCaseClassName = testCaseClassName.substring(0, 100);
				}
		} else {
			//Name based on Testcase ID
			if (testCaseId > 0)
				testCaseClassName = "TC_" + testCaseId;
			else {
				testCaseClassName = removeSpecialCharactersForClassMethodNames(testCaseName.trim());
				if (testCaseClassName.length() > 100) {
					testCaseClassName = testCaseClassName.substring(0, 100);
				}
			}
		}
		return testCaseClassName;
	}
	
	public static String getTestStepMethodName(String testCaseName, String testStepName, int testStepId, String testStepCode, int nameSource) {
		
		String testStepMethodName = testStepName;
		if (nameSource == 1) {
			testStepMethodName = "TS_"+ testStepId + "_" + removeSpecialCharactersForClassMethodNames(testStepMethodName.trim());
			if (testStepMethodName.length() > 100) {
				testStepMethodName = testStepMethodName.substring(0, 100);
			}
		} else if (nameSource == 2) {
			if (testStepCode == null || testStepCode.trim().length() < 1) 
				testStepMethodName = "TS_" + testStepId;
			else {
				testStepMethodName = "TS_" + testStepId + "_" + removeSpecialCharactersForClassMethodNames(testStepCode.trim());
				if (testStepMethodName.length() > 100) {
					testStepMethodName = testStepMethodName.substring(0, 100);
				}
			}
		} else {
			testStepMethodName = "TS_" + testStepId;
		}
		return testStepMethodName;
	}

	public static String removeSpecialCharactersForClassMethodNames(String name) {

		if (name == null) {
			return name;
		}
		name = name.trim();
		name = name.replace(" ","_");
		name = name.replace("-","_");
		name = name.replace("'","_");
		name = name.replace("(","_");
		name = name.replace(")","_");
		name = name.replace(",","_");
		name = name.replace(".","_");
		name = name.replace("<","_");
		name = name.replace(">","_");
		name = name.replace(":","_");
		name = name.replace(";","_");
		name = name.replace("&","_");
		name = name.replace("*","_");
		name = name.replace("$","_");
		name = name.replace("@","_");
		name = name.replace("!","_");
		name = name.replace("~","_");
		name = name.replace("=","_");
		name = name.replace("+","_");
		name = name.replace("[","_");
		name = name.replace("]","_");
		name = name.replace("{","_");
		name = name.replace("}","_");
		name = name.replace("|","_");
		name = name.replace("\\","_");
		name = name.replace("/","_");
		name = name.replace("?","_");
		name = name.replace("%","_");
		name = name.replace("^","_");
		name = name.replace("#","_");

		return name;
	}
	
	public static String readFile(String path, Charset encoding) throws IOException {

		try {
			if (encoding == null)
				encoding = Charset.defaultCharset();
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			return null;
		}
	}
	
	
	//New methid added below
	public static boolean insertTestCaseImportsForJavaScripts(String fileName, String importStatements, String suiteClasses, String type) {
		
	      List<String> fileLines = new ArrayList<String>();
	      Scanner scanner = null;
	      String markerPattern = null;
	      if(type.equalsIgnoreCase("selective")){
	    	  markerPattern = "package";
	      } else {
	    	  markerPattern = "import";
	      }
	      if (importStatements == null) {
	    	  log.info("Import statements provided for inserting");
	    	  importStatements = "";
	      }
	      boolean importStatementsAdded = false;
	      try {
	         scanner = new Scanner(new File(fileName));
	         while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            log.info("File Line : " + line);
	            if (line.trim().startsWith(markerPattern) && !importStatementsAdded) {
	            	if( markerPattern.contains("import") ) {
	            		fileLines.add(importStatements);	            		
	            		fileLines.add(line);
	            	} else if (markerPattern.contains("package")) {
	            		fileLines.add(line);
	            		fileLines.add(importStatements);	
	            	}
	            	importStatementsAdded =  true;
            		log.info("Added import statement");
	            } else if (line.trim().startsWith("@SuiteClasses")){
	            	if (suiteClasses == null || suiteClasses.isEmpty()) {
	            		fileLines.add(line);
	            	} else {
		               fileLines.add(suiteClasses);
		               log.info("Added suite classes");
	            	}
	            } else {
			           fileLines.add(line);
	            }

	         }

	      } catch (FileNotFoundException e) {
	         log.error("Could not find file for reading contents : " + fileName, e);
	      } finally {
	         if (scanner != null) {
	            scanner.close();
	         }
	      }

	      PrintWriter pw = null;
	      try {
	         pw = new PrintWriter(new File(fileName));
	         for (String line : fileLines) {
	            pw.println(line);
	         }
	      } catch (FileNotFoundException e) {
		         log.error("Could not find file for adding Testcase import statements : "+ fileName, e);
	      } finally {
	         if (pw != null) {
	            pw.close();
	         }
	      }
		return true;
	}
}