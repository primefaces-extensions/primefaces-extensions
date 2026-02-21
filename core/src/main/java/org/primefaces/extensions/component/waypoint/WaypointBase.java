/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.waypoint;

import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.WaypointEvent;

/**
 * <code>Waypoint</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "reached", event = WaypointEvent.class,
                        description = "Fires when a waypoint is reached.", defaultEvent = true)
})
public abstract class WaypointBase extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";

    public WaypointBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "The target component registered as waypoint. If null, the parent component will be taken as target.")
    public abstract String getFor();

    @Property(description = "The context defines which scrollable element the waypoint belongs to and acts within. "
                + "It can be a component or plain HTML element like a div. Default is window if nothing specified.")
    public abstract String getForContext();

    @Property(description = "If false, this waypoint will be created but will be disabled from triggering. "
                + "You can call widget's method 'enable' to turn it back on.",
                defaultValue = "true")
    public abstract boolean isEnabled();

    @Property(description = "Default waypoints live on the vertical axis. Their offset is calculated in relation to "
                + "the top of the viewport, and they listen for vertical scroll changes. If horizontal is set to true, offset "
                + "is calculated in relation to the left of the viewport and listens for horizontal scroll changes. For instance, "
                + "the value 50 means 'when the left side of this element reaches 50px from the left side of the viewport'.",
                defaultValue = "false")
    public abstract boolean isHorizontal();

    @Property(description = "This option determines how far the top of the element must be from the top of the viewport to "
                + "trigger a waypoint (callback function). It can be a number, which is taken as a number of pixels (can be also "
                + "negative, e.g. -10), a string representing a percentage of the viewport height (e.g. '50%') or a function that "
                + "will return a number of pixels.")
    public abstract String getOffset();

    @Property(description = "If true, and multiple waypoints are triggered in one scroll, this waypoint will trigger even if "
                + "it is not the last waypoint reached. If false, it will only trigger if it is the last waypoint.",
                defaultValue = "true")
    public abstract boolean isContinuous();

    @Property(description = "If true, the waypoint will be destroyed when triggered.",
                defaultValue = "false")
    public abstract boolean isTriggerOnce();
}
