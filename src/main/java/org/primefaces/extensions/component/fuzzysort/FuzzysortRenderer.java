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
package org.primefaces.extensions.component.fuzzysort;

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

@FacesRenderer(componentFamily = Fuzzysort.COMPONENT_FAMILY, rendererType = Fuzzysort.DEFAULT_RENDERER)
public class FuzzysortRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        Fuzzysort fuzzysort = (Fuzzysort) component;

        encodeMarkup(context, fuzzysort);
        encodeScript(context, fuzzysort);
    }

    private void encodeScript(FacesContext context, Fuzzysort fuzzysort) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);

        // TODO is it possible to make improvement here to have @FuzzysortKey annotated class?
        List list = (List) fuzzysort.getValue();
        Object o = list.get(0);
        Class<? extends Object> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> keys = Arrays.asList(fields).stream()
                .filter(f -> f.getAnnotation(FuzzysortKey.class) != null)
                .map(Field::getName)
                .collect(Collectors.toList());
        if (keys.isEmpty()) {
            throw new FacesException("No @FuzzyKey annotation was detected on your class " + clazz + "."
                                     + " Please annotate at least one field in the class as @FuzzysortKey.");
        }

        Gson gson = new Gson();
        String jsonKeys = gson.toJson(keys);
        String jsonValue = gson.toJson(fuzzysort.getValue());

        wb.init(Fuzzysort.class.getSimpleName(), fuzzysort.resolveWidgetVar(), fuzzysort.getClientId(context))
                .attr("keys", jsonKeys)
                .attr("value", jsonValue);

        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, Fuzzysort fuzzysort) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String clientId = fuzzysort.getClientId(context);
        String style = fuzzysort.getStyle();
        String styleClass = fuzzysort.getStyleClass();
        styleClass = (styleClass == null) ? Fuzzysort.CONTAINER_CLASS : Fuzzysort.CONTAINER_CLASS + " " + styleClass;

        writer.startElement("div", fuzzysort);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        writer.startElement("input", fuzzysort);
        writer.writeAttribute("id", clientId + "_fuzzysort-search-box", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("placeholder", "Search", null);
        writer.endElement("input");

        writer.startElement("div", fuzzysort);
        writer.writeAttribute("id", clientId + "_fuzzysort-search-results", null);

        if (fuzzysort.getVar() != null) {
            for (int i = 0; i < fuzzysort.getRowCount(); i++) {
                fuzzysort.setRowIndex(i);

                writer.startElement("div", fuzzysort);

                renderChildren(context, fuzzysort);

                writer.endElement("div");
            }

            fuzzysort.setRowIndex(-1);
        }
        else {
            for (UIComponent kid : fuzzysort.getChildren()) {
                if (kid.isRendered()) {
                    writer.startElement("div", fuzzysort);

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
