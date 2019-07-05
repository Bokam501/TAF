var parentTreeData;
var selectedNodeDetails;
var sourceItemId, destinationItemId, currentItemId;
var nodeDroppedOnTreePopup = false;
var urlToUpdateParent;

var TreePagination = function() {	
	   var initialise = function(jsonObj){
		   $('#treeSelectFeatureTestcasesRowCount').find('option[value="50"]').attr("selected", true);
		   showTreePagination(jsonObj);		   
	   };
			return {				
	        init: function(jsonObj) {        	
	        	initialise(jsonObj);
	        }		
		};	
}();

function showTreePagination(jsonObj){
	nodeDroppedOnTreePopup=false;
	initializeRightSideHandlers();
	getParentNodes(jsonObj.urlToGetTree, jsonObj.urlToGetChildData);
	
	$("#treePaginationContainer h4").text("");
	$("#treePaginationContainer h4").text(jsonObj.Title);	
	$("#treePaginationContainer").modal();
	
	$("#treeJtableTestCaseExecution, #treeJtableTestCaseDefect, #dataTableContainerForExecutions, #dataTableContainerForDefectDetails").empty();
	$('#treeEntityDetails').empty();
	$('#treeTestCaseFeatureMappingContainer').hide();
	(!featureTCFlag) ? $('#tablistActivities').hide() : $('#tablistActivities').show();
}

function getParentNodes(treeFilterUrl, urlToGetChildData){	
	var elementDivId = "#parentTree";
	
	$(elementDivId).jstree("destroy");
		$(elementDivId).jstree({
			  "core" : {
				  	"check_callback" : function(operation, node, node_parent, node_position, more) {
                        if (operation == 'move_node') {
                        	
                        	if ((node.data == 'Feature' )) {
                        		currentItemId = node.id;
                        		sourceItemId = node.parent;
                        		destinationItemId = node_parent.id;
                        		
                            	if (sourceItemId == undefined)
                            		sourceItemId = -1;
                            	if (destinationItemId == undefined)
                            		destinationItemId = -1;
                            	urlToUpdateParent = 'administration.product.feature.update.parent?featureId='+currentItemId+'&oldParentFeatureId='+sourceItemId+'&newParentFeatureId='+destinationItemId;
                            	
                            	if(node.parent == "#" || node_parent.id == "#"){ 
                            		nodeDroppedOnTreePopup=false;
                            		return false;
                            	}
                            	
                                return true;
                            } 
                        	else if ((node.data == 'UserSkills' )) {
                            	currentItemId = node.id;
                        		sourceItemId = node.parent;
                        		destinationItemId = node_parent.id;
                            	
                            	if(node.parent == "#" || node_parent.id == "#"){ 
                            		nodeDroppedOnTreePopup=false;
                            		return false;
                            	
                            	}else{                            	
	                            	if (sourceItemId == undefined)
	                            		sourceItemId = -1;
	                            	if (destinationItemId == undefined)
	                            		destinationItemId = -1;
	                            	
	                            	urlToUpdateParent = 'administration.skill.update.parent?skillId='+currentItemId+'&oldParentSkillId='+sourceItemId+'&newParentSkillId='+destinationItemId;
	                                return true;
                            	}
                            }
                        	else if ((node.data == 'EnvironmentCategory' )) {
                            	currentItemId = node.id;
                        		sourceItemId = node.parent;
                        		destinationItemId = node_parent.id;
                            	
                            	if(node.parent == "#" || node_parent.id == "#"){  
                            		nodeDroppedOnTreePopup=false
                            		return false;
                            	
                            	}else{                            	
	                            	if (sourceItemId == undefined)
	                            		sourceItemId = -1;
	                            	if (destinationItemId == undefined)
	                            		destinationItemId = -1;
	                            	
	                            	urlToUpdateParent = 'environment.category.update.parent?environmentCategoryId='+currentItemId+'&oldParentCategoryId='+sourceItemId+'&newParentCategoryId='+destinationItemId;
	                                return true;
                            	}
                            }
                        	else if ((node.data == 'DecouplingCategory' )) {
                            	currentItemId = node.id;
                        		sourceItemId = node.parent;
                        		destinationItemId = node_parent.id;
                            	
                            	if(node.parent == "#" || node_parent.id == "#"){  
                            		nodeDroppedOnTreePopup=false
                            		return false;
                            	
                            	}else{                            	
	                            	if (sourceItemId == undefined)
	                            		sourceItemId = -1;
	                            	if (destinationItemId == undefined)
	                            		destinationItemId = -1;
	                            	
	                            	urlToUpdateParent = 'administration.product.decouplingcategory.update.parent?decouplingCategoryId='+currentItemId+'&oldParentCategoryId='+sourceItemId+'&newParentCategoryId='+destinationItemId;
	                                return true;
                            	}
                            }
                        	else{
                                return false;
                        	}
                        }                                               
                    },
				    'data' :  {
				      'type' : 'POST',
				      'url' : function (node) {
				    	  
				    	  if(node.data == "TestCase" || node.data == "EnvironmentCategory") {
				    		  var nodeId=node.id;
				    		  if(nodeId.includes('F') || nodeId.includes('S') || nodeId.includes('C')) {
				    			  nodeId = nodeId.substring(1,nodeId.length);
				    		  } 
				    		  return node.id === "#" ? treeFilterUrl : urlToGetChildData+nodeId+'&node='+node.data;
				    	  } else {
				    		  var nodeId=node.id;
				    		  if(nodeId.includes('F') || nodeId.includes('S') || nodeId.includes('C')) {
				    			  nodeId = nodeId.substring(1,nodeId.length);
				    		  }
				    		  return node.id === "#" ? treeFilterUrl : urlToGetChildData+nodeId+'&node='+node.data; 
				    	  }
				      },
				      'data' : function (node) {			    	
				    	  		return { 'id' : node.id };
				      }
				      
				    } 
				  },
			    "dnd" : {
				    "drag_check" : {
				    	
				    },
					"drag_finish" : function(data){
					   callAlert("ok");
				   }
			   }, 
	 	       "plugins" : [ "contextmenu", "themes", "ui", "dnd"]
		  });
        
	     $(elementDivId).on("loaded.jstree", function(evt, data){
	    	 if($('#parentTree .jstree-anchor i').length == 0)
	    		 $('#treeTestCaseFeatureMappingContainer').css('display','block').html('No data Available');
	    	 else
	    		 $('#treeTestCaseFeatureMappingContainer').css('display','none').html('');	 
	    	 setTreeNodeAttr();
	     });
	     $(elementDivId).on("open_node.jstree", function(evt, data){
	    	 setTreeNodeAttr();
	     });
	     $(elementDivId).on("move_node.jstree", function(evt, data){
	    	 $(elementDivId).jstree("create_node", data.parent, function(e,data){});				
	    	 performNodeDragAndDrop(urlToUpdateParent);				
	     });
		rebindEvent(elementDivId);
		
		$(document).on('dnd_stop.vakata', function (e, data) {
			console.log("dropped item");		
			nodeDroppedOnTreePopup=true;
			performNodeDragAndDrop(urlToUpdateParent);
		});
} 

function performNodeDragAndDrop(urlToUpdateParent){
	if(nodeDroppedOnTreePopup) {
		nodeDroppedOnTreePopup=false;
		console.log("urlToUpdateParent : "+urlToUpdateParent+" nodeDroppedOnTreePopup : "+nodeDroppedOnTreePopup);
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : urlToUpdateParent,
			dataType : 'json',
			success : function(data) {
				console.log("data");				
				$('#parentTree').jstree(true).refresh();
				console.log("refreshed ");
			}       
		});
	}
}

function rebindEvent(elementDivId) {
	$(elementDivId).on("select_node.jstree",
	     function(evt, data){
			selectedNodeDetails = data;		
			handleTreeNodeSelection(data);
	});
}

function setTreeNodeAttr() {
	$.each($(".jstree-anchor"), function(ind, elem) {
		$(elem).removeAttr("title");
 		$(elem).attr("title", $(elem).text()); 		
  	});
}

function initializeRightSideHandlers(){
	$('#treeTestCaseFeatureMappingContainer').hide();
	$("#treeJTableDivId").hide();
	$("#treeJtableTestCaseExecution, #dataTableContainerForExecutions").hide();
	$("#treeJtableTestCaseDefect, #dataTableContainerForDefectDetails").hide();
	$('#executionsTitle').text('');
	$('#defectDetailsTitle').text('');
	$('#treeEntityDetails').hide();	
	$("#treeTestScriptContainer").hide();
}

function handleTreeNodeSelection(obj) {
	var nodeId, nodeType,nodeName;
	nodeId = obj.node.id;
	nodeType = obj.node.data;
	nodeName = obj.node.text;
	var parentId=obj.node.parent;
	initializeRightSideHandlers();
	
	if(nodeType == "TestCaseType"){		
		$('#treeTestCaseFeatureMappingContainer').show();
		//listMappedTestCaseOfFeature(parentId);
		var containerID = "treeTestcaseFeatureMappingChild1";
		var parentIdafterSplit;
		
		if(parentId.includes('F') || parentId.includes('S') || parentId.includes('C')){
			
			parentIdafterSplit = parentId.substring(1,parentId.length);
		} else {
			parentIdafterSplit = parentId;
		}
		
		
		var urlToGetFeaturesOfSpecifiedProductId;		
		if(featureTCFlag){
			urlToGetFeaturesOfSpecifiedProductId = 'product.feature.testcase.mappedlist?productFeatureId='+parentIdafterSplit+'&jtStartIndex=0&jtPageSize=10000';		
		}else{
			urlToGetFeaturesOfSpecifiedProductId = 'test.suite.case.list?testSuiteId='+parentIdafterSplit+'&jtStartIndex=0&jtPageSize=10000';		
		}
		listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "240px", "childTable1", containerID, "");
		$("#treeTestCaseFeatureMappingContainer").html('');
		$("#treeTestCaseFeatureMappingContainer").html('<div id="'+containerID+'"></div>');
	}else if(nodeType == "TestCase"){
		$("#treeJTableDivId").show();
		$("#treeJtableTestCaseExecution, #dataTableContainerForExecutions").show();
		$("#treeJtableTestCaseDefect, #dataTableContainerForDefectDetails").show();
		$('#executionsTitle').text('Execution Summary');
		$('#defectDetailsTitle').text('Defects');
		testCaseExecutionDetailsDataTable(nodeId);
		testCaseDesfectDetailsDataTable(nodeId);
	}else if(nodeType == "Feature"){
		$('#treeEntityDetails').show();
		var featureId;
		if(nodeId.includes('F') || nodeId.includes('S') || nodeId.includes('C')) {
			 featureId = nodeId.substring(1,nodeId.length);
		} else {
			featureId=nodeId;
		}
		showFeatureDetails(featureId, nodeName);
	}else if(nodeType == "DecouplingCategory"){
		showDecouplingCategoryDetails(nodeId, nodeName);
	}else if(nodeType == "TestCaseScript"){
		$('#treeEntityDetails').show();
		showTestScriptDetails(nodeId, nodeName);
	}else if(nodeType == "TestCaseScriptType"){
		$("#treeTestScriptContainer").show();
		var testCaseId;
		if(parentId.includes('F') || parentId.includes('S') || parentId.includes('C')) {
			
			testCaseId = parentId.substring(1,parentId.length);
		} else {
			testCaseId=parentId;
		}
		
		var url = 'get.mapped.testscripts.by.testcaseId?testcaseId='+testCaseId+'&jtStartIndex=0&jtPageSize=10000';		
		listSelectedTestScript(url);
	}
}
function listSelectedTestScript(url){
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {					
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			testScriptResults_Container(data);						
		  },
		  error : function(data) {			
			 //$("#addCommentsLoaderIcon").hide();
		 },
		 complete: function(data){			
			//$("#addCommentsLoaderIcon").hide();
		 }
	});	
}

function treeTestScript_Header(){		
	var tr = '<tr>'+			
		'<th>ScriptID</th>'+
		'<th>ScriptName</th>'+
		'<th>ScriptQualifiedName</th>'+
		'<th>Language</th>'+
		'<th>Source</th>'+
		'<th>ScriptVersion</th>'+
		'<th>Revision</th>'+
		'<th>URI</th>'+				
	'</tr>';
	return tr;
}

var testScript_oTable
function testScriptResults_Container(data){	try{
		if ($("#treeTestScriptContainer").children().length>0) {
			$("#treeTestScriptContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = '<table id="treeTestScript_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	$("#treeTestScriptContainer").append(childDivString);
	
	var emptytr = emptyTableRowAppending(8);  // total coulmn count			
	$("#treeTestScript_dataTable thead").html('');
	$("#treeTestScript_dataTable thead").append(treeTestScript_Header());
	  
	$("#treeTestScript_dataTable tfoot tr").html('');     			  
	$("#treeTestScript_dataTable tfoot tr").append(emptytr);
	
	testScript_oTable = $("#treeTestScript_dataTable").dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY": "100%",
	       "bSort": false,
	       "bScrollCollapse": true,
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
     		  $("#treeTestScript_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
    	    	    var i=$(this).index();
    	    	    
    	    	    var flag=false;
    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
    	    	    	if(i == searchcolumnVisibleIndex[j]){
    	    	    		flag=true;
    	    	    		break;
    	    	    	}
    	    	    }
    	    	    
    	    	    if(!flag){
    	    	    	$(this).html('');
    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
    	    	    }
	       	   });			   
			   reInitializeDTProductFeature();			   
		   },  
		   select: true,
		   buttons: [	             		  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "TestScript",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "TestScript",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "TestScript",
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis',									
				], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "scriptId",className: 'disableEditInline', sWidth: '3%' },		
           { mData: "scriptName",className: 'disableEditInline', sWidth: '3%' },
           { mData: "scriptQualifiedName",className: 'disableEditInline', sWidth: '30%' }, 
			{ mData: "language",className: 'disableEditInline', sWidth: '10%' },		
           { mData: "source",className: 'disableEditInline', sWidth: '10%' },
           { mData: "scriptVersion",className: 'disableEditInline', sWidth: '10%' },
		   { mData: "revision",className: 'disableEditInline', sWidth: '10%' },		
           { mData: "uri",className: 'disableEditInline', sWidth: '10%' },          		   
		  
       ], 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 $("#treeTestScript_dataTable_length").css('margin-top','8px');
		 $("#treeTestScript_dataTable_length").css('padding-left','35px');
			
		testScript_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );		
	// ------	
});
}

function showTestScriptDetails(scriptId, nodeName){
	$("#treeJTableDivId").hide();
	$('#treeEntityDetails').show();
	var scriptDetails = "";
	var res = "";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'get.testscript.details?scriptId='+scriptId,
		dataType : 'json',
		success : function(data) {
			res = data.Records;						
			if(res.length > 0) {				
				scriptDetails = createTableinTreeLayout('Details of Testscript', nodeName);
				
				$.each(res, function(i,item){
					scriptDetails += createTableIteminTree('Script Name', item.scriptName);
					scriptDetails += createTableIteminTree('Script Qualified Name', item.scriptQualifiedName);
					scriptDetails += createTableIteminTree('Source', item.source);
					scriptDetails += createTableIteminTree('URI', item.uri);
					scriptDetails += createTableIteminTree('Script Version', item.scriptVersion);
					scriptDetails += createTableIteminTree('Revision', item.revision);
				});				
				scriptDetails += createTableLastIteminTree();
			}
			
			$("#treeEntityDetails").html(scriptDetails);
			$('.table-striped>tbody>tr:nth-of-type(odd)').css("background", "#fff");
			$('.table>tbody>tr>td').css("border-top", "0px solid #fff");
		}       
	});
}

function showFeatureDetails(featureId, nodeName){
	$("#treeJTableDivId").hide();
	$('#treeEntityDetails').show();
	var ftDetails = "";
	var res = "";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'product.feature.details?featureId='+featureId,
		dataType : 'json',
		success : function(data) {
			res = data.Records;						
			if(res.length > 0) {				
				ftDetails = createTableinTreeLayout('Details of Feature', nodeName);
				
				$.each(res, function(i,item){
					ftDetails += createTableIteminTree('Name', item.productFeatureName);
					ftDetails += createTableIteminTree('Display Name', item.displayName);
					ftDetails += createTableIteminTree('Description', item.productFeatureDescription);
					ftDetails += createTableIteminTree('Parent Feature', item.parentFeatureName);					
				});
				
				ftDetails += createTableLastIteminTree();
			}
			
			$("#treeEntityDetails").html(ftDetails);
			$('.table-striped>tbody>tr:nth-of-type(odd)').css("background", "#fff");
			$('.table>tbody>tr>td').css("border-top", "0px solid #fff");
		}       
	});
}

function showDecouplingCategoryDetails(dcId,nodeName){
	$("#treeJTableDivId").hide();
	$('#treeEntityDetails').show();
	var dcDetails = "";
	var res = "";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'product.decoupling.category.details?dcId='+dcId,
		dataType : 'json',
		success : function(data) {
			res = data.Records;
			if(res.length > 0) {				
				dcDetails = createTableinTreeLayout('Details of Decoupling Category', nodeName);
				
				$.each(res, function(i,item){
					dcDetails += createTableIteminTree('Name', item.decouplingCategoryName);
					dcDetails += createTableIteminTree('Display Name', item.displayName);
					dcDetails += createTableIteminTree('Description', item.description);
					dcDetails += createTableIteminTree('Parent Category', item.parentCategoryName);
					dcDetails += createTableIteminTree('User Type', item.userTypeLabel);
				});
				
				dcDetails += createTableLastIteminTree();				
			}
			
			$("#treeEntityDetails").html(dcDetails);
			$('.table-striped>tbody>tr:nth-of-type(odd)').css("background", "#fff");
			$('.table>tbody>tr>td').css("border-top", "0px solid #fff");
		}       
	});
}

function createTableinTreeLayout(textType, nodeName){
	var tableData = '<div class="table-scrollable"><table class="table table-striped table-hover customTableWidth"><thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px;width: 125px;">'+textType+': '+nodeName+'</th><th></th></tr></thead><tbody>';
		
	return tableData;
}

function createTableIteminTree(td1, td2){
	var tableData = '<tr> <td>' + td1 + '</td><td> '+': '+ td2 + '</td></tr>';	
	return tableData;
}

function createTableLastIteminTree(){
	var tableData = '</tbody></table></div>';	
	return tableData;
}

//BEGIN: ConvertDataTable - Executions
var executionsDT_oTable='';
function testCaseExecutionDetailsDataTable(tcId){
	var testCaseId; 
	if(tcId.includes('F') || tcId.includes('S') || tcId.includes('C')) {
		testCaseId = tcId.substring(1,tcId.length);
	} else {
		testCaseId = tcId;
	}
	 url='result.testcase.execution.summary?tcId='+testCaseId+'&workPackageId=-1&productId=&dataLevel=productLevel&jtStartIndex=0&jtPageSize=1000';
	 jsonObj={"Title":"Executions",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Executions",
	};
	executionsDataTableContainer.init(jsonObj);
}

var executionsDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignExecutionsDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignExecutionsDataTableValues(jsonObj){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			executionsDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function executionsDataTableHeader(){
	var childDivString ='<table id="executions_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Product Version</th>'+
			'<th>Product Build Name</th>'+
			'<th>Pass</th>'+
			'<th>Pass %</th>'+
			'<th>Fail</th>'+
			'<th>Fail %</th>'+
			'<th>No Run</th>'+
			'<th>No Run %</th>'+
			'<th>Blocked</th>'+
			'<th>Blocked %</th>'+
			'<th>Executed</th>'+
			'<th>Executed %</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function executionsDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForExecutions").children().length>0) {
			$("#dataTableContainerForExecutions").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = executionsDataTableHeader(); 			 
	$("#dataTableContainerForExecutions").append(childDivString);
	
	executionsDT_oTable = $("#executions_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#executions_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
	    	    	    var i=$(this).index();
	    	    	    var flag=false;
	    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
	    	    	    	if(i == searchcolumnVisibleIndex[j]){
	    	    	    		flag=true;
	    	    	    		break;
	    	    	    	}
	    	    	    }
	    	    	    if(!flag){
	    	    	    	$(this).html('');
	    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	    	    	    }
		       	   });	     		
		     	  reInitializeExecutionsDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Execution',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Execution',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
            { mData: "productVersionName", className: 'disableEditInline', sWidth: '15%' },	
            { mData: "productBuildName", className: 'disableEditInline', sWidth: '15%' },		
            { mData: "totalPass",  className: 'disableEditInline', sWidth: '7%' },
            { mData: null, className: 'disableEditInline', sWidth: '7%',
 	       		mRender: function (data, type, full) {
 		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
 	            	var data = Math.round((full.totalPass / totVal) * 100);
 		            return data;
 	            },
            },
            { mData: "totalFail",  className: 'disableEditInline', sWidth: '7%' },
            { mData: null, className: 'disableEditInline', sWidth: '7%',
 	       		mRender: function (data, type, full) {
 		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
 	            	var data = Math.round((full.totalFail / totVal) * 100);
 		            return data;
 	            },
            },
            { mData: "totalNoRun",  className: 'disableEditInline', sWidth: '7%' },
            { mData: null, className: 'disableEditInline', sWidth: '7%',
 	       		mRender: function (data, type, full) {
 		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
 	            	var data = Math.round((full.totalNoRun / totVal) * 100);
 		            return data;
 	            },
            },
            { mData: "totalBlock",  className: 'disableEditInline', sWidth: '7%' },
            { mData: null, className: 'disableEditInline', sWidth: '7%',
 	       		mRender: function (data, type, full) {
 		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
 	            	var data = Math.round((full.totalBlock / totVal) * 100);
 		            return data;
 	            },
            },
            { mData: "notExecuted",  className: 'disableEditInline', sWidth: '7%' },           
            { mData: null, className: 'disableEditInline', sWidth: '7%',
 	       		mRender: function (data, type, full) {
 		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
 	            	var data = Math.round((full.notExecuted / totVal) * 100);
 		            return data;
 	            },
            },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorExecutions-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#executions_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorExecutions.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#executions_dataTable_length").css('margin-top','8px');
	$("#executions_dataTable_length").css('padding-left','35px');		
	
	executionsDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );
}

var clearTimeoutExecutionsDT='';
function reInitializeExecutionsDT(){
	clearTimeoutExecutionsDT = setTimeout(function(){				
		executionsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutExecutionsDT);
	},200);
}
//END: ConvertDataTable - Executions

//BEGIN: ConvertDataTable - DefectsDetails
var defectDetailsDT_oTable='';
function testCaseDesfectDetailsDataTable(testCaseId){
	var tcId;
	if(testCaseId.includes('F') || testCaseId.includes('S') || testCaseId.includes('C')) {
		tcId = testCaseId.substring(1,testCaseId.length);
	} else {
		tcId = testCaseId;
	}
	 url= 'show.testcase.defect.count?tcId='+tcId+'&jtStartIndex=0&jtPageSize=10';
	 jsonObj={"Title":"Defect Details",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Defect Details",
	};
	defectDetailsDataTableContainer.init(jsonObj);
}

var defectDetailsDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignDefectDetailsDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignDefectDetailsDataTableValues(jsonObj){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			defectDetailsDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function defectDetailsDataTableHeader(){
	var childDivString ='<table id="defectDetails_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Product Version</th>'+
			'<th>Open</th>'+
			'<th>Reviewed</th>'+
			'<th>Approved</th>'+
			'<th>Closed</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function defectDetailsDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForDefectDetails").children().length>0) {
			$("#dataTableContainerForDefectDetails").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = defectDetailsDataTableHeader(); 			 
	$("#dataTableContainerForDefectDetails").append(childDivString);
	
	defectDetailsDT_oTable = $("#defectDetails_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#defectDetails_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
	    	    	    var i=$(this).index();
	    	    	    var flag=false;
	    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
	    	    	    	if(i == searchcolumnVisibleIndex[j]){
	    	    	    		flag=true;
	    	    	    		break;
	    	    	    	}
	    	    	    }
	    	    	    if(!flag){
	    	    	    	$(this).html('');
	    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	    	    	    }
		       	   });	     		
		     	  reInitializeDefectDetailsDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "productVersionName", className: 'disableEditInline', sWidth: '28%' },	
           { mData: "openDefects", className: 'disableEditInline', sWidth: '18%' },	
           { mData: "reviewedDefects", className: 'disableEditInline', sWidth: '18%' },
           { mData: "approvedDefects", className: 'disableEditInline', sWidth: '18%' },
           { mData: "closedDefects", className: 'disableEditInline', sWidth: '18%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorDefectDetails-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#defectDetails_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectDetails.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#defectDetails_dataTable_length").css('margin-top','8px');
	$("#defectDetails_dataTable_length").css('padding-left','35px');		
	
	defectDetailsDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );
}

var clearTimeoutDefectDetailsDT='';
function reInitializeDefectDetailsDT(){
	clearTimeoutDefectDetailsDT = setTimeout(function(){				
		defectDetailsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectDetailsDT);
	},200);
}
//END: ConvertDataTable - DefectsDetails

function listMappedTestCaseOfFeature(featureId){
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'show.mapped.testcase.of.feature?featureId='+featureId+'&jtStartIndex=0&jtPageSize=1000',
		dataType : 'json',
		success : function(data) {
			res = data.Records;	
		}       
	});
}
