﻿<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta charset="utf-8" />
    <title>Pivot Report Builder UI Demo</title>

    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- bootstrap (optional) -->
    <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
    <script type="text/javascript" src="//code.jquery.com/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.js"></script>


    <!-- select2 plugin (depends on jquery-ui sortable)-->
    <link href="Scripts/select2/select2.css" rel="stylesheet" />
    <link href="Scripts/select2/select2-bootstrap.css" rel="stylesheet" />
    <script type="text/javascript" src="Scripts/select2/select2.min.js"></script>
    <script type="text/javascript" src="Scripts/jquery-ui-1.9.2.custom.min.js"></script>

</head>
<body>

    <div class="container">

        <!-- basic pivot table styles -->
        <link href="Scripts/nrecopivottable.css" rel="stylesheet" />

        <!-- interactive nreco pivot table jQuery plugin (change sorting, fixed headers) -->
        <script type="text/javascript" src="Scripts/jquery.nrecopivottable.js"></script>

        <!-- chartist.js for charts (optional)  -->
        <link rel="stylesheet" type="text/css" href="Scripts/chartist/chartist.min.css">
        <script type="text/javascript" src="Scripts/chartist/chartist.min.js"></script>

        <!-- chartist plugins for charts usability (tooltip, axis labels) -->
        <script type="text/javascript" src="Scripts/chartist/chartist-plugin-tooltip.min.js"></script>
        <script type="text/javascript" src="Scripts/chartist/chartist-plugin-axistitle.min.js"></script>

        <!-- nreco pivot chart wrapper for chartist.js -->
        <script type="text/javascript" src="Scripts/jquery.nrecopivotchart.js"></script>

        <!-- autocomplete plugin for filter (optional) -->
        <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.1/awesomplete.min.js"></script>
        <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/awesomplete/1.1.1/awesomplete.min.css">

        <h1>
            Pivot Table Builder
            <small>JS control + microservice backend for user-defined summary reports</small>
        </h1>
        <hr />
        <div class="row">
           

            <div class="col-md-6">

                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Data Source:</label>
                        <div class="col-sm-8" id="row">
                            <select id="cubeNameSelect" class="form-control">
                             </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Rows:</label>
                        <div class="col-sm-8" id="row">
                            <input type="hidden" id="rowDims" class="form-control" autocomplete="off" value="" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Columns:</label>
                        <div class="col-sm-8" id="column">
                            <input type="hidden" id="columnDims" class="form-control" autocomplete="off" value="" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Values:</label>
                        <div class="col-sm-5">
                            <input type="hidden" id="measureIndexes" class="form-control" autocomplete="off" value="" />
                        </div>
                        <div class="col-sm-3">
                            <select id="measurePercentage" class="form-control">
                                <option value="">Number</option>
                                <option value="GrandTotal">% of Grand Total</option>
                                <option value="RowTotal">% of Row</option>
                                <option value="ColumnTotal">% of Column</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Limits:</label>
                        <div class="col-sm-4">
                            <select id="limitRows" class="form-control">
                                <option value="">(unlimited rows)</option>
                                <option value="5">Top 5 rows</option>
                                <option value="10">Top 10 rows</option>
                                <option value="50">Top 50 rows</option>
                                <option value="100">Top 100 rows</option>
								<option value="1000">Top 1000 rows</option>
                            </select>
                        </div>
                        <div class="col-sm-4">
                            <select id="limitColumns" class="form-control">
                                <option value="">(unlimited columns)</option>
                                <option value="5">Top 5 columns</option>
                                <option value="10">Top 10 columns</option>
                                <option value="50">Top 50 columns</option>
                                <option value="100">Top 100 columns</option>
								<option value="1000">Top 1000 columns</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Preserve labels grouping:</label>
                        <div class="col-sm-8" id="column">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" id="preserveGroupOrder" autocomplete="off" value="" />
                                    Do not change labels grouping when table is sorted by value
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Totals:</label>
                        <div class="col-sm-8" id="column">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" id="grandTotal" autocomplete="off" value="" checked="checked" />
                                    Grand Total
                                </label>

                                <label>
                                    <input type="checkbox" id="totalsColumn" autocomplete="off" value="" checked="checked" />
                                    Totals Column
                                </label>

                                <label>
                                    <input type="checkbox" id="totalsRow" autocomplete="off" value="" checked="checked" />
                                    Totals Row
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Subtotals:</label>
                        <div class="col-sm-8" id="column">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" id="subtotalColumns" autocomplete="off" value="" />
                                    Columns
                                </label>

                                <label>
                                    <input type="checkbox" id="subtotalRows" autocomplete="off" value="" />
                                    Rows
                                </label>
                            </div>
                        </div>
                    </div>

                    <!--<div class="form-group">
                        <label class="col-sm-4 control-label">Pivot Chart:</label>
                        <div class="col-sm-8" id="column">
                            <select id="chartType" class="form-control">
                                <option value="">(none)</option>
                                <option value="bar">Bar</option>
                                <option value="stackedBar">Stacked Bar</option>
                                <option value="horizontalBar">Horizontal Bar</option>
                                <option value="horizontalStackedBar">Horizontal Stacked Bar</option>
                                <option value="line">Line</option>
                                <option value="pie">Pie</option>
                            </select>
                        </div>
                    </div>-->
                </div>


            </div>

        </div>




        <div class="pull-left">
            <a href="javascript:;" id="renderPivotTable" class="btn btn-primary">Generate Pivot Table</a>
        </div>

        <div class="pull-right form-inline">
            <!--<div class="form-group">
                <input type="text" class="form-control" id="filter" style="width:400px;"
                       placeholder="Filter rows or columns..."
                       title="Enter comma-separated list of keywords to filter. Use '-' prefix to exclude." />
            </div>-->
			<a href="javascript:;" id="saveButton" class="btn btn-default exportBtn">Save</a>
            <!--<a href="javascript:;" class="btn btn-default exportBtn" rel="csv">Export to CSV</a>

            <a href="javascript:;" class="btn btn-default exportBtn" rel="excel">Export to Excel</a>

            <a href="javascript:;" class="btn btn-default exportBtn" rel="pdf">Export to PDF</a>

            <a href="javascript:;" class="btn btn-default exportBtn" rel="json">Export to JSON</a>-->
        </div>
        <div class="clearfix"></div>
        <br />

        <div id="pivotChartHolder">

        </div>

        <style>
            .select2-search-choice.ui-sortable-helper {
                width: auto !important;
                height: auto !important;
            }

            #pivotHolder {
                overflow: auto;
                max-height: 500px; /* height may be changed dynamically */
            }
        </style>


		<div id="contentDiv" style="display:none;">
        <div id="pivotHolder" style="display:none;margin-top:20px;">
            <div class="progress" style="width:50%;margin-left:auto;margin-right:auto;margin-top:20px;margin-bottom:20px;">
                <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    <span class="sr-only">loading...</span>
                </div>
            </div>
        </div>
		</div>


        <script type="text/javascript">
            $(function () {
                var metadataCubes = [];
                var currentCubeSchema = null;

                // init pivot configuration controls (you may use any multiple-select widgets instead of select2 jQuery plugin)
                var $cubeNameSelect = $('#cubeNameSelect');

                var $rowDims = $('#rowDims');
                $rowDims.select2({
                    minimumInputLength: 0,
                    multiple: true,
                    data: []
                });
                var $columnDims = $('#columnDims');
                $columnDims.select2({
                    minimumInputLength: 0,
                    multiple: true,
                    data: []
                });
                var $measureIdxs = $('#measureIndexes');
                $measureIdxs.select2({
                    minimumInputLength: 0,
                    multiple: true,
					data: []
                });
                $measureIdxs.on("select2-selecting", function(e) { 
                	//console.log(e);
                	//e.val = e.val;
				});

                // filter autocomplete (awesomplete plugin)
                /*var $filterInput = $('#filter');
                var filterAwesomplete = new Awesomplete($filterInput[0], {
                    minChars: 1,
                    filter: function (text, input) {
                        var lastKeyword = input.match(/[^+;,-]*$/)[0];
                        if ($.trim(lastKeyword).length == 0)
                            return false;
                        return Awesomplete.FILTER_CONTAINS(text, lastKeyword);
                    },
                    replace: function (text) {
                        var val = this.input.value;
                        var lastKeyword = val.match(/[^+;,-]*$/)[0];
                        var before = val.substring(0, val.length - lastKeyword.length);
                        this.input.value = before + text + ", ";
                    }
                });
                var setFilterAutocompleteList = function (pvtTblData) {
                    var keywords = [];
                    var addKeys = function (dimKeys) {
                        for (var i = 0; i < dimKeys.length; i++) {
                            var key = dimKeys[i];
                            for (var j = 0; j < key.length; j++) {
                                keywords.push(key[j]);
                            }
                        }
                    }
                    addKeys(pvtTblData.ColumnKeys);
                    addKeys(pvtTblData.RowKeys);
                    filterAwesomplete.list = keywords;
                    filterAwesomplete.close();
                };*/

                var applySortable = function () {
                    $rowDims.select2("container").find("ul.select2-choices").sortable({
                        containment: 'parent',
                        start: function () { $rowDims.select2("onSortStart"); },
                        update: function () { $rowDims.select2("onSortEnd"); }
                    });
                    $columnDims.select2("container").find("ul.select2-choices").sortable({
                        containment: 'parent',
                        start: function () { $columnDims.select2("onSortStart"); },
                        update: function () { $columnDims.select2("onSortEnd"); }
                    });
                    $measureIdxs.select2("container").find("ul.select2-choices").sortable({
                        containment: 'parent',
                        start: function () { $measureIdxs.select2("onSortStart"); },
                        update: function () { $measureIdxs.select2("onSortEnd"); }
                    });
                };

                var currentCubeCfg = null;

                // function that reads pivot configuration from the UI
                var getPivotTableConfig = function () {
                    var percentageMode = $('#measurePercentage').val();

                    var rowDimVals = $.map($rowDims.select2('val'), function (dimName) { return { Name: dimName }; });
                    var colDimVals = $.map($columnDims.select2('val'), function (mName) { return { Name: mName }; });
                    var measureVals = $.map($measureIdxs.select2('val'), function (mName) { return { Name: mName, Percentage: percentageMode }; });

                    var pivotTableConfig = {
                        Rows: rowDimVals,
                        Columns: colDimVals,
                        Measures: measureVals,
                        OrderBy : {
                            PreserveGroupOrder: $('#preserveGroupOrder').is(':checked'),
                        },
                        SubtotalColumns: $('#subtotalColumns').is(':checked'),
                        SubtotalRows: $('#subtotalRows').is(':checked'),
                        GrandTotal: $('#grandTotal').is(':checked'),
                        TotalsRow: $('#totalsRow').is(':checked'),
                        TotalsColumn: $('#totalsColumn').is(':checked'),
                        LimitRows: $('#limitRows').val(),
                        LimitColumns: $('#limitColumns').val(),
                        //Filter: $filterInput.val()
                    };
                    var uiConfig = $('#pivotHolder').nrecoPivotTable("getPivotTableConfig");
                    if (uiConfig != null) {
                    	pivotTableConfig.OrderBy.Values = uiConfig.SortByValue;
                        pivotTableConfig.OrderBy.Dimensions = uiConfig.OrderKeys;
                        pivotTableConfig.ColumnPage = uiConfig.ColumnPage ? uiConfig.ColumnPage : { Offset: 0, Limit: 100 };
                        pivotTableConfig.RowPage = uiConfig.RowPage ? uiConfig.RowPage : { Offset: 0, Limit: 100 };
                    }
                    return pivotTableConfig;
                };

                var isIE = (navigator.userAgent.indexOf('MSIE') !== -1 || navigator.appVersion.indexOf('Trident/') > 0);

                // initialize interactive pivot table plugin
                $('#pivotHolder').nrecoPivotTable({
                    renderTable: function (pivotTable, callback) {
                        var cubeName = $cubeNameSelect.val();
						console.log("cubeName>>>"+cubeName);
                        var cfg = getPivotTableConfig();
						console.log("***"+cfg);
						console.log("string>>>"+JSON.stringify(cfg));
                        $.ajax("api/cube/" + encodeURIComponent(cubeName) + "/pivot/render", {
                            type: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(cfg)
                        }).success(function (res) {
                            callback(res.HtmlContent, res.Configuration);
                            setFilterAutocompleteList( $.parseJSON( res.JsonData) );
                        });

                    },
                    fixedHeaders: true,
                    fixedHeadersSmooth: !isIE  // IE raises scroll event with some delay and smooth headers positioning leads to weird UX
                }).bind("sortChanged pageChanged", function () {
                	$(this).nrecoPivotTable("load");
                });

                // default chartist css supports only 15 colours
               /* Chartist.alphaNumerate = function (n) {
                    return String.fromCharCode(97 + n % 15);
                };

                var loadPivotChart = function (chartType) {
                    var cubeName = $cubeNameSelect.val();
                    var cfg = getPivotTableConfig();
                    $.ajax("api/cube/" + encodeURIComponent(cubeName) + "/pivot/export/json", {
                        type: "POST",
                        data: {
                            "pvtReportJson": JSON.stringify(cfg)
                        }
                    }).success(function (res) {
                        $('#pivotChartHolder').html('<div id="pivotChart" class="ct-chart" style="height:400px;"></div>');
                        var chartElem = $('#pivotChart'); // chart element should have ID for correct initialization

                        var pvtChartOpts = {
                            pivotData: res,
                            chartType: chartType,

                            // this is chartist.js options
                            chartOptions: {
                                axisY: { offset: 60 },
                                axisX: { offset: 35 },
                                height: 400,
                                plugins: [
                                    // optional chart plugin: tooltip on mouse over
                                    Chartist.plugins.tooltip({
                                        tooltipOffset: { x: 0, y: -10 },
                                        metaIsHTML: true
                                    })
                                ]
                            }
                        };

                        if (chartType == 'pie') {
                            pvtChartOpts.chartOptions.chartPadding = 20;
                            pvtChartOpts.chartOptions.labelOffset = 10;
                            pvtChartOpts.chartOptions.labelPosition = 'outside';
                            pvtChartOpts.chartOptions.labelDirection = 'explode';
                        } else {
                            // optional chart plugin: render axes labels
                            pvtChartOpts.initAxesLabels = function (labelInfo) {
                                pvtChartOpts.chartOptions.plugins.push(
                                    Chartist.plugins.ctAxisTitle({
                                        axisX: {
                                            axisTitle: labelInfo.axisXLabel,
                                            axisClass: 'ct-axis-title ct-label',
                                            offset: { x: 0, y: 35 },
                                            textAnchor: 'middle'
                                        },
                                        axisY: {
                                            axisTitle: labelInfo.axisYLabel,
                                            axisClass: 'ct-axis-title ct-label',
                                            flipTitle: true,
                                            offset: { x: 0, y: 15 },
                                            textAnchor: 'middle'
                                        }
                                    })
                                );
                            };
                        }

                        // build pivot chart with Chartist.js library
                        chartElem.nrecoPivotChart(pvtChartOpts);
                    });
                };*/

                $cubeNameSelect.change(function () {
                	var newCubeName = $cubeNameSelect.val();

                	var newCubeInfo = null;
                	for (var i = 0; i < metadataCubes.length; i++)
                		if (metadataCubes[i].Id == newCubeName)
                			newCubeInfo = metadataCubes[i];
                	currentCubeSchema = newCubeInfo;
                	if (newCubeInfo == null) return;

                	var dimOptions = $.map(newCubeInfo.Dimensions, function (dimInfo, i) {
                		return { id: dimInfo.Name, text: dimInfo.LabelText != null ? dimInfo.LabelText : dimInfo.Name };
                	});
                	var measureOptions = $.map(newCubeInfo.Measures, function (measureInfo, i) {
                		var labelText = measureInfo.LabelText;
                		if (labelText == null) {
                			var labelText = measureInfo.Type;
                			var fldName = measureInfo.Params && measureInfo.Params.length > 0 ? measureInfo.Params[0] : null;
                			if (fldName != null)
                				labelText += " of " + fldName;
                		}
                		return { id: measureInfo.Name, text: labelText };
                	});

                	$rowDims.val(dimOptions.length > 0 ? dimOptions[0].id : "").select2({
                		minimumInputLength: 0,
                		allowClear: true,
                		multiple: true,
                		data: dimOptions
                	});
                	$columnDims.val(dimOptions.length > 1 ? dimOptions[1].id : "").select2({
                		minimumInputLength: 0,
                		allowClear: true,
                		multiple: true,
                		data: dimOptions
                	});
                	$measureIdxs.val(measureOptions.length > 0 ? [measureOptions[0].id] : "").select2({
                        minimumInputLength: 0,
                        allowClear: true,
                        multiple: true,
                        data: measureOptions
                    });
                	applySortable();

                	// clear filter and autocomplete list
                	//$('#filter').val('');
                	//filterAwesomplete.list = [];

                	var $pvtHolder = $('#pivotHolder').show();
                	$pvtHolder.nrecoPivotTable('setPivotTableConfig', {});
                	$pvtHolder.nrecoPivotTable("load");
                });

                $('#renderPivotTable').click(function () {
                    if (window.pageTracker)
                        window.pageTracker._trackEvent("demo", "PivotData_ToolkitPivotTable_render");
                    $('#pivotHolder').show().nrecoPivotTable("load");

                    /*var chartType = $('#chartType').val();
                    $('#pivotChartHolder').html("");
                    if ($.trim(chartType) != "") {
                        if (window.pageTracker)
                            window.pageTracker._trackEvent("demo", "PivotData_PivotChart_" + chartType);
                        loadPivotChart(chartType);
                    }*/
                });

                // refresh pivot table on filter enter
               /* $filterInput.keypress(function (e) {
                    if (e.which == 13) {
                        //filterAwesomplete.close();
                        $('#renderPivotTable').click();
                        return false;
                    }
                });*/

                $('.exportBtn').click(function () {
                    var format = $(this).attr('rel');
                    if (window.pageTracker)
                        window.pageTracker._trackEvent("demo", "PivotData_ToolkitPivotTable_export_" + format);

                    var cubeName = $cubeNameSelect.val();

                    var $f = $('#exportForm');
                    $('#exportFormTarget').remove();
                    var $iframe = $('<iframe id="exportFormTarget" name="exportFormTarget" style="display:none;"/>');
                    $iframe.insertAfter($f);

                    $f.attr('action', "api/cube/" + encodeURIComponent(cubeName)+"/pivot/export/"+format);
                    $f.find('input[name="pvtReportJson"]').val(JSON.stringify(getPivotTableConfig()));
                    $f.submit();
                });

                $.get("api/cube").done(
                    function (cubes) {
                        metadataCubes = cubes;
                        $cubeNameSelect.remove('option');
                        for (var i = 0; i < cubes.length; i++) {
                            $cubeNameSelect.append($('<option/>', { value: cubes[i].Id, text: cubes[i].Name }))
                        }
                        $cubeNameSelect.change();
                    }
                );
				$('#saveButton').click(function () {
                   $('#contentDiv').show();
                   $('#pivotHolder').show().nrecoPivotTable("load");

                });
            });
        </script>

        <form id="exportForm" method="POST" action="" target="exportFormTarget">
            <input type="hidden" name="pvtReportJson" />
        </form>

        <style>
            div.awesomplete > ul {
                z-index: 199;
            }

            /* pretty styles for charts */
            .ct-label {
                font-size: 1.0rem;
            }

            .ct-label {
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .ct-axis-title {
                font-style: italic;
            }

            .ct-grid {
                stroke-dasharray: 1px !important;
            }

            .ct-chart line {
                shape-rendering: crispedges;
            }

            .chartist-tooltip {
                position: absolute;
                display: inline-block;
                opacity: 0;
                min-width: 50px;
                padding: 5px;
                background-color: rgba(230,230,230,0.7);
                color: #453D3F;
                font-family: Helvetica,Arial,sans-serif;
                font-weight: 600;
                font-size: 14px;
                text-align: center;
                pointer-events: none;
                z-index: 1;
                -webkit-transition: opacity .2s linear;
                -moz-transition: opacity .2s linear;
                -o-transition: opacity .2s linear;
                transition: opacity .2s linear;
            }

                .chartist-tooltip:before {
                    content: "";
                    position: absolute;
                    top: 100%;
                    left: 50%;
                    width: 0;
                    height: 0;
                    margin-left: -10px;
                    border: 10px solid transparent;
                    border-top-color: rgba(230,230,230,0.7);
                }

                .chartist-tooltip.tooltip-show {
                    opacity: 1;
                }

            .chartist-tooltip-meta {
                font-weight: normal;
                font-size: 11px;
                overflow: hidden;
                text-overflow: ellipsis;
                max-width: 200px;
                max-height: 50px;
                display: inline-block;
                padding: 2px;
                background-color: rgba(255,255,255,0.9);
            }
        </style>

        <br/><br/>
    </div>

</body>
</html>
