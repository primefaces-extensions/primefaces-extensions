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
package org.primefaces.extensions.component.creditcard;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>CreditCard</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "creditcard/creditcard.css")
@ResourceDependency(library = "primefaces-extensions", name = "creditcard/creditcard.js")
public class CreditCard extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CreditCard";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS = "ui-credit-card ";

    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CreditCardRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        width,
        formatting,
        labelValidDate,
        labelMonthYear,
        placeholderNumber,
        placeholderName,
        placeholderExpiry,
        placeholderCvc;
        // @formatter:on

        private String toString;

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    /**
     * Default constructor
     */
    public CreditCard() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public int getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, 350);
    }

    public void setWidth(final int _width) {
        getStateHelper().put(PropertyKeys.width, _width);
    }

    public boolean isFormatting() {
        return (Boolean) getStateHelper().eval(PropertyKeys.formatting, true);
    }

    public void setFromatting(final boolean _formatting) {
        getStateHelper().put(PropertyKeys.formatting, _formatting);
    }

    public String getLabelValidDate() {
        return (String) getStateHelper().eval(PropertyKeys.labelValidDate, "valid\\nthru");
    }

    public void setLabelValidDate(final String _labelValidDate) {
        getStateHelper().put(PropertyKeys.labelValidDate, _labelValidDate);
    }

    public String getLabelMonthYear() {
        return (String) getStateHelper().eval(PropertyKeys.labelMonthYear, "month/year");
    }

    public void setLabelMonthYear(final String _labelMonthYear) {
        getStateHelper().put(PropertyKeys.labelMonthYear, _labelMonthYear);
    }

    public String getPlaceholderNumber() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderNumber, null);
    }

    public void setPlaceholderNumber(final String _placeholderNumber) {
        getStateHelper().put(PropertyKeys.placeholderNumber, _placeholderNumber);
    }

    public String getPlaceholderName() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderName, "Full Name");
    }

    public void setPlaceholderName(final String _placeholderName) {
        getStateHelper().put(PropertyKeys.placeholderName, _placeholderName);
    }

    public String getPlaceholderExpiry() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderExpiry, null);
    }

    public void setPlaceholderExpiry(final String _placeholderExpiry) {
        getStateHelper().put(PropertyKeys.placeholderExpiry, _placeholderExpiry);
    }

    public String getPlaceholderCvc() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderCvc, null);
    }

    public void setPlaceholderCvc(final String _placeholderCvc) {
        getStateHelper().put(PropertyKeys.placeholderCvc, _placeholderCvc);
    }
}
