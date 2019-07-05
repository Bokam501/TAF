package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.SkillDAO;
import com.hcl.atf.taf.dao.utilities.HibernateCustomOrderByForHierarchicalEntities;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.dto.SkillDTO;


@Repository
public class SkillDAOImpl implements SkillDAO {
	private static final Log log = LogFactory.getLog(SkillDAOImpl.class);
	
	private static final String ENTITY_TABLE_NAME = "skill";
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<Skill> list(int status) {		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		if(status <2){
			c.add(Restrictions.eq("skill.status", status));	
		}			
		List<Skill> skills = c.list();		
		for (Skill skill : skills) {
			Hibernate.initialize(skill.getParentSkill());			
		}
		return skills;
	}
		@Override
	@Transactional
	public List<Skill> listnoRoot(int status) {		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		if(status <2){
			c.add(Restrictions.eq("skill.status", status));	
		}	
		c.add(Restrictions.ne("skill.skillId", 1));	//Excluding Root Skill
		List<Skill> skills = c.list();		
		for (Skill skill : skills) {
			Hibernate.initialize(skill.getParentSkill());			
		}
		return skills;
	}
	
	@Override
	@Transactional
	public boolean isSkillExistingByName(String skillName) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		c.add(Restrictions.eq("skill.skillName", skillName));
		
		List<Skill> skills = c.list();		
		for (Skill skill : skills) {
			Hibernate.initialize(skill.getParentSkill());			
		}
		if (skills == null || skills.isEmpty()) 
		    return false;
		else 
			return true;
	}		

	@Override
	@Transactional
	public Skill getSkillByName(String skillName) {
		log.debug("listing specific Skill instance by Name");
		Skill skill=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
			c.add(Restrictions.eq("skill.skillName", skillName));
			
			List<Skill> skillList = c.list();	
			for (Skill skills : skillList) {
				Hibernate.initialize(skills.getParentSkill());			
			}
			skill=(skillList!=null && skillList.size()!=0)?(Skill)skillList.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return skill;
	}	
	
	@Override
	@Transactional
	public void add(Skill skill) {
		log.debug("adding Skill instance");
		try {
			//There will be only one root node 
			List<Skill> skills = list(1);
			Skill rootSK = null;
			if(skills == null){
				//Some problem in finding out Skill size. Will lead to inconsistent state
				//Abort adding.
				log.error("Add failed as could not find Skill objects");
				return;
			}else if(skills.size() <= 0){

				//There are no Features in the DB. Seed the main root node
				rootSK = new Skill();
				rootSK.setSkillName("Skills");
				rootSK.setSkillDescription("Root Skill");
				rootSK.setDisplayName("Skills");
				rootSK.setLeftIndex(1);
				rootSK.setRightIndex(2);
				rootSK.setStatus(1);
				sessionFactory.getCurrentSession().save(rootSK);
			}
			
			if(skill.getParentSkill() == null){
				//If no parent category is specified, make the root category the parent category
				rootSK = getSkillByName("Skills");
				//TODO : Get the SK without parent. This will be the root SK. There will be only one root SK,
				//one without a parent SK
				skill.setParentSkill(rootSK);
			}
			
			Skill parentSK = skill.getParentSkill();
			if(parentSK != null){
				int leftIndex = parentSK.getRightIndex();
				int rightIndex = parentSK.getRightIndex() + 1;
				updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentSK.getRightIndex());			
				skill.setLeftIndex(leftIndex);
				skill.setRightIndex(rightIndex);
				skill.setStatus(1);
				sessionFactory.getCurrentSession().save(skill);	
			}
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}
	
	public void updateHierarchyIndexForNew(String tableName, int parentRightIndex) {
		
        int leftIndex = parentRightIndex;
        int rightIndex = leftIndex + 1;
        final String strRightIndex = "rightIndex";
        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex + 2 WHERE rightIndex >= :rightIndex and leftIndex < :rightIndex";
        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, parentRightIndex);
        fetchQuery.executeUpdate();
        
        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex + 2, rightIndex = rightIndex + 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, parentRightIndex);
        fetchQuery.executeUpdate();
	}

	public void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex) {
		
        final String strRightIndex = "rightIndex";
        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex < :rightIndex";
        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, rightIndex);
        fetchQuery.executeUpdate();
        
        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex - 2, rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, rightIndex);
        fetchQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void delete(Skill skill) {
		log.debug("updating Skill instance");
		try {			
			//Delete only if the SK has no child categories
			if (skill.getChildCategories() == null || skill.getChildCategories().size() <= 0) {
				
				updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, skill.getLeftIndex(), skill.getRightIndex());
				sessionFactory.getCurrentSession().delete(skill);
			} else {				
				log.info("Delete not allowed as the SK has child SK. Delete them first");
			}
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}		
	}
	
	@Override
	@Transactional
	public Skill getBySkillId(int skillId) {

		log.debug("getting Skill instance by id");
		Skill skill=null;
		List<Skill> skillList = new ArrayList<Skill>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
			c.add(Restrictions.eq("skill.skillId", skillId));
			
			skillList = c.list();
			skill=(skillList!=null && skillList.size()!=0)?(Skill)skillList.get(0):null;
			
			log.debug("getBySkillId successful");
		} catch (RuntimeException re) {
			log.error("getBySkillId failed", re);
			//throw re;
		}
		return skill;
	}
	
	@Override
	@Transactional
	public void update(Skill skill) {
		log.debug("updating Skill instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(skill);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	public SkillDTO getSkillwithMaxRightIndex() {
		log.debug("getting Skill with Max RightIndex id");
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{
		DetachedCriteria maxId = DetachedCriteria.forClass(Skill.class)
			    .setProjection(Projections.max("rightIndex") );
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));
		c.setProjection(projectionList);
	            
		List<Object[]> list = c.add(Property.forName("skill.rightIndex").eq(maxId)).list();	
		for (Object[] row : list) {
			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);
		}
		
		
		log.info("Skill Max Right Index"+skilldto.getSkillName());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skilldto;
	}

	@Override
	@Transactional
	public SkillDTO getSkillwithMinRightIndex() {
		log.debug("getting Skill with min RightIndex id");
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{
		DetachedCriteria maxId = DetachedCriteria.forClass(Skill.class)
			    .setProjection(Projections.min("rightIndex") );			
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));
		c.setProjection(projectionList);
	            
		List<Object[]> list = c.add(Property.forName("skill.rightIndex").eq(maxId)).list();

		for (Object[] row : list) {			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);
		}		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skilldto;
	}

	@Override
	@Transactional
	public SkillDTO getSkillwithMaxLeftIndex() {
		log.debug("getting Skill with Max LeftIndex id");
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{
		DetachedCriteria maxId = DetachedCriteria.forClass(Skill.class)
			    .setProjection(Projections.max("leftIndex") );			
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));
		c.setProjection(projectionList);
	            
		List<Object[]> list = c.add(Property.forName("skill.rightIndex").eq(maxId)).list();

		for (Object[] row : list) {			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);
		}		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skilldto;
	}

	@Override
	@Transactional
	public SkillDTO getSkillwithMinLeftIndex() {
		log.debug("getting Skill with Min LeftIndex id");
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{
		DetachedCriteria maxId = DetachedCriteria.forClass(Skill.class)
			    .setProjection(Projections.min("leftIndex") );			
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));
		c.setProjection(projectionList);
	            
		List<Object[]> list = c.add(Property.forName("skill.rightIndex").eq(maxId)).list();

		for (Object[] row : list) {			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);
		}		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skilldto;
	}

	@Override
	@Transactional
	//Child 
	public List<SkillDTO> getDescendantSkills(int parentRightIndex,
			int ParentLeftIndex) {
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{		
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");		
		c.add(Restrictions.lt("skill.rightIndex", parentRightIndex));
		c.add(Restrictions.gt("skill.leftIndex", ParentLeftIndex));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));

		c.setProjection(projectionList);
	            
		List<Object[]> list = c.list();
		for (Object[] row : list) {
			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);
			skillDTOList.add(skilldto);
		}		
		
		log.info("Child list:----"+skillDTOList.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skillDTOList;
	}

	@Override
	@Transactional
	public List<SkillDTO> getAscendantSkills(int parentRightIndex, int ParentLeftIndex) {
		log.debug("getting Ancestors of Skill ");
		Skill skill=null;
		SkillDTO skilldto = new SkillDTO();
		List<Skill> skillList = new ArrayList<Skill>();
		List<SkillDTO> skillDTOList = new ArrayList<SkillDTO>();
		try{		
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		c.add(Restrictions.gt("skill.rightIndex", parentRightIndex));
		c.add(Restrictions.lt("skill.leftIndex", ParentLeftIndex));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("skill.skillId").as("ID"));
		projectionList.add(Property.forName("skill.skillName").as("Name"));
		projectionList.add(Property.forName("skill.rightIndex").as("RIndex"));
		projectionList.add(Property.forName("skill.leftIndex").as("LIndex"));

		c.setProjection(projectionList);
	            
		List<Object[]> list = c.list();
		for (Object[] row : list) {			
			skilldto.setSkillId((Integer)row[0]);
			skilldto.setSkillName((String)row[1]);
			skilldto.setRightIndex((Integer)row[2]);
			skilldto.setLeftIndex((Integer)row[3]);		
			skillDTOList.add(skilldto);
		}		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return skillDTOList;
	}

	@Override
	@Transactional
	public Skill incrementChildIndices(int skillId, int incrementingRightIndex, int incrementingLeftIndex) {
		log.info("Incrmenting child ");
		List<Skill> skillList = null;
		Skill skillFromDB = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
		
			c.add(Restrictions.eq("skill.skillId", skillId));		
			skillList = c.list();
			skillFromDB=(skillList!=null && skillList.size()!=0)?(Skill)skillList.get(0):null;
		 
			int rightIndexFromDB = skillFromDB.getRightIndex();
			int leftIndexFromDB = skillFromDB.getLeftIndex();
			skillFromDB.setRightIndex(rightIndexFromDB+incrementingRightIndex);
			skillFromDB.setLeftIndex(leftIndexFromDB+incrementingLeftIndex);
			log.info("Incremented Indices for :"+skillId +", R Index "+skillFromDB.getRightIndex() +", -L Index - "+skillFromDB.getLeftIndex());
			sessionFactory.getCurrentSession().saveOrUpdate(skillFromDB);
			log.debug("add successful");
		}catch(Exception e){
			e.printStackTrace();
		}		
		return skillFromDB;
	}

	@Override
	@Transactional
	public List<Skill> getSkillstoIncrementRightIndicesofAncestors(int currentSkillRightIndex, int currentSkillLeftIndex) {
		List<Skill> skillList = new ArrayList<Skill>();
	try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");	
		c.add(Restrictions.ge("skill.rightIndex", currentSkillRightIndex));
		c.add(Restrictions.lt("skill.leftIndex", currentSkillRightIndex));
		skillList = c.list();
	}catch(Exception e){
		e.printStackTrace();
	}	 
		return skillList;
	}

	@Override
	public List<Skill> getSkillstoIncrementLeftnRighttIndices(int currentSkillRightIndex, int currentSkillLeftIndex) {
		List<Skill> skillList = new ArrayList<Skill>();
	try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");	
		c.add(Restrictions.gt("skill.rightIndex", currentSkillRightIndex));
		c.add(Restrictions.ge("skill.leftIndex", currentSkillRightIndex));
		skillList = c.list();
	}catch(Exception e){
		e.printStackTrace();
	}	 
		return skillList;
	}	
	
	@Override
	@Transactional
	public Integer getRootUserSkillId(String rootUserSkillsDescription) {
		Skill skill = null;
		List<Skill> skillList = new ArrayList<Skill>();
		Integer rootSkillId = 0;
		try{
			Query q = sessionFactory.getCurrentSession().createQuery(
					"from "
					+ "Skill where skillDescription = :rootUserSkillsDescription").setParameter("rootUserSkillsDescription", rootUserSkillsDescription);
			skillList = q.list();
			skill = (skillList != null && skillList.size() != 0)?(Skill)skillList.get(0):null; 
				if(skill != null){
					rootSkillId = skill.getSkillId();
				}
		}catch(RuntimeException re){
			log.error("getRootUserSkillId failed", re);
		}
		return rootSkillId;
	}
	
	@Override
	@Transactional
	public List<Skill> getSkillListByRootId(
			Integer jtStartIndex, Integer jtPageSize, Integer rootSkillId, boolean isInitializationReq) {
		List<Skill> skills = null;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
			c.add(Restrictions.eq("skill.parentSkill.skillId",rootSkillId));
			c.add(Restrictions.eq("skill.status", 1));
			if(jtStartIndex != null && jtPageSize != null && jtStartIndex != -1 && jtPageSize != -1)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.asc("skill.leftIndex"));
			skills = c.list();
			
			if (isInitializationReq) {
				if(!(skills == null || skills.isEmpty())){
					for(Skill skill : skills){
						if(skill.getParentSkill() != null){
							Hibernate.initialize(skill.getParentSkill());
						}			
						Hibernate.initialize(skill.getChildCategories());											
					}
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return skills;
	}
	
	@Override
	@Transactional
	public List<Skill> getChildSkillListByParentSkillId(Integer parentSkillId) {

		log.debug("listing all Skills");
		List<Skill> skills = null;
		try{
			skills=sessionFactory.getCurrentSession().createQuery("from Skill s where s.parentSkill.skillId=:parentSkillId and status=1 order by s.leftIndex asc")
					.setParameter("parentSkillId", parentSkillId).list();	
			if(!(skills == null || skills.isEmpty())){
				for(Skill skill : skills){
					if(skill.getParentSkill() != null){
						Hibernate.initialize(skill.getParentSkill());
					}			
					Hibernate.initialize(skill.getChildCategories());					
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return skills;
	}
	
	@Override
	@Transactional
	public List<Skill> listChildNodesInHierarchyinLayers(Skill skill) {
		log.debug("listing all child nodes in hierarchy");
		List<Skill> skills=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Skill.class, "skill");
			c.add(Restrictions.gt("leftIndex", skill.getLeftIndex()));
			c.add(Restrictions.lt("rightIndex", skill.getRightIndex()));
			
			c.addOrder(HibernateCustomOrderByForHierarchicalEntities.sqlFormula("(rightIndex-leftIndex) desc"));
			c.addOrder(Order.asc("leftIndex"));
			skills = c.list();	
			
			log.debug("listing child nodes in hierarchy successful");
		} catch (RuntimeException re) {
			log.error("listing child nodes in hierarchy failed", re);
		}
		return skills;
	}
	
	@Override
	@Transactional
	public Skill getRootSkill() {
		log.debug("Getting Root Skill instance");
		Skill rootSkill = null;
		try {

			rootSkill = getSkillByName("Skills");
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return rootSkill;
	}
	
}
