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

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final FuzzySearch fuzzySearch = (FuzzySearch) component;
        if (!shouldDecode(fuzzySearch)) {
            return;
        }

        final String clientId = fuzzySearch.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String submittedValue = params.get(clientId + "_input");

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
        String styleClass = fuzzySearch.getStyleClass();
        styleClass = (styleClass == null) ? FuzzySearch.STYLE_CLASS : FuzzySearch.STYLE_CLASS + " " + styleClass;
        styleClass = styleClass + " ui-buttonset-" + selectItemsSize;
        styleClass = !fuzzySearch.isValid() ? styleClass + " ui-state-error" : styleClass;

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute(Attrs.STYLE, style, Attrs.STYLE);
        }

        writer.startElement("input", fuzzySearch);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("name", clientId + "_input", null);
        writer.writeAttribute("placeholder", fuzzySearch.getPlaceholder(), null);
        writer.writeAttribute("class", createStyleClass(fuzzySearch), "styleClass");
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
        String resultStyleClass = fuzzySearch.getResultStyleClass();
        resultStyleClass = (resultStyleClass == null) ? FuzzySearch.ITEM_CLASS : FuzzySearch.ITEM_CLASS + " " + resultStyleClass;

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
        final String clientId = fuzzySearch.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);

        final List<SelectItem> selectItems = getSelectItems(context, fuzzySearch);
        final String jsonDatasource = new JSONArray(selectItems).toString();

        wb.init(FuzzySearch.class.getSimpleName(), fuzzySearch.resolveWidgetVar(context), clientId)
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

    protected String createStyleClass(final FuzzySearch inputText) {
        String defaultClass = InputText.STYLE_CLASS;
        defaultClass = inputText.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputText.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

        String styleClass = inputText.getStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

        return styleClass;
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }

}
