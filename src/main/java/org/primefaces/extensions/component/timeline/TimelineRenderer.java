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
        Timeline timeline = (Timeline) component;
        encodeMarkup(context, timeline);
        encodeScript(context, timeline);
    }

    protected void encodeMarkup(final FacesContext context, Timeline component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        if (component.getStyle() != null) {
            writer.writeAttribute("style", component.getStyle(), "style");
        }
        if (component.getStyleClass() != null) {
            writer.writeAttribute("class", component.getStyleClass(), "styleClass");
        }
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, Timeline component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        List<org.primefaces.extensions.model.timeline.Timeline> model = (List<org.primefaces.extensions.model.timeline.Timeline>) component.getValue();
        startScript(writer, clientId);
        writer.write("$(function() {");
        writer.write(component.resolveWidgetVar() + " = new PrimeFacesExt.widget.Timeline({");
        writer.write("id: \"" + clientId + "\"");
        if (!model.isEmpty()) {
            writer.write(", dataSource: [");
            for (Iterator<org.primefaces.extensions.model.timeline.Timeline> it = model.iterator(); it.hasNext();) {
                org.primefaces.extensions.model.timeline.Timeline timeline = it.next();
                String id = timeline.getId();
                writer.write("{");
                writer.write("\"id\":\"" + timeline.getId() + "\"");
                writer.write(",\"title\":\"" + timeline.getTitle() + "\"");

                //events
                writer.write(",\"events\":[");
                for (Iterator<TimelineEvent> eventIter = timeline.getEvents().iterator(); eventIter.hasNext();) {
                    encodeEvent(context, eventIter.next(), id);

                    if (eventIter.hasNext()) {
                        writer.write(",");
                    }
                }
                writer.write("]}");

                if (it.hasNext()) {
                    writer.write(",");
                }
            }
        }
        writer.write("]});});");
        endScript(writer);
    }

    protected void encodeEvent(FacesContext context, TimelineEvent event, String timelineId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("{");
        writer.write("\"id\":\"" + timelineId + "-" + event.getId() + "\"");
        writer.write(",\"title\":\"" + event.getTitle() + "\"");
        writer.write(",\"description\":\"" + event.getDescription() + "\"");
        writer.write(",\"startDate\":\"" + ((event.getStartDate() == null) ? "" : DateUtil.getLocalDateString(event.getStartDate())) + "\"");

        if (event.getEndDate() != null) {
            writer.write(",\"endDate\":\"" + ((event.getEndDate() == null) ? "" : DateUtil.getLocalDateString(event.getEndDate())) + "\"");
        }
        if (event.getIcon() != null) {
            writer.write(",\"icon\":\"" + getResourceURL(context, event.getIcon()) + "\"");
        }
        writer.write("}");
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
