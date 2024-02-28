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
package org.primefaces.extensions.component.inputpin;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link InputPin} component.
 *
 * @since 14.0.0
 */
public class InputPinRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputPin inputPin = (InputPin) component;

        if (!shouldDecode(inputPin)) {
            return;
        }

        final String inputId = inputPin.getClientId(context) + InputPin.HIDDEN_SUFFIX;
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            inputPin.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, inputPin);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final InputPin inputPin = (InputPin) component;

        if (inputPin.getSize() < 1) {
            throw new FacesException("InputPin size property is required.");
        }

        String valueToRender = ComponentUtils.getValueToRender(context, inputPin, inputPin.getValue());
        if (valueToRender == null) {
            valueToRender = Constants.EMPTY_STRING;
        }

        encodeMarkup(context, inputPin, valueToRender);
        encodeScript(context, inputPin);
    }

    protected void encodeMarkup(final FacesContext context, final InputPin inputPin, final String valueToRender)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputPin.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(InputPin.STYLE_CLASS, inputPin.getStyleClass())
                    .build();

        writer.startElement("span", inputPin);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (inputPin.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, inputPin.getStyle(), Attrs.STYLE);
        }

        encodeInput(context, inputPin, clientId, valueToRender);
        encodeHiddenInput(context, inputPin, clientId, valueToRender);

        writer.endElement("span");
    }

    protected void encodeInput(final FacesContext context, final InputPin inputPin, final String clientId,
                final String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputStyle = inputPin.getInputStyle();
        final String inputStyleClass = createStyleClass(inputPin, InputPin.PropertyKeys.inputStyleClass.name(), InputPin.CELL_STYLE_CLASS);
        final char[] chars = valueToRender.toCharArray();
        for (int i = 1; i <= inputPin.getSize(); i++) {

            if (i > 1 && !LangUtils.isBlank(inputPin.getSeparator())) {
                writer.writeText(inputPin.getSeparator(), InputPin.PropertyKeys.separator.name());
            }

            final String inputId = clientId + InputPin.INPUT_SUFFIX + i;
            final String inputValue = chars.length >= i ? String.valueOf(chars[i - 1]) : "";

            writer.startElement("input", null);
            writer.writeAttribute("id", inputId, null);
            writer.writeAttribute("name", inputId, null);
            writer.writeAttribute("value", inputValue, null);
            writer.writeAttribute("size", 1, null);
            writer.writeAttribute(Attrs.CLASS, inputStyleClass, null);

            if (LangUtils.isNotBlank(inputStyle)) {
                writer.writeAttribute(Attrs.STYLE, inputStyle, null);
            }

            if ("password".equalsIgnoreCase(inputPin.getType())) {
                writer.writeAttribute("type", inputPin.getType(), null);
            }

            if (inputPin.isNumeric() && LangUtils.isBlank(inputPin.getInputmode())) {
                inputPin.setInputmode("numeric");
            }

            if (LangUtils.isNotBlank(inputPin.getPlaceholder())) {
                char placeholder = inputPin.getPlaceholder().charAt((i - 1) % inputPin.getPlaceholder().length());
                writer.writeAttribute("placeholder", placeholder, null);
            }

            renderAccessibilityAttributes(context, inputPin);
            renderPassThruAttributes(context, inputPin, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
            renderDomEvents(context, inputPin, HTML.INPUT_TEXT_EVENTS);
            renderValidationMetadata(context, inputPin);

            writer.endElement("input");
        }
    }

    protected void encodeHiddenInput(final FacesContext context, final InputPin inputPin, final String clientId, final String valueToRender)
                throws IOException {
        renderHiddenInput(context, clientId + InputPin.HIDDEN_SUFFIX, valueToRender, inputPin.isDisabled());
    }

    protected void encodeScript(final FacesContext context, final InputPin inputPin) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPin", inputPin);
        wb.attr("numeric", inputPin.isNumeric(), false);
        if (LangUtils.isNotBlank(inputPin.getAriaLabel())) {
            wb.attr("ariaLabel", inputPin.getAriaLabel());
        }

        encodeClientBehaviors(context, inputPin);

        wb.finish();
    }

}
