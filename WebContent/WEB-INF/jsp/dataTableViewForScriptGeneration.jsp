<div id="keywordDiv" class="tab-pane">	</div>

<script type="text/javascript">
var headerArr;
var renderColData=[];

function scriptGenerationDataTable(cols, coldata){					
	var titleName = '';
	var secondRow = '';
	var footer = '';
	var flagHeader = true;
	titleName =	'<th rowspan="2" class="dataTableChildHeaderTitleTH text-center">LANGUAGE</th>';
	footer = footer+'<th></th>';
	$.each(cols,function(index, element) {
		titleName =	titleName +'<th colspan="4" class="dataTableChildHeaderTitleTH text-center">'+element.DisplayText+'</th>';
		if(flagHeader){
			flagHeader=false;
			secondRow =	secondRow +'<th class="dataTableChildHeaderTitleTH">Scriptless</th><th class="dataTableChildHeaderTitleTH">Script Generation Generic</th><th class="dataTableChildHeaderTitleTH">Script Generation TAF</th><th class="dataTableChildHeaderTitleTH">Test Execution</th>';		
			footer = footer+'<th></th><th></th><th></th>';
		}else{
			secondRow =	secondRow +'<th class="dataTableChildHeaderTitleTH">Scriptless</th><th class="dataTableChildHeaderTitleTH">Script Generation Generic</th><th class="dataTableChildHeaderTitleTH">Script Generation TAF</th><th class="dataTableChildHeaderTitleTH">Test Execution</th>';	
			footer = footer+'<th></th><th></th><th></th>';
		}
		footer = footer+'<th></th>';
	});
	
	for(key in coldata){
		if(coldata.hasOwnProperty(key)){
			delete coldata[key].modifiedFieldTitle;
			delete coldata[key].modifiedFieldValue;
			delete coldata[key].oldFieldValue;
		}
	}
	
	for(key in coldata){
		if(coldata.hasOwnProperty(key)){
			for(var keyItem in coldata[key]){
				if(keyItem == "languageName"){										
					renderColsJson = [{mData: keyItem, className: 'editable',sWidth: '5%'}]
				}else{
					if(coldata[key][keyItem].toLowerCase() == 'yes'){
						coldata[key][keyItem] = '1';
		           	}else if(coldata[key][keyItem].toLowerCase() == 'no'){
		           		coldata[key][keyItem] = '0'; 
		           	}					
				}
			}
		}
	}
				
	 for(key in coldata){
		if(coldata.hasOwnProperty(key)){
			for(var keyItem in coldata[key]){
				var renderColsJson;
				if(keyItem == "languageName"){										
					renderColsJson = [{mData: keyItem, className: 'editable',sWidth: '5%'}]
				}else{
					if(coldata[key][keyItem].toLowerCase() == 'yes'){
						coldata[key][keyItem] = '1';
		           	}else if(coldata[key][keyItem].toLowerCase() == 'no'){
		           		coldata[key][keyItem] = '0'; 
		           	} 
					
					data = optionsValueHandler(editorTestdataTest, keyItem , coldata[key][keyItem]);
					renderColsJson = [{mData: keyItem, className: 'editable',sWidth: '5%', 
						mRender: function(data, type, full){
							if(data == 1 || data.toLowerCase() == 'yes'){
								data = '<span style="color:#2db300;"> YES </span>';	
							}else{
								data = '<span style="color:#7575a3;"> NO </span>';
							}					
							return data;
						}
					}]; 				
				} 
				
				//data = optionsValueHandler(editorTestdataTest, keyItem , coldata[key][keyItem]);								
				//renderColsJson = [{mData: keyItem, className: 'editable selectedit',sWidth: '5%'}];
			
				renderColData = renderColData.concat(renderColsJson);
			}
		}
		break;
	}

	var childDivString = '<table id="keyword_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+titleName+'</tr>'+
		'<tr>'+secondRow+'</tr>'+
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	footer +
	'</tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}

function assignDataTableValuesForTestEngine(){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
   			url:'gettestengines.list',				
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}	
			$.ajax({
				type: "POST",
		   		url:'common.list.testToolMaster.option.supportmatrix',				
				contentType: "application/json; charset=utf-8",
				dataType : 'json',
				success : function(header) {		
					// -----
					headerArr = (header.Options);
					listKeywords(data);			
				  },
				  error : function(data) {
					 //closeLoaderIcon();  
				 },
				 complete: function(data){
					//closeLoaderIcon();
				 }
			});
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

var clearTimeoutDTPMP='';
function reInitializeDTBddKeyword(){
	clearTimeoutDTPMP = setTimeout(function(){				
		tcResults_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTPMP);
	},200);
}

function bddKeywordDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTBddKeyword();
		$("#keyword_dataTable_wrapper .dataTables_scrollBody").css('max-height','220px');
	}else{
		reInitializeDTBddKeyword();
		$("#keyword_dataTable_wrapper .dataTables_scrollBody").css('max-height','400px');
	}
}

var isShareObj={};
var isShareArr=[];
var nameArr = [];
var nameLableArr = [];
var editorTestdataTest='';

function listKeywords(data){
	nameArr = [];
	nameLableArr = [];
	var fieldsJson = [];
	var fieldsJsonAll;
	for(key in data){
		if(data.hasOwnProperty(key)){
			for(var keyItem in data[key]){
				if(keyItem == "modifiedFieldTitle" || keyItem == "modifiedFieldValue" || keyItem == "oldFieldValue"){
					
				}else{					
					var temp = keyItem;
					var tempIndex=0;
					tempIndex = temp.indexOf('Scriptless');
					if(tempIndex == -1){
						tempIndex = temp.indexOf('ScriptGeneration');	
					}if(tempIndex == -1){
						tempIndex = temp.indexOf('ScriptGenTAF');	
					}if(tempIndex == -1){
						tempIndex = temp.indexOf('TestExecution');	
					}
					
					if(tempIndex != -1){
						var temp2 = temp.substring(0,tempIndex);
						var temp3 = temp.substring(tempIndex,keyItem.length)+'~'+temp2;
						nameLableArr.push(temp3);	
					}else{
						nameLableArr.push(keyItem);
					}
					nameArr.push(keyItem);		
				}
			}
			break;
		}
	}
	isShareObj = {"Options":[{"value":"1","label":"YES","DisplayText":"YES"},
                       {"value":"0","label":"NO","DisplayText":"NO"}
				]		
			};						
	isShareArr.push(isShareObj.Options);
	
	for(var i=0;i<nameArr.length;i++){
		if(nameArr[i] == "languageName"){
			fieldsJsonAll = [{
				label: nameArr[i],
				name: nameArr[i],
				options: isShareArr[0],
			}]
		}else{
			fieldsJsonAll = [{
				label: nameLableArr[i],
				name: nameArr[i],
				options: isShareArr[0],
	             "type":"select",
			}]
		}
		fieldsJson = fieldsJson.concat(fieldsJsonAll);
	}
			
	editorTestdataTest = new $.fn.dataTable.Editor( {
	    "table": "#keyword_dataTable",
		ajax: "language.testengine.update",
		ajaxUrl: "language.testengine.update",
		idSrc:  "languageName",
		i18n: {
	        create: {
	            title:  "Create a new Test Data",
	            submit: "Create",
	        }
	    }, 
	    fields: fieldsJson		
	});
	
	try{
		if ($("#scriptGeneratorContainer").children().length>0) {
			$("#scriptGeneratorContainer").children().remove();
		}
	} 
	catch(e) {};
	
	var childDivString = scriptGenerationDataTable(headerArr, data); 			 
	$("#scriptGeneratorContainer").append(childDivString);
	
	 tcResults_oTable = $('#keyword_dataTable').dataTable( {
		dom: "Bfrtilp",	
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,	
       "sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY": "220px",
       "bScrollCollapse": true,
       "fnInitComplete": function(data) {
	  		var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	  		$('#keyword_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	  		reInitializeDTBddKeyword();
	   },  
        buttons: [
 					//{ extend: "create", editor: editorTestdataTest }, 
                  {
			          extend: "collection",	 
			          text: 'Export',
		              buttons: [
			          {
	                    	extend: 'excel',
	                    	title: 'BDD Keywords',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'BDD Keywords',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                   ],
			         },
			         //'colvis',
	               ],         
       aaData:data,    
       aoColumns: renderColData,      
       "oLanguage": {
           "sSearch": "Search all columns:"
       }, 
	});
	 
	//-----------------	
	 $(function(){ // this will be called when the DOM is ready 	
		 
	 $('#keyword_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	editorTestdataTest.inline( this, {
                submitOnBlur: true
            } );
     } );

	 tcResults_oTable.DataTable().columns().every( function () {
	        var that = this;
	 
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
	 
	    $("#keyword_dataTable_length").css('margin-top','8px');
		$("#keyword_dataTable_length").css('padding-left','35px');		
		$(".select2-drop").css('z-index','100000');
	} ); 
}

</script>