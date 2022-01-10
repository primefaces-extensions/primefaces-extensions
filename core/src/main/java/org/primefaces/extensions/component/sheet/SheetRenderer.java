/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.extensions.util.JavascriptVarBuilder;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
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
        encodeScript(context, sheet);
    }

    /**
     * Encodes the HTML markup for the sheet.
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

        responseWriter.writeAttribute(Attrs.CLASS, divclass, "styleClass");
        if (width != null) {
            responseWriter.writeAttribute(Attrs.STYLE, "width: " + width + "px;", null);
        }

        encodeHiddenInputs(context, sheet, clientId);
        encodeFilterValues(responseWriter, sheet, clientId);
        encodeHeader(context, responseWriter, sheet);

        // handsontable div
        responseWriter.startElement("div", null);
        responseWriter.writeAttribute("id", clientId + "_tbl", "id");
        responseWriter.writeAttribute("name", clientId + "_tbl", "clientId");
        responseWriter.writeAttribute(Attrs.CLASS, "handsontable-inner", "styleClass");

        if (style == null) {
            style = Constants.EMPTY_STRING;
        }

        if (width != null) {
            style = style + "width: " + width + "px;";
        }

        if (height != null) {
            style = style + "height: " + height + "px;";
        }
        else {
            style = style + "height: 100%;";
        }

        responseWriter.writeAttribute(Attrs.STYLE, style, null);

        responseWriter.endElement("div");
        encodeFooter(context, responseWriter, sheet);
        responseWriter.endElement("div");
    }

    /**
     * Encodes an optional attribute to the widget builder specified.
     *
     * @param wb the WidgetBuilder to append to
     * @param attrName the attribute name
     * @param value the value
     * @throws IOException if any IO error occurs
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
     * @param wb the WidgetBuilder to append to
     * @param attrName the attribute name
     * @param value the value
     * @throws IOException if any IO error occurs
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
     * @param context the FacesContext
     * @param sheet the Sheet
     * @throws IOException if any IO error occurs
     */
    protected void encodeScript(final FacesContext context, final Sheet sheet)
                throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtSheet", sheet);

        // errors
        encodeInvalidData(sheet, wb);
        // data
        encodeData(context, sheet, wb);

        // the delta var that will be used to track changes client side
        // stringified and placed in hidden input for submission
        wb.nativeAttr("delta", "{}");

        // filters
        encodeFilterVar(sheet, wb);
        // sortable
        encodeSortVar(sheet, wb);
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
        encodeOptionalAttr(wb, "language", sheet.getLocale());
        encodeOptionalAttr(wb, "selectionMode", sheet.getSelectionMode());
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
        if (LangUtils.isBlank(emptyMessage)) {
            emptyMessage = "No Records Found";
        }
        encodeOptionalAttr(wb, "emptyMessage", emptyMessage);

        encodeColHeaders(sheet, wb);
        encodeColOptions(sheet, wb);
        wb.finish();
    }

    /**
     * Encodes the necessary JS to render invalid data.
     *
     * @throws IOException if any IO error occurs
     */
    protected void encodeInvalidData(final Sheet sheet, final WidgetBuilder wb)
                throws IOException {
        wb.attr("errors", sheet.getInvalidDataValue());
    }

    /**
     * Encode the column headers
     *
     * @throws IOException if any IO error occurs
     */
    protected void encodeColHeaders(final Sheet sheet, final WidgetBuilder wb)
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
     * @throws IOException if any IO error occurs
     */
    protected void encodeColOptions(final Sheet sheet, final WidgetBuilder wb)
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
            options.appendProperty("trimWhitespace", column.isTrimWhitespace().toString(), false);
            options.appendProperty("wordWrap", column.isWordWrap().toString(), false);

            // validate can be a function, regex, or string
            final String validateFunction = column.getOnvalidate();
            if (validateFunction != null) {
                final boolean quoted;
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
                    options.appendProperty("strict", Boolean.toString(column.isAutoCompleteStrict()), false);
                    options.appendProperty("allowInvalid", Boolean.toString(column.isAutoCompleteAllowInvalid()),
                                false);
                    options.appendProperty("trimDropdown", Boolean.toString(column.isAutoCompleteTrimDropdown()),
                                false);
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
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection) value;
            for (final Object item : collection) {
                items.appendArrayValue(String.valueOf(item), true);
            }
        }
        else if (value instanceof Map) {
            final Map map = (Map) value;

            for (final Object item : map.keySet()) {
                items.appendArrayValue(String.valueOf(item), true);
            }
        }

        options.appendProperty("source", items.closeVar().toString(), false);
    }

    /**
     * Encode the row data. Builds row data, style data and read only object.
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
            wb.nativeAttr("rowHeaders", Boolean.toString(sheet.isShowRowHeaders()));
        }
        else {
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
                final int rowIndex) {
        // encode rowStyle (if any)
        final String rowStyleClass = sheet.getRowStyleClass();
        if (rowStyleClass == null) {
            jsRowStyle.appendArrayValue("null", false);
        }
        else {
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
     */
    private void encodeHiddenInputs(final FacesContext fc, final Sheet sheet, final String clientId)
                throws IOException {
        renderHiddenInput(fc, clientId + "_input", null, false);
        renderHiddenInput(fc, clientId + "_focus", sheet.getFocusId(), false);
        renderHiddenInput(fc, clientId + "_selection", sheet.getSelection(), false);
        renderHiddenInput(fc, clientId + "_sortby", Integer.toString(sheet.getSortColRenderIndex()), false);
        renderHiddenInput(fc, clientId + "_sortorder", sheet.getSortOrder().toLowerCase(), false);
    }

    /**
     * Encode client behaviors to widget config
     */
    private void encodeBehaviors(final FacesContext context, final Sheet sheet, final WidgetBuilder wb)
                throws IOException {
        // note we write out the onchange event here so we have the selected
        // cell too
        final Map<String, List<ClientBehavior>> behaviors = sheet.getClientBehaviors();

        final List<ClientBehaviorContext.Parameter> params = null;

        wb.append(",behaviors:{");
        final String clientId = sheet.getClientId(context);

        // sort event (manual since callBack prepends leading comma)
        if (behaviors.containsKey("sort")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "sort", clientId, params);
            final AjaxBehavior ajaxBehavior = (AjaxBehavior) behaviors.get("sort").get(0);
            ajaxBehavior.setUpdate(ExtLangUtils.defaultString(ajaxBehavior.getUpdate()) + " " + clientId);
            wb.append("sort").append(":").append("function(s, event)").append("{")
                        .append(behaviors.get("sort").get(0).getScript(behaviorContext)).append("}");
        }
        else {
            // default sort event if none defined by user
            wb.append("sort").append(":").append("function(s, event)").append("{").append("PrimeFaces.ab({source: '")
                        .append(clientId).append("',event: 'sort', process: '").append(clientId).append("', update: '")
                        .append(clientId).append("'});}");
        }

        // filter
        if (behaviors.containsKey("filter")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "filter", clientId, params);
            final AjaxBehavior ajaxBehavior = (AjaxBehavior) behaviors.get("filter").get(0);
            ajaxBehavior.setUpdate(ExtLangUtils.defaultString(ajaxBehavior.getUpdate()) + " " + clientId);
            wb.callback("filter", "function(source, event)", behaviors.get("filter").get(0).getScript(behaviorContext));
        }
        else {
            // default filter event if none defined by user
            wb.callback("filter", "function(source, event)", "PrimeFaces.ab({s: '" + clientId
                        + "', event: 'filter', process: '" + clientId + "', update: '" + clientId + "'});");
        }

        if (behaviors.containsKey("change")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "change", clientId, params);
            wb.callback("change", "function(source, event)", behaviors.get("change").get(0).getScript(behaviorContext));
        }

        if (behaviors.containsKey("cellSelect")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "cellSelect", clientId, params);
            wb.callback("cellSelect", "function(source, event)",
                        behaviors.get("cellSelect").get(0).getScript(behaviorContext));
        }

        if (behaviors.containsKey("columnSelect")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "columnSelect", clientId, params);
            wb.callback("columnSelect", "function(source, event)",
                        behaviors.get("columnSelect").get(0).getScript(behaviorContext));
        }

        if (behaviors.containsKey("rowSelect")) {
            final ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context,
                        sheet, "rowSelect", clientId, params);
            wb.callback("rowSelect", "function(source, event)",
                        behaviors.get("rowSelect").get(0).getScript(behaviorContext));
        }

        wb.append("}");
    }

    /**
     * Encode the sheet footer
     */
    private void encodeFooter(final FacesContext context, final ResponseWriter responseWriter, final Sheet sheet)
                throws IOException {
        // footer
        final UIComponent footer = sheet.getFacet("footer");
        if (ComponentUtils.shouldRenderFacet(footer)) {
            responseWriter.startElement("div", null);
            responseWriter.writeAttribute(Attrs.CLASS, "ui-datatable-footer ui-widget-header ui-corner-bottom", null);
            footer.encodeAll(context);
            responseWriter.endElement("div");
        }
    }

    /**
     * Encode the Sheet header
     */
    private void encodeHeader(final FacesContext context, final ResponseWriter responseWriter, final Sheet sheet)
                throws IOException {
        // header
        final UIComponent header = sheet.getFacet("header");
        if (ComponentUtils.shouldRenderFacet(header)) {
            responseWriter.startElement("div", null);
            responseWriter.writeAttribute(Attrs.CLASS, "ui-datatable-header ui-widget-header ui-corner-top", null);
            header.encodeAll(context);
            responseWriter.endElement("div");
        }
    }

    /**
     * Encodes the filter values.
     */
    protected void encodeFilterValues(final ResponseWriter responseWriter,
                final Sheet sheet, final String clientId) throws IOException {
        int renderCol = 0;
        for (final SheetColumn column : sheet.getColumns()) {
            if (!column.isRendered()) {
                continue;
            }

            if (column.getValueExpression("filterBy") != null) {
                responseWriter.startElement("input", null);
                responseWriter.writeAttribute("id", clientId + "_filter_" + renderCol, "id");
                responseWriter.writeAttribute("name", clientId + "_filter_" + renderCol, "name");
                responseWriter.writeAttribute("type", "hidden", null);
                responseWriter.writeAttribute("value", column.getFilterValue(), null);
                responseWriter.endElement("input");
            }

            renderCol++;
        }
    }

    /**
     * Encodes a javascript filter var that informs the col header event of the column's filtering options. The var is an array in the form:
     * ["false","true",["option 1", "option 2"]] False indicates no filtering for the column. True indicates simple input text filter. Array of values indicates
     * a drop down filter with the listed options.
     */
    protected void encodeFilterVar(final Sheet sheet, final WidgetBuilder wb)
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
            }
            else {
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
     * Encodes a javascript sort var that informs the col header event of the column's sorting options. The var is an array of boolean indicating whether or not
     * the column is sortable.
     */
    protected void encodeSortVar(final Sheet sheet, final WidgetBuilder wb)
                throws IOException {
        final JavascriptVarBuilder vb = new JavascriptVarBuilder(null, false);

        for (final SheetColumn column : sheet.getColumns()) {
            if (!column.isRendered()) {
                continue;
            }

            if (column.getValueExpression("sortBy") == null) {
                vb.appendArrayValue("false", false);
            }
            else {
                vb.appendArrayValue("true", false);
            }
        }
        wb.nativeAttr("sortable", vb.closeVar().toString());
    }

    /**
     * Overrides decode and to parse the request parameters for the two hidden input fields:
     * <ul>
     * <li>clientid_input: any new changes provided by the user</li>
     * <li>clientid_selection: the user's cell selections</li>
     * </ul>
     * These are JSON values and are parsed into our submitted values data on the Sheet component.
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
        decodeSubmittedValues(sheet, jsonUpdates);

        // decode the selected range so we can puke it back
        decodeSelection(sheet, jsonSelection);

        // decode client behaviors
        decodeBehaviors(context, sheet);

        // decode filters
        decodeFilters(sheet, params, clientId);

        final String sortBy = params.get(clientId + "_sortby");
        final String sortOrder = params.get(clientId + "_sortorder");
        if (sortBy != null) {
            int col = Integer.parseInt(sortBy);
            if (col >= 0) {
                col = sheet.getMappedColumn(col);
                sheet.saveSortByColumn(sheet.getColumns().get(col).getId());
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
     */
    protected void decodeFilters(final Sheet sheet, final Map<String, String> params,
                final String clientId) {
        int renderCol = 0;
        for (final SheetColumn column : sheet.getColumns()) {
            if (!column.isRendered()) {
                continue;
            }

            if (column.getValueExpression("filterBy") != null) {
                final String value = params.get(clientId + "_filter_" + renderCol);
                column.setFilterValue(value);
            }

            renderCol++;
        }
    }

    /**
     * Decodes client behaviors (ajax events).
     *
     * @param context the FacesContext
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
        if (clientId.equals(behaviorSource)) {
            for (final ClientBehavior behavior : behaviorsForEvent) {
                behavior.decode(context, component);
            }
        }
    }

    /**
     * Decodes the user Selection JSON data
     */
    private void decodeSelection(final Sheet sheet, final String jsonSelection) {
        if (LangUtils.isBlank(jsonSelection)) {
            return;
        }

        try {
            // data comes in array of arrays
            final JSONArray arrays = new JSONArray(jsonSelection);
            for (int i = 0; i < arrays.length(); i++) {
                final JSONArray array = arrays.getJSONArray(i);
                // data comes in: [ [fromrow, fromcol, torow, tocol] ... ]
                sheet.setSelectedRow(array.getInt(0));
                sheet.setSelectedColumn(sheet.getMappedColumn(array.getInt(1)));
                sheet.setSelectedLastRow(array.getInt(2));
                sheet.setSelectedLastColumn(array.getInt(3));
            }
            sheet.setSelection(jsonSelection);
        }
        catch (final JSONException e) {
            throw new FacesException("Failed parsing Ajax JSON message for cell selection event:" + e.getMessage(),
                        e);
        }
    }

    /**
     * Converts the JSON data received from the in the request params into our submitted values map. The map is cleared first.
     */
    private void decodeSubmittedValues(final Sheet sheet, final String jsonData) {
        if (LangUtils.isBlank(jsonData)) {
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
                sheet.setSubmittedValue(rowKey, col, newValue);
            }
        }
        catch (final JSONException ex) {
            throw new FacesException("Failed parsing Ajax JSON message for cell change event:" + ex.getMessage(),
                        ex);
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
