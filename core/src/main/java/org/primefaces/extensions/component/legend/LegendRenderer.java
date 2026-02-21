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
package org.primefaces.extensions.component.legend;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Legend} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@FacesRenderer(rendererType = Legend.DEFAULT_RENDERER, componentFamily = Legend.COMPONENT_FAMILY)
public class LegendRenderer extends CoreRenderer<Legend> {

    @Override
    public void encodeEnd(final FacesContext context, final Legend component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    private void encodeMarkup(FacesContext context, Legend component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String widgetVar = component.resolveWidgetVar();
        final String styleClass = getStyleClassBuilder(context)
                    .add(component.getLayout().equalsIgnoreCase("vertical")
                                ? Legend.STYLE_CLASS_VERTICAL
                                : Legend.STYLE_CLASS_HORIZONTAL)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        // title
        encodeTitle(context, component);

        // items
        encodeItems(context, component);

        // footer
        encodeFooter(context, component);

        writer.endElement("div");
    }

    private void encodeItems(FacesContext context, Legend component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        // scales
        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, Legend.SCALE_STYLE, null);
        writer.startElement("ul", null);
        writer.writeAttribute(Attrs.CLASS, Legend.LABELS_STYLE, null);

        // Key=text, Value=Color
        final Map<String, String> values = component.getValues();
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

    private void encodeTitle(FacesContext context, Legend component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent facet = component.getTitleFacet();
        final String title = component.getTitle();
        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, Legend.TITLE_STYLE, null);
        if (FacetUtils.shouldRenderFacet(facet)) {
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

    private void encodeFooter(FacesContext context, Legend component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent facet = component.getFooterFacet();
        final String footer = component.getFooter();
        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, Legend.FOOTER_STYLE, null);
        if (FacetUtils.shouldRenderFacet(facet)) {
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

    private void encodeScript(FacesContext context, Legend component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtLegend", component);
        wb.attr("layout", component.getLayout());
        wb.finish();
    }

}