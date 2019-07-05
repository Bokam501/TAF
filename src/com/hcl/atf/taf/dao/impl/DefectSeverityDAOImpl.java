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

import com.hcl.atf.taf.dao.DefectSeverityDAO;
import com.hcl.atf.taf.model.DefectSeverity;

@Repository
public class DefectSeverityDAOImpl implements DefectSeverityDAO {
	private static final Log log = LogFactory
			.getLog(DefectSeverityDAOImpl.class);
	
	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<DefectSeverity> list() {
		List<DefectSeverity> defectSeverityList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectSeverity.class, "defSev");			
			defectSeverityList = c.list();			
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectSeverityList;	
	}

	@Override
	@Transactional
	public DefectSeverity getDefectSeverityByseverityId(int severityId) {
		List<DefectSeverity> defectSeverityList = null;
		DefectSeverity defectSeverity =null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectSeverity.class, "defSev");		
			c.add(Restrictions.eq("defSev.severityId", severityId));
			defectSeverityList = c.list();			
			defectSeverity =(defectSeverityList!=null && defectSeverityList.size()!=0)?(DefectSeverity) defectSeverityList.get(0):null;
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectSeverity;
		}

	}
