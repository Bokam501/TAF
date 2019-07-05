var newnodetype = "";
var selectedCompetency = "";
var status = "2";

var urlToCompetencyDetails = "";
var urlToCompetencyMemberDetails = "competency.user.by.status";
var urlToDimensionProductDetails = "dimension.product.by.status";

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("Manage Competency");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	getTreeData('dimension.type.tree?dimensionTypeId=1');
	
	urlToCompetencyDetails = 'dimension.list.by.status?status=1&dimensionTypeId=1';
	
	if(userRole != 'TESTMANAGER' && userRole != 'TestManager'){
		listCompetencies();		
	}
	
	$("#treeContainerDiv").on("loaded.jstree", function(evt, data){
	   var defaultNodeId = "j1_1";
	   $.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
   });
	
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){
			
   			var entityIdAndType =  data.node.data;
   			var arry = entityIdAndType.split("~");
   			var key = arry[0];
   			var type = arry[1];
   			var name = arry[2];
   			var title = data.node.text;
			var date = new Date();
		    var timestamp = date.getTime();
		    var nodeType = type;
		    var parent = data.node.original.parent;
		    var loMainSelected = data;
	        uiGetParents(loMainSelected);
	        
	        var id = $("#status_ul_competencies").find('option:selected').val();
			if(parent == "#"){
				urlToCompetencyDetails = 'dimension.list.by.status?status='+id+'&dimensionTypeId=1';
				newnodetype=nodeType;
				listCompetencies();
			}else{
				if(name.toLowerCase() != "no competency available"){
					selectedCompetency = key;
					$("#competenciesList").hide();
					$('#tabslistCompetencies li').first().find("a").trigger("click");
					$("#competencyTabs").show();
					$("#tbCntnt").show();
				}else{
					$("#competenciesList").hide();
				}
			}
		}
	);
	
	$(document).on('change','#status_ul_competencies', function() {
		var id = $("#status_ul_competencies").find('option:selected').val();
	    urlToCompetencyDetails = 'dimension.list.by.status?status='+id+'&dimensionTypeId=1';
	    listCompetencies();
	});
	
	$(document).on('change','#status_ul_products', function() {
		status = $("#status_ul_products").find('option:selected').val();
	    listProducts();
	});
	
	$(document).on('change','#status_ul_team', function() {
		status = $("#status_ul_team").find('option:selected').val();
	    listTeam();
	});
	
});


function listCompetencies(){
	$("#competencyTabs").hide();
	$("#tbCntnt").hide();
	$("#competenciesList").show();
	try{
		if ($('#jTableContainerCompetencies').length>0) {
			$('#jTableContainerCompetencies').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerCompetencies').jtable({
         title: 'Competencies',
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
             listAction: urlToCompetencyDetails,
     		 //createAction: 'competency.add',
             editinlineAction: 'dimension.update'
             //deleteAction: 'competency.delete'
         },
         fields: {
         				   		
   			dimensionId: { 
   				key: true,
   				type: 'hidden',
   				create: false, 
   				edit: false, 
   				list: false,
   				display: function (dataCompetency) {return dataCompetency.record.dimensionId;},	   			 
   			}, 
   			name: { 
     	  		title: 'Name',
     	  		width: '20%',
     	  		edit: true,
     	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
     	  		list:true
  			},
  		  	description: { 
       			title: 'Description' ,
       			width: '30%',
       			edit: true,
     		  	list:true
    	  	}, 
    	  	parentId:{
            	title: 'Parent Competency',
            	width: '25%',
            	list:false,
            	edit: false,
            	create:false,
            	options:function(data){
		        	return 'dimension.list.options?dimensionTypeId=1';
		        },		        	 	
            },  
			status: {	      
		       	title: 'Status' ,
		       	width: '15%',
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
		    ownerName: { 
       			title: 'Owner' ,
       			width: '30%',
       			edit: false,
     		  	create: false,
     		  	list:true
    	  	}, 
    	  	
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="name"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerCompetencies').jtable('load');		 
}

$(document).on('click', '#tabslistCompetencies>li', function(){
	selectedTab=$(this).index();
	competencyTabSelection(selectedTab);
});

function competencyTabSelection(selectedTab){
	status = "2";
	if(selectedTab==0){						  
		var firstTab = $(".Summary");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		showCompetencySummaryDetails(selectedCompetency);
	  
	 }else if (selectedTab==1){
		listProducts();
	}else if (selectedTab==2){
		$('select option[value="2"]').attr("selected",true);
		listTeam();
	}
}


function listProducts(){
	
	try{
		if ($('#jTableContainerProducts').length>0) {
			$('#jTableContainerProducts').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerProducts').jtable({
         title: 'Products',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
 			$('#jTableContainerProducts').find('.jtable-child-table-container').jtable('reload');
 		 },
 		 recordAdded: function (event, data) {
 			$('#jTableContainerProducts').find('.jtable-child-table-container').jtable('reload');
 			listProducts();
 		 },
 		 recordDeleted: function (event, data) {
 			$('#jTableContainerProducts').find('.jtable-child-table-container').jtable('reload');
 		 },
         actions: {
             listAction: urlToDimensionProductDetails+"?dimensionId="+selectedCompetency+"&status="+status,
     		 //createAction: 'competency.product.add',
             editinlineAction: 'dimension.product.update',
             //deleteAction: 'competency.delete'
         },
         fields: {
         				   		
        	competencyProductId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false,
			}, 
			dimensionId: { 
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false,
			}, 
			productName: { 
	  	  		title: 'Product Name',
	  	  		inputTitle: 'Product <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: false,
	  	  		edit: false,
	  	  		list:true,
   			},
   		  	startDate: { 
    			title: 'Begin Date' ,
    			inputTitle: 'Begin Date <font color="#efd125" size="4px">*</font>',
    			width: '15%',
    			type: 'date',
    			edit: true,
      		  	list:true,
     	  	}, 
     	  	endDate:{
             	title: 'End Date',
             	inputTitle: 'End Date <font color="#efd125" size="4px">*</font>',
             	width: '15%',
             	type: 'date',
             	list:true,
             	edit: true,
             			        	 	
             },  
 			status: {	      
 		       	title: 'Status' ,
 		       	width: '10%',
 	        	list:true,
 	        	edit:false,
 	        	create:false,
 	        	type : 'checkbox',
 	        	values: {'0' : 'Inactive','1' : 'Active'},
 		   		defaultValue: '1'
 		    },
 		    mappedByName: { 
    			title: 'Mapped By' ,
    			width: '20%',
    			edit: false,
      		  	create: false,
      		  	list:true
     	  	}, 
 		    mappedDate: { 
    			title: 'Mapped Date' ,
    			width: '15%',
    			edit: false,
      		  	create: false,
      		  	list:false
     	  	}, 
     	  	membersForProduct:{ 
             	title: 'Product Members',
             	width: "5%",
             	edit: true,
             	create: false,
             
        	 	display: function (productData) { 
        		//Create an image that will be used to open child table 
        			var $img = $('<img src="css/images/user.png" title="Members for Product" />');
        			//Open child table when user clicks the image 
        			$img.click(function () {
        				
        				// ----- Closing child table on the same icon click -----
        				closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerProducts"));
        				if(closeChildTableFlag){
        					return;
        				}
        				
        				$img = listProductMembers($img, productData.record.productId);
        			});
        			return $img; 
            	} 
    	  	}
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="competencyName"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');
        	 data.form.find('input[name="competencyDescription"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerProducts').jtable('load');		 
}


function listTeam(){
	
	try{
		if ($('#jTableContainerTeam').length>0) {
			$('#jTableContainerTeam').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerTeam').jtable({
         title: 'Team',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
		 },
		 recordAdded: function (event, data) {
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
			listTeam();
		 },
		 recordDeleted: function (event, data) {
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
		 },
         actions: {
             listAction: urlToCompetencyMemberDetails+"?competencyId="+selectedCompetency+"&status="+status,
     		 createAction: 'competency.user.add?dimensionId='+selectedCompetency,
             editinlineAction: 'competency.user.update',
             //deleteAction: 'competency.delete'
         },
         /*toolbar : {
        	 items : [
    	          {
    	        	  text : 'Add Members',
    	        	  click : function(){
    	        		var id = document.getElementById("hdnProductId").value;	        				
    	     			displayDownloadTestScriptsFromTestCases(id,"Product",scriptDownloadName);
    	        	  }
    	          }
        	 ]
     	},*/
         fields: {
         				   		
   			competencyMemberId: { 
   				key: true,
   				type: 'hidden',
   				create: false, 
   				edit: false, 
   				list: false,
   				//display: function (dataCompetency) {return dataCompetency.record.competencyMemberId;},	   			 
   			}, 
   			dimensionId: { 
   				type: 'hidden',
   				create: false, 
   				edit: false, 
   				list: false,
   				//display: function (dataCompetency) {return dataCompetency.record.dimensionId;},	   			 
   			}, 
   			userId: { 
     	  		title: 'Member',
     	  		inputTitle: 'Member <font color="#efd125" size="4px">*</font>',
     	  		width: '20%',
     	  		edit: false,
     	  		list:false,
     	  		options:function(data){
		        	return 'competency.user.list.to.add';
		        },
  			},
   			userName: { 
     	  		title: 'Member Name',
     	  		inputTitle: 'Member <font color="#efd125" size="4px">*</font>',
     	  		width: '20%',
     	  		create: false,
     	  		edit: false,
     	  		list:true,
   				//display: function (dataCompetency) {return dataCompetency.record.userName;},	
  			},
  		  	startDate: { 
       			title: 'Begin Date' ,
       			inputTitle: 'Begin Date <font color="#efd125" size="4px">*</font>',
       			width: '15%',
       			type: 'date',
       			edit: true,
     		  	list:true,
     		  	create:true,
    	  	}, 
    	  	endDate:{
            	title: 'End Date',
            	inputTitle: 'End Date <font color="#efd125" size="4px">*</font>',
            	width: '15%',
            	type: 'date',
            	list:true,
            	edit: true,
            	create:true,
            			        	 	
            },  
			status: {	      
		       	title: 'Status' ,
		       	width: '10%',
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
		    mappedByName: { 
       			title: 'Mapped By' ,
       			width: '20%',
       			edit: false,
     		  	create: false,
     		  	list:true
    	  	}, 
		    mappedDate: { 
       			title: 'Mapped Date' ,
       			width: '15%',
       			edit: false,
     		  	create: false,
     		  	list:false
    	  	}, 
			commentsCompTeam:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 64;
						var entityNameComments = "CompetencyMember";
						listComments(entityTypeIdComments, entityNameComments, data.record.competencyMemberId, data.record.userName, "teamCompetencyComments");
					});
					return $img;
				}		
			}, 
    	  	
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="startDate"]').addClass('validate[required]');
        	 data.form.find('input[name="endDate"]').addClass('validate[required]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerTeam').jtable('load');		 
}

function listProductMembers($img, productId){
	
	$('#jTableContainerProducts').jtable('openChildTable', 
		$img.closest('tr'), 
		{
			title: 'Product Members',
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
				listAction:  'competency.product.team.resouces.list?productId='+productId+'&dimensionId='+selectedCompetency, 
				editinlineAction: 'competency.product.team.user.update',  
				createAction: 'competency.product.team.user.add'
			}, 
			recordUpdated:function(event, data){
				$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
			},
			recordAdded: function (event, data) {
				$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
			},
			recordDeleted: function (event, data) {
				$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
			},
			fields: { 
				productId: { 
					type: 'hidden', 
					defaultValue: productId 
				}, 
				dimensionId: { 
					type: 'hidden', 
					defaultValue: selectedCompetency 
				},
				productTeamResourceId: { 
					key: true,
				    list: false,
				    create: false
				}, 
				loginId: {
				    title: 'User',
				    width:"15%",
				    list:true,
				    create:false,
				    edit:false,
				 }, 		 	
				 userId: {
				    title: 'User',
				   	inputTitle: 'User <font color="#efd125" size="4px">*</font>',
				    width:"15%",
				    edit : false,
				    list:false,
				    options: function(data) {
				        if (data.source=='create') {
				        	return 'competency.user.for.product.options?competencyId='+selectedCompetency;
				        	
				        }
					 },
				 },
				userDefaultRoleName: {
					    title: 'Default Role',
					    width:"15%",
					    list:true,
					    create:false,
					    edit:false
				},
			    productSpecificUserRoleId: {
				    title: 'Role',
				   	inputTitle: 'Role <font color="#efd125" size="4px">*</font>',
				    width:"15%",
				    dependsOn:'userId',
				   // options: 'testFactoryManagementControl.administration.user.roleList?userId='+data.dependedValues.userId;
				    options:function(data){
			       		if(data.dependedValues.userId!=null){
			        	return 'testFactoryManagementControl.administration.user.roleList?userId='+data.dependedValues.userId;
			       		}
			       	}				    	
				},  
				fromDate: { 
					title: 'From Date' ,
					inputTitle: 'From Date <font color="#efd125" size="4px">*</font>',
					edit: true, 
					list: true,
					type: 'date',
					width: "30%"          	
				},
				toDate: { 
					title: 'To Date' ,
					inputTitle: 'To Date <font color="#efd125" size="4px">*</font>',
					edit: true, 
					list: true,
					type: 'date',
					width: "30%"          	
				},
				remarks: { 
					title: 'Remarks' ,
					list:true
				},	
				status: {
					title: 'Status' ,
					inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
					width: "10%",  
					list:true,
					edit:false,
					create:false,
					type : 'checkbox',
					values: {'0' : 'No','1' : 'Yes'},
					defaultValue: '1'
				}, 
				commentsCompProduct:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 63;
							var entityNameComments = "Competency";
							listComments(entityTypeIdComments, entityNameComments, data.record.productTeamResourceId, data.record.loginId, "prodCompetencyComments");
						});
						return $img;
					}		
				},
						
			}, 
			
			formSubmitting: function (event, data) {
			fromdate=$("input[name=fromDate]").val();
			todate=$("input[name=toDate]").val();
			if(new Date(fromdate)>new Date(todate))
			{
				$("#basicAlert").css("z-index", "100001");
				
				callAlert("Warning: From date should be lessthan or equal to Todate");
				return false;
			}
			data.form.find('input[name="fromDate"]').addClass('validate[required]');
			data.form.find('input[name="toDate"]').addClass('validate[required]');
			
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