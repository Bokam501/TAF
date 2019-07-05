package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.HostStatus;
import com.hcl.atf.taf.dao.HostListDAO;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.json.JsonHostList;

@Repository
public class HostListDAOImpl implements HostListDAO {
	private static final Log log = LogFactory.getLog(HostListDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void delete(HostList hostList) {
		log.debug("deleting HostList instance");
		try {
			sessionFactory.getCurrentSession().delete(hostList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public void add(HostList hostList) {
		log.debug("adding HostList instance");
		try {
			sessionFactory.getCurrentSession().save(hostList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
				
	}

	@Override
	@Transactional
	public void update(HostList hostList) {
		log.debug("updating HostList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(hostList);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
    
	
	
	@Override
	@Transactional
	public List<HostList> list() {
		log.debug("listing all HostList instance");
		List<HostList> hostList=null;
		try {
			String TERMINAL ="TERMINAL";
			hostList=sessionFactory.getCurrentSession().createQuery("from HostList hs where hs.hostTypeMaster.hostType =:TERMINAL")
					.setParameter("TERMINAL", TERMINAL)
					.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return hostList;
	}
	
	@Override
	@Transactional
    public List<HostList> list(int startIndex, int pageSize) {
		log.debug("listing HostList instance");
		List<HostList> hostList=null;
		try {
			if(startIndex!=-1 && pageSize!=-1){
			hostList=sessionFactory.getCurrentSession().createQuery("from HostList hs order by hs.commonActiveStatusMaster.status,hs.hostId asc")
					.setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			}else{
				hostList=sessionFactory.getCurrentSession().createQuery("from HostList hs order by hs.commonActiveStatusMaster.status,hs.hostId asc")
				                .list();
						
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return hostList;       
    }
	
	@Override
	@Transactional
    public List<HostList> listByHostIP(String hostIP) {
		log.debug("listing  HostList instance by host ip");
		List<HostList> hostList=null;
		try {			
			
			hostList=sessionFactory.getCurrentSession().createQuery("from HostList h where hostIpAddress=:hostIpAddress")
					.setParameter("hostIpAddress",hostIP)
	                .list();
			
			log.debug("list successful by host ip");
		} catch (RuntimeException re) {
			log.error("list failed by host ip", re);
		}
		return hostList;       
    }
	
	@Override
	@Transactional
	public List<HostList> listByHostName(String hostName) {
		log.debug("listing  HostList instance by host name");
		List<HostList> hostList=null;
		try {			
			
			hostList=sessionFactory.getCurrentSession().createQuery("from HostList h where hostName=:hostName")
					.setParameter("hostName",hostName)
	                .list();
			if (hostList != null) {
				for (HostList hl : hostList) {
					Hibernate.initialize(hl.getCommonActiveStatusMaster());
					Hibernate.initialize(hl.getHostTypeMaster());
					Hibernate.initialize(hl.getHostPlatformMaster());
				}
			}
			
			
			log.debug("list successful by host name");
		} catch (RuntimeException re) {
			log.error("list failed by host name", re);
		}
		return hostList;       
	}

	
	@Override
	@Transactional
	public HostList getByHostId(int hostId){
		log.debug("getting HostList instance by id");
		HostList hostList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from HostList h where hostId=:hostId").setParameter("hostId",hostId).list(); 
			hostList=(list!=null && list.size()!=0)?(HostList) list.get(0):null;
			if (hostList != null) {
				Hibernate.initialize(hostList.getCommonActiveStatusMaster());
				Hibernate.initialize(hostList.getDeviceLists());
			}
			log.debug("getByHostId successful");
		} catch (RuntimeException re) {
			log.error("getByHostId failed", re);
		}
		return hostList;
        
	}
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting HostList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from host_list").uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}

	@Override
	@Transactional
	public void resetHostsStatus() {
		log.debug("reset all Host status to INACTIVE");		
		try {
			sessionFactory.getCurrentSession().createQuery("update HostList set hostStatus=:inactive where hostStatus=:active")
					.setParameter("inactive", HostStatus.INACTIVE.toString()).setParameter("active",HostStatus.ACTIVE.toString()).executeUpdate();
			log.debug("reset all Host status to INACTIVE successful");
		} catch (RuntimeException re) {
			log.error("reset all Host status to INACTIVE failed", re);
		}
		
	}

	@Override
	@Transactional
	public void resetHostsStatus(int hostId) {
		log.debug("reset all Host status to ACTIVE");		
		try {
			sessionFactory.getCurrentSession().createQuery("update HostList set hostStatus=:active where hostId=:hostId")
					.setParameter("active", HostStatus.ACTIVE.toString()).setParameter("hostId",hostId).executeUpdate();
			log.debug("reset all Host status to ACTIVE successful");
		} catch (RuntimeException re) {
			log.error("reset all Host status to ACTIVE failed", re);
		}
		
	}
	
	@Override
	@Transactional
	public boolean isHostExistingByName(String hostName) {
		try {
		String hql = "from HostList ul where hostName =:hostName";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("hostName", hostName)
					.list();
		if (list != null  && !list.isEmpty()) 
		    return true;
		else 
			return false;
		}catch(RuntimeException re) {
			log.error("Error in isHostExistingByName",re);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId) {
		log.info("Get UnMappedHostListOfProductfromRunConfiguration");
		List<Object[]> hostListsobj = new ArrayList<Object[]>();
		List<JsonHostList> jsonHostLists= new ArrayList<JsonHostList>();
		List<Object[]> runconfigObjList = new ArrayList<Object[]>();
		Integer testRunPlanId = 0;
		String sql = "";
		try {			
			//Check if WP was created through TestRunPlan
			sql = "select wp.testRunPlanId from workpackage wp where workPackageId=:wpid";
			Object tRPlanIdObject = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workpackageId).uniqueResult();
			if(tRPlanIdObject != null){
				testRunPlanId = ((Number)tRPlanIdObject).intValue();
			}
			//if created through TRPlan, then fetch Mapped Host from WPRunconfig(WorkpackageRunConfiguration) table
			if(testRunPlanId != null && testRunPlanId != 0){
				//Check if Host were already Mapped To WorkPackage(At WorkpackageRunConfiguration).				
				runconfigObjList = sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc join rc.workPackageRunConfigSet whr "+
						"  where whr.workpackage.workPackageId=:wpid and rc.runconfigId=whr.runconfiguration.runconfigId and "+
						 "rc.runConfigStatus=:rconfigStat and rc.hostList.hostId is not null and "+
						"rc.environmentcombination.environment_combination_id =:envcombid group by whr.runconfiguration.runconfigId order by whr.runconfiguration.runconfigId")
						.setParameter("wpid", workpackageId)
						.setParameter("envcombid", ecId)						
						.setParameter("rconfigStat",runConfigStatus )
						.list();
				if(runconfigObjList != null || !runconfigObjList.isEmpty() || runconfigObjList.size() != 0 ){
					//Few Hosts were already mapped to WP through TRPlan, then (Product has host - Mapped Host)
					hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
							+ "hl.hostStatus, hl.hostType, hl.hostNickName"
							+ " from host_list hl inner join product_has_host phh "
							+ "on hl.hostId=phh.hostId and phh.productId=:pid where phh.hostId not in"
							+ " (select rf.hostId from runconfiguration rf  join workpackage_has_runconfiguration whr "
							+ "where whr.workPackageId=:wpid and  rf.runconfigId=whr.runconfigurationId and "
							+ "rf.runConfigStatus=:rconfigStat and rf.hostId is not null and "
							+ "rf.environmentcombinationId=:envcombid group by whr.runconfigurationId order by whr.runconfigurationId)")
							//+ " (select rf.hostId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.hostId !=:nullobject)")
							.setParameter("pid", productId)
							.setParameter("envcombid", ecId)
							.setParameter("wpid", workpackageId)
							.setParameter("rconfigStat", runConfigStatus)
							.list();
				}else if(runconfigObjList == null || runconfigObjList.isEmpty() || runconfigObjList.size() == 0){
					//No Hosts were mapped to WorkPackage so far// So fetch Host from Product has Host
					hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
							+ "hl.hostStatus, hl.hostType, hl.hostNickName"
							+ " from host_list hl inner join product_has_host phh "
							+ "on hl.hostId=phh.hostId and phh.productId=:pid")						
							.setParameter("pid", productId)
							.list();
				}
			}else{//if WP was created manually and not through TestRunPlan
				//Check if Host were already Mapped To WorkPackage(At RunConfiguration).
				hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select rf.hostId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.hostId !=:nullobject")
						.setParameter("envcombId", ecId)
						.setParameter("wpId", workpackageId)
						.setParameter("rcStatus", runConfigStatus)
						.setParameter("nullobject", "NULL")
						.list();
				
				if(hostListsobj != null || !hostListsobj.isEmpty() || hostListsobj.size() != 0 ){
					//Few Hosts were already mapped to WorkPackage
					hostListsobj = null;
					hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
							+ "hl.hostStatus, hl.hostType, hl.hostNickName"
							+ " from host_list hl inner join product_has_host phh "
							+ "on hl.hostId=phh.hostId and phh.productId=:pid where phh.hostId not in"
							+ " (select rf.hostId from runconfiguration rf where environmentcombinationId=:envcombId and workpackageId=:wpId and runConfigStatus =:rcStatus and rf.hostId !=:nullobject)")
							.setParameter("pid", productId)
							.setParameter("envcombId", ecId)
							.setParameter("wpId", workpackageId)
							.setParameter("rcStatus", runConfigStatus)						
							.setParameter("nullobject", "NULL")
							.list();
				}else if(hostListsobj == null || hostListsobj.isEmpty() || hostListsobj.size() == 0){
					//No Hosts were mapped to WorkPackage so far
					hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
							+ "hl.hostStatus, hl.hostType, hl.hostNickName"
							+ " from host_list hl inner join product_has_host phh "
							+ "on hl.hostId=phh.hostId and phh.productId=:pid")						
							.setParameter("pid", productId)
							.list();
				}
			}
		
			for (Object hostobj[] : hostListsobj) {
				JsonHostList jsonHL = new JsonHostList();
				if(hostobj[0] != null){
					Integer hostId = (Integer)hostobj[0];
					jsonHL.setHostId(hostId);
				}
				if(hostobj[1] != null){
					String hostName = (String)hostobj[1];
					jsonHL.setHostName(hostName);
				}
				if(hostobj[2] != null){
					String hostIpAddress = (String)hostobj[2];
					jsonHL.setHostIpAddress(hostIpAddress);
				}
				if(hostobj[3] != null){
					String hostPlatform = (String)hostobj[3];
					jsonHL.setHostPlatform(hostPlatform);
				}
				if(hostobj[4] != null){
					String hostStatus = (String)hostobj[4];
					jsonHL.setHostStatus(hostStatus);
				}
				if(hostobj[5] != null){
					String hostType = (String)hostobj[5];
					jsonHL.setHostType(hostType);
				}
							
				jsonHostLists.add(jsonHL);
			}
			
			return jsonHostLists;
		} catch (HibernateException e1) {
			log.error("ERROR UnMapped HostList Of Product from RunConfiguration  ",e1);
		}
		return jsonHostLists;
	}
	
	@Override
	@Transactional
	public List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId) {
		log.info("Get UnMappedHostListOfProductfromRunConfiguration");
		List<Object[]> hostListsobj = new ArrayList<Object[]>();
		List<JsonHostList> jsonHostLists= new ArrayList<JsonHostList>();
		try {
			
			//Check if Host were already Mapped To TestRunPlan(At RunConfiguration).			
			hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select rf.hostId from runconfiguration rf where environmentcombinationId=:envcombId and testRunPlanId=:trplanId and runConfigStatus =:rcStatus and rf.hostId !=:nullobject")
					.setParameter("envcombId", ecId)
					.setParameter("trplanId", testRunPlanId)
					.setParameter("rcStatus", runConfigStatus)
					.setParameter("nullobject", "NULL")
					.list();
			
			if(hostListsobj != null || !hostListsobj.isEmpty() || hostListsobj.size() != 0 ){
				//Few Hosts were already mapped to TestRunPlan
				hostListsobj = null;
				hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
						+ "hl.hostStatus, hl.hostType, hl.hostNickName"
						+ " from host_list hl inner join product_has_host phh "
						+ "on hl.hostId=phh.hostId and phh.productId=:pid where phh.hostId not in"
						+ " (select rf.hostId from runconfiguration rf where environmentcombinationId=:envcombId and testRunPlanId=:trplanId and runConfigStatus =:rcStatus and rf.hostId !=:nullobject)")
						.setParameter("pid", productId)
						.setParameter("envcombId", ecId)
						.setParameter("trplanId", testRunPlanId)
						.setParameter("rcStatus", runConfigStatus)
						.setParameter("nullobject", "NULL")
						.list();
			}else if(hostListsobj == null || hostListsobj.isEmpty() || hostListsobj.size() == 0){
				//No Hosts were mapped to TestRunPlan so far
				hostListsobj = sessionFactory.getCurrentSession().createSQLQuery("select hl.hostId, hl.hostName, hl.hostIpAddress, hl.hostPlatform, "
						+ "hl.hostStatus, hl.hostType, hl.hostNickName"
						+ " from host_list hl inner join product_has_host phh "
						+ "on hl.hostId=phh.hostId and phh.productId=:pid")						
						.setParameter("pid", productId)
						.list();
			}			
			if(hostListsobj != null || !hostListsobj.isEmpty() || hostListsobj.size() != 0){
				for (Object hostobj[] : hostListsobj) {
					JsonHostList jsonHL = new JsonHostList();
					if(hostobj[0] != null){
						Integer hostId = (Integer)hostobj[0];
						jsonHL.setHostId(hostId);
					}
					if(hostobj[1] != null){
						String hostName = (String)hostobj[1];
						jsonHL.setHostName(hostName);
					}
					if(hostobj[2] != null){
						String hostIpAddress = (String)hostobj[2];
						jsonHL.setHostIpAddress(hostIpAddress);
					}
					if(hostobj[3] != null){
						String hostPlatform = (String)hostobj[3];
						jsonHL.setHostPlatform(hostPlatform);
					}
					if(hostobj[4] != null){
						String hostStatus = (String)hostobj[4];
						jsonHL.setHostStatus(hostStatus);
					}
					if(hostobj[5] != null){
						String hostType = (String)hostobj[5];
						jsonHL.setHostType(hostType);
					}
					jsonHostLists.add(jsonHL);
				}	
			}
					
			return jsonHostLists;
		} catch (HibernateException e1) {
			log.error("ERROR fetching UnMapped HostList Of Product from RunConfiguration TestRunPlanLevel ",e1);
		}
		return jsonHostLists;
	}
	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfHostOfWorkPackageExisting(Integer environmentCombinationId, Integer workpackageId, Integer hostId) {
		log.debug("isRunConfigurationOfHostOfWorkPackageExisting");
		RunConfiguration runconfig = null;
		List<RunConfiguration> runconfigList = null;
		try {
			runconfigList=sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
					+ " rc.workPackage.workPackageId=:wpId and rc.hostList.hostId=:hostID")
					.setParameter("ecid",environmentCombinationId )
					.setParameter("wpId", workpackageId)
					.setParameter("hostID",hostId ).list();					
			runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList.get(0) : null;
			if (runconfig != null) {
				Hibernate.initialize(runconfig.getTestRunPlanSet());
				Hibernate.initialize(runconfig.getEnvironmentcombination());				
				if (runconfig.getHostList() != null) {
					HostList hl = runconfig.getHostList();
					Hibernate.initialize(hl);					
					if(hl.getCommonActiveStatusMaster() != null){
						CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
						Hibernate.initialize(casm);
					}
					if(hl.getHostTypeMaster() != null){
						HostTypeMaster htm = hl.getHostTypeMaster();
						Hibernate.initialize(htm);
					}
					if(hl.getHostPlatformMaster() != null){
						HostPlatformMaster hpfm = hl.getHostPlatformMaster();
						Hibernate.initialize(hpfm);
					}					
				}
				
			}
			return runconfig;
		} catch (RuntimeException re) {
			log.error("isRunConfigurationOfHostOfWorkPackageExisting Error ", re);
		}		
		return runconfig;
	}
	
	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfHostOfTestRunPlanExisting(Integer environmentCombinationId, Integer testRunPlanId, Integer hostId) {
		log.debug("isRunConfigurationOfHostOfTestRunPlanExisting");
		RunConfiguration runconfig = null;
		List<RunConfiguration> runconfigList = null;
		try {
			runconfigList=sessionFactory.getCurrentSession().createQuery("from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
					+ " rc.testRunPlan.testRunPlanId=:trPlanId and rc.hostList.hostId=:hostID")
					.setParameter("ecid",environmentCombinationId )
					.setParameter("trPlanId", testRunPlanId)
					.setParameter("hostID",hostId ).list();					
			runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList.get(0) : null;
			if (runconfig != null) {
				Hibernate.initialize(runconfig.getTestRunPlanSet());
				Hibernate.initialize(runconfig.getEnvironmentcombination());
				Hibernate.initialize(runconfig.getHostList());
				if (runconfig.getHostList() != null) {
					HostList hl = runconfig.getHostList();
					Hibernate.initialize(hl);					
					if(hl.getCommonActiveStatusMaster() != null){
						CommonActiveStatusMaster casm = hl.getCommonActiveStatusMaster();
						Hibernate.initialize(casm);
					}
					if(hl.getHostTypeMaster() != null){
						HostTypeMaster htm = hl.getHostTypeMaster();
						Hibernate.initialize(htm);
					}
					if(hl.getHostPlatformMaster() != null){
						HostPlatformMaster hpfm = hl.getHostPlatformMaster();
						Hibernate.initialize(hpfm);
					}					
				}
				//TestRunPlan not needed, as it is WorkPackageLevel
				if(runconfig.getTestRunPlan() != null){
						TestRunPlan trunplan = runconfig.getTestRunPlan();
						Hibernate.initialize(trunplan);					
						if(trunplan.getProductVersionListMaster() != null){
							ProductVersionListMaster version = trunplan.getProductVersionListMaster();
							Hibernate.initialize(version);						
							if(version.getProductMaster() != null){
								ProductMaster prod = version.getProductMaster();
								Hibernate.initialize(prod);
								if(prod.getProductType() != null){
									ProductType ptype = prod.getProductType();
									Hibernate.initialize(ptype);								
								}
							}
						}				
				}
			}
			return runconfig;
		} catch (RuntimeException re) {
			log.error("isRunConfigurationOfHostOfTestRunPlanExisting Error ", re);
		}		
		return runconfig;
	}
	
	@Override
	@Transactional
	public List<HostList> listHostIdByStatus(String status) {
		log.debug("listing all HostList instance");
		List<HostList> hostList=null;
		try {
			hostList=sessionFactory.getCurrentSession().createQuery("from HostList hs where hs.commonActiveStatusMaster.status=:status")
					.setParameter("status", status.toUpperCase())
					.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return hostList;
	}	
}