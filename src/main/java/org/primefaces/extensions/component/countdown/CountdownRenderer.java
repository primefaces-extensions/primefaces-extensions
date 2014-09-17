package org.primefaces.extensions.component.countdown;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.clock.Clock;
import org.primefaces.component.inputtext.InputText;
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
 * Renderer for {@link org.primefaces.extensions.component.countdown.Countdown}
 * @author f.strazzullo
 * @since 3.0.0
 *
 */
public class CountdownRenderer extends CoreRenderer {

	public static final String RENDERER_TYPE = "org.primefaces.extensions.component.CountdownRenderer";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Countdown countdown = (Countdown) component;

        if(context.getExternalContext().getRequestParameterMap().containsKey(countdown.getClientId(context))) {
            ActionEvent event = new ActionEvent(countdown);
            if(countdown.isImmediate())
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            else
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);

            countdown.queueEvent(event);
        }
    }

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		Countdown countdown = (Countdown) component;

        encodeMarkup(context,countdown);
        encodeScript(context,countdown);

	}

	protected void encodeMarkup(FacesContext context, Countdown countdown) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", countdown);
        writer.writeAttribute("id", countdown.getClientId(), null);
        writer.writeAttribute("class", Countdown.STYLE_CLASS + countdown.getStyleClass(), "styleclass");
        writer.writeAttribute("style", (!countdown.isVisible() ? "display:none;" : "") + countdown.getStyle(), "style");
        writer.endElement("span");
	}

	protected void encodeScript(FacesContext context, Countdown countdown) throws IOException {

        String clientId = countdown.getClientId(context);
        String widgetVar = countdown.resolveWidgetVar();

        UIComponent form = ComponentUtils.findParentForm(context, countdown);
        if(form == null) {
            throw new FacesException("Countdown:" + clientId + " needs to be enclosed in a form component");
        }

        AjaxRequestBuilder builder = RequestContext.getCurrentInstance().getAjaxRequestBuilder();

        String request = builder.init()
                .source(clientId)
                .form(form.getClientId(context))
                .process(countdown, countdown.getProcess())
                .update(countdown, countdown.getUpdate())
                .async(countdown.isAsync())
                .global(countdown.isGlobal())
                .delay(countdown.getDelay())
                .partialSubmit(countdown.isPartialSubmit(),countdown.isPartialSubmitSet())
                .resetValues(countdown.isResetValues(), countdown.isResetValuesSet())
                .ignoreAutoUpdate(countdown.isIgnoreAutoUpdate())
                .onstart(countdown.getOnstart())
                .onerror(countdown.getOnerror())
                .onsuccess(countdown.getOnsuccess())
                .oncomplete(countdown.getOncomplete())
                .params(countdown)
                .build();

        ExtWidgetBuilder ewb = ExtWidgetBuilder.get(context);

        ewb.initWithDomReady("Countdown", widgetVar, clientId).
                attr("timeout",countdown.getTimeout()).
                attr("singleRun",countdown.isSingleRun()).
                attr("format",countdown.getFormat()).
                attr("autoStart",countdown.isAutoStart()).
                callback("listener","function()",request);

        if(StringUtils.isNotEmpty(countdown.getOntimerstep())){
            ewb.callback("ontimerstep","function(intervalData)",countdown.getOntimerstep());
        }

        if(StringUtils.isNotEmpty(countdown.getFormatFunction())){
            ewb.callback("formatFunction","function(value)",countdown.getFormatFunction());
        }

        if(StringUtils.isNotEmpty(countdown.getOntimercomplete())){
            ewb.callback("ontimercomplete","function()",countdown.getOntimercomplete());
        }

        ewb.finish();
	}

}