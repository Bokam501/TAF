<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>

<div id="featureDetailedViewsModal" class="modal fade" tabindex="-1" aria-hidden="true" style="display:none; width: 98%; left: 1%; top: 1%; bottom: 1%;">
	<div class="modal-full">
		<div class="modal-content" style="min-height: 500px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title">Feature Details</h4>
			</div>
			<div class="modal-body">
				<div id="tablistRelatedFeatureDetails">
					<ul class="nav nav-tabs toolbarFullScreen">
						<li class="active"><a href="#relatedFeatureDetails" onclick="getFeatureDetailsForResultOverall()" data-toggle="tab">Feature Details</a></li>					
						<li><a href="#relatedTestCases" onclick="getRelatedFeatureTestcases()" data-toggle="tab">Related Testcases</a></li>
					</ul>					
					<div class="tab-content">		
						<div id="relatedFeatureDetails" class="tab-pane fade active in" style="overflow: hidden; height: 100%;">
							<div class="scroller"  style="height:490px" data-always-visible="1" data-rail-visible1="1">
								<div class="panel-body">
									<div class="table-responsive">		
										
										<div class="row"  id="pdtSummaryView">
											
											<div class="col-md-6">
												<div class="table-scrollable">
													<table class="table table-striped table-hover" id="opnTable">
														<thead>
														<tr height="30">
															<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Feature Summary : </th>
															<th>
															</th>
														</tr>
														</thead>
														<tbody>
															<tr>
																<td>ID: 
																</td>
																<td><span id="featureId"></span></td>
															</tr>
															<tr>
																<td>Name :
																</td>
																<td><span id="featureName"></span>
																</td>
															</tr>
															<tr>
																<td>Code :
																</td>
																<td><span id="featureCode"></span>
																</td>
															</tr>
															<tr>
																<td>Parent Feature Name :
																</td>
																<td><span id="parentFeatureName"></span>
																</td>
															</tr>												
															<tr>
																<td>Priority :
																</td>
																<td><span id="featurePriority"></span>
																</td>
															</tr>
														</tbody>
													</table>
												</div>										
											</div>
											
											<div class="col-md-6">
												<div class="table-scrollable">
													<table class="table table-striped table-hover" id="dfnTable">
													<thead>
														<tr height="30">
															<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Product Summary : </th>
															<th>
															</th>
														</tr>
														</thead>
														<tbody>
														<tr>
															<td>ID :
															</td>
															<td><span id="productId"></span>
															</td>
														</tr>
														<tr>
															<td>Name : 
															</td>
															<td><span id="productName"></span></td>
														</tr>
														<tr>
															<td>Type :
															</td>
															<td><span id="productType" ></span>
															</td>
														</tr>											
														</tbody>
													</table>
												</div>										
											</div>
											
										</div>							
										
										<div class="row"  id="pdtSummaryView">
											<div class="col-md-6">	
											<div class="row" >
											
											<div class="table-scrollable">
												<table class="table table-striped table-hover" id="resTable">
													<thead>
													<tr height="30">
														<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Feature mapping data:</th>
														<th>
														</th>
													</tr>
													</thead>
													<tbody>
														<tr>
															<td>Mapped Testcase Count :
															</td>
															<td><span id="mappedTestcaseCount"></span></td>
														</tr>
													</tbody>
												</table>
											</div>
											
											<div class="table-scrollable">
													<table class="table table-striped table-hover" id="resTable">
														<thead>
														<tr height="30">
															<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Feature Audit Information :</th>
															<th>
															</th>
														</tr>
														</thead>
														<tbody>
															<tr>
																<td>Created :
																</td>
																<td><span id="createdDate"></span></td>
															</tr>
															<tr>
																<td>Modified:
																</td>
																<td><span id="modifiedDate"></span></td>
															</tr>
																																					
														</tbody>
													</table>
												</div>
											</div>
										</div>
										</div>	
									<br>
							</div>			
					</div>
				</div>
						</div>
						<div id="relatedTestCases" class="tab-pane fade" style="overflow: hidden; height: 100%;">	
							<div id="relatedTestcaseDetails"></div>
						</div>
					</div>
				</div>					
			</div>	
		</div>
	</div>
</div>

<script src="js/featureOverAllView.js" type="text/javascript"></script>
</body>

</html>