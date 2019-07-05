<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@ page import = "java.util.ResourceBundle" %>
<!DOCTYPE html>

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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Activity Gnatt Chart -->
<link rel="stylesheet" type="text/css" href="files/lib/chart/css/angular-ui-tree.min.css">
<link rel="stylesheet" type="text/css" href="files/lib/chart/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="files/lib/chart/css/angular-gantt.css">
<!-- <link rel="stylesheet" type="text/css" href="files/lib/chart/css/angular-gantt-demo.css"> -->
<link rel="stylesheet" type="text/css" href="files/lib/chart/css/style.css">

<script type='text/javascript' src="files/lib/chart/script/angular/angular.min.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/moment.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/moment-range.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/angular-ui-tree.min.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/draganddrop.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/ElementQueries.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/ResizeSensor.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/jsplumb.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/angular-gantt.js"></script>
  <script type="text/javascript" src="js/viewScript/activityChart.js"></script>
 <script type='text/javascript' src="files/lib/chart/script/script.js"></script> 
<!--  <link rel="stylesheet" type="text/css" href="files/lib/chart/css/bootstrap.min.css"> -->
 
</head>
	
<!-- <div id="activityGnattChartContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 98%;left: 1%;right: 1%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onClick="activityGanttCloseHandler()"></button>
					<h4 class="modal-title theme-font">Workpackage Gantt View</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id="activityChartContainer">
		 					 <div ng-app="plnkrGanttStable" id="activityWorkpackageGanttController" ng-controller="activityWorkpackageGanttController">
							    <div gantt data="data" api="registerApi" current-date-value="getToday" current-date="'column'" options="options" column-width="25">
							      <gantt-tree header="treeHeaderName"></gantt-tree>
							        <gantt-table columns="['model.idValue','model.assignee', 'model.priority', 'model.status']"></gantt-table>							        							      
							        <gantt-groups></gantt-groups>
							        <gantt-tooltips></gantt-tooltips>
							        <gantt-bounds></gantt-bounds>
							        <gantt-progress></gantt-progress>
							        <gantt-sortable></gantt-sortable>
							        <gantt-movable enabled="true"
                   						allow-moving="true" 
                   						allow-resizing="true" 
							        	allow-row-switching="false"></gantt-movable>
							        <gantt-draw-task enabled="false"></gantt-draw-task>
							        <gantt-resize-sensor></gantt-resize-sensor>
							        <gantt-overlap></gantt-overlap>
							        <gantt-dependencies	enabled="true"></gantt-dependencies>
							    </div>							    
							</div> 
		 				</div>					 
					</div>
				</div>				
			</div>	
		</div>
</div>  -->

<div id="activityGnattChartContainer" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
     <div class="modal-dialog modal-full">
         <div class="modal-content">
             <div class="modal-header">
                 <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onClick="activityGanttCloseHandler()"></button>
                <h4 class="modal-title theme-font">Workpackage Gantt View</h4>
             </div>
             <div class="modal-body">
             	<div id="activityChartContainer">
 					 <div ng-app="plnkrGanttStable" id="activityWorkpackageGanttController" ng-controller="activityWorkpackageGanttController">
					    <div gantt data="data" api="registerApi" current-date-value="getToday" current-date="'column'" options="options" column-width="25">
					      <gantt-tree header="treeHeaderName"></gantt-tree>
					        <gantt-table columns="['model.idValue','model.assignee', 'model.priority', 'model.status']"></gantt-table>							        							      
					        <gantt-groups></gantt-groups>
					        <gantt-tooltips></gantt-tooltips>
					        <gantt-bounds></gantt-bounds>
					        <gantt-progress></gantt-progress>
					        <gantt-sortable></gantt-sortable>
					        <gantt-movable enabled="true"
                 						allow-moving="true" 
                 						allow-resizing="true" 
					        	allow-row-switching="false"></gantt-movable>
					        <gantt-draw-task enabled="false"></gantt-draw-task>
					        <gantt-resize-sensor></gantt-resize-sensor>
					        <gantt-overlap></gantt-overlap>
					        <gantt-dependencies	enabled="true"></gantt-dependencies>
					    </div>							    
					</div> 
 				</div>
             </div>
             <!-- <div class="modal-footer">
                 <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                 <button type="button" class="btn green">Save changes</button>
             </div> -->
         </div>
         <!-- /.modal-content -->
     </div>
     <!-- /.modal-dialog -->
 </div>

<!-- END BODY -->
</html>