/**
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

import com.google.gson.Gson;
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
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(componentFamily = FuzzySearch.COMPONENT_FAMILY, rendererType = FuzzySearch.DEFAULT_RENDERER)
public class FuzzySearchRenderer extends SelectOneRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        FuzzySearch fuzzySearch = (FuzzySearch) component;
        if (!shouldDecode(fuzzySearch)) {
            return;
        }

        String clientId = getSubmitParam(context, fuzzySearch);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String editorInput = params.get(clientId + "_change");
        fuzzySearch.setSubmittedValue(editorInput);

        decodeBehaviors(context, fuzzySearch);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        FuzzySearch fuzzySearch = (FuzzySearch) component;

        encodeMarkup(context, fuzzySearch);
        encodeScript(context, fuzzySearch);
    }

    protected void encodeMarkup(FacesContext context, FuzzySearch fuzzySearch) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String clientId = fuzzySearch.getClientId(context);
        List<SelectItem> selectItems = getSelectItems(context, fuzzySearch);
        int selectItemsSize = selectItems.size();
        String style = fuzzySearch.getStyle();
        String styleClass = fuzzySearch.getStyleClass();
        styleClass = (styleClass == null) ? FuzzySearch.STYLE_CLASS : FuzzySearch.STYLE_CLASS + " " + styleClass;
        styleClass = styleClass + " ui-buttonset-" + selectItemsSize;
        styleClass = !fuzzySearch.isValid() ? styleClass + " ui-state-error" : styleClass;

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
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

    protected void encodeSelectItems(FacesContext context, FuzzySearch fuzzySearch, List<SelectItem> selectItems) throws IOException {
        int selectItemsSize = selectItems.size();
        Converter converter = fuzzySearch.getConverter();
        String name = fuzzySearch.getClientId(context);
        Object value = fuzzySearch.getSubmittedValue();
        if (value == null) {
            value = fuzzySearch.getValue();
        }

        Class type = value == null ? String.class : value.getClass();

        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);
            boolean disabled = selectItem.isDisabled() || fuzzySearch.isDisabled();
            String id = name + UINamingContainer.getSeparatorChar(context) + i;

            boolean selected;
            if (value == null && selectItem.getValue() == null) {
                selected = true;
            }
            else {
                Object coercedItemValue = coerceToModelType(context, selectItem.getValue(), type);
                selected = (coercedItemValue != null) && coercedItemValue.equals(value);
            }

            encodeOption(context, fuzzySearch, selectItem, id, name, converter, selected, disabled, i, selectItemsSize);
        }
    }

    protected void encodeOption(FacesContext context, FuzzySearch fuzzySearch, SelectItem option, String id, String name, Converter converter,
                                boolean selected, boolean disabled, int idx, int size) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        String itemValueAsString = getOptionAsString(context, fuzzySearch, converter, option.getValue());

        String resultStyle = fuzzySearch.getResultStyle();
        String resultStyleClass = fuzzySearch.getResultStyleClass();
        resultStyleClass = (resultStyleClass == null) ? FuzzySearch.ITEM_CLASS : FuzzySearch.ITEM_CLASS + " " + resultStyleClass;

        String buttonStyle = HTML.BUTTON_TEXT_ONLY_BUTTON_FLAT_CLASS;
        if (size == 1) {
            buttonStyle = buttonStyle + " ui-corner-all";
        }
        else if (idx == 0) {
            buttonStyle = buttonStyle + " ui-corner-left";
        }
        else if (idx == (size - 1)) {
            buttonStyle = buttonStyle + " ui-corner-right";
        }

        buttonStyle = selected ? buttonStyle + " ui-state-active" : buttonStyle;
        buttonStyle = disabled ? buttonStyle + " ui-state-disabled" : buttonStyle;

        //button
        writer.startElement("div", null);
        writer.writeAttribute("class", resultStyleClass, "resultStyleClass");
        if (resultStyle != null) {
            writer.writeAttribute("style", resultStyle, "resultStyle");
        }
        writer.writeAttribute("tabindex", fuzzySearch.getTabindex(), null);
        if (option.getDescription() != null) {
            writer.writeAttribute("title", option.getDescription(), null);
        }
        writer.writeAttribute("data-item-value", itemValueAsString, null);
//        writer.writeAttribute("data-item-value", option.getValue(), null);
        if (option.isEscape()) {
            writer.writeText(option.getLabel(), "itemLabel");
        }
        else {
            writer.write(option.getLabel());
        }

        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, FuzzySearch fuzzySearch) throws IOException {
        String clientId = fuzzySearch.getClientId(context);
        WidgetBuilder wb = getWidgetBuilder(context);

        List<SelectItem> selectItems = getSelectItems(context, fuzzySearch);
        Gson gson = new Gson();
        String jsonDatasource = gson.toJson(selectItems);

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
    protected String getSubmitParam(FacesContext context, UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }

}
