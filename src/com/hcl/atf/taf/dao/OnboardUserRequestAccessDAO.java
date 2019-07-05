/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.OnboardUserRequestAccess;

/**
 * @author silambarasur
 *
 */
public interface OnboardUserRequestAccessDAO {
	
	void addOnboardUserRequestAccess (OnboardUserRequestAccess onboardUserRequestAccess);
	void updateOnboardUserRequestAccess (OnboardUserRequestAccess onboardUserRequestAccess);
	void deleteOnboardUserRequestAccess (OnboardUserRequestAccess onboardUserRequestAccess);
	List<OnboardUserRequestAccess> listOnboardUserRequestAccess();
	OnboardUserRequestAccess getByOnboardUserRequestAccessUserName(String userName);
	List<OnboardUserRequestAccess> listOnboardUserRequestAccess(int status,int startIndex, int pageSize);  
	OnboardUserRequestAccess getOnboardUserRequestAccessById(Integer onboardUserId);
	OnboardUserRequestAccess getByOnboardUserRequestAccessByProductIdandUserName(Integer productId,String userName);

}
