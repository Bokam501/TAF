package com.hcl.atf.taf.jasper.reports;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperPrint;

public interface TestResultReports {
	public JasperPrint generateTestRunListReport(Integer testRunNo,String reportMode,DataSource dataSource) throws Exception;
	public JasperPrint generateTestRunDeviceListReport(Integer testRunNo,String deviceId,String reportMode,DataSource dataSource) throws Exception;
}
