// ---- Raise Resource Demand Table starts ----

function raiseResourceDemandForWorkPackageForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId){
	var curDemandShiftName = document.getElementById("currentDemandShiftName").value;
	var cWPackgeName = document.getElementById("cworkPackageName").value;		
	var weekToRaiseDemand = workWeek;
	var titleName = popupTitle_RaiseResourceDemandFor+" "+cWPackgeName+" for WeekNo: "+weekToRaiseDemand+" for Shift: "+curDemandShiftName;
    
   urlToRaiseSkillBasedDemandForWeekly = 'workpackage.demand.projection.weekly.add?workPackageId='+workPackageId+"&workYear="+operationYear;
   selectedWorkWeekForDemand = workWeek;
   selectedShiftIdForDemand = shiftId;
   urlToListSkillBasedDemandForWeekly ='workpackage.skill.demand.projection.list.by.week.and.year?workPackageId='+workPackageId+"&shiftId="+shiftId+"&workWeek="+weekToRaiseDemand+"&workYear="+operationYear+"&skillId="+skillId+"&roleId="+roleId+"&userTypeId="+userTypeId+"&recursiveWeeks="+demandRecursive; 
   var url = urlToListSkillBasedDemandForWeekly+'&jtStartIndex=0&jtPageSize=10000';
 
   /* urlToUpdateSkillBasedDemandForWeekly='workpackage.demand.projection.skill.weekly.data.update',
    urlToDeleteSkillBasedDemandForWeekly='workpackage.demand.projection.skill.weekly.data.delete';*/
    
    divPopupMainTitle(titleName);
    assignDataTableValuesInRaiseDemand(url, "parentTable","","",workPackageId);
	$("#dragDropListItemLoaderIcon").show();
 }

var raiseDemandDT_oTable='';

var raiseDemandDTJsonData='';
var raiseResourceDemandFlag=false;
var editorRaiseResourceDemand ="";
function assignDataTableValuesInRaiseDemand(url,tableValue, row, tr,workPackageId){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				raiseDemandDTJsonData = data;
				if(!raiseResourceDemandFlag){
					raiseReourceDemand_Container(data, "240px", row,workPackageId);
					setTimeout(function(){$("#dragDropListItemLoaderIcon").hide();},500);
			}else{				
				reloadDataTableHandler(data, raiseDemandDT_oTable);
			}
			}else{
				console.log("no child");
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

var optionsRaiseRsrDemandArr=[];
var optionsRaiseRsrDemandResultArr=[];
var optionsItemRaiseRsrDemandCounter=0;
var optionsType_RaiseRsrDemand="RaiseResourceDemand";

function raiseReourceDemand_Container(data, scrollYValue, row,workPackageId){     
	optionsItemRaiseRsrDemandCounter=0;
	optionsRaiseRsrDemandResultArr=[];     
	optionsRaiseRsrDemandArr = [{id:"shiftName", type:optionsType_RaiseRsrDemand,url:'common.list.workshift.list.workpackage.weekly.demand?workpackageId='+workPackageId},
	              {id:"userRoleId", type:optionsType_RaiseRsrDemand, url:'list.roles.for.demand.projection'},
	               {id:"userTypeId", type:optionsType_RaiseRsrDemand, url:'administration.user.userType'},
	               
	              {id:"skillId", type:optionsType_RaiseRsrDemand, url:'common.list.skill.list?skillIdfromUI=1'},
	             ];
	returnOptionsItemRaiseRsrDemand(optionsRaiseRsrDemandArr[0].url, scrollYValue, data, row);
}

function returnOptionsItemRaiseRsrDemand(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json.Result == "Error" || json.Options == null){
         	callAlert(json.Message);
         	json.Options=[];
         	optionsRaiseRsrDemandResultArr.push(json.Options);
         	if(optionsRaiseRsrDemandArr[0].type == optionsType_RaiseRsrDemand){
         		raiseReourceDemandContainer(data, scrollYValue, tr);     			   
  		   } 
         }else{
     	   if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	  optionsRaiseRsrDemandResultArr.push(json.Options);
     	   
     	   if(optionsItemRaiseRsrDemandCounter<optionsRaiseRsrDemandArr.length-1){
     		 // optionsRaiseRsrDemandArr[optionsRaiseRsrDemandResultArr.length].url = optionsRaiseRsrDemandArr[optionsRaiseRsrDemandResultArr.length].url+''+json.Options[0].value;
     
     		  returnOptionsItemRaiseRsrDemand(optionsRaiseRsrDemandArr[optionsRaiseRsrDemandResultArr.length].url, scrollYValue, data, tr);     		  
     	   }else{
     		   if(optionsRaiseRsrDemandArr[0].type == optionsType_RaiseRsrDemand){
     			  raiseReourceDemandContainer(data, scrollYValue, tr);     			   
     		   }
     	   }
     	  optionsItemRaiseRsrDemandCounter++;     	   
         }
         },
         error: function (data) {
        	 optionsItemRaiseRsrDemandCounter++;
        	 
         },
         complete: function(data){
         	console.log('Completed');	
         },	            
   	});		
}

function raiseResourceDemandDataTable(){
	var parentDivString = '<table id="RaiseResourceDemand_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTable">WorkPackage</th>'+
			'<th class="dataTable">WorkWeek</th>'+
			'<th class="dataTable">Shift</th>'+
			'<th class="dataTable">Role</th>'+
			'<th class="dataTable">UserType</th>'+
			'<th class="dataTable">Skill</th>'+
			'<th class="dataTable">Resources</th>'+
			'<th class="dataTable"></th>'+
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return parentDivString;	
}
var recurWeekVal;
function raiseReourceDemandContainer(data, scrollYValue, row){	
	
	editorRaiseResourceDemand = new $.fn.dataTable.Editor( { 
		    "table": "#RaiseResourceDemand_dataTable",
			ajax: urlToRaiseSkillBasedDemandForWeekly,
			ajaxUrl: "workpackage.demand.projection.skill.weekly.data.update",
			idSrc:  "wpDemandProjectionId",
			i18n: {
		        create: {
		            title:  "Raise Resource Demand",
		            submit: "Create",
		        }
		    },
			fields: [/*{
		        label: "Work Package Id:",
		        name: "workPackageId",
		        "type": "hidden",	
		        "def":workPackageId,
		    }*/{
		        label: "Work Package:",
		        name: "workPackageName",
		        "type": "hidden",
		        "def":workPackageName,
		    },{
		        label: "Work Week:",
		        name: "workWeek",
		        "type": "hidden",
		        "def": selectedWorkWeekForDemand
		    },{
		        label: "Shift",
		        name: "shiftId",
		        type : "select",
		        options: optionsRaiseRsrDemandResultArr[0],
		        "def": selectedShiftIdForDemand,
		    },{
		        label: "Shift Name",
		        name: "shiftName",
		        "type": "hidden",	
		       //"def": 8,
		    },{
		        label: "roleId",
		        name: "roleId",
		        type : "hidden",
		        "def":roleId,
		    },{
		        label: "Role",
		        name: "userRoleId",
		        type : "select",		        
		        options: optionsRaiseRsrDemandResultArr[1]
		    },{
		        label: "User Type",
		        name: "userTypeId",
		        type : "select",		        
		        options: optionsRaiseRsrDemandResultArr[2]
		    },{
		        label: "Skill:",
		        name: "skillId",
		        type : "select",		       
		        options: optionsRaiseRsrDemandResultArr[3]
		    },{
		        label: "Resources:",
		        name: "skillandRoleBasedresourceCount",
		        "def": 1,
		    },{
		        label: "Demand Week:",
		        name: "recursiveWeeks",
		        "def": demandRecursive,
		    }, 
		]
	});
	
	try{
		if ($('#JTable_Allocate').children().length>0) {
			 $('#JTable_Allocate').children().remove(); 
		}
	} catch(e) {}
	
	var parentDivString = raiseResourceDemandDataTable(); 			 
	$("#JTable_Allocate").append(parentDivString);
	 
	raiseDemandDT_oTable = $('#RaiseResourceDemand_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "100%",
        "sScrollXInner": "100%",
        "scrollY":"100%",
        "bScrollCollapse": true,
       /* fixedColumns: {
            leftColumns: 1,
            rightColumns: 1,
        },*/
        "fnInitComplete": function(data) {
     	   //var searchcolumnVisibleIndex = [0,1,7,9,10]; // search column TextBox Invisible Column position
 		   
     	   /*if(!productManagementFlag){
     		  $('#products_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   }
     	   productManagementFlag=true;
     	   reInitializeDTProductManagementPlan();*/
 	   }, 
		buttons: [
		       { extend: "create", editor: editorRaiseResourceDemand },
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Raise Demand',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Raise Demand',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Raise Demand',
                    	exportOptions: {
                            columns: ':visible',	                            
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
                    { mData: "workPackageName", className:'disableEditInline', sWidth: '15%' },		
                    { mData: "workWeek", className:'disableEditInline', sWidth: '15%' },	
                    //{ mData: "shiftName", className:'disableEditInline', sWidth: '15%' },
                    { mData: "shiftName", className: 'disableEditInline', sWidth: '20%', editField: "shiftId",
        				mRender: function (data, type, full) {
        					 if (full.action == "create" || full.action == "edit"){
        						data = optionsValueHandler(editorRaiseResourceDemand, 'shiftId', full.shiftId);
        					 }
        					 else if(type == "display"){
        						data = full.shiftName;
        					 }	           	 
        					 return data;
        				 },
        			},	
                    { mData: "userRoleName", className: 'disableEditInline', sWidth: '20%', editField: "userRoleId",
        				mRender: function (data, type, full) {
        					 if (full.action == "create" || full.action == "edit"){
        						data = optionsValueHandler(editorRaiseResourceDemand, 'userRoleId', full.userRoleId);
        					 }
        					 else if(type == "display"){
        						data = full.userRoleName;
        					 }	           	 
        					 return data;
        				 },
        			},	
        			
        			{ mData: "userTypeName", className: 'disableEditInline', sWidth: '20%', editField: "userTypeId",
        				mRender: function (data, type, full) {
        					 if (full.action == "create" || full.action == "edit"){
        						data = optionsValueHandler(editorRaiseResourceDemand, 'userTypeId', full.userTypeId);
        					 }
        					 else if(type == "display"){
        						data = full.userTypeName;
        					 }	           	 
        					 return data;
        				 },
        			},	
        			{ mData: "skillName", className: 'disableEditInline', sWidth: '20%', editField: "skillId",
        				mRender: function (data, type, full) {
        					 if (full.action == "create" || full.action == "edit"){
        						data = optionsValueHandler(editorRaiseResourceDemand, 'skillId', full.skillId);
        					 }
        					 else if(type == "display"){
        						data = full.skillName;
        					 }	           	 
        					 return data;
        				 },
        			},	
                    { mData: "skillandRoleBasedresourceCount",className:'editable', sWidth: '15%' },
                    { mData: null,				 
    	            	bSortable: false,
    	            	mRender: function(data, type, full) {				            	
    	           		 var img = ('<div style="display: flex;">'+
       						'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
       								'<i class="fa fa-search-plus img1" title="Audit History"></i></button>'+
       						'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
       						'<img src="css/images/edit.png" class="details-control img2" title="Edit Record" style="width: 20px;height: 16px;">'+
             	       		'</div>');	      		
                   		 return img;
    	            	}
    	            },			
                    
                ],
                rowCallback: function ( row, data ) {
                	recurWeekVal = $('#DTE_Field_recursiveWeeks').val();
                	if(recurWeekVal != undefined){
                		$('#demandRecursive').val(recurWeekVal);
         	          	raiseResourceDemandPopupData();                		
                	}
    	        },
        "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
        },
	});
	
	// ------
	 $(function(){ // this will be called when the DOM is ready 
		 
		 $( 'input', editorRaiseResourceDemand.node()).on( 'focus', function () {
			this.select();
		});
		 
		 $("#RaiseResourceDemand_dataTable").on( 'click', 'tbody td.editable', function (e) {
			 editorRaiseResourceDemand.inline( this, {
	                submitOnBlur: true
	            } );
	        } );
		 
		$("#RaiseResourceDemand_dataTable_length").css('margin-top','8px');
		$("#RaiseResourceDemand_dataTable_length").css('padding-left','35px');	
		$(".select2-drop").css('z-index','100000');
		 
		 raiseDemandDT_oTable.DataTable().columns().every( function () {
	       var that = this;
	       $('input', this.footer() ).on( 'keyup change', function () {
	           if ( that.search() !== this.value && this.id == "") {
	               that
	                   .search( this.value )
	                   .draw();
	           }
	       } );
	   });

		   // ----- Activity Audit History child table -----
		   $('#RaiseResourceDemand_dataTable tbody').on('click', 'td button .img1', function () {
			   	var tr = $(this).closest('tr');
			   	var row = raiseDemandDT_oTable.DataTable().row(tr);
			   	listGenericAuditHistory(row.data().wpDemandProjectionId,"ResourceDemand","resourceDemandAudit");
		   });
		   
		   // ----- Activity bulk Update popup -----
		   $('#RaiseResourceDemand_dataTable tbody').on('click', 'td button .img2', function () {
			   	var tr = $(this).closest('tr');
			   	var row = raiseDemandDT_oTable.DataTable().row(tr);
			   	wpDemandProjectionIdSource = row.data().wpDemandProjectionId;
			   	//demandRecursive = row.data().workWeek
			   	console.log("==="+wpDemandProjectionIdSource);
			   	demandResource = row.data().skillandRoleBasedresourceCount;
			   	console.log("resource = "+demandResource);
			   	demandResourceWeeks=demandRecursive;
			   	$("#resource_name").val(demandResource);
			   	$("#demand_Week").val(demandResourceWeeks);
			   	console.log("resourceweeks = "+demandResourceWeeks);
			   	$("#editRaiseResourceDemandContainer").modal();
		   });
  });
}

function saveRaiseRsrDemandRecord(){
	demandResource=$("#resource_name").val();
	demandResourceWeeks = $("#demand_Week").val();
	var fd = new FormData();
	fd.append("wpDemandProjectionId", wpDemandProjectionIdSource);  
	fd.append("skillandRoleBasedresourceCount", demandResource);
	fd.append("recursiveWeeks", demandResourceWeeks);
	
	openLoaderIcon();
	$("#dragDropListItemLoaderIcon").show();
	
	$.ajax({
		url : 'workpackage.demand.projection.skill.weekly.data.update',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			closeLoaderIcon();
			$("#dragDropListItemLoaderIcon").hide();
			$('#editRaiseResourceDemandContainer').modal('hide');
		},
		error : function(data) {
			$("#dragDropListItemLoaderIcon").hide();
			closeLoaderIcon();  
		},
		complete: function(data){
			$("#dragDropListItemLoaderIcon").hide();
			closeLoaderIcon();
		}
	});
}
//---- Raise Resource Demand Table Ends ----


//---- Reserve Resource Demand Table Starts ----
function getResourcesForReservationWeekly(currentWorkPackageName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,resourceDemandCount,groupDemandId,userTypeId){	
//	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	//currentShiftName = document.getElementById("currentShiftName").value;
	//groupDemandId = document.getElementById("groupDemandId").value; 
	
	weekDetails = weeksName[workWeek-1];
	currentWorkPackageName = selectedDetails["selectedWorkPackageName"];
	weekDetails = selectedDetails["selectedWorkWeekName"];
	userTypeName = selectedDetails["selectedUserType"];
	userRoleName = selectedDetails["selectedUserRole"];
	skillName = selectedDetails["selectedUserSkill"];
	
//	var details = currentWorkPackageName+" for Week : "+weekDetails+" and for Shift: "+selectedShiftName;
	var details = "Reserve for WP : "+currentWorkPackageName+", Week : "+weekDetails+", Role : "+userRoleName+", Type : "+userTypeName+", Skill : "+skillName;
	var date = new Date();
    var timestamp = date.getTime();
    urlToGetResourcesOfWorkPackageWeekly = "workPackage.block.resources.weekly?workPackageId="+workPackageId+"&shiftId="+shiftId+"&workWeek="+workWeek+"&workYear="+workYear+"&userRoleId="+userRoleId+"&skillId="+skillId+"&userTypeId="+userTypeId+"&filter="+availablilityType;
    var url= urlToGetResourcesOfWorkPackageWeekly+'&jtStartIndex=0&jtPageSize=10000';
    $("#div_PopupMain").find("h4").text(details);
	loadPopup("div_PopupMain");
	assignDataTableValuesInReserveDemand(url, "childTable","",""); 
}
var reserveDemandDT_oTable='';
var reserveDemandDTJsonData='';
var reserveResourceDemandFlag=false;

function assignDataTableValuesInReserveDemand(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "childTable"){
				reserveDemandDTJsonData = data;
				if(!reserveResourceDemandFlag){
					reserveReourceDemand_Container(data, "240px", row);
			}else{				
				reloadDataTableHandler(data, reserveDemandDT_oTable);
			}
			}else{
				console.log("no child");
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

function reserveResourceDemandDataTable(){
	var parentDivString = '<table id="reserveResourceDemand_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTable">User</th>'+
			'<th class="dataTable">User Code</th>'+
			'<th class="dataTable">Role</th>'+
			'<th class="dataTable">User Type</th>'+
			'<th class="dataTable">Skill</th>'+
			'<th class="dataTable">Reservation Details</th>'+
			'<th class="dataTable">Reservation Percentage</th>'+
		'</tr>'+
	'</thead>'+
	'</table>';		
	return parentDivString;	
}

function reserveReourceDemand_Container(data, scrollYValue, row){
	try{
		if ($('#div_Table3').children().length>0) {
			 $('#div_Table3').children().remove(); 
		}
	} catch(e) {}
	var parentDivString = reserveResourceDemandDataTable(); 			 
	$("#div_Table3").append(parentDivString);
	reserveDemandDT_oTable = $('#reserveResourceDemand_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "100%",
        "sScrollXInner": "100%",
        "scrollY":"100%",
        "bScrollCollapse": true,
       /* fixedColumns: {
            leftColumns: 1,
            rightColumns: 1,
        },*/
        "fnInitComplete": function(data) {
     	   //var searchcolumnVisibleIndex = [0,1,7,9,10]; // search column TextBox Invisible Column position
 		   
     	   /*if(!productManagementFlag){
     		  $('#products_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   }
     	   productManagementFlag=true;
     	   reInitializeDTProductManagementPlan();*/
 	   }, 
		buttons: [
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Reserve Demand',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Reserve Demand',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Reserve Demand',
                    	exportOptions: {
                            columns: ':visible',	                            
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
                    { mData: "loginId", className:'disableEditInline', sWidth: '15%' },		
                    { mData: "userCode", className:'disableEditInline', sWidth: '15%' },	
                    { mData: "userRoleLabel", className:'disableEditInline', sWidth: '15%' },
                    { mData: "userTypeLabel", className:'disableEditInline', sWidth: '15%' },	
                    { mData: "skillName", className:'disableEditInline', sWidth: '15%' },	
                    { mData: "reservationDetails", className:'disableEditInline', sWidth: '15%' },	
                    { mData: "reservationPercentage", className:'disableEditInline', sWidth: '15%' },	    
                ],
        "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
        },
	});
	
	
	$(function(){ // this will be called when the DOM is ready 

		$("#reserveResourceDemand_dataTable_length").css('margin-top','8px');
		$("#reserveResourceDemand_dataTable_length").css('padding-left','35px');	
		$(".select2-drop").css('z-index','100000');	
		$("#div_Table3").css('border','none');	
	});

}
//---- Reserve Resource Demand Table Ends ----