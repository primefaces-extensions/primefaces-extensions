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

        final String clientId = chart.getClientId();
        final String widgetVar = chart.resolveWidgetVar();

        String apiKey = chart.getApiKey();
        if (LangUtils.isValueBlank(apiKey)) {
            apiKey = getApiKey(context);
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtGChart", widgetVar, clientId)
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
        String key = null;
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