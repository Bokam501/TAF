//
// NReco PivotData Pivot Chart Plugin (renders pivot data with Chartist.js)
// @version 1.0
// @author Vitaliy Fedorchenko
// 
// Copyright (c) Vitaliy Fedorchenko (nrecosite.com) - All Rights Reserved
// THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY 
// KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
// PARTICULAR PURPOSE.
//

(function ($) {

	function NRecoPivotChart(element, options) {
		this.element = element;
		this.options = options;

		init(this);
	}

	function init(pvtChart) {
		var o = pvtChart.options;

		if ((typeof pvtChart[o.chartType]) == "function") {
			pvtChart[o.chartType].apply(pvtChart, []);
		} else {
			$.error('Unknown chart type: ' + o.chartType);
		}
	}

	function ifnull(o, nullValue) {
		if (o instanceof Array) {
			var arr = [];
			for (var i = 0; i < o.length; i++)
				arr.push(ifnull(o[i], nullValue));
			return arr;
		}
		return o != null ? o : nullValue;
	}

	function onCreated(pvtChart) {
		if (pvtChart.chart && typeof pvtChart.options.created == "function")
			pvtChart.chart.on('created', pvtChart.options.created);
	}

	NRecoPivotChart.prototype.getChartData = function (totalsOnly) {
		var pivotData = this.options.pivotData;

		var chartData = { labels: [], series: [] };
		var addLabels = function (dimKeys, dims) {
			for (var i = 0; i < dimKeys.length; i++)
				chartData.labels.push(dimKeys[i].join(" "));
			if (dims)
				chartData.axisLabel = dims.join(", ");
		};
		if (pivotData.MeasureLabels.length>1) {
			// handle multiple measures in a special way
			if (pivotData.RowKeys.length > 0) {
				addLabels(pivotData.RowKeys, pivotData.Rows);
				for (var i = 0; i < pivotData.MeasureLabels.length; i++) {
					var seriesData = [];
					for (var j = 0; j < pivotData.RowTotals.length; j++) {
						seriesData.push(ifnull(pivotData.RowTotals[j][i], 0));
					}
					seriesData.name = pivotData.MeasureLabels[i];
					chartData.series.push(seriesData);
				}
			} else if (pivotData.ColumnKeys.length > 0) {
				addLabels(pivotData.ColumnKeys, pivotData.Columns);
				for (var i = 0; i < pivotData.MeasureLabels.length; i++) {
					var seriesData = [];
					for (var j = 0; j < pivotData.ColumnTotals.length; j++) {
						seriesData.push(ifnull(pivotData.ColumnTotals[j][i], 0));
					}
					seriesData.name = pivotData.MeasureLabels[i];
					chartData.series.push(seriesData);
				}
			} else {
				chartData.labels = pivotData.MeasureLabels;
				chartData.series.push(pivotData.GrandTotal);
			}
			return chartData;
		}

		if (pivotData.RowKeys.length > 0 && pivotData.ColumnKeys.length > 0 && !totalsOnly) {
			if (pivotData.ColumnKeys.length > 0) {
				addLabels(pivotData.ColumnKeys, pivotData.Columns);
			} else {
				addLabels(pivotData.RowKeys, pivotData.Rows);
			}
			for (var r = 0; r < pivotData.Values.length; r++) {
				var row = pivotData.Values[r];
				var rowData = [];
				for (var c = 0; c < row.length; c++)
					rowData.push(ifnull(row[c], 0));
				rowData.name = pivotData.RowKeys[r].join(" ");
				chartData.series.push( rowData );
			}
		} else {
			if (pivotData.RowTotals.length > 0) {
				addLabels(pivotData.RowKeys, pivotData.Rows);
				chartData.series.push(ifnull(pivotData.RowTotals, 0));
			} else if (pivotData.ColumnTotals.length > 0) {
				addLabels(pivotData.ColumnKeys, pivotData.Columns);
				chartData.series.push( ifnull(pivotData.ColumnTotals, 0) );
			} else {
				chartData.labels = ["Grand Total"];
				chartData.series.push([ifnull(pivotData.GrandTotal, 0)])
			}
		}
		return chartData;
	};

	var onInitAxesLabels = function (pvtChart, chartData, flip) {
		if (typeof pvtChart.options.initAxesLabels == "function") {
			var numAxisLabel = pvtChart.options.pivotData.MeasureLabels.join(", ");
			var dimAxisLabel = chartData.axisLabel;
			pvtChart.options.initAxesLabels({
				axisXLabel: flip ? numAxisLabel : dimAxisLabel,
				axisYLabel: flip ? dimAxisLabel : numAxisLabel
			});
		}
	};

	var createBarInternal = function (pvtChart, stacked, horizontal, getWidth) {
		var pivotData = pvtChart.options.pivotData;
		var chartData = pvtChart.getChartData();

		var barsInSeries = (chartData.series.length > 0 ? chartData.series[0].length : 0);
		var barsCount = Math.max(1, stacked ? barsInSeries : chartData.series.length * barsInSeries);

		var chartOpts = $.extend({}, pvtChart.options.chartOptions);
		chartOpts.seriesBarDistance = 15;
		chartOpts.stackBars = stacked;
		if (horizontal) {
			chartOpts.reverseData = true;
			chartOpts.horizontalBars = true;
		}
		onInitAxesLabels(pvtChart, chartData, horizontal);

		pvtChart.chart = new Chartist.Bar('#' + pvtChart.element.attr('id'), chartData, chartOpts, {})
			.on('draw', function (data) {
				if (data.type === 'bar') {
					var chartWidth = getWidth(data) - chartData.series.length * 10;
					var barWidth = Math.max(3, Math.round(chartWidth / barsCount));
					if (barWidth > 3)
						barWidth *= 1 - (1 / Math.max( barsCount, 2) );
					data.element.attr({
						style: 'stroke-width: ' + barWidth + 'px'
					});
				}
			});
		// lets use 'data' event to intercept 'getCurrentOptions' for correct seriesBarDistrance calculation on resize
		pvtChart.chart.on('data', function () {
			var origGetCurrentOptions = pvtChart.chart.optionsProvider.getCurrentOptions;
			pvtChart.chart.optionsProvider.getCurrentOptions = function () {
				var opts = origGetCurrentOptions();
				var width = getWidth(null, opts) - chartData.series.length*10;
				opts.seriesBarDistance = Math.max(4, Math.round(width / barsCount) );
				return opts;
			};
		});

		onCreated(pvtChart);
	};

	var getChartWidth = function (data, opts) {
		if (!data) {
			var w = this.element.width();
			if (opts && opts.axisX && opts.axisX.offset)
				w -= opts.axisX.offset;
			if (opts && opts.chartPadding) {
				w -= (opts.chartPadding.left + opts.chartPadding.right);
			}
			return w;
		}
		return data.chartRect.width();
	};
	var getChartHeight = function (data, opts) {
		if (!data) {
			var h = this.element.height();
			if (opts && opts.axisY && opts.axisY.offset)
				h -= opts.axisY.offset;
			if (opts && opts.chartPadding) {
				h -= (opts.chartPadding.top + opts.chartPadding.bottom);
			}
			return h;
		}
		return data.chartRect.height();
	};

	NRecoPivotChart.prototype.bar = function (stacked) {
		createBarInternal(this, false, false, getChartWidth.bind(this) );
	};

	NRecoPivotChart.prototype.stackedBar = function () {
		createBarInternal(this, true, false, getChartWidth.bind(this) );
	};

	NRecoPivotChart.prototype.horizontalBar = function (stacked) {
		createBarInternal(this, false, true, getChartHeight.bind(this) );
	};


	NRecoPivotChart.prototype.horizontalStackedBar = function () {
		createBarInternal(this, true, true, getChartHeight.bind(this) );
	};

	NRecoPivotChart.prototype.pie = function () {
		var pivotData = this.options.pivotData;

		var chartData = this.getChartData(true);

		var chartOpts = $.extend({}, this.options.chartOptions);

		var totalSum = chartData.series[0].reduce(function (a, b) { return a + b });

		chartOpts.labelInterpolationFnc = function (value) {
			var percent = Math.round(value / totalSum * 100);
			return percent>1 ? percent+'%' : '';
		};
		var pieChartSeries = [];
		for (var i = 0; i < chartData.series[0].length; i++) {
			var d = { value: chartData.series[0][i] };
			if (chartData.labels && i<chartData.labels.length)
				d.meta = chartData.labels[i];
			pieChartSeries.push(d);
		}

		this.chart = new Chartist.Pie('#' + this.element.attr('id'),
			{
				series: pieChartSeries
			}, chartOpts, {});
		onCreated(this);
	};

	NRecoPivotChart.prototype.line = function () {
		var pivotData = this.options.pivotData;

		var chartData = this.getChartData();
		var chartOpts = $.extend({}, this.options.chartOptions);
		chartOpts.fullWidth = true;
		chartOpts.lineSmooth = false;
		onInitAxesLabels(this, chartData, false);

		this.chart = new Chartist.Line('#' + this.element.attr('id'), chartData, chartOpts);
		onCreated(this);
	};

	NRecoPivotChart.prototype.destroy = function () {
		if (this.chart) {
			this.chart.detach();
			this.chart = null;
		}
	};


	$.fn.nrecoPivotChart = function (options) {
		if (typeof options == "string") {
			var instance = this.data('_nrecoPivotChart');
			if (instance) {
				if ((typeof instance[options]) == "function") {
					return instance[options].apply(instance, Array.prototype.slice.call(arguments, 1));
				} else {
					$.error('Method ' + options + ' does not exist');
				}
			} else {
				return; // nothing to do
			}
		}
		return this.each(function () {
			var opts = $.extend({}, $.fn.nrecoPivotChart.defaults, options);
			var $holder = $(this);

			if (!$.data(this, '_nrecoPivotChart')) {
				$.data(this, '_nrecoPivotChart', new NRecoPivotChart($holder, opts));
			}
		});

	};

	$.fn.nrecoPivotChart.defaults = {
		pivotData: {},
		chartOptions: {},
		created: null,
		initAxesLabels : null,
		chartType : "bar"  // line, bar, stackedBar, horizontalBar, horizontalStackedBar, pie
	};

	$.fn.nrecoPivotChart.version = 1.0;

})(jQuery);