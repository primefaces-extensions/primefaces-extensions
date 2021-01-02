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
package org.primefaces.extensions.component.gchart;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

public class GChartRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        super.decode(context, component);
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final GChart gChart = (GChart) component;

        encodeMarkup(context, gChart);
        encodeScript(context, gChart);
    }

    protected void encodeMarkup(final FacesContext context, final GChart chart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", chart);
        writer.writeAttribute("id", chart.getClientId() + "_hidden", null);
        writer.writeAttribute("name", chart.getClientId() + "_hidden", null);
        writer.writeAttribute("type", "hidden", null);
        writer.endElement("input");

        writer.startElement("div", chart);
        writer.writeAttribute("id", chart.getClientId(), null);
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final GChart chart) throws IOException {
        String apiKey = chart.getApiKey();
        if (LangUtils.isValueBlank(apiKey)) {
            apiKey = getApiKey(context);
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtGChart", chart)
                    .attr("chart", ((GChartModel) chart.getValue()).toJson())
                    .attr("title", chart.getTitle())
                    .attr("apiKey", apiKey)
                    .attr("language", chart.getLanguage())
                    .attr("width", chart.getWidth())
                    .attr("height", chart.getHeight());
        if (chart.getExtender() != null) {
            wb.nativeAttr("extender", chart.getExtender());
        }
        encodeClientBehaviors(context, chart);

        wb.finish();
    }

    protected String getApiKey(final FacesContext context) {
        String key;
        try {
            final String initParam = context.getExternalContext().getInitParameter(GChart.API_KEY);
            key = context.getApplication().evaluateExpressionGet(context, initParam, String.class);
        }
        catch (final Exception e) {
            key = null;
        }
        return key;
    }
}