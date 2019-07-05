/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityTaskType;

/**
 * @author silambarasur
 *
 */
public interface ActivityTaskTypeDAO {
	List<ActivityTaskType> getActivityTaskTypesForProduct(Integer productId);
}
