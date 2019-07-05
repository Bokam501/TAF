package com.hcl.atf.taf.mongodb.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ElasticSearchDAO;
import com.hcl.atf.taf.mongodb.service.ElasticSearchService;


@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

	private static final Log log = LogFactory
			.getLog(ElasticSearchServiceImpl.class);

	@Autowired
	private ElasticSearchDAO elasticSearchDAO;
	private Client client;
	private String collectionName;
	private String tableName;
	private String rivertype;
	private String mongoCollection;
	private String indexName;
	private String indexRequestSource;
	
	public void initializeClient(String hostName,String clusterName){
		
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
		TransportClient transportClient = new TransportClient(settings);
		String []hostArr = hostName.split(",");
		for (int i = 0; i < hostArr.length; i++) {
			transportClient.addTransportAddress(new InetSocketTransportAddress(hostArr[i], 9300));
			transportClient.addTransportAddress(new InetSocketTransportAddress(hostArr[i], 9301));
		}

		setClient(transportClient);
	}
	@Override
	@Transactional
	public String elasticSearchTestcases(ProductMaster productMaster,String type) {
		String projectName=productMaster.getProductName();
		
		if(type!=null && type.equals(0)){
				collectionName=MongodbConstants.INDEXING_TESTCASENAME;
				tableName = MongodbConstants.TESTCASES_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_TESTCASENAME;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client=getClient();
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_DEFECTS;
				tableName = MongodbConstants.DEFECTS_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_DEFECTS;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				
				collectionName=MongodbConstants.INDEXING_PRODUCT;
				tableName = MongodbConstants.PRODUCT_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTNAME;
				mongoCollection = MongodbConstants.APP_NAME+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_PRODUCTVERSION;
				tableName = MongodbConstants.PRODUCTVERSION_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTVERSION;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_PROUCTBUILD;
				tableName = MongodbConstants.PRODUCTBUILD_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTBUILD;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_FEATURENAME;
				tableName = MongodbConstants.FEATURE_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_FEATURENAME;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_TESTBEDS;
				tableName = MongodbConstants.TESTBEDS_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_TESTBEDS;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_WORPACKAGE;
				tableName = MongodbConstants.WORPACKAGE_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_WORPACKAGE;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

				collectionName=MongodbConstants.INDEXING_RESULTS;
				tableName = MongodbConstants.RESULT_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_RESULT;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();

		}else{
			if(type!=null && type.equalsIgnoreCase("1")){// Testcase collection
				collectionName=MongodbConstants.INDEXING_TESTCASENAME;
				tableName = MongodbConstants.TESTCASES_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_TESTCASENAME;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("2")){
				collectionName=MongodbConstants.INDEXING_DEFECTS;
				tableName = MongodbConstants.DEFECTS_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_DEFECTS;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
	
				
			}else if(type!=null && type.equalsIgnoreCase("3")){
				
				collectionName=MongodbConstants.INDEXING_PRODUCT;
				tableName = MongodbConstants.PRODUCT_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTNAME;
				mongoCollection = MongodbConstants.APP_NAME+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				log.info("index:"+indexRequestSource);
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
	
			}else if(type!=null && type.equalsIgnoreCase("4")){
				collectionName=MongodbConstants.INDEXING_PRODUCTVERSION;
				tableName = MongodbConstants.PRODUCTVERSION_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTVERSION;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("5")){
				collectionName=MongodbConstants.INDEXING_PROUCTBUILD;
				tableName = MongodbConstants.PRODUCTBUILD_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_PRODUCTBUILD;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("6")){
				collectionName=MongodbConstants.INDEXING_FEATURENAME;
				tableName = MongodbConstants.FEATURE_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_FEATURENAME;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("7")){
				collectionName=MongodbConstants.INDEXING_TESTBEDS;
				tableName = MongodbConstants.TESTBEDS_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_TESTBEDS;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("8")){
				collectionName=MongodbConstants.INDEXING_WORPACKAGE;
				tableName = MongodbConstants.WORPACKAGE_TABLE;
				rivertype = MongodbConstants.RIVER_WORPACKAGE;
				mongoCollection = tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \"workpackage\"}}" ;
				log.info("index:"+indexRequestSource);
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}else if(type!=null && type.equalsIgnoreCase("9")){
				collectionName=MongodbConstants.INDEXING_RESULTS;
				tableName = MongodbConstants.RESULT_TABLE;
				rivertype = projectName+MongodbConstants.RIVER_RESULT;
				mongoCollection = projectName+"."+tableName;
				indexName = (collectionName);
				indexRequestSource = "{\"type\": \"mongodb\",\"mongodb\": " +
					"{ \"db\": \"ise\",\"collection\": \""+mongoCollection+"\"}, \"index\":" +
							"{\"name\":\""+indexName+"\", \"type\": \""+projectName+"\"}}" ;
				client.index(Requests.indexRequest("_river").type(rivertype).id("_meta").source(indexRequestSource)).actionGet();
			}
		}
		client.close();
		return MongodbConstants.DEFAULT_STATUS;

	
	}
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	@Override
	@Transactional
	public void closeClient(){
		this.client.close();
	}
}