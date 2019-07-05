package com.hcl.atf.taf.controller.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.hcl.atf.taf.constants.IDPAConstants;


public class ScriptParser {
	public static void main(String[] args) {


		File f = new File(args[0]);
		String search = args[1];	        
		String projectPath = "";
		ScriptParser parser = new ScriptParser(f,search,projectPath);	      
	}

	ScriptParser(File f, String search,String projectPath ){    
		String source = "";
		if(f.toString().endsWith(".js") || f.toString().endsWith(".java")){
			String dest =  find(f, search,projectPath);

			if(dest!= null && !dest.isEmpty()){
				source = f.getPath();//"D:\\testHPQC\\script.js";		    	 
				File sourceFile = new File(source);
				File destFile = new File(dest);		    	 
				boolean isFileRenamed = fileRenameAndMove(sourceFile, destFile);
			}
		}
	}

	public String find(File f, String searchString, String projectPath) {
		boolean result = false;
		boolean mainResult = false;
		Scanner in = null;
		String className = "";
		boolean packageFound = false;
		String destLocation = ""; 
		String inputFileName = "";
		try {
			in = new Scanner(new FileReader(f));
			while(in.hasNextLine() &&(!packageFound || !mainResult)) {
				String line = in.nextLine();


				result = line.indexOf(searchString) >= 0;
				mainResult = line.indexOf("class") >= 0;
				if(result){
					packageFound = true;

					int index = line.indexOf("com");
					if(index >= 0){
						String processLine = line.substring(line.indexOf("com"));	                	 
						if(processLine!= null){
							String processLine2 = processLine.substring(0,processLine.indexOf(";"));

							String packageString =  processLine2.replace(".","\\");

							destLocation = projectPath + "\\src\\" + packageString;
							boolean isDirectoryCreated = checkAndCreateDirectory(destLocation);

						}
					}
				}
				if(mainResult){

					boolean isClassNameExists = line.contains("class");
					Scanner scan = new Scanner(line).useDelimiter(" ");	                	 	
					while(scan.hasNext()){
						String token = scan.next();
						if(token.equalsIgnoreCase("class")){
							className = scan.next();

							destLocation = destLocation + "\\"+ className + ".java";
						}

					}
					if(className == null || (className != null && className.isEmpty())){
						mainResult = false;
					}
					scan.close();
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();      
		}
		finally {
			try { in.close() ; } catch(Exception e) { /* ignore */ }  
		}
		return destLocation;
	}


	public boolean checkAndCreateDirectory(String location){
		File file = new File(location);
		boolean isDircreated = false; 
		if(file.exists()){
			if(file.isFile()){
				file.delete();
				isDircreated = file.mkdirs();
			}			
		}else{
			isDircreated = file.mkdirs();
		}	
		return isDircreated;
	}

	public boolean fileRenameAndMove(File source, File dest){


		File destDir = new File(dest.toString().substring(0,dest.toString().lastIndexOf("\\")));

		boolean isFileRenamed = false;

		isFileRenamed =  source.renameTo(dest);	    	


		return isFileRenamed;	    
	}
	
	public static String getFileExtensionForScriptLanguage(String languageName) {
		
		if (languageName == null || languageName.trim().isEmpty())
			return "";
		
		if (languageName.equalsIgnoreCase(IDPAConstants.TEST_SCRIPT_LANGUAGE_JAVA))
			return ".java";
		else if (languageName.equalsIgnoreCase(IDPAConstants.TEST_SCRIPT_LANGUAGE_JAVASCRIPT))
			return ".js";
		// Need to do this for other engines
		
		return "";
	}
	

	public static String getLanguageForTestEngine(String testEngineName) {
		String languageName= "";
		switch(testEngineName){
			case IDPAConstants.TEST_TOOL_SELENIUM:
				languageName="JAVA_TESTNG";
				break;
			case IDPAConstants.TEST_TOOL_APPIUM:
				languageName="JAVA_TESTNG";
				break;
			case IDPAConstants.TEST_TOOL_SEETEST:
				languageName="JAVA_TESTNG";
				break;
			default:
				languageName="JAVA_TESTNG";
				break;
		}
		return languageName;
	}
}

