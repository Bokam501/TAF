package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DefectSeverity;

public interface DefectSeverityDAO  {	 
	
	List<DefectSeverity> list();
	DefectSeverity getDefectSeverityByseverityId(int severityId);
}
