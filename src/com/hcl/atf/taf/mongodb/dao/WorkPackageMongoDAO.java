package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.WorkPackageMongo;

public interface WorkPackageMongoDAO {

	void save(WorkPackageMongo workPackageMongo);
	void save(List<WorkPackageMongo> workPackagesMongo);
}
