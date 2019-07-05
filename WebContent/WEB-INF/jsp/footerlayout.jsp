<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval var="planworkpackgesGridWorkPackagesHeaderMenu" expression="@ilcmProps.getProperty('XEROX_PLAN_TEST_WORKPACKAGE_LABLEL_WORKPACKAGE')" />
<spring:eval var="planworkpackgesGridProductHeaderMenu" expression="@ilcmProps.getProperty('XEROX_SETUP_PRODUCTMANAGEMENT_GRID_HEADER_PRODUCT')" />


</head>
<body>
<div class="page-footer" style="position: fixed; bottom: 0px; margin: 0px; width: 100%;z-index:1;">
	<div class="container" style="margin-left:0px;text-align:center;">© Copyright 2014 - 2019<a href="http://www.hcl.com">  HCL Technologies Ltd.</a>
	&nbsp &nbsp <span>This website is best viewed in Google Chrome.</span>
	</div>
</div>
<div class="scroll-to-top">
	<i class="icon-arrow-up"></i>
</div>

<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="files/lib/metronic/theme/assets/global/plugins/respond.min.js"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/icheck/icheck.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
<!-- END CORE PLUGINS -->
<script src="js/tab/jquery-ui.js"></script>
<script src="files/lib/metronic/theme/assets/admin/pages/scripts/ui-blockui.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-tabdrop/js/bootstrap-tabdrop.js" type="text/javascript"></script>

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<!--  Begin Theme Change -->
<script src="files/lib/metronic/theme/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/demo.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/pages/scripts/tasks.js" type="text/javascript"></script> 
<!--  End Theme Change -->
<!-- END PAGE LEVEL SCRIPTS --> 

<!-- <script src="files/lib/metronic/theme/assets/admin/layout3/scripts/layout.js" type="text/javascript"></script>-->
<!-- <script src="files/lib/metronic/theme/assets/admin/layout3/scripts/demo.js" type="text/javascript"></script> --> 

<script src="js/treeNodeHandler.js" type="text/javascript"></script> 

<script>
$(document).ready(function() {	
	 Metronic.init();
	 Layout.init();
	 Demo.init(); // init demo(theme settings page)
	 Tasks.initDashboardWidget(); // init tash dashboard widget	 
	 
	  $.sessionTimeout({
		 warnAfter: 3600000
 	 }); 
     $("input.date-picker").attr("readonly", "true").css({"background-color":"white","cursor":"default"});	 
     $("#treeContainerDiv").on("loaded.jstree", function(evt, data){
    	 treeNodeAttr();
     });
     $("#treeContainerDiv").on("open_node.jstree", function(evt, data){
    	 treeNodeAttr();
     });
     
    var menuworkpackageLable = "${planworkpackgesGridWorkPackagesHeaderMenu}";
    var menuProductLable ="${planworkpackgesGridProductHeaderMenu}";
    	
    		 // -- menu name changes through property file
    		 $("#CreateNewWorkPackage a").eq(0).text("Create New "+menuworkpackageLable);
    		 $("#WorkPackages a").eq(0).text(menuworkpackageLable); 
    		 $("#ProductManagement_SubMenuTab a").eq(0).text(menuProductLable+" Management");
 
});
function treeNodeAttr() {
	$.each($(".jstree-anchor"), function(ind, elem) {
		$(elem).removeAttr("title");
 		$(elem).attr("title", $(elem).text()); 
 		//console.log(elem);
  	});
}
$(document).tooltip({
	 track: true,
    position: {
      my: "center bottom-20",
      at: "center top",
      using: function( position, feedback ) {
        $( this ).css( position ).css("z-index", "100001");
        $( "<div>" )
          .addClass( "arrow" )
          .addClass( feedback.vertical )
          .addClass( feedback.horizontal )
          .appendTo( this );
      }
    }
  });
  
// $(document).ajaxStart(function() {
// 	 // console.log("start");
// 	  $("#loadingIcon").show();
// 	  $("body").addClass("loaderIcon");
// })
//   $(document).ajaxStop(function() {
// 	 // console.log("end");
// 	  $("body").removeClass("loaderIcon");
// 	  $("#loadingIcon").hide();
//   })

    /*** More-Less Starts ***/
$(document).ajaxStop(function() {
	var showChar = 20; // How many characters are shown by default

     var ellipsestext = "...";
     var moretext = "More >";
     var lesstext = "Less";

     $('.more1').each(function() {
    	 
         var content = $(this).html();
  
         if(content.length > showChar) {
  
             var c = content.substr(0, showChar);
             var h = content.substr(showChar, content.length - showChar);
  
             var html = c + '<span class="moreellipses">' + ellipsestext+ '&nbsp;</span><span class="morecontent"><span>' + h + '</span>&nbsp;&nbsp;<a href="" class="morelink">' + moretext + '</a></span>';
  
             $(this).html(html);
         }
  
     });
  
     $(".morelink").click(function(){
         if($(this).hasClass("less")) {
             $(this).removeClass("less");
             $(this).html(moretext);
         } else {
             $(this).addClass("less");
             $(this).html(lesstext);
         }
         $(this).parent().prev().toggle();
         $(this).prev().toggle();
         return false;
     });
 
});

$(document).ready(function() {
	window.history.forward();
	document.body.addEventListener('onload',function() { noBack(); },false);
	document.body.addEventListener('onpageshow',function() { if (event.persisted) noBack(); },false);
});

function noBack() { window.history.forward(); }

/*** More-Less Ends ***/

</script>
<div><%@include file="editorHelp.jsp"%></div>
</body>
</html>