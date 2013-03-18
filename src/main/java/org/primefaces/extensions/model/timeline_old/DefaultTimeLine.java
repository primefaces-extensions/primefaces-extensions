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

package org.primefaces.extensions.model.timeline_old;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Default implementation of Timeline.
 *
 * @author  Nilesh Namdeo Mali / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
public class DefaultTimeLine implements Timeline, Serializable {

	private String id;
	private String title;
	private List<TimelineEvent> events;

	public DefaultTimeLine() {
		this.events = new ArrayList<TimelineEvent>();
	}

	public DefaultTimeLine(String id, String title) {
		this.id = id;
		this.title = title;
		this.events = new ArrayList<TimelineEvent>();
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

	public void addEvent(TimelineEvent event) {
		event.setId(UUID.randomUUID().toString());
		events.add(event);
	}

	public boolean deleteEvent(TimelineEvent event) {
		return events.remove(event);
	}

	public List<TimelineEvent> getEvents() {
		return events;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof DefaultTimeLine)) {
			return false;
		}

		DefaultTimeLine that = (DefaultTimeLine) o;

		if (!id.equals(that.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return title;
	}
}
