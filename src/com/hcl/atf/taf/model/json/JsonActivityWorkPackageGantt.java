/**
 * 
 */
package com.hcl.atf.taf.model.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @author silambarasur
 *
 */
public class JsonActivityWorkPackageGantt {
	
	private String name;
	
	
	Map<String,Object> ganttActivityDetails = new HashMap<String,Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String,Object> getGanttActivityDetails() {
		return ganttActivityDetails;
	}

	public void setGanttActivityDetails(
			Map<String,Object> ganttActivityDetails) {
		this.ganttActivityDetails = ganttActivityDetails;
	}
	
	

}
