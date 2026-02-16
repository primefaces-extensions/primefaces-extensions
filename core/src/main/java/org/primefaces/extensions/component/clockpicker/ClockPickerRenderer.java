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
package org.primefaces.extensions.component.clockpicker;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

public class ClockPickerRenderer extends InputRenderer {

    private static final DateTimeFormatter FORMATTER_24_HOUR = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void decode(FacesContext context, UIComponent component) {

        final ClockPicker clockPicker = (ClockPicker) component;

        if (clockPicker.isDisabled() || clockPicker.isReadonly()) {
            return;
        }
        final String param = clockPicker.getClientId(context) + "_input";
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(param);

        if (null != submittedValue) {
            clockPicker.setSubmittedValue(submittedValue);
        }
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final ClockPicker clockPicker = (ClockPicker) component;
        final Locale locale = clockPicker.calculateLocale(context);
        final DateFormatSymbols symbols = new DateFormatSymbols(locale);

        final String value = getValueAsString(context, clockPicker);
        encodeMarkup(context, clockPicker, value, symbols);
        encodeScript(context, clockPicker, symbols);
    }

    private void encodeMarkup(FacesContext context, ClockPicker clockPicker, final String value, DateFormatSymbols symbols) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = clockPicker.getClientId();
        final String inputId = clientId + "_input";
        final String[] ampm = symbols.getAmPmStrings();
        final int inputSize = clockPicker.isTwelveHour() ? 5 + ampm[0].length() : 5;

        writer.startElement("div", clockPicker);
        writer.writeAttribute("id", clientId, null);
        String containerClass = ClockPicker.CONTAINER_CLASS;
        if (clockPicker.isShowOnButton()) {
            containerClass += " ui-inputgroup";
        }
        writer.writeAttribute("class", containerClass, null);

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("size", inputSize, null);
        writer.writeAttribute("maxlength", inputSize, null);

        final String styleClass = createStyleClass(clockPicker, "ui-inputfield ui-widget ui-state-default ui-corner-all");
        writer.writeAttribute(Attrs.CLASS, styleClass, null);

        if (clockPicker.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, clockPicker.getStyle(), null);
        }

        if (LangUtils.isNotBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        renderAccessibilityAttributes(context, clockPicker);
        renderPassThruAttributes(context, clockPicker, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, clockPicker, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, clockPicker);

        writer.endElement("input");

        if (clockPicker.isShowOnButton()) {
            writer.startElement("button", null);
            writer.writeAttribute(Attrs.CLASS, ClockPicker.BUTTON_TRIGGER_CLASS, null);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("role", "button", null);

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, ClockPicker.BUTTON_TRIGGER_ICON_CLASS, null);
            writer.endElement("span");

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, ClockPicker.BUTTON_TRIGGER_TEXT_CLASS, null);
            writer.write("ui-button");
            writer.endElement("span");
            writer.endElement("button");
        }

        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final ClockPicker clockPicker, DateFormatSymbols symbols) throws IOException {
        final String[] ampm = symbols.getAmPmStrings();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtClockPicker", clockPicker);
        wb.attr("appendTo", SearchExpressionUtils.resolveOptionalClientIdForClientSide(context, clockPicker, clockPicker.getAppendTo()));
        wb.attr("placement", clockPicker.getPlacement(), "bottom");
        wb.attr("align", clockPicker.getAlign(), "left");
        wb.attr("autoclose", clockPicker.isAutoClose(), false);
        wb.attr("vibrate", clockPicker.getVibrate(), true);
        wb.attr("twelvehour", clockPicker.isTwelveHour(), false);
        wb.attr("amtext", ampm[0], "AM");
        wb.attr("pmtext", ampm[1], "PM");

        if (clockPicker.getOnbeforehourselect() != null) {
            wb.callback("beforeHourSelect", "function()", clockPicker.getOnbeforehourselect());
        }
        if (clockPicker.getOnbeforedone() != null) {
            wb.callback("beforeDone", "function()", clockPicker.getOnbeforedone());
        }
        if (clockPicker.getOnbeforeshow() != null) {
            wb.callback("beforeShow", "function()", clockPicker.getOnbeforeshow());
        }
        if (clockPicker.getOnbeforehide() != null) {
            wb.callback("beforeHide", "function()", clockPicker.getOnbeforehide());
        }
        if (clockPicker.getOnafterhourselect() != null) {
            wb.callback("afterHourSelect", "function()", clockPicker.getOnafterhourselect());
        }
        if (clockPicker.getOnafterdone() != null) {
            wb.callback("afterDone", "function()", clockPicker.getOnafterdone());
        }
        if (clockPicker.getOnaftershow() != null) {
            wb.callback("afterShow", "function()", clockPicker.getOnaftershow());
        }
        if (clockPicker.getOnafterhide() != null) {
            wb.callback("afterHide", "function()", clockPicker.getOnafterhide());
        }
        if (clockPicker.getOnafterampmselect() != null) {
            wb.callback("afterAmPmSelect", "function()", clockPicker.getOnafterampmselect());
        }

        encodeClientBehaviors(context, clockPicker);
        wb.finish();
    }

    protected static String getValueAsString(final FacesContext context, final ClockPicker clockPicker) {
        Object submittedValue = clockPicker.getSubmittedValue();
        if (submittedValue != null) {
            return submittedValue.toString();
        }

        Object value = clockPicker.getValue();
        if (value == null) {
            return null;
        }

        try {
            if (clockPicker.getConverter() != null) {
                return clockPicker.getConverter().getAsString(context, clockPicker, value);
            }
            else if (value instanceof LocalTime) {
                return clockPicker.isTwelveHour()
                            ? ((LocalTime) value).format(DateTimeFormatter.ofPattern("hh:mma").withLocale(clockPicker.calculateLocale(context)))
                            : ((LocalTime) value).format(FORMATTER_24_HOUR);
            }
        }
        catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component,
                Object value) throws ConverterException {
        final ClockPicker clockPicker = (ClockPicker) component;
        String submittedValue = Objects.toString(value, null);

        if (isValueBlank(submittedValue)) {
            return null;
        }

        // Delegate to user supplied converter if defined
        try {
            Converter converter = clockPicker.getConverter();
            if (converter != null) {
                return converter.getAsObject(context, clockPicker, submittedValue);
            }
        }
        catch (ConverterException e) {
            return submittedValue;
        }

        // Delegate to global defined converter (e.g. joda or java8)
        try {
            ValueExpression ve = clockPicker.getValueExpression("value");
            if (ve != null) {
                Class<?> type = ve.getType(context.getELContext());
                if (type != null && submittedValue != null && type.isAssignableFrom(LocalTime.class)) {
                    // Use built-in converter for LocalTime
                    return clockPicker.isTwelveHour()
                                ? LocalTime.parse(submittedValue, DateTimeFormatter.ofPattern("hh:mma").withLocale(clockPicker.calculateLocale(context)))
                                : LocalTime.parse(submittedValue, FORMATTER_24_HOUR);
                }
            }
        }
        catch (Exception e) {
            return submittedValue;
        }
        return submittedValue;
    }
}