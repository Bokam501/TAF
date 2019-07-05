/**
 * Ajax.js
 *
 * Collection of Scripts to allow in page communication from browser to (struts) server
 * ie can reload part instead of full page
 *
 * How to use
 * ==========
 * 1) Call xmlreqPOST from the relevant event on the HTML page (e.g. onclick)
 * 2) Pass the url to contact (e.g. Struts Action) and the name of the HTML form to post
 * 3) When the server responds ...
 *		 - the script loops through the response , looking for <div id="name">newContent</div>
 * 		 - each <div> tag in the *existing* document will be replaced with newContent
 *
 * NOTE: <div id="name"> is case sensitive. Name *must* follow the first quote mark and end in a quote
 *		 Everything after the first '>' mark until </div> is considered content.
 *		 Empty Sections should be in the format <span id="name"></div>
 */

//global variables

// Create an array to hold the request objects

var xmlreqs = new Array();

function CXMLReq(freed)
{
	this.freed = freed;
	this.xmlhttp = false;
	if (window.XMLHttpRequest)
	{
		this.xmlhttp = new XMLHttpRequest();
	}
	else if (window.ActiveXObject)
	{
		this.xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function xmlreqPOST(url,data)
{
    var date = new Date();
    var timestamp = date.getTime();
    var ques=url.indexOf('?');
   	if(ques!=-1){
   	url=url+"&time="+timestamp;
   	}else{
   		url=url+"?time="+timestamp;
	}
	var pos = -1;
	for (var i=0; i<xmlreqs.length; i++)
	{
		if (xmlreqs[i].freed == 1)
		{
			pos = i; break;
		}
	}
	document.getElementById(data).innerHTML="<div align='center' id='loader'><img src='images/indicator.gif'/></div>";

	if (pos == -1)
	{
		pos = xmlreqs.length;
		xmlreqs[pos] = new CXMLReq(1);
	}
	if (xmlreqs[pos].xmlhttp)
	{
		xmlreqs[pos].freed = 0;
		xmlreqs[pos].xmlhttp.open("POST",url,true);
		xmlreqs[pos].xmlhttp.onreadystatechange = function()
		{
			if (typeof(xmlhttpChange) != 'undefined')
			{
				var temp=xmlhttpChange(pos,data);
				return temp;
			}
		};
		xmlreqs[pos].xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xmlreqs[pos].xmlhttp.send(data);
	}
}

function xmlhttpChange(pos,data)
{
	if (typeof(xmlreqs[pos]) != 'undefined' && xmlreqs[pos].freed == 0 && xmlreqs[pos].xmlhttp.readyState == 4)
	{
		if (xmlreqs[pos].xmlhttp.status == 200 || xmlreqs[pos].xmlhttp.status == 304)
		{
			//handle_response(xmlreqs[pos].xmlhttp.responseXML);
			//alert("Inside"+xmlreqs[pos].xmlhttp.responseText);
			//alert("data>>"+data);
			 //document.getElementById(data).innerHTML = xmlreqs[pos].xmlhttp.responseText;
		return  xmlreqs[pos].xmlhttp.responseText;
		}
		else
		{
			//handle_error();
		}
		xmlreqs[pos].freed = 1;
	}
}


//Coding for Add New Build Upload Progress Status - Starts
function xmlreqBuildUploadPOST(url,data)
{
    var date = new Date();
    var timestamp = date.getTime();
    var ques=url.indexOf('?');
   	if(ques!=-1){
   	url=url+"&time="+timestamp;
   	}else{
   		url=url+"?time="+timestamp;
	}
	var pos = -1;
	for (var i=0; i<xmlreqs.length; i++)
	{
		if (xmlreqs[i].freed == 1)
		{
			pos = i; break;
		}
	}
	//document.getElementById(data).innerHTML="<div align='left' id='loader'>&nbsp;&nbsp;<img src='images/indicator.gif'/></div>";

	if (pos == -1)
	{
		pos = xmlreqs.length;
		xmlreqs[pos] = new CXMLReq(1);
	}
	if (xmlreqs[pos].xmlhttp)
	{
		xmlreqs[pos].freed = 0;
		xmlreqs[pos].xmlhttp.open("POST",url,true);
		xmlreqs[pos].xmlhttp.onreadystatechange = function()
		{
			if (typeof(xmlhttpBldUpldChange) != 'undefined')
			{
				return xmlhttpBldUpldChange(pos,data);
			}
		};
		xmlreqs[pos].xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xmlreqs[pos].xmlhttp.send(data);
	}
}

function xmlhttpBldUpldChange(pos,data)
{
	var myJSONtext;
	var myJSONobj;
	if (typeof(xmlreqs[pos]) != 'undefined' && xmlreqs[pos].freed == 0 && xmlreqs[pos].xmlhttp.readyState == 4)
	{
		if (xmlreqs[pos].xmlhttp.status == 200)
		{			
			
			myJSONtext =xmlreqs[pos].xmlhttp.responseText;
			//alert("myJSONtext>>>"+myJSONtext);
			//myJSONobj = eval("(" + myJSONtext + ")");	
			document.getElementById(data).value=myJSONtext;
			//alert("myJSONobj>>>"+myJSONobj);
			setTimeout(tmpFun(),5000);
		}		
		xmlreqs[pos].freed = 1;
	}	
	 
}