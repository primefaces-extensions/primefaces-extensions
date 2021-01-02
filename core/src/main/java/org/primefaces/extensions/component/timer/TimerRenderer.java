/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.*;

/**
 * Renderer for {@link Timer}
 *
 * @author f.strazzullo
 * @since 3.0.0
 */
public class TimerRenderer extends CoreRenderer {

    public static final String RENDERER_TYPE = "org.primefaces.extensions.component.TimerRenderer";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Timer timer = (Timer) component;

        if (context.getExternalContext().getRequestParameterMap().containsKey(timer.getClientId(context))) {
            final ActionEvent event = new ActionEvent(timer);
            if (timer.isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }

            timer.queueEvent(event);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Timer timer = (Timer) component;

        encodeMarkup(context, timer);
        encodeScript(context, timer);

    }

    protected void encodeMarkup(final FacesContext context, final Timer timer) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", timer);
        writer.writeAttribute("id", timer.getClientId(), null);
        writer.writeAttribute("title", timer.getTitle(), null);
        writer.writeAttribute(Attrs.CLASS, Timer.STYLE_CLASS + " " + timer.getStyleClass(), "styleclass");
        writer.writeAttribute(Attrs.STYLE, (!timer.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + timer.getStyle(), Attrs.STYLE);
        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final Timer timer) throws IOException {
        final String clientId = timer.getClientId(context);

        final UIForm form = ComponentTraversalUtils.closestForm(context, timer);
        if (form == null) {
            throw new FacesException("Timer:" + clientId + " needs to be enclosed in a form component");
        }

        final AjaxRequestBuilder builder = PrimeRequestContext.getCurrentInstance().getAjaxRequestBuilder();

        final String request = builder.init()
                    .source(clientId)
                    .form(timer, timer, form)
                    .process(timer, timer.getProcess())
                    .update(timer, timer.getUpdate())
                    .async(timer.isAsync())
                    .global(timer.isGlobal())
                    .delay(timer.getDelay())
                    .partialSubmit(timer.isPartialSubmit(), timer.isPartialSubmitSet(), timer.getPartialSubmitFilter())
                    .resetValues(timer.isResetValues(), timer.isResetValuesSet())
                    .ignoreAutoUpdate(timer.isIgnoreAutoUpdate())
                    .onstart(timer.getOnstart())
                    .onerror(timer.getOnerror())
                    .onsuccess(timer.getOnsuccess())
                    .oncomplete(timer.getOncomplete())
                    .params(timer)
                    .build();

        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtTimer", timer)
                    .attr("timeout", timer.getTimeout()).attr("interval", timer.getInterval())
                    .attr("singleRun", timer.isSingleRun()).attr("format", timer.getFormat())
                    .attr("autoStart", timer.isAutoStart()).attr("forward", timer.isForward())
                    .attr("locale", timer.calculateLocale().toString())
                    .callback("listener", "function()", request);

        if (!LangUtils.isValueBlank(timer.getOntimerstep())) {
            wb.callback("ontimerstep", "function(intervalData)", timer.getOntimerstep());
        }

        if (!LangUtils.isValueBlank(timer.getFormatFunction())) {
            wb.callback("formatFunction", "function(value)", timer.getFormatFunction());
        }

        if (!LangUtils.isValueBlank(timer.getOntimercomplete())) {
            wb.callback("ontimercomplete", "function()", timer.getOntimercomplete());
        }

        wb.finish();
    }

}