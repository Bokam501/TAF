package com.hcl.atf.taf.controller.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.IOUtils;


public class ZipTool {
	
	public static void unzip(String source,String destination){
	    try {
	         ZipFile zipFile = new ZipFile(source);
	         if (zipFile.isValidZipFile()) {
			    zipFile.extractAll(destination);
			 }
	    } catch (ZipException e) {
	        e.printStackTrace();
	    }
	}
	
	 public static void unpackFileFromLocal(String lib, File out) {
	    //
	    InputStream      in =
	        Thread.currentThread().getContextClassLoader().getResourceAsStream("win32-x86/" + lib);
	    if (in == null) {
	      System.err.println("Cannot load " + lib);
	    }

	    File o = new File(out, lib);
	    try {
	      FileOutputStream w = new FileOutputStream(o);
	      IOUtils.copy(in, w);
	      IOUtils.closeQuietly(w);
	      IOUtils.closeQuietly(in);
	      //
	    } catch (IOException e) {
	      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	    }
	}
	 
	//Added for Evidence module
	public static String dozip(String folderToAdd){
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(folderToAdd + ".zip");
			
			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();
			
			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			
			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			
			// Add folder to the zip file
			zipFile.addFolder(folderToAdd, parameters);
			return folderToAdd + ".zip";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isValidZipArchive(String filePath){
		try
		{
			ZipFile zipFile = new ZipFile(filePath);
	      	
			if (zipFile.isValidZipFile())
				return true;
			else
				return false;
		} catch (ZipException e) {
			e.printStackTrace();
			return false;
		}
	}
}
