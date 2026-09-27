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
package org.primefaces.extensions.showcase.controller.gantt;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.extensions.component.gantt.GanttTask;
import org.primefaces.extensions.event.GanttDateChangeEvent;
import org.primefaces.extensions.event.GanttDateClickEvent;
import org.primefaces.extensions.event.GanttProgressChangeEvent;
import org.primefaces.extensions.event.GanttSelectEvent;
import org.primefaces.extensions.event.GanttViewChangeEvent;

/**
 * GanttController
 */
@Named
@ViewScoped
public class GanttController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<GanttTask> tasks;
    private String viewMode = "Week";
    private String selectedTaskId;
    private String lastAction;

    public GanttController() {
        super();
        init();
    }

    public void init() {
        final LocalDate today = LocalDate.now();
        tasks = new ArrayList<>();
        tasks.add(new GanttTask("1", "Project Kickoff", date(today, -21), date(today, -19), 100, "", "#ff5e57"));
        tasks.add(new GanttTask("2", "Requirements Gathering", date(today, -18), date(today, -12), 80, "1", "#ff9f43"));
        tasks.add(new GanttTask("3", "Design Phase", date(today, -11), date(today, -5), 60, "2", "#54a0ff"));
        tasks.add(new GanttTask("4", "Development Sprint 1", date(today, -4), date(today, 6), 40, "3", "#5f27cd"));
        tasks.add(new GanttTask("5", "Development Sprint 2", date(today, 7), date(today, 17), 20, "4", "#5f27cd"));
        tasks.add(new GanttTask("6", "Testing & QA", date(today, 18), date(today, 27), 0, "5", "#00d2d3"));
        tasks.add(new GanttTask("7", "Deployment", date(today, 28), date(today, 31), 0, "6", "#10ac84"));
        tasks.add(new GanttTask("8", "Post-Launch Review", date(today, 32), date(today, 35), 0, "7", "#ee5253"));
    }

    private String date(final LocalDate base, final int offset) {
        return base.plusDays(offset).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void onTaskClick(final GanttSelectEvent event) {
        selectedTaskId = event.getTaskId();
        lastAction = "Task " + selectedTaskId + " selected";
        addMessage("Task selected", lastAction);
    }

    public void onDateChange(final GanttDateChangeEvent event) {
        final GanttTask task = findTask(event.getTaskId());
        if (task != null && event.getStart() != null && event.getEnd() != null) {
            task.setStart(formatDate(event.getStart()));
            task.setEnd(formatDate(event.getEnd()));
        }
        lastAction = "Task " + event.getTaskId() + " moved to " + formatDate(event.getStart()) + " - " + formatDate(event.getEnd());
        addMessage("Date changed", lastAction);
    }

    public void onProgressChange(final GanttProgressChangeEvent event) {
        final GanttTask task = findTask(event.getTaskId());
        if (task != null) {
            task.setProgress(event.getProgress());
        }
        lastAction = "Task " + event.getTaskId() + " progress set to " + event.getProgress() + "%";
        addMessage("Progress changed", lastAction);
    }

    public void onViewChange(final GanttViewChangeEvent event) {
        viewMode = event.getViewMode();
        lastAction = "View changed to " + viewMode;
    }

    public void onDateClick(final GanttDateClickEvent event) {
        lastAction = "Date " + event.getDate() + " selected";
        addMessage("Date selected", lastAction);
    }

    private GanttTask findTask(final String taskId) {
        if (taskId == null) {
            return null;
        }
        for (final GanttTask task : tasks) {
            if (taskId.equals(task.getId())) {
                return task;
            }
        }
        return null;
    }

    private String formatDate(final java.util.Date date) {
        if (date == null) {
            return "";
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void addMessage(final String summary, final String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    public List<GanttTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<GanttTask> tasks) {
        this.tasks = tasks;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public String getSelectedTaskId() {
        return selectedTaskId;
    }

    public void setSelectedTaskId(String selectedTaskId) {
        this.selectedTaskId = selectedTaskId;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
}
