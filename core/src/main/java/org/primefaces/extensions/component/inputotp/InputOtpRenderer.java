/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.component.inputotp;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link InputOtp} component.
 *
 * @since 14.0.0
 */
public class InputOtpRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputOtp inputOtp = (InputOtp) component;

        if (!shouldDecode(inputOtp)) {
            return;
        }

        final String inputId = inputOtp.getClientId(context) + InputOtp.HIDDEN_SUFFIX;
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            inputOtp.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, inputOtp);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final InputOtp inputOtp = (InputOtp) component;

        if (inputOtp.getLength() < 1) {
            throw new FacesException("InputOtp length property should be > 0");
        }

        String valueToRender = ComponentUtils.getValueToRender(context, inputOtp, inputOtp.getValue());
        if (valueToRender == null) {
            valueToRender = Constants.EMPTY_STRING;
        }

        encodeMarkup(context, inputOtp, valueToRender);
        encodeScript(context, inputOtp);
    }

    protected void encodeMarkup(final FacesContext context, final InputOtp inputOtp, final String valueToRender)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputOtp.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(InputOtp.STYLE_CLASS, inputOtp.getStyleClass())
                    .add(ComponentUtils.isRTL(context, inputOtp), InputOtp.RTL_STYLE_CLASS)
                    .build();

        writer.startElement("span", inputOtp);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (inputOtp.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, inputOtp.getStyle(), Attrs.STYLE);
        }

        encodeInput(context, inputOtp, clientId, valueToRender);
        encodeHiddenInput(context, inputOtp, clientId, valueToRender);

        writer.endElement("span");
    }

    protected void encodeInput(final FacesContext context, final InputOtp inputOtp, final String clientId,
                final String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputStyle = inputOtp.getInputStyle();
        final String inputStyleClass = createStyleClass(inputOtp, InputOtp.PropertyKeys.inputStyleClass.name(), InputOtp.CELL_STYLE_CLASS);
        final char[] chars = valueToRender.toCharArray();
        final boolean hasSeparatorFacet = FacetUtils.shouldRenderFacet(inputOtp.getFacet("separator"));
        for (int i = 1; i <= inputOtp.getLength(); i++) {

            if (i > 1 && (LangUtils.isNotBlank(inputOtp.getSeparator()) || hasSeparatorFacet)) {
                writer.startElement("div", null);
                writer.writeAttribute(Attrs.CLASS, InputOtp.SEPARATOR_STYLE_CLASS, null);
                if (hasSeparatorFacet) {
                    inputOtp.getFacet("separator").encodeAll(context);
                }
                else {
                    writer.writeText(inputOtp.getSeparator(), InputOtp.PropertyKeys.separator.name());
                }
                writer.endElement("div");
            }

            final String inputId = clientId + InputOtp.INPUT_SUFFIX + i;
            final String inputValue = chars.length >= i ? String.valueOf(chars[i - 1]) : "";

            writer.startElement("input", null);
            writer.writeAttribute("id", inputId, null);
            writer.writeAttribute("name", inputId, null);
            writer.writeAttribute("value", inputValue, null);
            writer.writeAttribute("size", 1, null);
            writer.writeAttribute("maxlength", inputOtp.getLength(), null);
            writer.writeAttribute(Attrs.CLASS, inputStyleClass, null);

            if (LangUtils.isNotBlank(inputStyle)) {
                writer.writeAttribute(Attrs.STYLE, inputStyle, null);
            }

            if (inputOtp.isMask()) {
                writer.writeAttribute("type", "password", null);
            }

            if (inputOtp.isIntegerOnly() && LangUtils.isBlank(inputOtp.getInputmode())) {
                inputOtp.setInputmode("numeric");
            }

            if (LangUtils.isNotBlank(inputOtp.getPlaceholder())) {
                char placeholder = inputOtp.getPlaceholder().charAt((i - 1) % inputOtp.getPlaceholder().length());
                writer.writeAttribute("placeholder", placeholder, null);
            }

            renderAccessibilityAttributes(context, inputOtp);
            renderRTLDirection(context, inputOtp);
            renderPassThruAttributes(context, inputOtp, InputOtp.INPUT_OTP_ATTRIBUTES_WITHOUT_EVENTS);
            renderDomEvents(context, inputOtp, HTML.INPUT_TEXT_EVENTS);
            renderValidationMetadata(context, inputOtp);

            writer.endElement("input");
        }
    }

    protected void encodeHiddenInput(final FacesContext context, final InputOtp inputOtp, final String clientId, final String valueToRender)
                throws IOException {
        renderHiddenInput(context, clientId + InputOtp.HIDDEN_SUFFIX, valueToRender, inputOtp.isDisabled());
    }

    protected void encodeScript(final FacesContext context, final InputOtp inputOtp) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputOtp", inputOtp);
        wb.attr("integerOnly", inputOtp.isIntegerOnly(), false);
        if (LangUtils.isNotBlank(inputOtp.getAriaLabel())) {
            wb.attr("ariaLabel", inputOtp.getAriaLabel());
        }

        encodeClientBehaviors(context, inputOtp);

        wb.finish();
    }

}