/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.InputHolder;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CodeScanner} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
public class CodeScannerRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final CodeScanner codeScanner = (CodeScanner) component;
        encodeMarkup(context, codeScanner);
        encodeScript(context, codeScanner);
    }

    protected void encodeMarkup(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = codeScanner.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(CodeScanner.STYLE_CLASS)
                    .add(codeScanner.getStyleClass())
                    .build();
        writer.startElement("span", codeScanner);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (codeScanner.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, codeScanner.getStyle(), Attrs.STYLE);
        }

        if (codeScanner.isVideo()) {
            encodeVideo(context, codeScanner);
        }

        writer.endElement("span");
    }

    protected void encodeVideo(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("video", null);
        if (codeScanner.getWidth() != null) {
            writer.writeAttribute("width", codeScanner.getWidth(), null);
        }
        if (codeScanner.getHeight() != null) {
            writer.writeAttribute("height", codeScanner.getHeight(), null);
        }
        writer.endElement("video");
    }

    protected void encodeScript(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCodeScanner", codeScanner)
                    .attr("type", codeScanner.getTypeEnum().name())
                    .attr("autoStart", codeScanner.isAutoStart());
        if (codeScanner.getDeviceId() != null) {
            wb.attr("deviceId", codeScanner.getDeviceId());
        }
        if (codeScanner.getFor() != null) {
            String forInputClientId = getForInputClientId(context, codeScanner);
            if (forInputClientId != null) {
                wb.attr("forInput", forInputClientId);
            }
        }
        if (codeScanner.getOnsuccess() != null) {
            wb.callback("onsuccess", "function()", codeScanner.getOnsuccess());
        }
        if (codeScanner.getOnerror() != null) {
            wb.callback("onerror", "function()", codeScanner.getOnerror());
        }

        encodeClientBehaviors(context, codeScanner);

        wb.finish();
    }

    protected String getForInputClientId(final FacesContext context, final CodeScanner codeScanner) {
        UIComponent forComponent = SearchExpressionFacade.resolveComponent(context, codeScanner, codeScanner.getFor());
        if (forComponent == null) {
            return null;
        }
        if (forComponent instanceof InputHolder) {
            return ((InputHolder) forComponent).getInputClientId();
        }
        return forComponent.getClientId(context);
    }

}
