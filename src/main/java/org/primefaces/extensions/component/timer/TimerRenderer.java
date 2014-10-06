package org.primefaces.extensions.component.timer;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.util.ExtWidgetBuilder;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.AjaxRequestBuilder;
import org.primefaces.util.ComponentUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;

/**
 * Renderer for {@link Timer}
 * @author f.strazzullo
 * @since 3.0.0
 *
 */
public class TimerRenderer extends CoreRenderer {

	public static final String RENDERER_TYPE = "org.primefaces.extensions.component.TimerRenderer";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Timer timer = (Timer) component;

        if(context.getExternalContext().getRequestParameterMap().containsKey(timer.getClientId(context))) {
            ActionEvent event = new ActionEvent(timer);
            if(timer.isImmediate())
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            else
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);

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
        writer.writeAttribute("class", Timer.STYLE_CLASS + timer.getStyleClass(), "styleclass");
        writer.writeAttribute("style", (!timer.isVisible() ? "display:none;" : "") + timer.getStyle(), "style");
        writer.endElement("span");
	}

	protected void encodeScript(FacesContext context, Timer timer) throws IOException {

        String clientId = timer.getClientId(context);
        String widgetVar = timer.resolveWidgetVar();

        UIComponent form = ComponentUtils.findParentForm(context, timer);
        if(form == null) {
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
                .partialSubmit(timer.isPartialSubmit(), timer.isPartialSubmitSet())
                .resetValues(timer.isResetValues(), timer.isResetValuesSet())
                .ignoreAutoUpdate(timer.isIgnoreAutoUpdate())
                .onstart(timer.getOnstart())
                .onerror(timer.getOnerror())
                .onsuccess(timer.getOnsuccess())
                .oncomplete(timer.getOncomplete())
                .params(timer)
                .build();

        ExtWidgetBuilder ewb = ExtWidgetBuilder.get(context);

        ewb.initWithDomReady("Timer", widgetVar, clientId).
                attr("timeout", timer.getTimeout()).
                attr("singleRun", timer.isSingleRun()).
                attr("format", timer.getFormat()).
                attr("autoStart", timer.isAutoStart()).
                callback("listener","function()",request);

        if(StringUtils.isNotEmpty(timer.getOntimerstep())){
            ewb.callback("ontimerstep","function(intervalData)", timer.getOntimerstep());
        }

        if(StringUtils.isNotEmpty(timer.getFormatFunction())){
            ewb.callback("formatFunction","function(value)", timer.getFormatFunction());
        }

        if(StringUtils.isNotEmpty(timer.getOntimercomplete())){
            ewb.callback("ontimercomplete","function()", timer.getOntimercomplete());
        }

        ewb.finish();
	}

}