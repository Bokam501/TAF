package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestCaseStoryDAO;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestStoryGeneratedScripts;

@Repository
public class TestCaseStoryDAOImpl implements TestCaseStoryDAO{
	private static final Log log = LogFactory.getLog(TestCaseListDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<TestCaseAutomationStory> getTestCaseStoryWithVersions(Integer testCaseId) {
		List<TestCaseAutomationStory> testCaseAutomationScriptList = new ArrayList<TestCaseAutomationStory>();
		List<TestCaseAutomationStory> automationScripts = new ArrayList<TestCaseAutomationStory>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseAutomationStory.class, "tcs");
			c.createAlias("tcs.testCase", "tc");
			c.add(Restrictions.eq("tc.testCaseId", testCaseId));
			c.addOrder(Order.asc("versionId"));
			testCaseAutomationScriptList = c.list();
			if (testCaseAutomationScriptList == null || testCaseAutomationScriptList.isEmpty()) {
				log.info("Test Automation Scripts Count : " + testCaseAutomationScriptList.size());
				return null;
			} else { 
				for(TestCaseAutomationStory automationScript:testCaseAutomationScriptList){
					Hibernate.initialize(automationScript);
					if(automationScript.getTestCase() != null){
						Hibernate.initialize(automationScript.getTestCase());
					}
					automationScripts.add(automationScript);
				}

			}
		}catch (RuntimeException re) {
			log.error("getting TestCaseStory failed", re);
		}

		return automationScripts;
	}

	@Transactional
	@Override
	public TestStoryGeneratedScripts saveGeneratedScripts(
			TestStoryGeneratedScripts storyGeneratedScripts) {
		try{
			if(storyGeneratedScripts != null){
				sessionFactory.getCurrentSession().saveOrUpdate(storyGeneratedScripts);

			}

		}catch(Exception e){

			e.printStackTrace();
		}
		return storyGeneratedScripts;
	}

	@Override
	@Transactional
	public TestStoryGeneratedScripts getGeneratedScripts(
			Integer testCaseStoryId, Integer languageId ,Integer testToolId) {

		List<TestStoryGeneratedScripts> testStoryGeneratedScriptsList = new ArrayList<TestStoryGeneratedScripts>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStoryGeneratedScripts.class, "tcs");
			c.createAlias("tcs.testCaseStory", "tc");
			c.add(Restrictions.eq("tc.testCaseAutomationStoryId", testCaseStoryId));

			c.createAlias("tcs.languages", "tcl");
			c.add(Restrictions.eq("tcl.id", languageId));
			c.createAlias("tcs.testTool", "ttl");
			c.add(Restrictions.eq("ttl.testToolId", testToolId));

			testStoryGeneratedScriptsList = c.list();	




		} catch (HibernateException e) {
			log.error("getting generated scripts failed", e);
			e.printStackTrace();
		}
		if(testStoryGeneratedScriptsList.size() > 0){
			return testStoryGeneratedScriptsList.get(0);
		}else{
			return null;
		}
	}

	@Override
	@Transactional
	public TestStoryGeneratedScripts getGeneratedScript(
			Integer automationScriptId , Integer languageId) {
		List<TestStoryGeneratedScripts> testStoryGeneratedScriptsList = new ArrayList<TestStoryGeneratedScripts>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStoryGeneratedScripts.class, "tsgs");
			c.createAlias("tsgs.testCaseStory", "tc");
			c.add(Restrictions.eq("tc.testCaseAutomationStoryId", automationScriptId));
			c.createAlias("tsgs.languages", "lg");
			c.add(Restrictions.eq("lg.id", languageId));
			testStoryGeneratedScriptsList = c.list();
		}catch (HibernateException e) {
			log.error("getting generated scripts failed", e);
			e.printStackTrace();
		}

		if(testStoryGeneratedScriptsList.size() > 0){
			return testStoryGeneratedScriptsList.get(0);
		}else{
			return null;
		}
	}

	@Override
	@Transactional
	public String updateGeneratedScript(
			TestStoryGeneratedScripts testStoryGeneratedScript) {

		try{
			sessionFactory.getCurrentSession().saveOrUpdate(testStoryGeneratedScript);
		}
		catch(Exception e){
			log.error("updating generated scripts failed", e);
			return "failed";
		}

		return "Success";
	}
	@Override
	@Transactional
	public TestStoryGeneratedScripts getGeneratedScripts(
			Integer testCaseStoryId) {

		List<TestStoryGeneratedScripts> testStoryGeneratedScriptsList = new ArrayList<TestStoryGeneratedScripts>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStoryGeneratedScripts.class, "tcs");
			c.createAlias("tcs.testCaseStory", "tc");
			c.add(Restrictions.eq("tc.testCaseAutomationStoryId", testCaseStoryId));
			testStoryGeneratedScriptsList = c.list();	

		} catch (HibernateException e) {
			log.error("getting generated scripts failed", e);
			e.printStackTrace();
		}
		if(testStoryGeneratedScriptsList.size() > 0){
			return testStoryGeneratedScriptsList.get(0);
		}else{
			return null;
		}
	}

	@Override
	@Transactional
	public TestStoryGeneratedScripts getGeneratedScripts(
			Integer testCaseStoryId, Integer languageId, Integer testToolId,
			String codeGenerationMode) {

		List<TestStoryGeneratedScripts> testStoryGeneratedScriptsList = new ArrayList<TestStoryGeneratedScripts>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStoryGeneratedScripts.class, "tcs");
			c.createAlias("tcs.testCaseStory", "tc");
			c.add(Restrictions.eq("tc.testCaseAutomationStoryId", testCaseStoryId));

			c.createAlias("tcs.languages", "tcl");
			c.add(Restrictions.eq("tcl.id", languageId));
			
			c.add(Restrictions.eq("tcs.codeGenerationMode", codeGenerationMode));
			testStoryGeneratedScriptsList = c.list();	

		} catch (HibernateException e) {
			log.error("getting generated scripts failed", e);
			e.printStackTrace();
		}
		if(testStoryGeneratedScriptsList.size() > 0){
			return testStoryGeneratedScriptsList.get(0);
		}else{
			return null;
		}

	}

	

}
