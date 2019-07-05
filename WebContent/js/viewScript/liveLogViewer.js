var liveLogTimer;
var logContent;
var lastLine;
var jobStatus ="";

function liveLogClose(){
	clearTimeout(liveLogTimer);
	jobLogContent = "";
}
function liveLog(testJobId,lastLine){
	$.ajax({
		type:"POST",
		url: 'testjob.livelog.status?testJobId='+testJobId+'&lastLine='+lastLine,
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		success: function(data){
			if(data.Result == "OK"){
				if(data.Record.logFileContent != null && data.Record.logFileContent != undefined && data.Record.lastLine != null){
					 logContent = data.Record.logFileContent;
					 lastLine = data.Record.lastLine;
					 jobStatus = data.Record.jobStatus;
				}
					
				jobLogContent = jobLogContent + logContent ;
				//$('#liveLogText').val('');
				$('#liveLogText').val(jobLogContent);
				$('#liveLogContainer .modal-title').text("Job ID- "+testJobId+": Log viewer -"+" Status : "+data.Record.jobStatus);
				$("#liveLogContainer").modal();		
				if(!data.Record.jobStatus.includes("Completed"))
					liveLogTimer = setTimeout(function(){ liveLog(testJobId,lastLine); }, 20000);
			}else if(data.Result == "ERROR"){
				liveLogClose();
				callAlert('Job Log is not available.');
			}
		},
		error: function(data){
			
		},
		complete: function(data){
			
		}
	});
	//liveLogTimer = setInterval(function(){ liveLog(testJobId); }, 2000);
}
