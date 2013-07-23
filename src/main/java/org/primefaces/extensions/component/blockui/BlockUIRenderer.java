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

package org.primefaces.extensions.component.blockui;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;

/**
 * Renderer for the {@link BlockUI} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class BlockUIRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		encodeMarkup(fc, component);
		encodeScript(fc, component);
	}

	protected void encodeMarkup(final FacesContext fc, final UIComponent component) throws IOException {
		BlockUI blockUI = (BlockUI) component;
		if (blockUI.getContent() == null && blockUI.getChildCount() > 0) {
			ResponseWriter writer = fc.getResponseWriter();
			writer.startElement("div", null);
			writer.writeAttribute("id", blockUI.getClientId(fc) + "_content", null);
			writer.writeAttribute("style", "display: none;", null);
			renderChildren(fc, component);
			writer.endElement("div");
		}
	}

	protected void encodeScript(final FacesContext fc, final UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		BlockUI blockUI = (BlockUI) component;
		String clientId = blockUI.getClientId(fc);

		// get source
		String source = blockUI.getSource();
		if (source == null) {
            source = blockUI.getParent().getClientId(fc);
		} else {
			source = SearchExpressionFacade.resolveComponentsForClient(fc, blockUI, source);
		}

		if (source == null) {
			throw new FacesException("Cannot find source for blockUI component '" + clientId + "'.");
		}

		// get target
		String target = blockUI.getTarget();
		if (target != null) {
			target = SearchExpressionFacade.resolveComponentsForClient(fc, blockUI, target);
		}

		if (target == null) {
			throw new FacesException("Cannot determinate target for blockUI component '" + clientId + "'.");
		}

		// get content
		String jqContent = null;
		boolean isContentExtern = false;
		if (blockUI.getContent() != null) {
			UIComponent contentComponent = blockUI.findComponent(blockUI.getContent());
			if (contentComponent == null) {
				throw new FacesException("Cannot find content for blockUI component '" + clientId + "'.");
			}

			jqContent = ComponentUtils.escapeJQueryId(contentComponent.getClientId(fc));
			isContentExtern = true;
		} else if (blockUI.getChildCount() > 0) {
			jqContent = ComponentUtils.escapeJQueryId(clientId + "_content");
		}

		// get reg. expression
		String eventRegEx;
		String events = blockUI.getEvent();

		if (StringUtils.isBlank(events)) {
			// no events means all events of the given source are accepted
			eventRegEx = "/" + Constants.RequestParams.PARTIAL_SOURCE_PARAM + "=" + source + "(.)*$/";
		} else {
			String[] arrEvents = events.split("[\\s,]+");
			StringBuilder sb = new StringBuilder("/");

			for (int i = 0; i < arrEvents.length; i++) {
				sb.append(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
				sb.append("=");
				sb.append(arrEvents[i]);

				if (i + 1 < arrEvents.length) {
					sb.append("|");
				}
			}

			sb.append("/");
			eventRegEx = sb.toString();
		}

		// generate script
		startScript(writer, clientId);
		writer.write("$(function() {");

		final String widgetVar = blockUI.resolveWidgetVar();
		writer.write("PrimeFacesExt.cw('BlockUI', '" + widgetVar + "',{");

		writer.write("id:'" + clientId + "'");
		writer.write(",source:'" + source + "'");
		writer.write(",target:'" + target + "'");
        writer.write(",autoShow:" + blockUI.isAutoShow());
        
		if (jqContent != null) {
			writer.write(",content:'" + jqContent + "'");
		} else {
			writer.write(",content:null");
		}

		writer.write(",contentExtern:" + isContentExtern);
		writer.write(",regEx:" + eventRegEx + "},true);");

        /*
		if (blockUI.isAutoShow()) {
			writer.write(widgetVar + ".setupAjaxSend();");
			writer.write(widgetVar + ".setupAjaxComplete();");
		}*/

		writer.write("});");

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
