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
 * $Id$
 */
package org.primefaces.extensions.component.timeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.timeline.TimelineAddEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.event.timeline.TimelineRangeEvent;
import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.DateUtils;
import org.primefaces.util.Constants;

/**
 * Timeline component class.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7 (reimplemented)
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.css")
                      })
public class Timeline extends UIComponentBase implements Widget, ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Timeline";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimelineRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	private static final Collection<String> EVENT_NAMES =
	    Collections.unmodifiableCollection(Arrays.asList("add", "change", "edit", "delete", "select", "rangechange",
	                                                     "rangechanged"));

	/**
	 * PropertyKeys
	 *
	 * @author  Oleg Varaksin / last modified by $Author: $
	 * @version $Revision: 1.0 $
	 */
	enum PropertyKeys {

		widgetVar,
		value,
		var,
		locale,
		timeZone,
		browserTZ,
		style,
		styleClass,
		height,
		minHeight,
		width,
		responsive,
		axisOnTop,
		dragAreaWidth,
		editable,
		selectable,
		zoomable,
		moveable,
		start,
		end,
		min,
		max,
		zoomMin,
		zoomMax,
		eventMargin,
		eventMarginAxis,
		eventStyle,
		groupsChangeable,
		groupsOnRight,
		groupsWidth,
		snapEvents,
		stackEvents,
		showCurrentTime,
		showMajorLabels,
		showMinorLabels,
		showButtonNew,
		showNavigation;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Timeline() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(String widgetVar) {
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public TimelineModel getValue() {
		return (TimelineModel) getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(TimelineModel value) {
		setAttribute(PropertyKeys.value, value);
	}

	public String getVar() {
		return (String) getStateHelper().eval(PropertyKeys.var, null);
	}

	public void setVar(String var) {
		setAttribute(PropertyKeys.var, var);
	}

	public Object getLocale() {
		return getStateHelper().eval(PropertyKeys.locale, null);
	}

	public void setLocale(Object locale) {
		setAttribute(PropertyKeys.locale, locale);
	}

	public Object getTimeZone() {
		return getStateHelper().eval(PropertyKeys.timeZone, null);
	}

	public void setTimeZone(Object timeZone) {
		setAttribute(PropertyKeys.timeZone, timeZone);
	}

	public Object getBrowserTZ() {
		return getStateHelper().eval(PropertyKeys.browserTZ, null);
	}

	public void setBrowserTZ(Object timeZone) {
		setAttribute(PropertyKeys.browserTZ, timeZone);
	}

	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(String style) {
		setAttribute(PropertyKeys.style, style);
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(String styleClass) {
		setAttribute(PropertyKeys.styleClass, styleClass);
	}

	public String getHeight() {
		return (String) getStateHelper().eval(PropertyKeys.height, "auto");
	}

	public void setHeight(String height) {
		setAttribute(PropertyKeys.height, height);
	}

	public int getMinHeight() {
		return (Integer) getStateHelper().eval(PropertyKeys.minHeight, 0);
	}

	public void setMinHeight(int minHeight) {
		setAttribute(PropertyKeys.minHeight, minHeight);
	}

	public String getWidth() {
		return (String) getStateHelper().eval(PropertyKeys.width, "100%");
	}

	public void setWidth(String width) {
		setAttribute(PropertyKeys.width, width);
	}

	public boolean isResponsive() {
		return (Boolean) getStateHelper().eval(PropertyKeys.responsive, true);
	}

	public void setResponsive(boolean responsive) {
		setAttribute(PropertyKeys.responsive, responsive);
	}

	public boolean isAxisOnTop() {
		return (Boolean) getStateHelper().eval(PropertyKeys.axisOnTop, false);
	}

	public void setAxisOnTop(boolean axisOnTop) {
		setAttribute(PropertyKeys.axisOnTop, axisOnTop);
	}

	public int getDragAreaWidth() {
		return (Integer) getStateHelper().eval(PropertyKeys.dragAreaWidth, 10);
	}

	public void setDragAreaWidth(int dragAreaWidth) {
		setAttribute(PropertyKeys.dragAreaWidth, dragAreaWidth);
	}

	public boolean isEditable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.editable, false);
	}

	public void setEditable(boolean editable) {
		setAttribute(PropertyKeys.editable, editable);
	}

	public boolean isSelectable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.selectable, true);
	}

	public void setSelectable(boolean selectable) {
		setAttribute(PropertyKeys.selectable, selectable);
	}

	public boolean isZoomable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.zoomable, true);
	}

	public void setZoomable(boolean zoomable) {
		setAttribute(PropertyKeys.zoomable, zoomable);
	}

	public boolean isMoveable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.moveable, true);
	}

	public void setMoveable(boolean moveable) {
		setAttribute(PropertyKeys.moveable, moveable);
	}

	public Date getStart() {
		return (Date) getStateHelper().eval(PropertyKeys.start, null);
	}

	public void setStart(Date start) {
		setAttribute(PropertyKeys.start, start);
	}

	public Date getEnd() {
		return (Date) getStateHelper().eval(PropertyKeys.end, null);
	}

	public void setEnd(Date end) {
		setAttribute(PropertyKeys.end, end);
	}

	public Date getMin() {
		return (Date) getStateHelper().eval(PropertyKeys.min, null);
	}

	public void setMin(Date min) {
		setAttribute(PropertyKeys.min, min);
	}

	public Date getMax() {
		return (Date) getStateHelper().eval(PropertyKeys.max, null);
	}

	public void setMax(Date max) {
		setAttribute(PropertyKeys.max, max);
	}

	public long getZoomMin() {
		return (Long) getStateHelper().eval(PropertyKeys.zoomMin, 10L);
	}

	public void setZoomMin(long zoomMin) {
		setAttribute(PropertyKeys.zoomMin, zoomMin);
	}

	public long getZoomMax() {
		return (Long) getStateHelper().eval(PropertyKeys.zoomMax, 315360000000000L);
	}

	public void setZoomMax(long zoomMax) {
		setAttribute(PropertyKeys.zoomMax, zoomMax);
	}

	public int getEventMargin() {
		return (Integer) getStateHelper().eval(PropertyKeys.eventMargin, 10);
	}

	public void setEventMargin(int eventMargin) {
		setAttribute(PropertyKeys.eventMargin, eventMargin);
	}

	public int getEventMarginAxis() {
		return (Integer) getStateHelper().eval(PropertyKeys.eventMarginAxis, 10);
	}

	public void setEventMarginAxis(int eventMarginAxis) {
		setAttribute(PropertyKeys.eventMarginAxis, eventMarginAxis);
	}

	public String getEventStyle() {
		return (String) getStateHelper().eval(PropertyKeys.eventStyle, "box");
	}

	public void setEventStyle(String eventStyle) {
		setAttribute(PropertyKeys.eventStyle, eventStyle);
	}

	public boolean isGroupsChangeable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.groupsChangeable, true);
	}

	public void setGroupsChangeable(boolean groupsChangeable) {
		setAttribute(PropertyKeys.groupsChangeable, groupsChangeable);
	}

	public boolean isGroupsOnRight() {
		return (Boolean) getStateHelper().eval(PropertyKeys.groupsOnRight, false);
	}

	public void setGroupsOnRight(boolean groupsOnRight) {
		setAttribute(PropertyKeys.groupsOnRight, groupsOnRight);
	}

	public String getGroupsWidth() {
		return (String) getStateHelper().eval(PropertyKeys.groupsWidth, null);
	}

	public void setGroupsWidth(String groupsWidth) {
		setAttribute(PropertyKeys.groupsWidth, groupsWidth);
	}

	public boolean isSnapEvents() {
		return (Boolean) getStateHelper().eval(PropertyKeys.snapEvents, true);
	}

	public void setSnapEvents(boolean snapEvents) {
		setAttribute(PropertyKeys.snapEvents, snapEvents);
	}

	public boolean isStackEvents() {
		return (Boolean) getStateHelper().eval(PropertyKeys.stackEvents, true);
	}

	public void setStackEvents(boolean stackEvents) {
		setAttribute(PropertyKeys.stackEvents, stackEvents);
	}

	public boolean isShowCurrentTime() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showCurrentTime, true);
	}

	public void setShowCurrentTime(boolean showCurrentTime) {
		setAttribute(PropertyKeys.showCurrentTime, showCurrentTime);
	}

	public boolean isShowMajorLabels() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showMajorLabels, true);
	}

	public void setShowMajorLabels(boolean showMajorLabels) {
		setAttribute(PropertyKeys.showMajorLabels, showMajorLabels);
	}

	public boolean isShowMinorLabels() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showMinorLabels, true);
	}

	public void setShowMinorLabels(boolean showMinorLabels) {
		setAttribute(PropertyKeys.showMinorLabels, showMinorLabels);
	}

	public boolean isShowButtonNew() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showButtonNew, false);
	}

	public void setShowButtonNew(boolean showButtonNew) {
		setAttribute(PropertyKeys.showButtonNew, showButtonNew);
	}

	public boolean isShowNavigation() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showNavigation, false);
	}

	public void setShowNavigation(boolean showNavigation) {
		setAttribute(PropertyKeys.showNavigation, showNavigation);
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
		String clientId = this.getClientId(context);

		if (isSelfRequest(context)) {
			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if ("add".equals(eventName)) {
				// preset start / end date and the group
				TimeZone targetTZ = ComponentUtils.resolveTimeZone(getTimeZone());
				TimeZone browserTZ = ComponentUtils.resolveTimeZone(getBrowserTZ(), targetTZ);
				TimelineAddEvent te =
				    new TimelineAddEvent(this, behaviorEvent.getBehavior(),
				                         DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_startDate")),
				                         DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_endDate")),
				                         params.get(clientId + "_group"));
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			} else if ("change".equals(eventName)) {
				TimelineEvent clonedEvent = null;
				TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));

				if (timelineEvent != null) {
					clonedEvent = new TimelineEvent();
					clonedEvent.setData(timelineEvent.getData());
					clonedEvent.setEditable(timelineEvent.isEditable());
					clonedEvent.setStyleClass(timelineEvent.getStyleClass());

					// update start / end date and the group
					TimeZone targetTZ = ComponentUtils.resolveTimeZone(getTimeZone());
					TimeZone browserTZ = ComponentUtils.resolveTimeZone(getBrowserTZ(), targetTZ);
					clonedEvent.setStartDate(DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_startDate")));
					clonedEvent.setEndDate(DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_endDate")));
					clonedEvent.setGroup(params.get(clientId + "_group"));
				}

				TimelineModificationEvent te = new TimelineModificationEvent(this, behaviorEvent.getBehavior(), clonedEvent);
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			} else if ("edit".equals(eventName) || "delete".equals(eventName)) {
				TimelineEvent clonedEvent = null;
				TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));

				if (timelineEvent != null) {
					clonedEvent = new TimelineEvent();
					clonedEvent.setData(timelineEvent.getData());
					clonedEvent.setStartDate((Date) timelineEvent.getStartDate().clone());
					clonedEvent.setEndDate(timelineEvent.getEndDate() != null ? (Date) timelineEvent.getEndDate().clone() : null);
					clonedEvent.setEditable(timelineEvent.isEditable());
					clonedEvent.setGroup(timelineEvent.getGroup());
					clonedEvent.setStyleClass(timelineEvent.getStyleClass());
				}

				TimelineModificationEvent te = new TimelineModificationEvent(this, behaviorEvent.getBehavior(), clonedEvent);
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			} else if ("select".equals(eventName)) {
				TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));
				TimelineSelectEvent te = new TimelineSelectEvent(this, behaviorEvent.getBehavior(), timelineEvent);
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			} else if ("rangechange".equals(eventName)) {
				// get start / end date
				TimeZone targetTZ = ComponentUtils.resolveTimeZone(getTimeZone());
				TimeZone browserTZ = ComponentUtils.resolveTimeZone(getBrowserTZ(), targetTZ);
				TimelineRangeEvent te =
				    new TimelineRangeEvent(this, behaviorEvent.getBehavior(),
				                           DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_startDate")),
				                           DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_endDate")));
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			} else if ("rangechanged".equals(eventName)) {
				// get start / end date
				TimeZone targetTZ = ComponentUtils.resolveTimeZone(getTimeZone());
				TimeZone browserTZ = ComponentUtils.resolveTimeZone(getBrowserTZ(), targetTZ);
				TimelineRangeEvent te =
				    new TimelineRangeEvent(this, behaviorEvent.getBehavior(),
				                           DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_startDate")),
				                           DateUtils.toUtcDate(browserTZ, targetTZ, params.get(clientId + "_endDate")));
				te.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(te);

				return;
			}
		}

		super.queueEvent(event);
	}

	private boolean isSelfRequest(FacesContext context) {
		return this.getClientId(context)
		           .equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
	}

        @Override
	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	public void setAttribute(PropertyKeys property, Object value) {
		getStateHelper().put(property, value);

		@SuppressWarnings("unchecked")
		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}
}
