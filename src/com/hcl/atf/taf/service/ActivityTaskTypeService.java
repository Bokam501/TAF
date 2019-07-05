/**
 * 
 */
package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityTaskType;

/**
 * @author silambarasur
 *
 */
public interface ActivityTaskTypeService {
	List<ActivityTaskType> getActivityTaskTypesForProduct(Integer productId);
}
