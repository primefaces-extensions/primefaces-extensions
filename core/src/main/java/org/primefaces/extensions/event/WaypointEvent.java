/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
 */
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.waypoint.Waypoint} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version $Revision$
 * @since 0.6
 */
public class WaypointEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "reached";
    private static final long serialVersionUID = 1L;

    private final Direction direction;

    private final String waypointId; // client Id of the current waypoint

    public enum Direction {
        DOWN, UP, RIGHT, LEFT
    }

    public WaypointEvent(final UIComponent component, final Behavior behavior, final Direction direction,
                final String waypointId) {
        super(component, behavior);
        this.direction = direction;
        this.waypointId = waypointId;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getWaypointId() {
        return waypointId;
    }

}
