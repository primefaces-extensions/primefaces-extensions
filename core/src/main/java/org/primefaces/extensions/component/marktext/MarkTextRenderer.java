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
package org.primefaces.extensions.component.marktext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>MarkText</code> component.
 *
 * @author jxmai
 * @since 15.0.13
 */
public class MarkTextRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final MarkText markText = (MarkText) component;
        encodeMarkup(context, markText);
        encodeScript(context, markText);
    }

    protected void encodeMarkup(final FacesContext context, final MarkText markText) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = markText.getClientId(context);

        writer.startElement("span", markText);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.STYLE, markText.getStyle(), "style");
        writer.writeAttribute(Attrs.CLASS, markText.getStyleClass(), Attrs.CLASS);
        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final MarkText markText) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtMarkText", markText);

        wb.attr("value", markText.getValue());
        wb.attr("forValue", findClientId(context, markText, markText.getFor()));
        wb.attr("caseSensitive", markText.getCaseSensitive());
        wb.attr("separateWordSearch", markText.getSeparateWordSearch());
        wb.attr("accuracy", markText.getAccuracy());
        wb.attr("className", markText.getStyleClass());
        wb.attr("hasActionListener", markText.getActionListener() != null);

        encodeClientBehaviors(context, markText);

        wb.finish();
    }

    protected String findClientId(final FacesContext context, final MarkText markText, final String forValue) {
        if (forValue != null) {
            final UIComponent forComponent = markText.findComponent(forValue);
            if (forComponent != null) {
                return forComponent.getClientId(context);
            }
        }
        return forValue;
    }
}