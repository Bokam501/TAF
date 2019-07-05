package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.model.AtsgParameters;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.dto.AttachmentDTO;
import com.hcl.atf.taf.service.AttachmentService;

@Service
public class AttachmentServiceImpl  implements AttachmentService{

	@Autowired
    private AttachmentDAO attachmentsDAO;
	
	@Override
	public Integer addTestDataAttachment(Attachment attachment) {
		return attachmentsDAO.addTestDataAttachment(attachment);
	}

	@Override
	public List<AttachmentDTO> listTestDataAttachmentDTO(Integer productId, int versionId, String attachmentType) {
		return attachmentsDAO.listTestDataAttachmentDTO(productId, versionId, attachmentType);
	}
	
	@Override
	public List<Attachment> listTestDataAttachment(Integer productId, int versionId, String attachmentType) {
		return attachmentsDAO.listTestDataAttachments(productId, versionId, attachmentType);
	}

	@Override
	public Attachment getAttachmentById(Integer attachmentId) {
		return attachmentsDAO.getAttachmentById(attachmentId);
	}
	@Override
	public Attachment getAttachmentByFileName(Integer productVersionId,String fileName) {
		return attachmentsDAO.getAttachmentByFileName(productVersionId,fileName);
	}

	@Override
	public void upateTestDataAttachment(Attachment attachment) {
		 attachmentsDAO.upateTestDataAttachment(attachment);		
	}

	@Override
	public AtsgParameters getAtsgScriptParametersByTestCaseId(int testCaseId) {
		return attachmentsDAO.getAtsgScriptParametersByTestCaseId(testCaseId);	
	}
	@Override
	public void upateorsavAtsgParameters(AtsgParameters atsg) {
		attachmentsDAO.upateorsavAtsgParameters(atsg);
	}

	@Override
	@Transactional
	public List<Object[]> listAllAttachmentsForProduct(Integer productId,int versionId,  int testRunPlanId, int jtStartIndex, int jtPageSize) {
		
		return attachmentsDAO.listAllAttachmentsForProduct(productId, versionId, testRunPlanId, jtStartIndex, jtPageSize);
				
	}

	@Override
	@Transactional
	public int getUnMappedAttachmentCount(Integer productId,Integer productVersionId, int testRunPlanId) {
		return attachmentsDAO.getUnMappedAttachmentCount(productId,productVersionId, testRunPlanId);
	}

	@Override
	@Transactional
	public List<Object[]> listMappedAttachmentsForTestRunPlan(Integer productId, int versionId, int testRunPlanId) {
		return attachmentsDAO.listMappedAttachmentsForTestRunPlan(productId,versionId, testRunPlanId);
	}

	@Override
	@Transactional
	public void mapTestRunPlanWithAttachment(Attachment attachment,TestRunPlan testRunPlan, String action) {		
		attachmentsDAO.mapTestRunPlanWithAttachment(attachment,testRunPlan, action);
	}

	@Override
	@Transactional
	public List<Attachment> listDataRepositoryAttachments(int testRunPlanId) {
		return attachmentsDAO.listDataRepositoryAttachments(testRunPlanId);
	}

	@Override
	@Transactional
	public Integer getAttachmentsCountByentityMasterId(int entityMasterId,
			int entityId, int jtStartIndex, int jtPageSize) {
		return attachmentsDAO.getAttachmentsCountByentityMasterId(entityMasterId, entityId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getAttachmentsCountFromProductHierarchy(Integer productId,
			Boolean isHierarchy, Integer jtStartIndex, Integer jtPageSize) {
		return attachmentsDAO.getAttachmentsCountFromProductHierarchy(productId, isHierarchy, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<Attachment> getTestRunPlanAttachments(Integer entityId,
			Integer testRunPlanId) {
		return attachmentsDAO.getTestRunPlanAttachments(entityId, testRunPlanId);
	}	
	
	@Override
	public Attachment getAttachmentByFileName(Integer productId, String fileName,Integer userId,String attachmentType) {
		return this.attachmentsDAO.getAttachmentByFileName(productId, fileName,userId,attachmentType);
	}
	@Override
	@Transactional
	public List<String> listUIObjectItemPageNamewithUIObjectItemIdByProductId(
			Integer productId) {
		
		return attachmentsDAO.getUIObjectItemPageNamewithUIObjectItemIdByProductId(productId);
	}
	@Override
	@Transactional
	public List<AttachmentDTO> listTestDataAttachmentDTO(Integer projectCodeId, int userId, String attachmentType,int jtStartIndex, int jtPageSize) {
		return this.attachmentsDAO.listTestDataAttachmentDTO(projectCodeId, userId, attachmentType,jtStartIndex,jtPageSize);
	}
	@Override
	@Transactional
	public Integer totalTestDataAttachmentByProductIdAndUserId(Integer productId,
			int userId, String string) {
		return attachmentsDAO.totalTestDataAttachmentByProductIdAndUserId(productId,userId,string);
	}
}