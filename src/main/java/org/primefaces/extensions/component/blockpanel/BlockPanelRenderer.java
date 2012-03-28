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

package org.primefaces.extensions.component.blockpanel;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Renderer for the {@link BlockPanel} component.
 *
 * @author  Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class BlockPanelRenderer extends CoreRenderer {
	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		encodeMarkup(fc, component);
		encodeScript(fc, component);
	}

	protected void encodeMarkup(final FacesContext fc, final UIComponent component) throws IOException {
		BlockPanel blockPanel = (BlockPanel) component;

		ResponseWriter writer = fc.getResponseWriter();
		writer.startElement("div", blockPanel);
		writer.writeAttribute("id", blockPanel.getClientId(fc), null);
		writer.writeAttribute("class", StringUtils.trim("pe-lock-panel "+blockPanel.getStyleClass()), "styleClass");

		String style = blockPanel.getStyle();
		if (!StringUtils.isBlank(style)) {
			writer.writeAttribute("style", style, "style");
		}
		renderChildren(fc, component);
		writer.endElement("div");
	}

	protected void encodeScript(final FacesContext fc, final UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		BlockPanel blockPanel = (BlockPanel) component;
		String clientId = blockPanel.getClientId(fc);

		// generate script
		startScript(writer, clientId);
		writer.write("$(function() {");	// START OF ANONYM FUNCTION

		final String widgetVar = blockPanel.resolveWidgetVar();
		writer.write("PrimeFacesExt.cw('BlockPanel', '" + widgetVar + "',{id:'" + clientId + "', blocked: "+blockPanel.isBlocked()+"},true);");

		writer.write("});");			// END OF ANONYM FUNCTION
		endScript(writer);
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
