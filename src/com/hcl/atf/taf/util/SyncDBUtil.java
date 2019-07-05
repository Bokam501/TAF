package com.hcl.atf.taf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SyncDBUtil {
	private static final Log log = LogFactory.getLog(SyncDBUtil.class);

	public static void Backupdbtosql() {
	    try {

	    	String jarDir="D:";
	        String dbName = "ilcm_workflow";
	        String dbUser = "root";
	        String dbPass = "root";

	        /*NOTE: Creating Path Constraints for folder saving*/
	        /*NOTE: Here the backup folder is created for saving inside it*/
	        String folderPath = jarDir + "\\backup";

	        /*NOTE: Creating Folder if it does not exist*/
	        File f1 = new File(folderPath);
	        f1.mkdir();

	        /*NOTE: Creating Path Constraints for backup saving*/
	        /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
	         String savePath = jarDir + "\\backup\\" + "backup_ilcm_workflow.sql\"";

	        /*NOTE: Used to create a cmd command*/
	        String executeCmd = "C:/Program Files/MySQL/MySQL Server 5.1/bin/mysqldump -u" + dbUser + " -p" + dbPass + " --database " + dbName + " -r " + savePath;

	        /*NOTE: Executing the command here*/
	        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	        int processComplete = runtimeProcess.waitFor();

	        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
	        if (processComplete == 0) {
	            log.info("Backup Complete");
	        } else {
	            log.info("Backup Failure");
	        }

	    } catch (Exception ex) {
	        log.info("Error at Backuprestore" + ex.getMessage());
	    }
	}
	public static String getData(String host, String port, String user, String password, String db) throws Exception {

		log.info("started...");
		String jarDir="D:";
		String folderPath = jarDir + "\\backup";
		File f1 = new File(folderPath);
        f1.mkdir();
		String savePath = jarDir + "\\backup\\" + "backup_"+db+".sql\"";
	    Process run = Runtime.getRuntime().exec(
	            "C:/Program Files/MySQL/MySQL Server 5.1/bin/mysqldump --host="  + host + " --port=" + port + 
	            " --user=" + user + " --password=" + password +
	            " --compact --databases --add-drop-table --complete-insert --extended-insert " +
	            "--skip-comments --skip-triggers "+ db+ " -r " + savePath);

	    InputStream in = run.getInputStream(); 
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    StringBuffer temp = new StringBuffer();
	    int count;
	    char[] cbuf = new char[1024];

	    while ((count = br.read(cbuf, 0, 1024)) != -1)
	        temp.append(cbuf, 0, count);

	    br.close();
	    in.close();
	    log.info("completed..."+temp.toString());
	    return temp.toString();
	}
	public static void Restoredbfromsql(String host, String port,String s) {
		log.info("Calling restore...");
        try {
            /*NOTE: String s is the mysql file name including the .sql in its name*/
            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
          
        	String jarDir="";
            /*NOTE: Creating Database Constraints*/
             String dbName = "pms";
             String dbUser = "root";
             String dbPass = "admin";

            /*NOTE: Creating Path Constraints for restoring*/
            String restorePath = jarDir + "\\backup" + "\\backup_" + dbName+".sql";

            /*NOTE: Used to create a cmd command*/
            /*NOTE: Do not create a single large string, this will cause buffer locking, use string array*/
            String[] executeCmd = new String[]{"C:/Program Files/MySQL/MySQL Server 5.1/bin/mysql", dbName, "-u" + dbUser, "-p" + dbPass, "-e", " source " + restorePath};
            
            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            Process runtimeProcess = Runtime.getRuntime().exec("C:/Program Files/MySQL/MySQL Server 5.1/bin/mysql --host="  + host + " --port=" + port + 
    	            " --user=" + dbUser + " --password=" + dbPass +
    	            " -e " + restorePath);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
            	log.info("Successfully restored from SQL : " + s);
            } else {
            	log.info("Error at restoring");
            }


        } catch (Exception ex) {
            log.info("Error at Restoredbfromsql" + ex.getMessage());
        }
        log.info("Finished..");
    }
	public static void main(String[] args) {
		try {
			getData("10.108.66.181", "3306", "root", "root", "pms");
			Restoredbfromsql("localhost", "3306","");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
