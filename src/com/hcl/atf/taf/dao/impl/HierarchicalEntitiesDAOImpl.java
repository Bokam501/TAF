package com.hcl.atf.taf.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.HierarchicalEntitiesDAO;
import com.hcl.atf.taf.service.HierarchicalEntitiesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;


@Repository
public class HierarchicalEntitiesDAOImpl implements HierarchicalEntitiesDAO {
	
	private static final Log log = LogFactory.getLog(HierarchicalEntitiesDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(String tableName, int parentRightIndex) {
		
        try {
			int newLeftIndex = parentRightIndex;
	        int newRightIndex = newLeftIndex + 1;
	        final String strRightIndex = "rightIndex";
	        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex + 2 WHERE rightIndex >= :rightIndex and leftIndex < :rightIndex";
	        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
	        fetchQuery.setParameter(strRightIndex, parentRightIndex);
	        fetchQuery.executeUpdate();
	        
	        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex + 2, rightIndex = rightIndex + 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
	        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
	        fetchQuery.setParameter(strRightIndex, parentRightIndex);
	        fetchQuery.executeUpdate();
			log.debug("updateHierarchyIndexForNew successful");
		} catch (RuntimeException re) {
			log.error("updateHierarchyIndexForNew failed", re);
        }
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex) {
		
		try {
			final String strRightIndex = "rightIndex";
	        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex < :rightIndex";
	        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
	        fetchQuery.setParameter(strRightIndex, rightIndex);
	        fetchQuery.executeUpdate();
	        
	        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex - 2, rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
	        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
	        fetchQuery.setParameter(strRightIndex, rightIndex);
	        fetchQuery.executeUpdate();
			log.debug("updateHierarchyIndexForDelete successful");
		} catch (RuntimeException re) {
			log.error("updateHierarchyIndexForDelete failed", re);
	    }
	}
}
