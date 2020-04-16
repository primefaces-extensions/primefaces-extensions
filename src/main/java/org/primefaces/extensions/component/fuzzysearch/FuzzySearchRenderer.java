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
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;

import com.google.gson.Gson;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(componentFamily = FuzzySearchBase.COMPONENT_FAMILY, rendererType = FuzzySearchBase.DEFAULT_RENDERER)
public class FuzzySearchRenderer extends SelectOneRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final FuzzySearch fuzzySearch = (FuzzySearch) component;
        if (!shouldDecode(fuzzySearch)) {
            return;
        }

        final String clientId = getSubmitParam(context, fuzzySearch);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        final String editorInput = params.get(clientId + "_change");
        fuzzySearch.setSubmittedValue(editorInput);

        decodeBehaviors(context, fuzzySearch);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

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
        writer.writeAttribute("id", clientId + "_fuzzysearch-search-input", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("placeholder", fuzzySearch.getPlaceholder(), null);
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
        final int selectItemsSize = selectItems.size();
        final Converter converter = fuzzySearch.getConverter();
        final String name = fuzzySearch.getClientId(context);
        Object value = fuzzySearch.getSubmittedValue();
        if (value == null) {
            value = fuzzySearch.getValue();
        }

        final Class type = value == null ? String.class : value.getClass();

        for (int i = 0; i < selectItems.size(); i++) {
            final SelectItem selectItem = selectItems.get(i);
            final boolean disabled = selectItem.isDisabled() || fuzzySearch.isDisabled();
            final String id = name + UINamingContainer.getSeparatorChar(context) + i;

            final boolean selected;
            if (value == null && selectItem.getValue() == null) {
                selected = true;
            }
            else {
                final Object coercedItemValue = coerceToModelType(context, selectItem.getValue(), type);
                selected = (coercedItemValue != null) && coercedItemValue.equals(value);
            }

            encodeOption(context, fuzzySearch, selectItem, id, name, converter, selected, disabled, i, selectItemsSize);
        }
    }

    protected void encodeOption(
                final FacesContext context, final FuzzySearch fuzzySearch, final SelectItem option, final String id, final String name,
                final Converter converter,
                final boolean selected, final boolean disabled, final int idx, final int size) throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, fuzzySearch, converter, option.getValue());

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
        final Gson gson = new Gson();
        final String jsonDatasource = gson.toJson(selectItems);

        wb.init(FuzzySearch.class.getSimpleName(), fuzzySearch.resolveWidgetVar(context), clientId)
                    .attr("resultStyle", fuzzySearch.getResultStyle())
                    .attr("resultStyleClass", fuzzySearch.getResultStyleClass())
                    .attr("listItemsAtTheBeginning", fuzzySearch.isListItemsAtTheBeginning())
                    .attr("datasource", jsonDatasource)
                    .attr("unselectable", fuzzySearch.isUnselectable(), true)
                    .callback("change", "function()", fuzzySearch.getOnchange());

        encodeClientBehaviors(context, fuzzySearch);

        wb.finish();
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }

}
