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
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Timeline} component.
 *
 * @author Nilesh Namdeo Mali / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class TimelineRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        Timeline timeline = (Timeline) component;
        encodeMarkup(context, timeline);
        encodeScript(context, timeline);
    }

    protected void encodeMarkup(final FacesContext context, final Timeline component) throws IOException {
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

    protected void encodeScript(final FacesContext context, final Timeline component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        List<org.primefaces.extensions.model.timeline.Timeline> model =
                (List<org.primefaces.extensions.model.timeline.Timeline>) component.getValue();

        startScript(writer, clientId);
        writer.write("$(function() {");
        writer.write("PrimeFacesExt.cw('Timeline', '" + component.resolveWidgetVar() + "',{");
        writer.write("id:\"" + clientId + "\"");
        writer.write(",axisPosition:\"" + component.getAxisPosition() + "\"");
        writer.write(",eventStyle:\"" + component.getEventStyle() + "\"");
        writer.write(",height:\"" + component.getHeight() + "\"");
        writer.write(",width:\"" + component.getWidth() + "\"");
        writer.write(",showNavigation:" + component.getShowNavigation());
        if (!model.isEmpty()) {
            String groupName = null;
            boolean hasGroup = model.size() > 1;
            writer.write(",dataSource: [");
            for (Iterator<org.primefaces.extensions.model.timeline.Timeline> it = model.iterator(); it.hasNext(); ) {
                org.primefaces.extensions.model.timeline.Timeline timeline = it.next();
                groupName = (hasGroup) ? timeline.getTitle() : null;
                for (Iterator<TimelineEvent> eventIter = timeline.getEvents().iterator(); eventIter.hasNext(); ) {
                    encodeEvent(context, component, eventIter.next(), groupName, timeline.getId());

                    if (eventIter.hasNext()) {
                        writer.write(",");
                    }
                }
                if (it.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }
        encodeClientBehaviors(context, component);
        writer.write("},true);});");
        endScript(writer);
    }

    protected void encodeEvent(final FacesContext context, final Timeline component, final TimelineEvent event, final String groupName, final String timelineId)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("{");
        writer.write("\"timelineId\":" + "\"" + timelineId + "\"");
        writer.write(",\"id\":" + "\"" + event.getId() + "\"");
        writer.write(",\"start\":" + createJsDate(event.getStartDate().getTime()));
        writer.write(",\"content\":\"");
        if (component.getChildCount() > 0) {
            if (StringUtils.isNotBlank(component.getVar())) {
                context.getExternalContext().getRequestMap().put(component.getVar(), event);
            }
            FastStringWriter fsw = new FastStringWriter();
            ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
            context.setResponseWriter(clonedWriter);

            renderChildren(context, component);

            context.setResponseWriter(writer);
            writer.write(escapeText(fsw.toString()));
        } else {
            writer.write(escapeText(event.getTitle()));
        }
        writer.write("\"");

        if (event.getEndDate() != null) {
            writer.write(",\"end\":" + createJsDate(event.getEndDate().getTime()));
        }
        if (groupName != null) {
            writer.write(",\"group\":\"" + groupName + "\"");
        }
        if(StringUtils.isNotBlank(event.getStyleClass())){
            writer.write(",\"className\":\"" + event.getStyleClass() + "\"");
        }
        writer.write("}");

    }

    private static String createJsDate(long milliseconds) {
        StringBuilder sb = new StringBuilder(50);
        sb.append("new Date(");
        sb.append(milliseconds).append(')');
        return sb.toString();
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
