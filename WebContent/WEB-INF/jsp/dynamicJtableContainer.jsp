<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<style type="text/css">
 .dynamic_line {
    display: block;
    height: 1px;
    border: 0;
    border-top: 1px solid #ccc;
    margin: 1em 0;
    padding: 0;
}
</style>

</head>
<body>

<div id="div_DynamicJTableSummary" class="modal " tabindex="-1" aria-hidden="true" style="display:none; width: 96%; left: 2%; top: 2%;">
		<div class="modal-full" >
			<div class="modal-content" style="min-height: 600px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>					
					<h4 class="modal-title  theme-font" ></h4>
					
				<h4 class="modal-title" id="headerSubTitle" style="padding-top: 5px;"></h4>				
					
				</div>
				<div class="modal-body">
					<!-- <div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1"> -->						
						<div class="container" id="test" style="height: 100%; padding: 0px;margin-top: -20px !important;">								
							<div class="row">
								<div class="col-lg-6 col-md-6" id="InProductSummary">
								</div>
								<div class="col-lg-6 col-md-6" id="InResourceTemplate">
								</div>	
							</div>
							
							<div class="dynamic_line"></div>
							
							<div class="row">
								<div class="col-lg-6 col-md-6" id="InActivitySummary">
								</div>
								<div class="col-lg-6 col-md-6">
								</div>	
							</div>																
						</div>						
					<!-- </div> -->
				</div>
			</div>
		</div>
	</div>

<script src="js/dynamicJtableContainer.js" type="text/javascript"></script>
</body>
</html>