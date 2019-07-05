package com.hcl.atf.taf.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EnvironmentService;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

	private static final Log log = LogFactory
			.getLog(EnvironmentServiceImpl.class);

	@Autowired
	private EnvironmentDAO environmentDAO;
	@Autowired
	private EmailService emailService;
	
	@Override
	@Transactional
	public List<EnvironmentGroup> getEnvironmentGroupList() {
		return environmentDAO.getEnvironmentGroupList();
	}

	@Override
	@Transactional
	public void addEnvironmentGroup(EnvironmentGroup environmentGroup) {
		// TODO Auto-generated method stub
		environmentDAO.addEnvironmentGroup(environmentGroup);
	}

	@Override
	@Transactional
	public void updateEnvironmentGroup(EnvironmentGroup environmentGroupfromUI) {
		environmentDAO.updateEnvironmentGroup(environmentGroupfromUI);
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryListByGroup(
			Integer environmentGroupId) {
		return environmentDAO.getEnvironmentCategoryListByGroup(environmentGroupId);
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getParentEnvironmentCategoryList(
			Integer environmentGroupId,Integer environmentCategoryId) {
		return environmentDAO.getParentEnvironmentCategoryList(environmentGroupId,environmentCategoryId);
	}

	@Override
	@Transactional
	public EnvironmentGroup getEnvironmentGroupById(Integer environmentGroupId) {
		return environmentDAO.getEnvironmentGroupById(environmentGroupId);
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryById(
			Integer parentEnvironmentCategoryId) {
		return environmentDAO.getEnvironmentCategoryById(parentEnvironmentCategoryId);
	}

	@Override
	@Transactional
	public void addEnvironmentCategory(EnvironmentCategory environmentCategory) {
		environmentDAO.addEnvironmentCategory(environmentCategory);
	}

	@Override
	@Transactional
	public List<Environment> getMappedEnvironments(Integer workPackageId) {
		// TODO Auto-generated method stub
		return environmentDAO.getMappedEnvironments(workPackageId);
	}

	@Override
	@Transactional
	public Environment getEnvironmentByEnvId(Integer environmentId) {
		return environmentDAO.getEnvironmentByEnvId(environmentId);
	}

	@Override
	@Transactional
	public EnvironmentCombination addEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		return environmentDAO.addEnvironmentCombination(environmentCombination);
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> listEnvironmentCombinationByStatus(
			Integer productId, int status) {
		return environmentDAO.listEnvironmentCombinationByStatus(productId,status);
	}

	@Override
	@Transactional
	public void mapEnvironmentCombinationWithEnvironment(Integer environment_combination_id, Integer environmentId,String string) {
		environmentDAO.mapEnvironmentCombinationWithEnvironment(environment_combination_id,environmentId,string);
	}

	@Override
	@Transactional
	public void updateEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		environmentDAO.updateEnvironmentCombination(environmentCombination);
		
	}
	@Override
	@Transactional
	public void deleteEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		environmentDAO.deleteEnvironmentCombination(environmentCombination);
		
	}

	@Override
	@Transactional
	public EnvironmentCombination getEnvironmentCombinationById(
			Integer environmentCombinationId) {
		return environmentDAO.getEnvironmentCombinationById(environmentCombinationId);
	}

	@Override
	@Transactional
	public List<RunConfiguration> listRunConfiguration(Integer productId) {
		return environmentDAO.listRunConfiguration(productId);
	}

	@Override
	@Transactional
	public Integer getTotalJobsOfWPByStatus(Integer workpackageId,Integer testRunPlanId, Integer testRunStatus){
		return environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, testRunStatus, null);				
	}
	
	@Override
	@Transactional
	public RunConfiguration addRunConfiguration(RunConfiguration runConfiguration) {
		return environmentDAO.addRunConfiguration(runConfiguration);
	}
	

	@Override
	@Transactional
	public void updateRunConfiguration(RunConfiguration runConfiguration) {
		environmentDAO.updateRunConfiguration(runConfiguration);
	}
	
	@Override
	@Transactional
	public List<GenericDevices> listGenericDevices(int productId) {
		return environmentDAO.listGenericDevices(productId);
	}
	
	@Override	
	@Transactional
	public List<GenericDevices> listGenericDevicesByProductId(int productId) {
		return environmentDAO.listGenericDevicesByProductId(productId);
	}
	
	@Override
	@Transactional
	public void addGenericDevice(GenericDevices genericDevices) {
		environmentDAO.addGenericDevice(genericDevices);		
	}

	@Override
	@Transactional
	public GenericDevices getGenericDevicesById(int genericsDevicesId) {
		return environmentDAO.getGenericDevicesById(genericsDevicesId);
	}
	
	@Override
	@Transactional
	public Set<RunConfiguration> listRunConfigurationByProductVersionByEC(
			Integer productversionId, Integer environmentCombinationId,Integer testRunPlanId,Integer workpackageId) {
		return environmentDAO.listRunConfigurationByProductVersionByEC(productversionId, environmentCombinationId,testRunPlanId,workpackageId);
	}
	@Override
	@Transactional
	public GenericDevices getGenericDevices(Integer genericDeviceId) {
		return environmentDAO.getGenericDevices(genericDeviceId);
	}

	@Override
	@Transactional
	public void deleteRunConfiguration(RunConfiguration runcfg) {
		environmentDAO.deleteRunConfiguration(runcfg);
		
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfiguration(int runcfgId) {
		return environmentDAO.getRunConfiguration(runcfgId);
	}
	
	@Override
	@Transactional
	public SCMSystem getSourceManagementSystem(int productId,int scmSystemId) {
		return environmentDAO.getscmConfiguration(productId,scmSystemId);
	}
	
	

	@Override
	@Transactional
	public void updateEnvironmentCategory(
			EnvironmentCategory environmentCategory) {
		environmentDAO.updateEnvironmentCategory(environmentCategory);
	}

	@Override
	@Transactional
	public GenericDevices getGenericDevices(String UDID) {
		return environmentDAO.getGenericDevices(UDID);
	}

	@Override
	@Transactional
	public void updateGenericDevice(GenericDevices deviceList) {
		environmentDAO.updateGenericDevice(deviceList);
	}

	@Override
	@Transactional
	public List<GenericDevices> getGenericDevicesByHostId(Integer hostId) {
		return environmentDAO.getGenericDevicesByHostId(hostId);
	}
	
	@Override
	@Transactional
	public List<GenericDevices> getGenericDevicesByHostIdAndDeviceTypeId(Integer hostId, Integer deviceTypeId) {
		return environmentDAO.getGenericDevicesByHostIdAndDeviceTypeId(hostId, deviceTypeId);
	}

	@Override
	@Transactional
	public List<TestRunJob> listByHostId(Integer hostId, Integer status) {
		return environmentDAO.listByHostId(hostId, status);
	}

	@Override
	@Transactional
	public Integer getAverageTestRunExecutionTime(TestRunJob testRunJob) {
		return environmentDAO.getAverageTestRunExecutionTime(testRunJob);
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobById(Integer testRunJobId) {
		return environmentDAO.getTestRunJobById(testRunJobId);
	}
	
	@Override
	@Transactional
	public void updateTestRunJob(TestRunJob testRunJob) {
		environmentDAO.update(testRunJob);
		if(testRunJob.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			 for(StackTraceElement st : stack){
				 log.info("Method is called from " + st.getClassName() +" -> " + st.getLineNumber()+" -> "+st.getMethodName());
			 }
			 if(testRunJob.getTestRunEvidenceStatus()!=null && !testRunJob.getTestRunEvidenceStatus().isEmpty()){
				log.info("Is Evidence recieved at Server:"+testRunJob.getTestRunEvidenceStatus()+"--");
				log.info("Test Run Job has been completed. Initiating report dispatch by EMail.");
				emailService.sendTestRunPlanCompletionMail(testRunJob);
			 }
		}
		boolean testRunCompleted = environmentDAO.hasTestRunCompleted(testRunJob);
		if (!testRunCompleted) {
			log.info("Test Run Job has not completed yet. Waiting till Test Run is completed before sending mail");
			return;
		}			
	}

	@Override
	@Transactional
	public void addTestRunJob(TestRunJob testRunJob) {
		environmentDAO.addTestRunJob(testRunJob);
	}

	@Override
	@Transactional
	public List<GenericDevices> listGenericDevices(Integer deviceLabId,
			int filter, int deviceTypeId) {
		return environmentDAO.listGenericDevices(deviceLabId, filter, deviceTypeId);
	}

	@Override
	@Transactional
	public List<HostList> listHost(Integer deviceLabId, Integer filter) {
		return environmentDAO.listHost(deviceLabId, filter);
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobByStatus(Integer testRunJobId, Integer status) {
		return environmentDAO.getTestRunJobByStatus(testRunJobId, status);
	}

	@Override
	@Transactional
	public void deleteTestRunJob(Integer workpackageId, Integer runConfigId,
			Integer testSuiteId) {
		environmentDAO.deleteTestRunJob(workpackageId, runConfigId, testSuiteId);
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> listEnvironmentCombination(
			Integer productId) {
		return environmentDAO.listEnvironmentCombination(productId);
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJob(Integer testRunJobId) {
		return environmentDAO.getTestRunJob(testRunJobId);
	}

	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostListFromRunconfigurationWorkPackageLevel(
			Integer environmentCombinationId, Integer workPackageId,
			Integer runConfigStatus) {
		return environmentDAO.getMappedHostListFromRunconfigurationWorkPackageLevel(environmentCombinationId, workPackageId, runConfigStatus);
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfDeviceOfTestRunExisting(Integer environmentCombinationId, Integer testRunPlanId, Integer deviceId, Integer versionId) {
		return environmentDAO.isRunConfigurationOfDeviceOfTestRunExisting(environmentCombinationId, testRunPlanId, deviceId, versionId);
	}

	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostListFromRunconfigurationTestRunPlanLevel(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus) {
		return environmentDAO.getMappedHostListFromRunconfigurationTestRunPlanLevel(environmentCombinationId, testRunPlanId, runConfigStatus);
	}
	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus) {
		return environmentDAO.getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(environmentCombinationId, testRunPlanId, runConfigStatus);
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> getEnvironmentsCombinationsMappedToActivity(Integer activityId,Integer activityWorkPkgId) {
		return environmentDAO.getEnvironmentsCombinationsMappedToActivity(activityId,activityWorkPkgId);
	}

	@Override
	@Transactional
	public int getProductTypeByTestRunPlanId(Integer testRunPlanId){
		return environmentDAO.getProductTypeByTestRunPlanId(testRunPlanId);
	}

	@Override
	@Transactional
	public EnvironmentCombination listEnvironmentCombinationByNameNoIntialize(Integer productId, String enviCombinationName){
		return environmentDAO.listEnvironmentCombinationByNameNoIntialize(productId, enviCombinationName);
	}
	
	@Override
	@Transactional
	public EnvironmentCategory getRootEnvironmentCategory() {
		return environmentDAO.getRootEnvironmentCategory();
	}
	
	@Override
	@Transactional
	public List<EnvironmentCategory> listChildEnvCategoryNodesInHierarchyinLayers(EnvironmentCategory environmentCategory) {
		return environmentDAO.listChildEnvCategoryNodesInHierarchyinLayers(environmentCategory);
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(String tableName, int parentRightIndex) {
		 environmentDAO.updateHierarchyIndexForNew(tableName, parentRightIndex);
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex) {
		environmentDAO.updateHierarchyIndexForDelete(tableName, leftIndex, rightIndex);
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> getEnvCombinationsForOtherEnvGroups(Integer productId,  Integer environmentGroupId) {

		return environmentDAO.getEnvCombinationsForOtherEnvGroups(productId, environmentGroupId);
	}

	@Override
	@Transactional
	public EnvironmentCombination cloneEnvironmentCombination(EnvironmentCombination existingEnvironmentCombination, Environment newEnvironment) {

		//Create the cloned POJO env combination object
		EnvironmentCombination clonedEnvironmentCombination = new EnvironmentCombination();
		clonedEnvironmentCombination.setProductMaster(existingEnvironmentCombination.getProductMaster());
		String newEnvCombinationName = existingEnvironmentCombination.getEnvironmentCombinationName()+ "~" +newEnvironment.getEnvironmentName().trim();
		clonedEnvironmentCombination.setEnvironmentCombinationName(newEnvCombinationName);
		clonedEnvironmentCombination.setEnvionmentCombinationStatus(0); //Set Default Status as 0
		//Persist the cloned env combination POJO. This will give it an ID
		clonedEnvironmentCombination =	environmentDAO.addEnvironmentCombination(clonedEnvironmentCombination);
		//Copy the environments to the persisted clone env combination object
		//TODO : Is it possible to handle all these in one DB transaction
		Set<Environment> environments = existingEnvironmentCombination.getEnvironmentSet();
		for (Environment environment : environments) {
			
			//Map the existing environments to the cloned environment combination 
			mapEnvironmentCombinationWithEnvironment(clonedEnvironmentCombination.getEnvironment_combination_id(), environment.getEnvironmentId(),"Add");
		}
		//Map the new environments to the cloned environment combination 
		mapEnvironmentCombinationWithEnvironment(clonedEnvironmentCombination.getEnvironment_combination_id(), newEnvironment.getEnvironmentId(),"Add");
		log.info("Created environment combination : " + clonedEnvironmentCombination.getEnvironmentCombinationName());			
		return clonedEnvironmentCombination;
	}

	@Override
	@Transactional
	public void createEnvironmentCombinationsForNewEnvironment(ProductMaster product, Environment environment, Integer environmentGroupId) {

		//A new environment combination must be added for the new environment
		EnvironmentCombination environmentCombination = new EnvironmentCombination();
		environmentCombination.setProductMaster(product);
		environmentCombination.setEnvironmentCombinationName(environment.getEnvironmentName());
		environmentCombination.setEnvionmentCombinationStatus(0); // Default status
		environmentCombination = addEnvironmentCombination(environmentCombination);
		mapEnvironmentCombinationWithEnvironment(environmentCombination.getEnvironment_combination_id(),environment.getEnvironmentId(),"Add");
		log.info("Created default environment combination : " + environmentCombination.getEnvironmentCombinationName());			

		//If the environment is a standalone environment, do not create other environment combinations
		if (environment.getIsStandAloneEnvironment() == 1)
			return;
		
		//If not, create environment combinations based on the existing environment combinations
		List<EnvironmentCombination> existingEnvironmentCombinations = getEnvCombinationsForOtherEnvGroups(product.getProductId(), environmentGroupId);
		if (existingEnvironmentCombinations == null || existingEnvironmentCombinations.size() <= 0) {
			//Do nothing
			return;
		} else {
			for (EnvironmentCombination existingEnvironmentCombination : existingEnvironmentCombinations) {
				//Clone the existing environment combination
				cloneEnvironmentCombination(existingEnvironmentCombination, environment);
			}
		}
		return;
	}
	
	@Override
	@Transactional
	public List<Object[]> getEnvironmentDetailsByEnvironmentCombinationId(Integer environmentCombinationId) {
		List<Object[]> environmentDetails=null;
		try {
			return environmentDAO.getEnvironmentDetailsByEnvironmentCombinationId(environmentCombinationId);
		}catch(Exception e) {
			log.error("Error in Service getEnvironmentDetailsByEnvironmentCombinationId ",e);
		}
		
		return environmentDetails;
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryByName(String envName) {
		return environmentDAO.getEnvironmentCategoryByName(envName);
	}
	
	@Override
	@Transactional
	public RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId){
		return environmentDAO.isRunConfigurationAlreadyExist(environmentCombinationId, testRunPlanId, hostId, deviceId,versionId);
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryListByGroup() {
		return environmentDAO.getEnvironmentCategoryListByGroup();
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId,Integer productTypeId) {
		return environmentDAO.isRunConfigurationAlreadyExist(environmentCombinationId, testRunPlanId, hostId, deviceId,versionId,productTypeId);
	}
}
