/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.speedtest;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentTraversalUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Speedtest} component.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
public class SpeedtestRenderer extends CoreRenderer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Speedtest speedtest = (Speedtest) component;
        encodeMarkup(context, speedtest);
        encodeScript(context, speedtest);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Speedtest speedtest) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = speedtest.getClientId(context);
        final String widgetVar = speedtest.resolveWidgetVar();
        // Generate Speedtest:
        writer.startElement("div", speedtest);
        writer.writeAttribute("id", clientId + "SpeedTest", "id");
        if (speedtest.getStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, speedtest.getStyleClass(), "styleClass");
        }
        if (speedtest.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, speedtest.getStyle(), Attrs.STYLE);
        }
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.startElement("div", speedtest);
        writer.writeAttribute(Attrs.CLASS, "ui-g", null);
        // Download Gauge
        writer.startElement("div", speedtest);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute("id", clientId + "ggdown", "id");
        writer.endElement("div");
        // Upload Gauge
        writer.startElement("div", speedtest);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute("id", clientId + "ggup", "id");
        writer.endElement("div");
        // Ping Gauge
        writer.startElement("div", speedtest);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute("id", clientId + "ggping", "id");
        writer.endElement("div");
        // Jitter Gauge
        writer.startElement("div", speedtest);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute("id", clientId + "ggjitter", "id");
        writer.endElement("div");
        // Row End
        writer.endElement("div");
        // Speedtest End
        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Speedtest speedtest) throws IOException {
        final String clientId = speedtest.getClientId(context);
        final UIComponent form = ComponentTraversalUtils.closestForm(context, speedtest);
        if (form == null) {
            throw new FacesException("Speedtest:" + clientId + " needs to be enclosed in a form component");
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtSpeedtest", speedtest);
        wb.attr("idDown", clientId + "ggdown");
        wb.attr("idUp", clientId + "ggup");
        wb.attr("idPing", clientId + "ggping");
        wb.attr("idJitter", clientId + "ggjitter");
        wb.attr("captionPing", speedtest.getCaptionPing());
        wb.attr("captionJitter", speedtest.getCaptionJitter());
        wb.attr("captionDownload", speedtest.getCaptionDownload());
        wb.attr("captionUpload", speedtest.getCaptionUpload());
        wb.attr("colorPing", speedtest.getColorPing());
        wb.attr("colorJitter", speedtest.getColorJitter());
        wb.attr("colorDownload", speedtest.getColorDownload());
        wb.attr("colorUpload", speedtest.getColorUpload());
        wb.attr("file", speedtest.getFile());
        encodeClientBehaviors(context, speedtest);
        wb.finish();
    }
}
