package com.hcl.atf.taf.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.AttachmentType;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;

@Repository
public class CommonDAOImpl implements CommonDAO{

	private static final Log log = LogFactory.getLog(CommonDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	@Transactional
	public String duplicateName(String value, String tableName, String columnName,String entityName,String filter) {
		log.info("getting duplicateName error message");
		String message=null;
		try {			
			List list = null;
			String sql = "";
			if(filter != null && !filter.isEmpty()){
				sql = "select * from "+tableName+" x where x."+columnName+"='"+value.trim()+"' and x."+filter;
				list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}else{
				sql = "select * from "+tableName+" x where x."+columnName+"='"+value.trim()+"'";
				list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();	
			}
			
			if(list!=null && !list.isEmpty() && list.size()>0){
				message=entityName+" already Exists.";
			}
			log.debug("duplicateName successful");
		} catch (RuntimeException re) {
			log.error("duplicateName failed", re);
			// throw re;
		}
		return message;
	}

	@Override
	@Transactional
	public String getModifiedFieldOldValue(String modifiedFieldName, String entityTypeName, String tableName, String primaryKeyColumnName, String ColumnValue, String filter) {
		log.info("getting ModifiedFieldOldValue");
		String modifiedFieldOldValue ="";
		String sql ="";
		try {	
			if(filter == null){
				sql="select "+modifiedFieldName+" from "+tableName+ " tab where tab."+primaryKeyColumnName+" =:colValue";	
			}else{
				sql="select "+modifiedFieldName+" from "+tableName+ " tab where tab."+primaryKeyColumnName+" =:colValue and tab."+filter;
			}
			
			Object obj =sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("colValue", ColumnValue).
					uniqueResult();
			if(obj instanceof Integer){
				modifiedFieldOldValue= ""+((Integer)obj);
			}if(obj instanceof String){
				modifiedFieldOldValue = (String)obj;
			}if(obj instanceof Date){
				modifiedFieldOldValue = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format((Date)obj);
			}
		
			log.debug("getModifiedFieldOldValue successful");
		} catch (RuntimeException re) {
			log.error("getModifiedFieldOldValue failed", re);
		}
		return modifiedFieldOldValue;
	}
	@Override
	@Transactional
	public String duplicateNameWithOutFilter(String value, String tableName, String columnName,String entityName) {
		log.info("getting duplicateName error message");
		String message=null;
		try {
			
			List list = sessionFactory.getCurrentSession().createQuery("from "+tableName+" x where x."+columnName+"='"+value.trim()+"' ").list();
			if(list!=null && !list.isEmpty() && list.size()>0){
				message=entityName+" already Exists.";
			}
			log.debug("duplicateName successful");
		} catch (RuntimeException re) {
			log.error("duplicateName failed", re);
		}
		return message;
	}

	@Override
	@Transactional
	public List<EntityMaster> getEntityTypeList() {
		List<EntityMaster> entityMasters = new ArrayList<EntityMaster>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntityMaster.class, "entityMaster");
			entityMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error while obtaining entity type list in getEntityTypeList", ex);
		}
		return entityMasters;
	}

	@Override
	@Transactional
	public List<Object[]> getEntityForTypeList(String className, String aliasName, HashMap<String, Object> contraints, List<String> projectionKeyList) {
		List<Object[]> entityLists = new ArrayList<Object[]>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className, aliasName);
			for(Entry<String, Object>  entry : contraints.entrySet()){
				criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			}
			ProjectionList projectionLists = Projections.projectionList();
			for(String projectionKey : projectionKeyList){
				projectionLists.add(Projections.property(projectionKey));
			}
			criteria.setProjection(Projections.distinct(projectionLists));
			entityLists = criteria.list();
		}catch(Exception ex){
			log.error("Error while obtaining entity list in getEntityTypeList foe entity type - "+className, ex);
		}
		return entityLists;
	}

	@Override
	@Transactional
	public List<EntityMaster> getWorkflowCapableEntityTypeList() {
		List<EntityMaster> entityMasters = new ArrayList<EntityMaster>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntityMaster.class, "entityMaster");
			criteria.add(Restrictions.eq("entityMaster.isWorkflowCapable", 1));
			criteria.addOrder(Order.asc("entityMaster.entitymastername"));
			entityMasters = criteria.list();
		}catch(Exception ex){
			log.error("Error while obtaining entity type list in getEntityTypeList", ex);
		}
		return entityMasters;
	}

	@Override
	@Transactional
	public void addAttachment(Attachment attachment) {
		log.info("Inside addAttachment");
		try{
			sessionFactory.getCurrentSession().save(attachment);
		}catch(Exception ex){
			log.error("Error in addAttachment ", ex);
		}
	}

	@Override
	@Transactional
	public void updateAttachment(Attachment attachment) {
		log.info("Inside updateAttachment");
		try{
			sessionFactory.getCurrentSession().update(attachment);
		}catch(Exception ex){
			log.error("Error in updateAttachment ", ex);
		}
	}
	
	@Override
	@Transactional
	public List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Fetching Attachment Of EntityOrInstance with parameters entityTypeId - "+entityTypeId);
		log.debug("Inside getAttachmentOfEntityOrInstance with parameters entityTypeId - "+entityTypeId+", entityInstanceId - "+entityInstanceId+", fileName - "+fileName+", userId - "+userId+", jtStartIndex - "+jtStartIndex+", jtPageSize - "+jtPageSize);
		List<Attachment> totalAttachmentsList = new ArrayList<Attachment>();
		List<Integer> activityWPIdList = new ArrayList<Integer>();
		List<Attachment> attachOfAWPList = null;
		List<Attachment> attachOfACTList = null;
		List<Attachment> attachOfProductList = null;
		try{
			if(entityTypeId == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){	
				attachOfProductList = new ArrayList<Attachment>();
				attachOfProductList = getAttachmentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, null, fileName, userId, jtStartIndex, jtPageSize);
				totalAttachmentsList.addAll(attachOfProductList);
				
				List<Integer> activityWPIDList = new ArrayList<Integer>();
				activityWPIDList = sessionFactory.getCurrentSession().createSQLQuery("select awp.activityWorkPackageId from activity_work_package awp" 
						+ " inner join product_build pb on awp.productbuild = pb.productBuildId "
						+ " inner join product_version_list_master pvlm on pb.productVersionId = pvlm.productVersionListId "
						+ " inner join product_master pm on pvlm.productId = pm.productId ")
						.list();
				if(activityWPIDList != null && activityWPIDList.size() != 0){
					attachOfAWPList = new ArrayList<Attachment>();
					
					attachOfAWPList = getAttachmentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, activityWPIDList, fileName, userId, jtStartIndex, jtPageSize);
					totalAttachmentsList.addAll(attachOfAWPList); //Combination of Product Attachments and its child ActvitityWorkPackage Attachments.
				}				
				/*----------------------------*/
				List<Integer> activityIdList = new ArrayList<Integer>();
				activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where act.activityWorkPackageId in :awpIds")
				.setParameterList("awpIds", activityWPIDList)
				.list();
				/*----------------------------*/
				if(activityIdList != null && !activityIdList.isEmpty()){					
					attachOfACTList = new ArrayList<Attachment>();
					attachOfACTList = getAttachmentsOfEntityIDorEntityIDList(0, 0, activityIdList, fileName, userId, jtStartIndex, jtPageSize)		;
					totalAttachmentsList.addAll(attachOfACTList); //Combination of AWP Attachments and its child Acitivities Attachments.
					/*----------------------------*/	
				}
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){			
				attachOfAWPList = new ArrayList<Attachment>();
				attachOfAWPList = getAttachmentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, null, fileName, userId, jtStartIndex, jtPageSize);
				totalAttachmentsList.addAll(attachOfAWPList);
				/*----------------------------*/
				List<Integer> activityIdList = new ArrayList<Integer>();
				activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where act.activityWorkPackageId=:actWPId")
				.setParameter("actWPId", entityInstanceId)	
				.list();
				if(activityIdList != null && !activityIdList.isEmpty()){					
					attachOfACTList = new ArrayList<Attachment>();
					attachOfACTList = getAttachmentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, activityIdList, fileName, userId, jtStartIndex, jtPageSize)		;
					totalAttachmentsList.addAll(attachOfACTList); //Combination of AWP Attachments and its child Acitivities Attachments.
					/*----------------------------*/	
				}
			}else{				
				totalAttachmentsList = getAttachmentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId,
						null, fileName, userId, jtStartIndex, jtPageSize);
			}
			
		}catch(Exception ex){
			log.error("Error in getAttachmentOfEntityOrInstance ", ex);
		}
		return totalAttachmentsList;
	}
		
	@Override
	@Transactional
	public  List<Attachment> getAttachmentsOfEntityIDorEntityIDList(Integer entityTypeId,Integer entityInstanceId, List<Integer> entityIdList, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize){
		List<Attachment> attachmentsOfEntity = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityIdList != null && !entityIdList.isEmpty()){
				c.add(Restrictions.in("attachment.entityId", Arrays.asList(entityIdList.toArray())));					
			}
			else if(entityInstanceId != null && entityInstanceId != 0){
				c.add(Restrictions.eq("attachment.entityId", entityInstanceId));
			}
			else if(userId != null){
					c.add(Restrictions.eq("attachment.entityId", 0));
					c.add(Restrictions.eq("attachment.createdBy.userId", userId));
			}
			if(fileName != null && !fileName.trim().isEmpty()){
				c.add(Restrictions.eq("attachment.attributeFileName", fileName));
			}
			if(jtStartIndex != null && jtPageSize != null){
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			attachmentsOfEntity = new ArrayList<Attachment>();
			attachmentsOfEntity = c.list();
		} catch (HibernateException e) {
			log.error("Error ", e);
		}
		return attachmentsOfEntity;
	}
	
	@Override
	@Transactional
	public Attachment getAttachmentById(Integer attachmentId) {
		log.info("Inside getAttachmentById with parameters attachmentId - "+attachmentId);
		Attachment attachment = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.add(Restrictions.eq("attachment.attachmentId",attachmentId));

			List<Attachment> attachments = c.list();
			attachment = (attachments != null && attachments.size() != 0) ? (Attachment)(attachments.get(0)): null;				
			
		} catch (Exception re) {
			log.error("Error in getAttachmentById ", re);
		}
		return attachment;
	}

	@Override
	@Transactional
	public void deleteAttachment(Attachment attachment) {
		log.info("Inside deleteAttachment");
		try{
			sessionFactory.getCurrentSession().delete(attachment);
		}catch(Exception ex){
			log.error("Error in deleteAttachment ", ex);
		}
	}

	@Override
	@Transactional
	public List<AttachmentType> getAttachmentTypeForEntity(Integer entityTypeId) {
		List<AttachmentType> attachmentTypes = new ArrayList<AttachmentType>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AttachmentType.class, "attachmentType");
			if(entityTypeId != null && entityTypeId > 0){
				criteria.add(Restrictions.eq("attachmentType.entityMaster.entitymasterid", entityTypeId));
			}
			attachmentTypes = criteria.list();
		}catch(Exception ex){
			log.error("Error while obtaining attachment type list in getAttachmentTypeForEntity ", ex);
		}
		return attachmentTypes;
	}
	
	@Override
	@Transactional
	public Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId) {
		log.info("Fetching Attachment Of EntityOrInstance Pagination with parameters entityTypeId - "+entityTypeId);
		log.debug("Fetching AttachmentOfEntityOrInstancePagination with parameters entityTypeId - "+entityTypeId+", entityInstanceId - "+entityInstanceId+", fileName - "+fileName+", userId - "+userId);
		Integer attachmentPagination = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityInstanceId != null && entityInstanceId != 0){
				c.add(Restrictions.eq("attachment.entityId", entityInstanceId));
			}else{
				if(userId != null){
					c.add(Restrictions.eq("attachment.entityId", 0));
					c.add(Restrictions.eq("attachment.createdBy.userId", userId));
				}
			}
			if(fileName != null && !fileName.trim().isEmpty()){
				c.add(Restrictions.eq("attachment.attributeFileName", fileName));
			}
			c.setProjection(Projections.rowCount());
			attachmentPagination = ((Long)c.uniqueResult()).intValue(); 
			
		}catch(Exception ex){
			log.error("Error in getAttachmentOfEntityOrInstancePagination ", ex);
		}
		return attachmentPagination;
	}
		
	@Override
	@Transactional
	public  List<Comments> getCommentsOfEntityIDorEntityIDList(Integer entityTypeId,Integer entityInstanceId, List<Integer> entityIdList, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize){
		List<Comments> commentsListOfEntity = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Comments.class, "commentsObj");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("commentsObj.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityIdList != null && !entityIdList.isEmpty()){
				c.add(Restrictions.in("commentsObj.entityId", Arrays.asList(entityIdList.toArray())));					
			}
			else if(entityInstanceId != null && entityInstanceId != 0){
				c.add(Restrictions.eq("commentsObj.entityId", entityInstanceId));
			}
			else if(userId != null){
					c.add(Restrictions.eq("commentsObj.entityId", 0));
					c.add(Restrictions.eq("commentsObj.createdBy.userId", userId));
			}		
			if(jtStartIndex != null && jtPageSize != null){
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			commentsListOfEntity = new ArrayList<Comments>();
			commentsListOfEntity = c.list();
		} catch (HibernateException e) {
			log.error("Error getting CommentsOfEntityID or EntityIDList : ", e);
		}
		return commentsListOfEntity;
	}
	
	@Override
	@Transactional
	public Integer getCommentsCountOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId) {
		log.info("Fetching Comments Of EntityOrInstance Pagination with parameters entityTypeId - "+entityTypeId);
		log.debug("Fetching Comments Of EntityOrInstancePagination with parameters entityTypeId - "+entityTypeId+", entityInstanceId - "+entityInstanceId+", fileName - "+fileName+", userId - "+userId);
		Integer commentsPagination = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Comments.class, "commentsObj");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("commentsObj.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityInstanceId != null && entityInstanceId != 0){
				c.add(Restrictions.eq("commentsObj.entityId", entityInstanceId));
			}else{
				if(userId != null){
					c.add(Restrictions.eq("commentsObj.entityId", 0));
					c.add(Restrictions.eq("commentsObj.createdBy.userId", userId));
				}
			}
			c.setProjection(Projections.rowCount());
			commentsPagination = ((Long)c.uniqueResult()).intValue(); 
			
		}catch(Exception ex){
			log.error("Error in getCommentsOfEntityOrInstancePagination ", ex);
		}
		return commentsPagination;
	}
	
	@Override
	@Transactional
	public List<Object[]> getAttachmentCountOfEntity(Integer entityTypeId) {
		List<Object[]> attachmentCountDetails = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			c.setProjection(Projections.projectionList().add(Projections.property("attachment.entityId")).add(Projections.count("attachment.entityId")).add(Projections.groupProperty("attachment.entityId")));
			attachmentCountDetails = c.list(); 
		}catch(Exception ex){
			log.error("Error in getAttachmentCountOfEntityOrInstance ", ex);
		}
		return attachmentCountDetails;
	}
	

	@Override
	@Transactional
	public List<Object[]> getAttachmentCountOfEntityTestFactoryLevel(Integer entityTypeId, Integer entityInstanceId) {
		List<Object[]> attachmentCountDetails = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityInstanceId != null && entityInstanceId != 0){
				c.add(Restrictions.eq("attachment.entityId", entityInstanceId));
			}
			c.setProjection(Projections.projectionList().add(Projections.property("attachment.entityId")).add(Projections.count("attachment.entityId")).add(Projections.groupProperty("attachment.entityId")));
			attachmentCountDetails = c.list(); 
		}catch(Exception ex){
			log.error("Error in getAttachmentCountOfEntityOrInstance ", ex);
		}
		return attachmentCountDetails;
	}
	
	@Override
	@Transactional
	public List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, List<Integer> entityInstanceIds, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Inside getAttachmentOfEntityOrInstance with parameters entityTypeId - "+entityTypeId+", entityInstanceIds - "+entityInstanceIds+", jtStartIndex - "+jtStartIndex+", jtPageSize - "+jtPageSize);
		List<Attachment> attachments = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityInstanceIds != null && entityInstanceIds.size() > 0){
				c.add(Restrictions.in("attachment.entityId", entityInstanceIds));
			}else{
				c.add(Restrictions.isNull("attachment.entityId"));
			}
			
			if(jtStartIndex != null && jtPageSize != null){
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			
			attachments = c.list();
			
		}catch(Exception ex){
			log.error("Error in getAttachmentOfEntityOrInstance ", ex);
		}
		return attachments;
	}
	
	@Override
	@Transactional
	public Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, List<Integer> entityInstanceIds) {
		log.info("Inside getAttachmentOfEntityOrInstancePagination with parameters entityTypeId - "+entityTypeId+", entityInstanceIds - "+entityInstanceIds);
		Integer attachmentPagination = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			if(entityTypeId != null && entityTypeId != 0){
				c.add(Restrictions.eq("attachment.entityMaster.entitymasterid", entityTypeId));
			}
			if(entityInstanceIds != null && entityInstanceIds.size() > 0){
				c.add(Restrictions.in("attachment.entityId", entityInstanceIds));
			}else{
				c.add(Restrictions.isNull("attachment.entityId"));
			}
			
			c.setProjection(Projections.rowCount());
			attachmentPagination = ((Long)c.uniqueResult()).intValue(); 
			
		}catch(Exception ex){
			log.error("Error in getAttachmentOfEntityOrInstancePagination ", ex);
		}
		return attachmentPagination;
	}

	@Override
	@Transactional
	public List<Attachment> getAttachmentsConsolidatedForProduct(Integer productId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Getting AttachmentsConsolidatedForProduct with parameters productId - "+productId);
		log.debug("Inside getAttachmentsConsolidatedForProduct with parameters productId - "+productId+", jtStartIndex - "+jtStartIndex+", jtPageSize - "+jtPageSize);
		List<Attachment> attachments = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.add(Restrictions.eq("attachment.product.productId", productId));
			if(jtStartIndex != null && jtPageSize != null){
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			attachments = c.list();
		}catch(Exception ex){
			log.error("Error in getAttachmentsConsolidatedForProduct ", ex);
		}
		return attachments;
	}

	@Override
	@Transactional
	public Integer getAttachmentsConsolidatedForProductPagination(Integer productId) {
		Integer attachmentPagination = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.add(Restrictions.eq("attachment.product.productId", productId));
			c.setProjection(Projections.rowCount());
			attachmentPagination = ((Long)c.uniqueResult()).intValue(); 
		}catch(Exception ex){
			log.error("Error in getAttachmentsConsolidatedForProductPagination ", ex);
		}
		return attachmentPagination;
	}

	@Override
	@Transactional
	public void addComments(Comments comments) {
		log.info("Adding Comments");
		try{
			sessionFactory.getCurrentSession().save(comments);
		}catch(Exception ex){
			log.error("Error Adding Comments ", ex);
		}
	}

	@Override
	@Transactional
	public Integer getResourceForAutoAllocation(String tableName, String resourceTypeName, Integer productId, String startDate, String endDate, String workloadField) {
		Integer userId = null;
		try{
			String sqlOfResourceForAutoAllocation = "SELECT ul.userId FROM user_list ul LEFT JOIN "+tableName+" tn ON tn."+resourceTypeName+" = ul.userId"
					+ " LEFT JOIN product_team_resources ptr ON tn."+resourceTypeName+" = ptr.userId"
					+ " WHERE ptr.productId = "+productId+" AND ptr.fromDate <= '"+startDate+"' AND ptr.toDate >= '"+endDate+"'"
					+ " GROUP BY tn."+resourceTypeName+" ORDER BY SUM(tn."+workloadField+"), tn."+resourceTypeName +" LIMIT 1";
			
			userId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlOfResourceForAutoAllocation).uniqueResult();
			
		}catch(Exception ex){
			log.error("Error occured in getResourceForAutoAllocation - ", ex);
		}
		return userId;
	}

	@Override
	@Transactional
	public List<Comments> getCommentsListBasedOnDateFillter(List<Integer> ids,Integer entityTypeId,Date fromDate,Date toDate) {
		List<Comments>comments = new ArrayList<Comments>();
		try{
			
				Criteria c =sessionFactory.getCurrentSession().createCriteria(Comments.class,"comments");
				c.createAlias("comments.entityMaster", "entityMaster");
				if(fromDate != null && toDate != null){
					c.add(Restrictions.ge("comments.createdDate", fromDate));
					c.add(Restrictions.le("comments.createdDate", toDate));
				}				
				c.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
				c.addOrder(Order.desc("commentId"));
				
				if(ids != null && ids.size() != 0){
					c.add(Restrictions.in("comments.entityId", ids));				
				}
				comments = c.list();
			
		}catch(Exception ex){
			log.error("Unable to get Comments ", ex);
		}
		
		return comments;
		
	}

	@Override
	@Transactional
	public Comments getLatestCommentOfByEntityTypeAndInstanceId(Integer entityTypeId,Integer instanceId) {
		Comments comment =null;
		try{
			List<Comments>comments = new ArrayList<Comments>();
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Comments.class,"comment");
			criteria.createAlias("comment.entityMaster", "entityMaster");
			
			criteria.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
			criteria.add(Restrictions.eq("comment.entityId", instanceId));
			
			criteria.addOrder(Order.desc("comment.createdDate"));
			
			comments = criteria.list();
			if(comments!= null && comments.size() > 0 ){
				comment = comments.get(0);
			}
			
		}catch(Exception ex){
			log.error("Unable to get Comment ", ex);
		}
		return comment;
	}
	
	
	@Override
	@Transactional
	public List<Comments> getLatestCommentOfByEnagementIdAndProductId(Integer engagmentId,Integer productId, Integer entityTypeId) {
		List<Comments>comments = new ArrayList<Comments>();
		try{
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Comments.class,"comment");
			criteria.createAlias("comment.entityMaster", "entityMaster");
			criteria.createAlias("comment.product", "product");
			criteria.createAlias("product.testFactory", "testFactory");
			if(productId != null && productId >0) {
				criteria.add(Restrictions.eq("product.productId", productId));
			}
			criteria.add(Restrictions.eq("testFactory.testFactoryId", engagmentId));
			criteria.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
			
			criteria.addOrder(Order.desc("comment.createdDate"));
			
			comments = criteria.list();
			
			
		}catch(Exception ex){
			log.error("Unable to get Comment ", ex);
		}
		return comments;
	}
	
	@Override
	@Transactional
	public Comments getLatestCommentOfByEntityType(Integer entityTypeId) {
		List<Comments> commentsList = new ArrayList<Comments>();
		Comments comments = null;
		try{
			
				Criteria c =sessionFactory.getCurrentSession().createCriteria(Comments.class,"comments");
				c.createAlias("comments.entityMaster", "entityMaster");
								
				c.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
				c.addOrder(Order.desc("commentId"));							
				commentsList = c.list();
				 if(commentsList.size() > 0 && commentsList != null){
					 comments = commentsList.get(0);
				 }
			
		}catch(Exception ex){
			log.error("Unable to get Comments ", ex);
		}
		
		return comments;
	}
	
	
	@Override
	@Transactional
	public Comments getLatestCommentOfByEntityTypeAndEntityInstanceId(Integer entityTypeId,Integer entityInstanceId) {
		List<Comments> commentsList = new ArrayList<Comments>();
		Comments comments = null;
		try{
			
				Criteria c =sessionFactory.getCurrentSession().createCriteria(Comments.class,"comments");
				c.createAlias("comments.entityMaster", "entityMaster");
								
				c.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
				c.add(Restrictions.eq("comments.entityId", entityInstanceId));
				c.addOrder(Order.desc("commentId"));							
				commentsList = c.list();
				 if(commentsList.size() > 0 && commentsList != null){
					 comments = commentsList.get(0);
				 }
			
		}catch(Exception ex){
			log.error("Unable to get Comments ", ex);
		}
		
		return comments;
	}
	
	/*@Override
	@Transactional
	public List<Comments> getCommentsByDateRangeAndComment(Integer entityTypeId,Date fromDate,Date toDate,String comment) {
		log.info("getCommentsByDateRangeAndComment");
		List<Comments> commentsList = new ArrayList<Comments>();
		try {
			Criteria c =sessionFactory.getCurrentSession().createCriteria(Comments.class,"comments");
			c.createAlias("comments.entityMaster", "entityMaster");
							
			c.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
			if (fromDate != null) {
				c.add(Restrictions.ge("comments.createdDate", fromDate));
			}
			if (toDate != null) {
				c.add(Restrictions.le("comments.createdDate", toDate));
			}
			if(comment != null && !comment.isEmpty()) {
				c.add(Restrictions.eq("comments.comments", comment));
			}
			commentsList = c.list();
		} catch (RuntimeException re) {
			log.error("list workflow events failed", re);
		}
		return commentsList;
	}*/
	
	
	@Override
	@Transactional
	public List<Comments> getCommentsListBasedOnDateFillterAndComment(Integer productId,Integer entityTypeId,Date fromDate,Date toDate,String comment) {
		List<Comments>comments = new ArrayList<Comments>();
		try{
			
				Criteria c =sessionFactory.getCurrentSession().createCriteria(Comments.class,"comments");
				c.createAlias("comments.entityMaster", "entityMaster");
				c.createAlias("comments.product", "product");
				
				if(fromDate != null && toDate != null){
					c.add(Restrictions.ge("comments.createdDate", fromDate));
					c.add(Restrictions.le("comments.createdDate", toDate));
				}				
				c.add(Restrictions.eq("entityMaster.entitymasterid", entityTypeId));
				if(productId != null && productId >0) {
					c.add(Restrictions.eq("product.productId", productId));	
				}
				
				if(comment != null && !comment.isEmpty()) {
					c.add(Restrictions.ilike("comments.comments","%"+comment+"%"));
				}
				comments = c.list();
			
		}catch(Exception ex){
			log.error("Unable to get Comments ", ex);
		}
		return comments;
	}	
}
