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
package org.primefaces.extensions.component.lightswitch;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;

/**
 * <code>LightSwitch</code> component. Automatically switches to defined dark mode theme based on OS settings.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@FacesComponent(value = LightSwitch.COMPONENT_TYPE, namespace = LightSwitch.COMPONENT_FAMILY)
@FacesComponentInfo(description = "LightSwitch automatically switches to defined dark mode theme based on OS settings.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "lightswitch/lightswitch.js")
public class LightSwitch extends LightSwitchBaseImpl {

    public static final String EVENT_SWITCH = "switch";

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isAjaxBehaviorEventSource(event)) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys._switch)) {
                final String theme = params.get(getClientId(context) + "_theme");
                final SelectEvent<String> selectEvent = new SelectEvent<>(this, behaviorEvent.getBehavior(), theme);
                selectEvent.setPhaseId(behaviorEvent.getPhaseId());
                setSelectedByValueExpression(context, theme);
                super.queueEvent(selectEvent);
                return;
            }
        }

        super.queueEvent(event);
    }

    protected void setSelectedByValueExpression(final FacesContext context, final String selected) {
        if (getValueExpression(PropertyKeys.selected.name()) != null) {
            getValueExpression(PropertyKeys.selected.name()).setValue(context.getELContext(), selected);
        }
        else {
            setSelected(selected);
        }
    }

}
