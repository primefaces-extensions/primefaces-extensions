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
 */
package org.primefaces.extensions.component.remotecommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import org.primefaces.component.api.AjaxSource;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;

/**
 * Renderer for the {@link RemoteCommand} component.
 *
 * @author Thomas Andraschko
 * @since 0.2
 */
public class RemoteCommandRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		final RemoteCommand command = (RemoteCommand) component;

		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = command.getClientId(context);

		if (params.containsKey(clientId)) {
			ActionEvent event = new ActionEvent(command);
			if (command.isImmediate()) {
				event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
			} else {
				event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			}

			//apply params
			final ELContext elContext = context.getELContext();

			for (final RemoteCommandParameter param : getParameters(command)) {
				final ValueExpression valueExpression = param.getValueExpression("applyTo");
				final String paramValue = params.get(clientId + "_" + param.getName());

				final Converter converter = param.getConverter();
				if (converter != null) {
					final Object convertedValue = converter.getAsObject(context, param, paramValue);
					valueExpression.setValue(elContext, convertedValue);
				} else {
					valueExpression.setValue(elContext, paramValue);
				}
			}

			command.queueEvent(event);
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final RemoteCommand command = (RemoteCommand) component;

		final List<RemoteCommandParameter> parameters = getParameters(command);

		//script
		writer.startElement("script", command);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("id", command.getClientId(), null);

		writer.write(command.getName() + " = function(");

		//parameters
		for (int i = 0; i < parameters.size(); i++) {
			if (i != 0) {
				writer.write(",");
			}

			final RemoteCommandParameter param = parameters.get(i);
			writer.write(param.getName());
		}

		writer.write(") {");

		writer.write(buildAjaxRequest(context, command, parameters));

		writer.write("}");

		writer.endElement("script");
	}

	protected String buildAjaxRequest(final FacesContext context, final AjaxSource source,
			final List<RemoteCommandParameter> parameters) {

		final UIComponent component = (UIComponent) source;
		final String clientId = component.getClientId(context);
		final UIComponent form = ComponentUtils.findParentForm(context, component);

		if (form == null) {
			throw new FacesException("Component " + component.getClientId(context)
					+ " must be enclosed in a form.");
		}

		final StringBuilder req = new StringBuilder();
		req.append("PrimeFaces.ab(");

		//form
		req.append("{formId:").append("'").append(form.getClientId(context)).append("'");

		//source
		req.append(",source:").append("'").append(clientId).append("'");

		//process
		String process = source.getProcess();
		if (process == null) {
			process = "@all";
		} else {
			process = ComponentUtils.findClientIds(context, component, process);

			//add @this
			if (process.indexOf(clientId) == -1) {
				process = process + " " + clientId;
			}
		}
		req.append(",process:'").append(process).append("'");

		//update
		if (source.getUpdate() != null) {
			req.append(",update:'").append(ComponentUtils.findClientIds(context, component, source.getUpdate())).append("'");
		}

		//async
		if (source.isAsync()) {
			req.append(",async:true");
		}

		//global
		if (!source.isGlobal()) {
			req.append(",global:false");
		}

		//callbacks
		if (source.getOnstart() != null) {
			req.append(",onstart:function(){").append(source.getOnstart()).append(";}");
		}
		if (source.getOnerror() != null) {
			req.append(",onerror:function(xhr, status, error){").append(source.getOnerror()).append(";}");
		}
		if (source.getOnsuccess() != null) {
			req.append(",onsuccess:function(data, status, xhr){").append(source.getOnsuccess()).append(";}");
		}
		if (source.getOncomplete() != null) {
			req.append(",oncomplete:function(xhr, status, args){").append(source.getOncomplete()).append(";}");
		}

		//params
		req.append(",params:{");

		for (int i = 0; i < parameters.size(); i++) {
			if (i != 0) {
				req.append(",");
			}

			final RemoteCommandParameter param = parameters.get(i);

			req.append("\"");
			req.append(clientId).append("_").append(param.getName());
			req.append("\"");
			req.append(":").append(param.getName());
		}

		req.append("}});");

		return req.toString();
	}

	protected final List<RemoteCommandParameter> getParameters(final RemoteCommand command) {
		final List<RemoteCommandParameter> parameters = new ArrayList<RemoteCommandParameter>();
		for (final UIComponent child : command.getChildren()) {
			if (child instanceof RemoteCommandParameter) {
				parameters.add((RemoteCommandParameter) child);
			}
		}
		return parameters;
	}
}
