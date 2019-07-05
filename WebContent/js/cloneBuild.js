var cloneId;
function cloneBuild(id){
	cloneId = id;
	$("#divPopUpCloneBuild h4").text("");
	$("#divPopUpCloneBuild h4").text("Clone Build");
	$("#divPopUpCloneBuild").modal().css('z-index','10051');
	$('.modal-backdrop.in').eq(2).css('z-index','10050');
	$("#datePickerBuildDate").datepicker('setDate','today');
	$('#build_no').val("");
	$('#build_name').val("");
	$('#build_des').val("");
	$.post('common.list.defect.identification.stages.list',function(data) {	
		$('#buildType_ul').html('');
        var ary = (data.Options);
        var div_data='';
        $(div_data).html('');
        $.each(ary,function(i,data){        	
			div_data="<option value="+data.Value+">"+data.DisplayText+"</option>";			
        	$(div_data).appendTo('#buildType_ul');         	
       });	   
	   
	   $('select[name="buildType_ul"] option[value="Release"]').attr("selected","selected");
	   $('#buildType_ul').select2(); 
 	});	
		
}

function popUpCloneBuildclose() {
	$('.modal-backdrop.in').eq(2).css('z-index','10049');
	$("#divPopUpCloneBuild").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function saveCloneBuild(){	  
	  var buildTypeId =  $("#buildType_ul").find('option:selected').val();
	  var buildNo = $("#build_no").val();	  
	  var buildName = $("#build_name").val();	  
	  var buildDes = $("#build_des").val();	  
	  var buildDate= document.getElementById("datePickerBuildDate").value;
	  	
	  $('.clone-build-error').hide();
	  
	  if(buildNo== "" || buildName== ""){
		  
			 if(buildNo== ""){
				 
				 $("#build_no_err").show();
			 }
			 
			 if(buildName== ""){
				 
				  $("#build_name_err").show();
			 }
			
		  
	  }else{
		  $("#divPopUpCloneBuild").modal('hide');
			popUpCloneBuildclose();  
		 
		   openLoaderIcon();
		var urltoCloneProductBuild = 'product.clone.build?sourceBuildId='+cloneId+'&buildNo='+buildNo+'&buildName='+buildName+'&buildDes='+buildDes+'&buildTypeId='+buildTypeId+'&buildDate='+buildDate;
		$.ajax({
	    url: urltoCloneProductBuild,
	    method: 'POST',
	    contentType: "application/json; charset=utf-8",
	    dataType : 'json',
	    complete : function(data){
			if(data != undefined){			
				closeLoaderIcon();
			}
			$('#jTableContainerproducts').find('.jtable-child-table-container:eq(1)').jtable('reload');
		},
		success : function(data) {
			closeLoaderIcon();
			if(data.Result == 'OK'){
				$("#divPopUpCloneBuild").modal('hide');
				callAlert("Build Cloned Successfully");
				//$("[data-dismiss='modal']").on("click",function(){   $("#divPopUpCloneBuild").modal('hide');});
			}else if(data.Result == 'ERROR'){
				$("#divPopUpCloneBuild").modal('hide');
				closeLoaderIcon();
				callAlert(data.Message);
				//$("[data-dismiss='modal']").on("click",function(){ $("#divPopUpCloneBuild").modal();});
			}
			else{
				$("#divPopUpCloneBuild").modal('hide');
				callAlert("Build Cloning Failed !!!");
				//$("[data-dismiss='modal']").on("click",function(){ $("#divPopUpCloneBuild").modal();});	
			}
		},
		error: function (data){
			closeLoaderIcon();
		}
	});
		  
	  }
	 
	   
}

/*function popUpCloneBuildclose(){
	$("#divPopUpCloneBuild").fadeOut("normal");
}*/
