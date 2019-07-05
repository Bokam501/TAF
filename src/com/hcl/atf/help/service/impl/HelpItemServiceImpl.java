package com.hcl.atf.help.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.help.dao.HelpItemDAO;
import com.hcl.atf.help.model.HelpItem;
import com.hcl.atf.help.service.HelpItemService;

@Service
public class HelpItemServiceImpl implements HelpItemService {
	private static final Log log = LogFactory.getLog(HelpItemServiceImpl.class);
	
	@Autowired
	private HelpItemDAO helpItemDAO;
	
	@Override
	@Transactional
	public void add(HelpItem helpItem) {
		try {
			helpItemDAO.add(helpItem);
		}catch(Exception e) {
			log.error("Error add Help",e);
		}
		
	}

	@Override
	@Transactional
	public List<HelpItem> list() {
		try {
			return helpItemDAO.list();
		}catch(Exception e) {
			log.error("Error fetching help list",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<HelpItem> list(Integer status) {
		try {
			return helpItemDAO.list(status);
		}catch(Exception e) {
			log.error("Error while fetching help list by status",e);
		}
		return null;
	}

	@Override
	@Transactional
	public void delete(int helpItemId) {
		try {
			helpItemDAO.delete(helpItemId);
		}catch(Exception e) {
			log.error("Error while deleting helpitem by helpId",e);
		}
		
	}

	@Override
	@Transactional
	public HelpItem update(HelpItem helpItem) {
		try {
			return helpItemDAO.update(helpItem);
		}catch(Exception e) {
			log.error("Error while updating helpitem",e);
		}
		return null;
	}

	@Override
	@Transactional
	public int getTotalCount() {
		try {
			return helpItemDAO.getTotalCount();
		}catch(Exception e) {
			log.error("Error while getTotal help item count",e);
		}
		return 0;
	}

	@Override
	@Transactional
	public HelpItem getHelpItemById(int helpItemId) {
		try {
			return helpItemDAO.getHelpItemById(helpItemId);
		}catch(Exception e) {
			log.error("Error while get helpitem by Id",e);
		}
		return null;
	}

	@Override
	@Transactional
	public HelpItem getHelpItemByName(String name) {
		try {
			return helpItemDAO.getHelpItemByName(name);
		}catch(Exception e) {
			log.error("Error while get helpitem by name",e);
		}
		return null;
	}

}