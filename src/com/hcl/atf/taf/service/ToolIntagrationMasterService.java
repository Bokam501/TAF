/**
 * 
 */
package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.ToolTypeMaster;

/**
 * @author silambarasur
 *
 */
public interface ToolIntagrationMasterService {
	public void addToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster);
	public void updateToolIntagrationMaster(ToolIntagrationMaster toolIntagrationMaster);
	public List<ToolIntagrationMaster> getToolIntagrationMasterList();
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByStatus(Integer status);
	public List<ToolTypeMaster> getToolTypeMasterListByStatus(Integer status);
	public boolean isExistsToolIntagrationMasterByNameAndTypeId(String toolIntagrationName,Integer typeId);
	public List<ToolIntagrationMaster> getToolIntagrationMasterListByTypeId(Integer typeId);
	public ToolIntagrationMaster getToolIntagrationMasterListById(Integer toolIntagrationId);

}
