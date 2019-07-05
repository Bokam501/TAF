package com.hcl.atf.taf.controller.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SciptJarGenerator {
	
  private static final Log log = LogFactory.getLog(SciptJarGenerator.class);
  public static int BUFFER_SIZE = 10240;
  
  public void generateJarandZip(File jarSourcelocation, File targetDir, String projectPath, String uploadPath){
	 
	  try {
		  log.info("starting JAR creation");		 
		  generateJar(jarSourcelocation,targetDir, projectPath, uploadPath);    

		  log.info("starting ZIP File creation");	
		  String zipFile = dozip(targetDir, projectPath, uploadPath);
		  log.info("Zipfile : "+zipFile);
		
	} catch (Exception e) {		
		log.error("Exception in generating JAR and ZIP files",e);
	}
  }
  
  public void createJarArchive(File archiveFile, File[] tobeJared) {
    try {
      byte buffer[] = new byte[BUFFER_SIZE];
      // Open archive file
      FileOutputStream stream = new FileOutputStream(archiveFile);
      JarOutputStream out = new JarOutputStream(stream, new Manifest());

      for (int i = 0; i < tobeJared.length; i++) {
        if (tobeJared[i] == null || !tobeJared[i].exists()
            || tobeJared[i].isDirectory()){    
        	
        	
        	JarEntry jarAdd = new JarEntry(tobeJared[i].getName());
        	jarAdd.setTime(tobeJared[i].lastModified());
            out.putNextEntry(jarAdd);
        	continue; // Just in case...
        }
        
        log.info("Adding " + tobeJared[i].getName());

        // Add archive entry
        JarEntry jarAdd = new JarEntry(tobeJared[i].getName());
        jarAdd.setTime(tobeJared[i].lastModified());
        out.putNextEntry(jarAdd);

        // Write file to archive
        FileInputStream in = new FileInputStream(tobeJared[i]);
        while (true) {
          int nRead = in.read(buffer, 0, buffer.length);
          if (nRead <= 0)
            break;
          out.write(buffer, 0, nRead);
        }
        in.close();
      }

      out.close();
      stream.close();
      log.info("Adding completed OK");
    } catch (Exception ex) {
      log.error("Exception in adding jars: " + ex.getMessage());
    }
  }

           

///

public void generateJar(File Source, File targetDir, String projectPath, String uploadPath) throws IOException
{
  Manifest manifest = new Manifest();
  
  if(!targetDir.exists()){
	  targetDir.mkdirs();	
  }
  
  String sourceFile =  Source.toString();
  File targetFile = new File(targetDir + "\\" + projectPath+".jar");
  
  manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
  JarOutputStream target = new JarOutputStream(new FileOutputStream(targetFile), manifest);
 
  add(new File(sourceFile+ "\\bin\\com"),target);
  add(new File(sourceFile+ "\\lib"),target);
 
  target.close();
 
}

private void add(File source, JarOutputStream target) throws IOException
{
	
   BufferedInputStream in = null;
  try
  {
    if (source.isDirectory())
    {      
      for (File nestedFile: source.listFiles())
        add(nestedFile, target);
      return;
    }
    
    String nameEntry = source.getPath().replace("\\", "/");
    
    if(nameEntry.contains("com/")){
    	nameEntry = nameEntry.substring(nameEntry.indexOf("com/"));
    }    
    
    if(nameEntry.contains("lib/")){
    	nameEntry = nameEntry.substring(nameEntry.indexOf("lib/"));
    }    
   
    JarEntry entry = new JarEntry(nameEntry);
    entry.setTime(source.lastModified());
    target.putNextEntry(entry);
    in = new BufferedInputStream(new FileInputStream(source));

    byte[] buffer = new byte[1024];
    while (true)
    {
      int count = in.read(buffer);
      if (count == -1)
        break;
      target.write(buffer, 0, count);
    }
    target.closeEntry();       
  }
  finally
  {
    if (in != null)
      in.close();
  }
}

public String dozip(File folderToAdd, String projectPath, String uploadPath){
	try {
		String baseDirectory = "";
		int rootIndex = 0;
		//String projectPath = "JNJ_demo_TRCC_13"; 
		log.info("started Zipping");
		
		File file1 = new File(uploadPath);
		file1.mkdirs();
	
		// Initiate ZipFile object with the path/name of the zip file.
		ZipOutputStream  zos = new ZipOutputStream(new FileOutputStream(file1 + "\\"+ projectPath + ".zip"));
		// Initiate Zip Parameters which define various properties such
		// as compression method, etc.
		ZipParameters parameters = new ZipParameters();
		
		// set compression method to store compression
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		
		// Set the compression level
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		
		// Add folder to the zip file
		
		if(folderToAdd.isDirectory()){
			rootIndex = folderToAdd.getPath().length()+1;//indexOf(folderToAdd.getName());
			
			File[] files = folderToAdd.listFiles();
			for (int j = 0; j < files.length; j++) {
				 addZipFiles(rootIndex, files[j],zos);
			}				
		}
		
		
		zos.close();
		return uploadPath + "\\"+projectPath+".zip";
	} catch (Exception e) {
		log.error("Exception in adding zip files ",e);
		return "";
	}
}


private void addZipFiles(int rootIndex, File source, ZipOutputStream target) throws IOException
{
 BufferedInputStream  in = null;
 File[] files = null;
 if(source.isDirectory()){
	 files =  source.listFiles();
 } else if(source.isFile()){
	 files = new File[]{source};
 }
	 
 if(files!= null){
	 for (int i = 0; i < files.length; i++) {
		 if(files[i].isDirectory()){
			
			 ZipEntry entry = new ZipEntry(files[i].toString().substring(rootIndex)+ "/");
			 log.info("directory added:" + files[i].getName());
			 entry.setTime(files[i].lastModified());
			 target.putNextEntry(entry);
			 addZipFiles(rootIndex, files[i],target); 
			 continue;
		 } 
    try {
    	
	    byte[] buffer = new byte[1024];
	   
	    ZipEntry entry = new ZipEntry(files[i].toString().substring(rootIndex));
	    log.info("started zipping files" + files[i].getName());
	    entry.setTime(files[i].lastModified());
	    target.putNextEntry(entry);
	    in  = new BufferedInputStream(new FileInputStream(files[i]));
	      
	    while(true){
	    	int count = in.read(buffer);
	    	if(count == -1) 
	    		break;
	    	  target.write(buffer, 0, count);
	    	}
	    log.info("Files zipped" + files[i].getName());
	    target.closeEntry();  
	    if ( in != null)
	    	 in.close();
	  } catch(IOException ioe){
		  log.info("IOException:" + ioe);
	  }
		  }
	  }
	  
	}


}
         
