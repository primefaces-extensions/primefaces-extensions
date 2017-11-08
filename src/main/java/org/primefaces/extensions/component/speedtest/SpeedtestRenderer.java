/**
 * Copyright 2011-2017 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.speedtest;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentTraversalUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Renderer for the {@link Speedtest} component.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.1
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
            writer.writeAttribute("class", speedtest.getStyleClass(), "styleClass");
        }
        if (speedtest.getStyle() != null) {
            writer.writeAttribute("style", speedtest.getStyle(), "style");
        }
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.startElement("div",speedtest);
        writer.writeAttribute("class", "ui-g",null);
        // Download Gauge
        writer.startElement("div",speedtest);
        writer.writeAttribute("class", "ui-g-3",null);
        writer.writeAttribute("id", clientId + "ggdown", "id");
        writer.endElement("div");
        // Upload Gauge
        writer.startElement("div",speedtest);
        writer.writeAttribute("class", "ui-g-3",null);
        writer.writeAttribute("id", clientId + "ggup", "id");
        writer.endElement("div");
        // Ping Gauge
        writer.startElement("div",speedtest);
        writer.writeAttribute("class", "ui-g-3",null);
        writer.writeAttribute("id", clientId + "ggping", "id");
        writer.endElement("div");
        // Jitter Gauge
        writer.startElement("div",speedtest);
        writer.writeAttribute("class", "ui-g-3",null);
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
    private void encodeScript(FacesContext context, Speedtest speedtest) throws IOException {
        final String clientId = speedtest.getClientId(context);
        UIComponent form = ComponentTraversalUtils.closestForm(context, speedtest);
        if (form == null) {
            throw new FacesException("Speedtest:" + clientId + " needs to be enclosed in a form component");
        }
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("ExtSpeedtest", speedtest.resolveWidgetVar(), clientId);
        wb.attr("IdDown", clientId + "ggdown");
        wb.attr("IdUp", clientId + "ggup");
        wb.attr("IdPing", clientId + "ggping");
        wb.attr("IdJitter", clientId + "ggjitter");
        wb.attr("CaptionPing", speedtest.getCaptionPing());
        wb.attr("CaptionJitter", speedtest.getCaptionJitter());
        wb.attr("CaptionDownload", speedtest.getCaptionDownload());
        wb.attr("CaptionUpload", speedtest.getCaptionUpload());
        wb.attr("ColorPing", speedtest.getColorPing());
        wb.attr("ColorJitter", speedtest.getColorJitter());
        wb.attr("ColorDownload", speedtest.getColorDownload());
        wb.attr("ColorUpload", speedtest.getColorUpload());
        wb.attr("File", speedtest.getFile());
        encodeClientBehaviors(context, speedtest);
        wb.finish();
    }
}
