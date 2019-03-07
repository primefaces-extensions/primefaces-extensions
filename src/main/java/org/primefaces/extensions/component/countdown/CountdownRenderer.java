package org.primefaces.extensions.component.countdown;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer(componentFamily = Countdown.COMPONENT_FAMILY, rendererType = Countdown.DEFAULT_RENDERER)
public class CountdownRenderer extends Renderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        Countdown countdown = (Countdown) component;

        encodeMarkup(context, countdown);
    }

    public void encodeMarkup(FacesContext context, Countdown component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final String value = component.getValue();
        final String text = component.getText();
        final Boolean fast = component.isFast();

        String clientId = component.getClientId(context);
        String style = component.getStyle();
        String styleClass = component.getStyleClass();
        styleClass = (styleClass == null) ? Countdown.COMPONENT_CLASS : Countdown.COMPONENT_CLASS + " " + styleClass;

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, null);

        writer.writeAttribute("class", styleClass, "styleClass");

        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        writer.writeAttribute("countdown", "", null);
        writer.writeAttribute("data-text", text, null);
        writer.writeAttribute("data-fast", fast, null);
        writer.writeText(value, null);
        writer.endElement("span");
    }

}
