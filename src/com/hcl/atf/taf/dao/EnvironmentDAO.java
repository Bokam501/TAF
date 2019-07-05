package com.hcl.atf.taf.dao;

import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;


public interface EnvironmentDAO  {	 
	
	List<EnvironmentGroup> getEnvironmentGroupList();
	void addEnvironmentGroup(EnvironmentGroup environmentGroup);
	void updateEnvironmentGroup(EnvironmentGroup environmentGroupfromUI);
	List<EnvironmentCategory> getEnvironmentCategoryListByGroup(Integer environmentGroupId);
	List<EnvironmentCategory> getParentEnvironmentCategoryList(Integer environmentGroupId,Integer environmentCategoryId);
	Integer getIndexValue(Integer environmentCategoryId,String indexPosition);
	EnvironmentGroup getEnvironmentGroupById(Integer environmentGroupId);
	EnvironmentCategory getEnvironmentCategoryById(Integer parentEnvironmentCategoryId);
	void addEnvironmentCategory(EnvironmentCategory environmentCategory);

	List<Environment>  getMappedEnvironments(Integer workPackageId);
	Environment getEnvironmentByEnvId(Integer environmentId);
	EnvironmentCombination addEnvironmentCombination(EnvironmentCombination environmentCombination);
	List<EnvironmentCombination>  listEnvironmentCombination(Integer productId);
	List<EnvironmentCombination>  listEnvironmentCombinationByStatus(Integer productId, int status);
	void mapEnvironmentCombinationWithEnvironment(Integer environment_combination_id, Integer environmentId,String string);
	EnvironmentCombination getEnvironmentCombinationByEnvCombId(Integer environment_combination_id);
	public void updateEnvironmentCombination(EnvironmentCombination environmentCombination);
	void deleteEnvironmentCombination(EnvironmentCombination environmentCombination);
	EnvironmentCombination getEnvironmentCombinationById(Integer environmentCombinationId);
	void updateRunConfiguration(RunConfiguration runConfiguration);
	void addGenericDevice(GenericDevices genericDevices);
	List<GenericDevices> listGenericDevices(int productId);
	List<RunConfiguration> listRunConfiguration(Integer productId);
	RunConfiguration addRunConfiguration(RunConfiguration runConfiguration);
	Set<RunConfiguration> listRunConfigurationByProductVersionByEC(Integer productversionId,Integer environmentCombinationId,Integer testRunPlanId,Integer workpackageId);
	RunConfiguration getRunConfiguration(Integer  runConfigurationId);	
	SCMSystem getscmConfiguration(Integer  productId,Integer scmSystemId);
	GenericDevices getGenericDevicesById(int genericsDevicesId);
	GenericDevices getGenericDevices(Integer genericDeviceId);
	void deleteRunConfiguration(RunConfiguration runcfg);
	List<RunConfiguration> getRunConfigurationByProductVersion(Integer productVersionId) ;
	void updateEnvironmentCategory(EnvironmentCategory environmentCategory);
	GenericDevices getGenericDevices(String UDID);
	void updateGenericDevice(GenericDevices deviceList);
	List<GenericDevices> getGenericDevicesByHostId(Integer hostId);
	List<TestRunJob> listByHostId(Integer hostId, Integer status);
	Integer getAverageTestRunExecutionTime(TestRunJob testRunJob);
	TestRunJob getTestRunJobById(Integer testRunJobId);
	void update(TestRunJob testRunJob);
	boolean hasTestRunCompleted(TestRunJob testRunJob) ;
	void addTestRunJob(TestRunJob testRunJob);
	List<GenericDevices> listGenericDevices(Integer deviceLabId,int filter, int deviceTypeId);

	List<HostList> listHost(Integer deviceLabId,Integer filter);

	TestRunJob getTestRunJobByStatus(Integer testRunJobId,Integer status);

    void deleteTestRunJob(Integer workpackageId,Integer runConfigId,Integer testSuiteId);
	List<RunConfiguration> listRunConfigurationBywpId(Integer workPackageId);
	List<GenericDevices> listGenericDevicesByProductId(int productId);
	List<EnvironmentCategory> getChildEnvCategoriesByParentCategoryId(
			Integer parentEnvironmentCategoryId);
	TestRunJob getTestRunJob(Integer testRunJobId);
	List<EnvironmentCombination> listEnvironmentCombinationByStatusNoIntialize(
			Integer productId, int enviCombinationstatus);
	RunConfiguration isRunConfigurationOfDeviceOfTestRunExisting(Integer environmentCombinationId, Integer testRunPlanId,
			Integer deviceId, Integer versionId);
	List<JsonRunConfiguration> getMappedHostListFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId,Integer runConfigStatus);
	List<JsonRunConfiguration> getMappedHostListFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId,Integer runConfigStatus);
	List<EnvironmentCombination> getEnvironmentsCombinationsMappedToActivity(Integer activityId,Integer workPackageId);
	int getProductTypeByTestRunPlanId(Integer testRunPlanId);
	EnvironmentCombination listEnvironmentCombinationByNameNoIntialize(Integer productId, String enviCombinationName);
	List<GenericDevices> getGenericDevicesByHostIdAndDeviceTypeId(Integer hostId, Integer deviceTypeId);
	Integer getTotalJobsOfWPByStatus(Integer workpackageId, Integer testRunPlanId, Integer testRunStatus, List<Integer> testRunStatuses);
	List<EnvironmentCategory> listEnvCategoryByStatus(Integer status);
	EnvironmentCategory getEnvironmentCategoryByName(String environmentCategoryName);
	EnvironmentCategory getRootEnvironmentCategory();
	List<EnvironmentCategory> listChildEnvCategoryNodesInHierarchyinLayers(EnvironmentCategory environmentCategory);
	void updateHierarchyIndexForNew(String tableName, int parentRightIndex);
	void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex);
	List<EnvironmentCombination> getEnvCombinationsForOtherEnvGroups(Integer productId, Integer environmentGroupId);
	
	 List<Object[]> getEnvironmentDetailsByEnvironmentCombinationId(Integer environmentCombinationId);
	List<JsonRunConfiguration> getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus);
	RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId);
	List<EnvironmentCategory> getEnvironmentCategoryListByGroup();
	RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId,Integer productTypeId);
}
