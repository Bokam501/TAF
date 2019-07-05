package com.hcl.atf.taf.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.utilities.HibernateCustomOrderByForHierarchicalEntities;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;

@Repository
public class EnvironmentDAOImpl implements EnvironmentDAO {
	private static final Log log = LogFactory.getLog(EnvironmentDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductMasterDAO productMasterDAO;

	private static final String ENTITY_TABLE_NAME = "environment_category";

	@Override
	@Transactional
	public List<EnvironmentGroup> getEnvironmentGroupList() {
		log.info("listing getEnvironmentGroupList instance");
		List<EnvironmentGroup> environmentGroup = null;
		try {
			environmentGroup = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentGroup where status='1' order by displayName")
					.list();
			log.info("EnvironmentGroupList successful -"
					+ environmentGroup.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return environmentGroup;
	}

	@Override
	@Transactional
	public void addEnvironmentGroup(EnvironmentGroup environmentGroup) {
		log.info("adding environmentGroup instance");
		try {
			environmentGroup.setStatus(1);
			sessionFactory.getCurrentSession().save(environmentGroup);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateEnvironmentGroup(EnvironmentGroup environmentGroupfromUI) {
		log.info("adding updateEnvironmentGroup instance");
		try {
			sessionFactory.getCurrentSession().update(environmentGroupfromUI);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryListByGroup(
			Integer environmentGroupId) {
		List<EnvironmentCategory> environmentCategoryList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "ec");
			c.createAlias("ec.environmentGroup", "environmentGroup");
			c.add(Restrictions.eq("environmentGroup.environmentGroupId",
					environmentGroupId));
			c.add(Restrictions.eq("ec.status", 1));
			environmentCategoryList = c.list();

			if (environmentCategoryList != null
					&& !environmentCategoryList.isEmpty()) {
				for (EnvironmentCategory environmentCategory : environmentCategoryList) {
					Hibernate.initialize(environmentCategory
							.getEnvironmentGroup());
					Hibernate.initialize(environmentCategory
							.getParentEnvironmentCategory());
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return environmentCategoryList;
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getParentEnvironmentCategoryList(
			Integer environmentGroupId, Integer environmentCategoryId) {
		Integer leftIndex = null;
		Integer rightIndex = null;
		if (environmentCategoryId == 0) {
			leftIndex = getIndexValue(environmentCategoryId, "left");
			rightIndex = getIndexValue(environmentCategoryId, "right");
		}

		List<EnvironmentCategory> environmentCategoryList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "ec");
			c.createAlias("ec.environmentGroup", "environmentGroup");
			c.add(Restrictions.eq("environmentGroup.environmentGroupId",
					environmentGroupId));
			c.add(Restrictions.ne("ec.environmentCategoryId",
					environmentCategoryId));
			if (leftIndex != null && rightIndex != null) {
				c.add(Restrictions.lt("ec.leftIndex", leftIndex));
				c.add(Restrictions.gt("ec.rightIndex", rightIndex));
			}

			environmentCategoryList = c.list();

			if (environmentCategoryList != null
					&& !environmentCategoryList.isEmpty()) {
				for (EnvironmentCategory environmentCategory : environmentCategoryList) {
					Hibernate.initialize(environmentCategory
							.getEnvironmentGroup());
					Hibernate.initialize(environmentCategory
							.getParentEnvironmentCategory());
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return environmentCategoryList;
	}

	@Override
	@Transactional
	public Integer getIndexValue(Integer environmentCategoryId,
			String indexPosition) {
		EnvironmentCategory environmentCategory = null;
		List<EnvironmentCategory> environmentCategoryList = null;
		Integer indexValue = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "ec");
			c.add(Restrictions.eq("ec.environmentCategoryId",
					environmentCategoryId));
			environmentCategoryList = c.list();
			environmentCategory = (environmentCategoryList != null && environmentCategoryList
					.size() != 0) ? (EnvironmentCategory) environmentCategoryList
					.get(0) : null;
			if (environmentCategory != null) {
				if (indexPosition != null
						&& indexPosition.equalsIgnoreCase("left")) {
					indexValue = environmentCategory.getLeftIndex();
				} else if (indexPosition.equalsIgnoreCase("right")) {
					indexValue = environmentCategory.getRightIndex();
				}

			}

		} catch (Exception e) {
			log.error(e);
		}
		return indexValue;
	}

	@Override
	@Transactional
	public EnvironmentGroup getEnvironmentGroupById(Integer environmentGroupId) {
		log.info("getting getEnvironmentGroupById: " + environmentGroupId);
		EnvironmentGroup environmentGroup = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentGroup e where e.environmentGroupId=:environmentGroupId")
					.setParameter("environmentGroupId", environmentGroupId)
					.list();
			environmentGroup = (list != null && list.size() != 0) ? (EnvironmentGroup) list
					.get(0) : null;
			log.info("getEnvironmentGroupById successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentGroupById failed", re);
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return environmentGroup;
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryById(
			Integer parentEnvironmentCategoryId) {
		log.info("getting getEnvironmentCategoryById: "
				+ parentEnvironmentCategoryId);
		EnvironmentCategory environmentCategory = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentCategory e where e.environmentCategoryId=:environmentCategoryId")
					.setParameter("environmentCategoryId",
							parentEnvironmentCategoryId).list();
			environmentCategory = (list != null && list.size() != 0) ? (EnvironmentCategory) list
					.get(0) : null;
			if (environmentCategory != null) {
				Hibernate.initialize(environmentCategory.getEnvironmentGroup());
			}
			log.info("getEnvironmentCategoryById successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentCategoryById failed", re);
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return environmentCategory;
	}

	@Override
	@Transactional
	public void addEnvironmentCategory(EnvironmentCategory environmentCategory) {
		log.info("adding environmentGroup instance");
		List<EnvironmentCategory> listOfEnvironmentCategories = null;
		EnvironmentCategory rootEnv = null;
		try {
			listOfEnvironmentCategories = listEnvCategoryByStatus(1);
			if (listOfEnvironmentCategories == null) {
				log.error("Add failed as could not find category objects");
				return;
			} else if (listOfEnvironmentCategories.size() <= 0) {
				rootEnv = new EnvironmentCategory();
				rootEnv.setEnvironmentCategoryName("Default");
				rootEnv.setDescription("Default");
				rootEnv.setDisplayName("Default");
				rootEnv.setStatus(1);
				rootEnv.setLeftIndex(1);
				rootEnv.setRightIndex(2);
				sessionFactory.getCurrentSession().save(rootEnv);
			}
			if (environmentCategory.getParentEnvironmentCategory() == null) {
				rootEnv = getEnvironmentCategoryByName("Default");
				environmentCategory.setParentEnvironmentCategory(rootEnv);
			}
			EnvironmentCategory parentEnvCategory = environmentCategory
					.getParentEnvironmentCategory();
			if (parentEnvCategory != null) {
				updateHierarchyIndexForNew(ENTITY_TABLE_NAME,
						parentEnvCategory.getRightIndex());
				environmentCategory.setLeftIndex(parentEnvCategory
						.getRightIndex());
				environmentCategory.setRightIndex(parentEnvCategory
						.getRightIndex() + 1);
				environmentCategory.setStatus(1);
				sessionFactory.getCurrentSession().save(environmentCategory);
			}
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public List<Environment> getMappedEnvironments(Integer workPackageId) {
		log.info("getting workPackageId: " + workPackageId);
		List<Environment> environment = null;
		Set<Environment> envList = null;
		try {
			List<WorkPackage> workPackageList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackage w where w.workPackageId=:workPackageId")
					.setParameter("workPackageId", workPackageId).list();
			WorkPackage workPackage = (workPackageList != null && workPackageList
					.size() != 0) ? (WorkPackage) workPackageList.get(0) : null;
			envList = workPackage.getEnvironmentList();

			log.info("getEnvironmentCategoryById successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentCategoryById failed", re);
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return environment;
	}

	@Override
	@Transactional
	public Environment getEnvironmentByEnvId(Integer environmentId) {
		Environment env = null;
		try {
			List<Environment> environmentList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Environment e where e.environmentId=:environmentId")
					.setParameter("environmentId", environmentId).list();
			env = (environmentList != null && environmentList.size() != 0) ? (Environment) environmentList
					.get(0) : null;

			if (env != null) {
				Hibernate.initialize(env.getEnvironmentCategory());
				Hibernate.initialize(env.getEnvironmentCategory()
						.getEnvironmentGroup());
				Hibernate.initialize(env.getProductMaster());
			}
			log.info("getEnvironmentCategoryById successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentCategoryById failed", re);
			// throw re;
		}

		return env;
	}

	@Override
	@Transactional
	public EnvironmentCombination addEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		EnvironmentCombination envCom = new EnvironmentCombination();
		log.info("adding environmentGroup instance");
		try {
			sessionFactory.getCurrentSession().save(environmentCombination);
			List<EnvironmentCombination> environmentCombinations = listEnvironmentCombinationByStatus(
					0, 2);
			envCom = environmentCombinations
					.get(environmentCombinations.size() - 1);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return environmentCombination;
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> listEnvironmentCombinationByStatus(
			Integer productId, int enviCombinationstatus) {
		List<EnvironmentCombination> environmentCombinations = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.productMaster", "pm");
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if (enviCombinationstatus == 2) {
				c.add(Restrictions.in("ec.envionmentCombinationStatus",
						Arrays.asList(0, 1)));
			} else {
				c.add(Restrictions.eq("ec.envionmentCombinationStatus",
						enviCombinationstatus));
			}
			environmentCombinations = c.list();

			if (environmentCombinations != null
					&& !environmentCombinations.isEmpty()) {
				for (EnvironmentCombination environmentCombination : environmentCombinations) {
					Hibernate.initialize(environmentCombination
							.getEnvironmentSet());
					Hibernate.initialize(environmentCombination
							.getProductMaster());
					Hibernate.initialize(environmentCombination
							.getProductMaster().getProductType());
					if (environmentCombination.getEnvironmentSet() != null) {
						for (Environment env : environmentCombination
								.getEnvironmentSet()) {
							Hibernate.initialize(env.getEnvironmentCategory());
							Hibernate.initialize(env.getEnvironmentCategory()
									.getEnvironmentGroup());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return environmentCombinations;
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> listEnvironmentCombinationByStatusNoIntialize(
			Integer productId, int enviCombinationstatus) {
		List<EnvironmentCombination> environmentCombinations = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.productMaster", "pm");
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));
			}
			if (enviCombinationstatus == 2) {
				c.add(Restrictions.in("ec.envionmentCombinationStatus",
						Arrays.asList(0, 1)));
			} else {
				c.add(Restrictions.eq("ec.envionmentCombinationStatus",
						enviCombinationstatus));
			}
			environmentCombinations = c.list();

		} catch (Exception e) {
			log.error(e);
		}
		return environmentCombinations;
	}

	@Override
	@Transactional
	public void mapEnvironmentCombinationWithEnvironment(
			Integer environmentCombinationId, Integer environmentId,
			String action) {

		log.info("Map Env combination with env : " + environmentCombinationId
				+ " : " + environmentId + " : " + action);

		try {
			// Get the objects
			EnvironmentCombination envcombination = getEnvironmentCombinationByEnvCombId(environmentCombinationId);
			Environment environMent = getEnvironmentByEnvId(environmentId);
			if (envcombination == null || environMent == null) {
				// Do nothing
				log.info("Relevant Env Combination or Env objects could not be found");
				return;
			}

			if (action.equalsIgnoreCase("Add")) {
				// Check if the environment combination is already mapped to the
				// environment
				Set<Environment> envSet = envcombination.getEnvironmentSet();
				boolean isEnvAlreadyMapped = false;
				for (Environment alreadyMappedEnv : envSet) {

					if (alreadyMappedEnv.getEnvironmentId() == environmentId) {
						// The env is already mapped to the Env combination
						// Do nothing
						isEnvAlreadyMapped = true;
						break;
					}
				}
				if (isEnvAlreadyMapped) {
					// Do Nothing
					return;
				}

				// The env is not mapped to the environment combination
				// Add the mapping
				envcombination.getEnvironmentSet().add(environMent);
				environMent.getEnvironmentCombinationSet().add(envcombination);
				sessionFactory.getCurrentSession().saveOrUpdate(envcombination);
				sessionFactory.getCurrentSession().saveOrUpdate(environMent);
				log.info("Mapped environment to Env Combination successfully : "
						+ envcombination.getEnvironmentCombinationName()
						+ " : " + environMent.getEnvironmentName());
			} else if (action.equalsIgnoreCase("Remove")) {

				log.info("Remove Environmnet from Environmnet Combination");
				try {

					Set<Environment> environmentSet = envcombination
							.getEnvironmentSet();
					environmentSet.remove(environMent);
					envcombination.setEnvironmentSet(environmentSet);
					sessionFactory.getCurrentSession().save(envcombination);
					log.info("Removed Environment Combination from Environmnet Combination successfully");
				} catch (RuntimeException re) {
					log.error("Failed to remove workPackage from workpackage",
							re);
				}
			}
		} catch (RuntimeException re) {
			log.error("Mapping env combination to env failed : " + action, re);
		}
	}

	@Override
	@Transactional
	public EnvironmentCombination getEnvironmentCombinationByEnvCombId(
			Integer environment_combination_id) {
		EnvironmentCombination envComb = null;
		try {
			List<EnvironmentCombination> environmentCombList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentCombination envComb where envComb.environment_combination_id=:environment_combination_id")
					.setParameter("environment_combination_id",
							environment_combination_id).list();
			envComb = (environmentCombList != null && environmentCombList
					.size() != 0) ? (EnvironmentCombination) environmentCombList
					.get(0) : null;
			log.info("envComb ID is " + envComb.getEnvironment_combination_id());
			if (envComb != null) {
				Hibernate.initialize(envComb.getProductMaster());
				Hibernate.initialize(envComb.getEnvironmentSet());
				Hibernate.initialize(envComb.getTestCaseConfiguration());
			}
			log.info("getEnvironmentCategoryById successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentCategoryById failed", re);
			// throw re;
		}

		return envComb;
	}

	@Override
	@Transactional
	public void updateEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		log.info("update environmentGroup instance");
		try {

			sessionFactory.getCurrentSession().update(environmentCombination);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void deleteEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		log.info("deleting EnvironmentCombination instance");
		try {

			sessionFactory.getCurrentSession().delete(environmentCombination);

			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}

	}

	@Override
	@Transactional
	public EnvironmentCombination getEnvironmentCombinationById(
			Integer environmentCombinationId) {

		log.info("getting getEnvironmentCategoryById: "
				+ environmentCombinationId);
		EnvironmentCombination environmentCombination = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentCombination e where e.environment_combination_id=:environment_combination_id")
					.setParameter("environment_combination_id",
							environmentCombinationId).list();
			environmentCombination = (list != null && list.size() != 0) ? (EnvironmentCombination) list
					.get(0) : null;
			if (environmentCombination != null) {
				Hibernate
						.initialize(environmentCombination.getEnvironmentSet());
				Hibernate.initialize(environmentCombination.getProductMaster());
				Hibernate.initialize(environmentCombination.getProductMaster()
						.getProductType());
			}
			log.info("EnvironmentCombination successful");
		} catch (RuntimeException re) {
			log.error("EnvironmentCombination failed", re);
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return environmentCombination;
	}

	@Override
	@Transactional
	public List<RunConfiguration> listRunConfiguration(Integer productId) {
		List<RunConfiguration> runConfigurationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					RunConfiguration.class, "rc");
			c.createAlias("rc.product", "pm");
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));
			}
			c.add(Restrictions.eq("rc.runConfigStatus", 1));
			runConfigurationList = c.list();

			if (runConfigurationList != null && !runConfigurationList.isEmpty()) {
				for (RunConfiguration runConfiguration : runConfigurationList) {
					Hibernate.initialize(runConfiguration
							.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					Hibernate.initialize(runConfiguration.getGenericDevice()
							.getDeviceModelMaster());
					Hibernate.initialize(runConfiguration.getGenericDevice()
							.getDeviceLab());
					Hibernate.initialize(runConfiguration.getGenericDevice()
							.getPlatformType());
					Hibernate.initialize(runConfiguration.getProduct());
					GenericDevices devices = runConfiguration
							.getGenericDevice();
					if ((devices instanceof MobileType)
							&& ((MobileType) devices).getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) devices)
								.getDeviceMakeMaster());
					}
					if ((devices instanceof ServerType)) {
						if (((ServerType) devices).getProcessor() != null) {
							Hibernate.initialize(((ServerType) devices)
									.getProcessor());
							if (((ServerType) devices).getProcessor()
									.getSystemType() != null) {
								Hibernate.initialize(((ServerType) devices)
										.getProcessor().getSystemType());
							}
						}
						if (((ServerType) devices).getSystemType() != null) {
							Hibernate.initialize(((ServerType) devices)
									.getSystemType());
						}
					}

				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return runConfigurationList;
	}

	@Override
	@Transactional
	public RunConfiguration addRunConfiguration(
			RunConfiguration runConfiguration) {
		log.info("adding runConfiguration instance");
		try {
			runConfiguration.setRunConfigStatus(1);
			sessionFactory.getCurrentSession().save(runConfiguration);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return runConfiguration;
	}

	@Override
	@Transactional
	public void updateRunConfiguration(RunConfiguration runConfiguration) {
		log.info("update environmentGroup instance");
		try {
			sessionFactory.getCurrentSession().update(runConfiguration);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void addGenericDevice(GenericDevices genericDevices) {
		log.info("adding genericDevices instance");
		try {
			sessionFactory.getCurrentSession().save(genericDevices);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			// throw re;
		}
	}

	@Override
	@Transactional
	public List<GenericDevices> listGenericDevices(int productId) {
		List<GenericDevices> genericDevicesList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					GenericDevices.class, "gd");
			genericDevicesList = c.list();

			if (genericDevicesList != null && !genericDevicesList.isEmpty()) {
				for (GenericDevices genericDevices : genericDevicesList) {
					Hibernate.initialize(genericDevices.getDeviceModelMaster());
					Hibernate.initialize(genericDevices.getDeviceLab());
					Hibernate.initialize(genericDevices.getPlatformType());
					Hibernate.initialize(genericDevices.getProductMaster());
					Hibernate.initialize(genericDevices.getHostList());
					if ((genericDevices instanceof MobileType)
							&& ((MobileType) genericDevices)
									.getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) genericDevices)
								.getDeviceMakeMaster());
					}
					if ((genericDevices instanceof ServerType)) {
						if (((ServerType) genericDevices).getProcessor() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getProcessor());
						}
						if (((ServerType) genericDevices).getSystemType() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getSystemType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return genericDevicesList;
	}

	@Override
	@Transactional
	public List<GenericDevices> listGenericDevicesByProductId(int productId) {
		List<GenericDevices> genericDevicesList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					GenericDevices.class, "gd");
			if (productId != 0) {
				c.createAlias("gd.productMaster", "pm");
				c.add(Restrictions.eq("pm.productId", productId));
			}
			genericDevicesList = c.list();
			if (genericDevicesList != null && !genericDevicesList.isEmpty()) {
				for (GenericDevices genericDevices : genericDevicesList) {
					Hibernate.initialize(genericDevices.getGenericsDevicesId());
					Hibernate.initialize(genericDevices.getDeviceModelMaster());
					Hibernate.initialize(genericDevices.getDeviceLab());
					Hibernate.initialize(genericDevices.getPlatformType());
					Hibernate.initialize(genericDevices.getProductMaster());
					Hibernate.initialize(genericDevices.getHostList());
					if ((genericDevices instanceof MobileType)
							&& ((MobileType) genericDevices)
									.getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) genericDevices)
								.getDeviceMakeMaster());
					}
					if ((genericDevices instanceof ServerType)) {
						if (((ServerType) genericDevices).getProcessor() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getProcessor());
						}
						if (((ServerType) genericDevices).getSystemType() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getSystemType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return genericDevicesList;
	}

	@Override
	@Transactional
	public GenericDevices getGenericDevicesById(int genericsDevicesId) {
		List<GenericDevices> genericDevicesList = null;
		GenericDevices gd = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					GenericDevices.class, "gd");
			c.add(Restrictions.eq("gd.genericsDevicesId", genericsDevicesId));
			genericDevicesList = c.list();
			gd = (genericDevicesList != null && genericDevicesList.size() != 0) ? (GenericDevices) genericDevicesList
					.get(0) : null;
			if (genericDevicesList != null && !genericDevicesList.isEmpty()) {
				for (GenericDevices genericDevices : genericDevicesList) {
					Hibernate.initialize(genericDevices.getDeviceModelMaster());
					Hibernate.initialize(genericDevices.getDeviceLab());
					Hibernate.initialize(genericDevices.getPlatformType());
					Hibernate.initialize(genericDevices.getProductMaster());
					Hibernate.initialize(genericDevices.getHostList());

					if ((genericDevices instanceof MobileType)
							&& ((MobileType) genericDevices)
									.getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) genericDevices)
								.getDeviceMakeMaster());
					}
					if ((genericDevices instanceof ServerType)) {
						if (((ServerType) genericDevices).getProcessor() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getProcessor());
						}
						if (((ServerType) genericDevices).getSystemType() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getSystemType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return gd;
	}

	@Override
	@Transactional
	public Set<RunConfiguration> listRunConfigurationByProductVersionByEC(
			Integer productversionId, Integer environmentCombinationId,
			Integer testRunPlanId, Integer workpackageId) {
		Set<RunConfiguration> runConfigurationList = new HashSet<RunConfiguration>();
		List<RunConfiguration> runConfigurations = null;

		List<TestRunPlan> testRunPlanList = null;
		TestRunPlan testRunPlan = null;
		try {
			if (testRunPlanId != -1) {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(
						TestRunPlan.class, "trp");
				c.createAlias("trp.productVersionListMaster", "pv");

				if (productversionId != -1) {
					c.add(Restrictions.eq("pv.productVersionListId",
							productversionId));
				}

				c.add(Restrictions.eq("trp.testRunPlanId", testRunPlanId));
				testRunPlanList = c.list();
				testRunPlan = (testRunPlanList != null && testRunPlanList
						.size() != 0) ? (TestRunPlan) testRunPlanList.get(0)
						: null;

				if (testRunPlan != null) {
					Hibernate.initialize(testRunPlan.getExecutionType());
					Hibernate.initialize(testRunPlan
							.getProductVersionListMaster());
					if (testRunPlan.getProductVersionListMaster()
							.getProductMaster() != null) {
						Hibernate.initialize(testRunPlan
								.getProductVersionListMaster()
								.getProductMaster());
					}
					Hibernate.initialize(testRunPlan.getRunConfigurationList());
					Hibernate.initialize(testRunPlan.getTestSuiteLists());
					Set<RunConfiguration> rcset = testRunPlan
							.getRunConfigurationList();
					for (RunConfiguration rc : rcset) {
						if (rc.getEnvironmentcombination()
								.getEnvironment_combination_id()
								.equals(environmentCombinationId)) {
							Hibernate.initialize(rc.getGenericDevice());
							Hibernate
									.initialize(rc.getEnvironmentcombination());
							runConfigurationList.add(rc);
						}
					}
				}
			} else {
				List<WorkpackageRunConfiguration> wprcList = null;
				WorkpackageRunConfiguration wprc = null;
				Criteria c = sessionFactory.getCurrentSession().createCriteria(
						WorkpackageRunConfiguration.class, "wprunconfig");
				c.createAlias("wprunconfig.workpackage", "wp");
				c.createAlias("wprunconfig.runconfiguration", "rc");
				c.createAlias("rc.environmentcombination", "ec");

				if (workpackageId != -1) {
					c.add(Restrictions.eq("wp.workPackageId", workpackageId));
				}

				if (environmentCombinationId != -1) {
					c.add(Restrictions.eq("ec.environment_combination_id",
							environmentCombinationId));
				}
				wprcList = c.list();
				runConfigurationList.clear();
				if (wprcList != null && !wprcList.isEmpty()) {
					for (WorkpackageRunConfiguration wp : wprcList) {
						Hibernate.initialize(wp.getWorkpackage());
						Hibernate.initialize(wp.getRunconfiguration()
								.getEnvironmentcombination());
						if (wp.getRunconfiguration().getGenericDevice() != null) {
							Hibernate.initialize(wp.getRunconfiguration()
									.getGenericDevice());
							Hibernate.initialize(wp.getRunconfiguration()
									.getGenericDevice().getDeviceModelMaster());
							Hibernate.initialize(wp.getRunconfiguration()
									.getGenericDevice().getDeviceLab());
							Hibernate.initialize(wp.getRunconfiguration()
									.getGenericDevice().getPlatformType());
							GenericDevices gd = wp.getRunconfiguration()
									.getGenericDevice();
							if ((gd instanceof MobileType)
									&& ((MobileType) gd).getDeviceMakeMaster() != null) {
								Hibernate.initialize(((MobileType) gd)
										.getDeviceMakeMaster());
							}
							if ((gd instanceof ServerType)) {
								if (((ServerType) gd).getProcessor() != null) {
									Hibernate.initialize(((ServerType) gd)
											.getProcessor());
								}
								if (((ServerType) gd).getSystemType() != null) {
									Hibernate.initialize(((ServerType) gd)
											.getSystemType());
								}
							}
						}
						Hibernate.initialize(wp.getRunconfiguration()
								.getProduct());

						runConfigurationList.add(wp.getRunconfiguration());
					}

				}

			}

		} catch (Exception e) {
			log.error("Error ", e);
		}
		return runConfigurationList;
	}

	@Override
	@Transactional
	public int getProductTypeByTestRunPlanId(Integer testRunPlanId) {
		int productType = 0;
		String sql = "";
		try {
			sql = "select pm.productTypeId from product_master pm inner join "
					+ "product_version_list_master pvm on pm.productId=pvm.productId inner join "
					+ "test_run_plan trp on pvm.productVersionListId=trp.productVersionId and testRunPlanId=:trpId";
			productType = ((Number) (sessionFactory.getCurrentSession()
					.createSQLQuery(sql).setParameter("trpId", testRunPlanId)
					.uniqueResult())).intValue();
		} catch (RuntimeException re) {
			log.error(re);
		}
		return productType;
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfiguration(Integer runConfigurationId) {
		List<RunConfiguration> runConfigurationList = null;
		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					RunConfiguration.class, "runcfg");

			c.add(Restrictions.eq("runcfg.runconfigId", runConfigurationId));

			runConfigurationList = c.list();
			runConfiguration = (runConfigurationList != null && runConfigurationList
					.size() != 0) ? (RunConfiguration) runConfigurationList
					.get(0) : null;
			if (runConfiguration != null) {
				Hibernate.initialize(runConfiguration.getTestRunPlanSet());
				Hibernate.initialize(runConfiguration
						.getEnvironmentcombination());
				Hibernate.initialize(runConfiguration.getGenericDevice());
				Hibernate.initialize(runConfiguration.getTestSuiteLists());
				if (runConfiguration.getTestRunPlanSet() != null) {
					for (TestRunPlan testRunPlan : runConfiguration
							.getTestRunPlanSet()) {
						Hibernate.initialize(testRunPlan
								.getProductVersionListMaster());
						Hibernate.initialize(testRunPlan.getExecutionType());
					}
				}

				if (runConfiguration.getTestSuiteLists() != null) {
					for (TestSuiteList tsl : runConfiguration
							.getTestSuiteLists()) {
						if (tsl.getTestCaseLists() != null) {
							Hibernate.initialize(tsl.getTestCaseLists());
						}
					}
				}

			}
		} catch (Exception e) {
			log.error(e);
		}
		return runConfiguration;
	}

	@Override
	@Transactional
	public SCMSystem getscmConfiguration(Integer productId, Integer scmSystemId) {
		List<SCMSystem> scmConfigurationList = null;
		SCMSystem scmConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					SCMSystem.class, "scmcfgsys");
			c.createAlias("scmcfgsys.productMaster", "pm");
			c.add(Restrictions.eq("scmcfgsys.id", scmSystemId));
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));
			}
			scmConfigurationList = c.list();
			scmConfiguration = (scmConfigurationList != null && scmConfigurationList
					.size() != 0) ? (SCMSystem) scmConfigurationList.get(0)
					: null;
		} catch (Exception e) {
			log.error(e);
		}
		return scmConfiguration;
	}

	@Override
	@Transactional
	public GenericDevices getGenericDevices(Integer genericDeviceId) {
		List<GenericDevices> genericDevicesList = null;
		GenericDevices genericDevices = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					GenericDevices.class, "genricDevice");
			c.add(Restrictions.eq("genricDevice.genericsDevicesId",
					genericDeviceId));

			genericDevicesList = c.list();
			genericDevices = (genericDevicesList != null && genericDevicesList
					.size() != 0) ? (GenericDevices) genericDevicesList.get(0)
					: null;
			if (genericDevices != null) {
				Hibernate.initialize(genericDevices.getDeviceModelMaster());
				Hibernate.initialize(genericDevices.getProductMaster());
				Hibernate.initialize(genericDevices.getPlatformType());
				if (genericDevices.getHostList() != null) {
					HostList hl = genericDevices.getHostList();
					Hibernate.initialize(hl);
				}
				if (genericDevices.getDeviceModelMaster() != null) {
					Hibernate.initialize(genericDevices.getDeviceModelMaster()
							.getDeviceMakeMaster());
				}
				if ((genericDevices instanceof MobileType)
						&& ((MobileType) genericDevices).getDeviceMakeMaster() != null) {
					Hibernate.initialize(((MobileType) genericDevices)
							.getDeviceMakeMaster());
				}
				if ((genericDevices instanceof ServerType)) {
					if (((ServerType) genericDevices).getProcessor() != null) {
						Hibernate.initialize(((ServerType) genericDevices)
								.getProcessor());
					}
					if (((ServerType) genericDevices).getSystemType() != null) {
						Hibernate.initialize(((ServerType) genericDevices)
								.getSystemType());
					}
				}

			}
		} catch (Exception e) {
			log.error(e);
		}
		return genericDevices;
	}

	@Override
	@Transactional
	public GenericDevices getGenericDevices(String UDID) {
		List<GenericDevices> genericDevicesList = null;
		GenericDevices genericDevices = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					GenericDevices.class, "genricDevice");
			c.add(Restrictions.eq("genricDevice.UDID", UDID));

			genericDevicesList = c.list();
			genericDevices = (genericDevicesList != null && genericDevicesList
					.size() != 0) ? (GenericDevices) genericDevicesList.get(0)
					: null;
			if (genericDevices != null) {
				Hibernate.initialize(genericDevices.getDeviceModelMaster());
				Hibernate.initialize(genericDevices.getProductMaster());
				Hibernate.initialize(genericDevices.getPlatformType());
				Hibernate.initialize(genericDevices.getHostList());
				Hibernate.initialize(genericDevices.getDeviceLab());
				if (genericDevices.getDeviceModelMaster() != null)
					Hibernate.initialize(genericDevices.getDeviceModelMaster()
							.getDeviceMakeMaster());
				if ((genericDevices instanceof MobileType)
						&& ((MobileType) genericDevices).getDeviceMakeMaster() != null) {
					Hibernate.initialize(((MobileType) genericDevices)
							.getDeviceMakeMaster());
				}
				if ((genericDevices instanceof ServerType)) {
					if (((ServerType) genericDevices).getProcessor() != null) {
						Hibernate.initialize(((ServerType) genericDevices)
								.getProcessor());
					}
					if (((ServerType) genericDevices).getSystemType() != null) {
						Hibernate.initialize(((ServerType) genericDevices)
								.getSystemType());
					}
				}

			}
		} catch (Exception e) {
			log.error(e);
		}
		return genericDevices;
	}

	@Override
	@Transactional
	public void deleteRunConfiguration(RunConfiguration runcfg) {
		log.info("deleting RunConfiguration instance "
				+ runcfg.getRunconfigId());
		try {

			runcfg.setTestRunPlan(null);
			sessionFactory.getCurrentSession().update(runcfg);
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}

	}

	@Override
	@Transactional
	public List<RunConfiguration> getRunConfigurationByProductVersion(
			Integer productVersionId) {
		List<RunConfiguration> runConfigurationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					RunConfiguration.class, "runcfg");

			c.add(Restrictions.eq("productVersion.productVersionListId",
					productVersionId));

			runConfigurationList = c.list();

			if (runConfigurationList != null && !runConfigurationList.isEmpty()) {
				for (RunConfiguration runConfiguration : runConfigurationList) {
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration
							.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					if (runConfiguration.getTestRunPlanSet() != null) {
						for (TestRunPlan testRunPlan : runConfiguration
								.getTestRunPlanSet()) {
							Hibernate.initialize(testRunPlan
									.getProductVersionListMaster());
							Hibernate
									.initialize(testRunPlan.getExecutionType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return runConfigurationList;
	}

	@Override
	@Transactional
	public void updateEnvironmentCategory(
			EnvironmentCategory environmentCategory) {
		log.info("update environmentCategory instance");
		try {
			sessionFactory.getCurrentSession().update(environmentCategory);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateGenericDevice(GenericDevices deviceList) {
		log.info("updating DeviceList instance");
		try {

			sessionFactory.getCurrentSession().update(deviceList);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}

	}

	@Override
	@Transactional
	public List<GenericDevices> getGenericDevicesByHostId(Integer hostId) {
		log.info("listing Devices by host id");
		List<GenericDevices> deviceList = null;
		try {

			deviceList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from GenericDevices d where hostId=:hostId ORDER BY d.availableStatus asc")
					.setParameter("hostId", hostId).list();
			if (!(deviceList == null || deviceList.isEmpty())) {
				for (GenericDevices dl : deviceList) {
					if (dl.getDeviceModelMaster() != null)
						Hibernate.initialize(dl.getDeviceModelMaster()
								.getDeviceMakeMaster());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getDeviceLab());
					Hibernate.initialize(dl.getProductMaster());
					Hibernate.initialize(dl.getPlatformType());
					if ((dl instanceof MobileType)
							&& ((MobileType) dl).getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) dl)
								.getDeviceMakeMaster());
					}
					if ((dl instanceof ServerType)) {
						if (((ServerType) dl).getProcessor() != null) {
							Hibernate.initialize(((ServerType) dl)
									.getProcessor());
						}
						if (((ServerType) dl).getSystemType() != null) {
							Hibernate.initialize(((ServerType) dl)
									.getSystemType());
						}
					}
				}
			}
			log.info("listing Devices by host id successful");
		} catch (RuntimeException re) {
			log.error("listing Devices by host id failed", re);
			// throw re;
		}
		return deviceList;
	}

	@Override
	@Transactional
	public List<TestRunJob> listByHostId(Integer hostId, Integer status) {
		log.info("listing Devices by host id");
		List<TestRunJob> testRunJob = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					TestRunJob.class, "trj");
			c.createAlias("trj.hostList", "hl");
			c.add(Restrictions.eq("hl.hostId", hostId));
			c.add(Restrictions.eq("trj.testRunStatus", status));

			testRunJob = c.list();

			if (!(testRunJob == null || testRunJob.isEmpty())) {
				for (TestRunJob dl : testRunJob) {
					Hibernate.initialize(dl.getGenericDevices());
					Hibernate.initialize(dl.getTestRunStatus());

					if (dl.getGenericDevices() != null
							&& dl.getGenericDevices().getPlatformType() != null)
						Hibernate.initialize(dl.getGenericDevices()
								.getPlatformType());
					GenericDevices gd = dl.getGenericDevices();
					if ((gd instanceof MobileType)
							&& ((MobileType) gd).getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) gd)
								.getDeviceMakeMaster());
					}
					if ((gd instanceof ServerType)) {
						if (((ServerType) gd).getProcessor() != null) {
							Hibernate.initialize(((ServerType) gd)
									.getProcessor());
						}
						if (((ServerType) gd).getSystemType() != null) {
							Hibernate.initialize(((ServerType) gd)
									.getSystemType());
						}
					}
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getTestRunPlan());
					Hibernate.initialize(dl.getTestRunPlan()
							.getTestSuiteLists());
					Hibernate.initialize(dl.getTestSuite());
					if (dl.getTestSuite() != null) {
						Hibernate
								.initialize(dl.getTestSuite().getTestSuiteId());
						log.info("Test Suite Initialized for Test Run Job : "
								+ dl.getTestSuite().getTestSuiteId());
					}
					Hibernate.initialize(dl.getTestSuiteSet());
					Hibernate.initialize(dl.getEnvironmentCombination());
					Hibernate.initialize(dl.getRunConfiguration()
							.getEnvironmentcombination());
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration()
							.getTestSuiteLists());
					Hibernate.initialize(dl.getRunConfiguration()
							.getGenericDevice());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getTestRunPlan());
					Hibernate.initialize(dl.getTestRunPlan()
							.getProductVersionListMaster());
					Hibernate.initialize(dl.getTestRunPlan()
							.getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(dl.getTestRunPlan()
							.getProductVersionListMaster().getProductMaster()
							.getProductType());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild()
							.getProductVersion().getProductMaster()
							.getProductType());
					if (dl.getTestRunPlan().getAttachments() != null
							&& dl.getTestRunPlan().getAttachments().size() > 0) {
						Set<Attachment> attachmentSet = dl.getTestRunPlan()
								.getAttachments();
						Hibernate.initialize(attachmentSet);
						for (Attachment attach : attachmentSet) {
							Hibernate.initialize(attach);
						}
					}
					if (dl.getRunConfiguration() != null) {
						Hibernate.initialize(dl.getRunConfiguration());
						if (dl.getRunConfiguration().getTestSuiteLists() != null) {
							Hibernate.initialize(dl.getRunConfiguration()
									.getTestSuiteLists());
							for (TestSuiteList tsl : dl.getRunConfiguration()
									.getTestSuiteLists()) {
								if (tsl.getTestCaseLists() != null) {
									Hibernate
											.initialize(tsl.getTestCaseLists());
								}
							}
						}
					}
				}
			}
			log.info("listing Devices by host id successful");
		} catch (RuntimeException re) {
			log.error("listing Devices by host id failed", re);
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public Integer getAverageTestRunExecutionTime(TestRunJob testRunJob) {
		log.info("Getting average testRunJob execution time");
		try {
			String sqlQuery = "";
			if (testRunJob.getTestRunPlan().getProductVersionListMaster()
					.getProductMaster().getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_WEB
					|| testRunJob.getTestRunPlan()
							.getProductVersionListMaster().getProductMaster()
							.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_EMBEDDED
					|| testRunJob.getTestRunPlan()
							.getProductVersionListMaster().getProductMaster()
							.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_DESKTOP) {
				log.info("inside if");
				sqlQuery = "SELECT AVG(extract(epoch from(testRunEndTime-testRunStartTime))) as averageTestRunExecutionTime "
						+ " FROM test_run_job"
						+ " where testRunPlanId = "
						+ testRunJob.getTestRunPlan().getTestRunPlanId()
						+ " and hostId = "
						+ testRunJob.getHostList().getHostId()
						+ " and testSuiteId = "
						+ testRunJob.getTestSuite().getTestSuiteId()
						+ " group by testRunPlanId, hostId,testSuiteId";

			} else if (testRunJob.getTestRunPlan()
					.getProductVersionListMaster().getProductMaster()
					.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_COMPOSITE) {
				log.info("inside else");
				Integer hostId = null, deviceId = null;
				if (testRunJob.getHostList() != null
						&& testRunJob.getHostList().getHostId() != null)
					hostId = testRunJob.getHostList().getHostId();
				else
					hostId = null;

				if (testRunJob.getGenericDevices() != null
						&& testRunJob.getGenericDevices()
								.getGenericsDevicesId() != null)
					deviceId = testRunJob.getGenericDevices()
							.getGenericsDevicesId();
				else
					deviceId = null;

				sqlQuery = "SELECT AVG(extract(epoch from(testRunEndTime-testRunStartTime))) as averageTestRunExecutionTime "
						+ " FROM test_run_job"
						+ " where testRunPlanId = "
						+ testRunJob.getTestRunPlan().getTestRunPlanId()
						+ " and testSuiteId = "
						+ testRunJob.getTestSuite().getTestSuiteId()
						+ " and hostId = "
						+ hostId
						+ " and genericDeviceId = "
						+ deviceId
						+ " group by testRunPlanId, genericDeviceId,testSuiteId";
			} else {
				log.info("inside else");
				sqlQuery = "SELECT AVG(extract(epoch from(testRunEndTime-testRunStartTime))) as averageTestRunExecutionTime "
						+ " FROM test_run_job"
						+ " where testRunPlanId = "
						+ testRunJob.getTestRunPlan().getTestRunPlanId()
						+ " and genericDeviceId = "
						+ testRunJob.getGenericDevices().getGenericsDevicesId()
						+ " and testSuiteId = "
						+ testRunJob.getTestSuite().getTestSuiteId()
						+ " group by testRunPlanId, genericDeviceId,testSuiteId";
			}
			log.info("sqlQuery>>>>>>>>>>>>>>" + sqlQuery);

			Double averageTestRunExecutionTime = (Double) sessionFactory
					.getCurrentSession().createSQLQuery(sqlQuery)
					.uniqueResult();
			log.info("Getting average testrun execution time successful");
			if (averageTestRunExecutionTime != null)
				return averageTestRunExecutionTime.intValue();
			else
				return null;
		} catch (RuntimeException re) {
			log.error("Getting average testrun execution time failed", re);
			return null;
		}
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobById(Integer testRunJobId) {
		List<TestRunJob> testRunJobs = null;
		TestRunJob testRunJob = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					TestRunJob.class, "trj");

			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));
			testRunJobs = c.list();
			testRunJob = (testRunJobs != null && testRunJobs.size() != 0) ? (TestRunJob) testRunJobs
					.get(0) : null;
			if (testRunJob != null) {
				Hibernate.initialize(testRunJob.getGenericDevices());
				if (testRunJob.getGenericDevices() != null
						&& testRunJob.getGenericDevices().getPlatformType() != null)
					Hibernate.initialize(testRunJob.getGenericDevices()
							.getPlatformType());
				GenericDevices gd = testRunJob.getGenericDevices();
				if ((gd instanceof MobileType)
						&& ((MobileType) gd).getDeviceMakeMaster() != null) {
					Hibernate.initialize(((MobileType) gd)
							.getDeviceMakeMaster());
				}
				if ((gd instanceof ServerType)) {
					if (((ServerType) gd).getProcessor() != null) {
						Hibernate.initialize(((ServerType) gd).getProcessor());
					}
					if (((ServerType) gd).getSystemType() != null) {
						Hibernate.initialize(((ServerType) gd).getSystemType());
					}
				}
				Hibernate.initialize(testRunJob.getHostList());
				Hibernate.initialize(testRunJob.getEnvironmentCombination());
				if (testRunJob.getTestRunPlan() != null) {
					Hibernate.initialize(testRunJob.getTestRunPlan());
					Hibernate.initialize(testRunJob.getTestRunPlan()
							.getAttachments());
					if (testRunJob.getTestRunPlan().getAttachments() != null
							&& testRunJob.getTestRunPlan().getAttachments()
									.size() > 0) {
						Set<Attachment> attachmentSet = testRunJob
								.getTestRunPlan().getAttachments();
						Hibernate.initialize(attachmentSet);
						for (Attachment attach : attachmentSet) {
							Hibernate.initialize(attach);
						}
					}
					if (testRunJob.getTestRunPlan() != null
							&& testRunJob.getTestRunPlan()
									.getProductVersionListMaster() != null
							&& testRunJob.getTestRunPlan()
									.getProductVersionListMaster()
									.getProductMaster() != null)
						Hibernate.initialize(testRunJob.getTestRunPlan()
								.getProductVersionListMaster()
								.getProductMaster());
					if (testRunJob.getTestRunPlan().getRunConfigurationList() != null
							&& !testRunJob.getTestRunPlan()
									.getRunConfigurationList().isEmpty()) {
						Hibernate.initialize(testRunJob.getTestRunPlan()
								.getRunConfigurationList());
					}
				}

				Hibernate.initialize(testRunJob.getTestSuite());
				if (testRunJob.getTestSuite() != null
						&& testRunJob.getTestSuite().getTestCaseLists() != null)
					Hibernate.initialize(testRunJob.getTestSuite()
							.getTestCaseLists());
				Hibernate.initialize(testRunJob.getWorkPackage());
				Hibernate.initialize(testRunJob.getWorkPackage()
						.getTestRunJobSet());
			}
		} catch (Exception e) {
			log.error(e);
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJob(Integer testRunJobId) {
		List<TestRunJob> testRunJobs = null;
		TestRunJob testRunJob = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					TestRunJob.class, "trj");

			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));

			testRunJobs = c.list();
			testRunJob = (testRunJobs != null && testRunJobs.size() != 0) ? (TestRunJob) testRunJobs
					.get(0) : null;
			if (testRunJob != null) {
				Hibernate.initialize(testRunJob.getGenericDevices());
				if (testRunJob.getGenericDevices() != null
						&& testRunJob.getGenericDevices().getPlatformType() != null)
					Hibernate.initialize(testRunJob.getGenericDevices()
							.getPlatformType());
				GenericDevices gd = testRunJob.getGenericDevices();
				if ((gd instanceof MobileType)
						&& ((MobileType) gd).getDeviceMakeMaster() != null) {
					Hibernate.initialize(((MobileType) gd)
							.getDeviceMakeMaster());
				}
				if ((gd instanceof ServerType)) {
					if (((ServerType) gd).getProcessor() != null) {
						Hibernate.initialize(((ServerType) gd).getProcessor());
					}
					if (((ServerType) gd).getSystemType() != null) {
						Hibernate.initialize(((ServerType) gd).getSystemType());
					}
				}
				Hibernate.initialize(testRunJob.getHostList());
				Hibernate.initialize(testRunJob.getEnvironmentCombination());
				Hibernate.initialize(testRunJob
						.getWorkPackageTestCaseExecutionPlans());
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = testRunJob
						.getWorkPackageTestCaseExecutionPlans();
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult());
					if (workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult() != null
							&& workPackageTestCaseExecutionPlan
									.getTestCaseExecutionResult()
									.getTestExecutionResultBugListSet() != null) {
						Hibernate.initialize(workPackageTestCaseExecutionPlan
								.getTestCaseExecutionResult()
								.getTestExecutionResultBugListSet());
						Set<TestExecutionResultBugList> testExecutionResultBugList = workPackageTestCaseExecutionPlan
								.getTestCaseExecutionResult()
								.getTestExecutionResultBugListSet();
						if (testExecutionResultBugList != null
								&& !testExecutionResultBugList.isEmpty()) {
							for (TestExecutionResultBugList terb : testExecutionResultBugList) {
								Hibernate
										.initialize(terb
												.getTestCaseExecutionResult()
												.getWorkPackageTestCaseExecutionPlan()
												.getWorkPackage()
												.getProductBuild()
												.getProductVersion()
												.getProductMaster());
								Hibernate.initialize(terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getRunConfiguration()
										.getRunconfiguration());
								Hibernate.initialize(terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getRunConfiguration()
										.getRunconfiguration()
										.getGenericDevice());
								if (terb.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getRunConfiguration()
										.getRunconfiguration()
										.getGenericDevice() != null) {
									Hibernate
											.initialize(terb
													.getTestCaseExecutionResult()
													.getWorkPackageTestCaseExecutionPlan()
													.getRunConfiguration()
													.getRunconfiguration()
													.getGenericDevice()
													.getPlatformType());
								}

								Hibernate.initialize(terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getTestCase());
								Hibernate.initialize(terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getTestCase().getTestCasePriority());

								Hibernate.initialize(terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getTestCaseExecutionResult()
										.getTestStepExecutionResultSet());
								Set<TestStepExecutionResult> testStepExecutionResults = terb
										.getTestCaseExecutionResult()
										.getWorkPackageTestCaseExecutionPlan()
										.getTestCaseExecutionResult()
										.getTestStepExecutionResultSet();
								for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
									Hibernate
											.initialize(testStepExecutionResult
													.getTestSteps());
								}

							}
						}
					}
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getWorkPackage().getProductBuild()
							.getProductVersion().getProductMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult()
							.getTestStepExecutionResultSet());
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getRunConfiguration().getRunconfiguration()
							.getGenericDevice());
					if (workPackageTestCaseExecutionPlan.getRunConfiguration()
							.getRunconfiguration().getGenericDevice() != null) {
						Hibernate.initialize(workPackageTestCaseExecutionPlan
								.getRunConfiguration().getRunconfiguration()
								.getGenericDevice().getPlatformType());
					}

					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult()
							.getTestStepExecutionResultSet());
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCase());
					Set<TestStepExecutionResult> testStepExecutionResults = workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult()
							.getTestStepExecutionResultSet();
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						Hibernate.initialize(testStepExecutionResult
								.getTestSteps());

					}
					Hibernate.initialize(workPackageTestCaseExecutionPlan
							.getTestCase().getTestCasePriority());
				}

				Hibernate.initialize(testRunJob.getTestRunPlan());
				if (testRunJob.getTestRunPlan() != null) {
					Hibernate.initialize(testRunJob.getTestRunPlan()
							.getAttachments());
					if (testRunJob.getTestRunPlan().getAttachments() != null
							&& testRunJob.getTestRunPlan().getAttachments()
									.size() > 0) {
						Set<Attachment> attachmentSet = testRunJob
								.getTestRunPlan().getAttachments();
						Hibernate.initialize(attachmentSet);
						for (Attachment attach : attachmentSet) {
							Hibernate.initialize(attach);
						}
					}
				}
				Hibernate.initialize(testRunJob.getTestRunPlan()
						.getProductVersionListMaster());
				if (testRunJob.getTestRunPlan().getProductVersionListMaster()
						.getProductMaster() != null) {
					Hibernate.initialize(testRunJob.getTestRunPlan()
							.getProductVersionListMaster().getProductMaster());
				}
				Hibernate.initialize(testRunJob.getTestSuiteSet());
				Hibernate.initialize(testRunJob.getWorkPackage());
				Hibernate.initialize(testRunJob.getWorkPackage()
						.getProductBuild().getProductVersion()
						.getProductMaster());
				Hibernate.initialize(testRunJob.getWorkPackage()
						.getProductBuild().getProductVersion()
						.getProductMaster().getTestManagementSystems());
				Set<TestManagementSystem> tms = testRunJob.getWorkPackage()
						.getProductBuild().getProductVersion()
						.getProductMaster().getTestManagementSystems();
				for (TestManagementSystem testManagementSystem : tms) {
					Hibernate.initialize(testManagementSystem
							.getTestManagementSystemMappings());
				}

				if (testRunJob.getGenericDevices() != null) {
					if (testRunJob.getGenericDevices().getDeviceModelMaster() != null) {
						Hibernate.initialize(testRunJob.getGenericDevices()
								.getDeviceModelMaster());
						Hibernate.initialize(testRunJob.getGenericDevices()
								.getDeviceModelMaster().getDeviceMakeMaster());

					}
				}

			}
		} catch (Exception e) {
			log.error("Not able to get Test Run Job", e);
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public boolean hasTestRunCompleted(TestRunJob testRunJob) {
		log.info("Checking if TestRun has completed");
		List<TestRunJob> testRunJobs = null;
		try {
			int testRunPlanId = testRunJob.getTestRunPlan().getTestRunPlanId()
					.intValue();
			testRunJobs = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestRunJob e where e.testRunPlan.testRunPlanId=:testRunPlanId and e.workPackage.workPackageId=:workPackageId")
					.setParameter("testRunPlanId", testRunPlanId)
					.setParameter("workPackageId",
							testRunJob.getWorkPackage().getWorkPackageId())
					.list();
			int totalJobs = testRunJobs.size();
			int completedJobs = 0;
			for (TestRunJob trl : testRunJobs) {
				if (trl.getTestRunStatus() == 1 || trl.getTestRunStatus() == 2) {
					completedJobs++;
				}
			}
			log.info("TestRunLists count : " + completedJobs + " / "
					+ totalJobs);
			return (completedJobs == totalJobs);
		} catch (RuntimeException re) {
			log.error("Not able to verify of TestRun is complete", re);
			// throw re;
		}
		return false;
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfDeviceOfTestRunExisting(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer deviceId, Integer versionId) {
		log.info("isRunConfigurationOfDeviceOfTestRunExisting");
		RunConfiguration runconfig = null;
		List<RunConfiguration> runconfigList = null;
		try {
			runconfigList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
									+ " rc.testRunPlan.testRunPlanId=:trplanId and rc.genericDevice.genericsDevicesId=:deviceId and "
									+ " rc.productVersion.productVersionListId=:verId ")
					.setParameter("ecid", environmentCombinationId)
					.setParameter("trplanId", testRunPlanId)
					.setParameter("deviceId", deviceId)
					.setParameter("verId", versionId).list();
			runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList
					.get(0) : null;
			if (runconfig != null) {
				Hibernate.initialize(runconfig.getTestRunPlanSet());
				Hibernate.initialize(runconfig.getEnvironmentcombination());
				if (runconfig.getGenericDevice() != null) {
					GenericDevices gd = runconfig.getGenericDevice();
					Hibernate.initialize(gd);
					if (gd.getHostList() != null) {
						HostList hl = gd.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}
				}

				if (runconfig.getTestRunPlan() != null) {
					TestRunPlan trunplan = runconfig.getTestRunPlan();
					Hibernate.initialize(trunplan);
					if (trunplan.getProductVersionListMaster() != null) {
						ProductVersionListMaster version = trunplan
								.getProductVersionListMaster();
						Hibernate.initialize(version);
						if (version.getProductMaster() != null) {
							ProductMaster prod = version.getProductMaster();
							Hibernate.initialize(prod);
							if (prod.getProductType() != null) {
								ProductType ptype = prod.getProductType();
								Hibernate.initialize(ptype);
							}
						}
					}
				}
				if (runconfig.getTestRunPlanSet() != null) {
					for (TestRunPlan testRunPlan : runconfig
							.getTestRunPlanSet()) {
						Hibernate.initialize(testRunPlan
								.getProductVersionListMaster());
						if (testRunPlan.getProductVersionListMaster()
								.getProductMaster() != null) {
							Hibernate.initialize(testRunPlan
									.getProductVersionListMaster()
									.getProductMaster());
						}
						Hibernate.initialize(testRunPlan.getExecutionType());
					}
				}
			}
			return runconfig;
		} catch (RuntimeException re) {
			log.error("isRunConfigurationOfDeviceOfTestRunExisting Error ", re);
		}
		return runconfig;
	}

	@Override
	@Transactional
	public void update(TestRunJob testRunJob) {
		log.info("updating TestRunList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testRunJob);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public void addTestRunJob(TestRunJob testRunJob) {
		TestRunList res = null;
		log.info("adding testRunJob instance");
		try {
			sessionFactory.getCurrentSession().save(testRunJob);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}

	}

	@Override
	@Transactional
	public List<GenericDevices> listGenericDevices(Integer deviceLabId,
			int filter, int deviceTypeId) {
		List<GenericDevices> genericDevicesList = null;
		try {
			if (deviceTypeId != -1 && deviceTypeId == 6) {
				int stat = 1;
				String sql = "";
				if (filter == 1) {// Active
					sql = "select * from generic_devices gd where gd.deviceLabId =:deviceLabId AND gd.deviceTypeId=:devTypeId and gd.status=1";
				} else if (filter == 0) {// InActive
					sql = "select * from generic_devices gd where gd.deviceLabId =:deviceLabId AND gd.deviceTypeId=:devTypeId and gd.status=0";
				} else {// All
					sql = "select * from generic_devices gd where gd.deviceLabId =:deviceLabId AND gd.deviceTypeId=:devTypeId";
				}

				Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sql).addEntity(GenericDevices.class)
						.setParameter("deviceLabId", deviceLabId)
						.setParameter("devTypeId", deviceTypeId);

				genericDevicesList = query.list();
			} else {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(
						GenericDevices.class, "gd");

				if (deviceLabId != -1) {
					c.createAlias("gd.deviceLab", "dl");
					c.add(Restrictions.eq("dl.device_lab_Id", deviceLabId));
				}
				if (filter == 1) {
					c.add(Restrictions.eq("gd.availableStatus", 1));
				} else if (filter == 0) {
					c.add(Restrictions.eq("gd.availableStatus", 0));
				}

				genericDevicesList = c.list();
			}

			if (genericDevicesList != null && !genericDevicesList.isEmpty()) {
				for (GenericDevices genericDevices : genericDevicesList) {
					Hibernate.initialize(genericDevices.getDeviceModelMaster());
					Hibernate.initialize(genericDevices.getDeviceLab());
					Hibernate.initialize(genericDevices.getPlatformType());
					Hibernate.initialize(genericDevices.getProductMaster());
					Hibernate.initialize(genericDevices.getHostList());
					if ((genericDevices instanceof MobileType)
							&& ((MobileType) genericDevices)
									.getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) genericDevices)
								.getDeviceMakeMaster());
					}
					if ((genericDevices instanceof ServerType)) {
						if (((ServerType) genericDevices).getProcessor() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getProcessor());
						}
						if (((ServerType) genericDevices).getSystemType() != null) {
							Hibernate.initialize(((ServerType) genericDevices)
									.getSystemType());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Unable to list generic devices", e);
		}
		return genericDevicesList;
	}

	@Override
	@Transactional
	public List<HostList> listHost(Integer deviceLabId, Integer filter) {
		List<HostList> hostLists = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					HostList.class, "hl");
			c.createAlias("hl.commonActiveStatusMaster", "s");
			if (filter == 1) {
				c.add(Restrictions.eq("s.status", "ACTIVE"));
			} else if (filter == 0) {
				c.add(Restrictions.eq("s.status", "INACTIVE"));
			}
			hostLists = c.list();

		} catch (Exception e) {
			log.error("Unable to list hosts", e);
		}
		return hostLists;
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobByStatus(Integer testRunJobId, Integer status) {
		List<TestRunJob> testRunJobs = null;
		TestRunJob testRunJob = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					TestRunJob.class, "trj");

			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));
			c.add(Restrictions.eq("trj.testRunStatus", 3));

			testRunJobs = c.list();
			testRunJob = (testRunJobs != null && testRunJobs.size() != 0) ? (TestRunJob) testRunJobs
					.get(0) : null;
			if (testRunJob != null) {

				Hibernate.initialize(testRunJob.getGenericDevices());
				if (testRunJob.getGenericDevices() != null
						&& testRunJob.getGenericDevices().getPlatformType() != null)
					Hibernate.initialize(testRunJob.getGenericDevices()
							.getPlatformType());
				GenericDevices gd = testRunJob.getGenericDevices();
				if ((gd instanceof MobileType)
						&& ((MobileType) gd).getDeviceMakeMaster() != null) {
					Hibernate.initialize(((MobileType) gd)
							.getDeviceMakeMaster());
				}
				if ((gd instanceof ServerType)) {
					if (((ServerType) gd).getProcessor() != null) {
						Hibernate.initialize(((ServerType) gd).getProcessor());
					}
					if (((ServerType) gd).getSystemType() != null) {
						Hibernate.initialize(((ServerType) gd).getSystemType());
					}
				}
				Hibernate.initialize(testRunJob.getHostList());
				Hibernate.initialize(testRunJob.getEnvironmentCombination());

				Hibernate.initialize(testRunJob.getTestRunPlan());
				Hibernate.initialize(testRunJob.getTestSuite());
				Hibernate.initialize(testRunJob.getWorkPackage());
			}
		} catch (Exception e) {
			log.error("Unable to get Test Run Job by status", e);
		}
		return testRunJob;

	}

	@Override
	@Transactional
	public void deleteTestRunJob(Integer workpackageId, Integer runConfigId,
			Integer testSuiteId) {
		List<TestRunJob> testRunJobs = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					TestRunJob.class, "trj");
			c.createAlias("trj.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId", workpackageId));

			if (testSuiteId != null) {
				c.createAlias("trj.testSuite", "ts");
				c.add(Restrictions.eq("ts.testSuiteId", testSuiteId));
			}
			if (runConfigId != null) {
				c.createAlias("trj.runConfiguration", "rc");
				c.add(Restrictions.eq("rc.runconfigId", runConfigId));
			}
			testRunJobs = c.list();
			if (testRunJobs != null && !testRunJobs.isEmpty()) {
				for (TestRunJob testRunJob : testRunJobs) {
					Hibernate.initialize(testRunJob.getRunConfiguration());
					sessionFactory.getCurrentSession().delete(testRunJob);
				}
			}
		} catch (Exception e) {
			log.error("Unable to deleted test run job", e);
		}
	}

	@Override
	@Transactional
	public List<RunConfiguration> listRunConfigurationBywpId(
			Integer workPackageId) {
		List<TestRunJob> testRunJobs = null;
		List<RunConfiguration> runConfigList = new ArrayList<RunConfiguration>(
				0);
		Criteria c = sessionFactory.getCurrentSession().createCriteria(
				TestRunJob.class, "trj");
		c.createAlias("trj.workPackage", "wp");
		c.createAlias("trj.runConfiguration", "rc");
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		c.add(Restrictions.eq("rc.runConfigStatus", 1));
		c.addOrder(Order.asc("rc.runconfigId"));
		testRunJobs = c.list();
		if (testRunJobs != null && !testRunJobs.isEmpty()) {
			for (TestRunJob testRunJob : testRunJobs) {
				if (testRunJob.getRunConfiguration() != null) {
					runConfigList.add(testRunJob.getRunConfiguration());
				}

			}
		}
		return runConfigList;
	}

	@Override
	@Transactional
	public Integer getTotalJobsOfWPByStatus(Integer workpackageId,
			Integer testRunPlanId, Integer testRunStatus,
			List<Integer> testRunStatuses) {
		int jobsOfWPByStatus = 0;
		String sql = "";
		try {
			sql = "select count(trj.testRunStatus) from test_run_job trj "
					+ "inner join test_run_plan trp on trj.testRunPlanId =  trp.testRunPlanId "
					+ "inner join workpackage wp on trj.workpackageId = wp.workPackageId "
					+ "where trj.workpackageId=:wpId and trj.testRunPlanId=:trpId and trj.testRunStatus=:trjStatus";

			if (testRunStatuses != null && !testRunStatuses.isEmpty()) {
				jobsOfWPByStatus = ((Number) (sessionFactory
						.getCurrentSession().createSQLQuery(sql)
						.setParameter("wpId", workpackageId)
						.setParameter("trpId", testRunPlanId)
						.setParameter("trjStatus", testRunStatuses)
						.uniqueResult())).intValue();
			} else {
				jobsOfWPByStatus = ((Number) (sessionFactory
						.getCurrentSession().createSQLQuery(sql)
						.setParameter("wpId", workpackageId)
						.setParameter("trpId", testRunPlanId)
						.setParameter("trjStatus", testRunStatus)
						.uniqueResult())).intValue();
			}

		} catch (RuntimeException re) {
			log.error(re);
		}
		return jobsOfWPByStatus;
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getChildEnvCategoriesByParentCategoryId(
			Integer parentEnvironmentCategoryId) {
		List<EnvironmentCategory> environmentCategoryList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "ec");
			c.add(Restrictions.eq(
					"ec.parentEnvironmentCategory.environmentCategoryId",
					parentEnvironmentCategoryId));
			c.add(Restrictions.eq("ec.status", 1));
			environmentCategoryList = c.list();

			if (environmentCategoryList != null
					&& !environmentCategoryList.isEmpty()) {
				for (EnvironmentCategory environmentCategory : environmentCategoryList) {
					Hibernate.initialize(environmentCategory
							.getEnvironmentGroup());
					Hibernate.initialize(environmentCategory
							.getParentEnvironmentCategory());
				}
			}
		} catch (Exception e) {
			log.error("Unable to get Child Env categories", e);
		}
		return environmentCategoryList;
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> listEnvironmentCombination(
			Integer productId) {
		List<EnvironmentCombination> environmentCombinations = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.productMaster", "pm");
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));

				// for in PLAN tab only active status need to be display
				c.add(Restrictions.eq("ec.envionmentCombinationStatus", 1));
			}

			environmentCombinations = c.list();

			if (environmentCombinations != null
					&& !environmentCombinations.isEmpty()) {
				for (EnvironmentCombination environmentCombination : environmentCombinations) {
					Hibernate.initialize(environmentCombination
							.getEnvironmentSet());
					Hibernate.initialize(environmentCombination
							.getProductMaster());
					Hibernate.initialize(environmentCombination
							.getProductMaster().getProductType());
					if (environmentCombination.getEnvironmentSet() != null) {
						for (Environment env : environmentCombination
								.getEnvironmentSet()) {
							Hibernate.initialize(env.getEnvironmentCategory());
							Hibernate.initialize(env.getEnvironmentCategory()
									.getEnvironmentGroup());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Unable to get Environment lists", e);
		}
		return environmentCombinations;
	}

	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostListFromRunconfigurationWorkPackageLevel(
			Integer environmentCombinationId, Integer workPackageId,
			Integer runConfigStatus) {
		log.info("getMappedHostListFromRunconfigurationWorkPackageLevel");

		List<RunConfiguration> runconfigList = new ArrayList<RunConfiguration>();
		List<JsonRunConfiguration> jsonRunconfigList = new ArrayList<JsonRunConfiguration>();
		List<Object[]> runconfigObjList = new ArrayList<Object[]>();
		Integer testRunPlanId = 0;
		String sql = "";
		try {
			// Check if WP was created through TestRunPlan
			sql = "select wp.testRunPlanId from workpackage wp where workPackageId=:wpid";
			// testRunPlanId =
			// ((Number)(sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid",
			// workPackageId).uniqueResult())).intValue();
			Object tRPlanIdObject = sessionFactory.getCurrentSession()
					.createSQLQuery(sql).setParameter("wpid", workPackageId)
					.uniqueResult();
			if (tRPlanIdObject != null) {
				testRunPlanId = ((Number) tRPlanIdObject).intValue();
			}

			// if created through TRPlan, then fetch Mapped Host from
			// WPRunconfig(WorkpackageRunConfiguration) table
			if (testRunPlanId != null && testRunPlanId != 0) {
				runconfigObjList = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from RunConfiguration rc join rc.workPackageRunConfigSet whr "
										+ "  where whr.workpackage.workPackageId=:wpid and rc.runconfigId=whr.runconfiguration.runconfigId and "
										+ "rc.runConfigStatus=:rconfigStat and rc.hostList.hostId is not null and "
										+ "rc.environmentcombination.environment_combination_id =:envcombid group by whr.runconfiguration.runconfigId order by whr.runconfiguration.runconfigId")
						.setParameter("wpid", workPackageId)
						.setParameter("envcombid", environmentCombinationId)
						.setParameter("rconfigStat", runConfigStatus).list();
				// jsonRunconfigList=
				// getMappedHostListFromRunconfigurationTestRunPlanLevel(environmentCombinationId,
				// testRunPlanId, runConfigStatus);
			} else {// else if WP was created manually,
					// Fetch Mapped Host of WP
				runconfigList = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
										+ " rc.workPackage.workPackageId=:wpId and rc.runConfigStatus=:rconfigStat and rc.hostList.hostId is not null")
						.setParameter("ecid", environmentCombinationId)
						.setParameter("wpId", workPackageId)
						.setParameter("rconfigStat", runConfigStatus).list();
			}
			// runconfig = (runconfigList != null && runconfigList.size() != 0)
			// ? (RunConfiguration) runconfigList.get(0) : null;
			if (runconfigObjList != null && !runconfigObjList.isEmpty()) {
				for (Object[] runconfigwprunconfigobj : runconfigObjList) {
					runconfigList
							.add((RunConfiguration) runconfigwprunconfigobj[0]);
				}
			}
			if (runconfigList != null && !runconfigList.isEmpty()) {
				for (RunConfiguration runConfiguration : runconfigList) {
					if (runConfiguration != null) {
						Hibernate.initialize(runConfiguration
								.getTestRunPlanSet());
						Hibernate.initialize(runConfiguration
								.getEnvironmentcombination());
						if (runConfiguration.getGenericDevice() != null) {
							GenericDevices gd = runConfiguration
									.getGenericDevice();
							Hibernate.initialize(gd);
							if (gd.getHostList() != null) {
								HostList hl = gd.getHostList();
								Hibernate.initialize(hl);
								if (hl.getCommonActiveStatusMaster() != null) {
									CommonActiveStatusMaster casm = hl
											.getCommonActiveStatusMaster();
									Hibernate.initialize(casm);
								}
							}
						}

						if (runConfiguration.getTestRunPlan() != null) {
							if (runConfiguration.getTestRunPlan() != null) {
								TestRunPlan trunplan = runConfiguration
										.getTestRunPlan();
								Hibernate.initialize(trunplan);
								if (trunplan.getProductVersionListMaster() != null) {
									ProductVersionListMaster version = trunplan
											.getProductVersionListMaster();
									Hibernate.initialize(version);
									if (version.getProductMaster() != null) {
										ProductMaster prod = version
												.getProductMaster();
										Hibernate.initialize(prod);
										if (prod.getProductType() != null) {
											ProductType ptype = prod
													.getProductType();
											Hibernate.initialize(ptype);
										}
									}
								}
							}
						}
						if (runConfiguration.getHostList() != null) {
							HostList hl = runConfiguration.getHostList();
							Hibernate.initialize(hl);
							if (hl.getCommonActiveStatusMaster() != null) {
								CommonActiveStatusMaster casm = hl
										.getCommonActiveStatusMaster();
								Hibernate.initialize(casm);
							}
						}
						jsonRunconfigList.add(new JsonRunConfiguration(
								runConfiguration));
					}
				}
			}

			return jsonRunconfigList;
		} catch (RuntimeException re) {
			log.error(
					"getMappedHostListFromRunconfigurationWorkPackageLevel Error ",
					re);
		}
		return jsonRunconfigList;
	}

	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostListFromRunconfigurationTestRunPlanLevel(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus) {
		log.info("getMappedHostListFromRunconfigurationWorkPackageLevel");

		List<RunConfiguration> runconfigList = null;
		List<JsonRunConfiguration> jsonRunconfigList = new ArrayList<JsonRunConfiguration>();

		try {
			runconfigList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
									+ " rc.testRunPlan.testRunPlanId=:trplanId and rc.runConfigStatus=:rconfigStat and rc.hostList.hostId is not null")
					.setParameter("ecid", environmentCombinationId)
					.setParameter("trplanId", testRunPlanId)
					.setParameter("rconfigStat", runConfigStatus).list();
			for (RunConfiguration runConfiguration : runconfigList) {
				if (runConfiguration != null) {
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration
							.getEnvironmentcombination());

					if (runConfiguration.getHostList() != null) {
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
						if (hl.getHostTypeMaster() != null) {
							HostTypeMaster htm = hl.getHostTypeMaster();
							Hibernate.initialize(htm);
						}
						if (hl.getHostPlatformMaster() != null) {
							HostPlatformMaster hpfm = hl
									.getHostPlatformMaster();
							Hibernate.initialize(hpfm);
						}
					}
					if (runConfiguration.getTestRunPlan() != null) {
						if (runConfiguration.getTestRunPlan() != null) {
							TestRunPlan trunplan = runConfiguration
									.getTestRunPlan();
							Hibernate.initialize(trunplan);
							if (trunplan.getProductVersionListMaster() != null) {
								ProductVersionListMaster version = trunplan
										.getProductVersionListMaster();
								Hibernate.initialize(version);
								if (version.getProductMaster() != null) {
									ProductMaster prod = version
											.getProductMaster();
									Hibernate.initialize(prod);
									if (prod.getProductType() != null) {
										ProductType ptype = prod
												.getProductType();
										Hibernate.initialize(ptype);
									}
								}
							}
						}
					}
					if (runConfiguration.getHostList() != null) {
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}
					jsonRunconfigList.add(new JsonRunConfiguration(
							runConfiguration));
				}
			}
			return jsonRunconfigList;
		} catch (RuntimeException re) {
			log.error(
					"getMappedHostListFromRunconfigurationWorkPackageLevel Error ",
					re);
		}
		return jsonRunconfigList;
	}

	@Override
	@Transactional
	public List<JsonRunConfiguration> getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer runConfigStatus) {
		log.info("getMappedHostAnd DeviceListFromRunconfigurationTestRunPlanLevel");

		List<RunConfiguration> runconfigList = null;
		List<JsonRunConfiguration> jsonRunconfigList = new ArrayList<JsonRunConfiguration>();

		try {
			String query = "select * from runconfiguration where environmentcombinationId="
					+ environmentCombinationId
					+ " and testRunPlanId="
					+ testRunPlanId
					+ " and runConfigStatus="
					+ runConfigStatus
					+ " and (hostId or deviceId) is not null";
			runconfigList = sessionFactory.getCurrentSession()
					.createSQLQuery(query).addEntity(RunConfiguration.class)
					.list();

			for (RunConfiguration runConfiguration : runconfigList) {
				if (runConfiguration != null) {
					Hibernate.initialize(runConfiguration.getTestRunPlanSet());
					Hibernate.initialize(runConfiguration
							.getEnvironmentcombination());

					if (runConfiguration.getHostList() != null) {
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
						if (hl.getHostTypeMaster() != null) {
							HostTypeMaster htm = hl.getHostTypeMaster();
							Hibernate.initialize(htm);
						}
						if (hl.getHostPlatformMaster() != null) {
							HostPlatformMaster hpfm = hl
									.getHostPlatformMaster();
							Hibernate.initialize(hpfm);
						}
					}
					if (runConfiguration.getTestRunPlan() != null) {
						if (runConfiguration.getTestRunPlan() != null) {
							TestRunPlan trunplan = runConfiguration
									.getTestRunPlan();
							Hibernate.initialize(trunplan);
							if (trunplan.getProductVersionListMaster() != null) {
								ProductVersionListMaster version = trunplan
										.getProductVersionListMaster();
								Hibernate.initialize(version);
								if (version.getProductMaster() != null) {
									ProductMaster prod = version
											.getProductMaster();
									Hibernate.initialize(prod);
									if (prod.getProductType() != null) {
										ProductType ptype = prod
												.getProductType();
										Hibernate.initialize(ptype);
									}
								}
							}
						}
					}
					if (runConfiguration.getHostList() != null) {
						HostList hl = runConfiguration.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}
					jsonRunconfigList.add(new JsonRunConfiguration(
							runConfiguration));
				}
			}
			return jsonRunconfigList;
		} catch (RuntimeException re) {
			log.error(
					"getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel Error ",
					re);
		}
		return jsonRunconfigList;
	}

	@Override
	@Transactional
	public List<EnvironmentCombination> getEnvironmentsCombinationsMappedToActivity(
			Integer activityId, Integer workPackageId) {
		log.info(" getEnvironmentsCombinationsMappedToActivity for activity "
				+ activityId);
		List<EnvironmentCombination> environmentCombinationList = new ArrayList<EnvironmentCombination>();
		List<EnvironmentCombination> environmentCombinations = new ArrayList<EnvironmentCombination>();
		try {
			Set<EnvironmentCombination> ecombinationSet = new HashSet<EnvironmentCombination>();
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.activitySet", "activity");
			c.createAlias("ec.activitySet.activityWorkPackage", "activityWp");
			if (workPackageId != null && workPackageId > 0) {
				c.add(Restrictions.eq("activityWp.activityWorkPackageId",
						workPackageId));
			}

			if (activityId != null && activityId > 0) {
				c.add(Restrictions.eq("activity.activityId", activityId));
			}

			environmentCombinationList = c.list();

			for (EnvironmentCombination environmentCombination : environmentCombinationList) {
				Hibernate.initialize(environmentCombination.getActivitySet());
				Hibernate.initialize(environmentCombination.getProductMaster()
						.getProductType());
			}

			if (environmentCombinationList != null) {
				for (EnvironmentCombination ec : environmentCombinationList) {
					ecombinationSet.add(ec);
				}
			}
			if (ecombinationSet != null) {
				for (EnvironmentCombination ecSet : ecombinationSet) {
					environmentCombinations.add(ecSet);
				}
			}
			log.info("getEnvironmentsCombinationsMappedToActivity successful");
		} catch (RuntimeException re) {
			log.error("Unable to get Env combinations for Activity", re);
		}
		return environmentCombinations;
	}

	@Override
	@Transactional
	public EnvironmentCombination listEnvironmentCombinationByNameNoIntialize(
			Integer productId, String enviCombinationName) {
		List<EnvironmentCombination> environmentCombinations = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.productMaster", "pm");
			if (productId != 0) {
				c.add(Restrictions.eq("pm.productId", productId));
			}

			c.add(Restrictions.eq("ec.envionmentCombinationStatus", 1));
			c.add(Restrictions.eq("ec.environmentCombinationName",
					enviCombinationName));
			environmentCombinations = c.list();
			if (environmentCombinations != null
					&& environmentCombinations.size() > 0) {
				return environmentCombinations.get(0);
			}

		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	@Override
	public List<GenericDevices> getGenericDevicesByHostIdAndDeviceTypeId(
			Integer hostId, Integer deviceTypeId) {
		log.info("listing Devices by host id and deviceTypeId");
		List<GenericDevices> deviceList = null;
		try {

			deviceList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from GenericDevices d where hostId=:hostId and deviceTypeId=:deviceTypeId ORDER BY d.availableStatus asc")
					.setParameter("hostId", hostId)
					.setParameter("deviceTypeId", deviceTypeId).list();
			if (!(deviceList == null || deviceList.isEmpty())) {
				for (GenericDevices dl : deviceList) {
					if (dl.getDeviceModelMaster() != null)
						Hibernate.initialize(dl.getDeviceModelMaster()
								.getDeviceMakeMaster());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getDeviceLab());
					Hibernate.initialize(dl.getProductMaster());
					Hibernate.initialize(dl.getPlatformType());
					if ((dl instanceof MobileType)
							&& ((MobileType) dl).getDeviceMakeMaster() != null) {
						Hibernate.initialize(((MobileType) dl)
								.getDeviceMakeMaster());
					}
					if ((dl instanceof ServerType)) {
						if (((ServerType) dl).getProcessor() != null) {
							Hibernate.initialize(((ServerType) dl)
									.getProcessor());
						}
						if (((ServerType) dl).getSystemType() != null) {
							Hibernate.initialize(((ServerType) dl)
									.getSystemType());
						}
					}
				}
			}
			log.info("listing Devices by host id and deviceTypeId successful");
		} catch (RuntimeException re) {
			log.error("listing Devices by host id and deviceTypeId failed", re);
		}
		return deviceList;
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> listEnvCategoryByStatus(Integer status) {
		List<EnvironmentCategory> listOfEnvCategories = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "env");
			listOfEnvCategories = c.add(Restrictions.eq("env.status", status))
					.list();
		} catch (RuntimeException re) {
			log.error("listEnvCategoryByStatus failed", re);
		}
		return listOfEnvCategories;
	}

	@Override
	@Transactional
	public EnvironmentCategory getEnvironmentCategoryByName(
			String environmentCategoryName) {
		EnvironmentCategory environmentCategory = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "env");
			List<EnvironmentCategory> envCategories = c.add(
					Restrictions.eq("env.environmentCategoryName",
							environmentCategoryName)).list();
			for (EnvironmentCategory envCategory : envCategories) {
				Hibernate
						.initialize(envCategory.getParentEnvironmentCategory());
			}
			environmentCategory = (envCategories != null && envCategories
					.size() != 0) ? (EnvironmentCategory) envCategories.get(0)
					: null;
			log.info("getEnvironmentCategoryByName successful");
		} catch (Exception e) {
			log.error("getEnvironmentCategoryByName failed");
		}
		return environmentCategory;
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(String tableName,
			int parentRightIndex) {

		int leftIndex = parentRightIndex;
		int rightIndex = leftIndex + 1;
		final String strRightIndex = "right_index";
		String jpql = "UPDATE "
				+ tableName
				+ " SET right_index = right_index + 2 WHERE right_index >= :right_index and left_index < :right_index";
		SQLQuery fetchQuery = sessionFactory.getCurrentSession()
				.createSQLQuery(jpql);
		fetchQuery.setParameter(strRightIndex, parentRightIndex);
		fetchQuery.executeUpdate();

		jpql = "UPDATE "
				+ tableName
				+ " SET left_index=left_index + 2, right_index = right_index + 2 WHERE right_index > :right_index and left_index > :right_index";
		fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
		fetchQuery.setParameter(strRightIndex, parentRightIndex);
		fetchQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(String tableName, int leftIndex,
			int rightIndex) {

		final String strRightIndex = "right_index";
		String jpql = "UPDATE "
				+ tableName
				+ " SET right_index = right_index - 2 WHERE right_index > :right_index and left_index < :right_index";
		SQLQuery fetchQuery = sessionFactory.getCurrentSession()
				.createSQLQuery(jpql);
		fetchQuery.setParameter(strRightIndex, rightIndex);
		fetchQuery.executeUpdate();

		jpql = "UPDATE "
				+ tableName
				+ " SET left_index=left_index - 2, right_index = right_index - 2 WHERE right_index > :right_index and left_index > :right_index";
		fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
		fetchQuery.setParameter(strRightIndex, rightIndex);
		fetchQuery.executeUpdate();
	}

	@Override
	@Transactional
	public EnvironmentCategory getRootEnvironmentCategory() {
		EnvironmentCategory environmentCategory = null;
		try {
			environmentCategory = getEnvironmentCategoryByName("Default");
		} catch (Exception e) {
			log.error("getRootEnvironmentCategory failed");
		}
		return environmentCategory;
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> listChildEnvCategoryNodesInHierarchyinLayers(
			EnvironmentCategory environmentCategory) {
		log.info("listing all child environment category nodes in hierarchy");
		List<EnvironmentCategory> envCategories = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class);
			c.add(Restrictions.gt("leftIndex",
					environmentCategory.getLeftIndex()));
			c.add(Restrictions.lt("rightIndex",
					environmentCategory.getRightIndex()));

			c.addOrder(HibernateCustomOrderByForHierarchicalEntities
					.sqlFormula("(right_index-left_index) desc"));
			c.addOrder(Order.asc("leftIndex"));
			envCategories = c.list();

			log.info("listing child environment category nodes in hierarchy successful");
		} catch (RuntimeException re) {
			log.error(
					"listing child environment category nodes in hierarchy failed",
					re);
		}
		return envCategories;
	}

	@Override
	public List<EnvironmentCombination> getEnvCombinationsForOtherEnvGroups(
			Integer productId, Integer environmentGroupId) {

		log.info(" getEnvCombinationsForOtherEnvGroups for Product Environment ");

		List<EnvironmentCombination> environmentCombinationList = new ArrayList<EnvironmentCombination>();
		List<EnvironmentCombination> otherEnvGroupEnvCombinations = new ArrayList<EnvironmentCombination>();

		try {
			Set<EnvironmentCombination> ecombinationSet = new HashSet<EnvironmentCombination>();
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCombination.class, "ec");
			c.createAlias("ec.productMaster", "product");
			c.add(Restrictions.eq("product.productId", productId));
			environmentCombinationList = c.list();
			if (environmentCombinationList.isEmpty())
				return otherEnvGroupEnvCombinations;
			log.info("No of Env Coombinations : "
					+ environmentCombinationList.size());

			for (EnvironmentCombination environmentCombination : environmentCombinationList) {
				log.info("Env Combination : "
						+ environmentCombination
								.getEnvironmentCombinationName());
				Set<Environment> environments = environmentCombination
						.getEnvironmentSet();
				log.info("Member Envrionments count : " + environments.size());
				boolean hasEnvFromSameEnvGroup = false;
				for (Environment environment : environments) {
					log.info("Member Envrionment : "
							+ environment.getEnvironmentName()
							+ " : "
							+ environment.getEnvironmentCategory()
									.getEnvironmentGroup()
									.getEnvironmentGroupName());
					log.info("Groups Ids : "
							+ environment.getEnvironmentCategory()
									.getEnvironmentGroup()
									.getEnvironmentGroupId() + " : "
							+ environmentGroupId);
					if (environment.getEnvironmentCategory()
							.getEnvironmentGroup().getEnvironmentGroupId() == environmentGroupId) {
						hasEnvFromSameEnvGroup = true;
						log.info("Has Env from same group : "
								+ environment.getEnvironmentName()
								+ " : "
								+ environment.getEnvironmentCategory()
										.getEnvironmentGroup()
										.getEnvironmentGroupName());
						break;
					}
				}
				if (!hasEnvFromSameEnvGroup) {
					otherEnvGroupEnvCombinations.add(environmentCombination);
				}
				Hibernate.initialize(environmentCombination.getProductMaster()
						.getProductType());
			}

			log.info("No of environments from other groups : "
					+ otherEnvGroupEnvCombinations.size());
			log.info("getEnvCombinationsForOtherEnvGroups successful");
		} catch (RuntimeException re) {
			log.error(
					"Unable to get other gorup Env combinations for environment",
					re);
		}
		return otherEnvGroupEnvCombinations;
	}

	@Override
	@Transactional
	public List<Object[]> getEnvironmentDetailsByEnvironmentCombinationId(
			Integer environmentCombinationId) {
		List<Object[]> environmentDetails = null;
		log.info("Environment Combination Id :" + environmentCombinationId);
		try {
			String query = " SELECT env.environmentId,env.environmentName,env.description,envGtgy.environment_category_name,evnGrp.environment_group_name "
					+ " FROM environment_combination_has_environment envCombination "
					+ " LEFT JOIN tfwp_test_environment env ON  (envCombination.environmentId=env.environmentId) "
					+ " LEFT JOIN environment_category envGtgy ON (env.environmentCategoryId=envGtgy.environment_category_id)"
					+ " LEFT JOIN environment_group evnGrp ON (envGtgy.environment_group_id=evnGrp.environment_group_id)"
					+ " WHERE envCombination.environmentCombinationId="
					+ environmentCombinationId;
			environmentDetails = sessionFactory.getCurrentSession()
					.createSQLQuery(query).list();
		} catch (RuntimeException re) {
			log.error(
					"Error in getEnvironmentDetailsByEnvironmentCombinationId",
					re);
		}
		return environmentDetails;
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationAlreadyExist(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer hostId, Integer deviceId, Integer versionId) {
		log.info("isRunConfigurationAlreadyExist");
		RunConfiguration runconfig = null;
		List<RunConfiguration> runconfigList = null;
		try {
			if (hostId != null && deviceId != null) {
				runconfigList = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
										+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId=:deviceId and "
										+ " rc.hostList.hostId=:hostId )")
						.setParameter("ecid", environmentCombinationId)
						.setParameter("trplanId", testRunPlanId)
						.setParameter("deviceId", deviceId)
						.setParameter("hostId", hostId).list();
			} else {
				if (hostId == null && deviceId == null) {
					runconfigList = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
											+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId is null and "
											+ " rc.hostList.hostId is null )")
							.setParameter("ecid", environmentCombinationId)
							.setParameter("trplanId", testRunPlanId).list();
				} else if (hostId == null && deviceId != null) {
					runconfigList = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
											+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId=:deviceId and "
											+ " rc.hostList.hostId is null )")
							.setParameter("ecid", environmentCombinationId)
							.setParameter("trplanId", testRunPlanId)
							.setParameter("deviceId", deviceId).list();
				} else if (hostId != null && deviceId == null) {
					runconfigList = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
											+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId is null and "
											+ " rc.hostList.hostId=:hostId )")
							.setParameter("ecid", environmentCombinationId)
							.setParameter("trplanId", testRunPlanId)
							.setParameter("hostId", hostId).list();
				}
			}

			runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList
					.get(0) : null;
			if (runconfig != null) {
				Hibernate.initialize(runconfig.getTestRunPlanSet());
				Hibernate.initialize(runconfig.getEnvironmentcombination());
				Hibernate.initialize(runconfig.getEnvironmentcombination()
						.getEnvironment_combination_id());
				if (runconfig.getHostList() != null) {
					Hibernate.initialize(runconfig.getHostList());
					Hibernate.initialize(runconfig.getHostList().getHostId());
				}
				if (runconfig.getGenericDevice() != null) {
					GenericDevices gd = runconfig.getGenericDevice();
					Hibernate.initialize(gd);
					if (gd.getHostList() != null) {
						HostList hl = gd.getHostList();
						Hibernate.initialize(hl);
						if (hl.getCommonActiveStatusMaster() != null) {
							CommonActiveStatusMaster casm = hl
									.getCommonActiveStatusMaster();
							Hibernate.initialize(casm);
						}
					}
				}

				if (runconfig.getTestRunPlan() != null) {
					TestRunPlan trunplan = runconfig.getTestRunPlan();
					Hibernate.initialize(trunplan);
					if (trunplan.getProductVersionListMaster() != null) {
						ProductVersionListMaster version = trunplan
								.getProductVersionListMaster();
						Hibernate.initialize(version);
						if (version.getProductMaster() != null) {
							ProductMaster prod = version.getProductMaster();
							Hibernate.initialize(prod);
							if (prod.getProductType() != null) {
								ProductType ptype = prod.getProductType();
								Hibernate.initialize(ptype);
							}
						}
					}
				}
				if (runconfig.getTestRunPlanSet() != null) {
					for (TestRunPlan testRunPlan : runconfig
							.getTestRunPlanSet()) {
						Hibernate.initialize(testRunPlan
								.getProductVersionListMaster());
						if (testRunPlan.getProductVersionListMaster()
								.getProductMaster() != null) {
							Hibernate.initialize(testRunPlan
									.getProductVersionListMaster()
									.getProductMaster());
						}
						Hibernate.initialize(testRunPlan.getExecutionType());
					}
				}

				if (runconfig.getTestTool() != null) {
					Hibernate.initialize(runconfig.getTestTool());
				}

				if (runconfig.getScriptTypeMaster() != null) {
					Hibernate.initialize(runconfig.getScriptTypeMaster());
				}
			}
			return runconfig;
		} catch (RuntimeException re) {
			log.error("isRunConfigurationAlreadyExist Error ", re);
		}
		return runconfig;
	}

	@Override
	@Transactional
	public List<EnvironmentCategory> getEnvironmentCategoryListByGroup() {
		List<EnvironmentCategory> environmentCategoryList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(
					EnvironmentCategory.class, "ec");

			environmentCategoryList = c.list();

			if (environmentCategoryList != null
					&& !environmentCategoryList.isEmpty()) {
				for (EnvironmentCategory environmentCategory : environmentCategoryList) {
					Hibernate.initialize(environmentCategory
							.getEnvironmentGroup());
					Hibernate.initialize(environmentCategory
							.getParentEnvironmentCategory());
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return environmentCategoryList;
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationAlreadyExist(
			Integer environmentCombinationId, Integer testRunPlanId,
			Integer hostId, Integer deviceId, Integer versionId,
			Integer productTypeId) {		
			log.info("isRunConfigurationAlreadyExist");
			RunConfiguration runconfig = null;
			List<RunConfiguration> runconfigList = null;
			try {
				if (hostId != null && deviceId != null) {
					runconfigList = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
											+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId=:deviceId and "
											+ " rc.hostList.hostId=:hostId )"
											+ " and rc.productType.productTypeId=:productTypeId")
							.setParameter("ecid", environmentCombinationId)
							.setParameter("trplanId", testRunPlanId)
							.setParameter("deviceId", deviceId)
							.setParameter("hostId", hostId)
							.setParameter("productTypeId", productTypeId).list();
				} else {
					if (hostId == null && deviceId == null) {
						runconfigList = sessionFactory
								.getCurrentSession()
								.createQuery(
										"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
												+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId is null and "
												+ " rc.hostList.hostId is null )")
								.setParameter("ecid", environmentCombinationId)
								.setParameter("trplanId", testRunPlanId).list();
					} else if (hostId == null && deviceId != null) {
						runconfigList = sessionFactory
								.getCurrentSession()
								.createQuery(
										"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
												+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId=:deviceId and "
												+ " rc.hostList.hostId is null )")
								.setParameter("ecid", environmentCombinationId)
								.setParameter("trplanId", testRunPlanId)
								.setParameter("deviceId", deviceId).list();
					} else if (hostId != null && deviceId == null) {
						runconfigList = sessionFactory
								.getCurrentSession()
								.createQuery(
										"from RunConfiguration rc where rc.environmentcombination.environment_combination_id=:ecid and "
												+ " rc.testRunPlan.testRunPlanId=:trplanId and  ( rc.genericDevice.genericsDevicesId is null and "
												+ " rc.hostList.hostId=:hostId )"
												+ " and rc.productType.productTypeId=:productTypeId")
								.setParameter("ecid", environmentCombinationId)
								.setParameter("trplanId", testRunPlanId)
								.setParameter("hostId", hostId)
								.setParameter("productTypeId", productTypeId).list();
					}
				}

				runconfig = (runconfigList != null && runconfigList.size() != 0) ? (RunConfiguration) runconfigList
						.get(0) : null;
				if (runconfig != null) {
					Hibernate.initialize(runconfig.getTestRunPlanSet());
					Hibernate.initialize(runconfig.getEnvironmentcombination());
					Hibernate.initialize(runconfig.getEnvironmentcombination()
							.getEnvironment_combination_id());
					if (runconfig.getHostList() != null) {
						Hibernate.initialize(runconfig.getHostList());
						Hibernate.initialize(runconfig.getHostList().getHostId());
					}
					if (runconfig.getGenericDevice() != null) {
						GenericDevices gd = runconfig.getGenericDevice();
						Hibernate.initialize(gd);
						if (gd.getHostList() != null) {
							HostList hl = gd.getHostList();
							Hibernate.initialize(hl);
							if (hl.getCommonActiveStatusMaster() != null) {
								CommonActiveStatusMaster casm = hl
										.getCommonActiveStatusMaster();
								Hibernate.initialize(casm);
							}
						}
					}

					if (runconfig.getTestRunPlan() != null) {
						TestRunPlan trunplan = runconfig.getTestRunPlan();
						Hibernate.initialize(trunplan);
						if (trunplan.getProductVersionListMaster() != null) {
							ProductVersionListMaster version = trunplan
									.getProductVersionListMaster();
							Hibernate.initialize(version);
							if (version.getProductMaster() != null) {
								ProductMaster prod = version.getProductMaster();
								Hibernate.initialize(prod);
								if (prod.getProductType() != null) {
									ProductType ptype = prod.getProductType();
									Hibernate.initialize(ptype);
								}
							}
						}
					}
					if (runconfig.getTestRunPlanSet() != null) {
						for (TestRunPlan testRunPlan : runconfig
								.getTestRunPlanSet()) {
							Hibernate.initialize(testRunPlan
									.getProductVersionListMaster());
							if (testRunPlan.getProductVersionListMaster()
									.getProductMaster() != null) {
								Hibernate.initialize(testRunPlan
										.getProductVersionListMaster()
										.getProductMaster());
							}
							Hibernate.initialize(testRunPlan.getExecutionType());
						}
					}

					if (runconfig.getTestTool() != null) {
						Hibernate.initialize(runconfig.getTestTool());
					}

					if (runconfig.getScriptTypeMaster() != null) {
						Hibernate.initialize(runconfig.getScriptTypeMaster());
					}
				}
				return runconfig;
			} catch (RuntimeException re) {
				log.error("isRunConfigurationAlreadyExist Error ", re);
			}
			return runconfig;
		}
}
