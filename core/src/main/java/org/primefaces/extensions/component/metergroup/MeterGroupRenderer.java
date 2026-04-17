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
package org.primefaces.extensions.component.metergroup;

import java.io.IOException;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.model.metergroup.MeterGroupItem;
import org.primefaces.renderkit.CoreRenderer;

public class MeterGroupRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, javax.faces.component.UIComponent component) throws IOException {
        encodeMarkup(context, (MeterGroup) component);
    }

    protected void encodeMarkup(FacesContext context, MeterGroup meterGroup) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = meterGroup.getClientId(context);
        String style = meterGroup.getStyle();
        String orientation = meterGroup.getOrientation();
        String labelPosition = meterGroup.getLabelPosition();

        String styleClass = "ui-metergroup";
        styleClass += "vertical".equals(orientation) ? " ui-metergroup-vertical" : " ui-metergroup-horizontal";
        if (meterGroup.getStyleClass() != null) {
            styleClass += " " + meterGroup.getStyleClass();
        }

        writer.startElement("div", meterGroup);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        @SuppressWarnings("unchecked")
        Collection<MeterGroupItem> value = (Collection<MeterGroupItem>) meterGroup.getValue();

        if (value != null && !value.isEmpty()) {
            if ("start".equals(labelPosition) && meterGroup.isShowLabels()) {
                encodeLabels(context, meterGroup, value);
            }

            encodeMeters(context, meterGroup, value);

            if ("end".equals(labelPosition) && meterGroup.isShowLabels()) {
                encodeLabels(context, meterGroup, value);
            }
        }

        writer.endElement("div");
    }

    protected void encodeMeters(FacesContext context, MeterGroup meterGroup, Collection<MeterGroupItem> value)
                throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        double total = 0;
        for (MeterGroupItem item : value) {
            total += item.getValue();
        }

        double max = meterGroup.getMax();
        if (max == 0.0) {
            max = Math.max(100.0, total);
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-metergroup-meters", null);

        for (MeterGroupItem item : value) {
            double percent = (item.getValue() / max) * 100.0;
            // Bound it to avoid overflowing
            percent = Math.min(100.0, Math.max(0.0, percent));

            writer.startElement("span", null);
            writer.writeAttribute("class", "ui-metergroup-meter", null);

            String widthHeightSpanAttr = "horizontal".equals(meterGroup.getOrientation()) ? "width" : "height";
            String style = widthHeightSpanAttr + ": " + percent + "%;";
            if (item.getColor() != null) {
                style += " background-color: " + item.getColor() + ";";
            }
            writer.writeAttribute("style", style, null);

            writer.endElement("span");
        }

        writer.endElement("div");
    }

    protected void encodeLabels(FacesContext context, MeterGroup meterGroup, Collection<MeterGroupItem> value)
                throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String labelOrientation = meterGroup.getLabelOrientation();

        double total = 0;
        for (MeterGroupItem item : value) {
            total += item.getValue();
        }

        double max = meterGroup.getMax();
        if (max == 0.0) {
            max = Math.max(100.0, total);
        }

        writer.startElement("ul", null);
        String labelListClass = "ui-metergroup-labels";
        if ("vertical".equals(labelOrientation)) {
            labelListClass += " ui-metergroup-labels-vertical";
        }
        else {
            labelListClass += " ui-metergroup-labels-horizontal";
        }
        writer.writeAttribute("class", labelListClass, null);

        for (MeterGroupItem item : value) {
            writer.startElement("li", null);
            writer.writeAttribute("class", "ui-metergroup-label", null);

            if (item.getIcon() != null) {
                writer.startElement("i", null);
                writer.writeAttribute("class", "ui-metergroup-label-icon " + item.getIcon(), null);
                if (item.getColor() != null) {
                    writer.writeAttribute("style", "color: " + item.getColor() + ";", null);
                }
                writer.endElement("i");
            }
            else if (item.getColor() != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", "ui-metergroup-label-color", null);
                writer.writeAttribute("style", "background-color: " + item.getColor() + ";", null);
                writer.endElement("span");
            }

            writer.startElement("span", null);
            writer.writeAttribute("class", "ui-metergroup-label-text", null);

            double percent = (item.getValue() / max) * 100.0;
            String text = "";
            if (item.getLabel() != null) {
                text += item.getLabel() + " ";
            }
            text += "(" + Math.round(percent) + "%)";
            writer.writeText(text, null);

            writer.endElement("span");

            writer.endElement("li");
        }

        writer.endElement("ul");
    }
}
