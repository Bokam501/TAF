package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.dao.CommentsTypeMasterDAO;
import com.hcl.atf.taf.model.CommentsTypeMaster;
import com.hcl.atf.taf.service.CommentsTypeMasterService;

@Service
public class CommentsTypeMasterServiceImpl implements CommentsTypeMasterService {

	@Autowired
	CommentsTypeMasterDAO commentsTypeMasterDAO;


	@Override
	public CommentsTypeMaster getCommentsTypeMasterById(
			Integer commentsTypeMasterById) {
		return commentsTypeMasterDAO.getCommentsTypeMasterById(commentsTypeMasterById);
	}

	@Override
	public List<CommentsTypeMaster> listCommentsTypeMaster() {
		return commentsTypeMasterDAO.listCommentsTypeMasters();
	}
	@Override
	public CommentsTypeMaster getCommentsTypeMasterByName(String name) {
		return commentsTypeMasterDAO.getCommentsTypeMasterByName(name);
	}
}
