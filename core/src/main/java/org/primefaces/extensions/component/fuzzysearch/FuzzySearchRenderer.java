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
package org.primefaces.extensions.component.fuzzysearch;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.model.SelectItem;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>FuzzySearch</code> component.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
@FacesRenderer(rendererType = FuzzySearch.DEFAULT_RENDERER, componentFamily = FuzzySearch.COMPONENT_FAMILY)
public class FuzzySearchRenderer extends SelectOneRenderer<FuzzySearch> {

    private static final String INPUT = "_input";

    @Override
    public void decode(final FacesContext context, final FuzzySearch component) {
        if (!shouldDecode(component)) {
            return;
        }

        final String clientId = component.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String submittedValue = params.get(clientId + INPUT);

        if (submittedValue != null) {
            component.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final FuzzySearch component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final FuzzySearch component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final String clientId = component.getClientId(context);
        final List<SelectItem> selectItems = getSelectItems(context, component);
        final int selectItemsSize = selectItems.size();
        final String style = component.getStyle();
        final String styleClass = getStyleClassBuilder(context)
                    .add(FuzzySearch.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .add("ui-buttonset-" + selectItemsSize)
                    .add(!component.isValid(), "ui-state-error")
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute(Attrs.STYLE, style, Attrs.STYLE);
        }

        writer.startElement("input", component);
        writer.writeAttribute("id", clientId + INPUT, null);
        writer.writeAttribute("name", clientId + INPUT, null);
        writer.writeAttribute("placeholder", component.getPlaceholder(), null);
        writer.writeAttribute("class", createStyleClass(component, InputText.STYLE_CLASS), "styleClass");
        renderPassThruAttributes(context, component, HTML.TAB_INDEX);
        renderDomEvents(context, component, HTML.BLUR_FOCUS_EVENTS);
        renderAccessibilityAttributes(context, component);
        renderValidationMetadata(context, component);
        final String valueToRender = ComponentUtils.getValueToRender(context, component);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }
        writer.endElement("input");

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId + "_fuzzysearch-search-results", null);

        if (component.isListItemsAtTheBeginning()) {
            encodeSelectItems(context, component, selectItems);
        }

        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeSelectItems(final FacesContext context, final FuzzySearch component, final List<SelectItem> selectItems) throws IOException {
        for (final SelectItem selectItem : selectItems) {
            encodeOption(context, component, selectItem);
        }
    }

    protected void encodeOption(
                final FacesContext context, final FuzzySearch component, final SelectItem option) throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, component, component.getConverter(), option.getValue());

        final String resultStyle = component.getResultStyle();
        final String resultStyleClass = getStyleClassBuilder(context)
                    .add(FuzzySearch.ITEM_CLASS)
                    .add(component.getResultStyleClass())
                    .build();

        // results
        writer.startElement("div", null);
        writer.writeAttribute("class", resultStyleClass, "resultStyleClass");
        if (resultStyle != null) {
            writer.writeAttribute(Attrs.STYLE, resultStyle, "resultStyle");
        }
        writer.writeAttribute("tabindex", component.getTabindex(), null);
        if (option.getDescription() != null) {
            writer.writeAttribute("title", option.getDescription(), null);
        }
        writer.writeAttribute("data-item-value", itemValueAsString, null);

        if (option.isEscape()) {
            writer.writeText(option.getLabel(), "itemLabel");
        }
        else {
            writer.write(option.getLabel());
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final FuzzySearch component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final List<SelectItem> selectItems = getSelectItems(context, component);
        final JSONArray ja = new JSONArray();
        for (final SelectItem selectItem : selectItems) {
            final JSONObject jo = new JSONObject();
            jo.put("label", selectItem.getLabel());
            jo.put("value", getOptionAsString(context, component, component.getConverter(), selectItem.getValue()));
            jo.put("escape", selectItem.isEscape());
            if (selectItem.getDescription() != null) {
                jo.put("description", selectItem.getDescription());
            }
            ja.put(jo);
        }
        final String jsonDatasource = ja.toString();

        wb.init(FuzzySearch.class.getSimpleName(), component)
                    .attr("resultStyle", component.getResultStyle())
                    .attr("resultStyleClass", component.getResultStyleClass())
                    .attr("listItemsAtTheBeginning", component.isListItemsAtTheBeginning())
                    .attr("datasource", jsonDatasource)
                    .attr("unselectable", component.isUnselectable(), true)
                    .attr("highlight", component.isHighlight(), true)
                    .callback("change", "function()", component.getOnchange());

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final FuzzySearch component) {
        return component.getClientId(context);
    }

}
