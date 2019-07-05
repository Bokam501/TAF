/** JTABLE editinline extension
    by : nubuntu
**/
(function ($) {
	/* Begin : Modified to support request mode*/
	// Default - editInlineRowRequestMode is Row - false, and editInlineRequestMode if 'Column' is true;
	var editInlineRowRequestMode=false;
	var editInlineRowRequestModeDependsOn=false;
	
var base={
		_createCellForRecordField:$.hik.jtable.prototype._createCellForRecordField,
		_updateRowTexts:$.hik.jtable.prototype._updateRowTexts		
	//}
	};  // ----- modified -----

	$.extend(true, $.hik.jtable.prototype, {
		options: {
			editinline:{enable:false,img:""},
			editInlineRowRequestMode:false,
			editInlineRowRequestModeDependsOn:false,
	},

/** Overrides Method
*****************************/
_createCellForRecordField: function (record, fieldName) {
/*	if(this.options.editinline){
    	return $('<td></td>')*/
	if(this.options.editinline.enable && this.options.fields[fieldName].edit != false){    		
        return $('<td style="background:white"></td>')
        .addClass(this.options.fields[fieldName].listClass)
        .append((this._getDisplayTextEditInline(record, fieldName)));
    }else{
    	//return $('<td></td>')
    	return $('<td style="background:#eff3f8"></td>')  // ----- modified -----
        .addClass(this.options.fields[fieldName].listClass)
        .append((this._getDisplayTextForRecordField(record, fieldName)));

    }
},

/** Overrides Method
*****************************/
_updateRowTexts: function ($tableRow) {
	var record = $tableRow.data('record');
	var $columns = $tableRow.find('td');
	if(this.options.editinline){
		for (var i = 0; i < this._columnList.length; i++) {
			if(this.options.fields[this._columnList[i]].type=='date'){
				var displayItem = this._getDisplayTextForRecordField(record, this._columnList[i]);
				$columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
			}else{
				var displayItem = this._getDisplayTextEditInline(record, this._columnList[i]);
				$columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
			}
		}
	}else{
		for (var i = 0; i < this._columnList.length; i++) {
			var displayItem = this._getDisplayTextForRecordField(record, this._columnList[i]);
			$columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
		}
	}
	this._onRowUpdated($tableRow);
},
_getDisplayTextEditInline:function (record, fieldName) {
	var field = this.options.fields[fieldName];
	var fieldValue = record[fieldName];
	if (field.display) {
		var fieldIMG = field.display({ record: record }); // modified		
		if(fieldIMG.addClass != undefined){fieldIMG.addClass('showHandCursor');}			
		return fieldIMG;
		//return field.display({ record: record });
	}
	if (field.type == 'date') {
		return this._editInline_date(record,fieldName);
	} else if (field.type == 'checkbox') {
		//return this._editInline_checkbox(record, fieldName);
		return this._editInline_checkbox(record, fieldName,fieldValue);   // ----- modified -----
	} else if (field.type == 'textarea') {
		return this._editInline_textarea(record, fieldName);
	} else if (field.options) { //combobox or radio button list since there are options.
		var options = this._getOptionsForField(fieldName, {
			record: record,
			value: fieldValue,
			source: 'list',
			dependedValues: this._createDependedValuesUsingRecord(record, field.dependsOn)
		});
		return this._editInline_options(options, fieldValue,record,fieldName);
	} else { //other types
		//return this._editInline_default(record,fieldName);
		return this._editInline_default(record,fieldName,fieldValue);    // ----- modified -----
	}
},
_editInline_options: function (options, value,record,fieldName) {
	var self = this;
	var val = value;
	var valtext ='';
	var defaulttext ='';
	var $txt ='';
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}
	
	var optionsFieldTitle = this.options.fields[fieldName].title;;
	var valtextID='';
	
	if(!editInlineRowRequestMode){
		var $inputhtml = $('<select></select>');
		$inputhtml.css('background-repeat','no-repeat');
		$inputhtml.css('background-position','right center');
		
		for (var i = 0; i < options.length; i++) {
			$inputhtml.append('<option value="' + options[i].Value + '"' + (options[i].Value == value ? ' selected="selected"' : '') + '>' + (options[i].DisplayText == null ? "--" : options[i].DisplayText) + '</option>');
			if(options[i].Value == value){
				valtextID = options[i].Value;
				valtext = options[i].DisplayText;
				if(valtext == null) valtext = "--"; 
			}
		}
		defaulttext = (valtext) ? valtext :'---';
		$txt = $('<span>' + defaulttext + '</span>');
		$txt.click(function(){	
			if($(this).children().length < 1){		
				//$inputhtml.val(val);
				//$inputhtml.val(value);    // ----- modified -----
				$(this).html($inputhtml);
				$inputhtml.bind('change blur focusout',function(){
					$(this).css('background-image','url("' + self.options.editinline.img + 'loading.gif")');
					var postData = {};
					$("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);   // -----  modified added extra-----
					
					/*postData[fieldName]=$(this).val();
					postData[self._keyField]=record[self._keyField];
					if(self._editInline_ajax(postData)){*/
					
					record[fieldName]=$(this).val();    // ----- modified -----
					postData=record;
					postData[self._keyField]=record[self._keyField];
														
					// ------ modified -----
					postData["oldFieldID"] = valtextID;
					postData["oldFieldValue"] = valtext;
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldID"] = $(this).val();	
                    postData["modifiedFieldTitle"] = optionsFieldTitle;
                    
                    var num = this.selectedOptions[0].value;
					valtext = this.selectedOptions[0].text;
					postData["modifiedFieldValue"] = valtext;
					
					/*if(!isNaN(parseInt((this.selectedOptions[0].value), 10))){
						var num = this.selectedOptions[0].value;
						valtextID = num;
						valtext = this.options[num-1].text;
						postData["modifiedFieldValue"] = valtext;
						
					}else{
						valtextID = "";
						valtext = "";
						postData["modifiedFieldValue"] = "";						
					}*/
					
					/*valtextID = "";
					valtext = "";
					postData["modifiedFieldValue"] = "";*/					
                    					
					var oldValue=valtext;              
					var newValue=$(this).val();
					if(self._editInline_ajax(postData,$txt,oldValue,fieldName,newValue)){
						
						val = $(this).val();
						$txt.html($(this).find("option:selected").text());
						record[fieldName]=$(this).val();
						$(this).css('background','none');
						self._showUpdateAnimationForRow($txt.closest("tr"));
					}
				});
				$inputhtml.focus();
			}
		});
		return $txt;
		
	}else{
		var $inputhtml = $('<select></select>');
		$inputhtml.css('background-repeat','no-repeat');
		$inputhtml.css('background-position','right center');
		
		for (var i = 0; i < options.length; i++) {
			$inputhtml.append('<option value="' + options[i].Value + '"' + (options[i].Value == value ? ' selected="selected"' : '') + '>' + (options[i].DisplayText == null ? "--" : options[i].DisplayText) + '</option>');
			if(options[i].Value == value){
				//valtext = options[i].DisplayText;
				// ----- modified -----
				valtextID = options[i].Value;
				valtext = options[i].DisplayText;				
			}
		}
		defaulttext = (valtext) ? valtext :'---';
		$txt = $('<span>' + defaulttext + '</span>');
        $txt.click(function() {
            if ($(this).children().length < 1) {            	
                $inputhtml.val(val);
                $(this).html($inputhtml);
                $inputhtml.on('focus', function() {
                    prev = $(this).find("option:selected").text();
                }).change(function() {

                    $(this).css('background-image', 'url("' + self.options.editinline.img + 'loading.gif")');
                    var postData = {};

                    //postData[fieldName]=$(this).val();
                    postData[self._keyField] = record[self._keyField];
                    //postData["modifiedField"] = fieldName
                    //postData["modifiedFieldValue"] = $(this).val();
                    
                    // 	------ modified -----
					postData["oldFieldID"] = valtextID;
					postData["oldFieldValue"] = valtext;
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldID"] = $(this).val();	
                    postData["modifiedFieldTitle"] = optionsFieldTitle;
                    
                    var num = this.selectedOptions[0].value;
					valtext = this.selectedOptions[0].text;
					postData["modifiedFieldValue"] = valtext;
                    
                   /*if(!isNaN(parseInt((this.selectedOptions[0].value), 10))){
						var num = this.selectedOptions[0].value;
						valtextID = num;
						valtext = this.options[num-1].text;
						postData["modifiedFieldValue"] = valtext;
						
					}else{
						valtextID = "";
						valtext = "";
						postData["modifiedFieldValue"] = "";						
					}*/
                    
                   /* valtextID = "";
					valtext = "";
					postData["modifiedFieldValue"] = "";*/

                    var promise = self._editInline_ajax(postData,$txt,prev);
                    var that =this;
                   
                    promise.done(function() {
                        val = $(that).val();
                        $txt.html($(that).find("option:selected").text());
                        record[fieldName] = $(that).val();
                        $(that).css('background', 'none');
                        self._showUpdateAnimationForRow($txt.closest("tr"));
                    });

                    promise.fail(function() {
                        postData["modifiedFieldValue"] = $txt.html(prev);
                    });
                });
                $inputhtml.focus();
            }
        });
        return $txt;
	}		
},
_editInline_date:function(record,fieldName){
	var self = this;
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}
	
	var field = this.options.fields[fieldName];
	var fieldValue = record[fieldName];
	var displayFormat = field.displayFormat || this.options.defaultDateFormat;
	//var defaultval = (fieldValue) ? fieldValue :'&nbsp;&nbsp;&nbsp;';
	var defaultval = (fieldValue) ? fieldValue :'';
	var dataFieldTitle = this.options.fields[fieldName].title;
	var defaultvalFlag=false;
	
	if(!editInlineRowRequestMode){
		//var $txt = $('<span>' + defaultval + '</span>');
		var $txt = $('<span >' + setJtableFormattedDate(defaultval) + '</span>');	// ----- modified -----
		$txt.click(function(){
			if($(this).children().length < 1){
				var oldValue=$(this).html() ;
				var $inputhtml = $('<input readonly type="text" value="' + $(this).html() + '"/>');
				$inputhtml.css('background-repeat','no-repeat');
				$inputhtml.css('background-position','right center');
				$(this).html($inputhtml);
				
				//$inputhtml.datepicker({dateFormat:displayFormat,onClose: function(calDate) {
				$inputhtml.datepicker();   											// -----  modified added extra-----
				$inputhtml.change(function(calDate) {
					$(".datepicker").hide();
					$(this).css('background-image','url(' + self.options.editinline.img + 'loading.gif)');
				
					var postData = {};
					//postData[fieldName]=$(this).val();
					postData=record;
				
					postData[self._keyField] = record[self._keyField];
					record[fieldName] = $(this).val();
					
					// ------ modified -----
					postData["modifiedField"] = fieldName;
					postData["modifiedFieldTitle"] = dataFieldTitle;
					postData["modifiedFieldValue"] = $(this).val();
					if(!defaultvalFlag){
						postData["oldFieldValue"] = setJtableFormattedDate(defaultval);
						defaultvalFlag=true;
					}else{
						postData["oldFieldValue"] = defaultval;
					}
						
					fieldValue = record[fieldName];
					defaultval = (fieldValue) ? fieldValue :'';
					
					if(self._editInline_ajax(postData,$txt,oldValue,fieldName)){
						$txt.html($(this).val());
						//postData[fieldName]=$(this).val();
						record[fieldName] = $(this).val();
						$(this).css('background','none');
						self._showUpdateAnimationForRow($txt.closest("tr"));
						}
					});			
					$inputhtml.focus();
				}
			});
		return $txt;
	}else{
        var $txt = $('<span>' + defaultval + '</span>');
        $txt.click(function() {
            if ($(this).children().length < 1) {
                var $inputhtml = $('<input type="text" value="' + $(this).html() + '"/>');
                $inputhtml.css('background-repeat', 'no-repeat');
                $inputhtml.css('background-position', 'right center');
                $(this).html($inputhtml);
                $inputhtml.datepicker({
                    dateFormat: displayFormat,
                    onClose: function(calDate) {
                        $(this).css('background-image', 'url(' + self.options.editinline.img + 'loading.gif)');
                        var postData = {},
                            oldValue = record.fromDate;
                        //postData[fieldName]=$(this).val();
                        postData[self._keyField] = record[self._keyField];
                        postData["modifiedField"] = fieldName
                        postData["modifiedFieldTitle"] = dataFieldTitle;
                        postData["modifiedFieldValue"] = $(this).val();
                        
                        if(!defaultvalFlag){
    						postData["oldFieldValue"] = setJtableFormattedDate(defaultval);
    						defaultvalFlag=true;
    					}else{
    						postData["oldFieldValue"] = defaultval;
    					}                        
                        fieldValue = record[fieldName];
                        defaultval = (fieldValue) ? fieldValue :'';
                        
                        var promise = self._editInline_ajax(postData, $txt,oldValue);
                        var that=this;
                        promise.done(function() {
                            $txt.html($(that).val());
                            record[fieldName] = $(that).val();
                            $(that).css('background', 'none');
                            self._showUpdateAnimationForRow($txt.closest("tr"));
                        });
                        promise.fail(function() {
                            postData["modifiedFieldValue"] = $txt.html(oldValue);
                        });

                        /*if(self._editInline_ajax(postData)){
                        $txt.html($(this).val());
                        record[fieldName] = $(this).val();
                        $(this).css('background','none');
                        self._showUpdateAnimationForRow($txt.closest("tr"));
                        }*/
                    }
                });
                $inputhtml.focus();
            }
        });
        return $txt;	
	}
},

_editInline_checkbox:function(record,fieldName,fieldValue){
	var self = this;
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}	
	var checkBoxFieldTitle = this.options.fields[fieldName].title;;
	
	if(!editInlineRowRequestMode){
		var field = this.options.fields[fieldName];
		var fieldValue = record[fieldName];
		var $img = (fieldValue != 0) ? $('<img val="1" style="cursor:pointer; width:16px; height:16px;" src="' + this.options.editinline.img + 'css/images/right.jpg"></img>') : $('<img val="0" style="cursor:pointer; width:16px; height:16px;" src="' + this.options.editinline.img + 'css/images/crossmark.jpg"></img>');
		$img.click(function(){
			$("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);       // -----  modified added extra-----
			var postData = {};
			record[fieldName]=($(this).attr('val'))!=0?0:1;
			
			var newValue=record[fieldName];
			postData=record;
			
			// ------ modified -----
			postData["modifiedField"] = fieldName;
			postData["modifiedFieldValue"] = ($(this).attr('val')) != 0 ? 0 : 1;
			postData["oldFieldValue"] = ($(this).attr('val')) == 0 ? 0 : 1;
			postData["modifiedFieldTitle"] = checkBoxFieldTitle;
			
			var oldValue=fieldValue;
			if(self._editInline_ajax_checkbox(postData,$img,oldValue,fieldName,newValue)){				
			}
		});
		return $img;		
	}else
	{
		//var field = this.options.fields[fieldName];
        var fieldValue = record[fieldName];
        var $img = (fieldValue != 0) ? $('<img val="1" style="cursor:pointer; width:16px; height:16px;" src="' + this.options.editinline.img + 'css/images/right.jpg"></img>') : $('<img val="0" style="cursor:pointer; width:16px; height:16px;" src="' + this.options.editinline.img + 'css/images/crossmark.jpg"></img>');
        $img.click(function() {
            var postData = {};
            var oldValue = $(this).attr('val');
            //postData[fieldName]=($(this).attr('val'))!=0?0:1;

            postData[self._keyField] = record[self._keyField];
            postData["modifiedField"] = fieldName;
            postData["modifiedFieldValue"] = ($(this).attr('val')) != 0 ? 0 : 1;
            postData["oldFieldValue"] = ($(this).attr('val')) == 0 ? 0 : 1;
            postData["modifiedFieldTitle"] = checkBoxFieldTitle;

            var promise = self._editInline_ajax(postData,$img,oldValue);
            var that = this;
            promise.done(function() {
                if ($(that).attr('val') == '0') {
                    record[fieldName] = 1;
                    $(that).attr('title', 'click to uncheck');
                    $(that).attr('val', '1');
                    $(that).attr('src', self.options.editinline.img + 'css/images/right.jpg');
                } else {
                    record[fieldName] = 0;
                    $(that).attr('title', 'click to check');
                    $(that).attr('val', '0');
                    $(that).attr("src", self.options.editinline.img + 'css/images/crossmark.jpg');
                }
            });

            promise.fail(function() {
                postData["modifiedFieldValue"] = oldValue;
            });

            /*if(self._editInline_ajax(postData)){
            if($(this).attr('val')=='0'){
	            record[fieldName]=1;
	            $(this).attr('title','click to uncheck');
	            $(this).attr('val','1');
	            $(this).attr('src',self.options.editinline.img + 'css/images/right.jpg');
            }else{
	            record[fieldName]=0;
	            $(this).attr('title','click to check');
	            $(this).attr('val','0');
	            $(this).attr("src",self.options.editinline.img + 'css/images/crossmark.jpg');
	            }
	            self._showUpdateAnimationForRow($img.closest("tr"));
            }*/
        });
        return $img;
	}
},
_editInline_textarea:function(record,fieldName){	
	var self = this;
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}
	var field = this.options.fields[fieldName];
	var inlineTextAreaFieldTitle = field.title;
	
	var fieldValue = record[fieldName];
	var defaultval = (fieldValue) ? fieldValue :'&nbsp;&nbsp;&nbsp;';
	var $txt = $('<span>' + defaultval + '</span>');
	
	if(!editInlineRowRequestMode){
		$txt.click(function(){
			if($(this).children().length < 1){
				var $inputhtml = $('<textarea>' + $(this).html() + '</textarea>');
				$inputhtml.css('background-repeat','no-repeat');
				$inputhtml.css('background-position','right center');
				$(this).html($inputhtml);
				$inputhtml.bind('change blur focusout',function(){
					$(this).css('background-image','url("' + self.options.editinline.img + 'loading.gif")');
					$("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);     // -----  modified added extra-----				      
					var postData = {};
					record[fieldName]=$(this).val();
					postData=record;
					//postData[fieldName]=$(this).val();
					postData[self._keyField] = record[self._keyField];
					
					if($(this).val() == ''){
						$(this).val(this.defaultValue);
					}
					
					// ------ modified -----
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldValue"] = $(this).val();	
                    postData["modifiedFieldTitle"] = inlineTextAreaFieldTitle;
                    postData["oldFieldValue"] = this.defaultValue;
                    
					if(self._editInline_ajax(postData)){
						$txt.html($(this).val());
						record[fieldName]=$(this).val();
						$(this).css('background','none');
						self._showUpdateAnimationForRow($txt.closest("tr"));
					}
				});
				$inputhtml.focus();
			}
		});
		return $txt;
	}else
	{
        $txt.click(function() {
        	 var oldValue = $(this).attr('val');
            if ($(this).children().length < 1) {  
            	var $inputhtml = $('<textarea>' + $(this).html() + '</textarea>');
        		$inputhtml.css('background-repeat','no-repeat');
        		$inputhtml.css('background-position','right center');
        		$(this).html($inputhtml);
                $inputhtml.bind('change blur focusout', function() {
                    $(this).css('background-image', 'url("' + self.options.editinline.img + 'loading.gif")');
                    
                    var postData = {};
                    //postData[fieldName]=$(this).val();
                    
                    if($(this).val() == ''){
						$(this).val(this.defaultValue);
					}
                    
                    postData[self._keyField] = record[self._keyField];
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldValue"] = $(this).val();
                    postData["modifiedFieldTitle"] = inlineTextAreaFieldTitle;
                    postData["oldFieldValue"] = this.defaultValue;

                    if (self._editInline_ajax(postData,$txt,oldValue)) {
                        $txt.html($(this).val());
                        record[fieldName] = $(this).val();
                        $(this).css('background', 'none');
                        self._showUpdateAnimationForRow($txt.closest("tr"));
                    }
                });
                $inputhtml.focus();
            }
        });
        return $txt;
	}	
},
_editInline_default:function(record,fieldName,value){
	var self = this;
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}	
	
	var field = this.options.fields[fieldName];
	var inlineFieldTitle = field.title;
	
	if(!editInlineRowRequestMode){
		var fieldValue = record[fieldName];

		//Modified to display when 0 is coming as field value 
		var defaultval = (fieldValue) ? fieldValue :'&nbsp;&nbsp;&nbsp;';
		if(fieldValue === 0){
			defaultval = 0;
		}
		//var defaultval = (fieldValue) ? fieldValue :'&nbsp;&nbsp;&nbsp;';
		
		var $txt = $('<span>' + defaultval + '</span>');
		
		$txt.click(function(){
			$("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);
			if($(this).children().length < 1){
				var $inputhtml = $('<input type="text" value="' + $(this).html() + '"/>');
				$inputhtml.css('background-repeat','no-repeat');
				$inputhtml.css('background-position','right center');
				$(this).html($inputhtml);
				var oldValue=value;
				$inputhtml.bind('change blur focusout',function(){
					$(this).css('background-image','url("' + self.options.editinline.img + 'loading.gif")');
					
					var postData = {};				
					/*postData[fieldName]=$(this).val();
					postData[self._keyField] = record[self._keyField];
					if(self._editInline_ajax(postData)){*/
					
					if($(this).val() == ''){
						$(this).val(this.defaultValue);
					}
					record[fieldName]=$(this).val();			// ----- modified -----
					postData=record;
					
					// ------ modified -----
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldValue"] = $(this).val();
                    postData["modifiedFieldTitle"] = inlineFieldTitle;
                    postData["oldFieldValue"] = this.defaultValue;
					
					var newValue=$(this).val();
					if(self._editInline_ajax(postData,$txt,oldValue,fieldName,newValue)){
						$txt.html($(this).val());
						record[fieldName]=$(this).val();
						$(this).css('background','none');
						self._showUpdateAnimationForRow($txt.closest("tr"));
					}
					
				});
				$inputhtml.focus();
			}
		});
		return $txt;
	}else
	{
        var field1 = this.options.fields[fieldName][2];
        var field2 = this.options.fields[fieldName][3];
        var fieldValue = record[fieldName];
       
		//Modified to display when 0 is coming as field value
        var defaultval = (fieldValue) ? fieldValue :'&nbsp;&nbsp;&nbsp;';
    	if(fieldValue === 0){
    		defaultval = 0;
    	}
    	//var defaultval = (fieldValue) ? fieldValue : '&nbsp;&nbsp;&nbsp;';
    	
        var $txt = $('<span>' + defaultval + '</span>');
        $txt.click(function() {
        	 var oldValue = $(this).html();
            if ($(this).children().length < 1) {
                var $inputhtml = $('<input type="text" value="' + $(this).html() + '"/>');
                $inputhtml.css('background-repeat', 'no-repeat');
                $inputhtml.css('background-position', 'right center');
                $(this).html($inputhtml);
                $inputhtml.bind('change blur focusout', function() {
                    $(this).css('background-image', 'url("' + self.options.editinline.img + 'loading.gif")');
                    var postData = {};
                    //postData[fieldName]=$(this).val();
                    
                    if($(this).val() == ''){
						$(this).val(this.defaultValue);
					}

                    postData[self._keyField] = record[self._keyField];
                    postData["modifiedField"] = fieldName;
                    postData["modifiedFieldValue"] = $(this).val();
                    postData["modifiedFieldTitle"] = inlineFieldTitle;
                    postData["oldFieldValue"] = this.defaultValue;
					
                    //postData["fieldValue"]=$(this).val();

                    if (self._editInline_ajax(postData,$txt,oldValue)) {
                        $txt.html($(this).val());
                        record[fieldName] = $(this).val();
                        $(this).css('background', 'none');
                        self._showUpdateAnimationForRow($txt.closest("tr"));
                    }
                });
                $inputhtml.focus();
            }
        });
        return $txt;
	}	
},
_editInline_ajax_checkbox:function(postData,img,oldValue,fieldName,newValue){          // -----  modified added extra-----
	var self = this;
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}
	
	if(!editInlineRowRequestMode){
		var dataToSend_chkbox = postData;
		for (var i in dataToSend_chkbox) {
			if(i.indexOf("date") > -1 || i.indexOf("Date") > -1){		   
				if(dataToSend_chkbox[i] !== "" && dataToSend_chkbox[i] !== null){
					if(!(dataToSend_chkbox[i].indexOf("/") == 2)){
						dataToSend_chkbox[i] = setJtableFormattedDate(dataToSend_chkbox[i]);
					}
				}		   
			}
		}
		var res = true;
		$("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);
		this._ajax({
			//url: (self.options.actions.updateAction),
			url: (self.options.actions.editinlineAction),		
			//data: postData,
			data: dataToSend_chkbox,		
			success: function (data) {
				if (data.Result != 'OK') {
					noty({
						text: 'Failed: '+data.Message,
						layout: 'topRight',
						closeWith: ['click', 'hover'],
						type: 'success'						
					});
					if(oldValue==0){
						postData[fieldName]=0;
						img.attr('title','click to check');
						img.attr('val','0');
						img.attr("src",self.options.editinline.img + 'css/images/crossmark.jpg');
					}else{
						postData[fieldName]=1;
						img.attr('title','click to uncheck');
						img.attr('val','1');
						img.attr('src',self.options.editinline.img + 'css/images/right.jpg');
					}
					self._showUpdateAnimationForRow(img.closest("tr"));
					return res =false;
				}else{				
					if(newValue==0){
						postData[fieldName]=0;
						img.attr('title','click to check');
						img.attr('val','0');
						img.attr("src",self.options.editinline.img + 'css/images/crossmark.jpg');
					}else{
						postData[fieldName]=1;
						img.attr('title','click to uncheck');
						img.attr('val','1');
						img.attr('src',self.options.editinline.img + 'css/images/right.jpg');
					}
					self._showUpdateAnimationForRow(img.closest("tr"));
					
					//self._reloadTable();
					return res =true;
				}			
			},
			error: function() {
				noty({
					text: 'Record update failed',
					layout: 'topRight',
					timeout: 5000,
					closeWith: ['click', 'hover'],
					type: 'success'
				});
				//error(self.options.messages.serverCommunicationError);
				//  txt.html($(this).find("option:selected").text());
				if(oldValue==0){   
					postData[fieldName]=0;				
					img.attr('title','click to check');
					img.attr('val','0');
					img.attr("src",self.options.editinline.img + 'css/images/crossmark.jpg');
				}else{
					postData[fieldName]=1;
					img.attr('title','click to uncheck');
					img.attr('val','1');
					img.attr('src',self.options.editinline.img + 'css/images/right.jpg');
				}
				//$(this).css('background','none');
				//self._showUpdateAnimationForRow($txt.closest("tr"));
				return res=false; 
			}
		});
		return res;
	}else{
	  console.log("Not available");	
		
	}
},
_editInline_ajax:function(postData,txt,val,fieldName,newValue){
	var self = this;
	var res = true;   
	if(self.options.editInlineRowRequestMode!="" && self.options.editInlineRowRequestMode!=null){                                  
		   editInlineRowRequestMode = self.options.editInlineRowRequestMode;
	}
	
	if(self.options.editInlineRowRequestModeDependsOn!="" && self.options.editInlineRowRequestModeDependsOn!=null){                                  
		editInlineRowRequestModeDependsOn = self.options.editInlineRowRequestModeDependsOn;
	}
   
   if(!editInlineRowRequestMode){
		var dataToSend = postData;
		
		/*var apprPage = false;
		for (var i in dataToSend) {
	   	if(i.indexOf("date") > -1 || i.indexOf("Date") > -1){
		   	console.log(i.toLowerCase());
		   	if(i.toLowerCase().indexOf("approved") > -1)apprPage = true;
	   }
		}*/
	   for (var i in dataToSend) {
		   if(i.indexOf("date") > -1 || i.indexOf("Date") > -1){		   
			   if(dataToSend[i] !== "" && dataToSend[i] !== null){
				   
				   if(!((dataToSend[i]+"").indexOf("/") == 2)){
					   dataToSend[i] = setJtableFormattedDate(""+dataToSend[i]);
				   }
			   }		   
		   }
	   }
	   
	   $("#jTableContainer").find(".jtable-selecting-column > input").prop("checked", false);
	   this._ajax({
		   //url: (self.options.actions.updateAction),
		   url: (self.options.actions.editinlineAction), 
		   data: dataToSend,
		   success: function (data) {
			   if (data.Result != 'OK') {
				   noty({
					   text: 'Failed: '+data.Message,
					   layout: 'topRight',
					   closeWith: ['click', 'hover'],
					   type: 'success'
						   
				   });
				   if(val==null){
					   val='---';
				   }
				   postData[fieldName]=txt.html(val); // modified 1---
				   postData[fieldName]=val;
				   
				   // ------ modified -----
				  postData["modifiedField"] = fieldName;
				  postData["modifiedFieldValue"] = txt.html(val);
				   
				   $(this).css('background','none');
				   self._showUpdateAnimationForRow(txt.closest("tr"));
				   //self._reloadTable();
				   return res =false;
			   }else{
				   //self._trigger("rowUpdated", null, { postData: txt.html(newValue)});
				   //self._jtable('selectedRows');
				   //var selectedRow= self._getSelectedRows();
			   }
			   /*self._trigger("recordUpdated", null, {});
				     updateTrccExecutionPlanDetail();
					noty({
					  text: 'Record updated successfully',
					  layout: 'topRight',
					  closeWith: ['click', 'hover'],
					  type: 'success'
					  
					});
				   this._trigger("recordUpdated", null, { record: $row.data('record')});
			   */
			   if(editInlineRowRequestModeDependsOn)
			   	   self._trigger("recordUpdated", null, {});
		   },
		   error: function() {
			   noty({
				   text: 'Record update failed',
				   layout: 'topRight',
				   timeout: 5000,
				   closeWith: ['click', 'hover'],
				   type: 'success'
			   });
			   //error(self.options.messages.serverCommunicationError);
			   //txt.html($(this).find("option:selected").text());
	
			   if(val==null){
				   val='---';
			   }
			   postData[fieldName]=txt.html(val);
			   $(this).css('background','none');
			   self._showUpdateAnimationForRow(txt.closest("tr"));
			   return res=false; 
		   }
	   }); 
	   return res; 
   }else{
	      var deferred = new $.Deferred();
	       this._ajax({
	    	   url: (self.options.actions.editinlineAction + ".inline"),
		       data: postData,
		       success: function(data) {
		           if (data.Result != 'OK') {
		               noty({
		                   text: 'Failed : '+data.Message,
		                   layout: 'topRight',
		                   closeWith: ['click', 'hover'],
		                   type: 'success'
		
		               });
		               postData["modifiedFieldValue"] = txt.html(val);
		               deferred.reject();
		               //return res =false;
		           }else if(data.Result=="OK"){
		   			iosOverlay({
		   				text: "Saved", // String
		   				icon: "css/images/check.png", // String (file path)
		   				spinner: null,
		   				duration: 1500, // in ms
		   				});
		   		  }		           
		           /*self._trigger("rowUpdated", null, {
		               row: $row,
		               record: $row.data('record')
		           });*/
		           deferred.resolve();
		       },
		       error: function() {
		           noty({
		               text: 'Record update failed',
		               layout: 'topRight',
		               timeout: 5000,
		               closeWith: ['click', 'hover'],
		               type: 'success'
		           });
		           postData["modifiedFieldValue"] = txt.html(val);
		           //error(self.options.messages.serverCommunicationError);
		           //return res=false;
		           deferred.reject();
		       }
		   });	       
	       return deferred.promise();
   } 
}
});	
})(jQuery);