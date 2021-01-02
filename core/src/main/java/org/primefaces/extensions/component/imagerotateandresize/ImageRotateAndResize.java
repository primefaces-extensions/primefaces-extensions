/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.imagerotateandresize;

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
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageRotateAndResize</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class ImageRotateAndResize extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageRotateAndResize";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String EVENT_ROTATE = "rotate";
    public static final String EVENT_RESIZE = "resize";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageRotateAndResizeRenderer";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(EVENT_ROTATE, EVENT_RESIZE));

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        widgetVar, forValue("for");

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

    public ImageRotateAndResize() {
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

            final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

            if (eventName.equals(EVENT_RESIZE)) {
                final double width = Double.parseDouble(params.get(clientId + "_width"));
                final double height = Double.parseDouble(params.get(clientId + "_height"));

                final ResizeEvent resizeEvent = new ResizeEvent(this, behaviorEvent.getBehavior(), width, height);

                super.queueEvent(resizeEvent);
            }
            else if (eventName.equals(EVENT_ROTATE)) {
                final int degree = Integer.parseInt(params.get(clientId + "_degree"));

                final RotateEvent rotateEvent = new RotateEvent(this, behaviorEvent.getBehavior(), degree);

                super.queueEvent(rotateEvent);
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
