/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

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
public class FuzzySearchRenderer extends SelectOneRenderer {

    private static final String INPUT = "_input";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final FuzzySearch fuzzySearch = (FuzzySearch) component;
        if (!shouldDecode(fuzzySearch)) {
            return;
        }

        final String clientId = fuzzySearch.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String submittedValue = params.get(clientId + INPUT);

        if (submittedValue != null) {
            fuzzySearch.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, fuzzySearch);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final FuzzySearch fuzzySearch = (FuzzySearch) component;

        encodeMarkup(context, fuzzySearch);
        encodeScript(context, fuzzySearch);
    }

    protected void encodeMarkup(final FacesContext context, final FuzzySearch fuzzySearch) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final String clientId = fuzzySearch.getClientId(context);
        final List<SelectItem> selectItems = getSelectItems(context, fuzzySearch);
        final int selectItemsSize = selectItems.size();
        final String style = fuzzySearch.getStyle();
        final String styleClass = getStyleClassBuilder(context)
                    .add(FuzzySearch.STYLE_CLASS)
                    .add(fuzzySearch.getStyleClass())
                    .add("ui-buttonset-" + selectItemsSize)
                    .add(!fuzzySearch.isValid(), "ui-state-error")
                    .build();

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute(Attrs.STYLE, style, Attrs.STYLE);
        }

        writer.startElement("input", fuzzySearch);
        writer.writeAttribute("id", clientId + INPUT, null);
        writer.writeAttribute("name", clientId + INPUT, null);
        writer.writeAttribute("placeholder", fuzzySearch.getPlaceholder(), null);
        writer.writeAttribute("class", createStyleClass(fuzzySearch, InputText.STYLE_CLASS), "styleClass");
        renderPassThruAttributes(context, fuzzySearch, HTML.TAB_INDEX);
        renderDomEvents(context, fuzzySearch, HTML.BLUR_FOCUS_EVENTS);
        renderAccessibilityAttributes(context, fuzzySearch);
        renderValidationMetadata(context, fuzzySearch);
        final String valueToRender = ComponentUtils.getValueToRender(context, fuzzySearch);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }
        writer.endElement("input");

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId + "_fuzzysearch-search-results", null);

        if (fuzzySearch.isListItemsAtTheBeginning()) {
            encodeSelectItems(context, fuzzySearch, selectItems);
        }

        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeSelectItems(final FacesContext context, final FuzzySearch fuzzySearch, final List<SelectItem> selectItems) throws IOException {
        for (final SelectItem selectItem : selectItems) {
            encodeOption(context, fuzzySearch, selectItem);
        }
    }

    protected void encodeOption(
                final FacesContext context, final FuzzySearch fuzzySearch, final SelectItem option) throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, fuzzySearch, fuzzySearch.getConverter(), option.getValue());

        final String resultStyle = fuzzySearch.getResultStyle();
        final String resultStyleClass = getStyleClassBuilder(context)
                    .add(FuzzySearch.ITEM_CLASS)
                    .add(fuzzySearch.getResultStyleClass())
                    .build();

        // results
        writer.startElement("div", null);
        writer.writeAttribute("class", resultStyleClass, "resultStyleClass");
        if (resultStyle != null) {
            writer.writeAttribute(Attrs.STYLE, resultStyle, "resultStyle");
        }
        writer.writeAttribute("tabindex", fuzzySearch.getTabindex(), null);
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

    protected void encodeScript(final FacesContext context, final FuzzySearch fuzzySearch) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final List<SelectItem> selectItems = getSelectItems(context, fuzzySearch);
        final JSONArray ja = new JSONArray();
        for (final SelectItem selectItem : selectItems) {
            ja.put(new JSONObject(selectItem));
        }
        final String jsonDatasource = ja.toString();

        wb.init(FuzzySearch.class.getSimpleName(), fuzzySearch)
                    .attr("resultStyle", fuzzySearch.getResultStyle())
                    .attr("resultStyleClass", fuzzySearch.getResultStyleClass())
                    .attr("listItemsAtTheBeginning", fuzzySearch.isListItemsAtTheBeginning())
                    .attr("datasource", jsonDatasource)
                    .attr("unselectable", fuzzySearch.isUnselectable(), true)
                    .attr("highlight", fuzzySearch.isHighlight(), true)
                    .callback("change", "function()", fuzzySearch.getOnchange());

        encodeClientBehaviors(context, fuzzySearch);

        wb.finish();
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }

}
