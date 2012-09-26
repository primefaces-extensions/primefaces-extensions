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

import java.util.*;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.util.Constants;

/**
 * Timeline component.
 *
 * @author Nilesh Namdeo Mali / last modified by $Author$
 * @version $Revision$
 * @since 0.3
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
    private static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimelineRenderer";
    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("eventSelect"));

    /**
     * Properties that are tracked by state saving.
     *
     * @author Nilesh Namdeo Mali / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {
        axisPosition,
        eventStyle,
        height,
        showNavigation,
        style,
        styleClass,
        value,
        var,
        widgetVar,
        width;

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

    public String getAxisPosition() {
        return (String) getStateHelper().eval(PropertyKeys.axisPosition, "bottom");
    }

    public void setAxisPosition(final String axisPosition) {
        setAttribute(PropertyKeys.axisPosition, axisPosition);
    }

    public String getEventStyle() {
        return (String) getStateHelper().eval(PropertyKeys.eventStyle, "box");
    }

    public void setEventStyle(final String eventStyle) {
        setAttribute(PropertyKeys.eventStyle, eventStyle);
    }

    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, "auto");
    }

    public void setHeight(final String height) {
        setAttribute(PropertyKeys.height, height);
    }

    public String getShowNavigation() {
        return (String) getStateHelper().eval(PropertyKeys.showNavigation, "false");
    }

    public void setShowNavigation(final String showNavigation) {
        setAttribute(PropertyKeys.showNavigation, showNavigation);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String style) {
        setAttribute(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String styleClass) {
        setAttribute(PropertyKeys.styleClass, styleClass);
    }

    public List<org.primefaces.extensions.model.timeline.Timeline> getValue() {
        return (List<org.primefaces.extensions.model.timeline.Timeline>) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(final List<org.primefaces.extensions.model.timeline.Timeline> timelines) {
        setAttribute(PropertyKeys.value, timelines);
    }

    public String getVar() {
        return (String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public void setVar(final String var) {
        setAttribute(PropertyKeys.var, var);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, "100%");
    }

    public void setWidth(final String width) {
        setAttribute(PropertyKeys.width, width);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context)
                .equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
        String clientId = this.getClientId(context);
        if (isSelfRequest(context)) {

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;
            if (eventName.equals("eventSelect")) {
                String selectedTimelineId = params.get(clientId + "_selectedTimelineId");
                String selectedEventId = params.get(clientId + "_selectedEventId");

                if (this.getValue() != null && !this.getValue().isEmpty()) {
                    Iterator<org.primefaces.extensions.model.timeline.Timeline> itrTimeline = this.getValue().iterator();
                    org.primefaces.extensions.model.timeline.Timeline timeline;
                    while (itrTimeline.hasNext()) {
                        timeline = itrTimeline.next();
                        if (timeline.getId().equals(selectedTimelineId)) {
                            Iterator<TimelineEvent> itrEvent = timeline.getEvents().iterator();
                            TimelineEvent selectedEvent = null;
                            while (itrEvent.hasNext()) {
                                selectedEvent = itrEvent.next();
                                if (selectedEvent.getId().equals(selectedEventId)) {
                                    wrapperEvent = new SelectEvent(this, behaviorEvent.getBehavior(), selectedEvent);
                                    super.queueEvent(wrapperEvent);
                                    return;
                                }
                            }
                        }
                    }

                }
            }
        }

        super.queueEvent(event);
    }

    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAttribute(final PropertyKeys property, final Object value) {
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
