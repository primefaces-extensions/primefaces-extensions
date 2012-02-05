/*
 * Copyright 2011 PrimeFaces Extensions.
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
 * 
 * $Id$
 */
package org.primefaces.extensions.component.timeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.timeline.Timeline;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.util.DateUtil;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Timeline} component.
 *
 * @author Nilesh Mali / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class TimelineRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UITimeline timeline = (UITimeline) component;
        String clientId = timeline.getClientId(context);
        startScript(writer, clientId);
        writer.write("$(function() {");
        writer.write(timeline.resolveWidgetVar() + " = new PrimeFacesExt.widget.Timeline({");
        writer.write("id: \"" + clientId + "\"");
        writer.write("});});");
        endScript(writer);
        encodeMarkup(context, timeline);
    }

    protected void encodeMarkup(final FacesContext context, UITimeline component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        List<Timeline> model = (List<Timeline>) component.getValue();
        StringBuilder sb = new StringBuilder();
        sb.append("$(function() {");
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", "ui-timeline ui-widget-content ui-corner-all", null);
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_scroll", null);
        int timelineIndex = 0;
        for (Iterator<Timeline> it = model.iterator(); it.hasNext();) {
            Timeline timeline = it.next();
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-timeline-menu", null);
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-timeline-menu-header ui-widget-header ui-corner-all", null);
            writer.write(timeline.getTitle());
            writer.endElement("div");
            writer.startElement("ul", null);
            writer.writeAttribute("class", "ui-timeline-event-list", null);
            int eventIndex = 0;
            for (TimelineEvent event : timeline.getEvents()) {
                String eventId = (clientId + "-" + event.getId()).replace('-', '_');
                writer.startElement("li", null);
                writer.writeAttribute("id", eventId, null);
                writer.writeAttribute("class", "ui-widget-content ui-corner-all", null);
                writer.startElement("span", null);
                writer.endElement("span");
                writer.startElement("div", null);
                writer.writeAttribute("id", eventId + "_content", null);
                writer.writeAttribute("class", "content", null);
                if (event.getStartDate() != null) {
                    writer.write(DateUtil.getLocalDateString(event.getStartDate()));
                    writer.write("<br/>");
                }
                if (event.getEndDate() != null) {
                    writer.write(DateUtil.getLocalDateString(event.getEndDate()));
                    writer.write("<br/>");
                }                
                if (event.getDescription() != null) {
                    writer.write(event.getDescription().toString());
                }
                writer.endElement("div");
                writer.write(event.getTitle());
                writer.endElement("li");

                sb.append(component.resolveWidgetVar()).append("_").append(eventId).append(" = new PrimeFacesExt.widget.BlockUI(\"").append(event.getId()).append("\",{");
                sb.append("source:\"#").append(eventId).append("\",");
                sb.append("target:\"#").append(clientId).append("\",");
                sb.append("content:\"#").append(eventId).append("_content\"");
                sb.append("});$(\"#");
                sb.append(eventId);
                sb.append("\").click(function(){");
                sb.append(component.resolveWidgetVar()).append("_").append(eventId).append(".block();");
                sb.append("$(\".blockOverlay\").attr(\"title\",\"Click to unblock\").click(");
                sb.append(component.resolveWidgetVar()).append("_").append(eventId).append(".unblock);");
                sb.append("});");
                eventIndex++;
            }
            writer.endElement("ul");
            writer.endElement("div");
            timelineIndex++;
        }
        writer.endElement("div");
        writer.endElement("div");
        sb.append("});");
        startScript(writer, clientId + "_2");
        writer.write(sb.toString());
        endScript(writer);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        //do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
