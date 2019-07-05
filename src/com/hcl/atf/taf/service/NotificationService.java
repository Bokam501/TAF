/**
 * 
 */
package com.hcl.atf.taf.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;
import com.hcl.atf.taf.model.UserList;

/**
 * @author silambarasur
 *
 */
public interface NotificationService {
	
	List<NotificationPolicy> getAllNotificationPolicyList();
	
	List<NotificationPolicy> getNotificationPolicyByProductId(Integer productId);
	
	NotificationPolicy getNotificationPolicyByNotificationId(Integer notificationId);
	
	void addNotificationPolicy(NotificationPolicy notification);
	
	void updateNotificationPolicy(NotificationPolicy notification);

	void processNotification(HttpServletRequest request,Integer productId,String notificationName, Object entity,String Subject);
	
	List<NotificationMaster> getAllNotificationList(Integer activeState);
	
	void prepareNotificationMaster(UserList user);
	
	void prepareNotificationPolicy(Integer productId);
	
	Map<String, Object> prepareActivityMailTemplateModel(Activity activity);
}
