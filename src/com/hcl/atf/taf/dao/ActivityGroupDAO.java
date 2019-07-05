package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityGroup;



public interface ActivityGroupDAO {
	List<ActivityGroup> listActivityGroups(int status, Integer jtStartIndex, Integer jtPageSize);	
}
