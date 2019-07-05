
var openLoaderIconFlag=false;
function openLoaderIcon(messages){
	//console.log("UI Blocked");
	if(messages == undefined){
		messages = 'Please Wait...';
	}
	
	if(!openLoaderIconFlag){
		Metronic.blockUI({
			//message: 'Please Wait...',
			message: messages,
	        boxed: true
	    });
		openLoaderIconFlag=true;
	}
}

function closeLoaderIcon(){	
	//console.log("UI UnBlocked");
	if(openLoaderIconFlag){
		Metronic.unblockUI();
		openLoaderIconFlag=false;
	}
	
}

