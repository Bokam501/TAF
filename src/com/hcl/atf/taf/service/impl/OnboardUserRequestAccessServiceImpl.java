/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.OnboardUserRequestAccessDAO;
import com.hcl.atf.taf.model.OnboardUserRequestAccess;
import com.hcl.atf.taf.service.OnboardUserRequestAccessService;

/**
 * @author silambarasur
 *
 */
@Service
public class OnboardUserRequestAccessServiceImpl implements OnboardUserRequestAccessService {
	
	private static final Log log = LogFactory
			.getLog(OnboardUserRequestAccessServiceImpl.class);

	@Autowired
	private OnboardUserRequestAccessDAO onboardUserRequestAccessDAO;
	
	@Override
	@Transactional
	public void addOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		
		try {
			onboardUserRequestAccessDAO.addOnboardUserRequestAccess(onboardUserRequestAccess);
		}catch(Exception e) {
			log.error("Error in addOnboardUserRequestAccess",e);
		}
		
	}

	@Override
	public void updateOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {

		try {
			onboardUserRequestAccessDAO.updateOnboardUserRequestAccess(onboardUserRequestAccess);
		}catch(Exception e) {
			log.error("Error in updateOnboardUserRequestAccess",e);
		}
		
	}

	@Override
	public void deleteOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		try {
			onboardUserRequestAccessDAO.deleteOnboardUserRequestAccess(onboardUserRequestAccess);
		}catch(Exception e) {
			log.error("Error in deleteOnboardUserRequestAccess",e);
		}
		
	}

	@Override
	public List<OnboardUserRequestAccess> listOnboardUserRequestAccess() {
		try {
			return onboardUserRequestAccessDAO.listOnboardUserRequestAccess();
		}catch(Exception e) {
			log.error("Error in listOnboardUserRequestAccess",e);
		}
		return null;
	}

	@Override
	public OnboardUserRequestAccess getByOnboardUserRequestAccessUserName(
			String userName) {
		try {
			return onboardUserRequestAccessDAO.getByOnboardUserRequestAccessUserName(userName);
		}catch(Exception e) {
			log.error("Error in getByOnboardUserRequestAccessUserName",e);
		}
		return null;
	}

	@Override
	public List<OnboardUserRequestAccess> listOnboardUserRequestAccess(int status,
			int startIndex, int pageSize) {
		try {
			return onboardUserRequestAccessDAO.listOnboardUserRequestAccess(status,startIndex,pageSize);
		}catch(Exception e) {
			log.error("Error in getByOnboardUserRequestAccessUserName",e);
		}
		return null;
	}

	@Override
	public void add(OnboardUserRequestAccess onboardUserRequestAccess) {
	}

	@Override
	public OnboardUserRequestAccess getOnboardUserRequestAccessById(
			Integer onboardUserId) {
		return onboardUserRequestAccessDAO.getOnboardUserRequestAccessById(onboardUserId);
	}
	
	@Override
	public OnboardUserRequestAccess getByOnboardUserRequestAccessByProductIdandUserName(Integer productId,String userName) {
		try {
			return onboardUserRequestAccessDAO.getByOnboardUserRequestAccessByProductIdandUserName(productId,userName);
		}catch(Exception e) {
			log.error("Error in getByOnboardUserRequestAccessByProductIdandUserName",e);
		}
		return null;
	}
}
