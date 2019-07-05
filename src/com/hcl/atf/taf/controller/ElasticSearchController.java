package com.hcl.atf.taf.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.mongodb.service.ElasticSearchService;
import com.hcl.atf.taf.service.ProductListService;

@Controller
public class ElasticSearchController {

	private static final Log log = LogFactory.getLog(ElasticSearchController.class);
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ProductListService productListService;
	@Value("#{ilcmProps['ELASTIC_SEARCH_HOST']}")
    private String hostName;
	@Value("#{ilcmProps['CLUSTER_NAME']}")
    private String clusterName;
	
	@RequestMapping(value="elastic.search.testcase",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse elasticSearchTestCases(@RequestParam int productId,@RequestParam String type) {
			log.debug("inside elastic.search.testcase");
			JTableResponse jTableResponse = null;
			try{
				ProductMaster productMaster =productListService.getProductDetailsById(productId);
				elasticSearchService.initializeClient(hostName,clusterName);
				elasticSearchService.elasticSearchTestcases(productMaster,type);
				elasticSearchService.closeClient();

			    jTableResponse = new JTableResponse("SUCCESS","Testcase Indexed successfully");
			}catch (Exception e) {
			    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			    log.error("JSON ERROR", e);	            
			}
	        return jTableResponse;
	}

}