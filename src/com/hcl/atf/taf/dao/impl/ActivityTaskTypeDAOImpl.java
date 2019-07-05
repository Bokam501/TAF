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

import com.hcl.atf.taf.dao.ActivityTaskTypeDAO;
import com.hcl.atf.taf.model.ActivityTaskType;

/**
 * @author silambarasur
 *
 */
@Repository
public class ActivityTaskTypeDAOImpl implements ActivityTaskTypeDAO {
	private static final Log log = LogFactory.getLog(ActivityTaskTypeDAOImpl.class);
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ActivityTaskType> getActivityTaskTypesForProduct(Integer productId){
		log.info("getActivityTaskTypesForProduct Start");
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTaskType.class, "activityTaskType");
			c.createAlias("activityTaskType.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			List<ActivityTaskType> activityTaskTypeList = c.list();
			log.info("getActivityTaskTypesForProduct End");
			return activityTaskTypeList;
		}catch(Exception e){
			log.info("getActivityTaskTypesForProduct Failed");
		}
		return null;
	}
	
}
