package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.CommentsTracker;
import com.hcl.atf.taf.model.json.JsonCommentsTracker;

public interface CommentsTrackerService {

	List<CommentsTracker> listCommentsTracker();

	CommentsTracker getCommentsTrackerById(int commentsTrackerId);

	void addCommentsTracker(CommentsTracker commentsTracker);

	void updateCommentsTracker(CommentsTracker commentsTracker);

	void deleteCommentsTracker(CommentsTracker commentsTracker);

	List<JsonCommentsTracker> listCommentsTrackersByActivityTaskId(
			Integer activityTaskId, int initializationLevel);

}
