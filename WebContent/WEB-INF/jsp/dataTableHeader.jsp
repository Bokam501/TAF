<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%>
<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.4
Version: 3.9.0
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8">
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!--  dataTable v1.5.6 -->
<!-- datatable styles -->
<!-- <link rel="stylesheet" href="js/datatable/jquery-ui.css" type="text/javascript" media="all">  -->

<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.bootstrap.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.bootstrap.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.bootstrap4.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.bootstrap4.min.css" type="text/css"></link> -->
<link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.jqueryui.css" type="text/css"></link>
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.jqueryui.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.foundation.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.foundation.min.css" type="text/css"></link> -->
<link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/jquery.dataTables.css" type="text/css"></link>
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/jquery.dataTables.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.material.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.material.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.semanticui.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.semanticui.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.uikit.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/dataTables.uikit.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/media/css/jquery.dataTables_themeroller.css" type="text/css"></link> -->

<!-- <link rel="stylesheet" href="js/datatable/css/shCore.css" type="text/css"></link> -->

<link rel="stylesheet" href="js/datatable/DataTables-1.10.12/button/1.2.2/css/buttons.dataTables.min.css" type="text/css"></link>

<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/button/1.2.1/css/buttons.jqueryui.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/DataTables-1.10.12/button/1.2.0/css/buttons.dataTables.min.css" type="text/css"></link> -->

<link rel="stylesheet" href="js/datatable/DataTables-1.10.12/select/1.2.0/css/select.dataTables.min.css" type="text/css"></link>
<link rel="stylesheet" href="js/datatable/DataTables-1.10.12/select/1.2.0/css/select.jqueryui.min.css" type="text/css"></link>
<!-- <link rel="stylesheet" href="js/datatable/css/jquery-ui-1.7.2.custom.css" type="text/css"></link> -->

<!-- <link rel="stylesheet" href="js/datatable/css/datatables.min.css" type="text/css"></link> -->

<!-- <link rel="stylesheet" href="js/datatable/css/buttons.dataTables.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/css/select.dataTables.min.css" type="text/css"></link> -->
<!-- <link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all"> -->
<!-- <link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all"> -->
<!-- <link href="js/datatable/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"></link> -->

<!-- <link href="js/datatable/jquery-ui-1.11.4/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css"></link> -->

<!-- <link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.bootstrap.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.bootstrap.min.css" rel="stylesheet" type="text/css"></link> -->
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.dataTables.min.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.foundation.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.foundation.min.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.jqueryui.css" rel="stylesheet" type="text/css"></link> -->
<!-- <link href="js/datatable/Editor-1.5.6/css/editor.jqueryui.min.css" rel="stylesheet" type="text/css"></link> -->
<!-- end datatable styles -->


</body>
</html>