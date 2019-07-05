/**
 * 
 */
package com.hcl.ilcm.workflow.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseList;

/**
 * @author silambarasur
 *
 */
public interface WorkflowTestcasesDAO {
	List<TestCaseList> listActivityTestcasesforWorkFlow(Integer productId,List<String> testCaseStatusList, Integer jtStartIndex, Integer jtPageSize);
	List<TestCaseList> listActivityTestcasesforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize);
	List<TestCaseList> listTescaseforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> testCaseIds,Integer jtStartIndex, Integer jtPageSize);
}
