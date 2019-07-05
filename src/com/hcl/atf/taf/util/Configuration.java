package com.hcl.atf.taf.util;

import java.io.File;

public class Configuration {

	public static void checkAndCreateDirectory(String FolderPath ){
		// creates the directory if it does not exist
		File uploadDir = new File(FolderPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
	}
	
	public static void checkAndRemoveFile(String FilePath){
		File file = new File(FilePath);
		if(file.exists()){
			if(file.isFile()){file.delete();}
		}			
	}
	
	public static boolean isValidFileOrFolder(String FolderPath){
		File file = new File(FolderPath);
		if(file.exists())
			return true;
		else
			return false;
	}
}
