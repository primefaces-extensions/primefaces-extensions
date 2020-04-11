/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.timepicker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.MessageFactory;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link TimePicker} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version $Revision$
 * @since 0.3
 */
public class TimePickerRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext fc, final UIComponent component) {
        final TimePicker timepicker = (TimePicker) component;

        if (!shouldDecode(timepicker)) {
            return;
        }

        final String param = timepicker.getClientId(fc) + "_input";
        final String submittedValue = fc.getExternalContext().getRequestParameterMap().get(param);

        if (submittedValue != null) {
            timepicker.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(fc, timepicker);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final TimePicker timepicker = (TimePicker) component;
        final String value = getValueAsString(fc, timepicker);

        encodeMarkup(fc, timepicker, value);
        encodeScript(fc, timepicker, value);
    }

    @Override
    public Object getConvertedValue(final FacesContext fc, final UIComponent component, final Object submittedValue)
                throws ConverterException {
        final String value = (String) submittedValue;
        if (LangUtils.isValueBlank(value)) {
            return null;
        }

        final TimePicker timepicker = (TimePicker) component;
        final Converter converter = timepicker.getConverter();

        // first ask the converter
        if (converter != null) {
            return converter.getAsObject(fc, timepicker, value);
        }

        // use built-in conversion
        try {
            final Class<?> type = resolveDateType(fc, timepicker);
            if (type == LocalTime.class) {
                final DateTimeFormatter formatter = getDateTimeFormatter(timepicker);
                return LocalTime.parse(value, formatter);
            }
            else {
                final SimpleDateFormat formatter = getSimpleDateFormat(timepicker);
                return formatter.parse(value);
            }
        }
        catch (final ParseException e) {
            throw new ConverterException(
                        MessageFactory.getMessage(timepicker.calculateLocale(),
                                    TimePicker.TIME_MESSAGE_KEY,
                                    value,
                                    getDateTimeFormatter(timepicker).format(LocalDateTime.now()),
                                    MessageFactory.getLabel(fc, component)),
                        e);
        }
        catch (final Exception e) {
            throw new ConverterException(e);
        }
    }

    protected void encodeMarkup(final FacesContext fc, final TimePicker timepicker, final String value)
                throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = timepicker.getClientId(fc);
        final String inputId = clientId + "_input";

        writer.startElement("span", timepicker);
        writer.writeAttribute("id", clientId, null);

        String containerClass = TimePicker.CONTAINER_CLASS;
        if (timepicker.isSpinner()) {
            containerClass += " ui-spinner";
        }
        if (timepicker.isShowOnButton()) {
            containerClass += " ui-inputgroup";
        }
        writer.writeAttribute(Attrs.CLASS, containerClass, null);

        if (timepicker.isInline()) {
            // inline container
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId + "_inline", null);
            writer.endElement("div");
        }

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", timepicker.isInline() ? "hidden" : "text", null);
        if (timepicker.getSize() > 0) {
            writer.writeAttribute("size", timepicker.getSize(), null);
        }
        writer.writeAttribute("autocomplete", "off", null);

        if (timepicker.isReadonlyInput()) {
            writer.writeAttribute("readonly", "readonly", null);
        }

        if (!LangUtils.isValueBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        if (!timepicker.isInline()) {
            String styleClass = timepicker.getStyleClass();
            styleClass = styleClass == null ? TimePicker.INPUT_CLASS : TimePicker.INPUT_CLASS + " " + styleClass;
            if (timepicker.isSpinner()) {
                styleClass += " ui-spinner-input";
            }
            if (timepicker.isShowOnButton()) {
                styleClass += " ui-inputtext";
            }
            if (!timepicker.isValid()) {
                styleClass += " ui-state-error";
            }

            writer.writeAttribute(Attrs.CLASS, styleClass, null);

            if (timepicker.getStyle() != null) {
                writer.writeAttribute(Attrs.STYLE, timepicker.getStyle(), null);
            }
        }

        renderAccessibilityAttributes(fc, timepicker);
        renderPassThruAttributes(fc, timepicker, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(fc, timepicker, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(fc, timepicker);

        writer.endElement("input");

        if (timepicker.isSpinner()) {
            final boolean disabled = timepicker.isDisabled() || timepicker.isReadonly();
            encodeSpinnerButton(fc, TimePicker.UP_BUTTON_CLASS, TimePicker.UP_ICON_CLASS, disabled);
            encodeSpinnerButton(fc, TimePicker.DOWN_BUTTON_CLASS, TimePicker.DOWN_ICON_CLASS, disabled);
        }

        if (timepicker.isShowOnButton()) {
            writer.startElement("button", null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_CLASS, null);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("role", "button", null);

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_ICON_CLASS, null);
            writer.endElement("span");

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_TEXT_CLASS, null);
            writer.write("ui-button");
            writer.endElement("span");

            writer.endElement("button");
        }

        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext fc, final TimePicker timepicker, final String value)
                throws IOException {
        final String clientId = timepicker.getClientId(fc);

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtTimePicker", timepicker.resolveWidgetVar(), clientId);
        wb.attr("timeSeparator", timepicker.getTimeSeparator());
        wb.attr("myPosition", timepicker.getDialogPosition());
        wb.attr("atPosition", timepicker.getInputPosition());
        wb.attr("showPeriod", timepicker.isShowPeriod());
        wb.attr("showPeriodLabels", timepicker.isShowPeriod());
        wb.attr("modeInline", timepicker.isInline());
        wb.attr("modeSpinner", timepicker.isSpinner());
        wb.nativeAttr("hours", "{starts:" + timepicker.getStartHours() + ",ends:" + timepicker.getEndHours() + "}");
        wb.nativeAttr("minutes", "{starts:" + timepicker.getStartMinutes() + ",ends:" + timepicker.getEndMinutes()
                    + ",interval:" + timepicker.getIntervalMinutes() + "}");
        wb.attr("rows", timepicker.getRows());
        wb.attr("showHours", timepicker.isShowHours());
        wb.attr("showMinutes", timepicker.isShowMinutes());
        wb.attr("showCloseButton", timepicker.isShowCloseButton());
        wb.attr("showNowButton", timepicker.isShowNowButton());
        wb.attr("showDeselectButton", timepicker.isShowDeselectButton());

        if (timepicker.getOnHourShow() != null) {
            wb.nativeAttr("onHourShow", timepicker.getOnHourShow());
        }

        if (timepicker.getOnMinuteShow() != null) {
            wb.nativeAttr("onMinuteShow", timepicker.getOnMinuteShow());
        }

        if (timepicker.isShowOnButton()) {
            wb.attr("showOn", timepicker.getShowOn());
            wb.selectorAttr("button", "#" + clientId + " .pe-timepicker-trigger");
        }

        wb.attr("locale", timepicker.calculateLocale().toString());
        wb.attr("disabled", timepicker.isDisabled() || timepicker.isReadonly());

        if (LangUtils.isValueBlank(value)) {
            wb.attr("defaultTime", Constants.EMPTY_STRING);
        }
        else if (timepicker.isInline()) {
            wb.attr("defaultTime", value);
        }

        if (timepicker.getMinHour() != null || timepicker.getMinMinute() != null) {
            wb.nativeAttr("minTime", "{hour:" + timepicker.getMinHour()
                        + ",minute:" + timepicker.getMinMinute() + "}");
        }

        if (timepicker.getMaxHour() != null || timepicker.getMaxMinute() != null) {
            wb.nativeAttr("maxTime", "{hour:" + timepicker.getMaxHour()
                        + ",minute:" + timepicker.getMaxMinute() + "}");
        }

        encodeClientBehaviors(fc, timepicker);

        wb.finish();
    }

    protected void encodeSpinnerButton(final FacesContext fc, String styleClass, final String iconClass,
                final boolean disabled)
                throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

        writer.startElement("a", null);

        writer.writeAttribute(Attrs.CLASS, styleClass, null);
        writer.startElement("span", null);
        writer.writeAttribute(Attrs.CLASS, "ui-button-text", null);
        writer.startElement("span", null);
        writer.writeAttribute(Attrs.CLASS, iconClass, null);
        writer.endElement("span");
        writer.endElement("span");

        writer.endElement("a");
    }

    protected String getValueAsString(final FacesContext fc, final TimePicker timepicker) {
        final Object submittedValue = timepicker.getSubmittedValue();
        if (submittedValue != null) {
            return submittedValue.toString();
        }

        final Object value = timepicker.getValue();
        if (value == null) {
            return null;
        }
        else {
            if (timepicker.getConverter() != null) {
                // convert via registered converter
                return timepicker.getConverter().getAsString(fc, timepicker, value);
            }
            else {
                // use built-in converter
                if (value instanceof LocalTime) {
                    final DateTimeFormatter formatter = getDateTimeFormatter(timepicker);
                    return formatter.format((LocalTime) value);
                }
                else {
                    final SimpleDateFormat formatter = getSimpleDateFormat(timepicker);
                    return formatter.format(value);
                }
            }
        }
    }

    protected String getPattern(final TimePicker timepicker) {
        return timepicker.isShowPeriod() ? timepicker.getTimePattern12() : timepicker.getTimePattern24();
    }

    protected DateTimeFormatter getDateTimeFormatter(final TimePicker timepicker) {
        return DateTimeFormatter.ofPattern(getPattern(timepicker), timepicker.calculateLocale());
    }

    protected SimpleDateFormat getSimpleDateFormat(final TimePicker timepicker) {
        return new SimpleDateFormat(getPattern(timepicker), timepicker.calculateLocale());
    }

    protected Class<?> resolveDateType(FacesContext context, TimePicker timePicker) {
        final ValueExpression ve = timePicker.getValueExpression("value");

        if (ve == null) {
            return null;
        }

        Class<?> type = ve.getType(context.getELContext());

        // If type could not be determined via value-expression try it this way. (Very unlikely, this happens in real world.)
        if (type == null) {
            type = LocalTime.class;
        }

        return type;
    }
}
