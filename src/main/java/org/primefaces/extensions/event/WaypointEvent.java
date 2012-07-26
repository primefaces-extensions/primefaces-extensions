/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.waypoint.Waypoint} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.6
 */
@SuppressWarnings("serial")
public class WaypointEvent extends AbstractAjaxBehaviorEvent {

	private Direction direction;

	public WaypointEvent(UIComponent component, Behavior behavior, Direction direction) {
		super(component, behavior);
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	/**
	 * Direction
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	public static enum Direction {

		DOWN,
		UP
	}
}
