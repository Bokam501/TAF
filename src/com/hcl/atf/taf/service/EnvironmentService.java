package com.hcl.atf.taf.service;

import java.util.List;
import java.util.Set;

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


public interface EnvironmentService {
	List<EnvironmentGroup> getEnvironmentGroupList();
	void addEnvironmentGroup(EnvironmentGroup environmentGroup);
	void updateEnvironmentGroup(EnvironmentGroup environmentGroupfromUI);
	void updateEnvironmentCategory(EnvironmentCategory environmentCategory);
	
	List<EnvironmentCategory> getEnvironmentCategoryListByGroup(Integer environmentGroupId);
	List<EnvironmentCategory> getParentEnvironmentCategoryList(Integer environmentGroupId,Integer environmentCategoryId);
	EnvironmentGroup getEnvironmentGroupById(Integer environmentGroupId);
	EnvironmentCategory getEnvironmentCategoryById(Integer parentEnvironmentCategoryId);
	void addEnvironmentCategory(EnvironmentCategory environmentCategory);
	List<Environment>  getMappedEnvironments(Integer workPackageId);
	
	Environment getEnvironmentByEnvId(Integer environmentId);
	EnvironmentCombination addEnvironmentCombination(EnvironmentCombination environmentCombination);
	List<EnvironmentCombination>  listEnvironmentCombination(Integer productId);
	List<EnvironmentCombination>  listEnvironmentCombinationByStatus(Integer productId, int status);
	void mapEnvironmentCombinationWithEnvironment(Integer environment_combination_id, Integer environmentId,String string);
	void updateEnvironmentCombination(EnvironmentCombination environmentCombination);
	void deleteEnvironmentCombination(EnvironmentCombination environmentCombination);
	EnvironmentCombination getEnvironmentCombinationById(Integer environmentCombinationId);

	List<RunConfiguration> listRunConfiguration(Integer productId);
	RunConfiguration addRunConfiguration(RunConfiguration runConfiguration);
	void updateRunConfiguration(RunConfiguration runConfiguration);
	List<GenericDevices> listGenericDevices(int productId);
	List<GenericDevices> listGenericDevicesByProductId(int productId);
	void addGenericDevice(GenericDevices genericDevices);
	GenericDevices getGenericDevicesById(int genericsDevicesId);
	Set<RunConfiguration> listRunConfigurationByProductVersionByEC(Integer productversionId,Integer environmentCombinationId,Integer testRunPlanId,Integer workpackageId);
	GenericDevices getGenericDevices(Integer genericDeviceId);
	void deleteRunConfiguration(RunConfiguration runcfg);
	RunConfiguration getRunConfiguration(int runcfgId);
	SCMSystem getSourceManagementSystem(int productId, int scmSystemId);
	GenericDevices getGenericDevices(String UDID);
	void updateGenericDevice(GenericDevices deviceList);
	List<GenericDevices> getGenericDevicesByHostId(Integer hostId);
	List<GenericDevices> getGenericDevicesByHostIdAndDeviceTypeId(Integer hostId, Integer deviceTypeId);
	List<TestRunJob> listByHostId(Integer hostId, Integer status);
	Integer getAverageTestRunExecutionTime(TestRunJob testRunJob);
	TestRunJob getTestRunJobById(Integer testRunJobId);
	void updateTestRunJob(TestRunJob testRunJob);
	void addTestRunJob(TestRunJob testRunJob);
	List<GenericDevices> listGenericDevices(Integer deviceLabId,int filter, int deviceTypeId);
	List<HostList> listHost(Integer deviceLabId,Integer filter);
	TestRunJob getTestRunJobByStatus(Integer testRunJobId,Integer status);

    void deleteTestRunJob(Integer workpackageId,Integer runConfigId,Integer testSuiteId);
	TestRunJob getTestRunJob(Integer testRunJobId);
	RunConfiguration isRunConfigurationOfDeviceOfTestRunExisting(Integer environmentCombinationId, Integer testRunPlanId,Integer deviceId, Integer versionId);
	RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId);
	List<JsonRunConfiguration> getMappedHostListFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId,Integer runConfigStatus);	
	List<JsonRunConfiguration> getMappedHostListFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId,Integer runConfigStatus);
	List<EnvironmentCombination> getEnvironmentsCombinationsMappedToActivity(Integer activityId,Integer activityWorkpkgId);
	int getProductTypeByTestRunPlanId(Integer testRunPlanId);
	EnvironmentCombination listEnvironmentCombinationByNameNoIntialize(Integer productId, String enviCombinationName);
	Integer getTotalJobsOfWPByStatus(Integer workpackageId, Integer testRunPlanId, Integer testRunStatus);
	EnvironmentCategory getRootEnvironmentCategory();
	List<EnvironmentCategory> listChildEnvCategoryNodesInHierarchyinLayers(EnvironmentCategory environmentCategory);
	void updateHierarchyIndexForNew(String tableName, int parentRightIndex);
	void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex);
	List<EnvironmentCombination> getEnvCombinationsForOtherEnvGroups(Integer productId, Integer environmentGroupId);
	void createEnvironmentCombinationsForNewEnvironment(ProductMaster product, Environment environment, Integer environmentGroupId);
	EnvironmentCombination cloneEnvironmentCombination(EnvironmentCombination existingEnvironmentCombination,Environment newEnvironment);
	
	List<Object[]> getEnvironmentDetailsByEnvironmentCombinationId(Integer environmentCombinationId);
	List<JsonRunConfiguration> getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId, Integer runConfigStatus);
	EnvironmentCategory getEnvironmentCategoryByName(String envName);
	List<EnvironmentCategory> getEnvironmentCategoryListByGroup();
	RunConfiguration isRunConfigurationAlreadyExist(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId, Integer deviceId, Integer versionId, Integer productTypeId);
}
