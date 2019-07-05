package com.hcl.atf.taf.dao;

import java.util.List;

public interface DatabaseVersionDAO {
	public List<String> getDBVersionList();
	public String getCurrentDBVersion();
}
