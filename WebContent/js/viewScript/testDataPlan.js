//BEGIN: ConvertDataTable
		function myTestdataPlanDataTable(){
			//var url = 'project.codes.list'+'?jtStartIndex=0&jtPageSize=10';
			//var projectCodeId = 48;
			var url = 'testDataPlan.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
			var jsonObj = 	{
								"Title":"Test Data Plan",
								"url":url,
								"jtStartIndex":0,
								"jtPageSize":1000,
								"componentUsageTitile":"Test Data Plan"
							};
			assignTestdataPlanDataTableValues(jsonObj);
		};
		
		function assignTestdataPlanDataTableValues(jsonObj){
			openLoaderIcon();
			 $.ajax({
				  type: "POST",
				  url:jsonObj.url,
				  contentType: "application/json; charset=utf-8",
				  dataType : 'json',
				  success : function(data) {		
					closeLoaderIcon();
					
					if(data.Result=="ERROR"){
		      		    data = [];						
					}else{
						data = data.Records;
					}
					
					jsonObj.data = data;
					testDataPlanDataTableContainer(jsonObj);
				  },
				  error : function(data) {
					 closeLoaderIcon();  
				 },
				 complete: function(data){
					closeLoaderIcon();
				 }
			});	
		}

		function testDataPlanDataTable(){
			var childDivString = '<table id="myTestdataPlan_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
			'<thead>'+
				'<tr>'+
						'<th class="dataTableChildHeaderTitleTH">Name</th>'+
						'<th class="dataTableChildHeaderTitleTH">Description</th>'+
						'<th class="dataTableChildHeaderTitleTH">Date</th>'+
						'<th class="dataTableChildHeaderTitleTH">Created By</th>'+
				'</tr>'+
			'</thead>'+
			'<tfoot>'+
				'<tr>'+
					'<th></th>'+
					'<th></th>'+
					'<th></th>'+
					'<th></th>'+
				'</tr>'+
			'</tfoot>'+	
			'</table>';		
			
			return childDivString;	
		}
		var myTestdataPlanDT_oTable='';
		
		function testDataPlanDataTableContainer(jsonObj){
			try{
				if ($("#dataTableContainerTestdataPlan").children().length>0) {
					$("#dataTableContainerTestdataPlan").children().remove();
				}
			} 
			catch(e) {}
			
			
			var childDivString = testDataPlanDataTable(); 			 
			$("#dataTableContainerTestdataPlan").append(childDivString);
			
			editorTestdataPlan = new $.fn.dataTable.Editor( {
	    	    "table": "#myTestdataPlan_dataTable",
	    		ajax: "testDataPlan.save.and.update",
	    		ajaxUrl: "testDataPlan.save.and.update",
	    		idSrc:  "testDataPlanId",
	    		i18n: {
	    	        create: {
	    	            title:  "Create a new Test Data Plan",
	    	            submit: "Create",
	    	        }
	    	    },
	    		fields: [
	    		{
	                label: "Name",
	                name: "testDataPlanName",
			     },{
		                label: "productId",
		                name: "productId",
		                "type": "hidden",
		                "def" : projectCodeId,
			     },{
		                label: "TestDataPlanId",
		                name: "testDataPlanId",
		                "type": "hidden",
		              //  "def" : 70,
			     },{
		                label: "userName",
		                name: "userName",
		                "type": "hidden",
			     },{
		                label: "createdOn",
		                name: "createdOn",
		                "type": "hidden",
				},{
	                label: "Description",
	                name: "testDataPlanDescription",
	            }
	        ]
	    	});
	    	
			editorTestdataPlan.on( 'preSubmit', function ( e, o, action ) {
		        if ( action !== 'remove' ) {
			        var elementName = editorTestdataPlan.field('testDataPlanName');
			        if ( ! elementName.isMultiValue() ) {
			            if ( ! elementName.val() ) {
			            	elementName.error( 'Please enter Name');
			            	return false;
			            }
			        }
			        var elementDesc = editorTestdataPlan.field('testDataPlanDescription');
			        if ( ! elementDesc.isMultiValue() ) {
			            if ( ! elementDesc.val() ) {
			            	elementDesc.error( 'Please enter Description');
			            	return false;
			            }
			        }
		        }
		        if(action == 'create'){
					for(var i=0;i<myTestdataPlanDT_oTable.DataTable().data().length;i++){
		    			if(o.data[0].testDataPlanName == myTestdataPlanDT_oTable.DataTable().data()[i].testDataPlanName){
		    				this.error("The record already exists...");
		    				return false;
		    			}
					}
				}
		        
		    } );
			
			myTestdataPlanDT_oTable = $("#myTestdataPlan_dataTable").dataTable( {
				"dom":'Bfrtilp',
				paging: true,	    			      				
				destroy: true,
				searching: true,
				bJQueryUI: false,
			    "sScrollX": "90%",
		       "sScrollXInner": "100%",
		       //"scrollY":"100%",
		       "scrollY":"300px",
		       "bScrollCollapse": true,	 
		       //"aaSorting": [[1,'desc']],
		       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#myTestdataPlan_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestdataPlanDT();
			   },  
			   buttons: [
						 { extend: "create", editor: editorTestdataPlan },
				         {
			                extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend: 'excel',
			                    	title: 'My Testdata Plan',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                    {
			                    	extend: 'csv',
			                    	title: 'My Testdata Plan',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                ],	                
			            }
			            //'colvis'
			           ], 
		          aaData:jsonObj.data,		    				 
		          aoColumns: [	        	        
				       { mData: "testDataPlanName", className: 'editable', sWidth: '25%' },		
				       { mData: "testDataPlanDescription", className: 'editable', sWidth: '25%' },		
 				       { mData: "createdOn", className: 'disableEditInline', sWidth: '25%' },		
				       { mData: "userName", className: 'disableEditInline', sWidth: '25%' },		
				   ],
				   "oLanguage": {
				       "sSearch": "Search all columns:"
				   },     
			}); 
			// Activate an inline edit on click of a table cell
			$('#myTestdataPlan_dataTable').on( 'click', 'tbody td.editable', function (e) {
				editorTestdataPlan.inline( this, {
		            submitOnBlur: true
		        } );
			});
			$("#myTestdataPlan_dataTable_length").css('margin-top','8px');
			$("#myTestdataPlan_dataTable_length").css('padding-left','35px');		

			myTestdataPlanDT_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );

		}
		var clearTimeoutTestdataPlanDT='';
		function reInitializeTestdataPlanDT(){
			clearTimeoutTestcaseDT = setTimeout(function(){				
				myTestdataPlanDT_oTable.DataTable().columns.adjust().draw();
				clearTimeout(clearTimeoutTestdataPlanDT);
			},200);
		}

		//END: ConvertDataTable
