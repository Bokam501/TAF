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

import com.hcl.atf.taf.dao.LanguagesDAO;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.TestEngineLanguageMode;


@Repository
public class languagesDAOImpl implements LanguagesDAO {
	private static final Log log = LogFactory.getLog(languagesDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;

	
	@Override
	@Transactional
	public List<Languages> listLanguages(int status) {
		
		List<Languages> languagesList = null;
		try {
			String hql = "from Languages ln where status = :status";
			languagesList = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("status", status).list();			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return languagesList;
	}


	@Override
	@Transactional
	public Languages getLanguageForId(int languageId) {
		Languages languages = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Languages.class, "lang");
			c.add(Restrictions.eq("lang.id", languageId));
			List list = c.list();		
			languages=(list!=null && list.size()!=0)?(Languages)list.get(0):null;
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return languages;
	}


	@Override
	@Transactional
	public Languages getLanguageByName(String languageName) {
		Languages languages = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Languages.class, "lang");
			c.add(Restrictions.eq("lang.name", languageName));
			List list = c.list();		
			languages=(list!=null && list.size()!=0)?(Languages)list.get(0):null;
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return languages;
	}
	@Override
	@Transactional
	public void updateTestEngineLanguageMode(
			TestEngineLanguageMode testEngineLanguageMode) {
		try {
			sessionFactory.getCurrentSession().save(testEngineLanguageMode);
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return;
		
	}
}
