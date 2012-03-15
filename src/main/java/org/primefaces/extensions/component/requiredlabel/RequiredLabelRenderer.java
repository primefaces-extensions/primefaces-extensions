/*
 * Copyright 2011 - 2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.requiredlabel;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;

/**
 * Renderer for the {@link RequiredLabel} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.4
 */
public class RequiredLabelRenderer extends CoreRenderer {

	private static final String[] PASS_TROUGH_ATTRIBUTES =
		new String[] { "title", "tabindex", "style", "dir", "accesskey", "lang" };

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		final ResponseWriter writer = context.getResponseWriter();
		final RequiredLabel label = (RequiredLabel) component;

		String defaultClass = "";

		writer.startElement("label", component);

		final String forValue = label.getFor();
		if (forValue != null) {
			final UIComponent forComponent = component.findComponent(forValue);
			if (forComponent == null) {
				throw new FacesException("Cannot find component '" + forValue + "'.");
			}

			writer.writeAttribute("for", forComponent.getClientId(context), null);

	        if (forComponent instanceof UIInput) {
	        	defaultClass = ((UIInput) forComponent).isValid() ? defaultClass : defaultClass + " re-required-label-invalid";
	        }
		}

		//style
        String styleClass = label.getStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

		writer.writeAttribute("class", styleClass, null);

		//render common attributes
		renderPassThruAttributes(context, label, HTML.COMMON_EVENTS);
		renderPassThruAttributes(context, label, HTML.BLUR_FOCUS_EVENTS);
		renderPassThruAttributes(context, label, PASS_TROUGH_ATTRIBUTES);

		//write text
		if (label.isEscape()) {
			writer.writeText(ComponentUtils.getValueToRender(context, label), null);
		} else {
			writer.write(ComponentUtils.getValueToRender(context, label));
		}

		encodeAdditionalContent(context, component);

		writer.endElement("label");
	}

	protected void encodeAdditionalContent(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final RequiredLabel label = (RequiredLabel) component;

		writer.startElement("span", component);
		writer.writeAttribute("class", RequiredLabel.SPAN_DEFAULT_STYLE_CLASS, null);

		if (label.isEscape()) {
			writer.writeText(label.getRequiredIndicator(), null);
		} else {
			writer.write(label.getRequiredIndicator());
		}

		writer.endElement("span");
	}
}
