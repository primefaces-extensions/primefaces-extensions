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

package org.primefaces.extensions.component.message;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.application.TargetableFacesMessage;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Message} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class MessageRenderer extends CoreRenderer {

    @Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
    	final ResponseWriter writer = context.getResponseWriter();
    	final Message message = (Message) component;
    	final UIComponent target = message.findComponent(message.getFor());
    	final String display = message.getDisplay();
    	final boolean iconOnly = display.equals("icon");

		if (target == null) {
			throw new FacesException("Cannot find component \"" + message.getFor() + "\" in view.");
		}

		final Iterator<FacesMessage> msgs = context.getMessages(target.getClientId(context));

		writer.startElement("div", message);
		writer.writeAttribute("id", message.getClientId(context), null);

		if (msgs.hasNext()) {
			final FacesMessage facesMessage = msgs.next();

			if (facesMessage.isRendered() && !message.isRedisplay()) {
				writer.endElement("div");
				return;
			}

			final boolean shouldRender = message.shouldRender(facesMessage,
					TargetableFacesMessage.Target.ALL, TargetableFacesMessage.Target.MESSAGE);

			if (shouldRender) {
				final Severity severity = facesMessage.getSeverity();

				String severityKey = null;

				if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
					severityKey = "error";
				} else if (severity.equals(FacesMessage.SEVERITY_INFO)) {
					severityKey = "info";
				} else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
					severityKey = "warn";
				} else if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
					severityKey = "fatal";
				}

				String styleClass = "ui-message-" + severityKey + " ui-widget ui-corner-all";
				if (iconOnly) {
				    styleClass = styleClass + " ui-message-icon-only ui-helper-clearfix";
				}

				writer.writeAttribute("class", styleClass , null);

				if (!display.equals("text")) {
				    encodeIcon(writer, severityKey, facesMessage.getDetail(), iconOnly);
				}

				if (!iconOnly) {
				    if (message.isShowSummary()) {
				        encodeText(writer, facesMessage.getSummary(), severityKey + "-summary", message);
				    }
				    if (message.isShowDetail()) {
				        encodeText(writer, facesMessage.getDetail(), severityKey + "-detail", message);
				    }
				}
				facesMessage.rendered();
			}
		}
		writer.endElement("div");
	}

	protected void encodeText(final ResponseWriter writer, final String text, final String severity, final Message message)
			throws IOException {

		writer.startElement("span", null);
		writer.writeAttribute("class", "ui-message-" + severity, null);

		if (message.isEscape()) {
			writer.writeText(text, null);
		} else {
			writer.write(text);
		}

		writer.endElement("span");
	}

    protected void encodeIcon(final ResponseWriter writer, final String severity, final String title, final boolean iconOnly)
    	throws IOException {

		writer.startElement("span", null);
		writer.writeAttribute("class", "ui-message-" + severity + "-icon", null);
        if (iconOnly) {
            writer.writeAttribute("title", title, null);
        }
		writer.endElement("span");
	}
}