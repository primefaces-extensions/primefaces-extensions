/**
 * Copyright 2011-2019 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.sheet;

import java.util.ArrayList;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;
import org.primefaces.model.SortOrder;
import org.primefaces.util.ComponentUtils;

/**
 * Spreadsheet component wrappering the Handsontable jQuery UI component.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
abstract class SheetBase extends UIInput implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SheetRenderer";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {
        /**
         * The local value of this {@link UIComponent}.
         */
        value,

        /**
         * List to keep the filtered and sorted data.
         */
        filteredValue,

        /**
         * Flag indicating whether or not this component is valid.
         */
        valid,

        /**
         * The request scope attribute under which the data object for the current row will be exposed when iterating.
         */
        var,

        /**
         * The IL8N Locale. Default is en-US.
         */
        locale,

        /**
         * single, range, multiple
         */
        selectionMode,

        /**
         * The selected row
         */
        selectedRow,

        /**
         * The last selected row
         */
        selectedLastRow,

        /**
         * The selected column
         */
        selectedColumn,

        /**
         * The last selected column
         */
        selectedLastColumn,
        /**
         * flag indication whether or not to show column headers
         */
        showColumnHeaders,

        /**
         * flag indication whether or not to show row headers
         */
        showRowHeaders,

        /**
         * The custom row header to be used in place of the standard numeric header value
         */
        rowHeader,

        /**
         * Maximum number of rows.
         */
        maxRows,

        /**
         * Minimum number of rows.
         */
        minRows,

        /**
         * Maximum number of columns.
         */
        maxCols,

        /**
         * Minimum number of columns.
         */
        minCols,

        /**
         * Fixed rows when scrolling
         */
        fixedRows,

        /**
         * You can fix the bottom rows of the table, by using the fixedRowsBottom config option. This way, when you're scrolling the table, the fixed rows will
         * stay at the bottom edge of the table's container.
         */
        fixedRowsBottom,

        /**
         * Fixed columns when scrolling
         */
        fixedCols,

        /**
         * Allow rows to be manually resizable
         */
        resizableRows,

        /**
         * Allow columns to be resizable
         */
        resizableCols,

        /**
         * Allow rows to be manually moved
         */
        movableRows,

        /**
         * Allow columns to be manually moved
         */
        movableCols,

        /**
         * The width of the component in pixels
         */
        width,

        /**
         * The height of the component in pixels
         */
        height,

        /**
         * The global error message to be displayed when the sheet is in error
         */
        errorMessage,

        /**
         * Style of the html container element
         */
        style,

        /**
         * User style class for sheet
         */
        styleClass,

        /**
         * The row key, used to unqiuely identify each row for update operations
         */
        rowKey,

        /**
         * The current sortBy value expression
         */
        sortBy,

        /**
         * The current direction of the sort
         */
        sortOrder,

        /**
         * Defines where the null values are placed in ascending sort order. Default value is "1" meaning null values are placed at the end in ascending mode
         * and at beginning in descending mode. Set to "-1" for the opposite behavior.
         */
        nullSortOrder,

        /**
         * Case sensitivity for sorting, insensitive by default.
         */
        caseSensitiveSort,

        /**
         * The original sortBy value expression saved off for reset
         */
        origSortBy,

        /**
         * The original sort direction saved off for reset
         */
        origSortOrder,

        /**
         * The Handsontable stretchH value
         */
        stretchH,

        /**
         * The style class to apply to each row in the sheet (EL expression)
         */
        rowStyleClass,
        /**
         * Flag indicating whether or not the sheet is read only
         */
        readOnly,

        /**
         * The message displayed when no records are found
         */
        emptyMessage,

        /**
         * Active Header style class
         */
        activeHeaderStyleClass,
        /**
         * Commented cell style class
         */
        commentedCellStyleClass,
        /**
         * Current column style class
         */
        currentColStyleClass,
        /**
         * Current header style class
         */
        currentHeaderStyleClass,
        /**
         * Current row style class
         */
        currentRowStyleClass,
        /**
         * Invalid cell style class
         */
        invalidCellStyleClass,
        /**
         * No Word Wrap style class
         */
        noWordWrapStyleClass,
        /**
         * Placeholder style class
         */
        placeholderCellStyleClass,
        /**
         * Read only style class
         */
        readOnlyCellStyleClass,
        /**
         * Name of javascript function to extend the options of the underlying Handsontable plugin.
         */
        extender
    }

    /**
     * Default constructor
     */
    public SheetBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getStyleClass() {
        final Object result = getStateHelper().eval(PropertyKeys.styleClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    public void setStretchH(final String value) {
        getStateHelper().put(PropertyKeys.stretchH, value);
    }

    public String getStretchH() {
        final Object result = getStateHelper().eval(PropertyKeys.stretchH, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    public void setEmptyMessage(final String value) {
        getStateHelper().put(PropertyKeys.emptyMessage, value);
    }

    public String getEmptyMessage() {
        final Object result = getStateHelper().eval(PropertyKeys.emptyMessage, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    public void setShowColumnHeaders(final Boolean value) {
        getStateHelper().put(PropertyKeys.showColumnHeaders, value);
    }

    public Boolean isShowColumnHeaders() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.showColumnHeaders, true).toString());
    }

    public void setShowRowHeaders(final Boolean value) {
        getStateHelper().put(PropertyKeys.showRowHeaders, value);
    }

    public Boolean isShowRowHeaders() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.showRowHeaders, true).toString());
    }

    public void setResizableRows(final Boolean value) {
        getStateHelper().put(PropertyKeys.resizableRows, value);
    }

    public Boolean isResizableRows() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.resizableRows, false).toString());
    }

    public void setResizableCols(final Boolean value) {
        getStateHelper().put(PropertyKeys.resizableCols, value);
    }

    public Boolean isResizableCols() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.resizableCols, false).toString());
    }

    public void setMovableRows(final Boolean value) {
        getStateHelper().put(PropertyKeys.movableRows, value);
    }

    public Boolean isMovableRows() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.movableRows, false).toString());
    }

    public void setMovableCols(final Boolean value) {
        getStateHelper().put(PropertyKeys.movableCols, value);
    }

    public Boolean isMovableCols() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.movableCols, false).toString());
    }

    public void setRowStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.rowStyleClass, styleClass);
    }

    public String getRowStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.rowStyleClass, null);
    }

    public String getActiveHeaderStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.activeHeaderStyleClass, null);
    }

    public void setReadOnly(final Boolean value) {
        getStateHelper().put(PropertyKeys.readOnly, value);
    }

    public Boolean isReadOnly() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.readOnly, Boolean.FALSE).toString());
    }

    public void setActiveHeaderStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.activeHeaderStyleClass, _value);
    }

    public String getCommentedCellStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.commentedCellStyleClass, null);
    }

    public void setCommentedCellStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.commentedCellStyleClass, _value);
    }

    public String getCurrentColStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.currentColStyleClass, null);
    }

    public void setCurrentColStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.currentColStyleClass, _value);
    }

    public String getCurrentHeaderStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.currentHeaderStyleClass, null);
    }

    public void setCurrentHeaderStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.currentHeaderStyleClass, _value);
    }

    public String getCurrentRowStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.currentRowStyleClass, null);
    }

    public void setCurrentRowStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.currentRowStyleClass, _value);
    }

    public String getInvalidCellStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.invalidCellStyleClass, null);
    }

    public void setInvalidCellStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.invalidCellStyleClass, _value);
    }

    public String getNoWordWrapStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.noWordWrapStyleClass, null);
    }

    public void setNoWordWrapStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.noWordWrapStyleClass, _value);
    }

    public String getPlaceholderCellStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderCellStyleClass, null);
    }

    public void setPlaceholderCellStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.placeholderCellStyleClass, _value);
    }

    public String getReadOnlyCellStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderCellStyleClass, null);
    }

    public void setReadOnlyCellStyleClass(final String _value) {
        getStateHelper().put(PropertyKeys.readOnlyCellStyleClass, _value);
    }

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(final String _extender) {
        getStateHelper().put(PropertyKeys.extender, _extender);
    }

    public void setCaseSensitiveSort(final Boolean value) {
        getStateHelper().put(PropertyKeys.caseSensitiveSort, value);
    }

    public Boolean isCaseSensitiveSort() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.caseSensitiveSort, Boolean.FALSE).toString());
    }

    public Integer getNullSortOrder() {
        return (Integer) getStateHelper().eval(PropertyKeys.nullSortOrder, Integer.valueOf(1));
    }

    public void setNullSortOrder(final Integer value) {
        getStateHelper().put(PropertyKeys.nullSortOrder, value);
    }

    public void setMaxRows(final Integer value) {
        getStateHelper().put(PropertyKeys.maxRows, value);
    }

    public Integer getMaxRows() {
        final Object result = getStateHelper().eval(PropertyKeys.maxRows, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    public void setMinRows(final Integer value) {
        getStateHelper().put(PropertyKeys.minRows, value);
    }

    public Integer getMinRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.minRows, Integer.valueOf(0));
    }

    public void setMaxCols(final Integer value) {
        getStateHelper().put(PropertyKeys.maxCols, value);
    }

    public Integer getMaxCols() {
        final Object result = getStateHelper().eval(PropertyKeys.maxCols, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    public void setMinCols(final Integer value) {
        getStateHelper().put(PropertyKeys.minCols, value);
    }

    public Integer getMinCols() {
        return (Integer) getStateHelper().eval(PropertyKeys.minCols, Integer.valueOf(0));
    }

    public void setFixedRows(final Integer value) {
        getStateHelper().put(PropertyKeys.fixedRows, value);
    }

    public Integer getFixedRows() {
        final Object result = getStateHelper().eval(PropertyKeys.fixedRows, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    public void setFixedRowsBottom(final Integer value) {
        getStateHelper().put(PropertyKeys.fixedRowsBottom, value);
    }

    public Integer getFixedRowsBottom() {
        final Object result = getStateHelper().eval(PropertyKeys.fixedRowsBottom, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    public void setFixedCols(final Integer value) {
        getStateHelper().put(PropertyKeys.fixedCols, value);
    }

    public Integer getFixedCols() {
        final Object result = getStateHelper().eval(PropertyKeys.fixedCols, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    public String getLocale() {
        return (String) getStateHelper().eval(PropertyKeys.locale, "en-US");
    }

    public void setLocale(final String locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public void setSelectionMode(final String value) {
        getStateHelper().put(PropertyKeys.selectionMode, value);
    }

    public String getSelectionMode() {
        return (String) getStateHelper().eval(PropertyKeys.selectionMode, "multiple");
    }

    /**
     * The currently selected column.
     *
     * @return
     */
    public Integer getSelectedColumn() {
        final Object result = getStateHelper().eval(PropertyKeys.selectedColumn);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the selected column.
     *
     * @param col
     */
    public void setSelectedColumn(final Integer col) {
        getStateHelper().put(PropertyKeys.selectedColumn, col);
    }

    /**
     * The currently selected column.
     *
     * @return
     */
    public Integer getSelectedLastColumn() {
        final Object result = getStateHelper().eval(PropertyKeys.selectedLastColumn);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the selected column.
     *
     * @param col
     */
    public void setSelectedLastColumn(final Integer col) {
        getStateHelper().put(PropertyKeys.selectedLastColumn, col);
    }

    /**
     * The currently selected row.
     *
     * @return
     */
    public Integer getSelectedRow() {
        final Object result = getStateHelper().eval(PropertyKeys.selectedRow);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * The currently selected row.
     *
     * @return
     */
    public Integer getSelectedLastRow() {
        final Object result = getStateHelper().eval(PropertyKeys.selectedLastRow);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the selected row.
     *
     * @param row
     */
    public void setSelectedRow(final Integer row) {
        getStateHelper().put(PropertyKeys.selectedRow, row);
    }

    /**
     * Updates the selected row.
     *
     * @param row
     */
    public void setSelectedLastRow(final Integer row) {
        getStateHelper().put(PropertyKeys.selectedLastRow, row);
    }

    public Integer getWidth() {
        final Object result = getStateHelper().eval(PropertyKeys.width);
        if (result == null) {
            return null;
        }
        // this will handle any type so long as its convertible to integer
        return Integer.valueOf(result.toString());
    }

    public void setWidth(final Integer value) {
        getStateHelper().put(PropertyKeys.width, value);
    }

    /**
     * The height of the sheet. Note this is applied to the inner div which is why it is recommend you use this property instead of a style class.
     *
     * @return
     */
    public Integer getHeight() {
        final Object result = getStateHelper().eval(PropertyKeys.height);
        if (result == null) {
            return null;
        }
        // this will handle any type so long as its convertable to integer
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the height
     *
     * @param row
     */
    public void setHeight(final Integer value) {
        getStateHelper().put(PropertyKeys.height, value);
    }

    /**
     * Return the value of the Sheet. This value must be a java.util.List value at this time.
     */
    @Override
    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value);
    }

    /**
     * Holds the filtered and sorted List of values.
     *
     * @return a List of sorted and filtered values
     */
    public java.util.List getFilteredValue() {
        return (java.util.List) getStateHelper().eval(PropertyKeys.filteredValue, new ArrayList());
    }

    /**
     * Sets the filtered list.
     *
     * @param _filteredValue the List to store
     */
    public void setFilteredValue(final java.util.List _filteredValue) {
        getStateHelper().put(PropertyKeys.filteredValue, _filteredValue);
    }

    /**
     * Set the value of the <code>Sheet</code>. This value must be a java.util.List at this time.
     *
     * @param value the new value
     */
    @Override
    public void setValue(final Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    /**
     * Update the style value for the component
     *
     * @param value
     */
    public void setStyle(final String value) {
        getStateHelper().put(PropertyKeys.style, value);
    }

    /**
     * The style value
     *
     * @return the style value
     */
    public String getStyle() {
        final Object result = getStateHelper().eval(PropertyKeys.style, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Gets the rowHeader value expression defined
     *
     * @return a value expression for Row Header or null if the expression is not set
     */
    protected ValueExpression getRowHeaderValueExpression() {
        return getValueExpression(PropertyKeys.rowHeader.name());
    }

    /**
     * Gets the rowKey for the current row
     *
     * @param context the faces context
     * @return a row key value or null if the expression is not set
     */
    protected Object getRowKeyValue(final FacesContext context) {
        final ValueExpression veRowKey = getValueExpression(PropertyKeys.rowKey.name());
        if (veRowKey == null) {
            throw new RuntimeException("RowKey required on sheet!");
        }
        final Object value = veRowKey.getValue(context.getELContext());
        if (value == null) {
            throw new RuntimeException("RowKey must resolve to non-null value for updates to work properly");
        }
        return value;
    }

    /**
     * Return the request-scope attribute under which the data object for the current row will be exposed when iterating. This property is <strong>not</strong>
     * enabled for value binding expressions.
     */
    public String getVar() {
        // must be a string literal (no eval)
        return (String) getStateHelper().get(PropertyKeys.var);
    }

    /**
     * Set the request-scope attribute under which the data object for the current row wil be exposed when iterating.
     *
     * @param var The new request-scope attribute name
     */
    public void setVar(final String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    /**
     * The current sortBy value expression in use.
     *
     * @return
     */
    public ValueExpression getSortByValueExpression() {
        final ValueExpression veSortBy = getValueExpression(PropertyKeys.sortBy.name());
        return veSortBy;
    }

    /**
     * Update the sort field
     *
     * @param sortBy
     */
    public void setSortByValueExpression(final ValueExpression sortBy) {
        // when updating, make sure we store off the original so it may be
        // restored
        // ValueExpression orig = (ValueExpression)
        // getStateHelper().get(PropertyKeys.origSortBy);
        // if (orig == null) {
        // getStateHelper().put(PropertyKeys.origSortBy,
        // getSortByValueExpression());
        // }
        setValueExpression(PropertyKeys.sortBy.name(), sortBy);
    }

    /**
     * The sort direction
     *
     * @return
     */
    public String getSortOrder() {
        // if we have a toggled sort in our state, use it
        final String result = (String) getStateHelper().eval(PropertyKeys.sortOrder, SortOrder.ASCENDING.toString());
        return result;
    }

    /**
     * Update the sort direction
     *
     * @param sortOrder
     */
    public void setSortOrder(final java.lang.String sortOrder) {
        // when updating, make sure we store off the original so it may be
        // restored
        final String orig = (String) getStateHelper().get(PropertyKeys.origSortOrder);
        if (orig == null) {
            // do not call getSortOrder as it defaults to ascending, we want
            // null
            // if this is the first call and there is no previous value.
            getStateHelper().put(PropertyKeys.origSortOrder, getStateHelper().eval(PropertyKeys.sortOrder));
        }
        getStateHelper().put(PropertyKeys.sortOrder, sortOrder);
    }

    /**
     * The error message to display when the sheet is in error.
     *
     * @return
     */
    public String getErrorMessage() {
        final Object result = getStateHelper().eval(PropertyKeys.errorMessage);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Updates the errorMessage value.
     *
     * @param value
     */
    public void setErrorMessage(final String value) {
        getStateHelper().put(PropertyKeys.errorMessage, value);
    }

}
