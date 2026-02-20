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
package org.primefaces.extensions.component.timepicker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.render.FacesRenderer;

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
@FacesRenderer(rendererType = TimePicker.DEFAULT_RENDERER, componentFamily = TimePicker.COMPONENT_FAMILY)
public class TimePickerRenderer extends InputRenderer<TimePicker> {

    private static final String BUTTON = "button";

    @Override
    public void decode(final FacesContext fc, final TimePicker component) {

        if (!shouldDecode(component)) {
            return;
        }

        final String param = component.getClientId(fc) + "_input";
        final String submittedValue = fc.getExternalContext().getRequestParameterMap().get(param);

        if (submittedValue != null) {
            component.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(fc, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final TimePicker component) throws IOException {
        final String value = getValueAsString(fc, component);

        encodeMarkup(fc, component, value);
        encodeScript(fc, component, value);
    }

    @Override
    public Object getConvertedValue(final FacesContext fc, final UIComponent component, final Object submittedValue) {
        final String value = (String) submittedValue;
        if (LangUtils.isBlank(value)) {
            return null;
        }

        final TimePicker timepicker = (TimePicker) component;
        final Converter<?> converter = timepicker.getConverter();

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
            throw new ConverterException(e.getMessage(), e);
        }
    }

    protected void encodeMarkup(final FacesContext fc, final TimePicker component, final String value)
                throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = component.getClientId(fc);
        final String inputId = clientId + "_input";

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, null);

        String containerClass = TimePicker.CONTAINER_CLASS;
        if (component.isSpinner()) {
            containerClass += " ui-spinner";
        }
        if (component.isShowOnButton()) {
            containerClass += " ui-inputgroup";
        }
        writer.writeAttribute(Attrs.CLASS, containerClass, null);

        if (component.isInline()) {
            // inline container
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId + "_inline", null);
            writer.endElement("div");
        }

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", component.isInline() ? "hidden" : "text", null);
        if (component.getSize() > 0) {
            writer.writeAttribute("size", component.getSize(), null);
        }
        writer.writeAttribute("autocomplete", "off", null);

        if (component.isReadonlyInput()) {
            writer.writeAttribute("readonly", "readonly", null);
        }

        if (LangUtils.isNotBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        if (!component.isInline()) {
            final String styleClass = getStyleClassBuilder(fc)
                        .add(TimePicker.INPUT_CLASS)
                        .add(component.getStyleClass())
                        .add(component.isSpinner(), "ui-spinner-input")
                        .add(component.isShowOnButton(), "ui-inputtext")
                        .add(!component.isValid(), "ui-state-error")
                        .build();

            writer.writeAttribute(Attrs.CLASS, styleClass, null);

            if (component.getStyle() != null) {
                writer.writeAttribute(Attrs.STYLE, component.getStyle(), null);
            }
        }

        renderAccessibilityAttributes(fc, component);
        renderPassThruAttributes(fc, component, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(fc, component, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(fc, component);

        writer.endElement("input");

        if (component.isSpinner()) {
            final boolean disabled = component.isDisabled() || component.isReadonly();
            encodeSpinnerButton(fc, TimePicker.UP_BUTTON_CLASS, TimePicker.UP_ICON_CLASS, disabled);
            encodeSpinnerButton(fc, TimePicker.DOWN_BUTTON_CLASS, TimePicker.DOWN_ICON_CLASS, disabled);
        }

        if (component.isShowOnButton()) {
            writer.startElement(BUTTON, null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_CLASS, null);
            writer.writeAttribute("type", BUTTON, null);
            writer.writeAttribute("role", BUTTON, null);

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_ICON_CLASS, null);
            writer.endElement("span");

            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, TimePicker.BUTTON_TRIGGER_TEXT_CLASS, null);
            writer.write("ui-button");
            writer.endElement("span");

            writer.endElement(BUTTON);
        }

        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext fc, final TimePicker component, final String value)
                throws IOException {
        final String clientId = component.getClientId(fc);

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtTimePicker", component);
        wb.attr("timeSeparator", component.getTimeSeparator());
        wb.attr("myPosition", component.getDialogPosition());
        wb.attr("atPosition", component.getInputPosition());
        wb.attr("showPeriod", component.isShowPeriod());
        wb.attr("showPeriodLabels", component.isShowPeriod());
        wb.attr("modeInline", component.isInline());
        wb.attr("modeSpinner", component.isSpinner());
        wb.nativeAttr("hours", "{starts:" + component.getStartHours() + ",ends:" + component.getEndHours() + "}");
        wb.nativeAttr("minutes", "{starts:" + component.getStartMinutes() + ",ends:" + component.getEndMinutes()
                    + ",interval:" + component.getIntervalMinutes() + "}");
        wb.attr("rows", component.getRows());
        wb.attr("showHours", component.isShowHours());
        wb.attr("showMinutes", component.isShowMinutes());
        wb.attr("showCloseButton", component.isShowCloseButton());
        wb.attr("showNowButton", component.isShowNowButton());
        wb.attr("showDeselectButton", component.isShowDeselectButton());

        if (component.getOnHourShow() != null) {
            wb.nativeAttr("onHourShow", component.getOnHourShow());
        }

        if (component.getOnMinuteShow() != null) {
            wb.nativeAttr("onMinuteShow", component.getOnMinuteShow());
        }

        if (component.isShowOnButton()) {
            wb.attr("showOn", component.getShowOn());
            wb.selectorAttr(BUTTON, "#" + clientId + " .pe-timepicker-trigger");
        }

        wb.attr("locale", component.calculateLocale().toString());
        wb.attr("disabled", component.isDisabled() || component.isReadonly());

        if (LangUtils.isBlank(value)) {
            wb.attr("defaultTime", Constants.EMPTY_STRING);
        }
        else if (component.isInline()) {
            wb.attr("defaultTime", value);
        }

        if (component.getMinHour() != null || component.getMinMinute() != null) {
            wb.nativeAttr("minTime", "{hour:" + component.getMinHour()
                        + ",minute:" + component.getMinMinute() + "}");
        }

        if (component.getMaxHour() != null || component.getMaxMinute() != null) {
            wb.nativeAttr("maxTime", "{hour:" + component.getMaxHour()
                        + ",minute:" + component.getMaxMinute() + "}");
        }

        encodeClientBehaviors(fc, component);

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

    protected String getValueAsString(final FacesContext fc, final TimePicker component) {
        final Object submittedValue = component.getSubmittedValue();
        if (submittedValue != null) {
            return submittedValue.toString();
        }

        final Object value = component.getValue();
        if (value == null) {
            return null;
        }
        else {
            if (component.getConverter() != null) {
                // convert via registered converter
                return component.getConverter().getAsString(fc, component, value);
            }
            else {
                // use built-in converter
                if (value instanceof LocalTime) {
                    final DateTimeFormatter formatter = getDateTimeFormatter(component);
                    return formatter.format((LocalTime) value);
                }
                else {
                    final SimpleDateFormat formatter = getSimpleDateFormat(component);
                    return formatter.format(value);
                }
            }
        }
    }

    protected String getPattern(final TimePicker component) {
        if (!component.isShowMinutes()) {
            return component.getTimePatternWithoutMinutes();
        }
        else if (!component.isShowHours()) {
            return component.getTimePatternWithoutHours();
        }
        else if (component.isShowPeriod()) {
            return component.getTimePattern12();
        }
        else {
            return component.getTimePattern24();
        }
    }

    protected DateTimeFormatter getDateTimeFormatter(final TimePicker component) {
        return DateTimeFormatter.ofPattern(getPattern(component), component.calculateLocale());
    }

    protected SimpleDateFormat getSimpleDateFormat(final TimePicker component) {
        return new SimpleDateFormat(getPattern(component), component.calculateLocale());
    }

    protected Class<?> resolveDateType(final FacesContext context, final TimePicker component) {
        final ValueExpression ve = component.getValueExpression("value");

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