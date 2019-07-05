<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link href="files/lib/metronic/theme/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css" />

</head>
<body>
	 <div class="skin skin-flat">
		<form role="form">
			<div class="form-body">									
				<div class="form-group">													
					<div class="input-group">
					<div id="role_types1" class="icheck-list">														
					  </div>	 
					</div>
				</div>
			</div>
			<div class="form-actions">
			<!--<button type="button"  onclick="changeUserProfile();popupCloserolechange();" class="btn green-haze" >Submit</button>-->
		<div><button type="button" onclick="submitRadioBtnHandler();" class="btn green-haze" style="padding: 6px 12px;" >Submit</button></div>
			<div>	<button type="button" class="btn grey-cascade"  onclick="popupCloserolechange()">Cancel</button></div>
			</div>
		</form>
	</div>
</body>
</html>