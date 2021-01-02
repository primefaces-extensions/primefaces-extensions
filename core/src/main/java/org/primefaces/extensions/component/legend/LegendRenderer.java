/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.legend;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
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
        final String styleClass = getStyleClassBuilder(context)
                    .add(legend.getLayout().equalsIgnoreCase("vertical")
                                ? Legend.STYLE_CLASS_VERTICAL
                                : Legend.STYLE_CLASS_HORIZONTAL)
                    .add(legend.getStyleClass())
                    .build();

        writer.startElement("div", legend);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (legend.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, legend.getStyle(), Attrs.STYLE);
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
        writer.writeAttribute(Attrs.CLASS, Legend.SCALE_STYLE, null);
        writer.startElement("ul", null);
        writer.writeAttribute(Attrs.CLASS, Legend.LABELS_STYLE, null);

        // Key=text, Value=Color
        final Map<String, String> values = legend.getValues();
        for (final Entry<String, String> item : values.entrySet()) {
            writer.startElement("li", null);
            writer.startElement("span", null);
            writer.writeAttribute(Attrs.STYLE, "background:" + item.getValue(), null);
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
        writer.writeAttribute(Attrs.CLASS, Legend.TITLE_STYLE, null);
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
        writer.writeAttribute(Attrs.CLASS, Legend.FOOTER_STYLE, null);
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
        wb.init("ExtLegend", legend);
        wb.attr("layout", legend.getLayout());
        wb.finish();
    }

}
