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
		TimelineModel model = timeline.getValue();
		if (model == null) {
			return;
		}

		ResponseWriter writer = context.getResponseWriter();
		String clientId = timeline.getClientId(context);
		Calendar calendar = Calendar.getInstance(ComponentUtils.resolveTimeZone(timeline.getTimeZone()));
		FastStringWriter fsw = new FastStringWriter();
		FastStringWriter fswHtml = new FastStringWriter();

		startScript(writer, clientId);
		writer.write("$(function(){");
		writer.write("PrimeFacesExt.cw('Timeline','" + timeline.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",data:[");

		// encode events
		List<TimelineEvent> events = model.getEvents();
		int size = events != null ? events.size() : 0;
		for (int i = 0; i < size; i++) {
			writer.write(encodeEvent(context, fsw, fswHtml, timeline, calendar, events.get(i)));
			if (i + 1 < size) {
				writer.write(",");
			}
		}

		writer.write("],opts:{");

		// encode options
		writer.write("height:'" + timeline.getHeight() + "'");
		writer.write(",minHeight:" + timeline.getMinHeight());
		writer.write(",width:'" + timeline.getWidth() + "'");
		writer.write(",responsive:" + timeline.isResponsive());
		writer.write(",axisOnTop:" + timeline.isAxisOnTop());
		writer.write(",dragAreaWidth:" + timeline.getDragAreaWidth());
		writer.write(",editable:" + timeline.isEditable());
		writer.write(",selectable:" + timeline.isSelectable());
		writer.write(",zoomable:" + timeline.isZoomable());
		writer.write(",moveable:" + timeline.isMoveable());

		if (timeline.getStart() != null) {
			writer.write(",start:" + encodeDate(calendar, timeline.getStart()));
		}

		if (timeline.getEnd() != null) {
			writer.write(",end:" + encodeDate(calendar, timeline.getEnd()));
		}

		if (timeline.getMin() != null) {
			writer.write(",min:" + encodeDate(calendar, timeline.getMin()));
		}

		if (timeline.getMax() != null) {
			writer.write(",max:" + encodeDate(calendar, timeline.getMax()));
		}

		writer.write(",intervalMin:" + timeline.getZoomMin());
		writer.write(",intervalMax:" + timeline.getZoomMax());
		writer.write(",eventMargin:" + timeline.getEventMargin());
		writer.write(",eventMarginAxis:" + timeline.getEventMarginAxis());
		writer.write(",style:'" + timeline.getEventStyle() + "'");
		writer.write(",groupsChangeable:" + timeline.isGroupsChangeable());
		writer.write(",groupsOnRight:" + timeline.isGroupsOnRight());

		if (timeline.getGroupsWidth() != null) {
			writer.write(",groupsWidth:'" + timeline.getGroupsWidth() + "'");
		}

		writer.write(",snapEvents:" + timeline.isSnapEvents());
		writer.write(",stackEvents:" + timeline.isStackEvents());
		writer.write(",showCurrentTime:" + timeline.isShowCurrentTime());
		writer.write(",showMajorLabels:" + timeline.isShowMajorLabels());
		writer.write(",showMinorLabels:" + timeline.isShowMinorLabels());
		writer.write(",showButtonNew:" + timeline.isShowButtonNew());
		writer.write(",showNavigation:" + timeline.isShowNavigation());
		writer.write("}");

		encodeClientBehaviors(context, timeline);

		writer.write("},true);});");
		endScript(writer);
	}

	public String encodeEvent(FacesContext context, FastStringWriter fsw, FastStringWriter fswHtml, Timeline timeline,
	                          Calendar calendar, TimelineEvent event) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		fsw.write("{\"start\":" + encodeDate(calendar, event.getStartDate()));

		if (event.getEndDate() != null) {
			fsw.write(",\"end\":" + encodeDate(calendar, event.getEndDate()));
		}

		if (event.isEditable() != null) {
			fsw.write(",\"editable\":" + event.isEditable());
		}

		if (event.getGroup() != null) {
			fsw.write(",\"group\":\"" + event.getGroup() + "\"");
		}

		if (StringUtils.isNotBlank(event.getStyleClass())) {
			fsw.write(",\"className\":\"" + event.getStyleClass() + "\"");
		}

		fsw.write(",\"content\":\"");
		if (timeline.getChildCount() > 0) {
			Object data = event.getData();
			if (StringUtils.isNotBlank(timeline.getVar()) && data != null) {
				context.getExternalContext().getRequestMap().put(timeline.getVar(), data);
			}

			ResponseWriter clonedWriter = writer.cloneWithWriter(fswHtml);
			context.setResponseWriter(clonedWriter);

			renderChildren(context, timeline);

			// restore writer
			context.setResponseWriter(writer);
			fsw.write(ComponentUtils.escapeHtmlTextInJson(fswHtml.toString()));
			fswHtml.reset();
		} else if (event.getData() != null) {
			fsw.write(event.getData().toString());
		}

		fsw.write("\"");
		fsw.write("}");

		String eventJson = fsw.toString();
		fsw.reset();

		return eventJson;
	}

	private String encodeDate(Calendar calendar, Date date) {
		calendar.setTime(date);

		StringBuilder sb = new StringBuilder();
		sb.append("new Date(");
		sb.append(calendar.get(Calendar.YEAR));
		sb.append(',');
		sb.append(calendar.get(Calendar.MONTH));
		sb.append(',');
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(',');
		sb.append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append(',');
		sb.append(calendar.get(Calendar.MINUTE));
		sb.append(',');
		sb.append(calendar.get(Calendar.SECOND));
		sb.append(',');
		sb.append(calendar.get(Calendar.MILLISECOND));
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
