(function() {
    'use strict';

    var gnattApp = angular.module('plnkrGanttStable',
        ['gantt',
            'gantt.sortable',
            'gantt.movable',
            'gantt.drawtask',
            'gantt.tooltips',
            'gantt.bounds',
            'gantt.progress',
            'gantt.table',
            'gantt.tree',
            'gantt.groups',
            'gantt.resizeSensor',
            'gantt.overlap',
            'gantt.dependencies'
        ]);
	
	gnattApp.controller('activityWorkpackageGanttController', function($scope, $http, $element) {
		var data="";
		//Call the services
		
		$scope.getToday = new Date();
		$scope.treeHeaderName = "";
		$scope.ganttTitle = "Workpackage Gantt View";
		$scope.initialFlag = false;		
		$scope.task = [];
		isActivityGanttChartModified=false;
		
		$element.bind('mousedown', function (e) {
            $scope.$apply(function() {                
               $scope.initialFlag = true;
            });
        });
		
	    $scope.registerApi = function(api) {
	    	 /*api.core.on.ready($scope, function () {
	     		  console.log("ready");
	     		   $scope.task = [];
	     	  });*/
	    		    	 
	    	 api.data.on.change($scope, function (newData, oldData){
	    		 //console.log("changed");
	    		 $scope.task = [];
	    		 $scope.initialFlag=false;	    		 
	    	 });
	    	 
	    	 api.tasks.on.change($scope, function (task) {	    		 
	    		 if($scope.initialFlag){		   
		     		 var activityID = task.model.id;
		     		 var stDate = new Date(task.row.from._d);
		     		 var startDate = String((stDate.getMonth()+1)+"/"+stDate.getDate()+"/"+stDate.getFullYear());
		     		 var enDate = new Date(task.row.to._d);	 
		     		 var endDate = 	 String((enDate.getMonth()+1)+"/"+enDate.getDate()+"/"+enDate.getFullYear());
		     		 
		     		 /*console.log("from date : "+task.row.from._d);
		     		 console.log("to date : "+task.row.to._d);*/
		     		 console.log("activity id : "+task.model.id);
		     		 console.log("startDate : "+startDate);
		     		 console.log("endDate : "+endDate);
		     		
	     			$http.post('process.activity.gantt.update.planDates?activityId='+activityID+'&startDate='+startDate+'&endDate='+endDate).then(function (response) {
	     				//console.log(response.data.Message);
	     				isActivityGanttChartModified=true;
	     				$scope.initialFlag = false;
	     			});
	     		}
		     });	    	 
	    };
	            
		$scope.displayGanttHandler = function (activityWorkPackageIdValue) {
			if(activityWorkPackageIdValue == undefined)
				activityWorkPackageIdValue=40;
	        
			$scope.task = [];
			$scope.initialFlag=false;
	        $http.post('get.Activities.for.gantt.by.activityWorkPackageId?activityWorkPackageId='+activityWorkPackageIdValue, JSON.stringify(data)).then(function (response) {
	        		var resultArr = response.data.Records;
		        	var arr = resultArr[0]['tasks'];
		        	var parentObj={};
		        	var childObj={};
		        	var parentArr=[];
		        	var obj={};
		        	var startDate='';
		        	var endDate='';
		        	
		        	$scope.ganttTitle = "Workpackage Gantt View : "+resultArr[0]['name']+ ' (ID : '+arr[0]['activityWorkPackageId']+')';
		        	
		        	// -- fix length to 75 character --
		        	var len = resultArr[0]['children'].length;
		        	var joinStr = '...';
		        	var str='';
		        	var strlen=0;
		        	var maxCharacterLen=50;
		        			        	
		        	parentObj['name'] = resultArr[0]['name'];
		        	parentObj['idValue'] = "Activity ID";
		        	parentObj['assignee'] = "Assignee";
		        	parentObj['priority'] = "Priority";
		        	parentObj['status'] = "Status";
		        			        	
		        	parentObj['children'] = resultArr[0]['children'];
		        	parentObj['color'] = "#45607D";
		        	parentObj['height'] = "3em";
		        	parentObj['classes'] = "gantt-row-milestone";
		        	parentObj['sortable'] = !1;
		        	parentObj['drawTask'] = !1;
	        	
		        	parentArr.push(parentObj);		        	
		        	
		        	for(var i=0;i<arr.length;i++){	
		        		var taskArr=[];
		        		obj=arr[i];
		        		startDate = obj['from'];
		        		startDate = startDate.split(',');
		        		startDate = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]+' '+startDate[3]+':'+startDate[4]+':'+startDate[4]);
		        		
		        		endDate = obj['to'];
		        		endDate = endDate.split(',');
		        		endDate = new Date(endDate[0]+'/'+endDate[1]+'/'+endDate[2]+' '+endDate[3]+':'+endDate[4]+':'+endDate[4]);
		        		
		        		obj['from']= startDate;
		        		obj['to']= endDate;
		        		
		        		str = arr[i]['name'];
		        		strlen = str.length;
		        		if(strlen>maxCharacterLen){
		        			str = str.substr(0,maxCharacterLen-1);
		        			arr[i]['name'] = str.concat(joinStr);		        			
		        		}
		        		
		        		childObj={};
		        		childObj['id'] = arr[i]['id'];
		        		childObj['idValue'] = arr[i]['id'];
		        		childObj['name'] = arr[i]['name'];					
		        		childObj['assignee'] = arr[i]['assignee'];
		        		childObj['priority'] = arr[i]['priority'];
		        		childObj['status'] = arr[i]['status'];
		        		
		        		taskArr[0] = obj;
		        		childObj['tasks'] = taskArr;
		        		childObj['dependencies'] = childObj['tasks'][0]['dependencies'];
		        		
		        		childObj['progress'] =  arr[i]['progress'];
		        		childObj['progress']['percent'] = parseFloat(arr[i]['progress']['percent']);
		        		childObj['progress']['color'] ="#22313F";		        		
		        		
		        		if(childObj['tasks'][0]['dependencies'] != null){
		        			childObj['tasks'][0]['dependencies'] = arr[i]['dependencies'];		        					        			
		        		}else{
		        			delete  childObj['tasks'][0]['dependencies'];
		        			delete  childObj['dependencies'];
		        		}
		        		
		        		parentArr.push(childObj);		        		
		        		$scope.task.push(taskArr);
		        	}		        	
		        	$scope.data = parentArr;		     
		        	$scope.getToday = new Date();
		        	
		        }, function (response) {
		        	$scope.msg = "Service not Exists";
		        	$scope.statusval = response.status;
		        	$scope.statustext = response.statusText;
		        	$scope.headers = response.headers();
		        });	
	        
	    };		
	});   
})();

