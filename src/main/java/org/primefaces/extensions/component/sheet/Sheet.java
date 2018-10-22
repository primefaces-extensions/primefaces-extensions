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
import org.primefaces.component.api.Widget;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetRowColIndex;
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
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "components.css"),
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

	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(EVENT_CHANGE,
			EVENT_CELL_SELECT, EVENT_SORT, EVENT_FILTER, EVENT_COLUMN_SELECT, EVENT_ROW_SELECT));

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
		 * The request scope attribute under which the data object for the current row
		 * will be exposed when iterating.
		 */
		var,

		/**
		 * The IL8N Locale. Default is en-US.
		 */
		locale,

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
		 * The custom row header to be used in place of the standard numeric header
		 * value
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
		 * You can fix the bottom rows of the table, by using the fixedRowsBottom config
		 * option. This way, when you're scrolling the table, the fixed rows will stay
		 * at the bottom edge of the table's container.
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
		 * Defines where the null values are placed in ascending sort order. Default
		 * value is "1" meaning null values are placed at the end in ascending mode and
		 * at beginning in descending mode. Set to "-1" for the opposite behavior.
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
		 * Name of javascript function to extend the options of the underlying
		 * Handsontable plugin.
		 */
		extender
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
	 * Transient list of sheet updates that can be accessed after a successful model
	 * update.
	 */
	private final List<SheetUpdate> updates = new ArrayList<>();

	/**
	 * Maps a visible, rendered column index to the actual column based on whether
	 * or not the column is rendered. Updated on encode, and used on decode. Saved
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
	public void setSubmittedValue(final FacesContext context, final String rowKey, final int col, final String value) {
		submittedValues.put(new SheetRowColIndex(rowKey, col), value);
	}

	/**
	 * Retrieves the submitted value for the row and col.
	 *
	 * @param row
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
	 * @param row
	 * @param col
	 * @return
	 */
	public Object getLocalValue(final String rowKey, final int col) {
		return localValues.get(new SheetRowColIndex(rowKey, col));
	}

	/**
	 * Updates the row var for iterations over the list. The var value will be
	 * updated to the value for the specified rowKey.
	 *
	 * @param context the FacesContext against which to the row var is set. Passed
	 *                for performance
	 * @param rowKey  the rowKey string
	 */
	public void setRowVar(final FacesContext context, final String rowKey) {

		if (context == null) {
			return;
		}

		if (rowKey == null) {
			context.getExternalContext().getRequestMap().remove(getVar());
		} else {
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
	 * Gets the object value of the row and col specified. If a local value exists,
	 * that is returned, otherwise the actual value is return.
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
	 * Gets the render string for the value the given cell. Applys the available
	 * converters to convert the value.
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
		} else {
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
	public void setWidth(final Integer value) {
		getStateHelper().put(PropertyKeys.width, value);
	}

	/**
	 * The height of the sheet. Note this is applied to the inner div which is why
	 * it is recommend you use this property instead of a style class.
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
	 * Return the value of the Sheet. This value must be a java.util.List value at
	 * this time.
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
	 * Set the value of the <code>Sheet</code>. This value must be a java.util.List
	 * at this time.
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
	 * @return a value expression for Row Header or null if the expression is not
	 *         set
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
	protected String getRowHeaderValueAsString(final FacesContext context) {
		final ValueExpression veRowHeader = getRowHeaderValueExpression();
		final Object value = veRowHeader.getValue(context.getELContext());
		if (value == null) {
			return StringUtils.EMPTY;
		} else {
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
	 * Gets the rendered col index of the column corresponding to the current
	 * sortBy. This is used to keep track of the current sort column in the page.
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
	 * Evaluates the specified item value against the column filters and if they
	 * match, returns true, otherwise false. If no filterMatchMode is given on a
	 * column than the "contains" mode is used. Otherwise the following
	 * filterMatchMode values are possible: - startsWith: Checks if column value
	 * starts with the filter value. - endsWith: Checks if column value ends with
	 * the filter value. - contains: Checks if column value contains the filter
	 * value. - exact: Checks if string representations of column value and filter
	 * value are same.
	 *
	 * @param obj
	 * @return
	 */
	protected boolean matchesFilter(final Object obj) {
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

			String filterMatchMode = col.getFilterMatchMode();
			if (StringUtils.isEmpty(filterMatchMode)) {
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
			if (StringUtils.isNotEmpty(col.getFilterValue())) {
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
				if (matchesFilter(obj)) {
					filteredList.add(obj);
				}
			}
		} else {
			filteredList.addAll(values);
		}

		final ValueExpression veSortBy = getValueExpression(PropertyKeys.sortBy.name());
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
			} finally {
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
	 * Gets the row key value as a String suitable for use in javascript rendering.
	 *
	 * @param context
	 * @return
	 */
	protected String getRowKeyValueAsString(final Object key) {
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
		} else {
			final SortOrder result = SortOrder.valueOf(sortOrder.toUpperCase(Locale.ENGLISH));
			return result;
		}
	}

	/**
	 * Return the request-scope attribute under which the data object for the
	 * current row will be exposed when iterating. This property is
	 * <strong>not</strong> enabled for value binding expressions.
	 */
	public String getVar() {
		// must be a string literal (no eval)
		return (String) getStateHelper().get(PropertyKeys.var);
	}

	/**
	 * Set the request-scope attribute under which the data object for the current
	 * row wil be exposed when iterating.
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

	/**
	 * Converts each submitted value into a local value and stores it back in the
	 * hash. If all values convert without error, then the component is valid, and
	 * we can proceed to the processUpdates.
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
				} catch (final ConverterException e) {
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
			} finally {
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
	 * Override to update model with local values. Note that this is where things
	 * can be fragile in that we can successfully update some values and fail on
	 * others. There is no clean way to roll back the updates, but we also need to
	 * fail processing. TODO consider keeping old values as we update (need for
	 * event anyhow) and if there is a failure attempt to roll back by updating
	 * successful model updates with the old value. This may not all be necessary.
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

		final Object values[] = (Object[]) state;
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
		} else {
			submittedValues = (Map<SheetRowColIndex, String>) restoredSubmittedValues;
		}

		if (restoredLocalValues == null) {
			localValues.clear();
		} else {
			localValues = (Map<SheetRowColIndex, Object>) restoredLocalValues;
		}

		if (restoredInvalidUpdates == null) {
			getInvalidUpdates().clear();
		} else {
			invalidUpdates = (List<SheetInvalidUpdate>) restoredInvalidUpdates;
		}

		if (restoredColMappings == null) {
			columnMapping = null;
		} else {
			columnMapping = (Map<Integer, Integer>) restoredColMappings;
		}

		if (restoredSortedList == null) {
			getFilteredValue().clear();
		} else {
			setFilteredValue((List<Object>) restoredSortedList);
		}

		if (restoredRowMap == null) {
			rowMap = null;
		} else {
			rowMap = (Map<String, Object>) restoredRowMap;
		}

		if (restoredRowNumbers == null) {
			rowNumbers = null;
		} else {
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
	 *
	 * @see javax.faces.component.EditableValueHolder#getSubmittedValue()
	 */
	@Override
	public Object getSubmittedValue() {
		if (submittedValues.isEmpty()) {
			return null;
		} else {
			return submittedValues;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.EditableValueHolder#setSubmittedValue(java.lang
	 * .Object)
	 */
	@Override
	public void setSubmittedValue(final Object submittedValue) {
		if (submittedValue == null) {
			submittedValues.clear();
		} else {
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
	 * @param renderIdx the rendered index
	 * @return the mapped index
	 */
	public int getMappedColumn(final int renderIdx) {
		if (columnMapping == null || renderIdx == -1) {
			return renderIdx;
		} else {
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
	public void setFocusId(final String focusId) {
		this.focusId = focusId;
	}

	/**
	 * Invoke this method to commit the most recent set of ajax updates and restart
	 * the tracking of changes. Use this when you have processes the updates to the
	 * model and are confident that any changes made to this point can be cleared
	 * (likely because you have persisted those changes).
	 */
	public void commitUpdates() {
		resetSubmitted();
		final FacesContext context = FacesContext.getCurrentInstance();
		if (context.getPartialViewContext().isPartialRequest()) {
			final StringBuilder eval = new StringBuilder();
			final String jsVar = resolveWidgetVar();
			eval.append("PF('").append(jsVar).append("')").append(".clearDataInput();");
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
			vb.appendProperty(rowKeyProperty + "_c" + col,
					sheetInvalidUpdate.getInvalidMessage().replace("'", "&apos;"), true);
		}
		return vb.closeVar().toString();
	}

	/**
	 * Adds eval scripts to the ajax response to update the rows dirtied by the most
	 * recent successful update request.
	 *
	 * @param context   the FacesContext
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
					jsStyle.appendRowColProperty(rowIndex, col, styleClass, true);
				}

				// read only per cell
				final boolean readOnly = column.isReadonlyCell();
				if (readOnly) {
					jsReadOnly.appendRowColProperty(rowIndex, col, "true", true);
				}
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
		RequestContext.getCurrentInstance().getScriptsToExecute().add(eval.toString());
	}

	/**
	 * Adds eval scripts to update the bad data array in the sheet to render
	 * validation failures produced by the most recent ajax update attempt.
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
		RequestContext.getCurrentInstance().getScriptsToExecute().add(sb.toString());

		sb = new StringBuilder();
		sb.append("PF('").append(widgetVar).append("')");
		sb.append(".sheetDiv.removeClass('ui-state-error')");
		if (!getInvalidUpdates().isEmpty()) {
			sb.append(".addClass('ui-state-error')");
		}
		RequestContext.getCurrentInstance().getScriptsToExecute().add(sb.toString());
	}

	/**
	 * Appends an update event
	 */
	public void appendUpdateEvent(final Object rowKey, final int colIndex, final Object rowData, final Object oldValue,
			final Object newValue) {
		updates.add(new SheetUpdate(rowKey, colIndex, rowData, oldValue, newValue));
	}
}
