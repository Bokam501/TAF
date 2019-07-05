/**
 * 
 */
package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.OnboardUserRequestAccess;

/**
 * @author silambarasur
 *
 */
public interface OnboardUserRequestAccessService {
	
	void add (OnboardUserRequestAccess onboardUserRequestAccess);
	void updateOnboardUserRequestAccess (OnboardUserRequestAccess onboardUserRequestAccess);
	void deleteOnboardUserRequestAccess (OnboardUserRequestAccess onboardUserRequestAccess);
	List<OnboardUserRequestAccess> listOnboardUserRequestAccess();
	OnboardUserRequestAccess getByOnboardUserRequestAccessUserName(String userName);
	List<OnboardUserRequestAccess> listOnboardUserRequestAccess(int status,int startIndex, int pageSize);  
	OnboardUserRequestAccess getOnboardUserRequestAccessById(Integer onboardUserId);
	void addOnboardUserRequestAccess(OnboardUserRequestAccess onboardUserRequestAccess);
	OnboardUserRequestAccess getByOnboardUserRequestAccessByProductIdandUserName(Integer productId,String userName);


}
