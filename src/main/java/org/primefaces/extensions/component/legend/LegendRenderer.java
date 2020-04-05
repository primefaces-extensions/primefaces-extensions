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
package org.primefaces.extensions.component.legend;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Legend} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
public class LegendRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Legend legend = (Legend) component;
        encodeMarkup(context, legend);
        encodeScript(context, legend);
    }

    private void encodeMarkup(FacesContext context, Legend legend) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = legend.getClientId(context);
        final String widgetVar = legend.resolveWidgetVar();
        final String styleClass = legend.getLayout().equalsIgnoreCase("vertical") ? Legend.STYLE_CLASS_VERTICAL
                    : Legend.STYLE_CLASS_HORIZONTAL + StringUtils.defaultString(legend.getStyleClass());

        writer.startElement("div", legend);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (legend.getStyle() != null) {
            writer.writeAttribute("style", legend.getStyle(), "style");
        }

        // title
        encodeTitle(context, legend);

        // items
        encodeItems(context, legend);

        // footer
        encodeFooter(context, legend);

        writer.endElement("div");
    }

    private void encodeItems(FacesContext context, Legend legend) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        // scales
        writer.startElement("div", null);
        writer.writeAttribute("class", Legend.SCALE_STYLE, null);
        writer.startElement("ul", null);
        writer.writeAttribute("class", Legend.LABELS_STYLE, null);

        // Key=text, Value=Color
        final Map<String, String> values = legend.getValues();
        for (final Entry<String, String> item : values.entrySet()) {
            writer.startElement("li", null);
            writer.startElement("span", null);
            writer.writeAttribute("style", "background:" + item.getValue(), null);
            writer.endElement("span");
            writer.writeText(item.getKey(), null);
            writer.endElement("li");
        }
        writer.endElement("ul");
        writer.endElement("div");
    }

    private void encodeTitle(FacesContext context, Legend legend) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent facet = legend.getFacet("title");
        final String title = legend.getTitle();
        writer.startElement("div", null);
        writer.writeAttribute("class", Legend.TITLE_STYLE, null);
        if (ComponentUtils.shouldRenderFacet(facet)) {
            facet.encodeAll(context);
        }
        else if (title != null) {
            writer.writeText(title, null);
        }
        else {
            writer.write("&nbsp;");
        }
        writer.endElement("div");
    }

    private void encodeFooter(FacesContext context, Legend legend) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent facet = legend.getFacet("footer");
        final String footer = legend.getFooter();
        writer.startElement("div", null);
        writer.writeAttribute("class", Legend.FOOTER_STYLE, null);
        if (ComponentUtils.shouldRenderFacet(facet)) {
            facet.encodeAll(context);
        }
        else if (footer != null) {
            writer.writeText(footer, null);
        }
        else {
            writer.write("&nbsp;");
        }
        writer.endElement("div");
    }

    private void encodeScript(FacesContext context, Legend legend) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtLegend", legend.resolveWidgetVar(), legend.getClientId(context));
        wb.attr("layout", legend.getLayout());
        wb.finish();
    }

}
