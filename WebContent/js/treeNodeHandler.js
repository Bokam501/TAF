var currentNodeId = "";
/*var nodeDetails = new function() {
	this.id = currentNodeId;
	this.parentNode = getParentNode;//(id);
	this.childNode = getChildNode;//(id);
}*/
function getParentNode() {
	var parNode = $.jstree.reference("#treeContainerDiv").get_parent(currentNodeId);
	console.log(parNode);
	return parNode;
}
function getChildNode() {
	var node_details = $.jstree.reference("#treeContainerDiv").get_node(currentNodeId);
	var node_childs = node_details.children;
	return node_childs;
}

function setParentNode() {
	$.jstree.reference('#treeContainerDiv').deselect_all();
	$.jstree.reference("#treeContainerDiv").select_node(getParentNode());
	//$.jstree.reference('#treeContainerDiv').trigger('select_node');
}
function setChildNode() {
	$.jstree.reference('#treeContainerDiv').deselect_all();
	$.jstree.reference("#treeContainerDiv").select_node(getChildNode()[0]);
	//$.jstree.reference('#treeContainerDiv').trigger('select_node');
}
function setDefaultNode() {
//TODO
	
}

