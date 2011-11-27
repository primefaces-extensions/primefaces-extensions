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

        if (isAjaxRequest(context)) {
            writer.write(widgetVar + ".show(");
            encodeMessages(context, growl);
            writer.write(");");
        } else {
            writer.write("$(function(){");

            writer.write("PrimeFaces.cw('Growl','" + widgetVar + "',{");
            writer.write("id:'" + clientId + "',msgs:");
            encodeMessages(context, growl);
            writer.write("});});");
        }

		endScript(writer);
	}

    protected void encodeMessages(final FacesContext context, final Growl growl) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
		final Iterator<FacesMessage> messages =
			growl.isGlobalOnly() ? context.getMessages(null) : context.getMessages();

        writer.write("[");

		while (messages.hasNext()) {
			final FacesMessage message = messages.next();
			final boolean shouldRender = growl.shouldRender(message,
					TargetableFacesMessage.Target.ALL, TargetableFacesMessage.Target.GROWL);

			if (shouldRender) {
				final String severityImage = getImage(context, growl, message);
				final String summary = escapeText(message.getSummary());
				final String detail = escapeText(message.getDetail());

	            writer.write("{");

	            //render summary
				if (growl.isShowSummary()) {
					if (growl.isEscape()) {
						writer.writeText("title:'" + summary + "'", null);
					} else {
						writer.write("title:'" + summary + "'");
					}
				} else {
					writer.write("title:''");
				}

				//render detail
				if (growl.isShowDetail()) {
					if (growl.isEscape()) {
						writer.writeText(",text:'" + detail + "'", null);
					} else {
						writer.write(",text:'" + detail + "'");
					}
				} else {
					writer.write(",text:''");
				}

				if (!isValueBlank(severityImage)) {
					writer.write(",image:'" + severityImage + "'");
				}

				if (growl.isSticky()) {
					writer.write(",sticky:true");
				} else {
					writer.write(",sticky:false");
				}

				if (growl.getLife() != 6000) {
	                writer.write(",time:" + growl.getLife());
				}

	            writer.write("}");

	            if (messages.hasNext()) {
	                writer.write(",");
	            }

				message.rendered();
			}
		}

        writer.write("]");
    }

	protected String getImage(final FacesContext facesContext, final Growl growl, final FacesMessage message) {
        final FacesMessage.Severity severity = message.getSeverity();

		if (severity != null) {
			if (severity.equals(FacesMessage.SEVERITY_INFO)) {
				return growl.getInfoIcon() != null ? getResourceURL(facesContext, growl.getInfoIcon())
						: getResourceRequestPath(facesContext, Growl.INFO_ICON);
			} else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
				return growl.getErrorIcon() != null ? getResourceURL(facesContext, growl.getErrorIcon())
						: getResourceRequestPath(facesContext, Growl.ERROR_ICON);
			} else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
				return growl.getWarnIcon() != null ? getResourceURL(facesContext, growl.getWarnIcon())
						: getResourceRequestPath(facesContext, Growl.WARN_ICON);
			} else if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
				return growl.getFatalIcon() != null ? getResourceURL(facesContext, growl.getFatalIcon())
						: getResourceRequestPath(facesContext, Growl.FATAL_ICON);
			}
		}

		return "";
	}
}
