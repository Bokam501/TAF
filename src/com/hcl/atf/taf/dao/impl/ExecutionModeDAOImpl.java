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

import com.hcl.atf.taf.dao.ExecutionModeDAO;
import com.hcl.atf.taf.model.ExecutionMode;
import com.hcl.atf.taf.model.Languages;

@Repository
public class ExecutionModeDAOImpl implements ExecutionModeDAO {
	private static final Log log = LogFactory.getLog(ExecutionModeDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	

	@Override
	@Transactional
	public List<ExecutionMode> listExecutionModes(int modeId) {
		List<ExecutionMode> executionModeList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Languages.class, "execMode");
			executionModeList = c.list();			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return executionModeList;
	}


	@Override
	@Transactional
	public ExecutionMode getExecutionModeId(int modeId) {
		ExecutionMode executionMode = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Languages.class, "execMode");
			c.add(Restrictions.eq("execMode.modeId", modeId));
			List list = c.list();		
			executionMode=(list!=null && list.size()!=0)?(ExecutionMode)list.get(0):null;
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return executionMode;
	}
	
	@Override
	@Transactional
	public ExecutionMode getModeIdByName(String modeName) {
		ExecutionMode executionMode=null;
		List<ExecutionMode> executionModeList=null;
		try {
			executionModeList=sessionFactory.getCurrentSession().createQuery("from ExecutionMode where name=:modeName").setParameter("modeName", modeName).list();
			for(Object em : executionModeList){
				executionMode = (ExecutionMode)em;
				break;
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return executionMode;
	}
	
}
