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
package org.primefaces.extensions.component.inputotp;

import java.io.IOException;

import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

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
@FacesRenderer(rendererType = InputOtp.DEFAULT_RENDERER, componentFamily = InputOtp.COMPONENT_FAMILY)
public class InputOtpRenderer extends InputRenderer<InputOtp> {

    @Override
    public void decode(final FacesContext context, final InputOtp component) {

        if (!shouldDecode(component)) {
            return;
        }

        final String inputId = component.getClientId(context) + InputOtp.HIDDEN_SUFFIX;
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            component.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final InputOtp component) throws IOException {

        if (component.getLength() < 1) {
            throw new FacesException("InputOtp length property should be > 0");
        }

        String valueToRender = ComponentUtils.getValueToRender(context, component, component.getValue());
        if (valueToRender == null) {
            valueToRender = Constants.EMPTY_STRING;
        }

        encodeMarkup(context, component, valueToRender);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final InputOtp component, final String valueToRender)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(InputOtp.STYLE_CLASS, component.getStyleClass())
                    .add(ComponentUtils.isRTL(context, component), InputOtp.RTL_STYLE_CLASS)
                    .build();

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        encodeInput(context, component, clientId, valueToRender);
        encodeHiddenInput(context, component, clientId, valueToRender);

        writer.endElement("span");
    }

    protected void encodeInput(final FacesContext context, final InputOtp component, final String clientId,
                final String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputStyle = component.getInputStyle();
        final String inputStyleClass = createStyleClass(component, "inputStyleClass", InputOtp.CELL_STYLE_CLASS);
        final char[] chars = valueToRender.toCharArray();
        final boolean hasSeparatorFacet = FacetUtils.shouldRenderFacet(component.getFacet("separator"));
        for (int i = 1; i <= component.getLength(); i++) {

            if (i > 1 && (LangUtils.isNotBlank(component.getSeparator()) || hasSeparatorFacet)) {
                writer.startElement("div", null);
                writer.writeAttribute(Attrs.CLASS, InputOtp.SEPARATOR_STYLE_CLASS, null);
                if (hasSeparatorFacet) {
                    component.getFacet("separator").encodeAll(context);
                }
                else {
                    writer.writeText(component.getSeparator(), "separator");
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
            writer.writeAttribute("maxlength", component.getLength(), null);
            writer.writeAttribute(Attrs.CLASS, inputStyleClass, null);

            if (LangUtils.isNotBlank(inputStyle)) {
                writer.writeAttribute(Attrs.STYLE, inputStyle, null);
            }

            if (component.isMask()) {
                writer.writeAttribute("type", "password", null);
            }

            if (component.isIntegerOnly() && LangUtils.isBlank(component.getInputmode())) {
                component.setInputmode("numeric");
            }

            if (LangUtils.isNotBlank(component.getPlaceholder())) {
                char placeholder = component.getPlaceholder().charAt((i - 1) % component.getPlaceholder().length());
                writer.writeAttribute("placeholder", placeholder, null);
            }

            renderAccessibilityAttributes(context, component);
            renderRTLDirection(context, component);
            renderPassThruAttributes(context, component, InputOtp.INPUT_OTP_ATTRIBUTES_WITHOUT_EVENTS);
            renderDomEvents(context, component, HTML.INPUT_TEXT_EVENTS);
            renderValidationMetadata(context, component);

            writer.endElement("input");
        }
    }

    protected void encodeHiddenInput(final FacesContext context, final InputOtp component, final String clientId, final String valueToRender)
                throws IOException {
        renderHiddenInput(context, clientId + InputOtp.HIDDEN_SUFFIX, valueToRender, component.isDisabled());
    }

    protected void encodeScript(final FacesContext context, final InputOtp component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputOtp", component);
        wb.attr("integerOnly", component.isIntegerOnly(), false);
        if (LangUtils.isNotBlank(component.getAriaLabel())) {
            wb.attr("ariaLabel", component.getAriaLabel());
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

}
