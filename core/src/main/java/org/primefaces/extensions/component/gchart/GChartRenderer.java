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
package org.primefaces.extensions.component.gchart;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(rendererType = GChart.DEFAULT_RENDERER, componentFamily = GChart.COMPONENT_FAMILY)
public class GChartRenderer extends CoreRenderer<GChart> {

    @Override
    public void decode(final FacesContext context, final GChart component) {
        super.decode(context, component);
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final GChart component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final GChart component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        renderHiddenInput(context, component.getClientId() + "_hidden", null, false);

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), null);
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final GChart component) throws IOException {
        String apiKey = component.getApiKey();
        if (LangUtils.isBlank(apiKey)) {
            apiKey = getApiKey(context);
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtGChart", component)
                    .attr("chart", ((GChartModel) component.getValue()).toJson())
                    .attr("title", component.getTitle())
                    .attr("apiKey", apiKey)
                    .attr("language", component.getLanguage())
                    .attr("width", component.getWidth())
                    .attr("height", component.getHeight());
        if (component.getExtender() != null) {
            wb.nativeAttr("extender", component.getExtender());
        }
        encodeClientBehaviors(context, component);

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