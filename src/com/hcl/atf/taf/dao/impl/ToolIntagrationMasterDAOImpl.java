/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ToolIntagrationMasterDAO;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.ToolTypeMaster;

/**
 * @author silambarasur
 *
 */
@Repository
public class ToolIntagrationMasterDAOImpl implements ToolIntagrationMasterDAO{
private static final Log log = LogFactory.getLog(ToolIntagrationMasterDAOImpl.class);
	
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void addToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster){
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(toolIntagrationMaster);
		}catch(RuntimeException re) {
			log.error("Error in addToolIntagrationMaster",re);
		}
	}
	
	@Override
	@Transactional
	public void updateToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster){
		try {
			sessionFactory.getCurrentSession().update(toolIntagrationMaster);
		}catch(RuntimeException re) {
			log.error("Error in updateToolIntagrationMaster",re);
		}
	}
	
	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterList(){
		List<ToolIntagrationMaster> toolIntagrationMasterList=null;
		try {
			toolIntagrationMasterList=sessionFactory.getCurrentSession().createCriteria(ToolIntagrationMaster.class,"tIM").list();
		}catch(RuntimeException re) {
			log.error("Error in getToolIntagrationMasterList",re);
		}
		return toolIntagrationMasterList;
	}
	
	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByStatus(Integer status){
		List<ToolIntagrationMaster> toolIntagrationMasterList=null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ToolIntagrationMaster.class,"tIM");
			c.add(Restrictions.eq("tIM.status", status));
			toolIntagrationMasterList = c.list();
		}catch(RuntimeException re) {
			log.error("Error in getToolIntagrationMasterListByStatus",re);
		}
		return toolIntagrationMasterList;
	}
	
	@Override
	@Transactional
	public List<ToolTypeMaster> getToolTypeMasterListByStatus(Integer status){
		List<ToolTypeMaster> toolTypeList=null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ToolTypeMaster.class,"tTM");
			c.add(Restrictions.eq("tTM.status", status));
			toolTypeList = c.list();
		}catch(RuntimeException re) {
			log.error("Error in getToolTypeMasterListByStatus",re);
		}
		return toolTypeList;
	}
	
	@Override
	@Transactional
	public boolean isExistsToolIntagrationMasterByNameAndTypeId(String toolIntagrationName,Integer typeId){
		List<ToolIntagrationMaster> toolIntagrationMasters=null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ToolIntagrationMaster.class,"tIM");
			c.createAlias("tIM.toolType", "toolType");
			c.add(Restrictions.eq("tIM.name", toolIntagrationName));
			c.add(Restrictions.eq("toolType.id", typeId));
			toolIntagrationMasters = c.list();
			if(toolIntagrationMasters != null && toolIntagrationMasters.size() >0) {
				return true;
			} else {
				return false;
			}
		}catch(RuntimeException re) {
			log.error("Error in getToolTypeMasterListByStatus",re);
		}
		return false;
	}
	
	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByTypeId(Integer typeId){
		List<ToolIntagrationMaster> toolIntagrationMasterList=null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ToolIntagrationMaster.class,"tIM");
			c.createAlias("tIM.toolType", "toolType");
			c.add(Restrictions.eq("tIM.status", 1));
			c.add(Restrictions.eq("toolType.id", typeId));
			toolIntagrationMasterList = c.list();
		}catch(RuntimeException re) {
			log.error("Error in getToolIntagrationMasterListByStatus",re);
		}
		return toolIntagrationMasterList;
	}

	@Override
	@Transactional
	public ToolIntagrationMaster getToolIntagrationMasterListById(Integer toolIntagrationId) {
		List<ToolIntagrationMaster> toolIntagrationMasterList=null;
		try {
			Criteria c= sessionFactory.getCurrentSession().createCriteria(ToolIntagrationMaster.class,"tim");
			c.add(Restrictions.eq("tim.id", toolIntagrationId));
			toolIntagrationMasterList = c.list();
			for(ToolIntagrationMaster tim : toolIntagrationMasterList){
				Hibernate.initialize(tim.getId());
				Hibernate.initialize(tim.getName());
			}
		}catch(RuntimeException re) {
			log.error("Error in getToolIntagrationMasterListByStatus",re);
		}
		return toolIntagrationMasterList.get(0);
	}
	
}
