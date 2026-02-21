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
package org.primefaces.extensions.component.layout;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;

/**
 * <code>Layout</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "open", event = OpenEvent.class,
                        description = "Fires when a pane is opened.", defaultEvent = true),
            @FacesBehaviorEvent(name = "close", event = CloseEvent.class,
                        description = "Fires when a pane is closed."),
            @FacesBehaviorEvent(name = "resize", event = ResizeEvent.class,
                        description = "Fires when a pane is resized.")
})
public abstract class LayoutBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Layout";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutRenderer";

    public LayoutBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Full page layout mode.", defaultValue = "true")
    public abstract boolean isFullPage();

    @Property(description = "Layout options (object or JSON string).")
    public abstract Object getOptions();

    @Property(description = "Serialized layout state for server-side persistence.")
    public abstract String getState();

    @Property(description = "Persist state in cookie.", defaultValue = "false")
    public abstract boolean isStateCookie();

    @Property(description = "Tooltip when toggler is open.")
    public abstract String getTogglerTipOpen();

    @Property(description = "Tooltip when toggler is closed.")
    public abstract String getTogglerTipClosed();

    @Property(description = "Resizer tooltip.")
    public abstract String getResizerTip();

    @Property(description = "Slider tooltip.")
    public abstract String getSliderTip();

    @Property(description = "Mask panes early during init.", defaultValue = "false")
    public abstract boolean isMaskPanesEarly();
}
