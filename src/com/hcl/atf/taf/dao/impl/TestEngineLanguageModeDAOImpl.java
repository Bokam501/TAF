package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestEngineLanguageModeDAO;
import com.hcl.atf.taf.model.TestEngineLanguageMode;

@Repository
public class TestEngineLanguageModeDAOImpl implements TestEngineLanguageModeDAO {
	private static final Log log = LogFactory.getLog(TestEngineLanguageModeDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public TestEngineLanguageMode getTestEngineLanguageModeId(int testEngineId, int languageId, int modeId) {
		TestEngineLanguageMode testEngineLanguageMode=null;
		List<TestEngineLanguageMode> testEngineLanguageModeList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestEngineLanguageMode.class, "telm");
			c.createAlias("telm.testTool", "engine");
			c.add(Restrictions.eq("engine.testToolId", testEngineId));
			c.createAlias("telm.language", "lang");
			c.add(Restrictions.eq("lang.id", languageId));
			c.createAlias("telm.executionMode", "mode");
			c.add(Restrictions.eq("mode.modeId", modeId));

			testEngineLanguageModeList =  c.list();
			log.debug("getting TestCaseAutomationScript successful");
		} catch (RuntimeException re) {
			log.error("getting TestCaseAutomationScript failed", re);
		}
		return testEngineLanguageModeList.get(0);
	}

	@Override
	@Transactional
	public TestEngineLanguageMode delete(TestEngineLanguageMode testEngineLanguageMode) {
		sessionFactory.getCurrentSession().delete(testEngineLanguageMode);
		return testEngineLanguageMode;
		
	}
}