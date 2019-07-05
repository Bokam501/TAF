package com.hcl.atf.taf.controller.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileParser {
  
private static final Log log = LogFactory.getLog(FileParser.class);
  
public static String scriptName;
public static String initialCheckoutFolder;
public static String processingFolder;
public static String OutputFolder;
public static String finalScriptslocation;
public static String uploadPath;


  public static void fileParser(String scriptNameValue, String initialCheckoutPathValue, String uploadPath){
	  
	  scriptName = scriptNameValue;	
	  initialCheckoutFolder = initialCheckoutPathValue+ "\\checkoutFolder";
	  processingFolder = initialCheckoutPathValue + "\\processingFolder";
	  OutputFolder = initialCheckoutPathValue +  "\\OutputFolder";
	  finalScriptslocation = initialCheckoutPathValue +"\\finalScriptslocation";
	  
	  String source = initialCheckoutFolder;
	  String destination = processingFolder; 
	  SciptJarGenerator jarGenerator  = new SciptJarGenerator(); 
	  FileCompiler compiler = new FileCompiler();
	  
	  //Unzipping all the attachments downloaded from external source.
	  unzipAllAttachments(source, destination);	 
	  unzipRecursive(initialCheckoutFolder+"\\resources",destination);
	  
	  
	  File srcScriptsPath = new File(initialCheckoutFolder+"\\scripts");    
	  File targetScriptsPath = new File(processingFolder+"\\src");
	  
	  unzipScriptsRecursive(initialCheckoutFolder+"\\scripts", source);
	  
	    try {
	    	if(srcScriptsPath.exists()){
	    		FileUtils.copyDirectory(srcScriptsPath, targetScriptsPath);
	    	}			
		} catch (IOException e) {			
			log.error("Exception in copying directory",e);
		}
	  
	 // String root = processingFolder+"\\src";
	    String root = processingFolder;
	    FileVisitor<Path> fileProcessor = new ProcessFile();
	    
	    //Parsing each file and rename to .java
	    try {
			Files.walkFileTree(Paths.get(root), fileProcessor);
		} catch (IOException e) {
			log.error("Exception in processing root folder",e);
		} 
	    
	    
	    //Copying the lib files to folder
	    
	    File srcLibPath = new File(processingFolder+"\\lib");    
	    File targetLibPath = new File(OutputFolder+"\\lib");
	    try {
			FileUtils.copyDirectory(srcLibPath, targetLibPath);
		} catch (IOException e) {
			log.error("Exception in copying lib files to folder",e);
		}
	    
	    //copying the properties files to folder
	    File srcPropertiesPath = new File(processingFolder+"\\properties");    
	    File targetPropertiesPath = new File(OutputFolder+"\\properties");
	    try {
			FileUtils.copyDirectory(srcPropertiesPath, targetPropertiesPath);
		} catch (IOException e) {
			log.error("Exception in copying the properties files to folder",e);
		}
	    
	  //copying the bin folder
	    File srcBinPath = new File(processingFolder+"\\bin");  
	    if(!srcBinPath.exists()){
	    	srcBinPath.mkdirs();	
	    }
	    File targetBinPath = new File(OutputFolder+"\\bin");
	    try {
			FileUtils.copyDirectory(srcBinPath, targetBinPath);
		} catch (IOException e) {
			log.error("Exception in copying the bin folder",e);
		}
	    
	    //Copying all the repositories to FinalScriptLocation
	    File srcRepositoriesPath = new File(processingFolder+"\\repositories");    
	    File targetRepositoriesPath = new File(finalScriptslocation);
	    try {
	    	FileUtils.copyDirectory(srcRepositoriesPath, targetRepositoriesPath);
	    	
		} catch (IOException e) {
			log.error("Exception in moving all the repositories to FinalScriptLocation",e);
		}
	   	 
	    //Copying the source to output folder
	    File srcPath = new File(processingFolder+"\\src");    
	    File targetSrcPath = new File(OutputFolder+"\\src");
	    try {
			FileUtils.copyDirectory(srcPath, targetSrcPath);
		} catch (IOException e) {
			log.error("Exception inCopying the source to output folder",e);
		}
	    
	    //Compiling the scripts
	    File file = new File(OutputFolder+"\\src\\com\\hcl\\atf\\taf\\Main.java");
		compiler.compile(file,OutputFolder);
		
		//For Selective TestCases
		File selectiveFile = new File(OutputFolder+"\\src\\com\\hcl\\atf\\taf\\SelectiveTestCasesMain.java");
		if(selectiveFile.exists()){
			compiler.compile(selectiveFile,OutputFolder);
		}
			
		//Generating Scripts and building the final Zip file
		File sourceFile = new File(OutputFolder+"\\");		
		File targetDir = new File(finalScriptslocation);
		 
		jarGenerator.generateJarandZip(sourceFile, targetDir, scriptName,uploadPath);
  }

  private static final class ProcessFile extends SimpleFileVisitor<Path> {
	  
	@Override
    public FileVisitResult visitFile(
      Path aFile, BasicFileAttributes aAttrs
    ) throws IOException {
      log.info("Processing file:" + aFile);
      ScriptParser scriptParser = new ScriptParser(aFile.toFile(), "package", processingFolder);     
      return FileVisitResult.CONTINUE;
    }
    
    @Override  
    public FileVisitResult preVisitDirectory(
      Path aDir, BasicFileAttributes aAttrs
    ) throws IOException {
    	log.info("Processing directory:" + aDir);
      return FileVisitResult.CONTINUE;
    }
  }
  
  
  public static void unzip(String source,String destination){
		try {
			ZipFile zipFile = new ZipFile(source);
	         
			 //The following if check Added for Evidence module
	       	 if (zipFile.isValidZipFile()) {
	   	         zipFile.extractAll(destination);
	       	 }
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
  
  public static void unzipAllAttachments(String source,String destination){
	  
	  File sourceFile = new File (source);
	
	  if(sourceFile.isDirectory()){
		  File[] files = sourceFile.listFiles();
		  for (File file2 : files) {
			  if(file2.getPath().endsWith(".zip")){				
				 unzip(file2.getPath(), destination);
			  }
		}
	  } 
  }
  
  
  public static void unzipScriptsRecursive(String source,String destination){
	  
	  File sourceFile = new File (source);
	
	  if(sourceFile.isDirectory()){
		  File[] files = sourceFile.listFiles();
		  for (File file2 : files) {
			  if(file2.isDirectory()){
				  unzipAllAttachments(file2.getPath(),file2.getPath());
			  }
			  if(file2.getPath().endsWith(".zip")){				
				 unzip(file2.getPath(), destination);
			  }
		}
	  } 
  }
  
  
 public static void unzipRecursive(String source,String destination){
	  
	  File sourceFile = new File (source);
	
	  if(sourceFile.isDirectory()){
		  File[] files = sourceFile.listFiles();
		  for (File file2 : files) {
			  if(file2.isDirectory()){
				  unzipAllAttachments(file2.getPath(),destination);
			  }
			  if(file2.getPath().endsWith(".zip")){				
				 unzip(file2.getPath(), destination);
			  }
		}
	  } 
  }
	
} 
 
