package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.SystemType;

public interface SystemTypeDAO  {
	List<SystemType> list();
	void add(SystemType systemType);
	SystemType getSystemTypeByName(String name);	
}
