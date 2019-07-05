<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<!-- BEGIN PAGE HEAD --> 
  <div class="page-head" id="page-header" style="display:none">
 		<div class="container-fluid"> 
 			<!-- BEGIN PAGE TITLE --> 
 			<div class="page-title"> 
 				<h1 style="padding-right: 15px;" id="currPageTitle"></h1>
 				<span class="postHeader-separator"></span>
	 			<!-- BEGIN PAGE BREADCRUMB -->
				<ul class="page-breadcrumb breadcrumb">
					<li class="active" id="currSelMenu"> </li>
					<li> <i class="fa fa-circle"></i> </li>			
					<li class="active" id="currSelSubMenu"> </li>
				</ul> 
				<!-- END PAGE BREADCRUMB -->
 			</div>
 			<!-- END PAGE TITLE -->		 
 		</div> 
	</div> 
<!-- END PAGE HEAD -->
<script>

function setBreadCrumb(title){
//WPResPlanning
	$("#currPageTitle").text(title);
}
function setBreadCrumbMenu(menu, submenu){
	$("#currSelMenu").text(menu);
	$("#currSelSubMenu").text(submenu);
}
// $("#menuList>li>ul>li").on("click", function(){alert("hi")
// 	/*if (typeof(Storage) != "undefined") {
// 	 localStorage.setItem("menu", $(this).find("a").text());
// 	}*/
// });
// $(document).ready(function(){
// 	if (typeof(Storage) != "undefined") {
		   
// 	    $("#currSelSubMenu").text(localStorage.getItem("menu"))
// 	} else {
// 	}
// });


</script>
</body>
</html>