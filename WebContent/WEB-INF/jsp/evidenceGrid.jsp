<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- <script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script> -->
</head>
<body>
	<div id="evidenceContainer" class="modal " tabindex="-1" aria-hidden="true" style="z-index:10051;width:95%;left:2%;top:2%;padding:17px;">
		<div class="modal-dialog" style="width:100%;margin-top:0px">
			<div class="modal-content">
			    
				<div class="modal-header" style="padding:0px">
				    <div class="page-header-top">
						<div class="container container-position" style="display: -webkit-box;">		
						<div class="page-logo page-logo-position logo headerLogo" style="margin:0px;cursor: default;width:98%" title="">
							<p class="logoTitle" style="margin:0px 0px -7px 0px;color:#55616f !important"></p>
							<p class="logoSubTitle" style="margin:0px;"></p>
						</div>						
						<div class="top-menu xpull-right" style="padding-top:18px;">
							<button type="button" class="close" data-dismiss="modal" title="close" aria-hidden="true"></button>							
						</div>
					</div>
				</div>
					<div id="evidencesubTitle" class="page-footer" style="padding: 5px 10px;">						
						<h4 class="modal-title"></h4>
						<h5 class="modal-title"></h5>
					</div>
				</div>
				<div class="modal-body" xstyle="max-height: 405px">				
					<div id="evidenceReport"></div>														
				</div>				
				<div class="page-footer" style="position: relative; bottom: 0px; margin: 0px; width: 100%;z-index:1;">
					<div class="container" style="margin-left:0px;text-align:center;">© Copyright 2014 - 2019<a href="http://www.hcl.com">  HCL Technologies Ltd.</a>
					&nbsp; &nbsp; <span>This website is best viewed in Google Chrome.</span>
					</div>
				</div>
				
				</div>
			</div>
		</div>
		
				
<script type="text/javascript">
	$(document).ready(function(){
		// ----- mode change iLCM / TAF -----
		changeModeInGrid();	
	});
	
	function changeModeInGrid(){
		if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoTitle').text(title_ilcm);
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ilcm);			
			$('#evidenceContainer > .page-header-top > .container >.headerLogo').eq(1).attr('title',tooltip_ilcm);
			
		}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){			
			// ---- loadign TAF logo image file in the header part -----
			/* var temp = '<img src="css/images/taf_logo.png" style="height:40px;margin-top:6px;" alt="hclLogo" title="Test Automation Framework">;'
			$('.page-header-top > .container >.headerLogo').eq(1).empty();
			$('.page-header-top > .container >.headerLogo').eq(1).append(temp); */
			
			// ---- loadign TAF logo text font in the header part -----
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoTitle').text(title_taf);
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoSubTitle').text(subTitle_taf);
			$('#evidenceContainer > .page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_taf);
			
		}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){			
			
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoTitle').text(title_ATLAS);
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ATLAS);
			$('#evidenceContainer > .page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_ATLAS);
			
		}else if(modeSelection.toLowerCase() == title_bot.toLowerCase()){			
			
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoTitle').text(title_bot);
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoSubTitle').text(subTitle_bot);
			$('#evidenceContainer > .page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_bot);
			
		}else{
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoTitle').text(title_ilcm);
			$('#evidenceContainer > .page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ilcm);			
			$('#evidenceContainer > .page-header-top > .container >.headerLogo').eq(1).attr('title',tooltip_ilcm);
		}
	}
</script>			

<script type="text/javascript" src="js/evidenceGrid.js"></script>

</body>
</html>