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

package org.primefaces.extensions.component.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Renderer for the {@link Messages} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class MessagesRenderer extends org.primefaces.component.messages.MessagesRenderer {

    @Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final Messages messagesComponent = (Messages) component;
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = messagesComponent.getClientId(context);

		final Map<String, List<FacesMessage>> messages = new HashMap<String, List<FacesMessage>>();
		messages.put("info", new ArrayList<FacesMessage>());
		messages.put("warn", new ArrayList<FacesMessage>());
		messages.put("error", new ArrayList<FacesMessage>());
		messages.put("fatal", new ArrayList<FacesMessage>());

		final Iterator<FacesMessage> messagesIterator =
			messagesComponent.isGlobalOnly() ? context.getMessages(null) : context.getMessages();

		while (messagesIterator.hasNext()) {
			final FacesMessage message = messagesIterator.next();
			final Severity severity = message.getSeverity();

			if (message.isRendered() && !messagesComponent.isRedisplay()) {
				continue;
			}

			if (severity.equals(FacesMessage.SEVERITY_INFO)) {
				messages.get("info").add(message);
			} else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
				messages.get("warn").add(message);
			} else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
				messages.get("error").add(message);
			} else if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
				messages.get("fatal").add(message);
			}
		}

		writer.startElement("div", messagesComponent);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", "ui-messages ui-widget", null);

		for (final String severity : messages.keySet()) {
			final List<FacesMessage> severityMessages = messages.get(severity);

			if (severityMessages.size() > 0) {
				encodeSeverityMessages(context, messagesComponent, severity, severityMessages);
			}
		}

		writer.endElement("div");
	}

	private void encodeSeverityMessages(final FacesContext context, final Messages messagesComponent, final String severity,
			final List<FacesMessage> messages) throws IOException {

		final ResponseWriter writer = context.getResponseWriter();
		String styleClassPrefix = "ui-messages-" + severity;

		writer.startElement("div", null);
		writer.writeAttribute("class", styleClassPrefix + " ui-corner-all", null);

		writer.startElement("span", null);
		writer.writeAttribute("class", styleClassPrefix + "-icon", null);
		writer.endElement("span");

		writer.startElement("ul", null);

		for (FacesMessage message : messages) {
			writer.startElement("li", null);

			final String summary = message.getSummary() != null ? message.getSummary() : "";
			final String detail = message.getDetail() != null ? message.getDetail() : summary;

            if (messagesComponent.isShowSummary()) {
	            writer.startElement("span", null);
	            writer.writeAttribute("class", styleClassPrefix + "-summary", null);

	            if (messagesComponent.isEscape().equals(Boolean.TRUE)) {
	    			writer.writeText(summary, null);
	    		} else {
	    			writer.write(summary);
	    		}

	            writer.endElement("span");
            }

            if (messagesComponent.isShowDetail()) {
            	writer.startElement("span", null);
            	writer.writeAttribute("class", styleClassPrefix + "-detail", null);

	            if (messagesComponent.isEscape().equals(Boolean.TRUE)) {
	    			writer.writeText(detail, null);
	    		} else {
	    			writer.write(detail);
	    		}

            	writer.endElement("span");
            }

			writer.endElement("li");

			message.rendered();
		}

		writer.endElement("ul");
		writer.endElement("div");
	}
}