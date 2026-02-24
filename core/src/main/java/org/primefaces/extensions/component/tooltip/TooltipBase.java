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
package org.primefaces.extensions.component.tooltip;

import jakarta.faces.component.UIOutput;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;

/**
 * <code>Tooltip</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
public abstract class TooltipBase extends UIOutput implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Tooltip";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TooltipRenderer";

    public TooltipBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "A global tooltip converts each title attribute to a tooltip.", defaultValue = "false")
    public abstract boolean isGlobal();

    @Property(description = "A shared tooltip - one tooltip, multiple targets.", defaultValue = "false")
    public abstract boolean isShared();

    @Property(description = "This flag enables showing tooltips automatically after page loading. "
                + "Auto shown tooltips can not be global or shared.", defaultValue = "false")
    public abstract boolean isAutoShow();

    @Property(description = "This flag enables tooltip's positioning in relation to the mouse.", defaultValue = "false")
    public abstract boolean isMouseTracking();

    @Property(description = "When set to true, the tooltip will not hide if moused over, "
                + "allowing the contents to be clicked and interacted with.", defaultValue = "false")
    public abstract boolean isFixed();

    @Property(description = "Header or Titlebar of the tooltip. Not applicable on global tooltips.")
    public abstract String getHeader();

    @Property(description = "A positive or negative pixel value by which to offset the tooltip in the horizontal plane (x-axis). "
                + "Negative values cause a reduction in the value (moves tooltip to the left).",
                defaultValue = "0")
    public abstract int getAdjustX();

    @Property(description = "A positive or negative pixel value by which to offset the tooltip in the vertical plane (y-axis). "
                + "Negative values cause a reduction in the value (moves tooltip upwards).",
                defaultValue = "0")
    public abstract int getAdjustY();

    @Property(description = "The corner of the target element to position the tooltips corner at.", defaultValue = "bottom right")
    public abstract String getAtPosition();

    @Property(description = "The corner of the tooltip to position in relation to the target element.", defaultValue = "top left")
    public abstract String getMyPosition();

    @Property(description = "Event displaying the tooltip.", defaultValue = "mouseenter")
    public abstract String getShowEvent();

    @Property(description = "Delay time for displaying the tooltip.", defaultValue = "0")
    public abstract int getShowDelay();

    @Property(description = "Show effect function.", defaultValue = "fadeIn")
    public abstract String getShowEffect();

    @Property(description = "Duration for show effect.", defaultValue = "500")
    public abstract int getShowEffectLength();

    @Property(description = "Style class of the tooltip will override ThemeRoller theme.")
    public abstract String getStyleClass();

    @Property(description = "Event hiding the tooltip.", defaultValue = "mouseleave")
    public abstract String getHideEvent();

    @Property(description = "Delay time for hiding the tooltip.", defaultValue = "0")
    public abstract int getHideDelay();

    @Property(description = "Hide effect function.", defaultValue = "fadeOut")
    public abstract String getHideEffect();

    @Property(description = "Duration for hide effect.", defaultValue = "500")
    public abstract int getHideEffectLength();

    @Property(description = "Id of the component to attach the tooltip.")
    public abstract String getFor();
}
