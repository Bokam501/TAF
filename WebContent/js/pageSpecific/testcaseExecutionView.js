var SOURCE;
var treeData;
var workPackageId = "";
var urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = "";
var nodeType = '';
var date = new Date();
var timestamp = date.getTime();
var prevTestcaseId='';
var nextTestcaseId='';
var wptceId='';
var date = new Date();
var timestamp = date.getTime();

function getWorkPackageTreeData() {
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'administration.workpackage.plan.tree',
		dataType : 'json',
		success : function(data) {
			setWorkPackageTreeData(data);
		}
	});
}; 

function setWorkPackageTreeData(data){
	treeData = data;
	showTree();
} 

function showTree(){
	SOURCE = jQuery.parseJSON(treeData);
	 $("#tree").fancytree({
		 	extensions: ["childcounter","persist"],  
		    activate: function(event,data) {
		    	getAncestorDepth(event,data);
				treeNodeClicked(event, data);
			},
			childcounter: {
		        deep: true,
		        hideZeros: true,
		        hideExpanded: true
			},
			persist: {
		        store: "auto" // 'cookie', 'local': use localStore, 'session': sessionStore
		      },
			checkbox: false,
		    selectMode: 3,
			source: SOURCE
	});  
}  

function treeNodeClicked(event, data){	
    nodeType=data.node.data.type;
    if(nodeType == "WorkPackage" || nodeType=='Environment'){
    	  workPackageId = data.node.key;
    	  urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.execution.tester?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&nodeType="+nodeType+"&plannedExecutionDate="+datepicker.value+"&filter=-1";
    	  getTestCaseData(urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId);
    }
}

function buildItem(item) {
	var entityIdAndType =  item.data;
	var arry = entityIdAndType.split("~");
	var wptceId = arry[0];
	var testCaseId=arry[2];
	var html = "";	
	var isExecuted=item.executionId;
	console.log("isExecuted"+isExecuted);
    
	html = '<li class="dd-item" data-id="'+wptceId+'" id= "'+wptceId+'">';
	if(isExecuted==1){
		 html += '<div title="'+item.tooltip+'" onclick="getTestCaseDetails('+testCaseId+','+wptceId+',1,'+isExecuted+');setActiveTestCase(this)" style="color:white"><span class="badge badge-success" title="Executed" style="float:right">&#9989</span>'+item.text+'</div>';
	}else{
    html += '<div title="'+item.tooltip+'"  onclick="getTestCaseDetails('+testCaseId+','+wptceId+',1,'+isExecuted+');setActiveTestCase(this)" style="color:white">'+item.text+'</div>';
	}

    if (item.Records) {

        html += '<ol class=dd-list>';
        $.each(item.Records, function (index, sub) {
            html += buildItem(sub);
        });
        html += '</ol>';

    }
    html += '</li>';
    return html;
}

function getTestCaseData(urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId){
	var testcase=$('#selectable');
	testcase.empty();
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId,
		dataType : 'json',
		success : function(data) {			
			var source=data;
			data = jQuery.parseJSON(source);
			if(data == null) {
				$("#btnPrevious, #btnNext").hide();
				if( selectedButtonValue == 0){
					document.getElementById("noDataMSg").innerHTML = " No jobs available";
					$("#noDataMSg").show();
					$("#noDataTC").hide();
				}
				else if( selectedButtonValue == 1){
					document.getElementById("noDataMSg").innerHTML = " No features available";
					$("#noDataMSg").show();
					$("#noDataTC").hide();
				}
				else if( selectedButtonValue == 2){
					document.getElementById("noDataMSg").innerHTML = " No testsuites available";
					$("#noDataMSg").show();
					$("#noDataTC").hide();
				}
				else if( selectedButtonValue == 3){
					document.getElementById("noDataMSg").innerHTML = " No testcases available";
					$("#noDataMSg").show();
					$("#noDataTC").hide();
				}
				else{
					$("#noDataTC").show();
					$("#noDataMSg").hide();
				}
			} else {
				$("#btnPrevious").hide();
				$("#noDataTC").hide();
			}
			
			var blogpost="";
			var listHTMLITEM="";
			blogpost = '<div id="nestable"><ul class="dd-list"></ul></div>';
			$.each(data, function(i,item){
				var entityIdAndType =  item.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[2];
	   			var title = item.text;
				
				wptceId=item.id;
				if(i==0){
					prevTestcaseId=item.testcaseId;
				}
				if(i==2){
					nextTestcaseId=item.testcaseId;
				}
				if (item.isExecuted == 1){
					blogpost = '<li title="'+item.tooltip+'" id="li_'+ item.testcaseId +'" class=" " style="margin-top:2px;"> <a href="javascript:getTestCaseDetails('+item.testcaseId+','+item.id+',1,'+item.isExecuted+');setActiveTestCase(this);"  style="color:white;word-wrap:break-word;">'+item.testcaseId+':'+item.runConfigurationName+'</a><span class="badge badge-success" title="Executed" style="float:right">&#9989</span></li>';
					$("#noDataMSg").hide();
				}else{					
					listHTMLITEM += buildItem(item);
					$("#noDataMSg").hide();
				}               
			});
			testcase.append(blogpost);				
			$('#nestable ul').html(listHTMLITEM);	
			$('#nestable').nestable();
		}
	});	
}

function getTestCaseDetails(testcaseId,wptcepId,mode,isExecuted){
	if(document.getElementById('executionTime').value != "" && isExecuted !="" && isExecuted != 1) {
	    urll='testcase.results.update?tcerId='+document.getElementById('tcerIdhidden').value+'&modifiedfField='+""+'&modifiedValue='+""+'&executionTime='+document.getElementById('executionTime').value;
	    $.post(urll,function(data) {
			$.unblockUI();
		});
	}
	var prodMode = document.getElementById("hdnProductMode").value;
	$("#hdnCurrentSelectedTestCaseID").val(wptcepId);
	if(prodMode == "Project") {
		$("#collapse_3 table tr:nth-child(3) td:nth-child(5)").hide();
		$("#collapse_3 table tr:nth-child(3) td:nth-child(6)").hide();
	}else {
		$("#collapse_3 table tr:nth-child(3) td:nth-child(5)").show();
		$("#collapse_3 table tr:nth-child(3) td:nth-child(6)").show();
	}
	var tcerId='';
	$("#tcView").fadeOut().css("display","block");
	if($("#selectable>li").length > 1)
		$("#btnNext").show();
	else
		$("#btnNext, #btnPrevious").hide();
	if(mode==1){
		mode="edit";
	}else{
		mode="view";
	}
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'workpackage.testcase.details?testcaseId='+testcaseId+"&wptcepId="+wptcepId+"&timeStamp="+timestamp+"&mode="+mode,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			$("#currCaseName, #currCaseId").text("");
			$("#productName").empty();
			$("#productVersionName").empty();
			$("#productBuildName").empty();
			$("#workPackageName").empty();
			$("#testcaseId").empty();
			$("#testcaseName").empty();
			$("#preconditions").empty();
			$("#expectedOutput").empty();
			$("#testcaseCode").empty();
			$("#description").empty();
			$("#testcasetype").empty();
			$("#testcasePriority").empty();
			$("#environment").empty();
			$("#plannedDate").empty();
			$("#actualDate").empty();
			$("#plannedShift").empty();
			$("#tcerIdhidden").empty();
			$("#executionStatus").empty();
			$("#execTimerVal").empty();
			$("#executionPriority").empty();
			$.each(result, function(i,item){ 
				$("#productName").append("<div style='font-size:small;'  >"+item.productName+"</div>");
				$("#productVersionName").append("<div style='font-size:small;'  >"+item.productVersionName+"</div>");
				$("#productBuildName").append("<div style='font-size:small;'  >"+item.productBuildName+"</div>");
				$("#workPackageName").append("<div style='font-size:small;'  >"+item.workPackageName+"</div>");
				$("#testcaseId").append("<div style='font-size:small;' >"+item.testcaseId +" / " +item.testcaseCode +"</div>");
				$("#testcaseName").append("<div style='font-size:small;'  >"+item.testcaseName+"</div>");
				$("#currCaseId").text(item.testcaseId);
				$("#currCaseName").text(item.testcaseName);
				$("#preconditions").append("<div style='font-size:small;'  >"+item.preconditions+"</div>");
				$("#expectedOutput").append("<div style='font-size:small;'  >"+item.expectedOutput+"</div>");
				$("#testcaseCode").append("<div style='font-size:small;'  >"+item.testcaseCode+"</div>");
				$("#description").append("<div style='font-size:small;'  >"+item.testcaseDescription+"</div>");
				$("#testcasetype").append("<div style='font-size:small;'  >"+item.testcaseType+"</div>");
				$("#testcasePriority").append("<div  style='font-size:small;' >"+item.testcasePriority+"</div>");
				$("#environment").append("<div style='font-size:small;'  >"+item.runConfigurationName+"</div>");
				$("#plannedDate").append("<div style='font-size:small;' >"+item.plannedExecutionDate+"</div>");
				$("#plannedShift").append("<div style='font-size:small;' >"+item.plannedShiftName+"</div>");
				$("#actualDate").append("<div  style='font-size:small;' >"+item.actualExecutionDate+"</div>");
				if(item.executionStatus==3)
					executionName="NotStarted";
				else if(item.executionStatus==1)
					executionName="Assigned";
				else if(item.executionStatus==2)
					executionName="Completed";
				$("#executionStatus").append("<div  style='font-size:small;' >"+executionName+"</div>");
				var inputId = item.id+item.testcaseId;
				var dataid = item.executionPriorityId;
				if(dataid == 5){
					$("#executionPriority").append('<input name="star'+inputId+'" disabled type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" checked="checked"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"   type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>');
				}else if(dataid == 4){
					$("#executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>');
				}else if(dataid == 3){
					$("#executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>');
				}else if(dataid == 2){
					$("#executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>');
				}else if(dataid == 1){
					$("#executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0" checked="checked"/>');
				}else {
					$("#executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0"/>');
				}
	
				if(document.getElementById('tcerIdhidden')!=null)
				document.getElementById('tcerIdhidden').value=item.testCaseExecutionResultId;
				tcerId=item.testCaseExecutionResultId;
				executionTime=item.executionTime;
				executionStatus=item.isExecuted;
				isapproved= item.isApproved;
				console.log("-Approved --"+item.isApproved);
				
				if(isapproved == 1){
					enableTestCaseExecutionContent(false);
					callAlert("Approved Cannot Be Modified");
				}
				
			});
			$('.hover-star').rating({ 
				  
			 }); 
			if(document.getElementById('tcerIdhidden')!=null)
				tcerId=document.getElementById('tcerIdhidden').value;
									
			if(tcerId==''){
				tcerId=0;	
			}
			if(mode=="edit"){
				loadTestStep(testcaseId,tcerId);
				loadDefectInfoBytcerId(tcerId);
			}else{
				loadTestStepView(testcaseId,tcerId);
				loadDefectInfoBytcerIdView(tcerId);
			}
			loadTestResults(data);
			loadEvidenceDetails();
			if(document.getElementById("paginationButton")!=null)
			document.getElementById("paginationButton").style.display = "block";

			if(executionStatus ==1){
				if(document.getElementById("timer")!=null)
				document.getElementById("timer").style.display = "none";
				if(document.getElementById("timer1")!=null)
				document.getElementById("timer1").style.display = "none";
				if(document.getElementById("timer2")!=null)
				document.getElementById("timer2").style.display = "none";
				if(document.getElementById("execTimer")!=null)
				document.getElementById("execTimer").style.display = "block";
				if(executionTime==null){
					if(document.getElementById('execTimerVal')!=null)
						document.getElementById('execTimerVal').value = "0 sec" ;
				}else{
					if(document.getElementById('execTimerVal')!=null) {
						var sec = parseInt(executionTime);
						var minutes = Math.floor(sec / 60); // 7
						var seconds = sec % 60;
						if(seconds < 10) seconds = "0" + seconds;
						document.getElementById('execTimerVal').value = minutes + ":" + seconds + " min";
					}
				}				
			}else{
				if(mode!="view"){
				document.getElementById("timer").style.display = "block";
				document.getElementById("timer1").style.display = "block";
				document.getElementById("timer2").style.display = "block";
				document.getElementById("execTimer").style.display = "none";
				document.getElementById('execTimerVal').value="";
				if(executionTime!=null && executionTime!=0){
					console.log(executionTime);
					$('.timer').timer('remove');
					$('.timer').timer({
					    seconds: executionTime 
					});
				}else {
					$('.timer').timer('reset');
				}
				
				var hasTimer = true;
				jQuery('#start').click();
				}
				
			}
			$("#tcView").fadeIn();			
		}
	});	
}

function enableTestCaseExecutionContent(value){
	 console.log("enable :"+value);	 
	 if(!value){		 	
			$('#testCaseExecutionTestStepTestResultID').append('<div id="lastElementResult" style="position: absolute;top:100px;left:0;width: 95%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)"></div>');
			$('#testCaseExecutionDefectDetailsID').append('<div id="lastElementDetails" style="position: absolute;top:100px;left:0;width: 95%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)"></div>');	 
	 }else{		
		var resultElement = document.getElementById("lastElementResult");
		var detailsElement = document.getElementById("lastElementDetails");		
		if (resultElement != null ) {
			resultElement.remove();
	    }
		
		if (detailsElement != null ) {
			detailsElement.remove();
	    }
	 }	
}

function loadTestStep(testcaseId,tcerId){
	try{
		if ($('#jTableContainerTS').length>0) {
			 $('#jTableContainerTS').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerTS').jtable({
       title: 'Test Case Steps',
       editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: 'teststep.plan.list?testCaseId='+testcaseId+"&tcerId="+tcerId,
       	 editinlineAction: 'teststep.plan.update'
        },  
        fields : {
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
        		title: 'TestStep Input', 
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
		        title:'',
		        width: "2%",
		        create:false,
		        edit:false,
		        display: function (childData) { 
	        				//Create an image that will be used to open child table 
				        	var $img = $('<img src="css/images/upload.gif" title="Evidence List" />'); 
				        	//Open child table when user clicks the image 
				        	$img.click(function (data) { 
				        		
			        		// ----- Closing child table on the same icon click -----
	           			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainerTS'));
	           				if(closeChildTableFlag){
	           					return;
	           				} 
				        		
				        	$('#jTableContainerTS').jtable('openChildTable', 
				        	$img.closest('tr'), 
				        	{ 
				        	title: 'Evidence List',
				        	actions: { 
				        		listAction: 'testcase.list.eveidence?tcerId='+childData.record.teststepexecutionresultid+'&type=teststep',
				        		createAction: function(postData){
									value=document.getElementById("uploadImageTS").files[0];
									 var executionTime=document.getElementById('executionTime').value;
										
									 if(executionTime==null)
										 executionTime="";
									 
									 var formdata = new FormData();
		        					 formdata.append("uploadImage", value);
		        					 formdata.append("description", document.getElementById('Edit-description').value);
		        					 formdata.append("evidenceType", document.getElementById('Edit-evidenceTypeId').value);
		        				     url='testcase.results.update.evidence?tcerId='+childData.record.teststepexecutionresultid+"&modifiedfField=image&modifiedValue="+value+"&type=teststep"+"&executionTime="+executionTime;
		        			 		 return $.Deferred(function($dfd){
		        					     $.ajax({
		        							    url:url,
		        							    method: 'POST',
		        							    contentType: false,
		        							    data: formdata,
		        							    dataType:'json',
		        							    processData: false,
		        							    success: function (data) {
		        							    	var json = data;
		        							    	var obj = json;
		        							    	obj.Record = obj.Records;
		        							    	delete obj.Records;
		        							    	json = obj;
			        							    $dfd.resolve(json);
		        	                                $('#jTableContainerTS').jtable('load');
		        	                            },
		        	                            error: function () {
		        	                                $dfd.reject();
		        	                            }
		        							});
		        		  });
		        		}
				          	}, 
				        	fields: { 
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
				        	evidenceTypeId:{
				         		title : 'Evidence Type',
				         		width : "10%",
				         		create : true,
				         		list : true,
				         		edit : true,
				       			options:function(data){
				       					return 'common.list.evidenceType';	
				       			}
				         	}, 
				        	UploadFile: {
			                        title: 'Upload File *',
			                        create:true,
			                        list:false,
			                        input: function (data) {
			                        	var $img=$('<input id="uploadImageTS" type="file" name="uploadImage" value="Upload Image"/>');
			                        
			                                //var $img = $('<img alt="" style="cursor: pointer;" src="css/images/uploadbutton.jpg" />');
			                                return $img;
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
				            
				           },
						   
							//Initialize validation logic when a form is created
							formCreated: function (event, data) {
								//data.form.find('input[name="uploadImage"]').addClass('validate[required]');
								data.form.find('input[name="uploadImage"]');
								data.form.validationEngine();
							},
							//Validate form when it is being submitted
							formSubmitting: function (event, data) {
								data.form.find('input[name="uploadImage"]').addClass('validate[required]');
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
				        		//Return image 
				        		return $img; 
				        	}, 
      	},
			
        },  // This is for closing $img.click(function (data) {  
	      
		});
	 $('#jTableContainerTS').jtable('load');
}

function loadTestStepView(testcaseId,tcerId){
	try{
		if ($('#jTableContainerTS').length>0) {
			 $('#jTableContainerTS').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerTS').jtable({
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10,
          actions: {
       	 	listAction: 'teststep.plan.list?testCaseId='+testcaseId+"&tcerId="+tcerId,
        },  
        fields : {
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
        		title: 'TestStep Input', 
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
                edit:false							                
            },
			result : {
				title : 'Result',
				width : "20%",
				edit : false,
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
                edit:false							                
            },
			evidenceDetails: {
		        title:'',
		        width: "2%",
		        create:false,
		        edit:false,
		        display: function (childData) { 
	        				//Create an image that will be used to open child table 
				        	var $img = $('<img src="css/images/upload.gif" title="Evidence List" />'); 
				        	//Open child table when user clicks the image 
				        	$img.click(function (data) { 
				        	$('#jTableContainerTS').jtable('openChildTable', 
				        	$img.closest('tr'), 
				        	{ 
				        	title: 'Evidence List',
				        	actions: { 
				        		listAction: 'testcase.list.eveidence?tcerId='+childData.record.teststepexecutionresultid+'&type=teststep',				        		
				          	}, 
				        	fields: { 
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
				        	UploadFile: {
			                        title: 'Upload File',
			                        create:true,
			                        list:false,
			                        input: function (data) {
			                        	var $img=$('<input id="uploadImageTS" type="file" name="uploadImage" value="Upload Image"/>');
			                                return $img;
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
			
        },  // This is for closing $img.click(function (data) {  
	      
		});
	 $('#jTableContainerTS').jtable('load');
}

	function loadDefectInfoBytcerId(tcerId){
			try{
				if ($('#jTableContainerDefectsTC').length>0) {
					 $('#jTableContainerDefectsTC').jtable('destroy'); 
				}
			} catch(e) {}
			$('#jTableContainerDefectsTC').jtable({
		       title: 'Defect Details',
		        editinline:{enable:true},
		        selecting: true,  //Enable selecting 
		        paging: true, //Enable paging
		        pageSize: 10, 
		          actions: {
		       	 	listAction: 'testcase.defect.list?tcerId='+tcerId,
		        	 createAction: 'testcase.defect.add?tcerId='+tcerId,	
		        	 editinlineAction: 'testcase.defect.update?tcerId='+tcerId
		        },  
		        fields : {
		        	bugTitle: { 
		        		title: 'Bug Title',  
		        		width: "20%",                          
		                create: true,
		                list:true,
		                edit:true
		        	},
		        	testCaseExecutionResultId: { 
			        		title: 'id',
			        		width: "7%",         
			                list: false	,
			                create:false,
			                edit:false
			            },
		        	bugDescription:{
		        		title: 'Description',  
		        		width: "30%",
		        		type: 'textarea',
		        		 create: true,
			                list:true,
			                edit:true  
		        	},
		        	runConfiguration:{
		        		title: 'TestBed',  
		        		width: "30%",                          
		        		 create: false,
			                list:true,
			                edit:false  
		        	},
		        	severityId:{		        		 
		        		title: 'Severity',
		        		width: "7%",         
		        		 create: true,
			                list:true,
			                edit:true,
			                options : 'common.list.defectSeverity'
		            
		        	},
		            bugFilingStatusId: { 
		        		title: 'Filing Status',
		        		width: "7%",         
		        		 create: true,
			                list:true,
			                edit:true,
			                options : 'administration.workFlow.list?entityType=1'
		            },
		            remarks: { 
		        		title: 'Remarks',
		        		width: "7%",         
		        		 create: true,
			                list:true,
			                edit:true							                
		            },
		            userId:{
		            	title: 'Raised By', 
		        		width: "10%",                         
		        		create:false,
			                list:true,
			                edit:false
		          
		            },
		            bugCreationTime: { 
		        		title: 'Creation Time',
		        		width: "7%",         
		        		 create: false,
			                list:true,
			                edit:false							                
		            } ,bugManagementSystemName: { 
		        		title: 'System Name', 
		        		width: "20%",                         
		        		 create: false,
			                list:false,
			                edit:false
		           },
		           bugManagementSystemBugId: { 
		        		title: 'System Bug Id',  
		        		width: "25%",                        
		        		 create: false,
			                list:false,
			                edit:false
		            },
		            fileBugInBugManagementSystem: { 
		        		title: 'File Bug',  
		        		width: "7%",         
		        		 create: false,
			                list:false,
			                edit:false
		            },
		            defectTypeId: { 
		        		title: 'Defect Type',  
		        		width: "7%",         
			            options:'common.list.defect.types.list'
		            },
		            defectIdentifiedInStageId: { 
		        		title: 'Found In',  
		        		width: "7%",      
		        		create: false,
		                list:true,
		                edit:false,
			            options:'common.list.defect.identification.stages.list'
		            },
		            testersPriorityId: { 
		        		title: 'Priority',  
		        		width: "20%",     
		        		create: true,
		        		list: true,
		        		edit: true,
			            options:'administration.executionPriorityList'
		            },
		        },  // This is for closing $img.click(function (data) {  
				});
			 $('#jTableContainerDefectsTC').jtable('load');
	}
		
	function loadDefectInfoBytcerIdView(tcerId){
		try{
			if ($('#jTableContainerDefectsTC').length>0) {
				 $('#jTableContainerDefectsTC').jtable('destroy'); 
			}
		} catch(e) {}
		$('#jTableContainerDefectsTC').jtable({
	       title: '',
	        editinline:{enable:true},
	        selecting: true,  //Enable selecting 
	        paging: true, //Enable paging
	        pageSize: 10, 
	          actions: {
	       	 	listAction: 'testcase.defect.list?tcerId='+tcerId,
	       	 	editinlineAction: 'defects.for.analyse.update',
	        },  
	        fields : {
	        	bugTitle: { 
	        		title: 'Bug Title',  
	        		width: "20%",                          
	                create: true,
	                list:true,
	                edit:true
	        	},
	        	testCaseExecutionResultId: { 
		        		title: 'id',
		        		width: "7%",         
		                list: false	,
		                create:false,
		                edit:false
		            },
	        	bugDescription:{
	        		title: 'Description',  
	        		width: "30%",                          
	        		 create: true,
		                list:true,
		                edit:true  
	        	},
	        	environments:{
	        		title: 'Environments',  
	        		width: "30%",                          
	        		 create: false,
		                list:true,
		                edit:false  
	        	},
	        	severityId:{		        		 
	        		title: 'Severity',
	        		width: "7%",         
	        		 create: true,
		                list:true,
		                edit:true,
		                options : 'common.list.defectSeverity'
	            
	        	},
	          
	            bugFilingStatusId: { 
	        		title: 'Filing Status',
	        		width: "7%",         
	        		 create: true,
		                list:true,
		                edit:true,
		                options : 'administration.workFlow.list?entityType=1'
	            },
	            remarks: { 
	        		title: 'Remarks',
	        		width: "7%",         
	        		 create: true,
		                list:true,
		                edit:true							                
	            },
	            bugCreationTime: { 
	        		title: 'Creation Time',
	        		width: "7%",         
	        		 create: false,
		                list:true,
		                edit:false							                
	            } ,bugManagementSystemName: { 
	        		title: 'System Name', 
	        		width: "20%",                         
	        		 create: true,
		                list:true,
		                edit:false
	           },
	           bugManagementSystemBugId: { 
	        		title: 'System Bug Id',  
	        		width: "25%",                        
	        		 create: true,
		                list:true,
		                edit:false
	            },
	            fileBugInBugManagementSystem: { 
	        		title: 'File Bug',  
	        		width: "7%",         
	        		 create: true,
		                list:true,
		                edit:false
	            },reportedInBuildId: { 
	        		title: 'Reported Build',  
	        		width: "15%",  
	        		edit:false,
	        		list:true,
		            options:'process.list.builds.by.product?productId='+productId
	            },	            
	            fixedInBuildId: { 
	        		title: 'Fixed Build',  
	        		width: "15%",  
	        		edit:true,
	        		list:true,
	        		  options:'process.list.builds.by.product?productId='+productId
	            },
	            sourceFilesModifiedForFixing: { 
	        		title: 'Source File Modified',  
	        		width: "10%",  
	        		edit:true,
	        		list:true,
	            },	           
	        },  // This is for closing $img.click(function (data) {  
		      
			});
		 $('#jTableContainerDefectsTC').jtable('load');
}
var isLoad = false;			
var exceptedOutput='';
	function loadTestResults(data){		
		var result=data.Records;
		$("#preconditions").empty();
		$("#expectedOutput").empty();
		$("#testcaseInput").empty();
		$("#observedOutput").empty();
		$("#comments").empty();
		var selectedShift="";		
		$('#actualshift_options').empty(); 
		$.each(result, function(i,item){ 
			if(item.preconditions==null)
				$("#preconditions").append("<div style='font-size:small;'  > NA </div>");
			else
				$("#preconditions").append("<div style='font-size:small;'  >"+item.preconditions+"</div>");
			if(item.expectedOutput==null){
				$("#expectedOutput").append("<div style='font-size:small;' >NA</div>");
				}
			else{
				exceptedOutput=item.expectedOutput;
				$("#expectedOutput").append("<div style='font-size:small;' >"+item.expectedOutput+"</div>");
			}
			if(item.testcaseInput==null)
				$("#testcaseInput").append("<div style='font-size:small;'  >NA</div>");
			else
				$("#testcaseInput").append("<div style='font-size:small;'  >"+item.testcaseInput+"</div>");

			if(item.observedOutput==null){
				if(mode=="view"){
					$("#observedOutput").val("").attr("disabled",true);
				}else{
					$("#observedOutput").val("");
				}
			}
			else{
				if(mode=="view"){
					$("#observedOutput").val(item.observedOutput).attr("disabled",true);
				}else{
					$("#observedOutput").val(item.observedOutput);
				}
			}
			if(item.comments==null){
				if(mode=="view"){			
					$("#comments").val("").attr("disabled",true);
				}else{
					$("#comments").val(item.comments);
				}
			}
			else{
				if(mode=="view"){	
					$("#comments").val(item.comments).attr("disabled",true);
				}else{
					$("#comments").val(item.comments);
				}
			}
			
			if(mode!="view"){
				if(item.isExecuted==null || item.isExecuted==""){
					if($(".make-switch").bootstrapSwitch('state'))
						$(".make-switch").bootstrapSwitch('toggleState');
				}
				else{
					if($(".make-switch").bootstrapSwitch('state') == false)
						$(".make-switch").bootstrapSwitch('toggleState');
				}
			}
			if(item.result=="PASSED"){
				$("#pass").addClass("active").siblings("label").removeClass("active");
			}else if(item.result=="FAILED"){
				$("#fail").addClass("active").siblings("label").removeClass("active");
			}else if(item.result=="NORUN"){
				$("#norun").addClass("active").siblings("label").removeClass("active");
			} else if(item.result=="BLOCKED"){
				$("#blocked").addClass("active").siblings("label").removeClass("active");
			}
			if((item.isExecuted == 0) || (item.isExecuted == 0)) {
				$("#blocked").removeClass("active").siblings("label").removeClass("active");
			}
			if(item.actualExecutionDate == null){
				isLoad = true;
				$("#datepickerAC").datepicker('setDate','today');
				currActExecDate = $("#datepickerAC").val();
			}else{
				isLoad = true;
				currActExecDate = setFormattedDate(item.actualExecutionDate);
				$("#datepickerAC").datepicker('setDate', setFormattedDate(item.actualExecutionDate));
			}
			if(item.actualShiftId==null){
				selectedShift=-1;
			}else{
				selectedShift=item.actualShiftId
			}
		});
		isLoad = false;
		if(mode!="view"){
			loadActualShift(selectedShift);
		}else{
			if(selectedShift!=-1){
				$('select[name="actualshift_options"] option[value="'+selectedShift+'"]').attr("selected","selected");
	       	}
		}		
	}
	
	function loadActualShift(selectedShift){
		var workPackageId = $("#workpackageList_ul").find('option:selected').attr('id');
		
		if(workPackageId==null || workPackageId==undefined || workPackageId=='')
			workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
		
		if(workPackageId==null || workPackageId==undefined || workPackageId=='')
			{
			callAlert("Please select Workpackage ");
				return false;
			}
		var url='common.list.actualShift?workpackageId='+workPackageId+'&actualExecutionDate='+datepickerAC.value;
		var $select = $('#actualshift_options');
		$.post(url,function(data) {
	        var ary = (data.Options);	        
	        $('#actualshift_options').empty(); 
	        if(ary.length==0){
	        	$select.append('<option value=-1>No Shift</option>');
	        }else{
	        	$select.append('<option value=-1>Select Actual Shift</option>');
	        }
	        $.each(ary,function(index,element) 
			{
				 $select.append('<option value=' + element.Number + '>' + element.DisplayText + '</option>');
			});
			if(selectedShift!=-1){
				$('select[name="actualshift_options"] option[value="'+selectedShift+'"]').attr("selected","selected");
	       	}			
			$('#actualshift_options').select2();			
		});
	}
	
function loadWorkpackageList(){
	$('#selectable').empty();
	$.post('common.list.user.workpackages.list?userId=0&plannedExecutionDate='+datepicker.value,function(data) {		
		$("#workpackageList_ul").empty(); 
		if(data.Result=="ERROR"){
			$("#workpackageList_dd span.select2-chosen").empty();
			$("#noDataTC").show();
			$("#noDataMSg").hide();
    		return false;
    	}else{
			$("#noDataTC").hide();
			var ary = (data.Options);
			console.log("array--->"+ary);
			 $.each(ary, function(index, element) {
 		        	$('#workpackageList_ul').append('<option id="' + element.Value + '" title ="' + element.DisplayText + '"><a href="#">' + element.DisplayText + '</a></option>');
	        });
			$('#workpackageList_ul').select2();
		 	workPackageId = $("#workpackageList_ul").find('option:selected').attr('id');
		 	$('#workpackageList_dd .select2-container').attr("title" , $("#workpackageList_ul").find('option:selected').attr('title'));
		 	workPackageId = (typeof workPackageId == 'undefined') ? -1 : workPackageId;
		    if(workPackageId==null || workPackageId=='undefined' || workPackageId==-1){
		    	workPackageId=-1;
		    	$('#selectable').empty();
		    	$("#tcView").hide();
		    	callAlert("Please select workpackage");
		    	return false;
		    }
		    getProductMode(workPackageId);
		    
		    var testCaseSelectionTab= $("#testCaseFilter").find("label.active").index();
		    urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.execution.tester?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&nodeType="+nodeType+"&plannedExecutionDate="+datepicker.value+"&filter="+testCaseSelectionTab;
			getTestCaseData(urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId); 
	    	return true;
    	}
 	});	
} 
	
		function loadScrollbarForDropDown(){
			$('.dropdown').jScrollPane({showArrows: false,
			        					scrollbarWidth: 5,
			        					arrowSize: 5});
		}

   	 $("#datepicker").datepicker('setDate','today');
   	 
$(document).ready(function(){
		createHiddenFieldsForTree();
		
		if(document.getElementById("tcView")!=null)
			document.getElementById("tcView").style.display = "none";
		
		if(document.getElementById("timer")!=null)
			document.getElementById("timer").style.display = "none";
		
		if(document.getElementById("execTimer")!=null)
			document.getElementById("execTimer").style.display = "none";
		
		if(document.getElementById("paginationButton")!=null)
			document.getElementById("paginationButton").style.display = "none";
		 
		$("#start, #resume, #remove, #pause").hide();
		/*$.sessionTimeout({
			 warnAfter: 1800000, 
		     redirAfter: 1810000
		});*/
		
		if(mode=="edit"){
			loadWorkpackageList();
		}		
		
		 $('#selectable').on('click', 'li', function() {
			 $('#selectable >li').removeClass('ui-selected');
		     $(this).addClass('ui-selected');
		 });	     
		
		 var executionTime='';
		 if(document.getElementById('executionTime')!=null)
			 executionTime= document.getElementById('executionTime').value;
			
		 if(executionTime==null)
			 executionTime="";
		 
		 url='testcase.results.update.evidence?tcerId=&modifiedfField=image&modifiedValue=&type=testcase'+"&executionTime="+executionTime;
		 if(mode!="view"){
		 $("#fileuploader").uploadFile({
				url:url,
				fileName:"myfile",
				multiple:true,
				dynamicFormData: function()
				{
					var id="";
					if(document.getElementById('tcerIdhidden')!=null){
						id=document.getElementById('tcerIdhidden').value;
					}
					var data ={ 'tcerId':id}
					return data;
				},
			 afterUploadAll:function()
			 {
				 loadEvidenceDetails();
			 	
			 }
			});
		
		 }

		 var hasTimer = false;
			// Init timer start
		// $("#start, #resume, #remove, #pause").hide();
});

function setActiveValue(val) {
	activeResultVal = val;
}

function saveTestCaseResult(modifiedField){ 
	var productMode = document.getElementById("hdnProductMode").value;
	var value="";
	if(document.getElementById('tcerIdhidden')!=null)
		tcerId=document.getElementById('tcerIdhidden').value;
	 var thediv = document.getElementById('reportbox');
	 var url="";
	 var executionTime=document.getElementById('executionTime').value;
	
	 if(executionTime==null)
		 executionTime="";
	 
	 if(modifiedField!='image'){
		 if(modifiedField=='result' ){
			    value=activeResultVal;//$.trim($(".radio-toolbar>.btn-group>label.active").text());
				console.log(value);
				$('.timer').timer('pause');
				$("#pause").hide();
				$("#remove").hide();
				$("#resume").hide();
				$("#start").hide();
				
				if(value=='Pass'){
					if(exceptedOutput=='')
						document.getElementById('observedOutput').value="Passed";
					else
						document.getElementById('observedOutput').value=exceptedOutput;
				}else if(value=='Fail'){
					document.getElementById('observedOutput').value="Failed";
				}else if(value=='No Run'){
					document.getElementById('observedOutput').value="No Run";
				}else if(value=='Blocked'){
					document.getElementById('observedOutput').value="Blocked";
				}
				//$('.ToggleSwitch').toggleCheckedState(true);
				if($(".make-switch").bootstrapSwitch('state') == false)
					$(".make-switch").bootstrapSwitch('toggleState');
				if($("#" + $("#hdnCurrentSelectedTestCaseID").val()).find('span.badge').length == 0)
					$("#" + $("#hdnCurrentSelectedTestCaseID").val()+" div").append('<span class="badge badge-success" title="Executed" style="float:right">&#9989</span>');
		 
		 }else if(modifiedField=='actualDate'){
				value=datepickerAC.value;
				currActExecDate = value;
		 
		 }else if(modifiedField=='actualshift'){
				value= $('#actualshift_options').val();
				if(value==-1){
					callAlert("Please select Actual Shift");
					return false;
				}		 
		 }else if(modifiedField=='executionStatus'){
			 var copyDataType = "";
			 $(':checkbox:checked').each(function(i){
			    	copyDataType = $(this).val();
			    });
			 
				 if(copyDataType=="on"){
					 value="1";
					 if(document.getElementById("timer")!=null)
							document.getElementById("timer").style.display = "none";
					 if(document.getElementById("timer1")!=null)
							document.getElementById("timer1").style.display = "none";
					 if(document.getElementById("timer2")!=null)
							document.getElementById("timer2").style.display = "none";
					 if(document.getElementById("execTimer")!=null)
							document.getElementById("execTimer").style.display = "block";
					 if(executionTime==null){
						if(document.getElementById('execTimerVal')!=null)
							document.getElementById('execTimerVal').value = "0 sec" ;
					 }else{
						if(document.getElementById('execTimerVal')!=null)
							document.getElementById('execTimerVal').value = executionTime ;
					 }				 	
				 }else{
					 value="0";
					 document.getElementById("timer").style.display = "block";
					 document.getElementById("timer1").style.display = "block";
					 document.getElementById("timer2").style.display = "block";
					 document.getElementById("execTimer").style.display = "none";
					 document.getElementById('execTimerVal').value="";
						
						if(executionTime!=null && executionTime!=0 && executionTime!="" && executionTime!="null"){
							$('.timer').timer('reset');
							$('.timer').timer({
							    seconds: executionTime 
							});
							jQuery('#start').click();
						}else{
							$('.timer').timer('reset');
							
							jQuery('#start').click();
						}
					 //from old end
						$("#" + $("#hdnCurrentSelectedTestCaseID").val()).find('span.badge').remove();
				 }
		 	}
		 
		    url='testcase.results.update?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&executionTime="+executionTime;
			if (thediv.style.display == "none") {
				 $.post(url,function(data) {
					 $.unblockUI();
					});
				} else {
					thediv.style.display = "none";
					thediv.innerHTML = '';
				}
			 if(modifiedField=='actualDate'){
				 loadActualShift(-1);      
			 }
	 }else if(modifiedField=='image'){
		value=document.getElementById("uploadImage").files[0];	
		 var formdata = new FormData();
		 formdata.append("uploadImage", value);		 
	     url='testcase.results.update.evidence?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&type=testcase"+"&executionTime="+executionTime;
	     if (thediv.style.display == "none") {
		     $.ajax({
				    url:url,
				    method: 'POST',
				    contentType: false,
				    data: formdata,
				    dataType:'json',
				    processData: false,
				    success : function(data) {
				    	if(data.Result=="ERROR"){
				    		callAlert(data.Message);
				    		return false;
				    	}
				    	 loadEvidenceDetails();
				    	 $.unblockUI();
				    },
				});
	     } else {
				thediv.style.display = "none";
				thediv.innerHTML = '';
			}	    
	}else{
		return false;
	}
}

function loadEvidenceDetails(){
	if(document.getElementById('tcerIdhidden')!=null)
		tcerId=document.getElementById('tcerIdhidden').value;
	else
		tcerId=0;
	var evidenceURL="testcase.list.eveidence?tcerId="+tcerId+"&type=testcase";
	var root="";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : evidenceURL,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			$("#evidenceUplaoded").empty();			
			$.each(result, function(i,item){ 
				root=item.fileuri;
				$("#evidenceUplaoded").append("<div style=display:none; id=evidenceFilename"+i+"></div>");
				document.getElementById("evidenceFilename"+i).innerHTML = root;
			/*	$("#evidenceUplaoded").append("<div style=font-size:small;> <a href=javascript:loadPopupEvidence('evidenceFilename"+i+"');>"+item.evidencename+"</a></div>"); */
				$("#evidenceUplaoded").append("<div style=font-size:small;><a href=javascript:loadPopupEvidence('evidenceFilename"+i+"');>"+item.evidencename+"</a><span onclick='javascript:deleteEvidence("+item.evidenceid+")'; style='padding-left: 5px;padding-top: 5px;'><i class='fa fa-close showHandCursor'  title='Delete Evidence'></i></span></div>");
			});
		}		
	});
}

function loadPopupEvidence(urlId){
    var url=document.getElementById(urlId).innerHTML;
	var urlfinal="rest/download/evidence?fileName="+url;
  	parent.window.location.href=urlfinal;
 }
	
function deleteEvidence(evidenceId){
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=utf-8",
		url : 'administration.evidence.delete?evidenceId='+evidenceId,
		dataType : 'json',
		success : function(data) {
			loadEvidenceDetails();
		}
	});
}

function callCalender(type){
	 if(type=='actual'){
	 	$( "#actualExecDate" ).datepicker();
	 }
}
$("#workpackageList_ul").on( "select2-close", function(){
	$('#workpackageList_dd .select2-container').attr("title" , $("#workpackageList_ul").find('option:selected').attr('title'));
	$("#workpackageList_ul").find('option:selected').attr('title').hide();
	workpackageListId= $("#workpackageList_ul").select2("data").element[0].id;
	 nodeType="WorkPackage";
	 workPackageId = (typeof workpackageListId == 'undefined') ? -1 : workpackageListId;
	 if(workPackageId==null || workPackageId=='undefined' || workPackageId==-1){
		 workPackageId=-1;
	    	return false;
	    }
	 getProductMode(workPackageId);
	 
	 var testCaseSelectionTab= $("#testCaseFilter").find("label.active").index();
	 urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.execution.tester?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&nodeType="+nodeType+"&plannedExecutionDate="+datepicker.value+"&filter="+testCaseSelectionTab;	 
	 getTestCaseData(urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId);
});

function getProductMode(workPackageId){
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'workpackage.product.mode?wpId='+workPackageId,
		dataType : 'json',
		success : function(data) {
			document.getElementById("hdnProductMode").value = data;
		}
	});
}

$(document).on("changeDate", "#datepicker", function() {
 	$(".datepicker").hide();
 	loadWorkpackageList();
});

var currActExecDate = "";
$(document).on('changeDate','#datepickerAC', function(){
	if(!isLoad) {
		if (new Date($("#datepicker").val()) > new Date($("#datepickerAC").val()) ){
			callAlert("Actual Execution Date should not be lesser than Planned Execution Date");
			$("#datepickerAC").datepicker('setDate', currActExecDate);
			return false;
		}
		saveTestCaseResult('actualDate');
	}
});

$(document).on('click','.start-timer-btn', function() {
	hasTimer = true;
	$('.timer').timer({
		editable: true
	});
	$("#start").hide();
	$("#pause").show();
	$("#remove").show();
	$("#resume").hide();
});

// Init timer resume	
$('.resume-timer-btn').on('click', function() {
	$('.timer').timer('resume');
	//$(this).addClass('hidden');
	$("#pause").show();
	$("#remove").show();
	$("#resume").hide();
	$("#start").hide();
});

// Init timer pause
$('.pause-timer-btn').on('click', function() {
	$('.timer').timer('pause');
	//$(this).addClass('hidden');
	$("#resume").show();
	$("#pause").hide();
	$("#remove").show();
	$("#start").hide();
});

// Remove timer
$('.remove-timer-btn').on('click', function() {
	hasTimer = false;
	$('.timer').timer('remove');
	//$(this).addClass('hidden');
	$("#start").show();
	$("#pause").hide();
	$("#remove").hide();
	$("#resume").hide();
	
});

// Additional focus event for this demo
$('.timer').on('focus', function() {
	if(hasTimer) {
		
		$("#resume").show();
		$("#pause").hide();
	}
});

// Additional blur event for this demo
$('.timer').on('blur', function() {
	if(hasTimer) {
		$("#resume").hide();
		$("#pause").show();	
	
	}
});

//acknowledgement message
var message_status = $("#status");
//$("td[contenteditable=true]").on("blur", function(){

$("textarea").on("change", function(){	
    var modifiedField = $(this).attr("id") ;    
    var value = $(this).val() ;
    if(document.getElementById('tcerIdhidden')!=null)
    tcerId=document.getElementById('tcerIdhidden').value;
    var executionTime='';
    if(document.getElementById('executionTime')!=null)
    	executionTime=document.getElementById('executionTime').value;
	
	 if(executionTime==null)
		 executionTime="";	 
    
    var url='testcase.results.update?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&executionTime="+executionTime;
    var thediv = document.getElementById('reportbox');
    if (thediv.style.display == "none") {
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

$("textarea").on("blur", function(){	
    var modifiedField = $(this).attr("id") ;    
    var value = $(this).val() ;
    if(document.getElementById('tcerIdhidden')!=null)
    tcerId=document.getElementById('tcerIdhidden').value;
    var executionTime='';
    if(document.getElementById('executionTime')!=null)
    	executionTime=document.getElementById('executionTime').value;
	
	 if(executionTime==null)
		 executionTime="";	 
    
    var url='testcase.results.update?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&executionTime="+executionTime;
    var thediv = document.getElementById('reportbox');
    if (thediv.style.display == "none") {
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

function handlePageNavigation(obj) {
	var testCaseCount = $("#selectable>li").length;
	if(obj == 1) {
		if(testCaseCount == 1) {
			$("#btnPrevious").hide();
		}		
		var actInd = $("#selectable").find("li.active").index();
		if(actInd == 1) {
			$("#btnPrevious").hide();
		}		
		$("#selectable").find("li.active").prev("li").children("a").click();
	}else {
		$("#btnPrevious").show();
		var actInd = $("#selectable").find("li.active").index() + 1;
		if(testCaseCount == actInd) {
			$("#btnNext").hide();
		}		
		$("#selectable").find("li.active").next("li").children("a").click();
	}
}

function setActiveTestCase(obj) {
	$(obj).closest("li").addClass("active");	
	$(obj).closest("li").siblings("li").removeClass("active");	
}
$("#selectable").css({"border-top": "1px solid lightsteelblue", "padding-top" : "5px"});
