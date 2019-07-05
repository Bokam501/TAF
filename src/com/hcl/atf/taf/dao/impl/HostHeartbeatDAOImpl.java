package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.model.HostHeartbeat;

@Repository
public class HostHeartbeatDAOImpl implements HostHeartbeatDAO {
	private static final Log log = LogFactory.getLog(HostHeartbeatDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public void update(HostHeartbeat hostHeartbeat) {
		log.debug("updating HostHeartbeat instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(hostHeartbeat);
			 log.debug("Updated Host : " + hostHeartbeat.getHostId());
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
    
	
	
	@Override
	@Transactional
	public List<HostHeartbeat> list() {
		log.debug("listing all HostList instance");
		List<HostHeartbeat> hostHeartbeats=null;
		try {
			hostHeartbeats=sessionFactory.getCurrentSession().createQuery("from HostHeartbeat").list();
			if (hostHeartbeats != null) {
				for (HostHeartbeat heartBeat : hostHeartbeats) {
				 log.info("Host : " + heartBeat.getHostId());
				}
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return hostHeartbeats;
	}
	
	@Override
	@Transactional
	public List<HostHeartbeat> listExpiredHost(long time) {
		log.debug("listing all expired HostList instance");
		List<HostHeartbeat> hostHeartbeats=null;
		try {
			
			hostHeartbeats=sessionFactory.getCurrentSession().createQuery("from HostHeartbeat h where lastHeartPulseReceivedTime<:time").setParameter("time", time).list();
			if (hostHeartbeats != null) {
				for (HostHeartbeat heartBeat : hostHeartbeats) {
				}
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return hostHeartbeats;
	}
	

	
	
	
	@Override
	@Transactional
	public HostHeartbeat getByHostId(int hostId){
		log.debug("getting HostHeartbeat instance by id");
		HostHeartbeat hostHeartbeat=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from HostHeartbeat h where hostId=:hostId").setParameter("hostId",hostId).list(); 
			hostHeartbeat=(list!=null && list.size()!=0)?(HostHeartbeat) list.get(0):null;
			log.debug("Got Heartbeat for host : " + hostId);
		} catch (RuntimeException re) {
			log.error("getByHostId failed", re);
		}
		return hostHeartbeat;
        
	}
	
	
	@Override
	@Transactional
	public void clearAll() {
		log.info("clear all");		
		try {
			sessionFactory.getCurrentSession().createQuery("delete from HostHeartbeat")
					.executeUpdate();
			log.info("Deleted All Heartbeats for Hosts.");
			log.debug("clear all successful");
		} catch (RuntimeException re) {
			log.error("clear all failed", re);
		}
		
	}

	@Override
	@Transactional
	public void add(HostHeartbeat hostHeartbeat) {
		log.debug("adding HostHeartbeat instance");
		try {
			sessionFactory.getCurrentSession().save(hostHeartbeat);
			log.info("Added Heartbeat for Host : " + hostHeartbeat.getHostId());
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void delete(HostHeartbeat hostHeartbeat) {
		log.debug("Deleting HostHeartbeat instance");
		try {
			sessionFactory.getCurrentSession().delete(hostHeartbeat);
			log.info("Deleted Heartbeat for Host : " + hostHeartbeat.getHostId());
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
	}	
}
