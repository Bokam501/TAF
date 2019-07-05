package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.AtsgParameters;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.dto.AttachmentDTO;

public interface AttachmentDAO{	
	Integer addTestDataAttachment(Attachment attachment);
	List<AttachmentDTO> listTestDataAttachmentDTO(Integer productId, int versionId, String attachmentType);
	Attachment getAttachmentById(Integer attachmentId);
	Attachment getAttachmentByFileName(Integer productVersionId, String fileName);
	List<Attachment> listTestDataAttachments(Integer productId, int versionId, String attachmentType);
	void upateTestDataAttachment(Attachment attachment);
	AtsgParameters getAtsgScriptParametersByTestCaseId(int testCaseId);
	void upateorsavAtsgParameters(AtsgParameters atsg);
	List<Object[]> listAllAttachmentsForProduct(Integer productId,int versionId,  int testRunPlanId, int jtStartIndex, int jtPageSize);
	int getUnMappedAttachmentCount(Integer productId, Integer productVersionId,int testRunPlanId);
	List<Object[]> listMappedAttachmentsForTestRunPlan(Integer productId,int versionId, int testRunPlanId);
	void mapTestRunPlanWithAttachment(Attachment attachment,TestRunPlan testRunPlan, String action);
	List<Attachment> listDataRepositoryAttachments(int testRunPlanId);
	Integer getAttachmentsCountByentityMasterId(int entityMasterId, int entityId, int jtStartIndex, int jtPageSize);
	Integer getAttachmentsCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize);
	List<Attachment> getTestRunPlanAttachments (Integer entityId , Integer testRunPlanId);
    public Attachment getAttachmentByFileName(Integer productId, String prefixName, Integer userId, String attachmentType);
	public List<String> getUIObjectItemPageNamewithUIObjectItemIdByProductId(
			Integer productId);
	public List<AttachmentDTO> listTestDataAttachmentDTO(Integer productId,
			int userId, String attachmentType, int jtStartIndex, int jtPageSize);
	public Integer totalTestDataAttachmentByProductIdAndUserId(Integer productId,
			int userId, String attachmentType);

}
