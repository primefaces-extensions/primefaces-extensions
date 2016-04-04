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

package org.primefaces.extensions.component.spotlight;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import org.primefaces.extensions.util.ExtWidgetBuilder;

/**
 * Renderer for the {@link org.primefaces.extensions.component.spotlight.Spotlight} component.
 *
 * @author  Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class SpotlightRenderer extends CoreRenderer {
	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		encodeMarkup(fc, component);
		encodeScript(fc, component);
	}

	protected void encodeMarkup(final FacesContext fc, final UIComponent component) throws IOException {
		Spotlight spotlight = (Spotlight) component;

		ResponseWriter writer = fc.getResponseWriter();
		writer.startElement("div", spotlight);
		writer.writeAttribute("id", spotlight.getClientId(fc), null);
		writer.writeAttribute("class", StringUtils.trim("pe-lock-panel "+ spotlight.getStyleClass()), "styleClass");

		String style = spotlight.getStyle();
		if (!StringUtils.isBlank(style)) {
			writer.writeAttribute("style", style, "style");
		}
		renderChildren(fc, component);
		writer.endElement("div");
	}

	protected void encodeScript(final FacesContext context, final UIComponent component) throws IOException {
		Spotlight spotlight = (Spotlight) component;

        ExtWidgetBuilder wb = ExtWidgetBuilder.get(context);
        wb.initWithDomReady(Spotlight.class.getSimpleName(), spotlight.resolveWidgetVar(), spotlight.getClientId());
        wb.attr("blocked", spotlight.isBlocked());

        wb.finish();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
		// nothing to do
	}
}
