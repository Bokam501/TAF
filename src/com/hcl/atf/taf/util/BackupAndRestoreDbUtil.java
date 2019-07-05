package com.hcl.atf.taf.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BackupAndRestoreDbUtil {
	private static final Log log = LogFactory.getLog(BackupAndRestoreDbUtil.class);

	public static void main(String[] args){
	
	try{
	
		String cmdPath="cmd,exe /c";
		String mysqlPath="C:/Program Files/MySQL/MySQL Server 5.1/bin/mysql";
		String dbName="pms";
		String mysqlFilePath="D:\\backup" + "\\backup_" + dbName+".sql";
		String batDir="D:";
		String batFilePath = batDir + "\\backup\\restoredb.bat";
		String dbUserName="root";
		String dbPassword="root";
		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public boolean restoreDatabase(String mysqlDriveLocation,String mysqlPath, String host, String port, String dbUserName, String dbPassword, String dbName, String mysqlFilePath,String batFilePath, String mysqlBinLoc) throws Exception {
		
		try{
			String mysqlURL ="jdbc:mysql://"+host+":"+port+"/";
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection mysqlconn = DriverManager.getConnection(mysqlURL,dbUserName,dbPassword);
			
			Statement stmtcreatedb = mysqlconn.createStatement();
			int rscreatedb = stmtcreatedb.executeUpdate("CREATE DATABASE IF NOT EXISTS "+dbName);
			if(rscreatedb == 1){
				log.info("Database created successfully");
			}
			else{
				log.info("Unable to create Database");
			}
			stmtcreatedb.close();
			mysqlconn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		writeBatFile(mysqlDriveLocation,mysqlPath, host, port,dbUserName, dbPassword, dbName, mysqlFilePath,batFilePath,mysqlBinLoc,"mysql");
	    
	    final Process run = Runtime.getRuntime().exec("cmd /c start /wait  "+batFilePath+" ");
	    new Thread(new Runnable() {
            public void run() {
             BufferedReader input = new BufferedReader(new InputStreamReader(run.getInputStream()));
             String line = null; 

             try {
                while ((line = input.readLine()) != null)
                    log.info(">>>"+line);
             } catch (IOException e) {
                    e.printStackTrace();
             }
            }
        }).start();
	    
	    int processComplete = run.waitFor();
        if (processComplete == 0) {
            log.info("Restored db successfully!");
        } else {
            log.info("Could not restore the db backup!");
        }
	    return true;

	}
	
	public void writeBatFile(String mysqlDriveLocation,String mysqlPath, String host, String port, String dbUserName, String dbPassword, String dbName, String mysqlFilePath, String batFilePath, String mysqlBinLoc, String executionType) throws Exception{
		//create StringBuffer object
        StringBuffer sbf = new StringBuffer();
        sbf.append("@echo off");
        sbf.append(System.getProperty("line.separator"));
        sbf.append("CLS");
        sbf.append(System.getProperty("line.separator"));
        sbf.append(mysqlDriveLocation);
        sbf.append(System.getProperty("line.separator"));
        sbf.append("cd "+mysqlBinLoc);
        sbf.append(System.getProperty("line.separator"));
        
        if(executionType.equals("mysql"))
        	sbf.append("mysql " + dbName + " --host=" + host + " --port=" + port + " --user=" + dbUserName + " --password=" + dbPassword + " < " + mysqlFilePath);
        else
        	sbf.append("mysqldump --host="  + host + " --port=" + port + " --user=" + dbUserName + " --password=" + dbPassword +" "+ dbName+ " -r " + mysqlFilePath);
        sbf.append(System.getProperty("line.separator"));
        sbf.append("exit");
        
       
        /*
         * To write contents of StringBuffer to a file, use
         * BufferedWriter class.
         */
       
        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(batFilePath)));
       
        //write contents of StringBuffer to a file
        bwr.write(sbf.toString());
       
        //flush the stream
        bwr.flush();
       
        //close the stream
        bwr.close();
       
        log.info("Bat file created.");
	}
	public String getData(String mysqlDriveLocation,String mysqlPath, String host, String port, String dbUserName, String dbPassword, String dbName, String mysqlFilePath,String batFilePath, String mysqlBinLoc) throws Exception {

		log.info("db backup started...");
		
		writeBatFile(mysqlDriveLocation,mysqlPath, host, port,dbUserName, dbPassword, dbName, mysqlFilePath,batFilePath,mysqlBinLoc,"mysqldump");
		Process run = Runtime.getRuntime().exec("cmd /c start /wait "+batFilePath+" ");
	    InputStream in = run.getInputStream(); 
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    StringBuffer temp = new StringBuffer();
	    int count;
	    char[] cbuf = new char[1024];

	    while ((count = br.read(cbuf, 0, 1024)) != -1)
	        temp.append(cbuf, 0, count);

	    br.close();
	    in.close();
	    log.info("db backup completed...");
	    return temp.toString();
	}
	public static boolean restoreDB(String mysqlPath, String host, String port, String dbUserName, String dbPassword, String dbName, String mysqlFilePath) {
	
		boolean status = false;
		
		String[] restoreCmd = new String[]{"C:/Program Files/MySQL/MySQL Server 5.1/bin/mysql.exe", "--host="  + host , "--port=" + port , "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + mysqlFilePath};
		 
        Process runtimeProcess;
        try {
        	int rscreatedb = -1;
        	String mysqlURL ="jdbc:mysql://"+host+":"+port+"/";
    		Class.forName("com.mysql.jdbc.Driver");
    		
    		Connection mysqlconn = DriverManager.getConnection(mysqlURL,dbUserName,dbPassword);
    		ResultSet rs = null;
    		boolean dbExists=false;
    		if(mysqlconn != null){
				
				rs = mysqlconn.getMetaData().getCatalogs();

				while(rs.next()){
					String catalogs = rs.getString(1);					
					if(dbName.equals(catalogs)){
						log.info("the database "+dbName+" exists");
						dbExists=true;
					}
				}
				log.info("dbExists>>>"+dbExists);
				if(dbExists){
					
				}else{
					Statement stmtcreatedb = mysqlconn.createStatement();
	    			rscreatedb = stmtcreatedb.executeUpdate("CREATE DATABASE IF NOT EXISTS "+dbName);
	    			if(rscreatedb == 1){
	    				log.info("Database created successfully");
	    			}
	    			else{
	    				log.info("Unable to create Database");
	    			}
					runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
		            int processComplete = runtimeProcess.waitFor();
		 
		            if (processComplete == 0) {
		                log.info("Restored successfully!");
		                status = true;
		            } else {
		                log.info("Could not restore the backup!");
		                status = false;
		            }
				}

			}
			else{
				log.info("unable to create database connection");
			}
        	
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return status;
	}
}