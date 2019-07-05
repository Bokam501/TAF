	<div class="portlet-body form">
		<div id="hidden"></div>
		<div class="row">
			<div class="col-md-4">											
				<div class=" btn-group" data-toggle="buttons" style="width:100%">
					<label class="btn darkblue" data-toggle="modal" onclick="mapMobiles();">
					<input type="radio" class="toggle">Map Mobile</label>
					<label class="btn darkblue" data-toggle="modal" onclick="mapServer();">
					<input type="radio" class="toggle" >Map Server</label>
				</div>
			</div>	
			<div class="col-md-4">
				<div class="col-md-3" style="padding-right:0px;margin-top:5px;"><label>Status :</label></div>
				<div id="devstatus" class="radio-toolbar col-md-9" style="padding-left:0px">	
					<div class=" btn-group" data-toggle="buttons" style="width:100%">
						<label class="btn darkblue active"  data-status="-1" onclick="filterAllDeviceTiles(this)">
						<input type="radio" class="toggle"  >All</label>
						<label class="btn darkblue" data-status="1" onclick="filterAllDeviceTiles(this)">
						<input type="radio" class="toggle"  >Active</label>
						<label class="btn darkblue"  data-status="0" onclick="filterAllDeviceTiles(this)">
						<input type="radio" class="toggle"  >Inactive</label>
					</div>
				</div>
			</div>
			<div class="col-md-4 " >
				<div class="col-md-3" style="padding-right:0px;margin-top:5px;"><label>Type :</label></div>
				<div id="devtype" class="radio-toolbar col-md-9" style="padding-left:0px">	
					<div class=" btn-group" data-toggle="buttons" style="width:100%">
						<label class="btn darkblue active" data-type="1" onclick="filterAllDeviceTiles(this);">
						<input type="radio" class="toggle"  >Mobile</label>
						<label class="btn darkblue" data-type="2" onclick="filterAllDeviceTiles(this);">
						<input type="radio" class="toggle"  >Server</label>
					</div>
				</div>
			</div>
		</div>
		
		<div id="tileContent" style="margin:10px; border-style: double; padding: 20px;"></div>		
		<div id="clearDataMessage" style="display:none;text-align:center"><label class="control-label" style="color:grey">No Mobile or Servers are available for this product </label></div>
	</div>


<div id="div_PopupMobile" class="modal" tabindex="-1" style="display:none;"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupMobileClose()"></button>
					<h4 id="headertagMobile" class="modal-title">Map Target Mobiles</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test1"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Mobile<span class='badge badge-default' id='listCount_allmobiles' style='float:right;background:#a294bb'>0</span></h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchmobile" placeholder="Type to search"></div>
									</div>
									<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
										<img src="css/images/ajax-loader.gif"/>
									</div>
									<ul id="allmobiles" class="block__list block__list_tags" style="padding-left: 0px; height: 300px; width:262px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Mobile</li>
									</ul>
								</div>
								
								</div> 
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Mapped Mobile<span class='badge badge-default' id='listCount_mobilesmapped' style='float:right;background:#076'>0</span></h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchmap_mob" placeholder="Type to search"></div>
									</div>
									<ul id="mobilesmapped" class="block__list block__list_tags" style="padding-left: 0px; height: 300px; width: 262px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Mobile</li>
									</ul>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<div id="div_PopupServer" class="modal" tabindex="-1" style="display:none;"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupServerClose()"></button>
					<h4 id="headertag" class="modal-title">Map Target Server</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test1"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">									
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Server <span class='badge badge-default' id='listCount_allservers' style='float:right;background:#a294bb'>0</span></h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_server" placeholder="Type to search"></div>
									</div>
									<div id="dragDropListItemLoaderIconServer" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
										<img src="css/images/ajax-loader.gif"/>
									</div>
									<ul id="allservers" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; width:262px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Server</li>
									</ul>
								</div>
							</div> 
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Mapped Server<span class='badge badge-default' id='listCount_serversmapped' style='float:right;background:#076'>0</span></h5>
									</div>
									
										<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_servermap" placeholder="Type to search"></div>
									</div>
									<ul id="serversmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; width: 262px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Server</li>
									</ul>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>						
			
<script type="text/javascript">
var flag=1;
var SOURCE;
var treeData;
var isEdited = false;
 
function devicesDetails(){
	if(nodeType == "Product"){
		productId = key;
		$('#Devices').children().show();
	 	
		var statusid = $("#devstatus").find("label.active").attr("data-status");	
	 	var typeid=$("#devtype").find("label.active").attr("data-type");	 	
	 	showAllDeviceTiles(typeid, statusid);
	 	
	}else{
		callAlert("Please select Product");
		return false;
	}	    	
}

function showAllDeviceTiles(typeId, statusId){
	
	if(typeId==1){
  		flag=1;
  	 	urlToGetDevicesOfSpecifiedProductId= 'admin.genericDevices.list.product?productId='+productId+'&filter='+statusId; 
	 	devicesNew(urlToGetDevicesOfSpecifiedProductId);
	
	}else if(typeId==2){
  		flag=2;
		urlToGetHost='administration.host.list.product?productId='+productId+'&filter='+statusId;
  		hostNew(urlToGetHost);
	}
}

function devicesNew(url){
	openLoaderIcon();
	
	var response;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			response = data.Records;
			listTilesDevice(response);
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}

function hostNew(url){
	openLoaderIcon();
	
	var response;	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			response = data.Records;
			listTilesDevice(response);
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}

function listTilesDevice(data){	
	if(data.length==0){
		
		if(flag==1){
			$("#clearDataMessage label").text("No mobile is available for this product.");
		}else{
			$("#clearDataMessage label").text("No server is available for this product.");
		}		
		$("#clearDataMessage").show();
		
	}else{
		$("#clearDataMessage").hide();
	}
	
	$('#tileContent').empty();
	
	var devType= $("#devtype").find('label.active').attr("data-type");
	if(devType == 1)
		var imgSrc = "css/images/mobile.png";
	else
		var imgSrc = "css/images/server.png";
	
	$.each(data,function(ind, ele){
		var availability = "bg-green"; 
		var hostName = "NA";
		
		if(flag==1){			
			if(ele.hostName != null){
				hostName = ele.hostName;
			}
			
			if(ele.availableStatus==0){
				availability = "bg-red-sunglo"
			};
			
			var ele_name=ele.name;
			
			if(ele.name.length>18){
				
				ele_name=ele.name.substr(0,15)+"...";
			}
			
			var ele_platformTypeName=ele.platformTypeName;
			
			if(ele.platformTypeName != null && ele.platformTypeName.length>18){
				ele_platformTypeName=ele.platformTypeName.substr(0,15)+"...";
			}else{
				ele.platformTypeName = "";
			}
			
			var ele_UDID=ele.UDID;
			
			if(ele_UDID.length>18){
				ele_UDID=ele.UDID.substr(0,15)+"...";
				
			}
			
		    var brick = '<div class="'+"tile "+ availability + '"><div class="tile-body" style="padding:3px 3px;">' +
		    '<div class="row"><img width="30%" height="30px" src="'+ imgSrc +'" ></img></div>'+
		    '<div class="row"><label class="deviceTileWrap" title="'+ ele.name+'" style="font-size: 12px;margin-bottom: 0px;">'+ ele_name+'</label><span style="font-size:11px; title="'+ele.platformTypeName+'" class="deviceTileWrap">'+ele_platformTypeName+'</span></div></div>'+
		    '<div class="tile-object" ><span title="'+ele.UDID+'" class="deviceTileWrap">' + ele_UDID +' <br /> host :</span><span style="font-size:11px;" class="deviceTileWrap">'+hostName+'</span></div>'+			
		    '</div>';
		    
		}else if(flag ==2){
			
			if(ele.hostStatus=='INACTIVE'){
				availability = "bg-red-sunglo"
			};
			
		    var brick = '<div class="'+"tile "+ availability + '"><div class="tile-body" style="padding:3px 3px;">' +
		    '<div class="row"><img width="30%" height="30px" src="'+ imgSrc +'"></img></div>'+
		    '<div class="row"><label class="deviceTileWrap" style="font-size: 12px;margin-bottom: 0px;">'+ ele.hostName+'</label><span style="font-size:11px;" class="deviceTileWrap">'+ele.hostPlatform+'</span></div></div>'+
		    '<div class="tile-object" ><span class="deviceTileWrap">' + ele.hostIpAddress +'</span></div>'
		    '</div>';
		}
		$('#tileContent').append($(brick));
	});
	
	if(!($('#tileContent').hasClass('tiles'))){
		$('#tileContent').addClass('tiles').css("margin-top","10px");
	}
}

function filterAllDeviceTiles(obj){
	statusId = $(obj).attr("data-status");
	typeId = $(obj).attr("data-type");	
	
	if (statusId == undefined)
	    statusId= $("#devstatus").find('label.active').attr("data-status");
	
	if (typeId == undefined)
	  	typeId= $("#devtype").find('label.active').attr("data-type");
	
	showAllDeviceTiles(typeId, statusId);
}

function popupMobileClose() {	
	$("#div_PopupMobile").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
	
	if(isEdited){
		devicesDetails();
		isEdited=false;
	}
}

function popupServerClose() {	
	$("#div_PopupServer").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
	
	if(isEdited){
		devicesDetails();
		isEdited=false;
	}
}

function mapMobiles(){
	// ----- To be replaced with the DragAndDropReusable Component : Starting -----
	
	if(nodeType == "Product"){
		isEdited=false;
		productId = key;
	}else{
		callAlert("Please select Product");
		return false;
	}
	
	$('#searchmobile').keyup(function() {
		var txt=$('#searchmobile').val();
		$("#listCount_allmobiles").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
			$('#allmobiles li').show().filter(function() {
	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");
	        
	    	  return !~text.indexOf(val);
	    
	    }).hide();
			
			$("#listCount_allmobiles").text(+resArr.length+" / "+$('#allmobiles li').length);
			if(txt==""){
				$("#listCount_allmobiles").text($('#allmobiles li').length);
			
			}
	});
	
	$('#searchmap_mob').keyup(function() {
		var txt=$('#searchmap_mob').val();
		if($('#searchmap_mob').value==''){
			$("#listCount_mobilesmapped").text($('#devicesmapped li').length);
		}
	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
	    var resArr = [];
		$('#mobilesmapped li').show().filter(function() {
	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	    	 if(!~text.indexOf(val) == false) resArr.push("item");
	        return !~text.indexOf(val);
	    }).hide();
		
		$("#listCount_mobilesmapped").text(+resArr.length+" / "+$('#mobilesmapped li').length);
		if(txt==""){
			$("#listCount_mobilesmapped").text($('#mobilesmapped li').length);
		
		}
	});
	
	$(".tilebody").empty();
	$(".tilebody").remove();
	
	 document.getElementById("div_PopupMobile").style.display = "block";
	 $("#allmobiles").empty();
	 $("#mobilesmapped").empty();
	  
	
	$("#dragDropListItemLoaderIcon").show();
	var url='admin.genericDevices.product?deviceLabId=-1&productId='+productId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			$("#dragDropListItemLoaderIcon").hide();
			var result = data.Records;
			
			if(result.length==0){
				$("#allmobiles").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Mobiles</b></span>");
				$("#listCount_allmobiles").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var devicesid = item.name;					
					//devicesid=trim(devicesid);					
					var tempStatus=item.availableStatus;
					var status='';
					if(tempStatus==1){
						status='ACTIVE';
					}else{
						status='INACTIVE';
					}
					$("#allmobiles").append("<li id='"+item.genericsDevicesId +"' title='"+item.UDID+"' style='color: black;'>"+devicesid+"-"+status+"&"+item.hostName+"-"+item.hostStatus+")</li>");
			 	});
				$("#listCount_allmobiles").text(result.length);
			}
		},
		error : function(data){
			$("#dragDropListItemLoaderIcon").hide();
		},
		complete : function(data){
			$("#dragDropListItemLoaderIcon").hide();
		}
	});  
	
	urlmappedDevices='admin.genericDevices.list.product?productId='+productId+'&filter=-1';		
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#mobilesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Mobiles</b></span>");
				$("#listCount_mobilesmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.name;					
					var tempStatus=item.availableStatus;
					var status='';
					if(tempStatus==1){
						status='ACTIVE';
					}else{
						status='INACTIVE';
					}
					$("#mobilesmapped").append("<li id='"+item.genericsDevicesId +"'  title='"+item.UDID+"' style='color: black;'>"+tsid+"-"+status+"&"+item.hostName+"-"+item.hostStatus+")</li>");
		
				});
				$("#listCount_mobilesmapped").text(listOfData.length);
			}
		}
	});
	
	// ----- To be replaced with the DragAndDropReusable Component : Ending -----
	
	// ----- To be replaced with the DragAndDropReusable Component : Starting -----
	
	Sortable.create(document.getElementById("allmobiles"), {			    
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
			isEdited=true;
			var draggeditem = trim(evt.item.id);
		 	var urlUnMapping = "";
		 	urlUnMapping = 'administration.product.mapMobile?productId='+productId+'&deviceId='+draggeditem+'&type=unmap';

		 	if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_allmobiles").text($("#allmobiles").children().length);
		 	
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
							}
						}					
				});
			},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
			isEdited=true;
		 	if($("#allmobiles").children().length == 0) {
		 		$("#allmobiles").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Mobiles</b></span>");
		 		$("#listCount_allmobiles").text(0);
		 	}else{
				$("#listCount_allmobiles").text($("#allmobiles").children().length);
		 	}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});
	
	Sortable.create(document.getElementById("mobilesmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){ 
			isEdited=true;
			var draggeditem = trim(evt.item.id);
			urlMapping ='administration.product.mapMobile?productId='+productId+'&deviceId='+draggeditem+'&type=map';
	
		 	if($("#emptyListMapped").length) $("#emptyListMapped").remove();
			$("#listCount_mobilesmapped").text($("#mobilesmapped").children().length);
			
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
							
							}
						}						
				});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
			isEdited=true;
		 	if($("#mobilesmapped").children().length == 0) {
		 		$("#mobilesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Mobile</b></span>");
				$("#listCount_mobilesmapped").text(0);				
		 	}else{
				$("#listCount_mobilesmapped").text($("#mobilesmapped").children().length);							 		
		 	}
		},
		onStart:function(evt){ },
		onEnd: function(evt){}
	});
	
	// ----- To be replaced with the DragAndDropReusable Component : Ending -----
}

function mapServer(){
	
	// ----- To be replaced with the DragAndDropReusable Component : Starting -----
	
	if(nodeType == "Product"){
		productId = key;
		isEdited=false;
	}else{		
		callAlert("Please select Product");
		return false;
	}
	
	$('#search_server').keyup(function() {
		var txt=$('#search_server').val();
		$("#listCount_allservers").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
			$('#allservers li').show().filter(function() {
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");
	    	  return !~text.indexOf(val);
	    
	    }).hide();
			
			$("#listCount_allservers").text(+resArr.length+" / "+$('#allservers li').length);
			if(txt==""){
				$("#listCount_allservers").text($('#allservers li').length);
			}
	});
	
	$('#search_servermap').keyup(function() {
		var txt=$('#search_servermap').val();
		if($('#serversmapped').value==''){
			$("#listCount_serversmapped").text($('#devicesmapped li').length);
		}
	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
	    var resArr = [];
		$('#serversmapped li').show().filter(function() {
	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	    	 if(!~text.indexOf(val) == false) resArr.push("item");
	        return !~text.indexOf(val);
	    }).hide();
		
		$("#listCount_serversmapped").text(+resArr.length+" / "+$('#serversmapped li').length);
		if(txt==""){
			$("#listCount_serversmapped").text($('#serversmapped li').length);
		
		}
	});
		
	$(".tilebody").empty();
	$(".tilebody").remove();
	
	 document.getElementById("div_PopupServer").style.display = "block";
	 $("#allservers").empty();
	 $("#serversmapped").empty();
	 
	
	 $("#dragDropListItemLoaderIconServer").show();
	var url='admin.host.product?deviceLabId=-1&productId='+productId;	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			$("#dragDropListItemLoaderIconServer").hide();
			var result = data.Records;
			
			if(result.length==0){
				$("#allservers").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Server</b></span>");
				$("#listCount_allservers").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var terminalid = item.hostIpAddress;					
					terminalid=terminalid;		
					var serverid = item.hostName;
					
					$("#allservers").append("<li id='"+item.hostId +"' title='"+item.hostIpAddress+"' style='color: black;'>"+terminalid+"-"+serverid+"("+item.hostStatus+")</li>");
			 	});
				$("#listCount_allservers").text(result.length);
			}
		},
		error : function(data){
			$("#dragDropListItemLoaderIconServer").hide();
		},
		complete : function(data){
			$("#dragDropListItemLoaderIconServer").hide();
		}
	});  
	
	urlmappedDevices='administration.host.list.product?productId='+productId+'&filter=-1';        	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#serversmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Servers</b></span>");
				$("#listCount_serversmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var terminalid = item.hostIpAddress;					
					terminalid=terminalid;		
					var serverid = item.hostName;
					
					$("#serversmapped").append("<li id='"+item.hostId +"'  title='"+item.hostIpAddress+"' style='color: black;'>"+terminalid+"-"+serverid+"("+item.hostStatus+")</li>");
		
				});
				$("#listCount_serversmapped").text(listOfData.length);
			}
		}
	});
	
	// ----- To be replaced with the DragAndDropReusable Component : Ended -----
	
	// ----- To be replaced with the DragAndDropReusable Component : Starting -----
 	
	Sortable.create(document.getElementById("allservers"), {			    
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
		 	urlUnMapping = 'administration.product.mapServer?productId='+productId+'&hostId='+draggeditem+'&type=unmap';

		 	if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_allservers").text($("#allservers").children().length);
		 	
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
							}
						}					
				});
			},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
		 	
		 	if($("#allservers").children().length == 0) {
		 		$("#allservers").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Servers</b></span>");
		 		$("#listCount_allservers").text(0);
		 	}else{
				$("#listCount_allservers").text($("#allservers").children().length);
		 	}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});
	
	Sortable.create(document.getElementById("serversmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){ 
			var draggeditem = trim(evt.item.id);
			urlMapping ='administration.product.mapServer?productId='+productId+'&hostId='+draggeditem+'&type=map';
	
		 	if($("#emptyListMapped").length) $("#emptyListMapped").remove();
			$("#listCount_serversmapped").text($("#serversmapped").children().length);
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
							
							}
						}						
				});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
		 
		 	if($("#serversmapped").children().length == 0) {
		 		$("#serversmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Server</b></span>");
				$("#listCount_serversmapped").text(0);				
		 	}else{
				$("#listCount_serversmapped").text($("#serversmapped").children().length);							 		
		 	}
		},
		onStart:function(evt){ },
		onEnd: function(evt){}
	});
	
	// ----- To be replaced with the DragAndDropReusable Component : Ending -----
}

/* END Devices */
</script>

<!-- END JAVASCRIPTS -->
<!-- </body> -->
<!-- END BODY -->
<!-- </html> -->