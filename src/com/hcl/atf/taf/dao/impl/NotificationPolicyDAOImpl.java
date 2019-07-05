/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.NotificationPolicyDAO;
import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;

/**
 * @author silambarasur
 *
 */
@Repository
public class NotificationPolicyDAOImpl implements NotificationPolicyDAO{

private static final Log log = LogFactory.getLog(NotificationPolicyDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<NotificationPolicy> getAllNotificationPolicyList() {
		List<NotificationPolicy> notificationPolicyList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			notificationPolicyList=c.list();
		}catch(RuntimeException re) {
			log.error("Notification list failed",re);
		}
		return notificationPolicyList;
	}
	
	@Override
	@Transactional
	public List<NotificationPolicy> getNotificationPolicyByProductId(Integer productId) {
		List<NotificationPolicy> notificationPolicyList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			c.createAlias("notificationPolicy.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			notificationPolicyList=c.list();
		}catch(RuntimeException re) {
			log.error("Notification list failed",re);
		}
		return notificationPolicyList;
	}
	
	@Override
	@Transactional
	public NotificationPolicy getNotificationPolicyByNotificationId(Integer notificationId) {
		List<NotificationPolicy> notificationPolicyList=null;
		try {
		Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
		c.add(Restrictions.eq("notificationPolicy.notification.notificationId", notificationId));
		notificationPolicyList=c.list();
		if(notificationPolicyList !=null  && notificationPolicyList.size() >0) {
			return notificationPolicyList.get(0);
		}
		
		}catch(RuntimeException re) {
			log.error("getNotificationPolicyById failed",re);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void addNotificationPolicy(NotificationPolicy notificationPolicy) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(notificationPolicy);
		}catch(RuntimeException re) {
			log.error("error saving notification policy",re);
		}
	}
	
	@Override
	@Transactional
	public void updateNotificationPolicy(NotificationPolicy notificationPolicy) {
		try {
			sessionFactory.getCurrentSession().update(notificationPolicy);
		}catch(RuntimeException re) {
			log.error("error update notification policy",re);
		}
	}
	
	@Override
	@Transactional
	public List<NotificationPolicy> getNotificationPolicyByNotificationPolicyType(String notificationPolicyType) {
		List<NotificationPolicy> notificationList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			c.add(Restrictions.eq("notificationPolicy.notificationPolicyType", notificationPolicyType));
			notificationList=c.list();
			return notificationList;
		}catch(RuntimeException re) {
			log.error("getNotificationPolicyByNotificationType",re);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<NotificationPolicy> getNotificationByName(String notificationName,Integer productId) {
		List<NotificationPolicy> notificationList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			c.createAlias("notificationPolicy.notification", "notification");
			c.createAlias("notificationPolicy.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("notification.notificationName", notificationName));
			notificationList=c.list();
			return notificationList;
		}catch(RuntimeException re) {
			log.error("getNotificationByName",re);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<NotificationMaster> getAllNotificationList(Integer activeState) {
		List<NotificationMaster> notificationList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationMaster.class, "notificationMaster");
			c.add(Restrictions.eq("notificationMaster.activeState", activeState));
			notificationList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getAllNotificationList");
		}
		
		return notificationList;
	}
	
	@Override
	@Transactional
	public void bulkAddNotificationPolicy(List<NotificationPolicy> notificationPolicyList) {
		try {
			if(notificationPolicyList != null && notificationPolicyList.size() >0) {
				for(NotificationPolicy notificationPolicy:notificationPolicyList) {
					String notificationName=notificationPolicy.getNotification().getNotificationName();
					Integer productId=notificationPolicy.getProduct().getProductId();
					Integer entityTypeId=notificationPolicy.getNotification().getEntity().getEntitymasterid();
					if(!isCheckNotificationPolicyAlreadyMappedByNotificationNameAndProductId(notificationName,productId,entityTypeId)) {
						addNotificationPolicy(notificationPolicy);
					}
				}
			}
		}catch(RuntimeException re) {
			log.error("Error in Bulk notification Policy",re);
		}
	}
	
	
	
	@Override
	@Transactional
	public void bulkAddNotification(List<NotificationMaster> notificationList) {
		try {
			if(notificationList != null && notificationList.size() >0) {
				for(NotificationMaster notification:notificationList) {
					if(!checkNotificationByNameIsExist(notification.getNotificationName())) {
						sessionFactory.getCurrentSession().save(notification);
					}
				}
			}
		}catch(RuntimeException re) {
			log.error("Error in Bulk add notification master",re);
		}
	}
	
	
	private boolean checkNotificationByNameIsExist(String notificationName) {
		List<NotificationPolicy> notificationList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			c.createAlias("notificationPolicy.notification", "notification");
			c.add(Restrictions.eq("notification.notificationName", notificationName));
			notificationList=c.list();
			if(notificationList != null && notificationList.size() >0) {
				return true;
			}
		}catch(RuntimeException re) {
			log.error("checkNotificationByNameIsExist",re);
		}
		return false;
	}
	

	public boolean isCheckNotificationPolicyAlreadyMappedByNotificationNameAndProductId(String notificationName,Integer productId,Integer entityTypeId) {
		List<NotificationPolicy> notificationList=null;
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(NotificationPolicy.class, "notificationPolicy");
			c.createAlias("notificationPolicy.notification", "notification");
			c.createAlias("notification.entity", "entity");
			c.createAlias("notificationPolicy.product", "product");
			c.add(Restrictions.eq("notification.notificationName", notificationName));
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("entity.entitymasterid", entityTypeId));
			notificationList=c.list();
			return (notificationList !=null &&notificationList.size() >0) ?true:false;
		}catch(RuntimeException re) {
			log.error("isCheckNotificationPolicyAlreadyMappedByNotificationNameAndProductId",re);
		}
		return false;
	}
}
