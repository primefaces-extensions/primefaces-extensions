/**
 * PrimeFaces Extensions Sheet Widget.
 * 
 * @author Melloware 
 * @author Mark Lassiter 
 * @since 6.2
 */
PrimeFaces.widget.ExtSheet = PrimeFaces.widget.BaseWidget.extend({
    
    // flag tracking whether or not an update ajax event needs fired
    // after a select cell
    updated: false,
    // flag tracking whether a filter event needs fired after a focusin
    filterChanged: false,

    // initialize the component
    init: function (cfg) {
        this._super(cfg);
        // store off jquery wrappers
        this.sheetDiv = $(this.jqId);
        this.tableDiv = $(this.jqId + '_tbl');
        this.dataInput = $(this.jqId + '_input');
        this.selectionInput = $(this.jqId + '_selection');
        this.sortByInput = $(this.jqId + '_sortby');
        this.sortOrderInput = $(this.jqId + '_sortorder');
        this.focusInput = $(this.jqId + '_focus');
        // need to track to avoid recursion
        this.focusing = false;
        // create table
        this.setupHandsonTable();
    },
    
    setupHandsonTable: function() {
        var $this = this;
        var options = {
            data: $this.cfg.data,
            colHeaders: $this.cfg.colHeaders,
            rowHeaders: $this.cfg.rowHeaders,
            columns: $this.cfg.columns,
            stretchH: $this.cfg.stretchH || 'all',
            contextMenu: false,
            autoRowSize: !$this.cfg.rowHeaders,
            enterMoves: {
                row: 0,
                col: 1
            },
            cellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.cellTypes.text.renderer.apply(this, arguments);

                var styleClass = '';
                // append row style (if we have one)
                var rowClass = $this.cfg.rowStyles[row];
                if (rowClass) {
                    styleClass = rowClass;
                }
                // append cell style (if we have one)
                var cellClass = $this.cfg.styles['r' + row + '_c' + col];
                if (cellClass) {
                    styleClass = styleClass.concat(' ').concat(cellClass);
                }
                // check for errors
                var invalidMessage = $this.cfg.errors[$this.cfg.rowKeys[row] + '_c' + col];
                if (invalidMessage) {
                    styleClass = styleClass.concat(' ui-message-error');
                    td.innerHTML = "<span class='ui-sheet-error' title='" + invalidMessage
                        + "'><span class='ui-outputlabel-rfi'>*</span>" + value + "</span>";
                }
                // every other row highlighting
                if (row % 2 == 1) {
                    styleClass = styleClass.concat(' ui-datatable-odd');
                }
                td.className = td.className.concat(' ').concat(styleClass);
            },
            cells: function (row, col, prop) {
                var cp = {};
                cp.renderer = this.cellRenderer;
                var readonly = $this.cfg.readOnly['r' + row + '_c' + col];
                if (readonly) {
                    cp.readOnly = true;
                }
                return cp;
            },
            afterChange: function (change, source) {
                if (source === 'loadData') {
                    return;
                }
                var isChanged = false;
                for (var i = 0; i < change.length; i++) {
                    if (change[i][2] == change[i][3])
                        continue;
                    var row = change[i][0];
                    var col = change[i][1];
                    change[i].push($this.cfg.rowKeys[row]);
                    $this.cfg.delta['r' + row + '_c' + col] = change[i];
                    isChanged = true;
                }
                if (isChanged) {
                    $this.dataInput.val(JSON.stringify($this.cfg.delta));
                    $this.updated = true;
                }
            },
            afterSelectionEnd: function (r, c, r2, c2) {
                var sel = [r, c, r2, c2];
                $this.selectionInput.val(JSON.stringify(sel));
                if ($this.updated) {
                    $this.updated = false;
                    if ($this.hasBehavior('change')) {
                        $this.cfg.behaviors['change'].call(this, 'change');
                    }
                } else {
                    if ($this.hasBehavior('cellSelect')) {
                        $this.cfg.behaviors['cellSelect'].call(this, 'cellSelect');
                    }
                }
            },
            afterOnCellMouseDown: function (event, coords, TD) {
                var sel = [coords.row, coords.col, coords.row, coords.col];
                $this.selectionInput.val(JSON.stringify(sel));

                // only fire event if row is -1 which means its a header
                if ((coords.row == -1 && coords.col != -1) && $this.hasBehavior('columnSelect')) {
                    $this.cfg.behaviors['columnSelect'].call(this, 'columnSelect');
                }

                // only fire event if col is -1 which means its a header
                if ((coords.col == -1 && coords.row != -1) && $this.hasBehavior('rowSelect')) {
                    $this.cfg.behaviors['rowSelect'].call(this, 'rowSelect');
                }
            },
            afterDeselect: function () {
                if ($this.updated) {
                    $this.updated = false;
                    if ($this.hasBehavior('change')) {
                        $this.cfg.behaviors['change'].call(this, 'change');
                    }
                }
            },
            afterGetColHeader: function (col, TH) {
                // handle sorting
                var sortable = $this.cfg.sortable[col];
                if (sortable) {
                    $(TH).find('.relative .ui-sortable-column-icon').remove();
                    var sortCol = $this.sortByInput.val();
                    var sortOrder = $this.sortOrderInput.val();
                    var iconclass = 'ui-sortable-column-icon ui-icon ui-icon ui-icon-carat-2-n-s ';
                    if (sortCol == col) {
                        iconclass = iconclass
                            + (sortOrder == 'ascending' ? 'ui-icon-triangle-1-n' : 'ui-icon-triangle-1-s');
                        $(TH).addClass('ui-state-active');
                    } else {
                        $(TH).removeClass('ui-state-active');
                    }
                    $(TH).find('.relative').append("<span class='" + iconclass + "'></span>");
                    $(TH).addClass('ui-sortable');
                    $(TH).off('click').click(function (e) {
                        $this.sortClick($this, e, col);
                    });
                } else {
                    $(TH).removeClass('ui-state-active');
                }

                // handle filtering
                var f = $this.cfg.filters[col];
                if (typeof (f) != "undefined" && f != 'false') {
                    $(TH).find('.handson-filter').remove();
                    var v = $($this.jqId + '_filter_' + col).val();
                    var filterId = $this.id + '_f' + col;
                    if (f == 'true') {
                        $(TH)
                            .append(
                                '<span class="handson-filter"><input type="text" id="'
                                + filterId
                                + '" class="ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all" role="textbox" aria-disabled="false" aria-readonly="false" aria-multiline="false" value="'
                                + v + '" /></span>');
                        $(TH).find('input').change(function () {
                            $this.filterchange($this, col, this.value, false)
                        }).keydown(function (e) {
                            $this.keyDown($this, e)
                        }).keyup(function (e) {
                            $this.keyUp($this, e)
                        }).focusin(function () {
                            $this.filterFocusIn($this, this)
                        }).focusout(function () {
                            $this.filterFocusOut($this, this)
                        });
                    } else {
                        $(TH)
                            .append(
                                '<span class="handson-filter"><select id="'
                                + filterId
                                + '" class="ui-column-filter ui-widget ui-state-default ui-corner-left" ></select></span>');
                        var s = $(TH).find('select');
                        for (var i = 0; i < f.length; i++) {
                            s.append('<option value="' + f[i].value + '"'
                                + (f[i].value == v ? ' selected="selected"' : '') + '>' + f[i].label
                                + '</option>');
                        }
                        $(TH).find('select').change(function () {
                            $this.filterchange($this, col, this.value, true)
                        }).keydown(function (e) {
                            $this.keyDown($this, e)
                        }).keyup(function (e) {
                            $this.keyUp($this, e)
                        }).focusin(function () {
                            $this.filterFocusIn($this, this)
                        }).focusout(function () {
                            $this.filterFocusOut($this, this)
                        });
                    }
                }
            }
        };

        if ($this.cfg.fixedColumnsLeft)
            options.fixedColumnsLeft = $this.cfg.fixedColumnsLeft;

        if ($this.cfg.fixedRowsTop)
            options.fixedRowsTop = $this.cfg.fixedRowsTop;
        
        if ($this.cfg.manualColumnResize)
            options.manualColumnResize = $this.cfg.manualColumnResize;

        if ($this.cfg.manualRowResize)
            options.manualRowResize = $this.cfg.manualRowResize;
        
        if ($this.cfg.manualColumnMove)
            options.manualColumnMove = $this.cfg.manualColumnMove;

        if ($this.cfg.manualRowMove)
            options.manualRowMove = $this.cfg.manualRowMove;

        if ($this.cfg.height)
            options.height = $this.cfg.height;

        if ($this.cfg.currentColClassName)
            options.currentColClassName = $this.cfg.currentColClassName;

        if ($this.cfg.currentRowClassName)
            options.currentRowClassName = $this.cfg.currentRowClassName;

        $this.tableDiv.handsontable(options);
        $this.ht = $this.tableDiv.data('handsontable');

        // prevent column clicks from selecting entire column, we use it for sort
        // We were seeing an issue with this change and how it affected the columnSelect ajax action
        // so we needed to NOT enabled this behavior if the given sheet has the ajax function defined.
        // TODO may make this conditional on whether or not sorting is enabled
        if (!($this.hasBehavior('columnSelect'))) {
            Handsontable.hooks.add('beforeOnCellMouseDown',
                    $this.handleHotBeforeOnCellMouseDown, $this.ht);
        }

        // Check if data exist. If not insert No Records Found message
        if (options.data.length == 0) {
            $this.tableDiv.find('tbody')
                .html(
                    "<tr><td colspan='" + options.columns.length + "'>" + $this.cfg.emptyMessage
                    + "</td></tr>")
        }

        var selval = $this.selectionInput.val();
        if (selval && selval.length > 0) {
            var sel = JSON.parse(selval);
            $this.ht.selectCell(sel[0], sel[1], sel[2], sel[3], true);
        }
        var focusId = $this.focusInput.val();
        if (focusId && focusId.length > 0) {
            focusId = focusId.replace(":", "\\:");
            // for some reason does not work when focused immediately,
            // dom node hasn't attached
            setTimeout(function () {
                $('#' + focusId).focus()
            }, 100);
        }
        
        // repaint HT when the document is loaded to fix all column widths
        $(document).ready(function(){
            $this.ht.render();
        }); 
    },

    // updates the row with the new data value
    updateData: function (rowKey, v) {
        for (var i = 0; i < this.cfg.rowKeys.length; i++)
            if (this.cfg.rowKeys[i] == rowKey) {
                this.cfg.data[i] = v;
                return;
            }
    },

    // true if sheet has assigned behavior, otherwise false
    hasBehavior: function (event) {
        if (this.cfg.behaviors) {
            return this.cfg.behaviors[event] != undefined;
        }
        return false;
    },

    // fired when a filter input is edited. firenow indicates the filter
    // event should be fired immediately (select)
    filterchange: function (sheet, col, v, firenow) {
        $(sheet.jqId + '_filter_' + col).val(v);
        sheet.filterChanged = true;

        if (firenow) {
            sheet.filter();
        }
    },

    // fired when a sortable column is clicked
    sortClick: function (sheet, e, col) {
        if ($(e.target).is(':not(th,span,div)'))
            return;
        var sc = sheet.sortByInput.val();
        if (col == sc) {
            var so = sheet.sortOrderInput.val();
            sheet.sortOrderInput.val((so == 'ascending' ? 'descending' : 'ascending'));
        } else {
            sheet.sortOrderInput.val('ascending');
            sheet.sortByInput.val(col);
        }
        // destroy editor to avoid posting request after resort
        sheet.ht.destroyEditor(true);
        if (sheet.hasBehavior('sort'))
            sheet.cfg.behaviors['sort'].call(this, 'sort');
    },

    // eat enter keys for filter inputs so they do not submit form
    keyDown: function (sheet, e) {
        e.stopImmediatePropagation();
        var key = e.which, keyCode = $.ui.keyCode;
        if ((key === keyCode.ENTER || key === keyCode.NUMPAD_ENTER)) {
            e.preventDefault();
        }
    },

    // again, eat enter key. but also fire filter event on enter
    keyUp: function (sheet, e) {
        e.stopImmediatePropagation();
        var key = e.which, keyCode = $.ui.keyCode;
        if ((key === keyCode.ENTER || key === keyCode.NUMPAD_ENTER)) {
            // destroy editor to avoid posting request after resort
            sheet.ht.destroyEditor(true);

            $(e.target).change();
            sheet.filter();
            e.preventDefault();
        }
    },

    // keep track of focused filter input. if previous filter altered,
    // fire filter event
    filterFocusIn: function (sheet, inp) {
        // if this call is the result of jQuery setFocus, exit
        if (sheet.focusing)
            return;

        // destroy editor to avoid posting request after resort
        // this causes us to lose focus, so we need to refocus
        // we need to prevent recursion with this hack
        sheet.focusing = true;
        sheet.focusInput.val($(inp).attr('id'));
        sheet.ht.destroyEditor(true);
        $(inp).focus();
        sheet.focusing = false;

        sheet.filter();
    },

    // remove focused filter tracking when tabbing off
    filterFocusOut: function (sheet, inp) {
        sheet.filter();
        sheet.focusInput.val(null);
    },
    
    // clear currently stored input and deltas
    clearDataInput: function () {
        this.cfg.delta = {};
        this.dataInput.val('');
    },
    
    // tell handstontable to repaint itself
    redraw: function () {
        if (this.ht) {
            this.ht.render();
        }
    },
    
    // run filtering if it has changed
    filter: function () {
        if (this.filterChanged && this.hasBehavior('filter')) {
            this.filterChanged = false;
            this.cfg.behaviors['filter'].call(this, 'filter');
        }
    },
    
    // Remove the row from the sheet
    removeRow: function (index) {
        if (this.ht) {
           this.ht.alter('remove_row', index);
        }
    },

    // method to prevent selection of cells on column header click
    handleHotBeforeOnCellMouseDown: function (event, coords, element) {
        if (coords.row < 0) {
            event.stopImmediatePropagation();
        }
    }
});
