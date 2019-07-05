<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link type="text/css" href="css/scott.css" rel="stylesheet" media="all" />
</head>

<body>	
<!-- started popup -->
	<div id="dragListItemsContainer" class="modal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-dnd">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closePopupDragListItems()"></button>
					<h4 id="headertag" class="modal-title">Map / unmap Item</h4>
					
					<div id="dropDownWorkpacakge_dd" style="top: 10px;right: 45px;display: none;">
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div id="leftDragItemsHeader" class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
									 <h5>Available Items<span class='badge badge-default' id='leftDragItemsTotalCount' 
									 	style='float:right;background:#a294bb'>0</span></h5> 
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchLeftDragItems" placeholder="Type to search"></div>
									</div>									
								 	<div class="a">									 
											<div id="dragDropListItemLoaderIcon" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
												<img src="css/images/ajax-loader.gif"/>
											</div>									 
											<div>									 
										 	<ul id="leftDragItemsContainer" class="block__list block__list_tags"
											style="padding-left: 0px; height: 280px; overflow-y: auto; background-color: white;">
												<li style="color: black;">No Items to show</li>
											</ul>											
											<div class="row">
												<div class="col-md-1" style="padding-right:0px; padding-left: 0px;">
													<select id="selectAllUnmappedTestcasesRowCount" onchange="setDropDownName(this.value)" style="vertical-align: middle;margin-top: 10px;">
														<option value="10">10</option>
														<option value="25">25</option>
														<option value="50" selected="selected">50</option>
														<option value="100">100</option>
														<option value="250">250</option>
														<option value="500">500</option>
													</select>
												</div>
												<div class="col-md-8" style="padding-right:0px; padding-left: 10px;">
													<div id="paginationCase" class="pagination toppagination"></div>
												</div>
												<div class="col-md-3" style="padding-right: 0px;  padding-left: 0px; padding-top: 10px;"> 
													<span id="badgeUnMappedTestCases" class="badge badge-default" style="background:#8775a7;margin-top: 10px;margin-bottom: 10px;"></span>
												</div>												
											</div>											
									 	</div>
								  </div>									
								</div>
							</div>
								<div id="rightDragItemsHeader" class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Mapped Items<span class='badge badge-default' id='rightDragItemsTotalCount' style='float:right;background:#076'>0</span></h5>
									</div>
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchRightDragItems" placeholder="Type to search"></div>
									</div>
									<ul id="rightDragItemsContainer" class="block__list block__list_tags"
										style="padding-left: 0px; height: 280px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Items to show</li>
									</ul>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- ended popup -->
<script type="text/javascript" src="js/dragDropListItems.js"></script>
<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
<script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>
		
</body>

</html>