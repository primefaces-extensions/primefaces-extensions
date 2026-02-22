/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.sheet;

import java.util.List;

import jakarta.faces.component.UIInput;
import jakarta.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.SheetEvent;

/**
 * CDK base for the Sheet (spreadsheet) component.
 *
 * @since 6.2
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "change", event = SheetEvent.class, description = "Fires when cell data changes.", defaultEvent = true),
            @FacesBehaviorEvent(name = "cellSelect", event = SheetEvent.class, description = "Fires when a cell is selected."),
            @FacesBehaviorEvent(name = "sort", event = SheetEvent.class, description = "Fires when column sort is applied."),
            @FacesBehaviorEvent(name = "filter", event = SheetEvent.class, description = "Fires when column filter is applied."),
            @FacesBehaviorEvent(name = "columnSelect", event = SheetEvent.class, description = "Fires when a column is selected."),
            @FacesBehaviorEvent(name = "rowSelect", event = SheetEvent.class, description = "Fires when a row is selected.")
})
public abstract class SheetBase extends UIInput implements ClientBehaviorHolder, Widget, StyleAware {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SheetRenderer";

    public SheetBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "The value of the sheet (java.util.List).")
    public abstract Object getValue();

    @Property(description = "Filtered and sorted list of values.")
    public abstract List<Object> getFilteredValue();

    @Property(description = "Whether the component is valid.", defaultValue = "true")
    public abstract boolean isValid();

    @Property(description = "Request-scope attribute name for the current row data.")
    public abstract String getVar();

    @Property(description = "Locale.", defaultValue = "en-US")
    public abstract String getLocale();

    @Property(description = "Selection mode: single, range, multiple.", defaultValue = "multiple")
    public abstract String getSelectionMode();

    @Property(description = "Selected row index.")
    public abstract Integer getSelectedRow();

    @Property(description = "Last selected row index.")
    public abstract Integer getSelectedLastRow();

    @Property(description = "Selected column index.")
    public abstract Integer getSelectedColumn();

    @Property(description = "Last selected column index.")
    public abstract Integer getSelectedLastColumn();

    @Property(description = "Show column headers.", defaultValue = "true")
    public abstract boolean isShowColumnHeaders();

    @Property(description = "Show row headers.", defaultValue = "true")
    public abstract boolean isShowRowHeaders();

    @Property(description = "Maximum number of rows.")
    public abstract Integer getMaxRows();

    @Property(description = "Minimum number of rows.", defaultValue = "0")
    public abstract Integer getMinRows();

    @Property(description = "Maximum number of columns.")
    public abstract Integer getMaxCols();

    @Property(description = "Minimum number of columns.", defaultValue = "0")
    public abstract Integer getMinCols();

    @Property(description = "Number of fixed rows when scrolling.")
    public abstract Integer getFixedRows();

    @Property(description = "Number of fixed rows at bottom.")
    public abstract Integer getFixedRowsBottom();

    @Property(description = "Number of fixed columns when scrolling.")
    public abstract Integer getFixedCols();

    @Property(description = "Allow rows to be resizable.", defaultValue = "false")
    public abstract boolean isResizableRows();

    @Property(description = "Allow columns to be resizable.", defaultValue = "false")
    public abstract boolean isResizableCols();

    @Property(description = "Allow rows to be moved.", defaultValue = "false")
    public abstract boolean isMovableRows();

    @Property(description = "Allow columns to be moved.", defaultValue = "false")
    public abstract boolean isMovableCols();

    @Property(description = "Width in pixels.")
    public abstract Integer getWidth();

    @Property(description = "Height in pixels.")
    public abstract Integer getHeight();

    @Property(description = "Global error message when sheet is in error.")
    public abstract String getErrorMessage();

    @Property(description = "Column/value expression to sort by (Object for correct type).")
    public abstract Object getSortBy();

    @Property(description = "Sort direction.", defaultValue = "ASCENDING")
    public abstract String getSortOrder();

    @Property(description = "Null sort order placement.", defaultValue = "1")
    public abstract Integer getNullSortOrder();

    @Property(description = "Case sensitive sort.", defaultValue = "false")
    public abstract boolean isCaseSensitiveSort();

    @Property(description = "ID of column currently used for sorting (internal).")
    public abstract String getCurrentSortBy();

    @Property(description = "Original sort order for reset (internal).")
    public abstract String getOrigSortOrder();

    @Property(description = "Handsontable stretchH.")
    public abstract String getStretchH();

    @Property(description = "Style class for each row (EL).")
    public abstract String getRowStyleClass();

    @Property(description = "Sheet read only.", defaultValue = "false")
    public abstract boolean isReadOnly();

    @Property(description = "Message when no records.")
    public abstract String getEmptyMessage();

    @Property(description = "Active header style class.")
    public abstract String getActiveHeaderStyleClass();

    @Property(description = "Commented cell style class.")
    public abstract String getCommentedCellStyleClass();

    @Property(description = "Current column style class.")
    public abstract String getCurrentColStyleClass();

    @Property(description = "Current header style class.")
    public abstract String getCurrentHeaderStyleClass();

    @Property(description = "Current row style class.")
    public abstract String getCurrentRowStyleClass();

    @Property(description = "Invalid cell style class.")
    public abstract String getInvalidCellStyleClass();

    @Property(description = "No word wrap style class.")
    public abstract String getNoWordWrapStyleClass();

    @Property(description = "Placeholder cell style class.")
    public abstract String getPlaceholderCellStyleClass();

    @Property(description = "Read only cell style class.")
    public abstract String getReadOnlyCellStyleClass();

    @Property(description = "Allow tab off sheet.", defaultValue = "false")
    public abstract boolean isAllowTabOffSheet();

    @Property(description = "Tab index.", defaultValue = "0")
    public abstract String getTabindex();

    @Property(description = "JavaScript function to extend Handsontable options.")
    public abstract String getExtender();

}
