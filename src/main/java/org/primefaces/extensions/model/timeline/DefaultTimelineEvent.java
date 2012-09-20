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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DefaultTimelineEvent other = (DefaultTimelineEvent) obj;
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }

        if (this.startDate != other.startDate && (this.startDate == null || !this.startDate.equals(other.startDate))) {
            return false;
        }

        if (this.endDate != other.endDate && (this.endDate == null || !this.endDate.equals(other.endDate))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 79 * hash + (this.startDate != null ? this.startDate.hashCode() : 0);
        hash = 79 * hash + (this.endDate != null ? this.endDate.hashCode() : 0);

        return hash;
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
