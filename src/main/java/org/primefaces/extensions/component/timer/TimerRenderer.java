/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
        writer.writeAttribute("class", Timer.STYLE_CLASS + " " + timer.getStyleClass(), "styleclass");
        writer.writeAttribute("style", (!timer.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + timer.getStyle(), "style");
        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final Timer timer) throws IOException {

        final String clientId = timer.getClientId(context);
        final String widgetVar = timer.resolveWidgetVar();

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

        wb.init("ExtTimer", widgetVar, clientId).attr("timeout", timer.getTimeout())
                    .attr("singleRun", timer.isSingleRun()).attr("format", timer.getFormat())
                    .attr("autoStart", timer.isAutoStart()).attr("forward", timer.isForward())
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