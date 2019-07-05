/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;

/**
 * @author silambarasur
 *
 */
public interface NotificationPolicyDAO {
	
	List<NotificationPolicy> getAllNotificationPolicyList();
	
	List<NotificationPolicy> getNotificationPolicyByProductId(Integer productId);
	
	NotificationPolicy getNotificationPolicyByNotificationId(Integer notificationId);
	
	void addNotificationPolicy(NotificationPolicy notification);
	
	void updateNotificationPolicy(NotificationPolicy notification);

	List<NotificationPolicy> getNotificationPolicyByNotificationPolicyType(String notificationType);
	
	List<NotificationPolicy> getNotificationByName(String notificationName,Integer productId);
	
	List<NotificationMaster> getAllNotificationList(Integer activeState);
	
	void bulkAddNotificationPolicy(List<NotificationPolicy> notificationPolicyList);
	
	void bulkAddNotification(List<NotificationMaster> notificationList);
}
