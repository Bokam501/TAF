package com.hcl.atf.taf.scriptgeneration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.sun.codemodel.JMod;

public class TestCaseRefScriptGenerator {	 

	private static final Log log = LogFactory.getLog(TestCaseRefScriptGenerator.class);

	@Autowired
	private TestCaseService testCaseService;

	private static String MAIN_PACKAGE_NAME = "com.hcl.atf.taf"; 
	private static String MAIN_PACKAGE_NAME_FOLDER = File.separator + "com" + File.separator + "hcl" + File.separator + "atf" + File.separator + "taf";
	private static String DEFAULT_TEST_CASES_REFERENCE_CLASS_NAME = "TestCasesReference"; 
	
	public String generateTestCaseRefSourceCode(List<TestCaseList> allTestCases, ProductMaster product, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine) {
		
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
			packageName = MAIN_PACKAGE_NAME;
		if (className == null)
			className = DEFAULT_TEST_CASES_REFERENCE_CLASS_NAME;
		if (destinationDirectory == null)
			return "Failed : Unable to generate reference source code as Destination directory is not specified";

		return generateTestCaseRefClass(allTestCases, product, packageName, className, destinationDirectory, nameSource, scriptType, executionEngine);
		
	}
	
	private String generateTestCaseRefClass(List<TestCaseList> allTestCases, ProductMaster product, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine) {
	
		String message = null;
		//Default Script type is JUnit
		if (scriptType == null || scriptType.trim().length()< 1) 
			scriptType = TAFConstants.TESTSCRIPT_TYPE_JUNIT;
		
		if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_JUNIT)) {
			message = generateJUnitTestCaseRefClass(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine);
		} else if (scriptType.equalsIgnoreCase(TAFConstants.TESTSCRIPT_TYPE_TESTNG)) {
			message = generateJUnitTestCaseRefClass(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine);
		} else {
			message = generateJUnitTestCaseRefClass(allTestCases, product, packageName, className, destinationDirectory, nameSource, executionEngine);
		}
		return message;
	}

	private String generateJUnitTestCaseRefClass(List<TestCaseList> allTestCases, ProductMaster product, String packageName, String className, String destinationDirectory, int nameSource, String executionEngine) {

		String message = null;
		log.info("Generating TestCasesRefClass");
		//Now process all the test cases to generate code
		JCodeModel codeModel = new JCodeModel();
		try {
			JDefinedClass testCasesRefClass = codeModel._class(packageName + "." + className);
			JDocComment classComment = testCasesRefClass.javadoc();
			String javadocCommentClass = "This class provides references to the test cases and test steps for developing test automation scripts."
					+ "\n" + "Product Name : " + product.getProductName()
					+ "\n" + "Code generated by TAF on " + new Date(System.currentTimeMillis());
			classComment.append(javadocCommentClass);
			
			JClass testCaseClassReference = codeModel.ref("com.hcl.atf.taf.TestCase");
			JClass testStepClassReference = codeModel.ref("com.hcl.atf.taf.TestStep");
			
			for (TestCaseList testCase : allTestCases) {
				String testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(), testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource);
				JFieldVar testCaseField = testCasesRefClass.field(JMod.PUBLIC + JMod.STATIC, testCaseClassReference, testCaseName.trim());
				testCaseField.init(JExpr._new(testCaseClassReference).arg(JExpr.lit(testCase.getTestCaseId()))
																.arg(JExpr.lit(testCase.getTestCaseName()))
                        										.arg(JExpr.lit((testCase.getTestCaseDescription() != null) ? testCase.getTestCaseDescription() : ""))
                        										.arg(JExpr.lit((testCase.getTestCaseCode() != null) ? testCase.getTestCaseCode() : ""))
                        										.arg(JExpr.lit((testCase.getTestCaseDescription() != null) ? testCase.getTestCaseDescription() : ""))
                        										.arg(JExpr.lit((testCase.getTestCaseType() != null) ? testCase.getTestCaseType() : ""))
                        										.arg(JExpr.lit(((testCase.getTestCasePriority() != null) && (testCase.getTestCasePriority().getPriorityName() != null)) ? testCase.getTestCasePriority().getPriorityName() : ""))
                        										.arg(JExpr.lit((testCase.getTestcaseinput() != null) ? testCase.getTestcaseinput() : ""))
                        										.arg(JExpr.lit((testCase.getTestcaseexpectedoutput() != null) ? testCase.getTestcaseexpectedoutput() : ""))
                        										.arg(JExpr.lit((testCase.getPreconditions() != null) ? testCase.getPreconditions() : ""))
				                                               );
				
				JDocComment variableComment = testCaseField.javadoc();
				String vComment = "Reference variables (Testcase and its TestSteps) for Testcase : " + testCase.getTestCaseName().trim();
				variableComment.append(vComment);
				log.debug("Created testcase variable for : " + testCase.getTestCaseName());
				List<TestCaseStepsList> testSteps = testCaseService.listTestCaseSteps(testCase.getTestCaseId());
				if (testSteps != null) {

					for (TestCaseStepsList testStep : testSteps) {
						
						String testStepName = ScriptGeneratorUtilities.getTestStepMethodName(testCaseName, testStep.getTestStepName(), testStep.getTestStepId(), testStep.getTestStepCode(), nameSource );
						JFieldVar testStepField = testCasesRefClass.field(JMod.PUBLIC + JMod.STATIC, testStepClassReference, testCaseName + "_" + testStepName.trim());
						testStepField.init(JExpr._new(testStepClassReference)
																	.arg(JExpr.lit(testStep.getTestStepId()))
						                                            .arg(JExpr.lit((testStep.getTestStepName() != null) ? testStep.getTestStepName() : ""))
						                                            .arg(JExpr.lit((testStep.getTestStepDescription() != null) ? testStep.getTestStepDescription(): ""))
						                                            .arg(JExpr.lit((testStep.getTestStepInput() != null) ? testStep.getTestStepInput() : ""))
						                                            .arg(JExpr.lit((testStep.getTestStepExpectedOutput() != null) ? testStep.getTestStepExpectedOutput() : ""))
						                                            .arg(JExpr.lit((testStep.getTestStepCode() != null) ? testStep.getTestStepCode() : ""))
						                                            .arg(JExpr.lit((testStep.getTestStepSource() != null) ? testStep.getTestStepSource() : ""))
						                                            .arg(JExpr.ref(testCaseName))
						                                            );
						log.debug("Created teststep variable for : " + testStep.getTestStepName());
					}
				}
			}
			
			File sourceFile = new File(destinationDirectory);
			if (!sourceFile.exists()) {
				boolean createdDir = sourceFile.mkdirs();
				log.info(destinationDirectory + " not found. Created new directory for source code generation : " +  " : " + createdDir);
			}
			codeModel.build(sourceFile);
			log.info("Success : Generated Testcase ref source code : " + product.getProductId() + " : " + packageName + " : " + className + " : " + destinationDirectory + " : " + nameSource);
			message = sourceFile.getAbsolutePath() + MAIN_PACKAGE_NAME_FOLDER + File.separator + className + ".java";
			log.info("Success : TestCaseRefClass" + message);
			return message;
		} catch (JClassAlreadyExistsException e) {
			log.error("Unable to generate testcase ref source code class", e);
			return "Failed : Unable to generate testcase ref source code class";
		} catch (IOException io) {
			log.error("Unable to create file in file system", io);
			return "Failed : Unable to create file in file system";
		}
	}
}
