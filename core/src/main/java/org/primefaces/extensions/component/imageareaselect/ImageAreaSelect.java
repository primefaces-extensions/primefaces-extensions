/*
 * Copyright 2011-2021 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.imageareaselect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageAreaSelect</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.css")
@ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.js")
public class ImageAreaSelect extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageAreaSelect";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String EVENT_SELECT_END = "selectEnd";
    public static final String EVENT_SELECT_START = "selectStart";
    public static final String EVENT_SELECT_CHANGE = "selectChange";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageAreaSelectRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(EVENT_SELECT_END, EVENT_SELECT_START, EVENT_SELECT_CHANGE));

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        forValue("for"),
        aspectRatio,
        autoHide,
        fadeSpeed,
        handles,
        hide,
        imageHeight,
        imageWidth,
        movable,
        persistent,
        resizable,
        show,
        zIndex,
        maxHeight,
        maxWidth,
        minHeight,
        minWidth,
        parentSelector,
        keyboardSupport;
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

    public ImageAreaSelect() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_SELECT_END;
    }

    public String getAspectRatio() {
        return (String) getStateHelper().eval(PropertyKeys.aspectRatio, null);
    }

    public void setAspectRatio(final String aspectRatio) {
        getStateHelper().put(PropertyKeys.aspectRatio, aspectRatio);
    }

    public String getParentSelector() {
        return (String) getStateHelper().eval(PropertyKeys.parentSelector, null);
    }

    public void setParentSelector(final String parentSelector) {
        getStateHelper().put(PropertyKeys.parentSelector, parentSelector);
    }

    public Boolean isAutoHide() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoHide, null);
    }

    public void setAutoHide(final Boolean autoHide) {
        getStateHelper().put(PropertyKeys.autoHide, autoHide);
    }

    public Integer getFadeSpeed() {
        return (Integer) getStateHelper().eval(PropertyKeys.fadeSpeed, null);
    }

    public void setFadeSpeed(final Integer fadeSpeed) {
        getStateHelper().put(PropertyKeys.fadeSpeed, fadeSpeed);
    }

    public Boolean isHandles() {
        return (Boolean) getStateHelper().eval(PropertyKeys.handles, null);
    }

    public void setHandles(final Boolean handles) {
        getStateHelper().put(PropertyKeys.handles, handles);
    }

    public Boolean isHide() {
        return (Boolean) getStateHelper().eval(PropertyKeys.hide, null);
    }

    public void setHide(final Boolean hide) {
        getStateHelper().put(PropertyKeys.hide, hide);
    }

    public Integer getImageHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.imageHeight, null);
    }

    public void setImageHeight(final Integer imageHeight) {
        getStateHelper().put(PropertyKeys.imageHeight, imageHeight);
    }

    public Integer getImageWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.imageWidth, null);
    }

    public void setImageWidth(final Integer imageWidth) {
        getStateHelper().put(PropertyKeys.imageWidth, imageWidth);
    }

    public Boolean isMovable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.movable, null);
    }

    public void setMovable(final Boolean movable) {
        getStateHelper().put(PropertyKeys.movable, movable);
    }

    public Boolean isKeyboardSupport() {
        return (Boolean) getStateHelper().eval(PropertyKeys.keyboardSupport, null);
    }

    public void setKeyboardSupport(final Boolean keyboardSupport) {
        getStateHelper().put(PropertyKeys.keyboardSupport, keyboardSupport);
    }

    public Boolean isPersistent() {
        return (Boolean) getStateHelper().eval(PropertyKeys.persistent, null);
    }

    public void setPersistent(final Boolean persistent) {
        getStateHelper().put(PropertyKeys.persistent, persistent);
    }

    public Boolean isResizable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizable, null);
    }

    public void setResizable(final Boolean resizable) {
        getStateHelper().put(PropertyKeys.resizable, resizable);
    }

    public Boolean isShow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.show, null);
    }

    public void setShow(final Boolean show) {
        getStateHelper().put(PropertyKeys.show, show);
    }

    public Integer getZIndex() {
        return (Integer) getStateHelper().eval(PropertyKeys.zIndex, null);
    }

    public void setZIndex(final Integer zIndex) {
        getStateHelper().put(PropertyKeys.zIndex, zIndex);
    }

    public Integer getMaxHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxHeight, null);
    }

    public void setMaxHeight(final Integer maxHeight) {
        getStateHelper().put(PropertyKeys.maxHeight, maxHeight);
    }

    public Integer getMaxWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxWidth, null);
    }

    public void setMaxWidth(final Integer maxWidth) {
        getStateHelper().put(PropertyKeys.maxWidth, maxWidth);
    }

    public Integer getMinHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.minHeight, null);
    }

    public void setMinHeight(final Integer minHeight) {
        getStateHelper().put(PropertyKeys.minHeight, minHeight);
    }

    public Integer getMinWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.minWidth, null);
    }

    public void setMinWidth(final Integer minWidth) {
        getStateHelper().put(PropertyKeys.minWidth, minWidth);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String forValue) {
        getStateHelper().put(PropertyKeys.forValue, forValue);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        if (isRequestSource(clientId, params)) {
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if (eventName.equals(EVENT_SELECT_END)
                        || eventName.equals(EVENT_SELECT_CHANGE)
                        || eventName.equals(EVENT_SELECT_START)) {

                final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

                final int x1 = Integer.parseInt(params.get(clientId + "_x1"));
                final int x2 = Integer.parseInt(params.get(clientId + "_x2"));
                final int y1 = Integer.parseInt(params.get(clientId + "_y1"));
                final int y2 = Integer.parseInt(params.get(clientId + "_y2"));
                final int height = Integer.parseInt(params.get(clientId + "_height"));
                final int width = Integer.parseInt(params.get(clientId + "_width"));
                final int imgHeight = Integer.parseInt(params.get(clientId + "_imgHeight"));
                final int imgWidth = Integer.parseInt(params.get(clientId + "_imgWidth"));
                final String imgSrc = params.get(clientId + "_imgSrc");

                final ImageAreaSelectEvent selectEvent = new ImageAreaSelectEvent(this, behaviorEvent.getBehavior(), height, width, x1, x2, y1, y2, imgHeight,
                            imgWidth, imgSrc);

                super.queueEvent(selectEvent);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    private static boolean isRequestSource(final String clientId, final Map<String, String> params) {
        return clientId.equals(params.get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }
}
