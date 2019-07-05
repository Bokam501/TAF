
function activityChartView(){
	$("#activityGnattChartContainer").modal();
	angular.element($("#activityWorkpackageGanttController")).scope().displayGanttHandler(document.getElementById("treeHdnCurrentActivityWorkPackageId").value);    
}
