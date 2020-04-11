/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetRowColIndex;
import org.primefaces.extensions.model.sheet.SheetUpdate;
import org.primefaces.extensions.util.JavascriptVarBuilder;
import org.primefaces.model.BeanPropertyComparator;
import org.primefaces.model.SortOrder;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * Spreadsheet component wrappering the Handsontable jQuery UI component.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = "primefaces-extensions", target = "head", name = "sheet/sheet.css")
@ResourceDependency(library = "primefaces-extensions", name = "sheet/sheet.js")
public class Sheet extends SheetBase {

    public static final String EVENT_CELL_SELECT = "cellSelect";
    public static final String EVENT_CHANGE = "change";
    public static final String EVENT_SORT = "sort";
    public static final String EVENT_FILTER = "filter";
    public static final String EVENT_COLUMN_SELECT = "columnSelect";
    public static final String EVENT_ROW_SELECT = "rowSelect";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Sheet";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(EVENT_CHANGE,
                EVENT_CELL_SELECT, EVENT_SORT, EVENT_FILTER, EVENT_COLUMN_SELECT, EVENT_ROW_SELECT));

    /**
     * The list of UI Columns
     */
    private List<SheetColumn> columns;

    /**
     * List of invalid updates
     */
    private List<SheetInvalidUpdate> invalidUpdates;

    /**
     * Map of submitted values by row index and column index
     */
    private Map<SheetRowColIndex, String> submittedValues = new HashMap<>();

    /**
     * Map of local values by row index and column index
     */
    private Map<SheetRowColIndex, Object> localValues = new HashMap<>();

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
    private final List<SheetUpdate> updates = new ArrayList<>();

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
     * Map by row keys for row number
     */
    private Map<String, Integer> rowNumbers;

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
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final SheetEvent sheetEvent = new SheetEvent(this, behaviorEvent.getBehavior());
            sheetEvent.setPhaseId(event.getPhaseId());
            super.queueEvent(sheetEvent);
            return;
        }

        super.queueEvent(event);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                    .get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    /**
     * The list of child columns.
     *
     * @return
     */
    public List<SheetColumn> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
            getColumns(this);
        }
        return columns;
    }

    /**
     * Grabs the UIColumn children for the parent specified.
     *
     * @param parent
     */
    private void getColumns(final UIComponent parent) {
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
    public void setColumns(final List<SheetColumn> columns) {
        this.columns = columns;
    }

    /**
     * The list of invalid updates
     *
     * @return List<SheetInvalidUpdate>
     */
    public List<SheetInvalidUpdate> getInvalidUpdates() {
        if (invalidUpdates == null) {
            invalidUpdates = new ArrayList<>();
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
        // Set to null to restore initial sort order specified by sortBy
        getStateHelper().put(PropertyKeys.currentSortBy.name(), null);

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
     * @param context
     * @param rowKey
     * @param col
     * @param value
     */
    public void setSubmittedValue(final String rowKey, final int col, final String value) {
        submittedValues.put(new SheetRowColIndex(rowKey, col), value);
    }

    /**
     * Retrieves the submitted value for the row and col.
     *
     * @param rowKey
     * @param col
     * @return
     */
    public String getSubmittedValue(final String rowKey, final int col) {
        return submittedValues.get(new SheetRowColIndex(rowKey, col));
    }

    /**
     * Updates a local value.
     *
     * @param rowKey
     * @param col
     * @param value
     */
    public void setLocalValue(final String rowKey, final int col, final Object value) {
        localValues.put(new SheetRowColIndex(rowKey, col), value);
    }

    /**
     * Retrieves the submitted value for the rowKey and col.
     *
     * @param rowKey
     * @param col
     * @return
     */
    public Object getLocalValue(final String rowKey, final int col) {
        return localValues.get(new SheetRowColIndex(rowKey, col));
    }

    /**
     * Updates the row var for iterations over the list. The var value will be updated to the value for the specified rowKey.
     *
     * @param context the FacesContext against which to the row var is set. Passed for performance
     * @param rowKey the rowKey string
     */
    public void setRowVar(final FacesContext context, final String rowKey) {

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
            remapRows();
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
    public Object getValueForCell(final FacesContext context, final String rowKey, final int col) {
        // if we have a local value, use it
        // note: can't check for null, as null may be the submitted value
        final SheetRowColIndex index = new SheetRowColIndex(rowKey, col);
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
    public String getRenderValueForCell(final FacesContext context, final String rowKey, final int col) {

        // if we have a submitted value still, use it
        // note: can't check for null, as null may be the submitted value
        final SheetRowColIndex index = new SheetRowColIndex(rowKey, col);
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
     * Gets the row header text value as a string for use in javascript
     *
     * @param context
     * @return
     */
    protected String getRowHeaderValueAsString(final FacesContext context) {
        final ValueExpression veRowHeader = getRowHeaderValueExpression();
        final Object value = veRowHeader.getValue(context.getELContext());
        if (value == null) {
            return Constants.EMPTY_STRING;
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
        List<Object> filtered = getFilteredValue();
        if (filtered == null || filtered.isEmpty()) {
            filtered = sortAndFilter();
        }
        return filtered;
    }

    /**
     * Gets the rendered col index of the column corresponding to the current sortBy. This is used to keep track of the current sort column in the page.
     *
     * @return
     */
    public int getSortColRenderIndex() {
        // Was the column by which to sort changed by the user, ie. is there a saved ID?
        String currentSortById = (String) getStateHelper().get(PropertyKeys.currentSortBy.name());
        // Otherwise, did the user specify a valid column ID for the sortBy attribute?
        if (StringUtils.isEmpty(currentSortById)) {
            final Object sortBy = getStateHelper().eval(PropertyKeys.sortBy.name());
            if (sortBy instanceof String) {
                currentSortById = (String) sortBy;
            }
        }
        if (StringUtils.isNotEmpty(currentSortById)) {
            int colIdx = 0;
            for (final SheetColumn column : getColumns()) {
                if (!column.isRendered()) {
                    continue;
                }

                if (currentSortById.equals(column.getId())) {
                    return colIdx;
                }
                colIdx++;
            }
        }

        // Otherwise, fall back to the previous behavior of searching for the column
        // by its value expression
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
            if (veCol != null && veCol.getExpressionString().equals(sortByExp)) {
                return colIdx;
            }
            colIdx++;
        }
        return -1;
    }

    /**
     * Evaluates the specified item value against the column filters and if they match, returns true, otherwise false. If no filterMatchMode is given on a
     * column than the "contains" mode is used. Otherwise the following filterMatchMode values are possible: - startsWith: Checks if column value starts with
     * the filter value. - endsWith: Checks if column value ends with the filter value. - contains: Checks if column value contains the filter value. - exact:
     * Checks if string representations of column value and filter value are same.
     *
     * @return
     */
    protected boolean matchesFilter() {
        for (final SheetColumn col : getColumns()) {
            final String filterValue = col.getFilterValue();
            if (LangUtils.isValueBlank(filterValue)) {
                continue;
            }

            final Object filterBy = col.getFilterBy();
            // if we have a filter, but no value in the row, no match
            if (filterBy == null) {
                return false;
            }

            String filterMatchMode = col.getFilterMatchMode();
            if (LangUtils.isValueBlank(filterMatchMode)) {
                filterMatchMode = "contains";
            }

            // case-insensitive
            final String value = filterBy.toString().toLowerCase();
            final String filter = filterValue.toLowerCase();
            switch (filterMatchMode) {
                case "startsWith":
                    if (!value.startsWith(filter)) {
                        return false;
                    }
                    break;
                case "endsWith":
                    if (!value.endsWith(filter)) {
                        return false;
                    }
                    break;
                case "exact":
                    if (!value.equals(filter)) {
                        return false;
                    }
                    break;
                default:
                    // contains is default
                    if (!value.contains(filter)) {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * Sorts and filters the data
     */
    public List<Object> sortAndFilter() {
        final List filteredList = getFilteredValue();
        filteredList.clear();
        rowMap = new HashMap<>();
        rowNumbers = new HashMap<>();

        final Collection<?> values = (Collection<?>) getValue();
        if (values == null || values.isEmpty()) {
            return filteredList;
        }

        remapRows();

        boolean filters = false;
        for (final SheetColumn col : getColumns()) {
            if (!LangUtils.isValueBlank(col.getFilterValue())) {
                filters = true;
                break;
            }
        }

        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final String var = getVar();

        if (filters) {
            // iterate and add those matching the filters
            for (final Object obj : values) {
                requestMap.put(var, obj);
                if (matchesFilter()) {
                    filteredList.add(obj);
                }
            }
        }
        else {
            filteredList.addAll(values);
        }

        // Sort by the saved column. When none was saved, sort by the "sortBy" attribute of the sheet.
        final int sortByIdx = getSortColRenderIndex();
        final SheetColumn currentSortByColumn = sortByIdx >= 0 ? getColumns().get(sortByIdx) : null;
        final ValueExpression currentSortByVe = currentSortByColumn != null ? currentSortByColumn.getValueExpression(
                    PropertyKeys.sortBy.name()) : null;
        final ValueExpression veSortBy;
        if (currentSortByVe != null) {
            veSortBy = currentSortByVe;
        }
        else {
            veSortBy = getValueExpression(PropertyKeys.sortBy.name());
        }
        if (veSortBy != null) {
            Collections.sort(filteredList, new BeanPropertyComparator(veSortBy, var, convertSortOrder(), null,
                        isCaseSensitiveSort(), Locale.ENGLISH, getNullSortOrder()));
        }

        // map filtered rows
        remapFilteredList(filteredList);
        return filteredList;
    }

    /**
     * Remaps the row keys in a hash map.
     */
    protected void remapFilteredList(final List filteredList) {
        rowNumbers = new HashMap<>(rowMap.size());
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final String var = getVar();
        int row = 0;
        for (final Object value : filteredList) {
            requestMap.put(var, value);
            rowNumbers.put(getRowKeyValueAsString(context), Integer.valueOf(row));
            row++;
        }
        requestMap.remove(var);
    }

    /**
     * Remaps the row keys in a hash map.
     */
    protected void remapRows() {
        rowMap = new HashMap<>();
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final Collection<?> values = (Collection<?>) getValue();
        final String var = getVar();
        for (final Object obj : values) {
            requestMap.put(var, obj);
            try {
                final String key = getRowKeyValueAsString(context);
                rowMap.put(key, obj);
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
    @Override
    protected Object getRowKeyValue(final FacesContext context) {
        final ValueExpression veRowKey = getValueExpression(PropertyKeys.rowKey.name());
        if (veRowKey == null) {
            throw new FacesException("RowKey required on sheet!");
        }
        final Object value = veRowKey.getValue(context.getELContext());
        if (value == null) {
            throw new FacesException("RowKey must resolve to non-null value for updates to work properly");
        }
        return value;
    }

    /**
     * Gets the row key value as a String suitable for use in javascript rendering.
     *
     * @param key
     * @return
     */
    protected String getRowKeyValueAsString(final Object key) {
        final String result = key.toString();
        return "r_" + StringUtils.deleteWhitespace(result);
    }

    /**
     * Gets the row key value as a string for the current row var.
     *
     * @param context
     * @return
     */
    protected String getRowKeyValueAsString(final FacesContext context) {
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
     * Converts each submitted value into a local value and stores it back in the hash. If all values convert without error, then the component is valid, and we
     * can proceed to the processUpdates.
     */
    @Override
    public void validate(final FacesContext context) {
        // iterate over submitted values and attempt to convert to the proper
        // data type. For successful values, remove from submitted and add to
        // local values map. for failures, add a conversion message and leave in
        // the submitted state
        final Iterator<Entry<SheetRowColIndex, String>> entries = submittedValues.entrySet().iterator();
        final boolean hadBadUpdates = !getInvalidUpdates().isEmpty();
        getInvalidUpdates().clear();
        while (entries.hasNext()) {
            final Entry<SheetRowColIndex, String> entry = entries.next();
            final SheetColumn column = getColumns().get(entry.getKey().getColIndex());
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

        if ((hadBadUpdates || newBadUpdates) && context.getPartialViewContext().isPartialRequest()) {
            // update the bad data var if partial request
            renderBadUpdateScript(context);
        }

        if (newBadUpdates && errorMessage != null) {
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
            context.addMessage(null, message);
        }
    }

    /**
     * Override to update model with local values. Note that this is where things can be fragile in that we can successfully update some values and fail on
     * others. There is no clean way to roll back the updates, but we also need to fail processing. Consider keeping old values as we update (need for event
     * anyhow) and if there is a failure attempt to roll back by updating successful model updates with the old value. This may not all be necessary.
     */
    @Override
    public void updateModel(final FacesContext context) {
        final Iterator<Entry<SheetRowColIndex, Object>> entries = localValues.entrySet().iterator();
        // Keep track of the dirtied rows for ajax callbacks so we can send
        // updates on what was touched
        final HashSet<String> dirtyRows = new HashSet<>();
        while (entries.hasNext()) {
            final Entry<SheetRowColIndex, Object> entry = entries.next();

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
    public Object saveState(final FacesContext context) {
        final Object values[] = new Object[8];
        values[0] = super.saveState(context);
        values[1] = submittedValues;
        values[2] = localValues;
        values[3] = invalidUpdates;
        values[4] = columnMapping;
        values[5] = getFilteredValue();
        values[6] = rowMap;
        values[7] = rowNumbers;
        return values;
    }

    /**
     * Restores the state for the submitted, local and bad values.
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        if (state == null) {
            return;
        }

        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        final Object restoredSubmittedValues = values[1];
        final Object restoredLocalValues = values[2];
        final Object restoredInvalidUpdates = values[3];
        final Object restoredColMappings = values[4];
        final Object restoredSortedList = values[5];
        final Object restoredRowMap = values[6];
        final Object restoredRowNumbers = values[7];

        if (restoredSubmittedValues == null) {
            submittedValues.clear();
        }
        else {
            submittedValues = (Map<SheetRowColIndex, String>) restoredSubmittedValues;
        }

        if (restoredLocalValues == null) {
            localValues.clear();
        }
        else {
            localValues = (Map<SheetRowColIndex, Object>) restoredLocalValues;
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
            getFilteredValue().clear();
        }
        else {
            setFilteredValue((List<Object>) restoredSortedList);
        }

        if (restoredRowMap == null) {
            rowMap = null;
        }
        else {
            rowMap = (Map<String, Object>) restoredRowMap;
        }

        if (restoredRowNumbers == null) {
            rowNumbers = null;
        }
        else {
            rowNumbers = (Map<String, Integer>) restoredRowNumbers;
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
    public void setSelection(final String selection) {
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
    public void setSubmittedValue(final Object submittedValue) {
        if (submittedValue == null) {
            submittedValues.clear();
        }
        else {
            submittedValues = (Map<SheetRowColIndex, String>) submittedValue;
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
     * @param renderCol the rendered index
     * @return the mapped index
     */
    public int getMappedColumn(final int renderCol) {
        if (columnMapping == null || renderCol == -1) {
            return renderCol;
        }
        else {
            final Integer result = columnMapping.get(renderCol);
            if (result == null) {
                throw new IllegalArgumentException("Invalid index " + renderCol);
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
    public int getRenderIndexFromRealIdx(final int realIdx) {
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
        columnMapping = new HashMap<>();
        int realIdx = 0;
        int renderCol = 0;
        for (final SheetColumn column : getColumns()) {
            if (column.isRendered()) {
                columnMapping.put(renderCol, realIdx);
                renderCol++;
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
        return getSortedValues().size();
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
    public void setFocusId(final String focusId) {
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
            final String jsVar = resolveWidgetVar();
            eval.append("PF('").append(jsVar).append("')").append(".clearDataInput();");
            PrimeFaces.current().executeScript(eval.toString());
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
            vb.appendProperty(rowKeyProperty + "_c" + col,
                        sheetInvalidUpdate.getInvalidMessage().replace("'", "&apos;"), true);
        }
        return vb.closeVar().toString();
    }

    /**
     * Adds eval scripts to the ajax response to update the rows dirtied by the most recent successful update request.
     *
     * @param context the FacesContext
     * @param dirtyRows the set of dirty rows
     */
    public void renderRowUpdateScript(final FacesContext context, final Set<String> dirtyRows) {
        final String jsVar = resolveWidgetVar();
        final StringBuilder eval = new StringBuilder();

        for (final String rowKey : dirtyRows) {
            setRowVar(context, rowKey);
            final int rowIndex = rowNumbers.get(rowKey);
            // data is array of array of data
            final JavascriptVarBuilder jsRow = new JavascriptVarBuilder(null, false);
            final JavascriptVarBuilder jsStyle = new JavascriptVarBuilder(null, true);
            final JavascriptVarBuilder jsReadOnly = new JavascriptVarBuilder(null, true);
            int renderCol = 0;
            for (int col = 0; col < getColumns().size(); col++) {
                final SheetColumn column = getColumns().get(col);
                if (!column.isRendered()) {
                    continue;
                }

                // render data value
                final String value = getRenderValueForCell(context, rowKey, col);
                jsRow.appendArrayValue(value, true);

                // custom style
                final String styleClass = column.getStyleClass();
                if (styleClass != null) {
                    jsStyle.appendRowColProperty(rowIndex, renderCol, styleClass, true);
                }

                // read only per cell
                final boolean readOnly = column.isReadonlyCell();
                if (readOnly) {
                    jsReadOnly.appendRowColProperty(rowIndex, renderCol, "true", true);
                }
                renderCol++;
            }
            eval.append("PF('").append(jsVar).append("')");
            eval.append(".updateData('");
            eval.append(rowIndex);
            eval.append("',");
            eval.append(jsRow.closeVar().toString());
            eval.append(",");
            eval.append(jsStyle.closeVar().toString());
            eval.append(",");
            eval.append(jsReadOnly.closeVar().toString());
            eval.append(");");
        }
        eval.append("PF('").append(jsVar).append("')").append(".redraw();");
        PrimeFaces.current().executeScript(eval.toString());
    }

    /**
     * Adds eval scripts to update the bad data array in the sheet to render validation failures produced by the most recent ajax update attempt.
     *
     * @param context the FacesContext
     */
    public void renderBadUpdateScript(final FacesContext context) {
        final String widgetVar = resolveWidgetVar();
        final String invalidValue = getInvalidDataValue();
        StringBuilder sb = new StringBuilder("PF('" + widgetVar + "')");
        sb.append(".cfg.errors=");
        sb.append(invalidValue);
        sb.append(";");
        sb.append("PF('" + widgetVar + "')");
        sb.append(".ht.render();");
        PrimeFaces.current().executeScript(sb.toString());

        sb = new StringBuilder();
        sb.append("PF('").append(widgetVar).append("')");
        sb.append(".sheetDiv.removeClass('ui-state-error')");
        if (!getInvalidUpdates().isEmpty()) {
            sb.append(".addClass('ui-state-error')");
        }
        PrimeFaces.current().executeScript(sb.toString());
    }

    /**
     * Appends an update event
     */
    public void appendUpdateEvent(final Object rowKey, final int colIndex, final Object rowData, final Object oldValue,
                final Object newValue) {
        updates.add(new SheetUpdate(rowKey, colIndex, rowData, oldValue, newValue));
    }
}
