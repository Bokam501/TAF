/** JTABLE editinline extension
by : nubuntu
**/
(function($) {
    var base = {
        _createCellForRecordField: $.hik.jtable.prototype._createCellForRecordField,
        _updateRowTexts: $.hik.jtable.prototype._updateRowTexts
    }
    $.extend(true, $.hik.jtable.prototype, {
        options: {
            editinline: {
                enable: false,
                img: ""
            }
        },
        /** Overrides Method
         *****************************/
        _createCellForRecordField: function(record, fieldName) {
            if (this.options.editinline.enable && this.options.fields[fieldName].edit != false) {
                return $('<td style="background:white"></td>')
                    .addClass(this.options.fields[fieldName].listClass)
                    .append((this._getDisplayTextEditInline(record, fieldName)));
            } else {
                return $('<td style="background:lightgrey"></td>')
                    .addClass(this.options.fields[fieldName].listClass)
                    .append((this._getDisplayTextForRecordField(record, fieldName)));
            }
        },
        /** Overrides Method
         *****************************/
        _updateRowTexts: function($tableRow) {
            var record = $tableRow.data('record');
            var $columns = $tableRow.find('td');
            if (this.options.editinline) {
                for (var i = 0; i < this._columnList.length; i++) {
                    if (this.options.fields[this._columnList[i]].type == 'date') {
                        var displayItem = this._getDisplayTextForRecordField(record, this._columnList[i]);
                        $columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
                    } else {
                        var displayItem = this._getDisplayTextEditInline(record, this._columnList[i]);
                        $columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
                    }
                }
            } else {
                for (var i = 0; i < this._columnList.length; i++) {
                    var displayItem = this._getDisplayTextForRecordField(record, this._columnList[i]);
                    $columns.eq(this._firstDataColumnOffset + i).html(displayItem || '');
                }
            }
            this._onRowUpdated($tableRow);
        },
        _getDisplayTextEditInline: function(record, fieldName) {
            var field = this.options.fields[fieldName];
            var fieldValue = record[fieldName];
            if (field.display) {
                return field.display({
                    record: record
                });
            }
            if (field.type == 'date') {
                return this._editInline_date(record, fieldName);
            } else if (field.type == 'checkbox') {            	
                return this._editInline_checkbox(record, fieldName);
            } else if (field.type == 'textarea') {
                return this._editInline_textarea(record, fieldName);
            } else if (field.options) { //combobox or radio button list since there are options.
                var options = this._getOptionsForField(fieldName, {
                    record: record,
                    value: fieldValue,
                    source: 'list',
                    dependedValues: this._createDependedValuesUsingRecord(record, field.dependsOn)
                });
                return this._editInline_options(options, fieldValue, record, fieldName);
            } else { //other types
                return this._editInline_default(record, fieldName);
            }
        },
        _editInline_options: function(options, value, record, fieldName) {
            var self = this;
            var val = value;
            var valtext = '';
            var $inputhtml = $('<select></select>');
            $inputhtml.css('background-repeat', 'no-repeat');
            $inputhtml.css('background-position', 'right center');
            for (var i = 0; i < options.length; i++) {
                $inputhtml.append('<option value="' + options[i].Value + '"' + (options[i].Value == value ? ' selected="selected"' : '') + '>' + options[i].DisplayText + '</option>');
                if (options[i].Value == value) {
                    valtext = options[i].DisplayText;
                }
            }
            var defaulttext = (valtext) ? valtext : '---';

            var $txt = $('<span>' + defaulttext + '</span>');
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
                        postData["modifiedField"] = fieldName
                        postData["modifiedFieldValue"] = $(this).val();

                        postData[self._keyField] = record[self._keyField];
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

                        /*if(self._editInline_ajax(postData)){
							val = $(this).val();
							$txt.html($(this).find("option:selected").text());
							record[fieldName]=$(this).val();
							$(this).css('background','none');
							self._showUpdateAnimationForRow($txt.closest("tr"));
						}*/
                    });
                    $inputhtml.focus();
                }
            });
            return $txt;
        },
        _editInline_date: function(record, fieldName) {
            var self = this;
            var field = this.options.fields[fieldName];
            var fieldValue = record[fieldName];
            var displayFormat = field.displayFormat || this.options.defaultDateFormat;
            var defaultval = (fieldValue) ? fieldValue : '&nbsp;&nbsp;&nbsp;';
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
                            postData["modifiedFieldValue"] = $(this).val();
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
        },
        _editInline_checkbox: function(record, fieldName) {
            var self = this;
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
        },
        _editInline_textarea: function(record, fieldName) {
            var self = this;
            var field = this.options.fields[fieldName];
            var fieldValue = record[fieldName];
           
            var defaultval = (fieldValue) ? fieldValue : '&nbsp;&nbsp;&nbsp;';
            var $txt = $('<span>' + defaultval + '</span>');
            $txt.click(function() {
            	 var oldValue = $(this).attr('val');
                if ($(this).children().length < 1) {
                    var $inputhtml = $('<textarea>' + $(this).html() + '</textarea>');
                    $inputhtml.css('background-repeat', 'no-repeat');
                    $inputhtml.css('background-position', 'right center');
                    $(this).html($inputhtml);
                    $inputhtml.bind('change blur focusout', function() {
                        $(this).css('background-image', 'url("' + self.options.editinline.img + 'loading.gif")');
                        
                        var postData = {};
                        //postData[fieldName]=$(this).val();
                        postData[self._keyField] = record[self._keyField];
                        postData["modifiedField"] = fieldName;
                        postData["modifiedFieldValue"] = $(this).val();

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
        },
        _editInline_default: function(record, fieldName) {
            var self = this;
            var field = this.options.fields[fieldName];
            var field1 = this.options.fields[fieldName][2];
            var field2 = this.options.fields[fieldName][3];
            var fieldValue = record[fieldName];
           
            var defaultval = (fieldValue) ? fieldValue : '&nbsp;&nbsp;&nbsp;';
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

                        postData[self._keyField] = record[self._keyField];
                        postData["modifiedField"] = fieldName;
                        postData["modifiedFieldValue"] = $(this).val();

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
        },
        _editInline_ajax: function(postData,txt,val) {
            var self = this,
                res = true,
                deferred = new $.Deferred();
            this._ajax({
                //url: (self.options.actions.updateAction + ".inline"),
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
        },
       
    });
})(jQuery);