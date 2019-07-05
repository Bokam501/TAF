package com.hcl.atf.taf.controller;

import org.springframework.beans.factory.annotation.Value;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class ConnectionPool {
	@Value("#{mongoProps['SERVER_IP']}")
    private static String server_Ip;
	@Value("#{mongoProps['PORT']}")
    private static String port;
	@Value("#{mongoProps['DB_NAME']}")
    private String db_Name;
	
	private static ConnectionPool instance;
	protected ConnectionPool() {
	}

	public static ConnectionPool getInstance() {
		if (instance==null)
			instance = new ConnectionPool();
		return instance;
	}
	public enum ConnectionFactory {
		CONNECTION;
		private MongoClient client = null;

		private ConnectionFactory() {
			try {
					
				client = new MongoClient("10.108.57.20" , 27017);
			//	client = new MongoClient("10.98.11.18" , 27017);				
				

			} catch (Exception e) {
				
			}
		}
		public MongoClient getClient() {
			if (client == null)
				throw new RuntimeException();
			return client;
		}
	}
	
	public DB getMongoDB(){		
		DB db = null;
		String DB_NAME="ise";
		try {	
			if(DB_NAME!=null){
				MongoClient mongoClient = ConnectionFactory.CONNECTION.getClient();
				db = mongoClient.getDB(DB_NAME);
			}
			

		} catch(Exception e){
			
		}		
		return db;
	}
	/*private String getMongoDbFromPropertyFile(){
		String dbName = "palm";	
		Properties props = new Properties();
		String apphome="";
		try {
			//String appHome = CommonUtils.getApplicationHomePath();
			//props.load(new FileInputStream(apphome+File.separator+"config"+File.separator+"upload_config.properties"));	

			if(null != props.getProperty("DB_NAME")){
				dbName = props.getProperty("DB_NAME").trim();
			}
		} catch (FileNotFoundException e) {
			//
		} catch (IOException e) {
			//
		}catch (Exception e) {
			//
		}

		return dbName;
	}*/	

	
}
