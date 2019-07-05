package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.mongodb.model.WorkflowEventMongo;

public interface  WorkflowEventMongoDAO {
	void save(WorkflowEventMongo workflowEventMongo);
}
