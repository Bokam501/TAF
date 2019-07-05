package com.hcl.atf.help.service;

import java.util.List;

import com.hcl.atf.help.model.HelpItem;

public interface HelpItemService {

	void add(HelpItem helpItem);
	List<HelpItem> list();
	List<HelpItem> list(Integer status);
	void delete(int helpItemId);
	HelpItem update(HelpItem helpItem);
	int getTotalCount();
	HelpItem getHelpItemById(int helpItemId);
	HelpItem getHelpItemByName(String name);
}
