var workPackageType ="";
var key='';
var nodeType="";
var prodId ='-1';
var productId = -1;
var prodName = "";
var productBuildId='-1';
var buildIdTemp = '-1';
var buildName = "";
var productVersionId = '-1';
var productVersionName ="";
var title ='';
var editableFlag=true;
var defaultNodeId = "";
var isFirstLoad = true;
var selectedShiftName = '';
var weekDetails = "";
var demandRecursive = 0;
var selectedDemandDetails = {};
var selectedWorkWeekForDemand = 0;
var selectedShiftIdForDemand = 0;
var wpDemandProjectionIdSource = 0;
var demandResource = 0;
var demandResourceWeeks = 0;
var testCaseExecution="";
var pageType="PRODUCTMANAGEMENTPLANVIEW";
var executeEditTrplan = "";
trpExecMode = "Attended";
function showCorrespondingDemandTable(obj) {
	var toShow = obj;
	$('#form_wizard_1').hide();		
	if(toShow == 1) {
		$("#jTableWorkPackageWeeklyPlanContainer").hide();
		$("#jTableContainerWorkPackageDemand").hide();
		$("#jTableContainerWorkPackageWeeklyDemand").show();
		$("#resourceReservationAndDemandContainer").show();		
		
	}else{
		$("#jTableWorkPackageWeeklyPlanContainer").show();
		$("#jTableContainerWorkPackageDemand").show();
		$("#jTableContainerWorkPackageWeeklyDemand").hide();
		$("#resourceReservationAndDemandContainer").hide();
		
	}
}

function showCorrespondingReserveTable(obj) {
	var toShow = obj;
	$('#form_wizard_1').hide();		
	if(toShow == 1) {
		$("#jTableContainerDemand").hide();
		$("#resourceDemandDatePickerDiv").hide();
		
		$("#jTableContainerWorkPackageWeeklyResourceReservation").show();
		
	}else  {
		$("#jTableContainerDemand").show();			
		$("#resourceDemandDatePickerDiv").show();
		$("#jTableContainerWorkPackageWeeklyResourceReservation").hide();
	}
}

function setCreatedWorkPackageNode() {
	$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
		$.each($('#treeContainerDiv li'), function(ind, ele){
			var nodeDetail = $.jstree.reference('#treeContainerDiv').get_node($(ele).attr("id"));
   			var arr = nodeDetail.data.split("~");
   			var prodKey = arr[0];
   			var defaultNodeId='';
   			if(prodKey == $("#productKey").val()) {   				
   				defaultNodeId = $(ele).attr("id");				
				$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
	  	 		$.jstree.reference('#treeContainerDiv').trigger("select_node");
   				
	   			/*$.each(nodeDetail.children, function(childInd, childElem){
	   				var childNodeDetail = $.jstree.reference('#treeContainerDiv').get_node(childElem);
		   			arr = childNodeDetail.data.split("~");
		   			prodKey = arr[0];
		   			if(prodKey == $("#productBuildKey").val()) {
						defaultNodeId = childElem;//$(childElem).attr("id");
						isFirstLoad = false;
		   				return false;
		   			}
	   			});*/  			
	   			return false;
   			}
		});
		//setDefaultnode();
	});
}

function recommendedDisplay(){
	$("#recommendedID").modal();	  
	$("#recommendedID").find('h4').text("RECOMMEDED RESOURCES");
	  
	  try{
		if ($('#recommendedID table').length>0) {
			$('#recommendedID table').remove(); 
	  	}
	  } catch(e) {}
	  /*
	  var url = "metrics.master.varience.resource.performance.popup";
	  
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: url,
	        dataType: 'json',
	        success: function(data) {        	
	    		console.log("sucess")
	        },
	       complete: function(data) {
			   var records = data.responseJSON.Records;			   
	    		
				var table = '<table class="table table-hover table-light">'
					+'<thead><tr class="uppercase"><th>RESOURCE</th><th>VARIANCE</th><th>PLANNED</th><th>ACTUAL</th><th>% COMPLETE</th><th>REMARKS</th></tr></thead>'
					+'<tbody>'
				for(var i=0;i<data.length;i++){
					table +='<tr><td>'+data[i].productName+'</td><td>'+data[i].variancePercentage+'</td><td>'+data[i].totalPlannedTestcaseCount+'</td><td>'+data[i].testCaseCompletedCount+'</td><td>'+data[i].completedTestCasePercentage+'</td></tr>'
				}
							
				table+= '</tbody></table>'			
				$("#recommendedStatusID").append(table);		 		 		
		   }
	   });
	   */
		var table = '<table class="table table-hover table-light">'
			+'<thead><tr class="uppercase"><th>RESOURCE</th><th>VARIANCE</th><th>PLANNED</th><th>ACTUAL</th><th>% COMPLETE</th></tr></thead>'
			+'<tbody>'
			+'<tr><td>Tester 1</td><td>50</td><td>100</td><td>50</td><td>50</td></tr>'												
			+'<tr><td>Tester 2</td><td>45</td><td>100</td><td>50</td><td>55</td></tr>'												
			+'</tbody></table>';			
		$("#recommendedStatusID").append(table);		 		 		
		   
		var url2 = "http://localhost:8080/iLCM/dashboard/index.html#/visualize/edit/Resource-PlanvsActual?_g=(time:(from:now-5y,mode:quick,to:now))&_a=(filters:!(),linked:!f,query:(query_string:(analyze_wildcard:!t,query:'*')),vis:(aggs:!((id:'1',params:(),schema:metric,type:count),(id:'2',params:(field:ExecutionStage,order:desc,orderBy:'1',size:5),schema:group,type:terms),(id:'3',params:(field:Tester,order:desc,orderBy:'1',row:!f,size:5),schema:split,type:terms)),listeners:(),params:(addLegend:!t,addTooltip:!t,defaultYExtents:!f,mode:stacked,shareYAxis:!t),type:histogram))";
		
		var iFrm = '<iframe src="'+url2+'"&output=embed width="100% height:100%" style="height:300px;"></iframe>'
		$('#recommendedLoadingChart').html(iFrm);	
}

	 function getResourcePerformanceData(fileLocation) {
		   $.ajax({
		        type: "POST",
		        contentType: "application/json; charset=utf-8",
		        url: fileLocation,
		        dataType: 'json',
		        success: function(data) {        	
		    		console.log("sucess");
		        },
		       complete: function(data) {
		    	  /*  if(urlFunction == homeMetrics){
		    	   	populateTilesView(data.responseJSON.Records);
					
		    	   }
				   else if(urlFunction == executionMetrics){
		    		populateExecutionMetrics(data.responseJSON.Records);
					
		    	   }else if(urlFunction == productQuality){
					populateQualityProgress(data.responseJSON.Records);   
				   }
				   else if(urlFunction == defectQuality){
					populateDefectProgress(data.responseJSON.Records);   
				   }
				   else if(urlFunction == productConfidence){
					populateProductConfidence(data.responseJSON.Records);   
				   } */
				   
		       }
		   });	   
	   } 

function showCorrespondingTable(obj) {
	//var toShow = $(obj).attr("data-name");
	setTimeout(function(){
		var toShow = $("#giveId").find("label.active").attr("data-name");	
		if(toShow == "Feature") {
			$(".jtbFeature").show();
			$(".jtbCase, .jtbSuite").hide();
			defineFeatures(nodeType);
		}else if(toShow == "TestCase") {
			$(".jtbCase").show();
			$(".jtbFeature, .jtbSuite").hide();
			defineTestcases(nodeType);
		}else {
			$(".jtbSuite").show();
			$(".jtbCase, .jtbFeature").hide();
			defineTestSuite(nodeType);
		}		
	}, 250);
}


function showCorrespondingTableAllocate(obj) {
	//var toShow = $(obj).attr("data-name");
	var toShow = obj;
	$(".jtbAllocateFeature, .jtbAllocateTestCase, .jtbAllocateTestSuite").hide()
	if(toShow == 1) {
		$(".jtbAllocateFeature").show();
		$(".jtbAllocateTestCase, .jtbAllocateTestSuite").hide();
		$("#filterfeature").show();
		$("#filtertestsuite, #filtertestcase").hide();
		
		var status = $("#testBedfeaturestatus_ul").find('option:selected').val();
		//listWorkPackageFeatureOfSelectedWorkPackage(status);//END - Delete - JTable
		allocateTestcaseFeaturesDataTable();
	}else if(toShow == 3) {
		$(".jtbAllocateTestCase").show();
		$(".jtAllocatebFeature, .jtbAllocateTestSuite").hide();
		$("#filtertestcase").show();
		$("#filtertestsuite, #filterfeature").hide();
		
		var status = $("#testBedtestcasestatus_ul").find('option:selected').val();
		//listWorkPackageTestcasesOfSelectedWorkPackage(status);//END - Delete - JTable
		allocateTestcaseTestcasesDataTable();
	}else { 
		$(".jtbAllocateTestSuite").show();
		$(".jtbAllocateTestCase, .jtbAllocateFeature").hide();
		$("#filtertestsuite").show();
		$("#filtertestcase, #filterfeature").hide();
		
		var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();				
		//listWorkPackageTestSuiteOfSelectedWorkPackage(status);//END - Delete - JTable
		allocateTestcaseTestsuitesDataTable();
	}
}

function setDefaultnode() {	
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");														
					
					$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
		  	 		$.jstree.reference('#treeContainerDiv').trigger("select_node");
					isFirstLoad = false;
					return false;
				}
			});	
			//setDefaultnode();
		});
	} else {		
		if($.jstree.reference('#treeContainerDiv').is_parent(defaultNodeId)) {			
			defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];			
			//console.log(defaultNodeId)
			
			if($.jstree.reference("#treeContainerDiv").get_node(defaultNodeId).text == "Work Packages"){
				if(($("#productKey").val() == null) || ($("#productKey").val() == "null") || ($("#productKey").val() == "")) {
					defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
				}
				else{
					var len = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children.length;
					defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[len - 1];
					$("#productKey").val("");
				}
				
				$.jstree.reference('#treeContainerDiv').deselect_all();
				$.jstree.reference('#treeContainerDiv').close_all();
				$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
				$.jstree.reference('#treeContainerDiv').trigger("select_node");				
				$("#clearDataMessage").hide();
				$(".wrkPckgTabCntnt").show();
			}else{
				setDefaultnode();
			}
		}else {
			$("#clearDataMessage").show();
			$(".wrkPckgTabCntnt").hide();		
		}
	}	
}

function showCorrespondingWorkpackageDataTable(value){
	// value -1 : show tabular view , else grid view
	if(value == 1){
		$("#showWorkpackageContainer").hide();
		$("#workPackageTileViewContainer").show();
		$(".btn-circle").hide();	
		
	}else{
		$("#showWorkpackageContainer").show();
		$("#workPackageTileViewContainer").hide();
		$(".btn-circle").show();

		//var url='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId+'&testFactoryId='+document.getElementById("treeHdnCurrentTestFactoryId").value+'&jtStartIndex=0&jtPageSize='+pageCountSelectedDT;
		//assignWPDataTableValues(url, "parentTable", "", "");
		
		allTestRunPlanFunDT(0, pageCountSelectedDT);
	}
}

//To select first workpackage in a currently selected product
function setWorkPackageNode() {
	
	if(nodeType == "") return false;
	if(nodeType == "ProductBuild") {
		defaultNodeId = $("#hdnCurrentProductBuildNodeId").val();
		$('.wrkPckgTabCntnt').hide();
		workPackageExecutionDetailsBuildLevel("build");

	}else if(nodeType == "Product") {
		defaultNodeId = $("#hdnCurrentProductNodeId").val();
		$('.wrkPckgTabCntnt').hide();
		workPackageExecutionDetailsBuildLevel("prod");
		
		if($.jstree.reference('#treeContainerDiv').is_parent(defaultNodeId)) {
			/*defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
			$.jstree.reference('#treeContainerDiv').deselect_all();
			$.jstree.reference('#treeContainerDiv').close_all();
			$.jstree.reference('#treeContainerDiv').open_node(defaultNodeId);
			$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId)
			$.jstree.reference('#treeContainerDiv').open_node(defaultNodeId);
			$.jstree.reference('#treeContainerDiv').trigger("select_node");*/		
		}else{
			$("#clearDataMessage").show();
			$('.wrkPckgTabCntnt').hide();
		}		
   }else if(nodeType == "TestFactory") {	
		if(typeof(atlasIframeFlag)=="undefined"){
			$('.wrkPckgTabCntnt').hide();
		}
		workPackageExecutionDetailsBuildLevel("testFactory");
		
	}else{
		$('.wrkPckgTabCntnt').hide();
	}
}

function exportResultPopUp() {
	var productId =document.getElementById("treeHdnCurrentProductId").value;
	
	var urlMapping = 'administration.test.management.system.list?productId='+productId;
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : urlMapping,
        dataType : 'json',
        success : function(data) {
             if(data.Result=="OK"){
            	 var listOfData=data.Records;
            	 if(listOfData.length==0){
					 callAlert("Test Management system is not available");
					 return false;
				 }
            	 $('#expRes_types').empty();
            	 $.each(listOfData, function(i,item){	
 					var tmsid = item.testManagementSystemId;					
 					var tmsName=item.title;
 					$('#expRes_types').append('<label><input type="checkbox" name="'+tmsName+'"  class="icheck" id="' + tmsid + '" data-radio="iradio_flat-grey">'+tmsName+'</label>');
 					
 				});
            	 $("#div_PopupExportRes1").modal();
            	
            
             }
        }
 }); 
	
	/* $('#exp_types').empty();
	$.post('',function(data) {	
		var ary = (data.Options);
        $.each(ary, function(index, element) {
        	if(index == 0){
    			$('#exp_types').append('<label><input type="radio" name="radio1" checked class="icheck" id="' + element.Value + '" data-radio="iradio_flat-grey">'+element.DisplayText+'</label>');
        	}else{
        		$('#exp_types').append('<label><input type="radio" name="radio1" class="icheck" id="' + element.Value + '" data-radio="iradio_flat-grey">'+element.DisplayText+'</label>');
        	}
		});
	});
	$("#div_PopupExportDef1").modal(); */
}

function exportDefectsPopUp() {
	var productId =document.getElementById("treeHdnCurrentProductId").value;
	
	var urlDefect = 'administration.defect.management.system.list?productId='+productId;
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : urlDefect,
        dataType : 'json',
        success : function(data) {
             if(data.Result=="OK"){
            	 var listOfData=data.Records
            	 $('#exp_types').empty();
            	 console.log(data);
            	 $.each(listOfData, function(i,item){	
 					var tmsid = item.defectManagementSystemId;					
 					var tmsName=item.title;
 					$('#exp_types').append('<label><input type="checkbox" name="'+tmsName+'"  class="icheck" id="' + tmsid + '" data-radio="iradio_flat-grey">'+tmsName+'</label>');
 					
 				});
            	 $("#div_PopupExportDef1").modal(); 
            	
             }
        }
 }); 
	
}

var testExecutionResultBugIdForExport="";
function exportOneDefectsPopUp(testExecutionResultBugId) {
	testExecutionResultBugIdForExport=testExecutionResultBugId;
	var productId =document.getElementById("treeHdnCurrentProductId").value;
	
	var urlDefect = 'administration.defect.management.system.list?productId='+productId;
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : urlDefect,
        dataType : 'json',
        success : function(data) {
             if(data.Result=="OK"){
            	 var listOfData=data.Records;
				 if(listOfData.length==0){
					 callAlert("Defect Management system is not available");
					 return false;
				 }
            	 $('#exp_one_def_types').empty();
            	 console.log(data);
            	 $.each(listOfData, function(i,item){	
 					var tmsid = item.defectManagementSystemId;					
 					var tmsName=item.title;
 					$('#exp_one_def_types').append('<label><input type="checkbox" name="'+tmsName+'"  class="icheck" id="' + tmsid + '" data-radio="iradio_flat-grey">'+tmsName+'</label>');
 					
 				});
            	 $("#div_PopupExportOneDef").modal(); 
            	
             }
        }
 }); 
	
}

var testRunJobIDForWorkPackageResult="";
function exportResult_TestrunJobPopUp(testRunJobId) {
	testRunJobIDForWorkPackageResult = testRunJobId;
	
var productId =document.getElementById("treeHdnCurrentProductId").value;
	
	var urlMapping = 'administration.test.management.system.list?productId='+productId;
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : urlMapping,
        dataType : 'json',
        success : function(data) {
             if(data.Result=="OK"){
            	 var listOfData=data.Records;
            	 if(listOfData.length==0){
					 callAlert("Test Management system is not available");
					 return false;
				 }
            	 $('#expRes_testrunjob_types').empty();
            	 $.each(listOfData, function(i,item){	
 					var tmsid = item.testManagementSystemId;					
 					var tmsName=item.title;
 					$('#expRes_testrunjob_types').append('<label><input type="checkbox" name="'+tmsName+'"  class="icheck" id="' + tmsid + '" data-radio="iradio_flat-grey">'+tmsName+'</label>');
 					
 				});
            	 $("#div_PopupExportResTestrunJob").modal();
            
             }
        }
 });	
	
}


function showWorkPackageTreeData(){	
	var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.workpackage.plan.tree?type=1',
	    	 	"urlToGetChildData": 'administration.workpackage.plan.tree?type=1',	    	 	
	 };	 
	 TreeLazyLoading.init(jsonObj);	 
}

$(document).ready(function(){	
		if(pageType=="HOMEPAGE" || pageType=="PRODUCTMANAGEMENTPLANVIEW" )return false;
	   QuickSidebar.init(); // init quick sidebar
	   setBreadCrumb("Plan");
	   ComponentsPickers.init();
	   createHiddenFieldsForUserDetails();
	   createHiddenFieldsForTree();
	   setPageTitle("Work Packages");
		//getWorkPackageTreeData();
	   showWorkPackageTreeData();
	   
		//getTreeData('administration.workpackage.plan.tree?type=1');
		
		if(($("#productKey").val() == null) || ($("#productKey").val() == "") || (document.documentURI.indexOf("workpackageId") == -1)) {
			isFirstLoad = true;
			$("#productKey").val("");
  	 		setDefaultnode();  	 		
		}
		else{			
			setCreatedWorkPackageNode();
		}
		
	 $("#treeContainerDiv").on("select_node.jstree",
				  function(evt, data){
		 		   if(data.node != undefined){
			 		if(data.node.icon=='fa fa-close' || data.node.icon=='fa fa-lock'){
			 			editableFlag=false;
			 		}else{
			 			editableFlag=true;
			 		}
			 		wpkageIdArr = [];
			 		workPackageChecked = false;
			 		var entityIdAndType =  data.node.data;			 		
		   			var arry = entityIdAndType.split("~");
			 		key = arry[0];
			 		var type = arry[1];
			 		
		   			title = data.node.text;
		   		 	var loMainSelected = data;
			        uiGetParents(loMainSelected);
					var date = new Date();
				    var timestamp = date.getTime();
				    nodeType = type;
				    document.getElementById("filter").style.display = "block";
				    //document.getElementById("accordionWD").style.display = "none";
				    operationYear = new Date().getFullYear();
					$("a[href^=#Block]").parent("li").hide();
					$("a[href^=#Demand]").parent("li").hide();
					if(	(($("a[href^=#Block]").parent("li").css("display") !== "none") && $("a[href^=#Block]").parent("li").hasClass('active')) ||
						(($("a[href^=#Demand]").parent("li").css("display") !== "none") && $("a[href^=#Demand]").parent("li").hasClass('active')) ){
						$("#tablistWP>li:first-child>a").trigger('click');
					}
					
					$('#toAnimate .portlet .actions').eq(0).css('display','none');
					
					$(".workPackage_radGrp").find("label:last").addClass("active").siblings("label").removeClass("active");
					
					if(nodeType == "Product"){
			   	 		//$('#showWorkpackageContainer').show();
						showCorrespondingWorkpackageDataTable(1);
						
			   	 		$('#workPacakgeRadioGroup').show();
			   	 		$('#toAnimate .portlet .actions').eq(0).css('display','block');
			   	 	
				    	prodId = key;
				    	productId = key;
				    	setWorkPackageNode();
				    	return false;
				    
			   	 	}else if(nodeType == "ProductBuild" || title == "Work Packages") {
			   	 		//$('#showWorkpackageContainer').show();
			   	 		showCorrespondingWorkpackageDataTable(1);
			   	 		
			   	 		$('#workPacakgeRadioGroup').show();
			   	 		$('#toAnimate .portlet .actions').eq(0).css('display','block');
			   	 		
			   	 		if(title == "Work Packages"){
			   	 			nodeType = "ProductBuild";  // to show same information when clicking on the product build / Work package folder.
			   	 		}
			   	 		
				    	productBuildId = key;
				    	setWorkPackageNode();
				    	return false;
				    
			   	 	}else if(nodeType=='WorkPackage')
			   	 	{
				    	workPackageId = key;				    	
				    	document.getElementById("treeHdnCurrentWorkPackageId").value = workPackageId;
				    	document.getElementById("cworkPackageName").value = title;
				    	prodId =document.getElementById("treeHdnCurrentProductId").value;
				    	
				    	$('#showWorkpackageContainer').hide();
				    	$("#workPackageTileViewContainer").hide();
				    	$('#workPacakgeRadioGroup').hide();
						$("#clearDataMessage").hide();
						$(".wrkPckgTabCntnt").show();
						fetchWPProductType(-1, prodId, workPackageId);			   	 	
						getWeeksName();						
						
						//Metronic.handleTabs();
						
			   	 	}else if (nodeType=='Environment'){
				    	environmentId = key;
				    
			   	 	}else if (nodeType=='TestFactory'){		
			   	 		//$('#showWorkpackageContainer').show();
			   	 		showCorrespondingWorkpackageDataTable(1);
			   	 		
			   	 		$('#workPacakgeRadioGroup').show();
			   	 		$('#toAnimate .portlet .actions').eq(0).css('display','block');   	 		   	 		
			    	
			   	 		document.getElementById("treeHdnCurrentProductId").value = -1;
			   	 		prodId = document.getElementById("treeHdnCurrentProductId").value;
			   	 		productBuildId = -1;
			   	 		workPackageId = -1;
			   	 	    document.getElementById("treeHdnCurrentTestFactoryId").value = key;
			   	 	    
			   	 		setWorkPackageNode();		   	 	    
				    
			   	 	}else{
				    	prodId = document.getElementById("treeHdnCurrentProductId").value;
				    	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
				    }
				  
				    showReserveResourcesTab(prodId, nodeType);
				    
				    var selectedTab= $("#tablistWP>li.active").index();				    
				    utilizationWeekRange = '';									
				    tabSelection(selectedTab,workPackageId);
	 		};
		 });
		 
		 $(document).on('click', '#div_ShowWorkpackageSummary #tablistWP>li', function(){	
			if($("#clearDataMessage").css("display") == "block") return;
			var workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
				
			var selectedTab = $(this).index();
		 	if(workPackageId==null || workPackageId <=0 || workPackageId == 'null' || workPackageId==''){
		 		hideAllTabs();	
		 		callAlert("Please select the workPackage");
				return false;
			}				
		 	tabSelection(selectedTab,workPackageId);			 				  
		}); 
		 
				 
		$(document).on('change','#testerList_ul', function() {
			var testerListId =  $("#testerList_ul").find('option:selected').attr('id');
			testerListId = (typeof testerListId == 'undefined') ? -1 : testerListId;
		 });
		 
		 $(document).on('change','#testLeadList_ul', function() {
			 var testLeadListId =  $("#testLeadList_ul").find('option:selected').attr('id');
			 testLeadListId = (typeof testLeadListId == 'undefined') ? -1 : testLeadListId;
		 });
		 
		 $(document).on('change','#executionPriorityList_ul', function() {
			 var executionPriorityListId =  $("#executionPriorityList_ul").find('option:selected').attr('id');
			 executionPriorityListId = (typeof executionPriorityListId == 'undefined') ? -1 : executionPriorityListId;
		 });

		 $(document).on('change','#sort_ul_hm', function() {
		 	viewmode="heatmap";	
		 	workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	        sorthmIds =  $("#sort_ul_hm").find('option:selected').val();
		 	sorthmIds = (typeof sorthmIds == 'undefined') ? 1 : sorthmIds;
		 	url='workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+sorthmIds+"&testcaseId=0&jtStartIndex=0&jtPageSize=0";
		 	gridView(url);
		  });
		 
			document.getElementById("accordionWD").style.display = "none";
		
			$("#datepickerRP").datepicker('setDate','today');
				$("#datepickerRP").on("changeDate",function() {
				$(".datepicker").hide();
				urlToGetResourceDemandOfWorkPackage = 'workPackage.resource.demand.listbywp?workPackageId='+workPackageId+'&resourceDemandForDate='+datepickerRP.value;
				listResourceDemandOfWorkPackage();
			});
				 
			//acknowledgement message
		    var message_status = $("#status");
		    $("td[contenteditable=true]").blur(function(){
		        var modifiedField = $(this).attr("id") ;
		        var value = $(this).text() ;
		        var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
		        
		        var url='administration.workpackage.update?workpackageId='+workpackageId+"&modifiedfField="+modifiedField+"&modifiedValue="+value;
		        var thediv = document.getElementById('reportbox');
		        if (thediv.style.display == "none") {
			 		$.blockUI({
			 		 	theme : true,
			 		 	title : 'Please Wait',
			 		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
			 		 });
		        $.post(url , function(data){
		        
		            if(data != '')
					{
						message_status.show();
						message_status.text(data);
						//hide the message
						setTimeout(function(){message_status.hide()},3000);
					}
					 $.unblockUI();

		        });
		        } else {
					thediv.style.display = "none";
					thediv.innerHTML = '';
				}
		      
		    });
		    
			 document.getElementById("ResultsGridView").style.display = "none";			 
	
			 $("[id$='fromTodatepicker']").daterangepicker();			
			 
			 $(document).on('change','#testBedfeaturestatus_ul', function() {
					var status = $("#testBedfeaturestatus_ul").find('option:selected').val();				
	 		    	//listWorkPackageFeatureOfSelectedWorkPackage(status);//END - Delete - JTable
					allocateTestcaseFeaturesDataTable();
				 });
			 $(document).on('change','#testBedtestsuitestatus_ul', function() {
					var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();				
					//listWorkPackageTestSuiteOfSelectedWorkPackage(status);//END - Delete - JTable
					allocateTestcaseTestsuitesDataTable();
				 });
			 $(document).on('change','#testBedtestcasestatus_ul', function() {
					var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				
					urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId=0&testerId=0&envId=0&localeId=0&plannedExecutionDate=&dcId=0&executionPriority=0&status="+status;
				 	  //listWorkPackageTestcasesOfSelectedWorkPackage(status);//END - Delete - JTable
					  allocateTestcaseTestcasesDataTable();
				 });
			 
			 $(document).on('blur','#reserveRecursive', function() {
				 if(currentView == "Table"){
					 displayResoucesAvailability(1);
				 }
			 });
			 
			 var demandYears = '';
			 var currentYear = new Date().getFullYear();
			 for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
				 demandYears += '<option value="'+i+'">'+i+'</option>';
			 }
			 $('#demandYears').append(demandYears);
			 $('#demandYears').val(currentYear);
			 $(document).on('change','#demandYears', function() {
				 if(operationYear != $('#demandYears').val()){
					 operationYear = $('#demandYears').val();
					 raiseResourceDemandForWeekly(selectedDemandDetails['workPackageId'], selectedDemandDetails['shiftId'], selectedDemandDetails['workWeek'], selectedDemandDetails['skillId'], selectedDemandDetails['roleId'], selectedDemandDetails['userTypeId']);
				 }
			 });
			 
			 $(document).on('blur','#demandRecursive', function() {
				 var newDemandRecursive = $("#demandRecursive").val();
				 if(typeof newDemandRecursive == 'undefined' || newDemandRecursive == null || newDemandRecursive.trim() == '' || newDemandRecursive <= 0 || demandRecursive == newDemandRecursive){
					 $("#demandRecursive").val(demandRecursive);
				 }else{
					 demandRecursive = newDemandRecursive;
					 raiseResourceDemandForWeekly(selectedDemandDetails['workPackageId'], selectedDemandDetails['shiftId'], selectedDemandDetails['workWeek'], selectedDemandDetails['skillId'], selectedDemandDetails['roleId'], selectedDemandDetails['userTypeId']);
				 }
			 });
			 utilizationWeekRange = '';
			 getWeeksName();
	});

function dateFilterWorkpackages(){
	var startDateVal = $('#workpackageStartDate').val();
	var endDateVal = $('#workpackageEndDate').val();
	var d1=startDateVal.split('/');
	var d2=endDateVal.split('/');
	var startDate = new Date(d1[2], d1[0]-1, d1[1]);  
	var endDate = new Date(d2[2], d2[0]-1, d2[1]);
	//console.log(from,"---",to);
	var firstExecDate;
	var lastExecDate;
	var firstDate;
	var lastDate;
	var dateFilteredData={};
	dateFilteredData["Records"]=[];
	
	for(var i=0;i<workpackageData.Records.length;i++){		
		firstDate = workpackageData.Records[i].firstActualExecutionDate;
		lastDate = workpackageData.Records[i].lastActualExecutionDate;
		if(firstDate && lastDate){
			firstDate = firstDate.split('-');
			lastDate = lastDate.split('-');
			firstExecDate = new Date(firstDate[0], firstDate[1]-1, firstDate[2]);
			lastExecDate = new Date(lastDate[0], lastDate[1]-1, lastDate[2]);
			if ((firstExecDate >= startDate) && (lastExecDate <= endDate)){
				dateFilteredData["Records"].push(workpackageData.Records[i]);
			}
		}
	}
	//console.log(dateFilteredData);
	displayRecordsTestRunPlan(dateFilteredData);
}
var productTypehidden = 0;
function fetchWPProductType(productVersionId, prodId, workPackageId){
var url = 'get.prodcutType.by.versionIdProdIdWorkPackageId?productVersionId='+productVersionId+"&productId=-1&workpackageId="+workPackageId;			
		$.ajax({
			 type: "POST",
			    dataType : 'json',
			    url : url,
			    success: function(data) {
			    	var prodTypeData=eval(data);
					productTypehidden = prodTypeData[0].productType;
					if(productTypehidden!=null){
						document.getElementById("productType").value  = productTypehidden;
						}						  	
			    }
		});
}

	$('#shiftList_ul').on("select2-selecting", function() {
	    var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	    $('#testLeadList_ul').select2('data', null);
	    $('#testerList_ul').select2('data', null);
	    
		 if(workpackageId==''){
			 callAlert("Please select Workpackage");
			 return false;
		 }
			
		 var plannedExecutionDate=datepickerbulk.value;
		 if(plannedExecutionDate=='Planned Execution Date')
			 plannedExecutionDate='';
		 if(plannedExecutionDate=='' || plannedExecutionDate=='Planned Execution Date')
		{
			 callAlert("Please select Planned Execution Date");
				 return false;
		}
		 setTimeout(function(){
		 	var shiftListId =  $("#shiftList_ul").find('option:selected').attr('id');
			shiftListId = (typeof shiftListId == 'undefined') ? -1 : shiftListId;
			
			 if(shiftListId=='' || shiftListId==null || shiftListId=='undefined'){
				 callAlert("Please select the Shift");
				 return false;
			 }
			 
			 loadTesterList(plannedExecutionDate,shiftListId,workpackageId);
			 loadTestLeadList(plannedExecutionDate,shiftListId,workpackageId);
		 }, 250);
		 
		 
	});

	$("#datepickerbulk").datepicker('setDate','today');
	$("#datepickerbulk").on("changeDate",function() {
	$(".datepicker").hide();
	$('#testLeadList_ul').empty();
	$('#testLeadList_ul').select2('data', null);
	
	$('#testerList_ul').empty();
	$('#testerList_ul').select2('data', null);
		//alert("date"+datepicker.value);
		 var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
		 if(workpackageId==''){
			 callAlert("Please select Workpackage");
			 return false;
		 }
		 var plannedExecutionDate=datepickerbulk.value;
		 if(plannedExecutionDate=='Planned Execution Date')
			 plannedExecutionDate='';
		 if(plannedExecutionDate=='' || plannedExecutionDate=='Planned Execution Date')
		 {
			 callAlert("Please select Planned Execution Date");
			 return false;
		 }
		 loadWorkShifts();
	});

$(document).on("click",".daterangepicker .applyBtn", function(e) {
    var startDateVal = $(this).siblings(".daterangepicker_start_input").find("input").val();
	var endDateVal = $(this).siblings(".daterangepicker_end_input").find("input").val();
	//var startDateVal = $('#workpackageStartDate').val();
	//var endDateVal = $('#workpackageEndDate').val();
	$('#planned_fromTodatepicker4').val(startDateVal+" - "+endDateVal);
	var d1=startDateVal.split('/');
	var d2=endDateVal.split('/');
	var startDate = new Date(d1[2], d1[0]-1, d1[1]);  
	var endDate = new Date(d2[2], d2[0]-1, d2[1]);
	//console.log(from,"---",to);
	var firstExecDate;
	var lastExecDate;
	var firstDate;
	var lastDate;
	var dateFilteredData={};
	dateFilteredData["Records"]=[];
	
	for(var i=0;i<workpackageData.Records.length;i++){		
		firstDate = workpackageData.Records[i].firstActualExecutionDate;
		lastDate = workpackageData.Records[i].lastActualExecutionDate;
		if(firstDate && lastDate){
			firstDate = firstDate.split('-');
			lastDate = lastDate.split('-');
			firstExecDate = new Date(firstDate[0], firstDate[1]-1, firstDate[2]);
			lastExecDate = new Date(lastDate[0], lastDate[1]-1, lastDate[2]);
			if ((firstExecDate >= startDate) && (lastExecDate <= endDate)){
				dateFilteredData["Records"].push(workpackageData.Records[i]);
			}
		}
	}
	//console.log(dateFilteredData);
	displayRecordsTestRunPlan(dateFilteredData);
});

$(document).on("click","#workPackageDiv .daterangepicker .applyBtn", function(e) {
    var workdateFrom = $(this).siblings(".daterangepicker_start_input").find("input").val();
	var workdateTo = $(this).siblings(".daterangepicker_end_input").find("input").val();
	$("#hdnStartDate").val(workdateFrom);
	$("#hdnEndDate").val(workdateTo);
	changeValue($("#hdnFieldType").val());
});

$(document).on("click",".date-range-toggle", function(e) {
	if($(this).closest("div").attr("id").indexOf("plan") > -1)
		$("#hdnFieldType").val("plannedStartDate");
	else
		$("#hdnFieldType").val("start");
});
function verifyWorkpackageSelected(nodeType){
	if(nodeType !='WorkPackage'){
		  callAlert("Please select a workPackage");
		  return false;
	  }else{
		  return true;
	  }
}
function hideAllTabs(){
	$('#accordionWD').children().hide();
	$('div#Plan').children().hide();
	$('#PlanTS').children().hide();
	$('#Allocate').children().hide();
	$('#Demand').children().hide();
	$('#Block').children().hide();
	$('#Defects').children().hide();
	$('#Results').children().hide();	
}

var mode="view";
var viewmode="list";

var urlToGetResourcesOfWorkPackage = "";
var urlToGetResourcesOfWorkPackageWeekly = "";
var availablilityType = "Booked";

var weeklyEnable = "";
var workPackageId;
var shiftId = 0;
var userTypeId = 0;
var groupDemandId;
var resourceId = 0;



var reservationDate;
var resourceDemandCount;
var availablilityType;
var currentView;
var globalCounter = 0;
var currentWorkPackageName = "";
var currentShiftName = "";
var urlToGetResourceDemandOfWorkPackage = "";
var urlForWorkPackageDemandprojection = "";
var urlForWorkPackageDemandProjectionWeekly = "";
var urlForWorkPackageWeeklyPlan = "";
var urlToGetWorkPackagesTestCasesOfSpecifiedWorkPackage = "";
var urlToRaiseSkillBasedDemand = "";
var urlToRaiseSkillBasedDemandForWeekly = "";
var urlToListSkillBasedDemand = "";
var urlToUpdateSkillBasedDemand = "";
var urlToDeleteSkillBasedDemand = "";
var urlToGetWorkPackageDetailsOfSelectedWorkPackageId = "";
var jsEnvironmentFieldDisplayTitles = new Array();
var jsEnvironmentFieldsInJson = ["env1","env2","env3","env4","env5","env6","env7","env8","env9","env10","env11","env12","env13","env14","env15","env16","env17","env18","env19","env20"];
var availableEnvironmentsCount = 20;
var jsLocaleFieldDisplayTitles = new Array();
var jsLocaleFieldsInJson = ["loc1","loc2","loc3","loc4","loc5","loc6","loc7","loc8","loc9","loc10"];
var availableLocalesCount = 10;
var date = new Date();
var timestamp = date.getTime();
var testing=0;


function showReserveResourcesTab(prodId,nodeType){
	$.ajax({
	    url: 'product.tab.show.mode?testFactoryId=0&productId='+prodId,
	    method: 'POST',
	    contentType: "application/json; charset=utf-8",
	    dataType : 'json',
		success : function(data) {
			if(data.Result == 'OK'){
				$("a[href^=#Block]").parent("li").show();
				$("a[href^=#Demand]").parent("li").show();
			}else{
				$("a[href^=#Block]").parent("li").hide();
				$("a[href^=#Demand]").parent("li").hide();
				if(($("a[href^=#Block]").parent("li").hasClass('active')) || ($("a[href^=#Demand]").parent("li").hasClass('active'))){
					$("#tablistWP>li:first-child>a").trigger('click');	
				}
			}
		},
	});
}

function allocateTestCase(nodeType){	
	 //Remove unwanted alerts
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
		 $('#Allocate').children().not('.jtbAllocateTestCase, .jtbAllocateTestSuite').show();
		 $("#filterfeature").show();
			$("#filtertestsuite, #filtertestcase").hide();
	
	//showCorrespondingTableAllocate($("#allocateRadios").find("label.active").index() + 1);
		document.getElementById("filter").style.display = "block";		
	
	var thediv = document.getElementById('newreportbox');
	 document.getElementById("ResultsGridView").style.display = "none";
    if (thediv.style.display == "none") {
 		$.blockUI({
 		 	theme : true,
 		 	title : 'Please Wait',
 		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
 		 });
 		if(pageType=="HOMEPAGE"){
 			document.getElementById("treeHdnCurrentEnvironmentId").value="";
 		}
	 	var environmentId=document.getElementById("treeHdnCurrentEnvironmentId").value;
	 
		// document.getElementById("filter").style.display = "block";
		 document.getElementById("envLoc").style.display = "none";
		 document.getElementById("envLocTS").style.display = "none";

 		  loadTesterList();
		  loadTestLeadList();
// 		  loadDCFilterList();
// 		  loadTestLeadFilterList();
// 		  loadTesterFilterList();
// 		  loadEnvFilterList();
// 		  loadLocaleFilterList();
		  loadExecutionPriorityList();
		  loadWorkShifts();
// 		  var dcListFilter = $("#dcListFilter_dd").children();
// 		  var dcFilterId= dcListFilter.attr('id'); 
		  
// 		  var testLeadListFilter = $("#testLeadListFilter_dd").children();
// 		  var testLeadFilterId= testLeadListFilter.attr('id'); 
		  
// 		  var testerListFilter = $("#testerListFilter_dd").children();
// 		  var testerFilterId= testerListFilter.attr('id'); 
// 		  var envListFilter = $("#envListFilter_dd").children();
// 		  var envFilterId= envListFilter.attr('id'); 
// 		  var localeListFilter = $("#localeListFilter_dd").children();
// 		  var localeFilterId= localeListFilter.attr('id'); 
// 	      var plannedExecutionDateFilter=datepickerFilter.value;
	    	  
// 		  if(plannedExecutionDateFilter=='Planned Execution Date')
// 			  plannedExecutionDateFilter='';
// 		  if(dcFilterId ==null || dcFilterId=='undefined' || dcFilterId=='null'){
// 			  dcFilterId=0;
// 			}
		  
// 			if(testLeadFilterId ==null || testLeadFilterId=='undefined' || testLeadFilterId=='null'){
// 				testLeadFilterId=0;
// 			}
// 			if(testerFilterId ==null || testerFilterId=='undefined' || testerFilterId=='null'){
// 				testerFilterId=0;
// 			}
// 			if(envFilterId ==null || envFilterId=='undefined' || envFilterId=='null'){
// 				envFilterId=0;
// 			}
// 			if(localeFilterId ==null || localeFilterId=='undefined' || localeFilterId=='null'){
// 				localeFilterId=0;
// 			}
				var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				

		  if(environmentId==null || environmentId <=0 || environmentId == 'null') {
				urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId=0&testerId=0&envId=0&localeId=0&plannedExecutionDate=&dcId=0&executionPriority=0&status="+status;
		  }
		  else{
				urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.planbyenv?workPackageId='+workPackageId+'&envId='+environmentId+'&timeStamp='+timestamp;
		  }
		  showCorrespondingTableAllocate($("#allocateRadios").find("label.active").index() + 1);
	 	
 	   
    } else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
    closeLoaderIcon();
}

function setDateOnDateRangepicker(pkr_id, stDate, edDate){
	$(pkr_id).data("daterangepicker").setStartDate(stDate);
	$(pkr_id).data("daterangepicker").setEndDate(edDate);
	var strtDate = moment();
	strtDate._d = new Date(stDate);
	var endDate = moment();
	endDate._d = new Date(edDate);
	$(pkr_id).data("daterangepicker").cb(strtDate, endDate)
}
function getWPTypeFromTRP(workPackageId){
	var wpIdType = '';
 $.ajax({
         type: "POST",
   		 contentType: "application/json; charset=utf-8",
         url : 'workpackage.typeof.testrunplan.bywpId?workPackageId='+workPackageId,
         dataType : 'json',
         success : function(data) {
                if(data.Result=="ERROR"){
                     // callAlert(data.Message);
                return false;
		         }else{
		        	 
		        	 if(data.Result=="OK"){
		        		//callAlert("Export result Completed.");
						wpIdType = workPackageId+':'+data.Record;
						console.log('wpId---'+workPackageId+'- TRPType'+data.Record);
						document.getElementById("wptype").value = data.Record;
						document.getElementById("wptypewpId").value = workPackageId; 
						 return wpIdType;
		        	 }else{
		        		 //callAlert(data.Message);
						 return wpIdType;
		        	 }
	         	}               
         }
  });  	
}

function workPackageSummary(urlToGetWorkPackageDetailsOfSelectedWorkPackageId, workPackageId, nodeType) {
	getWPTypeFromTRP(workPackageId); //FetchWPTRPType
	 
	//Remove unwanted alerts
	 /*if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }*/
	$('#accordionWD').children().show();
	
			 document.getElementById("ResultsGridView").style.display = "none";
			document.getElementById("workpackageTCER").style.display = "none";
			openLoaderIcon();
			$.ajax({
				type: "POST",
		        contentType: "application/json; charset=utf-8",
				url : urlToGetWorkPackageDetailsOfSelectedWorkPackageId,
				dataType : 'json',
				cache:false,
				success : function(data) {
					
					var result=data.Records;
					var totalTCCount=0;
					var totalTCExecutedCount=0;
					var totalDefectsCount=0;
					var approvedDefectsCount=0;
					var plannedStartDate='';
					var plannedEndDate='';
					var totalNumberOfCompDays=0;
					var totaljobscount = 0;
					var totaljobscompleted = 0;
					var wpType = 0;
					if(data.Records.length === 0) {
						 $("#accordionWD").css("display","none");
					} else {					
						$("#accordionWD").css("display","block");
						if(data.Records[0].productId != null){
							productId = data.Records[0].productId;
						}
						if(data.Records[0].pBuildId != null){
							productBuildId = data.Records[0].pBuildId;
						}
					} 
					 $("#wpkg_Id").empty();
					 $("#wpkg_name").empty();
					 $("#wpkg_type").empty();
					 $("#wpkg_desc").empty();
					 $("#prodName").empty();
					 $("#sel_tccount").empty();
					 $("#sel_tscount").empty();
					 $("#sel_featurecount").empty();
					 $("#prodBuildName").empty();
					 $("#plannedStartDate").empty();
					 $("#plannedEndDate").empty();
					 $("#tc_executed_count").empty();
					 $("#actualStartDate").empty();
					 $("#actualEndDate").empty();
					 $("#percent_completed").empty();
					 $("#envList").empty();
					 $("#wpkg_owner").empty();
					 $("#total_tc").empty();
					 $("#total_defects").empty();
					 $("#env_count").empty();
					 $("#iteration_No").empty();
					 
					 $.each(result, function(i,item){ 
						
						$("#wpkg_Id").append("<div style='font-size:small;' >"+item.id+"</div>");						
					
					/*  Abort_completed  Start For workpackageSummary . */
						if(editableFlag)
							$("#workPackageDiv #wpkg_name").val(item.workPackageName).removeAttr("readonly");
						else
							$("#workPackageDiv #wpkg_name").val(item.workPackageName).attr("readonly", "true");
						/*  Abort_completed  End For workpackageSummary . */
						
						
						if(item.workPackageType==null){
							$("#wpkg_type").append("<div style='font-size:small;' >NA</div>");
						}
						else{
							$("#wpkg_type").append("<div style='font-size:small;' >"+item.workPackageType+"</div>");
							wpType = item.workPackageType;
						}
						
						
					  /*  Abort_completed  Start For description . */
				      if(editableFlag){
						if(item.description==null)							
							$("#workPackageDiv #wpkg_desc").val("NA").removeAttr("readonly");
						else							
							$("#workPackageDiv #wpkg_desc").val(item.description).removeAttr("readonly");
				      }
				      else{
				    	  if(item.description==null)							
								$("#workPackageDiv #wpkg_desc").val("NA").attr("readonly", "true");
							else								
								$("#workPackageDiv #wpkg_desc").val(item.description).attr("readonly", "true");
				    	  	    	  
				      }
				      /*  Abort_completed  End For description . */
						$("#prodName").append("<div style='font-size:small;' >"+item.productName+"</div>");
						$("#prodBuildName").append("<div style='font-size:small;' >"+item.pBuildName+"</div>");
						if(item.selectedTestCasesCount!=null)
							$("#sel_tccount").append("<div style='font-size:small;' >"+item.selectedTestCasesCount+"</div>");
						if(item.selectedTestSuitesCount!=null)
						$("#sel_tscount").append("<div style='font-size:small;' >"+item.selectedTestSuitesCount+"</div>");
						if(item.selectedFeaturesCount!=null)
						$("#sel_featurecount").append("<div style='font-size:small;' >"+item.selectedFeaturesCount+"</div>");

						$("#tc_executed_count").append("<div style='font-size:small;' >"+item.totalTestCaseForExecutionCount+"</div>");
							
						if(item.plannedStartDate!= null && item.plannedEndDate != null){
							plannedEndDate=item.plannedEndDate;
							plannedStartDate=item.plannedStartDate;
							setDateOnDateRangepicker("#plan_defaultrange", setFormattedDate(item.plannedStartDate), setFormattedDate(item.plannedEndDate));
						}				
					
						if(item.actualStartDate!= null && item.actualEndDate != null)
							setDateOnDateRangepicker("#actual_defaultrange", setFormattedDate(item.actualStartDate), setFormattedDate(item.actualEndDate));
						
						
						$("#percent_completed").append("<div style='font-size:small;' >"+item.completedTestCasePercentage+"%</div>");
						if(item.environmentCombination==null)
							$("#envList").append("<div style='font-size:small;' >NA</div>");
						else
							$("#envList").append("<div style='font-size:small;' class='more1'>"+item.environmentCombination+"</div>");
						if(item.wpcreatedUser==null)
							$("#wpkg_owner").append("<div style='font-size:small;' >NA</div>");
						else
							$("#wpkg_owner").append("<div style='font-size:small;' class='more1'>"+item.wpcreatedUser+"</div>");
						
						//$("#env_count").append("<div style='font-size:small;' >"+item.environmentCount+"</div>");
						$("#total_tc").append("<div style='font-size:small;' >"+item.totalTestCaseCount+"</div>");
						$("#total_defects").append("<div style='font-size:small;' >"+item.totalDefectsCount+"</div>");
						/* if(item.productType!=null){
							document.getElementById("productType").value = item.productType;
						} */
						if(editableFlag)
							$("#iteration_No").val(item.iterationNo).removeAttr("readonly");
						else
							$("#iteration_No").val(item.iterationNo).attr("readonly", "true");
						
						currentStatus = item.workpackageStatus;						
						jsonstatus = item.workFlowstageNameList;	
						totalTCCount=item.totalTestCaseCount;
						totalTCExecutedCount=item.totalTestCaseForExecutionCount
						totalDefectsCount=item.totalDefectsCount;
							approvedDefectsCount=item.approvedDefectsCount;
							totalNumberOfCompDays=item.totalNumberOfCompDays;		
							totalNumberOfDays=item.totalNumberOfDays;
							totalNumberOfDaysCompleted=item.totalNumberOfDaysCompleted;
						totaljobscount = (item.jobsCount == null) ? 'NA' : item.jobsCount;
						totaljobscompleted = (item.testRunJobsCompleted == null) ? 'NA' : item.testRunJobsCompleted;
						$("#env_count").append("<div style='font-size:small;' >"+totaljobscount+"</div>"); //Runconfig val from total TestRunJobs.
						 $('#wpkg_status_options').empty(); 
						statusElement = document.getElementById('wpkg_status_options');
						var $select = $('#wpkg_status_options');
						var $currentStatusVal="";
						$.each(jsonstatus,function(key, value) 
						{
						    $select.append('<option value=' + key + '>' + value + '</option>');
						    if(currentStatus==key){
						    	currentStatusVal=value;
						    }
						});
						$('select[name="wpkg_status_options"] option[value="'+currentStatus+'"]').attr("selected","selected");
						$('#wpkg_status_options').select2(); 
						
						currentLife = item.lifeCycleId;						
						jsonLife = item.lifeCycleNameList;	
						$("#life_cycle_phase").empty();
						var $selectLife = $('#life_cycle_phase');
						var $currentStatusValLife="";
						$.each(jsonLife,function(key2, value2) 
						{
						    $selectLife.append('<option value=' + key2 + '>' + value2 + '</option>');
						    if(currentLife==key2){
						    	currentStatusValLife=value2;
						    }
						});
						$('select[name="life_cycle_phase"] option[value="'+currentLife+'"]').attr("selected","selected");
						$('#life_cycle_phase').select2(); 
					});
					 if(currentStatus=="9" || currentStatus=="10"||currentStatus=="11"){
						 $('#progress-bar').val('100');
				         $(this).addClass('border-change');
				        $(this).prevAll().addClass('border-change');
				         $('.percent').html("100% Complete"); 
					 }else if(currentStatus=="6"){
						 $('#progress-bar').val('0');
					        $(this).nextAll().removeClass('border-change');  
					        $('.percent').html("0% Complete");
					 }else if(currentStatus=="7"){
						 $(this).nextAll().removeClass('border-change');  
					        $('#progress-bar').val('34');
					        $(this).prevAll().addClass('border-change');  
					        $(this).addClass('border-change');
					         $('.percent').html("33% Complete");
					 }else if(currentStatus=="8"){
						 $(this).nextAll().removeClass('border-change');  
					        $('#progress-bar').val('67');
					        $(this).prevAll().addClass('border-change'); 
					        $(this).addClass('border-change');
					        $('.percent').html("66% Complete");
					 }
					
					 if(totalTCCount==0 && totalTCExecutedCount==0){
						 totalTCCount=0;
					 }
					 if(totalDefectsCount==0 && approvedDefectsCount==0){
						 totalDefectsCount=0;
					 }					

					 var testCaseWidth = Math.round(parseInt((totalTCExecutedCount/totalTCCount) || 0)*100) + "%" ;					
					 var defectsWidth = 0;
					 if(wpType == "Automated"){
						defectsWidth = 100 + "%";
					 }else{
						defectsWidth = Math.round((approvedDefectsCount/totalDefectsCount)*100) + "%";						
					 }
					 
					 var jobsWidth = 0;
					 if(totaljobscompleted != 0){
						jobsWidth = Math.round(parseInt((totaljobscompleted/totaljobscount)||0)*100) + "%";
					 }
					 
					 var jobscount = totaljobscount;
					 var timeCmpletedWidth ="";
					 if(totalNumberOfDaysCompleted==totalNumberOfDays){
						 timeCmpletedWidth="100%";
					 }else{
					 timeCmpletedWidth=Math.round((totalNumberOfDaysCompleted/totalNumberOfDays)*100) + "%";
					 }

					 $('#jqmeter-container').find('.progress-bar-important').css("width",testCaseWidth);
					 $('#jqmeter-container-defects').find('.progress-bar-important').css("width",defectsWidth);
					 $('#jqmeter-container-jobs').find('.progress-bar-important').css("width",jobsWidth);
					 $('#jqmeter-container-time').find('.progress-bar-important').css("width",timeCmpletedWidth);
					 $('#jqmeter-container').find('.percent').text(testCaseWidth + "[" + totalTCExecutedCount + "]" );
					 $('#jqmeter-container-defects').find('.percent').text(defectsWidth + "[" + totalDefectsCount + "]");
					 $('#jqmeter-container-jobs').find('.percent').text(jobsWidth + "[" + totaljobscompleted + "]");
					 $('#jqmeter-container-time').find('.percent').text(timeCmpletedWidth + "[" +  totalNumberOfDaysCompleted + "]");

				 	loadWorkPackageExecutionSummary(workPackageId);					
				},
				error : function(data){					
					closeLoaderIcon();
				},
				complete : function(data){				
					closeLoaderIcon();
				}
		        
			});
	 }
 
 function callCalender(type){
	 if(type=='start'){
	 	$( "#actualStartDate" ).datepicker();
	 }
	 if(type=='end'){
		 $( "#actualEndDate" ).datepicker();
	 }
	 if(type=='plannedStartDate'){
		 	$( "#plannedStartDate" ).datepicker();
		 }
		 if(type=='plannedEndDate'){
			 $( "#plannedEndDate" ).datepicker();
		 }
 }
 
 function changeValue(type){
	 var modifiedValue;
	 var modifiedField;
	 var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	 var thediv = document.getElementById('reportbox');
	 
	 if(type=='workPackageName'){
		 modifiedValue= $('#wpkg_name').val();
	 }
 	if(type=='description'){
 		modifiedValue=$('#wpkg_desc').val();
	 }
   	 
	 if(type == 'start' || type == 'end'){
		 modifiedValue = $("#hdnStartDate").val() + "~" + $("#hdnEndDate").val();
	 }
	 if(type == 'plannedStartDate' || type == 'plannedEndDate'){
		 modifiedValue = $("#hdnStartDate").val() + "~" + $("#hdnEndDate").val();
	 }
	if(type=='dropdown'){
		modifiedValue= $('#wpkg_status_options').val();
	}
	
	if(type=='iterationNo'){
		modifiedValue= $('#iteration_No').val();
	}
	
	if(type=='lifecyclephase'){
		modifiedValue= $('#life_cycle_phase').val();
	}
	
	 url='administration.workpackage.update.summary?workpackageId='+workpackageId+"&modifiedfField="+type+"&modifiedValue="+modifiedValue;
	   
	if (thediv.style.display == "none") {
	$.blockUI({
	 	theme : true,
	 	title : 'Please Wait',
	 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
	 });
	 $.post(url,function(data) {
		 if(data.Result=="ERROR"){
			 $.unblockUI();
			 callAlert(data.Message);
			 return false;
		 }
		 $.unblockUI();
		});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
 }
 
 
function loadWorkPackageExecutionSummary(workPackageId){
	try{
		if ($('#jTableContainerWPSummary').length>0) {
			 $('#jTableContainerWPSummary').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerWPSummary').jtable({
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
         
          actions: {
       	 	listAction: 'workpackage.testcase.execution.summary.listByDate?workPackageId='+workPackageId,
       	 editinlineAction: ''
        },  
        fields : {
        	executedDate: { 
        		title: 'Date',  
        		width: "20%",                          
                create: false,
                edit:false
        	},       
        	shiftName: { 
        		title: 'Shift Name',  
        		width: "20%",                          
                create: false,
                edit:false
        	},       
        	completedTestCaseCount: { 
        		title: 'Test Cases Completed ', 
        		width: "20%",                         
                create: false,
                edit:false
           },
            rejectedTestCaseCount: { 
       		title: 'Reviewed',
       		width: "20%",         
               create: false,
               edit:false							                
            },
            approvedTestCaseCount: { 
   		   title: 'Approved',  
   		   width: "20%",         
           create: false,
           list:true,
           edit:false
       },
       
           defectsCount: { 
        		title: 'Defects',  
        		width: "25%",                        
                create: false,
                edit:false
           }
            },
            
              // This is for closing $img.click(function (data) {  
	      });
	 $('#jTableContainerWPSummary').jtable('load');

}
function resourceDemand(nodeType){	
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	$('#Demand').children().show();
	document.getElementById("ResultsGridView").style.display = "none";
	document.getElementById("envLocTS").style.display = "none";

	document.getElementById("envLoc").style.display = "none";
	document.getElementById("treeHdnCurrentWorkPackageId").value = workPackageId;
  	document.getElementById("treeHdnCurrentWorkPackageName").value = workPackageName;
    var wNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
  	urlForWorkPackageDemandprojection = 'workpackage.demand.projection.list?workPackageId='+workPackageId+"&weekNumber="+wNo;
  	
  urlForWorkPackageDemandProjectionWeekly =	'workpackage.demand.projection.list.for.all.weeks?workPackageId='+workPackageId+"&weekNumber="+wNo+"&weekYear="+operationYear;
  	
  	
 	urlForWorkPackageWeeklyPlan = 'workpackage.executionplan.weekly?workPackageId='+workPackageId+"&weekNo="+wNo;
  	getWeekDateNames(0);
  	//showWorkPackageResourceDemandOfSelectedWorkPackageTableWeekly();
  	showResourceDemandAndReservation('resourceReservationAndDemandContainer', workPackageId, '150px');
  	closeLoaderIcon();
}

function reserveResource(nodeType){	
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	$('#Block').children().show();
	$("#resourceDemandDatePickerDiv").css('display','block');
	document.getElementById("envLoc").style.display = "none";
	document.getElementById("ResultsGridView").style.display = "none";
	
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	urlToGetResourceDemandOfWorkPackage = 'workPackage.resource.demand.listbywp?workPackageId='+workPackageId+'&resourceDemandForDate='+datepickerRP.value;
	listResourceDemandOfWorkPackage();
	
	var wNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
	urlToGetResourceReservarionForWeekly =	'workpackage.resource.reservation.projection.list.for.all.weeks?workPackageId='+workPackageId+"&reservationWeek="+wNo+"&reservationYear="+operationYear;
	showWorkPackageResourceReservationOfSelectedWorkPackageTableWeekly();
	closeLoaderIcon();
}

function defineTestcases(nodeType){ 
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	$('div#Plan').children().not('.jtbFeature, .jtbCase, .jtbSuite').show();		
	$("#accordionWD").next().addClass("active in").siblings().removeClass("active in");
	document.getElementById("envLoc").style.display = "block";	
	document.getElementById("ResultsGridView").style.display = "none";
	if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
		callAlert("Please select the workPackage");
		return false;
	} 
	urlToGetWorkPackagesTestCasesOfSpecifiedWorkPackage = 'workpackage.testcase.list?workPackageId='+workPackageId+"&timeStamp="+timestamp;
	//callJTableFun();//END - Delete - JTable
	//getEnvironmentsForProduct();
	tType = "WorkpackageTestcaseTable";
	workpackageTestcaseDT();//ConvertDataTable
}

//BEGIN: ConvertDataTable - WorkpackageTestcase
var workpackageTestcaseDT_oTable='';
var editorWorkpackageTestcase='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tType,currTestCaseId,currTestSuiteId,currFeatureId;

function workpackageTestcaseDT(){
	if(tType == "WorkpackageTestcaseTable"){
		url= urlToGetWorkPackagesTestCasesOfSpecifiedWorkPackage +'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Testcase","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Workpackage Testcase"};
	}else if(tType == "WorkpackageTestStepsTable"){
		url= 'testcase.teststep.list?testCaseId='+ currTestCaseId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"View Test Cases","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Workpackage Teststep"};
	}else if(tType == "WorkpackageTestsuiteTable"){
		url= urlToGetWorkPackagesTestSuiteOfSpecifiedWorkPackage +'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Test Suite","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Workpackage Test Suite"};
	}else if(tType == "WorkpackageViewTestcaseTable"){
		url= 'test.suite.case.list?testSuiteId='+ currTestSuiteId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage View Test Cases","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"View Test Cases"};
	}else if(tType == "WorkpackageFeaturesTable"){
		url= urlToGetWorkPackagesFeaturesOfSpecifiedWorkPackage +'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Features","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"WorkPackage Features"};
	}else if(tType == "WorkpackageFeaturesMappedTestcaseTable"){
		url= 'product.feature.testcase.mappedlist?productFeatureId='+ currFeatureId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Features Mapped Test Cases","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"WorkPackage Features Mapped Test Cases"};
	}else if(tType == "WorkpackageFeaturesMappedTestbedsTable"){
		url= 'feature.testbeds.list?featureId='+currFeatureId+'&workpackageId='+workPackageId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Features Mapped Testbeds","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"WorkPackage Features Mapped Testbeds"};
	}else if(tType == "WorkpackageTestsuiteMappedTestbedsTable"){
		url= 'testsuite.testbeds.list?testsuiteId='+currTestSuiteId+'&workpackageId='+workPackageId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Workpackage Testsuite Mapped Testbeds","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"WorkPackage Testsuite Mapped Testbeds"};
	}else if(tType == "AllocateTestcaseFeaturesTable"){
		var status = $("#testBedfeaturestatus_ul").find('option:selected').val();
		url= 'workpackage.featureplan.list?workpackageId='+workPackageId+'&status='+status+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Allocate Testcase Features","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Allocate Testcase Features"};
	}else if(tType == "AllocateTestcaseTestcasesTable"){
		var date = new Date();
		var timestamp = date.getTime();
		var status = $("#testBedtestcasestatus_ul").find('option:selected').val();
		url= 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId=0&testerId=0&envId=0&localeId=0&plannedExecutionDate=&dcId=0&executionPriority=0&status="+status+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Allocate Testcase Testcases","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Allocate Testcase Testcases"};
	}else if(tType == "AllocateTestcaseTestsuitesTable"){
		var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();
		url= 'workpackage.testSuiteplan.list?workpackageId='+workPackageId+'&status='+status+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Allocate Testcase Testsuites","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Allocate Testcase Features"};
	}
	workpackageTestcaseDataTableContainer.init(jsonObj);
}

var workpackageTestcaseDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignWorkpackageTestcaseDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignWorkpackageTestcaseDataTableValues(jsonObj){
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
			if(tType == "WorkpackageTestcaseTable"){
				workpackageTestcaseDT_Container(jsonObj);
			}else if(tType == "WorkpackageTestStepsTable"){
				workpackageTeststepDT_Container(jsonObj);
			}else if(tType == "WorkpackageTestsuiteTable"){
				workpackageTestsuiteDT_Container(jsonObj);
			}else if(tType == "WorkpackageViewTestcaseTable"){
				workpackageViewTestcaseDT_Container(jsonObj);
			}else if(tType == "WorkpackageFeaturesTable"){
				workpackageFeaturesDT_Container(jsonObj);
			}else if(tType == "WorkpackageFeaturesMappedTestcaseTable"){
				workpackageFeaturesMappedTestcaseDT_Container(jsonObj);
			}else if(tType == "WorkpackageFeaturesMappedTestbedsTable"){
				workpackageFeaturesMappedTestbedsDT_Container(jsonObj);
			}else if(tType == "WorkpackageTestsuiteMappedTestbedsTable"){
				workpackageFeaturesMappedTestbedsDT_Container(jsonObj);
			}else if(tType == "AllocateTestcaseFeaturesTable"){
				allocateTestcaseFeaturesDT_Container(jsonObj);
			}else if(tType == "AllocateTestcaseTestcasesTable"){
				allocateTestcaseTestcasesDT_Container(jsonObj);
			}else if(tType == "AllocateTestcaseTestsuitesTable"){
				allocateTestcaseTestsuitesDT_Container(jsonObj);
			}
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function workpackageTestcaseDataTableHeader(){
	var childDivString ='<table id="workpackageTestcase_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th></th>'+
			'<th>Test Step</th>'+
			'<th>Testcase ID</th>'+
			'<th>Testcase Code</th>'+
			'<th>Testcase</th>'+
			'<th>Description</th>'+
			'<th>Decoupling Category</th>'+
			'<th>Recommended</th>'+
			'<th>Why</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}
function workpackageTestcaseDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageTestcase").children().length>0) {
			$("#dataTableContainerForWorkpackageTestcase").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageTestcaseDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageTestcase").append(childDivString);
	
	editorWorkpackageTestcase = new $.fn.dataTable.Editor( {
		"table": "#workpackageTestcase_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new testcase",
	            submit: "Create",
	        }
	    },
		fields: [{
			label: "isSelected",
			name: "isSelected",
			"type": "hidden",					
		}]
	});	
	
	workpackageTestcaseDT_oTable = $("#workpackageTestcase_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,1]; // search column TextBox Invisible Column position
	     		  $('#workpackageTestcase_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageTestcaseDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
	        { mData: "isSelected",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorWorkpackageTestcase-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
	        { mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="workpackageTestStepImg" title="Test Step" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
			{ mData: "testcaseId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testcaseCode",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testcaseName",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testcaseDescription",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "decouplingCategoryName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "recommendedByITF",className: 'disableEditInline', sWidth: '5%' },           
			{ mData: "recommendationType", className: 'disableEditInline', sWidth: '10%'},	
	        	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorWorkpackageTestcase-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageTestcase_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageTestcase.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#workpackageTestcase_dataTable tbody').on('click', 'td .workpackageTestStepImg', function () {
		var tr = $(this).closest('tr');
    	var row = workpackageTestcaseDT_oTable.DataTable().row(tr);
    	currTestCaseId = row.data().testcaseId;
    	$('#workpackageTestcaseDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		tType = "WorkpackageTestStepsTable";
		workpackageTestcaseDT();
	});

	$('#workpackageTestcase_dataTable').on( 'change', 'input.editorWorkpackageTestcase-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = workpackageTestcaseDT_oTable.DataTable().row(tr);
    	
    	/*editorWorkpackageTestcase
	        .edit( $(this).closest('tr'), false )
	        .set( 'isSelected', $(this).prop('checked') ? 1 : 0)
	        .submit();*/
    		var flag=$(this).prop('checked');
	    	var obj = row.data();
	    	var fd = new FormData();
	    	for(key in obj){
	    		if(key == "isSelected"){	    			
	    			if(flag){
	    				fd.append(key, 1);
	    			}else{
	    				fd.append(key, 0);
	    			}
	    		}else{
	    			fd.append(key, obj[key]);
	    		}
    		}	    	

    	$.ajax({
			type: "POST",
			url: "workpackage.testcase.update",
			data : fd,
			contentType: false,
			processData: false,
			type: "POST",
			success: function(data) {
				if(data.Result=='ERROR'){
					//callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	}); 
	});
	
	$("#workpackageTestcase_dataTable_length").css('margin-top','8px');
	$("#workpackageTestcase_dataTable_length").css('padding-left','35px');
	
	$("#workpackageTestcase_dataTable_filter").css('margin-right','6px');
	
	workpackageTestcaseDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageTestcaseDT='';
function reInitializeWorkpackageTestcaseDT(){
	clearTimeoutWorkpackageTestcaseDT = setTimeout(function(){				
		workpackageTestcaseDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageTestcaseDT);
	},200);
}
//END: ConvertDataTable - WorkpackageTestcase

//BEGIN: ConvertDataTable - WorkpackageTeststeps
function workpackageTeststepDataTableHeader(){
	var childDivString ='<table id="workpackageTeststep_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Input</th>'+
			'<th>Expected Output</th>'+
			'<th>Source</th>'+
			'<th>Code</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function workpackageTeststepDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageTeststep").children().length>0) {
			$("#dataTableContainerForWorkpackageTeststep").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageTeststepDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageTeststep").append(childDivString);
	
	editorWorkpackageTeststep = new $.fn.dataTable.Editor( {
		"table": "#workpackageTeststep_dataTable",
		idSrc:  "testStepId",
		i18n: {
	        create: {
	            title:  "Create a new teststep",
	            submit: "Create",
	        }
	    },
		fields: []
	});	
	
	workpackageTeststepDT_oTable = $("#workpackageTeststep_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#workpackageTeststep_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageTeststepDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Teststep',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Teststep',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
			{ mData: "testStepName",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testStepDescription",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testStepInput",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testStepExpectedOutput",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testStepSource",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testStepCode",className: 'disableEditInline', sWidth: '5%' },           
	        	
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorWorkpackageTeststep-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageTeststep_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageTeststep.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#workpackageTeststep_dataTable_length").css('margin-top','8px');
	$("#workpackageTeststep_dataTable_length").css('padding-left','35px');
	
	$("#workpackageTeststep_dataTable_filter").css('margin-right','6px');
	
	workpackageTeststepDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageTeststepDT='';
function reInitializeWorkpackageTeststepDT(){
	clearTimeoutWorkpackageTeststepDT = setTimeout(function(){				
		workpackageTeststepDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageTeststepDT);
	},200);
}
//END: ConvertDataTable - WorkpackageTeststeps

//BEGIN: ConvertDataTable - WorkpackageTestsuite
function workpackageTestsuiteDataTableHeader(){
	var childDivString ='<table id="workpackageTestsuite_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Selected</th>'+
			'<th>Test Suite ID</th>'+
			'<th>Test Suite Name</th>'+
			'<th>Mapped Testbeds</th>'+
			'<th>View Testcase</th>'+
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
function workpackageTestsuiteDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageTestsuite").children().length>0) {
			$("#dataTableContainerForWorkpackageTestsuite").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageTestsuiteDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageTestsuite").append(childDivString);
	
	editorWorkpackageTestsuite = new $.fn.dataTable.Editor( {
		"table": "#workpackageTestsuite_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new testsuite",
	            submit: "Create",
	        }
	    },
		fields: [
			{								
				label:"testSuiteId",
				name:"testSuiteId",		
				type: 'hidden',					
			},{
				label:"Test Suite Name",
				name:"testSuiteName",									
			},{
				label:"id",
				name:"id",									
			},{
				label: "isSelected",
				name: "isSelected", 
			},{
				label: "workPackageId",
				name: "workPackageId",                
			},{
				label:"workPackageName",
				name:"workPackageName",									
			},{
				label: "createdDate",
				name: "createdDate",   
				type: 'hidden',					
			},{
				label: "status",
				name: "status",   
				type: 'hidden',		
			},{
				label: "editedDate",
				name: "editedDate", 
			},{
				label: "recommendedByITF",
				name: "recommendedByITF", 
			},{
				label: "recommendationType",
				name: "recommendationType", 
			}, 
		]
	});	
	
	workpackageTestsuiteDT_oTable = $("#workpackageTestsuite_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,3,4]; // search column TextBox Invisible Column position
	     		  $('#workpackageTestsuite_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageTestsuiteDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testsuite',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testsuite',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
	        { mData: "isSelected",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorWorkpackageTestsuite-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "testsuiteId",className: 'disableEditInline', sWidth: '45%' },			
			{ mData: "testsuiteName",className: 'disableEditInline', sWidth: '45%' },
	        { mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="workpackageTestsuiteMappedTestbedsImg" title="Mapped Testbeds" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
			{ mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="workpackageViewTestcaseImg" title="View Testcases" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorWorkpackageTestsuite-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageTestsuite_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageTestsuite.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#workpackageTestsuite_dataTable tbody').on('click', 'td .workpackageViewTestcaseImg', function () {
		var tr = $(this).closest('tr');
    	var row = workpackageTestsuiteDT_oTable.DataTable().row(tr);
    	currTestSuiteId = row.data().testsuiteId;
    	$('#workpackageTestsuiteDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		workpackageViewTestcaseDataTable();
	});

	$('#workpackageTestsuite_dataTable').on( 'change', 'input.editorWorkpackageTestsuite-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = workpackageTestsuiteDT_oTable.DataTable().row(tr);
		var formdata = new FormData();
		formdata.append("createdDate", row.data().createdDate);
		formdata.append("editedDate", row.data().editedDate);
		formdata.append("id", row.data().id);
		formdata.append("isSelected", row.data().isSelected);
		formdata.append("recommendationType", row.data().recommendationType);
		formdata.append("recommendedByITF", row.data().recommendedByITF);
		formdata.append("status", row.data().status);
		formdata.append("testSuiteId", row.data().testsuiteId);
		formdata.append("testSuiteName", row.data().testsuiteName);
		formdata.append("workPackageId", row.data().workPackageId);
		formdata.append("workPackageName", row.data().workPackageName);
    	$.ajax({
			method: 'POST',
			contentType: false,
			data: formdata,
			url: "workpackage.testsuite.update",
			dataType:'json',
			processData: false,
			success: function(data) {
				if(data.Result=='ERROR'){
					//callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 }, 
			 error: function (){
				console.log('error'); 
			 },				
	 	});
	});
	
	$("#workpackageTestsuite_dataTable_length").css('margin-top','8px');
	$("#workpackageTestsuite_dataTable_length").css('padding-left','35px');
	
	$("#workpackageTestsuite_dataTable_filter").css('margin-right','6px');
	
	$('#workpackageTestsuite_dataTable tbody').on('click', 'td .workpackageTestsuiteMappedTestbedsImg', function () {
		var tr = $(this).closest('tr');
    	var row = workpackageTestsuiteDT_oTable.DataTable().row(tr);
		currTestSuiteId = row.data().testsuiteId;
    	$('#workpackageFeaturesMappedTestbedsDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		tType = "WorkpackageTestsuiteMappedTestbedsTable";
		workpackageTestcaseDT();
	});
	
	workpackageTestsuiteDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageTestsuiteDT='';
function reInitializeWorkpackageTestsuiteDT(){
	clearTimeoutWorkpackageTestsuiteDT = setTimeout(function(){				
		workpackageTestsuiteDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageTestsuiteDT);
	},200);
}
//END: ConvertDataTable - WorkpackageTestsuite

//BEGIN: ConvertDataTable - WorkpackageViewTestcase
function workpackageViewTestcaseDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [ {id:"executionTypeId", url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
		               {id:"testcasePriority", url:'common.list.testcasepriority'}];
	workpackageViewTestcaseOptions_Container(optionsArr);
}

function workpackageViewTestcaseOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 workpackageViewTestcaseOptions_Container(optionsArr);
			 }else{
				tType = "WorkpackageViewTestcaseTable";
				workpackageTestcaseDT();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function workpackageViewTestcaseDataTableHeader(){
	var childDivString ='<table id="workpackageViewTestcase_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Testcase ID</th>'+
			'<th>Testcase Name</th>'+
			'<th>Testcase Description</th>'+
			'<th>Testcase Code</th>'+
			'<th>Execution Type</th>'+
			'<th>Priority</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}
function workpackageViewTestcaseDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageViewTestcase").children().length>0) {
			$("#dataTableContainerForWorkpackageViewTestcase").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageViewTestcaseDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageViewTestcase").append(childDivString);
	
	editorWorkpackageViewTestcase = new $.fn.dataTable.Editor( {
		"table": "#workpackageViewTestcase_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new testcase",
	            submit: "Create",
	        }
	    },
		fields: [
			{
				label: "executionTypeId",
				name: "executionTypeId",     
				options: optionsResultArr[0],
				"type"  : "select",	
			},{
				label:"testcasePriorityId",
				name:"testcasePriorityId",	
				options: optionsResultArr[1],
				"type"  : "select",	
			}
		]
	});	
	
	workpackageViewTestcaseDT_oTable = $("#workpackageViewTestcase_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#workpackageViewTestcase_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageViewTestcaseDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [   	        
			{ mData: "testCaseId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testCaseName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseDescription",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testCaseCode",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageViewTestcase, 'executionTypeId', full.executionTypeId);
					return data;
				 },
			},	
			{ mData: "testcasePriorityId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageViewTestcase, 'testcasePriorityId', full.testcasePriorityId);
					return data;
				 },
			},
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorWorkpackageViewTestcase-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageViewTestcase_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageViewTestcase.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#workpackageViewTestcase_dataTable_length").css('margin-top','8px');
	$("#workpackageViewTestcase_dataTable_length").css('padding-left','35px');
	
	$("#workpackageViewTestcase_dataTable_filter").css('margin-right','6px');
		
	workpackageViewTestcaseDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageViewTestcaseDT='';
function reInitializeWorkpackageViewTestcaseDT(){
	clearTimeoutWorkpackageViewTestcaseDT = setTimeout(function(){				
		workpackageViewTestcaseDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageViewTestcaseDT);
	},200);
}
//END: ConvertDataTable - WorkpackageViewTestcase

//BEGIN: ConvertDataTable - WorkpackageFeatures
function workpackageFeaturesDataTableHeader(){
	var childDivString ='<table id="workpackageFeatures_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Selected</th>'+
			'<th>Feature Code</th>'+
			'<th>Feature</th>'+
			'<th>Description</th>'+
			'<th>Mapped Testbeds</th>'+
			'<th>View Testcase</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}
function workpackageFeaturesDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageFeatures").children().length>0) {
			$("#dataTableContainerForWorkpackageFeatures").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageFeaturesDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageFeatures").append(childDivString);
	
	editorWorkpackageFeatures = new $.fn.dataTable.Editor( {
		"table": "#workpackageFeatures_dataTable",
		idSrc:  "featureId",
		i18n: {
	        create: {
	            title:  "Create a new feature",
	            submit: "Create",
	        }
	    },
		fields: []
	});	
	
	workpackageFeaturesDT_oTable = $("#workpackageFeatures_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,4,5]; // search column TextBox Invisible Column position
	     		  $('#workpackageFeatures_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageFeaturesDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Features',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Features',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
	        { mData: "isSelected",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorWorkpackageFeatures-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "productFeatureCode",className: 'disableEditInline', sWidth: '30%' },			
			{ mData: "featureName",className: 'disableEditInline', sWidth: '30%' },
			{ mData: "featureDesc",className: 'disableEditInline', sWidth: '30%' },
	        { mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="workpackageFeatureMappedTestbedsImg" title="Mapped Testbeds" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
			{ mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="workpackageFeatureMappedTestcaseImg" title="View Testcases" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorWorkpackageFeatures-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageFeatures_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageFeatures.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#workpackageFeatures_dataTable tbody').on('click', 'td .workpackageFeatureMappedTestbedsImg', function () {
		var tr = $(this).closest('tr');
    	var row = workpackageFeaturesDT_oTable.DataTable().row(tr);
		currFeatureId = row.data().featureId;
    	$('#workpackageFeaturesMappedTestbedsDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		tType = "WorkpackageFeaturesMappedTestbedsTable";
		workpackageTestcaseDT();
	});
	
	$('#workpackageFeatures_dataTable tbody').on('click', 'td .workpackageFeatureMappedTestcaseImg', function () {
		var tr = $(this).closest('tr');
    	var row = workpackageFeaturesDT_oTable.DataTable().row(tr);
		currFeatureId = row.data().featureId;
    	$('#workpackageFeaturesDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		workpackageFeaturesMappedTestcaseDataTable();
	});

	$('#workpackageFeatures_dataTable').on( 'change', 'input.editorWorkpackageFeatures-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = workpackageFeaturesDT_oTable.DataTable().row(tr);
		var formdata = new FormData();
		formdata.append("createdDate", row.data().createdDate);
		formdata.append("editedDate", row.data().editedDate);
		formdata.append("id", row.data().id);
		formdata.append("isSelected", row.data().isSelected);
		formdata.append("recommendationType", row.data().recommendationType);
		formdata.append("recommendedByITF", row.data().recommendedByITF);
		formdata.append("status", row.data().status);
		formdata.append("testSuiteId", row.data().testsuiteId);
		formdata.append("testSuiteName", row.data().testsuiteName);
		formdata.append("workPackageId", row.data().workPackageId);
		formdata.append("workPackageName", row.data().workPackageName);
    	$.ajax({
			method: 'POST',
			contentType: false,
			data: formdata,
			url: "workpackage.feature.update",
			dataType:'json',
			processData: false,
			success: function(data) {
				if(data.Result=='ERROR'){
					//callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 }, 
			 error: function (){
				console.log('error'); 
			 },				
	 	});
	});
	
	$("#workpackageFeatures_dataTable_length").css('margin-top','8px');
	$("#workpackageFeatures_dataTable_length").css('padding-left','35px');
	
	$("#workpackageFeatures_dataTable_filter").css('margin-right','6px');
	
	workpackageFeaturesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageFeaturesDT='';
function reInitializeWorkpackageFeaturesDT(){
	clearTimeoutWorkpackageFeaturesDT = setTimeout(function(){				
		workpackageFeaturesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageFeaturesDT);
	},200);
}
//END: ConvertDataTable - WorkpackageFeatures

//BEGIN: ConvertDataTable - WorkpackageFeaturesMappedTestcase
function workpackageFeaturesMappedTestcaseDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [ {id:"executionTypeId", url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
		               {id:"testcasePriority", url:'common.list.testcasepriority'},
					   {id:"testcasetype", url:'common.list.testcasetype'},
					   {id:"testcasePriority", url:'product.feature.list?productId='+prodId}];
	workpackageFeaturesMappedTestcaseOptions_Container(optionsArr);
}

function workpackageFeaturesMappedTestcaseOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 workpackageFeaturesMappedTestcaseOptions_Container(optionsArr);
			 }else{
				tType = "WorkpackageFeaturesMappedTestcaseTable";
				workpackageTestcaseDT();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function workpackageFeaturesMappedTestcaseDataTableHeader(){
	var childDivString ='<table id="workpackageFeaturesMappedTestcase_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Testcase ID</th>'+
			'<th>Testcase Name</th>'+
			'<th>Testcase Description</th>'+
			'<th>Testcase Code</th>'+
			'<th>Script Package Name Type</th>'+
			'<th>Script File Name</th>'+
			'<th>Execution Type</th>'+
			'<th>Priority</th>'+
			'<th>Testcase Type</th>'+
			'<th>Features</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}
function workpackageFeaturesMappedTestcaseDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageFeaturesMappedTestcase").children().length>0) {
			$("#dataTableContainerForWorkpackageFeaturesMappedTestcase").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageFeaturesMappedTestcaseDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageFeaturesMappedTestcase").append(childDivString);
	
	editorWorkpackageFeaturesMappedTestcase = new $.fn.dataTable.Editor( {
		"table": "#workpackageFeaturesMappedTestcase_dataTable",
		idSrc:  "testCaseId",
		i18n: {
	        create: {
	            title:  "Create a new Feature Mapped testcase",
	            submit: "Create",
	        }
	    },
		fields: [
			{
				label: "executionTypeId",
				name: "executionTypeId",     
				options: optionsResultArr[0],
				"type"  : "select",	
			},{
				label:"testcasePriorityId",
				name:"testcasePriorityId",	
				options: optionsResultArr[1],
				"type"  : "select",	
			},{
				label:"testcaseTypeId",
				name:"testcaseTypeId",	
				options: optionsResultArr[2],
				"type"  : "select",	
			},{
				label:"productFeatureId",
				name:"productFeatureId",	
				options: optionsResultArr[3],
				"type"  : "select",	
			}
		]
	});	
	
	workpackageFeaturesMappedTestcaseDT_oTable = $("#workpackageFeaturesMappedTestcase_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#workpackageFeaturesMappedTestcase_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageFeaturesMappedTestcaseDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Mapped Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Mapped Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [   	        
			{ mData: "testCaseId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testCaseName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseDescription",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testCaseCode",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseScriptQualifiedName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseScriptFileName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageFeaturesMappedTestcase, 'executionTypeId', full.executionTypeId);
					return data;
				 },
			},	
			{ mData: "testcasePriorityId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageFeaturesMappedTestcase, 'testcasePriorityId', full.testcasePriorityId);
					return data;
				 },
			},
			{ mData: "testcaseTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageFeaturesMappedTestcase, 'testcaseTypeId', full.testcaseTypeId);
					return data;
				 },
			},
			{ mData: "productFeatureId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorWorkpackageFeaturesMappedTestcase, 'productFeatureId', full.productFeatureId);
					return data;
				 },
			},
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorWorkpackageFeaturesMappedTestcase-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageFeaturesMappedTestcase_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageFeaturesMappedTestcase.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#workpackageFeaturesMappedTestcase_dataTable_length").css('margin-top','8px');
	$("#workpackageFeaturesMappedTestcase_dataTable_length").css('padding-left','35px');
	
	$("#workpackageFeaturesMappedTestcase_dataTable_filter").css('margin-right','6px');
		
	workpackageFeaturesMappedTestcaseDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageFeaturesMappedTestcaseDT='';
function reInitializeWorkpackageFeaturesMappedTestcaseDT(){
	clearTimeoutWorkpackageFeaturesMappedTestcaseDT = setTimeout(function(){				
		workpackageFeaturesMappedTestcaseDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageFeaturesMappedTestcaseDT);
	},200);
}
//END: ConvertDataTable - WorkpackageFeaturesMappedTestcase

//BEGIN: ConvertDataTable - WorkpackageFeaturesMappedTestbeds
function workpackageFeaturesMappedTestbedsDataTableHeader(){
	var childDivString ='<table id="workpackageFeaturesMappedTestbeds_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Environment Combinataion</th>'+
			'<th>Device</th>'+
			'<th>Host</th>'+
			'<th>Status</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function workpackageFeaturesMappedTestbedsDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForWorkpackageFeaturesMappedTestbeds").children().length>0) {
			$("#dataTableContainerForWorkpackageFeaturesMappedTestbeds").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageFeaturesMappedTestbedsDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackageFeaturesMappedTestbeds").append(childDivString);
	
	editorWorkpackageFeaturesMappedTestbeds = new $.fn.dataTable.Editor( {
		"table": "#workpackageFeaturesMappedTestbeds_dataTable",
		idSrc:  "environmentcombinationId",
		i18n: {
	        create: {
	            title:  "Create a new testbed",
	            submit: "Create",
	        }
	    },
		fields: []
	});	
	
	workpackageFeaturesMappedTestbedsDT_oTable = $("#workpackageFeaturesMappedTestbeds_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
	     		  $('#workpackageFeaturesMappedTestbeds_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageFeaturesMappedTestbedsDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Feature Mapped Testbeds',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Feature Mapped Testbeds',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
			{ mData: "environmentcombinationName",className: 'disableEditInline', sWidth: '30%' },			
			{ mData: "deviceName",className: 'disableEditInline', sWidth: '30%' },
			{ mData: "hostIpAddress",className: 'disableEditInline', sWidth: '30%' },			
			{ mData: "status",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorWorkpackageFeaturesMappedTestbeds-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
	        	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorWorkpackageFeaturesMappedTestbeds-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackageFeaturesMappedTestbeds_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackageFeaturesMappedTestbeds.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#workpackageFeaturesMappedTestbeds_dataTable_length").css('margin-top','8px');
	$("#workpackageFeaturesMappedTestbeds_dataTable_length").css('padding-left','35px');
	
	$("#workpackageFeaturesMappedTestbeds_dataTable_filter").css('margin-right','6px');
	
	workpackageFeaturesMappedTestbedsDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageFeaturesMappedTestbedsDT='';
function reInitializeWorkpackageFeaturesMappedTestbedsDT(){
	clearTimeoutWorkpackageFeaturesMappedTestbedsDT = setTimeout(function(){				
		workpackageFeaturesMappedTestbedsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageFeaturesMappedTestbedsDT);
	},200);
}
//END: ConvertDataTable - WorkpackageFeaturesMappedTestbeds

function defineTestSuite(nodeType){	
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	
	 document.getElementById("envLoc").style.display = "block";
	 document.getElementById("ResultsGridView").style.display = "none";

	 if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
			callAlert("Please select the workPackage");
			return false;
		}
 
	urlToGetWorkPackagesTestSuiteOfSpecifiedWorkPackage = 'workpackage.testsuite.list?workPackageId='+workPackageId+"&timeStamp="+timestamp;
	//callJTableFunTestSuite();//END - Delete - JTable
	tType = "WorkpackageTestsuiteTable";
	workpackageTestcaseDT();
}
/*//BEGIN - Delete - JTable
function callJTableFunTestSuite(){

	var fields = {};
	//Add the static fields
	fields['id'] = {key: true,list:false,edit:false};
	fields['testsuiteId'] =  {title: 'Test Suite ID',list:true,edit: false,width:"20%"};
	fields['testsuiteName'] = {title: 'Test Suite ',edit: false,width:"20%"};
	fields['workPackageId'] = {title: 'work Package ID',list:false,edit: false,width:"20%"};
	fields['workPackageName'] = {title: 'Work Package Name',list:false,edit: false,width:"20%"};
	
	fields['isSelected'] = {title: 'Selected',type : 'checkbox',width:"20%",values: {'1' : 'Yes','0' : 'No'}};
	fields['createdDate'] = {title: 'Created Date',list:false,edit: false,width:"20%"};
	fields['editedDate'] = {title: 'Edited Date',list:false,edit:false,width:"20%"};
	fields['status'] = {title: 'Status',list:false,width:"20%"};
	//testbed list starts
	fields['testBedsMapped']={
			 title:'Mapped TestBeds',
		        width: "2%",
		        create:false,
		        edit:true,
		        display: function (childData) { 
    				//Create an image that will be used to open child table 
    				var $img = $('<img src="css/images/list_metro.png" title="Mapped TestBeds" />'); 
		        	//Open child table when user clicks the image 
		        	$img.click(function (data) { 
		        		
		        		// ----- Closing child table on the same icon click -----
						closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerPlanTS"));
						if(closeChildTableFlag){
							return;
						}
		        		
			        	$('#jTableContainerPlanTS').jtable('openChildTable', 
			        	$img.closest('tr'), 
			        	{ 
			        	title: 'Mapped TestBeds',
			        	paging: true, //Enable paging
			            pageSize: 10, //Set page size (default: 10)
			            selecting: true, //Enable selecting 
			            editinline:{enable:false},					        	  	
			        	actions: { 
			        		 listAction: 'testsuite.testbeds.list?testsuiteId='+childData.record.testsuiteId+'&workpackageId='+workPackageId,
			        		// editinlineAction: 'feature.testbeds.update',
			        	     recordUpdated:function(event, data){
			        	        	$('#jTableContainerPlanTS').find('.jtable-main-container').jtable('reload');
			        	        },
			        	     recordAdded: function (event, data){
			        	         	$('#jTableContainerPlanTS').find('.jtable-main-container').jtable('reload');
			        	        },
			            },
			        	fields: {		      
			        		runconfigId:{
					 	        	type: 'hidden',  
					    				list:false,
					    				key: true
					 	       		},
					 	       		environmentcombinationId: { 
					        			title: 'environmentcombinationId',
					         		create:false,
					         		edit: false,
					         		list:false,
					        			},        	
					        			environmentcombinationName: {
					                 title: 'Environment Combination ',
					                 width: "12%",
					                 create : true,
					                 edit :false,
					                 list:true
					         	},
					         	deviceId:{
					         		title: 'Device Id', 
					         		width: "7%",
					         		type : 'hidden',
					                 create: false,
					                 edit : false,
					                 list : false,
					         	},
					         	deviceName:{
					         		title: 'Device ', 
					         		width: "7%",                         
					                 create: false,
					                 list: true,
					                 edit:false
					         	},
					         	hostIpAddress:{
					         		title: 'Host', 
					         	    width: "40%",                         
					                 create: false,
					                 edit:false,
					                 list:true
					         	},
					         	status:{
					         		title: 'Status', 
					         	    width: "40%",                         
					                 create: false,
					                 edit:true,
					                 type : 'checkbox',
					                 values: {'1' : 'Yes','0' : 'No'},
					                 display: function (data) 
					                 {
					                	 
					                	 if(childData.record.isSelected==0){
					                		 return '<input type="checkbox" id="status" checked  disabled value='+data.record.status+'>';
					                	 }else{
					                	  if(data.record.status=="1")
			                                 return '<input type="checkbox" id="status" checked onclick=testBedTestSuiteMap(0,'+data.record.runconfigId+','+childData.record.testsuiteId+','+workPackageId+'); value='+data.record.status+'>';
					                		else 
	                                         return '<input type="checkbox" id="status" onclick=testBedTestSuiteMap(1,'+data.record.runconfigId+','+childData.record.testsuiteId+','+workPackageId+'); value='+data.record.status+'>';
					                	 }
					      			    },
					         	},
					         	hostId:{
					         		title: 'hostId', 
					         		width: "20%",                         
					                 create: false,
					                 list: false,
					                 edit: false
					         	},
					             
						    	 },
				    	 // This is for closing $img.click(function (data) { 
						 //Moved Formcreate validation to Form Submitting
			         	 //Validate form when it is being submitted
			       		
					}, 
     			function (data) { //opened handler 
		        	data.childTable.jtable('load'); 
		        	}); 
	        	}); 
	        		return $img; 
	        	}
			};	
	
	//testbed list ends
	fields['testCaseIdMapping'] = {
        	title: 'View Test Case',
        	width: "5%",
        	edit: true,
        	create: false,
   	 	display: function (testCaseData) { 
   		//Create an image that will be used to open child table 
   		 //  Abort_completed functionality for view Testcases Start . 
   			var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible;" title="View TestCase" />'); 
   			//  Abort_completed functionality for view Testcases End . 
   			//Open child table when user clicks the image 
   			$img.click(function () { 
   				
   				// ----- Closing child table on the same icon click -----
				closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerPlanTS"));
				if(closeChildTableFlag){
					return;
				}
				
   				$('#jTableContainerPlanTS').jtable('openChildTable', 
   				$img.closest('tr'), 
   					{ 
   					title: 'View TestCase', 
   					editinline:{enable:true},
   					actions: {
   					listAction: 'test.suite.case.list?testSuiteId='+testCaseData.record.testsuiteId,
   							},   	 			
   					fields: {productId:{
   			        	type: 'hidden',  
   		   				list:false,
   		   				defaultValue: prodId
   			       			},
   		            testCaseId: { 
   		       			title: 'Test Case ID',
   		        		create:false,
   		        		edit: false,
   		        		list:true,
   		        		key: true
   		       			},        	
   		        	testCaseName: {
   		                title: 'Test Case ',
   		                width: "12%",
   		                create : false,
   		                edit :false
   		        	},   		        	
   		        	testCaseDescription:{
   		        		title: 'Test Case Description', 
   		        		width: "20%",                         
   		                create: false   
   		        	},
   		        	testCaseSource:{
   		        		title: 'Test Case Source', 
   		        		width: "20%",                         
   		                create: false,
   		                list: false,
   		                edit: false
   		        	},
   		        	testCaseCode: { 
   		        		title: 'Test Case Code',  
   		        		width: "15%",                        
   		                create: false,
   		                edit : false,
   		        	},
   		        	testCasePriority:{
   		        		title: 'Test Case Priority',  
   		        		width: "15%",    
   		        		create: false,
   		        		edit : false,
   		        		list : false
   		        	},
   		        	testCaseScriptQualifiedName:{
   		        		title: 'Script Package Name',  
   		        		width: "15%",
   		        		create: false,
   		        		list : false, 
   		        		edit : false
   		        	},
   		        	testCaseScriptFileName:{
   		        		title: 'Script File Name',  
   		        		width: "15%",
   		        		create: false,
   		        		list : false, 
   		        		edit : false
   		        	}, 
   		        	testcaseExecutionType:{
   		        		title: 'Execution Type',  
   		        		width: "15%",
   		        		create: false,
   		        		list : false,           		        		
   		        		options : {
   							'1' : 'Manual',
   							'2' : 'Automated',
   							'3' : 'Mixed'
   						}
   		        	}, 
   		        	executionTypeId:{
   		        		title : 'Execution Type',
   		        		width : "15%",
   		        		create : false,
   		        		list : true,
   		        		edit : false,
   		      			options:function(data){
   		      				if(data.source =='list'){	      				
   		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
   		      				}else if(data.source == 'create'){	      				
   		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
   		      				}
   		      			}
   		        	}, 
   		        	testcasePriorityId:{
   		        		title : 'Priority',
   		        		width : "15%",
   		        		create : false,
   		        		list : true,
   		        		edit : false,
   		      			options:function(data){
   		      				if(data.source =='list'){	      				
   		      					return 'common.list.testcasepriority';	
   		      				}else if(data.source == 'create'){	      				
   		      					return 'common.list.testcasepriority';
   		      				}
   		      			}
   		        	}, 
   		},   	
         formSubmitting: function (event, data) {  
         data.form.find('input[name="testSuiteName"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
         data.form.validationEngine();
         return data.form.validationEngine('validate');
        }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
         data.form.validationEngine('hide');
         data.form.validationEngine('detach');
        }
   	}, function (data) { //opened handler 
   	data.childTable.jtable('load'); 
   	}); 
   	}); 
   	//Return image 
   	return $img; 
   	}           
       	 };
	//Destroy the current jtable so that it is forced to reload with the new workpackage id
	try{
		if ($('#jTableContainerPlanTS').length>0) {
			 $('#jTableContainerPlanTS').jtable('destroy'); 
		}
	} catch(e) {}
	 $('#jTableContainerPlanTS').jtable({
         
         title: 'Select Test Suite for Work Package',
         selecting: true,  //Enable selecting 
         
         paging: true, //Enable paging
         pageSize: 10, 
         openChildAsAccordion: true,
         editinline:{enable:true},
		 multiselect: true, //Allow multiple selecting
         selectingCheckboxes: true, //Show checkboxes on first column
         toolbar : {
			items : [ {
				text : 'Import Selected TestSuites',
				click : function() {					
				}
			} ]
		},
         actions: {
        	 listAction : urlToGetWorkPackagesTestSuiteOfSpecifiedWorkPackage,
        	 
        	 editinlineAction: 'workpackage.testsuite.update'
            },
        
         fields : fields,
         recordsLoaded: function(event, data) {
    		 var $selectedRowsDefine = $('#jTableContainerPlanTS').jtable('selectedRows');
    		 if($selectedRowsDefine >=0)
    		 {
    			 $("#jTableContainerPlanTS").find(".jtable-selecting-column > input").prop("checked", true);
    		 }
    	 }
		});
	 
	 $('#jTableContainerPlanTS').jtable('load');
	 
	 var jscrolheight = $("#jTableContainerPlanTS").height();
	 var jscrolwidth = $("#jTableContainerPlanTS").width();
	  
	 $(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		
				var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
			
				 if (lastScrollLeft < documentScrollLeft) {
				    	$("#jTableContainerPlanTS").width($(".jtable").width()).height($(".jtable").height());			
				        lastScrollLeft = documentScrollLeft;
				    }else if(lastScrollLeft >= documentScrollLeft){			
				    	$("#jTableContainerPlanTS").width(jscrolwidth).height(jscrolheight);
				    }			
	 });
}*///END - Delete - JTable

//Feature Starts

function defineFeatures(nodeType){
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	
	document.getElementById("envLoc").style.display = "block";
	//document.getElementById("filter").style.display = "none";
	 document.getElementById("ResultsGridView").style.display = "none";

	 if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
			callAlert("Please select the workPackage");
			return false;
		}
 
	urlToGetWorkPackagesFeaturesOfSpecifiedWorkPackage = 'workpackage.feature.list?workPackageId='+workPackageId+"&timeStamp="+timestamp;
	//callJTableFunFeature();//END - Delete - JTable
	tType = "WorkpackageFeaturesTable";
	workpackageTestcaseDT();//ConvertDataTable
}

//BEGIN - Delete - JTable
/*function testBedFeatureMap(status,runConfigId,featureId,workpackageId){
	if(status==1){
        urlMapping = 'administration.testBed.plan?runConfigId='+runConfigId+'&type=map&workpackageId='+workpackageId+'&featureId='+featureId+'&testSuiteId=-1&testcaseId=-1&sourceType=Feature';
	 }
	 if(status==0){
		 
	        urlMapping = 'administration.testBed.plan?runConfigId='+runConfigId+'&type=unmap&workpackageId='+workpackageId+'&featureId='+featureId+'&testSuiteId=-1&testcaseId=-1&sourceType=Feature';
	 }
	 $.ajax({
	        type: "POST",
	  contentType: "application/json; charset=utf-8",
	        url : urlMapping,
	        dataType : 'json',
	        success : function(data) {
	               if(data.Result=="ERROR"){
	                     callAlert(data.Message);
	               return false;
	        }
	              
	        }
	 }); 
}*///END - Delete - JTable

/*//BEGIN - Delete - JTable
function testBedTestSuiteMap(status,runConfigId,testsuiteId,workpackageId){
	if(status==1){
        urlMapping = 'administration.testBed.plan?runConfigId='+runConfigId+'&type=map&workpackageId='+workpackageId+'&featureId=-1&testSuiteId='+testsuiteId+'&testcaseId=-1&sourceType=TestSuite';
	 }
	 if(status==0){
		 
	        urlMapping = 'administration.testBed.plan?runConfigId='+runConfigId+'&type=unmap&workpackageId='+workpackageId+'&featureId=-1&testSuiteId='+testsuiteId+'&testcaseId=-1&sourceType=TestSuite';
	 }
	 $.ajax({
	        type: "POST",
	  contentType: "application/json; charset=utf-8",
	        url : urlMapping,
	        dataType : 'json',
	        success : function(data) {
	               if(data.Result=="ERROR"){
	                     callAlert(data.Message);
	               return false;
	        }
	              
	        }
	 }); 
}*///END - Delete - JTable

/*BEGIN - Delete - JTable
function callJTableFunFeature(){
	var hideornot = true;
	var wpexecType = document.getElementById("wptype").value;
	var wpIdofexeType = document.getElementById("wptypewpId").value;
	if(wpIdofexeType != workPackageId){//Verifying if diff WP was clicked in Tree, other than the existing one.
		getWPTypeFromTRP(workPackageId); //FetchWPTRPType
	}
	var fields = {};
	//Add the static fields
	fields['id'] = {key: true,list:false,edit:false};
	fields['featureId'] =  {title: 'Feature ID',list:false,edit: false,width:"20%"};
	fields['productFeatureCode'] = {title: 'Feature Code', list:true,edit: false,width:"20%"};
	fields['featureName'] = {title: 'Feature ',edit: false,width:"20%"};
	fields['featureDesc'] = {title: 'Description ',edit: false,width:"20%"};
	

	fields['workPackageId'] = {title: 'work Package ID',list:false,edit: false,width:"20%"};
	fields['workPackageName'] = {title: 'Work Package Name',list:false,edit: false,width:"20%"};
	
	fields['isSelected'] = {title: 'Selected',type : 'checkbox',width:"20%",values: {'1' : 'Yes','0' : 'No'}};
	fields['createdDate'] = {title: 'Created Date',list:false,edit: false,width:"20%"};
	fields['editedDate'] = {title: 'Edited Date',list:false,edit:false,width:"20%"};
	fields['status'] = {title: 'Status',list:false,width:"20%"};
	//testbed list starts
	fields['testBedsMapped']={
			 title:'Mapped TestBeds',
		        width: "2%",
		        create:false,
		        edit:true,
		        display: function (childData) { 
    				//Create an image that will be used to open child table 
    				var $img = $('<img src="css/images/list_metro.png" title="Mapped TestBeds" />'); 
		        	//Open child table when user clicks the image 
		        	$img.click(function (data) { 
		        		
		        		// ----- Closing child table on the same icon click -----
						closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerPlanFeature"));
						if(closeChildTableFlag){
							return;
						}
		        		
			        	$('#jTableContainerPlanFeature').jtable('openChildTable', 
			        	$img.closest('tr'), 
			        	{ 
			        	title: 'Mapped TestBeds',
			        	paging: true, //Enable paging
			            pageSize: 10, //Set page size (default: 10)
			            selecting: true, //Enable selecting 
			            editinline:{enable:false},					        	  	
			        	actions: { 
			        		 listAction: 'feature.testbeds.list?featureId='+childData.record.featureId+'&workpackageId='+workPackageId,
			        		// editinlineAction: 'feature.testbeds.update',
			        	     recordUpdated:function(event, data){
			        	        	$('#jTableContainerPlanFeature').find('.jtable-main-container').jtable('reload');
			        	        },
			        	     recordAdded: function (event, data){
			        	         	$('#jTableContainerPlanFeature').find('.jtable-main-container').jtable('reload');
			        	        },
			            },
			        	fields: {		      
			        		runconfigId:{
					 	        	type: 'hidden',  
					    				list:false,
					    				key: true
					 	       		},
					 	       		environmentcombinationId: { 
					        			title: 'environmentcombinationId',
					         		create:false,
					         		edit: false,
					         		list:false,
					        			},        	
					        			environmentcombinationName: {
					                 title: 'Environment Combination ',
					                 width: "12%",
					                 create : true,
					                 edit :false,
					                 list:true
					         	},
					         	deviceId:{
					         		title: 'Device Id', 
					         		width: "7%",
					         		type : 'hidden',
					                 create: false,
					                 edit : false,
					                 list : false,
					         	},
					         	deviceName:{
					         		title: 'Device ', 
					         		width: "7%",                         
					                 create: false,
					                 list: true,
					                 edit:false
					         	},
					         	hostIpAddress:{
					         		title: 'Host', 
					         	    width: "40%",                         
					                 create: false,
					                 edit:false,
					                 list:true
					         	},
					         	status:{
					         		title: 'Status', 
					         	    width: "40%",                         
					                 create: false,
					                 edit:true,
					                 type : 'checkbox',
					                 values: {'1' : 'Yes','0' : 'No'},
					                 display: function (data) 
					                 {
					                 	if(childData.record.isSelected==0){
	                                         return '<input type="checkbox" id="status" checked disabled value='+data.record.status+'>';

					                 	}else{
					                	  if(data.record.status=="1")
			                                 return '<input type="checkbox" id="status" checked onclick=testBedFeatureMap(0,'+data.record.runconfigId+','+childData.record.featureId+','+workPackageId+'); value='+data.record.status+'>';
					                		else 
	                                         return '<input type="checkbox" id="status" onclick=testBedFeatureMap(1,'+data.record.runconfigId+','+childData.record.featureId+','+workPackageId+'); value='+data.record.status+'>';
					                 	}
					      			  },
					         	},
					         	hostId:{
					         		title: 'hostId', 
					         		width: "20%",                         
					                 create: false,
					                 list: false,
					                 edit: false
					         	},
					             
						    	 },
				    	 // This is for closing $img.click(function (data) { 
						 //Moved Formcreate validation to Form Submitting
			         	 //Validate form when it is being submitted
			       		
					}, 
     			function (data) { //opened handler 
		        	data.childTable.jtable('load'); 
		        	}); 
	        	}); 
	        		return $img; 
	        	}	
			};	
	
	//testbed list ends

	fields['testCaseIdMapping'] = {
        	title: 'View Test Case',
        	width: "5%",
        	edit: true,
        	create: false,
   	 	display: function (testCaseData) { 
   		//Create an image that will be used to open child table 
   		 //  Abort_completed functionality for view Testcases Start . 
   			var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible;" title="View TestCase" />'); 
   			//  Abort_completed functionality for view Testcases End . 
   			//Open child table when user clicks the image 
   			$img.click(function () { 
   				
   				// ----- Closing child table on the same icon click -----
				closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerPlanFeature"));
				if(closeChildTableFlag){
					return;
				}
   				
	        	$('#jTableContainerPlanFeature').jtable('openChildTable', 
	        	$img.closest('tr'), 
	        	{ 
	        	title: 'Mapped TestCases',
	        	paging: true, //Enable paging
	            pageSize: 10, //Set page size (default: 10)
	            selecting: true, //Enable selecting 
	            editinline:{enable:false},					        	  	
	        	actions: { 
	        		 //listAction: urlToGetTestCasesOfSpecifiedProductId,	      
	        		 listAction: 'product.feature.testcase.mappedlist?productFeatureId='+testCaseData.record.featureId,
	        		// createAction: 'product.testcase.add',
	        		// editinlineAction: 'product.testcase.update',
	        	     recordUpdated:function(event, data){
	        	        	$('#jTableContainerPlanFeature').find('.jtable-main-container').jtable('reload');
	        	        },
	        	     recordAdded: function (event, data){
	        	         	$('#jTableContainerPlanFeature').find('.jtable-main-container').jtable('reload');
	        	        },
	            },
	        	fields: {		      
		   	         productId:{
			 	        	type: 'hidden',  
			    				list:false
			    				//defaultValue: productId
			 	       			},
			             testCaseId: { 
			        			title: 'Test Case ID',
			         		create:false,
			         		edit: false,
			         		list:true,
			         		key: true
			        			},        	
			         	testCaseName: {
			                 title: 'Test Case Name ',
			                 inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
			                 width: "12%",
			                 create : false,
			                 edit :false
			         	},
			         	testSuiteId:{
			         		title: 'Test Suite', 
			         		width: "7%",
			         		type : 'hidden',
			                 create: false,
			                 edit : false,
			                 list : false,
			             //    options: 'common.list.testsuite.byproductId?productId='+ productId,              
			         	},
			         	testSuiteName:{
			         		title: 'TestSuite Name', 
			         		width: "7%",                         
			                 create: false,
			                 list: false
			         	},
			         	testCaseDescription:{
			         		title: 'Test Case Description', 
			         		inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
			         	    width: "40%",                         
			                 create: false   
			         	},
			         	testCaseSource:{
			         		title: 'Test Case Source', 
			         		width: "20%",                         
			                 create: false,
			                 list: false,
			                 edit: false
			         	},
			         	testCaseCode: { 
			         		title: 'Test Case Code',  
			         		inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
			         		width: "10%",                        
			                 create: false
			         	},
			         					         	
			         	testCaseScriptQualifiedName:{
			         		title: 'Script Package Name',  
			         		width: "10%",
			         		create: false,
			         		list : true, 
			         		edit : false
			         	},
			         	testCaseScriptFileName:{
			         		title: 'Script File Name',  
			         		width: "10%",
			         		create: false,
			         		list : true, 
			         		edit : false
			         	}, 
			         	executionTypeId:{
			         		title : 'Execution Type',
			         		width : "10%",
			         		create : false,
			         		list : true,
			         		edit : false,
			       			options:function(data){
			       				if(data.source =='list'){	      				
			       					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
			       				}else if(data.source == 'create'){	      				
			       					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
			       				}
			       			}
			         	}, 
			         	 testcasePriorityId:{
			 	        		title : 'Priority',
			 	        		width : "10%",
			 	        		create : true,
			 	        		list : true,
			 	        		edit : false,
			 	      			options:function(data){
			 	      				if(data.source =='list'){	      				
			 	      					return 'common.list.testcasepriority';	
			 	      				}else if(data.source == 'create'){	      				
			 	      					return 'common.list.testcasepriority';
			 	      				}
			 	      			}
			 	        	},
			 	        	testcaseTypeId:{
			 	        		title : 'Test Case Type',
			 	        		width : "13%",
			 	        		create : true,
			 	        		list : true,
			 	        		edit : false,
			 	      			options:function(data){
			 	      				if(data.source =='list'){	      				
			 	      					return 'common.list.testcasetype';	
			 	      				}else if(data.source == 'create'){	      				
			 	      					return 'common.list.testcasetype';
			 	      				}
			 	      			}
			 	        	},
			 	        	productFeatureId: {
			 		             title: 'Features',
			 		             width:"15%",
			 		             list:true,
			 		             edit: false,
			 		             options:function(data){		      				
			 		            	 if(data.source =='list'){	      				
			 		      					return 'product.feature.list?productId='+data.record.productId;	
			 		      				}else if(data.source == 'create'){	      				
			 		      					return 'product.feature.list?productId='+data.record.productId;
			 		      				}
			 		      			}		            
			 		        },				         
				    	 },
		    	 // This is for closing $img.click(function (data) { 
				 //Moved Formcreate validation to Form Submitting
	         	 //Validate form when it is being submitted
	       		 formSubmitting: function (event, data) {
	        	  data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
	              data.form.find('input[name="testCaseDescription"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
	              data.form.find('input[name="testCaseCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');
	              data.form.validationEngine();
	             return data.form.validationEngine('validate');
	            }, 
	            //Dispose validation logic when form is closed
	           formClosed: function (event, data) {
	             data.form.validationEngine('hide');
	             data.form.validationEngine('detach');
	           }
			}, 
			function (data) { //opened handler 
        	data.childTable.jtable('load'); 
        	}); 
    	}); 
	        	return $img; 
   	}           
       	 };
       	 
       	 
	//Destroy the current jtable so that it is forced to reload with the new workpackage id
	try{
		if ($('#jTableContainerPlanFeature').length>0) {
			 $('#jTableContainerPlanFeature').jtable('destroy'); 
		}
	} catch(e) {}
	 $('#jTableContainerPlanFeature').jtable({
         
         title: 'Select Feature for Work Package',
         selecting: true,  //Enable selecting 
         
         paging: true, //Enable paging
         pageSize: 10, 
         openChildAsAccordion: true,
         editinline:{enable:true},
		 multiselect: true, //Allow multiple selecting
         selectingCheckboxes: true, //Show checkboxes on first column
         toolbar : {
			items : [ {
				text : 'Import Selected Features',
				click : function() {
					
					
				}
			} ]
		},
         actions: {
        	 listAction : urlToGetWorkPackagesFeaturesOfSpecifiedWorkPackage,
        	 
        	 editinlineAction: 'workpackage.feature.update'
            },
        
         fields : fields,
         recordsLoaded: function(event, data) {
    		 var $selectedRowsDefine = $('#jTableContainerPlanFeature').jtable('selectedRows');
    		 if($selectedRowsDefine >=0)
    		 {
    			 $("#jTableContainerPlanFeature").find(".jtable-selecting-column > input").prop("checked", true);
    		 }
			 var wpexecType = document.getElementById("wptype").value;
			 var wpIdofexeType = document.getElementById("wptypewpId").value;
			 if(wpexecType  == 3){				
				var ary = (data.records);
				if(ary.length == 0){ 
					$('#Plan').find('[data-name="Feature"]').hide();
					$('#Plan').find('#jTableContainerPlanFeature').hide();	
					$('#Plan').find('[data-name="TestSuite"]').addClass('active');					
					$(".jtbSuite").show();	
					defineTestSuite(nodeType);					
				}else{
					$('#Plan').find('[data-name="Feature"]').show();
					$('#Plan').find('#jTableContainerPlanFeature').show();
					$(".jtbCase, .jtbSuite").hide();					
					$('#Plan').find('[data-name="TestSuite"]').removeClass('active');						
					$('#Plan').find('[data-name="TestCase"]').removeClass('active');
					//Make Feature tab uneditable
				}
		 //Hiding Feature Tab button itself
				//$('#Plan').find('#giveId')
				//$('#Plan').find('[data-name="Feature"]').hide();
				//To Hide Entire Jtable with class
				//$('#Plan .jtbFeature').show();
				//To Hide Entire Jtable with id
				//$('#Plan').find('#jTableContainerPlanFeature').hide();
			 }else{
				$('#Plan').find('[data-name="Feature"]').show();
				$('#Plan').find('#jTableContainerPlanFeature').show();	
				$(".jtbCase, .jtbSuite").hide();		
				$('#Plan').find('[data-name="TestSuite"]').removeClass('active');
				$('#Plan').find('[data-name="TestCase"]').removeClass('active');
			 }
    	 }
		});
	 
	 $('#jTableContainerPlanFeature').jtable('load');
	 
	 var jscrolheight = $("#jTableContainerPlanFeature").height();
	 var jscrolwidth = $("#jTableContainerPlanFeature").width();
	  
	 $(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
	
		 if (lastScrollLeft < documentScrollLeft) {
		    	$("#jTableContainerPlanFeature").width($(".jtable").width()).height($(".jtable").height());			
		        lastScrollLeft = documentScrollLeft;
		    }else if(lastScrollLeft >= documentScrollLeft){			
		    	$("#jTableContainerPlanFeature").width(jscrolwidth).height(jscrolheight);
		    }			
	 });
}*///END - Delete - JTable
//Feature Ends

var SOURCE;
var treeData;
var workPackageId = "";
var workPackageName="";
var environmentId = "";
var urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = "";

/* Pop Up close function */
function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
		
	var selectedTab = $("#tablistWP").find("li.active").index();		
	var workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	
	if(reloadJTableAllocateFlag)
		tabSelection(selectedTab,workPackageId);	
}

/* common functionlaity for tabselection and tree node selection started */
// 1. selectedTab=0 -> Work Package Summary.
// 2. selectedTab=1 -> Work Package Plan.
// 3. selectedTab=2 -> Work Package Allocate Test case.
// 4. selectedTab=3 -> Raise Resource Demand.
// 5. selectedTab=4 -> Reserve Resources.
// 6. selectedTab=5 -> Results Summary.
// 7. selectedTab=6 -> TestCase Results.
// 8. selectedTab=7 -> Defects.

function tabSelection(selectedTab,workPackageId){
	openLoaderIcon();
	$('#toAnimate .portlet .actions').eq(0).css('display','none');
	utilizationWeekRange = '';
	if (selectedTab==0){
		document.getElementById("accordionWD").style.display = "none";
		urlToGetWorkPackageDetailsOfSelectedWorkPackageId='workpackage.testcase.execution.summary.list?workPackageId='+workPackageId;				
		
		workPackageSummary(urlToGetWorkPackageDetailsOfSelectedWorkPackageId, workPackageId, nodeType);
	  
		}else if (selectedTab==1){			 
			var subTabActive = $("#giveId").find("label.active").attr("data-name");						
			if(subTabActive == "Feature") {
				$(".jtbFeature").show();
				$(".jtbCase, .jtbSuite").hide();
				defineFeatures(nodeType);
			}else if(subTabActive == "TestCase") {
				$(".jtbCase").show();
				$(".jtbFeature, .jtbSuite").hide();
				defineTestcases(nodeType);
			}else {
				$(".jtbSuite").show();
				$(".jtbCase, .jtbFeature").hide();
				defineTestSuite(nodeType);
			}
			document.getElementById("accordionWD").style.display = "none";
			closeLoaderIcon();
		}else if(selectedTab==2){
			allocateTestCase(nodeType);
			document.getElementById("accordionWD").style.display = "none";
	
		}else if (selectedTab==3){
			//operationYear = new Date().getFullYear();
			getWeeksName();
			resourceDemand(nodeType);
			document.getElementById("accordionWD").style.display = "none";
	
		}else if (selectedTab==4){
			//operationYear = new Date().getFullYear();
			//getWeeksName();
			reserveResource(nodeType);
			document.getElementById("accordionWD").style.display = "none";
	
		}else if (selectedTab==5){	
			openLoaderIcon();
			 if(nodeType !='WorkPackage'){
				 	setWorkPackageNode();
			 }
			
			 url='workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+-1+"&testcaseId=0"; 
			 listWorkPackageTestCaseSummary(url);			 
			 setTimeout(function (){handleResultsRadios($("#resultRadios").find("label.active").index()+1);},500);
			  
			 document.getElementById("accordionWD").style.display = "none";
		}
		else if (selectedTab==6){
			listTCExecutionResults(workPackageId, nodeType);
			document.getElementById("accordionWD").style.display = "none";
			
			$('#toAnimate .portlet .actions').eq(0).css('display','block');		
		}else if (selectedTab==7){
		loadDefectInfo(workPackageId, nodeType);
		document.getElementById("accordionWD").style.display = "none";
		closeLoaderIcon();
		//$('#toAnimate .portlet .actions').eq(0).css('display','block');
		}
		else if (selectedTab==8){
			operationYear = new Date().getFullYear();
			showTeamUtilization('TeamUtilization', 0, 0, 0, prodId, workPackageId, true, '150px',0);
			document.getElementById("accordionWD").style.display = "none";
			closeLoaderIcon();
			//$('#toAnimate .portlet .actions').eq(0).css('display','block');
		}
	
		 /* Jtable disable for Abort_completed start  */
		 if(editableFlag){
			 $(".jtable").removeClass("pointersnone");
		 }else{
			 $(".jtable").addClass("pointersnone"); 			
		 }
		 /* Jtable disable for Abort_completed End  */	
		 
		 if(editableFlag){
    	$("#plan_defaultrange>span>button").removeAttr("disabled","disabled");
    	$("#actual_defaultrange>span>button").removeAttr("disabled","disabled");
    	$("#saveSelected").removeAttr("disabled");
	 }else {
		$("#plan_defaultrange>span>button").attr("disabled","disabled");
		$("#actual_defaultrange>span>button").attr("disabled","disabled");
		$("#saveSelected").attr("disabled","disabled");
	}
} 
/* common functionlaity for tabselection and tree node selection ended */

function popupCloseFilter() {
	$("#div_PopupMainUserProfile").fadeOut("normal");
	$("#div_PopupMainFilter").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

function popupCloseUserProfile() 
{
	$("#div_PopupMainUserProfile").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {	
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

function popupDevicesClose() {	
	$("#div_PopupDevices").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function listTestcasesResultsOfSelectedWorkPackageGrid(workPackageId,url){	
	document.getElementById("ResultsGridView").style.display = "block";
	 document.getElementById("Results").style.display = "none"; 
	 document.getElementById("workpackageTCER").style.display = "none"; 

	 document.getElementById("workpackageTCERGrid").style.display = "block"; 

	 if(workPackageId==null || workPackageId==''){
			workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
			url='workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+sortIds+"&testcaseId=0&jtStartIndex=0&jtPageSize=0";
	}
	 //var urlEnv='testCase.configuration.listBywpId?workPackageId='+workPackageId;
	 var urlEnv='environmentCombination.workpackage?workPackageId='+workPackageId;
	 var content = '<table width="620" cellspacing="1" cellpadding="2" border="0" id="ex1">  <thead><tr>';
	 $.post(urlEnv,function(data)
	{
		 var ary = (data.Records);
		 if(ary.length > 0){ 
			 content += '<th  bgcolor="#cccccc" align="center" class="textSm primary">Test Case</th>';
		        $.each(ary, function(index, element) {
		        	var testcase=element.testcaseId;
		        	 content += '<th width="24" nowrap="nowrap" bgcolor="#cccccc" align="center" class="textSm primary"><b>' + element.environmentCombinationName + '</b></th>';
				}); 
		        content += "</tr></thead><tbody>";       		
		 }  
		 else{
			 jAlert("No Test case available","ok");
		 }
				 
	});
	 
	 $.post(url,function(data)
	{		 
		 var ary = (data.Records);
		 if(ary.length > 0){ 
		        $.each(ary, function(index, element) {
		        	var testcase=element.testcaseId;
		        	 content += '<tr><td><table cellspacing="0" cellpadding="0" border="0"><tbody><tr><td nowrap="" align="left">' 
		        	
		        	 content += element.testcaseId + '</td></tr></tbody></table></td>';
		        	 
		        	 content += '<td align="center">158</td>';
		        	 content += '<td align="center">158</td>';
		        	 content += '<td align="center">158</td>';
		        	 content += '<td align="center">158</td>';
		        	 content += '</tr>';
				}); 
		        content += "</tbody>";
		        content += "</table>";
		        		 $('#workpackageTCERGrid').append(content);
		 }
		 else{
			 jAlert("No Test case available","ok");
		 }				 
	});	 

	 $("#ex1").tablesorter();

	 function sortwithcolor( column ) {
	 	$("#ex1 > tbody > tr").heatcolor(
	 		function() { return $("td:nth-child(" + column + ")", this).text(); }
	 	);
	 };

	 $("th").click(function() {
	 	$(this).siblings().css("background-color","#cccccc").end().css("background-color","#dd0000");
	 	sortwithcolor( $(this).parent().children().index( this ) + 1 );
	 });
		sortwithcolor(8);
}

function downloadReport(testRunJobId){
	// var url="D:\\TAF\\Evidence_Collected\\report\\"+testRunJobId+"\\report.html";
	var url="\\"+testRunJobId+"\\REPORT\\report.html";
	    //alert(url);
		var urlfinal="rest/download/evidenceReport?fileName="+url;
	  	parent.window.location.href=urlfinal;
	  	//D:\Program Files\Tomcat-7.0.42\bin\Evidence\\579\2015-10-19\testrunJobWp-6905-4147-144043.gif
}
function exportResults(testRunJobId){
	 var fileLocation="";
 	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : 'test.export.testresults.testrunjob.rest?testRunJobId='+testRunJobId+"&fileLocation="+fileLocation,
         dataType : 'json',
         success : function(data) {
                if(data.Result=="ERROR"){
                      callAlert(data.Message);
                return false;
		         }else{
		        	 
		        	 if(data.Result=="Ok"){
		        		 callAlert("Export result Completed.");
		        	 }else{
		        		 callAlert(data.Message);
		        	 }
	         	}               
         }
  });
	
}




function exportResultstestrunJobBulk(bool){
	if(bool == 0) {
		$("#div_PopupExportResTestrunJob").modal("hide");
		return false;
	}
	var testRunJobId=testRunJobIDForWorkPackageResult;
	 var checkboxValues = [];
	 $("#div_PopupExportResTestrunJob input:checked").each(function(){ 
	   	checkboxValues.push($(this).attr('id'));
	 });
	 console.log("checkboxValues :"+checkboxValues);
	 if($('#div_PopupExportResTestrunJob input').length>0 && !$('#div_PopupExportResTestrunJob input').is(':checked')){
		callAlert("Plese select the Test Management System");
		return false;
	}
	 var fileLocation="";
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : 'test.export.testresults.testrunjob.rest?testRunJobId='+testRunJobId+'&tmsIdValues='+checkboxValues+'&fileLocation='+fileLocation,
        dataType : 'json',
        success : function(data) {
               if(data.Result=="ERROR"){
                     callAlert(data.Message);
               return false;
		         }else{
		        	 if(data.Result=="SUCCESS"){
		        		 callAlert("Export result Completed.");
		        		 $("#div_PopupExportResTestrunJob").modal("hide");
		        	 }else{
		        		 callAlert(data.Message);
		        		 $("#div_PopupExportResTestrunJob").modal("hide");
		        	 }
	         	}               
        }
 });
	
}



function exportResultsBulk(bool){
	if(bool == 0) {
		$("#div_PopupExportRes1").modal("hide");
		return false;
	}

 	workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	//var tmsId= $("#expRes_types").find('option:selected').attr('id');
	// var tmsId = $("input[name=radio1]:checked").attr("id");
	 
	var checkboxValues = [];
	$("#div_PopupExportRes1 input:checked").each(function(){ 
	  	checkboxValues.push($(this).attr('id'));
	});
	console.log("checkboxValues :"+checkboxValues);
	if($('#div_PopupExportRes1 input').length>0 && !$('#div_PopupExportRes1 input').is(':checked')){
		callAlert("Plese select the Test Management System");
		return false;
	}
				 
 	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : 'test.export.testresults.rest?workpackageId='+workpackageId+'&tmsIdValues='+checkboxValues,
         dataType : 'json',
         success : function(data) {
                if(data.Result=="ERROR"){
                      callAlert(data.Message);
               		 return false;
		         }else{
		        	 if(data.Result=="Ok"){
		        		 callAlert("Export result Completed.");
		        		 $("#div_PopupExportRes1").modal("hide");
		        	 }
		        	 else{
		        		 callAlert(data.Message);
		        		 $("#div_PopupExportRes1").modal("hide");
		        	 }
	         	}
               
         }
  });
}


  function exportDefectsBulk(bool){
	
	  if(bool == 0) {
			$("#div_PopupExportDef1").modal("hide");
			return false;
		}

	 	workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
		 var checkboxValues = [];
		 $("#div_PopupExportDef1 input:checked").each(function(){ 
		   	checkboxValues.push($(this).attr('id'));
		 });
		 console.log("checkboxValues :"+checkboxValues);
	  $.ajax({
	         type: "POST",
	   		contentType: "application/json; charset=utf-8",
	         url : 'test.defects.export.workpackage?workpackageId='+workpackageId+'&dmsIdValues='+checkboxValues,
	         dataType : 'json',
	         success : function(data) {
	                if(data.Result=="ERROR"){
	                      callAlert(data.Message);
	                      $("#div_PopupExportDef1").modal("hide");
	                      return false;
			         }else{
			        	 if(data.Result=="SUCCESS"){
			        		 callAlert("Export result Completed.");
		         		}
	       		  }
	  		}
	  });
  }

  function exportOneDefectBulk(bool){
	var tebId=testExecutionResultBugIdForExport;
		if(bool == 0) {
				$("#div_PopupExportOneDef").modal("hide");
				return false;
			}

		 	workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
			 var checkboxValues = [];
			 $("#div_PopupExportOneDef input:checked").each(function(){ 
			   	checkboxValues.push($(this).attr('id'));
			 });
			 console.log("checkboxValues :"+checkboxValues);
			 if($('#div_PopupExportOneDef input').length>0 && !$('#div_PopupExportOneDef input').is(':checked')){
				 callAlert("Plese select the Defect Management System");
				 return false;
			 }
				 
	    $("#loadingIconExportDefect").show();	 
		$.ajax({
	         type: "POST",
	   		contentType: "application/json; charset=utf-8",
	         url : 'export.defect.bugId?tebId='+tebId+'&dmsIdValues='+checkboxValues,
	         dataType : 'json',
	         success : function(data) {
	        	 $("#loadingIconExportDefect").hide();
	        	 
                if(data.Result=="ERROR" || data.Result=="Error"){
                      callAlert(data.Message);
                      $("#div_PopupExportOneDef").modal("hide");
                      return false;
		         }else{
		        	 if(data.Result=="Ok"){
		        		 callAlert(data.Message);			        		 
		        	 }
	         	}
                $('#div_PopupExportOneDef').modal("hide");
	         },error : function(data){
	        	 $("#loadingIconExportDefect").hide();
	         }, complete : function(data){
	        	 $("#loadingIconExportDefect").hide();
	         }
	  });
	}
function planningStatus(){	
	$("#datepicker_popup").datepicker('setDate','today');
	$("#psTestSuite, #psFeature").empty();
	var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	//planing status workpakage Name 
	var WorkPackageName=document.getElementById("cworkPackageName").value;
	$("#popUpPlanningStatus").find("h4").text("PlanningStatus For Workpackage:-"+ WorkPackageName );
	//planing status workpakage Name END
	var urlToGetWorkPackageTestcasePlanStatus = "workPackage.plan.status?workPackageId="+workPacakgeId;
 	try{
		if ($('#psTestCase').length>0) {
			 $('#psTestCase').jtable('destroy'); 
		}
	} catch(e) {}
	$('#psTestCase').jtable({
		title: 'Workpackage Execution Plan Status',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetWorkPackageTestcasePlanStatus,
        },  
        fields : {
        	workPackageStatusSummaryId: {
	                 key: true,
	                 list: false,
	                 create: false
	             },
	             workPackageId:{
	                 title: 'Workpackage Id',
	            	 list:false,
	                 width:"20%"
	             },
	             workPackageName:{
	                 title: 'Workpackage Name',
	            	 list:true,
	                 width:"20%"
	             },
	             totalFeatureCount:{
	                 title: 'Total Feature Count',
	            	 list:true,
	                 width:"20%"
	             },
	             totalTestSuiteCount:{
	                 title: 'Total TestSuite Count',
	            	 list:true,
	                 width:"20%"
	             },
				 activeTestCaseCount:{
	                 title: 'Active TestCase Count',
	            	 list:true,
	                 width:"20%"
	             },
				 inActiveTestCaseCount:{
	                 title: 'InActive TestCase Count',
	            	 list:true,
	                 width:"20%"
	             },
	             totalTestCaseCount:{
	                 title: 'Total TestCase Count',
	            	 list:true,
	                 width:"20%"
	             },
	             assignedTestLeadCountwithPercentage: {
	                 title: 'Assigned to Test Lead',
	                 list:true,
	                 width:"20%"
	             },
	             assignedTesterCountwithPercentage: {
	                 title: 'Assigned to Tester',
	                 list:true,
	                 width:"20%"
	             },
	             plannedExecutionDateCountwithPercentage: {
	                 title: 'Execution Date Planned',
	                 list:true,
	                 width:"20%"
	             },
        	},  
		});
	$('#psTestCase').jtable('load');
	$('#popUpPlanningStatus').modal();
   
}

function planningStatusFeature(){	
	$("#psTestSuite, #psTestCase").empty();
	var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	//planing status workpakage Name 
	var WorkPackageName=document.getElementById("cworkPackageName").value;
	$("#popUpPlanningStatus").find("h4").text("PlanningStatus For Workpackage:-"+ WorkPackageName );
	//planing status workpakage Name END
	var urlToGetWorkPackageTestcasePlanStatus = "workPackage.planfeature.status?workPackageId="+workPacakgeId;
 	try{
		if ($('#psFeature').length>0) {
			 $('#psFeature').jtable('destroy'); 
		}
	} catch(e) {}
	$('#psFeature').jtable({
		title: 'Workpackage Feature Execution Plan Status',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetWorkPackageTestcasePlanStatus,
        },  
        fields : {
        	workPackageStatusSummaryId: {
	                 key: true,
	                 list: false,
	                 create: false
	             },
	             workPackageId:{
	                 title: 'Workpackage Id',
	            	 list:false,
	                 width:"20%"
	             },
	             workPackageName:{
	                 title: 'Workpackage Name',
	            	 list:true,
	                 width:"20%"
	             },
	             totalFeatureCount:{
	                 title: 'Total Feature Count',
	            	 list:true,
	                 width:"20%"
	             },
	             totalTestCaseCount:{
	                 title: 'Total TestCase Count',
	            	 list:true,
	                 width:"20%"
	             },
	             assignedTestLeadCountwithPercentage: {
	                 title: 'Assigned to Test Lead',
	                 list:true,
	                 width:"20%"
	             },
	             assignedTesterCountwithPercentage: {
	                 title: 'Assigned to Tester',
	                 list:true,
	                 width:"20%"
	             },
	             plannedExecutionDateCountwithPercentage: {
	                 title: 'Execution Date Planned',
	                 list:true,
	                 width:"20%"
	             },
        	},  
		});
	$('#psFeature').jtable('load');
	$('#popUpPlanningStatus').modal();   	
}

function planningStatusTestSuite(){	
	$("#psFeature, #psTestCase").empty();
	var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	//planing status workpakage Name 
	var WorkPackageName=document.getElementById("cworkPackageName").value;
	$("#popUpPlanningStatus").find("h4").text("PlanningStatus For Workpackage:-"+ WorkPackageName );
	//planing status workpakage Name END
	var urlToGetWorkPackageTestcasePlanStatus = "workPackage.plantestsuite.status?workPackageId="+workPacakgeId;
 	try{
		if ($('#psTestSuite').length>0) {
			 $('#psTestSuite').jtable('destroy'); 
		}
	} catch(e) {}
	$('#psTestSuite').jtable({
		title: 'Workpackage TestSuite Execution Plan Status',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetWorkPackageTestcasePlanStatus,
        },  
        fields : {
        	workPackageStatusSummaryId: {
	                 key: true,
	                 list: false,
	                 create: false
	             },
	             workPackageId:{
	                 title: 'Workpackage Id',
	            	 list:false,
	                 width:"20%"
	             },
	             workPackageName:{
	                 title: 'Workpackage Name',
	            	 list:true,
	                 width:"20%"
	             },
	             totalTestSuiteCount:{
	                 title: 'Total TestSuite Count',
	            	 list:true,
	                 width:"20%"
	             },
	             totalTestCaseCount:{
	                 title: 'Total TestCase Count',
	            	 list:true,
	                 width:"20%"
	             },
	             assignedTestLeadCountwithPercentage: {
	                 title: 'Assigned to Test Lead',
	                 list:true,
	                 width:"20%"
	             },
	             assignedTesterCountwithPercentage: {
	                 title: 'Assigned to Tester',
	                 list:true,
	                 width:"20%"
	             },
	             plannedExecutionDateCountwithPercentage: {
	                 title: 'Execution Date Planned',
	                 list:true,
	                 width:"20%"
	             },
        	},  
		});
	$('#psTestSuite').jtable('load');
	$('#popUpPlanningStatus').modal();
		
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMainFilter").style.display == 'block') {
			popupClose();
		}
	}
};

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMainUserProfile").style.display == 'block') {
			popupClose();
		}
	}
};

var popupTitle_TestCase ="Test Cases";
var popupTitle_TestSuite ="View TestCase";
var popupTitle_TestSteps ="Test Steps";
var popupTitle_RaiseResourceDemandFor ="Raise Resource Demand for";
var popupTitle_RaiseResourceDemand ="Raise Resource Demand";
var popupTitle_AvailableResource="Available Resources For Reservation";
var popupTitle_EnvironmentCombination ="Environment Combinations";

var reloadJTableAllocateFlag=false;
function divPopupMainTitle(name){
	$("#div_PopupMain_Allocate").find('h4').text("");
	$("#div_PopupMain_Allocate").find('h4').text(name);
	$("#div_PopupMain_Allocate").modal();
	showEnvironmentCombinationTable(1);
	$("#JTable_Allocate").html('');
	reloadJTableAllocateFlag=false;
	
	if($("#tcRadioGrp .btn-group .btn").eq(0).hasClass('active')){
		$("#demandHeader").show();
		reloadJTableAllocateFlag=true;
	}else{
		$("#demandHeader").hide();
	}
}

function divPopupMainTitleEnv(name){
	$("#div_PopupMain_Allocate_Env").find('h4').text("");
	$("#div_PopupMain_Allocate_Env").find('h4').text(name);
	$("#div_PopupMain_Allocate_Env").modal();
	
	$("#JTable_Allocate").html('');
	reloadJTableAllocateFlag=false;
	
	if($("#tcRadioGrp .btn-group .btn").eq(0).hasClass('active')){
		$("#demandHeader").show();
		reloadJTableAllocateFlag=true;
	}else{
		$("#demandHeader").hide();
	}
}
function popupClose_Allocate(){
	$("#div_PopupMain_Allocate").fadeOut("normal");
	var length = popupTitle_RaiseResourceDemandFor;
	
	var popup_title = $("#div_PopupMain_Allocate").find('h4').text();
	if(popup_title == popupTitle_RaiseResourceDemand || popup_title == popupTitle_AvailableResource || popup_title == popupTitle_EnvironmentCombination){
		popupClose();
	
	}else if(popup_title.substr(0,popupTitle_RaiseResourceDemandFor.length) == popupTitle_RaiseResourceDemandFor){
		popupClose();
	}
	
	if($("#tcRadioGrp .btn-group .btn").eq(0).hasClass('active')){
		$("#resourceReservationAndDemandContainer").show();
	}else{
		$("#resourceReservationAndDemandContainer").hide();
	}
}


function popupClose_AllocateEnv(){
	$("#div_PopupMain_Allocate_Env").fadeOut("normal");
	var length = popupTitle_RaiseResourceDemandFor;
	
	var popup_title = $("#div_PopupMain_Allocate_Env").find('h4').text();
	if(popup_title == popupTitle_RaiseResourceDemand || popup_title == popupTitle_AvailableResource || popup_title == popupTitle_EnvironmentCombination){
		popupClose();
	
	}else if(popup_title.substr(0,popupTitle_RaiseResourceDemandFor.length) == popupTitle_RaiseResourceDemandFor){
		popupClose();
	}
	
	if($("#tcRadioGrp .btn-group .btn").eq(0).hasClass('active')){
		$("#resourceReservationAndDemandContainer").show();
	}else{
		$("#resourceReservationAndDemandContainer").hide();
	}
}

function loadtest_case(childData){	
	divPopupMainTitle(popupTitle_TestCase);
	
 	try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
	
	$('#JTable_Allocate').jtable({
		title: 'Test Cases',
    	actions: { 
   		 listAction: 'product.feature.testcase.mappedlist?productFeatureId='+childData.record.featureId,   		 
      	}, 
        fields : { productId:{
	        	type: 'hidden',  
				list:false
				//defaultValue: productId
	       			},
         testCaseId: { 
    			title: 'Test Case ID',
     		create:false,
     		edit: false,
     		list:true,
     		key: true
    			},        	
     	testCaseName: {
             title: 'Test Case Name ',
             inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
             width: "12%",
             create : false,
             edit :false,
     	},
     	testSuiteId:{
     		title: 'Test Suite', 
     		width: "7%",
     		type : 'hidden',
             create: false,
             edit : false,
             list : false,
         //    options: 'common.list.testsuite.byproductId?productId='+ productId,              
     	},
     	testSuiteName:{
     		title: 'TestSuite Name', 
     		width: "7%",                         
             create: false,
             list: false
     	},
     	testCaseDescription:{
     		title: 'Test Case Description', 
     		inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
     	    width: "40%",                         
             create: false   
     	},
     	testCaseSource:{
     		title: 'Test Case Source', 
     		width: "20%",                         
             create: false,
             list: false,
             edit: false
     	},
     	testCaseCode: { 
     		title: 'Test Case Code',  
     		inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
     		width: "10%",                        
             create: false
     	},
//      	 testCasePriority:{
//      		title: 'Test Case Priority',  
//      		width: "15%",    
//      		create: true
//      	}, 				         	
     	testCaseScriptQualifiedName:{
     		title: 'Script Package Name',  
     		width: "10%",
     		create: false,
     		list : true, 
     		edit : false
     	},
     	testCaseScriptFileName:{
     		title: 'Script File Name',  
     		width: "10%",
     		create: false,
     		list : true, 
     		edit : false
     	}, 
     	executionTypeId:{
     		title : 'Execution Type',
     		width : "10%",
     		create : false,
     		list : true,
     		edit : false,
   			options:function(data){
   				if(data.source =='list'){	      				
   					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
   				}else if(data.source == 'create'){	      				
   					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
   				}
   			}
     	}, 
     	 testcasePriorityId:{
	        		title : 'Priority',
	        		width : "10%",
	        		create : true,
	        		list : true,
	        		edit : false,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.testcasepriority';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.testcasepriority';
	      				}
	      			}
	        	},
	        	testcaseTypeId:{
	        		title : 'Test Case Type',
	        		width : "13%",
	        		create : true,
	        		list : true,
	        		edit : false,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.testcasetype';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.testcasetype';
	      				}
	      			}
	        	},
	        	productFeatureId: {
		             title: 'Features',
		             width:"15%",
		             list:true,
		             edit: false,
		             options:function(data){		      				
		            	 if(data.source =='list'){	      				
		      					return 'product.feature.list?productId='+data.record.productId;	
		      				}else if(data.source == 'create'){	      				
		      					return 'product.feature.list?productId='+data.record.productId;
		      				}
		      			}		            
		        },	
        	},  
		});
	$('#JTable_Allocate').jtable('load');     	
}

function loadtestsuite_case(childData){	
	divPopupMainTitle(popupTitle_TestSuite);
	
 	try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
		
	$('#JTable_Allocate').jtable({ 
			title: 'View TestCase', 
				editinline:{enable:true},
				actions: {
				listAction: 'test.suite.case.list?testSuiteId='+childData.record.testSuiteId,
						},   	 			
				fields: {productId:{
		        	type: 'hidden',  
	   				list:false,
	   				defaultValue: prodId
		       			},
	            testCaseId: { 
	       			title: 'Test Case ID',
	        		create:false,
	        		edit: false,
	        		list:true,
	        		key: true
	       			},        	
	        	testCaseName: {
	                title: 'Test Case ',
	                width: "12%",
	                create : false,
	                edit :false,
	        	},   		        	
	        	testCaseDescription:{
	        		title: 'Test Case Description', 
	        		width: "20%",                         
	                create: false,
	                edit:false
	        	},
	        	testCaseSource:{
	        		title: 'Test Case Source', 
	        		width: "20%",                         
	                create: false,
	                list: false,
	                edit: false
	        	},
	        	testCaseCode: { 
	        		title: 'Test Case Code',  
	        		width: "15%",                        
	                create: false,
	                edit : false,
	        	},
	        	testCasePriority:{
	        		title: 'Test Case Priority',  
	        		width: "15%",    
	        		create: false,
	        		edit : false,
	        		list : false
	        	},
	        	testCaseScriptQualifiedName:{
	        		title: 'Script Package Name',  
	        		width: "15%",
	        		create: false,
	        		list : false, 
	        		edit : false
	        	},
	        	testCaseScriptFileName:{
	        		title: 'Script File Name',  
	        		width: "15%",
	        		create: false,
	        		list : false, 
	        		edit : false
	        	}, 
	        	testcaseExecutionType:{
	        		title: 'Execution Type',  
	        		width: "15%",
	        		create: false,
	        		list : false,           		        		
	        		options : {
						'1' : 'Manual',
						'2' : 'Automated',
						'3' : 'Mixed'
					}
	        	}, 
	        	executionTypeId:{
	        		title : 'Execution Type',
	        		width : "15%",
	        		create : false,
	        		list : true,
	        		edit : false,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
	      				}
	      			}
	        	}, 
	        	testcasePriorityId:{
	        		title : 'Priority',
	        		width : "15%",
	        		create : false,
	        		list : true,
	        		edit : false,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.testcasepriority';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.testcasepriority';
	      				}
	      			}
	        	}, 
	},   	
 formSubmitting: function (event, data) {  
 data.form.find('input[name="testSuiteName"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
 data.form.validationEngine();
 return data.form.validationEngine('validate');
}, 
 //Dispose validation logic when form is closed
 formClosed: function (event, data) {
 data.form.validationEngine('hide');
 data.form.validationEngine('detach');
}  ,
		});
	$('JTable_Allocate').jtable('load');     	
}

function loadtest_step(childData){	
	divPopupMainTitle(popupTitle_TestSteps);
	
 	try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}	
		
	var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var urlToGetWorkPackageTestcasePlanStatus = "workPackage.plan.status?workPackageId="+workPacakgeId;
 	try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
	$('#JTable_Allocate').jtable({
		title: 'Test Steps',
    	actions: {    		
    		listAction: 'testcase.teststep.list?testCaseId='+childData.record.testcaseId
      	}, 
        fields : {
        	testStepName: { 
        		title: 'Name',  
        		width: "20%",                          
                create: false   
        	},
        	testStepDescription:{
        		title: 'Description',  
        		width: "30%",                          
                create: false   
        	},
        	testStepInput: { 
        		title: 'Input', 
        		width: "20%",                         
                create: false
           },
           testStepExpectedOutput: { 
        		title: 'Expected Output',  
        		width: "25%",                        
                create: false
            },
           testStepSource: { 
        		title: 'Source',  
        		width: "7%",         
                create: false							                 
            },
           testStepCode: { 
        		title: 'Code',
        		width: "7%",         
                create: false							                
            },
        	},  
		});
	$('#JTable_Allocate').jtable('load');     	
}

function getStarString(inputId, value,isDisable){
	var addChecked ='';
	var starStr = '';
	var totalStarsCount=5;
	
	for(var i=1; i<=totalStarsCount; i++){
		if(value == i){
			addChecked = ' checked="checked"';
			//console.log(inputId," -- ",starStr);
		}
		if(!isDisable){
			starStr += '<input name="star'+inputId+'" type="radio" class="auto-submit-star" value="'+((totalStarsCount+1)-i)+'~'+inputId+'" title="P'+((totalStarsCount)-i)+'"'+addChecked+'/>';
		    addChecked='';
		}
		else{
			starStr += '<input name="star'+inputId+'" type="radio" class="auto-submit-star" disabled="disabled" value="'+i+'~'+inputId+'" title="P'+((totalStarsCount)-i)+'"'+addChecked+'/>';
		    addChecked='';
		}
	}
	return starStr;
}

/*//BEGIN - Delete - JTable
function listWorkPackageTestcasesOfSelectedWorkPackage(status){
	var totalStarsCount=5;
	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
	} catch(e) {}
	//init jTable
	if(status==0){
		editableFlag=false;
	}else{
		editableFlag=true;
	}
	$('#jTableContainer').jtable({
		title: 'Assign Resources for Work Package',
		paging : true, //Enable paging
		pageSize : 10,
		"scrollY":"50px",
		editinline : {
			enable : true
		},
		toolbarsearch:true,
		selecting : true, //Enable selecting
		multiselect : true, //Allow multiple selecting
		selectingCheckboxes : true, //Show checkboxes on first column
		
	toolbar : {
		items : [
// 			{
// 				icon : "css/images/filter_new_jtable.png",
// 				click : function() {
// 					loadFilterPopup();
// 				}
// 			} ,
		  {
			text : 'Planning Status',
			click : function() {
				
				planningStatus();
				
			}
		} , {
				text : 'Export to Excel',
				click : function() {
					
					exportToExcel();
				}
			} ]
			
			
		},
		
		actions : {
			listAction : urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId,
			//createAction: 'administration.device.add',
			 editinlineAction : 'workpackage.testcaseplan.update'
		//deleteAction: 'administration.device.delete'
		},
		fields : {
			testStepsDetails: {
		        title:'',
		        width: "2%",
		        create:false,
		        edit:true,
		        display: function (childData) { 
	        				//Create an image that will be used to open child table 
	        				
//  Abort_completed functionality for view Testcases Start . 
				        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Test Step List" />'); 

				        	///*  Abort_completed functionality for view Testcases End . 
				        	//Open child table when user clicks the image 
				        	$img.click(function (data) {				        		
				        		loadtest_step(childData);				        	
				        	}); 
				        	 
				        	return $img; 
				        }, 
      		},	
		
			id : {
				key : true,
				list : false
			},
			testcaseId : {
				title : 'Testcase Id',
				list : true,
				width : "20%",
				edit: false
			},
			testcaseName : {
				title : 'TestCase ',
				edit : false,
				width : "20%"
			},
			testcaseCode : {
				title : 'TestCase Code',
				edit : false,
				width : "20%"
			},
			testsuiteName : {
				title : 'TestSuite ',
				edit : false,
				list : false,
				width : "20%"
			},
			testcaseDescription : {
				title : 'Description',
				edit : false,
				width : "20%"
			},decouplingCategoryId : {
				title : 'Decoupling Category Id',
				list:false,
				edit : false,
				width : "20%"
			},
			decouplingCategoryName : {
				title : 'Decoupling Category',
				edit : false,
				list : false,
				width : "20%"
			},featureName : {
				title : 'Features',
				edit : false,
				width : "20%"
			},workPackageId : {
				title : 'ID',
				type : 'hidden'
			},
			workPackageName : {
				title : 'Workpackage Name',
				type : 'hidden'
			},
			sourceType:{
				title : 'Source Type ',
				edit : false
			},
			runConfigurationId : {
				title : 'RunConfiguration ID',
				type : 'hidden',
				//list:true
			},
			environmentCombinationName : {
				title : 'Environment Combination ',
				edit : false
			},
			deviceName : {
				title : 'Device ',
				list : false,
				edit : false
			},
// 			localeId : {
// 				title : 'Locale ID',
// 				type : 'hidden'
// 			},
// 			localeName : {
// 				title : 'Locale Name',
// 				edit : false
// 			},
			productModeId : {
				title : 'Product Mode Id',
				type : 'hidden'
			},
			productModeName : {
				title : 'Product Mode Name',
				type : 'hidden'
			},
			plannedExecutionDate : {
				title : 'Planned Execution Date',
				list : true,
				edit : editableFlag,
				width : "15%",
				type : 'date',
				display: function (data) {
					
			
				 var flag='';
					 if(!editableFlag)
						flag='readonly';  
					if((data.record.testerName==null && data.record.testLeadName!=null )||(data.record.testerName!=null && data.record.testLeadName==null )||(data.record.testerName!=null && data.record.testLeadName!=null )){
						if(editableFlag)
						 	return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" onclick=errorMessage();  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
						else
							//alert("In else"+editableFlag);
							return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
					}
					
					
					else{
						if(data.record.plannedExecutionDate!=null){	
							if(editableFlag){
								
								  return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalender("'+data.record.id+'");  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
								
							}else{
								
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.plannedExecutionDate+'</p>';
								}
									      
						
							} 
						
						else{
							if(editableFlag){
								
								  return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalender("'+data.record.id+'");  value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								
							}else{
					        //return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalender("'+data.record.id+'");   value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.workpackagePlannedStartDate+'</p>';
							}
						}
					
					}
                }
			},
			plannedShiftName : {
				title : 'Planned Shift',
				edit : editableFlag,
				//options : 'common.list.workshift.list.workpackage?workpackageId='+workPackageId
				options : function(data){
					if(data.record.plannedExecutionDate!=null && data.record.plannedExecutionDate!='null'){
							if(data.source =='list'){
								 return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								  return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;	
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
				}
				
			},
			testLeadId : {
				title : 'TestLead ID',
				type : 'hidden'
			},
			testLeadName : {
				title : 'TestLead ',
				width : "20%",
				edit : editableFlag,
				list : true,								
				options : function(data){
					//if(data.record.productModeId == 1){
						if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null && data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
					//}
					
				}
			},
			testerId : {
				title : 'Tester ID',
				type : 'hidden'
			},
			testerName : {
				title : 'Tester ',
				edit : editableFlag,
				list : true,			    
			    options : function(data){						
			    	
			    		if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null&& data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
							}else if(data.source =='create'){
		            			 data.clearCache();
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
							}		
						}else{
							return {'No Data':'No Data'};
						}
			    	
				}
			},			
			plannedShiftId : {
				title : 'Planned Shift ID',
				type : 'hidden'
			},
			actualShiftId : {
				title : 'Actual Shift ID',
				type : 'hidden'
			},
			
			actualShiftName : {
				title : 'Actual Shift',
				edit : false,
				list:false
				//options : 'common.list.actualShift?workpackageId='+workPackageId+'&plannedExecutionDate='+data.record.plannedExecutionDate
			},
			
			actualExecutionDate : {
				title : 'Actual Execution Date',
				edit : false,
				width : "20%",
				type : 'date',
				list:false
			},

			createdDate : {
				title : 'Created Date',
				list : false,
				edit : false,
				width : "20%"
			},
			modifiedDate : {
				title : 'Modified Date',
				edit : false,
				width : "20%",
				list : false
			},
			//executionStatus is disabled
			executionStatus : {
				title : 'Execution Status',
				width : "20%",
			    edit : editableFlag,
			    toolbarsearchComboBox: true,
			    options : [
					{'Value' : '0', 'DisplayText' : 'All'},
					{'Value' : '1', 'DisplayText' : 'Assigned'},
					{'Value' : '2', 'DisplayText' : 'Completed'},
					{'Value' : '3', 'DisplayText' : 'NotStarted'}
				]			    
			}, 
			//is Executed Removed Start
			
			//isExecution END.
			
			executionPriorityId :{
				title: 'Execution Priority',
				width:"50%",
				edit:editableFlag,
				list:true,
				//options :'administration.executionPriorityList'
				display:function(data)
	    		 {
					var dataid=data.record.executionPriorityId;
					var isStarRatingDisabled= false;
					$('.rating-cancel').hide();
					var inputId=data.record.id;
					//alert("status"+data.record.executionStatus);
					if(data.record.executionStatus!=2){
						//if(editableFlag){
							if(dataid == 1){							
								return $(getStarString(inputId, 5,isStarRatingDisabled));
								
							}else if(dataid == 2){							
								return $(getStarString(inputId, 4,isStarRatingDisabled));
								
							}else if(dataid == 3){							
								return $(getStarString(inputId, 3,isStarRatingDisabled));
								
							}else if(dataid == 4){														
								return $(getStarString(inputId, 2,isStarRatingDisabled));
								
							}else if(dataid == 5){							
								return $(getStarString(inputId, 1,isStarRatingDisabled));
								
							}else {														
								return $(getStarString(inputId, 0,isStarRatingDisabled));							
							}
						//}else{
					}else{
						    isStarRatingDisabled= true;
						  
							if(dataid == 1){
								return $(getStarString(inputId, 5,isStarRatingDisabled));
							
							}else if(dataid == 2){
								return $(getStarString(inputId, 4,isStarRatingDisabled));
							
							}else if(dataid == 3){
						        return $(getStarString(inputId, 3,isStarRatingDisabled));
							
							}else if(dataid == 4){
								return $(getStarString(inputId, 2,isStarRatingDisabled));
							
							}else if(dataid == 5){
								return $(getStarString(inputId, 1,isStarRatingDisabled));
							
							}else {
								return $(getStarString(inputId, 0,isStarRatingDisabled));
							}
						}
	    		 }
			},
			
		},
		

		recordsLoaded: function(event, data) {			 
// 			 $(".basic").jRating({
// 				  length:6,
// 				  //step:true,
// 				  decimalLength:1,
// 				  onSuccess : function(){
// 					alert('Success : your rate has been saved :)');
// 				  },
// 				  onError : function(){
// 					alert('Error : please retry');
// 				  }
// 				});	 
	
				$('#jtable-toolbarsearch-testStepsDetails').prop("type", "hidden");  				
				$('#jtable-toolbarsearch-testcaseId').prop("type", "hidden");  				
				$('#jTableContainer #jtable-toolbarsearch-executionPriorityId').prop("type", "hidden");  				
			//	$('#jtable-toolbarsearch-executionStatus').prop("type", "hidden");
				

				
			 $('.auto-submit-star').rating({ 
				 canRateAgain:true,
				
				 callback: function(value, link){
					 var url="administration.workpackage.testcase.executionPriority?executionPriority="+value;
					 $.ajax({
				 		    type: "POST",
				 		    url: url,
				 		    success: function(data) {
				 		    	
				 		    	if(data.Result=='ERROR'){
				 		    		callAlert(data.Message);
					 		    	return false;
				 		    	}
				 				var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				

				 		    	listWorkPackageTestcasesOfSelectedWorkPackage(status);
				 		    },    
				 		    dataType: "json", // expected return value type
				 		    
				 	});  
				 } 
			 }); 
			 
			 var $selectedRows = $('#jTableContainer').jtable('selectedRows');
			 if($selectedRows >=0)
			 {
				// alert("sel row-- "+$selectedRows);
				 $("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", true);
			 }
			 $('.jtable-data-row').click(function() {
		            var row_id = $(this).attr('data-record-key');
		            url='administration.workPackage.planByid?wptcepId='+row_id;
		            $.ajax({
			 		    type: "POST",
			 		    url: url,
			 		    success: function(data) {
			 		    	
			 		    	if(data.Result=='ERROR'){
			 		    		alert(data.Message);
				 		    	return false;
			 		    	}else{
			// 		    		this._onSelectionChanged();
			 		    		 
			 //		                this.trigger("recordUpdated", null, { record: $selectedRows.data('record'), row: $selectedRows, serverResponse: data });
			 	 	//	 $('#jTableContainer').jtable('onRecordUpdated',$selectedRows,data);
			 	 	$('#jTableContainer').jtable('selectRows',$selectedRows);
				            

			 		    	//	return true;
			 		    	}
			 		    },    
			 		    dataType: "json", // expected return value type
			 		    
			 	});
		        });
			 $.unblockUI();
						 
			var flag=false;
 	    	function checkForChanges()
            {
    			flag=false;
    			if (!flag && $("#jTableContainer .jtable").eq(0).css('width') != $("#jTableContainer .jtable-title").eq(0).css("width"))
                {
                	$("#jTableContainer .jtable-title").eq(0).css("width" , $("#jTableContainer .jtable").eq(0).css('width'));
                    $("#jTableContainer .jtable-bottom-panel").eq(0).css("width" , $("#jTableContainer .jtable").eq(0).css('width'));                     
                    flag=true;
                }
            }
 	        setInterval(checkForChanges, 100);			 
			 
		 },
		
	});
	
		$('#jTableContainer .jtable-toolbarsearch').bind('keyup', function(e){
        
   	   	$('#jTableContainer').jtable(
   	   			'load',
   	   			{
   	   			searchTestCaseId: $('#jTableContainer #jtable-toolbarsearch-testcaseId').val(),
	   			searchTestCaseName: $('#jTableContainer #jtable-toolbarsearch-testcaseName').val(),
				searchTestCaseCode: $('#jTableContainer #jtable-toolbarsearch-testcaseCode').val(), 
				searchTestSuiteName: $('#jTableContainer #jtable-toolbarsearch-testsuiteName').val(), 
				searchDescription: $('#jTableContainer #jtable-toolbarsearch-testcaseDescription').val(), 
				searchDCName: $('#jTableContainer #jtable-toolbarsearch-decouplingCategoryName').val(), 
				searchFeatureName: $('#jTableContainer #jtable-toolbarsearch-featureName').val(), 
				searchsourceType: $('#jTableContainer #jtable-toolbarsearch-sourceType').val(), 
				searchECName: $('#jTableContainer #jtable-toolbarsearch-environmentCombinationName').val(),
				searchDeviceName: $('#jTableContainer #jtable-toolbarsearch-deviceName').val(),
				searchTestLeadName: $('#jTableContainer #jtable-toolbarsearch-testLeadName').val(),  
				searchTesterName: $('#jTableContainer #jtable-toolbarsearch-testerName').val(),
				searchPED: $('#jTableContainer #jtable-toolbarsearch-plannedExecutionDate').val(),
				searchPlannedShift: $('#jTableContainer #jtable-toolbarsearch-plannedShiftName').val(),
	   			searchExecutionStatus: $('#jTableContainer #jtable-toolbarsearch-executionStatus').val(),
	   				
   	   			});
   	   }); 
	   var arr = ['#jTableContainer #jtable-toolbarsearch-executionStatus', '#jTableContainer #jtable-toolbarsearch-plannedExecutionDate'];
	   for(var i=0;i<arr.length;i++){
		$(arr[i]).bind('change', function(e){
        
   	   	$('#jTableContainer').jtable(
   	   			'load',
   	   			{
   	   			searchTestCaseId: $('#jTableContainer #jtable-toolbarsearch-testcaseId').val(),
	   			searchTestCaseName: $('#jTableContainer #jtable-toolbarsearch-testcaseName').val(),
				searchTestCaseCode: $('#jTableContainer #jtable-toolbarsearch-testcaseCode').val(), 
				searchTestSuiteName: $('#jTableContainer #jtable-toolbarsearch-testsuiteName').val(), 
				searchDescription: $('#jTableContainer #jtable-toolbarsearch-testcaseDescription').val(), 
				searchDCName: $('#jTableContainer #jtable-toolbarsearch-decouplingCategoryName').val(), 
				searchFeatureName: $('#jTableContainer #jtable-toolbarsearch-featureName').val(), 
				searchsourceType: $('#jTableContainer #jtable-toolbarsearch-sourceType').val(), 
				searchECName: $('#jTableContainer #jtable-toolbarsearch-environmentCombinationName').val(),
				searchDeviceName: $('#jTableContainer #jtable-toolbarsearch-deviceName').val(),
				searchTestLeadName: $('#jTableContainer #jtable-toolbarsearch-testLeadName').val(),  
				searchTesterName: $('#jTableContainer #jtable-toolbarsearch-testerName').val(),
				searchPED: $('#jTableContainer #jtable-toolbarsearch-plannedExecutionDate').val(),
				searchPlannedShift: $('#jTableContainer #jtable-toolbarsearch-plannedShiftName').val(),
	   			searchExecutionStatus: $('#jTableContainer #jtable-toolbarsearch-executionStatus').val(),
	   				
   	   			});
	   	   });
	   }


     //Load all records when page is first shown
   //  $('#LoadRecordsButton').click();
	$('#jTableContainer').jtable('load');
	  
	}	*/	//END - Delete - JTable
	
//BEGIN: ConvertDataTable - AllocateTestcaseFeature
function allocateTestcaseFeaturesDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [];
	//allocateTestcaseFeaturesOptions_Container(optionsArr);
	tType = "AllocateTestcaseFeaturesTable";
	workpackageTestcaseDT();
}

function allocateTestcaseFeaturesOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 allocateTestcaseFeaturesOptions_Container(optionsArr);
			 }else{
				tType = "AllocateTestcaseFeaturesTable";
				workpackageTestcaseDT();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function allocateTestcaseFeaturesDataTableHeader(){
	var childDivString ='<table id="allocateTestcaseFeatures_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th></th>'+ 
			'<th>Feature ID</th>'+
			'<th>Feature Name</th>'+
			'<th>Feature Description</th>'+
			'<th>Environment Combinataion</th>'+
			'<th>Device</th>'+
			'<th>Planned Execution Date</th>'+
			'<th>Planned Shift</th>'+
			'<th>Team Lead</th>'+
			'<th>Tester</th>'+
			'<th>Execution Priority</th>'+
			'<th>View Testcase</th>'+
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
function allocateTestcaseFeaturesDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForAllocateTestcaseFeatures").children().length>0) {
			$("#dataTableContainerForAllocateTestcaseFeatures").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = allocateTestcaseFeaturesDataTableHeader(); 			 
	$("#dataTableContainerForAllocateTestcaseFeatures").append(childDivString);
	
	editorAllocateTestcaseFeatures = new $.fn.dataTable.Editor( {
		"table": "#allocateTestcaseFeatures_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new feature",
	            submit: "Create",
	        }
	    },
		fields: [
			{
				label: "plannedShiftId",
				name: "plannedShiftId",     
				options: optionsResultArr[0],
				"type"  : "select",	
			},{
				label:"testLeadId",
				name:"testLeadId",	
				options: optionsResultArr[1],
				"type"  : "select",	
			},{
				label:"testerId",
				name:"testerId",	
				options: optionsResultArr[2],
				"type"  : "select",	
			},{
				label:"executionPriorityId",
				name:"executionPriorityId",	
				options: optionsResultArr[3],
				"type"  : "select",	
			}
		]
	});	
	
	allocateTestcaseFeaturesDT_oTable = $("#allocateTestcaseFeatures_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,11]; // search column TextBox Invisible Column position
	     		  $('#allocateTestcaseFeatures_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAllocateTestcaseFeaturesDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Feature',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Feature',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [   	     
			{ mData: "status",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorAllocateTestcaseFeatures-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "featureId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "featureName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "featureDescription",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "environmentCombinationName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "deviceName", className: 'disableEditInline', sWidth: '10%'},	
			{ mData: "plannedExecutionDate", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "plannedShiftName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testLeadName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testerName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "executionPriorityName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="allocateTCFeatureMappedTestcaseImg" title="View Testcases" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorAllocateTestcaseFeatures-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#allocateTestcaseFeatures_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAllocateTestcaseFeatures.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#allocateTestcaseFeatures_dataTable_length").css('margin-top','8px');
	$("#allocateTestcaseFeatures_dataTable_length").css('padding-left','35px');
	
	$("#allocateTestcaseFeatures_dataTable_filter").css('margin-right','6px');
	
	$('#allocateTestcaseFeatures_dataTable').on( 'change', 'input.editorAllocateTestcaseFeatures-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseFeaturesDT_oTable.DataTable().row(tr);

    	$.ajax({
			type: "POST",
			url: "administration.workPackage.planByid?wptcepId="+row.data().id,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});
	});
	
	$('#allocateTestcaseFeatures_dataTable tbody').on('click', 'td .allocateTCFeatureMappedTestcaseImg', function () {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseFeaturesDT_oTable.DataTable().row(tr);
		currFeatureId = row.data().featureId;
    	$('#workpackageFeaturesDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		workpackageFeaturesMappedTestcaseDataTable();
	});
	
	allocateTestcaseFeaturesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutAllocateTestcaseFeaturesDT='';
function reInitializeAllocateTestcaseFeaturesDT(){
	clearTimeoutAllocateTestcaseFeaturesDT = setTimeout(function(){				
		allocateTestcaseFeaturesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAllocateTestcaseFeaturesDT);
	},200);
}
//END: ConvertDataTable - AllocateTestcaseFeature

//BEGIN: ConvertDataTable - AllocateTestcaseTestcases
function allocateTestcaseTestcasesDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [];
	//allocateTestcaseTestcasesOptions_Container(optionsArr);
	tType = "AllocateTestcaseTestcasesTable";
	workpackageTestcaseDT();
}

function allocateTestcaseTestcasesOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 allocateTestcaseTestcasesOptions_Container(optionsArr);
			 }else{
				tType = "AllocateTestcaseTestcasesTable";
				workpackageTestcaseDT();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function allocateTestcaseTestcasesDataTableHeader(){
	var childDivString ='<table id="allocateTestcaseTestcases_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th></th>'+ 
			'<th>Testcase ID</th>'+
			'<th>Testcase Name</th>'+
			'<th>Testcase Code</th>'+
			'<th>Description</th>'+
			'<th>Features</th>'+
			'<th>Source Type</th>'+
			'<th>Environment Combinataion</th>'+
			'<th>Planned Execution Date</th>'+
			'<th>Planned Shift</th>'+
			'<th>Test Lead</th>'+
			'<th>Tester</th>'+
			'<th>Execution Status</th>'+
			'<th>Execution Priority</th>'+
			'<th>View Teststeps</th>'+
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
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}
function allocateTestcaseTestcasesDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForAllocateTestcaseTestcases").children().length>0) {
			$("#dataTableContainerForAllocateTestcaseTestcases").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = allocateTestcaseTestcasesDataTableHeader(); 			 
	$("#dataTableContainerForAllocateTestcaseTestcases").append(childDivString);
	
	editorAllocateTestcaseTestcases = new $.fn.dataTable.Editor( {
		"table": "#allocateTestcaseTestcases_dataTable",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new feature",
	            submit: "Create",
	        }
	    },
		fields: [
			{
				label: "plannedShiftId",
				name: "plannedShiftId",     
				options: optionsResultArr[0],
				"type"  : "select",	
			},{
				label:"testLeadId",
				name:"testLeadId",	
				options: optionsResultArr[1],
				"type"  : "select",	
			},{
				label:"testerId",
				name:"testerId",	
				options: optionsResultArr[2],
				"type"  : "select",	
			},{
				label:"executionPriorityId",
				name:"executionPriorityId",	
				options: optionsResultArr[3],
				"type"  : "select",	
			},{
				label:"executionStatus",
				name:"executionStatus",	
				options: optionsResultArr[4],
				"type"  : "select",	
			}
		]
	});	
	
	allocateTestcaseTestcasesDT_oTable = $("#allocateTestcaseTestcases_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,14]; // search column TextBox Invisible Column position
	     		  $('#allocateTestcaseTestcases_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAllocateTestcaseTestcasesDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testcase',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [   	     
			{ mData: "status",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorAllocateTestcaseTestcases-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "testcaseId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testcaseName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testcaseCode",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testcaseDescription",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "featureName", className: 'disableEditInline', sWidth: '10%'},	
			{ mData: "sourceType", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "environmentCombinationName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "plannedExecutionDate", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "plannedShiftName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testLeadName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testerName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "executionStatus", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "executionPriorityName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="allocateTCTestlistImg" title="View Teststeps" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorAllocateTestcaseTestcases-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#allocateTestcaseTestcases_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAllocateTestcaseTestcases.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#allocateTestcaseTestcases_dataTable_length").css('margin-top','8px');
	$("#allocateTestcaseTestcases_dataTable_length").css('padding-left','35px');
	
	$("#allocateTestcaseTestcases_dataTable_filter").css('margin-right','6px');
	
	$('#allocateTestcaseTestcases_dataTable').on( 'change', 'input.editorAllocateTestcaseTestcases-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseTestcasesDT_oTable.DataTable().row(tr);

    	$.ajax({
			type: "POST",
			url: "administration.workPackage.planByid?wptcepId="+row.data().id,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});
	});
	
	$('#allocateTestcaseTestcases_dataTable tbody').on('click', 'td .allocateTCTestlistImg', function () {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseTestcasesDT_oTable.DataTable().row(tr);
    	currTestCaseId = row.data().testcaseId;
    	$('#workpackageTestcaseDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		tType = "WorkpackageTestStepsTable";
		workpackageTestcaseDT();
	});
	
	allocateTestcaseTestcasesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutAllocateTestcaseTestcasesDT='';
function reInitializeAllocateTestcaseTestcasesDT(){
	clearTimeoutAllocateTestcaseTestcasesDT = setTimeout(function(){				
		allocateTestcaseTestcasesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAllocateTestcaseTestcasesDT);
	},200);
}
//END: ConvertDataTable - AllocateTestcaseTestcases

//BEGIN: ConvertDataTable - AllocateTestcaseTestsuites
function allocateTestcaseTestsuitesDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [];
	//allocateTestcaseTestsuitesOptions_Container(optionsArr);
	tType = "AllocateTestcaseTestsuitesTable";
	workpackageTestcaseDT();
}

function allocateTestcaseTestsuitesOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 allocateTestcaseTestsuitesOptions_Container(optionsArr);
			 }else{
				tType = "AllocateTestcaseTestsuitesTable";
				workpackageTestcaseDT();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function allocateTestcaseTestsuitesDataTableHeader(){
	var childDivString ='<table id="allocateTestcaseTestsuites_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th></th>'+ 
			'<th>Testsuite ID</th>'+
			'<th>Testsuite Name</th>'+
			'<th>Description</th>'+
			'<th>Environment Combinataion</th>'+
			'<th>Device</th>'+
			'<th>Planned Execution Date</th>'+
			'<th>Planned Shift</th>'+
			'<th>Test Lead</th>'+
			'<th>Tester</th>'+
			'<th>Execution Priority</th>'+
			'<th>View Testcases</th>'+
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
function allocateTestcaseTestsuitesDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForAllocateTestcaseTestsuites").children().length>0) {
			$("#dataTableContainerForAllocateTestcaseTestsuites").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = allocateTestcaseTestsuitesDataTableHeader(); 			 
	$("#dataTableContainerForAllocateTestcaseTestsuites").append(childDivString);
	
	editorAllocateTestcaseTestsuites = new $.fn.dataTable.Editor( {
		"table": "#allocateTestcaseTestsuites_dataTable",
		//ajaxUrl: "test.suite.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new Testsuite",
	            submit: "Create",
	        }
	    },
		fields: [
			{
				label: "plannedShiftId",
				name: "plannedShiftId",     
				options: optionsResultArr[0],
				"type"  : "select",	
			},{
				label:"testLeadId",
				name:"testLeadId",	
				options: optionsResultArr[1],
				"type"  : "select",	
			},{
				label:"testerId",
				name:"testerId",	
				options: optionsResultArr[2],
				"type"  : "select",	
			},{
				label:"executionPriorityId",
				name:"executionPriorityId",	
				options: optionsResultArr[3],
				"type"  : "select",	
			},{
				label:"plannedExecutionDate",
				name:"plannedExecutionDate",	
				"type"  : "date",	
			}
		]
	});	
	
	allocateTestcaseTestsuitesDT_oTable = $("#allocateTestcaseTestsuites_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [0,11]; // search column TextBox Invisible Column position
	     		  $('#allocateTestcaseTestsuites_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAllocateTestcaseTestsuitesDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testsuite',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testsuite',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	    columnDefs: [],
        aaData:jsonObj.data,		    				 
	    aoColumns: [   	     
			{ mData: "status",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorAllocateTestcaseTestsuites-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "testSuiteId",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testSuiteName",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testSuiteDescription",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "environmentCombinationName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "deviceName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "plannedExecutionDate", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "plannedShiftName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testLeadName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "testerName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "executionPriorityName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: null,
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="allocateTCTestsuiteTestcasesImg" title="View Testcases" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorAllocateTestcaseTestsuites-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#allocateTestcaseTestsuites_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAllocateTestcaseTestsuites.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#allocateTestcaseTestsuites_dataTable_length").css('margin-top','8px');
	$("#allocateTestcaseTestsuites_dataTable_length").css('padding-left','35px');
	
	$("#allocateTestcaseTestsuites_dataTable_filter").css('margin-right','6px');
	
	$('#allocateTestcaseTestsuites_dataTable').on( 'change', 'input.editorAllocateTestcaseTestsuites-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseTestsuitesDT_oTable.DataTable().row(tr);

    	$.ajax({
			type: "POST",
			url: "administration.workPackage.planByid?wptcepId="+row.data().id,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	//callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});
	});
	
	$('#allocateTestcaseTestsuites_dataTable tbody').on('click', 'td .allocateTCTestsuiteTestcasesImg', function () {
		var tr = $(this).closest('tr');
    	var row = allocateTestcaseTestsuitesDT_oTable.DataTable().row(tr);
    	currTestSuiteId = row.data().testSuiteId;
    	$('#workpackageTestsuiteDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		workpackageViewTestcaseDataTable();
	});
	
	allocateTestcaseTestsuitesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutAllocateTestcaseTestsuitesDT='';
function reInitializeAllocateTestcaseTestsuitesDT(){
	clearTimeoutAllocateTestcaseTestsuitesDT = setTimeout(function(){				
		allocateTestcaseTestsuitesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAllocateTestcaseTestsuitesDT);
	},200);
}
//END: ConvertDataTable - AllocateTestcaseTestsuites
/*//END - Delete - JTable
function listWorkPackageFeatureOfSelectedWorkPackage(status){
			
	if(status==0){
		editableFlag=false;
	}else{
		editableFlag= true;
	}
	
	try{
		if ($('#jTableContainerFeature').length>0) {
			 $('#jTableContainerFeature').jtable('destroy'); 
		}
	} catch(e) {}
	//init jTable
	$('#jTableContainerFeature').jtable({

		title: 'Assign Resources for Work Package(Feature)',
		paging : true, //Enable paging
		pageSize : 10,
		"scrollY":"50px",
		editinline : {
			enable : true
		},
		toolbarsearch:true,
		selecting : true, //Enable selecting
		multiselect : true, //Allow multiple selecting
		selectingCheckboxes : true, //Show checkboxes on first column
		
	toolbar : {
		items : [

		  {
			text : 'Planning Status',
			click : function() {
				
				planningStatus();
			}
		} , {
				text : 'Export to Excel',
				click : function() {
					
					exportToExcel();
				}
			} ]
			
			
		},
		
		actions : {
			 listAction : 'workpackage.featureplan.list?workpackageId='+workPackageId+'&status='+status,
			 editinlineAction : 'workpackage.featureplan.update'
		},
		fields : {
			testCasesDetails: {
		        title:'',
		        width: "2%",
		        create:false,
		        edit:true,
		        display: function (childData) { 
				        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Test Case List" />'); 

				        	$img.click(function (data) { 
				        		
				        	loadtest_case(childData);

				        	}); 
				        		return $img; 
				        	}, 
      	},	
		
			id : {
				key : true,
				list : false
			},
			featureId : {
				title : 'Feature Id',
				list : true,
				width : "20%",
				edit: false
			},
			featureName : {
				title : 'Feature Name ',
				edit : false,
				width : "20%"
			},
		   featureDescription : {
				title : 'Description',
				edit : false,
				width : "20%"
			},
			
			workPackageId : {
				title : 'ID',
				type : 'hidden'
			},
			workPackageName : {
				title : 'Workpackage Name',
				type : 'hidden'
			},
			runConfigurationId : {
				title : 'RunConfiguration ID',
				type : 'hidden',
				//list:true
			},
			environmentCombinationName : {
				title : 'Environment Combination ',
				edit : false
			},
			deviceName : {
				title : 'Device ',
				edit : false
			},
			productModeId : {
				title : 'Product Mode Id',
				type : 'hidden'
			},
			productModeName : {
				title : 'Product Mode Name',
				type : 'hidden'
			},
			plannedExecutionDate : {
				title : 'Planned Execution Date',
				list : true,
				edit : editableFlag,
				width : "15%",
				type : 'date',
				display: function (data) {	
				
				 var flag='';
					 if(!editableFlag )
						flag='readonly';  
					if((data.record.testerName==null && data.record.testLeadName!=null )||(data.record.testerName!=null && data.record.testLeadName==null )||(data.record.testerName!=null && data.record.testLeadName!=null )){
							if(editableFlag)
							 	return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" onclick=errorMessage();  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
							else
								//alert("In else"+editableFlag);
								return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
					}					
					else{						
						if(data.record.plannedExecutionDate!=null){	
							if(editableFlag){								
								return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalenderFeature("'+data.record.id+'");  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
								
							}else{								
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.plannedExecutionDate+'</p>';
							}						
						}						
						else{
							if(editableFlag){								
								  return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalenderFeature("'+data.record.id+'");  value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								
							}else{
					        //return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalender("'+data.record.id+'");   value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.workpackagePlannedStartDate+'</p>';
							}
						}
						}
	                }
			},
			plannedShiftName : {
				title : 'Planned Shift',
				edit : editableFlag,
				//options : 'common.list.workshift.list.workpackage?workpackageId='+workPackageId
				options : function(data){						
						if(data.record.plannedExecutionDate!=null && data.record.plannedExecutionDate!='null'){
							if(data.source =='list'){
								 return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								  return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;	
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
				}
			},
			testLeadId : {
				title : 'TestLead ID',
				type : 'hidden'
			},
			testLeadName : {
				title : 'TestLead ',
				width : "20%",
				edit : editableFlag,
				list : true,								
				options : function(data){
					//if(data.record.productModeId == 1){
						if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null && data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
				}
			},
			testerId : {
				title : 'Tester ID',
				type : 'hidden'
			},
			testerName : {
				title : 'Tester ',
				edit : editableFlag,
				list : true,			    
			    options : function(data){						
			    	//if(data.record.productModeId == 1){
			    		if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null&& data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
							}else if(data.source =='create'){
		            			 data.clearCache();
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
							}		
						}else{
							return {'No Data':'No Data'};
						}
				}
			},			
			plannedShiftId : {
				title : 'Planned Shift ID',
				type : 'hidden'
			},
			actualShiftId : {
				title : 'Actual Shift ID',
				type : 'hidden'
			},
			
			actualShiftName : {
				title : 'Actual Shift',
				edit : false,
				list:false
				//options : 'common.list.actualShift?workpackageId='+workPackageId+'&plannedExecutionDate='+data.record.plannedExecutionDate
			},
			
			actualExecutionDate : {
				title : 'Actual Execution Date',
				edit : false,
				width : "20%",
				type : 'date',
				list:false
			},

			createdDate : {
				title : 'Created Date',
				list : false,
				edit : false,
				width : "20%"
			},
			modifiedDate : {
				title : 'Modified Date',
				edit : false,
				width : "20%",
				list : false
			},
			executionPriorityId :{
				title: 'Execution Priority',
				width:"50%",
				edit:editableFlag,
				list:true,
				//options :'administration.executionPriorityList'
				display:function(data)
	    		 {
					var dataid=data.record.executionPriorityId;
					var isStarRatingDisabled= false;
					$('.rating-cancel').hide();
					var inputId=data.record.id;
					//alert("status"+data.record.executionStatus);
					//if(data.record.executionStatus!=2){
					if(editableFlag){
							if(dataid == 1){							
								return $(getStarString(inputId, 5,isStarRatingDisabled));
								
							}else if(dataid == 2){							
								return $(getStarString(inputId, 4,isStarRatingDisabled));
								
							}else if(dataid == 3){							
								return $(getStarString(inputId, 3,isStarRatingDisabled));
								
							}else if(dataid == 4){														
								return $(getStarString(inputId, 2,isStarRatingDisabled));
								
							}else if(dataid == 5){							
								return $(getStarString(inputId, 1,isStarRatingDisabled));
								
							}else {														
								return $(getStarString(inputId, 0,isStarRatingDisabled));							
							}
						//}else{
						}else{
						    isStarRatingDisabled= true;
						  
							if(dataid == 1){
								return $(getStarString(inputId, 5,isStarRatingDisabled));
							
							}else if(dataid == 2){
								return $(getStarString(inputId, 4,isStarRatingDisabled));
							
							}else if(dataid == 3){
						        return $(getStarString(inputId, 3,isStarRatingDisabled));
							
							}else if(dataid == 4){
								return $(getStarString(inputId, 2,isStarRatingDisabled));
							
							}else if(dataid == 5){
								return $(getStarString(inputId, 1,isStarRatingDisabled));
							
							}else {
								return $(getStarString(inputId, 0,isStarRatingDisabled));
							}
						}
	    		 }
			},									
		},		

		recordsLoaded: function(event, data) {			 
			$('#jtable-toolbarsearch-testCasesDetails').prop("type", "hidden");  				
				$('#jtable-toolbarsearch-featureId').prop("type", "hidden");  						
				$('#jtable-toolbarsearch-executionPriorityId').prop("type", "hidden"); 
				
				 $('.auto-submit-star').rating({ 
					 canRateAgain:true,
					
					 callback: function(value, link){
						 var url="administration.workpackage.feature.executionPriority?executionPriority="+value;
						 $.ajax({
					 		    type: "POST",
					 		    url: url,
					 		    success: function(data) {
					 		    	
					 		    	if(data.Result=='ERROR'){
					 		    		callAlert(data.Message);
						 		    	return false;
					 		    	}
					 		    	var status = $("#testBedfeaturestatus_ul").find('option:selected').val();
					 		    	listWorkPackageFeatureOfSelectedWorkPackage(status);
					 		    },    
					 		    dataType: "json", // expected return value type
					 		    
					 	});  
					 } 
				 });				
				
			 var $selectedRows = $('#jTableContainerFeature').jtable('selectedRows');
			 if($selectedRows >=0)
			 {
				 $("#jTableContainerFeature").find(".jtable-selecting-column > input").prop("checked", true);
			 }
			 $('.jtable-data-row').click(function() {
		            var row_id = $(this).attr('data-record-key');
		            url='administration.workPackage.planByid?wptcepId='+row_id;
		            $.ajax({
			 		    type: "POST",
			 		    url: url,
			 		    success: function(data) {
			 		    	
			 		    	if(data.Result=='ERROR'){
			 		    		alert(data.Message);
				 		    	return false;
			 		    	}else{
			 	 	$('#jTableContainerFeature').jtable('selectRows',$selectedRows);
			 		    	//	return true;
			 		    	}
			 		    },    
			 		    dataType: "json", // expected return value type
			 		    
			 	});
		        });
			 $.unblockUI();
		 },
		
	});
	
   		$('#jTableContainerFeature').keyup(function(e){
       
  	   	$('#jTableContainerFeature').jtable(
   	   			'load',
   	   			{
  	   			searchFeatureName: $('#jtable-toolbarsearch-featureName').val(),
	   			searchFeatureDescription: $('#jtable-toolbarsearch-featureDescription').val(),   				
   			    searchECName: $('#jtable-toolbarsearch-environmentCombinationName').val(),
				searchDeviceName: $('#jtable-toolbarsearch-deviceName').val(),
			    searchPED: $('#jtable-toolbarsearch-plannedExecutionDate').val(),
			    searchPlannedShift: $('#jtable-toolbarsearch-plannedShiftName').val(),
	   			searchTestLeadName: $('#jtable-toolbarsearch-testLeadName').val(),  
	   			searchTesterName: $('#jtable-toolbarsearch-testerName').val(),
   	   				
   	   			});
   	   });
		
	$('#jTableContainerFeature #jtable-toolbarsearch-plannedExecutionDate').bind('change', function(e){
                
   	   	$('#jTableContainerFeature').jtable(
   	   			'load',
   	   			{
   	   			searchFeatureName: $('#jtable-toolbarsearch-featureName').val(),
	   			searchFeatureDescription: $('#jtable-toolbarsearch-featureDescription').val(),   				
	   			searchECName: $('#jtable-toolbarsearch-environmentCombinationName').val(),
				searchDeviceName: $('#jtable-toolbarsearch-deviceName').val(),
				searchPED: $('#jtable-toolbarsearch-plannedExecutionDate').val(),
				searchPlannedShift: $('#jtable-toolbarsearch-plannedShiftName').val(),
	   			searchTestLeadName: $('#jtable-toolbarsearch-testLeadName').val(),  
	   			searchTesterName: $('#jtable-toolbarsearch-testerName').val(),
	   			});
	   	   });


     //Load all records when page is first shown
   //  $('#LoadRecordsButton').click();
	$('#jTableContainerFeature').jtable('load');
 /* 	$.session.remove('testerListId');
   	$.session.remove('testLeadListId');
   	$.session.remove('shiftListId');
   	$.session.remove('dcListFilterId');
   	$.session.remove('testLeadListFilterId');
   	$.session.remove('testerListFilterId');
   	$.session.remove('envListFilterId');
   	$.session.remove('localeListFilterId');
   	$.session.remove('executionPriorityListId'); 
}*///END - Delete - JTable
/*//END - Delete - JTable
function listWorkPackageTestSuiteOfSelectedWorkPackage(status){
			
	if(status==0){
		editableFlag=false;
	}else{
		editableFlag=true;
	}
	try{
		if ($('#jTableContainerTestSuite').length>0) {
			 $('#jTableContainerTestSuite').jtable('destroy'); 
		}
	} catch(e) {}
	//init jTable
	$('#jTableContainerTestSuite').jtable({

		title: 'Assign Resources for Work Package(TestSuite)',
		paging : true, //Enable paging
		pageSize : 10,
		"scrollY":"50px",
		editinline : {
			enable : true
		},
		toolbarsearch:true,
		selecting : true, //Enable selecting
		multiselect : true, //Allow multiple selecting
		selectingCheckboxes : true, //Show checkboxes on first column
		
	toolbar : {
		items : [

		  {
			text : 'Planning Status',
			click : function() {
				
				planningStatus();
			}
		} , {
				text : 'Export to Excel',
				click : function() {
					
					exportToExcel();
				}
			} ]
			
			
		},
		
		actions : {
			listAction : 'workpackage.testSuiteplan.list?workpackageId='+workPackageId+'&status='+status,
			 editinlineAction : 'workpackage.testSuiteplan.update'
		},
		fields : {
			testCasesDetails: {
		        title:'',
		        width: "2%",
		        create:false,
		        edit:true,
		        display: function (childData) { 
				        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Test Case List" />'); 

				        	$img.click(function (data) { 
				        		
				        	loadtestsuite_case(childData);

				        	}); 
				        		return $img; 
				        	}, 
      	},	
		
			id : {
				key : true,
				list : false
			},
			testSuiteId : {
				title : 'TestSuite Id',
				list : true,
				width : "20%",
				edit: false
			},
			testSuiteName : {
				title : 'TestSuite Name ',
				edit : false,
				width : "20%"
			},
			testSuiteDescription : {
				title : 'Description',
				edit : false,
				width : "20%"
			},
			
			workPackageId : {
				title : 'ID',
				type : 'hidden'
			},
			workPackageName : {
				title : 'Workpackage Name',
				type : 'hidden'
			},
			runConfigurationId : {
				title : 'RunConfiguration ID',
				type : 'hidden',
				//list:true
			},
			environmentCombinationName : {
				title : 'Environment Combination ',
				edit : false
			},
			deviceName : {
				title : 'Device ',
				edit : false
			},
			productModeId : {
				title : 'Product Mode Id',
				type : 'hidden'
			},
			productModeName : {
				title : 'Product Mode Name',
				type : 'hidden'
			},
			plannedExecutionDate : {
				title : 'Planned Execution Date',
				list : true,
				edit : editableFlag,
				width : "15%",
				type : 'date',
				display: function (data) {					
			
				 var flag='';
					 if(!editableFlag)
						flag='readonly';  
					if((data.record.testerName==null && data.record.testLeadName!=null )||(data.record.testerName!=null && data.record.testLeadName==null )||(data.record.testerName!=null && data.record.testLeadName!=null )){
						if(editableFlag)
						 	return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" onclick=errorMessage();  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
						else					
							return $('<input id="'+data.record.id+'" class=" date-picker"  '+flag+' type="text" value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
					}					
					else{						
						if(data.record.plannedExecutionDate!=null){	
							if(editableFlag){								
								return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalenderTestSuite("'+data.record.id+'");  value="'+setFormattedDate(data.record.plannedExecutionDate)+'"/>');
								
							}else{								
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.plannedExecutionDate+'</p>';
							}						
						}						
						else{
							if(editableFlag){								
								 return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalenderTestSuite("'+data.record.id+'");  value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								
							}else{
					        //return $('<input id="'+data.record.id+'" class="date-picker"  type="text" onclick=callJtableCalender("'+data.record.id+'");   value="'+setFormattedDate(data.record.workpackagePlannedStartDate)+'"/>');
								return  '<p  style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  >'+data.record.workpackagePlannedStartDate+'</p>';
							}
						}	
					}
                }
			},
			plannedShiftName : {
				title : 'Planned Shift',
				edit : editableFlag,
				//options : 'common.list.workshift.list.workpackage?workpackageId='+workPackageId
				options : function(data){
					if(data.record.plannedExecutionDate!=null && data.record.plannedExecutionDate!='null'){
							if(data.source =='list'){
								 return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								  return 'common.list.workshift.list.workpackage?workpackageId='+workPackageId;	
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
				}
			},
			testLeadId : {
				title : 'TestLead ID',
				type : 'hidden'
			},
			testLeadName : {
				title : 'TestLead ',
				width : "20%",
				edit : editableFlag,
				list : true,								
				options : function(data){
					//if(data.record.productModeId == 1){
						if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null && data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
		            		 }else if(data.source =='create'){
		            			 data.clearCache();
								 return 'administration.user.testLeadListExcludingTester?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
		            		 }	
						}else{
							return {'No Data':'No Data'};
						}
				}
			},
			testerId : {
				title : 'Tester ID',
				type : 'hidden'
			},
			testerName : {
				title : 'Tester ',
				edit : editableFlag,
				list : true,			    
			    options : function(data){						
			    	//if(data.record.productModeId == 1){
			    		if(data.record.plannedExecutionDate!=null && data.record.plannedShiftId!=null&& data.record.plannedExecutionDate!='null' && data.record.plannedShiftId!=0){
							if(data.source =='list'){
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;							
							}else if(data.source =='create'){
		            			 data.clearCache();
								return 'administration.user.testerListExcludingTestLead?workPackageId='+workPackageId+"&plannedExecutionDate="+data.record.plannedExecutionDate+"&shiftId="+data.record.plannedShiftId;
							}		
						}else{
							return {'No Data':'No Data'};
						}
				}
			},			
			plannedShiftId : {
				title : 'Planned Shift ID',
				type : 'hidden'
			},
			actualShiftId : {
				title : 'Actual Shift ID',
				type : 'hidden'
			},
			
			actualShiftName : {
				title : 'Actual Shift',
				edit : false,
				list:false
				//options : 'common.list.actualShift?workpackageId='+workPackageId+'&plannedExecutionDate='+data.record.plannedExecutionDate
			},
			
			actualExecutionDate : {
				title : 'Actual Execution Date',
				edit : false,
				width : "20%",
				type : 'date',
				list:false
			},

			createdDate : {
				title : 'Created Date',
				list : false,
				edit : false,
				width : "20%"
			},
			modifiedDate : {
				title : 'Modified Date',
				edit : false,
				width : "20%",
				list : false
			},
			executionPriorityId :{
				title: 'Execution Priority',
				width:"50%",
				edit:editableFlag,
				list:true,
				//options :'administration.executionPriorityList'
				display:function(data)
	    		 {
					var dataid=data.record.executionPriorityId;
	var isStarRatingDisabled= false;
					$('.rating-cancel').hide();
					var inputId=data.record.id;
					//alert("status"+data.record.executionStatus);
					//if(data.record.executionStatus!=2){
						if(editableFlag){
							if(dataid == 1){							
								return $(getStarString(inputId, 5,isStarRatingDisabled));
								
							}else if(dataid == 2){							
								return $(getStarString(inputId, 4,isStarRatingDisabled));
								
							}else if(dataid == 3){							
								return $(getStarString(inputId, 3,isStarRatingDisabled));
								
							}else if(dataid == 4){														
								return $(getStarString(inputId, 2,isStarRatingDisabled));
								
							}else if(dataid == 5){							
								return $(getStarString(inputId, 1,isStarRatingDisabled));
								
							}else {														
								return $(getStarString(inputId, 0,isStarRatingDisabled));							
							}
						//}else{
					}else{
						    isStarRatingDisabled= true;
						  
							if(dataid == 1){
								return $(getStarString(inputId, 5,isStarRatingDisabled));
							
							}else if(dataid == 2){
								return $(getStarString(inputId, 4,isStarRatingDisabled));
							
							}else if(dataid == 3){
						        return $(getStarString(inputId, 3,isStarRatingDisabled));
							
							}else if(dataid == 4){
								return $(getStarString(inputId, 2,isStarRatingDisabled));
							
							}else if(dataid == 5){
								return $(getStarString(inputId, 1,isStarRatingDisabled));
							
							}else {
								return $(getStarString(inputId, 0,isStarRatingDisabled));
							}
						}
	    		 }
			},
			
		},
		

		recordsLoaded: function(event, data) {
				$('#jtable-toolbarsearch-testStepsDetails').prop("type", "hidden");  				
// 				$('#jtable-toolbarsearch-testcaseId').prop("type", "hidden");  				
 				$('#jTableContainerTestSuite #jtable-toolbarsearch-executionPriorityId').prop("type", "hidden");  				
// 				$('#jtable-toolbarsearch-executionStatus').prop("type", "hidden");
				$('#jtable-toolbarsearch-testSuiteId').prop("type", "hidden");
				$('#jTableContainerTestSuite #jtable-toolbarsearch-testCasesDetails').prop("type", "hidden");  				
			
				 $('.auto-submit-star').rating({ 
					 canRateAgain:true,
					
					 callback: function(value, link){
						 var url="administration.workpackage.testsuite.executionPriority?executionPriority="+value;
						 $.ajax({
					 		    type: "POST",
					 		    url: url,
								cache: true,
					 		    success: function(data) {
					 		    	
					 		    	if(data.Result=='ERROR'){
					 		    		callAlert(data.Message);
						 		    	return false;
					 		    	}
					 				var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();				

					 		    	listWorkPackageTestSuiteOfSelectedWorkPackage(status);
					 		    },    
					 		    dataType: "json", // expected return value type
					 		    
					 	});  
					 } 
				 }); 
				
				
			 var $selectedRows = $('#jTableContainerTestSuite').jtable('selectedRows');
			 if($selectedRows >=0)
			 {
				 $("#jTableContainerTestSuite").find(".jtable-selecting-column > input").prop("checked", true);
			 }
			 $('.jtable-data-row').click(function() {
		            var row_id = $(this).attr('data-record-key');
		            url='administration.workPackage.planByid?wptcepId='+row_id;
		            $.ajax({
			 		    type: "POST",
			 		    url: url,
			 		    success: function(data) {
			 		    	
			 		    	if(data.Result=='ERROR'){
			 		    		alert(data.Message);
				 		    	return false;
			 		    	}else{
			 	 				$('#jTableContainerTestSuite').jtable('selectRows',$selectedRows);
			 		    		//	return true;
			 		    	}
			 		    },    
			 		    dataType: "json", // expected return value type
			 		    
			 	});
		        });
			 $.unblockUI();
		 },
		
	});
	
	 	$('#jTableContainerTestSuite').keyup(function(e){
        
   	   	$('#jTableContainerTestSuite').jtable(
   	   			'load',
   	   			{
				searchTestSuiteDescription: $('#jTableContainerTestSuite #jtable-toolbarsearch-testSuiteDescription').val(),
	   			searchECName: $('#jTableContainerTestSuite #jtable-toolbarsearch-environmentCombinationName').val(),
				searchTestSuiteName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testSuiteName').val(),
				searchDeviceName: $('#jTableContainerTestSuite #jtable-toolbarsearch-deviceName').val(),
				searchPED: $('#jTableContainerTestSuite #jtable-toolbarsearch-plannedExecutionDate').val(),
				searchPlannedShift: $('#jTableContainerTestSuite #jtable-toolbarsearch-plannedShiftName').val(),
	   			searchTestLeadName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testLeadName').val(),  
	   			searchTesterName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testerName').val(),
   	   				
   	   			});
   	   });
	   
	   	$('#jTableContainerTestSuite #jtable-toolbarsearch-plannedExecutionDate').bind('change', function(e){
                
   	   	$('#jTableContainerTestSuite').jtable(
   	   			'load',
   	   			{
   	   			searchTestSuiteDescription: $('#jTableContainerTestSuite #jtable-toolbarsearch-testSuiteDescription').val(),
	   			searchECName: $('#jTableContainerTestSuite #jtable-toolbarsearch-environmentCombinationName').val(),
				searchTestSuiteName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testSuiteName').val(),
				searchDeviceName: $('#jTableContainerTestSuite #jtable-toolbarsearch-deviceName').val(),
				searchPED: $('#jTableContainerTestSuite #jtable-toolbarsearch-plannedExecutionDate').val(),
				searchPlannedShift: $('#jTableContainerTestSuite #jtable-toolbarsearch-plannedShiftName').val(),
	   			searchTestLeadName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testLeadName').val(),  
	   			searchTesterName: $('#jTableContainerTestSuite #jtable-toolbarsearch-testerName').val(),
	   			});
	   	   });


     //Load all records when page is first shown
   //  $('#LoadRecordsButton').click();
	$('#jTableContainerTestSuite').jtable('load');

}*/	//END - Delete - JTable
		
function errorMessage(){
	callAlert("Planned Execution Date cannot be modified after allocating user");
	return false;
}
	function callJtableCalender(calenderId){
		$( "#"+calenderId ).datepicker();
		$( "#"+calenderId ).focus();

		
		$("#"+calenderId).on("changeDate", function() {
		var date=document.getElementById(calenderId).value;
		$(".datepicker").hide();
		 $.ajax({
				type: "POST",
				url:'workpackage.allocateTestCase.update?modifiedColumn=PlannedExecutionDate&modifiedValue='+date+'&rowId='+calenderId,
				success : function(data) {
					if(data.Result=="ERROR"){
			    		callAlert(data.Message);
			    		return false;
			    	}
				}
			});	
			var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				

		 urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId=0&testerId=0&envId=0&localeId=0&plannedExecutionDate=&dcId=0&executionPriority=0&status="+status;
	 	  //listWorkPackageTestcasesOfSelectedWorkPackage(status);//END - Delete - JTable
		  allocateTestcaseTestcasesDataTable();
	});
	}
	function callJtableCalenderFeature(calenderId){
		$( "#"+calenderId ).datepicker();
		$( "#"+calenderId ).focus();
		
		$("#"+calenderId).on("changeDate", function() {
		var date=document.getElementById(calenderId).value;
		$(".datepicker").hide();
		 $.ajax({
				type: "POST",
				url:'workpackage.allocateFeature.update?modifiedColumn=PlannedExecutionDate&modifiedValue='+date+'&rowId='+calenderId,
				success : function(data) {
					if(data.Result=="ERROR"){
			    		callAlert(data.Message);
			    		return false;
			    	}
				}
			});	
		 var status = $("#testBedfeaturestatus_ul").find('option:selected').val();
	 	  //listWorkPackageFeatureOfSelectedWorkPackage(status);//END - Delete - JTable
		  allocateTestcaseFeaturesDataTable();
	});
	}	
	
	function callJtableCalenderTestSuite(calenderId){
		$( "#"+calenderId ).datepicker();
		$( "#"+calenderId ).focus();

		
		$("#"+calenderId).on("changeDate", function() {
			
		var date=document.getElementById(calenderId).value;
		$(".datepicker").hide();
		 $.ajax({
				type: "POST",
				url:'workpackage.allocateTestSuite.update?modifiedColumn=PlannedExecutionDate&modifiedValue='+date+'&rowId='+calenderId,
				success : function(data) {
					if(data.Result=="ERROR"){
			    		callAlert(data.Message);
			    		return false;
			    	}
				}
			});	
			var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();				

	 	  //listWorkPackageTestSuiteOfSelectedWorkPackage(status);//END - Delete - JTable
		  allocateTestcaseTestsuitesDataTable();
	});
	}
function loadRecordsByFilter(){
	
	 var dcFilterId =  $("#dcFilterList_ul").find('option:selected').attr('id');
	 dcFilterId = (typeof dcFilterId == 'undefined') ? -1 : dcFilterId;
	 
	 var testLeadFilterId =  $("#testLeadFilterList_ul").find('option:selected').attr('id');
	 testLeadFilterId = (typeof testLeadFilterId == 'undefined') ? -1 : testLeadFilterId;
	 
     var testerFilterId =  $("#testerFilterList_ul").find('option:selected').attr('id');
     testerFilterId = (typeof testerFilterId == 'undefined') ? -1 : testerFilterId;
	 
     var envFilterId =  $("#envFilterList_ul").find('option:selected').attr('id');
     envFilterId = (typeof envFilterId == 'undefined') ? -1 : envFilterId;
     
     var localeFilterId =  $("#localeFilterList_ul").find('option:selected').attr('id');
     localeFilterId = (typeof localeFilterId == 'undefined') ? -1 : localeFilterId;
     
     var plannedExecutionDateFilter=datepickerFilter.value;	 
	var environmentId=document.getElementById("treeHdnCurrentEnvironmentId").value;
		 document.getElementById("filter").style.display = "block";
		 document.getElementById("envLocTS").style.display = "none";

			document.getElementById("envLoc").style.display = "none";
				loadExecutionPriorityList();
			  loadTesterList();
			  loadTestLeadList();
			  loadWorkShifts();
			  loadDCFilterList();
			  loadTestLeadFilterList();
			  loadTesterFilterList();
			  loadEnvFilterList();
			  loadLocaleFilterList();
			  if(dcFilterId ==null || dcFilterId=='undefined' || dcFilterId=='null'){
					dcFilterId=0;
				}
				if(testLeadFilterId ==null || testLeadFilterId=='undefined' || testLeadFilterId=='null'){
					testLeadFilterId=0;
				}
				if(testerFilterId ==null || testerFilterId=='undefined' || testerFilterId=='null'){
					testerFilterId=0;
				}
				if(envFilterId ==null || envFilterId=='undefined' || envFilterId=='null'){
					envFilterId=0;
				}
				if(localeFilterId ==null || localeFilterId=='undefined' || localeFilterId=='null'){
					localeFilterId=0;
				}
				if(plannedExecutionDateFilter=='Planned Execution Date')
					  plannedExecutionDateFilter='';
				var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				

			  if(environmentId==null || environmentId <=0 || environmentId == 'null')
				urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId="+testLeadFilterId+"&testerId="+testerFilterId+"&envId="+envFilterId+"&localeId="+localeFilterId+"&plannedExecutionDate="+plannedExecutionDateFilter+"&dcId="+dcFilterId+"&status="+status;
			  else
				urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.planbyenv?workPackageId='+workPackageId+'&envId='+environmentId+'&timeStamp='+timestamp;
			//listWorkPackageTestcasesOfSelectedWorkPackage(status);//END - Delete - JTable
			allocateTestcaseTestcasesDataTable();
}
function exportToExcel() {
	
	if(nodeType=='WorkPackage')
	{
		workPackageId = key;
		workPackageName = title;
	}else{
		callAlert("Please select Workpackage");
		return false;
	}
	
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var productName = document.getElementById("treeHdnCurrentProductName").value; 
	var url = "workpackage.testcaseplan.report?workPackageId="+ workPackageId +'&reportMode=XLS';
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
		$
			.blockUI({
				theme : true,
				title : 'Please Wait',
				message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
			});
		$
			.post(
				url,
				function(data) {						
				var fileName='';					
					if(data.Result=='Ok'){					
						fileName=data.exportFileName;
						fileName=fileName+"/WorkPackageTestCaseExecutionPlan/TFWF_WPTCEP-"+ productName + "-" + workPackageName+ ".xlsx";
							
					}else{
						callAlert(data.Message);
						$.unblockUI();
						return false;
					}											
					thediv.style.display = "";
					loadPopupEvidence(fileName);
					$.unblockUI();
				});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
	return false;
}

function loadPopupEvidence(urlId){
	var urlfinal="rest/download/evidence?fileName="+urlId;
  	parent.window.location.href=urlfinal;
  	
 }

function loadTesterList(plannedExecutionDate,shiftListId,workpackageId){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	$('#testerList_ul').empty();
	//$('#testerList_dd div').remove();
	$.post('administration.user.testerListExcludingTestLead?workPackageId='+workpackageId+"&plannedExecutionDate="+plannedExecutionDate+"&shiftId="+shiftListId,function(data) {
		var ary = (data.Options);
       	$.each(ary, function(index, element) {
			$('#testerList_ul').append('<option id="' + element.Number + '" ><a href="#">' + element.DisplayText + '</a></option>');	
  		});
	});
}

function loadTestLeadList(plannedExecutionDate,shiftListId,workpackageId){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var	workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	$('#testLeadList_ul').empty();
	//$('#testLeadList_dd div').remove();
	$.post('administration.user.testLeadListExcludingTester?workPackageId='+workpackageId+"&plannedExecutionDate="+plannedExecutionDate+"&shiftId="+shiftListId,function(data) {
		var ary = (data.Options);
       	$.each(ary, function(index, element) {
		$('#testLeadList_ul').append('<option id="' + element.Number + '" ><a href="#">' + element.DisplayText + '</a></option>');	
   		});
  	});
}

function loadDCFilterList(){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	$.post('administration.user.dcList?workPackageId='+workPackageId,function(data) {		

        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#dcFilterList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}

function loadTestLeadFilterList(){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	
	$.post('administration.user.testLeadList?workPackageId='+workPackageId+"&plannedExecutionDate=''&shiftId=0",function(data) {		

        var ary = (data.Options);
       	$.each(ary, function(index, element) {
		$('#testLeadFilterList_ul').append('<option id="' + element.Number + '" ><a href="#">' + element.DisplayText + '</a></option>');	
   		});
        
 		});
}

function loadTesterFilterList(){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	
	$.post('administration.user.testerList?workPackageId='+workPackageId+"&plannedExecutionDate=''&shiftId=0",function(data) {		

        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#testerFilterList_ul').append('<option id="' + element.Number + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}

function loadEnvFilterList(){
	var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	
	$.post('administration.workpackage.environment.mappedlist?workPackageId='+workPackageId,function(data) {		

        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#envFilterList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}

function loadLocaleFilterList(){
	var productId = document.getElementById("treeHdnCurrentProductId").value;
	
	$.post('administration.workpackage.locale.list?productMasterId='+productId,function(data) {		

        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#localeFilterList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}

function loadExecutionPriorityList(){
	//var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	$('#executionPriorityList_ul').empty();
	
	$.post('administration.executionPriorityList',function(data) {		

        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#executionPriorityList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
} 
var sortIds="0";
var sorthmIds="0";

function loadScrollbarForDropDown(){
	$('.dropdown').jScrollPane({showArrows: false,
	        					scrollbarWidth: 5,
	        					arrowSize: 5});
}
		
function saveWorkpackagePlanDetail(){
	var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var subTabActive = $("#giveAllocateId").find("label.active").attr("data-name");
	var subTabActiveIndex = $("#giveAllocateId").find("label.active").index();
	var wptcepListsFromUI = [];
	
	var $selectedRows = '';
	var $selectedFeatureRows = '';
	var $selectedTestSuiteRows = '';
	var tctype='';
	var featuretype='';
	var tstype='';
	if(subTabActiveIndex==2){
		$selectedRows = $('#jTableContainer').jtable('selectedRows');
		if($selectedRows.length == 0){
			callAlert("Please select test cases for bulk allocation");
		}else{
			$selectedRows.each(function () {
				var record = $(this).data('record');
				tctype=record.sourceTable;
			});
		}
	}
	if(subTabActiveIndex==0){
		$selectedFeatureRows = $('#jTableContainerFeature').jtable('selectedRows');
		if($selectedFeatureRows.length == 0){
			callAlert("Please select feature for bulk allocation");
		}else{
			$selectedFeatureRows.each(function () {
				var record = $(this).data('record');
				featuretype=record.sourceTable;
			});
		}
	}
	if(subTabActiveIndex==1){
		$selectedTestSuiteRows = $('#jTableContainerTestSuite').jtable('selectedRows');
		if($selectedTestSuiteRows.length == 0){
			callAlert("Please select test suite for bulk allocation");
		}else{
			$selectedTestSuiteRows.each(function () {
				var record = $(this).data('record');
				tstype=record.sourceTable;
			});
		}
	}
	
	if(tctype=='TestCase'){
		$selectedRows.each(function () {
		    var record = $(this).data('record');
		    var wptcepId = record.id;
		    wptcepListsFromUI.push(wptcepId);
		});
		
		 var plannedExecutionDate=datepickerbulk.value;
		 if(plannedExecutionDate=='Planned Execution Date')
			 plannedExecutionDate='';
		 
		 var tester=$("#testerList_dd .select2-chosen").text();
		 var testLead=$("#testLeadList_dd .select2-chosen").text();
		 var shift=$("#shiftList_dd .select2-chosen").text();
		 var priority=  $("#executionPriorityList_dd .select2-chosen").text();		 
		 
		var shiftId ="";
		if((shift!="-")&&(shift!="")){
			shiftId = $("#shiftList_ul").find('option:selected').attr('id');
		}else{
			shiftId=-1;
		}		
		 
		 var testerId="";
		 if((tester!="-")&&(tester!="")){
			 testerId= $("#testerList_ul").find('option:selected').attr('id');
		 }else{
			 testerId=-1;
		 }		
		
		var testLeadId="";
		if((testLead!="-")&&(testLead!="")){
			testLeadId = $("#testLeadList_ul").find('option:selected').attr('id');
		}else{
			testLeadId=-1;
		}
		
		var executionPriorityId ="";
		if(priority!="-"){
			executionPriorityId = $("#executionPriorityList_ul").find('option:selected').attr('id');
		}else{
			executionPriorityId=-1;
		}		
		
		if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
				callAlert("Please select the workPackage");
				return false;
			}else
		if(plannedExecutionDate!='' && (shiftId=='' || shiftId==null || shiftId=='undefined'))
		{
				 callAlert("Please select the Shift");
				 return false;
		}else
			if(wptcepListsFromUI.length == null || wptcepListsFromUI==''){
				callAlert("Select atleast one testcase","ok");
			}else if(plannedExecutionDate=='' && shiftId==-1 && testerId==-1&&	testLeadId==-1 &&	executionPriorityId ==-1){
				callAlert("Select atleast one Option","ok");
			}			
			else{		
				
			$.post('workpackage.testcaseplan.update.bulk?testerId='+testerId+'&testLeadId='+testLeadId+'&wptcepListsFromUI='+wptcepListsFromUI+'&plannedExecutionDate='+plannedExecutionDate+'&executionPriorityId='+executionPriorityId+'&shiftId='+shiftId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();
				   allocateTestCase(nodeType);				
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				});
			}
	}else if(featuretype=='Feature'){		
	
		$selectedFeatureRows.each(function () {
		    var record = $(this).data('record');
		    var wptcepId = record.id;
		    wptcepListsFromUI.push(wptcepId);
		});
		
		 var plannedExecutionDate=datepickerbulk.value;
		 if(plannedExecutionDate=='Planned Execution Date')
			 plannedExecutionDate='';
		 
		 var tester=$("#testerList_dd .select2-chosen").text();
		 var testLead=$("#testLeadList_dd .select2-chosen").text();
		 var shift=$("#shiftList_dd .select2-chosen").text();
		 var priority=  $("#executionPriorityList_dd .select2-chosen").text();		 
		 
		var shiftId ="";
		if((shift!="-")&&(shift!="")){
			shiftId = $("#shiftList_ul").find('option:selected').attr('id');
		}else{
			shiftId=-1;
		}		
		 
		 var testerId="";
		 if((tester!="-")&&(tester!="")){
			 testerId= $("#testerList_ul").find('option:selected').attr('id');
		 }else{
			 testerId=-1;
		 }
		
		var testLeadId="";
		if((testLead!="-")&&(testLead!="")){
			testLeadId = $("#testLeadList_ul").find('option:selected').attr('id');
		}else{
			testLeadId=-1;
		}
		
		var executionPriorityId ="";
		if(priority!="-"){
			executionPriorityId = $("#executionPriorityList_ul").find('option:selected').attr('id');
		}else{
			executionPriorityId=-1;
		}		
		
		if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
				callAlert("Please select the workPackage");
				return false;
			}else
		if(plannedExecutionDate!='' && (shiftId=='' || shiftId==null || shiftId=='undefined'))
		{
				 callAlert("Please select the Shift");
				 return false;
		}else
			if(wptcepListsFromUI.length == null || wptcepListsFromUI==''){
				callAlert("Select atleast one Feature","ok");
			}else if(plannedExecutionDate=='' && shiftId==-1 && testerId==-1&&	testLeadId==-1 &&	executionPriorityId ==-1){
				callAlert("Select atleast one Option","ok");
			}
			
			else{
				
			$.post('workpackage.featureplan.update.bulk?testerId='+testerId+'&testLeadId='+testLeadId+'&wptcepListsFromUI='+wptcepListsFromUI+'&plannedExecutionDate='+plannedExecutionDate+'&executionPriorityId='+executionPriorityId+'&shiftId='+shiftId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();				   
				var status = $("#testBedfeaturestatus_ul").find('option:selected').val();
				    //listWorkPackageFeatureOfSelectedWorkPackage(status);//END - Delete - JTable
					allocateTestcaseFeaturesDataTable();
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				});
			}
		}else if(tstype=='TestSuite'){		
	
		$selectedTestSuiteRows.each(function () {
		    var record = $(this).data('record');
		    var wptcepId = record.id;
		    wptcepListsFromUI.push(wptcepId);
		});
		
		 var plannedExecutionDate=datepickerbulk.value;
		 if(plannedExecutionDate=='Planned Execution Date')
			 plannedExecutionDate='';		 
		 
		 var tester=$("#testerList_dd .select2-chosen").text();
		 var testLead=$("#testLeadList_dd .select2-chosen").text();
		 var shift=$("#shiftList_dd .select2-chosen").text();
		 var priority=  $("#executionPriorityList_dd .select2-chosen").text();
		 	 
		var shiftId ="";
		if((shift!="-")&&(shift!="")){
			shiftId = $("#shiftList_ul").find('option:selected').attr('id');
		}else{
			shiftId=-1;
		}		
		 
		 var testerId="";
		 if((tester!="-")&&(tester!="")){
			 testerId= $("#testerList_ul").find('option:selected').attr('id');
		 }else{
			 testerId=-1;
		 }		
		
		var testLeadId="";
		if((testLead!="-")&&(testLead!="")){
			testLeadId = $("#testLeadList_ul").find('option:selected').attr('id');
		}else{
			testLeadId=-1;
		}
		
		var executionPriorityId ="";
		if(priority!="-"){
			executionPriorityId = $("#executionPriorityList_ul").find('option:selected').attr('id');
		}else{
			executionPriorityId=-1;
		}		
		
		if(workPackageId==null || workPackageId <=0 || workPackageId == 'null'){
				callAlert("Please select the workPackage");
				return false;
			}else
		if(plannedExecutionDate!='' && (shiftId=='' || shiftId==null || shiftId=='undefined'))
		{
				 callAlert("Please select the Shift");
				 return false;
		}else
			if(wptcepListsFromUI.length == null || wptcepListsFromUI==''){
				callAlert("Select atleast one Feature","ok");
			}else if(plannedExecutionDate=='' && shiftId==-1 && testerId==-1&&	testLeadId==-1 &&	executionPriorityId ==-1){
				callAlert("Select atleast one Option","ok");
			}
			
			else{		
				
			$.post('workpackage.testsuiteplan.update.bulk?testerId='+testerId+'&testLeadId='+testLeadId+'&wptcepListsFromUI='+wptcepListsFromUI+'&plannedExecutionDate='+plannedExecutionDate+'&executionPriorityId='+executionPriorityId+'&shiftId='+shiftId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();				   
						var status = $("#testBedtestsuitestatus_ul").find('option:selected').val();				

				    //listWorkPackageTestSuiteOfSelectedWorkPackage(status);//END - Delete - JTable
					allocateTestcaseTestsuitesDataTable();
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				});
			}
	}
}

// Demand starts

var resourcesCount = {'0': '0','1': '1','2': '2', '3': '3',	'4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9',
						'10': '10','11': '11','12': '12', '13': '13','14': '14', '15': '15', '16': '16', '17': '17', '18': '18', '19': '19',
						'20': '20','21': '21','22': '22', '23': '23','24': '24', '25': '25', '26': '26', '27': '27', '28': '28', '29': '29',
						'30': '30','31': '31','32': '32', '33': '33','34': '34', '35': '35', '36': '36', '37': '37', '38': '38', '39': '39',
						'40': '40','41': '41','42': '42', '43': '43','44': '44', '45': '45', '46': '46', '47': '47', '48': '48', '49': '49',
						'50': '50','51': '51','52': '52', '53': '53','54': '54', '55': '55', '56': '56', '57': '57', '58': '58', '59': '59',
						'60': '60','61': '61','62': '62', '63': '63','64': '64', '65': '65', '66': '66', '67': '67', '68': '68', '69': '69',
						'70': '70','71': '71','72': '72', '73': '73','74': '74', '75': '75', '76': '76', '77': '77', '78': '78', '79': '79',
						'80': '80','81': '81','82': '82', '83': '83','84': '84', '85': '85', '86': '86', '87': '87', '88': '88', '89': '89',
						'90': '90','91': '91','92': '92', '93': '93','94': '94', '95': '95', '96': '96', '97': '97', '98': '98', '99': '99',
						'100': '100'};
var weekDateNames = new Array();
var weekDateCompleteNames = new Array();
function getWeekDateNames(weekIncrement) {
	 var dateNames = new Array();
	 var completeDateNames = new Array();
	 var weekNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
	    $.ajax({
	            url : 'common.list.weekDateNames?weekNo='+weekNo,
	 			dataType : 'json',
	 			error: function() {
	 			      callAlert("Unable to get the dates for the week");
	 			},
	            success : function(jsonData) {
	        	   	for (var i = 0; i < jsonData.Records.length; i++) {
		             	var dateName = jsonData.Records[i];
		             	dateNames.push(dateName.option.substr(0, 6));
		             	completeDateNames.push(getDateFormat(dateName.option));
	        	   }
	               setCompleteWeekDateNames(completeDateNames);
	               setWeekDateNames(dateNames);
	          }
	    });
};

function getDateFormat(date){
	 var dt = new Date(date);
	  var yr = dt.getYear() + 1900;
	  var mn = dt.getMonth() + 1;
	  var dtValue =  dt.getDate();
	  if(mn.toString().length == 1){
		  mn =  "0"+mn;
	  }
	  if(dtValue.toString().length == 1){
		  dtValue =  "0"+dtValue;
	  }
	  date=yr + "-" + mn + "-" + dtValue;	 
	  return date;
}

function setWeekDateNames(dateNames){
	weekDateNames = dateNames;
	showWorkPackageWeeklyResourceDemandTables();
}

function setCompleteWeekDateNames(completeDateNames){
	weekDateCompleteNames = completeDateNames;
}

function showWorkPackageWeeklyResourceDemandTables() {
	showWorkPackageResourceDemandOfSelectedWorkPackageTable();
	showWorkPackageWeeklyPlanTable();
}

function showWorkPackageResourceDemandOfSelectedWorkPackageTable(){
	 try{
		if ($('#jTableContainerWorkPackageDemand').length>0) {
			 $('#jTableContainerWorkPackageDemand').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerWorkPackageDemand').jtable({

		title: 'Create/Edit resource demand for executing testcases',
		selecting : true, //Enable selecting 
		editinline : {enable : true},
		gotopageArea : 'none',
		pageSizeChangeArea : false,
		toolbar : {
			items : [ 
			{
				text : '   <<   ',
				click : function() {
					showDemandForPreviousWeek();
				}
			},
			{	
				text : '   >>   ',
				click : function() {
					showDemandForNextWeek();
				}
			}
			]
		},
		
		actions : {
			listAction : urlForWorkPackageDemandprojection,
			//createAction: 'administration.device.add',
			//updateAction : 'workpackage.demand.projection.update'
		    //deleteAction: 'administration.device.delete'
		},
		
		fields : {			
			/* name:{
				title: 'Demand Projection Id',
	       		 edit: false,
	       		 list:true
				}, */
			
			 wpDemandProjectionId: { 
       		 title: 'Demand Projection Id',
       		 edit: false,
       		 list:false,
       		 key: true
       	},
       	weekNo: {
            title: 'WeekNo',
            width: "12%",
            edit: false,
            list:false
	   	},
       	shiftId: {
                title: 'Shift Id',
                width: "12%",
                edit: false,
                list:false,
                 display: function (data) { 
   				 return data.record.shiftId;
                },
       	},
       	shiftName: {
                title: 'Shift Name',
                width: "12%",
                edit: false,
                 display: function (data) { 
   				 return data.record.shiftName;
                },
       	},
       	day1ResourceCount: {
                title: weekDateNames[0],
                width: "12%",
                edit: true,
                display: function (data) { 
                	 var currentDemandShiftFieldName = "currentDemandShiftName0"+data.record.shiftId;
                	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
					 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
     				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+0+');>'+data.record.day1ResourceCount+'</a>');
               }
       	},
       	day2ResourceCount: {
            title: weekDateNames[1],
            width: "12%",
            edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName1"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
 				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+1+');>'+data.record.day2ResourceCount+'</a>');
           }
	   	},
	   	day3ResourceCount: {
	        title: weekDateNames[2],
	        width: "12%",
	        edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName2"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
 				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+2+');>'+data.record.day3ResourceCount+'</a>');
           }
		},
		day4ResourceCount: {
	        title: weekDateNames[3],
	        width: "12%",
	        edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName3"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
            	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+3+');>'+data.record.day4ResourceCount+'</a>');
           }
		},
		day5ResourceCount: {
	        title: weekDateNames[4],
	        width: "12%",
	        edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName4"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
            	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+4+');>'+data.record.day5ResourceCount+'</a>');
           }
		},
		day6ResourceCount: {
	        title: weekDateNames[5],
	        width: "12%",
	        edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName5"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
            	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+5+');>'+data.record.day6ResourceCount+'</a>');
           }
		},
		day7ResourceCount: {
	        title: weekDateNames[6],
	        width: "12%",
	        edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName6"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
            	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemand('+data.record.workPackageId+','+data.record.shiftId+','+6+');>'+data.record.day7ResourceCount+'</a>');
           }
		}, 
	},
	});
	$('#jTableContainerWorkPackageDemand').jtable('load',{
				workPackageName : $('#workPackageName_dd')
						.children('div').attr('id')
			});
	var jscrolheight = $("#jTableContainerWorkPackageDemand").height();
	var jscrolwidth = $("#jTableContainerWorkPackageDemand").width();

	$(".jScrollContainer").on(
			"scroll",
			function() {
				var lastScrollLeft = 0;

				var documentScrollLeft = $(".jScrollContainer")
						.scrollLeft();

				if (lastScrollLeft < documentScrollLeft) {
					$("#jTableContainerWorkPackageDemand").width(
							$(".jtable").width()).height(
							$(".jtable").height());
					lastScrollLeft = documentScrollLeft;
				} else if (lastScrollLeft >= documentScrollLeft) {
					$("#jTableContainerWorkPackageDemand").width(jscrolwidth)
							.height(jscrolheight);
				}
			}); 
}


//start1

function showWorkPackageResourceReservationOfSelectedWorkPackageTableWeekly(){
	 try{
		if ($('#jTableContainerWorkPackageWeeklyResourceReservation').length>0) {
			 $('#jTableContainerWorkPackageWeeklyResourceReservation').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerWorkPackageWeeklyResourceReservation').jtable({

		title: 'Create/Edit resource Resource Reservation for executing testcases Weekly ',
		selecting : true, //Enable selecting 
		editinline : {enable : true},
		gotopageArea : 'none',
		pageSizeChangeArea : false,
		toolbar : {
			items : [ 
			{
				text : '   <<   ',
				click : function() {
					showDemandForPreviousWeek();
				}
			},
			{	
				text : '   >>   ',
				click : function() {
					showDemandForNextWeek();
				}
			}
			]
		},
		
		actions : {
			listAction : urlToGetResourceReservarionForWeekly,
		},
		
		fields : {			
			
			testFactoryResourceReservationId: { 
   		 title: 'Resource Reservation Id',
   		 edit: false,
   		 list:false,
   		 key: true
   	},
   	reservationWeek: {
        title: 'WeekNo',
        width: "12%",
        edit: false,
        list:false
	   	},
	   	
	   	shiftName:{
	   		title: 'Shift',
	           width: "12%",
	           edit: false,
	           list:true
	   	},
	   	
	   	workPackageName:{
	   		title: 'Workpackage',
	           width: "12%",
	           edit: false,
	           list:true
	   	},
	   	
	   	userName: {
	           title: 'userName',
	           width: "12%",
	           edit: false,
	           list:true
		   	},
	   	
		   	userRoleName: {
		           title: 'Role',
		           width: "12%",
		           edit: false,
		           list:true
			   	},
			   	
			   	
	   	
   	week1: {
            title: weeksName[0],
            width: "12%",
            edit: true,
            display: function (data) { 
            	 var currentDemandShiftFieldName = "currentDemandShiftName0"+data.record.shiftId;
            	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
					 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
 				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+0+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week1+'</a>');
           }
   	},
   	week2: {
        title: weeksName[1],
        width: "12%",
        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName1"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+1+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week2+'</a>');
       }
	   	},
	   	week3: {
	        title: weeksName[2],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName2"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
				return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+2+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week3+'</a>');
       }
		},
		week4: {
	        title: weeksName[3],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName3"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+3+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week4+'</a>');
       }
		},
		week5: {
	        title: weeksName[4],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName4"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+4+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week5+'</a>');
       }
		},
		week6: {
	        title: weeksName[5],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName5"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+5+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week6+'</a>');
       }
		},
		week7: {
	        title: weeksName[6],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName6"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+6+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week7+'</a>');
       }
		}, 
		
		week8: {
	        title: weeksName[7],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName7"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+7+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week8+'</a>');
       }
		}, 
		
		week9: {
	        title: weeksName[8],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName8"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+8+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week9+'</a>');
       }
		}, 
		
		week10: {
	        title: weeksName[9],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName9"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+9+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week10+'</a>');
       }
		}, 
		week11: {
	        title: weeksName[10],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName10"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+10+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week11+'</a>');
       }
		}, 
		
		week12: {
	        title: weeksName[11],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName11"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+11+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week12+'</a>');
       }
		}, 
		
		week13: {
	        title: weeksName[12],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName12"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+12+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week13+'</a>');
       }
		}, 
		week14: {
	        title: weeksName[13],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName13"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+13+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week14+'</a>');
       }
		}, 
		
		
		week15: {
	        title: weeksName[14],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName14"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+14+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week15+'</a>');
       }
		}, 
		
		week16: {
	        title: weeksName[15],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName15"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+15+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week16+'</a>');
       }
		}, 
		
		week17: {
	        title: weeksName[16],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName16"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+16+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week17+'</a>');
       }
		}, 
		
		week18: {
	        title: weeksName[17],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName17"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+17+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week18+'</a>');
       }
		}, 
		
		week19: {
	        title: weeksName[18],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName18"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+18+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week19+'</a>');
       }
		}, 
		
		week20: {
	        title: weeksName[19],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName19"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+19+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week20+'</a>');
       }
		}, 
		
		week21: {
	        title: weeksName[20],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName20"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+20+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week21+'</a>');
       }
		}, 
		
		week22: {
	        title: weeksName[21],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName21"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+21+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week22+'</a>');
       }
		}, 
		
		week23: {
	        title: weeksName[22],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName22"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+22+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week23+'</a>');
       }
		}, 
		
		week24: {
	        title: weeksName[23],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName23"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+23+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week24+'</a>');
       }
		}, 
		
		week25: {
	        title: weeksName[24],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName24"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+24+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week25+'</a>');
       }
		}, 
		
		week26: {
	        title: weeksName[25],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName25"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+25+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week26+'</a>');
       }
		}, 
		
		week27: {
	        title: weeksName[26],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName26"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+26+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week27+'</a>');
       }
		}, 
		
		week28: {
	        title: weeksName[27],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName27"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+27+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week28+'</a>');
       }
		}, 
		
		week29: {
	        title: weeksName[28],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName28"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+28+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week29+'</a>');
       }
		}, 
		
		week30: {
	        title: weeksName[29],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName29"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+29+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week30+'</a>');
       }
		}, 
		
		week31: {
	        title: weeksName[30],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName30"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+30+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week31+'</a>');
       }
		}, 
		
		week32: {
	        title: weeksName[31],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName31"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+31+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week32+'</a>');
       }
		}, 
		
		week33: {
	        title: weeksName[32],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName32"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+32+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week33+'</a>');
       }
		}, 
		
		week34: {
	        title: weeksName[33],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName33"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+33+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week34+'</a>');
       }
		}, 
		
		week35: {
	        title: weeksName[34],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName34"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+34+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week35+'</a>');
       }
		}, 
		
		week36: {
	        title: weeksName[35],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName35"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+35+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week36+'</a>');
       }
		}, 
		
		week37: {
	        title: weeksName[36],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName36"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+36+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week37+'</a>');
       }
		}, 
		
		week38: {
	        title: weeksName[37],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName37"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+37+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week38+'</a>');
       }
		}, 
		
		week39: {
	        title: weeksName[38],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName38"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+38+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week39+'</a>');
       }
		}, 
		
		week40: {
	        title: weeksName[39],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName39"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+39+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week40+'</a>');
       }
		}, 
		
		week41: {
	        title: weeksName[40],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName40"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+40+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week41+'</a>');
       }
		}, 
		
		week42: {
	        title: weeksName[41],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName41"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+41+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week42+'</a>');
       }
		}, 
		
		week43: {
	        title: weeksName[42],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName42"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+42+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week43+'</a>');
       }
		}, 
		
		week44: {
	        title: weeksName[43],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName43"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+43+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week44+'</a>');
       }
		}, 
		
		week45: {
	        title: weeksName[44],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName44"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+44+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week45+'</a>');
       }
		}, 
		
		week46: {
	        title: weeksName[45],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName45"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+45+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week46+'</a>');
       }
		}, 
		
		week47: {
	        title: weeksName[46],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName46"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+46+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week47+'</a>');
       }
		}, 
		
		week48: {
	        title: weeksName[47],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName47"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+47+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week48+'</a>');
       }
		}, 
		
		week49: {
	        title: weeksName[48],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName48"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+48+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week49+'</a>');
       }
		}, 
		
		week50: {
	        title: weeksName[49],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName49"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+49+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week50+'</a>');
       }
		}, 
		
		week51: {
	        title: weeksName[50],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName50"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+50+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week51+'</a>');
       }
		}, 
		
		week52: {
	        title: weeksName[51],
	        width: "12%",
	        edit: true,
        display: function (data) { 
        	 var currentDemandShiftFieldName = "currentDemandShiftName51"+data.record.shiftId;
        	 $("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				 document.getElementById(currentDemandShiftFieldName).value = data.record.shiftName;
        	return $('<a style="color: #0000FF;" href=javascript:raiseResourceDemandForWeekly('+data.record.workPackageId+','+data.record.shiftId+','+51+','+data.record.skillId+','+data.record.userRoleId+');>'+data.record.week52+'</a>');
        	
        	
       }
		}, 
		
		
		
	},
	});
	$('#jTableContainerWorkPackageWeeklyResourceReservation').jtable('load',{
				workPackageName : $('#workPackageName_dd')
						.children('div').attr('id')
			});
	var jscrolheight = $("#jTableContainerWorkPackageWeeklyResourceReservation").height();
	var jscrolwidth = $("#jTableContainerWorkPackageWeeklyResourceReservation").width();

	$(".jScrollContainer").on(
			"scroll",
			function() {
				var lastScrollLeft = 0;

				var documentScrollLeft = $(".jScrollContainer")
						.scrollLeft();

				if (lastScrollLeft < documentScrollLeft) {
					$("#jTableContainerWorkPackageWeeklyResourceReservation").width(
							$(".jtable").width()).height(
							$(".jtable").height());
					lastScrollLeft = documentScrollLeft;
				} else if (lastScrollLeft >= documentScrollLeft) {
					$("#jTableContainerWorkPackageWeeklyResourceReservation").width(jscrolwidth)
							.height(jscrolheight);
				}
			}); 
}




//end1


function isValidWorkPackageStatus(workPackageId,shiftId, index){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=RaiseDemand',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			raiseResourceDemandForWorkPackage(workPackageId, shiftId, index);
   		}
	});
}

function isValidWorkPackageStatusForWeekly(workPackageId,shiftId, workWeek,skillId,roleId,userTypeId){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=RaiseDemand',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			raiseResourceDemandForWorkPackageForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId);
   		}
	});
}





function raiseResourceDemand(workPackageId, shiftId, index){
	
	 var currentDemandShiftFieldName = "currentDemandShiftName"+index+shiftId;
	 var demandForShift = document.getElementById(currentDemandShiftFieldName).value;
	 document.getElementById("currentDemandShiftName").value = demandForShift;
	 isValidWorkPackageStatus(workPackageId, shiftId, index);
}

function raiseResourceDemandForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId){
	if(skillId == null){
		skillId = 0;
	}
	if(roleId == null){
		roleId= 0;
	}
	if(userTypeId == null){
		userTypeId= 0;
	}
	selectedDemandDetails = {
		'workPackageId' : workPackageId,
		'shiftId' : shiftId,
		'workWeek' : workWeek,
		'skillId' : skillId,
		'roleId' : roleId,
		'userTypeId' : userTypeId
	};

//	 var currentDemandShiftFieldName = "currentDemandShiftName"+index+shiftId;
//	 var demandForShift = document.getElementById(currentDemandShiftFieldName).value;
//	 document.getElementById("currentDemandShiftName").value = demandForShift;
	if(demandRecursive == 0){
		demandRecursive = workWeek;
		$("#demandRecursive").val(demandRecursive);
	} 
	isValidWorkPackageStatusForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId);
}



function raiseResourceDemandForWorkPackage(workPackageId, shiftId, index){
	var curDemandShiftName = document.getElementById("currentDemandShiftName").value;
	var cWPackgeName = document.getElementById("cworkPackageName").value;		
	var dateToRaiseDemand = weekDateCompleteNames[index];
	var titleName = popupTitle_RaiseResourceDemandFor+" "+cWPackgeName+" for Date: "+dateToRaiseDemand+" for Shift: "+curDemandShiftName;
    urlToRaiseSkillBasedDemand = 'workpackage.demand.projection.add?workPackageId='+workPackageId+"&shiftId="+shiftId+"&workDate="+setJtableFormattedDate(dateToRaiseDemand);
    urlToListSkillBasedDemand='workpackage.skill.demand.projection.list.by.date?workPackageId='+workPackageId+"&shiftId="+shiftId+"&workDate="+setJtableFormattedDate(dateToRaiseDemand);
    urlToUpdateSkillBasedDemand='workpackage.demand.projection.skill.update',
    urlToDeleteSkillBasedDemand='workpackage.demand.projection.delete';
    
    divPopupMainTitle(titleName);
    
    try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
	
    $('#JTable_Allocate').jtable({
		title: 'Raise Resource Demand',
        selecting: true,  //Enable selecting 
        editinline : {enable : true},
        actions: {
       	 	createAction: urlToRaiseSkillBasedDemand,
       	 	listAction: urlToListSkillBasedDemand,
       	    editinlineAction:urlToUpdateSkillBasedDemand,
       	 	deleteAction:urlToDeleteSkillBasedDemand,       	 	
        },          
        recordUpdated:function(event, data){
        	reloadJTableAllocateFlag=true;
		},
        recordAdded: function(event, data) {
        	reloadJTableAllocateFlag=true;	 
        },
        recordDeleted: function(event, data) {
        	reloadJTableAllocateFlag=true;	 
        },
        fields : {
       	 wpDemandProjectionId: {
                 key: true,
                 list: false
             },
           workPackageId: {
                 title: 'Work Package Id',
                 create:false,
                 edit:false,
                 list:false,
                 width: "10%"
 			},
           workPackageName: {
                 title: 'Work Package',
                 create:false,
                 edit:false,
                 list:true,
                 width: "10%"
 			},
 			workDate: {
 				title: 'Work Date',
                create:false,
                edit:false,
                list:true,
                width: "10%",
                display:function(data){
                	return "<span>"+setJtableFormattedDate(data.record.workDate)+"</span>";
                }
 			},
 			shiftId:{
			   title: 'Shift Id',
			   create:false,
              edit:false,
              list:false,
              width: "5%"
			},
 			shiftName:{
				title: 'Shift',
				create:false,
              	edit:false,
              	list:true,
              	width: "5%"
 			},
 			userRoleId:{
				title: 'Role',
              	edit:false,
              	list:true,
              	width: "5%",
              	options:'list.roles.for.demand.projection'
			},
 			skillId:{
				title: 'Skill',
				create:true,
              	edit:false,
              	list:true,
              	width: "10%",
              	options: 'common.list.skill.list?skillIdfromUI=1',
			}, 
 			
 			skillandRoleBasedresourceCount:{
				       title: 'Resource',
				       create:true,
	                   edit:true,
	                   list:true,
	                   width: "5%"
	  		 },  			
        	}, formSubmitting: function (event, data) {
                data.form.find('input[name="skillandRoleBasedresourceCount"]').addClass('validate[required,custom[onlyNumberSp]]'); 
                data.form.validationEngine();
                return data.form.validationEngine('validate');
           },  
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }
		});
	$('#JTable_Allocate').jtable('load');	
}

function showWorkPackageWeeklyPlanTable(){
	try{
		if ($('#jTableWorkPackageWeeklyPlanContainer').length>0) {
			 $('#jTableWorkPackageWeeklyPlanContainer').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableWorkPackageWeeklyPlanContainer').jtable({

		title: 'Ref : Test Cases planned for execution during the week',
		selecting : true, //Enable selecting 
		editinline : {enable : true},
		gotopageArea : 'none',
		pageSizeChangeArea : false,
		actions : {
			listAction : urlForWorkPackageWeeklyPlan
			//createAction: 'administration.device.add',
			//updateAction : 'workpackage.demand.projection.update'
		    //deleteAction: 'administration.device.delete'
		},
		
		fields : {
			dailyPlanId: { 
       		 title: 'Weekly Plan 1',
       		 edit: false,
       		 list:false,
       		 key: true
       	},
       	weekNo: {
            title: 'WeekNo',
            width: "12%",
            edit: false,
            list:false,            
	   	},
       	shiftId: {
                title: 'Shift Id',
                width: "12%",
                edit: false,
                list:false,
                 display: function (data) { 
   				 return data.record.shiftId;
                },
       	},
       	shiftName: {
                title: 'Shift Name',
                width: "12%",
                edit: false,
                 display: function (data) { 
   				 return data.record.shiftName;
                },
       	},
       	day1ResourceCount: {
                title: weekDateNames[0],
                width: "12%",
                edit: false
       	},
       	day2ResourceCount: {
            title: weekDateNames[1],
            width: "12%",
            edit: false
	   	},
	   	day3ResourceCount: {
	        title: weekDateNames[2],
	        width: "12%",
	        edit: false
		},
		day4ResourceCount: {
	        title: weekDateNames[3],
	        width: "12%",
	        edit: false
		},
		day5ResourceCount: {
	        title: weekDateNames[4],
	        width: "12%",
	        edit: false
		},
		day6ResourceCount: {
	        title: weekDateNames[5],
	        width: "12%",
	        edit: false
		},
		day7ResourceCount: {
	        title: weekDateNames[6],
	        width: "12%",
	        edit: false
		},
	},
	});
	$('#jTableWorkPackageWeeklyPlanContainer').jtable(
			'load',
			{
				workPackageName : $('#workPackageName_dd')
						.children('div').attr('id')

			});

	var jscrolheight = $("#jTableWorkPackageWeeklyPlanContainer").height();
	var jscrolwidth = $("#jTableWorkPackageWeeklyPlanContainer").width();

	$(".jScrollContainer").on(
			"scroll",
			function() {
				var lastScrollLeft = 0;

				var documentScrollLeft = $(".jScrollContainer")
						.scrollLeft();

				if (lastScrollLeft < documentScrollLeft) {
					$("#jTableWorkPackageWeeklyPlanContainer").width(
							$(".jtable").width()).height(
							$(".jtable").height());
					lastScrollLeft = documentScrollLeft;
				} else if (lastScrollLeft >= documentScrollLeft) {
					$("#jTableWorkPackageWeeklyPlanContainer").width(jscrolwidth)
							.height(jscrolheight);
				}
			});
}

function showDemandForPreviousWeek() {
	
	var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var workPackageName = document.getElementById("treeHdnCurrentWorkPackageName").value;
  	var wNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
  	var previousWeekNo = wNo-1;
	var productName = document.getElementById("treeHdnCurrentProductName").value;
	document.getElementById("currentWorkPackageResourceDemandWeekNo").value=previousWeekNo;
    urlForWorkPackageDemandprojection = 'workpackage.demand.projection.list?workPackageId='+workPackageId+"&weekNumber="+previousWeekNo;
	urlForWorkPackageWeeklyPlan = 'workpackage.executionplan.weekly?workPackageId='+workPackageId+"&weekNo="+previousWeekNo;
	
	getWeekDateNames(1);
	//alert("New URL : " + urlForWorkPackageDemandprojection);
	return false;
}
	
function showDemandForNextWeek() {
	
	var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var workPackageName = document.getElementById("treeHdnCurrentWorkPackageName").value;
  	var wNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
  	var nextWeekNo = wNo-(-1);
	var productName =document.getElementById("treeHdnCurrentProductName").value;
	document.getElementById("currentWorkPackageResourceDemandWeekNo").value=nextWeekNo;
	urlForWorkPackageDemandprojection = 'workpackage.demand.projection.list?workPackageId='+workPackageId+"&weekNumber="+nextWeekNo;
	urlForWorkPackageWeeklyPlan = 'workpackage.executionplan.weekly?workPackageId='+workPackageId+"&weekNo="+nextWeekNo;
	getWeekDateNames(-1);
	return false;
}
//Demand ends 
$(document).on("click","[attr^=shift_]", function(){alert($(this).attr("shift_name"))});
//Blocked starts
function listResourceDemandOfWorkPackage(){
	try{
		if ($('#jTableContainerDemand').length>0) {
			 $('#jTableContainerDemand').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerDemand').jtable({
        title: 'Work Package Resource Plan',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
        actions: {
       	 	listAction: urlToGetResourceDemandOfWorkPackage,
        },
       // recordsLoaded:function(){
        	
       // },
        fields : {
        		workPackageResourceReservationId: {
	                 key: true,
	                 list: false,
	                 create: false
	             },	
	             productId:{
	                 title: 'Product Id',
	            	 list:false,
	            	 hidden: true
	             },
	             productName:{
	                 title: 'Product',
	            	 list:true,
	                 width:"10%"
	             },
	             workPackageId:{
	                 title: 'Work Package Id',
	            	 list:false,
	            	 hidden: true
	             },
	             workPackageName:{
	                 title: 'Work Package',
	            	 list:true,
	                 width:"10%"
	             },
	             dateOfCount:{
	                 title: 'Reservation Date',
	            	 list:true,
	            	 type: 'date',
	                 width:"10%"
	             },
	             shiftId:{
	                 title: 'Shift Id',
	            	 list:false,
	            	 hidden: true
	             },
	             shiftName:{
	                 title: 'Shift',
	            	 list:true,
	                 width:"10%"
	             },
	             demandId:{
	                 title: 'Demand Id',
	            	 list:false,
	            	 hidden: true
	             },
	             resourceDemandCount:{
	                 title: 'Demand',
	            	 list:true,
	                 width:"8%",
	                 display: function (data) { 
	      				return $('<a style="color: #0000FF;" href=javascript:viewResourceDemand('+data.record.workPackageId+','+data.record.shiftId+');>'+data.record.resourceDemandCount+'</a>');
	                }
	             },
	             availableCoreResourceCount:{
	                 title: 'Booked Resources',
	            	 list:true,
	                 width:"10%",
	                  display: function (data) { 
	      				return $('<a style="color: #0000FF;" href=javascript:viewAvailableResourcesDetails('+data.record.workPackageId+','+data.record.shiftId+');>'+data.record.availableCoreResourceCount+'</a>');
	                } 
	             }, 
	             blockedResourceCount:{
	                 title: 'Reserved',
	            	 list:true,
	                 width:"8%",
	                 display: function (data) {
	                       return $('<a style="color: #0000FF;" href=javascript:loadBlockedResourceDetails('+data.record.workPackageId+','+data.record.shiftId+');>' + data.record.blockedResourceCount + '</a>');
	                 }
	             },
	             gapInResourceCount:{
	                 title: 'Gap',
	            	 list:true,
	                 width:"10%",
	                 display: function (data) {
	                	 var currentShiftFieldName = "currentShiftName"+data.record.workPackageResourceReservationId;
	                	 $("#hdnDiv").append("<input type='hidden' id=currentWorkPackageName></input> <input type='hidden' id="+currentShiftFieldName+"></input>");
						 document.getElementById("currentWorkPackageName").value = data.record.workPackageName;
						 //document.getElementById(currentShiftFieldName).value = data.record.shiftName;
	                	 return $('<a style="color: RED;" href=javascript:getResourceForBlockingGridView('+data.record.workPackageResourceReservationId+','+data.record.workPackageId+','+data.record.shiftId+','+data.record.resourceDemandCount+');>' + data.record.gapInResourceCount + '</a>'); 
	                 }
	             },
       		}, 

		});
	 $('#jTableContainerDemand').jtable('load');	 
}

function viewResourceDemand(workPackageId, shiftId){	
	divPopupMainTitle(popupTitle_RaiseResourceDemand);
	
 	try{
		if ($('#JTable_Allocate').length>0) {
			 $('#JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
			    
	   urlToListSkillBasedDemand='workpackage.demand.projection.list.by.date?workPackageId='+workPackageId+"&shiftId="+shiftId+"&workDate="+datepickerRP.value;
		
	    $('#JTable_Allocate').jtable({
			title: "Raise Resource Demand",
	        selecting: true,  //Enable selecting 
	        editinline : {enable : true},
	        actions: {
	       	 	listAction: urlToListSkillBasedDemand
	        },  
	        fields : {
	       	 wpDemandProjectionId: {
	                 key: true,
	                 list: false
	             },
	           workPackageId: {
	                 title: 'Work Package Id',
	                 create:false,
	                 edit:false,
	                 list:false,
	                 width: "5%"
	 			},
	           workPackageName: {
	                 title: 'Work Package',
	                 create:false,
	                 edit:false,
	                 list:true,
	                 width: "10%"
	 			},
	 			workDate: {
	 				title: 'Work Date',
	                create:false,
	                edit:false,
	                list:true,
	                width: "10%"
	 			},
	 			shiftId:{
				   title: 'Shift Id',
				   create:false,
	              edit:false,
	              list:false,
	              width: "5%"
				},
	 			shiftName:{
					title: 'Shift',
					create:false,
	              	edit:false,
	              	list:true,
	              	width: "5%"
	 			},
	 			skillId:{
					title: 'Skill',
					create:true,
	              	edit:false,
	              	list:true,
	              	width: "10%",
	              	options: 'common.list.skill.list?skillIdfromUI='+0,
				}, 
	 			userRoleId:{
					title: 'Role',
	              	edit:false,
	              	list:true,
	              	width: "5%",
	              	options:'list.roles.for.demand.projection'
				},
	 			skillandRoleBasedresourceCount:{
					       title: 'Resource',
					       create:true,
		                   edit:true,
		                   list:true,
		                   width: "5%"
		  			},
	        	}
			});
		$('#JTable_Allocate').jtable('load');		
}

function getResourceForBlocking(workPackageId, shiftId, resourceDemandCount){
	availablilityType = "Booked";
	var reservationDate = datepickerRP.value;
	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	currentShiftName = document.getElementById("currentShiftName").value;
	document.getElementById("currentWorkPackage").value = workPackageId;
	document.getElementById("currentReservationShift").value = shiftId;
	document.getElementById("currentReservationDate").value = datepickerRP.value;
	document.getElementById("currentResourceDemandCount").value = resourceDemandCount;
	getResourcesForReservation(currentWorkPackageName,workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType);
}

function getResourcesForReservation(currentWorkPackageName,workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType){	
	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	currentShiftName = document.getElementById("currentShiftName").value;
	var details = currentWorkPackageName+" for Date: "+reservationDate ;
	var date = new Date();
    var timestamp = date.getTime();
	urlToGetResourcesOfWorkPackage = "workPackage.block.resources?workPackageId="+workPackageId+"&shiftId="+shiftId+"&resourceDemandForDate="+reservationDate+"&filter="+availablilityType+"&timestamp="+timestamp;
	$("#div_PopupMain").find("h4").text("Reserve Resources for WP : "+ details);
 	
	try{
		 if ($('#div_Table3').length>0) {
			$('#div_Table3').jtable('destroy');					 
		}
		
	} catch(e) {}
	
	$('#div_Table3').jtable({		
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        editinline : {enable : true},
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetResourcesOfWorkPackage,
       	 editinlineAction:'workpackage.block.resources.for.day.shift?shiftId='+shiftId+'&workpackageId='+workPackageId+'&blockResourceForDate='+reservationDate+'&demand='+resourceDemandCount
        },  
        fields : {
        	 userId: {
                 key: true,
                 list: false
             },
			
 			loginId: {
                 title: 'User',
                 inputTitle: 'LoginId <font color="#efd125" size="4px">*</font>',  
                 edit:false,
                 list:true,
                 width: "10%",
				  display:function(data){
					      if(data.record.productCoreResource=="Core"){
								return data.record.loginId+' <img src="css/images/core.png" title="Core" />';
						  }else{
								return data.record.loginId;
						  }
					 
				 },
            
 			},
 			userRoleLabel:{
 				title: 'Role',
                  edit:false,
                  list:true,
                  width: "5%",
 			},
 			skillName:{
 				title: 'Skill',
                  edit:false,
                  list:true,
                  width: "10%",
 			},
 			available:{
 				title: 'Is Available',
                  edit:false,
                  list:true,
                  width: "5%",
 			},
 			booked:{
 				title: 'Is Booked',
                  edit:false,
                  list:true,
                  width: "5%",
 			},
 			timeSheetHours:{
 				title: 'Remaining Time Sheet Hour',
                  edit:false,
                  list:true,
                  width: "5%",
                  display: function (data) {
					  var timeHrs = "40";                	  
                	  if(data.record.timeSheetHours == null){
                		  return timeHrs;
                	  }else{
                		  return data.record.timeSheetHours;
                	  }
                  }
 			},
 			bookedHrs:{
 				title: 'Remaining Shift Hour',
                  edit:false,
                  list:true,
                  width: "5%",
                  display: function (data) {
					  var bookingShiftHrs = "40";                	  
                	  if(data.record.bookedHrs == null){
                		  return bookingShiftHrs;
                	  }else{
                		  return data.record.bookedHrs;
                	  }
                  }
 			},
 			reserve:{
 				title : 'Reserve',
                edit: true,
                width: "5%",
                type : 'checkbox',
                values: {'1' : 'Yes','0' : 'No'}
 			},
 			reservationDetails:{
 				title : 'Reservation Details',
                edit: false,
 			},
        	},   
        	recordsLoaded: function(event, data) {
   			 var $selectedRows = $('#div_Table3').jtable('selectedRows');
   			 if($selectedRows >=0)
   			 {
   				 $("#div_Table3").find(".jtable-selecting-column > input").prop("checked", true);
   			 }
  		 },
		});
	$('#div_Table3').jtable('load');	
     loadPopup("div_PopupMain");
}

function initializeView()
{
	availablilityType = "Booked"; 
 	currentView = "Grid";
}

function resetTabSelection()
{	
	$("#availabilityFilter>div").find("label:first").addClass("active").siblings().removeClass("active");
	$('#girdViewRadio').attr('checked',true);
}

function getResourceForBlockingGridView(id,workPackageId, shiftId, resourceDemandCount){	 
	 $("#currentShiftName").val($("#currentShiftName" +id).val());
	isValidWorkPackageForReservation(workPackageId, shiftId, resourceDemandCount);
}

var selectedDetails = {};
function getResourceForBlockingGridViewForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId, workPackageName, workWeekName, userType, userRole, userSkill){
	if(skillId == null){
		skillId = 0;
	}
	if(userRoleId == null){
		userRoleId= 0;
	}
	if(userTypeId == null){
		userTypeId= 0;
	}
	selectedDetails = {};
	selectedDetails["selectedWorkPackageName"] = workPackageName;
	selectedDetails["selectedWorkWeekName"] = workWeekName;
	selectedDetails["selectedUserType"] = userType;
	selectedDetails["selectedUserRole"] = userRole;
	selectedDetails["selectedUserSkill"] = userSkill;
	
	 $("#currentShiftName").val($("#currentShiftName" +shiftId).val());
	isValidWorkPackageForReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId);
}



function isValidWorkPackageForReservation(workPackageId, shiftId, resourceDemandCount){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=Reservation',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			getResourceForBlockingGridViewReservation(workPackageId, shiftId, resourceDemandCount);
   		}
	});
}


function isValidWorkPackageForReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=Reservation',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			getResourceForBlockingGridViewReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId);
   			$("#reservationStartDateId").empty();
   			$("#reservationEndDateId").empty();
   			$("#reservationStartDateId").datepicker('setDate',new Date());
   			$("#reservationEndDateId").datepicker('setDate',new Date());
   		}
	});
}

function getResourceForBlockingGridViewReservation(workPackageId, shiftId, resourceDemandCount){

   initializeView();
	var loaddiv = '<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999"><div class="modal-dialog modal-lg" ><div class="modal-content" ><div class="modal-header"><button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button><h4 class="modal-title">Reserve Resources for Work Package</h4></div><div class="modal-body"><div class="scroller" style="max-height:420px" data-always-visible="1" data-rail-visible1="1"><div class="row" style="margin-bottom:10px"><div class="col-md-6"><div id="availabilityFilter" class="radio-toolbar"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label class="btn darkblue active" onclick="getBookedResources(0);"><input type="radio" class="toggle" >Booked</label><label class="btn darkblue" onclick="getBookedOrAvailableResources(0);"><input type="radio" class="toggle" >Booked or Available</label><label class="btn darkblue" onclick="getUnAvailableResources(0);"><input type="radio" class="toggle" >All</label></div></div></div><div class="col-md-3"><div id="viewFilter" class="radio-toolbar"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label class="btn darkblue active" onclick="showGridView(0);"><input type="radio" class="toggle" >Grid</label><label class="btn darkblue" onclick="showTableView(0);"><input type="radio" class="toggle" >Table</label></div></div></div><div class="col-md-3"><label>Resources : &nbsp</label><span class="badge " style="background:#95A5A6" id="sp_demandCount">0/0</span></div></div><div id="div_Table1" ></div><div id="div_Table2" ></div><div id="div_Table3" style="border: 1px solid rgb(204, 204, 204);"></div></div></div></div></div></div>';	
	$("#div_PopupMain").replaceWith(loaddiv);
	 	
 	var reservationDate = datepickerRP.value;
 	getResourceDetailsForBlockingGridView(workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType,currentView);
 	resetTabSelection();
} 

function reservationDatePickerHandler(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
//	sendJsonCustomFieldObject(id, oldValue, modifiedValue);
}
function getResourceForBlockingGridViewReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId){

	   initializeView();
		var loaddiv = '<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999"><div class="modal-dialog modal-lg" ><div class="modal-content" ><div class="modal-header"><button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button><h4 class="modal-title">Reserve Resources for Work Package</h4></div><div class="modal-body"><div class="scroller" style="max-height:420px" data-always-visible="1" data-rail-visible1="1">'+
		    '<div class="row" style="margin-bottom:10px">'+
		    '<div class="col-md-6"><div id="availabilityFilter" class="radio-toolbar"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label id= "weeklyBooked" class="btn darkblue active" onclick="getBookedResources(1);">'+
		    '<input type="radio" class="toggle" >Booked</label><label id = "weeklyBookedOrAvailable" class="btn darkblue" onclick="getBookedOrAvailableResources(1);"><input type="radio" class="toggle" >Booked or Available</label><label class="btn darkblue" onclick="getUnAvailableResources(1);"><input type="radio" class="toggle" >All</label></div>'+
			'</div></div>'+			
			'<div class="col-md-3"><div id="viewFilter" class="radio-toolbar"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label class="btn darkblue active" onclick="showGridView(1);"><input type="radio" class="toggle" >Grid</label><label class="btn darkblue" onclick="showTableView(1);"><input type="radio" class="toggle" >Table</label></div></div></div>'+
			'<div class="col-md-3"><label>Resources : &nbsp</label><span class="badge " style="background:#95A5A6" id="sp_demandCount">0/0</span></div></div>'+
			
			'<div class="row" style="margin-bottom:10px">'+
			'<div class="row" id="plannedDateDiv" style="display: flex;"><div class="input-group" style="padding-top: 5px;padding-right: 5px;"><label> Start Date:</label></div><div class="input-group"><div class=""><input id="reservationStartDateId" name="reservationStartDate" class="form-control input-small  date-picker" onchange="reservationDatePickerHandler(event);"></div></div><div class="input-group" style="padding-top: 5px;padding-right: 5px;padding-left: 5px;"><label>End Date:</label></div><div class="input-group"><div class=""><input id="reservationEndDateId" name="reservationEndDate" class="form-control input-small  date-picker" onchange="reservationDatePickerHandler(event); /></div></div></div></div><div id=" div_table1"=""></div><div id="div_Table2"></div><div id="div_Table3" style="border: 1px solid rgb(204, 204, 204);"></div></div></div>'+
			
			/*'<div class="col-md-12"><div style = "height: 12px; margin: 5px 1px;">Reservation Week(s) : <input type = "text" id="reserveRecursive"/><span style="margin-left: 6px; "> Please select the week range before resource reservation.</span></div></div>'+*/			
			'</div>'+
			
			'<div id="div_Table1" ></div><div id="div_Table2" >'+
			'</div><div id="div_Table3" style="border: 1px solid rgb(204, 204, 204);"></div></div></div></div></div></div>';	
		$("#div_PopupMain").replaceWith(loaddiv);
		$("#reserveRecursive").val(workWeek); 	
	 	var reservationDate = datepickerRP.value;
	 	getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,demandCount,groupDemandId,userTypeId);
	 	resetTabSelection();
	} 


function getResourceDetailsForBlockingGridView(workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType, currentView){
	document.getElementById("currentWorkPackage").value = workPackageId;
	document.getElementById("currentReservationShift").value = shiftId;
	document.getElementById("currentReservationDate").value = reservationDate;
	document.getElementById("currentResourceDemandCount").value = resourceDemandCount;
	document.getElementById("currentAvailabilityType").value = availablilityType;
	document.getElementById("currentViewType").value = currentView;
	
	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	currentShiftName = document.getElementById("currentShiftName").value;
	var details = currentWorkPackageName+" for Date: "+reservationDate;
	var date = new Date();
    var timestamp = date.getTime();
	urlToGetResourcesOfWorkPackage = "workPackage.block.resources?workPackageId="+workPackageId+"&shiftId="+shiftId+"&resourceDemandForDate="+reservationDate+"&filter="+availablilityType+"&timestamp="+timestamp;
	showResources(urlToGetResourcesOfWorkPackage,details,reservationDate,currentShiftName);
	loadPopup("div_PopupMain"); 
} 
 
 
 function getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId){
	 document.getElementById("currentWorkPackage").value = workPackageId;
		document.getElementById("currentReservationShift").value = shiftId;
		document.getElementById("currentReservationDate").value = reservationDate;
		document.getElementById("currentResourceDemandCount").value = resourceDemandCount;
		document.getElementById("currentAvailabilityType").value = availablilityType;
		document.getElementById("currentViewType").value = currentView;
		document.getElementById("groupDemandId").value = groupDemandId;
		document.getElementById("skillId").value = skillId;
		document.getElementById("userRoleId").value = userRoleId;
		document.getElementById("workWeek").value = workWeek;
		document.getElementById("workYear").value = workYear;
		document.getElementById("userTypeId").value = userTypeId;
		
		
		
		$("#weeklyBooked").text("Matching");
		
		$("#weeklyBookedOrAvailable").hide();
		
		
		weeklyEnable = "true";
		currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
		
		weekDetails = weeksName[workWeek-1];
		
		currentWorkPackageName = selectedDetails["selectedWorkPackageName"];
		weekDetails = selectedDetails["selectedWorkWeekName"];
		userTypeName = selectedDetails["selectedUserType"];
		userRoleName = selectedDetails["selectedUserRole"];
		skillName = selectedDetails["selectedUserSkill"];
		
		var details = "Reserve for - WP : "+currentWorkPackageName+", Week : "+weekDetails+", Role : "+userRoleName+", Type : "+userTypeName+", Skill : "+skillName;
		var date = new Date();
	    var timestamp = date.getTime();
		urlToGetResourcesOfWorkPackageWeekly = "workPackage.block.resources.weekly?workPackageId="+workPackageId+"&shiftId="+shiftId+"&workWeek="+workWeek+"&workYear="+workYear+"&userRoleId="+userRoleId+"&skillId="+skillId+"&userTypeId="+userTypeId+"&filter="+availablilityType;
		showResourcesWeekly(urlToGetResourcesOfWorkPackageWeekly,details,reservationDate,selectedShiftName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,groupDemandId,reservedCount,resourceDemandCount,userTypeId);
		loadPopup("div_PopupMain"); 
	} 

function showResources(urlToGetResourcesOfWorkPackage,details,reservationDate,currentShiftName) {
   try{
		 if($('#div_PopupMain').length>0){           				
		    $('#div_PopupMain').jtable('destroy');           					
		 };          			
		}catch(e){			
			$("#div_PopupMain .scroller").append('<div id="blockedResource_Popup"></div>');

		}     		
	    $('#blockedResource_Popup').show();		

	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
		$.blockUI({
		 	theme : true,
		 	title : 'Please Wait',
		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
		 });
   	$.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: urlToGetResourcesOfWorkPackage,
        dataType: 'json',
        success: function(jsonData) {
             var data = jsonData.Records;            	
             var reserveCount = 0;
             
         if(data.length!=0){ 
            $("#blockedResource_Popup").empty();          
            $("#div_PopupMain").find("h4").text("Reserve Resources for WP : "+ details);
            for (var key in data) {
            	var userId = data[key].userId;
            	
            	resourceId = userId;
            	var roleId = data[key].userRoleId;
            	var userReservationStatus = data[key].reserve;
            	var isReserved = "";
            	var productCoreRes = "";
            	var tfCoreRes = "";
            	var checkboxToolTip = "";
            	var remainingShiftBookingMsg = "";
            	var remainingTimeSheetBookingMsg = "";
            	var loginId = data[key].loginId;
            	var timeSheetHours = data[key].timeSheetHours;
            	var tsHrs = "";
            	var tsMins = "";
            	var shiftBookingHours = data[key].bookedHrs;
            	var userAvailability = data[key].available;
            	var userLoginId = data[key].loginId;
            	var userBookingStatus = data[key].booked;
            	var userAvailabilityToolTip = data[key].available;
            	var userBookingStatusToolTip = data[key].booked;
            	
                if(userAvailability != null){
                	if(userAvailability=="A"){
                		userAvailabilityToolTip = userLoginId+ " is available on "+reservationDate+" for Shift "+currentShiftName;
                	}else if(userAvailability == "NA"){
                		userAvailabilityToolTip = userLoginId+ " is not available on "+reservationDate+" for Shift "+currentShiftName;
                    }
                }
                
                if(userBookingStatus != null){
                	if(userBookingStatus=="B"){
                		userBookingStatusToolTip = userLoginId+ " is booked for "+reservationDate+" for Shift "+currentShiftName;
                	}else if(userBookingStatus == "NB"){
                		userBookingStatusToolTip = userLoginId+ " is not booked for "+reservationDate+" for Shift "+currentShiftName;
                    }
                }
            	
            	if (timeSheetHours == null) {
                	timeSheetHours = 40;
                    remainingTimeSheetBookingMsg = "40h 00m remaining in the week as per current time sheet bookings";
                }else{
              		if(timeSheetHours.indexOf(":") > -1){
              			tsHrs = timeSheetHours.split(":")[0];
              			tsMins = timeSheetHours.split(":")[1];
              			remainingTimeSheetBookingMsg = tsHrs+"h "+tsMins+"m remaining in the week as per current time sheet bookings";
              		}else{
              			remainingTimeSheetBookingMsg = timeSheetHours+"h 00m remaining in the week as per current time sheet bookings";
              		}
                }
                if (shiftBookingHours == null) {
                	shiftBookingHours = 40;
                    remainingShiftBookingMsg = "40h 00m remaining in the week as per current shift bookings";
                }else{
                	remainingShiftBookingMsg = shiftBookingHours+"h 00m remaining in the week as per current shift bookings";
                }

                if (userReservationStatus) {
                	reserveCount ++;
                	isReserved = "checked=checked";
                	checkboxToolTip = "Uncheck to Release";
                } else { 
                	isReserved = "";
                	checkboxToolTip = "Check to Reserve";
                }
                if(data[key].imageURI=="" || data[key].imageURI=="/Profile/" || data[key].imageURI == null){
                	data[key].imageURI="css/images/noimage.jpg";
                }else{
                	data[key].imageURI="/Profile/"+data[key].imageURI;
                }
                if(loginId.length >= 8){
                	loginId = loginId.substring(0,8)+"...";
                }
                productCoreRes = data[key].productCoreResource;
                tfCoreRes = data[key].tfCoreResource;
                var responses = fileExists(data[key].imageURI);
                if (responses == 404){
                	data[key].imageURI = "css/images/noimage.jpg";
                }
                var brick = 			'<div class="tile bg-grey-cascade"><div class="tile-body" style="padding:3px 3px;">' +
						'<img src=' + data[key].imageURI + ' width = "50%" height="50%"> ' +
						'<div style="text-align:center"><span class="badge badge-default bgCell" style="font-weight:bold"  title="'+remainingShiftBookingMsg+'">' + shiftBookingHours + '</span>' +
						'<span class="badge badge-default bgCell" style="font-weight:bold"  title="'+remainingTimeSheetBookingMsg+'">' + timeSheetHours + '</span></div>' +
						'<span class="badge badge-roundless test" style="padding:3px;margin-top:3px;"  title="'+userAvailabilityToolTip+'">' + data[key].available + '</span>' +
						'<span class="badge badge-roundless test" style="padding:3px;margin-top:3px;margin-left:3px;"  title="'+userBookingStatusToolTip+'">' + data[key].booked + '</span>' +
                        '</div>' +
						'<div class="tile-object" >';
						if((productCoreRes !== null) && (typeof productCoreRes !=='undefined')){
							brick += '<div title="'+data[key].loginId+'" style="margin-bottom:5px;padding-left:3px;">' ;
							brick +='<span class="badge badge-roundless " style="padding:3px;  title="Product Core Resource">'+ productCoreRes +'</span>&nbsp' ;
						} else{
							brick += '<div title="'+data[key].loginId+'" style="margin-bottom:5px;">' ;							
						}
						brick += '<input type="checkbox" title="'+checkboxToolTip+'"  name="div_'+userId+'" value="" id="div_'+userId+'" '+isReserved+' onclick="blockOrUnBlockResource('+userId+')"/>'+
						'<a style="color:white;" data-target="#div_PopupMainUserProfile" data-toggle="modal"  onclick="javascript:showUserProfile('+userId+','+roleId+');">&nbsp<b>'+loginId+'</b></a>&nbsp;<input type="hidden" name="hdndiv_'+userId+'" value="'+userReservationStatus+'" id="hdndiv_'+userId+'" /></div>'+
						//'<span class="badge badge-default"   title="Product Core Resource">' + productCoreRes + '</span> </div>'  +
						'<div><label style="margin-left:5px">'+ data[key].userRoleLabel + '</label></div>' +
						'</div>';
                if(!($('#blockedResource_Popup').hasClass('tiles'))){
                	$('#blockedResource_Popup').addClass('tiles');
                }              
                $('#blockedResource_Popup').append($(brick));
            }
         }else{
        	if(availablilityType == "All"){
        		callAlert(" Resource Pool are not mapped to Test Factory. Please contact Resource Manager.");
        		popupClose();
        	}else{
        		callAlert("Resources are not available for Reservation. Check with Resource Manager or Reserve unavailable resources");
               	workPackageId = document.getElementById("currentWorkPackage").value;
               	shiftId = document.getElementById("currentReservationShift").value;
               	reservationDate = document.getElementById("currentReservationDate").value;
               	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
               	availablilityType = "All";
               	//$('#radio3').attr('checked',true);
               	$("#availabilityFilter>div").find("label:last").addClass("active").siblings().removeClass("active");
               	currentView = document.getElementById("currentViewType").value;
               	getResourceDetailsForBlockingGridView(workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType, currentView);
        	}
         }
           	    $("#sp_demandCount").text(reserveCount + "/" + $("#currentResourceDemandCount").val());
           	    $("#sp_demandCount").attr("title", "Number of Reserved Resources are " + reserveCount + " for demand count of " + $("#currentResourceDemandCount").val());
                $('.bgCell').each(function(){
                    var thisCell = $(this);
                    var cellValue = parseInt(thisCell.text());

                    if (!isNaN(cellValue) && (cellValue > 8)) {
                        thisCell.addClass('badge-success');//css("background-color", "#006600");
                    } else if (!isNaN(cellValue) && (cellValue <= 8) && (cellValue > 0)) {
                        thisCell.addClass('badge-warning');//css("background-color", "#ffa500");
                    } else {
                        thisCell.addClass('badge-danger');//css("background-color", "#FF0000");
                    }            	
                });
                $('.test').each(function() {
                    var thisCell = $(this);
                    if (thisCell.text() === "A" || thisCell.text() === "B") {
                        thisCell.addClass('badge-success');//css("background-color", "#006600");
						//thisCell.css("margin-left","3px");                      
                    } else {
                        thisCell.addClass('badge-danger');//.css("background-color", "#E00000");
                    }
                });           
         /*   $('table.gridtable').find('td.bgCell').each(function() {
                var thisCell = $(this);
                var cellValue = parseInt(thisCell.text());

                if (!isNaN(cellValue) && (cellValue > 8)) {
                    thisCell.css("background-color", "#006600");
                } else if (!isNaN(cellValue) && (cellValue <= 8) && (cellValue > 0)) {
                    thisCell.css("background-color", "#ffa500");
                } else {
                    thisCell.css("background-color", "#FF0000");
                }
            });
            $('table.gridtable').find('td.test').each(function() {
                var thisCell = $(this);
                if (thisCell.text() === "Yes") {
                    thisCell.css("background-color", "#006600");
                } else {
                    thisCell.css("background-color", "#E00000");
                }
            });
            $('#blockedResource_Popup').addClass("scrollbarPopup");
            wall.fitWidth();*/
            $('#blockedResource_Popup').addClass("scrollbarPopup").css("background","#eff3f8");
           	$.unblockUI();
        }
    });
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
}

var reservedUserStatus = {};

function showResourcesWeekly(urlToGetResourcesOfWorkPackageWeekly,details,reservationDate,currentShiftName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,groupDemandId,reservedCount,demandCount,userTypeId) {
	reservedUserStatus = {};   
	try{
			 if($('#div_PopupMain').length>0){           				
			    $('#div_PopupMain').jtable('destroy');           					
			 };          			
			}catch(e){			
				$("#div_PopupMain .scroller").append('<div id="blockedResource_Popup"></div>');

			}     		
		    $('#blockedResource_Popup').show();		

		var thediv = document.getElementById('reportbox');
		openLoaderIcon();
		if (thediv.style.display == "none") {
			$.blockUI({
			 	theme : true,
			 	title : 'Please Wait',
			 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
			 });
	   	$.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetResourcesOfWorkPackageWeekly,
	        dataType: 'json',
	        success: function(jsonData) {
	        	closeLoaderIcon();
	             var data = jsonData.Records;            	
	             var reserveCount = 0;
	             
	         if(data.length!=0){ 
	            $("#blockedResource_Popup").empty();          
	            $("#div_PopupMain").find("h4").text(details);
	            for (var key in data) {
	            	var userId = data[key].userId;
	            	var roleId = data[key].userRoleId;
	            	var userReservationStatus = data[key].reserve;
	            	var isReserved = "";
	            	var productCoreRes = "";
	            	var tfCoreRes = "";
	            	var checkboxToolTip = "";
	            	var remainingShiftBookingMsg = "";
	            	var remainingTimeSheetBookingMsg = "";
	            	var loginId = data[key].loginId;
	            	var userRoleLabel = data[key].userRoleLabel;
	            	var userTypeLabel = data[key].userTypeLabel;
	            	var timeSheetHours = data[key].timeSheetHours;
	            	var tsHrs = "";
	            	var tsMins = "";
	            	var shiftBookingHours = data[key].bookedHrs;
	            	var userAvailability = data[key].available;
	            	var userLoginId = data[key].loginId;
	            	var userBookingStatus = data[key].booked;
	            	var userAvailabilityToolTip = data[key].available;
	            	var userBookingStatusToolTip = data[key].booked;
	            	var resourcePercentage = 0;
	            	var totalReservationPercentage = 0;
	            	
	            	if(data[key].reservationPercentage != null){
	            		resourcePercentage = data[key].reservationPercentage;
	            	}
	            	
	            	
	            	if(data[key].totalReservationPercentage != null){
	            		totalReservationPercentage = data[key].totalReservationPercentage;
	            	}
	            	
	                if(userAvailability != null){
	                	if(userAvailability=="A"){
	                		userAvailabilityToolTip = userLoginId+ " is available on "+reservationDate+" for Shift "+currentShiftName;
	                	}else if(userAvailability == "NA"){
	                		userAvailabilityToolTip = userLoginId+ " is not available on "+reservationDate+" for Shift "+currentShiftName;
	                    }
	                }
	                
	                if(userBookingStatus != null){
	                	if(userBookingStatus=="B"){
	                		userBookingStatusToolTip = userLoginId+ " is booked for "+reservationDate+" for Shift "+currentShiftName;
	                	}else if(userBookingStatus == "NB"){
	                		userBookingStatusToolTip = userLoginId+ " is not booked for "+reservationDate+" for Shift "+currentShiftName;
	                    }
	                }
	            	
	            	if (timeSheetHours == null) {
	                	timeSheetHours = 40;
	                    remainingTimeSheetBookingMsg = "40h 00m remaining in the week as per current time sheet bookings";
	                }else{
	              		if(timeSheetHours.indexOf(":") > -1){
	              			tsHrs = timeSheetHours.split(":")[0];
	              			tsMins = timeSheetHours.split(":")[1];
	              			remainingTimeSheetBookingMsg = tsHrs+"h "+tsMins+"m remaining in the week as per current time sheet bookings";
	              		}else{
	              			remainingTimeSheetBookingMsg = timeSheetHours+"h 00m remaining in the week as per current time sheet bookings";
	              		}
	                }
	                if (shiftBookingHours == null) {
	                	shiftBookingHours = 40;
	                    remainingShiftBookingMsg = "40h 00m remaining in the week as per current shift bookings";
	                }else{
	                	remainingShiftBookingMsg = shiftBookingHours+"h 00m remaining in the week as per current shift bookings";
	                }

	                if (userReservationStatus) {
	                	//reserveCount ++;
	                	isReserved = "checked=checked";
	                	checkboxToolTip = "Uncheck to Release";
	                } else { 
	                	isReserved = "";
	                	checkboxToolTip = "Check to Reserve";
	                }
	                if(data[key].imageURI=="" || data[key].imageURI=="/Profile/" || data[key].imageURI == null){
	                	data[key].imageURI="css/images/noimage.jpg";
	                }else{
	                	data[key].imageURI="/Profile/"+data[key].imageURI;
	                }
	                if(loginId.length >= 8){
	                	loginId = loginId.substring(0,8)+"...";
	                }
	                if(userRoleLabel != 'undefined' && userRoleLabel != null && userRoleLabel.length >= 8){
	                	userRoleLabel = userRoleLabel.replace(/ /g, "&nbsp;");
	                	userRoleLabel = userRoleLabel.substring(0,8)+"...";
	                }
	                if(userTypeLabel != 'undefined' && userTypeLabel != null && userTypeLabel.length >= 8){
	                	userTypeLabel = userTypeLabel.replace(/ /g, "&nbsp;");
	                	userTypeLabel = userTypeLabel.substring(0,8)+"...";
	                }
	                productCoreRes = data[key].productCoreResource;
	                tfCoreRes = data[key].tfCoreResource;
	                var responses = fileExists(data[key].imageURI);
	                if (responses == 404){
	                	data[key].imageURI = "css/images/noimage.jpg";
	                }
	                var bgColor = "green";
	                if(totalReservationPercentage > 100){
	                	bgColor = "red";
	                }else if(totalReservationPercentage < 100 && totalReservationPercentage > 0){
	                	bgColor = "orange";
	                }else if(totalReservationPercentage == 0){
	                	bgColor = "#5d6b7a";
//	                	bgColor = "blue";
	                }
	            	var isFullView = true;
	            	var isCreatePopup = true;
	                var showRp = true;
	                var scrolValue = 150;
	                var brick = '<div class="tile bg-grey-cascade"><div class="tile-body" style="padding:3px 1px;">'+
							'<img src=' + data[key].imageURI + ' width = "50%" height="50%">'+
							//'<div style="text-align:center"><span class="badge badge-default bgCell" style="font-weight:bold;background-color: '+bgColor+';" onclick="showIndividualUserAvailbilty(\'resourceReservationAndDemandContainer\', 0, 0, 0, 0,0, true, \'150px\','+userId+',\''+loginId+'\');" title="Total Reservation ">' + totalReservationPercentage+ '%'+'</span>'+
							'<div style="text-align:center"><span class="badge badge-default bgCell" style="font-weight:bold;background-color: '+bgColor+';" onclick="showDetailedUtilizationOfResource(\'resourceReservationAndDemandContainer\', 0, 0, 0, 0, 0, '+userId+', 0, '+workYear+', \''+data[key].loginId+'\', \''+data[key].userRoleLabel+'\', \''+data[key].userTypeLabel+'\',\''+isFullView+'\',\''+isCreatePopup+'\');" title="Total Reservation ">' + totalReservationPercentage+ '%'+'</span>'+
	                        '</div>';
	                
							if((productCoreRes !== null) && (typeof productCoreRes !=='undefined')){
								brick += '<span class="badge badge-roundless " style="padding:3px;  title="Product Core Resource">'+ productCoreRes +'</span>&nbsp';								
								brick += '<div><a style="color:white;" data-target="#div_PopupMainUserProfile" data-toggle="modal"  onclick="javascript:showUserProfile('+userId+','+roleId+');">&nbsp<b>'+loginId+'</b></a>&nbsp;<input type="hidden" name="hdndiv_'+userId+'" value="'+userReservationStatus+'" id="hdndiv_'+userId+'" /></div>';
								brick += '<div><label style="margin-left:5px">'+ data[key].userRoleLabel + '</label></div></div></div>'								
							} else{
								brick += '<div title="'+data[key].loginId+'" style="margin-bottom:5px;position: absolute;margin-left: 35px;">';							
							}
							brick += '<ul style="list-style-type: none;">'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="100%"  name="div_'+userId+'" value="100" id="div_'+userId+'100" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',100)"/>100%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="75%"  name="div_'+userId+'" value="75" id="div_'+userId+'75" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',75)"/>75%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="50%"  name="div_'+userId+'" value="50" id="div_'+userId+'50" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',50)"/>50%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="25%"  name="div_'+userId+'" value="25" id="div_'+userId+'25" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',25)"/>25%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="0%"  name="div_'+userId+'" value="0" id="div_'+userId+'0" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',0)"/>0%</input></li></ul>'+
							'</div>';
							
							brick += '<div style="width:50%; font-size:11px;cursor:default;">'+
							'<div title="'+data[key].loginId+'" style="">'+
							'<a style="color:white;cursor:pointer;" data-target="#div_PopupMainUserProfile" data-toggle="modal"  onclick="javascript:showUserProfile('+userId+','+roleId+');"><b>'+loginId+'</b></a><input type="hidden" name="hdndiv_'+userId+'" value="'+userReservationStatus+'" id="hdndiv_'+userId+'" />'+
							'</div>'+
							'<div title="'+data[key].userRoleLabel+'">'+ userRoleLabel + '</div>'+
							'<div title="'+data[key].userTypeLabel+'">'+ userTypeLabel + '</div>' +
							
							'</div>';
	                if(!($('#blockedResource_Popup').hasClass('tiles'))){
	                	$('#blockedResource_Popup').addClass('tiles');
	                }              
	                $('#blockedResource_Popup').append($(brick));
	                $('#div_'+userId+data[key].reservationPercentage).attr('checked', true);
	                
	                if(typeof data[key].reservationPercentage != 'undefined' && data[key].reservationPercentage != null && data[key].reservationPercentage > 0){
	                	reservedUserStatus[userId] = data[key].reservationPercentage / 100;
	                	reserveCount += data[key].reservationPercentage / 100;
	                }
	                
	            }
	         }else{
	        	if(availablilityType == "All"){
	        		callAlert(" Resource Pool are not mapped to Test Factory or Resources are not available for Selected Period.");
	        		popupClose();
	        	}else{
	        		callAlert("Resources are not available for Reservation. Check with Resource Manager or Reserve unavailable resources");
	               	workPackageId = document.getElementById("currentWorkPackage").value;
	               	shiftId = document.getElementById("currentReservationShift").value;
	               	reservationDate = document.getElementById("currentReservationDate").value;
	               	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	               	groupDemandId = document.getElementById("groupDemandId").value;
	               	availablilityType = "All";
	               	//$('#radio3').attr('checked',true);
	               	$("#availabilityFilter>div").find("label:last").addClass("active").siblings().removeClass("active");
	               	currentView = document.getElementById("currentViewType").value;
	               	getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId);
	               	
	        	}
	         }
	           	    $("#sp_demandCount").text(reserveCount + "/" + $("#currentResourceDemandCount").val());
	           	    $("#sp_demandCount").attr("title", "Number of Reserved Resources are " + reserveCount + " for demand count of " + resourceDemandCount);
	                $('.bgCell').each(function(){
	                    var thisCell = $(this);
	                    var cellValue = parseInt(thisCell.text());

	                    if (!isNaN(cellValue) && (cellValue > 8)) {
	                        thisCell.addClass('badge-success');//css("background-color", "#006600");
	                    } else if (!isNaN(cellValue) && (cellValue <= 8) && (cellValue > 0)) {
	                        thisCell.addClass('badge-warning');//css("background-color", "#ffa500");
	                    } else {
	                        thisCell.addClass('badge-danger');//css("background-color", "#FF0000");
	                    }            	
	                });
	                $('.test').each(function() {
	                    var thisCell = $(this);
	                    if (thisCell.text() === "A" || thisCell.text() === "B") {
	                        thisCell.addClass('badge-success');//css("background-color", "#006600");
							//thisCell.css("margin-left","3px");                      
	                    } else {
	                        thisCell.addClass('badge-danger');//.css("background-color", "#E00000");
	                    }
	                });           
	         
	            $('#blockedResource_Popup').addClass("scrollbarPopup").css("background","#eff3f8");
	           	$.unblockUI();
	        }
	    });
		} else {
			thediv.style.display = "none";
			thediv.innerHTML = '';
		}
}




function fileExists(fileLocation) {
    var response = $.ajax({
        url: fileLocation,
        type: 'HEAD',
        async: false
    }).status;
    return response;
}

function showUserProfile(userId, roleId){
	var profileFilter = 1;
	setUserFieldValues(profileFilter,userId, roleId);
	//alert("user Id: "+userId + " User Id from hidden field: "+document.getElementById("currentSelectedUserId").value);
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
        url : 'profile.management.plan.content?selecteduserId='+userId,
	//	url : 'profile.management.plan.content',
		dataType : 'html',
		success : function(data) {			
			 $("#div_SelectedUserProfile").html(data);			  
		},
		complete : function(data) {
			 $("#div_PopupMainUserProfile").modal();
		}
	});
}

function blockOrUnBlockResource(userId){
	var reservationStatus = document.getElementById('hdndiv_'+userId).value;
	var reservationStatusId = 'hdndiv_'+userId;
	var checkBoxDivId = "#div_"+userId;
	var currentAvailability = document.getElementById("currentAvailabilityType").value;
	var manageCount = $("#sp_demandCount").text().split("/")[0];
	if($(checkBoxDivId).is(':checked')){
		if(reservationStatus == 0){
			manageCount = parseInt(manageCount) + 1; 
			reserveSelectedResource(userId,checkBoxDivId,reservationStatusId,currentAvailability);
			$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
			$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
		}
	}
	if(!$(checkBoxDivId).is(':checked')){
		if(reservationStatus == 1){
			manageCount = parseInt(manageCount) - 1; 
			unReserveResource(userId,reservationStatusId,currentAvailability);
			$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
			$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
		}
	}
	
}

function blockOrUnBlockResourceWeekly(userId,groupDemandId,reservationPercentage){
	var reservationStatus = document.getElementById('hdndiv_'+userId).value;
	var reservationStatusId = 'hdndiv_'+userId;
	var checkBoxDivId = "#div_"+userId;
	var currentAvailability = document.getElementById("currentAvailabilityType").value;

	//manageCount = parseInt(manageCount) + 1; 
	reserveSelectedResourceWeekly(userId,checkBoxDivId,reservationStatusId,currentAvailability,groupDemandId,reservationPercentage);
	
//	if($(checkBoxDivId).is(':checked')){
//		if(reservationStatus == 0){
//			manageCount = parseInt(manageCount) + 1; 
//			reserveSelectedResourceWeekly(userId,checkBoxDivId,reservationStatusId,currentAvailability,groupDemandId,reservationPercentage);
//			$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
//			$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
//		}
//	}
//	if(!$(checkBoxDivId).is(':checked')){
//		if(reservationStatus == 1){
//			manageCount = parseInt(manageCount) - 1; 
//			unReserveResourceWeekly(userId,reservationStatusId,currentAvailability,groupDemandId);
//			$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
//			$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
//		}
//	}
	
}




function reserveSelectedResource(userId,checkBoxDivId,reservationStatusId,currentAvailability){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	reservationDate = document.getElementById("currentReservationDate").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	document.getElementById("currentAvailabilityType").value = currentAvailability;
	var urlToReserveOrReleaseResource='workpackage.block.resources.for.workpackage?shiftId='+shiftId+'&workpackageId='+workPackageId+'&blockResourceForDate='+reservationDate+'&demand='+resourceDemandCount+'&userId='+userId+'&reserveOrUnreserve=Reserve&currentAvailability='+currentAvailability;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToReserveOrReleaseResource,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="ERROR"){
	    		callAlert(data.Message);
	    		$(checkBoxDivId).prop('checked',false);
	    	}else{
	    		document.getElementById(reservationStatusId).value = "1";
	    	}
		},
	});
}

function reserveSelectedResourceWeekly(userId,checkBoxDivId,reservationStatusId,currentAvailability,groupDemandId,reservationPercentage){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	workWeek =  document.getElementById("workWeek").value;
	workYear = document.getElementById("workYear").value;
	skillId = document.getElementById("skillId").value; 
	userRoleId = document.getElementById("userRoleId").value; 
	userTypeId = document.getElementById("userTypeId").value; 
	
//	groupDemandId = document.getElementById("groupDemandId").value; 
	
	var reserveWeeks = $("#reserveRecursive").val();
	if(typeof reserveWeeks == 'undefined' || reserveWeeks == null || reserveWeeks == '' || reserveWeeks == '0'){
		reserveWeeks = (workWeek);
    }
	openLoaderIcon();
	document.getElementById("currentAvailabilityType").value = currentAvailability;
	
	var startDate = $("#reservationStartDateId").val();
	var endDate = $("#reservationEndDateId").val();	
	
//	var urlToReserveOrReleaseResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+' &reserveOrUnreserve=Reserve&currentAvailability='+currentAvailability+'&recursiveWeeks='+reserveWeeks+'&reservationPercentage='+reservationPercentage+'&userTypeId='+userTypeId;
	var urlToReserveOrReleaseResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+'&reserveOrUnreserve=Reserve&currentAvailability='+currentAvailability+'&startDate='+startDate+'&endDate='+endDate +'&reservationPercentage='+reservationPercentage+'&userTypeId='+userTypeId+'&overOccupancy=NO';
	var percentage = reservationPercentage / 100;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToReserveOrReleaseResourceForWeek,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			if(data.Result=="ERROR"){
	    		callAlert(data.Message);
	    		$(checkBoxDivId).prop('checked',false);
	    	}else{
	    		document.getElementById(reservationStatusId).value = "1";
	    		var manageCount = $("#sp_demandCount").text().split("/")[0];
	    		if(typeof reservedUserStatus[userId] != 'undefined' && reservedUserStatus[userId] != null){
	    			manageCount = parseFloat(manageCount);
	    			manageCount = manageCount - reservedUserStatus[userId];
	    			manageCount = manageCount + percentage;
	    			reservedUserStatus[userId] = percentage;
	    		}else{
	    			manageCount = parseFloat(manageCount);
	    			manageCount = manageCount + percentage;
	    			reservedUserStatus[userId] = percentage;
	    		}
	    		$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
	    		$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
	    	}
		},
	});
}




function unReserveResource(userId,reservationStatusId,currentAvailability){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	reservationDate = document.getElementById("currentReservationDate").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	document.getElementById("currentAvailabilityType").value = currentAvailability;
	var urlToReserveOrReleaseResource='workpackage.block.resources.for.workpackage?shiftId='+shiftId+'&workpackageId='+workPackageId+'&blockResourceForDate='+reservationDate+'&demand='+resourceDemandCount+'&userId='+userId+'&reserveOrUnreserve=Release&currentAvailability='+currentAvailability;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToReserveOrReleaseResource,
		dataType : 'json',
		success : function(data) {
			document.getElementById(reservationStatusId).value = "0";
			if(data.Result=="ERROR"){
				callAlert(data.Message);
			}
			
		}
	});
}


function unReserveResourceWeekly(userId,reservationStatusId,currentAvailability,groupDemandId){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	reservationDate = document.getElementById("currentReservationDate").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	workWeek =  document.getElementById("workWeek").value;
	workYear = document.getElementById("workYear").value; 
	skillId = document.getElementById("skillId").value; 
	userRoleId = document.getElementById("userRoleId").value;
	document.getElementById("currentAvailabilityType").value = currentAvailability;
//	groupDemandId = document.getElementById("groupDemandId").value;
	
	var reserveWeeks = $("#reserveRecursive").val();
	if(typeof reserveWeeks == 'undefined' || reserveWeeks == null || reserveWeeks == '' || reserveWeeks == '0'){
		reserveWeeks = (workWeek);
    }
	
	var startDate = $("#reservationStartDateId").val();
	var endDate = $("#reservationEndDateId").val();
	/*var urlToReserveOrReleaseResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+'&reserveOrUnreserve=Release&currentAvailability='+currentAvailability+'&recursiveWeeks='+reserveWeeks;*/
	var urlToReserveOrReleaseResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+'&reserveOrUnreserve=Release&currentAvailability='+currentAvailability+'&startDate='+startDate+'&endDate='+endDate ;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToReserveOrReleaseResourceForWeek,
		dataType : 'json',
		success : function(data) {
			document.getElementById(reservationStatusId).value = "0";
			if(data.Result=="ERROR"){
				callAlert(data.Message);
			}
			
		}
	});
}




//  Jtable view of Reserved Resources - start
 function loadBlockedUserDetails(workPackageId, shiftId, rowId){
	     $('a.resource'+rowId).attr('href', function(event) {
	     var contextpath = (window.location.pathname).split("/", 2);
		 var root = window.location.protocol	+ "//"+ window.location.host+ "/"+ contextpath[1]+"/workPackage.block.resources?workPackageId="+workPackageId+"&shiftId="+shiftId+"&resourceDemandForDate="+datepickerRP.value;
	      $.popupWindow(root);
	    });
}
 
 function viewAvailableResourcesDetails(workPackageId,shiftId){		
		var reserve_date=datepickerRP.value;
		var url = "resourceAvailability.list?workPackageId="+workPackageId+"&shiftId="+shiftId+"&availabilityForDate="+reserve_date;
		
		divPopupMainTitle(popupTitle_AvailableResource);
		 
		try{
			if ($("#JTable_Allocate").length>0) {
				 $('#JTable_Allocate').jtable('destroy');
			}			
		} catch(e) {}
		
	    $('#JTable_Allocate').jtable({
			title: "Available Resources For Reservation",
	        selecting: true,  //Enable selecting 
	        editinline : {enable : true},
	        actions: {
	       	 	listAction: url
	        },  
	        fields : {
	          resourceAvailabilityDetailsId: {
	                 key: true,
	                 list: false
	             },
	             resourceId: {
	                 title: 'Resource Id',
	                 create:false,
	                 edit:false,
	                 list:false
	 			},
	 			resourceName: {
	                 title: 'Resource',
	                 create:false,
	                 edit:false,
	                 list:true,
	                 width: "8%"
	 			},
	 			resourceRoleName: {
	                 title: 'Role',
	                 create:false,
	                 edit:false,
	                 list:true,
	                 width: "8%"
	 			},
	 			availabilityForDate: {
	 				title: 'Date',
	                create:false,
	                edit:false,
	                list:true,
	                width: "8%"
	 			},
	 			shiftTypeId:{
				   title: 'Shift Id',
				   create:false,
	              edit:false,
	              list:false
				},
	 			shiftTypeName:{
					title: 'Shift Type',
					create:false,
	              	edit:false,
	              	list:true,
	              	width: "5%"
	 			},
	 			skillName:{
					title: 'Skill',
					create:false,
	              	edit:false,
	              	list:true,
	              	width: "10%"
	 			},
	        	}
			});
		$('#JTable_Allocate').jtable('load');		
	}
 
 function loadBlockedResourceDetails(workPackageId,shiftId){				
		var reserve_date=datepickerRP.value;
		var url = "testFactory.ResourceReservation.list?workPackageId="+workPackageId+"&shiftId="+shiftId+"&reservationDate="+reserve_date;
		
		var date = new Date();	
		date.setMonth(date.getMonth() - 1); 
		var dt = new Date(reserve_date);	 
		var yr = dt.getYear() + 1900;
		var mn = dt.getMonth() + 1;
		date=yr + "-" + mn + "-" + dt.getDate();	 
		var resource_resv_statistics_url=  "testFactory.ResourceReservation.details.list?workPackageId="+workPackageId+"&shiftId="+shiftId+"&reservationDate="+date;
		
		$("#div_PopupMain_reserve").find('h4').text("");
		$("#div_PopupMain_reserve").find('h4').text("Resource Statistics and Reserved Resources");
		$("#div_PopupMain_reserve").modal();
		
	 	try{
			if ($('#JTable_ResourceStatistics').length>0) {
				 $('#JTable_ResourceStatistics').jtable('destroy'); 
			}
			
			if ($('#JTable_ReservedResources').length>0) {
				 $('#JTable_ReservedResources').jtable('destroy'); 
			}
		} catch(e) {}		
		
		loadResourceDetails(url, resource_resv_statistics_url);
	} 
	//Jtable view of Reserved Resources -End

	function loadResourceDetails(url, resource_resv_statistics_url){
		var urlToGetTestCasesOfWorkPackage = url;	
	 	
		 $('#JTable_ResourceStatistics').jtable({		
				title: "Resource Statistics",
		        selecting: true,  //Enable selecting 
		        paging: true, //Enable paging
		        pageSize: 10, 
		          actions: {
		       	 	listAction: resource_resv_statistics_url,
		        },  
		        fields : {
		        	wpDemandProjectionId: {
		                 key: true,
		                 list: false,
		                 create: false
		             },	
		             /* productName:{
		                 title: 'Product Name',
		            	 list:true,
		                 width:"20%"
		             }, */
		             workPackageName:{
		                 title: 'WorkPackage',
		            	 list:true,
		                 width:"20%"
		             },
		             workDate: { 
		                	title: 'Work Date' ,
		                	list:true,
		                  
		                	width: "16%"          	
		                }, 
		             shiftName:{
		                 title: 'Shift',
		            	 list:true,
		                 width:"20%"
		             },
		             skillName:{
		                 title: 'Skill',
		            	 list:true,
		                 width:"20%"
		             },
		             userRoleName:{
		                 title: 'Role',
		            	 list:true,
		                 width:"20%"
		             },
		             resourceCount:{
		                 title: 'Resource Count',
		            	 list:true,
		                 width:"20%"
		             },
		            
	       	},   
				});
		$('#JTable_ResourceStatistics').jtable('load'); 
		
		$('#JTable_ReservedResources').jtable({
			title: "Reserved Resources",
	        selecting: true,  //Enable selecting 
	        paging: true, //Enable paging
	        pageSize: 10, 
	          actions: {
	       	 	listAction: urlToGetTestCasesOfWorkPackage,
	        },  
	        fields : {
	        		id: {
		                 key: true,
		                 list: false,
		                 create: false
		             },	
		             testFactoryResourceReservationId:{
		                 title: 'Test Case Id',
		            	 list:false,
		                 width:"20%"
		             },
		             workPackageName:{
		                 title: 'WorkPackage',
		            	 list:true,
		                 width:"20%"
		             },
		             reservationDate: { 
		                	title: 'Reservation Date' ,
		                	list:true,		                  
		                	width: "16%"          	
		                }, 
		             shiftName:{
		                 title: 'Shift',
		            	 list:true,
		                 width:"20%"
		             },
		             blockedUserName:{
		                 title: 'Users',
		            	 list:true,
		                 width:"20%"
		             },
		             skillNameCharacterlength:{	            	
			   			type: 'hidden',
			   			create: false, 
			   			edit: false, 
			   			list: true,
		            	display: function (dataskill) { return dataskill.record.skillNameCharacterlength;},	   
		             },
		             skillName:{
		                 title: 'Skills',
		            	 list:true,
		                 width:"10%", 
		                 dependsOn: 'skillNameCharacterlength',
		                 edit:false,
		                 //tooltip:'FORMAT: First and Last name (no initials)'
		                 display:function(data)
			    		 {
			    		 var char_count = data.record.skillNameCharacterlength;
			    		 if(data.record){
			        		 if(char_count >10){		        			
			        			 return '<p style="border:none, outline:none; display: inline-block;" readonly class="jtable-input-readonly"  title="'+data.record.skillName+'">'+data.record.skillName +'</p>';			        				 
			        		}else{
			        			return '<p style="border:none" readonly class="jtable-input-readonly" type="label"  >'+ data.record.skillName +'</p>';
			        		}
			        	   }
			           	} 
		             },
		            
	        	},   
			});
		$('#JTable_ReservedResources').jtable('load');	     
	}

// Define scope starts
function getEnvironmentsForProduct() {
	
	 var environmentNames = new Array();
    $.ajax({
            url : 'common.list.product.environments?workPackageId='+workPackageId,
 			dataType : 'json',
 			error: function() {
 			      callAlert("An error occurred");
 			},
            success : function(jsonData) {
        	   
        	   	for (var i = 0; i < jsonData.Options.length; i++) {
	             	var environment = jsonData.Options[i];
	             	environmentNames.push(environment.DisplayText);
        	   }
               setEnvironmentNames(environmentNames);
             //  getLocalesForProduct();
               //callJTableFun();//END - Delete - JTable
           		
          }
    });
}; 

function setEnvironmentNames(environmentNames){
	jsEnvironmentFieldDisplayTitles = environmentNames;
}

/*//BEGIN - Delete - JTable
function callJTableFun(){
	var fields = {};
	//Add the static fields
	fields['testStepsDetails']= {
	        title:'Test Step',
	        width: "2%",
	        create:false,
	        edit:true,
	        display: function (childData) { 
        				//Create an image that will be used to open child table 
        				
	        			///*  Abort_completed functionality for view Testcases Start . 
			        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Test Step List" />'); 

			        	///*  Abort_completed functionality for view Testcases End . 
        				//Open child table when user clicks the image 
			        	$img.click(function (data) { 
			        		
		        		// ----- Closing child table on the same icon click -----
						closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerPlan"));
						if(closeChildTableFlag){
							return;
						}
			        		
			        	$('#jTableContainerPlan').jtable('openChildTable', 
			        	$img.closest('tr'), 
			        	{ 
			        	title: 'Test Steps',
			        	actions: { 
			        		//listAction: 'test.suite.step.list?testCaseId='+childData.record.testCaseId
			        		listAction: 'testcase.teststep.list?testCaseId='+childData.record.testcaseId
			          	}, 
			        	fields: { 
			        	testStepName: { 
			        		title: 'Name',  
			        		width: "20%",                          
			                create: false   
			        	},
			        	testStepDescription:{
			        		title: 'Description',  
			        		width: "30%",                          
			                create: false   
			        	},
			        	testStepInput: { 
			        		title: 'Input', 
			        		width: "20%",                         
			                create: false
			           },
			           testStepExpectedOutput: { 
			        		title: 'Expected Output',  
			        		width: "25%",                        
			                create: false
			            },
			           testStepSource: { 
			        		title: 'Source',  
			        		width: "7%",         
			                create: false							                 
			            },
			           testStepCode: { 
			        		title: 'Code',
			        		width: "7%",         
			                create: false							                
			            }
			           } 
			        }, 
			        	function (data) { //opened handler 
			        	data.childTable.jtable('load'); 
			        	}); 
			        	}); 
			        		//Return image 
			        		return $img; 
			        	}, 
  	};
	fields['id'] = {key: true,list:false,edit:false};
	fields['testcaseId'] =  {title: 'Test Case Id',list:true,edit: false,width:"20%"};
	fields['testcaseCode'] = {title: 'Test Case Code',list:true,edit: false,width:"20%"};
	fields['testcaseName'] = {title: 'Test Case ',edit: false,width:"20%"};
	fields['testcaseDescription'] = {title: 'Description',edit: false,width:"20%"};
	fields['decouplingCategoryId'] = {title: 'Decoupling Category Id',edit: false,list:false,width:"20%"};
	fields['decouplingCategoryName'] = {title: 'Decoupling Category',edit: false,list:true,width:"20%"};
	fields['features'] = {title: 'Features',edit: false,list:true,width:"20%"};
	fields['workPackageId'] = {title: 'workpackage ID',list:false,edit: false,width:"20%"};
	fields['workPackageName'] = {title: 'Workpackage Name',list:false,edit: false,width:"20%"};
	fields['recommendedByITF'] = {title: 'Recommended',list:true,edit: false,width:"20%"};
	fields['recommendationType'] = {title: 'Why',list:true,edit: false,width:"20%"};
	fields['isSelected'] = {title: 'Selected',type : 'checkbox',width:"20%",values: {'1' : 'Yes','0' : 'No'}};
	fields['allEnvironments'] = {title: 'All Env',list:false, edit:false,type : 'checkbox',width:"20%",values: {'1' : 'Yes','0' : 'No'}};
	fields['clearEnvironments'] = {title: 'Clear Env',list:false,edit:false,type : 'checkbox',width:"20%",values: {'1' : 'Yes','0' : 'No'}};
	fields['createdDate'] = {title: 'Created Date',list:false,edit: false,width:"20%"};
	fields['editedDate'] = {title: 'Edited Date',list:false,edit:false,width:"20%"};
	fields['status'] = {title: 'Status',list:false,width:"20%"};
	
	//Add the Dynamic Env fields
// 	for(var i=0;i<availableEnvironmentsCount;i++) {
// 		if (jsEnvironmentFieldDisplayTitles[i] == "NA") {
// 			fields[jsEnvironmentFieldsInJson[i]] = {
// 	       		title: jsEnvironmentFieldDisplayTitles[i],
// 	       		width: '20%',
// 	       		list : false,
// 	       		edit : false,
// 	       		type : 'checkbox',
// 	       		values: {'1' : 'Yes','0' : 'No'},
// 	       		defaultValue: '0'
// 			};
// 		} else {
// 	    	fields[jsEnvironmentFieldsInJson[i]] = {
// 		    	title: jsEnvironmentFieldDisplayTitles[i],
// 		        width: '20%',
// 	    		type : 'checkbox',
// 	    		values: {'1' : 'Yes','0' : 'No'},
// 	    		defaultValue: '0'
// 			};
// 		}
// 	}
	
	//Add th dynamic Locale fields
// 	for(var i=0;i<availableLocalesCount;i++) {
// 		if (jsLocaleFieldDisplayTitles[i] == "NA") {
// 			fields[jsLocaleFieldsInJson[i]] = {
// 	       		title: jsLocaleFieldDisplayTitles[i],
// 	       		width: '20%',
// 	       		list : false,
// 	       		edit : false,
// 	       		type : 'checkbox',
// 	       		values: {'1' : 'Yes','0' : 'No'},
// 	       		defaultValue: '0'
// 			};
// 		} else {
// 	    	fields[jsLocaleFieldsInJson[i]] = {
// 		    	title: jsLocaleFieldDisplayTitles[i],
// 		        width: '20%',
// 	    		type : 'checkbox',
// 	    		values: {'1' : 'Yes','0' : 'No'},
// 	    		defaultValue: '0'
// 			};
// 		}
//	}
	
	//Destroy the current jtable so that it is forced to reload with the new workpackage id

	try{
		if ($('#jTableContainerPlan').length>0) {
			 $('#jTableContainerPlan').jtable('destroy'); 
		}
	} catch(e) {}
	 $('#jTableContainerPlan').jtable({
         
         title: 'Select Test Cases for Work Package',
         selecting: true,  //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, 
         openChildAsAccordion: true,
         editinline:{enable:true},
		 multiselect: true, //Allow multiple selecting
         selectingCheckboxes: true, //Show checkboxes on first column
         toolbar : {
			items : [ {
				text : 'Import Selected TestCases',
				click : function() {
					
					
				}
			} ]
		},
         actions: {
        	 listAction : urlToGetWorkPackagesTestCasesOfSpecifiedWorkPackage,
        	 //listAction: 'workpackage.testcase.list',
             //createAction: 'administration.device.add',
             editinlineAction: 'workpackage.testcase.update'
             //deleteAction: 'administration.device.delete'
             //inlineUpdateAction : 'workpackage.testcase.update'
         },
        
         fields : fields,
         recordsLoaded: function(event, data) {
    		 var $selectedRowsDefine = $('#jTableContainerPlan').jtable('selectedRows');
    		 if($selectedRowsDefine >=0)
    		 {
    			 $("#jTableContainerPlan").find(".jtable-selecting-column > input").prop("checked", true);
    		 }    		 
    	 }
		});
	 
	 $('#jTableContainerPlan').jtable('load');
	 
	 var jscrolheight = $("#jTableContainerPlan").height();
	 var jscrolwidth = $("#jTableContainerPlan").width();
	  
	 $(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;		
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
	
		 if (lastScrollLeft < documentScrollLeft) {
		    	$("#jTableContainerPlan").width($(".jtable").width()).height($(".jtable").height());			
		        lastScrollLeft = documentScrollLeft;
		    }else if(lastScrollLeft >= documentScrollLeft){			
		    	$("#jTableContainerPlan").width(jscrolwidth).height(jscrolheight);
		    }			
	 });
}*/
//END - Delete - JTable

/* function loadPopupEnvironmentDetails(url,type){
	var urlToGetTestCasesOfWorkPackage = url;
	var workpackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	
	var loaddiv = loadDiv_PopupMain();
	$("#div_PopupMain").replaceWith(loaddiv);
	$('#div_PopupMain').find('h4').remove();	
	$("#availabilityFilter").remove();
	$("#viewFilter").remove();
	$("#div_Table3").remove();
	$("#popclosetcr").remove();
	$("#testcaseResults").remove();
	$('#div_PopupMain .scroller').empty();
	
	if(type=='testsuite')
		editurl='administration.workpackage.maprunconfig?workpackageId='+workpackageId;
	else
		editurl='administration.workpackage.mapenvironmentcombination?workpackageId='+workpackageId;
	
	try{
		if ($('#div_PopupMain .scroller').length>0) {
			$('#div_PopupMain .scroller').jtable('destroy'); 
		}
	} catch(e) {}
	
	$('#div_PopupMain .scroller').jtable({
		title: 'Test Configuration(s)',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
        editinline:{enable:true},
          actions: {
       	 	listAction: urlToGetTestCasesOfWorkPackage,       	 
       	 	editinlineAction: editurl
        },  
        fields: {	           
           
        	runconfigId: {
                key: true,
                list: false,
                create: false
            },
            runConfigurationName: {
                title: 'Test Configuration',
                width:"20%"
            },
     
        	status: {
				 title: 'Selected' ,
				 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
				 width: "10%",  
				 list:true,
				 edit:true,
				 type : 'checkbox',
				 values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '0'
		    		}
       }, 
		});
	$('#div_PopupMain .scroller').jtable('load');
     loadPopup("div_PopupMain");
} */

$("#runConfiguration").click( function()
       {
		event.preventDefault();
	    
	    var productId=document.getElementById("treeHdnCurrentProductId").value; 
	    var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
	    if(productId==null || productId <=0 || productId == 'null'){
				callAlert("Please select the product");
				return false;
			}
			if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
				callAlert("Please select the workpackage");
				return false;
			}				
       }
    );
   

$(function() {
    $('#saveSelected').on('click', function(event) {
    	var subTabActive = $("#giveId").find("label.active").attr("data-name");
    	var subTabActiveIndex = $("#giveId").find("label.active").index();
    	console.log(subTabActive," - ",subTabActiveIndex);

    	if(subTabActiveIndex==2){
    		var $selectedRows = $('#jTableContainerPlan').jtable('selectedRows');
	    	if($selectedRows.length>0) {
	    		saveTestCases();
	    	} 
    	}else if(subTabActiveIndex==1){ 
        	var $selectedRows_TS = $('#jTableContainerPlanTS').jtable('selectedRows');

    		if($selectedRows_TS.length>0) {
	    		saveTestSuite();
	    	} 
    	}else if(subTabActiveIndex ==0){
        	var $selectedRows_Feature = $('#jTableContainerPlanFeature').jtable('selectedRows');

    		if($selectedRows_Feature.length>0) {
	    		saveFeature();
	    	} 
    	}
		 
    });
});


function saveTestCases(){
	var thediv = document.getElementById('reportbox');
	var productId=document.getElementById("treeHdnCurrentProductId").value; 
    var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
    if(productId==null || productId <=0 || productId == 'null'){
			callAlert("Please select the product");
			return false;
		}
		if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
			callAlert("Please select the workpackage");
			return false;
		}
		
		var tcListsFromUI = [];		
		var $selectedRows = $('#jTableContainerPlan').jtable('selectedRows');
		
		$selectedRows.each(function () {
		    var record = $(this).data('record');
		    var tcId = record.id;
		    	
		    /* #1731:while Saving TestSuite or TestCase unwanted Popup Start 18/08/2015 */
		   // callAlert(tcId);
		    /* #1731:while Saving TestSuite or TestCase unwanted Popup End 18/08/2015 */
		    tcListsFromUI.push(tcId);
		});

		if(tcListsFromUI.length == null || tcListsFromUI==''){
		     console.log("tcListsFromUI.........!!!");
			//jAlert("Select atleast one testcase","ok");
			//alert(tcListsFromUI);
			//callAlert("Select atleast one testcase","ok");
			
		}  
		
		else{
			if (thediv.style.display == "none") {
				$
					.blockUI({
						theme : true,
						title : 'Please Wait',
						message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
					});
			$.post('workpackage.testcase.select.bulk?tcListsFromUI='+tcListsFromUI+'&workpackageId='+workpackageId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();
				    //callJTableFun();    //END - Delete - JTable
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				thediv.style.display = "";
				$.unblockUI();
				});
			}
			 else {
					thediv.style.display = "none";
					thediv.innerHTML = '';
				}
		}
			
		
}

function saveTestSuite(){
	var thediv = document.getElementById('reportbox');
	 var productId=document.getElementById("treeHdnCurrentProductId").value; 
     var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
     if(productId==null || productId <=0 || productId == 'null'){
			callAlert("Please select the product");
			return false;
		}
		if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
			callAlert("Please select the workpackage");
			return false;
		}
		
		var tsListsFromUI = [];
	    
		var $selectedRowsTS = $('#jTableContainerPlanTS').jtable('selectedRows');
      
		$selectedRowsTS.each(function () {
		    var recordTs = $(this).data('record');
		    var tsId = recordTs.id;		  
		    tsListsFromUI.push(tsId);		    
		});

		if(tsListsFromUI.length == null || tsListsFromUI==''){
			jAlert("Select atleast one testsuite","ok");						
		}
		
		else{
			if (thediv.style.display == "none") {
				$
					.blockUI({
						theme : true,
						title : 'Please Wait',
						message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
					});
			$.post('workpackage.testsuite.select.bulk?tsListsFromUI='+tsListsFromUI+'&workpackageId='+workpackageId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();
				    //callJTableFun();    //END - Delete - JTable
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				thediv.style.display = "";
				$.unblockUI();
				});
			}
			else {
				thediv.style.display = "none";
				thediv.innerHTML = '';
			}
		}
}


function saveFeature(){
	var thediv = document.getElementById('reportbox');
	 var productId=document.getElementById("treeHdnCurrentProductId").value; 
    var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		productId = prodId;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			productId=document.getElementById("treeHdnCurrentProductId").value; 
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
				callAlert("Please select the product");
				return false;
			}
		}
	}
	if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
		callAlert("Please select the workpackage");
		return false;
	}
		
		var tsListsFromUI = [];	    
		var $selectedRowsTS = $('#jTableContainerPlanFeature').jtable('selectedRows');
     
		$selectedRowsTS.each(function () {
		    var recordTs = $(this).data('record');
		    var tsId = recordTs.id;		    		  
		    tsListsFromUI.push(tsId);		    
		});

		if(tsListsFromUI.length == null || tsListsFromUI==''){
			jAlert("Select atleast one Feature","ok");			
		}
		
		else{
			if (thediv.style.display == "none") {
				$
					.blockUI({
						theme : true,
						title : 'Please Wait',
						message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
					});
			$.post('workpackage.feature.select.bulk?tsListsFromUI='+tsListsFromUI+'&workpackageId='+workpackageId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});
				var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
				var date = new Date();
				    var timestamp = date.getTime();
				    //callJTableFun();//END - Delete - JTable    
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				thediv.style.display = "";
				$.unblockUI();
				});
			}
			else {
				thediv.style.display = "none";
				thediv.innerHTML = '';
			}
		}
}

$(function() {
    $('.locale').on('click', function(event) {
        event.preventDefault();
        var productId=document.getElementById("treeHdnCurrentProductId").value; 
        var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
        var contextpath = (window.location.pathname).split("/", 2);
  		
  		if(productId==null || productId <=0 || productId == 'null'){
  			callAlert("Please select the product");
  			return false;
  		}
  		if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
  			callAlert("Please select the workpackage");
  			return false;
  		}
  		var root = window.location.protocol	+ "//"+ window.location.host+ "/"+ contextpath[1]+"/administration.workpackage.locale?productId="+productId+"&workpackageId="+workpackageId;
        $.popupWindow(root);
      });
});

//For locale

//Load the dynamic Locale columns for Jtable
function getLocalesForProduct() {
	 var localeNames = new Array();
    $.ajax({
            url : 'common.list.product.locale?workPackageId='+workPackageId,
 			dataType : 'json',
 			error: function() {
 			      callAlert("An error occurred");
 			},
            success : function(jsonData) {        	   
        	   	for (var i = 0; i < jsonData.Options.length; i++) {
	             	var locale = jsonData.Options[i];	             
	             	localeNames.push(locale.DisplayText);
        	   }
               setLocaleNames(localeNames);
               //callJTableFun();//END - Delete - JTable
          }
    });
};

function setLocaleNames(localeNames){
	jsLocaleFieldDisplayTitles = localeNames;
}

function showGridView(value){
	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Grid";		
	currentView="Grid";
	
	removetable();	
	displayResoucesAvailability(value);
}

function showTableView(value){
	removeGridItems();	

	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Table";
	currentView="Table"
	$('#div_Table3').show();
	
	displayResoucesAvailability(value); 	
}

function removeGridItems(){
	$('#blockedResource_Popup').remove();
	$('#resourceReservation_Popup').remove();
}

function removetable(){	
	$('#div_Table3').hide();
}
function getHiddenFieldValues(){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	reservationDate = document.getElementById("currentReservationDate").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	availablilityType = document.getElementById("currentAvailabilityType").value;
	currentView = document.getElementById("currentViewType").value;
	skillId = document.getElementById("skillId").value;
	userRoleId = document.getElementById("userRoleId").value;
	workWeek =  document.getElementById("workWeek").value;
	workYear =  document.getElementById("workYear").value;
	userTypeId = document.getElementById("userTypeId").value;
}

function getBookedResources(value){	
	getHiddenFieldValues();
	availablilityType = "Booked";	
	displayResoucesAvailability(value);	
}

function getBookedOrAvailableResources(value){	
	getHiddenFieldValues();
	availablilityType = "BookedOrAvailable";	
	displayResoucesAvailability(value);	
}

function getUnAvailableResources(value){	
	getHiddenFieldValues();
	availablilityType = "All";	
	if(value == 0){
		weeklyEnable = "false"
	}
	displayResoucesAvailability(value);	
	
}

function displayResoucesAvailability(value)
{	
	document.getElementById("currentAvailabilityType").value = availablilityType;	
	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	getHiddenFieldValues();
	groupDemandId = document.getElementById("groupDemandId").value;
	console.log("---->> "+weeklyEnable);
	if (weeklyEnable == "true"){
		value = 1;
	}
	if(currentView == "Grid")
	{
		removeGridItems();
		if(value == 0){
			getResourceDetailsForBlockingGridView(workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType,currentView);
		}else{
			getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId);
			
		}
	}else if(currentView == "Table"){
		
		if(value == 0){
			getResourcesForReservation(currentWorkPackageName,workPackageId, shiftId, resourceDemandCount,reservationDate, availablilityType);
		}else{
			getResourcesForReservationWeekly(currentWorkPackageName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,resourceDemandCount,groupDemandId,userTypeId)
		}
	}
}

var envFilterId=null;
var testleadFilterId =null;
var dcFilterId=null;
var epFilterId=null;
var testerFilterId=null;
function loadFilterPopup(){
	
	var thediv = document.getElementById('newreportbox');
	 document.getElementById("ResultsGridView").style.display = "none";
   if (thediv.style.display == "none") {
		$.blockUI({
		 	theme : true,
		 	title : 'Please Wait',
		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
		 });

	 var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	 
	 var date = new Date();
	  var timestamp = date.getTime();
	
	   
	 $(function() {			
			$("#datepickerFilter").datepicker();
		});
	 
	 var dcTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=dc&timestamp='+timestamp;
	 var dcTreeDivElement = "dctree";
	 getTreeFilterData(dcTreeURL,dcTreeDivElement);	
	
	var envTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=env&timestamp='+timestamp;
	var envTreeDivElement = "envtree";
	getTreeFilterData(envTreeURL,envTreeDivElement);	
	
	var deviceTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=device&timestamp='+timestamp;
	var deviceTreeDivElement = "devicetree";
	getTreeFilterData(deviceTreeURL,deviceTreeDivElement);	
	
	var epTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=ep&timestamp='+timestamp;
	var epTreeDivElement = "eptree";
	getTreeFilterData(epTreeURL,epTreeDivElement);			
			
	var testerTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=tester&timestamp='+timestamp;
	var testerTreeDivElement = "testertree";
	getTreeFilterData(testerTreeURL,testerTreeDivElement);			
			
	var testleadTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=testlead&timestamp='+timestamp;
	var testleadTreeDivElement = "testleadtree";
	getTreeFilterData(testleadTreeURL,testleadTreeDivElement);			
			
			$("#datepickerFilter").change(function() {
				
				var thediv = document.getElementById('newreportbox');
				/* $.unblockUI(); */
			    if (thediv.style.display == "none") {
			 		$.blockUI({
			 		 	theme : true,
			 		 	title : 'Please Wait',
			 		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
			 		 });
					var status = $("#testBedtestcasestatus_ul").find('option:selected').val();				

				urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workpackageId+"&timeStamp="+timestamp+"&testLeadId="+testleadFilterId+"&testerId="+testerFilterId+"&envId="+envFilterId+"&localeId=0&plannedExecutionDate="+datepickerFilter.value+"&dcId="+dcFilterId+"&executionPriority="+epFilterId+"&status="+status;
				
				//listWorkPackageTestcasesOfSelectedWorkPackage(status);//END - Delete - JTable
				
			 } else {
					thediv.style.display = "none";
					thediv.innerHTML = '';
				}
			});
			
			
	document.getElementById("div_PopupMainFilter").style.display = "block";
	$.unblockUI();
   } else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
}

function gridView(urlvalue){
	
	workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	if(urlvalue==null)
	{
		viewmode="heatmap";	
		urlvalue='workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+sortIds+"&testcaseId=0&jtStartIndex=0&jtPageSize=0";

	}
	var workpackage=$('#dashboard');
	workpackage.empty();
	 document.getElementById("ResultsGridView").style.display = "block";
	 document.getElementById("Results").style.display = "none"; 
	document.getElementById("workpackageTCERGrid").style.display = "none";
	document.getElementById("workpackageTCER").style.display = "block";


	//var url="workpackage.testcaseexecution.list?workPackageId="+workPackageId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlvalue,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			if(data.Records!=null && data.Records.length === 0) {

			} else {
				console.log("length"+data.Records.length);
			}	
			
			$.each(result, function(i,item){
				wptceId=item.id;
                var blogpost = '<li style="margin-right:2px" title="'+item.testcaseName+'" data-hist="'+item.result+'"> <a href="javascript:testcaseByEnvDetailsPopup('+workPackageId+','+item.testcaseId +')">'+item.testcaseId+'</a></li>';
                workpackage.append(blogpost);
                
                $(".dashboard li").hottie({
                	 colorArray : [ 
               	               "#13705E",
                	           "#F21818 ",
                	            "#CFE431"                	                 
                	          ],	
                
                	  readValue : function(e) {
                	        return $(e).attr("data-hist");
                	      }
                });
                
			});			
			
		}
	});
}

function testcaseByEnvDetailsPopup(workPackageId,testcaseId){
	$('#div_PopupMain').find('h4').text("");
	$("#availabilityFilter").remove();
	$("#viewFilter").remove();
	$("#div_Table3").remove();
	$("#popclose").remove();
	$("#blockedResource_Popup").remove();
	if(document.getElementById("div_PopupMaintcer")!=null)
	document.getElementById("div_PopupMaintcer").style.display = "none";
	if(document.getElementById("div_PopupMain")!=null)
	document.getElementById("div_PopupMain").style.display = "none";
	
	document.getElementById("div_PopupMaintcerenv").style.display = "block";

	
	if(document.getElementById("accordion")!=null)
	document.getElementById("accordion").style.display = "block";
	
	loadTestcaseByEnvi(workPackageId,testcaseId);
	
}

function loadTestcaseByEnvi(workPackageId,testcaseId){
	try{
		if ($('#tcrbyenv').length>0) {
			 $('#tcrbyenv').jtable('destroy'); 
		}
	} catch(e) {}
	//init jTable
	$('#tcrbyenv').jtable({

		title: 'Test Case Execution Results',
		selecting : true, //Enable selecting 
		paging : true, //Enable paging
		pageSize : 10,
		editinline : {
			enable : true
		},
		
		actions : {
					//listAction :  'workpackage.testcase.plan.resultsByWorkPackageByTestCase?workPackageId='+workPackageId+'&testcaseId='+testcaseId	
			listAction:'workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId=0&executionPriority=0&result=0&sortBy=0&testcaseId="+testcaseId

		},
		
		fields : {
				
		id : {
			key : true,
			list : false
		},
		testerId : {
			title : 'Tester ID',
			type : 'hidden'
		},
		
	
		actualShiftId : {
			title : 'Actual Shift ID',
			type : 'hidden'
		},
			
		testCaseExecutionResultId : {
			title : 'ID',
			list : false,
			width : "20%"
		},
		testcaseId : {
			title : 'Test Case ID',
			list : true,
			edit:false,
			//type:'hiden',
			width : "20%"
		}, 
	
		 testcaseName : {
			title : 'Test Case Name',
			edit : false,
			width : "20%"
		},
		testcaseDescription : {
			title : 'Description',
			edit : false,
			width : "20%"
		},
		testcaseCode: {title: 'Code',edit: false,width:"20%"},
		environmentCount: {title: 'Environment Count',edit: false,width:"20%"},
		preconditions : {
			title : 'Pre Condition',
			edit : false
		},		
		input : {
			title : 'Input',
				edit : false,
				list:true
			},
		
			expectedOutput : {
			title : 'Expected Output',
				edit : false,
				list:true
			},
			 observedOutput: { 
     		title: 'Observed Output',
     		width: "7%",         
             create: false,
             edit:false	,	
         },
         overalResult: { 
     		title: 'Overal Result',
     		width: "7%",         
             create: false,
             edit:false	,	
         },
         defectsCount: { 
      		title: 'Defects Count',
      		width: "7%",         
              create: false,
              edit:false	,	
          },
  	environments: {
        title:'Environments',
        width: "2%",
        create:false,
        edit:false,
        
        display: function (childData) { 
    				//Create an image that will be used to open child table 
    				
/*  Abort_completed functionality for view Testcases Start . */
		        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Evidence" />'); 

		        	/*  Abort_completed functionality for view Testcases End . */
		        	//Open child table when user clicks the image 
		        	$img.click(function (data) { 
		        	$('#jTableContainerResult').jtable('openChildTable', 
		        	$img.closest('tr'), 
		        	{ 
		        	title: 'Testcase Result By Environments',
		        	paging : false, //Enable paging
		    		pageSize : 10,
		        	actions: { 
		        		//listAction: 'testcase.list.eveidence?tcerId='+childData.record.testCaseExecutionResultId
		        		listAction : 'workpackage.testcase.plan.resultsByWorkPackageByTestCase?workPackageId='+workPackageId+'&testcaseId='+childData.record.testcaseId	
		          	}, 
		        	fields: {
		        		testStepsDetails: {
		    		        title:'Test Step',
		    		        width: "2%",
		    		        create:false,
		    		        edit:false,
		    		        display: function (childData) { 
		    	        				//Create an image that will be used to open child table 
		    	        				
/*  Abort_completed functionality for view Testcases Start . */
		    				        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Test Step List" />'); 

		    				        	/*  Abort_completed functionality for view Testcases End . */
		    	        				//Open child table when user clicks the image 
		    				        	$img.click(function (data) { 
		    				        	$('#jTableContainerResult').jtable('openChildTable', 
		    				        	$img.closest('tr'), 
		    				        	{ 
		    				        	title: 'Test Steps Details',
		    				        	actions: { 
		    				        		//listAction: 'testcase.teststep.list.byTestCaseExeResId?tcerId='+childData.record.testCaseExecutionResultId
		    				        		listAction: 'teststep.plan.list?testCaseId='+childData.record.testcaseId+'&tcerId='+childData.record.testCaseExecutionResultId
		    				          	}, 
		    				        	fields: { 
		    				        		testcaseId : {
		    				    				
		    				    				list : false
		    				    			},
		    				    			
		    				            	teststepexecutionresultid : {
		    				    				key : true,
		    				    				list : false
		    				    			},	
		    				        	
		    				        		testStepsName: { 
		    				            		title: 'Name',  
		    				            		width: "20%",                          
		    				                    create: false,
		    				                    edit:false
		    				            	},
		    				            	description:{
		    				            		title: 'Description',  
		    				            		width: "30%",                          
		    				                    create: false,
		    				                    edit:false   
		    				            	},
		    				            	input: { 
		    				            		title: 'Test Step Input', 
		    				            		width: "20%",                         
		    				                    create: false,
		    				                    edit:false
		    				               },
		    				               expectedOutput: { 
		    				            		title: 'Expected Output',  
		    				            		width: "25%",                        
		    				                    create: false,
		    				                    edit:false
		    				                },
		    				                code: { 
		    				            		title: 'Code',  
		    				            		width: "7%",         
		    				                    create: false,
		    				                    list:false,
		    				                    edit:false
		    				                },
		    				                observedOutput: { 
		    				            		title: 'Observed Output',
		    				            		width: "7%",         
		    				                    create: false,
		    				                    edit:true							                
		    				                },
		    				    			result : {
		    				    				title : 'Result',
		    				    				width : "20%",
		    				    				edit : true,
		    				    				options : {
		    				    					'1' : 'Pass',
		    				    					'2' : 'Fail',
		    				    					'3' : 'No Run',
		    				    					'4' : 'Blocked'
		    				    				}
		    				    			},
		    				                comments: { 
		    				            		title: 'Comments',
		    				            		width: "7%",         
		    				                    create: false,
		    				                    edit:true							                
		    				                },
		    				                
		    				              
		    				                evidenceDetails: {
		    				    		        title:'Evidence',
		    				    		        width: "2%",
		    				    		        create:false,
		    				    		        edit:false,
		    				    		        display: function (childData) { 
		    				    				        	var $img = $('<img src="css/images/upload.gif" title="Evidene List" />'); 
		    				    				        	$img.click(function (data) { 
		    				    				        	$('#jTableContainerResult').jtable('openChildTable', 
		    				    				        	$img.closest('tr'), 
		    				    				        	{ 
		    				    				        	title: 'Evidence List',
		    				    				        	actions: { 
		    				    				        		listAction: 'testcase.list.eveidence?tcerId='+childData.record.teststepexecutionresultid+'&type=teststep',	
		    				    				        	
		    				    				          	}, 
		    				    				        	fields: { 
		    				    				        		
		    				    				        		evidenceid:{
		    					    				        		  type:'hidden'                     
		    					    				                
		    					    				        	},
		    					    				        	fileuri:{
		    						   				        		title: 'File Uri',  
		    						   				        		width: "30%",                          
		    						   				                create: false,
		    						   				                edit:false,
		    						   				                list:false
		    						   				               
		    						   				        	},
		    				    				        		evidencename: { 
		    				    				        		title: 'Evidence Name',  
		    				    				        		width: "20%",                          
		    				    				                create: false,
		    				    				               display: function (data) {
		    				    				    				$("#evidenceUplaoded").append("<div style=display:none; id=evidenceTestStepFilename"+data.record.evidenceid+"></div>");
		    				    				    				document.getElementById("evidenceTestStepFilename"+data.record.evidenceid).innerHTML = data.record.fileuri;
		    				    			                       return $("<a style='color: #0000FF'; href=javascript:loadPopupEvidence('evidenceTestStepFilename"+data.record.evidenceid+"');>" + data.record.evidencename + "</a>");
		    				    				                 }    
		    				    				                
		    				    				        	},
		    				    				        
		    				    				        	description:{
		    				    				        		title: 'Description',  
		    				    				        		width: "30%",                          
		    				    				                create: true   
		    				    				        	},
		    				    				        	filetype: { 
		    				    				        		title: 'File Type', 
		    				    				        		width: "20%",                         
		    				    				                create: false
		    				    				           },
		    				    				         
		    				    				           size: { 
		    				    				        		title: 'Size',  
		    				    				        		width: "25%",                        
		    				    				                create: false
		    				    				            }
		    				    				            
		    				    				           } 
		    				    				        }, 
		    				    				        	function (data) { //opened handler 
		    				    				        	data.childTable.jtable('load'); 
		    				    				        	}); 
		    				    				        	}); 
		    				    				        		//Return image 
		    				    				        		return $img; 
		    				    				        	}, 
		    				          	},
		    				           }, 
		    				        }, 
		    				        	function (data) { //opened handler 
		    				        	data.childTable.jtable('load'); 
		    				        	}); 
		    				        	}); 
		    				        		//Return image 
		    				        		return $img; 
		    				        	}, 
		          	},
		          	runConfigurationName: { 
			              		title: 'Environments',
			              		width: "7%",         
			                      create: false,
			                      edit:false		
			                  },
			                  
			      			executionPriorityDisplayName :{
			      				title: 'Execution Priority',
			      				width:"20%",
			      				edit:false,
			      				list:true,
			      				display:function(data)
			      	    		 {
			      					var dataid=data.record.executionPriorityId;

			      					$('.rating-cancel').hide();
			      					var inputId=data.record.id+data.record.testcaseId;
			      					if(dataid == 1){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" checked="checked" disabled="disabled" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" disabled="disabled" /> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/>   ')
			      					}else if(dataid == 2){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" checked="checked" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/>   ')
			      					}else if(dataid == 3){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" checked="checked" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/>  ')
			      					}else if(dataid == 4){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" checked="checked" disabled="disabled"/>   ')
			      					}else if(dataid == 5){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/>  ')
			      					}else if(dataid == 6){
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/> ')
			      					}else {
			      						return $('<input name="star'+inputId+'" type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="Low" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star" value="3~'+inputId+'" title="P2" disabled="disabled"/> <input name="star'+inputId+'" type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" disabled="disabled"/>  ')
			      					}
			             			
			      	    		 }
			      				//options :'administration.executionPriorityList'
			      			},
		        		 observedOutput: { 
		              		title: 'Observed Output',
		              		width: "7%",         
		                      create: false,
		                      edit:false		
		                  },
		      			
		     			
		     			comments : {
		     				title : 'Comments',
		     				width : "20%",
		     				edit : false	
		     			},
		     			result : {
		     				title : 'Result',
		     				width : "20%",
		     				edit : false,
		     				options : {
		     					'1' : 'Pass',
		     					'2' : 'Fail'
		     				}	
		     			}, 
		     			 executionTime: { 
		              		title: 'Execution Time',
		              		width: "7%",         
		                      create: false,
		                      edit:false,
		                      display: function (data) {
		                     	 if(data.record.executionTime!=null)
		                     		 return data.record.executionTime+" sec";
		                     	 else
		                     		 return "0 sec";
		     	                 }  	
		                  },
		                  defectsCount: { 
			             		title: 'Defect Count',
			             		width: "7%",         
			                     create: false,
			                     edit:false		
			                 },
		     			evidence: {
		     		        title:'Evidence',
		     		        width: "2%",
		     		        create:false,
		     		        edit:false,
		     		        
		     		        display: function (childData) { 
		     	        				//Create an image that will be used to open child table 
		     	        				
/*  Abort_completed functionality for view Testcases Start . */
		     				        	var $img = $('<img src="css/images/list_metro.png" style="pointer-events:visible" title="Evidence" />'); 

		     				        	/*  Abort_completed functionality for view Testcases End . */
		     	        				//Open child table when user clicks the image 
		     				        	$img.click(function (data) { 
		     				        	$('#jTableContainerResult').jtable('openChildTable', 
		     				        	$img.closest('tr'), 
		     				        	{ 
		     				        	title: 'Evidence Details',
		     				        	actions: { 
		     				        		//listAction: 'testcase.list.eveidence?tcerId='+childData.record.testCaseExecutionResultId
		     				        		  listAction: 'testcase.list.eveidence?tcerId='+childData.record.testCaseExecutionResultId+'&type=testcase'		
		     				          	}, 
		     				        	fields: { 
		     				        		evidenceid: { 
		     				        		title: 'Id',  
		     				        		width: "20%",                          
		     				                create: false,
		     				                list:false
		     				        	},
		     				        	fileuri:{
		     				        		title: 'File Uri',  
		     				        		width: "30%",                          
		     				                create: false,
		     				                edit:false,
		     				                list:false
		     				        	},
		     			        		evidencename: { 
		     			        		title: 'Evidence Name',  
		     			        		width: "20%",                          
		     			                create: false,
		     			               display: function (data) {
		     			    				$("#evidenceUplaoded").append("<div style=display:none; id=evidenceTestStepFilename"+data.record.evidenceid+"></div>");
		     			    				document.getElementById("evidenceTestStepFilename"+data.record.evidenceid).innerHTML = data.record.fileuri;
		     		                       return $("<a style='color: #0000FF'; href=javascript:loadPopupEvidence('evidenceTestStepFilename"+data.record.evidenceid+"');>" + data.record.evidencename + "</a>");
		     			                 }    
		     			                
		     			        	},
		     				        	
		     				           description: { 
		     				        		title: 'Description',  
		     				        		width: "20%",         
		     				                create: false,
		     				                edit:false
		     				            },
		     				            filetype: { 
		     				        		title: 'File Type', 
		     				        		width: "10%",                         
		     				                create: false,
		     				                edit:false
		     				           },
		     				            size: { 
		     				        		title: 'Size',  
		     				        		width: "10%",                        
		     				                create: false
		     				            }
		     				           } 
		     				        }, 
		     				        	function (data) { //opened handler 
		     				        	data.childTable.jtable('load'); 
		     				        	}); 
		     				        	}); 
		     				        		//Return image 
		     				        		return $img; 
		     				        	}, 
		           	},	
		            
		           } ,
		           recordsLoaded: function(event, data) {

						 $('.hover-star').rating({ 
							  
					 }); 
			        },
		        }, 
		       
		        	function (data) { //opened handler 
		        	data.childTable.jtable('load'); 
		        	}); 
		        	}); 
		        	
		        		//Return image 
		        		return $img; 
		        	},		        	
			}      	
		},
	});
	
	$('#tcrbyenv').jtable(
			'load',
			{
				workPackageName : $('#workPackageName_dd')
						.children('div').attr('id')

			});
	$('#tcrbyenv').jtable('load');
	
	var jscrolheight = $("#tcrbyenv").height();
	var jscrolwidth = $("#tcrbyenv").width();

	$(".jScrollContainer").on(
			"scroll",
			function() {
				var lastScrollLeft = 0;

				var documentScrollLeft = $(".jScrollContainer")
						.scrollLeft();

				if (lastScrollLeft < documentScrollLeft) {
					$("#tcrbyenv").width(
							$(".jtable").width()).height(
							$(".jtable").height());
					lastScrollLeft = documentScrollLeft;
				} else if (lastScrollLeft >= documentScrollLeft) {
					$("#tcrbyenv").width(jscrolwidth)
							.height(jscrolheight);
				}
			});
}

/* Pop Up close function */
function popupClosetcer() {	
	$("#div_PopupMaintcer").fadeOut("normal");	
	viewmode="heatmap";	
	workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
	url='workpackage.execution.list?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+sortIds+"&jtStartIndex=0&jtPageSize=0";
	gridView(url);  
}

function popupClosetcerEnv() {	
	$("#div_PopupMaintcerenv").fadeOut("normal");	
}

function popupCloserc() {	
	$("#div_PopupMainrc").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

function popupTestCaseExecutionHistoryClose() {	
	$("#div_PopupTestCaseExecutionHistory").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

var envTCERFilterId=null;
var epTCERFilterId=null;
var resultFilterId=null;
function loadTCERFilterPopup(viewmode){
	 var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	 var date = new Date();
	   var timestamp = date.getTime();
	   var envTCERTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=env&timestamp='+timestamp;
	   var envTCERTreeDivElement = "envTCERtree";
	   getTreeFilterData(envTCERTreeURL,envTCERTreeDivElement);

		var deviceTCERTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=device&timestamp='+timestamp;
		var deviceTCERTreeDivElement = "deviceTCERtree";
		getTreeFilterData(deviceTCERTreeURL,deviceTCERTreeDivElement);
	   	
	 var epTCERTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=ep&timestamp='+timestamp;
	 var epTCERTreeDivElement = "epTCERtree";
	 getTreeFilterData(epTCERTreeURL,epTCERTreeDivElement);
			
		 var resultTreeURL = 'workpackage.allocate.testcase.filter?workpackageId='+workpackageId+'&param=result&timestamp='+timestamp;
		 var resultTreeDivElement = "resulttree";
		 getTreeFilterData(resultTreeURL,resultTreeDivElement);
	 			
	document.getElementById("div_PopupMainTCERFilter").style.display = "block";
}

function popupCloseTCERFilter() {
	$("#div_PopupMainTCERFilter").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

function loadWorkShifts(){
	var workPackageId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	$('#shiftList_ul').empty();
	$('#shiftList_ul').select2('data', null);
	$('#testLeadList_ul').select2('data', null);
    $('#testerList_ul').select2('data', null);
	//$('#shiftList_dd div').remove(); 
	$.post('common.list.workshift.list.workpackage?workpackageId='+workPackageId,function(data) {
		var ary = (data.Options);
       	$.each(ary, function(index, element) {
			$('#shiftList_ul').append('<option id="' + element.Number + '" ><a href="#">' + element.DisplayText + '</a></option>');	
   });
	});
}

// define scope ends
var productType = 0;
function environmentDetailsPlan(){
	productType = document.getElementById("productType").value;
	workpackageId=key;
	if(workpackageId == null || workpackageId <= 0 || workpackageId == 'null' || workpackageId == ''){
		callAlert("Please select the Workpackage");
		return false;
	}
	//var productVersionId= document.getElementById("hdnProductVersionId").value;
	if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == '' )
	{
		productVersionId= -1;	 
	}
	if(productId == null || productId <= 0 || productId == 'null' || productId == '' )
	{
		productId= -1;	 
	}
	//var productVersionId= -1;
	//var productId=-1;
	var date = new Date();
    var timestamp = date.getTime();
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId=-1&productId=-1&workpackageId="+workpackageId+"&testRunPlanId=-1";
	//listEnvCombinationByProductPlan(productId,productVersionId); //END - Delete - JTable
	divPopupMainTitleEnv(popupTitle_EnvironmentCombination);
	showEnvironmentCombinationTable(1);
}

function terminalMapping(ecId,productVersionId,productId){
	$(".tilebody").empty();
	$(".tilebody").remove();
	
	 document.getElementById("div_PopupTerminal").style.display = "block";
	 $("#allterminals").empty();
	 $("#terminalsmapped").empty();
	var workpackageId=key;
	var testRunPlanId=-1;
	var url="administration.host.list.mapping?jtStartIndex=-1&jtPageSize=-1&ecId="+ecId+"&workpackageId="+workpackageId+"&testRunPlanId="+testRunPlanId+"&filter=1";
	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			
			var result = data.Records;
			
			if(result.length==0){
				$("#allterminals").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Terminals</b></span>");
				$("#listCount_allterminals").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var terminalid = item.hostIpAddress;					
					terminalid=terminalid;					
					$("#allterminals").append("<li id='"+item.hostId +"' title='"+item.hostIpAddress+"' style='color: black;'>"+terminalid+"("+item.hostStatus+")</li>");
			 	});
				$("#listCount_allterminals").text(result.length);
			}
		}
	});  
	
	urlmappedDevices='administration.host.mappedList?testRunPlanId=-1&ecId='+ecId+'&workpackageId='+workpackageId;        	
		
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#terminalsmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Terminals</b></span>");
				$("#listCount_terminalsmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.hostIpAddress;					
					tsid=tsid;
					$("#terminalsmapped").append("<li id='"+item.runconfigId +"'  title='"+item.hostIpAddress+"' style='color: black;'>"+tsid+"("+item.hostStatus+")</li>");
				});
				$("#listCount_terminalsmapped").text(listOfData.length);
			}
		}
	});	
	
	Sortable.create(document.getElementById("allterminals"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		
		onAdd: function (evt){
			var draggeditem = trim(evt.item.id);
			
		 	var urlUnMapping = "";

		 	urlUnMapping = 'administration.host.mapTerminal.wp?hostId=-1&workpackageId='+workpackageId+'&runconfigId='+draggeditem+'&type=unmap&ecId='+ecId;

		 	if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_allterminals").text($("#allterminals").children().length);
		 	
			 $.ajax({
				  type: "POST",
					url:urlUnMapping,
					success : function(data) {
						if(data.Result=="ERROR"){
							$("#basicAlert").css("z-index", "100001");
							callAlert(data.Message);
				    		return false;
				    	}
						else{	
							console.log("left added");
							evt.item.id = data.Records[0].hostId;								
							}
						}					
				});
			},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
		 	
		 	if($("#allterminals").children().length == 0) {
		 		$("#allterminals").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Terminals</b></span>");
		 		$("#listCount_allterminals").text(0);
		 	}else{
				$("#listCount_allterminals").text($("#allterminals").children().length);
		 	}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});	
	
	Sortable.create(document.getElementById("terminalsmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){ 
			var draggeditem = trim(evt.item.id);
						
			urlMapping = 'administration.host.mapTerminal.wp?hostId='+draggeditem+'&workpackageId='+workpackageId+'&runconfigId=-1&type=map&ecId='+ecId;
		 	if($("#emptyListMapped").length) $("#emptyListMapped").remove();
			$("#listCount_terminalsmapped").text($("#terminalsmapped").children().length);
			
			
				 $.ajax({
				  type: "POST",
					url:urlMapping,
					success : function(data) {
						if(data.Result=="ERROR"){
							$("#basicAlert").css("z-index", "100001");
				    		callAlert(data.Message);
				    		return false;
				    	}
						else{
							console.log("right added");
							evt.item.id = data.Records[0].runconfigId;
							}
						}						
				});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
		 
		 	if($("#terminalsmapped").children().length == 0) {
		 		$("#terminalsmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Terminal</b></span>");
				$("#listCount_terminalsmapped").text(0);				
		 	}else{
				$("#listCount_terminalsmapped").text($("#terminalsmapped").children().length);							 		
		 	}
		},
		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}

function popupTerminalClose() {	
	$("#div_PopupTerminal").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

/*//BEGIN - Delete - JTable
function listEnvCombinationByProductPlan(productId,productVersionId){	
	var testRunPlanId=-1;
	var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	var terminal_device_viewtitle = 'View Mapped Devices';
	var maptarget_terminal_device_title = 'Map Target Devices';
	if(productType == 1 || productType == 5){
		terminal_device_viewtitle = 'View Mapped Devices';
		maptarget_terminal_device_title = 'Map Target Devices';
	}else if(productType == 2 || productType == 3){
		terminal_device_viewtitle = 'View Mapped Terminals';
		maptarget_terminal_device_title = 'Map Target Terminals';
	}
	
	//divPopupMainTitle(popupTitle_EnvironmentCombination);
	divPopupMainTitleEnv(popupTitle_EnvironmentCombination);
	
 	try{
		 $('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable('destroy'); 
		if ($('#div_PopupMain_Allocate_Env #JTable_Allocate').length>0) {
			 $('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable('destroy'); 
		}
	} catch(e) {}
	
	$('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable({
			title: 'Select Environment Combination',
			selecting: true, //Enable selecting 
	        paging: true, //Enable paging
	        pageSize: 10, //Set page size (default: 10)
	        editinline:{enable:true},
			multiselect: false, //Allow multiple selecting
	        selectingCheckboxes: false,  //Show checkboxes on first column

	          actions: {
	        	listAction: urlToGetEnvironmentCombinationByProduct,
	        },  
	        fields : {
	        	envionmentCombinationId:{
	                title: 'envionmentCombination Id',
	                list:false,
	                width:"20%"
	            }, 
	            environmentCombinationName:{
	                title: 'Environment Combination',
	                list:true,
	                edit:false,
	                width:"20%"
	            },
	            deviceMapping:{
	            	title: terminal_device_viewtitle,
	            	width: "5%",
	            	edit: true,
	            	create: false,
	            	display: function (environmentData) { 
	          
					//Create an image that will be used to open child table	          
					var $img = $('<img src="css/images/mobile_metro.png" style="pointer-events:visible" title="View Target Devices"   style="padding-left: 37px;"/>'); 
	          
					//Open child table when user clicks the image 	          
	          
					if(environmentData.record.productType==1 || environmentData.record.productType==5){

   					$img.click(function () { 
   						$('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable('openChildTable', 
   						$img.closest('tr'), 
   					{ 
   						title: 'View Target Devices', 
   					 	editinline:{enable:true},
   					 	paging: true, //Enable paging
   			        	pageSize: 10, 
   						recordsLoaded: function(event, data) {
   		        	 		$(".jtable-edit-command-button").prop("disabled", true);
   		         			},
   						actions: { 
   							//listAction:  'administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId=-1&workpackageId="+workpackageId
							listAction:  'administration.genericdevice.mappedList?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId=-1&workpackageId="+workpackageId	
   							}, 
	   	 				fields: {
   		 					runconfigId: {
   	 				    		key: true,
   		                		list: false,
   		                		create: false
   		            		},
   		            		runConfigurationName: {
		   		                title: 'Test Configuration',
   		                		inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
   		                		list:false
   		            		}, 
   		         			environmentcombinationName: {
   		                		title: 'Environment Combination',
   		                		inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
   		            		}, 
   		         			deviceName: {
   		                		title: 'Device Name',
   		                		inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
   		            		}, 
   					},
   	
		         formSubmitting: function (event, data) {               	
        		     data.form.validationEngine();
            		return data.form.validationEngine('validate');
        		}, 
         		//Dispose validation logic when form is closed
         		formClosed: function (event, data) {
            		data.form.validationEngine('hide');
            		data.form.validationEngine('detach');
        		}
   			}, function (data) { //opened handler 
   				data.childTable.jtable('load'); 
   			}); 
   		}); 
   	 	}else{
   	 		$img.click(function () { 
				$('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable('openChildTable', 
					$img.closest('tr'), 
					{ 
						title: 'View Target Terminals', 
					 	editinline:{enable:true},
					 	paging: true, //Enable paging
			        	pageSize: 10, 
						recordsLoaded: function(event, data) {
		        	 		$(".jtable-edit-command-button").prop("disabled", true);
		         			},
						actions: { 
							//listAction:  'administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId=-1&workpackageId="+workpackageId
							listAction:  'administration.host.mappedList?testRunPlanId=-1&ecId='+environmentData.record.envionmentCombinationId+"&workpackageId="+workpackageId
							}, 	
	 					fields: {
	 						runconfigId: {
	 				    	key: true,
		                	list: false,
		                	create: false
		            	},		            
			            runConfigurationName: {
		                	title: 'Test Configuration',
		                	inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
		                	list:false
		            	}, 
		         		environmentcombinationName: {
		                	title: 'Environment Combination',
		                	inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
		            	}, 
		         		hostName: {
		                	title: 'Host Name',
		                	inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
		            	}, 
				},	
     			formSubmitting: function (event, data) {               	
         			data.form.validationEngine();
        			return data.form.validationEngine('validate');
    			}, 
     			//Dispose validation logic when form is closed
     			formClosed: function (event, data) {
       				data.form.validationEngine('hide');
        			data.form.validationEngine('detach');
    			}
			}, function (data) { //opened handler 
				data.childTable.jtable('load'); 
			}); 
		}); 
   	 	}
	          
		//Return image	          
		return $img;	          
		}	      
	},	      
	envionmentCombinationMapping:{	       
		title : maptarget_terminal_device_title,
		list : true,
		create : false,
		edit : false,
		width: "10%",
		display:function (data) { 
	          
			//Create an image that will be used to open child table 
	 		///*  Abort_completed Functionality For Mapped Devices start . 
			var $img = $('<img src="css/images/mapping.png" style="pointer-events:visible" title="Target Devices Mapping" />'); 
	 		///*  Abort_completed Functionality For Mapped Devices End .            
				//Open child table when user clicks the image	          
				$img.click(function () {
       				if(data.record.productType==1 || data.record.productType==5){
		           		//Environment Combinataion Name For Avaliable Devices   
		           		if(data.record.status==0){
		           			devicecMapping(data.record.envionmentCombinationId,productVersionId,productId, data.record.environmentCombinationName);
		           			callAlert(" ");		           			
		           			$("#basicAlert .modal-header button").trigger('click');
		           		}else{
					        callAlert("Environment Combinations are Already mapped to test case! To modify Target Device please unselect the Environment Combination.");
					    }
		           		//Environment Combinataion Name For Avaliable Devices End
		           	}else if(data.record.productType==2 || data.record.productType==3){
		           		if(data.record.status==0){
					        terminalMapping(data.record.envionmentCombinationId,productVersionId,productId);
						    callAlert(" ");		           			
		           		    $("#basicAlert .modal-header button").trigger('click');
		           		}else{
					      callAlert("Environment Combinations are Already mapped to test case! To modify Target Server please unselect the Environment Combination.");
					 }
		        }
			 });	          
		return $img;	       
		}
	   },
	      status: {
               title: 'Selected',//Selected
               list:true,
               create: false,
               edit:true,
               type : 'checkbox',
                    values: {'0' : 'No','1' : 'Yes'},
                    display:function(data){
                         if(data.record.status==1){                                      
                              return '<input type="checkbox" id="status" checked onclick=saveEnvCombMap(0,'+data.record.envionmentCombinationId+'); value='+data.record.status+'>';                                  
                          }else if(data.record.status==0){                                             
                              return '<input type="checkbox" id="status" onclick=saveEnvCombMap(1,'+data.record.envionmentCombinationId+'); value='+data.record.status+'>';
                          }else{
                              return '<input type="checkbox" id="status" value='+data.record.status+'>';
                         }                                          
                     }
           		},	 
	        },  
	});
	
	$('#div_PopupMain_Allocate_Env #JTable_Allocate').jtable('load'); 
	
	///*  Abort_completed functionality for Mapped Devices Start . 
	 if(editableFlag){
			 $(".jtable").removeClass("pointersnone");
		 }else{
			 $(".jtable").addClass("pointersnone");
			
		 }
	 ///*  Abort_completed functionality for Mapped Devices End . 
	}*///END - Delete - JTable

/*//BEGIN - Delete - JTable	
function devicecMapping(envionmentCombinationId,productVersionId,productId, envionmentCombinationName){
	
	productId= productId;
	$('#searchdev').keyup(function() {
		var txt=$('#searchdev').val();
		
		$("#listCount_alldevices").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		
			$('#alldevices li').show().filter(function() {
	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");
	        
	    	  return !~text.indexOf(val);
	    
	    }).hide();
			
			$("#listCount_alldevices").text(+resArr.length+" / "+$('#alldevices li').length);
			if(txt==""){
				$("#listCount_alldevices").text($('#alldevices li').length);			
			}
	});	
	
	$('#searchmapdev').keyup(function() {
		var txt=$('#searchmapdev').val();
		if($('#searchmapdev').value==''){
			//alert();
			$("#listCount_devicesmapped").text($('#devicesmapped li').length);
		}
	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
	    var resArr = [];
		$('#devicesmapped li').show().filter(function() {
	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	    	 if(!~text.indexOf(val) == false) resArr.push("item");
	        return !~text.indexOf(val);
	        
	        
	    }).hide();
		
		$("#listCount_devicesmapped").text(+resArr.length+" / "+$('#devicesmapped li').length);
		//$("#listCount_alldevices").text(+resArr.length+" / "+$('#alldevices li').length);
		if(txt==""){
			$("#listCount_devicesmapped").text($('#devicesmapped li').length);		
		}
	});
	
	$(".tilebody").empty();
	$(".tilebody").remove();
	
	 document.getElementById("div_PopupDevices").style.display = "block";
	 $("#alldevices").empty();
	 $("#devicesmapped").empty();
	 var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;
	 //Environment Combinataion Name For Available Devices
	 $("#div_PopupDevices").find("h4").text("Map Devices to envionmentCombination:-"+ envionmentCombinationName );
	 //Environment Combinataion Name For Avaliable Devices End

	var url="admin.genericDevices.list.mapping?productId="+productId+"&jtStartIndex=-1&jtPageSize=-1&filter=1&productversionId=-1&environmentCombinationId="+envionmentCombinationId+"&workpackageId="+workpackageId+"&testRunPlanId=-1";
	
	//
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			
			var result = data.Records;
			
			if(result.length==0){
				$("#alldevices").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_alldevices").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var devicesid = item.name;					
					devicesid=trim(devicesid);					
					$("#alldevices").append("<li id='"+item.genericsDevicesId +"' title='"+item.UDID+"' style='color: black;'>"+devicesid+"</li>");
			 	});
				$("#listCount_alldevices").text(result.length);
			}
		}
	});  
	
	//urlmappedDevices="admin.genericDevices.list?productId="+productId+"&jtStartIndex=-1&jtPageSize=-1&filter=1&productversionId="+versionId+"&environmentCombinationId="+envionmentCombinationId+"&workpackageId="+workpackageId+"&testRunPlanId="+testRunPlanId;
	//urlmappedDevices='administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+envionmentCombinationId+"&testRunPlanId=-1&workpackageId="+workpackageId+"&jtStartIndex=-1&jtPageSize=-1";		
	urlmappedDevices='administration.genericdevice.mappedList?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+envionmentCombinationId+"&testRunPlanId=-1&workpackageId="+workpackageId+"&jtStartIndex=-1&jtPageSize=-1";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#devicesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_devicesmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.deviceName;					
					tsid=trim(tsid);
					
					$("#devicesmapped").append("<li id='"+item.runconfigId +"'  title='"+item.UDID+"' style='color: black;'>"+tsid+"</li>");
				});
				$("#listCount_devicesmapped").text(listOfData.length);
			}
		}
	});
	///*  Abort_completed functionality for Mapped Devices Start . 
	if(editableFlag) {
		$("#devicesmapped, #alldevices").removeClass("pointersnone");
	}else{
		$("#devicesmapped, #alldevices").addClass("pointersnone");
	}
	///*  Abort_completed functionality for Mapped Devices End . 
	Sortable.create(document.getElementById("alldevices"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		
		onAdd: function (evt){
			var draggeditem = trim(evt.item.id);
		 	var urlUnMapping = "";
		 	urlUnMapping = 'administration.device.maprunConfiguration.workpackage?environmentCombinationId='+envionmentCombinationId+'&deviceId=-1&type=unmap&workpackageId='+workpackageId+'&runConfigId='+draggeditem;
		 	if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_alldevices").text($("#alldevices").children().length);
		 	
			 $.ajax({
				  type: "POST",
					url:urlUnMapping,
					success : function(data) {
						if(data.Result=="ERROR"){
							$("#basicAlert").css("z-index", "100001");
							callAlert(data.Message);
				    		return false;
				    	}
						else{	
							console.log("left added");
							evt.item.id = data.Records[0].deviceId;	
							}
						}					
				});
			},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
		 	if($("#alldevices").children().length == 0) {
		 		$("#alldevices").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
		 		$("#listCount_alldevices").text(0);
		 	}else{
				$("#listCount_alldevices").text($("#alldevices").children().length);
		 	}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});
	
	Sortable.create(document.getElementById("devicesmapped"), {
			group: "words",
			animation: 150,
			onAdd: function (evt){ 
				var draggeditem = trim(evt.item.id);
				urlMapping = 'administration.device.maprunConfiguration.workpackage?environmentCombinationId='+envionmentCombinationId+'&deviceId='+draggeditem+'&type=map&workpackageId='+workpackageId+"&runConfigId=-1";
			 	if($("#emptyListMapped").length) $("#emptyListMapped").remove();
				$("#listCount_devicesmapped").text($("#devicesmapped").children().length);
				
				
					 $.ajax({
					  type: "POST",
						url:urlMapping,
						success : function(data) {
							if(data.Result=="ERROR"){
					    		callAlert(data.Message);
					    		return false;
					    	}
							else{
								console.log("right added");
								evt.item.id = data.Records[0].runconfigId;	
								}
							}						
					});				
			},
			onUpdate: function (evt){  },
			onRemove: function (evt){ 
			 	if($("#devicesmapped").children().length == 0) {
			 		$("#devicesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
					$("#listCount_devicesmapped").text(0);				
			 	}else{
					$("#listCount_devicesmapped").text($("#devicesmapped").children().length);							 		
			 	}
			},
			onStart:function(evt){ },
			onEnd: function(evt){}
		});	
}*///END - Delete - JTable

$("#environmentDetails").click( function()
        {
			event.preventDefault();
		    var productId=document.getElementById("treeHdnCurrentProductId").value;
		    if(productId== -1){
		    	productId = prodId;
		    }
			//var productId =prodId;
		    var workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value; 
		    if(productId==null || productId <=0 || productId == 'null'){
					callAlert("Please select the product");
					return false;
				}
		    if(workpackageId==null || workpackageId <=0 || workpackageId == 'null'){
					callAlert("Please select the workpackage");
					return false;
			}
			environmentDetailsPlan();
        }
     );
function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}
/*//BEGIN - Delete - JTable
function saveEnvCombMap(isSelected,envionmentCombinationId){
    var workpackageId= document.getElementById("treeHdnCurrentWorkPackageId").value;

    var testRunPlanId=-1;
    if(isSelected==1){
           urlMapping = 'administration.maprunConfiguration.workpackage?environmentCombinationId='+envionmentCombinationId+'&type=map&workpackageId='+workpackageId;
    }
    if(isSelected==0){
           urlMapping = 'administration.maprunConfiguration.workpackage?environmentCombinationId='+envionmentCombinationId+'&type=unmap&workpackageId='+workpackageId;
    }
    $.ajax({
           type: "POST",
     contentType: "application/json; charset=utf-8",
           url : urlMapping,
           dataType : 'json',
           success : function(data) {
                  if(data.Result=="ERROR"){
                        callAlert(data.Message);
                  return false;
           }else{
            	   urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId=-1&productId=-1&workpackageId="+workpackageId+"&testRunPlanId=-1";
           	       listEnvCombinationByProductPlan(-1,-1); 
           	}
                 
           }
    }); 
}*///END - Delete - JTable
function handleResultsRadios(sortIds) {
 	viewmode="list";	
 	workpackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
 	document.getElementById('filterContent').style.display = 'none';
	 document.getElementById('environmentContent').style.display='none';
	 document.getElementById('featureWiseContent').style.display = 'none';
	 document.getElementById('priorityWiseContent').style.display='none';
	 document.getElementById('decoupleWiseContent').style.display='none';
	 document.getElementById('TargTestBeds').style.display='none';
	 document.getElementById('TargTestBedsForFeature').style.display='none';
	 document.getElementById('featureByTestbeds').style.display='none';
 	url='workpackage.execution.list?workPackageId='+workpackageId+"&timeStamp="+timestamp+"&envId="+envTCERFilterId+"&executionPriority="+epTCERFilterId+"&result="+resultFilterId+"&sortBy="+sortIds+"&testcaseId=0";
 	
 	if(sortIds==1){
 		listEnvCombOfSelectedWorkPackage(url);
 		
 	}else if(sortIds==2){
 		listTestcasesResOfSelectedWorkPackage(url);
 		
 	}else if(sortIds==3){
		url='administration.workPackage.testbed?workPackageId='+workPackageId;
       	testBeds(url);       	
	 
	}else if(sortIds==4){
 		listPriorityWiseOfSelectedWorkPackage(url);
 		
 	}else if(sortIds==5){
 		listFeaturesOfSelectedWorkPackage(url);
 		
	}else if(sortIds==6){
 		listDecoupleOfSelectedWorkPackage(url);
 		
	}else if(sortIds==7){
 		url='administration.workPackage.feature.testbed?workPackageId='+workPackageId;
		testBedsForFeature(url);		
	 
	}else if(sortIds==8){
	 	url='administration.workPackage.featureResByTestBed?workPackageId='+workPackageId;	
		listFeaturesByTestBedsSelectedWorkPackage(url);
		
 	}	
}
//View records

var urlToGetWorkPackageTestcasePlanStatus ='';
function showCorrespondingTablePopUp(val) {
	$('.jtbPop1, .jtbPop2').hide();
	if(val == 1) {
		
		var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
		urlToGetWorkPackageTestcasePlanStatus = "workPackage.plan.tester.status?workPackageId="+workPacakgeId+"&plannedExecutionDate=-1";
	 	
		listWorkpackageTesterPlan();
		$('.jtbPop1').show();
		$('.jtbPop2').hide();
		//TODO
	}else {
		var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;

		urlToGetWorkPackageTestcaseTLPlanStatus = "workPackage.plan.testlead.status?workPackageId="+workPacakgeId+"&plannedExecutionDate=-1";
		listWorkpackageTestLeadPlan();
		$('.jtbPop2').show();
		$('.jtbPop1').hide();
		//TODO
	}
}

function listWorkpackageTesterPlan(){

	$("#psTestSuite, #psFeature").empty();
	try{
		if ($('#psTester').length>0) {
			 $('#psTester').jtable('destroy'); 
		}
	} catch(e) {}
	$('#psTester').jtable({
		title: 'TestCase Distribution - Tester',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetWorkPackageTestcasePlanStatus,
        },  
        fields : {
        	loginId: {
        		title: 'Tester ',
	                 list: true,
	                 create: false
	             },
	             plannedExecutionDate:{
	            	 title: 'Planned Execution Date ',
	                 list: true,
	                 create: false
	            	 
	             },
	             resourceAllocationCount:{
	                 title: 'Allocation Count',
	            	 list:true,
	                 width:"20%"
	             },
	           },  
		});
	$('#psTester').jtable('load');
	$('#popUpPlanningStatus').modal();   	
}

$("#datepicker_popup").on("changeDate",function() {
	var workPacakgeId = document.getElementById("treeHdnCurrentWorkPackageId").value;
	urlToGetWorkPackageTestcasePlanStatus = "workPackage.plan.tester.status?workPackageId="+workPacakgeId+"&plannedExecutionDate="+datepicker_popup.value;
 	listWorkpackageTesterPlan();
 	urlToGetWorkPackageTestcaseTLPlanStatus = "workPackage.plan.testlead.status?workPackageId="+workPacakgeId+"&plannedExecutionDate="+datepicker_popup.value;
 	listWorkpackageTestLeadPlan();
});

function listWorkpackageTestLeadPlan(){

	$("#psTestSuite, #psFeature").empty();
	try{
		if ($('#psTestLead').length>0) {
			 $('#psTestLead').jtable('destroy'); 
		}
	} catch(e) {}
	$('#psTestLead').jtable({
		title: 'TestCase Distribution - TestLead',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetWorkPackageTestcaseTLPlanStatus,
        },  
        fields : {
        	loginId: {
        		title: 'TestLead ',
	                 list: true,
	                 create: false
	             },
	             plannedExecutionDate:{
	            	 title: 'Planned Execution Date ',
	                 list: true,
	                 create: false
	            	 
	             },
	             resourceAllocationCount:{
	                 title: 'Allocation Count',
	            	 list:true,
	                 width:"20%"
	             },
	           },  
		});
	$('#psTestLead').jtable('load');
	$('#popUpPlanningStatus').modal();   	
}

function abortjob(jobId){
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : 'workpackage.job.automated.abort?testRunJobId='+jobId,
        dataType : 'json',
        error: function() {
				callAlert("Connection Failed.Please check the connection parameter");
				return false;
			},
        success : function(data) {
       
               if(data.Result=="Error"){
                     callAlert(data.Message);
                     return false;
		         }else{
		        	 
		        	 if(data.Result=="OK"){
		        		 callAlert(data.Message);
		        	 }
	         	}              
        }
 });
}
function abortWorkPackage(workPackageId){
	 $.ajax({
       type: "POST",
 		contentType: "application/json; charset=utf-8",
       url : 'workpackage.automated.abort?workPackageId='+workPackageId,
       dataType : 'json',
       error: function() {
				callAlert("Connection Failed.Please check the connection parameter");
			},
       success : function(data) {
      
              if(data.Result=="Error"){
                    callAlert(data.Message);
              return false;
		         }else{
		        	 
		        	 if(data.Result=="OK"){
		        		 callAlert(data.Message);
		        	 }
	         	}              
       }
});
}

function listWorkPackageAuditHistory(workPackageId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=WorkPackage&sourceEntityId='+workPackageId;
	var jsonObj={"Title":"WorkPackage Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,	    
			"componentUsageTitle":"workPackageAudit",
	};
	SingleJtableContainer.init(jsonObj);
}

function workPackageAuditFileConent(jsonObj){
		$('#JtableSingleContainer').jtable({
	         title: 'WorkPackage Audit History ',
	        // selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: jsonObj.jtPageSize, //Set page size (default: 10)
	         editinline:{enable:false},	        
	         actions: {
	           listAction:jsonObj.url,
	         },
	         fields: {        	
	        	eventId: { 
	            	title: 'EventId',
					width: "5%", 					
	            	list:false,
	            	edit:false
	            },
//	            eventSourceComponent: { 
//	            	title: 'Source Component' ,
//	            	list:true,
//	            	edit:false
//	            },
	            eventName: { 
	            	title: 'Event',
	            	list:true,
	            	edit:false
	        	},
	            eventDescription: { 
	            	title: 'Description',
					width: "10%",
	            	list:true,
	            	edit:false
	        	},
	        	sourceEntityType: { 
	            	title: 'EntityType',
	                list:false,
	            	edit:false
	            },
	        	sourceEntityName: { 
	            	title: 'Remarks',
	                list:true,
	            	edit:false
	            },
	            fieldName: { 
	            	title: 'FieldModified',
	                list:false,
	            	edit:false
	            },
	            fieldValue: { 
	            	title: 'Old Value',
	                list:false,
	            	edit:false
	            },
	            newFieldValue: { 
	            	title: 'New Value',
	                list:false,
	            	edit:false
	            },
	            userName: { 
	            	title: 'User',
	                list:true,
	            	edit:false
	            },				
	            endTime: { 
	            	title: 'Time',
	                list:true,
	            	edit:false
	            },		    		
	       },			 
	        formSubmitting: function (event, data) {    
			data.form.validationEngine();
	      	return data.form.validationEngine('validate');
	       }, 
	        //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	           data.form.validationEngine('hide');
	           data.form.validationEngine('detach');
	       },
	    });		 
	   $('#JtableSingleContainer').jtable('load');	
}

/*function loadTestCasesofJob(trjId, wpId){
	 	var headerName = 'TestCase Run';
	 		var jsonObj={"Title":headerName,			          
	    			"urlToGetTree": 'testcasesexecution.of.testrunjob.Id?testRunJobId='+trjId,	 
					"workPackageId":wpId
	    			};	 
	 		TestCaseOfJobsPagination.init(jsonObj);	    
}*/

function exportTestResultToGD() {
	
	var urlMapping = 'export.to.generate.auth.key';
	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : urlMapping,
         dataType : 'json',
         success : function(data) {
              if(data.Result=="OK"){
                //callAlert(data.Message);
                $("#export_test_results_url").text("Click here for Authentication Key");
                $("#export_test_results_url").attr("href", data.Message);
                $("#export_test_results_key").val("");
                $("#div_PopupExportTestResults").modal();
             }
         }
  }); 
	
}


function setColorIndicator(reservedCount,demandCount){
	
	if(reservedCount > demandCount){
		return 'red';
	}else if(reservedCount < demandCount ){
		return '#ff6600';
	}else if(reservedCount == 0 &&  demandCount == 0){
		return 'blue';
	}else{
		return 'green';
	}
}

function exportTestResultOption(){
	var key= $("#export_test_results_key").val();
	if($.trim($("#export_test_results_key").val()) == "") {
		callAlert("Please Enter Authentication Key");
		return false;
	}
	var url = 'export.to.google.drive.test.results?workPackageId='+workPackageId+'&code='+key;
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
			$("#loadingIcon").show();
			$("body").addClass("loaderIcon");
		$ 
			.post(url,
				function(data) {
					if(data.Result=='OK'){
						$("#div_PopupExportTestResults").modal("hide");
						callAlert(data.Message);
					}else{
						callAlert(data.Message);
						$("#loadingIcon").hide();
						$("body").removeClass("loaderIcon");
						return false;
					}
					thediv.style.display = "";
					$("#loadingIcon").hide();
					$("body").removeClass("loaderIcon");
				});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
	return false;
} 



var resourceDemandAndReservationData = {};
function showResourceDemandAndReservation(containerId, workpackageId, scrollYValue){
	var wNo = document.getElementById("currentWorkPackageResourceDemandWeekNo").value;
	
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url : 'workpackage.demand.projection.list.for.all.weeks?workPackageId='+workpackageId+"&weekNumber="+wNo+"&weekYear="+operationYear,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				resourceDemandAndReservationData = data.Records;
				getResourceDemandAndReservationHeaderAndFooterColumns(containerId);
				getResourceDemandAndReservation(containerId, workpackageId, scrollYValue, getResourceDemandAndReservationColumnMappings(), resourceDemandAndReservationData);
			}
			closeLoaderIcon();
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	 });
	
}

var resourceDemandAndReservation_oTable = '';
var resourceDemandAndReservationColumsAvailableIndex = [];
function getResourceDemandAndReservation(containerId, workpackageId, scrollYValue, resourceDemandAndReservationColumns, resourceDemandAndReservationData){
	try{
		if ($('#resourceDemandAndReservation_dataTable').length > 0) {
			$('#resourceDemandAndReservation_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	resourceDemandAndReservation_oTable = $('#resourceDemandAndReservation_dataTable').dataTable( {
		 "dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position    	  
    	  var footerItems = $('#resourceDemandAndReservation_dataTable_wrapper tfoot tr#resourceDemandAndReservation_dataTable_filterRow th');    	  
    	  footerItems.each( function () {   
  	    	    var i=$(this).index();
  	    	    var flag=false;
  	    	    var singleItem = $(footerItems).eq(i).find('div'); 
  	    	    for(var j=0; j < searchcolumnVisibleIndex.length; j++){
  	    	    	if(i == searchcolumnVisibleIndex[j]){
  	    	    		flag=true;
  	    	    		$(singleItem).css('height','0px');
   	    	    		$(singleItem).css('color','#4E5C69');
   	    	    		$(singleItem).css('line-height','12px');
  	    	    		break;
  	    	    	}else{
  	    	    		$(singleItem).css('height','0px');
   	    	    		$(singleItem).css('color','#4E5C69');
   	    	    		$(singleItem).css('line-height','12px');
  	    	    	}
  	    	    }
  	    	    
  	    	    if(searchcolumnVisibleIndex.length==0){
  	    	    	$(singleItem).css('height','0px');
    	    		$(singleItem).css('color','#4E5C69');
    	    		$(singleItem).css('line-height','12px');
  	    	    }
  	    	    
  	    	    if(!flag){
  	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
  	    	    }
    	   });   	
	  
		   reInitializeDataTableResourceDemandAndReservation();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Resource Demand and Reservation',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Resource Demand and Reservation',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Resource Demand and Reservation',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL',
	                        footer: true
	                    },	                   
	                ],	                
	            },
	            'colvis',
	            {					
					text: '<i class="fa fa-upload showHandCursor" title="Upload Demand"></i>',
					action: function ( e, dt, node, config ) {					
						triggerDemandUpload();
					}
				},
         ], 
         columnDefs: [
          	  //{ "orderable": false, "targets": 0 },
              { targets: resourceDemandAndReservationColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : resourceDemandAndReservationData,                 
	    aoColumns : resourceDemandAndReservationColumns,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },       
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		new $.fn.dataTable.FixedColumns( resourceDemandAndReservation_oTable, {
			leftColumns:5,
		});		
		
		$('#resourceDemandAndReservation_dataTable_wrapper .dataTables_scrollFoot tfoot tr#resourceDemandAndReservation_dataTable_filterRow th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#resourceDemandAndReservation_dataTable_wrapper .dataTables_scrollFoot tfoot tr#resourceDemandAndReservation_dataTable_filterRow th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			resourceDemandAndReservation_oTable.fnFilter( this.value, visIndex );
		});
					
		/* Use the elements to store their own index */
		$('#resourceDemandAndReservation_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#resourceDemandAndReservation_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			resourceDemandAndReservation_oTable.fnFilter( this.value, visIndex );
		});
		
		$("#resourceDemandAndReservation_dataTable_length").css('margin-top','8px');
		$("#resourceDemandAndReservation_dataTable_length").css('padding-left','35px');
	    
		var importDemandUpload = '<input id="uploadDemand" type="file" name="uploadDemand" style="display:none;" onclick="{this.value = null;};" onchange="importDemand()">'
		$('#resourceDemandAndReservation_dataTable_filter').append(importDemandUpload);
		
		var currentYear = new Date().getFullYear();
		var yearFilter = '<span><label style="margin: 5px;">Year :</label><select id="resourceDemandAndReservationYearFilter" style="margin: 5px;">';
		for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
			if(operationYear == i){
				yearFilter += '<option value="'+i+'" selected>'+i+'</option>';
			}else{
				yearFilter += '<option value="'+i+'">'+i+'</option>';
			}
			
		}
		yearFilter += '</select><span>';
		$('#resourceDemandAndReservation_dataTable_filter').append(yearFilter);
		
		$(document).on('change','#resourceDemandAndReservationYearFilter', function() {
			operationYear =  $("#resourceDemandAndReservationYearFilter").val();
			getWeeksName();
			showResourceDemandAndReservation(containerId, workpackageId, scrollYValue);
		});
		
	}); 
}

function getResourceDemandAndReservationColumnMappings(){
	var resourceDemandAndReservationColumns = [];
	resourceDemandAndReservationColumns.push({ mData: "shiftName", sWidth: '5%'});
	resourceDemandAndReservationColumns.push({ mData: "workPackageName", sWidth: '5%'});
	resourceDemandAndReservationColumns.push({ mData: "userRoleName", sWidth: '5%'});
	resourceDemandAndReservationColumns.push({ mData: "userTypeName", sWidth: '5%'});
	resourceDemandAndReservationColumns.push({ mData: "skillName", sWidth: '5%'});
	
		
	resourceDemandAndReservationColumsAvailableIndex = [0, 1, 2, 3,4];
	var resourceDemandAndReservationColumsIndex = 4;
	
	$.each(weeksName, function(index, value){
		resourceDemandAndReservationColumsIndex++;
		resourceDemandAndReservationColumsAvailableIndex.push(resourceDemandAndReservationColumsIndex);
		columnMapping =	{ 
			mData: 'week'+(index+1),
			sWidth: '2%',
			"render" : function (resourceDemandAndReservationData, resourceDemandAndReservationType, resourceDemandAndReservationFull) {
				var currentDemandShiftFieldName = "currentDemandShiftName0"+resourceDemandAndReservationFull.shiftId;
            	var currentShiftFieldName = resourceDemandAndReservationFull.shiftName;
               	$("#hdnDiv").append("<input type='hidden' id=currentWorkPackageName></input> <input type='hidden' id=currentShiftFieldName></input><input type='hidden' id=currentUserType></input><input type='hidden' id=currentUserRole></input><input type='hidden' id=currentUserSkill></input>");
				document.getElementById("currentWorkPackageName").value = resourceDemandAndReservationFull.workPackageName;
				
				document.getElementById("currentDemandShiftName").value = resourceDemandAndReservationFull.shiftName;
				selectedShiftName = resourceDemandAndReservationFull.shiftName;
				
				demandRecursive = 0;
				$('#demandYears').val(operationYear);
				
               	$("#hdnDemandDiv").append("<input type='hidden' id="+currentDemandShiftFieldName+"></input>");
				document.getElementById(currentDemandShiftFieldName).value = resourceDemandAndReservationFull.shiftName;
				var colorValue =  setColorIndicator(resourceDemandAndReservationFull['reservedResourceCountWk'+(index + 1)], resourceDemandAndReservationFull['week'+(index + 1)]);
    			return ('<ul style="display: -webkit-inline-box;list-style: none;padding: 4px;margin: 0px;color: '+colorValue+';"><li><a style="color: '+colorValue+';" href="javascript:getResourceForBlockingGridViewForWeekly('+resourceDemandAndReservationFull.workPackageId+','+resourceDemandAndReservationFull.shiftId+','+resourceDemandAndReservationFull.skillId+','+resourceDemandAndReservationFull.userRoleId+','+(index + 1)+','+resourceDemandAndReservationFull.workYear+','+resourceDemandAndReservationFull['reservedResourceCountWk'+(index + 1)]+','+resourceDemandAndReservationFull['week'+(index + 1)]+','+resourceDemandAndReservationFull.groupDemandId+','+resourceDemandAndReservationFull.userTypeId+',\''+resourceDemandAndReservationFull.workPackageName+'\',\''+weeksName[index]+'\',\''+resourceDemandAndReservationFull.userTypeName+'\',\''+resourceDemandAndReservationFull.userRoleName+'\',\''+resourceDemandAndReservationFull.skillName+'\');" class="showHandCursor">'+resourceDemandAndReservationFull['reservedResourceCountWk'+(index + 1)]+'</a></li><li>/</li><li><a style="color: '+colorValue+';" href="javascript:raiseResourceDemandForWeekly('+resourceDemandAndReservationFull.workPackageId+','+resourceDemandAndReservationFull.shiftId+','+(index + 1)+','+resourceDemandAndReservationFull.skillId+','+resourceDemandAndReservationFull.userRoleId+','+resourceDemandAndReservationFull.userTypeId+');" class="showHandCursor">'+setValuePresicion(resourceDemandAndReservationFull['week'+(index + 1)])+'</a></li></ul>');
			},
        };
		resourceDemandAndReservationColumns.push(columnMapping);
	});
	
	return resourceDemandAndReservationColumns;
}

function setValuePresicion(value){
	if(value != "undefined" && value != 0  ){
		value = value.toFixed(2);
	}
	return value;
}


function getResourceDemandAndReservationHeaderAndFooterColumns(containerId){
	var resourceDemandAndReservationContainer = $('#'+containerId);
	resourceDemandAndReservationContainer.empty();
	
	
	var resourceDemandAndReservation_dtColumns = ['Shift&nbsp;Name', 'Workpackage&nbsp;Name', 'User&nbsp;Role', 'User&nbsp;Type','Skill&nbsp;Name'];
		resourceDemandAndReservation_dtColumns[1] = check+"Name";
	
	
	$.each(weeksName, function(index, value){
		resourceDemandAndReservation_dtColumns.push(value);
	});
	
	var resourceDemandAndReservationTable = '<table id="resourceDemandAndReservation_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var resourceDemandAndReservation_thead = '<thead><tr>';
	var resourceDemand_filter_row = '<tfoot>';	
	var resourceDemandAndReservation_filter_row = '<tr id="resourceDemandAndReservation_dataTable_filterRow">';
	
	$.each(resourceDemandAndReservation_dtColumns, function(index, value){
		resourceDemandAndReservation_thead += '<th class="sorting">'+value+'</th>';
		resourceDemandAndReservation_filter_row += '<th></th>';		
		resourceDemand_filter_row += '<th></th>';
	});
	resourceDemandAndReservationTable += resourceDemandAndReservation_thead + '</tr></thead>' + resourceDemand_filter_row + resourceDemandAndReservation_filter_row + '</tr></table>';//+ resourceDemandAndReservation_tfoot + '</tr></tfoot>';
	resourceDemandAndReservationContainer.append(resourceDemandAndReservationTable);
}

function reInitializeDataTableResourceDemandAndReservation(){
	setTimeout(function(){				
		resourceDemandAndReservation_oTable.DataTable().columns.adjust().draw();		
	},200);
}

function reRunTestPlan(testPlanId, automationMode, useIntelligentTestPlan){
	var url = 'testplan.readiness.check?testPlanId='+testPlanId;
	document.getElementById("hdnTestRunPlanId").value = testPlanId;
	document.getElementById("hdnTestRunPlanDeviceId").value = testPlanId;
	 $.ajax({
		 type: "POST",
		 url: url,
		 success: function(data) {  
			 if(data.Result == "ERROR"){
				 return false;
			 }else if(data.Result == "OK"){
				 if(data.Message.toUpperCase() != 'YES') {
					 alertWidthFlag = true;
					 callAlert(data.exportFileName);
					 return false;
			     }					 
				 if(automationMode == 'Attended'){
		        	if(useIntelligentTestPlan == 0){
		        		$('#recommendedHeader').hide();
		        		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','20px');
		        	}else{
		        		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','0px');
		        		$('#recommendedHeader').show();
		        	}
					$('#div_PopupRunConfigurationList').modal();
					testCasebyTestRunPlanId();
					testRunPlanTestBeds();
				 } else {
					 trpExecMode = automationMode;
					unattendedCallMode(testPlanId);
				 } 
			 }
		 }    
	 });
}