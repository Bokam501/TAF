var wfSummaryProductId;
var wfSummaryProductName;
var wfSummaryEntityTypeId;
var wfSummaryEntityTypeName;
var wfSummaryentityId;
var wfSummaryentityInstanceId;
var wfSummaryProductVersionId;
var wfSummaryProductBuildId;
var wfSummaryActivityWorkPackageId;
var wfSummaryEngagementId;
var typeFiledDisplay=true;
var entityTypeTitles ="";

/*
function showWorkflowEntityInstanceStatusSummary(engagementId,engagementName,productId, productName, entityTypeId, entityTypeName, entityId, entityInstanceId){
	wfSummaryProductId = productId;
	wfSummaryProductName = productName;
	wfSummaryEntityTypeId = entityTypeId;
	wfSummaryEntityTypeName = entityTypeName;
	wfSummaryEntityId = entityId;
	wfSummaryEntityInstanceId = entityInstanceId;
	wfSummaryEngagementId = engagementId;
	if(entityTypeId == 34) {
		typeFiledDisplay=false;
	}
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").empty();

	var jsonObj={};	
	if(engagementId != 0 && productId==0) {
		jsonObj={
				"Title":"Workflow Summary: "+engagementName+" Engagements",
		};
	} else if(productId!=0 && entityInstanceId ==0) {
		jsonObj={
				"Title":"Workflow Summary: "+productName+" Products",
		};
	} else if(productId != 0 && entityInstanceId!=0) {
		jsonObj={
				"Title":"Workflow Summary : "+wfSummaryProductName+" Workpackage "+wfSummaryEntityTypeName,
		};
	}	
	DynamicJtableContainer.init(jsonObj);
	clearDynamicJTableDatas();
	
	listWorkflowInidcatorySummary();
	
	var instanceTemplateGridPosition=0;
	var instanceStatusGridPosition=1;
	var instanceActorGridPosition=2;
	var instanceStatusCategoryGridPosition=3;
	if(wfSummaryEntityTypeId != 34) {
		listWorkflowEntityInstanceTemplateSummary('instanceTemplateSummary',instanceTemplateGridPosition);
	} else {
		instanceStatusGridPosition=0;
		instanceActorGridPosition=1;
		instanceStatusCategoryGridPosition=2;
	}
	listWorkflowEntityInstanceStatusSummary('instanceStatusSummary',instanceStatusGridPosition);
	listWorkflowEntityInstanceStatusUserSummary('instanceStatusUserSummary',instanceActorGridPosition);
	listWorkflowEntityInstanceStatusCategorySummary('instanceWorkflowStatusCategorySummary',instanceStatusCategoryGridPosition);
	//listWorkflowEntityInstanceStatusRoleSummary('instanceStatusRoleSummary');	
	$("#div_DynamicJTableSummary .modal-header .row").css('display','none');    
}
*/

/*
function listWorkflowEntityInstanceTemplateSummary(containerId,gridPosition){
	var entityTypeTitle="";
	if(wfSummaryEntityTypeId == 30) {
		entityTypeTitle="Task Life Cycle";
	} else if(wfSummaryEntityTypeId == 33) {
		entityTypeTitle="Activity Life Cycle";
	} else if(wfSummaryEntityTypeId == 34) {
		entityTypeTitle="Workpackage Life Cycle";
	} else if(wfSummaryEntityTypeId == 3) {
		entityTypeTitle="Testcase Life Cycle";
	} 
	entityTypeTitles = entityTypeTitle;
	var urlToListWorkflowEntityInstanceTemplateSummary = 'workflow.entity.instance.type.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(gridPosition).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceTemplateSummary,
		}, 
		fields: {
			workflowId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeId:{
				title : 'typeId',
				list: false,
				edit : false,
				create: false,
			},
			typeName: { 
				title: 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			typeCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
					var link = '<a href="javascript:void(0)" onclick="viewWorkflowTypeBasedDetail('+data.record.typeId+')">'+data.record.typeCount+'</a>';
					return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceStatusSummary(containerId,gridPosition){
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(gridPosition).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Status Summary',
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeName: { 
				title: 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
				size:'7%',
			},
			workflowStatusCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowStatusBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+')">'+data.record.workflowStatusCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceStatusUserSummary(containerId,gridPosition){
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.actor.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityInstanceId='+ wfSummaryEntityInstanceId+'&actorType=User';
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(gridPosition).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'User Queue',
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},
			userId:{
				list: false,
				edit : false,
				create: false,
			},
			typeName: { 
				title: 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},			
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
			},
			actorName: { 
				title : 'User Name',
				create:false,
				list : true,
				edit : false,
			},
			instanceCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowUserBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+','+data.record.userId+')">'+data.record.instanceCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceStatusCategorySummary(containerId,gridPosition){
	var urlToListWorkflowEntityInstanceStatusCategorySummary = 'workflow.entity.instance.status.category.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityParentInstanceId='+wfSummaryEntityInstanceId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(gridPosition).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Status Category Summary',
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusCategorySummary,
		}, 
		fields: {
			workflowStatusCategoryId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},
			typeName: { 
				title: 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},			
			workflowStatusCategoryName: { 
				title: 'Category',
				list: true,
				edit : false,
				create: false,
				size:'7%',
			},
			workflowStatusCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowStatusCategoryBasedDetail('+data.record.workflowStatusCategoryId+','+data.record.entityId+')">'+data.record.workflowStatusCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*function listWorkflowEntityInstanceStatusRoleSummary(containerId){
	
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.actor.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityInstanceId='+ wfSummaryEntityInstanceId+'&actorType=Role';
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(3).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Role Queue',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
         saveUserPreferences: false, 
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},
			roleId:{
				list: false,
				edit : false,
				create: false,
			},
			workflowName: { 
				title: 'Workflow Template',
				list: true,
				edit : false,
				create: false,
			},
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
			},
			actorName: { 
				title : 'Role Name',
				create:false,
				list : true,
				edit : false,
			},
			instanceCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowRoleBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+','+data.record.roleId+')">'+data.record.instanceCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}*/

function listWorkflowInidcatorySummary(){
	var indicatorSummaryURL = 'workflow.status.indicator.product.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">SLA </span><span style="padding:10px"><i class="" style="color: blue;" title="Total Workflow Status"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'blue\')">[0]</a></span>:<span style="padding:10px"><i class="fa fa-circle" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orange;" title="Need immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: green;" title="Availble for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">SLA </span><span style="padding:10px"><i class="" style="color: blue;" title="Total Workflow Status"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'blue\')">[ '+indicatorDetails["totalWorkflowStatusCount"]+' ]</a></span>:<span style="padding:10px"><i class="fa fa-circle" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orangered\')">'+indicatorDetails["orangered"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orange;" title="Need immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: green;" title="Availble for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'green\')">'+indicatorDetails["green"]+'</a></span>';
			}
			$('#headerSubTitle').html(indicatorSummary);
		}else{
			callAlert(data.Message);
		}
	});
}

/*
function showWorkflowEntityInstanceRAGSummary(engagementId,engagementName,productId,productVersionId,productBuildId,activityWorkPackageId, productName, entityTypeId, entityTypeName, entityId, entityInstanceId){
	wfSummaryProductId = productId;
	wfSummaryProductName = productName;
	wfSummaryEntityTypeId = entityTypeId;
	wfSummaryEntityTypeName = entityTypeName;
	wfSummaryEntityId = entityId;
	wfSummaryEntityInstanceId = entityInstanceId;
	wfSummaryProductVersionId=productVersionId;
	wfSummaryProductBuildId=productBuildId;
	wfSummaryActivityWorkPackageId=activityWorkPackageId;
	wfSummaryEngagementId=engagementId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").empty();
	//listWorkflowInidcatoryRAGSummary();
	
	var jsonObj={};		
	listWorkflowInidcatoryRAGSummary();
	lisRAGViewEngagementSummary('instanceEngagementRAGSummary');
	lisRAGViewActivityCategorySummary('instanceRAGActivityCategorySummary');
	
	if(engagementId != 0 && productId==0) {
		jsonObj={
				"Title":"RAG Summary: "+engagementName+" Engagements",
		};	
	} else if((engagementId != 0 && productId != 0) && activityWorkPackageId==0) {
		jsonObj={
				"Title":"RAG Summary : "+wfSummaryProductName+" Products",
		};		
	} else if(productId != 0 && activityWorkPackageId!=0) {
		jsonObj={
				"Title":"RAG Summary : "+wfSummaryProductName+" Workpackage "+wfSummaryEntityTypeName,
		};	
	}
	
	DynamicJtableContainer.init(jsonObj);
	clearDynamicJTableDatas();
	if(wfSummaryEngagementId != 0 || productId != 0) {
		listWorkflowEntityInstanceResourceRAGSummary('instanceResourceRAGSummary');
	}	
	$("#div_DynamicJTableSummary .modal-header .row").css('display','none');
	
}
*/

/*function listWorkflowInidcatoryRAGSummary(){
	var indicatorSummaryURL = 'workflow.RAG.indicator.product.SLA.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&wfSummaryEntityInstanceId='+wfSummaryProductBuildId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">SLA :</span><span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Available for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">SLA :</span><span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Available for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'green\')">'+indicatorDetails["green"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: darkgray;" title="SLA duration End"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'darkgray\')">'+indicatorDetails["completedCount"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: gray;" title="SLA duration aborted"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'gray\')">'+indicatorDetails["abortCount"]+'</a></span>';
			}
			$('#headerSubTitle').html(indicatorSummary);
		}else{
			callAlert(data.Message);
		}
	});
}*/

/*
function lisRAGViewEngagementSummary(containerId){
	var entityTypeTitle="";
	
	if(wfSummaryEngagementId != 0 && wfSummaryProductId==0) {
		entityTypeTitle="Engagement Summary";
	} else if((wfSummaryEngagementId != 0 && wfSummaryProductId != 0) && wfSummaryActivityWorkPackageId==0) {
		entityTypeTitle="Product Summary";
	} else if(wfSummaryProductId != 0 && wfSummaryActivityWorkPackageId!=0) {
		entityTypeTitle="Workpackage Summary";
	}
	
	var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.engagement.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToLisRAGViewProductSummary,
		}, 
		fields: {
			engagementId:{
				list: false,
				edit : false,
				create: false,
			},
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+',\'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*function listWorkflowInidcatoryRAGSummary(){
	var entityParentInstanceId=0;
	if (wfSummaryEntityTypeId == 34) {
		entityParentInstanceId=wfSummaryProductBuildId;
	} else if(wfSummaryEntityTypeId == 33){
		entityParentInstanceId=wfSummaryActivityWorkPackageId;
	} 
	var indicatorSummaryURL = 'workflow.RAG.indicator.product.SLA.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+entityParentInstanceId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">SLA</span><span style="padding:10px"><i class="" style="color: blue;" title="Total RAG Summary"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'blue\')">[0]</a></span>:<span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orangered;" title="Need immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">SLA</span><span style="padding:10px"><i class="" style="color: Blue;" title="Total RAG Summary"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'blue\')">[ '+indicatorDetails["totalRAGCount"]+' ]</a></span>:<span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'green\')">'+indicatorDetails["green"]+'</a></span>|<span style="padding:10px"><i class="fa fa-check" style="color: darkgreen;" title="Completed"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'darkgreen\')">'+indicatorDetails["completedCount"]+'</a></span>|<span style="padding:10px"><i class="fa fa-times" style="color: darkRed;" title="Aborted"></i> <a href="javascript:void(0)" onclick="viewWorkflowIndicatorDetailRAGView(\'darkRed\')">'+indicatorDetails["abortCount"]+'</a></span>';
			}
			$('#headerSubTitle').html(indicatorSummary);
		}else{
			callAlert(data.Message);
		}
	});
}*/

/*
function listWorkflowEntityInstanceTemplateRAGABuildSummary(containerId){
	var entityTypeTitle="Workpackage";
	entityTypeTitles = entityTypeTitle;
	var urlToListWorkflowEntityInstanceBuildTemplateSummary = 'workflow.RAG.indicator.activity.grouping.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryProductBuildId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceBuildTemplateSummary,
		}, 
		fields: {
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.productId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceTemplateRAGWorkpackageSummary(containerId){
	var entityTypeTitle="Activities";
	entityTypeTitles = entityTypeTitle;
	var urlToListWorkflowEntityInstanceWorkpackageTemplateSummary = 'workflow.RAG.indicator.product.workpackage.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceWorkpackageTemplateSummary,
		}, 
		fields: {
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'WorkPackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceResourceRAGSummary(containerId){
	var entityTypeTitle="Resource Queue";
	entityTypeTitles = entityTypeTitle;
	
	var entityParentInstanceId=0;
	if (wfSummaryEntityTypeId == 34) {
		entityParentInstanceId=wfSummaryProductBuildId;
	} else if(wfSummaryEntityTypeId == 33){
		entityParentInstanceId=wfSummaryActivityWorkPackageId;
	}
	var urlToListWorkflowEntityInstanceResourceTemplateSummary = 'workflow.RAG.indicator.product.resource.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+entityParentInstanceId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(1).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceResourceTemplateSummary,
		}, 
		fields: {
			userId:{
				list: false,
				edit : false,
				create: false,
			},
			roleId:{
				list: false,
				edit : false,
				create: false,
			},
			userName:{
				title : 'Resource',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activites"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
					var link = '<a href="javascript:void(0)" onclick="viewWorkflowResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
					var link = '<a href="javascript:void(0)" onclick="viewWorkflowResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceTemplateRAGActivitiesSummary(containerId){
	var entityTypeTitle="Activities";	
	var urlToListWorkflowEntityInstanceActivityTemplateSummary = 'workflow.RAG.indicator.product.activity.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceActivityTemplateSummary,
		}, 
		fields: {
			activityId:{
				list: false,
				edit : false,
				create: false,
			},
			activityName:{
				title : 'Activity',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.activityId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.activityId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.activityId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.activityId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityTypeBasedDetail('+data.record.activityId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

/*
function lisRAGViewActivityCategorySummary(containerId){
	var entityTypeTitle="Category Summary";
	
	var urlToLisRAGViewActivityCategorySummary = 'workflow.RAG.indicator.activity.category.summary?engagementId='+wfSummaryEngagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").eq(2).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToLisRAGViewActivityCategorySummary,
		}, 
		fields: {
			engagementId:{
				list: false,
				edit : false,
				create: false,
			},
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			activityCategoryId:{
				list: false,
				edit : false,
				create: false,
			},
			activityCategoryName:{
				title : 'Category',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+',\'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Need immediate attention"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="Upcoming Activities"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
		        }
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
		
	});
	$('#'+containerId).jtable('load'); 
}
*/

function viewWorkflowIndicatorDetailView(indicatiorValue) {
	var userId = 0;
	var roleId = 0;
	var workflowStatusId = 0;
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails( wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicatiorValue,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowStatusBasedDetail(workflowStatusId, entityId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var typeId=0;
	var titlename = "Status Summary";
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,titlename,wfSummaryEntityInstanceId,workflowStatusCategoryId);
} 

function viewWorkflowStatusCategoryBasedDetail(workflowStatusCategoryId, entityId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var typeId=0;
	var workflowStatusId =0;
	var titlename = "Status Category Summary";
	listWorkflowSummarySLADetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,titlename,wfSummaryEntityInstanceId,workflowStatusCategoryId);
} 

function viewWorkflowUserBasedDetail(workflowStatusId, entityId, userId){
	var roleId = 0;
	var indicator = "";
	var typeId=0;
	var titlename = "User Queue";
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,titlename,wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowRoleBasedDetail(workflowStatusId, entityId, roleId){
	var userId = 0;
	var indicator = "";
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowTypeBasedDetail(typeId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var workflowStatusId=0;
	var titlename = entityTypeTitles;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,titlename,wfSummaryEntityInstanceId,workflowStatusCategoryId);
} 


function viewWorkflowIndicatorDetailRAGView(indicatiorValue) {
	var userId = 0;
	var roleId = 0;
	var titlename = entityTypeTitles;
	var categoryId=0;
	var entityTypeInstanceId=0;
	if(wfSummaryActivityWorkPackageId != 0){
		entityTypeInstanceId=wfSummaryActivityWorkPackageId;
	}
	listWorkflowSummarySLARAGDetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicatiorValue,userId, roleId,entityTypeInstanceId,categoryId,titlename);
}

function viewWorkflowResourceBasedDetail(userId, roleId,indicator){
	var entityTypeInstanceId=0;
	var titlename = entityTypeTitles;
	if(roleId == null) {
		roleId=0;
	}
	if(userId == null) {
		userId=0;
	}
	if(wfSummaryProductId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=0;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(entityTypeInstanceId == 0){
		entityTypeInstanceId=wfSummaryActivityWorkPackageId;
	}
	var categoryId=0;
	listWorkflowSummarySLARAGDetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId,categoryId,titlename);
} 

function viewWorkflowEntityTypeBasedDetail(productId,entityTypeInstanceId, indicator){
	var roleId = 0;
	var userId = 0;
	var categoryId=0;
	var titlename = entityTypeTitles;
	if(productId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=entityTypeInstanceId;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(wfSummaryProductId ==0) {
		wfSummaryProductId=productId;
	}
	listWorkflowSummarySLARAGDetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId,categoryId,titlename);
}


function viewWorkflowEntityCategoryBasedDetail(productId,entityTypeInstanceId,categoryId,indicator){
	var roleId = 0;
	var userId = 0;
	var titlename = entityTypeTitles;
	if(productId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=entityTypeInstanceId;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(wfSummaryProductId ==0) {
		wfSummaryProductId=productId;
	}
	listWorkflowSummarySLARAGDetails(wfSummaryEngagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId,categoryId,titlename);
}