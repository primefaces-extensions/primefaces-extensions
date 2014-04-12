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

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.DateUtils;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

		TimeZone targetTZ = ComponentUtils.resolveTimeZone(timeline.getTimeZone());
		TimeZone browserTZ = ComponentUtils.resolveTimeZone(timeline.getBrowserTimeZone());

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
			writer.write(encodeEvent(context, fsw, fswHtml, timeline, browserTZ, targetTZ, events.get(i)));
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
		writer.write(",unselectable:" + timeline.isUnselectable());
		writer.write(",zoomable:" + timeline.isZoomable());
		writer.write(",moveable:" + timeline.isMoveable());
        writer.write(",timeChangeable:" + timeline.isTimeChangeable());

		if (timeline.getStart() != null) {
			writer.write(",start:" + encodeDate(browserTZ, targetTZ, timeline.getStart()));
		}

		if (timeline.getEnd() != null) {
			writer.write(",end:" + encodeDate(browserTZ, targetTZ, timeline.getEnd()));
		}

		if (timeline.getMin() != null) {
			writer.write(",min:" + encodeDate(browserTZ, targetTZ, timeline.getMin()));
		}

		if (timeline.getMax() != null) {
			writer.write(",max:" + encodeDate(browserTZ, targetTZ, timeline.getMax()));
		}

		writer.write(",zoomMin:" + timeline.getZoomMin());
		writer.write(",zoomMax:" + timeline.getZoomMax());

		if (timeline.getPreloadFactor() < 0) {
			writer.write(",preloadFactor:0");
		} else {
			writer.write(",preloadFactor:" + timeline.getPreloadFactor());
		}

		writer.write(",eventMargin:" + timeline.getEventMargin());
		writer.write(",eventMarginAxis:" + timeline.getEventMarginAxis());
		writer.write(",style:'" + timeline.getEventStyle() + "'");
		writer.write(",groupsChangeable:" + timeline.isGroupsChangeable());        
		writer.write(",groupsOnRight:" + timeline.isGroupsOnRight());
        writer.write(",groupsOrder:" + timeline.isGroupsOrder());

		if (timeline.getGroupsWidth() != null) {
			writer.write(",groupsWidth:'" + timeline.getGroupsWidth() + "'");
		}

		writer.write(",snapEvents:" + timeline.isSnapEvents());
		writer.write(",stackEvents:" + timeline.isStackEvents());

		if (timeline.isShowCurrentTime()) {
			writer.write(",showCurrentTime:" + timeline.isShowCurrentTime());
			writer.write(",currentTime:"
			             + encodeDate(browserTZ, targetTZ, Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
		}

		writer.write(",showMajorLabels:" + timeline.isShowMajorLabels());
		writer.write(",showMinorLabels:" + timeline.isShowMinorLabels());
		writer.write(",showButtonNew:" + timeline.isShowButtonNew());
		writer.write(",showNavigation:" + timeline.isShowNavigation());

		if (timeline.getLocale() != null) {
			writer.write(",locale:'" + timeline.getLocale().toString() + "'");
		}

		if (timeline.getDropHoverStyleClass() != null) {
			writer.write(",hoverClass:'" + timeline.getDropHoverStyleClass() + "'");
		}

		if (timeline.getDropActiveStyleClass() != null) {
			writer.write(",activeClass:'" + timeline.getDropActiveStyleClass() + "'");
		}

		if (timeline.getDropAccept() != null) {
			writer.write(",accept:'" + timeline.getDropAccept() + "'");
		}

		if (timeline.getDropScope() != null) {
			writer.write(",scope:'" + timeline.getDropScope() + "'");
		}

		writer.write("}");
		encodeClientBehaviors(context, timeline);
		writer.write("},true);});");
		endScript(writer);
	}

	public String encodeEvent(FacesContext context, FastStringWriter fsw, FastStringWriter fswHtml, Timeline timeline,
	                          TimeZone browserTZ, TimeZone targetTZ, TimelineEvent event) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		fsw.write("{\"start\":" + encodeDate(browserTZ, targetTZ, event.getStartDate()));

		if (event.getEndDate() != null) {
			fsw.write(",\"end\":" + encodeDate(browserTZ, targetTZ, event.getEndDate()));
		} else {
			fsw.write(",\"end\":null");
		}

		if (event.isEditable() != null) {
			fsw.write(",\"editable\":" + event.isEditable());
		} else {
			fsw.write(",\"editable\":null");
		}

		if (event.getGroup() != null) {
			fsw.write(",\"group\":\"" + event.getGroup() + "\"");
		} else {
			fsw.write(",\"group\":null");
		}

		if (StringUtils.isNotBlank(event.getStyleClass())) {
			fsw.write(",\"className\":\"" + event.getStyleClass() + "\"");
		} else {
			fsw.write(",\"className\":null");
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
			fsw.write(escapeText(fswHtml.toString()));
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

	// convert from UTC to locale date
	private String encodeDate(TimeZone browserTZ, TimeZone targetTZ, Date utcDate) {
		return "new Date(" + DateUtils.toLocalDate(browserTZ, targetTZ, utcDate) + ")";
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
