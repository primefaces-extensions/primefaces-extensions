/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.util.Constants;

/**
 * <code>SlideOut</code> component.
 *
 * @author Melloware info@melloware.com
 * @since 6.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "slideout/slideout.css")
@ResourceDependency(library = "primefaces-extensions", name = "slideout/slideout.js")
public class SlideOut extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String HANDLE_CLASS = "ui-slideout-handle ui-slideouttab-handle-rounded";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SlideOut";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SlideOutRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(OpenEvent.NAME, CloseEvent.NAME));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
      widgetVar,
      panelStyle,
      panelStyleClass,
      handleStyle,
      handleStyleClass,
      title,
      icon,
      showOn, // click or hover
      location, // left, right, top or bottom
      offset, // panel distance from edge
      offsetReverse,  // if true, panel is aligned with right or bottom of window
      handleOffset, // panel distance from edge
      handleOffsetReverse,  // if true, panel is aligned with right or bottom of window
      sticky, // fixed or absolute
      clickScreenToClose,
      autoOpen, // slide out after DOM load
      animateSpeed,
      bounceDistance, // how far bounce event will move everything
      bounceTimes, // how many bounces when 'bounce' is called
      onopen, // handler called after opening
      onclose, // handler called after closing
      onslide, // handler called after opening or closing
      onbeforeopen, // handler called before opening, return false to cancel
      onbeforeclose, // handler called before closing, return false to cancel
      onbeforeslide // handler called before opening or closing, return false to cancel
      // @formatter:on
    }

    /**
     * Default constructor
     */
    public SlideOut() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultEventName() {
        return OpenEvent.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (OpenEvent.NAME.equals(eventName)) {
                final OpenEvent openEvent = new OpenEvent(this, behaviorEvent.getBehavior());
                openEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(openEvent);

                return;
            }
            else if (CloseEvent.NAME.equals(eventName)) {
                final CloseEvent closeEvent = new CloseEvent(this, behaviorEvent.getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getPanelStyle() {
        return (String) getStateHelper().eval(PropertyKeys.panelStyle, null);
    }

    public void setPanelStyle(final String _panelStyle) {
        getStateHelper().put(PropertyKeys.panelStyle, _panelStyle);
    }

    public String getPanelStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.panelStyleClass, null);
    }

    public void setPanelStyleClass(final String _panelStyleClass) {
        getStateHelper().put(PropertyKeys.panelStyleClass, _panelStyleClass);
    }

    public String getHandleStyle() {
        return (String) getStateHelper().eval(PropertyKeys.handleStyle, null);
    }

    public void setHandleStyle(final String _handleStyle) {
        getStateHelper().put(PropertyKeys.handleStyle, _handleStyle);
    }

    public String getHandleStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.handleStyleClass, null);
    }

    public void setHandleStyleClass(final String _handleStyleClass) {
        getStateHelper().put(PropertyKeys.handleStyleClass, _handleStyleClass);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(final String _title) {
        getStateHelper().put(PropertyKeys.title, _title);
    }

    public String getIcon() {
        return (String) getStateHelper().eval(PropertyKeys.icon, null);
    }

    public void setIcon(final String _icon) {
        getStateHelper().put(PropertyKeys.icon, _icon);
    }

    public String getShowOn() {
        return (String) getStateHelper().eval(PropertyKeys.showOn, "click");
    }

    public void setShowOn(final String _showOn) {
        getStateHelper().put(PropertyKeys.showOn, _showOn);
    }

    public String getLocation() {
        return (String) getStateHelper().eval(PropertyKeys.location, "right");
    }

    public void setLocation(final String _location) {
        getStateHelper().put(PropertyKeys.location, _location);
    }

    public String getOffset() {
        return (String) getStateHelper().eval(PropertyKeys.offset, "200px");
    }

    public void setOffset(final String _offset) {
        getStateHelper().put(PropertyKeys.offset, _offset);
    }

    public void setOffsetReverse(final boolean _offsetReverse) {
        getStateHelper().put(PropertyKeys.offsetReverse, _offsetReverse);
    }

    public boolean isOffsetReverse() {
        return (Boolean) getStateHelper().eval(PropertyKeys.offsetReverse, false);
    }

    public String getHandleOffset() {
        return (String) getStateHelper().eval(PropertyKeys.handleOffset, null);
    }

    public void setHandleOffset(final String _handleOffset) {
        getStateHelper().put(PropertyKeys.handleOffset, _handleOffset);
    }

    public void setHandleOffsetReverse(final boolean _handleOffsetReverse) {
        getStateHelper().put(PropertyKeys.handleOffsetReverse, _handleOffsetReverse);
    }

    public boolean isHandleOffsetReverse() {
        return (Boolean) getStateHelper().eval(PropertyKeys.handleOffsetReverse, false);
    }

    public void setSticky(final boolean _sticky) {
        getStateHelper().put(PropertyKeys.sticky, _sticky);
    }

    public boolean isSticky() {
        return (Boolean) getStateHelper().eval(PropertyKeys.sticky, false);
    }

    public void setClickScreenToClose(final boolean _clickScreenToClose) {
        getStateHelper().put(PropertyKeys.clickScreenToClose, _clickScreenToClose);
    }

    public boolean isClickScreenToClose() {
        return (Boolean) getStateHelper().eval(PropertyKeys.clickScreenToClose, true);
    }

    public void setAutoOpen(final boolean _autoOpen) {
        getStateHelper().put(PropertyKeys.autoOpen, _autoOpen);
    }

    public boolean isAutoOpen() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoOpen, false);
    }

    public int getAnimateSpeed() {
        return (Integer) getStateHelper().eval(PropertyKeys.animateSpeed, 300);
    }

    public void setAnimateSpeed(final int _animateSpeed) {
        getStateHelper().put(PropertyKeys.animateSpeed, _animateSpeed);
    }

    public String getBounceDistance() {
        return (String) getStateHelper().eval(PropertyKeys.bounceDistance, "50px");
    }

    public void setBounceDistance(final String _offset) {
        getStateHelper().put(PropertyKeys.bounceDistance, _offset);
    }

    public int getBounceTimes() {
        return (Integer) getStateHelper().eval(PropertyKeys.bounceTimes, 4);
    }

    public void setBounceTimes(final int _bounceTimes) {
        getStateHelper().put(PropertyKeys.bounceTimes, _bounceTimes);
    }

    public String getOnopen() {
        return (String) getStateHelper().eval(PropertyKeys.onopen, null);
    }

    public void setOnopen(final String _onOpen) {
        getStateHelper().put(PropertyKeys.onopen, _onOpen);
    }

    public String getOnclose() {
        return (String) getStateHelper().eval(PropertyKeys.onclose, null);
    }

    public void setOnclose(final String _onClose) {
        getStateHelper().put(PropertyKeys.onclose, _onClose);
    }

    public String getOnslide() {
        return (String) getStateHelper().eval(PropertyKeys.onslide, null);
    }

    public void setOnslide(final String _onslide) {
        getStateHelper().put(PropertyKeys.onslide, _onslide);
    }

    public String getOnbeforeopen() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeopen, null);
    }

    public void setOnbeforeopen(final String _onBeforeOpen) {
        getStateHelper().put(PropertyKeys.onbeforeopen, _onBeforeOpen);
    }

    public String getOnbeforeclose() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeclose, null);
    }

    public void setOnbeforeclose(final String _onbeforeClose) {
        getStateHelper().put(PropertyKeys.onbeforeclose, _onbeforeClose);
    }

    public String getOnbeforeslide() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeslide, null);
    }

    public void setOnbeforeslide(final String _onbeforeslide) {
        getStateHelper().put(PropertyKeys.onbeforeslide, _onbeforeslide);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

}
