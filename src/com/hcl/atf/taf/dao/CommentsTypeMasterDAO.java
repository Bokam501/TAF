package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.CommentsTypeMaster;

public interface CommentsTypeMasterDAO {

	List<CommentsTypeMaster> listCommentsTypeMasters();
	CommentsTypeMaster getCommentsTypeMasterById(Integer 
			commentsTypeMasterId);
	CommentsTypeMaster getCommentsTypeMasterByName(String name);

}
