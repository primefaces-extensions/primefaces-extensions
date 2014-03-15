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

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.FastStringWriter;
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
        String widgetVar = tooltip.resolveWidgetVar();
		boolean global = tooltip.isGlobal();
		boolean shared = tooltip.isShared();
		boolean autoShow = tooltip.isAutoShow();
		boolean mouseTracking = tooltip.isMouseTracking();
		String target = null;

		if (!global || tooltip.getFor() != null) {
			target = SearchExpressionFacade.resolveComponentsForClient(context, tooltip, tooltip.getFor());
		}

		startScript(writer, clientId);
		writer.write("$(function() {");

		writer.write("PrimeFacesExt.cw('Tooltip', '" + widgetVar + "',{");
		writer.write("id:'" + clientId + "'");
        writer.write(",widgetVar:'" + widgetVar + "'");
		writer.write(",global:" + global);
		writer.write(",shared:" + shared);
		writer.write(",autoShow:" + autoShow);

		if (target == null) {
			writer.write(",forTarget:null");
		} else {
			writer.write(",forTarget:'" + target + "'");
		}

		if (!global) {
			writer.write(",content:\"");
			if (tooltip.getChildCount() > 0) {
				FastStringWriter fsw = new FastStringWriter();
				ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
				context.setResponseWriter(clonedWriter);

				renderChildren(context, tooltip);

				context.setResponseWriter(writer);
				writer.write(escapeText(fsw.toString()));
			} else {
				String valueToRender = ComponentUtils.getValueToRender(context, tooltip);
				if (valueToRender != null) {
					writer.write(escapeText(valueToRender));
				}
			}

			writer.write("\"");
		}

		// events
		if (mouseTracking) {
			writer.write(",hide:{fixed:true}");
		} else if (shared && !global) {
			writer.write(",show:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('" + target + "')" + ",delay:"
			             + tooltip.getShowDelay() + ",effect:function(){$(this)." + tooltip.getShowEffect() + "("
			             + tooltip.getShowEffectLength() + ");}}");
			writer.write(",hide:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('" + target + "')" + ",delay:"
			             + tooltip.getHideDelay() + ",fixed:" + tooltip.isFixed() + ",effect:function(){$(this)."
			             + tooltip.getHideEffect() + "(" + tooltip.getHideEffectLength() + ");}}");
		} else if (autoShow) {
			writer.write(",show:{when:false,ready:true}");
			writer.write(",hide:false");
		} else {
			writer.write(",show:{event:'" + tooltip.getShowEvent() + "',delay:" + tooltip.getShowDelay()
			             + ",effect:function(){$(this)." + tooltip.getShowEffect() + "(" + tooltip.getShowEffectLength()
			             + ");}}");
			writer.write(",hide:{event:'" + tooltip.getHideEvent() + "',delay:" + tooltip.getHideDelay() + ",fixed:"
			             + tooltip.isFixed() + ",effect:function(){$(this)." + tooltip.getHideEffect() + "("
			             + tooltip.getHideEffectLength() + ");}}");
		}

		// position
		writer.write(",position: {");
		writer.write("at:'" + tooltip.getAtPosition() + "'");
		writer.write(",my:'" + tooltip.getMyPosition() + "'");
		writer.write(",adjust:{x:" + tooltip.getAdjustX() + ",y:" + tooltip.getAdjustY() + "}");
		writer.write(",viewport:$(window)");
		if (mouseTracking) {
			writer.write(",target:'mouse'");
		} else if (shared && !global) {
			writer.write(",target:'event'");
			writer.write(",effect:false");
		}

		writer.write("}},true);});");
		endScript(writer);
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
