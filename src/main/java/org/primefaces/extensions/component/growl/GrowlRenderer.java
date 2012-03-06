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

package org.primefaces.extensions.component.growl;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.application.TargetableFacesMessage;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Growl} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class GrowlRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final Growl growl = (Growl) component;
		final String clientId = growl.getClientId(context);
		final String widgetVar = growl.resolveWidgetVar();

		writer.startElement("span", growl);
		writer.writeAttribute("id", clientId, "id");
		writer.endElement("span");

		startScript(writer, clientId);

        writer.write("$(function(){");
        writer.write("PrimeFaces.cw('Growl','" + widgetVar + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",sticky:" + growl.isSticky());
        writer.write(",life:" + growl.getLife());

        writer.write(",msgs:");
        encodeMessages(context, growl);

        writer.write("});});");

        endScript(writer);
	}

	protected void encodeMessages(final FacesContext context, final Growl growl) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final Iterator<FacesMessage> messages = growl.isGlobalOnly() ? context.getMessages(null) : context.getMessages();

		writer.write("[");

		while(messages.hasNext()) {
			final FacesMessage message = messages.next();
			final boolean shouldRender = growl.shouldRender(message,
					TargetableFacesMessage.Target.ALL, TargetableFacesMessage.Target.GROWL);

			if (message.isRendered() && !growl.isRedisplay() || !shouldRender) {
				continue;
			}

			final String summary = escapeText(message.getSummary());
			final String detail = escapeText(message.getDetail());

			writer.write("{");

			if (growl.isShowSummary() && growl.isShowDetail()) {
				if (growl.isEscape()) {
					writer.writeText("summary:\"" + summary + "\",detail:\"" + detail + "\"", null);
				} else {
					writer.write("summary:\"" + summary + "\",detail:\"" + detail + "\"");
				}
			} else if (growl.isShowSummary() && !growl.isShowDetail()) {
				if (growl.isEscape()) {
					writer.writeText("summary:\"" + summary + "\",detail:\"\"", null);
				} else {
					writer.write("summary:\"" + summary + "\",detail:\"\"");
				}
			} else if(!growl.isShowSummary() && growl.isShowDetail()) {
				if (growl.isEscape()) {
					writer.writeText("summary:\"\",text:\"" + detail + "\"", null);
				} else {
					writer.write("summary:\"\",text:\"" + detail + "\"");
				}
			}

			writer.write(",severity:" + message.getSeverity().getOrdinal());

			writer.write("}");

			if (messages.hasNext()) {
				writer.write(",");
			}

			message.rendered();
		}

		writer.write("]");
	}
}
