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

package org.primefaces.extensions.component.imagerotateandresize;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link ImageRotateAndResize} component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.1
 */
public class ImageRotateAndResizeRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final ImageRotateAndResize imageRotateAndResize = (ImageRotateAndResize) component;
		final String clientId = imageRotateAndResize.getClientId(context);
		final String widgetVar = imageRotateAndResize.resolveWidgetVar();
		final String target = SearchExpressionFacade.resolveClientId(
				context, imageRotateAndResize, imageRotateAndResize.getFor());

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('" + ImageRotateAndResize.class.getSimpleName() + "', '" + widgetVar + "', {");

		writer.write("id:'" + clientId + "'");
		writer.write(",target:'" + target + "'");

		encodeClientBehaviors(context, imageRotateAndResize);

		writer.write("});});");

		endScript(writer);
	}
}
