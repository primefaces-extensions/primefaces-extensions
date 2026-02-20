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
package org.primefaces.extensions.component.keynote;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.api.AbstractDynamicData;
import org.primefaces.extensions.event.KeynoteEvent;

/**
 * <code>Keynote</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 6.3
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "slideTransitionEnd", event = KeynoteEvent.class,
                        description = "Fires when a slide transition ends."),
            @FacesBehaviorEvent(name = "slideChanged", event = KeynoteEvent.class,
                        description = "Fires when the slide changes.", defaultEvent = true)
})
public abstract class KeynoteBase extends AbstractDynamicData implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Keynote";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.KeynoteRenderer";

    public KeynoteBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Presentation width in pixels.", defaultValue = "960")
    public abstract int getWidth();

    @Property(description = "Presentation height in pixels.", defaultValue = "700")
    public abstract int getHeight();

    @Property(description = "Factor of the display size that should remain empty around the content.", defaultValue = "0.04")
    public abstract Double getMargin();

    @Property(description = "Minimum scale factor.", defaultValue = "0.2")
    public abstract Double getMinScale();

    @Property(description = "Maximum scale factor.", defaultValue = "2.0")
    public abstract Double getMaxScale();

    @Property(description = "Auto-slide interval in milliseconds; 0 disables.", defaultValue = "0")
    public abstract int getAutoSlide();

    @Property(description = "Whether to vertically center the content.", defaultValue = "true")
    public abstract boolean isCenter();

    @Property(description = "Whether to show navigation controls.", defaultValue = "true")
    public abstract boolean isControls();

    @Property(description = "Whether to disable layout changes.", defaultValue = "false")
    public abstract boolean isDisableLayout();

    @Property(description = "Whether the presentation is embedded (e.g. in an iframe).", defaultValue = "false")
    public abstract boolean isEmbedded();

    @Property(description = "Whether to loop the presentation.", defaultValue = "false")
    public abstract boolean isLoop();

    @Property(description = "Navigation mode: 'default' or 'linear'.", defaultValue = "default")
    public abstract String getNavigationMode();

    @Property(description = "Whether to show progress bar.", defaultValue = "true")
    public abstract boolean isProgress();

    @Property(description = "Whether to show speaker notes.", defaultValue = "false")
    public abstract boolean isShowNotes();

    @Property(description = "Slide number display: 'true', 'false', or a CSS selector.", defaultValue = "false")
    public abstract String getSlideNumber();

    @Property(description = "Whether to enable touch navigation.", defaultValue = "true")
    public abstract boolean isTouch();

    @Property(description = "Transition style: 'none', 'fade', 'slide', 'convex', 'concave', 'zoom'.", defaultValue = "slide")
    public abstract String getTransition();

    @Property(description = "Transition speed: 'default', 'fast', 'slow'.", defaultValue = "default")
    public abstract String getTransitionSpeed();

    @Property(description = "Background transition: 'fade', 'slide', 'convex', 'concave', 'zoom'.", defaultValue = "fade")
    public abstract String getBackgroundTransition();

    @Property(description = "Theme name; 'none' to disable.", defaultValue = "none")
    public abstract String getTheme();

    @Property(description = "Resource library for theme CSS.", defaultValue = "primefaces-extensions")
    public abstract String getLibrary();
}
