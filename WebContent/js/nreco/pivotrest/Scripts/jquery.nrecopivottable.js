//
// NReco PivotData Pivot Table Plugin
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

	function NRecoPivotTable(element, options) {
		this.element = element;
		this.options = options;

		if (options.pivotTableConfig==null)
			options.pivotTableConfig = $.extend({}, $.fn.nrecoPivotTable.defaults.pivotTableConfig);

		if (options.fixedHeaders == null) {
			options.fixedHeaders = element.css('overflow') == "auto";
		}

		init(this);
		if (options.autoload)
			this.load();
		else {
			var $tbl = element.find('table');
			if ($tbl.length == 1) {
				initTable(this, $tbl);
			}
		}
	}

	function onSortChanged(pvtTbl) {
		pvtTbl.element.trigger("sortChanged");
	}

	function onPageChanged(pvtTbl) {
		pvtTbl.element.trigger("pageChanged");
	}

	function getDirectionStr(direction) {
		return direction == "Ascending" || direction == "0" ? "Ascending" : "Descending";
	}
	function getAxisStr(axis) {
		return axis == "Rows" || axis == "0" ? "Rows" : "Columns";
	}
	function normalizeEnums(pivotTblCfg) {
		if (pivotTblCfg.OrderKeys)
			for (var i = 0; i < pivotTblCfg.OrderKeys.length; i++) {
				var k = pivotTblCfg.OrderKeys[i];
				k.Direction = getDirectionStr(k.Direction);
				k.Axis = getAxisStr(k.Axis);
			}
		if (pivotTblCfg.SortByValue) {
			if (pivotTblCfg.SortByValue.Axis != null)
				pivotTblCfg.SortByValue.Axis = getAxisStr(pivotTblCfg.SortByValue.Axis);
			if (pivotTblCfg.SortByValue.Direction != null)
				pivotTblCfg.SortByValue.Direction = getDirectionStr(pivotTblCfg.SortByValue.Direction);
		}
	}

 	function getOrderKeysDirection(orderKeys, axis, index) {
		for (var i = 0; i < orderKeys.length; i++) {
			var k = orderKeys[i];
			if (k.Axis == axis && k.Index == index)
				return k.Direction;
		}
		return "Ascending";
	}

	function setOrderKeys(orderKeys, axis, index, direction) {
		for (var i = 0; i < orderKeys.length; i++) {
			var k = orderKeys[i];
			if ( k.Axis == axis && k.Index == index) {
				k.Direction = direction;
				return;
			}
		}
		orderKeys.push({
			Axis: axis,
			Index: index,
			Direction : direction
		});
	}

	function init(pvtTbl) {
		var o = pvtTbl.options;
		if (o.pivotTableConfig)
			normalizeEnums(o.pivotTableConfig);

		pvtTbl.element.on('click', o.sortColumnSelector, function () {
			var sortIdxStr = $(this).attr('data-sort-index');
			var sortIdx = null;
			if (sortIdxStr.length > 0) {
				sortIdx = parseInt(sortIdxStr);
				if (o.pivotTableConfig && o.pivotTableConfig.ColumnPage && o.pivotTableConfig.ColumnPage.Offset > 0)
					sortIdx += o.pivotTableConfig.ColumnPage.Offset;
			}
			var measureIdx = $(this).attr('data-sort-measure');
			switchSortByValue(pvtTbl, "Rows", sortIdx, measureIdx); // sort columns by row values
		});
		pvtTbl.element.on('click', o.sortRowSelector, function () {
			var sortIdxStr = $(this).attr('data-sort-index');
			var sortIdx = null;
			if (sortIdxStr.length > 0) {
				sortIdx = parseInt(sortIdxStr);
				if (o.pivotTableConfig && o.pivotTableConfig.RowPage && o.pivotTableConfig.RowPage.Offset > 0)
					sortIdx += o.pivotTableConfig.RowPage.Offset;
			}
			switchSortByValue(pvtTbl, "Columns", sortIdx); // sort columns by row values
		});

		pvtTbl.element.on('click', o.sortColumnLabelSelector, function () {
			var colDimIdx = parseInt($(this).attr('dim-sort-index'));
			var pvtTblCfg = pvtTbl.options.pivotTableConfig;
			var sort = pvtTblCfg.SortByValue ? pvtTblCfg.SortByValue : {};
			if (!pvtTblCfg.OrderKeys)
				pvtTblCfg.OrderKeys = [];

			var direction = "Ascending";
			if (sort.Axis == "Columns" && (!pvtTblCfg.PreserveGroupOrder || colDimIdx == (pvtTblCfg.Columns.length - 1))) {
				sort.Axis = null;
				sort.Index = null;
				sort.Direction = null;
			} else {
				direction = getOrderKeysDirection(pvtTblCfg.OrderKeys, "Columns", colDimIdx) == "Descending" ? "Ascending" : "Descending";
			}
			setOrderKeys(pvtTblCfg.OrderKeys, "Columns", colDimIdx, direction);
			onSortChanged(pvtTbl);
		});
		
		pvtTbl.element.on('click', o.sortRowLabelSelector, function () {
			var rowDimIdx = parseInt($(this).attr('dim-sort-index'));
			var pvtTblCfg = pvtTbl.options.pivotTableConfig;
			var sort = pvtTblCfg.SortByValue ? pvtTblCfg.SortByValue : {};
			if (!pvtTblCfg.OrderKeys)
				pvtTblCfg.OrderKeys = [];

			var direction = "Ascending";
			if (sort.Axis == "Rows" && (!pvtTblCfg.PreserveGroupOrder || rowDimIdx == (pvtTblCfg.Rows.length - 1))) {
				sort.Axis = null;
				sort.Index = null;
				sort.Direction = null;
			} else {
				direction = getOrderKeysDirection(pvtTblCfg.OrderKeys, "Rows", rowDimIdx) == "Descending" ? "Ascending" : "Descending";
			}
			setOrderKeys(pvtTblCfg.OrderKeys, "Rows", rowDimIdx, direction);
			onSortChanged(pvtTbl);
		});

		if (o.pager) {
			pvtTbl.element.on('click', o.pagerSelector, function () {
				var $el = $(this);
				var $th = $el.closest('th');
				var dataOffset = $el.attr('data-offset');
				if (!dataOffset)
					dataOffset = $th.attr('data-offset');
				var newOffset = parseInt(dataOffset);
				var pvtTblCfg = pvtTbl.options.pivotTableConfig;
				if ($th.hasClass("pvtColumn")) {
					if (pvtTblCfg.ColumnPage) {
						pvtTblCfg.ColumnPage.Offset = newOffset;
						onPageChanged(pvtTbl);
					}
				} else {
					if (pvtTblCfg.RowPage) {
						pvtTblCfg.RowPage.Offset = newOffset;
						onPageChanged(pvtTbl);
					}
				}
			});
		}
	}

	function switchSortByValue(pvtTbl, axis, sortIdx, measureIdx) {
		var pvtTblCfg = pvtTbl.options.pivotTableConfig;
		if (!pvtTblCfg.SortByValue)
			pvtTblCfg.SortByValue = {};
		var sort = pvtTblCfg.SortByValue;
		if (sort.Axis == axis && sort.Index == sortIdx && sort.Measure == measureIdx) {
			if (sort.Direction == "Ascending") {
				sort.Direction = "Descending";
			} else {
				sort.Axis = null;
				sort.Index = null;
				sort.Direction = null;
				sort.Measure = null;
			}
		} else {
			sort.Axis = axis;
			sort.Index = sortIdx;
			sort.Direction = "Ascending";
			sort.Measure = measureIdx ? measureIdx : null;
		}
		onSortChanged(pvtTbl);
	}

	function applySortStyle(pvtTbl, $t) {
		var sort = pvtTbl.options.pivotTableConfig ? pvtTbl.options.pivotTableConfig.SortByValue : {};
		if (!sort || sort.Axis == null) return;

		var directionClass = sort.Direction=="Ascending" ? "pvtSortAsc" : "pvtSortDesc";
		var headerClassByAxis = sort.Axis == "Rows" ? "pvtColumn" : "pvtRow";
		var sortIdx = sort.Index == null ? "" : sort.Index;
		// apply pager offsets
		if (sortIdx != "") {
			if ( sort.Axis == "Rows") {
				if (pvtTbl.options.pivotTableConfig.ColumnPage && pvtTbl.options.pivotTableConfig.ColumnPage.Offset > 0)
					sortIdx -= pvtTbl.options.pivotTableConfig.ColumnPage.Offset;
			} else {
				if (pvtTbl.options.pivotTableConfig.RowPage && pvtTbl.options.pivotTableConfig.RowPage.Offset > 0)
					sortIdx -= pvtTbl.options.pivotTableConfig.RowPage.Offset;
			}
		}
		var selector = 'th.' + headerClassByAxis + '[data-sort-index="' + sortIdx + '"]';
		if (sort.Measure != null)
			selector += '[data-sort-measure="' + sort.Measure + '"]';
		$t.find(selector).addClass(directionClass);
	};

	function applyAxesLabels(pvtTbl, $t, pvtInfo) {
		var sortByValueAxis = pvtInfo.SortByValue != null ?  pvtInfo.SortByValue.Axis : null;
		var isSortDesc = function (axis, index) {
			if (pvtInfo.OrderKeys) {
				var list = pvtInfo.OrderKeys;
				for (var s = 0; s < list.length; s++)
					if ( list[s].Axis == axis && list[s].Index == index && list[s].Direction == "Descending")
						return true;
			}
			return false;
		};
		if (pvtInfo.Rows && pvtInfo.Rows.length > 0) {
			$t.find('th.pvtRowLabel').each(function (idx, th) {
				var dimSortIdx = th.getAttribute("dim-sort-index");
				var r = dimSortIdx != null ? parseInt(dimSortIdx) : idx;
				if (sortByValueAxis != "Rows" || (pvtInfo.PreserveGroupOrder && r < (pvtInfo.Rows.length - 1))) {
					var isDesc = isSortDesc("Rows", r);
					$(th).addClass(isDesc ? "pvtSortDesc" : "pvtSortAsc");
				}
			});
		}

		if (pvtInfo.Columns && pvtInfo.Columns.length > 0) {
			$t.find('th.pvtColumnLabel').each(function (i, th) {
				var dimSortIdx = th.getAttribute("dim-sort-index");
				var r = dimSortIdx != null ? parseInt(dimSortIdx) : idx;
				if (sortByValueAxis != "Columns" || (pvtInfo.PreserveGroupOrder && r < (pvtInfo.Columns.length - 1))) {
					var isDesc = isSortDesc("Columns", r);
					$(th).addClass(isDesc ? "pvtSortDesc" : "pvtSortAsc");
				}

			});
		}
	};

	function preparePagerContent(elem, content, page) {
		if (elem.length == 0)
			return "";
		var totalCount = parseInt(elem.attr('data-count'));
		var currentPage = Math.floor(page.Offset / page.Limit) + 1;
		var lastPage = Math.ceil(totalCount / page.Limit);
		var nextPage = Math.min(lastPage, currentPage + 1);
		var prevPage = Math.max(1, currentPage - 1);

		var pagerData = {
			firstPageOffset: 0,
			firstPage: 0,
			prevPage: prevPage,
			prevPageOffset: (prevPage - 1) * page.Limit,
			hasPrevPage: prevPage < currentPage,
			currPage: currentPage,
			nextPage: nextPage,
			nextPageOffset: (nextPage - 1) * page.Limit,
			hasNextPage: (currentPage - 1) * page.Limit < totalCount,
			lastPage : lastPage,
			lastPageOffset: (lastPage - 1) * page.Limit
		};
		if (typeof content === 'function') {
			return content(pagerData);
		} else {
			for (var token in pagerData)
				content = content.replace("{" + token + "}", pagerData[token].toString());
			return content;
		}
	};

	function mergePagerPrevNext(pvtTbl, $t) {
		var mergeCells = function($cells, attrName) {
			var res = $cells.first();
			if ($cells.length > 1) {
				var attrVal = 0;
				for (var i = 0; i < $cells.length; i++) {
					var $c = $($cells[i]);
					$c.hide();
					var v = $c.attr(attrName);
					attrVal += v ? parseInt(v) : 1;
				}
				res.show().attr(attrName, attrVal);
			}
			return res;
		};
		var colPage = pvtTbl.options.pivotTableConfig.ColumnPage;
		if (colPage) {
			var colPrev = mergeCells($t.find('th.pvtColumn[data-key-type="prev"]'), "rowspan");
			colPrev.html( preparePagerContent(colPrev, pvtTbl.options.pagerContent.columnPrev, colPage) );

			var colNext = mergeCells($t.find('th.pvtColumn[data-key-type="next"]'), "rowspan");
			colNext.html( preparePagerContent(colNext, pvtTbl.options.pagerContent.columnNext, colPage) );
		}

		var rowPage = pvtTbl.options.pivotTableConfig.RowPage;
		if (rowPage) {
			var rowPrev = mergeCells($t.find('th.pvtRow[data-key-type="prev"]'), "colspan");
			rowPrev.html( preparePagerContent(rowPrev, pvtTbl.options.pagerContent.rowPrev, rowPage) );

			var rowNext = mergeCells($t.find('th.pvtRow[data-key-type="next"]'), "colspan");
			rowNext.html( preparePagerContent(rowNext, pvtTbl.options.pagerContent.rowNext, rowPage) );
		}
	}

	var initTable = function (pvt, $tbl) {
		$tbl.addClass(pvt.options.tableClass);
		applySortStyle(pvt, $tbl);
		if (pvt.options.pivotTableConfig) {
			applyAxesLabels(pvt, $tbl, pvt.options.pivotTableConfig);
		}
		mergePagerPrevNext(pvt, $tbl);
	};

	NRecoPivotTable.prototype.load = function () {
		var pvt = this;

		pvt.element.addClass(pvt.options.loadingClass);
		pvt.options.renderTable(pvt, function (tblHtml, pvtInfo) {
			var $tbl = $(tblHtml);
			pvt.options.pivotTableConfig = pvtInfo;
			normalizeEnums(pvt.options.pivotTableConfig);

			initTable(pvt, $tbl);

			if (pvt.fixedHeaders)
				pvt.fixedHeaders.destroy();

			pvt.element.removeClass(pvt.options.loadingClass).html($tbl);

			if (pvt.options.fixedHeaders) {
				pvt.fixedHeaders = new NRecoFixedHeaders(pvt.element, $tbl, pvt.options.fixedHeadersSmooth);
			}
			pvt.element.trigger("pvt.loaded");
		});
	};

	NRecoPivotTable.prototype.getPivotTableConfig = function () {
		var cfgCopy = $.extend({}, this.options.pivotTableConfig);
		if (cfgCopy.SortByValue && cfgCopy.SortByValue.Axis == null)
			cfgCopy.SortByValue = null;
		if (!this.options.pager) {
			cfgCopy.ColumnPage = null;
			cfgCopy.RowPage = null;
		}
		return cfgCopy;
	};

	NRecoPivotTable.prototype.setPivotTableConfig = function (pvtTblCfg) {
		this.options.pivotTableConfig = pvtTblCfg;
	};

	NRecoPivotTable.prototype.destroy = function () {
		if (this.fixedHeaders)
			this.fixedHeaders.destroy();
	};

	NRecoPivotTable.prototype.refreshFixedHeaders = function () {
		if (this.fixedHeaders && this.fixedHeaders.resizeHandler)
			this.fixedHeaders.resizeHandler();
	};

	// -- START: Fixed headers plugin
	function NRecoFixedHeaders($containerElem, $t, smooth) {
		this.$containerElem = $containerElem;
		this.$t = $t;
		this.fixedByTop = [];
		this.fixedByLeft = [];
		this.smooth = smooth;
		this.init();
	}
	NRecoFixedHeaders.prototype.buildFixedHeaders = function () {
		var $scrollDiv = this.$containerElem;

		var $t = this.$t;
		var fixedHeaders = [];
		var fixedByTop = this.fixedByTop;
		var fixedByLeft = this.fixedByLeft;

		$scrollDiv.addClass("pvtFixedHeaderOuterContainer");

		var thNodes = $t[0].getElementsByTagName('TH');
		var rowDimElems = [];
		var colDimElems = [];
		for (var i = 0; i < thNodes.length; i++) {
			var th = thNodes[i];

			var isColumnDimLabel = th.className.indexOf('pvtColumnLabel') >= 0;
			var isRowDimLabel = th.className.indexOf('pvtRowLabel') >= 0;
			var isPvtColumn = !isColumnDimLabel && th.className.indexOf('pvtColumn') >= 0;
			var isPvtRow = !isRowDimLabel && th.className.indexOf('pvtRow') >= 0;

			var hdr = {
				th: th,
				width: th.clientWidth,
				height: th.clientHeight,
				isCol: isPvtColumn,
				isRow: isPvtRow
			};

			if (!isPvtColumn)
				hdr.left = 1;
			if (!isPvtRow) {
				hdr.top = 1;
			}

			fixedHeaders.push(hdr);

			if (isColumnDimLabel)
				colDimElems.push(th);
			if (isRowDimLabel)
				rowDimElems.push(th);

		}
		$t.addClass('pvtFixedHeader');

		for (var i = 0; i < fixedHeaders.length; i++) {
			var entry = fixedHeaders[i];
			var th = entry.th;
			var thHeight = entry.height;
			th.style.height = thHeight + "px";

			var wrapDiv = null;
			if (th.childNodes.length == 1 && th.childNodes[0].tagName == "DIV") {
				wrapDiv = th.childNodes[0];
				wrapDiv.className = "pvtFixedHeader";
				wrapDiv.style.height = thHeight + "px";
			} else {
				wrapDiv = document.createElement('div');
				wrapDiv.className = 'pvtFixedHeader';
				wrapDiv.style.height = thHeight + "px";
				if (entry.isCol || entry.isRow)
					wrapDiv.setAttribute('title', th.textContent);
				if (th.childNodes.length > 0) {
					while (th.childNodes.length > 0)
						wrapDiv.appendChild(th.childNodes[0]);
				} else {
					wrapDiv.textContent = th.textContent;
				}
				th.appendChild(wrapDiv);
			}
			
			if (entry.top!=null)
				fixedByTop.push( {
					el : wrapDiv, 
					top: entry.top,
					lastTop : entry.top,
					offsetLeft: entry.isCol ? -1 : null,
					width : entry.width
				});
			if (entry.left!= null)
				fixedByLeft.push( {
					el :wrapDiv,
					left: entry.left,
					lastLeft: entry.left,
					offsetTop: entry.isRow ? -1 : null,
					height : entry.height
				});
		}
		// lock table width
		var tWidth = $t.outerWidth();
		var tHeight = $t.outerHeight();
		$t.width($t.width());

		// wrap with overflow container div
		var containerDiv = document.createElement("div");
		containerDiv.style.width = tWidth + "px";
		containerDiv.style.height = tHeight + "px";
		containerDiv.style.overflow = "hidden";
		containerDiv.appendChild($t[0]);
		$scrollDiv[0].appendChild(containerDiv);
		this.wrapDiv = containerDiv;

		// build bg panels
		if (fixedHeaders.length > 0) {

			this.bgPanels = {
				containerDiv : containerDiv,
				colDimElems: colDimElems,
				rowDimElems: rowDimElems,
				firstTH : $t[0].querySelector('th:first-child'),

				calcHdrSize: function () {
					// calc row headers width and column headers height
					var colHdrHeight = 0;
					var rowHdrWidth = 0;
					for (var i = 0; i < this.colDimElems.length; i++) {
						rowHdrWidth = this.colDimElems[i].clientWidth + 1;
					}
					for (var i = 0; i < this.rowDimElems.length; i++) {
						colHdrHeight = this.rowDimElems[i].clientHeight + 1; //border
						rowHdrWidth += this.rowDimElems[i].clientWidth + 1;
					}
					if (this.firstTH != null && this.firstTH.className == "") {
						colHdrHeight += this.firstTH.clientHeight + 1;
						if (rowHdrWidth == 0 || rowDimElems.length == 0)
							rowHdrWidth += this.firstTH.clientWidth + 1;
					}
					this.colHdrHeight = colHdrHeight;
					this.rowHdrWidth = rowHdrWidth;
				},

				rowHdrPanel: null,
				rowHdrPanelFixedByLeft : null,

				colHdrPanel: null,
				colHdrPanelFixedByTop : null,

				topLeftHdrPanel: null,
				topLeftHdrPanelFixedByTop: null,
				topLeftHdrPanelFixedByLeft : null,

				setPanelSize : function(tWidth, tHeight) {
					this.calcHdrSize();

					var rowHdrPanelWidth = (this.rowHdrWidth + 1); //+1 border shift correction
					var colHdrPanelHeight = (this.colHdrHeight + 1);  //+1 border shift correction

					var extraW = 100; // use extra width to compensate possible UI repaint delays and avoid weird UX
					var extraH = 100; // use extra height to compensate possible UI repaint delays and avoid weird UX

					var rowHdrPanelLeft = -extraW;
					this.rowHdrPanel.style.height = tHeight + "px";
					this.rowHdrPanel.style.width = (rowHdrPanelWidth + extraW) + "px";
					this.rowHdrPanel.style.top = "-" + tHeight + "px";
					this.rowHdrPanel.style.left = rowHdrPanelLeft + "px";
					this.rowHdrPanelFixedByLeft.left = rowHdrPanelLeft;
					this.rowHdrPanelFixedByLeft.lastLeft = rowHdrPanelLeft;

					var colHdrPanelTop = -(tHeight * 2 + extraH);
					this.colHdrPanel.style.width = tWidth + "px";
					this.colHdrPanel.style.height = (colHdrPanelHeight + extraH) + "px";
					this.colHdrPanel.style.top = colHdrPanelTop + "px";
					this.colHdrPanelFixedByTop.top = colHdrPanelTop;
					this.colHdrPanelFixedByTop.lastTop = colHdrPanelTop;

					var topLeftHdrPanelLeft = -extraW;
					var topLeftHdrPanelTop = -(tHeight * 2 + colHdrPanelHeight + extraH + extraH);
					this.topLeftHdrPanel.style.width = (rowHdrPanelWidth + extraW) + "px";
					this.topLeftHdrPanel.style.height = (colHdrPanelHeight + extraH) + "px";
					this.topLeftHdrPanel.style.top = topLeftHdrPanelTop + "px";
					this.topLeftHdrPanel.style.left = topLeftHdrPanelLeft + "px";
					this.topLeftHdrPanelFixedByTop.top = topLeftHdrPanelTop;
					this.topLeftHdrPanelFixedByTop.lastTop = topLeftHdrPanelTop;
					this.topLeftHdrPanelFixedByLeft.left = topLeftHdrPanelLeft;
					this.topLeftHdrPanelFixedByLeft.lastLeft = topLeftHdrPanelLeft;
				},

				createPanels: function () {
					this.rowHdrPanel = document.createElement('div');
					this.rowHdrPanel.className = 'pvtFixedHeaderPanel pvtLeftFixedHeaderPanel';
					this.rowHdrPanelFixedByLeft = { el: this.rowHdrPanel };

					this.colHdrPanel = document.createElement('div');
					this.colHdrPanel.className = 'pvtFixedHeaderPanel pvtTopFixedHeaderPanel';
					this.colHdrPanelFixedByTop = { el: this.colHdrPanel };

					this.topLeftHdrPanel = document.createElement('div');
					this.topLeftHdrPanel.className = 'pvtFixedHeaderPanel pvtTopLeftFixedHeaderPanel';
					this.topLeftHdrPanelFixedByTop = { el: this.topLeftHdrPanel };
					this.topLeftHdrPanelFixedByLeft = { el: this.topLeftHdrPanel };
				}
			};

			containerDiv.style.height = tHeight + "px";

			this.bgPanels.createPanels();
			this.bgPanels.setPanelSize(tWidth, tHeight);
			containerDiv.appendChild(this.bgPanels.rowHdrPanel);
			containerDiv.appendChild(this.bgPanels.colHdrPanel);
			containerDiv.appendChild(this.bgPanels.topLeftHdrPanel);

			fixedByLeft.push(this.bgPanels.rowHdrPanelFixedByLeft);
			fixedByTop.push(this.bgPanels.colHdrPanelFixedByTop);

			fixedByLeft.push(this.bgPanels.topLeftHdrPanelFixedByLeft);
			fixedByTop.push(this.bgPanels.topLeftHdrPanelFixedByTop);

		}
		fixedHeaders = null;
	};
	NRecoFixedHeaders.prototype.refreshHeaders = function (top, left, width, height) {
		var newPos;
		var entry;
		var checkOffset;
		var refreshPos;
		var bottom = top + height;
		var right = left + width;
		var fixedByLeft = this.fixedByLeft;
		for (var i = 0; i < fixedByLeft.length; i++) {
			entry = fixedByLeft[i];
			checkOffset = entry.offsetTop != null;
			refreshPos = !checkOffset;
			if (checkOffset) {
				if (entry.offsetTop < 0)
					entry.offsetTop = entry.el.offsetTop;
				refreshPos = ((entry.offsetTop + entry.height) >= top) && entry.offsetTop <= bottom;
			}
			if (refreshPos) {
				newPos = (left + entry.left);
				if (newPos != entry.lastLeft) {
					entry.lastLeft = newPos;
					entry.el.style.left = newPos + "px";
				}
			}
		}

		var fixedByTop = this.fixedByTop;
		for (var i = 0; i < fixedByTop.length; i++) {
			entry = fixedByTop[i];
			checkOffset = entry.offsetLeft != null;
			refreshPos = !checkOffset;
			if (checkOffset) {
				if (entry.offsetLeft < 0)
					entry.offsetLeft = entry.el.offsetLeft;
				refreshPos = ((entry.offsetLeft + entry.width) >= left) && entry.offsetLeft <= right;
			}
			if (refreshPos) {
				newPos = (top + entry.top);
				if (newPos != entry.lastTop) {
					entry.lastTop = newPos;
					entry.el.style.top = newPos + "px";
				}
			} 
		}

	};

	NRecoFixedHeaders.prototype.destroy = function () {
		this.$containerElem.off("scroll");
		if (this.resizeHandler)
			$(window).off('resize', this.resizeHandler);
		this.fixedByTop = null;
		this.fixedByLeft = null;
		this.$containerElem = null;
		this.wrapDiv = null;
		this.$t = null;
	};

	NRecoFixedHeaders.prototype.init = function () {
		this.buildFixedHeaders();
		var prevTop = -1;
		var prevLeft = -1;
		var scrollElem = this.$containerElem[0];
		var timeout = null;
		var instance = this;
		var containerWidth = this.$containerElem.outerWidth();
		var containerHeight = this.$containerElem.outerHeight();

		var refreshHandler = this.smooth  ?
			instance.refreshHeaders :
			function (top,left, width, height) {
				if (timeout)
					clearTimeout(timeout);
				this.wrapDiv.className = "pvtFixedHeadersOutdated";
				timeout = setTimeout(function () {
					timeout = null;
					instance.refreshHeaders(top, left, width, height);
					instance.wrapDiv.className = "";
				}, 300);
			};

		this.$containerElem.on("scroll", function (evt) {
			var top = scrollElem.scrollTop;
			var left = scrollElem.scrollLeft;
			if (top != prevTop || left!=prevLeft) {
				prevTop = top;
				prevLeft = left;
				refreshHandler.call(instance, top, left, containerWidth, containerHeight);
			}
		});
		this.$containerElem.scroll();

		var wrapWidth = this.wrapDiv.clientWidth;
		var tHeight = this.$t.outerHeight();
		var fullWidth = false;
		this.resizeHandler = function () {
			var cWidth = instance.$containerElem.innerWidth();
			var resizePanels = false;
			if (cWidth > wrapWidth) {
				instance.wrapDiv.style.width = '100%';
				instance.$t[0].style.width = '100%';
				fullWidth = true;
				resizePanels = true;
			} else {
				if (fullWidth) {
					fullWidth = false;
					resizePanels = true;
					instance.wrapDiv.style.width = wrapWidth + "px";
					instance.$t[0].style.width = wrapWidth + "px";
				}
			}
			if (resizePanels)
				instance.bgPanels.setPanelSize(instance.$t.outerWidth(), tHeight);
			// recalc positions for new container size
			containerWidth = instance.$containerElem.outerWidth();
			containerHeight = instance.$containerElem.outerHeight();
			prevTop = -1;
			prevLeft = -1;
			instance.$containerElem.scroll();
		};
		$(window).on('resize', this.resizeHandler);
	};
	// -- END: Fixed headers plugin 

	$.fn.nrecoPivotTable = function (options) {
		if (typeof options == "string") {
			var instance = this.data('_nrecoPivotTable');
			if (instance && (typeof instance[options]) == "function") {
				return instance[options].apply(instance, Array.prototype.slice.call(arguments, 1));
			} else {
				$.error('Method ' + options + ' does not exist');
			}
		}
		return this.each(function () {
			var opts = $.extend({}, $.fn.nrecoPivotTable.defaults, options);
			var $holder = $(this);

			if (!$.data(this, '_nrecoPivotTable')) {
				$.data(this, '_nrecoPivotTable', new NRecoPivotTable($holder, opts));
			}
		});

	};

	$.fn.nrecoPivotTable.defaults = {
		renderTable: function (settings, callback) { alert('renderTable is not defined!'); },
		sortColumnSelector: 'th.pvtColumn[data-sort-index]',
		sortRowSelector: 'th.pvtRow[data-sort-index]',
		tableClass: 'table pvtTable',
		sortColumnLabelSelector: 'th.pvtColumnLabel',
		sortRowLabelSelector: 'th.pvtRowLabel',
		pagerSelector : 'th .pvtPager',
		loadingClass: "pvtLoading",
		autoload: false,
		fixedHeaders: null,  // true/false or null = autodetect by overflow:auto
		fixedHeadersSmooth: true,
		pager : true,
		pagerContent : {
			columnPrev: '<span class="pvtPager glyphicon glyphicon-chevron-left" title="Previous ({prevPage}/{lastPage})"></span>', // or function(pagerData) that returns content
			columnNext: '<span class="pvtPager glyphicon glyphicon-chevron-right" title="Next ({nextPage}/{lastPage})"></span>',
			rowPrev: '<span class="pvtPager glyphicon glyphicon-chevron-up" title="Previous ({prevPage}/{lastPage})"></span>',
			rowNext: '<span class="pvtPager glyphicon glyphicon-chevron-down" title="Next ({nextPage}/{lastPage})"></span>'
		},
		pivotTableConfig: {
			SortByValue: {},
			OrderKeys: [],
			ColumnPage: null,  // example: { Offset: 0, Limit : 10 }
			RowPage : null
		}
	};

	$.fn.nrecoPivotTable.version = 1.3;

})(jQuery);