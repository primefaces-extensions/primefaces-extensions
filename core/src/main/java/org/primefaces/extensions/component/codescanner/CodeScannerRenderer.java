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
package org.primefaces.extensions.component.codescanner;

import java.io.IOException;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.component.api.InputHolder;
import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CodeScanner} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@FacesRenderer(rendererType = CodeScanner.DEFAULT_RENDERER, componentFamily = CodeScanner.COMPONENT_FAMILY)
public class CodeScannerRenderer extends CoreRenderer<CodeScanner> {

    @Override
    public void decode(final FacesContext context, final CodeScanner component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        encodeMarkup(context, codeScanner);
        encodeScript(context, codeScanner);
    }

    protected void encodeMarkup(final FacesContext context, final CodeScanner component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(CodeScanner.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();
        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        if (component.isVideo()) {
            encodeVideo(context, component);
        }

        writer.endElement("span");
    }

    protected void encodeVideo(final FacesContext context, final CodeScanner component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("video", null);
        if (component.getWidth() != null) {
            writer.writeAttribute("width", component.getWidth(), null);
        }
        if (component.getHeight() != null) {
            writer.writeAttribute("height", component.getHeight(), null);
        }
        writer.endElement("video");
    }

    protected void encodeScript(final FacesContext context, final CodeScanner component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCodeScanner", component)
                    .attr("type", component.getTypeEnum().name())
                    .attr("autoStart", component.isAutoStart());
        if (component.getDeviceId() != null) {
            wb.attr("deviceId", component.getDeviceId());
        }
        if (component.getFor() != null) {
            String forInputClientId = getForInputClientId(context, component);
            if (forInputClientId != null) {
                wb.attr("forInput", forInputClientId);
            }
        }
        if (component.getOnsuccess() != null) {
            wb.callback("onsuccess", "function()", component.getOnsuccess());
        }
        if (component.getOnerror() != null) {
            wb.callback("onerror", "function()", component.getOnerror());
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    protected String getForInputClientId(final FacesContext context, final CodeScanner component) {
        UIComponent forComponent = SearchExpressionUtils.resolveComponent(component.getFor(), component);
        if (forComponent == null) {
            return null;
        }
        if (forComponent instanceof InputHolder) {
            return ((InputHolder) forComponent).getInputClientId();
        }
        return forComponent.getClientId(context);
    }
}
