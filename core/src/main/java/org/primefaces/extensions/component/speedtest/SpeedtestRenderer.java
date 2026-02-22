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
package org.primefaces.extensions.component.speedtest;

import java.io.IOException;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

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
@FacesRenderer(rendererType = SpeedtestBase.DEFAULT_RENDERER, componentFamily = SpeedtestBase.COMPONENT_FAMILY)
public class SpeedtestRenderer extends CoreRenderer<Speedtest> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final FacesContext context, final Speedtest component) {
        decodeBehaviors(context, component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final Speedtest component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Speedtest component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String widgetVar = component.resolveWidgetVar();
        // Generate Speedtest:
        writer.startElement("div", component);
        writer.writeAttribute("id", clientId + "SpeedTest", "id");
        if (component.getStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, component.getStyleClass(), "styleClass");
        }
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.startElement("div", component);
        writer.writeAttribute(Attrs.CLASS, "ui-g", null);
        // Download Gauge
        writer.startElement("div", component);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute(Attrs.STYLE, "height:200px", null);
        writer.writeAttribute("id", clientId + "ggdown", "id");
        writer.endElement("div");
        // Upload Gauge
        writer.startElement("div", component);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute(Attrs.STYLE, "height:200px", null);
        writer.writeAttribute("id", clientId + "ggup", "id");
        writer.endElement("div");
        // Ping Gauge
        writer.startElement("div", component);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute(Attrs.STYLE, "height:200px", null);
        writer.writeAttribute("id", clientId + "ggping", "id");
        writer.endElement("div");
        // Jitter Gauge
        writer.startElement("div", component);
        writer.writeAttribute(Attrs.CLASS, "ui-g-3", null);
        writer.writeAttribute(Attrs.STYLE, "height:200px", null);
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
    private void encodeScript(final FacesContext context, final Speedtest component) throws IOException {
        final String clientId = component.getClientId(context);
        final UIComponent form = ComponentTraversalUtils.closestForm(component);
        if (form == null) {
            throw new FacesException("Speedtest:" + clientId + " needs to be enclosed in a form component");
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtSpeedtest", component);
        wb.attr("idDown", clientId + "ggdown");
        wb.attr("idUp", clientId + "ggup");
        wb.attr("idPing", clientId + "ggping");
        wb.attr("idJitter", clientId + "ggjitter");
        wb.attr("captionPing", component.getCaptionPing());
        wb.attr("captionJitter", component.getCaptionJitter());
        wb.attr("captionDownload", component.getCaptionDownload());
        wb.attr("captionUpload", component.getCaptionUpload());
        wb.attr("colorPing", component.getColorPing());
        wb.attr("colorJitter", component.getColorJitter());
        wb.attr("colorDownload", component.getColorDownload());
        wb.attr("colorUpload", component.getColorUpload());
        wb.attr("file", component.getFile());
        encodeClientBehaviors(context, component);
        wb.finish();
    }
}