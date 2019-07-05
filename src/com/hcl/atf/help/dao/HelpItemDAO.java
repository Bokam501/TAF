package com.hcl.atf.help.dao;

import java.util.List;

import com.hcl.atf.help.model.HelpItem;

public interface HelpItemDAO {

	List<HelpItem> list();
	List<HelpItem> list(int status);
	HelpItem getHelpItemById(int helpItemId);
	HelpItem getHelpItemByName(String name);
	void add(HelpItem helpItem);
	void delete(HelpItem helpItem);
	HelpItem update(HelpItem helpItem);
	void delete(Integer helpItemId);
	int getTotalCount();
}
