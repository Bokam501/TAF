package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.model.AtsgParameters;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.dto.AttachmentDTO;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {
	private static final Log log = LogFactory.getLog(AttachmentDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private ActivityWorkPackageDAO activityWorkPackageDAO;
	@Autowired
	private ActivityDAO activityDAO;
	@Override
	@Transactional
	public Integer addTestDataAttachment(Attachment attachment) {
		log.debug("adding TestData instance");
		try {
			attachment.setStatus(1);
			attachment.setModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(attachment);
			log.debug("add successful"+attachment.getAttachmentId());
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return attachment.getAttachmentId();
	}


	@Override
	@Transactional
	public List<Attachment> listTestDataAttachments(Integer productId, int versionId, String attachmentType) {
		log.debug("listing listTestDataAttachments instance");
		List<Attachment> attachmentList=null;
		try {	
			List<Integer> versionIdList = null;

			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");	
			crit.createAlias("attachment.entityMaster", "em");
			crit.add(Restrictions.eq("em.entitymastername", IDPAConstants.ENTITY_PRODUCT_VERSION));//Filtering only Version based attachments.
			if(productId != -1){
				versionIdList = productVersionListMasterDAO.listVersionId(productId);			
				crit.add(Restrictions.in("attachment.entityId",versionIdList));
			}else if(versionId !=-1){
				crit.add(Restrictions.eq("attachment.entityId", versionId));			
			}
			if(!attachmentType.equalsIgnoreCase("-1")){
				crit.add(Restrictions.eq("attachment.attachmentType", attachmentType));	
			}
			crit.add(Restrictions.eq("attachment.status", 1));
			attachmentList = crit.list();
			if (!(attachmentList == null || attachmentList.isEmpty())){
				for (Attachment attach : attachmentList) {
					Hibernate.initialize(attach.getEntityMaster());
					Hibernate.initialize(attach.getCreatedBy());
					Hibernate.initialize(attach.getModifiedBy());
				}
			}
			log.debug("list successful");

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return attachmentList;    
	}


	@Override
	@Transactional
	public List<AttachmentDTO> listTestDataAttachmentDTO(Integer productId, int versionId, String attachmentType) {
		log.debug("listing listTestDataAttachments instance");
		List<Attachment> attachmentList=null;
		List<AttachmentDTO> attachmentDTOList = new ArrayList<AttachmentDTO>();
		try {	
			List<Integer> versionIdList = null;

			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");	
			crit.createAlias("attachment.entityMaster", "em");
			crit.add(Restrictions.eq("em.entitymastername", IDPAConstants.ENTITY_PRODUCT_VERSION));//Filtering only Version based attachments.
			if(productId != -1){
				versionIdList = productVersionListMasterDAO.listVersionId(productId);			
				crit.add(Restrictions.in("attachment.entityId",versionIdList));
			}else if(versionId !=-1){
				crit.add(Restrictions.eq("attachment.entityId", versionId));			
			}
			if(!attachmentType.equalsIgnoreCase("-1")){
				crit.add(Restrictions.eq("attachment.attachmentType", attachmentType));	
			}
			crit.add(Restrictions.ne("attachment.attachmentType", "EDAT"));	
			crit.add(Restrictions.eq("attachment.status", 1));
			attachmentList = crit.list();
			if (!(attachmentList == null || attachmentList.isEmpty())){
				for (Attachment attach : attachmentList) {	

					Hibernate.initialize(attach.getEntityMaster());
					Hibernate.initialize(attach.getCreatedBy());
					Hibernate.initialize(attach.getModifiedBy());
					AttachmentDTO attDTO = new AttachmentDTO();
					attDTO.setAttachmentId(attach.getAttachmentId());
					if(attach.getEntityMaster() != null){
						attDTO.setEntityMasterId(attach.getEntityMaster().getEntitymasterid());
						attDTO.setEntityMasterName(attach.getEntityMaster().getEntitymastername());					
					}
					if(attach.getEntityId() != null){
						attDTO.setEntityPrimaryId(attach.getEntityId());
						ProductVersionListMaster pversion = productVersionListMasterDAO.getByProductListId(attach.getEntityId());
						if(pversion != null)
							attDTO.setEntityName(pversion.getProductVersionName());
					}

					if(attach.getAttachmentType() != null){
						attDTO.setAttachmentType(attach.getAttachmentType());
					}

					if(attach.getAttributeFileExtension() != null){
						attDTO.setAttributeFileExtension(attach.getAttributeFileExtension());
					}

					if(attach.getAttributeFileName() != null){
						attDTO.setAttributeFileName(attach.getAttributeFileName());
					}
					if(attach.getAttributeFileURI() != null){
						attDTO.setAttributeFileName(attach.getAttributeFileURI());						
					}

					if(attach.getDescription() != null){
						attDTO.setDescription(attach.getDescription());
					}

					if(attach.getCreatedBy() != null){
						attDTO.setCreaterId(attach.getCreatedBy().getUserId());
						attDTO.setCreaterName(attach.getCreatedBy().getLoginId());
					}

					if(attach.getModifiedBy() != null){
						attDTO.setModifierId(attach.getModifiedBy().getUserId());
						attDTO.setModifierName(attach.getModifiedBy().getLoginId());
					}

					if(attach.getAttributeFileExtension() != null){
						attDTO.setAttributeFileExtension(attach.getAttributeFileExtension());
					}

					if(attach.getUploadedDate() != null){
						attDTO.setUploadedDate(DateUtility.dateToStringWithSeconds1(attach.getUploadedDate()));
					}

					if(attach.getModifiedDate() != null){
						attDTO.setModifiedDate(DateUtility.dateToStringWithSeconds1(attach.getModifiedDate()));
					}
					if(attach.getLastModifiedDate() != null){
						attDTO.setLastModifiedDate(DateUtility.dateToStringWithSeconds1(attach.getLastModifiedDate()));
					}

					if(attach.getStatus() != null){
						attDTO.setStatus(attach.getStatus());
					}	
					attachmentDTOList.add(attDTO);
				}
			}
			log.debug("list successful");

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return attachmentDTOList;    
	}

	@Override
	@Transactional
	public Attachment getAttachmentById(Integer attachmentId) {
		log.debug("getting getAttachmentById instance by attachmentId");

		List<Attachment> attachmentList=null;
		Attachment attachment=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.add(Restrictions.eq("attachment.attachmentId",attachmentId));

			attachmentList=c.list();
			attachment = (attachmentList != null && attachmentList.size() != 0) ? (Attachment)(attachmentList.get(0)): null;				

			log.debug("Attachment successful");
		} catch (Exception re) {
			log.error("Listing Attachment failed", re);
		}
		return attachment;
	}

	@Override
	@Transactional
	public Attachment getAttachmentByFileName(Integer productVersionId,String fileName) {
		log.debug("getting getAttachmentByFileName instance by FileName");

		List<Attachment> attachmentList=null;
		Attachment attachment=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.add(Restrictions.eq("attachment.attributeFileName",fileName));
			c.add(Restrictions.eq("attachment.entityId",productVersionId));

			attachmentList=c.list();
			attachment = (attachmentList != null && attachmentList.size() != 0) ? (Attachment)(attachmentList.get(0)): null;				

			log.debug("Attachment successful");
		} catch (Exception re) {
			log.error("ERROR Getting AttachmentByFileName failed : ", re);
		}
		return attachment;
	}
	@Override
	@Transactional
	public void upateTestDataAttachment(Attachment attachment) {
		log.debug("updating the attachment");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(attachment);
		}catch (Exception re) {
			log.error("ERROR updating TestDataAttachment : ", re);
		}
	}


	@Override
	@Transactional
	public AtsgParameters getAtsgScriptParametersByTestCaseId(int testCaseId) {
		List<AtsgParameters> atsgList=null;
		AtsgParameters atsg=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(AtsgParameters.class, "atsg");
			c.add(Restrictions.eq("atsg.testCaseId",testCaseId));

			atsgList=c.list();
			atsg = (atsgList != null && atsgList.size() != 0) ? (AtsgParameters)(atsgList.get(0)): null;				

			log.debug("atsg successful");
		} catch (Exception re) {
			log.error("atsg failed", re);
			// throw re;
		}
		return atsg;
	}

	@Override
	@Transactional
	public void upateorsavAtsgParameters(AtsgParameters atsg) {
		log.debug("updating the atsg");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(atsg);
		}catch (Exception re) {
			log.error("atsg update failed", re);
		}
	}


	@Override
	@Transactional
	public List<Object[]> listAllAttachmentsForProduct(Integer productId,int versionId,  int testRunPlanId, int jtStartIndex, int jtPageSize) {
		List<Object[]> unMappedTRPAttachmentListObj = null;
		String sql=null;
		try{
			if(versionId != -1){
				sql="SELECT DISTINCT attachmentId, attachmentName, attachmentType FROM attachment WHERE attachmentId NOT IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND entityId=:versionId AND productId=:productId ";
				sql = sql+"OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";			
				unMappedTRPAttachmentListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productId", productId).
						setParameter("versionId", versionId).
						setParameter("entityMasterId", 19).
						setParameter("testRunPlanId", testRunPlanId).
						list();
			} else {
				sql="SELECT DISTINCT attachmentId, attachmentName, attachmentType FROM attachment WHERE attachmentId NOT IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND productId=:productId ";
				sql = sql+"OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";			
				unMappedTRPAttachmentListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productId", productId).
						setParameter("entityMasterId", 19).
						setParameter("testRunPlanId", testRunPlanId).
						list();
			}
			log.info("unmapped attachment list :"+unMappedTRPAttachmentListObj.size());
		} catch(Exception e){
			log.error(e);
		}
		return unMappedTRPAttachmentListObj;
	}


	@Override
	@Transactional
	public int getUnMappedAttachmentCount(Integer productId,Integer productVersionId, int testRunPlanId) {
		log.info("Get ProductFeature unmapped to Testcaselist");
		int totUnMappedAttachmentCount = 0;
		try {
			if(productVersionId != -1){
				totUnMappedAttachmentCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT DISTINCT COUNT(*) FROM attachment WHERE attachmentId NOT IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND entityId=:entityId AND productId=:productId").
						setParameter("productId", productId).
						setParameter("entityId", productVersionId).
						setParameter("entityMasterId",19).
						setParameter("testRunPlanId", testRunPlanId).uniqueResult()).intValue();
			} else {
				totUnMappedAttachmentCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT DISTINCT COUNT(*) FROM attachment WHERE attachmentId NOT IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND productId=:productId").
						setParameter("productId", productId).
						setParameter("entityMasterId",19).
						setParameter("testRunPlanId", testRunPlanId).uniqueResult()).intValue();
			}
		} catch (Exception e) {
			log.error(e);
		}		
		return totUnMappedAttachmentCount;
	}


	@Override
	@Transactional
	public List<Object[]> listMappedAttachmentsForTestRunPlan(Integer productId, int versionId, int testRunPlanId) {
		List<Object[]> mappedTRPAttachmentListObj = null;
		String sql=null;
		try{
			if(versionId != -1){
				sql="SELECT DISTINCT attachmentId, attachmentName, attachmentType FROM attachment WHERE attachmentId IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND entityId=:versionId AND productId=:productId";
				mappedTRPAttachmentListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productId", productId).
						setParameter("versionId", versionId).
						setParameter("entityMasterId", 19).
						setParameter("testRunPlanId", testRunPlanId).
						list();
			} else {
				sql="SELECT DISTINCT attachmentId, attachmentName, attachmentType FROM attachment WHERE attachmentId IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId) AND entityMasterId=:entityMasterId AND productId=:productId";
				mappedTRPAttachmentListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("productId", productId).
						setParameter("entityMasterId", 19).
						setParameter("testRunPlanId", testRunPlanId).
						list();
			}
			log.info("Mapped attachment list :"+mappedTRPAttachmentListObj.size());
		} catch(Exception e){
			log.error(e);
		}
		return mappedTRPAttachmentListObj;
	}


	@Override
	@Transactional
	public void mapTestRunPlanWithAttachment(Attachment attachment,TestRunPlan testRunPlan, String action) {		
		try{
			if(action.equalsIgnoreCase("map")){		
				sessionFactory.getCurrentSession().createSQLQuery("insert into test_runplan_has_attachments (testRunPlanId,attachmentId) values ("+testRunPlan.getTestRunPlanId()+","+attachment.getAttachmentId()+")").executeUpdate();
			}else if(action.equalsIgnoreCase("unmap")){						
				sessionFactory.getCurrentSession().createSQLQuery("delete from test_runplan_has_attachments where attachmentId=:attachmentId and testRunPlanId=:trpId").setParameter("attachmentId",attachment.getAttachmentId()).setParameter("trpId", testRunPlan.getTestRunPlanId()).executeUpdate();
			}
		} catch(Exception e){
			log.error(e);
		}		
	}

	@Override
	@Transactional
	public List<Attachment> listDataRepositoryAttachments(int testRunPlanId) {
		List<Attachment> testRunPlanAttachmentListObj = new ArrayList<Attachment>();

		String sql=null;
		try{
			sql="SELECT * FROM attachment WHERE attachmentId IN(SELECT attachmentId FROM test_runplan_has_attachments WHERE testRunPlanId=:testRunPlanId)";
			List list =sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Attachment.class).setParameter("testRunPlanId", testRunPlanId).list();
			for(Iterator iterator = list.iterator();iterator.hasNext();){
				Attachment attachment = (Attachment)iterator.next();
				testRunPlanAttachmentListObj.add(attachment);
			}
			log.info("Test Run Plan attachment size :"+testRunPlanAttachmentListObj.size());
		} catch(Exception e){
			log.error(e);
		}
		return testRunPlanAttachmentListObj;
	}


	@Override
	@Transactional
	public Integer getAttachmentsCountByentityMasterId(int entityMasterId, int entityId, int jtStartIndex, int jtPageSize) {
		log.debug("listing getAttachmentsCountByentityMasterId instance");
		int wpTCCount = 0;
		
		String sql="select count(att.entityMasterId) as entityMasterIdCount from attachment att where att.entityMasterId=:entMastId and att.entityId=:entId order by entityMasterIdCount asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("entMastId", entityMasterId)
						.setParameter("entId", entityId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("entMastId", entityMasterId)
						.setParameter("entId", entityId)
						.uniqueResult()).intValue();					
			}

		} catch (RuntimeException re) {
			log.error("list getAttachmentsCountByentityMasterId", re);
		}
		return wpTCCount;
	}


	@Override
	@Transactional
	public Integer getAttachmentsCountFromProductHierarchy(Integer productId, Boolean isHierarchy, 
			Integer jtStartIndex, Integer jtPageSize){
		int attachmentsCount = 0;
		List<Integer> awpIdList = null;
		List<Integer> actIdList = null;
		String sql = null;
		Boolean paramIsList = false;
		try {
			if(isHierarchy){				
				awpIdList = activityWorkPackageDAO.getActivityWorkPackagesofProductIDS(productId, null, paramIsList);
				paramIsList = true;
				actIdList = activityDAO.getActivitiesofAWPIDS(null, awpIdList, paramIsList);
				if(awpIdList != null && !awpIdList.isEmpty()){
					if(actIdList != null && !actIdList.isEmpty()){

						sql = "select count(*) from  attachment as att "
								+ "where (att.entityMasterId=18 and att.entityId=:prodId) "
								+ "or (att.entityMasterId=34 and att.entityId in (:awpIds)) "
								+ "or (att.entityMasterId=28 and att.entityId in (:actIds)) ";

						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						attachmentsCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.setParameterList("actIds", actIdList)
								.uniqueResult()).intValue();	
					}else{
						sql = "select count(*) from  attachment as att "
								+ "where (att.entityMasterId=18 and att.entityId=:prodId) "
								+ "or (att.entityMasterId=34 and att.entityId in (:awpIds)) ";

						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						attachmentsCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.uniqueResult()).intValue();	
					}
				}			
			}else{
				sql ="select count(*) from  attachment as att "
						+ "where att.entityMasterId=18 and att.entityId=:prodId ";
				if(jtStartIndex != -1 && jtPageSize != -1 ){
					sql = sql + "limit ";								
					sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
				}
				attachmentsCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("prodId", productId).uniqueResult()).intValue();
			}
		} catch (Exception e) {
			log.error("getAttachmentsCountFromProductHierarchy failed");
		}
		return attachmentsCount;
	}

	@Override
	@Transactional
	public List<Attachment> getTestRunPlanAttachments(Integer entityMasterId,
			Integer testRunPlanId) {
		List<Attachment> attachmentList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.createAlias("attachment.entityMaster", "entityMaster");
			c.add(Restrictions.eq("entityMaster.entitymasterid",entityMasterId));
			c.add(Restrictions.eq("attachment.entityId",testRunPlanId));
			attachmentList=c.list();		
			log.debug("Attachments successful");
		} catch (Exception re) {
			log.error("ERROR Getting Attachments failed : ", re);
		}
		return attachmentList;
	}
	@Transactional
	@Override
	public Attachment getAttachmentByFileName(Integer productId, String fileName,Integer userId,String attachmentType) {
		log.debug((Object)"getting getAttachmentByFileName instance by FileName");
		List<Attachment> attachmentList = null;
		Attachment attachment = null;
		try {
			Criteria c = this.sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			c.createAlias("attachment.createdBy", "user");
			c.createAlias("attachment.product", "product");
			//need to check this 
			if (productId != -1) {
				c.add((Criterion)Restrictions.eq((String)"product.productId", productId));
			}  if (userId != -1) {
				c.add((Criterion)Restrictions.eq((String)"user.userId", userId));
			}
			if (!attachmentType.equalsIgnoreCase("-1")) {
				c.add((Criterion)Restrictions.eq("attachment.attachmentType",attachmentType));
			}
			c.add((Criterion)Restrictions.eq((String)"attachment.attachmentPrefixName", fileName));
			attachmentList = c.list();
			attachment = attachmentList != null && attachmentList.size() != 0 ? (Attachment)attachmentList.get(0) : null;
			log.debug((Object)"Attachment successful");
		}
		catch (Exception re) {
			re.printStackTrace();
			log.error((Object)"Attachment failed", (Throwable)re);
		}
		return attachment;
	}
	@Override
	@Transactional
	public List<String> getUIObjectItemPageNamewithUIObjectItemIdByProductId(Integer productId) {
		log.debug("getting UIObjectItems");
		List<String> uiObjectsList = null;

		try {
			String query = " select uiObject.pageName from ui_object_items uiObject  where productId = :productId";
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("productId", productId);
			uiObjectsList = sqlQuery.list();
			log.debug("getting UIObjectItemes PageName with UIObjectItemId is successful");
		} catch (Exception re) {
			log.error("getting UIObjectItemes PageName with UIObjectItemId failed", re);
		}
		return uiObjectsList;
	}
	@Transactional
	@Override
	public List<AttachmentDTO> listTestDataAttachmentDTO(Integer productId, int userId, String attachmentType,int jtStartIndex, int jtPageSize) {
		log.debug((Object)"listing listTestDataAttachments instance");
		List<Attachment> attachmentList = null;
		ArrayList<AttachmentDTO> attachmentDTOList = new ArrayList<AttachmentDTO>();
		try {
			Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
			crit.createAlias("attachment.createdBy", "user");
			crit.createAlias("attachment.product", "pm");
			if (productId != -1) {
				crit.add((Criterion)Restrictions.eq((String)"pm.productId", (Object)productId));
			}  if (userId != -1) {
				crit.add((Criterion)Restrictions.eq((String)"user.userId", (Object)userId));
			}
			if (!attachmentType.equalsIgnoreCase("-1")) {
				crit.add((Criterion)Restrictions.eq((String)"attachment.attachmentType", (Object)attachmentType));
			}
			crit.add((Criterion)Restrictions.eq((String)"attachment.status", (Object)1));
			if(jtStartIndex != -1){
				crit.setFirstResult(jtStartIndex);
			}
			if(jtPageSize != -1){
				crit.setMaxResults(jtPageSize);
			}

			attachmentList = crit.list();
			if (attachmentList != null && !attachmentList.isEmpty()) {
				for (Attachment attach : attachmentList) {
					Hibernate.initialize((Object)attach.getEntityMaster());
					Hibernate.initialize((Object)attach.getCreatedBy());
					Hibernate.initialize((Object)attach.getModifiedBy());
					AttachmentDTO attDTO = new AttachmentDTO();
					attDTO.setAttachmentId(attach.getAttachmentId());
					if (attach.getEntityMaster() != null) {
						attDTO.setEntityMasterId(attach.getEntityMaster().getEntitymasterid());
						attDTO.setEntityMasterName(attach.getEntityMaster().getEntitymastername());
					}
					if (attach.getEntityId() != null) {
						attDTO.setEntityPrimaryId(attach.getEntityId());
					}
					attDTO.setEntityName(null);
					if (attach.getAttachmentName() != null) {
						attDTO.setAttachmentName(attach.getAttachmentName());
					}
					if (attach.getAttachmentType() != null) {
						attDTO.setAttachmentType(attach.getAttachmentType());
					}
					if (attach.getAttributeFileExtension() != null) {
						attDTO.setAttributeFileExtension(attach.getAttributeFileExtension());
					}
					if (attach.getAttributeFileName() != null) {
						attDTO.setAttributeFileName(attach.getAttributeFileName());
					}
					if (attach.getAttributeFileURI() != null) {
						attDTO.setAttributeFileURI(attach.getAttributeFileURI());
					}
					if (attach.getDescription() != null) {
						attDTO.setDescription(attach.getDescription());
					}
					if (attach.getCreatedBy() != null) {
						attDTO.setCreaterId(attach.getCreatedBy().getUserId());
						attDTO.setCreaterName(attach.getCreatedBy().getLoginId());
					}
					if (attach.getModifiedBy() != null) {
						attDTO.setModifierId(attach.getModifiedBy().getUserId());
						attDTO.setModifierName(attach.getModifiedBy().getLoginId());
					}
					if (attach.getAttributeFileExtension() != null) {
						attDTO.setAttributeFileExtension(attach.getAttributeFileExtension());
					}
					if (attach.getUploadedDate() != null) {
						attDTO.setUploadedDate(DateUtility.dateToStringWithSeconds1((Date)attach.getUploadedDate()));
					}
					if (attach.getModifiedDate() != null) {
						attDTO.setModifiedDate(DateUtility.dateToStringWithSeconds1((Date)attach.getModifiedDate()));
					}
					if (attach.getLastModifiedDate() != null) {
						attDTO.setLastModifiedDate(DateUtility.dateToStringWithSeconds1((Date)attach.getLastModifiedDate()));
					}
					if (attach.getStatus() != null) {
						attDTO.setStatus(attach.getStatus());
					}
					attachmentDTOList.add(attDTO);
				}
			}
			log.debug((Object)"list successful");
		}
		catch (RuntimeException re) {
			log.error((Object)"list failed", (Throwable)re);
		}
		return attachmentDTOList;
	}
	 @Transactional
	    @Override
	    public Integer totalTestDataAttachmentByProductIdAndUserId(Integer productId, int userId, String attachmentType) {
	    	 log.debug((Object)"listing listTestDataAttachments instance");
	    	 Integer count = 0;
	         try {
	             Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(Attachment.class, "attachment");
	             crit.createAlias("attachment.createdBy", "user");
	             crit.createAlias("attachment.product", "pm");
	             if (productId != -1) {
	                 crit.add((Criterion)Restrictions.eq((String)"pm.productId", (Object)productId));
	             }  if (userId != -1) {
	                 crit.add((Criterion)Restrictions.eq((String)"user.userId", (Object)userId));
	             }
	             if (!attachmentType.equalsIgnoreCase("-1")) {
	                 crit.add((Criterion)Restrictions.eq((String)"attachment.attachmentType", (Object)attachmentType));
	             }
	             crit.add((Criterion)Restrictions.eq((String)"attachment.status", (Object)1));
	           String  total = ""+ crit .setProjection(Projections.countDistinct("attachment.attachmentId")).uniqueResult();
	           count = (Integer)Integer.parseInt(total);
	         } catch (RuntimeException re) {
	             log.error("list failed", re);
	         }
			return count;
	             
	    }
}
