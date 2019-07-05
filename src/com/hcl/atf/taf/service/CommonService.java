package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonAttachmentType;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonSimpleOption;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;

public interface CommonService {

	List<String> getPalmBuildDetails(HttpServletRequest request);
	String duplicateName(String name,String tableName,String columnName,String entityName,String filter);
	String duplicateNameWithOutFilter(String name, String tableName, String columnName, String entityName);
	List<JsonEntityMaster> getEntityTypeList();
	JTableResponseOptions getEntityForTypeList(Integer engagementId, Integer productId, Integer entityTypeId);
	List<JsonEntityMaster> getWorkflowCapableEntityTypeList();
	void addAttachment(Attachment attachment);
	void updateAttachment(Attachment attachment);
	List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId, Integer jtStartIndex, Integer jtPageSize);
	Attachment getAttachmentById(Integer attachmentId);
	void deleteAttachment(Attachment attachment);
	List<JsonAttachmentType> getAttachmentTypeForEntity(Integer entityTypeId);
	Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId);
	List<Integer> getEntityInstanceIdsOfEntityByProductId(Integer productId, Integer entityTypeId);
	List<Attachment> getAttachmentOfEntityOrInstance(Integer entityTypeId, List<Integer> entityInstanceIds, Integer jtStartIndex, Integer jtPageSize);
	Integer getAttachmentOfEntityOrInstancePagination(Integer entityTypeId, List<Integer> entityInstanceIds);
	String getModifiedFieldOldValue(String modifiedFieldName, String entityTypeName, String tableName, String primaryKeyColumnName, String ColumnValue, String filter);
	List<Attachment> getAttachmentsConsolidatedForProduct(Integer productId, Integer jtStartIndex, Integer jtPageSize);
	Integer getAttachmentsConsolidatedForProductPagination(Integer productId);
	Integer getCountFromAttachmentCountDetails(List<Object[]> attachmentCountDetails, Integer entityInstanceId);
	List<Object[]> getAttachmentCountOfEntity(Integer entityWorkflowTemplateId);

	List<Comments> getCommentsOfEntityIDorEntityIDList(Integer entityTypeId, Integer entityInstanceId,
			List<Integer> entityIdList, String fileName, Integer userId, Integer jtStartIndex,
			Integer jtPageSize);
	Integer getCommentsCountOfEntityOrInstancePagination(Integer entityTypeId, Integer entityInstanceId, String fileName, Integer userId);
	void addComments(Comments comments);
	List<JsonSimpleOption> getColumnByFrequency(String frequencyType, Integer frequencyMonth, Integer frequencyYear);
	Date getModifiedAfterDate(Date loginDate, Date logoutDate);
	Boolean isModifiedAfterDate(Date instanceModifiedDate, Date modifiedAfterDate);
	Object autoAllocationOfResource(Object instanceObject);
	String getInstanceAndAutoAllocationOfResource(Integer entityId, Integer entityInstanceId, Integer parentEntityId, Integer parentEntityInstanceId, UserList user);
	List<Comments> getCommentsListBasedOnDateFillter(Integer entityTypeId, Integer productId, Integer workPackageId, Integer entityTypeId2, Date fromDate,Date toDate);
	void writeUploadReportFile(Workbook extractionReportWorkbook, String reportLocation, String reportName, ProductMaster product);
	void createAttachment(ProductMaster product, String reportName, String reportLocation);
	Comments getLatestCommentOfByEntityTypeAndInstanceId(Integer entityTypeId,Integer instanceId);
	Comments getLatestCommentOfByEntityType(Integer entityTypeId);
	List<Comments> getCommentsListBasedOnDateFillterAndComment(Integer productId,Integer entityTypeId,Date fromDate,Date toDate,String comment);
}
