package com.hcl.atf.taf.util;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.RuntimeEnvironment;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.UserGroupCallback;

public class JBPMUtil {

	private static RuntimeManager runtimeManager;
	private static RuntimeEngine runtime;
	private static KieSession ksession;
	
	
	public static RuntimeManager SingletonRuntimeManager(String process) {
		 UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl("usergroup.properties");
	        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.getDefault()
	        		.userGroupCallback(new JBossUserGroupCallbackImpl("usergroup.properties"))
	           .userGroupCallback(userGroupCallback)
	            .addAsset(ResourceFactory.newClassPathResource(process), ResourceType.BPMN2)
	            .get();
	        runtimeManager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
	        return runtimeManager;
	}
	
	
	
	public static KieSession getSingletonSession( ){
		 runtime = runtimeManager.getRuntimeEngine(EmptyContext.get());  
		 ksession = runtime.getKieSession();
		return ksession;	
	}
	
	
	public static TaskService getTaskService(){
		System.getProperties().put("http.proxyHost", "10.108.66.161");
		System.out.println("Hardcoded ip address...");
		return runtime.getTaskService();
	}
}
