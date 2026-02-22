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
package org.primefaces.extensions.component.slideout;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;

/**
 * <code>SlideOut</code> component base class.
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "open", event = OpenEvent.class, description = "Fires when the panel is opened.", defaultEvent = false),
            @FacesBehaviorEvent(name = "close", event = CloseEvent.class, description = "Fires when the panel is closed.", defaultEvent = false)
})
public abstract class SlideOutBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SlideOut";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SlideOutRenderer";

    public SlideOutBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Style of the slideout panel.")
    public abstract String getPanelStyle();

    @Property(description = "Style class of the slideout panel.")
    public abstract String getPanelStyleClass();

    @Property(description = "Style of the handle.")
    public abstract String getHandleStyle();

    @Property(description = "Style class of the handle.")
    public abstract String getHandleStyleClass();

    @Property(description = "Text title of the handle.")
    public abstract String getTitle();

    @Property(description = "Icon for the handle.")
    public abstract String getIcon();

    @Property(description = "Show on 'click' or 'hover'.", defaultValue = "click")
    public abstract String getShowOn();

    @Property(description = "Location: left, right, top or bottom.", defaultValue = "right")
    public abstract String getLocation();

    @Property(description = "Offset distance of the panel from the edge.", defaultValue = "200px")
    public abstract String getOffset();

    @Property(description = "If true, offset is reversed (align with right/bottom).", defaultValue = "false")
    public abstract boolean isOffsetReverse();

    @Property(description = "Handle offset distance from edge.")
    public abstract String getHandleOffset();

    @Property(description = "If true, handle offset is reversed (align with right/bottom).", defaultValue = "false")
    public abstract boolean isHandleOffsetReverse();

    @Property(description = "If true, the panel is positioned fixed (sticky).", defaultValue = "false")
    public abstract boolean isSticky();

    @Property(description = "If true, clicking the screen closes the panel.", defaultValue = "true")
    public abstract boolean isClickScreenToClose();

    @Property(description = "If true, the panel auto opens on load.", defaultValue = "false")
    public abstract boolean isAutoOpen();

    @Property(description = "Animation speed in ms.", defaultValue = "300")
    public abstract int getAnimateSpeed();

    @Property(description = "Distance for bounce effect.", defaultValue = "50px")
    public abstract String getBounceDistance();

    @Property(description = "Number of bounces.", defaultValue = "4")
    public abstract int getBounceTimes();

    @Property(description = "Callback javascript executed after open.")
    public abstract String getOnopen();

    @Property(description = "Callback javascript executed after close.")
    public abstract String getOnclose();

    @Property(description = "Callback javascript executed on slide.")
    public abstract String getOnslide();

    @Property(description = "Callback javascript executed before open.")
    public abstract String getOnbeforeopen();

    @Property(description = "Callback javascript executed before close.")
    public abstract String getOnbeforeclose();

    @Property(description = "Callback javascript executed before slide.")
    public abstract String getOnbeforeslide();
}
