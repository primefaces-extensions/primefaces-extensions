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

import java.util.List;

import jakarta.faces.component.UIOutput;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.GanttDateChangeEvent;
import org.primefaces.extensions.event.GanttDateClickEvent;
import org.primefaces.extensions.event.GanttProgressChangeEvent;
import org.primefaces.extensions.event.GanttSelectEvent;
import org.primefaces.extensions.event.GanttViewChangeEvent;

/**
 * <code>Gantt</code> component base class.
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "click", event = GanttSelectEvent.class, description = "Fires when a task is clicked.",
                        defaultEvent = true),
            @FacesBehaviorEvent(name = "dateChange", event = GanttDateChangeEvent.class,
                        description = "Fires when a task date changes."),
            @FacesBehaviorEvent(name = "progressChange", event = GanttProgressChangeEvent.class,
                        description = "Fires when task progress changes."),
            @FacesBehaviorEvent(name = "viewChange", event = GanttViewChangeEvent.class,
                        description = "Fires when view mode changes."),
            @FacesBehaviorEvent(name = "dateClick", event = GanttDateClickEvent.class,
                        description = "Fires when a date cell is clicked.")
})
public abstract class GanttBase extends UIOutput implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Gantt";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GanttRenderer";

    public GanttBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "The tasks to display in the Gantt chart.")
    public abstract List<GanttTask> getTasks();

    @Property(description = "The initial view mode.", defaultValue = "Day")
    public abstract String getViewMode();

    @Property(description = "Enable the today button.", defaultValue = "true")
    public abstract Boolean getTodayButton();

    @Property(description = "Show the view mode selector dropdown.", defaultValue = "false")
    public abstract Boolean getViewModeSelect();

    @Property(description = "Enable readonly mode to disable all editing.", defaultValue = "false")
    public abstract Boolean getReadonly();

    @Property(description = "Show the progress bar on tasks.", defaultValue = "true")
    public abstract Boolean getShowProgress();

    @Property(description = "Show expected progress bar.", defaultValue = "false")
    public abstract Boolean getShowExpectedProgress();

    @Property(description = "Column width in pixels.", defaultValue = "45")
    public abstract Integer getColumnWidth();

    @Property(description = "Bar height in pixels.", defaultValue = "30")
    public abstract Integer getBarHeight();

    @Property(description = "Date format.", defaultValue = "YYYY-MM-DD")
    public abstract String getDateFormat();

    @Property(description = "Locale/language.", defaultValue = "en")
    public abstract String getLang();

    @Property(description = "Weekend highlight color.")
    public abstract String getWeekendColor();

    @Property(description = "Snap at interval.", defaultValue = "1d")
    public abstract String getSnapAt();

    @Property(description = "Enable infinite scroll padding.", defaultValue = "true")
    public abstract Boolean getInfinitePadding();

    @Property(description = "Name of javascript function to extend the widget.")
    public abstract String getExtender();
}
