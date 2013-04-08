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
 * $Id: $
 */

package org.primefaces.extensions.model.timeline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.extensions.component.timeline.TimelineUpdater;

/**
 * Model class for the Timeline component which consists of {@link TimelineEvent}s.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7 (reimplemented)
 */
public class TimelineModel implements Serializable {

	private static final long serialVersionUID = 20130316L;

	private List<TimelineEvent> events;

	public TimelineModel() {
		events = new ArrayList<TimelineEvent>();
	}

	public TimelineModel(List<TimelineEvent> events) {
		this.events = new ArrayList<TimelineEvent>();

		if (events != null && !events.isEmpty()) {
			for (TimelineEvent event : events) {
				add(event);
			}
		}
	}

	public void add(TimelineEvent event) {
		events.add(event);
	}

	public void add(TimelineEvent event, TimelineUpdater timelineUpdater) {
		events.add(event);

		if (timelineUpdater != null) {
			// update UI
			timelineUpdater.add(event);
		}
	}

	public void addAll(List<TimelineEvent> events) {
		addAll(events, null);
	}

	public void addAll(List<TimelineEvent> events, TimelineUpdater timelineUpdater) {
		if (events != null && !events.isEmpty()) {
			for (TimelineEvent event : events) {
				add(event, timelineUpdater);
			}
		}
	}

	public void update(TimelineEvent event) {
		update(event, null);
	}

	public void update(TimelineEvent event, TimelineUpdater timelineUpdater) {
		int index = getIndex(event);
		if (index >= 0) {
			events.set(index, event);
		}

		if (timelineUpdater != null) {
			// update UI
			timelineUpdater.update(event, index);
		}
	}

	public void updateAll(List<TimelineEvent> events) {
		updateAll(events, null);
	}

	public void updateAll(List<TimelineEvent> events, TimelineUpdater timelineUpdater) {
		if (events != null && !events.isEmpty()) {
			for (TimelineEvent event : events) {
				update(event, timelineUpdater);
			}
		}
	}

	public void delete(TimelineEvent event) {
		delete(event, null);
	}

	public void delete(TimelineEvent event, TimelineUpdater timelineUpdater) {
		int index = getIndex(event);
		events.remove(event);

		if (timelineUpdater != null) {
			// update UI
			timelineUpdater.delete(index);
		}
	}

	public void deleteAll(List<TimelineEvent> events) {
		deleteAll(events, null);
	}

	public void deleteAll(List<TimelineEvent> events, TimelineUpdater timelineUpdater) {
		if (events != null && !events.isEmpty()) {
			List<Integer> indexes = new ArrayList<Integer>();
			for (TimelineEvent event : events) {
				indexes.add(getIndex(event));
			}

			this.events.removeAll(events);

			if (timelineUpdater != null) {
				// update UI
				timelineUpdater.deleteAll(indexes);
			}
		}
	}

	public void clear() {
		events.clear();
	}

	public void clear(TimelineUpdater timelineUpdater) {
		events.clear();

		if (timelineUpdater != null) {
			// update UI
			timelineUpdater.clear();
		}
	}

	public List<TimelineEvent> getEvents() {
		return events;
	}

	public void setEvents(List<TimelineEvent> events) {
		this.events = events;
	}

	public TimelineEvent getEvent(String index) {
		return getEvent(index != null ? Integer.valueOf(index) : -1);
	}

	public TimelineEvent getEvent(int index) {
		if (index < 0) {
			return null;
		}

		return events.get(index);
	}

	public int getIndex(TimelineEvent event) {
		int index = -1;

		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).equals(event)) {
				index = i;

				break;
			}
		}

		return index;
	}
}
