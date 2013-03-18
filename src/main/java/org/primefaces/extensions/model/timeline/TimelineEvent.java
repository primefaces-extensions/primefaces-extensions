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

package org.primefaces.extensions.model.timeline;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Model class representing a timeline event.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7 (reimplemented)
 */
public class TimelineEvent implements Serializable {

	private static final long serialVersionUID = 20120316L;

	private String id;
	private Object data;
	private Date startDate;
	private Date endDate;
	private Boolean editable;
	private String group;
	private String styleClass;

	public TimelineEvent() {
		id = UUID.randomUUID().toString();
	}

	public TimelineEvent(Object data, Date startDate) {
		checkStartDate(startDate);

		id = UUID.randomUUID().toString();
		this.data = data;
		this.startDate = startDate;
	}

	public TimelineEvent(Object data, Date startDate, Date endDate) {
		checkStartDate(startDate);

		id = UUID.randomUUID().toString();
		this.data = data;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public TimelineEvent(Object data, Date startDate, Date endDate, Boolean editable) {
		checkStartDate(startDate);

		id = UUID.randomUUID().toString();
		this.data = data;
		this.startDate = startDate;
		this.endDate = endDate;
		this.editable = editable;
	}

	public TimelineEvent(Object data, Date startDate, Date endDate, Boolean editable, String group) {
		checkStartDate(startDate);

		id = UUID.randomUUID().toString();
		this.data = data;
		this.startDate = startDate;
		this.endDate = endDate;
		this.editable = editable;
		this.group = group;
	}

	public TimelineEvent(Object data, Date startDate, Date endDate, Boolean editable, String group, String styleClass) {
		checkStartDate(startDate);

		id = UUID.randomUUID().toString();
		this.data = data;
		this.startDate = startDate;
		this.endDate = endDate;
		this.editable = editable;
		this.group = group;
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TimelineEvent that = (TimelineEvent) o;

		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	private void checkStartDate(Date startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("Event start date can not be null!");
		}
	}
}
