/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.imagerotateandresize;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageRotateAndResize</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
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
    protected enum PropertyKeys {

        widgetVar, forValue("for");

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
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

    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
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

    private boolean isRequestSource(final String clientId, final Map<String, String> params) {
        return clientId.equals(params.get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }
}
