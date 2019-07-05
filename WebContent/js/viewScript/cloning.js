var cloningJsonObj="";
var Cloning = function() {
  
   var initialise = function(jsonObj){
	   cloningJsonObj = jsonObj;
	   displayCloning();
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function displayCloning(){
	$("#cloningContainerID h4").text("");
	$("#cloningContainerID h4").text(cloningJsonObj.title);
	$("#cloningContainerID .form-group").eq(0).find('label').text(cloningJsonObj.packageName);
	$("#cloningContainerID .portlet-title .caption").text(cloningJsonObj.selectionTerm);
	
	$('#cloning_name').empty();
	$('#cloning_desc').empty();
	$("#cloningStartDate").empty();
	$("#cloningEndDate").empty();
	
	$('#cloning_name').val(cloningJsonObj.sourceName);
	$("#cloningStartDate").val(cloningJsonObj.startDate);
	$("#cloningEndDate").val(cloningJsonObj.endDate);
	
	/*var date = new Date();*/
	//date.setDate(date.getDate()-30);  // display next month
	$("#cloningStartDate").datepicker('setDate',cloningJsonObj.startDate);	
	$("#cloningEndDate").datepicker('setDate',cloningJsonObj.endDate);

	$("#cloningContainerID").modal();
	
	loadTreeCheckbox();
	//setCloneDefaultnode();		
	
	
	/*var checkEle = $("#cloneTree").find('.jstree-ocl');
	if(checkEle.length>0){
		$.each(checkEle, function(index, element){						
			$(element).removeClass('jstree-icon');
		});
	}*/
}

var productMode='';
var cloningStartDate="";	
var cloningEndDate="";	

jQuery(document).ready(function() {	
	ComponentsPickers.init();	
	
});

var destinationId='';
function loadTreeCheckbox(){
	destinationId='';
	var jsonTreeData = jQuery.parseJSON(treeData); 	
	if(jsonTreeData != null){
		 $('#cloneTree')
		    .on("changed.jstree", function (e, data) {
		       // console.log(data.node.data); // newly selected
		       // console.log(data.node.id); // newly deselected
		    	if(data.node !=null || data.node !=undefined){
			        var buildID = data.node.data
			        var buildArr = buildID.split('~');
			        if(buildArr.length>0){
			        	if(buildArr[1] == "ProductBuild")
			        		destinationId = buildArr[0];
			        	else if(buildArr[1] == 'ActivityWorkPackage' || buildArr[1] == 'ActivityWorkpackage')
			        		destinationId = buildArr[0];
			        	else if(buildArr[1] == 'Activity')
			        		destinationId = buildArr[0];
			        	else
			        		destinationId='';
			        }
		        }
		      })
		 .jstree({
	         'plugins': ["unique","json_data","checkbox"],
	         'core' : {
	 	   	 	   'data' : jsonTreeData,
	 	   	 	   "multiple" : false
	 		}, 		
	     })
	     /*.bind('check_node.jstree', function(e, data) {
	         var currentNode = data.rslt.obj.attr("id");
	         $("#cloneTree").jstree("get_checked",null,true).each 
	             (function () { 
	                 if(currentNode != this.id){
	                     jQuery.jstree._reference($("#cloneTree")).uncheck_node('#'+this.id);
	                 }
	             }); 
	     });*/
	 }
	
	 $('#cloneTree').jstree(true).deselect_all();
	 $('#cloneTree').jstree(true).settings.core.data = jsonTreeData;
	 $('#cloneTree').jstree(true).refresh();
 }

/*function setCloneDefaultnode() {	
	$("#cloneTree").on("loaded.jstree",function(evt, data) {
		$.each($('#cloneTree li'), function(ind, ele){
			if($.jstree.reference('#cloneTree').is_parent($(ele).attr("id"))){
				defaultNodeId = $(ele).attr("id");
				console.log("defaultNodeId :"+defaultNodeId+" name -->"+$.jstree.reference("#cloneTree").get_node(defaultNodeId).text);
				
				if($.jstree.reference("#cloneTree").get_node(defaultNodeId).children && 
						$.jstree.reference("#cloneTree").get_node(defaultNodeId).children.length>0){
					$(ele).find('.jstree-ocl').trigger('click');
					setCloneDefaultSubNode($.jstree.reference("#cloneTree").get_node(defaultNodeId).children);
				}
			}
		});	
		setCloneDefaultnode();
	});
}

function setCloneDefaultSubNode(element){
	$.each($(element), function(ind, ele){		
		$(ele).find('.jstree-ocl').trigger('click');
		defaultNodeId = $.jstree.reference("#cloneTree").get_node($(ele));
		
		if($(ele).hasClass('jstree-checkbox')){
			$(ele).removeClass('jstree-checkbox');
		}
		if(defaultNodeId.children && defaultNodeId.length>0){
			subDefaultNodeId = $.jstree.reference("#cloneTree").get_node(defaultNodeId);
			setCloneDefaultSubSubNode(subDefaultNodeId.children);
			
			console.log("defaultNodeId 22 :"+subDefaultNodeId);
			console.log("defaultNodeId 33 :"+subDefaultNodeId.text);
		}	
		
	});
}*/




/*$(document).on("click",".daterangepicker .applyBtn", function(e) {
	
});*/

var productBuildId='';

function closeCloningContainer(){
	
}

function loadWorkpackages(){
    var productId=document.getElementById("treeHdnCurrentProductId").value; 
	$.ajax({
		    type: "POST",
		    url: "administration.workpackage.list.by.product?productId="+productId,
		    success: function(data) {
		    	var ary = (data.Options);
		    	var $select = $('#wpkg_options');  
		    	$select.empty();
		    	$.each(ary,function(index, element) 
		    	{
		    	    $select.append('<option value=' + element.Number + '>' + element.DisplayText + '</option>');
		    	});
		    	
		    },    
	});
}

function saveWorkpackageDetail(){
	if(cloningJsonObj.componentUsageTitle == 'activityWorkpackageClone'){
		cloningActivityWorkpackage();
		
	}else if(cloningJsonObj.componentUsageTitle == 'activityClone'){
		cloneActivity();
	}else if(cloningJsonObj.componentUsageTitle == 'activityTaskClone'){
		cloneActivityTask();
	}if(cloningJsonObj.componentUsageTitle == 'activityWorkpackageMove'){
		//$('#plannedDateDiv').hide();
		movingActivityWorkpackage();
		
	}else if(cloningJsonObj.componentUsageTitle == 'activityMove'){
		//$('#plannedDateDiv').hide();
		movingActivity();
	}else if(cloningJsonObj.componentUsageTitle == 'activityTaskMove'){
		//$('#plannedDateDiv').hide();
		movingActivityTask();
	} else{
		console.log("clone others");
	}	
}

function dateFormat(date){
	var datearr = date.split('-');
	return datearr[1]+"/"+datearr[0]+"/"+datearr[2];
}

function cloningActivityWorkpackage(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityWorkpackageName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Build ID --> "+cloningJsonObj.sourceParentID);
	console.log("Build Name --> "+cloningJsonObj.sourceParentName);
	console.log("Workpackage ID --> "+cloningJsonObj.sourceID);
	console.log("Workpackage name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified workpackage name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  buildID :"+destinationId);
	
	if(destinationId==""){
		if(cloningJsonObj.componentUsageTitle == 'activityWorkpackageClone'){
			callAlert("Please Select a Build");
		}
	}
	
	//alert("url>>>"+'process.activityworkpackage.clone?srcProductBuildId='+cloningJsonObj.sourceParentID+'&destProductBuildId='+destinationId+'&workpackageId='+cloningJsonObj.sourceID+'&newWorkpackageName='+newActivityWorkpackageName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activityworkpackage.clone?srcProductBuildId='+cloningJsonObj.sourceParentID+'&destProductBuildId='+destinationId+'&workpackageId='+cloningJsonObj.sourceID+'&newWorkpackageName='+newActivityWorkpackageName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
       	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity workpackage cloned successfully");
            	 $("#cloningContainerID").modal('hide');
            	 $('.portlet > .portlet-title > .tools > .reload').trigger('click');
             }else{
            	callAlert(data.Message);
             }
        }
	});
}

function cloneActivity(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Src Activity Wkpg ID --> "+cloningJsonObj.sourceParentID);
	console.log("Src Activity Wkpg Name --> "+cloningJsonObj.sourceParentName);
	console.log("Activity ID --> "+cloningJsonObj.sourceID);
	console.log("Activity name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified activity name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  activity workpackage id :"+destinationId);
	
	if(destinationId==""){
		callAlert("Please Select Activity Workpackage");
		
	}
	
	//alert("url>>>"+'process.activity.clone?srcActWorkpackageId='+cloningJsonObj.sourceParentID+'&destActWorkpackageId='+destinationId+'&cloneActivityId='+cloningJsonObj.sourceID+'&newActivityName='+newActivityName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activity.clone?srcActWorkpackageId='+cloningJsonObj.sourceParentID+'&destActWorkpackageId='+destinationId+'&cloneActivityId='+cloningJsonObj.sourceID+'&newActivityName='+newActivityName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
        	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity cloned successfully");
            	 
            	var activityWPId=destinationId;
         		var enableAddOrNot="yes";	
         		listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, "jTableContainerWPActivities_dataTable");            	 
            	 $("#cloningContainerID").modal('hide');
            	 
             }else{
            	 callAlert(data.Message);
             }
        }
	});
}
function cloneActivityTask(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityTaskName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Src Activity ID --> "+cloningJsonObj.sourceParentID);
	console.log("Src Activity Name --> "+cloningJsonObj.sourceParentName);
	console.log("Activity Task ID --> "+cloningJsonObj.sourceID);
	console.log("Activity Task name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified activity task name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  activity id :"+destinationId);
	
	if(destinationId==""){
		callAlert("Please Select Activity");
		
	}
	
	//alert("url>>>"+'process.activitytask.clone?srcActivityId='+cloningJsonObj.sourceParentID+'&destActivityId='+destinationId+'&cloneActivityTaskId='+cloningJsonObj.sourceID+'&newActivityTaskName='+newActivityTaskName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activitytask.clone?srcActivityId='+cloningJsonObj.sourceParentID+'&destActivityId='+destinationId+'&cloneActivityTaskId='+cloningJsonObj.sourceID+'&newActivityTaskName='+newActivityTaskName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
        	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity Task cloned successfully");
            	 $("#cloningContainerID").modal('hide');
             }else{
            	callAlert(data.Message);
             }
        }
	});
}
/* Pop Up close function */
function popupClose() {
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};


/* function displayRecords(result)
{
	//var result = data.Records;
	$("#activityCloning_list").empty();
	
	if(result.length==0){
		 $("#activityCloning_list").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Data</b></span>");
	}else{
	    var resultValue="";
		$.each(result, function(i,item){				
			resultValue = item.id;
			$("#activityCloning_list").append(resultValue);				
	 	});
	}
 } */


function movingActivityWorkpackage(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityWorkpackageName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Build ID --> "+cloningJsonObj.sourceParentID);
	console.log("Build Name --> "+cloningJsonObj.sourceParentName);
	console.log("Workpackage ID --> "+cloningJsonObj.sourceID);
	console.log("Workpackage name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified workpackage name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  buildID :"+destinationId);
	
	if(destinationId==""){
		if(cloningJsonObj.componentUsageTitle == 'activityWorkpackageClone'){
			callAlert("Please Select a Build");
		}
	}
	
	//alert("url>>>"+'process.activityworkpackage.clone?srcProductBuildId='+cloningJsonObj.sourceParentID+'&destProductBuildId='+destinationId+'&workpackageId='+cloningJsonObj.sourceID+'&newWorkpackageName='+newActivityWorkpackageName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activityworkpackage.move?srcProductBuildId='+cloningJsonObj.sourceParentID+'&destProductBuildId='+destinationId+'&workpackageId='+cloningJsonObj.sourceID+'&newWorkpackageName='+newActivityWorkpackageName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
       	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity workpackage Moved successfully");
            	 $("#cloningContainerID").modal('hide');
            	 $('.portlet > .portlet-title > .tools > .reload').trigger('click');
             }else{
            	callAlert(data.Message);
             }
        }
	});
}

function movingActivity(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Src Activity Wkpg ID --> "+cloningJsonObj.sourceParentID);
	console.log("Src Activity Wkpg Name --> "+cloningJsonObj.sourceParentName);
	console.log("Activity ID --> "+cloningJsonObj.sourceID);
	console.log("Activity name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified activity name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  activity workpackage id :"+destinationId);
	
	if(destinationId==""){
		callAlert("Please Select Activity Workpackage");
		
	}
	
	//alert("url>>>"+'process.activity.clone?srcActWorkpackageId='+cloningJsonObj.sourceParentID+'&destActWorkpackageId='+destinationId+'&cloneActivityId='+cloningJsonObj.sourceID+'&newActivityName='+newActivityName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activity.move?srcActWorkpackageId='+cloningJsonObj.sourceParentID+'&destActWorkpackageId='+destinationId+'&movingActivityId='+cloningJsonObj.sourceID+'&newActivityName='+newActivityName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
        	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity Moved successfully");
            	 $("#cloningContainerID").modal('hide');
             }else{
            	 callAlert(data.Message);
             }
        }
	});
}
function movingActivityTask(){
	$("#cloningLoaderIcon").show();
	var plannedStartDate=document.getElementById("cloningStartDate").value;
	var plannedEndDate=document.getElementById("cloningEndDate").value;
	var newActivityTaskName=$('#cloning_name').val();
	var description=$('#cloning_desc').val();
	console.log("Src Activity ID --> "+cloningJsonObj.sourceParentID);
	console.log("Src Activity Name --> "+cloningJsonObj.sourceParentName);
	console.log("Activity Task ID --> "+cloningJsonObj.sourceID);
	console.log("Activity Task name --> "+cloningJsonObj.sourceName);
	console.log("plannedStartDate --> "+plannedStartDate);
	console.log("plannedEndDate --> "+plannedEndDate);
	console.log("modified activity task name --> "+$('#cloning_name').val());
	console.log("desc --> "+$('#cloning_desc').val());
	console.log("destination  activity id :"+destinationId);
	
	if(destinationId==""){
		callAlert("Please Select Activity");
		
	}
	
	//alert("url>>>"+'process.activitytask.clone?srcActivityId='+cloningJsonObj.sourceParentID+'&destActivityId='+destinationId+'&cloneActivityTaskId='+cloningJsonObj.sourceID+'&newActivityTaskName='+newActivityTaskName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description);
	//var urlMapping='administration.test.management.system.list?buildID='+buildID+"&workpackageID="+workpackageID+'&destinationID="+45;
	$.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'process.activitytask.move?srcActivityId='+cloningJsonObj.sourceParentID+'&destActivityId='+destinationId+'&cloneActivityTaskId='+cloningJsonObj.sourceID+'&newActivityTaskName='+newActivityTaskName+'&planStartDate='+plannedStartDate+'&planEndDate='+plannedEndDate+'&description='+description,
        dataType : 'json',
        success : function(data) {
        	$("#cloningLoaderIcon").hide();
             if(data.Result=="OK"){           	        	
            	 callAlert("Activity Task Moved successfully");
            	 $("#cloningContainerID").modal('hide');
             }else{
            	callAlert(data.Message);
             }
        }
	});
}

