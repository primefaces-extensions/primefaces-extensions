/*
 * Copyright 2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.model.timeline;

import java.util.Date;

/**
 * Default implementation of TimelineEvent
 *
 * @author Nilesh Namdeo Mali / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class DefaultTimelineEvent implements TimelineEvent {

    private String id;
    private String title;
    private String styleClass;
    private Date startDate;
    private Date endDate;

    public DefaultTimelineEvent() {
    }

    public DefaultTimelineEvent(String title, Date startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    public DefaultTimelineEvent(String title, Date startDate, Date endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DefaultTimelineEvent(String title, Date startDate, Date endDate, String styleClass) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.styleClass = styleClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultTimelineEvent)) return false;

        DefaultTimelineEvent that = (DefaultTimelineEvent) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultTimelineEvent{" +
                "title='" + title + '\'' +
                ", styleClass='" + styleClass + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
