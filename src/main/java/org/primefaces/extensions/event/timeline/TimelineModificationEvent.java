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

package org.primefaces.extensions.event.timeline;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

import org.primefaces.extensions.event.AbstractAjaxBehaviorEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;

/**
 * Event when a timeline event gets changed, edited, deleted.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7
 */
public class TimelineModificationEvent extends AbstractAjaxBehaviorEvent {

	private TimelineEvent timelineEvent;

	public TimelineModificationEvent(UIComponent component, Behavior behavior, TimelineEvent timelineEvent) {
		super(component, behavior);
		this.timelineEvent = timelineEvent;
	}

	public TimelineEvent getTimelineEvent() {
		return timelineEvent;
	}
}
