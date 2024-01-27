/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

public class ClockPickerRenderer extends CoreRenderer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

        final String value = getValueAsString(context, clockPicker);
        encodeMarkup(context, clockPicker, value);
        encodeScript(context, clockPicker);
    }

    private void encodeMarkup(FacesContext context, ClockPicker clockPicker, final String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = clockPicker.getClientId();
        String inputId = clientId + "_input";

        writer.startElement("div", clockPicker);
        writer.writeAttribute("class", ClockPicker.CONTAINER_CLASS, null);

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("class", "ui-inputfield ui-widget ui-state-default ui-corner-all", null);
        writer.writeAttribute("size", 5, null);
        writer.writeAttribute("maxlength", 5, null);

        if (LangUtils.isNotBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        writer.endElement("input");

        writer.startElement("span", null);
        writer.writeAttribute("class", "input-group-addon", null);
        writer.startElement("span", null);
        writer.writeAttribute("class", "glyphicon glyphicon-time", null);
        writer.endElement("span");
        writer.endElement("span");
        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final ClockPicker clockPicker) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtClockPicker", clockPicker);
        wb.attr("placement", clockPicker.getPlacement(), "bottom");
        wb.attr("align", clockPicker.getAlign(), "left");
        wb.attr("autoclose", clockPicker.getAutoclose(), false);
        wb.attr("vibrate", clockPicker.getVibrate(), true);

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
                return FORMATTER.format((LocalTime) value);
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
                if (type != null && type != Object.class && type.isAssignableFrom(LocalTime.class)) {
                    // Use built-in converter for LocalTime
                    return LocalTime.parse(submittedValue, FORMATTER);
                }
            }
        }
        catch (Exception e) {
            return submittedValue;
        }
        return submittedValue;
    }
}