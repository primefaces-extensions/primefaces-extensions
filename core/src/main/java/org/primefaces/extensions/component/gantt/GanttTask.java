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

import java.io.Serial;
import java.io.Serializable;

/**
 * Simple data model representing a single task in the Gantt chart.
 */
public class GanttTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String start;
    private String end;
    private Integer progress;
    private String dependencies;
    private String color;
    private String colorProgress;
    private String customClass;
    private String thumbnail;
    private String description;
    private String barLabel;
    private boolean invalid;

    public GanttTask() {
    }

    public GanttTask(String id, String name, String start, String end) {
        this(id, name, start, end, null, null, null, null, null, null, null, false);
    }

    public GanttTask(String id, String name, String start, String end, Integer progress, String dependencies, String color) {
        this(id, name, start, end, progress, dependencies, color, null, null, null, null, false);
    }

    public GanttTask(String id, String name, String start, String end, Integer progress, String dependencies, String color,
                String description, String customClass, String barLabel, String thumbnail, boolean invalid) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.progress = progress;
        this.dependencies = dependencies;
        this.color = color;
        this.description = description;
        this.customClass = customClass;
        this.barLabel = barLabel;
        this.thumbnail = thumbnail;
        this.invalid = invalid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorProgress() {
        return colorProgress;
    }

    public void setColorProgress(String colorProgress) {
        this.colorProgress = colorProgress;
    }

    public String getCustomClass() {
        return customClass;
    }

    public void setCustomClass(String customClass) {
        this.customClass = customClass;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarLabel() {
        return barLabel;
    }

    public void setBarLabel(String barLabel) {
        this.barLabel = barLabel;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
