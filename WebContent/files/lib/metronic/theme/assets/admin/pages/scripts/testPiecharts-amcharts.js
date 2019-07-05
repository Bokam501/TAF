var chart;
var workpackageFull;
var resourcePackageFull;
var ChartsAmcharts = function() {
	
	workpackageFull = [{
        "Workpackage": "Prilim Work Package",
        "WPid":"WP-1",
        "value": 10
    }, {
        "Workpackage": "Functional-Level1 WP",
        "WPid":"WP-2",
        "value": 8
    }, {
        "Workpackage": "Functional-Level2 WP",
        "WPid":"WP-3",
        "value": 12
    }, {
        "Workpackage": "Non Functional WP",
        "WPid":"WP-4",
        "value": 15
    }];
	
	resourcePackageFull = [{
        "Workpackage": "Redmond ",
        "WPid":"WP-1",
        "value": 100
    }, {
        "Workpackage": "MS - Noida ",
        "WPid":"WP-2",
        "value": 20
    }, {
        "Workpackage": "MS - Hyderabad ",
        "WPid":"WP-3",
        "value": 50
    }, {
        "Workpackage": "MS - Chennai ",
        "WPid":"WP-4",
        "value": 21
    }];
	
    var initChartSample7 = function() {
        chart = AmCharts.makeChart("chart_7", {
            "type": "pie",
            "theme": "light",            
            "fontFamily": 'Open Sans',            
            "color":    '#888',
            "dataProvider": workpackageFull,
            "valueField": "value",
            "titleField": "Workpackage",
            "outlineAlpha": 0.4,
            "depth3D": 15,
            "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
            "angle": 30,
            "exportConfig": {
                menuItems: [{
                    icon: '/lib/3/images/export.png',
                    format: 'png'
                }]
            }
        });
        
        jQuery('.chart_7_chart_input').off().on('input change', function() {
            var property = jQuery(this).data('property');
            var target = chart;
            var value = Number(this.value);
            chart.startDuration = 0;

            if (property == 'innerRadius') {
                value += "%";
            }

            target[property] = value;
            chart.validateNow();
        });
		
        $('#chart_7').closest('.portlet').find('.fullscreen').click(function() {
            chart.invalidateSize();
        });
						
		chart.addListener("clickSlice", function (event) {
			// if dataItem.dataContext.id, this is the 
			// event before drillDown
			if (event.dataItem.dataContext.id != undefined) {
				selected = event.dataItem.dataContext.id;
			}
			else { // click event on subtypes after drilldown occurs
				selected = undefined;
				// PUT YOUR CODE HERE
			}
			//alert("selected :"+event.dataItem.dataContext.Workpackage);
			var str = event.dataItem.dataContext.WPid;
			if(str.split('-').length>1){
				var id = str.split('-')[1].substr(0,1);
				//alert(id)
				
				var selectedTab = $("#btnGrp").find("label.active").index();
				if(selectedTab == 0){
					$("#tblHdrText").text(event.dataItem.dataContext.Workpackage);
					displaySubWorkpackage(parseInt(id));
				}else{
					$("#tblHdrTxtRes").text(event.dataItem.dataContext.Resource);

					displaySubResourceWpackage(parseInt(id));
				}
			}
						
			$("#chartPrevBtn").show();
		});		
	  };
	
	var initChartSample4 = function() {
        var chart = AmCharts.makeChart("chart_4", {
            "type": "serial",
            "theme": "light",
            "handDrawn": false,
            "handDrawScatter": 3,            
            "dataProvider": [{
                "year": "Pool Size",
                "income": 45.5,
            }, {
                "year": "Demand",
                "income": 26.2,
            }, {
                "year": "Available",
                "income": 30.1,
            }, {
                "year": "Booked",
                "income": 29.5,
            }],
            "valueAxes": [{
                "minorGridAlpha": 0,
                "minorGridEnabled": false,
                "position": "top",
                "axisAlpha": 0
            }],
            "startDuration": 1,
            "graphs": [{
                "balloonText": "<span style='font-size:13px;'>[[title]] in [[category]]:<b>[[value]]</b></span>",
                "title": "Resource Tracking",
                "type": "column",
                "fillAlphas": 0.8,

                "valueField": "income"
            }, {
                "balloonText": "<span style='font-size:13px;'>[[title]] in [[category]]:<b>[[value]]</b></span>",
                "bullet": "round",
                "bulletBorderAlpha": 1,
                "bulletColor": "#FFFFFF",
                "useLineColorForBulletBorder": true,
                "fillAlphas": 0,
                "lineThickness": 2,
                "lineAlpha": 1,
                "bulletSize": 7,
                "title": "Expenses",
                "valueField": "expenses"
            }],
            "rotate": true,
            "categoryField": "year",
            "categoryAxis": {
                "gridPosition": "start"
            }
        });
        
        $('#chart_4').closest('.portlet').find('.fullscreen').click(function() {
            chart.invalidateSize();
        });
    };

	  	  
   return {
        //main function to initiate the module

        init: function() {           
            initChartSample7();
            $("#chartPrevBtn").hide();
        },
   
   		init2: function() {           
   			initChartSample4();       
   		}
					
    };

}();

$("#tblHdrText").text("Prilim Work Package");
$("#tblHdrTxtRes").text("Resource-1");

var initData =[{
    "Workpackage": "Planned",
    "value": 8,
    "planStatus":8,
    "executionStatus":2,
    "schedule":300,
    "velocity":2,
    "pass":2,
    "fail":1,
    "bugs":1
}, {
    "Workpackage": "Executed",
    "value": 2,
    "planStatus":8,
    "executionStatus":2,
    "schedule":300,
    "velocity":400,
    "pass":2,
    "fail":0,
    "bugs":0
}];
// ----- workpackage start -----

function displayActiveWorkpackage(){
	//var selectedTab = $("#btnGrp").find("label.active").index();alert(selectedTab)
	//if(selectedTab == 0){
		chart.dataProvider = workpackageFull;
	//}else{
	//	chart.dataProvider = resourcePackageFull;
	//}						
	chart.validateData();
	$("#chartPrevBtn").hide();
}

function displaySubWorkpackage(num){
	switch(num){
		case 1:
			chart.dataProvider = generateChartData1();
		    break;		 
		case 2:
			chart.dataProvider = generateChartData2();
		    break;
		case 3:
			chart.dataProvider = generateChartData3();
		    break;
		case 4:
			chart.dataProvider = generateChartData4();
		    break;		 
		default:
			break;    
	}
	fillActiveWrkPkgData(chart.dataProvider);
	chart.validateData();	
}

function fillActiveWrkPkgData(jsonData) {
	$("#planStatus").text(jsonData[0].planStatus);
	$("#execStatus").text(jsonData[0].executionStatus);
	$("#schedule").text(jsonData[0].schedule);
	$("#velPerDay").text(jsonData[0].velocity);
	//$("#velPerHr").text("-");
	$("#pass").text(jsonData[0].pass);
	$("#fail").text(jsonData[0].fail);
	$("#bugs").text(jsonData[0].bugs);
}
fillActiveWrkPkgData(initData);

function generateChartData1(){
	var generateChartData = [{
        "Workpackage": "Planned",
        "value": 8,
        "planStatus":8,
        "executionStatus":2,
        "schedule":300,
        "velocity":2,
        "pass":2,
        "fail":1,
        "bugs":1
    }, {
        "Workpackage": "Executed",
        "value": 2,
        "planStatus":8,
        "executionStatus":2,
        "schedule":300,
        "velocity":400,
        "pass":2,
        "fail":0,
        "bugs":0
    }];	
	return generateChartData;
}


function generateChartData2(){
	var generateChartData = [{
        "Workpackage": "Planned",
        "value": 200,
        "planStatus":300,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":11,
        "fail":2,
        "bugs":1
    }, {
        "Workpackage": "Executed",
        "value": 50,
        "planStatus":500,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":10,
        "fail":2,
        "bugs":0
    }];	
	return generateChartData;
}

function generateChartData3(){
	var generateChartData = [{
        "Workpackage": "Planned",
        "value": 400,
        "planStatus":500,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":20,
        "fail":4,
        "bugs":1
    }, {
        "Workpackage": "Executed",
        "value": 100,
        "planStatus":510,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":10,
        "fail":2,
       "bugs":0
    }];	
	return generateChartData;
}

function generateChartData4(){
	var generateChartData = [{
        "Workpackage": "Planned",
        "value": 10,
        "planStatus":900,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":16,
        "fail":2,
        "bugs":0
    }, {
        "Workpackage": "Executed",
        "value": 2,
        "planStatus":560,
        "executionStatus":600,
        "schedule":300,
        "velocity":400,
        "pass":10,
        "fail":2,
        "bugs":0
    }];	
	return generateChartData;
}

// ----- workpackage end -----
	
// ----- resource package start -----
	
	function displayActiveResource(){
		chart.dataProvider = resourcePackageFull;					
		chart.validateData();
		$("#chartPrevBtn").hide();
	}
	
function displaySubResourceWpackage(num){
	switch(num){
		case 1:
			chart.dataProvider = generateResourceChartData1();
		    break;		 
		case 2:
			chart.dataProvider = generateResourceChartData2();
		    break;
		case 3:
			chart.dataProvider = generateResourceChartData3();
		    break;
		case 4:
			chart.dataProvider = generateResourceChartData4();
		    break;
		 default:
			break;    
	}
	fillActiveResourceData(chart.dataProvider);
	chart.validateData();	
}

function fillActiveResourceData(jsonData) {
	$("#resResrvCnt").text(jsonData[0].reservedRes);
	$("#resDemCnt").text(jsonData[0].demandRes);
	$("#resNtCnfrm").text(jsonData[0].resNotConfirmed);
}
var initDataRes = [{
    "Workpackage": "Available",
    "value": 8,
    "reservedRes":8,
    "demandRes":10,
    "resNotConfirmed":6
}, {
    "Workpackage": "Booked",
    "value": 2,
    "reservedRes":20,
    "demandRes":30,
    "resNotConfirmed":5
}];	

fillActiveResourceData(initDataRes);
function generateResourceChartData1() {
	var generateChartData = [{
        "Workpackage": "Available",
        "value": 8,
        "reservedRes":8,
        "demandRes":10,
        "resNotConfirmed":6
    }, {
        "Workpackage": "Booked",
        "value": 2,
        "reservedRes":20,
        "demandRes":10,
        "resNotConfirmed":5
    }];	
	return generateChartData;
}


function generateResourceChartData2() {
	var generateChartData = [{
        "Workpackage": "Available",
        "value": 100,
        "reservedRes":70,
        "demandRes":30,
        "resNotConfirmed":2
    }, {
        "Workpackage": "Booked",
        "value": 44,
        "reservedRes":20,
        "demandRes":30,
        "resNotConfirmed":5
    }];	
	return generateChartData;
}

function generateResourceChartData3() {
	var generateChartData = [{
        "Workpackage": "Available",
        "value": 20,
        "reservedRes":90,
        "demandRes":30,
        "resNotConfirmed":1
    }, {
        "Workpackage": "Booked",
        "value": 10,
        "reservedRes":20,
        "demandRes":30,
        "resNotConfirmed":5
    }];	
	return generateChartData;
} 

function generateResourceChartData4() {
	var generateChartData = [{
        "Workpackage": "Available",
        "value": 10,
        "reservedRes":20,
        "demandRes":10,
        "resNotConfirmed":4
    }, {
        "Workpackage": "Booked",
        "value": 2,
        "reservedRes":20,
        "demandRes":30,
        "resNotConfirmed":5
    }];	
	return generateChartData;
}	
	
		