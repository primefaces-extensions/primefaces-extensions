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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DOCUMENT_ME
 *
 *
 * @author  Nilesh Namdeo Mali / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
public class DefaultTimeLine implements Timeline {

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
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final DefaultTimeLine other = (DefaultTimeLine) obj;
		if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);

		return hash;
	}

	@Override
	public String toString() {
		return title;
	}
}
