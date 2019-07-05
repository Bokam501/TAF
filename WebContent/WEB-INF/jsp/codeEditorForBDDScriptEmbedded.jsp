<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.min.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.js"  charset="UTF-8" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/addon/lint/lint.min.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin-lint.js" type="text/javascript"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/lib/util/formatting.js"></script>
<script src="js/codeEditorForBDDScriptEmbedded.js" type="text/javascript"></script>

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
    
 .CodeMirror {
  height:420px;
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

</style>
</head>

<body>	

<!-- started popup -->

<div id="codeMirrorTextEditorBDDEmbedded" class="modal "   data-backdrop="static" tabindex="-1" aria-hidden="true" style="width: 99%;left: 8px;top: 2%; padding: 0px;padding-left:0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close"  title="Press Esc to close" aria-hidden="true" onclick="closecodeMirrorTextEditorBDDEmbedded()"></button>
					<h4 class="modal-title theme-font"></h4>
					<h5 class="modal-title theme-font" style="padding-left: 20px;margin: 0px 0px;height: 10px;"></h5>
					
						<div class="row belowHeader">
						<div class="col-md-12" style="padding-left: 5px;">
						
							<h5 class="modal-title" style="padding-top: 5px;float: left;"> </h5>
							
						
						<div class="col-md-3"> 
							<div id="testTollMaster_bddEmbedded_dd">
								<select class="input-medium select2me" id="testTollMaster_bddEmbedded_ul" >			        			 
									<option  value="EDAT" ><a href="#">EDAT</a></option>
								</select>									
							</div>	
						 </div>
					 	<div class="col-md-2" style="display: inline-flex;"> 
					 		<h6 class="hidden"><span id="editorBadgeLineNumberEmbedded" class="badge" style="background: #4db3a4 !important;width: 100px;padding-top: 3px;">50 Lines</span></h6>
					 		<h6><span id="editorBadgeReadOnlyEmbedded" class="badge" style="background: #4db3a4 !important;width: 100px;margin-left: 10px;cursor: default;">Editable</span></h6>
					 	</div>
					 	
					 	<div class="col-md-2" style="padding-left: 25px;">
							<div id="image-preview"></div>							
						</div>							
						</div>					 
					</div>					
				</div>
					<div class="modal-body">					
				   <div>				
				  <div class="slimScrollDiv" style="position: relative;float:left; overflow: hidden; width: 48%; height: 425px;">
				   		<ul class="nav nav-tabs " style="width:100%;margin:0px;">
	                      	<li class="testStoryTabEDAT active" style="width:50%;">
	                        	<a href="#" class="text-center;" onclick="testStoryContentEDAT()" data-toggle="tab" aria-expanded="false">Story
	                        		<span id="storyLocTextEDAT"></span>
		                        	<div id="testTollMaster_bdd_version_dd_container_EDAT" style="display:inline-block;float:right;margin-top:-2px;">
		                        		<span style="font-size:13px;">Version: </span> 
										<div id="testTollMaster_bdd_version_dd" style="display:inline-block;">
											<select class="select2me" id="testTollMaster_bdd_version_ul_EDAT"></select>									
										</div>	
									</div>
								</a>
	                      	</li>
	                      	<li class="generatedScriptTabEDAT" style="width:50%;">
	                        	<a href="#" class="text-center;" onclick="generatedScriptContentEDAT()" data-toggle="tab" aria-expanded="true">Script
	                        		<span id="scriptLocTextEDAT"></span>
		                        	<div id="scriptLanguageDDEDAT" style="display:inline-block;float:right;margin-top:-3px;width:120px;">
<!-- 										<span style="font-size:13px;">Language: </span> 						 -->
										<div id="testTollMaster_bdd_scriptLanguage_dd" style="display:inline-block;width:100%;">
											<select class="select2me" id="testTollMaster_bdd_scriptLanguage_ul_EDAT" style="width:100%;"></select>									
										</div>	
									</div>
	                        	</a>
	                      	</li>
	                	</ul>
				   
					<div id="codeBDDEmbeddedContainer"  style="height: 425px;float:left;width:100% " data-always-visible="1" data-rail-visible1="1">											
						<textarea id="codeEditorArea_BDDEmbedded" ></textarea>
					</div> 
					<div id="codeMirrorTextEditorGeneratedScriptEDAT" class="xmodal hidden" tabindex="-1" aria-hidden="false" xstyle="position:relative;height:381px;display:block;z-index: 100052;">
							<div class="xmodal-full">
								<div class="xmodal-content">
									<div class="xmodal-body" style="padding:0px;">					
										<!--  <div class="scroller" id="testing" style="height: 410px" data-always-visible="1" data-rail-visible1="1"> -->	 
										 <div id="testing" style="height:376px" data-always-visible="1" data-rail-visible1="1">	 
											<textarea id="java-code-edat"></textarea>
										</div>
									</div>
								</div>
						</div>
					</div>
						  
					 </div>			
					
					<div id="codeBDDEmbeddedContainerTest" style="height: 425px;float:left;width: 51%;margin-left: 5px;" data-always-visible="1" data-rail-visible1="1">

					<div id="keywordsDragItemsHeaderEmbedded" class="portlet box"
							style="width: 49%; background: white;float:left;margin-right: 3px;margin-bottom: 0px;background-color:#337ab7;border: 1px solid #337ab7 " data-force="30">
						<div class="portlet-title" style="min-height: 14px;">										
						  <h5>Available Items <span class='badge badge-default' id='keywordsDragItemsTotalCountEmbedded' 
							style='float:right;background:#a294bb'>0</span> 
						  </h5>  	
						</div>	
						<div class="portlet-body" >
							<div class="row" style="margin-bottom: 10px;">										
							</div>
			
							<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
								<input type="text" class="form-control" id="keywordssearchLeftDragItemsEmbedded" placeholder="Type to search"></div>
							</div>									
						 	<div class="a">	
						 			<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
										<img src="css/images/ajax-loader.gif"/>
									</div>									 
									<div>									 
									 	<ul id="keywordDragItemsContainerEmbedded" class="block__list block__list_tags scroller"
										 style="padding-left: 0px;height: 345px;width: 330px;list-style-type: none;">
											<li style="color: black;">No Items to show</li>
										</ul>																						
							 		</div>
						  	</div>									
						</div>
					</div>
			
		 <div class="tabbable-custom " style="width: 50%;border:1px solid #337ab7; height:460px;" id="toolMaindiv"> <!-- tabs opening -->
				<div id="toolMenu" style="">Tools</div> 	
				<ul class="nav nav-tabs" style="width: 100%;">
                      <li class="active">
                          <a href="#leftDragItemsHeaderEmbedded" onclick = "objectRepositoryFileContentEmbedded()" data-toggle="tab" aria-expanded="false">Device Objects</a>
                      </li>                      
                 </ul>
				<div class="tab-content" style="height: 382px;border-left:none;border-right:none;"> <!-- Tabcontent opening --> 
					 <div id="leftDragItemsHeaderEmbedded" class="portlet box tab-pane active"
									style="width: 98%; background: white;float:left;margin-right: 10px;margin-bottom: 10px; data-force="30">								
							<div class="portlet-body" >
								<div class="row " style="margin-bottom: 10px;">										
									<div id="repositoryEditor_bdd_dd" class="col-md-16"  >
										<select class="input-medium  select2me"  id="objectrepositoryEditorEmbedded_bdd_ul"  data-placeholder="Select..." ></select>				
									</div>
								</div>
				
								<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
									<input type="text" class="form-control" id="searchLeftDragItemsEmbedded" placeholder="Type to search"></div>
								</div>									
							 	<div class="a">	
							 			<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
											<img src="css/images/ajax-loader.gif"/>
										</div>									 
										<div>									 
										 	<ul id="leftDragItemsContainerEmbedded" class="block__list block__list_tags scroller"
											 style="padding-left: 0px;height: 301px;width: 330px;list-style-type: none;">
												<li style="color: black;">No Items to show</li>
											</ul>								
								 	</div>
							  </div>									
							</div>
						</div>						 						
					</div>   <!-- Tabcontent closing -->							
				</div> <!-- tabs closing -->					
				</div>
					
					<div class="row">
						<div class="col-md-6"></div>
						<div class="col-md-6" style="padding-top: 5px;clear:both">
						 <button type="button" id="codeEditorBDDEmbeddedSave" class="btn green-haze" onclick="codeEditorBDDEmbeddedSaveHandler();">Save</button>						
						 <button type="button" id="codeEditorBDDGenerateScriptEDAT" class="btn green-haze hidden" onclick="generateScriptSouce();">Generate Script</button>
						 <button type="button" id="codeEditorBDDSaveAsNewVersionEDAT" class="btn green-haze" onclick="codeEditorBDDEmbeddedSaveAsNewVersionHandler();">Save As New Version</button>
						 <input type="button" id="downloadScriptBtnEDAT" nwsaveas="" class="btn green-haze hidden" value="Download" onclick="downloadScriptHandler()">
						 <button type="button" id="codeEditorBDDEmbeddedCommit" class="btn green-haze" onclick="codeEditorBDDEmbeddedCommitHandlerInit();">Save & Commit</button>
						 <input id="codeEditorBDDEmbeddedCancel" type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="codeEditorBDDEmbeddedCancelHandler()">						
					</div>				 
					</div>
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
						<td class="text-left" style="padding-left:4px;">Firefox</td>
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
</body>
 
<script>
	var amdocsFlag = '';
	amdocsFlag ="${amdocsFlag}";
	if(amdocsFlag == 'NO'){
		$("#toolMaindiv #amdocsId").hide();
	}
</script>
 
</html>