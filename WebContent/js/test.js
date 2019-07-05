function listResourceDemandOfWorkPackage(){
	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainer').jtable({
        title: 'Resource Planning',
        //editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10,
           actions: {
       	 	listAction: urlToGetResourceDemandOfWorkPackage,
       	    updateAction:function(event, data){
       		    console.log("recordUpdated");
				$('#jTableContainer').find('.jtable-child-table-container').jtable('reload');
			}
        },
        recordUpdated: function (event, data) {
            $('#jTableContainer').jtable('reload');
        },
        fields : {
        		workPackageResourceReservationId: {
	                 key: true,
	                 list: false,
	                 create: false
	             },	
	             productId:{
	                 title: 'Product Id',
	            	 list:false,
	            	 hidden: true
	             },
	             productName:{
	                 title: 'Product',
	            	 list:true,
	                 width:"20%"
	             },
	             workPackageId:{
	                 title: 'Work Package Id',
	            	 list:false,
	            	 hidden: true
	             },
	             workPackageName:{
	                 title: 'Work Package',
	            	 list:true,
	                 width:"20%"
	             },
	             shiftId:{
	                 title: 'Shift Id',
	            	 list:false,
	            	 hidden: true
	             },
	             shiftName:{
	                 title: 'Shift',
	            	 list:true,
	                 width:"15%"
	             },
	             demandId:{
	                 title: 'Demand Id',
	            	 list:false,
	            	 hidden: true
	             },
	             resourceDemandCount:{
	                 title: 'Demand',
	            	 list:true,
	                 width:"10%"
	             },
	             /*
	             availableCoreResourceCount:{
	                 title: 'Core Resources',
	            	 list:true,
	                 width:"10%"
	             }, */
	             blockedResourceCount:{
	                 title: 'Resources Blocked',
	            	 list:true,
	                 width:"10%",
	                 display: function (data) {
	                       return $('<a style="color: #0000FF;" href=javascript:loadBlockedResourceDetails('+data.record.workPackageId+','+data.record.shiftId+');>' + data.record.blockedResourceCount + '</a>');
	                 }
	             },
	             gapInResourceCount:{
	                 title: 'Gap',
	            	 list:true,
	                 width:"10%",
	                 display: function (data) {
	                       return $('<a style="color: RED;" href=javascript:loadBlockedUserDetails('+data.record.workPackageId+','+data.record.shiftId+','+data.record.workPackageResourceReservationId+'); class="resource'+data.record.workPackageResourceReservationId+'">' + data.record.gapInResourceCount + '</a>');
	                 }
	             },
       		}, 
       		/*rowUpdated: function (event, data) {
        		console.log("testing............?????");
                $('#jTableContainer').find('.jtable-child-table-container').jtable('reload');
            },
            //Validate form when it is being submitted
            formSubmitting: function (event, data) {
            	console.log("formSubmitting");
                //return data.form.validationEngine('validate');
            },
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
            	console.log("formClosed");
                //data.form.validationEngine('hide');
                //data.form.validationEngine('detach');
            },

            recordsLoaded: function (event, data) {
            	console.log("recordsLoaded");
               // return qtipload();
            },*/
       		//recordsLoaded: function(event, data) {
               // $('#jTableContainer').click(function() {
                	//alert("1");
                	//var record = $tableRow.data('getRowByKey');
                	//alert(record);
                	//var $columns = $tableRow.find('td');
                	//alert(columns);
                    //var row_id = $(this).attr('data-record-key');
                    //alert('clicked row with id '+row_id);
                //});
               
       			/* $('.jtable-data-row').click(function(index,element) {
       				alert("1");
       				var rowToBeSelected = $('#jTableContainer').jtable('getRowByKey', element)[0];
       	            //var row_id = $(this).attr('data-record-key');
       	            alert('clicked row with id '+$(rowToBeSelected));
       	        	 var selectRows = $('#jTableContainer').jtable('selectRows', $(rowToBeSelected));
       	        	 
       	        	alert('clicked row with id '+$(selectRows.attr('id')));
       	         	//alert('clicked row with id '+data.record.getRowByKey);
       	            
       	        });
            }, */
		});
	 $('#jTableContainer').jtable('load');
	 /*window.setInterval(function(){
		 alert($('#jTableContainer')[0]); 
		 //$('#jTableContainer').find('jtable-main-container').jtable('reload');
		 }, 10000);*/
	 //$('#jTableContainer').jtable('destroy');
	// $(".jScrollContainer").jScrollPane();
	 
	var jscrolheight = $("#jTableContainer").height();
	var jscrolwidth = $("#jTableContainer").width();
	  
	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
	   	$("#jTableContainer").width($(".jtable").width()).height($(".jtable").height());			
	       lastScrollLeft = documentScrollLeft;
	   }else if(lastScrollLeft >= documentScrollLeft){			
	   	$("#jTableContainer").width(jscrolwidth).height(jscrolheight);
	   }			
	});
}