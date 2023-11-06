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
package org.primefaces.extensions.component.lightswitch;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>LightSwitch</code> component. Automatically switches to defined dark mode theme based on OS settings.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "lightswitch/lightswitch.js")
public class LightSwitch extends UIComponentBase implements Widget, ClientBehaviorHolder, MixedClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LightSwitch";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String EVENT_SWITCH = "switch";

    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LightSwitchRenderer";
    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList(EVENT_SWITCH);

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        widgetVar,
        selected,
        light,
        dark,
        automatic
    }
    // @formatter:on

    /**
     * Default constructor
     */
    public LightSwitch() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_SWITCH;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getSelected() {
        return (String) getStateHelper().eval(PropertyKeys.selected, null);
    }

    public void setSelected(final String selected) {
        getStateHelper().put(PropertyKeys.selected, selected);
    }

    public void setSelectedByValueExpression(final FacesContext context, final String selected) {
        this.getValueExpression(PropertyKeys.selected.name()).setValue(context.getELContext(), selected);
    }

    public String getLight() {
        return (String) getStateHelper().eval(PropertyKeys.light, "saga");
    }

    public void setLight(final String light) {
        getStateHelper().put(PropertyKeys.light, light);
    }

    public String getDark() {
        return (String) getStateHelper().eval(PropertyKeys.dark, "arya");
    }

    public void setDark(final String dark) {
        getStateHelper().put(PropertyKeys.dark, dark);
    }

    public boolean isAutomatic() {
        return (boolean) getStateHelper().eval(PropertyKeys.automatic, true);
    }

    public void setAutomatic(final boolean automatic) {
        getStateHelper().put(PropertyKeys.automatic, automatic);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public Collection<String> getUnobstrusiveEventNames() {
        return getEventNames();
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (EVENT_SWITCH.equals(eventName)) {
                final String theme = params.get(getClientId(context) + "_theme");
                final SelectEvent<String> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), theme);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                setSelectedByValueExpression(context, theme);
                super.queueEvent(selectEvent);
            }
        }
    }

}