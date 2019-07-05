package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityGroup;


public interface ActivityGroupService {
	List<ActivityGroup> listActivityGroups(int status, Integer jtStartIndex, Integer jtPageSize);
}
