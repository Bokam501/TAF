package com.hcl.atf.taf.scriptgeneration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.service.TestCaseService;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

public class TestCaseSkeletonScriptGenerator {	 

	@Autowired
	private TestCaseService testCaseService;
	
	private static final Log log = LogFactory.getLog(TestCaseSkeletonScriptGenerator.class);

	private static String DEFAULT_PACKAGE_NAME = "com.hcl.atf.taf.testcase"; 
	private static String DEFAULT_PACKAGE_NAME_FOLDER = File.separator + "com" + File.separator + "hcl" + File.separator + "atf" + File.separator + "taf" + File.separator + "testcase";
	private static String MAIN_PACKAGE_NAME = "com.hcl.atf.taf"; 
	private static String MAIN_PACKAGE_NAME_FOLDER = File.separator + "com" + File.separator + "hcl" + File.separator + "atf" + File.separator + "taf";
	
	public String generateTestCasesSkeletonCode(List<TestCaseList> allTestCases, ProductMaster product, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine, String testCaseOption) {
		
		if (product == null) {
			log.info("Test Case Ref Source Code Generation : Unable to generate source code as product is null");
			return "Failed : Test Case Ref Source Code Generation : Unable to generate source code as product is null";
		}
		
		if (allTestCases == null || allTestCases.size() < 1) {
			log.info("Test Case Ref Source Code Generation : Unable to generate source code as no test cases are available");
			return "Failed : Test Case Ref Source Code Generation : Unable to generate source code as no test cases are available";
		}

		String message = null;
		if (packageName == null)
			packageName = DEFAULT_PACKAGE_NAME;
		if (destinationDirectory == null)
			return "Failed : Unable to generate skeleton script as Destination directory is not specified";
		if (testCaseOption == null || testCaseOption.isEmpty())
			testCaseOption = "SINGLE_METHOD";
		
		if (scriptType == null || scriptType.trim().length()< 1) 
			scriptType = TAFConstants.TESTSCRIPT_TYPE_JUNIT;
		
		if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_JUNIT)) {
			message = generateJUnitTestCasesSkeletonCode(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine, testCaseOption);
		} else if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_TESTNG)) {
			message = generateTestNGTestCasesSkeletonCode(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine, testCaseOption);
		} else {
			message = generateJUnitTestCasesSkeletonCode(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine, testCaseOption);
		}
		return message;
	}
	

	private String generateJUnitTestCasesSkeletonCode(List<TestCaseList> testCases, ProductMaster product, String packageName, String refClassName, String destinationDirectory, int nameSource, String testExecutionEngine, String testStepOption) {

		//Now process all the test cases to generate code
		JCodeModel codeModel = new JCodeModel();
		String message = null;
		try {
			if (testExecutionEngine == null)
				testExecutionEngine = TAFConstants.TESTENGINE_SEETEST;
			log.info("Test Execution Engine : " + testExecutionEngine);
				
			JClass junitAfterReference = null;
			JClass junitBeforeReference = null;
			JClass junitTestReference = null;
			JClass junitFixMethodOrderReference = null;
			JClass junitMethodSortersReference = null;
			junitAfterReference = codeModel.ref("org.junit.After");
			junitBeforeReference = codeModel.ref("org.junit.Before");
			junitTestReference = codeModel.ref("org.junit.Test");
			junitFixMethodOrderReference = codeModel.ref("org.junit.FixMethodOrder");
			junitMethodSortersReference = codeModel.ref("org.junit.runners.MethodSorters");

			for (TestCaseList testCase : testCases) {

				// 1. Create the class for the test case
				String testCaseName = null;
				String classPackageName = null;
				if (testCase.getTestCaseScriptFileName() == null || testCase.getTestCaseScriptFileName().trim().isEmpty()) {
					testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource);
				} else {
					testCaseName = testCase.getTestCaseScriptFileName();
					if(testCaseName.contains(".java")){
						testCaseName = testCaseName.replace(testCaseName.substring(testCaseName.lastIndexOf(".")),"");
					}
				}
				if (testCase.getTestCaseScriptQualifiedName() == null || testCase.getTestCaseScriptQualifiedName().trim().isEmpty()) {
					classPackageName = packageName;
				} else {
					classPackageName = testCase.getTestCaseScriptQualifiedName();
				}
				JDefinedClass testCaseClass = codeModel._class(classPackageName + "." + testCaseName);
				JDocComment classComment = testCaseClass.javadoc();
				String javadocCommentClass = "Automation Script for Testcase : " + testCase.getTestCaseName()
						+ "\n" + "Product 			: " + product.getProductDescription()
						+ "\n" + "Testcase ID		: " + testCase.getTestCaseId()
						+ "\n" + "Testcase Code  	: " + testCase.getTestCaseCode()
						+ "\n" + "Description 		: " + testCase.getTestCaseDescription()
						+ "\n" + "Test case type 	: " + testCase.getTestCaseType()
						+ "\n" + "Script Type 		: " + TAFConstants.TESTSCRIPT_TYPE_JUNIT
						+ "\n" + "Test Execution Engine 	: " + testExecutionEngine
						+ "\n" + "Code generated by TAF on 	: " + new Date(System.currentTimeMillis());
				classComment.append(javadocCommentClass);
				testCaseClass.annotate(junitFixMethodOrderReference).param("value", MethodSorters.NAME_ASCENDING);

				testCaseClass = addClassVariablesForTestCaseClass(codeModel, testCaseClass, testExecutionEngine);
				
				
				if (testStepOption == null)
					testStepOption = "SINGLE_METHOD";
				// 4. Add methods for the test steps of the test case
				// 4.a Add the setup and tear down methods
				JMethod setUpMethod = testCaseClass.method(JMod.PUBLIC, void.class, "setUp");
				JDocComment methodComment1 = setUpMethod.javadoc();
				String commentString = "Setup method for testcase";
				methodComment1.append(commentString);
				setUpMethod = constructSetUpMethodForTestCase(setUpMethod, testExecutionEngine, refClassName, testCaseName);
				setUpMethod.annotate(junitBeforeReference);
				
				JMethod tearDownMethod = testCaseClass.method(JMod.PUBLIC, void.class, "tearDown");
				JDocComment methodComment2 = tearDownMethod.javadoc();
				commentString = "Teardown method for the testcase";
				methodComment2.append(commentString);
				tearDownMethod = constructTearDownMethodForTestCase(tearDownMethod, testExecutionEngine);
				tearDownMethod.annotate(junitAfterReference);
				
				List<TestCaseStepsList> testSteps = testCaseService.listTestCaseSteps(testCase.getTestCaseId());
				if (testSteps == null || testSteps.isEmpty()) {
					log.debug("No steps in the testcase : " + testCaseName);
					JMethod testCaseMethod = testCaseClass.method(JMod.PUBLIC, void.class, testCaseName + "_Test");
					testCaseMethod.annotate(junitTestReference);
					testCaseMethod = constructDefaultTestCaseMethodForTestCase(testCaseMethod, testExecutionEngine, testCase);
				} else {
					if(testStepOption.contains("SEPARATE_METHOD")) {
						JMethod mainTestStepMethod = testCaseClass.method(JMod.PUBLIC, void.class, "mainTest");
						mainTestStepMethod.annotate(junitTestReference);
						String stepMethod = "";
						for (TestCaseStepsList testStep : testSteps) {						
							String testStepName = ScriptGeneratorUtilities.getTestStepMethodName(testCaseName, testStep.getTestStepName(), testStep.getTestStepId(), testStep.getTestStepCode(), 1 );
							JMethod testStepMethod = testCaseClass.method(JMod.PUBLIC, void.class, testStepName);
							testStepMethod = constructTestStepMethodForTestCase(testStepMethod, testExecutionEngine, refClassName, testCaseName, testStepName, testStep);
							stepMethod = testStepName+"();";
							mainTestStepMethod.body().directStatement(stepMethod);
							stepMethod = "";
						}
					} else {
						
						JMethod singleTestStepMethod = testCaseClass.method(JMod.PUBLIC, void.class, testCaseName + "_Test");
						singleTestStepMethod.annotate(junitTestReference);
						for (TestCaseStepsList testStep : testSteps) {
							
							String testStepName = ScriptGeneratorUtilities.getTestStepMethodName(testCaseName, testStep.getTestStepName(), testStep.getTestStepId(), testStep.getTestStepCode(), 1 );
							singleTestStepMethod = addTestStepForSingleTestStepMethodForTestCase(singleTestStepMethod, testExecutionEngine, refClassName, testCaseName, testStepName, testStep);
						}
					}
				}
			
				File sourceFile = new File(destinationDirectory);
				if (!sourceFile.exists()) {
					log.info("Created Directory for source code generation : " + destinationDirectory);
					sourceFile.mkdirs();
				}
				codeModel.build(sourceFile);
				String CLASS_PACKAGE_NAME_FOLDER = classPackageName.replace(".", File.separator);
				log.info("CLASS_PACKAGE_NAME_FOLDER : " + CLASS_PACKAGE_NAME_FOLDER);
				message = sourceFile.getAbsolutePath() + File.separator + CLASS_PACKAGE_NAME_FOLDER + File.separator + testCaseName + ".java";
			}
		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			message = "Failed : Unable to generate testcase ref source code class";
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			message = "Failed : Unable to create file in file system";
		}
		
		log.debug("Success : Generated Testcases source code framework." );
		return message;

	}

	private String generateTestNGTestCasesSkeletonCode(List<TestCaseList> testCases, ProductMaster product, String packageName, String refClassName, String destinationDirectory, int nameSource, String testExecutionEngine, String testStepOption) {

		//Now process all the test cases to generate code
		JCodeModel codeModel = new JCodeModel();
		String message = null;
		try {
			if (testExecutionEngine == null)
				testExecutionEngine = TAFConstants.TESTENGINE_SEETEST;
			log.info("Test Execution Engine : " + testExecutionEngine);
				
			JClass testNGAfterMethodReference = null;
			JClass testNGBeforeMethodReference = null;
			JClass testNGTestReference = null;
			testNGAfterMethodReference = codeModel.ref("org.testng.annotations.AfterMethod");
			testNGBeforeMethodReference = codeModel.ref("org.testng.annotations.BeforeMethod");
			testNGTestReference = codeModel.ref("org.testng.annotations.Test");

			for (TestCaseList testCase : testCases) {

				// 1. Create the class for the test case
				//String testCaseName = getTestCaseClassName(testCase, nameSource);
				//String testCaseName = null;
				String testCaseName = null;
				String classPackageName = null;
				if (testCase.getTestCaseScriptFileName() == null || testCase.getTestCaseScriptFileName().trim().isEmpty()) {
					testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource);
				} else {
					testCaseName = testCase.getTestCaseScriptFileName();
					if(testCaseName.contains(".java")){
						testCaseName = testCaseName.replace(testCaseName.substring(testCaseName.lastIndexOf(".")),"");
					}
				}
				if (testCase.getTestCaseScriptQualifiedName() == null || testCase.getTestCaseScriptQualifiedName().trim().isEmpty()) {
					classPackageName = packageName;
				} else {
					classPackageName = testCase.getTestCaseScriptQualifiedName();
				}
				JDefinedClass testCaseClass = codeModel._class(classPackageName + "." + testCaseName);
				JDocComment classComment = testCaseClass.javadoc();
				String javadocCommentClass = "Automation Script for Testcase : " + testCase.getTestCaseName()
						+ "\n" + "Product : " + product.getProductDescription()
						+ "\n" + "Testcase ID 		: " + testCase.getTestCaseId()
						+ "\n" + "Testcase Code		: " + testCase.getTestCaseCode()
						+ "\n" + "Description 		: " + testCase.getTestCaseDescription()
						+ "\n" + "Test case type 	: " + testCase.getTestCaseType()
						+ "\n" + "Script Type 		: " + TAFConstants.TESTSCRIPT_TYPE_TESTNG
						+ "\n" + "Test Execution Engine 	: " + testExecutionEngine
						+ "\n" + "Code generated by TAF on 	: " + new Date(System.currentTimeMillis());
				classComment.append(javadocCommentClass);
				
				testCaseClass = addClassVariablesForTestCaseClass(codeModel, testCaseClass, testExecutionEngine);
				
				// 4.a Add the setup method
				JMethod setUpMethod = testCaseClass.method(JMod.PUBLIC, void.class, "setUp");
				JDocComment methodComment1 = setUpMethod.javadoc();
				String commentString = "Setup method for testcase";
				methodComment1.append(commentString);
				setUpMethod = constructSetUpMethodForTestCase(setUpMethod, testExecutionEngine, refClassName, testCaseName);
				setUpMethod.annotate(testNGBeforeMethodReference);
				
				JMethod tearDownMethod = testCaseClass.method(JMod.PUBLIC, void.class, "tearDown");
				JDocComment methodComment2 = tearDownMethod.javadoc();
				commentString = "Teardown method for the testcase";
				methodComment2.append(commentString);
				tearDownMethod = constructTearDownMethodForTestCase(setUpMethod, testExecutionEngine);
				tearDownMethod.annotate(testNGAfterMethodReference);
				
				if (testStepOption == null)
					testStepOption = "SINGLE_METHOD";
				List<TestCaseStepsList> testSteps = testCaseService.listTestCaseSteps(testCase.getTestCaseId());
				if (testSteps == null || testSteps.isEmpty()) {
					log.debug("No steps in the testcase : " + testCaseName);
					JMethod testCaseMethod = testCaseClass.method(JMod.PUBLIC, void.class, testCaseName + "_Test");
					testCaseMethod.annotate(testNGTestReference);
					testCaseMethod = constructDefaultTestCaseMethodForTestCase(testCaseMethod, testExecutionEngine, testCase);
				} else {
					if(testStepOption.contains("SEPARATE_METHOD")) {
						for (TestCaseStepsList testStep : testSteps) {
						
							String testStepName = ScriptGeneratorUtilities.getTestStepMethodName(testCaseName, testStep.getTestStepName(), testStep.getTestStepId(), testStep.getTestStepCode(), 1 );
							JMethod testStepMethod = testCaseClass.method(JMod.PUBLIC, void.class, testStepName);
							testStepMethod.annotate(testNGTestReference);
							testStepMethod = constructTestStepMethodForTestCase(testStepMethod, testExecutionEngine, refClassName, testCaseName, testStepName, testStep);
						}
					} else {
						
						JMethod singleTestStepMethod = testCaseClass.method(JMod.PUBLIC, void.class, testCaseName + "_Test");
						singleTestStepMethod.annotate(testNGTestReference);
						for (TestCaseStepsList testStep : testSteps) {
							
							String testStepName = ScriptGeneratorUtilities.getTestStepMethodName(testCaseName, testStep.getTestStepName(), testStep.getTestStepId(), testStep.getTestStepCode(), 1 );
							singleTestStepMethod = addTestStepForSingleTestStepMethodForTestCase(singleTestStepMethod, testExecutionEngine, refClassName, testCaseName, testStepName, testStep);
						}
					}
				}
				
				File sourceFile = new File(destinationDirectory);
				if (!sourceFile.exists()) {
					log.info("Created Directory for source code generation : " + destinationDirectory);
					sourceFile.mkdirs();
				}
				codeModel.build(sourceFile);
				message = sourceFile.getAbsolutePath() + DEFAULT_PACKAGE_NAME_FOLDER + File.separator + testCaseName + ".java";
			}
		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			message = "Failed : Unable to generate testcase ref source code class";
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			message =  "Failed : Unable to create file in file system";
		}
		
		log.debug("Success : Generated Testcases source code framework." );
		return message;

	}
	

	public String generateMainClass(List<TestCaseList> allTestCases, ProductMaster product, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine) {
		
		if (product == null) {
			log.info("Test Case Ref Source Code Generation : Unable to generate Main Class as product is null");
			return "Failed : Test Case Ref Source Code Generation : Unable to generate Main class as product is null";
		}
		
		if (allTestCases == null || allTestCases.size() < 1) {
			log.info("Test Case Ref Source Code Generation : Unable to generate Main class as no test cases are available");
			return "Failed : Test Case Ref Source Code Generation : Unable to generate Main class as no test cases are available";
		}

		String message = null;
		packageName = MAIN_PACKAGE_NAME;
		if (destinationDirectory == null)
			return "Failed : Unable to generate skeleton script as Destination directory is not specified";

		if (scriptType == null || scriptType.trim().length()< 1) 
			scriptType = TAFConstants.TESTSCRIPT_TYPE_JUNIT;
		
		if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_JUNIT)) {
			message = generateJUnitMainClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
			generateSelectiveTestCaseClass(product, executionEngine, packageName, destinationDirectory, nameSource);
			generateJUnitSetUpClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
			generateJUnitTearDownClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
		} else if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_TESTNG)) {			
			message = generateTestNGMainClass(product, executionEngine, packageName, destinationDirectory, nameSource);
			generateSelectiveTestCaseClass(product, executionEngine, packageName, destinationDirectory, nameSource);
		} else {
			message = generateJUnitMainClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
			generateJUnitSetUpClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
			generateJUnitTearDownClass(allTestCases, product, executionEngine, packageName, destinationDirectory, nameSource);
		}
		return message;
	}
	
	private String generateTestNGMainClass(ProductMaster product, String testExecutionEngine, String packageName, String destinationDirectory, int nameSource) {
		
		//Now process all the test cases to generate code
		JCodeModel codeModel = new JCodeModel();
		String message = null;
		try {
			if (testExecutionEngine == null)
				testExecutionEngine = TAFConstants.TESTENGINE_SEETEST;
			log.info("Test Execution Engine : " + testExecutionEngine);
				
			JClass testNGAfterSuiteReference = null;
			JClass testNGBeforeSuiteReference = null;
			testNGAfterSuiteReference = codeModel.ref("org.testng.annotations.AfterSuite");
			testNGBeforeSuiteReference = codeModel.ref("org.testng.annotations.BeforeSuite");


			// 1. Create the class for the main class
			JDefinedClass mainClass = codeModel._class(packageName + "." + "Main");
			JDocComment classComment = mainClass.javadoc();
			String javadocCommentClass = "Main driver class for integration of scripts with  TAF"
						+ "\n" + "Product : " + product.getProductDescription()
						+ "\n" + "Script Type 		: " + TAFConstants.TESTSCRIPT_TYPE_TESTNG
						+ "\n" + "Test Execution Engine 	: " + testExecutionEngine
						+ "\n" + "Code generated by TAF on 	: " + new Date(System.currentTimeMillis());
			classComment.append(javadocCommentClass);

			//Add the class variables
			mainClass = addClassVariablesForMainClass(codeModel, mainClass, testExecutionEngine);
		
			//Add the setup method
			JMethod setUpMethod = mainClass.method(JMod.PUBLIC, void.class, "setUp");
			JDocComment methodComment1 = setUpMethod.javadoc();
			String commentString = "Setup method for the TestSuite/TestRun";
			methodComment1.append(commentString);
			setUpMethod = constructSetUpMethodForMain(setUpMethod, testExecutionEngine);
			setUpMethod.annotate(testNGBeforeSuiteReference);
				
			//Add the SeeTest Initialization method
			if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
				JMethod initSeeTestClientMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(com.experitest.client.Client.class), "initSeeTestClient");
				initSeeTestClientMethod = constructSeeTestInitMethodForMain(initSeeTestClientMethod, testExecutionEngine, product);
			}

			//Add the teardown method
			JMethod tearDownMethod = mainClass.method(JMod.PUBLIC, void.class, "tearDown");
			JDocComment methodComment2 = tearDownMethod.javadoc();
			commentString = "Teardown method for the TestSuite/TestRun";
			methodComment2.append(commentString);
			tearDownMethod = constructTearDownMethodForMain(tearDownMethod, testExecutionEngine, product);
			tearDownMethod.annotate(testNGAfterSuiteReference);
				
			// Add accessor methods			
			mainClass = constructAccessorMethodsForMain(codeModel, mainClass, testExecutionEngine, product);
			
			File sourceFile = new File(destinationDirectory);
			if (!sourceFile.exists()) {
				log.info("Created Directory for source code generation : " + destinationDirectory);
				sourceFile.mkdirs();
			}
			codeModel.build(sourceFile);
			message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator +"Main.java";

		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			message =  "Failed : Unable to generate testcase ref source code class";
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			message = "Failed : Unable to create file in file system";
		}
		
		log.info("Success : Generated Main class source code" + message);
		return message;
	}

	private String generateJUnitMainClass(List<TestCaseList> allTestCases, ProductMaster product, String testExecutionEngine, String packageName, String destinationDirectory, int nameSource) {
		
		//Now process all the test cases to generate code
		JCodeModel codeModel = new JCodeModel();
		String message = null;
		try {
			if (testExecutionEngine == null)
				testExecutionEngine = TAFConstants.TESTENGINE_SEETEST;
			log.info("Test Execution Engine : " + testExecutionEngine);
				
			// JUnit References
			JClass runWithReference = null;
			JClass suiteReference = null;
			JClass suiteClassesReference = null;
			runWithReference = codeModel.ref("org.junit.runner.RunWith");
			suiteReference = codeModel.ref("org.junit.runners.Suite");			
			suiteClassesReference = codeModel.ref("org.junit.runners.Suite.SuiteClasses");
			// 1. Create the class for the main class
			JDefinedClass mainClass = codeModel._class(packageName + "." + "Main");
			JDocComment classComment = mainClass.javadoc();
			String javadocCommentClass = "Main driver class for integration of scripts with  TAF"
						+ "\n" + "Product : " + product.getProductDescription()
						+ "\n" + "Script Type 		: " + TAFConstants.TESTSCRIPT_TYPE_JUNIT
						+ "\n" + "Test Execution Engine 	: " + testExecutionEngine
						+ "\n" + "Code generated by TAF on 	: " + new Date(System.currentTimeMillis());
			classComment.append(javadocCommentClass);
			// 2. Add the required import statements and the class variables
			// 2.a TAF Lib references for import
			mainClass = addClassVariablesForMainClass(codeModel, mainClass, testExecutionEngine);
			//Annotate for the Suite execution
			mainClass.annotate(runWithReference).param("value", suiteReference);
			

			
			StringBuffer defaultSuiteClassesSB = new StringBuffer();		
			defaultSuiteClassesSB.append("{SetUp.class, ");
			for (TestCaseList testCase : allTestCases) {
				defaultSuiteClassesSB.append(ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource).concat(".class"));
				defaultSuiteClassesSB.append(", ");
			}
			defaultSuiteClassesSB.append("TearDown.class}");			
			mainClass.annotate(suiteClassesReference).param("value", defaultSuiteClassesSB.toString());
			
			
			StringBuffer importClassStatements = new StringBuffer();

			StringBuffer suiteClassesSB = new StringBuffer();
			suiteClassesSB.append("@SuiteClasses(");
			suiteClassesSB.append("{SetUp.class, ");
			for (TestCaseList testCase : allTestCases) {
				
				String classPackageName;
			
				if (testCase.getTestCaseScriptQualifiedName() == null || testCase.getTestCaseScriptQualifiedName().trim().isEmpty()) {
					classPackageName = packageName;
				} else {
					classPackageName = testCase.getTestCaseScriptQualifiedName();
				}

				importClassStatements.append("import " + classPackageName.trim() + "."+ ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource) + ";\n");
				suiteClassesSB.append(ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource).concat(".class"));
				suiteClassesSB.append(", ");
			}
			suiteClassesSB.append("TearDown.class})");			
			log.info(importClassStatements.toString());
			log.info(suiteClassesSB.toString());

			// Add the setup method
			JMethod setUpMethod = mainClass.method(JMod.PUBLIC, void.class, "setUp");
			JDocComment methodComment1 = setUpMethod.javadoc();
			String commentString = "Setup method for the TestSuite/TestRun";
			methodComment1.append(commentString);
			setUpMethod = constructSetUpMethodForMain(setUpMethod, testExecutionEngine);

			//Add seetest initialization method
			if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
				JMethod initSeeTestClientMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(com.experitest.client.Client.class), "initSeeTestClient");
				initSeeTestClientMethod = constructSeeTestInitMethodForMain(initSeeTestClientMethod, testExecutionEngine, product);
			}

			//Add tear down method
			JMethod tearDownMethod = mainClass.method(JMod.PUBLIC, void.class, "tearDown");
			JDocComment methodComment2 = tearDownMethod.javadoc();
			commentString = "Teardown method for the TestSuite/TestRun";
			methodComment2.append(commentString);
			tearDownMethod = constructTearDownMethodForMain(tearDownMethod, testExecutionEngine, product);
			
			JMethod mainMethod = mainClass.method(JMod.PUBLIC | JMod.STATIC, mainClass, "getMain");
			mainMethod.body().directStatement("  if(main==null)main=new Main();                 ");
			mainMethod.body().directStatement("		return main;");
			
			// Add accessor methods			
			mainClass = constructAccessorMethodsForMain(codeModel, mainClass, testExecutionEngine, product);
			
			File sourceFile = new File(destinationDirectory);
			if (!sourceFile.exists()) {
				log.info("Created Directory for source code generation : " + destinationDirectory);
				sourceFile.mkdirs();
			}
			codeModel.build(sourceFile);
			message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator +"Main.java";
			ScriptGeneratorUtilities.insertTestCaseImportsForJavaScripts(message, importClassStatements.toString(), suiteClassesSB.toString(),"");

		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			return "Failed : Unable to generate testcase ref source code class";
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			return "Failed : Unable to create file in file system";
		}
		log.debug("Success : Generated Testcases source code framework." );
		return message;
	}
	
	private JDefinedClass addClassVariablesForMainClass (JCodeModel codeModel, JDefinedClass mainClass, String testExecutionEngine) { 
	
		JClass runWithReference = null;
		JClass suiteReference = null;
		JClass suiteClassesReference = null;
		JClass webDriverReference = null;
		JClass InternetExplorerDriverReference = null;
		JClass FirefoxDriverReference = null;
		JClass ChromeDriverReference = null;
		JClass safariDriverReference = null;
		JClass evidenceCaptureReference = null;
		JClass seeTestClientReference = null;
		JClass evidenceCreationReference = null;

		JClass logTypeReference = null;
		JClass loggingPrefReference = null;
		JClass capabilityTypeReference = null;
		JClass desiredCapabilitiesReference = null;
		JClass fileReference = codeModel.directClass("java.io.File");
		JClass timeUnitReference = codeModel.directClass("java.util.concurrent.TimeUnit");
		JClass loggingLevelReference = codeModel.directClass("java.util.logging.Level");
		JClass consoleWriterReference = codeModel.ref("com.hcl.atf.taf.ConsoleWriter");		
		JClass mapReference = codeModel.ref("java.util.Map");

		evidenceCreationReference = codeModel.ref("com.hcl.atf.taf.util.EvidenceCreation");
		runWithReference = codeModel.ref("org.junit.runner.RunWith");
		suiteReference = codeModel.ref("org.junit.runners.Suite");			
		suiteClassesReference = codeModel.ref("org.junit.runners.Suite.SuiteClasses");			
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			webDriverReference = codeModel.ref("org.openqa.selenium.WebDriver");	
			evidenceCaptureReference = codeModel.ref("com.hcl.atf.taf.seleniumUtilities.EvidenceCapture");
			InternetExplorerDriverReference = codeModel.directClass("org.openqa.selenium.ie.InternetExplorerDriver");
			FirefoxDriverReference = codeModel.directClass("org.openqa.selenium.firefox.FirefoxDriver");
			ChromeDriverReference = codeModel.directClass("org.openqa.selenium.chrome.ChromeDriver");
			safariDriverReference = codeModel.directClass("org.openqa.selenium.safari.SafariDriver");
			
			logTypeReference = codeModel.directClass("org.openqa.selenium.logging.LogType");
			loggingPrefReference = codeModel.directClass("org.openqa.selenium.logging.LoggingPreferences");
			capabilityTypeReference = codeModel.directClass("org.openqa.selenium.remote.CapabilityType");
			desiredCapabilitiesReference = codeModel.directClass("org.openqa.selenium.remote.DesiredCapabilities");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			seeTestClientReference = codeModel.ref("com.experitest.client.Client");
			evidenceCaptureReference = codeModel.ref("com.hcl.atf.taf.seeTestUtilities.EvidenceCapture");
		} 		
		
		// 2.d Add standard reference variables
		JFieldVar projectPath = mainClass.field(20, codeModel.ref(java.lang.String.class), "projectPath");
		JFieldVar evidencePath = mainClass.field(20, codeModel.ref(java.lang.String.class), "evidencePath");
		JFieldVar runListId = mainClass.field(20, codeModel.ref(java.lang.String.class), "runListId");
		JFieldVar hostId = mainClass.field(20, codeModel.ref(java.lang.String.class), "hostId");
		JFieldVar caps = mainClass.field(JMod.NONE, codeModel.ref(java.lang.String.class), "caps");
		JFieldVar logPrefs = mainClass.field(JMod.NONE, codeModel.ref(java.lang.String.class), "logPrefs");
		JFieldVar main = mainClass.field(20, mainClass, "main");
		JFieldVar fileVar = mainClass.field(20, codeModel.ref(java.io.File.class), "file");
		JFieldVar evidenceCreationVar = mainClass.field(20, evidenceCreationReference, "evidenceCreation");
		JFieldVar mapRef = mainClass.field(20, mapReference.narrow(String.class, Object.class), "map");
		

		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			JFieldVar devicePlatform = mainClass.field(20, codeModel.ref(java.lang.String.class), "devicePlatform");
			JFieldVar webURL = mainClass.field(20, codeModel.ref(java.lang.String.class), "webURL");
			JFieldVar driver = mainClass.field(JMod.STATIC, webDriverReference, "driver");
			JFieldVar webDriverVar = mainClass.field(20, webDriverReference, "webDriver");
			JFieldVar InternetExplorerDriverVar = mainClass.field(20, InternetExplorerDriverReference, "InternetExplorerDriver");
			JFieldVar FirefoxDriverVar = mainClass.field(20, FirefoxDriverReference, "FirefoxDriver");
			JFieldVar ChromeDriverVar = mainClass.field(20, ChromeDriverReference, "ChromeDriver");
			JFieldVar safariDriverVar = mainClass.field(20, safariDriverReference, "safariDriver");
			JFieldVar logTypeVar = mainClass.field(20, logTypeReference, "logType");
			JFieldVar loggingPrefVar = mainClass.field(20, loggingPrefReference, "loggingPref");
			JFieldVar capabilityTypeVar = mainClass.field(20, capabilityTypeReference, "capabilityType");
			JFieldVar desiredCapabilitiesVar = mainClass.field(20, desiredCapabilitiesReference, "desiredCapabilities");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			JFieldVar seeTestClient = mainClass.field(20, seeTestClientReference, "client");
			JFieldVar seeTestPort = mainClass.field(20, codeModel.ref(java.lang.Integer.class), "seeTestPort");
			JFieldVar deviceId = mainClass.field(20, codeModel.ref(java.lang.String.class), "deviceId");
			JFieldVar deviceModel = mainClass.field(20, codeModel.ref(java.lang.String.class), "deviceModel");
			JFieldVar devicePlatform = mainClass.field(20, codeModel.ref(java.lang.String.class), "devicePlatform");
			JFieldVar scriptName = mainClass.field(20, codeModel.ref(java.lang.String.class), "scriptName");
			JFieldVar devicePort = mainClass.field(20, codeModel.ref(java.lang.String.class), "devicePort");
		}		
		return mainClass;
	}
	
	private JMethod constructSetUpMethodForMain(JMethod setUpMethod, String testExecutionEngine) {

		setUpMethod.body().directStatement("                          ");
		setUpMethod.body().directStatement("try {");
		
		setUpMethod.body().directStatement("	devicePlatform = System.getProperty(\"devicePlatform\");");
		
		setUpMethod.body().directStatement("	scriptName = System.getProperty(\"scriptName\");");
		setUpMethod.body().directStatement("	devicePort = System.getProperty(\"devicePort\");");
		
		setUpMethod.body().directStatement("	evidencePath=System.getProperty(\"EVIDENCE_PATH\");");
		setUpMethod.body().directStatement("	runListId=System.getProperty(\"TESTRUNLIST_ID\");");
		setUpMethod.body().directStatement("	EvidenceCreation.setdefaultTafEvidencePath(evidencePath);");
		setUpMethod.body().directStatement("	EvidenceCreation.setrunListId(runListId);");
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			setUpMethod.body().directStatement("	hostId=System.getProperty(\"hostId\");");
			setUpMethod.body().directStatement("	webURL=System.getProperty(\"webAppURL\");");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			setUpMethod.body().directStatement("	deviceId = System.getProperty(\"deviceId\");");
			setUpMethod.body().directStatement("	EvidenceCreation.setdeviceId(deviceId);");
		}		
	
		setUpMethod.body().directStatement("	projectPath=Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace(\"/\", File.separator);");
		setUpMethod.body().directStatement("	if(projectPath.endsWith(\".jar\"))projectPath=projectPath.substring(0,projectPath.lastIndexOf(File.separator)+1);");
		setUpMethod.body().directStatement("	//work around for local execution");
		setUpMethod.body().directStatement("	if(projectPath==null || projectPath.endsWith(\"null\")) projectPath=new File(\".\").getAbsolutePath()+\"\\\\\";");
	
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			setUpMethod.body().directStatement("	String platform[]=devicePlatform.split(\"-\")");
			setUpMethod.body().directStatement("	switch(platform[1])");
			setUpMethod.body().directStatement("		{");
			setUpMethod.body().directStatement("		case \"FIREFOX\":");
			setUpMethod.body().directStatement("			driver=new FirefoxDriver();");
			setUpMethod.body().directStatement("			caps = DesiredCapabilities.firefox();");
			setUpMethod.body().directStatement("			logPrefs = new LoggingPreferences();");
			setUpMethod.body().directStatement("			logPrefs.enable(LogType.BROWSER, Level.ALL);");
			setUpMethod.body().directStatement("			caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);");
			setUpMethod.body().directStatement("			driver=new FirefoxDriver(caps);");
			setUpMethod.body().directStatement("			break;");
			setUpMethod.body().directStatement("		case \"IE\":");
			setUpMethod.body().directStatement("			driver=new InternetExplorerDriver();");
			setUpMethod.body().directStatement("			caps = DesiredCapabilities.internetExplorer();");
			setUpMethod.body().directStatement("			logPrefs = new LoggingPreferences();");
			setUpMethod.body().directStatement("			logPrefs.enable(LogType.BROWSER, Level.ALL);");
			setUpMethod.body().directStatement("			caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);");
			setUpMethod.body().directStatement("			driver=new InternetExplorerDriver(caps);");
			setUpMethod.body().directStatement("			break;");
			setUpMethod.body().directStatement("		case \"CHROME\":");
			setUpMethod.body().directStatement("			driver=new ChromeDriver();");
			setUpMethod.body().directStatement("			caps = DesiredCapabilities.chrome();");
			setUpMethod.body().directStatement("			logPrefs = new LoggingPreferences();");
			setUpMethod.body().directStatement("			logPrefs.enable(LogType.BROWSER, Level.ALL);");
			setUpMethod.body().directStatement("			caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);");
			setUpMethod.body().directStatement("			driver=new ChromeDriver(caps);");
			setUpMethod.body().directStatement("			break;");
			setUpMethod.body().directStatement("		case \"SAFARI\":");
			setUpMethod.body().directStatement("			driver=new SafariDriver();");
			setUpMethod.body().directStatement("			caps = DesiredCapabilities.safari();");
			setUpMethod.body().directStatement("			logPrefs = new LoggingPreferences();");
			setUpMethod.body().directStatement("			logPrefs.enable(LogType.BROWSER, Level.ALL);");
			setUpMethod.body().directStatement("			caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);");
			setUpMethod.body().directStatement("			driver=new SafariDriver(caps);");
			setUpMethod.body().directStatement("			break;	");
			setUpMethod.body().directStatement("	}");
	
			setUpMethod.body().directStatement("		driver.manage().window().maximize();");
			setUpMethod.body().directStatement("		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);");
			setUpMethod.body().directStatement("    PauseResumeController.checkForPauseFile(Integer.parseInt(runListId.trim()));");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			setUpMethod.body().directStatement("//Please change the port no according to your configuration");
			setUpMethod.body().directStatement("getMain().initSeeTestClient();");
	
		}
		setUpMethod.body().directStatement("}  catch (Exception e) {");
		setUpMethod.body().directStatement("	e.printStackTrace();");
		setUpMethod.body().directStatement("}");
		
		return setUpMethod;
	}
	
	private JMethod constructSeeTestInitMethodForMain(JMethod initSeeTestClientMethod, String testExecutionEngine, ProductMaster product) {
		initSeeTestClientMethod.body().directStatement("try {");
		initSeeTestClientMethod.body().directStatement("	client = new Client(\"localhost\",Integer.parseInt(devicePort.trim()),true);");
		initSeeTestClientMethod.body().directStatement("	client.setProjectBaseDirectory(projectPath+scriptName);");
		if (product.getProductType().getTypeName().equalsIgnoreCase("Mobile") || product.getProductType().getTypeName().equalsIgnoreCase("Device")) {
			
			initSeeTestClientMethod.body().directStatement("	String platformPrefix=\"\";");
			initSeeTestClientMethod.body().directStatement("	if(devicePlatform.equalsIgnoreCase(Common.ANDROID.toString()))");
			initSeeTestClientMethod.body().directStatement("		platformPrefix=\"adb:\";");
			initSeeTestClientMethod.body().directStatement("	else if(devicePlatform.equalsIgnoreCase(Common.IOS.toString()))");
			initSeeTestClientMethod.body().directStatement("		platformPrefix=\"ios_app:\";");
			initSeeTestClientMethod.body().directStatement("	else if(devicePlatform.equalsIgnoreCase(Common.WINDOWS_PHONE.toString()))");
			initSeeTestClientMethod.body().directStatement("		platformPrefix=\"wp:\";");
			initSeeTestClientMethod.body().directStatement("	client.setApplicationTitle(platformPrefix+deviceId);");
			initSeeTestClientMethod.body().directStatement("	map = client.getLastCommandResultMap();");
			initSeeTestClientMethod.body().directStatement("	String strResultText = (String) map.get(\"text\");");
			initSeeTestClientMethod.body().directStatement("	//deviceModel = strResultText.substring(strResultText.indexOf(\"Model: \") + \"Model: \".length(), strResultText.indexOf(\"Version:\"));");			
		}
		initSeeTestClientMethod.body().directStatement("} catch(Exception e) {");
		initSeeTestClientMethod.body().directStatement("	e.printStackTrace();");
		initSeeTestClientMethod.body().directStatement("	tearDown();");
		initSeeTestClientMethod.body().directStatement("}	");	
		initSeeTestClientMethod.body().directStatement(" return client; ");
		

		return initSeeTestClientMethod;
	}
	
	private JMethod constructTearDownMethodForMain(JMethod tearDownMethod, String testExecutionEngine, ProductMaster product) {

		tearDownMethod.body().directStatement("                          ");
		tearDownMethod.body().directStatement("try {");
		tearDownMethod.body().directStatement("    PauseResumeController.checkForPauseFile(Integer.parseInt(runListId.trim()));");
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			tearDownMethod.body().directStatement("		EvidenceCapture.captureBrowserLogInSelenium(driver);");
		}
		tearDownMethod.body().directStatement("}  catch (Exception e) {");
		tearDownMethod.body().directStatement("		e.printStackTrace();");
		tearDownMethod.body().directStatement("}");
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			tearDownMethod.body().directStatement("driver.quit();");
		}
		tearDownMethod.body().directStatement("System.out.println(\"teardown\");");
		tearDownMethod.body().directStatement("System.out.println(ConsoleWriter.TAF.END);");
		
		return tearDownMethod;
	}
	
	private JDefinedClass constructAccessorMethodsForMain(JCodeModel codeModel, JDefinedClass mainClass, String testExecutionEngine, ProductMaster product) {
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			JMethod getWebURLMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getWebURL");
			getWebURLMethod.body().directStatement("	return webURL; ");
			
			JMethod getDriverMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getDriver");
			getDriverMethod.body().directStatement("	return driver; ");
	
			JMethod setDriverMethod = mainClass.method(JMod.PUBLIC, void.class, "setDriver");
			setDriverMethod.body().directStatement("	Main.driver = driver;");
			
			JMethod getDevicePlatformMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getDevicePlatform");
			getDevicePlatformMethod.body().directStatement("	return devicePlatform; ");
	
			JMethod setDevicePlatformMethod = mainClass.method(JMod.PUBLIC, void.class, "setDevicePlatform");
			setDevicePlatformMethod.body().directStatement("	Main.devicePlatform = devicePlatform;");
		}
	
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {		
			
			JMethod getDeviceIdMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getDeviceId");
			getDeviceIdMethod.body().directStatement("	return deviceId; ");
	
			JMethod setDeviceIdlMethod = mainClass.method(JMod.PUBLIC, void.class, "setDeviceId");
			setDeviceIdlMethod.body().directStatement("	Main.deviceId = deviceId;");
			
			JMethod getClientMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(com.experitest.client.Client.class), "getClient");
			getClientMethod.body().directStatement("  return client;\n");
			
			
			JMethod setClientMethod = mainClass.method(JMod.PUBLIC, void.class, "setClient");	
			setClientMethod.param(codeModel.ref(com.experitest.client.Client.class), "client");
			setClientMethod.body().directStatement("  Main.client = client;\n");				
			
			
			JMethod getDevicePlatformMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getDevicePlatform");
			getDevicePlatformMethod.body().directStatement("	return devicePlatform; ");
	
			JMethod setDevicePlatformMethod = mainClass.method(JMod.PUBLIC, void.class, "setDevicePlatform");
			setDevicePlatformMethod.body().directStatement("	Main.devicePlatform = devicePlatform;");
	
			JMethod getDeviceModelMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getDeviceModel");
			getDeviceModelMethod.body().directStatement("	return deviceModel; ");
	
			JMethod setDeviceModelMethod = mainClass.method(JMod.PUBLIC, void.class, "setDeviceModel");
			setDeviceModelMethod.body().directStatement("	Main.deviceModel = deviceModel;");
	
		}			
		
		JMethod getProjectPathMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getProjectPath");
		getProjectPathMethod.body().directStatement("	return projectPath; ");
	
		JMethod setProjectPathMethod = mainClass.method(JMod.PUBLIC, void.class, "setProjectPath");
		setProjectPathMethod.body().directStatement("	Main.projectPath = projectPath;");
		
	
		JMethod getEvidencePathMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getEvidencePath");
		getEvidencePathMethod.body().directStatement("	return evidencePath; ");
	
		JMethod setEvidencePathMethod = mainClass.method(JMod.PUBLIC, void.class, "setEvidencePath");
		setEvidencePathMethod.body().directStatement("	Main.evidencePath = evidencePath;");
	
		JMethod getRunlistIdMethod = mainClass.method(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "getRunlistId");
		getRunlistIdMethod.body().directStatement("	return runListId; ");
	
		JMethod setrunListIdMethod = mainClass.method(JMod.PUBLIC, void.class, "setrunListId");
		setrunListIdMethod.body().directStatement("	Main.runListId = runListId;");
		
		return mainClass;
	}

	private JDefinedClass addClassVariablesForTestCaseClass (JCodeModel codeModel, JDefinedClass testCaseClass, String testExecutionEngine) { 
	
		// 2. Add the required import statements and the class variables
		// 2.a TAF Lib references for import
		JClass testCaseClassReference = codeModel.ref("com.hcl.atf.taf.TestCase");
		JClass testStepClassReference = codeModel.ref("com.hcl.atf.taf.TestStep");
		JClass consoleWriterReference = codeModel.directClass("com.hcl.atf.taf.ConsoleWriter");
		JClass mainClassReference = codeModel.directClass("com.hcl.atf.taf.Main");
		JClass evidenceCaptureReference = null;
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			evidenceCaptureReference = codeModel.directClass("com.hcl.atf.taf.seleniumUtilities.EvidenceCapture");
			JClass ByReference = codeModel.directClass("org.openqa.selenium.By");
			JClass InternetExplorerDriverReference = codeModel.directClass("org.openqa.selenium.ie.InternetExplorerDriver");
			JClass FirefoxDriverReference = codeModel.directClass("org.openqa.selenium.firefox.FirefoxDriver");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			evidenceCaptureReference = codeModel.directClass("com.hcl.atf.taf.seeTestUtilities.EvidenceCapture");
		} 	
		
		JFieldVar testCaseField = testCaseClass.field(JMod.PUBLIC, testCaseClassReference, "testCase");
		JFieldVar testStepField = testCaseClass.field(JMod.PUBLIC, testStepClassReference, "testStep");
		JFieldVar consoleWriterField = testCaseClass.field(JMod.PUBLIC, consoleWriterReference, "taf");
	
		// 2.c Test execution API references
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
	
			JClass seeTestClientReference = codeModel.ref("com.experitest.client.Client");
			JFieldVar seeTestClient = testCaseClass.field(JMod.PUBLIC, seeTestClientReference, "client");
			JFieldVar evidenceCaptureVar = testCaseClass.field(JMod.PUBLIC, evidenceCaptureReference, "evidenceCapture");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
	
			JClass WebDriverReference = codeModel.ref("org.openqa.selenium.WebDriver");
			JClass WebElementReference = codeModel.ref("org.openqa.selenium.WebElement");
			JClass ActionsReference = codeModel.ref("org.openqa.selenium.interactions.Actions");
			JFieldVar driver = testCaseClass.field(JMod.PUBLIC, WebDriverReference, "driver");
			JFieldVar element = testCaseClass.field(JMod.PUBLIC, WebElementReference, "element");
			JFieldVar actor = testCaseClass.field(JMod.PUBLIC, ActionsReference, "actor");
			JFieldVar webURL = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "webURL");
			JFieldVar evidenceCaptureVar = testCaseClass.field(JMod.PUBLIC, evidenceCaptureReference, "evidenceCapture");
		}
		// 2.d Add standard reference variables
		JFieldVar result = testCaseClass.field(JMod.PUBLIC, codeModel.BOOLEAN, "status");
		JFieldVar observedOutput = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "observedOutput");
		JFieldVar executionRemarks = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "executionRemarks");
		JFieldVar failureRemarks = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "failureRemarks");
		JFieldVar screenShotPath = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "screenShotPath");
		JFieldVar screenShotLabel = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "screenShotLabel");
		JFieldVar devicePlatform = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "devicePlatform");
		JFieldVar runListId = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "runListId");
		JFieldVar testScriptQualifiedName = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "testScriptQualifiedName");
		testScriptQualifiedName.assign(JExpr.lit(""));
		JFieldVar testScriptFileName = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "testScriptFileName");
		testScriptFileName.assign(JExpr.lit(""));
		JFieldVar testPriority = testCaseClass.field(JMod.PUBLIC, codeModel.ref(java.lang.String.class), "testPriority");
		testPriority.assign(JExpr.lit(""));
		return testCaseClass;
	}
	
	private JMethod constructSetUpMethodForTestCase(JMethod setUpMethod, String testExecutionEngine, String refClassName, String testCaseName) {

		setUpMethod.body().directStatement("                          ");
		setUpMethod.body().directStatement("//Not recommended to remove the below framework code.");
		setUpMethod.body().directStatement("testCase = " + refClassName + "." + testCaseName + ";");
		setUpMethod.body().directStatement("devicePlatform = Main.getMain().getDevicePlatform();");
		setUpMethod.body().directStatement("runListId = Main.getMain().getRunlistId();");
		setUpMethod.body().directStatement("testScriptQualifiedName=getClass().getPackage().toString().substring(8);");
		setUpMethod.body().directStatement("testScriptFileName=getClass().getSimpleName()+\".java\"; ");
		setUpMethod.body().directStatement("testPriority=\"Medium\";");
	
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			setUpMethod.body().directStatement("client = Main.getMain().initSeeTestClient();");
		} else if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			setUpMethod.body().directStatement("driver = Main.getMain().getDriver();");
			setUpMethod.body().directStatement("webURL=Main.getMain().getWebURL();");
			setUpMethod.body().directStatement("actor=new Actions(driver);");
			setUpMethod.body().directStatement("driver.manage().window().maximize();");
			setUpMethod.body().directStatement("driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);");
		}
		setUpMethod.body().directStatement("      ");
		setUpMethod.body().directStatement("//Add your code below as needed for setup");
		setUpMethod.body().directStatement("      ");
		setUpMethod.body().directStatement("      ");
		setUpMethod.body().directStatement("PauseResumeController.checkForPauseFile(Integer.parseInt(runListId.trim()));");
		
		return setUpMethod;
	}
	
	private JMethod constructTearDownMethodForTestCase(JMethod tearDownMethod, String testExecutionEngine) {
		tearDownMethod.body().directStatement("                          ");
		tearDownMethod.body().directStatement("//Add your code below as needed for setup");
		tearDownMethod.body().directStatement("      ");
		tearDownMethod.body().directStatement("      ");
		tearDownMethod.body().directStatement("PauseResumeController.checkForPauseFile(Integer.parseInt(runListId.trim()));");
		tearDownMethod.body().directStatement("      ");
		return tearDownMethod;
	}

	private JMethod constructDefaultTestCaseMethodForTestCase(JMethod testCaseMethod, String testExecutionEngine, TestCaseList testCase) {
		
		JDocComment methodComment3 = testCaseMethod.javadoc();
		String commentString = "Automation Script Method for Teststep : " + testCase.getTestCaseName()
		+ "\n" + "ID  				: " + testCase.getTestCaseId()
		+ "\n" + "Code  			: " + testCase.getTestCaseCode()
		+ "\n" + "Description 		: " + testCase.getTestCaseDescription()
		+ "\n" + "Input 			: " + testCase.getTestcaseinput()
		+ "\n" + "Expected Output 	: " + testCase.getTestcaseexpectedoutput();
		methodComment3.append(commentString);

		testCaseMethod.body().directStatement("                          ");
		testCaseMethod.body().directStatement("//Not recommended to remove the below framework code.");
		testCaseMethod.body().directStatement("testCase.startTestCase();");
		testCaseMethod.body().directStatement("                          ");	
		testCaseMethod.body().directStatement("//Add test case logic here");
		testCaseMethod.body().directStatement(" try{                     ");	
		testCaseMethod.body().directStatement("                          ");			
		testCaseMethod.body().directStatement("//Result of the test step. true / false");
		testCaseMethod.body().directStatement("status = true;");
		testCaseMethod.body().directStatement("//Outcome of the teststep execution. Specify the values for reporting");
		testCaseMethod.body().directStatement("observedOutput = testStep.getTestStepExpectedOutput();");
		testCaseMethod.body().directStatement("executionRemarks = \"\";");
		testCaseMethod.body().directStatement("failureRemarks = \"\";");
		testCaseMethod.body().directStatement("                  ");
		testCaseMethod.body().directStatement("} catch(Exception e){     ");
		testCaseMethod.body().directStatement("    failureRemarks = \"\";");
		testCaseMethod.body().directStatement("    status = false;       ");
		testCaseMethod.body().directStatement("    observedOutput = \"\";");
		testCaseMethod.body().directStatement("	   e.printStackTrace();  ");		
		testCaseMethod.body().directStatement("}                         ");
		testCaseMethod.body().directStatement("                          ");
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			testCaseMethod.body().directStatement("//Utility Method for capturing screenshot. Delete or comment out if not needed");
			testCaseMethod.body().directStatement("screenShotPath=EvidenceCapture.captureScreenshotInSeetest(client, runListId, testCase.getTestCaseName(), testStep.getTestStepName());");  
		}
		testCaseMethod.body().directStatement("testStep.setTestStepScreenShot(screenShotPath);");
		testCaseMethod.body().directStatement("testStep.setTestStepScreenShotLabel(screenShotLabel);");
		
		testCaseMethod.body().directStatement("//Framework code for reporting results. Not recommended for change or remove");
		testCaseMethod.body().directStatement("testStep.finishedTestStep(observedOutput, executionRemarks, failureRemarks, status);");
		testCaseMethod.body().directStatement("testStep.report();");
		return testCaseMethod;
	}

	private JMethod constructTestStepMethodForTestCase(JMethod testStepMethod, String testExecutionEngine, String refClassName, String testCaseName, String testStepName, TestCaseStepsList testStep) {

		JDocComment methodComment3 = testStepMethod.javadoc();
		String commentString = "Automation Script Method for Teststep : " + testStep.getTestStepName()
		+ "\n" + "Teststep ID  		: " + testStep.getTestStepId()
		+ "\n" + "Teststep Code		: " + testStep.getTestStepCode()
		+ "\n" + "Description 		: " + testStep.getTestStepDescription()
		+ "\n" + "Input 			: " + testStep.getTestStepInput()
		+ "\n" + "Expected Output 	: " + testStep.getTestStepExpectedOutput();
		methodComment3.append(commentString);

		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("//Not recommended to remove the below framework code.");
		testStepMethod.body().directStatement("testStep = " + refClassName + "." + testCaseName + "_" + testStepName + ";");
		testStepMethod.body().directStatement("testStep.startTestStep();");
		testStepMethod.body().directStatement("                          ");
	
		testStepMethod.body().directStatement("//Add test step logic here");
		testStepMethod.body().directStatement(" try {                     ");			
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			testStepMethod.body().directStatement("//Utility Method for capturing screenshot. Delete or comment out if not needed");
			testStepMethod.body().directStatement("screenShotPath=EvidenceCapture.screencapture(driver,screenShotPath,runListId,testCase.getTestCaseName(),testStep.getTestStepName());");  
		}
		
		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("//Result of the test step. true / false");
		testStepMethod.body().directStatement("status = true;");		
		testStepMethod.body().directStatement("//Outcome of the teststep execution. Specify the values for reporting");
		testStepMethod.body().directStatement("observedOutput = testStep.getTestStepExpectedOutput();");
		testStepMethod.body().directStatement("executionRemarks = \"\";");
		testStepMethod.body().directStatement("failureRemarks = \"\";");
		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("} catch(Exception e){     ");
		testStepMethod.body().directStatement("    failureRemarks = \"\";");
		testStepMethod.body().directStatement("    status = false;       ");
		testStepMethod.body().directStatement("    e.printStackTrace();  ");
		testStepMethod.body().directStatement("    observedOutput = \"\";");
		testStepMethod.body().directStatement("}                         ");
		testStepMethod.body().directStatement("                          ");
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			testStepMethod.body().directStatement("//Utility Method for capturing screenshot. Delete or comment out if not needed");
			testStepMethod.body().directStatement("screenShotPath=EvidenceCapture.captureScreenshotInSeetest(client, runListId, testCase.getTestCaseName(), testStep.getTestStepName());");  
		}
		
		testStepMethod.body().directStatement("testStep.setTestStepScreenShot(screenShotPath);");
		testStepMethod.body().directStatement("testStep.setTestStepScreenShotLabel(screenShotLabel);");
		
		testStepMethod.body().directStatement("//Framework code for reporting results. Not recommended for change or remove");
		testStepMethod.body().directStatement("testStep.finishedTestStep(observedOutput, executionRemarks, failureRemarks, status);");
		testStepMethod.body().directStatement("testStep.report();");
		
		return testStepMethod;
	}

	private JMethod addTestStepForSingleTestStepMethodForTestCase(JMethod testStepMethod, String testExecutionEngine, String refClassName, String testCaseName, String testStepName, TestCaseStepsList testStep) {

		testStepMethod.body().directStatement("/*******************************************************************");
		testStepMethod.body().directStatement(" * Start test step script block : " + testStepName);
		testStepMethod.body().directStatement(" * Teststep          : " + testStep.getTestStepName());
		testStepMethod.body().directStatement(" * Teststep ID		: " + testStep.getTestStepId());
		testStepMethod.body().directStatement(" * Teststep Code 	: " + testStep.getTestStepCode());
		testStepMethod.body().directStatement(" * Description 		: " + testStep.getTestStepDescription());
		testStepMethod.body().directStatement(" * Input 			: " + testStep.getTestStepInput());
		testStepMethod.body().directStatement(" * Expected Output 	: " + testStep.getTestStepExpectedOutput());
		testStepMethod.body().directStatement(" */");
		
		testStepMethod.body().directStatement("                          ");
		
		
		testStepMethod.body().directStatement("//Framework code 1 : For getting test step ref and starting timer. Not recommended for change or removal.");
		testStepMethod.body().directStatement("testStep = " + refClassName + "." + testCaseName + "_" + testStepName + "; //Get test step ref");
		testStepMethod.body().directStatement("testStep.startTestStep(); //Starting test step timer");
		testStepMethod.body().directStatement("try{                      ");			
		testStepMethod.body().directStatement("//Framework code 1 : End");
		testStepMethod.body().directStatement("                          ");	
		testStepMethod.body().directStatement("//Add test step functional automation code here");
		
		testStepMethod.body().directStatement("//Outcome of the teststep execution. Specify the values for reporting");
		
		testStepMethod.body().directStatement("//Result of the test step. true / false");
		testStepMethod.body().directStatement("status = true; //Specify whether the test step passed or failed");
		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("observedOutput = testStep.getTestStepExpectedOutput();");
		testStepMethod.body().directStatement("executionRemarks = \"\";");
		testStepMethod.body().directStatement("failureRemarks = \"\";");
		
		
		testStepMethod.body().directStatement("} catch (Exception e) {   ");
		testStepMethod.body().directStatement("   failureRemarks = \"\";");
		testStepMethod.body().directStatement("   status = false;       ");
		testStepMethod.body().directStatement("	  e.printStackTrace();  ");	
		testStepMethod.body().directStatement("   observedOutput = \"\";");
		testStepMethod.body().directStatement("}                     ");	
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SEETEST)) {
			testStepMethod.body().directStatement("//Utility Method for capturing screenshot. Delete or comment out if not needed");
			testStepMethod.body().directStatement("screenShotPath=EvidenceCapture.captureScreenshotInSeetest(client, runListId, testCase.getTestCaseName(), testStep.getTestStepName()); //Take a screen shot for evidence");  
		}
		
		if (testExecutionEngine.equals(TAFConstants.TESTENGINE_SELENIUM)) {
			testStepMethod.body().directStatement("//Utility Method for capturing screenshot. Delete or comment out if not needed");
			testStepMethod.body().directStatement("screenShotPath=EvidenceCapture.screencapture(driver,screenShotPath,runListId,testCase.getTestCaseName(),testStep.getTestStepName());");  
		}
		
		testStepMethod.body().directStatement("testStep.setTestStepScreenShot(screenShotPath);");
		testStepMethod.body().directStatement("testStep.setTestStepScreenShotLabel(screenShotLabel);");
		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("//Framework code 2 : For reporting results. Not recommended for change or remove");
		testStepMethod.body().directStatement("testStep.finishedTestStep(observedOutput, executionRemarks, failureRemarks, status); //The test step is finished, along with the time taken");
		testStepMethod.body().directStatement("testStep.report();");
		
		testStepMethod.body().directStatement("//Framework code 2 : End");
		testStepMethod.body().directStatement("                          ");
		testStepMethod.body().directStatement("/***********************************************************************");
		testStepMethod.body().directStatement(" *Finish test step script block : " + testCaseName + "_" + testStepName);
		testStepMethod.body().directStatement(" */");
		testStepMethod.body().directStatement("                          ");
		
		return testStepMethod;
	}
	
	private void generateJUnitSetUpClass(List<TestCaseList> allTestCases, ProductMaster product, String testExecutionEngine, String packageName, String destinationDirectory, int nameSource) {
				
				JCodeModel codeModel = new JCodeModel();
				String message = null;
				try {
					if (testExecutionEngine == null)
						testExecutionEngine = TAFConstants.TESTENGINE_SEETEST;
					log.info("Test Execution Engine : " + testExecutionEngine);
					
					JClass beforeReference = null;				
					beforeReference = codeModel.ref("org.junit.Before");	
					
					JPackage cl = codeModel.rootPackage();
			        cl = cl.subPackage("com.hcl.atf.taf.testcase");
					JDefinedClass setUpClass = codeModel._class(packageName + "." + "SetUp");
					
					JMethod setUpMethod = setUpClass.method(JMod.PUBLIC, void.class, "setUp");
					setUpMethod.annotate(beforeReference);
					JDocComment methodComment1 = setUpMethod.javadoc();
					String commentString = "Setup method for the TestSuite/TestRun";
					methodComment1.append(commentString);
					
					setUpMethod.body().directStatement("                          ");
					setUpMethod.body().directStatement("try {");
					setUpMethod.body().directStatement("	Main.getMain().setUp();");
					setUpMethod.body().directStatement("} catch (Exception e) {");
					setUpMethod.body().directStatement(" 	e.printStackTrace();");
					setUpMethod.body().directStatement("}						");
					setUpMethod.body().directStatement(" 						");		
					
					JClass testReference = null;				
					testReference = codeModel.ref("org.junit.Test");
					
					JMethod testMethod = setUpClass.method(JMod.PUBLIC, void.class, "test");
					testMethod.annotate(testReference);
					JDocComment methodComment2 = testMethod.javadoc();
					String commentString1 = "Test method for the TestSuite/TestRun";
					methodComment2.append(commentString1);
					
					testMethod.body().directStatement("                          ");					
					testMethod.body().directStatement(" 						");	
					
					File sourceFile = new File(destinationDirectory);
					if (!sourceFile.exists()) {
						log.info("Created Directory for source code generation : " + destinationDirectory);
						sourceFile.mkdirs();
					}
					codeModel.build(sourceFile);
					message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator +"SetUp.java";

				} catch (JClassAlreadyExistsException e) {
					log.error("Unable to generate testcase ref source code class", e);
					return ;
				} catch (IOException io) {
					log.error("Unable to create file in file system", io);
					return ;
				}
				log.debug("Success : Generated Testcases source code framework." );
				return ;
	}
	

	private void generateJUnitTearDownClass(List<TestCaseList> allTestCases, ProductMaster product, String executionEngine, String packageName, String destinationDirectory, int nameSource) {
		JCodeModel codeModel = new JCodeModel();
		String message = null;
		try {
			if (executionEngine == null)
				executionEngine = TAFConstants.TESTENGINE_SEETEST;
			log.info("Test Execution Engine : " + executionEngine);
			
			JClass afterReference = null;				
			afterReference = codeModel.ref("org.junit.After");	
			
			JPackage cl = codeModel.rootPackage();
	        cl = cl.subPackage("com.hcl.atf.taf.testcase");
			JDefinedClass tearDownClass = codeModel._class(packageName + "." + "TearDown");
			
			JMethod tearDownMethod = tearDownClass.method(JMod.PUBLIC, void.class, "tearDown");
			tearDownMethod.annotate(afterReference);
			JDocComment methodComment1 = tearDownMethod.javadoc();
			String commentString = "TearDown method for the TestSuite/TestRun";
			methodComment1.append(commentString);
			
			tearDownMethod.body().directStatement("                          ");
			tearDownMethod.body().directStatement("try {");
			tearDownMethod.body().directStatement("	Main.getMain().tearDown();");
			tearDownMethod.body().directStatement("} catch (Exception e) {");
			tearDownMethod.body().directStatement(" 	e.printStackTrace();");
			tearDownMethod.body().directStatement("}						");
			tearDownMethod.body().directStatement(" 						");				
			
			JClass testReference = null;				
			testReference = codeModel.ref("org.junit.Test");
			
			JMethod testMethod = tearDownClass.method(JMod.PUBLIC, void.class, "test");
			testMethod.annotate(testReference);
			JDocComment methodComment2 = testMethod.javadoc();
			String commentString1 = "Test method for the TestSuite/TestRun";
			methodComment2.append(commentString1);
			
			testMethod.body().directStatement("                          ");					
			testMethod.body().directStatement(" 						");	
			
			File sourceFile = new File(destinationDirectory);
			if (!sourceFile.exists()) {
				log.info("Created Directory for source code generation : " + destinationDirectory);
				sourceFile.mkdirs();
			}
			codeModel.build(sourceFile);
			message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator +"TearDown.java";

		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			return ;
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			return ;
		}
		log.debug("Success : Generated Testcases source code framework." );
		return ;
		
	}
	
	private void generateSelectiveTestCaseClass(ProductMaster product, String testExecutionEngine, String packageName, String destinationDirectory, int nameSource){
		try{
			log.info("Generating Selective Test Case Class...");
			String message = null;
			JCodeModel codeModel = new JCodeModel();
			JClass exception = (JClass) codeModel._ref(Exception.class);
			JDefinedClass selectiveTestCaseMainClass = codeModel._class(packageName + "." + "SelectiveTestCasesMain");
			StringBuffer importClassStatements = new StringBuffer();

			//For import statements
			importClassStatements.append("import org.junit.runner.JUnitCore;\n");
			importClassStatements.append("import org.junit.runner.Request;\n");
			importClassStatements.append("import org.junit.runner.Result;\n");
			importClassStatements.append("import com.hcl.atf.taf.util.SelectiveTestCasesExecutionProcessing;\n");
			
			// Create Main method for Selective test case class
			JMethod mainMethod = selectiveTestCaseMainClass.method(JMod.PUBLIC|JMod.STATIC, void.class, "main");
			mainMethod.param(String[].class, "args");
			mainMethod._throws(exception);
			mainMethod.body().directStatement("  String SelectiveTestCasesFolderPath;");
			mainMethod.body().directStatement("  String ExecuteTestCasesSelectively; ");
			mainMethod.body().directStatement("  String runListId;                   ");
			mainMethod.body().directStatement("  final JUnitCore junit = new JUnitCore(); \n  final Request req; \n  ExecuteTestCasesSelectively = System.getProperty(\"EXECUTE_TESTCASES_SELECTIVELY\");\n\n");			
			mainMethod.body().directStatement("  if (ExecuteTestCasesSelectively == null || ExecuteTestCasesSelectively.equals(\"\") || ExecuteTestCasesSelectively.equals(\"NO\") || ExecuteTestCasesSelectively.equals(\"null\")) {");	
			mainMethod.body().directStatement("     System.out.println(\"ExecuteTestCasesSelectively is null\"); " );
			mainMethod.body().directStatement("     req = Request.classes(Main.class);                           ");
			mainMethod.body().directStatement("  }// Run only the junit classes that come as parameter list         ");
			mainMethod.body().directStatement("  else {                                                             ");
			mainMethod.body().directStatement("     System.out.println(\"ExecuteTestCasesSelectively is not null\");");
			mainMethod.body().directStatement("     SelectiveTestCasesFolderPath=System.getProperty(\"SELECTIVE_TESTCASES_FOLDER_PATH\"); ");
			mainMethod.body().directStatement("     runListId=System.getProperty(\"TESTRUNLIST_ID\"); ");
			mainMethod.body().directStatement("     Class<?>[] classesTobeInvoked =  SelectiveTestCasesExecutionProcessing.getTestCasesListToExecute(SelectiveTestCasesFolderPath, runListId);");
			mainMethod.body().directStatement("     System.out.println(\"classesTobeInvoked\"+classesTobeInvoked);");
			mainMethod.body().directStatement("     req = Request.classes(classesTobeInvoked);");
			mainMethod.body().directStatement("  }");
			mainMethod.body().directStatement("  final Result result = junit.run(req);");
			mainMethod.body().directStatement("  System.out.println(\"RESULT=\"+result.wasSuccessful());");		
			File sourceFile = new File(destinationDirectory);
			if (!sourceFile.exists()) {
				log.info("Created Directory for source code generation : " + destinationDirectory);
				sourceFile.mkdirs();
			}
			codeModel.build(sourceFile);
			message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator +"SelectiveTestCasesMain.java";
			String type = "SELECTIVE";
			ScriptGeneratorUtilities.insertTestCaseImportsForJavaScripts(message, importClassStatements.toString(), "",type);			
		} catch(Exception e){
			log.error("Failed to generate Selective Test Case Main Class.", e);
		}
		return;
	}
}