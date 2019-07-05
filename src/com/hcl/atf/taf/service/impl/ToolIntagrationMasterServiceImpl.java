/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ToolIntagrationMasterDAO;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.ToolTypeMaster;
import com.hcl.atf.taf.service.ToolIntagrationMasterService;

/**
 * @author silambarasur
 *
 */
@Service
public class ToolIntagrationMasterServiceImpl implements ToolIntagrationMasterService{

	private static final Log log = LogFactory.getLog(ToolIntagrationMasterServiceImpl.class);

	@Autowired
	private ToolIntagrationMasterDAO toolIntagrationMasterDAO;

	@Override
	@Transactional
	public void addToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster) {
		try{
			toolIntagrationMasterDAO.addToolIntagrationMaster(toolIntagrationMaster);
		}catch(Exception e) {
			log.error("Error Service in addToolIntagrationMaster",e);
		}
		
	}

	@Override
	@Transactional
	public void updateToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster) {
		try{
			toolIntagrationMasterDAO.updateToolIntagrationMaster(toolIntagrationMaster);
		}catch(Exception e) {
			log.error("Error Service in updateToolIntagrationMaster",e);
		}
		
	}

	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterList() {
		try{
			return toolIntagrationMasterDAO.getToolIntagrationMasterList();
		}catch(Exception e) {
			log.error("Error Service in getToolIntagrationMasterList",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByStatus(Integer status) {
		try{
			return toolIntagrationMasterDAO.getToolIntagrationMasterListByStatus(status);
		}catch(Exception e) {
			log.error("Error Service in getToolIntagrationMasterListByStatus",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ToolTypeMaster> getToolTypeMasterListByStatus(Integer status) {
		try{
			return toolIntagrationMasterDAO.getToolTypeMasterListByStatus(status);
		}catch(Exception e) {
			log.error("Error Service in getToolTypeMasterListByStatus",e);
		}
		return null;
	}

	@Override
	@Transactional
	public boolean isExistsToolIntagrationMasterByNameAndTypeId(String toolIntagrationName, Integer typeId) {
		try{
			return toolIntagrationMasterDAO.isExistsToolIntagrationMasterByNameAndTypeId(toolIntagrationName, typeId);
		}catch(Exception e) {
			log.error("Error Service in isExistsToolIntagrationMasterByNameAndTypeId",e);
		}
		return false;
	}

	@Override
	@Transactional
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByTypeId(Integer typeId) {
		try {
			return toolIntagrationMasterDAO.getToolIntagrationMasterListByTypeId(typeId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public ToolIntagrationMaster getToolIntagrationMasterListById(Integer toolIntagrationId) {
		return toolIntagrationMasterDAO.getToolIntagrationMasterListById(toolIntagrationId);
	}
	
}
