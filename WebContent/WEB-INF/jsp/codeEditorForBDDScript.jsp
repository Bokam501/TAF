<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> --%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
  <spring:eval var="amdocsFlag" expression="@ilcmProps.getProperty('AMDOCS')" /> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<link rel=stylesheet href="files/lib/CodeMirror-master/lib/codemirror.css">
<link rel=stylesheet href="files/lib/CodeMirror-master/addon/hint/show-hint.css">
<link rel="stylesheet" href="files/lib/CodeMirror-master/addon/lint/lint.min.css">

<script src="files/lib/CodeMirror-master/lib/codemirror.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/xml/xml.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/addon/hint/show-hint.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/addon/hint/javascript-hint.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/javascript/javascript.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/css/css.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/htmlmixed/htmlmixed.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/clike/clike.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/addon/edit/matchbrackets.js" type="text/javascript"></script>
<!-- <script src="files/lib/CodeMirror-master/addon/display/autorefresh.js" type="text/javascript"></script> -->

<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.min.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin-lint.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/addon/lint/lint.min.js" type="text/javascript"></script>
<!-- <script src="files/lib/CodeMirror-master/addon/lint/lint.js" type="text/javascript"></script> -->
<script type="text/javascript" src="files/lib/CodeMirror-master/lib/util/formatting.js"></script>

<!-- <!-- <script type="text/javascript" src="files/lib/CodeMirror-master/addon/comment/comment.js"></script> -->
<!-- <script type="text/javascript" src="files/lib/CodeMirror-master/addon/comment/continuecomment.js"></script> -->


<!-- <script src="js/viewScript/atsgHelp.js" type="text/javascript"></script> -->

<script src="js/codeEditorForBDDScript.js" type="text/javascript"></script>

<style>

  .select2-drop-active {
	z-index:100060 !important;
  }
.select2-drop-mask {
	z-index:100054 !important;
}	
  .CodeMirror pre { padding-left: 7px; line-height: 1.25; }
  #repositoryEditor_bdd_dd>.input-medium{
  width:100% !important;
  }
  #testEditorObjectRepository_bdd_dd>.input-medium {
  width:270px!important;
  }
 .CodeMirror {
  height:382px !important;
  width:100%;
    border: 1px solid #ddd; 
  } 
  .tab-content>.active {
    display: block !important;
}
#toolMenu{
	background:#337ab7;
	color: #FFF;
	height: 35px;
    font-size: 13px;
    padding-top: 10px;
    padding-left: 13px;
}
<!-- Tool Tip CSS -->
a.tooltip {outline:none; }
a.tooltip strong {line-height:30px;}
a.tooltip:hover {text-decoration:none;} 
a.tooltip span {
    z-index:10;display:none; padding:14px 20px;
    margin-top:-30px; margin-left:28px;
    width:300px; line-height:16px;
}
a.tooltip:hover span{
    display:inline; position:absolute; color:#111;
    border:1px solid #DCA; background:#fffAF0;}
.callout {z-index:20;position:absolute;top:30px;border:0;left:-12px;}
    
/*CSS3 extras*/
a.tooltip span
{
    border-radius:4px;
    box-shadow: 5px 5px 8px #CCC;
}

.contextmenuPopup th, .contextmenuPopup td {
	padding:5px;
}
.scriptTextArea {
	width:100%;
	height:100%;
	padding-top:27%;
	text-align:center;
	font-size:15px;
}
.addUIObjectsPopup {
	text-align:left !important;
	padding-left:50px;
}
.form-group{
	margin-bottom:5px !important;
}
#uniform-isShareNo, #uniform-isShareYes, #uniform-typeText, #uniform-typeNumber, #uniform-typeDate {
	padding-top:1px !important;
	margin-right:2px;
}
.bootbox-alert .modal-footer button{
	color: #FFFFFF !important;
	background-color: #44b6ae !important;
}
    
</style>
</head>

<body>	


<!-- started popup -->

<div id="codeMirrorTextEditorBDD" class="modal "   data-backdrop="static" tabindex="-1" aria-hidden="true" style="width: 99%;left: 8px;top: 2%; padding: 0px;padding-left:0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close"  title="Press Esc to close" aria-hidden="true" onclick="closeCodeMirrorTextEditorBDD()"></button>
					<h4 class="modal-title theme-font"></h4>
					
						<div class="row">
						<div class="col-md-12" style="padding-left: 5px;">
						
							<h5 id="editorBDDsubTitle" class="modal-title" style="padding-top: 5px;float: left;"> </h5>
							
						
						<div class="col-md-2"> 
							<div id="testTollMaster_bdd_dd">
								<select class="select2me" id="testTollMaster_bdd_ul" style="width:160px;">
									<option  value="SELENIUM" ><a href="#">SELENIUM</a></option>
									<option  value="APPIUM" ><a href="#">APPIUM</a></option>
									<option  value="PROTRACTOR" ><a href="#">PROTRACTOR</a></option>
									<option  value="EDAT" ><a href="#">EDAT</a></option>
	    							<option  value="SEETEST" ><a href="#">SEETEST</a></option>
								</select>
							</div>
						 </div>						 
					 	<div id="editorLineNumBadge" style="display:inline-flex;float:right;"> 
					 		<h6 class="hidden"><span id="editorBadgeLineNumber" class="badge" style="background: #4db3a4 !important;width: 100px;padding-top: 3px;">50 Lines</span></h6>
					 		<h6><span id="editorBadgeReadOnly" class="badge" style="background: #4db3a4 !important;width: 100px;margin-left: 10px;cursor: default;">Editable</span></h6>
					 	</div>
					 	
					 	<div class="col-md-2" style="padding-left: 25px;">
							<div id="image-preview"></div>
						</div>					
							
						</div>
						
					</div>	
					
									
				</div>
				<div class="modal-body">					
				    <div>
				<!--  <div id="div1" class="scroller" >  -->
				 	<div class="slimScrollDiv" style="position: relative;float:left; overflow: hidden; width: 48%; height: 425px;"> 
				 		<ul class="nav nav-tabs " style="width:100%;margin:0px;">
	                      	<li class="testStoryTab active" style="width:50%;">
	                        	<a href="#" class="text-center;" onclick="testStoryContent()" data-toggle="tab" aria-expanded="false">Story
		                        	<span id="storyLocText"></span>
		                        	<div id="testTollMaster_bdd_version_dd_container" style="display:inline-block;float:right;margin-top:-2px;">
		                        		<span style="font-size:13px;">Version: </span> 
										<div id="testTollMaster_bdd_version_dd" style="display:inline-block;">
											<select class="select2me" id="testTollMaster_bdd_version_ul"></select>									
										</div>	
									</div>
								</a>
	                      	</li>
	                      	<li class="generatedScriptTab" style="width:50%;">
	                        	<a href="#" class="text-center;" onclick="generatedScriptContent()" data-toggle="tab" aria-expanded="true">Script
		                        	<span id="scriptLocText"></span>
		                        	<div id="scriptLanguageDD" style="display:inline-block;float:right;margin-top:-3px;">
<!-- 										<span style="font-size:13px;">Language: </span> 						 -->
										<div id="testTollMaster_bdd_scriptLanguage_dd" style="display:inline-block;">
											<select class="select2me" id="testTollMaster_bdd_scriptLanguage_ul"></select>									
										</div>	
									</div>
		                        	<div id="generationModeDD" style="display:inline-block;float:right;margin-top:-3px;margin-right:25px;">
										<div id="generationMode_dd" style="display:inline-block;width:100px;">
											<select class="select2me" id="generationModeDD_ul" style="width:100%;">
												<option id="TAF" value="TAF-MODE">TAF</option>
												<option id="GENERIC" value="GENERIC-MODE">GENERIC</option>
											</select>									
										</div>	
									</div>
	                        	</a>
	                      	</li>
	                      	<li id="amdocsId" style="display: none;">
	                        	<a href="#pageObjectDragItemsHeader" onclick="listingPageObjects()" data-toggle="tab">Page Objects</a>
	                    	</li>
	                	</ul>
						<div id="codeBDDContainer"  style="height: 425px;float:left;width:100% " data-always-visible="1" data-rail-visible1="1">
							<textarea id="codeEditorArea_BDD" ></textarea>
						</div> 
						<div id="codeMirrorTextEditorGeneratedScript" class="xmodal hidden" tabindex="-1" aria-hidden="false" xstyle="position:relative;height:381px;display:block;z-index: 100052;">
							<div class="xmodal-full">
								<div class="xmodal-content">
									<div class="xmodal-body" style="padding:0px;">					
										<!--  <div class="scroller" id="testing" style="height: 410px" data-always-visible="1" data-rail-visible1="1"> -->	 
										 <div id="testing" style="height:376px" data-always-visible="1" data-rail-visible1="1">	 
											<textarea id="java-code"></textarea>
										</div>
									</div>
								</div>
						</div>
					</div>
					</div>  					
					<!--  </div>  -->
					
					<div id="codeBDDContainerTest" style="height: 425px;float:left;width: 51%;margin-left: 5px;" data-always-visible="1" data-rail-visible1="1">

					<div id="keywordsDragItemsHeader" class="portlet box"
									style="width: 49%; background: white;float:left;margin-right: 3px;margin-bottom: 0px;background-color:#337ab7;border: 1px solid #337ab7 " data-force="30">
								<div class="portlet-title" style="min-height: 14px;">										
								  <h5>Available Items <span class='badge badge-default' id='keywordsDragItemsTotalCount' 
									style='float:right;background:#a294bb'>0</span> 
								  </h5>  	
								</div>	
								<div class="portlet-body" >
									<div class="row" style="margin-bottom: 10px;">										
									</div>
					
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="keywordssearchLeftDragItems" placeholder="Type to search"></div>
									</div>									
								 	<div class="a">	
								 			<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:83%">
												<img src="css/images/ajax-loader.gif"/>
											</div>									 
											<div>									 
											 	<ul id="keywordDragItemsContainer" class="block__list block__list_tags scroller"
												 style="padding-left: 0px;height: 362px;width: 330px;list-style-type: none;">
													<li style="color: black;">No Items to show</li>
												</ul>																					
									 	</div>
								  </div>									
								</div>
							</div>
			
		 <div class="tabbable-custom " style="width: 50%;border:1px solid #337ab7; height:478px;" id="toolMaindiv"> <!-- tabs opening -->
				<div id="toolMenu" style="">Tools</div> 	
				<ul class="nav nav-tabs " style="width: 100%;">
                      <li class="uiobjectTab active">
                          <a href="#leftDragItemsHeader" onclick="objectRepositoryFileContent()" data-toggle="tab" aria-expanded="false">UI Objects</a>
                      </li>
                      <li class="testDataTab">
                          <a href="#rightDragItemsHeader" onclick="testDataFileContent()" data-toggle="tab" aria-expanded="true">Test Data</a>
                      </li>
                      <li id="amdocsId">
                          <a href="#pageObjectDragItemsHeader" onclick="listingPageObjects()" data-toggle="tab">Page Objects</a>
                      </li>
                 </ul>
                <button style="float:right;margin-right:28px;margin-top:5px;"onclick="addUIObjectsFromEditor()"><i class="fa fa-plus" aria-hidden="true"></i></button> 
				<div style="height: 388px;border-left:none;border-right:none;"> <!-- Tabcontent opening --> 
					    <div id="leftDragItemsHeader" class="portlet box tab-pane active"
									style="width: 98%; background: white;float:left;margin-right: 10px;margin-bottom: 10px; data-force="30">								
								<div class="portlet-body" >
									<div class="row " style="margin-bottom: 10px;">										
										<div id="repositoryEditor_bdd_dd" class="col-md-16"  >
											<select class="input-medium  select2me"  id="objectrepositoryEditor_bdd_ul"  data-placeholder="Select..." ></select>				
										</div>
									</div>
					
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchLeftDragItemsUIObject" placeholder="Type to search"></div>
									</div>									
								 	<div class="a">	
								 			<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
												<img src="css/images/ajax-loader.gif"/>
											</div>									 
											<div>									 
											 	<ul id="leftDragItemsContainerCodeEditor" class="block__list block__list_tags scroller"
												 style="padding-left: 0px;height:289px;width: 330px;list-style-type: none;">
													<li style="color: black;">No Items to show</li>
												</ul>								
									 	</div>
								  </div>									
								</div>
							</div>			
					 
							 <div id="pageObjectDragItemsHeader"  class="portlet box tab-pane"
									style="width: 98%; display:none;background: white;float:left;margin-right: 10px;margin-bottom: 10px; data-force="30">									
									<div class="portlet-body" >									 
									 <div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="pageObjectSearchRightDragItems" placeholder="Type to search"></div>
									 </div>
									 <ul id="pageObjectrightDragItemsContainer" class="block__list block__list_tags scroller"
										style="padding-left: 0px; height: 300px;  background-color: white;">
										<li style="color: black;">No Items to show</li>
									 </ul>
								 </div>
							</div>
							
							<div id="rightDragItemsHeader"  class="portlet box tab-pane "
									style="width: 98%; background: white;float:left;margin-right: 10px;margin-bottom: 10px; data-force="30">
									<div class="portlet-body" >
									  <div class="row " style="margin-bottom: 10px;">
					 				  </div>  
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchRightDragItemsTestdata" placeholder="Type to search"></div>
									</div>
									<ul id="rightDragItemsContainerCodeEditor" class="block__list block__list_tags scroller"
										style="padding-left: 0px; height: 301px;  background-color: white;">
										<li style="color: black;">No Items to show</li>
									</ul>
								</div>
							</div>							
				</div>   <!-- Tabcontent closing -->							
			</div> <!-- tabs closing -->
						
					<!-- </div> -->
					</div>
					
					<div class="row">
						<div class="col-md-6"></div>
							<div class="col-md-6" style="padding-top: 5px;clear:both">
								 <button type="button" id="codeEditorBDDFormat" class="btn" onclick="codeEditorBDDFormatHandler();" style="background-color: transparent;
								    padding: 0px; margin-top: 3px;margin-right: -3px;"><a class="dt-button" href="#"><span><i class="fa fa-indent showHandCursor" title="Code Formatting"></i></span></a>
								</button>
								 <button type="button" id="codeEditorBDDSave" class="btn green-haze" disabled onclick="codeEditorBDDSaveHandler();">Save</button>
								 <button type="button" id="codeEditorBDDSaveAsNewVersion" class="btn green-haze" disabled onclick="codeEditorBDDSaveAsNewVersionHandler();">Save As New Version</button>
								 <button type="button" id="codeEditorBDDExecuteHandler" class="btn green-haze" onclick="codeEditorBDDExecuteHandler();"><img src="css/images/execute_metro.png" title="Execute TestCase" data-toggle="modal" class="showHandCursor"></button>	
								 <button type="button" id="codeEditorBDDGenerateScript" class="btn green-haze hidden" onclick="generateScriptSouce();">Generate Script</button>
								 <input type="button" id="downloadScriptBtn" nwsaveas="" class="btn green-haze hidden" value="Download" onclick="downloadScriptHandler()">
								 <button type="button" id="codeEditorBDDCommit" class="btn green-haze" disabled onclick="codeEditorBDDCommitHandlerInit();">Save & Commit</button>
								 <input id="codeEditorBDDCancel" type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="codeEditorBDDCancelHandler()">
							</div>
					</div> <!--1st  -->
				</div>
			</div>
	</div>
</div>
</div>
<!-- ended popup -->

<div id="contextMenuBDDKeyword" class="contextmenuPopup hidden" name="contextmenuPopup" style="position:absolute;top:36%;left:180px;width:35%;height:auto;max-height:318px;background-color:#ffffcc;border:1px solid lightgrey;z-index:10051;">
 	<div class="close" name="contextmenuPopupCloseBtn" style="position:absolute;right:2px;top:2px;z-index:10052;"></div>
	<div style="position:relative;width:100%;height:auto;max-height:310px;overflow-y:auto;font-family:monospace;">
		<div class="contextmenuPopupKeywords hidden">
			<div class="row" style="margin:10px;">
				<div class="namelabel" style="font-weight:bold;"></div>
				<div class="namevalue"></div>
			</div>
			<div class="row descriptionvalue" style="margin:10px;"></div>
			<div class="row" style="margin:10px;">
				<div class="uiobjectslabel" style="font-weight:bold;"></div>
				<div class="uiobjectsvalue"></div>
			</div>
			<div class="row" style="margin:10px;">
				<div class="parameterslabel" style="font-weight:bold;"></div>
				<div class="parametersvalue"></div>
			</div>
			<div class="row" style="margin:10px;">
				<div class="tagslabel" style="font-weight:bold;"></div>
				<div class="tagsvalue"></div>
			</div>
			<div class="row" style="margin:10px;">
				<div class="supportlabel" style="font-weight:bold;"></div>
				<table border style="width:95%;border-collapse:separate;border-spacing:1px;">
					<thead>
						<tr>
							<th></th>
							<th class="text-center">Selenium</th>
							<th class="text-center">Appium</th>
							<th class="text-center">SeeTest</th>
							<th class="text-center">Protractor</th>
							<th class="text-center">EDAT</th>
							<th class="text-center">CodedUI</th>
							<th class="text-center">TestComplete</th>
							<th class="text-center">RestAssured</th>
						</tr>
					</thead>
					<tbody class="text-center">
						<tr>
							<td class="text-left" style="padding-left:4px;">Script Generation</td>
							<td class="sgseleniumvalue"></td>
							<td class="sgappvalue"></td>
							<td class="sgseetestvalue"></td>
							<td class="sgprotractorvalue"></td>
							<td class="sgedatvalue"></td>
							<td class="sgcodeduivalue"></td>
							<td class="sgtestcompletevalue"></td>
							<td class="sgrestassuredvalue"></td>
						</tr>
						<tr>
							<td class="text-left" style="padding-left:4px;">Script Execution</td>
							<td class="seseleniumvalue"></td>
							<td class="seappvalue"></td>
							<td class="seseetestvalue"></td>
							<td class="seprotractorvalue"></td>
							<td class="seedatvalue"></td>
							<td class="secodeduivalue"></td>
							<td class="setestcompletevalue"></td>
							<td class="serestassuredvalue"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class="contextmenuPopupUIObjects hidden">
			<div class="row" style="margin:10px;">
				<div class="elementlabel" style="font-weight:bold;"></div>
				<div class="elementvalue"></div>
			</div>
			<div class="row descriptionvalue" style="margin:10px;"></div>
			<div class="row" style="margin:10px;">
				<div class="idtypelabel" style="font-weight:bold;"></div>
				<div class="idtypevalue"></div>
			</div>
			<table class="uiObjectBrowserTable hidden" border style="margin:10px;width:95%;border-collapse:separate;border-spacing:1px;">
				<thead>
					<tr>
						<th class="text-center" style="width:15%;">Browser</th>
						<th class="text-center">X-Path </th>
						<th class="text-center">CSS Selector</th>
						<th class="text-center hidden">Locale </th>
					</tr>
				</thead>
				<tbody class="text-center">
					<tr>
						<td class="text-left" style="padding-left:4px;">Chrome</td>
						<td class="chromexpathvalue text-left"></td>
						<td class="chromecssvalue text-left"></td>
						<td class="chromelocalvalue hidden"></td>
					</tr>
					<tr>
						<td class="text-left" style="padding-left:4px;">FireFox</td>
						<td class="firefoxxpathvalue text-left"></td>
						<td class="firefoxcssvalue text-left"></td>
						<td class="firefoxlocalvalue hidden"></td>
					</tr>
					<tr>
						<td class="text-left" style="padding-left:4px;">Safari</td>
						<td class="safarixpathvalue text-left"></td>
						<td class="safaricssvalue text-left"></td>
						<td class="safarilocalvalue hidden"></td>
					</tr>
					<tr>
						<td class="text-left" style="padding-left:4px;">Internet Explorer</td>
						<td class="iexpathvalue text-left"></td>
						<td class="iecssvalue text-left"></td>
						<td class="ielocalvalue hidden"></td>
					</tr>
					<tr>
						<td class="text-left" style="padding-left:4px;">FireFox Gecko</td>
						<td class="firefoxgeckoxpathvalue text-left"></td>
						<td class="firefoxgeckocssvalue text-left"></td>
						<td class="firefoxgeckolocalvalue hidden"></td>
					</tr>
					<tr>
						<td class="text-left" style="padding-left:4px;">Edge</td>
						<td class="edgexpathvalue text-left"></td>
						<td class="edgecssvalue text-left"></td>
						<td class="edgelocalvalue hidden"></td>
					</tr>
				</tbody>
			</table>
		</div>	

		<div class="contextmenuPopupTestData hidden">
			<div class="row" style="margin:10px;">
				<div class="dataitemlabel" style="font-weight:bold;"></div>
				<div class="dataitemvalue"></div>
			</div>
			<div class="row descriptionvalue" style="margin:10px;"></div>
			<div class="row" style="margin:10px;">
				<div class="typelabel" style="font-weight:bold;"></div>
				<div class="typevalue"></div>
			</div>
			<div class="row" style="margin:10px;">
				<div class="valueslabel" style="font-weight:bold;"></div>
				<div class="valuesvalue"></div>
			</div>
		</div>	

		<div class="contextmenuPopupPageObjects hidden">
			<div class="contextmenuPopupPageObjectsLevelOne hidden">
				<div class="row" style="margin:10px;">
					<div class="packagenamelabel" style="font-weight:bold;"></div>
					<div class="packagenamevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="namelabel" style="font-weight:bold;"></div>
					<div class="namevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="testcasenamelabel" style="font-weight:bold;"></div>
					<div class="testcasenamevalue"></div>
				</div>
			</div>
			<div class="contextmenuPopupPageObjectsLevelTwo hidden">
				<div class="row" style="margin:10px;">
					<div class="packagenamelabel" style="font-weight:bold;"></div>
					<div class="packagenamevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="namelabel" style="font-weight:bold;"></div>
					<div class="namevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="methodnamelabel" style="font-weight:bold;"></div>
					<div class="methodnamevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="paramlabel" style="font-weight:bold;"></div>
					<div class="paramvalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="paramtypelabel" style="font-weight:bold;"></div>
					<div class="paramtypevalue"></div>
				</div>
				<div class="row" style="margin:10px;">
					<div class="returntypelabel" style="font-weight:bold;"></div>
					<div class="returntypevalue"></div>
				</div>
			</div>	
			
		</div>	

	</div>
</div>
           
<!-- BEGIN - addUIObjectsFromEditor -->
<div id="addUIObjectsFromEditorContainer" class="hidden" style="width: 100%;height: 100%;top: 0px;left: 0px;position: absolute;z-index: 10053;">
	<div id="addUIObjectsFromEditor" class="modal hidden" tabindex="-1" aria-hidden="false" style="width:99%;height:98%;top:8px;left:17px;margin-left:-9px;xpadding-left:8px !important;display:block;z-index:100052;">
		<div class="modal-full">
			<div class="modal-content" style="background-color:#e6e6e6;">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close"  title="Press Esc to close" aria-hidden="true" onclick="closeAddUIObjectsFromEditorPopup()"></button>
					<h4 class="modal-title theme-font">Add a new UI Object Element</h4>
				</div>		
				<div class="modal-body col-md-12" style="padding:0px;">
					<form id="myUIObjectForm" class="form-horizontal" role="form">
					<div class="portlet box blue col-md-6" style="margin:10px 10px 10px 40px;">
	                    <div class="portlet-title">
	                        <div class="caption">Common</div>
	                    </div>
	                    <div class="portlet-body form">
				        	<div class="form-body">
				            	<div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Name</label>
			                        <div class="col-md-7">
			                            <input id="uiObjectName" type="text" class="form-control" name="elementName">
			                            <span id="uiObjectNameError" class="help-block hidden" style="color:red;">Please enter name</span>
			                        </div>
			                    </div>
			                     <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Description</label>
			                        <div class="col-md-7">
			                            <textarea class="form-control" rows="3" name="description"></textarea>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Group</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="groupName">
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Handle</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="handle">
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Element Type</label>
			                        <div class="col-md-7">
			                            <select class="form-control" name="elementType">
			                                <option>Text</option>
			                                <option>Password</option>
			                                <option>Checkbox</option>
			                                <option>Scrollable Checkbox</option>
			                                <option>Checkbox List</option>
			                                <option>Dropdown</option>
			                                <option>Radio Button</option>
			                                <option>List</option>
			                                <option>Text Area</option>
			                                <option>Option</option>
			                                <option>File Select box</option>
			                                <option>Select</option>
			                                <option>List Item</option>
			                                <option>Submit</option>
			                                <option>Reset</option>
			                                <option>Combo box</option>
			                                <option>iFrame</option>
			                            </select>
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Page Name</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="pageName">
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Id Type</label>
			                        <div class="col-md-7">
			                            <select id="idTypeDD" class="form-control" name="idType">
			                                <option>id</option>
			                                <option>xpath</option>
			                                <option>cssSelector</option>
			                                <option>className</option>
			                                <option>tagName</option>
			                                <option>linkText</option>
			                                <option>name</option>
			                                <option>partialLinkText</option>
			                                <option>cssContainingText</option>
			                                <option>buttonText</option>
			                                <option>partialButtonText</option>
			                                <option>binding</option>
			                                <option>repeater</option>
			                                <option>model</option>
			                                 <option>src</option>
			                            </select>
			                        </div>
			                    </div>
			                    <div class="form-group hidden" id="chrome">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Chrome</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="chrome">
			                        </div>
			                    </div>
			                    <div class="form-group hidden" id="firefox">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Firefox</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="firefox">
			                        </div>
			                    </div>
			                    <div class="form-group hidden" id="ie">
			                        <label class="col-md-4 control-label addUIObjectsPopup">IE</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="ie">
			                        </div>
			                    </div>
			                    <div class="form-group hidden" id="safari">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Safari</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="safari">
			                        </div>
			                    </div>
			                    <div class="form-group hidden" id="firefoxgecko">
			                        <label class="col-md-4 control-label addUIObjectsPopup">FireFoxGecko</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="firefoxgecko">
			                        </div>
			                    </div>	
			                    <div class="form-group hidden" id="edge">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Edge</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="edge">
			                        </div>
			                    </div>		
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Application Type</label>
			                        <div class="col-md-7">
			                            <select class="form-control" name="testEngineName">
			                                <option>Web</option>
			                                <option>Mobile</option>
			                                <option>Desktop</option>
			                            </select>
			                        </div>
			                    </div>
								<!-- <div class="form-group">
                                    <label class="col-md-4 control-label addUIObjectsPopup">Share</label>
                                    <div class="md-radio-inline">
                                        <label class="md-radio" style="margin-left:10px;">
                                            <input type="radio" name="isShare" id="isShareYes" value="yes" checked=""> YES
                                            <span style="margin-top:-7px;"></span>
                                        </label>
                                        <label class="md-radio">
                                            <input type="radio" name="isShare" id="isShareNo" value="no"> NO
                                            <span style="margin-top:-7px;"></span>
                                        </label>
                                    </div>
                                </div>
                               -->  			                    
                            </div>
	                    </div>
	                </div>
				
				
				<div class="portlet box blue col-md-5 hidden" id="mobileContainer" style="margin:10px;border:1px solid #5C9BD1 !important;background:#fff;">
                    <div class="portlet-title" style="background:#5C9BD1 !important;">
                        <div class="caption">Mobile</div>
                    </div>
                    <div class="portlet-body form">
                        <div class="form-horizontal col-md-6" role="form" style="padding:0px;background:lightgrey;">
		                    <div class="portlet box blue" style="margin-bottom:0px !important;border:1px solid grey !important;">
			                    <div class="portlet-title" style="background:grey !important;">
			                        <div class="caption" style="font-size:16px;">SeeTest</div>
			                    </div>
			                </div>
			                <div class="portlet-body form">
                        		<div class="form-horizontal" role="form">    
						        	<div class="form-body">
					                    <div class="form-group">
					                        <label class="col-md-4 control-label">Zone</label>
					                        <div class="col-md-7">
					                            <input type="text" class="form-control" name="seeTestZoneIndex">
					                        </div>
					                    </div>
					                    <div class="form-group">
					                        <label class="col-md-4 control-label">Index</label>
					                        <div class="col-md-7">
					                            <input type="text" class="form-control" name="seeTestIndex">
					                        </div>
					                    </div>
					                    <div class="form-group">
					                        <label class="col-md-4 control-label">Label</label>
					                        <div class="col-md-7">
					                            <input type="text" class="form-control" name="seetestLabel">
					                        </div>
					                    </div>
		                            </div>
		                    	</div>
		                	</div>
                        </div>
                        <div class="form-horizontal col-md-6" role="form" style="padding:0px;background:#ccccb3;">
		                    <div class="portlet box blue" style="margin-bottom:0px !important;border:1px solid #adad85 !important;">
			                    <div class="portlet-title" style="background:#adad85 !important;">
			                        <div class="caption" style="font-size:16px;">Appium</div>
			                    </div>
			                </div>
			                <div class="portlet-body form">
                        		<div class="form-horizontal" role="form">    
						        	<div class="form-body">
					                    <div class="form-group" style="height:112px;">
					                        <label class="col-md-4 control-label">Appium</label>
					                        <div class="col-md-7">
					                            <input type="text" class="form-control" name="appiumLabel">
					                        </div>
					                    </div>
		                            </div>
		                    	</div>
		                	</div>
                            
                        </div>                        
                    </div>
                </div>
				
				<div class="portlet box blue col-md-5 hidden" id="desktopContainer" style="margin:10px;border:1px solid #ff794d !important;">
	                    <div class="portlet-title" style="background:#ff794d !important;">
	                        <div class="caption">Desktop</div>
	                    </div>
	                    <div class="portlet-body form">
				        	<div class="form-body">
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">CodedUI</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="codeduiLabel">
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">TestComplete</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="testCompleteLabel">
			                        </div>
			                    </div>
                            </div>
	                    </div>
	                </div>
				
					<div class="portlet box blue col-md-5 hidden" id="webContainer" style="margin:10px;border:1px solid #F3565D !important;">
	                    <div class="portlet-title" style="background:#F3565D !important;">
	                        <div class="caption">Web</div>
	                    </div>
	                    <div class="portlet-body form">
				        	<div class="form-body">
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Page URL</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="pageURL">
			                        </div>
			                    </div>
			                    <div class="form-group">
			                        <label class="col-md-4 control-label addUIObjectsPopup">Label</label>
			                        <div class="col-md-7">
			                            <input type="text" class="form-control" name="weblabel">
			                        </div>
			                    </div>
                            </div>
	                    </div>
	                </div>
				
				 		<div class="portlet light" style="background-color:#e6e6e6;">
					        <div class="portlet-body form" style="padding:30px !important;">
				                <div>
				                    <div class="row">
				                        <div class="text-center">
				                        	<input type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="closeAddUIObjectsFromEditorPopup()">
				                            <button type="submit" id="addUIObjectSubmitBtn" onclick="addUIObjectsFormSubmit()" class="btn green">Submit</button>
				                        </div>
				                    </div>
				                </div>
					        </div>
					    </div>	
					</form>	    				
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END - addUIObjectsFromEditor -->


<!-- BEGIN - addTestDataFromEditor -->
<div id="addTestDataFromEditorContainer" class="hidden" style="width: 100%;height: 100%;top: 0px;left: 0px;position: absolute;z-index: 10053;">
	<div id="addTestDataFromEditor" class="modal hidden" tabindex="-1" aria-hidden="false" style="width:50%;height:555px;top:6%;left:26%;display:block;z-index:100052;">
		<div class="modal-full">
			<div class="modal-content" style="background-color:#e6e6e6;">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close"  title="Press Esc to close" aria-hidden="true" onclick="closeAddTestDataFromEditorPopup()"></button>
					<h4 class="modal-title theme-font">Add a new Test Data</h4>
				</div>		
				<div class="modal-body" style="padding:0px;">
						<div class="portlet light xbordered" style="background-color:#e6e6e6;">
					        <div class="portlet-body form">
					            <form id="myTestDataForm" class="form-horizontal" role="form">
					                <div class="form-body">
					                    <div class="form-group">
					                        <label class="col-md-4 control-label addUIObjectsPopup">Name</label>
					                        <div class="col-md-6">
					                            <input id="testDataName" type="text" class="form-control" name="dataName">
					                            <span id="testDataNameError" class="help-block hidden" style="color:red;">Please enter name</span>
					                        </div>
					                    </div>
										<div class="form-group">
		                                    <label class="col-md-4 control-label addUIObjectsPopup">Type</label>
		                                    <div class="md-radio-inline">
		                                        <label class="md-radio" style="margin-left:10px;">
		                                            <input type="radio" name="type" id="typeText" value="text" checked=""> TEXT
		                                            <span style="margin-top:-7px;"></span>
		                                        </label>
		                                        <label class="md-radio">
		                                            <input type="radio" name="type" id="typeNumber" value="number"> NUMBER
		                                            <span style="margin-top:-7px;"></span>
		                                        </label>
		                                        <label class="md-radio">
		                                            <input type="radio" name="type" id="typeDate" value="date"> DATE
		                                            <span style="margin-top:-7px;"></span>
		                                        </label>
		                                    </div>
		                                </div>
					                    
					                    <div class="form-group">
					                        <label class="col-md-4 control-label addUIObjectsPopup">Remarks</label>
					                        <div class="col-md-6">
					                            <textarea class="form-control" rows="3" name="remarks"></textarea>
					                        </div>
					                    </div>
					                   <div class="form-group">
					                        <label class="col-md-4 control-label addUIObjectsPopup">Handle</label>
					                        <div class="col-md-6">
					                            <input id="handle" type="text" class="form-control" name="handle">
					                        </div>
					                    </div>
					                </div>
									<!-- <div class="form-group">
	                                    <label class="col-md-4 control-label addUIObjectsPopup">Share</label>
	                                    <div class="md-radio-inline">
	                                        <label class="md-radio" style="margin-left:10px;">
	                                            <input type="radio" name="isShare" id="isShareYes" value="yes" checked=""> YES
	                                            <span style="margin-top:-7px;"></span>
	                                        </label>
	                                        <label class="md-radio">
	                                            <input type="radio" name="isShare" id="isShareNo" value="no"> NO
	                                            <span style="margin-top:-7px;"></span>
	                                        </label>
	                                    </div>
	                                </div> -->
					                
					                <div>
					                    <div class="row">
					                        <div class="text-center">
					                        	<input type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="closeAddTestDataFromEditorPopup()">
					                            <button type="submit" id="addUIObjectSubmitBtn" onclick="addTestDataFormSubmit()" class="btn green">Submit</button>
					                        </div>
					                    </div>
					                </div>
					            </form>
					        </div>
					    </div>					
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END - addUIObjectsFromEditor -->
<%-- <div><%@include file="dragDropListItems.jsp"%></div> --%>
</body>
 <script>
var amdocsFlag = '';
amdocsFlag ="${amdocsFlag}";
if(amdocsFlag == 'NO'){
$("#toolMaindiv #amdocsId").hide();
}
</script> 
</html>