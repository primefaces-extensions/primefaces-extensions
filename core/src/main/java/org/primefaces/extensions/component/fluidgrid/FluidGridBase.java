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
package org.primefaces.extensions.component.fluidgrid;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.api.AbstractDynamicData;
import org.primefaces.extensions.event.LayoutCompleteEvent;

/**
 * <code>FluidGrid</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 1.1.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "layoutComplete", event = LayoutCompleteEvent.class,
                        description = "Fires when the layout is complete.", defaultEvent = true)
})
public abstract class FluidGridBase extends AbstractDynamicData implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FluidGrid";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FluidGridRenderer";

    public FluidGridBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Name of the client-side widget variable.")
    public abstract String getWidgetVar();

    @Property(description = "Inline style for the grid.")
    public abstract String getStyle();

    @Property(description = "Style class for the grid.")
    public abstract String getStyleClass();

    @Property(description = "Horizontal gutter between items in pixels.", defaultValue = "0")
    public abstract int getHGutter();

    @Property(description = "Vertical gutter between items in pixels.", defaultValue = "0")
    public abstract int getVGutter();

    @Property(description = "When true, grid fits within the container width.", defaultValue = "false")
    public abstract boolean isFitWidth();

    @Property(description = "When true, items are laid out from left to right.", defaultValue = "true")
    public abstract boolean isOriginLeft();

    @Property(description = "When true, items are laid out from top to bottom.", defaultValue = "true")
    public abstract boolean isOriginTop();

    @Property(description = "When true, layout is recalculated on window resize.", defaultValue = "true")
    public abstract boolean isResizeBound();

    @Property(description = "Search expression for element(s) to stamp - hidden during layout.")
    public abstract String getStamp();

    @Property(description = "CSS transition duration for layout changes.", defaultValue = "0.4s")
    public abstract String getTransitionDuration();

    @Property(description = "When true, images are taken into account for layout (wait for load).", defaultValue = "false")
    public abstract boolean isHasImages();
}
