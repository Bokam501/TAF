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

<!-- <script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/jquery.dataTables.js" type="text/javascript"></script>
<script src="js/Scripts/popup/jquery.jtable.toolbarsearch.js" type="text/javascript"></script> -->

<!-- datatable v 1.5.6 -->
<!-- For DataTable -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.bootstrap" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.bootstrap.min" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.bootstrap4.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.bootstrap4.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/jquery.js" type="text/javascript"></script> -->
<script src="js/datatable/DataTables-1.10.12/media/js/jquery.dataTables.js" type="text/javascript"></script>
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/jquery.dataTables.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.foundation.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.foundation.min.js" type="text/javascript"></script> -->
<script src="js/datatable/DataTables-1.10.12/media/js/dataTables.jqueryui.js" type="text/javascript"></script>
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.jqueryui.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.material.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.material.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.semanticui.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.semanticui.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.uikit.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/media/js/dataTables.uikit.min.js" type="text/javascript"></script> -->

<!-- <script type="text/javascript" src="js/datatable/shCore.js"></script> -->

<!-- <script type="text/javascript" src="js/datatable/jquery-ui-1.11.3/datatables.min.js"></script> -->

<script src="js/datatable/DataTables-1.10.12/button/1.2.2/js/dataTables.buttons.min.js" type="text/javascript"></script>
<script src="js/datatable/DataTables-1.10.12/button/1.2.2/js/buttons.flash.min.js" type="text/javascript"></script>

<!-- <script type="text/javascript" src="js/datatable/DataTables-1.10.12/colresizable/colResizable-1.5.min.js"></script> -->

<script type="text/javascript" src="js/datatable/DataTables-1.10.12/button/jszip/2.5.0/jszip.min.js"></script>
<script type="text/javascript" src="js/datatable/DataTables-1.10.12/button/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script type="text/javascript" src="js/datatable/DataTables-1.10.12/button/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script type="text/javascript" src="js/datatable/DataTables-1.10.12/button/1.2.1/js/buttons.html5.min.js"></script>

<!-- <script src="js/datatable/DataTables-1.10.12/button/1.2.2/js/pdfmake.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/button/1.2.2/js/buttons.html5.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/button/1.2.2/js/buttons.print.min.js" type="text/javascript"></script> -->
<script type="text/javascript" src="js/datatable/DataTables-1.10.12/button/1.2.2/js/buttons.colVis.min.js"></script>

<!-- <script src="js/datatable/DataTables-1.10.12/button/1.2.1/js/buttons.jqueryui.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/DataTables-1.10.12/button/1.2.1/js/dataTables.buttons.min.js" type="text/javascript"></script> -->
<script src="js/datatable/DataTables-1.10.12/select/1.2.0/js/dataTables.select.min.js" type="text/javascript"></script>

<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/datetime.js" type="text/javascript"></script>

<!-- <script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/dataTables.buttons.min.js" type="text/javascript"></script> -->
<!-- <script src="js/datatable/dataTables.select.min.js" type="text/javascript"></script>	 -->
<!-- <script src="js/datatable/dataTables.editor.min.js" type="text/javascript"></script> -->

<!-- <script type="text/javascript" src="js/datatable/jquery.dataTables.rowGrouping.js"></script> -->
<script type="text/javascript" src="js/datatable/dataTables.fixedColumns.min.js"></script>
<script type="text/javascript" src="js/datatable/jquery.dataTables.columnFilter.js"></script>
<!-- <script type="text/javascript" src="js/datatable/dataTables.fixedHeader.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script> -->

<!-- <script type="text/javascript" src="js/jquery/1.12.3/js/jquery-1.12.3.js"></script>
<script type="text/javascript" src="js/datatable/jquery-ui-1.11.4/jquery-ui.js"></script> -->

<script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.js"></script>
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.min.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.bootstrap.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.bootstrap.min.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.foundation.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.foundation.min.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.jqueryui.js"></script> -->
<!-- <script type="text/javascript" src="js/datatable/Editor-1.5.6/js/editor.jqueryui.min.js"></script> -->

</body>
</html>