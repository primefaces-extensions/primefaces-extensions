/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
 * $Id: $
 */

package org.primefaces.extensions.component.timeline;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * TimelineRenderer
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7 (reimplemented)
 */
public class TimelineRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		Timeline timeline = (Timeline) component;
		encodeMarkup(context, timeline);
		encodeScript(context, timeline);
	}

	protected void encodeMarkup(FacesContext context, Timeline timeline) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = timeline.getClientId(context);
		writer.startElement("div", timeline);

		writer.writeAttribute("id", clientId, "id");
		if (timeline.getStyle() != null) {
			writer.writeAttribute("style", timeline.getStyle(), "style");
		}

		if (timeline.getStyleClass() != null) {
			writer.writeAttribute("class", timeline.getStyleClass(), "styleClass");
		}

		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, Timeline timeline) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = timeline.getClientId(context);
		TimelineModel model = timeline.getValue();
		Calendar calendar = Calendar.getInstance(ComponentUtils.resolveTimeZone(timeline.getTimeZone()));

		WidgetBuilder wb = getWidgetBuilder(context);
		wb.widget("Timeline", timeline.resolveWidgetVar(), clientId, true);
		writer.write(",data:[");

		// encode events
		List<TimelineEvent> events = model.getEvents();
		int size = events != null ? events.size() : 0;
		for (int i = 0; i < size; i++) {
			writer.write(escapeText(encodeEvent(context, timeline, calendar, events.get(i))));
			if (i + 1 < size) {
				writer.write(",");
			}
		}

		writer.write("]");

		// encode options
		wb.attr("height", timeline.getHeight());
		wb.attr("minHeight", timeline.getMinHeight());
		wb.attr("width", timeline.getWidth());
		wb.attr("responsive", timeline.isResponsive());
		wb.attr("axisOnTop", timeline.isAxisOnTop());
		wb.attr("dragAreaWidth", timeline.getDragAreaWidth());
		wb.attr("editable", timeline.isEditable());
		wb.attr("selectable", timeline.isSelectable());
		wb.attr("zoomable", timeline.isZoomable());
		wb.attr("moveable", timeline.isMoveable());

		if (timeline.getStart() != null) {
			wb.attr("start", encodeDate(calendar, timeline.getStart()));
		}

		if (timeline.getEnd() != null) {
			wb.attr("end", encodeDate(calendar, timeline.getEnd()));
		}

		if (timeline.getMin() != null) {
			wb.attr("min", encodeDate(calendar, timeline.getMin()));
		}

		if (timeline.getMax() != null) {
			wb.attr("max", encodeDate(calendar, timeline.getMax()));
		}

		wb.attr("intervalMin", timeline.getZoomMin());
		wb.attr("intervalMax", timeline.getZoomMax());
		wb.attr("eventMargin", timeline.getEventMargin());
		wb.attr("eventMarginAxis", timeline.getEventMarginAxis());
		wb.attr("eventStyle", timeline.getEventStyle());
		wb.attr("groupsChangeable", timeline.isGroupsChangeable());
		wb.attr("groupsOnRight", timeline.isGroupsOnRight());

		if (timeline.getGroupsWidth() != null) {
			wb.attr("groupsWidth", timeline.getGroupsWidth());
		}

		wb.attr("snapEvents", timeline.isSnapEvents());
		wb.attr("stackEvents", timeline.isStackEvents());
		wb.attr("showCurrentTime", timeline.isShowCurrentTime());
		wb.attr("showMajorLabels", timeline.isShowMajorLabels());
		wb.attr("showMinorLabels", timeline.isShowMinorLabels());
		wb.attr("showButtonNew", timeline.isShowButtonNew());
		wb.attr("showNavigation", timeline.isShowNavigation());

		encodeClientBehaviors(context, timeline, wb);

		startScript(writer, clientId);
		writer.write(wb.build());
		endScript(writer);
	}

	public String encodeEvent(FacesContext context, Timeline timeline, Calendar calendar, TimelineEvent event)
	    throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		FastStringWriter fsw = new FastStringWriter();
		ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
		context.setResponseWriter(clonedWriter);

		clonedWriter.write("{\"id\":\"" + event.getId() + "\"");
		clonedWriter.write(",\"start\":" + encodeDate(calendar, event.getStartDate()));

		if (event.getEndDate() != null) {
			clonedWriter.write(",\"end\":" + encodeDate(calendar, event.getEndDate()));
		}

		clonedWriter.write(",\"editable\":" + event.isEditable());

		if (event.getGroup() != null) {
			clonedWriter.write(",\"group\":\"" + event.getGroup() + "\"");
		}

		if (StringUtils.isNotBlank(event.getStyleClass())) {
			clonedWriter.write(",\"styleClass\":\"" + event.getStyleClass() + "\"");
		}

		clonedWriter.write(",\"content\":\"");
		if (timeline.getChildCount() > 0) {
			if (StringUtils.isNotBlank(timeline.getVar())) {
				context.getExternalContext().getRequestMap().put(timeline.getVar(), event);
			}

			renderChildren(context, timeline);
		} else if (event.getData() != null) {
			clonedWriter.write(event.getData().toString());
		}

		clonedWriter.write("\"");
		clonedWriter.write("}");

		// restore the original writer
		context.setResponseWriter(writer);

		return fsw.toString();
	}

	private String encodeDate(Calendar calendar, Date date) {
		calendar.setTime(date);

		StringBuilder sb = new StringBuilder();
		sb.append("new Date(");
		calendar.get(Calendar.YEAR);
		sb.append(',');
		calendar.get(Calendar.MONTH);
		sb.append(',');
		calendar.get(Calendar.DAY_OF_MONTH);
		sb.append(',');
		calendar.get(Calendar.HOUR_OF_DAY);
		sb.append(',');
		calendar.get(Calendar.MINUTE);
		sb.append(',');
		calendar.get(Calendar.SECOND);
		sb.append(',');
		calendar.get(Calendar.MILLISECOND);
		sb.append(')');

		return sb.toString();
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//do nothing
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
