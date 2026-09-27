/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.gantt;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.GanttDateChangeEvent;
import org.primefaces.extensions.event.GanttDateClickEvent;
import org.primefaces.extensions.event.GanttProgressChangeEvent;
import org.primefaces.extensions.event.GanttSelectEvent;
import org.primefaces.extensions.event.GanttViewChangeEvent;
import org.primefaces.extensions.util.Constants;

/**
 * <code>gantt</code> component.
 */
@FacesComponent(value = Gantt.COMPONENT_TYPE, namespace = Gantt.COMPONENT_FAMILY)
@FacesComponentInfo(name = "gantt", description = "Gantt is a modern, interactive gantt chart component.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "gantt/gantt.js")
@ResourceDependency(library = Constants.LIBRARY, name = "gantt/gantt.css")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.css")
public class Gantt extends GanttBaseImpl {

    public static final String STYLE_CLASS = "ui-gantt ";

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isAjaxRequestSource(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            FacesContext context = event.getFacesContext();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String clientId = getClientId(context);
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.click)) {
                final String taskId = params.get(clientId + "_taskId");
                final GanttSelectEvent ganttSelectEvent = new GanttSelectEvent(this, behaviorEvent.getBehavior(), taskId);
                ganttSelectEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(ganttSelectEvent);
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.dateChange)) {
                final String taskId = params.get(clientId + "_taskId");
                final String start = params.get(clientId + "_start");
                final String end = params.get(clientId + "_end");
                final GanttDateChangeEvent ganttDateChangeEvent = new GanttDateChangeEvent(this,
                            behaviorEvent.getBehavior(), taskId,
                            parseDate(start),
                            parseDate(end));
                ganttDateChangeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(ganttDateChangeEvent);
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.progressChange)) {
                final String taskId = params.get(clientId + "_taskId");
                final String progressStr = params.get(clientId + "_progress");
                int progress = 0;
                if (progressStr != null) {
                    try {
                        progress = Integer.parseInt(progressStr);
                    }
                    catch (NumberFormatException e) {
                        progress = 0;
                    }
                }
                final GanttProgressChangeEvent ganttProgressChangeEvent = new GanttProgressChangeEvent(this,
                            behaviorEvent.getBehavior(), taskId, progress);
                ganttProgressChangeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(ganttProgressChangeEvent);
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.viewChange)) {
                final String viewMode = params.get(clientId + "_viewMode");
                final GanttViewChangeEvent ganttViewChangeEvent = new GanttViewChangeEvent(this,
                            behaviorEvent.getBehavior(), viewMode);
                ganttViewChangeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(ganttViewChangeEvent);
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.dateClick)) {
                final String date = params.get(clientId + "_date");
                final GanttDateClickEvent ganttDateClickEvent = new GanttDateClickEvent(this,
                            behaviorEvent.getBehavior(), date);
                ganttDateClickEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(ganttDateClickEvent);
            }
            else {
                super.queueEvent(event);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    static Date parseDate(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        final String text = value.trim();
        try {
            return Date.from(Instant.parse(text));
        }
        catch (final DateTimeParseException ignored) {
        }
        try {
            return Date.from(OffsetDateTime.parse(text).toInstant());
        }
        catch (final DateTimeParseException ignored) {
        }
        try {
            return Date.from(LocalDateTime.parse(text).atZone(ZoneId.systemDefault()).toInstant());
        }
        catch (final DateTimeParseException ignored) {
        }
        try {
            return Date.from(LocalDate.parse(text).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        catch (final DateTimeParseException ignored) {
        }
        try {
            return new Date(Long.parseLong(text));
        }
        catch (final NumberFormatException ignored) {
        }
        try {
            @SuppressWarnings("deprecation")
            final Date legacy = new Date(text);
            return legacy;
        }
        catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
