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
package org.primefaces.extensions.component.qrcode;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the QRCode component.
 *
 * @since 1.2.0
 */
@FacesRenderer(rendererType = QRCodeBase.DEFAULT_RENDERER, componentFamily = QRCodeBase.COMPONENT_FAMILY)
public class QRCodeRenderer extends CoreRenderer<QRCode> {

    @Override
    public void encodeEnd(final FacesContext context, final QRCode component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeScript(final FacesContext context, final QRCode component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtQRCode", component);
        wb.attr("render", component.getRenderMethod())
                    .attr("mode", component.getRenderMode())
                    .attr("minVersion", component.getMinVersion())
                    .attr("maxVersion", component.getMaxVersion())
                    .attr("left", component.getLeftOffset())
                    .attr("top", component.getTopOffset())
                    .attr("size", component.getSize())
                    .attr("fill", component.getFillColor())
                    .attr("ecLevel", component.getEcLevel())
                    .attr("background", component.getBackground())
                    .attr("text", component.getText())
                    .attr("radius", component.getRadius())
                    .attr("quiet", component.getQuiet())
                    .attr("mSize", component.getLabelSize())
                    .attr("mPosX", component.getLabelPosX())
                    .attr("mPosY", component.getLabelPosY())
                    .attr(Attrs.LABEL, component.getLabel())
                    .attr("fontname", component.getFontName())
                    .attr("fontcolor", component.getFontColor());

        wb.finish();
    }

    private void encodeMarkup(final FacesContext context, final QRCode component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(QRCode.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }
        writer.endElement("span");
    }

    @Override
    public void encodeChildren(final FacesContext context, final QRCode component) {
        // do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
