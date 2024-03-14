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
package org.primefaces.extensions.component.inputplace;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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
public class InputPlaceRenderer extends InputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        InputPlace inputPlace = (InputPlace) component;

        if (!shouldDecode(inputPlace)) {
            return;
        }

        decodeBehaviors(context, inputPlace);

        String clientId = inputPlace.getClientId(context);
        String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId);

        if (submittedValue != null) {
            int maxlength = inputPlace.getMaxlength();
            if (maxlength > 0 && submittedValue.length() > maxlength) {
                submittedValue = LangUtils.substring(submittedValue, 0, maxlength);
            }
            inputPlace.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        InputPlace inputPlace = (InputPlace) component;

        encodeMarkup(context, inputPlace);
        encodeScript(context, inputPlace);
    }

    protected void encodeScript(FacesContext context, InputPlace inputPlace) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPlace", inputPlace)
                    .attr("restrictTypes", inputPlace.getRestrictTypes(), null)
                    .attr("restrictCountries", inputPlace.getRestrictCountries(), null)
                    .callback("onPlaceChanged", "function(place)", inputPlace.getOnplacechanged());

        encodeClientBehaviors(context, inputPlace);
        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, InputPlace inputPlace) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = inputPlace.getClientId(context);

        writer.startElement("input", inputPlace);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "text", null);

        String valueToRender = ComponentUtils.getValueToRender(context, inputPlace);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }

        if (inputPlace.getStyle() != null) {
            writer.writeAttribute("style", inputPlace.getStyle(), null);
        }

        writer.writeAttribute("class", createStyleClass(inputPlace, InputPlace.STYLE_CLASS), "styleClass");

        renderAccessibilityAttributes(context, inputPlace);
        renderRTLDirection(context, inputPlace);
        renderPassThruAttributes(context, inputPlace, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputPlace, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, inputPlace);

        writer.endElement("input");
    }

}