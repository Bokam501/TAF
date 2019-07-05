package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.CommentsTrackerDAO;
import com.hcl.atf.taf.model.CommentsTracker;
import com.hcl.atf.taf.model.json.JsonCommentsTracker;
import com.hcl.atf.taf.service.CommentsTrackerService;

@Service
public class CommentsTrackerServiceImpl  implements CommentsTrackerService {
	private static final Log log = LogFactory.getLog(CommentsTrackerServiceImpl.class);
	@Autowired
	CommentsTrackerDAO commentsTrackerDAO;
	
	@Override
	@Transactional
	public List<CommentsTracker> listCommentsTracker() {
		List<CommentsTracker> listCommentsTracker = null;
		List<JsonCommentsTracker> listJsonCommentsTracker = new ArrayList<JsonCommentsTracker>();
		try {
			listCommentsTracker = commentsTrackerDAO.listCommentsTracker();
			for(CommentsTracker ct: listCommentsTracker){
				listJsonCommentsTracker.add(new JsonCommentsTracker(ct));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return listCommentsTracker;
	}
	
	@Override
	public CommentsTracker getCommentsTrackerById(int commentsTrackerId) {
		return commentsTrackerDAO.getCommentsTrackerById(commentsTrackerId);
		
	}
	
	@Override
	@Transactional
	public void addCommentsTracker(CommentsTracker commentsTracker) {
		 commentsTrackerDAO.addCommentsTracker(commentsTracker);
		
	}
	
	

	@Override
	@Transactional
	public void updateCommentsTracker(CommentsTracker commentsTracker) {
		commentsTrackerDAO.updateCommentsTracker(commentsTracker);
		
	}

	@Override
	public void deleteCommentsTracker(CommentsTracker commentsTracker) {
		commentsTrackerDAO.deleteCommentsTracker(commentsTracker);
		
	}
	
	@Override
	@Transactional
	public List<JsonCommentsTracker> listCommentsTrackersByActivityTaskId(
			Integer activityTaskId, int initializationLevel) {
		List<JsonCommentsTracker> listOfJsonCommentsTrackers = new ArrayList<JsonCommentsTracker>();
		List<CommentsTracker> listOfCommentsTrackers =commentsTrackerDAO.listCommentsTrackersByActivityTaskId(activityTaskId, initializationLevel);
		if(listOfCommentsTrackers != null && listOfCommentsTrackers.size()>0){
			for (CommentsTracker commentsTracker : listOfCommentsTrackers) {
				JsonCommentsTracker jsonCommentsTracker = new JsonCommentsTracker(commentsTracker);
				listOfJsonCommentsTrackers.add(jsonCommentsTracker);
			}
		}
		return listOfJsonCommentsTrackers;
	}

	
}
