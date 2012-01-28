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

package org.primefaces.extensions.component.imageareaselect;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.WidgetUtils;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link ImageAreaSelect} component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.1
 */
public class ImageAreaSelectRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final ImageAreaSelect imageAreaSelect = (ImageAreaSelect) component;
		final String clientId = imageAreaSelect.getClientId(context);
		final String widgetVar = imageAreaSelect.resolveWidgetVar();
		final String target = ComponentUtils.findTarget(context, imageAreaSelect);

		final Map<String, Object> additionalOptions = new Hashtable<String, Object>();
		additionalOptions.put("target", target);

		writer.startElement("script", imageAreaSelect);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('ImageAreaSelect', '" + widgetVar + "', {");

		WidgetUtils.renderAllOptions(clientId, writer, imageAreaSelect, false, additionalOptions);

		encodeClientBehaviors(context, imageAreaSelect);

		writer.write("}, true);});");
		writer.endElement("script");
	}
}
