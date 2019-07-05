/*
This script enables twisties and show hide for RoboHelp output.
Created by Willam van Weelden on 18-03-2010, updated on 12-05-2010 and 08-05-2013
This script uses tecniques created by Lewis Gault, Greg Swearingen and Paul Symonds.

=== What does this script do? ===

This script supports:
	* Twisties only
	* Two button ShowHide only
	* One button ShowHide only
	* Two button ShowHide with twisties
	* One button ShowHide with twisties
	
The default ShowHide mode is the one button method. You can turn this of with the variable OneButtonMethod.

=== Setting up the script ===

First step is, ofcourse, to add the script in the header section of your topic.

Twisties:
	1. Set the filenames of the twisty images you want to use in the variables TwistyHidden and TwistyShow
	2. Add the TwistyShow image as a child element of a link with a dropdown or expandspot. 
		Correct: <a href=""><img /></a> 
		Incorrect: <a href=""></a><img />
	3. Set the onclick for the link as follows: "swapImages(this, '<name of the image>')"
	4. Done

ShowHide Two button method:
	1. Set the variable OneButtonMethod to 'false'
	2. Add a ShowAll button (image) to your topic and add the onclick "ShowAll(this)"
	3. Add a HideAll button (image) to your topic and add the onclick "HideAll(this)"
	4. Done
	
ShowHide One button method:
	1. Set the variable OneButtonMethod to 'true' (this is default)
	2. Add the names of the images you want to use for the ShowAll/HideAll buttons in the variables ShowAllButton and HideAllButton
	3. Add a ShowAll button (image) to your topic and add the onclick "ShowAll(this)"
	4. Done

ShowHide with Twisties:
	Create twisties and set the ShowHide button. No extra settings needed.

*/
//Settings for the script.
var TwistyHidden = "arrowright.gif";//The image you use for the invisible dropdown. Only the filename is needed.
var TwistyShow = "arrowdown.gif";//The image you use for the visible dropdown. Only the filename is needed.
var OneButtonMethod = true; //Sets if you want to use a one button method. True or false. If true, also set ShowAllButton and HideAllButton.
var ShowAllButton = "btnshowall.gif"; //The image you use for the show all button.
var HideAllButton = "btnhideall.gif"; //The image you use for the hide all button.
var RH8orup = true; //Set to 'true' if you use RoboHelp 8 or higher. Set to 'false' if you're using RH7 or lower.
//Don't change anything below, unless you know what you're doing.

//Determine the browser. Needed to make the twisties compatible with Firefox.
if(typeof(navigator) != "undefined") {/* 20130508: Added check on navigator. IE may give undefined. */
	if(navigator.userAgent.indexOf("MSIE")!=-1) {
		var Browser = "IE";
	} else {
		var Browser = "undetermined"; //Only IE and Firefox need specific support. Don't bother with other browsers.
	}
} else {
	var Browser = "IE";
}/* 20130508: Removed FF specific code because it breaks new versions of FF. Leaving the links as they are works fine in the latest FF. */

var Links = document.getElementsByTagName('a');
var Divs = document.getElementsByTagName('div');
var Spans = document.getElementsByTagName('span');

var DropdownClass = "dropspot";
var DropdownDivClass = "droptext";
var ExpandLinkClass = "expandspot";
var ExpandClass = "expandtext";
var newHref = "javascript:void(0);"

//Swap twisties.
function swapImages(elem, imageName) 
{
	//Check if the dropdown is visible or not. Needed to stay in sync when using show all and hide all buttons.
	var DivId = getDivId( elem.id )
	if(DivId != false) //There is a DivId. There must allways be a div id. This if is just in case there isn't an id. The script won't produce an error.
	{
		if(document.getElementById(DivId).style.display == "none")
			Check = "show";
		else
			Check = "hide";
		
		if(RH8orup == false)
			imageName = document.getElementsByName(imageName)[0];
		
		//Call the function to set the twity. This is a separate function that the function ShowHide can call directly.
		Twisty (Check, imageName)	
	}
}

//Show and Hide all function. You can directly call ShowHide, but the one button methon will not work.
//I kept this method because it is easy to implement and is backward compatible with earlier versions of this script.
function HideAll( button )
{
	var action = "show";
	ShowHide( action, button )
}
function ShowAll( button )
{
	var action = "hide";
	ShowHide( action, button )
}
	
function ShowHide( action, button )
{
	//Make all dropspots visible
	if(Divs != null)
	{
		for(var i in Divs)
		{
			var ToggleDiv = false;
			if(Divs[i].className == DropdownDivClass)
			{
				if(Divs[i].style.display == "none" && action == "show")
				{
					ToggleDiv = true;
					var TwistyAction = "show";
				}
				else if (Divs[i].style.display != "none" && action == "hide")
				{
					ToggleDiv = true;
					var TwistyAction = "hide";
				}
					
			}
			if(ToggleDiv == true)
			{
				//This is the long way home to directly setting the display style. For maximal compatibility
				var LinkId = getLinkId(Divs[i].id) //This is the link corresponding with the dropdown
				//Call kadovTextPopup instead of TextPopup directly to create maximum backward compatibility
				kadovTextPopup(document.getElementById(LinkId)) //Call TextPopup to toggle the dropdown.
				GetTwisty(LinkId, TwistyAction)
			}
		}
	}
	
	//Make all expandspots visible
	if(Spans != null)
	{
		for(var i in Spans)
		{
			var ToggleSpan = false;
			if(Spans[i].className == ExpandClass)
			{
				if(Spans[i].style.display == "none" && action == "show")
				{
					ToggleSpan = true;
					var TwistyAction = "show";
				}
				else if(Spans[i].style.display != "none" && action == "hide")
				{
					ToggleSpan = true;
					var TwistyAction = "hide";
				}
			}
			
			if(ToggleSpan == true)
			{
				var LinkId = getLinkId(Spans[i].id) //This is the link corresponding with the expandspot
				//Call kadovTextPopup instead of TextPopup directly to create maximum backward compatibility
				kadovTextPopup(document.getElementById(LinkId)) //Call TextPopup to toggle the expandspot.
				GetTwisty(LinkId, TwistyAction)
			}
		}
	}
	
	if(OneButtonMethod == true)
		OneButtonSwitch( button )
}

//Turn twisties
function Twisty (action, image)
{
	if(action == "show")
	{
		matchpattern = new RegExp(TwistyHidden);
		var Original = TwistyHidden;
		var New = TwistyShow;
	}
	else if(action == "hide")
	{
		matchpattern = new RegExp(TwistyShow);
		var Original = TwistyShow;
		var New = TwistyHidden;
	}
	
	//Now check the twisty
	if (matchpattern.test(image.src)) 
	{
		//Replace image name leaving path intact
		newName = replaceSubstring(image.src, Original, New) 
		image.src=newName;
	}
}

//Get the link element of a dropdown or expandspot
function getLinkId ( elemid )
{
	var Id = false;
	try{
		for(var j=0;j<gPopupData.length;j++)
		{
			linkAttribute=gPopupData[j].popupId;
			linkAttribute = linkAttribute.substr(1, linkAttribute.length-1);
			if(linkAttribute == elemid)
				Id = gPopupData[j].el;
		}
		if(Id == false)//The regular method did not return an Id. Use alternate method of getting the Id.
		{
			for(var j=0;j<=Links.length;j++)
			{
				var CorrespondingDiv = getDivId(Links[j].id);//Getting the div the link triggers.
				if(CorrespondingDiv == elemid)//If elemid matches the DivId of this link, we need this link.
					var Id = Links[j].id;
			}
		}
		return Id;
	}
	catch(err)
	{
		return Id; //Silently swallow exceptions.
	}	
}
//Get the dropdown or expandspot element of a link
function getDivId ( elemid )
{
	var Id = false;
	try{
		for(var j=0;j<gPopupData.length;j++)
		{
			linkAttribute=gPopupData[j].el;
			if(linkAttribute == elemid)
			{
				PopupId = gPopupData[j].popupId;
				PopupId = PopupId.substr(1, PopupId.length-1);
				Id = PopupId
			}
		}
		
		if(Id == false)//The regular method did not return an Id. Use alternate method of getting the Id. Script comes from RH7 ehlpdhtm.js and is directed at RH7 CHM
		{
			var el = document.getElementById(elemid);
					
			if( typeof(el) == "string" )
				el = getElement(el);
				
			var src = el.getAttribute( "x-use-popup" );
			var name = src;
			if( src.substr(0,1) == "#" )
			{
				name = src.substr(1, src.length-1) + "_tmp";	
			}
			var srcDiv = getElement(name);
			Id = srcDiv.id;
		}
		
		return Id;
	}
	catch(err)
	{
		return Id;//Silently swallow exceptions.
	}	
}
//Get the twisty of a link
function GetTwisty(LinkId, action)
{
	//Get all the images that are children of the link. This is normally only the twisty.
	//Images in the rest of the paragraph or in the dropdown are excluded.
	var Images = document.getElementById(LinkId).getElementsByTagName("img");
	if(Images != null)
	{
		for(var i in Images)
		{
			matchpattern = new RegExp(TwistyHidden);
			matchpattern2 = new RegExp(TwistyShow);
			//If the image uses the same name as the link, assume it is the twisty and set the twisty.
			if(matchpattern.test(Images[i].src) || matchpattern2.test(Images[i].src))
				Twisty(action, Images[i])
		}
	}
}
//Replace strings
function replaceSubstring(inputString, fromString, toString) 
{
	// Goes through the inputString and replaces every occurrence of fromString with toString
	var temp = inputString;
	
	if (fromString == "") 
	{
		return inputString;
	}
	
	if (toString.indexOf(fromString) == -1) 
	{ 
		while (temp.indexOf(fromString) != -1) 
		{
			 var toTheLeft = temp.substring(0, temp.indexOf(fromString));
			 var toTheRight = temp.substring(temp.indexOf(fromString)+fromString.length, temp.length);
			 temp = toTheLeft + toString + toTheRight;
		}
	}
	// Send the updated string back to the user
	return temp; 
}
//Switch the button
function OneButtonSwitch ( elem )
{
	var ButtonTst = new RegExp(ShowAllButton);
		
	if(ButtonTst.test(elem.src))
	{
		//Replace image name leaving path intact
		newName = replaceSubstring(elem.src, ShowAllButton, HideAllButton)
		elem.src=newName;
		
		if(Browser == "IE")
			elem.onclick= function () { HideAll(this);}; //IE <= 8 does not support setAttribute
		else
			elem.setAttribute("onclick", "HideAll(this)");
	}
	else
	{
		newName = replaceSubstring(elem.src, HideAllButton, ShowAllButton) 
		elem.src=newName;
		if(Browser == "IE")
			elem.onclick= function () { ShowAll(this);}; //IE <= 8 does not support setAttribute
		else
			elem.setAttribute("onclick", "ShowAll(this)");
	}
}