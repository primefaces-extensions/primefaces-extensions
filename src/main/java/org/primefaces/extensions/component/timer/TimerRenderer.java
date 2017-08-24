/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.AjaxRequestBuilder;
import org.primefaces.util.ComponentTraversalUtils;
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
    public void decode(FacesContext context, UIComponent component) {
        Timer timer = (Timer) component;

        if (context.getExternalContext().getRequestParameterMap().containsKey(timer.getClientId(context))) {
            ActionEvent event = new ActionEvent(timer);
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
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        Timer timer = (Timer) component;

        encodeMarkup(context, timer);
        encodeScript(context, timer);

    }

    protected void encodeMarkup(FacesContext context, Timer timer) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", timer);
        writer.writeAttribute("id", timer.getClientId(), null);
        writer.writeAttribute("class", Timer.STYLE_CLASS + " " + timer.getStyleClass(), "styleclass");
        writer.writeAttribute("style", (!timer.isVisible() ? "display:none;" : "") + timer.getStyle(), "style");
        writer.endElement("span");
    }

    protected void encodeScript(FacesContext context, Timer timer) throws IOException {

        String clientId = timer.getClientId(context);
        String widgetVar = timer.resolveWidgetVar();

        UIComponent form = ComponentTraversalUtils.closestForm(context, timer);
        if (form == null) {
            throw new FacesException("Timer:" + clientId + " needs to be enclosed in a form component");
        }

        AjaxRequestBuilder builder = RequestContext.getCurrentInstance().getAjaxRequestBuilder();

        String request = builder.init()
                    .source(clientId)
                    .form(form.getClientId(context))
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

        WidgetBuilder wb = RequestContext.getCurrentInstance().getWidgetBuilder();

        wb.initWithDomReady("ExtTimer", widgetVar, clientId).attr("timeout", timer.getTimeout())
                    .attr("singleRun", timer.isSingleRun()).attr("format", timer.getFormat())
                    .attr("autoStart", timer.isAutoStart()).attr("forward", timer.isForward())
                    .callback("listener", "function()", request);

        if (StringUtils.isNotEmpty(timer.getOntimerstep())) {
            wb.callback("ontimerstep", "function(intervalData)", timer.getOntimerstep());
        }

        if (StringUtils.isNotEmpty(timer.getFormatFunction())) {
            wb.callback("formatFunction", "function(value)", timer.getFormatFunction());
        }

        if (StringUtils.isNotEmpty(timer.getOntimercomplete())) {
            wb.callback("ontimercomplete", "function()", timer.getOntimercomplete());
        }

        wb.finish();
    }

}