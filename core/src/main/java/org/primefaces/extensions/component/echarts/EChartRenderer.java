/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.component.echarts;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.WidgetBuilder;

public class EChartRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        EChart chart = (EChart) component;

        encodeMarkup(context, chart);
        encodeScript(context, chart);
    }

    protected void encodeMarkup(FacesContext context, EChart chart) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = chart.getClientId(context);
        String style = chart.getStyle();
        String styleClass = getStyleClassBuilder(context).add("ui-chart").add(chart.getStyleClass()).build();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");

        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        else {
            writer.writeAttribute("style", "width: 600px; height: 400px;", "style");
        }
        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, EChart chart) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtEChart", chart)
                    .attr("theme", chart.getTheme(), "default")
                    .nativeAttr("config", renderConfig(context, chart))
                    .nativeAttr("extender", chart.getExtender());

        encodeClientBehaviors(context, chart);

        wb.finish();
    }

    /**
     * Allow value to be a property or a facet of raw JSON.
     */
    protected String renderConfig(FacesContext context, EChart chart) throws IOException {
        UIComponent facet = chart.getFacet("value");
        if (FacetUtils.shouldRenderFacet(facet)) {
            // swap writers
            ResponseWriter originalWriter = context.getResponseWriter();
            FastStringWriter fsw = new FastStringWriter();
            ResponseWriter clonedWriter = originalWriter.cloneWithWriter(fsw);
            context.setResponseWriter(clonedWriter);

            // encode the component
            facet.encodeAll(context);

            // restore the original writer
            context.setResponseWriter(originalWriter);
            return fsw.toString();
        }
        else {
            return chart.getValue();
        }
    }

}