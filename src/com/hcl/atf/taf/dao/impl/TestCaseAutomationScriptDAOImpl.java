package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.AOTCConstants;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.model.AmdocsPageMethods;
import com.hcl.atf.taf.model.AmdocsPageObjects;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.ScriptGenerationDetails;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseScriptVersion;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.service.EntityRelationshipService;

@Repository
public class TestCaseAutomationScriptDAOImpl implements TestCaseAutomationScriptDAO{

	private static final Log log = LogFactory.getLog(TestCaseAutomationScriptDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EntityRelationshipService entityRelationshipService;

	@Override
	@Transactional
	public Integer addTestCaseAutomationScript(TestCaseAutomationStory testCaseAutomationStory) {
		log.info("adding TestCaseAutomationStory..");
		try {	
			sessionFactory.getCurrentSession().save(testCaseAutomationStory);
			log.info("add TestCaseAutomationStory successful");
		} catch (RuntimeException re) {
			log.error("add TestCaseAutomationStory failed", re);
			return -1; 
		}	
		return testCaseAutomationStory.getTestCase().getTestCaseId();
	}

	@Override
	@Transactional
	public void updateTestCaseAutomationScript(TestCaseAutomationStory testCaseAutomationScript) {
		log.debug("updating updateActivity instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseAutomationScript);
			log.debug("update TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error("update TestCaseAutomationScript failed", re);
		}
	}

	@Override
	@Transactional
	public List<TestCaseAutomationStory> listTestAutomationScripts(Integer testCaseId){
		log.debug("getting TestCaseAutomationScript");
		List<TestCaseAutomationStory> scripts = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "tcas");
			c.createAlias("tcas.testCase", "tc");
			c.add(Restrictions.eq("tc.testCaseId", testCaseId));

			scripts = c.list();
			log.debug("getting TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error("getting TestCaseAutomationScript failed", re);
		}
		return scripts;
	}


	@Override
	@Transactional
	public TestCaseAutomationStory getTestAutomationScript(Integer testCaseId, String scriptType, String testEngine, Integer maxVersionId){
		log.debug("getting TestCaseAutomationScript");
		TestCaseAutomationStory story = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "tcas");
			c.createAlias("tcas.testCase", "tc");
			c.add(Restrictions.eq("tc.testCaseId", testCaseId));
			c.add(Restrictions.eq("tcas.versionId", maxVersionId));
			@SuppressWarnings("unchecked")
			List<Object> list = c.list();
			if (list == null || list.isEmpty()) {
				log.info("Test Automation Scripts Count : " + list.size());
				return null;
			} else { 
				story = (TestCaseAutomationStory)list.get(0);
			}
			log.debug("getting TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error("getting TestCaseAutomationScript failed", re);
		}
		return story;
	}


	//Automatic Scripts
	@Override
	@Transactional
	public void addTestCaseAutomationScript(TestCaseScript testcasescript){
		log.info("adding Test Case Script Details instance");
		try {	
			sessionFactory.getCurrentSession().save(testcasescript);
			log.info("add TestCaseAutomationScript successful");

		} catch (RuntimeException re) {
			log.error("add TestCaseAutomationScript failed", re);
		}	
	}

	@Override
	@Transactional
	public List<TestCaseScript> getTestCaseAutoscripts(Integer testcaseId,Integer jtStartIndex,Integer jtPageSize) {

		List<TestCaseScript> tcscriptsList = null;

		log.info("Get the Test Case Automation Script details in DAO Impl");	
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class, "tcscript");
			c.createAlias("tcscript.testCaseList", "tcList");
			c.add(Restrictions.eq("tcList.testCaseId",testcaseId));
			c.setFirstResult(jtStartIndex);
			c.setMaxResults(jtPageSize);
			tcscriptsList = c.list();
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the Test Case Automation Script details in DAO Impl");
		}

		return tcscriptsList;
	}

	@Override
	@Transactional
	public void addTestCaseAutomationScriptVersionDAO(TestCaseScriptVersion testcasescriptversion) {
		log.info("Adding Test Case Script Version Details Instance");
		try {	
			sessionFactory.getCurrentSession().saveOrUpdate(testcasescriptversion);
			log.info("Adding Test Case Script Version Details Instance successful");

		} catch (Exception re) {
			re.printStackTrace();

		}	

	}

	@Override
	@Transactional
	public List<TestCaseScriptVersion> getTestAutomationScriptversion(Integer scriptId, Integer jtStartIndex, Integer jtPageSize) {

		List<TestCaseScriptVersion> tcscriptsVersionList = null;

		log.info("Get the Test Case Automation Script Version details in DAO Impl");	
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptVersion.class, "tcscriptversion");
			c.createAlias("tcscriptversion.testcasescript", "tcs");
			c.add(Restrictions.eq("tcs.scriptId", scriptId));
			c.setFirstResult(jtStartIndex);
			c.setMaxResults(jtPageSize);
			tcscriptsVersionList = c.list();		

		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the Test Case Automation Script details in DAO Impl");
		}

		return tcscriptsVersionList;
	}

	@Override
	@Transactional
	public List<TestCaseScriptVersion> getTestCaseScriptVersionIsSelected(Integer testCaseId){

		List<TestCaseScriptVersion> tcscriptversionIssel = null;
		log.info("Get the isSelected testcase version details in DAO Impl");
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptVersion.class, "tcscriptversion");
			c.createAlias("tcscriptversion.testcasescript", "tcs");
			c.createAlias("tcs.testCaseList", "tcl");			
			c.add(Restrictions.eq("tcl.testCaseId", testCaseId));
			c.add(Restrictions.eq("tcscriptversion.isSelected", 1));
			tcscriptversionIssel = c.list();
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the isSelected testcase version details in DAO Impl");
		}

		return tcscriptversionIssel;

	}


	@Override
	@Transactional
	public List<String> getKeywordPhrases(String productType,String testTool ) {

		List<String> keywords = new ArrayList<String>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywrhprase");


			if(productType != null && !productType.trim().isEmpty()){
				if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_WEB)){
					c.add(Restrictions.eq("keywrhprase.isWeb", 1));
				}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_MOBILE)){
					c.add(Restrictions.eq("keywrhprase.isMobile", 1));

				}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_EMBEDDED)){
					c.add(Restrictions.eq("keywrhprase.isEmbedded", 1));
				}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DESKTOP)){
					c.add(Restrictions.eq("keywrhprase.isDesktop", 1));
				}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DEVICE)){
					c.add(Restrictions.eq("keywrhprase.isDevice", 1));
				}else{

				}
			}
			if(testTool != null && !testTool.trim().isEmpty()){
				if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
					c.add(Restrictions.eq("keywrhprase.isAppium", 1));
				}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
					c.add(Restrictions.eq("keywrhprase.isSeeTest", 1));
				}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_AUTOIT)){
					c.add(Restrictions.eq("keywrhprase.isAutoIt", 1));
				}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
					c.add(Restrictions.eq("keywrhprase.isSelenium", 1));
				}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_ROBOTIUM)){
					c.add(Restrictions.eq("keywrhprase.isRobotium", 1));
				}else{

				}
			}
			c.add(Restrictions.eq("keywrhprase.status", 1));


			c .setProjection(Projections.property("keywrhprase.keywordPhrase").as("keywordPhrase"));

			keywords = c.list();

		} catch (RuntimeException re) {
			log.error("Unable to get keyword phrases", re);
			// throw re;
		}
		return keywords;
	}

	@Override
	@Transactional
	public List<BDDKeywordsPhrases> getBDDKeywordsPhrases(String productType,String testTool,Integer jtStartIndex,
			Integer jtPageSize) {
		List<BDDKeywordsPhrases> bddList = null;

		Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywrhprase");
		if(productType != null && !productType.trim().isEmpty()){
			if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_WEB)){
				c.add(Restrictions.eq("keywrhprase.isWeb", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_MOBILE)){
				c.add(Restrictions.eq("keywrhprase.isMobile", 1));

			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_EMBEDDED)){
				c.add(Restrictions.eq("keywrhprase.isEmbedded", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DESKTOP)){
				c.add(Restrictions.eq("keywrhprase.isDesktop", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DEVICE)){
				c.add(Restrictions.eq("keywrhprase.isDevice", 1));
			}else{

			}
		}
		if(testTool != null && !testTool.trim().isEmpty()){
			if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
				c.add(Restrictions.eq("keywrhprase.isAppium", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
				c.add(Restrictions.eq("keywrhprase.isSeeTest", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_AUTOIT)){
				c.add(Restrictions.eq("keywrhprase.isAutoIt", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
				c.add(Restrictions.eq("keywrhprase.isSelenium", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_ROBOTIUM)){
				c.add(Restrictions.eq("keywrhprase.isRobotium", 1));
			}else{
				return bddList;
			}
		}
		c.add(Restrictions.eq("keywrhprase.status", 1));
		if(jtStartIndex != null){
			c.setFirstResult(jtStartIndex);

		}
		if(jtPageSize != null){
			c.setMaxResults(jtPageSize);

		}

		bddList = c.list();
		return bddList;
	}

	@Override
	@Transactional
	public void updateBDDKeyWordsPhrase(
			BDDKeywordsPhrases bddKeyWordsPhrasesFromUI) {	

		try{
			sessionFactory.getCurrentSession().saveOrUpdate(bddKeyWordsPhrasesFromUI);
		}catch(Exception ex){

			log.info("Error in getting the updating bddkeywordsphrases in DAO Impl");
		}


	}

	@Override
	@Transactional
	public List<TestCaseScriptVersion> getTestCaseScriptVersionStatus(Integer scriptVersionId , Integer status){

		List<TestCaseScriptVersion> tcscriptversionstatus = null;
		log.info("Get the isSelected testcase version details in DAO Impl");
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptVersion.class, "tcscriptversion");
			c.add(Restrictions.eq("tcscriptversion.scriptVersionId", scriptVersionId));
			c.add(Restrictions.eq("tcscriptversion.status", 1));
			tcscriptversionstatus = c.list();


		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the isSelected testcase version details in DAO Impl");
		}

		return tcscriptversionstatus;

	}

	@Override
	@Transactional
	public Integer getBDDKeywordsPhrasesListSize(String productType,
			String testTool) {
		Integer  bDDkeywordsCount=0;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywrhprase");
		if(productType != null && !productType.trim().isEmpty()){
			if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_WEB)){
				c.add(Restrictions.eq("keywrhprase.isWeb", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_MOBILE)){
				c.add(Restrictions.eq("keywrhprase.isMobile", 1));

			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_EMBEDDED)){
				c.add(Restrictions.eq("keywrhprase.isEmbedded", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DESKTOP)){
				c.add(Restrictions.eq("keywrhprase.isDesktop", 1));
			}else if(productType.equalsIgnoreCase(IDPAConstants.PRODUCT_DEVICE)){
				c.add(Restrictions.eq("keywrhprase.isDevice", 1));
			}else{

			}
		}
		if(testTool != null && !testTool.trim().isEmpty()){
			if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
				c.add(Restrictions.eq("keywrhprase.isAppium", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
				c.add(Restrictions.eq("keywrhprase.isSeeTest", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_AUTOIT)){
				c.add(Restrictions.eq("keywrhprase.isAutoIt", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
				c.add(Restrictions.eq("keywrhprase.isSelenium", 1));
			}else if(testTool.equalsIgnoreCase(IDPAConstants.TEST_TOOL_ROBOTIUM)){
				c.add(Restrictions.eq("keywrhprase.isRobotium", 1));
			}else{
				return bDDkeywordsCount;
			}
		}
		c.add(Restrictions.eq("keywrhprase.status", 1));
		c.setProjection(Projections.countDistinct("keywrhprase.id"));

		String count =""+  c.uniqueResult();

		bDDkeywordsCount=Integer.parseInt(count);


		return bDDkeywordsCount;
	}

	@Override
	@Transactional
	public List<KeywordLibrary> listKeywordLibraryByKeywordId(Integer keywordId) {
		log.debug("getting TestCaseAutomationScript");
		List<KeywordLibrary> keywordLibraryList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywordLibrary");
			c.createAlias("keywordLibrary.keywords", "keywords");
			c.add(Restrictions.eq("keywords.id", keywordId));

			keywordLibraryList = c.list();
			if(keywordLibraryList != null){
				for (KeywordLibrary keywordLibr : keywordLibraryList) {
					Hibernate.initialize(keywordLibr.getKeywords());
					Hibernate.initialize(keywordLibr.getLanguage());
					Hibernate.initialize(keywordLibr.getTestToolMaster());
					Hibernate.initialize(keywordLibr.getUser());
				}
			}
			log.debug("getting keywordLibraryList successful");
		} catch (RuntimeException re) {
			log.error("getting keywordLibraryList failed", re);
		}
		return keywordLibraryList;
	}

	@Override
	@Transactional
	public List<String> getKeywordLibrariesbyName(String testEngine) {

		List<String> classNamesList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywrdLib");
			c.createAlias("keywrdLib.testToolMaster", "testToolMaster");
			c.add(Restrictions.eq("testToolMaster.testToolName", testEngine));
			c.add(Restrictions.eq("keywrdLib.status", IDPAConstants.STATUS_COMPLETED));
			c .setProjection(Projections.property("keywrdLib.className").as("className"));
			classNamesList = c.list();
		}catch(Exception e){
			log.error(e);
		}
		return 	classNamesList;
	}

	@Override
	@Transactional
	public List<BDDKeywordsPhrases> getMyKeywords(Integer userId,
			Integer jtStartIndex, Integer jtPageSize) {
		List<BDDKeywordsPhrases> keywordList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "bddKeywrds");
			if(userId != -1){
				c.createAlias("bddKeywrds.user", "user");

				c.add(Restrictions.eq("user.userId", userId));
			}
			if(jtStartIndex != null){
				c.setFirstResult(jtStartIndex);

			}
			if(jtPageSize != null){
				c.setMaxResults(jtPageSize);

			}

			keywordList = c.list();

			if (keywordList != null && !keywordList.isEmpty()) {
				for(BDDKeywordsPhrases keywrd: keywordList){
					Hibernate.initialize(keywrd.getUser());
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return keywordList;
	}

	@Override
	@Transactional
	public Integer getMyKeywordsSize(Integer userId) {
		int count =0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "bddKeywrds");
			if(userId != -1){
				c.createAlias("bddKeywrds.user", "user");

				c.add(Restrictions.eq("user.userId", userId));
			}
			c.setProjection(Projections.countDistinct("bddKeywrds.id"));
			String res = ""+	c.uniqueResult();
			count = Integer.parseInt(res);

		} catch (Exception e) {
			log.error(e);
		}
		return count;
	}

	@Override
	@Transactional
	public KeywordLibrary getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(
			Integer testToolId, Integer languagID, String type, Integer id) {
		KeywordLibrary keywrdLib = null;
		List<KeywordLibrary> keywrdLibList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywrdLib");
			c.createAlias("keywrdLib.testToolMaster", "testToolMaster");
			c.createAlias("keywrdLib.language", "language");
			c.createAlias("keywrdLib.keywords", "keywords");
			c.add(Restrictions.eq("testToolMaster.testToolId", testToolId));
			c.add(Restrictions.eq("language.id", languagID));
			c.add(Restrictions.eq("keywrdLib.type", type));
			c.add(Restrictions.eq("keywords.id", id));
			keywrdLibList = c.list();
			if (keywrdLibList != null && !keywrdLibList.isEmpty()) {
				keywrdLib = keywrdLibList.get(0);
			}
		}catch(Exception e){
			log.error(e);
		}
		return keywrdLib;
	}

	@Override
	@Transactional
	public int totalKeywordLibraryByKeywordId(Integer keywordId) {
		int count =0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywrdLib");
			c.createAlias("keywrdLib.keywords", "keyword");

			c.add(Restrictions.eq("keyword.id", keywordId));
			c.setProjection(Projections.countDistinct("keywrdLib.id"));
			String res = ""+	c.uniqueResult();
			count = Integer.parseInt(res);

		} catch (Exception e) {
			log.error(e);
		}
		return count;

	}

	@Override
	@Transactional
	public BDDKeywordsPhrases getBDDKeyWordsPhraseByKeywordPharse(
			String keywordPhrase) {
		List<BDDKeywordsPhrases> keywordList = null;
		BDDKeywordsPhrases KeywordsPhrases = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywrd");

			c.add(Restrictions.eq("keywrd.keywordPhrase",""+keywordPhrase));
			keywordList = c.list();
			if (keywordList != null && !keywordList.isEmpty()) {
				KeywordsPhrases = keywordList.get(0);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return KeywordsPhrases;
	}




	@Override
	@Transactional
	public Integer saveKeywordLibrary(KeywordLibrary keywordLibrary) {
		log.info("adding saveKeywordLibrary instance");
		try {	
			sessionFactory.getCurrentSession().save(keywordLibrary);
			log.info("add saveKeywordLibrary successful");
			return keywordLibrary.getId();
		} catch (Exception re) {
			log.error("add saveKeywordLibrary failed", re);
			return null; 
			//throw re;
		}	
	}

	@Override
	@Transactional
	public void updateKeywordLibrary(KeywordLibrary keywordLibrary) {
		log.debug("updating saveKeywordLibrary instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(keywordLibrary);
			log.debug("update saveKeywordLibrary successful");
		}catch (Exception re) {
			log.error("add saveKeywordLibrary failed", re);
			//throw re;
		}	
	}

	@Override
	@Transactional
	public List<KeywordLibrary> getkeywordLibById(
			Integer keywrdLibId) {
		List<KeywordLibrary> keywordLibraryList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywrdLib");
			if(keywrdLibId!=null){

				c.add(Restrictions.eq("keywrdLib.id", keywrdLibId));
			}

			keywordLibraryList = c.list();

			if (keywordLibraryList .size()>0) {
				for(KeywordLibrary keywordLibrary:keywordLibraryList){
					Hibernate.initialize(keywordLibrary.getTestToolMaster());
					Hibernate.initialize(keywordLibrary.getKeywords());
					Hibernate.initialize(keywordLibrary.getLanguage());
					Hibernate.initialize(keywordLibrary.getKeywords());
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return keywordLibraryList;
	}
	@Override
	@Transactional
	public Integer saveBDDKeyWordsPhrase(BDDKeywordsPhrases bddKeyWordsPhrasesFromUI) {	
		try{
			sessionFactory.getCurrentSession().save(bddKeyWordsPhrasesFromUI);
		}catch(Exception ex){

			log.error(ex);
		}

		return 	bddKeyWordsPhrasesFromUI.getId();
	}

	@Override
	@Transactional
	public List<TestCaseScript> getTestCaseScripts(Integer productId, Integer jtStartIndex,Integer jtPageSize) {
		List<TestCaseScript> testCaseScriptList = null;

		log.info("Get the Test Case Script details in DAO Impl");	
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class,"tcs");
			c.add(Restrictions.eq("tcs.product.productId", productId));
			c.setFirstResult(jtStartIndex);
			c.setMaxResults(jtPageSize);
			testCaseScriptList = c.list();
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the Test Case Script details in DAO Impl");
		}		
		return testCaseScriptList;
	}

	public void addTestcaseScript(TestCaseScript testCaseScript) {
		try {
			sessionFactory.getCurrentSession().save(testCaseScript);
			log.debug("add Testcase Script successful");
		}catch(RuntimeException re) {
			log.debug("Error in adding Testcase Script", re);
		}

	}

	@Override
	@Transactional
	public void updateTestcaseScript(TestCaseScript testCaseScript) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseScript);
			log.debug("update Testcase Script successful");
		} catch (RuntimeException re) {
			log.debug("Error in updating Testcase Script", re);
		}
	}

	@Override
	@Transactional
	public void deleteTestcaseScript(TestCaseScript testCaseScript) {
		int scriptId = 0;
		try {
			scriptId = testCaseScript.getScriptId();
			String query = "Delete from TestCaseScript where scriptId=:scriptId";
			int result = sessionFactory.getCurrentSession().createQuery(query).setInteger("scriptId", scriptId).executeUpdate();
			if(result>0){
				String sql = "Delete from EntityRelationship er where er.entityTypeId1=78 and er.entityTypeId2=3 and er.entityInstanceId1=:scriptId";
				sessionFactory.getCurrentSession().createQuery(sql).setInteger("scriptId", scriptId).executeUpdate();				
			}
			log.debug("delete Testcase Script successful");
		} catch (RuntimeException re) {
			log.debug("Error in deleting Testcase Script", re);
		}
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCasesByProductId(int productId, int scriptId, int jtStartIndex, int jtPageSize) {
		List<Object[]> unMappedTestCaseListObj = null;
		try {
			String sql="select distinct testCaseId, testCaseName from test_case_list tcl where tcl.productId=:productId AND tcl.testCaseId NOT IN(SELECT testScriptAssn.testCaseId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.scriptId=:scriptId)";
			unMappedTestCaseListObj = sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productId", productId).setParameter("scriptId", scriptId).list();
		} catch (RuntimeException re) {
			log.error("ERROR getting TestCases Unmapped to TestCaseScript", re);	
		}
		return unMappedTestCaseListObj;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCasesByScriptId(int scriptId) {
		List<Object[]> mappedTestCasesListObj = null;
		try {

			String sql="select distinct testCaseId, testCaseName from test_case_list tcl where tcl.testCaseId in(SELECT testScriptAssn.testCaseId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.scriptId=:scriptId)";
			mappedTestCasesListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("scriptId", scriptId).
					list();
		} catch (RuntimeException re) {
			log.error("ERROR getting Mapped TestCases for TestCaseScript", re);	
		}
		return mappedTestCasesListObj;
	}

	@Override
	@Transactional
	public int getUnMappedTestCasesCountByProductId(int productId, int scriptId) {
		int totUnMappedTestCasesCount = 0;
		try {
			totUnMappedTestCasesCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from test_case_list tcl where tcl.productId=:productId AND tcl.testCaseId NOT IN(SELECT testScriptAssn.testCaseId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.scriptId=:scriptId)").
					setParameter("scriptId", scriptId).
					setParameter("productId", productId).uniqueResult()).intValue();
		} catch (RuntimeException e) {			
			log.error(e);
		}		
		return totUnMappedTestCasesCount;
	}

	@Override
	@Transactional
	public TestCaseScript getTestCaseScriptById(int testCaseScriptId) {
		TestCaseScript testCaseScript = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class);
			List list = criteria.add(Restrictions.eq("scriptId", testCaseScriptId)).list();
			testCaseScript = (list.size() != 0 && list != null)?(TestCaseScript)list.get(0):null;
		} catch (RuntimeException re) {
			log.error("Error in getTestCaseScriptById", re);
		}
		return testCaseScript;
	}

	@Override
	@Transactional
	public TestCaseScript getTestCaseScriptByName(String testCaseScriptName) {
		TestCaseScript testCaseScript = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class);
			List list = criteria.add(Restrictions.like("scriptName", testCaseScriptName)).list();
			testCaseScript = (list.size() != 0 && list != null)?(TestCaseScript)list.get(0):null;
		} catch (RuntimeException re) {
			log.error("Error in getTestCaseScriptById", re);
		}
		return testCaseScript;
	}

	@Override
	@Transactional
	public void addTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		log.info("adding TestCaseScriptAssociation");
		try {	
			sessionFactory.getCurrentSession().save(testCaseScriptHasTestCase);
			log.info("add TestCaseScriptAssociation successful");
		} catch (RuntimeException re) {
			log.error("Error in addTestCaseScriptAssociation", re);
		}
	}	

	@Override
	public void deleteTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		try {
			String query = "Delete from TestCaseScriptHasTestCase where scriptId=:scriptId and testCaseId=:testCaseId";
			sessionFactory.getCurrentSession().createQuery(query).setInteger("scriptId", testCaseScriptHasTestCase.getScriptId()).
			setInteger("testCaseId", testCaseScriptHasTestCase.getTestCaseId()).executeUpdate();
			log.debug("deleteTestCaseScriptAssociation successful");
		} catch (RuntimeException re) {
			log.error("Error in deleteTestCaseScriptAssociation", re);
		}
	}

	@Override
	public TestCaseScriptHasTestCase getTestCaseScriptAssociationByIds(Integer scriptId, Integer testCaseId) {
		log.info("getTestCaseScriptAssociationByIds");
		TestCaseScriptHasTestCase testCaseScriptHasTestCase = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptHasTestCase.class,"testScriptAssoction");
			List list = criteria.add(Restrictions.eq("testScriptAssoction.scriptId", scriptId)).
					add(Restrictions.eq("testScriptAssoction.testCaseId", testCaseId)).list();
			testCaseScriptHasTestCase =  (list.size() != 0 && list != null)?(TestCaseScriptHasTestCase)list.get(0):null;
		} catch (RuntimeException re) {
			log.error("Error in getTestCaseScriptAssociationByIds", re);
		}
		return testCaseScriptHasTestCase;
	}


	@Override
	@Transactional
	public List<TestCaseScriptHasTestCase> getTestcaseAndScriptByScriptId(Integer testScriptId) {
		List<TestCaseScriptHasTestCase> testCaseScriptList = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptHasTestCase.class);
			testCaseScriptList= criteria.add(Restrictions.eq("scriptId", testScriptId)).list();
		} catch (RuntimeException re) {
			log.error("Error in getTestcaseAndScriptByScriptId", re);
		}
		return testCaseScriptList;
	}

	@Override
	@Transactional
	public List<TestCaseScript> getTestcaseScriptsList() {
		List<TestCaseScript> testCaseScriptList = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class);
			testCaseScriptList= criteria.list();
		} catch (RuntimeException re) {
			log.error("Error in getTestcaseScriptsList", re);
		}
		return testCaseScriptList;
	}

	@Override
	@Transactional
	public List<TestCaseScriptHasTestCase> getTestCaseScriptAssociationList() {
		List<TestCaseScriptHasTestCase> testCaseScriptHasTestCases = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptHasTestCase.class);
			testCaseScriptHasTestCases= criteria.list();
		} catch (RuntimeException re) {
			log.error("Error in getTestcaseScriptsList", re);
		}
		return testCaseScriptHasTestCases;
	}

	@Override
	@Transactional
	public Integer getTestCaseScriptCount(Integer productId) {
		List<TestCaseScript> testCaseScriptList = null;
		Integer testScriptCount=0;
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class,"tcs");
			c.add(Restrictions.eq("tcs.product.productId", productId));
			testCaseScriptList = c.list();
			if(testCaseScriptList != null && testCaseScriptList.size() >0) {
				testScriptCount= testCaseScriptList.size();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("Error in getting the Test Case Script details in DAO Impl");
		}		
		return testScriptCount;
	}

	@Override
	public List<TestCaseScript> getTestcaseScriptByScriptId(Integer scriptId) {
		List<TestCaseScript> testCaseScriptList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class,"tcs");
			c.add(Restrictions.eq("tcs.scriptId", scriptId));
			testCaseScriptList = c.list();
		}catch(Exception e){

		}
		return testCaseScriptList;
	}

	@Override
	@Transactional
	public List<TestCaseScript> getTestCaseScripsByTestcaseId(Integer testcaseId) {
		List<TestCaseScript> scriptList=null;
		List<Integer> scriptIdList= new ArrayList<Integer>();
		try {
			String sql="SELECT scriptId,testcaseId FROM testcase_script_has_test_case_list WHERE testcaseId="+testcaseId;
			List<Object[]> scriptIds=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(scriptIds !=null && scriptIds.size() >0) {
				for(Object[] scriptId:scriptIds) {
					scriptIdList.add((Integer)scriptId[0]);
				}
			}
			if(scriptIdList != null && scriptIdList.size() >0) {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class,"tcs");
				c.add(Restrictions.in("tcs.scriptId", scriptIdList));
				scriptList = c.list();
			}

		}catch(RuntimeException re) {
			log.error("Error in getTestCaseScripsByTestcaseId",re);
		}

		return scriptList;
	}

	@Override
	@Transactional
	public Integer getMappedTestCasesCountByProductId(int productId) {
		List<Object[]> mappedTestCasesListObj = null;
		Integer mappedTestcaseByScriptCount=0;
		List<TestCaseList> testCaseLists = null;
		try {
			Criteria subQuery = sessionFactory.getCurrentSession().createCriteria(TestCaseScriptHasTestCase.class,"tcstc");
			List<TestCaseScriptHasTestCase> tcstcList = subQuery.list();
			if(tcstcList== null || tcstcList.isEmpty())
				return mappedTestcaseByScriptCount;
			List<Integer> tcList = new ArrayList<Integer>();
			for(TestCaseScriptHasTestCase tsc : tcstcList) {
				tcList.add(tsc.getTestCaseId());
			}
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testCaseList");
			c.createAlias("testCaseList.productType", "pd");
			c.add(Restrictions.in("testCaseList.testCaseId", tcList));
			c.add(Restrictions.eq("pd.productTypeId", productId));
			
			testCaseLists= c.list();
		} catch (RuntimeException re) {
			log.error("ERROR getting Mapped TestCases for TestCaseScript", re);	
		}
		return testCaseLists.size();
	}

	@Override
	@Transactional
	public Integer getMappedTestscriptCountByTestCaseId(Integer testCaseId) {
		List<Object[]> mappedTestCasesListObj = null;
		Integer mappedTestcaseByScriptCount=0;
		try {

			String sql="SELECT COUNT(*),scriptId FROM test_case_scripts WHERE scriptId IN (SELECT scriptId FROM testcase_script_has_test_case_list WHERE  testCaseId="+testCaseId+") group by scriptId";
			mappedTestCasesListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(mappedTestCasesListObj != null && mappedTestCasesListObj.size() >0) {
				for(Object[] mappedCount:mappedTestCasesListObj) {
					mappedTestcaseByScriptCount=Integer.parseInt(mappedCount[0].toString());
				}

			}
		} catch (RuntimeException re) {
			log.error("ERROR getting Mapped TestCases for TestCaseScript count", re);	
		}
		return mappedTestcaseByScriptCount;
	}

	@Override
	@Transactional
	public TestDataItems getTestDataItemByItemName(String testDataItemName,Integer productId, Integer userId) {
		log.debug("getting TestDataItems");
		List<TestDataItems> testDataItemList = null;
		TestDataItems testDataItem = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "testDataItems");


			if(userId != null){
				c.createAlias("testDataItems.userlist", "userlist");
				c.add(Restrictions.eq("userlist.userId", userId));
			}
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "productMaster");
				c.add(Restrictions.eq("productMaster.productId", productId));
			}
			c.add(Restrictions.eq("testDataItems.dataName", testDataItemName));
			testDataItemList = c.list();
			if(testDataItemList != null && !testDataItemList.isEmpty()){
				testDataItem = testDataItemList.get(0);
			}

			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItem;
	}	
	@Override
	@Transactional
	public int addTestDataItems(TestDataItems testDataItems) {
		log.debug(" testDataItems instance");
		try {
			sessionFactory.getCurrentSession().save(testDataItems);
			log.debug(" testDataItems successful");
			return testDataItems.getTestDataItemId();
		} catch (RuntimeException re) {
			log.error(" testDataItems failed", re);
			return 0; 
		}
	}
	@Override
	@Transactional
	public int addTestDataItemValues(TestDataItemValues testDataItemVal) {
		log.debug(" testDataItemVal instance");
		try {
			sessionFactory.getCurrentSession().save(testDataItemVal);
			log.debug(" testDataItemVal successful");
			return testDataItemVal.getTestDataValueId();
		} catch (RuntimeException re) {
			log.error(" testDataItemVal failed", re);
			return 0; 
		}
	}
	@Override
	@Transactional
	public TestDataPlan getTestDataPlanByTestDataPlanName(String testPlan,Integer productId) {
		log.debug("getting TestDataItems");
		List<TestDataPlan> testDataPlanList = null;
		TestDataPlan testDataPlan = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataPlan.class, "testDataPlan");


			if(testPlan != null){
				c.add(Restrictions.eq("testDataPlan.testDataPlanName", testPlan));
			}
			if(productId != null){
				c.createAlias("testDataPlan.productMaster", "productMaster");
				c.add(Restrictions.eq("productMaster.productId", productId));
			}


			testDataPlanList = c.list();
			if(testDataPlanList != null && !testDataPlanList.isEmpty()){
				testDataPlan = testDataPlanList.get(0);
			}

			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataPlan;
	}
	@Override
	@Transactional
	public Integer addTestDataPlan(TestDataPlan testDataPlan) {
		log.info("adding TestDataPlan instance");
		try {	
			sessionFactory.getCurrentSession().save(testDataPlan);
			log.info("add TestDataPlan successful");
			return testDataPlan.getTestDataPlanId();
		} catch (RuntimeException re) {
			log.error("add TestDataPlan failed", re);
			return -1; 
			//throw re;
		}	
	}

	@Override
	@Transactional
	public TestDataItemValues getTestDataItemValuesByProductAndTestItemValName(Integer productId,Integer userId,String testDataItemValue,Integer testDataItemId,Integer testDataPlanId) {
		log.debug("getting testDataItemValues");
		List<TestDataItemValues> testDataItemValuesList = null;
		TestDataItemValues testDataItemVal = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItemValues.class, "testDataItemValues");
			c.createAlias("testDataItemValues.testDataItems", "testDataItems");

			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(userId != null){
				c.createAlias("testDataItems.userlist", "userlist");
				c.add(Restrictions.eq("userlist.userId", userId));
			}
			if(testDataPlanId != null){
				c.createAlias("testDataItemValues.testDataPlan", "testDataPlan");
				c.add(Restrictions.eq("testDataPlan.testDataPlanId", testDataPlanId));
			}
			c.add(Restrictions.eq("testDataItems.testDataItemId", testDataItemId));
			c.add(Restrictions.eq("testDataItemValues.values", testDataItemValue));


			testDataItemValuesList = c.list();
			if(testDataItemValuesList  != null && testDataItemValuesList.size()>0){
				testDataItemVal = testDataItemValuesList.get(0);
			}

			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItemVal;

	}
	@Override
	@Transactional
	public void updateTestDataItems(TestDataItems testDataItems) {
		log.debug(" updateTestDataItems instance");
		try {
			sessionFactory.getCurrentSession().merge(testDataItems);
			log.debug(" updateTestDataItems successful");
		} catch (RuntimeException re) {
			log.error(" updateTestDataItems failed", re);
		}
	}
	@Override
	@Transactional
	public UIObjectItems getUIObjectItemByElementName(String elementName,Integer productId,Integer userId) {
		log.debug("getting UIObjectItems");
		List<UIObjectItems> uiObjectsList = null;
		UIObjectItems uiObjItem = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiObj");
			if(productId != null){
				c.createAlias("uiObj.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(userId != null){
				c.createAlias("uiObj.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}
			c.add(Restrictions.eq("uiObj.elementName", elementName));


			uiObjectsList = c.list();
			if(uiObjectsList.size()>0){
				uiObjItem = uiObjectsList.get(0);
				//Hibernate.initialize(uiObjItem.getProjectCode());
				Hibernate.initialize(uiObjItem.getProductMaster());
				Hibernate.initialize(uiObjItem.getUserlist());
				Hibernate.initialize(uiObjItem.getUserlist().getUserId());
			}
			log.debug("getting UIObjectItems successful");
		} catch (RuntimeException re) {
			log.error("getting UIObjectItems failed", re);
		}
		return uiObjItem;

	}
	@Override
	@Transactional
	public void updateUIObjects(UIObjectItems uiObjects) {
		log.debug(" updateUIObjects instance");
		try {
			sessionFactory.getCurrentSession().update(uiObjects);
			log.debug(" updateUIObjects successful");
		} catch (RuntimeException re) {
			log.error(" updateUIObjects failed", re);
		}
	}
	@Override
	@Transactional
	public int addUIObjects(UIObjectItems uiObjects) {
		log.debug(" addUIObjects instance");
		try {
			sessionFactory.getCurrentSession().save(uiObjects);
			log.debug(" addUIObjects successful");
			return uiObjects.getUiObjectItemId();
		} catch (RuntimeException re) {
			log.error(" addUIObjects failed", re);
			return 0; 
		}

	}
	@Override
	public List<String> getKeywordPhraseRegularExpressions(String productType, String testEngine) {

		List<String> keywordRegularExpressions = new ArrayList<String>();
		String mode = "GENERICMODE" ;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywordPhrase");


			if(productType != null && !productType.trim().isEmpty()){
				if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_WEB)){
					c.add(Restrictions.eq("keywordPhrase.isWeb", 1));
				}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_MOBILE)){
					c.add(Restrictions.eq("keywordPhrase.isMobile", 1));
				}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_EMBEDDED)){
					c.add(Restrictions.eq("keywordPhrase.isEmbedded", 1));
				}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_DESKTOP)){
					c.add(Restrictions.eq("keywordPhrase.isDesktop", 1));
				}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_DEVICE)){
					c.add(Restrictions.eq("keywordPhrase.isDevice", 1));
				}else{
					//Since no type is specified, ignore this.
				}
			}
			if(testEngine != null && !testEngine.trim().isEmpty()) {
				if(mode.equalsIgnoreCase("GENERICMODE")) {
					if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM)){
						c.add(Restrictions.eq("keywordPhrase.isAppiumScripGeneration", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST)){
						c.add(Restrictions.eq("keywordPhrase.isSeetestScripGeneration", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_AUTOIT)){
						c.add(Restrictions.eq("keywordPhrase.isAutoIt", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM)){
						c.add(Restrictions.eq("keywordPhrase.isSeleniumScripGeneration", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_ROBOTIUM)){
						c.add(Restrictions.eq("keywordPhrase.isRobotium", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_PROTRACTOR)){
						c.add(Restrictions.eq("keywordPhrase.isProtractor", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT)){
						c.add(Restrictions.eq("keywordPhrase.isEdat", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_TESTCOMPLETE)){
						c.add(Restrictions.eq("keywordPhrase.isTestComplete", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED)){
						c.add(Restrictions.eq("keywordPhrase.isRestAssured", 1));
					}else {

					}
				} else {
					if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM)){
						c.add(Restrictions.eq("keywordPhrase.isAppium", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST)){
						c.add(Restrictions.eq("keywordPhrase.isSeeTest", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_AUTOIT)){
						c.add(Restrictions.eq("keywordPhrase.isAutoIt", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM)){
						c.add(Restrictions.eq("keywordPhrase.isSelenium", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_ROBOTIUM)){
						c.add(Restrictions.eq("keywordPhrase.isRobotium", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_PROTRACTOR)){
						c.add(Restrictions.eq("keywordPhrase.isProtractor", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT)){
						c.add(Restrictions.eq("keywordPhrase.isEdat", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED)){
						c.add(Restrictions.eq("keywordPhrase.isRestAssured", 1));
					}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_TESTCOMPLETE)){
						c.add(Restrictions.eq("keywordPhrase.isTestComplete", 1));
					}else{

					}
				}
			}
			c.add(Restrictions.eq("keywordPhrase.status", 1));
			c.add(Restrictions.isNotNull("keywordPhrase.keywordRegularExpression"));
			c.add(Restrictions.ne("keywordPhrase.keywordRegularExpression",""));
			c .setProjection(Projections.property("keywordPhrase.keywordRegularExpression").as("keywordRegularExpression"));

			keywordRegularExpressions = c.list();

		} catch (RuntimeException re) {
			log.error("Unable to get keyword regular expressions", re);
		}
		return keywordRegularExpressions;
	}
	@Override
	@Transactional
	public List<String> listUIObjectItemElementNamesByProjectId(Integer productId,Integer userId,String filter) {
		log.debug("getting UIObjectItems");
		List<String> uiObjectsList = null;

		try {
			SQLQuery sqlQuery = null;
			if(filter.equalsIgnoreCase("-1")){
				String query = " SELECT CONCAT(CAST( uiObject.uiObjectItemId AS CHAR(10000)),'~',uiObject.elementName) FROM ui_object_items uiObject  WHERE (productId = :productId AND createdBy= :userId) OR (createdBy !=:userId  AND isShare=1 AND productId = :productId) ";
				sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
				sqlQuery.setParameter("productId", productId);
				sqlQuery.setParameter("userId",userId);				
			}else{
				String query = " SELECT CONCAT(CAST( uiObject.uiObjectItemId AS CHAR(10000)),'~',uiObject.elementName) FROM ui_object_items uiObject  WHERE (productId = :productId AND createdBy= :userId AND pageName= :pageName) OR (createdBy !=:userId  AND isShare=1 AND productId = :productId) ";
				sqlQuery.setParameter("productId", productId);
				sqlQuery.setParameter("userId",userId);
				sqlQuery.setParameter("pageName", filter);
			}
			uiObjectsList = sqlQuery.list();
			log.debug("getting UIObjectItems successful");
		} catch (Exception re) {
			log.error("getting UIObjectItems failed", re);
		}
		return uiObjectsList;
	}

	@Override
	@Transactional
	public List<String> listTestDataNamesByProductId(Integer productId,	Integer userId,String filter) {
		log.debug("getting listTestDataNamesByProjectId");
		List<String> uiObjectsList = null;

		try {
			SQLQuery sqlQuery = null;
			if(filter.equalsIgnoreCase("-1")){
				String query = "SELECT CONCAT(CAST(testDataItem.testDataItemId AS CHAR(10000)),'~',testDataItem.dataName) FROM testdata_items testDataItem  WHERE (productId = :productId AND createdBy=:userId) OR (createdBy != :userId AND isShare=1 AND productId = :productId)";
				sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
				sqlQuery.setParameter("productId", productId);
				sqlQuery.setParameter("userId",userId);
				//String query = " select concat(Cast(testDataItem.testDataItemId AS CHAR(10000)),'~',testDataItem.dataName) from testdata_items testDataItem  where projectId = :projectId and createdBy != userId";
				uiObjectsList = sqlQuery.list();
			}
			else{
				String query = " select concat(Cast(testDataItem.testDataItemId AS CHAR(10000)),'~',testDataItem.dataName) from testdata_items testDataItem  where (productId = :productId and typeName = :typeName and createdBy=:userId) OR (createdBy != :userId AND isShare=1 AND productId = :productId)";
				sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
				sqlQuery.setParameter("productId", productId);
				sqlQuery.setParameter("typeName", filter);
				sqlQuery.setParameter("userId",userId);							
				uiObjectsList = sqlQuery.list();
			}
			log.debug("getting Test Data Items  successful");
		} catch (Exception re) {
			log.error("getting Test data Items  failed", re);
		}
		return uiObjectsList;
	}
	@Override
	@Transactional
	public List<AmdocsPageObjects> listAmdocsPageObjectsByProjectIdAndTestCaseId(Integer productId,Integer userId,Integer testCaseId) {
		log.debug("getting AmdocsPageObjects");
		List<AmdocsPageObjects> amdocsobjects = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AmdocsPageObjects.class, "amdobj");
			c.createAlias("amdobj.productMaster", "pc");
			c.createAlias("amdobj.testCaseList", "tc");
			if(productId != null){
				c.add(Restrictions.eq("pc.productId", productId));

			}

			if(userId != null){
				c.createAlias("amdobj.userlist", "ul");
				c.add(Restrictions.eq("ul.userId", userId));
			}
			if(testCaseId != null){
				c.add(Restrictions.eq("tc.testCaseId", testCaseId));
			}
			c.add(Restrictions.eq("tc.isActive", 1));
			c.addOrder(Order.asc("amdobj.name"));
			amdocsobjects = c.list();
			if(amdocsobjects.size()>0){
				for(AmdocsPageObjects amdobj:amdocsobjects){
					Hibernate.initialize(amdobj.getAmdocsPageMethods());
				}
			}
			log.debug("getting AmdocsPageObjects successful");
		} catch (RuntimeException re) {
			log.error("getting AmdocsPageObjects failed", re);
		}
		return amdocsobjects;
	}

	@Override
	@Transactional
	public List<AmdocsPageMethods> listAmdocsPageMethodsByProjectId(Integer productId,Integer userId) {
		log.debug("getting AmdocsPageMethods");
		List<AmdocsPageMethods> pageMethods = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AmdocsPageMethods.class, "pageMethod");
			c.createAlias("pageMethod.amdocsPageObjects", "amdobj");
			c.createAlias("amdobj.productMaster", "pc");

			if(productId != null){
				c.add(Restrictions.eq("pc.productId", productId));
			}
			c.addOrder(Order.asc("amdobj.name"));
			c.addOrder(Order.asc("pageMethod.methodName"));

			pageMethods = c.list();
			if(pageMethods.size()>0){
				for(AmdocsPageMethods amdobj:pageMethods){
					Hibernate.initialize(amdobj.getAmdocsPageObjects().getProductMaster());
				}
			}
			log.debug("getting AmdocsPageMethods successful");
		} catch (RuntimeException re) {
			log.error("getting AmdocsPageMethods failed", re);
		}
		return pageMethods;
	}
	@Override
	@Transactional
	public List<BDDKeywordsPhrases> getBDDKeywordsPhrases(String productType,String testTool,Integer status,Integer jtStartIndex,
			Integer jtPageSize,Map<String, String> searchStrings) {
		List<BDDKeywordsPhrases> bddList = null;
		String mode = "GENERICMODE" ;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(BDDKeywordsPhrases.class, "keywrhprase");

		if(productType != null && !productType.trim().isEmpty()){
			if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_WEB)){
				c.add(Restrictions.eq("keywrhprase.isWeb", 1));
			}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_MOBILE)){
				c.add(Restrictions.eq("keywrhprase.isMobile", 1));
			}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_EMBEDDED)){
				c.add(Restrictions.eq("keywrhprase.isEmbedded", 1));
			}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_DESKTOP)){
				c.add(Restrictions.eq("keywrhprase.isDesktop", 1));
			}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_DEVICE)){
				c.add(Restrictions.eq("keywrhprase.isDevice", 1));
			}
			
		}

		if(testTool != null && !testTool.trim().isEmpty()){
			if(mode.equalsIgnoreCase("GENERICMODE")){
				if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM)){
					c.add(Restrictions.eq("keywrhprase.isAppiumScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_CODEDUI)){
					c.add(Restrictions.eq("keywrhprase.isCodeduiScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST)){
					c.add(Restrictions.eq("keywrhprase.isSeetestScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_AUTOIT)){
					c.add(Restrictions.eq("keywrhprase.isAutoIt", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM)){
					c.add(Restrictions.eq("keywrhprase.isSeleniumScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_ROBOTIUM)){
					c.add(Restrictions.eq("keywrhprase.isRobotium", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_PROTRACTOR)){
					c.add(Restrictions.eq("keywrhprase.isProtractorScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT)){
					c.add(Restrictions.eq("keywrhprase.isEDATScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_TESTCOMPLETE)){
					if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_WEB)){
						c.add(Restrictions.eq("keywrhprase.isTestCompleteWebScripGeneration", 1));
					}else if(productType.equalsIgnoreCase(AOTCConstants.PRODUCT_DESKTOP)){
						c.add(Restrictions.eq("keywrhprase.isTestCompleteScripGeneration", 1));
					}
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED)){
					c.add(Restrictions.eq("keywrhprase.isRestAssuredScripGeneration", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_CUSTOM_CISCO)){
					c.add(Restrictions.eq("keywrhprase.isCustomCiscoScripGeneration", 1));
				}
			}else{
				if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM)){
					c.add(Restrictions.eq("keywrhprase.isAppium", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_CODEDUI)){
					c.add(Restrictions.eq("keywrhprase.isCodedui", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST)){
					c.add(Restrictions.eq("keywrhprase.isSeeTest", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_AUTOIT)){
					c.add(Restrictions.eq("keywrhprase.isAutoIt", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM)){
					c.add(Restrictions.eq("keywrhprase.isSelenium", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_ROBOTIUM)){
					c.add(Restrictions.eq("keywrhprase.isRobotium", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_PROTRACTOR)){
					c.add(Restrictions.eq("keywrhprase.isProtractor", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT)){
					c.add(Restrictions.eq("keywrhprase.isEDAT", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_TESTCOMPLETE)){
					c.add(Restrictions.eq("keywrhprase.isTestComplete", 1));
				}else if(testTool.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED)){
					c.add(Restrictions.eq("keywrhprase.isRestAssured", 1));
				}else{
					return bddList;
				}
			}
		}
		if(searchStrings != null && !searchStrings.isEmpty()){
			if(searchStrings.get("searchKeywordsPhrase") != null && searchStrings.get("searchKeywordsPhrase") !=""){
				c.add(Restrictions.ilike("keywrhprase.keywordPhrase", searchStrings.get("searchKeywordsPhrase"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchDescription") != null && searchStrings.get("searchDescription") !=""){
				c.add(Restrictions.ilike("keywrhprase.description", searchStrings.get("searchDescription"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchTags") != null && searchStrings.get("searchTags") !=""){
				c.add(Restrictions.ilike("keywrhprase.tags", searchStrings.get("searchTags"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchIsSelenium") != null && searchStrings.get("searchIsSelenium") !=""){
				c.add(Restrictions.eq("keywrhprase.isSeleniumScripGeneration", Integer.parseInt(searchStrings.get("searchIsSelenium"))));
			}
			if(searchStrings.get("searchIsAppium") != null && searchStrings.get("searchIsAppium") !=""){
				c.add(Restrictions.eq("keywrhprase.isAppiumScripGeneration", Integer.parseInt(searchStrings.get("searchIsAppium"))));
			}
			if(searchStrings.get("searchIsSeeTest") != null && searchStrings.get("searchIsSeeTest") !=""){
				c.add(Restrictions.eq("keywrhprase.isSeetestScripGeneration", Integer.parseInt(searchStrings.get("searchIsSeeTest"))));
			}			
			if(searchStrings.get("searchIsCodedui") != null && searchStrings.get("searchIsCodedui") !=""){
				c.add(Restrictions.eq("keywrhprase.isCodeduiScripGeneration", Integer.parseInt(searchStrings.get("searchIsCodedui"))));
			}
			if(searchStrings.get("searchIsTestComplete") != null && searchStrings.get("searchIsTestComplete") !=""){
				c.add(Restrictions.eq("keywrhprase.isTestCompleteScripGeneration", Integer.parseInt(searchStrings.get("searchIsTestComplete"))));
			}
			if(searchStrings.get("searchIsWebTestComplete") != null && searchStrings.get("searchIsWebTestComplete") !=""){
				c.add(Restrictions.eq("keywrhprase.isTestCompleteWebScripGeneration", Integer.parseInt(searchStrings.get("isTestCompleteWebScripGeneration"))));
			}
			if(searchStrings.get("searchIsRestAssured") != null && searchStrings.get("searchIsRestAssured") !=""){
				c.add(Restrictions.eq("keywrhprase.isRestAssuredScripGeneration", Integer.parseInt(searchStrings.get("searchIsRestAssured"))));
			}
			if(status != null && status != 2) {
				c.add(Restrictions.eq("keywrhprase.status", status));
			}
		}
		if(jtStartIndex != null){
			c.setFirstResult(jtStartIndex);
		}
		if(jtPageSize != null){
			c.setMaxResults(jtPageSize);
		}
		bddList = c.list();
		return bddList;
	}

	@Override
	@Transactional
	public Integer getMaxVersionIdByTestCaseId(
			Integer testCaseId) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class,"tcas").setProjection(Projections.max("versionId"));
		c.createAlias("tcas.testCase", "tc");
		c.add(Restrictions.eq("tc.testCaseId",testCaseId));
		Integer maxVersionId = (Integer) c.uniqueResult();
		return maxVersionId;
	}

	@Override
	@Transactional
	public TestCaseAutomationStory getTestAutomationScript(Integer testCaseId, String scriptType, Integer versionId, String testEngine){
		log.debug("getting TestCaseAutomationScript");
		TestCaseAutomationStory story = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "tcas");
			c.createAlias("tcas.testCase", "tc");
			c.add(Restrictions.eq("tc.testCaseId", testCaseId));
			c.add(Restrictions.eq("tcas.versionId", versionId));
			story = (TestCaseAutomationStory) c.list().get(0);	
			if(story != null){
				Hibernate.initialize(story);
				if(story.getTestCase() != null){
					Hibernate.initialize(story.getTestCase());
				}
			}

			log.debug("getting TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error("getting TestCaseAutomationScript failed", re);
		}
		return story;
	}


	@Override
	@Transactional
	public String getTestCaseStoryEditingUser(Integer testcaseId) {

				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT userName, editingStartTime FROM test_stories_being_edited WHERE testStoryId = :testcaseId");
				query.setParameter("testcaseId", testcaseId);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List result = query.list();
				if (result == null || result.isEmpty()) {
					log.info("Testcase : " + testcaseId + " is not being edited. Could not get userName");
					return null;
				}
				//There should be only one object
				Map testCaseDetails = (Map) result.get(0);
				//This will work in MySQL, where the column name is in came case
				String userName = (String) testCaseDetails.get("userName");
				Date editingStartTime = (Date) testCaseDetails.get("editingstarttime");
				
				//If the DB is postgres, then the column name is in lowercase
				if (userName == null)
					userName = (String) testCaseDetails.get("username");
				if (editingStartTime == null)
					editingStartTime = (Date) testCaseDetails.get("editingStartTime");
				
				return ("The test story is being edited by " + userName+ " from " + (editingStartTime).toString());
	}
	@Override
	@Transactional
	public void updateTestStoryEditingStatus(Integer testcaseId, String userName, Date editingStartTime, String editingStatus,Integer userId) {

		if (editingStatus.equals(AOTCConstants.ATSG_STARTED_EDITING)) {
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO test_stories_being_edited (testStoryId, userName, editingStartTime,userId) VALUES (:testcaseId, :userName, :editingStartTime,:userId)");
			query.setParameter("testcaseId", testcaseId);
			query.setParameter("userName", userName);
			query.setParameter("editingStartTime", editingStartTime);
			query.setParameter("userId",userId);
			query.executeUpdate();
			log.info("Testcase : " + testcaseId + " Marking as being edited");
		} else if (editingStatus.equals(AOTCConstants.ATSG_FINISHED_EDITING)) {
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM test_stories_being_edited where testStoryId=:testcaseId and userId = :userId");
			query.setParameter("testcaseId", testcaseId);
			query.setParameter("userId", userId);
			query.executeUpdate();
			log.info("Testcase : " + testcaseId + " Marking as not being edited");
		} else {
			//Fall Through condition. Remove it from the map to be on safe side
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM test_stories_being_edited where testStoryId=:testcaseId");
			query.setParameter("testcaseId", testcaseId);
			query.executeUpdate();
			log.info("Testcase : " + testcaseId + " Marking as not being edited by default");
		}
	}
	@Override
	@Transactional
	public List<Attachment> listDeviceObjectsNamesByProductId(Integer productId) {
		log.debug("getting listTestDataNamesByProjectId");
		List<Attachment> deviceObjectsList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class,"attachment");
			c.createAlias("attachment.product", "pm");
			c.add(Restrictions.eq("pm.productId",productId));
			c.add(Restrictions.eq("attachment.attachmentType","EDAT"));
			c.add(Restrictions.eq("attachment.attributeFileExtension",".xml"));
			deviceObjectsList = c.list();

			if (deviceObjectsList != null && !deviceObjectsList.isEmpty()) {
				for (Attachment attach : deviceObjectsList) {
					Hibernate.initialize((Object)attach.getEntityMaster());
					Hibernate.initialize((Object)attach.getCreatedBy());
					Hibernate.initialize((Object)attach.getModifiedBy());
				}
			}

			log.debug("getting Device Obejct Items  successful");
		} catch (Exception re) {
			log.error("getting Device Obejct data Items  failed", re);
		}
		return deviceObjectsList;
	}
	@Override
	@Transactional
	public List<TestDataItems> listTestDataItemsByProductId(Integer productId,Integer testDataFilterId,Integer userId,Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getting testDataItems");
		List<TestDataItems> testDataItemsList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "testDataItems");
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pc");
				c.add(Restrictions.eq("pc.productId", productId));
			}
			//Commenting the User restriction block especially for PGi Bug : 5065
			/*if(userId != null && testDataFilterId.equals(0)){
				c.createAlias("testDataItems.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}*/
			if(testDataFilterId.equals(1)){
				c.add(Restrictions.eq("testDataItems.isShare", 1));
			}
			c.addOrder(Order.asc("testDataItems.testDataItemId"));

			if(jtStartIndex != null){
				c.setFirstResult(jtStartIndex);

			}
			if(jtPageSize != null){
				c.setMaxResults(jtPageSize);

			}
			testDataItemsList = c.list();
			if(testDataItemsList.size()>0){
				for(TestDataItems testDataItems:testDataItemsList){
					Hibernate.initialize(testDataItems.getProductMaster());
					Hibernate.initialize(testDataItems.getUserlist());
				}
			}
			log.debug("getting testDataItemsList successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemsList failed", re);
		}
		return testDataItemsList;
	}
	@Override
	@Transactional
	public int totalTestDataItemsByProductId(Integer productId,Integer testDataFilterId, Integer userId) {
		int count = 0 ;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "testDataItems");
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pc");
				c.add(Restrictions.eq("pc.productId", productId));
			}
			if(userId != null && testDataFilterId.equals(0) ){
				c.createAlias("testDataItems.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}
			if(testDataFilterId.equals(1)){
				c.add(Restrictions.eq("testDataItems.isShare", testDataFilterId));
			}
			c.setProjection(Projections.countDistinct("testDataItems.testDataItemId"));
			String res = ""+	c.uniqueResult();
			count = Integer.parseInt(res);
			return count;
		}catch(Exception e){
			log.error("getting TestDataItems count failed", e);
			return 0;
		}
	}
	@Override
	@Transactional
	public List<TestDataPlan> listTestDataPlan(Integer productId) {
		log.debug("getting TestDataPlan");
		List<TestDataPlan> testDataPlanList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataPlan.class, "tdPlan");
			if(productId != null){
				c.createAlias("tdPlan.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}

			testDataPlanList = c.list();
			if(testDataPlanList != null && !testDataPlanList.isEmpty()){
				for(TestDataPlan testDataPlan : testDataPlanList){
					Hibernate.initialize(testDataPlan);
					if(testDataPlan.getProductMaster() != null){
						Hibernate.initialize(testDataPlan.getProductMaster());
					}
					if(testDataPlan.getUserlist() != null){
						Hibernate.initialize(testDataPlan.getUserlist());
					}
				}

			}
			log.debug("getting TestDataPlan successful");
		} catch (RuntimeException re) {
			log.error("getting TestDataPlan failed", re);
		}
		return testDataPlanList;
	}
	@Override
	@Transactional
	public List<TestDataItemValues> listTestDataItemValuesByTestDataItemId(Integer testDataItemId,Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getting testDataItemValues");
		List<TestDataItemValues> testDataItemValuesList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItemValues.class, "testDataItemValues");
			if(testDataItemId != null){
				c.createAlias("testDataItemValues.testDataItems", "testDataItems");
				c.add(Restrictions.eq("testDataItems.testDataItemId", testDataItemId));
			}

			c.addOrder(Order.asc("testDataItemValues.testDataValueId"));

			if(jtStartIndex != null){
				c.setFirstResult(jtStartIndex);

			}
			if(jtPageSize != null){
				c.setMaxResults(jtPageSize);

			}
			testDataItemValuesList = c.list();
			if(testDataItemValuesList  != null && testDataItemValuesList.size()>0){
				for(TestDataItemValues testDataItemVal: testDataItemValuesList){
					Hibernate.initialize(testDataItemVal.getTestDataItems());
					if(testDataItemVal.getTestDataPlan() != null){
						Hibernate.initialize(testDataItemVal.getTestDataPlan());
					}
				}
			}
			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItemValuesList;
	}
	@Override
	@Transactional
	public int totalTestDataItemValuesByTestDataItemId(Integer testDataItemId) {
		int count = 0 ;
		String res = "";
		try {
			String query = "SELECT count(*) FROM testdata_item_value WHERE testDataItemId = :testDataItemId ";
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("testDataItemId", testDataItemId);
			res =  res + sqlQuery.uniqueResult();
			if(res != null && res != "" && !res.equalsIgnoreCase("null")){
				count = Integer.valueOf(res);
			}
			return count;
		}catch(Exception e){
			log.error("getting UIObjectItems count failed", e);
			return 0;
		}
	}
	@Override
	@Transactional
	public void updateTestDataItemValues(TestDataItemValues testDataItemVal) {
		log.debug(" updateTestDataItemValues instance");
		try {
			sessionFactory.getCurrentSession().update(testDataItemVal);
			log.debug(" updateTestDataItemValues successful");
		} catch (RuntimeException re) {
			log.error(" updateTestDataItemValues failed", re);
		}
	}

	@Override
	@Transactional
	public List<UIObjectItems> listUIObjectItemsByProductId(Integer productId,Integer objRepoFilterId,Integer userId,Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getting UIObjectItems");
		List<UIObjectItems> uiObjectsList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiObj");
			if(productId != null){
				c.createAlias("uiObj.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			//Commenting the User restriction block especially for PGi Bug : 5065
			/*if(objRepoFilterId.equals(0) && userId != null){
				c.createAlias("uiObj.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}*/
			if(objRepoFilterId.equals(1)){
				c.add(Restrictions.eq("uiObj.isShare",1 ));
			}

			c.addOrder(Order.asc("uiObj.uiObjectItemId"));

			if(jtStartIndex != null){
				c.setFirstResult(jtStartIndex);

			}
			if(jtPageSize != null){
				c.setMaxResults(jtPageSize);

			}
			uiObjectsList = c.list();
			if(uiObjectsList.size()>0){
				for(UIObjectItems uiObjcts:uiObjectsList){
					Hibernate.initialize(uiObjcts.getProductMaster());
					Hibernate.initialize(uiObjcts.getUserlist());
				}
			}
			log.debug("getting UIObjectItems successful");
		} catch (RuntimeException re) {
			log.error("getting UIObjectItems failed", re);
		}
		return uiObjectsList;
	}
	@Override
	@Transactional
	public int totalUIObjectItemsByProductId(Integer productId,Integer objRepoFilterId, Integer userId) {
		int count = 0 ;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiObj");
			if(productId != null){
				c.createAlias("uiObj.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(objRepoFilterId.equals(0) && userId != null){
				c.createAlias("uiObj.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}
			if(objRepoFilterId.equals(1)){
				c.add(Restrictions.eq("uiObj.isShare", 1));
			}
			c.setProjection(Projections.countDistinct("uiObj.uiObjectItemId"));
			String res = ""+	c.uniqueResult();
			count = Integer.parseInt(res);
			return count;
		}catch(Exception e){
			log.error("getting UIObjectItems count failed", e);
			return 0;
		}
	}


	@Override
	@Transactional
	public TestCaseAutomationStory getByTestCaseAutomationScriptId(Integer testCaseId,Integer versionId) {
		log.debug("getting TestCaseAutomationScript instance by id");
		TestCaseAutomationStory testCaseAutomationScript=null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from TestCaseAutomationStory script where testCaseId=:testCaseId and versionId=:versionId").setParameter("testCaseId", testCaseId).setParameter("versionId", versionId)
					.list();

			testCaseAutomationScript=(list!=null && list.size()!=0)?(TestCaseAutomationStory)list.get(0):null;
			if(testCaseAutomationScript != null){
				Hibernate.initialize(testCaseAutomationScript);
				if(testCaseAutomationScript.getTestCase() != null){
					Hibernate.initialize(testCaseAutomationScript.getTestCase());
				}
			}
			log.debug("get TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error(" get TestCaseAutomationScript failed", re);
			//throw re;
		}
		return testCaseAutomationScript;        
	}
	@Override
	@Transactional
	public void addScriptGenerationDetails(ScriptGenerationDetails scriptDetails) {
		try {
			this.sessionFactory.getCurrentSession().save((Object)scriptDetails);
		}
		catch (Exception e) {
			log.error((Object)("Error == " + e));
		}
	}
	@Override
	@Transactional
	public List<String> listUIObjectItemHandleNamesByProductId(Integer productId, Integer userId) {
		log.debug("listUIObjectItemHandleNamesByProjectId");
		List<String> handleList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiObj");
			if(productId != null){
				c.createAlias("uiObj.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(userId != null){
				c.createAlias("uiObj.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}
			c.addOrder(Order.asc("uiObj.uiObjectItemId"));
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("uiObj.handle"));
			c.setProjection(Projections.distinct(projList));

			handleList = c.list();

			log.debug(" listUIObjectItemHandleNamesByProjectId successful");
		} catch (RuntimeException re) {
			log.error(" listUIObjectItemHandleNamesByProjectId failed", re);
		}
		return handleList;

	}
	
	@Override
	@Transactional
	public List<UIObjectItems> listUIObjectItemsByHandleName(Integer productId,Integer userId, String handle) {
		log.debug(" listUIObjectItemsByHandleName");
		List<UIObjectItems> uiObjectsList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiObj");
			if(productId != null){
				c.createAlias("uiObj.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(userId != null){
				c.createAlias("uiObj.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}
			if(handle != null){
				c.add(Restrictions.eq("uiObj.handle", handle));
			}

			uiObjectsList = c.list();
			if(uiObjectsList.size()>0){
				for(UIObjectItems uiObjcts:uiObjectsList){
					Hibernate.initialize(uiObjcts.getProductMaster());
					Hibernate.initialize(uiObjcts.getUserlist());
				}
			}
			log.debug("getting UIObjectItems successful");
		} catch (RuntimeException re) {
			log.error("getting UIObjectItems failed", re);
		}
		return uiObjectsList;

	}
	@Override
	@Transactional
	public List<String> listTesDataItemHandleNamesByProductId(Integer productId, Integer userId) {
		log.debug("listTesDataItemHandleNamesByProjectId");
		List<String> handleList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "testDataItems");
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(userId != null){
				c.createAlias("testDataItems.userlist", "user");
				c.add(Restrictions.eq("user.userId", userId));
			}

			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("testDataItems.groupName"));
			c.setProjection(Projections.distinct(projList));

			handleList = c.list();

			log.debug(" listTesDataItemHandleNamesByProjectId successful");
		} catch (RuntimeException re) {
			log.error(" listTesDataItemHandleNamesByProjectId failed", re);
		}
		return handleList;

	}
	@Override
	@Transactional
	public Integer getTestDataItemValuesCountByProductIdAndUserId(Integer productId,Integer userId) {
		log.debug("getting testDataItemValues");
		int testDataItemValuesCount = 0;
		String res = "";
		try {
			//String query = "SELECT MAX(q.totalValuesCount) FROM (SELECT COUNT(*) AS totalValuesCount FROM testdata_item_value AS itemVal INNER JOIN testdata_items AS item ON item.testDataItemId = itemVal.testDataItemId WHERE  item.productId = :productId AND item.createdBy = :userId GROUP BY itemVal.testDataItemId,itemVal.testDataPlanId ) as q" ;
			String query = "SELECT MAX(q.totalValuesCount) FROM (SELECT COUNT(*) AS totalValuesCount FROM testdata_item_value AS itemVal INNER JOIN testdata_items AS item ON item.testDataItemId = itemVal.testDataItemId WHERE  item.productId = :productId AND item.createdBy = :userId GROUP BY itemVal.testDataItemId) as q" ;
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("productId", productId);
			sqlQuery.setParameter("userId", userId);
			res =  res + sqlQuery.uniqueResult();
			if(res != null && res != "" && !res.equalsIgnoreCase("null")){
				testDataItemValuesCount = Integer.valueOf(res);
			}
			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItemValuesCount;
	}
	/*@Override
	@Transactional
	public List<TestDataItemValues> listTestDataItemValuesByProductAndTestDataPlanAndHandle(Integer productId,String handle,Integer testDataPlanId,Integer userId) {
		log.debug("getting testDataItemValues");
		List<TestDataItemValues> testDataItemValuesList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItemValues.class, "testDataItemValues");
			c.createAlias("testDataItemValues.testDataItems", "testDataItems");

			if(userId != null){
				c.createAlias("testDataItems.userlist", "userlist");
				c.add(Restrictions.eq("userlist.userId", userId));
			}
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(handle != null && handle != ""){
				c.add(Restrictions.eq("testDataItems.groupName", handle.trim()));
			}else{
				c.addOrder(Order.asc("testDataItems.groupName"));
			}

			if(testDataPlanId != -1){
				c.createAlias("testDataItemValues.testDataPlan", "testDataPlan");
				c.add(Restrictions.eq("testDataPlan.testDataPlanId", testDataPlanId));
			}else{
				c.addOrder(Order.asc("testDataPlan.testDataPlanId"));
			}

			c.addOrder(Order.asc("testDataItemValues.testDataValueId"));
			testDataItemValuesList = c.list();
			if(testDataItemValuesList  != null && testDataItemValuesList.size()>0){
				for(TestDataItemValues testDataItemVal: testDataItemValuesList){
					Hibernate.initialize(testDataItemVal.getTestDataItems());
					if(testDataItemVal.getTestDataPlan() != null){
						Hibernate.initialize(testDataItemVal.getTestDataPlan());
					}
				}
			}
			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItemValuesList;


	}
*/	
	@Override
	@Transactional
	public List<TestDataItems> listTestDataItemValuesByProductAndTestDataPlanAndHandle(Integer productId,String handle,Integer testDataPlanId,Integer userId) {
		log.debug("getting testDataItemValues");
		List<TestDataItems> testDataItemsList = null;
		List <TestDataItemValues>testDataItemsValueList = null;
		Set <TestDataItemValues>testDataItemsValueSet = null;
		List<Integer> testDatItemIdList = new ArrayList<Integer>();
		try {
			
			Criteria tdItemvalueCriteria = sessionFactory.getCurrentSession().createCriteria(TestDataItemValues.class, "testDataItemValues");
			
			if(testDataPlanId != -1){
				tdItemvalueCriteria.createAlias("testDataItemValues.testDataPlan", "testDataPlan");
				tdItemvalueCriteria.add(Restrictions.eq("testDataPlan.testDataPlanId", testDataPlanId));
			}
			tdItemvalueCriteria.addOrder(Order.asc("testDataItemValues.testDataValueId"));
			testDataItemsValueList = tdItemvalueCriteria.list();
			
			for(TestDataItemValues tdiv :  testDataItemsValueList){
				testDatItemIdList.add(tdiv.getTestDataItems().getTestDataItemId());
			}
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "testDataItems");
			
			//c.createAlias("testDataItems.testDataItemsValSet", "testDataItemvalues");

			if(userId != null){
				c.createAlias("testDataItems.userlist", "userlist");
				c.add(Restrictions.eq("userlist.userId", userId));
			}
			if(productId != null){
				c.createAlias("testDataItems.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if(handle != null && handle != ""){
				c.add(Restrictions.eq("testDataItems.groupName", handle.trim()));
			}else{
				c.addOrder(Order.asc("testDataItems.groupName"));
			}
			
			if(testDataPlanId != -1 && testDataItemsValueList != null && testDataItemsValueList.size() > 0){
				c.add(Restrictions.in("testDataItems.testDataItemId", testDatItemIdList));
			}

			/*if(testDataPlanId != -1){
				c.createAlias("testDataItemValues.testDataPlan", "testDataPlan");
				c.add(Restrictions.eq("testDataPlan.testDataPlanId", testDataPlanId));
			}else{
				c.addOrder(Order.asc("testDataPlan.testDataPlanId"));
			}*/

			c.addOrder(Order.asc("testDataItems.testDataItemId"));
			testDataItemsList = c.list();
			if(testDataItemsList  != null && testDataItemsList.size()>0){
				for(TestDataItems testDataItem: testDataItemsList){
					Hibernate.initialize(testDataItem.getTestDataItemsValSet());
					testDataItemsValueSet = testDataItem.getTestDataItemsValSet();
					List testDataList = new ArrayList<TestDataItemValues>(testDataItemsValueSet);
					java.util.Collections.sort(testDataList, new Comparator<TestDataItemValues>(){
						@Override
						public int compare(TestDataItemValues tcl1, TestDataItemValues tcl2) {
							return tcl1.getTestDataValueId().compareTo(tcl2.getTestDataValueId());
						}
					});
					List<TestDataItemValues> testItemValList = new ArrayList<TestDataItemValues>(testDataList);
					for(TestDataItemValues dataItemValues : testItemValList){
						if(dataItemValues.getTestDataPlan() != null){
							Hibernate.initialize(dataItemValues.getTestDataPlan());
						}
					}					
					testDataItem.setTestDataItemsValSet(null);
					Set<TestDataItemValues> sortedSet = new LinkedHashSet<>(testItemValList);
					testDataItem.setTestDataItemsValSet(sortedSet);					
				}
			}
			log.debug("getting testDataItemValues successful");
		} catch (RuntimeException re) {
			log.error("getting testDataItemValues failed", re);
		}
		return testDataItemsList;


	}

	@Override
	@Transactional
	public TestCaseAutomationStory getByTestCaseStoryByAutomationStoryId(Integer generatedScrpitId) {
		log.debug("getting TestCaseAutomationScript instance by id");
		TestCaseAutomationStory testCaseAutomationStory=null;
		try {			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "testCaseAutomationStory");
			c.add(Restrictions.eq("testCaseAutomationStory.testCaseAutomationStoryId", generatedScrpitId));

			testCaseAutomationStory = (TestCaseAutomationStory)c.uniqueResult();
			
			log.debug("get TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error(" get TestCaseAutomationScript failed", re);
			//throw re;
		}
		return testCaseAutomationStory;        
	}
	@Override
	@Transactional
	public List<TestCaseAutomationStory> listTestAutomationStories(Integer productId) {
		List<TestCaseAutomationStory> stories = new ArrayList<TestCaseAutomationStory>();
		try { 
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "tcas");
			criteria.add(Restrictions.eq("tcas.productId", productId));
			stories =  criteria.list();
			if(stories != null && stories.size() > 0){
				for(TestCaseAutomationStory story : stories){
					if(story.getTestCase() != null ){
						Hibernate.initialize(story.getTestCase());
					}
				}
			}

		} catch(Exception e) {
			log.error("Failed to retrieve Test Case Automation Stories to the Product Id : "+productId);
		}

		return stories;
	}
	@Override
	@Transactional
	public void updateTestDataPlan(TestDataPlan testDataPlan) {
		log.debug("updating updateTestDataPlan instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testDataPlan);
			log.debug("update updateTestDataPlan successful");
		} catch (RuntimeException re) {
			log.error("update updateTestDataPlan failed", re);
		}
	}

	@Override
	@Transactional
	public TestDataItems getTestDataItemById(Integer testDataItemId) {
		List<TestDataItems> testDataItemList = new ArrayList<TestDataItems>();
		TestDataItems testDataItem = null;
		Set <TestDataItemValues>testDataItemsValueSet = null;
		try{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestDataItems.class, "td");
		criteria.add(Restrictions.eq("td.testDataItemId", testDataItemId));
		testDataItemList = criteria.list();
		if(testDataItemList != null && testDataItemList.size() >0){
			testDataItem = testDataItemList.get(0);
			Hibernate.initialize(testDataItem.getTestDataItemsValSet());
			testDataItemsValueSet = testDataItem.getTestDataItemsValSet();
			List testDataList = new ArrayList<TestDataItemValues>(testDataItemsValueSet);
			java.util.Collections.sort(testDataList, new Comparator<TestDataItemValues>(){
				@Override
				public int compare(TestDataItemValues tcl1, TestDataItemValues tcl2) {
					return tcl1.getTestDataValueId().compareTo(tcl2.getTestDataValueId());
				}
			});
			List<TestDataItemValues> testItemValList = new ArrayList<TestDataItemValues>(testDataList);
			for(TestDataItemValues dataItemValues : testItemValList){
				if(dataItemValues.getTestDataPlan() != null){
					Hibernate.initialize(dataItemValues.getTestDataPlan());
				}
			}					
			testDataItem.setTestDataItemsValSet(null);
			Set<TestDataItemValues> sortedSet = new LinkedHashSet<>(testItemValList);
			testDataItem.setTestDataItemsValSet(sortedSet);					
		}
		}catch(Exception e){
			log.error("Failed to get Testdata Item By Id : "+testDataItemId);
		}
		return testDataItem;
	}

	@Override
	@Transactional
	public String deleteTestdata(TestDataItems tesDataItem) {
		try{
			sessionFactory.getCurrentSession().delete(tesDataItem);
		}catch(Exception e){
			log.error("Error while trying to delete testdata : "+tesDataItem.getDataName());
			return "FAILURE";
		}
		
		return "SUCCESS";
	}

	@Override
	@Transactional
	public UIObjectItems getUiObjectItemById(Integer uiObjectItemId) {
		List<UIObjectItems> uiObjectItemList = new ArrayList<UIObjectItems>();
		UIObjectItems uiItem = null;
		try{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UIObjectItems.class, "uiobj");
		criteria.add(Restrictions.eq("uiobj.uiObjectItemId", uiObjectItemId));
		uiObjectItemList = criteria.list();
		if(uiObjectItemList != null && uiObjectItemList.size() >0){
			uiItem = uiObjectItemList.get(0);
		}
		}catch(Exception e){
			log.error("Failed to get UIObjectItems By Id : "+uiObjectItemId);
		}
		return uiItem;
	}

	@Override
	@Transactional
	public String deleteUiObjectRepository(UIObjectItems uiObjectItem) {
		try{
			sessionFactory.getCurrentSession().delete(uiObjectItem);
		}catch(Exception e){
			log.error("Error while trying to delete uiObjectItem : "+uiObjectItem.getElementName());
			return "FAILURE";
		}
		return "SUCCESS";
	}

	@Override
	@Transactional
	public boolean deleteKeywordLibrary(Integer keywordLibId) {
		try {
			KeywordLibrary keywordLibrary=null;
			List<KeywordLibrary> keywordLibraryList =getkeywordLibById(keywordLibId);
			if(keywordLibraryList != null && keywordLibraryList.size() >0) {
				keywordLibrary=keywordLibraryList.get(0);
			}
			if(keywordLibrary != null) {
				sessionFactory.getCurrentSession().delete(keywordLibrary);
				return true;
			} 
		}catch(RuntimeException re) {
			log.error("Error while trying to delete Keyword Library");
		}
		return false;
	}

	@Override
	@Transactional
	public KeywordLibrary getKeywordLibraryByClassNameAndBinary(String className, String binaryLoaderName) {
		KeywordLibrary keywordLibrary=null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(KeywordLibrary.class, "keywordLib");
			criteria.add(Restrictions.eq("keywordLib.className", className));
			criteria.add(Restrictions.eq("keywordLib.binary", binaryLoaderName));			
			criteria.setMaxResults(1);
			keywordLibrary = (KeywordLibrary) criteria.uniqueResult();
		} catch(Exception e) {
			log.error("Error while trying to retrieve Keyword Library");
		}
		return keywordLibrary;
	}
}