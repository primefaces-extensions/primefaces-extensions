/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
