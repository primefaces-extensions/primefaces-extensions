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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;

/**
 * Tooltip renderer.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class TooltipRenderer extends CoreRenderer {
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		Tooltip tooltip = (Tooltip) component;

		// dummy markup for ajax update
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.startElement("span", tooltip);
		writer.writeAttribute("id", tooltip.getClientId(facesContext), "id");
		writer.endElement("span");

		encodeScript(facesContext, tooltip);
	}

	protected void encodeScript(FacesContext facesContext, Tooltip tooltip) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		boolean global = tooltip.isGlobal();
		String owner = getTarget(facesContext, tooltip);

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.write("$(function() {");

		writer.write(tooltip.resolveWidgetVar() + " = new PrimeFacesExt.widget.Tooltip({");
		writer.write("global:" + global);

		if (!global) {
			writer.write(",forComponent:'" + owner + "'");
			writer.write(",content:'");
			if (tooltip.getValue() == null) {
				renderChildren(facesContext, tooltip);
			} else {
				writer.write(ComponentUtils.getStringValueToRender(facesContext, tooltip).replaceAll("'", "\\\\'"));
			}

			writer.write("'");
		}

		//Events
		writer.write(",show:{event:'" + tooltip.getShowEvent() + "',delay:" + tooltip.getShowDelay()
		             + ",effect:function(){$(this)." + tooltip.getShowEffect() + "(" + tooltip.getShowEffectLength()
		             + ");}}");
		writer.write(",hide:{event:'" + tooltip.getHideEvent() + "',delay:" + tooltip.getHideDelay()
		             + ",effect:function(){$(this)." + tooltip.getHideEffect() + "(" + tooltip.getHideEffectLength()
		             + ");}}");

		//Position
		writer.write(",position: {");
		writer.write("container:$(document.body)");
		writer.write(",at:'" + tooltip.getTargetPosition() + "'");
		writer.write(",my:'" + tooltip.getPosition() + "'");
		writer.write("}});});");

		writer.endElement("script");
	}

	protected String getTarget(FacesContext facesContext, Tooltip tooltip) {
		if (tooltip.isGlobal()) {
			return null;
		} else {
			String _for = tooltip.getFor();

			if (_for != null) {
				UIComponent forComponent = tooltip.findComponent(_for);
				if (forComponent == null) {
					throw new FacesException("Cannot find component \"" + _for + "\" in view.");
				} else {
					return forComponent.getClientId(facesContext);
				}
			} else if (tooltip.getForElement() != null) {
				return tooltip.getForElement();
			} else {
				return tooltip.getParent().getClientId(facesContext);
			}
		}
	}

	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
	}

	public boolean getRendersChildren() {
		return true;
	}
}
