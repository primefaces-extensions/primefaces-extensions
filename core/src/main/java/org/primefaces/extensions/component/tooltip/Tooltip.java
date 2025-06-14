/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIOutput;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.util.Constants;

/**
 * <code>Tooltip</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "tooltip/tooltip.css")
@ResourceDependency(library = Constants.LIBRARY, name = "tooltip/tooltip.js")
public class Tooltip extends UIOutput implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Tooltip";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TooltipRenderer";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        global,
        shared,
        autoShow,
        mouseTracking,
        fixed,
        header,
        adjustX,
        adjustY,
        atPosition,
        myPosition,
        showEvent,
        showDelay,
        showEffect,
        showEffectLength,
        styleClass,
        hideEvent,
        hideDelay,
        hideEffect,
        hideEffectLength,
        forValue("for");
        //@formatter:on

        private final String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
            toString = null;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public Tooltip() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public boolean isGlobal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.global, false);
    }

    public void setGlobal(final boolean global) {
        getStateHelper().put(PropertyKeys.global, global);
    }

    public boolean isShared() {
        return (Boolean) getStateHelper().eval(PropertyKeys.shared, false);
    }

    public void setShared(final boolean shared) {
        getStateHelper().put(PropertyKeys.shared, shared);
    }

    public boolean isAutoShow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
    }

    public void setAutoShow(final boolean autoShow) {
        getStateHelper().put(PropertyKeys.autoShow, autoShow);
    }

    public boolean isMouseTracking() {
        return (Boolean) getStateHelper().eval(PropertyKeys.mouseTracking, false);
    }

    public void setMouseTracking(final boolean mouseTracking) {
        getStateHelper().put(PropertyKeys.mouseTracking, mouseTracking);
    }

    public boolean isFixed() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fixed, false);
    }

    public void setFixed(final boolean fixed) {
        getStateHelper().put(PropertyKeys.fixed, fixed);
    }

    public int getAdjustX() {
        return (Integer) getStateHelper().eval(PropertyKeys.adjustX, 0);
    }

    public void setAdjustX(final int adjustX) {
        getStateHelper().put(PropertyKeys.adjustX, adjustX);
    }

    public int getAdjustY() {
        return (Integer) getStateHelper().eval(PropertyKeys.adjustY, 0);
    }

    public void setAdjustY(final int adjustY) {
        getStateHelper().put(PropertyKeys.adjustY, adjustY);
    }

    public String getAtPosition() {
        return (String) getStateHelper().eval(PropertyKeys.atPosition, "bottom right");
    }

    public void setAtPosition(final String atPosition) {
        getStateHelper().put(PropertyKeys.atPosition, atPosition);
    }

    public String getMyPosition() {
        return (String) getStateHelper().eval(PropertyKeys.myPosition, "top left");
    }

    public void setMyPosition(final String myPosition) {
        getStateHelper().put(PropertyKeys.myPosition, myPosition);
    }

    public String getShowEvent() {
        return (String) getStateHelper().eval(PropertyKeys.showEvent, "mouseenter");
    }

    public void setShowEvent(final String showEvent) {
        getStateHelper().put(PropertyKeys.showEvent, showEvent);
    }

    public int getShowDelay() {
        return (Integer) getStateHelper().eval(PropertyKeys.showDelay, 0);
    }

    public void setShowDelay(final int showDelay) {
        getStateHelper().put(PropertyKeys.showDelay, showDelay);
    }

    public String getShowEffect() {
        return (String) getStateHelper().eval(PropertyKeys.showEffect, "fadeIn");
    }

    public void setShowEffect(final String showEffect) {
        getStateHelper().put(PropertyKeys.showEffect, showEffect);
    }

    public int getShowEffectLength() {
        return (Integer) getStateHelper().eval(PropertyKeys.showEffectLength, 500);
    }

    public void setShowEffectLength(final int showEffectLength) {
        getStateHelper().put(PropertyKeys.showEffectLength, showEffectLength);
    }

    public String getHideEvent() {
        return (String) getStateHelper().eval(PropertyKeys.hideEvent, "mouseleave");
    }

    public void setHideEvent(final String hideEvent) {
        getStateHelper().put(PropertyKeys.hideEvent, hideEvent);
    }

    public int getHideDelay() {
        return (Integer) getStateHelper().eval(PropertyKeys.hideDelay, 0);
    }

    public void setHideDelay(final int hideDelay) {
        getStateHelper().put(PropertyKeys.hideDelay, hideDelay);
    }

    public String getHideEffect() {
        return (String) getStateHelper().eval(PropertyKeys.hideEffect, "fadeOut");
    }

    public void setHideEffect(final String hideEffect) {
        getStateHelper().put(PropertyKeys.hideEffect, hideEffect);
    }

    public int getHideEffectLength() {
        return (Integer) getStateHelper().eval(PropertyKeys.hideEffectLength, 500);
    }

    public void setHideEffectLength(final int hideEffectLength) {
        getStateHelper().put(PropertyKeys.hideEffectLength, hideEffectLength);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String forValue) {
        getStateHelper().put(PropertyKeys.forValue, forValue);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getHeader() {
        return (String) getStateHelper().eval(PropertyKeys.header);
    }

    public void setHeader(final String header) {
        getStateHelper().put(PropertyKeys.header, header);
    }

}
