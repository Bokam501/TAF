package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProcessorDAO;
import com.hcl.atf.taf.model.Processor;


@Repository
public class ProcessorDAOImpl implements ProcessorDAO {
	private static final Log log = LogFactory.getLog(ProcessorDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<Processor> list() {
		List<Processor> processorList = new ArrayList<Processor>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Processor.class, "process");		
			if(c.list() != null){
				processorList = c.list();	
			}			
			log.debug("list Processor successful");
		}catch (RuntimeException re) {
			log.error("list Processor failed", re);
		}
		return processorList;	
	}
	
	@Override
	@Transactional
	public void add(Processor processor) {
		log.debug("adding Processor instance");
		try {
			sessionFactory.getCurrentSession().save(processor);
			log.debug("Processor - add successful");
		} catch (RuntimeException re) {
			log.error("Processor - add failed", re);
		}				
	}

	@Override
	@Transactional
	public Processor getProcessorByProcessorName(String processorName) {
		Processor processor = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Processor.class, "process");
			c.add(Restrictions.eq("process.processorName", processorName));
			List list = c.list();		
			processor=(list!=null && list.size()!=0)?(Processor)list.get(0):null;			
			log.debug("Processor by Name successful");
		} catch (RuntimeException re) {
			log.error("Processor by Name failed", re);
		}
		return processor;
	}
	@Override
	@Transactional
	public Processor getProcessorByProcessorId(Integer processorId) {
		Processor processor = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Processor.class, "process");
			c.add(Restrictions.eq("process.processorId", processorId));
			List list = c.list();		
			processor=(list!=null && list.size()!=0)?(Processor)list.get(0):null;			
			log.debug("Processor by Id successful");
		} catch (RuntimeException re) {
			log.error("Processor by Id failed", re);
		}
		return processor;
	}

}
