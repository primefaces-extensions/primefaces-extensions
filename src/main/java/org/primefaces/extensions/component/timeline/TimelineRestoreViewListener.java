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

package org.primefaces.extensions.component.timeline;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Phase listener to put a {@link DefaultTimelineUpdater} instance into the tread-safe FacesContext in the "restore view" phase.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7
 */
public class TimelineRestoreViewListener implements PhaseListener {

	private static final long serialVersionUID = 20130317L;

	private String widgetVar;

	public TimelineRestoreViewListener(String widgetVar) {
		this.widgetVar = widgetVar;
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void beforePhase(PhaseEvent event) {
		// NOOP.
	}

	public void afterPhase(PhaseEvent event) {
		createTimelineUpdater(event.getFacesContext(), widgetVar);
	}

	public static void createTimelineUpdater(FacesContext fc, String widgetVar) {
		@SuppressWarnings("unchecked")
		Map<String, TimelineUpdater> map = (Map<String, TimelineUpdater>) fc.getAttributes().get(TimelineUpdater.class.getName());
		if (map == null) {
			map = new HashMap<String, TimelineUpdater>();
			fc.getAttributes().put(TimelineUpdater.class.getName(), map);
		}

		DefaultTimelineUpdater timelineUpdater = new DefaultTimelineUpdater();
		timelineUpdater.setWidgetVar(widgetVar);

		map.put(widgetVar, timelineUpdater);

		// render "update" JS script in the "render response" phase
		UIViewRoot viewRoot = fc.getViewRoot();
		viewRoot.removePhaseListener(timelineUpdater);
		viewRoot.addPhaseListener(timelineUpdater);
	}
}
