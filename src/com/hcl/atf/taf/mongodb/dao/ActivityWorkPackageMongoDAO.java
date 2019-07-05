package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.mongodb.model.AcitivityWorkPackageMongo;

public interface ActivityWorkPackageMongoDAO {
	
	void save(AcitivityWorkPackageMongo activityWorkPackageMongo);

	void deleteActivityWorkPackageFromMongoDb(Integer activityWorkPackageId);

}
