package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.CommentsTracker;

public interface CommentsTrackerDAO {
	List<CommentsTracker> listCommentsTracker();
	CommentsTracker getCommentsTrackerById(int CommentsTrackerId);
	void addCommentsTracker(CommentsTracker commentsTracker);
	void updateCommentsTracker(CommentsTracker commentsTracker);
	void deleteCommentsTracker(CommentsTracker commentsTracker);
	List<CommentsTracker> listCommentsTrackersByActivityTaskId(
			Integer activityTaskId, int initializationLevel);

}
