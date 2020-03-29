/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(componentFamily = FuzzySearch.COMPONENT_FAMILY, rendererType = FuzzySearch.DEFAULT_RENDERER)
public class FuzzySearchRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        FuzzySearch fuzzySearch = (FuzzySearch) component;

        encodeMarkup(context, fuzzySearch);
        encodeScript(context, fuzzySearch);
    }

    private void encodeScript(FacesContext context, FuzzySearch fuzzySearch) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);

        // TODO is it possible to make improvement here to have @FuzzySearchKey annotated class?
        List list = (List) fuzzySearch.getValue();
        Object o = list.get(0);
        Class<? extends Object> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> keys = Arrays.asList(fields).stream()
                .filter(f -> f.getAnnotation(FuzzySearchKey.class) != null)
                .map(Field::getName)
                .collect(Collectors.toList());
        if (keys.isEmpty()) {
            throw new FacesException("No @FuzzyKey annotation was detected on your class " + clazz + "."
                                     + " Please annotate at least one field in the class as @FuzzySearchKey.");
        }

        Gson gson = new Gson();
        String jsonKeys = gson.toJson(keys);
        String jsonValue = gson.toJson(fuzzySearch.getValue());

        wb.init(FuzzySearch.class.getSimpleName(), fuzzySearch.resolveWidgetVar(), fuzzySearch.getClientId(context))
                .attr("keys", jsonKeys)
                .attr("value", jsonValue);

        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, FuzzySearch fuzzySearch) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String clientId = fuzzySearch.getClientId(context);
        String style = fuzzySearch.getStyle();
        String styleClass = fuzzySearch.getStyleClass();
        styleClass = (styleClass == null) ? FuzzySearch.CONTAINER_CLASS : FuzzySearch.CONTAINER_CLASS + " " + styleClass;
        String resultStyle = fuzzySearch.getResultStyle();
        String resultStyleClass = fuzzySearch.getResultStyleClass();
        resultStyleClass = (resultStyleClass == null) ? FuzzySearch.ITEM_CLASS : FuzzySearch.ITEM_CLASS + " " + resultStyleClass;

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("data-result-style", fuzzySearch.getResultStyle(), null);
        writer.writeAttribute("data-result-style-class", fuzzySearch.getResultStyleClass(), null);

        writer.startElement("input", fuzzySearch);
        writer.writeAttribute("id", clientId + "_fuzzysearch-search-input", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("placeholder", "Search", null);
        writer.endElement("input");

        writer.startElement("div", fuzzySearch);
        writer.writeAttribute("id", clientId + "_fuzzysearch-search-results", null);

        if (fuzzySearch.getVar() != null) {
            for (int i = 0; i < fuzzySearch.getRowCount(); i++) {
                fuzzySearch.setRowIndex(i);

                writer.startElement("div", fuzzySearch);
                writer.writeAttribute("class", resultStyleClass, null);
                if (resultStyle != null) {
                    writer.writeAttribute("style", resultStyle, null);
                }

                renderChildren(context, fuzzySearch);

                writer.endElement("div");
            }

            fuzzySearch.setRowIndex(-1);
        }
        else {
            for (UIComponent kid : fuzzySearch.getChildren()) {
                if (kid.isRendered()) {
                    writer.startElement("div", fuzzySearch);
                    writer.writeAttribute("class", resultStyleClass, null);
                    if (resultStyle != null) {
                        writer.writeAttribute("style", resultStyle, null);
                    }

                    renderChild(context, kid);

                    writer.endElement("div");
                }
            }
        }

        writer.endElement("div");

        writer.endElement("div");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
