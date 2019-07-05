package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DatabaseVersionDAO;
@Repository
public class DatabaseVersionDAOImpl implements DatabaseVersionDAO{
	
	private static final Log log = LogFactory.getLog(DatabaseVersionDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<String> getDBVersionList() {
		log.debug("listing all Database Versions");
		return null;
	}

	@Override
	@Transactional
	public String getCurrentDBVersion() {
		log.debug("get current Database Version");
		String currentDBVersion = null;
		try {
			currentDBVersion=((String) sessionFactory.getCurrentSession().createSQLQuery("SELECT versionNumber FROM taf_database_version  WHERE id=(SELECT MAX(id) FROM taf_database_version WHERE isCurrent=1)").uniqueResult()).toString();
			
			log.info("Current DB version fetch successful");
		} catch (RuntimeException re) {
			log.error("Current DB version fetch failed", re);			
		}
		return currentDBVersion;
	}

}
