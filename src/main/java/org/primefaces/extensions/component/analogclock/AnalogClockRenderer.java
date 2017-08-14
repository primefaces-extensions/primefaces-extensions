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
package org.primefaces.extensions.component.analogclock;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.context.RequestContext;

import org.primefaces.extensions.component.analogclock.model.AnalogClockColorModel;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link AnalogClock}
 * @author f.strazzullo
 * @since 3.0.0
 *
 */
public class AnalogClockRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		AnalogClock analogClock = (AnalogClock) component;

		encodeMarkup(context, analogClock);
		encodeScript(context, analogClock);
	}

	protected void encodeMarkup(FacesContext context, AnalogClock clock) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", clock);
		writer.writeAttribute("id", clock.getClientId(), null);
		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, AnalogClock analogClock) throws IOException {

		String clientId = analogClock.getClientId();
		String widgetVar = analogClock.resolveWidgetVar();

		WidgetBuilder wb = RequestContext.getCurrentInstance().getWidgetBuilder();

		wb.initWithDomReady("ExtAnalogClock", widgetVar, clientId)
                    .attr("mode", analogClock.getMode())
                    .attr("time", analogClock.getStartTime() != null ? analogClock.getStartTime().getTime() : null);
		if (analogClock.getColorTheme() != null) {
			if (analogClock.getColorTheme() instanceof String) {
				wb.attr("colorTheme", analogClock.getColorTheme().toString());
			} else {
                AnalogClockColorModel model = (AnalogClockColorModel) analogClock.getColorTheme();
                wb.attr("themeObject", model.toJson());
			}
		}

		if (analogClock.getWidth() != null) {
			wb.attr("width", analogClock.getWidth().toString());
		}

		wb.finish();
	}

}