package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.CommentsTypeMaster;

public interface CommentsTypeMasterService {
	CommentsTypeMaster getCommentsTypeMasterById(Integer commentsTypeMasterById);
	CommentsTypeMaster getCommentsTypeMasterByName(String commentsTypeMasterByName);
	List<CommentsTypeMaster> listCommentsTypeMaster();

}
