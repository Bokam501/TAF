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
	<div id="buildListItemsContainer" tabindex="-1" aria-hidden="true" style="width:35%;display:inline-block;outline:none;">
		<div>
			<div>
				<div style="padding: 15px 0px;">
					<div class="scroller" style="xheight: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test" style="xheight: 100%; float: left; display: inline-flex; padding-left: 30px;">
								<div id="leftItemsHeaderBuild" class="portlet box blue" style="width:100%;border:1px solid #60aee4 !important;background: white;" data-force="30">
									<div class="portlet-title" style="height:55px;background-color:#3598dc !important;">										
									 <h5><span style="position:relative;top:8px;">Recent Builds</span>
									 	<div id="home_recent_builds" class="inlineHelp">
											<i class="fa fa-question-circle inlineHelpItem" title="Help" onclick="editorHTMLHandler(event)"></i>
									 	</div>
										<div id="recentBuilds">
											<div id="recentBuilds_dd" style="float:right;">
												<select class="form-control input-small xselect2me" id="recentBuilds_ul">
													<option value="0" selected><a href="#">Day</a></option>
													<option value="1" ><a href="#">Week</a></option>
													<option value="2" ><a href="#">Month</a></option>
												</select>
											</div>
										</div>
									</h5> 
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
										 	<ul id="leftDragItemsContainer" class="block__list block__list_tags" style="padding-left: 0px; height: 280px; overflow-y: auto; background-color: white;">
												<li style="color: black;">No Items to show</li>
											</ul>											
											<div class="row hidden">
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
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- ended popup -->

<!-- started popup 2-->
	<div id="wpkgeListItemsContainer" tabindex="-1" aria-hidden="true" style="width:35%;display:inline-block;outline:none;">
		<div>
			<div>
				<div style="padding: 15px 0px;">
					<div class="scroller" style="xheight: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test" style="xheight: 100%; float: left; display: inline-flex; padding-left: 30px;">
								<div id="leftItemsHeaderWpkge" class="portlet box blue" style="width:100%;border:1px solid #60aee4 !important;background: white;" data-force="30">
									<div class="portlet-title" style="height:55px;background-color:#3598dc !important;">										
									 <h5><span style="position:relative;top:8px;">Recent Workpackages</span>
									 	 <div id="home_recent_version" class="inlineHelp">
											<i class="fa fa-question-circle inlineHelpItem" title="Help" onclick="editorHTMLHandler(event)"></i>
									 	</div>
								 	</h5>									  
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchLeftDragItemsWorkpackage" placeholder="Type to search"></div>
									</div>									
								 	<div class="a">									 
											<div id="dragDropListItemLoaderIconWorkpackage" style="display:none;z-index:100001;position:absolute;top:46%;left:22%">
												<img src="css/images/ajax-loader.gif"/>
											</div>									 
											<div>									 
										 	<ul id="wpkgeItemsContainer" class="block__list block__list_tags" style="padding-left: 0px; height: 280px; overflow-y: auto; background-color: white;">
												<li style="color: black;">No Items to show</li>
											</ul>											
											<div class="row hidden">
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
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- ended popup 2-->

<script type="text/javascript" src="js/viewScript/listItems.js"></script>
<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
<script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>
		
</body>

</html>