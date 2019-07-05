<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>

<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%><html xmlns="http://www.w3.org/1999/xhtml"><head>
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />

<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/black/jtable.min.css" rel="stylesheet" type="text/css" />
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--[if IE]>
<style type="text/css" media="screen">
.shell {background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/shell-bg.png', sizingMethod='scale');}
.box{background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/dot.png', sizingMethod='scale');}
.transparent-frame .frame{background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/transparent-frame.png', sizingMethod='image');}
.search .field{padding-bottom:9px}
</style>
<![endif]-->
<!-- <script src="js/modernizr-2.6.2.js" type="text/javascript"></script> -->
    <script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
<script src="js/jquery-func.js" type="text/javascript"></script>
<!-- the mousewheel plugin -->
		<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
		<!-- the jScrollPane script -->
		<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>
<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>
<script type="text/javascript">
function setErrorMessage(msg){
	if(msg==null || msg==''){
		document.getElementById("errormessage").innerHTML="<br>"; 
	}else{
		document.getElementById("errormessage").innerHTML=msg; 
	}
}
  function validateAndSubmit()
{
   var type=document.forms["frmLogin"]["entityType"].value;
var id=document.forms["frmLogin"]["entityId"].value;
if (type==null || type=="" || id==null || id==""){
     setErrorMessage("ALL FIELDS REQUIRED!") 
  }else{
	  
	  document.forms["frmLogin"].submit();  
  }
	 
return true;
} 
 
</script>
</head>
<body onload="setErrorMessage('${message}')" style="overflow:hidden">

<!-- Shell -->
<div class="shell" style="height:580px; position:relative;">
<!-- Content -->
  <div id="content" style="width:100%; margin:0 auto; background:url(images/shell-bg.png) repeat-y; height:0px; display:block;">
  <!-- Header -->
  <div id="header">
    <!-- Logo -->
    <h1 id="logo"><a href="#">Test Automation Framework</a></h1>
    <!-- End Logo -->
    <!-- Navigation -->
    <div id="nav">
      <ul>
        <li><a class="active" href="home">Home</a></li>
        <li><a href="administration">Administration</a></li>
        <li><a href="test" href="test">Test</a></li>
        <li><a href="report" href="report">Report</a></li>
        <!--Enable once the feature is implemented -->        
		<!--<li><a href="#">Configuration</a></li>-->
      
      </ul>
    </div>
    <!-- End Navigation -->
  </div>
  <!-- End Header -->
  
  <!-- Sidebar -->
   <div id="loginbar1" style="display:block;float:center; margin-right:100px; margin-top:-180px;">
     <div class="box1" style="padding: 200px 0px 0px 360px;"> 
       <p style="padding-left: 84px;font-size: 20px;"><strong>Reactivate</strong></p>
       
            <%-- <form:form action="reactivateEntities" commandName="productName" class="textbox" method="post" name="frmLogin"> --%>
            <form action="reactivateEntities"  class="textbox" method="post" name="frmLogin">
            <ul>
             <li>
              <div class="info1">
              <div class="cl">&nbsp;</div>
               <h5 class="field1 blink"><a href="#">Entity Type :
                <%-- <form:select path="productName"> 
                <form:option value="-" label="--Select Product Name">
                <c:forEach var="productName" items="${productName}"> 
               <form:options items="${productMaster}" itemValue="productId" itemLabel="productName"></form:options></form:option></form:select> --%> 
               <select name="entityType" id="entityType">
               <c:forEach var="productMaster" items="${productMaster}">

                	<option value="productId">${productMaster}</option>

            	</c:forEach>
            	</select>
               <%-- <option value="Product"><c:out value="${productMaster}"></c:out></option> --%>
                 <%-- <c:out value="${productMaster}"></c:out>  --%>
              </div>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
             </li>
      		  <br>
      		 <li>
      		  <div class="info1">
      		   <h5 class="field2 blink" style="padding-right: 20px"><a href="#">Entity Id :  
               <input name="entityId" id="entityId" type="text" value="" title="" style="padding-left: 16px"></h5></a>
              </div>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
              <div>&nbsp;</div>
             </li>
              <li><div id="errormessage" align="center" style="color:#ff0000;"></div></li>
             <li>
			  <div class="links1">
        		<div class="cl">&nbsp;</div> 
        		<a href="#" class="left" style="margin-left:20px">Close</a>
        		<a href="#" class="right" style="margin-right:20px" onclick="validateAndSubmit()">Reactivate</a>
        		<div class="cl">&nbsp;</div>
     		  </div>
     		  
             </li>
            </ul>
            <%-- </form:form> --%>
            </form>
            <div align="center" style="font-size: 15px"><strong>${Message}</strong></div>
            </div>
            <!-- End Box -->
            </div>
            <!-- End Sidebar -->
            </div>
     
            <!-- End Content -->
            
  			<div id="line" style="position:absolute;bottom:52px"></div>
  
 		    <!-- Footer -->
 			<div id="footer">
  			  <div align="left" style="float:left; margin-left:20px">${user}</div><div align="right" style="float:right"><a href="j_spring_security_logout">LogOut</a></div>    
  			</div>
  			<!-- End Footer -->
  			</div>
  			<!-- End Shell -->
  			<div align="center">© Copyright 2011 - 2013<a href="http://www.hcl.com">  HCL Technologies Ltd.</a></div>


	</body>
</html>