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
package org.primefaces.extensions.component.inputplace;

import java.io.IOException;
import java.util.Locale;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link InputPlace} component.
 *
 * @since 14.0.0
 */
@FacesRenderer(rendererType = InputPlace.DEFAULT_RENDERER, componentFamily = InputPlace.COMPONENT_FAMILY)
public class InputPlaceRenderer extends InputRenderer<InputPlace> {

    @Override
    public void decode(FacesContext context, InputPlace component) {

        if (!shouldDecode(component)) {
            return;
        }

        decodeBehaviors(context, component);

        String clientId = component.getClientId(context);
        String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId);

        if (submittedValue != null) {
            int maxlength = component.getMaxlength();
            if (maxlength > 0 && submittedValue.length() > maxlength) {
                submittedValue = LangUtils.substring(submittedValue, 0, maxlength);
            }
            component.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, InputPlace component) throws IOException {

        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeScript(FacesContext context, InputPlace component) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPlace", component)
                    .attr("apiType", component.getApiType().toLowerCase(Locale.ROOT))
                    .attr("apiKey", component.getApiKey(), null)
                    .attr("restrictTypes", component.getRestrictTypes(), null)
                    .attr("restrictCountries", component.getRestrictCountries(), null)
                    .callback("onPlaceChanged", "function(place)", component.getOnplacechanged());

        encodeClientBehaviors(context, component);
        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, InputPlace component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);

        writer.startElement("input", component);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "text", null);

        String valueToRender = ComponentUtils.getValueToRender(context, component);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }

        if (component.getStyle() != null) {
            writer.writeAttribute("style", component.getStyle(), null);
        }

        writer.writeAttribute("class", createStyleClass(component, InputPlace.STYLE_CLASS), "styleClass");

        renderAccessibilityAttributes(context, component);
        renderRTLDirection(context, component);
        renderPassThruAttributes(context, component, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, component, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, component);

        writer.endElement("input");
    }

}