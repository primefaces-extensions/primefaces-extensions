package org.primefaces.extensions.component.analogclock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.primefaces.extensions.component.analogclock.model.AnalogClockColorModel;
import org.primefaces.extensions.util.ColorUtils;
import org.primefaces.extensions.util.ExtWidgetBuilder;
import org.primefaces.json.JSONObject;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for {@link AnalogClock}
 * @author f.strazzullo
 * @since 3.0.0
 *
 */
public class AnalogClockRenderer extends CoreRenderer {

	public static final String RENDERER_TYPE = "org.primefaces.extensions.component.AnalogClockRenderer";

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		AnalogClock analogClock = (AnalogClock) component;

		encodeMarkup(context, analogClock);
		encodeScript(context, analogClock);
	}

	protected void encodeMarkup(FacesContext context, AnalogClock clock) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", clock);
		writer.writeAttribute("id", clock.getClientId(), null);
		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, AnalogClock analogClock) throws IOException {

		String clientId = analogClock.getClientId();
		String widgetVar = analogClock.resolveWidgetVar();

		ExtWidgetBuilder ewb = ExtWidgetBuilder.get(context);

		ewb.initWithDomReady("AnalogClock", widgetVar, clientId);
		ewb.attr("mode", analogClock.getMode());
		ewb.attr("time", analogClock.getStartTime() != null ? analogClock.getStartTime().getTime() : null);
		if (analogClock.getColorTheme() != null) {
			if (analogClock.getColorTheme() instanceof String) {
				ewb.attr("colorTheme", analogClock.getColorTheme().toString());
			} else {
                AnalogClockColorModel model = (AnalogClockColorModel) analogClock.getColorTheme();
                ewb.attr("themeObject", this.escapeText(model.toJson()));
			}
		}

		if (analogClock.getWidth() != null) {
			ewb.attr("width", analogClock.getWidth().toString());
		}

		ewb.finish();
	}

}