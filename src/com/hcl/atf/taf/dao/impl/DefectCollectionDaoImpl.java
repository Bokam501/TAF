package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DefectCollectionDao;
import com.hcl.atf.taf.model.DefectCollection;

@Repository
public class DefectCollectionDaoImpl implements DefectCollectionDao {
	
	private static final Log log = LogFactory.getLog(DefectCollectionDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<DefectCollection> getDefectCollectionList() {
		List<DefectCollection> defectCollectionList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectCollection.class);
			defectCollectionList = c.list();
		} catch (Exception e) {
			log.error("Unable to fetch DefectCollectionList", e);
		}
		return defectCollectionList;
	}

}
