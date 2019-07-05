var selectedNodeDetails;
var nodeDropped = false;

var TreeLazyLoading = function() {	
	   var initialise = function(jsonObj){
		   showTreeLazyLoadingPagination(jsonObj);		   
	   };
			return {	        
	        init: function(jsonObj) {        	
	        	initialise(jsonObj);
	        }		
		};	
}();

function showTreeLazyLoadingPagination(jsonObj){
	getParentTreeLazyNodes(jsonObj.urlToGetTree, jsonObj.urlToGetChildData);
}

function getParentTreeLazyNodes(treeFilterUrl, urlToGetChildData){
	
	var elementDivId = "#treeContainerDiv";
	$(elementDivId).jstree("destroy");
		$(elementDivId).jstree({
			  "core" : {
				  	//"check_callback" : true,
				  	"check_callback" : function(operation, node, node_parent, node_position, more) {
                        if (operation == 'move_node') {
                        	//Check nodetype is Testcase                        	
                            if ((node.data == 'TestCase' ) && (node_parent.data !== 'TestCase')) {                            
                                return true;
                            }else{
                                return false;
                            }
                        }
                    },
				    'data' :  {
				      'type' : 'POST',
				      'url' : function (node) {
				    			return node.id === "#" ? treeFilterUrl : urlToGetChildData+'&node='+node.data.split("~")[1]+'&parentId='+node.data.split("~")[0];
				      },
				      'data' : function (node) {
				    	  		return { 'id' : node.id };
				      }
				      
				    } 
				  },
			   /* "dnd" : {
				    "drag_check" : {
				    	
				    },
					"drag_finish" : function(data){
					   alert("ok");
				   }
			   }, */
	 	       "plugins" : [ "search", "contextmenu", "themes", "ui", "dnd"]
		  });
        
	     $(elementDivId).on("loaded.jstree", function(evt, data){
	    	setTreeLazyLoadingNodeAttr();	
	     });
	     $(elementDivId).on("open_node.jstree", function(evt, data){
	    	 setTreeLazyLoadingNodeAttr();
	     });
	     $(elementDivId).on("move_node.jstree", function(evt, data){
	    	  /*$(elementDivId).jstree("create_node", data.parent, function(e,data){});				
				$('#dcTestcaseTree').jstree(true).refresh();
	    	 	performDCTestCaseDragAndDrop(sourceDCId, destinationDCId, tcId);*/
	     });
	     
	     var to = false;
	     $('#treeContainerPluginsSearch').keyup(function () {
	       if(to) { clearTimeout(to); }
	       
	       to = setTimeout(function () {
	         var v = $('#treeContainerPluginsSearch').val();
	         $(elementDivId).jstree(true).search(v);
	       }, 250);
	     });
	     
		rebindTreeLazyLoadingEvent(elementDivId);
}

/*function treeLazyLoadingDefault(data){
	//var str = JSON.stringify(data);
	//jsonTreeData_new = jQuery.parseJSON(data);
		
	$('#treeContainerDiv').jstree(true).settings.core.data = data;
	$('#treeContainerDiv').jstree(true).refresh();	
}*/

$(document).on('dnd_stop.vakata', function (e, data) {
	nodeDropped = true;	   
});

function rebindTreeLazyLoadingEvent(elementDivId) {
	$(elementDivId).on("select_node.jstree",
	     function(evt, data){
			selectedNodeDetails = data;		
			//handleLazyLoadingTreeNodeSelection(data);
	});
}

function setTreeLazyLoadingNodeAttr() {
	$.each($(".jstree-anchor"), function(ind, elem) {
		$(elem).removeAttr("title");
 		$(elem).attr("title", $(elem).text()); 		
  	});
}