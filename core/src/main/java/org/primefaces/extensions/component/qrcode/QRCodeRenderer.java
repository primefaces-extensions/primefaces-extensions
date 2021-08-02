/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import java.io.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link QRCode} component.
 *
 * @author Mauricio Fenoglio / last modified by Melloware
 * @since 1.2.0
 */
public class QRCodeRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final QRCode qrCode = (QRCode) component;
        encodeMarkup(context, qrCode);
        encodeScript(context, qrCode);
    }

    protected void encodeScript(final FacesContext context, final QRCode qrCode) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtQRCode", qrCode);
        wb.attr("render", qrCode.getRenderMethod())
                    .attr("mode", qrCode.getRenderMode())
                    .attr("minVersion", qrCode.getMinVersion())
                    .attr("maxVersion", qrCode.getMaxVersion())
                    .attr("left", qrCode.getLeftOffset())
                    .attr("top", qrCode.getTopOffset())
                    .attr("size", qrCode.getSize())
                    .attr("fill", qrCode.getFillColor())
                    .attr("ecLevel", qrCode.getEcLevel())
                    .attr("background", qrCode.getBackground())
                    .attr("text", qrCode.getText())
                    .attr("radius", qrCode.getRadius())
                    .attr("quiet", qrCode.getQuiet())
                    .attr("mSize", qrCode.getMSize())
                    .attr("mPosX", qrCode.getMPosX())
                    .attr("mPosY", qrCode.getMPosY())
                    .attr(Attrs.LABEL, qrCode.getLabel())
                    .attr("fontname", qrCode.getFontName())
                    .attr("fontcolor", qrCode.getFontColor());

        wb.finish();
    }

    private void encodeMarkup(final FacesContext context, final QRCode qrCode) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = qrCode.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(QRCode.STYLE_CLASS)
                    .add(qrCode.getStyleClass())
                    .build();
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (qrCode.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, qrCode.getStyle(), Attrs.STYLE);
        }
        writer.endElement("span");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) {
        // do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
