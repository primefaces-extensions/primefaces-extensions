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
package org.primefaces.extensions.component.imageareaselect;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ImageAreaSelectEvent;

/**
 * Component base class for the <code>ImageAreaSelect</code> component.
 *
 * @author Thomas Andraschko
 * @since 0.1
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "selectEnd", event = ImageAreaSelectEvent.class,
                        description = "Fires when area selection ends.", defaultEvent = true),
            @FacesBehaviorEvent(name = "selectStart", event = ImageAreaSelectEvent.class,
                        description = "Fires when area selection starts."),
            @FacesBehaviorEvent(name = "selectChange", event = ImageAreaSelectEvent.class,
                        description = "Fires when area selection changes.")
})
public abstract class ImageAreaSelectBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageAreaSelect";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageAreaSelectRenderer";

    public ImageAreaSelectBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Search expression for the target image to attach the area selector to.")
    public abstract String getFor();

    @Property(description = "Aspect ratio of the selection (e.g. '1:1').")
    public abstract String getAspectRatio();

    @Property(description = "Whether to auto-hide the selection.")
    public abstract Boolean getAutoHide();

    @Property(description = "Fade speed in ms.")
    public abstract Integer getFadeSpeed();

    @Property(description = "Whether to show resize handles.")
    public abstract Boolean getHandles();

    @Property(description = "Whether to hide the selection on click outside.")
    public abstract Boolean getHide();

    @Property(description = "Image height for coordinate mapping.")
    public abstract Integer getImageHeight();

    @Property(description = "Image width for coordinate mapping.")
    public abstract Integer getImageWidth();

    @Property(description = "Whether the selection is movable.")
    public abstract Boolean getMovable();

    @Property(description = "Whether the selection is persistent.")
    public abstract Boolean getPersistent();

    @Property(description = "Whether the selection is resizable.")
    public abstract Boolean getResizable();

    @Property(description = "Whether to show the selection on init.")
    public abstract Boolean getShow();

    @Property(description = "z-index of the selection overlay.")
    public abstract Integer getZIndex();

    @Property(description = "Maximum selection height.")
    public abstract Integer getMaxHeight();

    @Property(description = "Maximum selection width.")
    public abstract Integer getMaxWidth();

    @Property(description = "Minimum selection height.")
    public abstract Integer getMinHeight();

    @Property(description = "Minimum selection width.")
    public abstract Integer getMinWidth();

    @Property(description = "Parent selector for the widget.")
    public abstract String getParentSelector();

    @Property(description = "Whether keyboard support is enabled.")
    public abstract Boolean getKeyboardSupport();
}
