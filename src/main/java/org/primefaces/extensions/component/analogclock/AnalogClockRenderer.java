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

    private static final Gson GSON = new GsonBuilder().create();

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
                String jsonColors = GSON.toJson(colorThemeToMap((AnalogClockColorModel) analogClock.getColorTheme()));
                ewb.attr("themeObject", this.escapeText(jsonColors));
			}
		}

		if (analogClock.getWidth() != null) {
			ewb.attr("width", analogClock.getWidth().toString());
		}

		ewb.finish();
	}

	private Map colorThemeToMap(AnalogClockColorModel colorTheme) {

		Map<String, String> map = new HashMap<String, String>(0);

		map.put("face", ColorUtils.colorToHex(colorTheme.getFace()));
		map.put("border", ColorUtils.colorToHex(colorTheme.getBorder()));
		map.put("hourHand", ColorUtils.colorToHex(colorTheme.getHourHand()));
        map.put("hourSigns", ColorUtils.colorToHex(colorTheme.getHourSigns()));
		map.put("minuteHand", ColorUtils.colorToHex(colorTheme.getMinuteHand()));
		map.put("secondHand", ColorUtils.colorToHex(colorTheme.getSecondHand()));
		map.put("secondSigns", ColorUtils.colorToHex(colorTheme.getHourSigns()));
		map.put("pin", ColorUtils.colorToHex(colorTheme.getPin()));

		return map;
	}

}