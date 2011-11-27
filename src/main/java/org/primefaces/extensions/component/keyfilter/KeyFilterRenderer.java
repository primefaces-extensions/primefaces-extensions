/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.keyfilter;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link KeyFilter} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class KeyFilterRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final KeyFilter keyFilter = (KeyFilter) component;
		final String target = findTarget(context, keyFilter).getClientId(context);
		final String clientId = keyFilter.getClientId(context);
		final String widgetVar = keyFilter.resolveWidgetVar();

		writer.startElement("script", keyFilter);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.write("$(function() {");

		writer.write(widgetVar + " = new PrimeFacesExt.widget.KeyFilter('" + clientId + "', {");
		writer.write("target:'" + target + "'");

		if (keyFilter.getRegularExpression() != null) {
			writer.write(",regEx:" + keyFilter.getRegularExpression());
		}

		writer.write("});});");
		writer.endElement("script");
	}

	protected UIComponent findTarget(final FacesContext facesContext, final KeyFilter keyFilter) {
		final String forValue = keyFilter.getFor();

		if (forValue == null) {
			throw new FacesException("\"for\" attribute for KeyFilter can not be null or empty");
		}

		final UIComponent component = keyFilter.findComponent(forValue);
		if (component == null) {
			throw new FacesException("Cannot find component \"" + forValue + "\" in view.");
		}

		return component;
	}
}
