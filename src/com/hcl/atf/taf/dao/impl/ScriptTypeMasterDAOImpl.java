package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ScriptTypeMasterDAO;
import com.hcl.atf.taf.model.ScriptTypeMaster;

@Repository
public class ScriptTypeMasterDAOImpl implements ScriptTypeMasterDAO {
	private static final Log log = LogFactory.getLog(ScriptTypeMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	public List<ScriptTypeMaster> list() {
		log.debug("listing all TestToolMaster instance");
		List<ScriptTypeMaster> scriptTypeMaster=null;
		try {
			scriptTypeMaster=sessionFactory.getCurrentSession().createQuery("from ScriptTypeMaster").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return scriptTypeMaster;
		
	}
	
	@Override
	@Transactional
	public ScriptTypeMaster getScriptTypeMasterByscriptType(String scriptTypeName) {
		List<ScriptTypeMaster> scriptTypeMasterList = null;		
		ScriptTypeMaster scriptTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ScriptTypeMaster.class, "stm");		
			c.add(Restrictions.eq("stm.scriptType", scriptTypeName));
			scriptTypeMasterList = c.list();
			for (ScriptTypeMaster scriptTypeMasterobj : scriptTypeMasterList) {
				if(scriptTypeMasterobj.getTestSuiteLists() != null && !scriptTypeMasterobj.getTestSuiteLists().isEmpty()){
					Hibernate.initialize(scriptTypeMasterobj.getTestSuiteLists());	
				}
				
			}
			scriptTypeMaster = (scriptTypeMasterList!=null && scriptTypeMasterList.size()!=0)?(ScriptTypeMaster)scriptTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return scriptTypeMaster;
	}

}
