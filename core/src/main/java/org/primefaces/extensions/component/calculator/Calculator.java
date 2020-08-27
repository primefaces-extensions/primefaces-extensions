/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.calculator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ButtonEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>Calculator</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "calculator/calculator.css")
@ResourceDependency(library = "primefaces-extensions", name = "calculator/calculator.js")
public class Calculator extends UIComponentBase implements ClientBehaviorHolder, Widget, RTLAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Calculator";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CalculatorRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(OpenEvent.NAME, CloseEvent.NAME, ButtonEvent.NAME));

    private Locale appropriateLocale;

    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        showOn,
        layout,
        locale,
        precision,
        dir,
        styleClass,
        onopen,
        onbutton,
        onclose,
        forValue("for");
        // @formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    /**
     * Default constructor
     */
    public Calculator() {
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
            final String clientId = this.getClientId(fc);
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
            else if (ButtonEvent.NAME.equals(eventName)) {
                final String name = params.get(clientId + "_button");
                final BigDecimal value = new BigDecimal(params.get(clientId + "_value"));
                final ButtonEvent buttonEvent = new ButtonEvent(this, behaviorEvent.getBehavior(), name, value);
                buttonEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(buttonEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
    }

    public String getShowOn() {
        return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
    }

    public void setShowOn(final String _showOn) {
        getStateHelper().put(PropertyKeys.showOn, _showOn);
    }

    public String getLayout() {
        return (String) getStateHelper().eval(PropertyKeys.layout, "standard");
    }

    public void setLayout(final String _layout) {
        getStateHelper().put(PropertyKeys.layout, _layout);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public int getPrecision() {
        return (Integer) getStateHelper().eval(PropertyKeys.precision, 10);
    }

    public void setPrecision(final int _precision) {
        getStateHelper().put(PropertyKeys.precision, _precision);
    }

    public void setDir(final String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
    }

    @Override
    public String getDir() {
        return (String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
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

    public String getOnbutton() {
        return (String) getStateHelper().eval(PropertyKeys.onbutton, null);
    }

    public void setOnbutton(final String _onButton) {
        getStateHelper().put(PropertyKeys.onbutton, _onButton);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

}
