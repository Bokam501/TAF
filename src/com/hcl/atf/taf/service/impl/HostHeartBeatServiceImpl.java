package com.hcl.atf.taf.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.ClientReponseMessage;
import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.dao.impl.WorkPackageDAOImpl;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.service.HostHeartBeatService;

@Service
public class HostHeartBeatServiceImpl implements HostHeartBeatService {
	

	private static final Log log = LogFactory.getLog(HostHeartBeatServiceImpl.class);

	@Autowired
    private HostHeartbeatDAO hostHeartbeatDAO;

	@Override
	@Transactional
	public void add(HostHeartbeat hostHeartbeat) {
		hostHeartbeatDAO.add(hostHeartbeat);
		
	}

	@Override
	@Transactional
	public void update(HostHeartbeat hostHeartbeat) {
		hostHeartbeatDAO.update(hostHeartbeat);
		
	}

	@Override
	@Transactional
	public void delete(int hostId) {
		HostHeartbeat heartBeat = hostHeartbeatDAO.getByHostId(hostId);
		if(heartBeat != null)
			hostHeartbeatDAO.delete(heartBeat);		
	}

	@Override
	@Transactional
	public List<HostHeartbeat> list() {
		
		return hostHeartbeatDAO.list();
	}

	@Override
	@Transactional
	public HostHeartbeat getByHostId(int hostId) {
		
		return hostHeartbeatDAO.getByHostId(hostId);
	}

	@Override
	@PostConstruct
	public void clearAll() {
		hostHeartbeatDAO.clearAll();
		
	}

	@Override
	@Transactional
	public List<HostHeartbeat> listExpiredHost(long timeInMillisecs) {
		return hostHeartbeatDAO.listExpiredHost(timeInMillisecs);
	}

	@Override
	@Transactional
	public boolean setHostResponseToHeartbeatAsJobsAvailable(Integer hostId) {

		return setHostResponseToHeartbeat(hostId, true, (short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
	}

	@Override
	@Transactional
	public boolean setHostResponseToHeartbeat(Integer hostId, boolean response, Short responseCode) {

		try {
			HostHeartbeat host = hostHeartbeatDAO.getByHostId(hostId);
			host.setHasResponse(response);
			host.setResponseToSend(responseCode);
			hostHeartbeatDAO.update(host);
			return true;
		} catch (Exception e) {
	        log.error("Problem in updating Host Response Status for host : " + hostId, e);            
	    	return false;
		}
	}

}
