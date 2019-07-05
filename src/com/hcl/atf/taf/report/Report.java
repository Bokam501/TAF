package com.hcl.atf.taf.report;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JasperPrint;

import com.google.common.collect.ListMultimap;
import com.hcl.atf.taf.model.ProductType;


public interface Report {
	public JasperPrint generateTestRunListReport(Integer testRunNo,Integer testRunConfigurationChildId,String reportMode,ProductType prodType,BufferedImage logo, String loginUserName);
	public JasperPrint generateTestRunDeviceListReport(Integer testRunNo,Integer testRunListId,String deviceId,Integer testRunConfigurationChildId,String reportMode, BufferedImage logo, String loginUserName, String reportType);
	public JasperPrint generateTestRunListRCReport(Integer testRunNo,Integer testRunConfigurationChildId,String reportMode,ProductType prodType,BufferedImage logo, String loginUserName);
	abstract List<JasperPrint> generateTestRunReport(Map<String,Object> pInput);
	abstract List<JasperPrint> generateTestRunDeviceReport(Map<String,Object> pInput);
	public List<String> generateTestRunListHtmlReport(Integer testRunNo, Integer testRunConfigurationChildId, String strPrintMode, ProductType productType, BufferedImage logo, String loginUserName);
	public ListMultimap<Integer, Object> getFeatureNamesByTestCaseIdList(
			Set<Integer> listOfTestCaseIds);
	public List<String> getAllJobIdsByWorkpackageId(Integer testRunNo);
}
