var testFactoryId = 0;
var productId = "";
var isFirstLoad = true;
var isAddonUsersAllowed = false;

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("Products");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	getTreeData('administration.product.bot.tree');
	setDefaultnode("j1_1");
		
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){
			
   			var entityIdAndType =  data.node.data;
   			var arry = entityIdAndType.split("~");
   			var key = arry[0];
   			var type = arry[1];
   			//var name = arry[2];
   			//var title = data.node.text;
			//var date = new Date();
		    //var timestamp = date.getTime();
		    var nodeType = type;
		    var parent = data.node.original.parent;
		    var loMainSelected = data;
	        uiGetParents(loMainSelected);
	        
	        if(nodeType == 'Product' || nodeType == 'product'){
	        	productId = key;
	        	testFactoryId = data.node.original.parent;
	        }else{
	        	testFactoryId = key;
	        	productId = 0;
	        }
	        
	        getProductBotDetails(productId);		
			if(parent == "#"){
				setDefaultnode(data.node.id);
			}
		}
	);
	
});


function listStatusAndPolicies($img, workflowId, entityTypeId, entityId){
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $img.closest('tr'), 
	{
		title: 'Status Policy',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        /* saveUserPreferences: false, */
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+workflowId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&scopeType=Entity',
            //createAction: 'workflow.status.policy.add?workflowId='+workflowId,
            editinlineAction: 'workflow.status.policy.update?workflowId='+workflowId,
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusPolicyId: { 
				type: 'hidden', 
				edit: false,
			}, 
			workflowStatusName: { 
				title: 'Status Name',
				inputTitle: 'Status Name <font color="#efd125" size="4px">*</font>',
				width: "15%",
				list: true,
				edit : false,
				create: true,
			},
			workflowStatusDisplayName: { 
            	 title: 'Status Display Name',
            	 inputTitle: 'Status Name <font color="#efd125" size="4px">*</font>',
                 width: "15%",
                 list: true,
                 edit : true,
                 create: true,	
   			},
			description: { 
				title: 'Description',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			statusCategoryId: { 
				title : 'Status Category',
				inputTitle: 'Status Category <font color="#efd125" size="4px">*</font>',
				create:false,
				list : false,
				edit : false,
				defaultValue : 1,
				options:function(data){
					return 'status.category.option.list';
				},
			},
			/*statusPolicyType: {
				title: 'Status Policy Type',
				inputTitle : 'Status Policy Type',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},*/   			   	   
			weightage: {
				title: 'Weightage',
				inputTitle:'Weightage',
				width: "10%",
				list: true,
				edit : true,
				create: true,
			},
			slaDuration : {
				title: 'SLA Duration(Hrs)',
				create : true,
				edit : true,
				list: true,
			},
			slaViolationAction : {
				title : 'SLA Violation Action',
				width : "20%",
				create : true,
				edit : true,
				list: true,
			},
			statusOrder: {
   				title: 'Status Order',
   				inputTitle:'Status Order',
                width: "15%",
                list: true,
               edit : false,
               create: true,
  		  	},
	  		workflowStatusType : {
	    		 title : 'Workflow Status Type',
	    		 width : "10%",
	  			 inputTitle: 'Workflow Status Type',
	              create : true,
	              edit : true,
	              list: true,
	              options:function(data){
				 	return 'workflow.status.type.option.list';
	     		},
			}, 
			stautsTransitionPolicy : {
	    		 title : 'Status Transition Policy',
	    		 width : "10%",
	    		 inputTitle: 'Status Transition Policy',
	             create : true,
	             edit : true,
	             list: true,
	             dependsOn:'actionScope',
	             options:function(data){
				 	return 'workflow.status.transition.policy.option.list?actionScope='+data.dependedValues.actionScope;
	     		},
			},
			actionScope: {
				title: 'Actor',
				inputTitle: 'Actor',
				create : true,
				edit : true,
				list: true,
				options:function(data){
					return 'workflow.status.policy.action.mode.option.list';
				},
			},
			usersMapping:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : true,
	        	width: "5%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
	        		var $imgUser = $('<i class="fa fa-user" title="Users"></i>');
	        		$imgUser.click(function () {
	        			$imgUser = listUsers($imgUser, data.record.workflowStatusId, entityTypeId, entityId);
           		  });
           			return $imgUser;
	        	}
	        }, 
	        rolesMapping:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : true,
	        	width: "5%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
	        		var $imgRole = $('<i class="fa fa-users" title="Roles"></i>');
	        		$imgRole.click(function () {
	        			$imgRole = listRoles($imgRole, data.record.workflowStatusId, entityTypeId, entityId);
           		  });
           			return $imgRole;
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
    	data.childTable.jtable('load'); 
	});

	return $img; 
}

function listUsers($imgUser, workflowStatusId, entityTypeId, entityId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	
	if(entityTypeId == 30 || entityTypeId == 33){
		isAddonUsersAllowed = true;
	}
	
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $imgUser.closest('tr'), 
	{
		title: 'Status Policy User',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        /* saveUserPreferences: false, */
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=User',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=User',
            editinlineAction :'workflow.status.policy.actor.update',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			//$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			//$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusId:{
	          	 type : 'hidden',
	           	 defaultValue: workflowStatusId,
	        },
	        workflowStatusActorId:{
	        	list:false,
	           	edit:false,
	           	create:false,
	        	key: true,
	        },
			userId: { 
				title: 'User',
				edit: false,
				create: true,
				list: true,
				options : function(data) {
					return 'workflow.user.list.by.product.id.options?productId='+productId+'&isAddonUsersAllowed='+isAddonUsersAllowed;
				}
			}, 
			actionRequirement: { 
				title : 'Action Type',
				inputTitle: 'Action Type <font color="#efd125" size="4px"></font>',
				create:true,
				list : true,
				edit : true,
				defaultValue : 'Optional',
				options: {'Optional' : 'Optional', 'Mandatory' : 'Mandatory'}
			},
			userActionStatus: { 
				title : 'Action Status',
				create:false,
				list : false,
				edit : false,
				options: {'Not Complete' : 'Not Complete', 'Completed' : 'Completed'},
				defaultValue : 'Not Complete'
			}
		}, 
		formCreated: function (event, data) {
			data.form.find('select[name="userId"]').addClass('validate[required]');
            data.form.validationEngine();
        },
		formSubmitting: function (event, data) {
			//data.form.validationEngine();
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

	return $imgUser; 
}

function listRoles($imgRole, workflowStatusId, entityTypeId, entityId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $imgRole.closest('tr'), 
	{
		title: 'Status Policy Role',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:false},	         
        //sorting: true, //Enable sortin
        /* saveUserPreferences: false, */
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=Role',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=Role',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusId:{
	          	 type : 'hidden',
	           	 defaultValue: workflowStatusId,
	        },
	        workflowStatusActorId:{
	        	list:false,
	           	edit:false,
	           	create:false,
	        	key: true,
	        },
			roleId: { 
				title: 'Role',
				edit: false,
				create: true,
				list: true,
				options : function(data) {
					return 'administration.user.role.list?typeFilter=1';
				}
			}, 
			/*actionRequirement: { 
				title : 'Action Type',
				inputTitle: 'Action Type <font color="#efd125" size="4px"></font>',
				create:true,
				list : true,
				edit : false,
				defaultValue : 'Optional',
				options: {'Optional' : 'Optional', 'Mandatory' : 'Mandatory'}
			},
			userActionStatus: { 
				title : 'Action Status',
				create:false,
				list : false,
				edit : false,
				options: {'Not Complete' : 'Not Complete', 'Completed' : 'Completed'},
				defaultValue : 'Not Complete'
			}*/
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

	return $imgRole; 
}

function setDefaultnode(defaultNodeId) {			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					return false;
				}
			});	
			setDefaultnode(defaultNodeId);
		});
	} else {
		defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		$.jstree.reference('#treeContainerDiv').deselect_all();
		$.jstree.reference('#treeContainerDiv').close_all();
		$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
		$.jstree.reference('#treeContainerDiv').trigger("select_node");
	}
}


function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
	};
	CommentsMetronicsUI.init(jsonObj);
}