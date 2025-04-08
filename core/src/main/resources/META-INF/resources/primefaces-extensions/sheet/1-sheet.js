/**
 * PrimeFaces Extensions Sheet Widget.
 *
 * @author Melloware
 * @author Mark Lassiter
 * @since 6.2
 */
PrimeFaces.widget.ExtSheet = PrimeFaces.widget.DeferredWidget.extend({

    // flag tracking whether an update ajax event needs fired
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
        this.keyboardTrap = $(this.jqId + '_keyboard');
        this.selectionInput = $(this.jqId + '_selection');
        this.sortByInput = $(this.jqId + '_sortby');
        this.sortOrderInput = $(this.jqId + '_sortorder');
        // need to track to avoid recursion
        this.focusing = false;

        // user extension to configure handsontable
        let extender = this.cfg.extender
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        this.renderDeferred();
    },

    _render: function () {
        let $this = this;
        let options = {
            data: $this.cfg.data,
            colHeaders: $this.cfg.colHeaders,
            rowHeaders: $this.cfg.rowHeaders,
            columns: $this.cfg.columns,
            stretchH: $this.cfg.stretchH || 'all',
            selectionMode: $this.cfg.selectionMode || 'multiple',
            contextMenu: false,
            allowInvalid: false,
            autoRowSize: !$this.cfg.rowHeaders,
            enterMoves: {
                row: 0,
                col: 1
            },
            textCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                let doc = new DOMParser().parseFromString(value, "text/html");
                value = doc.documentElement.textContent || value;
                Handsontable.renderers.HtmlRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            passwordCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.PasswordRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            numericCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.NumericRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            checkboxCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.CheckboxRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            dateCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.DateRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            timeCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.TimeRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            dropdownCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.DropdownRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            autocompleteCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
                Handsontable.renderers.AutocompleteRenderer.apply(this, arguments);
                $this._defaultCellRenderer(instance, td, row, col, prop, value, cellProperties);
            },
            cells: function (row, col, prop) {
                let cp = {};
                if (col < 0) {
                    return cp;
                }
                let column = $this.cfg.columns[col];
                if (column.type === 'password') {
                    cp.renderer = this.passwordCellRenderer;
                } else if (column.type === 'numeric') {
                    cp.renderer = this.numericCellRenderer;
                } else if (column.type === 'checkbox') {
                    cp.renderer = this.checkboxCellRenderer;
                } else if (column.type === 'date') {
                    cp.renderer = this.dateCellRenderer;
                } else if (column.type === 'time') {
                    cp.renderer = this.timeCellRenderer;
                } else if (column.type === 'dropdown') {
                    cp.renderer = this.dropdownCellRenderer;
                } else if (column.type === 'autocomplete') {
                    cp.renderer = this.autocompleteCellRenderer;
                } else {
                    cp.renderer = this.textCellRenderer;
                }
                let readonlyCell = $this.cfg.readOnlyCells['r' + row + '_c' + col];
                if (readonlyCell) {
                    cp.readOnly = true;
                }
                return cp;
            },
            afterChange: function (change, source) {
                if (source === 'loadData') {
                    return;
                }
                // let change = changes[0]; // [row, prop, oldVal, newVal]
                let isChanged = false;
                let cellType = 'normal';
                for (let i = 0; i < change.length; i++) {
                    let oldValue = change[i][2];
                    let newValue = change[i][3];
                    if (oldValue === newValue)
                        continue;
                    let row = change[i][0];
                    let col = change[i][1];
                    change[i].push($this.cfg.rowKeys[row]);
                    $this.cfg.delta['r' + row + '_c' + col] = change[i];
                    isChanged = true;
                    cellType = this.getCellMeta(row, col).type;
                }
                if (isChanged) {
                    $this.dataInput.val(JSON.stringify($this.cfg.delta));
                    $this.updated = true;

                    // GitHub #599
                    if (cellType === "numeric" || cellType === "checkbox" || cellType === "dropdown" || cellType === "autocomplete" || cellType === "date") {
                        $this.updated = false;
                        $this.callBehavior('change');
                    }
                }
            },
            afterSelectionEnd: function (r, c, r2, c2) {
                let selected = this.getSelected() || [];
                $this.selectionInput.val(JSON.stringify(selected));
                if ($this.updated) {
                    $this.updated = false;
                    $this.callBehavior('change');
                } else {
                    $this.callBehavior('cellSelect');
                }
            },
            afterOnCellMouseDown: function (event, coords, TD) {
                let sel = [coords.row, coords.col, coords.row, coords.col];
                $this.selectionInput.val(JSON.stringify(sel));

                // only fire event if row is -1 which means its a header
                if ((coords.row == -1 && coords.col != -1) && $this.hasBehavior('columnSelect')) {
                    $this.callBehavior('columnSelect');
                }

                // only fire event if col is -1 which means its a header
                if ((coords.col == -1 && coords.row != -1) && $this.hasBehavior('rowSelect')) {
                    $this.callBehavior('rowSelect');
                }
            },
            afterDeselect: function () {
                if ($this.updated) {
                    $this.updated = false;
                    $this.callBehavior('change');
                }
            },
            afterListen: function () {
                // turn off the keyboard trap while sheet has keyboard control
                let tabIndex = $this.keyboardTrap.attr("tabindex");
                if (tabIndex) {
                    $this.keyboardTrap.attr("tabindex", "-1").data("tabindex", tabIndex);
                }
            },
            afterUnlisten: function () {
                // turn on the keyboard trap once sheet no longer has keyboard control
                PrimeFaces.queueTask(() => {
                    let tabIndex = $this.keyboardTrap.data("tabindex");
                    if (tabIndex) {
                        $this.keyboardTrap.attr("tabindex", tabIndex).data("tabindex", "");
                    }
                }, 250)
            },
            afterGetColHeader: function (col, TH) {
                let header = $(TH);

                // remove all current events
                header.off();

                // handle sorting
                let sortable = $this.cfg.sortable[col];
                if (sortable) {
                    header.find('.relative .ui-sortable-column-icon').remove();
                    let sortCol = $this.sortByInput.val();
                    let sortOrder = $this.sortOrderInput.val();
                    let iconclass = 'ui-sortable-column-icon ui-icon ui-icon ui-icon-carat-2-n-s ';
                    if (sortCol == col) {
                        iconclass = iconclass
                            + (sortOrder == 'ascending' ? 'ui-icon-triangle-1-n' : 'ui-icon-triangle-1-s');
                        header.addClass('ui-state-active');
                    } else {
                        header.removeClass('ui-state-active');
                    }
                    header.find('.relative').append("<span class='" + iconclass + "'></span>");
                    header.addClass('ui-sortable ui-sortable-column');
                    header.off().on("click.sheetheader", function (e) {
                        $this.sortClick($this, e, col);
                    });
                } else {
                    header.removeClass('ui-state-active');
                }

                // handle filtering
                let f = $this.cfg.filters[col];
                if (typeof (f) != "undefined" && f != 'false') {
                    header.addClass('ui-filter-column');
                    header.find('.handson-filter').remove();
                    let v = $($this.jqId + '_filter_' + col).val();
                    if (f == 'true') {
                        header
                            .append(
                                '<span class="handson-filter"><input type="text" class="ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all" role="textbox" aria-disabled="false" aria-readonly="false" aria-multiline="false" value="'
                                + v + '"></input></span>');
                        header.find('input').off().on('change.sheetfilter', function () {
                            $this.filterchange($this, col, this.value, false)
                        }).on('keydown.sheetfilter', function (e) {
                            $this.filterKeyDown($this, e)
                        }).on('keyup.sheetfilter', function (e) {
                            $this.filterKeyUp($this, e)
                        }).on('mouseover.sheetfilter', function (e) {
                            $this.filterMouseOver($this, e)
                        }).on('focusin.sheetfilter', function () {
                            $this.filterFocusIn($this, this)
                        }).on('focusout.sheetfilter', function () {
                            $this.filterFocusOut($this, this)
                        });
                    } else {
                        header
                            .append(
                                '<span class="handson-filter"><select class="ui-column-filter ui-widget ui-state-default ui-corner-left" ></select></span>');
                        let selectInput = header.find('select');
                        for (let i = 0; i < f.length; i++) {
                            selectInput.append('<option value="' + f[i].value + '"'
                                + (f[i].value == v ? ' selected="selected"' : '') + '>' + f[i].label
                                + '</option>');
                        }
                        selectInput.off().on('change.sheetfilter', function () {
                            $this.filterchange($this, col, this.value, true)
                        }).on('keydown.sheetfilter', function (e) {
                            $this.filterKeyDown($this, e)
                        }).on('keyup.sheetfilter', function (e) {
                            $this.filterKeyUp($this, e)
                        }).on('mouseover.sheetfilter', function (e) {
                            $this.filterMouseOver($this, e)
                        }).on('focusin.sheetfilter', function () {
                            $this.filterFocusIn($this, this)
                        });
                    }
                }
            }
        };

        // make a copy of the configuration
        let configuration = $.extend(true, {}, $this.cfg);

        // remove any properties we don't want in the options
        delete configuration["readOnlyCells"];
        delete configuration["rowKeys"];
        delete configuration["columns"];
        delete configuration["data"];
        delete configuration["delta"];
        delete configuration["errors"];
        delete configuration["filters"];
        delete configuration["rowKeys"];
        delete configuration["sortable"];
        delete configuration["styles"];
        delete configuration["rowStyles"];

        // merge configuration into options
        $.extend(options, configuration);

        // create the handsontable
        $this.tableDiv.handsontable(options);
        $this.ht = $this.tableDiv.data('handsontable');

        // prevent column clicks from selecting entire column, we use it for sort
        // We were seeing an issue with this change and how it affected the columnSelect ajax action
        // so we needed to NOT enabled this behavior if the given sheet has the ajax function defined.
        if (!($this.hasBehavior('columnSelect'))) {
            $this.ht.addHook('beforeOnCellMouseDown', $this.handleHotBeforeOnCellMouseDown);
        }

        // add before key down hook
        $this.ht.allowTabOffSheet = $this.cfg.allowTabOffSheet;
        $this.ht.addHook('beforeKeyDown', $this.handleHotBeforeKeyDown);

        // fix tbody
        $('.htCore > tbody').addClass('ui-datatable-data ui-widget-content');

        // Check if data exist. If not insert No Records Found message
        if (options.data.length === 0) {
            let colspan = options.columns.length;
            colspan++;
            $this.tableDiv.find('.emptyRows').find('tbody').html("<tr><td colspan='" + colspan + "'>" + $this.cfg.emptyMessage + "</td></tr>")
        }

        let selval = $this.selectionInput.val();
        if (selval && selval.length > 0) {
            let sel = JSON.parse(selval);
            $this.ht.selectCell(sel[0], sel[1], sel[2], sel[3], true);
        }
    },

    _defaultCellRenderer: function (instance, td, row, col, prop, value, cellProperties) {
        let styleClass = '';
        // append row style (if we have one)
        let rowClass = this.cfg.rowStyles[row];
        if (rowClass) {
            styleClass = rowClass;
        }
        // append cell style (if we have one)
        let cellClass = this.cfg.styles['r' + row + '_c' + col];
        if (cellClass) {
            styleClass = styleClass.concat(' ').concat(cellClass);
        }
        // check for errors
        let invalidMessage = this.cfg.errors[this.cfg.rowKeys[row] + '_c' + col];
        if (invalidMessage) {
            styleClass = styleClass.concat(' ui-message-error');
            td.innerHTML = "<span class='ui-sheet-error' title='" + invalidMessage
                + "'><span class='ui-outputlabel-rfi'>*</span>" + value + "</span>";
        }
        // every other row highlighting
        if (row % 2 == 1) {
            styleClass = styleClass.concat(' ui-datatable-odd');
        } else {
            styleClass = styleClass.concat(' ui-datatable-even');
        }
        td.className = td.className.concat(' ').concat(styleClass);
    },

    //@Override
    refresh: function (cfg) {
        // clean up HT memory
        if (this.ht) {
            this.ht.destroy();
            this.ht = null;
        }

        this._super(cfg);
    },

    //@Override
    destroy: function () {
        this._super();

        // clean up HT memory
        if (this.ht) {
            this.ht.destroy();
        }
    },

    /**
     * Updates the row with the new data value
     *
     * @param rowIndex the row index
     * @param data the array of data values for the columns
     * @param styles the JSON cell style updates
     * @param readOnlyCells the JSON cell read only updates
     */
    updateData: function (rowIndex, data, styles, readOnlyCells) {
        // merge the new styles in
        $.extend(this.cfg.styles, styles);
        //merge the new readonly cells in
        $.extend(this.cfg.readOnlyCells, readOnlyCells);

        // update any data rows
        if (rowIndex <= this.cfg.rowKeys.length) {
            this.cfg.data[rowIndex] = data;
        }
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
        let sc = sheet.sortByInput.val();
        if (col == sc) {
            let so = sheet.sortOrderInput.val();
            sheet.sortOrderInput.val((so == 'ascending' ? 'descending' : 'ascending'));
        } else {
            sheet.sortOrderInput.val('ascending');
            sheet.sortByInput.val(col);
        }
        // destroy editor to avoid posting request after resort
        sheet.ht.destroyEditor(true);
        sheet.ht.deselectCell();

        sheet.callBehavior('sort');
    },

    // eat enter keys for filter inputs so they do not submit form
    filterKeyDown: function (sheet, e) {
        e.stopImmediatePropagation();
        let key = e.which, keyCode = $.ui.keyCode;
        if (key === keyCode.ENTER) {
            e.preventDefault();
        }
    },

    // again, eat enter key. but also fire filter event on enter
    filterKeyUp: function (sheet, e) {
        e.stopImmediatePropagation();
        let key = e.which, keyCode = $.ui.keyCode;
        if (key === keyCode.ENTER) {
            // destroy editor to avoid posting request after resort
            sheet.ht.destroyEditor(true);
            sheet.ht.deselectCell();

            $(e.target).trigger('change');
            sheet.filter();
            e.preventDefault();
        }
    },

    // to alleviate focus issues focus on mouse over
    filterMouseOver: function (sheet, e) {
        $(e.target).focus();
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
        sheet.ht.destroyEditor(true);
        sheet.ht.deselectCell();
        // for some reason does not work when focused immediately,
        PrimeFaces.queueTask(() => {
            $(inp).focus();
            sheet.focusing = false;
        }, 150);
    },

    // remove focused filter tracking when tabbing off
    filterFocusOut: function (sheet, inp) {
        // if this call is the result of jQuery setFocus, exit
        if (sheet.focusing)
            return;

        sheet.filter();
    },

    // clear currently stored input and deltas
    clearDataInput: function () {
        this.cfg.delta = {};
        this.dataInput.val('');
    },

    // clear all the filters
    clearFilters: function () {
        $("input[id^='" + this.id + "_filter_']").val("");
        this.filterChanged = true;
        this.filter();
    },

    // tell handstontable to repaint itself
    redraw: function () {
        if (this.ht) {
            this.ht.render();
        }
    },

    // #741 focus the handsontable https://stackoverflow.com/questions/11007947/how-to-onload-set-focus-to-jquery-handsontable
    focus: function () {
        if (this.ht) {
            this.ht.selectCell(0, 0);
        }
    },

    // run filtering if it has changed
    filter: function () {
        if (this.filterChanged && this.hasBehavior('filter')) {
            this.filterChanged = false;
            this.callBehavior('filter');
        }
    },

    // Remove the row from the sheet
    removeRow: function (index) {
        if (this.ht) {
            this.ht.alter('remove_row', index);
        }
    },

    focusFirstError: function () {
        let errors = this.tableDiv.find('.ui-message-error');
        if (errors.length > 0) {
            let firstError = errors.first();
            let col = firstError.index() - 1;
            let row = firstError.parent().index();
            this.ht.selectCell(row, col);
        }
    },

    // method to prevent selection of cells on column header click
    handleHotBeforeOnCellMouseDown: function (event, coords, element) {
        if (coords.row < 0) {
            event.stopImmediatePropagation();
        }
    },

    handleHotBeforeKeyDown: function (e) {
        let selectedLast = this.getSelectedLast();
        if (!selectedLast) {
            return;
        }
        let row = selectedLast[0];
        let col = selectedLast[1];
        let celltype = this.getCellMeta(row, col).type;

        let evt = e || window.event; // IE support
        let key = evt.charCode || evt.keyCode || 0;
        let shiftDown = e.shiftKey;

        // #740 tab on last cell should focus this next component
        if (this.allowTabOffSheet && key == 9) {
            let lastRow = this.countRows() - 1;
            let lastCol = this.countCols() - 1;
            if ((!shiftDown && row === lastRow && col === lastCol)
                || (shiftDown && row === 0 && col === 0)) {
                e.stopImmediatePropagation();
                this.unlisten();
                this.deselectCell();

                //add all elements we want to include in our selection
                let focusableElements = 'a:not([disabled]), button:not([disabled]), input[type=text]:not([disabled]):not([hidden]):not([aria-hidden="true"]), [tabindex]:not([disabled]):not([tabindex="-1"]):not([aria-hidden="true"])';
                if (document.activeElement && document.activeElement.form) {
                    let focusable = Array.prototype.filter.call(document.activeElement.form.querySelectorAll(focusableElements),
                        function (element) {
                            //check for visibility while always include the current activeElement
                            return element.offsetWidth > 0 || element.offsetHeight > 0 || element === document.activeElement
                        });
                    let index = focusable.indexOf(document.activeElement);
                    if (index > -1) {
                        let nextElement = focusable[index + 1] || focusable[0];
                        nextElement.focus();
                    }
                }
            }
            return;
        }

        // prevent Alpha chars in numeric sheet cells
        if (celltype === "numeric") {
            // #766 do not block if just CTRL or SHIFT key
            if (key === 16 || key === 17) {
                return;
            }

            // #739 allow navigation
            let ctrlDown = evt.ctrlKey || evt.metaKey; // Mac support
            if (shiftDown || ctrlDown) {
                // navigation keys
                if (key == 9 || (key >= 35 && key <= 40)) {
                    return;
                }
            }

            // check for cut and paste
            let isClipboard = false;
            // Check for Alt+Gr (http://en.wikipedia.org/wiki/AltGr_key)
            if (ctrlDown && evt.altKey) isClipboard = false;
            // Check for ctrl+c, v and x
            else if (ctrlDown && key == 65) isClipboard = true; // a (select all)
            else if (ctrlDown && key == 67) isClipboard = true; // c
            else if (ctrlDown && key == 86) isClipboard = true; // v
            else if (ctrlDown && key == 88) isClipboard = true; // x

            // allow backspace, tab, delete, enter, arrows, numbers and keypad numbers
            // ONLY home, end, F5, F12, minus (-), period (.)
            // console.log('Key: ' + key + ' Shift: ' + e.shiftKey + ' Clipboard: ' + isClipboard);
            let isNumeric = ((key == 8) || (key == 9) || (key == 13)
                || (key == 46) || (key == 110) || (key == 116)
                || (key == 123) || (key == 188) || (key == 189)
                || (key == 190) || ((key >= 35) && (key <= 40))
                || ((key >= 48) && (key <= 57)) || ((key >= 96) && (key <= 105)));

            if ((!isNumeric && !isClipboard) || shiftDown) {
                // prevent alpha characters
                e.stopImmediatePropagation();
                e.preventDefault();
            }
        }
    }
});