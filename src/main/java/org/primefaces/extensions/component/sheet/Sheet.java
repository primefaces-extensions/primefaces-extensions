/**
 * Copyright 2011-2018 PrimeFaces Extensions
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.primefaces.component.api.Widget;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;
import org.primefaces.extensions.util.JavascriptVarBuilder;
import org.primefaces.model.BeanPropertyComparator;
import org.primefaces.model.SortOrder;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * Spreadsheet component wrappering the Handsontable jQuery UI component.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces", name = "components.js"),
            @ResourceDependency(library = "primefaces-extensions", target = "head", name = "sheet/sheet.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "sheet/sheet.js") })
public class Sheet extends UIInput implements ClientBehaviorHolder, EditableValueHolder, Widget {

    public static final String EVENT_CELL_SELECT = "cellSelect";
    public static final String EVENT_CHANGE = "change";
    public static final String EVENT_SORT = "sort";
    public static final String EVENT_FILTER = "filter";
    public static final String EVENT_COLUMN_SELECT = "columnSelect";
    public static final String EVENT_ROW_SELECT = "rowSelect";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Sheet";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SheetRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(EVENT_CHANGE, EVENT_CELL_SELECT, EVENT_SORT, EVENT_FILTER, EVENT_COLUMN_SELECT,
                            EVENT_ROW_SELECT));

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {
        /**
         * The local value of this {@link UIComponent}.
         */
        value,

        /**
         * Flag indicating whether or not this component is valid.
         */
        valid,

        /**
         * The request scope attribute under which the data object for the current row will be exposed when iterating.
         */
        var,

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
         * Fixed rows when scrolling
         */
        fixedRows,

        /**
         * Fixed columns when scrolling
         */
        fixedCols,

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
         * The style class to apply to the currently selected row
         */
        currentRowClass,

        /**
         * The style class to apply to the currently selected column
         */
        currentColClass,

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
         * The message displayed when no records are found
         */
        emptyMessage
    }

    /**
     * The list of UI Columns
     */
    private List<SheetColumn> columns;

    /**
     * List of invalid updates
     */
    private List<SheetInvalidUpdate> invalidUpdates;

    /**
     * The sorted list of data
     */
    private List<Object> sortedList;

    /**
     * Map of submitted values by row index and column index
     */
    private Map<RowColIndex, String> submittedValues = new HashMap<RowColIndex, String>();

    /**
     * Map of local values by row index and column index
     */
    private Map<RowColIndex, Object> localValues = new HashMap<RowColIndex, Object>();

    /**
     * The selection data
     */
    private String selection;

    /**
     * The id of the focused filter input if any
     */
    private String focusId;

    /**
     * Transient list of sheet updates that can be accessed after a successful model update.
     */
    private final List<SheetUpdate> updates = new ArrayList<SheetUpdate>();

    /**
     * Maps a visible, rendered column index to the actual column based on whether or not the column is rendered. Updated on encode, and used on decode. Saved
     * in the component state.
     */
    private Map<Integer, Integer> columnMapping;

    /**
     * Map by row keys for values found in list
     */
    private Map<String, Object> rowMap;

    /**
     * Default constructor
     */
    public Sheet() {
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
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_CHANGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(fc);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final SheetEvent sheetEvent = new SheetEvent(this, behaviorEvent.getBehavior());
            sheetEvent.setPhaseId(event.getPhaseId());
            super.queueEvent(sheetEvent);
            System.out.println("Sheet Event!");
            return;
        }

        super.queueEvent(event);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    /**
     * Update's the user's custom style class to be added to the div container for the sheet.
     *
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    /**
     * The user's custom style class to be added to the div container for the sheet.
     */
    public String getStyleClass() {
        final Object result = getStateHelper().eval(PropertyKeys.styleClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the stretcH value for the component
     *
     * @param value
     */
    public void setStretchH(String value) {
        getStateHelper().put(PropertyKeys.stretchH, value);
    }

    /**
     * The handsontable stretchH value.
     *
     * @return the stretchH value
     */
    public String getStretchH() {
        final Object result = getStateHelper().eval(PropertyKeys.stretchH, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the emptyMessage value for the component
     *
     * @param value
     */
    public void setEmptyMessage(String value) {
        getStateHelper().put(PropertyKeys.emptyMessage, value);
    }

    /**
     * The emptyMessage value.
     *
     * @return the emptyMessage value
     */
    public String getEmptyMessage() {
        final Object result = getStateHelper().eval(PropertyKeys.emptyMessage, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the current row style class
     *
     * @param styleClass
     */
    public void setCurrentColClass(String styleClass) {
        getStateHelper().put(PropertyKeys.currentColClass, styleClass);
    }

    /**
     * The col style class to use for the selected col
     *
     * @param styleClass
     */
    public String getCurrentColClass() {
        final Object result = getStateHelper().eval(PropertyKeys.currentColClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the current row style class
     *
     * @param styleClass
     */
    public void setCurrentRowClass(String styleClass) {
        getStateHelper().put(PropertyKeys.currentRowClass, styleClass);
    }

    /**
     * The row style class to use for the selected row
     *
     * @param styleClass
     */
    public String getCurrentRowClass() {
        final Object result = getStateHelper().eval(PropertyKeys.currentRowClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the current row style class to apply to the row
     *
     * @param styleClass
     */
    public void setRowStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.rowStyleClass, styleClass);
    }

    /**
     * The row style class to apply to each row
     *
     * @param styleClass
     */
    public String getRowStyleClass() {
        final Object result = getStateHelper().eval(PropertyKeys.rowStyleClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Update the ShowColumnheaders
     *
     * @param value
     */
    public void setShowColumnHeaders(Boolean value) {
        getStateHelper().put(PropertyKeys.showColumnHeaders, value);
    }

    /**
     * Flag indicating whether or not column headers are visible
     *
     * @return
     */
    public Boolean isShowColumnHeaders() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.showColumnHeaders, true).toString());
    }

    /**
     * Update the ShowRowHeaders value.
     *
     * @param value
     */
    public void setShowRowHeaders(Boolean value) {
        getStateHelper().put(PropertyKeys.showRowHeaders, value);
    }

    /**
     * The ShowRowHeaders flag
     *
     * @return
     */
    public Boolean isShowRowHeaders() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.showRowHeaders, true).toString());
    }

    /**
     * The list of child columns.
     *
     * @return
     */
    public List<SheetColumn> getColumns() {
        if (columns == null) {
            columns = new ArrayList<SheetColumn>();
            getColumns(this);
        }
        return columns;
    }

    /**
     * Grabs the UIColumn children for the parent specified.
     *
     * @param parent
     */
    private void getColumns(UIComponent parent) {
        for (final UIComponent child : parent.getChildren()) {
            if (child instanceof SheetColumn) {
                columns.add((SheetColumn) child);
            }
        }
    }

    /**
     * Updates the list of child columns.
     *
     * @param columns
     */
    public void setColumns(List<SheetColumn> columns) {
        this.columns = columns;
    }

    /**
     * Updates the fixed row count.
     *
     * @param value
     */
    public void setFixedRows(Integer value) {
        getStateHelper().put(PropertyKeys.fixedRows, value);
    }

    /**
     * The fixed row count
     *
     * @return
     */
    public Integer getFixedRows() {
        final Object result = getStateHelper().eval(PropertyKeys.fixedRows, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the fixed columns count.
     *
     * @param value
     */
    public void setFixedCols(Integer value) {
        getStateHelper().put(PropertyKeys.fixedCols, value);
    }

    /**
     * The fixed column count.
     *
     * @return
     */
    public Integer getFixedCols() {
        final Object result = getStateHelper().eval(PropertyKeys.fixedCols, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * The list of invalid updates
     *
     * @return List<SheetInvalidUpdate>
     */
    public List<SheetInvalidUpdate> getInvalidUpdates() {
        if (invalidUpdates == null) {
            invalidUpdates = new ArrayList<SheetInvalidUpdate>();
        }
        return invalidUpdates;
    }

    /**
     * Resets the submitted values
     */
    public void resetSubmitted() {
        submittedValues.clear();
        updates.clear();
    }

    /**
     * Resets the sorting to the originally specified values (if any)
     */
    public void resetSort() {
        final ValueExpression origSortBy = (ValueExpression) getStateHelper().get(PropertyKeys.origSortBy);
        // Set sort by even if null to restore to initial sort order.
        setSortByValueExpression(origSortBy);

        final String origSortOrder = (String) getStateHelper().get(PropertyKeys.origSortOrder);
        if (origSortOrder != null) {
            setSortOrder(origSortOrder);
        }
    }

    /**
     * Resets invalid updates
     */
    public void resetInvalidUpdates() {
        getInvalidUpdates().clear();
    }

    /**
     * Resets all filters, sorting and submitted values.
     */
    public void reset() {
        resetSubmitted();
        resetSort();
        resetInvalidUpdates();
        localValues.clear();
        for (final SheetColumn c : getColumns()) {
            c.setFilterValue(null);
        }
    }

    /**
     * Updates a submitted value.
     *
     * @param row
     * @param col
     * @param value
     */
    public void setSubmittedValue(FacesContext context, String rowKey, int col, String value) {
        submittedValues.put(new RowColIndex(rowKey, col), value);
    }

    /**
     * Retrieves the submitted value for the row and col.
     *
     * @param row
     * @param col
     * @return
     */
    public String getSubmittedValue(String rowKey, int col) {
        return submittedValues.get(new RowColIndex(rowKey, col));
    }

    /**
     * Updates a local value.
     *
     * @param rowKey
     * @param col
     * @param value
     */
    public void setLocalValue(String rowKey, int col, Object value) {
        localValues.put(new RowColIndex(rowKey, col), value);
    }

    /**
     * Retrieves the submitted value for the rowKey and col.
     *
     * @param row
     * @param col
     * @return
     */
    public Object getLocalValue(String rowKey, int col) {
        return localValues.get(new RowColIndex(rowKey, col));
    }

    /**
     * Updates the row var for iterations over the list. The var value will be updated to the value for the specified rowKey.
     *
     * @param context the FacesContext against which to the row var is set. Passed for performance
     * @param rowKey the rowKey string
     */
    public void setRowVar(FacesContext context, String rowKey) {

        if (context == null) {
            return;
        }

        if (rowKey == null) {
            context.getExternalContext().getRequestMap().remove(getVar());
        }
        else {
            final Object value = getRowMap().get(rowKey);
            context.getExternalContext().getRequestMap().put(getVar(), value);
        }
    }

    protected Map<String, Object> getRowMap() {
        if (rowMap == null || rowMap.isEmpty()) {
            reMapRows();
        }
        return rowMap;
    }

    /**
     * Gets the object value of the row and col specified. If a local value exists, that is returned, otherwise the actual value is return.
     *
     * @param context
     * @param rowKey
     * @param col
     * @return
     */
    public Object getValueForCell(FacesContext context, String rowKey, int col) {
        // if we have a local value, use it
        // note: can't check for null, as null may be the submitted value
        final RowColIndex index = new RowColIndex(rowKey, col);
        if (localValues.containsKey(index)) {
            return localValues.get(index);
        }

        setRowVar(context, rowKey);
        final SheetColumn column = getColumns().get(col);
        return column.getValueExpression("value").getValue(context.getELContext());
    }

    /**
     * Gets the render string for the value the given cell. Applys the available converters to convert the value.
     *
     * @param context
     * @param rowKey
     * @param col
     * @return
     */
    public String getRenderValueForCell(FacesContext context, String rowKey, int col) {

        // if we have a submitted value still, use it
        // note: can't check for null, as null may be the submitted value
        final RowColIndex index = new RowColIndex(rowKey, col);
        if (submittedValues.containsKey(index)) {
            return submittedValues.get(index);
        }

        final Object value = getValueForCell(context, rowKey, col);
        if (value == null) {
            return null;
        }

        final SheetColumn column = getColumns().get(col);
        final Converter converter = ComponentUtils.getConverter(context, column);
        if (converter == null) {
            return value.toString();
        }
        else {
            return converter.getAsString(context, this, value);
        }
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
    public void setSelectedColumn(Integer col) {
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
    public void setSelectedLastColumn(Integer col) {
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
    public void setSelectedRow(Integer row) {
        getStateHelper().put(PropertyKeys.selectedRow, row);
    }

    /**
     * Updates the selected row.
     *
     * @param row
     */
    public void setSelectedLastRow(Integer row) {
        getStateHelper().put(PropertyKeys.selectedLastRow, row);
    }

    /**
     * The width of the sheet in pixels
     *
     * @return
     */
    public Integer getWidth() {
        final Object result = getStateHelper().eval(PropertyKeys.width);
        if (result == null) {
            return null;
        }
        // this will handle any type so long as its convertable to integer
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the width
     *
     * @param row
     */
    public void setWidth(Integer value) {
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
    public void setHeight(Integer value) {
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
     * Update the style value for the component
     *
     * @param value
     */
    public void setStyle(String value) {
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
     * Gets the row header text value as a string for use in javascript
     *
     * @param context
     * @return
     */
    protected String getRowHeaderValueAsString(FacesContext context) {
        ValueExpression veRowHeader = getRowHeaderValueExpression();
        final Object value = veRowHeader.getValue(context.getELContext());
        if (value == null) {
            return StringUtils.EMPTY;
        }
        else {
            return value.toString();
        }
    }

    /**
     * The sorted list of values.
     *
     * @return
     */
    public List<Object> getSortedValues() {
        if (sortedList == null) {
            sortAndFilter();
        }
        return sortedList;
    }

    /**
     * Gets the rendered col index of the column corresponding to the current sortBy. This is used to keep track of the current sort column in the page.
     *
     * @return
     */
    public int getSortColRenderIndex() {
        final ValueExpression veSortBy = getValueExpression(PropertyKeys.sortBy.name());
        if (veSortBy == null) {
            return -1;
        }

        final String sortByExp = veSortBy.getExpressionString();
        int colIdx = 0;
        for (final SheetColumn column : getColumns()) {
            if (!column.isRendered()) {
                continue;
            }

            final ValueExpression veCol = column.getValueExpression(PropertyKeys.sortBy.name());
            if (veCol != null) {
                if (veCol.getExpressionString().equals(sortByExp)) {
                    return colIdx;
                }
            }
            colIdx++;
        }
        return -1;
    }

    /**
     * Evaluates the specified item value against the column filters and if they match, returns true, otherwise false.
     *
     * @param obj
     * @return
     */
    protected boolean matchesFilter(Object obj) {
        for (final SheetColumn col : getColumns()) {
            final String filterValue = col.getFilterValue();
            if (StringUtils.isEmpty(filterValue)) {
                continue;
            }

            final Object filterBy = col.getFilterBy();
            // if we have a filter, but no value in the row, no match
            if (filterBy == null) {
                return false;
            }

            // case-insensitive
            final String compareA = filterBy.toString().toLowerCase();
            final String compareB = filterValue.toLowerCase();

            // TODO need to support match modes
            if (!compareA.contains(compareB)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sorts and filters the data
     */
    public void sortAndFilter() {
        sortedList = new ArrayList<Object>();
        rowMap = new HashMap<String, Object>();

        final Collection<?> values = (Collection<?>) getValue();
        if (values == null || values.isEmpty()) {
            return;
        }

        reMapRows();

        boolean filters = false;
        for (final SheetColumn col : getColumns()) {
            if (StringUtils.isNotEmpty(col.getFilterValue())) {
                filters = true;
                break;
            }
        }

        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (filters) {
            // iterate and add those matching the filters
            final String var = getVar();
            for (final Object obj : values) {
                requestMap.put(var, obj);
                try {
                    if (matchesFilter(obj)) {
                        sortedList.add(obj);
                    }
                }
                finally {
                    requestMap.remove(var);
                }
            }
        }
        else {
            sortedList.addAll(values);
        }

        final ValueExpression veSortBy = getValueExpression(PropertyKeys.sortBy.name());
        if (veSortBy != null) {
            Collections.sort(sortedList, new BeanPropertyComparator(veSortBy, getVar(), convertSortOrder(), null, false,
                        Locale.ENGLISH, 0));
        }

    }

    /**
     * Remaps the row keys to the sorted and filtered list.
     */
    protected void reMapRows() {
        rowMap = new HashMap<String, Object>();
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final Collection<?> values = (Collection<?>) getValue();
        final String var = getVar();
        for (final Object obj : values) {
            requestMap.put(var, obj);
            try {
                rowMap.put(getRowKeyValueAsString(context), obj);
            }
            finally {
                requestMap.remove(var);
            }
        }
    }

    /**
     * Gets the rowKey for the current row
     *
     * @param context the faces context
     * @return a row key value or null if the expression is not set
     */
    protected Object getRowKeyValue(FacesContext context) {
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
     * Gets the row key value as a String suitable for use in javascript rendering.
     *
     * @param context
     * @return
     */
    protected String getRowKeyValueAsString(Object key) {
        // TODO for now just use toString and remove spaces/etc, but in future
        // we'll want to revisit this to support complex key objects
        final String result = key.toString();
        return "r_" + StringUtils.deleteWhitespace(result);
    }

    /**
     * Gets the row key value as a string for the current row var.
     *
     * @param context
     * @return
     */
    protected String getRowKeyValueAsString(FacesContext context) {
        return getRowKeyValueAsString(getRowKeyValue(context));
    }

    /**
     * Convert to PF SortOrder enum since we are leveraging PF sorting code.
     *
     * @return
     */
    protected SortOrder convertSortOrder() {
        final String sortOrder = getSortOrder();
        if (sortOrder == null) {
            return SortOrder.UNSORTED;
        }
        else {
            final SortOrder result = SortOrder.valueOf(sortOrder.toUpperCase(Locale.ENGLISH));
            return result;
        }
    }

    /**
     * Set the value of the <code>Sheet</code>. This value must be a java.util.List at this time.
     *
     * @param value the new value
     */
    @Override
    public void setValue(Object value) {
        getStateHelper().put(PropertyKeys.value, value);
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
    public void setVar(String var) {
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
    public void setSortByValueExpression(ValueExpression sortBy) {
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
    public void setSortOrder(java.lang.String sortOrder) {
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
    public void setErrorMessage(String value) {
        getStateHelper().put(PropertyKeys.errorMessage, value);
    }

    /**
     * Converts each submitted value into a local value and stores it back in the hash. If all values convert without error, then the component is valid, and we
     * can proceed to the processUpdates.
     */
    @Override
    public void validate(FacesContext context) {
        // iterate over submitted values and attempt to convert to the proper
        // data type. For successful values, remove from submitted and add to
        // local values map. for failures, add a conversion message and leave in
        // the submitted state
        final Iterator<Entry<RowColIndex, String>> entries = submittedValues.entrySet().iterator();
        final boolean hadBadUpdates = !getInvalidUpdates().isEmpty();
        getInvalidUpdates().clear();
        while (entries.hasNext()) {
            final Entry<RowColIndex, String> entry = entries.next();
            final SheetColumn column = getColumns().get(entry.getKey().colIndex);
            final String newValue = entry.getValue();
            final String rowKey = entry.getKey().getRowKey();
            final int col = entry.getKey().getColIndex();
            setRowVar(context, rowKey);

            // attempt to convert new value from string to correct object type
            // based on column converter. Use PF util as helper
            final Converter converter = ComponentUtils.getConverter(context, column);

            // assume string value if converter not found
            Object newValueObj = newValue;
            if (converter != null) {
                try {
                    newValueObj = converter.getAsObject(context, this, newValue);
                }
                catch (final ConverterException e) {
                    // add offending cell to list of bad updates
                    // and to a StringBuilder for error messages (so we have one
                    // message for the component)
                    setValid(false);
                    FacesMessage message = e.getFacesMessage();
                    if (message == null) {
                        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
                    }
                    context.addMessage(this.getClientId(context), message);

                    final String messageText = message.getDetail();
                    getInvalidUpdates()
                                .add(new SheetInvalidUpdate(getRowKeyValue(context), col, column, newValue, messageText));
                    continue;
                }
            }
            // value is fine, no further validations (again, not to be confused
            // with validators. until we have a "required" or something like
            // that, nothing else to do).
            setLocalValue(rowKey, col, newValueObj);

            // process validators on column
            column.setValue(newValueObj);
            try {
                column.validate(context);
            }
            finally {
                column.resetValue();
            }

            entries.remove();
        }
        setRowVar(context, null);

        final boolean newBadUpdates = !getInvalidUpdates().isEmpty();
        final String errorMessage = getErrorMessage();

        if (hadBadUpdates || newBadUpdates) {
            // update the bad data var if partial request
            if (context.getPartialViewContext().isPartialRequest()) {
                renderBadUpdateScript(context);
            }
        }

        if (newBadUpdates && errorMessage != null) {
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
            context.addMessage(null, message);
        }
    }

    /**
     * Override to update model with local values. Note that this is where things can be fragile in that we can successfully update some values and fail on
     * others. There is no clean way to roll back the updates, but we also need to fail processing. TODO consider keeping old values as we update (need for
     * event anyhow) and if there is a failure attempt to roll back by updating successful model updates with the old value. This may not all be necessary.
     */
    @Override
    public void updateModel(FacesContext context) {
        final Iterator<Entry<RowColIndex, Object>> entries = localValues.entrySet().iterator();
        // Keep track of the dirtied rows for ajax callbacks so we can send
        // updates on what was touched
        final HashSet<String> dirtyRows = new HashSet<String>();
        while (entries.hasNext()) {
            final Entry<RowColIndex, Object> entry = entries.next();

            final Object newValue = entry.getValue();
            final String rowKey = entry.getKey().getRowKey();
            final int col = entry.getKey().getColIndex();
            final SheetColumn column = getColumns().get(col);
            setRowVar(context, rowKey);
            final Object rowVal = rowMap.get(rowKey);

            final ValueExpression ve = column.getValueExpression(PropertyKeys.value.name());
            final ELContext elContext = context.getELContext();
            final Object oldValue = ve.getValue(elContext);
            ve.setValue(elContext, newValue);
            entries.remove();
            appendUpdateEvent(getRowKeyValue(context), col, rowVal, oldValue, newValue);
            dirtyRows.add(rowKey);
        }
        setLocalValueSet(false);
        setRowVar(context, null);

        if (context.getPartialViewContext().isPartialRequest()) {
            renderRowUpdateScript(context, dirtyRows);
        }
    }

    /**
     * Saves the state of the submitted and local values and the bad updates.
     */
    @Override
    public Object saveState(FacesContext context) {
        final Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = submittedValues;
        values[2] = localValues;
        values[3] = invalidUpdates;
        values[4] = columnMapping;
        values[5] = sortedList;
        values[6] = rowMap;

        return values;
    }

    /**
     * Restores the state for the submitted, local and bad values.
     */
    @Override
    public void restoreState(FacesContext context, Object state) {
        if (state == null) {
            return;
        }

        final Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        final Object restoredSubmittedValues = values[1];
        final Object restoredLocalValues = values[2];
        final Object restoredInvalidUpdates = values[3];
        final Object restoredColMappings = values[4];
        final Object restoredSortedList = values[5];
        final Object restoredRowMap = values[6];

        if (restoredSubmittedValues == null) {
            submittedValues.clear();
        }
        else {
            submittedValues = (Map<RowColIndex, String>) restoredSubmittedValues;
        }

        if (restoredLocalValues == null) {
            localValues.clear();
        }
        else {
            localValues = (Map<RowColIndex, Object>) restoredLocalValues;
        }

        if (restoredInvalidUpdates == null) {
            getInvalidUpdates().clear();
        }
        else {
            invalidUpdates = (List<SheetInvalidUpdate>) restoredInvalidUpdates;
        }

        if (restoredColMappings == null) {
            columnMapping = null;
        }
        else {
            columnMapping = (Map<Integer, Integer>) restoredColMappings;
        }

        if (restoredSortedList == null) {
            sortedList = null;
        }
        else {
            sortedList = (List<Object>) restoredSortedList;
        }

        if (restoredRowMap == null) {
            rowMap = null;
        }
        else {
            rowMap = (Map<String, Object>) restoredRowMap;
        }
    }

    /**
     * The selection value.
     *
     * @return the selection
     */
    public String getSelection() {
        return selection;
    }

    /**
     * Updates the selection value.
     *
     * @param selection the selection to set
     */
    public void setSelection(String selection) {
        this.selection = selection;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.EditableValueHolder#getSubmittedValue()
     */
    @Override
    public Object getSubmittedValue() {
        if (submittedValues.isEmpty()) {
            return null;
        }
        else {
            return submittedValues;
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.EditableValueHolder#setSubmittedValue(java.lang .Object)
     */
    @Override
    public void setSubmittedValue(Object submittedValue) {
        if (submittedValue == null) {
            submittedValues.clear();
        }
        else {
            submittedValues = (Map<RowColIndex, String>) submittedValue;
        }

    }

    /**
     * A list of updates from the last submission or ajax event.
     *
     * @return the editEvent
     */
    public List<SheetUpdate> getUpdates() {
        return updates;
    }

    /**
     * Appends an update event
     *
     * @param rowIndex
     * @param colIndex
     * @param rowData
     * @param oldValue
     * @param newValue
     */
    protected void appendUpdateEvent(Object rowKey, int colIndex, Object rowData, Object oldValue, Object newValue) {
        updates.add(new SheetUpdate(rowKey, colIndex, rowData, oldValue, newValue));
    }

    /**
     * Returns true if any of the columns contain conditional styling.
     *
     * @return
     */
    public boolean isHasStyledCells() {
        for (final SheetColumn column : getColumns()) {
            if (column.getStyleClass() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Maps the rendered column index to the real column index.
     *
     * @param renderIdx the rendered index
     * @return the mapped index
     */
    public int getMappedColumn(int renderIdx) {
        if (columnMapping == null || renderIdx == -1) {
            return renderIdx;
        }
        else {
            final Integer result = columnMapping.get(renderIdx);
            if (result == null) {
                throw new IllegalArgumentException("Invalid index " + renderIdx);
            }
            return result;
        }
    }

    /**
     * Provides the render column index based on the real index
     *
     * @param realIdx
     * @return
     */
    public int getRenderIndexFromRealIdx(int realIdx) {
        if (columnMapping == null || realIdx == -1) {
            return realIdx;
        }

        for (final Entry<Integer, Integer> entry : columnMapping.entrySet()) {
            if (entry.getValue().equals(realIdx)) {
                return entry.getKey();
            }
        }

        return realIdx;
    }

    /**
     * Updates the column mappings based on the rendered attribute
     */
    public void updateColumnMappings() {
        columnMapping = new HashMap<Integer, Integer>();
        int realIdx = 0;
        int renderIdx = 0;
        for (final SheetColumn column : getColumns()) {
            if (column.isRendered()) {
                columnMapping.put(renderIdx, realIdx);
                renderIdx++;
            }
            realIdx++;
        }
    }

    /**
     * The number of rows in the value list.
     *
     * @return
     */
    public int getRowCount() {
        final List<Object> values = getSortedValues();
        if (values == null) {
            return 0;
        }
        return values.size();
    }

    /**
     * The focusId value.
     *
     * @return the focusId
     */
    public String getFocusId() {
        return focusId;
    }

    /**
     * Updates the focusId value.
     *
     * @param focusId the focusId to set
     */
    public void setFocusId(String focusId) {
        this.focusId = focusId;
    }

    /**
     * Invoke this method to commit the most recent set of ajax updates and restart the tracking of changes. Use this when you have processes the updates to the
     * model and are confident that any changes made to this point can be cleared (likely because you have persisted those changes).
     */
    public void commitUpdates() {
        resetSubmitted();
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context.getPartialViewContext().isPartialRequest()) {
            final StringBuilder eval = new StringBuilder();
            final String jQueryId = this.getClientId().replace(":", "\\\\:");
            final String jsDeltaVar = this.getClientId().replace(":", "_") + "_delta";

            eval.append("$('#");
            eval.append(jQueryId);
            eval.append("_input').val('');");
            eval.append(jsDeltaVar);
            eval.append("={};");
            RequestContext.getCurrentInstance().getScriptsToExecute().add(eval.toString());
        }
    }

    /**
     * Generates the bad data var value for this sheet.
     *
     * @return
     */
    public String getInvalidDataValue() {
        final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, true);
        for (final SheetInvalidUpdate sheetInvalidUpdate : getInvalidUpdates()) {
            final Object rowKey = sheetInvalidUpdate.getInvalidRowKey();
            final int col = getRenderIndexFromRealIdx(sheetInvalidUpdate.getInvalidColIndex());
            final String rowKeyProperty = this.getRowKeyValueAsString(rowKey);
            vb.appendProperty(rowKeyProperty + "_c" + col, sheetInvalidUpdate.getInvalidMessage().replace("'", "&apos;"), true);
        }
        return vb.closeVar().toString();
    }

    /**
     * Adds eval scripts to the ajax response to update the rows dirtied by the most recent successful update request.
     *
     * @param context the FacesContext
     * @param dirtyRows the set of dirty rows
     */
    protected void renderRowUpdateScript(FacesContext context, Set<String> dirtyRows) {
        final String jsVar = resolveWidgetVar();
        final StringBuilder eval = new StringBuilder();

        for (final String rowKey : dirtyRows) {
            setRowVar(context, rowKey);
            // data is array of array of data
            final JavascriptVarBuilder vbRow = new JavascriptVarBuilder(null, false);
            for (int col = 0; col < getColumns().size(); col++) {
                final SheetColumn column = getColumns().get(col);
                if (!column.isRendered()) {
                    continue;
                }

                // render data value
                final String value = getRenderValueForCell(context, rowKey, col);
                vbRow.appendArrayValue(value, true);
            }
            eval.append("PF('" + jsVar + "')");
            eval.append(".updateData('");
            eval.append(rowKey);
            eval.append("',");
            eval.append(vbRow.closeVar().toString());
            eval.append(");");
        }
        eval.append("PF('" + jsVar + "')");
        eval.append(".ht.render();");
        RequestContext.getCurrentInstance().getScriptsToExecute().add(eval.toString());
    }

    /**
     * Adds eval scripts to update the bad data array in the sheet to render validation failures produced by the most recent ajax update attempt.
     *
     * @param context the FacesContext
     */
    protected void renderBadUpdateScript(FacesContext context) {
        final String widgetVar = resolveWidgetVar();
        final String invalidValue = getInvalidDataValue();
        StringBuilder sb = new StringBuilder("PF('" + widgetVar + "')");
        sb.append(".cfg.errors=");
        sb.append(invalidValue);
        sb.append(";");
        sb.append("PF('" + widgetVar + "')");
        sb.append(".ht.render();");
        RequestContext.getCurrentInstance().getScriptsToExecute().add(sb.toString());

        sb = new StringBuilder();
        sb.append("PF('" + widgetVar + "')");
        sb.append(".sheetDiv.removeClass('ui-state-error')");
        if (!getInvalidUpdates().isEmpty()) {
            sb.append(".addClass('ui-state-error')");
        }
        RequestContext.getCurrentInstance().getScriptsToExecute().add(sb.toString());
    }

    /**
     * Private class used as a key for row,col maps.
     */
    private class RowColIndex implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String rowKey;
        private final Integer colIndex;

        /**
         * Constructs an instance of RowColIndex for the row and column specified.
         *
         * @param row the row represented by this index
         * @param col the column respresented by this index
         */
        public RowColIndex(String rowKey, Integer col) {
            this.rowKey = rowKey;
            colIndex = col;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof RowColIndex)) {
                return false;
            }
            final RowColIndex castOther = (RowColIndex) other;
            return new EqualsBuilder().append(rowKey, castOther.rowKey).append(colIndex, castOther.colIndex).isEquals();
        }

        @Override
        public String toString() {
            return "RowColIndex [rowKey=" + rowKey + ", colIndex=" + colIndex + "]";
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(rowKey).append(colIndex).toHashCode();
        }

        /**
         * The rowIndex value.
         *
         * @return the rowIndex
         */
        public String getRowKey() {
            return rowKey;
        }

        /**
         * The colIndex value.
         *
         * @return the colIndex
         */
        public Integer getColIndex() {
            return colIndex;
        }

    }
}
