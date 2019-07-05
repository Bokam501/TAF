
function hideMenuItems(menu,submenu,visible){
	if(visible == false){
		var subMenuList = [];
		var len = $("#"+menu).find('ul li').length;
		subMenuList = $("#"+menu).find('ul li'); 
		var mainMenu = $("#"+menu);
		if(submenu == ""){
			$(mainMenu).hide();
		}
		else{
			for(var i=0;i<len;i++){
		
				var sebMeuID = $(subMenuList[i]).find('a').text();
				if(myfunTrim(sebMeuID) == myfunTrim(submenu)){
					//console.log(i, " match ", submenu);
					$(subMenuList[i]).hide();
				}else{
					//console.log(i, " === ", sebMeuID);
			    }
		  }
		}
	}
}

function loadjscssfile(filename, filetype){
    if (filetype=="js"){          //if filename is a external JavaScript file
        var fileref=document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src", filename);
    }
    if (typeof fileref!="undefined")
        document.getElementsByTagName("head")[0].appendChild(fileref)
}


 

