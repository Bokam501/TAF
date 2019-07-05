package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.AttachmentType;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;

public interface CommonDAO {
	String duplicateName(String name,String tableName,String columnName,String entityName,String filter);

	String duplicateNameWithOutFilter(String value, String tableName, String columnName, String entityName);

	List<EntityMaster> getEntityTypeList();

	List<Object[]> getEntityForTypeList(String className, String aliasName, HashMap<String, Object> contraints, List<String> projectionKeyList);

	List<EntityMaster> getWorkflowCapableEntityTypeList();

	void addAttachment(Attachment attachment);
	
	void updateAttachment(Attachment attachment);

	List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize);

	Attachment getAttachmentById(Integer attachmentId);

	void deleteAttachment(Attachment attachment);

	List<AttachmentType> getAttachmentTypeForEntity(Integer entityTypeId);

	Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId);

	List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, List<Integer> entityInstanceIds, Integer jtStartIndex, Integer jtPageSize);

	Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, List<Integer> entityInstanceIds);
	String getModifiedFieldOldValue(String modifiedFieldName, String entityTypeName, String tableName, String primaryKeyColumnName, String ColumnValue, String filter);
	List<Object[]> getAttachmentCountOfEntity(Integer entityTypeId);

	List<Attachment> getAttachmentsConsolidatedForProduct(Integer productId, Integer jtStartIndex, Integer jtPageSize);

	Integer getAttachmentsConsolidatedForProductPagination(Integer productId);

	List<Object[]> getAttachmentCountOfEntityTestFactoryLevel(Integer entityTypeId, Integer entityInstanceId);

	List<Attachment> getAttachmentsOfEntityIDorEntityIDList(Integer entityTypeId, Integer entityInstanceId, List<Integer> entityIdList, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize);

	List<Comments> getCommentsOfEntityIDorEntityIDList(Integer entityTypeId,
			Integer entityInstanceId, List<Integer> entityIdList,
			String fileName, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);

	Integer getCommentsCountOfEntityOrInstancePagination(Integer entityTypeId,
			Integer entityInstanceId, String fileName, Integer userId);
	void addComments(Comments comments);

	Integer getResourceForAutoAllocation(String tableName, String resourceTypeName, Integer productId, String startDate, String endDate, String workloadField);

	List<Comments> getCommentsListBasedOnDateFillter( List<Integer> ids, Integer entityTypeId2, Date fromDate, Date toDate);
	Comments getLatestCommentOfByEntityTypeAndInstanceId(Integer entityTypeId,Integer instanceId);
	
	List<Comments> getLatestCommentOfByEnagementIdAndProductId(Integer engagmentId,Integer productId, Integer entityTypeId);
	Comments getLatestCommentOfByEntityType(Integer entityTypeId);
	
	Comments getLatestCommentOfByEntityTypeAndEntityInstanceId(Integer entityTypeId,Integer entityInstanceId);
	List<Comments> getCommentsListBasedOnDateFillterAndComment(Integer productId,Integer entityTypeId,Date fromDate,Date toDate,String comment);
}
