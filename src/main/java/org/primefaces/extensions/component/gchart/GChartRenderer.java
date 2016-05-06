/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 *
 * $Id$
 */
package org.primefaces.extensions.component.gchart;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import org.primefaces.context.RequestContext;

public class GChartRenderer extends CoreRenderer {

    @Override
	public void decode(FacesContext context, UIComponent component) {
		super.decode(context, component);
		decodeBehaviors(context, component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		GChart gChart = (GChart) component;

		encodeMarkup(context, gChart);
		encodeScript(context, gChart);
	}

	protected void encodeMarkup(FacesContext context, GChart chart)	throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("input", chart);
		writer.writeAttribute("id", chart.getClientId() + "_hidden",null);
		writer.writeAttribute("name",chart.getClientId() + "_hidden", null);
		writer.writeAttribute("type", "hidden", null);
		writer.endElement("input");

		writer.startElement("div", chart);
		writer.writeAttribute("id", chart.getClientId(), null);
		writer.endElement("div");
		
		this.startScript(writer, chart.getClientId());
		writer.writeAttribute("src", "https://www.google.com/jsapi", null);
		this.endScript(writer);
		
	}

	protected void encodeScript(FacesContext context, GChart chart) throws IOException {

		String clientId = chart.getClientId();
		String widgetVar = chart.resolveWidgetVar();

        WidgetBuilder wb = RequestContext.getCurrentInstance().getWidgetBuilder();
		wb.init("ExtGChart", widgetVar, clientId)
                    .attr("chart", this.escapeText(((GChartModel) chart.getValue()).toJson()))
                    .attr("title", chart.getTitle())
                    .attr("width", chart.getWidth())
                    .attr("height", chart.getHeight());
		
		encodeClientBehaviors(context, chart);
		
		wb.finish();
	}
}