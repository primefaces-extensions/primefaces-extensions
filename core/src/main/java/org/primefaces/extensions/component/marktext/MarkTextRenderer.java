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
package org.primefaces.extensions.component.marktext;

import java.io.IOException;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

import com.google.gson.Gson;

/**
 * <code>MarkText</code> component renderer.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesRenderer(rendererType = MarkText.DEFAULT_RENDERER, componentFamily = MarkText.COMPONENT_FAMILY)
public class MarkTextRenderer extends CoreRenderer<MarkText> {

    @Override
    public void decode(final FacesContext context, final MarkText component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final MarkText component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final MarkText component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.STYLE, component.getStyle(), "style");
        writer.writeAttribute(Attrs.CLASS, component.getStyleClass(), Attrs.CLASS);
        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final MarkText component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtMarkText", component);

        wb.attr("value", component.getValue());
        wb.attr("forValue", findClientId(context, component, component.getFor()));
        wb.attr("caseSensitive", component.getCaseSensitive());
        wb.attr("separateWordSearch", component.getSeparateWordSearch());
        wb.attr("accuracy", component.getAccuracy());
        wb.attr("acrossElements", component.getAcrossElements());
        wb.attr("className", component.getStyleClass());
        wb.attr("hasActionListener", component.getActionListener() != null);
        if (component.getSynonyms() != null) {
            wb.nativeAttr("synonyms", new Gson().toJson(component.getSynonyms()));
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    protected String findClientId(final FacesContext context, final MarkText component, final String forValue) {
        if (forValue != null) {
            final UIComponent forComponent = component.findComponent(forValue);
            if (forComponent != null) {
                return forComponent.getClientId(context);
            }
        }
        return forValue;
    }
}
