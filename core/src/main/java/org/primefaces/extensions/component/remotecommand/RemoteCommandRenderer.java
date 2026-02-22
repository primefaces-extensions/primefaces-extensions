/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.remotecommand;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.component.UIForm;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.component.api.AbstractParameter;
import org.primefaces.extensions.component.parameters.AssignableParameter;
import org.primefaces.extensions.util.ExtAjaxRequestBuilder;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentTraversalUtils;

/**
 * Renderer for the RemoteCommand component.
 *
 * @since 0.2
 */
@FacesRenderer(rendererType = RemoteCommandBase.DEFAULT_RENDERER, componentFamily = RemoteCommandBase.COMPONENT_FAMILY)
public class RemoteCommandRenderer extends CoreRenderer<RemoteCommand> {

    @Override
    public void decode(final FacesContext context, final RemoteCommand command) {

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

                final Converter<?> converter = param.getConverter();
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
    public void encodeEnd(final FacesContext context, final RemoteCommand command) throws IOException {
        final UIForm form = ComponentTraversalUtils.closestForm(command);
        if (form == null) {
            throw new FacesException("Component " + command.getClientId(context)
                        + " must be enclosed in a form.");
        }

        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = command.getClientId(context);

        final List<AbstractParameter> parameters = command.getAllParameters();
        final String name = command.getName();

        final ExtAjaxRequestBuilder builder = ExtAjaxRequestBuilder.get(context);
        builder.init()
                    .source(clientId)
                    .form(command, command, form)
                    .process(command, command.getProcess())
                    .update(command, command.getUpdate())
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
