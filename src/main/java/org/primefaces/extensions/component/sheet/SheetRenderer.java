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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.util.JavascriptVarBuilder;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * The Sheet renderer.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetRenderer extends CoreRenderer {

	/**
	 * Encodes the Sheet component
	 */
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter responseWriter = context.getResponseWriter();
		final Sheet sheet = (Sheet) component;

		// update column mappings on render
		sheet.updateColumnMappings();

		// sort data
		sheet.sortAndFilter();

		// encode markup
		encodeMarkup(context, sheet, responseWriter);

		// encode javascript
		encodeScript(context, sheet, responseWriter);
	}

	/**
	 * Encodes the HTML markup for the sheet.
	 *
	 * @param context
	 * @param sheet
	 * @throws IOException
	 */
	protected void encodeMarkup(final FacesContext context, final Sheet sheet, final ResponseWriter responseWriter)
			throws IOException {
		/*
		 * <div id="..." name="..." class="" style="">
		 */
		final String styleClass = sheet.getStyleClass();
		final String clientId = sheet.getClientId(context);
		final Integer width = sheet.getWidth();
		final Integer height = sheet.getHeight();
		String style = sheet.getStyle();

		// outer div to wrapper table
		responseWriter.startElement("div", null);
		responseWriter.writeAttribute("id", clientId, "id");
		responseWriter.writeAttribute("name", clientId, "clientId");
		// note: can't use ui-datatable here because it will mess with
		// handsontable cell rendering
		String divclass = "ui-handsontable ui-widget";
		if (styleClass != null) {
			divclass = divclass + " " + styleClass;
		}
		if (!sheet.isValid()) {
			divclass = divclass + " ui-state-error";
		}

		responseWriter.writeAttribute("class", divclass, "styleClass");
		if (width != null) {
			responseWriter.writeAttribute("style", "width: " + width + "px;", null);
		}

		encodeHiddenInputs(responseWriter, sheet, clientId);
		encodeFilterValues(context, responseWriter, sheet, clientId);
		encodeHeader(context, responseWriter, sheet);

		// handsontable div
		responseWriter.startElement("div", null);
		responseWriter.writeAttribute("id", clientId + "_tbl", "id");
		responseWriter.writeAttribute("name", clientId + "_tbl", "clientId");
		responseWriter.writeAttribute("class", "handsontable-inner", "styleClass");

		if (style == null) {
			style = StringUtils.EMPTY;
		}

		if (width != null) {
			style = style + "width: " + width + "px;";
		}

		if (height != null) {
			style = style + "height: " + height + "px;";
		} else {
			style = style + "height: 100%;";
		}

		if (style.length() > 0) {
			responseWriter.writeAttribute("style", style, null);
		}

		responseWriter.endElement("div");
		encodeFooter(context, responseWriter, sheet);
		responseWriter.endElement("div");
	}

	/**
	 * Encodes an optional attribute to the widget builder specified.
	 *
	 * @param wb       the WidgetBuilder to append to
	 * @param attrName the attribute name
	 * @param value    the value
	 * @throws IOException
	 */
	protected void encodeOptionalAttr(final WidgetBuilder wb, final String attrName, final String value)
			throws IOException {
		if (value != null) {
			wb.attr(attrName, value);
		}
	}

	/**
	 * Encodes an optional native attribute (unquoted).
	 *
	 * @param wb       the WidgetBuilder to append to
	 * @param attrName the attribute name
	 * @param value    the value
	 * @throws IOException
	 */
	protected void encodeOptionalNativeAttr(final WidgetBuilder wb, final String attrName, final Object value)
			throws IOException {
		if (value != null) {
			wb.nativeAttr(attrName, value.toString());
		}
	}

	/**
	 * Encodes the Javascript for the sheet.
	 *
	 * @param context
	 * @param sheet
	 * @throws IOException
	 */
	protected void encodeScript(final FacesContext context, final Sheet sheet, final ResponseWriter responseWriter)
			throws IOException {
		final WidgetBuilder wb = getWidgetBuilder(context);
		final String clientId = sheet.getClientId(context);
		wb.initWithDomReady("ExtSheet", sheet.resolveWidgetVar(), clientId);

		// errors
		encodeInvalidData(context, sheet, wb);
		// data
		encodeData(context, sheet, wb);

		// the delta var that will be used to track changes client side
		// stringified and placed in hidden input for submission
		wb.nativeAttr("delta", "{}");

		// filters
		encodeFilterVar(context, sheet, wb);
		// sortable
		encodeSortVar(context, sheet, wb);
		// behaviors
		encodeBehaviors(context, sheet, wb);

		encodeOptionalNativeAttr(wb, "readOnly", sheet.isReadOnly());
		encodeOptionalNativeAttr(wb, "fixedColumnsLeft", sheet.getFixedCols());
		encodeOptionalNativeAttr(wb, "fixedRowsTop", sheet.getFixedRows());
		encodeOptionalNativeAttr(wb, "fixedRowsBottom", sheet.getFixedRowsBottom());
		encodeOptionalNativeAttr(wb, "manualColumnResize", sheet.isResizableCols());
		encodeOptionalNativeAttr(wb, "manualRowResize", sheet.isResizableRows());
		encodeOptionalNativeAttr(wb, "manualColumnMove", sheet.isMovableCols());
		encodeOptionalNativeAttr(wb, "manualRowMove", sheet.isMovableRows());
		encodeOptionalNativeAttr(wb, "width", sheet.getWidth());
		encodeOptionalNativeAttr(wb, "height", sheet.getHeight());
		encodeOptionalNativeAttr(wb, "minRows", sheet.getMinRows());
		encodeOptionalNativeAttr(wb, "minCols", sheet.getMinCols());
		encodeOptionalNativeAttr(wb, "maxRows", sheet.getMaxRows());
		encodeOptionalNativeAttr(wb, "maxCols", sheet.getMaxCols());
		encodeOptionalAttr(wb, "stretchH", sheet.getStretchH());
		encodeOptionalAttr(wb, "activeHeaderClassName", sheet.getActiveHeaderStyleClass());
		encodeOptionalAttr(wb, "commentedCellClassName", sheet.getCommentedCellStyleClass());
		encodeOptionalAttr(wb, "currentRowClassName", sheet.getCurrentRowStyleClass());
		encodeOptionalAttr(wb, "currentColClassName", sheet.getCurrentColStyleClass());
		encodeOptionalAttr(wb, "currentHeaderClassName", sheet.getCurrentHeaderStyleClass());
		encodeOptionalAttr(wb, "invalidCellClassName", sheet.getInvalidCellStyleClass());
		encodeOptionalAttr(wb, "noWordWrapClassName", sheet.getNoWordWrapStyleClass());
		encodeOptionalAttr(wb, "placeholderCellClassName", sheet.getPlaceholderCellStyleClass());
		encodeOptionalAttr(wb, "readOnlyCellClassName", sheet.getReadOnlyCellStyleClass());
		encodeOptionalNativeAttr(wb, "extender", sheet.getExtender());

		String emptyMessage = sheet.getEmptyMessage();
		if (StringUtils.isEmpty(emptyMessage)) {
			emptyMessage = "No Records Found";
		}
		encodeOptionalAttr(wb, "emptyMessage", emptyMessage);

		encodeColHeaders(context, sheet, wb);
		encodeColOptions(context, sheet, wb);
		wb.finish();
	}

	/**
	 * Encodes the necessary JS to render invalid data.
	 *
	 * @throws IOException
	 */
	protected void encodeInvalidData(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		wb.attr("errors", sheet.getInvalidDataValue());
	}

	/**
	 * Encode the column headers
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	protected void encodeColHeaders(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, false);
		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}
			vb.appendArrayValue(column.getHeaderText(), true);
		}
		wb.nativeAttr("colHeaders", vb.closeVar().toString());
	}

	/**
	 * Encode the column options
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	protected void encodeColOptions(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, false);
		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}

			final JavascriptVarBuilder options = new JavascriptVarBuilder(null, true);
			options.appendProperty("type", column.getColType(), true);
			options.appendProperty("copyable", "true", false);
			final Integer width = column.getColWidth();
			String calculatedWidth = null;
			if (width != null) {
				calculatedWidth = width.toString();
			}
			// HT doesn't have a hidden property so make column small as possible will leave
			// it in the DOM, if 0 then Handsontable removes it entirely
			if (!column.isVisible()) {
				calculatedWidth = "0.1";
			}
			if (calculatedWidth != null) {
				options.appendProperty("width", calculatedWidth, false);
			}
			if (column.isReadOnly()) {
				options.appendProperty("readOnly", "true", false);
			}

			// validate can be a function, regex, or string
			final String validateFunction = column.getOnvalidate();
			if (validateFunction != null) {
				boolean quoted = false;
				switch (validateFunction) {
				case "autocomplete":
				case "date":
				case "numeric":
				case "time":
					quoted = true;
					break;

				default:
					// its a function or regex!
					quoted = false;
					break;
				}
				options.appendProperty("validator", validateFunction, quoted);
			}

			switch (column.getColType()) {
			case "password":
				final Integer passwordLength = column.getPasswordHashLength();
				if (passwordLength != null) {
					options.appendProperty("hashLength", passwordLength.toString(), false);
				}
				final String passwordSymbol = column.getPasswordHashSymbol();
				if (passwordSymbol != null) {
					options.appendProperty("hashSymbol", passwordSymbol, true);
				}
				break;
			case "numeric":
				final JavascriptVarBuilder numeric = new JavascriptVarBuilder(null, true);
				final String pattern = column.getNumericPattern();
				if (pattern != null) {
					numeric.appendProperty("pattern", pattern, true);
				}
				final String culture = column.getNumericLocale();
				if (culture != null) {
					numeric.appendProperty("culture", culture, true);
				}
				options.appendProperty("numericFormat", numeric.closeVar().toString(), false);
				break;
			case "date":
				options.appendProperty("dateFormat", column.getDateFormat(), true);
				options.appendProperty("correctFormat", "true", false);
				final String dateConfig = column.getDatePickerConfig();
				if (dateConfig != null) {
					options.appendProperty("datePickerConfig", dateConfig, false);
				}
				break;
			case "time":
				options.appendProperty("timeFormat", column.getTimeFormat(), true);
				options.appendProperty("correctFormat", "true", false);
				break;
			case "dropdown":
				encodeSelectItems(column, options);
				break;
			case "autocomplete":
				options.appendProperty("strict", column.isAutoCompleteStrict().toString(), false);
				options.appendProperty("allowInvalid", column.isAutoCompleteAllowInvalid().toString(), false);
				options.appendProperty("trimDropdown", column.isAutoCompleteTrimDropdown().toString(), false);
				final Integer visibleRows = column.getAutoCompleteVisibleRows();
				if (visibleRows != null) {
					options.appendProperty("visibleRows", visibleRows.toString(), false);
				}
				encodeSelectItems(column, options);
				break;
			default:
				break;
			}

			vb.appendArrayValue(options.closeVar().toString(), false);
		}
		wb.nativeAttr("columns", vb.closeVar().toString());
	}

	private void encodeSelectItems(final SheetColumn column, final JavascriptVarBuilder options) {
		final JavascriptVarBuilder items = new JavascriptVarBuilder(null, false);
		final Object value = column.getSelectItems();
		if (value == null) {
			return;
		}
		if (value.getClass().isArray()) {
			for (int j = 0; j < Array.getLength(value); j++) {
				final Object item = Array.get(value, j);
				items.appendArrayValue(String.valueOf(item), true);
			}
		} else if (value instanceof Collection) {
			final Collection collection = (Collection) value;
			for (final Iterator it = collection.iterator(); it.hasNext();) {
				final Object item = it.next();
				items.appendArrayValue(String.valueOf(item), true);
			}
		} else if (value instanceof Map) {
			final Map map = (Map) value;

			for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
				final Object item = it.next();
				items.appendArrayValue(String.valueOf(item), true);
			}
		}

		options.appendProperty("source", items.closeVar().toString(), false);
	}

	/**
	 * Encode the row data. Builds row data, style data and read only object.
	 * <p>
	 * TODO figure out how to clean this up without having to iterate over data more
	 * than once and still keep it thread safe (no private member field use).
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	protected void encodeData(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {

		final JavascriptVarBuilder jsData = new JavascriptVarBuilder(null, false);
		final JavascriptVarBuilder jsRowKeys = new JavascriptVarBuilder(null, false);
		final JavascriptVarBuilder jsStyle = new JavascriptVarBuilder(null, true);
		final JavascriptVarBuilder jsRowStyle = new JavascriptVarBuilder(null, false);
		final JavascriptVarBuilder jsReadOnly = new JavascriptVarBuilder(null, true);
		final JavascriptVarBuilder jsRowHeaders = new JavascriptVarBuilder(null, false);

		final boolean isCustomHeader = sheet.getRowHeaderValueExpression() != null;

		final List<Object> values = sheet.getSortedValues();
		int row = 0;
		for (final Object value : values) {
			context.getExternalContext().getRequestMap().put(sheet.getVar(), value);
			final String rowKey = sheet.getRowKeyValueAsString(context);
			jsRowKeys.appendArrayValue(rowKey, true);
			encodeRow(context, rowKey, jsData, jsRowStyle, jsStyle, jsReadOnly, sheet, row);

			// In case of custom row header evaluate the value expression for every row to
			// set the header
			if (sheet.isShowRowHeaders() && isCustomHeader) {
				final String rowHeader = sheet.getRowHeaderValueAsString(context);
				jsRowHeaders.appendArrayValue(rowHeader, true);
			}
			row++;
		}

		sheet.setRowVar(context, null);

		wb.nativeAttr("data", jsData.closeVar().toString());
		wb.nativeAttr("styles", jsStyle.closeVar().toString());
		wb.nativeAttr("rowStyles", jsRowStyle.closeVar().toString());
		wb.nativeAttr("readOnlyCells", jsReadOnly.closeVar().toString());
		wb.nativeAttr("rowKeys", jsRowKeys.closeVar().toString());

		// add the row header as a native attribute
		if (!isCustomHeader) {
			wb.nativeAttr("rowHeaders", sheet.isShowRowHeaders().toString());
		} else {
			wb.nativeAttr("rowHeaders", jsRowHeaders.closeVar().toString());
		}
	}

	/**
	 * Encode a single row.
	 *
	 * @return the JSON row
	 */
	protected JavascriptVarBuilder encodeRow(final FacesContext context, final String rowKey,
			final JavascriptVarBuilder jsData, final JavascriptVarBuilder jsRowStyle,
			final JavascriptVarBuilder jsStyle, final JavascriptVarBuilder jsReadOnly, final Sheet sheet,
			final int rowIndex) throws IOException {
		// encode rowStyle (if any)
		final String rowStyleClass = sheet.getRowStyleClass();
		if (rowStyleClass == null) {
			jsRowStyle.appendArrayValue("null", false);
		} else {
			jsRowStyle.appendArrayValue(rowStyleClass, true);
		}

		// data is array of array of data
		final JavascriptVarBuilder jsRow = new JavascriptVarBuilder(null, false);
		int renderCol = 0;
		for (int col = 0; col < sheet.getColumns().size(); col++) {
			final SheetColumn column = sheet.getColumns().get(col);
			if (!column.isRendered()) {
				continue;
			}

			// render data value
			final String value = sheet.getRenderValueForCell(context, rowKey, col);
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
		// close row and append to jsData
		jsData.appendArrayValue(jsRow.closeVar().toString(), false);
		return jsData;
	}

	/**
	 * Encode hidden input fields
	 *
	 * @param responseWriter
	 * @param sheet
	 * @param clientId
	 * @throws IOException
	 */
	private void encodeHiddenInputs(final ResponseWriter responseWriter, final Sheet sheet, final String clientId)
			throws IOException {
		responseWriter.startElement("input", null);
		responseWriter.writeAttribute("id", clientId + "_input", "id");
		responseWriter.writeAttribute("name", clientId + "_input", "name");
		responseWriter.writeAttribute("type", "hidden", null);
		responseWriter.writeAttribute("value", "", null);
		responseWriter.endElement("input");

		responseWriter.startElement("input", null);
		responseWriter.writeAttribute("id", clientId + "_focus", "id");
		responseWriter.writeAttribute("name", clientId + "_focus", "name");
		responseWriter.writeAttribute("type", "hidden", null);
		if (sheet.getFocusId() == null) {
			responseWriter.writeAttribute("value", "", null);
		} else {
			responseWriter.writeAttribute("value", sheet.getFocusId(), null);
		}
		responseWriter.endElement("input");

		responseWriter.startElement("input", null);
		responseWriter.writeAttribute("id", clientId + "_selection", "id");
		responseWriter.writeAttribute("name", clientId + "_selection", "name");
		responseWriter.writeAttribute("type", "hidden", null);
		if (sheet.getSelection() == null) {
			responseWriter.writeAttribute("value", "", null);
		} else {
			responseWriter.writeAttribute("value", sheet.getSelection(), null);
		}
		responseWriter.endElement("input");

		// sort col and order if specified and supported
		final int sortCol = sheet.getSortColRenderIndex();
		responseWriter.startElement("input", null);
		responseWriter.writeAttribute("id", clientId + "_sortby", "id");
		responseWriter.writeAttribute("name", clientId + "_sortby", "name");
		responseWriter.writeAttribute("type", "hidden", null);
		responseWriter.writeAttribute("value", sortCol, null);
		responseWriter.endElement("input");

		responseWriter.startElement("input", null);
		responseWriter.writeAttribute("id", clientId + "_sortorder", "id");
		responseWriter.writeAttribute("name", clientId + "_sortorder", "name");
		responseWriter.writeAttribute("type", "hidden", null);
		responseWriter.writeAttribute("value", sheet.getSortOrder().toLowerCase(), null);
		responseWriter.endElement("input");
	}

	/**
	 * Encode client behaviors to widget config
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	private void encodeBehaviors(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		// note we write out the onchange event here so we have the selected
		// cell too
		final Map<String, List<ClientBehavior>> behaviors = sheet.getClientBehaviors();

		wb.append(",behaviors:{");
		final String clientId = sheet.getClientId();

		// sort event (manual since callBack prepends leading comma)
		wb.append("sort").append(":").append("function(s, event)").append("{").append("PrimeFaces.ab({source: '")
				.append(clientId).append("',event: 'sort', process: '").append(clientId).append("', update: '")
				.append(clientId).append("'}, arguments[1]);}");

		// filter
		wb.callback("filter", "function(s, event)", "PrimeFaces.ab({source: '" + clientId
				+ "', event: 'filter', process: '" + clientId + "', update: '" + clientId + "'}, arguments[1]);");

		if (behaviors.containsKey("change")) {
			final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
					sheet, "change", sheet.getClientId(context), null);
			wb.callback("change", "function(source, event)", behaviors.get("change").get(0).getScript(behaviorContext));
		}

		if (behaviors.containsKey("cellSelect")) {
			final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
					sheet, "cellSelect", sheet.getClientId(context), null);
			wb.callback("cellSelect", "function(source, event)",
					behaviors.get("cellSelect").get(0).getScript(behaviorContext));
		}

		if (behaviors.containsKey("columnSelect")) {
			final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
					sheet, "columnSelect", sheet.getClientId(context), null);
			wb.callback("columnSelect", "function(source, event)",
					behaviors.get("columnSelect").get(0).getScript(behaviorContext));
		}

		if (behaviors.containsKey("rowSelect")) {
			final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
					sheet, "rowSelect", sheet.getClientId(context), null);
			wb.callback("rowSelect", "function(source, event)",
					behaviors.get("rowSelect").get(0).getScript(behaviorContext));
		}

		wb.append("}");
	}

	/**
	 * Encode the sheet footer
	 *
	 * @param context
	 * @param responseWriter
	 * @param sheet
	 * @throws IOException
	 */
	private void encodeFooter(final FacesContext context, final ResponseWriter responseWriter, final Sheet sheet)
			throws IOException {
		// footer
		final UIComponent footer = sheet.getFacet("footer");
		if (footer != null) {
			responseWriter.startElement("div", null);
			responseWriter.writeAttribute("class", "ui-datatable-footer ui-widget-header ui-corner-bottom", null);
			footer.encodeAll(context);
			responseWriter.endElement("div");
		}
	}

	/**
	 * Encode the Sheet header
	 *
	 * @param context
	 * @param responseWriter
	 * @param sheet
	 * @throws IOException
	 */
	private void encodeHeader(final FacesContext context, final ResponseWriter responseWriter, final Sheet sheet)
			throws IOException {
		// header
		final UIComponent header = sheet.getFacet("header");
		if (header != null) {
			responseWriter.startElement("div", null);
			responseWriter.writeAttribute("class", "ui-datatable-header ui-widget-header ui-corner-top", null);
			header.encodeAll(context);
			responseWriter.endElement("div");
		}
	}

	/**
	 * Encodes the filter values.
	 *
	 * @param context
	 * @param responseWriter
	 * @param sheet
	 * @throws IOException
	 */
	protected void encodeFilterValues(final FacesContext context, final ResponseWriter responseWriter,
			final Sheet sheet, final String clientId) throws IOException {
		int renderIdx = 0;
		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}

			if (column.getValueExpression("filterBy") != null) {
				responseWriter.startElement("input", null);
				responseWriter.writeAttribute("id", clientId + "_filter_" + renderIdx, "id");
				responseWriter.writeAttribute("name", clientId + "_filter_" + renderIdx, "name");
				responseWriter.writeAttribute("type", "hidden", null);
				responseWriter.writeAttribute("value", column.getFilterValue(), null);
				responseWriter.endElement("input");
			}

			renderIdx++;
		}
	}

	/**
	 * Encodes a javascript filter var that informs the col header event of the
	 * column's filtering options. The var is an array in the form:
	 *
	 * <pre>
	 * ["false","true",["option 1", "option 2"]]
	 * </pre>
	 *
	 * False indicates no filtering for the column. True indicates simple input text
	 * filter. Array of values indicates a drop down filter with the listed options.
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	protected void encodeFilterVar(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, false);

		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}

			if (column.getValueExpression("filterBy") == null) {
				vb.appendArrayValue("false", true);
				continue;
			}

			final Collection<SelectItem> options = column.getFilterOptions();
			if (options == null) {
				vb.appendArrayValue("true", true);
			} else {
				final JavascriptVarBuilder vbOptions = new JavascriptVarBuilder(null, false);
				for (final SelectItem item : options) {
					vbOptions.appendArrayValue(
							"{ label: \"" + item.getLabel() + "\", value: \"" + item.getValue() + "\"}", false);
				}
				vb.appendArrayValue(vbOptions.closeVar().toString(), false);
			}

		}
		wb.nativeAttr("filters", vb.closeVar().toString());
	}

	/**
	 * Encodes a javascript sort var that informs the col header event of the
	 * column's sorting options. The var is an array of boolean indicating whether
	 * or not the column is sortable.
	 *
	 * @param context
	 * @param sheet
	 * @param wb
	 * @throws IOException
	 */
	protected void encodeSortVar(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
			throws IOException {
		final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, false);

		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}

			if (column.getValueExpression("sortBy") == null) {
				vb.appendArrayValue("false", false);
			} else {
				vb.appendArrayValue("true", false);
			}
		}
		wb.nativeAttr("sortable", vb.closeVar().toString());
	}

	/**
	 * Overrides decode and to parse the request parameters for the two hidden input
	 * fields:
	 * <ul>
	 * <li>clientid_input: any new changes provided by the user</li>
	 * <li>clientid_selection: the user's cell selections</li>
	 * </ul>
	 * These are JSON values and are parsed into our submitted values data on the
	 * Sheet component.
	 *
	 * @param context
	 * @parma component
	 */
	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		final Sheet sheet = (Sheet) component;
		// update Sheet references to work around issue with getParent sometimes
		// being null
		for (final SheetColumn column : sheet.getColumns()) {
			column.setSheet(sheet);
		}

		// clear updates from previous decode
		sheet.getUpdates().clear();

		// get parameters
		// we'll need the request parameters
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = sheet.getClientId(context);

		// get our input fields
		final String jsonUpdates = params.get(clientId + "_input");
		final String jsonSelection = params.get(clientId + "_selection");

		// decode into submitted values on the Sheet
		decodeSubmittedValues(context, sheet, jsonUpdates);

		// decode the selected range so we can puke it back
		decodeSelection(context, sheet, jsonSelection);

		// decode client behaviors
		decodeBehaviors(context, sheet);

		// decode filters
		decodeFilters(context, sheet, params, clientId);

		final String sortBy = params.get(clientId + "_sortby");
		final String sortOrder = params.get(clientId + "_sortorder");
		if (sortBy != null) {
			int col = Integer.valueOf(sortBy);
			if (col >= 0) {
				col = sheet.getMappedColumn(col);
				sheet.setSortByValueExpression(sheet.getColumns().get(col).getValueExpression("sortBy"));
			}
		}

		if (sortOrder != null) {
			sheet.setSortOrder(sortOrder);
		}

		final String focus = params.get(clientId + "_focus");
		sheet.setFocusId(focus);
	}

	/**
	 * Decodes the filter values
	 *
	 * @param context
	 * @param sheet
	 * @param params
	 * @param clientId
	 */
	protected void decodeFilters(final FacesContext context, final Sheet sheet, final Map<String, String> params,
			final String clientId) {
		int renderIdx = 0;
		for (final SheetColumn column : sheet.getColumns()) {
			if (!column.isRendered()) {
				continue;
			}

			if (column.getValueExpression("filterBy") != null) {
				final String value = params.get(clientId + "_filter_" + renderIdx);
				column.setFilterValue(value);
			}

			renderIdx++;
		}
	}

	/**
	 * Decodes client behaviors (ajax events).
	 *
	 * @param context   the FacesContext
	 * @param component the Component being decodes
	 */
	@Override
	protected void decodeBehaviors(final FacesContext context, final UIComponent component) {

		// get current behaviors
		final Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();

		// if empty, done
		if (behaviors.isEmpty()) {
			return;
		}

		// get the parameter map and the behaviorEvent fired
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String behaviorEvent = params.get("javax.faces.behavior.event");

		// if no event, done
		if (behaviorEvent == null) {
			return;
		}

		// get behaviors for the event
		final List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);
		if (behaviorsForEvent == null || behaviorsForEvent.isEmpty()) {
			return;
		}

		// decode event if we are the source
		final String behaviorSource = params.get("javax.faces.source");
		final String clientId = component.getClientId();
		if (behaviorSource != null && clientId.equals(behaviorSource)) {
			for (final ClientBehavior behavior : behaviorsForEvent) {
				behavior.decode(context, component);
			}
		}
	}

	/**
	 * Decodes the user Selection JSON data
	 *
	 * @param context
	 * @param sheet
	 * @param jsonSelection
	 */
	private void decodeSelection(final FacesContext context, final Sheet sheet, final String jsonSelection) {
		if (StringUtils.isEmpty(jsonSelection)) {
			return;
		}

		try {
			// data comes in: [ [row, col, oldValue, newValue] ... ]
			final JSONArray array = new JSONArray(jsonSelection);
			sheet.setSelectedRow(array.getInt(0));
			sheet.setSelectedColumn(sheet.getMappedColumn(array.getInt(1)));
			sheet.setSelectedLastRow(array.getInt(2));
			sheet.setSelectedLastColumn(array.getInt(3));
			sheet.setSelection(jsonSelection);
		} catch (final JSONException e) {
			throw new FacesException("Failed parsing Ajax JSON message for cell selection event:" + e.getMessage(), e);
		}
	}

	/**
	 * Converts the JSON data received from the in the request params into our
	 * sumitted values map. The map is cleared first.
	 *
	 * @param jsonData the submitted JSON data
	 * @param sheet
	 * @param jsonData
	 */
	private void decodeSubmittedValues(final FacesContext context, final Sheet sheet, final String jsonData) {
		if (StringUtils.isEmpty(jsonData)) {
			return;
		}

		try {
			// data comes in as a JSON Object with named properties for the row and columns
			// updated this is so that
			// multiple updates to the same cell overwrite previous deltas prior to
			// submission we don't care about
			// the property names, just the values, which we'll process in turn
			final JSONObject obj = new JSONObject(jsonData);
			final Iterator<String> keys = obj.keys();
			while (keys.hasNext()) {
				final String key = keys.next();
				// data comes in: [row, col, oldValue, newValue, rowKey]
				final JSONArray update = obj.getJSONArray(key);
				// GitHub #586 pasted more values than rows
				if (update.isNull(4)) {
					continue;
				}
				final String rowKey = update.getString(4);
				final int col = sheet.getMappedColumn(update.getInt(1));
				final String newValue = String.valueOf(update.get(3));
				sheet.setSubmittedValue(context, rowKey, col, newValue);
			}
		} catch (final JSONException ex) {
			throw new FacesException("Failed parsing Ajax JSON message for cell change event:" + ex.getMessage(), ex);
		}
	}

	/**
	 * We render the columns (the children).
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}

}
