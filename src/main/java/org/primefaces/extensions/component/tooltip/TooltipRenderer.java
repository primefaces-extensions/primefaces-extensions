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

package org.primefaces.extensions.component.tooltip;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Tooltip} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class TooltipRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		Tooltip tooltip = (Tooltip) component;
		String clientId = tooltip.getClientId(context);
		boolean global = tooltip.isGlobal();
		boolean shared = tooltip.isShared();
		String target = null;

		if (!tooltip.isGlobal()) {
			target = ComponentUtils.findTarget(tooltip, context);
		}

		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_script", null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.write("$(function() {");

		writer.write(tooltip.resolveWidgetVar() + " = new PrimeFacesExt.widget.Tooltip('" + clientId + "',{");
		writer.write("global:" + global);
		writer.write(",shared:" + shared);

		if (!global) {
			writer.write(",forTarget:'" + target + "'");
			writer.write(",content:'");
			if (tooltip.getValue() == null) {
				renderChildren(context, tooltip);
			} else {
				writer.write(ComponentUtils.getStringValueToRender(context, tooltip, tooltip.getValue()).replaceAll("'", "\\\\'"));
			}

			writer.write("'");
		}

		//Events
		if (shared && !global) {
			writer.write(",show:{target:$('" + target + "')}");
			writer.write(",hide:{target:$('" + target + "')}");
		} else {
			writer.write(",show:{event:'" + tooltip.getShowEvent() + "',delay:" + tooltip.getShowDelay()
			             + ",effect:function(){$(this)." + tooltip.getShowEffect() + "(" + tooltip.getShowEffectLength()
			             + ");}}");
			writer.write(",hide:{event:'" + tooltip.getHideEvent() + "',delay:" + tooltip.getHideDelay()
			             + ",effect:function(){$(this)." + tooltip.getHideEffect() + "(" + tooltip.getHideEffectLength()
			             + ");}}");
		}

		//Position
		writer.write(",position: {");
		writer.write("at:'" + tooltip.getTargetPosition() + "'");
		writer.write(",my:'" + tooltip.getPosition() + "'");
		if (shared && !global) {
			writer.write(",target:'event'");
			writer.write(",effect:false");
		}

		writer.write("}});});");
		writer.endElement("script");
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		//do nothing
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
