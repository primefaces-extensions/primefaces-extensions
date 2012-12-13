/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.layout;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Layout} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class LayoutRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext fc, UIComponent component) {
		decodeBehaviors(fc, component);
	}

	@Override
	public void encodeBegin(FacesContext fc, UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		Layout layout = (Layout) component;

		boolean buildOptions = (layout.getOptions() == null);
		layout.setBuildOptions(buildOptions);

		if (buildOptions) {
			FastStringWriter fsw = new FastStringWriter();
			layout.setOriginalWriter(writer);
			layout.setFastStringWriter(fsw);
			fc.setResponseWriter(writer.cloneWithWriter(fsw));
			writer = fc.getResponseWriter();
		} else {
			encodeScript(fc, layout);
		}

		if (!layout.isFullPage()) {
			writer.startElement("div", layout);
			writer.writeAttribute("id", layout.getClientId(fc), "id");

			if (layout.getStyle() != null) {
				writer.writeAttribute("style", layout.getStyle(), "style");
			}

			if (layout.getStyleClass() != null) {
				writer.writeAttribute("class", layout.getStyleClass(), "styleClass");
			}
		}
	}

	@Override
	public void encodeEnd(FacesContext fc, UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		Layout layout = (Layout) component;

		if (!layout.isFullPage()) {
			if (!layout.isStateCookie()) {
				// render hidden field for server-side state saving
				String clientId = layout.getClientId(fc);
				writer.startElement("input", null);
				writer.writeAttribute("type", "hidden", null);
				writer.writeAttribute("id", clientId + "_state", null);
				writer.writeAttribute("name", clientId + "_state", null);
				writer.writeAttribute("autocomplete", "off", null);
				writer.endElement("input");
			}

			writer.endElement("div");
		}

		if (layout.isBuildOptions()) {
			fc.setResponseWriter(layout.getOriginalWriter());
			encodeScript(fc, layout);
			fc.getResponseWriter().write(layout.getFastStringWriter().toString());
			layout.removeOptions();
			layout.setOriginalWriter(null);
			layout.setFastStringWriter(null);
		}

		layout.setBuildOptions(false);
	}

	@Override
	public boolean getRendersChildren() {
		return false;
	}

	protected void encodeScript(FacesContext fc, Layout layout) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = layout.getClientId(fc);

		startScript(writer, clientId);
		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('Layout', '" + layout.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");

		if (layout.isFullPage()) {
			writer.write(",forTarget:'body'");
		} else {
			writer.write(",forTarget:'" + ComponentUtils.escapeJQueryId(clientId) + "'");
		}

		boolean stateCookie = layout.isStateCookie();
		writer.write(",clientState:");
		if (stateCookie) {
			writer.write("true");
		} else {
			writer.write("false");
		}

		writer.write(",serverState:");

		ValueExpression stateVE = layout.getValueExpression(Layout.PropertyKeys.state.toString());
		if (stateVE != null && !layout.isFullPage() && !stateCookie) {
			writer.write("true");

			String state = layout.getState();
			if (StringUtils.isNotBlank(state)) {
				writer.write(",state:" + state);
			} else {
				writer.write(",state:{}");
			}
		} else {
			writer.write("false");
		}

		LayoutOptions layoutOptions = (LayoutOptions) layout.getOptions();
		if (layoutOptions != null) {
			writer.write(",options:" + layoutOptions.render());
		} else {
			writer.write(",options:{}");
		}

		encodeClientBehaviors(fc, layout);

		writer.write("},true);});");
		endScript(writer);
	}
}
