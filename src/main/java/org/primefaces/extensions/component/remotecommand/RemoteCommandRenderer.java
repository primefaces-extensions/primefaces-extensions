/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.primefaces.extensions.component.base.AbstractParameter;
import org.primefaces.extensions.component.parameters.AssignableParameter;
import org.primefaces.extensions.util.ExtAjaxRequestBuilder;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentTraversalUtils;

/**
 * Renderer for the {@link RemoteCommand} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class RemoteCommandRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final RemoteCommand command = (RemoteCommand) component;

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = command.getClientId(context);

        if (params.containsKey(clientId)) {
            final ActionEvent event = new ActionEvent(command);
            if (command.isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }

            // apply params
            final ELContext elContext = context.getELContext();

            for (final AssignableParameter param : command.getAssignableParameters()) {
                if (!param.isRendered()) {
                    continue;
                }

                final ValueExpression valueExpression = param.getAssignTo();
                final String paramValue = params.get(clientId + "_" + param.getName());

                final Converter converter = param.getConverter();
                if (converter != null) {
                    final Object convertedValue = converter.getAsObject(context, param, paramValue);
                    valueExpression.setValue(elContext, convertedValue);
                }
                else {
                    valueExpression.setValue(elContext, paramValue);
                }
            }

            command.queueEvent(event);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final UIForm form = ComponentTraversalUtils.closestForm(context, component);
        if (form == null) {
            throw new FacesException("Component " + component.getClientId(context)
                        + " must be enclosed in a form.");
        }

        final ResponseWriter writer = context.getResponseWriter();
        final RemoteCommand command = (RemoteCommand) component;
        final String clientId = command.getClientId(context);

        final List<AbstractParameter> parameters = command.getAllParameters();
        final String name = command.getName();

        ExtAjaxRequestBuilder builder = ExtAjaxRequestBuilder.get(context);
        builder.init()
                    .source(clientId)
                    .form(command, command, form)
                    .process(component, command.getProcess())
                    .update(component, command.getUpdate())
                    .async(command.isAsync())
                    .global(command.isGlobal())
                    .partialSubmit(command.isPartialSubmit(), command.isPartialSubmitSet(), command.getPartialSubmitFilter())
                    .resetValues(command.isResetValues(), command.isResetValuesSet())
                    .ignoreAutoUpdate(command.isIgnoreAutoUpdate())
                    .onstart(command.getOnstart())
                    .onerror(command.getOnerror())
                    .onsuccess(command.getOnsuccess())
                    .oncomplete(command.getOncomplete())
                    .delay(command.getDelay())
                    .timeout(command.getTimeout());

        builder.params(clientId, parameters);

        final String request = builder.build();

        // script
        writer.startElement("script", command);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("id", command.getClientId(), null);

        writer.write(name + " = function(");

        // parameters
        for (int i = 0; i < parameters.size(); i++) {
            if (i != 0) {
                writer.write(",");
            }

            final AbstractParameter param = parameters.get(i);
            writer.write(param.getName());
        }

        writer.write(") {");
        writer.write(request);
        writer.write("}");

        if (command.isAutoRun()) {
            writer.write(";$(function() {");
            writer.write(name + "();");
            writer.write("});");
        }

        writer.endElement("script");
    }
}
