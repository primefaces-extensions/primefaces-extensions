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
package org.primefaces.extensions.component.timer;

import java.io.IOException;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIForm;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.AjaxRequestBuilder;
import org.primefaces.util.ComponentTraversalUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link Timer}
 *
 * @author f.strazzullo
 * @since 3.0.0
 */
@FacesRenderer(rendererType = Timer.DEFAULT_RENDERER, componentFamily = Timer.COMPONENT_FAMILY)
public class TimerRenderer extends CoreRenderer<Timer> {

    @Override
    public void decode(final FacesContext context, final Timer component) {
        if (context.getExternalContext().getRequestParameterMap().containsKey(component.getClientId(context))) {
            final ActionEvent event = new ActionEvent(component);
            if (component.isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }

            component.queueEvent(event);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final Timer component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final Timer component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", component);
        writer.writeAttribute("id", component.getClientId(), null);
        writer.writeAttribute("title", component.getTitle(), null);
        writer.writeAttribute(Attrs.CLASS, Timer.STYLE_CLASS + " " + component.getStyleClass(), "styleclass");
        writer.writeAttribute(Attrs.STYLE,
                    (!component.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + component.getStyle(), Attrs.STYLE);
        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final Timer component) throws IOException {
        final String clientId = component.getClientId(context);

        final UIForm form = ComponentTraversalUtils.closestForm(component);
        if (form == null) {
            throw new FacesException("Timer:" + clientId + " needs to be enclosed in a form component");
        }

        final AjaxRequestBuilder builder = PrimeRequestContext.getCurrentInstance().getAjaxRequestBuilder();

        String request = null;
        if (component.getListener() != null) {
            request = builder.init()
                        .source(clientId)
                        .form(component, component, form)
                        .process(component, component.getProcess())
                        .update(component, component.getUpdate())
                        .async(component.isAsync())
                        .global(component.isGlobal())
                        .delay(component.getDelay())
                        .partialSubmit(component.isPartialSubmit(), component.isPartialSubmitSet(), component.getPartialSubmitFilter())
                        .resetValues(component.isResetValues(), component.isResetValuesSet())
                        .ignoreAutoUpdate(component.isIgnoreAutoUpdate())
                        .onstart(component.getOnstart())
                        .onerror(component.getOnerror())
                        .onsuccess(component.getOnsuccess())
                        .oncomplete(component.getOncomplete())
                        .params(component)
                        .build();
        }

        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtTimer", component)
                    .attr("timeout", component.getTimeout()).attr("interval", component.getInterval())
                    .attr("singleRun", component.isSingleRun()).attr("format", component.getFormat())
                    .attr("autoStart", component.isAutoStart()).attr("forward", component.isForward())
                    .attr("locale", component.calculateLocale().toString());

        if (request != null) {
            wb.callback("listener", "function()", request);
        }

        if (LangUtils.isNotBlank(component.getOntimerstep())) {
            wb.callback("ontimerstep", "function(intervalData)", component.getOntimerstep());
        }

        if (LangUtils.isNotBlank(component.getFormatFunction())) {
            wb.callback("formatFunction", "function(value)", component.getFormatFunction());
        }

        if (LangUtils.isNotBlank(component.getOntimercomplete())) {
            wb.callback("ontimercomplete", "function()", component.getOntimercomplete());
        }

        wb.finish();
    }

}